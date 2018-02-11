package com.urizev.birritas.view.beer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.auto.value.AutoValue;
import com.urizev.birritas.R;
import com.urizev.birritas.view.common.ViewStateAdapterDelegate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BeerBreweryViewStateAdapterDelegate extends ViewStateAdapterDelegate<BeerBreweryViewStateAdapterDelegate.ViewState,BeerBreweryViewStateAdapterDelegate.ViewHolder> {
    public BeerBreweryViewStateAdapterDelegate(ViewStateAdapterDelegateClickListener listener) {
        super(listener);
    }

    @Override
    protected boolean isForViewType(@NonNull com.urizev.birritas.view.common.ViewState item, @NonNull List<com.urizev.birritas.view.common.ViewState> items, int position) {
        return item instanceof ViewState;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new ViewHolder(viewItemForViewHolder(parent, R.layout.cell_single_line));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewState item, @NonNull ViewHolder holder, @NonNull List<Object> payloads) {
        holder.bind(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.text) TextView text;
        private ViewState mViewState;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView.setOnClickListener(this);
        }

        public void bind(ViewState item) {
            this.mViewState = item;
            text.setText(item.text());
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
        public abstract String text();

        public static ViewState create(String id, String text) {
            return new AutoValue_BeerBreweryViewStateAdapterDelegate_ViewState(id, text);
        }
    }
}
