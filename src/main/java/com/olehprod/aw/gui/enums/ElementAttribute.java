package com.olehprod.aw.gui.enums;

public enum ElementAttribute {

    VALUE("value");

    private final String attribute;

    /**
     * Constructor
     *
     * @param attribute
     */
    ElementAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
     * Getter
     *
     * @return attribute
     */
    public String getAttribute() {
        return attribute;
    }
}
