package com.lunatech.postroom;

import io.vavr.control.Either;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Formatters {

  public static final Formatter<String> stringFormatter = new Formatter<String>() {
    @Override
    public Either<List<String>, String> bind(String key, Map<String, String> data) {
      String value = data.get(key);
      if(value == null) {
        return Either.left(Collections.singletonList("No value found for key " + key));
      } else {
        return Either.right(value);
      }
    }

    @Override
    public Map<String, String> unbind(String key, String value) {
      return Collections.singletonMap(key, value);
    }
  };

  public static final Formatter<Integer> integerFormatter =
      stringFormatter.transform(
          stringValue -> {
            try {
              return Either.right(Integer.parseInt(stringValue));
            } catch (NumberFormatException e) {
              return Either.left(Collections.singletonList("Invalid numeric format"));
            }
          },
          Object::toString);
}
