package com.github.aanno.beanflattener.model;

import com.google.common.base.MoreObjects;

public class InputProperty extends Property {

  private InputBean from;

  public InputProperty() {}

  public InputBean getFrom() {
    return from;
  }

  public void setFrom(InputBean from) {
    this.from = from;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("from", from).toString();
  }
}
