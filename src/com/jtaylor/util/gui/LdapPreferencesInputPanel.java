package com.jtaylor.util.gui;

import com.jtaylor.util.LdapOperations;
import com.jtaylor.util.Logging;
import com.jtaylor.util.gui.rwindow.ActionHelper;
import com.jtaylor.util.gui.rwindow.GBCHelper;
import com.jtaylor.util.gui.rwindow.RBar;
import com.jtaylor.util.gui.rwindow.RDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import static com.jtaylor.util.LdapOperations.*;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 5/27/11
 * Time: 8:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class LdapPreferencesInputPanel extends JPanel
{
   private JTextField serverField;
   private JTextField usernameField;
   private JPasswordField passwordField;
   private JSpinner portField;
   private JTextField userDNField;
   private JTextField roleDNField;
   public LdapPreferencesInputPanel()
   {
      super(new GridBagLayout());
      serverField=new JTextField();
      usernameField=new JTextField();
      passwordField=new JPasswordField();
      portField=new JSpinner(new SpinnerNumberModel(389,1,9999,1));
      userDNField=new JTextField();
      roleDNField=new JTextField();
      int gridy=0;
      add(new JLabel("LDAP Server"), GBCHelper.getInlineGBC(0, gridy, 2));
      add(serverField,GBCHelper.getInlineGBC(2,gridy++,2));
      add(new JLabel("Distinct Path of User Name"), GBCHelper.getInlineGBC(0, gridy, 2));
      add(usernameField, GBCHelper.getInlineGBC(2, gridy++, 2));
      add(new JLabel("Password"), GBCHelper.getInlineGBC(0, gridy, 2));
      add(passwordField, GBCHelper.getInlineGBC(2, gridy++, 2));
      add(new JLabel("Port"), GBCHelper.getInlineGBC(0, gridy, 2));
      add(portField, GBCHelper.getInlineGBC(2, gridy, 1));
      gridy++;
      add(new JLabel("BaseDN for User Names"), GBCHelper.getInlineGBC(0, gridy, 2));
      add(userDNField, GBCHelper.getInlineGBC(2, gridy++, 2));
      add(new JLabel("BaseDN for Role Names"), GBCHelper.getInlineGBC(0, gridy, 2));
      add(roleDNField, GBCHelper.getInlineGBC(2, gridy++, 2));
   }
   public LdapPreferencesInputPanel(Map<String,Object> prefs)
   {
      this();
      setSettings(prefs);
   }
   public Map getSettingsAsMap()
   {
      Map<String,Object> map=new HashMap<String,Object>();
      map.put(KEY_SERVER,serverField.getText());
      map.put(KEY_USERNAME,usernameField.getText());
      map.put(KEY_PASSWORD,new String(passwordField.getPassword()));
      map.put(KEY_PORT,(Integer)portField.getValue());
      map.put(KEY_DN_USER, userDNField.getText());
      map.put(KEY_DN_ROLE, roleDNField.getText());
      return map;
   }
   public void setSettings(Map<String,Object> map)
   {
      if (map.containsKey(KEY_SERVER))
      {
         serverField.setText((String)map.get(KEY_SERVER));
      }
      if (map.containsKey(KEY_USERNAME))
      {
         usernameField.setText((String)map.get(KEY_USERNAME));
      }
      if (map.containsKey(KEY_PASSWORD))
      {
         passwordField.setText((String)map.get(KEY_PASSWORD));
      }
      if (map.containsKey(KEY_PORT))
      {
         portField.setValue((Integer) map.get(KEY_PORT));
      }
      if (map.containsKey(KEY_DN_USER))
      {
         userDNField.setText((String) map.get(KEY_DN_USER));
      }
      if (map.containsKey(KEY_DN_ROLE))
      {
         roleDNField.setText((String) map.get(KEY_DN_ROLE));
      }
   }
   public Action getTestAction(RBar parentBar)
   {
      final LdapPreferencesInputPanel dis=this;
      ActionHelper.ActionPerformer performer=new ActionHelper.ActionPerformer()
      {
         public void actionPerformed(ActionEvent e)
         {
            JTextField field=new JTextField();
            RDialog dialog=new RDialog("User Name",field,new RBar(true));
            dialog.promptOnTop();
            if(dialog.pressedOk())
            {
               try
               {
                  Logging.notifyUser("User email found: "+new LdapOperations(dis.getSettingsAsMap()).getEmailAddress(field.getText()));
               }
               catch(Exception er)
               {
                  Logging.notifyUser(er);
               }
            }
         }
      };
      ActionHelper.ActionEnabler enabler=new ActionHelper.ActionEnabler()
      {
         public boolean isEnabled()
         {
            return !serverField.getText().equals("")&&
                  !usernameField.getText().equals("")&&
                  !userDNField.getText().equals("")&&
                  !roleDNField.getText().equals("")&&
                  !new String(passwordField.getPassword()).equals("");
         }
      };
      Action action=ActionHelper.getNewButton("Lookup User Email",performer,enabler);
      serverField.getDocument().addDocumentListener(parentBar);
      usernameField.getDocument().addDocumentListener(parentBar);
      passwordField.getDocument().addDocumentListener(parentBar);
      userDNField.getDocument().addDocumentListener(parentBar);
      roleDNField.getDocument().addDocumentListener(parentBar);
      return action;
   }
   public boolean areSettingsValid()
   {
      return !serverField.getText().equals("")&&
            !usernameField.getText().equals("")&&
            passwordField.getPassword().length>0&&
            !userDNField.getText().equals("")&&
            !roleDNField.getText().equals("");
   }
}
