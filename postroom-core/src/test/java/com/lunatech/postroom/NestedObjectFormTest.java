package com.lunatech.postroom;

import static com.lunatech.postroom.Mappings.field;
import static com.lunatech.postroom.Mappings.mapping;
import static com.lunatech.postroom.PublicFieldsStructor.publicFields;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class NestedObjectFormTest {

  static class Organisation {
    public String name;
    public String city;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Organisation that = (Organisation) o;

      if (!name.equals(that.name)) {
        return false;
      }
      return city.equals(that.city);
    }

    @Override
    public int hashCode() {
      int result = name.hashCode();
      result = 31 * result + city.hashCode();
      return result;
    }
  }

  static class User {
    public String name;
    public Organisation organisation;

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
      return organisation.equals(user.organisation);
    }

    @Override
    public int hashCode() {
      int result = name.hashCode();
      result = 31 * result + organisation.hashCode();
      return result;
    }
  }

  @Test
  public void testNestedObjectBinding() {

    Form<User> userForm = Form.of(
        mapping(
            field("name", FieldMappings.text()),
            field("organisation", mapping(
                field("name", FieldMappings.text()),
                field("city", FieldMappings.text()))
                .to(publicFields(Organisation.class)))
    ).to(publicFields(User.class)));

    Map<String, String> data = new HashMap<>();
    data.put("name", "Erik");
    data.put("organisation.name", "Lunatech");
    data.put("organisation.city", "Rotterdam");

    Form<User> boundForm = userForm.bind(data);

    User boundUser = boundForm.value();

    User expectedUser = new User();
    expectedUser.name = "Erik";
    expectedUser.organisation = new Organisation();
    expectedUser.organisation.name = "Lunatech";
    expectedUser.organisation.city = "Rotterdam";

    assertEquals(expectedUser, boundUser);
  }

  @Test
  public void testNestedObjectFilling() {
    Form<User> userForm = Form.of(
        mapping(
            field("name", FieldMappings.text()),
            field("organisation", mapping(
                field("name", FieldMappings.text()),
                field("city", FieldMappings.text()))
                .to(publicFields(Organisation.class)))
        ).to(publicFields(User.class)));

    User user = new User();
    user.name = "Erik";
    user.organisation = new Organisation();
    user.organisation.name = "Lunatech";
    user.organisation.city = "Rotterdam";

    Map<String, String> expectedData = new HashMap<>();
    expectedData.put("name", "Erik");
    expectedData.put("organisation.name", "Lunatech");
    expectedData.put("organisation.city", "Rotterdam");

    Form<User> filledForm = userForm.fill(user);

    assertEquals(expectedData, filledForm.data());
  }

  @Test
  public void testNestedObjectConstraint() {
    Form<User> userForm = Form.of(
        mapping(
            field("name", FieldMappings.text()),
            field("organisation", mapping(
                field("name", FieldMappings.nonEmptyText()),
                field("city", FieldMappings.text()))
                .to(publicFields(Organisation.class)))
        ).to(publicFields(User.class)));

    Map<String, String> data = new HashMap<>();
    data.put("name", "Erik");
    data.put("organisation.name", "");
    data.put("organisation.city", "Rotterdam");

    Form<User> boundForm = userForm.bind(data);

    assertEquals(Collections.singletonList(
        new FormError("organisation.name", "Value should not be empty")), boundForm.getErrors());
  }
}
