package com.jtaylor.util;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 10/19/11
 * Time: 1:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class MemoryPrintStream extends PrintStream
{
   private ByteArrayOutputStream myByteArrayOutputStream;
   private PrintStream myCopyStream;
   public static MemoryPrintStream getMemoryPrintStream()
   {
      return getMemoryPrintStream(null);
   }
   public static MemoryPrintStream getMemoryPrintStream(PrintStream copyStream)
   {
      ByteArrayOutputStream bout=new ByteArrayOutputStream();
      MemoryPrintStream mps= new MemoryPrintStream(bout);
      if(copyStream!=null)
      {
         mps.setCopyStream(copyStream);
      }
      return mps;
   }

   private void setCopyStream(PrintStream copyStream)
   {
      myCopyStream=copyStream;//right now it just copyies calls to print
   }

   private MemoryPrintStream(ByteArrayOutputStream bout)
   {
      super(bout,true);
      myByteArrayOutputStream=bout;
   }

   @Override
   public void close()
   {
      if(myCopyStream!=null)myCopyStream.close();
      super.close();    //To change body of overridden methods use File | Settings | File Templates.
   }
   @Override
   public void println()
   {
      if(myCopyStream!=null)myCopyStream.println();
      super.println();    //To change body of overridden methods use File | Settings | File Templates.
   }

   @Override
   public void println(String s)
   {
      if(myCopyStream!=null)myCopyStream.println(s);
      super.println(s);    //To change body of overridden methods use File | Settings | File Templates.
   }

   public String getMemory()
   {
      String memory=myByteArrayOutputStream.toString();
      myByteArrayOutputStream.reset();
      return memory;
   }
}
