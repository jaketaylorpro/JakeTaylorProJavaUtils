package com.jtaylor.util.gui.rwindow.flowchart;

import com.jtaylor.util.enums.Direction;
import com.jtaylor.util.datastructures.Pair;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 9/8/11
 * Time: 11:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class BufferedDecoratedVertexRenderer<V> extends DecoratedVertexRenderer implements BufferedVertexRenderer
{
   private HashMap<Vertex<V>,Map<Direction,Pair<String,Color>>> myBuffer;
   private DecoratedVertexRenderer<V> myRenderer;
   public BufferedDecoratedVertexRenderer(DecoratedVertexRenderer<V> renderer)
   {
      super(renderer.myBorderSize);
      myBuffer=new HashMap<Vertex<V>, Map<Direction, Pair<String,Color>>>();
      myRenderer=renderer;
   }
   public void clearBuffer()
   {
      myBuffer=new HashMap<Vertex<V>, Map<Direction, Pair<String,Color>>>();
   }
   @Override
   public Pair<String, Color> vertexToString(Direction place, Vertex vertex)
   {
      if(!myBuffer.containsKey(vertex))
      {
         myBuffer.put(vertex,new HashMap<Direction,Pair<String,Color>>());
      }
      if(!myBuffer.get(vertex).containsKey(place))
      {
         myBuffer.get(vertex).put(place,myRenderer.vertexToString(place,vertex));
      }
      return myBuffer.get(vertex).get(place);
   }
}
