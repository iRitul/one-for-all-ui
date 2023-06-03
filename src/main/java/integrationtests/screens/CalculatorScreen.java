package integrationtests.screens;

import allforone.GenericMethods;
import enums.PlatformName;
import integrationtests.screens.android.AndroidCalculatorScreen;
import integrationtests.screens.iOS.IosCalculatorScreen;
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

    public abstract CalculatorScreen clickNumber(int i);

    public abstract CalculatorScreen clickOnPlusButton();

    public abstract CalculatorScreen clickOnDeleteButton();

    public abstract CalculatorScreen clickOnEqualToButton();

    public abstract int getResult();

    public abstract CalculatorScreen clickOnMinusButton();
}
