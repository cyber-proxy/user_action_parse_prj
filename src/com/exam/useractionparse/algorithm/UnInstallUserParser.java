package com.exam.useractionparse.algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.exam.useractionparse.utils.Log;
import com.exam.useractionparse.utils.ParseUtils;

public class UnInstallUserParser {
	private ArrayList<String> android_id_readLineResult = new ArrayList<>();

	public void doParse(String fileName, String fileName1) {
		readFileByLine(fileName);
		readFileByLine(fileName1);
		Log.print("设备总数: " + android_id_readLineResult.size() + "\n");
		for (String string : android_id_readLineResult) {
			Log.print(string + "\n");
		}
	}

	private void readFileByLine(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String readLineStr = null;
			while ((readLineStr = reader.readLine()) != null) {
				if (ParseUtils.isValidLine(readLineStr)) {
					Log.consolePrint(ParseUtils.getAndroidIdStr(readLineStr) + "\n");
					android_id_readLineResult.add(ParseUtils.getAndroidIdStr(readLineStr));
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

}
