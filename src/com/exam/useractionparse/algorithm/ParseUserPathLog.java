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
	
//	���ļ��н����õ����ַ���������С�	
    private ArrayList<String[]> actionPathList = new ArrayList<>();    
//  ����ͳ����Ϣ��  
    private ArrayList<Map<String, Integer>> actionStaticsOfPerStep = new ArrayList<>(MAX_USER_ACTION_STEP);
//	ͳ��HOME���˳�ǰ�������ֲ�������
    private Map<String, Integer> home_exit_static = new HashMap<String, Integer>();
//	ͳ��˫���˳�ǰ�������ֲ�������
    private Map<String, Integer> double_click_statics = new HashMap<>();
//	ͳ��HOME���˳�ǰ���Ѿ������Ĳ��ֲַ���
    private Map<Integer, Integer> step_statics_home = new HashMap<>();
//	ͳ��˫���˳�ǰ���Ѿ������Ĳ��ֲַ���
    private Map<Integer, Integer> step_statics_double = new HashMap<>();
//	��ͨͳ��һ�����ڵĲ������ֲ���
    private Map<Integer, Integer> step_statics = new HashMap<>();
//	���һ������ͳ�ơ�
    private Map<String, Integer> last_action_statics = new HashMap<>();
//  ͳ��2���������˳���ǰһ�������ķֲ�������
    private Map<String, Integer> action_exit_2_step_statics = new HashMap<>();
//	ж���û���android id
    private ArrayList<String> uninstalledAndroidIdList = new ArrayList<>();
//	��������ļ��ظ���android id�б�
    private ArrayList<String> android_list_distinct1 = new ArrayList<>();
    private ArrayList<String> android_list_distinct2 = new ArrayList<>();
    
    private void init(){
    	//MAX_USER_ACTION_TYPE = //Utils.MaxNum +1; // ע��һ��Ҫ������+1��Ϊʲô�������1����parse��ʱ�򣬵����η���columns�ĵ����е�ʱ��columns.get(6).size()�Ĵ�Сʼ��Ϊ2����Ϊ1������
    	Utils.print("����ACTION TYPEֵ�� "+MAX_USER_ACTION_TYPE+"\n");
        
        /**
         * columns��ʼ��������ݣ�
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
    	Utils.print("��ʼ����ɡ�\n");
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
     * ִ����ɺ�path = 
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
		            		Utils.println("�ظ�android id: "+androidid);					
						}
		            	android_list_distinct1.add(androidid);						
					}
				}else {
					// ��ӡ������newUserAction���涨�����Ϊ���š�
		            if (Config.printInvalidValue) {
		            	Utils.println("��Ч��: "+readLineStr+"\n");						
					}
				}
            }
            Utils.println("��Ч�豸�ۼ�����: "+actionPathList.size());
            Utils.println("��ǰ�ļ�Ч�豸�ۼ�: "+validPathCount);
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
            Utils.consolePrint("�ļ���ȡ��ɡ�\n");
        }
    }

    /** ͳ����ɺ���������£�
    * 0 <"1",10> <"4",22> <"5",33> ... <"37",55> <"EXITTAG",10>
    * 1 <"1",22> <"4",20> <"5",11> ... <"37",40> <"EXITTAG",0>
    * 2 <"1",0> <"4",1> <"3",0> ... <"37",0> <"EXITTAG",0>
    * ...
    * 99 <"1",0> <"2",0> <"3",0> ... <"37",0> <"EXITTAG",0>
    */
    /**
     * ��i=0:
     * 	map = <"1",10> <"4",22> <"5",33> ... <"37",55> <"EXITTAG",10>
     * 	total = ����Ԫ��ֵ�ĺ͡�
     *  ѭ����ӡ����Ԫ��ֵ��total�ĺ͡�
     *  ���ʣ�none�����ֵ�����ȷ���ģ���ô����ͳ��������ڣ�������Ϊ����ʼ�ն��ǲ���ģ���ֵ����path��������������û�����.
     */
    private void doStatics(String ...fileNames) {
    	Utils.consolePrint("��ʼ���������\n");
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
            
            // �ܶ�ͳ�ơ�
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
             * ���������һ�������˳��˶��ٱ������û���
             */
            sb.append("����"+i);
            sb.append(" remain��"+sum+" "+" ��һ���˳���"+(Utils.DECIMAL_FORMAT_2.format(exitBasedOnLastPercent * 100))+"%").append("(");
            lastSum = sum;

            // ÿ����Ϊ�ı������㡣
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
             * ������ԭ������
             */
            {
            	sum = 0;
            	deString = "";
//            	exitTotalBefore = 0; ��Ҫ����������һ�ε�ֵ��
            	exitTotalBeforeTemp = 0;    	
            	exitBasedOnLastStep = 0;
            	exitBasedOnLastPercent = 0.0;
            	total = 0.0;
            	percentOfPerActionAction = 0.0;
            	mapList.clear();
            	sb.setLength(0);  
            	theOthers = 0;
            	thOthersPercent = 0.0;
            } // end ������
        } // end for
    }

    // ����ٷֱ�
    private String myPercent(int many, int hundred) {
        String baifenbi = "";// ���ܰٷֱȵ�ֵ
        double baiy = many * 1.0;
        double baiz = hundred * 1.0;
        double fen = baiy / baiz;
        DecimalFormat df1 = new DecimalFormat("##.00%"); // ##.00%
        baifenbi = df1.format(fen);
        return baifenbi;
    }
    
