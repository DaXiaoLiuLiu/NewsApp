   package com.example.viewnews.ui.news;
/*
 * @Author Lxf
 * @Date 2021/9/13 20:37
 * @Description 使用RecyclerView编写的新闻列表 适配器
 * @Since version-1.0
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewnews.R;
import com.example.viewnews.logic.dao.NewsData;
import com.example.viewnews.ui.GlideHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NewsTabAdapter extends RecyclerView.Adapter{
    private static final int TEXT_VIEW = 0;//单、双图片标题
    private static final int IMAGE_VIEW = 1;//多图片标题
    private static final int ALL_TEXT_VIEW = 2;//纯文本标题
    private static final int PROGRESS_VIEW = 3;//上拉加载的实现相关

    private Context context;

    private List<NewsData> newsDataList = new ArrayList<>();

    public NewsTabAdapter(List<NewsData> list,Context context) {//传入列表数据
        this.newsDataList.addAll(list);
        this.context = context;
    }
    public NewsTabAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (newsDataList.size() == 0){//未设置数据

        }
        if(newsDataList.get(position) == null){//上拉加载
            return PROGRESS_VIEW;
        }
        //常规布局判断
        return getItemType(newsDataList.get(position));
    }

    //绑定子项布局，目前方法不完善。缺少纯文本布局和实现上拉加载功能
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case TEXT_VIEW://单图片
                view = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.item_01,parent,false);
                return new TextViewHolder(view);
            case IMAGE_VIEW://多图片
                view = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.item_02,parent,false);
                return new ImageViewHolder(view);
            case ALL_TEXT_VIEW://纯文本
                break;
            case PROGRESS_VIEW://设置加载
                break;

            default:
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int position) {
        GlideHelper glideHelper;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {//列表子项，响应事件
            @Override
            public void onClick(View v) {
               NewsData newsData = newsDataList.get(position);
                String url = newsData.getUrl();
                Log.d("当前新闻子项的连接是：", "onItemClick: " + url);
                String uniquekey = newsData.getUniquekey();
                String newsTitle = newsData.getTitle();
                //开启web activity
                Intent intent = new Intent(v.getContext(), WebActivity.class);
                intent.putExtra("type",newsData.getCategory());
                intent.putExtra("pageUrl", url);
                intent.putExtra("uniquekey", uniquekey);
                intent.putExtra("news_title", newsTitle);
                v.getContext().startActivity(intent);
            }
        });

        if(viewHolder instanceof TextViewHolder){//单图片子项布局的设置
            TextViewHolder holder = (TextViewHolder) viewHolder;
            holder.title.setText(newsDataList.get(position).getTitle());
            holder.author_name.setText(newsDataList.get(position).getAuthor_name());
            glideHelper = new GlideHelper(holder.view.getContext());
            glideHelper.setGilde(newsDataList.get(position).getThumbnail_pic_s(),holder.image);

        }
        else if(viewHolder instanceof ImageViewHolder){//多图片子项布局的设置
            ImageViewHolder holder = (ImageViewHolder) viewHolder;
            holder.title.setText(newsDataList.get(position).getTitle());
            glideHelper = new GlideHelper(holder.view.getContext());
            glideHelper.setGilde(newsDataList.get(position).getThumbnail_pic_s(),holder.image01);
            glideHelper.setGilde(newsDataList.get(position).getThumbnail_pic_s02(),holder.image02);
            glideHelper.setGilde(newsDataList.get(position).getThumbnail_pic_s03(),holder.image03);

        }
        else if(viewHolder instanceof PureViewHolder){

        }
    }

    @Override
    public int getItemCount() {
        return newsDataList.size();
    }

    //下面是不同的三个子项布局
    class PureViewHolder extends RecyclerView.ViewHolder {//纯文本布局

        public PureViewHolder(@NonNull @NotNull View itemView) {//这里的itemView

            super(itemView);

        }
    }

    class TextViewHolder extends RecyclerView.ViewHolder{//单图片布局
        TextView title, author_name;
        ImageView image;
        View view;
        public TextViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            view = itemView;
            title = view.findViewById(R.id.title);
            author_name = view.findViewById(R.id.author_name);
            image = view.findViewById(R.id.image);
        }


    }

    class ImageViewHolder extends RecyclerView.ViewHolder{//多图片布局
        TextView title;
        ImageView image01, image02, image03;
        View view;
        public ImageViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            view = itemView;
            title = view.findViewById(R.id.title);
            image01 = view.findViewById(R.id.image01);
            image02 = view.findViewById(R.id.image02);
            image03 = view.findViewById(R.id.image03);
        }

    }

    //判断子项布局类型，已做判空处理
    public int getItemType(NewsData data){
        if(data.getThumbnail_pic_s() == null) return ALL_TEXT_VIEW;//纯文本布局
        else{
            if (data.getThumbnail_pic_s03() == null) return TEXT_VIEW;//单双，图片布局
            else return IMAGE_VIEW;//多图片布局
        }
    }

    public List<NewsData> getNewsDataList() {
        return newsDataList;
    }

    public void setNewsDataList(List<NewsData> newsDataList) {
        this.newsDataList.clear();
        this.newsDataList.addAll(newsDataList);
    }
}
