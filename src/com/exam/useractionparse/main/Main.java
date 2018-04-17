package com.exam.useractionparse.main;

import com.exam.useractionparse.algorithm.ParseUnInstallUserPath;
import com.exam.useractionparse.algorithm.ParseUserPathLog;
import com.exam.useractionparse.utils.Utils;

public class Main {
	static int i;
	public static void main(String args[]) {
		if (i < 0) {
			
		}
		if (args != null && args.length > 0) {		
			if (args[0].toString().equals("-h") || args[0].toString().equals("help")) {
				showHelp();
			}else if (args[0].toString().equals("-uninstall")) {
				Utils.resultFileName.append(args[1]).append(".result").append(".txt");
			    ParseUnInstallUserPath parseUserPathLog = new ParseUnInstallUserPath();
			    parseUserPathLog.doParse(args[1].toString(), args[2].toString());
				
			}else if(args[0].toString().equals("-parse")){
				/**
				 * 此处之所以传入了2个文件，是因为之前某一天的日志都是分做两个文件的。
				 * parseUserPathLog.doParse传入的是可变参数。
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
				showHelp();
			}
		}else {
			showHelp();
		}		
		
		try {
			Utils.resultFileHandle.flush();
			Utils.resultFileHandle.close();
			
			Runtime runtime = Runtime.getRuntime();
			runtime.exec("cmd /c start notepad " + Utils.resultFileName.toString());
		}catch (Exception e) {
			e.printStackTrace();
		}  
	    
	}
	
	public static void showHelp(){
		System.out.println("help for example:\n"
				+ "\t-parse \"F:\\user_path\\user_path_s2.log.2017-05-02\" .xls \"F:\\user_path\\user_path.log.2017-05-02.xls\"\n"
				+ "\t-unistall \"F:\\user_path\\user_path_s2.log.2017-05-02\" \"F:\\user_path\\user_path.log.2017-05-02\""
				+ "\nFYA:your file path that filled in must have a excel file");
		System.exit(0);		
	}

}
