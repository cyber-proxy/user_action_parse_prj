package com.exam.useractionparse.utils;

import java.awt.print.Printable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.exam.useractionparse.cfg.Config;
import com.exam.useractionparse.data.NewUserAction;
import com.exam.useractionparse.data.UserActionDes;
import com.exam.useractionparse.main.UserActionParser;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class DesUtils {

	private final static String PREFIX = "int";
	private final static String ASIGN = "=";
	private final static String delimiter = ";";
	public static int MaxNum = 73;

	public static StringBuilder resultFileName = new StringBuilder();
	public static FileWriter resultFileHandle = null;

	/**
	 * 解析NewUserActionType.java文件
	 * 
	 * @param classFilePath
	 * @return
	 */
	public static Map<String, String> getActionDes(String classFilePath) {

		Map<String, String> actionDes = new HashMap<>();
		File file = new File(classFilePath);
		// if(file.getName().substring(file.getName().lastIndexOf(".") + 1) ==
		// "xls"||file.getName().substring(file.getName().lastIndexOf(".") + 1)
		// == "xlsx") {

		// }else {
		BufferedReader reader = null;
		try {
			if (!file.isDirectory()) {
				reader = new BufferedReader(new FileReader(classFilePath));
				String readLine = null;
				while ((readLine = reader.readLine()) != null) {
					// consolePrint(readLine);
					readLine = readLine.replaceAll(" ", "");
					if (readLine.contains(PREFIX)) {
						String intStatement = readLine.split(PREFIX)[1];
						String name = intStatement.split(ASIGN)[0];
						name = name.toLowerCase();
						String valueInt = intStatement.split(ASIGN)[1];
						valueInt = valueInt.split(delimiter)[0];
						if (NumberUtils.isNumeric2(valueInt)) {
							actionDes.put(valueInt, name);
							if (Integer.valueOf(valueInt) > MaxNum) {
								MaxNum = Integer.valueOf(valueInt);
							}
						}
					}
				}
			} else {
				Log.consolePrint("not a file\n");
			}
		} catch (Exception e) {
			Log.consolePrint(e.getMessage().toString() + "\n");
			return null;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (Map.Entry<String, String> entry : actionDes.entrySet()) {

				// consolePrintln("Key=" + entry.getKey() + "Value =" +
				// entry.getValue());

			}
			// }
		}

		return actionDes;
	}

	public static boolean isValidActionType(String actionType) {
		if (!Config.chekValid) {
			return true;
		}
		if (NumberUtils.isNumeric(actionType)) {
			if (NewUserAction.ACTION_DES_MAP.containsKey(Integer.valueOf(actionType))) {
				return true;
			} else {
				if (Config.printInvalidValue) {
					Log.print("not contain :" + actionType);
				}
			}
		} else {
			Log.print("not numeric :" + actionType);
		}
		return false;
	}

	public static String getDescrib(String actionType) {
		HashMap<Integer, String> actionMap = NewUserAction.ACTION_DES_MAP;
		if (UserActionDes.ACTION_DES_MAP.size() > 0) {
			actionMap = UserActionDes.ACTION_DES_MAP;
		}
		if (actionType.equals("none")) {
			return "*没有操作*";
		}
		if (!NumberUtils.isNumeric(actionType)) {
			return actionType;
		}
		if (NewUserAction.ACTION_DES_MAP.containsKey(Integer.valueOf(actionType))) {
			return NewUserAction.ACTION_DES_MAP.get(Integer.valueOf(actionType));
		} else {
			return actionType;
		}
	}

	/**
	 * 判断是否合法","分割的数字串。
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isValidActionList(String str) {
		Pattern pattern = Pattern.compile("(/d{1,}/,){0,}");
		if (pattern.matcher(str).matches()) {
			return true;
		}
		return false;
	}

	public static String getArrayStr(String[] array) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : array) {
			stringBuilder.append(string + " ");
		}
		return stringBuilder.toString();
	}

	public static int getValueOfMap(Map<String, Integer> map, String key) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			return 0;
		}
	}

	public static int getValueOfMap(Map<Integer, Integer> map, Integer key) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			return 0;
		}
	}

	public static void showHelp() {
		Log.printError("Error messages:pls input valid parameters.\n");
		Log.consolePrint("HELP:\n解析从移动端上传的用户行为日志。\n" + "输入：1）行为集合日志（可能有多个）；2）行为（描述）和对应值映射表（EXCEL表格）。\n" + "输出：解析结果。\n"
				+ "Usages:\n"
				+ "java -jar UserActionParser -l(log file) \"行为日志文件1完整路径\" \"行为日志文件2完整路径\"  -e(excel file) \"行为（描述）和其值映射表完整路径\"\n"
				+ "E.g:\n"
				+ "\t-l \"F:\\user_path\\user_path_s2.log.2017-05-02(1)\" \"F:\\user_path\\user_path_s2.log.2017-05-02(2)\" -e \"F:\\user_path\\user_path.log.2017-05-02.xls\"\n"
				+ "\t-unistall \"F:\\user_path\\user_path_s2.log.2017-05-02\" \"F:\\user_path\\user_path.log.2017-05-02\"\n"
				+ "附注：行为日志文件可能会存在多个（因为之前某一天的日志都是分做两个文件的）。 可选“-f(filter) \"过滤文件路径\"”， “-u(uninstall) \"已经卸载的日志文件路径\")”");
		System.exit(0);
	}

	public static void openLogFile() {
		try {
			DesUtils.resultFileHandle.flush();
			DesUtils.resultFileHandle.close();

			Runtime runtime = Runtime.getRuntime();
			runtime.exec("cmd /c start notepad++ " + DesUtils.resultFileName.toString());
		} catch (Exception e) {
			try {
				DesUtils.resultFileHandle.flush();
				DesUtils.resultFileHandle.close();

				Runtime runtime = Runtime.getRuntime();
				runtime.exec("cmd /c start notepad " + DesUtils.resultFileName.toString());
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}

	}

}
