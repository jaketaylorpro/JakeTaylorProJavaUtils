package com.jtaylor.util.gui;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Color;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: May 28, 2010
 * Time: 6:15:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class ToggleBorder extends TitledBorder
{
	private boolean isToggled;
	private Color myFalseColor;
	private Color myTrueColor;
	public ToggleBorder(String title, Color falseColor, Color trueColor)
	{
		super(BorderFactory.createLineBorder(falseColor,2),title,TitledBorder.RIGHT,TitledBorder.TOP);
		isToggled=false;
		myFalseColor=falseColor;
		myTrueColor=trueColor;
	}
	public boolean isSelected()
	{
		return isToggled;
	}
	public void setSelected(boolean selected)
	{
		isToggled=selected;
		setBorder(BorderFactory.createLineBorder(isToggled?myTrueColor:myFalseColor,2));
	}
}
