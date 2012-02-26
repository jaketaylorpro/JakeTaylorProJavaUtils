package com.jtaylor.util;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 4/13/11
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcessReader
{
   public static final Logger log=Logger.getLogger(ProcessReader.class);

   private Process myProcess;
   private StreamReaderThread myErrorReader;
   private StreamReaderThread myOutputReader;

   public ProcessReader(Process process,Logger logger)
   {
      myProcess=process;
      myErrorReader=new StreamReaderThread(myProcess.getErrorStream(),logger,Level.ERROR);
      myErrorReader.start();
      myOutputReader=new StreamReaderThread(myProcess.getInputStream(),logger, Level.INFO);
      myOutputReader.start();
   }
   public String getStdErrOutput()
   {
      return myErrorReader.getOutput();
   }
   public String getStdOutOutput()
   {
      return myOutputReader.getOutput();
   }

   public static void main(String[] args)
   {
      Logger log=Logger.getLogger("com.moksa.test");
      log.addAppender(new ConsoleAppender(new PatternLayout(Logging.BASIC_PATTERN)));
      try
      {
         String bitrate="600k";
         int width=640;
         int height=360;
         int start=0;
         int length=10;
         File myFile=new File("/usr/local/uploaded_assets/test/fifteenMinuteTest.mp4");
         File convertedFile=new File("/usr/local/uploaded_assets/test/fifteenMinuteTestNew.ts");
         final Process p1 = Runtime.getRuntime().exec(new String[]{"/opt/local/bin/ffmpeg","-i",myFile.getAbsolutePath(),"-f","mpegts","-acodec","libmp3lame","-ar","48000","-ab","64k","-s",width+"x"+height,"-vcodec","libx264","-b",bitrate,"-flags","+loop","-cmp","+chroma","-partitions","+parti4x4+partp8x8+partb8x8","-subq","7","-trellis","0","-refs","0","-coder","0","-me_range","16","-keyint_min","25","-sc_threshold","40","-i_qfactor","0.71","-bt","200k","-maxrate",bitrate,"-bufsize",bitrate,"-rc_eq","'blurCplx^(1-qComp)'","-qcomp","0.6","-qmin","30","-qmax","51","-qdiff","4","-level","30","-aspect",width+":"+height,"-g","30","-async","2",convertedFile.getAbsolutePath()});
         final ProcessReader p1Reader=new ProcessReader(p1,log);
         p1.waitFor();
         System.out.println("exit code: "+p1.exitValue());
         System.out.println("err: " + p1Reader.getStdErrOutput());
         System.out.println("out: " + p1Reader.getStdOutOutput());

      }
      catch(Exception e)
      {
         System.err.println("there was an error");
         e.printStackTrace();
      }
   }
   public static String formatSS(int seconds)
   {
      int minutes = seconds / 60;
      int hours = minutes / 60;
      int minutes2 = minutes % 60;
      int seconds2 = seconds % 60;
      String hoursS=(hours<10?"0":"")+hours;
      String minutesS=(minutes2<10?"0":"")+minutes2;
      String secondsS=(seconds2<10?"0":"")+seconds2;
      return hoursS+":"+minutesS+":"+secondsS;
   }
}
