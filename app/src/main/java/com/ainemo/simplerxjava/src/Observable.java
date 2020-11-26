package com.ainemo.simplerxjava.src;

import androidx.annotation.NonNull;

public abstract class Observable<T> {

    protected Observable() {
    }

    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
        return new ObservableCreate<T>(source);
    }

    public final Observable<? extends T> subscribeOn(Scheduler scheduler) {
        return new ObservableSubscribeOn<T>(this, scheduler);
    }

    public final void subscribe(Observer<? super T> observer) {
        subscribeActual(observer);
    }

    protected abstract void subscribeActual(Observer<? super T> observer);


    public final Observable<? extends T> observerOn(Scheduler scheduler) {
        return new ObservableObserveOn<T>(this, scheduler);
    }

    @NonNull
    public static <T> Observable<T> fromArray(T... items) {
        return new ObservableFromArray<T>(items);
    }



}
