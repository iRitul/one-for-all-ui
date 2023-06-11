package tests.sample;

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
@Epic("Addition Test")
@Feature("Addition Test")
public class SampleTest extends CreateSession {
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
    @Description("Addition of single digit numbers")
    public void additionTest() {
        Log.info("Running addition of single digit numbers");
        calculatorCoreLogic.addSingleDigitNumbers(List.of(1, 2));
        Log.info("Verified addition of single digit numbers");
    }
}