package com.ainemo.simplerxjava.src;

import java.util.concurrent.Executor;

public class Scheduler {

    // main commit 1

    final Executor executor;

    public Scheduler(Executor executor) {
        this.executor = executor;
    }

    public Worker createWorker() {
        return new Worker(executor);
    }

    public static  class Worker {
        final Executor executor;

        public Worker (Executor executor) {
            this.executor = executor;
        }

        public void schedule(Runnable runnable) {
            executor.execute(runnable);
        }
    }

    // main commit 2

}
