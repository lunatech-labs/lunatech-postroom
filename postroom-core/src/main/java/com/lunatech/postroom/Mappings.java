package com.lunatech.postroom;

import com.lunatech.postroom.CompositeMapping.CompositeMappingStage;
import java.util.Arrays;

public final class Mappings {

  private Mappings(){}

  public static <T> CompositeMapping.CompositeMappingStage mapping(Mapping<?>... mappings) {
    return new CompositeMappingStage(Arrays.asList(mappings));
  }

  public static <T> Mapping<T> field(String field, Mapping<T> mapping) {
    return mapping.withPrefix(field);
  }

}
