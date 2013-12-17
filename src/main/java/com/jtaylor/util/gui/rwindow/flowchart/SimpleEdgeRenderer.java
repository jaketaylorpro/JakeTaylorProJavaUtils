package com.jtaylor.util.gui.rwindow.flowchart;

import com.jtaylor.util.ColorOperations;
import com.jtaylor.util.enums.Direction;
import com.jtaylor.util.Logging;
import com.jtaylor.util.gui.GraphicsOperations;
import com.jtaylor.util.gui.Line;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/4/11
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleEdgeRenderer<V,E,L> implements BufferedEdgeRenderer <V,E,L>, MouseMotionListener
{
   public void clearBuffer()
   {
   }

   protected Logger log;
   private int labelWidth=50;
   private int labelHeight=20;
   private int startPenetration=5;
   private int arrowSize=5;
   private int mouseSensitivity=4;
   private FlowGraphView<V,E,L> myGraph;
   private Graphics2D myG2;
   private Map<Edge<V,E>,List<Shape>> myShapeMap;
   private Edge<V,E> myFocusEdge;
   public SimpleEdgeRenderer()
   {
      myShapeMap=new HashMap<Edge<V,E>,List<Shape>>();

      log= Logging.createServerLogger(SimpleEdgeRenderer.class);
   }
   public void drawEdges()
   {
      myShapeMap.clear();
      List<Shape> shapes;
      Line startLine;
      Line endLine;
      Line line;
      Line arrowTop;
      Line arrowBottom;

      Font oldFont=myG2.getFont();
      myG2.setFont(new Font(oldFont.getName(), Font.PLAIN, 100));
      List<Edge<V,E>> edges=myGraph.getFlowGraph().getEdges();
      for(int i=0;i<edges.size();i++)
      {
         final Edge<V,E> edge=edges.get(i);
         int otherWithSamePath=0;
         boolean ignore=false;
         for(int i1=0;i1<i;i1++)
         {
            if(edge.hasSamePath(edges.get(i1)))
            {
               ignore=true;
               otherWithSamePath++;
            }
         }
         shapes=new Vector<Shape>();
         if(edge==myGraph.getSelectedEdge())
         {
            myG2.setColor(ColorOperations.forest);
         }
         else
         {
            myG2.setColor(Color.red);
         }
         Point startPoint=myGraph.convertPointTo2D(edge.getStartVertex().getLocation(), Direction.EAST);
         Point endPoint=myGraph.convertPointTo2D(edge.getEndVertex().getLocation(), Direction.WEST);
         startLine=new Line(new Point(startPoint.x - startPenetration, startPoint.y), startPoint);
         if(!ignore)
         {
            GraphicsOperations.drawThickLine(myG2,startLine,2);
         }
         endLine=new Line(new Point(endPoint.x - startPenetration, endPoint.y), endPoint);
         if(!ignore)
         {
            GraphicsOperations.drawThickLine(myG2,endLine,2);
         }
         line=new Line(startPoint, new Point(endPoint.x-startPenetration,endPoint.y));

         if(!ignore)
         {
            GraphicsOperations.drawThickLine(myG2,line, 2);
         }
         arrowTop=new Line(new Point(endPoint.x-arrowSize,endPoint.y-arrowSize),endPoint);
         arrowBottom=new Line(new Point(endPoint.x-arrowSize,endPoint.y+arrowSize),endPoint);
         if(!ignore)
         {
            GraphicsOperations.drawThickLine(myG2, arrowTop, 2);
            GraphicsOperations.drawThickLine(myG2, arrowBottom, 2);
//            myG2.fill(arrow);
         }
         String edgeString =edgeToString(edge);
         Point midpoint=new Point((line.x1+line.x2)/2,(line.y1+line.y2)/2);
         Point labelStartPoint=new Point(midpoint.x-(labelWidth/2),(midpoint.y-(labelHeight/2))+(labelHeight*otherWithSamePath));
         Point labelEndPoint= new Point(labelStartPoint.x+labelWidth, labelStartPoint.y+labelHeight);
         Rectangle labelRect=new Rectangle(labelStartPoint.x,labelStartPoint.y,labelWidth,labelHeight);
//         myG2.fill(labelRect);
//         myG2.setColor(Color.white);
//         GraphicsOperations.drawCenteredString(myG2, labelStartPoint,labelEndPoint, edgeString);


         if(!ignore)
         {
            shapes.add(startLine);
            shapes.add(endLine);
            shapes.add(line);
            shapes.add(arrowTop);
            shapes.add(arrowBottom);
         }
//         shapes.add(labelRect);
         myShapeMap.put(edge,shapes);
      }
   }

   public void drawEdges(FlowGraphView graph, Graphics2D g2)
   {
      try
      {
         myG2=g2;
         myGraph=graph;
//         labelWidth=Math.min(myGraph.getHSpacing(),60);
         labelWidth=Math.min(myGraph.getHSpacing(),100);
         labelHeight=(int)(labelWidth*.4);
         myGraph.removeMouseMotionListener(this);
         myGraph.addMouseMotionListener(this);
         drawEdges();
      }
      catch (Exception e)
      {
         log.error("there was an error drawing the edges",e);
         e.printStackTrace();
      }
   }

   public void mouseDragged(MouseEvent e){}

   public void mouseMoved(MouseEvent e)
   {
      try
      {
         boolean found=false;
         String edgeString;
         for(Edge<V,E> edge:myGraph.getFlowGraph().getEdges())
         {
            for(Shape shape:myShapeMap.get(edge))
            {
               if(shape.intersects(new Rectangle(new Point(e.getPoint().x-mouseSensitivity,e.getPoint().y-mouseSensitivity),new Dimension(mouseSensitivity*2,mouseSensitivity*2))))
               {
                  found=true;
                  Image edgeName=new BufferedImage(labelWidth,labelHeight,BufferedImage.TYPE_INT_RGB);
                  edgeString=edgeToString(edge);
                  myFocusEdge =edge;
                  break;
               }
            }
         }
         if(!found)
         {
            myFocusEdge =null;
         }
      }
      catch (Throwable t)
      {
         log.error("there was an error handling mouse move",t);
      }
   }
   public String edgeToString(Edge<V,E> edge)
   {
      return edge.getValue().toString();
   }
   public Edge<V,E> getFocusEdge()
   {
      return myFocusEdge;
   }
}
