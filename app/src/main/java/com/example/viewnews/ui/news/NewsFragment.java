package com.example.viewnews.ui.news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.viewnews.MyApplication;
import com.example.viewnews.logic.ThreadPool;
import com.example.viewnews.logic.dao.NewsData;
import com.example.viewnews.R;
import com.example.viewnews.logic.dao.NewsCache;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

//每个tab下的碎片Fragment
public class NewsFragment extends Fragment {
    //新闻列表
    private RecyclerView newsRecyclerView;
    //下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;
    //新闻子项
    private List<NewsData> contentItems = new ArrayList<>();

    //用来保存当前tab的名字
    private String currentTabName = "top";

    //每一个Fragment页面都有一个浮动按钮，用于快速回到顶部
    private FloatingActionButton fab;

    private  NewsViewModel viewModel;
    private NewsTabAdapter newsTabAdapter;

    //创建Fragment被添加到活动中时回调，且只会被调用一次
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //每次创建，绘制该Fragment的View组件时回调，将显示的View返回
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载新闻列表
        View view = inflater.inflate(R.layout.news_rlist, container, false);//这里需要改下布局
        //获取每个实例之后，返回当前视图

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);

        newsRecyclerView = view.findViewById(R.id.newsRecyclerView);//设置RecyclerView的布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyApplication.getContext());
        newsRecyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    //当NewsFragment所在的Activity启动完成后调用，声明周期紧接在onCreateView()之后

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //onAttach(getActivity());//该方法已弃用，查看源码即可
        onAttach(getContext());

        viewModel = new ViewModelProvider(this).get(NewsViewModel.class);//只有一个Viewmodel
        //viewModel = new NewsViewModel();
        NewsCache newsCache = NewsCache.getInstance(getContext());
        newsTabAdapter = new NewsTabAdapter(getContext());

        viewModel.setNewsCache(newsCache);

        Log.d("上下文：", "Context: " + getContext());
        Log.d("NewsFragment", "Activity: " + getActivity());
        //获取键对应的值，参数2表示默认填充的值，其用法和Intent差不多，但又有区别
        //data = bundle.getString("name", "top");
        //tv.setText(data);
        Bundle bundle = getArguments();
        final String category = bundle.getString("name", "top");
        //final String category = type;
        viewModel.setType(category);//设置viewModel标题

        currentTabName = category;
        Log.d("NewsFragment", "点击的标题是 ： " + category);

        //实现置顶功能
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsRecyclerView.smoothScrollToPosition(0);
            }
        });
        //实现下拉刷新的功能
        //设置下拉刷新进度条的颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorRed);
        //实现下拉刷新的监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ThreadPool.getExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("NewsFragment","调用了addCount");
                        viewModel.addCount();
                    }
                });

            }
        });

        viewModel.getNewsLiveData().observe(getViewLifecycleOwner(), new Observer<List<NewsData>>() {
            @Override
            public void onChanged(List<NewsData> list) {
                Log.d("NewsFragment","更新列表");

                if(newsTabAdapter != null){
                    Log.d("NewsFragment","adapter is not null");
                    newsTabAdapter.setNewsDataList(list);
                }
                else {
                    newsTabAdapter = new NewsTabAdapter(list,getContext());
                }

                newsRecyclerView.setAdapter(newsTabAdapter);
                newsTabAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
}