package com.jtaylor.util.gui.rwindow.progress;

abstract public class WaitCountWaitProgressRunner extends ProgressRunner
{
	private int myMax;
	private int myVal;
	public WaitCountWaitProgressRunner(int max)
	{
		super();
		myVal=0;
		myMax=max;
	}
	public void wait(String message)
	{
		notifyListeners(-1,message);
	}
	public void stopWaiting()
	{
		simpleNotify();
	}
	public void increment()
	{
		increment(1);
	}
	public void increment(int increment)
	{
		myVal+=increment;
		simpleNotify();
	}
	public int getMax()
	{
		return myMax;
	}
	public int getVal()
	{
		return myVal;
	}
	public void simpleNotify()
	{
		notifyListeners((int)(100*(myVal/(double)myMax)), null);		
	}
	public static void main(String[] args)
	{
		new RProgressDialog("waitCountWaitTest",new WaitCountWaitProgressRunner(5)
		{
			@Override
			public void run()
			{
				increment();
				try{Thread.sleep(300);}catch(InterruptedException e){e.printStackTrace();}
				increment();
				try{Thread.sleep(300);}catch(InterruptedException e){e.printStackTrace();}
				increment();
				try{Thread.sleep(300);}catch(InterruptedException e){e.printStackTrace();}
				increment();
				try{Thread.sleep(300);}catch(InterruptedException e){e.printStackTrace();}
				wait("waiting");
				try{Thread.sleep(3000);}catch(InterruptedException e){e.printStackTrace();}
				stopWaiting();
				try{Thread.sleep(300);}catch(InterruptedException e){e.printStackTrace();}
				increment();
				System.out.println("done");
				done();
			}
		}).start();
		System.out.println("blablabla");
	}
}
