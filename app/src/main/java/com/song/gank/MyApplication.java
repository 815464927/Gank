package com.song.gank;

import android.app.Application;
import android.os.Environment;

import com.song.gank.consts.Consts;

import java.io.File;

/**
 * Created by song on 2017/3/4.
 * Email：815464927@qq.com
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initPath();
    }

    private void initPath() {
        File appPath = new File(Environment.getExternalStorageDirectory(),"gank");
        if(!appPath.exists()){
            appPath.mkdir();
        }
        Consts.appPath = appPath;
    }
}
