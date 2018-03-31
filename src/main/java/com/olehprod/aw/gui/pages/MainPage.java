package com.olehprod.aw.gui.pages;

import com.olehprod.aw.gui.base.BaseTest;
import lombok.extern.java.Log;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Log
public class MainPage extends AbstractPage {

    @FindBy(id = "header-nav")
    protected WebElement navigationBar;

    @FindBy(id = "content")
    protected WebElement pageContent;

    @FindBy(id = "s")
    protected WebElement searchInput;

    @FindBy(css = "#findcity > button.bt-go")
    protected WebElement searchButton;

    public MainPage(BaseTest testClass) {
        super(testClass);
        log.info(String.format(">>> Initializing <%s> <<<", this.getClass().getSimpleName()));

        // Verify that page content was loaded
        testClass.waitTillElementIsVisible(navigationBar);
        testClass.waitTillElementIsVisible(pageContent);
    }

    public CurrentLocationPage searchForLocation(String location) {
        typeIn(searchInput, location);
        testClass.waitTillClickableAndClickElement(searchButton);
        return new CurrentLocationPage(testClass);
    }

}
