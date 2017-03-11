package com.song.gank.utils;

import android.util.Log;

import com.song.gank.consts.Consts;


/**
 * Created by song on 2017/2/3.
 * Email：815464927@qq.com
 */

public class LogUtils {

    public static void debug(String tag ,String message){
        if(Consts.isLog) {
            Log.d("--->"+tag, message.length()>0?message:"null");
        }
    }

}
