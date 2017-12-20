package com.urizev.birritas.app.providers.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.urizev.birritas.domain.entities.User;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

public class FireAuth implements Auth {
    private final FirebaseAuth mAuth;
    private final BehaviorSubject<User> mUserSubject;

    @Inject
    FireAuth() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser fireUser = mAuth.getCurrentUser();
        if (fireUser != null) {
            Timber.d("User already logged");
            mUserSubject = BehaviorSubject.createDefault(new FireUser(fireUser));
        }
        else {
            Timber.d("User logging…");
            mUserSubject = BehaviorSubject.createDefault(NO_USER);
            mAuth.signInAnonymously().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Timber.d("Logging successfull!");
                    FirebaseUser fUser = mAuth.getCurrentUser();
                    mUserSubject.onNext(new FireUser(fUser));
                } else {
                    Timber.e(task.getException(),"Logging failed!");
                    mUserSubject.onNext(NO_USER);
                }
            });
        }
    }

    @Override
    public Observable<User> observeUser() {
        return mUserSubject;
    }
}
