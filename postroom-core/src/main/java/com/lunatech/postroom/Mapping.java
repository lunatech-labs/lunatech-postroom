package com.lunatech.postroom;

import io.vavr.control.Either;
import java.util.List;
import java.util.Map;

public interface Mapping<T> {

  Either<List<FormError>, T> bind(Map<String, String> data);
  Map<String, String> unbind(T value);
  String key();

  // TODO, can we make this internal somehow?
  Mapping<T> withPrefix(String prefix);
  Mapping<T> withConstraint(Constraint<T> constraint);
}
