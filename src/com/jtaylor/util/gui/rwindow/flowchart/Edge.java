package com.jtaylor.util.gui.rwindow.flowchart;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/3/11
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class Edge <V,E> implements Comparable
{
   private List<ChangeListener> myListeners;
   private Vertex<V> mySourceVertex;
   private Vertex<V> myDestVertex;
   private E myValue;
   public Edge(E value,Vertex start,Vertex end)
   {
      myListeners=new Vector<ChangeListener>();
      myValue=value;
      mySourceVertex=start;
      myDestVertex=end;
   }
   public int compareTo(Object o)
   {
      return ((Comparable)myValue).compareTo(o);
   }
   public E getValue()
   {
      return myValue;
   }
   public Vertex<V> getStartVertex()
   {
      return mySourceVertex;
   }
   public Vertex<V> getEndVertex()
   {
      return myDestVertex;
   }
   public void setStartVertex(Vertex<V> vertex)
   {
      mySourceVertex=vertex;
      notifyListeners();
   }
   public void setEndVertex(Vertex<V> vertex)
   {
      myDestVertex=vertex;
      notifyListeners();
   }
   public void setValue(E value)
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
   @Override
   public boolean equals(Object o)
   {
      if(Edge.class.isAssignableFrom(o.getClass()))
      {
         Edge edge=(Edge)o;
         return myValue.equals(edge.myValue)&&hasSamePath(edge);
      }
      else
      {
         return false;
      }
   }
   public boolean hasSamePath(Edge edge)
   {
//      return mySourceVertex.equals(edge.mySourceVertex)&&myDestVertex.equals(edge.myDestVertex);
      return mySourceVertex.hasSameCoordinates(edge.mySourceVertex)&&myDestVertex.hasSameCoordinates(edge.myDestVertex);
   }
}
