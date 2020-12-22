package com.lunatech.postroom;

import org.junit.jupiter.api.Test;

import java.util.*;

import static com.lunatech.postroom.FieldMappings.*;
import static com.lunatech.postroom.Mappings.field;
import static com.lunatech.postroom.Mappings.mapping;
import static com.lunatech.postroom.PublicFieldsStructor.publicFields;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class FormTest {

  static class User {
    public String name;
    public Integer age;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      User user = (User) o;

      if (!name.equals(user.name)) {
        return false;
      }
      return age.equals(user.age);
    }

    @Override
    public int hashCode() {
      int result = name.hashCode();
      result = 31 * result + age.hashCode();
      return result;
    }
  }

  private final Form<User> userForm = Form.of(mapping(
          field("name", FieldMappings.nonEmptyText()),
          field("age", FieldMappings.integer())).to(
          publicFields(User.class)));

  @Test
  public void testCompositeMappingWithRecordCombiner() {
    Map<String, String> data = new HashMap<>();
    data.put("name", "Erik");
    data.put("age", "36");

    Form<User> boundForm = userForm.bind(data);

    User user = new User();
    user.name = "Erik";
    user.age = 36;

    assertEquals(user, boundForm.value());
  }

  @Test
  public void testFill() {
    User user = new User();
    user.name = "Erik";
    user.age = 36;

    Form<User> boundForm = userForm.fill(user);

    Map<String, String> data = new HashMap<>();
    data.put("name", "Erik");
    data.put("age", "36");

    assertEquals(data, boundForm.data());
  }

  @Test
  public void testFillAndValidate() {

    User user = new User();
    user.name = "";
    user.age = 36;

    Form<User> boundForm = userForm.fillAndValidate(user);

    assertEquals(Collections.singletonList(
        new FormError("name", "Value should not be empty")), boundForm.getErrors());
  }

  @Test
  public void testCompositeMappingWithMappingErrors() {
    Map<String, String> data = new HashMap<>();
    data.put("name", "Erik");
    data.put("age", "Banana");

    Form<User> boundForm = userForm.bind(data);

    assertEquals(Collections.singletonList(
        new FormError("age", "Invalid numeric format")), boundForm.getErrors());
  }

  @Test
  public void testIntegerMappings() {
    Form<Integer> form = Form.of(FieldMappings.integer(1, 10).withPrefix("number"));

    Map<String, String> data = new HashMap<>();
    data.put("number", "17");

    Form<Integer> boundForm = form.bind(data);

    assertEquals(Collections.singletonList(
        new FormError("number", "Value should be between 1 and 10, inclusive")), boundForm.getErrors());
  }

  @Test
  public void testNonEmptyTextMappings() {
    Form<String> form = Form.of(FieldMappings.nonEmptyText().withPrefix("text"));

    Map<String, String> data = new HashMap<>();
    data.put("text", "");

    Form<String> boundForm = form.bind(data);

    assertEquals(Collections.singletonList(
        new FormError("text", "Value should not be empty")), boundForm.getErrors());
  }

  @Test
  public void testTransform() {
    Form<Integer> form = Form.of(text()
                    .withPrefix("foo")
                    .transform(Integer::parseInt, Object::toString));

    assertEquals(101,
            form.bind(Collections.singletonMap("foo", "101")).value());

    assertEquals(Collections.singletonMap("foo", "42"),
            form.fill(42).data());
  }

  @Test
  public void testVerifying() {
    Form<String> form = Form.of(text()
            .withPrefix("foo")
            .verifying(v -> v.length() >= 4, "Length should be at least 4"));

    assertEquals(
            "banana",
            form.bind(Collections.singletonMap("foo", "banana")).value());

    assertEquals(
            Collections.singletonList(new FormError("foo", "Length should be at least 4")),
            form.bind(Collections.singletonMap("foo", "wo")).getErrors("foo"));

  }

  static class T {
    public Optional<String> foo;
    public Optional<String> bar;

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;

      T t = (T) o;

      if (!foo.equals(t.foo))
        return false;
      return bar.equals(t.bar);
    }

    @Override
    public int hashCode() {
      int result = foo.hashCode();
      result = 31 * result + bar.hashCode();
      return result;
    }

  }

  @Test
  public void testOptional() {
    Form<T> form = Form.of(mapping(
            field("foo", optional(nonEmptyText())),
            field("bar", optional(nonEmptyText())))
    .to(publicFields(T.class)));

    Map<String, String> data = new HashMap<>();
    data.put("foo", "hey");

    T expected = new T();
    expected.foo = Optional.of("hey");
    expected.bar = Optional.empty();

    assertEquals(
            expected,
            form.bind(data).value());

  }

}
