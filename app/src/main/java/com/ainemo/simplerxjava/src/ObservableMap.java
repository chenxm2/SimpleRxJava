package com.ainemo.simplerxjava.src;


public final class ObservableMap<T, U> extends Observable<U> {
    private final Observable<T> source;
    final Function<? super T, ? extends U> function;

    public ObservableMap(Observable<T> source, Function<? super T, ? extends U> function) {
        this.source = source;
        this.function = function;
    }

    @Override
    protected void subscribeActual(Observer<? super U> t) {
        source.subscribe(new MapObserver<T, U>(t, function));
    }

    static final class MapObserver<T, U> implements Observer<T> {

        final Function<? super T, ? extends U> mapper;
        final Observer<? super U> downstream;

        MapObserver(Observer<? super U> actual, Function<? super T, ? extends U> mapper) {
            this.downstream = actual;
            this.mapper = mapper;
        }

        @Override
        public void onNext(T t) {
            RxLogger.logger.info("MapObserver onNext");
            U v = null;
            try {
                v = mapper.apply(t);
            } catch (Exception e) {
                onError(e);
                e.printStackTrace();
            }
            if (v == null) {
                onError(new NullPointerException("The mapper function returned a null value."));
                return;
            }
            downstream.onNext(v);
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
