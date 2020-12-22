package com.lunatech.postroom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

final class Invalid<T> implements ValidationResult<T> {

  private final List<String> errors;

  static <T> Invalid<T> of(String error, String... moreErrors) {
    return new Invalid(error, moreErrors);
  }

  private Invalid(String error, String... moreErrors) {
    errors = new ArrayList<>();
    errors.add(error);
    errors.addAll(Arrays.asList(moreErrors));
  }

  @Override
  public boolean isValid() {
    return false;
  }

  @Override
  public List<String> getErrors() {
    return errors;
  }

  @Override
  public <U> U fold(Function<List<String>, U> onInvalid, Function<T, U> onValid) {
    return onInvalid.apply(errors);
  }

}
