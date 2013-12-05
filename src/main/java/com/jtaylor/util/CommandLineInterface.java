package com.jtaylor.util;


import com.jtaylor.util.datastructures.StructOperations;

import java.lang.String;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/22/12
 * Time: 12:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class CommandLineInterface
{
   public abstract class Command
   {
      public abstract String getCommand();
      public abstract List<String> getMandatoryArgs();
      public abstract List<String> getOptionalArgs();
      public abstract List<String> getFlags();
      public abstract void execute(Map<String,String> mandatoryArgs,Map<String,String> optionalArgs,List<String> flags);
      public String toString()
      {
         return getCommand();
      }
   }
   public class CommandLineInterfaceException extends Exception
   {
      public CommandLineInterfaceException(String message)
      {
         super(message);
      }
      public CommandLineInterfaceException(String message,Throwable cause)
      {
         super(message,cause);
      }
   }
   private Set<Command> myCommands=new HashSet<Command>();
   private Command myCommand;
   private Map<String,String> myMandatoryArgs=new HashMap<String, String>();
   private Map<String,String> myOptionalArgs=new HashMap<String, String>();
   private List<String> myFlags =new Vector<String>();
   public CommandLineInterface(Collection<Command> commands,List<String> runtimeArgs)
   {
      myCommands.addAll(commands);
      try
      {
         interpretCommand(runtimeArgs);
      }
      catch (CommandLineInterfaceException e)
      {
         System.out.println(e.getMessage());
      }
   }
   private void interpretCommand(List<String> args) throws CommandLineInterfaceException
   {
      if(args.isEmpty())
      {
         throw new CommandLineInterfaceException("No command specified, available commands are: "+myCommands);
      }
      boolean commandFound=false;
      for(Command command:myCommands)
      {
         if(command.getCommand().equalsIgnoreCase(args.get(0)))
         {
            commandFound=true;
            myCommand=command;
            break;
         }
      }
      if(!commandFound)
      {
         throw new CommandLineInterfaceException("No such command: '"+args.get(0)+"', available commands are: "+myCommands);
      }
      else
      {
         args.remove(0);//remove the command
         for(int i=0;i<args.size();i++)
         {
            String arg=args.get(i);
            if(arg.length()>1&&arg.startsWith("--"))//than it's a flag
            {
               boolean flagFound=false;
               for(String flag:myCommand.getFlags())
               {
                  if(flag.equalsIgnoreCase(arg.substring(2)))
                  {
                     flagFound=true;
                     myFlags.add(flag);
                     args.remove(i);//remove flags because they can go anywhere
                     i--;//so that the loop will not skip the next arg (which will be at the same index)
                  }
               }
               if(!flagFound)
               {
                  throw new CommandLineInterfaceException("Unknown flag: '"+arg.substring(2)+"', available flags are: "+myCommand.getFlags());
               }
            }
         }

         for(String mandatoryArg:myCommand.getMandatoryArgs())
         {
            boolean argFound=false;
            for(int i=0;i<args.size();i++)
            {
               String arg=args.get(i);
               if(arg.length()>0&&arg.substring(1).equalsIgnoreCase(mandatoryArg))
               {
                  if(i==args.size()-1||args.get(i+1).startsWith("-"))
                  {
                     throw new CommandLineInterfaceException("No value for mandatory argument: '"+mandatoryArg+"'");
                  }
                  myMandatoryArgs.put(mandatoryArg,args.get(i+1));
                  args.remove(i);
                  args.remove(i);
                  i-=2;
                  break;//TODO what if it occurs twice
               }
            }
            if(!argFound)
            {
               throw new CommandLineInterfaceException("Mandatory argument: '"+mandatoryArg+"', not found");
            }
         }
         for(int i=0;i<args.size();i++)
         {

         }
      }
   }
}
