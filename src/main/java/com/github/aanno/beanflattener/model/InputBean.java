package com.github.aanno.beanflattener.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.lang.model.element.TypeElement;

public class InputBean {

  // wrapped type
  private TypeElement value;

  public InputBean() {}

  public TypeElement getValue() {
    return value;
  }

  public void setValue(TypeElement value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("value", value)
        .toString();
  }
}
