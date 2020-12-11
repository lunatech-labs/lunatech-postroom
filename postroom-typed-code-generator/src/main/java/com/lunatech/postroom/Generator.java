package com.lunatech.postroom;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Generator {

  public static void main(String... args) {
    System.out.println(generateTypedListWrapper(3));
  }

  public static String generateFactoryMethods(int i) {
    StringBuilder sb = new StringBuilder();
    sb.append("package com.lunatech.postroom.typed;\n\n");

    sb.append("import com.lunatech.postroom.Mapping;\n\n");
    sb.append("public class TypedMappings {\n\n");

    for (int j = 1; j <= i; j++) {
      sb.append(IntStream.range(1, j + 1).boxed().map(v -> "A" + v)
          .collect(Collectors.joining(", ", "  public static <", "> TypedMappingStage" + j)));
      sb.append(IntStream.range(1, j + 1).boxed().map(v -> "A" + v).collect(Collectors.joining(", ", "<", ">")));
      sb.append(IntStream.range(1, j + 1).boxed().map(v -> "Mapping<A" + v + "> m" + v)
          .collect(Collectors.joining(", ", " typed(", ") {\n")));
      sb.append(IntStream.range(1, j + 1).boxed().map(v -> "m" + v)
          .collect(Collectors
              .joining(", ", "    return new TypedMappingStage" + j + "<>(", ");\n  }\n\n")));

    }
    sb.append("\n\n");
    for (int j = 1; j <= i; j++) {

      sb.append(IntStream.range(1, j + 1).boxed().map(v -> "A" + v).collect(Collectors.joining(", ", "  public static " + "<", ">")));
      sb.append(IntStream.range(1, j + 1).boxed().map(v -> "A" + v).collect(Collectors.joining(", ", " TypedListWrapper" + j + "<", ">")));
      sb.append(IntStream.range(1, j + 1).boxed().map(v -> "A" + v + " v" + v).collect(Collectors.joining(", ", " parts(", ") { \n")));
      sb.append(IntStream.range(1, j + 1).boxed().map(v -> "v" + v).collect(Collectors.joining(", ", "    return new TypedListWrapper" + j + "<>(", ");\n  }\n\n")));
    }
    sb.append("}");

    return sb.toString();
  }

  public static String generateTypedListWrapper(int i) {
    StringBuilder sb = new StringBuilder();
    sb.append("package com.lunatech.postroom.typed;\n\n");

    sb.append("import java.util.List;\n");
    sb.append("import java.util.Arrays;\n\n");

    sb.append(IntStream.range(1, i + 1).boxed().map(v -> "A" + v)
        .collect(Collectors.joining(", ", "public class TypedListWrapper" + i + "<", "> {\n")));
    sb.append("  final List<?> values;\n\n");
    sb.append(IntStream.range(1, i + 1).boxed().map(v -> "A" + v + " v" + v)
        .collect(Collectors.joining(", ", "  TypedListWrapper" + i + "(", ") {\n")));
    sb.append(IntStream.range(1, i + 1).boxed().map(v -> "v" + v)
        .collect(Collectors.joining(", ", "    values = Arrays.asList(",  ");\n  }\n")));
    sb.append("}");
    return sb.toString();
  }

  public static String generateTypedMappingStage(int i) {
    StringBuilder sb = new StringBuilder();
    sb.append("package com.lunatech.postroom.typed;\n\n");

    sb.append("import com.lunatech.postroom.Mapping;\n");
    sb.append("import com.lunatech.postroom.Structor;\n");
    sb.append("import com.lunatech.postroom.Destructor;\n");
    sb.append("import io.vavr.Function1;\n");
    sb.append("import io.vavr.Function" + i + ";\n");
    sb.append("import io.vavr.control.Either;\n");
    sb.append("import java.lang.invoke.MethodHandle;");
    sb.append("import java.lang.invoke.MethodHandles;");
    sb.append("import java.lang.invoke.MethodType;");
    sb.append("import java.util.ArrayList;\n");
    sb.append("import java.util.Arrays;\n");
    sb.append("import java.util.HashMap;\n");
    sb.append("import java.util.List;\n");
    sb.append("import java.util.Map;\n\n");


    // public class CompositeMapping2<A1, A2, T> implements Mapping<T> {
    sb.append("public class TypedMappingStage" + i);
    sb.append(IntStream.range(1, i + 1).boxed().map(v -> "A" + v)
        .collect(Collectors.joining(", ", "<", "> ")));
    sb.append("extends AbstractTypedMappingStage {\n\n");

    // Fields
    sb.append("  private static final MethodHandle constructorHandle;\n\n");

    sb.append("  static {;\n");
    sb.append("    MethodType mt = MethodType.methodType(");
    sb.append(IntStream.range(1, i + 2).boxed().map(__ -> "Object.class")
        .collect(Collectors.joining(", ", "", ");\n\n")));
    sb.append("    try {\n");
    sb.append("      constructorHandle = MethodHandles.lookup().findVirtual(Function" + i
        + ".class, \"apply\", mt);\n");
    sb.append("    } catch (Exception e) {\n");
    sb.append("      throw new RuntimeException(e);\n");
    sb.append("    }\n");
    sb.append("  }\n\n");

    sb.append(IntStream.range(1, i + 1).boxed().map(v -> "Mapping<A" + v + "> m" + v)
        .collect(Collectors.joining(", ", "  TypedMappingStage" + i + "(", ") {\n\n")));
    sb.append(IntStream.range(1, i + 1).boxed().map(v -> "m" + v)
        .collect(Collectors.joining(", ", "    super(Arrays.asList(", "));\n}\n\n")));

    // 'to' with typed destructor
    sb.append("  public <T> Mapping<T> to(Function" + i);
    sb.append(IntStream.range(1, i + 1).boxed().map(v -> "A" + v)
        .collect(Collectors.joining(", ", "<", ", T> constructor, ")));
    sb.append(IntStream.range(1, i + 1).boxed().map(v -> "A" + v)
        .collect(Collectors.joining(", ", "Function1<T, TypedListWrapper" + i + "<", ">> destructor) {\n")));
    sb.append(
        "    return to(constructor, (t, __) -> destructor.apply(t).values);\n  }\n\n");

    // 'to' with untyped destructor
    sb.append("  public <T> Mapping<T> to(Function" + i);
    sb.append(IntStream.range(1, i + 1).boxed().map(v -> "A" + v)
        .collect(Collectors.joining(", ", "<", ", T> constructor, ")));
    sb.append("Destructor<T> destructor) {\n");
    sb.append("    return build(new Structor<T>() {\n");
    sb.append("      @SuppressWarnings(\"unchecked\")\n");
    sb.append("      @Override\n");
    sb.append("      public T construct(List<String> names, List<?> values) {\n");
    sb.append("        try {\n");
    sb.append("          ArrayList<Object> args = new ArrayList<>(values.size() + 1);\n");
    sb.append("          args.add(constructor);\n");
    sb.append("          args.addAll(values);\n");
    sb.append("          return (T) constructorHandle.invokeWithArguments(args);\n");
    sb.append("        } catch(Throwable e) {\n");
    sb.append("          throw new RuntimeException(e); // TODO\n");
    sb.append("        }\n");
    sb.append("      }\n");
    sb.append("\n");
    sb.append("      @Override\n");
    sb.append("      public List<?> destruct(T value, List<String> names) {\n");
    sb.append("        return destructor.destruct(value, names);\n");
    sb.append("      }\n");
    sb.append("    });\n");
    sb.append("  }\n");
    sb.append("}");

    return sb.toString();
  }

}
