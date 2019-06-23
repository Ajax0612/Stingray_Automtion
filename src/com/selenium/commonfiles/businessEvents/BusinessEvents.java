package com.selenium.commonfiles.businessEvents;

import java.lang.reflect.Method;
import org.testng.annotations.*;

import com.selenium.commonfiles.util.CommonFunctionsInitializationError;
import com.selenium.commonfiles.util.DataExcelInitError;
import com.selenium.commonfiles.util.DataStructurePopulateError;
import com.selenium.commonfiles.util.ErrorInTestMethod;
import com.selenium.commonfiles.util.ErrorUtil;
import com.selenium.commonfiles.util.TestUtil;

public class BusinessEvents extends DataStructure {


	public static String tc_name = null;
	public static Throwable t;
	public String Scenario = null;
	public String current_TestDataID = null;
	public int current_TestDataID_index = 0;
	public int TOTAL_RUNNABLE_TEST_CASE_IDS;
	public int total_Executed_Tests=0;
	public int total_Passed_Tests=0,total_Failed_Tests=0;
	public int testResultIndicator = 1;
	public boolean isTestFailed = false;

	public BusinessEvents() {
		// TestBase.Initialize("ABC.xlsx");
	}

	/**
	 * 
	 * This method will run only once before all the test case .
	 * 
	 */
	@BeforeMethod
	public void runBeforeSuite(Method m) {
		System.out.println("**before Suite **");
		try {
			Initialize();
		} catch (Exception e) {
			App_logs.debug("Error while initializing framework . ");
			System.out.println("Error while initializing framework . "+e.getMessage());
			e.printStackTrace();
		}
		
	}

	@Test(priority = 0)
	public void HandleBusinessEvent() throws Exception {
		try {

			// common.beforeSuite();
			Initiliaze_DataStructure();
			TOTAL_RUNNABLE_TEST_CASE_IDS = countRunnableTestCases;

			switch (TOTAL_RUNNABLE_TEST_CASE_IDS) {

			case 0:
				System.out.println("** Suite.xlsx does not contain any Runnable Test Cases **");
				break;
			default:

				while (countRunnableTestCases > 0) {
					try {

						// beforeMethod();
						// For e.g.
						current_TestDataID_index = TOTAL_RUNNABLE_TEST_CASE_IDS - countRunnableTestCases;
						current_TestDataID = TestDataIDs.get(current_TestDataID_index);

						Initialize_Common_Functions();

						product = common.getProductName(current_TestDataID);
						businessEvent = common.getBusinessEvent(current_TestDataID);

						customBeforeMethod(current_TestDataID);
						decideDataStructureToInit(businessEvent);
						Populate_DataStructure(current_TestDataID);
						total_Executed_Tests++;
						System.out.println("Started Test '"+current_TestDataID+"'");
						testResultIndicator = common.funcSelectBusinessEvent(product, businessEvent);
						if(testResultIndicator==0){
							testResult = TestUtil.Test_Cases_Staus.PASS.toString();
						}else{
							throw new ErrorInTestMethod("Error in '"+current_TestDataID+"'");
						}
						
						customAfterMethod();
						countRunnableTestCases--;

					} catch (CommonFunctionsInitializationError common_ex) {
						App_logs.error(common_ex);
						System.out.println(common_ex);
						exceptionList.add(common_ex.toString());
						isTestFailed=true;
						break;
					} catch (DataExcelInitError excelInit_ex) {
						App_logs.error(excelInit_ex);
						exceptionList.add(excelInit_ex.toString());
						isTestFailed=true;
						if (excelInit_ex.toString().contains("Suite")){
							System.out.println(excelInit_ex.toString());
							break;
						}else {
							countRunnableTestCases--;
							testResult = TestUtil.Test_Cases_Staus.FAIL.toString();
							total_Failed_Tests++;
							//continue;
						}
					}catch (DataStructurePopulateError dataPopulate_ex) {
						App_logs.error(dataPopulate_ex);
						System.out.println(dataPopulate_ex.toString());
						countRunnableTestCases--;
						testResult = TestUtil.Test_Cases_Staus.FAIL.toString();
						exceptionList.add(dataPopulate_ex.toString());
						isTestFailed=true;
						report.endTest(logger);
						report.flush();
						finalinfo.clear();
						total_Failed_Tests++;
						//continue;
						
					}catch (ErrorInTestMethod testError_ex) {
						App_logs.error(testError_ex);
						System.out.println(testError_ex.toString());
						countRunnableTestCases--;
						testResult = TestUtil.Test_Cases_Staus.FAIL.toString();
						exceptionList.add(testError_ex.toString());
						isTestFailed=true;
						report.endTest(logger);
						report.flush();
						finalinfo.clear();
						total_Failed_Tests++;
						//continue;
						
					}catch (Throwable t) {
						App_logs.error(t);
						System.out.println(t.toString());
						countRunnableTestCases--;
						testResult = TestUtil.Test_Cases_Staus.FAIL.toString();
						exceptionList.add(t.toString());
						isTestFailed=true;
						report.endTest(logger);
						report.flush();
						finalinfo.clear();
						total_Failed_Tests++;
						//continue;
						
					}
					listner.listen(current_TestDataID);
					
				} // while loop
				listner.listenAtSuiteLevel();
				break;
			} //switch

		}catch (DataExcelInitError suite_excelInit_ex) {
			App_logs.error(suite_excelInit_ex);
			System.out.println("Suite.xlsx initialization Error - "+suite_excelInit_ex);
			isTestFailed=true;
			
			
		} catch (Throwable t) {
			// Exception
			App_logs.error(t.getMessage());
			System.out.println(t.getMessage());
			isTestFailed=true;
		}
		finally{
			if(isTestFailed){
				throw new Exception("Test - HandleBusinessEvent Failed");
			}
		}

	}

