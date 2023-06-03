package tests.sample2;

import allforone.CreateSession;
import config.DeviceConfig;
import config.UserDetailsModel;
import integrationtests.coreLogic.CalculatorCoreLogic;
import io.qameta.allure.*;
import listeners.AllureListener;
import logger.Log;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

@Listeners(AllureListener.class)
@Epic("Subtraction Test")
@Feature("Subtraction Test")
public class SampleTest2 extends CreateSession {
    UserDetailsModel testData;
    private CalculatorCoreLogic calculatorCoreLogic;

    @Parameters({"os"})
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(String os) throws IOException {
        DeviceConfig def = new DeviceConfig();
        testData = def.readTestData().getTestData(1);
        calculatorCoreLogic = new CalculatorCoreLogic(driver, os);
    }

    @Test(testName = "Addition test")
    @Severity(SeverityLevel.NORMAL)
    @Description("Subtraction of single digit numbers")
    public void subtractionTest() {
        Log.info("Running subtraction of single digit numbers");
        calculatorCoreLogic.subtractSingleDigitNumbers(List.of(3, 2, 1));
        Log.info("Verified subtraction of single digit numbers");
    }
}