package com.github.aanno.beanflattener.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import javax.lang.model.element.TypeElement;

public class Property implements Comparable<Property> {

  private String name;
  private TypeElement typeElement;

  public Property() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TypeElement getTypeElement() {
    return typeElement;
  }

  public void setTypeElement(TypeElement typeElement) {
    this.typeElement = typeElement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Property)) return false;
    Property property = (Property) o;
    return Objects.equal(name, property.name)
        && Objects.equal(
            typeElement.getQualifiedName().toString(),
            property.typeElement.getQualifiedName().toString());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, typeElement);
  }

  @Override
  public int compareTo(Property that) {
    return ComparisonChain.start()
        .compare(this.name, that.name)
        .compare(
            this.typeElement.getQualifiedName().toString(),
            that.typeElement.getQualifiedName().toString())
        .result();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("name", name)
            .add("typeElement", typeElement).toString();
  }
}
