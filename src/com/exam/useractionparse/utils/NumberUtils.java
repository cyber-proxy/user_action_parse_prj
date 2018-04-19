package com.exam.useractionparse.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

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

	public static boolean isNumeric2(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

}
