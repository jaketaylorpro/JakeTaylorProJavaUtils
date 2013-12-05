package com.jtaylor.util.gui.rwindow;

import com.jtaylor.util.Logging;
import com.jtaylor.util.gui.rwindow.ActionHelper.ActionEnabler;
import com.jtaylor.util.gui.rwindow.ActionHelper.ActionPerformer;
import org.apache.log4j.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: Oct 3, 2010
 * Time: 5:26:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class RAction extends AbstractAction
{
	public static Logger log=Logger.getLogger(RAction.class);
	private ActionEnabler myEnabler;
	private ActionPerformer myPerformer;
	private RBar myParentBar;
	private String myName;
	public RAction(String name, Icon icon, ActionPerformer performer, ActionEnabler enabler)
	{
		super(name,icon);
		myName=name;
		myEnabler=enabler;
		myPerformer=performer;
	}

	public void setParentBar(RBar parent)
	{
		myParentBar=parent;
		myPerformer.setParentBar(myParentBar);
	}

	public boolean isEnabled()
	{
		try
		{
			if (myEnabler == null)
			{
				return true;
			}
			else
			{
				return myEnabler.isEnabled();
			}
		}
		catch(Exception e)
		{
			log.error("there was an error checking if the button: "+myName+" should be enabled",e);
			return false;
		}
	}

	public void actionPerformed(ActionEvent e)
	{
//		System.out.println("action performed: ");
		log.debug("action performed: "+e);
		try
		{
			myPerformer.actionPerformed(e);
			if (myParentBar != null)
			{
				myParentBar.reenableButtons();
			}
			else
			{
				log.debug("parentbar is null cannot reenable buttons");
//				System.out.println("parentbar is null cannot reenable buttons");
			}
		}
		catch(Exception e1)
		{
			Logging.notifyUser("There was an error performing the action: " + myName, e1, log);
		}
	}

	public String toString()
	{
		return "action-" + getValue(Action.NAME);
	}
	public RBar getParentBar()
	{
		return myParentBar;
	}
}
