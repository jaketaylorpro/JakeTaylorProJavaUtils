package com.jtaylor.util.gui;

import javax.swing.JLabel;
import java.awt.Font;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: Aug 10, 2010
 * Time: 12:55:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class BoldJLabel extends JLabel
{
	public BoldJLabel(String text)
	{
		super(text);
		setFont(new Font(getFont().getName(),Font.BOLD,getFont().getSize()));
	}
}
