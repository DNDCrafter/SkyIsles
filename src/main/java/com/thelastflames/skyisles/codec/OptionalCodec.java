package com.thelastflames.skyisles.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.PrimitiveCodec;

import java.util.HashMap;
import java.util.Optional;

public class OptionalCodec<E> implements Codec<Optional<E>> {
    Codec<E> internal;

    public OptionalCodec(Codec internal) {
        this.internal = internal;
    }

    @Override
    public <T> DataResult<Pair<Optional<E>, T>> decode(DynamicOps<T> ops, T input) {
        DataResult<MapLike<T>> m = ops.getMap(input);
        MapLike<T> mp = m.get().map(l -> l, r -> null);
        if (mp != null && mp.entries().count() == 0) return DataResult.success(Pair.of(Optional.empty(), input));

        DataResult<Pair<E, T>> dec = internal.decode(ops, input);
        Pair<E, T> t = dec.get().map(l -> l, r -> null);
        if (t == null) return DataResult.success(Pair.of(Optional.empty(), null));

        return DataResult.success(Pair.of(Optional.of(t.getFirst()), t.getSecond()));
    }

    @Override
    public <T> DataResult<T> encode(Optional<E> input, DynamicOps<T> ops, T prefix) {
        if (input.isPresent())
            return internal.encode(input.get(), ops, prefix);

        HashMap<T, T> s = new HashMap<>();
        return ops.mergeToMap(prefix, s);
    }
}
