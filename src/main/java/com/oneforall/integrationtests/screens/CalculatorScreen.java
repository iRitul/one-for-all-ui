package com.oneforall.integrationtests.screens;

import com.oneforall.GenericMethods;
import com.oneforall.enums.PlatformName;
import com.oneforall.integrationtests.screens.android.AndroidCalculatorScreen;
import com.oneforall.integrationtests.screens.iOS.IosCalculatorScreen;
import org.openqa.selenium.WebDriver;

public abstract class CalculatorScreen {
    public static GenericMethods genericMethods;
    public static CalculatorScreen get(WebDriver driver, String os) {
        genericMethods = new GenericMethods(driver);
        if (os.equalsIgnoreCase(PlatformName.ANDROID.toString())) {
            return new AndroidCalculatorScreen(driver);
        } else {
            return new IosCalculatorScreen(driver);
        }
    }

    public abstract CalculatorScreen clickNumber(int number);

    public abstract CalculatorScreen clickOnPlusButton();

    public abstract CalculatorScreen clickOnDeleteButton();

    public abstract CalculatorScreen clickOnEqualToButton();

    public abstract int getResult();

    public abstract CalculatorScreen clickOnMinusButton();
}
