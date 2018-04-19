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
		// ��EXCEL���ݽ�����actionDes���档
		int i;
		Sheet sheet;
		Workbook book;
		Cell cell1, cell2, cell3;
		try {
			// hello.xlsΪҪ��ȡ��excel�ļ���
			book = Workbook.getWorkbook(new File(sExcelPath));
			// System.out.println("����1��"+book + "");
			// ��õ�һ�����������(ecxel��sheet�ı�Ŵ�0��ʼ,0,1,2,3,....)
			sheet = book.getSheet(0);
			// ��ȡ���Ͻǵĵ�Ԫ��
			cell1 = sheet.getCell(0, 0);

			// System.out.println("����2��"+sheet + "");
			i = 1;
			while (true) {
				// ��ȡÿһ�еĵ�Ԫ��
				cell1 = sheet.getCell(0, i);// ���У��У�
				cell2 = sheet.getCell(1, i);
				cell3 = sheet.getCell(2, i);
//				System.out.println("����1��"+cell1.getContents() + "");
//				System.out.println("����2��"+cell2.getContents() + "");
//				System.out.println("����3��"+cell3.getContents() + "");
				
				// System.out.println("����һ������"+cell1.getContents());
				// System.out.println("�ⲻ��һ������"+cell2.getContents());
				if (" ".equals(cell1.getContents()) == true) // �����ȡ������Ϊ��
					break;
				// actionDes.put(cell1.getContents(),
				// cell2.getContents());
				actionDes.put(Integer.valueOf(cell3.getContents()), cell2.getContents());
				i++;
			}
			book.close();
		} catch (Exception e) {
			System.out.println("����0��" + e + "");
		}
	}
	public static final HashMap<Integer, String> actionDes = new HashMap();
}
