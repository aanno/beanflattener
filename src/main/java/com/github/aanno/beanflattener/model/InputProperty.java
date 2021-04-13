package com.github.aanno.beanflattener.model;

import com.google.common.base.MoreObjects;

public class InputProperty extends Property {

  private String from;
  private boolean ignore;

  public InputProperty() {}

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public boolean isIgnore()
  {
    return ignore;
  }

  public void setIgnore(boolean ignore)
  {
    this.ignore = ignore;
  }

  @Override
  public String toString()
  {
    return MoreObjects.toStringHelper(this)
            .add("parentProp", super.toString())
            .add("from", from)
            .add("ignore", ignore)
            .toString();
  }
}
