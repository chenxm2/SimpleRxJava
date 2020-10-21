package com.ainemo.simplerxjava.src;
public final class ObservableCreate<T> extends Observable<T> {
    private final ObservableOnSubscribe<T> source;

    public ObservableCreate(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        RxLogger.logger.info("ObservableCreate subscribeActual ");
        CreateEmitter<T> parent = new CreateEmitter<T>(observer);
        source.subscribe(parent);
    }

    static final class CreateEmitter<T> implements Emitter<T>{


        final Observer<? super T> observer;

        CreateEmitter(Observer<? super T> observer) {
            this.observer = observer;
        }

        @Override
        public void onNext(T t) {
            RxLogger.logger.info("CreateEmitter onNext");
            if (t == null) {
                onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
                return;
            }

            observer.onNext(t);
        }

        @Override
        public void onError(Throwable t) {
            observer.onError(t);
        }


        @Override
        public void onComplete() {
            observer.onCompleted();
        }
    }

}
