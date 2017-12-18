package com.urizev.birritas.view.common;

import com.urizev.birritas.app.rx.RxUtils;

public abstract class DirectPresenterFragment<VS extends ViewState, P extends Presenter<VS>> extends PresenterFragment<VS,VS,P> {
    @Override
    protected VS prepareViewState(VS vs) {
        RxUtils.assertComputationThread();
        return vs;
    }
}
