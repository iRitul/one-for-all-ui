package tests.sample2;

import com.oneforall.CreateSession;
import com.oneforall.config.DeviceConfig;
import com.oneforall.config.UserDetailsModel;
import com.oneforall.integrationtests.coreLogic.CalculatorCoreLogic;
import io.qameta.allure.*;
import com.oneforall.listeners.AllureListener;
import com.oneforall.logger.Log;
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

    @Test(testName = "Subtraction test")
    @Severity(SeverityLevel.NORMAL)
    @Description("Subtraction of single digit numbers")
    public void subtractionTest() {
        Log.info("Running subtraction of single digit numbers");
        calculatorCoreLogic.subtractSingleDigitNumbers(List.of(3, 2));
        Log.info("Verified subtraction of single digit numbers");
    }
}
