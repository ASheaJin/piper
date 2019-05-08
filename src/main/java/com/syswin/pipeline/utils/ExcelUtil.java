package com.syswin.pipeline.utils;

import com.syswin.pipeline.service.exception.BusinessException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:lhz
 * @date:2019/2/18 10:12
 */
public class ExcelUtil {


	private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

	public static List<String> getExcelData(MultipartFile file, String publisherId, String basedir) {
		if (!checkFile(file)) {
			throw new BusinessException("上传文件不正确");
		}


		//获得Workbook工作薄对象
		Workbook workbook = getWorkBook(file, publisherId, basedir);
		//创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
		List<String> list = new ArrayList<String>();
		if (workbook != null) {
			//获得当前sheet工作表
			Sheet sheet = workbook.getSheetAt(0);
			//获得当前sheet的开始行
			int firstRowNum = sheet.getFirstRowNum();
			//获得当前sheet的结束行
			int lastRowNum = sheet.getLastRowNum();
			//循环除了第一行的所有行
			for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
				//获得当前行
				Row row = sheet.getRow(rowNum);
				if (sheet == null) {
					continue;
				}
				Cell cell = row.getCell(0);
				String userId = getCellValue(cell);
				if (!StringUtils.isNullOrEmpty(userId)) {
					list.add(getCellValue(cell));
				}
			}
		}
		return list;
	}


	/**
	 * 检查文件
	 *
	 * @param file
	 * @throws IOException
	 */
	public static boolean checkFile(MultipartFile file) {
		//判断文件是否存在
		if (null == file) {
			log.error("文件不存在！");
			return false;
		}
		//获得文件名
		String fileName = file.getOriginalFilename();
		//判断文件是否是excel文件
		if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
			log.error(fileName + "不是excel文件");
			return false;
		}
		return true;
	}

	public static Workbook getWorkBook(MultipartFile file, String publisherId, String basedir) {
		//获得文件名
		String fileName = file.getOriginalFilename();
		//创建Workbook工作薄对象，表示整个excel
		Workbook workbook = null;
		InputStream is = null;
		InputStream isSave = null;
		try {
			//获取excel文件的io流
			is = file.getInputStream();
			isSave = file.getInputStream();
			//存储文件
			File parent = new File(basedir + "/excels/" + publisherId);
			if (!parent.exists()) {
				parent.mkdirs();
			}
			String filePath = basedir + "/excels/" + publisherId + "/" + (System.currentTimeMillis() / 1000) + "_" + file.getOriginalFilename();
			File desFile = new File(filePath);
			try {
				OutputStream outStream = new FileOutputStream(desFile);
				byte[] buffer = new byte[isSave.available()];
				isSave.read(buffer);
				outStream.write(buffer);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				log.error("文件存储失败" + e.getMessage(), e);
			}

			//根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
			if (fileName.endsWith("xls")) {
				//2003
				workbook = new HSSFWorkbook(is);
			} else if (fileName.endsWith("xlsx")) {
				//2007 及2007以上
				workbook = new XSSFWorkbook(is);
			}

		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			try {
				is.close();
				isSave.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return workbook;
	}

	public static String getCellValue(Cell cell) {
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		}
		//判断数据的类型
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING: //字符串
				cellValue = String.valueOf(cell.getStringCellValue());
				break;
		}
		return cellValue;
	}
}
