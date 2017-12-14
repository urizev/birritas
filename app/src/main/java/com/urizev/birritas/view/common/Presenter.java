package com.urizev.birritas.view.common;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

public class Presenter<VS extends ViewState> {
    private final BehaviorSubject<VS> mViewStateSubject;
    private final CompositeDisposable mDisposables;

    public Presenter() {
        this.mViewStateSubject = BehaviorSubject.create();
        mDisposables = new CompositeDisposable();
    }

    Observable<VS> observeViewState() {
        return mViewStateSubject;
    }

    protected VS currentViewState() {
        return mViewStateSubject.getValue();
    }

    protected void publishViewState(VS vs) {
        mViewStateSubject.onNext(vs);
    }

    protected void addDisposable(Disposable disposable) {
        mDisposables.add(disposable);
    }

    protected void dispose() {
        mDisposables.dispose();
    }
}
