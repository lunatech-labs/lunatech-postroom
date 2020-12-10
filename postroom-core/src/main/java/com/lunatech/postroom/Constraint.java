package com.lunatech.postroom;

@FunctionalInterface
public interface Constraint<T> {
  ValidationResult validate(T value);
}