	/**
	 * 
	 * This method will run only once after all the test case completes running
	 * .
	 * 
	 */
	@AfterMethod
	public void runAfterSuite() throws Throwable {
		// TestUtil.reportDataSetResult(Suite_PCC_Xls,
		// this.getClass().getSimpleName(), count,testResult );
		System.out.println("--||--------------------||--");
		System.out.println("--||---Test Run Stats---||--");
		System.out.println("--||--------------------||--");
		System.out.println("[#]--Total Executed Test Cases = " + total_Executed_Tests);
		System.out.println("[#]--Total Passed Test Cases = " + (total_Executed_Tests-total_Failed_Tests));
		System.out.println("[#]--Total Failed Test Cases = " + total_Failed_Tests);
		System.out.println("--||--------------------||--");
		k.closeBrowser();
		Master_TC_Xls.cleanup_xl_instance();
		exceptionList.clear();
		report.flush();
		App_logs.removeAllAppenders();
		//System.out.println("** Clear Common Data Structure **");
		//System.out.println("**after Suite **");
	}

	/**
	 * 
	 * This method will run for all the runnable Test Case IDs .
	 * 
	 */
	public void customBeforeMethod(String current_TestDataID) throws Exception {

		try {
			ErrorUtil.verificationFailureMapTmp.clear();
			common.GrosspremSmryData.clear();
			logger = report.startTest(current_TestDataID);
			Initialize_XL(product + "_" + businessEvent + ".xlsx");

			App_logs.debug(product + "_" + businessEvent + ".xlsx Workbook initialized Sucessfully.");
			
			tc_name = current_TestDataID.toString();

			//System.out.println("** before Method **");
		} catch (Throwable e) {
			 System.out.println(e.getMessage());
			throw new DataExcelInitError(e.getMessage());
			// throw new Exception(product + "_" + businessEvent + ".xlsx
			// initialization Error " + e.getMessage());
			
			// initialization Error " + e.getMessage());
		}
	}

	/**
	 * 
	 * This method will run for all the runnable Test Case IDs .
	 * 
	 */
	public void customAfterMethod() {

		testResult = "PASS";
		k.closeBrowser();
		report.endTest(logger);
		report.flush();
		finalinfo.clear();
		tc_name = null;
		
		Clear_DataStructure();
		//System.out.println("** after Method **");

	}

}
