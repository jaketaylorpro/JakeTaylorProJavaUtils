package com.jtaylor.util.datastructures;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: Apr 2, 2010
 * Time: 2:59:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class MutableInteger
{
	private int myValue;
	public MutableInteger()
	{
		this(0);
	}
	public MutableInteger(int value)
	{
		myValue=value;
	}
	public void increment()
	{
		myValue++;
	}
	public void set(int value)
	{
		myValue=value;
	}
	public int getValue()
	{
		return myValue;
	}
   @Override
   public String toString()
   {
      return ""+myValue;
   }
}
