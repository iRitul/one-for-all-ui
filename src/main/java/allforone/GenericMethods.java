package allforone;

import config.DeviceConfig;
import helper.SlackImageNotification;
import io.appium.java_client.*;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.connection.ConnectionState;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import logger.Log;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GenericMethods {
    private static final Logger LOG = Logger.getLogger(GenericMethods.class);

    WebDriver driver = null;

    // common timeout for all tests can be set here
    public final int timeOut = 15;
    WebDriverWait wait;

    public GenericMethods(WebDriver driver) {
        this.driver = driver;
    }

    private static String channel = "slackChannelId";

    public void postScreenshotOnSlackChannel(String imageName) {
        if (driver != null) {
            File scr = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File("./ShowSelection/" + imageName + ".png"); //Directory where Screenshot get saved.
            try {
                FileUtils.copyFile(scr, dest);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        SlackImageNotification.postScreenShots("./ShowSelection/" + imageName + ".png", imageName, channel);
    }

    public Boolean isElementPresent(By targetElement) {
        Boolean isPresent = driver.findElements(targetElement).size() > 0;
        return isPresent;
    }

    /**
     * method to go back by Android Native back click
     */
    public void back() {
        ((AndroidDriver) driver).pressKey(new KeyEvent().withKey(AndroidKey.BACK));
    }

    /**
     * method to wait for an element to be visible
     *
     * @param targetElement element to be visible
     * @return true if element is visible else throws TimeoutException
     */


    public WebElement waitForVisibility(By targetElement) {
        Allure.step("wait For Visibility => " + targetElement);
        try {
            wait = new WebDriverWait(driver, timeOut);
            LOG.info("wait For Visibility :" + targetElement);
            return wait.pollingEvery(50, TimeUnit.MILLISECONDS).until(ExpectedConditions.visibilityOf(driver.findElement(targetElement)));
        } catch (TimeoutException e) {
            LOG.error("Element is not visible: " + targetElement);
            Allure.addAttachment("Element is not visible: " + targetElement, new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
            throw e;
        }
    }

    @Step("Scrolling to text : {text}")
    public void scrollToText(String text) {
        try {
            driver.findElement(new MobileBy.ByAndroidUIAutomator("new UiScrollable(new UiSelector()." +
                    "scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"" + text + "\").instance(0))"));
        } catch (Exception e) {
            Assert.fail(text + " could not be located on the page");
        }

    }

    @Step("Scrolling to text : {text}")
    public void scrollToId(String resourceId) {
        try {
            driver.findElement(new MobileBy.ByAndroidUIAutomator("new UiScrollable(new UiSelector()." +
                    "scrollable(true).instance(0)).scrollIntoView(new UiSelector().xpath(\"" + resourceId + "\").instance(0))"));
        } catch (Exception e) {
            Assert.fail(resourceId + " could not be located on the page");
        }

    }

    /**
     * method to wait for an element until it is invisible
     *
     * @param targetElement element to be invisible
     * @return true if element gets invisible else throws TimeoutException
     */
    public boolean waitForInvisibility(By targetElement) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeOut);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(targetElement));
            return true;
        } catch (TimeoutException e) {
            System.out.println("Element is still visible: " + targetElement);
            System.out.println(e.getMessage());
            throw e;

        }
    }

    /**
     * method to tap on the screen on provided coordinates
     *
     * @param xPosition x coordinate to be tapped
     * @param yPosition y coordinate to be tapped
     */
    public void tap(double xPosition, double yPosition) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, Double> tapObject = new HashMap<String, Double>();
        tapObject.put("startX", xPosition);
        tapObject.put("startY", yPosition);
        js.executeScript("mobile: tap", tapObject);
    }


    /**
     * method to find an element
     *
     * @param locator element to be found
     * @return WebElement if found else throws NoSuchElementException
     */

    @Step("findElement {locator}")
    public WebElement findElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element;
        } catch (NoSuchElementException e) {
            Allure.addAttachment("Element NOT FOUND - > " + locator, new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
            LOG.error("Element NOT FOUND - > " + locator);
            throw e;
        }
    }

    /**
     * method to find all the elements of specific locator
     *
     * @param locator element to be found
     * @return return the list of elements if found else throws NoSuchElementException
     */
    public List<WebElement> findElements(By locator) {
        Allure.step("findElements " + locator);
        LOG.info("findElements " + locator);
        try {
            List<WebElement> element = driver.findElements(locator);
            return element;
        } catch (NoSuchElementException e) {
            Allure.addAttachment("Elements NOT FOUND - > " + locator, new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
            Assert.fail("Elements NOT FOUND - > " + locator);
            LOG.error("Elements NOT FOUND - > " + locator);
            throw e;
        }
    }

    /**
     * method to get message test of alert
     *
     * @return message text which is displayed
     */
    public String getAlertText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            return alertText;
        } catch (NoAlertPresentException e) {
            throw e;
        }
    }

    /**
     * method to verify if alert is present
     *
     * @return returns true if alert is present else false
     */
    public boolean isAlertPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeOut);
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            throw e;
        }
    }

    /**
     * method to Accept Alert if alert is present
     */

    public void acceptAlert() {
        WebDriverWait wait = new WebDriverWait(driver, timeOut);
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }

    /**
     * method to Dismiss Alert if alert is present
     */

    public void dismissAlert() {
        WebDriverWait wait = new WebDriverWait(driver, timeOut);
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().dismiss();
    }

    /**
     * method to get network settings
     */
    public void getNetworkConnection() {
        System.out.println(((AndroidDriver) driver).getConnection());
    }


    /**
     * method to set network settings
     *
     * @param airplaneMode pass true to activate airplane mode else false
     * @param wifi         pass true to activate wifi mode else false
     * @param data         pass true to activate data mode else false
     */
    public void setNetworkConnection(boolean airplaneMode, boolean wifi, boolean data) {

        long mode = 1L;

        if (wifi) {
            mode = 2L;
        } else if (data) {
            mode = 4L;
        }

        ConnectionState connectionState = new ConnectionState(mode);
        ((AndroidDriver) driver).setConnection(connectionState);
        System.out.println("Your current connection settings are :" + ((AndroidDriver) driver).getConnection());
    }


    /**
     * method to get all the context handles at particular screen
     */
    public void getContext() {
        ((AppiumDriver) driver).getContextHandles();
    }

    /**
     * method to set the context to required view.
     *
     * @param context view to be set
     */
    public void setContext(String context) {

        Set<String> contextNames = ((AppiumDriver) driver).getContextHandles();

        if (contextNames.contains(context)) {
            ((AppiumDriver) driver).context(context);
            Log.info("Context changed successfully");
        } else {
            Log.info(context + "not found on this page");
        }

        Log.info("Current context" + ((AppiumDriver) driver).getContext());
    }

    /**
     * method to long press on specific element by passing locator
     *
     * @param locator element to be long pressed
     */
    public void longPress(By locator) {
        try {
            WebElement element = driver.findElement(locator);

            TouchAction touch = new TouchAction((MobileDriver) driver);
            LongPressOptions longPressOptions = new LongPressOptions();
            longPressOptions.withElement(ElementOption.element(element));
            touch.longPress(longPressOptions).release().perform();
            Log.info("Long press successful on element: " + element);
        } catch (NoSuchElementException e) {
            Log.logError(this.getClass().getName(), "findElement", "Element not found" + locator);
            throw e;
        }

    }

    /**
     * method to long press on specific x,y coordinates
     *
     * @param x x offset
     * @param y y offset
     */
    public void longPress(int x, int y) {
        TouchAction touch = new TouchAction((MobileDriver) driver);
        PointOption pointOption = new PointOption();
        pointOption.withCoordinates(x, y);
        touch.longPress(pointOption).release().perform();
        Log.info("Long press successful on coordinates: " + "( " + x + "," + y + " )");

    }

    /**
     * method to long press on element with absolute coordinates.
     *
     * @param locator element to be long pressed
     * @param x       x offset
     * @param y       y offset
     */
    public void longPress(By locator, int x, int y) {
        try {
            WebElement element = driver.findElement(locator);
            TouchAction touch = new TouchAction((MobileDriver) driver);
            LongPressOptions longPressOptions = new LongPressOptions();
            longPressOptions.withPosition(new PointOption().withCoordinates(x, y)).withElement(ElementOption.element(element));
            touch.longPress(longPressOptions).release().perform();
            Log.info("Long press successful on element: " + element + "on coordinates" + "( " + x + "," + y + " )");
        } catch (NoSuchElementException e) {
            Log.logError(this.getClass().getName(), "findElement", "Element not found" + locator);
            throw e;
        }

    }

    /**
     * method to swipe on the screen on provided coordinates
     *
     * @param startX   - start X coordinate to be tapped
     * @param endX     - end X coordinate to be tapped
     * @param startY   - start y coordinate to be tapped
     * @param endY     - end Y coordinate to be tapped
     * @param duration duration to be tapped
     */

    public void swipe(double startX, double startY, double endX, double endY, double duration) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, Double> swipeObject = new HashMap<String, Double>();
        // swipeObject.put("touchCount", 1.0);
        swipeObject.put("startX", startX);
        swipeObject.put("startY", startY);
        swipeObject.put("endX", endX);
        swipeObject.put("endY", endY);
        swipeObject.put("duration", duration);
        js.executeScript("mobile: swipe", swipeObject);
    }


    protected static String UiScrollable(String uiSelector) {
        return "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().text(\"" + uiSelector + "\"))";
    }


    /**
     * method to open notifications on Android
     */

    public void openNotifications() {
        ((AndroidDriver) driver).openNotifications();
    }

    /**
     * method to launchApp
     */

    public void launchApp() {
        ((AppiumDriver) driver).launchApp();
    }

    public void terminateApp() {
        try {
            ((AppiumDriver) driver).closeApp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void reactivateApp() {
        try {
            ((AppiumDriver) driver).activateApp(new DeviceConfig().readAndroidDeviceConfig().getAndroidDeviceByName("appSpecification").getAppPackage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method to click on Element By Name
     *
     * @param elementByName - String element name to be clicked
     */

    public void click(String elementByName) {
        ((AppiumDriver) driver).findElementByName(elementByName).click();
    }

    /**
     * method to scroll down on screen from java-client 6
     *
     * @param swipeTimes       number of times swipe operation should work
     * @param durationForSwipe time duration of a swipe operation
     */
    public void scrollDown(int swipeTimes, int durationForSwipe) {
        Dimension dimension = driver.manage().window().getSize();

        for (int i = 0; i <= swipeTimes; i++) {
            int start = (int) (dimension.getHeight() * 0.5);
            int end = (int) (dimension.getHeight() * 0.3);
            int x = (int) (dimension.getWidth() * .5);


            new TouchAction((AppiumDriver) driver).press(PointOption.point(x, start)).moveTo(PointOption.point(x, end))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(durationForSwipe)))
                    .release().perform();
        }
    }


    /**
     * method to scroll up on screen from java-client 6
     *
     * @param swipeTimes       number of times swipe operation should work
     * @param durationForSwipe time duration of a swipe operation
     */
    public void scrollUp(int swipeTimes, int durationForSwipe) {
        Dimension dimension = driver.manage().window().getSize();

        for (int i = 0; i <= swipeTimes; i++) {
            int start = (int) (dimension.getHeight() * 0.3);
            int end = (int) (dimension.getHeight() * 0.5);
            int x = (int) (dimension.getWidth() * .5);


            new TouchAction((AppiumDriver) driver).press(PointOption.point(x, start)).moveTo(PointOption.point(x, end))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(durationForSwipe)))
                    .release().perform();
        }
    }

    public WebElement waitForClickabilityOf(By elementId) {
        return waitForClickabilityOf(elementId, timeOut);
    }

    public WebElement waitForClickabilityOf(By elementId, int numberOfSecondsToWait) {
        Allure.step("Waiting for ClickAbility ->" + elementId + "|" + "waitTill ->" + numberOfSecondsToWait);
        LOG.info("Waiting for ClickAbility ->" + elementId + "|" + "waitTill ->" + numberOfSecondsToWait);
        return (new WebDriverWait(driver, numberOfSecondsToWait)).pollingEvery(50, TimeUnit.MILLISECONDS).until(ExpectedConditions.elementToBeClickable(elementId));
    }

    public void waitFor(int timeOut) {
        try {
            Thread.sleep(timeOut * 1000);
            Allure.step("waitFor " + timeOut);
        } catch (Exception e) {
            LOG.info("System was not able to sleep for timeout value, exception catched: " + e);
        }
    }

    public boolean isVisible(By elementId) {
        try {
            return driver.findElement(elementId).isDisplayed();
        } catch (Exception e) {
            LOG.info("The element- " + elementId + " expected is not found and thrown exception therefore it is not displayed and thus returned false. Exceptiom : " + e);
            Allure.addAttachment("Any text", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
            return false;
        }
    }

    public Boolean verifyElement(By element) {
        Allure.step("verifyElement : " + element);
        LOG.info("verifyElement : " + element);
        try {
            WebElement el = waitForVisibility(element);
            if (el != null) {
                return el.isDisplayed();
            } else {
                LOG.error(element + "NOT displayed..!");
                Allure.addAttachment("Any text", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
                return false;
            }
        } catch (Exception e) {
            Allure.addAttachment("Any text", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
            LOG.info("skipped");
            return false;
        }
    }


    public void switchToNativeContext() {
        ((AndroidDriver) driver).context("NATIVE_APP");
    }


    public void scrollTillMaxLimitOrElementVisible(String text, int maxScroll) {
        int count = 0;
        while (count < maxScroll) {
            By element = By.xpath("//*[@text='" + text + "']");
            if (isElementPresent(element)) {
                LOG.info("Element found " + element);
                break;
            } else {
                scrollText(text);
                LOG.info("Number of Scrolls: " + count++);
                Allure.addAttachment("Any text", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
            }
        }
    }

    public void scrollText(String text) {
        Allure.step("scrollText " + text);
        LOG.info("scrolling to Text started :  " + text);
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endX = size.width / 2;
        int endY = (int) (size.height * 0.2);
        TouchAction action = new TouchAction((PerformsTouchActions) driver);
        action.press(PointOption.point(startX, startY))
                .waitAction()
                .moveTo(PointOption.point(endX, endY))
                .release()
                .perform();
        LOG.info("scrolling to Text Finished :  " + text);
    }


    public void clickElementByCoordinates(By element) {
        waitForVisibility(element);
        MobileElement el = driver.findElement(element);
        int x = el.getLocation().getX();
        int y = el.getLocation().getY();
        LOG.info("X " + x + "Y :" + y);
        TouchAction action = new TouchAction((PerformsTouchActions) driver);
        action.tap(PointOption.point(x + 10, y + 10)).release().perform();


    }


    public void navigateBack() {
        driver.navigate().back();
        LOG.info("Clicked Device Back button");
    }


    public void volumeIncrease() {
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.VOLUME_UP));
    }

    public void volumeDecrease() {
        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.VOLUME_DOWN));
    }

    public void scrollDown() {
        Dimension dimension = driver.manage().window().getSize();
        int scrollStart = (int) (dimension.getHeight() * 0.7);
        int scrollEnd = (int) (dimension.getHeight() * 0.2);

        TouchAction touch = new TouchAction((PerformsTouchActions) driver);
        touch.press(PointOption.point(0, scrollStart));
        touch.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)));
        touch.moveTo(PointOption.point(0, scrollEnd));
        touch.release().perform();
    }

    public void scrollBelowTillMaxLimitOrElementVisible(By elementId, int maxScroll) {
        int count = 0;
        while (count < maxScroll) {
            if (isVisible(elementId)) {
                waitFor(1); // wait for screen to be stable
                break;
            } else {
                scrollDown();
                LOG.info("Number of Scrolls: " + count++);
                Allure.addAttachment("Any text", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

            }
        }
    }

    public void tapOnCoordinates(int x, int y) {
        TouchAction action = new TouchAction((PerformsTouchActions) driver);
        action.tap(PointOption.point(x, y)).release().perform();

    }

    public void retryClicking(By elementId) {
        for (int i = 0; i < 5; i++) {
            try {
                LOG.info("Trying retry clicking for " + i);
                waitForClickabilityOf(elementId).click();
                break;
            } catch (Exception e) {
                LOG.info("Failed to click element : " + elementId);
            }
        }
    }

    public void scrollAboveTillMaxLimitOrElementVisible(By elementId, int maxScroll) {
        int count = 0;
        while (count < maxScroll) {
            if (isVisible(elementId)) {
                waitFor(1); // wait for screen to be stable
                break;
            } else {
                scrollAbove();
                LOG.info("Number of Scrolls: " + count++);
                Allure.addAttachment("Any text", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

            }
        }
    }

    public void scrollAbove() {
        Dimension dimension = driver.manage().window().getSize();
        int scrollEnd = (int) (dimension.getHeight() * 0.8);
        int scrollStart = (int) (dimension.getHeight() * 0.3);

        TouchAction touch = new TouchAction((PerformsTouchActions) driver);
        touch.press(PointOption.point(0, scrollStart));
        touch.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)));
        touch.moveTo(PointOption.point(0, scrollEnd));
        touch.release().perform();
    }

    public static String getCurrentDateTime() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  |  HH:mm:ss");
        return dateFormat.format(currentDate);
    }

}

