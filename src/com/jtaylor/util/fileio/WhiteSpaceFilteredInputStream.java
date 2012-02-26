package com.jtaylor.util.fileio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/*
 * this class doenst really work with window's newline chars
 */
public class WhiteSpaceFilteredInputStream extends FilterInputStream
{
	private boolean inValue;
	private boolean inTag;
	private String buffer;
	public static void main(String[] args)
	{
		System.out.println("starting ("+((int)' ')+","+((int)'\t')+","+((int)'\n')+")");
		String read="";
		try
        {
//			BufferedReader in=new BufferedReader(new InputStreamReader(new WhiteSpaceFilteredInputStream(new FileInputStream(new File("/Users/jtaylor/Desktop/test.txt")))));
	        BufferedReader in=new BufferedReader(new InputStreamReader(new WhiteSpaceFilteredInputStream(new FileInputStream(new File("/Users/jtaylor/Downloads/config.xml")))));
	        while(in.ready())
	        {
	        	read+=in.readLine();
	        }
	        System.out.println("read: "+read);
        }
        catch (Exception e)
        {
	        e.printStackTrace();
        }
	}
	public WhiteSpaceFilteredInputStream(InputStream in)
    {
	    super(in);
	    inValue=false;
	    inTag=false;
	    buffer="";
    }
	@Override
	public int read() throws IOException
	{
		int b;
        b = super.read();
        System.out.println("debug char: "+b);
        buffer+=(char)b;
        if(buffer.endsWith("<"))
        {
        	inTag=true;
        }
        else if(buffer.endsWith(">"))
        {
        	inTag=false;
        }
        if(buffer.endsWith("<key>"))
        {
        	inValue=true;
        }
        else if(buffer.endsWith("</key>"))
        {
        	inValue=false;
        }
		if(inValue||inTag)
		{
			return b;
		}
		else
		{
			if(b==' '||b=='\t'||b=='\n')
			{
				System.out.println("skipped a white");
				return read();
			}
			else
			{
				return b;
			}
		}
	}
	@Override
	public int read(byte[] b,int off,int len) throws IOException
	{
		int count=0;
		boolean ioe=false;
		for(int i=off;i<off+len&&i<b.length;i++)
		{
			try
			{
				b[i]=(byte) read();
			}
			catch(Exception e)
			{
				b[i]=-1;
			}
			if(b[i]!=-1)
			{
				ioe=false;
				count++;
			}
			else
			{
				ioe=true;
			}
		}
		if(ioe&&count==0)
		{
			return -1;
		}
		else
		{
			return count;
		}
	}
}
