package com.selenium.commonfiles.base;

import static com.selenium.commonfiles.util.TestUtil.WriteDataToXl;

import java.awt.AWTException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.selenium.commonfiles.util.ErrorInTestMethod;
import com.selenium.commonfiles.util.TestUtil;

public class CommonFunction_XOQ extends TestBase{

	SimpleDateFormat df = new SimpleDateFormat();
	Properties PLEL_Rater = new Properties();
	public int actual_no_of_years=0,err_count=0,trans_error_val=0;
	public Map<String,Double> PL_EL_Rater_output = new HashMap<>();
	public boolean isIncludingHeatPresent = false,isExcludingHeatPresent = false,isIncludeHeat_10M=false;
	public double cent_work_including_heat = 0.0; 
	
public void NewBusinessFlow(String code,String event) throws ErrorInTestMethod{
	String testName = (String)common.NB_excel_data_map.get("Automation Key");
	String navigationBy = CONFIG.getProperty("NavigationBy");
	try{
		
		customAssert.assertTrue(common.StingrayLogin("VELA"),"Unable to login.");
		customAssert.assertTrue(common.checkClient(common.NB_excel_data_map,code,event),"Unable to check Client.");
		customAssert.assertTrue(common.createNewQuote(common.NB_excel_data_map,code,event), "Unable to create new quote.");
		customAssert.assertTrue(common.selectLatestQuote(common.NB_excel_data_map,code,event), "Unable to select quote from table.");
		customAssert.assertTrue(common_XOC.funcPolicyGeneral(common.NB_excel_data_map,code,event), "Policy General function having issue .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(common_XOC.funcPreviousClaims(common.NB_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(common_VELA.funcCovers(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(common_XOC.MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"General Risk Details"),"Issue while Navigating to General Risk Details screen . ");
		customAssert.assertTrue(common_XOC.GeneralRiskDetailsPage(common.NB_excel_data_map));
	
		if(((String)common.NB_excel_data_map.get("CD_ThirdPartyMotorLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Third Party Motor Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.ThirdPartyMotorLiability(common.NB_excel_data_map), "Issue with  Third Party Motor Liability function");
		}
		if(((String)common.NB_excel_data_map.get("CD_CombinedLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Combined Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.CombinedLiability(common.NB_excel_data_map), "Issue with  CombinedLiability function");
		}
		if(((String)common.NB_excel_data_map.get("CD_Public&ProductsLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen . ");
			customAssert.assertTrue(common_XOC.PublicProductsLiabilityPage(common.NB_excel_data_map), "Issue with  Public & Products Liability function");
		}
		if(((String)common.NB_excel_data_map.get("CD_EmployersLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.EmployersLiabilityPage(common.NB_excel_data_map), "Issue with  Employers Liability function");
		}
		if(((String)common.NB_excel_data_map.get("CD_JCT6.5.1")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.JCTPage(common.NB_excel_data_map), "Issue with  Employers Liability function");
		}
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
		customAssert.assertTrue(common.funcEndorsementOperations(common.NB_excel_data_map),"Endorsement is having issue(S).");
	
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_VELA.funcPremiumSummary(common.NB_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			Assert.assertTrue(common_VELA.funcStatusHandling(common.NB_excel_data_map,code,event));
		
		if(TestBase.businessEvent.equals("NB")){
				customAssert.assertEquals(common_VELA.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
				customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
				TestUtil.reportTestCasePassed(testName);
			}
			
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}

public void RewindFlow(String code,String event) throws ErrorInTestMethod{
	String testName = (String)common.Rewind_excel_data_map.get("Automation Key");
	try{
		common.currentRunningFlow="NB";
		String s= TestBase.businessEvent;
		if(!common.currentRunningFlow.equalsIgnoreCase("Renewal") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
			NewBusinessFlow(code,"NB");
		}
		
		common.currentRunningFlow="Rewind";
		String navigationBy = CONFIG.getProperty("NavigationBy");
		common_CTA.PremiumFlag = false;
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common.funcRewind());
		
		TestUtil.reportStatus("<b> -----------------------Rewind flow is started---------------------- </b>", "Info", false);
		
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
		customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
		
		if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.MTA_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		}else{
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		}
		customAssert.assertTrue(common_XOC.funcPolicyGeneral(common.Rewind_excel_data_map,code,event), "Policy General function having issue .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(common_XOC.funcPreviousClaims(common.Rewind_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(common_VELA.funcCovers(common.Rewind_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(common_XOC.MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"General Risk Details"),"Issue while Navigating to General Risk Details screen . ");
		customAssert.assertTrue(common_XOC.GeneralRiskDetailsPage(common.Rewind_excel_data_map));
		
		if(((String)common.Rewind_excel_data_map.get("CD_ThirdPartyMotorLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Third Party Motor Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.ThirdPartyMotorLiability(common.Rewind_excel_data_map), "Issue with  Third Party Motor Liability function");
		}
		if(((String)common.Rewind_excel_data_map.get("CD_CombinedLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Combined Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.CombinedLiability(common.Rewind_excel_data_map), "Issue with  CombinedLiability function");
		}
		if(((String)common.Rewind_excel_data_map.get("CD_Public&ProductsLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen . ");
			customAssert.assertTrue(common_XOC.PublicProductsLiabilityPage(common.Rewind_excel_data_map), "Issue with  Public & Products Liability function");
		}
		if(((String)common.Rewind_excel_data_map.get("CD_EmployersLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.EmployersLiabilityPage(common.Rewind_excel_data_map), "Issue with  Employers Liability function");
		}
		if(((String)common.Rewind_excel_data_map.get("CD_JCT6.5.1")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.JCTPage(common.Rewind_excel_data_map), "Issue with  Employers Liability function");
		}
		
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		if(!TestBase.businessEvent.equals("MTA")){
		customAssert.assertTrue(common_VELA.funcPremiumSummary(common.Rewind_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
		Assert.assertTrue(common_VELA.funcStatusHandling(common.Rewind_excel_data_map,code,event));
		}else{
			customAssert.assertTrue(common_VELA.funcPremiumSummary_MTA(common.Rewind_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
		}
		if(TestBase.businessEvent.equals("Rewind")){
		customAssert.assertEquals(common_VELA.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
		customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
		TestUtil.reportTestCasePassed(testName);
		}
		
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}


public void RequoteFlow(String code,String event) throws ErrorInTestMethod{
	if(!common.currentRunningFlow.equalsIgnoreCase("Renewal") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
		NewBusinessFlow(code,"NB");
	}
	common.currentRunningFlow="Requote";
	
	String testName = (String)common.Requote_excel_data_map.get("Automation Key");
	String navigationBy = CONFIG.getProperty("NavigationBy");
	try{
		
		customAssert.assertTrue(common.funcButtonSelection("Re-Quote"));
		
		TestUtil.reportStatus("<b> -----------------------Requote flow is started---------------------- </b>", "Info", false);
		
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
		customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Requote_excel_data_map,code,event,"Submitted"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		
		customAssert.assertTrue(common_XOC.funcPolicyGeneral(common.Requote_excel_data_map,code,event), "Policy General function having issue .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(common_XOC.funcPreviousClaims(common.Requote_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(common_VELA.funcCovers(common.Requote_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(common_XOC.MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"General Risk Details"),"Issue while Navigating to General Risk Details screen . ");
		customAssert.assertTrue(common_XOC.GeneralRiskDetailsPage(common.Requote_excel_data_map));
		if(((String)common.Requote_excel_data_map.get("CD_ThirdPartyMotorLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Third Party Motor Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.ThirdPartyMotorLiability(common.Requote_excel_data_map), "Issue with  Third Party Motor Liability function");
		}
		if(((String)common.Requote_excel_data_map.get("CD_CombinedLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Combined Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.CombinedLiability(common.Requote_excel_data_map), "Issue with  CombinedLiability function");
		}
		if(((String)common.Requote_excel_data_map.get("CD_Public&ProductsLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen . ");
			customAssert.assertTrue(common_XOC.PublicProductsLiabilityPage(common.Requote_excel_data_map), "Issue with  Public & Products Liability function");
		}
		if(((String)common.Requote_excel_data_map.get("CD_EmployersLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.EmployersLiabilityPage(common.Requote_excel_data_map), "Issue with  Employers Liability function");
		}
		if(((String)common.Requote_excel_data_map.get("CD_JCT6.5.1")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.JCTPage(common.Requote_excel_data_map), "Issue with  Employers Liability function");
		}
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common_VELA.funcPremiumSummary(common.Requote_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
		Assert.assertTrue(common_VELA.funcStatusHandling(common.Requote_excel_data_map,code,event));
		customAssert.assertEquals(common_VELA.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
		

	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}

public void CancellationFlow(String code,String event) throws ErrorInTestMethod{
	
	String testName = (String)common.CAN_excel_data_map.get("Automation Key");
	try{
		NewBusinessFlow(code,"NB");
		
		common.currentRunningFlow="CAN";
		System.out.println("Test method of CAN For - "+code);
		
		customAssert.assertTrue(common_COB.COB_CancelPolicy(common.CAN_excel_data_map), "Unable to Logout.");
		customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
		
		customAssert.assertTrue(common_COB.COB_Cancel_PremiumSummary(common.CAN_excel_data_map), "Issue in verifying Premium summary for cancellation");
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""), "Unable to click on policies tab");
		Assert.assertTrue(common_VELA.funcStatusHandling(common.NB_excel_data_map,code,TestBase.businessEvent));
				
		if(TestBase.businessEvent.equals("CAN")){
			customAssert.assertEquals(err_count,0,"Errors in premium calculations . ");
			customAssert.assertEquals(trans_error_val,0,"Errors in Transaction premium calculations . ");
			customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
		
			customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
			TestUtil.reportTestCasePassed(testName);
		}
		
	}catch (ErrorInTestMethod e) {
		System.out.println("Error in New Business test method for Cancellation > "+testName);
		throw e;
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
	
}
public void RenewalFlow(String code,String event) throws ErrorInTestMethod{
	String testName = (String)common.Renewal_excel_data_map.get("Automation Key");
	common.currentRunningFlow = "Renewal";
	String navigationBy = CONFIG.getProperty("NavigationBy");
	try{
		
		customAssert.assertTrue(common.StingrayLogin("VELA"),"Unable to login.");
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		/*customAssert.assertTrue(common_POB.renewalSearchPolicyNEW(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Pending"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
		customAssert.assertTrue(common.funcAssignUnderWriter((String)common.Renewal_excel_data_map.get("Renewal_AssignUnderwritter")));
		customAssert.assertTrue(common.funcButtonSelection("Send to Underwriter"));
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Submitted"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
		*/
         customAssert.assertTrue(common_CCJ.renewalSearchPolicyNEW(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Pending"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		if(!common_HHAZ.isAssignedToUW){ // This variable is initialized in common_CCJ.renewalSearchPolicyNEW function
			customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
			customAssert.assertTrue(common_SPI.funcAssignPolicyToUW());
		}
		
		customAssert.assertTrue(common.funcButtonSelection("Send to Underwriter"));
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
	
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Submitted"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
		
		customAssert.assertTrue(common_XOC.funcPolicyGeneral(common.Renewal_excel_data_map,code,event), "Policy General function having issue .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(common_XOC.funcPreviousClaims(common.Renewal_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(common_VELA.funcCovers(common.Renewal_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(common_XOC.MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"General Risk Details"),"Issue while Navigating to General Risk Details screen . ");
		customAssert.assertTrue(common_XOC.GeneralRiskDetailsPage(common.Renewal_excel_data_map));
	
		if(((String)common.Renewal_excel_data_map.get("CD_ThirdPartyMotorLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Third Party Motor Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.ThirdPartyMotorLiability(common.Renewal_excel_data_map), "Issue with  Third Party Motor Liability function");
		}
		if(((String)common.Renewal_excel_data_map.get("CD_CombinedLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Combined Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.CombinedLiability(common.Renewal_excel_data_map), "Issue with  CombinedLiability function");
		}
		if(((String)common.Renewal_excel_data_map.get("CD_Public&ProductsLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen . ");
			customAssert.assertTrue(common_XOC.PublicProductsLiabilityPage(common.Renewal_excel_data_map), "Issue with  Public & Products Liability function");
		}
		if(((String)common.Renewal_excel_data_map.get("CD_EmployersLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.EmployersLiabilityPage(common.Renewal_excel_data_map), "Issue with  Employers Liability function");
		}
		if(((String)common.Renewal_excel_data_map.get("CD_JCT6.5.1")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.JCTPage(common.Renewal_excel_data_map), "Issue with  Employers Liability function");
		}
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
	//	customAssert.assertTrue(common.funcEndorsementOperations(common.Renewal_excel_data_map),"Endorsement is having issue(S).");
	
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_VELA.funcPremiumSummary(common.Renewal_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			Assert.assertTrue(common_VELA.funcStatusHandling(common.Renewal_excel_data_map,code,event));
			
			if(TestBase.businessEvent.equals("NB")){
				customAssert.assertEquals(common_VELA.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
				customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
				TestUtil.reportTestCasePassed(testName);
			}
			
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}
public boolean funcClaimsExperience(Map<Object, Object> map_data){

    boolean retvalue = true;
    
      try {    
    	  	customAssert.assertTrue(common.funcMenuSelection("Navigate", "Claims Experience"),"Unable To navigate to Claims Experience screen");
			customAssert.assertTrue(common.funcPageNavigation("Claims Experience",""), "Claims Experience Page Navigation issue . ");
			TestUtil.reportStatus("varified Claims Experience page .", "Info", true);
			customAssert.assertTrue(common.funcButtonSelection("Next"), "Unable to click on Next Button on Claims Experience .");
			return retvalue;
             
      } catch(Throwable t) {
             String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
          TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
          Assert.fail("Unable to enter details in Previous Claims Page", t);
          return false;
      }


}

public boolean funcPreviousClaims(Map<Object, Object> map_data){

    boolean retvalue = true;
    
      try {    
    	  	customAssert.assertTrue(common.funcMenuSelection("Navigate", "Previous Claims"),"Unable To navigate to Previous Claims scren");
			customAssert.assertTrue(common.funcPageNavigation("Previous Claims",""), "Previous Claims Page Navigation issue . ");
			TestUtil.reportStatus("varified Previous Claims page .", "Info", true);
			customAssert.assertTrue(common.funcButtonSelection("Next"), "Unable to click on Next Button on Previous Claims .");
			return retvalue;
             
      } catch(Throwable t) {
             String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
          TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
          Assert.fail("Unable to enter details in Previous Claims Page", t);
          return false;
      }


}

public boolean funcPolicyGeneral(Map<Object, Object> map_data, String code, String event) {
	boolean retVal = true;
	String testName = "";
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");
		
		customAssert.assertTrue(k.Input("COB_PG_InsuredName", (String)map_data.get("PG_InsuredName")),	"Unable to enter value in Insured Name  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_InsuredName", "value"),"Insured Name Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("COB_PG_BusinessEstYear", (String)map_data.get("PG_YearEstablished")),	"Unable to enter value in Business Instablished year  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_BusinessEstYear", "value"),"Business Establishment Year Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("COB_PG_TurnOver", (String)map_data.get("PG_TurnOver")),	"Unable to enter value in Turnover field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_TurnOver", "value"),"TurnOver Field Should Contain Valid Name  .");
		customAssert.assertTrue(!k.getListAttributeIsEmpty("XOC_PG_GeoLimit", ""),"Geographic Limit Field Should Contain Valid Name  .");
		//System.out.println(driver.findElement(By.xpath("//*[@id='p1_f1']/tbody/tr[10]/td[2]/span/span[1]/span")).getText());
		//System.out.println(driver.findElement(By.xpath("//*[@id='p1_f1']/tbody/tr[10]/td[2]/span/span[1]/span/ul/li[1]")).getText());
		
		String[] geographical_Limits = ((String)common.NB_excel_data_map.get("PG_GeoLimit")).split(",");
		List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
		for(String geo_limit : geographical_Limits){
			for(WebElement each_ul : ul_elements){
				customAssert.assertTrue(k.Click("XOC_PG_GeoLimit"),"Error while Clicking Geographic Limit List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		
		
	String[] Prof_Bodies = ((String)common.NB_excel_data_map.get("PG_InsuredDetails_Q1")).split(",");
       if(TestBase.businessEvent.equals("Renewal")){
    	  Prof_Bodies = ((String)common.Renewal_excel_data_map.get("PG_InsuredDetails_Q1")).split(",");	
		}
		List<WebElement> ul_ele = driver.findElements(By.xpath("//ul"));
		for(String pBodies_limit : Prof_Bodies){
			for(WebElement each_ul : ul_ele){
				customAssert.assertTrue(k.Click("XOC_PG_InsuredDetails_Q1"),"Error while Clicking Professional Bodies List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+pBodies_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+pBodies_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		customAssert.assertTrue(!k.getListAttributeIsEmpty("XOC_PG_InsuredDetails_Q1", ""),"Geographic Limit Field Should Contain Valid Name  .");	
		customAssert.assertTrue(k.DropDownSelection("COB_PG_InsuredDetails_Q2", (String)map_data.get("PG_InsuredDetails_Q2")),"Unable to select Second qustion");
		String sVal = (String)map_data.get("PG_InsuredDetails_Q2");
		if(sVal.contains("Yes")){
			customAssert.assertTrue(k.Input("COB_PG_InsuredDetails_details", (String)map_data.get("PG_InsuredDetails_details")),	"Unable to enter value in Provided Details field .");
		}
		
		customAssert.assertTrue(k.DropDownSelection("COB_PG_QuoteValidity", (String)map_data.get("PG_QuoteValidity")),"Unable to select Auote Validity value");
		
		customAssert.assertTrue(k.Input("COB_PG_BusDesc", (String)map_data.get("PG_BusDesc")),	"Unable to enter value in Provided Details field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_BusDesc", "value"),"Business Description Field Should Contain Valid Name  .");
		// Select Trade Code :
			
			String sValue = (String)map_data.get("PG_TCS_TradeCode");
			if(!sValue.equalsIgnoreCase("") || sValue!=null){
				customAssert.assertTrue(common_XOC.XOC_SelectTradeCode((String)common.NB_excel_data_map.get("PG_TCS_TradeCode") , "Policy General" , 0),"Trade code selection function is having issue(S).");
			}
			
		//customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");
		
		TestUtil.reportStatus("Entered all the details on Policy General page .", "Info", true);
		
		return retVal;
	
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Unable to to do operation on policy details page. \n", t);
        return false;
	}
	
}

public boolean COB_SelectTradeCode(String tradeCodeValue , String pageName , int currentPropertyIndex) {
	
		try{
			
			customAssert.assertTrue(k.Click("COB_Btn_SelectTradeCode"), "Unable to click on Select Trade Code button in Policy Details .");
			customAssert.assertTrue(common.funcPageNavigation("Trade Code Selection", ""), "Navigation problem to Trade Code Selection page .");
			
			String sVal = tradeCodeValue;
			String[] TradeCodes = tradeCodeValue.split(",");
			
			String a_selectedTradeCode = null;
			String a_selectedTradeCode_desc = null;
			
			for(String s_TradeCode : TradeCodes){
	 			driver.findElement(By.name("multiTrade_"+s_TradeCode)).click();
	 		}
			
			common.funcButtonSelection("Save");
			common.funcButtonSelection("exit (Save First)");
	 		
	 		return true;
	 		
		}catch(Throwable t){
			return false;
		}
}

public void MTAFlow(String code,String event) throws ErrorInTestMethod{
	String testName = (String)common.MTA_excel_data_map.get("Automation Key");
	try{
		
		if(!TestBase.businessEvent.equalsIgnoreCase("Renewal")){
			NewBusinessFlow(code,"NB");
			}
		common.currentRunningFlow="MTA";
		String navigationBy = CONFIG.getProperty("NavigationBy");
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen.");
		customAssert.assertTrue(common_XOC.funcCreateEndorsement(),"Issue while creating Endorsement. ");
		customAssert.assertTrue(common_XOC.funcPolicyGeneral_MTA(common.MTA_excel_data_map,code,"MTA"), "Policy Details function having issue .");
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(funcPreviousClaims(common.MTA_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Previous Claims  . ");
		
		customAssert.assertTrue(common_VELA.funcCovers(common.MTA_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"General Risk Details"),"Issue while Navigating to General Risk Details screen . ");
		customAssert.assertTrue(common_XOC.GeneralRiskDetailsPage(common.MTA_excel_data_map));
		
		if(((String)common.MTA_excel_data_map.get("CD_ThirdPartyMotorLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Third Party Motor Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.ThirdPartyMotorLiability(common.MTA_excel_data_map), "Issue with  Third Party Motor Liability function");
		}
		if(((String)common.MTA_excel_data_map.get("CD_CombinedLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Combined Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.CombinedLiability(common.MTA_excel_data_map), "Issue with  CombinedLiability function");
		}
		if(((String)common.MTA_excel_data_map.get("CD_Public&ProductsLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen . ");
			customAssert.assertTrue(common_XOC.PublicProductsLiabilityPage(common.MTA_excel_data_map), "Issue with  Public & Products Liability function");
		}
		if(((String)common.MTA_excel_data_map.get("CD_EmployersLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.EmployersLiabilityPage(common.MTA_excel_data_map), "Issue with  Employers Liability function");
		}
		if(((String)common.MTA_excel_data_map.get("CD_JCT6.5.1")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.JCTPage(common.MTA_excel_data_map), "Issue with  Employers Liability function");
		}
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
	//	customAssert.assertTrue(common.funcEndorsementOperations(common.MTA_excel_data_map),"Endorsement is having issue(S).");
	
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
	
		
		customAssert.assertTrue(common_VELA.funcPremiumSummary_MTA(common.MTA_excel_data_map,code,"MTA"), "MTA Premium Summary function is having issue(S) . ");
		common_VELA.PremiumFlag = false;
		if(!TestBase.businessEvent.equalsIgnoreCase("Renewal")){
		Assert.assertTrue(common_VELA.funcStatusHandling(common.MTA_excel_data_map,code,"MTA"));
		customAssert.assertEquals(common_VELA.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
		customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
		TestUtil.reportTestCasePassed(testName);
		}

	
	}catch (ErrorInTestMethod e) {
		System.out.println("Error in New Business test method for MTA > "+testName);
		throw e;
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
	
	
}



/**
 * 
 * This method handles all Cancellation Cases for SPI product.
 * 
 */
/*public void CancellationFlow(String code,String event) throws ErrorInTestMethod{
	
	String testName = (String)common.CAN_excel_data_map.get("Automation Key");
	try{
		NewBusinessFlow(code,"NB");
		//		System.out.println("Test method of CAN For - "+code);
//		System.out.println("CAN data Map - "+common.CAN_excel_data_map);
//		System.out.println("CAN inner data Map - "+common.CAN_Structure_of_InnerPagesMaps);
// 
		//customAssert.assertTrue(CancellationProcess(common.NB_excel_data_map,code,event),"Failed in Cancellation Process Function");
		common.currentRunningFlow = "CAN";
		TestUtil.reportStatus("Test Method of CAN For - "+code, "Pass", true);
		Assert.assertTrue(common.funcStatusHandling(common.CAN_excel_data_map,code,event));
		customAssert.assertEquals(err_count,0,"Errors in premium calculations . ");
		customAssert.assertEquals(trans_error_val,0,"Errors in Transaction premium calculations . ");
		customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
		
		
		customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
	
		TestUtil.reportTestCasePassed(testName);
	
	}catch (ErrorInTestMethod e) {
		System.out.println("Error in New Business test method for Cancellation > "+testName);
		throw e;
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
	
}*/




//*************************START***********************************//
//************************************************************//
//**** PL EL Book Rate Calculator Based on CPRS Sheet ********//
//************************************************************//
//************************************************************//

//**** Calculate Wage Size PL & EL Discount *****//
public int calculate_Wage_Size_Discount(String coverName){
	
	
	int final_wageSize_discount = 0;
	String wage_Size_discount_Rate = null;
	String sectionName = null;
	int annualWagerollalue = 0;
	try{
	
	PLEL_Rater = OR.getORProperties();

	
	int total_Wages=0;
	
		List<WebElement> employeeWages = driver.findElements(By.xpath("//input[contains(@name,'coa_empl_break_tc_activity_tcode')]")); 
		int total_EWages = employeeWages.size();
	
		List<WebElement> cent_employeeWages = driver.findElements(By.xpath("//input[contains(@name,'coa_bd_wageroll_tcode')]")); 
	
		for(int i=0;i<total_EWages;i++){
		 
			sectionName = employeeWages.get(i).getAttribute("value");
			annualWagerollalue = Integer.parseInt(cent_employeeWages.get(i).getAttribute("value"));
			total_Wages = total_Wages + annualWagerollalue;
		}
		switch(coverName){
		
		case "PL":
			
			if(total_Wages <= 250000){
				wage_Size_discount_Rate = PLEL_Rater.getProperty("PL_Wage_Size_0k_250k");
			}else if(total_Wages > 250000 && total_Wages <=500000){
				wage_Size_discount_Rate = PLEL_Rater.getProperty("PL_Wage_Size_250k_500k");
			}else if(total_Wages > 500000 && total_Wages <=750000){
				wage_Size_discount_Rate = PLEL_Rater.getProperty("PL_Wage_Size_500k_750k");
			}else if(total_Wages > 750000 && total_Wages <=1000000){
				wage_Size_discount_Rate = PLEL_Rater.getProperty("PL_Wage_Size_750k_1M");
			}else{
				wage_Size_discount_Rate = PLEL_Rater.getProperty("PL_Wage_Size_Above_1M");
			}
			
			break;
			
		case "EL":
			
			if(total_Wages <= 250000){
				wage_Size_discount_Rate = PLEL_Rater.getProperty("EL_Wage_Size_0k_250k");
			}else if(total_Wages > 250000 && total_Wages <=500000){
				wage_Size_discount_Rate = PLEL_Rater.getProperty("EL_Wage_Size_250k_500k");
			}else if(total_Wages > 500000 && total_Wages <=750000){
				wage_Size_discount_Rate = PLEL_Rater.getProperty("EL_Wage_Size_500k_750k");
			}else if(total_Wages > 750000 && total_Wages <=1000000){
				wage_Size_discount_Rate = PLEL_Rater.getProperty("EL_Wage_Size_750k_1M");
			}else{
				wage_Size_discount_Rate = PLEL_Rater.getProperty("EL_Wage_Size_Above_1M");
			}
			
			break;
		default:
			break;
		
		
		}
	
			
			
	}catch(Throwable t){
		System.out.println("Error while Calculating Wage Size discount - "+t);
	}
	final_wageSize_discount = Integer.parseInt(wage_Size_discount_Rate);
	return final_wageSize_discount;
	
}

//Calculate Turnover Size PL Discount
public int calculate_Turnover_Size_Discount(String coverName){
	
	
	int final_turnOver_discount = 0;
	String turnover_Size_discount_Rate = null;
		
	Map<Object,Object> data_map = null;
	
	switch(common.currentRunningFlow){
	
		case "NB":
			data_map = common.NB_excel_data_map;
			break;
		case "Renewal":
			data_map = common.Renewal_excel_data_map;
			break;
	
	}
	
	if(coverName.equals("EL")){
		return final_turnOver_discount;
	}
	
	try{
	
		PLEL_Rater = OR.getORProperties();

		double policy_TurnOver = Double.parseDouble((String)data_map.get("PG_TurnOver"));
		
		switch(coverName){
		
			case "PL":
				
				if(policy_TurnOver <= 250000){
					turnover_Size_discount_Rate = PLEL_Rater.getProperty("PL_Turnover_Size_0k_250k");
				}else if(policy_TurnOver > 250000 && policy_TurnOver <=500000){
					turnover_Size_discount_Rate = PLEL_Rater.getProperty("PL_Turnover_Size_250k_500k");
				}else if(policy_TurnOver > 500000 && policy_TurnOver <=750000){
					turnover_Size_discount_Rate = PLEL_Rater.getProperty("PL_Turnover_Size_500k_750k");
				}else if(policy_TurnOver > 750000 && policy_TurnOver <=1000000){
					turnover_Size_discount_Rate = PLEL_Rater.getProperty("PL_Turnover_Size_750k_1M");
				}else{
					turnover_Size_discount_Rate = PLEL_Rater.getProperty("PL_Turnover_Size_Above_1M");
				}
				
			break;
			
			case "Works":
				
				if(policy_TurnOver <= 1000000){
					turnover_Size_discount_Rate = PLEL_Rater.getProperty("Works_Turnover_Size_0M_1M");
				}else if(policy_TurnOver > 1000000 && policy_TurnOver <=5000000){
					turnover_Size_discount_Rate = PLEL_Rater.getProperty("Works_Turnover_Size_1M_5M");
				}else if(policy_TurnOver > 5000000 && policy_TurnOver <=10000000){
					turnover_Size_discount_Rate = PLEL_Rater.getProperty("Works_Turnover_Size_5M_10M");
				}else if(policy_TurnOver > 10000000){
					turnover_Size_discount_Rate = PLEL_Rater.getProperty("Works_Turnover_Size_Above_10M");
				}
				
			break;
		}
	
					
			
	}catch(Throwable t){
		System.out.println("Error while Calculating Turnover Size discount - "+t);
	}
	final_turnOver_discount = Integer.parseInt(turnover_Size_discount_Rate);
	return final_turnOver_discount;
	
}

//Calculate Area of Work PL Discount
public double calculate_Areas_of_Work_Discount(String coverName){
	
	double final_AOW_discount = 0;
	PLEL_Rater = OR.getORProperties();
	String sectionName = null;
	double centValue = 0.0;
	
	try{
	Map<Object,Object> data_map = null;
	
	switch(common.currentRunningFlow){
	
		case "NB":
			data_map = common.NB_excel_data_map;
			break;
		case "Renewal":
			data_map = common.Renewal_excel_data_map;
			break;
	
	}
			//input[contains(@name,"coa_percentage_annual_work")]
			//input[contains(@name,"coa_percentage_total")]
			List<WebElement> contractingAOW = driver.findElements(By.xpath("//input[contains(@name,'coa_percentage_annual_work')]")); 
			int total_AOW = contractingAOW.size();
			
			List<WebElement> cent_contractingAOW = driver.findElements(By.xpath("//input[contains(@name,'coa_percentage_total')]")); 
			
			for(int i=0;i<total_AOW;i++){
				 
				sectionName = contractingAOW.get(i).getAttribute("value");
				centValue = Double.parseDouble(cent_contractingAOW.get(i).getAttribute("value"));
				final_AOW_discount = final_AOW_discount + get_AOP_discount(sectionName,centValue,data_map,coverName);
		  }
			
				
	}catch(Throwable t){
		System.out.println("Error while Calculating Area Of Work discount - "+t);
	}
	return final_AOW_discount;
	
}

public double get_AOP_discount(String Area_Of_Work,double cent_AOW,Map<Object,Object> data_map,String coverName){
	
	double AOP_discount = 0.0;
	PLEL_Rater = OR.getORProperties();
	
	k.scrollInView("COB_turnover_contacting_cent");
	String turnOver_contract_cent_s = k.getText("COB_turnover_contacting_cent");
	turnOver_contract_cent_s = turnOver_contract_cent_s.substring(0, turnOver_contract_cent_s.indexOf("%"));
	int turnOver_contract_cent = Integer.parseInt(turnOver_contract_cent_s);
	
	try{
		
		
		switch(Area_Of_Work){
	
			case "Domestic - PDH, flats & New Builds":
				AOP_discount = cent_AOW * (turnOver_contract_cent) 
				* (Double.parseDouble(PLEL_Rater.getProperty(coverName+"_PDH_Flats_New_Build")));
				break;
			
			case "Commercial - Shops & Offices":
				AOP_discount = cent_AOW * (turnOver_contract_cent) 
				* (Double.parseDouble(PLEL_Rater.getProperty(coverName+"_Shops_Offices")));
				break;
			
			case "Other Commerical":
				AOP_discount = cent_AOW * (turnOver_contract_cent) 
				* (Double.parseDouble(PLEL_Rater.getProperty(coverName+"_Hotels_Leisure_Centres")));

				break;
			
			case "Industrial - Manufacturing plant & production areas":
				AOP_discount = cent_AOW * (turnOver_contract_cent) 
				* (Double.parseDouble(PLEL_Rater.getProperty(coverName+"_Commercial_Industrial")));

				break;
			
			case "Other Industrial":
				AOP_discount = cent_AOW * (turnOver_contract_cent) 
				* (Double.parseDouble(PLEL_Rater.getProperty(coverName+"_Commercial_Industrial")));

				break;
			
			default:
					System.out.println("* Invalid Area of Work *");
				break;
	}
		
	
	}catch(Throwable t){
		System.out.println("Error in get_AOP_discount method - "+t);
	}
	AOP_discount = AOP_discount/10000;
	return AOP_discount;
	
}

public double calculate_Claims_discount(String coverName){
	
	double total_claims_discount = 0.0;
	PLEL_Rater = OR.getORProperties();
	
	try{
	
	Map<Object,Object> data_map = null;
	switch(common.currentRunningFlow){
	
	case "NB":
		data_map = common.NB_excel_data_map;
		break;
	case "Renewal":
		data_map = common.Renewal_excel_data_map;
		break;

}
	
	double claim_ratio = Double.parseDouble((String)data_map.get("CE_TotalLossRatio"));
	
	switch(get_Years_Trading()){
	
		case 0:
		case 1:
		case 2:
			if(claim_ratio == 0)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_0_Years_Trading_0_2"));
			else if(claim_ratio >= 0.01 && claim_ratio <= 9.99)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_0.01_9.99_Years_Trading_0_2"));
			else if(claim_ratio >= 10.00 && claim_ratio <= 19.99)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_10.00_19.99_Years_Trading_0_2"));
			else if(claim_ratio >= 20.00 && claim_ratio <= 29.99)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_20.00_29.99_Years_Trading_0_2"));
			else if(claim_ratio >= 30.00 && claim_ratio <= 39.99)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_30.00_39.99_Years_Trading_0_2"));
			else if(claim_ratio >= 40.0)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_Above_40.00_Years_Trading_0_2"));
			break;
			
		case 3:
		case 4:
			
			if(claim_ratio == 0)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_0_Years_Trading_3_4"));
			else if(claim_ratio >= 0.01 && claim_ratio <= 9.99)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_0.01_9.99_Years_Trading_3_4"));
			else if(claim_ratio >= 10.00 && claim_ratio <= 19.99)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_10.00_19.99_Years_Trading_3_4"));
			else if(claim_ratio >= 20.00 && claim_ratio <= 29.99)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_20.00_29.99_Years_Trading_3_4"));
			else if(claim_ratio >= 30.00 && claim_ratio <= 39.99)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_30.00_39.99_Years_Trading_3_4"));
			else if(claim_ratio >= 40.0)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_Above_40.00_Years_Trading_3_4"));
			break;
			
		case 5:
			
			if(claim_ratio == 0)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_0_Years_Trading_eqlAbov_5"));
			else if(claim_ratio >= 0.01 && claim_ratio <= 9.99)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_0.01_9.99_Years_Trading_eqlAbov_5"));
			else if(claim_ratio >= 10.00 && claim_ratio <= 19.99)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_10.00_19.99_Years_Trading_eqlAbov_5"));
			else if(claim_ratio >= 20.00 && claim_ratio <= 29.99)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_20.00_29.99_Years_Trading_eqlAbov_5"));
			else if(claim_ratio >= 30.00 && claim_ratio <= 39.99)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_30.00_39.99_Years_Trading_eqlAbov_5"));
			else if(claim_ratio >= 40.0)
				return Double.parseDouble(PLEL_Rater.getProperty(coverName+"_CD_GLR_Above_40.00_Years_Trading_eqlAbov_5"));
			break;
			
			
			
	}

	
	}catch(Throwable t){
		System.out.println("Error while calculating Claims discount - "+t);
	}
	return total_claims_discount;
	
	
	
	
}

public double get_Claims_Ratio_cent(Map<Object,Object> data_map){
	
	double loss_Ratio=0.0;
	
	loss_Ratio = Double.parseDouble((String)data_map.get("CE_TotalLossRatio"));
		
	return loss_Ratio;
	
}
//From Years Business Established
public int get_Years_Trading(){
	
	PLEL_Rater = OR.getORProperties();
	
	Map<Object,Object>data_map = null;
	
	switch(common.currentRunningFlow){
		case "NB":
			data_map = common.NB_excel_data_map;
			break;
		case "Renewal":
			data_map = common.Renewal_excel_data_map;
			break;
	}
	
	
	int currentYear = Integer.parseInt(PLEL_Rater.getProperty("CurrentYear"));
	int yearEstd = Integer.parseInt((String)data_map.get("PG_YearEstablished"));
	
	switch(currentYear - yearEstd){
		case 0:
			return 0;
		case 1:
			return 1;
		case 2:
			return 2;
		case 3:
			return 3;
		case 4:
			return 4;
		default:
			return 5;
		
	}
	
}
//This method calculates total discount
public void calculate_Total_Discounts(String coverName){
	
	
 	double total_PL_Discount = calculate_Wage_Size_Discount(coverName) + calculate_Areas_of_Work_Discount(coverName) + calculate_Claims_discount(coverName);
	common_COB.Book_rate_Rater_output.put("Total_PL_Discount", total_PL_Discount);
}

//This method calculates total Plant discount
public void calculate_Plant_Total_Discounts(String coverName){
	
	double total_PL_Discount = 0.0;
	
	if(coverName.equals("PL")){
		total_PL_Discount = calculate_Turnover_Size_Discount(coverName) + calculate_Claims_discount(coverName);
	}else if(coverName.equals("EL")){
		total_PL_Discount = calculate_Wage_Size_Discount(coverName) + calculate_Claims_discount(coverName);
	}
	
	common_COB.Book_rate_Rater_output.put("Total_PL_Discount", total_PL_Discount);
}

//This method calculates total Plant discount
public void calculate_Works_Total_Discounts(String coverName){
	
	double total_PL_Discount = 0.0;
	total_PL_Discount = calculate_Turnover_Size_Discount(coverName) + calculate_Claims_discount(coverName);
	common_COB.Book_rate_Rater_output.put("Total_PL_Discount", total_PL_Discount);
}


public double calculate_Height_Depth_Heat_Load(String load_type,String coverName){
	
	double final_load=0.0;
	String sectionName = null;
	double centValue = 0.0;

	Map<Object,Object> data_map = null;
	
	switch(common.currentRunningFlow){
	
		case "NB":
			data_map = common.NB_excel_data_map;
			break;
		case "Renewal":
			data_map = common.Renewal_excel_data_map;
			break;
	
	}
	
	List<WebElement> wage_work_activities = driver.findElements(By.xpath("//select[contains(@name,'coa_empl_break_activity_2')]//option[@selected='selected']")); 
	int total_selected_elm = wage_work_activities.size();
	
	List<WebElement> cent_wage_work_activities = driver.findElements(By.xpath("//input[contains(@name,'coa_empl_break_percentage_total')]")); 
	
	for(int i=0;i<total_selected_elm;i++){
		
		sectionName = wage_work_activities.get(i).getAttribute("value");
		if(sectionName.contains(load_type)){
			centValue = Double.parseDouble(cent_wage_work_activities.get(i).getAttribute("value"));
			final_load = final_load + get_Height_Depth_Heat_load(sectionName,centValue,data_map,coverName);	
		}else{
			continue;
		}
	}
	
	//default heat logic
	if(common_COB.isIncludingHeatPresent && !common_COB.isExcludingHeatPresent){
		if(!common_COB.isIncludeHeat_10M){
			centValue = 100 - common_COB.cent_work_including_heat;
			final_load = final_load + get_default_Heat_load("Heat Exluding - not using heat or fire ",centValue,data_map,coverName);
		}else{
			
			final_load = final_load + 0; //IF PPL LOI >10M and Include heat is present
		}
	}
		
	return final_load;
	
}



public double get_Height_Depth_Heat_load(String wage_work_activity,double cent_wage_work,Map<Object,Object> data_map,String coverName){
	
	double wage_work_load = 0.0;
	PLEL_Rater = OR.getORProperties();
	
	try{
	String activity =null;
	if(wage_work_activity.contains("Height"))
			activity = "Height";
	else if(wage_work_activity.contains("Depth"))
			activity = "Depth";
	else if(wage_work_activity.contains("heat"))
		activity = "Heat";
	
	
	if(coverName.equals("EL") && activity.equals("Heat")){
		return wage_work_load;
	}
	
	switch(activity){
	
		case "Height":
			if(wage_work_activity.contains("up to 10 metres"))
				wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty(coverName+"_HLL_0_10M")));
			else
				wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty(coverName+"_HLL_over_10M")));
			break;
			
		case "Depth":
			if(wage_work_activity.contains("up to 1 metre"))
				wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty(coverName+"_DLL_0_1M")));
			else if(wage_work_activity.contains("between 1 and 3 metres"))
				wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty(coverName+"_DLL_1M_3M")));
			else if(wage_work_activity.contains("between 3 and 5 metres"))
				wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty(coverName+"_DLL_3M_5M")));
			else if(wage_work_activity.contains("over 5 metres"))
				wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty(coverName+"_DLL_over_5M")));
			break;
			
		case "Heat":
			//Including Heat
			String PPL_LOI = driver.findElement(By.xpath("//*[contains(@name,'coa_pl_pl_indemnity')]/option[@selected='selected']")).getText();
			switch(PPL_LOI){
			
				case "£1,000,000":
					
					if(wage_work_activity.contains("Work using heat")){
						wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty("Including_Heat_1M")));
						common_COB.cent_work_including_heat = common_COB.cent_work_including_heat + cent_wage_work;
						common_COB.isIncludingHeatPresent=true;
					}
					else if(wage_work_activity.contains("not using heat or fire")){
						wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty("Excluding_Heat_1M")));
						common_COB.isExcludingHeatPresent=true;
					}
			
					break;
					
				case "£2,000,000":
					
					if(wage_work_activity.contains("Work using heat")){
						wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty("Including_Heat_2M")));
						common_COB.cent_work_including_heat = common_COB.cent_work_including_heat + cent_wage_work;
						common_COB.isIncludingHeatPresent=true;
					}
					else if(wage_work_activity.contains("not using heat or fire")){
						wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty("Excluding_Heat_2M")));
						common_COB.isExcludingHeatPresent=true;
					}
			
					break;
					
				case "£5,000,000":
					
					if(wage_work_activity.contains("Work using heat")){
						wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty("Including_Heat_5M")));
						common_COB.cent_work_including_heat = common_COB.cent_work_including_heat + cent_wage_work;
						common_COB.isIncludingHeatPresent=true;
					}
					else if(wage_work_activity.contains("not using heat or fire")){
						wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty("Excluding_Heat_5M")));
						common_COB.isExcludingHeatPresent=true;
					}
			
					break;
					
				case "£10,000,000":
				case "£15,000,000":
				case "£20,000,000":
				case "£25,000,000":
					
					if(wage_work_activity.contains("Work using heat")){
						common_COB.isIncludeHeat_10M=true;
						wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty("Including_Heat_10M")));
						common_COB.cent_work_including_heat = common_COB.cent_work_including_heat + cent_wage_work;
						common_COB.isIncludingHeatPresent=true;
					}
					else if(wage_work_activity.contains("not using heat or fire")){
						wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty("Excluding_Heat_10M")));
						common_COB.isExcludingHeatPresent=true;
					}
			
					break;
			}
			
			
			break;//Main Switch
			
		
			default:
				System.out.println("* Invalid Wage Work *");
				break;
	}
	
	}catch(Throwable t){
		return 0.0;
	}
	wage_work_load = wage_work_load/100;
	return wage_work_load;
	
}

public double get_default_Heat_load(String wage_work_activity,double cent_wage_work,Map<Object,Object> data_map,String coverName){
	
	double wage_work_load = 0.0;
	PLEL_Rater = OR.getORProperties();
	
	if(coverName.equals("EL") && wage_work_activity.contains("Heat")){
		return wage_work_load;
	}
	
	
		String PPL_LOI = driver.findElement(By.xpath("//*[contains(@name,'coa_pl_pl_indemnity')]/option[@selected='selected']")).getText();
		switch(PPL_LOI){
	
				case "£1,000,000":
					
					if(wage_work_activity.contains("not using heat or fire")){
						wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty("Excluding_Heat_1M")));
						
					}
			
					break;
					
				case "£2,000,000":
					
					if(wage_work_activity.contains("not using heat or fire")){
						wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty("Excluding_Heat_2M")));
					}
			
					break;
					
				case "£5,000,000":
					
					if(wage_work_activity.contains("not using heat or fire")){
						wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty("Excluding_Heat_5M")));
					}
			
					break;
					
				case "£10,000,000":
				case "£15,000,000":
				case "£20,000,000":
				case "£25,000,000":
					
					if(wage_work_activity.contains("not using heat or fire")){
						wage_work_load = cent_wage_work * (Double.parseDouble(PLEL_Rater.getProperty("Excluding_Heat_10M")));
					}
			
					break;
			}

			wage_work_load = wage_work_load/100;
			System.out.println("Default heat load ="+wage_work_load);
			return wage_work_load;
	
}

public double calculate_Years_Business_Established_Load(){
	
	double load_value=0.0;
	PLEL_Rater = OR.getORProperties();
	
	int getYearsTrading = get_Years_Trading();
	
	switch(getYearsTrading){
	
		case 0:
			load_value = Double.parseDouble(PLEL_Rater.getProperty("Less_than_One_Year"));
			break;
		case 1:
			load_value = Double.parseDouble(PLEL_Rater.getProperty("Less_than_Two_Years"));
			break;
		case 2:
			load_value = Double.parseDouble(PLEL_Rater.getProperty("Less_than_Three_Years"));
			break;
		default:
			load_value = 0.0;
			break;
	
	}
	
	return load_value;
	
}

public double calculate_Works_LOI_Load(String coverName){
	
	String final_load=null;

	String works_LOI = k.getAttribute("COB_works_LOI", "value").replaceAll(",", "");
	int _works_LOI = Integer.parseInt(works_LOI);
	
	if(_works_LOI >= 0 && _works_LOI <= 1000000){
		final_load = PLEL_Rater.getProperty("Works_LOI_0M_1M");
	}else if(_works_LOI > 1000000 && _works_LOI <=2500000){
		final_load = PLEL_Rater.getProperty("Works_LOI_1M_2.5M");
	}else if(_works_LOI > 2500000 && _works_LOI <= 5000000){
		final_load = PLEL_Rater.getProperty("Works_LOI_2.5M_5M");
	}else if(_works_LOI > 5000000 && _works_LOI <= 10000000){
		final_load = PLEL_Rater.getProperty("Works_LOI_2.5M_5M");
	}else{
		final_load = "0.0";
	}
	
	
	return Double.parseDouble(final_load);
	
}

public double calculate_Works_Timber_Frame_Load(String coverName){
	
	String final_load=null;
	
	k.scrollInView("COB_AW_ErectionOfTimberFrame");
	String timber_Frame_Option = k.GetDropDownSelectedValue("COB_AW_ErectionOfTimberFrame");
	if(timber_Frame_Option.equalsIgnoreCase("Yes")){
		final_load = PLEL_Rater.getProperty("Works_Timber_Frame_Load");
	}else{
		final_load = "0.0";
	}
	
	
	return Double.parseDouble(final_load);
	
}

public double calculate_Works_DE5_4_extension_Load(String coverName){
	
	String final_load="0.0";
	
	String bespoke_xpath = "//a[text()='Add Bespoke Item']//following::table[@id='table0']";
	WebElement bespoke_Table = driver.findElement(By.xpath(bespoke_xpath));
	
	k.ScrollInVewWebElement(bespoke_Table);
	
	int _tble_Rows = driver.findElements(By.xpath("//a[text()='Apply Book Rates']//following::table[@id='table0']//tbody//tr")).size();
	String sectionValue = null;

	
	for(int row = 1; row < _tble_Rows ;row ++){
		WebElement sec_Val = driver.findElement(By.xpath(bespoke_xpath+"//tbody//tr["+row+"]//td["+1+"]"));
		sectionValue = sec_Val.getText();
		if(sectionValue.contains("DE")){
			final_load = PLEL_Rater.getProperty("Works_DE5_4_extension_Load");
			break;
		}else{
			continue;
		}
	}
	
	
	return Double.parseDouble(final_load);
	
}



public double get_Bona_Fide_bookRate(){
	
	
	double final_Bf_book_rate = 0.0;
	String sectionName = null;
	int annualWagerollalue = 0;
	double wage_weight=0.0;
	double book_rate = 0.0;
	double multiplie_br = 0.0;
	double add_all = 0.0,multipli_with_rate=0.0;
	try{
	
	PLEL_Rater = OR.getORProperties();

	
	int total_Wages=0;
	
		List<WebElement> employeeWages = driver.findElements(By.xpath("//input[contains(@name,'coa_empl_break_tc_activity_tcode')]")); 
		int total_EWages = employeeWages.size();
	
		List<WebElement> cent_employeeWages = driver.findElements(By.xpath("//input[contains(@name,'coa_bd_wageroll_tcode')]")); 
	
		for(int i=0;i<total_EWages;i++){
		 
			sectionName = employeeWages.get(i).getAttribute("value");
			annualWagerollalue = Integer.parseInt(cent_employeeWages.get(i).getAttribute("value"));
			total_Wages = total_Wages + annualWagerollalue;
		}
		
		//Wage Percent of Each Trade
		for(int i=0;i<total_EWages;i++){
			 
			sectionName = employeeWages.get(i).getAttribute("value");
			annualWagerollalue = Integer.parseInt(cent_employeeWages.get(i).getAttribute("value"));
			wage_weight = ((float)annualWagerollalue / total_Wages);
			book_rate = get_book_rate_from_table(sectionName);
			multiplie_br = wage_weight * book_rate;
			add_all = add_all + multiplie_br;
			
		}
		
		multipli_with_rate = add_all * Double.parseDouble(PLEL_Rater.getProperty("Bona_Fide_Sub_Contractors_cent_of_PL_rate_PL_NBR"));
		final_Bf_book_rate = multipli_with_rate/100;
			
	}catch(StaleElementReferenceException se){
		get_Bona_Fide_bookRate();
	}
	catch(Throwable t){
		System.out.println("Error while Calculating Bona Fide book rate - "+t);
	}
	System.out.println("Bona Fide book rate = "+final_Bf_book_rate);
	return final_Bf_book_rate;
	
}

//This method calculates total Loads
public void calculate_Total_Loads(String coverName){
	
	
	double total_PL_Loads = calculate_Height_Depth_Heat_Load("Height",coverName) + calculate_Height_Depth_Heat_Load("Depth",coverName) + 
							calculate_Height_Depth_Heat_Load("heat",coverName) + calculate_Years_Business_Established_Load();
	System.out.println("Total PL Loads = "+total_PL_Loads);
	common_COB.Book_rate_Rater_output.put("Total_PL_Loads", total_PL_Loads);
}
public void calculate_Plant_Total_Loads(String coverName){
	
	double total_PL_Loads = 0.0;
	
	if(coverName.equals("PL")){
		total_PL_Loads = calculate_Height_Depth_Heat_Load("heat",coverName) + calculate_Years_Business_Established_Load();
	}else if(coverName.equals("EL")){
		total_PL_Loads = calculate_Years_Business_Established_Load();
	}
	
	
	System.out.println("Total PL Loads = "+total_PL_Loads);
	common_COB.Book_rate_Rater_output.put("Total_PL_Loads", total_PL_Loads);
}
public void calculate_Works_Total_Loads(String coverName){
	
	double total_PL_Loads = 0.0;
	
	total_PL_Loads = calculate_Works_LOI_Load(coverName) + calculate_Works_Timber_Frame_Load(coverName) + calculate_Works_DE5_4_extension_Load(coverName);
	
	System.out.println("Total PL Loads = "+total_PL_Loads);
	common_COB.Book_rate_Rater_output.put("Total_PL_Loads", total_PL_Loads);
}

public void calculate_Multiplying_Factor(String coverName){
	
	double total_Load_Discount_aplied = 0.0;
	double multiplying_Factor = 0.0;
	
	
	if(coverName.equalsIgnoreCase("Works")){
		
		calculate_Works_Total_Discounts(coverName);
		calculate_Works_Total_Loads(coverName);
		
	}else{
		calculate_Total_Discounts(coverName);
		calculate_Total_Loads(coverName);
	}
	
	
	double total_Loads = common_COB.Book_rate_Rater_output.get("Total_PL_Loads");
	double total_Discount = common_COB.Book_rate_Rater_output.get("Total_PL_Discount");
	
	
	if((total_Loads - total_Discount) > 0){
		
		total_Load_Discount_aplied = total_Loads - total_Discount;
		multiplying_Factor = 1 + (total_Load_Discount_aplied/100);
	}else{
		total_Load_Discount_aplied = Math.abs(total_Loads - total_Discount);
		multiplying_Factor = 1 - (total_Load_Discount_aplied/100);
	
	}
	System.out.println("Total MF = "+multiplying_Factor);
	common_COB.Book_rate_Rater_output.put("Multiplying_Factor", multiplying_Factor);
}

public void calculate_Plant_Multiplying_Factor(String coverName){
	
	double total_Load_Discount_aplied = 0.0;
	double multiplying_Factor = 0.0;
	
		
	calculate_Plant_Total_Discounts(coverName);
	calculate_Plant_Total_Loads(coverName);
	
	double total_Loads = common_COB.Book_rate_Rater_output.get("Total_PL_Loads");
	double total_Discount = common_COB.Book_rate_Rater_output.get("Total_PL_Discount");
	
	
	if((total_Loads - total_Discount) > 0){
		
		total_Load_Discount_aplied = total_Loads - total_Discount;
		multiplying_Factor = 1 + (total_Load_Discount_aplied/100);
	}else{
		total_Load_Discount_aplied = Math.abs(total_Loads - total_Discount);
		multiplying_Factor = 1 - (total_Load_Discount_aplied/100);
	
	}
	System.out.println("Total MF = "+multiplying_Factor);
	common_COB.Book_rate_Rater_output.put("Multiplying_Factor", multiplying_Factor);
}

public double get_PL_Book_Rate(String trade_Activity,String coverName){
	
	double book_rate = 0.0,net_Base_Rate=0.0;
	String activity = null;
	if(trade_Activity.contains("Plant")){
		calculate_Plant_Multiplying_Factor(coverName);
	}else{
		calculate_Multiplying_Factor(coverName);
	}
	
	double multiplying_f = common_COB.Book_rate_Rater_output.get("Multiplying_Factor");
	
	if(trade_Activity.contains("Work Away")){
		activity = trade_Activity.substring("Work Away - ".length());
	}else {	
		activity = trade_Activity;
	}
	net_Base_Rate = get_Net_Base_Rate(activity,coverName);
	
	book_rate = multiplying_f * net_Base_Rate;
	
	System.out.println("Book Rate of "+activity + " = "+book_rate);
	common_COB.Book_rate_Rater_output.put(coverName+"_"+trade_Activity, book_rate);
	return book_rate;
		
}


public double get_Net_Base_Rate(String trade_Activity,String cover){
	
	double r_value=0.0;
	DecimalFormat formatter = new DecimalFormat("###.####");
	
	PLEL_Rater = OR.getORProperties();
	String t_activity = trade_Activity.replaceAll(" -", "").replaceAll(" ", "_").replaceAll(",", "");
	t_activity = t_activity + "_"+cover + "_NBR";
	
	r_value = Double.parseDouble(PLEL_Rater.getProperty(t_activity));
	r_value = Double.valueOf(formatter.format(r_value));
	
	return r_value;
		
}


public boolean calculate_Book_Rate(String coverName){
	
	try{
	//double book_rate = 0.0;
	double bonaFide_book_rate=0.0;
		
	switch(coverName){
	
	case "PL": //Public & Products Liability
		
		List<String> pl_activites = get_PL_Activities();
		for(String activity : pl_activites){
			if(activity.contains("Bona Fide")){
				bonaFide_book_rate = get_Bona_Fide_bookRate();
				common_COB.Book_rate_Rater_output.put(coverName+"_"+activity, bonaFide_book_rate);
			}else if(activity.contains("Asbestos") || activity.contains("CPA Contract Lift Cover")) {
				common_COB.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
				continue;
			}else{
				
				get_PL_Book_Rate(activity,coverName);
				common_COB.isIncludingHeatPresent = false;
				common_COB.isExcludingHeatPresent = false;
				common_COB.cent_work_including_heat=0.0;
				
			}
		}
		TestUtil.reportStatus("Book Rate for Public and Products Liability Cover calculated Successfully . ", "Info", false);
		
		break;
		
	case "EL": //Employers Liability
		
		List<String> el_activites = get_PL_Activities();
		for(String activity : el_activites){
			if(activity.contains("Asbestos") || activity.contains("Offshore Work & Visits") 
					|| activity.contains("Overseas Work")) {
				common_COB.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
				continue;
			}else{
				get_PL_Book_Rate(activity,coverName);
				common_COB.isIncludingHeatPresent = false;
				common_COB.isExcludingHeatPresent = false;
				common_COB.cent_work_including_heat=0.0;
			}
		}
		TestUtil.reportStatus("Book Rate for Employers Liability Cover calculated Successfully . ", "Info", false);
		
		break;
		
		
	case "Works": //Annual Works
		
		List<String> aw_activites = get_PL_Activities();
		for(String activity : aw_activites){
			if(activity.contains("DE") || activity.contains("Existing Structures") || activity.contains("Other")){
				common_COB.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
			}else {
				
				get_PL_Book_Rate(activity,coverName);
				common_COB.isIncludingHeatPresent = false;
				common_COB.isExcludingHeatPresent = false;
				common_COB.cent_work_including_heat=0.0;
				
			}
		}
		TestUtil.reportStatus("Book Rate for Public and Products Liability Cover calculated Successfully . ", "Info", false);
		
		break;
		
	
	}	
		
	
	}catch(Throwable t){
		return false;
	}
	
	
	System.out.println("Book Rate lookup Outputs = "+common_COB.Book_rate_Rater_output);
	return true;
	
}

public double get_book_rate_from_table(String tradeActivity){
	
	double book_rate=0.0;
	int index_book_Rate_Cent_col=0;
	
	try{
	
	String bookRate_xpath = "//a[text()='Apply Book Rates']//following::table[@id='table0']";
	WebElement bookRate_Table = driver.findElement(By.xpath(bookRate_xpath));
	
	k.ScrollInVewWebElement(bookRate_Table);
	
	int _tble_Headers = driver.findElements(By.xpath("//a[text()='Apply Book Rates']//following::table[@id='table0']//thead//th")).size();
	for(int row = 1; row < _tble_Headers ;row ++){
		
		WebElement sec_Val = driver.findElement(By.xpath(bookRate_xpath+"//thead//th["+row+"]"));
		if(sec_Val.getText().equalsIgnoreCase("Book Rate (%)")){
			index_book_Rate_Cent_col = row;
			break;
		}
	}
	
	int _tble_Rows = driver.findElements(By.xpath("//a[text()='Apply Book Rates']//following::table[@id='table0']//tbody//tr")).size();

	for(int row = 1; row < _tble_Rows ;row ++){
	
		WebElement sec_Val = driver.findElement(By.xpath(bookRate_xpath+"//tbody//tr["+row+"]//td["+1+"]"));
		if(sec_Val.getText().equals(tradeActivity)){
			WebElement sce_cent = driver.findElement(By.xpath(bookRate_xpath+"//tbody//tr["+row+"]//td["+index_book_Rate_Cent_col+"]//input"));
			book_rate = Double.parseDouble(sce_cent.getAttribute("value"));
			break;
		}
		
	}
	
	}catch(Throwable t){
		System.out.println("Error while getting book rate from table "+t);
	}
	return book_rate;
}

public List<String> get_PL_Activities(){
	
	List<String> pl_activites = new ArrayList<>();
	
	String bookRate_xpath = "//a[text()='Apply Book Rates']//following::table[@id='table0']";
	WebElement bookRate_Table = driver.findElement(By.xpath(bookRate_xpath));
	
	k.ScrollInVewWebElement(bookRate_Table);
	
	int _tble_Rows = driver.findElements(By.xpath("//a[text()='Apply Book Rates']//following::table[@id='table0']//tbody//tr")).size();
	String sectionValue = null;

	
	for(int row = 1; row < _tble_Rows ;row ++){
	
	
		WebElement sec_Val = driver.findElement(By.xpath(bookRate_xpath+"//tbody//tr["+row+"]//td["+1+"]"));
		sectionValue = sec_Val.getText();

		pl_activites.add(sectionValue);
	
	}
	
	return pl_activites;
	
}

//************************END************************************//
//************************************************************//
//**** PL EL Book Rate Calculator Based on CPRS Sheet ********//
//************************************************************//
//************************************************************//

/**
 * 
 * This method gives MF&D pages referrals texts."
 * 
 *
 */
public boolean func_Referrals_MaterialFactsDeclerationPage(){
	
	boolean retValue = true;
	
	Map<Object,Object> map_data=null;
	
	switch(common.currentRunningFlow){
		case "NB":
			map_data = common.NB_excel_data_map;
			break;
		case "MTA":
			map_data = common.MTA_excel_data_map;
			break;
		case "Renewal":
			map_data = common.Renewal_excel_data_map;
			break;
	}
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Material Facts & Declarations", ""),"Material Facts and Declarations page is having issue(S)");
		 k.ImplicitWaitOff();
		 String mfd_q_value = null,mf_key="RM_MaterialFactsandDeclarations_";
		 
		 //Work with silica, asbestos, or substances containing asbestos?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_containingAsbestos");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"containingAsbestos"));
		 
		 //Work with acids, gases, chemicals, explosives, radioactive or similar dangerous liquids or substances?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_dangerousLiquidsORsubstances");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"dangerousLiquidsORsubstances"));
		 
		 //Work on power stations, nuclear installations or establishments?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_nuclearInstallations");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"nuclearInstallations"));
		 
		 //Work on refineries, bulk storage, or premises in oil, gas or chemical industries?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_gasORchemicalIndustries");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"gasORchemicalIndustries"));
		 
		 //Work airside or on aircraft, hovercraft, aerospace systems, watercraft, railway, underground, quarries, underwater at docks, harbours or piers?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_harboursORpiers");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"harboursORpiers"));
			 
		 //Work in or on computer suites or on computers?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_onComputers");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"onComputers"));
		
		 //Work on Bridges, viaducts, towers, steeples, chimney shafts or blast furnaces?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_blastFurnace");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"blastFurnace"));
		
		 	 
		 //Do you source products from outside of the EU?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_outside_of_the_EU");
		 if(mfd_q_value.equalsIgnoreCase("Yes")){
			 //Products Liability Material facts - Multi-select list
			 try{
				 List<WebElement> _outside_eu_MF = driver.findElements(By.xpath("//*[text()='Please select all relevant regions']//following::ul[1]//li"));
				 String eu_value =null;
				 for(WebElement prd_elm:_outside_eu_MF){
					 try{
						 eu_value = prd_elm.getAttribute("title");
					 }catch(Throwable t){
						 eu_value="None";
						 
						 }
					 
					 if(!eu_value.equalsIgnoreCase("None") && !eu_value.isEmpty()){
						 common_VELA.referrals_list.add("Material Facts & Declarations_Products sourced from outside of the EU: "+eu_value);
					 }
					 
				 }
			 }catch(Throwable t){
				 
			 }
			 
		 }
		 
		 //Products Liability Material facts - Multi-select list
		 try{
			 List<WebElement> _prd_liability_MF = driver.findElements(By.xpath("//*[text()='Products Liability Material facts']//following::ul[1]//li"));
			 String prd_value =null;
			 for(WebElement prd_elm:_prd_liability_MF){
				 try{
					 prd_value = prd_elm.getAttribute("title");
				 }catch(Throwable t){
					 prd_value="None";
					 
					 }
				 
				 if(!prd_value.equalsIgnoreCase("None") && !prd_value.isEmpty()){
					 common_VELA.referrals_list.add("Material Facts & Declarations_"+prd_value);
				 }
				 
			 }
		 }catch(Throwable t){
			 
		 }
		
		 //Are any products supplied to hazardous industries?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_hazardousIndustries");
		 if(mfd_q_value.equalsIgnoreCase("Yes")){
		 // Please select all relevant industries - Multi Select list
		 try{
			 List<WebElement> _relevant_industries = driver.findElements(By.xpath("//*[text()='Please select all relevant industries']//following::ul[1]//li"));
			 String _relevant_industries_vlaue =null;
			 for(WebElement prd_elm:_relevant_industries){
				 try{
					 _relevant_industries_vlaue = prd_elm.getAttribute("title");
				 }catch(Throwable t){
					 _relevant_industries_vlaue="None";
					 
					 }
				 
				 if(!_relevant_industries_vlaue.equalsIgnoreCase("None") && !_relevant_industries_vlaue.isEmpty()){
					 common_VELA.referrals_list.add("Material Facts & Declarations_Products supplied to hazardous industries: "+_relevant_industries_vlaue);
				 }
				 
			 }
			
		 }catch(Throwable t){
			 
		 }
		  
		 }
		 
		 return retValue;
		 
	}catch(Throwable t){
		k.ImplicitWaitOn();
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     
        return false;
		}
	finally{
		 k.ImplicitWaitOn();
	 }
	
	}

public boolean MaterialFactsDeclerationPage(){
	boolean retValue = true;
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Material Facts & Declarations", ""),"Material Facts and Declarations page is having issue(S)");
		 k.ImplicitWaitOff();
		 String q_value = null;
		 
		List<WebElement> elements = driver.findElements(By.className("selectinput"));
		 Select sel = null;
		 String sInput = null;
		
		 for(int i = 0;i<elements.size();i++){
			 if(elements.get(i).isDisplayed()){
				 if(elements.get(i).getAttribute("name").contains("_proposer_")){
					 sel = new Select(elements.get(i));
					 try{
						 
						 q_value = (String)common.NB_excel_data_map.get("MFD_Q"+(i+1));
						 
						 sel.selectByVisibleText(q_value);
					 }
					 catch(Throwable t){
						 //sel.selectByVisibleText("No");
					 } 
				 }else if(elements.get(i).getAttribute("name").contains("_work")){
					 sel = new Select(elements.get(i));
					 try{
						 sInput = elements.get(i).getAttribute("name");
						 q_value = (String)common.NB_excel_data_map.get(sInput);
						 sel.selectByVisibleText(q_value);
					 }
					 catch(Throwable t){
						 //sel.selectByVisibleText("No");
					 } 
				 }else {
					 sel = new Select(elements.get(i));
					 try{
						 sInput = elements.get(i).getAttribute("name");
						 q_value = (String)common.NB_excel_data_map.get(sInput);
						 sel.selectByVisibleText(q_value);
					 }
					 catch(Throwable t){
						 //sel.selectByVisibleText("No");
					 } 
				 }
		 }
		 }	 
		 ////*[contains(@name,'_work')]
		
		  
		 
//		if(((String)common.NB_excel_data_map.get("MFD_Q10")).equalsIgnoreCase("Yes")){
//			 String[] Industries = ((String)common.NB_excel_data_map.get("MFD_Industries")).split(",");
//				List<WebElement> ul_elements_Industries = driver.findElements(By.xpath("//ul"));
//				for(String geo_limit : Industries){
//					for(WebElement each_ul : ul_elements_Industries){
//						customAssert.assertTrue(k.Click("XOC_MFD_Industries"),"Error while Clicking Geographic Limit List object . ");
//						k.waitTwoSeconds();
//						if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
//							each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
//						else
//							continue;
//						break;
//					}
//				}
//		 }
//		 if(((String)common.NB_excel_data_map.get("MFD_Q12")).equalsIgnoreCase("Yes")){
//			 String[] Regoins = ((String)common.NB_excel_data_map.get("MFD_Regoins")).split(",");
//				List<WebElement> ul_elements_Regoins = driver.findElements(By.xpath("//ul"));
//				for(String geo_limit : Regoins){
//					for(WebElement each_ul : ul_elements_Regoins){
//						customAssert.assertTrue(k.Click("XOC_MFD_Regoins"),"Error while Clicking Geographic Limit List object . ");
//						k.waitTwoSeconds();
//						if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
//							each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
//						else
//							continue;
//						break;
//					}
//				}
//		 }
//		 xoc_details_
		 if(k.isDisplayed("XOC_MFD_ProductLiability")){
			 
			 String[] MDF_Limits = ((String)common.NB_excel_data_map.get("XOC_MFD_ProductLiability")).split(",");
				List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
				for(String ins_limit : MDF_Limits){
					for(WebElement each_ul : ul_elements){
						customAssert.assertTrue(k.Click("XOC_MFD_ProductLiability"),"Error while Clicking Products Liability Material facts List object . ");
						k.waitTwoSeconds();
						if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed())
							each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
						else
							continue;
						break;
					}
				}
			 
				List<WebElement> det_elements = driver.findElements(By.xpath("//*[starts-with(@name,'xoc_details_')]"));
					for(WebElement each_el : det_elements){
						if(each_el.isDisplayed())
							each_el.sendKeys("Test input");
					}
				
					 
		 }
		 List<WebElement> txt_Elements = driver.findElements(By.className("write"));
		 q_value = (String)common.NB_excel_data_map.get("MFD_T");
		 WebElement ws = null; 
		 
		 for(int i = 0;i<txt_Elements.size();i++){
			 ws = txt_Elements.get(i);
			 if(ws.isDisplayed()){
				 ws.sendKeys(q_value);
			 }			 
		 }
		 
		 customAssert.assertTrue(common.funcButtonSelection("Save"), "Unable to click on Save Button on Material Facts and Declarations Screen .");
		 
		 return retValue;
		 
	}catch(Throwable t){
		k.ImplicitWaitOn();
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     
        return false;
		}
	finally{
		 k.ImplicitWaitOn();
	 }
	
	}

public void RenewalRewindFlow(String code,String event) throws ErrorInTestMethod{
	String testName = (String)common.Rewind_excel_data_map.get("Automation Key");
	try{
		common.currentRunningFlow="Rewind";
		String navigationBy = CONFIG.getProperty("NavigationBy");
		common_HHAZ.PremiumFlag = false;
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common.funcRewind());
		
		TestUtil.reportStatus("<b> -----------------------Renewal Rewind flow is started---------------------- </b>", "Info", false);
		
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
		customAssert.assertTrue(common.funcSearchPolicy(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		if(TestBase.businessEvent.equals("MTA")){
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status  Endorsement Submitted (Rewind) function is having issue(S) . ");
		}else{
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Renewal Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		}		
		customAssert.assertTrue(common_XOC.funcPolicyGeneral(common.Rewind_excel_data_map,code,event), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(funcPreviousClaims(common.Rewind_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(common_VELA.funcCovers(common.Rewind_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(common_XOC.MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"General Risk Details"),"Issue while Navigating to General Risk Details screen . ");
		customAssert.assertTrue(common_XOC.GeneralRiskDetailsPage(common.Rewind_excel_data_map));
		if(((String)common.Rewind_excel_data_map.get("CD_ThirdPartyMotorLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Third Party Motor Liability"),"Issue while Navigating to Third Party Motor Liability screen . ");
			customAssert.assertTrue(common_XOC.ThirdPartyMotorLiability(common.Rewind_excel_data_map), "Issue with  Third Party Motor Liability function");
		}
		if(((String)common.Rewind_excel_data_map.get("CD_CombinedLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Combined Liability"),"Issue while Navigating to Combined Liabilit screen . ");
			customAssert.assertTrue(common_XOC.CombinedLiability(common.Rewind_excel_data_map), "Issue with  CombinedLiability function");
		}
		if(((String)common.Rewind_excel_data_map.get("CD_Public&ProductsLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen . ");
			customAssert.assertTrue(common_XOC.PublicProductsLiabilityPage(common.Rewind_excel_data_map), "Issue with  Public & Products Liability function");
		}
		if(((String)common.Rewind_excel_data_map.get("CD_EmployersLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.EmployersLiabilityPage(common.Rewind_excel_data_map), "Issue with  Employers Liability function");
		}
		if(((String)common.Rewind_excel_data_map.get("CD_JCT6.5.1")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.JCTPage(common.Rewind_excel_data_map), "Issue with  Employers Liability function");
		}
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common_VELA.funcPremiumSummary(common.Rewind_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");	
		
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}


}
