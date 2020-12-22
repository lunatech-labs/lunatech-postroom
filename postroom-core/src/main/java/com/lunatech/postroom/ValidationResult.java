package com.lunatech.postroom;

import java.util.List;
import java.util.function.Function;

public interface ValidationResult<T> {

  boolean isValid();
  List<String> getErrors();
  <U> U fold(
          Function<List<String>, U> onInvalid,
          Function<T, U> onValid);

  static <T> ValidationResult<T> valid(T value) {
    return Valid.of(value);
  }

  static <T> ValidationResult<T> invalid(String error, String... moreErrors) {
    return Invalid.of(error, moreErrors);
  }

}
