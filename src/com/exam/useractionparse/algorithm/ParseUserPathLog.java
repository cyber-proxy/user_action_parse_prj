package com.exam.useractionparse.algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.rmi.CORBA.Util;

import com.exam.useractionparse.cfg.Config;
import com.exam.useractionparse.data.NewUserAction;
import com.exam.useractionparse.utils.ParseUtils;
import com.exam.useractionparse.utils.Utils;

import java.util.Set;

public class ParseUserPathLog {
	
	private static final int MAX_USER_ACTION_STEP = 200;
	private static final int MAX_USER_ACTION_TYPE = 100;
	private static final String EXIT_TAG = "none";
	private static final double THRESHOLD = 0.01;
	private static final String HOME_CLICK_KEY = ""+NewUserAction.ACTION_HOME_KEY_PRESS;
	private static final String DOUBLE_CLICK_BACK_KEY = ""+NewUserAction.ACTION_DOUBLE_BACK;
	
//	从文件中解析得到的字符串数组队列。	
    private ArrayList<String[]> actionPathList = new ArrayList<>();    
//  保存统计信息。  
    private ArrayList<Map<String, Integer>> actionStaticsOfPerStep = new ArrayList<>(MAX_USER_ACTION_STEP);
//	统计HOME键退出前，操作分布比例。
    private Map<String, Integer> home_exit_static = new HashMap<String, Integer>();
//	统计双击退出前，操作分布比例。
    private Map<String, Integer> double_click_statics = new HashMap<>();
//	统计HOME建退出前，已经操作的部分分布。
    private Map<Integer, Integer> step_statics_home = new HashMap<>();
//	统计双击退出前，已经操作的部分分布。
    private Map<Integer, Integer> step_statics_double = new HashMap<>();
//	拉通统计一天以内的操作数分布。
    private Map<Integer, Integer> step_statics = new HashMap<>();
//	最后一步类型统计。
    private Map<String, Integer> last_action_statics = new HashMap<>();
//  统计2步操作后退出后，前一个操作的分布比例。
    private Map<String, Integer> action_exit_2_step_statics = new HashMap<>();
//	卸载用户的android id
    private ArrayList<String> uninstalledAndroidIdList = new ArrayList<>();
//	检测两个文件重复的android id列表。
    private ArrayList<String> android_list_distinct1 = new ArrayList<>();
    private ArrayList<String> android_list_distinct2 = new ArrayList<>();
    
    private void init(){
    	//MAX_USER_ACTION_TYPE = //Utils.MaxNum +1; // 注意一定要在这里+1。为什么如果不加1，在parse的时候，第六次访问columns的第六列的时候columns.get(6).size()的大小始终为2或者为1？？？
    	Utils.print("最大的ACTION TYPE值： "+MAX_USER_ACTION_TYPE+"\n");
        
        /**
         * columns初始化后的内容：
         * 0 <"1",0> <"2",0> <"3",0> ... <"43",0>
         * 1 <"1",0> <"2",0> <"3",0> ... <"43",0>
         * 2 <"1",0> <"2",0> <"3",0> ... <"43",0>
         * ...
         * 99 <"1",0> <"2",0> <"3",0> ... <"43",0>
         */
        for (int i = 0; i < MAX_USER_ACTION_STEP; i++) {
        	try{
                Map<String, Integer> row = new HashMap<>();
                for (int j = 0; j < MAX_USER_ACTION_TYPE; j++) {
                    row.put(String.valueOf(j), 0);
                }
//                Utils.consolePrint(row.size()+" ");
                actionStaticsOfPerStep.add(row);        		
        	}catch (Exception e) {
        		Utils.print(e.getMessage().toString());
			}
        }
    	Utils.print("初始化完成。\n");
    }

