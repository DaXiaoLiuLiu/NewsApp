package com.example.viewnews.logic.network.user;
/*
 * @Author Lxf
 * @Date 2021/7/28 10:02
 * @Description 用于进行网络连接的类
 * @Since version-1.0
 */

import android.util.Log;

import com.example.viewnews.logic.model.User;
import com.example.viewnews.logic.model.UserResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginNetWork {
    //获取实例，才可以进行网络请求
    private static final LoginService loginService= ServiceCreator.Create(LoginService.class);
    private static UserResponse LoginResult;//获取登录请求的结果
    private static UserResponse SignResult ;;//获取注册请求的结果

    public static UserResponse sign_in(User user){

        if(user == null){//这边进行判空处理
            Log.d("LoginNetWork","user is empty");
        }
        else {
            loginService.getLogin2(user.getUserName(),user.getPassword()).enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                    if(response.body() == null){
                        Log.d("LoginNetWork","返回错误，返回的结果为空");
                        LoginResult = new UserResponse(false,null);
                    }
                    else {
                        LoginResult = response.body();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable throwable) {
                    Log.d("LoginNetWork","网络连接错误");

                    LoginResult = new UserResponse(false,null);
                }
            });
        }
        return LoginResult;
    }

    public static UserResponse sign_up(User user){
        if(user == null){//这边进行判空处理
            Log.d("LoginNetWork","Register user is empty");
        }
        else {

            try {
                Response<UserResponse> response = loginService.
                        createUser2(user.getUserName(),user.getPassword()).execute();
                if(response.body() == null){
                    Log.d("RegisterNetWork","Register: 返回错误，返回的结果为空");
                    SignResult = new UserResponse(false,"");
                }
                else {
                    SignResult = response.body();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("RegisterNetWork","Register: 网络连接错误");
            }
        }
        return SignResult;
    }

}
