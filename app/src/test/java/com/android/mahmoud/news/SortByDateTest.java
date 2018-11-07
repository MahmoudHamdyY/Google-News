package com.android.mahmoud.news;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class SortByDateTest {

    @Test
    public void test_sort()
    {
        List<NewsItem> list= getData();

        Collections.sort(list, new SortByDate());

        assertEquals("2018-12-11", list.get(0).getDate());
        assertEquals("2018-11-11", list.get(1).getDate());
        assertEquals("2018-11-06", list.get(2).getDate());
        assertEquals("2018-10-11", list.get(3).getDate());
    }

    private List<NewsItem> getData()
    {
        List<NewsItem> list = new ArrayList<>();

        list.add(new NewsItem("Title 1","2018-11-11T17:28:20Z","","",""));
        list.add(new NewsItem("Title 2","2018-10-11T17:28:20Z","","",""));
        list.add(new NewsItem("Title 3","2018-12-11T17:28:20Z","","",""));
        list.add(new NewsItem("Title 4","2018-11-06T17:28:20Z","","",""));

        return list;
    }
}