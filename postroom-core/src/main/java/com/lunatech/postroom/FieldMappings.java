package com.lunatech.postroom;

import java.util.Collections;
import java.util.Optional;

public class FieldMappings {

  public static <T> Mapping<T> of(Formatter<T> formatter) {
    return new FieldMapping<>(formatter);
  }

  public static Mapping<String> text() {
    return of(Formatters.stringFormatter);
  }

  public static Mapping<String> nonEmptyText() {
    return text().verifying(Constraints.of(value -> !value.trim().isEmpty(),
            (__) -> "Value should not be empty"));
  }

  public static Mapping<Integer> integer() {
    return new FieldMapping<>(Formatters.integerFormatter);
  }

  public static Mapping<Integer> integer(int minValue, int maxValue) {
    return integer()
        .verifying(Constraints.of(value -> value >= minValue && value <= maxValue, (__) ->
                "Value should be between " + minValue + " and " + maxValue + ", inclusive"));
  }

  public static <T> Mapping<Optional<T>> optional(Mapping<T> underlying) {
    return new OptionalMapping<>(underlying, Collections.emptyList());
  }

}
