package com.example.viewnews.logic.network.user;
/*
 * @Author Lxf
 * @Date 2021/7/28 9:10
 * @Description Retrofit 接口,包含用户登录和用户注册的方法
 * @Since version-1.0
 */

import com.example.viewnews.logic.model.UInfo;
import com.example.viewnews.logic.model.User;
import com.example.viewnews.logic.model.UserInfoResponse;
import com.example.viewnews.logic.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {

    //登录方法，输入url
    @POST("login")
    Call<UserResponse> getLogin(@Body User user);

    //注册方法
    @POST("register")
    Call<UserResponse> createUser(@Body User user);

    @FormUrlEncoded
    @POST("login")
    Call<UserResponse> getLogin2(@Field("name") String name,@Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<UserResponse> createUser2(@Field("name") String name,@Field("password") String password);

    @FormUrlEncoded
    @POST("getInfo")
    Call<UserInfoResponse> getInfo(@Field("name") String name);

    @POST("setInfo")
    Call<UserInfoResponse> setInfo(@Body UInfo uInfo);
}
