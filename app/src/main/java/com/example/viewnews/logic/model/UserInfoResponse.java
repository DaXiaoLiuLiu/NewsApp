package com.example.viewnews.logic.model;
/*
 * @Author Lxf
 * @Date 2021/9/18 22:36
 * @Description
 * 个人信息默认格式：
 *         //性别
        mmkv.encode("Sex","待完善");
        //地区
        mmkv.encode("Location","待完善");
        //个性签名
        mmkv.encode("Signature","这个人很懒，TA什么也没留下");
        //昵称
        mmkv.encode("NickName","未设置");
        //图片路径
        mmkv.encode("Url","URL");
 * @Since version-1.0
 */

import com.example.viewnews.logic.model.UInfo;

public class UserInfoResponse {

    //返回状态
    private Boolean status;
    private UInfo data;
    private String message;

    public UserInfoResponse(Boolean status, UInfo uInfo,String message) {
        this.status = status;
        this.data = uInfo;
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public UInfo getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
