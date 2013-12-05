package com.jtaylor.util.gui.rwindow.flowchart;

import com.jtaylor.util.Direction;
import com.jtaylor.util.Logging;
import com.jtaylor.util.gui.rwindow.ActionHelper;
import com.jtaylor.util.gui.rwindow.RBar;
import com.jtaylor.util.gui.rwindow.RFrame;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/3/11
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class FlowGraphView <V,E,L>  extends JPanel implements MouseListener,ChangeListener
{
   public interface SelectionListener <V,E,L>
   {
      public void vertexSelected(Vertex<V> vertex);
      public void deselected();
      public void laneSelected(Lane<L> vertex);
      public void edgeSelected(Edge<V,E> edge);
   }
   private List<SelectionListener<V,E,L>> mySelectionListeners;
   private List<ChangeListener> myChangeListeners;
   private List<ChangeListener> myResizeListeners;
//   private static final Color GRID_COLOR=Color.black;
   private static final Color BACKGROUND_COLOR=Color.white;
   private FlowGraph<V,E,L> myFlowGraph;
   private Dimension myCellSize;
   private Dimension myDimension;
   private int myHSpacing;
   private int myVSpacing;
   private BufferedVertexRenderer<V> myVertexRenderer;
   private BufferedEdgeRenderer<V,E,L> myEdgeRenderer;
   private BufferedLaneRenderer<L> myLaneRenderer;
   private Vertex<V> mySelectedVertex;
   private Lane<L> mySelectedLane;
   private Edge<V,E> mySelectedEdge;
   private Logger log;
   private boolean myClickable;
   public FlowGraphView(FlowGraph<V,E,L> flowGraph,BufferedVertexRenderer<V> vertexRenderer,BufferedEdgeRenderer<V,E,L> edgeRenderer,BufferedLaneRenderer<L> laneRenderer,Dimension cellSize,Dimension dimension, int VSpacing,int HSpacing)
   {
      super();
      log= Logging.createServerLogger(FlowGraphView.class);
      myFlowGraph=flowGraph;
      myCellSize=cellSize;
      myDimension=dimension;
      myVertexRenderer=vertexRenderer;
      myEdgeRenderer=edgeRenderer;
      myLaneRenderer=laneRenderer;
      myVSpacing=VSpacing;
      myHSpacing=HSpacing;
      myClickable=true;
      mySelectionListeners=new Vector<SelectionListener<V,E,L>>();
      myChangeListeners=new Vector<ChangeListener>();
      myResizeListeners=new Vector<ChangeListener>();
      Dimension size=getRealDimension();
      System.out.println("setting size: "+size);
      setSize(size);
      setPreferredSize(size);
//      setMinimumSize(size);
//      setMaximumSize(size);
      addMouseListener(this);
      log.debug("adding self as change listener to flowgraph");
      myFlowGraph.addChangeListener(this);
   }
   public void clearBuffer()
   {
      myVertexRenderer.clearBuffer();
      myLaneRenderer.clearBuffer();
      myEdgeRenderer.clearBuffer();
   }
   public void setVertexRenderer(BufferedVertexRenderer<V> vr)
   {
      myVertexRenderer=vr;
      repaint();
   }
   public void setLaneRenderer(BufferedLaneRenderer<L> lr)
   {
      myLaneRenderer=lr;
      repaint();
   }
   public void setEdgeRenderer(BufferedEdgeRenderer<V,E,L> er)
   {
      myEdgeRenderer=er;
      repaint();
   }
   public void setClickable(boolean clickable)
   {
      myClickable=clickable;
   }
   public void setCellSize(Dimension cellSize)
   {
      myCellSize=cellSize;
      Dimension size=getRealDimension();
      System.out.println("setting size: "+size);
//      setSize(size);
      setPreferredSize(size);
//      setMinimumSize(size);
//      setMaximumSize(size);
      notifyResizeListeners();
      repaint();
   }
   public Dimension getCellSize()
   {
      return myCellSize;
   }
   @Override
   public void paint(Graphics g)
   {
      try
      {
   //      super.paint(g);
         Graphics2D g2 = (Graphics2D) g;
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         drawGrid(g2);
         Point p1;
         Point p2;
         boolean selected;
         for(Lane<L> lane:myFlowGraph.getLanes())
         {
            p1=new Point(0,lane.getStart()*myCellSize.height+(lane.getStart()+1)*myVSpacing);
            p2=new Point(getRealDimension().width,(lane.getEnd()+1)*myCellSize.height+(lane.getEnd()+1)*myVSpacing);
            selected=lane==mySelectedLane;
            log.debug("drawing lane: "+lane+" in: "+p1+" - "+p2);
            myLaneRenderer.drawLane(lane,g2,p1,p2,myCellSize.width,selected);
         }
         for(Vertex<V> vertex:myFlowGraph.getVertices())
         {
            selected=vertex==mySelectedVertex;
            p1=new Point(vertex.getX(),vertex.getY());
            log.debug("vertex: "+vertex+" drawn at: "+ convertPointTo2D(p1, Direction.NORTH_WEST)+","+ convertPointTo2D(p1, Direction.SOUTH_EAST));
            myVertexRenderer.drawVertex(vertex,g2, convertPointTo2D(p1, Direction.NORTH_WEST), convertPointTo2D(p1, Direction.SOUTH_EAST),selected);
         }
         myEdgeRenderer.drawEdges(this,g2);
      }
      catch(Throwable t)
      {
         log.error("there was an error painting",t);
      }
   }
   public Dimension getRealDimension()
   {
      int width=myCellSize.width*myDimension.width;
      width+=(myDimension.width+1)*myHSpacing;//add spacing
      width+=myCellSize.width;//add width for the lane
      int height=myCellSize.height*myDimension.height;
      height+=+(myDimension.height+1)*myVSpacing;//add spacing
      return new Dimension(width,height);
   }
   public void drawGrid(Graphics2D g2)
   {
      g2.setColor(BACKGROUND_COLOR);
      Dimension realDimension=getRealDimension();
      g2.fill(new Rectangle2D.Double(0, 0, realDimension.width, realDimension.height));
//      g2.setColor(GRID_COLOR);
//      int x;
//      for(int i=0;i<=myDimension.width;i++)
//      {
//         x=i*(myCellSize.width+mySpacing);
//         g2.fillRect(x,0,mySpacing,myDimension.height*myCellSize.height+(myDimension.height+1)*mySpacing);
//      }
//      int y;
//      for(int i=0;i<=myDimension.height;i++)
//      {
//         y=i*(myCellSize.height+mySpacing);
//         g2.fillRect(0,y,myDimension.width*myCellSize.width+(myDimension.width+1)*mySpacing,mySpacing);
//      }
   }
   public static void main(String[] args)
   {
//      final JPopupMenu popup=new JPopupMenu("popup1");
//      popup.add(ActionHelper.getNewButton("button 1",new ActionHelper.ActionPerformer()
//      {
//         @Override
//         public void actionPerformed(ActionEvent e)
//         {
//            popup.setVisible(false);
//            JOptionPane.showMessageDialog(null,"button1 pressed");
//         }
//      },null));
//      popup.add(ActionHelper.getNewButton("button 2",new ActionHelper.ActionPerformer()
//      {
//         @Override
//         public void actionPerformed(ActionEvent e)
//         {
//            popup.setVisible(false);
//            JOptionPane.showMessageDialog(null,"button2 pressed");
//         }
//      },null));
//      popup.add(ActionHelper.getNewButton("button 3",new ActionHelper.ActionPerformer()
//      {
//         @Override
//         public void actionPerformed(ActionEvent e)
//         {
//            popup.setVisible(false);
//            JOptionPane.showMessageDialog(null,"button3 pressed");
//         }
//      },null));
      FlowGraph<String,String,String> flowgraph=new FlowGraph<String,String,String>();
      Vertex<String> v1=new Vertex<String>("test1",0,0);
      Vertex<String> v15=new Vertex<String>("test1.5",0,1);
      Vertex<String> v2=new Vertex<String>("test2",2,4);
      flowgraph.addVertex(v1);
      flowgraph.addVertex(v15);
      flowgraph.addVertex(v2);
      flowgraph.addEdge(new Edge<String, String>("edge1", v1, v2));
      flowgraph.addLane(new Lane<String>("lane1", 0, 1));
      flowgraph.addLane(new Lane<String>("lane2", 3, 3));
      flowgraph.addLane(new Lane<String>("lane3",4,4));
      final FlowGraphView<String,String,String> flowGraphView=new FlowGraphView<String,String,String>(
            flowgraph,
            new BufferedDefaultVertexRenderer<String>(new DefaultVertexRenderer<String>()),
//            new DecoratedVertexRenderer<String>(),
            new BufferedDefaultEdgeRenderer<String,String,String>(new DefaultEdgeRenderer<String, String,String>()),
            new BufferedDefaultLaneRenderer<String>(new DefaultLaneRenderer<String>()),
            new Dimension(200,100),new Dimension(3,5),60,60);
      RBar bar=new RBar(false);
      bar.removeAll();
      bar.add(ActionHelper.getNewButton("Z+", new ActionHelper.ActionPerformer()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            flowGraphView.setCellSize(new Dimension(flowGraphView.getCellSize().width + 10, flowGraphView.getCellSize().height + 10));
            flowGraphView.setVSpacing(flowGraphView.getVSpacing()+10);
            flowGraphView.setHSpacing(flowGraphView.getHSpacing()+10);
         }
      }, null));
      bar.add(ActionHelper.getNewButton("Z-", new ActionHelper.ActionPerformer()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            flowGraphView.setCellSize(new Dimension(flowGraphView.getCellSize().width-10,flowGraphView.getCellSize().height-10));
            flowGraphView.setVSpacing(flowGraphView.getVSpacing() - 10);
            flowGraphView.setHSpacing(flowGraphView.getHSpacing() - 10);
         }
      },null));
      final JToggleButton decorateButton=new JToggleButton("D");
      decorateButton.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            if (decorateButton.isSelected())
            {
               flowGraphView.setVertexRenderer(new BufferedDecoratedVertexRenderer(new DecoratedVertexRenderer<String>()));
               flowGraphView.setEdgeRenderer(new BufferedDefaultEdgeRenderer<String,String,String>(new DefaultEdgeRenderer<String, String, String>()));
//               flowGraphView.setLaneRenderer(new DefaultLaneRenderer<String>());
            }
            else
            {
               flowGraphView.setVertexRenderer(new BufferedDefaultVertexRenderer(new DefaultVertexRenderer<String>()));
               flowGraphView.setEdgeRenderer(new SimpleEdgeRenderer<String, String, String>());
//               flowGraphView.setLaneRenderer(new DefaultLaneRenderer<String>());
            }
            flowGraphView.repaint();
         }
      });
