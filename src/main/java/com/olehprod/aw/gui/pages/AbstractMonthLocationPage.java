package com.olehprod.aw.gui.pages;

import com.olehprod.aw.gui.base.BaseTest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public abstract class AbstractMonthLocationPage extends AbstractLocationPage {

    @FindBy(xpath = "//div[contains(@class, 'panel-body')]//div[contains(@class, 'calendar-controls')]" +
            "//div[contains(@class, 'view')]//a[contains(@class, 'view-list')]/..")
    protected WebElement tableView;

    public AbstractMonthLocationPage(BaseTest testClass) {
        super(testClass);
    }

    public TableMonthLocationPage tableViewClick() {
        testClass.waitTillClickableAndClickElement(tableView);
        return new TableMonthLocationPage(testClass);
    }

}
