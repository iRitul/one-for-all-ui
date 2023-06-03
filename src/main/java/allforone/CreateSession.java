package allforone;

import com.google.common.collect.ImmutableMap;
import drivers.AndroidDriverBuilder;
import drivers.IOSDriverBuilder;
import enums.PlatformName;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.qameta.allure.Step;
import logger.Log;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

/**
 * contains all the methods to create a new session and destroy the
 * session after the test(s) execution is over. Each test extends
 * this class.
 */
public class CreateSession {
    private static final Logger LOG = Logger.getLogger(CreateSession.class);
    private static int deviceIndex = 0;

    public WebDriver driver;
    protected File file = new File("");
    Properties configProp = new Properties();
    String OS;


    @BeforeSuite
    @Parameters({"os", "executionType", "threadCount"})
    public void setAllureEnvironment(@Optional("android") String os, @Optional("local") String executionType, int threadCount) {
        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("Browser", os)
                        .put("Execution Type", executionType)
                        .put("Thread Count", String.valueOf(threadCount))
                        .build());
    }

    @BeforeSuite
    public void invokeAppium() throws Exception {
        String OS = System.getProperty("os.name").toLowerCase();
        try {
            startAppiumServer(OS);
            LOG.info("Appium server started successfully");
        } catch (Exception e) {
            Log.logError(getClass().getName(), "startAppium", "Unable to start appium server");
            throw new Exception(e.getMessage());
        }
    }

	@AfterSuite(alwaysRun = true)
    public void stopAppium() throws Exception {
        try {
            stopAppiumServer(OS);
            LOG.info("Appium server stopped successfully");
        } catch (Exception e) {
            Log.logError(getClass().getName(), "stopAppium", "Unable to stop appium server");
            throw new Exception(e.getMessage());
        }
    }

    @Step("{os}")
    @Parameters({"os"})
    @BeforeClass(alwaysRun = true)
    public void createDriver(@Optional("android") String os) throws Exception {
        File propertiesFile = new File(file.getAbsoluteFile() + "//src//main//resources//log4j.properties");
        PropertyConfigurator.configure(propertiesFile.toString());
    }

    @Step("{os},{executionType}")
    @Parameters({"os", "executionType", "threadCount"})
    @BeforeClass(alwaysRun = true)
    public void initialiseDriver(@Optional("android") String os, @Optional("local") String executionType, int threadCount) throws Exception {
        int deviceIndex = getDeviceIndex(threadCount);
        LOG.info("Device Index: " + deviceIndex);
        int lengthOfClassName = this.getClass().getName().split("\\.").length;
        String className = this.getClass().getName().split("\\.")[lengthOfClassName - 1];
        LOG.info("Initialization Driver ..!");
        Thread.sleep(2000);
        if (os.equalsIgnoreCase(PlatformName.ANDROID.toString())) {
            String buildPath = chooseBuild(os);
            this.driver = new AndroidDriverBuilder().androidDriver(buildPath, className, executionType, deviceIndex);
            LOG.info("=============== Android driver created ===============");
        } else if (os.equalsIgnoreCase(PlatformName.IOS.toString())) {
            String buildPath = chooseBuild(os);
            LOG.info("=============== IOS driver created ===============");
            this.driver = new IOSDriverBuilder().iosDriver(buildPath, className, executionType, deviceIndex);
            Log.info("iOS driver created");
        }
    }

    private int getDeviceIndex(int threadCount) throws InterruptedException {
        if (deviceIndex >= threadCount) {
            deviceIndex = 0;
        }
        if (deviceIndex == 1) {
            Thread.sleep(2000);
        }
        return deviceIndex++;
    }

    @Step("{os}")
    @Parameters({"os"})
    @BeforeMethod
    public void relaunch(String os) {
        if (os.equalsIgnoreCase(PlatformName.IOS.toString())){
            ((IOSDriver<WebElement>) driver).launchApp();
        }else{
            ((AndroidDriver<WebElement>) driver).resetApp();
            ((AndroidDriver<WebElement>) driver).launchApp();
            LOG.info("=============== RE-LAUNCH APP..!  ===============");
        }
    }

    @Step("{os}")
    @Parameters({"os"})
    @AfterMethod
    public void closeApp(String os) {
        if (os.equalsIgnoreCase(PlatformName.IOS.toString())){
            ((IOSDriver<WebElement>) driver).closeApp();
        }else{
            ((AndroidDriver<WebElement>) driver).closeApp();
        }
        LOG.info("=============== APP CLOSED..!  ===============");
    }

    @AfterClass
    public void teardown() {
        if (this.driver != null) {
            this.driver.quit();
        }
        SoftAssertions assertions = new SoftAssertions();
        assertions.assertAll();
        LOG.info("=============== DRIVER QUITED ..! ===============");
    }

    private static AppiumDriverLocalService appiumService;
    private static AppiumServiceBuilder builder;

    public void startAppiumServer(String os) throws IOException, InterruptedException {
        if (os.contains("windows")) {
            builder = new AppiumServiceBuilder()
                    .usingAnyFreePort()
                    .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                    .withArgument(GeneralServerFlag.LOG_LEVEL, "error");
            appiumService = builder.build();
            appiumService.start();
        } else if (os.contains("mac os x")) {
            builder = new AppiumServiceBuilder()
                    .usingAnyFreePort()
                    .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                    .withArgument(GeneralServerFlag.LOG_LEVEL, "error");
        } else if (os.contains("linux")) {
            //Start the appium server
            System.out.println("ANDROID_HOME : ");
            System.getenv("ANDROID_HOME");
            //	System.out.println("PATH :" +System.getenv("PATH"));
            CommandLine command = new CommandLine("/bin/bash");
            command.addArgument("-c");
            command.addArgument("~/.linuxbrew/bin/node");
            command.addArgument("~/.linuxbrew/lib/node_modules/appium/lib/appium.js", true);
            DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            DefaultExecutor executor = new DefaultExecutor();
            executor.setExitValue(1);
            executor.execute(command, resultHandler);
            Thread.sleep(5000); //Wait for appium server to start

        } else {
            Log.info(os + "is not supported yet");
        }

        appiumService = builder.build();
        appiumService.start();
        Log.info("Appium started on " + appiumService.getUrl());
    }

    public void stopAppiumServer(String os) throws ExecuteException, IOException {
        if (appiumService != null) {
            appiumService.stop();
            Log.info("Appium server stopped");
        } else {
            Log.logError(getClass().getName(), getClass().getEnclosingMethod().getName(), "Appium server fail to stopped");
        }
    }

    public String chooseBuild(String invokeDriver) {
        String appPath = null;
        if (invokeDriver.equalsIgnoreCase(PlatformName.ANDROID.toString())) {
            appPath = configProp.getProperty("AndroidAppPath");
            return appPath;
        } else if (invokeDriver.equalsIgnoreCase(PlatformName.IOS.toString())) {
            appPath = configProp.getProperty("iOSAppPath");
            return appPath;
        }

        return appPath;
    }

    /**
     * Returns the instance of the webdriver.
     *
     * @return webdriver instance
     */
    public WebDriver getDriver() {
        return driver;
    }

}

