package com.jtaylor.util;

import com.jtaylor.util.datastructures.EasyVector;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 4/16/11
 * Time: 12:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExecQueue extends Thread
{
   private List<String[]> myCLIs;
   private int myThreadCount;
   private List<Process> myProcesses;
   private List<ProcessReader> myProcessReaders;
   private int myIndex;
   private Logger log;
   public ExecQueue(List<String[]> clis,int threadCount)
   {
      myCLIs=clis;
      myThreadCount=threadCount;
      myProcesses=new Vector<Process>();
      myProcessReaders=new Vector<ProcessReader>();
      myIndex=0;
      log=Logger.getLogger(ExecQueue.class);
   }
   public void run()
   {
      boolean allDone=false;
      Process p;
      while(!allDone)
      {
         while(myProcesses.size()<myThreadCount&&myIndex<myCLIs.size())
         {
            try
            {
               p=Runtime.getRuntime().exec(myCLIs.get(myIndex));
               myProcessReaders.add(new ProcessReader(p,log));
               myProcesses.add(p);
            }
            catch(Exception e)
            {
               log.error("there was an error starting cli: "+myIndex+": "+new EasyVector(myCLIs.get(0)),e);
            }
            myIndex++;
         }
         if(myProcesses.size()==0)
         {
            allDone=true;
         }
         for(int i=myProcesses.size()-1;i>=0;i--)
         {
            p=myProcesses.get(i);
            try
            {
               p.exitValue();
               myProcesses.remove(i);
//               ProcessReader reader=myProcessesReaders.remove(i);
//               log.debug(reader.);
            }
            catch(Exception e){}
         }
         if(!allDone)
         {
            try{Thread.sleep(1000);}catch (Exception e){};
         }
      }
   }
}
