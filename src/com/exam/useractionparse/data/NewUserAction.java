package com.exam.useractionparse.data;

import java.util.HashMap;

/**
 * Created by ytq on 2017/1/25.
 */
public class NewUserAction {
    //new user action key begin
    public static final int ACTION_INTERSTITIAL_AD_SHOW = 1;
    public static final int ACTION_INTERSTITIAL_AD_CLICK = 2;
    public static final int ACTION_AD_CLICK = 3;

    public static final int ACTION_BOOST_TAB = 4;
    public static final int ACTION_CLEAN_TAB = 5;
    public static final int ACTION_SECURITY_TAB = 6;

    public static final int ACTION_WIFI_SCAN = 7;
    public static final int ACTION_FULL_SCAN = 8;
    public static final int ACTION_PRIVACY_CLEAN = 9;
    public static final int ACTION_ENTER_APPLOCK = 10;
    public static final int ACTION_BOOST = 11;
    public static final int ACTION_BATTERY_SAVE = 12;
    public static final int ACTION_COOLER = 13;
    public static final int ACTION_BATTERY_INSTANT = 14;
    public static final int ACTION_CLEAN = 15;
    public static final int ACTION_CLEAN_SYSTEM_JUNK = 16;
    public static final int ACTION_POWER_BOOST = 17;
    public static final int ACTION_NETWORK = 18;
    public static final int ACTION_CPU = 19;

    public static final int ACTION_HOME_KEY_PRESS = 20;
    public static final int ACTION_DOUBLE_BACK = 21;

    public static final int ACTION_SHOW_CHARGING_PAGE = 22;
    public static final int ACTION_SHOW_SECURITY_MONITOR_PAGE = 23;

    public static final int ACTION_QUIT_SECURITY_SCAN = 24;
    public static final int ACTION_QUIT_BOOST = 25;
    public static final int ACTION_QUIT_CLEAN = 26;
    public static final int ACTION_QUIT_COOLER = 27;
    public static final int ACTION_QUIT_BATTERY_SAVE = 28;
    public static final int ACTION_QUIT_SYSTEM_CACHE_CLEAN = 29;
    public static final int ACTION_QUIT_HIBERTNATE_APP = 30;

    public static final int ACTION_SHOW_DIALOG_IN_MAIN_PAGE = 31;
    public static final int ACTION_SHOW_SECURITY_MONITOR_PAGE2 = 32;

    //new user action key end

    public static String getActionDes(int action) {
        if (actionDes.keySet().contains(action)) {
            return actionDes.get(action);
        } else {
            return "des not found :" + action;
        }
    }

    public static final HashMap<Integer, String> actionDes = new HashMap() {
        {
            put(ACTION_AD_CLICK, "������");
            put(ACTION_WIFI_SCAN, "���WIFIɨ��");
            put(ACTION_FULL_SCAN, "���FULLɨ��");
            put(ACTION_PRIVACY_CLEAN, "�����˽����");
            put(ACTION_ENTER_APPLOCK, "����APP LOCKER");
            put(ACTION_SECURITY_TAB, "���밲ȫҳ��");
            put(ACTION_BOOST_TAB, "�������ҳ��");
            put(ACTION_BOOST, "�������");
            put(ACTION_BATTERY_SAVE, "���ʡ��");
            put(ACTION_COOLER, "�������");
            put(ACTION_CLEAN, "�������");
            put(ACTION_BATTERY_INSTANT, "�����ʱ�ĵ�");
            put(ACTION_NETWORK, "�����ʱ����");
            put(ACTION_CPU, "�����ʱCPU");
            put(ACTION_HOME_KEY_PRESS, "���HOME����");
            put(ACTION_DOUBLE_BACK, "˫�����˼�");
            put(ACTION_QUIT_BOOST, "�������˳�");
            put(ACTION_QUIT_CLEAN, "�������˳�");
            put(ACTION_QUIT_COOLER, "�������˳�");
            put(ACTION_QUIT_BATTERY_SAVE, "ʡ�����˳�");
            put(ACTION_QUIT_SYSTEM_CACHE_CLEAN, "����ϵͳ�������˳�");
            put(ACTION_QUIT_HIBERTNATE_APP, "PowerBoost�˳�");
            put(ACTION_QUIT_SECURITY_SCAN, "��ȫɨ�����˳�");
            put(ACTION_SHOW_CHARGING_PAGE, "��ʾ���ҳ��");
            put(ACTION_SHOW_SECURITY_MONITOR_PAGE, "��ʾʵʱ����ҳ��");
            put(ACTION_SHOW_SECURITY_MONITOR_PAGE2, "��ʾ�߼�����ҳ��");
            put(ACTION_CLEAN_TAB, "����������ҳ��");
            put(ACTION_CLEAN_SYSTEM_JUNK, "ϵͳ����ҳ��");
            put(ACTION_POWER_BOOST, "ǿ������ҳ��");
            put(ACTION_INTERSTITIAL_AD_SHOW, "��ʾ�������");
            put(ACTION_INTERSTITIAL_AD_CLICK, "����������");
            put(ACTION_SHOW_DIALOG_IN_MAIN_PAGE, "�˳������ܵ���");
        }
    };
}
