package com.jtaylor.util.gui.rwindow;

import com.jtaylor.util.Logging;
import org.apache.log4j.Logger;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Vector;

public class RBar extends JToolBar implements DocumentListener, ItemListener, ActionListener, ListSelectionListener, ListDataListener, ChangeListener
{
	protected List<RAction> myActions;
	protected RWindow myParentWindow;
	protected JButton myOkButton;
	protected Logger log=Logger.getLogger(RBar.class);

	public RBar(RAction... actions)
	{
		super();
		log= Logging.createServerLogger(RBar.class);
		myActions=new Vector<RAction>();
		if(actions!=null)
		{
			for(RAction a:actions)
			{
				add(a);
			}
		}
		setFloatable(false);
	}
	public RBar(boolean okCancel)
	{
		super();
		myActions=new Vector<RAction>();
		myOkButton=add(ActionHelper.getOkAction());
		if(okCancel)
		{
			add(ActionHelper.getCancelAction());
		}
		setFloatable(false);
	}
	public RBar(int orientation)
	{
		super(orientation);
		myActions=new Vector<RAction>();		
		setFloatable(false);
	}
	@Override
	public JButton add(Action a)
	{
		if(a instanceof RAction)
		{
			return addRAction((RAction)a);
		}
		else
		{
			return super.add(a);
		}
	}
	public JButton addRAction(RAction a)
	{
		myActions.add(a);
		a.setParentBar(this);
//		System.out.println("added raciton and set parentbar: "+a.getParentBar());
		return super.add(a);
	}
	@Override
	public void removeAll()
	{
		myActions.clear();
		super.removeAll();
	}
	public RBar()
	{
		this(false);
	}
	public void setParentWindow(RWindow window)
    {
		myParentWindow=window;
    }
	public RWindow getParentWindow()
	{
		return myParentWindow;
	}
	public Component add(Component c)
	{
		if(c instanceof JComponent)
		{
			((JComponent)c).setAlignmentX(.5f);
		}
		return super.add(c);
	}

	public void reenableButtons()
	{
//		log.debug("reenableButtons called");
		for(Component c:getComponents())
		{
			if(c instanceof AbstractButton)
			{
				AbstractButton b=(AbstractButton)c;
            if(b.getAction()!=null)
            {
				   b.setEnabled(b.getAction().isEnabled());
            }
			}
		}
//		System.out.println("sucessfully renabled buttons");
	}

	public void requestFocus()
	{
		//super.requestFocus();
		super.requestFocusInWindow();
		//myOkButton.requestFocus();
		if(myOkButton!=null)
		{
			myOkButton.requestFocusInWindow();
		}
	}

	public RAction getAction(int index)
	{
		return myActions.get(index);
	}

	public void changedUpdate(DocumentEvent e){reenableButtons();}
	public void insertUpdate(DocumentEvent e){reenableButtons();}
	public void removeUpdate(DocumentEvent e){reenableButtons();}
	public void itemStateChanged(ItemEvent e){reenableButtons();}
	public void actionPerformed(ActionEvent e){reenableButtons();}
	public void valueChanged(ListSelectionEvent e){reenableButtons();}
	public void intervalAdded(ListDataEvent e){reenableButtons();}
	public void intervalRemoved(ListDataEvent e){reenableButtons();}
	public void contentsChanged(ListDataEvent e){reenableButtons();}
	public void stateChanged(ChangeEvent e){reenableButtons();}
}
