package com.jtaylor.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import java.io.*;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 7/15/11
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class FTPOperations
{
   private String myServer;
   private String myUser;
   private String myPass;
   private FTPClient myFTPClient;
   private Logger log;
   public FTPOperations(String server,String user,String pass)
   {
      log=Logging.createServerLogger(FTPOperations.class);
      myServer=server;
      myUser=user;
      myPass=pass;
      myFTPClient=new FTPClient();
   }
   private void connect() throws IOException
   {
      myFTPClient.connect(myServer);
      myFTPClient.login(myUser,myPass);
      log.debug("connected");
   }
   private void disconnect() throws IOException
   {
      myFTPClient.disconnect();
      log.debug("disconnected");
   }
   public void downloadFile(String remotePath,File saveAs) throws IOException
   {
      connect();

      myFTPClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
      myFTPClient.setFileType(FTPClient.BINARY_FILE_TYPE);

      InputStream inputStream=myFTPClient.retrieveFileStream(remotePath);
      log.debug("got stream");
      saveAs.delete();
      FileOperations.catToFile(inputStream,saveAs);
      log.debug("done writing");
//      myFTPClient.completePendingCommand();
      disconnect();
   }
   public void uploadFile(String remotePath,File file) throws IOException
   {
//      System.out.println("connecting");
      connect();
//      System.out.println("done");
      myFTPClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
      myFTPClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//      System.out.println("set binary mode");
      OutputStream outputStream=myFTPClient.storeFileStream(remotePath);
//      System.out.println("got stream");
      log.debug("got stream");
      FileOperations.catToStream(new FileInputStream(file), outputStream);
//      System.out.println("done writing");
      log.debug("done writing");
      myFTPClient.completePendingCommand();
      disconnect();
//      System.out.println("disconnected");
   }
   public void deleteFile(String remotePath) throws IOException
   {
      connect();
      myFTPClient.deleteFile(remotePath);
      log.debug("done deleting");
      disconnect();
   }
   public List<String> listAllFiles() throws IOException
   {
      return listAllFiles("");
   }
   public List<String> listAllFiles(String startingPath) throws IOException
   {
      List<String> files=new Vector<String>();
      connect();
      files.addAll(_listAllFiles(startingPath));
      disconnect();
      return files;
   }
   private List<String> _listAllFiles(String path) throws IOException
   {
      List<String> files=new Vector<String>();
      FTPFile[] ftpFiles=myFTPClient.listFiles(path);
      for(FTPFile ftpFile:ftpFiles)
      {
         if(ftpFile.getType()==FTPFile.DIRECTORY_TYPE)
         {
            files.addAll(_listAllFiles(path.length()==0?ftpFile.getName():path+"/"+ftpFile.getName()));
         }
         else
         {
            files.add(path+"/"+ftpFile.getName());
         }
      }
      return files;
   }
   public boolean testConnection()
   {
      try
      {
         connect();
         disconnect();
         return true;
      }
      catch (IOException e)
      {
         return false;
      }
   }
   public boolean testPath(String path)
   {
      try
      {
         boolean found=false;
         connect();
         if(path.length()==0)
         {
            found=true;
         }
         else if(path.startsWith("/"))
         {
            found=false;
         }
         else
         {
            if(path.endsWith("/"))
            {
               path=path.substring(0,path.length()-1);
            }
            String basePath="";
            String namePath=path;
            if(path.contains("/"))
            {
               basePath=path.substring(0,path.lastIndexOf("/"));
               namePath=path.substring(path.lastIndexOf("/")+1);
            }
            log.debug("basepath: "+basePath);
            log.debug("namepath: "+namePath);
            FTPFile[] ftpFiles=myFTPClient.listFiles(basePath);
            for(FTPFile ftpFile:ftpFiles)
            {
               log.debug("file: "+ftpFile);
               if(ftpFile.getName().equalsIgnoreCase(namePath))
               {
                  if(ftpFile.getType()==FTPFile.DIRECTORY_TYPE)
                  {
                     found=true;
                     break;
                  }
               }
            }
         }
         disconnect();
         return found;
      }
      catch (IOException e)
      {
         return false;
      }
   }
   public static void main(String[] args)
   {
      Logger log=Logger.getLogger("com.moksa.test");
      log.addAppender(new ConsoleAppender(new SimpleLayout()));
      try
      {
         //test1
//         FTPOperations ftpOperations=new FTPOperations("ph03.droa.com","wh167089","ro7Sy6oY");
//         System.out.println("all files: "+ftpOperations.listAllFiles());
//         System.out.println("done");
         //test2
//         FTPOperations ftpOperations=new FTPOperations("ph03.droa.com","wh167089","ro7Sy6oY");
//         ftpOperations.downloadFile("httpdocs/Downloads/com.moksa.WorkflowGenerator.zip",new File("/Users/jtaylor/Downloads/com.moksa.WorkflowGenerator.zip"));
//         System.out.println("done");
         //test3
//         System.out.println("start");
//         FTPOperations ftpOperations=new FTPOperations("ph03.droa.com","wh167089","ro7Sy6oY");
//         ftpOperations.uploadFile("httpdocs/Downloads/com.moksa.WorkflowGenerator4.zip", new File("/Users/jtaylor/Downloads/com.moksa.WorkflowGenerator4.zip"));
//         System.out.println("done uploading");
//         System.out.println("files: "+ftpOperations.listAllFiles("httpdocs/Downloads"));
         //test4
//         System.out.println(new FTPOperations("ftp.qg.com","in_specialty_catalog","5082380199").testPath(""));
//         System.out.println(new FTPOperations("ftp.qg.com","in_specialty_catalog","5082380199").testPath("INCOMING"));
//         System.out.println(new FTPOperations("ftp.qg.com","in_specialty_catalog","5082380199").testPath("INCOMING/IMAGING"));
//         System.out.println(new FTPOperations("ftp.qg.com","in_specialty_catalog","5082380199").testPath("INCOMING/IMAGING/BOSTON"));
//         new FTPOperations("ftp.qg.com","in_specialty_catalog","5082380199").uploadFile("OUTGOING/IMAGING/BOSTON/test2.zip",new File("/Users/jtaylor/Downloads/test2.zip"));
//         new FTPOperations("ftp.qg.com","in_specialty_catalog","5082380199").uploadFile("OUTGOING/IMAGING/BOSTON/test2.zip",new File("/Users/jtaylor/Downloads/test2.zip"));
            new FTPOperations("ph03.droa.com","wh167089","ro7Sy6oY").downloadFile("private/testIN/finalCountdown.mp3.zip",new File("/Users/jtaylor/Downloads/finalCountdown.mp3.zip"));
//         System.out.println(new FTPOperations("ph03.droa.com","wh167089","ro7Sy6oY").testPath("httpdocs"));
//         System.out.println(new FTPOperations("ph03.droa.com","wh167089","ro7Sy6oY").testPath("/httpdocs"));
//         System.out.println(new FTPOperations("ph03.droa.com","wh167089","ro7Sy6oY").testPath("httpdocs/blanofolder"));
         System.out.println("done");
         //test5
//         FTPClient client=new FTPClient();
//         client.connect("ph03.droa.com");
//         client.login("wh167089", "ro7Sy6oY");
//         System.out.println("connected");
//         FTPFile[] files=client.listFiles();
//         for(FTPFile file:files)
//         {
//            System.out.println("file: "+file);
//         }
//         files=client.listDirectories();
//         for(FTPFile file:files)
//         {
//            System.out.println("directories: "+file);
//         }
//         client.changeWorkingDirectory("httpdocs/Downloads");
////         InputStream inputStream=client.retrieveFileStream("com.moksa.WorkflowGenerator.zip");
////         System.out.println("got stream");
////         FileOperations.catToFile(inputStream, new File("/Users/jtaylor/Downloads/com.moksa.WorkflowGenerator.zip"));
////         System.out.println("done writing");
////         client.completePendingCommand();
//         System.out.println("done");
//         client.disconnect();
      }
      catch(Exception e)
      {
         System.err.println("there was an error");
         e.printStackTrace();
      }
   }
}
