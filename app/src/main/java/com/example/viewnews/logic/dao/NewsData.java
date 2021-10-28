package com.example.viewnews.logic.dao;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


/*
 * @Author Lxf
 * @Date 2021/9/7 11:48
 * @Description 重要类，文章主题内容的数据实体
 * @Since version-1.0
 */

@Entity
public class NewsData {

    @PrimaryKey(autoGenerate = true)
    private int id ;//存到数据库中的索引,序号自动生成

    private String uniquekey;
    private String title;
    private String date;
    private String category;
    private String author_name;
    private String url;

    private String thumbnail_pic_s;
    private String thumbnail_pic_s02;
    private String thumbnail_pic_s03;


    public void setId(int id) {
        this.id = id;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumbnail_pic_s(String thumbnail_pic_s) {
        this.thumbnail_pic_s = thumbnail_pic_s;
    }

    public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
        this.thumbnail_pic_s02 = thumbnail_pic_s02;
    }

    public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
        this.thumbnail_pic_s03 = thumbnail_pic_s03;
    }

    public int getId() {
        return id;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail_pic_s() {
        return thumbnail_pic_s;
    }

    public String getThumbnail_pic_s02() {
        return thumbnail_pic_s02;
    }

    public String getThumbnail_pic_s03() {
        return thumbnail_pic_s03;
    }
}
