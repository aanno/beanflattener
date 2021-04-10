package com.github.aanno.beanflattener.model;

import com.github.aanno.beanflattener.annotation.FlatBeanClassFactory;
import com.google.common.base.MoreObjects;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class OutputBean {

  private Set<TypeElement> uses = new TreeSet<>();
  private Set<OutputProperty> properties = new TreeSet<>();
  private FlatBeanClassFactory factoryAnnotation;
  private ExecutableElement factoryMethodName;
  private TypeElement factoryClass;

  public OutputBean() {}

  public Set<TypeElement> getUses() {
    return uses;
  }

  public void addUses(TypeElement use) {
    uses.add(use);
  }

  public void addAllUses(Collection<TypeElement> useCol) {
    uses.addAll(useCol);
  }

  public void setUses(Set<TypeElement> uses) {
    this.uses = uses;
  }

  public Set<OutputProperty> getProperties() {
    return properties;
  }

  public FlatBeanClassFactory getFactoryAnnotation() {
    return factoryAnnotation;
  }

  public void setFactoryAnnotation(FlatBeanClassFactory factoryAnnotation) {
    this.factoryAnnotation = factoryAnnotation;
  }

  public void addProperty(OutputProperty property) {
    properties.add(property);
  }

  public void setProperties(Set<OutputProperty> properties) {
    this.properties = properties;
  }

  public ExecutableElement getFactoryMethodName() {
    return factoryMethodName;
  }

  public void setFactoryMethodName(ExecutableElement factoryMethodName) {
    this.factoryMethodName = factoryMethodName;
  }

  public TypeElement getFactoryClass() {
    return factoryClass;
  }

  public void setFactoryClass(TypeElement factoryClass) {
    this.factoryClass = factoryClass;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("uses", uses)
        .add("properties", properties)
        .add("factoryAnnotation", factoryAnnotation)
        .add("factoryMethodName", factoryMethodName)
        .add("factoryClass", factoryClass)
        .toString();
  }
}
