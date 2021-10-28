package com.example.viewnews.logic.network.news;

import com.example.viewnews.logic.network.news.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {

    @GET("index")
    Call<NewsResponse> getNewsList(@Query("type") String type, @Query("key") String key);
}
