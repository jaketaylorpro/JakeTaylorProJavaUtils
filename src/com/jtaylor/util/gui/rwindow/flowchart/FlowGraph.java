package com.jtaylor.util.gui.rwindow.flowchart;

import com.jtaylor.util.Logging;
import org.apache.log4j.Logger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/3/11
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class FlowGraph<V,E,L> implements ChangeListener
{
   private List<ChangeListener> myListeners;
   private Map<V,Vertex<V>> myVertexMap;
   private List<Edge<V,E>> myEdgeList;
   private Map<L,Lane<L>> myLaneMap;
   private Logger log;
   public FlowGraph()
   {
      myListeners=new Vector<ChangeListener>();
      myVertexMap=new HashMap<V,Vertex<V>>();
      myEdgeList=new Vector<Edge<V,E>>();
      myLaneMap=new HashMap<L,Lane<L>>();
      log= Logging.createServerLogger(FlowGraph.class);
   }
   public void addVertex(Vertex<V> vertex)
   {
      myVertexMap.put(vertex.getValue(),vertex);
      vertex.addChangeListener(this);
      notifyListeners();
   }
   public void addEdge(Edge<V,E> edge)
   {
      myEdgeList.add(edge);
      edge.addChangeListener(this);
      notifyListeners();
   }
   public void addLane(Lane<L> lane)
   {
      log.debug("add lane");
      myLaneMap.put(lane.getValue(),lane);
      lane.addChangeListener(this);
      notifyListeners();
   }
   public Vertex<V> getVertexByValue(V v)
   {
      return myVertexMap.get(v);
   }
   public Edge<V,E> getEdgeByValue(E e)
   {
      for(Edge<V,E> edge:myEdgeList)
      {
         if(edge.getValue().equals(e))
         {
            return edge;
         }
      }
      return null;
   }
   public Lane<L> getLaneByValue(L l)
   {
      return myLaneMap.get(l);
   }
   public Lane<L> getLaneByYCoord(int y)
   {
      for(Lane<L> lane:myLaneMap.values())
      {
         if(lane.getStart()<=y&&lane.getEnd()>=y)
         {
            return lane;
         }
      }
      return null;
   }
   public Collection<Vertex<V>> getVertices()
   {
      return myVertexMap.values();
   }
   public List<Edge<V,E>> getEdges()
   {
      return new Vector<Edge<V,E>>(myEdgeList);
   }
   public Collection<Lane<L>> getLanes()
   {
      return myLaneMap.values();
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
      log.debug("flowgraph notifying: "+myListeners.size()+" listeners");
      ChangeEvent e=new ChangeEvent(this);
      for(ChangeListener l:myListeners)
      {
         l.stateChanged(e);
      }
   }
   public List<Vertex<V>> getVerticesInLane(Lane<L> lane)
   {
      List<Vertex<V>> vertices=new Vector<Vertex<V>>();
      int y;
      Lane l;
      for(Vertex<V> v:getVertices())
      {
         y=(int)v.getLocation().getY();
         l=getLaneByYCoord(y);
         if(l==lane)
         {
            vertices.add(v);
         }
      }
      return vertices;
   }

   public void stateChanged(ChangeEvent e)
   {
      notifyListeners();
   }

   public boolean containsVertex(Vertex<V> vertex)
   {
      return myVertexMap.containsKey(vertex.getValue());
   }
   public boolean containsEdge(Edge<V,E> edge)
   {
      return myEdgeList.contains(edge);
   }
   public boolean containsLane(Lane<L> lane)
   {
      return myLaneMap.containsKey(lane.getValue());
   }

   public void removeVertex(Vertex<V> vertex)
   {
      vertex.removeChangeListener(this);
      myVertexMap.remove(vertex.getValue());
      notifyListeners();
   }
   public void removeEdge(Edge<V, E> edge)
   {
      edge.removeChangeListener(this);
      myEdgeList.remove(edge);
      notifyListeners();
   }
   public void removeLane(Lane<L> lane)
   {
      lane.removeChangeListener(this);
      myLaneMap.remove(lane.getValue());
      notifyListeners();
   }
   public int getLastLaneIndex()
   {
      int lastIndex=0;
      for(Lane<L> lane:myLaneMap.values())
      {
         if(lane.getEnd()>lastIndex)
         {
            lastIndex=lane.getEnd();
         }
      }
      return lastIndex;
   }
   public void clearAll()
   {
      myLaneMap.clear();
      myVertexMap.clear();
      myEdgeList.clear();
   }
}
