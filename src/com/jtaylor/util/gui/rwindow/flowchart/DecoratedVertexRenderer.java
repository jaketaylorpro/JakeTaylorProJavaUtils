package com.jtaylor.util.gui.rwindow.flowchart;

import com.jtaylor.util.ColorOperations;
import com.jtaylor.util.Direction;
import com.jtaylor.util.datastructures.Pair;
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
public class DecoratedVertexRenderer<V> implements VertexRenderer<V>
{
   protected int myBorderSize;
   public DecoratedVertexRenderer(int borderSize)
   {
      myBorderSize=borderSize;
   }
   public DecoratedVertexRenderer()
   {
      this(15);
   }
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
         g2.fill(new RoundRectangle2D.Double(x - 5, y - 5, w + 10, h + 10, 10, 10));
      }
      g2.setColor(ColorOperations.midnight);
      RoundRectangle2D shape = new RoundRectangle2D.Double(x, y, w, h, 10, 10);
      g2.fill(shape);
      g2.setColor(Color.white);
      Font oldFont = g2.getFont();
      g2.setFont(new Font(oldFont.getName(), Font.PLAIN, 100));
      Pair<String,Color> colorString=vertexToString(Direction.CENTER,vertex);
      g2.setColor(colorString.getB());
      GraphicsOperations.drawCenteredString(g2, new Point(upperLeft.x+myBorderSize,upperLeft.y+myBorderSize), new Point(lowerRight.x-myBorderSize,lowerRight.y-myBorderSize),colorString.getA());

      Point northUL=new Point(upperLeft.x+myBorderSize,upperLeft.y);
      Point northLR= new Point(lowerRight.x-myBorderSize,upperLeft.y+myBorderSize);
      //System.out.println("northul: "+northUL);
      //System.out.println("northlr: "+northLR);
      g2.setFont(new Font(oldFont.getName(), Font.PLAIN, 100));
      colorString=vertexToString(Direction.NORTH,vertex);
      g2.setColor(colorString.getB());
      GraphicsOperations.drawCenteredString(g2, northUL,northLR, colorString.getA());

      Point southUL=new Point(upperLeft.x+myBorderSize,lowerRight.y-myBorderSize);
      Point southLR=new Point(lowerRight.x-myBorderSize,lowerRight.y);
      //System.out.println("southul: "+southUL);
      //System.out.println("southlr: "+southLR);
      g2.setFont(new Font(oldFont.getName(), Font.PLAIN, 100));
      colorString= vertexToString(Direction.SOUTH,vertex);
      g2.setColor(colorString.getB());
      GraphicsOperations.drawCenteredString(g2, southUL,southLR,colorString.getA());

      Point eastUL=new Point(lowerRight.x-myBorderSize,upperLeft.y+myBorderSize);
      Point eastLR=new Point(lowerRight.x,lowerRight.y-myBorderSize);
      //System.out.println("eastUL: "+eastUL);
      //System.out.println("eastlr: "+eastLR);
      g2.setFont(new Font(oldFont.getName(), Font.PLAIN, 100));
      colorString=vertexToString(Direction.EAST, vertex);
      g2.setColor(colorString.getB());
      GraphicsOperations.drawVerticalString(g2, eastUL, eastLR, colorString.getA());

      Point westUL=new Point(upperLeft.x,upperLeft.y+myBorderSize);
      Point westLR=new Point(upperLeft.x+myBorderSize,lowerRight.y-myBorderSize);
      //System.out.println("westUL: "+westUL);
      //System.out.println("westlr: "+westLR);
      g2.setFont(new Font(oldFont.getName(), Font.PLAIN, 100));
      colorString=vertexToString(Direction.WEST, vertex);
      g2.setColor(colorString.getB());
      GraphicsOperations.drawVerticalString(g2, westUL, westLR, colorString.getA());

      Point northEastUL=new Point(lowerRight.x-myBorderSize,upperLeft.y);
      Point northEastLR=new Point(lowerRight.x,upperLeft.y+myBorderSize);
      //System.out.println("northEastUL: "+northEastUL);
      //System.out.println("northEastLR: "+northEastLR);
      g2.setFont(new Font(oldFont.getName(), Font.PLAIN, 100));
      colorString=vertexToString(Direction.NORTH_EAST, vertex);
      g2.setColor(colorString.getB());
      GraphicsOperations.drawCenteredString(g2, northEastUL, northEastLR, colorString.getA());

      Point southEastUL=new Point(lowerRight.x-myBorderSize,lowerRight.y-myBorderSize);
      Point southEastLR=new Point(lowerRight.x,lowerRight.y);
      //System.out.println("southEastUL: "+southEastUL);
      //System.out.println("southEastLR: "+southEastLR);
      g2.setFont(new Font(oldFont.getName(), Font.PLAIN, 100));
      colorString=vertexToString(Direction.SOUTH_EAST, vertex);
      g2.setColor(colorString.getB());
      GraphicsOperations.drawCenteredString(g2, southEastUL, southEastLR, colorString.getA());

      Point southWestUL=new Point(upperLeft.x,lowerRight.x-myBorderSize);
      Point southWestLR=new Point(upperLeft.x+myBorderSize,lowerRight.y);
      //System.out.println("southWestUL: "+southWestUL);
      //System.out.println("southWestLR: "+southWestLR);
      g2.setFont(new Font(oldFont.getName(), Font.PLAIN, 100));
      colorString=vertexToString(Direction.SOUTH_WEST, vertex);
      g2.setColor(colorString.getB());
      GraphicsOperations.drawCenteredString(g2, southWestUL, southWestLR, colorString.getA());

      Point northWestUL=new Point(upperLeft.x,upperLeft.y);
      Point northWestLR=new Point(upperLeft.x+myBorderSize,upperLeft.y+myBorderSize);
      //System.out.println("northWestUL: "+northWestUL);
      //System.out.println("northWestLR: "+northWestLR);
      g2.setFont(new Font(oldFont.getName(), Font.PLAIN, 100));
      colorString=vertexToString(Direction.NORTH_WEST, vertex);
      g2.setColor(colorString.getB());
      GraphicsOperations.drawCenteredString(g2, northWestUL, northWestLR, colorString.getA());
   }
   public Pair<String,Color> vertexToString(Direction place,Vertex<V> vertex)
   {
      String string;
      if(place==Direction.CENTER)
      {
         return new Pair<String,Color>(vertex.getValue().toString(),Color.white);
      }
      else if(place==Direction.NORTH)
      {
         string="north";
      }
      else if(place==Direction.SOUTH)
      {
         string="south";
      }
      else if(place==Direction.EAST)
      {
         string="east";
      }
      else if(place==Direction.WEST)
      {
         string="west";
      }
      else if(place==Direction.NORTH_EAST)
      {
         string="1";
      }
      else if(place==Direction.SOUTH_EAST)
      {
         string="2";
      }
      else if(place==Direction.SOUTH_WEST)
      {
         string="3";
      }
      else if(place==Direction.NORTH_WEST)
      {
         string="4";
      }
      else
      {
         string="";
      }
      return new Pair<String,Color>(string,place.isCardinal()?Color.lightGray:Color.red);
   }
}
