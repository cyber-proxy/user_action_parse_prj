package com.exam.useractionparse.data;

import java.util.HashMap;

import com.exam.useractionparse.utils.Utils;

public class UserActionDes {
	public static String sExcelPath = "";
	public static void parseExl() {
		Utils.consolePrint(sExcelPath);
		// ��EXCEL���ݽ�����actionDes���档
	}
	public static final HashMap<Integer, String> actionDes = new HashMap();
}
