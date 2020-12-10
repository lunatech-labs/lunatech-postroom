package com.lunatech.postroom.records;

import com.lunatech.postroom.Structor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.List;

public class IndexedRecordStructor<T extends Record> implements Structor<T> {

  Class<T> type;

  IndexedRecordStructor(Class<T> type) {
    this.type = type;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T construct(List<String> names, List<?> values) {
    try {
      return (T) type.getDeclaredConstructors()[0].newInstance(values.toArray());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<?> destruct(T value, List<String> names) {
    ArrayList<Object> data = new ArrayList<>(names.size());
    for (RecordComponent component : value.getClass().getRecordComponents()) {
      try {
        data.add(component.getAccessor().invoke(value));
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }
    return data;
  }
}
