package com.android.mahmoud.news;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private ApiService apiService;

    List<NewsItem> items;
    List<NewsItem> tempList;
    List<NewsItem> favoriteItems;

    // View
    RecyclerView recyclerView;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    RVAdapter adapter;

    private MySharedPreference mySharedPreference;

    private Menu menu;
    // true if fetching from service
    boolean isLoading = false;
    // number of page to start fetch from
    int from = 1;
    // no more data to load
    boolean noMore = false;
    // if favorite list is the viewed list
    boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiService = ApiClient.getClient().create(ApiService.class);

        recyclerView = findViewById(R.id.list);
        progressBar = findViewById(R.id.load_more_pb);
        swipeRefreshLayout = findViewById(R.id.swipeContainer);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        items = new ArrayList<>();
        tempList = new ArrayList<>();

        mySharedPreference = new MySharedPreference(this);

        favoriteItems = mySharedPreference.getFavoriteList();

        swipeRefreshLayout.setOnRefreshListener(this::refresh);
        refresh();
    }

    private void refresh() {
        from = 1;
        isLoading = false;
        noMore = false;
        if (!isFavorite)
            loadPage(from);
        else
            swipeRefreshLayout.setRefreshing(false);
    }

    private void updateUI() {
        // stop refreshing
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        // hide loading bar
        progressBar.setVisibility(View.GONE);

        isLoading = false;

        items.addAll(tempList);

        // sort items by date in decreasing order
        Collections.sort(items, new SortByDate());
        NewsItem.removeDuplicate(items);
        NewsItem.setUpDividers(items);
        setUpFavorites(items);

        if (adapter == null) {
            setupAdapter();
        } else {
            adapter.notifyDataSetChanged();
            adapter.setLoaded();
        }
    }

    private void setupAdapter() {

        adapter = new RVAdapter(recyclerView, items, (parent, view, position, id) -> {
            NewsItem item = adapter.getList().get(position);
            if (item.getUrl() != null) {
                // load news url in browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
                startActivity(browserIntent);
            }
        });

        // when user reached the end list load more data if exist
        adapter.setOnLoadMoreListener(() -> {
            if (!isLoading && !noMore && !isFavorite) {
                from++;
                loadPage(from);
            } else
                adapter.setLoaded();
        });

        // when user add/remove item to/from favorite list
        adapter.setFavoriteChangeListener(new RVAdapter.FavoriteChangeListener() {
            @Override
            public void add(int position) {
                if (!isInFavoriteList(adapter.getList().get(position)))
                    favoriteItems.add(new NewsItem(adapter.getList().get(position)));
                adapter.getList().get(position).setFavorite(true);

            }

            @Override
            public void remove(int position) {
                removeFromFavoriteList(adapter.getList().get(position));
                if (isFavorite) {
                    NewsItem.setUpDividers(favoriteItems);
                    adapter.notifyDataSetChanged();
                } else
                    adapter.getList().get(position).setFavorite(false);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void loadPage(int page) {
        tempList.clear();
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);

        new NewsService(apiService).loadItems(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseItem>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        noMore = true;
                        if(e instanceof IOException)
                            sendErrorDialog("Network Error!\nPlease try again later.");
                        else
                            sendErrorDialog("An error occurred while getting data\nPlease try again later.");
                    }

                    @Override
                    public void onNext(ResponseItem responseItem) {
                        if (responseItem.getStatus().equals("error"))
                            noMore = true;

                        if (responseItem.getArticles().size() == 0)
                            noMore = true;

                        tempList.addAll(responseItem.getArticles());
                        updateUI();
                    }
                });
    }

    // mark isFavorite true if the item is in favorite list
    private void setUpFavorites(List<NewsItem> list) {

        for (int i = 0; i < list.size(); i++)
            if (isInFavoriteList(list.get(i)))
                list.get(i).setFavorite(true);
            else
                list.get(i).setFavorite(false);
    }

    private boolean isInFavoriteList(NewsItem item) {
        boolean found = false;

        for (NewsItem i : favoriteItems)
            if (i.isEquals(item)) {
                found = true;
                break;
            }

        return found;
    }

    private void removeFromFavoriteList(NewsItem item) {
        int pos = -1;
        for (int i = 0; i < favoriteItems.size(); i++)
            if (favoriteItems.get(i).getUrl().equals(item.getUrl())) {
                pos = i;
                break;
            }
        if (pos != -1)
            favoriteItems.remove(pos);
    }

    private void sendErrorDialog(String msg) {
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setTitle("Error");
        ad.setMessage(msg);
        ad.setPositiveButton("Ok", null);
        ad.show();
        progressBar.setVisibility(View.GONE);
        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }


    // change the viewed list from/to all/favorite items
    private void toggle() {
        if (isFavorite) {
            // change the icon / title in action bar
            menu.getItem(0).setIcon(getDrawable(R.drawable.favortie_bt));
            menu.getItem(0).setTitle(getString(R.string.favorite));

            setUpFavorites(items);

            isFavorite = false;
            adapter.setData(items);
            adapter.notifyDataSetChanged();
        } else {

            // change the icon / title in action bar
            menu.getItem(0).setIcon(getDrawable(R.drawable.news_bt));
            menu.getItem(0).setTitle(getString(R.string.news));

            Collections.sort(favoriteItems, new SortByDate());
            NewsItem.removeDuplicate(favoriteItems);
            NewsItem.setUpDividers(favoriteItems);

            for (NewsItem i : favoriteItems)
                i.setFavorite(true);

            if (adapter == null)
                setupAdapter();
            isFavorite = true;
            adapter.setData(favoriteItems);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //saving favorite list
        mySharedPreference.saveFavoriteList(favoriteItems);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.favorite_bt) {
            toggle();
        }
        return super.onOptionsItemSelected(item);
    }
}
