package com.exam.useractionparse.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Map;

import com.exam.useractionparse.cfg.ConstantValue;

public class Log {

	public static void print(String strLine) {
		Log.consolePrint(strLine);
		Log.filePrint(strLine);
	}

	public static void filePrint(String strLine) {
		if (DesUtils.resultFileHandle == null) {
			try {
				DesUtils.resultFileHandle = new FileWriter(DesUtils.resultFileName.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			DesUtils.resultFileHandle.write(strLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void println(String strLine) {
		Log.consolePrint(strLine + "\n");
		filePrint(strLine + "\n");
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

	public static void printStrArray(String[] array) {
		for (String string : array) {
			consolePrint(string + " ");
		}
		consolePrint("\n");
	}

	public static void printTableListArray(ArrayList<String[]> columns) {
		consolePrint("------------- start -------------------\n");
		for (String[] array : columns) {
			printStrArray(array);
		}
		consolePrint("------------- end -------------------\n");
	}

	public static void printError(String string) {
		System.err.println(string);
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

	public static String format(String format, Object... objects) {
		return new Formatter().format(format, objects).toString();
	}

	public static String getFormat3Str(double param) {
		return ConstantValue.DECIMAL_FORMAT_3.format(param * 100) + "%";
	}

	public static String getFormat2Str(double param) {
		return ConstantValue.DECIMAL_FORMAT_2.format(param * 100) + "%";
	}

}
