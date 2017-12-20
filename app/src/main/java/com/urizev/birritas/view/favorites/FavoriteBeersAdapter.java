package com.urizev.birritas.view.favorites;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.image.ImageLoader;
import com.urizev.birritas.ui.FeatureBeerCellParamView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

class FavoriteBeersAdapter extends RecyclerView.Adapter<FavoriteBeersAdapter.ViewHolder> {
    private final ImageLoader mImageLoader;
    private ImmutableList<FavoriteBeersItemViewState> mViewStates;
    private PublishSubject<Integer> mDeletionEvents;

    FavoriteBeersAdapter(ImageLoader imageLoader) {
        this.mImageLoader = imageLoader;
        this.mDeletionEvents = PublishSubject.create();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(mViewStates.get(position));
    }

    public Observable<Integer> deletionEvents() {
        return mDeletionEvents;
    }

    @Override
    public int getItemCount() {
        return mViewStates == null ? 0 : mViewStates.size();
    }

    void update(ImmutableList<FavoriteBeersItemViewState> viewStates) {
        this.mViewStates = viewStates;
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.beer_label) ImageView labelView;
        @BindView(R.id.beer_title) TextView titleView;
        @BindView(R.id.brewed_by) TextView brewedBy;
        @BindView(R.id.beer_style) TextView styleView;
        @BindView(R.id.abv_param) FeatureBeerCellParamView abvParamView;
        @BindView(R.id.ibu_param) FeatureBeerCellParamView ibuParamView;
        @BindView(R.id.fav_action) ImageView favView;

        ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_favorite, parent, false));
            ButterKnife.bind(this, itemView);
        }

        void update(FavoriteBeersItemViewState vs) {
            mImageLoader.load(vs.imageUrl(), labelView);
            titleView.setText(vs.title());
            styleView.setText(vs.style());
            brewedBy.setText(vs.brewedBy());
            abvParamView.setValueText(vs.abvValue());
            ibuParamView.setValueText(vs.ibuValue());
        }

        @OnClick(R.id.cell_favorite)
        void onCellClick(View view) {

        }
    }
}
