package com.selenium.commonfiles.base;

import static com.selenium.commonfiles.util.TestUtil.WriteDataToXl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import org.omg.PortableServer.POA;
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


public class CommonFunction_PAA extends TestBase{

	SimpleDateFormat df = new SimpleDateFormat();
	Properties CCD_Rater = new Properties();
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
	public static double Ter_BuildingContents_Sum = 0;
	public double totalMD = 0.0, Ter_BI_Sum = 0.00;
	public boolean isGrossPremiumReferralCheckDone = false;
	public double  CarsNetP,CvNetP,AvNetP,OtNetP,StNetP,TrNetP,TpNetP,PoaNetP;
	public double tempNP=0.00;
	public DecimalFormat f = new DecimalFormat("00.00");
	Throwable t;
	public static double CMA_total_Premium;
	
	
	
	public Map<String,Map<String,Double>> CAN_MFB_ReturnP_Values_Map = new HashMap<>();
	
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
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			
			// Personal Accident page - funcPersonalAccident
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Personal Accident"),"Issue while Navigating to Personal Accident Cover page ");
			customAssert.assertTrue(funcPersonalAccident(common.NB_excel_data_map), "Personal Accident is having issue(S) .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_HHAZ.funcPremiumSummary(common.NB_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			
			Assert.assertTrue(common_HHAZ.funcStatusHandling(common.NB_excel_data_map,code,"NB"));
//		
			if(TestBase.businessEvent.equals("NB")){
				
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
	
	public void MTAFlow(String code,String fileName) throws ErrorInTestMethod{
		String testName = (String)common.MTA_excel_data_map.get("Automation Key");
		try{
			CommonFunction_HHAZ.AdjustedTaxDetails.clear();
			if(((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy")).equalsIgnoreCase("Yes")) {
				common_EP.ExistingPolicyAlgorithm(common.MTA_excel_data_map , (String)common.MTA_excel_data_map.get("MTA_ExistingPolicy_Type"), (String)common.MTA_excel_data_map.get("MTA_ExistingPolicy_Status"));
			}else {
				if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
					NewBusinessFlow(code,"NB");
				}
				common_CCD.MD_Building_Occupancies_list.clear();
				//common_HHAZ.CoversDetails_data_list.clear();
				common_HHAZ.PremiumFlag = false;
			}
			
			common.currentRunningFlow="MTA";
			String navigationBy = CONFIG.getProperty("NavigationBy");
			common_HHAZ.CoversDetails_data_list = new ArrayList<String>();
			common_HHAZ.CoversDetails_data_list.add("PersonalAccident");
			
			customAssert.assertTrue(common_CCD.funcCreateEndorsement(),"Error in Create Endorsement function . ");
			
			customAssert.assertTrue(funcPolicyGeneral(common.MTA_excel_data_map,code,"MTA"), "Policy Details function having issue .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Personal Accident"),"Issue while Navigating to Personal Accident Cover page ");
			customAssert.assertTrue(funcPersonalAccident(common.MTA_excel_data_map), "Personal Accident function is having issue(S) .");
		
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
			
		
		
		
		}catch (ErrorInTestMethod e) {
			//System.out.println("Error in New Business test method for MTA > "+testName);
			throw e;
		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
		
		
		
	}
	
	// Cancellation starts here :

	public void CancellationFlow(String code,String event) throws ErrorInTestMethod{
		
		String testName = (String)common.CAN_excel_data_map.get("Automation Key");
		try{
			NewBusinessFlow(code,"NB");
			
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
	
	public void RenewalFlow(String code,String fileName) throws ErrorInTestMethod{
		String testName = (String)common.Renewal_excel_data_map.get("Automation Key");
		try{
			
			common.currentRunningFlow="Renewal";
			String navigationBy = CONFIG.getProperty("NavigationBy");
		
			customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
			customAssert.assertTrue(common_POB.renewalSearchPolicyNEW(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Pending"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
			customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
			customAssert.assertTrue(common.funcAssignUnderWriter((String)common.Renewal_excel_data_map.get("Renewal_AssignUnderwritter")));
			customAssert.assertTrue(common.funcButtonSelection("Send to Underwriter"));
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
			customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Submitted"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
			
			
			customAssert.assertTrue(funcPolicyGeneral(common.Renewal_excel_data_map,code,"Renewal"), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			
			// Computers page - funcComputer
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Personal Accident"),"Issue while Navigating to Personal Accident Cover page ");
			customAssert.assertTrue(funcPersonalAccident(common.Renewal_excel_data_map), "Personal Accident function is having issue(S) .");
		
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		
			
			//  Premium Summary
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_HHAZ.funcPremiumSummary(common.Renewal_excel_data_map,code,"Renewal"), "Premium Summary function is having issue(S) . ");
			Assert.assertTrue(common_HHAZ.funcStatusHandling(common.Renewal_excel_data_map,code,"Renewal"));
		
			TestUtil.reportTestCasePassed(testName);
		
		}catch (ErrorInTestMethod e) {
			System.out.println("Error in New Business test method for MTA > "+testName);
			throw e;
		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
	
}
	
	public void RewindFlow(String code,String event) throws ErrorInTestMethod{
		String testName = (String)common.Rewind_excel_data_map.get("Automation Key");
		try{
			
			if(!common.currentRunningFlow.equalsIgnoreCase("Renewal") && !common.currentRunningFlow.equalsIgnoreCase("MTA")){
				NewBusinessFlow(code,"NB");
			}
			common.currentRunningFlow="Rewind";
			common_HHAZ.CoversDetails_data_list = new ArrayList<String>();
			common_HHAZ.CoversDetails_data_list.add("PersonalAccident");
			String navigationBy = CONFIG.getProperty("NavigationBy");
			common_HHAZ.PremiumFlag = false;
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcRewind());
			
			TestUtil.reportStatus("<b> -----------------------Rewind flow started---------------------- </b>", "Info", false);
			
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
			customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			if(TestBase.businessEvent.equalsIgnoreCase("MTA"))
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status (Endorsement Submitted (Rewind)) function is having issue(S) . ");
			else
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
			
			customAssert.assertTrue(funcPolicyGeneral(common.Rewind_excel_data_map,code,event), "Policy Details function having issue .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			
			// Personal Accident page - funcPersonalAccident
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Personal Accident"),"Issue while Navigating to Personal Accident Cover page ");
			customAssert.assertTrue(funcPersonalAccident(common.Rewind_excel_data_map), "Personal Accident is having issue(S) .");
					
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


	public void RequoteFlow(String code,String event) throws ErrorInTestMethod{
		String testName = (String)common.Requote_excel_data_map.get("Automation Key");
		try{
			
			if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
				NewBusinessFlow(code,"NB");
			}
			common.currentRunningFlow="Requote";
			//common_HHAZ.CoversDetails_data_list.clear();
			String navigationBy = CONFIG.getProperty("NavigationBy");
			common_HHAZ.PremiumFlag = false;
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcButtonSelection("Re-Quote"));
			
			TestUtil.reportStatus("<b> -----------------------Requote flow started---------------------- </b>", "Info", false);
			
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
			customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Requote_excel_data_map,code,event,"Submitted"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
			
			customAssert.assertTrue(funcPolicyGeneral(common.Requote_excel_data_map,code,event), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common_HHAZ.funcCovers(common.Requote_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts and Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcPreviousClaims(common.Requote_excel_data_map), "Previous claim function is having issue(S) .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Claims Experience"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcClaimsExperience(common.Requote_excel_data_map), "Previous claim function is having issue(S) .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
			customAssert.assertTrue(common_HHAZ.funcEndorsementOperations(common.Requote_excel_data_map),"Insurance tax adjustment is having issue(S).");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_HHAZ.funcPremiumSummary(common.Requote_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			customAssert.assertTrue(common_HHAZ.funcStatusHandling(common.Requote_excel_data_map,code,event));
			
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
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Renewal Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
			
			customAssert.assertTrue(funcPolicyGeneral(common.Rewind_excel_data_map,code,event), "Policy Details function having issue .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			
			// Computers page - funcComputer
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Personal Accident"),"Issue while Navigating to Personal Accident Cover page ");
			customAssert.assertTrue(funcPersonalAccident(common.Rewind_excel_data_map), "Personal Accident function is having issue(S) .");
		
		
			//customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
			//customAssert.assertTrue(common_HHAZ.funcEndorsementOperations(common.Rewind_excel_data_map),"Insurance tax adjustment is having issue(S).");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_HHAZ.funcPremiumSummary(common.Rewind_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			
			
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
			
			customAssert.assertTrue(k.Input("COB_PG_TurnOver", (String)map_data.get("PG_TurnOver")),	"Unable to enter value in Turnover field .");
			
			String[] geographical_Limits = ((String)map_data.get("PG_GeoLimit")).split(",");
			List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
			for(String geo_limit : geographical_Limits){
				for(WebElement each_ul : ul_elements){
					customAssert.assertTrue(k.Click("PAA_GEOLimit"),"Error while Clicking Geographic Limit List object . ");
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
					customAssert.assertTrue(k.Click("PAA_PG_TradeAssociations"),"Error while Clicking Professional Bodies List object . ");
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
				String ExpectedCode = (String)map_data.get("PG_TCS_TradeCode");
				String ActualCode ;
				
							
				ActualCode = k.getTextByXpath("html/body/div[3]/form/div/table[6]/tbody/tr[2]/td[2]");
				customAssert.assertTrue(ExpectedCode.equals(ActualCode), "Trade code selection failed");
					
				
				
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
			customAssert.assertTrue(k.Click("CMA_Btn_SelectTradeCode"), "Unable to click on Select Trade Code button in Policy Details .");
			customAssert.assertTrue(common.funcPageNavigation("Trade Code Selection", ""), "Navigation problem to Trade Code Selection page .");
			
			String sVal = tradeCodeValue;
			String[] TradeCodes = tradeCodeValue.split(",");
			
			String a_selectedTradeCode = null;
			String a_selectedTradeCode_desc = null;
			
			for(String s_TradeCode : TradeCodes){
	 			driver.findElement(By.xpath("//*[text()='"+s_TradeCode+"']")).click();
	 		}
			//Referral code - Trade Referrals
			/*if(is_Trade_referral_activity(tradeCodeName)){
				tradeCodeName = tradeCodeName.replaceAll(" ", "");
				
				common_HHAZ.referrals_list.add((String)map_data.get(referralKey+tradeCodeName));
			}*/
			
//			common.funcButtonSelection("Save");
//			common.funcButtonSelection("exit (Save First)");
	 		
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
			 
			 
			 
			 
			 if(k.isDisplayedField("MFD_PLMF")){
				 String[] geographical_Limits = ((String)data_map.get("MFD_PLMF")).split(",");
				 customAssert.assertTrue(k.Click("MFD_PLMF"),"Error while Clicking Geographic Limit List object . ");
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
	
	public boolean funcClaimsExperience(Map<Object, Object> map_data){

	    boolean retvalue = true;
	    
	      try {    
	    	  	//customAssert.assertTrue(common.funcMenuSelection("Navigate", "Claims Experience"),"Unable To navigate to Claims Experience screen");
				customAssert.assertTrue(common.funcPageNavigation("Claims Experience",""), "Claims Experience Page Navigation issue . ");
				TestUtil.reportStatus("Verified Claims Experience page .", "Info", true);
				//customAssert.assertTrue(common.funcButtonSelection("Next"), "Unable to click on Next Button on Claims Experience .");
				return retvalue;
	             
	      } catch(Throwable t) {
	             String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	          TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
	          Assert.fail("Unable to enter details in ClaimsExperience Page", t);
	          return false;
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
	
		public boolean funcPersonalAccident(Map<Object, Object> mdata) {
			double Comp_HW_Amt=0.00;
			double Comp_P_Amt=0.00;
			try {
				customAssert.assertTrue(common.funcPageNavigation("Personal Accident",""), "Personal Accident Page Navigation issue . ");
				
				if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
		         	
			    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
			      }
				// Retrieving the additional item count over the default item.
//				int iCount = (((String)mdata.get("Computer_Items_list")).split(";")).length;
//				
//				if(iCount>0){
					
					tempNP=0.00;
					Map<String, List<Map<String, String>>> internal_data_map = new HashMap<>();
					String Ctype;
					
					customAssert.assertTrue(k.DropDownSelection("PAA_all_persons_in_good_health",(String) mdata.get("PAA_all_persons_in_good_health")),
							"Unable to select PAA_all_persons_in_good_health ");
					if((k.getText("PAA_all_persons_in_good_health")).equalsIgnoreCase("No"))
						customAssert.assertTrue(k.Input("PAA_Details_of_health_problemTxtArea",(String) mdata.get("PAA_Details_of_health_problemTxtArea")),
							"Unable to select PAA_Details_of_health_problemTxtArea ");
					
					customAssert.assertTrue(k.DropDownSelection("PAA_all_persons_free_from_physical_defect_or_deformity",(String) mdata.get("PAA_all_persons_free_from_physical_defect_or_deformity")),
							"Unable to select PAA_all_persons_free_from_physical_defect_or_deformity ");
					if((k.getText("PAA_all_persons_free_from_physical_defect_or_deformity")).equalsIgnoreCase("No"))
						customAssert.assertTrue(k.Input("PAA_Details_of_health_problemTxtArea",(String) mdata.get("PAA_Details_of_health_problemTxtArea")),
							"Unable to select PAA_Details_of_health_problemTxtArea ");
					
					customAssert.assertTrue(k.DropDownSelection("PAA_cover_basis",(String) mdata.get("PAA_cover_basis")),
							"Unable to select PAA_cover_basis ");
					customAssert.assertTrue(k.DropDownSelection("PAA_illness_benifit",(String) mdata.get("PAA_illness_benifit")),
							"Unable to select PAA_illness_benifit ");
					
					customAssert.assertTrue(k.DropDownSelection("PAA_benefit_amount",(String) mdata.get("PAA_benefit_amount")),
							"Unable to select PAA_illnesPAA_benefit_amounts_benifit ");
					if((k.getText("PAA_benefit_amount")).equalsIgnoreCase("Other")){
						customAssert.assertTrue(k.Input("PAA_benefit_amount_OtherTxtBox",(String) mdata.get("PAA_benefit_amount_OtherTxtBox")),
							"Unable to select PAA_benefit_amount_OtherTxtBox ");
						customAssert.assertTrue(k.Input("PAA_Description_of_benefit_payable",(String) mdata.get("PAA_Description_of_benefit_payable")),
								"Unable to select PAA_Description_of_benefit_payable ");
							
					}
					
					int GP_index = k.getTableIndex("Activity Type", "xpath","html/body/div[3]/form/div/table");
					String GPtblxp = "html/body/div[3]/form/div/table["+GP_index+"]";
					// html/body/div[3]/form/div/table[2]/tbody/tr[1]/td[1]/input
					
					String Man_ATxpath = GPtblxp+"/tbody/tr[1]/td[1]/input"; // Activity Type Xpath for Manual
					String Man_ALxpath = GPtblxp+"/tbody/tr[1]/td[2]/input"; // Accumulation Limit Xpath for Manual
					String Man_NPxpath = GPtblxp+"/tbody/tr[1]/td[3]/input"; // Number of People Xpath for Manual
					String Man_AWxpath = GPtblxp+"/tbody/tr[1]/td[4]/input"; // Annual Wages Xpath for Manual
					

					String NonMan_ATxpath = GPtblxp+"/tbody/tr[2]/td[1]/input"; // Activity Type Xpath for Non Manual
					String NonMan_ALxpath = GPtblxp+"/tbody/tr[2]/td[2]/input"; // Accumulation Limit Xpath for Non Manual
					String NonMan_NPxpath = GPtblxp+"/tbody/tr[2]/td[3]/input"; // Number of People Xpath for Non Manual
					String NonMan_AWxpath = GPtblxp+"/tbody/tr[2]/td[4]/input"; // Annual Wages Xpath for Non Manual
					
					
					customAssert.assertEquals(k.getAttributeByXpath(Man_ATxpath, "value"), "Manual Work","Unable to verify Activity Type for Manual Work");
					customAssert.assertTrue(k.InputByXpath(Man_ALxpath, (String)mdata.get("PAA_Man_accu_limit")),"Unable to enter PAA_Man_accu_limit");
					customAssert.assertTrue(k.InputByXpath(Man_NPxpath, (String)mdata.get("PAA_Man_employee")),"Unable to enter PAA_Man_employee");
					customAssert.assertTrue(k.InputByXpath(Man_AWxpath, (String)mdata.get("PAA_Man_annual_wageroll")),"Unable to enter PAA_Man_annual_wageroll");
					
					customAssert.assertEquals(k.getAttributeByXpath(NonMan_ATxpath, "value"), "Non Manual Work","Unable to verify Activity Type for Non Manual Work");
					customAssert.assertTrue(k.InputByXpath(NonMan_ALxpath, (String)mdata.get("PAA_Non_Man_accu_limit")),"Unable to enter PAA_Non_Man_accu_limit");
					customAssert.assertTrue(k.InputByXpath(NonMan_NPxpath, (String)mdata.get("PAA_Non_Man_employee")),"Unable to enter PAA_Non_Man_employee");
					customAssert.assertTrue(k.InputByXpath(NonMan_AWxpath, (String)mdata.get("PAA_Non_Man_annual_wageroll")),"Unable to enter PAA_Non_Man_annual_wageroll");
					
					double ActTotalPeople = Double.parseDouble(k.getTextByXpath(GPtblxp+"/tbody/tr[3]/td[3]"));
					double ExpTotalPeople = Double.parseDouble((String)mdata.get("PAA_Non_Man_employee"))+ Double.parseDouble((String)mdata.get("PAA_Man_employee"));
					
					CommonFunction.compareValues(ExpTotalPeople, ActTotalPeople, "Total Number of People");
					
					customAssert.assertTrue(k.Click("PAA_applyBookrate"), "Unable to click on PAA_applyBookrate");
					k.waitTwoSeconds();
					
					double Discount=0.00;
					
					if(ExpTotalPeople >= 1 && ExpTotalPeople <= 3)
						Discount=Double.parseDouble(ObjectMap.properties.getProperty("Number_Band_1_to_3"));
					
					else if(ExpTotalPeople >= 4 && ExpTotalPeople <= 10)
						Discount=Double.parseDouble(ObjectMap.properties.getProperty("Number_Band_4_to_10"));
					
					else if(ExpTotalPeople >= 11 && ExpTotalPeople <= 20)
						Discount=Double.parseDouble(ObjectMap.properties.getProperty("Number_Band_11_to_20"));
					
					else if(ExpTotalPeople >= 21 && ExpTotalPeople <= 35)
						Discount=Double.parseDouble(ObjectMap.properties.getProperty("Number_Band_21_to_35"));
					
					else if(ExpTotalPeople >= 36 && ExpTotalPeople <= 50)
						Discount=Double.parseDouble(ObjectMap.properties.getProperty("Number_Band_36_to_50"));
					
					else if(ExpTotalPeople>50)
						Discount = -1;
					
					double Manual_IR = 0.00;
					double NonManual_IR = 0.00;
					double illness_IR=0.00;
					
					String s1 = "",s2;
					
					
					
					if((k.getAttribute("PAA_cover_basis", "value")).contains("24 hours"))
						s1 = "24Hours";
					else if((k.getAttribute("PAA_cover_basis", "value")).contains("including"))
							s1 = "Including";
					else if((k.getAttribute("PAA_cover_basis", "value")).contains("excluding"))
							s1 = "Excluding";
					
					s2 = (k.getAttribute("PAA_benefit_amount", "value")).replaceAll(" ","");
					s2 = s2.replaceAll("£","");
					s2 = s2.replaceAll(",", "");
					s2 = s2.replaceAll("€","");
					s2 = "_"+s2+"_";
					
					if(!ObjectMap.properties.getProperty(s1+s2+"Manual").contains("refer"))
						Manual_IR = Double.parseDouble(ObjectMap.properties.getProperty(s1+s2+"Manual"));
					
					if(!ObjectMap.properties.getProperty(s1+s2+"Non_Manual").contains("refer"))
						NonManual_IR = Double.parseDouble(ObjectMap.properties.getProperty(s1+s2+"Non_Manual"));
					
					if(ObjectMap.properties.getProperty(s1+s2+"Non_Manual").contains("refer"))
						Discount = 0.0;
					
					customAssert.assertTrue(k.Click("PAA_applyBookrate"), "Unable to click on PAA_applyBookrate");
					
					int mw = 1 ; // Manual Work Rating row
					int nmw = 2 ; // Non Manual Work Rating row
					int ill = 3 ; // Illness Rating row
					
					
					int PARate_index = k.getTableIndex("Description", "xpath","html/body/div[3]/form/div/table");
					String PA_Ratetblxp = "html/body/div[3]/form/div/table["+PARate_index+"]";
					
					//html/body/div[3]/form/div/table[5]/tbody/tr[1]/td[1]
					String r1 = "/tbody/tr[";
					String rc1 = "]/td[1]", rc2 = "]/td[2]", rc3 = "]/td[3]", rc4 = "]/td[4]/input", rc5 = "]/td[5]/input", 
					       rc6 = "]/td[6]/input", rc7 = "]/td[7]/input" , rc8 = "]/td[8]/input" , rc9 = "]/td[9]/input" , rc10 = "]/td[10]/input", rc11 = "]/td[11]/input";
					
					
					String act_Mw_desc = k.getTextByXpath(PA_Ratetblxp+r1+mw+rc1);
					String act_NMw_desc = k.getTextByXpath(PA_Ratetblxp + r1 + nmw + rc1);
					String act_Ill_desc = k.getTextByXpath(PA_Ratetblxp + r1 + ill + rc1);
					
					customAssert.assertTrue(act_Mw_desc.equals("Manual Work"),"Manual Work Description verification failed in rating table");
					customAssert.assertTrue(act_NMw_desc.equals("Non Manual Work"),"Non Manual Work Description verification failed in rating table");
					customAssert.assertTrue(act_Ill_desc.equals("Illness"),"Illness Description verification failed in rating table");

					// Employees Number
					CommonFunction.compareValues(Double.parseDouble((String)mdata.get("PAA_Man_employee")), Double.parseDouble(k.getTextByXpath(PA_Ratetblxp+r1+mw+rc2)), "Employee Number for Manual");
					CommonFunction.compareValues(Double.parseDouble((String)mdata.get("PAA_Non_Man_employee")), Double.parseDouble(k.getTextByXpath(PA_Ratetblxp+r1+nmw+rc2)), "Employee Number for Non Manual");
					
					// Employees Rateable factor
					CommonFunction.compareValues(Double.parseDouble((String)mdata.get("PAA_Man_annual_wageroll")), Double.parseDouble(k.getTextByXpath(PA_Ratetblxp+r1+mw+rc3)), "Rateable factor for Manual");
					CommonFunction.compareValues(Double.parseDouble((String)mdata.get("PAA_Non_Man_annual_wageroll")), Double.parseDouble(k.getTextByXpath(PA_Ratetblxp+r1+nmw+rc3)), "Rateable factor for Non Manual");
					
					//Initial Rate 
					CommonFunction.compareValues(Manual_IR, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+mw+rc4, "value")), "Initial Rate for Manual");
					CommonFunction.compareValues(NonManual_IR, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+nmw+rc4, "value")), "Initial Rate factor for Non Manual");
					
					//Auto Adjustment %
					CommonFunction.compareValues(-Discount, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+mw+rc5, "value")), "Auto Adjustment for Manual");
					CommonFunction.compareValues(-Discount, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+nmw+rc5, "value")), "Auto Adjustment factor for Non Manual");
					
					//Book Rate
									
					double exp_mw_br = (Manual_IR - (Manual_IR *(Discount/100)));
					double exp_nmw_br = (NonManual_IR - (NonManual_IR *(Discount/100)));
					
					CommonFunction.compareValues(exp_mw_br, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+mw+rc6, "value")), "Manual BookRate");
					CommonFunction.compareValues(exp_nmw_br, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+nmw+rc6, "value")), "Non Manual BookRate");
					
					//Book Premium
					
					double exp_mw_bp = Double.parseDouble(f.format(Double.parseDouble((String)mdata.get("PAA_Man_annual_wageroll")) * (exp_mw_br / 100)));
					double exp_nmw_bp = Double.parseDouble(f.format(Double.parseDouble((String)mdata.get("PAA_Non_Man_annual_wageroll")) * (exp_nmw_br / 100)));
					
					CommonFunction.compareValues(exp_mw_bp, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+mw+rc7, "value")), "Manual Book Premium");
					CommonFunction.compareValues(exp_nmw_bp, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+nmw+rc7, "value")), "Non Manual Premium");
					
					customAssert.assertTrue(k.InputByXpath(PA_Ratetblxp+r1+mw+rc8, (String)mdata.get("PAA_Man_TechAdjust")),"Unable to enter Manual work Tech Adjustment%");
					customAssert.assertTrue(k.InputByXpath(PA_Ratetblxp+r1+nmw+rc8, (String)mdata.get("PAA_Non_Man_TechAdjust")),"Unable to enter Non Manual work Tech Adjustment%");
					customAssert.assertTrue(k.InputByXpath(PA_Ratetblxp+r1+ill+rc8, (String)mdata.get("PAA_illness_TechAdjust")),"Unable to enter Illness Tech Adjustment%");
					
					customAssert.assertTrue(k.Click("PAA_applyBookrate"), "Unable to click on PAA_applyBookrate");
					k.waitTwoSeconds();
					
					// Revised Premium 
					
					double exp_mw_rp = Double.parseDouble(f.format(exp_mw_bp +( exp_mw_bp * (Double.parseDouble((String)mdata.get("PAA_Man_TechAdjust"))/100))));
					double exp_nmw_rp =  Double.parseDouble(f.format(exp_nmw_bp +( exp_nmw_bp * (Double.parseDouble((String)mdata.get("PAA_Non_Man_TechAdjust"))/100))));
					
					CommonFunction.compareValues(exp_mw_rp, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+mw+rc9, "value")), "Revised Premium for Manual Work ");
					CommonFunction.compareValues(exp_nmw_rp, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+nmw+rc9, "value")), "Revised Premium for Non Manual Work ");
					
					
					double exp_mw_np =exp_mw_rp;
					double exp_nmw_np =exp_nmw_rp;
					double exp_ill_rp = 0.00;
					
