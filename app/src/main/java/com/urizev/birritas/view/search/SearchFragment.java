package com.urizev.birritas.view.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.common.collect.ImmutableList;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.image.ImageLoader;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.domain.usecases.SearchUseCase;
import com.urizev.birritas.view.beer.BeerActivity;
import com.urizev.birritas.view.brewery.BreweryActivity;
import com.urizev.birritas.view.common.DirectPresenterFragment;
import com.urizev.birritas.view.common.ViewState;
import com.urizev.birritas.view.common.ViewStateAdapter;
import com.urizev.birritas.view.common.ViewStateAdapterDelegate;
import com.urizev.birritas.view.common.adapter.HeaderViewStateAdapterDelegate;
import com.urizev.birritas.view.common.adapter.NoResultsViewStateAdapterDelegate;
import com.urizev.birritas.view.search.adapter.SearchResultItemViewStateAdapterDelegate;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.schedulers.Schedulers;

public class SearchFragment extends DirectPresenterFragment<SearchViewState,SearchPresenter> implements ViewStateAdapterDelegate.ViewStateAdapterDelegateClickListener {
    public static final int MIN_QUERY_SIZE = 2;
    private static final long DEBOUNCE_MILLIS = 300;
    private static final String KEY_LIST_STATE = "com.urizev.birritas.view.search.listState";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.search_box) EditText mSearchBox;
    @BindView(R.id.recycler) RecyclerView mRecyclerView;

    private ImmutableList<ViewStateAdapterDelegate> mAdapterDelegates;
    private ViewStateAdapter mAdapter;

    @Inject SearchUseCase mUseCase;
    @Inject ResourceProvider mResourceProvider;
    @Inject ImageLoader mImageLoader;
    private Parcelable mListState;
    private boolean mListStateShouldBeRestored;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createAdapterDelegates();
    }

    private void createAdapterDelegates() {
        mAdapterDelegates = new ImmutableList.Builder<ViewStateAdapterDelegate>()
                .add(new NoResultsViewStateAdapterDelegate())
                .add(new HeaderViewStateAdapterDelegate())
                .add(new SearchResultItemViewStateAdapterDelegate(mImageLoader, this))
                .build();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getApp().getNetComponent().inject(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_search;
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
        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(KEY_LIST_STATE, listState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected SearchPresenter createPresenter(Bundle savedInstanceState) {
        return new SearchPresenter(mUseCase, mResourceProvider);
    }

    @Override
    protected void renderViewState(SearchViewState searchViewState) {
        mAdapter.setItems(searchViewState.items());
        mAdapter.notifyDataSetChanged();
        if (mListStateShouldBeRestored) {
            mListStateShouldBeRestored = false;
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
        }
    }

    @Override
    protected boolean bindView(View view) {
        ButterKnife.bind(this, view);

        getBaseActivity().setSupportActionBar(mToolbar);
        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mAdapter = new ViewStateAdapter(mAdapterDelegates);
        mRecyclerView.setAdapter(mAdapter);

        RxTextView.textChanges(mSearchBox)
                .observeOn(Schedulers.computation())
                .subscribeOn(Schedulers.computation())
                .map(CharSequence::toString)
                .map(String::trim)
                .filter(str -> str.length() > MIN_QUERY_SIZE)
                .debounce(DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
                .doOnNext(query -> getPresenter().search(query))
                .subscribe();

        return true;
    }

    @Override
    public void onViewStateAdapterDelegateClicked(ViewState viewState, int position) {
        SearchResultItemViewStateAdapterDelegate.ViewState vs = (SearchResultItemViewStateAdapterDelegate.ViewState) viewState;

        Intent intent = null;
        switch (vs.type()) {
            case SearchResultItemViewStateAdapterDelegate.ViewState.TYPE_BEER:
                intent = new Intent(getContext(), BeerActivity.class);
                intent.putExtra(BeerActivity.EXTRA_BEER_ID, vs.id());
                break;
            case SearchResultItemViewStateAdapterDelegate.ViewState.TYPE_BREWERY:
                intent = new Intent(getContext(), BreweryActivity.class);
                intent.putExtra(BreweryActivity.EXTRA_BREWERY_ID, vs.id());
                break;
        }

        if (intent != null) {
            getContext().startActivity(intent);
        }
    }
}
