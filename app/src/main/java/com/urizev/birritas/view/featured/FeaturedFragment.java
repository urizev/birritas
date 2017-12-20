package com.urizev.birritas.view.featured;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.image.ImageLoader;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.usecases.FavoritesBeerIdsUseCase;
import com.urizev.birritas.domain.usecases.FeaturedBeersUseCase;
import com.urizev.birritas.domain.usecases.UpdateFavoriteBeerUseCase;
import com.urizev.birritas.ui.ErrorView;
import com.urizev.birritas.ui.LoadingView;
import com.urizev.birritas.view.common.DirectPresenterFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class FeaturedFragment extends DirectPresenterFragment<FeaturedViewState,FeaturedPresenter> {
    @BindView(R.id.featured_content) RecyclerView mContentView;
    @BindView(R.id.featured_loading) LoadingView mLoadingView;
    @BindView(R.id.featured_error) ErrorView mErrorView;

    @Inject ImageLoader mImageLoader;
    @Inject ResourceProvider mResourceProvider;
    @Inject FeaturedBeersUseCase mFeaturedBeerUseCase;
    @Inject FavoritesBeerIdsUseCase mFavoriteBeerIdsUseCase;
    @Inject UpdateFavoriteBeerUseCase mUpdateFavoriteBeerUseCase;

    private FeaturedAdapter mAdapter;

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

    @Override
    protected FeaturedPresenter createPresenter(Bundle savedInstanceState) {
        return new FeaturedPresenter(mFeaturedBeerUseCase, mFavoriteBeerIdsUseCase, mUpdateFavoriteBeerUseCase, mResourceProvider);
    }

    @Override
    protected void renderViewState(FeaturedViewState viewState) {
        RxUtils.assertMainThread();

        mErrorView.setVisibility(View.INVISIBLE);
        mContentView.setVisibility(View.INVISIBLE);
        mLoadingView.setVisibility(View.INVISIBLE);

        Throwable error = viewState.error();
        if (error != null) {
            Timber.e(error);
            mErrorView.setMessage(error.getMessage());
            mErrorView.setVisibility(View.VISIBLE);
        }
        else if (viewState.loading()) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
        else {
            mAdapter.update(viewState.viewStates());
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
}
