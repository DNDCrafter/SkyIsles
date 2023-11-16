package com.thelastflames.skyisles.codec;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.ListBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ListCodec<E> implements Codec<List<E>> {
    Codec<E> internal;

    public ListCodec(Codec<E> internal) {
        this.internal = internal;
    }

    @Override
    public <T> DataResult<Pair<List<E>, T>> decode(DynamicOps<T> ops, T input) {
        DataResult<Stream<T>> t = ops.getStream(input);
        Either<Stream<T>, DataResult.PartialResult<Stream<T>>> res = t.get();
        try {
            Stream<T> val = res.map(l -> l, r -> {
                if (r != null) {
                    throw new RuntimeException(r.message());
                }
                return null;
            });
            ArrayList<E> rRes = new ArrayList<>();
            val.forEach(v -> {
                rRes.add(
                        internal.decode(ops, v).get().map(
                                l -> l,
                                r -> {
                                    if (r != null) {
                                        throw new RuntimeException(r.message());
                                    }
                                    return null;
                                }
                        ).getFirst()
                );
            });
            return DataResult.success(Pair.of(rRes, null));
        } catch (Throwable err) {
            return DataResult.error(() -> "Failed to read stream from " + internal);
        }
    }

    @Override
    public <T> DataResult<T> encode(List<E> input, DynamicOps<T> ops, T prefix) {
        ListBuilder<T> builder = ops.listBuilder();
        for (E e1 : input) {
            builder.add(internal.encode(e1, ops, prefix));
        }
        return builder.build(prefix);
    }
}
