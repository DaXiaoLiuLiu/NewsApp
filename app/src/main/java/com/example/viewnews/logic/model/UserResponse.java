package com.example.viewnews.logic.model;
/*
 * @Author Lxf
 * @Date 2021/7/28 9:20
 * @Description 数据实体类
 * @Since version-1.0
 */

public class UserResponse {
    //status 是登录或者注册的识别码
    private Boolean status;
    private String message;

    public UserResponse(){

    }

    public UserResponse(Boolean status,String message){
        this.status = status;
        this.message = message;
    }
    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


}
