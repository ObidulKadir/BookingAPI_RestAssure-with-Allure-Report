package api.utilities;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.*;
import java.io.*;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;

public class XLUtility {

	public FileInputStream fi;
	public FileOutputStream fo;
	public XSSFWorkbook workbook;
	public XSSFSheet sheet;
	public XSSFRow row;
	public XSSFCell cell;
	public CellStyle style;
	String path;

	public XLUtility(String path) {
		this.path = path;
	}

	public int getRowCount(String sheetName) throws IOException {

		fi = new FileInputStream(path);
		workbook = new XSSFWorkbook(fi);
		sheet = workbook.getSheet(sheetName);
		int rowCount = sheet.getLastRowNum();
		workbook.close();
		fi.close();
		return rowCount;
	}

	public int getCellCount(String sheetName, int rowNum) throws IOException {
		fi = new FileInputStream(path);
		workbook = new XSSFWorkbook(fi);
		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(rowNum);
		int cellCount = row.getLastCellNum();
		workbook.close();
		fi.close();
		return cellCount;

	}

	public String getCellData(String sheetName, int rowNum, int colNum) throws IOException {
		
		fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rowNum);
        cell = row.getCell(colNum);
        
        DataFormatter dataFormatter = new DataFormatter();
        
        String data;
        try {
        	 data = dataFormatter.formatCellValue(cell); // returns the formated value of a cell as string regardless 
        } catch (Exception e) {
        	data = "";
        }
        
        workbook.close();
        fi.close();
        return data;
	}
	

    // Method to write data to a specific cell
	public void setCellData(String sheetName, int rownum, int column, String data) throws IOException {

	    File xlfile = new File(path);

	    // If file not exists then create new file
	    if (!xlfile.exists()) {
	        workbook = new XSSFWorkbook();
	        fo = new FileOutputStream(path);
	        workbook.write(fo);
	    }

	    fi = new FileInputStream(path);
	    workbook = new XSSFWorkbook(fi);

	    // If sheet not exists then create new sheet
	    if (workbook.getSheetIndex(sheetName) == -1)
	        workbook.createSheet(sheetName);

	    sheet = workbook.getSheet(sheetName);

	    // If row not exists then create new row
	    if (sheet.getRow(rownum) == null)
	        sheet.createRow(rownum);

	    row = sheet.getRow(rownum);

	    cell = row.createCell(column);
	    cell.setCellValue(data);

	    fo = new FileOutputStream(path);
	    workbook.write(fo);

	    workbook.close();
	    fi.close();
	    fo.close();
	}
	
	// Fill cell with Green color
	public void fillGreenColor(String sheetName, int rownum, int column) throws IOException {

	    fi = new FileInputStream(path);
	    workbook = new XSSFWorkbook(fi);
	    sheet = workbook.getSheet(sheetName);

	    row = sheet.getRow(rownum);
	    cell = row.getCell(column);

	    style = workbook.createCellStyle();
	    style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	    cell.setCellStyle(style);

	    fi.close();

	    fo = new FileOutputStream(path);
	    workbook.write(fo);
	    workbook.close();
	    fo.close();
	}


	// Fill cell with Red color
	public void fillRedColor(String sheetName, int rownum, int column) throws IOException {

	    fi = new FileInputStream(path);
	    workbook = new XSSFWorkbook(fi);
	    sheet = workbook.getSheet(sheetName);

	    row = sheet.getRow(rownum);
	    cell = row.getCell(column);

	    style = workbook.createCellStyle();
	    style.setFillForegroundColor(IndexedColors.RED.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	    cell.setCellStyle(style);

	    fi.close();

	    fo = new FileOutputStream(path);
	    workbook.write(fo);
	    workbook.close();
	    fo.close();
	}
}