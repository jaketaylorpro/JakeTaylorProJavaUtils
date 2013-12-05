package com.jtaylor.util.gui.rwindow.flowchart;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 9/8/11
 * Time: 11:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class BufferedDefaultVertexRenderer<V> extends DefaultVertexRenderer implements BufferedVertexRenderer<V>
{
   private HashMap<Vertex<V>,String> myBuffer;
   private DefaultVertexRenderer<V> myRenderer;
   public BufferedDefaultVertexRenderer(DefaultVertexRenderer<V> renderer)
   {
      super();
      myRenderer=renderer;
      myBuffer=new HashMap<Vertex<V>, String>();
   }
   public void clearBuffer()
   {
      myBuffer=new HashMap<Vertex<V>, String>();
   }
   @Override
   public String vertexToString(Vertex vertex)
   {
      if(!myBuffer.containsKey(vertex))
      {
         myBuffer.put(vertex,myRenderer.vertexToString(vertex));
      }
      return myBuffer.get(vertex);
   }
}
