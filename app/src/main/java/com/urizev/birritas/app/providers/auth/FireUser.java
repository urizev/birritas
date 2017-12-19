package com.urizev.birritas.app.providers.auth;

import com.google.firebase.auth.FirebaseUser;
import com.urizev.birritas.domain.entities.User;

class FireUser implements User {
    private final FirebaseUser mFireUser;

    FireUser(FirebaseUser fireUser) {
        this.mFireUser = fireUser;
    }

    @Override
    public String getId() {
        return mFireUser.getUid();
    }
}
