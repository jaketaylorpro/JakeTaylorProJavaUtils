package com.jtaylor.util.gui;

import com.jtaylor.util.ColorOperations;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/9/11
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringMapRenderer implements TableCellRenderer,ListCellRenderer
{
   private Map<?, String> myMap;

   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
   {
      return lookupString(value,isSelected);
   }
   public StringMapRenderer(Map<?, String> map)
   {
      myMap = map;
   }

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
   {
      return lookupString(value,isSelected);
   }

   public Component lookupString(Object value,boolean isSelected)
   {

      JLabel label = new JLabel();
      if(value==null)
      {
         label.setText("null");
      }
      else
      {
         if (myMap.containsKey(value))
         {
            label.setText(myMap.get(value));
         }
         else
         {
            label.setText(value.toString());
         }
      }
      if (isSelected)
      {
         label.setOpaque(true);
         label.setForeground(Color.white);
         label.setBackground(ColorOperations.midnight);
      }
      else
      {
         label.setOpaque(true);
         label.setForeground(Color.black);
         label.setBackground(Color.white);
      }
      return label;
   }

}
