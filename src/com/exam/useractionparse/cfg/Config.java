package com.exam.useractionparse.cfg;

public class Config {
	// 打印开关。
	public static final boolean enablePathParse = true;

	public static boolean printInvalidValue = false;

	public static boolean printDebugMsg = false;

	public static boolean printColumn = false;

	public static boolean printRedundancy = false;
	
	// 是否检测行为列表的可解析性。
	public static boolean chekValid = true;
	
	// 配置解析详细参数。
	public static final int MAX_USER_ACTION_STEP = 200;

	public static final int MAX_USER_ACTION_TYPE = 100;

	public static final String EXIT_TAG = "none";

	public static final double THRESHOLD = 0.01;

}
