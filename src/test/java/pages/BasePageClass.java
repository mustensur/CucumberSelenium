package pages;

import driver.DriverFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.aerogear.security.otp.Totp;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import static driver.DriverFactory.getStringFromPropertiesFile;

public class BasePageClass
{
    public BasePageClass()
    {
        PageFactory.initElements(getDriver(), this);
    }

    public WebDriver getDriver()
    {
        return DriverFactory.getDriver();
    }

    /**
     * navigate to URL
     *
     * @param url
     */
    public void navigateTo_URL(String url) throws Exception
    {
        String strEnv = getStringFromPropertiesFile("env");
        String strUrl = null;
        if (strEnv.equals("dev"))
        {
            strUrl = getStringFromPropertiesFile("base_dev_url");
        } else if (strEnv.equals("qa"))
        {
            strUrl = getStringFromPropertiesFile("base_qa_url");
        } else
        {
            throw new Exception("Invalid Environment");
        }
        getDriver().get(strUrl + url);
    }

    /**
     * This is used to send keys option in Selenium
     *
     * @param by   - option to select by locator
     * @param text - the text that will be entered into the text-box
     */
    public void sendKeys(By by, String text)
    {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(by)).sendKeys(text);
    }

    /**
     * Used to send keys using Webelement
     *
     * @param element
     * @param text
     */
    public void sendKeys(WebElement element, String text)
    {
        waitForElement(element).sendKeys(text);
    }


    /**
     * Used to setText using Webelement
     *
     * @param element
     * @param text
     */
    public void setText(WebElement element, String text)
    {
        WebElement ele = waitForElement(element);
        while(!ele.getAttribute("value").equals("")) { ele.sendKeys(Keys.CONTROL + "a", Keys.DELETE); }
        ele.sendKeys(text);
    }

    /**
     * Selenium click command using By
     *
     * @param by
     */
    public void click(By by)
    {
        waitForElement(by).click();
    }

    /**
     * Selenium click command using Webelement
     *
     * @param element
     */
    public void click(WebElement element)
    {
        waitForElement(element).click();
    }

    /**
     * returns the string value of the element
     *
     * @param by
     * @return
     */
    public String getText(By by)
    {
        return waitForElement(by).getText();
    }

    /**
     * returns the string value of the element
     *
     * @param element
     * @return
     */
    public String getText(WebElement element)
    {
        return waitForElement(element).getText();
    }

    /**
     * Find specific text on a webpage
     * @param text
     * @return
     */
    public boolean findText(String text)
    {
        return DriverFactory.getDriver().getPageSource().contains(text);
    }

    public Boolean isDisplayed(WebElement element)
    {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
        return wait.until(ExpectedConditions.elementToBeClickable(element)).isDisplayed();
    }

    /**
     * Select dropdown by value
     *
     * @param element
     * @param value
     */
    public void selectDropdownByValue(WebElement element, String value)
    {
        Select select = new Select(waitForElement(element));
        select.selectByValue(value);
    }

    /**
     * Select dropdown by index
     *
     * @param element
     * @param index
     */
    public void selectDropdownByIndex(WebElement element, int index)
    {
        Select select = new Select(waitForElement(element));
        select.selectByIndex(index);
    }

    /**
     * Select by visible text
     *
     * @param element
     * @param visibleText
     */
    public void selectDropdownByVisibleText(WebElement element, String visibleText)
    {
        Select select = new Select(waitForElement(element));
        select.selectByVisibleText(visibleText);
    }

    /**
     * Wait for number of seconds
     *
     * @param durationInSeconds
     */
    public void waitNumberOfSeconds(long durationInSeconds)
    {
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(durationInSeconds));
    }

    /**
     * Generate a random number
     *
     * @param length - the number of characters to generate
     * @return
     */
    public String generateRandomNumber(int length)
    {
        return RandomStringUtils.randomNumeric(length);
    }

    /**
     * Get the secret from the properties file which can be used to generate the OTP
     *
     * @param user - defined in the properties file minus the .otp.secret
     * @return
     * @throws IOException
     */
    private static String getOtpFromPropertiesFile(String user) throws IOException
    {
        String otpKeyString = null;
        Properties properties = new Properties();
        FileInputStream file = new FileInputStream(System.getProperty("user.dir")
                + "/src/main/java/properties/config.properties");
        properties.load(file);
        otpKeyString = properties.getProperty(user + ".otp.secret").trim();

        return otpKeyString;
    }

    /**
     * Get
     *
     * @param user
     * @return
     * @throws IOException
     */

    public static String generateOtpForUser(String user) throws IOException
    {
        String secret = getOtpFromPropertiesFile(user);
        Totp totp = new Totp(secret);
        return totp.now();
    }

    /**
     * Get locator
     *
     * @param elementProp
     * @param elementType
     * @param index
     * @return
     */
    public static By getLocator(String elementProp, String elementType, String index)
    {
        String strLocator = elementProp.replace("{0}", index);
        return getElement(strLocator, elementType);
    }

    /**
     * Get locator
     *
     * @param elementProp
     * @param elementType
     * @return
     */
    public static By getLocator(String elementProp, String elementType)
    {
        return getElement(elementProp, elementType);
    }

    /**
     * Get locator
     *
     * @param locator
     * @param elementType
     * @return
     */
    private static By getElement(String locator, String elementType)
    {
        switch (elementType.toLowerCase())
        {
            case "name":
                return By.name(locator);
            case "class":
                return By.className(locator);
            case "id":
                return By.id(locator);
            case "cssselector":
                return By.cssSelector(locator);
            case "xpath":
                return By.xpath(locator);
            case "linktext":
                return By.linkText(locator);
            case "partiallinktext":
                return By.partialLinkText(locator);
            default:
                return null;
        }
    }

    /**
     * Verify element is exists
     *
     * @param by
     * @return
     */
    public boolean verifyElementExists(By by)
    {
        try
        {
            return waitForElement(by).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e)
        {
            return false;
        }
    }

    /**
     * Verify element is exists
     *
     * @param element
     * @return
     */
    public boolean verifyElementExists(WebElement element)
    {
        try
        {
            return element.isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e)
        {
            return false;
        }
    }

    /**
     * Verify mouse hover on element
     *
     * @param by perform
     */
    public void mouseHoverOnElement(By by)
    {
        new Actions(getDriver()).moveToElement(waitForElement(by)).build().perform();
    }

    /**
     * Verify mouse hover on element
     *
     * @param element perform
     */
    public void mouseHoverOnElement(WebElement element)
    {
        new Actions(getDriver()).moveToElement(waitForElement(element)).build().perform();
    }

    /**
     * Move to element
     *
     * @param element
     */
    public void moveToElement(WebElement element)
    {
        new Actions(getDriver()).moveToElement(waitForElement(element)).build().perform();
    }

    /**
     * wait until page load
     *
     */
    public void waitUntilPageLoad(){
        WebDriverWait pageLoad = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");}
        };
        pageLoad.until(pageLoadCondition);
    }

    /**
     * Wait for element
     *
     * @param by
     */
    public WebElement waitForElement(By by)
    {
        WebElement element = (new WebDriverWait(getDriver(), Duration.ofSeconds(25))).
                until(ExpectedConditions.visibilityOfElementLocated(by));
        return element;
    }

    /**
     * Wait for element
     *
     * @param ele
     */
    public WebElement waitForElement(WebElement ele)
    {
        WebElement element = (new WebDriverWait(getDriver(), Duration.ofSeconds(25))).
                until(ExpectedConditions.elementToBeClickable(ele));
        return element;
    }

    /**
     * Get Random String
     *
     * @param stringLength
     */
    public String getRandomString(int stringLength)
    {
        return RandomStringUtils.randomAlphanumeric(stringLength);
    }

    /**
     * Get Random Email
     *
     * @param emailLength
     */
    public String getRandomOutlookEmail(int emailLength)
    {
        return RandomStringUtils.randomAlphanumeric(emailLength) + "@outlook.com";
    }


}
