package com.urizev.birritas.view.common;

public abstract class DirectPresenterFragment<VS extends ViewState, P extends Presenter<VS>> extends PresenterFragment<VS,VS,P> {
    @Override
    protected VS prepareViewState(VS vs) {
        return vs;
    }
}
