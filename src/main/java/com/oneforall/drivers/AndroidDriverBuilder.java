package com.oneforall.drivers;

import com.oneforall.config.AndroidDeviceModel;
import com.oneforall.config.DeviceConfig;
import com.oneforall.enums.ExecutionType;
import com.oneforall.helper.BrowserstackService;
import com.oneforall.utilities.PropertiesUtils;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * AndroidDriverBuilder is for initializing "android driver" for local as well cloud setup..
 */
public class AndroidDriverBuilder {
    private static final Logger LOG = Logger.getLogger(AndroidDriverBuilder.class);
    public AndroidDriver<AndroidElement> driver;
    PropertiesUtils propertiesUtils = new PropertiesUtils();
    HashMap<String, Object> browserstackOptions;
    AndroidDeviceModel device;
    AndroidDeviceModel appConfig;
    DeviceConfig def = new DeviceConfig();

    /**
     * this method creates the android driver
     *
     * @param buildPath - path to pick the location of the app
     * @throws MalformedURLException Thrown to indicate that a malformed URL has occurred.
     */
    public synchronized AndroidDriver androidDriver(String buildPath, String className, String executionType, int deviceIndex) throws IOException {
        LOG.info("Initialization Android Driver..!");
        device = def.readAndroidDeviceConfig().getAndroidDeviceByName(executionType, deviceIndex);
        appConfig = def.readAndroidDeviceConfig().getAndroidDeviceByName("appSpecification");
        LOG.info("Build path : " + appConfig.getAppPath());
        LOG.info(System.getProperty("user.dir") + appConfig.getAppPath());
        // W3C
        browserstackOptions = new HashMap<String, Object>();
        String userName = propertiesUtils.getBrowserStackProperties("user");
        String accesskey = propertiesUtils.getBrowserStackProperties("accesskey");
        String server = propertiesUtils.getBrowserStackProperties("server");

        if (ExecutionType.BROWSERSTACK.name().equalsIgnoreCase(executionType)) {
            String bsUrl = "https://" + userName + ":" + accesskey + "" + server + "";
            DesiredCapabilities capabilities = getBrowserstackDeviceCapabilities(className);
            driver = new AndroidDriver(new URL(bsUrl), capabilities);
            driver.setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, 100);
            LOG.info("BrowserStack : " + capabilities);
            LOG.info("bsUrl : " + bsUrl);
            int retry = 2;
            while (retry > 0 && driver == null) {
                try {
                    driver = new AndroidDriver(new URL(bsUrl), capabilities);
                } catch (Exception e) {
                    retry--;
                    LOG.error("Error while creating driver");
                    LOG.error(e.getMessage());
                }
            }
            if (driver == null) {
                throw new AuthenticationException("Browserstack driver not initialized");
            }
        } else {
            LOG.info("Initialization Local Driver..!");
            DesiredCapabilities capabilities = getLocalDeviceCapabilities();
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
            driver.setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, 1000);
        }
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        LOG.info("Android driver has been created for the " + executionType + " devices");
        return driver;
    }

    private DesiredCapabilities getBrowserstackDeviceCapabilities(String className) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String buildNumber = System.getenv("BUILD_NUMBER");
        Map<String, String> appStoreCreds = new HashMap<>();
        appStoreCreds.put("username", propertiesUtils.getBrowserStackProperties("appStoreEmail"));
        appStoreCreds.put("password", propertiesUtils.getBrowserStackProperties("appStorePassword"));
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, device.getDeviceName());
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, device.getPlatformVersion());
        LOG.info("BS app id :" + new BrowserstackService().getBrowserstackAppId());
        capabilities.setCapability(MobileCapabilityType.APP, new BrowserstackService().getBrowserstackAppId());
        capabilities.setCapability(MobileCapabilityType.NO_RESET, propertiesUtils.getBrowserStackProperties("noReset"));
        capabilities.setCapability("autoGrantPermissions", propertiesUtils.getBrowserStackProperties("autoGrantPermissions"));
        browserstackOptions.put("deviceLogs", propertiesUtils.getBrowserStackProperties("browserstack.deviceLogs"));
        browserstackOptions.put("video", propertiesUtils.getBrowserStackProperties("browserstack.video"));
        browserstackOptions.put("debug", propertiesUtils.getBrowserStackProperties("browserstack.debug"));
        browserstackOptions.put("networkLogs", propertiesUtils.getBrowserStackProperties("browserstack.networkLogs"));
        browserstackOptions.put("appiumLogs", propertiesUtils.getBrowserStackProperties("browserstack.appiumLogs"));
        browserstackOptions.put("sessionName", className);
        browserstackOptions.put("buildName", "JENKINS_JOB_" + buildNumber);
        browserstackOptions.put("networkProfile", "4g-lte-advanced-good");
        if (className.equalsIgnoreCase("PaymentTest")) {
            capabilities.setCapability("browserstack.appStoreConfiguration", appStoreCreds);
        }
        capabilities.setCapability("autoAcceptAlerts", propertiesUtils.getBrowserStackProperties("browserstack.autoAcceptAlerts"));
        capabilities.setCapability("autoGrantPermissions", propertiesUtils.getBrowserStackProperties("browserstack.autoGrantPermissions"));
        capabilities.setCapability("bstack:options", browserstackOptions);
        return capabilities;
    }

    private DesiredCapabilities getLocalDeviceCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.UDID, device.getDeviceName());
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, device.getPlatformName());
        capabilities.setCapability("appPackage", appConfig.getAppPackage());
        capabilities.setCapability("appWaitActivity", appConfig.getAppWaitActivity());
        capabilities.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir") + appConfig.getAppPath());
        capabilities.setCapability(MobileCapabilityType.FULL_RESET, appConfig.getFullReset());
        capabilities.setCapability("automationName", appConfig.getAutomationName());
        return capabilities;
    }
}
