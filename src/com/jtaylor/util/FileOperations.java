package com.jtaylor.util;

import com.jtaylor.util.datastructures.StructOperations;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

public class FileOperations
{
//	public static final Logger log = Logger.getLogger(FileOperations.class);
   public static FileFilter NON_HIDDEN_FILES=new FileFilter()
   {
      public boolean accept(File file)
      {
         return !file.isHidden();
      }
   };

   public static File getTempFolder()
	{
		File tmp = new File("/tmp/");
		tmp.mkdirs();
		return tmp;
	}

	public static String printFolderContents(File file)
	{
		return printFolderContents(file, 0);
	}

	public static String printFolderContents(File file, int depth)
	{
		if (file.isDirectory())
		{
			String s = (depth == 0 ? file.getAbsolutePath() + "\n" : getDepth(depth) + file.getName() + "\n");
			for (File subFile : file.listFiles())
			{
				s += printFolderContents(subFile, depth + 1) + "\n";
			}
			return s;
		}
		else
		{
			return (depth == 0 ? file.getAbsolutePath() : getDepth(depth) + file.getName());
		}
	}

	private static String getDepth(int x)
	{
		String s = "";
		for (int i = 0; i < x; i++)
		{
			s += "\t";
		}
		return s;
	}

	public static List<File> getNestedFiles(File file)
	{
		if (file.isDirectory())
		{
			List<File> nestedFiles = new Vector<File>();
			for (File f : file.listFiles())
			{
				nestedFiles = StructOperations.concatinateLists(nestedFiles, getNestedFiles(f));
			}
			return nestedFiles;
		}
		else
		{
			return Arrays.asList(file);
		}
	}
	public static long getChecksum(File file) throws IOException
   {
      return getChecksum(new FileInputStream(file));
	}

	public static final int BUFFER_SIZE = 1024;
	public static long getChecksum(InputStream inputStream) throws IOException
   {
      CheckedInputStream checkIn = new CheckedInputStream(inputStream, new Adler32());
      byte[] buffer=new byte[BUFFER_SIZE];
      int read=0;
      while(read!=-1)
      {
         read=checkIn.read(buffer);
      }
      inputStream.close();
      return checkIn.getChecksum().getValue();
	}

