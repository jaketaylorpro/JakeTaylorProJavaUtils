package com.jtaylor.util.datastructures;

import java.io.Serializable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

public class Pair<A,B> implements Serializable, Entry<A, B>
{
    private static final long serialVersionUID = 1L;
	protected A a;
	protected B b;
	public Pair(A myA,B myB)
	{
		a=myA;
		b=myB;
	}
	public A getA()
	{
		return a;
	}
	public B getB()
	{
		return b;
	}
	public boolean equals(Object o)
   {
      return equals(o,false);
   }
	public boolean equals(Object o,boolean ignoreCase)
	{
		if(o instanceof Pair)
		{
         Object oA=((Pair)o).getA();
         boolean aMatch=match(oA,a,ignoreCase);
         Object oB=((Pair)o).getB();
         boolean bMatch=match(oB,b,ignoreCase);
         return aMatch&&bMatch;
      }
		else
		{
			return false;
		}
	}
   private static boolean match(Object o1,Object o2,boolean ignoreCase)
   {
      if(o1==null)
      {
         return o2==null;
      }
      else if(o2==null)
      {
         return false;
      }
      else if(ignoreCase&&o1 instanceof String&&o2 instanceof String)
      {
         return((String)o1).equalsIgnoreCase((String)o2);
      }
      else if(ignoreCase&&o1 instanceof Pair&&o2 instanceof Pair)
      {
         return ((Pair)o1).equals(o2,false);
      }
      else
      {
         return o1.equals(o2);
      }
   }

	public String toString()
	{
		return "<"+a.toString()+","+b.toString()+">";
	}
	public A getKey()
    {
	    return getA();
    }
	public B getValue()
    {
	    return getB();
    }
	public B setValue(B value)
    {
	    return b=value;
    }
	public static <A,B> List<A> getAs(List<Pair<A,B>> listOfPairs)
	{
		List<A> as=new Vector<A>();
		for(Pair<A,B> pair:listOfPairs)
		{
			as.add(pair.getA());
		}
		return as;
	}
	public static <A,B> List<B> getBs(List<Pair<A,B>> listOfPairs)
	{
		List<B> bs=new Vector<B>();
		for(Pair<A,B> pair:listOfPairs)
		{
			bs.add(pair.getB());
		}
		return bs;
	}
}
