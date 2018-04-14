package com.olehprod.aw.gui.base;

import com.olehprod.aw.gui.enums.EmulationMode;
import com.olehprod.aw.gui.enums.LocatorType;
import com.olehprod.aw.gui.pages.MainPage;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.reporters.SuiteHTMLReporter;
import org.testng.reporters.XMLReporter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Log
@Listeners({SuiteHTMLReporter.class, XMLReporter.class})
public class BaseTest {

    private static Actions actions = null;
    private static WebDriver driver = null;

    private static final String BASE_URL = "https://www.accuweather.com/";

    // Only explicit timeouts like best practice
    protected static final int EXPLICIT = 8;

    // Anyway BaseTest can be created only by page classes
    public WebDriver getWebDriver() {
        return driver;
    }

    @BeforeTest
    protected void setUp() {
        // TODO: Add Linux and Mac support
        System.setProperty("webdriver.chrome.driver", "src/main/resources/driver/chrome_win32.exe");
        ChromeOptions chromeOptions = new ChromeOptions().addArguments("disable-infobars", "--disable-extensions", "test-type");
        chromeOptions.setCapability("pageLoadStrategy", "none");
        driver = new ChromeDriver(getChromeEmulationMode(chromeOptions, EmulationMode.IPAD_PRO.getEmulator()));
        turnOnImplicitWaits();
        // Actions is a user-facing API for emulating complex user gestures. Use this class rather than
        // using the Keyboard or Mouse directly.
        actions = new Actions(driver);
    }

    @AfterMethod
    protected void afterMethod(ITestResult testResult) {
        if (testResult.getStatus() == ITestResult.FAILURE) snapScreenshot();
    }

    @AfterTest
    protected void tearDown() {
        driver.quit();
    }

    public WebElement findElement(LocatorType locatorType, String locatorStr) {
        WebElement element = null;
        try {
            turnOffImplicitWaits();
            WebDriverWait wait = new WebDriverWait(driver, EXPLICIT);

            By byLocator = null;
            switch (locatorType) {
                case CLASS_NAME:
                    byLocator = By.className(locatorStr);
                    break;
                case CSS_SELECTOR:
                    byLocator = By.cssSelector(locatorStr);
                    break;
                case ID:
                    byLocator = By.id(locatorStr);
                    break;
                case LINK_TEXT:
                    byLocator = By.linkText(locatorStr);
                    break;
                case NAME:
                    byLocator = By.name(locatorStr);
                    break;
                case PARTIAL_LINK_TEXT:
                    byLocator = By.partialLinkText(locatorStr);
                    break;
                case TAG_NAME:
                    byLocator = By.tagName(locatorStr);
                    break;
                case XPATH:
                    byLocator = By.xpath(locatorStr);
                    break;
            }

            element = wait.ignoring(StaleElementReferenceException.class).until(
                    ExpectedConditions.visibilityOfElementLocated(byLocator));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Assert.fail(String.format("Element with xpath: <%s> not found", locatorStr));
        } catch (TimeoutException e) {
            e.printStackTrace();
            Assert.fail(String.format("Timeout exception caught while waiting for element: <%s>", locatorStr));
        } finally {
            turnOnImplicitWaits();
        }

        return element;
    }

    private ChromeOptions getChromeEmulationMode(ChromeOptions chromeOptions, String deviceName) {
        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", deviceName);
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
        return chromeOptions;
    }

    /**
     * Get int from String by regex
     *
     * @param str
     * @return int
     */
    public int getNumFromStr(String str) {
        return Integer.parseInt(str.replaceAll(".*?([-+]?\\d+).*", "$1"));
    }

    public MainPage openWeather() {
        driver.get(BASE_URL);
        return new MainPage(this);
    }

    private void snapScreenshot() {
        DateFormat date = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        String fileName = this.getClass().getName() + "_" + date.format(new Date());
        String screenshotPath = System.getProperties().get("user.dir") + "\\screenshots\\";
        try {
            File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(f, new File(screenshotPath + File.separator + fileName + ".png"));
            log.config("Screenshot file [" + fileName + "]" + " written in [" + screenshotPath + "]");
        } catch (IOException e) {
            throw new RuntimeException("Unable to store screenshot.", e);
        }
    }

    private void turnOffImplicitWaits() {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    }

    private void turnOnImplicitWaits() {
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    public void waitTillClickableAndClickElement(WebElement element) {
        waitTillElementIsClickable(element);
        turnOffImplicitWaits();
        element.click();
        turnOnImplicitWaits();
    }

    public void waitTillElementIsClickable(WebElement element) {
        try {
            turnOffImplicitWaits();
            WebDriverWait wait = new WebDriverWait(driver, EXPLICIT);
            wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(element));
        } catch (NoSuchElementException e) {
            Assert.fail("NoSuchElement exception caught for element " + e.toString());
        } catch (TimeoutException e) {
            Assert.fail("Timeout exception caught for element " + e.toString());
        } finally {
            turnOnImplicitWaits();
        }

        actions.moveToElement(element).build().perform();
    }

    public void waitTillElementIsVisible(WebElement element) {
        waitTillElementIsVisible(element, false, EXPLICIT);
    }

    public void waitTillElementIsVisible(WebElement element, boolean throwException, long timeOutInSeconds) {
        try {
            turnOffImplicitWaits();
            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(element));
        } catch (NoSuchElementException e) {
            if (throwException) {
                throw (e);
            }
            Assert.fail("NoSuchElement exception caught for element " + e.toString());
        } catch (TimeoutException e) {
            if (throwException) {
                throw (e);
            }
            Assert.fail("Timeout exception caught for element " + e.toString());
        } finally {
            turnOnImplicitWaits();
        }
    }

    public void waitTillElementsAreVisible(List<WebElement> elements) {
        waitTillElementsAreVisible(elements, false, EXPLICIT);
    }

    public void waitTillElementsAreVisible(List<WebElement> elements, boolean throwException, long timeOutInSeconds) {
        try {
            turnOffImplicitWaits();
            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOfAllElements(elements));
        } catch (NoSuchElementException e) {
            if (throwException) {
                throw (e);
            }
            Assert.fail("NoSuchElement exception caught for element " + e.toString());
        } catch (TimeoutException e) {
            if (throwException) {
                throw (e);
            }
            Assert.fail("Timeout exception caught for element " + e.toString());
        } finally {
            turnOnImplicitWaits();
        }
    }

}
