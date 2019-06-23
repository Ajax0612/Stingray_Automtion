package com.selenium.commonfiles.util;

import java.util.*;

import org.testng.ITestResult;
import org.testng.Reporter;

public class ErrorUtil  {
	@SuppressWarnings("rawtypes")
	private static Map<ITestResult,List> verificationFailureMap = new HashMap<ITestResult,List>();
	public static Map<ITestResult,List> verificationFailureMapTmp = new HashMap<ITestResult,List>();
	@SuppressWarnings({ "rawtypes", "unused" })
	private static Map<ITestResult,List> skipMap= new HashMap<ITestResult,List>();
		
	
	@SuppressWarnings("unchecked")
	public static void addVerificationFailure(Throwable t){
		@SuppressWarnings("rawtypes")
		List verificationFailures = getVerificationFailures();
		verificationFailureMap.put(Reporter.getCurrentTestResult(), verificationFailures);
		verificationFailureMapTmp.put(Reporter.getCurrentTestResult(), verificationFailures);
		verificationFailures.add(t);
	}
	
	@SuppressWarnings("rawtypes")
	public static List getVerificationFailures(){
		List verificationFailures=verificationFailureMap.get(Reporter.getCurrentTestResult());
		return verificationFailures==null ? new ArrayList() : verificationFailures;
	}
	
	

}
