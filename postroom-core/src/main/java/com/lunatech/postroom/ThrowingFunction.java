package com.lunatech.postroom;

@FunctionalInterface
public interface ThrowingFunction<T, U> {
    T apply(U value) throws Exception;
}
