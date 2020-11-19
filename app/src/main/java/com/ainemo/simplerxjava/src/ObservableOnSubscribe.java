package com.ainemo.simplerxjava.src;

import androidx.annotation.NonNull;

public interface ObservableOnSubscribe<T> {

    /**
     * Called for each Observer that subscribes.
     * @param emitter the safe emitter instance, never null
     * @throws Exception on error
     */
    void subscribe(@NonNull Emitter<? super T> emitter);
}
