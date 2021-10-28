package com.example.viewnews.ui.other;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewnews.R;
import com.example.viewnews.BaseActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TopicActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View topicView= LayoutInflater.from(TopicActivity.this).inflate(R.layout.activity_topic,null);
        //新建话题按钮的注册
        FloatingActionButton button=topicView.findViewById(R.id.topic_fab);
        button.setOnClickListener(this);

        //recyclerview即话题列表的初始化
        //ToDo 利用后台数据初始化话题列表，此处暂时用循环代替
        List list = new ArrayList<>();
        for (int i=0;i<10;i++){
            list.add("这是第"+i+"个测试话题标题");
        }
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(this,list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recyclerView=topicView.findViewById(R.id.topic_recyclerview);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        //toolbar初始化
        toolbar=topicView.findViewById(R.id.topic_toolbar);
        setSupportActionBar(toolbar);

        setContentView(topicView);
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(TopicActivity.this);
        builder.setTitle("请输入话题标题");
        builder.setIcon(R.drawable.news_comment);
        builder.setView(new EditText(TopicActivity.this));
        builder.setPositiveButton("提交",null);
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topic_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
        if(item.getItemId()==R.id.topic_join){
            //todo
            final EditText et = new EditText(this);
            AlertDialog.Builder builder=new AlertDialog.Builder(TopicActivity.this);
            builder.setTitle("请输入想加入的话题序号");
            builder.setIcon(R.drawable.news_comment);
            builder.setView(et);
            builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //ToDo 跳转至topic详情页面
                    String topicNum=et.getText().toString();
                    Intent intent=new Intent(TopicActivity.this, TopicDetailActivity.class);
                    intent.putExtra("topicNum",topicNum);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("取消",null);
            builder.show();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
