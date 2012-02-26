package com.jtaylor.util.datastructures;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: Mar 18, 2010
 * Time: 9:05:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Triplet<A,B,C> implements Serializable
{
	protected A a;
	protected B b;
	protected C c;
	public Triplet(A aa,B ab,C ac)
	{
		a=aa;
		b=ab;
		c=ac;
	}
	public A getA()
	{
		return a;
	}
	public B getB()
	{
		return b;
	}
	public C getC()
	{
		return c;
	}
}
