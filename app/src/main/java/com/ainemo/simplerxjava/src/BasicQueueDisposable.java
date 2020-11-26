package com.ainemo.simplerxjava.src;

public abstract class BasicQueueDisposable<T> implements QueueDisposable<T> {

    @Override
    public final boolean offer(T e) {
        throw new UnsupportedOperationException("Should not be called");
    }

    @Override
    public final boolean offer(T v1, T v2) {
        throw new UnsupportedOperationException("Should not be called");
    }
}
