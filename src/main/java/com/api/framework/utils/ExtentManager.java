package com.api.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static final String REPORT_PATH = "test-output/ExtentReport.html";
    private static ExtentReports extent;

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            extent = createInstance(REPORT_PATH);
        }
        return extent;
    }

    private static ExtentReports createInstance(String filePath) {
        ExtentSparkReporter reporter = new ExtentSparkReporter(filePath);
        reporter.config().setReportName("API Automation Report");
        reporter.config().setDocumentTitle("Execution Report");

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Java", System.getProperty("java.version"));

        return extent;
    }
}
