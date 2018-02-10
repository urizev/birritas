package com.urizev.birritas.view.common;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate;

import java.lang.ref.WeakReference;

public abstract class ViewStateAdapterDelegate<VS extends ViewState, VH extends RecyclerView.ViewHolder> extends AbsListItemAdapterDelegate<VS,ViewState,VH> {
    protected final WeakReference<ViewStateAdapterDelegateClickListener> mListener;

    protected ViewStateAdapterDelegate() {
        mListener = new WeakReference<>(null);
    }

    protected ViewStateAdapterDelegate(ViewStateAdapterDelegateClickListener listener) {
        mListener = new WeakReference<>(listener);
    }

    @NonNull
    protected View viewItemForViewHolder(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    public interface ViewStateAdapterDelegateClickListener {
        void onViewStateAdapterDelegateClicked(ViewState viewState, int position);
    }
}