					customAssert.assertTrue(k.InputByXpath(PA_Ratetblxp+r1+mw+rc10, (String)mdata.get("PAA_Man_PremiumOveride")), "Unable to enter Premium Overide for Manual Work ");
					customAssert.assertTrue(k.InputByXpath(PA_Ratetblxp+r1+nmw+rc10, (String)mdata.get("PAA_Non_Man_PremiumOveride")), "Unable to enter Premium Overide for Non Manual Work ");
					
					if(Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+mw+rc10, "value"))>0){
						exp_mw_np=Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+mw+rc10, "value"));
					}
					if(Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+nmw+rc10, "value"))>0){
						exp_mw_np=Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+nmw+rc10, "value"));
					}
					
					
					
					if(((String) mdata.get("PAA_illness_benifit")).equalsIgnoreCase("Yes")){
						double exp_ill_rateable = Double.parseDouble(f.format(exp_mw_np + exp_nmw_np));
						CommonFunction.compareValues(exp_ill_rateable, Double.parseDouble(k.getTextByXpath(PA_Ratetblxp+r1+ill+rc3)), "Illness Rateable factor");
						
						double Ill_ir = Double.parseDouble(ObjectMap.properties.getProperty("Illness_Initial_Rate"));
						CommonFunction.compareValues(Ill_ir, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+ill+rc4,"value")),"Illness Initial Rate ");
						
						CommonFunction.compareValues(0, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+ill+rc5,"value")),"Illness Auto Adjustment ");
						
						CommonFunction.compareValues(Ill_ir, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+ill+rc6,"value")),"Illness Book Rate ");
						
						exp_ill_rp = Double.parseDouble(f.format(exp_ill_rateable *(Ill_ir / 100)));
						
						exp_ill_rp = exp_ill_rp + exp_ill_rp * (Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+ill+rc8,"value")) /100);
						
						CommonFunction.compareValues(exp_ill_rp, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+ill+rc9,"value")),"Illness Revised Premium ");
					
					}
									
					customAssert.assertTrue(k.InputByXpath(PA_Ratetblxp+r1+ill+rc10, (String)mdata.get("PAA_illness_PremiumOveride")), "Unable to enter Premium Overide for Illness ");
					
					
					double exp_ill_np =exp_ill_rp;
					
					if(Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+ill+rc10, "value"))>0){
						exp_ill_np=Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+mw+rc10, "value"));
					}
					
					CommonFunction.compareValues(exp_mw_np, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+mw+rc11, "value")), "Manual Work Premium");
					CommonFunction.compareValues(exp_nmw_np, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+nmw+rc11, "value")), "Non Manual Work Premium");
					CommonFunction.compareValues(exp_ill_np, Double.parseDouble(k.getAttributeByXpath(PA_Ratetblxp+r1+ill+rc11, "value")), "Illness Premium");
					
					double exp_Net_Premium_Total = Double.parseDouble(f.format(exp_mw_np+exp_nmw_np+exp_ill_np));
					
