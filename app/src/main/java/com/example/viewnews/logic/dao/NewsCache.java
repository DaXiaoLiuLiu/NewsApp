package com.example.viewnews.logic.dao;
/*
 * @Author Lxf
 * @Date 2021/9/13 16:41
 * @Description room 数据库单例类的标准写法
 * @Since version-1.0
 */

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NewsData.class},version = 1)
public abstract class NewsCache extends RoomDatabase {
    public static NewsCache INSTANCE;
    private static final Object sLock = new Object();
    public abstract NewsDao newsDao();//用于获取dao实例的抽象方法

    public static NewsCache getInstance(Context context){
        synchronized (sLock){
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),NewsCache.class,"news.db")
                        .build();
            }
            return INSTANCE;
        }
    }
}
