package com.urizev.birritas.view.common.adapter;

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
import com.urizev.birritas.ui.FeatureBeerCellParamView;
import com.urizev.birritas.view.common.ViewStateAdapterDelegate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BeerSlimViewStateAdapterDelegate extends ViewStateAdapterDelegate<BeerSlimViewStateAdapterDelegate.ViewState,BeerSlimViewStateAdapterDelegate.ViewHolder> {
    private final ImageLoader mImageLoader;

    public BeerSlimViewStateAdapterDelegate(ImageLoader imageLoader, ViewStateAdapterDelegateClickListener listener) {
        super(listener);
        mImageLoader = imageLoader;
    }

    @Override
    protected boolean isForViewType(@NonNull com.urizev.birritas.view.common.ViewState item, @NonNull List<com.urizev.birritas.view.common.ViewState> items, int position) {
        return item instanceof ViewState;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new ViewHolder(viewItemForViewHolder(parent, R.layout.cell_beer_slim));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewState item, @NonNull ViewHolder holder, @NonNull List<Object> payloads) {
        holder.bind(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.cell_favorite_foreground) View foreground;
        @BindView(R.id.cell_favorite_background) View background;
        @BindView(R.id.beer_label)
        ImageView labelView;
        @BindView(R.id.beer_title) TextView titleView;
        @BindView(R.id.brewed_by) TextView brewedBy;
        @BindView(R.id.beer_style) TextView styleView;
        @BindView(R.id.abv_param)
        FeatureBeerCellParamView abvParamView;
        @BindView(R.id.ibu_param) FeatureBeerCellParamView ibuParamView;
        private ViewState mViewState;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(ViewState vs) {
            this.mViewState = vs;
            mImageLoader.load(vs.imageUrl(), labelView);
            titleView.setText(vs.title());
            styleView.setText(vs.style());
            brewedBy.setText(vs.brewedBy());
            abvParamView.setValueText(vs.abvValue());
            ibuParamView.setValueText(vs.ibuValue());
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
        public abstract String id();
        public abstract String title();
        @Nullable
        public abstract String imageUrl();
        public abstract String style();
        public abstract String brewedBy();
        public abstract String ibuValue();
        public abstract String abvValue();

        public static ViewState create(String id, String title, String imageUrl, String style, String brewedBy, String ibuValue, String abvValue) {
            return new AutoValue_BeerSlimViewStateAdapterDelegate_ViewState(id, title, imageUrl, style, brewedBy, ibuValue, abvValue);
        }
    }
}
