package com.lunatech.postroom;

import static com.lunatech.postroom.FieldMappings.integer;
import static com.lunatech.postroom.FieldMappings.text;
import static com.lunatech.postroom.Mappings.field;
import static com.lunatech.postroom.Mappings.mapping;
import static com.lunatech.postroom.PublicFieldsStructor.publicFields;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class CombinerTest {

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

  @Test
  public void nameBasedRecordCombinerWorksWithOutOfOrderFields() {

    Form<User> userForm = Form.of(mapping(
        field("age", integer()), // Note that these fields are not in the same order as the
        field("name", text()))   // fields in the User record.
        .to(publicFields(User.class)));
    
    Map<String, String> data = new HashMap<>();
    data.put("name", "Erik");
    data.put("age", "36");

    User user = new User();
    user.name = "Erik";
    user.age = 36;

    assertEquals(user,
        userForm.bind(data).value());
  }

}
