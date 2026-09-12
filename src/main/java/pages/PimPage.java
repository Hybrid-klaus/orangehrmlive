package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Keys;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for the PIM (Personnel Information Management) module -
 * employee list and search functionality.
 */
public class PimPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By addButton = By.xpath("//button[normalize-space()='Add']");
    private final By employeeIdSearchInput = By.xpath("//label[text()='Employee Id']/following::input[1]");
    private final By searchButton = By.xpath("//button[normalize-space()='Search']");
    private final By employeeTableRows = By.xpath("//div[contains(@class,'oxd-table-card')]");
    private final By deleteIconInRow = By.xpath(".//i[contains(@class,'bi-trash')]");
    private final By confirmDeleteButton = By.xpath("//button[normalize-space()='Yes, Delete']");
    //private final By employeeNameLinkInRow = By.xpath(".//div[contains(@class,'oxd-table-cell')]//a");

    public PimPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public AddEmployeePage clickAddEmployee() {
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
        return new AddEmployeePage(driver);
    }

    /**
     * Searches by Employee ID and waits for the ID to actually appear
     * anywhere in the visible page text. This avoids depending on any
     * specific/nested CSS class for the "results" indicator, which has
     * proven unreliable across OrangeHRM demo UI updates.
     */
    public PimPage searchByEmployeeId(String employeeId) {
        WebElement idField = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeIdSearchInput));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", idField);
        idField.click();
        idField.sendKeys(Keys.CONTROL + "a");
        idField.sendKeys(Keys.DELETE);
        idField.sendKeys(employeeId);
        driver.findElement(searchButton).click();

        wait.until(d -> {
            String bodyText = d.findElement(By.tagName("body")).getText();
            return bodyText.contains("Record Found") || bodyText.contains("No Records Found");
        });
        return this;
    }

    public boolean isEmployeeRecordDisplayed() {
        List<WebElement> rows = driver.findElements(employeeTableRows);
        return !rows.isEmpty();
    }

    public EditEmployeePage openMatchingEmployeeByName(String expectedFirstName) {
        By nameCell = By.xpath("//div[contains(@class,'oxd-table-cell')]//div[contains(text(),'" + expectedFirstName + "')]");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(nameCell)).click();
        } catch (StaleElementReferenceException e) {
            wait.until(ExpectedConditions.elementToBeClickable(nameCell)).click();
        }
        return new EditEmployeePage(driver);
    }

    public void deleteFirstMatchingEmployee() {
        try {
            List<WebElement> rows = driver.findElements(employeeTableRows);
            if (rows.isEmpty()) {
                throw new RuntimeException("No employee record found to delete.");
            }
            rows.get(0).findElement(deleteIconInRow).click();
        } catch (StaleElementReferenceException e) {
            List<WebElement> rows = driver.findElements(employeeTableRows);
            if (rows.isEmpty()) {
                throw new RuntimeException("No employee record found to delete (after stale retry).");
            }
            rows.get(0).findElement(deleteIconInRow).click();
        }
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButton)).click();
    }
}
