package com.github.aanno.beanflattener.model;

import com.google.common.base.MoreObjects;

public class OutputProperty extends Property {

    private PropertyConverter converter;

    public OutputProperty() {
    }

    public PropertyConverter getConverter() {
        return converter;
    }

    public void setConverter(PropertyConverter converter) {
        this.converter = converter;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("converter", converter)
                .toString();
    }
}
