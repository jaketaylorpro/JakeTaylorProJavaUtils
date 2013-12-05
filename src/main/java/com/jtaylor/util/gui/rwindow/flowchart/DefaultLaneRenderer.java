package com.jtaylor.util.gui.rwindow.flowchart;

import com.jtaylor.util.Logging;
import com.jtaylor.util.ColorOperations;
import com.jtaylor.util.gui.GraphicsOperations;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/5/11
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultLaneRenderer <L> implements LaneRenderer<L>
{
   private final static int BRACKET_WIDTH=10;
   private final static int BRACKET_THICKNESS=5;
   public Logger log;
   public DefaultLaneRenderer()
   {
      log= Logging.createServerLogger(DefaultLaneRenderer.class);
   }
   public void drawLane(Lane<L> lane, Graphics2D g2, Point upperLeft, Point lowerRight,int cellWidth, boolean selected)
   {
      log.debug("drawing lane: "+lane+" selected: "+selected+" from: "+upperLeft+" - "+lowerRight);
      int w = lowerRight.x - upperLeft.x;
      int h = lowerRight.y - upperLeft.y;
      int x = upperLeft.x;
      int y = upperLeft.y;
      Font oldFont = g2.getFont();
      if(selected)
      {
         g2.setColor(ColorOperations.grey13);
         g2.setFont(new Font(oldFont.getName(), Font.BOLD, 100));
      }
      else
      {
         g2.setColor(Color.white);
         g2.setFont(new Font(oldFont.getName(), Font.PLAIN, 100));

      }
      g2.fill(new Rectangle(x, y, w, h));
      g2.setColor(Color.black);
      Point midpoint=new Point(cellWidth-BRACKET_WIDTH,upperLeft.y+(h/2));
      GraphicsOperations.drawBracket(g2,midpoint,BRACKET_WIDTH,h,BRACKET_THICKNESS);
      GraphicsOperations.drawMultiLineCenteredString(g2, upperLeft, new Point(cellWidth - BRACKET_WIDTH, lowerRight.y), laneToString(lane));
   }

   public List<String> laneToString(Lane lane)
   {
      return Arrays.asList(lane.getValue().toString());
   }
}
