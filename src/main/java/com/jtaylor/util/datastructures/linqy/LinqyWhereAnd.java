package com.jtaylor.util.datastructures.linqy;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 11/29/13
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinqyWhereAnd<F> implements LinqyWhere<F>
	{
		LinqyWhere<F>[] whereAnd;
		public LinqyWhereAnd(LinqyWhere<F> ... where)
		{
			whereAnd =where;
		}

		public boolean where(final F f)
		{
			return new Linqy<LinqyWhere<F>>().from(Arrays.asList(whereAnd)).where(new LinqyWhere<LinqyWhere<F>>()
			{
				public boolean where(LinqyWhere<F> fLinqyWhere)
				{
					return fLinqyWhere.where(f);
				}
			}).count()>0;
		}
	}