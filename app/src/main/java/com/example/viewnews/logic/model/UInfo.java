package com.example.viewnews.logic.model;

public class UInfo {
    //用户账号，唯一标识主键
    private String name;
    private UDetail uDetail;

    public String getName() {
        return name;
    }

    public UDetail getuDetail() {
        return uDetail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setuDetail(UDetail uDetail) {
        this.uDetail = uDetail;
    }

    public static class UDetail{
        //用户昵称
        private String nickName;
        //性别
        private String userSex;
        // 地区
        private String userLocation;
        // 个性签名
        private String userSignature;
        //头像图片路径
        private String imageUrl;

        public UDetail(){
            super();
        }

        public UDetail(String NickName, String userSex, String userLocation, String userSignature, String imageUrl) {
            this.nickName = NickName;
            this.userSex = userSex;
            this.userLocation = userLocation;
            this.userSignature = userSignature;
            this.imageUrl = imageUrl;
        }
        public String getnickname(){
            return nickName;
        }

        public String getUserSex() {
            return userSex;
        }

        public String getUserLocation() {
            return userLocation;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getUserSignature() {
            return userSignature;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public void setUserSex(String userSex) {
            this.userSex = userSex;
        }

        public void setUserLocation(String userLocation) {
            this.userLocation = userLocation;
        }

        public void setUserSignature(String userSignature) {
            this.userSignature = userSignature;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
