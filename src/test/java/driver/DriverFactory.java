package driver;

import org.jboss.aerogear.security.otp.Totp;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DriverFactory
{
    private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();


    /**
     * set the webdriver if it is not set
     * @return
     */
    public static WebDriver getDriver()
    {
        if(webDriver.get() == null)
        {
            webDriver.set(createDriver());
        }
        return webDriver.get();
    }

    /**
     * initialise the browser. Where the browser type is set in the config.properties file
     * @return webdriver
     */
    private static WebDriver createDriver()
    {
        WebDriver driver = null;

        String browserType = getBrowser();

        switch (browserType)
        {
            case "chrome" ->
            {
                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") +
                        "/src/main/java/driver/drivers/chromedriver.exe");
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");

                //Page strategy to wait until the page is loaded
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                driver = new ChromeDriver(chromeOptions);

            }
            case "firefox" ->
            {
                System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") +
                        "/src/main/java/driver/drivers/geckodriver.exe");
                FirefoxOptions firefoxOptions = new FirefoxOptions();

                //Page strategy to wait until the page is loaded
                firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                driver = new FirefoxDriver(firefoxOptions);

            }
        }
        driver.manage().window().maximize();

        return driver;
    }

    /**
     * Get the browser type from the properties file
     * @return the browser type e.g. chrome
     */
    private static String getBrowser()
    {
        String browserType = null;
        try
        {
            Properties properties = new Properties();
            FileInputStream file = new FileInputStream(System.getProperty("user.dir")
                    + "/src/main/java/properties/config.properties");
            properties.load(file);
            browserType = properties.getProperty("browser").toLowerCase().trim();
        }catch (IOException e)
        {
            //TODO use Logger instead as this is bad practice
            System.out.println(e.getMessage());
        }
        return browserType;
    }

    /**
     * Teardown the browser instance
     */
    public static void cleanUpDriver()
    {
        webDriver.get().quit();
        webDriver.remove();
    }

    /**
     * Get the OTP secret from the properties file
     * @return
     */
    private static String getOtpKeyString() throws IOException {
        String otpKeyString = null;
        Properties properties = new Properties();
        FileInputStream file = new FileInputStream(System.getProperty("user.dir")
                + "/src/main/java/properties/config.properties");
        properties.load(file);
        otpKeyString = properties.getProperty("otpKeyString").toLowerCase().trim();

        return otpKeyString;
    }

    /**
     * Computes the 2FA code using secret provided in the config properties file
     * @return
     */
    public static String generateTwoFactorCode() throws IOException {
        String secret = getOtpKeyString();
        Totp totp = new Totp(secret);
        return totp.now();
    }

    /**
     * Get the value of an item from the properties file by providing the key
     * @param key the name of the variable stored in the properties file
     * @return value
     */
    public static String getStringFromPropertiesFile(String key) throws IOException {
        String value = null;
        Properties properties = new Properties();
        FileInputStream file = new FileInputStream(System.getProperty("user.dir")
                + "/src/main/java/properties/config.properties");
        properties.load(file);
        value = properties.getProperty(key).trim();

        return  value;
    }
}
