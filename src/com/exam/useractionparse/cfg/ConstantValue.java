package com.exam.useractionparse.cfg;

import java.text.DecimalFormat;

public class ConstantValue {
	public static final String DELIMITER = ",";
	public static final String PATH_PREFIX = "path=";
	public static final String ANDROID_ID_REFIX = "android_id=";
	public static final String ANDORID_ID_POSTFIX = "&spend_time=";
	
	public static final String PATH = "E:\\AndroidProject\\user_action_parse_prj\\excel.xls";
	
	public static final String ACTION_ID_COLUMN = "ID";
	public static final String ACTION_DES_COLUMN = "DES";
	public static final String ACTION_VALUE_COLUMN = "VALUE";
	public static final DecimalFormat DECIMAL_FORMAT_2 = new DecimalFormat("0.00");
	public static final DecimalFormat DECIMAL_FORMAT_3 = new DecimalFormat("0.000");

}
