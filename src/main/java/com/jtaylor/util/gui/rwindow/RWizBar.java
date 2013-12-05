package com.jtaylor.util.gui.rwindow;

import com.jtaylor.util.gui.rwindow.ActionHelper.ActionEnabler;
import com.jtaylor.util.gui.rwindow.ActionHelper.ActionPerformer;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.event.ActionEvent;

public class RWizBar extends RBar
{
	protected Component[] myComponents;
	protected int index;
	protected AbstractButton nextButton;
	protected AbstractButton finishButton;
	protected ActionPerformer finishPerformer;
	public static void main(String[] args)
	{
		JLabel label=new JLabel("screen1");
		ActionPerformer finish=new ActionPerformer()
		{
		
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("finish");
			}
		};
		RDialog window=new RDialog("test",label,new RWizBar(finish,label,new JLabel("screen2"),new JLabel("screen3"),new JLabel("screen4")));
		window.prompt();
		System.out.println(window.pressedOk);
	}
	public RWizBar(ActionPerformer finish, Component... components)
	{
		super((RAction[])null);
		finishPerformer=finish;
		index=0;
		myComponents=components;
	}
	public void setParentWindow(RWindow parent)
	{
		super.setParentWindow(parent);
		//make the cancel button
//		add(ActionHelper.getCancelAction(myParentWindow));
		add(ActionHelper.getCancelAction());
		//make the previous button
		add(ActionHelper.getNewButton("Previous", makePreviousPerformer(),makePreviousEnabler()));
		//make the next button
		nextButton=add(ActionHelper.getNewButton("Next",makeNextPerformer(),makeNextEnabler()));
		//make the finish button
		finishButton=add(ActionHelper.getNewButton("Finish",makeFinishPerformer(),makeFinishEnabler()));
	}
	protected ActionPerformer makePreviousPerformer()
	{
		return new ActionPerformer()
		{
			public void actionPerformed(ActionEvent e)
            {
				if(index>0)
				{
					index--;
					myParentWindow.setView(myComponents[index]);
				}
            }
		};
	}
	protected ActionEnabler makePreviousEnabler()
	{
		return new ActionEnabler()
		{
			public boolean isEnabled()
            {
	            return index>0;
            }
		};
	}
	protected ActionPerformer makeNextPerformer()
	{
		return new ActionPerformer()
		{
			public void actionPerformed(ActionEvent e)
            {
				if(index<myComponents.length-1)
				{
					index++;
					myParentWindow.setView(myComponents[index]);
				}
            }
		};
	}
	protected ActionEnabler makeNextEnabler()
	{
		return new ActionEnabler()
		{
			public boolean isEnabled()
            {
	            return index<myComponents.length-1;
            }
		};
	}
	protected ActionPerformer makeFinishPerformer()
	{
		return new ActionPerformer()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(index==myComponents.length-1)
				{
					myParentWindow.setPressedOk(true);
					myParentWindow.dispose();
					finishPerformer.actionPerformed(e);
				}
			}
		};
	}
	protected ActionEnabler makeFinishEnabler()
	{
		return new ActionEnabler()
		{
			public boolean isEnabled()
			{
				return index==myComponents.length-1;
			}
		};
	}
}
