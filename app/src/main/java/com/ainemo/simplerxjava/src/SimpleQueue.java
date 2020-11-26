package com.ainemo.simplerxjava.src;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface SimpleQueue<T> {

    /**
     * Atomically enqueue a single.
     * @param value the value to enqueue, not null
     * @return true if successful, false if the value was not enqueued
     * likely due to reaching the queue capacity)
     */
    boolean offer(@NonNull T value);

    /**
     * Atomically enqueue two values.
     * @param v1 the first value to enqueue, not null
     * @param v2 the second value to enqueue, not null
     * @return true if successful, false if the value was not enqueued
     * likely due to reaching the queue capacity)
     */
    boolean offer(@NonNull T v1, @NonNull T v2);

    /**
     * Tries to dequeue a value (non-null) or returns null if
     * the queue is empty.
     * <p>
     * If the producer uses {@link #offer(Object, Object)} and
     * when polling in pairs, if the first poll() returns a non-null
     * item, the second poll() is guaranteed to return a non-null item
     * as well.
     * @return the item or null to indicate an empty queue
     * @throws Exception if some pre-processing of the dequeued
     * item (usually through fused functions) throws.
     */
    @Nullable
    T poll() throws Exception;

    /**
     * Returns true if the queue is empty.
     * <p>
     * Note however that due to potential fused functions in {@link #poll()}
     * it is possible this method returns false but then poll() returns null
     * because the fused function swallowed the available item(s).
     * @return true if the queue is empty
     */
    boolean isEmpty();

    /**
     * Removes all enqueued items from this queue.
     */
    void clear();
}