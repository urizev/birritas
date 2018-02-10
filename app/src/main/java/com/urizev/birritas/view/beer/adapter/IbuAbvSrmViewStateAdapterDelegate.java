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

public class IbuAbvSrmViewStateAdapterDelegate extends ViewStateAdapterDelegate<IbuAbvSrmViewStateAdapterDelegate.ViewState, IbuAbvSrmViewStateAdapterDelegate.ViewHolder> {
    @Override
    protected boolean isForViewType(@NonNull com.urizev.birritas.view.common.ViewState item, @NonNull List<com.urizev.birritas.view.common.ViewState> items, int position) {
        return item instanceof ViewState;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new ViewHolder(viewItemForViewHolder(parent, R.layout.cell_beer_ibu_abv_srm));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewState item, @NonNull ViewHolder holder, @NonNull List<Object> payloads) {
        holder.bind(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ibu_value) TextView ibu;
        @BindView(R.id.abv_value) TextView abv;
        @BindView(R.id.srm_value) TextView srm;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


        public void bind(ViewState item) {
            ibu.setText(item.ibu());
            abv.setText(item.abv());
            srm.setText(item.srm());
        }
    }

    @AutoValue
    public static abstract class ViewState implements com.urizev.birritas.view.common.ViewState {
        public abstract String ibu();
        public abstract String abv();
        public abstract String srm();
        public abstract int srmColor();

        public static ViewState create(String ibu, String abv, String srm, int srmColor) {
            return new AutoValue_IbuAbvSrmViewStateAdapterDelegate_ViewState(ibu, abv, srm, srmColor);
        }
    }
}
