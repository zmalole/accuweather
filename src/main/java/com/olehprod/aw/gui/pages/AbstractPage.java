package com.olehprod.aw.gui.pages;

import com.olehprod.aw.gui.base.BaseTest;
import com.olehprod.aw.gui.enums.ElementAttribute;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public abstract class AbstractPage {

    protected final WebDriver driver;
    protected BaseTest testClass;

    /**
     * Constructor
     *
     * @param testClass
     */
    public AbstractPage(BaseTest testClass) {
        this.testClass = testClass;
        driver = testClass.getWebDriver();

        PageFactory.initElements(driver, this);
    }

    /**
     * Get the lowest temperature
     *
     * @param temp
     * @return String - the lowest temperature
     */
    protected String getLowestTemp(String temp) {
        return temp.substring(temp.indexOf('/') + 1);
    }

    /**
     * If text contains data, populate a field with it
     * Expect exactly this text to be present in element value
     *
     * @param element
     * @param text
     */
    private void populateElementWithValidData(WebElement element, String text) {
        String value = ElementAttribute.VALUE.getAttribute();
        element.clear();
        Assert.assertEquals(element.getAttribute(value), "", "Element should be empty");

        if (!text.isEmpty()) {
            element.sendKeys(text);
            Assert.assertEquals(element.getAttribute(value), text, "Incorrect text in the element");
        }
    }

    /**
     * Enter text into element
     *
     * @param element
     * @param text
     */
    protected void typeIn(WebElement element, String text) {
        testClass.waitTillClickableAndClickElement(element);
        populateElementWithValidData(element, text);
    }

}
