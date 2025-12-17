package stepDefinition;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;

import java.time.Duration;

public class LoginStepDefinitions {
    WebDriver driver;
    WebDriverWait wait;

    // Parallel Testing
    public static final ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();

    public static WebDriver getDriver(){
        return threadDriver.get();
    }

    @Before
    public void iOpenBrowser() {
        // Write code here that turns the phrase above into concrete actions
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        threadDriver.set(new ChromeDriver(options));
        wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
    }

    @Given("I open Login page")
    public void iOpenLoginPage() {
        // Write code here that turns the phrase above into concrete actions
        getDriver().get("https://qa.koel.app/");
    }

    @When("I enter email {string}")
    public void iEnterEmail(String email) {
        // Write code here that turns the phrase above into concrete actions
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[type='email']"))).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[type='email']"))).sendKeys(email);
    }

    @And("I enter password {string}")
    public void iEnterPassword(String password) {
        // Write code here that turns the phrase above into concrete actions
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[type='password']"))).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[type='password']"))).sendKeys(password);
    }

    @And("I submit")
    public void iSubmit() {
        // Write code here that turns the phrase above into concrete actions
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[type='submit']"))).click();
    }

    @Then("I am logged in")
    public void iAmLoggedIn() {
        // Write code here that turns the phrase above into concrete actions
        Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img.avatar"))).isDisplayed());
    }

    @After
    public void tearDown() {
        threadDriver.get().close();
        threadDriver.remove();
    }

    public void closeBrowser(){
        driver.quit();
    }

    @Then("I am not logged in")
    public void iAmNotLoggedIn() {
        // Write code here that turns the phrase above into concrete actions
        Assert.assertEquals(getDriver().getCurrentUrl(), "https://qa.koel.app/");
    }
}
