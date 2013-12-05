package com.jtaylor.util.gui.rwindow.flowchart;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/3/11
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class Vertex <V> implements Comparable
{
   private int myX;
   private int myY;
   private V myValue;
   private List<ChangeListener> myListeners;

   public Vertex(V value,int x,int y)
   {
      myListeners=new Vector<ChangeListener>();
      myValue=value;
      myX=x;
      myY=y;
   }
   public int compareTo(Object o)
   {
      return ((Comparable)myValue).compareTo(o);
   }
   public V getValue()
   {
      return myValue;
   }
   public int getX()
   {
      return myX;
   }
   public int getY()
   {
      return myY;
   }
   public Point getLocation()
   {
      return new Point(myX,myY);
   }
   public void setX(int x)
   {
      myX=x;
      notifyListeners();
   }
   public void setY(int y)
   {
      myY=y;
      notifyListeners();
   }
   public void setValue(V value)
   {
      myValue=value;
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
   public boolean hasSameCoordinates(Vertex v)
   {
      return getLocation().x==v.getLocation().x&&getLocation().y==v.getLocation().y;
   }
   @Override
   public boolean equals(Object o)
   {
      System.out.println("vertex equals called: "+this+","+o);
      if(Vertex.class.isAssignableFrom(o.getClass()))
      {
         Vertex vertex=(Vertex)o;
         System.out.println("myValue.class: "+myValue.getClass());
         System.out.println("o.class: "+vertex.myValue.getClass());
         System.out.println("vertex returns: "+myValue.equals(vertex.myValue));
         return myValue.equals(vertex.myValue);
      }
      else
      {
         System.out.println("not a vertex");
         return false;
      }
   }
}
