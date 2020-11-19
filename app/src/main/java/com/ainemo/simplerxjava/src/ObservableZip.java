package com.ainemo.simplerxjava.src;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableZip<T, R> extends Observable<R> {

    final Observable<? extends T>[] sources;
    final Iterable<? extends Observable<? extends T>> sourcesIterable;
    final Function<? super Object[], ? extends R> zipper;
    final int bufferSize;
    final boolean delayError;

    public ObservableZip(Observable<? extends T>[] sources,
                         Iterable<? extends Observable<? extends T>> sourcesIterable,
                         Function<? super Object[], ? extends R> zipper,
                         int bufferSize,
                         boolean delayError) {
        this.sources = sources;
        this.sourcesIterable = sourcesIterable;
        this.zipper = zipper;
        this.bufferSize = bufferSize;
        this.delayError = delayError;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void subscribeActual(Observer<? super R> observer) {
        Observable<? extends T>[] sources = this.sources;
        int count = 0;
        if (sources == null) {
            sources = new Observable[8];
            for (Observable<? extends T> p : sourcesIterable) {
                if (count == sources.length) {
                    Observable<? extends T>[] b = new Observable[count + (count >> 2)];
                    System.arraycopy(sources, 0, b, 0, count);
                    sources = b;
                }
                sources[count++] = p;
            }
        } else {
            count = sources.length;
        }

        if (count == 0) {
            return;
        }

        ZipCoordinator<T, R> zc = new ZipCoordinator<T, R>(observer, zipper, count, delayError);
        zc.subscribe(sources, bufferSize);
    }

    static final class ZipCoordinator<T, R> extends AtomicInteger {

        private static final long serialVersionUID = 2983708048395377667L;
        final Observer<? super R> downstream;
        final Function<? super Object[], ? extends R> zipper;
        final ZipObserver<T, R>[] observers;
        final T[] row;
        final boolean delayError;

        volatile boolean cancelled;

        @SuppressWarnings("unchecked")
        ZipCoordinator(Observer<? super R> actual,
                       Function<? super Object[], ? extends R> zipper,
                       int count, boolean delayError) {
            this.downstream = actual;
            this.zipper = zipper;
            this.observers = new ZipObserver[count];
            this.row = (T[]) new Object[count];
            this.delayError = delayError;
        }

        public void subscribe(Observable<? extends T>[] sources, int bufferSize) {
            ZipObserver<T, R>[] s = observers;
            int len = s.length;
            for (int i = 0; i < len; i++) {
                s[i] = new ZipObserver<T, R>(this, bufferSize);
            }
            // this makes sure the contents of the observers array is visible
            this.lazySet(0);
            for (int i = 0; i < len; i++) {
                if (cancelled) {
                    return;
                }
                sources[i].subscribe(s[i]);
            }
        }


        void cancel() {
            clear();
            cancelSources();
        }

        void cancelSources() {
        }

        void clear() {
            for (ZipObserver<?, ?> zs : observers) {
                zs.queue.clear();
            }
        }

        public void drain() {
            if (getAndIncrement() != 0) {
                return;
            }

            int missing = 1;

            final ZipObserver<T, R>[] zs = observers;
            final Observer<? super R> a = downstream;
            final T[] os = row;
            final boolean delayError = this.delayError;

            for (; ; ) {

                for (; ; ) {
                    int i = 0;
                    int emptyCount = 0;
                    for (ZipObserver<T, R> z : zs) {
                        if (os[i] == null) {
                            boolean d = z.done;
                            T v = z.queue.poll();
                            boolean empty = v == null;

                            if (checkTerminated(d, empty, a, delayError, z)) {
                                return;
                            }
                            if (!empty) {
                                os[i] = v;
                            } else {
                                emptyCount++;
                            }
                        } else {
                            if (z.done && !delayError) {
                                Throwable ex = z.error;
                                if (ex != null) {
                                    cancelled = true;
                                    cancel();
                                    a.onError(ex);
                                    return;
                                }
                            }
                        }
                        i++;
                    }

                    if (emptyCount != 0) {
                        break;
                    }

                    R v;
                    try {
                        v = zipper.apply(os.clone());
                    } catch (Throwable ex) {
                        cancel();
                        a.onError(ex);
                        return;
                    }

                    a.onNext(v);

                    Arrays.fill(os, null);
                }

                missing = addAndGet(-missing);
                if (missing == 0) {
                    return;
                }
            }
        }

        boolean checkTerminated(boolean d, boolean empty, Observer<? super R> a, boolean delayError, ZipObserver<?, ?> source) {
            if (cancelled) {
                cancel();
                return true;
            }

            if (d) {
                if (delayError) {
                    if (empty) {
                        Throwable e = source.error;
                        cancelled = true;
                        cancel();
                        if (e != null) {
                            a.onError(e);
                        } else {
                            a.onCompleted();
                        }
                        return true;
                    }
                } else {
                    Throwable e = source.error;
                    if (e != null) {
                        cancelled = true;
                        cancel();
                        a.onError(e);
                        return true;
                    } else if (empty) {
                        cancelled = true;
                        cancel();
                        a.onCompleted();
                        return true;
                    }
                }
            }

            return false;
        }
    }

    static final class ZipObserver<T, R> implements Observer<T> {

        final ZipCoordinator<T, R> parent;
        final ArrayDeque<T> queue;

        volatile boolean done;
        Throwable error;


        ZipObserver(ZipCoordinator<T, R> parent, int bufferSize) {
            this.parent = parent;
            this.queue = new ArrayDeque<T>(bufferSize);
        }


        @Override
        public void onNext(T t) {
            queue.offer(t);
            parent.drain();
        }

        @Override
        public void onCompleted() {
            done = true;
            parent.drain();
        }

        @Override
        public void onError(Throwable t) {
            error = t;
            done = true;
            parent.drain();
        }


    }
}