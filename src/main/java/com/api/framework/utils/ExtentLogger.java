package com.api.framework.utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.Markup;

public class ExtentLogger {

    private static final ThreadLocal<ExtentTest> extentTestThread = new ThreadLocal<>();

    public static void setTest(ExtentTest test) {
        extentTestThread.set(test);
    }

    public static ExtentTest getTest() {
        return extentTestThread.get();
    }

    public static void logInfo(String message) {
        getTest().info(message);
    }

    public static void logInfo(Markup m) {
        getTest().info(m);
    }

    public static void logPass(String message) {
        getTest().pass(message);
    }

    public static void logFail(String message) {
        getTest().fail(message);
    }

    public static void logWarning(String message) {
        getTest().warning(message);
    }

    public static void clear() {
        extentTestThread.remove();
    }

    public static void logJson(String label, String json) {
        getTest().info("<pre><b>" + label + ":</b><br><code>" + json + "</code></pre>");
    }

}
