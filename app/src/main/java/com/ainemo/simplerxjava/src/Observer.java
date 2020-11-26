package com.ainemo.simplerxjava.src;

import androidx.annotation.NonNull;

public interface Observer<T> {
    void onCompleted();

    void onError(Throwable t);

    void onNext(T var);

    void onSubscribe(@NonNull Disposable d);
}
