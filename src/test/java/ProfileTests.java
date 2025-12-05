import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

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
}