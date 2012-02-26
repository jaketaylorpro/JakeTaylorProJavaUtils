package com.jtaylor.util.gui;

import com.jtaylor.util.ColorOperations;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/17/11
 * Time: 4:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ToStringRenderer implements ListCellRenderer,TableCellRenderer
{
   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
   {
      return render(value,isSelected);
   }

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
   {
      return render(value,isSelected);
   }

   public Component render(Object value,boolean isSelected)
   {
      JLabel label=new JLabel();
      label.setText(value==null?"null":value.toString());
      if(isSelected)
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
