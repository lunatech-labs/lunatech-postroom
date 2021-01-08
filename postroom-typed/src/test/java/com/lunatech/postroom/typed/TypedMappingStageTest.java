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
  public void testTypedHighArity() {
    Map<String, String> data = new HashMap<>();
    data.put("field1", "1");
    data.put("field2", "2");
    data.put("field3", "3");
    data.put("field4", "4");
    data.put("field5", "5");
    data.put("field6", "6");
    data.put("field7", "7");
    data.put("field8", "8");
    data.put("field9", "9");
    data.put("field10", "10");
    data.put("field11", "11");
    data.put("field12", "12");

    final Form<Integer> typedUserForm = Form.<Integer>of(typed(
            field("field1", integer()),
            field("field2", integer()),
            field("field3", integer()),
            field("field4", integer()),
            field("field5", integer()),
            field("field6", integer()),
            field("field7", integer()),
            field("field8", integer()),
            field("field9", integer()),
            field("field10", integer()),
            field("field11", integer()),
            field("field12", integer()))
            .to((i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12) ->
                    i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8 + i9 + i10 + i11 + i12,
                (Integer sum) -> parts(sum, sum, sum, sum, sum, sum, sum, sum, sum, sum, sum, sum)));
    Integer boundSum = typedUserForm.bind(data).value();

    assertEquals(78, boundSum);
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
