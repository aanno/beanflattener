package com.github.aanno.beanflattener.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class PropertyConverter {

  private String name;
  private String fromType;
  private String toType;

  public PropertyConverter() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFromType() {
    return fromType;
  }

  public void setFromType(String fromType) {
    this.fromType = fromType;
  }

  public String getToType() {
    return toType;
  }

  public void setToType(String toType) {
    this.toType = toType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PropertyConverter)) return false;
    PropertyConverter that = (PropertyConverter) o;
    return Objects.equal(name, that.name)
        && Objects.equal(fromType, that.fromType)
        && Objects.equal(toType, that.toType);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, fromType, toType);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .add("fromType", fromType)
        .add("toType", toType)
        .toString();
  }
}
