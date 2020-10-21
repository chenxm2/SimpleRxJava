package com.ainemo.simplerxjava.src;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.logging.Logger;

public class Schedulers {
    private static  Scheduler ioScheuler;
    private static Scheduler mainScheuler;

    public static Scheduler io() {
        if (ioScheuler == null) {
            ioScheuler = new Scheduler(new SubThreadExecutor());
        }


        return ioScheuler;
    }

    public static Scheduler main() {
        if (mainScheuler == null) {
            mainScheuler = new Scheduler(new MainThreadExecutor());
        }


        return mainScheuler;
    }



    private static class SubThreadExecutor implements Executor {
        private HandlerThread handlerThread;
        private Handler handler;


        private  SubThreadExecutor() {
            handlerThread = new HandlerThread("SubThread");
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        }

        @Override
        public void execute(Runnable r) {
            final Logger logger =  Logger.getLogger("simpleRxjava");

            if (handler != null) {
                handler.post(r);
            } else  {
                logger.info("main execute null");
            }
        }
    }

    private static class MainThreadExecutor implements Executor {
        private Handler handler;


        private  MainThreadExecutor() {
            handler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void execute(Runnable r) {
            if (handler != null) {
                handler.post(r);
            } else  {
                RxLogger.logger.info("MainThreadExecutor handler null");
            }
        }
    }
}
