package com.selenium.commonfiles.base;

import static com.selenium.commonfiles.util.TestUtil.WriteDataToXl;

import java.awt.AWTException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

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
import com.selenium.commonfiles.util.ObjectMap;
import com.selenium.commonfiles.util.TestUtil;

public class CommonFunction_COA extends TestBase{

	SimpleDateFormat df = new SimpleDateFormat();
	Properties PLEL_Rater = new Properties();
	public int actual_no_of_years=0,err_count=0,trans_error_val=0,Can_returnP_Error=0;
	public Map<String,Double> Book_rate_Rater_output = new HashMap<>();
	public Map<String,Double> TC_Calculations = new HashMap<>();
	public boolean isIncludingHeatPresent = false,isExcludingHeatPresent = false,isIncludeHeat_10M=false;
	public double cent_work_including_heat = 0.0; 
	Date currentDate = new Date();
	public List<String> CoversDetails_data_list = null;
	public static String Environment = null;
	public double JCT_TotalPremium = 0.0, OP_TotalPremium = 0.00, Ter_TotalPremium = 0.00; 
	public Map<String,Map<String,Double>> CAN_COA_ReturnP_Values_Map = new HashMap<>();
	public String MTARewindFlow = "";
	public boolean PremiumFlag = false , isTradeCodeSelected = false;
	
	
	
	/**
	 * 
	 * Below all variable are related to COA - MTA.  
	 * 
	 */
	public static WebElement taxTable_tBody;
	public static WebElement objTable;
	public static WebElement taxTable_tHead;
	public double totalGrossTax = 0.0,totalGrossTaxMTA = 0.0,totalGrossPremium = 0.0,totalGrossPremiumMTA=0.0,totalNetPremiumTax=0.0,totalNetPremiumTaxMTA=0.0;
	public static int countOfCovers,countOfTableRows;
	public static Map<Object, Integer> variableTaxAdjustmentIDs = null;
	public static Map<Object, Integer> variableTaxAdjustmentIDsMTA = null;
	public static Map<Object, Double> grossTaxValues_Map = null;
	public static Map<Object, Map<Object, Object>> variableTaxAdjustmentVerificationMaps = null;
	public static Map<Object, Object> variableTaxAdjustmentDataMaps = null;
	public static Map<Object, Object> variableTaxAdjustmentDataMapsMTA = null;
	public static List<Object> headerNameStorage = null;
	public static List<Object> headerNameStorageMTA = null;
	public Map<String,Map<String,Double>> transaction_Details_Premium_Values = new HashMap<>();
	public boolean isInsuranceTaxDone = false;
	public static double adjustedPremium = 0.0,adjustedTotalPremium=0.0,adjustedTotalPremiumMTA=0.0,adjustedTotalTax=0.0,adjustedTotalTaxMTA=0.0,unAdjustedTotalTax=0.0,unAdjustedTotalTaxMTA=0.0;
	public static ArrayList<Object> inputarraylist = null;
	public static ArrayList<Object> inputarraylistMTA = null;
	public static Map<String, Double> Adjusted_Premium_map = null;
	public static int errorVal=0,counter = 0;
	public static Map<String , String> AdjustedTaxDetails = new LinkedHashMap<String, String>();
	public static Map<String , String> AdjustedTaxCollection = new LinkedHashMap<String, String>();
	
	/**
	 * ---------------------------------------------------------------------------------------------------
	 * 
	 */
	
