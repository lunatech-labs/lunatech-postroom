package com.lunatech.postroom;

import java.util.List;

public interface ValidationResult {

  boolean isValid();
  List<String> getErrors();

}
