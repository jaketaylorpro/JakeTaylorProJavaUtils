package com.jtaylor.util.gui;

import com.jtaylor.util.Logging;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.text.*;
import java.lang.String;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 12/1/11
 * Time: 12:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class AutoCompleteBox extends JComboBox
{
   private Collection<String> myCompletes;
   private DefaultComboBoxModel myModel;
   private JTextComponent myTextEditor;
   private Logger log;
   public AutoCompleteBox(Collection<String> completes)
   {
      super();
      log= Logging.createServerLogger(AutoCompleteBox.class);
      myModel= (DefaultComboBoxModel) getModel();
      myCompletes=completes;
      for(String complete:completes)
      {
         myModel.addElement(complete);
      }
      setEditable(true);
      myTextEditor= (JTextComponent) editor.getEditorComponent();
      myTextEditor.setDocument(new PlainDocument()
      {
         @Override
         public void insertString(int i, String s, AttributeSet attributeSet) throws BadLocationException
         {
            super.insertString(i, s, attributeSet);    //To change body of overridden methods use File | Settings | File Templates.
            int pos=myTextEditor.getCaretPosition();
            String text=myTextEditor.getText();
            String relevantText=text.substring(0,pos);
            for(String complete:myCompletes)
            {
               if(complete.startsWith(relevantText))
               {
                  myTextEditor.setText(complete);
                  myTextEditor.setCaretPosition(pos);
                  myTextEditor.select(pos,complete.length());
                  break;
               }
            }
         }
      });
   }
   public void setText(String text)
   {
      myTextEditor.setText(text);
   }
}
