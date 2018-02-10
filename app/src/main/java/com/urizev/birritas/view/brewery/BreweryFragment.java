package com.urizev.birritas.view.brewery;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.image.ImageLoader;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.usecases.BreweryDetailsUseCase;
import com.urizev.birritas.view.brewery.adapter.ImageYearAddressViewStateAdapterDelegate;
import com.urizev.birritas.view.common.PresenterFragment;
import com.urizev.birritas.view.common.ViewState;
import com.urizev.birritas.view.common.ViewStateAdapter;
import com.urizev.birritas.view.common.ViewStateAdapterDelegate;
import com.urizev.birritas.view.common.adapter.DescriptionViewStateAdapterDelegate;
import com.urizev.birritas.view.common.adapter.HeaderViewStateAdapterDelegate;
import com.urizev.birritas.view.favorites.FavoriteBeersItemViewState;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BreweryFragment extends PresenterFragment<BreweryViewState,BreweryPresenterViewState,BreweryPresenter> {
    private static final String EXTRA_BREWERY_ID = "beerId";

    @BindView(R.id.main_recycler) RecyclerView mMainRecycler;

    @Inject ImageLoader mImageLoader;
    @Inject BreweryDetailsUseCase mBreweryDetailsUseCase;
    @Inject ResourceProvider mResourceProvider;

    private ViewStateAdapter mMainAdapter;
    private ImmutableList<ViewStateAdapterDelegate> mAdapterDelegates;
    private boolean mTablet;

    public static Fragment newInstance(String beerId) {
        Fragment fragment = new BreweryFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_BREWERY_ID, beerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createAdapterDelegates();
    }

    private void createAdapterDelegates() {
        mAdapterDelegates = new ImmutableList.Builder<ViewStateAdapterDelegate>()
                .add(new ImageYearAddressViewStateAdapterDelegate(mImageLoader))
                .add(new DescriptionViewStateAdapterDelegate())
                .add(new HeaderViewStateAdapterDelegate())
                .build();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getApp().getNetComponent().inject(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_brewery_detail;
    }

    @Override
    protected BreweryPresenter createPresenter(Bundle savedInstanceState) {
        String breweryId = getArguments().getString(EXTRA_BREWERY_ID);
        return new BreweryPresenter(breweryId, mBreweryDetailsUseCase, mResourceProvider);
    }

    @Override
    protected void renderViewState(BreweryViewState vs) {
        RxUtils.assertMainThread();

        mMainAdapter.setItems(vs.mainViewStates());
        mMainAdapter.notifyDataSetChanged();
    }

    @Override
    protected boolean bindView(View view) {
        ButterKnife.bind(this, view);

        mTablet = getResources().getBoolean(R.bool.is_tablet);

        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        mMainAdapter = new ViewStateAdapter(mAdapterDelegates);
        mMainRecycler.setAdapter(mMainAdapter);

        return true;
    }

    @Override
    protected BreweryViewState prepareViewState(BreweryPresenterViewState vs) {
        ImmutableList.Builder<ViewState> mainViewStates = new ImmutableList.Builder<>();
        if (!mTablet) {
            mainViewStates.add(ImageYearAddressViewStateAdapterDelegate.ViewState.create(vs.imageUrl(), vs.established(), vs.address()));
        }
        mainViewStates.add(DescriptionViewStateAdapterDelegate.ViewState.create(vs.description()));
        mainViewStates.add(HeaderViewStateAdapterDelegate.ViewState.create(getResources().getString(R.string.n_beers, vs.beers().size())));
        for (BreweryBeerPresenterViewState beer : vs.beers()) {
            mainViewStates.add(FavoriteBeersItemViewState.create(beer.id(), beer.title(), beer.imageUrl(), beer.style(), beer.brewedBy(), beer.ibuValue(), beer.abvValue()));
        }
        return BreweryViewState.create(vs.imageUrl(), vs.established(), vs.coordinate(), vs.address(), mainViewStates.build());
    }
}
