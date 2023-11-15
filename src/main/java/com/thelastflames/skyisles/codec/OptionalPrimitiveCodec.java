package com.thelastflames.skyisles.codec;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.PrimitiveCodec;

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
    public <T> T write(final DynamicOps<T> ops, final Optional<E> value) {
        if (value.isPresent())
            return writeValue(ops, value.get());
        else return ops.empty();
    }

    @Override
    public String toString() {
        return "Long";
    }
}
