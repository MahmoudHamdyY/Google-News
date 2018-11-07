package com.android.mahmoud.news;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseItem {
    @SerializedName("status")
    private String status = "";

    @SerializedName("totalResults")
    private int totalResults = 0;

    @SerializedName("articles")
    private List<NewsItem> articles = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    int getTotalResults() {
        return totalResults;
    }

    List<NewsItem> getArticles() {
        return articles;
    }

    void setStatus(String status) {
        this.status = status;
    }

    void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    void setArticles(List<NewsItem> articles) {
        this.articles = articles;
    }

}