//      bar.add(new JSeparator(JSeparator.VERTICAL));
      bar.add(decorateButton);
//      bar.add(new JSeparator(JSeparator.VERTICAL));
      JPanel panel=new JPanel(new BorderLayout());
      panel.add(flowGraphView,BorderLayout.CENTER);
      final JScrollPane myInfoPane=new JScrollPane();
      myInfoPane.setViewportView(new JLabel("no selection"));
      myInfoPane.setVisible(false);
      panel.add(myInfoPane,BorderLayout.SOUTH);
      flowGraphView.addSelectionListener(new SelectionListener<String, String, String>()
      {
         public void vertexSelected(Vertex<String> vertex)
         {
            myInfoPane.setViewportView(new JLabel("vertex: "+vertex.getValue()));
         }

         public void deselected()
         {
            myInfoPane.setViewportView(new JLabel("no selection"));
         }

         public void laneSelected(Lane<String> lane)
         {
            myInfoPane.setViewportView(new JLabel("lane: "+lane.getValue()));
         }

         public void edgeSelected(Edge<String, String> edge)
         {
            myInfoPane.setViewportView(new JLabel("edge: "+edge.getValue()));
         }
      });
      final JToggleButton infoButton=new JToggleButton("i");
      infoButton.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            myInfoPane.setVisible(infoButton.isSelected());
         }
      });
      bar.add(infoButton);
