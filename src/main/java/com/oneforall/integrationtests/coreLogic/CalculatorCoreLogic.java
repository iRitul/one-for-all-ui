package com.oneforall.integrationtests.coreLogic;

import com.oneforall.integrationtests.screens.CalculatorScreen;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.Assertion;

import java.util.List;

public class CalculatorCoreLogic {
    private CalculatorScreen calculatorScreen;
    private Assertion assertion;

    public CalculatorCoreLogic(WebDriver driver, String os) {
        calculatorScreen = CalculatorScreen.get(driver, os);
        assertion = new Assertion();
    }

    public CalculatorCoreLogic addSingleDigitNumbers(List<Integer> integers) {
        int expectedSum = 0;
        for (int i : integers) {
            expectedSum += i;
            calculatorScreen.clickNumber(i).clickOnPlusButton();
        }
        calculatorScreen.clickOnDeleteButton().clickOnEqualToButton();
        int sum = calculatorScreen.getResult();
        assertion.assertEquals(sum, expectedSum, "Sum not equal");
        return this;
    }

    public CalculatorCoreLogic subtractSingleDigitNumbers(List<Integer> integers) {
        int expected = integers.get(0);
        for (int i = 1; i < integers.size(); i++) {
            expected -= integers.get(i);
            calculatorScreen.clickNumber(integers.get(i)).clickOnMinusButton();
        }
        calculatorScreen.clickOnDeleteButton().clickOnEqualToButton();
        int remaining = calculatorScreen.getResult();
        assertion.assertEquals(remaining, expected, "Result is not equal to the expected result");
        return this;
    }
}