package com.urizev.birritas.app.providers.auth;

import com.urizev.birritas.domain.entities.User;

import io.reactivex.Observable;

public interface Auth {
    User NO_USER = () -> "";

    Observable<User> observeUser();
}
