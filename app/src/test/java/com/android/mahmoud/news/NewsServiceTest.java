package com.android.mahmoud.news;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NewsServiceTest {

    private NewsService newsService;

    @Mock
    private
    ApiService apiService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        newsService = new NewsService(apiService);
    }

    @Test
    public void load_2Responses_Api_Test()
    {
        when(apiService.fetchNews(anyString(), anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(Observable.just(getResponse()));


        TestSubscriber<ResponseItem> subscriber = new TestSubscriber<>();
        newsService.loadItems(1).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        ResponseItem item = subscriber.getOnNextEvents().get(0);
        assertEquals(item.getTotalResults(), item.getArticles().size());
        assertEquals(3, item.getArticles().size());
        assertEquals("Test 1", item.getArticles().get(1).getTitle());
        assertEquals("Test 2", item.getArticles().get(2).getTitle());
        assertEquals("2018-11-06", item.getArticles().get(1).getPublishedAt().substring(0, 10));
        assertEquals("16:28:17", item.getArticles().get(1).getPublishedAt().substring(11, 19));

        List<NewsItem> list = item.getArticles();

        Collections.sort(list, new SortByDate());

        assertEquals("2018-11-10", list.get(0).getPublishedAt().substring(0, 10));
        assertEquals("2018-11-10", list.get(0).getDate());
        assertEquals("2018-11-09", list.get(1).getDate());
        assertEquals("2018-11-06", list.get(2).getDate());

        verify(apiService).fetchNews("Google", "db35b345bfee4c3cb167ca32d453fb0f", "usa-today", "en", 1);
    }

    @Test
    public void load_2Responses_IOException_Api_Test()
    {
        when(apiService.fetchNews(anyString(), anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(getIOExceptionError());


        TestSubscriber<ResponseItem> subscriber = new TestSubscriber<>();
        newsService.loadItems(1).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        subscriber.assertError(IOException.class);

        verify(apiService).fetchNews("Google", "db35b345bfee4c3cb167ca32d453fb0f", "usa-today", "en", 1);

    }

    private ResponseItem getResponse()
    {
        ResponseItem responseItem = new ResponseItem();
        responseItem.setStatus("ok");
        responseItem.setTotalResults(3);
        List<NewsItem> itemList = new ArrayList<>();
        itemList.add(new NewsItem("Test 3","2018-11-09T17:28:20Z","picUrl","postUrl","test 2 description"));
        itemList.add(new NewsItem("Test 1","2018-11-06T16:28:17Z","picUrl","postUrl","test 1 description"));
        itemList.add(new NewsItem("Test 2","2018-11-10T17:28:20Z","picUrl","postUrl","test 2 description"));
        responseItem.setArticles(itemList);
        return responseItem;
    }

    private Observable<ResponseItem> getIOExceptionError() {
        return Observable.error(new IOException());
    }
}