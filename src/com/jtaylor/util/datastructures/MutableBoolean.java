package com.jtaylor.util.datastructures;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: Oct 18, 2010
 * Time: 10:46:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class MutableBoolean
{
	private boolean myValue;
	public MutableBoolean()
	{
		this(true);
	}
	public MutableBoolean(boolean value)
	{
		myValue=value;
	}
	public void not()
	{
		myValue=!myValue;
	}
	public void setValue(boolean value)
	{
		myValue=value;
	}
	public boolean getValue()
	{
		return myValue;
	}
}
