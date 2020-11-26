package com.ainemo.simplerxjava.src;

public interface QueueFuseable<T> extends SimpleQueue<T> {
    int NONE = 0;
    int SYNC = 1;
    int ASYNC = 2;
    int ANY = SYNC | ASYNC;
    int BOUNDARY = 4;
    int requestFusion(int mode);

}
