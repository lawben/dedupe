/*
 * The MIT License
 *
 * Copyright (c) 2018 bakdata GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package com.bakdata.deduplication.deduplication;

import com.bakdata.deduplication.fusion.FusedValue;

import java.util.Optional;
import java.util.function.Function;

public interface HardFusionHandler<T> extends Function<FusedValue<T>, Optional<FusedValue<T>>> {
    static <T> HardFusionHandler<T> dontFuse() {
        return partiallyFusedValue -> Optional.empty();
    }

    @Override
    default Optional<FusedValue<T>> apply(FusedValue<T> partiallyFusedValue) {
        return handlePartiallyFusedValue(partiallyFusedValue);
    }

    Optional<FusedValue<T>> handlePartiallyFusedValue(FusedValue<T> partiallyFusedValue);
}
