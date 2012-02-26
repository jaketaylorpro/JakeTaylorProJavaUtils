package com.jtaylor.util.gui;

import com.jtaylor.util.Logging;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.List;

public class ListFocusTraversalPolicy extends FocusTraversalPolicy
{
	private List<Component> myList;
	private Logger log;
	public ListFocusTraversalPolicy(List<Component> list)
	{
		myList=list;
		log= Logging.createServerLogger(ListFocusTraversalPolicy.class);

	}
	@Override
	public Component getComponentAfter(Container aContainer,
	        Component aComponent)
	{
		int index=myList.indexOf(aComponent);
		if(index==-1)
		{
			return getDefaultComponent(aContainer);
		}
		else if(index+1==myList.size())
		{
			return getFirstComponent(aContainer);
		}
		else
		{
			return myList.get(index+1);
		}
	}

	@Override
	public Component getComponentBefore(Container aContainer,Component aComponent)
	{
		int index=myList.indexOf(aComponent);
		if(index==-1)
		{
			return getDefaultComponent(aContainer);
		}
		else if(index==0)
		{
			return getLastComponent(aContainer);
		}
		else
		{
			return myList.get(index-1);
		}
	}

	@Override
	public Component getDefaultComponent(Container aContainer)
	{
		return getFirstComponent(aContainer);
	}

	@Override
	public Component getFirstComponent(Container aContainer)
	{
		return myList.get(0);
	}

	@Override
	public Component getLastComponent(Container aContainer)
	{
		return myList.get(myList.size()-1);
	}

}
