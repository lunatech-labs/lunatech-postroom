package com.lunatech.postroom;

import io.vavr.control.Either;

import java.util.*;
import java.util.stream.Collectors;

class CompositeMapping<T> implements Mapping<T> {

  private final String key;
  private final List<Mapping<?>> mappings;
  private final Structor<T> structor;
  private final List<Constraint<T>> constraints;

  private CompositeMapping(String key, List<Mapping<?>> mappings, Structor<T> structor, List<Constraint<T>> constraints) {
    this.key = key;
    this.mappings = mappings;
    this.structor = structor;
    this.constraints = constraints;
  }

  static <T> CompositeMapping<T> of(List<Mapping<?>> mappings, Structor<T> structor) {
    return new CompositeMapping<>("", mappings, structor, Collections.emptyList());
  }

  @Override
  public Either<List<FormError>, T> bind(Map<String, String> data) {
    List<FormError> errors = new ArrayList<>();
    List<String> keys = new ArrayList<>();
    List<Object> values = new ArrayList<>();
    for(Mapping<?> mapping: mappings) {
      keys.add(mapping.key());
      mapping.withPrefix(key).bind(data).fold(errors::addAll, values::add);
    }

    if(!errors.isEmpty()) {
      return Either.left(errors);
    } else {
      return Constraints.applyConstraints(constraints, structor.construct(keys, values))
          .mapLeft(combineErrors -> combineErrors.stream()
              .map(error -> new FormError(key(), error))
              .collect(Collectors.toList()));
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, String> unbind(T value) {
    List<String> keys = mappings.stream().map(Mapping::key).collect(Collectors.toList());
    List<?> values = structor.destruct(value, keys);

    Map<String, String> unbound = new HashMap<>();

    int i = 0;
    for(Object objectValue: values) {
      Mapping<Object> mapping = (Mapping<Object>) mappings.get(i);
      Map<String, String> valueUnbound = mapping.unbind(objectValue);
      if(key.equals("")) {
        unbound.putAll(valueUnbound);
      } else {
        valueUnbound.forEach((k, v) -> unbound.put(key + "." + k, v));
      }

      i++;
    }

    return unbound;
  }

  @Override
  public String key() {
    return key;
  }

  @Override
  public Mapping<T> withPrefix(String prefix) {
    String newKey = key.isEmpty() || prefix.isEmpty() ? prefix + key : prefix + "." + key;
    return new CompositeMapping<>(newKey, mappings, structor, constraints);
  }

  @Override
  public Mapping<T> verifying(Collection<Constraint<T>> constraints) {
    ArrayList<Constraint<T>> allConstraints = new ArrayList<>(this.constraints);
    allConstraints.addAll(constraints);
    return new CompositeMapping<>(key, mappings, structor, allConstraints);
  }

  public static class CompositeMappingStage {

    private final List<Mapping<?>> mappings;

    CompositeMappingStage(List<Mapping<?>> mappings) {
      this.mappings = mappings;
    }

    public <T> Mapping<T> to(Structor<T> structor) {
      return CompositeMapping.of(mappings, structor);
    }
  }
}
