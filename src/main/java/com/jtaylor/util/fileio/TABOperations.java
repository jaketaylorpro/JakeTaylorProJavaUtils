package com.jtaylor.util.fileio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class TABOperations
{
	public static void writeTabDelineatedFile(String[][] data,File file)
	{
		try
        {
	        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
	        for(String[] dataRow:data)
	        {
	        	for(String dataItem:dataRow)
	        	{
	        		writer.write(dataItem+"\t");
	        	}
        		writer.write("\n");
	        }
	        writer.flush();
	        writer.close();
        }
        catch (FileNotFoundException e)
        {
	        e.printStackTrace();
        }
        catch (IOException e)
        {
	        e.printStackTrace();
        }
	}
}
