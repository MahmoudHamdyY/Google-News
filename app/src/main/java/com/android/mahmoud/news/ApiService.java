package com.android.mahmoud.news;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {

    //Fetch News
    @GET("v2/everything")
    Observable<ResponseItem> fetchNews(@Query("q") String keyWord, @Query("apikey") String apiKey,
                                       @Query("sources") String sources, @Query("language") String language,
                                       @Query("page") int page);
}
