package com.github.aanno.beanflattener.model;

import com.google.common.base.MoreObjects;

public class InputProperty extends Property {

  private String from;

  public InputProperty() {}

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("parentProp", super.toString())
            .add("from", from)
            .toString();
  }
}
