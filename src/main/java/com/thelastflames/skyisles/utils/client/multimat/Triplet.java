package com.thelastflames.skyisles.utils.client.multimat;

public class Triplet<T, V, U> {
    T t;
    V v;
    U u;

    public Triplet(T t, V v, U u) {
        this.t = t;
        this.v = v;
        this.u = u;
    }

    public T getFirst() {
        return t;
    }

    public V getSecond() {
        return v;
    }

    public U getThird() {
        return u;
    }

    public static <T, V, U> Triplet<T, V, U> of(T t, V v, U u) {
        return new Triplet<>(t, v, u);
    }
}
