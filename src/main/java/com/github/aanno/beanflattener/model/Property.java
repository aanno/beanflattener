package com.github.aanno.beanflattener.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class Property implements Comparable<Property> {

  private String name;
  private String clazz;

  public Property() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getClazz() {
    return clazz;
  }

  public void setClazz(String clazz) {
    this.clazz = clazz;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Property)) return false;
    Property property = (Property) o;
    return Objects.equal(name, property.name) && Objects.equal(clazz, property.clazz);
  }

  @Override
  public int compareTo(Property that) {
    return ComparisonChain.start()
            .compare(this.name, that.name)
            // .compare(this.clazz, that.clazz)
            .result();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, clazz);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("name", name).add("clazz", clazz).toString();
  }
}
