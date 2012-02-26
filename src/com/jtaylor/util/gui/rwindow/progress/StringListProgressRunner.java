package com.jtaylor.util.gui.rwindow.progress;

import java.util.List;

abstract public class StringListProgressRunner<T> extends ProgressRunner
{
	protected int myIndex;
	protected List<T> myValues;
	public StringListProgressRunner (List<T> values)
	{
		super();
		myIndex=0;
		myValues=values;
		notifyListeners(0, (myValues==null||myIndex>=myValues.size()?"Done":myValues.get(myIndex).toString()));
	}
	public void increment()
	{
		if(myIndex<myValues.size()-1)
		{
			myIndex++;
			notifyListeners((int)(100*(myIndex/((double)myValues.size()-1))), myValues.get(myIndex).toString());
		}
		else
		{
			notifyListeners(100, myValues.get(myValues.size()-1).toString());
		}
	}
	public void setValue(Object val)
	{
		myIndex=myValues.indexOf(val);
		if(myIndex<0)
		{
			myIndex=0;
		}
		notifyListeners((int)(100*(myIndex/((double)myValues.size()-1))), myValues.get(myIndex).toString());
	}
}
