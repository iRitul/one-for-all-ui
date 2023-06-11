package com.oneforall.integrationtests.screens.iOS;

import com.oneforall.integrationtests.screens.CalculatorScreen;
import io.appium.java_client.ios.IOSDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class IosCalculatorScreen extends CalculatorScreen {
    public static final Logger LOG = Logger.getLogger(IosCalculatorScreen.class);
    private IOSDriver driver;
    public IosCalculatorScreen(WebDriver driver) {
        this.driver = (IOSDriver) driver;
    }

    @Override
    public CalculatorScreen clickNumber(int i) {
        return this;
    }

    @Override
    public CalculatorScreen clickOnPlusButton() {
        return this;
    }

    @Override
    public CalculatorScreen clickOnDeleteButton() {
        return this;
    }

    @Override
    public CalculatorScreen clickOnEqualToButton() {
        return this;
    }

    @Override
    public int getResult() {
        return 0;
    }

    @Override
    public CalculatorScreen clickOnMinusButton() {
        return this;
    }
}
