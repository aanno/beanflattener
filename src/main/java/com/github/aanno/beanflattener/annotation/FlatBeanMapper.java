package com.github.aanno.beanflattener.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
@Inherited
public @interface FlatBeanMapper {

    Class<?> value();

    FlatBeanMap[] mappers() default {};
}
