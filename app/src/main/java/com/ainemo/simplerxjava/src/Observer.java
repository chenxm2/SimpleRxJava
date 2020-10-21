package com.ainemo.simplerxjava.src;

public interface Observer<T> {
    void onCompleted();

    void onError(Throwable t);

    void onNext(T var);
}
