package com.ainemo.simplerxjava.src;

import androidx.annotation.NonNull;

/**
 * Description:
 * Create Time: 2020/11/15 17:16
 */
public interface Function<T, R> {

    R apply(@NonNull T t) throws Exception;
}
