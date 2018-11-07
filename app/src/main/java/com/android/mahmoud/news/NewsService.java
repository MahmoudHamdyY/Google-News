package com.android.mahmoud.news;

import java.io.IOException;

import rx.Observable;

class NewsService {
    private static final String ApiKey = "db35b345bfee4c3cb167ca32d453fb0f";
    private ApiService apiService;

    NewsService(ApiService apiService)
    {
        this.apiService = apiService;
    }

    Observable<ResponseItem> loadItems(final int page)
    {
        return Observable.defer(() -> apiService.fetchNews("Google", ApiKey, "usa-today", "en", page, "publishedAt")
                .retryWhen(observable -> observable.flatMap(o -> {
                    if(o instanceof IOException) {
                        return Observable.just(null);
                    }
                    return Observable.error(o);
                }).take(10).concatWith(observable.flatMap(Observable::error))));
    }
}
