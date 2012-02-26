package com.jtaylor.util.gui.rwindow.flowchart;

import com.jtaylor.util.ColorOperations;
import com.jtaylor.util.gui.GraphicsOperations;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/4/11
 * Time: 5:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultVertexRenderer<V> implements VertexRenderer<V>
{
   public void drawVertex(Vertex<V> vertex, Graphics2D g2,Point upperLeft, Point lowerRight,boolean selected)
   {
      int w = lowerRight.x - upperLeft.x;
      int h = lowerRight.y - upperLeft.y;
      int x = upperLeft.x;
      int y = upperLeft.y;
//      g2.setColor(Color.white);
//      g2.fill(new Rectangle2D.Double(x, y, w, h));
      if(selected)
      {
         g2.setColor(ColorOperations.forest);
         g2.fill(new RoundRectangle2D.Float(x-5,y-5,w+10,h+10,10,10));
      }
      g2.setColor(ColorOperations.midnight);
      RoundRectangle2D shape = new RoundRectangle2D.Float(x, y, w, h, 10, 10);
      g2.fill(shape);
      g2.setColor(Color.white);
      Font oldFont = g2.getFont();
      g2.setFont(new Font(oldFont.getName(), Font.PLAIN, 100));
      GraphicsOperations.drawCenteredString(g2, upperLeft, lowerRight, vertexToString(vertex));
   }
   public String vertexToString(Vertex vertex)
   {
      return vertex.getValue().toString();
   }
}
