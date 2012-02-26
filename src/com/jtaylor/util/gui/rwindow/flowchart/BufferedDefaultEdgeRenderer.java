package com.jtaylor.util.gui.rwindow.flowchart;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 9/8/11
 * Time: 11:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class BufferedDefaultEdgeRenderer<V,E,L> extends DefaultEdgeRenderer implements BufferedEdgeRenderer
{
   private DefaultEdgeRenderer<V,E,L> myRenderer;
   private HashMap<Edge<V,E>,String> myBuffer;
   public BufferedDefaultEdgeRenderer(DefaultEdgeRenderer<V,E,L> renderer)
   {
      super();
      myRenderer=renderer;
      myBuffer=new HashMap<Edge<V, E>, String>();
   }
   public void clearBuffer()
   {
      myBuffer=new HashMap<Edge<V, E>, String>();
   }

   @Override
   public String edgeToString(Edge edge)
   {
      if(!myBuffer.containsKey(edge))
      {
         myBuffer.put(edge,myRenderer.edgeToString(edge));
      }
      return myBuffer.get(edge);
   }
}
