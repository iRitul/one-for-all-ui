package com.oneforall.integrationtests.screens.android;

import com.oneforall.integrationtests.screens.CalculatorScreen;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AndroidCalculatorScreen extends CalculatorScreen {
    public static final Logger LOG = Logger.getLogger(AndroidCalculatorScreen.class);
    private AndroidDriver driver;

    private static String byNumberButtonIdText = "digit_'%s']";
    private static By byPlusButtonId = MobileBy.id("op_add");
    private static By byMinusButtonId = MobileBy.id("op_sub");
    private static By byEqualToButtonId = MobileBy.id("eq");
    private static By byFinalResultId = MobileBy.id("result_final");
    private static By byResultPreviewId = MobileBy.id("result_preview");
    private static By byDeleteButtonId = MobileBy.id("del");


    public AndroidCalculatorScreen(WebDriver driver) {
        this.driver = (AndroidDriver) driver;
    }

    @Override
    public CalculatorScreen clickNumber(int i) {
        genericMethods.waitForClickabilityOf(getNumberButtonID(i)).click();
        return this;
    }

    @Override
    public CalculatorScreen clickOnPlusButton() {
        genericMethods.waitForClickabilityOf(byPlusButtonId).click();
        return this;
    }

    @Override
    public CalculatorScreen clickOnDeleteButton() {
        genericMethods.waitForClickabilityOf(byDeleteButtonId).click();
        return this;
    }

    @Override
    public CalculatorScreen clickOnEqualToButton() {
        genericMethods.waitForClickabilityOf(byEqualToButtonId).click();
        return this;
    }

    @Override
    public int getResult() {
        return Integer.parseInt(genericMethods.waitForClickabilityOf(byFinalResultId).getText().trim());
    }

    @Override
    public CalculatorScreen clickOnMinusButton() {
        genericMethods.waitForClickabilityOf(byMinusButtonId).click();
        return this;
    }

    private By getNumberButtonID(int number) {
        return By.id(String.format(byNumberButtonIdText, number));
    }
}

