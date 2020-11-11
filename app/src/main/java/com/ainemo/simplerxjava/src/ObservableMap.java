package com.ainemo.simplerxjava.src;

class ObservableMap<T, U> extends Observable<U> {
    final ObservableSource<T> source;
    final Function<? super T, ? extends U> function;

    ObservableMap(ObservableSource<T> source, Function<? super T, ? extends U> function) {
        this.source = source;
        this.function = function;
    }

    @Override
    protected void subscribeActual(Observer<? super U> t) {
        source.subscribe(new MapObserver<T, U>(t, function));
    }

    static final class MapObserver<T, U> implements Observer<T> {
        protected final Observer<? super U> downstream;
        final Function<? super T, ? extends U> mapper;

        public MapObserver(Observer<? super U> downstream, Function<? super T, ? extends U> mapper) {
            this.downstream = downstream;
            this.mapper = mapper;
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
        public void onNext(T var) {
            U v;
            try {
                v = mapper.apply(var);
            } catch (Throwable ex) {
                return;
            }

            downstream.onNext(v);
        }
    }
}
