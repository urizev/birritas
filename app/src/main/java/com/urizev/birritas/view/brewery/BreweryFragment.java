package com.urizev.birritas.view.brewery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.image.ImageLoader;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.entities.Coordinate;
import com.urizev.birritas.domain.usecases.BreweryDetailsUseCase;
import com.urizev.birritas.view.beer.BeerActivity;
import com.urizev.birritas.view.brewery.adapter.AddressViewStateAdapterDelegate;
import com.urizev.birritas.view.brewery.adapter.BrewerySideMapViewStateAdapterDelegate;
import com.urizev.birritas.view.brewery.adapter.ImageYearAddressViewStateAdapterDelegate;
import com.urizev.birritas.view.brewery.adapter.SideImageViewStateAdapterDelegate;
import com.urizev.birritas.view.common.PresenterFragment;
import com.urizev.birritas.view.common.ViewState;
import com.urizev.birritas.view.common.ViewStateAdapter;
import com.urizev.birritas.view.common.ViewStateAdapterDelegate;
import com.urizev.birritas.view.common.adapter.BeerSlimViewStateAdapterDelegate;
import com.urizev.birritas.view.common.adapter.DescriptionViewStateAdapterDelegate;
import com.urizev.birritas.view.common.adapter.HeaderViewStateAdapterDelegate;
import com.urizev.birritas.view.common.adapter.SingleLineViewStateAdapterDelegate;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BreweryFragment extends PresenterFragment<BreweryViewState,BreweryPresenterViewState,BreweryPresenter> implements ViewStateAdapterDelegate.ViewStateAdapterDelegateClickListener {
    private static final String EXTRA_BREWERY_ID = "beerId";

    @BindView(R.id.main_recycler) RecyclerView mMainRecycler;
    @Nullable @BindView(R.id.side_recycler) RecyclerView mSideRecycler;

    @Inject ImageLoader mImageLoader;
    @Inject BreweryDetailsUseCase mBreweryDetailsUseCase;
    @Inject ResourceProvider mResourceProvider;

    private ViewStateAdapter mMainAdapter;
    private ViewStateAdapter mSideAdapter;
    private ImmutableList<ViewStateAdapterDelegate> mAdapterDelegates;
    private BitmapDescriptor mSelectedMarkerIcon;

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
        mSelectedMarkerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
    }

    private void createAdapterDelegates() {
        mAdapterDelegates = new ImmutableList.Builder<ViewStateAdapterDelegate>()
                .add(new ImageYearAddressViewStateAdapterDelegate(mImageLoader))
                .add(new SideImageViewStateAdapterDelegate(mImageLoader))
                .add(new SingleLineViewStateAdapterDelegate())
                .add(new AddressViewStateAdapterDelegate())
                .add(new DescriptionViewStateAdapterDelegate())
                .add(new HeaderViewStateAdapterDelegate())
                .add(new BeerSlimViewStateAdapterDelegate(mImageLoader, this))
                .add(new BrewerySideMapViewStateAdapterDelegate(getResources()))
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

        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(vs.name());
        }

        mMainAdapter.setItems(vs.mainViewStates());
        mMainAdapter.notifyDataSetChanged();
        if (mSideAdapter != null) {
            mSideAdapter.setItems(vs.sideViewStates());
            mSideAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected boolean bindView(View view) {
        ButterKnife.bind(this, view);

        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        mMainAdapter = new ViewStateAdapter(mAdapterDelegates);
        mMainRecycler.setAdapter(mMainAdapter);
        if (mSideRecycler != null) {
            mSideAdapter = new ViewStateAdapter(mAdapterDelegates);
            mSideRecycler.setAdapter(mSideAdapter);
        }

        return true;
    }

    @Override
    protected BreweryViewState prepareViewState(BreweryPresenterViewState vs) {
        RxUtils.assertComputationThread();

        ImmutableList.Builder<ViewState> mainViewStates = new ImmutableList.Builder<>();
        ImmutableList.Builder<ViewState> sideViewStates = new ImmutableList.Builder<>();
        if (mSideRecycler == null) {
            mainViewStates.add(ImageYearAddressViewStateAdapterDelegate.ViewState.create(vs.imageUrl(), vs.established(), vs.address()));
        }
        else {
            String imageUrl = vs.imageUrl();
            if (imageUrl != null) {
                sideViewStates.add(SideImageViewStateAdapterDelegate.ViewState.create(imageUrl));
            }
            String established = vs.established();
            if (established != null) {
                sideViewStates.add(SingleLineViewStateAdapterDelegate.ViewState.create(established));
            }
            Coordinate coordinate = vs.coordinate();
            if (coordinate != null) {
                MarkerOptions options = new MarkerOptions()
                        .title(vs.name())
                        .icon(mSelectedMarkerIcon)
                        .position(new LatLng(coordinate.latitude(), coordinate.longitude()));
                sideViewStates.add(BrewerySideMapViewStateAdapterDelegate.ViewState.create(options));
            }
            String address = vs.address();
            if (address != null) {
                sideViewStates.add(AddressViewStateAdapterDelegate.ViewState.create(address));
            }
        }

        if (vs.description() != null) {
            mainViewStates.add(DescriptionViewStateAdapterDelegate.ViewState.create(vs.description()));
        }

        if (!vs.beers().isEmpty()) {
            mainViewStates.add(HeaderViewStateAdapterDelegate.ViewState.create(getResources().getString(R.string.n_beers, vs.beers().size())));
            for (BreweryBeerPresenterViewState beer : vs.beers()) {
                mainViewStates.add(BeerSlimViewStateAdapterDelegate.ViewState.create(beer.id(), beer.title(), beer.imageUrl(), beer.style(), beer.brewedBy(), beer.ibuValue(), beer.abvValue()));
            }
        }
        return BreweryViewState.create(vs.name(), vs.imageUrl(), vs.established(), vs.coordinate(), vs.address(), mainViewStates.build(), sideViewStates.build());
    }

    @Override
    public void onViewStateAdapterDelegateClicked(ViewState viewState, int position) {
        if (viewState instanceof BeerSlimViewStateAdapterDelegate.ViewState) {
            BeerSlimViewStateAdapterDelegate.ViewState vs;
            vs = (BeerSlimViewStateAdapterDelegate.ViewState) viewState;
            Intent intent = new Intent(getContext(), BeerActivity.class);
            intent.putExtra(BeerActivity.EXTRA_BEER_ID, vs.id());
            startActivity(intent);
        }
    }
}
