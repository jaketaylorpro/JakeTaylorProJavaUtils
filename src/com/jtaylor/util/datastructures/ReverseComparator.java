package com.jtaylor.util.datastructures;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 1/13/11
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReverseComparator<T> implements Comparator<T>
{
   private Comparator<T> myInnerComparator;
   public ReverseComparator(Comparator<T> comparator)
   {
      myInnerComparator=comparator;
   }

   public int compare(T o1, T o2)
   {
      return myInnerComparator.compare(o1,o2)*-1;
   }
}
