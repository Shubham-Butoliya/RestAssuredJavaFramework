package com.api.framework.users.testUtils;

import com.api.framework.utils.ExtentLogger;
import io.qameta.allure.Allure;
import org.testng.Assert;

public class AssertUtils {

    public static void assertEquals(Object actual, Object expected, String message) {
        try {
            Assert.assertEquals(actual, expected, message);
            ExtentLogger.logPass("Assertion Passed: " + message + " | Expected: " + expected + ", Actual: " + actual);
            Allure.step("✅ Assertion Passed: " + message +
                    " | Expected: " + expected + ", Actual: " + actual);
        } catch (AssertionError e) {
            ExtentLogger.logFail("Assertion Failed: " + message + " | Expected: " + expected + ", Actual: " + actual);
            Allure.step("❌ Assertion Failed: " + message +
                    " | Expected: " + expected + ", Actual: " + actual);
            throw e;
        }
    }

}
