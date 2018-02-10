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

    public SearchResultItemViewStateAdapterDelegate(ImageLoader imageLoader, ViewStateAdapterDelegateClickListener listener) {
        super(listener);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image) ImageView image;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.subtitle) TextView subtitle;
        private ViewState mViewState;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView.setOnClickListener(this);
        }

        public void bind(ViewState item) {
            this.mViewState = item;
            mImageLoader.load(item.imageUrl(), image);
            title.setText(item.title());
            subtitle.setText(item.subtitle());
        }

        @Override
        public void onClick(View view) {
            ViewStateAdapterDelegateClickListener l = mListener.get();
            if (l != null) {
                l.onViewStateAdapterDelegateClicked(mViewState, getAdapterPosition());
            }
        }
    }

    @AutoValue
    public static abstract class ViewState implements com.urizev.birritas.view.common.ViewState {
        public static final String TYPE_BEER = "beer";
        public static final String TYPE_BREWERY = "brewery";

        public abstract String id();
        public abstract String type();
        @Nullable
        public abstract String imageUrl();
        public abstract String title();
        @Nullable
        public abstract String subtitle();

        public static ViewState create(String id, String type, String imageUrl, String title, String subtitle) {
            return new AutoValue_SearchResultItemViewStateAdapterDelegate_ViewState(id, type, imageUrl, title, subtitle);
        }
    }
}