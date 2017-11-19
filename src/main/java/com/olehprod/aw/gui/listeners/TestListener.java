package com.olehprod.aw.gui.listeners;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestListener extends TestListenerAdapter {

    @Override
    public void onTestStart(ITestResult tr) {
        System.out.println(tr.getName() + " -- Test method started\n");
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        System.out.println(tr.getName() + " -- Test method failed\n");
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        System.out.println(tr.getName() + " -- Test method skipped\n");
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        System.out.println(tr.getName() + " -- Test method success\n");
    }

}
