package com.lunatech.postroom;

import io.vavr.control.Either;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Constraints {

  public static <T> Constraint<T> of(Predicate<T> predicate, Function<T, String> message) {
    return value -> {
      if (predicate.test(value)) {
        return Valid.of(value);
      } else {
        return Invalid.of(message.apply(value));
      }
    };
  }

  public static <T> Constraint<T> of(Predicate<T> predicate, String message) {
    return of(predicate, (__) -> message);
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
