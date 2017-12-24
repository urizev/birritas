package com.urizev.birritas.view.favorites;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class FavoriteBeersTouchCallback extends ItemTouchHelper.SimpleCallback {
    private final PublishSubject<Integer> mSubject;

    FavoriteBeersTouchCallback() {
        super(0, ItemTouchHelper.END | ItemTouchHelper.START);
        this.mSubject = PublishSubject.create();
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mSubject.onNext(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((FavoriteBeersAdapter.ViewHolder) viewHolder).foreground;
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((FavoriteBeersAdapter.ViewHolder) viewHolder).foreground;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((FavoriteBeersAdapter.ViewHolder) viewHolder).foreground;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((FavoriteBeersAdapter.ViewHolder) viewHolder).foreground;

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);    }

    Observable<Integer> deletionEvents() {
        return mSubject;
    }
}
