package com.urizev.birritas.view.favorites;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.image.ImageLoader;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.usecases.FavoriteBeersUseCase;
import com.urizev.birritas.domain.usecases.UpdateFavoriteBeerUseCase;
import com.urizev.birritas.ui.LoadingView;
import com.urizev.birritas.ui.MessageView;
import com.urizev.birritas.view.common.DirectPresenterFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FavoriteBeersFragment extends DirectPresenterFragment<FavoriteBeersViewState,FavoriteBeersPresenter> {
    private static final String KEY_LIST_STATE = "com.urizev.birritas.view.favorites.listState";

    @BindView(R.id.favorites_content) RecyclerView mContentView;
    @BindView(R.id.favorites_loading) LoadingView mLoadingView;
    @BindView(R.id.favorites_error)
    MessageView mMessageView;

    @Inject FavoriteBeersUseCase mFavoriteBeersUseCase;
    @Inject UpdateFavoriteBeerUseCase mUpdateFavoriteBeerUseCase;
    @Inject ResourceProvider mResourceProvider;
    @Inject ImageLoader mImageLoader;

    private FavoriteBeersAdapter mAdapter;
    private FavoriteBeersTouchCallback mTouchCallback;
    private Disposable mTouchDisposable;
    private Parcelable mListState;
    private boolean mListStateShouldBeRestored;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.getApp().getNetComponent().inject(this);
        this.mTouchCallback = new FavoriteBeersTouchCallback();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_favorites;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_LIST_STATE)) {
            this.mListState = savedInstanceState.getParcelable(KEY_LIST_STATE);
            this.mListStateShouldBeRestored = true;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Parcelable listState = mContentView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(KEY_LIST_STATE, listState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected FavoriteBeersPresenter createPresenter(Bundle savedInstanceState) {
        return new FavoriteBeersPresenter(mFavoriteBeersUseCase, mUpdateFavoriteBeerUseCase, mResourceProvider);
    }

    @Override
    protected void renderViewState(FavoriteBeersViewState viewState) {
        RxUtils.assertMainThread();

        mMessageView.setVisibility(View.INVISIBLE);
        mContentView.setVisibility(View.INVISIBLE);
        mLoadingView.setVisibility(View.INVISIBLE);

        if (viewState.loading()) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
        else if (viewState.viewStates().isEmpty()) {
            mMessageView.setVisibility(View.VISIBLE);
            mMessageView.setMessage(getString(R.string.no_favorites));
            mMessageView.setIcon(R.drawable.ic_favorite_empty_24dp);
        }
        else {
            mAdapter.update(viewState.viewStates());
            if (mListStateShouldBeRestored) {
                mListStateShouldBeRestored = false;
                mContentView.getLayoutManager().onRestoreInstanceState(mListState);
            }
            mContentView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected boolean bindView(View view) {
        ButterKnife.bind(this, view);
        mAdapter = new FavoriteBeersAdapter(mImageLoader);
        mContentView.setAdapter(mAdapter);
        new ItemTouchHelper(mTouchCallback)
                .attachToRecyclerView(mContentView);
        mTouchDisposable = mTouchCallback.deletionEvents()
                .doOnNext(p -> getPresenter().deleteFavorite(p))
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe();

        return true;
    }

    @Override
    public void onDestroyView() {
        if (mTouchDisposable != null) {
            mTouchDisposable.dispose();
            mTouchDisposable = null;
        }
        super.onDestroyView();
    }
}
