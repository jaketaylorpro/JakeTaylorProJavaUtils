package com.jtaylor.util;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 9/6/11
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class JobReport <E>
{
   private TreeMap<Enum,Integer> myCountMap;
   private Date myStartTime;
   private Date myEndTime;
   private String myName;
   public <E extends Enum> JobReport(String name)
   {
      myName=name;
      myCountMap=new TreeMap<Enum, Integer> (new Comparator<Enum>()
      {
         public int compare(Enum o1, Enum o2)
         {
            return new Integer(o1.ordinal()).compareTo(o2.ordinal());
         }
      });
   }
   public void setName(String name)
   {
      myName=name;
   }
   public String getName()
   {
      return myName;
   }
   public Date getStartTime()
   {
      return myStartTime;
   }
   public Date getEndTime()
   {
      return myEndTime;
   }
   public void increment(E e)
   {
      if(myCountMap.containsKey(e))
      {
         myCountMap.put((Enum)e,myCountMap.get(e)+1);
      }
      else
      {
         myCountMap.put((Enum)e,1);
      }
   }
   public void setBegin()
   {
      myStartTime=new Date();
   }
   public void setDone()
   {
      myEndTime=new Date();
   }
   public String toString()
   {
      return getReportString();
   }
   public String getReportString()
   {
      String string="";
      if(myEndTime==null)
      {
         string+=myName+" Not Done, Elapsed Time: "+DateOperations.formatTime(new Date().getTime()-myStartTime.getTime())+"\n";
      }
      else
      {
         string+=myName+" Done, Time: "+DateOperations.formatTime(myEndTime.getTime()-myStartTime.getTime())+"\n";
      }
      if(myCountMap.isEmpty())
      {
         string +="\tNo Changes\n";
      }
      else
      {
         for(Enum e:myCountMap.keySet())
         {
            string+='\t'+e.toString()+": "+myCountMap.get(e)+'\n';
         }
      }
      string=string.substring(0,string.length()-1);//remove the final newline
      return string;
   }
}
