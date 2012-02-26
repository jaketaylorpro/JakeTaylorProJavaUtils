package com.jtaylor.util.gui.rwindow;

import javax.swing.Icon;
import java.awt.event.ActionEvent;

public class ActionHelper
{

	/*
	public static interface AnyChangeListener
	{
		public void anyChange();
	}
	*/
	public static abstract class ActionPerformer
	{
		protected RBar myParentBar;
		public void setParentBar(RBar parent)
		{
			myParentBar=parent;
		}
		public RBar getParentBar()
		{
			return myParentBar;
		}
		public abstract void actionPerformed(ActionEvent e);
	}
	public static interface ActionEnabler
	{
		public boolean isEnabled();
	}
	public static RAction getNewButton(final String name, final ActionPerformer performer,final ActionEnabler enabler)
	{
		return getNewButton(name,null,performer,enabler);
	}
	public static RAction getNewButton(final String name, final Icon icon, final ActionPerformer performer,final ActionEnabler enabler)
	{
		return new RAction(name,icon,performer,enabler);
	}

//	public static Action getOkAction(final RWindow parent)
//	{
//		return getNewButton("Ok", new ActionPerformer()
//		{
//			public void actionPerformed(ActionEvent e)
//			{
//				parent.dispose();
//				parent.setPressedOk(true);
//			}
//		}, null, null);
//	}

	public static RAction getOkAction()
	{
		return getNewButton("Ok", new ActionPerformer()
		{
			public void actionPerformed(ActionEvent e)
			{
				getParentBar().getParentWindow().setPressedOk(true);
				getParentBar().getParentWindow().dispose();
			}
		}, null);
	}

//	public static Action getCancelAction(final RWindow parent)
//	{
//		return getNewButton("Cancel", new ActionPerformer()
//		{
//			public void actionPerformed(ActionEvent e)
//            {
//					parent.dispose();
//					parent.setPressedOk(false);
//            }
//		},null,null);
//	}

	public static RAction getCancelAction()
	{
		return getNewButton("Cancel", new ActionPerformer()
		{
			public void actionPerformed(ActionEvent e)
			{
            getParentBar().getParentWindow().setPressedOk(false);
            getParentBar().getParentWindow().dispose();
			}
		}, null);
	}
}
