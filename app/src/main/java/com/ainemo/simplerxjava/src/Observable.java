package com.ainemo.simplerxjava.src;

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

    public final <R> Observable<R> map(Function<? super T, ? extends R> mapper) {
        return new ObservableMap<T,R>( this,mapper);
    }

    public final Observable<T> filter(Predicate<? super T> predicate) {
        return new ObservableFilter<T>(this, predicate);
    }
}
