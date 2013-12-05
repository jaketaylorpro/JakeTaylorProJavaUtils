package com.jtaylor.util;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 4/27/11
 * Time: 6:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class CaseAgnosticAlphabeticStringComparator implements Comparator<String>
{
   public int compare(String o1, String o2)
   {
      return o1.toLowerCase().compareTo(o2.toLowerCase());
   }
}
