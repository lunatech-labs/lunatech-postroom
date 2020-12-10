package com.lunatech.postroom;

import com.lunatech.postroom.NamedCompositeMapping.CompositeMappingStage;
import java.util.Arrays;

public final class Mappings {

  private Mappings(){}

  public static <T> NamedCompositeMapping.CompositeMappingStage mapping(Mapping<?>... mappings) {
    return new CompositeMappingStage(Arrays.asList(mappings));
  }

  public static <T> Mapping<T> field(String field, Mapping<T> mapping) {
    return mapping.withPrefix(field);
  }

}
