package com.jtaylor.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
* Created by IntelliJ IDEA.
* User: jtaylor
* Date: 4/29/11
* Time: 9:28 PM
* To change this template use File | Settings | File Templates.
*/
class StreamReaderThread extends Thread
{
   private InputStream myStream;
   private String myOutput;
   private Logger myLogger;
   private Level myLevel;
   public StreamReaderThread(InputStream stream, Logger logger, Level level)
   {
      myStream=stream;
      myOutput="";
      myLogger=logger;
      myLevel=level;
   }
   @Override
   public void run()
   {
      System.out.println("running stream reader thread");
      int read = 0;
      int total = 0;
      String string;
      byte[] buffer = new byte[1024];
      try
      {
         BufferedInputStream bufferedStream = new BufferedInputStream(myStream);
         while (read > -1)
         {
            System.out.println("about to read");
            read = bufferedStream.read(buffer);
            if (read > -1)
            {
               total += read;
               string=new String(buffer,0,read);
               myOutput+=string;
               myLogger.log(myLevel,string);

            }
            System.out.println("read: "+read);
         }
         ProcessReader.log.debug("wrote: " + total + " bytes");
         bufferedStream.close();
      }
      catch (Exception e)
      {
         ProcessReader.log.error("there was an error after reading: " + total + " bytes");
      }
   }
   public String getOutput()
   {
      return myOutput;
   }
}
