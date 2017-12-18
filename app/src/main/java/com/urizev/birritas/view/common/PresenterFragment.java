package com.urizev.birritas.view.common;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.urizev.birritas.app.base.BaseFragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Creado por jcvallejo en 29/11/17.
 */

public abstract class PresenterFragment<VS extends ViewState, PVS extends ViewState, P extends Presenter<PVS>> extends BaseFragment {
    private P mPresenter;
    private Disposable mDisposable;

    protected abstract @LayoutRes
    int getLayoutRes();
    protected abstract P createPresenter(Bundle savedInstanceState);
    protected abstract void renderViewState(VS vs);
    protected abstract boolean bindView(View view);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
        mDisposable = mPresenter.observeViewState()
                .map(this::prepareViewState)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(this::renderViewState);
    }

    protected abstract VS prepareViewState(PVS pvs);

    @Override
    public void onDestroyView() {
        mDisposable.dispose();
        super.onDestroyView();
    }

    protected P getPresenter() {
        return mPresenter;
    }
}
