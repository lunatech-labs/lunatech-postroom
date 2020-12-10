package com.lunatech.postroom;

import java.util.Collections;
import java.util.List;

// TODO, equals and hashcode here?
public final class Valid implements ValidationResult {

  Valid() {}

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public List<String> getErrors() {
    return Collections.emptyList();
  }
}
