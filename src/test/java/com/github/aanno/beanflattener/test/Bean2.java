package com.github.aanno.beanflattener.test;

import com.google.common.base.MoreObjects;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Bean2 {

  private List<Bean1> beans;
  private Map<String, String> map;
  private String[] strings;
  private Set<Double> doubles;
  private String string;

  public Bean2() {}

  public String getString() {
    return string;
  }

  public void setString(String string) {
    this.string = string;
  }

  public List<Bean1> getBeans() {
    return beans;
  }

  public void setBeans(List<Bean1> beans) {
    this.beans = beans;
  }

  public Map<String, String> getMap() {
    return map;
  }

  public void setMap(Map<String, String> map) {
    this.map = map;
  }

  public String[] getStrings() {
    return strings;
  }

  public void setStrings(String[] strings) {
    this.strings = strings;
  }

  public Set<Double> getDoubles() {
    return doubles;
  }

  public void setDoubles(Set<Double> doubles) {
    this.doubles = doubles;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("beans", beans)
        .add("map", map)
        .add("strings", strings)
        .add("string", string)
        .add("doubles", doubles)
        .toString();
  }
}
