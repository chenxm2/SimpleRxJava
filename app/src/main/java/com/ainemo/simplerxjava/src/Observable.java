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

    public static <T> Observable<T> just(T item1) {

        return fromArray(item1);
    }

    public static <T> Observable<T> just(T item1, T item2) {
        System.out.println("my-Just: threadname2=" + Thread.currentThread().getName() );
        return fromArray(item1, item2);
    }

    public static <T> Observable<T> just(T item1, T item2, T item3) {

        return fromArray(item1, item2, item3);
    }

    public static <T> Observable<T> just(T item1, T item2, T item3, T item4) {

        return fromArray(item1, item2, item3, item4);
    }

    public static <T> Observable<T> fromArray(T... items) {

        return new ObservableFromArray<T>(items);
    }
}
