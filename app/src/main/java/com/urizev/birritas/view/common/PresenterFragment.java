package com.urizev.birritas.view.common;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.urizev.birritas.app.base.BaseFragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Creado por jcvallejo en 29/11/17.
 */

public abstract class PresenterFragment<VS extends ViewState, PVS extends ViewState, P extends Presenter<PVS>> extends BaseFragment {
    private P mPresenter;
    private CompositeDisposable mDisposables;

    protected abstract @LayoutRes
    int getLayoutRes();
    protected abstract P createPresenter(Bundle savedInstanceState);
    protected abstract void renderViewState(VS vs);
    protected abstract boolean bindView(View view);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mDisposables = new CompositeDisposable();
        if (mPresenter == null) {
            mPresenter = createPresenter(savedInstanceState);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutRes(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (bindView(view)) {
            bindPresenter();
        }
    }

    protected void bindPresenter() {
        addDisposable(mPresenter.observeViewState()
                .map(this::prepareViewState)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(this::renderViewState));
    }

    protected void addDisposable(Disposable disposable) {
        mDisposables.add(disposable);
    }

    protected abstract VS prepareViewState(PVS pvs);

    @Override
    public void onDestroy() {
        mDisposables.dispose();
        mPresenter.dispose();
        super.onDestroy();
    }

    protected P getPresenter() {
        return mPresenter;
    }
}
