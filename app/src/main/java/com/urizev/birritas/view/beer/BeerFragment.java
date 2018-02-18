package com.urizev.birritas.view.beer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.image.ImageLoader;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.usecases.BeerDetailsUseCase;
import com.urizev.birritas.domain.usecases.FavoritesBeerIdsUseCase;
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

public class BeerFragment extends PresenterFragment<BeerViewState,PresenterBeerViewState,BeerPresenter> implements ViewStateAdapterDelegate.ViewStateAdapterDelegateClickListener {
    private static final String EXTRA_BEER_ID = "beerId";

    @Nullable
    @BindView(R.id.beer_label) ImageView labelView;
    @Nullable
    @BindView(R.id.srm_value) TextView srmView;
    @Nullable
    @BindView(R.id.ibu_value) TextView ibuView;
    @Nullable
    @BindView(R.id.abv_value) TextView abvView;
    @BindView(R.id.recycler) RecyclerView recyclerView;
    @BindView(R.id.fav_action) View fabAction;

    @Inject ImageLoader mImageLoader;
    @Inject BeerDetailsUseCase mBeerDetailsUseCase;
    @Inject FavoritesBeerIdsUseCase mFavoritesBeerIdsUseCase;
    @Inject ResourceProvider mResourceProvider;
    private ImmutableList<ViewStateAdapterDelegate> mAdapterDelegates;
    private boolean mIsTable;
    private ViewStateAdapter mAdapter;

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

    @Override
    protected BeerPresenter createPresenter(Bundle savedInstanceState) {
        String beerId = getArguments().getString(EXTRA_BEER_ID);
        return new BeerPresenter(beerId, mBeerDetailsUseCase, mFavoritesBeerIdsUseCase, mResourceProvider);
    }

    @Override
    protected void renderViewState(BeerViewState vs) {
        RxUtils.assertMainThread();

        getBaseActivity().getSupportActionBar().setTitle(vs.name());

        if (labelView != null && srmView != null && ibuView != null && abvView != null) {
            mImageLoader.load(vs.imageUrl(), labelView);
            srmView.setText(vs.srm());
            ibuView.setText(vs.ibu());
            abvView.setText(vs.abv());
        }

        mAdapter.setItems(vs.mainViewStates());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected boolean bindView(View view) {
        ButterKnife.bind(this, view);

        mIsTable = getResources().getBoolean(R.bool.is_tablet);

        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        mAdapter = new ViewStateAdapter(mAdapterDelegates);
        recyclerView.setAdapter(mAdapter);

        return true;
    }


    @Override
    protected BeerViewState prepareViewState(PresenterBeerViewState pvs) {
        ImmutableList.Builder<ViewState> builder = new ImmutableList.Builder<>();

        builder = builder.add(ImageViewStateAdapterDelegate.ViewState.create(pvs.imageUrl()));
        builder = builder.add(IbuAbvSrmViewStateAdapterDelegate.ViewState.create(pvs.ibu(), pvs.abv(), pvs.srm(), pvs.srmColor()));
        builder = builder.add(HeaderViewStateAdapterDelegate.ViewState.create(getResources().getString(R.string.brewed_by)));
        builder = builder.add(BeerBreweryViewStateAdapterDelegate.ViewState.create(pvs.brewedById(), pvs.brewedBy()));
        builder = builder.add(HeaderViewStateAdapterDelegate.ViewState.create(getResources().getString(R.string.style)));
        builder = builder.add(SingleLineViewStateAdapterDelegate.ViewState.create(pvs.style()));
        if (!pvs.ingredients().isEmpty()) {
            builder = builder.add(HeaderViewStateAdapterDelegate.ViewState.create(getResources().getString(R.string.ingredients)));
            for (String ingredient : pvs.ingredients()) {
                builder = builder.add(IngredientViewStateAdapterDelegate.ViewState.create(ingredient));
            }
        }

        return BeerViewState.create(pvs.name(), pvs.imageUrl(), pvs.abv(), pvs.ibu(), pvs.srm(), pvs.srmColor(), builder.build());
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
