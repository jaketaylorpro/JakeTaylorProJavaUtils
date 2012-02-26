package com.jtaylor.util.gui.rwindow;

import java.awt.Component;

public abstract class RView extends Component
{
	protected RWindow parentWindow;
	public void setParentWindow(RWindow parent)
	{
		parentWindow=parent;
	}
	public RWindow getParentWindow()
	{
		return parentWindow;
	}
}
