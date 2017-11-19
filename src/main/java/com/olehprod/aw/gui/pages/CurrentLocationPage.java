package com.olehprod.aw.gui.pages;

import com.olehprod.aw.gui.base.BaseTest;
import com.olehprod.aw.gui.enums.LocatorType;

public class CurrentLocationPage extends AbstractLocationPage {

    protected final static String SELECTOR_CURRENT_TEMP_TEMPLATE = "div#content #feed-tabs .current .temp > span%s";

    /**
     * Constructor
     *
     * @param testClass
     */
    public CurrentLocationPage(BaseTest testClass) {
        super(testClass);
    }

    /**
     * Get real feel temperature
     *
     * @return realFeelTemp
     */
    public int getCurrentRealFeelTemp() {
        return getCurrentTemp(".realfeel");
    }

    /**
     * Get current temperature
     *
     * @return currentTemp
     */
    public int getCurrentTemp() {
        return getCurrentTemp(".large-temp");
    }

    /**
     * Get current temperature
     *
     * @param selectorClass
     * @return temperature from custom selectorClass
     */
    private int getCurrentTemp(String selectorClass) {
        String realFeelTemp = testClass.findElement(
                LocatorType.CSS_SELECTOR, String.format(SELECTOR_CURRENT_TEMP_TEMPLATE, selectorClass)).getText();
        return testClass.getNumFromStr(realFeelTemp);
    }

}
