package com.jtaylor.util.gui.rwindow.progress;

abstract public class SimpleProgressRunner extends ProgressRunner
{
	private int myMax;
	private int myVal;
	public SimpleProgressRunner(int max)
	{
		super();
		myVal=0;
		myMax=max;
	}
	public void increment()
	{
		increment(1);
	}
	public void increment(int increment)
	{
		myVal+=increment;
		notifyListeners((int)(100*(myVal/(double)myMax)), null);
	}
	public int getMax()
	{
		return myMax;
	}
	public int getVal()
	{
		return myVal;
	}
}
