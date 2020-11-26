package com.ainemo.simplerxjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ainemo.simplerxjava.src.Disposable;
import com.ainemo.simplerxjava.src.Emitter;
import com.ainemo.simplerxjava.src.Observable;
import com.ainemo.simplerxjava.src.ObservableOnSubscribe;
import com.ainemo.simplerxjava.src.Observer;
import com.ainemo.simplerxjava.src.RxLogger;
import com.ainemo.simplerxjava.src.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.no_change_thread_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleNoChangeThreadButtonClicked();
            }
        });

        findViewById(R.id.change_thread_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleChangeThreadButtonClicked();
            }
        });

        findViewById(R.id.from_array).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleFromArray();
            }
        });
    }


    private void handleFromArray(){
        Observable.fromArray(1,2,3).subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onNext(Integer var) {
                RxLogger.logger.info("vale" + var);
            }

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }
        });
    }

    private void handleNoChangeThreadButtonClicked() {
        Observable.create(new ObservableOnSubscribe<String >() {
            @Override
            public void subscribe(@NonNull Emitter<String> emitter) {
                RxLogger.logger.info("subscribe thread = " + Thread.currentThread().getName());
                emitter.onNext("No Change Thread");

            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onNext(String var) {
                RxLogger.logger.info("final Observer thread = " + Thread.currentThread().getName());
                RxLogger.logger.info("final onNext onNext = " + var);
            }

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }
        });
    }

    private void handleChangeThreadButtonClicked() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull Emitter<String> emitter) {
                RxLogger.logger.info("subscribe thread = " + Thread.currentThread().getName());
                emitter.onNext("Change Thread");
            }
        }).subscribeOn(Schedulers.io()).observerOn(Schedulers.main()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onNext(String var) {
                RxLogger.logger.info("final Observer thread = " + Thread.currentThread().getName());
                RxLogger.logger.info("final onNext = " + var);
            }

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }
        });
    }
}
