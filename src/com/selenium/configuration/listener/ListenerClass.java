package com.selenium.configuration.listener;

import java.util.Random;

import org.testng.IClass;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import com.selenium.commonfiles.base.TestBase;
import com.selenium.commonfiles.util.TestUtil;

public class ListenerClass extends TestBase {

	public void listen(String testName) {

		switch (testResult) {
		case "PASS":
			onTestSuccess(testName);
			break;
		case "FAIL":
			onTestFailure(testName);
			break;
		default:
			App_logs.info("Test Result - "+testResult+" is not having Listeners . ");
			break;
		}

	}
	
	public void listenAtSuiteLevel(){
		
		if(exceptionList.size() == 0){
			onSuiteSuccess();
		}else{
			onSuiteFailure();
		}
		
	}
	

	public void onTestStart(ITestResult tr) {
		log("Test Execution has started....");

	}

	public void onTestSuccess(String testName) {

		log("Test '" + testName + "' PASSED");

		System.out.println(".....");
		//TestUtil.reportTestCasePassed(testName);
		TestBase.report.flush();
	}

	public void onTestFailure(String testName) {

		// log("Test '" + testName + "' FAILED");
		String file = System.getProperty("user.dir") + TestBase.screnshtpth + (new Random().nextInt()) + "_" + testName
				+ ".png";

		//System.out.println(file);

		//String image = TestBase.logger.addScreenCapture(file);
		// TestBase.logger.log(LogStatus.FAIL, result.getMethod().toString(),
		// image);
		TestUtil.reportStatus("Test Case Failed : " + testName, "Fail", false);
		// Reporter.log("screenshot saved at "+file);
		String strMsg = "&lt;b&gt;&lt;a target=&apos;_blank&apos; href=&apos;file:///" + file
				+ "&apos;&gt;&lt;img width=&apos;50&apos; height=&apos;60&apos; src=&apos;file:///" + file
				+ "&apos;/&gt;" + testName + "_error link &lt;/a&gt;";
		Reporter.log(strMsg);
		Reporter.setCurrentTestResult(null);

		log("Test '" + testName + "' FAILED");
		// log("Priority of this method is " +testName);
		log("Failed testcases -" + testName);
		System.out.println(".....");
		TestBase.report.flush();
		TestBase.isXLInitialized = false;
		isNBSuiteXLInitialized = false;
		isEventsSuiteXLInitialized = false;
	}
	public void onSuiteSuccess() {

		log("...Test Suite PASSED...");

		System.out.println(".....");
		TestBase.report.flush();
	}
	
	public void onSuiteFailure() {

		log("...Test Suite FAILED...");

		System.out.println(".....");
		TestBase.report.flush();
	}

	private void log(String methodName) {
		System.out.println(methodName);
	}

	/*
	 * private void log(IClass testClass) { System.out.println(testClass); }
	 */
}