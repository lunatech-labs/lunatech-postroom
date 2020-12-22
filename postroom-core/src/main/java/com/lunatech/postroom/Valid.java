package com.lunatech.postroom;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

// TODO, equals and hashcode here?
final class Valid<T> implements ValidationResult<T> {

  private final T value;

  static <T> Valid<T> of(T value) {
    return new Valid<>(value);
  }
  private Valid(T value) {
    this.value = value;
  }

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public List<String> getErrors() {
    return Collections.emptyList();
  }

  @Override
  public <U> U fold(Function<List<String>, U> onInvalid, Function<T, U> onValid) {
    return onValid.apply(value);
  }

}
