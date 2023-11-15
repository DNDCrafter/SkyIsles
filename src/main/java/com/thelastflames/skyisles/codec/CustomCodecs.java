package com.thelastflames.skyisles.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;

import java.util.Optional;

public class CustomCodecs {
    public static final Codec<Optional<Long>> OPTIONAL_LONG = new OptionalPrimitiveCodec<>() {
        @Override
        protected <T> T writeValue(DynamicOps<T> ops, Long aLong) {
            return ops.createLong(aLong);
        }

        @Override
        protected Long getValue(Number num) {
            return num.longValue();
        }

        @Override
        public String toString() {
            return "Optional Long";
        }
    };
}