//      bar.add(new JSeparator(JSeparator.VERTICAL));
      RFrame dialog=new RFrame("dialog",new JScrollPane(panel),bar);

      Dimension size=new Dimension(flowGraphView.getSize());
      size.width=size.width+1+3;
      size.height=size.height+1+dialog.getBar().getSize().height+25;
      System.out.println("setting dialog size: " + size);
      dialog.setSize(size);
//      dialog.setPreferredSize(size);
//      dialog.setMinimumSize(size);
      dialog.display();
   }
   public FlowGraph<V,E,L> getFlowGraph()
   {
      return myFlowGraph;
   }
   public Point convertPointTo2D(Point point, Direction direction)
   {
      int northY=point.y*myCellSize.height+(point.y+1)*myVSpacing;
      int westX=point.x*myCellSize.width+(point.x+1)*myHSpacing;
      westX+=myCellSize.width;
      if(direction== Direction.NORTH)
      {
         return new Point(westX+(int)(.5*myCellSize.width),northY);
      }
      else if(direction==Direction.NORTH_EAST)
      {
         return new Point(westX+myCellSize.width,northY);
      }
      else if(direction==Direction.EAST)
      {
         return new Point(westX+myCellSize.width,northY+(int)(.5*myCellSize.height));
      }
      else if(direction==Direction.SOUTH_EAST)
      {
         return new Point(westX+myCellSize.width,northY+myCellSize.height);
      }
      else if(direction==Direction.SOUTH)
      {
         return new Point(westX+(int)(.5*myCellSize.width),northY+myCellSize.height);
      }
      else if(direction==Direction.SOUTH_WEST)
      {
         return new Point(westX,northY+myCellSize.height);
      }
      else if(direction==Direction.WEST)
      {
         return new Point(westX,northY+(int)(.5*myCellSize.getHeight()));
      }
      else if(direction==Direction.NORTH_WEST)
      {
         return new Point(westX,northY);
      }
      else
      {
         return new Point(westX+(int)(.5*myCellSize.width),northY+(int)(.5*myCellSize.height));
      }
   }
   public Point convert2DToPoint(Point point)
   {
      int x=(point.x-myCellSize.width)/(myCellSize.width+myHSpacing);
      double xdiff=(point.x-myCellSize.width)-(x*(myCellSize.width+myHSpacing));
      log.debug("xdiff: "+xdiff);
      int y=point.y/(myCellSize.height+myVSpacing);
      double ydiff=point.y-(y*(myCellSize.height+myVSpacing));
      log.debug("ydiff: "+ydiff);
      return new Point(xdiff<myHSpacing?-1*x:x,ydiff<myVSpacing?-1*y:y);
   }
   public void mouseClicked(MouseEvent e)
   {
      if(myClickable)
      {
         handleMouseClick(e);
      }
   }
   protected void handleMouseClick(MouseEvent e)
   {
      boolean vertexSelected=false;
      boolean laneSelected=false;
      boolean edgeSelected=false;
      boolean previousSelection=mySelectedEdge!=null||mySelectedLane!=null||mySelectedVertex!=null;
      if(myEdgeRenderer.getFocusEdge()!=null)
      {
         mySelectedEdge=myEdgeRenderer.getFocusEdge();
         edgeSelected=true;
      }
      else
      {
         Point p= convert2DToPoint(e.getPoint());
         if(e.getX()<myCellSize.width)//than it might be a selected lane title
         {
            int laneY=(int)p.getY();
            log.debug("checking for lane selection, laneY: "+laneY);
            Lane<L> lane=myFlowGraph.getLaneByYCoord(Math.abs(laneY));
            log.debug("lane: "+lane);
            if(lane!=null)
            {
               if(laneY==0)
               {
                  if(e.getPoint().getY()>myVSpacing)
                  {
                     mySelectedLane=lane;
                     laneSelected=true;
                  }
                  else
                  {
                     log.debug("ignoring click in top spacing");
                  }
               }
               else if(laneY>0)
               {
                  mySelectedLane=lane;
                  laneSelected=true;
               }
               else
               {
                  int absLaneY=Math.abs(laneY);
                  if(lane==myFlowGraph.getLaneByYCoord(absLaneY-1))
                  {
                     mySelectedLane=lane;
                     laneSelected=true;
                  }
               }
            }
         }
         else
         {
            if(p.getX()<0||p.getY()<0)//than the click was in a spacer
            {
               log.debug("click was in a spacer");
            }
            else
            {
               for(Vertex<V> v:myFlowGraph.getVertices())
               {
                  if(p.equals(v.getLocation()))
                  {
                     mySelectedVertex=v;
                     vertexSelected=true;
                  }
               }
            }
         }
      }
      log.debug("click handler: "+vertexSelected+" , "+edgeSelected+" , "+laneSelected);
      if(!vertexSelected)
      {
         mySelectedVertex=null;
      }
      if(!laneSelected)
      {
         mySelectedLane=null;
      }
      if(!edgeSelected)
      {
         mySelectedEdge=null;
      }
      if(previousSelection&&!laneSelected&&!vertexSelected&&!edgeSelected)
      {
         notifyListenersOfDeselection();
      }
      else if(vertexSelected)
      {
         notifyListenersOfVertexSelection(mySelectedVertex);
      }
      else if(laneSelected)
      {
         notifyListenersOfLaneSelection(mySelectedLane);
      }
      else if(edgeSelected)
      {
         notifyListenersOfEdgeSelection(mySelectedEdge);
      }
      repaint();
      log.debug("click handler: "+mySelectedVertex+" , "+mySelectedEdge+" , "+mySelectedLane);
   }
   public Vertex<V> getSelectedVertex()
   {
      return mySelectedVertex;
   }
   public Lane<L> getSelectedLane()
   {
      return mySelectedLane;
   }
   public Edge<V,E> getSelectedEdge()
   {
      return mySelectedEdge;
   }
   public void deselect()
   {
      if(mySelectedEdge!=null||mySelectedLane!=null||mySelectedVertex!=null)
      {
         notifyListenersOfDeselection();
      }
      mySelectedEdge=null;
      mySelectedLane=null;
      mySelectedVertex=null;
   }
   public void setSelectedVertex(Vertex<V> vertex)
   {
      if(vertex==null||myFlowGraph.containsVertex(vertex))
      {
         deselect();
      }
      else
      {
         mySelectedVertex=vertex;
         notifyListenersOfVertexSelection(vertex);
      }
   }
   public void setSelectedVertex(Lane<L> lane)
   {
      if(lane==null||myFlowGraph.containsLane(lane))
      {
         deselect();
      }
      else
      {
         mySelectedLane=lane;
         notifyListenersOfLaneSelection(lane);
      }
   }
   public void setSelectedEdge(Edge<V,E> edge)
   {
      if(edge==null||myFlowGraph.containsEdge(edge))
      {
         deselect();
      }
      else
      {
         mySelectedEdge=edge;
         notifyListenersOfEdgeSelection(edge);
      }
   }
   public void mousePressed(MouseEvent e){}

   public void mouseReleased(MouseEvent e){}

   public void mouseEntered(MouseEvent e){}

   public void mouseExited(MouseEvent e){}

   public void stateChanged(ChangeEvent e)
   {
      //check to see if we need to increase size to fit all lanes and verticies
      log.debug("checking to see if we need to resize");
      boolean resized=false;
      int maxX=0;
      int maxY=myFlowGraph.getLastLaneIndex();
      log.debug("last lane index: "+maxY);
      for(Vertex<V> vertex:myFlowGraph.getVertices())
      {
         if(vertex.getX()>maxX)
         {
            log.debug("vertex found at x-index: "+vertex.getX());
            maxX=vertex.getX();
         }
         if(vertex.getY()>maxY)
         {
            log.debug("vertex found at y-index: "+vertex.getY());
            maxY=vertex.getY();
         }
      }
      maxX++;//because they are finding max indicies not sizes
      maxY++;
      log.debug("maxX="+maxX);
      log.debug("current-width="+myDimension.width);
      log.debug("maxY="+maxY);
      log.debug("current-height="+myDimension.height);
      if(maxX>myDimension.width)
      {
         resized=true;
         myDimension.width=maxX;
      }
      if(maxY>myDimension.height)
      {
         resized=true;
         myDimension.height=maxY;
      }
      log.debug("repaining");
      repaint();
      log.debug("done repaining");
      if(resized)
      {
         notifyResizeListeners();
      }
   }
   public void notifyListenersOfDeselection()
   {
      for(SelectionListener<V,E,L> l:new Vector<SelectionListener>(mySelectionListeners))
      {
         l.deselected();
      }
      notifyChangeListeners();
   }
   public void notifyListenersOfVertexSelection(Vertex<V> vertex)
   {
      for(SelectionListener<V,E,L> l:new Vector<SelectionListener>(mySelectionListeners))
      {
         l.vertexSelected(vertex);
      }
      notifyChangeListeners();
   }
   public void notifyListenersOfLaneSelection(Lane<L> lane)
   {
      for(SelectionListener<V,E,L> l:new Vector<SelectionListener>(mySelectionListeners))
      {
         l.laneSelected(lane);
      }
      notifyChangeListeners();
   }
   public void notifyListenersOfEdgeSelection(Edge<V,E> edge)
   {
      for(SelectionListener<V,E,L> l:new Vector<SelectionListener>(mySelectionListeners))
      {
         l.edgeSelected(edge);
      }
      notifyChangeListeners();
   }
   public void notifyChangeListeners()
   {
      for(ChangeListener l:new Vector<ChangeListener>(myChangeListeners))
      {
         l.stateChanged(new ChangeEvent(this));
      }
   }
   public void notifyResizeListeners()
   {
      for(ChangeListener l:new Vector<ChangeListener>(myResizeListeners))
      {
         l.stateChanged(new ChangeEvent(this));
      }
   }
   public void addSelectionListener(SelectionListener<V,E,L> l)
   {
      mySelectionListeners.add(l);
   }
   public void addChangeListener(ChangeListener l)
   {
      myChangeListeners.add(l);
   }
   public void addResizeListener(ChangeListener l)
   {
      myResizeListeners.add(l);
   }
   public boolean removeSelectionListener(SelectionListener<V,E,L> l)
   {
      return mySelectionListeners.remove(l);
   }
   public boolean removeChangeListener(ChangeListener l)
   {
      return myChangeListeners.remove(l);
   }
   public boolean removeResizeListener(ChangeListener l)
   {
      return myResizeListeners.remove(l);
   }
   public int getVSpacing()
   {
      return myVSpacing;
   }
   public void setVSpacing(int vspacing)
   {
      myVSpacing=vspacing;
      Dimension size=getRealDimension();
      System.out.println("setting size: "+size);
      setSize(size);
      setPreferredSize(size);
//      setMinimumSize(size);
//      setMaximumSize(size);
      notifyResizeListeners();
      repaint();
   }
   public void setHSpacing(int hspacing)
   {
      myHSpacing=hspacing;
      Dimension size=getRealDimension();
      System.out.println("setting size: "+size);
      setSize(size);
      setPreferredSize(size);
//      setMinimumSize(size);
//      setMaximumSize(size);
      notifyResizeListeners();
      repaint();
   }
   public int getHSpacing()
   {
      return myHSpacing;
   }
}
