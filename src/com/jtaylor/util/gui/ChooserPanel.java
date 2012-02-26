package com.jtaylor.util.gui;

import com.jtaylor.util.Logging;
import com.jtaylor.util.datastructures.ComparableComparator;
import com.jtaylor.util.gui.rwindow.GBCHelper;
import com.jtaylor.util.gui.rwindow.RBar;
import com.jtaylor.util.gui.rwindow.RDialog;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class ChooserPanel <T> extends JPanel
{
	public static final Dimension PREFERRED_SIZE=new Dimension(200,300);
	protected List<ActionListener> myListeners;
	protected JList myAllList;
	protected JList mySelectedList;
	protected JButton myUnSelectButton;
	protected JButton mySelectButton;
	protected JLabel myAllLabel;
	protected JLabel mySelectedLabel;
	protected Logger log;
	public static ChooserPanel createEasyChooserPanel(List<? extends Comparable> totalList)
	{
		return new ChooserPanel(totalList,new DefaultListCellRenderer(),new ComparableComparator()/*,PREFERRED_SIZE*/);
	}
	public ChooserPanel(List<T> totalList,ListCellRenderer renderer, Comparator<T> comparator)
	{
		this(totalList,renderer,comparator,PREFERRED_SIZE);
	}
	public ChooserPanel(List<T> totalList,ListCellRenderer renderer, Comparator<T> comparator,Dimension paneSize)
	{
		this(JListHelper.createJList(new SortedListModel(comparator,totalList),renderer),
			JListHelper.createJList(new SortedListModel(comparator),renderer),
			paneSize);
	}
	public ChooserPanel(JList allList,JList selectedList,Dimension paneSize)
	{
		super(new GridBagLayout());
		log= Logging.createServerLogger(ChooserPanel.class);
		myAllList=allList;
		mySelectedList=selectedList;
		myUnSelectButton=new JButton("->");
		myUnSelectButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SortedListModel model=(SortedListModel)mySelectedList.getModel();
				for(Object o:mySelectedList.getSelectedValues())
				{
					model.remove(o);
				}
				notifyAllListeners(e);
			}
		});
		mySelectButton=new JButton("<-");
		mySelectButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				selectAction();
				notifyAllListeners(e);				
			}
		});
		JScrollPane selectedPane=new JScrollPane(mySelectedList);
		if(paneSize!=null)selectedPane.setPreferredSize(paneSize);
		JScrollPane allPane=new JScrollPane(myAllList);
		if(paneSize!=null)allPane.setPreferredSize(paneSize);

		int gridy=0;
		mySelectedLabel=new JLabel("Selected");
		add(mySelectedLabel, GBCHelper.getStaticCenteredGBC(0,gridy));
		myAllLabel=new JLabel("All");
		add(myAllLabel, GBCHelper.getStaticCenteredGBC(1,gridy++));
		add(selectedPane, GBCHelper.getFullGBC(0,gridy));
		add(allPane, GBCHelper.getFullGBC(1,gridy++));
		add(myUnSelectButton, GBCHelper.getStaticCenteredGBC(0,gridy));
		add(mySelectButton, GBCHelper.getStaticCenteredGBC(1,gridy++));
		myListeners=new Vector<ActionListener>();
	}
	public List<T> getSelected()
	{
		ListModel model=mySelectedList.getModel();
		List<T> selected=new Vector<T>();
		for(int i=0;i<model.getSize();i++)
		{
			selected.add((T) model.getElementAt(i));
		}
		return selected;
	}
   public boolean addSelectedValue(T t)
   {
      SortedListModel selectedModel=(SortedListModel)mySelectedList.getModel();
      SortedListModel allModel=(SortedListModel)myAllList.getModel();
      if(allModel.contains(t))
      {
         selectedModel.add(t);
         return true;
      }
      else
      {
         return false;
      }
   }
	public static void main(String[] args)
	{
		ChooserPanel panel=createEasyChooserPanel(Arrays.asList("one","two","three"));
		RDialog dialog=new RDialog("test",panel,new RBar());
		dialog.display();
      try{Thread.sleep(10000);}catch(Exception e){}
	   panel.addSelectedValue("two");
   }
	protected void selectAction()
	{
		SortedListModel model=(SortedListModel)mySelectedList.getModel();
		for(Object o:myAllList.getSelectedValues())
		{
			model.add(o);
		}
	}

	public void addActionListener(ActionListener listener)
	{
		myListeners.add(listener);
	}
	public void removeActionListener(ActionListener listener)
	{
		myListeners.remove(listener);
	}
	protected void notifyAllListeners(ActionEvent event)
	{
		for(ActionListener listener:myListeners)
		{
			listener.actionPerformed(event);
		}
	}

   public void clearSelected()
   {
      SortedListModel selectedModel=(SortedListModel)mySelectedList.getModel();
      selectedModel.clear();
   }
}
