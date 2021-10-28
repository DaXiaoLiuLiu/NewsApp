package com.example.viewnews.ui.news.collection;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.viewnews.R;
import com.example.viewnews.logic.ThreadPool;
import com.example.viewnews.logic.dao.NewsData;
import com.example.viewnews.logic.dao.NewsCache;
import com.example.viewnews.ui.news.NewsTabAdapter;
import com.example.viewnews.BaseActivity;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

public class UserFavoriteActivity extends BaseActivity {

    //private ListView favoriteNewsList;//这个是列表
    private RecyclerView favoriteNewsList;

    private List<NewsData> dataList;

    private Toolbar favoriteToolbar;
    private String userIdNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MMKV.initialize(this);//获取mmkv
        MMKV mmkv = MMKV.defaultMMKV();

        setContentView(R.layout.activity_user_favorite);
        favoriteNewsList = findViewById(R.id.favorite_newsList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        favoriteNewsList.setLayoutManager(layoutManager);

        favoriteToolbar = findViewById(R.id.userFavorite_toolbar);
        favoriteToolbar.setTitle("我的收藏");
        setSupportActionBar(favoriteToolbar);
        //initNews();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_return_left);
        }


    }


    @Override
    protected void onStart() {
        super.onStart();

        MMKV mmkv = MMKV.defaultMMKV();
        userIdNumber = mmkv.decodeString("name","");
        Log.d("UserFavorite","收藏：当前用户的id为" + userIdNumber);
        if(TextUtils.isEmpty(userIdNumber)){
            Toast.makeText(this,"请登录",Toast.LENGTH_SHORT).show();
            dataList = new ArrayList<>();
        }
        else {

            ThreadPool.getExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    dataList = NewsCache.getInstance(UserFavoriteActivity.this).newsDao().
                            getFavNews(userIdNumber);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NewsTabAdapter tabAdapter = new NewsTabAdapter(dataList,getApplicationContext());
                            favoriteNewsList.setAdapter(tabAdapter);
                            tabAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });

        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                UserFavoriteActivity.this.finish();
                break;
        }
        return true;
    }


}