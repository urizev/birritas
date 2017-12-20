package com.urizev.birritas.data.db;

import com.google.common.collect.ImmutableSet;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.urizev.birritas.app.providers.auth.Auth;
import com.urizev.birritas.app.rx.RxFDB;
import com.urizev.birritas.domain.entities.User;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import timber.log.Timber;

public class FDBService {
    private static final GenericTypeIndicator<Map<String,Boolean>> TYPE_GET_FAVORITES = new GenericTypeIndicator<Map<String,Boolean>>(){};
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
                .doOnNext(u -> Timber.d("Unfiltered user %s", u.getId()))
                .filter(user -> user != Auth.NO_USER)
                .doOnNext(u -> Timber.d("User %s", u.getId()))
                .map(this::userRefForUser)
                .doOnNext(ref -> Timber.d("Reference %s", ref));
    }

    private DatabaseReference userRefForUser(User user) {
        return this.db.getReference(REF_USERS).child(user.getId()).child(REF_FAVORITE_BEERS);
    }

    public Observable<ImmutableSet<String>> favoriteBeerIds() {
        return userRef()
                .switchMap(RxFDB::read)
                .map(datasnapshot -> {
                    Timber.d("Getting values from %s", datasnapshot.getKey());
                    Map<String,Boolean> map = datasnapshot.getValue(TYPE_GET_FAVORITES);
                    Timber.d("%s", map);
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
