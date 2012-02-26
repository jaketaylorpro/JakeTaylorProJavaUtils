package com.jtaylor.util;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class DateOperations
{
	public static final SimpleDateFormat dateFormatyyyyMMddHHmmss= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	public static final SimpleDateFormat dateFormatyyyyMMdd= new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat dateFormatISO8601FolderNameFriendly= new SimpleDateFormat("yyyy-MM-dd-'T'-HH-mm-ss-SSS-'Z'");
	public static final SimpleDateFormat dateFormatISO8601= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	public static final SimpleDateFormat dateFormatISO8601NoZ= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	public static final SimpleDateFormat prettyDateFormat= new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	private static List<SimpleDateFormat> DATE_FORMATS=new Vector<SimpleDateFormat>();
	static
	{
		DATE_FORMATS=new Vector<SimpleDateFormat>();
		List<String> delims=Arrays.asList("-","/"," ",".");
		List<String> timeFormats=Arrays.asList(""," HH:mm"," HH:mm:ss"," HH:mm:ss.SSS","HH:mm:ss:SSS");
		List<String> timePostFixes=Arrays.asList(""," a"," zzz"," a zzz");
        DATE_FORMATS.add(dateFormatISO8601);
		String time;
        for(String delim:delims)
        {
			for(String timeFormat:timeFormats)
			{
				for(String timePostFix:timePostFixes)
				{
					time=timeFormat+timePostFix;
					DATE_FORMATS.add(new SimpleDateFormat("yyyy"+delim+"M"+delim+"d"+time));
					DATE_FORMATS.add(new SimpleDateFormat("M"+delim+"d"+delim+"y"+time));
					if(!time.equals(""))
					{
						DATE_FORMATS.add(new SimpleDateFormat(time));
					}
				}
			}
		}
        DATE_FORMATS.add(new SimpleDateFormat("MMM d y"));
        DATE_FORMATS.add(new SimpleDateFormat("E MMM d y"));
        DATE_FORMATS.add(new SimpleDateFormat("E MMM d y HH:mmss zzz"));
		DATE_FORMATS.add(new SimpleDateFormat("y"));
	}
	public static Calendar parseDate(String source)
	{
		return parseDate(source,null,null);
	}
	private static Calendar parseDate(String source,Date boundedAfter,Date boundedBefore)
    {
        for(SimpleDateFormat format:DATE_FORMATS)
        {
        	try
        	{
        		Date date= format.parse(source);
        		if((boundedAfter==null||date.after(boundedAfter))&&
        				(boundedBefore==null||date.before(boundedBefore)))
        		{
        			Calendar cal=Calendar.getInstance();
        			cal.setTime(date);
        			return cal;
        		}
        	}
        	catch(Exception e){}
        }
		System.err.println("parseDateException: could not parse: "+source);
		return null;
	}
	public static Date getServerDate()
	{
		return SystemOperations.getServerTime();
	}
	public static int getSecondsSinceEpoch(Date date)
	{
		return (int) (date.getTime()/1000);
	}
	public static Date parse(String source,List<SimpleDateFormat> formats)
	{
		for(SimpleDateFormat format:formats)
		{
			try
			{
				return format.parse(source); 
			}catch(Exception e){}
		}
		return null;
	}
	public static void main(String[] args)
	{
      System.out.println(getMillisecondsOfTime(new Date()));
      System.out.println(createDateFromMillisecondsOfTime(getMillisecondsOfTime(new Date())));
//		System.out.println(formatTime(31636000000L));
	}
	public static String formatTime(long milliseconds)
	{
		int seconds =(int) (milliseconds / 1000);
		int minutes = seconds / 60;
		int hours = minutes / 60;
		int days = hours / 24;
		int years = days/365;
		int days2 = days % 356;
		int hours2= hours%24;
		int minutes2 = minutes % 60;
		int seconds2 = seconds % 60;
		int milliseconds2=(int)milliseconds % 1000;
		return (years>0?years+"y ":"")+(days2>0?days2+"d ":"")+(hours2>0?hours2 + "h ":"") + (minutes2>0?minutes2 + "m ":"") + seconds2 + "s"+(milliseconds2==0?"":" "+milliseconds2+"ms");
	}
   public static int getMillisecondsOfTime(Date time)
   {
      int hours=Integer.parseInt(new SimpleDateFormat("hh").format(time))*1000*60*60;
      int minutes=Integer.parseInt(new SimpleDateFormat("mm").format(time))*1000*60;
      int seconds=Integer.parseInt(new SimpleDateFormat("ss").format(time))*1000;
      int milliSeconds=Integer.parseInt(new SimpleDateFormat("SSS").format(time));
      return hours+minutes+seconds+milliSeconds;
   }

   public static Date createDateFromMillisecondsOfTime(int milliseconds)
   {
      Logger log=Logging.getMoksaCallerLogger();
      int hours=milliseconds/(1000*60*60);
      int hoursR=milliseconds%(1000*60*60);
      int minutes=hoursR/(1000*60);
      int minutesR=hoursR%(1000*60);
      int seconds=minutesR/1000;
      int secondsR=minutesR%1000;
      String dateString=""+hours+":"+minutes+":"+seconds+"."+secondsR;
      try
      {
         return new SimpleDateFormat("hh:mm:ss.SSS").parse(dateString);
      }
      catch(Exception e)
      {
         log.error("there was an error creating a date from milliseconds",e);
         return null;
      }
   }
}
