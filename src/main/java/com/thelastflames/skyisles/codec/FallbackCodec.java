package com.thelastflames.skyisles.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.Optional;
import java.util.function.Function;

public class FallbackCodec<E> implements Codec<E> {
    Codec<E> internal;
    Function<String, E> fallback;

    public FallbackCodec(Codec<E> internal, Function<String, E> fallback) {
        this.internal = internal;
        this.fallback = fallback;
    }

    @Override
    public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> ops, T input) {
        DataResult<Pair<E, T>> dec = internal.decode(ops, input);
        Pair<E, T> t = dec.get().map(l -> l, r -> null);
        if (t == null) return DataResult.success(
                Pair.of(
                        fallback.apply(ops.getStringValue(input).getOrThrow(false, (e) -> {
                            throw new RuntimeException(e);
                        })), input
                )
        );
        return DataResult.success(t);
    }

    @Override
    public <T> DataResult<T> encode(E input, DynamicOps<T> ops, T prefix) {
        return internal.encode(input, ops, prefix);
    }
}
