package com.jtaylor.util.datastructures;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 8/15/11
 * Time: 8:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuickSort
{
   public static void main(String[] args)
   {
      Vector v=new Vector<Integer>(1000);
      for(int i=0;i<1000;i++)
      {
         int r=(int)(Math.random()*1000);
//         if(!v.contains(r))
//         {
            v.add(r);
//         }
      }
      System.out.println(v);
      sort(v,0,v.size());
      System.out.println("done: "+v);
   }
   public static void sort(Vector<Comparable> v,int start,int end)
   {

      if(end-start<2)
      {
         System.out.println("nosort: "+start+"-"+end);
      }
      else if(end-start==2)
      {
         System.out.println("simple 2 sort: "+start+"-"+end);
         if(v.get(start).compareTo(v.get(start+1))>0)
         {
            swap(v,start,start+1);
         }
      }
      else if(end-start==3)
      {
         System.out.println("simple 3 sort: "+start+"-"+end);
         if(v.get(start).compareTo(v.get(start+1))>0)
         {
            swap(v,start,start+1);
         }
         if(v.get(start+1).compareTo(v.get(start+2))>0)
         {
            swap(v,start+1,start+2);
         }
         if(v.get(start).compareTo(v.get(start+1))>0)
         {
            swap(v,start,start+1);
         }
      }
      else
      {
         System.out.print("sort "+start+"-"+end+": {");
         for(int i=start;i<end;i++)
         {
            System.out.print(v.get(i)+", ");
         }
         System.out.println("}");
         int pi=end-1;
         Comparable pivot=v.get(pi);
//         System.out.println("pivot: "+pivot);
         int i=start;
         while(i<pi)
         {
            Comparable c1=v.get(i);
            int comp=c1.compareTo(pivot);
            if(comp>=0)//i greater pivot, and on the left of it
            {
               swap(v,i,pi);
               swap(v,pi-1,i);
//               System.out.println(i+","+pi+" swapped "+v);
               pi--;
            }
            else
            {
               i++;
            }
         }
         if(pi==start)
         {
            sort(v,start+1,end);
         }
         else if(pi==end)
         {
            sort(v,start,end-1);
         }
         else
         {
            sort(v,start,pi);
            sort(v,pi,end);
         }
         System.out.println("done: {");
         for(int j=start;j<end;j++)
         {
            System.out.print(v.get(j)+", ");
         }
         System.out.println("}");
      }
   }
   private static void swap(Vector<Comparable> v,int i1,int i2)
   {
      Comparable c1=v.get(i1);
      Comparable c2=v.set(i2,c1);
      v.set(i1,c2);
   }
   private static Pair<Comparable,Integer> getPivot(Vector<Comparable> v)
   {
      Comparable first=v.get(0);
      Comparable middle=v.get(v.size()/2);
      Comparable last=v.get(v.size()-1);
      if(first.compareTo(last)>0)
      {
         if(middle.compareTo(last)>0)
         {
            if(first.compareTo(middle)>0)
            {
               return new Pair<Comparable,Integer>(middle,v.size()/2);
            }
            else
            {
               return new Pair<Comparable,Integer>(first,0);
            }
         }
         else
         {
            return new Pair<Comparable,Integer>(last,v.size()-1);
         }
      }
      else
      {
         if(middle.compareTo(last)>0)
         {
            return new Pair<Comparable,Integer>(middle,v.size()/2);
         }
         else
         {
            if(first.compareTo(middle)>0)
            {
               return new Pair<Comparable,Integer>(first,0);
            }
            else
            {
               return new Pair<Comparable,Integer>(last,v.size()-1);
            }
         }
      }
   }
}
