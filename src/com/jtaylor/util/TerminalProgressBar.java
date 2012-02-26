package com.jtaylor.util;

public class TerminalProgressBar
{
	private int myMax;
	private int myValue;
	private String myTitle;
	private int myIndeterminateIndex;
	private boolean printedDone;
	private static final int SIZE=60;
	private static final char PROGRESS='=';
	private static final char INCOMPLETE=' ';
	private static char[] INDETERMINATE={'\\','-','/'};
	private static final int INDENT=40;
	public static void main(String[] args)
	{
		TerminalProgressBar progressBar=new TerminalProgressBar("Initializing", 300);
		progressBar.print();
		
		for(int i=0;i<300;i++)
		{
			try{Thread.sleep(50);}catch(Exception e){}
			progressBar.increment();
		}
	}
	public TerminalProgressBar(String title,int max)
	{
		myTitle=title+getRepeatedCharString(' ', INDENT-title.length());
		myMax=max;
		myValue=0;
		myIndeterminateIndex=0;
		printedDone=false;
	}
	public void increment()
	{
		myValue++;
		print();
	}
	public void working()
	{
		print();
	}
	public void done()
	{
		myValue=myMax;
		if(!printedDone)
		{
			print();
			printedDone=true;
		}
	}
	private String getRepeatedCharString(char c,int num)
	{
		String s="";
		for(int i=0;i<num;i++)
		{
			s+=c;
		}
		return s;
	}
	public void print()
	{
		double percent=(((double)myValue)/myMax);
		double num=percent*SIZE;
		if(Double.compare(percent, 1.0)<0)
		{
			System.out.print(myTitle+" ["+getString(num)+"]\r");
		}
		else
		{
			String spacer=getRepeatedCharString(PROGRESS, (SIZE-4)/2);
			System.out.println(myTitle+" ["+spacer+"DONE"+spacer+"]");
			printedDone=true;
		}
		
	}
	private char getIndeterminateChar()
    {
		if(myIndeterminateIndex<INDETERMINATE.length-1)
		{
			myIndeterminateIndex++;
		}
		else
		{
			myIndeterminateIndex=0;
		}
		return INDETERMINATE[myIndeterminateIndex];
    }
	private String getString(double n)
	{
		String string="";
		double part;
		double pieces=1.0/PROGRESS;
		for(int i=0;i<SIZE;i++)
		{
			if(i==SIZE/2)
			{
				string+=getIndeterminateChar();
			}
			else
			{
				if(i<n)
				{
					string+=PROGRESS;
				}
				else
				{
					string+=INCOMPLETE;
				}
			}
		}
		return string;
	}
}
