package com.jtaylor.util.datastructures;


import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 8/15/11
 * Time: 11:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class BinaryTree
{
   BinaryTreeNode myRoot;
   private void add(Comparable value)
   {
      if(myRoot==null)
      {
         myRoot=new BinaryTreeNode(value);
      }
      else
      {
         BinaryTreeNode node=myRoot;
         boolean done=false;
         while(!done)
         {
            int comp=value.compareTo(node.myValue);
            if(comp>0)
            {
               if(node.myRight==null)
               {
                  node.myRight=new BinaryTreeNode(value);
                  done=true;
               }
               else
               {
                  node=node.myRight;
               }
            }
            else// if(comp<0)
            {
               if(node.myLeft==null)
               {
                  node.myLeft=new BinaryTreeNode(value);
                  done=true;
               }
               else
               {
                  node=node.myLeft;
               }
            }
         }
      }
   }
   private static class BinaryTreeNode
   {
      Comparable myValue;
      BinaryTreeNode myLeft;
      BinaryTreeNode myRight;
      public BinaryTreeNode(Comparable value)
      {
         myValue=value;
      }

      private String dfsPrint()
      {
         String s=myValue.toString()+", ";
         if(myLeft!=null)
         {
            s+=myLeft.dfsPrint();
         }
         if(myRight!=null)
         {
            s+=myRight.dfsPrint();
         }
         return s;
      }
      private String bfsPrint()
      {
         String s=myValue.toString()+", ";
         if(myLeft!=null)
         {
            s+=myLeft.dfsPrint();
         }
         if(myRight!=null)
         {
            s+=myRight.dfsPrint();
         }
         return s;
      }
   }
   public static void main(String[] args)
   {
      BinaryTree tree=new BinaryTree();
      Vector<Integer> list=new Vector<Integer>();
      for(int i=0;i<100;i++)
      {
         int r=(int)(Math.random()*1000);
         tree.add(r);
         list.add(r);
      }
      System.out.println(tree.myRoot.dfsPrint());
   }
}