	public void NewBusinessFlow(String code,String event) throws ErrorInTestMethod{
		String testName = (String)common.NB_excel_data_map.get("Automation Key");
		String navigationBy = CONFIG.getProperty("NavigationBy");
		try{
			common.currentRunningFlow = "NB";
			//customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Previous Claims  . ");
			customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
			customAssert.assertTrue(common.checkClient(common.NB_excel_data_map,code,event),"Unable to check Client.");
			customAssert.assertTrue(common.createNewQuote(common.NB_excel_data_map,code,event), "Unable to create new quote.");
			customAssert.assertTrue(common.selectLatestQuote(common.NB_excel_data_map,code,event), "Unable to select quote from table.");
			customAssert.assertTrue(funcPolicyGeneral(common.NB_excel_data_map,code,event), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcPreviousClaims(common.NB_excel_data_map), "Previous claim function is having issue(S) .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Claims Experience"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcClaimsExperience(common.NB_excel_data_map), "Previous claim function is having issue(S) .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common_VELA.funcCovers(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts and Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			customAssert.assertTrue(func_Referrals_MaterialFactsDeclerationPage(), "Referrals for MaterialFactsDeclerationPage function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"General Risk Details"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(GeneralRiskDetailsPage(common.NB_excel_data_map), "GeneralRiskDetailsPage function is having issue(S) . ");
			
			if(((String)common.NB_excel_data_map.get("CD_Public&ProductsLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen.");
				customAssert.assertTrue(PublicProductsLiabilityPage(common.NB_excel_data_map), "PublicProductsLiabilityPage function is having issue(S) . ");
			}
			if(((String)common.NB_excel_data_map.get("CD_EmployersLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to Employers Liability screen.");
				customAssert.assertTrue(EmployersLiabilityPage(common.NB_excel_data_map), "EmployersLiabilityPage function is having issue(S) . ");
			}
			if(((String)common.NB_excel_data_map.get("CD_AnnualWorks")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Annual Works"),"Issue while Navigating to Annual Works screen.");
				customAssert.assertTrue(common_COB.AnnualWorkPage(common.NB_excel_data_map), "AnnualWorkPage function is having issue(S) . ");
			}
			if(((String)common.NB_excel_data_map.get("CD_SingleProject")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Single Project"),"Issue while Navigating to Single Project screen.");
				customAssert.assertTrue(SingleProjectPage(common.NB_excel_data_map), "SingleProjectPage function is having issue(S) . ");
			}
			if(((String)common.NB_excel_data_map.get("CD_OwnPlant")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Own Plant"),"Issue while Navigating to Own Plant screen . ");
				customAssert.assertTrue(common_COB.OwnPlantsPage(common.NB_excel_data_map), "OwnPlantsPage function is having issue(S) . ");
				
			}
			if(((String)common.NB_excel_data_map.get("CD_HiredInPlant")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Hired In Plant"),"Issue while Navigating to Employers Liability screen.");
				customAssert.assertTrue(HireInPlantPage(common.NB_excel_data_map), "HireInPlantPage function is having issue(S) . ");
			}
		
			if(((String)common.NB_excel_data_map.get("CD_JCT6.5.1")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to JCT 6.5.1 screen.");
				customAssert.assertTrue(JCTPage(common.NB_excel_data_map), "JCTPage function is having issue(S) . ");
				
			}
			if(((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Terrorism"),"Issue while Navigating to Terrorism screen.");
				customAssert.assertTrue(TerrorismPage(common.NB_excel_data_map), "TerrorismPage function is having issue(S) . ");
				
			}
			if(((String)common.NB_excel_data_map.get("CD_PropertyOwnersLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Property Owners Liability"),"Issue while Navigating to Property Owners Liability screen.");
				customAssert.assertTrue(PropertyOwnersLiabilityPage(common.NB_excel_data_map), "PropertyOwnersLiabilityPage function is having issue(S) . ");
				
			}
			if(((String)common.NB_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){	
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
				customAssert.assertTrue(funcLegalExpenses(common.NB_excel_data_map,code,event), "Legal Expenses function is having issue(S) . ");
			}
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
			customAssert.assertTrue(common_HHAZ.funcEndorsementOperations(common.NB_excel_data_map),"Insurance tax adjustment is having issue(S).");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_VELA.funcPremiumSummary(common.NB_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			Assert.assertTrue(common_VELA.funcStatusHandling(common.NB_excel_data_map,code,event));
			if(TestBase.businessEvent.equals("NB")){
				customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
				customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
				common_VELA.PremiumFlag = false;
				TestUtil.reportTestCasePassed(testName);
				}
			//TestUtil.reportTestCasePassed(testName);
				
				//}
		
		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
		
	}
	public void RewindFlow(String code,String event) throws ErrorInTestMethod{
		String testName = (String)common.Rewind_excel_data_map.get("Automation Key");
		try{
			System.out.println("Test method of Rewind For - "+code);
			TestUtil.reportStatus("Test Method of Rewind For - "+code, "Pass", true);
			
			if(!common.currentRunningFlow.equalsIgnoreCase("Renewal") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
				NewBusinessFlow(code,"NB");
			}
			common.currentRunningFlow="Rewind";
	//		common.CoversDetails_data_list.clear();
			String navigationBy = CONFIG.getProperty("NavigationBy");
			PremiumFlag = false;
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
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common_VELA.funcCovers(common.Rewind_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts and Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			customAssert.assertTrue(func_Referrals_MaterialFactsDeclerationPage(), "Referrals for MaterialFactsDeclerationPage function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"General Risk Details"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(GeneralRiskDetailsPage(common.Rewind_excel_data_map), "GeneralRiskDetailsPage function is having issue(S) . ");
			
			if(((String)common.Rewind_excel_data_map.get("CD_Public&ProductsLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen.");
				customAssert.assertTrue(PublicProductsLiabilityPage(common.Rewind_excel_data_map), "PublicProductsLiabilityPage function is having issue(S) . ");
			}
			if(((String)common.Rewind_excel_data_map.get("CD_EmployersLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to Employers Liability screen.");
				customAssert.assertTrue(EmployersLiabilityPage(common.Rewind_excel_data_map), "EmployersLiabilityPage function is having issue(S) . ");
			}
			if(((String)common.Rewind_excel_data_map.get("CD_AnnualWorks")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Annual Works"),"Issue while Navigating to Annual Works screen.");
				customAssert.assertTrue(common_COB.AnnualWorkPage(common.Rewind_excel_data_map), "AnnualWorkPage function is having issue(S) . ");
			}
			if(((String)common.Rewind_excel_data_map.get("CD_SingleProject")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Single Project"),"Issue while Navigating to Single Project screen.");
				customAssert.assertTrue(SingleProjectPage(common.Rewind_excel_data_map), "SingleProjectPage function is having issue(S) . ");
			}
			if(((String)common.Rewind_excel_data_map.get("CD_OwnPlant")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Own Plant"),"Issue while Navigating to Own Plant screen . ");
				customAssert.assertTrue(common_COB.OwnPlantsPage(common.Rewind_excel_data_map), "OwnPlantsPage function is having issue(S) . ");
				
			}
			if(((String)common.Rewind_excel_data_map.get("CD_HiredInPlant")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Hired In Plant"),"Issue while Navigating to Employers Liability screen.");
				customAssert.assertTrue(HireInPlantPage(common.Rewind_excel_data_map), "HireInPlantPage function is having issue(S) . ");
			}
		
			if(((String)common.Rewind_excel_data_map.get("CD_JCT6.5.1")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to JCT 6.5.1 screen.");
				customAssert.assertTrue(JCTPage(common.Rewind_excel_data_map), "JCTPage function is having issue(S) . ");
				
			}
			if(((String)common.Rewind_excel_data_map.get("CD_Terrorism")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Terrorism"),"Issue while Navigating to Terrorism screen.");
				customAssert.assertTrue(TerrorismPage(common.Rewind_excel_data_map), "TerrorismPage function is having issue(S) . ");
				
			}
			if(((String)common.Rewind_excel_data_map.get("CD_PropertyOwnersLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Property Owners Liability"),"Issue while Navigating to Property Owners Liability screen.");
				customAssert.assertTrue(PropertyOwnersLiabilityPage(common.Rewind_excel_data_map), "PropertyOwnersLiabilityPage function is having issue(S) . ");
				
			}
			if(((String)common.Rewind_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){	
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
				customAssert.assertTrue(funcLegalExpenses(common.Rewind_excel_data_map,code,event), "Legal Expenses function is having issue(S) . ");
			}
//			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
//			customAssert.assertTrue(common.funcEndorsementOperations(common.Rewind_excel_data_map),"Insurance tax adjustment is having issue(S).");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_VELA.funcPremiumSummary_MTA(common.Rewind_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			if(!TestBase.businessEvent.equalsIgnoreCase("MTA")){
				customAssert.assertTrue(common_VELA.funcStatusHandling(common.Rewind_excel_data_map,code,event),"Status Handling Function is having issues.");
			}
			
			
				customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
				customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
				
				TestUtil.reportTestCasePassed(testName);
			
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
			common_VELA.PremiumFlag = false;
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcButtonSelection("Re-Quote"));
			
			TestUtil.reportStatus("<b> -----------------------Requote flow is started---------------------- </b>", "Info", false);
			
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
			customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Requote_excel_data_map,code,event,"Submitted"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
			
			customAssert.assertTrue(funcPolicyGeneral(common.Requote_excel_data_map,code,event), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcPreviousClaims(common.Requote_excel_data_map), "Previous claim function is having issue(S) .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Claims Experience"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcClaimsExperience(common.Requote_excel_data_map), "Previous claim function is having issue(S) .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common_VELA.funcCovers(common.Requote_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts and Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			customAssert.assertTrue(func_Referrals_MaterialFactsDeclerationPage(), "Referrals for MaterialFactsDeclerationPage function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"General Risk Details"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(GeneralRiskDetailsPage(common.Requote_excel_data_map), "GeneralRiskDetailsPage function is having issue(S) . ");
			
			if(((String)common.Requote_excel_data_map.get("CD_Public&ProductsLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen.");
				customAssert.assertTrue(PublicProductsLiabilityPage(common.Requote_excel_data_map), "PublicProductsLiabilityPage function is having issue(S) . ");
			}
			if(((String)common.Requote_excel_data_map.get("CD_EmployersLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to Employers Liability screen.");
				customAssert.assertTrue(EmployersLiabilityPage(common.Requote_excel_data_map), "EmployersLiabilityPage function is having issue(S) . ");
			}
			if(((String)common.Requote_excel_data_map.get("CD_AnnualWorks")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Annual Works"),"Issue while Navigating to Annual Works screen.");
				customAssert.assertTrue(common_COB.AnnualWorkPage(common.Requote_excel_data_map), "AnnualWorkPage function is having issue(S) . ");
			}
			if(((String)common.Requote_excel_data_map.get("CD_SingleProject")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Single Project"),"Issue while Navigating to Single Project screen.");
				customAssert.assertTrue(SingleProjectPage(common.Requote_excel_data_map), "SingleProjectPage function is having issue(S) . ");
			}
			if(((String)common.Requote_excel_data_map.get("CD_OwnPlant")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Own Plant"),"Issue while Navigating to Own Plant screen . ");
				customAssert.assertTrue(common_COB.OwnPlantsPage(common.Requote_excel_data_map), "OwnPlantsPage function is having issue(S) . ");
				
			}
			if(((String)common.Requote_excel_data_map.get("CD_HiredInPlant")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Hired In Plant"),"Issue while Navigating to Employers Liability screen.");
				customAssert.assertTrue(HireInPlantPage(common.Requote_excel_data_map), "HireInPlantPage function is having issue(S) . ");
			}
		
			if(((String)common.Requote_excel_data_map.get("CD_JCT6.5.1")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to JCT 6.5.1 screen.");
				customAssert.assertTrue(JCTPage(common.Requote_excel_data_map), "JCTPage function is having issue(S) . ");
				
			}
			if(((String)common.Requote_excel_data_map.get("CD_Terrorism")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Terrorism"),"Issue while Navigating to Terrorism screen.");
				customAssert.assertTrue(TerrorismPage(common.Requote_excel_data_map), "TerrorismPage function is having issue(S) . ");
				
			}
			if(((String)common.Requote_excel_data_map.get("CD_PropertyOwnersLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Property Owners Liability"),"Issue while Navigating to Property Owners Liability screen.");
				customAssert.assertTrue(PropertyOwnersLiabilityPage(common.Requote_excel_data_map), "PropertyOwnersLiabilityPage function is having issue(S) . ");
				
			}
			if(((String)common.Requote_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){	
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
				customAssert.assertTrue(funcLegalExpenses(common.Requote_excel_data_map,code,event), "Legal Expenses function is having issue(S) . ");
			}
			
			//customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
			//customAssert.assertTrue(common_HHAZ.funcEndorsementOperations(common.NB_excel_data_map),"Insurance tax adjustment is having issue(S).");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_VELA.funcPremiumSummary(common.Requote_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			Assert.assertTrue(common_VELA.funcStatusHandling(common.Requote_excel_data_map,code,event));
			customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
			customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
			common_VELA.PremiumFlag = false;
			TestUtil.reportTestCasePassed(testName);
		
			
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
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcPreviousClaims(common.Renewal_excel_data_map), "Previous claim function is having issue(S) .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Claims Experience"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcClaimsExperience(common.Renewal_excel_data_map), "Previous claim function is having issue(S) .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common_VELA.funcCovers(common.Renewal_excel_data_map), "Select covers function is having issue(S) . ");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts and Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			
			customAssert.assertTrue(func_Referrals_MaterialFactsDeclerationPage(), "Referrals for MaterialFactsDeclerationPage function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"General Risk Details"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(GeneralRiskDetailsPage(common.Renewal_excel_data_map), "GeneralRiskDetailsPage function is having issue(S) . ");
			
			if(((String)common.Renewal_excel_data_map.get("CD_Public&ProductsLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen.");
				customAssert.assertTrue(PublicProductsLiabilityPage(common.Renewal_excel_data_map), "PublicProductsLiabilityPage function is having issue(S) . ");
			}
			if(((String)common.Renewal_excel_data_map.get("CD_EmployersLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to Employers Liability screen.");
				customAssert.assertTrue(EmployersLiabilityPage(common.Renewal_excel_data_map), "EmployersLiabilityPage function is having issue(S) . ");
			}
			if(((String)common.Renewal_excel_data_map.get("CD_AnnualWorks")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Annual Works"),"Issue while Navigating to Annual Works screen.");
				customAssert.assertTrue(common_COB.AnnualWorkPage(common.Renewal_excel_data_map), "AnnualWorkPage function is having issue(S) . ");
			}
			if(((String)common.Renewal_excel_data_map.get("CD_SingleProject")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Single Project"),"Issue while Navigating to Single Project screen.");
				customAssert.assertTrue(SingleProjectPage(common.Renewal_excel_data_map), "SingleProjectPage function is having issue(S) . ");
			}
			if(((String)common.Renewal_excel_data_map.get("CD_OwnPlant")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Own Plant"),"Issue while Navigating to Own Plant screen . ");
				customAssert.assertTrue(common_COB.OwnPlantsPage(common.Renewal_excel_data_map), "OwnPlantsPage function is having issue(S) . ");
				
			}
			if(((String)common.Renewal_excel_data_map.get("CD_HiredInPlant")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Hired In Plant"),"Issue while Navigating to Employers Liability screen.");
				customAssert.assertTrue(HireInPlantPage(common.Renewal_excel_data_map), "HireInPlantPage function is having issue(S) . ");
			}
		
			if(((String)common.Renewal_excel_data_map.get("CD_JCT6.5.1")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to JCT 6.5.1 screen.");
				customAssert.assertTrue(JCTPage(common.Renewal_excel_data_map), "JCTPage function is having issue(S) . ");
				
			}
			if(((String)common.Renewal_excel_data_map.get("CD_Terrorism")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Terrorism"),"Issue while Navigating to Terrorism screen.");
				customAssert.assertTrue(TerrorismPage(common.Renewal_excel_data_map), "TerrorismPage function is having issue(S) . ");
				
			}
			if(((String)common.Renewal_excel_data_map.get("CD_PropertyOwnersLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Property Owners Liability"),"Issue while Navigating to Property Owners Liability screen.");
				customAssert.assertTrue(PropertyOwnersLiabilityPage(common.Renewal_excel_data_map), "PropertyOwnersLiabilityPage function is having issue(S) . ");
				
			}
			if(((String)common.Renewal_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){	
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
				customAssert.assertTrue(funcLegalExpenses(common.Renewal_excel_data_map,code,event), "Legal Expenses function is having issue(S) . ");
			}
			
			//customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
			//customAssert.assertTrue(common_HHAZ.funcEndorsementOperations(common.Renewal_excel_data_map),"Insurance tax adjustment is having issue(S).");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_VELA.funcPremiumSummary(common.Renewal_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			
			Assert.assertTrue(common_VELA.funcStatusHandling(common.Renewal_excel_data_map,code,event));
			customAssert.assertEquals(common_VELA.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
				
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
	
	public boolean SingleProjectPage(Map<Object, Object> map_data){
		boolean retValue = true;
		String abvr = null, coverName = null;
		String s_Sheet =null;
		
		
		try{
			 customAssert.assertTrue(common.funcPageNavigation("Single Project", ""),"Single Project page is having issue(S)");
			 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	            	
		    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
		      }
			 String[] rist_mgmt = ((String)common.NB_excel_data_map.get("SP_ToBeInsured")).split(",");
			 List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
			 for(String risk : rist_mgmt){
			 	for(WebElement each_ul : ul_elements){
			 	    customAssert.assertTrue(k.Click("COB_AW_InterestsInsured"),"Error while Clicking InterestsInsured List object . ");
					k.waitTwoSeconds();
					if(each_ul.findElement(By.xpath("//li[text()='"+risk+"']")).isDisplayed())
						each_ul.findElement(By.xpath("//li[text()='"+risk+"']")).click();
					else
						continue;
					break;
				}
			 }
			 
			 customAssert.assertTrue(k.Input("COB_SP_FullDescription", (String)map_data.get("SP_FullDescription")),"Unable to put value in FullDescription field");
			 customAssert.assertTrue(k.Input("COB_SP_ContractDuration", (String)map_data.get("SP_ContractDuration")),"Unable to put value in ContractDuration field");
			 customAssert.assertTrue(k.DropDownSelection("COB_SP_MaintenancePeriod", (String)map_data.get("SP_MaintenancePeriod")),"Unable to put value in MaintenancePeriod field");		 
			 customAssert.assertTrue(k.Input("COB_SP_BFSCPayments", (String)map_data.get("SP_BFSCPayments")),"Unable to put value in BFSCPayments field");		 
			 customAssert.assertTrue(k.Input("COB_SP_BriefDescription", (String)map_data.get("SP_BriefDescription")),"Unable to put value in BriefDescription field");
			 customAssert.assertTrue(k.DropDownSelection("COB_SP_ErectionOfTimber", (String)map_data.get("SP_ErectionOfTimber")),"Unable to put value in ErectionOfTimber field");
			 
			 customAssert.assertTrue(k.Input("COB_SP_ContractValue", (String)map_data.get("SP_ContractValue")),"Unable to put value in ContractValue field");
			 customAssert.assertTrue(k.Input("COB_SP_DeclarationCondition", (String)map_data.get("SP_DeclarationCondition")),"Unable to put value in DeclarationCondition field");
			 customAssert.assertTrue(k.Input("COB_SP_LOI", (String)map_data.get("SP_LOI")),"Unable to put value in LOI field");
			 
			// customAssert.assertTrue(k.DropDownSelection("COB_SP_TypeOfLimit", (String)map_data.get("SP_TypeOfLimit")),"Unable to put value in TypeOfLimit field");
			 
			 String sVal = (String)map_data.get("SP_AddBespoke");
			 if(sVal.length() > 0){
				 abvr = "SP_AddB_";
				 coverName = "SP";
				 customAssert.assertTrue(funcAddBespoke(map_data, sVal, abvr,coverName),"Unable to add bspoke item for SP cover");
			 }
			 
			 // Click on Calculate Premium button :
			 
			 customAssert.assertTrue(k.Click("COB_Btn_ApplyBookRate"),"Unable to click on ApplyBookRate button");
			 
			 // Get table id :
			 
			 	String sUniqueCol ="Tech. Adjust (%)";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
				coverName = "Single Project";
				abvr = "SP_";
				
				customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, sTablePath, coverName, abvr),"Unable to handle manual rated premium table on Annual Works screen");	 
							
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



	public boolean AnnualWorkPage(Map<Object, Object> map_data){
		boolean retValue = true;
		String abvr = null, coverName = null;
		String s_Sheet =null;
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
		
		try{
			 customAssert.assertTrue(common.funcPageNavigation("Annual Works", ""),"Annual Works page is having issue(S)");
			 
			 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	            	
		    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
		      }
			 
			 String[] rist_mgmt = ((String)map_data.get("AW_InterestsInsured")).split(",");
			 List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
			 for(String risk : rist_mgmt){
			 	for(WebElement each_ul : ul_elements){
			 	    customAssert.assertTrue(k.Click("COB_AW_InterestsInsured"),"Error while Clicking InterestsInsured List object . ");
					k.waitTwoSeconds();
					if(each_ul.findElement(By.xpath("//li[text()='"+risk+"']")).isDisplayed())
						each_ul.findElement(By.xpath("//li[text()='"+risk+"']")).click();
					else
						continue;
					break;
				}
			 }
			 
			 customAssert.assertTrue(k.Input("COB_AW_ContractingTurnover", (String)map_data.get("AW_ContractingTurnover")),"Unable to put value in ContractingTurnover field");
			 customAssert.assertTrue(k.Input("COB_AW_PercentageEstimate_JCT", (String)map_data.get("AW_PercentageEstimate_JCT")),"Unable to put value in PercentageEstimate_JCT field");
			 customAssert.assertTrue(k.Input("COB_AW_PercentageWork_MainContractor", (String)map_data.get("AW_PercentageWork_MainContractor")),"Unable to put value in PercentageWork MainContractor field");
			 customAssert.assertTrue(k.Input("COB_AW_MaxContractPeriod", (String)map_data.get("AW_MaxContractPeriod")),"Unable to put value in MaxContractPeriod field");
			 customAssert.assertTrue(k.Input("COB_AW_BFSCPayments", (String)map_data.get("AW_BFSCPayments")),"Unable to put value in BFSCPayments field");
			 
			 customAssert.assertTrue(k.DropDownSelection("COB_AW_ErectionOfTimberFrame", (String)map_data.get("AW_ErectionOfTimberFrame")),"Unable to put value in ErectionOfTimberFrame field");
			 try{
				 k.InputWithoutError("COB_AW_SumInsured_ContractValue", (String)map_data.get("AW_SumInsured_ContractValue"));
			 }catch(Exception e){
				 
			 }
			 
			 //customAssert.assertTrue(k.Input("COB_AW_SumInsured_ContractValue", (String)map_data.get("AW_SumInsured_ContractValue")),"Unable to put value in SumInsured_ContractValue field");
			 customAssert.assertTrue(k.Input("COB_AW_DeclarationCondition", (String)map_data.get("AW_DeclarationCondition")),"Unable to put value in DeclarationCondition field");
			 customAssert.assertTrue(k.Input("COB_AW_LOI", (String)map_data.get("AW_LOI")),"Unable to put value in LOI field");
			 
			// customAssert.assertTrue(k.DropDownSelection("COB_AW_TypeOfLimit", (String)map_data.get("AW_TypeOfLimit")),"Unable to put value in TypeOfLimit field");
			 
			 
			 
			// Add Edit Wages Breakdown :
			 
			  String  percentVal = k.getText("COB_BD_Percentage_Freeze");
			 
			 if(percentVal.length() < 1){
				 String sValue = (String)map_data.get("AW_AddEditWagesBreakdown");
				 
				 if(sValue.contains("Yes")){
					 coverName = "Annual Works";
					 abvr = "AW";
					 customAssert.assertTrue(k.Click("COB_Btn_AddWagesBreakdown"),"Unable to click on AddWagesBreakdown button");
					 customAssert.assertTrue(AddWages(map_data, coverName, abvr),"");
				 }
			 }
			 
			 
			 /*String sValue = (String)map_data.get("AwAddContract_01;");
			 if(sValue.length() > 0){
				 abvr = "AW_AD_";
				 coverName = "AW";
				 customAssert.assertTrue(funcAddContract(map_data, sValue, abvr,coverName),"Unable to add contact for AW cover");
			 }*/
				 
			 
			String sVal = (String)map_data.get("AW_AddBespoke");
			abvr = "AW_AddB_";
			 coverName = "AW";
			 if(sVal.length() > 0){
				 
				 customAssert.assertTrue(funcAddBespoke(map_data, sVal, abvr,coverName),"Unable to add bspoke item for AW cover");
			 }
			 
			 // Click on Calculate Premium button :
			 
			 customAssert.assertTrue(k.Click("COB_Btn_ApplyBookRate"),"Unable to click on ApplyBookRate button");
			 
			 // Get table id :
			 
			 	String sUniqueCol ="Tech. Adjust (%)";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
				coverName = "Annual Works";
				abvr = "AW_";
				
				customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, sTablePath, coverName, abvr),"Unable to handle Auto rated premium table on Annual Works screen");	 
							
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


	public boolean EmployersLiabilityPage(Map<Object, Object> map_data){
		boolean retValue = true;
		String abvr = null, coverName = null;
		String s_Sheet =null;
		
		
		try{
			 customAssert.assertTrue(common.funcPageNavigation("Employers Liability", ""),"Employers Liability page is having issue(S)");
			 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	            	
		    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
		      }
			 customAssert.assertTrue(k.Input("COB_EL_DeclarationCondition", (String)map_data.get("EL_DeclarationCondition")),"Unable to put value in DeclarationCondition field");
			 customAssert.assertTrue(k.DropDownSelection("COB_EL_LOI", (String)map_data.get("EL_LOI")),"Unable to put value in LOI field");
			// customAssert.assertTrue(k.DropDownSelection("COB_EL_TypeOfLimit", (String)map_data.get("EL_TypeOfLimit")),"Unable to put value in TypeOfLimit field");
			
			// Add Edit Wages Breakdown :
			  
			 
			 String  percentVal = k.getText("COB_BD_Percentage_Freeze");
			 
			 if(percentVal.length() < 1){
				 String sValue = (String)map_data.get("EL_AddEditWagesBreakdown");
				 
				 if(sValue.contains("Yes")){
					 coverName = "Employers Liability";
					 abvr = "EL";
					 customAssert.assertTrue(k.Click("COB_Btn_AddWagesBreakdown"),"Unable to click on AddWagesBreakdown button");
					 customAssert.assertTrue(AddWages(map_data, coverName, abvr),"");
				 }
			 }
			 
			String sVal = (String)map_data.get("EL_AddBespokeEL");
			 if(sVal.length() > 0){
				 abvr = "EL_AddB_";
				 coverName = "EL";
				 customAssert.assertTrue(funcAddBespoke(map_data, sVal, abvr,coverName),"Unable to add bspoke item for EL cover");
			 }
			 
			 // Click on Calculate Premium button :
			 
			 customAssert.assertTrue(k.Click("COB_Btn_ApplyBookRate"),"Unable to click on ApplyBookRate button");
			 
			 // Get table id :
			 
			 	String sUniqueCol ="Tech. Adjust (%)";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
				coverName = "Employers Liability";
				abvr = "EL_";
				
				customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, sTablePath, coverName, abvr),"Unable to handle manual rated premium table for Employers Liability screen");	 
							
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


	public boolean PublicProductsLiabilityPage(Map<Object, Object> map_data){
		boolean retValue = true;
		String abvr = null, coverName = null;
		String s_Sheet =null;
		
		
		try{
			 customAssert.assertTrue(common.funcPageNavigation("Public & Products Liability", ""),"Public & Products Liability page is having issue(S)");
			 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	            	
		    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
		      }
			 customAssert.assertTrue(k.SelectRadioBtn("COB_PPL_BonaFide", (String)map_data.get("PPL_BonaFide")),"Unable to put value in BonaFide field");
			 String b_Val =(String)map_data.get("PPL_BonaFide");
			 
			 if(b_Val.contains("Yes")){
				customAssert.assertTrue(k.Input("COB_PPL_BFSC_Payments", (String)map_data.get("PPL_BFSC_Payments")),"Unable to put value in BFSC_Payments field");
				customAssert.assertTrue(k.Input("COB_PPL_BF_PecentTotalWork", (String)map_data.get("PPL_BF_PecentTotalWork")),"Unable to put value in PecentTotalWork field");
				 
				 customAssert.assertTrue(k.SelectRadioBtn("COB_PPL_BF_WrittenRecord", (String)map_data.get("PPL_BF_WrittenRecord")),"Unable to put value in WrittenRecord field");
				 //referral code
				 if(((String)map_data.get("PPL_BF_WrittenRecord")).equalsIgnoreCase("No"))
					 common_VELA.referrals_list.add((String)map_data.get("RM_Public&ProductsLiability_NoWrittenRecord"));
			 }
			 
			 String[] rist_mgmt = ((String)map_data.get("PPL_PL_RiskManagement")).split(",");
			 List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
			 for(String risk : rist_mgmt){
			 	for(WebElement each_ul : ul_elements){
			 	    customAssert.assertTrue(k.Click("COB_PPL_PL_RiskManagement"),"Error while Clicking RiskManagement List object . ");
					k.waitTwoSeconds();
					if(each_ul.findElement(By.xpath("//li[text()='"+risk+"']")).isDisplayed())
						each_ul.findElement(By.xpath("//li[text()='"+risk+"']")).click();
					else
						continue;
					break;
				}
			 }
			 
			 customAssert.assertTrue(k.Input("COB_PPL_DescribeProducts", (String)map_data.get("PPL_DescribeProducts")),"Unable to put value in DescribeProducts field");
			 customAssert.assertTrue(k.Input("COB_PPL_DeclarationCondition", (String)map_data.get("PPL_DeclarationCondition")),"Unable to put value in DeclarationCondition field");
			 customAssert.assertTrue(k.DropDownSelection("COB_PPL_LOI_PPLiability", (String)map_data.get("PPL_LOI_PPLiability")),"Unable to put value in LOI field");
			 customAssert.assertTrue(k.DropDownSelection("COB_PPL_TypeOfLimit", (String)map_data.get("PPL_TypeOfLimit")),"Unable to put value in TypeOfLimit field");
			
			 // Add Edit Wages Breakdown :
			 
			 String sValue = (String)map_data.get("PPL_AddEditWagesBreakdown");
			 
			 if(sValue.contains("Yes")){
				 coverName = "Public & Products Liability";
				 abvr = "PPL";
				 customAssert.assertTrue(k.Click("COB_Btn_AddWagesBreakdown"),"Unable to click on AddWagesBreakdown button");
				 customAssert.assertTrue(AddWages(map_data, coverName, abvr),"");
			 }
			 
			String sVal = (String)map_data.get("PPL_AddBespoke");
			 if(sVal.length() > 0){
				 abvr = "PPL_AddB_";
				 coverName = "PPL";
				 customAssert.assertTrue(funcAddBespoke(map_data, sVal, abvr,coverName),"Unable to add bspoke item for PPL cover");
			 }
			 
			 // Click on Calculate Premium button :
			 
			 customAssert.assertTrue(k.Click("COB_Btn_ApplyBookRate"),"Unable to click on ApplyBookRate button");
			 
			 // Get table id :
			 
			 	String sUniqueCol ="Tech. Adjust (%)";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
				coverName = "Public & Products Liability";
				abvr = "PPL_";
				
				customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, sTablePath, coverName, abvr),"Unable to handle manual rated premium table on Property Owners Liability screen");	 
							
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

	public boolean AddWages(Map<Object, Object> map_data, String s_coverName, String s_Abvr ){
		boolean retValue = true;
		String abvr = null, coverName = null;
		String s_Sheet =null, Geo_Limit = null, s_Val = null, s_TC;
		String s_Total = null, e_Total = null;
		String s_Xpath = null;
		
		
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
		
		try{
				
			
		customAssert.assertTrue(k.Input("COB_BD_Percentage", (String)map_data.get("BD_Percentage")),"Unable to put value in Percentage field");
		
		if(s_coverName.contains("Public & Products Liability") || s_coverName.contains("Annual Works") ){
			
				 // Turnover Breakdown by Geographical Limit
			if(!common.currentRunningFlow.contains("Renewal")){
				
				 s_Xpath = "//table[contains(@id,'_geo_limit_bd_table')]";
				 WebElement s_Table  = driver.findElement(By.xpath(s_Xpath));
				 
				 List<WebElement> l_Rows = s_Table.findElements(By.tagName("tr"));
				 int n_Rows = l_Rows.size();
		        
				 for(int i = 0; i < n_Rows-2; i++){					
						Geo_Limit = s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[1]/input")).getAttribute("value");
						s_Val = (String)map_data.get("BD_Turnover_GLimit_"+ Geo_Limit);
						
						if(s_Val.length() > 0){
							s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[2]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[2]/input")).sendKeys(s_Val);
						}
				}
				s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(n_Rows-1)+"]/td[2]")).click();
				s_Total = s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(n_Rows-1)+"]/td[2]")).getText();
				e_Total = 	(String)map_data.get("BD_Turnover_GLimit_Total");	
				
				CommonFunction.compareValues(Double.parseDouble(s_Total), Double.parseDouble(e_Total), "Total % turnover for Turnover Breakdown by Geographical Limit for cover - " + s_coverName);
			
			 // Turnover Breakdown by Trade Code :
					
						s_Xpath = "//table[contains(@id,'_trade_code_bd_table')]";
						s_Table  = driver.findElement(By.xpath(s_Xpath));
						 
						 List<WebElement> tc_Rows = s_Table.findElements(By.tagName("tr"));
						 int i_Rows = tc_Rows.size();
				        
						 for(int i = 0; i < i_Rows-2; i++){
							s_TC = s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[1]/input")).getAttribute("value");
							s_Val = (String)map_data.get(s_TC);
							
							if(s_Val.length() > 0){
								s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[2]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
								s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[2]/input")).sendKeys(s_Val);
							}
						}
				
					s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i_Rows-1)+"]/td[2]")).click();
					String sTC_Total = s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i_Rows-1)+"]/td[2]")).getText();
					e_Total = 	(String)map_data.get("BD_Turnover_TCode_Total");	
					
					CommonFunction.compareValues(Double.parseDouble(sTC_Total), Double.parseDouble(e_Total), "Total % turnover for Turnover Breakdown by Trade code for cover - " + s_coverName);
					}					
		}
		
				
		// Contracting Turnover by Area of Work
				
		if(!common.currentRunningFlow.contains("Renewal")){
			s_Xpath = "//table[contains(@id,'_percentage_bd_table')]";
			WebElement s_Table  = driver.findElement(By.xpath(s_Xpath));
			 
			 List<WebElement> AOW_Rows = s_Table.findElements(By.tagName("tr"));
			 int aW_Rows = AOW_Rows.size();
	        
			for(int i = 0; i < aW_Rows-2; i++){
				String s_AOW = s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[1]/input")).getAttribute("value");
				switch (s_AOW) {
						case "Domestic - PDH, flats & New Builds" :
								s_Val = (String)map_data.get("BD_Turnover_AOW_Domestic");
								break;
						case "Commercial - Shops & Offices" :
								s_Val = (String)map_data.get("BD_Turnover_AOW_Commercial");
								break;
						case "Other Commerical" :
								s_Val = (String)map_data.get("BD_Turnover_AOW_OtherComm");
								break;
						case "Industrial - Manufacturing plant & production areas" :
								s_Val = (String)map_data.get("BD_Turnover_AOW_Industrial");
								break;
						case "Other – No Discount" :
								s_Val = (String)map_data.get("BD_Turnover_AOW_NoDiscount");
								break;
				}
				
				if(s_Val.length() > 0){
					s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[2]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[2]/input")).sendKeys(s_Val);
				}
			}
			
			s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(aW_Rows-1)+"]/td[2]")).click();
			String sAOW_Total = s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(aW_Rows-1)+"]/td[2]")).getText();;
			e_Total = (String)map_data.get("BD_Turnover_AOW_Total");	
			
			CommonFunction.compareValues(Double.parseDouble(sAOW_Total), Double.parseDouble(e_Total), "Total % turnover for Contracting Turnover by Area of Work for cover - " + s_coverName);
			
			
		}
			
		if(!common.currentRunningFlow.contains("Renewal")){
			if(!s_coverName.contains("Annual Works") ){
				// Add Employee Wages :
				
				String s_Wages = (String)map_data.get("BD_EmployeeWageWorkActivities");
				
				s_Xpath = "//table[contains(@id,'_employee_wages_breakdown_table')]";
				WebElement	s_Table  = driver.findElement(By.xpath(s_Xpath));
				 
				 List<WebElement> EW_Rows = s_Table.findElements(By.tagName("tr"));
				 int e_Rows = EW_Rows.size();
				 
				if(s_Wages.length() > 0){
					
					String wagesList[] = s_Wages.split(";");
					
					for(int i = 0; i < wagesList.length; i++){
						String e_WagesActivity = s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[1]/input")).getAttribute("value");
						String Val_Excel = (String)internal_data_map.get("Employee Wages").get(i).get("EW_Employee_Wage_Breakdown_Activities");
						if(e_WagesActivity.contains(Val_Excel)){
							
							// Select region
								s_Val = (String)internal_data_map.get("Employee Wages").get(i).get("EW_Workaway_location _region");
								String e_regions[] = s_Val.split(",");
								
								for(int j = 0; j < e_regions.length; j++){
									s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[2]/span/span[1]/span/ul")).click();
									
									if(s_Table.findElement(By.xpath("//li[text()='"+s_Val+"']")).isDisplayed())
										s_Table.findElement(By.xpath("//li[text()='"+s_Val+"']")).click();
									else
										continue;
									break;
								}
								 
							//Enter description :
							
								s_Val = (String)internal_data_map.get("Employee Wages").get(i).get("EW_Description_of_Activity");
								s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[3]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
								s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[3]/input")).sendKeys(s_Val);
								
							// Enter Estimated Annual WageRoll :
								s_Val = (String)internal_data_map.get("Employee Wages").get(i).get("EW_Estimated_Annual_WageRoll");
								s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[4]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
								s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[4]/input")).sendKeys(s_Val);
								
							// Enter Number of employees :
								s_Val = (String)internal_data_map.get("Employee Wages").get(i).get("EW_Number_of_employees");
								s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
								s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(s_Val);
						}
					}
					
				}
			}
	
		}
				
		
		// Employee Wage Work Activities table handling :
			
				/*String s_WagesWActivity = (String)map_data.get("BD_EmployeeWageWorkActivities");
				
				s_Xpath = "//table[contains(@id,'_empl_activity_bd_table')]";
				s_Table  = driver.findElement(By.xpath(s_Xpath));
				 
				 List<WebElement> EWwA_Rows = s_Table.findElements(By.tagName("tr"));
				 int WA_Rows = EWwA_Rows.size();
				 
				 if(s_WagesWActivity.length() > 0){
						
						String workAct[] = s_WagesWActivity.split(";");
						
						for(int i = 0; i < workAct.length; i++){
							
							String Val_Act = (String)common.NB_Structure_of_InnerPagesMaps.get("EmployeeWageWorkActivities").get(i).get("EWWA_Activity");
							
							String addRow_Xpath = "//*[contains(@id,'_empl_activity_bd_table')]//*[text()='Add Row']";
							driver.findElement(By.xpath("//*[contains(@id,'_empl_activity_bd_table')]//*[text()='Add Row']")).click();
							s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[1]/select")).sendKeys(Val_Act);
							
							s_Val = (String)common.NB_Structure_of_InnerPagesMaps.get("EmployeeWageWorkActivities").get(i).get("EWWA_Percentage");
							s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[2]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							
						}					
				 }*/
				
			customAssert.assertTrue(common.funcButtonSelection("CCF_Btn_Save"), "Unable to click on save button");
			customAssert.assertTrue(k.Click("CCF_Btn_Back"), "Unable to click on Back button");
				
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


	@SuppressWarnings("static-access")
	public boolean funcLegalExpenses(Map<Object, Object> map_data,String code,String event){
		
		boolean r_value= true;
		String abvr = null, coverName = null;
		try{
			
			customAssert.assertTrue(common.funcPageNavigation("Legal Expenses", ""),"Legal Expenses page navigations issue(S)");
			
			if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
            	
		    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
		      }
			
			if(((String)map_data.get("LE_ContractDisputes")).equals("Yes")){ 
				if(k.chkboxSelection("COB_LE_ContractDisputes")== false){
					customAssert.assertTrue(k.Click("COB_LE_ContractDisputes"), "Unable to select value from COB_LE_ContractDisputes Checkbox .");
			}
				}
			customAssert.assertTrue(k.DropDownSelection("COB_LE_lOI", (String)map_data.get("LE_lOI")), "Unable to select value from LE_lOI dropdown .");
			customAssert.assertTrue(k.Input("COB_LE_Turnover", Keys.chord(Keys.CONTROL, "a")),"Unable to select Turnover ");
			customAssert.assertTrue(k.Input("COB_LE_Turnover", (String)map_data.get("LE_Turnover")),"Unable to enter value in Turnover  . ");
			customAssert.assertTrue(k.Input("COB_LE_Average", Keys.chord(Keys.CONTROL, "a")),"Unable to select Wages ");
			customAssert.assertTrue(k.Input("COB_LE_Average", (String)map_data.get("LE_Average")),"Unable to enter value in LE_Average  . ");
			customAssert.assertTrue(k.Input("COB_LE_Largest", Keys.chord(Keys.CONTROL, "a")),"Unable to select NetPremium ");
			customAssert.assertTrue(k.Input("COB_LE_Largest", (String)map_data.get("LE_Largest")),"Unable to enter value in LE_Largest  . ");
			customAssert.assertTrue(k.DropDownSelection("COB_LE_Contract_Represent", (String)map_data.get("LE_ContractRepresentsMoreThan40")), "Unable to select value from LE_lOI dropdown .");
			if(((String)map_data.get("LE_ContractRepresentsMoreThan40")).equalsIgnoreCase("Yes")){
				customAssert.assertTrue(k.Input("COB_LE_Contract_Represent_Details", Keys.chord(Keys.CONTROL, "a")),"Unable to select COB_LE_Contract_Represent_Details ");
				customAssert.assertTrue(k.Type("COB_LE_Contract_Represent_Details", (String)map_data.get("LE_ContractRepresentsMoreThan40Details")),"Unable to enter value in COB_LE_Contract_Represent_Details  . ");
			}
			String sVal = (String)map_data.get("LE_AddBespoke");
			 if(sVal.length() > 0){
				 abvr = "LE_AddB_";
				 coverName = "LE";
				 customAssert.assertTrue(funcAddBespoke(map_data, sVal, abvr,coverName),"Unable to add bspoke item for PPL cover");
			
			 }
			 customAssert.assertTrue(k.Click("COB_Btn_ApplyBookRate"),"Unable to click on ApplyBookRates button");
			 
			 String sUniqueCol ="Description";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
				String sCover = "Legal Expenses";
				
			 customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, sTablePath, sCover, abvr),"Unable to handle Auto rated premium table on Legal Expense screen");
				 
				 
			 
//			int LE_Premium_Decimal_Places = common.countDecimalPlaces(k.getAttribute("CCF_LE_NetPremium", "value"));
//			boolean decimalFlag = LE_Premium_Decimal_Places > 2 ?false:true;
//			customAssert.assertTrue(decimalFlag , "LE Premium Should contain up to two Decimal Places . ");
//			
//			customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques1", (String)map_data.get("LE_Ques1")),"Unable to Select LE_Ques1 radio button . ");
//			
//			//Statement of Fact
//			customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques1", (String)map_data.get("LE_Ques1")),"Unable to Select LE_Ques1 radio button . ");
//			customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques2", (String)map_data.get("LE_Ques2")),"Unable to Select LE_Ques2 radio button . ");
//			customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques3", (String)map_data.get("LE_Ques3")),"Unable to Select LE_Ques3 radio button . ");
//			customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques4", (String)map_data.get("LE_Ques4")),"Unable to Select LE_Ques4 radio button . ");
//			customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques5", (String)map_data.get("LE_Ques5")),"Unable to Select LE_Ques5 radio button . ");
//			customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques6", (String)map_data.get("LE_Ques6")),"Unable to Select LE_Ques6 radio button . ");
//					
//			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Legal Expenses .");
//			
//			exp_AnnualCarrierPremium = common.roundedOff(common.getLEAnnualCarrierPremium(common.product,(String)map_data.get("LE_LimitOfLiability"), (String)map_data.get("LE_Turnover"), (String)map_data.get("LE_Wages")));
//			act_AnnualCarrierPremium = k.getAttribute("CCF_LE_AnnualCarrierPremium","value");
//			customAssert.assertEquals(exp_AnnualCarrierPremium, act_AnnualCarrierPremium,"Annual Carrier Premium (Excludes IPT) is incorrect Exppected: <b>"+exp_AnnualCarrierPremium+"</b> Actual: <b>"+act_AnnualCarrierPremium+" for Legal Expense . ");
//			customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Legal Expenses", testName, "LE_AnnualCarrierPremium", exp_AnnualCarrierPremium,common.NB_excel_data_map),"Error while writing data to excel for field >NB_ClientId<");
			
			
			TestUtil.reportStatus("Legal Expenses details are filled sucessfully . ", "Info", true);
			
		}catch(Throwable t){
			return false;
		}
		
		return r_value;
		}

	public boolean PropertyOwnersLiabilityPage(Map<Object, Object> map_data){
		boolean retValue = true;
		String abvr = null, coverName = null;
		String s_Sheet =null;
		
		
		try{
			 customAssert.assertTrue(common.funcPageNavigation("Property Owners Liability", ""),"Property Owners Liability page is having issue(S)");
			 
			 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	            	
		    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
		      }
			 
			 customAssert.assertTrue(k.DropDownSelection("COB_POL_LOI", (String)map_data.get("POL_LOI")),"Unable to put value in LOI field");
			 customAssert.assertTrue(k.DropDownSelection("COB_POL_TypeOfLimit", (String)map_data.get("POL_TypeOfLimit")),"Unable to put value in TypeOfLimit field");
			
			 String sVal = (String)map_data.get("POL_AddBespoke");
			 if(sVal.length() > 0){
				 abvr = "POL_AddB_";
				 coverName = "POL";
				 customAssert.assertTrue(funcAddBespoke(map_data, sVal, abvr,coverName),"Unable to add bspoke item for JCT cover");
			 }
			 
			 // Click on Calculate Premium button :
			 
			 customAssert.assertTrue(k.Click("COB_HCP_CalculatePremium"),"Unable to click on CalculatePremium button");
			 
			 // Get table id :
			 
			 	String sUniqueCol ="Tech. Adjust (%)";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
				coverName = "Property Owners Liability";
				abvr = "POL_";
				
				customAssert.assertTrue(funcValidate_ManualRatedTables(map_data, sTablePath, coverName, abvr),"Unable to handle manual rated premium table on Property Owners Liability screen");	 
							
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


	public boolean TerrorismPage(Map<Object, Object> map_data){
		boolean retValue = true;
		String abvr = null, coverName = null;
		String s_Sheet =null;
		
		
		try{
			 customAssert.assertTrue(common.funcPageNavigation("Terrorism", ""),"Terrorism page is having issue(S)");
			 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	            	
		    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
		      }
			
			 customAssert.assertTrue(k.Input("COB_Ter_Turnover_Zone_A", (String)map_data.get("Ter_Turnover_Zone_A")),"Unable to put value in Zone_A field");
			 customAssert.assertTrue(k.Input("COB_Ter_Turnover_Zone_B", (String)map_data.get("Ter_Turnover_Zone_B")),"Unable to put value in Zone_B field");
			 customAssert.assertTrue(k.Input("COB_Ter_Turnover_Zone_C", (String)map_data.get("Ter_Turnover_Zone_C")),"Unable to put value in Zone_C field");
			 customAssert.assertTrue(k.Input("COB_Ter_Turnover_Zone_D", (String)map_data.get("Ter_Turnover_Zone_D")),"Unable to put value in Zone_D field");
			 
			 k.Click("COB_Ter_Turnover_Total");	
			 String s_TotalTurnover = k.getAttribute("COB_Ter_Turnover_Total", "value");
			 //CommonFunction.compareValues(Double.parseDouble((String)map_data.get("Ter_Turnover_Total")), Double.parseDouble(s_TotalTurnover),"Total Turnover % for terrorism ");
					 
			 String sVal = (String)map_data.get("Ter_AddBespoke");
			 if(sVal.length() > 0){
				 abvr = "Ter_AddB_";
				 coverName = "Ter";
				 customAssert.assertTrue(funcAddBespoke(map_data, sVal, abvr,coverName),"Unable to add bspoke item for JCT cover");
			 }
			 
			 // Click on Calculate Premium button :
			 
			 customAssert.assertTrue(k.Click("COB_HCP_CalculatePremium"),"Unable to click on CalculatePremium button");
			 
			 // Get table id :
			 
			 	String sUniqueCol ="Tech. Adjust (%)";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
				coverName = "Terrorism";
				abvr = "Ter_";
				
				customAssert.assertTrue(funcValidate_ManualRatedTables(map_data, sTablePath, coverName, abvr),"Unable to handle manual rated premium table on Terrorism screen");	 
							
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


	public boolean JCTPage(Map<Object, Object> map_data){
		boolean retValue = true;
		String abvr = null;
		String s_Sheet =null;
		
		
		try{
			 customAssert.assertTrue(common.funcPageNavigation("JCT 6.5.1", ""),"JCT 6.5.1 page is having issue(S)");
			 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){

					customAssert.assertTrue(common.deleteItems(),"Delete Items function is having issues.");
				}

			 // First select value for all drop down objects :
			 
			 k.ImplicitWaitOff();
			 String q_value = null;
				 
				List<WebElement> elements = driver.findElements(By.tagName("select"));
				 Select sel = null;
				
				 for(int j = 4;j<elements.size()-1;j++){
					 if(elements.get(j).isDisplayed()){
						 
						 sel = new Select(elements.get(j));
						 try{
							 q_value = (String)map_data.get("JCT_Q"+(j-3));
							 sel.selectByVisibleText(q_value);
						 }
						 catch(Throwable t){
							 sel.selectByVisibleText("No");
						 } 
					 }		 
				 }
			 
			 customAssert.assertTrue(k.Input("COB_JCT_Archiect_PostCode", (String)map_data.get("JCT_Archiect_PostCode")),"Unable to put value in Archiect_PostCode field");
			 customAssert.assertTrue(k.Input("COB_JCT_ContractValue", (String)map_data.get("JCT_ContractValue")),"Unable to put value in ContractValue field");
			 customAssert.assertTrue(k.Input("COB_JCT_WorkToBeCarriedOut", (String)map_data.get("JCT_WorkToBeCarriedOut")),"Unable to put value in EmployeeTools_SumInsured field");
			 customAssert.assertTrue(k.Input("COB_JCT_PleaseDescribeFully", (String)map_data.get("JCT_PleaseDescribeFully")),"Unable to put value in PleaseDescribeFully field");
			 customAssert.assertTrue(k.Input("COB_JCT_DescriptionGroundConditions", (String)map_data.get("JCT_DescriptionGroundConditions")),"Unable to put value in DescriptionGroundConditions field");
			 customAssert.assertTrue(k.Input("COB_JCT_NatureOfSubsoil", (String)map_data.get("JCT_NatureOfSubsoil")),"Unable to put value in NatureOfSubsoil field");
			 customAssert.assertTrue(k.Input("COB_JCT_NatureOfOccupation", (String)map_data.get("JCT_NatureOfOccupation")),"Unable to put value in NatureOfOccupation field");
			 customAssert.assertTrue(k.Input("COB_JCT_FullDetailsOfProperty", (String)map_data.get("JCT_FullDetailsOfProperty")),"Unable to put value in FullDetailsOfProperty field");
			 customAssert.assertTrue(k.Input("COB_JCT_MethodOfDemolition", (String)map_data.get("JCT_MethodOfDemolition")),"Unable to put value in MethodOfDemolition field");
			 customAssert.assertTrue(k.Input("COB_JCT_NumberOfStoreys", (String)map_data.get("JCT_NumberOfStoreys")),"Unable to put value in NumberOfStoreys field");
			 customAssert.assertTrue(k.Input("COB_JCT_NumberOfBasements", (String)map_data.get("JCT_NumberOfBasements")),"Unable to put value in NumberOfBasements field");
			 customAssert.assertTrue(k.Input("COB_JCT_MaterialsUsed", (String)map_data.get("JCT_MaterialsUsed")),"Unable to put value in MaterialsUsed field");
			 customAssert.assertTrue(k.Input("COB_JCT_NatureOfFrameWork", (String)map_data.get("JCT_NatureOfFrameWork")),"Unable to put value in NatureOfFrameWork field");
			 customAssert.assertTrue(k.Input("COB_JCT_TypeOfCladding", (String)map_data.get("JCT_TypeOfCladding")),"Unable to put value in TypeOfCladding field");
			 customAssert.assertTrue(k.Input("COB_JCT_ApproximateAge", (String)map_data.get("JCT_ApproximateAge")),"Unable to put value in ApproximateAge field");
			 customAssert.assertTrue(k.Input("COB_JCT_GeneralCondition", (String)map_data.get("JCT_GeneralCondition")),"Unable to put value in GeneralCondition field");
			 customAssert.assertTrue(k.Input("COB_JCT_CurrentOccupation", (String)map_data.get("JCT_CurrentOccupation")),"Unable to put value in CurrentOccupation field");
			 customAssert.assertTrue(k.Input("COB_JCT_NatureOfAlteration", (String)map_data.get("JCT_NatureOfAlteration")),"Unable to put value in NatureOfAlteration field");
			 customAssert.assertTrue(k.Input("COB_JCT_LOI_Required", (String)map_data.get("JCT_LOI_Required")),"Unable to put value in LOI_Required field");
			// customAssert.assertTrue(k.DropDownSelection("COB_JCT_TypeOfLimit", (String)map_data.get("JCT_TypeOfLimit")),"Unable to put value in TypeOfLimit field");
			 
			
			 String sValue = (String)map_data.get("JCT_AddBuilding");
			 if(sValue.length() > 0){
				 abvr = "OP_AddB_";
				 customAssert.assertTrue(funcAddBuilding(map_data, sValue),"Unable to add buildings for JCT cover");
			 }
			 
			 
			 String sVal = (String)map_data.get("JCT_AddBespoke");
			 if(sVal.length() > 0){
				 abvr = "JCT_AddB_";
				 customAssert.assertTrue(funcAddBespoke(map_data, sVal, abvr, "JCT"),"Unable to add bspoke item for JCT cover");
			 }
			 
			 //---------/-------------//
			 //---Referrals code------//
			 //---------/--------------//
			 
			 String jct_activity_value=null;
			 jct_activity_value = k.GetDropDownSelectedValue("COB_JCT_StructuralDemolition");
			 if(jct_activity_value.equalsIgnoreCase("Yes"))
				 common_VELA.referrals_list.add((String)map_data.get("RM_JCT6_5_1_Structural_Demolition"));
			 
			 jct_activity_value = k.GetDropDownSelectedValue("COB_JCT_Piling");
			 if(jct_activity_value.equalsIgnoreCase("Yes"))
				 common_VELA.referrals_list.add((String)map_data.get("RM_JCT6_5_1_Piling"));
			
			 
			 jct_activity_value = k.GetDropDownSelectedValue("COB_JCT_GroundStabilisation");
			 if(jct_activity_value.equalsIgnoreCase("Yes"))
				 common_VELA.referrals_list.add((String)map_data.get("RM_JCT6_5_1_GroundStabilisation"));
			
			 
			 jct_activity_value = k.GetDropDownSelectedValue("COB_JCT_Dewatering");
			 if(jct_activity_value.equalsIgnoreCase("Yes"))
				 common_VELA.referrals_list.add((String)map_data.get("RM_JCT6_5_1_Dewatering"));
			
			 
			 jct_activity_value = k.GetDropDownSelectedValue("COB_JCT_Excavation");
			 if(jct_activity_value.equalsIgnoreCase("Yes")){
				 double excv_depth = k.getAttribute("JCT_Excavation_Depth", "value").equals("")?0.0:Double.parseDouble(k.getAttribute("JCT_Excavation_Depth", "value"));
				 if(excv_depth > 3)
					 common_VELA.referrals_list.add((String)map_data.get("RM_JCT6_5_1_Excavationdepth"));
			 }
			
			 
			 jct_activity_value = k.GetDropDownSelectedValue("COB_JCT_Underpinning");
			 if(jct_activity_value.equalsIgnoreCase("Yes")){
				 double underpining_depth = k.getAttribute("JCT_Underpinning_Depth", "value").equals("")?0.0:Double.parseDouble(k.getAttribute("JCT_Underpinning_Depth", "value"));
				 double underpining_length_bay = k.getAttribute("JCT_Underpinning_Length_any_bay", "value").equals("")?0.0:Double.parseDouble(k.getAttribute("JCT_Underpinning_Length_any_bay", "value"));
					
				 if(underpining_depth > 3 && underpining_length_bay >  1.2)
					 common_VELA.referrals_list.add((String)map_data.get("RM_JCT6_5_1_Underpinningdepth"));
			 }
		
			 
			 // Click on Calculate Premium button :
			 
			 customAssert.assertTrue(k.Click("COB_HCP_CalculatePremium"),"Unable to click on CalculatePremium button");
			 
			 // Get table id :
			 
			 	String sUniqueCol ="Tech. Adjust (%)";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
				String sCover = "JCT";
				abvr = "JCT_";
				
				customAssert.assertTrue(funcValidate_ManualRatedTables(map_data, sTablePath, sCover, abvr),"Unable to handle manual rated premium table on Own Plant screen");	 
							
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


	public boolean funcAddBuilding(Map<Object, Object> map_data, String s_Building){
		boolean retValue = true;
		String innerSheetName = null;
		
		try{
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
				String AllBuilding[] =  s_Building.split(";");
				
				for(int i = 0; i < AllBuilding.length; i++){
					
					customAssert.assertTrue(k.Click("COB_JCT_AddBuilding"),"Unable to click on JCT_AddBuilding button");
									
					customAssert.assertTrue(k.Input("COB_AB_JCT_Description", (String)internal_data_map.get("AddBuildingJCT").get(i).get("AB_JCT_Description")),"Unable to put value in Description field for add building");
					customAssert.assertTrue(k.Input("COB_AB_JCT_Address",(String)internal_data_map.get("AddBuildingJCT").get(i).get("AB_JCT_Address")),"Unable to put value in PleaseProvideFullDetails field");
					customAssert.assertTrue(k.Input("COB_AB_JCT_Address_PostCode",(String)internal_data_map.get("AddBuildingJCT").get(i).get("AB_JCT_Address_PostCode")),"Unable to put value in BookPremium field");
					customAssert.assertTrue(k.Input("COB_AB_JCT_ApproxDistanceFromSite",(String)internal_data_map.get("AddBuildingJCT").get(i).get("AB_JCT_ApproxDistanceFromSite")),"Unable to put value in Description field");
					customAssert.assertTrue(k.Input("COB_AB_JCT_ApproxAge",(String)internal_data_map.get("AddBuildingJCT").get(i).get("AB_JCT_ApproxAge")),"Unable to put value in Description field");
					
					customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"),"Unable to click on save button");
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

	public boolean HireInPlantPage(Map<Object, Object> map_data){
		boolean retValue = true;
		String abvr = null;
		
		
		try{
			 customAssert.assertTrue(common.funcPageNavigation("Hired In Plant", ""),"Hired In Plant page is having issue(S)");
			 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	            	
		    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
		      }
			 customAssert.assertTrue(k.Input("COB_HIP_TotalHiringCharges", (String)map_data.get("HIP_TotalHiringCharges")),"Unable to put value in TotalHiringCharges field");
			 customAssert.assertTrue(k.DropDownSelection("COB_HIP_OperateUnderground", (String)map_data.get("HIP_OperateUnderground")),"Unable to put value in OperateUnderground field");
			 customAssert.assertTrue(k.Input("COB_HIP_DeclarationCondition", (String)map_data.get("HIP_DeclarationCondition")),"Unable to put value in DeclarationCondition field");
			 customAssert.assertTrue(k.Input("COB_HIP_LOI", (String)map_data.get("HIP_LOI")),"Unable to put value in LOI field");
			 
			 String sAddUsage = (String)map_data.get("HIP_AddPlantUsage");
			 if(sAddUsage.contains("Yes")){
				 customAssert.assertTrue(funcAddUsage(map_data, sAddUsage,"HIP"),"Unable to add Usage details for HIP cover");
			 }
			 
			 String sVal = (String)map_data.get("HIP_AddBespokeItem");
			 if(sVal.length() > 0){
				 abvr = "HIP_AddB_";
				 customAssert.assertTrue(funcAddBespoke(map_data, sVal, abvr,"HIP"),"Unable to add bespoke item for HIP cover");
			 }
			 
			 // Click on apply book rates button :
			 
			 customAssert.assertTrue(k.Click("COB_Btn_ApplyBookRate"),"Unable to click on ApplyBookRate button");
			 
			 // Get table id :
			 
			 	String sUniqueCol ="Tech. Adjust (%)";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
				String sCover = "Hired In Plant";
				abvr = "HIP_";
				
				customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, sTablePath, sCover, abvr),"Unable to handle Auto rated premium table on HIP screen");
				
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


	public boolean OwnPlantsPage(Map<Object, Object> map_data){
		boolean retValue = true;
		String abvr = null;
		
		
		try{
			 customAssert.assertTrue(common.funcPageNavigation("Own Plant", ""),"Own Plant page is having issue(S)");
			 customAssert.assertTrue(k.DropDownSelection("COB_OP_PolicyBasis", (String)map_data.get("OP_PolicyBasis")),"Unable to put value in PolicyBasis field");
			 customAssert.assertTrue(k.Input("COB_OP_CurrentMarketValue", (String)map_data.get("OP_CurrentMarketValue")),"Unable to put value in CurrentMarketValue field");
			 customAssert.assertTrue(k.DropDownSelection("COB_OP_IsProposerOwner", (String)map_data.get("OP_IsProposerOwner")),"Unable to put value in IsProposerOwner field");
			 customAssert.assertTrue(k.DropDownSelection("COB_OP_AdjacentToWater", (String)map_data.get("OP_AdjacentToWater")),"Unable to put value in AdjacentToWater field");
			 
			 //referral code
			 String IsAdjacent_to_water = null;
			 IsAdjacent_to_water = k.GetDropDownSelectedValue("COB_OP_AdjacentToWater");
			 if(IsAdjacent_to_water.equalsIgnoreCase("Yes"))
				 common_VELA.referrals_list.add((String)map_data.get("RM_OwnPlant_adjacentToWater"));
			 
			 customAssert.assertTrue(k.Input("COB_OP_EmployeeTools_SumInsured", (String)map_data.get("OP_EmployeeTools_SumInsured")),"Unable to put value in EmployeeTools_SumInsured field");
			 customAssert.assertTrue(k.Input("COB_OP_DeclarationCondition", (String)map_data.get("OP_DeclarationCondition")),"Unable to put value in DeclarationCondition field");
			 customAssert.assertTrue(k.Input("COB_OP_LOI", (String)map_data.get("OP_LOI")),"Unable to put value in LOI field");
			
			 String sVal = (String)map_data.get("OP_AddBespokeItem");
			 if(sVal.length() > 0){
				 abvr = "OP_AddB_";
				 customAssert.assertTrue(funcAddBespoke(map_data, sVal, abvr,"OP"),"Unable to add bspoke item for own plant cover");
			 }
			 
			 // Click on apply book rates button :
			 
			 customAssert.assertTrue(k.Click("COB_Btn_ApplyBookRate"),"Unable to click on ApplyBookRates button");
			 
			 //Referral code
			 String IsOperateUnderground = null;
			 IsOperateUnderground = k.GetDropDownSelectedValue("HIP_OP_operateUndrground");
			 if(IsOperateUnderground.equalsIgnoreCase("Yes"))
				 common_VELA.referrals_list.add((String)map_data.get("RM_OwnPlant_operateUnderground"));
			 
			 // Get table id :
			 
			 	String sUniqueCol ="Tech. Adjust (%)";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
				String sCover = "Own Plant";
				abvr = "OP_";
				
				customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, sTablePath, sCover, abvr),"Unable to handle Auto rated premium table on Own Plant screen");
				 
				
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

	public boolean funcValidate_AutoRatedTables(Map<Object, Object> map_data, String s_TablePath, String s_CoverName, String s_Abvr){
		boolean retValue = true;
		String s_Section = null, sVal = null, s_ColName = null, s_InnerSheetName = null, i_abvr = null, s_SheetName = null;
		int totalCols = 0, totalRows = 0, InnerCount = 0;
		String sRater = null;
		
		double s_Wages = 0.00, s_BookRate = 0.00, s_BookP = 0.00, s_TechAdjust = 0.00, s_CommAdjust = 0.00, s_Premium = 0.00;
		double c_Wages = 0.00, c_BookRate = 0.00, c_BookP = 0.00, c_TechAdjust = 0.00, c_CommAdjust = 0.00, c_Premium = 0.00, c_TotalP = 0.00;
		double ad_Rate = 0.00;
		
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
		
		try{
				
				WebElement s_table= driver.findElement(By.xpath(s_TablePath));
				totalCols = s_table.findElements(By.tagName("th")).size(); 
				totalRows = s_table.findElements(By.tagName("tr")).size();
				
				s_InnerSheetName = "AddBespoke"+s_Abvr.replace("_", "");
				i_abvr = s_Abvr+"AddB_";
						
				s_SheetName = s_CoverName;		
				
				// Enter Data on screen :
						switch(s_CoverName){
						
							case "Public & Products Liability":
								sRater = "PL";
								for(int i = 0; i< totalRows-2; i++){
									s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[1]");
									
									if(s_Section.contains("Work Away")){
										
										s_ColName = s_Section.replace("Work Away - ", "");
										sVal = (String)map_data.get(s_ColName +" Tech Adjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
										
										sVal = (String)map_data.get(s_ColName +" Comm Adjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(sVal);
										
									}else if(s_Section.contains("Bona Fide Sub-Contractors")){
										sVal = (String)map_data.get("PPL_BonaFide_TechAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
										
										sVal = (String)map_data.get("PPL_BonaFide_CommAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(sVal);
									}else{						
										sVal = (String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
										
										sVal = (String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(sVal);
										
										InnerCount = InnerCount + 1;									
									}
								}
								break;
								
							case "Employers Liability":
								sRater = "EL";
								for(int i = 0; i< totalRows-2; i++){
									s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[1]");
									
								if(s_Section.contains("Work Away")){
										
										s_ColName = s_Section.replace("Work Away - ", "");
										sVal = (String)map_data.get(s_ColName +" Tech Adjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
										
										sVal = (String)map_data.get(s_ColName +" Comm Adjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
										
									}else{						
										sVal = (String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
										
										sVal = (String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
										
										InnerCount = InnerCount + 1;
										
									}
								}
								break;
							case "Legal Expenses":
								sRater = "LE";
								String innerSheetName = null;
								for(int i = 0; i< totalRows-2; i++){
									s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[2]");
									
									if(s_Section.contains("Legal Expenses (BASE LEVEL COVER)")){
										
										s_ColName = s_Section.replace("LE_BASE_LEVEL_COVER", "");
										sVal = (String)map_data.get(s_ColName +"TechAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys((String)map_data.get("LE_BASE_LEVEL_COVER_TechAdjust"));
										
										sVal = (String)map_data.get(s_ColName +"PremiumOverride");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys((String)map_data.get("LE_BASE_LEVEL_COVER_PremiumOverride"));
																												
										InnerCount = InnerCount + 1;									
									}else{
										String s_Bespoke = (String)map_data.get("LE_AddBespoke");
										String AllBespoke[] =  s_Bespoke.split(";");
										String sSheetName[] = AllBespoke[0].split("_");
										innerSheetName = sSheetName[0];
										int j;
										for(j=0; j<AllBespoke.length; j++){
											if(s_Section.equals((String)internal_data_map.get(innerSheetName).get(j).get("LE_AddB_Description"))){
												break;
											}
										}
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys((String)internal_data_map.get(innerSheetName).get(j).get("LE_TechAdjustment"));
										
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys((String)internal_data_map.get(innerSheetName).get(j).get("LE_PremiumOverride"));
									}
								}
								break;	
							case "Annual Works":
								sRater = "Annual Works";
								for(int i = 0; i< totalRows-2; i++){
									s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[1]");
									
									if(s_Section.contains("Existing Structures") || s_Section.contains("DE5") || s_Section.contains("Other works")){
										
										sVal = (String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
										
										sVal = (String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
										
										InnerCount = InnerCount + 1;
																					
									 }else{						
										
										sVal = (String)map_data.get(s_Section +" Tech Adjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
										
										sVal = (String)map_data.get(s_Section +" Comm Adjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
											
									}
								}
								break;
								
							case "Single Project":
								sRater = "Single Project";
								for(int i = 0; i< totalRows-2; i++){
									s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[1]");
									
									if(s_Section.contains("Existing Structures") || s_Section.contains("DE5") || s_Section.contains("Other works")){
										
										sVal = (String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
										
										sVal = (String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
										
										InnerCount = InnerCount + 1;
																					
									 }else{						
										
										sVal = (String)map_data.get(s_Section +" Tech Adjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
										
										sVal = (String)map_data.get(s_Section +" Comm Adjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
											
									}
								}
								break;
								
							case "Own Plant":
								sRater = "Own Plant";
								for(int i = 0; i< totalRows-2; i++){
									s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[1]");
									
									if(s_Section.contains("On Schedule")){
										
										sVal = (String)map_data.get("OP_TechAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
										
										sVal = (String)map_data.get("OP_CommAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);									
																					
									 }else{						
										
										 sVal = (String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
											driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
											driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
											
											sVal = (String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
											driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
											driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
											
											InnerCount = InnerCount + 1;
											
									}
								}
								break;
								
							case "Hire Cover Plus":
								sRater = "Hire Cover Plus";
								for(int i = 0; i< totalRows-2; i++){
									s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[1]");
									
									if(!s_Section.contains("Hire Cover Plus")){
										
										sVal = (String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
										
										sVal = (String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
										
										InnerCount = InnerCount + 1;										
									}
									
								}
								break;
								
							case "Hired In Plant":
								sRater = "Hired In Plant";
								for(int i = 0; i< totalRows-2; i++){
									s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[1]");
									
									if(s_Section.contains("Total Hiring Charges")){
										
										sVal = (String)map_data.get("HIP_TechAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
										
										sVal = (String)map_data.get("HIP_CommAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);									
									}else{

										sVal = (String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
										
										sVal = (String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
										
										InnerCount = InnerCount + 1;	
									}
									
								}
								break;
						}
					
				 if(!s_CoverName.contains("Hire Cover Plus")){
					 customAssert.assertTrue(k.Click("COB_Btn_ApplyBookRate"),"Unable to click on ApplyBookRate button");
				 }else{
					 customAssert.assertTrue(k.Click("COB_HCP_CalculatePremium"),"Unable to click on CalculatePremium button"); 
				 }
		 
			 
				 if(!s_CoverName.contains("Hire Cover Plus")){
					 calculate_Book_Rate(sRater);
				 }
				 
				 // Read data from Screen and compare :
				 
				 InnerCount = 0;
				 switch(s_CoverName){
					
					case "Public & Products Liability":
						sRater = "PL";
						c_TotalP = 0.00;
						for(int i = 0; i< totalRows-2; i++){
							s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[1]");
							
							if(s_Section.contains("Work Away") || s_Section.contains("Bona Fide Sub-Contractors")){
								s_Wages = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[2]"));
								s_BookRate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[4]/input"));
							}
							
							s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
							s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
							s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[7]/input"));
							s_Premium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[10]/input"));
							
							if(s_Section.contains("Work Away")){							
								s_ColName = s_Section.replace("Work Away - ", "");
								c_Wages = Double.parseDouble((String)internal_data_map.get("Employee Wages").get(i).get("EW_Estimated_Annual_WageRoll"));
								c_TechAdjust = Double.parseDouble((String)map_data.get(s_ColName +" Tech Adjust"));
								c_CommAdjust = Double.parseDouble((String)map_data.get(s_ColName +" Comm Adjust"));
							}else if(s_Section.contains("Bona Fide Sub-Contractors")){
								c_Wages = Double.parseDouble((String)map_data.get("PPL_BFSC_Payments"));
								c_TechAdjust = Double.parseDouble((String)map_data.get("PPL_BonaFide_TechAdjust"));
								c_CommAdjust = Double.parseDouble((String)map_data.get("PPL_BonaFide_CommAdjust"));
							}
							
							if(s_Section.contains("Work Away") || s_Section.contains("Bona Fide Sub-Contractors")){
								c_BookRate = common_COB.Book_rate_Rater_output.get(sRater+"_"+s_Section);
								c_BookP =  c_Wages* c_BookRate/100;
								ad_Rate = c_BookRate + ((c_BookRate*c_TechAdjust)/100);
								ad_Rate = ad_Rate + ((ad_Rate*c_CommAdjust)/100);
								c_Premium = ad_Rate*c_Wages/100;	
								customAssert.assertTrue(common_VELA.func_Referral_Rating_minimum_Premium(sRater, s_Section, ad_Rate),"Unable to handle func_Referral_Rating_minimum_Premium function for PL");

							}else{
								c_BookP = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"BookPremium"));
								c_TechAdjust = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
								c_CommAdjust = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
								InnerCount = InnerCount +1;		
								
								ad_Rate = c_BookP + ((c_BookP*c_TechAdjust)/100);
								c_Premium = ad_Rate + ((ad_Rate*c_CommAdjust)/100);
							}	
							
							//compare values : 
							
							CommonFunction.compareValues(c_Wages, s_Wages,"Wages for section "+s_Section +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_BookRate, s_BookRate,"BookRate for section "+s_Section +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_BookP, s_BookP,"BookP for section "+s_Section +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"TechAdjust for section "+s_Section +" of cover - "+s_CoverName);
							
							CommonFunction.compareValues(c_CommAdjust, s_CommAdjust,"CommAdjust for section "+s_Section +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_Premium, s_Premium,"Premium for section "+s_Section +" of cover - "+s_CoverName);
							c_TotalP  = c_TotalP + c_Premium;
							
							
						}
						TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_Public&ProductsLiability_NetNetPremium", String.valueOf(c_TotalP), map_data);
						break;
						
					case "Employers Liability":
						sRater = "EL";
						c_TotalP = 0.00;
						for(int i = 0; i< totalRows-2; i++){
							s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[1]");
							
							if(s_Section.contains("Work Away")){
								s_Wages = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[2]"));
								s_BookRate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[3]/input"));
							}
							
							s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[4]/input"));
							s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
							s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
							s_Premium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[9]/input"));
							
							if(s_Section.contains("Work Away")){							
								s_ColName = s_Section.replace("Work Away - ", "");
								c_Wages = Double.parseDouble((String)internal_data_map.get("Employee Wages").get(i).get("EW_Estimated_Annual_WageRoll"));
								c_TechAdjust = Double.parseDouble((String)map_data.get(s_ColName +" Tech Adjust"));
								c_CommAdjust = Double.parseDouble((String)map_data.get(s_ColName +" Comm Adjust"));
								
								c_BookRate = common_COB.Book_rate_Rater_output.get(sRater+"_"+s_Section);
								c_BookP =  c_Wages* c_BookRate/100;
								ad_Rate = c_BookRate + ((c_BookRate*c_TechAdjust)/100);
								ad_Rate = ad_Rate + ((ad_Rate*c_CommAdjust)/100);
								c_Premium = ad_Rate*c_Wages/100;
								customAssert.assertTrue(common_VELA.func_Referral_Rating_minimum_Premium(sRater, s_Section, ad_Rate),"Unable to handle func_Referral_Rating_minimum_Premium function for EL");
								
							}else{
								c_BookP = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"BookPremium"));
								c_TechAdjust = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
								c_CommAdjust = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
								InnerCount = InnerCount +1;		
								
								ad_Rate = c_BookP + ((c_BookP*c_TechAdjust)/100);
								c_Premium = ad_Rate + ((ad_Rate*c_CommAdjust)/100);
							}	
							
							//compare values : 
							
							CommonFunction.compareValues(c_Wages, s_Wages,"Wages for section "+s_Section +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_BookRate, s_BookRate,"BookRate for section "+s_Section +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_BookP, s_BookP,"BookP for section "+s_Section +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"TechAdjust for section "+s_Section +" of cover - "+s_CoverName);
							
							CommonFunction.compareValues(c_CommAdjust, s_CommAdjust,"CommAdjust for section "+s_Section +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_Premium, s_Premium,"Premium for section "+s_Section +" of cover - "+s_CoverName);
							c_TotalP  = c_TotalP + c_Premium;
							
						}
						TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_EmployersLiability_NetNetPremium", String.valueOf(c_TotalP), map_data);
						break;
						
					case "Annual Works":
						sRater = "Annual Works";
						c_TotalP = 0.00;
						for(int i = 0; i< totalRows-2; i++){
							s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[1]");
							
							s_BookRate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[3]/input"));
													
							s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[4]/input"));
							s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
							s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
							s_Premium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[9]/input"));
							
							if(s_Section.contains("Existing Structures") || s_Section.contains("DE5") || s_Section.contains("Other works")){
								
								c_BookP = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"BookPremium"));
								c_TechAdjust = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
								c_CommAdjust = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
								InnerCount = InnerCount +1;		
								
								ad_Rate = c_BookP + ((c_BookP*c_TechAdjust)/100);
								c_Premium = ad_Rate + ((ad_Rate*c_CommAdjust)/100);
									
							}else{
								
								c_TechAdjust = Double.parseDouble((String)map_data.get(s_Section +" Tech Adjust"));
								c_CommAdjust = Double.parseDouble((String)map_data.get(s_Section +" Comm Adjust"));
								
								c_BookRate = common_COB.Book_rate_Rater_output.get(sRater+"_"+s_Section);
								
								double turnover = Double.parseDouble((String)map_data.get("AW_ContractingTurnover"));
								c_BookP =  turnover* c_BookRate/100;
								
								ad_Rate = c_BookP + ((c_BookP*c_TechAdjust)/100);
								c_Premium = ad_Rate + ((ad_Rate*c_CommAdjust)/100);
								
								customAssert.assertTrue(common_VELA.func_Referral_Rating_minimum_Premium(sRater, s_Section, ad_Rate),"Unable to handle func_Referral_Rating_minimum_Premium function for Annual Works");
							}	
							
							//compare values : 
							if(s_Section.contains("Existing Structures") || s_Section.contains("DE5") || s_Section.contains("Other works")){
								// No comparison
							}else{
								CommonFunction.compareValues(c_BookRate, s_BookRate,"BookRate for section "+s_Section +" of cover - "+s_CoverName);
							}						
							
							CommonFunction.compareValues(c_BookP, s_BookP,"BookP for section "+s_Section +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"TechAdjust for section "+s_Section +" of cover - "+s_CoverName);
							
							CommonFunction.compareValues(c_CommAdjust, s_CommAdjust,"CommAdjust for section "+s_Section +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_Premium, s_Premium,"Premium for section "+s_Section +" of cover - "+s_CoverName);
							c_TotalP  = c_TotalP + c_Premium;
													
						}
						TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_AnnualWorks_NetNetPremium", String.valueOf(c_TotalP), map_data);
						break;
						
					case "Legal Expenses":
						sRater = "LE";
						String innerSheetName = null;
						String premiumOverride = null;
						String BookPremium, techAdjs,revisedPremium, premium = null;
						double calculatedpremium= 0.00, calTotalPremium=0.00;
						for(int i = 0; i< totalRows-2; i++){
							s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[2]");
							
							premiumOverride = driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).getAttribute("value");
							BookPremium = driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).getAttribute("value");
							premium = driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).getAttribute("value");
							revisedPremium = driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).getAttribute("value");
							techAdjs =  driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).getAttribute("value");		
							
							
							if(s_Section.contains("Legal Expenses (BASE LEVEL COVER)")){
								String turnover = driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[3]")).getText();
								String LOI = driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[4]")).getText();
								CommonFunction_VELA.compareValues(Double.parseDouble((String)map_data.get("LE_Turnover")), Double.parseDouble(turnover), "TurnOver for Legal Expse Base Cover");
								CommonFunction_VELA.compareValues(Double.parseDouble(((String)map_data.get("LE_lOI")).replace(",", "")), Double.parseDouble(LOI.replace(",", "")), "Limi of Indemnity for Legal Expse Base Cover");	
								if(Double.parseDouble(premiumOverride)==0){
									double calclatedbookPremium = get_LE_Book_Premium(LOI,turnover);
									CommonFunction_VELA.compareValues(calclatedbookPremium, Double.parseDouble(BookPremium), "Book Premium for Legal Expse Base Cover");
									calculatedpremium = calclatedbookPremium +(calclatedbookPremium* Double.parseDouble((String)map_data.get("LE_BASE_LEVEL_COVER_TechAdjust"))/100.0);
									CommonFunction_VELA.compareValues(calculatedpremium, Double.parseDouble(premium), "Premium for Legal Expse Base Cover");
								}else{
									CommonFunction_VELA.compareValues(Double.parseDouble((String)map_data.get("LE_BASE_LEVEL_COVER_PremiumOverride")), Double.parseDouble(premium), "Premium for Legal Expenses Base Cover");
								}
								
								calTotalPremium = calTotalPremium + Double.parseDouble(premium);
							}else{
								String s_Bespoke = (String)map_data.get("LE_AddBespoke");
								String AllBespoke[] =  s_Bespoke.split(";");
								String sSheetName[] = AllBespoke[0].split("_");
								innerSheetName = sSheetName[0];
								int j;
								for(j=0; j<AllBespoke.length; j++){
									if(s_Section.equals((String)internal_data_map.get(innerSheetName).get(j).get("LE_AddB_Description"))){
										break;
									}
								}
								CommonFunction_VELA.compareValues(Double.parseDouble(BookPremium), Double.parseDouble((String)internal_data_map.get(innerSheetName).get(j).get("LE_AddB_BookPremium")), "Book Premium for Legal Expenses All Bespoke"+AllBespoke[j]);
								
								calculatedpremium = Double.parseDouble(BookPremium)+(Double.parseDouble(BookPremium)*(Double.parseDouble(techAdjs)/100));
								CommonFunction_VELA.compareValues(calculatedpremium, Double.parseDouble(revisedPremium), "Premium for Legal Expenses Base Cover");
							
								if(Double.parseDouble(premiumOverride)==0){
								
									CommonFunction_VELA.compareValues(calculatedpremium, Double.parseDouble(premium), "Premium for Legal Expenses Base Cover");
									calTotalPremium = calTotalPremium + Double.parseDouble(premium);
								}
								else{
									CommonFunction_VELA.compareValues(Double.parseDouble(premiumOverride), Double.parseDouble(premium), "Premium for Legal Expenses Base Cover");
									calTotalPremium = calTotalPremium + Double.parseDouble(premium);
								}
								}
						}
						String totalPremium = driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+( totalRows-1)+"]/td[9]/input")).getAttribute("value");
						CommonFunction_VELA.compareValues(Double.parseDouble(totalPremium), calTotalPremium, "Total Premium for Legal Expenses ");
						TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_LegalExpenses_NetNetPremium", totalPremium, map_data);
						 
						break;
							
					case "Single Project":
						sRater = "Single Project";
						c_TotalP = 0.00;
						DecimalFormat df = new DecimalFormat("00.0000");
						for(int i = 0; i< totalRows-2; i++){
							s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[1]");
							
							s_BookRate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[3]/input"));
												
							s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[4]/input"));
							s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
							s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
							s_Premium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[9]/input"));
							
							if(s_Section.contains("Existing Structures") || s_Section.contains("DE5") || s_Section.contains("Other works")){
								
								c_BookP = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"BookPremium"));
								c_TechAdjust = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
								c_CommAdjust = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
								InnerCount = InnerCount +1;		
								
								ad_Rate = c_BookP + ((c_BookP*c_TechAdjust)/100);
								c_Premium = ad_Rate + ((ad_Rate*c_CommAdjust)/100);
									
							}else{
								
								c_TechAdjust = Double.parseDouble((String)map_data.get(s_Section +" Tech Adjust"));
								c_CommAdjust = Double.parseDouble((String)map_data.get(s_Section +" Comm Adjust"));
								
								c_BookRate = common_COB.Book_rate_Rater_output.get(sRater+"_"+s_Section);
								
								double turnover = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[2]").replaceAll(",", ""));
								c_BookP =  turnover* c_BookRate/100;
								ad_Rate = c_BookRate + ((c_BookRate*c_TechAdjust)/100);
								ad_Rate =  ad_Rate + ((ad_Rate*c_CommAdjust)/100);
								ad_Rate = Double.parseDouble(df.format(ad_Rate));
								c_Premium = (turnover * ad_Rate)/100.0;
								
								
								/*c_BookP =  turnover* c_BookRate/100;
								ad_Rate = c_BookP + ((c_BookP*c_TechAdjust)/100);
								c_Premium = ad_Rate + ((ad_Rate*c_CommAdjust)/100);*/
								
								customAssert.assertTrue(common_VELA.func_Referral_Rating_minimum_Premium(sRater, s_Section, ad_Rate),"Unable to handle func_Referral_Rating_minimum_Premium function for Single Project");
							}	
							
							//compare values : 
								
							if(s_Section.contains("Existing Structures") || s_Section.contains("DE5") || s_Section.contains("Other works")){
								//no comparison
							}else{
								CommonFunction.compareValues(c_BookRate, s_BookRate,"BookRate for section "+s_Section +" of cover - "+s_CoverName);
							}
							
							CommonFunction.compareValues(c_BookP, s_BookP,"BookP for section "+s_Section +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"TechAdjust for section "+s_Section +" of cover - "+s_CoverName);
							
							CommonFunction.compareValues(c_CommAdjust, s_CommAdjust,"CommAdjust for section "+s_Section +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_Premium, s_Premium,"Premium for section "+s_Section +" of cover - "+s_CoverName);
							c_TotalP  = c_TotalP + c_Premium;
													
						}
						TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_SingleProject_NetNetPremium", String.valueOf(c_TotalP), map_data);
						break;
						
					case "Own Plant":
						sRater = "Own Plant";
						c_TotalP = 0.00;
						for(int i = 0; i< totalRows-2; i++){
							s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[1]");
							
							s_BookRate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[3]/input"));
							
							s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[4]/input"));
							s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
							s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
							s_Premium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[9]/input"));
							
							if(s_Section.contains("On Schedule")){
								
								c_TechAdjust = Double.parseDouble((String)map_data.get("OP_TechAdjust"));
								c_CommAdjust = Double.parseDouble((String)map_data.get("OP_CommAdjust"));
								
								c_BookRate = common_COB.Book_rate_Rater_output.get(sRater+"_"+s_Section);
								
								double sumIns = Double.parseDouble((String)map_data.get("OP_CurrentMarketValue"));
								
								c_BookP =  sumIns* c_BookRate/100;
								
								ad_Rate = c_BookRate + ((c_BookRate*c_TechAdjust)/100);
								ad_Rate = ad_Rate + ((ad_Rate*c_CommAdjust)/100);
								
								c_Premium = ad_Rate*sumIns/100;
								
								customAssert.assertTrue(common_VELA.func_Referral_Rating_minimum_Premium(sRater, s_Section, ad_Rate),"Unable to handle func_Referral_Rating_minimum_Premium function for Own Plant");
																
							}else{
								
								c_BookP = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"BookPremium"));
								c_TechAdjust = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
								c_CommAdjust = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
								InnerCount = InnerCount +1;		
								
								ad_Rate = c_BookP + ((c_BookP*c_TechAdjust)/100);
								c_Premium = ad_Rate + ((ad_Rate*c_CommAdjust)/100);
								
							}	
							
							//compare values : 
							
							if(s_Section.contains("On Schedule")){
								CommonFunction.compareValues(c_BookRate, s_BookRate,"BookRate for section "+s_Section +" of cover - "+s_CoverName);
							}											
							
							CommonFunction.compareValues(c_BookP, s_BookP,"BookP for section "+s_Section +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"TechAdjust for section "+s_Section +" of cover - "+s_CoverName);
							
							CommonFunction.compareValues(c_CommAdjust, s_CommAdjust,"CommAdjust for section "+s_Section +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_Premium, s_Premium,"Premium for section "+s_Section +" of cover - "+s_CoverName);
							c_TotalP  = c_TotalP + c_Premium;
							
						}
						TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_OwnPlant_NetNetPremium", String.valueOf(c_TotalP), map_data);
						break;
						
					case "Hired In Plant":
						sRater = "Hired In Plant";
						c_TotalP = 0.00;
						for(int i = 0; i< totalRows-2; i++){
							s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[1]");
							
							s_BookRate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[3]/input"));
							
							s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[4]/input"));
							s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
							s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
							s_Premium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[9]/input"));
							
							if(s_Section.contains("Total Hiring Charges")){
								
								c_TechAdjust = Double.parseDouble((String)map_data.get("HIP_TechAdjust"));
								c_CommAdjust = Double.parseDouble((String)map_data.get("HIP_CommAdjust"));
								
								c_BookRate = common_COB.Book_rate_Rater_output.get(sRater+"_"+s_Section);
								
								double HiringCharges = Double.parseDouble((String)map_data.get("HIP_TotalHiringCharges"));
								
								c_BookP =  HiringCharges* c_BookRate/100;
								
								ad_Rate = c_BookRate + ((c_BookRate*c_TechAdjust)/100);
								ad_Rate = ad_Rate + ((ad_Rate*c_CommAdjust)/100);
								
								c_Premium = ad_Rate*HiringCharges/100;
								
								customAssert.assertTrue(common_VELA.func_Referral_Rating_minimum_Premium(sRater, s_Section, ad_Rate),"Unable to handle func_Referral_Rating_minimum_Premium function for HIP");
																
							}else{
								
								c_BookP = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"BookPremium"));
								c_TechAdjust = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
								c_CommAdjust = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
								InnerCount = InnerCount +1;		
								
								ad_Rate = c_BookP + ((c_BookP*c_TechAdjust)/100);
								c_Premium = ad_Rate + ((ad_Rate*c_CommAdjust)/100);
								
							}	
							
							//compare values : 
							
							if(s_Section.contains("Total Hiring Charges")){
								CommonFunction.compareValues(c_BookRate, s_BookRate,"BookRate for section "+s_Section +" of cover - "+s_CoverName);
							}											
							
							CommonFunction.compareValues(c_BookP, s_BookP,"BookP for section "+s_Section +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"TechAdjust for section "+s_Section +" of cover - "+s_CoverName);
							
							CommonFunction.compareValues(c_CommAdjust, s_CommAdjust,"CommAdjust for section "+s_Section +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_Premium, s_Premium,"Premium for section "+s_Section +" of cover - "+s_CoverName);
							c_TotalP  = c_TotalP + c_Premium;
							
						}
						TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_HiredInPlant_NetNetPremium", String.valueOf(c_TotalP), map_data);
						break;
						
					case "Hire Cover Plus":
						sRater = "Hire Cover Plus";
						c_TotalP = 0.00;
						for(int i = 0; i< totalRows-2; i++){
							s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[1]");
							
							if(!s_Section.contains("Hire Cover Plus")){
								
								//s_BookRate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[3]/input"));
								
								s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[4]/input"));
								s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
								s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
								s_Premium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[8]/input"));
								
								c_BookP = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"BookPremium"));
								c_TechAdjust = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
								c_CommAdjust = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
								InnerCount = InnerCount +1;		
								
								ad_Rate = c_BookP + ((c_BookP*c_TechAdjust)/100);
								c_Premium = ad_Rate + ((ad_Rate*c_CommAdjust)/100);
								
							
								CommonFunction.compareValues(c_BookP, s_BookP,"BookP for section "+s_Section +" of cover - "+s_CoverName);
								CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"TechAdjust for section "+s_Section +" of cover - "+s_CoverName);
								
								CommonFunction.compareValues(c_CommAdjust, s_CommAdjust,"CommAdjust for section "+s_Section +" of cover - "+s_CoverName);
								CommonFunction.compareValues(c_Premium, s_Premium,"Premium for section "+s_Section +" of cover - "+s_CoverName);
								c_TotalP  = c_TotalP + c_Premium;
							}
							
						}
						TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_HireCoverPlus_NetNetPremium", String.valueOf(c_TotalP), map_data);
						break;
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


	public boolean funcValidate_ManualRatedTables(Map<Object, Object> map_data, String s_TablePath, String s_CoverName, String abvr){
		boolean retValue = true;
		String s_Section = null, i_abvr = null;
		int totalCols = 0, totalRows = 0;
		String s_SheetName =null, s_InnerSheetName = null, s_ColName = null;
		double s_BookP = 0.00, s_TechAdjust = 0.00, s_commAdjust = 0.00, s_Final = 0.00, s_TotalP = 0.00;
		double c_BookP = 0.00, c_TechAdjust = 0.00, c_commAdjust = 0.00, c_Final = 0.00;
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
		try{
				
				WebElement table= driver.findElement(By.xpath(s_TablePath));
				totalCols = table.findElements(By.tagName("th")).size(); 
				totalRows = table.findElements(By.tagName("tr")).size();
				
				s_InnerSheetName = abvr.replace("_", "")+"AddBespoke";
				i_abvr = abvr+"AddB_";
						
				if(s_CoverName.contains("JCT")){
					s_SheetName = "JCT 6.5.1";
					s_ColName = "JCT6.5.1";
				}else{
					s_SheetName = s_CoverName;
					s_ColName = s_CoverName.replaceAll(" ", "");
				}						
				
				for(int i = 0; i< totalRows-2; i++){
					s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]");
					
					if(i == 0){
						customAssert.assertTrue(k.DynamicXpathWebDriver(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[2]/input", map_data, s_SheetName, i, abvr, "BookPremium", s_Section, "Input"),"Unable to enter Book Premium value for - "+s_Section);
						customAssert.assertTrue(k.DynamicXpathWebDriver(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[3]/input", map_data, s_SheetName, i, abvr, "TechAdjust", s_Section, "Input"),"Unable to enter TechAdjust value for - "+s_Section);
						customAssert.assertTrue(k.DynamicXpathWebDriver(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[4]/input", map_data, s_SheetName, i, abvr, "CommAdjust", s_Section, "Input"),"Unable to enter CommAdjust value for - "+s_Section);
					}else{
										
						customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[3]/input", internal_data_map, s_InnerSheetName, i-1, i_abvr, "TechAdjust", "TechAdjust", "Input"),"Unable to enter TechAdjust in table");
						customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[4]/input", internal_data_map, s_InnerSheetName, i-1, i_abvr, "CommAdjust", "CommAdjust", "Input"),"Unable to enter CommAdjust in table");
					}
				}
				
				// Click on Calculate Premium button :
				 
				 customAssert.assertTrue(k.Click("COB_HCP_CalculatePremium"),"Unable to click on CalculatePremium button");
				 
				// Read Premium values from screen, Compare with Calculated Value :
				 
				 for(int i = 0; i< totalRows-2; i++){
					 
					 //Read Values :
					 
						s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]");
						
						s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[2]/input"));
						s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[3]/input"));
						s_commAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[4]/input")); 
						s_Final = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[5]/input").replaceAll(",", ""));
						
					//Calculation :
						if(i == 0){
							c_BookP = Double.parseDouble((String)map_data.get(abvr+"BookPremium"));
							c_TechAdjust =  Double.parseDouble((String)map_data.get(abvr+"TechAdjust"));
							c_commAdjust = Double.parseDouble((String)map_data.get(abvr+"CommAdjust"));
						}else{
							c_BookP = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(i-1).get(i_abvr+"BookPremium"));
							c_TechAdjust = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(i-1).get(i_abvr+"TechAdjust"));
							c_commAdjust = Double.parseDouble((String)internal_data_map.get(s_InnerSheetName).get(i-1).get(i_abvr+"CommAdjust"));
						}
						
						double t_Premium = (c_BookP + ((c_BookP * c_TechAdjust)/100));
						c_Final = ((t_Premium* c_commAdjust)/100) + t_Premium;
						
						CommonFunction.compareValues(c_BookP, s_BookP,"Book Premium for section "+s_Section);
						CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"Tech Adjust for section "+s_Section);
						CommonFunction.compareValues(c_commAdjust, s_commAdjust,"Comm Adjust for section "+s_Section);
						CommonFunction.compareValues(c_Final, s_Final,"Final Prmium for section "+s_Section);
						
						s_TotalP = s_TotalP + s_Final;
					}	
				 
				 TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_"+s_ColName+"_NetNetPremium", String.valueOf(s_TotalP), map_data);
			 
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

	public boolean funcAddUsage(Map<Object, Object> map_data, String s_Usage, String s_CoverName){
		boolean retValue = true;
		String s_UsageAct = null, sVal = null;
		
		try{
					
				// Add Plant usage
					
					 customAssert.assertTrue(k.Click("COB_HIP_AddPlantUsage"),"Unable to click on AddPlantUsage button");
					 
					 String s_Xpath = "//table[contains(@id,'_plant_usage_bd_table')]";
					 WebElement s_Table  = driver.findElement(By.xpath(s_Xpath));
					 
					 List<WebElement> l_Rows = s_Table.findElements(By.tagName("tr"));
					 int n_Rows = l_Rows.size();
			        
					for(int i = 2; i < n_Rows-2; i++){					
						s_UsageAct = s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[1]/input")).getAttribute("value");
						s_UsageAct = s_UsageAct.replace(")", "_");
						String s_Act[] =  s_UsageAct.split("_");
						s_UsageAct = s_Act[1];
						sVal = (String)map_data.get("PUD"+ s_UsageAct);
						
						if(sVal.length() > 0){
							s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[2]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[2]/input")).sendKeys(sVal);
						}
					}
					s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(n_Rows-1)+"]/td[2]")).click();
					String s_Total = s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(n_Rows-1)+"]/td[2]")).getText();
					String e_Total = 	(String)map_data.get("BD_Turnover_GLimit_Total");	
					
					//common.compareValues(Double.parseDouble(s_Total), Double.parseDouble(e_Total), "Total Plant usage is not matching");
					
					customAssert.assertTrue(common.funcButtonSelection("Save"),"Unable to click on save button");
					customAssert.assertTrue(k.Click("CCF_Btn_Back"), "Unable to click on Back Button on Main Premises screen");
					 		
			 
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


	public boolean funcAddContract(Map<Object, Object> map_data, String s_Contract, String s_Abvr, String s_CoverName){
		boolean retValue = true;
		String innerSheetName = null;
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
		try{
					
				String AllContract[] =  s_Contract.split(";");
				innerSheetName = AllContract[0].replace("_01", "");
				
				for(int i = 0; i < AllContract.length; i++){
					
					customAssert.assertTrue(k.Click("COB_AW_AddContract"),"Unable to click on AddContract button");
					
					customAssert.assertTrue(k.Input("COB_AW_AD_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
					customAssert.assertTrue(k.Input("COB_AW_AD_AddressLine_1",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"AddressLine_1")),"Unable to put value in AddressLine_1 field");
					customAssert.assertTrue(k.Input("COB_AW_AD_Postcode",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Postcode")),"Unable to put value in Postcode field");
					customAssert.assertTrue(k.Input("COB_AW_AD_Duration",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Duration")),"Unable to put value in Duration field");
					customAssert.assertTrue(k.Input("COB_AW_AD_Value",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Value")),"Unable to put value in Value field");
					
					
					//Referral code
					if(k.GetDropDownSelectedValue("COB_AddB_Additionalcover").equalsIgnoreCase("Subrogation Waiver")){
						String subrogationWaiver_cent = k.getAttribute("OP_SubrogationWaiver_turnOver_cent", "value");
						if(!subrogationWaiver_cent.equals("") && Integer.parseInt(subrogationWaiver_cent) > 15){
							common_VELA.referrals_list.add((String)map_data.get("RM_SubrogationWaiver_HireUsingSubrogation"));
							}
					}
			
					
					customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"),"Unable to click on save button");
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


	public boolean funcAddBespoke(Map<Object, Object> map_data, String s_Bespoke, String s_Abvr, String s_CoverName){
		boolean retValue = true;
		String innerSheetName = null;
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
		try{
					
			String AllBespoke[] =  s_Bespoke.split(";");
			String s_Sheet[] =  AllBespoke[0].split("_");
			innerSheetName = s_Sheet[0];
			
			for(int i = 0; i < AllBespoke.length; i++){
				
				if(s_CoverName.contains("POL")){
					customAssert.assertTrue(k.Click("COB_POB_Btn_AddBespokeItem"),"Unable to click on AddBespokeItem button");
				}else{
					customAssert.assertTrue(k.Click("COB_Btn_AddBespokeItem"),"Unable to click on AddBespokeItem button");
				}
				
				switch(s_CoverName){
					case "JCT" :						
						if(k.isDisplayed("COB_JCT_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_JCT_AddB_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
						
					case "Ter" :
						if(k.isDisplayed("COB_HCP_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_HCP_AddB_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
						
					case "HCP" :
						if(k.isDisplayed("COB_HCP_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_HCP_AddB_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
						
					case "POL" :
						if(k.isDisplayed("COB_POL_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_POL_AddB_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
					case "LE" : 
						if(k.isDisplayed("COB_LE_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_LE_AddB_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_LE_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_LE_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
						
					case "SP" :
					case "AW" :
						//customAssert.assertTrue(k.DropDownSelection("COB_AddB_Additionalcover", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")),"Unable to put value in Additionalcover field");
						if(s_CoverName.equalsIgnoreCase("SP"))
							customAssert.assertTrue(k.DropDownSelection("COB_AddB_Additionalcover", (String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")),"Unable to put value in Additionalcover field");
						else{
							WebElement el = driver.findElement(By.xpath("//*[@class='subpage']//*[text()='Additional Cover']//following::select[1]"));
							Select sl = new Select(el);
							sl.selectByValue((String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover"));
						}
						
						//if(k.isDisplayed("COB_AddB_SP_TypeOfLimit")){
							//customAssert.assertTrue(k.DropDownSelection("COB_AddB_SP_TypeOfLimit", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"TypeOfLimit")),"Unable to put value in TypeOfLimit field");
						//}
						
						if(k.isDisplayed("COB_AddB_SP_LOI")){
							customAssert.assertTrue(k.Input("COB_AddB_SP_LOI",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"LOI")),"Unable to put value in LOI field");
						}		
						
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						
						String AddCov = (String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover");
						if(AddCov.contains("Other")){
							customAssert.assertTrue(k.Input("COB_AddB_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");							
						}
						
						break;
						
					case "OP" :
						customAssert.assertTrue(k.DropDownSelection("COB_AddB_Additionalcover", (String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")),"Unable to put value in Additionalcover field");
						
						//if(k.isDisplayed("COB_AddB_SP_TypeOfLimit")){
							//customAssert.assertTrue(k.DropDownSelection("COB_AddB_SP_TypeOfLimit", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"TypeOfLimit")),"Unable to put value in TypeOfLimit field");
						//}
						
						if(k.isDisplayed("COB_AddB_OP_BespokeLOI")){
							customAssert.assertTrue(k.Input("COB_AddB_OP_BespokeLOI",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"LOI")),"Unable to put value in LOI field");
						}		
						
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						
						AddCov = (String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover");
						if(AddCov.contains("Other")){
							customAssert.assertTrue(k.Input("COB_AddB_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");							
						}
						
						break;
						
					case "HIP" :
						
						customAssert.assertTrue(k.DropDownSelection("COB_AddB_Additionalcover", (String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")),"Unable to put value in Additionalcover field");
						
						//if(k.isDisplayed("COB_AddB_HIP_TypeOfLimit")){
							//customAssert.assertTrue(k.DropDownSelection("COB_AddB_HIP_TypeOfLimit", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"TypeOfLimit")),"Unable to put value in TypeOfLimit field");
						//}
						
						if(k.isDisplayed("COB_AddB_LOI")){
							customAssert.assertTrue(k.Input("COB_AddB_LOI",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"LOI")),"Unable to put value in LOI field");
						}
						
						if(k.isDisplayed("COB_AddB_HIP_Description")){
							customAssert.assertTrue(k.Input("COB_AddB_HIP_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
						
					default :	
						customAssert.assertTrue(k.DropDownSelection("COB_AddB_Additionalcover", (String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")),"Unable to put value in Additionalcover field");
						
						if(k.isDisplayed("COB_AddB_TypeOfLimit")){
							customAssert.assertTrue(k.DropDownSelection("COB_AddB_TypeOfLimit", (String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"TypeOfLimit")),"Unable to put value in TypeOfLimit field");
						}
						
						if(k.isDisplayed("COB_AddB_LOI")){
							customAssert.assertTrue(k.Input("COB_AddB_LOI",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"LOI")),"Unable to put value in LOI field");
						}
						
						if(k.isDisplayed("COB_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_AddB_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						
						if(s_CoverName.equalsIgnoreCase("OP")){
							//Referral code
								if(((String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")).equalsIgnoreCase("Subrogation Waiver"));
							//if(k.GetDropDownSelectedValue("COB_AddB_Additionalcover").equalsIgnoreCase("Subrogation Waiver")){
								String subrogationWaiver_cent = k.getAttribute("OP_SubrogationWaiver_turnOver_cent", "value");
								if(!subrogationWaiver_cent.equals("") && Integer.parseInt(subrogationWaiver_cent) > 15){
									common_VELA.referrals_list.add((String)map_data.get("RM_SubrogationWaiver_HireUsingSubrogation"));
									}
							//}
						}
				}
				
				customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"),"Unable to click on save button");
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



	public boolean GeneralRiskDetailsPage(Map<Object, Object> map_data){
		boolean retValue = true;
		
		try{
			 customAssert.assertTrue(common.funcPageNavigation("General Risk Details", ""),"General Risk Details page is having issue(S)");
			 if(k.isDisplayedField("COB_GRD_Address")){
			 customAssert.assertTrue(k.Input("COB_GRD_Address", (String)map_data.get("GRD_Address")),"Unable to put value in GRD_Address field");
			 customAssert.assertTrue(k.Input("COB_GRD_PostCode", (String)map_data.get("GRD_PostCode")),"Unable to put value in GRD_PostCode field");
			 }
			 String sVal = (String)map_data.get("GRD_AddMainP");
			 
			 
			 if(sVal.length() > 0){
				 customAssert.assertTrue(func_GRD_AddMainP(map_data, sVal),"Unable to add GRD Main Premises");
			 }
			 
			 sVal = (String)map_data.get("GRD_AddAdditionalP");
			 if(!sVal.contains("")){
				 customAssert.assertTrue(func_GRD_AddMainP(map_data, sVal),"Unable to add GRD Main Premises");
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

	public boolean func_GRD_AddMainP(Map<Object, Object> map_data, String s_Premises){
		boolean retValue = true;
		String s_Section = null;
		String s_Sheet = null;
		
		try{
			
			Map<String, List<Map<String, String>>> inner_map = null;
			
			switch(common.currentRunningFlow){
				case "NB" :
					inner_map = common.NB_Structure_of_InnerPagesMaps;
					break;
				case "MTA" :
					inner_map = common.MTA_Structure_of_InnerPagesMaps;
					break;
				case "Requote" : 
					inner_map = common.Requote_Structure_of_InnerPagesMaps;
					break;
				case "Renewal" :
					inner_map = common.Renewal_Structure_of_InnerPagesMaps;
					break;
				case "Rewind" : 
					inner_map = common.Rewind_Structure_of_InnerPagesMaps;
					break;
			}
			
				if(s_Premises.contains("AddMainP")){
					s_Section = "AddMP";
					s_Sheet = "GRDAddMainP";
				}else{
					s_Section = "AddAP";
					s_Sheet = "GRDAddAdditionalP";
				}
				
				
				String AllPremises[] =  s_Premises.split(";");
				
				for(int i = 0; i < AllPremises.length; i++){
					
						if(s_Section.contains("AddMP")){
							customAssert.assertTrue(k.Click("COB_GRD_AddMainP"),"Unable to click on GRDAddMainPremises button");
						}else{
							customAssert.assertTrue(k.Click("COB_GRD_AddAdditionalP"),"Unable to click on GRD_AddAdditionalP button");
						}
					 
					 customAssert.assertTrue(common.funcButtonSelection("Copy Correspondence Address"),"Unable to click on Copy correspondance Address button");
					 customAssert.assertTrue(k.Input("COB_GRD_AddMP_ArchitectPostCode", (String)inner_map.get(s_Sheet).get(i).get("GRD_"+s_Section+"_ArchitectPostCode")),"Unable to put value in ArchitectPostCode field");
					 customAssert.assertTrue(k.Input("COB_GRD_AddMP_EngineerPostCode", (String)inner_map.get(s_Sheet).get(i).get("GRD_"+s_Section+"_EngineerPostCode")),"Unable to put value in EngineerPostCode field");
					 customAssert.assertTrue(k.DropDownSelection("COB_GRD_AddMP_TypeOfPremises", (String)inner_map.get(s_Sheet).get(i).get("GRD_"+s_Section+"_TypeOfPremises")),"Unable to put value in TypeOfPremises field");
					 customAssert.assertTrue(k.Input("COB_GRD_AddMP_AgeAndCondition", (String)inner_map.get(s_Sheet).get(i).get("GRD_"+s_Section+"_AgeAndCondition")),"Unable to put value in AgeAndCondition field");
					 customAssert.assertTrue(k.Input("COB_GRD_AddMP_ProvideFullDetails",(String)inner_map.get(s_Sheet).get(i).get("GRD_"+s_Section+"_ProvideFullDetails")),"Unable to put value in  PleaseProvideFullDetails field");
					 customAssert.assertTrue(k.DropDownSelection("COB_GRD_AddMP_ConstrustionMethod", (String)inner_map.get(s_Sheet).get(i).get("GRD_"+s_Section+"_ConstrustionMethod")),"Unable to put value in ConstrustionMethod field");
					 customAssert.assertTrue(k.Input("COB_GRD_AddMP_StucturalWorkPerc", (String)inner_map.get(s_Sheet).get(i).get("GRD_"+s_Section+"_SucturalWorkPerc")),"Unable to put value in StucturalWorkPerc field");
					 customAssert.assertTrue(k.Input("COB_GRD_AddMP_HeatWorkPerc", (String)inner_map.get(s_Sheet).get(i).get("GRD_"+s_Section+"_HeatWorkPerc")),"Unable to put value in HeatWorkPerc field");
					 customAssert.assertTrue(k.Input("COB_GRD_AddMP_ContractPrice", (String)inner_map.get(s_Sheet).get(i).get("GRD_"+s_Section+"_ContractPrice")),"Unable to put value in ContractPrice field");
					 
					 // Start Date and Completion date :
					 
					 	customAssert.assertTrue(k.Click("COB_GRD_AddMP_WorkStartDate"), "Unable to Click WorkStartDate");
						customAssert.assertTrue(k.Input("COB_GRD_AddMP_WorkStartDate", (String)inner_map.get(s_Sheet).get(i).get("GRD_"+s_Section+"_WorkStartDate")),"Unable to Enter WorkStartDate.");
						customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
						customAssert.assertTrue(!k.getAttributeIsEmpty("COB_GRD_AddMP_WorkStartDate", "value"),"WorkStartDate Field Should Contain Valid value  .");
						
						customAssert.assertTrue(k.Click("COB_GRD_AddMP_CompletionDate"), "Unable to Click CompletionDate.");
						customAssert.assertTrue(k.Input("COB_GRD_AddMP_CompletionDate", (String)inner_map.get(s_Sheet).get(i).get("GRD_"+s_Section+"_CompletionDate")),"Unable to Enter CompletionDate.");
						customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
						customAssert.assertTrue(!k.getAttributeIsEmpty("COB_GRD_AddMP_CompletionDate", "value"),"CompletionDate Field Should Contain Valid value  .");
						
					customAssert.assertTrue(k.Input("COB_GRD_AddMP_ReinstatementValue", (String)inner_map.get(s_Sheet).get(i).get("GRD_"+s_Section+"_ReinstatementValue")),"Unable to put value in ReinstatementValue field");
					customAssert.assertTrue(k.Input("COB_GRD_AddMP_SiteSecurity", (String)inner_map.get(s_Sheet).get(i).get("GRD_"+s_Section+"_SiteSecurity")),"Unable to put value in SiteSecurity field");
					
					
					//Referral code
					double contract_price_including_VAT = k.getAttribute("COB_GRD_AddMP_ContractPrice", "value").equals("")?0.0:Double.parseDouble(k.getAttribute("COB_GRD_AddMP_ContractPrice", "value"));
					double reinstatement_value_including_VAT = k.getAttribute("COB_GRD_AddMP_ReinstatementValue", "value").equals("")?0.0:Double.parseDouble(k.getAttribute("COB_GRD_AddMP_ReinstatementValue", "value"));
					
					double _40_cent_of_reinst_value = (reinstatement_value_including_VAT * 40)/100;
					if(contract_price_including_VAT < _40_cent_of_reinst_value){
						common_VELA.referrals_list.add((String)map_data.get("RM_ExistingStructures_ContractPrice"));
					}
					
					// Put Values in All Dropdown :
						k.ImplicitWaitOff();
						String q_value = null;
						 
						List<WebElement> elements = driver.findElements(By.className("selectinput"));
						 Select sel = null;
						
						 for(int j = 7;j<elements.size();j++){
							 if(elements.get(j).isDisplayed()){
								 
								 sel = new Select(elements.get(j));
								 try{
									 q_value = (String)inner_map.get(s_Sheet).get(i).get("GRD_"+s_Section+"_Q"+(j-5));
									 if(j!=12){
										 
										 //sel.selectByIndex(2);
										 sel.selectByVisibleText(q_value);
									 } 
								 }
								 catch(Throwable t){
									 sel.selectByVisibleText("No");
									 //System.out.println("Error while selecting Material Facts questions .");
								 } 
							 }		 
						 }
						 
						 customAssert.assertTrue(common.funcButtonSelection("Save"), "Unable to click on Save Button on Main Premises screen");
						 customAssert.assertTrue(k.Click("CCF_Btn_Back"), "Unable to click on Back Button on Main Premises screen");
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
			 customAssert.assertTrue(common.funcPageNavigation("Material Facts and Declarations", ""),"Material Facts and Declarations page is having issue(S)");
			 k.ImplicitWaitOff();
			 String q_value = null;
			 
			 Map<Object, Object> map_data = null;
			 
			 switch(common.currentRunningFlow){
				 case "NB" :
					 map_data = common.NB_excel_data_map;
					 break;
				 case "MTA" :
					 map_data = common.MTA_excel_data_map;
					 break;
				 case "CAN" :
					 map_data = common.CAN_excel_data_map;
					 break;
				 case "Rewind" :
					 map_data = common.Rewind_excel_data_map;
					 break;
				 case "Requote" :
					 map_data = common.Requote_excel_data_map;
					 break;
				 case "Renewal" :
					 map_data = common.Renewal_excel_data_map;
					 break;
			 }
			 
			List<WebElement> elements = driver.findElements(By.className("selectinput"));
			 Select sel = null;
			
			 for(int i = 0;i<elements.size();i++){
				 if(elements.get(i).isDisplayed()){
					 
					 sel = new Select(elements.get(i));
					 try{
						 q_value = (String)map_data.get("MFD_Q"+(i+1));
						 
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
			 
			 
			 
			 
			// 
			 
			 if(k.isDisplayedField("MFD_PLMF")){
				 
				 String[] geographical_Limits = ((String)map_data.get("MFD_PLMF")).split(",");
				 
				 customAssert.assertTrue(k.Click("MFD_PLMF"),"Error while Clicking Geographic Limit List object . ");
				 List<WebElement> _prd_liability_MF = driver.findElements(By.xpath("//html//body//span//span//following::ul//li"));	
				 	for(String geo_limit : geographical_Limits){
						for(WebElement each_ul : _prd_liability_MF){
							//customAssert.assertTrue(k.Click("MFD_PLMF"),"Error while Clicking Geographic Limit List object . ");
							//each_ul.click();
							k.waitTwoSeconds();//*[text()=
							System.out.println(each_ul.findElement(By.xpath("//*[text()='"+geo_limit+"']")).isDisplayed());
							if(each_ul.findElement(By.xpath("//*[text()='"+geo_limit+"']")).isDisplayed())
								each_ul.findElement(By.xpath("//*[text()='"+geo_limit+"']")).click();
							else
								continue;
							break;
						}
					}
				
			 }
			 		 
			 	List<WebElement> txt_Elements = driver.findElements(By.className("write"));
			 q_value = (String)map_data.get("MFD_T");
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

	public boolean funcPolicyGeneral(Map<Object, Object> map_data, String code, String event) {
		boolean retVal = true;
		String testName = "";
		try{
//			common.currentRunningFlow = "NB";
		
			//customAssert.assertTrue(common.funcMenuSelection("Navigate", "Policy General"), "Unable to navigate to policy general screen");
			customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");
			customAssert.assertTrue(k.Input("COB_PG_InsuredName", (String)map_data.get("PG_InsuredName")),	"Unable to enter value in Insured Name  field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_InsuredName", "value"),"Insured Name Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("COB_PG_BusinessEstYear", (String)map_data.get("PG_YearEstablished")),	"Unable to enter value in Business Instablished year  field .");
			customAssert.assertTrue(k.Input("COB_PG_TurnOver", (String)map_data.get("PG_TurnOver")),	"Unable to enter value in Turnover field .");
			
			String[] geographical_Limits = ((String)map_data.get("PG_GeoLimit")).split(",");
			List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
			for(String geo_limit : geographical_Limits){
				for(WebElement each_ul : ul_elements){
					customAssert.assertTrue(k.Click("COB_PG_GeoLimit"),"Error while Clicking Geographic Limit List object . ");
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
					customAssert.assertTrue(k.Click("COB_PG_InsuredDetails_Q1"),"Error while Clicking Professional Bodies List object . ");
					k.waitTwoSeconds();
					if(each_ul.findElement(By.xpath("//li[text()='"+pBodies_limit+"']")).isDisplayed())
						each_ul.findElement(By.xpath("//li[text()='"+pBodies_limit+"']")).click();
					else
						continue;
					break;
				}
			}
					
			customAssert.assertTrue(k.DropDownSelection("COB_PG_InsuredDetails_Q2", (String)map_data.get("PG_InsuredDetails_Q2")),"Unable to select Second qustion");
			String sVal = (String)map_data.get("PG_InsuredDetails_Q2");
			if(sVal.contains("Yes")){
				customAssert.assertTrue(k.Input("COB_PG_InsuredDetails_details", (String)map_data.get("PG_InsuredDetails_details")),	"Unable to enter value in Provided Details field .");
			}
			
			customAssert.assertTrue(k.DropDownSelection("COB_PG_QuoteValidity", (String)map_data.get("PG_QuoteValidity")),"Unable to select Auote Validity value");
			customAssert.assertTrue(k.Input("COB_PG_BusDesc", (String)map_data.get("PG_BusDesc")),	"Unable to enter value in Provided Details field .");
			
			// Select Trade Code :
				
				String sValue = (String)map_data.get("PG_TCS_TradeCode_Button");
				if(sValue.contains("Yes")){
					customAssert.assertTrue(COB_SelectTradeCode((String)map_data.get("PG_TCS_TradeCode") , "Policy Details" , 0),"Trade code selection function is having issue(S).");
				}
				
			// Create map for all trade codes, with values - Tech Adjust and comm adjust
				
			customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");		
							
			TestUtil.reportStatus("Entered all the details on Policy General page .", "Info", true);
			
			return retVal;
		
		}catch(Throwable t) {
	        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
	        Assert.fail("Unable to to do operation on policy General page. \n", t);
	        return false;
		}
		
	}

	public boolean COB_SelectTradeCode(String tradeCodeValue , String pageName , int currentPropertyIndex) {
		
			try{
				
				customAssert.assertTrue(k.Click("COB_Btn_SelectTradeCode"), "Unable to click on Select Trade Code button in Policy Details .");
				customAssert.assertTrue(common.funcPageNavigation("Multi Trade Code Selection", ""), "Navigation problem to TMulti Trade Code Selection page .");
				
				String sVal = tradeCodeValue;
				String[] TradeCodes = tradeCodeValue.split(",");
				
				String a_selectedTradeCode = null;
				String a_selectedTradeCode_desc = null;
				
				String tXpath = null;
				// first delete selected trade code
				tXpath = "//*[contains(@name,'multiTrade_')]";
				
				List<WebElement> tcodes = driver.findElements(By.xpath(tXpath));
				int t_Nos = tcodes.size();
				
				for(int i = 0; i<t_Nos; i++){
					tXpath = "//*[@id='table0']/tbody/tr["+(i+1)+"]/td[1]/input";
					if(k.chkboxSelection_Dynamic(tXpath)){
						driver.findElement(By.xpath(tXpath)).click();
						//break;
					}
				}
				
				for(String s_TradeCode : TradeCodes){
					tXpath = "//*[@name='multiTrade_'"+s_TradeCode+"]";					
		 			driver.findElement(By.name("multiTrade_"+s_TradeCode)).click();
		 			isTradeCodeSelected =  true;
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
		String navigationBy = CONFIG.getProperty("NavigationBy");
		CommonFunction_HHAZ.AdjustedTaxDetails.clear();
		try{
			NewBusinessFlow(code,"NB");
			
			common.currentRunningFlow="MTA";
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen.");
			customAssert.assertTrue(funcCreateEndorsement(),"Issue while creating Endorsement. ");
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
			customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.MTA_excel_data_map,code,TestBase.businessEvent,"Endorsement Submitted"), "Verify Policy Status (Submitted ) function is having issue(S) . ");
			
			customAssert.assertTrue(funcPolicyGeneral_MTA(common.MTA_excel_data_map,code,event), "Policy Details function having issue .");
			
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers screen.");
			customAssert.assertTrue(funcCovers(common.MTA_excel_data_map),"Error in selecting cover for MTA.");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts and Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			//customAssert.assertTrue(common_VELA.removeEndorsement(common.MTA_excel_data_map), "Unable to remove appropriate Endorsement in MTA flow.");
			if(((String)common.MTA_excel_data_map.get("CD_Public&ProductsLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen.");
				customAssert.assertTrue(PublicProductsLiabilityPage(common.MTA_excel_data_map), "PublicProductsLiabilityPage function is having issue(S) . ");
			}
			if(((String)common.MTA_excel_data_map.get("CD_EmployersLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to Employers Liability screen.");
				customAssert.assertTrue(EmployersLiabilityPage(common.MTA_excel_data_map), "EmployersLiabilityPage function is having issue(S) . ");
			}
			if(((String)common.MTA_excel_data_map.get("CD_AnnualWorks")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Annual Works"),"Issue while Navigating to Annual Works screen.");
				customAssert.assertTrue(AnnualWorkPage(common.MTA_excel_data_map), "AnnualWorkPage function is having issue(S) . ");
			}
			if(((String)common.MTA_excel_data_map.get("CD_SingleProject")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Single Project"),"Issue while Navigating to Single Project screen.");
				customAssert.assertTrue(SingleProjectPage(common.MTA_excel_data_map), "SingleProjectPage function is having issue(S) . ");
			}
			if(((String)common.MTA_excel_data_map.get("CD_OwnPlant")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Own Plant"),"Issue while Navigating to Own Plant screen . ");
				customAssert.assertTrue(common_COB.OwnPlantsPage(common.MTA_excel_data_map), "OwnPlantsPage function is having issue(S) . ");
				
			}
			if(((String)common.MTA_excel_data_map.get("CD_HiredInPlant")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Hired In Plant"),"Issue while Navigating to Employers Liability screen.");
				customAssert.assertTrue(HireInPlantPage(common.MTA_excel_data_map), "HireInPlantPage function is having issue(S) . ");
			}
			
			
			if(((String)common.MTA_excel_data_map.get("CD_JCT6.5.1")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to JCT 6.5.1 screen.");
				customAssert.assertTrue(JCTPage(common.MTA_excel_data_map), "JCTPage function is having issue(S) . ");
				
			}
			if(((String)common.MTA_excel_data_map.get("CD_Terrorism")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Terrorism"),"Issue while Navigating to Terrorism screen.");
				customAssert.assertTrue(TerrorismPage(common.MTA_excel_data_map), "TerrorismPage function is having issue(S) . ");
				
			}
			if(((String)common.MTA_excel_data_map.get("CD_PropertyOwnersLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Property Owners Liability"),"Issue while Navigating to Property Owners Liability screen.");
				customAssert.assertTrue(PropertyOwnersLiabilityPage(common.MTA_excel_data_map), "PropertyOwnersLiabilityPage function is having issue(S) . ");
				
			}
			if(((String)common.MTA_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){	
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
				customAssert.assertTrue(funcLegalExpenses(common.MTA_excel_data_map,code,event), "Legal Expenses function is having issue(S) . ");
			}
			
			//customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
			//customAssert.assertTrue(common_HHAZ.funcEndorsementOperations(common.MTA_excel_data_map),"Endorsement function is having issue(S).");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Legal Expenses screen . ");
			customAssert.assertTrue(common_VELA.funcPremiumSummary_MTA(common.MTA_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			common_VELA.PremiumFlag = false;
			customAssert.assertTrue(common_VELA.funcStatusHandling(common.MTA_excel_data_map,code,TestBase.businessEvent));
			customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");	
			customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
			TestUtil.reportTestCasePassed(testName);
		
		}catch (ErrorInTestMethod e) {
			System.out.println("Error in New Business test method for MTA > "+testName);
			throw e;
		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
		
		
		
	}
	public boolean funcCreateEndorsement(){
		
	    boolean retvalue = true;
	    Date dateobj = null;
	    String testName = null;
	    
	    Map<Object,Object> data_map = null;
		
		switch(common.currentRunningFlow){
			case "MTA":
				data_map = common.MTA_excel_data_map;
				break;
			case "Renewal":
				data_map = common.Renewal_excel_data_map;
				break;
		}
		
		testName = (String)data_map.get("Automation Key");
		 df = new SimpleDateFormat("dd/MM/yyyy");
	    try {
	    	customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page is not loaded to perfrm MTA . ");
	   
			customAssert.assertTrue(common.funcButtonSelection("Create Endorsement"), "Unable to click on Create Endorsement button .");
		
	    	int ammendmet_period = Integer.parseInt((String)data_map.get("MTA_EndorsementPeriod"));
	    	if(ammendmet_period > Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"))){
	    		TestUtil.reportStatus("Amendement Period Should not be greater than Policy Duration", "Fail", true);
	    		return false;
	    	}
	    	
	    	TimeZone uk_timezone = TimeZone.getTimeZone("Europe/London");
	    	Calendar c = Calendar.getInstance(uk_timezone);
	    	c.add(Calendar.DATE, ammendmet_period);
	    	dateobj = df.parse(df.format(c.getTime()));
	    	
	    	customAssert.assertTrue(k.Click("SPI_Endorsement_eff_date"), "Unable to enter Endorsement effective Date.");
	        customAssert.assertTrue(k.Type("SPI_Endorsement_eff_date", df.format(dateobj)), "Unable to Enter Endorsement effective Date .");
	        customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
	        customAssert.assertTrue(k.Input("SPI_Reson_for_Endors", (String)data_map.get("MTA_Reason_for_Endorsement")),"Unable to Enter Reason for Endorsement");
	        customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", testName, "MTA_EffectiveDate", k.getAttribute("SPI_Endorsement_eff_date", "value"),data_map),"Error while writing data to excel for field >MTA_EffectiveDate<");
	        customAssert.assertTrue(common.funcButtonSelection("Create Endorsement"), "Unable to click on Create Endorsement button .");
	    	customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", testName, "MTA_PolicyNumber", k.getText("SPI_MTA_policy_number"),data_map),"Error while writing data to excel for field >MTA Policy Date<");
	    	if(common.currentRunningFlow.equals("MTA")){
	    		if(((String)data_map.get("MTA_Status")).equals("Endorsement Rewind"))
	    			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", testName, "MTA_isMTARewind", "Y",data_map),"Error while writing data to excel for field >MTA is Rewind<");
	    		else
	    			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", testName, "MTA_isMTARewind", "N",data_map),"Error while writing data to excel for field >MTA is Rewind<");
	    	}
	        
	    	TestUtil.reportStatus("Create Endorsement Details filled successfully . ", "Info", true);
			
	        return retvalue;
	    
			} catch (ParseException e) {
				System.out.println("Unable to Parse Endorsement Dates - "+e.getMessage());
				return false;
			}
	    	catch(Throwable t) {
	             String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	             TestUtil.reportFunctionFailed("Failed in "+methodName+" function");  
	             k.reportErr("Failed in "+methodName+" function", t);
	             return false;
	      }  
	}
	public boolean funcUpdateCoverDetails_MTA(Map<Object, Object> map_data){
		   
		try {
				customAssert.assertTrue(common.funcPageNavigation("Covers", ""),"Cover page is having issue(S)");
				String coverName = null;
				String c_locator = null;
				k.pressDownKeyonPage();
				String all_cover = ObjectMap.properties.getProperty(CommonFunction.product+"_CD_AllCovers");
				String[] split_all_covers = all_cover.split(",");
				for(String coverWithLocator : split_all_covers){
					String coverWithoutLocator = coverWithLocator.split("_")[0];
					try{
						//CoversDetails_data_list.add(coverWithoutLocator);
						coverName = coverWithLocator.split("_")[0];	
						c_locator = coverWithLocator.split("__")[1];
						k.waitTwoSeconds();
						if(c_locator.equals("md")){
							
							
							if (!driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).isSelected()){
								if(((String) common.MTA_excel_data_map.get("CD_"+coverName)).equalsIgnoreCase("No"))
									continue;
								else
						 			customAssert.assertTrue(common.selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
							}else{
								if(((String) common.MTA_excel_data_map.get("CD_"+coverName)).equalsIgnoreCase("Yes"))
									continue;
								else
									customAssert.assertTrue(common.deSelectCovers(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
							}
						
						}else if(c_locator.equals("PEL")){
						
						}else if(c_locator.equals("cob_hcp")){
								if(((String)common.NB_excel_data_map.get("QC_AgencyName")).equalsIgnoreCase("Arthur J. Gallagher (UK) Ltd")){
									
									if (!driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).isSelected()){
										JavascriptExecutor j_exe = (JavascriptExecutor) driver;
										j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")));
										
											if(((String) common.MTA_excel_data_map.get("CD_"+coverName)).equalsIgnoreCase("No"))
												continue;
											else
									 			customAssert.assertTrue(common.selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
																
										}else{
											if(((String) common.MTA_excel_data_map.get("CD_"+coverName)).equalsIgnoreCase("Yes"))
												continue;
											else
												customAssert.assertTrue(common.deSelectCovers(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
										 }
								
								}
								else{
									
								}
						}
						else{
					
							if (!driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).isSelected()){
								JavascriptExecutor j_exe = (JavascriptExecutor) driver;
								j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")));
								
									if(((String) common.MTA_excel_data_map.get("CD_"+coverName)).equalsIgnoreCase("No"))
										continue;
									else
							 			customAssert.assertTrue(common.selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
														
								}else{
									if(((String) common.MTA_excel_data_map.get("CD_"+coverName)).equalsIgnoreCase("Yes"))
										continue;
									else
										customAssert.assertTrue(common.deSelectCovers(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
								 }
						
						}	
						customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Covers .");
						
					}catch(Throwable tt){
						System.out.println("Error while Updating Cover data for MTA - "+coverWithoutLocator);
						break;
					}
		 		}
	 	 
		 	  return true;
			} catch (Exception e) {
				return false;
			}
		   
	   }


	/**
	 * 
	 * This method handles all Cancellation Cases for COA product.
	 * 
	 */
	public void CancellationFlow(String code,String event) throws ErrorInTestMethod{
		
		String testName = (String)common.CAN_excel_data_map.get("Automation Key");
		try{
			NewBusinessFlow(code,"NB");
			
			common.currentRunningFlow="CAN";
			customAssert.assertTrue(common_COB.COB_CancelPolicy(common.CAN_excel_data_map), "Error while Cancelling policy . ");
			customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			
			customAssert.assertTrue(common_COB.COB_Cancel_PremiumSummary(common.CAN_excel_data_map), "Issue in verifying Premium summary for cancellation");
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""), "Unable to click on policies tab");
			Assert.assertTrue(common_VELA.funcStatusHandling(common.CAN_excel_data_map,code,TestBase.businessEvent));
					
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
			 customAssert.assertTrue(common.funcPageNavigation("Material Facts and Declarations", ""),"Material Facts and Declarations page is having issue(S)");
			 k.ImplicitWaitOff();
			 String mfd_q_value = null,mf_key="RM_MaterialFactsandDeclarations_";
			 
			 try{
			 //Work with silica, asbestos, or substances containing asbestos?
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("MFD_containingAsbestos");
			 if(mfd_q_value.equalsIgnoreCase("Yes"))
				 common_VELA.referrals_list.add((String)map_data.get(mf_key+"containingAsbestos"));
			 }catch(Throwable t){
				 }
			 
			 try{
			 //Work with acids, gases, chemicals, explosives, radioactive or similar dangerous liquids or substances?
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("MFD_dangerousLiquidsORsubstances");
			 if(mfd_q_value.equalsIgnoreCase("Yes"))
				 common_VELA.referrals_list.add((String)map_data.get(mf_key+"dangerousLiquidsORsubstances"));
			 }catch(Throwable t){
			 }
			 
			 try{
			 //Work on power stations, nuclear installations or establishments?
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("MFD_nuclearInstallations");
			 if(mfd_q_value.equalsIgnoreCase("Yes"))
				 common_VELA.referrals_list.add((String)map_data.get(mf_key+"nuclearInstallations"));
			 }catch(Throwable t){
			 }
			 
			 try{
			 //Work on refineries, bulk storage, or premises in oil, gas or chemical industries?
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("MFD_gasORchemicalIndustries");
			 if(mfd_q_value.equalsIgnoreCase("Yes"))
				 common_VELA.referrals_list.add((String)map_data.get(mf_key+"gasORchemicalIndustries"));
			 }catch(Throwable t){
			 }
			 
			 try{
			 //Work airside or on aircraft, hovercraft, aerospace systems, watercraft, railway, underground, quarries, underwater at docks, harbours or piers?
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("MFD_harboursORpiers");
			 if(mfd_q_value.equalsIgnoreCase("Yes"))
				 common_VELA.referrals_list.add((String)map_data.get(mf_key+"harboursORpiers"));
			 }catch(Throwable t){
			 }
				
			 try{
			 //Work in or on computer suites or on computers?
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("MFD_onComputers");
			 if(mfd_q_value.equalsIgnoreCase("Yes"))
				 common_VELA.referrals_list.add((String)map_data.get(mf_key+"onComputers"));
			 }catch(Throwable t){
			 }
			
			 try{
			 //Work on Bridges, viaducts, towers, steeples, chimney shafts or blast furnaces?
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("MFD_blastFurnace");
			 if(mfd_q_value.equalsIgnoreCase("Yes"))
				 common_VELA.referrals_list.add((String)map_data.get(mf_key+"blastFurnace"));
			 }catch(Throwable t){
			 }
			
			 try{
			 //Will work stop and a licensed sub-contractor be employed to deal with any such material?
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("MFD_anySuchMaterial");
			 if(mfd_q_value.equalsIgnoreCase("No"))
				 common_VELA.referrals_list.add((String)map_data.get(mf_key+"anySuchMaterial"));
			 }catch(Throwable t){
			 }
			
			 try{
			 //Will a licensed sub-contractor be employed to deal with any such material that does not require a license?
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("MFD_notRequireAlicense");
			 if(mfd_q_value.equalsIgnoreCase("No"))
				 common_VELA.referrals_list.add((String)map_data.get(mf_key+"notRequireAlicense"));
			 }catch(Throwable t){
			 }
			 
			 try{
			 //Do you source products from outside of the EU?
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("MFD_outside_of_the_EU");
			 if(mfd_q_value.equalsIgnoreCase("Yes"))
				 common_VELA.referrals_list.add((String)map_data.get(mf_key+"productsFromOutsideOfTheEU"));
			 }catch(Throwable t){
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
						 common_VELA.referrals_list.add(prd_value);
					 }
					 
				 }
			 }catch(Throwable t){
				 
			 }
			
			 
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
						 common_VELA.referrals_list.add("Products supplied to hazardous industries: "+_relevant_industries_vlaue);
					 }
					 
				 }
				
			 }catch(Throwable t){
				 
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




	//*************************START***********************************//
	//************************************************************//
	//**** COB Book Rate Calculator Based on CPRS Sheet ********//
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
				int cent_empWage = cent_employeeWages.get(i).getAttribute("value").equals("")?0:Integer.parseInt(cent_employeeWages.get(i).getAttribute("value"));
				annualWagerollalue = cent_empWage;
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
			case "MTA":
				data_map = common.MTA_excel_data_map;
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
				
				case "Annual Works":
				case "Single Project":
					
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
			case "MTA":
				data_map = common.MTA_excel_data_map;
				break;
			case "Rewind":
				data_map = common.Rewind_excel_data_map;
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
					AOP_discount = cent_AOW * (turnOver_contract_cent) 
					* (Double.parseDouble(PLEL_Rater.getProperty(coverName+"_Commercial_Industrial")));
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
		case "MTA":
			data_map = common.MTA_excel_data_map;
			break;
		case "Rewind":
			data_map = common.Rewind_excel_data_map;
			break;
		case "Renewal":
			data_map = common.Renewal_excel_data_map;
			break;

	}
		
		if(coverName.equals("Annual Works") || coverName.equals("Single Project"))
			coverName = "Works";
		
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
			case "MTA":
				data_map = common.MTA_excel_data_map;
				break;
			case "Rewind":
				data_map = common.Rewind_excel_data_map;
				break;
			case "Renewal":
				data_map = common.Renewal_excel_data_map;
				break;
		}
		
		Calendar now = Calendar.getInstance();   // Gets the current date and time
		int currentYear = now.get(Calendar.YEAR); 
		//int currentYear = Integer.parseInt(PLEL_Rater.getProperty("CurrentYear"));
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
		List<WebElement> wage_work_activities = null; 
		int total_selected_elm = 0;
		
		List<WebElement> cent_wage_work_activities = null;
		

		Map<Object,Object> data_map = null;
		
		switch(common.currentRunningFlow){
		
			case "NB":
				data_map = common.NB_excel_data_map;
				break;
			case "MTA":
				data_map = common.MTA_excel_data_map;
				break;
			case "Rewind":
				data_map = common.Rewind_excel_data_map;
				break;
			case "Renewal":
				data_map = common.Renewal_excel_data_map;
				break;
		
		}
		k.ImplicitWaitOff();
		try{
		
			wage_work_activities = driver.findElements(By.xpath("//select[contains(@name,'coa_empl_break_activity_2')]//option[@selected='selected']")); 
			total_selected_elm = wage_work_activities.size();
		
			cent_wage_work_activities = driver.findElements(By.xpath("//input[contains(@name,'coa_empl_break_percentage_total')]"));
		}catch(Throwable t){
			k.ImplicitWaitOn();
		}finally{
			k.ImplicitWaitOn();
		}
		
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
		
		if(total_selected_elm==0 && load_type.equalsIgnoreCase("heat") && coverName.equalsIgnoreCase("PL")){
			final_load = get_excluding_heat(coverName);
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

	public double get_excluding_heat(String coverName){

		String _LOI = null;
	switch(coverName){
	case "PL":
		_LOI = driver.findElement(By.xpath("//*[contains(@name,'coa_pl_pl_indemnity')]/option[@selected='selected']")).getText();
		break;
	case "EL":
		_LOI = driver.findElement(By.xpath("//*[contains(@name,'limit_of_indemnity')]/option[@selected='selected']")).getText();
		break;
	case "Annual Works":
		_LOI = driver.findElement(By.xpath("//*[contains(@name,'limit_of_indemnity')]")).getAttribute("value");
		break;
	case "Own Plant":
		_LOI = driver.findElement(By.xpath("//*[contains(@name,'own_plant_indemnity_limit')]")).getAttribute("value");
		break;
	case "Hired In Plant":
		_LOI = driver.findElement(By.xpath("//*[contains(@name,'hired_in_plant_limit')]")).getAttribute("value");
		break;
	}
	switch(_LOI){

		case "£1,000,000":
			
			return Double.parseDouble(PLEL_Rater.getProperty("Excluding_Heat_1M"));
		
			
			
		case "£2,000,000":
			
			return Double.parseDouble(PLEL_Rater.getProperty("Excluding_Heat_2M"));
		
			
			
		case "£5,000,000":
			
			return Double.parseDouble(PLEL_Rater.getProperty("Excluding_Heat_5M"));
			
			
			
		case "£10,000,000":
		case "£15,000,000":
		case "£20,000,000":
		case "£25,000,000":
			
			return Double.parseDouble(PLEL_Rater.getProperty("Excluding_Heat_10M"));
		
			
	}
	return 0.0;
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
		String works_LOI =null;

		if(!coverName.equalsIgnoreCase("Single Project"))
			works_LOI = k.getAttribute("COB_works_LOI", "value").replaceAll(",", "");
		else
			works_LOI = k.getAttribute("COB_works_LOI_SP", "value").replaceAll(",", "");
		
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
		String timber_Frame_Option = null;
		
		if(!coverName.equalsIgnoreCase("Single Project")){
			k.scrollInView("COB_AW_ErectionOfTimberFrame");
			timber_Frame_Option =k.GetDropDownSelectedValue("COB_AW_ErectionOfTimberFrame");
		}else{
			
			k.scrollInView("COB_SP_ErectionOfTimber");
		timber_Frame_Option = k.GetDropDownSelectedValue("COB_SP_ErectionOfTimber");
		}
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
			//System.out.println("Error while Calculating Bona Fide book rate - "+t);
		}
		//System.out.println("Bona Fide book rate = "+final_Bf_book_rate);
		return final_Bf_book_rate;
		
	}

	//This method calculates total Loads
	public void calculate_Total_Loads(String coverName){
		
		
		double total_PL_Loads = calculate_Height_Depth_Heat_Load("Height",coverName) + calculate_Height_Depth_Heat_Load("Depth",coverName) + 
								calculate_Height_Depth_Heat_Load("heat",coverName) + calculate_Years_Business_Established_Load();
		//System.out.println("Total Loads = "+total_PL_Loads);
		common_COB.Book_rate_Rater_output.put("Total_PL_Loads", total_PL_Loads);
	}
	public void calculate_Plant_Total_Loads(String coverName){
		
		double total_PL_Loads = 0.0;
		
		if(coverName.equals("PL")){
			total_PL_Loads = calculate_Height_Depth_Heat_Load("heat",coverName) + calculate_Years_Business_Established_Load();
		}else if(coverName.equals("EL")){
			total_PL_Loads = calculate_Years_Business_Established_Load();
		}
		
		
		//System.out.println("Total Loads = "+total_PL_Loads);
		common_COB.Book_rate_Rater_output.put("Total_PL_Loads", total_PL_Loads);
	}
	public void calculate_Works_Total_Loads(String coverName){
		
		double total_PL_Loads = 0.0;
		
		total_PL_Loads = calculate_Works_LOI_Load(coverName) + calculate_Works_Timber_Frame_Load(coverName) + calculate_Works_DE5_4_extension_Load(coverName);
		
		System.out.println("Total Loads = "+total_PL_Loads);
		common_COB.Book_rate_Rater_output.put("Total_PL_Loads", total_PL_Loads);
	}


	public double get_Plant_cover_Base_rate(String trade_Activity){
		
		
		double final_baseRate = 0;
		String policyBasis_base_rate = null;
		
		
		Map<Object,Object> data_map = null;
		
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
		
		}
		try{
			
			//OwnPlantPolicyBasis = k.GetDropDownSelectedValue("COB_OP_PolicyBasis");
		
			PLEL_Rater = OR.getORProperties();

			switch(trade_Activity){
			
				case "Blanket":
					policyBasis_base_rate = PLEL_Rater.getProperty("Blanket_Cover_BR");
				break;
				
				case "On Schedule":
					policyBasis_base_rate = PLEL_Rater.getProperty("On_Schedule_BR");
				break;
				
				case "Cranes(Mobile)":
				case "Cranes(Tower)":
					policyBasis_base_rate = PLEL_Rater.getProperty("Mobile_&_Tower_Cranes_BR");
					break ;
				case "Site Huts":
					policyBasis_base_rate = PLEL_Rater.getProperty("Site_Huts_BR");
					break ;
				case "Telehandlers":
					policyBasis_base_rate = PLEL_Rater.getProperty("Telehandlers_BR");
					break ;
				case "Mini Excavators":
					policyBasis_base_rate = PLEL_Rater.getProperty("Mini_Excavators_BR");
					break ;
				case "Portable Hand Tools":
					policyBasis_base_rate = PLEL_Rater.getProperty("Portable_Hand_Tools_BR");
					break ;
				case "All Other Plant":
					policyBasis_base_rate = PLEL_Rater.getProperty("Other_plant_BR");
					break ;
				default:
				    policyBasis_base_rate = "1.0";
				    break ;
			}
		
						
				
		}catch(Throwable t){
			System.out.println("Error while Calculating Plant_cover_Base_rate - "+t);
		}
		final_baseRate = Double.parseDouble(policyBasis_base_rate);
		return final_baseRate;
		
	}

	public double get_HIP_cover_Base_rate(String trade_Activity){
		
		
		double final_baseRate = 0;
		String policyBasis_base_rate = null;
		
		try{
			
			//OwnPlantPolicyBasis = k.GetDropDownSelectedValue("COB_OP_PolicyBasis");
		
			PLEL_Rater = OR.getORProperties();
		
			policyBasis_base_rate = PLEL_Rater.getProperty("Hired_in_Plant_BR");
				
		}catch(Throwable t){
			System.out.println("Error while Calculating HIP_cover_Base_rate - "+t);
		}
		final_baseRate = Double.parseDouble(policyBasis_base_rate);
		return final_baseRate;
		
	}

	public double get_Plant_Voluntary_Excess_Multiplier(String coverName){
		
		
		double final_Vol_Excess = 0;
		String Vol_Excess = null;

		int maxExcessVal = 0;
		try{
		
	  	customAssert.assertTrue(common.funcMenuSelection("Navigate", "Excesses"),"Unable To navigate to Excesses scrren");
	  	maxExcessVal = get_Plant_Max_Excess_value(coverName);
	  	customAssert.assertTrue(common.funcMenuSelection("Navigate", "Own Plant"),"Unable To navigate to Own Plant scrren");
	  	if(maxExcessVal >= 0 && maxExcessVal <= 2500){
	  		Vol_Excess = PLEL_Rater.getProperty("Vol_Excess_0_2500");
		}else if(maxExcessVal > 2500 && maxExcessVal <= 5000){
			Vol_Excess = PLEL_Rater.getProperty("Vol_Excess_2500_5000");
		}else if(maxExcessVal > 5000 && maxExcessVal <= 10000){
			Vol_Excess = PLEL_Rater.getProperty("Vol_Excess_5000_10000");
		}else if(maxExcessVal > 10000 && maxExcessVal <= 25000){
			Vol_Excess = PLEL_Rater.getProperty("Vol_Excess_10000_25000");
		}else if(maxExcessVal > 25000){
			Vol_Excess = PLEL_Rater.getProperty("Vol_Excess_10000_25000");
		}else{
			Vol_Excess = "1.0";
		}
	  		
				
		}catch(Throwable t){
			System.out.println("Error while Calculating Plant_Voluntary_Excess_Multiplier - "+t);
		}
		final_Vol_Excess = Double.parseDouble(Vol_Excess);
		return final_Vol_Excess;
		
	}

	public double get_Own_Plant_LOI_limit(String trade_Activity,String coverName){
		
		String _OP_LOI = null;
		int OP_LOI = 0;

		try{
		
			_OP_LOI =  k.getAttribute("COB_OP_LOI", "value").replaceAll(",","");
			OP_LOI = Integer.parseInt(_OP_LOI);
			
			if(OP_LOI > 5000000){
				return 0.0;
			}else
				return 1.0;
				
		}catch(Throwable t){
			System.out.println("Error while Calculating Own_Plant_LOI_limit - "+t);
			return 0.0;
		}
		
		
		
	}

	public double get_Own_Plant_Size_Multiplier(String trade_Activity,String coverName){
		
		
		String _own_plant_size_multi = null;
		String _total_Current_mkt_val = null;
		double final_own_plant_size_multi = 0.0;
		int total_Current_mkt_val = 0;

		try{
		
		if(trade_Activity.equalsIgnoreCase("Blanket") || trade_Activity.equalsIgnoreCase("On Schedule"))
			_total_Current_mkt_val =  k.getAttribute("COB_OP_Own_Plant_Market_Value", "value").replaceAll(",","");
		else
			_total_Current_mkt_val =  k.getAttribute("COB_OP_Total_Current_Market_Value", "value").replaceAll(",","");
		total_Current_mkt_val = Integer.parseInt(_total_Current_mkt_val);

	  	
	  	if(total_Current_mkt_val >= 0 && total_Current_mkt_val <= 100000){
	  		_own_plant_size_multi = PLEL_Rater.getProperty("Below_£100000");
		}else if(total_Current_mkt_val > 100000 && total_Current_mkt_val <= 250000){
			_own_plant_size_multi = PLEL_Rater.getProperty("Over_£100000");
		}else if(total_Current_mkt_val > 250000 && total_Current_mkt_val <= 500000){
			_own_plant_size_multi = PLEL_Rater.getProperty("Over_£250000");
		}else if(total_Current_mkt_val > 500000 && total_Current_mkt_val <= 750000){
			_own_plant_size_multi = PLEL_Rater.getProperty("Over_£500000");
		}else if(total_Current_mkt_val > 750000 && total_Current_mkt_val <= 1000000){
			_own_plant_size_multi = PLEL_Rater.getProperty("Over_£750000");
		}else if(total_Current_mkt_val > 1000000 && total_Current_mkt_val <= 2500000){
			_own_plant_size_multi = PLEL_Rater.getProperty("Over_£1000000");
		}else if(total_Current_mkt_val > 2500000 ){
			_own_plant_size_multi = PLEL_Rater.getProperty("Over_£2500000");
		}else{
			_own_plant_size_multi="1.0";
		}
	  		
				
		}catch(Throwable t){
			System.out.println("Error while Calculating Own_Plant_Size_Multiplier - "+t);
		}
		final_own_plant_size_multi = Double.parseDouble(_own_plant_size_multi);
		return final_own_plant_size_multi;
		
	}

	public double get_Own_Plant_Quotient_Multiplier(String trade_Activity,String coverName){
		
		
		String _own_plant_quo_multi = null;
		String _sum_insured = null,Own_Plant_LOI=null,OwnPlantPolicyBasis=null;
		double final_own_plant_quo_multi = 0.0,division_SumByLOI=0.0;
		int total_sum_insured = 0,op_LOI=0;

		try{
			
			OwnPlantPolicyBasis = k.GetDropDownSelectedValue("COB_OP_PolicyBasis");
			
			if(OwnPlantPolicyBasis.equals("Specified")){
				return 1; //Quotient applies to Blanket and On-Schedule only
			}
		
			_sum_insured =  k.getAttribute("COB_OP_Own_Plant_Market_Value", "value").replaceAll(",","");
			total_sum_insured = Integer.parseInt(_sum_insured);
			
			Own_Plant_LOI =  k.getAttribute("COB_OP_LOI", "value").replaceAll(",","");
			op_LOI = Integer.parseInt(Own_Plant_LOI);
			
			division_SumByLOI = ((float)total_sum_insured/op_LOI);

	  	
	  	if(division_SumByLOI < 2.5){
	  		_own_plant_quo_multi = PLEL_Rater.getProperty("Own_Plant_Q_Below_2.5");
		}else if(division_SumByLOI >= 2.5 && division_SumByLOI <= 5){
			_own_plant_quo_multi = PLEL_Rater.getProperty("Own_Plant_Q_2.5_5");
		}else if(division_SumByLOI > 5 && division_SumByLOI <= 10){
			_own_plant_quo_multi = PLEL_Rater.getProperty("Own_Plant_Q_5_10");
		}else if(division_SumByLOI > 10 && division_SumByLOI <= 20){
			_own_plant_quo_multi = PLEL_Rater.getProperty("Own_Plant_Q_10_20");
		}else if(division_SumByLOI > 20 && division_SumByLOI <= 50){
			_own_plant_quo_multi = PLEL_Rater.getProperty("Own_Plant_Q_20_50");
		}else if(division_SumByLOI > 50){
			_own_plant_quo_multi = PLEL_Rater.getProperty("Own_Plant_Q_Over_50");
		}else{
			_own_plant_quo_multi="1.0";
		}
	  		
				
		}catch(Throwable t){
			System.out.println("Error while Calculating Own_Plant_Quo_Multiplier - "+t);
		}
		final_own_plant_quo_multi = Double.parseDouble(_own_plant_quo_multi);
		return final_own_plant_quo_multi;
		
	}

	public double get_Security_Multiplier(String trade_Activity,String coverName){
		
		
		String _Equipment_Security = null,_Equipment_Security_val=null;
		double final_security_multi = 0.0;

		try{
			
			_Equipment_Security = k.GetDropDownSelectedValue("COB_OP_Equipment_Security");
		
			switch(_Equipment_Security){
			
			case "Immobilisers":
				_Equipment_Security_val = PLEL_Rater.getProperty("Thatcham_P2_Immobiliser");
				break;
			case "Trackers":
				_Equipment_Security_val = PLEL_Rater.getProperty("Thatcham_P5_Tracking");
				break;
			case "CESAR":
				_Equipment_Security_val = PLEL_Rater.getProperty("CESAR");
				break;
			case "Other":
				_Equipment_Security_val = "1.0";
				break;
			default:
				_Equipment_Security_val = "1.0";
				break;
			
			}
	  	
			
		}catch(Throwable t){
			System.out.println("Error while Calculating Security_Multiplier - "+t);
		}
		final_security_multi = Double.parseDouble(_Equipment_Security_val);
		return final_security_multi;
		
	}

	public double get_Loss_Ratio_Multiplier(String trade_Activity,String coverName){
		
		
		String _loss_ratio = null;
		double final_loss_ratio = 0.0;
		int years_trading=0,loss_ratioCent=0;
		double loss_Ratio=0.0;
		
		Map<Object,Object> data_map = null;
		
		switch(common.currentRunningFlow){
		
			case "NB":
				data_map = common.NB_excel_data_map;
				break;
			case "MTA":
				data_map = common.MTA_excel_data_map;
				break;
			case "Rewind":
				data_map = common.Rewind_excel_data_map;
				break;
			case "Renewal":
				data_map = common.Renewal_excel_data_map;
				break;
		
		}

		try{
			loss_Ratio = Double.parseDouble((String)data_map.get("CE_TotalLossRatio"));
		
			//From Years Business Established
			years_trading = get_Years_Trading();
			
			if(years_trading <= 2){
				return 1;
			}
			
			if(loss_Ratio==0){
				_loss_ratio = PLEL_Rater.getProperty("Loss_Ratio_Multi_Nil");
			}else if(loss_ratioCent == 0){
				_loss_ratio = PLEL_Rater.getProperty("Loss_Ratio_Multi_Nil");
			}else if(loss_ratioCent > 0 && loss_ratioCent <= 15){
				_loss_ratio = PLEL_Rater.getProperty("Loss_Ratio_Multi_upto_15%");
			}else if(loss_ratioCent > 15 && loss_ratioCent <= 40){
				_loss_ratio = PLEL_Rater.getProperty("Loss_Ratio_Multi_upto_40%");
			}else if(loss_ratioCent > 40 && loss_ratioCent <= 60){
				_loss_ratio = PLEL_Rater.getProperty("Loss_Ratio_Multi_upto_60%");
			}else{
				_loss_ratio = "1.0";
			}
			
			
	  		
				
		}catch(Throwable t){
			System.out.println("Error while Calculating Loss_Ratio_Multipliers - "+t);
		}
		final_loss_ratio = Double.parseDouble(_loss_ratio);
		return final_loss_ratio;
		
	}

	public double get_Plant_Hire_Multiplier(String trade_Activity,String coverName){
		
		
		double final_plant_hire_multiplier = 0.0;
		String _plant_hire_multiplier = null;
		String sectionName = null;
		double operator_cent = 0;
		try{
		
		PLEL_Rater = OR.getORProperties();
		
			double total_Cent_Plant_usage = 0.0;
		
			List<WebElement> plant_Usage_Activites = driver.findElements(By.xpath("//*[contains(@name,'coa_plant_usage_values')]")); 
			int total_plant_activities = plant_Usage_Activites.size();
		
			List<WebElement> cent_plant_Usage_Activites = driver.findElements(By.xpath("//*[contains(@name,'coa_plant_usage_total')]")); 
		
			for(int i=0;i<total_plant_activities;i++){
			 
				sectionName = plant_Usage_Activites.get(i).getAttribute("value");
				if(sectionName.contains("Operator")){
					operator_cent = Double.parseDouble(cent_plant_Usage_Activites.get(i).getAttribute("value"));
					total_Cent_Plant_usage = total_Cent_Plant_usage + operator_cent;
				}else{
					continue;
				}
			}
			
			if(total_Cent_Plant_usage==100.0){
				_plant_hire_multiplier = PLEL_Rater.getProperty("Plant_Hire_100%");
			}else if(total_Cent_Plant_usage >= 75.0 && total_Cent_Plant_usage <= 99.0){
				_plant_hire_multiplier = PLEL_Rater.getProperty("Plant_Hire_75%_99%");
			}else if(total_Cent_Plant_usage < 75.0){
				_plant_hire_multiplier = PLEL_Rater.getProperty("Plant_Hire_Less_than_75%");
			}else{
				_plant_hire_multiplier = "1.0";
			}
			
				
		}catch(Throwable t){
			System.out.println("Error while Calculating Plant_Hire_Multiplier - "+t);
		}
		final_plant_hire_multiplier = Double.parseDouble(_plant_hire_multiplier);
		return final_plant_hire_multiplier;
		
	}

	public double get_HIP_LOI_Multiplier(String trade_Activity,String coverName){
		
		
		String _hip_loi_multi = null;
		String _hip_LOI = null;
		double final_hip_loi_multi = 0.0;
		int hip_LOI = 0;

		try{
		
			_hip_LOI =  k.getAttribute("COB_HIP_LOI", "value").replaceAll(",","");
			hip_LOI = Integer.parseInt(_hip_LOI);
			
			if(hip_LOI > 2500000){
				return 0.0;
			}

	  	
	  	if(hip_LOI >= 0 && hip_LOI <= 100000){
	  		_hip_loi_multi = PLEL_Rater.getProperty("HIP_LOI_Below_£100000");
		}else if(hip_LOI > 100000 && hip_LOI <= 250000){
			_hip_loi_multi = PLEL_Rater.getProperty("HIP_LOI_Over_£100000");
		}else if(hip_LOI > 250000 && hip_LOI <= 500000){
			_hip_loi_multi = PLEL_Rater.getProperty("HIP_LOI_Over_£250000");
		}else if(hip_LOI > 500000 && hip_LOI <= 1000000){
			_hip_loi_multi = PLEL_Rater.getProperty("HIP_LOI_Over_£500000");
		}else if(hip_LOI > 1000000 ){
			_hip_loi_multi = PLEL_Rater.getProperty("HIP_LOI_Over_£1000000");
		}else{
			_hip_loi_multi="1.0";
		}
	  		
				
		}catch(Throwable t){
			System.out.println("Error while Calculating HIP_LOI_Multiplier - "+t);
		}
		final_hip_loi_multi = Double.parseDouble(_hip_loi_multi);
		return final_hip_loi_multi;
		
	}


	public void calculate_Multiplying_Factor(String coverName){
		
		double total_Load_Discount_aplied = 0.0;
		double multiplying_Factor = 0.0;
		
		
		if(coverName.equalsIgnoreCase("Annual Works") || coverName.equalsIgnoreCase("Single Project")){
			
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
		//System.out.println("Total MF = "+multiplying_Factor);
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

	public int check_Works_LOI_limit(String coverName){
		
		PLEL_Rater = OR.getORProperties();
		String works_LOI = null;
		try{
			
			if(!coverName.equalsIgnoreCase("Single Project"))
				works_LOI = k.getAttribute("COB_works_LOI", "value").replaceAll(",", "");
			else
				works_LOI = k.getAttribute("COB_works_LOI_SP", "value").replaceAll(",", "");// For Single Project
			
			int _works_LOI = Integer.parseInt(works_LOI);
		
			if(_works_LOI > Integer.parseInt(PLEL_Rater.getProperty("Works_LOI_Limit")))
				return 1;
			else
				return 0;
		
		}catch(Throwable t){
			System.out.println("Error while checking Works LOI limit - "+t);
			return 0;
		}
		
	}

	public double get_Book_Rate(String trade_Activity,String coverName){
		
		double book_rate = 0.0,net_Base_Rate=0.0;
		String activity = null;
		int works_LOI_limit=0;
		
		if(coverName.equalsIgnoreCase("Annual Works") || coverName.equalsIgnoreCase("Single Project")){
			
			works_LOI_limit = check_Works_LOI_limit(coverName);
			if(works_LOI_limit == 1){
				//System.out.println("Book Rate of "+trade_Activity + " = "+0.0);
				common_COB.Book_rate_Rater_output.put(coverName+"_"+trade_Activity, 0.0);
				return 0.0;
			}
		}
		
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
		
		//System.out.println("Book Rate of "+activity + " = "+book_rate);
		common_COB.Book_rate_Rater_output.put(coverName+"_"+trade_Activity, book_rate);
		return book_rate;
			
	}

	public double get_Plant_Cover_Book_Rate(String trade_Activity,String coverName){
		
		double book_rate = 0.0;
		double plant_base_rate = get_Plant_cover_Base_rate(trade_Activity);
		double Own_Plant_LOI_limit_check = get_Own_Plant_LOI_limit(trade_Activity, coverName);
		if(Own_Plant_LOI_limit_check == 0.0){
			return 0.0;}
		double Voluntary_Excess_Multiplier  = get_Plant_Voluntary_Excess_Multiplier(coverName);
		double Own_Plant_Size_Multiplier  = get_Own_Plant_Size_Multiplier(trade_Activity, coverName);
		double Own_Plant_Quotient_Multiplier = get_Own_Plant_Quotient_Multiplier(trade_Activity, coverName);
		double Security_Multiplier = get_Security_Multiplier(trade_Activity, coverName);
		double Loss_Ratio_Multiplier = get_Loss_Ratio_Multiplier(trade_Activity, coverName);
		double Plant_Hire_Multiplier = get_Plant_Hire_Multiplier(trade_Activity, coverName);
		
		book_rate = plant_base_rate * Voluntary_Excess_Multiplier * Own_Plant_Size_Multiplier * Own_Plant_Quotient_Multiplier * Security_Multiplier * Loss_Ratio_Multiplier * Plant_Hire_Multiplier;
		
		System.out.println("Book Rate of "+trade_Activity + " = "+book_rate);
		common_COB.Book_rate_Rater_output.put(coverName+"_"+trade_Activity, book_rate);
		return book_rate;
			
	}

	public double get_HIP_Cover_Book_Rate(String trade_Activity,String coverName){
		
		double book_rate = 0.0;
		double plant_base_rate = get_HIP_cover_Base_rate(trade_Activity);
		double HIP_LOI_Multiplier  = get_HIP_LOI_Multiplier(trade_Activity, coverName);
		double Loss_Ratio_Multiplier = get_Loss_Ratio_Multiplier(trade_Activity, coverName);
		double Plant_Hire_Multiplier = get_Plant_Hire_Multiplier(trade_Activity, coverName);
		
		book_rate = plant_base_rate * HIP_LOI_Multiplier * Loss_Ratio_Multiplier * Plant_Hire_Multiplier ;
		
		System.out.println("Book Rate of "+trade_Activity + " = "+book_rate);
		common_COB.Book_rate_Rater_output.put(coverName+"_"+trade_Activity, book_rate);
		return book_rate;
			
	}

	public double get_Net_Base_Rate(String trade_Activity,String cover){
		
		double r_value=0.0;
		DecimalFormat formatter = new DecimalFormat("###.####");
		
		try{
			
			if(cover.equals("Annual Works") || cover.equals("Single Project"))
				cover = "Works";
		
			PLEL_Rater = OR.getORProperties();
			String t_activity = trade_Activity.replaceAll(" -", "").replaceAll(" ", "_").replaceAll(",", "");
			t_activity = t_activity + "_"+cover + "_NBR";
		
			r_value = Double.parseDouble(PLEL_Rater.getProperty(t_activity));
			r_value = Double.valueOf(formatter.format(r_value));
		
		}catch(Throwable t ){
			System.out.println("Error while getting Net Base Rate for activity > "+trade_Activity+" < "+t.getMessage());
		}
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
				}else if(activity.contains("Asbestos") || activity.contains("CPA Contract Lift Cover")
						|| activity.contains("Other-PL")) {
					common_COB.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
					continue;
				}else{
					
					get_Book_Rate(activity,coverName);
					common_COB.isIncludingHeatPresent = false;
					common_COB.isExcludingHeatPresent = false;
					common_COB.cent_work_including_heat=0.0;
					
				}
			}
			TestUtil.reportStatus("Book Rate for Public and Products Liability Cover calculated Successfully via lookup . ", "Info", false);
			
			break;
			
		case "EL": //Employers Liability
			
			List<String> el_activites = get_PL_Activities();
			for(String activity : el_activites){
				if(activity.contains("Asbestos") || activity.contains("Offshore Work & Visits") 
						|| activity.contains("Overseas Work") || activity.contains("Other-EL") || activity.isEmpty()) {
					common_COB.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
					continue;
				}else{
					get_Book_Rate(activity,coverName);
					common_COB.isIncludingHeatPresent = false;
					common_COB.isExcludingHeatPresent = false;
					common_COB.cent_work_including_heat=0.0;
				}
			}
			TestUtil.reportStatus("Book Rate for Employers Liability Cover calculated Successfully via lookup . ", "Info", false);
			
			break;
			
			
		case "Annual Works": //Annual Works
			
			List<String> aw_activites = get_PL_Activities();
			for(String activity : aw_activites){
				if(activity.contains("DE") || activity.contains("Existing Structures") || activity.contains("Other-Works")){
					common_COB.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
				}else {
					
					get_Book_Rate(activity,coverName);
					common_COB.isIncludingHeatPresent = false;
					common_COB.isExcludingHeatPresent = false;
					common_COB.cent_work_including_heat=0.0;
					
				}
			}
			TestUtil.reportStatus("Book Rate for Annual Works Cover calculated Successfully via lookup . ", "Info", false);
			
			break;
			
		case "Single Project": //Single Project
			
			List<String> sp_activites = get_PL_Activities();
			for(String activity : sp_activites){
				if(activity.contains("DE") || activity.contains("Existing Structures") || activity.contains("Other-Works")){
					common_COB.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
				}else {
					
					get_Book_Rate(activity,coverName);
					common_COB.isIncludingHeatPresent = false;
					common_COB.isExcludingHeatPresent = false;
					common_COB.cent_work_including_heat=0.0;
					
				}
			}
			TestUtil.reportStatus("Book Rate for Single Project Cover calculated Successfully via lookup . ", "Info", false);
			
			break;
			
		case "Own Plant"://Own Plant
			
			List<String> plant_activites = get_PL_Activities();
			for(String activity : plant_activites){
				if(activity.contains("Employee Tools") || activity.contains("CPA Contract Lift Cover") || activity.contains("Subrogation Waiver")
						|| activity.contains("Third Party Machinery Movement") || activity.contains("Gap Protection") ||
						activity.contains("Continuing Rental Payments") || activity.contains("Breakdown") ||
						activity.contains("Agreed Valuation") || activity.contains("Other-Plant")|| activity.isEmpty()){
					common_COB.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
				}else {
					
					get_Plant_Cover_Book_Rate(activity,coverName);
				
					
				}
			}
			TestUtil.reportStatus("Book Rate for "+coverName+" Cover calculated Successfully via lookup . ", "Info", false);
			
			break;
			
		
		case "Hired In Plant"://Hired In Plant
			
			List<String> hip_activites = get_PL_Activities();
			for(String activity : hip_activites){
				if(activity.contains("CPA Contract Lift Cover") || activity.contains("Other-HIP")){
					common_COB.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
				}else {
					get_HIP_Cover_Book_Rate(activity,coverName);
				}
			}
			TestUtil.reportStatus("Book Rate for "+coverName+" Cover calculated Successfully via lookup . ", "Info", false);
			
			break;
			default:
				break;
				
		
		}	
			
		
		}catch(Throwable t){
			return false;
		}
		
		
		//System.out.println("Book Rate lookup Outputs = "+common_COB.Book_rate_Rater_output);
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

	public List<String> get_Own_Plant_Specified_activity_from_table(){
		
		List<String> specified_activies = new ArrayList<>();
		
		try{
		
		String bookRate_xpath = "//a[text()='Apply Book Rates']//following::table[@id='table0']";
		WebElement bookRate_Table = driver.findElement(By.xpath(bookRate_xpath));
		
		k.ScrollInVewWebElement(bookRate_Table);
		
		int _tble_Rows = driver.findElements(By.xpath("//a[text()='Apply Book Rates']//following::table[@id='table0']//tbody//tr")).size();

		for(int row = 1; row < _tble_Rows ;row ++){
		
			WebElement sec_Val = driver.findElement(By.xpath(bookRate_xpath+"//tbody//tr["+row+"]//td["+1+"]"));
			specified_activies.add(sec_Val.getText());
			
		}
		
		}catch(Throwable t){
			System.out.println("Error while getting own plant specified activies from table "+t);
		}
		return specified_activies;
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

	public int get_Plant_Max_Excess_value(String coverName){
		
		
		int section_head_index = 0,excessValue=0,maxExcessVal=0;
		String excessName = null;

		try{
			
		customAssert.assertTrue(common.funcPageNavigation("Excesses", ""),"Excesses page not loaded");
			
		
		String excessTable_xpath = "//div[text()='Standard Excesses']//following::table";
		WebElement excessTable = driver.findElement(By.xpath(excessTable_xpath));
		
		k.ScrollInVewWebElement(excessTable);
		
		
		int _tble_Headers = driver.findElements(By.xpath("//div[text()='Standard Excesses']//following::table//thead//th")).size();
		for(int row = 1; row < _tble_Headers ;row ++){
			
			WebElement sec_Val = driver.findElement(By.xpath(excessTable_xpath+"//thead//th["+row+"]"));
			if(sec_Val.getText().equalsIgnoreCase("Section")){
				section_head_index = row;
				break;
			}
		}

		int _tble_Rows = driver.findElements(By.xpath("//div[text()='Standard Excesses']//following::table//tbody//tr")).size();
		
		for(int row = 1; row < _tble_Rows ;row ++){
			
			WebElement sec_Val = driver.findElement(By.xpath(excessTable_xpath+"//tbody//tr["+row+"]//td["+section_head_index+"]"));
			if(sec_Val.getText().equals(coverName)){
				WebElement execess = driver.findElement(By.xpath(excessTable_xpath+"//tbody//tr["+row+"]//td["+2+"]"));
				excessName = execess.getText();
				
				if(excessName.equals("Theft & Malicious Damage") || excessName.equals("Any Other Loss")){
				
					WebElement execess_Val = driver.findElement(By.xpath(excessTable_xpath+"//tbody//tr["+row+"]//td["+3+"]//input"));
					excessValue = Integer.parseInt(execess_Val.getAttribute("value").replaceAll(",", ""));
				
					if(excessValue > maxExcessVal){
						maxExcessVal = excessValue;
					}
				}
					
			}else{
				continue;
			}
			
		}
		
		
		}catch(Throwable t){
			System.out.println("Error while getting Max. Excess Value from excess screen for cover >"+coverName+"< - "+t);
		}
		return maxExcessVal;
	}

	//Legal Expense Book Premium Calculation
	public double get_LE_Book_Premium(String LE_LOI,String LE_turnover){
		
		
		double book_premium=0.0,LOI_rate_multiplier=0.0;
		
		Map<Object,Object> data_map=new HashMap<>();
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
		}
		
		
		try{
			String key = "LE_BLC",NetNetRate=null,_40_Cent_of_actual_turnover=null;
			int turnOver = Integer.parseInt(LE_turnover.replaceAll(",",""));
			boolean isContractDisputes=false,isConstructionContractDisputes=false;
			PLEL_Rater = OR.getORProperties();
		
			isContractDisputes = driver.findElement(By.xpath("//*[contains(@name,'lea_contract_')]")).isSelected();
			isConstructionContractDisputes = driver.findElement(By.xpath("//*[contains(@name,'lea_const_contract_')]")).isSelected();
			
			if(isContractDisputes && isConstructionContractDisputes){
				key = key + "_CD_CCD";
			}else if(isContractDisputes){
				key = key + "_CD";
			}
			
			_40_Cent_of_actual_turnover = k.GetDropDownSelectedValue("COB_LE_Contract_Represent");
			
			if(isConstructionContractDisputes && LE_LOI.equals("500,000")){ //Referral Rule - 4
				common_VELA.referrals_list.add((String)data_map.get("RM_LegalExpense_ConstructionTradeLimit"));
				
				book_premium = 0.0;
			}else if(_40_Cent_of_actual_turnover.equalsIgnoreCase("Yes")){ //Referral Rule - 6
				common_VELA.referrals_list.add((String)data_map.get("RM_LegalExpense_ContractPercentageOfTurnover"));
				
				book_premium = 0.0;
			}else if(turnOver > 25000000){ //Referral Rule - 1
				common_VELA.referrals_list.add((String)data_map.get("RM_LegalExpense_TurnoverAmount"));
				
				book_premium = 0.0;
			}else{
				
				LOI_rate_multiplier = get_LOI_rate_multiplier(LE_LOI);
				NetNetRate = get_LE_NetNetRate(key,turnOver);
			
				book_premium = Double.parseDouble(NetNetRate) * LOI_rate_multiplier;
			}
		
		}catch(Throwable t){
			System.out.println("Error while calculating LE Book Premium - "+t);
		}
		
		return book_premium;
		
	}

	public double get_LOI_rate_multiplier(String LE_LOI){
		
		String loi_multiplier = null;
		int LOI = Integer.parseInt(LE_LOI.replaceAll(",",""));
		PLEL_Rater = OR.getORProperties();
		
		switch(LOI){
		
		case 100000:
			loi_multiplier = PLEL_Rater.getProperty("LOI_£100000");
			break;
		case 250000:
			loi_multiplier = PLEL_Rater.getProperty("LOI_£250000");
			break;
		case 500000:
			loi_multiplier = PLEL_Rater.getProperty("LOI_£500000");
			break;
		default:
			loi_multiplier = "1.0";
			break;
		
		}
		
		return Double.parseDouble(loi_multiplier);
		
	}

	public String get_LE_NetNetRate(String key,int turnOver){
		
		String NNR = null;
		try{
		
		if(turnOver <= 100000){
			NNR = PLEL_Rater.getProperty(key+"_100000_NNR");
		}else if(turnOver > 100000 && turnOver <= 250000){
			NNR = PLEL_Rater.getProperty(key+"_250000_NNR");
		}else if(turnOver > 250000 && turnOver <= 500000){
			NNR = PLEL_Rater.getProperty(key+"_500000_NNR");
		}else if(turnOver > 500000 && turnOver <= 1000000){
			NNR = PLEL_Rater.getProperty(key+"_1000000_NNR");
		}else if(turnOver > 1000000 && turnOver <= 1500000){
			NNR = PLEL_Rater.getProperty(key+"_1500000_NNR");
		}else if(turnOver > 1500000 && turnOver <= 2000000){
			NNR = PLEL_Rater.getProperty(key+"_2000000_NNR");
		}else if(turnOver > 2000000 && turnOver <= 2500000){
			NNR = PLEL_Rater.getProperty(key+"_2500000_NNR");
		}else if(turnOver > 2500000 && turnOver <= 3000000){
			NNR = PLEL_Rater.getProperty(key+"_3000000_NNR");
		}else if(turnOver > 3000000 && turnOver <= 3500000){
			NNR = PLEL_Rater.getProperty(key+"_3500000_NNR");
		}else if(turnOver > 3500000 && turnOver <= 4000000){
			NNR = PLEL_Rater.getProperty(key+"_4000000_NNR");
		}else if(turnOver > 4000000 && turnOver <= 4500000){
			NNR = PLEL_Rater.getProperty(key+"_4500000_NNR");
		}else if(turnOver > 4500000 && turnOver <= 5000000){
			NNR = PLEL_Rater.getProperty(key+"_5000000_NNR");
		}else if(turnOver > 5000000 && turnOver <= 6000000){
			NNR = PLEL_Rater.getProperty(key+"_6000000_NNR");
		}else if(turnOver > 6000000 && turnOver <= 7000000){
			NNR = PLEL_Rater.getProperty(key+"_7000000_NNR");
		}else if(turnOver > 7000000 && turnOver <= 8000000){
			NNR = PLEL_Rater.getProperty(key+"_8000000_NNR");
		}else if(turnOver > 8000000 && turnOver <= 9000000){
			NNR = PLEL_Rater.getProperty(key+"_9000000_NNR");
		}else if(turnOver > 9000000 && turnOver <= 10000000){
			NNR = PLEL_Rater.getProperty(key+"_10000000_NNR");
		}else if(turnOver > 10000000 && turnOver <= 12500000){
			NNR = PLEL_Rater.getProperty(key+"_12500000_NNR");
		}else if(turnOver > 12500000 && turnOver <= 15000000){
			NNR = PLEL_Rater.getProperty(key+"_15000000_NNR");
		}else if(turnOver > 15000000 && turnOver <= 17500000){
			NNR = PLEL_Rater.getProperty(key+"_17500000_NNR");
		}else if(turnOver > 17500000 && turnOver <= 20000000){
			NNR = PLEL_Rater.getProperty(key+"_20000000_NNR");
		}else if(turnOver > 20000000 && turnOver <= 22500000){
			NNR = PLEL_Rater.getProperty(key+"_22500000_NNR");
		}else if(turnOver > 22500000 && turnOver <= 25000000){
			NNR = PLEL_Rater.getProperty(key+"_25000000_NNR");
		}else if(turnOver > 25000000){
			NNR = PLEL_Rater.getProperty(key+"_Above_25000000_NNR");
		}
		
		}catch(Throwable t){
			System.out.println("Error while getting LE Net Net rate - "+t);
			return "";
		}
		
		return NNR;
		
	}

	public static boolean isBetween(int x, int lower, int upper) {
		  return lower > x && x <= upper;
		}

		

//************************END************************************//
//************************************************************//
//**** COA Book Rate Calculator Based on CPRS Sheet ********//
//************************************************************//
//************************************************************//
public boolean funcRewindOperation(){
		
		boolean r_value= true;
		String navigationBy = CONFIG.getProperty("NavigationBy");
		try{
			
			if(((String)common.NB_excel_data_map.get("CD_Add_Remove_Cover")).equalsIgnoreCase("Yes")){
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
				customAssert.assertTrue(common_VELA.funcRewindCoversCheck(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts and Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
				customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
				
				if(((String)common.NB_excel_data_map.get("CD_Public&ProductsLiability")).equals("No") &&
						((String)common.NB_excel_data_map.get("CD_Add_Public&ProductsLiability")).equals("Yes")){	
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen.");
					customAssert.assertTrue(PublicProductsLiabilityPage(common.NB_excel_data_map), "PublicProductsLiabilityPage function is having issue(S) . ");
				}
				if(((String)common.NB_excel_data_map.get("CD_EmployersLiability")).equals("No") &&	
					((String)common.NB_excel_data_map.get("CD_Add_EmployersLiability")).equals("Yes")){
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to Employers Liability screen.");
					customAssert.assertTrue(EmployersLiabilityPage(common.NB_excel_data_map), "EmployersLiabilityPage function is having issue(S) . ");
				}
				if(((String)common.NB_excel_data_map.get("CD_AnnualWorks")).equals("No") && 
						((String)common.NB_excel_data_map.get("CD_Add_AnnualWorks")).equals("Yes")){	
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Annual Works"),"Issue while Navigating to Annual Works screen.");
					customAssert.assertTrue(common_COB.AnnualWorkPage(common.NB_excel_data_map), "AnnualWorkPage function is having issue(S) . ");
				}
				if(((String)common.NB_excel_data_map.get("CD_SingleProject")).equals("No") && 
						((String)common.NB_excel_data_map.get("CD_Add_SingleProject")).equals("Yes")){	
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Single Project"),"Issue while Navigating to Single Project screen.");
					customAssert.assertTrue(SingleProjectPage(common.NB_excel_data_map), "SingleProjectPage function is having issue(S) . ");
				}
				if(((String)common.NB_excel_data_map.get("CD_OwnPlant")).equals("No") && 
						((String)common.NB_excel_data_map.get("CD_Add_OwnPlant")).equals("Yes")){	
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Own Plant"),"Issue while Navigating to Own Plant screen . ");
					customAssert.assertTrue(common_COB.OwnPlantsPage(common.NB_excel_data_map), "OwnPlantsPage function is having issue(S) . ");
					
				}
				if(((String)common.NB_excel_data_map.get("CD_HiredInPlant")).equals("No") && 
						((String)common.NB_excel_data_map.get("CD_Add_HiredInPlant")).equals("Yes")){	
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Hired In Plant"),"Issue while Navigating to Employers Liability screen.");
					customAssert.assertTrue(HireInPlantPage(common.NB_excel_data_map), "HireInPlantPage function is having issue(S) . ");
				}
				if(((String)common.NB_excel_data_map.get("CD_JCT6.5.1")).equals("No") && 
						((String)common.NB_excel_data_map.get("CD_Add_JCT6.5.1")).equals("Yes")){	
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to JCT 6.5.1 screen.");
					customAssert.assertTrue(JCTPage(common.NB_excel_data_map), "JCTPage function is having issue(S) . ");
					
				}
				if(((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("No") && 
						((String)common.NB_excel_data_map.get("CD_Add_Terrorism")).equals("Yes")){	
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Terrorism"),"Issue while Navigating to Terrorism screen.");
					customAssert.assertTrue(TerrorismPage(common.NB_excel_data_map), "TerrorismPage function is having issue(S) . ");
					
				}
				if(((String)common.NB_excel_data_map.get("CD_PropertyOwnersLiability")).equals("No") && 
						((String)common.NB_excel_data_map.get("CD_Add_PropertyOwnersLiability")).equals("Yes")){	
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Property Owners Liability"),"Issue while Navigating to Property Owners Liability screen.");
					customAssert.assertTrue(PropertyOwnersLiabilityPage(common.NB_excel_data_map), "PropertyOwnersLiabilityPage function is having issue(S) . ");
					
				}
				if(((String)common.NB_excel_data_map.get("CD_LegalExpenses")).equals("No") && 
						((String)common.NB_excel_data_map.get("CD_Add_LegalExpenses")).equals("Yes")){	
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
					customAssert.assertTrue(funcLegalExpenses(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Legal Expenses function is having issue(S) . ");
				}
				
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
					customAssert.assertTrue(common_VELA.funcPremiumSummary(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Premium Summary function is having issue(S) . ");
					//customAssert.assertTrue(common.funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
			}
			
		}catch(Throwable t){
			return false;
			
		}
		
		return r_value;
	}

public boolean COA_CancelPolicy(Map<Object, Object> map_data) throws ErrorInTestMethod{
    boolean retVal = true;
    int dateDif = Integer.parseInt((String)map_data.get("CP_AddDifference"));
    df = new SimpleDateFormat("dd/MM/yyyy");
    //String Cancellation_date = common.daysIncrementWithOutFormation(df.format(currentDate), dateDif);
    try{
    	
    		//UK time zone change
    		Date c_date = df.parse(common.getUKDate());
    		String Cancellation_date = common.daysIncrementWithOutFormation(df.format(c_date), dateDif);
    	   
           customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"), "Unable to navigate to Premium Summary screen");
           customAssert.assertTrue(common.funcButtonSelection("Cancel Policy"), "Unable to click on Cancel Policy Button");
                        
           customAssert.assertTrue(k.Click("COB_CP_CancellationDate"), "Unable to Click Cancellation date.");
           customAssert.assertTrue(k.Input("COB_CP_CancellationDate", Cancellation_date),"Unable to Enter Cancellation date.");
           customAssert.assertTrue(k.Click("SPI_Btn_Calender_Done"), "Unable to click on done button in calander.");
           customAssert.assertTrue(!k.getAttributeIsEmpty("COB_CP_CancellationDate", "value"),"COB_CP_CancellationDate Field Should Contain Valid Value on Cancel Policy page .");
           common.NB_excel_data_map.put("Cancellation_date", Cancellation_date);
           customAssert.assertTrue(k.Input("COB_CP_CancellationReason", (String)map_data.get("CP_CancellationReason")),"Unable to Enter Cancellation Reason.");
           customAssert.assertTrue(common.funcButtonSelection("Continue"), "Unable to click on Cancel Policy Button");
                        
           switch(dateDif){
			case 0:
					TestUtil.reportStatus("--------------- Inception cancellation effective from [<b> " + Cancellation_date+ " </b>] --------------- ", "Info", false);	
				break;
				
			default:
					TestUtil.reportStatus("--------------- Post inception cancellation effective from [<b> " + Cancellation_date+ " </b>] --------------- ", "Info", false);	
				break;
           }
          
           // Read Cancellation Return Premium Summary and put values to Map  :
           
           customAssert.assertTrue(COA_Cancel_RetrunPremiumTable(map_data),"Error while verifying cancelaltion retun premium table . ");
           
           customAssert.assertTrue(k.Click("COB_Btn_FinalCancelPolicy"), "Unable to click on Cancel Polcy button after verifying Cancellation Return Premium");
           customAssert.assertTrue(k.AcceptPopup(), "Unable to handl pop up");
                  
           return retVal;
           
    
    }catch(Throwable t){
          return false;
    }
           
}

public boolean COA_Cancel_RetrunPremiumTable(Map<Object, Object> map_data) throws ErrorInTestMethod{
	boolean retVal = true;
	String testName = (String)common.CAN_excel_data_map.get("Automation Key");
	
	try{
					
		int policy_Duration = Integer.parseInt(k.getText("COB_CAN_Duration"));
		int DaysRemain = Integer.parseInt(k.getText("COB_CAN_DaysRemain"));
		
		// Read Values From Cancellation return Premium Table and write to Map :
		
			String ReturnP_TablePath = "//p[text()=' Cancellation Return Premium Summary ']//following-sibling::table[@id='table0']";
			WebElement RetunP_Table = driver.findElement(By.xpath(ReturnP_TablePath));
			
			List<WebElement> cols = RetunP_Table.findElements(By.tagName("th"));
						
			int RetunP_Rows = RetunP_Table.findElements(By.tagName("tr")).size();
			int RetunP_Cols = cols.size();
			
			List<String> sectionNames = new ArrayList<>();
			String sectionName = null;
			String sectionValue = null;
			String headerName = null;
		
			if(RetunP_Table.isDisplayed()){
				
				TestUtil.reportStatus("Cancellation Return Premium Table exist on cancel policy page . ", "Info", true);
			
				//For Each Cover Row
				for(int row = 1; row < RetunP_Rows ;row ++){
					
					WebElement sec_Name = driver.findElement(By.xpath(ReturnP_TablePath+"//tbody//tr["+row+"]//td["+1+"]"));
					sectionName = sec_Name.getText();
										
					switch(sectionName){
					
							case "Totals":
									Map<String,Double> ReturnP_Table_TotalVal = new HashMap<>();
									//For Each Cols
									for(int col = 2; col <= RetunP_Cols ;col ++){
								
										////p[text()=' Transaction Premium']//following-sibling::table[@id='table0']//thead//th[2]
										WebElement header_Name = driver.findElement(By.xpath(ReturnP_TablePath+"//thead//th["+col+"]"));
										headerName = header_Name.getText();
									
										if(!headerName.contains("Pen Comm %") && !headerName.contains("Broker Comm %") && !headerName.contains("Gross Comm %")
												&& !headerName.contains("Insurance Tax Rate") ){
											WebElement sec_Val = driver.findElement(By.xpath(ReturnP_TablePath+"//tbody//tr["+row+"]//td["+col+"]"));
											sectionValue = sec_Val.getText();
											sectionValue = sectionValue.replaceAll(",", "");
											ReturnP_Table_TotalVal.put(headerName, Double.parseDouble(sectionValue));
											
										}else{
											continue;
										}
										common_COA.CAN_COA_ReturnP_Values_Map.put(sectionName, ReturnP_Table_TotalVal);
									}
								
									break;
						
							default:
								Map<String,Double> ReturnP_Table_CoverVal = new HashMap<>();
								WebElement sec_Val = driver.findElement(By.xpath(ReturnP_TablePath));
								//For Each Cols
								for(int col = 2; col <= RetunP_Cols ;col ++){
							
									////p[text()=' Transaction Premium']//following-sibling::table[@id='table0']//thead//th[2]
									WebElement header_Name = driver.findElement(By.xpath(ReturnP_TablePath+"//thead//th["+col+"]"));
									headerName = header_Name.getText();
								
									if(col == 2){
										sec_Val = driver.findElement(By.xpath(ReturnP_TablePath+"//tbody//tr["+row+"]//td["+col+"]/input"));
										sectionValue = sec_Val.getAttribute("value");
									}else{
										sec_Val = driver.findElement(By.xpath(ReturnP_TablePath+"//tbody//tr["+row+"]//td["+col+"]"));
										sectionValue = sec_Val.getText();
									}
																	
									ReturnP_Table_CoverVal.put(headerName, Double.parseDouble(sectionValue));
								}
								
								common_COA.CAN_COA_ReturnP_Values_Map.put(sectionName, ReturnP_Table_CoverVal);
								break;				
					}
					
				}
			}
					
			
			// Calculation and Comparison : 
				
				for(int row = 1; row < RetunP_Rows ;row ++){
					WebElement sec_Name = driver.findElement(By.xpath(ReturnP_TablePath+"//tbody//tr["+row+"]//td["+1+"]"));
					sectionNames.add(sec_Name.getText());
				}
				 
				for(String s_Name : sectionNames){
					if(s_Name.equals("Totals"))
						Can_returnP_Error = Can_returnP_Error + Can_ReturnP_Total_Validation(sectionNames,common_COA.CAN_COA_ReturnP_Values_Map);
					else
						Can_returnP_Error = Can_returnP_Error + CanReturnPTable_CoverSection_Validation(policy_Duration,DaysRemain, s_Name,common_COA.CAN_COA_ReturnP_Values_Map);								
				}
						
			    if(Can_returnP_Error == 0){
			    	retVal = true;
			    }else{
			    	retVal = false;
			    }
				
			return retVal;		
	
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}

public int Can_ReturnP_Total_Validation(List<String> sectionNames,Map<String,Map<String,Double>> Can_ReturnP_Values_Map){
	
	try{
	
			TestUtil.reportStatus("---------------Totals-----------------","Info",false);
			
			double exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_COA.CAN_COA_ReturnP_Values_Map.get(section).get("Net Net Premium");
			}
			
			String canRP_NetNetP_actual = Double.toString(common_COA.CAN_COA_ReturnP_Values_Map.get("Totals").get("Net Net Premium"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_NetNetP_actual)," Net Net Premium");

			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_COA.CAN_COA_ReturnP_Values_Map.get(section).get("Pen Comm");
			}
			String canRP_pc_actual = Double.toString(common_COA.CAN_COA_ReturnP_Values_Map.get("Totals").get("Pen Comm"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_pc_actual)," Pen Commission");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_COA.CAN_COA_ReturnP_Values_Map.get(section).get("Net Premium");
			}
			String canRP_netP_actual = Double.toString(common_COA.CAN_COA_ReturnP_Values_Map.get("Totals").get("Net Premium"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_netP_actual),"Net Premium");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_COA.CAN_COA_ReturnP_Values_Map.get(section).get("Broker Commission");
			}
			String canRP_bc_actual =  Double.toString(common_COA.CAN_COA_ReturnP_Values_Map.get("Totals").get("Broker Commission"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_bc_actual),"Broker Commission");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_COA.CAN_COA_ReturnP_Values_Map.get(section).get("Gross Premium");
			}
			String canRP_grossP_actual = Double.toString(common_COA.CAN_COA_ReturnP_Values_Map.get("Totals").get("Gross Premium"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_grossP_actual)," Gross Premium");
		
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_COA.CAN_COA_ReturnP_Values_Map.get(section).get("Insurance Tax");
			}
			String canRP_InsuranceTax_actual = Double.toString(common_COA.CAN_COA_ReturnP_Values_Map.get("Totals").get("Insurance Tax"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_InsuranceTax_actual),"Insurance Tax");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_COA.CAN_COA_ReturnP_Values_Map.get(section).get("Total Premium");
			}
			String canRP_p_actual = Double.toString(common_COA.CAN_COA_ReturnP_Values_Map.get("Totals").get("Total Premium"));
			double premium_diff = exp_value - Double.parseDouble(canRP_p_actual);
	
			if(premium_diff<0.05 && premium_diff>-0.05){
				TestUtil.reportStatus("Total Premium [<b> "+exp_value+" </b>] matches with actual total premium [<b> "+canRP_p_actual+" </b>]as expected for Totals in Cancellation Return Premium table .", "Pass", false);
				return 0;
				
			}else{
				TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+exp_value+"</b>] and Actual Premium [<b> "+canRP_p_actual+"</b>] for Totals in Cancellation Return Premium table . </p>", "Fail", true);
				return 1;
			}
	
	}catch(Throwable t) {
	    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	    TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
	    Assert.fail("Cancellation Return Premium Table -  total Section verification issue.  \n", t);
	    return 1;
	}
}

public int CanReturnPTable_CoverSection_Validation(int policy_Duration,int DaysRemain, String sectionNames, Map<String,Map<String,Double>> Can_ReturnP_Values_Map){

	Map<Object,Object> map_data = common.NB_excel_data_map;
	//String testName = (String)map_data.get("Automation Key");
	String code=null;
	Map<String,Double> temp_cancellation_map = new HashMap<>();
	code = sectionNames.replaceAll(" ", "");
		
	try{
			
			TestUtil.reportStatus("---------------"+sectionNames+"-----------------","Info",false);
			
			if(code.contains("LegalExpenses")){
				code = "LegalExpenses";
			}
			double annual_NetNetP = Double.parseDouble((String)map_data.get("PS_"+code+"_NetNetPremium"));
			String canRP_NetNetP_expected = Double.toString((annual_NetNetP/365)*DaysRemain);
			String canRP_NetNetP_actual = Double.toString(common_COA.CAN_COA_ReturnP_Values_Map.get(sectionNames).get("Net Net Premium"));
			CommonFunction.compareValues(Double.parseDouble(canRP_NetNetP_expected),Double.parseDouble(canRP_NetNetP_actual)," Net Net Premium");
			temp_cancellation_map.put("Net Net Premium", Double.parseDouble(canRP_NetNetP_expected));
			
			//COA CAN Transaction Pen commission Calculation : 
			double canRP_pen_comm = (( Double.parseDouble(canRP_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate"))/100)));
			String canRP_pc_expected = common.roundedOff(Double.toString(canRP_pen_comm));
			String canRP_pc_actual = Double.toString(common_COA.CAN_COA_ReturnP_Values_Map.get(sectionNames).get("Pen Comm"));
			CommonFunction.compareValues(Double.parseDouble(canRP_pc_expected),Double.parseDouble(canRP_pc_actual)," Pen Commission");
			temp_cancellation_map.put("Pen Comm", Double.parseDouble(canRP_pc_expected));
			
			//COA CAN Transaction Net Premium verification : 
			double canRP_netP = Double.parseDouble(canRP_pc_expected) + Double.parseDouble(canRP_NetNetP_expected);
			String canRP_netP_expected = common.roundedOff(Double.toString(canRP_netP));
			String canRP_netP_actual = Double.toString(common_COA.CAN_COA_ReturnP_Values_Map.get(sectionNames).get("Net Premium"));
			CommonFunction.compareValues(Double.parseDouble(canRP_netP_expected),Double.parseDouble(canRP_netP_actual),"Net Premium");
			temp_cancellation_map.put("Net Premium", Double.parseDouble(canRP_netP_expected));
			
			//COA CAN Transaction Broker commission Calculation : 
			double canRP_broker_comm = ((Double.parseDouble(canRP_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate"))/100)));
			String canRP_bc_expected = common.roundedOff(Double.toString(canRP_broker_comm));
			String canRP_bc_actual =  Double.toString(common_COA.CAN_COA_ReturnP_Values_Map.get(sectionNames).get("Broker Commission"));
			CommonFunction.compareValues(Double.parseDouble(canRP_bc_expected),Double.parseDouble(canRP_bc_actual),"Broker Commission");
			temp_cancellation_map.put("Broker Commission", Double.parseDouble(canRP_bc_expected));
			
			//COA CAN Transaction GrossPremium verification : 
			double canRP_grossP = Double.parseDouble(canRP_netP_expected) + Double.parseDouble(canRP_bc_expected);
			String canRP_grossP_actual = Double.toString(common_COA.CAN_COA_ReturnP_Values_Map.get(sectionNames).get("Gross Premium"));
			CommonFunction.compareValues(canRP_grossP,Double.parseDouble(canRP_grossP_actual),sectionNames+" Transaction Gross Premium");
			temp_cancellation_map.put("Gross Premium", canRP_grossP);
			
			double canRP_InsuranceTax = (canRP_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_IPT")))/100.0;
			canRP_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(canRP_InsuranceTax)));
			String canRP_InsuranceTax_actual = Double.toString(common_COA.CAN_COA_ReturnP_Values_Map.get(sectionNames).get("Insurance Tax"));
			CommonFunction.compareValues(canRP_InsuranceTax,Double.parseDouble(canRP_InsuranceTax_actual),"Insurance Tax");
			temp_cancellation_map.put("Insurance Tax", canRP_InsuranceTax);
			
						
			//COA CAN  Transaction Total Premium verification : 
			double canRP_Premium = canRP_grossP + canRP_InsuranceTax;
			String canRP_p_expected = common.roundedOff(Double.toString(canRP_Premium));
			
			String canRP_p_actual = Double.toString(common_COA.CAN_COA_ReturnP_Values_Map.get(sectionNames).get("Total Premium"));
			temp_cancellation_map.put("Total Premium", Double.parseDouble(canRP_p_expected));
			
			double premium_diff = Double.parseDouble(canRP_p_expected) - Double.parseDouble(canRP_p_actual);
			
			common_COA.CAN_COA_ReturnP_Values_Map.put(sectionNames, temp_cancellation_map);
			
			if(premium_diff<0.10 && premium_diff>-0.10){
				TestUtil.reportStatus("Total Premium [<b> "+canRP_p_expected+" </b>] matches with actual total premium [<b> "+canRP_p_actual+" </b>]as expected for "+sectionNames+" in Cancellation Return Premium table .", "Pass", false);
				return 0;
				
			}else{
				TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+canRP_p_expected+"</b>] and Actual Premium [<b> "+canRP_p_actual+"</b>] for "+sectionNames+" in Cancellation Return Premium table . </p>", "Fail", true);
				return 1;
			}
				
	}catch(Throwable t) {
	    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	    TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
	    Assert.fail("Transaction Premium verification issue.  \n", t);
	    return 1;
	}
		
}

public boolean COA_Cancel_PremiumSummary(Map<Object, Object> map_data) throws ErrorInTestMethod{
	boolean retVal = true;
	String testName = (String)common.CAN_excel_data_map.get("Automation Key");
	double s_PenCommRate = 0.00, s_BrokerCommRate = 0.00, s_GrossCommRate = 0.00, s_InsTRate = 0.00;
	
	try{
	
		customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"), "Unable to navigate to Premium Summary screen");
		
		String Cancellation_Date = "//p[text()=' Cancellation Details']//following-sibling::p";
		WebElement Cancel_Date = driver.findElement(By.xpath(Cancellation_Date));
		String C_Date = Cancel_Date.getText();
		
		// Verification of cancellation date :
		
				
		// Read Values From Cancellation Premium Summary Table :
		
			String CancelP_TablePath = "//p[text()=' Cancellation Details']//following-sibling::p//following-sibling::table[@id='table0']";
			WebElement CancelP_Table = driver.findElement(By.xpath(CancelP_TablePath));
			
			List<WebElement> cols = CancelP_Table.findElements(By.tagName("th"));
				
			int CancelP_Rows = CancelP_Table.findElements(By.tagName("tr")).size();
			int CancelP_Cols = cols.size();
			
			List<String> sectionNames = new ArrayList<>();
			String sectionName = null;
			String sectionValue = null;
			String headerName = null;
		
			if(CancelP_Table.isDisplayed()){
				
				TestUtil.reportStatus("Cancellation Premium Summary Table exist on premium summary page . ", "Info", true);
			
				//For Each Cover Row
				for(int row = 1; row < CancelP_Rows ;row ++){
					
					WebElement sec_Name = driver.findElement(By.xpath(CancelP_TablePath+"//tbody//tr["+row+"]//td["+1+"]"));
					sectionName = sec_Name.getText();
					
					double s_NetNetP = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+2+"]").replaceAll(",", "")));
					
					if(!sectionName.equals("Totals")){
						s_PenCommRate = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+3+"]").replaceAll(",", "")));
					}
													
					double s_PenCommAmt = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+4+"]").replaceAll(",", "")));
					
					double s_NetP = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+5+"]").replaceAll(",", "")));
					
					if(!sectionName.equals("Totals")){
						s_BrokerCommRate = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+6+"]").replaceAll(",", "")));						
					}
					double s_BrokerCommAmt = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+7+"]").replaceAll(",", "")));
					
					if(!sectionName.equals("Totals")){
						s_GrossCommRate = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+8+"]").replaceAll(",", "")));
					}					
					double s_GrossCommAmt = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+9+"]").replaceAll(",", "")));
					
					if(!sectionName.equals("Totals")){
						s_InsTRate = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+10+"]").replaceAll(",", "")));
					}					
					double s_InsTAmt = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+11+"]").replaceAll(",", "")));
					double s_TotalP = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+12+"]").replaceAll(",", "")));
					
					CommonFunction.compareValues(s_NetNetP, common_COA.CAN_COA_ReturnP_Values_Map.get(sectionName).get("Net Net Premium"), "Cancellation Net Net Premium");
					CommonFunction.compareValues(s_PenCommAmt, common_COA.CAN_COA_ReturnP_Values_Map.get(sectionName).get("Pen Comm"), "Cancellation Pen commm Amount");
					CommonFunction.compareValues(s_NetP, common_COA.CAN_COA_ReturnP_Values_Map.get(sectionName).get("Net Premium"), "Cancellation Net Premium");
					CommonFunction.compareValues(s_BrokerCommAmt, common_COA.CAN_COA_ReturnP_Values_Map.get(sectionName).get("Broker Commission"), "Cancellation Broker commission amount");
					CommonFunction.compareValues(s_GrossCommAmt, common_COA.CAN_COA_ReturnP_Values_Map.get(sectionName).get("Gross Premium"), "Cancellation Gross Premium");
					CommonFunction.compareValues(s_InsTAmt, common_COA.CAN_COA_ReturnP_Values_Map.get(sectionName).get("Insurance Tax"), "Cancellation Ins tax Premium");
					CommonFunction.compareValues(s_TotalP, common_COA.CAN_COA_ReturnP_Values_Map.get(sectionName).get("Total Premium"), "Cancellation Total Premium");
										
				}	
			}
				
			return retVal;		
	
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}

public boolean funcPolicyGeneral_MTA(Map<Object, Object> map_data, String code, String event) {
	boolean retVal = true;
	String testName = "";
	try{
		common.currentRunningFlow = "MTA";
	
		//customAssert.assertTrue(common.funcMenuSelection("Navigate", "Policy General"), "Unable to navigate to policy general screen");
		customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");
		
		customAssert.assertTrue(k.Input("COB_PG_BusinessEstYear", (String)map_data.get("PG_YearEstablished")),	"Unable to enter value in Business Instablished year  field .");
		customAssert.assertTrue(k.Input("COB_PG_TurnOver", (String)map_data.get("PG_TurnOver")),	"Unable to enter value in Turnover field .");
		
		String[] geographical_Limits = ((String)common.MTA_excel_data_map.get("PG_GeoLimit")).split(",");
		List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
		for(String geo_limit : geographical_Limits){
			for(WebElement each_ul : ul_elements){
				customAssert.assertTrue(k.Click("COB_PG_GeoLimit"),"Error while Clicking Geographic Limit List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		
		String[] Prof_Bodies = ((String)common.MTA_excel_data_map.get("PG_InsuredDetails_Q1")).split(",");
		List<WebElement> ul_ele = driver.findElements(By.xpath("//ul"));
		for(String pBodies_limit : Prof_Bodies){
			for(WebElement each_ul : ul_ele){
				customAssert.assertTrue(k.Click("COB_PG_InsuredDetails_Q1"),"Error while Clicking Professional Bodies List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+pBodies_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+pBodies_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		
		
		customAssert.assertTrue(k.DropDownSelection("COB_PG_QuoteValidity", (String)map_data.get("PG_QuoteValidity")),"Unable to select Auote Validity value");
		customAssert.assertTrue(k.Input("COB_PG_BusDesc", (String)map_data.get("PG_BusDesc")),	"Unable to enter value in Provided Details field .");
		
		customAssert.assertTrue(endorsementVerificationOnPolicyGeneral(map_data),"Endorsement Verification function is causing issue on Policy General screen.");
		
		TestUtil.reportStatus("Entered all the details on Policy General page .", "Info", true);
		
		return retVal;
	
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Unable to to do operation on policy General page. \n", t);
        return false;
	}
	
}


public boolean endorsementVerificationOnPolicyGeneral(Map<Object, Object> map_data){
	
	
	try{
		
		String exp_effectiveDate = (String)map_data.get("MTA_EffectiveDate");
		String act_effectiveDate = k.getText("PG_EffectiveDate");
		String exp_endorReason = (String)map_data.get("MTA_Reason_for_Endorsement");
		String act_endorReason = k.getText("PG_endorReason");
				
		customAssert.assertEquals(act_effectiveDate,exp_effectiveDate,"Unable to compare effective date for created endorsement on policy general screen.");
		customAssert.assertEquals(act_endorReason,exp_endorReason,"Unable to compare endorsement reason for created endorsement on policy general screen.");
		
		
	}catch(Throwable t){
		TestUtil.reportStatus("Endorsement details verification is causing issue on Policy general screen.", "Fail", true);
		return false;
	}
	
	return true;
	
	
}


public boolean funcRewindOperation_MTA(){
	
	boolean r_value= true;
	String navigationBy = CONFIG.getProperty("NavigationBy");
	MTARewindFlow = "Yes";
	try{
		
		if(((String)common.MTA_excel_data_map.get("CD_Add_Remove_Cover")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common_VELA.funcRewindCoversCheck(common.MTA_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts and Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			customAssert.assertTrue(func_Referrals_MaterialFactsDeclerationPage(), "Referrals for MaterialFactsDeclerationPage function is having issue(S) . ");
			
			if(((String)common.MTA_excel_data_map.get("CD_Public&ProductsLiability")).equals("No") &&
					((String)common.NB_excel_data_map.get("CD_Add_Public&ProductsLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen.");
				customAssert.assertTrue(PublicProductsLiabilityPage(common.MTA_excel_data_map), "PublicProductsLiabilityPage function is having issue(S) . ");
			}
			if(((String)common.MTA_excel_data_map.get("CD_EmployersLiability")).equals("No") &&	
				((String)common.MTA_excel_data_map.get("CD_Add_EmployersLiability")).equals("Yes")){
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to Employers Liability screen.");
				customAssert.assertTrue(EmployersLiabilityPage(common.MTA_excel_data_map), "EmployersLiabilityPage function is having issue(S) . ");
			}
			if(((String)common.MTA_excel_data_map.get("CD_AnnualWorks")).equals("No") && 
					((String)common.MTA_excel_data_map.get("CD_Add_AnnualWorks")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Annual Works"),"Issue while Navigating to Annual Works screen.");
				customAssert.assertTrue(AnnualWorkPage(common.MTA_excel_data_map), "AnnualWorkPage function is having issue(S) . ");
			}
			if(((String)common.MTA_excel_data_map.get("CD_SingleProject")).equals("No") && 
					((String)common.MTA_excel_data_map.get("CD_Add_SingleProject")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Single Project"),"Issue while Navigating to Single Project screen.");
				customAssert.assertTrue(SingleProjectPage(common.MTA_excel_data_map), "SingleProjectPage function is having issue(S) . ");
			}
			if(((String)common.MTA_excel_data_map.get("CD_OwnPlant")).equals("No") && 
					((String)common.MTA_excel_data_map.get("CD_Add_OwnPlant")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Own Plant"),"Issue while Navigating to Own Plant screen . ");
				customAssert.assertTrue(OwnPlantsPage(common.MTA_excel_data_map), "OwnPlantsPage function is having issue(S) . ");
				
			}
			if(((String)common.MTA_excel_data_map.get("CD_HiredInPlant")).equals("No") && 
					((String)common.MTA_excel_data_map.get("CD_Add_HiredInPlant")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Hired In Plant"),"Issue while Navigating to Employers Liability screen.");
				customAssert.assertTrue(HireInPlantPage(common.MTA_excel_data_map), "HireInPlantPage function is having issue(S) . ");
			}
			
			if(((String)common.MTA_excel_data_map.get("CD_JCT6.5.1")).equals("No") && 
					((String)common.MTA_excel_data_map.get("CD_Add_JCT6.5.1")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to JCT 6.5.1 screen.");
				customAssert.assertTrue(JCTPage(common.MTA_excel_data_map), "JCTPage function is having issue(S) . ");
				
			}
			if(((String)common.MTA_excel_data_map.get("CD_Terrorism")).equals("No") && 
					((String)common.MTA_excel_data_map.get("CD_Add_Terrorism")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Terrorism"),"Issue while Navigating to Terrorism screen.");
				customAssert.assertTrue(TerrorismPage(common.MTA_excel_data_map), "TerrorismPage function is having issue(S) . ");
				
			}
			if(((String)common.MTA_excel_data_map.get("CD_PropertyOwnersLiability")).equals("No") && 
					((String)common.MTA_excel_data_map.get("CD_Add_PropertyOwnersLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Property Owners Liability"),"Issue while Navigating to Property Owners Liability screen.");
				customAssert.assertTrue(PropertyOwnersLiabilityPage(common.MTA_excel_data_map), "PropertyOwnersLiabilityPage function is having issue(S) . ");
				
			}
			if(((String)common.MTA_excel_data_map.get("CD_LegalExpenses")).equals("No") && 
					((String)common.MTA_excel_data_map.get("CD_Add_LegalExpenses")).equals("Yes")){	
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
				customAssert.assertTrue(funcLegalExpenses(common.MTA_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Legal Expenses function is having issue(S) . ");
			}
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
			customAssert.assertTrue(common.funcEndorsementOperations(common.MTA_excel_data_map),"Endorsement function(funcEndorsementOperations) is having issue(S).");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_VELA.funcPremiumSummary(common.MTA_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Premium Summary function is having issue(S) . ");
			
		}
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
}

public boolean funcCovers(Map<Object, Object> map_data){
	
	boolean retvalue = true;
	common_VELA.CoversDetails_data_list = new ArrayList<>();
    try {
	     customAssert.assertTrue(common.funcPageNavigation("Covers", ""),"Cover page is having issue(S)");
	 	 k.pressDownKeyonPage();
	 	 String all_cover = ObjectMap.properties.getProperty(CommonFunction.product+"_CD_AllCovers");
	 	 String[] split_all_covers = all_cover.split(",");
	 	 for(String coverWithLocator : split_all_covers){
	 		 String coverWithoutLocator = coverWithLocator.split("__")[0];
	 		 try{
	 		if(((String)map_data.get("CD_"+coverWithoutLocator)).equals("Yes")){
	 			if(!(coverWithoutLocator.equalsIgnoreCase("ProductsLiability") || 
	 					coverWithoutLocator.equalsIgnoreCase("PollutionLiability(suddenandunforeseen)") || 
	 					coverWithoutLocator.equalsIgnoreCase("Computer") || 
	 					coverWithoutLocator.equalsIgnoreCase("DeteriorationofStock"))){
	 				
	 				
	 				common_VELA.CoversDetails_data_list.add(coverWithoutLocator);
	 				
	 			}
	 			
	 			customAssert.assertTrue(selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
	 		}else{
	 			customAssert.assertTrue(deSelectCovers(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
	 		}
	 		 }catch(Throwable tt){
	 			 System.out.println("Error while selecting Cover - "+coverWithoutLocator);
	 			 break;
	 			}
	 		}
	 	 	
	 	  customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Covers Screen .");
	 	  
	 	  TestUtil.reportStatus("All specified covers are selected successfully  .", "Info", true);
	      return retvalue;
           
    } catch(Throwable t) {
      String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     
        k.reportErr("Failed in "+methodName+" function", t);
        Assert.fail("Unable to select specified covers . ", t);
        return false;
 }
    }

/**
 * This method selects the specified covers from cover Page
 */
public boolean selectCover(String coverNameWithLocator,Map<Object, Object> map_data){
	 
	 boolean result=true;
	 String c_locator = null;
	 String coverName = null;
	 String agency_Name = null;
	try{
			coverName = coverNameWithLocator.split("__")[0];	
			c_locator = coverNameWithLocator.split("__")[1];
			
				k.waitTwoSeconds();
				if (!driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).isSelected()){
					k.scrollInViewByXpath("//*[contains(@name,'"+c_locator+"_selected')]");
					driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).click();        
					TestUtil.reportStatus("Cover: <b>"+coverName+"</b> is selected ", "Info", false);
					/*if(common.currentRunningFlow.equalsIgnoreCase("MTA")){
						common.CoversDetails_data_list.add(coverName); //For Flat Premium
					}*/
				}else{
					TestUtil.reportStatus("Cover: <b>"+coverName+"</b> is checked by default.", "Info", false);
				}
			
		
			
	 
	}catch(Throwable t){
		
		System.out.println("Error while selecting Cover - "+t.getMessage());
		result=false;
	}
	return result;
 }

public boolean deSelectCovers(String coverNameWithLocator,Map<Object, Object> map_data){
	 boolean result=true;
	 String c_locator = null;
	 String coverName = null;
	 try{
			coverName = coverNameWithLocator.split("__")[0];	
			c_locator = coverNameWithLocator.split("__")[1];
			
				k.waitTwoSeconds();
				if(c_locator.equalsIgnoreCase("cob_hcp")){
					
				}else{
					if (driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).isSelected()){
						k.scrollInViewByXpath("//*[contains(@name,'"+c_locator+"_selected')]");
						driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).click();        
						k.AcceptPopup();
						TestUtil.reportStatus("Cover: <b>"+coverName+"</b> is unchecked ", "Info", false);
					}else{
						TestUtil.reportStatus("Cover: <b>"+coverName+"</b> is checked by default.", "Info", false);
					}
				}
				
			                    
			return result;
	           
	    } catch(Throwable t) {
	    	return false;
	}
} 

/**
 * 
 * New implementation of Insurance tax for COA MTA : 
 * 
 * 
 */

/*
 * Insurance tax adjustment handling
 * 
 */

@SuppressWarnings("static-access")
public boolean insuranceTaxAdjustmentHandling(String code , String event){
 	Map<Object, Object> map_to_update=common.NB_excel_data_map;
 	totalGrossPremium = 0.0;
	totalGrossTax = 0.0;
	totalNetPremiumTax = 0.0;
	common_HHAZ.totalGrossPremium = 0.00;
	common_HHAZ.totalGrossTax = 0.00;
	try {
		switch(TestBase.businessEvent){
		case "NB":
			map_to_update = common.NB_excel_data_map;
		break;
		case "Rewind":
			if(common.currentRunningFlow.equals("NB")){
				map_to_update = common.NB_excel_data_map;
				event = "NB";
				}
			else{
				map_to_update = common.Rewind_excel_data_map;
				event = "Rewind";	
			}
		break;
		case "Requote":
			if(common.currentRunningFlow.equals("NB")){
				map_to_update = common.NB_excel_data_map;
				event = "NB";
				}
			else{
				map_to_update = common.Requote_excel_data_map;
				event = "Rewind";	
			}
		break;
		case "Renewal":
			if(CommonFunction.businessEvent.equalsIgnoreCase("Renewal") && common.currentRunningFlow.equalsIgnoreCase("MTA")){
				map_to_update = common.MTA_excel_data_map;
			}else if(CommonFunction.businessEvent.equalsIgnoreCase("Renewal") && common.currentRunningFlow.equalsIgnoreCase("Rewind")){
				map_to_update = common.Rewind_excel_data_map;
			}else{
				map_to_update = common.Renewal_excel_data_map;
			}
			break;
		case "MTA":
			if(common.currentRunningFlow.equals("NB")){
				map_to_update = common.NB_excel_data_map;
				event = "NB";
				}else if(CommonFunction.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind")){
					map_to_update = common.Rewind_excel_data_map;
				}
			else{
				map_to_update = common.MTA_excel_data_map;
				event = "MTA";	
			}
			
		break;
	}
		customAssert.assertTrue(k.Click("insuranceTxButon"),"Unable to click on Tax insurance button.");
		customAssert.assertTrue(common.funcPageNavigation("Tax Adjustment", ""),"Unable to land on Tax adjustment screen.");
		String sectionName;
		
		if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
			try{
				if(common.currentRunningFlow.equalsIgnoreCase("Renewal")){
					common_HHAZ.deleteItems();
				}else{
					common_HHAZ.verifyAdjustedTaxOnBusinessEvent(map_to_update);
				}
			}catch(Throwable t){
				
			}
		}
		
		if(!common.currentRunningFlow.equalsIgnoreCase("MTA") && !(common.currentRunningFlow.equalsIgnoreCase("Rewind") && TestBase.businessEvent.equalsIgnoreCase("MTA"))){
			customAssert.assertTrue(verifyCoverDetails(),"Cover Details verification is causing issue(S).");
			customAssert.assertTrue(verifyGrossPremiumValues(),"Gross Premium verification is causing issue(S).");
		}
		
		if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
			customAssert.assertTrue(common_HHAZ.deleteItems(),"Unable to delete present tax ajustment on Tax adjustment screen");
		}
		
		String policy_status_actual = k.getText("Policy_status_header");
		
		switch ((String)map_to_update.get("PS_InsuranceTaxButton")) {
		case "Yes":
			String value = null;
			List<WebElement> list = k.findElements("insuranceTaxExemptionRadioButton");
			for(int i=0;i<list.size();i++){
				
				boolean selectedStatus =  list.get(i).isSelected();
				if(selectedStatus){
					value = list.get(i).getAttribute("Value");	
				}
			}
			
			
			if(((String)map_to_update.get("PS_TaxExempt")).equalsIgnoreCase("No") && value.equalsIgnoreCase("No")){
				customAssert.assertTrue(k.SelectRadioBtn("insuranceTaxExemptionRadioButton", (String)map_to_update.get("PS_InsuranceTaxButton")));
				k.waitTwoSeconds();
				customAssert.assertTrue(k.AcceptPopup(), "Unable to accept alert box.");
			}
			
			taxTable_tBody = k.getObject("inssuranceTaxMainTableBODY"); 
			List<WebElement> listOfCovers = taxTable_tBody.findElements(By.tagName("tr"));
			countOfCovers = listOfCovers.size();
			
			for(int j=0;j<countOfCovers-1;j++){
				
				sectionName = taxTable_tBody.findElement(By.xpath("tr["+(j+1)+"]/td[1]")).getText().replaceAll(" ", "");
				if(sectionName.equalsIgnoreCase("PersonalAccidentStandard")){
					sectionName = "PersonalAccident";
				}else if(sectionName.equalsIgnoreCase("GoodsInTransit")){
					sectionName = "GoodsinTransit";
				}
				if(sectionName.contains("AbbeyLegalExpenses")){
					sectionName = "LegalExpenses";
				}
				if(sectionName.contains("AnnualContractWorks")){
					sectionName = "AnnualWorks";
				}
				if(sectionName.contains("SingleProjectContractWorks")){
					sectionName = "SingleProject";
				}
				TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_to_update.get("Automation Key"), "PS_"+sectionName+"_GT", common.roundedOff("00.00"), map_to_update);
				TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_to_update.get("Automation Key"), "PS_"+sectionName+"_IPT", common.roundedOff("00.00"), map_to_update);
				//TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_to_update.get("Automation Key"), "PS_"+sectionName+"_NPIPT", common.roundedOff("00.00"), map_to_update);
				totalGrossPremium = totalGrossPremium + Double.parseDouble(common.roundedOff((String)map_to_update.get("PS_"+sectionName+"_GP")));
			}
			
			TestUtil.reportStatus("<b> Tax adjustment operatios is completed. </b>", "Info", false);
			
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_to_update.get("Automation Key"), "PS_Total_GP", common.roundedOff(Double.toString(totalGrossPremium)), map_to_update);
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_to_update.get("Automation Key"), "PS_Total_GT", common.roundedOff("00.00"), map_to_update);
			//TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_to_update.get("Automation Key"), "PS_Total_NPIPT", common.roundedOff("00.00"), map_to_update);
			TestUtil.reportStatus("<b>Policy Exempt from insurance tax radio button is selected as 'Yes' Hence skipped adjustment operation for all covers.</b>", "Info", false);
			break;
			
		case "No":
			
			TestUtil.reportStatus("<b> Tax adjustment operatios is started. </b>", "Info", false);
			customAssert.assertTrue(k.SelectRadioBtn("insuranceTaxExemptionRadioButton", (String)map_to_update.get("PS_InsuranceTaxButton")));
			taxTable_tBody = k.getObject("inssuranceTaxMainTableBODY"); 
			List<WebElement> list2 = taxTable_tBody.findElements(By.tagName("tr"));
			countOfCovers = list2.size();
			
			
			for(int j=0;j<countOfCovers-1;j++){
				
				taxTable_tBody = k.getObject("inssuranceTaxMainTableBODY");
				sectionName = taxTable_tBody.findElement(By.xpath("tr["+(j+1)+"]/td[1]")).getText();
				
				String grossPremium =  taxTable_tBody.findElement(By.xpath("tr["+(j+1)+"]/td[2]")).getText();
				
				if(sectionName.equalsIgnoreCase("") || sectionName==null || sectionName.equalsIgnoreCase("Totals")){
					continue;
				}else{
					
					customAssert.assertTrue(adjustInsuranceTax(grossPremium,sectionName,code,event),"Adjust insurance Tax function is causing issue(S).");
					customAssert.assertTrue(verifyAdjustedTaxValues(sectionName,code,event),"Verify adjusted Tax function is having issue(S).");
				}
					
				taxTable_tBody = k.getObject("inssuranceTaxMainTableBODY");
				List<WebElement> list3 = taxTable_tBody.findElements(By.tagName("tr"));
				countOfCovers = list3.size();
			}
			String actualTotalGP = taxTable_tBody.findElement(By.xpath("tr["+(countOfCovers)+"]/td[2]")).getText();
			String actualTotalGT = taxTable_tBody.findElement(By.xpath("tr["+(countOfCovers)+"]/td[5]")).getText();
				
			customAssert.assertTrue(common.compareValues(common_HHAZ.totalGrossPremium, Double.parseDouble(actualTotalGP), "Total Gross Premium from Insuracnce Tax screen"), "Unable to compare total gross premium on Tax adjustment screen.");
			customAssert.assertTrue(common.compareValues(common_HHAZ.totalGrossTax, Double.parseDouble(actualTotalGT), "Total Gross Tax from Insuracnce Tax screen"), "Unable to compare total gross tax on Tax adjustment screen.");
				
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_to_update.get("Automation Key"), "PS_Total_GP", common.roundedOff(Double.toString(common_HHAZ.totalGrossPremium)), map_to_update);
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_to_update.get("Automation Key"), "PS_Total_GT", common.roundedOff(Double.toString(common_HHAZ.totalGrossTax)), map_to_update);
			
			TestUtil.reportStatus("<b> Tax adjustment operatios is completed. </b>", "Info", false);
			break;
		
		default:
			break;
		}
		isInsuranceTaxDone = true;
		common.funcPageNavigation("", "Save");
		k.Click("Tax_adj_BackBtn");
			
		return true;
	}catch (Throwable t) {
		k.ImplicitWaitOn();
		return false;
	}
}

//This function will verify values from system with data sheet data.
@SuppressWarnings("static-access")
public static boolean verifyCoverDetails(){
	
	try {
		
		int count = 0 , count_datasheet = 0;
		String coverName = null , coverName_datasheet = null;
		try{
			k.ImplicitWaitOff();
			k.Click("insuranceTaxAddAdjustmentButton");
		}catch(Exception e){
			k.Click("insuranceTaxAddAdjustmentButton_1");
		}finally {
			k.ImplicitWaitOn();
		}
		
		List<WebElement> names = driver.findElements(By.tagName("option"));
		List<String> coversNameList = new ArrayList<>();
		String policy_status_actual = k.getText("Policy_status_header");
		String coverName_withoutSpace =null;
		
		for(int i=0;i<names.size();i++){
			coverName = names.get(i).getText();
			
			coverName_withoutSpace = coverName.replaceAll(" ", "");
			
			coversNameList.add(coverName_withoutSpace);
			if(common.currentRunningFlow.equalsIgnoreCase("NB")){
			
			String key = "CD_"+coverName_withoutSpace;
			if((policy_status_actual).contains("Rewind")){
				key = "CD_Add_"+coverName_withoutSpace;
			}
			
			if(common.NB_excel_data_map.get(key).toString().equalsIgnoreCase("Yes")){
				continue;
			}else{
				if(common.NB_excel_data_map.get("CD_"+coverName_withoutSpace.replaceAll(" ", "")).toString().equalsIgnoreCase("Yes")){
					
				}else{
					TestUtil.reportStatus("Cover Name <b>  ["+coverName+"]  </b> should not present in the dropdown list as This cover is not selected on Cover Details page.", "FAIL", false);
					count++;
				}
				
			}
			}else if(common.currentRunningFlow.equalsIgnoreCase("MTA")){
			String key = "CD_Add_"+coverName_withoutSpace;
							
			if(common.MTA_excel_data_map.get(key).toString().equalsIgnoreCase("Yes")){
				continue;
			}else{
				if(common.MTA_excel_data_map.get("CD_Add_"+coverName_withoutSpace.replaceAll(" ", "")).toString().equalsIgnoreCase("Yes")){
					
				}else{
					TestUtil.reportStatus("Cover Name <b>  ["+coverName+"]  </b> should not present in the dropdown list as This cover is not selected on Cover Details page.", "FAIL", false);
					count++;
				}
				
			}
		}
		else if(common.currentRunningFlow.equalsIgnoreCase("Renewal")){
			String key = "CD_"+coverName_withoutSpace;
							
			if(common.Renewal_excel_data_map.get(key).toString().equalsIgnoreCase("Yes")){
				continue;
			}else{
				if(common.Renewal_excel_data_map.get("CD_"+coverName_withoutSpace.replaceAll(" ", "")).toString().equalsIgnoreCase("Yes")){
					
				}else{
					TestUtil.reportStatus("Cover Name <b>  ["+coverName+"]  </b> should not present in the dropdown list as This cover is not selected on Cover Details page.", "FAIL", false);
					count++;
				}
				
			}
		}
		else if(common.currentRunningFlow.equalsIgnoreCase("Rewind")){
			if(coverName_withoutSpace.equalsIgnoreCase("AnnualContractWorks")){
				coverName_withoutSpace = "AnnualWorks";
			}
			String key = "CD_"+coverName_withoutSpace;
							
			if(common.Rewind_excel_data_map.get(key).toString().equalsIgnoreCase("Yes")){
				continue;
			}else{
				if(common.Rewind_excel_data_map.get("CD_"+coverName_withoutSpace.replaceAll(" ", "")).toString().equalsIgnoreCase("Yes")){
					
				}else{
					TestUtil.reportStatus("Cover Name <b>  ["+coverName+"]  </b> should not present in the dropdown list as This cover is not selected on Cover Details page.", "FAIL", false);
					count++;
				}
				
			}
		}
		else if(common.currentRunningFlow.equalsIgnoreCase("Requote")){
			String key = "CD_"+coverName_withoutSpace;
							
			if(common.Requote_excel_data_map.get(key).toString().equalsIgnoreCase("Yes")){
				continue;
			}else{
				if(common.Requote_excel_data_map.get("CD_"+coverName_withoutSpace.replaceAll(" ", "")).toString().equalsIgnoreCase("Yes")){
					
				}else{
					TestUtil.reportStatus("Cover Name <b>  ["+coverName+"]  </b> should not present in the dropdown list as This cover is not selected on Cover Details page.", "FAIL", false);
					count++;
				}
				
			}
		}
	}
		for(int p=0;p<common_VELA.CoversDetails_data_list.size();p++){
			coverName_datasheet = common_VELA.CoversDetails_data_list.get(p);
			
			if(coversNameList.contains(coverName_datasheet)){
				continue;
			}else{
				TestUtil.reportStatus("Cover Name <b>  ["+coverName_datasheet+"]  </b> is selected as 'NO' in datasheet but still listed in the dropdown list.", "FAIL", false);
				count_datasheet++;
			}
		}
		
		WebElement adjustmentTax = k.getObject("insuranceTaxAddAdjustmentTable");
		customAssert.assertTrue(k.SelectBtnWebElement(adjustmentTax, "insuranceTaxAddAdjustmentSaveCancleButton", "Cancel"), "Unable to select Cancel button.");
		
		if(count==0 && count_datasheet==0){
			TestUtil.reportStatus("<b> Verified covers present in dropdown list of Adjustment Tax table. </b>", "Info", false);
		}
		
		return true;
	
	}catch (Throwable t) {
		
		return false;
	}
}

//This function will verify Gross premium values from system with datasheet data.
@SuppressWarnings("static-access")
public static boolean verifyGrossPremiumValues(){
	
	try {
		
		int count = 0;
		//taxTable_tHead = k.getObject("inssuranceTaxMainTableHEAD");
		taxTable_tBody = k.getObject("inssuranceTaxMainTableBODY"); 
		List<WebElement> coverNameList = taxTable_tBody.findElements(By.tagName("tr"));
		String policy_status_actual = k.getText("Policy_status_header");
		// Below code will check the covers from the table.
		String coverName_withoutSpace = null;
		Map<Object, Object> map_to_update=common.NB_excel_data_map;
		switch(TestBase.businessEvent){
		case "NB":
			map_to_update = common.NB_excel_data_map;
		break;
		case "Rewind":
			if(common.currentRunningFlow.equals("NB")){
				map_to_update = common.NB_excel_data_map;
			}
			else{
				map_to_update = common.Rewind_excel_data_map;					
			}
		break;
		case "Renewal":
			if(common.currentRunningFlow.equals("MTA")){
				map_to_update = common.MTA_excel_data_map;
			}else if(common.currentRunningFlow.equals("Rewind")){
				map_to_update = common.Rewind_excel_data_map;
			}
			else{
				map_to_update = common.Renewal_excel_data_map;					
			}
		break;
		case "Requote":
			if(common.currentRunningFlow.equals("NB")){
				map_to_update = common.NB_excel_data_map;
			}
			else{
				map_to_update = common.Requote_excel_data_map;					
			}
		break;
		case "MTA":
			if(common.currentRunningFlow.equals("NB")){
				map_to_update = common.NB_excel_data_map;
			}else if(CommonFunction.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind")){
				map_to_update = common.Rewind_excel_data_map;
			}
			else{
				map_to_update = common.MTA_excel_data_map;					
			}
			
		break;
	}
		for(int i=0;i<coverNameList.size()-1;i++){
			
			String sectionName = taxTable_tBody.findElement(By.xpath("tr["+(i+1)+"]/td[1]")).getText();
			
			if(!(sectionName.equalsIgnoreCase(""))){
				
				
				coverName_withoutSpace = sectionName.replaceAll(" ", "");
				
				String key = "CD_"+coverName_withoutSpace;
				String expectedIPTRate;
				if(((String)map_to_update.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
					expectedIPTRate = "0.0";
				}else{
					expectedIPTRate = (String)map_to_update.get("PS_IPTRate");
				}
				
				
				
				if(common.currentRunningFlow.equalsIgnoreCase("Rewind")){
					if(sectionName.contains("Personal Accident Standard")){
						coverName_withoutSpace = "PersonalAccidentStandard";
					}
					if(sectionName.contains("Goods In Transit")){
						coverName_withoutSpace = "GoodsInTransit";
					}
					if(sectionName.contains("Annual Contract Works")){
						sectionName = "AnnualWorks";
					}
					key = "CD_"+coverName_withoutSpace;
					if(sectionName.contains("Personal Accident Standard")){
						coverName_withoutSpace = "PersonalAccident";
					}
					if(sectionName.contains("Goods In Transit")){
						coverName_withoutSpace = "GoodsinTransit";
					}
					if(sectionName.contains("Annual Contract Works")){
						sectionName = "AnnualWorks";
					}
					if(sectionName.contains("Single Project Contract Works")){
						sectionName = "SingleProject";
					}
					expectedIPTRate = (String)common.Rewind_excel_data_map.get("PS_"+coverName_withoutSpace+"_IPT");
					
				}
							
				if(common.currentRunningFlow.equalsIgnoreCase("Requote")){
					if(sectionName.contains("Personal Accident Standard")){
						coverName_withoutSpace = "PersonalAccidentStandard";
					}
					if(sectionName.contains("Goods In Transit")){
						coverName_withoutSpace = "GoodsInTransit";
					}
					key = "CD_"+coverName_withoutSpace;
					if(sectionName.contains("Personal Accident Standard")){
						coverName_withoutSpace = "PersonalAccident";
					}
					if(sectionName.contains("Goods In Transit")){
						coverName_withoutSpace = "GoodsinTransit";
					}
					if(sectionName.contains("Annual Contract Works")){
						sectionName = "AnnualWorks";
					}
					if(sectionName.contains("Single Project Contract Works")){
						sectionName = "SingleProject";
					}
					expectedIPTRate = (String)common.Requote_excel_data_map.get("PS_"+coverName_withoutSpace+"_IPT");
					
				}
				
				if(common.currentRunningFlow.equalsIgnoreCase("Renewal")){
					if(sectionName.contains("Personal Accident Standard")){
						coverName_withoutSpace = "PersonalAccidentStandard";
					}
					if(sectionName.contains("Goods In Transit")){
						coverName_withoutSpace = "GoodsInTransit";
					}
					key = "CD_"+coverName_withoutSpace;
					if(sectionName.contains("Personal Accident Standard")){
						coverName_withoutSpace = "PersonalAccident";
					}
					if(sectionName.contains("Goods In Transit")){
						coverName_withoutSpace = "GoodsinTransit";
					}
					if(sectionName.contains("Annual Contract Works")){
						sectionName = "AnnualWorks";
					}
					if(sectionName.contains("Single Project Contract Works")){
						sectionName = "SingleProject";
					}
					expectedIPTRate = (String)common.Renewal_excel_data_map.get("PS_"+coverName_withoutSpace+"_IPT");
					
				}
				if(key.contains("CD_AnnualContractWorks")){
					key = "CD_AnnualWorks";
				}
				if(map_to_update.get(key).toString().equalsIgnoreCase("Yes")){
					if(sectionName.contains("Personal Accident Standard")){
						coverName_withoutSpace = "PersonalAccident";
					}
					if(sectionName.contains("Goods In Transit")){
						coverName_withoutSpace = "GoodsinTransit";
					}
					if(sectionName.contains("Annual Contract Works")){
						sectionName = "AnnualWorks";
					}
					if(sectionName.contains("Single Project Contract Works")){
						sectionName = "SingleProject";
					}
					String actualGrossPremium =  common.roundedOff(taxTable_tBody.findElement(By.xpath("tr["+(i+1)+"]/td[2]")).getText());
					String actualIPTRate =  taxTable_tBody.findElement(By.xpath("tr["+(i+1)+"]/td[4]")).getText();
					String actualGrossTax =  common.roundedOff(taxTable_tBody.findElement(By.xpath("tr["+(i+1)+"]/td[5]")).getText());
					
					String expectedGrossPremium = common.roundedOff((String)map_to_update.get("PS_"+coverName_withoutSpace+"_GP"));
					String expectedGrossTax = common.roundedOff(Double.toString(((Double.parseDouble(expectedGrossPremium) * Double.parseDouble(expectedIPTRate)) / 100.0)));
					
					if(verification(actualGrossPremium, expectedGrossPremium, sectionName, "Gross Premium") && 
					   /*verification(actualIPTRate, expectedIPTRate, sectionName, "IPT Rate") &&*/
					   verification(actualGrossTax, expectedGrossTax, sectionName, "Gross Tax")){
						
					}
					continue;
				}else{
					
					if(common.NB_excel_data_map.get("CD_"+coverName_withoutSpace).toString().equalsIgnoreCase("Yes")){
						if(sectionName.contains("Personal Accident Standard")){
							sectionName = "Personal Accident";
						}
						if(sectionName.contains("Goods In Transit")){
							coverName_withoutSpace = "GoodsinTransit";
						}
						if(sectionName.contains("Annual Contract Works")){
							sectionName = "AnnualWorks";
						}
						if(sectionName.contains("Single Project Contract Works")){
							sectionName = "SingleProject";
						}
						String actualGrossPremium =  common.roundedOff(taxTable_tBody.findElement(By.xpath("tr["+(i+1)+"]/td[2]")).getText());
						String actualIPTRate =  taxTable_tBody.findElement(By.xpath("tr["+(i+1)+"]/td[4]")).getText();
						String actualGrossTax =  common.roundedOff(taxTable_tBody.findElement(By.xpath("tr["+(i+1)+"]/td[5]")).getText());
						
						String expectedGrossPremium = common.roundedOff((String)common.GrosspremSmryData.get("PS_"+coverName_withoutSpace.replaceAll(" ", "")+"_GP"));
						String expectedGrossTax = common.roundedOff(Double.toString(((Double.parseDouble(expectedGrossPremium) * Double.parseDouble(expectedIPTRate)) / 100.0)));
						
						if(verification(actualGrossPremium, expectedGrossPremium, sectionName, "Gross Premium") && 
						   /*verification(actualIPTRate, expectedIPTRate, sectionName, "IPT Rate") &&*/
						   verification(actualGrossTax, expectedGrossTax, sectionName, "Gross Tax")){
							
						}
						
					}else{
						TestUtil.reportStatus("Cover Name <b>  ["+sectionName+"]  </b> should not present in the table as This cover is not selected on Cover Details page.", "FAIL", false);
						count++;
					}
					
					
				}
			}
			
		}
		
		if(count==0){
			TestUtil.reportStatus("<b> Verified Gross Premium/Gross Tax/IPT Rate for each cover present under Tax Adjustment table. </b>", "Info", false);
		}
		
		return true;
	
	}catch (Throwable t) {
		
		return false;
	}
}

//This function will add 
	@SuppressWarnings("static-access")
	public static boolean adjustInsuranceTax(String grossPremium,String sectionName,String code,String event){
		
		
		Map<String, List<Map<String, String>>> internal_data_map = new HashMap<>();
		Map<Object, Object> outer_data_map=common.NB_excel_data_map;
		switch(common.currentRunningFlow){
			case "NB":
				internal_data_map = common.NB_Structure_of_InnerPagesMaps;
				outer_data_map = common.NB_excel_data_map;
				break;
			case "MTA":
				internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
				outer_data_map = common.MTA_excel_data_map;
				break;
			case "Renewal":
				internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
				outer_data_map = common.Renewal_excel_data_map;
				break;
			case "Rewind":
				internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
				outer_data_map = common.Rewind_excel_data_map;
				break;
			case "Requote":
				internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
				outer_data_map = common.Requote_excel_data_map;
				break;
		
		}
		
		try{
			variableTaxAdjustmentDataMaps = new LinkedHashMap<>();
			variableTaxAdjustmentVerificationMaps = new LinkedHashMap<>();
			variableTaxAdjustmentIDs = new HashMap<>();
			grossTaxValues_Map = new HashMap<>();
			headerNameStorage = new ArrayList<>();
			inputarraylist = new ArrayList<>();
			inputarraylistMTA = new ArrayList<>();
			Adjusted_Premium_map = new HashMap<>();
			String flag = "";
			if(sectionName.contains("Personal Accident Standard")){
				sectionName = "Personal Accident";
				flag = "true";
			}
			if(sectionName.equalsIgnoreCase("Goods In Transit")){
				sectionName = "Goods in Transit";
			}
			if(sectionName.contains("Annual Contract Works")){
				sectionName = "AnnualWorks";
			}
			if(sectionName.contains("Single Project Contract Works")){
				sectionName = "SingleProject";
			}
			adjustedPremium = Double.parseDouble(common.roundedOff((String)outer_data_map.get("PS_"+sectionName.replaceAll(" ", "")+"_GP")));
			
			
			String[] properties = ((String)outer_data_map.get("PS_VariableTaxAdjustment")).split(";");
            int count = properties.length;
					
			int counter = 0;
			String coverName = sectionName;
			
			for(int l=0;l<count;l++){
				if(flag.equalsIgnoreCase("true")){
					sectionName = "Personal Accident Standard";
					flag = "";
				}
				if(sectionName.contains("Goodsin")){
					sectionName = "Goods In Transit";
				}
				if(sectionName.contains("SingleProject")){
					sectionName = "Single Project Contract Works";
				}
				if(internal_data_map.get("Variable Tax Adjustment").get(l).get("VTA_SectionName").equalsIgnoreCase(sectionName)
						&& 
						!(internal_data_map.get("Variable Tax Adjustment").get(l).get("Automation Key").contains("Rewind"))){
					
						try{
							k.ImplicitWaitOff();
							k.Click("insuranceTaxAddAdjustmentButton");
						}catch(Exception e){
							k.Click("insuranceTaxAddAdjustmentButton_1");
						}finally {
							k.ImplicitWaitOn();
						}
						WebElement adjustmentTax = k.getObject("insuranceTaxAddAdjustmentTable");
						Select select = new Select(adjustmentTax.findElement(By.xpath("//*[contains(@name,'section')]")));
						String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.US).format(new Date());
						String description = null;
						description = internal_data_map.get("Variable Tax Adjustment").get(l).get("VTA_Description");
						
						if(!description.contains("_")){
							description = internal_data_map.get("Variable Tax Adjustment").get(l).get("VTA_Description")+"_"+timeStamp;
							TestUtil.WriteDataToXl_innerSheet(code+"_"+event, "Variable Tax Adjustment", internal_data_map.get("Variable Tax Adjustment").get(l).get("Automation Key"), "VTA_Description", description, common.NB_Structure_of_InnerPagesMaps.get("Variable Tax Adjustment").get(l));
						}
						
						select.selectByVisibleText(internal_data_map.get("Variable Tax Adjustment").get(l).get("VTA_SectionName"));
						customAssert.assertTrue(k.Input("InsuranceTaxAdjustmentPremium", internal_data_map.get("Variable Tax Adjustment").get(l).get("VTA_Premium") ), "Unable to enter Premium on insurance Tax adjustment.");
						customAssert.assertTrue(k.Input("InsuranceTaxAdjustmentTaxRate", internal_data_map.get("Variable Tax Adjustment").get(l).get("VTA_TaxRate") ), "Unable to enter Premium on insurance Tax adjustment.");
						customAssert.assertTrue(k.Input("InsuranceTaxAdjustmentDescription", description ), "Unable to enter Premium on insurance Tax adjustment.");
						
						//System.out.println(variableTaxAdjustmentDataMaps);
						
						variableTaxAdjustmentDataMaps.put(internal_data_map.get("Variable Tax Adjustment").get(l).get("Automation Key")+"_Section Name", internal_data_map.get("Variable Tax Adjustment").get(l).get("VTA_SectionName"));
						variableTaxAdjustmentDataMaps.put(internal_data_map.get("Variable Tax Adjustment").get(l).get("Automation Key")+"_Premium", internal_data_map.get("Variable Tax Adjustment").get(l).get("VTA_Premium"));
						variableTaxAdjustmentDataMaps.put(internal_data_map.get("Variable Tax Adjustment").get(l).get("Automation Key")+"_Tax Rate", internal_data_map.get("Variable Tax Adjustment").get(l).get("VTA_TaxRate"));
						variableTaxAdjustmentDataMaps.put(internal_data_map.get("Variable Tax Adjustment").get(l).get("Automation Key")+"_Description", internal_data_map.get("Variable Tax Adjustment").get(l).get("VTA_Description"));
						
						headerNameStorage.add(internal_data_map.get("Variable Tax Adjustment").get(l).get("Automation Key"));
						//variableTaxAdjustmentVerificationMaps.put(common.NB_Structure_of_InnerPagesMaps.get("Variable Tax Adjustment").get(l).get("Automation Key"), variableTaxAdjustmentDataMaps);
						
						List<WebElement> listOfButtons = adjustmentTax.findElements(By.tagName("a"));
						//System.out.println("****************Total present button "+listOfButtons.size()+"********************");
						
						for(int k=0;k<listOfButtons.size();k++){
							String buttonName = listOfButtons.get(k).getText();
							if(buttonName.equalsIgnoreCase("Save")){
								listOfButtons.get(k).click();
								counter++;
								if(!validationOnAdjustedPremium(sectionName,l)){
									break;
								}
								adjustedPremium = adjustedPremium - Double.parseDouble(internal_data_map.get("Variable Tax Adjustment").get(l).get("VTA_Premium"));
								//Adjusted_Premium_map.put(sectionName+"_adjustedPremium", adjustedPremium);
								break;
							}
						}
					}
				}
			variableTaxAdjustmentIDs.put(sectionName, counter);
			return true;
			
		}
		catch (Throwable t) {
			
			return false;
		}
	}

public static boolean validationOnAdjustedPremium(String sectionName,int index){
	
	Map<String, List<Map<String, String>>> internal_data_map = new HashMap<>();
	Map<Object, Object> outer_data_map=common.NB_excel_data_map;
	switch(common.currentRunningFlow){
		case "NB":
			internal_data_map = common.NB_Structure_of_InnerPagesMaps;
			outer_data_map = common.NB_excel_data_map;
			break;
		case "MTA":
			internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
			outer_data_map = common.MTA_excel_data_map;
			break;
		case "Renewal":
			internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
			outer_data_map = common.Renewal_excel_data_map;
			break;
		case "Rewind":
			internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
			outer_data_map = common.Rewind_excel_data_map;
			break;
		case "Requote":
			internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
			outer_data_map = common.Requote_excel_data_map;
			break;
	
	}
	
	
	try {
		
		if(Double.parseDouble(internal_data_map.get("Variable Tax Adjustment").get(index).get("VTA_Premium")) > 0.0 && 
		   Double.parseDouble(internal_data_map.get("Variable Tax Adjustment").get(index).get("VTA_Premium")) <= adjustedPremium &&
		   Double.parseDouble(internal_data_map.get("Variable Tax Adjustment").get(index).get("VTA_TaxRate")) <= 100.0 &&
		   Double.parseDouble(internal_data_map.get("Variable Tax Adjustment").get(index).get("VTA_TaxRate")) >= 0.0
				){
			return true;
		}else{
			if(!(Double.parseDouble(internal_data_map.get("Variable Tax Adjustment").get(index).get("VTA_Premium")) > 0.0)){
				TestUtil.reportStatus("<p style='color:black'> Gross Premium should be <b> > 0.0 </b> .Entered Gross Premium is : <b>[  "+internal_data_map.get("Variable Tax Adjustment").get(index).get("VTA_Premium")+"  ]</b>. Skipped Tax adjustment for Data ID is : [<b>  "+internal_data_map.get("Variable Tax Adjustment").get(index).get("Automation Key")+"  </b>] AND Cover Name is :  <b>[  "+sectionName+"  ]</b> </p>", "Info", false);
			}else if(!(Double.parseDouble(internal_data_map.get("Variable Tax Adjustment").get(index).get("VTA_Premium")) <= adjustedPremium)){
				TestUtil.reportStatus("<p style='color:black'> Gross Premium limit is either completed or higher than Gross Premium. Available Gross Premium is : <b>[  "+adjustedPremium+"  ]</b> And Entered Gross Premium is : <b>[  "+internal_data_map.get("Variable Tax Adjustment").get(index).get("VTA_Premium")+"  ]</b>. Skipped Tax adjustment for Data ID is : [<b>  "+internal_data_map.get("Variable Tax Adjustment").get(index).get("Automation Key")+"  </b>] AND Cover Name is :  <b>[  "+sectionName+"  ]</b> </p>", "Info", false);
			}else if(!(Double.parseDouble(internal_data_map.get("Variable Tax Adjustment").get(index).get("VTA_TaxRate")) <= 100.0)){
				TestUtil.reportStatus("<p style='color:black'> Tax rate should be <b> <= 100.0 </b>. Entered Tax rate is : <b>[  "+internal_data_map.get("Variable Tax Adjustment").get(index).get("VTA_TaxRate")+"  ]</b>. Skipped Tax adjustment for Data ID is : [<b>  "+internal_data_map.get("Variable Tax Adjustment").get(index).get("Automation Key")+"  </b>] AND Cover Name is :  <b>[  "+sectionName+"  ]</b> </p>", "Info", false);
			}else if(!(Double.parseDouble(internal_data_map.get("Variable Tax Adjustment").get(index).get("VTA_TaxRate")) >= 0.0)){
				TestUtil.reportStatus("<p style='color:black'> Tax rate should be <b> >= 0.0 </b>.Entered Tax rate is : <b>[  "+internal_data_map.get("Variable Tax Adjustment").get(index).get("VTA_TaxRate")+"  ]</b> Skipped Tax adjustment for Data ID is : [<b>  "+internal_data_map.get("Variable Tax Adjustment").get(index).get("Automation Key")+"  </b>] AND Cover Name is :  <b>[  "+sectionName+"  ]</b> </p>", "Info", false);
			}
			
			@SuppressWarnings("static-access")
			WebElement adjustmentTax = k.getObject("insuranceTaxAddAdjustmentTable");
			customAssert.assertTrue(k.SelectBtnWebElement(adjustmentTax, "insuranceTaxAddAdjustmentSaveCancleButton", "Cancel"), "Unable to select Cancel button.");
			
			return false;
		}
		
	} catch (Throwable t) {
		
		return false;
	} 

}

@SuppressWarnings({ "static-access", "unused" })
public static boolean verifyAdjustedTaxValues(String sectionName,String code , String event){
	
	Map<String, List<Map<String, String>>> internal_data_map = new HashMap<>();
	Map<Object, Object> outer_data_map=common.NB_excel_data_map;
	switch(common.currentRunningFlow){
		case "NB":
			internal_data_map = common.NB_Structure_of_InnerPagesMaps;
			outer_data_map = common.NB_excel_data_map;
			break;
		case "MTA":
			internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
			outer_data_map = common.MTA_excel_data_map;
			break;
		case "Renewal":
			internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
			outer_data_map = common.Renewal_excel_data_map;
			break;
		case "Rewind":
			internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
			outer_data_map = common.Rewind_excel_data_map;
			break;
		case "Requote":
			internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
			outer_data_map = common.Requote_excel_data_map;
			break;
	
	}
	
	
	
	String coverName = sectionName;
	adjustedTotalTax = 0.0;
	adjustedTotalPremium = 0.0;
	unAdjustedTotalTax = 0.0;
	inputarraylist.clear();
	inputarraylistMTA.clear();
	
	try {
		taxTable_tBody = k.getObject("inssuranceTaxMainTableBODY");
		List<WebElement> list2 = taxTable_tBody.findElements(By.tagName("tr"));
		countOfTableRows = list2.size();
		//System.out.println("Rows in table unde tBody : "+list2.size());
		
		Outer:
		for(int j=0;j<countOfCovers-1;j++){
			if(taxTable_tBody.findElement(By.xpath("tr["+(j+1)+"]/td[1]")).getText().equalsIgnoreCase("Totals")){
				//break Outer;
				break Outer;
			}
			
			if(sectionName.equalsIgnoreCase(taxTable_tBody.findElement(By.xpath("tr["+(j+1)+"]/td[1]")).getText())){
				if(sectionName.contains("Goods In")){
					sectionName = "Goods in Transit";
				}
				Inner :
				for(int m=0;m<=variableTaxAdjustmentIDs.get(sectionName)+1;m++){
					if(taxTable_tBody.findElement(By.xpath("tr["+(j+(m+1))+"]/td[1]")).getText().equalsIgnoreCase("Totals")){
						//break Outer;
						break Inner;
					}
					taxTable_tBody = k.getObject("inssuranceTaxMainTableBODY");
					//Verify Adjusted Values
					errorVal = errorVal + verifyAdjustedTaxCalculation(sectionName,j,m);
				}
				
				//Verify Unadjusted Values
				errorVal = errorVal + verifyUnAdjustedTaxCalculation(sectionName,j);
				//Verify Gross Tax values
				errorVal = errorVal + verifyGrossTaxCalculation(sectionName,j,code,event);
				break;
			}
		}
	} catch (Throwable t) {
		return false;
	} 
	
	return true;
}

public static int verifyAdjustedTaxCalculation(String sectionName,int j,int m){
	

	Map<String, List<Map<String, String>>> internal_data_map = new HashMap<>();
	Map<Object, Object> outer_data_map=common.NB_excel_data_map;
	switch(common.currentRunningFlow){
		case "NB":
			internal_data_map = common.NB_Structure_of_InnerPagesMaps;
			outer_data_map = common.NB_excel_data_map;
			break;
		case "MTA":
			internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
			outer_data_map = common.MTA_excel_data_map;
			break;
		case "Renewal":
			internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
			outer_data_map = common.Renewal_excel_data_map;
			break;
		case "Rewind":
			internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
			outer_data_map = common.Rewind_excel_data_map;
			break;
		case "Requote":
			internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
			outer_data_map = common.Requote_excel_data_map;
			break;
	
	}
	
	try{
		String coverName = taxTable_tBody.findElement(By.xpath("tr["+(j+(m+1))+"]/td[1]")).getText();
		String adjustedPremium =  taxTable_tBody.findElement(By.xpath("tr["+(j+(m+1))+"]/td[3]")).getText();
		String iptRate =  taxTable_tBody.findElement(By.xpath("tr["+(j+(m+1))+"]/td[4]")).getText();
		String adjustedTax =  taxTable_tBody.findElement(By.xpath("tr["+(j+(m+1))+"]/td[6]")).getText();
		String description =  taxTable_tBody.findElement(By.xpath("tr["+(j+(m+1))+"]/td[7]")).getText();
		String adjustedBy =  taxTable_tBody.findElement(By.xpath("tr["+(j+(m+1))+"]/td[8]")).getText();
		
		int index = k.getText("BannerName").indexOf("\n");
		String adjustedByName = k.getText("BannerName").substring(0, index);
		//int count = 0;
		
		String[] properties = ((String)outer_data_map.get("PS_VariableTaxAdjustment")).split(";");
        int noOfVariableTax = properties.length;
		
		if((coverName==null || coverName.isEmpty() || coverName.equalsIgnoreCase("")) && !(description.equalsIgnoreCase("Unadjusted Premium"))){
			for(int p=0;p<variableTaxAdjustmentIDs.get(sectionName);p++){
				if(!inputarraylist.contains(p)){
					int count = 0;
					while(count < noOfVariableTax){
						
						if(description.equalsIgnoreCase((String) variableTaxAdjustmentDataMaps.get(headerNameStorage.get(p)+"_Description"))){
							if(internal_data_map.get("Variable Tax Adjustment").get(count).get("Automation Key").equalsIgnoreCase((String)headerNameStorage.get(p))){
								
								double adjustedPremium_calc = Double.parseDouble(internal_data_map.get("Variable Tax Adjustment").get(count).get("VTA_Premium"));
								double iptRate_calc = Double.parseDouble(internal_data_map.get("Variable Tax Adjustment").get(count).get("VTA_TaxRate"));
								String adjustedTax_calc = common.roundedOff(Double.toString((adjustedPremium_calc * iptRate_calc ) / 100.0));
								
								if(verification(common.roundedOff(adjustedPremium), common.roundedOff((String) variableTaxAdjustmentDataMaps.get(headerNameStorage.get(p)+"_Premium")), sectionName, "Adjusted Premium") &&
								   /*verification(common.roundedOff(iptRate), common.roundedOff((String) variableTaxAdjustmentDataMaps.get(headerNameStorage.get(p)+"_Tax Rate")), sectionName, "IPT Rate") &&*/
								   verification(common.roundedOff(adjustedTax), adjustedTax_calc, sectionName, "Adjusted Tax") &&
								   verification(description, (String) variableTaxAdjustmentDataMaps.get(headerNameStorage.get(p)+"_Description"), sectionName, "Description") &&
								   verification(adjustedBy, adjustedByName, sectionName, "Adjusted By")){
								   
									   inputarraylist.add(p);
									   adjustedTotalTax = adjustedTotalTax + Double.parseDouble(adjustedTax_calc);
									   adjustedTotalPremium = adjustedTotalPremium + adjustedPremium_calc;
									   return 0;
								
								}else{
								   return 1;
								}
							}
							
						}
						count++;
					}
					
				}
			}
		}
		return 0;
	}catch (Throwable t) {
		
		return 1;
	} 
}

public static int verifyUnAdjustedTaxCalculation(String sectionName,int j){
	
	Map<String, List<Map<String, String>>> internal_data_map = new HashMap<>();
	Map<Object, Object> outer_data_map=common.NB_excel_data_map;
	switch(common.currentRunningFlow){
		case "NB":
			internal_data_map = common.NB_Structure_of_InnerPagesMaps;
			outer_data_map = common.NB_excel_data_map;
			break;
		case "MTA":
			internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
			outer_data_map = common.MTA_excel_data_map;
			break;
		case "Renewal":
			internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
			outer_data_map = common.Renewal_excel_data_map;
			break;
		case "Rewind":
			internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
			outer_data_map = common.Rewind_excel_data_map;
			break;
		case "Requote":
			internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
			outer_data_map = common.Requote_excel_data_map;
			break;
	
	}
	
	try{
		
		for(int m=0;m<=variableTaxAdjustmentIDs.get(sectionName)+1;m++){
			
			if(taxTable_tBody.findElement(By.xpath("tr["+(j+(m+1))+"]/td[1]")).getText().equalsIgnoreCase("Totals")){
				//break Outer;
				break;
			}
			
			String adjustedPremium =  taxTable_tBody.findElement(By.xpath("tr["+(j+(m+1))+"]/td[3]")).getText();
			String iptRate =  taxTable_tBody.findElement(By.xpath("tr["+(j+(m+1))+"]/td[4]")).getText();
			String adjustedTax =  taxTable_tBody.findElement(By.xpath("tr["+(j+(m+1))+"]/td[6]")).getText();
			String description =  taxTable_tBody.findElement(By.xpath("tr["+(j+(m+1))+"]/td[7]")).getText();
			String adjustedBy =  taxTable_tBody.findElement(By.xpath("tr["+(j+(m+1))+"]/td[8]")).getText();
			
			int index = k.getText("BannerName").indexOf("\n");
			String adjustedByName = k.getText("BannerName").substring(0, index);
			
			if(description.equalsIgnoreCase("Unadjusted Premium")){
				if(sectionName.equalsIgnoreCase("Personal Accident Standard")){
					sectionName = "Personal Accident";
				}
				if(sectionName.contains("Annual Contract Works")){
					sectionName = "AnnualWorks";
				}
				if(sectionName.contains("Single Project Contract Works")){
					sectionName = "SingleProject";
				}
				String unAdjustedPremium = common.roundedOff(Double.toString(Double.parseDouble((String)outer_data_map.get("PS_"+sectionName.replaceAll(" ", "")+"_GP")) - adjustedTotalPremium));
				String iptRate_calc =  common.roundedOff(Double.toString(Double.parseDouble((String)outer_data_map.get("PS_IPTRate"))));
				String adjustedTax_calc = common.roundedOff(Double.toString((Double.parseDouble(unAdjustedPremium) * Double.parseDouble(iptRate_calc) ) / 100.0));
				unAdjustedTotalTax = Double.parseDouble(adjustedTax_calc);
				
				if(verification(common.roundedOff(adjustedPremium), unAdjustedPremium, sectionName, "Un Adjusted Premium") &&
				   /*verification(common.roundedOff(iptRate), iptRate_calc, sectionName, "IPT Rate") &&*/
				   verification(common.roundedOff(adjustedTax), adjustedTax_calc, sectionName, "Un Adjusted Tax") &&
				   verification(description, "Unadjusted Premium", sectionName, "Description") &&
				   verification(adjustedByName, adjustedBy, sectionName, "Adjusted By")){
					
					return 0;
				
				}else{
					return 1;
				}
			}
		}
		return 0;
	}catch (Throwable t) {
		
		return 1;
	}
}

public static int verifyGrossTaxCalculation(String sectionName,int j,String code , String event){
	
	Map<String, List<Map<String, String>>> internal_data_map = new HashMap<>();
	Map<Object, Object> outer_data_map=common.NB_excel_data_map;
	switch(common.currentRunningFlow){
		case "NB":
			internal_data_map = common.NB_Structure_of_InnerPagesMaps;
			outer_data_map = common.NB_excel_data_map;
			break;
		case "MTA":
			internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
			outer_data_map = common.MTA_excel_data_map;
			break;
		case "Renewal":
			internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
			outer_data_map = common.Renewal_excel_data_map;
			break;
		case "Rewind":
			internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
			outer_data_map = common.Rewind_excel_data_map;
			break;
		case "Requote":
			internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
			outer_data_map = common.Requote_excel_data_map;
			break;
	
	}
	
	String grossPremium =  common.roundedOff(taxTable_tBody.findElement(By.xpath("tr["+(j+1)+"]/td[2]")).getText());
	String iptRate =  taxTable_tBody.findElement(By.xpath("tr["+(j+1)+"]/td[4]")).getText();
	String grossTax =  common.roundedOff(taxTable_tBody.findElement(By.xpath("tr["+(j+1)+"]/td[5]")).getText());
	String colName = (String)outer_data_map.get("Automation Key");
	if(sectionName.equalsIgnoreCase("Personal Accident Standard")){
		sectionName = "Personal Accident";
	}
	if(sectionName.contains("Annual Contract Works")){
		sectionName = "AnnualWorks";
	}
	if(sectionName.contains("Single Project Contract Works")){
		sectionName = "SingleProject";
	}
	String trimmedSectionName = sectionName.replaceAll(" ", "");
	
	String finalGrossPremium = common.roundedOff((String)outer_data_map.get("PS_"+trimmedSectionName+"_GP"));
	String finalGrossTax,finalIPTRate,finalNetPremiumTax = null;
	if(inputarraylist.size()!=0){
		finalGrossTax = common.roundedOff(Double.toString(unAdjustedTotalTax +  adjustedTotalTax));
		finalIPTRate = common_HHAZ.roundedOff(Double.toString(((Double.parseDouble(finalGrossTax) /Double.parseDouble(finalGrossPremium)) * 100.0 )));
	}else{
		finalGrossTax = common.roundedOff(Double.toString((Double.parseDouble((String)outer_data_map.get("PS_"+trimmedSectionName+"_GP")) * ((Double.parseDouble((String)outer_data_map.get("PS_IPTRate")) / 100.0)))));
		finalIPTRate = (String)outer_data_map.get("PS_IPTRate");
	}
	
	common_HHAZ.totalGrossTax = common_HHAZ.totalGrossTax + Double.parseDouble(finalGrossTax);
	common_HHAZ.totalGrossPremium = common_HHAZ.totalGrossPremium + Double.parseDouble(finalGrossPremium);
	
	
	TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",colName, "PS_"+sectionName.replaceAll(" ", "")+"_GT", finalGrossTax, outer_data_map);
	TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",colName, "PS_"+sectionName.replaceAll(" ", "")+"_IPT", finalIPTRate, outer_data_map);
	
	if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
		TestUtil.WriteDataToXl(code+"_Rewind", "Premium Summary",colName, "PS_"+sectionName.replaceAll(" ", "")+"_GT", finalGrossTax, common.Rewind_excel_data_map);
		TestUtil.WriteDataToXl(code+"_Rewind", "Premium Summary",colName, "PS_"+sectionName.replaceAll(" ", "")+"_IPT", finalIPTRate, common.Rewind_excel_data_map);
	}
	if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
		TestUtil.WriteDataToXl(code+"_MTA", "Premium Summary",colName, "PS_"+sectionName.replaceAll(" ", "")+"_GT", finalGrossTax, common.MTA_excel_data_map);
		TestUtil.WriteDataToXl(code+"_MTA", "Premium Summary",colName, "PS_"+sectionName.replaceAll(" ", "")+"_IPT", finalIPTRate, common.MTA_excel_data_map);
	}
	
	AdjustedTaxDetails.put(sectionName.replaceAll(" ", "")+"_AT", Double.toString(adjustedTotalTax));
	AdjustedTaxDetails.put(sectionName.replaceAll(" ", "")+"_AP", Double.toString(adjustedTotalPremium));
	
	grossTaxValues_Map.put(sectionName, Double.parseDouble(finalGrossTax));
	
	if(verification(grossPremium,finalGrossPremium,sectionName,"Gross Premium") && 
			verification(grossTax,finalGrossTax,sectionName,"Gross Tax") 
			/*&& verification(iptRate,finalIPTRate,sectionName,"IPT Rate")*/){
		TestUtil.reportStatus("[<b>  "+sectionName+"  </b>]  cover --- Verified Gross Premium is :  <b>[ "+finalGrossPremium+" ]</b> , IPT Rate is :  <b>[ "+finalIPTRate+" ]</b> , Gross Tax is :  <b>[ "+finalGrossTax+" ]</b> ", "PASS", false);
		return 0;
	}else{
		return 1;
	}
}

public static boolean verification(String actualValue,String expectedValue,String sectionName,String description){
	
	if(description.equalsIgnoreCase("Gross Tax")){
		double actVal = Double.parseDouble(actualValue);
		double expVal = Double.parseDouble(expectedValue);
		double diffrence = actVal - expVal;
		
		if(diffrence<=0.05 && diffrence>=-0.05){
			return true;
		}else{
			TestUtil.reportStatus("Mistmatch in "+description+" for [<b>  "+sectionName+"  </b>]  cover ---  Expected "+description+" is :  <b>[ "+expectedValue+" ]</b> and Actual "+description+" on Stingray application is : <b>[ "+actualValue+" ]</b>", "Fail", false);
			return false;
		}
	}else{
		if(actualValue.equalsIgnoreCase(expectedValue)){
			return true;
		}else{
			TestUtil.reportStatus("Mistmatch in "+description+" for [<b>  "+sectionName+"  </b>]  cover ---  Expected "+description+" is :  <b>[ "+expectedValue+" ]</b> and Actual "+description+" on Stingray application is : <b>[ "+actualValue+" ]</b>", "Fail", false);
			return false;
		}
	}
	
	
}


/**
 * 
 * -----------------------------------------------------------------------------------------------------------------------
 * 
 * 
 */


}
