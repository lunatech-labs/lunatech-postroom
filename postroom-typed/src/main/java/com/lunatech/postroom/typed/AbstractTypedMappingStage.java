package com.lunatech.postroom.typed;

import com.lunatech.postroom.Mapping;
import com.lunatech.postroom.Mappings;
import com.lunatech.postroom.Structor;
import java.util.List;

public class AbstractTypedMappingStage {

  private final List<Mapping<?>> mappings;

  public AbstractTypedMappingStage(List<Mapping<?>> mappings) {
    this.mappings = mappings;
  }

  protected <T> Mapping<T> build(Structor<T> structor) {
    return Mappings.mapping(mappings.toArray(new Mapping[]{})).to(structor);
  }
}
