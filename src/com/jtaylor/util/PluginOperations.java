package com.jtaylor.util;

import com.moksa.util.cumulus.EJPScriptingCommands;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

public class PluginOperations
{

	public static <T> List<Class<? extends T>> readPlugins(File folder, ClassLoader ejpLoader, Class<T> ofClass) throws IOException, ClassNotFoundException
   {
		return readPlugins(folder, ejpLoader, ofClass, 0);
	}

	/**
	 * reads jar files from a folder and if the main class specified in the manifest is assignable to ofClass, it returns it in the list
	 *
	 * @param folder
	 * @param ejpLoader
	 * @param ofClass
	 * @param recursive the number of directories to recurse through. -1 means it will fully search the directory
	 * @param <T>
	 * @return
	 */
	public static <T> List<Class<? extends T>> readPlugins(File folder, ClassLoader ejpLoader, Class<T> ofClass, int recursive) throws ClassNotFoundException, IOException
   {
		List<Class<? extends T>> plugins = new Vector<Class<? extends T>>();
		Class<? extends T> t;
		File libFolder;
		if (folder.exists() && folder.isDirectory())
		{
			for (File jar : folder.listFiles())
			{
				if (jar.isFile() && jar.getName().endsWith(".jar"))
				{
					//search for a lib folder at the same level
					libFolder=null;
					for(File f:jar.getParentFile().listFiles())
					{
						if(f.isDirectory()&&f.getName().equals("lib"))
						{
							libFolder=f;
							break;
						}
					}
               t = _readPlugin(jar,libFolder, ejpLoader, ofClass);
               if (t != null)
               {
                  plugins.add(t);
               }
				}
				else if (recursive != 0 && jar.isDirectory())
				{
					plugins.addAll(readPlugins(jar, ejpLoader, ofClass, recursive - 1));
				}
			}
		}
		return plugins;
	}

	public static <T> Class<? extends T> _readPlugin(File jar,File libFolder, ClassLoader ejpLoader, Class<T> ofClass) throws IOException, ClassNotFoundException
   {
      JarFile jarFile = new JarFile(jar);
      List<URL> jarURLs=new Vector<URL>();
      jarURLs.add(new URL("file","localhost",jar.getAbsolutePath()));
      String mainClassName = (String) jarFile.getManifest().getMainAttributes().get(Attributes.Name.MAIN_CLASS);
      URL url;
      if(libFolder!=null&&libFolder.isDirectory())
      {
         for(File f:FileOperations.getNestedFiles(libFolder))
         {
            if(f.getName().endsWith(".jar"))
            {
               url=new URL("file","localhost",f.getAbsolutePath());
               jarURLs.add(url);
            }
         }
      }
      URL[] jars=new URL[jarURLs.size()];
      jarURLs.toArray(jars);
      URLClassLoader loader = new URLClassLoader(jars, ejpLoader);
      Class<?> mainClass = Class.forName(mainClassName, true, loader);
      if (ofClass.isAssignableFrom(mainClass))
      {
         return mainClass.asSubclass(ofClass);
      }
      else
      {
         return null;
      }
	}
	/*public static <T> Class<? extends T> _readPlugin(File jar, ClassLoader ejpLoader, Class<T> ofClass)
	{
		try
		{
			JarFile jarFile = new JarFile(jar);
			String mainClassName = (String) jarFile.getManifest().getMainAttributes().get(Attributes.Name.MAIN_CLASS);
			URLClassLoader loader = new URLClassLoader(new URL[]{new URL("file", "localhost", jar.getAbsolutePath())}, ejpLoader);
			Class<?> mainClass = Class.forName(mainClassName, true, loader);
			if (ofClass.isAssignableFrom(mainClass))
			{
				return mainClass.asSubclass(ofClass);
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			log.error("there was an error loading the jar: " + jar, e);
			return null;
		}
	}*/

	public static <T> List<T> instantiateObjects(List<Class<? extends T>> classes) throws IllegalAccessException, InstantiationException
   {

		List<T> objects = new Vector<T>();
		for (Class<? extends T> c : classes)
		{
         objects.add(c.newInstance());
		}
		return objects;
	}
}
