package com.lunatech.postroom;

import java.util.ArrayList;
import java.util.List;

public class PublicFieldsStructor<T> implements Structor<T> {

  private final Class<T> type;

  private PublicFieldsStructor(Class<T> type) {
    this.type = type;
  }

  public static <T> Structor<T> publicFields(Class<T> type) {
    return new PublicFieldsStructor<>(type);
  }

  @Override
  public T construct(List<String> keys, List<?> values) {
    try {
      T object = type.getDeclaredConstructor().newInstance();

      for (int i = 0; i < keys.size(); i++) {
        try {
          object.getClass().getField(keys.get(i)).set(object, values.get(i));
        } catch (Exception e) {
          throw new RuntimeException(
              "Failed to bind field " + keys.get(i) + " to object of type " + type,
              e);
        }
      }
      return object;
    } catch (Exception e) {
      throw new RuntimeException(
          "Failed to construct object of type " + type + ", does it have a no-args constructor?",
          e);
    }
  }

  @Override
  public List<?> destruct(T value, List<String> keys) {
    Class<?> type = value.getClass();
    try {
      ArrayList<Object> values = new ArrayList<>(keys.size());
      for (String key : keys) {
        values.add(type.getField(key).get(value));
      }
      return values;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
