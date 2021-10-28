package com.example.viewnews.logic.dao;

import android.util.Log;

import java.util.List;

public class NewsSql {

    public static List<NewsData> getNewsList(String type,int count,NewsCache newsCache){
        int start;//查询开始索引
        //此方法需运行在子线程中
        String categoryName = new String();

        //做一个简单的参数转换
        switch (type){
            case "top":
                categoryName = "头条";
                break;
            case "shehui":
                categoryName = "社会";
                break;
            case "guonei":
                categoryName = "国内";
                break;
            case "guoji":
                categoryName = "国际";
                break;
            case "yule":
                categoryName = "娱乐";
                break;
            case "tiyu":
                categoryName = "体育";
                break;
            case "junshi":
                categoryName = "军事";
                break;
            case "keji":
                categoryName = "科技";
                break;
            case "caijing":
                categoryName = "财经";
                break;
            case "shishang":
                categoryName = "时尚";
                break;
            default:
                categoryName = "头条";
        }

        //计算本地查询的开始索引
        if(count < 0) count = 0;
        start = count * 10 + 1;
        Log.d("NewsSq","start is "+ start);

        List<NewsData> result;

      /*  int offsetV = (pageNo - 1) * pageSize;
        Log.d("pageNo", "页数为: " + pageNo);
        Log.d("offsetV", "偏移量为: " + offsetV);
        Log.d("offsetV", "以下开始查询");*/
        result = newsCache.newsDao().getNews(start,categoryName);//传入查询索引起点和查询类型
        return result;

   /*     if (dataBeanList.size() == 0) {
            pageNo = 1;
            offsetV = (pageNo - 1) * pageSize;//pageSize 指定查询结果的数量，offsetV是偏移量
            dataBeanList = newsCache.newsDao().getNews(offsetV,categoryName);
            Log.d("分页查询", "loaderRefreshData: 已经超过最大页数，归零并重新查询！");
        }*/
       // Log.d("刷新查到的数据大小", "run: " + dataBeanList.size());
    }

    public static void saveOnHost(List<NewsData> list,String type,NewsCache cache){//把之前的数据删除，再放进库里
        switch (type){
            case "top":
                type = "头条";
                break;
            case "shehui":
                type = "社会";
                break;
            case "guonei":
                type = "国内";
                break;
            case "guoji":
                type = "国际";
                break;
            case "yule":
                type = "娱乐";
                break;
            case "tiyu":
                type= "体育";
                break;
            case "junshi":
                type= "军事";
                break;
            case "keji":
                type = "科技";
                break;
            case "caijing":
                type = "财经";
                break;
            case "shishang":
                type = "时尚";
                break;
            default:
                type = "头条";
        }

        if(cache!= null){
            Log.d("NewsSql","开始清除");
            cache.newsDao().deleteNews(type);
            cache.newsDao().updateNews(list);
        }
        else {
            Log.d("NewsSql","传入的NewsCache is null");
        }
    }
}
