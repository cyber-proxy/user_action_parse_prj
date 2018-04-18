package com.exam.useractionparse.main;

import java.util.ArrayList;

import com.exam.useractionparse.algorithm.ParseUnInstallUserPath;
import com.exam.useractionparse.algorithm.ParseUserPathLog;
import com.exam.useractionparse.data.UserActionDes;
import com.exam.useractionparse.utils.Utils;

import antlr.StringUtils;

public class UserActionParser {
	private static ArrayList<String> sLogList= new ArrayList<>();
	
	public static void main(String args[]) {
		if (args != null && args.length > 0) {
			if (args[0].toString().equals("-uninstall")) {
				Utils.resultFileName.append(args[1]).append(".result").append(".txt");
				ParseUnInstallUserPath parseUserPathLog = new ParseUnInstallUserPath();
				parseUserPathLog.doParse(args[1].toString(), args[2].toString());
			} else {
				// get "user action log" path list
				boolean foundLogParam = false;
				for(String arg : args){
					if (arg.equals("-l")) {
						foundLogParam = true;
						continue;
					}
					if (arg.equals("-e")) {
						break;
					}
					if (foundLogParam) {
						sLogList.add(arg);
					}
				}
				// get "excel" path
				boolean foundExcel = false;
				for(String arg : args){
					if (arg.equals("-e")) {
						foundExcel = true;
						continue;
					}
					if (foundExcel) {
						UserActionDes.sExcelPath = arg;
						break;						
					}
				}
				if (!sLogList.isEmpty() && UserActionDes.sExcelPath.length() > 0) {
					/*Utils.resultFileName.append(sLogList.get(0)).append(".result").append(".txt");
					ParseUserPathLog parseUserPathLog = new ParseUserPathLog();
					parseUserPathLog.doParse(sLogList);*/
					UserActionDes.parseExl();
				}else {
					Utils.showHelp();
				}
				
			/*	if (args[0].toString().equals("-l")) {
					*//**
					 * 此处之所以传入了2个文件，是因为之前某一天的日志都是分做两个文件的。
					 * parseUserPathLog.doParse传入的是可变参数。
					 *//*
					Utils.resultFileName.append(args[1]).append(".result").append(".txt");
					String path[] = new String[10];
					for (int i = 1; i < args.length; i++) {
						path[i - 1] = new String(args[i]);
					}
					new ParseUserPathLog().doParse(path);
				} else if (args[0].toString().equals("-parse_filter")) {
					Utils.resultFileName.append(args[1]).append(".result").append(".txt");
					ParseUserPathLog parseUserPathLog = new ParseUserPathLog();
					switch (args.length) {
					case 3:
						parseUserPathLog.getFilter(args[2].toString());
						parseUserPathLog.doParse(args[1].toString());
						break;
					case 4:
						parseUserPathLog.getFilter(args[3].toString());
						parseUserPathLog.doParse(args[1].toString(), args[2].toString());
						break;
					}
				} else {
					Utils.showHelp();
				}*/
			}
		} else {
			Utils.showHelp();
		}
//		Utils.openLogFile();
	}
}
