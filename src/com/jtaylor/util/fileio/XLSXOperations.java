package com.jtaylor.util.fileio;

import com.jtaylor.util.datastructures.StructOperations;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class XLSXOperations
{
	public static String EXT_XLSX = ".xlsx";
	public static String EXT_XLS = ".xls";

	public static void writeExcelSheet(File workbookFile, Object[][] data) throws Exception
   {
      writeExcelSheet(workbookFile,data,null);
   }
	public static void writeExcelSheet(File workbookFile, Object[][] data,List<DVWrapper> validations) throws Exception
   {
		writeExcelSheet(workbookFile, 0, data,validations);
	}

	public static boolean rangeListContainsIndex(CellRangeAddressList rangeList, int row, int col)
	{
		CellRangeAddress address;
		for (int i = 0; i < rangeList.countRanges(); i++)
		{
			if (rangeContainsIndex(rangeList.getCellRangeAddress(i), row, col))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean rangeContainsIndex(CellRangeAddress range, int row, int col)
	{
		return row <= range.getLastRow() && row >= range.getFirstRow() && col <= range.getLastColumn() && col >= range.getFirstColumn();
	}

	public static int getRangeRightBound(CellRangeAddressList range)
	{

		CellRangeAddress address;
		int max = 0;
		for (int i = 0; i < range.countRanges(); i++)
		{
			address = range.getCellRangeAddress(i);
			if (address.getLastColumn() > max)
			{
				max = address.getLastColumn();
			}
		}
		return max;
	}

	public static int getRangeBottomBound(CellRangeAddressList range)
	{
		CellRangeAddress address;
		int max = 0;
		for (int i = 0; i < range.countRanges(); i++)
		{
			address = range.getCellRangeAddress(i);
			if (address.getLastRow() > max)
			{
				max = address.getLastRow();
			}
		}
		return max;
	}

	public static int max(int a, int b)
	{
		if (b > a)
		{
			return b;
		}
		else
		{
			return a;
		}
	}

	public static void writeExcelSheet(File workbookFile, int sheetIndex, Object data[][]) throws Exception
   {
      writeExcelSheet(workbookFile,sheetIndex,null,data,null);
   }
	public static void writeExcelSheet(File workbookFile, int sheetIndex, Object data[][],List<DVWrapper> validations) throws Exception
   {
		writeExcelSheet(workbookFile, sheetIndex, null, data,validations);
	}

	public static Workbook getWorkbook(FileInputStream excelReader, File workbookFile) throws Exception
	{
		if (workbookFile.getName().endsWith(".xls"))
		{
			return new HSSFWorkbook(excelReader);
		}
		else if (workbookFile.getName().endsWith(".xlsx"))
		{
			return new XSSFWorkbook(excelReader);
		}
		else
		{
			throw new Exception("bad file extension, must be xls or xlsx");
		}
	}

	public static Workbook getWorkbook(boolean xssf)
	{
		if (xssf)
		{
			return new XSSFWorkbook();
		}
		else
		{
			return new HSSFWorkbook();
		}
	}
   public enum DVType{FormulaList,ExplicitList}
   public static interface DVWrapper
   {
      public DVType getType();
      public int getSourceColumn();
      public int getTargetColumn();
      public List<String> getValueList();
   }
   public static class FormulaListDVWrapper implements DVWrapper
   {
      private int mySourceColumn;
      private int myTargetColumn;
      public FormulaListDVWrapper(int sourceColumn,int targetColumn)
      {
         mySourceColumn=sourceColumn;
         myTargetColumn=targetColumn;
      }
      public DVType getType()
      {
         return DVType.FormulaList;
      }

      public int getSourceColumn()
      {
         return mySourceColumn;
      }

      public int getTargetColumn()
      {
         return myTargetColumn;
      }
      public List<String> getValueList()
      {
         return null;
      }
   }
   public static class ExplicitListWrapper implements DVWrapper
   {
      private List<String> myValueList;
      private int myTargetColumn;
      public ExplicitListWrapper(int targetColumn,List<String> valueList)
      {
         myValueList=valueList;
         myTargetColumn=targetColumn;
      }
      public DVType getType()
      {
         return DVType.ExplicitList;
      }

      public int getSourceColumn()
      {
         return -2;
      }

      public int getTargetColumn()
      {
         return myTargetColumn;
      }

      public List<String> getValueList()
      {
         return myValueList;
      }
   }
	public static void writeExcelSheet(File workbookFile, int sheetIndex, String newSheetName, Object data[][]) throws Exception
   {
      writeExcelSheet(workbookFile,sheetIndex,newSheetName,data,null);
   }
	public static void writeExcelSheet(File workbookFile, int sheetIndex, String newSheetName, Object data[][],List<DVWrapper> validations) throws Exception
   {
		Workbook workbook;
		if (workbookFile.exists())
		{
			if (workbookFile.canRead())
			{
				if (workbookFile.canWrite())
				{
					FileInputStream excelReader = new FileInputStream(workbookFile);
               workbook = getWorkbook(excelReader, workbookFile);
               Sheet sheet;
               try
               {
                  sheet = workbook.getSheetAt(sheetIndex);
               }
               catch (Exception e)
               {
                  boolean done = false;
                  int tryNum = 0;
                  while (!done)
                  {//we keep trying until we get an exception, than we know we have a name that is not taken yet
                     try
                     {
                        if (tryNum == 0)
                        {
                           sheet = workbook.getSheet(newSheetName);
                           if (sheet == null)
                           {
                              throw new Exception("null");
                           }
                        }
                        else
                        {
                           sheet = workbook.getSheet(newSheetName + tryNum);
                           if (sheet == null)
                           {
                              throw new Exception("null");
                           }
                        }
                        tryNum++;
                     }
                     catch (Exception e1)
                     {
                        done = true;
                     }
                  }
                  sheet = workbook.createSheet(newSheetName + (tryNum == 0 ? "" : tryNum));
               }
               _writeDataToSheet(sheet, data);
               _writeValidationsToSheet(sheet, validations);
               excelReader.close();
				}
				else
				{
               throw new FileNotFoundException("no write permission: "+workbookFile);
				}
			}
			else
			{
            throw new FileNotFoundException("no read permission"+workbookFile);
			}
		}
		else
		{
			if (workbookFile.getAbsoluteFile().getParentFile().canWrite())
			{
				workbook = getWorkbook(workbookFile.getName().endsWith(EXT_XLSX));

				Sheet sheet;
				if (newSheetName == null)
				{
					sheet = workbook.createSheet();
				}
				else
				{
					sheet = workbook.createSheet(newSheetName);
				}
            _writeDataToSheet(sheet, data);
            _writeValidationsToSheet(sheet, validations);

			}
			else
			{
				System.out.println("cannot write new file: "+workbookFile);
				return;
			}
		}
		FileOutputStream excelWriter;
      workbookFile.createNewFile();
      excelWriter = new FileOutputStream(workbookFile);
      workbook.write(excelWriter);
      excelWriter.flush();
      excelWriter.close();
   }

	private static void _writeDataToSheet(Sheet sheet, Object[][] data)
	{
		Row row;
		Cell cell;
		for (int i = 0; i < data.length; i++)
		{
			if (!StructOperations.arrayIsEmpty(data[i]))
			{
				row = sheet.getRow(i);
				if (row == null)
				{
					row = sheet.createRow(i);
				}
				for (int j = 0; j < data[i].length; j++)
				{
					if (data[i][j] != null)
					{
						cell = row.getCell(j);
						if (cell == null)
						{
							cell = row.createCell(j);
						}
						if (data[i][j] != null)
						{
							_writeDataToCell(row.getCell(j), data[i][j]);
						}
					}
				}
			}
		}
	}

   private static void _writeValidationsToSheet(Sheet sheet,List<DVWrapper> validations)
   {
//      System.out.println("checking validations");
      if(validations!=null&&!validations.isEmpty())
      {
//         System.out.println("validations found");
         DataValidationHelper helper;
         if (sheet instanceof XSSFSheet)
         {
            helper=new XSSFDataValidationHelper((XSSFSheet)sheet);
         }
         else
         {
            helper=new HSSFDataValidationHelper((HSSFSheet)sheet);
         }
         for(DVWrapper wrapper:validations)
         {
            CellRangeAddressList targetRange = new CellRangeAddressList();
            DataValidation dv;
            targetRange.addCellRangeAddress(new CellRangeAddress(0, 10000, wrapper.getTargetColumn(), wrapper.getTargetColumn()));
            if(wrapper.getType()==DVType.FormulaList)
            {
               String sourceColumnLetter = CellReference.convertNumToColString(wrapper.getSourceColumn());
               String sourceRangeString = "$" + sourceColumnLetter + ":$" + sourceColumnLetter;
               dv=helper.createValidation(helper.createFormulaListConstraint(sourceRangeString),targetRange);
            }
            else if(wrapper.getType()==DVType.ExplicitList)
            {
               List<String> list=wrapper.getValueList();
               String[] array=new String[list.size()];
               for(int i=0;i<array.length;i++)
               {
                  array[i]=list.get(i);
               }
               dv=helper.createValidation(helper.createExplicitListConstraint(array),targetRange);
            }
            else
            {
               System.err.println("unknown validation wrapper: "+wrapper);
               dv=null;
            }
            sheet.addValidationData(dv);
         }
      }
      else
      {
         System.out.println("no validations found");
      }
   }

	private static void _writeDataToCell(Cell cell, Object object)
	{
		if (object == null)
		{
			String nulll = null;
			cell.setCellValue(nulll);
		}
		else if (object instanceof Integer)
		{
			cell.setCellValue((double) ((Integer) object).intValue());
		}
		else if (object instanceof Double)
		{
			cell.setCellValue(((Double) object).doubleValue());
		}
		else if (object instanceof Long)
		{
			cell.setCellValue(((Long) object).doubleValue());
		}
		else if (object instanceof Date)
		{
			cell.setCellValue((Date) object);
		}
		else if (object instanceof Boolean)
		{
			cell.setCellValue(((Boolean) object).booleanValue());
		}
		else
		{
			cell.setCellValue((cell instanceof HSSFCell ?
									 new HSSFRichTextString(object.toString()) :
									 new XSSFRichTextString(object.toString())));
		}
	}

	public static Object[][] readExcelSheet(File workbookFile) throws Exception
   {
		return readExcelSheet(workbookFile, 0);
	}

	public static Object[][] readExcelSheet(File workbookFile, int sheetIndex) throws Exception
   {
		FileInputStream excelReader = null;
		try
		{
			excelReader = new FileInputStream(workbookFile);
			Workbook workbook = getWorkbook(excelReader, workbookFile);
			Sheet worksheet = workbook.getSheetAt(sheetIndex);
			Row row;
			Cell cell;
			List<List<Object>> matrix = new Vector<List<Object>>();
			List<Object> rowCellValues;
			for (int i = 0; i <= worksheet.getLastRowNum(); i++)
			{
				row = worksheet.getRow(i);
				rowCellValues = new Vector<Object>();
				if (row != null)
				{
					for (int j = 0; j < row.getLastCellNum(); j++)
					{
						cell = row.getCell(j);
						rowCellValues.add(_interpretCell(cell));
					}
				}
				matrix.add(rowCellValues);
			}
			excelReader.close();
			return StructOperations.listOfListsToMatrix(matrix);
		}
		catch (Exception e)
		{
			try
			{
				if (excelReader != null)
				{
					excelReader.close();
				}
			}
			catch (Exception e1)
			{
			}
         throw new Exception("there was an error reading the excelsheet: "+workbookFile+":"+sheetIndex);
		}
	}

	private static Object _interpretCell(Cell cell)
	{
		if (cell == null)
		{
			return null;
		}
		else
		{
			if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
			{
				return null;
			}
			else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
			{
				return cell.getBooleanCellValue();
			}
			else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)//dates are numeric
			{
				if (HSSFDateUtil.isCellDateFormatted(cell))
				{
					return cell.getDateCellValue();
				}
				else
				{
					double numericValue = cell.getNumericCellValue();
					if (numericValue < Integer.MAX_VALUE &&
						 Double.compare(numericValue, (double) (int) numericValue) == 0)
					{
						return (int) numericValue;
					}
					else if (numericValue < Long.MAX_VALUE &&
								Double.compare(numericValue, (double) (long) numericValue) == 0)
					{
						return (long) numericValue;
					}
					else
					{
						return numericValue;
					}
				}
			}
			else// if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING)
			{
				return cell.getRichStringCellValue().getString();
			}
		}
	}

	public static void main(String[] args)
	{
		try
		{
			Object[][] data = {{"a", "b", "c"}, {"d", "e", "f"}, {new Date(), "h", "i"}};
			File file = new File("/Users/jtaylor/Desktop/file.xlsx");
			file.delete();
//         List<String> values=new Vector<String>();
//         for(int i=0;i<100;i++)
//         {
//            values.add("value"+i);
//         }
			writeExcelSheet(file, data,new Vector<DVWrapper>(Arrays.asList(new FormulaListDVWrapper(2,3))));
//			DVConstraint constraint=DVConstraint.createExplicitListConstraint(new String[]{"one","two"});
//			DVConstraint constraint=DVConstraint.createFormulaListConstraint("$C:$C");
//			CellRangeAddressList range=new CellRangeAddressList()f;dds
//			range.addCellRangeAddress(new CellRangeAddress(0,-1,3,3));
//			range.addCellRangeAddress(CellRangeAddress.valueOf("D:D"));kkjjssd
//			writeDVToXLS(file,new HSSFDataValidation(range,constraint));
			System.out.println("done");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
