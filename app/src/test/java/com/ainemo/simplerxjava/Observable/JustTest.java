package com.ainemo.simplerxjava.Observable;

import com.ainemo.simplerxjava.src.Observable;
import com.ainemo.simplerxjava.src.Observer;

import org.junit.Test;

public class JustTest {
    @Test
    public void testJust(){

        Observable.just("1-1", "1-2").subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onNext(String var) {
                System.out.println(var);

            }
        });
    }
}
