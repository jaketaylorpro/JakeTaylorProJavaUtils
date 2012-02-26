package com.jtaylor.util.gui;

import com.jtaylor.util.SystemOperations;
import com.jtaylor.util.gui.rwindow.RDialog;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.Element;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: Apr 23, 2010
 * Time: 1:06:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileInputPanel extends JPanel implements ActionListener, DocumentListener
{
   private List<DocumentListener> myListeners;
	private JTextField myTextField;
	private JButton myButton;
	private File mySelectedFile;
	private JFileChooser browserWindow;
	private boolean myCanBeFile;
	private boolean myCanBeDirectory;
	private boolean myNeedsToBeWritable;
	private boolean myNeedsToBeReadable;
	public FileInputPanel()
	{
		this(null,true,true,true,false);
	}
	public FileInputPanel(File defaultSelection)
	{
		this(defaultSelection,true,true,true,false);
	}
	public FileInputPanel(File defaultSelection,boolean canBeFile,boolean canBeDirectory,boolean needsToBeReadable,boolean needsToBeWritable)
	{
		super(new BorderLayout());
      myListeners=new Vector<DocumentListener>();
		mySelectedFile=defaultSelection;
		myCanBeFile=canBeFile;
		myCanBeDirectory=canBeDirectory;
		myNeedsToBeReadable=needsToBeReadable;
		myNeedsToBeWritable=needsToBeWritable;
		myTextField=new JTextField();
		myTextField.setText(mySelectedFile!=null?mySelectedFile.getAbsolutePath():"");
		myTextField.getDocument().addDocumentListener(this);
		evalTextField();
		myButton=new JButton("Browse");
		myButton.addActionListener(this);
		browserWindow=new JFileChooser();
		if(myCanBeDirectory&&myCanBeFile)
		{
			browserWindow.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		}
		else if(myCanBeDirectory)
		{
			browserWindow.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		else
		{
			browserWindow.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		add(myButton,BorderLayout.EAST);
		add(myTextField,BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e)
	{
		if(mySelectedFile!=null)
		{
			browserWindow.setCurrentDirectory(mySelectedFile);
		}
		else
		{
			browserWindow.setCurrentDirectory(SystemOperations.getHomeFoler());
		}
		int selection=browserWindow.showOpenDialog(this);
		if(selection==JFileChooser.APPROVE_OPTION)
		{
			File f=browserWindow.getSelectedFile();
			myTextField.setText(f!=null?f.getAbsolutePath():"");
//         for(DocumentListener l:myListeners)
//         {
//         }
		}
	}

	public void insertUpdate(DocumentEvent e)
	{
		evalTextField();
	}

	public void removeUpdate(DocumentEvent e)
	{
		evalTextField();
	}

	public void changedUpdate(DocumentEvent e)
	{
		evalTextField();
	}
	private void evalTextField()
	{
		String text=myTextField.getText();
		if(text!=null&&!text.equals(""))
		{
			File f=new File(text);
			if(myNeedsToBeReadable&&!f.canRead())
			{
				mySelectedFile=null;
				myTextField.setForeground(Color.red);
			}
			else if(myNeedsToBeWritable&&!f.canWrite())
			{
				mySelectedFile=null;
				myTextField.setForeground(Color.red);
			}
			else
			{
				if((f.isDirectory()&&!myCanBeDirectory)
					||(!f.isDirectory()&&!myCanBeFile))
				{
					mySelectedFile=null;
					myTextField.setForeground(Color.red);
				}
				else
				{
					mySelectedFile=f;
					myTextField.setForeground(Color.green);
				}
			}
		}
		else
		{
			text=null;
			myTextField.setForeground(Color.red);
		}
      for(DocumentListener l:myListeners)
      {
         l.changedUpdate(new DocumentEvent()
         {
            @Override
            public int getOffset()
            {
               return 0;
            }

            @Override
            public int getLength()
            {
               return 0;
            }

            @Override
            public Document getDocument()
            {
               return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public EventType getType()
            {
               return EventType.CHANGE;
            }

            @Override
            public ElementChange getChange(Element elem)
            {
               return null;
            }
         });
      }
	}

	public boolean isInputValid()
	{
		return mySelectedFile!=null;
	}
	public File getSelectedFile()
	{
		return mySelectedFile;
	}
	public void setText(String text)
	{
		myTextField.setText(text);
	}
	public boolean isEmpty()
	{
		return myTextField.getText()==null||myTextField.getText().equals("");
	}
	public String getText()
	{
		return myTextField.getText();
	}
   public void addListener(DocumentListener l)
   {
//      myTextField.getDocument().addDocumentListener(l);
      myListeners.add(l);
   }
	public static void main(String[] args)
	{
		RegexInputField panel=new RegexInputField();
		RDialog dialog=new RDialog(panel);
		dialog.prompt();
		System.out.println("text: "+panel.getText());
	}
}
