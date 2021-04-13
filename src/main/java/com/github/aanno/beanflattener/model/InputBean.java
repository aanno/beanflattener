package com.github.aanno.beanflattener.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.TreeSet;

public class InputBean implements Comparable<InputBean> {

  // wrapped type
  private TypeElement value;

  private Set<InputProperty> inputProperties = new TreeSet<>();

  public InputBean() {}

  public TypeElement getValue() {
    return value;
  }

  public void setValue(TypeElement value) {
    this.value = value;
  }

  public void addProperty(InputProperty inputProperty) {
    inputProperties.add(inputProperty);
  }

  public Set<InputProperty> getInputProperties()
  {
    return inputProperties;
  }

  public void setInputProperties(Set<InputProperty> inputProperties)
  {
    this.inputProperties = inputProperties;
  }

  @Override
  public int compareTo(InputBean that)
  {
    return ComparisonChain.start()
            .compare(this.value.getQualifiedName().toString(), that.value.getQualifiedName().toString())
            .result();
  }

  @Override
  public String toString()
  {
    return MoreObjects.toStringHelper(this)
            .add("value", value)
            .add("inputProperties", inputProperties)
            .toString();
  }
}
