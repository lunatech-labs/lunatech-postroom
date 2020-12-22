package com.lunatech.postroom;

import io.vavr.control.Either;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Mapping<T> {

  Either<List<FormError>, T> bind(Map<String, String> data);
  Map<String, String> unbind(T value);
  String key();

  // TODO, can we make this internal?
  Mapping<T> withPrefix(String prefix);

  Mapping<T> verifying(Collection<Constraint<T>> constraints);

  default Mapping<T> verifying(Constraint<T> constraint) {
    return verifying(Collections.singleton(constraint));
  }

  default Mapping<T> verifying(Predicate<T> predicate, String msg) {
    return verifying(Constraints.of(predicate, msg));
  }
  default Mapping<T> verifying(Predicate<T> predicate, Function<T, String> msg) {
    return verifying(Constraints.of(predicate, msg));
  }

  default <U> Mapping<U> transform(Function<T, U> map, Function<U, T> contramap) {
    return transformVerifying(t -> ValidationResult.valid(map.apply(t)), contramap);
  }

  default <U> Mapping<U> transformVerifying(Function<T, ValidationResult<U>> map, Function<U, T> contramap) {
    return new TransformedMapping<>(this, map, contramap, Collections.emptyList());
  }
}
