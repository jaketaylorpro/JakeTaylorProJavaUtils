package com.jtaylor.util.gui.rwindow.flowchart;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/5/11
 * Time: 3:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class Lane <L> implements Comparable
{

   private L myValue;
   private int myStart;
   private int myEnd;
   private List<ChangeListener> myListeners;

   public Lane(L value,int start,int end)
   {
      myListeners=new Vector<ChangeListener>();
      myValue=value;
      myStart=start;
      myEnd=end;
   }
   public int compareTo(Object o)
   {
      return ((Comparable)myValue).compareTo(o);
   }
   public L getValue()
   {
      return myValue;
   }
   public void setValue(L value)
   {
      myValue=value;
      notifyListeners();
   }
   public int getStart()
   {
      return myStart;
   }
   public void setStart(int start)
   {
      myStart=start;
      notifyListeners();
   }
   public int getEnd()
   {
      return myEnd;
   }
   public void setEnd(int end)
   {
      myEnd=end;
      notifyListeners();
   }
   public String toString()
   {
      if(myValue==null)
      {
         return "<no value>";
      }
      else
      {
         return myValue.toString();
      }
   }
   public void addChangeListener(ChangeListener l)
   {
      myListeners.add(l);
   }
   public boolean removeChangeListener(ChangeListener l)
   {
      return myListeners.remove(l);
   }
   public void notifyListeners()
   {
      ChangeEvent e=new ChangeEvent(this);
      for(ChangeListener l:myListeners)
      {
         l.stateChanged(e);
      }
   }
   @Override
   public boolean equals(Object o)
   {
      if(Lane.class.isAssignableFrom(o.getClass()))
      {
         Lane lane=(Lane)o;
         return myValue.equals(lane.myValue);
      }
      else
      {
         return false;
      }
   }
}
