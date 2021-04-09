package com.github.aanno.beanflattener.model;

import com.google.common.base.MoreObjects;

public class InputProperty extends Property {

  private Bean from;

  public InputProperty() {}

  public Bean getFrom() {
    return from;
  }

  public void setFrom(Bean from) {
    this.from = from;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("from", from).toString();
  }
}
