package com.urizev.birritas.view.featured;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.image.ImageLoader;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.usecases.FavoritesBeerIdsUseCase;
import com.urizev.birritas.domain.usecases.FeaturedBeersUseCase;
import com.urizev.birritas.domain.usecases.UpdateFavoriteBeerUseCase;
import com.urizev.birritas.ui.MessageView;
import com.urizev.birritas.ui.LoadingView;
import com.urizev.birritas.view.common.DirectPresenterFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class FeaturedFragment extends DirectPresenterFragment<FeaturedViewState,FeaturedPresenter> {
    private static final String KEY_LIST_STATE = "com.urizev.birritas.view.featured.listState";

    @BindView(R.id.featured_content) RecyclerView mContentView;
    @BindView(R.id.featured_loading) LoadingView mLoadingView;
    @BindView(R.id.featured_error) MessageView mMessageView;

    @Inject ImageLoader mImageLoader;
    @Inject ResourceProvider mResourceProvider;
    @Inject FeaturedBeersUseCase mFeaturedBeerUseCase;
    @Inject FavoritesBeerIdsUseCase mFavoriteBeerIdsUseCase;
    @Inject UpdateFavoriteBeerUseCase mUpdateFavoriteBeerUseCase;

    private FeaturedAdapter mAdapter;
    private Parcelable mListState;
    private boolean mListStateShouldBeRestored;

    public FeaturedFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getApp().getNetComponent().inject(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_featured;
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
    protected FeaturedPresenter createPresenter(Bundle savedInstanceState) {
        return new FeaturedPresenter(mFeaturedBeerUseCase, mFavoriteBeerIdsUseCase, mUpdateFavoriteBeerUseCase, mResourceProvider);
    }

    @Override
    protected void renderViewState(FeaturedViewState viewState) {
        RxUtils.assertMainThread();

        mMessageView.setVisibility(View.INVISIBLE);
        mContentView.setVisibility(View.INVISIBLE);
        mLoadingView.setVisibility(View.INVISIBLE);

        Throwable error = viewState.error();
        if (error != null) {
            Timber.e(error);
            mMessageView.setMessage(error.getMessage());
            mMessageView.setVisibility(View.VISIBLE);
        }
        else if (viewState.loading()) {
            mLoadingView.setVisibility(View.VISIBLE);
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
        mAdapter = new FeaturedAdapter(mImageLoader);
        addDisposable(mAdapter.favoriteEvents()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .doOnNext(e -> getPresenter().onFavoriteEvent(e))
                .subscribe());
        mContentView.setAdapter(mAdapter);
        return true;
    }

    @OnClick(R.id.featured_error)
    protected void onErrorClicked() {
        getPresenter().loadData();
    }
}
