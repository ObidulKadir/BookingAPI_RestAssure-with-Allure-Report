package api.utilities;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import io.qameta.allure.Allure;

public class AllureListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {

        System.out.println("Execution Started...");

        try {

            File allureResults = new File("target/allure-results");

            if (allureResults.exists()) {

                FileUtils.deleteDirectory(allureResults);

                System.out.println("Old Allure Results Deleted");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {

        Allure.step("Starting Test: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        Allure.step("Test Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {

        Allure.step("Test Failed: " + result.getName());

        if (result.getThrowable() != null) {

            Allure.addAttachment(
                    "Failure Reason",
                    result.getThrowable().getMessage()
            );
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        Allure.step("Test Skipped: " + result.getName());
    }

    @Override
    public void onFinish(ITestContext context) {

        System.out.println("Execution Finished...");
    }
}