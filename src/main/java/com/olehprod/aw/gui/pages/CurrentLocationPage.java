package com.olehprod.aw.gui.pages;

import com.olehprod.aw.gui.base.BaseTest;
import com.olehprod.aw.gui.enums.LocatorType;

public class CurrentLocationPage extends AbstractLocationPage {

    protected final static String SELECTOR_CURRENT_TEMP_TEMPLATE = "div#content #feed-tabs .current .temp > span%s";

    public CurrentLocationPage(BaseTest testClass) {
        super(testClass);
    }

    public int getCurrentRealFeelTemp() {
        return getCurrentTemp(".realfeel");
    }

    public int getCurrentTemp() {
        return getCurrentTemp(".large-temp");
    }

    private int getCurrentTemp(String selectorClass) {
        String realFeelTemp = testClass.findElement(
                LocatorType.CSS_SELECTOR, String.format(SELECTOR_CURRENT_TEMP_TEMPLATE, selectorClass)).getText();
        return testClass.getNumFromStr(realFeelTemp);
    }

}
