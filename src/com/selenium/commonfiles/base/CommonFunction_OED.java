package com.selenium.commonfiles.base;

import static com.selenium.commonfiles.util.TestUtil.WriteDataToXl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.selenium.commonfiles.util.ErrorInTestMethod;
import com.selenium.commonfiles.util.ErrorUtil;
import com.selenium.commonfiles.util.ObjectMap;
import com.selenium.commonfiles.util.TestUtil;


public class CommonFunction_OED extends TestBase{

	SimpleDateFormat df = new SimpleDateFormat();
	Properties CCD_Rater = new Properties();
	Properties OED_Rater = new Properties();
	public Map<String,Double> Book_rate_Rater_output = new HashMap<>();
	public boolean isMTARewindFlow=false,isFPEntries=false,isMTARewindStarted=false, isNBRewindStarted = false, isNBRquoteStarted = false;
	public int actual_no_of_years=0,err_count=0,trans_error_val=0 , Can_returnP_Error=0;
	public List<String> CoversDetails_data_list = null, MD_Building_Occupancies_list=new ArrayList<>();
	public Map<String,Double> TC_Calculations = new HashMap<>();
	public boolean isIncludingHeatPresent = false,isExcludingHeatPresent = false,isIncludeHeat_10M=false;
	public double cent_work_including_heat = 0.0; 
	Date currentDate = new Date();
	public static int p_Index = 0;
	public static String Environment = null;
	public double JCT_TotalPremium = 0.0, OP_TotalPremium = 0.00, Ter_TotalPremium = 0.00; 
	public static String pas_NoOfEmp_AllOthers = null, pas_NoOfEmp_Clerical = null, pas_NoOfEmp_Drivers = null, LE_TotalWegroll = null;
	public static String Ter_BuildingContents_Sum = null;
	public double totalMD = 0.0, Ter_BI_Sum = 0.00;
	public boolean isGrossPremiumReferralCheckDone = false;
	public double totalPAPremium = 0.0;
	public Map<String,Map<String,Double>> CAN_CCD_ReturnP_Values_Map = new HashMap<>();
	public boolean 	PremiumFlag = false;
	public static List<String> BusinessActivity = new ArrayList<String>();
	public static String CIPPLining = "0.00";
	public void NewBusinessFlow(String code,String event) throws ErrorInTestMethod{
		
		String testName = (String)common.NB_excel_data_map.get("Automation Key");
		String navigationBy = CONFIG.getProperty("NavigationBy");
		common.currentRunningFlow = "NB";
		try{
			customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
			customAssert.assertTrue(common.checkClient(common.NB_excel_data_map,code,event),"Unable to check Client.");
			customAssert.assertTrue(common.createNewQuote(common.NB_excel_data_map,code,event), "Unable to create new quote.");
			customAssert.assertTrue(common.selectLatestQuote(common.NB_excel_data_map,code,event), "Unable to select quote from table.");
			customAssert.assertTrue(funcPolicyGeneral(common.NB_excel_data_map,code,event), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common_HHAZ.funcCovers(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcPreviousClaims(common.NB_excel_data_map), "Previous claim function is having issue(S) .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Product General"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcProductGeneral(common.NB_excel_data_map), "Previous claim function is having issue(S) .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
 			customAssert.assertTrue(common_HHAZ.funcPremiumSummary(common.NB_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
  			Assert.assertTrue(common_HHAZ.funcStatusHandling(common.NB_excel_data_map,code,"NB"));
			if(TestBase.businessEvent.equalsIgnoreCase("NB")){
				customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
				customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
				TestUtil.reportTestCasePassed(testName);
			}		
		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
		
	}
	public void MTAFlow(String code,String event) throws ErrorInTestMethod{
		String testName = (String)common.MTA_excel_data_map.get("Automation Key");
		try{
			 Map<Object, Object> data_map = new HashMap<>();
			switch(TestBase.businessEvent){
				case "NB":
					data_map = common.NB_excel_data_map;
					break;
				case "MTA":
					data_map = common.MTA_excel_data_map;
					break;
				case "Renewal":
					data_map = common.Renewal_excel_data_map;
					break;
				case "Rewind":
					data_map = common.Rewind_excel_data_map;
					break;
				case "Requote":
					data_map = common.Requote_excel_data_map;
					break;
			
			}
			if(((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy")).equalsIgnoreCase("Yes")) {
				customAssert.assertTrue(common_EP.ExistingPolicyAlgorithm(common.MTA_excel_data_map , (String)common.MTA_excel_data_map.get("MTA_ExistingPolicy_Type"), (String)common.MTA_excel_data_map.get("MTA_ExistingPolicy_Status")), "Existing Policy Algorithm function is having issues. ");
			}else {
				if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
					NewBusinessFlow(code,"NB");
				}
				common_CCD.MD_Building_Occupancies_list.clear();
				common_HHAZ.CoversDetails_data_list.clear();
				common_HHAZ.PremiumFlag = false;
			}

			common.currentRunningFlow="MTA";
			CommonFunction_HHAZ.AdjustedTaxDetails.clear();
			String navigationBy = CONFIG.getProperty("NavigationBy");
			customAssert.assertTrue(common_CCD.funcCreateEndorsement(),"Error in Create Endorsement function . ");
		
			TestUtil.reportStatus("<b> -----------------------MTA flow started---------------------- </b>", "Info", false);
			
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
			if(TestBase.businessEvent.equals("Renewal")){
				customAssert.assertTrue(common.funcSearchPolicy(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
			}else{
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			}
			customAssert.assertTrue(common.funcVerifyPolicyStatus(data_map,code,event,"Endorsement Submitted"), "Verify Policy Status (Endorsement Submitted) function is having issue(S) . ");
			customAssert.assertTrue(funcPolicyGeneral(common.MTA_excel_data_map,code,event), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common_HHAZ.funcCovers(common.MTA_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcPreviousClaims(common.MTA_excel_data_map), "Previous claim function is having issue(S) .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Product General"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcProductGeneral(common.MTA_excel_data_map), "Previous claim function is having issue(S) .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_HHAZ.funcPremiumSummary_MTA(common.MTA_excel_data_map,code,"MTA"), "Premium Summary function is having issue(S) . ");
			
			if(!TestBase.businessEvent.equalsIgnoreCase("Renewal")){
				Assert.assertTrue(common_HHAZ.funcStatusHandling(common.MTA_excel_data_map,code,"MTA"));
				customAssert.assertEquals(err_count,0,"Errors in premium calculations . ");
				customAssert.assertEquals(trans_error_val,0,"Errors in Transaction premium calculations . ");
				customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
				
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
			if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")) {
				customAssert.assertTrue(common_EP.ExistingPolicyAlgorithm(common.Rewind_excel_data_map,(String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type"), (String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Status")), "Existing Policy Algorithm function is having issues. ");
			}else {
				CommonFunction_HHAZ.AdjustedTaxDetails.clear();
				if(!common.currentRunningFlow.equalsIgnoreCase("Renewal") && !common.currentRunningFlow.equalsIgnoreCase("MTA")){
					NewBusinessFlow(code,"NB");
				}
				common_HHAZ.CoversDetails_data_list.clear();
				common_CCD.MD_Building_Occupancies_list.clear();
				common_HHAZ.PremiumFlag = false;
			}
			common.transaction_Details_Premium_Values.clear();
			common.currentRunningFlow="Rewind";
			String navigationBy = CONFIG.getProperty("NavigationBy");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcRewind());
			
			TestUtil.reportStatus("<b> -----------------------Rewind flow started---------------------- </b>", "Info", false);
			
			if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
				customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
				customAssert.assertTrue(common.funcSearchPolicy(common.MTA_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status (Endorsement Submitted (Rewind)) function is having issue(S) . ");
				
			}else{
				customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status (Endorsement Submitted (Rewind)) function is having issue(S) . ");
				}else{
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
				}
			}
			customAssert.assertTrue(funcPolicyGeneral(common.Rewind_excel_data_map,code,event), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common_HHAZ.funcCovers(common.Rewind_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcPreviousClaims(common.Rewind_excel_data_map), "Previous claim function is having issue(S) .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Product General"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcProductGeneral(common.Rewind_excel_data_map), "Previous claim function is having issue(S) .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
				customAssert.assertTrue(common_HHAZ.funcPremiumSummary_MTA(common.Rewind_excel_data_map,code,event), "Rewind MTA Premium Summary in function is having issue(S) . ");
			}else if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
				customAssert.assertTrue(common_HHAZ.funcPremiumSummary_MTA(common.Rewind_excel_data_map,code,event), "Rewind MTA Premium Summary in function is having issue(S) . ");
			}else{
				customAssert.assertTrue(common_HHAZ.funcPremiumSummary(common.Rewind_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			}
			if(!TestBase.businessEvent.equalsIgnoreCase("Renewal") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
				customAssert.assertTrue(common_HHAZ.funcStatusHandling(common.Rewind_excel_data_map,code,event));
			}
			
			if(TestBase.businessEvent.equals("Rewind")){
				
				customAssert.assertEquals(err_count,0,"Errors in premium calculations . ");
				customAssert.assertEquals(trans_error_val,0,"Errors in Transaction premium calculations . ");
				customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
				customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
				TestUtil.reportTestCasePassed(testName);
				
			}
			
			
		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
	}
	public void CancellationFlow(String code,String event) throws ErrorInTestMethod{
		
		String testName = (String)common.CAN_excel_data_map.get("Automation Key");
		try{
			if(((String)common.CAN_excel_data_map.get("CAN_ExistingPolicy")).equalsIgnoreCase("Yes")) {
				customAssert.assertTrue(common_EP.ExistingPolicyAlgorithm(common.CAN_excel_data_map , (String)common.CAN_excel_data_map.get("CAN_ExistingPolicy_Type"), (String)common.CAN_excel_data_map.get("CAN_ExistingPolicy_Status")), "Existing Policy Algorithm function is having issues. ");
			}else{
				NewBusinessFlow(code,"NB");
			}
			
			
			common.currentRunningFlow="CAN";
			
			customAssert.assertTrue(common_CCD.CancelPolicy(common.CAN_excel_data_map), "Unable to cancel policy");
			customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			
			customAssert.assertTrue(common_CCD.Cancel_PremiumSummary(common.CAN_excel_data_map), "Issue in verifying Premium summary for cancellation");
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""), "Unable to click on policies tab");
			Assert.assertTrue(common_HHAZ.funcStatusHandling(common.NB_excel_data_map,code,TestBase.businessEvent));
					
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

	public void RequoteFlow(String code,String event) throws ErrorInTestMethod{
		String testName = (String)common.Requote_excel_data_map.get("Automation Key");
		try{
			
			if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
				NewBusinessFlow(code,"NB");
			}
			common.currentRunningFlow="Requote";
			common_VELA.CoversDetails_data_list.clear();
			String navigationBy = CONFIG.getProperty("NavigationBy");
			common_HHAZ.PremiumFlag = false;
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcButtonSelection("Re-Quote"));
			
			TestUtil.reportStatus("<b> -----------------------Requote flow is started---------------------- </b>", "Info", false);
			
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
			customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Requote_excel_data_map,code,event,"Submitted"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
			
			customAssert.assertTrue(funcPolicyGeneral(common.Requote_excel_data_map,code,event), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common_HHAZ.funcCovers(common.Requote_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcPreviousClaims(common.Requote_excel_data_map), "Previous claim function is having issue(S) .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Product General"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcProductGeneral(common.Requote_excel_data_map), "Previous claim function is having issue(S) .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_HHAZ.funcPremiumSummary(common.Requote_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			customAssert.assertTrue(common_HHAZ.funcStatusHandling(common.Requote_excel_data_map,code,event));
			if(TestBase.businessEvent.equals("Requote")){
				
				customAssert.assertEquals(err_count,0,"Errors in premium calculations . ");
				customAssert.assertEquals(trans_error_val,0,"Errors in Transaction premium calculations . ");
				customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
				customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
				TestUtil.reportTestCasePassed(testName);
				
			}
		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
		
		
		
	}

public void RenewalFlow(String code,String event)throws ErrorInTestMethod{	
	String testName = (String)common.Renewal_excel_data_map.get("Automation Key");
	common.currentRunningFlow = "Renewal";
	String navigationBy = CONFIG.getProperty("NavigationBy");
		
		try{
			customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
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
			
			customAssert.assertTrue(funcPolicyGeneral(common.Renewal_excel_data_map,code,event), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common_HHAZ.funcCovers(common.Renewal_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcPreviousClaims(common.Renewal_excel_data_map), "Previous claim function is having issue(S) .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Product General"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcProductGeneral(common.Renewal_excel_data_map), "Previous claim function is having issue(S) .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
 			customAssert.assertTrue(common_HHAZ.funcPremiumSummary(common.Renewal_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
  			Assert.assertTrue(common_HHAZ.funcStatusHandling(common.Renewal_excel_data_map,code,"Renewal"));
			if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
				customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
				customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
				TestUtil.reportTestCasePassed(testName);
			}		
		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
		
	}
public void RenewalRewindFlow(String code,String event) throws ErrorInTestMethod{
	String testName = (String)common.Rewind_excel_data_map.get("Automation Key");
	try{
		CommonFunction_HHAZ.AdjustedTaxDetails.clear();
		common.currentRunningFlow="Rewind";
		//common_HHAZ.CoversDetails_data_list.clear();
		String navigationBy = CONFIG.getProperty("NavigationBy");
		common_HHAZ.PremiumFlag = false;
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common.funcRewind());
		
		TestUtil.reportStatus("<b> -----------------------Renewal Rewind flow started---------------------- </b>", "Info", false);
		
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
		customAssert.assertTrue(common.funcSearchPolicy(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Renewal Submitted (Rewind)"), "Verify Policy Status (Renewal Submitted (Rewind)) function is having issue(S) . ");
		
		customAssert.assertTrue(funcPolicyGeneral(common.Rewind_excel_data_map,code,event), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(common_HHAZ.funcCovers(common.Rewind_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcPreviousClaims(common.Rewind_excel_data_map), "Previous claim function is having issue(S) .");
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Product General"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcProductGeneral(common.Rewind_excel_data_map), "Previous claim function is having issue(S) .");
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		if(TestBase.businessEvent.equalsIgnoreCase("MTA"))
			customAssert.assertTrue(common_HHAZ.funcPremiumSummary_MTA(common.Rewind_excel_data_map,code,event), "Rewind MTA Premium Summary in function is having issue(S) . ");
		else
			customAssert.assertTrue(common_HHAZ.funcPremiumSummary(common.Rewind_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
		if(!TestBase.businessEvent.equalsIgnoreCase("Renewal") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
			customAssert.assertTrue(common_HHAZ.funcStatusHandling(common.Rewind_excel_data_map,code,event));
		}
		
		if(TestBase.businessEvent.equals("Rewind")){
			
			customAssert.assertEquals(err_count,0,"Errors in premium calculations . ");
			customAssert.assertEquals(trans_error_val,0,"Errors in Transaction premium calculations . ");
			customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
			customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
			TestUtil.reportTestCasePassed(testName);
			
		}
		
		
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
}
	public boolean funcPolicyGeneral(Map<Object, Object> map_data, String code, String event) {
		boolean retVal = true;
		
		try{
			
			customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");
			customAssert.assertTrue(k.Input("COB_PG_InsuredName", (String)map_data.get("PG_InsuredName")),	"Unable to enter value in Insured Name  field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_InsuredName", "value"),"Insured Name Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("OED_BusinessEstablished", (String)map_data.get("PG_BusinessEstablishmentYear")),	"Unable to enter value in Turnover field .");
			customAssert.assertTrue(k.Input("COB_PG_TurnOver", (String)map_data.get("PG_TurnOver")),	"Unable to enter value in Turnover field .");
			
			String[] geographical_Limits = ((String)map_data.get("PG_GeoLimit")).split(",");
			List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
			for(String geo_limit : geographical_Limits){
				for(WebElement each_ul : ul_elements){
					customAssert.assertTrue(k.Click("OED_PG_GeoLimit"),"Error while Clicking Geographic Limit List object . ");
					k.waitTwoSeconds();
					if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
						each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
					else
						continue;
					break;
				}
			}
			
			String[] Prof_Bodies = ((String)map_data.get("PG_InsuredDetails_Q1")).split(",");
			List<WebElement> ul_ele = driver.findElements(By.xpath("//ul"));
			for(String pBodies_limit : Prof_Bodies){
				for(WebElement each_ul : ul_ele){
					customAssert.assertTrue(k.Click("OED_PG_InsuredDetails_Q1"),"Error while Clicking Professional Bodies List object . ");
					k.waitTwoSeconds();
					if(each_ul.findElement(By.xpath("//li[text()='"+pBodies_limit+"']")).isDisplayed())
						each_ul.findElement(By.xpath("//li[text()='"+pBodies_limit+"']")).click();
					else
						continue;
					break;
				}
			}
			
			customAssert.assertTrue(k.Input("CCF_Address_CC_Address", (String) map_data.get("PG_Address")),"Unable to enter value in Address field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"Address Field Should Contain Valid Address  .");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line2", (String) map_data.get("PG_Line1")),"Unable to enter value in Address field line 1 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line3", (String) map_data.get("PG_Line2")),"Unable to enter value in Address field line 2 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Town", (String) map_data.get("PG_Town")),"Unable to enter value in Town field . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_County", (String) map_data.get("PG_County")),"Unable to enter value in County  . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode", (String)map_data.get("PG_Postcode")),"Unable to enter value in PostCode");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Postcode", "value"),"PostCode Field Should Contain Valid Postcode  .");
			customAssert.assertTrue(common.validatePostCode((String)map_data.get("PG_Postcode")),"Post Code is not in Correct format .");
			
			customAssert.assertTrue(k.DropDownSelection("COB_PG_QuoteValidity", (String)map_data.get("PG_QuoteValidity")),"Unable to select Auote Validity value");
			customAssert.assertTrue(k.Input("COB_PG_BusDesc", (String)map_data.get("PG_BusDesc")),	"Unable to enter value in Provided Details field .");
			
			if(common.currentRunningFlow.equalsIgnoreCase("NB")){
				// Select Trade Code :
				
				String sValue = (String)map_data.get("PG_TCS_TradeCode_Button");
				if(sValue.contains("Yes")){
					customAssert.assertTrue(SelectTradeCode((String)map_data.get("PG_TCS_TradeCode") , "Policy Details" , 0,map_data),"Trade code selection function is having issue(S).");
				}
				
				// check added trade codes
				String allTradeValues = (String)map_data.get("PG_TCS_TradeCode");
				String sUniqueCol ="Trade Code";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
			
				WebElement s_table= driver.findElement(By.xpath(sTablePath));
				int totalCols = s_table.findElements(By.tagName("th")).size(); 
				int totalRows = s_table.findElements(By.tagName("tr")).size();
				String val_Trade = "";
				
				for(int i = 0; i<totalRows-1; i++){
					
					if(totalRows > 2){
						val_Trade = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[3]");							
					}else{
						val_Trade = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr/td[3]");
					}
					
					if(allTradeValues.contains(val_Trade)){
						TestUtil.reportStatus(val_Trade + "Tradecode is added and displayed on Policy general screen", "Info", true);
					}
				}
				
				
			customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");	
			}
			
				

			TestUtil.reportStatus("Entered all the details on Policy General page .", "Info", true);
			
			return retVal;
		
		}catch(Throwable t) {
	        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
	        Assert.fail("Unable to to do operation on policy General page. \n", t);
	        return false;
		}
		
	}
	
public boolean SelectTradeCode(String tradeCodeValue , String pageName , int currentPropertyIndex,Map<Object, Object> map_data) {
		
		try{
			
			String tradeCodeName = "",referralKey = "RM_PolicyGeneral_Trade";
			customAssert.assertTrue(k.Click("COB_Btn_SelectTradeCode"), "Unable to click on Select Trade Code button in Policy Details .");
			customAssert.assertTrue(common.funcPageNavigation("Multi Trade Code Selection", ""), "Navigation problem to TMulti Trade Code Selection page .");
			
			String sVal = tradeCodeValue;
			String[] TradeCodes = tradeCodeValue.split(",");
			
			String a_selectedTradeCode = null;
			String a_selectedTradeCode_desc = null;
			
			for(String s_TradeCode : TradeCodes){
	 			driver.findElement(By.name("multiTrade_"+s_TradeCode)).click();
	 		}
			//Referral code - Trade Referrals
			/*if(is_Trade_referral_activity(tradeCodeName)){
				tradeCodeName = tradeCodeName.replaceAll(" ", "");
				
				common_HHAZ.referrals_list.add((String)map_data.get(referralKey+tradeCodeName));
			}*/
			
			common.funcButtonSelection("Save");
			common.funcButtonSelection("exit (Save First)");
	 		
	 		return true;
	 		
		}catch(Throwable t){
			return false;
		}
	}
	
public boolean MaterialFactsDeclerationPage(){
	boolean retValue = true;
	Map<Object, Object> data_map = new HashMap<>();
	switch(common.currentRunningFlow){
		case "NB":
			data_map = common.NB_excel_data_map;
			break;
		case "MTA":
			data_map = common.MTA_excel_data_map;
			break;
		case "Renewal":
			data_map = common.Renewal_excel_data_map;
			break;
		case "Rewind":
			data_map = common.Rewind_excel_data_map;
			break;
		case "Requote":
			data_map = common.Requote_excel_data_map;
			break;
	
	}
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Material Facts & Declarations", ""),"Material Facts and Declarations page is having issue(S)");
		 k.ImplicitWaitOff();
		 String q_value = null;
		 
		List<WebElement> elements = driver.findElements(By.className("selectinput"));
		 Select sel = null;
		
		 for(int i = 0;i<elements.size();i++){
			 if(elements.get(i).isDisplayed()){
				 
				 sel = new Select(elements.get(i));
				 try{
					 q_value = (String)data_map.get("MFD_Q"+(i+1));
					 
					 sel.selectByVisibleText(q_value);
					 }
				 catch(Throwable t){
					 
					 try{
						 sel.selectByVisibleText("No");
					 }catch(Throwable t1){
						 
					 }
					 
				 } 
			 }		 
		 }
		 
		 
		 
		 
		 if(k.isDisplayedField("OED_MFD_PLMF")){
			 String[] geographical_Limits = ((String)data_map.get("OED_MFD_PLMF")).split(",");
			 customAssert.assertTrue(k.Click("OED_MFD_PLMF"),"Error while Clicking Geographic Limit List object . ");
			 List<WebElement> _prd_liability_MF = driver.findElements(By.xpath("//html//body//span//span//following::ul//li"));	
			 	for(String geo_limit : geographical_Limits){
					for(WebElement each_ul : _prd_liability_MF){
						//customAssert.assertTrue(k.Click("MFD_PLMF"),"Error while Clicking Geographic Limit List object . ");
						//each_ul.click();
						k.waitTwoSeconds();//*[text()=
						//System.out.println(each_ul.findElement(By.xpath("//*[text()='"+geo_limit+"']")).isDisplayed());
						if(each_ul.findElement(By.xpath("//*[text()='"+geo_limit+"']")).isDisplayed())
							each_ul.findElement(By.xpath("//*[text()='"+geo_limit+"']")).click();
						else
							continue;
						break;
					}
				}
		 	}
		 
				 
		 	List<WebElement> txt_Elements = driver.findElements(By.className("write"));
		 q_value = (String)data_map.get("MFD_T");
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

public boolean funcPreviousClaims(Map<Object, Object> map_data){

    boolean retvalue = true;
    
      try {    
    	  	//customAssert.assertTrue(common.funcMenuSelection("Navigate", "Previous Claims"),"Unable To navigate to Previous Claims scren");
			customAssert.assertTrue(common.funcPageNavigation("Previous Claims",""), "Previous Claims Page Navigation issue . ");
			TestUtil.reportStatus("Verified Previous Claims page .", "Info", true);
			//customAssert.assertTrue(common.funcButtonSelection("next"), "Unable to click on Next Button on Previous Claims .");
			return retvalue;
             
      } catch(Throwable t) {
             String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
          TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
          Assert.fail("Unable to handle Previous Claims Page", t);
          return false;
      }


}

public boolean funcProductGeneral(Map<Object, Object> map_data){

    boolean retvalue = true;
   
//  List<String> arrayList = new ArrayList<String>();
   
    Map<String, List<Map<String, String>>> internal_data_map = new HashMap<>();
	switch(common.currentRunningFlow){
		case "NB":
			internal_data_map = common.NB_Structure_of_InnerPagesMaps;
			break;
		case "MTA":
			internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
			break;
		case "Renewal":
			internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
			break;
		case "Rewind":
			internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
			break;
		case "Requote":
			internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
			break;
	
	}
      try {    
    	  	customAssert.assertTrue(common.funcPageNavigation("Product General",""), "Product General Page Navigation issue . ");
			
    	  	customAssert.assertTrue(k.DropDownSelection("OED_public_liability", (String)map_data.get("PL_CoverPremium")),"Unable to select Auote Validity value");
    	  	customAssert.assertTrue(k.DropDownSelection("OED_employers_liability", (String)map_data.get("EL_CoverPremium")),"Unable to select Auote Validity value");
			customAssert.assertTrue(k.Input("OED_manual_employees", (String)map_data.get("Manual_Employees")),	"Unable to enter value in Provided Details field .");
			customAssert.assertTrue(k.Input("OED_clerical_employees", (String)map_data.get("Clerical_Employees")),	"Unable to enter value in Provided Details field .");
			customAssert.assertTrue(k.Input("OED_turnover", (String)map_data.get("PL_Turnover")),	"Unable to enter value in Provided Details field .");
			customAssert.assertTrue(k.DropDownSelection("OED_work_outside", (String)map_data.get("PL_ConducntWork")),"Unable to select Auote Validity value");
			
			if(((String)map_data.get("PL_ConducntWork")).equalsIgnoreCase("Yes")){
				customAssert.assertTrue(k.Input("OED_work_outside_details", (String)map_data.get("PL_Details")),	"Unable to enter value in Provided Details field .");
			}
				
			if(!common.currentRunningFlow.equals("NB")){
				 List<WebElement> bussinesAct = driver.findElements(By.xpath("//*[@class='select2-selection__choice__remove']"));
				 int no_bussinesAct = bussinesAct.size();
				 int bussAct = 0;
				 while(bussAct < no_bussinesAct){
					 driver.findElement(By.xpath("//*[@class='select2-selection__choice__remove']")).click();
					 bussAct++;
				 }
			}
			String[] Prof_Bodies = ((String)map_data.get("BU_Activities")).split(";");
			 int no_of_property = Prof_Bodies.length;
	            List<WebElement> elm = null;
	            elm = null;
	            int no = 0;
	            WebElement ul_ele = driver.findElement(By.xpath("//ul"));
	            while(no < no_of_property ){
				
					
					 String activity = internal_data_map.get("Business Activities").get(no).get("Business_ACT_Details").replaceAll("\u00A0", " ");
					 if(activity.equals("CIPP Lining")){
						 CIPPLining = internal_data_map.get("Business Activities").get(no).get("Business_ACT_BeSpoke");
					 }
					BusinessActivity.add(activity);
					customAssert.assertTrue(k.Click("OED_Business_Activities"),"Error while Clicking Professional Bodies List object . ");
					k.waitTwoSeconds();
					if(ul_ele.findElement(By.xpath("//li[text()='"+activity+"']")).isDisplayed()){
						ul_ele.findElement(By.xpath("//li[text()='"+activity+"']")).click();
						if(activity.equalsIgnoreCase("Blocked Drains Cleaning & Rodding")){activity = "blocked_drains_cleanin_rodding";}
						if(activity.equalsIgnoreCase("Cesspit/Septic Tank Services Installation & Maintenance")){activity = "cesspit_septic_tank_services_installation_maintenance";}
						driver.findElement(By.xpath("//*[contains(@name,'oed_"+activity.replaceAll(" ", "_").toLowerCase()+"')]")).clear();
						driver.findElement(By.xpath("//*[contains(@name,'oed_"+activity.replaceAll(" ", "_").toLowerCase()+"')]")).sendKeys(internal_data_map.get("Business Activities").get(no).get("Business_ACT_BeSpoke"));
						  no++;
				}
			}
			
			String property = ((String)map_data.get("PL_Activities"));
			String[] properties = null;
            no_of_property =0;
            if(property.isEmpty()){
            	no_of_property = 0;
            }else{
            	properties = ((String)map_data.get("PL_Activities")).split(";");
            	no_of_property = properties.length;
            }
           no = 0;
            while(no < no_of_property ){
            	
            	customAssert.assertTrue(k.Click("OED_Add_Activity"));
            	customAssert.assertTrue(k.Input("OED_Activities_Details", internal_data_map.get("Activities").get(no).get("ACT_Details")),	"Unable to enter value in Provided Details field .");
    			customAssert.assertTrue(k.Input("OED_Activities_Bespoke", internal_data_map.get("Activities").get(no).get("ACT_BeSpoke")),	"Unable to enter value in Provided Details field .");
    			customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"), "Unable to click on Inner button.");
                no++;
            	
            }
            
            customAssert.assertTrue(k.Click("CCD_ApplyBookRate"));
    	  	
            
            List<WebElement> tableIdentification = driver.findElements(By.xpath("//a[text()='Apply Book Rates']//following::table"));
            int tableCount = tableIdentification.size();
            
            
            for(int tableIndex=0;tableIndex<tableCount;tableIndex++){
            	
            	String coverName = driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr[1]//td[1]")).getText();
            	String activity = driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr[1]//td[3]")).getText();
            	
            	if(coverName.equalsIgnoreCase("Public Liability")){
            		if(((String)map_data.get("CD_PublicLiability")).equalsIgnoreCase("Yes")){
            			
            			String coverRequired = driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr[1]//td[2]")).getText();
            			String noOfEMP = driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr[1]//td[4]")).getText();
            			String bookRate = driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr[1]//td[5]/input")).getAttribute("value");
            			double PL_bookrate = get_Book_Rate(coverName,(String)map_data.get("PL_CoverPremium"),BusinessActivity, CIPPLining);
            			if(!coverRequired.equalsIgnoreCase((String)map_data.get("PL_CoverPremium"))){
            				TestUtil.reportStatus("Cover required for Public Liability is not matching with input data.", "Fail", true);
            			}if(!activity.equalsIgnoreCase("Manual")){
            				TestUtil.reportStatus("Activity for Public Liability is not matching with input data.", "Fail", true);
            			}if(!noOfEMP.equalsIgnoreCase((String)map_data.get("Manual_Employees"))){
            				TestUtil.reportStatus("Number of Employees for Public Liability is not matching with input data.", "Fail", true);
            			}if(Double.parseDouble(bookRate) != PL_bookrate){
            				TestUtil.reportStatus("Book Rate for selected combination for Public Liability is not matching with input data.", "Fail", true);
            			}
            			
            			customAssert.assertTrue(calculatePremium(tableIndex , map_data , "PL" , "Public Liability" , 1), "Premium Calculation function for Public Liability is casuing some issue");
            			
            		}
            	}else if(coverName.equalsIgnoreCase("Employers Liability")){
            		if(((String)map_data.get("CD_EmployersLiability")).equalsIgnoreCase("Yes")){
            			
            			String coverRequired = driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr[1]//td[2]")).getText();
            			String noOfEMP = driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr[1]//td[4]")).getText();
            			String bookRate = driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr[1]//td[5]/input")).getAttribute("value");
            			double EL_bookrate = get_Book_Rate(coverName,(String)map_data.get("EL_CoverPremium"),BusinessActivity, CIPPLining);
            			if(!coverRequired.equalsIgnoreCase((String)map_data.get("EL_CoverPremium"))){
            				TestUtil.reportStatus("Cover required for Public Liability is not matching with input data.", "Fail", true);
            			}if(!activity.equalsIgnoreCase("Manual")){
            				TestUtil.reportStatus("Activity for Public Liability is not matching with input data.", "Fail", true);
            			}if(!noOfEMP.equalsIgnoreCase((String)map_data.get("Manual_Employees"))){
            				TestUtil.reportStatus("Number of Employees for Public Liability is not matching with input data.", "Fail", true);
            			}if(Double.parseDouble(bookRate) != EL_bookrate){
            				TestUtil.reportStatus("Book Rate for selected combination for Public Liability is not matching with input data.", "Fail", true);
            			}
            			
            			customAssert.assertTrue(calculatePremium(tableIndex , map_data , "EL" , "Employers Liability" , 1), "Premium Calculation function for Employers Liability is casuing some issue");
            			
            		}
            	}else if(coverName.equalsIgnoreCase("Personal Accident")){
            		if(((String)map_data.get("CD_PersonalAccident")).equalsIgnoreCase("Yes")){
            			totalPAPremium =0.00;
            			customAssert.assertTrue(calculatePremiumPA(tableIndex , map_data , "PAM" , "Personal Accident" , 1), "Premium Calculation function for Personal Accident is casuing some issue");
            			customAssert.assertTrue(calculatePremiumPA(tableIndex , map_data , "PAN" , "Personal Accident" , 2), "Premium Calculation function for Personal Accident is casuing some issue");
                	}
            	}else if(coverName.equalsIgnoreCase("Legal Expenses")){
            		if(((String)map_data.get("CD_LegalExpenses")).equalsIgnoreCase("Yes")){
            			customAssert.assertTrue(calculatePremium(tableIndex , map_data , "LE" , "Legal Expenses" , 1), "Premium Calculation function for Legal Expenses is casuing some issue");
            		}
            	}
            	
            	
            	
            }
            
            TestUtil.reportStatus("Verified Product General page .", "Info", true);
			return retvalue;
             
      } catch(Throwable t) {
          String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
          TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
          k.reportErr("Failed in "+methodName+" function", t);
          Assert.fail("Unable to handle Product General Page", t);
          return false;
      }


}


public boolean calculatePremium(int tableIndex , Map<Object, Object> map_data , String bookRateCode , String coverName , int rowNum){
	
	try{
		
		
		String expected_bookPremium = null , actual_bookPremium = null;
		if(bookRateCode.equalsIgnoreCase("LE")){
			expected_bookPremium = Double.toString(get_Book_Rate(coverName,"",BusinessActivity, "0"));
			//Double.toString(Double.parseDouble((String)map_data.get(bookRateCode+"_BookRate")));
		}else{
			expected_bookPremium = Double.toString(get_Book_Rate(coverName,(String)map_data.get(bookRateCode+"_CoverPremium"),BusinessActivity, CIPPLining) * Double.parseDouble((String)map_data.get("Manual_Employees")));
		}
		actual_bookPremium = driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr[1]//td[6]/input")).getAttribute("value");		
		CommonFunction.compareValues(Double.parseDouble(expected_bookPremium), Double.parseDouble(actual_bookPremium), "Book Premium of "+bookRateCode+" cover");
		
		driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+rowNum+"]//td[7]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
		driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+rowNum+"]//td[7]/input")).sendKeys((String)map_data.get(bookRateCode+"_TechAdjust"));
		
		driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+rowNum+"]//td[9]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
		driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+rowNum+"]//td[9]/input")).sendKeys((String)map_data.get(bookRateCode+"_CommAdjust"));
		
//		driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+rowNum+"]//td[10]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
//		driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+rowNum+"]//td[10]/input")).sendKeys((String)map_data.get(bookRateCode+"_PremiumOveride"));
		if(TestBase.businessEvent.equals("Renewal")){
		customAssert.assertTrue(k.Click("CCD_ApplyBookRate"));}
		String expected_RevisedPremium = common.roundedOff(Double.toString(Double.parseDouble(expected_bookPremium) + ((Double.parseDouble(expected_bookPremium) * Double.parseDouble((String)map_data.get(bookRateCode+"_TechAdjust")))/100.0)));
		String actual_RevisedPremium = driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr[1]//td[8]/input")).getAttribute("value");
		
		CommonFunction.compareValues(Double.parseDouble(expected_RevisedPremium), Double.parseDouble(actual_RevisedPremium), "Revised Premium of "+bookRateCode+" cover");
		
		driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+rowNum+"]//td[10]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
		driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+rowNum+"]//td[10]/input")).sendKeys((String)map_data.get(bookRateCode+"_PremiumOveride"));
		driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+rowNum+"]//td[10]/input")).sendKeys(Keys.TAB);
		
		String expected_Premium = common.roundedOff(Double.toString(Double.parseDouble(expected_RevisedPremium) + ((Double.parseDouble(expected_RevisedPremium) * Double.parseDouble((String)map_data.get(bookRateCode+"_CommAdjust")))/100.0)));
		String actual_Premium = driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr[1]//td[11]/input")).getAttribute("value");
		
		if(((String)map_data.get(bookRateCode+"_PremiumOveride")).equalsIgnoreCase("0") || ((String)map_data.get(bookRateCode+"_PremiumOveride"))==null){
			CommonFunction.compareValues(Double.parseDouble(expected_Premium), Double.parseDouble(actual_Premium), "Final Premium of "+bookRateCode+" cover");
		}else{
			CommonFunction.compareValues(Double.parseDouble((String)map_data.get(bookRateCode+"_PremiumOveride")), Double.parseDouble(actual_Premium), "Final Premium of "+bookRateCode+" cover");
		}
		
		TestUtil.WriteDataToXl(TestBase.product + "_" +common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_"+coverName.replace(" " , "")+"_NetNetPremium", actual_Premium, map_data);
		
		return true;
		
	}catch(Throwable t){
		
		return false;
		
	}
	
}

public boolean calculatePremiumPA(int tableIndex , Map<Object, Object> map_data , String bookRateCode , String coverName , int rowNum){
	
	try{
		
		String noOfEMP = null , bookRate = null;
		
		noOfEMP = driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+(rowNum)+"]//td[4]")).getText();
		bookRate = driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+(rowNum)+"]//td[5]/input")).getAttribute("value");
		String expected_bookPremium = null;
		
		if(bookRateCode.equalsIgnoreCase("PAM")){
			if(!noOfEMP.equalsIgnoreCase((String)map_data.get("Manual_Employees"))){
				TestUtil.reportStatus("Number of Employees for Public Liability is not matching with input data.", "Fail", true);
			}if(!bookRate.equalsIgnoreCase(Double.toString(get_Book_Rate(coverName,"PAM",BusinessActivity, "0")))){
				TestUtil.reportStatus("Book Rate for selected combination for Public Liability is not matching with input data.", "Fail", true);
			}
			expected_bookPremium = Double.toString(Double.parseDouble((String)map_data.get(bookRateCode+"_BookRate")) * Double.parseDouble((String)map_data.get("Manual_Employees")));
		}else{
			if(!noOfEMP.equalsIgnoreCase((String)map_data.get("Clerical_Employees"))){
				TestUtil.reportStatus("Number of Employees for Public Liability is not matching with input data.", "Fail", true);
			}if(!bookRate.equalsIgnoreCase(Double.toString(get_Book_Rate(coverName,"PAN",BusinessActivity, "0")))){
				TestUtil.reportStatus("Book Rate for selected combination for Public Liability is not matching with input data.", "Fail", true);
			}
			expected_bookPremium = Double.toString(Double.parseDouble((String)map_data.get(bookRateCode+"_BookRate")) * Double.parseDouble((String)map_data.get("Clerical_Employees")));
		}
		String actual_bookPremium = driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+(rowNum)+"]//td[6]/input")).getAttribute("value");		
		CommonFunction.compareValues(Double.parseDouble(expected_bookPremium), Double.parseDouble(actual_bookPremium), "Book Premium of "+bookRateCode+" cover");
		
		driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+rowNum+"]//td[7]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
		driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+rowNum+"]//td[7]/input")).sendKeys((String)map_data.get(bookRateCode+"_TechAdjust"));
		
		driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+rowNum+"]//td[9]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
		driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+rowNum+"]//td[9]/input")).sendKeys((String)map_data.get(bookRateCode+"_CommAdjust"));
		
		
		String expected_RevisedPremium = common.roundedOff(Double.toString(Double.parseDouble(expected_bookPremium) + ((Double.parseDouble(expected_bookPremium) * Double.parseDouble((String)map_data.get(bookRateCode+"_TechAdjust")))/100.0)));
		String actual_RevisedPremium = driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+(rowNum)+"]//td[8]/input")).getAttribute("value");
		
		CommonFunction.compareValues(Double.parseDouble(expected_RevisedPremium), Double.parseDouble(actual_RevisedPremium), "Revised Premium of "+bookRateCode+" cover");
		
		driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+rowNum+"]//td[10]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
		driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+rowNum+"]//td[10]/input")).sendKeys((String)map_data.get(bookRateCode+"_PremiumOveride"));
		driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+rowNum+"]//td[10]/input")).sendKeys(Keys.TAB);
		
		String expected_Premium = common.roundedOff(Double.toString(Double.parseDouble(expected_RevisedPremium) + ((Double.parseDouble(expected_RevisedPremium) * Double.parseDouble((String)map_data.get(bookRateCode+"_CommAdjust")))/100.0)));
		String actual_Premium = driver.findElement(By.xpath("//a[text()='Apply Book Rates']//following::table["+(tableIndex+1)+"]//tbody//tr["+(rowNum)+"]//td[11]/input")).getAttribute("value");
		
		if(((String)map_data.get(bookRateCode+"_PremiumOveride")).equalsIgnoreCase("0") || ((String)map_data.get(bookRateCode+"_PremiumOveride"))==null){
			CommonFunction.compareValues(Double.parseDouble(expected_Premium), Double.parseDouble(actual_Premium), "Final Premium of "+bookRateCode+" cover");
		}else{
			CommonFunction.compareValues(Double.parseDouble((String)map_data.get(bookRateCode+"_PremiumOveride")), Double.parseDouble(actual_Premium), "Final Premium of "+bookRateCode+" cover");
		}
		
		totalPAPremium = totalPAPremium + Double.parseDouble(actual_Premium);
		
		TestUtil.WriteDataToXl(TestBase.product + "_" +common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_"+coverName.replace(" ", "")+"_NetNetPremium", Double.toString(totalPAPremium), map_data);
		
		return true;
		
	}catch(Throwable t){
		
		return false;
		
	}
	
}
 // Rater For Book rate calculation
