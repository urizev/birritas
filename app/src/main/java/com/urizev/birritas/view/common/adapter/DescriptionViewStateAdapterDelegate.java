package com.urizev.birritas.view.common.adapter;

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

public class DescriptionViewStateAdapterDelegate extends ViewStateAdapterDelegate<DescriptionViewStateAdapterDelegate.ViewState,DescriptionViewStateAdapterDelegate.ViewHolder> {
    @Override
    protected boolean isForViewType(@NonNull com.urizev.birritas.view.common.ViewState item, @NonNull List<com.urizev.birritas.view.common.ViewState> items, int position) {
        return item instanceof ViewState;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new ViewHolder(viewItemForViewHolder(parent, R.layout.cell_description));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewState item, @NonNull ViewHolder holder, @NonNull List<Object> payloads) {
        holder.bind(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.description) TextView description;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


        public void bind(ViewState item) {
            description.setText(item.description());
        }
    }

    @AutoValue
    public static abstract class ViewState implements com.urizev.birritas.view.common.ViewState {
        public abstract String description();

        public static ViewState create(String description) {
            return new AutoValue_DescriptionViewStateAdapterDelegate_ViewState(description);
        }
    }
}
