package com.lunatech.postroom;

import io.vavr.control.Either;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface Formatter<T> {

  Either<List<String>, T> bind(String key, Map<String, String> data);
  Map<String, String> unbind(String key, T value);

  default <U> Formatter<U> transform(
      Function<T, Either<List<String>, U>> mapBind,
      Function<U, T> mapUnbind) {

    return new Formatter<U>() {
      @Override
      public Either<List<String>, U> bind(String key, Map<String, String> data) {
        return Formatter.this.bind(key, data).flatMap(mapBind);
      }

      @Override
      public Map<String, String> unbind(String key, U value) {
        return Formatter.this.unbind(key, mapUnbind.apply(value));
      }
    };
  }
}
