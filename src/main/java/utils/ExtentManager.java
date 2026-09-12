package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Singleton wrapper around ExtentReports for generating the HTML test report.
 */
public class ExtentManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentReports getInstance() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("test-output/reports/TestReport_" + timestamp + ".html");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("QA Automation - Employee Lifecycle Report");
            sparkReporter.config().setReportName("OrangeHRM Employee Lifecycle - Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Application", "OrangeHRM Demo");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Executed By", "Syed Sadiq Mehdi");
        }
        return extent;
    }

    public static void startTest(String testName, String description) {
        ExtentTest extentTest = getInstance().createTest(testName, description);
        test.set(extentTest);
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void flush() {
        getInstance().flush();
    }
}
