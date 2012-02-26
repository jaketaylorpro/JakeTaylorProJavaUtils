package com.jtaylor.util.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 3/29/11
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class ColoredToggleButton extends JToggleButton
{
   //TODO doesnt work because set selected is not reliably called
   Color mySelectedFG;
   Color mySelectedBG;
   Color myDeselectedFG;
   Color myDeselectedBG;
   public ColoredToggleButton(Action a,Color foregroundSelected,Color backgroundSelected,Color foregroundDeselected,Color backgroundDeselected)
   {
      super(a);
      setOpaque(true);
      mySelectedFG=foregroundSelected;
      mySelectedBG=backgroundSelected;
      myDeselectedFG=foregroundDeselected;
      myDeselectedBG=backgroundDeselected;
      applyColor(false);
   }

   @Override
   public void setSelected(boolean b)
   {
      super.setSelected(b);
      applyColor(b);
   }
   public void applyColor(boolean selected)
   {
      System.out.println("applyiing color: "+selected);
      setForeground(selected ? mySelectedFG : myDeselectedFG);
      setBackground(selected ? mySelectedBG : myDeselectedBG);
   }
}
