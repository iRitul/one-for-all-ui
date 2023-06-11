package com.oneforall.drivers;

import com.oneforall.config.DeviceConfig;
import com.oneforall.config.IOSDeviceModel;
import com.oneforall.enums.ExecutionType;
import com.oneforall.utilities.PropertiesUtils;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * IOSDriverBuilder is for initializing "iOS driver" for local as well cloud setup.
 */
public class IOSDriverBuilder {
    private static final Logger LOG = Logger.getLogger(IOSDriverBuilder.class);
    IOSDriver driver;
    PropertiesUtils propertiesUtils = new PropertiesUtils();
    HashMap<String, Object> browserstackOptions;
    IOSDeviceModel device;
    IOSDeviceModel appConfig;
    DeviceConfig def = new DeviceConfig();

    /**
     * this method creates the android driver
     *
     * @param buildPath - path to pick the location of the app
     * @throws MalformedURLException Thrown to indicate that a malformed URL has occurred.
     */
    public synchronized IOSDriver iosDriver(String buildPath, String className, String executionType, int deviceIndex) throws IOException {
        device = def.readIOSDeviceConfig().getIOSDeviceByName(executionType, deviceIndex);
        appConfig = def.readIOSDeviceConfig().getIOSDeviceByName("appSpecification");
        // W3C
        browserstackOptions = new HashMap<String, Object>();
        String useName = propertiesUtils.getBrowserStackProperties("user");
        String accesskey = propertiesUtils.getBrowserStackProperties("accesskey");
        String server = propertiesUtils.getBrowserStackProperties("server");

        if (ExecutionType.BROWSERSTACK.name().equalsIgnoreCase(executionType)) {
            LOG.info("Setting up browserstack capabilities. ");
            DesiredCapabilities capabilities = getBrowserstackCapabilities();
            String bsUrl = "https://" + useName + ":" + accesskey + "" + server + "";
            LOG.info("Browserstack ios Driver initialization with bsURL: " + bsUrl);
            driver = new IOSDriver(new URL(bsUrl), capabilities);
            LOG.info("BrowserStack : " + capabilities);
            LOG.info("bsUrl : " + bsUrl);
            int retry = 2;
            while (retry > 0 && driver == null) {
                try {
                    driver = new IOSDriver(new URL(bsUrl), capabilities);
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
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("platformName", device.getPlatformName());
            capabilities.setCapability("platformVersion", device.getPlatformVersion());
            capabilities.setCapability("appiumVersion", appConfig.getAppiumVersion());
            capabilities.setCapability("name", className);
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, device.getDeviceName());
            capabilities.setCapability("app", appConfig.getAppPath());
            capabilities.setCapability("automationName", appConfig.getAutomationName());
            capabilities.setCapability(MobileCapabilityType.UDID, appConfig.getUDID());
            capabilities.setCapability("autoAcceptAlerts", true);
            driver = new IOSDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        }
        LOG.info("IOS driver has been created for the " + executionType + " devices");
        return driver;
    }

    private DesiredCapabilities getBrowserstackCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, device.getDeviceName());
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, device.getPlatformVersion());
        capabilities.setCapability(MobileCapabilityType.APP, device.getApp());
        capabilities.setCapability(MobileCapabilityType.NO_RESET, propertiesUtils.getBrowserStackProperties("browserstack.noReset"));
        browserstackOptions.put("deviceLogs", propertiesUtils.getBrowserStackProperties("browserstack.deviceLogs"));
        browserstackOptions.put("video", propertiesUtils.getBrowserStackProperties("browserstack.video"));
        browserstackOptions.put("debug", propertiesUtils.getBrowserStackProperties("browserstack.debug"));
        browserstackOptions.put("networkLogs", propertiesUtils.getBrowserStackProperties("browserstack.networkLogs"));
        browserstackOptions.put("appiumLogs", propertiesUtils.getBrowserStackProperties("browserstack.appiumLogs"));
        browserstackOptions.put("networkProfile", "4g-lte-advanced-good");
        capabilities.setCapability("bstack:options", browserstackOptions);
        return capabilities;
    }
}
