package com.jtaylor.util.datastructures.linqy;

import sun.security.ssl.SSLContextImpl;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 11/26/13
 * Time: 7:05 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 * @param <F> From - the type you are selecting from
 */
public class Linqy<F>
{
	private static final LinqyWhere DEFAULT_WHERE=new LinqyWhere() {public boolean where(Object o){return true;}};
	private static final Collection DEFAULT_FROM=new LinkedList();

	private final LinqySelector<Object,F> DEFAULT_OBJECT_SELECTOR=new LinqySelector<Object,F>(){public Object select(F f){return f;}};
	private final LinqySelector<String,F> DEFAULT_STRING_SELECTOR=new LinqySelector<String,F>(){public String select(F f){return f.toString();}};
	private final LinqySelector<F,F> DEFAULT_SELECTOR=new LinqySelector<F,F>(){public F select(F o){return o;}};
	private LinqyWhere <F> where;
	private Collection<F> from;

	/**
	 * default constructor
	 */
	public Linqy()
	{
		this.where=DEFAULT_WHERE;
		this.from=DEFAULT_FROM;
	}
	/**
	 * copy constructor
	 */
	public Linqy(Linqy<F> s)
	{
		this();
		this.from=s.from;
		this.where=s.where;
	}
	public static <G> Linqy<G> from(Collection<G> list)
	{
		Linqy<G> lg=new Linqy<G>();
		lg.from=list;
		return lg;
	}
	public Linqy<F> where(LinqyWhere<F> where)
	{
		this.where=where;
		return this;
	}
	public <S> Iterable<S> select(final LinqySelector<S,F> selector)
	{
		return new Iterable<S>()
		{
			public Iterator<S> iterator()
			{
				return new Iterator<S>()
				{
					Iterator<F> it=from.iterator();
					LinkedList<S> buff=new LinkedList<S>();
					public boolean hasNext()
					{
						if(!buff.isEmpty())
						{
							return true;
						}
						else if (!it.hasNext())
						{
							return false;
						}
						else
						{
							while(it.hasNext())//iterate through the from list until we find a result that passes the where clause or the iterator is exhausted
							{
								F f=it.next();
								if(where.where(f))
								{
									buff.add(selector.select(f));//add it to the buff so we don't lose it
									return true;
								}
							}
							return false;//the iterator is exhausted and we didn't find anything that passes the where clause
						}
					}

					public S next()
					{
						if(buff.isEmpty())
						{
							hasNext();
						}
						return buff.poll();
					}
					public void remove()
					{
						throw new UnsupportedOperationException("Remove not supported by Linqy Iterator");
					}
				};
			}
		};
	}
	public Iterable<F> select()
	{
		return select(DEFAULT_SELECTOR);
	}
	public Object[] selectToObjectArray(LinqySelector<Object, F> selectColumnName)
	{
		int count=count();
		Iterator<Object> iterator=select(selectColumnName).iterator();
		Object[] newArray=new Object[count];
		for(int i=0;i<count;i++)
		{
			newArray[i]=iterator.next();
		}
		return newArray;
	}
	public Object[] selectToObjectArray()
	{
		return selectToObjectArray(DEFAULT_OBJECT_SELECTOR);
	}
	public String[] selectToStringArray(LinqySelector<String, F> selectColumnName)
	{
		int count=count();
		Iterator<String> iterator=select(selectColumnName).iterator();
		String[] newArray=new String[count];
		for(int i=0;i<count;i++)
		{
			newArray[i]=iterator.next();	
		}
		return newArray;
	}
	public String[] selectToStringArray()
	{
		return selectToStringArray(DEFAULT_STRING_SELECTOR);
	}
	public int count()
	{
		int i=0;
		for(Boolean b:select(new LinqySelector<Boolean, F>()
		{
			public Boolean select(F f)
			{
				return true;
			}
		}))
		{
			i++;
		}
		return i;
	}
	public void update(LinqyUpdater<F> updater)
	{
		for(F f:from)
		{
			if(where.where(f))
			{
				updater.update(f);
			}
		}
	}
	public void delete()
	{
		LinkedList<F> toDelete=new LinkedList<F>();
		for(F f:from)
		{
			if(where.where(f))
			{
				toDelete.add(f);
			}
		}
		for(F f:toDelete)
		{
			from.remove(f);
		}
	}

}