package com.jtaylor.util.fileio;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class RTFOperations
{
	public static final String DEFAULT_TITLE_FONT="Helvetica";
	public static final Color DEFAULT_TITLE_FONT_COLOR=Color.black;
	public static final String DEFAULT_HEADER_FONT="Helvetica";
	public static final Color DEFAULT_HEADER_FONT_COLOR=Color.black;
	public static final Color DEFAULT_HEADER_CELL_COLOR=Color.lightGray;
	public static final String DEFAULT_DATA_FONT="Helvetica";
	public static final Color DEFAULT_DATA_FONT_COLOR=Color.black;
	public static final Color DEFAULT_DATA_CELL_COLOR=Color.lightGray;
	public static void writeRTFTable(File rtfFile,String[][] data,String title)
	{
		try
        {
	        FileOutputStream rtfOut=new FileOutputStream(rtfFile);
	        _writeRTFTable(rtfOut,data,title,true,true,null,null,null,null,null,null,null,null);
        }
        catch (FileNotFoundException e)
        {
        	System.err.println("writeRTFTable error:\n\t"+e.getMessage()+"\n\t"+e.getCause());
        }
	}
	public static void _writeRTFTable(OutputStream rtfOut, Object[][] data,String title,boolean start,boolean finish,
			String titleFont,Color titleFontColor,
			String headerFont,Color headerFontColor,Color headerCellColor,
			String dataFont,Color dataFontColor,Color dataCellColor)
    {
		if(titleFont==null)
			titleFont=DEFAULT_TITLE_FONT;
		if(headerFont==null)
			headerFont=DEFAULT_HEADER_FONT;
		if(dataFont==null)
			dataFont=DEFAULT_DATA_FONT;
		if(titleFontColor==null)
			titleFontColor=DEFAULT_TITLE_FONT_COLOR;
		if(headerFontColor==null)
			headerFontColor=DEFAULT_HEADER_FONT_COLOR;
		if(dataFontColor==null)
			dataFontColor=DEFAULT_DATA_FONT_COLOR;
		if(headerCellColor==null)
			headerCellColor=DEFAULT_HEADER_CELL_COLOR;
		if(dataCellColor==null)
			dataCellColor=DEFAULT_DATA_CELL_COLOR;
		final String rtfHeader="{\\rtf1\\ansi"+"\n"+"{\\fonttbl{\\f0 "+titleFont+";}{\\f1 "+headerFont+";}{\\f2 "+dataFont+";}}"+"\n"+
			"{\\colortbl;\\red255\\green255\\blue255;"+
				"\\red"+titleFontColor.getRed()+"\\green"+titleFontColor.getGreen()+"\\blue"+titleFontColor.getBlue()+";"+
				"\\red"+headerFontColor.getRed()+"\\green"+headerFontColor.getGreen()+"\\blue"+headerFontColor.getBlue()+";"+
				"\\red"+dataFontColor.getRed()+"\\green"+dataFontColor.getGreen()+"\\blue"+dataFontColor.getBlue()+";"+
				"\\red"+headerCellColor.getRed()+"\\green"+headerCellColor.getGreen()+"\\blue"+headerCellColor.getBlue()+";"+
				"\\red"+dataCellColor.getRed()+"\\green"+dataCellColor.getGreen()+"\\blue"+dataCellColor.getBlue()+";"+
				"}"+"\n\n";
		final String titlePrefix="\\f0\\cf2\\b ";
		final String titlePostfix="\\b0 \\"+"\n";
		final String headerRowPrefix="\\itap\\trowd \\trgraph \\trbrdrt\\brdrs\\brdrw10\\brdrcf0 \\trbrdrl\\brdrs\\brdrw10\\brdrcf0 \\trbrdrb\\brdrs\\brdrw10\\brdrcf0 \\trbrdrr\\brdrs\\brdrw10\\brdrcf0 \\trbrdrh\\brdrs\\brdrw10\\brdrcf0 \\trbrdrv\\brdrs\\brdrw10\\brdrcf0"+"\n";
		final String rowPostfix="\\row";
		final String headerCellDef="\\clvertalc \\clcbpat5 \\clbrdrt\\brdrs\\brdrw10\\brdrcf0 \\clbrdrl\\brdrs\\brdrw10\\brdrcf0 \\clbrdrb\\brdrs\\brdrw10\\brdrcf0 \\clbrdrr\\brdrs\\brdrw10\\brdrcf0 \\gaph\\cellx"+"\n";
		final String headerCellPrefix="\\intbl\\itap1\\qc\\cf3\\f1 ";
		final String cellPostfix="\\cell ";
		final String rowPrefix="\\itap1\\trow"+"\n";
		final String cellDef="\\clvertalt \\clcbpat6 \\clbrdrt\\brdrs\\brdrw10\\brdrcf0 \\clbrdrl\\brdrs\\brdrw10\\brdrcf0 \\clbrdrb\\brdrs\\brdrw10\\brdrcf0 \\clbrdrr\\brdrs\\brdrw10\\brdrcf0 \\gaph\\cellx";
		final String cellPrefix="\\intbl\\itap1\\ql\\cf4\\f2 ";
		final String tablePostfix="\\par";
		final String rtfFooter="}";
		BufferedWriter out=new BufferedWriter(new OutputStreamWriter(rtfOut));
		try
		{
			if(start)
			{
				out.write(rtfHeader);
			}
			out.write(titlePrefix+title+titlePostfix);//write title
			out.write(headerRowPrefix);//write the definition for the first row of the table
			for(int i=0;i<data[0].length;i++)
			{
				out.write(headerCellDef);//write the definition for each cell in the first row
			}
			out.write("\n");
			for(Object s:data[0])
			{
				out.write(headerCellPrefix+s.toString()+cellPostfix);//write each piece of data into the proper cell
			}
			out.write(rowPostfix);
			//
			for(int i=1;i<data.length;i++)
			{
				out.write(rowPrefix);//write the definition for each of the rest of the rows of the table
				for(int j=0;j<data[i].length;j++)
				{
					out.write(cellDef);//write the definition for each cell
				}
				for(Object s:data[i])
				{
					out.write(cellPrefix+s.toString()+cellPostfix);//write each piece of data into the proper cell
				}
				out.write(rowPostfix);
				out.write("\n");
			}
			out.write(tablePostfix);
			out.flush();
			if(finish)
			{
				out.write(rtfFooter);
				out.flush();
				out.close();
			}
		}
		catch(Exception e)
		{
			System.err.println("writeRTFTableException:\n\t"+e.getMessage()+"\n\t"+e.getCause());
			e.printStackTrace();
		}
    }
}
