package com.jtaylor.util.gui;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: Apr 23, 2010
 * Time: 9:48:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegexInputField extends JTextField implements DocumentListener
{
	private Pattern myPattern;
	public RegexInputField()
	{
		super();
		getDocument().addDocumentListener(this);
		myPattern=null;
	}
	public boolean isInputValid()
	{
		return myPattern!=null;
	}
	public Pattern getPattern()
	{
		return myPattern;
	}
	public void removeUpdate(DocumentEvent	e)
	{
		update();
	}
	public void insertUpdate(DocumentEvent e)
	{
		update();
	}
	public void changedUpdate(DocumentEvent e)
	{
		update();
	}
	private void update()
	{
		String text = getText();
		if (text == null || text.equals(""))
		{
			setForeground(Color.red);
		}
		else
		{
			try
			{
				myPattern=Pattern.compile(text);
				setForeground(Color.green);
				setToolTipText(null);
			}
			catch (Exception e)
			{
				myPattern=null;
				setForeground(Color.red);
				setToolTipText(e.getMessage());
			}
		}
	}
	public boolean isEmpty()
	{
		return getText()==null||getText().equals("");
	}
}
