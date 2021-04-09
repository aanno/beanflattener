package com.github.aanno.beanflattener;

import com.github.aanno.beanflattener.annotation.FlatBeanClassFactory;
import com.github.aanno.beanflattener.annotation.FlatBeanMap;
import com.github.aanno.beanflattener.annotation.FlatBeanMapper;
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
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
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SupportedAnnotationTypes({
  "com.github.aanno.beanflattener.annotation.FlatBeanClassFactory"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class FlatBeanProcessor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    // annotation: (ClassSymbol) FlatBeanClassFactory
    for (TypeElement annotation : annotations) {
      List<? extends Element> enclosed = annotation.getEnclosedElements();
      // el0: (MethodSymbol) uses()
      Element el0 = enclosed.get(0);
      // el1: (MethodSymbol) mappers()
      Element el1 = enclosed.get(1);

      Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
      for (Element annotated : annotatedElements) {
        FlatBeanClassFactory anno = annotated.getAnnotation(FlatBeanClassFactory.class);
        List<? extends AnnotationMirror> mirrors = annotated.getAnnotationMirrors();
        List<?> list = mirrors.stream()
                .map(m -> m.getElementValues().entrySet().stream())
                // Map.Entry<ExecutableElement, AnnotationValue>
                .flatMap(e -> e)
                .collect(Collectors.toList());
        FlatBeanMapper[] mapper = anno.mappers();
        // Class<?>[] uses = anno.uses();
        System.out.println(anno);
      }
      System.out.println(annotatedElements);
    }
    return true;
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
