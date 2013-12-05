package com.jtaylor.util.datastructures;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 8/15/11
 * Time: 11:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class HashTable
{
   HashTableNode[] table;
   public HashTable(int size)
   {
      table=new HashTableNode[size];
   }
   public int hash(int value)
   {
      return value%table.length;
   }
   public void add(int value)
   {
      System.out.println("adding: "+value);
      int h=hash(value);
      if(table[h]==null)
      {
         table[h]=new HashTableNode(value);
      }
      else
      {
         boolean toAdd=true;
         HashTableNode node=table[h];
         if(node.myValue==value)
         {
            toAdd=false;
         }
         while (node.myNext!=null&&toAdd)
         {
            if(node.myNext.myValue==value)
            {
               toAdd=false;
            }
            else
            {
               node=node.myNext;
            }
         }
         if(toAdd)
         {
            node.myNext=new HashTableNode(value);
         }
      }
   }
   public boolean contains(int value)
   {
      int h=hash(value);
      if(table[h]==null)
      {
         return false;
      }
      else
      {
         HashTableNode node=table[h];
         if(node.myValue==value)
         {
            return true;
         }
         while (node.myNext!=null)
         {
            if(node.myNext.myValue==value)
            {
               return true;
            }
            else
            {
               node=node.myNext;
            }
         }
         return false;
      }
   }
   private static class HashTableNode
   {
      int myValue;
      HashTableNode myNext;
      public HashTableNode(int value)
      {
         myValue=value;
      }
      public void setNext(HashTableNode next)
      {
         myNext=next;
      }
      public String toString()
      {
         return myValue+"->"+myNext;
      }
   }
   public static void main(String[] args)
   {
      HashTable table=new HashTable(100);
      Vector<Integer> list=new Vector<Integer>();
      for(int i=0;i<100;i++)
      {
         int r=(int)(Math.random()*1000);
         table.add(r);
         list.add(r);

      }
      System.out.println(new EasyVector(table.table));
      int missed=0;
      for(int i=0;i<list.size();i++)
      {
         if(!table.contains(list.get(i)))
         {
            System.out.println("does not contain: "+list.get(i));
            missed++;
         }
      }
      System.out.println("done, missed: "+missed);
   }
}
