package com.exam.useractionparse.data;

import java.io.File;
import java.util.HashMap;

import com.exam.useractionparse.utils.Utils;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class UserActionDes {
	public static String sExcelPath = "";
	public static void parseExl() {
		Utils.consolePrint(sExcelPath);
		// 把EXCEL内容解析到actionDes里面。
		int i;
		Sheet sheet;
		Workbook book;
		Cell cell1, cell2, cell3;
		try {
			// hello.xls为要读取的excel文件名
			book = Workbook.getWorkbook(new File(sExcelPath));
			// System.out.println("标题1："+book + "");
			// 获得第一个工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)
			sheet = book.getSheet(0);
			// 获取左上角的单元格
			cell1 = sheet.getCell(0, 0);

			// System.out.println("标题2："+sheet + "");
			i = 1;
			while (true) {
				// 获取每一行的单元格
				cell1 = sheet.getCell(0, i);// （列，行）
				cell2 = sheet.getCell(1, i);
				cell3 = sheet.getCell(2, i);
//				System.out.println("标题1："+cell1.getContents() + "");
//				System.out.println("标题2："+cell2.getContents() + "");
//				System.out.println("标题3："+cell3.getContents() + "");
				
				// System.out.println("这是一个标题"+cell1.getContents());
				// System.out.println("这不是一个标题"+cell2.getContents());
				if (" ".equals(cell1.getContents()) == true) // 如果读取的数据为空
					break;
				// actionDes.put(cell1.getContents(),
				// cell2.getContents());
				actionDes.put(Integer.valueOf(cell3.getContents()), cell2.getContents());
				i++;
			}
			book.close();
		} catch (Exception e) {
			System.out.println("标题0：" + e + "");
		}
	}
	public static final HashMap<Integer, String> actionDes = new HashMap();
}
