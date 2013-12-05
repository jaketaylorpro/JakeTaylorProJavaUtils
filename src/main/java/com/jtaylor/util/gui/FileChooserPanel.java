package com.jtaylor.util.gui;

import com.jtaylor.util.ColorOperations;
import com.jtaylor.util.gui.rwindow.RDialog;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: Mar 16, 2010
 * Time: 4:20:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileChooserPanel extends ChooserPanel<File>
{
	public static Comparator<File> alphebeticFileComparator=new Comparator<File>(){
		public int compare(File o1, File o2)
		{
			if(o1==null)
			{
				if(o2==null)
				{
					return 0;
				}
				else
				{
					return -1;
				}
			}
			else if(o2==null)
			{
				return 1;
			}
			else
			{
				return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
			}
		}
	};
	public FileChooserPanel(File startingFolder)
	{
		this(startingFolder,PREFERRED_SIZE);
	}
	public FileChooserPanel(File startingFolder,Dimension paneSize)
	{
		super(new FileList(startingFolder),JListHelper.createJList(new SortedListModel(alphebeticFileComparator),new FileListRenderer()) ,paneSize);
		myAllList.getModel().addListDataListener(new ListDataListener(){
			public void intervalAdded(ListDataEvent e){handle(e);}
			public void intervalRemoved(ListDataEvent e){handle(e);}
			public void contentsChanged(ListDataEvent e){handle(e);}
			public void handle(ListDataEvent e)
			{
				myAllLabel.setText(((FileListModel)e.getSource()).getCurrentDirectory().getName());
			}
		});
		myAllLabel.setText(((FileListModel)myAllList.getModel()).getCurrentDirectory().getName());

	}
	public static void main(String[] args)
	{
		new RDialog(new FileChooserPanel(new File("."))).display();
	}
	private static class FileListRenderer implements ListCellRenderer
	{
//		private static Icon FOLDER_ICON=(Icon)UIManager.get("Tree.collapsedIcon");
//		private static Icon FILE_ICON=(Icon)UIManager.get("Tree.expandedIcon");
		private static Icon FOLDER_ICON=(Icon)UIManager.get("Tree.closedIcon");
		private static Icon FILE_ICON=(Icon)UIManager.get("Tree.leafIcon");
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			JLabel label;
			if(value==null)
			{
				label=new JLabel("..",JLabel.LEADING);
			}
			else
			{
				File file=(File)value;
				label=new JLabel(file.getName(),(file.isDirectory()?FOLDER_ICON:FILE_ICON),JLabel.LEADING);
			}
			if(isSelected)
			{
				label.setOpaque(true);
				label.setBackground(ColorOperations.midnight);
				label.setForeground(Color.white);
			}
			return label;
		}
	}
	private static class FileList extends JList
	{
		public FileList(File currentDirectory)
		{
			super(new FileListModel(currentDirectory));
			final FileList dis=this;
			addMouseListener(new MouseListener(){
				public void mouseClicked(MouseEvent e)
				{
					if(e.getClickCount()==2)
					{
						int index = dis.locationToIndex(e.getPoint());
						if(index>=0&&index<dis.getModel().getSize())
						{
							FileListModel model= (FileListModel) dis.getModel();	
							File file=(File)model.getElementAt(index);
							if(file==null)
							{
								model.setCurrentDirectory(model.getCurrentDirectory().getParentFile());
							}
							else if(file.isDirectory())
							{
								model.setCurrentDirectory(file);
							}
						}
					}
				}
				public void mousePressed(MouseEvent e){}
				public void mouseReleased(MouseEvent e){}
				public void mouseEntered(MouseEvent e){}
				public void mouseExited(MouseEvent e){}
			});
			setCellRenderer(new FileListRenderer());
		}


	}
	private static class FileListModel implements ListModel
	{
		private static FileFilter fileFilter=new FileFilter()
		{
			public boolean accept(File pathname)
			{
				return !pathname.getName().startsWith(".");
			}
		};;
		private List<ListDataListener> listeners;
		private File myDirectory;
		private boolean hasParent;
		public FileListModel(File currentDirectory)
		{
			super();
			listeners=new Vector<ListDataListener>();
			setCurrentDirectory(currentDirectory);
		}

		public File getCurrentDirectory()
		{
			return myDirectory;
		}
		public void setCurrentDirectory(File currentDirectory)
		{
			if(myDirectory!=null)
			{
				notifyListDataListeners(new ListDataEvent(this,ListDataEvent.INTERVAL_REMOVED,0,getSize()-(hasParent?0:1)));
			}
			myDirectory=currentDirectory.getAbsoluteFile();
			if(myDirectory.getName().equals("."))
			{
				myDirectory=myDirectory.getParentFile();
			}
			System.out.println("myNewDirectory: "+myDirectory.toString());
			hasParent=myDirectory.getParentFile()!=null;
			notifyListDataListeners(new ListDataEvent(this,ListDataEvent.INTERVAL_ADDED,0,getSize()-(hasParent?0:1)));
		}
		public int getSize()
		{
			return myDirectory.listFiles(fileFilter).length+(hasParent?1:0);
		}

		public Object getElementAt(int index)
		{
			if(hasParent)
			{
				if(index==0)
				{
					return null;
				}
				else
				{
					return myDirectory.listFiles(fileFilter)[index-1];
				}
			}
			else
			{
				return myDirectory.listFiles(fileFilter)[index];
			}
		}

		public void addListDataListener(ListDataListener l)
		{
			listeners.add(l);
		}

		public void removeListDataListener(ListDataListener l)
		{
			listeners.remove(l);
		}

		public void notifyListDataListeners(ListDataEvent e)
		{
			int type=e.getType();
			for(ListDataListener l:listeners)
			{
				if(type==ListDataEvent.CONTENTS_CHANGED)
				{
					l.contentsChanged(e);
				}
				else if(type==ListDataEvent.INTERVAL_ADDED)
				{
					l.intervalAdded(e);
				}
				else if(type==ListDataEvent.INTERVAL_REMOVED)
				{
					l.intervalRemoved(e);
				}
			}
		}
	}
}
