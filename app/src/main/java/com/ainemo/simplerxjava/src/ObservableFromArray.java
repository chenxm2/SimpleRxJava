package com.ainemo.simplerxjava.src;

public final class ObservableFromArray<T> extends Observable<T> {
    final T[] array;
    public ObservableFromArray(T[] array) {
        this.array = array;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        FromArrayDisposable<T> d = new FromArrayDisposable<T>(observer, array);

        d.run();
    }

    static final class FromArrayDisposable<T>  {

        final Observer<? super T> downstream;

        final T[] array;


        FromArrayDisposable(Observer<? super T> actual, T[] array) {
            this.downstream = actual;
            this.array = array;
        }




        void run() {
            T[] a = array;
            int n = a.length;

            for (int i = 0; i < n ; i++) {
                T value = a[i];
                if (value == null) {
                    downstream.onError(new NullPointerException("The element at index " + i + " is null"));
                    return;
                }
                downstream.onNext(value);
            }

        }
    }
}
