package com.ainemo.simplerxjava.src;


class ObservableFilter<T> extends Observable<T> {
    final Predicate<? super T> predicate;
    final Observable<T> source;

    public ObservableFilter(Observable<T> source, Predicate<? super T> predicate) {
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        source.subscribe(new FilterObserver<T>(observer, predicate));
    }

    static final class FilterObserver<T> implements Observer<T> {

        final Predicate<? super T> filter;
        final Observer<? super T> downstream;

        FilterObserver(Observer<? super T> actual, Predicate<? super T> filter) {
            this.downstream = actual;
            this.filter = filter;
        }

        @Override
        public void onNext(T t) {
            RxLogger.logger.info("FilterObserver onNext");
            boolean b;
            try {
                b = filter.test(t);
            } catch (Throwable e) {
                onError(e);
                return;
            }
            if (b) {
                downstream.onNext(t);
            }
        }

        @Override
        public void onCompleted() {
            downstream.onCompleted();
        }

        @Override
        public void onError(Throwable t) {
            downstream.onError(t);
        }

    }
}
