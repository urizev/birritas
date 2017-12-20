package com.urizev.birritas.app.providers.connectivity;

import io.reactivex.subjects.BehaviorSubject;

public abstract class ConnectivityProvider {
    protected final BehaviorSubject<Boolean> subject;

    protected ConnectivityProvider() {
        subject = BehaviorSubject.create();
    }
}
