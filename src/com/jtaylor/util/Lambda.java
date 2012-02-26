package com.jtaylor.util;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 5/26/11
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Lambda
{
   private String myName;
   public Lambda(String name)
   {
      myName=name;
   }
   public abstract Object lambda(Object o) throws LambdaException;
   public void throwException(String message) throws LambdaException
   {
      throw new LambdaException(myName,message);
   }
   public class LambdaException extends Exception
   {
      public LambdaException(String name,String message)
      {
         super(name+": "+message);
      }
   }
}
