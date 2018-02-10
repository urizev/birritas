package com.urizev.birritas.view.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.common.collect.ImmutableList;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.urizev.birritas.R;
import com.urizev.birritas.app.providers.image.ImageLoader;
import com.urizev.birritas.app.providers.resources.ResourceProvider;
import com.urizev.birritas.domain.usecases.SearchUseCase;
import com.urizev.birritas.view.common.DirectPresenterFragment;
import com.urizev.birritas.view.common.ViewStateAdapter;
import com.urizev.birritas.view.common.ViewStateAdapterDelegate;
import com.urizev.birritas.view.common.adapter.HeaderViewStateAdapterDelegate;
import com.urizev.birritas.view.search.adapter.SearchResultItemViewStateAdapterDelegate;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.schedulers.Schedulers;

public class SearchFragment extends DirectPresenterFragment<SearchViewState,SearchPresenter> {
    public static final int MIN_QUERY_SIZE = 2;
    private static final long DEBOUNCE_MILLIS = 300;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.search_box) EditText searchBox;
    @BindView(R.id.recycler) RecyclerView recycler;

    private ImmutableList<ViewStateAdapterDelegate> mAdapterDelegates;
    private ViewStateAdapter mAdapter;

    @Inject SearchUseCase mUseCase;
    @Inject ResourceProvider mResourceProvider;
    @Inject ImageLoader mImageLoader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createAdapterDelegates();
    }

    private void createAdapterDelegates() {
        mAdapterDelegates = new ImmutableList.Builder<ViewStateAdapterDelegate>()
                .add(new HeaderViewStateAdapterDelegate())
                .add(new SearchResultItemViewStateAdapterDelegate(mImageLoader))
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

    @Override
    protected SearchPresenter createPresenter(Bundle savedInstanceState) {
        return new SearchPresenter(mUseCase, mResourceProvider);
    }

    @Override
    protected void renderViewState(SearchViewState searchViewState) {
        mAdapter.setItems(searchViewState.items());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected boolean bindView(View view) {
        ButterKnife.bind(this, view);

        getBaseActivity().setSupportActionBar(toolbar);
        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mAdapter = new ViewStateAdapter(mAdapterDelegates);
        recycler.setAdapter(mAdapter);

        RxTextView.textChanges(searchBox)
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
}
