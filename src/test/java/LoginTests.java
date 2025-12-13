import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;


public class LoginTests extends BaseTest {

    @Test
    public void loginValidEmailValidPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        HomePage homePage = new HomePage(getDriver());

        loginPage.provideEmail("carlitos@testpro.io");
        loginPage.providePassword("vjNWk4Hn");
        loginPage.clickSubmit();

        Assert.assertTrue(homePage.getUserAvatar().isDisplayed());
    }

    @Test
    public void loginInvalidEmailInvalidPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        HomePage homePage = new HomePage(getDriver());

        loginPage.provideEmail("demotest@testpro.io");
        loginPage.providePassword("incorrectpassword");
        loginPage.clickSubmit();

        Assert.assertEquals(getDriver().getCurrentUrl(), url); //https://qa.koel.app/
    }
}