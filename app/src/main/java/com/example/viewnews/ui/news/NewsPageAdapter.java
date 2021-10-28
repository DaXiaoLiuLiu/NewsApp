package com.example.viewnews.ui.news;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewsPageAdapter extends FragmentStateAdapter {
    private List<String> list;

    public NewsPageAdapter( FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public NewsPageAdapter( FragmentActivity fragmentActivity,List<String> list) {
        super(fragmentActivity);
        this.list = list;
    }

    public NewsPageAdapter( Fragment fragment) {
        super(fragment);
    }

    public NewsPageAdapter( FragmentManager fragmentManager,  Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        Fragment newsFragment = new NewsFragment();
        //判断所选的标题，进行传值显示
        //Bundle主要用于传递数据；它保存的数据，是以key-value(键值对)的形式存在的。
        //详细讲解：https://blog.csdn.net/yiranruyuan/article/details/78049219
        Bundle bundle = new Bundle();
        Log.d("NewsPageAdapter","fragment type is " + list.get(position));
        if (list.get(position).equals("头条")) {
            bundle.putString("name", "top");
        } else if (list.get(position).equals("社会")) {
            bundle.putString("name", "shehui");
        } else if (list.get(position).equals("国内")) {
            bundle.putString("name", "guonei");
        } else if (list.get(position).equals("国际")) {
            bundle.putString("name", "guoji");
        } else if (list.get(position).equals("娱乐")) {
            bundle.putString("name", "yule");
        } else if (list.get(position).equals("体育")) {
            bundle.putString("name", "tiyu");
        } else if (list.get(position).equals("军事")) {
            bundle.putString("name", "junshi");
        } else if (list.get(position).equals("科技")) {
            bundle.putString("name", "keji");
        } else if (list.get(position).equals("财经")) {
            bundle.putString("name", "caijing");
        } else if (list.get(position).equals("时尚")) {
            bundle.putString("name", "shishang");
        }
        //设置当前newsFragment的bundle
        //具体讲解：https://www.jb51.net/article/102383.htm
        newsFragment.setArguments(bundle);
        return newsFragment;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
