package com.urizev.birritas.view.beer;

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

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.image.ImageLoader;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.usecases.BeerDetailsUseCase;
import com.urizev.birritas.domain.usecases.FavoritesBeerIdsUseCase;
import com.urizev.birritas.domain.usecases.UpdateFavoriteBeerUseCase;
import com.urizev.birritas.view.beer.adapter.BeerBreweryViewStateAdapterDelegate;
import com.urizev.birritas.view.beer.adapter.IbuAbvSrmViewStateAdapterDelegate;
import com.urizev.birritas.view.beer.adapter.IngredientViewStateAdapterDelegate;
import com.urizev.birritas.view.brewery.BreweryActivity;
import com.urizev.birritas.view.common.PresenterFragment;
import com.urizev.birritas.view.common.ViewState;
import com.urizev.birritas.view.common.ViewStateAdapter;
import com.urizev.birritas.view.common.ViewStateAdapterDelegate;
import com.urizev.birritas.view.common.adapter.DescriptionViewStateAdapterDelegate;
import com.urizev.birritas.view.common.adapter.HeaderViewStateAdapterDelegate;
import com.urizev.birritas.view.common.adapter.ImageViewStateAdapterDelegate;
import com.urizev.birritas.view.common.adapter.SingleLineViewStateAdapterDelegate;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BeerFragment extends PresenterFragment<BeerViewState,PresenterBeerViewState,BeerPresenter> implements ViewStateAdapterDelegate.ViewStateAdapterDelegateClickListener {
    private static final String EXTRA_BEER_ID = "beerId";
    private static final String KEY_MAIN_LIST_STATE = "com.urizev.birritas.view.beer.mainListState";
    private static final String KEY_SIDE_LIST_STATE = "com.urizev.birritas.view.beer.sideListState";

    @BindView(R.id.main_recycler) RecyclerView mMainRecyclerView;
    @Nullable @BindView(R.id.side_recycler) RecyclerView mSideRecyclerView;
    @BindView(R.id.fav_action) View fabAction;

    @Inject ImageLoader mImageLoader;
    @Inject BeerDetailsUseCase mBeerDetailsUseCase;
    @Inject FavoritesBeerIdsUseCase mFavoritesBeerIdsUseCase;
    @Inject UpdateFavoriteBeerUseCase mUpdateFavoriteBeerUseCase;

    @Inject ResourceProvider mResourceProvider;
    private ImmutableList<ViewStateAdapterDelegate> mAdapterDelegates;
    private ViewStateAdapter mMainAdapter;
    private ViewStateAdapter mSideAdapter;
    private Parcelable mMainListState;
    private Parcelable mSideListState;
    private boolean mListStateShouldBeRestored;

    public static Fragment newInstance(String beerId) {
        Fragment fragment = new BeerFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_BEER_ID, beerId);
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
                .add(new ImageViewStateAdapterDelegate(mImageLoader))
                .add(new IbuAbvSrmViewStateAdapterDelegate())
                .add(new HeaderViewStateAdapterDelegate())
                .add(new BeerBreweryViewStateAdapterDelegate(this))
                .add(new SingleLineViewStateAdapterDelegate())
                .add(new DescriptionViewStateAdapterDelegate())
                .add(new IngredientViewStateAdapterDelegate())
                .build();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getApp().getNetComponent().inject(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_beer_detail;
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
    protected BeerPresenter createPresenter(Bundle savedInstanceState) {
        String beerId = getArguments().getString(EXTRA_BEER_ID);
        return new BeerPresenter(beerId, mBeerDetailsUseCase, mFavoritesBeerIdsUseCase, mUpdateFavoriteBeerUseCase, mResourceProvider);
    }

    @Override
    protected void renderViewState(BeerViewState vs) {
        RxUtils.assertMainThread();

        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(vs.name());
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

        fabAction.setSelected(vs.favorite());
    }

    @OnClick(R.id.fav_action)
    public void onFavoriteClicked() {
        getPresenter().onFavoriteClicked(!fabAction.isSelected());
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
    protected BeerViewState prepareViewState(PresenterBeerViewState pvs) {
        ImmutableList.Builder<ViewState> mainBuilder = new ImmutableList.Builder<>();
        ImmutableList.Builder<ViewState> sideBuilder = new ImmutableList.Builder<>();

        ImmutableList.Builder<ViewState> builder = mSideRecyclerView != null ? sideBuilder : mainBuilder;
        builder = builder.add(ImageViewStateAdapterDelegate.ViewState.create(pvs.imageUrl()));
        builder = builder.add(IbuAbvSrmViewStateAdapterDelegate.ViewState.create(pvs.ibu(), pvs.abv(), pvs.srm(), pvs.srmColor()));
        mainBuilder = mainBuilder.add(HeaderViewStateAdapterDelegate.ViewState.create(getResources().getString(R.string.brewed_by)));
        if (pvs.brewedById() != null) {
            mainBuilder = mainBuilder.add(BeerBreweryViewStateAdapterDelegate.ViewState.create(pvs.brewedById(), pvs.brewedBy()));
        }
        mainBuilder = mainBuilder.add(HeaderViewStateAdapterDelegate.ViewState.create(getResources().getString(R.string.style)));
        mainBuilder = mainBuilder.add(SingleLineViewStateAdapterDelegate.ViewState.create(pvs.style()));
        if (!pvs.ingredients().isEmpty()) {
            mainBuilder = mainBuilder.add(HeaderViewStateAdapterDelegate.ViewState.create(getResources().getString(R.string.ingredients)));
            for (String ingredient : pvs.ingredients()) {
                mainBuilder = mainBuilder.add(IngredientViewStateAdapterDelegate.ViewState.create(ingredient));
            }
        }

        return BeerViewState.create(pvs.name(), pvs.imageUrl(), pvs.abv(), pvs.ibu(), pvs.srm(), pvs.srmColor(), mainBuilder.build(), sideBuilder.build(), pvs.favorite());
    }



    @Override
    public void onViewStateAdapterDelegateClicked(ViewState viewState, int position) {
        if (viewState instanceof BeerBreweryViewStateAdapterDelegate.ViewState) {
            BeerBreweryViewStateAdapterDelegate.ViewState vs;
            vs = (BeerBreweryViewStateAdapterDelegate.ViewState) viewState;
            Intent intent = new Intent(getContext(), BreweryActivity.class);
            intent.putExtra(BreweryActivity.EXTRA_BREWERY_ID, vs.id());
            startActivity(intent);
        }
    }
}
