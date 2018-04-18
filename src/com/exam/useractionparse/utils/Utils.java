package com.exam.useractionparse.utils;

import java.awt.print.Printable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exam.useractionparse.cfg.Config;
import com.exam.useractionparse.cfg.ConstantValue;
import com.exam.useractionparse.data.NewUserAction;
import com.exam.useractionparse.main.UserActionParser;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class Utils {

	public static final DecimalFormat DECIMAL_FORMAT_2 = new DecimalFormat("0.00");
	public static final DecimalFormat DECIMAL_FORMAT_3 = new DecimalFormat("0.000");

	private final static String PREFIX = "int";
	private final static String ASIGN = "=";
	private final static String delimiter = ";";
	public static int MaxNum = 73;

	public static StringBuilder resultFileName = new StringBuilder();
	public static FileWriter resultFileHandle = null;

	public static Comparator strKeyComparator = new Comparator<Map.Entry<String, Integer>>() {

		@Override
		public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
			return o2.getValue().compareTo(o1.getValue());
		}
	};

	public static Comparator intKeyComparator = new Comparator<Map.Entry<Integer, Integer>>() {

		@Override
		public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
			return o2.getValue().compareTo(o1.getValue());
		}
	};

	/**
	 * ����NewUserActionType.java�ļ�
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
						if (isNumeric2(valueInt)) {
							actionDes.put(valueInt, name);
							if (Integer.valueOf(valueInt) > MaxNum) {
								MaxNum = Integer.valueOf(valueInt);
							}
						}
					}
				}
			} else {
				consolePrint("not a file\n");
			}
		} catch (Exception e) {
			consolePrint(e.getMessage().toString() + "\n");
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
		if (isNumeric(actionType)) {
			if (NewUserAction.actionDes.containsKey(Integer.valueOf(actionType))) {
				return true;
			} else {
				if (Config.printInvalidValue) {
					Utils.print("not contain :" + actionType);
				}
			}
		} else {
			Utils.print("not numeric :" + actionType);
		}
		return false;
	}

	public static String getDescrib(String actionType) {
		if (actionType.equals("none")) { return "*û�в���*"; } if
		  (!isNumeric(actionType)) { return actionType; } if
		  (NewUserAction.actionDes.containsKey(Integer.valueOf(actionType))) {
		  return NewUserAction.actionDes.get(Integer.valueOf(actionType));
		  }else { return actionType; }
	}

/*	private void parseExcel() {
		String path = "";

		String fileName;
			// System.out.println(fileName.substring(fileName.lastIndexOf(".") +
			// 1));
			if (fileName == null) {
				Utils.showHelp();
				break;
			} else if (fileName != null && fileName.substring(fileName.lastIndexOf(".") + 1).equals("xls")) {
				// System.out.println("��������");
				int i;
				path = fileName;
				Sheet sheet;
				Workbook book;
				Cell cell1, cell2, cell3;
				try {
					// hello.xlsΪҪ��ȡ��excel�ļ���
					book = Workbook.getWorkbook(new File(fileName));
					// System.out.println("����1��"+book + "");
					// ��õ�һ�����������(ecxel��sheet�ı�Ŵ�0��ʼ,0,1,2,3,....)
					sheet = book.getSheet(0);
					// ��ȡ���Ͻǵĵ�Ԫ��
					cell1 = sheet.getCell(0, 0);

					// System.out.println("����2��"+sheet + "");
					i = 0;
					while (true) {
						// ��ȡÿһ�еĵ�Ԫ��
						cell1 = sheet.getCell(0, i);// ���У��У�
						cell2 = sheet.getCell(1, i);
						cell3 = sheet.getCell(2, i);
						// System.out.println("����1��"+cell1.getContents() + "");
						// System.out.println("����2��"+cell2.getContents() + "");
						// System.out.println("����3��"+cell3.getContents() + "");
						if (actionType.equals(cell3.getContents())) {
							return cell2.getContents();
						}

						// System.out.println("����һ������"+cell1.getContents());
						// System.out.println("�ⲻ��һ������"+cell2.getContents());
						if (" ".equals(cell1.getContents()) == true) // �����ȡ������Ϊ��
							break;
						// actionDes.put(cell1.getContents(),
						// cell2.getContents());
						i++;
					}
					book.close();
				} catch (Exception e) {
					System.out.println("����0��" + e + "");
				}
				break;
			}

	}
	
*/	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static boolean isNumeric2(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * �ж��Ƿ�Ϸ�","�ָ�����ִ���
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

	public static void printTable(ArrayList<Map<String, Integer>> columns) {
		consolePrint("------------- start -------------------\n");
		for (Map<String, Integer> map : columns) {
			Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Integer> entry = iterator.next();
				// consolePrint("("+entry.getKey()+","+entry.getValue()+") ");
			}
			// consolePrint("\n");
		}
		consolePrint("------------- end -------------------\n");
	}

	public static void printTableListArray(ArrayList<String[]> columns) {
		consolePrint("------------- start -------------------\n");
		for (String[] array : columns) {
			printStrArray(array);
		}
		consolePrint("------------- end -------------------\n");
	}

	public static void printStrArray(String[] array) {
		for (String string : array) {
			consolePrint(string + " ");
		}
		consolePrint("\n");
	}

	public static String getArrayStr(String[] array) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : array) {
			stringBuilder.append(string + " ");
		}
		return stringBuilder.toString();
	}

	public static void filePrint(String strLine) {
		if (resultFileHandle == null) {
			try {
				resultFileHandle = new FileWriter(resultFileName.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			resultFileHandle.write(strLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void print(String strLine) {
		consolePrint(strLine);
		filePrint(strLine);
	}

	public static void println(String strLine) {
		consolePrint(strLine + "\n");
		filePrint(strLine + "\n");
	}

	public static String format(String format, Object... objects) {
		return new Formatter().format(format, objects).toString();
	}

	public static void consolePrint(String strParam) {
		System.out.print(strParam);
	}

	public static void consolePrint(ArrayList<String[]> param) {
		for (String[] strings : param) {
			for (String string : strings) {
				System.out.print(string + " ");
			}
			System.out.println("");
		}
	}

	public static String getFormat3Str(double param) {
		return DECIMAL_FORMAT_3.format(param * 100) + "%";
	}

	public static String getFormat2Str(double param) {
		return DECIMAL_FORMAT_2.format(param * 100) + "%";
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
		System.err.println("Error messages:pls input valid parameters.\n");
		consolePrint("HELP:\n�������ƶ����ϴ����û���Ϊ��־��\n" + "���룺1����Ϊ������־�������ж������2����Ϊ���������Ͷ�Ӧֵӳ���EXCEL��񣩡�\n" + "��������������\n"
				+ "Usages:\n"
				+ "java -jar UserActionParser -l(log file) \"��Ϊ��־�ļ�1����·��\" \"��Ϊ��־�ļ�2����·��\"  -e(excel file) \"��Ϊ������������ֵӳ�������·��\"\n"
				+ "E.g:\n"
				+ "\t-l \"F:\\user_path\\user_path_s2.log.2017-05-02(1)\" \"F:\\user_path\\user_path_s2.log.2017-05-02(2)\" -e \"F:\\user_path\\user_path.log.2017-05-02.xls\"\n"
				+ "\t-unistall \"F:\\user_path\\user_path_s2.log.2017-05-02\" \"F:\\user_path\\user_path.log.2017-05-02\"\n"
				+ "��ע����Ϊ��־�ļ����ܻ���ڶ������Ϊ֮ǰĳһ�����־���Ƿ��������ļ��ģ��� ��ѡ��-f(filter) \"�����ļ�·��\"���� ��-u(uninstall) \"�Ѿ�ж�ص���־�ļ�·��\")��");
		System.exit(0);
	}

	public static void openLogFile() {
		try {
			Utils.resultFileHandle.flush();
			Utils.resultFileHandle.close();

			Runtime runtime = Runtime.getRuntime();
			runtime.exec("cmd /c start notepad++ " + Utils.resultFileName.toString());
		} catch (Exception e) {
			try {
				Utils.resultFileHandle.flush();
				Utils.resultFileHandle.close();

				Runtime runtime = Runtime.getRuntime();
				runtime.exec("cmd /c start notepad " + Utils.resultFileName.toString());
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}

	}

}
