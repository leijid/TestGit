/**
 * leijid
 *
 * FileUtil.java
 *
 * 2014年9月16日
 */
package com.self.util.tool;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.self.util.common.Common;

/**
 * @author leijid
 * 
 */
public class FileUtil {

	private static final Logger logger = Logger.getLogger(FileUtil.class);

	public static void newFile(String filePath) throws Exception {
		File file = new File(filePath);
		if (!file.exists()) {
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error("创建文件失败: " + e);
				throw e;
			}
		}
	}

	public static void writeFile(File file, String contents) throws Exception {
		if (StringUtil.isEmpty(contents)) {
			return;
		}
		PrintStream print = null;
		try {
			print = new PrintStream(new BufferedOutputStream(
			        new FileOutputStream(file)), false, Common.ENCODING);
			print.println(contents);
			print.flush();
		} catch (FileNotFoundException e) {
			logger.error("文件不存在: " + e);
			throw e;
		} catch (UnsupportedEncodingException e) {
			logger.error("字符集编码错误: " + e);
			throw e;
		} finally {
			if (print != null) {
				print.close();
			}
		}
	}

	public static String readFile(File file) throws Exception {
		String result = null;
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			byte[] buf = new byte[1024];
			inputStream.read(buf);
			result = new String(buf, Common.ENCODING);
		} catch (FileNotFoundException e) {
			logger.error("未找到文件: " + e);
			throw e;
		} catch (UnsupportedEncodingException e) {
			logger.error("字符集编码错误: " + e);
			throw e;
		} catch (IOException e) {
			logger.error("输入流读取错误: " + e);
			throw e;
		} finally {
			closeStream(inputStream, null);
		}
		return result;
	}

	public static void deleteFiile(File file) {
		if (file.exists()) {
			file.delete();
		}
	}

	public static void importPDFFile(File file, String contents)
	        throws Exception {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(file));
			BaseFont chinese = BaseFont.createFont("STSong-Light",
			        "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			Font font = new Font(chinese, 14, Font.NORMAL);
			document.open();
			document.add(new Paragraph(contents, font));
		} catch (DocumentException e) {
			logger.error("生成PDF文件失败: " + e);
			throw e;
		} catch (FileNotFoundException e) {
			logger.error("找不到文件: " + e);
			throw e;
		} finally {
			document.close();
		}
	}

	public static <T extends Object> void importExcelFile(File file,
	        List<ArrayList<T>> dataSets, String[] sheetNames) throws Exception {
		FileOutputStream outputStream = null;
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			outputStream = new FileOutputStream(file);
			for (int i = 0; i < dataSets.size(); i++) {
				XSSFSheet sheet = workbook.createSheet(String.valueOf(i));
				workbook.setSheetName(i, sheetNames[i]);
				ArrayList<T> dataSet = dataSets.get(i);
				XSSFRow row = sheet.createRow(0);
				for (int j = 0; j < dataSet.size(); j++) {
					T t = dataSet.get(j);
					Field[] fields = writeExcelTitles(row, t, j);
					writeExcelContent(sheet, fields, t, j);
				}
			}
			workbook.write(outputStream);
		} catch (FileNotFoundException e) {
			logger.error("文件不存在: " + e);
			throw e;
		} catch (IOException e) {
			logger.error("导出Excel文件异常: " + e);
			throw e;
		} catch (Exception e) {
			logger.error("类反射异常: " + e);
			throw e;
		} finally {
			closeStream(null, outputStream);
		}
	}

	private static <T> void writeExcelContent(XSSFSheet sheet, Field[] fields,
	        T t, int j) throws Exception {
		XSSFCell cell = null;
		XSSFRow row = sheet.createRow(j + 1);
		String getFieldName = null;
		Method getMethod = null;
		Object content = null;
		for (int col = 0; col < fields.length; col++) {
			getFieldName = ClassUtil.getFieldMethod(fields[col].getName());
			getMethod = ClassUtil.getMethod(t, getFieldName);
			content = ClassUtil.invokeMethod(getMethod, t);
			cell = row.createCell(col);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(content.toString());
		}
	}

	private static <T> Field[] writeExcelTitles(XSSFRow row, T t, int j) {
		XSSFCell cell;
		Field[] fields;
		fields = t.getClass().getDeclaredFields();
		if (j == 0) {
			for (int col = 0; col < fields.length; col++) {
				cell = row.createCell(col);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(fields[col].getName());
			}
		}
		return fields;
	}

	public static Map<String, String[]> readExcelTitle(File file)
	        throws Exception {
		String[] titles = null;
		FileInputStream inputStream = null;
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		XSSFRow row = null;
		Map<String, String[]> sheetMap = new HashMap<String, String[]>();
		try {
			inputStream = new FileInputStream(file);
			workbook = new XSSFWorkbook(inputStream);
			int numberOfSheets = workbook.getNumberOfSheets();
			for (int i = 0; i < numberOfSheets; i++) {
				sheet = workbook.getSheetAt(i);
				row = sheet.getRow(0);
				int cellNum = row.getPhysicalNumberOfCells();
				titles = new String[cellNum];
				for (int j = 0; j < cellNum; j++) {
					titles[j] = getCellFormatValue(row.getCell(j));
				}
				String sheetName = sheet.getSheetName();
				sheetMap.put(sheetName, titles);
			}
		} catch (FileNotFoundException e) {
			logger.error("找不到文件: " + e);
			throw e;
		} catch (IOException e) {
			logger.error("读取Excel文件异常: " + e);
			throw e;
		} finally {
			closeStream(inputStream, null);
		}
		return sheetMap;
	}

	public static Map<String, Integer> getExcelRowNum(File file)
	        throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		FileInputStream inputStream = null;
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		try {
			inputStream = new FileInputStream(file);
			workbook = new XSSFWorkbook(inputStream);
			int numberOfSheets = workbook.getNumberOfSheets();
			for (int i = 0; i < numberOfSheets; i++) {
				sheet = workbook.getSheetAt(i);
				int rowNum = sheet.getLastRowNum();
				String sheetName = sheet.getSheetName();
				map.put(sheetName, rowNum);
			}
		} catch (FileNotFoundException e) {
			logger.error("找不到文件: " + e);
			throw e;
		} catch (IOException e) {
			logger.error("读取Excel文件异常: " + e);
			throw e;
		} finally {
			closeStream(inputStream, null);
		}
		return map;
	}

	private static void closeStream(InputStream inputStream,
	        OutputStream outputStream) throws IOException {
		if (inputStream != null) {
			inputStream.close();
		}
		if (outputStream != null) {
			outputStream.close();
		}
	}

	private static String getCellFormatValue(XSSFCell cell) {
		String cellvalue = "";
		if (cell != null) {
			switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_NUMERIC:
				case XSSFCell.CELL_TYPE_FORMULA: {
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						Date date = cell.getDateCellValue();
						cellvalue = DateUtil.dateToString(date, "yyyy-MM-dd");
					} else {
						cellvalue = String.valueOf(cell.getNumericCellValue());
					}
					break;
				}
				case XSSFCell.CELL_TYPE_STRING:
					cellvalue = cell.getRichStringCellValue().getString();
					break;
				default:
					cellvalue = " ";
			}
		}
		return cellvalue;
	}

	public static void main(String[] args) throws Exception {
		// ArrayList<Person> persons = new ArrayList<Person>();
		// for (int i = 0; i < 65535; i++) {
		// Person person = new Person();
		// person.setId(i);
		// person.setName("test" + i);
		// person.setSex("man");
		// persons.add(person);
		// }
		//
		// List<ArrayList<Person>> dataSets = new
		// ArrayList<ArrayList<Person>>();
		// dataSets.add(persons);
		// String[] sheetNames = { "person" };
		// File file = new File("D:/excel1.xls");
		// importExcelFile(file, dataSets, sheetNames);
		// File file = new File("D:/test1.xlsx");
		// List<String[]> list = readExcelTitle(file);
		// for (int i = 0; i < list.size(); i++) {
		// String[] titles = list.get(i);
		// for (int j = 0; j < titles.length; j++) {
		// System.out.print(titles[j] + "\t");
		// }
		// System.out.print("\n");
		// }
	}
}
