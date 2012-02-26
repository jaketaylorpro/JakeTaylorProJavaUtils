package com.jtaylor.util.gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 1/8/11
 * Time: 10:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateInputField extends JFormattedTextField implements DocumentListener
{
   private SimpleDateFormat myFormat;
   private Date myValue;
   private boolean myIsValid;

   public DateInputField(SimpleDateFormat format,MaskFormatter mask)
   {
      super();
      getDocument().addDocumentListener(this);
      mask.install(this);
      myFormat = format;
      myValue = null;
      myIsValid=true;
   }

   public void setDate(Date date)
   {
      if(date==null)
      {
         setText("");
      }
      else
      {
         setText(myFormat.format(date));
      }
   }
   public Date getDate()
   {
      return myValue;
   }
   public void update()
   {
      if (isEmpty())
      {
         myValue=null;
         setForeground(Color.red);
         myIsValid=true;
      }
      else
      {
         try
         {
            myValue=myFormat.parse(getText());
            setForeground(Color.green);
            setToolTipText(null);
            myIsValid=true;
         }
         catch (Exception e)
         {
            myValue=null;
            setForeground(Color.red);
            setToolTipText(e.getMessage());
            myIsValid=false;
         }
      }
   }

   public void insertUpdate(DocumentEvent e)
   {
      update();
   }

   public void removeUpdate(DocumentEvent e)
   {
      update();
   }

   public void changedUpdate(DocumentEvent e)
   {
      update();
   }

   public boolean isEmpty()
   {
      return getText() == null || getText().equals("");
   }
   public boolean isValid()
   {
      return myIsValid;
   }
}
