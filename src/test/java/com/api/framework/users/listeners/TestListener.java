package com.api.framework.users.listeners;

import com.api.framework.utils.ExtentLogger;
import com.api.framework.utils.ExtentManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.util.Arrays;

public class TestListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        Object[] params = result.getParameters();
        String scenarioId = null;

        for (Object param : params) {
            // Get parameters passed to the test
            if (param == null) continue;
            Class<?> clazz = param.getClass();

            try {
                Method getScenarioIdMethod = clazz.getMethod("getScenarioId");
                Object value = getScenarioIdMethod.invoke(param);
                if (value instanceof String) {
                    scenarioId = (String) value;
                    break;
                }
            } catch (NoSuchMethodException ignored) {
                // Fallback to field access if method not found
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ExtentTest test;
        if (scenarioId != null) {
            test = extent.createTest(result.getMethod().getMethodName() + " - " + scenarioId);
        } else {
            test = extent.createTest(result.getMethod().getMethodName());
        }
        extentTest.set(test);
        ExtentLogger.setTest(test);

        String[] groups = result.getMethod().getGroups();
        for (String group : groups) {
            test.assignCategory(group);  // Adds category to Extent Report
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentLogger.logFail(result.getThrowable().toString());
        String stackTrace = Arrays.toString(result.getThrowable().getStackTrace());
        stackTrace = stackTrace.replaceAll(",", "<br>");
        String formmatedTrace = "<details>\n" +
                "    <summary>Click Here To See Exception Logs</summary>\n" +
                "    " + stackTrace + "\n" +
                "</details>\n";
        ExtentLogger.logFail(formmatedTrace);

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentLogger.logWarning("Test Skipped: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentLogger.clear();
        ExtentManager.getInstance().flush();
    }

}
