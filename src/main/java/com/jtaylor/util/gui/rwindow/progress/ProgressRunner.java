package com.jtaylor.util.gui.rwindow.progress;

import java.util.List;
import java.util.Vector;

public abstract class ProgressRunner implements Runnable
{
	private List<ProgressListener> listeners;
	private int currentProgress;
	private String currentText;
	public ProgressRunner()
	{
		listeners=new Vector<ProgressListener>();
	}
	public void addProgressListener(ProgressListener listener)
	{
		listeners.add(listener);
		listener.progress(currentProgress,currentText);//we bring it up to date
	}
	public void removeProgressListener(ProgressListener listener)
	{
		listeners.remove(listener);
	}
	public void notifyListeners(int progress,String text)
	{
		currentProgress=progress;
		currentText=text;
		for(ProgressListener listener:listeners)
		{
			listener.progress(progress,text);
		}
	}
	abstract public void run();
	abstract public void increment();
	public void done()
	{
		notifyListeners(100, "Done");
	}
	public void error()
	{
		notifyListeners(100, "Error");
	}
}
