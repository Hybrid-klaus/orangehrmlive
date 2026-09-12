package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for the OrangeHRM Dashboard (post-login landing page).
 */
public class DashboardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By dashboardHeader = By.xpath("//h6[text()='Dashboard']");
    private final By userDropdown = By.className("oxd-userdropdown-tab");
    private final By logoutLink = By.xpath("//a[text()='Logout']");
    private final By pimMenuItem = By.xpath("//span[text()='PIM']");
    private final By sideMenu = By.className("oxd-sidepanel");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public boolean isDashboardDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeader)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public PimPage navigateToPim() {
        wait.until(ExpectedConditions.elementToBeClickable(pimMenuItem)).click();
        return new PimPage(driver);
    }

    public LoginPage logout() {
        wait.until(ExpectedConditions.elementToBeClickable(userDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
        return new LoginPage(driver);
    }

    public boolean isSideMenuVisible() {
        return driver.findElement(sideMenu).isDisplayed();
    }
}
