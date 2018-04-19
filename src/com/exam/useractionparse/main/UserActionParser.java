package com.exam.useractionparse.main;

import java.util.ArrayList;

import com.exam.useractionparse.algorithm.UnInstallUserParser;
import com.exam.useractionparse.algorithm.ActionPathLogParser;
import com.exam.useractionparse.data.UserActionDes;
import com.exam.useractionparse.utils.DesUtils;

public class UserActionParser {
	private static ArrayList<String> sLogList = new ArrayList<>();

	public static void main(String args[]) {
		if (args != null && args.length > 0) {
			if (args[0].toString().equals("-uninstall")) {
				DesUtils.resultFileName.append(args[1]).append(".result").append(".txt");
				UnInstallUserParser parseUserPathLog = new UnInstallUserParser();
				parseUserPathLog.doParse(args[1].toString(), args[2].toString());
			} else {
				// get "user action log" path list
				boolean foundLogParam = false;
				for (String arg : args) {
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
				for (String arg : args) {
					if (arg.equals("-e")) {
						foundExcel = true;
						continue;
					}
					if (foundExcel) {
						UserActionDes.sExcelPath = arg;
						break;
					}
				}
				if (!sLogList.isEmpty()) {
					if (UserActionDes.sExcelPath.length() > 0) {
						UserActionDes.parseExl();						
					}
					DesUtils.resultFileName.append(sLogList.get(0)).append(".result").append(".txt");
					ActionPathLogParser parseUserPathLog = new ActionPathLogParser();
					parseUserPathLog.doParse(sLogList);
				} else {
					DesUtils.showHelp();
				}

				/*
				 * if (args[0].toString().equals("-l")) {
				 *//**
					 * �˴�֮���Դ�����2���ļ�������Ϊ֮ǰĳһ�����־���Ƿ��������ļ��ġ�
					 * parseUserPathLog.doParse������ǿɱ������
					 *//*
					 * Utils.resultFileName.append(args[1]).append(".result").
					 * append(".txt"); String path[] = new String[10]; for (int
					 * i = 1; i < args.length; i++) { path[i - 1] = new
					 * String(args[i]); } new ParseUserPathLog().doParse(path);
					 * } else if (args[0].toString().equals("-parse_filter")) {
					 * Utils.resultFileName.append(args[1]).append(".result").
					 * append(".txt"); ParseUserPathLog parseUserPathLog = new
					 * ParseUserPathLog(); switch (args.length) { case 3:
					 * parseUserPathLog.getFilter(args[2].toString());
					 * parseUserPathLog.doParse(args[1].toString()); break; case
					 * 4: parseUserPathLog.getFilter(args[3].toString());
					 * parseUserPathLog.doParse(args[1].toString(),
					 * args[2].toString()); break; } } else { Utils.showHelp();
					 * }
					 */
			}
		} else {
			DesUtils.showHelp();
		}
		 DesUtils.openLogFile();
	}
}
