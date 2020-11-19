package com.ainemo.simplerxjava.src;

public final class Functions {
    /**
     * Utility class.
     */
    private Functions() {
        throw new IllegalStateException("No instances!");
    }

    public static <T1, T2, R> Function<Object[], R> toFunction(final BiFunction<? super T1, ? super T2, ? extends R> f) {
        return new Array2Func<T1, T2, R>(f);
    }

    static final class Array2Func<T1, T2, R> implements Function<Object[], R> {
        final BiFunction<? super T1, ? super T2, ? extends R> f;

        Array2Func(BiFunction<? super T1, ? super T2, ? extends R> f) {
            this.f = f;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R apply(Object[] a) throws Exception {
            if (a.length != 2) {
                throw new IllegalArgumentException("Array of size 2 expected but got " + a.length);
            }
            return f.apply((T1) a[0], (T2) a[1]);
        }
    }
}
