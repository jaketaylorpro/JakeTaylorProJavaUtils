package com.jtaylor.util.gui.rwindow.flowchart;

import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 9/8/11
 * Time: 11:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class BufferedDefaultLaneRenderer <L> extends DefaultLaneRenderer implements BufferedLaneRenderer
{
   private HashMap<Lane<L>,List<String>> myBuffer;
   private DefaultLaneRenderer<L> myRenderer;

   public BufferedDefaultLaneRenderer(DefaultLaneRenderer<L> renderer)
   {
      super();
      myRenderer=renderer;
      myBuffer=new HashMap<Lane<L>, List<String>>();
   }
   public void clearBuffer()
   {
      myBuffer=new HashMap<Lane<L>, List<String>>();
   }

   @Override
   public List<String> laneToString(Lane lane)
   {
      if(!myBuffer.containsKey(lane))
      {
         myBuffer.put(lane,myRenderer.laneToString(lane));
      }
      return myBuffer.get(lane);
   }
}
