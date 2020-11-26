package com.ainemo.simplerxjava.src;

import androidx.annotation.NonNull;

public class ObservableObserveOn<T> extends Observable<T> {
    final Scheduler scheduler;
    private Observable<T> source;

    public ObservableObserveOn(Observable<T> observable, Scheduler scheduler) {
        this.scheduler = scheduler;
        this.source = observable;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        RxLogger.logger.info("ObservableObserveOn subscribeActual ");
        final ObserveOnObserver<T> parent = new ObserveOnObserver<T>(observer, scheduler);
        source.subscribe(parent);

    }

    static final class ObserveOnObserver<T> implements Observer<T> {
        final Observer<? super T> downstream;
        private Scheduler scheduler;

        ObserveOnObserver(Observer<? super T> downstream, Scheduler scheduler) {
            this.downstream = downstream;
            this.scheduler = scheduler;
        }

        @Override
        public void onCompleted() {
            downstream.onCompleted();
        }

        @Override
        public void onError(Throwable t) {
            downstream.onError(t);
        }

        @Override
        public void onNext(final T var) {
            RxLogger.logger.info("ObserveOnObserver onNext");
            scheduler.createWorker().schedule(new Runnable() {
                @Override
                public void run() {
                    downstream.onNext(var);
                }
            });
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }
    }
}
