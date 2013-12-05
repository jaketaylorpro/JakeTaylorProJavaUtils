package com.jtaylor.util.datastructures.linqy;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 11/29/13
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinqyWhereNot<F> implements LinqyWhere<F>
	{
		LinqyWhere whereNot;
		public LinqyWhereNot(LinqyWhere<F> where)
		{
			whereNot=where;
		}

		public boolean where(F f)
		{
			return !whereNot.where(f);
		}
	}
