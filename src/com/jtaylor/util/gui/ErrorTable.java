package com.jtaylor.util.gui;

import com.jtaylor.util.gui.rwindow.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 8/19/11
 * Time: 12:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ErrorTable <T> extends JPanel
{
   JTable myErrorTable;
   DefaultTableModel myErrorTableModel;
   Map<T,Throwable> myErrorMap;
   public ErrorTable(Map<T,Throwable> errorMap)
   {
      super(new BorderLayout());
      myErrorMap=errorMap;
      Object[] columns={"Item","ErrorType","ErrorMessage"};
      Object[][] rows=new Object[errorMap.size()][3];
      int i=0;
      for(T t:errorMap.keySet())
      {
         rows[i][0]=t;
         rows[i][1]=errorMap.get(t).getClass().getSimpleName();
         rows[i][2]=errorMap.get(t).getMessage();
         i++;
      }
      myErrorTableModel=new DefaultTableModel(rows,columns);
      myErrorTable = new JTable(myErrorTableModel)
      {
         @Override
         public boolean isCellEditable(int i, int i1)
         {
            return false;
         }
      };
      myErrorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      myErrorTable.getColumn(columns[0]).setWidth(100);
      myErrorTable.getColumn(columns[0]).setWidth(75);
      myErrorTable.getColumn(columns[0]).setWidth(300);
      add(new JScrollPane(myErrorTable), BorderLayout.CENTER);
   }
   public static void main(String[] args)
   {
      Map<Integer,Throwable> errorMap=new HashMap<Integer,Throwable>();
      for(int i=0;i<10;i++)
      {
         errorMap.put(i,new NullPointerException("error message"));
      }
      new RDialog("Errors",new ErrorTable<Integer>(errorMap),new RBar(false)).display();
   }
   public Throwable getError(T item)
   {
      return myErrorMap.get(item);
   }
   public Throwable getError(int index)
   {
      return getError((T)myErrorTableModel.getValueAt(index,0));
   }
   public void display()
   {
      RAction detailsAction=ActionHelper.getNewButton("Details",new ActionHelper.ActionPerformer()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            Throwable t=getError(myErrorTable.getSelectedRow());
            JPanel panel=new JPanel(new BorderLayout());
            JPanel stackTracePanel=new JPanel(new GridBagLayout());
            int gridy=0;
            for(StackTraceElement ste:t.getStackTrace())
            {
               stackTracePanel.add(new JLabel(ste.toString()), GBCHelper.getInlineGBC(0,gridy++));
            }
            panel.add(stackTracePanel,BorderLayout.CENTER);
            JLabel messageLabel=new JLabel("<html><p style='width:350px'>"+t.getMessage()+"</p></html>");
            messageLabel.setBackground(Color.white);
            messageLabel.setOpaque(true);
            panel.add(messageLabel,BorderLayout.NORTH);
            new RDialog("Error Details",panel,new RBar(false)).display();
         }
      },new ActionHelper.ActionEnabler()
      {
         public boolean isEnabled()
         {
            return myErrorTable.getSelectedRow()>-1;
         }
      });
      RBar bar=new RBar(detailsAction);
      myErrorTable.getSelectionModel().addListSelectionListener(bar);
      new RDialog("Errors",this,bar).display();
   }
}
