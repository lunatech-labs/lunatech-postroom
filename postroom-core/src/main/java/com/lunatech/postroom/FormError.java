package com.lunatech.postroom;

import java.util.Objects;

public class FormError {

  private final String error;
  private final String key;

  public FormError(String key, String error) {
    this.key = Objects.requireNonNull(key, "key must not be null");
    this.error = Objects.requireNonNull(error, "Error must not be null");
  }

  public String error() {
    return error;
  }

  public String key() {
    return key;
  }

  @Override
  public String toString() {
    return "FormError(" + key + ", " + error + ")";
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FormError formError = (FormError) o;
    return error.equals(formError.error) &&
        key.equals(formError.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(error, key);
  }
}
