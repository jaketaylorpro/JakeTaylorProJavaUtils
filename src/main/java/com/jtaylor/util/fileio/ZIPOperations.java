package com.jtaylor.util.fileio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.jtaylor.util.FileOperations;

public class ZIPOperations
{
	private static final String PATH_SEPERATOR="/";
	public static void main(String[] args)
	{
      try
      {
         unzip(new File("/Users/jtaylor/Downloads/Z1020_E19_002.qlr.bin"),new File("/Users/jtaylor/Desktop/box/zipTest"));
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
	}
   public static void unzip(File zipFile,File destFolder) throws IOException
   {
      ZipInputStream zipIn=new ZipInputStream(new FileInputStream(zipFile));
      ZipEntry zipEntry=null;
      boolean done=false;
      while(!done)
      {
         zipEntry=zipIn.getNextEntry();
         if(zipEntry==null)
         {
            done=true;
         }
         else
         {
            System.out.println("zipentryname: "+zipEntry.getName()+" "+zipEntry.isDirectory());
            File file=new File(destFolder+File.separator+zipEntry.getName());
            if(zipEntry.isDirectory())
            {
               file.mkdir();
               file.setLastModified(zipEntry.getTime());
               System.out.println("made dir");
            }
            else
            {
               file.createNewFile();
               FileOperations.catToStream(zipIn, false, new FileOutputStream(file),true);
               System.out.println("made file");
            }
         }
      }
      zipIn.close();
   }
   public static void zip(File destZipFile, File ... files) throws IOException
   {
      destZipFile.createNewFile();
      ZipOutputStream zipOut=new ZipOutputStream(new FileOutputStream(destZipFile));
      for(File file:files)
      {
         if(file.isDirectory())
         {
            _zipFolder(file, zipOut, "");
         }
         else
         {
            _zipFile(file,zipOut,"");
         }
         zipOut.flush();
         zipOut.close();
      }
   }
   public static void _zipFolder(File folder, ZipOutputStream zipOut,String path) throws IOException
   {
      String newPath=path+folder.getName()+PATH_SEPERATOR;
      for(File fileOrFolder:folder.listFiles())
      {
         if(fileOrFolder.isDirectory())
         {
            _zipFolder(fileOrFolder, zipOut, newPath);
         }
         else
         {
            _zipFile(fileOrFolder, zipOut, newPath);
         }
      }
   }
   public static void _zipFile(File file, ZipOutputStream zipOut,String path) throws IOException
   {
      zipOut.putNextEntry(new ZipEntry(path+file.getName()));
      FileOperations.catToStream(file, zipOut);
   }



	public static boolean zipFileOrFolder(File fileOrFolder,File destZipFile)
	{
		try
        {
	        destZipFile.createNewFile();
        }
        catch (IOException e)
        {
	        e.printStackTrace();
        }
		if(destZipFile.canWrite())
		{
			try
            {
	            ZipOutputStream zipOut=new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destZipFile)));
	            if(fileOrFolder.isDirectory())
	            {
	            	_writeFolderToZip(fileOrFolder,zipOut,"");
	            }
	            else
	            {
	            	_writeFileToZip(fileOrFolder,zipOut,"");
	            }
	            zipOut.flush();
	            zipOut.close();
	            return true;
	            
            }
            catch (Exception e)
            {
	            e.printStackTrace();
	            return false;
            }
		}
		else
		{
			return false;
		}
	}
	public static void _writeFileToZip(File file,
            ZipOutputStream zipOut,String path)
    {
		try
        {
	        zipOut.putNextEntry(new ZipEntry(path+file.getName()));
	        BufferedInputStream fin=new BufferedInputStream(new FileInputStream(file));
	        byte[] buffer=new byte[1024];
	        int available;
	        while(fin.available()>0)
	        {
	        	if(fin.available()>1024)
	        	{
	        		fin.read(buffer);
	        		zipOut.write(buffer);
	        	}
	        	else
	        	{
	        		available=fin.available();
	        		fin.read(buffer, 0, available);
	        		zipOut.write(buffer, 0, available);
	        	}
	        }
	        fin.close();
        }
        catch (IOException e)
        {
	        e.printStackTrace();
        }
    }
	public static void _writeFolderToZip(File folder,
            ZipOutputStream zipOut,String path)
    {
		String newPath=path+folder.getName()+PATH_SEPERATOR;
		for(File fileOrFolder:folder.listFiles())
		{
			if(fileOrFolder.isDirectory())
			{
				_writeFolderToZip(fileOrFolder, zipOut, newPath);
			}
			else
			{
				_writeFileToZip(fileOrFolder, zipOut, newPath);
			}
		}
    }
}
