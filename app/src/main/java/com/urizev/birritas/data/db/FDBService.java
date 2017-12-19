package com.urizev.birritas.data.db;

import com.google.common.collect.ImmutableSet;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.urizev.birritas.app.providers.auth.Auth;
import com.urizev.birritas.app.rx.RxFDB;
import com.urizev.birritas.domain.entities.User;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class FDBService {
    private static final String REF_USERS = "users";
    private static final String REF_FAVORITE_BEERS = "favoriteBeers";

    private final FirebaseDatabase db;
    private final Auth auth;

    @Inject
    public FDBService(Auth auth) {
        this.db = FirebaseDatabase.getInstance();
        this.auth = auth;
    }

    private Observable<DatabaseReference> userRef() {
        return auth.observeUser()
                .filter(user -> user != Auth.NO_USER)
                .map(this::userRefForUser);
    }

    private DatabaseReference userRefForUser(User user) {
        return this.db.getReference(REF_USERS).child(user.getId()).child(REF_FAVORITE_BEERS);
    }

    public Observable<ImmutableSet<String>> favoriteBeerIds() {
        return userRef()
                .switchMap(RxFDB::read)
                .map(datasnapshot -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = datasnapshot.getValue(Map.class);
                    if (map != null) {
                        return ImmutableSet.copyOf(map.keySet());
                    } else {
                        return ImmutableSet.of();
                    }
                });
    }

    public Completable markBeerAsFavorite(String id) {
        return userRef()
                .map(databaseReference -> databaseReference.child(id))
                .firstElement()
                .flatMapCompletable(ref -> RxFDB.write(ref, Boolean.TRUE));
    }

    public Completable unmarkBeerAsFavorite(String id) {
        return userRef()
                .map(databaseReference -> databaseReference.child(id))
                .firstElement()
                .flatMapCompletable(ref -> RxFDB.write(ref, null));
    }
}
