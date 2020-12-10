package com.lunatech.postroom;

import io.vavr.control.Either;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Constraints {

  private final static Valid VALID = new Valid();

  public static <T> Constraint<T> of(Predicate<T> predicate, String message) {
    return value -> {
      if (predicate.test(value)) {
        return VALID;
      } else {
        return new Invalid(message);
      }
    };
  }


  static <T> Either<List<String>, T> applyConstraints(Collection<Constraint<T>> constraints, T value) {
    List<String> errors = constraints.stream().flatMap(constraint ->
        constraint.validate(value).getErrors().stream()).collect(Collectors.toList());

    if(errors.isEmpty()) {
      return Either.right(value);
    } else {
      return Either.left(errors);
    }
  }
}
