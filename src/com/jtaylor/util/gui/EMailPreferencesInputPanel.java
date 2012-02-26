package com.jtaylor.util.gui;

import com.jtaylor.util.EmailOperations.EMail;
import com.jtaylor.util.EmailOperations.SMTP;
import com.jtaylor.util.Logging;
import com.jtaylor.util.gui.rwindow.ActionHelper;
import com.jtaylor.util.gui.rwindow.ActionHelper.ActionEnabler;
import com.jtaylor.util.gui.rwindow.ActionHelper.ActionPerformer;
import com.jtaylor.util.gui.rwindow.GBCHelper;
import com.jtaylor.util.gui.rwindow.RBar;
import com.jtaylor.util.gui.rwindow.RDialog;

import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import static com.jtaylor.util.EmailOperations.SMTP.*;
/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: May 23, 2010
 * Time: 7:30:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class EMailPreferencesInputPanel extends JPanel
{
	private JTextField serverField;
	private JTextField usernameField;
	private JPasswordField passwordField;
   private JCheckBox sslField;
   private JSpinner portField;
   private JTextField defaultFromField;
   public EMailPreferencesInputPanel()
	{
		super(new GridBagLayout());
		serverField=new JTextField();
		usernameField=new JTextField();
		passwordField=new JPasswordField();
		sslField=new JCheckBox("SSL");
		portField=new JSpinner(new SpinnerNumberModel(25,1,99999,1));
		sslField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(sslField.isSelected()&&portField.getValue().equals(25))
				{
					portField.setValue(465);
				}
				else if (!sslField.isSelected() && portField.getValue().equals(465))
				{
					portField.setValue(25);
				}
			}
		});
      defaultFromField=new JTextField();
		int gridy=0;
		add(new JLabel("SMTP Server"), GBCHelper.getInlineGBC(0,gridy,2));
		add(serverField,GBCHelper.getInlineGBC(2,gridy++,2));
		add(new JLabel("SMTP User Name"), GBCHelper.getInlineGBC(0, gridy, 2));
		add(usernameField, GBCHelper.getInlineGBC(2, gridy++, 2));
		add(new JLabel("SMTP Password"), GBCHelper.getInlineGBC(0, gridy, 2));
		add(passwordField, GBCHelper.getInlineGBC(2, gridy++, 2));
		add(new JLabel("SMTP Port"), GBCHelper.getInlineGBC(0, gridy, 2));
		add(portField, GBCHelper.getInlineGBC(2, gridy, 1));
		add(sslField,GBCHelper.getInlineGBC(3,gridy++,1));
	   add(new JLabel("Default From Address"),GBCHelper.getInlineGBC(0,gridy,2));
      add(defaultFromField,GBCHelper.getInlineGBC(2,gridy++,2));
   }
	public EMailPreferencesInputPanel(Map<String,Object> prefs)
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
		map.put(KEY_SSL,sslField.isSelected());
		map.put(KEY_DEF_FROM,defaultFromField.getText());
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
			portField.setValue((Integer)map.get(KEY_PORT));
		}
		if (map.containsKey(KEY_SSL))
		{
			sslField.setSelected((Boolean)map.get(KEY_SSL));
		}
      if(map.containsKey(KEY_DEF_FROM))
      {
         defaultFromField.setText((String)map.get(KEY_DEF_FROM));
      }
	}
	public Action getTestAction(RBar parentBar)
	{
		final EMailPreferencesInputPanel dis=this;
		ActionPerformer performer=new ActionPerformer()
		{
			public void actionPerformed(ActionEvent e)
			{
				JTextField field=new JTextField();
				RDialog dialog=new RDialog("Email Address",field,new RBar(true));
            dialog.setSize(300,75);
				dialog.promptOnTop();
				if(dialog.pressedOk())
				{
					try
					{
						new SMTP(dis.getSettingsAsMap()).send(new EMail(field.getText(),"Test Email","This test shows that your email settings were correct"));
					   Logging.notifyUserPrompt("Email sent successfully");
               }
					catch(Exception er)
					{
						Logging.notifyUserPrompt(er);
					}
				}
			}
		};
		ActionEnabler enabler=new ActionEnabler()
		{
			public boolean isEnabled()
			{
            return serverField.getText()!=null&&!serverField.getText().equals("")/*&&
                  usernameField.getText() != null && !usernameField.getText().equals("")&&
                  !new String(passwordField.getPassword()).equals("")*/;
			}
		};
		Action action=ActionHelper.getNewButton("Send Test Email",performer,enabler);
		serverField.getDocument().addDocumentListener(parentBar);
		usernameField.getDocument().addDocumentListener(parentBar);
		passwordField.getDocument().addDocumentListener(parentBar);
		return action;
	}
	public boolean areSettingsValid()
	{
      return serverField.getText()!=null&&!serverField.getText().equals("")&&
			   usernameField.getText()!=null&&!usernameField.getText().equals("")&&
			   passwordField.getPassword()!=null&&passwordField.getPassword().length>0;
	}
}
