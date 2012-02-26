package com.jtaylor.util.gui;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 8/29/11
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Line extends Line2D
{
   public int x1;
   public int y1;
   public int x2;
   public int y2;

   public Line(Point p1,Point p2)
   {
      x1=p1.x;
      y1=p1.y;
      x2=p2.x;
      y2=p2.y;
   }
   public Line(int X1,int Y1,int X2,int Y2)
   {
      x1=X1;
      y1=Y1;
      x2=X2;
      y2=Y2;
   }
   @Override
   public double getX1()
   {
      return x1;
   }

   @Override
   public double getY1()
   {
      return y1;
   }

   @Override
   public Point2D getP1()
   {
      return new Point(x1,y1);
   }

   @Override
   public double getX2()
   {
      return x2;
   }

   @Override
   public double getY2()
   {
      return y2;
   }

   @Override
   public Point2D getP2()
   {
      return new Point(x2,y2);
   }

   @Override
   public void setLine(double X1, double Y1, double X2, double Y2)
   {
      x1=(int)X1;
      y1=(int)Y1;
      x2=(int)X2;
      y2=(int)Y2;
   }

   public Rectangle2D getBounds2D()
   {
      int x, y, w, h;
      if (x1 < x2) {
         x = x1;
         w = x2 - x1;
      } else {
         x = x2;
         w = x1 - x2;
      }
      if (y1 < y2) {
         y = y1;
         h = y2 - y1;
      } else {
         y = y2;
         h = y1 - y2;
      }
      return new Rectangle(x, y, w, h);

   }
}
