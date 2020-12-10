package com.lunatech.postroom;

public class FieldMappings {

  public static Mapping<String> text() {
    return new StandardMapping<>(Formatters.stringFormatter);
  }

  public static Mapping<String> nonEmptyText() {
    return text().withConstraint(Constraints.of(value -> !value.trim().isEmpty(), "Value should not be empty"));
  }

  public static Mapping<Integer> integer() {
    return new StandardMapping<>(Formatters.integerFormatter);
  }

  public static Mapping<Integer> integer(int minValue, int maxValue) {
    String message = "Value should be between " + minValue + " and " + maxValue + ", inclusive";
    return integer()
        .withConstraint(Constraints.of(value -> value >= minValue && value <= maxValue, message));
  }

}
