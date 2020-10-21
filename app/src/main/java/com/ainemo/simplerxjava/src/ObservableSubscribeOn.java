package com.ainemo.simplerxjava.src;


public class ObservableSubscribeOn<T> extends Observable<T> {
    private Scheduler scheduler;
    private Observable<T> source;
    public ObservableSubscribeOn(Observable<T> source, Scheduler scheduler) {
        this.source = source;
        this.scheduler = scheduler;
    }

    @Override
    protected void subscribeActual(final Observer<? super T> observer) {
        RxLogger.logger.info("ObservableSubscribeOn subscribeActual ");
        final SubscribeOnObserver<T> parent = new SubscribeOnObserver<T>(observer, scheduler);

        scheduler.createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                source.subscribe(parent);
            }
        });

    }

    static final class SubscribeOnObserver<T> implements Observer<T> {
        private Scheduler scheduler;
        final Observer<? super T> downstream;

        SubscribeOnObserver(Observer<? super T> downstream, Scheduler scheduler) {
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
            RxLogger.logger.info("SubscribeOnObserver onNext");
            downstream.onNext(var);
        }
    }
}
