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
  private Set<InputBean> inputBeans = new TreeSet<>();
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

  public FlatBeanClassFactory getFactoryAnnotation() {
    return factoryAnnotation;
  }

  public void setFactoryAnnotation(FlatBeanClassFactory factoryAnnotation) {
    this.factoryAnnotation = factoryAnnotation;
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

  public Set<InputBean> getInputBeans()
  {
    return inputBeans;
  }

  public void addInputBean(InputBean inputBean) {
    inputBeans.add(inputBean);
  }

  public void setInputBeans(Set<InputBean> inputBeans)
  {
    this.inputBeans = inputBeans;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("uses", uses)
        .add("inputBeans", inputBeans)
        .add("factoryAnnotation", factoryAnnotation)
        .add("factoryMethodName", factoryMethodName)
        .add("factoryClass", factoryClass)
        .toString();
  }
}
