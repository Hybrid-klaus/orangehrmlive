package listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.DriverManager;
import utils.ExtentManager;
import utils.ScreenshotUtil;

/**
 * TestNG listener that bridges test execution events to ExtentReports,
 * and captures a screenshot automatically whenever a test fails.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentManager.startTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().log(Status.PASS, "Test passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = DriverManager.getDriver();
        String screenshotPath = null;
        if (driver != null) {
            screenshotPath = ScreenshotUtil.capture(driver, result.getMethod().getMethodName());
        }

        ExtentManager.getTest().log(Status.FAIL, "Test failed: " + result.getThrowable());
        if (screenshotPath != null) {
            try {
                ExtentManager.getTest().fail("Screenshot on failure",
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } catch (Exception e) {
                ExtentManager.getTest().log(Status.WARNING, "Could not attach screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentManager.getTest().log(Status.SKIP, "Test skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flush();
    }
}
