package com.urizev.birritas.app.rx;

import io.reactivex.disposables.Disposable;

public abstract class SimpleDisposable implements Disposable {
    private boolean mIsDisposed;

    @Override
    public void dispose() {
        mIsDisposed = true;
        this.onDisposed();
    }

    protected abstract void onDisposed();

    @Override
    public boolean isDisposed() {
        return mIsDisposed;
    }
}
