package com.oneforall.integrationtests.screens.iOS;

import com.oneforall.integrationtests.screens.CalculatorScreen;
import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class IosCalculatorScreen extends CalculatorScreen {
    public static final Logger LOG = Logger.getLogger(IosCalculatorScreen.class);
    private IOSDriver driver;
    public IosCalculatorScreen(WebDriver driver) {
        this.driver = (IOSDriver) driver;
    }

    private static String byNumberButtonText = "//XCUIElementTypeButton[@name='%s']";
    private static By byPlusButtonXpath = MobileBy.xpath("//XCUIElementTypeButton[@name='+']");
    private static By byMinusButtonXpath = MobileBy.xpath("//XCUIElementTypeButton[@name='-']");
    private static By byEqualsToButtonXpath = MobileBy.xpath("//XCUIElementTypeButton[@name='=']");
    private static By byResultAccessibilityId = new MobileBy.ByAccessibilityId("numberField");

    @Override
    public CalculatorScreen clickNumber(int number) {
        genericMethods.waitForClickabilityOf(getNumberButtonXpath(number)).click();
        return this;
    }

    private By getNumberButtonXpath(int number) {
        return By.xpath(String.format(byNumberButtonText, number));
    }

    @Override
    public CalculatorScreen clickOnPlusButton() {
        genericMethods.waitForClickabilityOf(byPlusButtonXpath).click();
        return this;
    }

    @Override
    public CalculatorScreen clickOnDeleteButton() {
        return this;
    }

    @Override
    public CalculatorScreen clickOnEqualToButton() {
        genericMethods.waitForClickabilityOf(byEqualsToButtonXpath).click();
        return this;
    }

    @Override
    public int getResult() {
        return Integer.parseInt(genericMethods.waitForClickabilityOf(byResultAccessibilityId).getAttribute("value").trim());
    }

    @Override
    public CalculatorScreen clickOnMinusButton() {
        genericMethods.waitForClickabilityOf(byMinusButtonXpath).click();
        return this;
    }
}
