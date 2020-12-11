package com.lunatech.postroom;

import io.vavr.control.Either;

import java.util.*;
import java.util.stream.Collectors;

class FieldMapping<T> implements Mapping<T> {

  private final String key;
  private final List<Constraint<T>> constraints;
  private final Formatter<T> formatter;

  FieldMapping(Formatter<T> formatter) {
    this("", Collections.emptyList(), formatter);
  }

  FieldMapping(String key, List<Constraint<T>> constraints, Formatter<T> formatter) {
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
      return new FieldMapping<>(newKey, constraints, formatter);
    }
  }

  @Override
  public Mapping<T> verifying(Collection<Constraint<T>> constraints) {
    ArrayList<Constraint<T>> allConstraints = new ArrayList<>(this.constraints);
    allConstraints.addAll(constraints);
    return new FieldMapping<>(key, allConstraints, formatter);
  }

}
