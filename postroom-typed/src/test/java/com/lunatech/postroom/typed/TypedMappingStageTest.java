package com.lunatech.postroom.typed;

import static com.lunatech.postroom.FieldMappings.*;
import static com.lunatech.postroom.Mappings.field;
import static com.lunatech.postroom.PublicFieldsStructor.publicFields;
import static com.lunatech.postroom.typed.TypedMappings.parts;
import static com.lunatech.postroom.typed.TypedMappings.typed;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import com.lunatech.postroom.Form;
import org.junit.jupiter.api.Test;

public class TypedMappingStageTest {

  static class User{
    public String name;
    public Integer age;
    public String company;

    public User(String name, Integer age, String company) {
      this.name = name;
      this.age = age;
      this.company = company;
    }

    @Override
    public String toString() {
      return "User(" + name + ", " + age + ", " + company + ")";
    }

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
      if (!age.equals(user.age)) {
        return false;
      }
      return company.equals(user.company);
    }

    @Override
    public int hashCode() {
      int result = name.hashCode();
      result = 31 * result + age.hashCode();
      result = 31 * result + company.hashCode();
      return result;
    }
  }

  private final Form<User> typedUserForm = Form.of(typed(
          field("name", nonEmptyText()),
          field("age", integer()),
          field("company", text()))
          .to(User::new, (User user) ->
                  parts(user.name, user.age, user.company)));

  private final Form<User> semiTypedUserForm = Form.of(typed(
          field("name", nonEmptyText()),
          field("age", integer()),
          field("company", text()))
          .to(User::new, publicFields(User.class)));

  @Test
  public void testTyped() {
    Map<String, String> data = new HashMap<>();
    data.put("name", "Erik");
    data.put("age", "36");
    data.put("company", "Lunatech");

    User boundUser = typedUserForm.bind(data).value();
    assertEquals(new User("Erik", 36, "Lunatech"), boundUser);
  }

  @Test
  public void testSemiTyped() {
    Map<String, String> data = new HashMap<>();
    data.put("name", "Erik");
    data.put("age", "36");
    data.put("company", "Lunatech");

    User boundUser = semiTypedUserForm.bind(data).value();
    assertEquals(new User("Erik", 36, "Lunatech"), boundUser);
  }
}
