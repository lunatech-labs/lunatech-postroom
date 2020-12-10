package com.lunatech.postroom;

import io.vavr.control.Either;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class StandardMapping<T> implements Mapping<T> {

  private final String key;
  private final List<Constraint<T>> constraints;
  private final Formatter<T> formatter;

  StandardMapping(Formatter<T> formatter) {
    this("", Collections.emptyList(), formatter);
  }

  StandardMapping(String key, List<Constraint<T>> constraints, Formatter<T> formatter) {
    this.key = key;
    this.constraints = constraints;
    this.formatter = formatter;
  }

  @Override
  public Either<List<FormError>, T> bind(Map<String, String> data) {
    return formatter
        .bind(key, data).flatMap(value -> Constraints.applyConstraints(constraints, value))
        .mapLeft(errors -> errors.stream().map(error ->  new FormError(key, error)).collect(
            Collectors.toList()));
  }

  @Override
  public Map<String, String> unbind(T value) {
    return formatter.unbind(key, value);
  }

  @Override
  public String key() {
    return key;
  }

  @Override
  public Mapping<T> withPrefix(String prefix) {
    String newKey = prefix.isEmpty() || key.isEmpty() ? prefix + key : prefix + "." + key;
    if(newKey.equals(key)) {
      return this;
    } else {
      return new StandardMapping<>(newKey, constraints, formatter);
    }
  }

  @Override
  public Mapping<T> withConstraint(Constraint<T> constraint) {
    ArrayList<Constraint<T>> allConstraints = new ArrayList<>(constraints);
    allConstraints.add(constraint);
    return new StandardMapping<>(key, allConstraints, formatter);
  }

}
