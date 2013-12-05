package com.jtaylor.util.gui.rwindow.progress;

abstract public class IndeterminateProgressRunner extends ProgressRunner
{
	public IndeterminateProgressRunner()
	{
		super();
		notifyListeners(-1,null);
	}
	public void increment()
	{
		notifyListeners(100," ");
	}

}
