package com.jtaylor.util.gui.rwindow;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jtaylor.util.gui.rwindow.ActionHelper.ActionEnabler;
import com.jtaylor.util.gui.rwindow.ActionHelper.ActionPerformer;


public class RCheckingWizBar extends RWizBar implements ItemListener,ChangeListener,ActionListener,DocumentListener
{
	protected ActionEnabler[] myEnablers;
	private ActionEnabler oldNextEnabler;
	public static void main(String[] args)
	{
		JPanel panel1=new JPanel();
		JPanel panel3=new JPanel();
		JPanel panel2=new JPanel();
		final JTextField textPane1=new JTextField(15);
		final JTextField textPane2=new JTextField(15);
		final JTextField textPane3=new JTextField(15);
		panel1.add(textPane1);
		panel2.add(textPane2);
		panel3.add(textPane3);
		Component[] components=new Component[3];
		ActionEnabler[] enablers=new ActionEnabler[3];
		components[0]=panel1;
		enablers[0]=new ActionEnabler()
		{
			public boolean isEnabled()
            {
	            return textPane1.getText()!=null&&textPane1.getText().length()>0;
            }
		};
        components[1]=panel2;
		enablers[1]=new ActionEnabler()
		{
			public boolean isEnabled()
            {
	            return textPane2.getText()!=null&&textPane2.getText().length()>0;
            }
		};
        components[2]=panel3;
		enablers[2]=new ActionEnabler()
		{
			public boolean isEnabled()
            {
	            return textPane3.getText()!=null&&textPane3.getText().length()>0;
            }
		};
		ActionPerformer finish=new ActionPerformer()
		{
		
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("finished");
			}
		};
        RCheckingWizBar bar=new RCheckingWizBar(finish,components,enablers);
        textPane1.addActionListener(bar);
        textPane1.getDocument().addDocumentListener(bar);
        textPane2.addActionListener(bar);
        textPane2.getDocument().addDocumentListener(bar);
        textPane3.addActionListener(bar);
        textPane3.getDocument().addDocumentListener(bar);
        RDialog window=new RDialog("checkingWizardTest",components[0],bar);
        window.prompt();
        System.out.println(window.pressedOk());
	}
	public RCheckingWizBar(ActionPerformer finish,Component[] components,ActionEnabler[] enablers)
	{
		super(finish,components);
		myEnablers=enablers;
	}
	protected ActionEnabler makeNextEnabler()
	{
		return new ActionEnabler()
		{
			public boolean isEnabled()
            {
				return myEnablers[index].isEnabled()&&index<myComponents.length-1;
            }
		};
	}
	protected ActionEnabler makeFinishEnabler()
	{
		return new ActionEnabler()
		{
			public boolean isEnabled()
			{
				return myEnablers[index].isEnabled()&&index==myComponents.length-1;
			}
		};
	}
	public void anyChange()
    {
		System.out.println("change");
		nextButton.setEnabled(nextButton.getAction().isEnabled());
		finishButton.setEnabled(finishButton.getAction().isEnabled());
    }
	public void itemStateChanged(ItemEvent e)
    {
		anyChange();
    }
	public void stateChanged(ChangeEvent e)
    {
		anyChange();
    }
	public void actionPerformed(ActionEvent e)
    {
		anyChange();	    
    }
	public void changedUpdate(DocumentEvent e)
    {
		anyChange();	    
    }
	public void insertUpdate(DocumentEvent e)
    {
		anyChange();	    
    }
	public void removeUpdate(DocumentEvent e)
    {
		anyChange();	    
    }
}
