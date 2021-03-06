package com.github.aanno.beanflattener;

import com.github.aanno.beanflattener.annotation.FlatBeanClassFactory;
import com.github.aanno.beanflattener.model.InputBean;
import com.github.aanno.beanflattener.model.InputProperty;
import com.github.aanno.beanflattener.model.OutputBean;
import com.github.aanno.beanflattener.model.Property;
import com.google.auto.common.AnnotationMirrors;
import com.google.auto.common.AnnotationValues;
import com.google.auto.common.BasicAnnotationProcessor;
import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSetMultimap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SupportedAnnotationTypes({"com.github.aanno.beanflattener.annotation.FlatBeanClassFactory"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class FlatBeanProcessor extends BasicAnnotationProcessor {

  @Override
  protected Iterable<? extends Step> steps() {
    return Collections.singleton(new FlatBeanClassStep());
  }

  private class FlatBeanClassStep implements Step {

    @Override
    public Set<String> annotations() {
      return Collections.singleton(
          "com.github.aanno.beanflattener.annotation.FlatBeanClassFactory");
    }

    @Override
    public Set<? extends Element> process(
        ImmutableSetMultimap<String, Element> elementsByAnnotation) {
      for (Element annotatedElement : elementsByAnnotation.values()) {
        // annotation is only allowed on methods
        ExecutableElement annotatedFactoryMethod = MoreElements.asExecutable(annotatedElement);
        FlatBeanClassFactory fbcfAnnotation =
            annotatedFactoryMethod.getAnnotation(FlatBeanClassFactory.class);

        List<? extends AnnotationMirror> mirrors = annotatedFactoryMethod.getAnnotationMirrors();
        List<Map<String, ImmutableList<AnnotationValue>>> fbcfMirrors =
            resolveAmListParameters(mirrors, null);
        if (fbcfMirrors.size() != 1) {
          throw new IllegalArgumentException(fbcfMirrors.toString());
        }
        // for-loop runs _exactly_ one time
        OutputBean outputBean = new OutputBean();
        for (Map<String, ImmutableList<AnnotationValue>> fbcfMirror : fbcfMirrors) {
          ImmutableList<AnnotationValue> uses = fbcfMirror.get("uses()");
          if (uses == null || uses.isEmpty()) {
            throw new IllegalArgumentException();
          }
          // not helpful as FlatBeanMapper contains Classes
          // FlatBeanMapper[] mappers = fbcfAnnotation.mappers();
          // Class<?>[] uses = fbcfAnnotation.uses();

          outputBean.setFactoryAnnotation(fbcfAnnotation);
          outputBean.setFactoryMethodName(MoreElements.asExecutable(annotatedFactoryMethod));
          Element factoryClass = annotatedFactoryMethod.getEnclosingElement();
          outputBean.setFactoryClass(MoreElements.asType(factoryClass));
          outputBean.setUses(
              uses.stream()
                  .map(av -> typeElementFromAnnotationValue(av))
                  .collect(Collectors.toSet()));
          // outputBean.addAllUses(uses);

          // List<InputBean> inputBeans = new ArrayList<>();
          for (AnnotationValue mapper : fbcfMirror.get("mappers()")) {
            AnnotationMirror mapperElement = AnnotationValues.getAnnotationMirror(mapper);
            // ImmutableMap<ExecutableElement, AnnotationValue> parameters =
            //     AnnotationMirrors.getAnnotationValuesWithDefaults(mapperElement);
            List<AnnotationMirror> mapperElements = Collections.singletonList(mapperElement);
            List<Map<String, ImmutableList<AnnotationValue>>> maps =
                resolveAmListParameters(mapperElements, e -> e.toString().equals("mappers()"));

            if (mapperElements.size() != 1) {
              throw new IllegalArgumentException(mapperElements.toString());
            }
            List<Map<String, TypeElement>> types =
                resolveAmTypeParameters(mapperElements, e -> e.toString().equals("value()"));
            TypeElement type = types.get(0).get("value()");
            InputBean bean = new InputBean();
            bean.setValue(type);
            outputBean.addInputBean(bean);

            for (Map<String, ImmutableList<AnnotationValue>> map : maps) {
              List<AnnotationMirror> mapMirrors =
                  map.get("mappers()").stream()
                      .map(AnnotationValues::getAnnotationMirror)
                      .collect(Collectors.toList());
              List<Map<String, TypeElement>> converters =
                  resolveAmTypeParameters(mapMirrors, e -> e.toString().equals("converter()"));
              List<Map<String, Boolean>> ignores =
                  resolveAmValue(mapMirrors, e -> e.toString().equals("ignore()"));
              List<Map<String, String>> fromTos =
                  resolveAmValue(
                      mapMirrors,
                      e -> e.toString().equals("from()") || e.toString().equals("to()"));

              for (int i = 0; i < converters.size(); ++i) {
                Map<String, String> fromTo = fromTos.get(i);
                TypeElement converter = converters.get(i).get("converter()");
                Boolean ignore = ignores.get(i).get("ignore()");

                InputProperty property = new InputProperty();
                property.setTypeElement(type);
                property.setName(fromTo.get("to()"));
                property.setFrom(fromTo.get("from()"));
                property.setIgnore(ignore.booleanValue());
                bean.addProperty(property);
              }
            }
          }
        }
        System.out.println(fbcfAnnotation);
      }
      // no deferred elements
      return Collections.emptySet();
    }
  }

  private TypeElement typeElementFromAnnotationValue(AnnotationValue value) {
    return MoreElements.asType(AnnotationValues.getTypeMirror(value).asElement());
  }

  private List<Map<String, ImmutableList<AnnotationValue>>> resolveAmListParameters(
      List<? extends AnnotationMirror> mirrors, Predicate<? super ExecutableElement> predicate) {
    return mirrors.stream()
        .map(
            m ->
                AnnotationMirrors.getAnnotationValuesWithDefaults(m).entrySet().stream()
                    // Map.Entry<ExecutableElement, AnnotationValue>
                    .filter(e -> predicate != null ? predicate.test(e.getKey()) : true)
                    .map(
                        e ->
                            new ImmutablePair<String, ImmutableList<AnnotationValue>>(
                                e.getKey().toString(),
                                AnnotationValues.getAnnotationValues(e.getValue())))
                    .collect(Collectors.toMap(Pair::getKey, Pair::getValue)))
        .collect(Collectors.toList());
  }

  private List<Map<String, TypeElement>> resolveAmTypeParameters(
      List<? extends AnnotationMirror> mirrors, Predicate<? super ExecutableElement> predicate) {
    return mirrors.stream()
        .map(
            m ->
                AnnotationMirrors.getAnnotationValuesWithDefaults(m).entrySet().stream()
                    // Map.Entry<ExecutableElement, AnnotationValue>
                    .filter(e -> predicate != null ? predicate.test(e.getKey()) : true)
                    .map(
                        e ->
                            new ImmutablePair<String, TypeElement>(
                                e.getKey().toString(),
                                typeElementFromAnnotationValue(e.getValue())))
                    .collect(Collectors.toMap(Pair::getKey, Pair::getValue)))
        .collect(Collectors.toList());
  }

  private <T> List<Map<String, T>> resolveAmValue(
      List<? extends AnnotationMirror> mirrors, Predicate<? super ExecutableElement> predicate) {
    return mirrors.stream()
        .map(
            m ->
                AnnotationMirrors.getAnnotationValuesWithDefaults(m).entrySet().stream()
                    // Map.Entry<ExecutableElement, AnnotationValue>
                    .filter(e -> predicate != null ? predicate.test(e.getKey()) : true)
                    .map(e -> new ImmutablePair<String, T>(e.getKey().toString(), (T) e.getValue().getValue()))
                    .collect(Collectors.toMap(Pair::getKey, Pair::getValue)))
        .collect(Collectors.toList());
  }

  public boolean process1(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (TypeElement annotation : annotations) {

      Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);

      Map<Boolean, List<Element>> annotatedMethods =
          annotatedElements.stream()
              .collect(
                  Collectors.partitioningBy(
                      element ->
                          ((ExecutableType) element.asType()).getParameterTypes().size() == 1
                              && element.getSimpleName().toString().startsWith("set")));

      List<Element> setters = annotatedMethods.get(true);
      List<Element> otherMethods = annotatedMethods.get(false);

      otherMethods.forEach(
          element ->
              processingEnv
                  .getMessager()
                  .printMessage(
                      Diagnostic.Kind.ERROR,
                      "@BuilderProperty must be applied to a setXxx method with a single argument",
                      element));

      if (setters.isEmpty()) {
        continue;
      }

      String className =
          ((TypeElement) setters.get(0).getEnclosingElement()).getQualifiedName().toString();

      Map<String, String> setterMap =
          setters.stream()
              .collect(
                  Collectors.toMap(
                      setter -> setter.getSimpleName().toString(),
                      setter ->
                          ((ExecutableType) setter.asType())
                              .getParameterTypes()
                              .get(0)
                              .toString()));

      try {
        writeBuilderFile1(className, setterMap);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return true;
  }

  private void writeBuilderFile1(String className, Map<String, String> setterMap)
      throws IOException {

    String packageName = null;
    int lastDot = className.lastIndexOf('.');
    if (lastDot > 0) {
      packageName = className.substring(0, lastDot);
    }

    String simpleClassName = className.substring(lastDot + 1);
    String builderClassName = className + "Builder";
    String builderSimpleClassName = builderClassName.substring(lastDot + 1);

    JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(builderClassName);

    try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {

      if (packageName != null) {
        out.print("package ");
        out.print(packageName);
        out.println(";");
        out.println();
      }

      out.print("public class ");
      out.print(builderSimpleClassName);
      out.println(" {");
      out.println();

      out.print("    private ");
      out.print(simpleClassName);
      out.print(" object = new ");
      out.print(simpleClassName);
      out.println("();");
      out.println();

      out.print("    public ");
      out.print(simpleClassName);
      out.println(" build() {");
      out.println("        return object;");
      out.println("    }");
      out.println();

      setterMap
          .entrySet()
          .forEach(
              setter -> {
                String methodName = setter.getKey();
                String argumentType = setter.getValue();

                out.print("    public ");
                out.print(builderSimpleClassName);
                out.print(" ");
                out.print(methodName);

                out.print("(");

                out.print(argumentType);
                out.println(" value) {");
                out.print("        object.");
                out.print(methodName);
                out.println("(value);");
                out.println("        return this;");
                out.println("    }");
                out.println();
              });

      out.println("}");
    }
  }
}
