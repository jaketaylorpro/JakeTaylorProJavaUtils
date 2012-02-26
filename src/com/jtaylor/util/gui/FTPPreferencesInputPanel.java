package com.jtaylor.util.gui;

import com.jtaylor.util.FTPOperations;
import com.jtaylor.util.gui.rwindow.GBCHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 7/17/11
 * Time: 3:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class FTPPreferencesInputPanel extends JPanel
{
   @Override
   public void setEnabled(boolean b)
   {
      super.setEnabled(b);
      for(Component c:getComponents())
      {
         c.setEnabled(b);
      }
   }

   private JTextField myServerField;
   private JTextField myUsernameField;
   private JPasswordField myPasswordField;
   private JTextField myBasePathField;
   private JButton myTestButton;
   public static final String KEY_SERVER="server";
   public static final String KEY_USER="user";
   public static final String KEY_PASS="pass";
   public static final String KEY_PATH="path";

   public FTPPreferencesInputPanel()
   {
      super(new GridBagLayout());
      myServerField=new JTextField();
      myUsernameField=new JTextField();
      myPasswordField=new JPasswordField();
      myBasePathField=new JTextField();
      myTestButton=new JButton("test");
      myTestButton.setForeground(Color.black);
      myTestButton.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent actionEvent)
         {
            try
            {
               if(getFTPOperations().testPath(myBasePathField.getText()))
               {
                  myTestButton.setForeground(Color.green);
                  myTestButton.setToolTipText("Success");
               }
               else
               {
                  myTestButton.setForeground(Color.red);
                  myTestButton.setToolTipText("Bad Path");
               }
            }
            catch (Exception e)
            {
               myTestButton.setForeground(Color.red);
               myTestButton.setToolTipText(e.getClass().getName()+": "+e.getMessage());
            }
         }
      });
      int gridy=0;
      add(new JLabel("Server"), GBCHelper.getLabelGBC(gridy));
      add(myServerField,GBCHelper.getFieldGBC(gridy++));
      add(new JLabel("Username"),GBCHelper.getLabelGBC(gridy));
      add(myUsernameField,GBCHelper.getFieldGBC(gridy++));
      add(new JLabel("Password"),GBCHelper.getLabelGBC(gridy));
      add(myPasswordField,GBCHelper.getFieldGBC(gridy++));
      add(new JLabel("Base Path"),GBCHelper.getLabelGBC(gridy));
      add(myBasePathField,GBCHelper.getFieldGBC(gridy++));
      add(myTestButton,GBCHelper.getInlineGBC(0,gridy++,2));
   }

   public FTPOperations getFTPOperations()
   {
      return new FTPOperations(myServerField.getText(),myUsernameField.getText(),new String(myPasswordField.getPassword()));
   }
   public static FTPOperations getFTPOperations(Map<String,Object> prefs)
   {
      return new FTPOperations((String)prefs.get(KEY_SERVER),(String)prefs.get(KEY_USER),(String)prefs.get(KEY_PASS));
   }

   public static String getBasePath(Map<String,Object> prefs)
   {
      return (String)prefs.get(KEY_PATH);
   }
   public String getBasePath()
   {
      return myBasePathField.getText();
   }
   public Map<String,Object> getPreferenceMap()
   {
      Map<String,Object> prefs=new HashMap<String, Object>();
      prefs.put(KEY_SERVER,myServerField.getText());
      prefs.put(KEY_USER,myUsernameField.getText());
      prefs.put(KEY_PASS,new String(myPasswordField.getPassword()));
      prefs.put(KEY_PATH,myBasePathField.getText());
      return prefs;
   }
   public void setPreferences(Map<String,Object> prefs)
   {
      if(prefs.containsKey(KEY_SERVER))
      {
         myServerField.setText((String)prefs.get(KEY_SERVER));
      }
      if(prefs.containsKey(KEY_USER))
      {
         myUsernameField.setText((String)prefs.get(KEY_USER));
      }
      if(prefs.containsKey(KEY_PASS))
      {
         myPasswordField.setText((String)prefs.get(KEY_PASS));
      }
      if(prefs.containsKey(KEY_PATH))
      {
         myBasePathField.setText((String)prefs.get(KEY_PATH));
      }
   }
}
