package com.android.mahmoud.news;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NewsItemTest {

    @Test
    public void test_is_Equals(){
        NewsItem item1 = new NewsItem(getData().get(0));
        NewsItem item2 = new NewsItem(getData().get(1));
        NewsItem item1_2 = new NewsItem(getData().get(0));

        assertNotEquals(item1, item1_2);
        assertFalse(item1.isEquals(item2));
        assertTrue(item1.isEquals(item1_2));

    }

    @Test
    public void test_Remove_Duplicates(){

        List<NewsItem> list = getData();
        List<NewsItem> expectedList = getData();

        assertEquals(list.size(), expectedList.size());
        for(int i = 0; i < list.size(); i++)
            assertTrue(list.get(i).isEquals(expectedList.get(i)));

        list.addAll(getData());
        assertEquals(list.size(), expectedList.size() * 2);
        NewsItem.removeDuplicate(list);

        assertEquals(list.size(), expectedList.size());
        for(int i = 0; i < list.size(); i++)
            assertTrue(list.get(i).isEquals(expectedList.get(i)));

    }

    @Test
    public void test_setUp_Dividers() {
        List<NewsItem> list = getData();

        NewsItem.setUpDividers(list);

        assertTrue(list.get(0).isDivider());
        assertFalse(list.get(1).isDivider());
        assertTrue(list.get(2).isDivider());
        assertTrue(list.get(3).isDivider());
        assertTrue(list.get(4).isDivider());

    }

    private List<NewsItem> getData() {
        List<NewsItem> list = new ArrayList<>();

        list.add(new NewsItem("Title 3","2018-12-11T17:28:20Z","","test/title3",""));
        list.add(new NewsItem("Title 4","2018-12-11T19:20:42Z","","test/title4",""));
        list.add(new NewsItem("Title 1","2018-11-11T17:28:20Z","","test/title1",""));
        list.add(new NewsItem("Title 5","2018-11-06T17:28:20Z","","test/title5",""));
        list.add(new NewsItem("Title 2","2018-10-11T17:28:20Z","","test/title2",""));

        return list;
    }


}