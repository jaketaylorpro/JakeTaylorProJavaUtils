package com.jtaylor.util.gui;

import com.jtaylor.util.gui.rwindow.RDialog;

import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/3/11
 * Time: 5:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class GraphicsOperations
{
   public static void drawVerticalString(Graphics2D g2,Point upperLeft,Point lowerRight,String string)
   {
      List<String> strings=new Vector<String>();
      for(int i=0;i<string.length();i++)
      {
         char c=string.charAt(i);
         if(Character.isWhitespace(c))
         {
            strings.add("");
         }
         else
         {
            strings.add(""+c);
         }
      }
      drawMultiLineCenteredString(g2,upperLeft,lowerRight,strings);
   }
   public static void drawCenteredString(Graphics2D g2,Point upperLeft,Point lowerRight,String string)
   {
      if(string!=null&&string.length()>0)
      {
         int w=lowerRight.x-upperLeft.x-4;
         int h=lowerRight.y-upperLeft.y-4;
         int x=upperLeft.x+2;
         int y=upperLeft.y+2;
         FontMetrics fm=g2.getFontMetrics();
         Font font=fm.getFont();
         Font originalFont=new Font(font.getName(),font.getStyle(),font.getSize());
         int stringWidth=-1;
         while((stringWidth<0||stringWidth>w||fm.getHeight()>h)&&font.getSize()>1)
         {
            font=new Font(font.getName(),font.getStyle(),font.getSize()-1);
            g2.setFont(font);
            fm=g2.getFontMetrics();
            stringWidth=fm.stringWidth(string);
         }
         double hSpacing=(w-stringWidth)/2f;
         double vSpacing=(h-fm.getHeight())/2f + fm.getMaxAscent();
   //      System.out.println("fm spacing: "+fm.getLeading());
   //      System.out.println("fm ascent: "+fm.getAscent());
   //      System.out.println("fm descent: "+fm.getDescent());
   //      System.out.println("fm max ascent: "+fm.getMaxAscent());
   //      System.out.println("fm max descent: "+fm.getMaxDescent());
   //      System.out.println("vspacing: "+vSpacing);
         g2.drawString(string,(float)(x+hSpacing),(float)(y+vSpacing));
         //not reset font to original
         g2.setFont(originalFont);
      }
   }
   public static void drawMultiLineCenteredString(Graphics2D g2,Point upperLeft,Point lowerRight,List<String> lines)
   {
//      System.out.println("lines: "+new EasyVector<String>(lines).toString(true));
      int height=(int)((lowerRight.getY()-upperLeft.getY())/lines.size());
//      System.out.println("height: "+height);
      Point ul;
      Point lr;
      for(int i=0;i<lines.size();i++)
      {
         ul=new Point(upperLeft.x,upperLeft.y+(i*height));
         lr=new Point(lowerRight.x,upperLeft.y+((i+1)*height));
//         System.out.println("ul: "+ul);
//         System.out.println("lr: "+lr);
         drawCenteredString(g2,ul,lr,lines.get(i));
      }
      //TODO tests
   }
   public static Rectangle convertRectangle2DToRectangle(Rectangle2D r2d)
   {
      return new Rectangle((int)r2d.getX(),(int)r2d.getY(),(int)r2d.getWidth(),(int)r2d.getHeight());
   }
   public static void drawThickLine(Graphics2D g2,Point p1,Point p2,int thickness)
   {
      drawThickLine(g2,new Line(p1,p2),thickness);
   }
   public static void drawThickLine(Graphics2D g2,Line line,int thickness)
   {
      g2.draw(line);
      Point2D start1;
      Point2D start2;
      Point2D end1;
      Point2D end2;
      for(int i=0;i<thickness;i++)
      {
         g2.draw(new Line(line.x1+i,line.y1+i,line.x2+i,line.y2+i));
         g2.draw(new Line(line.x1-i,line.y1-i,line.x2-i,line.y2-i));
      }
   }

   public static void drawBracket(Graphics2D g2, Point2D.Double midpoint, int width, double height, int thickness)
   {
      g2.fill(new Rectangle2D.Double(midpoint.x, midpoint.y - height / 2, thickness, height));//draw the vertical line
      g2.fill(new Rectangle2D.Double(midpoint.x, midpoint.y - height / 2, width, thickness));//draw top line
      g2.fill(new Rectangle2D.Double(midpoint.x,midpoint.y +height / 2 -thickness,width,thickness));//draw bottom line
   }

   public static void drawBracket(Graphics2D g2, Point midpoint, int width, int height, int thickness)
   {
      g2.fill(new Rectangle(midpoint.x, midpoint.y - height / 2, thickness, height));//draw the vertical line
      g2.fill(new Rectangle(midpoint.x, midpoint.y - height / 2, width, thickness));//draw top line
      g2.fill(new Rectangle(midpoint.x,midpoint.y +height / 2 -thickness,width,thickness));//draw bottom line
   }

   public static void main(String[] args)
   {
      JPanel panel=new JPanel()
      {
         @Override
         public void paint(Graphics g)
         {
//            drawBracket((Graphics2D)g,new Point2D.Double(10,100),20,200,5);
            g.drawRect(10,10,100,100);
            drawMultiLineCenteredString((Graphics2D) g, new Point(10, 10), new Point(110, 110), Arrays.asList("world"));
         }
      };
      new RDialog(panel).display();
   }
}
