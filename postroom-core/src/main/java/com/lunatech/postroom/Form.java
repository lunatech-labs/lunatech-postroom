package com.lunatech.postroom;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Form<T> {

  private final Map<String, String> data;
  private final Mapping<T> mapping;
  private final List<FormError> errors;
  private final T value;

  private Form(Mapping<T> mapping, List<FormError> errors, T value, Map<String, String> data) {
    this.mapping = mapping;
    this.errors = errors;
    this.value = value;
    this.data = data;
  }

  public static <T> Form<T> of(Mapping<T> mapping) {
    return new Form<>(mapping, Collections.emptyList(), null, Collections.emptyMap());
  }

  public Form<T> bind(Map<String, String> data) {
    return mapping.bind(data).fold(
        errors -> new Form<>(mapping, errors, null, data),
        value -> new Form<>(mapping, Collections.emptyList(), value, data));
  }

  public Form<T> fill(T value) {
    return new Form<>(mapping, Collections.emptyList(), value, mapping.unbind(value));
  }

  public Form<T> fillAndValidate(T value) {
    return bind(mapping.unbind(value));
  }

  public boolean hasErrors() {
    return !errors.isEmpty();
  }

  public boolean hasGlobalErrors() {
    return getError("").isPresent();
  }

  public List<FormError> getErrors() {
    return errors;
  }

  public boolean hasError(String key) { return !getErrors(key).isEmpty(); }

  public Optional<FormError> getError(String key) {
    return getErrors(key).stream().findFirst();
  }

  public Optional<FormError> getGlobalError() {
    return getError("");
  }

  public List<FormError> getErrors(String key) {
    return errors.stream().filter(e -> e.key().equals(key)).collect(Collectors.toList());
  }

  public List<FormError> getGlobalErrors() {
    return getErrors("");
  }

  public Form<T> withError(FormError formError) {
    return withErrors(Collections.singletonList(formError));
  }

  public Form<T> withErrors(FormError... formErrors) {
    return withErrors(Arrays.asList(formErrors));
  }

  public Form<T> withErrors(Collection<FormError> formErrors) {
    ArrayList<FormError> allErrors = new ArrayList<>(errors);
    allErrors.addAll(formErrors);
    return new Form<>(mapping, allErrors, value, data);
  }

  public Form<T> withGlobalError(String globalError) {
    return withError(new FormError("", globalError));
  }

  public Form<T> withGlobalErrors(String... globalErrors) {
    return withErrors(Arrays.stream(globalErrors)
        .map(errorString -> new FormError("", errorString))
        .collect(Collectors.toList()));
  }

  public Form<T> withGlobalErrors(Collection<String> globalErrors) {
    return withErrors(globalErrors.stream()
        .map(errorString -> new FormError("", errorString))
        .collect(Collectors.toList()));
  }

  public T value() {
    if(value == null) {
      throw new RuntimeException("No value! Did binding succeed?");
    } else {
      return value;
    }
  }

  public <U> U fold(
      Function<Form<T>, ? extends U> onErrors,
      Function<T, ? extends U> onSuccess) {
    if(hasErrors()) {
      return onErrors.apply(this);
    } else {
      return onSuccess.apply(value());
    }
  }

  public Map<String, String> data() {
    return data;
  }

  public Form<T> mergeData(Map<String, String> data) {
    Map<String, String> allData = new HashMap<>();
    allData.putAll(this.data);
    allData.putAll(data);
    return new Form<>(mapping, errors, value, allData);
  }

}