    public void doParse(String ...fileNames) {    	
    	init();

        try {
        	for (String fileName : fileNames) {
                readfile(fileName);				
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
//    	Utils.printTable(columns);
        if (Config.printColumn) {
        	Utils.printTableListArray(actionPathList);			
		}
        
        Utils.println("generate data");
        
        generateColumns();
    	
//    	Utils.printTable(columns);
        
        Utils.println("do statics");

        doStatics(fileNames);
        exit_Statics(fileNames);

    }

    /**
     * 执行完成后，path = 
     * [37, 5, 4]
     * [37]
     * [37, 5, 7, 14, 14, 21, 25, 5, 5, 8, 9, 6]
     */
    private void readFileByLines(String fileName) {
    	File file;
    	BufferedReader reader;
    	int validPathCount;
    	
    	// initialize block
    	{
            file = new File(fileName);
            reader = null;
            validPathCount = 0;
    		
    		if (Config.printRedundancy) {
    			android_list_distinct1.clear();
    		}
    		
    	}
    	
        try {
            reader = new BufferedReader(new FileReader(file));
            String readLineStr = null;
            while ((readLineStr = reader.readLine()) != null) 
            {
                if (ParseUtils.isValidLine(readLineStr)){// && ParseUtils.isInFilterList(uninstalledAndroidIdList, readLineStr)) {	
					actionPathList.add(ParseUtils.getUserPathStr(readLineStr));
					validPathCount++;
					
					if (Config.printRedundancy) {						
		            	String androidid = ParseUtils.getAndroidIdStr(readLineStr);
		            	if (android_list_distinct1.contains(androidid)) {
		            		Utils.println("重复android id: "+androidid);					
						}
		            	android_list_distinct1.add(androidid);						
					}
				}else {
					// 打印出不在newUserAction里面定义的行为代号。
		            if (Config.printInvalidValue) {
		            	Utils.println("无效行: "+readLineStr+"\n");						
					}
				}
            }
            Utils.println("有效设备累计总数: "+actionPathList.size());
            Utils.println("当前文件效设备累计: "+validPathCount);
            reader.close();
        } catch (Exception e) {
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
            Utils.consolePrint("文件读取完成。\n");
        }
    }

    /** 统计完成后的内容如下：
    * 0 <"1",10> <"4",22> <"5",33> ... <"37",55> <"EXITTAG",10>
    * 1 <"1",22> <"4",20> <"5",11> ... <"37",40> <"EXITTAG",0>
    * 2 <"1",0> <"4",1> <"3",0> ... <"37",0> <"EXITTAG",0>
    * ...
    * 99 <"1",0> <"2",0> <"3",0> ... <"37",0> <"EXITTAG",0>
    */
    /**
     * 当i=0:
     * 	map = <"1",10> <"4",22> <"5",33> ... <"37",55> <"EXITTAG",10>
     * 	total = 各个元素值的和。
     *  循环打印各个元素值与total的和。
     *  疑问：none里面的值是如何确定的？这么样的统计意义何在？？？因为总数始终都是不变的，其值等于path里面的行数，即用户总数.
     */
    private void doStatics(String ...fileNames) {
    	Utils.consolePrint("开始计算比例：\n");
    	String deString = "";
    	int exitTotalBefore = 0;
    	int exitTotalBeforeTemp = 0;    	
    	int exitBasedOnLastStep = 0;
    	double exitBasedOnLastPercent = 0.0;
    	double total = 0.0;
    	double percentOfPerActionAction = 0.0;
    	List<Entry<String, Integer>> mapList = new ArrayList<Entry<String, Integer>>();
    	int theOthers = 0;
    	double thOthersPercent = 0.0;
    	String theOtherStr = "TheOthers";
        StringBuilder sb = new StringBuilder();        
        int sum = 0;
        int lastSum = 0;

        for (int i = 0; i < actionStaticsOfPerStep.size(); i++) {
            Map<String, Integer> map = actionStaticsOfPerStep.get(i);
            mapList.addAll((map.entrySet()));
            Collections.sort(mapList, new Comparator<Map.Entry<String, Integer>>() {

				@Override
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					return -o1.getValue() + o2.getValue();
				}
			});
            
            // 总额统计。
            Iterator<Map.Entry<String, Integer>> it1 = mapList.iterator();
            while (it1.hasNext()) {
                Map.Entry<String, Integer> me = it1.next();
                Integer value = me.getValue();
                sum += (int) value;
                if (me.getKey().equals(EXIT_TAG)) {
                	exitTotalBeforeTemp = me.getValue();
				}
            } // end while
            exitBasedOnLastStep = exitTotalBeforeTemp - exitTotalBefore;
            sum = sum - exitTotalBeforeTemp;
            if (sum > 0) {
                exitBasedOnLastPercent = (exitBasedOnLastStep / (double)sum);				
			} else {
				exitBasedOnLastPercent = 0.0;
				break;
			}
            

            if (lastSum == 1) {
				if (!Config.printDebugMsg) {
					break;
				}
			}
            
            total = sum / 1.0;
            /**
             * 计算基于上一步操作退出了多少比例的用户：
             */
            sb.append("步："+i);
            sb.append(" remain："+sum+" "+" 上一步退出："+(Utils.DECIMAL_FORMAT_2.format(exitBasedOnLastPercent * 100))+"%").append("(");
            lastSum = sum;

            // 每个行为的比例计算。
            Iterator<Map.Entry<String, Integer>> it2 = mapList.iterator();
            while (it2.hasNext()) {
                Map.Entry<String, Integer> me = it2.next();
                Integer value = me.getValue(); 
                String stringKey = me.getKey();
                if (stringKey.equals(EXIT_TAG)) {
					continue;
				}
                if (value > 0) {
                	deString = Utils.getDescrib(stringKey,fileNames);
                	if(total > 0){
                		percentOfPerActionAction = value / total;
                	}else{
                		percentOfPerActionAction = 0.0;
                	}
                	if (percentOfPerActionAction < THRESHOLD) {
						theOthers += value;
						continue;
					}
                    sb.append(" [" + deString + ", " + (Utils.DECIMAL_FORMAT_2.format(percentOfPerActionAction * 100)) + "%]");
                }
            }  // end while
            if (theOthers > 0 && total > 0) {
				thOthersPercent = theOthers / total;
                sb.append(" [" + theOtherStr + ", " + (Utils.DECIMAL_FORMAT_2.format(thOthersPercent * 100)) + "%]");
			}

            sb.append(")\n");
            if (Config.enablePathParse) {
            	Utils.print(sb.toString());				
			}
            exitTotalBefore = exitTotalBeforeTemp;
            /**
             * 做清理还原工作。
             */
            {
            	sum = 0;
            	deString = "";
//            	exitTotalBefore = 0; 需要用来保存上一次的值。
            	exitTotalBeforeTemp = 0;    	
            	exitBasedOnLastStep = 0;
            	exitBasedOnLastPercent = 0.0;
            	total = 0.0;
            	percentOfPerActionAction = 0.0;
            	mapList.clear();
            	sb.setLength(0);  
            	theOthers = 0;
            	thOthersPercent = 0.0;
            } // end 清理工作
        } // end for
    }

    // 计算百分比
    private String myPercent(int many, int hundred) {
        String baifenbi = "";// 接受百分比的值
        double baiy = many * 1.0;
        double baiz = hundred * 1.0;
        double fen = baiy / baiz;
        DecimalFormat df1 = new DecimalFormat("##.00%"); // ##.00%
        baifenbi = df1.format(fen);
        return baifenbi;
    }
    
//    path保存的是字符串数组组成的列表，即从文件中得到的所有path内容。
//    此函数之前，columns的内容是空的。columns用来统计各个步骤中各个操作的次数。
//    依次取出path中的内容，
//    columns里面的行和列代表什么？行代表的是path中的一列（即操作中的一步），每一列代表的是每一个操作类型和这个类型对应的统计个数。
//    如果path中的某一行的长度小雨
//    此函数执行之后：
    /**
     * path里面的内容
     * [37, 5, 4]
     * [37]
     * [37, 5, 7, 14, 14, 21, 25, 5, 5, 8, 9, 6]
     * ...
     */
//    解析path里面的内容是一行一行解析的。
    /**
     * 当j = 0: 
     * 	j < 3; 
     * 		if(columns[0].contains[37]) 
     * 			columns[0][37].count++;
     * 当j = 1
     * 	j <３
     * 		if(columns[0].contains[5])
     * 			columns[1][5].count++;
     * 当j = 2
     * 	j <３
     * 		if(columns[0].contains[5])
     * 			columns[2][4].count++;
     * 当j=3: 
     * 	j<3不成立。
     * 		if(columns[0].containsKey("EXITTAG"))
     * 			columns[0]["EXITTAG"].count++;
     */
    
    /**
     * columns内容：
     * 0 <"1",0> <"4",0> <"5",0> ... <"37",3> <"EXITTAG",0>
     * 1 <"1",0> <"4",0> <"5",2> ... <"37",0> <"EXITTAG",0>
     * 2 <"1",0> <"4",1> <"3",0> ... <"37",0> <"EXITTAG",0>
     * ...
     * 99 <"1",0> <"2",0> <"3",0> ... <"37",0> <"EXITTAG",3>
     */
    /**
     * 这个表的意义是什么？
     * 第j行，代表第j次操作。第j行里面的内容，代表所有用户在进行第j次操作中，各个操作内容的次数总和。第j行的所有计数的总数等于path的行数，即用户数。
     * 统计完成后的内容如下：
     * 0 <"1",10> <"4",22> <"5",33> ... <"37",55> <"EXITTAG",10>
     * 1 <"1",22> <"4",20> <"5",11> ... <"37",40> <"EXITTAG",0>
     * 2 <"1",0> <"4",1> <"3",0> ... <"37",0> <"EXITTAG",0>
     * ...
     * 99 <"1",0> <"2",0> <"3",0> ... <"37",0> <"EXITTAG",0>
     */
    private void generateColumns() {
    	Utils.println("开始解析...");
    	int maxLength = 0;
    	String[] maxStrArray = new String[0];
        for (String[] arrPath : actionPathList) {
        	if (arrPath.length > maxLength) {
				maxLength = arrPath.length;
				maxStrArray = arrPath;
			}
            for (int j = 0; j < MAX_USER_ACTION_STEP; j++) {
                Map<String, Integer> map = actionStaticsOfPerStep.get(j);
                if (j < arrPath.length) {
                    String type = arrPath[j];
                    if (map.containsKey(type)) {
                        Integer num = map.get(type);
                        map.put(type, num + 1);
                    } else {
                        HashMap<String, Integer> newMap = new HashMap<>();
                        newMap.put(type, 1);
                        actionStaticsOfPerStep.set(j, newMap);
                    }
                } else {
                    if (map.containsKey(EXIT_TAG)) {
                        Integer num = map.get(EXIT_TAG);
                        map.put(EXIT_TAG, num + 1);
                    } else {
                        map.put(EXIT_TAG, 1);
                    }
                }
                
            }
        }
        Utils.consolePrint("统计表：");
        Utils.printTable(actionStaticsOfPerStep);
        Utils.print("最长操作 = "+maxLength+" 最长操作序列: \n");
        Utils.printStrArray(maxStrArray);
    }
    
    private void exit_Statics(String ...fileNames){
    	String key_before_back = "none";
    	int count_before_back = 0;
    	double amountOfHomeBack = 0;
    	double amountOfDoubleBack = 0;
    	double deviceAmount = actionPathList.size();
    	
    	for(String[] actionPath : actionPathList){
    		count_before_back = 0;
    		key_before_back = "none";
    		
    		if(actionPath.length>0) {
    			step_statics.put(actionPath.length, Utils.getValueOfMap(step_statics, actionPath.length)+1);
    			last_action_statics.put(actionPath[actionPath.length-1], Utils.getValueOfMap(last_action_statics, actionPath[actionPath.length-1])+1);
    		
    		}
    		
    		if (actionPath.length < 3) {
    			if (actionPath.length == 2) {
    				action_exit_2_step_statics.put(actionPath[0], Utils.getValueOfMap(action_exit_2_step_statics,  actionPath[0]) + 1);					
				}else {
//    				action_exit_2_step_statics.put(actionPath[0], Utils.getValueOfMap(last_action_statics,  actionPath[0]) + 1);						
				}
			}
    		
    		for(String key : actionPath){
    			if (key.equals(HOME_CLICK_KEY)) {
    				// 拉通统计所有home键退出前的操作分布。
					home_exit_static.put(key_before_back, Utils.getValueOfMap(home_exit_static,key_before_back) + 1);
					// 统计每一个用户在home键退出前操作步数。
					step_statics_home.put(count_before_back, Utils.getValueOfMap(step_statics_home, count_before_back) + 1);	
					amountOfHomeBack++;
					break;
				}else if (key.equals(DOUBLE_CLICK_BACK_KEY)) {
//					Utils.print(Utils.getArrayStr(keyArray)+"\n");
					// 拉通统计所有double键退出前的操作分布。
					double_click_statics.put(key_before_back, Utils.getValueOfMap(double_click_statics,key_before_back) + 1);
					// 统计每一个用户在double键退出前操作数分布。
					step_statics_double.put(count_before_back, Utils.getValueOfMap(step_statics_double, count_before_back) + 1);
					amountOfDoubleBack++;
//					if (count_before_back == 32) {
//						Utils.print("44之前为32的操作序列： " + Utils.getArrayStr(keyArray)+"\n");
//					}
					break;
				}else {
					key_before_back = key;
					count_before_back++;
				}
    		}
    	}
    	
    	Utils.println("................................"); 
    	Utils.println("HOME键退出前，操作分布：总数"+amountOfHomeBack);
    	Utils.println("................................");
    	List<Map.Entry<String, Integer>> home_exit_static_list = new ArrayList<Map.Entry<String, Integer>>(home_exit_static.entrySet());
    	Collections.sort(home_exit_static_list, Utils.strKeyComparator);
    	Iterator<Entry<String, Integer>> iterator = home_exit_static.entrySet().iterator();
		Utils.println(Utils.format("%-20s%5s", "操作","比例"));    
    	for(Map.Entry<String, Integer> entry : home_exit_static_list){
    		Utils.println(Utils.format("%-20s%5s", entry.getKey() + "-" + Utils.getDescrib(entry.getKey(),fileNames), Utils.getFormat3Str(entry.getValue()/amountOfHomeBack)));    		
		};
    	
    	Utils.println("................................");    	
    	Utils.println("双击退出前，操作分布：总数"+amountOfDoubleBack);
    	Utils.println("................................");
    	List<Map.Entry<String, Integer>> double_click_statics_list = new ArrayList<Map.Entry<String, Integer>>(double_click_statics.entrySet());    
    	Collections.sort(double_click_statics_list, Utils.strKeyComparator);
		Utils.println(Utils.format("%-20s%5s", "操作","比例"));    
    	for(Map.Entry<String, Integer> entry : double_click_statics_list){
    		Utils.println(Utils.format("%-20s%5s", entry.getKey() + "-" + Utils.getDescrib(entry.getKey(),fileNames), Utils.getFormat3Str(entry.getValue()/amountOfDoubleBack)));
    	}
    	
    	Utils.println("................................");    	
    	Utils.println("HOME键退出前，操作数分布：总数:"+amountOfHomeBack);
    	Utils.println("................................");
    	List<Map.Entry<Integer, Integer>> step_statics_home_list = new ArrayList<Map.Entry<Integer, Integer>>(step_statics_home.entrySet());    
    	Collections.sort(step_statics_home_list, Utils.intKeyComparator);
		Utils.println(Utils.format("%-10s%-5s%5s", "操作","数量", "比例")); 
    	for(Map.Entry<Integer, Integer> entry : step_statics_home_list){
    		Utils.println(Utils.format("%-10s%-5s%5s", entry.getKey(), entry.getValue(), Utils.getFormat3Str(entry.getValue()/amountOfHomeBack)));
    	}
    	
    	Utils.println("................................");    	
    	Utils.println("双击退出前，操作数分布：总数"+amountOfDoubleBack);
    	Utils.println("................................");
    	List<Map.Entry<Integer, Integer>> step_statics_double_list = new ArrayList<Map.Entry<Integer, Integer>>(step_statics_double.entrySet());    
    	Collections.sort(step_statics_double_list, Utils.intKeyComparator);
		Utils.println(Utils.format("%-5s%-10s%5s", "操作步数","数量", "比例")); 
    	for(Map.Entry<Integer, Integer> entry : step_statics_double_list){
    		Utils.println(Utils.format("%-5s%-10s%10s", entry.getKey(), entry.getValue(), Utils.getFormat3Str(entry.getValue()/amountOfDoubleBack)));
    	}
    	
    	
    	
    	Utils.println(".................................");
    	Utils.println("操作步数分布：总数： "+deviceAmount);
    	Utils.println("................................");
    	List<Map.Entry<Integer, Integer>> step_statics_list = new ArrayList<Map.Entry<Integer, Integer>>(step_statics.entrySet()); 
    	Collections.sort(step_statics_list, Utils.intKeyComparator);
		Utils.println(Utils.format("%-10s%-10s%5s", "操作步数","数量", "比例")); 
    	for (Entry<Integer, Integer> entry : step_statics_list) {
			Utils.println(Utils.format("%-10s%-10s%-5s", entry.getKey(), entry.getValue(), Utils.getFormat2Str(entry.getValue()/deviceAmount)));
		}
    	
    	Utils.println(".................................");
    	Utils.println("最后一步操作分布比例：总数： "+deviceAmount);
    	Utils.println("................................");
    	List<Map.Entry<String, Integer>> last_action_statics_list = new ArrayList<Map.Entry<String, Integer>>(last_action_statics.entrySet());
    	Collections.sort(last_action_statics_list, Utils.strKeyComparator);
		Utils.println(Utils.format("%-20s%5s", "操作","比例")); 
    	for (Entry<String, Integer> entry : last_action_statics_list) {
			Utils.println(Utils.format("%-20s%-5s", entry.getKey()+"-"+Utils.getDescrib(entry.getKey(),fileNames), Utils.getFormat2Str(entry.getValue()/deviceAmount)));
		} 
    	
		double action_2_step = step_statics.get(2);    	
    	Utils.println(".................................");
    	Utils.println("在2步操作退出的情况下，第一个操作分布比例：总数： "+action_2_step);
    	Utils.println("................................");
    	List<Map.Entry<String, Integer>> action_exit_2_step_statics_list = new ArrayList<Map.Entry<String, Integer>>(action_exit_2_step_statics.entrySet());
    	Collections.sort(action_exit_2_step_statics_list, Utils.strKeyComparator);
		Utils.println(Utils.format("%-20s%5s", "操作","比例"));
    	for (Entry<String, Integer> entry : action_exit_2_step_statics_list) {
			Utils.println(Utils.format("%-20s%-5s", entry.getKey()+"-"+Utils.getDescrib(entry.getKey(),fileNames), Utils.getFormat2Str(entry.getValue()/action_2_step)));
		}
    	
    }


    private boolean readfile(String filepath) throws FileNotFoundException, IOException {
        try {

            File file = new File(filepath);
            if (file.isFile()) {
                Utils.print("文件信息:\n");
                Utils.print("文件路径=" + file.getAbsolutePath()+"\n");
                Utils.print("文件名=" + file.getName()+"\n");
                readFileByLines(file.getPath());

            } else if (file.isDirectory()) {
//                Utils.print("文件夹\n");
//                File[] fz = file.listFiles();
//                String[] filelist = file.list();
//                for (int i = 0; i < filelist.length; i++) {
//                    File readfile = new File(filepath + "\\" + filelist[i]);
//                    if (!readfile.isDirectory()) {
//                        Utils.print("path=" + readfile.getPath()+"\n");
//                        Utils.print("absolutepath=" + readfile.getAbsolutePath());
//                        Utils.print("name=" + readfile.getName());
//
//                        readFileByLines(readfile.getPath());
//                    } else if (readfile.isDirectory()) {
//                        readfile(filepath + "\\" + filelist[i]);
//                    }
//                }
            }

        } catch (Exception e) {
            if (Config.printDebugMsg) {
            	Utils.println("readfile()   Exception:" + e.getMessage());
			}
        }
        return true;
    }

	public void getFilter(String fileName) {
        try {
            File file = new File(fileName);
            if (file.isFile()) {
                Utils.print("文件信息:\n");
                Utils.print("文件路径=" + file.getAbsolutePath()+"\n");
                Utils.print("文件名=" + file.getName()+"\n");
                readGeneralByLine(uninstalledAndroidIdList, file);
            } 

        } catch (Exception e) {
            Utils.print("readfile()   Exception:" + e.getMessage());
        }		
	}
	
	private void readGeneralByLine(ArrayList<String> targetList, File file){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String readLineStr = null;
            while ((readLineStr = reader.readLine()) != null) 
            {
            	targetList.add(readLineStr);
            }
            Utils.print("卸载总数: "+targetList.size()+"\n");
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
            Utils.print("文件读取完成。\n");
        }		
	}
}
