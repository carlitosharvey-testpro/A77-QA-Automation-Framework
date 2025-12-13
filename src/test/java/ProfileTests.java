import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pagefactory.ProfilePage;
import pages.HomePage;
import pages.LoginPage;

public class ProfileTests extends BaseTest {
    @Test
    public void changeProfileName () throws InterruptedException {

        provideEmail("demo@testpro.io");
        providePassword("te$t$tudent");
        clickSubmit();

        Thread.sleep(2000);
        clickAvatarIcon();

        String randomName = generateRandomName();

        provideCurrentPassword("te$t$tudent");
        provideProfileName(randomName);
        clickSaveButton();

//        Replaced thread.sleep with an explicit wait for the 'Profile updated' message
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.success.show")));

        WebElement actualProfileName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.view-profile>span")));
        Assert.assertEquals(actualProfileName.getText(), randomName);
    }

    @Test
    public void changeProfilePageTheme() throws InterruptedException{
        LoginPage loginPage = new LoginPage(getDriver());
        HomePage homePage = new HomePage(getDriver());
        ProfilePage profilePage = new ProfilePage(getDriver());

        loginPage.provideEmail("carlitos@testpro.io");
        loginPage.providePassword("vjNWk4Hn");
        loginPage.clickSubmit();

        homePage.getUserAvatar().click();

        profilePage.chooseVioletTheme();

        Assert.assertTrue(profilePage.isVioletThemeSelected());

    }
}