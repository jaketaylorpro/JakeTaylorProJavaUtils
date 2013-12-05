package com.jtaylor.util.fileio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class HTMLOperations
{
	public static final String HTML="<html>";
	public static final String _HTML="</html>";
	public static final String HEAD="<head>";
	public static final String _HEAD="</head>";
	public static final String STYLE="<style>";
	public static final String _STYLE="</style>";
	public static final String TITLE="<title>";
	public static final String _TITLE="</title>";
	public static final String H1="<h1>";
	public static final String _H1="<h1>";
	public static final String BODY="<body>";
	public static final String _BODY="</body>";
	public static final String TABLE="<table>";
	public static final String _TABLE="</table>";
	public static final String TR="<tr>";
	public static final String _TR="</tr>";
	public static final String TD="<td>";
	public static final String _TD="</td>";
	public static final String DIV="<div>";
	public static final String _DIV="</div>";
	
	public static void main(String[] args)
	{
		writeTable("<p>test<p>","TABLE{border-spacing: 2px;background-color:black}P{color: gray;}",new String[][]{{"a",null,"c"},{"d","e","f"},{"g","h","i"}},new File("/Users/jtaylor/Desktop/test.html"),getDefaultTableCellStyles(3, 3, false));
		System.out.println("done");
	}
	
	public static void writeTable(String title,String style,String[][] data,File file,String[][] columnStyles)
	{
        try
        {
        	OutputStream htmlOut = new FileOutputStream(file);
	        writeHeader(title,style,htmlOut);
	        writeTable(data,htmlOut,columnStyles);
	        writeFooter(htmlOut);
	        htmlOut.flush();
	        htmlOut.close();
        }
        catch (Exception e)
        {
	        e.printStackTrace();
        }
	}
	public static String[][] getDefaultTableCellStyles(int rows, int columns,boolean horizontal)
	{
		String[][] cellStyles=new String[rows][columns];
		for(int i=0;i<cellStyles.length;i++)
		{
			for(int j=0;j<cellStyles[0].length;j++)
			{
				if((horizontal&&i==0)||(!horizontal&&j==0))
				{
					cellStyles[i][j]="background-color: dimgray; border-color: dimgray; border-style: solid; border-width: 10px; color: white; font-weight: bold;";
				}
				else
				{
					cellStyles[i][j]="background-color: lightgray; border-color: lightgray; border-style: solid; border-width: 10px; color: black;";
				}
			}
		}
		return cellStyles;
	}
	public static void writeTable(String[][] data,OutputStream htmlOut,String[][] cellStyles)
	{
		if(cellStyles==null)
		{
			cellStyles=getDefaultTableCellStyles(data.length,data[0].length,false);
		}
		writeTable(data,new PrintWriter(htmlOut),cellStyles);
	}
	public static void writeTable(String[][] data,PrintWriter htmlOut,String[][] cellStyles)
	{
		if(cellStyles==null)
		{
			cellStyles=getDefaultTableCellStyles(data.length,data[0].length,false);
		}
		htmlOut.write(TABLE);
		for(int i=0;i<data.length;i++)
        {
        	htmlOut.write(TR);
        	for(int j=0;j<data[0].length;j++)
        	{
        		htmlOut.write("<td style='"+cellStyles[i][j]+"'>");
        		htmlOut.write(DIV);
        		if(data[i][j]!=null)
        		{
        			htmlOut.write(data[i][j]);
        		}
        		htmlOut.write(_DIV);
        		htmlOut.write(_TD);
        	}
        	htmlOut.write(_TR);
        }
        htmlOut.write(_TABLE);
        htmlOut.flush();
	}
	public static void writeHeader(String title,String style,OutputStream htmlOut)
	{
		writeHeader(title,style,new PrintWriter(htmlOut));
	}
	public static void writeHeader(String title,String style,PrintWriter htmlOut)
	{
        htmlOut.write(HTML);
        htmlOut.write(HEAD);
        htmlOut.write(STYLE);
        if(style!=null)
        {
        	htmlOut.write(style);
        }
        htmlOut.write(_STYLE);
        if(title!=null)
        {
        	htmlOut.write(TITLE);
        	htmlOut.write(title);
        	htmlOut.write(_TITLE);
        }
        htmlOut.write(_HEAD);
        htmlOut.write(BODY);
        htmlOut.flush();
	}
	public static void writeFooter(OutputStream htmlOut)
	{
		writeFooter(new PrintWriter(htmlOut));
	}
	public static void writeFooter(PrintWriter htmlOut)
	{
		htmlOut.write(_BODY);
        htmlOut.write(_HTML);
        htmlOut.flush();
	}
}
