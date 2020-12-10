package com.lunatech.postroom;

import java.util.List;

@FunctionalInterface
public interface Destructor<T> {
  List<?> destruct(T value, List<String> names);
}
