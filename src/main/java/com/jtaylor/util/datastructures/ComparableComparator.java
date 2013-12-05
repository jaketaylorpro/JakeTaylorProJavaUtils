package com.jtaylor.util.datastructures;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: Jan 24, 2010
 * Time: 1:04:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class ComparableComparator<E extends Comparable> implements Comparator<E>
{
	public int compare(E o1, E o2)
    {
        return o1.compareTo(o2);
    }
}
