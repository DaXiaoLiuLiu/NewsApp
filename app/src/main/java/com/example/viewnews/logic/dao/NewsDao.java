package com.example.viewnews.logic.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.viewnews.logic.dao.NewsData;

import java.util.List;

@Dao
public interface NewsDao {
    //一次查询10条
    @Query("SELECT * FROM NewsData WHERE  category = :type LIMIT :start,10")
    List<NewsData> getNews(int start,String type);

    //插入全新的数据
    @Insert
    void updateNews(List<NewsData> data);

    //先删除才能更新
    @Query("DELETE  FROM NewsData WHERE category = :type")
    void deleteNews(String type);

    //查询全部
    @Query("SELECT * FROM NewsData WHERE  category = :type")
    List<NewsData> getFavNews(String type);

    @Query("SELECT * FROM NewsData WHERE  category = :type AND uniquekey = :key")
    List<NewsData> selectFavNews(String type,String key);

    @Query("DELETE  FROM NewsData WHERE category = :type AND uniquekey = :key")
    void deleteFav(String type,String key);
}
