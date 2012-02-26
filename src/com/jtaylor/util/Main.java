package com.jtaylor.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: Dec 14, 2010
 * Time: 12:58:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main
{
	public static final String COMMAND_STAT="STAT";
	public static final String COMMAND_LDAP="LDAP";
	public static final String COMMAND_CHECKSUM="CHECKSUM";
	public static final String COMMAND_AWTTOOLKIT="AWTTOOLKIT";
	public static final String COMMAND_EMAIL="EMAIL";
	public static final String COMMAND_RECOVER_VAULT="RECOVER_VAULT";
	public static final List<String> COMMANDS=Arrays.asList(COMMAND_STAT,COMMAND_CHECKSUM,/*COMMAND_AWTTOOLKIT,COMMAND_EMAIL,*/COMMAND_RECOVER_VAULT,COMMAND_LDAP);

	public static final String KEYS_STAT_FILE="file";
	public static final List<String> KEYS_STAT=Arrays.asList(KEYS_STAT_FILE);

   public static final String KEYS_LDAP_SERVER="server";
   public static final String KEYS_LDAP_USER="user";
   public static final String KEYS_LDAP_PASS="pass";
   public static final String KEYS_LDAP_BASE="base";
   public static final String KEYS_LDAP_LOOKUP="lookup";
   public static final List<String> KEYS_LDAP=Arrays.asList(KEYS_LDAP_SERVER,KEYS_LDAP_USER,KEYS_LDAP_PASS,KEYS_LDAP_BASE,KEYS_LDAP_LOOKUP);


	public static final String KEYS_CHECKSUM_FILE = "file";
	public static final List<String> KEYS_CHECKSUM = Arrays.asList(KEYS_STAT_FILE);

   public static final String KEYS_EMAIL_FROM = "from";
   public static final String KEYS_EMAIL_PASS = "password";
   public static final String KEYS_EMAIL_TO = "to";
   public static final String KEYS_EMAIL_SUBJECT = "subject";
   public static final String KEYS_EMAIL_BODY = "body";
   public static final List<String> KEYS_EMAIL = Arrays.asList(KEYS_EMAIL_FROM,KEYS_EMAIL_PASS,KEYS_EMAIL_TO,KEYS_EMAIL_SUBJECT,KEYS_EMAIL_BODY);

   public static final String KEYS_RECOVER_VAULT_DIR="dir";
   public static final String KEYS_RECOVER_VAULT_IN="in";
   public static final String KEYS_RECOVER_VAULT_DEST="dest";
   public static final List<String> KEYS_RECOVER_VAULT=Arrays.asList(KEYS_RECOVER_VAULT_DIR,KEYS_RECOVER_VAULT_IN,KEYS_RECOVER_VAULT_DEST);

	public static final List<List<String>> KEYS=Arrays.asList(KEYS_STAT,KEYS_CHECKSUM,/*KEYS_EMAIL,*/KEYS_RECOVER_VAULT,KEYS_LDAP);

	public static final String FLAG_DEBUG = "debug";
	public static final List<String> FLAGS = Arrays.asList(FLAG_DEBUG);

	public static final String USAGE = "Usage:\n$java -jar Utilities.jar <COMMAND> -<key> <value> [--flag]" +
												  "\n\tSTAT -file <file path>" +
//												  "\n\tAWTTOOLKIT" +
												  "\n\tCHECKSUM -file <file path>"+
//												  "\n\tEMAIL -from <gmail address> -password <password> -to <email address> -subject <subject> -body <body>"+
												  "\n\tRECOVER_VAULT -dir <directory path> -in <list file> -dest <email address>"+
												  "\n\tLDAP -server <server> -user <user> -pass <password> -base <baseDN> -lookup <username to get email for>";
	public static void main(String[] args)
	{
      System.out.println("cp: "+System.getProperty("java.class.path"));
		String command=null;
		List<String> keys = new Vector<String>();
		List<String> values = new Vector<String>();
		List<String> flags = new Vector<String>();
		String arg;
		for (int i=0;i<args.length;i++)
		{
			if(i==0)
			{
				command=args[i];
				if(!COMMANDS.contains(command))
				{
					System.err.println("unknown command: "+command);
				}
			}
			else
			{
				arg=args[i];
				if (arg.startsWith("--"))
				{
					if (!FLAGS.contains(arg.substring(2)))
					{
						System.err.println("unknown flag: " + arg);
						System.exit(1);
					}
					else
					{
						flags.add(arg.substring(2));
					}
				}
				else if (arg.startsWith("-"))
				{
					if(KEYS.get(COMMANDS.indexOf(command)).contains(arg.substring(1)))
					{
						keys.add(arg.substring(1));
						if(i+1<args.length)
						{
							i++;
							if(!args[i].startsWith("--")&&!args[i].startsWith("-"))
							{
								values.add(args[i]);
							}
							else
							{
								System.err.println("key: "+arg+"; bad value: "+args[i]);
								System.exit(1);
							}
						}
					}
					else
					{
						System.err.println("unknown key: "+arg);
						System.exit(1);
					}
				}
			}
		}
      if(command==null)
      {
         System.out.println(USAGE);
      }
      else
      {

         if (command.equals(COMMAND_STAT))
         {
            if(keys.contains(KEYS_STAT_FILE))
            {
               File file=new File(values.get(keys.indexOf(KEYS_STAT_FILE)));
               boolean exists=file.exists();
               boolean canRead=file.canRead();
               boolean canWrite=file.canWrite();
               String absolutePath=file.getAbsolutePath();
               String canonicalPath;
               try
               {
                  canonicalPath=file.getCanonicalPath();
               }
               catch(Exception e)
               {
                  canonicalPath="<error: "+e.getMessage()+">";
               }
               String name=file.getName();
               String parent=file.getParent();
               String path=file.getPath();
               boolean isAbsolute=file.isAbsolute();
               boolean isFile=file.isFile();
               boolean isDirectory=file.isDirectory();
               boolean isHidden=file.isHidden();
               long lastModified=file.lastModified();
               Date lastModifiedDate=new Date(lastModified);
               long length=file.length();
               String[] list=file.list();
               int containedFileCount=list==null?0:list.length;

               System.out.println("Exists:\t"+exists);
               System.out.println("Is Readable:\t"+canRead);
               System.out.println("Is Writable:\t"+canWrite);
               System.out.println("Is Absolute:\t"+isAbsolute);
               System.out.println("Is File:\t"+isFile);
               System.out.println("  Length: "+length);
               System.out.println("Is Directory:\t"+isDirectory);
               System.out.println("  Count: "+containedFileCount);
               System.out.println("Is Hidden:\t"+isHidden);
               System.out.println("Name:\t\t"+name);
               System.out.println("Parent:\t\t"+parent);
               System.out.println("Path:\t\t"+path);
               System.out.println("Absolute Path:\t"+absolutePath);
               System.out.println("Canonical Path:\t"+canonicalPath);
               System.out.println("Modified:\t"+lastModified);
               System.out.println("  Date: "+ new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(lastModifiedDate));
            }
            else
            {
               System.err.println("no file specified. usage:\n"+USAGE);
               System.exit(1);
            }
         }
         else if(command.equals(COMMAND_CHECKSUM))
         {
            if(keys.contains(KEYS_CHECKSUM_FILE))
            {
               File file = new File(values.get(keys.indexOf(KEYS_STAT_FILE)));
               boolean exists = file.exists();
               boolean canRead = file.canRead();
               boolean isFile=file.isFile();
               if(exists&&canRead)
               {
                  if(isFile)
                  {
                     System.out.println("Checksum: "+FileOperations.getChecksum(file));
                  }
                  else
                  {
                     FileOperations.getRecursiveChecksum(file,false);
                  }
               }
               else
               {
                  if(!exists)
                  {
                     System.err.println("file does not exist: "+file.getAbsolutePath());
                     System.exit(1);
                  }
                  else
                  {
                     System.err.println("cannot read file: "+file.getAbsolutePath());
                     System.exit(1);
                  }
               }
            }
            else
            {
               System.err.println("no file specified. usage:\n"+USAGE);
               System.exit(1);
            }
         }
         else if(command.equals(COMMAND_AWTTOOLKIT))
         {
            System.out.println("awt.toolkit: "+System.getProperty("awt.toolkit"));
         }
//         else if(command.equals(COMMAND_EMAIL))
//         {
//            if(keys.contains(KEYS_EMAIL_TO)&&keys.contains(KEYS_EMAIL_FROM)&&keys.contains(KEYS_EMAIL_SUBJECT)&&keys.contains(KEYS_EMAIL_BODY)&&keys.contains(KEYS_EMAIL_PASS))
//            {
//               System.out.print("sending ... ");
//               EmailOperations.SMTP smtp=new EmailOperations.GSMTP(values.get(keys.indexOf(KEYS_EMAIL_FROM)),values.get(keys.indexOf(KEYS_EMAIL_PASS)));
//               try
//               {
//                  smtp.send(new EmailOperations.EMail(Arrays.asList(values.get(keys.indexOf(KEYS_EMAIL_TO))),new Vector<String>(),new Vector<String>(),new Vector<String>(),null,values.get(keys.indexOf(KEYS_EMAIL_SUBJECT)),values.get(keys.indexOf(KEYS_EMAIL_BODY)),false,null));
//                  System.out.println("done");
//               }
//               catch (MessagingException e)
//               {
//                  System.out.println("error");
//                  e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//               }
//            }
//            else
//            {
//               System.err.println("missing keys. usage:\n"+USAGE);
//               System.exit(1);
//            }
//         }
         else if(command.equals(COMMAND_RECOVER_VAULT))
         {
//            System.out.println("debug, keys: "+keys);
//            System.out.println("debug, values: "+values);
            if(keys.contains(KEYS_RECOVER_VAULT_DIR)&&keys.contains(KEYS_RECOVER_VAULT_DEST)&&keys.contains(KEYS_RECOVER_VAULT_IN))
            {
               File dirFold=new File(values.get(keys.indexOf(KEYS_RECOVER_VAULT_DIR)));
               if(!dirFold.exists()||!dirFold.isDirectory())
               {
                  System.err.println("vault dir does not exist");
                  System.exit(1);
               }
               File inFile=new File(values.get(keys.indexOf(KEYS_RECOVER_VAULT_IN)));
               if(!inFile.exists()||!inFile.isFile())
               {
                  System.err.println("in file does not exist");
                  System.exit(1);
               }
               File destFold=new File(values.get(keys.indexOf(KEYS_RECOVER_VAULT_DEST)));
               if(!destFold.exists()||!destFold.canWrite())
               {
                  System.err.println("dest folder does not exist");
                  System.exit(1);
               }
               List<String> searchFileNames=FileOperations.readLines(inFile);
               TerminalProgressBar progress=new TerminalProgressBar("searching vault",searchFileNames.size());
               File foundFile;
               File dataFile;
               for(String fileName:searchFileNames)
               {
                  foundFile=FileOperations.searchDirectoryForFileName(dirFold,fileName,false);
                  if(foundFile==null)
                  {
                     System.err.println("could not find file: "+fileName);
                  }
                  else
                  {
//                     System.out.println("found file: "+fileName);
                     int max=0;
                     int cur;
                     dataFile=null;
                     for(File f:foundFile.listFiles())
                     {
                        try
                        {
                           cur=Integer.parseInt(f.getName().substring(0,f.getName().lastIndexOf(".")));
//                           System.out.println("cur: "+cur);
                           if(cur>max)
                           {
                              max=cur;
                              dataFile=f;
                           }
                        }
                        catch(Exception e)
                        {
                        }
                     }
                     if(dataFile==null)
                     {
                        System.err.println("could not find datafile for: "+foundFile);
                     }
                     else
                     {
                        try
                        {
                           FileOperations.copyFile(dataFile,new File(destFold,fileName));
                        }
                        catch (Exception e)
                        {
                           System.err.println("there was an error copying file: "+foundFile);
                           e.printStackTrace();
                        }
                     }
                  }
                  progress.increment();
               }
               progress.done();
               System.out.println("done");
            }
            else
            {
               System.err.println("missing keys. usage:\n"+USAGE);
               System.exit(1);
            }
         }
         else if (command.equals(COMMAND_LDAP))
         {
            try
            {
               LdapOperations ldap=new LdapOperations(values.get(0),values.get(1),values.get(2),values.get(3),null);
               System.out.println(values.get(4)+": "+ldap.getEmailAddressForName(values.get(4)));
//               System.out.println(values.get(4)+": "+ldap.getEmailAddress(values.get(4)));
            }
            catch (Exception e)
            {
               System.err.println("there was an error");
               e.printStackTrace();
            }

         }
         else
         {
            System.err.println("unimplemented command: "+command);
            System.exit(1);
         }
      }
	}
}
