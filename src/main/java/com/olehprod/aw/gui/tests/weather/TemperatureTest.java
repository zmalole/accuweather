package com.olehprod.aw.gui.tests.weather;

import com.olehprod.aw.gui.base.BaseTest;
import com.olehprod.aw.gui.pages.CurrentLocationPage;
import com.olehprod.aw.gui.pages.MainPage;
import com.olehprod.aw.gui.pages.TableMonthLocationPage;
import lombok.extern.java.Log;
import org.testng.Assert;
import org.testng.annotations.Test;

@Log
public class TemperatureTest extends BaseTest {

    private final static String LOCATION_KOROSTYSHIV = "Korostyshiv";

    @Test(description = "Current weather equals to Real Feel weather in Current Weather panel")
    public void testCompareCurrentToRealFeelTemp() {
        MainPage mainPage = openWeather();
        log.info("Opened weather website");

        CurrentLocationPage currentLocationPage = mainPage.searchForLocation(LOCATION_KOROSTYSHIV);
        log.info(String.format("Found location <%s>", LOCATION_KOROSTYSHIV));

        Assert.assertEquals(currentLocationPage.getCurrentRealFeelTemp(), currentLocationPage.getCurrentTemp(),
                "Current weather isn't equals to real feel weather");
        log.info("Verified current weather equals to real feel weather");
    }

    @Test(description = "The lowest weather is lower than today for current month")
    public void testCompareTodayToTheColdestDay() {
        MainPage mainPage = openWeather();
        log.info("Opened weather website");

        CurrentLocationPage currentLocationPage = mainPage.searchForLocation(LOCATION_KOROSTYSHIV);
        log.info(String.format("Found location <%s>", LOCATION_KOROSTYSHIV));

        TableMonthLocationPage tableMonthLocationPage = currentLocationPage.monthClick().tableViewClick();
        log.info("Opened current month in table view");

        Assert.assertTrue(
                tableMonthLocationPage.getLowestTempForMonth() < tableMonthLocationPage.getLowestTempForToday(),
                "The lowest weather for month should be lower than today");
        log.info("Verified the lowest weather for month is lower than today");
    }

}
