package com.ainemo.simplerxjava.src;

import androidx.annotation.NonNull;


public interface Predicate<T> {
    /**
     * Test the given input value and return a boolean.
     * @param t the value
     * @return the boolean result
     * @throws Exception on error
     */
    boolean test(@NonNull T t) throws Exception;
}
