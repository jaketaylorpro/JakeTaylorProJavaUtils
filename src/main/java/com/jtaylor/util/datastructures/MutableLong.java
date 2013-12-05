package com.jtaylor.util.datastructures;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 12/14/11
 * Time: 6:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class MutableLong
{
   private long myValue;
   public MutableLong(long value)
   {
      myValue=value;
   }
   public long getValue()
   {
      return myValue;
   }
   public void setValue(long value)
   {
      myValue=value;
   }
   public void add(long value)
   {
      myValue+=value;
   }
   @Override
   public String toString()
   {
      return ""+myValue;
   }
}
