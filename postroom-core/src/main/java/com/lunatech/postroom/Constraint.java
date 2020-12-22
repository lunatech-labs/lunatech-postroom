package com.lunatech.postroom;

@FunctionalInterface
public interface Constraint<T> {
  ValidationResult<T> validate(T value);
}
