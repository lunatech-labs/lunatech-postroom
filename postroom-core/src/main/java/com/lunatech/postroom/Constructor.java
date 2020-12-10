package com.lunatech.postroom;

import java.util.LinkedHashMap;
import java.util.List;

@FunctionalInterface
public interface Constructor<T> {
  T construct(List<String> names, List<?> values);
}
