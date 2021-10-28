package com.example.viewnews.logic.network.news;
/*
 * @Author Lxf
 * @Date 2021/9/8 9:48
 * @Description 获取网络数据的工作类，仅对数据做简单处理
 * @Since version-1.0
 */

import android.util.Log;

import com.example.viewnews.MyApplication;
import com.example.viewnews.logic.dao.NewsData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Response;

public class NewsNetWork {
    //private static String type;//传入的类型
    private static final NewsService newsService = NewsServiceCreator.Create(NewsService.class);
    private static NewsResponse result = null;
    private static List<NewsData> list = null;

    
    public static NewsResponse getNewsFromNet(String type){//可能为空
        try {
            Response<NewsResponse > response = newsService.
                    getNewsList(type, MyApplication.key).execute();
            if(response.body() != null) {
                result = response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return result;
        }
    }

    //对newsResponse 进行解析
    public static List<NewsData> getNewsDataList(NewsResponse newsResponse){
        if(newsResponse == null){
            Log.d("NewsNetWork","newsResponse is null ");
            return new ArrayList<>();
        }
        if(newsResponse.getResult() == null){
            Log.d("NewsNetWork","newsResponse.result is null ");
            return new ArrayList<>();
        }
        if(newsResponse.getResult().getDataList() != null){
            list = newsResponse.getResult().getDataList();
        }
        return  list;
    }



}