//					
					String act_CompPremium = k.getAttributeByXpath("//*[@id='paa_pa_tot']", "value");
					
					CommonFunction.compareValues(exp_Net_Premium_Total, Double.parseDouble(act_CompPremium), "Personal Accident Total Premium");	
				
					//System.out.println(act_CompPremium);
					//common.compareValues(CvNetP, Double.parseDouble(CompPremium), "Computerss Net Premium Value");
					mdata.put("PS_PersonalAccident_NetNetPremium", act_CompPremium);
					
					if((Discount == 0.0) &&(exp_Net_Premium_Total == 0.00) && (k.getAttribute("PAA_benefit_amount", "value").equals("Other"))){
						TestUtil.reportStatus(" Total is 0.00 due to Benefit amount is selected as 'Other', This quote would be referred", "Fail", true);
						
					}
				
				TestUtil.reportStatus("Rating table Calculated Sucessfully", "Info", true);

				return true;

			} catch (Throwable t) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				TestUtil.reportFunctionFailed("Failed in " + methodName + " function");
				k.reportErr("Failed in " + methodName + " function", t);
				Assert.fail("Unable to fill the details on Personal Accident Cover Page \n", t);
				return false;
			}

		}
	
		
		public boolean RatingCalculation(Map<Object, Object> mData, int rowNum) {
			boolean retVal = true;

			try {

				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();

				double SumIns = Double.parseDouble(driver.findElement(By.xpath("html/body/div[3]/form/div/table[6]/tbody/tr[" + rowNum + "]/td[3]")).getText());
				double BookRate = Double.parseDouble(driver.findElement(By.xpath("html/body/div[3]/form/div/table[6]/tbody/tr[" + rowNum + "]/td[5]/input")).getAttribute("value"));
				double BookPrem = Double.parseDouble(driver.findElement(By.xpath("html/body/div[3]/form/div/table[6]/tbody/tr[" + rowNum + "]/td[6]/input")).getAttribute("value"));
				double techAdjust = Double.parseDouble(driver.findElement(By.xpath("html/body/div[3]/form/div/table[6]/tbody/tr[" + rowNum + "]/td[7]/input")).getAttribute("value"));
				double revPrem = Double.parseDouble(driver.findElement(By.xpath("html/body/div[3]/form/div/table[6]/tbody/tr[" + rowNum + "]/td[8]/input")).getAttribute("value"));
				
				/*Assert.assertEquals(f.format(SumIns), f.format(Double.parseDouble(mData.get("Sum_Insured_" + rowNum))),
						"Non Portable Sum Insured Does not Match");
				//Assert.assertEquals(f.format(BookRate), f.format(Double.parseDouble(mData.get("Book_Rate_" + rowNum))),
						"Non Portable Book Rate does not match");
				//Assert.assertEquals(f.format(BookPrem), f.format(Double.parseDouble(mData.get("Book_Premium_" + rowNum))),
						"Non Portable Book Premium Does not match");
				//Assert.assertEquals(f.format(techAdjust),
						f.format(Double.parseDouble(mData.get("Tech_Adjust (%)_" + rowNum))),
						"Non Portable Technical Adjustment does not match");*/

				String flag = f.format(techAdjust);
				Double RevPrem = 00.00;

				if (flag.startsWith("-")) {
					RevPrem = BookPrem - (BookPrem * Math.abs(techAdjust) / 100);
				} else {
					RevPrem = BookPrem + (BookPrem * Math.abs(techAdjust) / 100);
				}

				//Assert.assertEquals(f.format(revPrem), f.format(RevPrem), "Revised premium computed is incorrect");

				double PremOverride = Double.parseDouble(driver.findElement(By.xpath("html/body/div[3]/form/div/table[6]/tbody/tr[" + rowNum + "]/td[9]/input")).getAttribute("value"));
				double Premium = Double.parseDouble(driver.findElement(By.xpath("html/body/div[3]/form/div/table[6]/tbody/tr[" + rowNum + "]/td[10]/input")).getAttribute("value"));

				String Prem = "";
				if (PremOverride > 00.00) {
					Prem = f.format(PremOverride);
				} else if (RevPrem == Premium) {
					Prem = f.format(RevPrem);
				}

				if (Prem == "") {
					k.reportErr("Failed in " + methodName + " function", t);
				}
				String sline;
				if(rowNum==1){
					 sline = "Non Portable rating ";}
				else{
					 sline = "Portable rating ";}
		
					
			
				//Assert.assertEquals(f.format(Double.parseDouble(mData.get("Premium_" + rowNum))), Prem,"Premium calculated is incorrect");
				TestUtil.reportStatus(sline+" Total Premium - "+Premium, "info", true);
				

			} catch (Throwable t) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				TestUtil.reportFunctionFailed("Failed in " + methodName + " function");
				k.reportErr("Failed in " + methodName + " function", t);
				Assert.fail("Unable to click on create - > new quote \n", t);
				return false;
			}
			return retVal;
		}
		
		public boolean funInputTechAdjust(Map<Object, Object> mdata) {
			Boolean retvalue = true;

			try {

				driver.findElement(By.xpath("html/body/div[3]/form/div/table[6]/tbody/tr[1]/td[7]/input"))
						.sendKeys(Keys.chord(Keys.CONTROL, "a"));
				driver.findElement(By.xpath("html/body/div[3]/form/div/table[6]/tbody/tr[1]/td[7]/input"))
						.sendKeys((String)mdata.get("Tech_Adjust (%)_1"));
				driver.findElement(By.xpath("html/body/div[3]/form/div/table[6]/tbody/tr[1]/td[7]/input"))
						.sendKeys(Keys.chord(Keys.TAB));

				driver.findElement(By.xpath("html/body/div[3]/form/div/table[6]/tbody/tr[2]/td[7]/input"))
						.sendKeys(Keys.chord(Keys.CONTROL, "a"));
				driver.findElement(By.xpath("html/body/div[3]/form/div/table[6]/tbody/tr[2]/td[7]/input"))
						.sendKeys((String)mdata.get("Tech_Adjust (%)_2"));
				driver.findElement(By.xpath("html/body/div[3]/form/div/table[6]/tbody/tr[2]/td[7]/input"))
						.sendKeys(Keys.chord(Keys.TAB));

				TestUtil.reportStatus("Technical Adjustment applied sucessfullly", "Info", false);

			} catch (Throwable t) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				TestUtil.reportFunctionFailed("Failed in " + methodName + " function");
				k.reportErr("Failed in " + methodName + " function", t);
				Assert.fail("Unable to click on create - > new quote \n", t);
				return false;
			}
			return retvalue;

		}
		
// END OF FUNCTION

}
