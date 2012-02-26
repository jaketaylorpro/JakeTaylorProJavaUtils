package com.jtaylor.util.gui;

import com.jtaylor.util.Logging;
import com.jtaylor.util.datastructures.SortedList;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: Jan 24, 2010
 * Time: 12:53:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class SortedListModel <T> implements ListModel
{
	private List<ListDataListener> listeners;
	private SortedList<T> myList;
	private Logger log;
	public SortedListModel(Comparator<T> comparator)
	{
		myList=new SortedList<T>(comparator,false);//dont allow duplicates
		listeners=new Vector<ListDataListener>();
		log= Logging.createServerLogger(SortedListModel.class);
	}
	public SortedListModel(Comparator<T> comparator, Collection<? extends T> collection)
	{
		this(comparator);
		for(T t:collection)
		{
			add(t);
		}
	}

	public int getSize()
	{
		return myList.size();
	}

	public Object getElementAt(int index)
	{
		return myList.get(index);
	}

	public void add(T t)
	{
		if(myList.add(t))
		{
			int i=myList.indexOf(t);
			for(ListDataListener listener:listeners)
			{
				listener.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED,i,i));
			}
		}
	}

	public void remove(T t)
	{
		int i=myList.indexOf(t);
		if(i>-1)
		{
			myList.remove(i);
			for(ListDataListener listener:listeners)
			{
				listener.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED,i,i));
			}
		}
	}

   public boolean contains(T t)
   {
      return myList.contains(t);
   }

	public void addListDataListener(ListDataListener l)
	{
		listeners.add(l);
	}

	public void removeListDataListener(ListDataListener l)
	{
		listeners.remove(l);
	}
   public void clear()
   {
      myList.clear();
   }
}
