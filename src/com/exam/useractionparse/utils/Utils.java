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
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exam.useractionparse.cfg.Config;
import com.exam.useractionparse.data.NewUserAction;

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
	 * 解析NewUserActionType.java文件
	 * @param classFilePath
	 * @return
	 */
	public static Map<String, String> getActionDes(String classFilePath){
		Map<String, String> actionDes = new HashMap<>();
    	BufferedReader reader = null;
		try {
	        File file = new File(classFilePath);
	        if (!file.isDirectory()) {
	        	reader = new BufferedReader(new FileReader(classFilePath));
	        	String readLine = null;
	        	while( (readLine = reader.readLine()) != null){
//		        	consolePrint(readLine);
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
	        }else {
				consolePrint("not a file\n");
			}
		}catch (Exception e) {
			consolePrint(e.getMessage().toString()+"\n");
			return null;
		}finally {
			try {
				if (reader != null) {
					reader.close();					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
				
			for (Map.Entry<String, String> entry : actionDes.entrySet()) {  
				  
//			    consolePrintln("Key=" + entry.getKey() + "Value =" + entry.getValue());  
			  
			} 
		}		
		
		return actionDes;		
	}
	
	public static boolean isValidActionType(String actionType){
		if (isNumeric(actionType)) {
			if (NewUserAction.actionDes.containsKey(Integer.valueOf(actionType))) {
				return true;
			}else {
				if (Config.printInvalidValue) {
					Utils.print("not contain :" + actionType);					
				}		
			}
		}else {
			Utils.print("not numeric :" + actionType);
		}
		return false;
	}
	
	public static String getDescrib(String actionType){
		if (actionType.equals("none")) {
			return "*没有操作*";
		}
		if (!isNumeric(actionType)) {
			return actionType;
		}
		if (NewUserAction.actionDes.containsKey(Integer.valueOf(actionType))) {
			return NewUserAction.actionDes.get(Integer.valueOf(actionType));			
		}else {
			return actionType;
		}
	}
	
	public static boolean isNumeric(String str){ 
	   Pattern pattern = Pattern.compile("[0-9]*"); 
	   Matcher isNum = pattern.matcher(str);
	   if( !isNum.matches() ){
	       return false; 
	   } 
	   return true; 
	}
	
	public static boolean isNumeric2(String str){
	  for (int i = 0; i < str.length(); i++){
		   if (!Character.isDigit(str.charAt(i))){
			   return false;
		   }
	  	}
		  return true;
	 }
	
	/**
	 * 判断是否合法","分割的数字串。
	 * @param str
	 * @return
	 */
	public static boolean isValidActionList(String str){
		Pattern pattern = Pattern.compile("(/d{1,}/,){0,}");
		if (pattern.matcher(str).matches()) {
			return true;
		}
		return false;		
	}
	
	public static void printTable(ArrayList<Map<String, Integer>> columns){
		consolePrint("------------- start -------------------\n");
		for(Map<String, Integer> map : columns){
			Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<String, Integer> entry = iterator.next();
				consolePrint("("+entry.getKey()+","+entry.getValue()+") ");
			}
			consolePrint("\n");
		}
		consolePrint("------------- end -------------------\n");
	}
	
	public static void printTableListArray(ArrayList<String[]> columns){
		consolePrint("------------- start -------------------\n");
		for(String[] array : columns){
			printStrArray(array);
		}
		consolePrint("------------- end -------------------\n");
	}
	
	public static void printStrArray(String[] array){
		for(String string : array){
			consolePrint(string+" ");
		}
		consolePrint("\n");
	}
	
	public static String getArrayStr(String[] array){
		StringBuilder stringBuilder = new StringBuilder();
		for(String string : array){
			stringBuilder.append(string+" ");
		}
		return stringBuilder.toString();
	}
	
	public static void filePrint(String strLine){
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
	
	public static void print(String strLine){
		consolePrint(strLine);
		filePrint(strLine);
	}
	public static void println(String strLine){
		consolePrint(strLine+"\n");
		filePrint(strLine+"\n");
	}
	
	public static String format(String format, Object...objects){
		return new Formatter().format(format, objects).toString();
	}
	
	public static void consolePrint(String strParam){
		System.out.print(strParam);
	}
    
	public static  String getFormat3Str(double param){
    	return DECIMAL_FORMAT_3.format(param * 100) + "%";
    }
    
	public static  String getFormat2Str(double param){
    	return DECIMAL_FORMAT_2.format(param * 100) + "%";
    }
    
	public static  int getValueOfMap(Map<String, Integer> map, String key){
    	if (map.containsKey(key)) {
			return map.get(key);
		}else{
			return 0;
		}
    }
    
	public static int getValueOfMap(Map<Integer, Integer> map, Integer key){
    	if (map.containsKey(key)) {
			return map.get(key);
		}else{
			return 0;
		}
    }

}
