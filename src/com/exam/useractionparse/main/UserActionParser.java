package com.exam.useractionparse.main;

import com.exam.useractionparse.algorithm.ParseUnInstallUserPath;
import com.exam.useractionparse.algorithm.ParseUserPathLog;
import com.exam.useractionparse.utils.Utils;

public class UserActionParser {
	static int i;
	public static void main(String args[]) {
		if (args != null && args.length > 0) {
			if (args[0].toString().equals("-uninstall")) {
				Utils.resultFileName.append(args[1]).append(".result").append(".txt");
			    ParseUnInstallUserPath parseUserPathLog = new ParseUnInstallUserPath();
			    parseUserPathLog.doParse(args[1].toString(), args[2].toString());
				
			}else if(args[0].toString().equals("-l")){
				/**
				 * �˴�֮���Դ�����2���ļ�������Ϊ֮ǰĳһ�����־���Ƿ��������ļ��ġ�
				 * parseUserPathLog.doParse������ǿɱ������
				 */
				Utils.resultFileName.append(args[1]).append(".result").append(".txt");
				String path[] = new String[10];
				for(int i = 1; i < args.length; i++){
					path[i-1] = new String(args[i]);
				}				
			    new ParseUserPathLog().doParse(path);
			}else if(args[0].toString().equals("-parse_filter")){
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
			}else {
				Utils.showHelp();
			}
		}else {
			Utils.showHelp();
		}
	    Utils.openLogFile();
	}
}
