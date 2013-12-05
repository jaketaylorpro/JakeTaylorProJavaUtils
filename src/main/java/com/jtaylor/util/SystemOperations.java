package com.jtaylor.util;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemOperations
{

	public static final String ALL_USERS_PROFILE = System.getenv("ALLUSERSPROFILE");
   public static final String APP_DATA=System.getenv("APPDATA");
   public static final String LOCAL_APP_DATA=System.getenv("LOCALAPPDATA");
   public static final String HOME=System.getProperty("user.home");
	public static final String SYSTEM_LIBRARY_FOLDER = (isWinXP()||isWin2003() ? ALL_USERS_PROFILE + File.separator + "Application Data" + File.separator:
																		 (isWinOS() ? ALL_USERS_PROFILE + File.separator:
																		  (isMacOS() ? "/Library/Application Support/" :
																			"/opt/")));
	public static final String SYSTEM_LOG_FOLDER = (isWinXP()||isWin2003() ? ALL_USERS_PROFILE + File.separator + "Application Data" + File.separator + "Logs" + File.separator :
																	(isWinOS() ? ALL_USERS_PROFILE + File.separator + "Logs" + File.separator :
																	 (isMacOS() ? "/Library/Logs/" :
																	  "/var/log/")));


	static
	{
		//System.out.println("com.moksa.SystemConstants {OS: " + getOSNameVersionAndArch()+";lib: " + SYSTEM_LIBRARY_FOLDER+";log-local: "+LOCAL_LOG_FOLDER+";log-system: "+SYSTEM_LOG_FOLDER+"}");
	}
	public static String getOSNameVersionAndArch()
	{
		return System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch");
	}

	public static boolean isWinXP()
	{
		return System.getProperty("os.name").toUpperCase().equals("WINDOWS XP");
	}

	public static boolean isWin2003()
	{
		return System.getProperty("os.name").toUpperCase().equals("WINDOWS 2003");
	}

	public static boolean isWinVista()
	{
		return System.getProperty("os.name").toUpperCase().equals("WINDOWS VISTA");
	}

	public static boolean isWinOS()
	{
		return System.getProperty("os.name").toUpperCase().startsWith("WINDOWS");
	}

	public static boolean isMacOS()
	{
		return System.getProperty("os.name").toUpperCase().startsWith("MAC OS");
	}

	public static boolean isNixOS()
	{
		return !isWinOS() && !isMacOS();
	}

	public static File getHomeFoler()
	{
		return new File(System.getProperty("user.home"));
	}

   public static void openWebPage(URL url) throws URISyntaxException, IOException
   {
      Desktop.getDesktop().browse(url.toURI());
   }
   public static void openFile(File file) throws IOException
   {
      Desktop.getDesktop().edit(file);
   }

	public static HyperlinkListener getWebPageOpener()
	{
		return new HyperlinkListener()
		{
			public void hyperlinkUpdate(HyperlinkEvent e)
			{
				if (e.getEventType().equals(EventType.ACTIVATED))
				{
               try
               {
					   openWebPage(e.getURL());
               }
               catch (Exception t)
               {
                  Logging.notifyUser(t);
               }
				}
			}
		};
	}

	public static String getMacAddress()
	{
		try
		{
			Process process = Runtime.getRuntime().exec(new String[]{"ifconfig", "en0", "ether"});
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			reader.readLine();
			String line = reader.readLine();
			return line.substring(line.indexOf("ether") + "ether".length() + 1, line.length());
		}
		catch (Exception e)
		{
			System.out.println("getPublicIPException);");
			e.printStackTrace();
			return null;
		}
	}

	public static void copyToClipboard(String text)
	{
		try
		{
			Process process = Runtime.getRuntime().exec(new String[]{"pbcopy", text});
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			writer.write(text);
			writer.close();
		}
		catch (Exception e)
		{
         System.err.println("there was an error copying to the clipboard");
			e.printStackTrace();
		}
	}
	public static Date getServerTime() throws IOException, ParseException
   {
		String timeString= getServerTimeString();
      String[] tokens=timeString.split(" ");
      return new SimpleDateFormat("yy-MM-dd hh:mm:ss").parse(tokens[1]+" "+tokens[2]);
	}
	private static String getServerTimeString() throws IOException
   {
		Socket sock=new Socket("time-a.timefreq.bldrdoc.gov",13);
      InputStream in = sock.getInputStream();
      byte[] bytes = new byte[256]; int next_byte = -1; int i=0; while ((next_byte = in.read()) != -1
      && (i < bytes.length)) bytes[i++] = (byte) next_byte;
      sock.close();
      return new String(bytes);
	}
	public static int getPID()
	{
		try
		{
			String name=ManagementFactory.getRuntimeMXBean().getName();
			int i=name.indexOf("@");
			if(i>-1)
			{
				return Integer.parseInt(name.substring(0,i));
			}
			return -1;
		}
		catch(Exception e)
		{
			return -1;
		}
	}
	public static boolean processIsRunning(int pid) throws IOException, InterruptedException
   {
      if(pid<0)
      {
         return false;
      }
		if(isWinOS())
		{
			Process process = Runtime.getRuntime().exec(new String[]{"tasklist", "/fi", "\"PID eq "+pid+"\"","/nh"});
         process.waitFor();
         String err = FileOperations.cat(process.getErrorStream());
         String out = FileOperations.cat(process.getInputStream());
         if(err.length()==0)
         {
            return out.contains(""+pid);
            //if it's not running the output will be:
            //INFO: No tasks are running which match the specified criteria.
         }
         else
         {
            return false;
         }
		}
		else
		{
			Process process=Runtime.getRuntime().exec(new String[]{"kill","-0",""+pid});
         process.waitFor();
         String err= FileOperations.cat(process.getErrorStream());
         if(err.length()==0)
         {
            return true;
         }
         else if(err.endsWith("Operation not permitted"))
         {
            return true;
         }
         else
         {
            return false;
         }
		}
	}
}
