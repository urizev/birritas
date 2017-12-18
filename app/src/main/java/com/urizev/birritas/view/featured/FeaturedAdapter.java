package com.urizev.birritas.view.featured;

import android.content.res.ColorStateList;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.ImageLoader;
import com.urizev.birritas.ui.FeatureBeerCellParamView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {
    private final ImageLoader imageLoader;
    private ImmutableList<FeaturedItemViewState> viewStates;

    FeaturedAdapter(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    @Override
    public FeaturedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeaturedViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(FeaturedViewHolder holder, int position) {
        holder.update(viewStates.get(position));
    }

    @Override
    public int getItemCount() {
        return viewStates == null ? 0 : viewStates.size();
    }

    void update(ImmutableList<FeaturedItemViewState> viewStates) {
        this.viewStates = viewStates;
        this.notifyDataSetChanged();
    }

    class FeaturedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            imageLoader.load(vs.imageUrl(), labelView);
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
        @Override
        public void onClick(View view) {

        }
    }
}
