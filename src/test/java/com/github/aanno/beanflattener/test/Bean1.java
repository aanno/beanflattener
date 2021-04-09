package com.github.aanno.beanflattener.test;

public class Bean1 {

  private int i1;
  private Integer i2;
  private String string;
  private Bean2 bean2;

  public Bean1() {}

  public int getI1() {
    return i1;
  }

  public void setI1(int i1) {
    this.i1 = i1;
  }

  public Integer getI2() {
    return i2;
  }

  public void setI2(Integer i2) {
    this.i2 = i2;
  }

  public String getString() {
    return string;
  }

  public void setString(String string) {
    this.string = string;
  }

  public Bean2 getBean2() {
    return bean2;
  }

  public void setBean2(Bean2 bean2) {
    this.bean2 = bean2;
  }

  @Override
  public String toString() {
    return com.google.common.base.MoreObjects.toStringHelper(this)
        .add("i1", i1)
        .add("i2", i2)
        .add("string", string)
        .add("bean2", bean2)
        .toString();
  }
}
