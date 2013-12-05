package com.jtaylor.util.datastructures.linqy;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 11/29/13
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinqyWhereOr<F> implements LinqyWhere<F>
	{
		LinqyWhere<F>[] whereOr;
		public LinqyWhereOr(LinqyWhere<F> ... where)
		{
			whereOr=where;
		}

		public boolean where(final F f)
		{
			return new Linqy<LinqyWhere<F>>().from(Arrays.asList(whereOr)).where(new LinqyWhere<LinqyWhere<F>>()
			{
				public boolean where(LinqyWhere<F> fLinqyWhere)
				{
					return fLinqyWhere.where(f);
				}
			}).count()>0;
		}
	}
