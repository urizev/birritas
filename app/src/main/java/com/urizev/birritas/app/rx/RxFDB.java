package com.urizev.birritas.app.rx;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class RxFDB {
    public static Observable<DataSnapshot> read(DatabaseReference ref) {
        return Observable.create(e -> {
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!e.isDisposed()) {
                        e.onNext(dataSnapshot);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (!e.isDisposed()) {
                        e.onError(databaseError.toException());
                    }
                }
            };

            e.setDisposable(new Disposable() {
                private boolean mDisposed;

                @Override
                public void dispose() {
                    mDisposed = true;
                    ref.removeEventListener(listener);
                }

                @Override
                public boolean isDisposed() {
                    return mDisposed;
                }
            });

            ref.addValueEventListener(listener);
        });
    }

    public static Completable write(DatabaseReference ref, Object value) {
        return Completable.create(e -> {
            OnCompleteListener<Void> listener = task -> {
                if (e.isDisposed()) {
                    return;
                }

                if (task.isSuccessful()) {
                    e.onComplete();
                }
                else {
                    e.onError(task.getException());
                }
            };
            Task<Void> task = ref.setValue(value);
            task.addOnCompleteListener(listener);

            e.setDisposable(new Disposable() {
                private boolean mDisposable;

                @Override
                public void dispose() {
                    mDisposable = true;
                }

                @Override
                public boolean isDisposed() {
                    return mDisposable;
                }
            });
        });
    }
}
