package com.urizev.birritas.view.favorites;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.domain.usecases.FavoriteBeersUseCase;
import com.urizev.birritas.domain.usecases.UpdateFavoriteBeerUseCase;
import com.urizev.birritas.ui.ErrorView;
import com.urizev.birritas.ui.LoadingView;
import com.urizev.birritas.view.common.DirectPresenterFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class FavoriteBeersFragment extends DirectPresenterFragment<FavoriteBeersViewState,FavoriteBeersPresenter> {
    @BindView(R.id.featured_content) RecyclerView mContentView;
    @BindView(R.id.featured_loading) LoadingView mLoadingView;
    @BindView(R.id.featured_error) ErrorView mErrorView;

    @Inject FavoriteBeersUseCase mFavoriteBeersUseCase;
    @Inject UpdateFavoriteBeerUseCase mUpdateFavoriteBeerUseCase;
    @Inject ResourceProvider mResourceProvider;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.getApp().getNetComponent().inject(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_favorites;
    }

    @Override
    protected FavoriteBeersPresenter createPresenter(Bundle savedInstanceState) {
        return new FavoriteBeersPresenter(mFavoriteBeersUseCase, mUpdateFavoriteBeerUseCase, mResourceProvider);
    }

    @Override
    protected void renderViewState(FavoriteBeersViewState favoriteBeersViewState) {

    }

    @Override
    protected boolean bindView(View view) {
        return false;
    }
}