	public static void rmdirOnExit(final File folder)
	{
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
		{
			public void run()
			{
				System.gc();
				System.out.println("deleting file: " + folder + " sucess was: " + rmdir(folder));

			}
		}));
	}

	public static boolean rmdir(File folder)
	{
		if (folder.canWrite())
		{
			if (folder.isDirectory())
			{
//				System.out.println("listing files for folder: "+folder);
				for (File fileOrFolder : folder.listFiles())
				{
//					System.out.println("\t"+fileOrFolder);
					if (fileOrFolder.isDirectory())
					{
//						System.out.println("\t\tisDir");
						if (!rmdir(fileOrFolder))
						{
							return false;
						}
					}
					else
					{
//						System.out.println("\t\tisFile");
						if (!fileOrFolder.delete())
						{
							return false;
						}
					}
				}
				folder.delete();
				return true;
			}
			else
			{
				return folder.delete();
			}
		}
		else
		{
			return false;
		}
	}

	public static File getUniqueFolder(File folder, String name)
	{
		File file = new File(folder, name);
		int count = 1;
		while (file.exists())
		{
			file = new File(folder, _modifyFolderName(name, count++));
		}
		return file;
	}

	public static String _modifyFolderName(String name, int count)
	{
		return name + "-" + count;
	}

	public static File getUniqueFile(File folder, String name)
	{
		File file = new File(folder, name);
		int count = 1;
		while (file.exists())
		{
			file = new File(folder, _modifyFileName(name, count++));
		}
		return file;
	}

	public static String _modifyFileName(String name, int count)
	{
		int index = name.lastIndexOf('.');
		if (index < 1)
		{
			return name + "-" + count;
		}
		else
		{
			return name.substring(0, index) + "-" + count + name.substring(index);
		}
	}

	public static File getParentFolder(File file, String name)
	{
		if (file == null)
		{
			return null;
		}
		else if (file.getName().equals(name))
		{
			return file;
		}
		else
		{
			return getParentFolder(file.getParentFile(), name);
		}
	}

	public static int countNestedFiles(File folder)
	{
		return countNestedFiles(folder,true,false);
	}
	public static int countNestedFiles(File folder,boolean countHidden,boolean countDirectories)
	{
		if (folder.isDirectory())
		{
			int count = countDirectories?1:0;
			for (File f : folder.listFiles())
			{
				if(!f.isHidden()||countHidden)
				{
					if (f.isDirectory())
					{
						count += countNestedFiles(f)+(countDirectories?1:0);
					}
					else
					{
						count++;
					}
				}
			}
			return count;
		}
		else
		{
			return 1;
		}
	}

	public static File makeSymLink(File linkPath, File targetDir) throws IOException, InterruptedException
   {
      String[] command;
      if (SystemOperations.isWinOS())
      {
         command = new String[]{"cmd", "/c", "linkd", linkPath.getAbsolutePath(), targetDir.getAbsolutePath()};
      }
      else
      {
         command = new String[]{"ln", "-s", targetDir.getAbsolutePath(), linkPath.getAbsolutePath()};
      }
      if (!linkPath.exists())
      {
         Runtime.getRuntime().exec(command).waitFor();
         if (linkPath.exists())
         {
            return linkPath;
         }
      }
		return null;
	}

	public static String head(int n, File file) throws IOException
   {
      return head(n, new FileInputStream(file));
	}

	public static String head(int n, InputStream stream) throws IOException
   {
		String output = "";
		boolean first = true;
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      for (int i = 0; i < n && reader.ready(); i++)
      {
         if (first)
         {
            first = false;
         }
         else
         {
            output += "\n";
         }
         output += reader.readLine();
      }
		return output;

	}

   public static List<String> readLines(File file) throws IOException
   {
      return readLines(new FileInputStream(file));
   }
   public static List<String> readLines(InputStream stream) throws IOException
   {
      List<String> lines=new Vector<String>();
      BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
      String line=null;
      boolean first=true;
      while(first||line!=null)
      {
         first=false;
         line=reader.readLine();
         if(line!=null)
         {
            lines.add(line);
         }
      }
      return lines;

   }

	public static byte[] readBytes(InputStream stream) throws IOException
	{
		ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
		byte bytes[] = new byte[BUFFER_SIZE];
		BufferedInputStream inputStream = new BufferedInputStream(stream);
		int n;
		while ((n = inputStream.read(bytes, 0, BUFFER_SIZE)) != -1)
		{
			byteArrayStream.write(bytes, 0, n);
		}
		return byteArrayStream.toByteArray();
	}

	public static String readFirstLine(File file) throws IOException
   {
		return head(1, file);
	}

	public static boolean clearFolder(File folder)
	{
		try
		{
			File[] files = folder.listFiles();
			boolean success = true;
			for (File file : files)
			{
				try
				{
					file.delete();
				}
				catch (Exception e)
				{
					success = false;
				}
			}
			return success;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static boolean canReadAll(File folder)
	{
		return canReadOrWriteAll(folder, true);
	}

	public static boolean canWriteAll(File folder)
	{
		return canReadOrWriteAll(folder, false);
	}

	public static boolean canReadOrWriteAll(File folder, boolean readOrWrite)
	{
		if (readOrWrite ? folder.canRead() : folder.canWrite())
		{
			if (folder.isDirectory())
			{
				for (File f : folder.listFiles())
				{
					if (!canReadAll(f))
					{
						return false;
					}
				}
				return true;

			}
			else
			{
				return true;
			}
		}
		else
		{
			return false;
		}
	}
	public static File[] listNonHiddenFiles(File folder)
	{
		return folder.listFiles(new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				return !name.startsWith(".");
			}
		});
	}

   public static void copyFile(File srcFile, File destFile) throws IOException
   {
      FileInputStream inStream=new FileInputStream(srcFile);
      if(destFile.isDirectory())
      {
         destFile=new File(destFile,srcFile.getName());
      }
      FileOutputStream outStream=new FileOutputStream(destFile);
      writeStreamToStream(inStream,outStream);
   }
   public static File searchDirectoryForFileName(File dirFold, String fileName,boolean caseSensitive)
   {
      return searchDirectoryForFileName(dirFold,fileName,caseSensitive,false);
   }
   public static File searchDirectoryForFileName(File dirFold, String fileName,boolean caseSensitive,boolean ignoreExtension)
   {
      File foundFile;
      String fileNameName=fileName.contains(".")?fileName.substring(0,fileName.lastIndexOf(".")):fileName;
      for(File f:dirFold.listFiles())
      {
         String name=f.getName();
         boolean match;
         if(ignoreExtension)
         {
            if(caseSensitive)
            {
               if(name.contains("."))
               {
                  match=name.substring(0,name.lastIndexOf(".")).equals(fileNameName);
               }
               else
               {
                  match=name.equals(fileNameName);
               }
            }
            else
            {
               if(name.contains("."))
               {
                  match=name.substring(0,name.lastIndexOf(".")).equalsIgnoreCase(fileNameName);
               }
               else
               {
                  match=name.equalsIgnoreCase(fileNameName);
               }
            }
         }
         else
         {
            if(caseSensitive)
            {
               match=name.equals(fileName);
            }
            else
            {
               match=name.equalsIgnoreCase(fileName);
            }
         }
         if(match)
         {
//            System.out.println("match: "+f);
            return f;
         }
         else
         {
//            log.debug("nomatch: "+f);
            if(f.isDirectory())
            {
//               log.debug("searching sub folder: "+f);
               foundFile=searchDirectoryForFileName(f,fileName,caseSensitive,ignoreExtension);
               if(foundFile!=null)
               {
                  return foundFile;
               }
            }
         }
      }
//      log.error("file not found: "+fileNameName+" in "+dirFold);
      return null;
   }

   public static void writeFileToStream(File srcFile, OutputStream outStream) throws IOException
   {
      FileInputStream inStream=new FileInputStream(srcFile);
      writeStreamToStream(inStream, outStream);
   }
   public static void writeStreamToStream(InputStream src,OutputStream dest) throws IOException
   {
      int read=0;
      byte[] buffer=new byte[BUFFER_SIZE];
      while(read>-1)
      {
         read=src.read(buffer);
         if(read>0)
         {
            dest.write(buffer, 0, read);
         }
      }
      src.close();
      dest.close();
   }
   private static FileFilter NO_FILTER=new FileFilter()
   {
      public boolean accept(File pathname)
      {
         return true;
      }
   };
   public static List<File> listFilesRecursive(File directory)
   {
      return listFilesRecursive(directory,NO_FILTER,false);
   }
   public static List<File> listFilesRecursive(File directory,FileFilter filter,boolean filterDirs)
   {
      List<File> files=new Vector<File>();
      for(File f:directory.listFiles(filterDirs?filter:NO_FILTER))
      {
         if(f.isDirectory())
         {
            files.addAll(listFilesRecursive(f,filter,filterDirs));
         }
         else
         {
            if(filterDirs)
            {
//               System.out.println("adding file: "+f);
               files.add(f);
            }
            else
            {
               if(filter.accept(f))
               {
//                  System.out.println("adding file: "+f);
                  files.add(f);
               }
               else
               {
//                  System.out.println("rejecing file: "+f);
               }
            }
         }
      }
      return files;
   }
   public static final int DEFAULT_BUFFER = 1024;
   public static void catToFile(InputStream stream, File outFile) throws IOException
   {
      catToStream(stream, new FileOutputStream(outFile));
   }
   public static void catToStream(File inFile, OutputStream outStream) throws IOException
   {
      catToStream(new FileInputStream(inFile),outStream);
   }
   public static void catToStream(InputStream inStream, OutputStream outStream) throws IOException
   {
      catToStream(inStream,true,outStream,true);
   }
   public static void catToStream(InputStream inStream,boolean closeIn, OutputStream outStream,boolean closeOut) throws IOException
   {
      int read = 0;
      int total = 0;
      byte[] buffer = new byte[DEFAULT_BUFFER];
      BufferedInputStream in = new BufferedInputStream(inStream);
      BufferedOutputStream out = new BufferedOutputStream(outStream);

      while (read > -1)
      {
         read = in.read(buffer);
         if (read > -1)
         {
            total += read;
            out.write(buffer, 0, read);
         }
      }
      if(closeOut)
      {
         out.close();
      }
      if(closeIn)
      {
         in.close();
      }
   }
   public static String cat(InputStream inStream) throws IOException
   {
      return new String(catToBytes(inStream));
   }

   public static String cat(File inFile) throws IOException
   {
      return new String(catToBytes(new FileInputStream(inFile)));
   }
   public static byte[] catToBytes(InputStream inStream) throws IOException
   {
      return catToBytes(inStream,true);
   }
   public static byte[] catToBytes(InputStream inStream, boolean closeIn) throws IOException
   {
      ByteArrayOutputStream out=new ByteArrayOutputStream();
      catToStream(inStream,closeIn,out,true);
      return out.toByteArray();
   }
}