public double get_Book_Rate(String coverName,String LOI,List<String> business_activities,String percent_cipp_lining){
	
	
	OED_Rater = OR.getORProperties();
//	int number = StringUtils.countMatches(business_activities, ",");
	String h_act=OED_Rater.getProperty("h_act");
	String m_act=OED_Rater.getProperty("m_act");
	String l_act=OED_Rater.getProperty("l_act");
	
	//double cipp_lining=0.0;
	//String[] activities=new String[number+1];
	int risk=0;
	String current_act="";
	Iterator<String> itr=business_activities.iterator();
	while(itr.hasNext())
	{
//		activities[i]=business_activities.split(";")[i];
		//System.out.println(activities[i]);
		current_act=itr.next();
		if(current_act.contains("CIPP Lining"))
		{
			if(Double.parseDouble(percent_cipp_lining)<30)
			{
				risk=3;
				break;
			}
		}
		if(h_act.contains(current_act))
		{
			risk=3;
			break;
		}
		else
		{
			if(m_act.contains(current_act))
			{
				risk=2;
			}
			else
			{
				if(l_act.contains(current_act)&&risk<2)
				{
					risk=1;
				}
			}
		}
	}
	//System.out.println(risk);
	//double pl=2000000;
	double book_rate=0;
	double el_book_rate=0;
	if(coverName.equals("Public Liability"))
	{
	if(LOI.equals("1,000,000"))
	{
		switch(risk){
			case 1:
				book_rate=Double.parseDouble(OED_Rater.getProperty("PL_£1m_Low"));
				break;
			case 2:
				book_rate=Double.parseDouble(OED_Rater.getProperty("PL_£1m_Medium"));
				break;
			case 3:
				book_rate=Double.parseDouble(OED_Rater.getProperty("PL_£1m_High"));
				break;
			case 0:
				book_rate=0;
				break;
		}
		return book_rate;
	}
	if(LOI.equals("2,000,000"))
	{
		switch(risk){
			case 1:
				book_rate=Double.parseDouble(OED_Rater.getProperty("PL_£2m_Low"));
				break;
			case 2:
				book_rate=Double.parseDouble(OED_Rater.getProperty("PL_£2m_Medium"));
				break;
			case 3:
				book_rate=Double.parseDouble(OED_Rater.getProperty("PL_£2m_High"));;
				break;
			case 0:
				book_rate=0;
				break;
		}
		return book_rate;
		
	}
	if(LOI.equals("5,000,000"))
	{
		switch(risk){
			case 1:
				book_rate=Double.parseDouble(OED_Rater.getProperty("PL_£5m_Low"));
				break;
			case 2:
				book_rate=Double.parseDouble(OED_Rater.getProperty("PL_£5m_Medium"));
				break;
			case 3:
				book_rate=Double.parseDouble(OED_Rater.getProperty("PL_£5m_High"));
				break;
			case 0:
				book_rate=0;
				break;
		}
		return book_rate;
	}
	}
	else
	{
		if(coverName.equals("Employers Liability"))
		{
			switch(risk){
			case 1:
				el_book_rate=Double.parseDouble(OED_Rater.getProperty("EL_Low"));
				break;
			case 2:
				el_book_rate=Double.parseDouble(OED_Rater.getProperty("EL_Medium"));
				break;
			case 3:
				el_book_rate=Double.parseDouble(OED_Rater.getProperty("EL_High"));
				break;
			case 0:
				el_book_rate=0;
				break;
			}
			return el_book_rate;
		}
		else
		{
			if(coverName.equals("Personal Accident"))
			{
				if(LOI.equals("PAM"))
				{
					return Double.parseDouble(OED_Rater.getProperty("PA_Manual"));
				}
				if(LOI.equals("PAN"))
				{
					return Double.parseDouble(OED_Rater.getProperty("PA_Clerical"));
				}
			}
			else
			{
				if(coverName.equals("Legal Expenses"))
				{
					return Double.parseDouble(OED_Rater.getProperty("LE"));
				}
			}
		}
	}
	return 0;
}

}
