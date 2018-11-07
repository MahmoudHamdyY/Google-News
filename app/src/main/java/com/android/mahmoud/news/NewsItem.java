package com.android.mahmoud.news;


import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.List;

public class NewsItem{
    @SerializedName("title")
    private String title = "";

    @SerializedName("publishedAt")
    private String publishedAt = "";

    @SerializedName("urlToImage")
    private String urlToImage = "";

    @SerializedName("url")
    private String url = "";

    @SerializedName("description")
    private String description = "";

    private boolean isFavorite = false;

    private boolean isDateVisible = false;

    NewsItem(NewsItem item) {
        this.title = item.title;
        this.isDateVisible = item.isDateVisible;
        this.publishedAt = item.publishedAt;
        this.isFavorite = item.isFavorite;
        this.urlToImage = item.urlToImage;
        this.url = item.url;
        this.description = item.description;
    }

    NewsItem(String title, String publishedAt, String urlToImage, String url, String description) {
        this.title = title;
        this.publishedAt = publishedAt;
        this.urlToImage = urlToImage;
        this.url = url;
        this.description = description;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public boolean isDateVisible() {
        return isDateVisible;
    }

    public void setDateVisible(boolean dateVisible) {
        isDateVisible = dateVisible;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }


    public String getTitle() {
        return title;
    }

    String getPublishedAt() {
        return publishedAt;
    }

    public String getDate() {
        String date = "";
        try {
            date = publishedAt.substring(0, 10);
        }catch (StringIndexOutOfBoundsException ignored){}
        return date;
    }

    String getUrlToImage() {
        return urlToImage;
    }

    String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    boolean isEquals(NewsItem b) {
        boolean isEqual = true;

        if(!getUrl().equals(b.getUrl()))
            isEqual = false;
        if(!getPublishedAt().equals(b.getPublishedAt()))
            isEqual = false;
        if(!getTitle().equals(b.getTitle()))
            isEqual = false;
        if(getUrlToImage() != null && b.getUrlToImage() != null &&!getUrlToImage().equals(b.getUrlToImage()))
            isEqual = false;
        if(getDescription() != null && b.getDescription() != null && !getDescription().equals(b.getDescription()))
            isEqual = false;

        return isEqual;
    }

    static void removeDuplicate(List<NewsItem> items) {
        for (int i = 0; i < items.size(); i++) {
            for (int j = i + 1; j < items.size(); j++)
                // compare using post url
                if (items.get(i).isEquals(items.get(j))) {
                    items.remove(j);
                    j--;
                }
        }

    }

    static void setUpDividers(List<NewsItem> items) {
        if (!items.isEmpty())
            items.get(0).setDateVisible(true);

        for (int i = 1; i < items.size(); i++) {
            // check if they are in different days to insert divider
            if (!items.get(i - 1).getDate().equals(items.get(i).getDate()))
                items.get(i).setDateVisible(true);
            else
                items.get(i).setDateVisible(false);
        }

    }

}

class SortByDate implements Comparator<NewsItem> {
    public int compare(NewsItem a, NewsItem b) {
        return Integer.compare(0, a.getDate().compareTo(b.getDate()));
    }
}
