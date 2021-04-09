package com.github.aanno.beanflattener.model;

import com.github.aanno.beanflattener.annotation.FlatBeanClassFactory;
import com.google.common.base.MoreObjects;

import java.util.Set;
import java.util.TreeSet;

public class OutputBean extends Bean {

  private Set<String> uses;
  private Set<OutputProperty> properties = new TreeSet<>();
  private FlatBeanClassFactory factoryAnnotation;
  private String factoryMethodName;
  private String factoryClass;

  public OutputBean() {}

  public Set<String> getUses() {
    return uses;
  }

  public void addUses(String use) {
    uses.add(use);
  }

  public void setUses(Set<String> uses) {
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

  public String getFactoryMethodName() {
    return factoryMethodName;
  }

  public void setFactoryMethodName(String factoryMethodName) {
    this.factoryMethodName = factoryMethodName;
  }

  public String getFactoryClass() {
    return factoryClass;
  }

  public void setFactoryClass(String factoryClass) {
    this.factoryClass = factoryClass;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("uses", uses)
        .add("properties", properties)
        .toString();
  }
}
