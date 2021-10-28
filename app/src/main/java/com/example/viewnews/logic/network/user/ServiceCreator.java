package com.example.viewnews.logic.network.user;

import com.example.viewnews.BuildConfig;
import com.example.viewnews.logic.network.HttpLog;
import com.example.viewnews.logic.network.user.LoginService;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceCreator<T> {
    private static final String BASE_URL = "http://47.107.82.195:8080";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(initHttpClient())
            .build();

    public static LoginService Create(Class<LoginService> ServiceClass) {
        LoginService Service = retrofit.create(ServiceClass);
        return Service;
    }


    private static OkHttpClient initHttpClient() {
        Interceptor logInterceptor;
        //处理网络请求的日志拦截输出
        if (BuildConfig.DEBUG) {
            //只显示基础信息
            logInterceptor = new HttpLoggingInterceptor(new HttpLog()).setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logInterceptor = new HttpLoggingInterceptor(new HttpLog()).setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        return new OkHttpClient().newBuilder()
                .addInterceptor(logInterceptor)
                .build();

    }
}