package com.jtaylor.util.gui.rwindow;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

public interface RWindow
{
	public static final Color BORDER=Color.darkGray;
	public void setTitle(String title);
	public String getTitle();
	public void setView(Component view);
	public Component getView();
	public void setBar(RBar bar);
	public RBar getBar();
	public void display();
	public void displayOnTop();
	public void dispose();
	public void close();
	public boolean pressedOk();
	public Dimension getSize();
	public void setSize(Dimension size);
	public void setPressedOk(boolean ok);
}
