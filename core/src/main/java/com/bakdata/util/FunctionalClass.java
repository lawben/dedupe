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
package com.bakdata.util;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FunctionalClass<R> {
    @NonNull
    Class<R> clazz;

    public static <T> FunctionalClass<T> from(Class<T> clazz) {
        return new FunctionalClass<>(clazz);
    }

    public <F> Field<R, F> field(String name) {
        PropertyDescriptor descriptor = getPropertyDescriptor(name);
        return new Field<>(descriptor);
    }

    public Supplier<R> getConstructor() {
        try {
            Constructor<R> ctor = clazz.getDeclaredConstructor();
            return new FunctionalConstructor<>(ctor)::invoke;
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    private PropertyDescriptor getPropertyDescriptor(String name) {
        try {
            return new PropertyDescriptor(name, clazz);
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Value
    public static class Field<R, F> {

        @NonNull
        PropertyDescriptor descriptor;

        public Function<R, F> getGetter() {
            Method getter = descriptor.getReadMethod();
            return new FunctionalMethod<>(getter)::invoke;
        }

        public BiConsumer<R, F> getSetter() {
            Method setter = descriptor.getWriteMethod();
            return new FunctionalMethod<>(setter)::invoke;
        }

    }
}
