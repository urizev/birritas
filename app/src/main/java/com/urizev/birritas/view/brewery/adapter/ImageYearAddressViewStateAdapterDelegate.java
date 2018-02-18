package com.urizev.birritas.view.brewery.adapter;

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

public class ImageYearAddressViewStateAdapterDelegate extends ViewStateAdapterDelegate<ImageYearAddressViewStateAdapterDelegate.ViewState,ImageYearAddressViewStateAdapterDelegate.ViewHolder> {
    private final ImageLoader imageLoader;

    public ImageYearAddressViewStateAdapterDelegate(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    @Override
    protected boolean isForViewType(@NonNull com.urizev.birritas.view.common.ViewState item, @NonNull List<com.urizev.birritas.view.common.ViewState> items, int position) {
        return item instanceof ViewState;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new ViewHolder(viewItemForViewHolder(parent, R.layout.cell_brewery_image_year_address));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewState item, @NonNull ViewHolder holder, @NonNull List<Object> payloads) {
        holder.bind(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image) ImageView image;
        @BindView(R.id.established) TextView established;
        @BindView(R.id.address) TextView address;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


        public void bind(ViewState item) {
            imageLoader.load(item.imageUrl(), image);
            established.setText(item.established());
            address.setText(item.address());
        }
    }

    @AutoValue
    public static abstract class ViewState implements com.urizev.birritas.view.common.ViewState {
        @Nullable
        public abstract String imageUrl();
        @Nullable
        public abstract String established();
        @Nullable
        public abstract String address();

        public static ViewState create(String imageUrl, String established, String address) {
            return new AutoValue_ImageYearAddressViewStateAdapterDelegate_ViewState(imageUrl, established, address);
        }
    }
}
