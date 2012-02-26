package com.jtaylor.util.gui.rwindow;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GBCHelper
{
	/**
	 * retuns a gridbagconstraints that will stretch horizontally.
	 * @param gridx its horizontal index
	 * @param gridy its vertical index
	 * @return the appropriate gridbagconstraints
	 */
	public static GridBagConstraints getInlineCenteredGBC(int gridx,int gridy)
	{
		return getInlineCenteredGBC(gridx,gridy,1);
	}
	public static GridBagConstraints getInlineCenteredGBC(int gridx,int gridy,int gridw)
	{
		return new GridBagConstraints(gridx,gridy,gridw,1,1,0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
	}
	public static GridBagConstraints getInlineGBC(int gridx,int gridy)
	{
		return getInlineGBC(gridx,gridy,1);
	}
	public static GridBagConstraints getInlineGBC(int gridx,int gridy,int gridw)
	{
		return new GridBagConstraints(gridx,gridy,gridw,1,1,0,GridBagConstraints.FIRST_LINE_START,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
	}
	public static GridBagConstraints getFullGBC(int gridx,int gridy)
	{
		return getFullGBC(gridx,gridy,1,1);
	}
	public static GridBagConstraints getFullGBC(int gridx,int gridy,int gridw,int gridh)
	{
		return new GridBagConstraints(gridx,gridy,gridw,gridh,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
	}
	public static GridBagConstraints getStaticCenteredGBC(int gridx,int gridy)
	{
		return new GridBagConstraints(gridx,gridy,1,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
	}
	public static GridBagConstraints getStaticGBC(int gridx,int gridy)
	{
		return new GridBagConstraints(gridx,gridy,1,1,0,0,GridBagConstraints.FIRST_LINE_START,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
	}
	public static GridBagConstraints getStaticNorthGBC(int gridx,int gridy)
	{
		return new GridBagConstraints(gridx,gridy,1,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
	}
	public static GridBagConstraints getStaticSouthGBC(int gridx,int gridy)
	{
		return new GridBagConstraints(gridx,gridy,1,1,0,0,GridBagConstraints.SOUTH,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
	}
	public static GridBagConstraints getStaticEastGBC(int gridx,int gridy)
	{
		return new GridBagConstraints(gridx,gridy,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
	}
	public static GridBagConstraints getStaticWestGBC(int gridx,int gridy)
	{
		return new GridBagConstraints(gridx,gridy,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
	}
	public static GridBagConstraints getLabelGBC(int gridy)
	{
		return getStaticWestGBC(0, gridy);
	}
	public static GridBagConstraints getFieldGBC(int gridy)
	{
		return getInlineGBC(1, gridy);
	}
}
