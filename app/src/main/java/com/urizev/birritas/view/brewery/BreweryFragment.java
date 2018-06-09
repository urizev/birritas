package com.urizev.birritas.view.brewery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.urizev.birritas.ui.LoadingView;
import com.urizev.birritas.ui.MessageView;
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
import butterknife.OnClick;

public class BreweryFragment extends PresenterFragment<BreweryViewState,BreweryPresenterViewState,BreweryPresenter> implements ViewStateAdapterDelegate.ViewStateAdapterDelegateClickListener {
    private static final String EXTRA_BREWERY_ID = "beerId";
    private static final String KEY_MAIN_LIST_STATE = "com.urizev.birritas.view.brewery.mainListState";
    private static final String KEY_SIDE_LIST_STATE = "com.urizev.birritas.view.brewery.sideListState";

    @BindView(R.id.content) View mContentView;
    @BindView(R.id.loading) LoadingView mLoadingView;
    @BindView(R.id.error) MessageView mMessageView;
    @BindView(R.id.main_recycler) RecyclerView mMainRecyclerView;
    @Nullable @BindView(R.id.side_recycler) RecyclerView mSideRecyclerView;

    @Inject ImageLoader mImageLoader;
    @Inject BreweryDetailsUseCase mBreweryDetailsUseCase;
    @Inject ResourceProvider mResourceProvider;

    private ViewStateAdapter mMainAdapter;
    @Nullable private ViewStateAdapter mSideAdapter;
    private ImmutableList<ViewStateAdapterDelegate> mAdapterDelegates;
    private BitmapDescriptor mSelectedMarkerIcon;
    private Parcelable mMainListState;
    @Nullable private Parcelable mSideListState;
    private boolean mListStateShouldBeRestored;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_MAIN_LIST_STATE)) {
            this.mMainListState = savedInstanceState.getParcelable(KEY_MAIN_LIST_STATE);
            this.mSideListState = savedInstanceState.getParcelable(KEY_SIDE_LIST_STATE);
            this.mListStateShouldBeRestored = true;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Parcelable mainListState = mMainRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(KEY_MAIN_LIST_STATE, mainListState);
        if (mSideRecyclerView != null) {
            Parcelable sideListState = mSideRecyclerView.getLayoutManager().onSaveInstanceState();
            outState.putParcelable(KEY_SIDE_LIST_STATE, sideListState);
        }
        super.onSaveInstanceState(outState);
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

        mContentView.setVisibility(View.INVISIBLE);
        mLoadingView.setVisibility(View.INVISIBLE);
        mMessageView.setVisibility(View.INVISIBLE);
        Throwable throwable = vs.throwable();
        if (vs.loading()) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
        else if (throwable != null) {
            mMessageView.setVisibility(View.VISIBLE);
            mMessageView.setMessage(throwable.getLocalizedMessage());
        }
        else {
            mContentView.setVisibility(View.VISIBLE);
        }

        mMainAdapter.setItems(vs.mainViewStates());
        mMainAdapter.notifyDataSetChanged();
        if (mListStateShouldBeRestored) {
            mListStateShouldBeRestored = false;
            mMainRecyclerView.getLayoutManager().onRestoreInstanceState(mMainListState);
        }
        if (mSideRecyclerView != null && mSideAdapter != null) {
            mSideAdapter.setItems(vs.sideViewStates());
            mSideAdapter.notifyDataSetChanged();
            if (mListStateShouldBeRestored && mSideListState != null) {
                mListStateShouldBeRestored = false;
                mSideRecyclerView.getLayoutManager().onRestoreInstanceState(mSideListState);
            }
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
        mMainRecyclerView.setAdapter(mMainAdapter);
        if (mSideRecyclerView != null) {
            mSideAdapter = new ViewStateAdapter(mAdapterDelegates);
            mSideRecyclerView.setAdapter(mSideAdapter);
        }

        return true;
    }

    @Override
    protected BreweryViewState prepareViewState(BreweryPresenterViewState vs) {
        RxUtils.assertComputationThread();

        ImmutableList.Builder<ViewState> mainViewStates = new ImmutableList.Builder<>();
        ImmutableList.Builder<ViewState> sideViewStates = new ImmutableList.Builder<>();
        if (mSideRecyclerView == null) {
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
        return BreweryViewState.create(vs.loading(), vs.throwable(), vs.name(), vs.imageUrl(), vs.established(), vs.coordinate(), vs.address(), mainViewStates.build(), sideViewStates.build());
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

    @OnClick(R.id.error)
    protected void onErrorClicked() {
        getPresenter().reloadBrewery();
    }
}
