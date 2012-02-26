package com.jtaylor.util.datastructures;


import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: Dec 7, 2010
 * Time: 7:01:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class EasyVector <T> extends Vector<T>
{
	public EasyVector()
	{
		super();
	}
	public EasyVector(Collection<T> collection)
	{
		super(collection);
	}
   public EasyVector(Enumeration<T> enumeration)
   {
      super();
      while(enumeration.hasMoreElements())
      {
         add(enumeration.nextElement());
      }
   }
	public EasyVector(T ... ts)
	{
		super(ts.length);
		for(T t:ts)
		{
			add(t);
		}
	}
	public EasyVector(int initialCapacity)
	{
		super(initialCapacity);
	}
	public EasyVector(int initialCapcity,int capacityIncrement)
	{
		super(initialCapcity,capacityIncrement);
	}
	public String toString(boolean numbered)
   {
      String string = "";
		int n=0;
      for (T t : this)
      {
			if(numbered)
         {
            string+=n+":\t";
         }
         string+=t+"\n";
         n++;
      }
      return string;
   }
	public String toString()
	{
      return toString(false);
	}
}
