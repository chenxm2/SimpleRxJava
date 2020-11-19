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

    public static <T1, T2, R> Observable<R> zip(
            Observable<? extends T1> source1, Observable<? extends T2> source2,
            BiFunction<? super T1, ? super T2, ? extends R> zipper, boolean delayError) {
        return zipArray(Functions.toFunction(zipper), delayError, 8, source1, source2);
    }

    public static <T, R> Observable<R> zipArray(Function<? super Object[], ? extends R> zipper,
                                                boolean delayError, int bufferSize, Observable<? extends T>... sources) {

        return new ObservableZip<T, R>(sources, null, zipper, bufferSize, delayError);
    }


}
