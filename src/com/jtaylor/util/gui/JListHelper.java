package com.jtaylor.util.gui;

import com.jtaylor.util.ColorOperations;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;

public class JListHelper
{
	public static ListCellRenderer getStringMapRenderer(Map<?, String> map)
	{
		return new StringMapRenderer(map);
	}
	public static class StringMapRenderer implements ListCellRenderer
	{
		Map<?,String> myMap;
		public StringMapRenderer(Map<?,String> map)
		{
			myMap=map;
		}
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			JLabel label=new JLabel();
			if(myMap.containsKey(value))
			{
				label.setText(myMap.get(value));
			}
         else
         {
            label.setText(value==null?"null":value.toString());
         }
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

	public static class ToStringCellRenderer implements ListCellRenderer
	{
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
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
	public static ListCellRenderer TO_STRING_CELL_RENDERER=new ToStringCellRenderer();
	public static ListCellRenderer getToStringListCellRenderer()
	{
		return TO_STRING_CELL_RENDERER;
	}
	public static class JComponentCellRenderer implements ListCellRenderer
	{
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			if(value instanceof JComponent)
			{
				return (JComponent)value;
			}
			else
			{
				return null;
			}
		}
	}
	public static ListCellRenderer JCOMPONENT_CELL_RENDERER=new JComponentCellRenderer();
	public static ListCellRenderer getJComponentListCellRenderer()
	{
		return JCOMPONENT_CELL_RENDERER;	
	}
	public static JList createJList(ListModel model,ListCellRenderer renderer)
	{
		JList list= new JList(model);
		list.setCellRenderer(renderer);
		return list;
	}
	public static ListCellRenderer getHexColorRenderer()
	{
		return RENDERER_HEX_COLOR;
	}
	private static class HexColorRenderer extends JLabel implements ListCellRenderer
	{
		public HexColorRenderer()
		{
			super();
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			if (value != null)
			{
				Color color = ColorOperations.hexStringToColor((String) value);
				setIcon(new LabelIcon(color, list.getWidth()));
				setText(null);
				setOpaque(true);
				if (index >= 0)
				{
					setBorder(BorderFactory.createLineBorder((isSelected ? Color.blue : Color.white), 3));
				}
				else
				{
					setBorder(null);
				}
			}
			else
			{
				setOpaque(false);
				setBorder(null);
				setIcon(null);
				setText(null);
			}
			return this;
		}

		private static class LabelIcon implements Icon
		{
			private Color myColor;
			private int myWidth;

			public LabelIcon(Color color, int width)
			{
				myColor = color;
				myWidth = width;
			}

			public int getIconHeight()
			{
				return 10;
			}

			public int getIconWidth()
			{
				return myWidth;
			}

			public void paintIcon(Component c, Graphics g, int x, int y)
			{
				g.setColor(myColor);
				g.fillRect(x, y, getIconWidth(), getIconHeight());
			}
		}
	}
	public static ListCellRenderer RENDERER_HEX_COLOR=new HexColorRenderer();



	public static void addItemsToComboBox(JComboBox box,List items)
	{
		for(Object item:items)
		{
			box.addItem(item);
		}
	}
}
