package com.urizev.birritas.view.beer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.image.ImageLoader;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.app.rx.RxUtils;
import com.urizev.birritas.domain.usecases.BeerDetailsUseCase;
import com.urizev.birritas.domain.usecases.FavoritesBeerIdsUseCase;
import com.urizev.birritas.view.common.DirectPresenterFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BeerFragment extends DirectPresenterFragment<BeerViewState,BeerPresenter> {
    private static final String EXTRA_BEER_ID = "beerId";

    @BindView(R.id.beer_label) ImageView labelView;
    @BindView(R.id.beer_style) TextView styleView;
    @BindView(R.id.brewed_by) TextView brewedByView;
    @BindView(R.id.srm_value) TextView srmView;
    @BindView(R.id.ibu_value) TextView ibuView;
    @BindView(R.id.abv_value) TextView abvView;
    @BindView(R.id.fav_action) View fabAction;

    @Inject ImageLoader imageLoader;
    @Inject BeerDetailsUseCase mBeerDetailsUseCase;
    @Inject FavoritesBeerIdsUseCase mFavoritesBeerIdsUseCase;
    @Inject ResourceProvider mResourceProvider;

    public static Fragment newInstance(String beerId) {
        Fragment fragment = new BeerFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_BEER_ID, beerId);
        fragment.setArguments(args);
        return fragment;
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

        imageLoader.load(vs.imageUrl(), labelView);
        brewedByView.setText(vs.brewedBy());
        styleView.setText(vs.style());
        srmView.setText(vs.srm());
        ibuView.setText(vs.ibu());
        abvView.setText(vs.abv());
    }

    @Override
    protected boolean bindView(View view) {
        ButterKnife.bind(this, view);

        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        return true;
    }
}
