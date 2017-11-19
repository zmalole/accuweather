package com.olehprod.aw.gui.base;

import com.olehprod.aw.gui.enums.EmulationMode;
import com.olehprod.aw.gui.enums.LocatorType;
import com.olehprod.aw.gui.pages.MainPage;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.log4testng.Logger;
import org.testng.reporters.SuiteHTMLReporter;
import org.testng.reporters.XMLReporter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Listeners({SuiteHTMLReporter.class, XMLReporter.class})
public class BaseTest {

    private static Actions actions = null;
    private static WebDriver driver = null;
    private static Logger logger = null;

    private static final String BASE_URL = "https://www.accuweather.com/";

    // Timeouts
    protected static final int PAGE_LOAD_TIMEOUT = 60;
    protected static final int IMPLICIT_WAIT_TIMEOUT = 10;
    protected static final int EXPLICIT_WAIT_TIMEOUT = 60;

    /**
     * Get event driver
     *
     * @return WebDriver
     */
    public WebDriver getWebDriver() {
        return driver;
    }

    /**
     * Set up method
     */
    @BeforeTest
    protected void setUp() throws IOException {
        // Create instance of WebDriver
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chrome/chromedriver.exe");
        driver = new RemoteWebDriver(getHub(),
                getChromeCapabilities(getChromeEmulationMode(new ChromeOptions(), EmulationMode.IPAD_PRO.getEmulator())));

        setTimeouts(driver);

        // Actions is a user-facing API for emulating complex user gestures. Use this class rather than
        // using the Keyboard or Mouse directly.
        actions = new Actions(driver);

        logger = Logger.getLogger(this.getClass());
    }

    /**
     * After method
     *
     * @param testResult
     * @throws IOException
     */
    @AfterMethod
    protected void afterMethod(ITestResult testResult) throws IOException {
        if (testResult.getStatus() == ITestResult.FAILURE) {
            snapScreenshot();
        }
    }

    /**
     * Tear down method
     */
    @AfterTest
    protected void tearDown() {
        driver.quit();
    }

    /**
     * Find element by any locator
     *
     * @param locatorType
     * @param locatorStr
     * @return WebElement
     */
    public WebElement findElement(LocatorType locatorType, String locatorStr) {
        WebElement element = null;
        try {
            WebDriverWait wait = new WebDriverWait(driver, EXPLICIT_WAIT_TIMEOUT);

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
        }

        return element;
    }

    /**
     * Get Chrome capabilities
     *
     * @param chromeOptions
     * @return DesiredCapabilities
     */
    private DesiredCapabilities getChromeCapabilities(ChromeOptions chromeOptions) {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("test-type");
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        return capabilities;
    }

    /**
     * Get Chrome emulation mode
     *
     * @param deviceName
     * @return ChromeOptions
     */
    private ChromeOptions getChromeEmulationMode(ChromeOptions chromeOptions, String deviceName) {
        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", deviceName);
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
        return chromeOptions;
    }

    /**
     * Get hub
     *
     * @return URL
     */
    private URL getHub() {
        String urlStr = "http://localhost:16804/";
        URL hubUrl = null;
        try {
            hubUrl = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Assert.fail("Can't create hub by URL: " + urlStr);
        }
        return hubUrl;
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

    /**
     * Create log message
     *
     * @param message
     * @param arguments
     */
    public void log(String message, String... arguments) {
        logger.info(String.format(message, arguments));
    }

    /**
     * Open weather website
     *
     * @return MainPage
     */
    public MainPage openWeather() {
        driver.get(BASE_URL);
        return new MainPage(this);
    }

    /**
     * Set timeouts amd maximize WebDriver's window
     *
     * @param driver
     */
    private void setTimeouts(final WebDriver driver) {
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT_TIMEOUT, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    /**
     * Snap screenshot file
     *
     * @throws IOException
     */
    private void snapScreenshot() throws IOException {
        DateFormat date = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        String fileName = this.getClass().getName() + "_" + date.format(new Date());
        String screenshotPath = System.getProperties().get("user.dir") + "\\screenshots\\";
        try {
            File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(f, new File(screenshotPath + File.separator + fileName + ".png"));
            logger.debug("Screenshot file [" + fileName + "]" + " written in [" + screenshotPath + "]");
        } catch (IOException e) {
            throw new RuntimeException("Unable to store screenshot.", e);
        }
    }

    /**
     * Wait till element is clickable and click on it
     *
     * @param element
     */
    public void waitTillClickableAndClickElement(WebElement element) {
        waitTillElementIsClickable(element);
        element.click();
    }

    /**
     * Repeatedly check if element is clickable
     *
     * @param element
     */
    public void waitTillElementIsClickable(WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, EXPLICIT_WAIT_TIMEOUT);
            wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(element));
        } catch (NoSuchElementException e) {
            Assert.fail("NoSuchElement exception caught for element " + e.toString());
        } catch (TimeoutException e) {
            Assert.fail("Timeout exception caught for element " + e.toString());
        }

        actions.moveToElement(element).build().perform();
    }

    /**
     * Repeatedly check if the element is visible
     *
     * @param element
     */
    public void waitTillElementIsVisible(WebElement element) {
        waitTillElementIsVisible(element, false, EXPLICIT_WAIT_TIMEOUT);
    }

    /**
     * Repeatedly check if the element is visible, throw exception if throwException=true
     *
     * @param element
     * @param throwException
     * @param timeOutInSeconds
     */
    public void waitTillElementIsVisible(WebElement element, boolean throwException, long timeOutInSeconds) {
        try {
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
        }
    }

    /**
     * Repeatedly check if the elements are visible
     *
     * @param elements
     */
    public void waitTillElementsAreVisible(List<WebElement> elements) {
        waitTillElementsAreVisible(elements, false, EXPLICIT_WAIT_TIMEOUT);
    }

    /**
     * Repeatedly check if the elements are visible, throw exception if throwException=true
     *
     * @param elements
     * @param throwException
     * @param timeOutInSeconds
     */
    public void waitTillElementsAreVisible(List<WebElement> elements, boolean throwException, long timeOutInSeconds) {
        try {
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
        }
    }

}
