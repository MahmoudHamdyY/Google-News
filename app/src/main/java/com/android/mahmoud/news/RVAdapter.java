package com.android.mahmoud.news;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<NewsItem> list;
    private AdapterView.OnItemClickListener onItemClickListener;
    private boolean isLoading;
    private OnLoadMoreListener onLoadMoreListener;
    private FavoriteChangeListener favoriteChangeListener;

    RVAdapter(RecyclerView recyclerView, List<NewsItem> list, AdapterView.OnItemClickListener onItemClickListener) {
        this.list = list;
        this.onItemClickListener = onItemClickListener;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && lastVisibleItem == getItemCount() - 1) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    void setFavoriteChangeListener(FavoriteChangeListener favoriteChangeListener) {
        this.favoriteChangeListener = favoriteChangeListener;
    }

    void setLoaded() {
        isLoading = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.news_item, parent, false);
        return new ViewHolderNew(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewsItem item = list.get(position);
        ViewHolderNew viewHolder = (ViewHolderNew) holder;
        viewHolder.title.setText(item.getTitle());
        viewHolder.description.setText(item.getDescription());
        if (item.isFavorite())
            viewHolder.favorite.setActivated(true);
        else
            viewHolder.favorite.setActivated(false);
        if(item.getUrlToImage() != null && !item.getUrlToImage().isEmpty())
            Picasso.get().load(item.getUrlToImage()).error(R.drawable.news)
                    .placeholder(R.drawable.progress_animation).fit()
                    .centerCrop().noFade().into(viewHolder.item_image);
        else
            viewHolder.item_image.setImageResource(R.drawable.news);
        viewHolder.favorite.setOnClickListener(v -> {
            if(item.isFavorite())
                favoriteChangeListener.remove(position);
            else
                favoriteChangeListener.add(position);
            notifyItemChanged(position);
        });
        viewHolder.divider.setText(item.getDate());
        if (item.isDateVisible())
            viewHolder.divider.setVisibility(View.VISIBLE);
        else
            viewHolder.divider.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<NewsItem> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    public List<NewsItem> getList()
    {
        return this.list;
    }

    private class ViewHolderNew extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView item_image;
        private TextView title, description, divider;
        private ImageButton favorite;


        ViewHolderNew(View v) {
            super(v);
            item_image = v.findViewById(R.id.item_image);
            title = v.findViewById(R.id.item_title);
            description = v.findViewById(R.id.item_description);
            favorite = v.findViewById(R.id.item_favorite);
            divider = v.findViewById(R.id.divider);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(null, v, getAdapterPosition(), v.getId());
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface FavoriteChangeListener{
        void add(int position);
        void remove(int position);
    }

}
