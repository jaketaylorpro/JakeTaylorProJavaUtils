package com.jtaylor.util;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileFilters
{
	public static FileFilter getExcelFileFilter()
	{
		return new FileFilter()
		{
			public boolean accept(File f)
            {
	            return f.isDirectory()||f.getName().endsWith(".xls");
            }
            public String getDescription()
            {
	            return "Excel 97-2003 Workbook (*.xls)";
            }
		};
	}
	public static FileFilter getExcelXFileFilter()
	{
		return new FileFilter()
		{
			public boolean accept(File f)
            {
	            return f.isDirectory()||f.getName().endsWith(".xlsx");
            }
            public String getDescription()
            {
	            return "Excel 2007 Workbook (*.xlsx)";
            }
		};
	}
	public static FileFilter getExcelOrXFileFilter()
	{
		return new FileFilter()
		{
			public boolean accept(File f)
            {
	            return f.isDirectory()||f.getName().endsWith(".xlsx")||f.getName().endsWith(".xls");
            }
            public String getDescription()
            {
	            return "Excel Workbook (*.xlsx | *.xls)";
            }
		};
	}

	public static FileFilter getXMLFileFilter()
	{
		return new FileFilter()
		{
			public boolean accept(File f)
            {
	            return f.isDirectory()||f.getName().endsWith(".xml");
            }
            public String getDescription()
            {
	            return "XML File (*.xml)";
            }
		};
	}
	public static FileFilter getRTFFileFilter()
	{
		return new FileFilter()
		{
			public boolean accept(File f)
			{
				return f.isDirectory()||f.getName().endsWith(".rtf");
			}
			public String getDescription()
			{
				return "Rich Text Document (*.rtf)";
            }
		};
	}
	public static File promptForFile(FileFilter filter)
   {
      return promptForFile(null,filter);
   }
	public static File promptForFile(File currentDirectory,FileFilter filter)
	{
		JFileChooser chooser=new JFileChooser(currentDirectory);
		chooser.setFileFilter(filter);
		chooser.setMultiSelectionEnabled(false);
		if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
		{
			return chooser.getSelectedFile();
		}
		return null;
	}
	public static File promptForSaveFile(File currentDirectory,FileFilter filter)
   {
      JFileChooser chooser=new JFileChooser(currentDirectory);
      chooser.setFileFilter(filter);
      chooser.setMultiSelectionEnabled(false);
      if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
      {
         return chooser.getSelectedFile();
      }
      return null;
   }
	public static File promptForSaveFile(FileFilter filter)
	{
      return promptForSaveFile(null,filter);
	}
}
