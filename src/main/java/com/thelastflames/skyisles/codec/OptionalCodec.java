package com.thelastflames.skyisles.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.Optional;

public class OptionalCodec<E> implements Codec<Optional<E>> {
    Codec<E> internal;

    public OptionalCodec(Codec internal) {
        this.internal = internal;
    }

    @Override
    public <T> DataResult<Pair<Optional<E>, T>> decode(DynamicOps<T> ops, T input) {
        DataResult<Pair<E, T>> dec = internal.decode(ops, input);
        Pair<E, T> t = dec.get().map(l -> l, r -> null);
        if (t == null) return DataResult.success(Pair.of(Optional.empty(), null));
        return DataResult.success(Pair.of(Optional.of(t.getFirst()), t.getSecond()));
    }

    @Override
    public <T> DataResult<T> encode(Optional<E> input, DynamicOps<T> ops, T prefix) {
        if (input.isPresent())
            return internal.encode(input.get(), ops, prefix);
        return ops.mergeToPrimitive(prefix, ops.empty());
    }
}