//    path��������ַ���������ɵ��б������ļ��еõ�������path���ݡ�
//    �˺���֮ǰ��columns�������ǿյġ�columns����ͳ�Ƹ��������и��������Ĵ�����
//    ����ȡ��path�е����ݣ�
//    columns������к��д���ʲô���д������path�е�һ�У��������е�һ������ÿһ�д������ÿһ���������ͺ�������Ͷ�Ӧ��ͳ�Ƹ�����
//    ���path�е�ĳһ�еĳ���С��
//    �˺���ִ��֮��
    /**
     * path���������
     * [37, 5, 4]
     * [37]
     * [37, 5, 7, 14, 14, 21, 25, 5, 5, 8, 9, 6]
     * ...
     */
//    ����path�����������һ��һ�н����ġ�
    /**
     * ��j = 0: 
     * 	j < 3; 
     * 		if(columns[0].contains[37]) 
     * 			columns[0][37].count++;
     * ��j = 1
     * 	j <��
     * 		if(columns[0].contains[5])
     * 			columns[1][5].count++;
     * ��j = 2
     * 	j <��
     * 		if(columns[0].contains[5])
     * 			columns[2][4].count++;
     * ��j=3: 
     * 	j<3��������
     * 		if(columns[0].containsKey("EXITTAG"))
     * 			columns[0]["EXITTAG"].count++;
     */
    
    /**
     * columns���ݣ�
     * 0 <"1",0> <"4",0> <"5",0> ... <"37",3> <"EXITTAG",0>
     * 1 <"1",0> <"4",0> <"5",2> ... <"37",0> <"EXITTAG",0>
     * 2 <"1",0> <"4",1> <"3",0> ... <"37",0> <"EXITTAG",0>
     * ...
     * 99 <"1",0> <"2",0> <"3",0> ... <"37",0> <"EXITTAG",3>
     */
    /**
     * ������������ʲô��
     * ��j�У������j�β�������j����������ݣ����������û��ڽ��е�j�β����У������������ݵĴ����ܺ͡���j�е����м�������������path�����������û�����
     * ͳ����ɺ���������£�
     * 0 <"1",10> <"4",22> <"5",33> ... <"37",55> <"EXITTAG",10>
     * 1 <"1",22> <"4",20> <"5",11> ... <"37",40> <"EXITTAG",0>
     * 2 <"1",0> <"4",1> <"3",0> ... <"37",0> <"EXITTAG",0>
     * ...
     * 99 <"1",0> <"2",0> <"3",0> ... <"37",0> <"EXITTAG",0>
     */
    private void generateColumns() {
    	Utils.println("��ʼ����...");
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
        Utils.consolePrint("ͳ�Ʊ�");
        Utils.printTable(actionStaticsOfPerStep);
        Utils.print("����� = "+maxLength+" ���������: \n");
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
    				// ��ͨͳ������home���˳�ǰ�Ĳ����ֲ���
					home_exit_static.put(key_before_back, Utils.getValueOfMap(home_exit_static,key_before_back) + 1);
					// ͳ��ÿһ���û���home���˳�ǰ����������
					step_statics_home.put(count_before_back, Utils.getValueOfMap(step_statics_home, count_before_back) + 1);	
					amountOfHomeBack++;
					break;
				}else if (key.equals(DOUBLE_CLICK_BACK_KEY)) {
//					Utils.print(Utils.getArrayStr(keyArray)+"\n");
					// ��ͨͳ������double���˳�ǰ�Ĳ����ֲ���
					double_click_statics.put(key_before_back, Utils.getValueOfMap(double_click_statics,key_before_back) + 1);
					// ͳ��ÿһ���û���double���˳�ǰ�������ֲ���
					step_statics_double.put(count_before_back, Utils.getValueOfMap(step_statics_double, count_before_back) + 1);
					amountOfDoubleBack++;
//					if (count_before_back == 32) {
//						Utils.print("44֮ǰΪ32�Ĳ������У� " + Utils.getArrayStr(keyArray)+"\n");
//					}
					break;
				}else {
					key_before_back = key;
					count_before_back++;
				}
    		}
    	}
    	
    	Utils.println("................................"); 
    	Utils.println("HOME���˳�ǰ�������ֲ�������"+amountOfHomeBack);
    	Utils.println("................................");
    	List<Map.Entry<String, Integer>> home_exit_static_list = new ArrayList<Map.Entry<String, Integer>>(home_exit_static.entrySet());
    	Collections.sort(home_exit_static_list, Utils.strKeyComparator);
    	Iterator<Entry<String, Integer>> iterator = home_exit_static.entrySet().iterator();
		Utils.println(Utils.format("%-20s%5s", "����","����"));    
    	for(Map.Entry<String, Integer> entry : home_exit_static_list){
    		Utils.println(Utils.format("%-20s%5s", entry.getKey() + "-" + Utils.getDescrib(entry.getKey(),fileNames), Utils.getFormat3Str(entry.getValue()/amountOfHomeBack)));    		
		};
    	
    	Utils.println("................................");    	
    	Utils.println("˫���˳�ǰ�������ֲ�������"+amountOfDoubleBack);
    	Utils.println("................................");
    	List<Map.Entry<String, Integer>> double_click_statics_list = new ArrayList<Map.Entry<String, Integer>>(double_click_statics.entrySet());    
    	Collections.sort(double_click_statics_list, Utils.strKeyComparator);
		Utils.println(Utils.format("%-20s%5s", "����","����"));    
    	for(Map.Entry<String, Integer> entry : double_click_statics_list){
    		Utils.println(Utils.format("%-20s%5s", entry.getKey() + "-" + Utils.getDescrib(entry.getKey(),fileNames), Utils.getFormat3Str(entry.getValue()/amountOfDoubleBack)));
    	}
    	
    	Utils.println("................................");    	
    	Utils.println("HOME���˳�ǰ���������ֲ�������:"+amountOfHomeBack);
    	Utils.println("................................");
    	List<Map.Entry<Integer, Integer>> step_statics_home_list = new ArrayList<Map.Entry<Integer, Integer>>(step_statics_home.entrySet());    
    	Collections.sort(step_statics_home_list, Utils.intKeyComparator);
		Utils.println(Utils.format("%-10s%-5s%5s", "����","����", "����")); 
    	for(Map.Entry<Integer, Integer> entry : step_statics_home_list){
    		Utils.println(Utils.format("%-10s%-5s%5s", entry.getKey(), entry.getValue(), Utils.getFormat3Str(entry.getValue()/amountOfHomeBack)));
    	}
    	
    	Utils.println("................................");    	
    	Utils.println("˫���˳�ǰ���������ֲ�������"+amountOfDoubleBack);
    	Utils.println("................................");
    	List<Map.Entry<Integer, Integer>> step_statics_double_list = new ArrayList<Map.Entry<Integer, Integer>>(step_statics_double.entrySet());    
    	Collections.sort(step_statics_double_list, Utils.intKeyComparator);
		Utils.println(Utils.format("%-5s%-10s%5s", "��������","����", "����")); 
    	for(Map.Entry<Integer, Integer> entry : step_statics_double_list){
    		Utils.println(Utils.format("%-5s%-10s%10s", entry.getKey(), entry.getValue(), Utils.getFormat3Str(entry.getValue()/amountOfDoubleBack)));
    	}
    	
    	
    	
    	Utils.println(".................................");
    	Utils.println("���������ֲ��������� "+deviceAmount);
    	Utils.println("................................");
    	List<Map.Entry<Integer, Integer>> step_statics_list = new ArrayList<Map.Entry<Integer, Integer>>(step_statics.entrySet()); 
    	Collections.sort(step_statics_list, Utils.intKeyComparator);
		Utils.println(Utils.format("%-10s%-10s%5s", "��������","����", "����")); 
    	for (Entry<Integer, Integer> entry : step_statics_list) {
			Utils.println(Utils.format("%-10s%-10s%-5s", entry.getKey(), entry.getValue(), Utils.getFormat2Str(entry.getValue()/deviceAmount)));
		}
    	
    	Utils.println(".................................");
    	Utils.println("���һ�������ֲ������������� "+deviceAmount);
    	Utils.println("................................");
    	List<Map.Entry<String, Integer>> last_action_statics_list = new ArrayList<Map.Entry<String, Integer>>(last_action_statics.entrySet());
    	Collections.sort(last_action_statics_list, Utils.strKeyComparator);
		Utils.println(Utils.format("%-20s%5s", "����","����")); 
    	for (Entry<String, Integer> entry : last_action_statics_list) {
			Utils.println(Utils.format("%-20s%-5s", entry.getKey()+"-"+Utils.getDescrib(entry.getKey(),fileNames), Utils.getFormat2Str(entry.getValue()/deviceAmount)));
		} 
    	
		double action_2_step = step_statics.get(2);    	
    	Utils.println(".................................");
    	Utils.println("��2�������˳�������£���һ�������ֲ������������� "+action_2_step);
    	Utils.println("................................");
    	List<Map.Entry<String, Integer>> action_exit_2_step_statics_list = new ArrayList<Map.Entry<String, Integer>>(action_exit_2_step_statics.entrySet());
    	Collections.sort(action_exit_2_step_statics_list, Utils.strKeyComparator);
		Utils.println(Utils.format("%-20s%5s", "����","����"));
    	for (Entry<String, Integer> entry : action_exit_2_step_statics_list) {
			Utils.println(Utils.format("%-20s%-5s", entry.getKey()+"-"+Utils.getDescrib(entry.getKey(),fileNames), Utils.getFormat2Str(entry.getValue()/action_2_step)));
		}
    	
    }


    private boolean readfile(String filepath) throws FileNotFoundException, IOException {
        try {

            File file = new File(filepath);
            if (file.isFile()) {
                Utils.print("�ļ���Ϣ:\n");
                Utils.print("�ļ�·��=" + file.getAbsolutePath()+"\n");
                Utils.print("�ļ���=" + file.getName()+"\n");
                readFileByLines(file.getPath());

            } else if (file.isDirectory()) {
//                Utils.print("�ļ���\n");
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
                Utils.print("�ļ���Ϣ:\n");
                Utils.print("�ļ�·��=" + file.getAbsolutePath()+"\n");
                Utils.print("�ļ���=" + file.getName()+"\n");
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
            Utils.print("ж������: "+targetList.size()+"\n");
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
            Utils.print("�ļ���ȡ��ɡ�\n");
        }		
	}
}
