package com.example.viewnews;
/*
 * @Author Lxf
 * @Date 2021/9/15 23:22
 * @Description 全局Context获取的帮助类，还包含其他一些静态全局变量
 * @Since version-1.0
 */

import android.app.Application;
import android.content.Context;

import com.tencent.mmkv.MMKV;

public class MyApplication extends Application {

    private static Context context;
    public static final String key = "af2d37d2ed31f7a074f1d49b5460a0b5";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

}
