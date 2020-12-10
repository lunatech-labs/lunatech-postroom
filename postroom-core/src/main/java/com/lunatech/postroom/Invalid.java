package com.lunatech.postroom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Invalid implements ValidationResult {

  private final List<String> errors;

  Invalid(String error, String... moreErrors) {
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
}
