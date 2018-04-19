package com.exam.useractionparse.utils;

import java.util.ArrayList;

import com.exam.useractionparse.cfg.ConstantValue;

public class ParseUtils {
	public static boolean isValidLine(String line) {
		String[] array = line.split(ConstantValue.PATH_PREFIX);
		String[] actionList = new String[0];
		if (array != null && array.length > 1) {
			actionList = array[1].split(ConstantValue.DELIMITER);
		}
		for (String action : actionList) {
			if (!DesUtils.isValidActionType(action)) {
				return false;
			}
		}
		return true;
	}

	public static String[] getUserPathStr(String line) {
		String[] splitResult = line.split(ConstantValue.PATH_PREFIX);
		String[] actionList = new String[0];
		if (splitResult != null && splitResult.length > 1) {
			actionList = splitResult[1].split(ConstantValue.DELIMITER);
		}
		return actionList;
	}

	public static String getAndroidIdStr(String line) {
		String[] splitResult = line.split(ConstantValue.ANDROID_ID_REFIX);
		String[] splitResult1 = new String[0];
		if (splitResult != null && splitResult.length > 1) {
			splitResult1 = splitResult[1].split(ConstantValue.ANDORID_ID_POSTFIX);
		}
		return splitResult1[0];
	}

	public static boolean isInFilterList(ArrayList<String> androidList, String line) throws Exception {
		if (androidList.isEmpty()) {
			throw new Exception("android id filter list is empty!!!");
		}
		String androididOfLine = getAndroidIdStr(line);
		for (String filterAndroidId : androidList) {
			if (androididOfLine.equals(filterAndroidId)) {
				return true;
			}
		}
		return false;
	}
}
