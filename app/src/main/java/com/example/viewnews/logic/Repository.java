package com.example.viewnews.logic;
/*
 * @Author Lxf
 * @Date 2021/9/21 17:05
 * @Description 仓库类，数据访问接口，这里完成网络或者本地获取数据的封装
 *
 * ps:由于要创建多个fragment，这个类如果是单例类的时候，好像会出现问题
 * @Since version-1.0
 */

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.viewnews.logic.dao.NewsSql;
import com.example.viewnews.logic.network.news.NewsNetWork;
import com.example.viewnews.logic.network.news.NewsResponse;
import com.example.viewnews.logic.dao.NewsData;
import com.example.viewnews.logic.dao.NewsCache;

import java.util.List;

public class Repository {
    private  MutableLiveData<List<NewsData>> newsLiveData = new MutableLiveData<>();
    private List<NewsData> newsList;


    public MutableLiveData<List<NewsData>> refreshData(String type,int count,NewsCache newsCache){//本地刷新
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        newsList = NewsSql.getNewsList(type,count,newsCache);

                        Log.d("Repository","刷新咯");
                        Log.d("Repository","type is " + type);
                        if(newsList == null || newsList.size() == 0){//本地数据出错了,请求网络数据,放进库里，再进行本地查询
                            //原因很可能是索引太大了
                            int new_count = 0 ;
                            Log.d("Repository","数据出错咯");
                            newsList = NewsSql.getNewsList(type,new_count,newsCache);
                            getNewsFromNet(type,newsCache);
                        }
                        newsLiveData.postValue(newsList);
                    }
                }).start();

        return newsLiveData;
    }

    public void getNewsFromNet(String type,NewsCache cache){//请求网络数据，放进库里，进行差错检测和校验
        NewsResponse response = NewsNetWork.getNewsFromNet(type);
        newsList = NewsNetWork.getNewsDataList(response);
        if (newsList.size() != 0 ){
            NewsSql.saveOnHost(newsList,type,cache);
        }
        else {
            Log.d("Repository","获取的新闻列表为空");
        }
    }

}
