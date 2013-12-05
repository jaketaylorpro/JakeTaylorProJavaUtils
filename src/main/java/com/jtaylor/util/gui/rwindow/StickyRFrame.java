package com.jtaylor.util.gui.rwindow;

import com.jtaylor.util.Logging;
import com.jtaylor.util.fileio.PLISTOperations;
import org.apache.log4j.Logger;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

public class StickyRFrame extends RFrame implements WindowListener
{
   protected String myID;
   protected String myKey;
   protected boolean myClosed=false;

   public static final String KEY_X= "WinX";
   public static final String KEY_Y= "WinY";
   public static final String KEY_W= "WinW";
   public static final String KEY_H= "WinH";
   public static final Point DEF_LOCATION=new Point(400,400);
   public static final Dimension DEF_DIMENSION=new Dimension(400,400);

   public StickyRFrame(String title, Component view, RBar bar,String ID,String key)
   {
      this(title,view,bar,ID,key,DEF_LOCATION, DEF_DIMENSION);
   }

   public StickyRFrame(Component view, String ID, String key, Point defaultLocation, Dimension defaultSize)
   {
      this(view.getName(), view, new RBar(false), ID, key, defaultLocation, defaultSize);
   }
   public StickyRFrame(String title, Component view, RBar bar, String ID,String key, Point defaultLocation, Dimension defaultSize)
   {

      super(title, view, bar);
      Logger log = Logging.createServerLogger(StickyRDialog.class);

      myID = ID;
      myKey=key;

      addWindowListener(this);

      Map<String,Object> dict=(HashMap<String,Object>) PLISTOperations.safelyGetPreference(myID, key, new HashMap<String, Object>());
      int x=defaultLocation.x;
      int y=defaultLocation.y;
      int w=defaultSize.width;
      int h=defaultSize.height;
      if(dict.containsKey(KEY_X))
      {
         x=(Integer)dict.get(KEY_X);
      }
      if(dict.containsKey(KEY_Y))
      {
         y=(Integer)dict.get(KEY_Y);
      }
      if(dict.containsKey(KEY_W))
      {
         w=(Integer)dict.get(KEY_W);
      }
      if(dict.containsKey(KEY_H))
      {
         h=(Integer)dict.get(KEY_H);
      }
      Point location = new Point(x,y);
      Dimension size = new Dimension(w,h);
      setSize(size);
      setPreferredSize(size);
      setLocation(location);
   }

   public void windowActivated(WindowEvent e)
   {
   }

   public void windowClosed(WindowEvent e)
   {
      handleClose();
   }

   public void windowClosing(WindowEvent e)
   {
      handleClose();
   }

   public void windowDeactivated(WindowEvent e)
   {
   }

   public void windowDeiconified(WindowEvent e)
   {
   }

   public void windowIconified(WindowEvent e)
   {
   }

   public void windowOpened(WindowEvent e)
   {
   }

   private void handleClose()
   {
      if(!myClosed)
      {
         log.debug("preferred size: " + getPreferredSize());
         log.debug("minimum size: " + getMinimumSize());
         log.debug("maximum size: " + getMaximumSize());
         HashMap<String,Object> dict=new HashMap<String, Object>();
         Point location = getLocation();
         Dimension size = getSize();
         dict.put(KEY_X,location.x);
         dict.put(KEY_Y,location.y);
         dict.put(KEY_W,size.width);
         dict.put(KEY_H,size.height);
         log.debug("saving window location: " + location);
         log.debug("saving window size: " + size);
         try
         {
            PLISTOperations.setPreference(myID, myKey,dict);
         }
         catch (Exception e)
         {
            Logging.notifyUser(e);
         }
         myClosed=true;
      }
   }
}
