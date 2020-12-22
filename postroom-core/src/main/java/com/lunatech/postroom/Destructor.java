package com.lunatech.postroom;

import java.util.List;

@FunctionalInterface
public interface Destructor<T> {
  List<?> destruct(T value, List<String> names);

  // TODO, represent this nicer?
  static <T> Destructor<T> none() {
    return (value, names) -> {
      throw new RuntimeException("This form does not support destructing!");
    };
  }
}
