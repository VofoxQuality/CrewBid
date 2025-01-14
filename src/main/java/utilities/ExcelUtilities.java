package utilities;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtilities {
	public XSSFWorkbook w;
	public  XSSFSheet sh;
	public  FileInputStream f;
	public void setExcelFile() throws IOException{
		
		String file =  System.getProperty("user.dir")+"\\Excelfiles\\QA Enviornment.xlsx";
		f=new FileInputStream(file);
		// creating workbook instance that refers to .xls file
				w = new XSSFWorkbook(f);
				// creating a Sheet object to retrieve the object
				sh = w.getSheet("qa");
			}
			
			public String readData(int row, int column)
			{
				Row r = sh.getRow(row);
				Cell c = r.getCell(column);
				// get the cell type
				CellType type = c.getCellType();
				switch (type) {
				case STRING: {
					return c.getStringCellValue();
				}
				case NUMERIC: {
					int a = (int) c.getNumericCellValue();
					return String.valueOf(a);
				}
				default:
					break;
				}
				return null;
			}
		}
