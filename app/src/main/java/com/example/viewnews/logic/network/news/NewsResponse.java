package com.example.viewnews.logic.network.news;

import com.example.viewnews.logic.dao.NewsData;

import java.util.List;

public class NewsResponse {

    private int error_code;
    private String reason;
    private Result result;


    public class Result{
        private String stat;
        private List<NewsData> data;
        private String page;
        private String pageSize;

        public String getStat() {
            return stat;
        }

        public List<NewsData> getDataList() {
            return data;
        }

        public String getPage() {
            return page;
        }

        public String getPageSize() {
            return pageSize;
        }
    }

    public int getError_code() {
        return error_code;
    }

    public String getReason() {
        return reason;
    }

    public Result getResult() {
        return result;
    }
}
