package com.example.viewnews.ui.other;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewnews.R;
import com.example.viewnews.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class TopicDetailActivity extends BaseActivity implements View.OnClickListener {
    EditText editText=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        String topicNum=intent.getStringExtra("topicNum");

        View topicDetailView= LayoutInflater.from(TopicDetailActivity.this).inflate(R.layout.activity_topic_detail,null);
        //按钮注册
        Button button=topicDetailView.findViewById(R.id.topic_comment_submit);
        button.setOnClickListener(this);
        //输入框绑定
        editText=topicDetailView.findViewById(R.id.topic_comment_field);
        //recyclerview即话题内容的初始化
        //ToDo 利用后台数据初始化话题内容，此处暂时用循环代替
        List list = new ArrayList<>();
        for (int i=0;i<10;i++){
            list.add("这是第"+i+"个测试话题标题");
        }
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(this,list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recyclerView=topicDetailView.findViewById(R.id.topic_detail_recyclerview);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        setContentView(topicDetailView);
    }

    @Override
    public void onClick(View view) {
        //ToDo 将edittext内容传至服务器
    }
}
