package listeners;

import allforone.CreateSession;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.BeforeSuite;
import java.io.ByteArrayInputStream;
import static helper.SlackImageNotification.deleteScreenFolder;
import static helper.SlackImageNotification.getScreenShots;
import static helper.SlackUtils.generateSlackReport;

public class AllureListener extends CreateSession implements ITestListener {

    private static final Logger LOG = Logger.getLogger(AllureListener.class);
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW


    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    // Text attachments for Allure
    @Attachment(value = "Page Screenshot", type = "image/png")
    public byte[] saveScreenshotPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    // Text attachments for Allure
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info(GREEN + "------------------- " + result.getMethod().getMethodName() +
                " Test Started-------------------------------------------");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info(result.getMethod().getMethodName() + "=> SUCCESS");
        LOG.info(GREEN + "===================================================================");

    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.error(RED + getTestMethodName(result) + "-> FAILED" + GREEN);
        LOG.info(GREEN + "===================================================================");
        Object AllureListener = result.getInstance();
        WebDriver driver = ((CreateSession) AllureListener).getDriver();
        // Allure ScreenShotRobot
        if (driver != null) {
            LOG.info("Screenshot captured for test case:" + getTestMethodName(result));
//            saveScreenshotPNG(driver);
            // Save a log on allure.
        }
        saveTextLog(result.getThrowable().getMessage());

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.error(YELLOW + getTestMethodName(result) + "=> SKIPPED" + GREEN);
        LOG.info(GREEN + "===================================================================");
        // Allure ScreenShotRobot
        Object AllureListener = result.getInstance();
        WebDriver driver = ((CreateSession) AllureListener).getDriver();
        if (driver != null) {
            LOG.info("Screenshot captured for test case:" + getTestMethodName(result));
            Allure.addAttachment("Any text", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
            saveScreenshotPNG(driver);
        }
        saveTextLog(result.getThrowable().getMessage());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        LOG.info(result.getInstanceName() + " - " + result.getMethod().getMethodName()
                + " Test Failed but within Success %");
    }

    @BeforeSuite
    @Override
    public void onStart(ITestContext context) {
        deleteScreenFolder("ShowSelection");
        deleteScreenFolder("Screenshots");
        context.setAttribute("WebDriver", this.driver);
        LOG.info(GREEN + "<--------------------------- " + context.getName() +
                " Execution Started ---------------------------------");
    }


    @Override
    public void onFinish(ITestContext context) {
        int pass = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skip = context.getSkippedTests().size();
        LOG.info(GREEN + "Passed: " + pass + RED + " Failed: " + failed + YELLOW + " Skipped: " + skip + GREEN);
        int total = pass + failed + skip;
        int successPercent = 0;
        if (pass > 0) {
            successPercent = (int) Math.ceil((double) (pass * 100) / total);
            LOG.info("Success TestCase Percent: " + (successPercent) + "%");
        } else {
            LOG.error(RED + "All test case failed" + GREEN);
        }
        generateSlackReport(pass, failed, skip, successPercent);
        getScreenShots();
        LOG.info(GREEN + "<--------------------------- " + context.getName() +
                " Execution Finished ---------------------------------");
    }
}
