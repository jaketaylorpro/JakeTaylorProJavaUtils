package com.jtaylor.util.datastructures;


import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: Jan 15, 2010
 * Time: 12:47:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class SortedList <T> implements List<T>
{
	private List<T> internalList;
	private Comparator<T> myComparator;
	private boolean myAllowDuplicates;

    @Override
    public String toString()
    {
        String string="{\n";
        int count=0;
        for(T t:internalList)
        {
           string+=(count++)+"\t"+t+"\n";
        }
        string+="}";
        return string;
    }

    public SortedList(Comparator<T> comparator,boolean allowDuplicates)
	{
        this(comparator,allowDuplicates,10);
    }
    public SortedList(Comparator<T> comparator,boolean allowDuplicates,int size)
    {
		myComparator=comparator;
		myAllowDuplicates=allowDuplicates;
        internalList=new Vector<T>(size);
	}
	public SortedList(Comparator<T> comparator)
	{
		this(comparator,true);
	}
	public SortedList(Comparator<T> comparator,Collection<? extends T> collection)
	{
		this(comparator,true,collection.size());
		addAll(collection);
	}
	public SortedList(Comparator<T> comparator,boolean allowDuplicates,Collection<? extends T> collection)
	{
		this(comparator,allowDuplicates,collection.size());
		addAll(collection);
	}
	public int size()
	{
		return internalList.size();
	}

	public boolean isEmpty()
	{
		return internalList.isEmpty();
	}

	public boolean contains(Object o)
	{
		return internalList.contains(o);
	}

	public Iterator<T> iterator()
	{
		return internalList.iterator();
	}

	public Object[] toArray()
	{
		return internalList.toArray();
	}

	public <T> T[] toArray(T[] a)
	{
		return internalList.toArray(a);
	}

	public boolean add(T o)
	{
		for(int i=0;i<internalList.size();i++)
		{
			if(myComparator.compare(o,internalList.get(i))<=0)
			{
				if(internalList.get(i).equals(o))
				{
					return false;	
				}
				else
				{
					internalList.add(i,o);
					return true;
				}
			}
		}
		internalList.add(o);
		return true;
	}

	public boolean remove(Object o)
	{
		return internalList.remove(o);
	}

	public boolean containsAll(Collection<?> c)
	{
		return internalList.containsAll(c);
	}

	public boolean addAll(Collection<? extends T> c)
	{
		boolean modified=false;
		for(T t:c)
		{
			add(t);
			modified=true;
		}
		return modified;
	}

	public boolean addAll(int index, Collection<? extends T> c)
	{
		throw new AbstractMethodError("cannot specify index of sorted list");
	}

	public boolean removeAll(Collection<?> c)
	{
		return internalList.removeAll(c);
	}

	public boolean retainAll(Collection<?> c)
	{
		return internalList.retainAll(c);
	}

	public void clear()
	{
		internalList.clear();
	}

	public T get(int index)
	{
		return internalList.get(index);
	}

	public T set(int index, T element)
	{
		throw new AbstractMethodError("cannot specify index of sorted list");
	}

	public void add(int index, T element)
	{
		throw new AbstractMethodError("cannot specify index of sorted list");
	}

	public T remove(int index)
	{
		return internalList.remove(index);
	}

	public int indexOf(Object o)
	{
		return internalList.indexOf(o);
	}

	public int lastIndexOf(Object o)
	{
		return internalList.lastIndexOf(o);
	}

	public ListIterator<T> listIterator()
	{
		return internalList.listIterator();
	}

	public ListIterator<T> listIterator(int index)
	{
		return internalList.listIterator(index);
	}

	public List<T> subList(int fromIndex, int toIndex)
	{
		return internalList.subList(fromIndex,toIndex);
	}
}
