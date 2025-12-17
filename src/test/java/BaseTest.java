import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

public class BaseTest {

    public static WebDriver driver = null;
    public static String url = null;
    public static WebDriverWait wait = null;
    public static Wait<WebDriver> fluentWait;

    public  Actions actions = null;

    public static final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();

    public static WebDriver getDriver(){
        return threadDriver.get();
    }

    @DataProvider(name="IncorrectLoginData")
    public Object[][] getDataFromDataProviders() {

        return new Object[][] {
                {"invalid@testpro.io", "invalidPass"},
                {"demo@testpro.io", ""},
                {"", ""}
        };
    }

    @BeforeSuite
    static void setupClass() {
        //WebDriverManager.chromedriver().setup();
        //WebDriverManager.firefoxdriver().setup();
        //WebDriverManager.safaridriver().setup();
        //WebDriverManager.edgedriver().setup();
    }

    @BeforeMethod
    @Parameters({"BaseURL"})
    public void setupBrowser(String BaseURL) throws MalformedURLException {
        threadDriver.set(pickBrowser(System.getProperty("browser")));

        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        getDriver().manage().window().maximize();

        wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        fluentWait = new FluentWait<WebDriver>(getDriver()).withTimeout(Duration.ofSeconds(5)).pollingEvery(Duration.ofMillis(200));

        actions = new Actions(getDriver());

        url = BaseURL;
        navigateToPage();
    }

    // Single Test Execution
    public void launchBrowser(String BaseURL) throws MalformedURLException {
        // Added ChromeOptions argument below to fix websocket error
        //ChromeOptions options = new ChromeOptions();
        //options.addArguments("--remote-allow-origins=*");
        //EdgeOptions options = new EdgeOptions();
        //options.addArguments("--remote-allow-origins=*");

        //driver = new ChromeDriver(options);
        //driver = new FirefoxDriver();
        //driver = new SafariDriver();
        //driver = new EdgeDriver(options);

        driver = pickBrowser(System.getProperty("browser"));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        fluentWait = new FluentWait<WebDriver>(getDriver()).withTimeout(Duration.ofSeconds(5)).pollingEvery(Duration.ofMillis(200));

        actions = new Actions(driver);

        url = BaseURL;
        navigateToPage();
    }
    @AfterMethod
    public void tearDown(){
        threadDriver.get().close();
        threadDriver.remove();
    }

    public void closeBrowser() {
        driver.quit();
    }

    public void navigateToPage() {
        getDriver().get(url);
    }

    public WebDriver pickBrowser(String browser) throws MalformedURLException {

        DesiredCapabilities caps = new DesiredCapabilities();
        String gridUrl = "http://192.168.100.226:4444";

        switch(browser){
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return driver = new FirefoxDriver();
            case "safari":
                WebDriverManager.safaridriver().setup();
                return driver = new SafariDriver();
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--remote-allow-origins=*");
                return driver = new EdgeDriver();
            // Grid Capable Browsers
            case "grid-firefox":
                caps.setCapability("browserName", "firefox");
                return driver = new RemoteWebDriver(URI.create(gridUrl).toURL(), caps);
            case "grid-safari":
                caps.setCapability("browserName", "safari");
                return driver = new RemoteWebDriver(URI.create(gridUrl).toURL(), caps);
            case "grid-chrome":
                caps.setCapability("browserName", "chrome");
                return driver = new RemoteWebDriver(URI.create(gridUrl).toURL(), caps);
            case "grid-edge":
                caps.setCapability("browserName", "MicrosoftEdge");
                return driver = new RemoteWebDriver(URI.create(gridUrl).toURL(), caps);
            // Cloud Execution
            case "cloud-edge":
                return lambdaTestEdge();
            case "cloud-safari":
                return lambdaTestSafari();
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                return driver = new ChromeDriver(chromeOptions);
        }
    }

    public WebDriver lambdaTestEdge() throws MalformedURLException {
        String hubURL = "https://hub.lambdatest.com/wd/hub";

        EdgeOptions browserOptions = new EdgeOptions();
        browserOptions.setPlatformName("Windows 10");
        browserOptions.setBrowserVersion("dev");
        HashMap<String, Object> ltOptions = new HashMap<String, Object>();
        ltOptions.put("username", "carlitostestpro");
        ltOptions.put("accessKey", "LT_KnKGZEFkqN0iV2xtebmkp7CZcb5XENrzCNvgOk2WK2dA5FV");
        ltOptions.put("build", "Selenium 4");
        ltOptions.put("project", "EdgeTest01");
        ltOptions.put("name", this.getClass().getName());
        ltOptions.put("w3c", true);
        ltOptions.put("plugin", "java-testNG");
        browserOptions.setCapability("LT:Options", ltOptions);

        return new RemoteWebDriver(URI.create(hubURL).toURL(), browserOptions);
    }

    public WebDriver lambdaTestSafari() throws MalformedURLException {
        String hubURL = "https://hub.lambdatest.com/wd/hub";

        SafariOptions browserOptions = new SafariOptions();
        browserOptions.setPlatformName("MacOS Tahoe");
        browserOptions.setBrowserVersion("26");
        HashMap<String, Object> ltOptions = new HashMap<String, Object>();
        ltOptions.put("username", "carlitostestpro");
        ltOptions.put("accessKey", "LT_KnKGZEFkqN0iV2xtebmkp7CZcb5XENrzCNvgOk2WK2dA5FV");
        ltOptions.put("project", "Untitled");
        ltOptions.put("name", this.getClass().getName());
        ltOptions.put("w3c", true);
        ltOptions.put("plugin", "java-testNG");
        browserOptions.setCapability("LT:Options", ltOptions);

        return new RemoteWebDriver(URI.create(hubURL).toURL(), browserOptions);
    }

    public void provideEmail(String email) {
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='email']")));
        emailField.clear();
        emailField.sendKeys(email);
    }
    public void providePassword(String password) {
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='password']")));
        passwordField.clear();
        passwordField.sendKeys(password);
    }
    public void clickSubmit() {
        WebElement submit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[type='submit']")));
        submit.click();
    }
    public void clickSaveButton() {
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn-submit")));
        saveButton.click();
    }
    public void provideProfileName(String randomName) {
        WebElement profileName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name='name']")));
        profileName.clear();
        profileName.sendKeys(randomName);
    }
    public void provideCurrentPassword(String password) {
        WebElement currentPassword = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name='current_password']")));
        currentPassword.clear();
        currentPassword.sendKeys(password);
    }
    public String generateRandomName() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    public void clickAvatarIcon() {
        WebElement avatarIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img.avatar")));
        avatarIcon.click();
    }
}