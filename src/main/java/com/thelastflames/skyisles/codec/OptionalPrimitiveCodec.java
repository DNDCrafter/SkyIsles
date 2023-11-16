package com.thelastflames.skyisles.codec;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.PrimitiveCodec;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class OptionalPrimitiveCodec<E> implements PrimitiveCodec<Optional<E>> {
    protected abstract <T> T writeValue(DynamicOps<T> ops, E e);

    protected abstract E getValue(Number num);

    @Override
    public MapCodec<Optional<E>> fieldOf(String name) {
        return PrimitiveCodec.super.fieldOf(name).orElse(Optional.empty());
    }

    @Override
    public <T> DataResult<Optional<E>> read(final DynamicOps<T> ops, final T input) {
        DataResult<Number> res = ops
                .getNumberValue(input);

        Either<Number, DataResult.PartialResult<Number>> either = res.get();
        Number num = either.map((l) -> l, r -> null);

        if (num == null) return DataResult.success(Optional.empty());
        return DataResult.success(Optional.of(getValue(num)));
    }

    @Override
    public <T> DataResult<Pair<Optional<E>, T>> decode(DynamicOps<T> ops, T input) {
        DataResult<MapLike<T>> m = ops.getMap(input);
        MapLike<T> mp = m.get().map(l -> l, r -> null);
        if (mp != null && mp.entries().count() == 0) return DataResult.success(Pair.of(Optional.empty(), input));

        return PrimitiveCodec.super.decode(ops, input);
    }

    @Override
    public <T> DataResult<T> encode(Optional<E> input, DynamicOps<T> ops, T prefix) {
        if (input.isPresent())
            return DataResult.success(writeValue(ops, input.get()));
        else {
            HashMap<T, T> s = new HashMap<>();
            return ops.mergeToMap(prefix, s);
        }
    }

    @Override
    public String toString() {
        return "Long";
    }

    @Override
    public <T> T write(DynamicOps<T> ops, Optional<E> value) {
        throw new RuntimeException();
    }
}
