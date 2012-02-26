package com.jtaylor.util.gui;

import com.jtaylor.util.Direction;
import com.jtaylor.util.gui.rwindow.RDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/4/11
 * Time: 12:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class Triangle2D extends RectangularShape
{

   protected double x = 0.0;

   protected double y = 0.0;

   protected double w = 0.0;

   protected double h = 0.0;

   protected Direction d =Direction.EAST;

   /**
    * Creates a new instance of Triangle2D
    */
   public Triangle2D(double x, double y, double width, double height,
                     Direction direction)
   {
      setFrame(x, y, width, height);
      this.d = direction;
   }

   public boolean contains(double param, double param1)
   {
      return false;
   }

   public boolean contains(double param, double param1, double param2,
                           double param3)
   {
      return false;
   }

   public boolean intersects(double x, double y, double w, double h)
   {
      return false;
   }

   public Rectangle2D getBounds2D()
   {
      return new Rectangle2D.Double(x, y, w, h);
   }

   public double getHeight()
   {
      return h;
   }

   public PathIterator getPathIterator(AffineTransform affineTransform)
   {
      return new PathIterator()
      {
         int state = 0;
         int maxstate = 4;

         double[][] northSegment = {{x + w / 2d, y},
                                    {x, y + h},
                                    {x + w, y + h},
                                    {x + w / 2d, y},
                                    {0d, 0d}};
         double[][] eastSegment  = {{x, y},
                                    {x+w/2d, y+ h/2d},
                                    {x, y + h},
                                    {x, y},
                                    {0d, 0d}};
         double[][] southSegment = {{x, y},
                                    {x + w / 2d, y + h},
                                    {x + w, y},
                                    {x, y},
                                    {0d, 0d}};
         double[][] westSegment  = {{x, y+h/2d},
                                    {x+w, y},
                                    {x+w, y + h},
                                    {x, y+h/2d},
                                    {0.0, 0.0}};
         int[] segment = {PathIterator.SEG_MOVETO,
                          PathIterator.SEG_LINETO,
                          PathIterator.SEG_LINETO,
                          PathIterator.SEG_LINETO,
                          PathIterator.SEG_CLOSE};

         public int currentSegment(double[] coords)
         {
            if (d==Direction.NORTH)
            {
               coords[0] = northSegment[state][0];
               coords[1] = northSegment[state][1];
            }
            else if(d==Direction.SOUTH)
            {
               coords[0] = southSegment[state][0];
               coords[1] = southSegment[state][1];
            }
            else if(d== Direction.WEST)
            {
               coords[0] = westSegment[state][0];
               coords[1] = westSegment[state][1];
            }
            else//default east
            {
               coords[0] = eastSegment[state][0];
               coords[1] = eastSegment[state][1];
            }
            return segment[state];
         }

         public int currentSegment(float[] coords)
         {
            if (d==Direction.NORTH)
            {
               coords[0] = (float)northSegment[state][0];
               coords[1] = (float)northSegment[state][1];
            }
            else if(d==Direction.SOUTH)
            {
               coords[0] = (float)southSegment[state][0];
               coords[1] = (float)southSegment[state][1];
            }
            else if(d==Direction.WEST)
            {
               coords[0] = (float)westSegment[state][0];
               coords[1] = (float)westSegment[state][1];
            }
            else//default east
            {
               coords[0] = (float)eastSegment[state][0];
               coords[1] = (float)eastSegment[state][1];
            }
            return segment[state];
         }

         public int getWindingRule()
         {
            return PathIterator.WIND_NON_ZERO;
         }

         public boolean isDone()
         {
            return (state == maxstate);
         }

         public void next()
         {
            state++;
         }
      };
   }

   public double getWidth()
   {
      return w;
   }

   public double getX()
   {
      return x;
   }

   public double getY()
   {
      return y;
   }

   public boolean isEmpty()
   {
      return (w <= 0.0 || h <= 0.0);
   }

   public void setFrame(double x, double y, double width, double height)
   {
      this.x = x;
      this.y = y;
      this.w = width;
      this.h = height;
   }

   public static void main(String[] args)
   {
      final Triangle2D t=new Triangle2D(50,50,20,20,Direction.EAST);
      JPanel panel=new JPanel()
      {
         @Override
         public void paint(Graphics g)
         {
            Graphics2D g2=(Graphics2D)g;
            g2.setColor(Color.black);
            g2.fill(t);
            g2.setColor(Color.red);
            g2.fillRect(50,50,2,2);
            System.out.println("done drawing");
         }
      };
      new RDialog(panel).display();
   }

}
