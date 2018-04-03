package com.olehprod.aw.gui.pages;

import com.olehprod.aw.gui.base.BaseTest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public abstract class AbstractLocationPage extends MainPage {

    @FindBy(xpath = "//div[contains(@class, 'sub-header-wrap')]" +
            "//div[contains(@class, 'inner-subnav')]//ul[contains(@class, 'subnav-tab-buttons')]/li[.='Month']")
    protected WebElement month;

    public AbstractLocationPage(BaseTest testClass) {
        super(testClass);
    }

    public CalendarMonthLocationPage monthClick() {
        testClass.waitTillClickableAndClickElement(month);
        return new CalendarMonthLocationPage(testClass);
    }

}
