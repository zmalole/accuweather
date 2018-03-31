package com.olehprod.aw.gui.pages;

import com.olehprod.aw.gui.base.BaseTest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableMonthLocationPage extends AbstractMonthLocationPage {

    @FindBy(xpath = "//div[@id='content']//div[contains(@class, 'panel-body')]" +
            "//table[contains(@class, 'calendar')]//td[1]")
    protected List<WebElement> highLowTempList;

    @FindBy(xpath = "//div[@id='content']//div[contains(@class, 'panel-body')]" +
            "//table[contains(@class, 'calendar')]//tr[contains(@class, 'today')]//td[1]")
    protected WebElement highLowTempToday;

    public TableMonthLocationPage(BaseTest testClass) {
        super(testClass);
    }

    public int getLowestTempForMonth() {
        testClass.waitTillElementsAreVisible(highLowTempList);

        List<Integer> highLowTempValues = new ArrayList<>();
        for (WebElement highLowTemp : highLowTempList) {
            String highLowTempValue = highLowTemp.getText().replaceAll(" ", "");
            if (!highLowTempValue.isEmpty()) {
                int lowestTempValue = testClass.getNumFromStr(getLowestTemp(highLowTempValue));
                highLowTempValues.add(lowestTempValue);
            }
        }

        Collections.sort(highLowTempValues);

        return highLowTempValues.get(0);
    }

    public int getLowestTempForToday() {
        testClass.waitTillElementIsVisible(highLowTempToday);

        String highLowTempTodayValue = highLowTempToday.getText();
        return testClass.getNumFromStr(getLowestTemp(highLowTempTodayValue));
    }

}
