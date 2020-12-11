package com.lunatech.postroom;

public class FieldMappings {

  public static Mapping<String> text() {
    return new FieldMapping<>(Formatters.stringFormatter);
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

}
