package com.example.viewnews.ui.news;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.viewnews.logic.Repository;
import com.example.viewnews.logic.ThreadPool;
import com.example.viewnews.logic.dao.NewsData;
import com.example.viewnews.logic.dao.NewsCache;

import java.util.List;

public class NewsViewModel extends ViewModel {

    private int count ;
    private String type;
    private NewsCache newsCache ;
    private final Repository repository = new Repository();
    private MutableLiveData<Integer> countLivaData = new MutableLiveData<>();//计数器

    private  LiveData<List<NewsData>> newsLiveData = //new MutableLiveData<>();
    Transformations.switchMap
            (countLivaData, count ->repository.refreshData(type,count,newsCache));
            //用于执行本地刷新*/



    //需保证该方法运行在子线程中
    public void addCount(){
        count++;
        Log.d("NewsViewModel","count is "+ count);
        Log.d("ThreadTest","addCount run in " + Thread.currentThread().getName());
        countLivaData.postValue(count);

        if(count >= 2){//再开子进程，向网络请求数据,并把count置为0
            count = -1;
            ThreadPool.getExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(700);//这里延迟一定的时间，防止发生冲突
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("ThreadTest","getNewsFromNet run in " + Thread.currentThread().getName());
                    repository.getNewsFromNet(type,newsCache);
                }
            });
        }
    }

    //下面全是get和set方法

    public String getType() {
        return type;
    }

    //这个方法一般运行在主线程中
    public void setType(String type) {
        count = 1;
        this.type = type;
        Log.d("NewsViewModel","set count is " + count);
        countLivaData.setValue(count);
    }

    public LiveData<List<NewsData>> getNewsLiveData() {
        return newsLiveData;
    }

    public  void setNewsCache(NewsCache newsCache) {
        this.newsCache = newsCache;
    }
}
