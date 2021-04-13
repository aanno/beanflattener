package com.github.aanno.beanflattener.test;

import com.github.aanno.beanflattener.annotation.FlatBeanClassFactory;
import com.github.aanno.beanflattener.annotation.FlatBeanMap;
import com.github.aanno.beanflattener.annotation.FlatBeanMapper;

public interface Factory1 {

  @FlatBeanClassFactory(
      uses = {Bean2.class},
      mappers = {
        @FlatBeanMapper(
            value = Bean1.class,
            mappers = {
              @FlatBeanMap(from = "i1", to = "integer"),
              @FlatBeanMap(from = "a", to = "b")
            }),
        @FlatBeanMapper(
            value = Bean2.class,
            mappers = @FlatBeanMap(from = "string", ignore = true))
      })
  Class<?> generate1(Bean1 wrapped);
}
