package com.olehprod.aw.gui.enums;

import lombok.Getter;

@Getter
public enum ElementAttribute {

    VALUE("value");

    private final String attribute;

    ElementAttribute(String attribute) {
        this.attribute = attribute;
    }

}
