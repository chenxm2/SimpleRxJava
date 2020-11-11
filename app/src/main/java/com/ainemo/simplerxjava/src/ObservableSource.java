package com.ainemo.simplerxjava.src;

import androidx.annotation.NonNull;

interface ObservableSource<T> {
    void subscribe(@NonNull Observer<? super T> observer);
}
