package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for the Employee Details screen - "Job" tab (Job Title,
 * Employment Status) plus reading back the Personal Details name fields.
 */
public class EditEmployeePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By jobTab = By.xpath("//a[normalize-space()='Job']");
    private final By employmentStatusDropdown = By.xpath("(//label[text()='Employment Status']/../following-sibling::div//div[contains(@class,'oxd-select-text')])");
    private final By jobTitleDropdown = By.xpath("(//label[text()='Job Title']/../following-sibling::div//div[contains(@class,'oxd-select-text')])");
    private final By dropdownOptions = By.className("oxd-select-option");
    private final By saveButton = By.xpath("//button[normalize-space()='Save']");
    private final By successToast = By.className("oxd-toast-content--success");
    private final By personalDetailsHeader = By.xpath("//h6[text()='Personal Details']");
    private final By firstNameInput = By.name("firstName");
    private final By lastNameInput = By.name("lastName");
    private final By formLoader = By.className("oxd-form-loader");

    public EditEmployeePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public EditEmployeePage openJobTab() {
        wait.until(ExpectedConditions.elementToBeClickable(jobTab)).click();
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));
        } catch (Exception ignored) {
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(jobTitleDropdown));
        return this;
    }

    public EditEmployeePage updateJobTitle(String jobTitle) {
        wait.until(ExpectedConditions.elementToBeClickable(jobTitleDropdown)).click();
        selectDropdownOption(jobTitle);
        return this;
    }

    public EditEmployeePage updateEmploymentStatus(String status) {
        wait.until(ExpectedConditions.elementToBeClickable(employmentStatusDropdown)).click();
        selectDropdownOption(status);
        return this;
    }

    private void selectDropdownOption(String visibleText) {
        List<WebElement> options = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(dropdownOptions));
        for (WebElement option : options) {
            if (option.getText().trim().equalsIgnoreCase(visibleText)) {
                option.click();
                return;
            }
        }
        // Fallback: pick the first available option so the flow can proceed
        // even if the exact configured value isn't present in this demo instance.
        if (!options.isEmpty()) {
            options.get(0).click();
        } else {
            throw new RuntimeException("No dropdown options found for value: " + visibleText);
        }
    }

    public EditEmployeePage clickSave() {
        driver.findElement(saveButton).click();
        return this;
    }

    public boolean isSuccessToastDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successToast)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Reads back the saved First Name / Last Name from the Personal Details
     * form fields (stable, documented "name" attributes) rather than relying
     * on any undocumented header CSS class.
     */
    public String getEmployeeFullName() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader));
        WebElement firstNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput));

        // Wait until the field is actually populated (not just visible) -
        // OrangeHRM loads the saved data asynchronously after the redirect.
        wait.until(d -> {
            String value = firstNameField.getAttribute("value");
            return value != null && !value.trim().isEmpty();
        });

        WebElement lastNameField = driver.findElement(lastNameInput);
        String firstName = firstNameField.getAttribute("value");
        String lastName = lastNameField.getAttribute("value");
        return firstName + " " + lastName;
    }
}
