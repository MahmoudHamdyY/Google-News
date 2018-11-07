package com.android.mahmoud.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MySharedPreference {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private final String LIST_KEY = "Favorite_List";

    @SuppressLint("CommitPrefEdits")
    MySharedPreference(Context context)
    {
        String PREF_NAME = "FavoriteNewsPref";
        int PRIVATE_MODE = 0;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    List<NewsItem> getFavoriteList()
    {
        List<NewsItem> list = new ArrayList<>();
        if(preferences.contains(LIST_KEY))
        {
            String jsonData = preferences.getString(LIST_KEY, null);
            Gson gson = new Gson();
            NewsItem[] data = gson.fromJson(jsonData, NewsItem[].class);
            list = Arrays.asList(data);
            list = new ArrayList<>(list);
        }
        return list;
    }

    void saveFavoriteList(List<NewsItem> list)
    {
        Gson gson = new Gson();
        String jsonData = gson.toJson(list);
        editor.putString(LIST_KEY, jsonData);
        editor.apply();
    }
}
