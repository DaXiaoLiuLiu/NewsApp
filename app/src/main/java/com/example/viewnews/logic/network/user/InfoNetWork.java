package com.example.viewnews.logic.network.user;

import android.util.Log;

import com.example.viewnews.logic.model.UInfo;
import com.example.viewnews.logic.model.UserInfoResponse;

import java.io.IOException;

import retrofit2.Response;

public class InfoNetWork {
    private static final LoginService infoService = ServiceCreator.Create(LoginService.class);
    private static UserInfoResponse set_result;
    private static UserInfoResponse get_result;

    public static UserInfoResponse getInfo(String username){
        if(username == null){
            Log.d("InfoNetWork","name is null !");
        }
        else {
            try {
                Response<UserInfoResponse> response =
                        infoService.getInfo(username).execute();
                if(response == null){
                    get_result = new
                            UserInfoResponse(false,null,"response is null");
                }
                else {
                    get_result = response.body();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return get_result;
    }

    public static UserInfoResponse setInfo(UInfo uInfo){
        if (uInfo == null){
            Log.d("InfoNetWork","uInfo is null !");
        }
        else {
            try {
                Response<UserInfoResponse> response = infoService.setInfo(uInfo).execute();
                if (response == null){
                    set_result =
                            new UserInfoResponse(false,null,"set_response is null");
                }
                else {
                    set_result = response.body();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return set_result;
    }

}
