package integrationtests.screens.android;

import allforone.GenericMethods;
import integrationtests.screens.CalculatorScreen;
import io.appium.java_client.android.AndroidDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class AndroidCalculatorScreen extends CalculatorScreen {
    public static final Logger LOG = Logger.getLogger(AndroidCalculatorScreen.class);
    private AndroidDriver driver;
    public AndroidCalculatorScreen(WebDriver driver) {
        this.driver = (AndroidDriver) driver;
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

