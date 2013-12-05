package com.jtaylor.util.gui;

import com.jtaylor.util.ColorOperations;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.EventObject;
import java.util.Map;

public class JTableHelper
{
	public static final Color SELECTED= ColorOperations.midnight;
	public static final int PADX = 10;
	public static final int PADY = 10;

	public static JComponent _getJComponent(Object value)
	{
		if (value == null)
		{
			JLabel label = new JLabel("");
			label.setOpaque(true);
			label.setForeground(Color.black);
			label.setBackground(Color.white);
			return label;
		}
		else if (value instanceof JComponent)
		{
			return (JComponent) value;
		}
		else if (value instanceof Image)
		{
			Image image=(Image)value;
			JLabel label = new JLabel(new ImageIcon(image));
			label.setOpaque(true);
			label.setForeground(Color.black);
			label.setBackground(Color.white);
			return label;
		}
		else
		{
			JLabel label = new JLabel(value.toString());
			label.setOpaque(true);
			label.setForeground(Color.black);
			label.setBackground(Color.white);
			return label;
		}
	}
	public static TableCellRenderer getToStringTableCellRenderer()
	{
		return new TableCellRenderer()
		{
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
			{
				JLabel label=new JLabel();
				if(value!=null)
				{
					label.setText(value.toString());
				}
				if(isSelected)
				{
					label.setOpaque(true);
					label.setForeground(Color.white);
					label.setBackground(Color.blue);
				}
				return label;
			}
		};
	}

	public static TableCellRenderer getStringMapRenderer(Map<?, String> map)
	{
		return new StringMapRenderer(map);
	}

	public static class StringMapRenderer implements TableCellRenderer,ListCellRenderer
	{
		Map<?, String> myMap;

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
         if (myMap.containsKey(value))
         {
            label.setText(myMap.get(value));
         }
         if (isSelected)
         {
            label.setOpaque(true);
            label.setForeground(Color.white);
            label.setBackground(Color.blue);
         }
         return label;
      }

	}
	public static TableCellRenderer getJComponentTableCellRenderer()
	{
		return getJComponentTableCellRenderer(false,false);
	}

	public static TableCellRenderer getSelectableJComponentTableCellRenderer()
	{
		return getJComponentTableCellRenderer(false, true);
	}

	public static TableCellRenderer getJComponentTableCellRenderer(final boolean resize, final boolean selectable)
	{
		return new TableCellRenderer()
		{
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
			{
				JComponent component = _getJComponent(value);
				if (resize)
				{
					int height = component.getMinimumSize().height;
					int width = component.getSize().width;
					if (height > table.getRowHeight(row))
					{
						table.setRowHeight(row, height + PADY);
					}
					TableColumn col = table.getColumn(table.getColumnName(column));
					if (width > col.getMinWidth())
					{
						col.setMinWidth(width + PADX);
					}
				}
				if(selectable)
				{
					component.setOpaque(true);
					component.setBackground(isSelected?SELECTED:Color.white);
					component.setForeground(isSelected?Color.white:Color.black);
				}
				return component;
			}
		};
	}

	public static TableCellEditor getJComponentTableCellEditor(final boolean readOnly)
	{
		return getJComponentTableCellEditor(readOnly, false);
	}

	public static TableCellEditor getJComponentTableCellEditor(final boolean readOnly, final boolean resize)
	{
		return new TableCellEditor()
		{
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
			{
				JComponent component = _getJComponent(value);
				if (resize)
				{
					int height = component.getMinimumSize().height;
					int width = component.getSize().width;
					if (height > table.getRowHeight(row))
					{
						table.setRowHeight(row, height);
					}
					TableColumn col = table.getColumn(table.getColumnName(column));
					if (width > col.getMinWidth())
					{
						col.setMinWidth(width);
					}
				}
				return component;
			}

			public void addCellEditorListener(CellEditorListener l)
			{
			}

			public void cancelCellEditing()
			{
			}

			public Object getCellEditorValue()
			{
				return null;
			}

			public boolean isCellEditable(EventObject anEvent)
			{
				return !readOnly;
			}

			public void removeCellEditorListener(CellEditorListener l)
			{
			}

			public boolean shouldSelectCell(EventObject anEvent)
			{
				return true;
			}

			public boolean stopCellEditing()
			{
				return true;
			}
		};
	}
}
