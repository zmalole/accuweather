package com.olehprod.aw.gui.tests.temperature;

import com.olehprod.aw.gui.base.BaseTest;
import com.olehprod.aw.gui.pages.CurrentLocationPage;
import com.olehprod.aw.gui.pages.MainPage;
import com.olehprod.aw.gui.pages.TableMonthLocationPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CompareTemperature extends BaseTest {

    private final static String LOCATION_KOROSTYSHIV = "Korostyshiv";

    @Test(description = "Current temperature equals to Real Feel temperature in Current Weather panel")
    public void testCompareCurrentToRealFeelTemp() {
        MainPage mainPage = openWeather();
        log("Opened weather website");

        CurrentLocationPage currentLocationPage = mainPage.searchForLocation(LOCATION_KOROSTYSHIV);
        log("Found location <%s>", LOCATION_KOROSTYSHIV);

        Assert.assertEquals(currentLocationPage.getCurrentRealFeelTemp(), currentLocationPage.getCurrentTemp(),
                "Current temperature isn't equals to real feel temperature");
        log("Verified current temperature equals to real feel temperature");
    }

    @Test(description = "The lowest temperature is lower than today for current month")
    public void testCompareTodayToTheColdestDay() {
        MainPage mainPage = openWeather();
        log("Opened weather website");

        CurrentLocationPage currentLocationPage = mainPage.searchForLocation(LOCATION_KOROSTYSHIV);
        log("Found location <%s>", LOCATION_KOROSTYSHIV);

        TableMonthLocationPage tableMonthLocationPage = currentLocationPage.monthClick().tableViewClick();
        log("Opened current month in table view");

        Assert.assertTrue(
                tableMonthLocationPage.getLowestTempForMonth() < tableMonthLocationPage.getLowestTempForToday(),
                "The lowest temperature for month should be lower than today");
        log("Verified the lowest temperature for month is lower than today");
    }

}
