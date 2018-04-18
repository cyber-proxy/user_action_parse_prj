package com.exam.useractionparse.data;

import java.util.HashMap;

import com.exam.useractionparse.utils.Utils;

public class UserActionDes {
	public static String sExcelPath = "";
	public static void parseExl() {
		Utils.consolePrint(sExcelPath);
		// 把EXCEL内容解析到actionDes里面。
	}
	public static final HashMap<Integer, String> actionDes = new HashMap();
}
