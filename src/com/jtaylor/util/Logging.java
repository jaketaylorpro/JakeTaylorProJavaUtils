package com.jtaylor.util;

import com.jtaylor.util.datastructures.StructOperations;
import com.jtaylor.util.gui.rwindow.GBCHelper;
import com.jtaylor.util.gui.rwindow.RBar;
import com.jtaylor.util.gui.rwindow.RDialog;
import org.apache.log4j.*;
import org.apache.log4j.lf5.DefaultLF5Configurator;
import org.apache.log4j.lf5.LF5Appender;
import org.apache.log4j.spi.RootLogger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;

public class Logging
{
	public static final String FAST_PATTERN="%p %d %c: %m [THREAD:%t]%n";
	public static final String PATTERN="%p %d %c,%M: %m [THREAD:%t]%n";
	public static final String BASIC_PATTERN="%d: %m%n";
	public static final String SIMPLE_PATTERN="%p - %m%n";
	public static Logger createServerLogger(Class clazz)
	{
		return createLogger(clazz,SystemOperations.SYSTEM_LOG_FOLDER,true,false);
	}

	public static final String CONSOLE_APPENDER_NAME="-console";
	public static final String FILE_APPENDER_NAME="-file";
	public static final String LF5_APPENDER_NAME="-lf5";
	public static Logger createLogger(Class clazz,String folderPath,boolean fileAppend,boolean consoleAppend)
   {
      return createLogger(clazz,folderPath,fileAppend,consoleAppend,false);
   }
	public static Logger createLogger(Class clazz,String folderPath,boolean fileAppend,boolean consoleAppend,boolean monitorAppend)
	{
      String loggerName=clazz.getCanonicalName();
      Logger log=Logger.getLogger(loggerName);
      try
      {
         Enumeration<Appender> appenders=log.getAllAppenders();
         Appender appender;
         String fileAppenderName=loggerName+FILE_APPENDER_NAME;
         String consoleAppenderName=loggerName+CONSOLE_APPENDER_NAME;
         String monitorAppenderName=loggerName+LF5_APPENDER_NAME;
         boolean fileAppenderNameFound=false;
         boolean consoleAppenderNameFound=false;
         boolean monitorAppenderNameFound=false;
         while(appenders.hasMoreElements())
         {
            appender=appenders.nextElement();
            if(appender.getName().equals(fileAppenderName))
            {
               fileAppenderNameFound=true;
            }
            else if(appender.getName().equals(consoleAppenderName))
            {
               consoleAppenderNameFound=true;
            }
            else if(appender.getName().equals(monitorAppenderName))
            {
               monitorAppenderNameFound=true;
            }
         }
         if(fileAppend&&!fileAppenderNameFound)
         {
            try
            {
               new File(folderPath).mkdirs();
            }
            catch(Exception e)
            {
               System.err.println("error creating log dir");
               e.printStackTrace();
            }
            RollingFileAppender fileAppender=new RollingFileAppender(new PatternLayout(PATTERN),
               folderPath+File.separator+ getThirdName(clazz)+".log");
            fileAppender.setName(fileAppenderName);
            fileAppender.setMaxFileSize("10MB");
            fileAppender.setMaxBackupIndex(0);
            log.addAppender(fileAppender);
         }
         if(consoleAppend&&!consoleAppenderNameFound)
         {
            ConsoleAppender consoleAppender=new ConsoleAppender(new PatternLayout(PATTERN));
            consoleAppender.setName(consoleAppenderName);
            log.addAppender(consoleAppender);

         }
         if(monitorAppend&&!monitorAppenderNameFound)
         {
            DefaultLF5Configurator.configure();
            LF5Appender lf5Appender=new LF5Appender();
            lf5Appender.setName(monitorAppenderName);
            lf5Appender.getLogBrokerMonitor().show();
            log.addAppender(lf5Appender);
         }
         log.setLevel(Level.ALL);
         if(fileAppenderNameFound)
         {
               System.out.println("fileappender name already found");
         }
         return log;
      }
      catch(Throwable t)
      {
         System.err.println("there was ane error getting logger: "+clazz+" at path: "+folderPath);
         t.printStackTrace();
      }
      return log;
	}
   private static String getThirdName(Class clazz)
   {
      String name=clazz.getCanonicalName();
      if(StructOperations.countOccurances(name,".")>2)
      {
         String subName=name.substring(name.indexOf(".")+1);
         //System.out.println("subname: "+subName);
         String subSubName=subName.substring(subName.indexOf(".")+1);
         //System.out.println("subsubname: "+subSubName);
         return subSubName.substring(0,subSubName.indexOf("."));
      }
      else
      {
         return name;
      }
   }

