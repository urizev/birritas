package com.urizev.birritas.view.featured;

import android.content.res.ColorStateList;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.image.ImageLoader;
import com.urizev.birritas.ui.FeatureBeerCellParamView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {
    private final ImageLoader mImageLoader;
    private ImmutableList<FeaturedItemViewState> mViewStates;
    private PublishSubject<FavoriteEvent> mFavoriteEvents;

    FeaturedAdapter(ImageLoader imageLoader) {
        this.mImageLoader = imageLoader;
        this.mFavoriteEvents = PublishSubject.create();
    }

    @Override
    public FeaturedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeaturedViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(FeaturedViewHolder holder, int position) {
        holder.update(mViewStates.get(position));
    }

    public Observable<FavoriteEvent> favoriteEvents() {
        return mFavoriteEvents;
    }

    @Override
    public int getItemCount() {
        return mViewStates == null ? 0 : mViewStates.size();
    }

    void update(ImmutableList<FeaturedItemViewState> viewStates) {
        this.mViewStates = viewStates;
        this.notifyDataSetChanged();
    }

    class FeaturedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.beer_label) ImageView labelView;
        @BindView(R.id.beer_title) TextView titleView;
        @BindView(R.id.brewed_by) TextView brewedBy;
        @BindView(R.id.beer_style) TextView styleView;
        @BindView(R.id.srm_color) ImageView srcColorView;
        @BindView(R.id.srm_param) FeatureBeerCellParamView srmParamView;
        @BindView(R.id.abv_param) FeatureBeerCellParamView abvParamView;
        @BindView(R.id.ibu_param) FeatureBeerCellParamView ibuParamView;
        @BindView(R.id.fav_action) ImageView favView;

        FeaturedViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_featured, parent, false));
            ButterKnife.bind(this, itemView);
        }

        void update(FeaturedItemViewState vs) {
            mImageLoader.load(vs.imageUrl(), labelView);
            titleView.setText(vs.title());
            ImageViewCompat.setImageTintList(srcColorView, ColorStateList.valueOf(vs.srmColor()));
            styleView.setText(vs.style());
            brewedBy.setText(vs.brewedBy());
            srmParamView.setValueText(vs.srmValue());
            abvParamView.setValueText(vs.abvValue());
            ibuParamView.setValueText(vs.ibuValue());
            favView.setSelected(vs.favorite());
        }

        @OnClick(R.id.card)
        void onCardClick(View view) {

        }

        @OnClick(R.id.fav_action)
        void onFavClick(View view) {
            mFavoriteEvents.onNext(FavoriteEvent.create(this.getAdapterPosition(), !favView.isSelected()));
        }
    }

    @AutoValue
    public static abstract class FavoriteEvent {
        public abstract int position();
        public abstract boolean isFavorite();

        public static FavoriteEvent create(int position, boolean isFavorite) {
            return new AutoValue_FeaturedAdapter_FavoriteEvent(position, isFavorite);
        }
    }
}
