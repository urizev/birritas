package com.urizev.birritas.view.search.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.auto.value.AutoValue;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.image.ImageLoader;
import com.urizev.birritas.view.common.ViewStateAdapterDelegate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultItemViewStateAdapterDelegate extends ViewStateAdapterDelegate<SearchResultItemViewStateAdapterDelegate.ViewState, SearchResultItemViewStateAdapterDelegate.ViewHolder> {
    private final ImageLoader mImageLoader;

    public SearchResultItemViewStateAdapterDelegate(ImageLoader imageLoader) {
        this.mImageLoader = imageLoader;
    }

    @Override
    protected boolean isForViewType(@NonNull com.urizev.birritas.view.common.ViewState item, @NonNull List<com.urizev.birritas.view.common.ViewState> items, int position) {
        return item instanceof ViewState;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new ViewHolder(viewItemForViewHolder(parent, R.layout.cell_search_result));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewState item, @NonNull ViewHolder holder, @NonNull List<Object> payloads) {
        holder.bind(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image) ImageView image;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.subtitle) TextView subtitle;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


        public void bind(ViewState item) {
            mImageLoader.load(item.imageUrl(), image);
            title.setText(item.title());
            subtitle.setText(item.subtitle());
        }
    }

    @AutoValue
    public static abstract class ViewState implements com.urizev.birritas.view.common.ViewState {
        @Nullable
        public abstract String imageUrl();
        public abstract String title();
        @Nullable
        public abstract String subtitle();

        public static ViewState create(String imageUrl, String title, String subtitle) {
            return new AutoValue_SearchResultItemViewStateAdapterDelegate_ViewState(imageUrl, title, subtitle);
        }
    }
}