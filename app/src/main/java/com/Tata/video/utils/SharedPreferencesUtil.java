package com.Tata.video.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.Tata.video.AppContext;

/**
 * Created by cxf on 2017/8/3.
 * 保存登录后的uid和token
 */

public class SharedPreferencesUtil {

    private SharedPreferences mSharedPreferences;

    private static SharedPreferencesUtil sInstance;
    private final String UID = "uid";
    private final String TOKEN = "token";
    private final String JIM_LOGIN = "jimLogin";
    private final String SEARCH_HISTORY = "searchHistory";
    private final String USER_BEAN = "userBean";
    private  final  String ADVERTISMENT_STATE="advertisment_state";
    public  final  String WALLET_COIN="wallet_coin";
    public  final  String WALLET_RMB="wallet_rmb";
    public  final  String WALLET_TICKET="wallet_ticket";

    private SharedPreferencesUtil() {
        mSharedPreferences = AppContext.sInstance.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
    }

    public static SharedPreferencesUtil getInstance() {
        if (sInstance == null) {
            synchronized (SharedPreferencesUtil.class) {
                if (sInstance == null) {
                    sInstance = new SharedPreferencesUtil();
                }
            }
        }
        return sInstance;
    }

    public void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear().commit();
    }

    /**
     * 在登录成功之后返回uid和token
     *
     * @param uid
     * @param token
     */
    public void saveUidAndToken(String uid, String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(UID, uid);
        editor.putString(TOKEN, token);
        editor.commit();
    }

    /**
     * 返回保存在本地的uid和token
     *
     * @return 以字符串数组形式返回uid和token
     */
    public String[] readUidAndToken() {
        String uid = mSharedPreferences.getString(UID, "");
        if ("".equals(uid)) {
            return null;
        }
        String token = mSharedPreferences.getString(TOKEN, "");
        if ("".equals(token)) {
            return null;
        }
        return new String[]{uid, token};
    }

    /**
     * 读取私信登录状态
     */
    public boolean readEMLoginStatus() {
        return mSharedPreferences.getBoolean(JIM_LOGIN, false);
    }

    //保存私信登录状态
    public void saveEMLoginStatus(boolean login) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(JIM_LOGIN, login);
        editor.commit();
    }


    //保存搜索记录
    public void saveSearchHistory(String searchHistory) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(SEARCH_HISTORY, searchHistory);
        editor.commit();
    }

    //读取搜索记录
    public String readSearchHistory() {
        return mSharedPreferences.getString(SEARCH_HISTORY, "");
    }

    //保存用户信息
    public void saveUserBeanJson(String userBeanJsonString) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_BEAN, userBeanJsonString);
        editor.commit();
    }

    //读取用户信息
    public String readUserBeanJson() {
        return mSharedPreferences.getString(USER_BEAN, "");
    }

    //保存广告栏状态
    public   void saveAdvertismentState(boolean state){
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.putBoolean(ADVERTISMENT_STATE,state);
        editor.commit();
    }
    //获取广告栏状态
    public  boolean getAdvertismentState(){
        return  mSharedPreferences.getBoolean(ADVERTISMENT_STATE,true);
    }


    //保存钱包信息
    public void saveWallet(double coin ,double rmb ,double ticket){

        SharedPreferences.Editor editor=mSharedPreferences.edit();
         editor.putString(WALLET_COIN,String.valueOf(coin));
         editor.putString(WALLET_RMB,String.valueOf(rmb));
         editor.putString(WALLET_TICKET,String.valueOf(ticket));
         editor.commit();
    }

    public  String getWalletRmb(){
        return  mSharedPreferences.getString(WALLET_RMB,null);
    }
    public  String getWALLET_COIN(){
        return  mSharedPreferences.getString(WALLET_COIN,null);
    }

}
