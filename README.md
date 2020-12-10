# Postroom

Validate and bind HTML form data to Java objects. 

Define a form, and class it binds to:
```
Form<User> userForm = Form.of(mapping(
  field("name", nonEmptyText()),
  field("age", integer())).to(
  publicFields(User.class))
```

Validate incoming data:
```
userForm.bind(data).fold(
  formWithErrors -> render(formWithErrors),
  user -> userRepository.insert(user));
```

# Features

* Handy out of the box validators
* Easy to add constraints to mappings
* Composing mappings
* Transforming mappings to produce different types
* Unbinding of data to display data in a form
* Optional type-safe binding and unbinding

# User Guide

# Credits

This library is *extremely* similar to the Forms library for Scala in Play 2. Really, it's almost scary. That library in Play is a wonderful thing. 

# License

Apache License 2.0

