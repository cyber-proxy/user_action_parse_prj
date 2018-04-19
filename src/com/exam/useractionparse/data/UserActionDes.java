package com.exam.useractionparse.data;

import java.io.File;
import java.util.HashMap;

import com.exam.useractionparse.cfg.ConstantValue;
import com.exam.useractionparse.utils.Log;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class UserActionDes {
	public static final HashMap<Integer, String> ACTION_DES_MAP = new HashMap();
	public static String sExcelPath = "";

	// 把EXCEL内容解析到actionDes里面。
	public static void parseExl() {
		Log.consolePrint("解析Excel文件：" + sExcelPath + "\n");
		int column_id = 0;
		int column_des = 1;
		int column_value = 2;
		Sheet sheet;
		Workbook book;
		try {
			book = Workbook.getWorkbook(new File(sExcelPath));
			sheet = book.getSheet(0);
			Cell[] cells = sheet.getRow(0);
			for (int column = 0; column < cells.length; column++) {
				if (cells[column].getContents().equalsIgnoreCase(ConstantValue.ACTION_DES_COLUMN)) {
					column_des = column;
				} else if (cells[column].getContents().equalsIgnoreCase(ConstantValue.ACTION_ID_COLUMN)) {
					column_id = column;
				} else if (cells[column].getContents().equalsIgnoreCase(ConstantValue.ACTION_VALUE_COLUMN)) {
					column_value = column;
				}
			}
			int rowCounts = sheet.getRows();
			for (int row = 1; row < rowCounts - 1; row++) {
				String deString = sheet.getCell(column_des, row).getContents();
				String idString = sheet.getCell(column_id, row).getContents();
				String valueString = sheet.getCell(column_value, row).getContents();
				// Utils.consolePrint(deString + " " + idString + " "+
				// valueString +" \n");
				try {
					ACTION_DES_MAP.put(Integer.valueOf(valueString), deString);
				} catch (Exception e) {
					Log.printError("error format: " + valueString);
				}
			}
			book.close();
		} catch (Exception e) {
			System.out.println("标题0：" + e + "");
		}
	}
}
