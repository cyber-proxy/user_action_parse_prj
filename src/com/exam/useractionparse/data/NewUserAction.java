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
            put(ACTION_AD_CLICK, "点击广告");
            put(ACTION_WIFI_SCAN, "点击WIFI扫描");
            put(ACTION_FULL_SCAN, "点击FULL扫描");
            put(ACTION_PRIVACY_CLEAN, "点击隐私清理");
            put(ACTION_ENTER_APPLOCK, "进入APP LOCKER");
            put(ACTION_SECURITY_TAB, "进入安全页面");
            put(ACTION_BOOST_TAB, "进入加速页面");
            put(ACTION_BOOST, "点击加速");
            put(ACTION_BATTERY_SAVE, "点击省电");
            put(ACTION_COOLER, "点击降温");
            put(ACTION_CLEAN, "点击清理");
            put(ACTION_BATTERY_INSTANT, "点击即时耗电");
            put(ACTION_NETWORK, "点击即时网络");
            put(ACTION_CPU, "点击即时CPU");
            put(ACTION_HOME_KEY_PRESS, "点击HOME键盘");
            put(ACTION_DOUBLE_BACK, "双击回退键");
            put(ACTION_QUIT_BOOST, "加速中退出");
            put(ACTION_QUIT_CLEAN, "清理中退出");
            put(ACTION_QUIT_COOLER, "降温中退出");
            put(ACTION_QUIT_BATTERY_SAVE, "省电中退出");
            put(ACTION_QUIT_SYSTEM_CACHE_CLEAN, "清理系统缓存中退出");
            put(ACTION_QUIT_HIBERTNATE_APP, "PowerBoost退出");
            put(ACTION_QUIT_SECURITY_SCAN, "安全扫描中退出");
            put(ACTION_SHOW_CHARGING_PAGE, "显示充电页面");
            put(ACTION_SHOW_SECURITY_MONITOR_PAGE, "显示实时防护页面");
            put(ACTION_SHOW_SECURITY_MONITOR_PAGE2, "显示高级防护页面");
            put(ACTION_CLEAN_TAB, "进入清理主页面");
            put(ACTION_CLEAN_SYSTEM_JUNK, "系统缓存页面");
            put(ACTION_POWER_BOOST, "强力清理页面");
            put(ACTION_INTERSTITIAL_AD_SHOW, "显示插屏广告");
            put(ACTION_INTERSTITIAL_AD_CLICK, "点击插屏广告");
            put(ACTION_SHOW_DIALOG_IN_MAIN_PAGE, "退出带功能弹窗");
        }
    };
}
