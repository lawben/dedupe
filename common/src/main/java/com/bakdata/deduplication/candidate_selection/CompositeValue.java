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
package com.bakdata.deduplication.candidate_selection;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CompositeValue<T extends Comparable<?>> implements Comparable<CompositeValue<T>> {
    @Singular
    List<T> components;

    @SafeVarargs
    public static <T extends Comparable<?>> CompositeValue<T> of(T... values) {
        for (T value : values) {
            if(value == null) {
                return null;
            }
        }
        return new CompositeValue<>(List.of(values));
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(CompositeValue<T> o) {
        for (int index = 0; index < components.size(); index++) {
            int result = ((Comparable<Object>) components.get(index)).compareTo(o.components.get(index));
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}