	public static void notifyUserPrompt(String message)
   {
      _notifyUser(Level.INFO,message,null,true,true);
   }
	public static void notifyUser(String message)
	{
		_notifyUser(Level.INFO,message,null,true);
	}
	public static void notifyUserPrompt(final Throwable e)
   {
      _notifyUser(Level.ERROR,e.getClass().getName()+": "+e.getMessage(),e.getStackTrace(),true,true);
   }
	public static void notifyUser(final Throwable e)
	{
		_notifyUser(Level.ERROR,e.getClass().getName()+": "+e.getMessage(),e.getStackTrace(),true);
	}
	public static void notifyUser(String message,final Throwable e)
	{
		_notifyUser(Level.ERROR,message+"; "+e.getClass().getName()+": "+e.getMessage(),e.getStackTrace(),true);
	}
	public static void notifyUser(final Throwable e,Logger logger)
	{
		logger.error(e);
		_notifyUser(Level.ERROR,e.getClass().getName()+": "+e.getMessage(),e.getStackTrace(),true);
	}
	public static void notifyUser(String message,final Throwable e,Logger logger)
	{
		logger.error(e);
		_notifyUser(Level.ERROR,message+"; "+e.getClass().getName()+": "+e.getMessage(),e.getStackTrace(),true);
	}
	public static void notifyUser(final Level level,final String message,Logger logger)
	{
		if(level.equals(Level.TRACE))
		{
			logger.trace(message);
		}
		if(level.equals(Level.DEBUG))
		{
			logger.debug(message);
		}
		if(level.equals(Level.INFO))
		{
			logger.info(message);
		}
		if(level.equals(Level.WARN))
		{
			logger.warn(message);
		}
		if(level.equals(Level.ERROR))
		{
			logger.error(message);
		}
		if(level.equals(Level.FATAL))
		{
			logger.fatal(message);
		}
		_notifyUser(level,message,null,true);
	}
	public static void notifyUser(final Level level,final String message)
	{
		_notifyUser(level,message,null,true);
	}
	private static Font SMALL_FONT=new Font(new JLabel().getFont().getName(),Font.PLAIN,9);	
	public static void _notifyUser(final Level level,final String message,final StackTraceElement[] trace,final boolean onTop)
   {
      _notifyUser(level,message,trace,onTop,false);
   }
	public static void _notifyUser(final Level level,final String message,final StackTraceElement[] trace,final boolean onTop,final boolean prompt)
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				final RDialog dialog=new RDialog(new JLabel("place holder"));
				JPanel panel=new JPanel(new BorderLayout());
				JLabel label=new JLabel();
				label.setPreferredSize(new Dimension(50,50));
				label.setOpaque(true);
				String title=level.toString();
				if(level.equals(Level.FATAL))
				{
					label.setBackground(Color.black);
					title="Fatal Error";
				}
				else if(level.equals(Level.ERROR))
				{
					label.setBackground(Color.red);
					title="Error";
				}
				else if(level.equals(Level.WARN))
				{
					label.setBackground(Color.yellow);
					title="Warning";
				}
				else if(level.equals(Level.INFO))
				{
					label.setBackground(Color.green);
					title="Information";
				}
				else if(level.equals(Level.DEBUG))
				{
					label.setBackground(Color.gray);
					title="Debug Information";
				}
				panel.add(label,BorderLayout.WEST);
				JLabel messageLabel=new JLabel("<html><p style='width:250px'>"+message+"</p></html>");
				messageLabel.setOpaque(true);
				messageLabel.setBackground(Color.white);
				messageLabel.setForeground(Color.black);
				JPanel messagePane=new JPanel(new BorderLayout());
				messagePane.add(messageLabel,BorderLayout.CENTER);
				panel.add(messagePane,BorderLayout.CENTER);
				if(trace!=null)
				{
					final JPanel detailsPane=new JPanel(new GridBagLayout());
//					detailsPane.setVisible(false);
					int gridy=0;
					for(StackTraceElement e:trace)
					{
						detailsPane.add(new JLabel(e.toString()),GBCHelper.getLabelGBC(gridy++));
					}
					//messagePane.add(detailsPane,BorderLayout.SOUTH);
					final JButton detailsButton=new JButton("Details");
					detailsButton.setFont(SMALL_FONT);
					detailsButton.setUI(new BasicToggleButtonUI());
					JPanel buttonPane=new JPanel(new BorderLayout());
					buttonPane.setOpaque(true);
					buttonPane.setBackground(Color.white);
					buttonPane.add(detailsButton,BorderLayout.NORTH);
					panel.add(buttonPane,BorderLayout.EAST);
					final int lineHeight=new JLabel("1").getMaximumSize().height;

					detailsButton.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							new RDialog(message,new JScrollPane(detailsPane),new RBar()).display();
						}
					});
				}
				dialog.setView(panel);
				dialog.setTitle(title);
				dialog.setSize(new Dimension(475,150));
				if(prompt)
            {
               if(onTop)
               {
                  dialog.promptOnTop();
               }
               else
               {
                  dialog.prompt();
               }
            }
            else
            {
               if(onTop)
               {
                  dialog.displayOnTop();
               }
               else
               {
                  dialog.display();
               }
            }
			}
		}).start();
	}

	public static String exceptionToString(Throwable e)
	{
		String errorMessage= e.getClass().toString() + ": " + e.getMessage() + ":\n";
		for (StackTraceElement element : e.getStackTrace())
		{
			errorMessage += "\t" + element.getClassName() + "." + element.getMethodName() + ":" + element.getLineNumber() + "\n";
		}
		if (e.getCause()!=null)
		{
			errorMessage+="Caused By:"+exceptionToString(e.getCause());
		}
		return errorMessage;
	}
   public static boolean initUniversalLogger()
   {
      Logger logger= RootLogger.getRootLogger();
      logger.setLevel(Level.ALL);
      try
      {
         logger.addAppender(new RollingFileAppender(new PatternLayout(PATTERN),SystemOperations.SYSTEM_LOG_FOLDER+File.separator+"universalLog.log"));
         return true;
      }
      catch(Throwable t)
      {
         return false;
      }
   }
}
