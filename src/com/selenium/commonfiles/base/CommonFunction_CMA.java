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


public class CommonFunction_CMA extends TestBase{

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
			
			// Computers page - funcComputer
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Computers"),"Issue while Navigating to Computers Cover page ");
			customAssert.assertTrue(funcComputer(common.NB_excel_data_map), "Computers function is having issue(S) .");
		
			
			
			
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
			common_HHAZ.CoversDetails_data_list = new ArrayList<>();
			common_HHAZ.CoversDetails_data_list.add("ComputerRSA"); //For Flat Premium Verification
			
			common.currentRunningFlow="MTA";
			String navigationBy = CONFIG.getProperty("NavigationBy");
			
			customAssert.assertTrue(common_CCD.funcCreateEndorsement(),"Error in Create Endorsement function . ");
			
			customAssert.assertTrue(funcPolicyGeneral(common.MTA_excel_data_map,code,"MTA"), "Policy Details function having issue .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			
			// Computers page - funcComputer
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Computers"),"Issue while Navigating to Computers Cover page ");
			customAssert.assertTrue(funcComputer(common.MTA_excel_data_map), "Computers function is having issue(S) .");
		
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		
			

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
			//common_HHAZ.CoversDetails_data_list.clear();
			String navigationBy = CONFIG.getProperty("NavigationBy");
			common_HHAZ.PremiumFlag = false;
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcRewind());
			
			TestUtil.reportStatus("<b> -----------------------Rewind flow is started---------------------- </b>", "Info", false);
			
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
			if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
				customAssert.assertTrue(common.funcSearchPolicy(common.MTA_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status (Endorsement Submitted (Rewind)) function is having issue(S) . ");	
			}else{
				
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status (Endorsement Submitted (Rewind)) function is having issue(S) . ");
				}else{
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
				}
			}
			customAssert.assertTrue(funcPolicyGeneral(common.Rewind_excel_data_map,code,event), "Policy Details function having issue .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			
			// Computers page - funcComputer
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Computers"),"Issue while Navigating to Computers Cover page ");
			customAssert.assertTrue(funcComputer(common.Rewind_excel_data_map), "Computers function is having issue(S) .");
		
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
			
			TestUtil.reportStatus("<b> -----------------------Requote flow is started---------------------- </b>", "Info", false);
			
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
			if(((String)common.Requote_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Damage"),"Issue while Navigating to Material Damage screen.");
				customAssert.assertTrue(MaterialDamagePage(common.Requote_excel_data_map), "Material DamagePage function is having issue(S) . ");
			}	
			if(((String)common.Requote_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
				customAssert.assertTrue(funcBusinessInterruption(common.Requote_excel_data_map), "Business Interruption function is having issue(S) . ");
			}
			if(((String)common.Requote_excel_data_map.get("CD_Money&Assault")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Money & Assault"),"Issue while Navigating to Money & Assault screen.");
				customAssert.assertTrue(MoneyAssaultPage(common.Requote_excel_data_map), "Material DamagePage function is having issue(S) . ");
			}
			if(((String)common.Requote_excel_data_map.get("CD_EmployersLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to Employers Liability screen.");
				customAssert.assertTrue(EmployersLiabilityPage(common.Requote_excel_data_map), "EmployersLiabilityPage function is having issue(S) . ");
			}
			if(((String)common.Requote_excel_data_map.get("CD_PublicLiability")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public Liability"),"Issue while Navigating to Public Liability screen.");
				customAssert.assertTrue(PublicLiabilityPage(common.Requote_excel_data_map), "Public Liability function is having issue(S) . ");
			}
			if(((String)common.Requote_excel_data_map.get("CD_PersonalAccidentOptional")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Personal Accident Optional"),"Issue while Navigating to Personal Accident Optional screen.");
				customAssert.assertTrue(PersonalAccidentOptionalPage(common.Requote_excel_data_map), "Personal Accident Optional function is having issue(S) . ");
			}
			if(((String)common.Requote_excel_data_map.get("CD_GoodsInTransit")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Goods in Transit"),"Issue while Navigating to Goods in Transit screen.");
				customAssert.assertTrue(GoodsinTransitPage(common.Requote_excel_data_map), "Personal Accident Optional function is having issue(S) . ");
			}
			if(((String)common.Requote_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Legal Expenses"),"Issue while Navigating to Legal Expenses screen.");
				customAssert.assertTrue(LegalExpensesPage(common.Requote_excel_data_map), "Personal Accident Optional function is having issue(S) . ");
			}
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
				
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Computers"),"Issue while Navigating to Computers Cover page ");
				customAssert.assertTrue(funcComputer(common.Renewal_excel_data_map), "Computers function is having issue(S) .");
			
				
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
				
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Computers"),"Issue while Navigating to Computers Cover page ");
				customAssert.assertTrue(funcComputer(common.Rewind_excel_data_map), "Computers function is having issue(S) .");
			
			
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
					customAssert.assertTrue(k.Click("CMA_GEOLimit"),"Error while Clicking Geographic Limit List object . ");
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
					customAssert.assertTrue(k.Click("CMA_PG_TradeAssociations"),"Error while Clicking Professional Bodies List object . ");
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
				
							
				ActualCode = k.getTextByXpath("html/body/div[3]/form/div/table[5]/tbody/tr[2]/td[2]");
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
	
	
	public boolean MaterialDamagePage(Map<Object, Object> map_data){
		boolean retValue = true;
		
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
				customAssert.assertTrue(common.funcPageNavigation("Material Damage", ""),"Material Damage page is having issue(S)");
			 	int count = 0;
				int noOfProperties = 0;
				
				String[] properties = ((String)map_data.get("IP_AddProperty")).split(";");
	            noOfProperties = properties.length;
	            
				if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	            	
			    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
			    }
	            totalMD = 0.0;
	            double finalTotalMD = 0.0;
				while(count < noOfProperties ){
					p_Index = count;
					customAssert.assertTrue(k.Click("CCF_Btn_AddProperty"), "Unable to click Add Property Button on Insured Properties .");
					customAssert.assertTrue(addProperty(map_data,count),"Error while adding insured proprty  .");
					TestUtil.reportStatus("Insured Property  <b>[  "+internal_data_map.get("Property Details").get(count).get("Automation Key")+"  ]</b>  added successfully . ", "Info", true);
					customAssert.assertTrue(k.Click("CCF_Btn_Back"), "Unable to click on Back Button on Property Details .");
					finalTotalMD = finalTotalMD + totalMD;
					count++;
				}
				TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_MaterialDamage_NetNetPremium", String.valueOf(finalTotalMD), map_data);
				TestUtil.reportStatus("All the specified Insured properties added and verified successfully . ", "Info", true);
				
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
	public boolean addProperty(Map<Object, Object> map_data,int count){
		
		boolean r_value=true;
		
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
			
			customAssert.assertTrue(common.funcPageNavigation("Material Damage", ""),"Property Details page navigation issue(S)");
			
			customAssert.assertTrue(k.DropDownSelection("CCD_MD_Location", internal_data_map.get("Property Details").get(count).get("PoD_Locations")), "Unable to select Property location on Material damage screen.");
			String locationBName = internal_data_map.get("Property Details").get(count).get("PoD_Locations");
			
			if(locationBName.contains("Specified")){
				if(((String)map_data.get("CD_Terrorism")).equals("Yes")){	
					customAssert.assertTrue(k.DropDownSelection("CCD_MD_TerrorismArea", internal_data_map.get("Property Details").get(count).get("PoD_TerrorismArea")), "Unable to select Terrorisam area on Material damage screen.");
				}
			}
			int no_of_buildings = Integer.parseInt(k.getAttribute("CCD_MD_No_Of_Buildings", "value"));
			
			//Referral Code - 26
			if(no_of_buildings > 5){
					common_HHAZ.referrals_list.add((String)map_data.get("RM_MaterialDamage_NumberOfBuildingsOnTheSite"));
			}
			
			
			int i_building = 0, i_Content = 0;
			//Add Buildings :
			if((internal_data_map.get("Property Details").get(count).get("PoD_AddBuildings")).equalsIgnoreCase("Yes")){
				
				String addItemTable_Xpath = "//*[@id='buildings_heading']//following::a[@id='ccc-add-item'][1]";
				JavascriptExecutor j_exe = (JavascriptExecutor) driver;
				j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(addItemTable_Xpath)));
				driver.findElement(By.xpath(addItemTable_Xpath)).click();
				WebElement innerPage = k.getObject("Inner_page_locator");
				
				String propertyType = internal_data_map.get("Property Details").get(count).get("AddBuilding_Property").replaceAll("\u00A0", " ").trim();
				customAssert.assertTrue(k.DropDownSelection("CCD_MD_BD_BuildingItem", propertyType), "Unable to select below ground level dropdown from contents.");
				
				String coverBasis = internal_data_map.get("Property Details").get(count).get("AddBuilding_CoverBasis").replaceAll("\u00A0", " ").trim();
				customAssert.assertTrue(k.DropDownSelection("CCD_MD_BD_CoverBasis", coverBasis), "Unable to select below ground level dropdown from contents.");
				
				if(coverBasis.equalsIgnoreCase("Day 1")){
					customAssert.assertTrue(k.Input("CCD_MD_BD_Day1_DV", internal_data_map.get("Property Details").get(count).get("AddBuilding_DeclaredValue")), "Unable to enter Declraed value for buildings.");
					String expectedDayOnePercentage = internal_data_map.get("Property Details").get(count).get("AddBuilding_Day1Percentage");
					String actualDayOnePercentage = innerPage.findElement(By.xpath("//*[@id='ccd_bl_day_one']")).getText();
					
					customAssert.assertTrue(common.compareValues(Double.parseDouble(expectedDayOnePercentage), Double.parseDouble(actualDayOnePercentage), "Day one percentage Value"), "Unable to veiify day one percentage from buildings.");
					
				}else{
					customAssert.assertTrue(k.Input("CCD_MD_BD_SumInsured", internal_data_map.get("Property Details").get(count).get("AddBuilding_Suminsured")), "Unable to enter Sum Insured value for buildings.");
				}
				
				i_building = Integer.parseInt(internal_data_map.get("Property Details").get(count).get("AddBuilding_DeclaredValue"));
				String contigency = internal_data_map.get("Property Details").get(count).get("AddBuilding_Contengencies").replaceAll("\u00A0", " ").trim();
				customAssert.assertTrue(k.DropDownSelection("CCD_MD_BD_Contengencies", contigency), "Unable to select below ground level dropdown from contents.");
				
				String occupancy = internal_data_map.get("Property Details").get(count).get("AddBuilding_Occupancy").replaceAll("\u00A0", " ").trim();
				customAssert.assertTrue(k.DropDownSelection("CCD_MD_BD_Occupancy", occupancy), "Unable to select below ground level dropdown from contents.");
				
				//For BI Rater Purpose
				common_CCD.MD_Building_Occupancies_list.add(occupancy);
				
				customAssert.assertTrue(k.Input("CCD_MD_BD_FullValueItem", internal_data_map.get("Property Details").get(count).get("AddBuilding_FullValueOfItem")), "Unable to enter full value of item for on buildings item.");
				customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"), "Unable to click on Inner button.");
				if(coverBasis.equalsIgnoreCase("First Loss (non-average)")){
					i_Content = Integer.parseInt(internal_data_map.get("Property Details").get(count).get("AddBuilding_FullValueOfItem"));
					Ter_BuildingContents_Sum = Ter_BuildingContents_Sum + i_Content;
				}else if(coverBasis.equalsIgnoreCase("Day 1")){
					i_Content = Integer.parseInt(internal_data_map.get("Property Details").get(count).get("AddBuilding_DeclaredValue"));
					Ter_BuildingContents_Sum = Ter_BuildingContents_Sum + i_Content;
				}else{				
					i_Content = Integer.parseInt(internal_data_map.get("Property Details").get(count).get("AddBuilding_Suminsured"));
					Ter_BuildingContents_Sum = Ter_BuildingContents_Sum + i_Content;
				}
			}
			
			//Add Contents :
			if((internal_data_map.get("Property Details").get(count).get("PoD_AddContents")).equalsIgnoreCase("Yes")){
				
				String addItemTable_Xpath = "//*[@id='contents_heading']//following::a[@id='ccc-add-item'][1]";
				JavascriptExecutor j_exe = (JavascriptExecutor) driver;
				j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(addItemTable_Xpath)));
				driver.findElement(By.xpath(addItemTable_Xpath)).click();
				WebElement innerPage = k.getObject("Inner_page_locator");
				
				customAssert.assertTrue(k.DropDownSelection("CCD_MD_CO_BelowGroundLevel", internal_data_map.get("Property Details").get(count).get("AddContent_belowGroundLevel")), "Unable to select below ground level dropdown from contents.");
				
				String addtionalCover = internal_data_map.get("Property Details").get(count).get("AddContent_AdditionalCover").replaceAll("\u00A0", " ").trim();
				customAssert.assertTrue(k.DropDownSelection("CCD_MD_CO_AddCover", addtionalCover), "Unable to select below ground level dropdown from contents.");
				
				String contentType = internal_data_map.get("Property Details").get(count).get("AddContent_ContentsItemType").replaceAll("\u00A0", " ").trim();
				customAssert.assertTrue(k.DropDownSelection("CCD_MD_CO_ItemType", contentType), "Unable to select below ground level dropdown from contents.");
				
				customAssert.assertTrue(k.Input("CCD_MD_CO_ItemDesc", internal_data_map.get("Property Details").get(count).get("AddContent_ItemDesc")), "Unable to enter full value of item for on buildings item.");
				
				String coverBasis = internal_data_map.get("Property Details").get(count).get("AddContent_CoverBasis").replaceAll("\u00A0", " ").trim();
				customAssert.assertTrue(k.DropDownSelection("CCD_MD_CO_method", coverBasis), "Unable to select below ground level dropdown from contents.");
				
				if(coverBasis.equalsIgnoreCase("Day 1")){
					customAssert.assertTrue(k.Input("CCD_MD_CO_Day1_DV", internal_data_map.get("Property Details").get(count).get("AddContent_DeclaredValue")), "Unable to enter Declraed value for buildings.");
					String expectedDayOnePercentage = internal_data_map.get("Property Details").get(count).get("AddContent_Day1Percentage");
					String actualDayOnePercentage = innerPage.findElement(By.xpath("//*[@id='ccc_md7_day_1_perc']")).getText();
					
					//Referral Code 26.1
					if(contentType.equalsIgnoreCase("Computer Equipment") || contentType.equalsIgnoreCase("Electronic Equipment")){
						if((Double.parseDouble(internal_data_map.get("Property Details").get(count).get("AddContent_DeclaredValue"))) > 10000){
							common_HHAZ.referrals_list.add((String)map_data.get("RM_MaterialDamage_ContentsComputerEquipments"));
						}
					
					}
					
					
					customAssert.assertTrue(common.compareValues(Double.parseDouble(expectedDayOnePercentage), Double.parseDouble(actualDayOnePercentage), "Day one percentage Value"), "Unable to veiify day one percentage from buildings.");
					
				}else{
					customAssert.assertTrue(k.Input("CCD_MD_CO_SumInsured", internal_data_map.get("Property Details").get(count).get("AddContent_Suminsured")), "Unable to enter Sum Insured value for buildings.");
					
					//Referral Code 26.1
					if(contentType.equalsIgnoreCase("Computer Equipment") || contentType.equalsIgnoreCase("Electronic Equipment")){
						if((Double.parseDouble(internal_data_map.get("Property Details").get(count).get("AddContent_Suminsured"))) > 10000){
							common_HHAZ.referrals_list.add((String)map_data.get("RM_MaterialDamage_ContentsComputerEquipments"));
						}
					
					}
					
				}
				
				i_Content = Integer.parseInt(internal_data_map.get("Property Details").get(count).get("AddContent_DeclaredValue"));
				String occupancy = internal_data_map.get("Property Details").get(count).get("AddContent_Occupancy").replaceAll("\u00A0", " ").trim();
				customAssert.assertTrue(k.DropDownSelection("CCD_MD_CO_Occupancy", occupancy), "Unable to select below ground level dropdown from contents.");
				
				customAssert.assertTrue(k.Input("CCD_MD_CO_FullValueItem", internal_data_map.get("Property Details").get(count).get("AddContent_FullValueOfItem")), "Unable to enter full value of item for on buildings item.");
				
				customAssert.assertTrue(k.DropDownSelection("CCD_MD_CO_TeritorialLimit", internal_data_map.get("Property Details").get(count).get("AddContent_TerritorialCover")), "Unable to select below ground level dropdown from contents.");
				
				customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"), "Unable to click on Inner button.");
				if(coverBasis.equalsIgnoreCase("First Loss (non-average)")){
					i_Content = Integer.parseInt(internal_data_map.get("Property Details").get(count).get("AddContent_FullValueOfItem"));
					Ter_BuildingContents_Sum = Ter_BuildingContents_Sum + i_Content;
				}else if(coverBasis.equalsIgnoreCase("Day 1")){
					i_Content = Integer.parseInt(internal_data_map.get("Property Details").get(count).get("AddContent_Day1Percentage"));
					Ter_BuildingContents_Sum = Ter_BuildingContents_Sum + i_Content;
				}else{				
					i_Content = Integer.parseInt(internal_data_map.get("Property Details").get(count).get("AddContent_Suminsured"));
					Ter_BuildingContents_Sum = Ter_BuildingContents_Sum + i_Content;
				}
			}
			
			//Add Specified Content :
			if((internal_data_map.get("Property Details").get(count).get("PoD_AddSpecifiedContents")).equalsIgnoreCase("Yes")){
				
				String addItemTable_Xpath = "//*[@id='spec_cont_heading']//following::a[@id='ccc-add-item'][1]";
				JavascriptExecutor j_exe = (JavascriptExecutor) driver;
				j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(addItemTable_Xpath)));
				driver.findElement(By.xpath(addItemTable_Xpath)).click();
				WebElement innerPage = k.getObject("Inner_page_locator");
				
				customAssert.assertTrue(k.DropDownSelection("CCD_MD_CO_BelowGroundLevel", internal_data_map.get("Property Details").get(count).get("AddSPContent_belowGroundLevel")), "Unable to select below ground level dropdown from contents.");
				
				String SPAdditionalCover = internal_data_map.get("Property Details").get(count).get("AddSPContent_AdditionalCover").replaceAll("\u00A0", " ").trim();
				customAssert.assertTrue(k.DropDownSelection("CCD_MD_SPCO_AddCover", SPAdditionalCover), "Unable to select below ground level dropdown from contents.");
				
				String SPItem = internal_data_map.get("Property Details").get(count).get("AddSPContent_ContentsItemType").replaceAll("\u00A0", " ").trim();
				customAssert.assertTrue(k.DropDownSelection("CCD_MD_SPCO_ItemType", SPItem), "Unable to select below ground level dropdown from contents.");
				
				customAssert.assertTrue(k.Input("CCD_MD_CO_ItemDesc", internal_data_map.get("Property Details").get(count).get("AddSPContent_ItemDesc")), "Unable to enter full value of item for on buildings item.");
				
				String coverBasis = internal_data_map.get("Property Details").get(count).get("AddSPContent_CoverBasis").replaceAll("\u00A0", " ").trim();
				customAssert.assertTrue(k.DropDownSelection("CCD_MD_SPCO_method", coverBasis), "Unable to select below ground level dropdown from contents.");
				
				if(coverBasis.equalsIgnoreCase("Day 1")){
					customAssert.assertTrue(k.Input("CCD_MD_CO_Day1_DV", internal_data_map.get("Property Details").get(count).get("AddSPContent_DeclaredValue")), "Unable to enter Declraed value for buildings.");
					String expectedDayOnePercentage = internal_data_map.get("Property Details").get(count).get("AddSPContent_Day1Percentage");
					String actualDayOnePercentage = innerPage.findElement(By.xpath("//*[@id='ccc_md7_day_1_perc_sc']")).getText();
					
					//Referral Code 26.2
					if(SPItem.equalsIgnoreCase("Computer Equipment") || SPItem.equalsIgnoreCase("Computers - Portable") || SPItem.equalsIgnoreCase("Electronic Equipment")){
						if((Double.parseDouble(internal_data_map.get("Property Details").get(count).get("AddContent_DeclaredValue"))) > 10000){
							common_HHAZ.referrals_list.add((String)map_data.get("RM_MaterialDamage_SpecifiedContentsComputerEquipments"));
						}
					
					}
					
					customAssert.assertTrue(common.compareValues(Double.parseDouble(expectedDayOnePercentage), Double.parseDouble(actualDayOnePercentage), "Day one percentage Value"), "Unable to veiify day one percentage from buildings.");
					
				}else{
					customAssert.assertTrue(k.Input("CCD_MD_CO_SumInsured", internal_data_map.get("Property Details").get(count).get("AddSPContent_Suminsured")), "Unable to enter Sum Insured value for buildings.");
					
					//Referral Code 26.2
					if(SPItem.equalsIgnoreCase("Computer Equipment") || SPItem.equalsIgnoreCase("Computers - Portable") || SPItem.equalsIgnoreCase("Electronic Equipment")){
						if((Double.parseDouble(internal_data_map.get("Property Details").get(count).get("AddSPContent_Suminsured"))) > 10000){
							common_HHAZ.referrals_list.add((String)map_data.get("RM_MaterialDamage_SpecifiedContentsComputerEquipments"));
						}
					
					}
				}
				
				String occupancy = internal_data_map.get("Property Details").get(count).get("AddSPContent_Occupancy").replaceAll("\u00A0", " ").trim();
				customAssert.assertTrue(k.DropDownSelection("CCD_MD_SPCO_Occupancy", occupancy), "Unable to select below ground level dropdown from contents.");
				
				
				customAssert.assertTrue(k.Input("CCD_MD_CO_FullValueItem", internal_data_map.get("Property Details").get(count).get("AddSPContent_FullValueOfItem")), "Unable to enter full value of item for on buildings item.");
				
				customAssert.assertTrue(k.DropDownSelection("CCD_MD_CO_TeritorialLimit", internal_data_map.get("Property Details").get(count).get("AddSPContent_TerritorialCover")), "Unable to select below ground level dropdown from contents.");
				
				customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"), "Unable to click on Inner button.");
				if(coverBasis.equalsIgnoreCase("First Loss (non-average)")){
					i_Content = Integer.parseInt(internal_data_map.get("Property Details").get(count).get("AddSPContent_FullValueOfItem"));
					Ter_BuildingContents_Sum = Ter_BuildingContents_Sum + i_Content;
				}else if(coverBasis.equalsIgnoreCase("Day 1")){
					i_Content = Integer.parseInt(internal_data_map.get("Property Details").get(count).get("AddSPContent_DeclaredValue"));
					Ter_BuildingContents_Sum = Ter_BuildingContents_Sum + i_Content;
				}else{				
					i_Content = Integer.parseInt(internal_data_map.get("Property Details").get(count).get("AddSPContent_Suminsured"));
					Ter_BuildingContents_Sum = Ter_BuildingContents_Sum + i_Content;
				}
				
			}
			
			//Add Specified Content :
			if((internal_data_map.get("Property Details").get(count).get("PoD_AddBeSpoke")).equalsIgnoreCase("Yes")){
				
				String addItemTable_Xpath = "//*[@id='spec_cont_heading']//following::a[@id='ccc-add-bespoke-item'][1]";
				JavascriptExecutor j_exe = (JavascriptExecutor) driver;
				j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(addItemTable_Xpath)));
				driver.findElement(By.xpath(addItemTable_Xpath)).click();
				
				customAssert.assertTrue(k.Input("CCD_MD_BeSpokeDesc", internal_data_map.get("Property Details").get(count).get("AddBeSpoke_Desc")), "Unable to enter Bespoke description.");
				customAssert.assertTrue(k.Input("CCD_MD_BeSpokePremium", internal_data_map.get("Property Details").get(count).get("AddBeSpoke_Premium")), "Unable to enter BeSpoke Premium.");
				
				customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"), "Unable to click on Inner button.");
				
			}
			
			customAssert.assertTrue(k.Click("CCD_MD_ApplyBookRateButton"), "Unable to click on apply book rate button.");
			customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, "Material Damage", "MD_"), "issue in Validate_AutoRatedTables function for Material Damage Cover");
			
		}catch(Throwable t){
			return false;
		}
		
		return r_value;
	}	


public int getIndexDropdownBasicBICovers(String optionValue) {
	
	HashMap<String, Integer> dropdownMap = new HashMap<>();

	dropdownMap.put("Additional increased costs of working", 0);
	dropdownMap.put("Declaration Linked", 1);
	dropdownMap.put("Flexible Limit of Loss", 2);
	dropdownMap.put("Gross Profit", 3);
	dropdownMap.put("Gross Revenue", 4);
	dropdownMap.put("Increased Cost of Working", 5);
	dropdownMap.put("Rent Receivable", 6);
	if(optionValue.contains("Additional")){return 0;}
	if(optionValue.contains("Declaration")){return 1;}
	return dropdownMap.get(optionValue);
}
public int getIndexDropdownAdditionalExtensions(String optionValue) {
	
	HashMap<String, Integer> dropdownMap = new HashMap<>();
	
	dropdownMap.put("Alternative Accommodation Expenses", 1);
	dropdownMap.put("Customers - Specified", 2);
	dropdownMap.put("Customers - Specified (Outside UK)", 3);
	dropdownMap.put("Customers - Unspecified (Outside UK)", 4);
	dropdownMap.put("Data Reinstatement", 5);
	dropdownMap.put("Denial Of Access (non-damage)", 6);
	dropdownMap.put("Discovery Of Vermin", 7);
	dropdownMap.put("Diseases, Murder, Suicide, Defective Sanitation", 8);
	dropdownMap.put("Exhibition Sites", 9);
	dropdownMap.put("Fines And Damages", 10);
	dropdownMap.put("Food And/or Drink Poisoning", 11);
	dropdownMap.put("Loss Of Attraction", 12);
	dropdownMap.put("Motor Vehicle Manufacturers", 13);
	dropdownMap.put("National Lottery Win", 14);
	dropdownMap.put("Patterns, Moulds, Templates Etc", 15);
	dropdownMap.put("Property In Transit", 16);
	dropdownMap.put("Replacement Of Essential Documents", 17);
	dropdownMap.put("Stored Property", 18);
	dropdownMap.put("Sub Postmasters Salaries", 19);
	dropdownMap.put("Suppliers - Specified", 20);
	dropdownMap.put("Suppliers - Specified (Outside UK)", 21);
	dropdownMap.put("Suppliers - Unspecified (Outside UK)", 22);
	if(optionValue.contains("Alternative Accommodation")){return 1;}
	if(optionValue.contains("Customers") && !optionValue.contains("Outside UK")){return 2;}
	return dropdownMap.get(optionValue);
	
}


public boolean MoneyAssaultPage(Map<Object, Object> map_data){
	boolean retValue = true;
	
	try{
			customAssert.assertTrue(common.funcPageNavigation("Money & Assault", ""),"Money & Assault page is having issue(S)");
		 	
			customAssert.assertTrue(k.Input("CCD_MA_NonNegotiableInstrument", (String)map_data.get("MA_NonNegotiableInstrument")), "Unable to enter on Crossed cheques and other non-negotiable instruments  Money & Assualt screen.");
			customAssert.assertTrue(k.Input("CCD_MA_LossOfMoney", (String)map_data.get("MA_LossOfMoney")), "Unable to enter on Any other loss of money including in transit and in bank night safe  Money & Assualt screen.");
			customAssert.assertTrue(k.Input("CCD_MA_SecurityCompanies", (String)map_data.get("MA_SecurityCompanies")), "Unable to enter on  Estimated annual carryings by security companies Money & Assualt screen.");
			customAssert.assertTrue(k.Input("CCD_MA_LimitOfCashInLockedSafe", (String)map_data.get("MA_LimitOfCashInLockedSafe")), "Unable to enter on Limit of cash in locked safe  Money & Assualt screen.");
			customAssert.assertTrue(k.DropDownSelection("CCD_MA_SafeMake", (String)map_data.get("MA_SafeMake")), "Unable to select Safe - Make dropdown from Money & Assualt screen.");
			customAssert.assertTrue(k.DropDownSelection("CCD_MA_SafeModel", (String)map_data.get("MA_SafeModel")), "Unable to select Safe - Model dropdown from Money & Assualt screen.");
			customAssert.assertTrue(k.Input("CCD_MA_MoneyAtHome", (String)map_data.get("MA_MoneyAtHome")), "Unable to enter on Money at home of authorised person  Money & Assualt screen.");
			customAssert.assertTrue(k.Input("CCD_MA_MoneyOutsideBusinessHours", (String)map_data.get("MA_MoneyOutsideBusinessHours")), "Unable to enter on Money outside business hours, not in safe  Money & Assualt screen.");
			customAssert.assertTrue(k.Input("CCD_MA_MoneyDuringHours", (String)map_data.get("MA_MoneyDuringHours")), "Unable to enter on Money during hours Money & Assualt screen.");
			customAssert.assertTrue(k.Input("CCD_MA_OwnAnnualCarryings", (String)map_data.get("MA_OwnAnnualCarryings")), "Unable to enter on Estimated own annual carryings  Money & Assualt screen.");
			customAssert.assertTrue(k.Input("CCD_MA_LimitAnyOneLoss", (String)map_data.get("MA_LimitAnyOneLoss")), "Unable to enter on Limit any one loss Money & Assualt screen.");
			
			customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, "Money & Assault", "MA_"), "issue in Validate_AutoRatedTables function for BI Cover"); 
			TestUtil.reportStatus("All the specified Insured properties added and verified successfully . ", "Info", true);
			
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


public boolean PublicLiabilityPage(Map<Object, Object> map_data){
	boolean retValue = true;
	
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
			customAssert.assertTrue(common.funcPageNavigation("Public Liability", ""),"Public Liability page is having issue(S)");
		 	
			if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
            	
		    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
		    }
			
			customAssert.assertTrue(k.Input("CCD_PL_IndemnityLimit", (String)map_data.get("PL_IndemnityLimit")), "Unable to enter Indemnity limit (£) on  Public Liability screen.");
			
			//Referral Code - 34	
            if(Integer.parseInt((String)map_data.get("PL_IndemnityLimit")) > 5000000){
            	common_HHAZ.referrals_list.add((String)map_data.get("RM_PublicLiability_PLIndemnityLimit"));
            }
			
			// Add multiple activies : 
			String[] properties = ((String)map_data.get("PL_AddActivity")).split(";");
            int no_of_property = properties.length;
            List<WebElement> elm = null;
            
            for(int count=1;count<=no_of_property;count++){
            	
            	String addItemTable_Xpath = "//table//td[text()='Activities']//following::a[text()='Add Row'][1]";
				JavascriptExecutor j_exe = (JavascriptExecutor) driver;
				j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(addItemTable_Xpath)));
				
            	WebElement add_row = driver.findElement(By.xpath("//table//td[text()='Activities']//following::a[text()='Add Row'][1]"));
                add_row.click();
                
                elm = driver.findElements(By.xpath("//table//td[text()='Activities']//following::select[contains(@name,'ccc_pl_activity')]"));
                //int index = getIndexDropdownBD(internal_data_map.get("Loss Of Rent").get(count-1).get("LOI_CoverName"));
                String activity = internal_data_map.get("AddActivitiesPL").get(count-1).get("PL_ACT_Activity").replaceAll("\u00A0", " ");
                customAssert.assertTrue(k.DropDownSelection_WebElement(elm.get(count-1), activity),"Unable to select Activities in Public Liability Activities.");
                
                elm = driver.findElements(By.xpath("//*[contains(@name,'activity_desc')]"));
                customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("AddActivitiesPL").get(count-1).get("PL_ACT_DescriptionOfActivity"),"Input"),"Error while entering Description of activity");
                
                elm = driver.findElements(By.xpath("//*[contains(@name,'wageroll')]"));
                customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("AddActivitiesPL").get(count-1).get("PL_ACT_WageRollEmployees"),"Input"),"Error while entering Wageroll employees only (next 12 months)");
                
                elm = driver.findElements(By.xpath("//*[contains(@name,'turnover')]"));
                customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("AddActivitiesPL").get(count-1).get("PL_ACT_Turnover"),"Input"),"Error while entering Turnover (£)");
                
                customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button Public Liability screen for activies .");
                
               //Referal - Activity as per SUP-748
  	           if(is_PL_referral_activity(activity)){
  	        	   common_HHAZ.referrals_list.add("Public Liability_Refer Activity - "+activity);
  	           } 
                
                
                
                TestUtil.reportStatus("Activity <b> [ "+internal_data_map.get("AddActivitiesPL").get(count-1).get("PL_ACT_DescriptionOfActivity")+" ] </b> is added succefully for Public liability cover.", "Info", true);
                
            }
            
            TestUtil.reportStatus("All the activities are added succefully for Public liability cover.", "Info", true);
            
            //Add Additional cover add bespoke sum insured : 
            properties = ((String)map_data.get("PL_AddBespSumIns")).split(";");
            no_of_property = properties.length;
            elm.clear();
            elm = null;
            int no = 0;
            
            while(no < no_of_property ){
				
            	String addItemTable_Xpath = "//table//td[text()='Additional Covers']//following::a[text()='Add Bespoke Sum Insured'][1]";
				JavascriptExecutor j_exe = (JavascriptExecutor) driver;
				j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(addItemTable_Xpath)));
				
            	WebElement add_row = driver.findElement(By.xpath("//table//td[text()='Additional Covers']//following::a[text()='Add Bespoke Sum Insured'][1]"));
                add_row.click();
                
                WebElement element = driver.findElement(By.xpath("//table//td[text()='Additional Covers']//following::select[@name='cce_pl_additional_covers']"));
                //int index = getIndexDropdownBD(internal_data_map.get("Loss Of Rent").get(count-1).get("LOI_CoverName"));
                String activity = internal_data_map.get("AddBespSumInsPL").get(no).get("PL_ABSI_AddtionalCover").replaceAll("\u00A0", " ");
                customAssert.assertTrue(k.DropDownSelection_WebElement(element, activity),"Unable to select Additional covers in Public Liability Activities.");
                
                element = driver.findElement(By.xpath("//table//td[text()='Wageroll']//following::input[1]"));
                customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(element,internal_data_map.get("AddBespSumInsPL").get(no).get("PL_ABSI_Wageroll"),"Input"),"Error while entering Wageroll");
                
                element = driver.findElement(By.xpath("//table//td[text()='Turnover']//following::input[1]"));
                customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(element,internal_data_map.get("AddBespSumInsPL").get(no).get("PL_ABSI_Turnover"),"Input"),"Error while entering Turnover");
                
                customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"), "Unable to click on Inner button.");
                
                TestUtil.reportStatus("Additional Covers Bespoke sum insured <b> [ "+internal_data_map.get("AddBespSumInsPL").get(no).get("PL_ABSI_AddtionalCover")+" ] </b> is added succefully for Public liability cover.", "Info", true);
                
                no++;
			}
            
            TestUtil.reportStatus("All the Additional Covers Bespoke sum insured are added succefully for Public liability cover.", "Info", true);
            
            customAssert.assertTrue(k.DropDownSelection("CCD_PL_bona_fide_subcontractors", (String)map_data.get("PL_BonaFideSubContractors")), "Unable to select Bona Fide Sub Contractors? ");
            
            if(((String)map_data.get("PL_BonaFideSubContractors")).equalsIgnoreCase("Yes")){
            	
            	//Add Bonafide Sub Contractor Activity : 
     			properties = ((String)map_data.get("PL_AddBFSActivity")).split(";");
                no_of_property = properties.length;
                elm = null;
                 
    	        for(int count=1;count<=no_of_property;count++){
    	        	String addItemTable_Xpath = "//table//td[text()='Bona Fide Sub Contractors']//following::a[text()='Add Row'][1]";
    				JavascriptExecutor j_exe = (JavascriptExecutor) driver;
    				j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(addItemTable_Xpath)));
    				
    	        	WebElement add_row = driver.findElement(By.xpath("//table//td[text()='Bona Fide Sub Contractors']//following::a[text()='Add Row'][1]"));
    	            add_row.click();
    	            
    	            elm = driver.findElements(By.xpath("//table//td[text()='Bona Fide Sub Contractors']//following::select[contains(@name,'activity')]"));
    	            //int index = getIndexDropdownBD(internal_data_map.get("Loss Of Rent").get(count-1).get("LOI_CoverName"));
    	            String activity = internal_data_map.get("AddBFSActivityPL").get(count-1).get("PL_BFS_Activity").replaceAll("\u00A0", " ").trim();
    	            customAssert.assertTrue(k.DropDownSelection_WebElement(elm.get(count-1), activity),"Unable to select Activities in Public Liability Activities.");
    	            
    	            elm = driver.findElements(By.xpath("//table//td[text()='Bona Fide Sub Contractors']//following::*[contains(@name,'activity_desc')]"));
    	            customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("AddBFSActivityPL").get(count-1).get("PL_BFS_DescriptionOfActivity"),"Input"),"Error while entering Description of activity");
    	            
    	            elm = driver.findElements(By.xpath("//table//td[text()='Bona Fide Sub Contractors']//following::*[contains(@name,'wageroll')]"));
    	            customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("AddBFSActivityPL").get(count-1).get("PL_BFS_WageRollEmployees"),"Input"),"Error while entering Wageroll employees only (next 12 months)");
    	            
    	            elm = driver.findElements(By.xpath("//table//td[text()='Bona Fide Sub Contractors']//following::*[contains(@name,'turnover')]"));
    	            customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("AddBFSActivityPL").get(count-1).get("PL_BFS_Turnover"),"Input"),"Error while entering Turnover (£)");
    	            
    	            customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button Public Liability screen for activies .");
    	            
    	          //Referal - Activity as per SUP-748
     	           if(is_PL_referral_activity(activity)){
     	        	   common_HHAZ.referrals_list.add("Public Liability_Refer Activity - "+activity);
     	           } 
    	            
    	            
    	            TestUtil.reportStatus("Bona Fide Sub Contractors Activity <b> [ "+internal_data_map.get("AddBFSActivityPL").get(count-1).get("PL_BFS_Activity")+" ] </b> is added succefully for Public liability cover.", "Info", true);
    	            
                }
                 
                TestUtil.reportStatus("All the Bona Fide Sub Contractors activities are added succefully for Public liability cover.", "Info", true);
        
            }
            
            
            //Add Additional cover add bespoke sum insured : 
            properties = ((String)map_data.get("PL_AddBespoke")).split(";");
            no_of_property = properties.length;
            elm = null;
            no = 0;
            while(no < no_of_property ){
            	
            	String addItemTable_Xpath = "//table//td[text()='Risk Details']//following::a[text()='Add Bespoke Item'][1]";
				JavascriptExecutor j_exe = (JavascriptExecutor) driver;
				j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(addItemTable_Xpath)));
				
            	WebElement add_row = driver.findElement(By.xpath("//table//td[text()='Risk Details']//following::a[text()='Add Bespoke Item'][1]"));
                add_row.click();
                
                WebElement element;
                
	            element = driver.findElement(By.xpath("//table//td[text()='Description ']//following::input[1]"));
                customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(element,internal_data_map.get("AddBespokePL").get(no).get("PL_AddB_Desc"),"Input"),"Error while entering Wageroll");
                
                element = driver.findElement(By.xpath("//table//td[text()='Premium ']//following::input[1]"));
                customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(element,internal_data_map.get("AddBespokePL").get(no).get("PL_AddB_Premium"),"Input"),"Error while entering Turnover");
                
                customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"), "Unable to click on Inner button.");
            	
                TestUtil.reportStatus("Bespoke <b> [ "+internal_data_map.get("AddBespokePL").get(no).get("PL_AddB_Desc")+" ] </b> is added succefully for Public liability cover.", "Info", true);
                
            	no++;
            	
            }
            
            customAssert.assertTrue(k.Click("CCD_ApplyBookRate"), "Unable to click on apply book rate button.");
            customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, "Public Liability", "PL_"), "issue in Validate_AutoRatedTables function for Public Liability Cover");
			TestUtil.reportStatus("All activies are added and verified successfully on Public liability screen. ", "Info", true);
			
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
			customAssert.assertTrue(common.funcPageNavigation("Employers Liability", ""),"Public Liability page is having issue(S)");
		 	
			if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
            	
		    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
		    }
			
			customAssert.assertTrue(k.Input("CCD_EL_IndemnityLimit", (String)map_data.get("EL_IndemnityLimit")), "Unable to enter Indemnity limit (£) on Employers Liability screen.");
			
			//Referral Code - 28
			String El_referral_key="RM_EmployersLiability_";
			
			if(Integer.parseInt((String)map_data.get("EL_IndemnityLimit")) > 10000000){
				common_HHAZ.referrals_list.add((String)map_data.get(El_referral_key+"ELIndemnityLimit"));
			}
			
			
			
			//Add Additional cover add bespoke sum insured : 
			String[] properties = ((String)map_data.get("EL_AddBespSumIns")).split(";");
            int no_of_property = properties.length;
            List<WebElement> elm = null;
            elm = null;
            int no = 0;
            
            while(no < no_of_property ){
				
            	String addItemTable_Xpath = "//table//td[text()='Additional Covers']//following::a[text()='Add Bespoke Sum Insured'][1]";
				JavascriptExecutor j_exe = (JavascriptExecutor) driver;
				j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(addItemTable_Xpath)));
				
            	WebElement add_row = driver.findElement(By.xpath("//table//td[text()='Additional Covers']//following::a[text()='Add Bespoke Sum Insured'][1]"));
                add_row.click();
                
                WebElement element = driver.findElement(By.xpath("//table//td[text()='Additional Covers']//following::select[@name='cce_el_additional_covers']"));
         
                String activity = internal_data_map.get("AddBespSumInsEL").get(no).get("EL_ABSI_AddtionalCover").replaceAll("\u00A0", " ");
                customAssert.assertTrue(k.DropDownSelection_WebElement(element, activity),"Unable to select Additional covers in Public Liability Activities.");
                
                element = driver.findElement(By.xpath("//table//td[text()='Wageroll']//following::input[1]"));
                customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(element,internal_data_map.get("AddBespSumInsPL").get(no).get("PL_ABSI_Wageroll"),"Input"),"Error while entering Wageroll");
                customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"), "Unable to click on Inner button.");
                
                TestUtil.reportStatus("Additional Covers Bespoke sum insured <b> [ "+internal_data_map.get("AddBespSumInsPL").get(no).get("Automation Key")+" ] </b> is added succefully for Employers liability cover.", "Info", true);
                
                //Referral Code - 29
                if(activity.equalsIgnoreCase("Asbestos")){
                	common_HHAZ.referrals_list.add((String)map_data.get(El_referral_key+"AsbestosCover"));
                }
                
                //Referral Code - 30
                if(activity.contains("Polychlorinated Biphenyl - PCB")){
                	common_HHAZ.referrals_list.add((String)map_data.get(El_referral_key+"PCB"));
                }
                
                //Referral Code - 31	
                if(activity.contains("Offshore")){
                	common_HHAZ.referrals_list.add((String)map_data.get(El_referral_key+"Offshore"));
                }
                
                
                no++;
			}
            
            TestUtil.reportStatus("All the Additional Covers Bespoke sum insured are added succefully for Employers liability cover.", "Info", true);
            customAssert.assertTrue(k.DropDownSelection("CCD_EL_RIDDORClaims", (String)map_data.get("EL_RIDDORClaims")),"Unable to select RIDDOR Claims in Employers Liability .");
            
        
            	//Employee Wages Breakdown 
     			properties = ((String)map_data.get("EL_AddWagesBreakdown")).split(";");
                no_of_property = properties.length;
                elm = null;
                int noOfEmp_AllOther = 0,  noOfEmp_Drivers = 0, LE_wegroll = 0;
    	        for(int count=1;count<=no_of_property;count++){
    	        	String addItemTable_Xpath = "//table//td[text()='Employee Wages Breakdown']//following::a[text()='Add Row'][1]";
    				JavascriptExecutor j_exe = (JavascriptExecutor) driver;
    				j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(addItemTable_Xpath)));
    				
    	        	WebElement add_row = driver.findElement(By.xpath("//table//td[text()='Employee Wages Breakdown']//following::a[text()='Add Row'][1]"));
    	            add_row.click();
    	            
    	            elm = driver.findElements(By.xpath("//table//td[text()='Employee Wages Breakdown']//following::select[contains(@name,'ccc_el_activities')]"));
    	            //int index = getIndexDropdownBD(internal_data_map.get("Loss Of Rent").get(count-1).get("LOI_CoverName"));
    	            String activity = internal_data_map.get("AddEmpWages").get(count-1).get("EL_ACT_Activity").replaceAll("\u00A0", " ").trim();
    	            customAssert.assertTrue(k.DropDownSelection_WebElement(elm.get(count-1), activity),"Unable to select Activities in Public Liability Activities.");
    	            
    	            elm = driver.findElements(By.xpath("//table//td[text()='Employee Wages Breakdown']//following::*[contains(@name,'ccd_el_activity_desc')]"));
    	            customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("AddEmpWages").get(count-1).get("EL_ACT_DescriptionOfActivity"),"Input"),"Error while entering Description of activity");
    	            
    	            elm = driver.findElements(By.xpath("//table//td[text()='Employee Wages Breakdown']//following::*[contains(@name,'wageroll')]"));
    	            customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("AddEmpWages").get(count-1).get("EL_ACT_WageRollEmployees"),"Input"),"Error while entering Wageroll employees only (next 12 months)");
    	            LE_wegroll = LE_wegroll + Integer.parseInt(internal_data_map.get("AddEmpWages").get(count-1).get("EL_ACT_WageRollEmployees"));
    	            
    	            elm = driver.findElements(By.xpath("//table//td[text()='Employee Wages Breakdown']//following::*[contains(@name,'ccd_el_number_emp')]"));
    	            customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("AddEmpWages").get(count-1).get("EL_ACT_EmployeeCount"),"Input"),"Error while entering Employee Count.");
    	            
    	            customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button Employers Liability screen for activies .");
    	            
    	            //Referral Code - 32	
                    if(activity.equalsIgnoreCase("Height Work (ANY)")){
                    	common_HHAZ.referrals_list.add((String)map_data.get(El_referral_key+"HeightWork(ANY)"));
                    }
                    
                    //Referral Code - 33	
                    if(activity.equalsIgnoreCase("Height work in excess of 10 metres (on and away from the premises)")){
                    	common_HHAZ.referrals_list.add((String)map_data.get(El_referral_key+"HeightWorkInExcessOf10Metres"));
                    }
    	            
                    //Referal - Activity as per SUP-748
    	           if(is_EL_referral_activity(activity)){
    	        	   common_HHAZ.referrals_list.add("Employers Liability_Refer Activity - "+activity);
    	           } 
    	            
    	            TestUtil.reportStatus("Employee Wages Breakdown <b> [ "+internal_data_map.get("AddEmpWages").get(count-1).get("Automation Key")+" ] </b> is added succefully for Public liability cover.", "Info", true);
    	            
    	            if(activity.equalsIgnoreCase("Clerical")){
    	            	pas_NoOfEmp_Clerical = internal_data_map.get("AddEmpWages").get(count-1).get("EL_ACT_EmployeeCount");
    	            }else if(activity.equalsIgnoreCase("Drivers")){
    	            	noOfEmp_Drivers = noOfEmp_Drivers + Integer.parseInt(internal_data_map.get("AddEmpWages").get(count-1).get("EL_ACT_EmployeeCount"));
    	            }else{
    	            	noOfEmp_AllOther = noOfEmp_AllOther + Integer.parseInt(internal_data_map.get("AddEmpWages").get(count-1).get("EL_ACT_EmployeeCount"));
    	            }
    	            
                }
    	        
    	        if(noOfEmp_AllOther > 0){
    	        	 pas_NoOfEmp_AllOthers = String.valueOf(noOfEmp_AllOther);
    	        }else{
    	        	 pas_NoOfEmp_AllOthers = "0";
    	        }
    	        
    	        if(noOfEmp_Drivers > 0){
    	        	 pas_NoOfEmp_Drivers = String.valueOf(noOfEmp_Drivers);
	   	        }else{
	   	        	pas_NoOfEmp_Drivers = "0";
	   	        }
    	        
    	        TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Legal Expenses", (String)map_data.get("Automation Key"), "LE_TotalWages", String.valueOf(LE_wegroll), map_data);
    	        
    	        //LE_TotalWegroll = String.valueOf(LE_wegroll);
    	        
            //Add Additional cover add bespoke sum insured : 
            properties = ((String)map_data.get("EL_AddBespoke")).split(";");
            no_of_property = properties.length;
            elm = null;
            no = 0;
            while(no < no_of_property ){
            	
            	String addItemTable_Xpath = "//table//td[text()='Employee Wages Breakdown']//following::a[text()='Add Bespoke Item'][1]";
				JavascriptExecutor j_exe = (JavascriptExecutor) driver;
				j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(addItemTable_Xpath)));
				
            	WebElement add_row = driver.findElement(By.xpath("//table//td[text()='Employee Wages Breakdown']//following::a[text()='Add Bespoke Item'][1]"));
                add_row.click();
                
                WebElement element;
                
	            element = driver.findElement(By.xpath("//table//td[text()='Description ']//following::input[1]"));
                customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(element,internal_data_map.get("AddBespokeEL").get(no).get("EL_AddB_Desc"),"Input"),"Error while entering Wageroll");
                
                element = driver.findElement(By.xpath("//table//td[text()='Premium ']//following::input[1]"));
                customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(element,internal_data_map.get("AddBespokeEL").get(no).get("EL_AddB_Premium"),"Input"),"Error while entering Turnover");
                
                customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"), "Unable to click on Inner button.");
            	
                TestUtil.reportStatus("Bespoke <b> [ "+internal_data_map.get("AddBespokeEL").get(no).get("Automation Key")+" ] </b> is added succefully for Employers liability cover.", "Info", true);
                
            	no++;
            	
            }
            
            customAssert.assertTrue(k.Click("CCD_ApplyBookRate"), "Unable to click on apply book rate button.");
            customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, "Employers Liability", "EL_"), "issue in Validate_AutoRatedTables function for Employers Liability Cover");
			TestUtil.reportStatus("All activies are added and verified successfully on Employers Liability screen. ", "Info", true);
			
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
public boolean PersonalAccidentStandardPage(Map<Object, Object> map_data){
	boolean retValue = true;
	
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
		case "Requote":
			internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
			break;
		case "Rewind":
			internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
			break;
	
	}
	
	try{
		customAssert.assertTrue(common.funcPageNavigation("Personal Accident Standard", ""),"Personal Accident Standard page is having issue(S)");
	 	
		customAssert.assertTrue(k.DropDownSelection("CCD_ZurichMotorFleet", (String)map_data.get("PAS_ZurichMotorFleet")), "Unable to select Are all persons in good health value from Personal Accident Optional.");
        
        // Verify multiple activies : 
		String[] properties = ((String)map_data.get("EL_AddWagesBreakdown")).split(";");
        int no_of_property = properties.length;
        List<WebElement> elm = null, elm1=null;
        elm = null;
        int  no = 0;
        String actActivity=null;
        while(no < no_of_property ){

            
            elm = driver.findElements(By.xpath("//*[contains(@name,'ccc_el_activities-')]"));
            Select select = new Select(elm.get(no));
            WebElement tmp = select.getFirstSelectedOption();
            
            actActivity = tmp.getText().trim();              
            String expActivity = internal_data_map.get("AddEmpWages").get(no).get("EL_ACT_Activity");
            customAssert.assertEquals(actActivity, expActivity, "Actual Activity Name "+actActivity+" does not match with Expected Activity name "+expActivity+" ");
            
            elm = driver.findElements(By.xpath("//*[contains(@name,'ccd_el_number_emp')]"));
            String ActEmployeeCount = elm.get(no).getAttribute("value");
            String expEmployeeCount = internal_data_map.get("AddEmpWages").get(no).get("EL_ACT_EmployeeCount");
            customAssert.assertEquals(ActEmployeeCount, expEmployeeCount, "Actual Activity Name "+actActivity+" does not match with Expected Activity name "+expActivity+" ");
            
            TestUtil.reportStatus("Personal Accident Activity <b> [ "+internal_data_map.get("AddEmpWages").get(no).get("Automation Key")+" ] </b> is Verified succefully for Personal Accident Standard cover.", "Info", true);
            
        	no++;
        	
        }
            
       customAssert.assertTrue(k.Click("CCD_ApplyBookRate"), "Unable to click on apply book rate button.");
       customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, "Personal Accident Standard", "PAS_"), "issue in Validate_AutoRatedTables function for Personal Accident Standard Cover");
       TestUtil.reportStatus("All activies are added and verified successfully on Personal Accident Optional screen. ", "Info", true);
			
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

public boolean DeteriorationofStockPage(Map<Object, Object> map_data){
	boolean retValue = true;
	
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
		case "Requote":
			internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
			break;
		case "Rewind":
			internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
			break;
	
	}
	
	try{
		customAssert.assertTrue(common.funcPageNavigation("Deterioration of Stock", ""),"Deterioration of Stock page is having issue(S)");
		
		customAssert.assertTrue(k.Input("CCD_DOS_SumInsured", Keys.chord(Keys.CONTROL, "a")), "Unable to Enter Sum Insured on Deterioration of Stock Page.");
	    customAssert.assertTrue(k.Input("CCD_DOS_SumInsured", (String)map_data.get("DOS_SumInsured")), "Unable to Enter Sum Insured on Deterioration of Stock Page.");
	  
	    customAssert.assertTrue(k.DropDownSelection("CCD_DOS_TypeOfUnit", (String)map_data.get("DOS_TypeOfUnit")), "Unable to select type of Uni on Deterioration of Stock Page.");
	    
	    customAssert.assertTrue(k.Input("CCD_DOS_YearOfManufacture", Keys.chord(Keys.CONTROL, "a")), "Unable enter Year of Manufacture on Deterioration of Stock Page.");
	    customAssert.assertTrue(k.Input("CCD_DOS_YearOfManufacture", (String)map_data.get("DOS_YearOfManufacture")), "Unable enter Year of Manufacture on Deterioration of Stock Page.");

	    customAssert.assertTrue(k.DropDownSelection("CCD_DOS_TypeOfMaintenanceAgreement", (String)map_data.get("DOS_MaintanaceAgreement")), "Unable to select Type of manitanace on Deterioration of Stock Page.");
	    customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Deterioration of Stock page .");
	    
	    Calendar now = Calendar.getInstance();   // Gets the current date and time
	    int currentYear = now.get(Calendar.YEAR); 
	    
	    
	    //Referral Code - 36	
	    //int currentYear = Year.now().getValue();
        if((currentYear - Integer.parseInt((String)map_data.get("DOS_YearOfManufacture"))) > 10){
        	common_HHAZ.referrals_list.add((String)map_data.get("RM_DeteriorationofStock_Age>10Years"));
        }
      
       TestUtil.reportStatus("All activies are added and verified successfully on Deterioration of Stock screen. ", "Info", true);
			
       return retValue;
		 
	}catch(Throwable t){
		k.ImplicitWaitOn();
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     
        return false;
		}
	finally{
	 }
	
	}

public boolean GoodsinTransitPage(Map<Object, Object> map_data){
	boolean retValue = true;
	
	try{
		customAssert.assertTrue(common.funcPageNavigation("Goods in Transit", ""),"Goods in Transit page is having issue(S)");
	 	
		customAssert.assertTrue(k.Input("CCD_GIT_NumberOfVehicles", (String)map_data.get("GIT_NumberOfVehicles")), "Unable to enter Number of vehicles on Goods in Transit.");
        customAssert.assertTrue(k.Input("CCD_GIT_OneLoad", (String)map_data.get("GIT_OneLoad")), "Unable to enter Maximum Value Any One Load on Goods in Transit.");
        customAssert.assertTrue(k.Input("CCD_GIT_OneClaim", (String)map_data.get("GIT_OneClaim")), "Unable to enter Maximum Value Any One Claim on Goods in Transit.");
        
        //Referral Code - 36.1	
	    if(Double.parseDouble((String)map_data.get("GIT_OneLoad")) > 100000){
        	common_HHAZ.referrals_list.add((String)map_data.get("RM_GoodsInTransit_MaximumValueAnyOneLoad>100000"));
        }
        
        
        customAssert.assertTrue(k.Click("CCD_ApplyBookRate"), "Unable to click on apply book rate button.");
        customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, "Goods in Transit", "GIT_"), "issue in Validate_AutoRatedTables function for Goods In Transit Cover");
        TestUtil.reportStatus("All details are added and verified successfully on Goods in Transit screen. ", "Info", true);
			
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

public boolean LegalExpensesPage(Map<Object, Object> map_data){
	boolean retValue = true;
	
	try{
		customAssert.assertTrue(common.funcPageNavigation("Legal Expenses", ""),"Legal Expenses page is having issue(S)");
	 	
		customAssert.assertTrue(k.Click("CCD_ApplyBookRate"), "Unable to click on apply book rate button.");
		
		if(((String)map_data.get("CD_EmployersLiability")).equalsIgnoreCase("No")){
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Legal Expenses", (String)map_data.get("Automation Key"), "LE_TotalWages", "0.0", map_data);
		}
		
		//Referral Code - 37	
		double LE_total_WageRoll = Double.parseDouble((String)map_data.get("LE_TotalWages"));
		if(LE_total_WageRoll > 10000000){
		        common_HHAZ.referrals_list.add((String)map_data.get("RM_LegalExpenses_TotalOfWageroll>10000000"));
		}
		
		customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, "Legal Expenses", "LE_"), "issue in Validate_AutoRatedTables function for Legal Expenses Cover");
        TestUtil.reportStatus("All details are verified successfully on Legal Expenses screen. ", "Info", true);
			
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
		case "Requote":
			internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
			break;
		case "Rewind":
			internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
			break;
	
	}
	
	try{
		customAssert.assertTrue(common.funcPageNavigation("Terrorism", ""),"Terrorism page is having issue(S)");
		
		customAssert.assertTrue(k.DropDownSelection("CCD_TER_IsThisBankRequirement", (String)map_data.get("Ter_BankRequirement")), "Unable to select Is this Bank Requirement on Terrorism Page.");
	    
	    customAssert.assertTrue(k.DropDownSelection("CCD_TER_RequireCoverForSpecLocations", (String)map_data.get("Ter_CoverFor SpecLocation")), "Unable to select Required For Spec Location on Terrorism Page.");
	   
		customAssert.assertTrue(k.Click("CCD_ApplyBookRate"), "Unable to click on apply book rate button.");
	    customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Terrorism page .");   
      
	    customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, "Terrorism", "Ter_"), "issue in Validate_AutoRatedTables function for Terrorism Cover");
	    
       TestUtil.reportStatus("All activies are added and verified successfully on Terrorism screen. ", "Info", true);
			
       return retValue;
		 
	}catch(Throwable t){
		k.ImplicitWaitOn();
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     
        return false;
		}
	finally{
	 }
	
	}

public boolean funcBusinessInterruption(Map<Object, Object> map_data){
	boolean retValue = true;
		
		Map<String, List<Map<String, String>>> internal_data_map = null;
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
			
			customAssert.assertTrue(common.funcPageNavigation("Business Interruption", ""),"Business Interruption page navigations issue(S)");
			
		      if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	            	
		    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
		      }

			//Extensions
			customAssert.assertTrue(k.DropDownSelection("CCD_BI_CoverBasisDeclarationLinked", (String)map_data.get("BI_Declarationlinked")),"Unable to enter value in Cover Basis Declaration Linked.");
			//Basic Business Interruption
			String[] properties = ((String)map_data.get("BI_BBInterruption")).split(";");
            int no_of_property = properties.length;
            List<WebElement> elm = null;
            double BI_sumIns = 0.0;
           for(int count=1;count<=no_of_property;count++){
                
                WebElement add_row = driver.findElement(By.xpath("//*[contains(@id,'p6_table1')]//a[text()='Add Row']"));
                add_row.click();
                
                elm = driver.findElements(By.xpath("//*[text()='Basic Business Interruption']//following::*[contains(@name,'p6_cover')]"));
                
                String activity = internal_data_map.get("BI-BBI").get(count-1).get("BI_BBI_cover").replaceAll("\u00A0", " ").trim();
                //int index = getIndexDropdownBasicBICovers(internal_data_map.get("BI-BBI").get(count-1).get("BI_BBI_cover")); 
                customAssert.assertTrue(k.DropDownSelection_WebElement(elm.get(count-1), activity),"Unable to select Cover on BBI in BI Page.");
                
                elm = driver.findElements(By.xpath("//*[contains(@name,'p6_premises_name-')]"));
                customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("BI-BBI").get(count-1).get("BI_BBI_PremisesName"),"Input"),"Error while entering Premises Name");
                
                elm = driver.findElements(By.xpath("//*[contains(@name,'p6_sum_insured-')]"));
                customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("BI-BBI").get(count-1).get("BI_BBI_SumInsured"),"Input"),"Error while entering Sum Insured");
                
                BI_sumIns = Double.parseDouble(internal_data_map.get("BI-BBI").get(count-1).get("BI_BBI_SumInsured"));
                
                elm = driver.findElements(By.xpath("//*[contains(@name,'p6_indemnity_period_months-')]"));
                customAssert.assertTrue(k.DropDownSelection_WebElement(elm.get(count-1), internal_data_map.get("BI-BBI").get(count-1).get("BI_BBI_IP")),"Unable to enter Indemnity Period");
                
                String sVal = internal_data_map.get("BI-BBI").get(count-1).get("BI_BBI_IP");
                if(sVal.contains("12")){
                	BI_sumIns = BI_sumIns;                	
                }else if(sVal.contains("18")){
                	BI_sumIns = BI_sumIns*1.5;             	
                }else if(sVal.contains("24")){
                	BI_sumIns = BI_sumIns*2.0;             	
                }else if(sVal.contains("36")){
                	BI_sumIns = BI_sumIns*3.0;             	
                }
                
                Ter_BI_Sum = Ter_BI_Sum + BI_sumIns;
                
                customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Excesses page .");                
         }
        
            //BI Extensions
            elm = driver.findElements(By.xpath("//*[contains(@name,'ccd_bi_ext_sum_insured_ro')]"));          
            customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(0),(String)map_data.get("BI_Extensions_ContractSites"),"Input"),"Error while entering Contract Sites on BI page");
            customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(1),(String)map_data.get("BI_Extensions_ROI"),"Input"),"Error while entering Customers - Unspecified within Territorial Limits and ROI on BI page");
            customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(2),(String)map_data.get("BI_Extensions_DenialOfAccess"),"Input"),"Error while entering Denial Of Access on BI page");
            customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(3),(String)map_data.get("BI_Extensions_PublicUtilities"),"Input"),"Error while entering Public Utilities on BI page");
            customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(4),(String)map_data.get("BI_Extensions_SuppliersUnspecified"),"Input"),"Error while entering Suppliers - Unspecified on BI page");
            
            //Additional Extensions
            properties = ((String)map_data.get("BI_AdditionalExtensions")).split(";");
            no_of_property = properties.length;
           	elm = null;
             for(int count=1;count<=no_of_property;count++){
            	 
            	 String addItemTable_Xpath = "//*[text()='Additional Extensions']//following::*[text()='Add Row']";
 				JavascriptExecutor j_exe = (JavascriptExecutor) driver;
 				j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(addItemTable_Xpath)));
 				
                 WebElement add_row = driver.findElement(By.xpath("//*[text()='Additional Extensions']//following::*[text()='Add Row']"));
                add_row.click();
                
                elm = driver.findElements(By.xpath("//*[text()='Additional Extensions']//following::*[contains(@name,'ccd_bi_extension')]"));
                int index = getIndexDropdownAdditionalExtensions(internal_data_map.get("BI-AdditionalExt").get(count-1).get("BI_AE_Extension")); 
                customAssert.assertTrue(k.DropDownSelection_WebElement(elm.get(count-1), String.valueOf(index)),"Unable to select Additional Extensions on BI page.");
                
                //Referral code - 27
                if(is_BI_referral_activity(internal_data_map.get("BI-AdditionalExt").get(count-1).get("BI_AE_Extension"))){
                	common_HHAZ.referrals_list.add((String)map_data.get("RM_BusinessInterruption_BIExtensionsOutsideUK"));
                }
                
                
                elm = driver.findElements(By.xpath("//*[text()='Additional Extensions']//following::*[contains(@name,'ccd_bi_ext_sum_insured')]"));
                customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("BI-AdditionalExt").get(count-1).get("BI_AE_SumInsured"),"Input"),"Error while entering Sum Insured n BI Page.");
                
                elm = driver.findElements(By.xpath("//*[text()='Additional Extensions']//following::*[contains(@name,'ccd_bi_ext_indemnity_period')]"));
                customAssert.assertTrue(k.DropDownSelection_WebElement(elm.get(count-1), internal_data_map.get("BI-AdditionalExt").get(count-1).get("BI_AE_IP")),"Unable to enter EXS_ExcessApplies in Excesses table");
                 
                customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Excesses page .");                
         }
                        
          //Book Debts
           // customAssert.assertTrue(k.Input("CCD_BI_SumInsuredBookDebts", Keys.chord(Keys.CONTROL, "a")),"Unable to  select  Sum Insured. ");
			//customAssert.assertTrue(k.Input("CCD_BI_SumInsuredBookDebts", (String)map_data.get("BI_SumInsured_BookDebts")),"Unable to enter value in  Sum Insured. ");
			 
			properties = ((String)map_data.get("BI_AddBespoke")).split(";");
            no_of_property = properties.length;
            elm = null;
            int no = 0;
            while(no < no_of_property ){
            	String addItemTable_Xpath = "//*[text()='Book Debts']//following::a[text()='Add Bespoke Item']";
				JavascriptExecutor j_exe = (JavascriptExecutor) driver;
				j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(addItemTable_Xpath)));
				
            	WebElement add_row = driver.findElement(By.xpath("//*[text()='Book Debts']//following::a[text()='Add Bespoke Item']"));
                add_row.click();
                
                WebElement element;
                
	            element = driver.findElement(By.xpath("//table//td[text()='Description ']//following::input[1]"));
                customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(element,internal_data_map.get("AddBespokeBI").get(no).get("BI_AddB_Description"),"Input"),"Error while entering Wageroll");
                
                element = driver.findElement(By.xpath("//table//td[text()='Premium ']//following::input[1]"));
                customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(element,internal_data_map.get("AddBespokeBI").get(no).get("BI_AddB_Premium"),"Input"),"Error while entering Turnover");
                
                customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"), "Unable to click on Inner button.");
            	
                TestUtil.reportStatus("Bespoke <b> [ "+internal_data_map.get("AddBespokeBI").get(no).get("Automation Key")+" ] </b> is added succefully for Business Interruption cover.", "Info", true);
                
            	no++;
            	
            }
            customAssert.assertTrue(k.Click("CCD_ApplyBookRate"), "Unable to click on apply book rate button.");                        				
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
			customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, "Business Interruption", "BI_"), "issue in Validate_AutoRatedTables function for BI Cover");
			
			TestUtil.reportStatus("Business Interruption details are filled successfully . ", "Info", true);
			return retValue;
			
		}catch(Throwable t){
			return false;
		}
		}




//*************************START***********************************//
	//************************************************************//
	//**** CCD Book Rate Calculator Based on QBECC Sheet ********//
	//************************************************************//
	//************************************************************//
	
	public double get_Book_Rate_from_Properties(String _Activity,String wage_Roll,String cover,String wage_turnover_code){
		
		double r_value=0.0;
		DecimalFormat formatter = new DecimalFormat("###.###");
		//Drain_Rodding_–_incl._repair
		try{
			CCD_Rater = OR.getORProperties();
			String t_activity = _Activity.replaceAll("–", "").replaceAll(" ", "_").replaceAll(",", "").replaceAll("__", "_");
			t_activity = t_activity + "_"+cover+"_BR_"+wage_turnover_code+"_"+wage_Roll;
			
			//Bio_Fuels_Manual_EL_BR_W_100k
		
			r_value = Double.parseDouble(CCD_Rater.getProperty(t_activity));
			r_value = Double.valueOf(formatter.format(r_value));
		
		}catch(Throwable t ){
			//System.out.println("Error while getting Book rate for activity > "+_Activity+" < "+t.getMessage());
		}
		return r_value;
			
	}
	
	public double get_Book_Rate_from_Properties_BI(String _Activity,String limit_format){
		
		double r_value=0.0;
		DecimalFormat formatter = new DecimalFormat("###.###");
		try{
			
			CCD_Rater = OR.getORProperties();
		
			String t_activity = _Activity.replaceAll("–", "").replaceAll(" ", "_").replaceAll(",", "").replaceAll("__", "_");
			t_activity = t_activity + "_"+limit_format+"_BI_BR";
		
			r_value = Double.parseDouble(CCD_Rater.getProperty(t_activity));
			r_value = Double.valueOf(formatter.format(r_value));
		
		}catch(Throwable t ){
			System.out.println("Error while getting Book rate for activity > "+_Activity+" < "+t.getMessage());
		}
		return r_value;
			
	}
	
	public double get_Book_Rate_from_Properties_MD_Buildings(String _Activity){
		
		double r_value=0.0;
		DecimalFormat formatter = new DecimalFormat("###.###");
		try{
			
			CCD_Rater = OR.getORProperties();
		
			String t_activity = _Activity.replaceAll("–", "").replaceAll("-", "").replaceAll(" ", "_").replaceAll(",", "").replaceAll("__", "_");
			t_activity = t_activity + "_MD_BuildingRates_BR";
		
			r_value = Double.parseDouble(CCD_Rater.getProperty(t_activity));
			r_value = Double.valueOf(formatter.format(r_value));
		
		}catch(Throwable t ){
			//System.out.println("Error while getting Book rate for activity in get_Book_Rate_from_Properties_MD_Buildings > "+_Activity+" < "+t.getMessage());
		}
		return r_value;
			
	}
	
	public double get_Book_Rate_from_Properties_MONEY(String _Activity,int initial_rate){
		
		double r_value=0.0;
		DecimalFormat formatter = new DecimalFormat("###.###");
		
		
		try{
			CCD_Rater = OR.getORProperties();
			String t_activity = null;
			
			if(_Activity.contains("Any other loss"))
				t_activity = "Cash_In_Safe/Transit_in_excess_of_1000_MONEY_BR";
			else if(_Activity.contains("Limit of cash")){
				if(initial_rate > Integer.parseInt(CCD_Rater.getProperty("Limit_of_cash_in_locked_safe_MONEY_Default_Value")))
					t_activity = "Cash_In_Safe/Transit_in_excess_of_1000_MONEY_BR";
				else
					return 0.0;
			}
			else if(_Activity.contains("Estimated own"))
				t_activity = "Estimated_annual_cash_carryings_MONEY_BR";
			
			
		
			r_value = Double.parseDouble(CCD_Rater.getProperty(t_activity));
			r_value = Double.valueOf(formatter.format(r_value));
		
		}catch(Throwable t ){
			//System.out.println("Error while getting Book rate for activity > "+_Activity+" < "+t.getMessage());
		}
		return r_value;
			
	}
	
	public double get_Book_Rate_from_Properties_GIT(String _Activity){
		
		double r_value=0.0;
		DecimalFormat formatter = new DecimalFormat("###.###");
		//Drain_Rodding_–_incl._repair
		try{
			CCD_Rater = OR.getORProperties();
			String t_activity = _Activity.replaceAll("–", "").replaceAll(" ", "_").replaceAll(",", "").replaceAll("__", "_");
			t_activity = t_activity + "_GIT_BR";
			
			//Maximum_any_one_load_GIT_BR
		
			r_value = Double.parseDouble(CCD_Rater.getProperty(t_activity));
			r_value = Double.valueOf(formatter.format(r_value));
		
		}catch(Throwable t ){
			System.out.println("Error while getting Book rate for activity > "+_Activity+" < "+t.getMessage());
		}
		return r_value;
			
	}
	
	public double get_Book_Rate_from_Properties_PAS(String _Activity,String isZMF){
		
		double r_value=0.0;
		DecimalFormat formatter = new DecimalFormat("###.###");
		//Drain_Rodding_–_incl._repair
		try{
			CCD_Rater = OR.getORProperties();
			String t_activity = _Activity.replaceAll("–", "").replaceAll(" ", "_").replaceAll(",", "").replaceAll("__", "_");
			if(isZMF.equalsIgnoreCase("Yes"))
				t_activity = t_activity + "_PAS_ZMF_BR";
			else
				t_activity = t_activity + "_PAS_BR";
			
			//Clerical_PAS_ZMF_BR=2.76
			
			r_value = Double.parseDouble(CCD_Rater.getProperty(t_activity));
			r_value = Double.valueOf(formatter.format(r_value));
		
		}catch(Throwable t ){
			//System.out.println("Error while getting Book rate for activity > "+_Activity+" < "+t.getMessage());
		}
		return r_value;
			
	}
	
	public double get_Book_Rate_from_Properties_TER(String _Activity,String cover){
		
		double r_value=0.0;
		DecimalFormat formatter = new DecimalFormat("###.###");
		Map<Object,Object> data_map = new HashMap<>();
		
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
		//TER_BI_BR=0.0198
		//TER_ZoneA_MD_BR
		try{
			CCD_Rater = OR.getORProperties();
			String t_activity = _Activity.replaceAll("–", "").replaceAll(" ", "_").replaceAll(",", "").replaceAll("__", "_");
			
			if(cover.equalsIgnoreCase("MD")){
				String ter_Zone = (String)data_map.get("PoD_TerrorismArea");
				t_activity = "TER_Zone"+ter_Zone+"_"+cover+ "_BR";
			}else{
				t_activity = "TER_"+cover+ "_BR";
			}
			
			r_value = Double.parseDouble(CCD_Rater.getProperty(t_activity));
			r_value = Double.valueOf(formatter.format(r_value));
		
		}catch(Throwable t ){
			//System.out.println("Error while getting Book rate for activity > "+_Activity+" < "+t.getMessage());
		}
		return r_value;
			
	}
	
	public double get_highest_MD_Buildings_Occupancy_Rate_(){
		
		double occupancy_rate=0.0,max_rate=0.0;
		try{
			
			if(common_CCD.MD_Building_Occupancies_list.size() > 0){
				for(String md_occupancy : common_CCD.MD_Building_Occupancies_list){
					
					occupancy_rate = get_Book_Rate_from_Properties_MD_Buildings(md_occupancy);
					if(occupancy_rate > max_rate)
						max_rate = occupancy_rate;
				}
			}else{
				max_rate = 0.00;
			}
			
			
		}catch(Throwable t){
			System.out.println("Error in function func_get_highest_MD_Buildings_Occupancy_Rate_--> "+t.getMessage());
		}
		
		return max_rate;
		
	}
	
	public double get_BI_Book_Rate(String bi_Activity){
		
		try{
		double book_rate = 0.0;
		DecimalFormat formatter = new DecimalFormat("###.###");
		
		if(is_BI_Additional_Extensions("BI", bi_Activity)) {//For BI - Additional Extensions section
			//System.out.println("Book Rate of "+bi_Activity + " = "+book_rate);
			common_CCD.Book_rate_Rater_output.put("BI"+"_"+bi_Activity, book_rate);
			return 0.0;
		}
		//Basic Business Interruption
		if(bi_Activity.contains("Declaration Linked") || bi_Activity.contains("Flexible Limit of Loss") || bi_Activity.contains("Gross Profit") || 
				bi_Activity.contains("Gross Revenue")){
			//This Rate is depends on MD Buildings Occupancy Rate
			book_rate = get_highest_MD_Buildings_Occupancy_Rate_();
			
			//System.out.println("Book Rate of "+bi_Activity + " = "+book_rate);
			common_CCD.Book_rate_Rater_output.put("BI"+"_"+bi_Activity, book_rate);
			
			return book_rate;
			
		}else if(is_BI_Extensions("BI", bi_Activity)) //For BI - Extensions section
		{ 
			CCD_Rater = OR.getORProperties();
			
			String t_activity = bi_Activity.replaceAll(" – ", " ").replaceAll("-", "").replaceAll(" ", "_").replaceAll(",", "").replaceAll("__", "_");
			t_activity = t_activity  +"_BI_BR";
		
			book_rate = Double.parseDouble(CCD_Rater.getProperty(t_activity));
			book_rate = Double.valueOf(formatter.format(book_rate));
		
			
		}else{
		
			String _sum_insured = get_BI_Limit_format("BI",bi_Activity);
		
			book_rate = get_Book_Rate_from_Properties_BI(bi_Activity,_sum_insured);
		
		}
		
		//System.out.println("Book Rate of "+bi_Activity + " = "+book_rate);
		common_CCD.Book_rate_Rater_output.put("BI"+"_"+bi_Activity, book_rate);
		return book_rate;
		}catch(Throwable t){
			//System.out.println("Error while calculating BI Book rate for activity --> "+bi_Activity);
			return 0;
		}
			
	}
	
			//For EL- Additional Extensions
			public boolean is_EL_Additional_Cover(String cover,String el_Activity)
			{
				
				CCD_Rater = OR.getORProperties();
				int f=0;
				String el_add_cover = CCD_Rater.getProperty("EL_AdditionalCovers_List");
				String[] list_cov = el_add_cover.split(":");
				
				for(String bi_ext : list_cov){
					if(bi_ext.equalsIgnoreCase(el_Activity))
					{
						f=1;
						return true;
					}
				}
				if(f==0){
					return false;
				}
				return false;
					
			}
			
			//For EL- Additional Extensions
			public boolean is_EL_No_Rate_Activity_Refer(String cover,String el_Activity)
			{
				
				CCD_Rater = OR.getORProperties();
				int f=0;
				String el_add_cover = CCD_Rater.getProperty("EL_Refer_No_Rate_Activities");
				String[] list_cov = el_add_cover.split(":");
				
				for(String bi_ext : list_cov){
					if(bi_ext.equalsIgnoreCase(el_Activity))
					{
						f=1;
						return true;
					}
				}
				if(f==0){
					return false;
				}
				return false;
					
			}
			
			//For PL - Refer Activities
			public boolean is_PL_Refer_Activity(String cover,String pl_Activity)
			{
				
				CCD_Rater = OR.getORProperties();
				int f=0;
				String pl_refer = CCD_Rater.getProperty("PL_Refer_No_Rate_Activities");
				String[] list_refer = pl_refer.split(":");
				
				for(String pl_act : list_refer){
					if(pl_act.equalsIgnoreCase(pl_Activity))
					{
						f=1;
						return true;
					}
				}
				if(f==0){
					return false;
				}
				return false;
					
			}

	public double get_EL_Book_Rate(String el_Activity){
		
		double book_rate = 0.0;
		
		
		String _wageRoll = get_WageRoll_format("EL",el_Activity);
		
		book_rate = get_Book_Rate_from_Properties(el_Activity,_wageRoll,"EL","W");
		
		//System.out.println("Book Rate of "+el_Activity + " = "+book_rate);
		common_CCD.Book_rate_Rater_output.put("EL"+"_"+el_Activity, book_rate);
		return book_rate;
			
	}
	
	public double get_PL_Book_Rate(String pl_Activity,String category){
		
		double book_rate = 0.0;
		String _TO_W=null;
		String _wageRoll_T = null;
		double pl_bonafide_cent = 0.0; 
		CCD_Rater = OR.getORProperties();
		
		_TO_W = get_WageRoll_Turnover_string("PL", pl_Activity);
		if(_TO_W.equalsIgnoreCase("W"))
			_wageRoll_T = get_WageRoll_format("PL",pl_Activity);
		else
			_wageRoll_T = get_Turnover_format("PL",pl_Activity);
		
		if(category.contains("BF")){
			book_rate = get_Book_Rate_from_Properties(pl_Activity,_wageRoll_T,"PL",_TO_W);
			pl_bonafide_cent = Double.parseDouble(CCD_Rater.getProperty("PL_Bonafide_Cent_Rate"));
			book_rate = (book_rate * (pl_bonafide_cent/100));
		}else{
			book_rate = get_Book_Rate_from_Properties(pl_Activity,_wageRoll_T,"PL",_TO_W);
		}
		
		//System.out.println("Book Rate of "+pl_Activity + " = "+book_rate);
		common_CCD.Book_rate_Rater_output.put("PL"+"_"+pl_Activity, book_rate);
		return book_rate;
			
	}
	public double get_PAS_Book_Rate(String pas_Activity){
		
		double book_rate = 0.0;
		
		try{
		
			String isZMFP = k.GetDropDownSelectedValue("CCD_PAS_Zurich_MFP");
			book_rate = get_Book_Rate_from_Properties_PAS(pas_Activity,isZMFP);
		
			//System.out.println("Book Rate of "+pas_Activity + " = "+book_rate);
			common_CCD.Book_rate_Rater_output.put("PAS"+"_"+pas_Activity, book_rate);
			return book_rate;
			
		}catch(Throwable t){
			//System.out.println("Error while calculating PAS_Book_Rate"+ t.getMessage());
			return 0;
		}
			
	}
	public double get_TER_Book_Rate(String ter_Activity){
		
		double book_rate = 0.0;
		
		try{
			switch(ter_Activity){
			
				case "Business Interruption":
					book_rate = get_Book_Rate_from_Properties_TER(ter_Activity,"BI");
				break;
				case "Buildings and Contents":
					book_rate = get_Book_Rate_from_Properties_TER(ter_Activity,"MD");
				break;
				default: 
					//System.out.println("Terrorism Activity is not in scope");
				break;
			}
			
			//System.out.println("Book Rate of "+ter_Activity + " = "+book_rate);
			common_CCD.Book_rate_Rater_output.put("TER_"+ter_Activity, book_rate);
			return book_rate;
			
		}catch(Throwable t){
			//System.out.println("Error while calculating Terrorism book rate"+ t.getMessage());
			return 0;
		}
			
	}
	public double get_GIT_Book_Rate(String git_Activity){
		
		double book_rate = 0.0;
			
		book_rate = get_Book_Rate_from_Properties_GIT(git_Activity);
		
		//System.out.println("Book Rate of "+git_Activity + " = "+book_rate);
		common_CCD.Book_rate_Rater_output.put("GIT"+"_"+git_Activity, book_rate);
		return book_rate;
			
	}
	
	public double get_Money_Book_Rate(String money_Activity,int initial_rate){
		
		double book_rate = 0.0;
			
		book_rate = get_Book_Rate_from_Properties_MONEY(money_Activity,initial_rate);
		
		//System.out.println("Book Rate of "+money_Activity + " = "+book_rate);
		common_CCD.Book_rate_Rater_output.put("MONEY"+"_"+money_Activity, book_rate);
		return book_rate;
			
	}
	
	//For PL cover
	public String get_WageRoll_Turnover_string(String cover,String pl_Activity){
		
		try{
		
		CCD_Rater = OR.getORProperties();
		int f=0;
		String pl_act_1 = CCD_Rater.getProperty("PL_TurnOver_Based_Activitis_1");
		String pl_act_2 = CCD_Rater.getProperty("PL_TurnOver_Based_Activitis_2");
		
		String[] list_act_1 = pl_act_1.split(":");
		String[] list_act_2 = pl_act_2.split(":");
		
		
		List<String> abc = new ArrayList<>(Arrays.asList(list_act_1));
		abc.addAll(Arrays.asList(list_act_2));
	
		for(String bi_ext : abc){
			if(bi_ext.equalsIgnoreCase(pl_Activity))
			{
				f=1;
				return "TO";
			}
		}
		if(f==0){
			return "W";
		}
		
		}catch(Throwable t){
			//System.out.println("Error while getting PL Turnover/Wageroll string for activity --> "+pl_Activity);
			return "";
		}
		
		return "";
			
	}
	
		//For BI- Additional Extensions
		public boolean is_BI_Additional_Extensions(String cover,String bi_Activity)
		{
			
			CCD_Rater = OR.getORProperties();
			int f=0;
			String bi_add_ext = CCD_Rater.getProperty("BI_Additional_Extensions");
			String[] list_ext = bi_add_ext.split(":");
			
			for(String bi_ext : list_ext){
				if(bi_ext.equalsIgnoreCase(bi_Activity))
				{
					f=1;
					return true;
				}
			}
			if(f==0){
				return false;
			}
			return false;
				
		}
		
		//For BI- Additional Extensions
			public boolean is_BI_Extensions(String cover,String bi_Activity)
				{
					
					CCD_Rater = OR.getORProperties();
					int f=0;
					String bi_add_ext = CCD_Rater.getProperty("BI_Extensions");
					String[] list_ext = bi_add_ext.split(":");
					
					for(String bi_ext : list_ext){
						if(bi_ext.equalsIgnoreCase(bi_Activity))
						{
							f=1;
							return true;
						}
					}
					if(f==0){
						return false;
					}
					return false;
						
				}
	
	public String get_WageRoll_format(String cover,String el_Activity){
		
		String _wageRoll = null;
		int wage_Roll=0,_limit_wage=0;
		wage_Roll = get_Wageroll_value(cover, el_Activity);
	
		if(wage_Roll <= 100000)
			_limit_wage = 100000;
		else if(wage_Roll > 100000 && wage_Roll <= 250000)
			_limit_wage = 250000;
		else if(wage_Roll > 250000 && wage_Roll < 500000)
			_limit_wage = 500000;
		else{
			_limit_wage = 500000;
			_wageRoll = "_Above_"+_limit_wage/1000 + "k";
			return _wageRoll;
		}
		
		_wageRoll = _limit_wage/1000 + "k";
		
		return _wageRoll;
			
	}
	
	public String get_BI_Limit_format(String cover,String bi_Activity){
		
		String _limit = null;
		int f_limit=0,_limit_=0;
		_limit_ = get_Sum_Insured_value(cover, bi_Activity);
		
		if(bi_Activity.contains("Book Debts")){
			
			if(_limit_ <= 100000)
				f_limit = 100000;
			else if(_limit_ > 100000 && _limit_ <= 250000)
				f_limit = 250000;
			else if(_limit_ > 250000){
				f_limit = 250000;
				_limit = "Above_"+f_limit/1000 + "k";
				return _limit;
			}
			
			
		}else{
	
			if(_limit_ <= 50000)
				f_limit = 50000;
			else if(_limit_ > 50000 && _limit_ <= 100000)
				f_limit = 100000;
			else if(_limit_ > 100000){
				f_limit = 100000;
				_limit = "Above_"+f_limit/1000 + "k";
				return _limit;
			}
		
		}
		
		_limit = f_limit/1000 + "k";
		
		return _limit;
			
	}
	
	//For PL
	public String get_Turnover_format(String cover,String pl_Activity){
		
		String turnover = null;
		int turnover_=0,_limit_turnover=0;
		turnover_ = get_Turnover_value(cover, pl_Activity);
	
		if(turnover_ <= 500000)
			_limit_turnover = 500000;
		else if(turnover_ > 500000 && turnover_ <= 1000000)
			_limit_turnover = 1000000;
		else if(turnover_ > 1000000 && turnover_ <= 2000000)
			_limit_turnover = 2000000;
		else if(turnover_ > 2000000 && turnover_ <= 5000000){
			_limit_turnover = 5000000;
		}else if(turnover_ > 5000000 && turnover_ <= 10000000){
			_limit_turnover = 10000000;
		}else if(turnover_ > 10000000 && turnover_ <= 20000000){
			_limit_turnover = 20000000;
		}else if(turnover_ > 0000000 && turnover_ <= 50000000){
			_limit_turnover = 50000000;
		}else if(turnover_ > 50000000 && turnover_ <= 100000000){
			_limit_turnover = 100000000;
		}else{
			_limit_turnover = 100000000;
			turnover = "_Above_"+_limit_turnover/1000 + "k";
			return turnover;
		}
		
		turnover = _limit_turnover/1000 + "k";
		
		return turnover;
			
	}
	
	

/**
* 
* This method Calculates Book Rate for CCD Autorated Covers [BI, Money, EL , PL, GIT, PAS, TER].
* 
*/
public boolean calculate_Book_Rate(String coverName){
		
		try{
			
		String desc=null;
		int i_rate=0;
			
		switch(coverName){
		
		case "BI": //Business Interruption
			
			List<String> bi_activites = get_Activities_from_Table("Business Interruption");
			for(String activity : bi_activites){
				if(activity.contains("Contract Sites") || activity.contains("Loss Of Attraction") 
						||  activity.isEmpty()) {
					common_CCD.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
					continue;
				}else{
					get_BI_Book_Rate(activity);
					
				}
			}
			TestUtil.reportStatus("Book Rate for Employers Liability Cover calculated Successfully by rater . ", "Info", false);
			//System.out.println(common_CCD.Book_rate_Rater_output);
			break;
		
		case "MONEY": //Money & Assault
			
			List<String> money_activites = get_Activities_from_Table("Description");
			
			for(String activity : money_activites){
				i_rate = get_Initial_Rate_value(coverName,activity);
				if(activity.isEmpty()) {
					common_CCD.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
					continue;
				}else{
					get_Money_Book_Rate(activity,i_rate);
					
				}
			}
			TestUtil.reportStatus("Book Rate for Money & Assault Cover calculated Successfully by rater . ", "Info", false);
			
			break;
			case "EL": //Employers Liability
			
			List<String> el_activites = get_Activities_from_Table("Activity");
			for(String activity : el_activites){
					//is_EL_Additional_Cover(coverName,activity);
				if(is_EL_Additional_Cover(coverName,activity) || is_EL_No_Rate_Activity_Refer(coverName,activity) || activity.isEmpty()){
						common_CCD.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
					continue;
				}else{
					get_EL_Book_Rate(activity);
					
				}
			}
			TestUtil.reportStatus("Book Rate for Employers Liability Cover calculated Successfully by rater . ", "Info", false);
			
			break;
			
			case "PL": //Public Liability
				
				List<String> pl_activites = get_Activities_from_Table("Activity");
				
				for(String activity : pl_activites){
					desc = get_Description_from_Table(activity);
					if(is_PL_Refer_Activity(coverName,activity) || desc.contains("AC") || !desc.contains("Public Liability") || activity.isEmpty()) {
						common_CCD.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
						continue;
					}else if(desc.contains("BF")){
						get_PL_Book_Rate(activity,desc);
					}
					else{
						get_PL_Book_Rate(activity,desc);
						
					}
				}
				TestUtil.reportStatus("Book Rate for Public Liability Cover calculated Successfully by rater . ", "Info", false);
				
				break;
				
			case "PAS": //Personal Accident Standard
				
				List<String> pas_activites = get_Activities_from_Table("Activity");
			
				for(String activity : pas_activites){
					
					if(activity.isEmpty()) {
						common_CCD.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
						continue;
					}else{
						get_PAS_Book_Rate(activity);
						
					}
				}
				TestUtil.reportStatus("Book Rate for Personal Accident Standard Cover calculated Successfully by rater . ", "Info", false);
				
				break;
				
			case "GIT": //Goods in Transit
				
				
				List<String> git_activites = get_GIT_Activities_from_Table();
				
				for(String activity : git_activites){
					
					if(activity.isEmpty()) {
						common_CCD.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
						continue;
					}else{
						get_GIT_Book_Rate(activity);
						
					}
				}
				TestUtil.reportStatus("Book Rate for Goods In Transit Cover calculated Successfully by rater . ", "Info", false);
				
				break;
				
			case "TER": //Terrorism
				
				List<String> ter_activites = get_Activities_from_Table("Description");
			
				for(String activity : ter_activites){
					
					if(activity.isEmpty()) {
						common_CCD.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
						continue;
					}else if(activity.contains("Buildings")) {
						continue; // BA Will Put Book Rate directly like MD
					}else{
						get_TER_Book_Rate(activity);
						
					}
				}
				TestUtil.reportStatus("Book Rate for Terrorism Cover calculated Successfully by rater . ", "Info", false);
				
				break;
				
						
		
		}	
			
		
		}catch(Throwable t){
			return false;
		}
		
		
		//System.out.println("Book Rate lookup Outputs = "+common_COB.Book_rate_Rater_output);
		return true;
		
	}

//For Legal Expenses
public double calculate_LE_Book_Premium(Map<Object,Object> data_map){
	try{
		
		
		double book_premium=0.0;
		String wage_range= null;
		
		double LE_total_WageRoll = Double.parseDouble((String)data_map.get("LE_TotalWages"));
		
		CCD_Rater = OR.getORProperties();
		
		
		if(LE_total_WageRoll <= 100000)
			wage_range = "100000";
		else if(LE_total_WageRoll > 100000 && LE_total_WageRoll <= 250000)
			wage_range = "250000";
		else if(LE_total_WageRoll > 250000 && LE_total_WageRoll <= 500000)
			wage_range = "500000";
		else if(LE_total_WageRoll > 500000 && LE_total_WageRoll <= 1000000)
			wage_range = "1000000";
		else if(LE_total_WageRoll > 1000000 && LE_total_WageRoll <= 5000000)
			wage_range = "5000000";
		else if(LE_total_WageRoll > 5000000 && LE_total_WageRoll <= 10000000)
			wage_range = "10000000";
		else if(LE_total_WageRoll > 10000000)
			wage_range = "Above_10000000";
		
		book_premium = Double.parseDouble(CCD_Rater.getProperty("LE_"+wage_range+"_BP"));
		
		return book_premium;
		
	}catch(Throwable t){
		//System.out.println("Error while calculating book Premium for Legal E");
		return 0.0;
	}
}

//For EL 
public int get_Wageroll_value(String coverName,String activity){
	
	
	int section_head_index = 0,wageroll_head_index = 0;
	String wageRoll = null;
	boolean isTrue1=false,isTrue2=false;

	try{
		
	//customAssert.assertTrue(common.funcPageNavigation("Employers Liability", ""),"Employers Liability page not loaded");
		
	
	String ratingTable_xpath = "//*[text()='Apply Book Rates']//following::table";
	WebElement ratingTable = driver.findElement(By.xpath(ratingTable_xpath));
	
	k.ScrollInVewWebElement(ratingTable);
	
	int _tble_Headers = driver.findElements(By.xpath("//*[text()='Apply Book Rates']//following::table//thead//th")).size();
	for(int row = 1; row < _tble_Headers ;row ++){
		
		if(isTrue1 && isTrue2)
			break;
		
		WebElement sec_Val = driver.findElement(By.xpath(ratingTable_xpath+"//thead//th["+row+"]"));
		if(sec_Val.getText().equalsIgnoreCase("Activity")){
			section_head_index = row;
			isTrue1=true;
		}
		if(sec_Val.getText().equalsIgnoreCase("Wageroll")){
			wageroll_head_index = row;
			isTrue2=true;
		}
	}

	int _tble_Rows = driver.findElements(By.xpath("//*[text()='Apply Book Rates']//following::table//tbody//tr")).size();
	
	for(int row = 1; row < _tble_Rows ;row ++){
		
		WebElement activity_Val = driver.findElement(By.xpath(ratingTable_xpath+"//tbody//tr["+row+"]//td["+section_head_index+"]"));
		if(activity_Val.getText().equals(activity)){
			WebElement wage_ = driver.findElement(By.xpath(ratingTable_xpath+"//tbody//tr["+row+"]//td["+wageroll_head_index+"]"));
			wageRoll = wage_.getText();
				break;
		}else{
			continue;
		}
		
	}
	
	
	}catch(Throwable t){
		//System.out.println("Error while getting Wage Roll for cover >"+coverName+"< - "+t);
	}
	return Integer.parseInt(wageRoll.replaceAll(",", ""));
}

//For BI 
public int get_Sum_Insured_value(String coverName,String activity){
	
	
	int section_head_index = 0,sum_head_index = 0;
	String sum = null;
	boolean isTrue1=false,isTrue2=false;

	try{
		
	customAssert.assertTrue(common.funcPageNavigation("Business Interruption", ""),"Business Interruption page not loaded");
		
	
	String ratingTable_xpath = "//*[text()='Apply Book Rates']//following::table";
	WebElement ratingTable = driver.findElement(By.xpath(ratingTable_xpath));
	
	k.ScrollInVewWebElement(ratingTable);
	
	int _tble_Headers = driver.findElements(By.xpath("//*[text()='Apply Book Rates']//following::table//thead//th")).size();
	for(int row = 1; row < _tble_Headers ;row ++){
		
		if(isTrue1 && isTrue2)
			break;
		
		WebElement sec_Val = driver.findElement(By.xpath(ratingTable_xpath+"//thead//th["+row+"]"));
		if(sec_Val.getText().equalsIgnoreCase("Business Interruption")){
			section_head_index = row;
			isTrue1=true;
		}
		if(sec_Val.getText().equalsIgnoreCase("Sum Insured")){
			sum_head_index = row;
			isTrue2=true;
		}
	}

	int _tble_Rows = driver.findElements(By.xpath("//*[text()='Apply Book Rates']//following::table//tbody//tr")).size();
	
	for(int row = 1; row < _tble_Rows ;row ++){
		
		WebElement activity_Val = driver.findElement(By.xpath(ratingTable_xpath+"//tbody//tr["+row+"]//td["+section_head_index+"]"));
		if(activity_Val.getText().equals(activity)){
			WebElement sum_ = driver.findElement(By.xpath(ratingTable_xpath+"//tbody//tr["+row+"]//td["+sum_head_index+"]"));
			sum = sum_.getText();
				break;
		}else{
			continue;
		}
		
	}
	
	
	}catch(Throwable t){
		//System.out.println("Error while getting Sum Insured for cover >"+coverName+"< - "+t);
	}
	return Integer.parseInt(sum.replaceAll(",", ""));
}


public int get_Turnover_value(String coverName,String activity){
	
	
	int section_head_index = 0,turnover_head_index = 0;
	String turnover = null;
	boolean isTrue1=false,isTrue2=false;

	try{
		
	customAssert.assertTrue(common.funcPageNavigation("Public Liability", ""),"Public Liability page not loaded");
		
	
	String ratingTable_xpath = "//*[text()='Apply Book Rates']//following::table";
	WebElement ratingTable = driver.findElement(By.xpath(ratingTable_xpath));
	
	k.ScrollInVewWebElement(ratingTable);
	
	int _tble_Headers = driver.findElements(By.xpath("//*[text()='Apply Book Rates']//following::table//thead//th")).size();
	for(int row = 1; row < _tble_Headers ;row ++){
		
		if(isTrue1 && isTrue2)
			break;
		
		WebElement sec_Val = driver.findElement(By.xpath(ratingTable_xpath+"//thead//th["+row+"]"));
		if(sec_Val.getText().equalsIgnoreCase("Activity")){
			section_head_index = row;
			isTrue1=true;
		}
		if(sec_Val.getText().equalsIgnoreCase("Turnover")){
			turnover_head_index = row;
			isTrue2=true;
		}
	}

	int _tble_Rows = driver.findElements(By.xpath("//*[text()='Apply Book Rates']//following::table//tbody//tr")).size();
	
	for(int row = 1; row < _tble_Rows ;row ++){
		
		WebElement activity_Val = driver.findElement(By.xpath(ratingTable_xpath+"//tbody//tr["+row+"]//td["+section_head_index+"]"));
		if(activity_Val.getText().equals(activity)){
			WebElement wage_ = driver.findElement(By.xpath(ratingTable_xpath+"//tbody//tr["+row+"]//td["+turnover_head_index+"]"));
			turnover = wage_.getText();
				
		}else{
			continue;
		}
		
	}
	
	
	}catch(Throwable t){
		//System.out.println("Error while getting turnover for cover >"+coverName+"< - "+t);
	}
	return Integer.parseInt(turnover.replaceAll(",", ""));
}

//MONEY
public int get_Initial_Rate_value(String coverName,String activity){
	
	
	int section_head_index = 0,turnover_head_index = 0;
	String init_Rate = null;
	boolean isTrue1=false,isTrue2=false;

	try{
		
	customAssert.assertTrue(common.funcPageNavigation("Money & Assault", ""),"Money & Assault page not loaded");
		
	
	String ratingTable_xpath = "//*[text()='Apply Book Rates']//following::table";
	WebElement ratingTable = driver.findElement(By.xpath(ratingTable_xpath));
	
	k.ScrollInVewWebElement(ratingTable);
	
	int _tble_Headers = driver.findElements(By.xpath("//*[text()='Apply Book Rates']//following::table//thead//th")).size();
	for(int row = 1; row < _tble_Headers ;row ++){
		
		if(isTrue1 && isTrue2)
			break;
		
		WebElement sec_Val = driver.findElement(By.xpath(ratingTable_xpath+"//thead//th["+row+"]"));
		if(sec_Val.getText().equalsIgnoreCase("Description")){
			section_head_index = row;
			isTrue1=true;
		}
		if(sec_Val.getText().equalsIgnoreCase("Initial Rate")){
			turnover_head_index = row;
			isTrue2=true;
		}
	}

	int _tble_Rows = driver.findElements(By.xpath("//*[text()='Apply Book Rates']//following::table//tbody//tr")).size();
	
	for(int row = 1; row < _tble_Rows ;row ++){
		
		WebElement activity_Val = driver.findElement(By.xpath(ratingTable_xpath+"//tbody//tr["+row+"]//td["+section_head_index+"]"));
		if(activity_Val.getText().equals(activity)){
			try{
				init_Rate = k.getAttributeByXpath(ratingTable_xpath+"//tbody//tr["+row+"]//td["+turnover_head_index+"]//input", "value");
			}catch(Throwable t){
				WebElement rate_ = driver.findElement(By.xpath(ratingTable_xpath+"//tbody//tr["+row+"]//td["+turnover_head_index+"]"));
				init_Rate = rate_.getText();
			}
			
				
		}else{
			continue;
		}
		
	}
	
	
	}catch(Throwable t){
		//System.out.println("Error while getting initial Rate for cover >"+coverName+"< - "+t);
	}
	return Integer.parseInt(init_Rate.replaceAll(",", ""));
}

	
	
	
public String get_Description_from_Table(String activity){
		
		int section_head_index = 0,description_head_index = 0;
		String description = null;
		boolean isTrue1=false,isTrue2=false;

		try{
			
			customAssert.assertTrue(common.funcPageNavigation("Public Liability", ""),"Public Liability page not loaded");
			
		
		String ratingTable_xpath = "//*[text()='Apply Book Rates']//following::table";
		WebElement ratingTable = driver.findElement(By.xpath(ratingTable_xpath));
		
		k.ScrollInVewWebElement(ratingTable);
		
		int _tble_Headers = driver.findElements(By.xpath("//*[text()='Apply Book Rates']//following::table//thead//th")).size();
		for(int row = 1; row < _tble_Headers ;row ++){
			
			if(isTrue1 && isTrue2)
				break;
			
			WebElement sec_Val = driver.findElement(By.xpath(ratingTable_xpath+"//thead//th["+row+"]"));
			if(sec_Val.getText().equalsIgnoreCase("Activity")){
				section_head_index = row;
				isTrue1=true;
			}
			if(sec_Val.getText().equalsIgnoreCase("Description")){
				description_head_index = row;
				isTrue2=true;
			}
		}

		int _tble_Rows = driver.findElements(By.xpath("//*[text()='Apply Book Rates']//following::table//tbody//tr")).size();
		
		for(int row = 1; row < _tble_Rows ;row ++){
			
			WebElement activity_Val = driver.findElement(By.xpath(ratingTable_xpath+"//tbody//tr["+row+"]//td["+section_head_index+"]"));
			if(activity_Val.getText().equals(activity)){
				WebElement desc_ = driver.findElement(By.xpath(ratingTable_xpath+"//tbody//tr["+row+"]//td["+description_head_index+"]"));
				description = desc_.getText();
				break;	
			}else{
				continue;
			}
			
		}
		
		
		}catch(Throwable t){
			//System.out.println("Error while getting description for activity >"+activity+"< - "+t);
		}
		return description;
		
	}
	

public List<String> get_Activities_from_Table(String col_Name){
		
		List<String> pl_activites = new ArrayList<>();
		int section_head_index = 0;
		
		String bookRate_xpath = "//a[text()='Apply Book Rates']//following::table[@id='table0']";
		WebElement bookRate_Table = driver.findElement(By.xpath(bookRate_xpath));
		
		int _tble_Headers = driver.findElements(By.xpath("//*[text()='Apply Book Rates']//following::table//thead//th")).size();
		for(int row = 1; row < _tble_Headers ;row ++){
			
			WebElement sec_Val = driver.findElement(By.xpath(bookRate_xpath+"//thead//th["+row+"]"));
			if(sec_Val.getText().equalsIgnoreCase(col_Name)){
				section_head_index = row;
				break;
			}
		
		}
		
		k.ScrollInVewWebElement(bookRate_Table);
		
		int _tble_Rows = driver.findElements(By.xpath("//a[text()='Apply Book Rates']//following::table[@id='table0']//tbody//tr")).size();
		String sectionValue = null;

		
		for(int row = 1; row < _tble_Rows ;row ++){
		
		
			WebElement sec_Val = driver.findElement(By.xpath(bookRate_xpath+"//tbody//tr["+row+"]//td["+section_head_index+"]"));
			sectionValue = sec_Val.getText();

			pl_activites.add(sectionValue);
		
		}
		
		return pl_activites;
		
}
public List<String> get_GIT_Activities_from_Table(){
		
		List<String> git_activites = new ArrayList<>();
		int section_head_index = 0;
		
		customAssert.assertTrue(common.funcPageNavigation("Goods in Transit", ""),"Goods in Transit page not loaded");
		
		String bookRate_xpath = "//a[text()='Apply Book Rates']//following::table[@id='table0']";
		WebElement bookRate_Table = driver.findElement(By.xpath(bookRate_xpath));
		
		int _tble_Headers = driver.findElements(By.xpath("//*[text()='Apply Book Rates']//following::table//thead//th")).size();
		for(int row = 1; row < _tble_Headers ;row ++){
			
			WebElement sec_Val = driver.findElement(By.xpath(bookRate_xpath+"//thead//th["+row+"]"));
			if(sec_Val.getText().equalsIgnoreCase("Description")){
				section_head_index = row;
				break;
			}
		
		}
		
		k.ScrollInVewWebElement(bookRate_Table);
		
		int _tble_Rows = driver.findElements(By.xpath("//a[text()='Apply Book Rates']//following::table[@id='table0']//tbody//tr")).size();
		String sectionValue = null;

		
		for(int row = 1; row < _tble_Rows ;row ++){
		
		
			WebElement sec_Val = driver.findElement(By.xpath(bookRate_xpath+"//tbody//tr["+row+"]//td["+section_head_index+"]"));
			sectionValue = sec_Val.getText();

			git_activites.add(sectionValue);
		
		}
		
		return git_activites;
		
}
	
	
public double get_PL_Auto_Adjustment_(String pl_Activity){
		
	double auto_rate = 0.0;
	double PL_Indemnity_limit=0.0;
	
	Map<Object,Object> data_map = null;
	
	switch(common.currentRunningFlow){
		case "NB":
			data_map = common.NB_excel_data_map;
		break;
		case "CAN":
			data_map = common.CAN_excel_data_map;
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
		
		int PL_LOI = Integer.parseInt((String)data_map.get("PL_IndemnityLimit"));
		PL_Indemnity_limit = PL_LOI;
		CCD_Rater = OR.getORProperties();
		
			if(PL_Indemnity_limit <= 1000000){
				auto_rate = Double.parseDouble(CCD_Rater.getProperty("PL_LOI_LoadDiscount_1000000"));
			}else if(PL_Indemnity_limit > 1000000 && PL_Indemnity_limit <= 2000000){
				auto_rate = Double.parseDouble(CCD_Rater.getProperty("PL_LOI_LoadDiscount_2000000"));
			}else if(PL_Indemnity_limit > 2000000 && PL_Indemnity_limit <= 3000000){
				auto_rate = Double.parseDouble(CCD_Rater.getProperty("PL_LOI_LoadDiscount_3000000"));
			}else if(PL_Indemnity_limit > 3000000 && PL_Indemnity_limit <= 4000000){
				auto_rate = Double.parseDouble(CCD_Rater.getProperty("PL_LOI_LoadDiscount_4000000"));
			}else if(PL_Indemnity_limit > 4000000 && PL_Indemnity_limit <= 5000000){
				auto_rate = Double.parseDouble(CCD_Rater.getProperty("PL_LOI_LoadDiscount_5000000"));
			}else{
				auto_rate = Double.parseDouble(CCD_Rater.getProperty("PL_LOI_LoadDiscount_Above_5000000"));
			}
		}catch(Throwable t){
			//System.out.println("Error while calculating PL Auto Adjustment Rate --> "+t.getMessage());
		}
		
		return auto_rate;
			
}
	
public double get_BI_Auto_Adjustment_(String bi_Activity, int indemnity_period){
		
		double auto_rate = 0.0;
		CCD_Rater = OR.getORProperties();
		
		if(bi_Activity.equalsIgnoreCase("Additional increased costs of working") || bi_Activity.contains("Increased Cost of Working")
				|| bi_Activity.contains("Gross Profit") || bi_Activity.contains("Gross Revenue")){
			
			auto_rate = Integer.parseInt(CCD_Rater.getProperty("IP_discount_BI_"+indemnity_period+"_Months"));
			
		}else{
			auto_rate = 0.0;
		}
		
		return auto_rate;
			
}
	
	//This function is to check " if Book Premium generated is less than Minimum Premium
	//Applicable Covers --> EL , PL
public double func_Check_Minimum_Premium(String coverName,String activityName,double bookPremium){
			
			CCD_Rater = OR.getORProperties();
			double _Min_Premium_=0.0;
			
			try{
				
				_Min_Premium_ = get_Min_Premium_from_Properties(coverName,activityName);
				
				if(bookPremium < _Min_Premium_){ //Minimum Premium Rule
					return _Min_Premium_;
				}else{
					return bookPremium;
				}
					
				
			}catch(Throwable t){
				//System.out.println("Error in func_Check_Minimum_Premium "+t.getMessage());
				return 0.0;
			}
			
			
}
	
public double get_Min_Premium_from_Properties(String coverName,String _Activity){
		
		double r_value=0.0;
		DecimalFormat formatter = new DecimalFormat("###.###");
	
		try{
			CCD_Rater = OR.getORProperties();
			String t_activity = _Activity.replaceAll("–", "").replaceAll(" ", "_").replaceAll(",", "").replaceAll("__", "_");
			t_activity = t_activity + "_"+coverName+"_MP";
			
			r_value = Double.parseDouble(CCD_Rater.getProperty(t_activity));
			r_value = Double.valueOf(formatter.format(r_value));
			
		}catch(Throwable t ){
			//System.out.println("Error while getting Minimum Premium for activity > "+_Activity+" < "+t.getMessage());
		}
		return r_value;
			
}
	
	


	//************************END************************************//
	//************************************************************//
	//**** CCD Book Rate Calculator Based on QBECC Sheet ********//
	//************************************************************//
	//************************************************************//


	// Auto Rated Cover - Calculation table handling :
	
	public boolean funcValidate_AutoRatedTables(Map<Object, Object> map_data, String s_CoverName, String s_Abvr){
		boolean retValue = true;
		String s_Section = null, sVal = null, s_ColName = null, s_InnerSheetName = null, i_abvr = null, s_SheetName = null;
		int totalCols = 0, totalRows = 0, InnerCount = 0;
		String sRater = null;
		
		double s_Wages = 0.00, s_BookRate = 0.00, s_BookP = 0.00, s_TechAdjust = 0.00, s_CommAdjust = 0.00, s_Premium = 0.00;
		double c_Wages = 0.00, c_BookRate = 0.00, c_BookP = 0.00, c_TechAdjust = 0.00, c_CommAdjust = 0.00, c_Premium = 0.00, c_TotalP = 0.00;
		double ad_Rate = 0.00;
		
		try{
			
			Map<String, List<Map<String, String>>> data_map = null;
			
			if(common.currentRunningFlow.contains("MTA")){
				data_map = common.MTA_Structure_of_InnerPagesMaps;
			}else if(common.currentRunningFlow.contains("Rewind")){
				data_map = common.Rewind_Structure_of_InnerPagesMaps;
			}else if(common.currentRunningFlow.contains("Requote")){
				data_map = common.Requote_Structure_of_InnerPagesMaps;
			}else if(common.currentRunningFlow.contains("Renewal")){
				data_map = common.Renewal_Structure_of_InnerPagesMaps;
			}else{
				data_map = common.NB_Structure_of_InnerPagesMaps;
			}
			
			
			String sTablePath = "//a[text()='Apply Book Rates']//following::table[@id='table0']";
			
			WebElement s_table= driver.findElement(By.xpath(sTablePath));
			k.ScrollInVewWebElement(s_table);
			
			totalCols = s_table.findElements(By.tagName("th")).size(); 
			totalRows = s_table.findElements(By.tagName("tr")).size();			
			
			customAssert.assertTrue(func_AddInput_CalTable(sTablePath, s_Abvr, s_CoverName),"Issue in input data to the  premium calculation table");
			
			//Calculation :
			
			if(s_CoverName.contains("Material Damage")){
				customAssert.assertTrue(k.Click("CCD_MD_ApplyBookRateButton"), "Unable to click on apply book rate button.");
			}else{
				customAssert.assertTrue(k.Click("CCD_ApplyBookRate"), "Unable to click on apply book rate button.");
			}
			
			customAssert.assertTrue(func_AllCovers_Calculation(sTablePath, s_Abvr, s_CoverName),"Issue in input data to the  premium calculation table");
							 
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


	public boolean func_AddInput_CalTable(String sTablePath, String s_Abvr, String s_CoverName) {
		
		boolean retVal = true;
		
		int totalCols=0, totalRows = 0, InnerCount = 0, iBespokeIndex = 0, iIndex = 0, iIndex_temp = 0;
		String s_Activity = null, s_ColName = null, sVal = null, s_Description = null, s_InnerSheetName = null, i_abvr = null ;
		String BI_Extensions = null;
		
		Map<String, List<Map<String, String>>> data_map = null;
		Map<Object, Object> map_data = null;
		
		if(common.currentRunningFlow.contains("MTA")){
			data_map = common.MTA_Structure_of_InnerPagesMaps;
			map_data = common.MTA_excel_data_map;
		}else if(common.currentRunningFlow.contains("Rewind")){
			data_map = common.Rewind_Structure_of_InnerPagesMaps;
			map_data = common.Rewind_excel_data_map;
		}else if(common.currentRunningFlow.contains("Requote")){
			data_map = common.Requote_Structure_of_InnerPagesMaps;
			map_data = common.Requote_excel_data_map;
		}else if(common.currentRunningFlow.contains("Renewal")){
			data_map = common.Renewal_Structure_of_InnerPagesMaps;
			map_data = common.Renewal_excel_data_map;
		}else{
			data_map = common.NB_Structure_of_InnerPagesMaps;
			map_data = common.NB_excel_data_map;
		}
				
		WebElement s_table= driver.findElement(By.xpath(sTablePath));
		k.ScrollInVewWebElement(s_table);
		
		totalCols = s_table.findElements(By.tagName("th")).size(); 
		totalRows = s_table.findElements(By.tagName("tr")).size();
		
		switch (s_CoverName){	
				
			case "Business Interruption" :
				
				String BI_BBInterruption = ObjectMap.properties.getProperty(CommonFunction.product+"_BI_BBInterruption");
				BI_Extensions = ObjectMap.properties.getProperty(CommonFunction.product+"_BI_Extensions");		
				
				for(int i = 0; i< totalRows-2; i++){
					s_Description = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[1]");
					s_Activity = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]");
										
					if(!s_Description.contains("Business Interruption")){
						s_InnerSheetName = "AddBespoke"+s_Abvr.replace("_", "");
						i_abvr = s_Abvr+"AddB_";
						sVal = (String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"TechAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(sVal);
						
						sVal = (String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"CommAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[10]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[10]/input")).sendKeys(sVal);
						
						String pOverride = (String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"PremiumOverride");
						if(!pOverride.contains("0")){
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(pOverride);
						}
						iBespokeIndex = iBespokeIndex + 1;
						
						
					}else if(s_Description.contains("Business Interruption") && s_Activity.contains("Book Debts")){
											
						sVal = (String)map_data.get("BI_TechAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(sVal);
						
						sVal = (String)map_data.get("BI_CommAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[10]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[10]/input")).sendKeys(sVal);
						
						String pOverride = (String)map_data.get("BI_PremiumOverride");
						if(!pOverride.contains("0")){
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(pOverride);
						}
												
					}else if(s_Description.contains("Business Interruption") && BI_Extensions.contains(s_Activity)){
						
						sVal =  (String)map_data.get("BI_TechAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(sVal);
						
						sVal = (String)map_data.get("BI_CommAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[10]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[10]/input")).sendKeys(sVal);
						
						String pOverride = (String)map_data.get("BI_PremiumOverride");
						if(!pOverride.contains("0")){
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(pOverride);
						}
					}else if(s_Description.contains("Business Interruption") && BI_BBInterruption.contains(s_Activity)){
					
						
						s_InnerSheetName = "BI-BBI";
						i_abvr = "BI_BBI_";
						sVal = (String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(sVal);
						
						sVal = (String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[10]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[10]/input")).sendKeys(sVal);
						
						String pOverride = (String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"PremiumOverride");
						if(!pOverride.contains("0")){
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(pOverride);
						}
												
						InnerCount = InnerCount + 1;
						
					}else{
						
						s_InnerSheetName = "BI-AdditionalExt";
						i_abvr = "BI_AE_";
						sVal = (String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"TechAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(sVal);
						
						sVal = (String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"CommAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[10]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[10]/input")).sendKeys(sVal);
						
						String pOverride = (String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"PremiumOverride");
						if(!pOverride.contains("0")){
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(pOverride);
						}
						
						iIndex = iIndex + 1;	
					}
				}
				break;
				
			case "Money & Assault" :
				
				for(int i = 0; i< totalRows-2; i++){
					s_Description = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[1]");
					
					if(s_Description.contains("Any other loss of money")){
						i_abvr = "MA_LOL_";
					}else if(s_Description.contains("Limit of cash in locked safe")){
						i_abvr = "MA_LOC_";
					}else{
						i_abvr = "MA_EOA_";
					}
					
					sVal = (String)map_data.get(i_abvr+"TechAdjust");
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
					
					sVal = (String)map_data.get(i_abvr+"CommAdjust");
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(sVal);
					
					String pOverride = (String)map_data.get(i_abvr+"PremiumOverride");
					if(!pOverride.contains("0")){
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(pOverride);
					}				
				}
				break;
				
			case "Employers Liability" :
				
				String EL_AdditionalCovers = ObjectMap.properties.getProperty(CommonFunction.product+"_EL_AdditionalCovers");
											
				for(int i = 0; i< totalRows-2; i++){
					s_Description = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[1]");
					s_Activity = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]");
					
					if(!s_Description.contains("Employers Liability")){
						s_InnerSheetName = "AddBespoke"+s_Abvr.replace("_", "");
						i_abvr = s_Abvr+"AddB_";
						sVal = (String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"TechAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
						
						sVal = (String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"CommAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(sVal);
						
						String pOverride = (String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"PremiumOverride");
						if(!pOverride.contains("0")){
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(pOverride);
						}
						iBespokeIndex = iBespokeIndex + 1;
						
						
					}else if(s_Description.contains("Employers Liability") && EL_AdditionalCovers.contains(s_Activity)){
					
						s_InnerSheetName = "AddBespSumInsEL";
						i_abvr = "EL_ABSI_";
						sVal = (String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"TechAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
						
						sVal = (String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"CommAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(sVal);
						
						String pOverride = (String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"PremiumOverride");
						if(!pOverride.contains("0")){
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(pOverride);
						}
												
						iIndex = iIndex + 1;
						
					}else{
						
						s_InnerSheetName = "AddEmpWages";
						i_abvr = "EL_ACT_";
						sVal = (String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
						
						sVal = (String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(sVal);
						
						String pOverride = (String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"PremiumOverride");
						if(!pOverride.contains("0")){
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(pOverride);
						}
						
						InnerCount = InnerCount + 1;	
					}
				}
				break;
				
			case "Public Liability" :
															
				for(int i = 0; i< totalRows-2; i++){
					s_Description = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[1]");
								
					if(!s_Description.contains("Public Liability")){
						s_InnerSheetName = "AddBespoke"+s_Abvr.replace("_", "");
						i_abvr = s_Abvr+"AddB_";
						sVal = (String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"TechAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(sVal);
						
						sVal = (String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"CommAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(sVal);
						
						String pOverride = (String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"PremiumOverride");
						if(!pOverride.contains("0")){
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[12]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[12]/input")).sendKeys(pOverride);
						}
						iBespokeIndex = iBespokeIndex + 1;
						
						
					}else if(s_Description.contains("Public Liability AC")){
						
						s_InnerSheetName = "AddBespSumInsPL";
						i_abvr = "PL_ABSI_";
						sVal = (String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(sVal);
						
						sVal = (String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(sVal);
						
						String pOverride = (String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"PremiumOverride");
						if(!pOverride.contains("0")){
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[12]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[12]/input")).sendKeys(pOverride);
						}
												
						InnerCount = InnerCount + 1;
						
					}else if(s_Description.contains("Public Liability BF")){
					
						s_InnerSheetName = "AddBFSActivityPL";
						i_abvr = "PL_BFS_";
						sVal = (String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"TechAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(sVal);
						
						sVal = (String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"CommAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(sVal);
						
						String pOverride = (String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"PremiumOverride");
						if(!pOverride.contains("0")){
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[12]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[12]/input")).sendKeys(pOverride);
						}
												
						iIndex = iIndex + 1;
						
					}else{
					
						s_InnerSheetName = "AddActivitiesPL";
						i_abvr = "PL_ACT_";
						sVal = (String)data_map.get(s_InnerSheetName).get(iIndex_temp).get(i_abvr+"TechAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(sVal);
						
						sVal = (String)data_map.get(s_InnerSheetName).get(iIndex_temp).get(i_abvr+"CommAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[11]/input")).sendKeys(sVal);
						
						String pOverride = (String)data_map.get(s_InnerSheetName).get(iIndex_temp).get(i_abvr+"PremiumOverride");
						if(!pOverride.contains("0")){
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[12]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[12]/input")).sendKeys(pOverride);
						}
						
						iIndex_temp = iIndex_temp + 1;	
					}
				}
				break;
				
			case "Personal Accident Standard" :
				
				for(int i = 0; i< totalRows-2; i++){
					s_Description = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[1]");
					s_Activity = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]");
					
					if(s_Activity.contains("Clerical")){
						i_abvr = "PAS_Clerical_";
					}else if(s_Activity.contains("Drivers")){
						i_abvr = "PAS_Drivers_";
					}else{
						i_abvr = "PAS_Other_";
					}
					
					sVal = (String)map_data.get(i_abvr+"TechAdjust");
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
					
					sVal = (String)map_data.get(i_abvr+"CommAdjust");
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(sVal);
					
					String pOverride = (String)map_data.get(i_abvr+"PremiumOverride");
					if(!pOverride.contains("0")){
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(pOverride);
					}				
				}
				break;
				
			case "Personal Accident Optional" :
				
				for(int i = 0; i< totalRows-2; i++){
					
					i_abvr = "PAO_ACT_";					
					
					sVal = (String)map_data.get(i_abvr+"TechAdjust");
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
					
					sVal = (String)map_data.get(i_abvr+"CommAdjust");
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(sVal);
					
					String pOverride = (String)map_data.get(i_abvr+"PremiumOverride");
					if(!pOverride.contains("0")){
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(pOverride);
					}				
				}
				break;
				
			case "Goods in Transit" :
				
				for(int i = 0; i< totalRows-2; i++){
					
					i_abvr = "GIT_";					
					
					sVal = (String)map_data.get(i_abvr+"TechAdjust");
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
					
					sVal = (String)map_data.get(i_abvr+"CommAdjust");
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(sVal);
					
					String pOverride = (String)map_data.get(i_abvr+"PremiumOverride");
					if(!pOverride.contains("0")){
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(pOverride);
					}				
				}
				break;
				
			case "Terrorism" :
				
				for(int i = 0; i< totalRows-2; i++){
					
					s_Description = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[1]");
					
					if(s_Description.contains("Business Interruption")){
						i_abvr = "Ter_BI_";
					}else if(s_Description.contains("Buildings and Contents")){
						i_abvr = "Ter_BC_";
					}				
					
					sVal = (String)map_data.get(i_abvr+"TechAdjust");
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
					
					sVal = (String)map_data.get(i_abvr+"CommAdjust");
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(sVal);
					
					String pOverride = (String)map_data.get(i_abvr+"PremiumOverride");
					if(!pOverride.contains("0")){
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(pOverride);
					}				
				}
				break;
				
			case "Legal Expenses" :
				
				for(int i = 0; i< totalRows-2; i++){
					
					i_abvr = "LE_";									
					
					sVal = (String)map_data.get(i_abvr+"TechAdjust");
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[4]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[4]/input")).sendKeys(sVal);
					
					sVal = (String)map_data.get(i_abvr+"CommAdjust");
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
					
					String pOverride = (String)map_data.get(i_abvr+"PremiumOverride");
					if(!pOverride.contains("0")){
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(pOverride);
					}				
				}
				break;
				
			case "Material Damage":
				
				int temp_Count  = 0;

				String MD_Buildings = ObjectMap.properties.getProperty(CommonFunction.product+"_MD_Buildings");
				String MD_Contents = ObjectMap.properties.getProperty(CommonFunction.product+"_MD_Contents");		
				String MD_SpContents = ObjectMap.properties.getProperty(CommonFunction.product+"_MD_SpContents");
				
				for(int i = 0; i< totalRows-2; i++){
					
					s_Description = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[1]");
					String s_Act = s_Description;
					s_Act = s_Act.replace(",", "");
					s_Act = s_Act.replace(" ", "_");
					
					if(MD_Buildings.contains(s_Act) || s_Description.contains("Subsidience")){
						iIndex = p_Index;
						s_InnerSheetName = "Property Details";
						i_abvr = "AddBuilding_";
						
						sVal = (String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"TechAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
						
						sVal = (String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"CommAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(sVal);
						
						String pOverride = (String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"PremiumOverride");
						if(!pOverride.contains("0")){
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(pOverride);
						}
						iIndex  = iIndex  + 1;						
						
					}else if(MD_Contents.contains(s_Act)){
					    s_InnerSheetName = "Property Details";
						i_abvr = "AddContent_";
						
						InnerCount = p_Index;
						
						sVal = (String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
						
						sVal = (String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(sVal);
						
						String pOverride = (String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"PremiumOverride");
						if(!pOverride.contains("0")){
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(pOverride);
						}
						
						InnerCount  = InnerCount  + 1;		
												
					}else if(MD_SpContents.contains(s_Act)){
					    s_InnerSheetName = "Property Details";
						i_abvr = "AddSPContent_";
						
						temp_Count = p_Index;
						
						sVal = (String)data_map.get(s_InnerSheetName).get(temp_Count ).get(i_abvr+"TechAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
						
						sVal = (String)data_map.get(s_InnerSheetName).get(temp_Count ).get(i_abvr+"CommAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(sVal);
						
						String pOverride = (String)data_map.get(s_InnerSheetName).get(temp_Count ).get(i_abvr+"PremiumOverride");
						if(!pOverride.contains("0")){
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(pOverride);
						}
						
						temp_Count  = temp_Count + 1;
						
					}else{
						s_InnerSheetName = "Property Details";
						i_abvr = "AddBeSpoke_";
						
						iBespokeIndex = p_Index;
						
						sVal = (String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"TechAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
						
						sVal = (String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"CommAdjust");
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(sVal);
						
						String pOverride = (String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"PremiumOverride");
						if(!pOverride.contains("0")){
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
							driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(pOverride);
						}
						
						iBespokeIndex   = iBespokeIndex  + 1;
					}
				}
				
				break;
				
			}
		
		return retVal;
}
	
public boolean func_AllCovers_Calculation(String sTablePath, String s_Abvr, String s_CoverName) {
		
		boolean retVal = true;
		
		int totalCols=0, totalRows = 0, InnerCount = 0, iBespokeIndex = 0, iIndex = 0, iIndex_temp = 0;
		String s_Activity = null, s_ColName = null, sVal = null, s_Description = null, s_InnerSheetName = null, i_abvr = null ;
		String BI_Extensions = null;
		
		int indemnity_period = 0;
		String sCol_PremiumSummary = null;
		
		double s_SumIns = 0.00, s_BookRatePerc = 0.00, s_BookP = 0.00, s_AutoAdjust = 0.00, s_BookRate = 0.00, s_TechAdjust = 0.00, s_RevisedP = 0.00, s_CommAdjust = 0.00, s_OverrideP = 0.00, s_FinalP = 0.00, s_TotalP = 0.00;
		double c_SumIns = 0.00, c_BookRatePerc = 0.00, c_BookP = 0.00, c_AutoAdjust = 0.00, c_BookRate = 0.00, c_TechAdjust = 0.00, c_RevisedP = 0.00, c_CommAdjust = 0.00, c_OverrideP = 0.00, c_FinalP = 0.00, c_TotalP = 0.00;
		double temp_P = 0.00;
		
		Map<String, List<Map<String, String>>> data_map = null;
		Map<Object, Object> map_data = null;
		
		if(common.currentRunningFlow.contains("MTA")){
			data_map = common.MTA_Structure_of_InnerPagesMaps;
			map_data = common.MTA_excel_data_map;
		}else if(common.currentRunningFlow.contains("Rewind")){
			data_map = common.Rewind_Structure_of_InnerPagesMaps;
			map_data = common.Rewind_excel_data_map;
		}else if(common.currentRunningFlow.contains("Requote")){
			data_map = common.Requote_Structure_of_InnerPagesMaps;
			map_data = common.Requote_excel_data_map;
		}else if(common.currentRunningFlow.contains("Renewal")){
			data_map = common.Renewal_Structure_of_InnerPagesMaps;
			map_data = common.Renewal_excel_data_map;
		}else{
			data_map = common.NB_Structure_of_InnerPagesMaps;
			map_data = common.NB_excel_data_map;
		}
				
		WebElement s_table= driver.findElement(By.xpath(sTablePath));
		k.ScrollInVewWebElement(s_table);
		
		totalCols = s_table.findElements(By.tagName("th")).size(); 
		totalRows = s_table.findElements(By.tagName("tr")).size();
		
		switch (s_CoverName){
		
		case "Material Damage" :
			
			sCol_PremiumSummary = "PS_MaterialDamage_";
			int temp_Count = 0;
			String s_Act = null;
						
			String MD_Buildings = ObjectMap.properties.getProperty(CommonFunction.product+"_MD_Buildings");
			String MD_Contents = ObjectMap.properties.getProperty(CommonFunction.product+"_MD_Contents");		
			String MD_SpContents = ObjectMap.properties.getProperty(CommonFunction.product+"_MD_SpContents");
			
			// Read values from screen , calculate n compare :
			
			for(int i = 0; i< totalRows-2; i++){
				
				// Read Values :
				
				s_Description = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[1]");
				s_Act = s_Description;
				s_Act = s_Act.replace(",", "");
				s_Act = s_Act.replace(" ", "_");
				
				s_SumIns  = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]"));
				s_BookRatePerc  = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[3]/input"));
				s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[4]/input"));
				s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
				s_RevisedP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
				s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[7]/input"));
				s_OverrideP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[8]/input"));
				s_FinalP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[9]/input"));
				
									
				if(MD_Buildings.contains(s_Act) || s_Description.contains("Subsidience")){
					
					s_InnerSheetName = "Property Details";
					i_abvr = "AddBuilding_";
					
					iIndex = p_Index;
					
					InnerCount = p_Index;
					String s_coverBasis = (String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CoverBasis");
					if(s_coverBasis.contains("Day 1")){
						c_SumIns = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"DeclaredValue"));
					}else{
						c_SumIns = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"Suminsured"));
					}
										
					if(s_Description.contains("Subsidience")){
						c_BookRatePerc  = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"Subsidence_BookRate"));
					}else{
						c_BookRatePerc  = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"Property_BookRate"));
					}
					
					c_BookP = (c_BookRatePerc*c_SumIns)/100;
					c_TechAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"TechAdjust"));
					c_CommAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"CommAdjust"));
					c_OverrideP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"PremiumOverride"));
					
					iIndex = iIndex + 1;
					
				}else if(MD_Contents.contains(s_Act)){
				    s_InnerSheetName = "Property Details";
					i_abvr = "AddContent_";
					
					InnerCount = p_Index;
					String s_coverBasis = (String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CoverBasis");
					if(s_coverBasis.contains("Day 1")){
						c_SumIns = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"DeclaredValue"));
					}else{
						c_SumIns = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"Suminsured"));
					}
					
					
					c_BookRatePerc  = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"ContentsItemType_BookRate"));
					c_BookP = (c_BookRatePerc*c_SumIns)/100;
					c_TechAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
					c_CommAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
					c_OverrideP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"PremiumOverride"));
					
					InnerCount = InnerCount + 1;
										
				}else if(MD_SpContents.contains(s_Act)){
				    s_InnerSheetName = "Property Details";
					i_abvr = "AddSPContent_";
					
					temp_Count = p_Index;
					
					InnerCount = p_Index;
					String s_coverBasis = (String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CoverBasis");
					if(s_coverBasis.contains("Day 1")){
						c_SumIns = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"DeclaredValue"));
					}else{
						c_SumIns = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"Suminsured"));
					}
										
					c_BookRatePerc  = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(temp_Count).get(i_abvr+"ContentsItemType_BookRate"));
					c_BookP = (c_BookRatePerc*c_SumIns)/100;
					c_TechAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(temp_Count).get(i_abvr+"TechAdjust"));
					c_CommAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(temp_Count).get(i_abvr+"CommAdjust"));
					c_OverrideP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(temp_Count).get(i_abvr+"PremiumOverride"));
						
					temp_Count = temp_Count + 1;
					
				}else{
					
					s_InnerSheetName = "Property Details";
					i_abvr = "AddBeSpoke_";
					
					iBespokeIndex = p_Index;
					
					c_SumIns = 0.00;
					c_BookP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"Premium"));
					c_BookRatePerc  = 0.00;					
					c_TechAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"TechAdjust"));
					c_CommAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"CommAdjust"));
					c_OverrideP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"PremiumOverride"));
					
					iBespokeIndex = iBespokeIndex + 1;	
				}
				
				c_RevisedP =  c_BookP + ((c_BookP*c_TechAdjust)/100);
				
				temp_P = c_RevisedP + ((c_RevisedP*c_CommAdjust)/100);
				
				if(c_OverrideP == 0.00){
					c_FinalP = temp_P;
				}else{
					c_FinalP = c_OverrideP;
				}
				
				// Compare values :
				
				try {
					CommonFunction.compareValues(c_SumIns , s_SumIns ,"Sum insured for activity  "+s_Description +" of cover - "+s_CoverName);					
					CommonFunction.compareValues(c_BookRatePerc, s_BookRatePerc,"BookRate % for activity "+s_Description +" of cover - "+s_CoverName);
					CommonFunction.compareValues(c_BookP, s_BookP,"BookP for activity "+s_Description +" of cover - "+s_CoverName);
					CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"TechAdjust for activity "+s_Description +" of cover - "+s_CoverName);
					CommonFunction.compareValues(c_RevisedP, s_RevisedP,"RevisedP for activity "+s_Description +" of cover - "+s_CoverName);
					CommonFunction.compareValues(c_CommAdjust, s_CommAdjust,"CommAdjust for activity "+s_Description +" of cover - "+s_CoverName);
					CommonFunction.compareValues(c_OverrideP, s_OverrideP,"PremiumOverride for activity "+s_Description +" of cover - "+s_CoverName);
					CommonFunction.compareValues(c_FinalP, s_FinalP,"FinalPremium for activity "+s_Description +" of cover - "+s_CoverName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				c_TotalP = c_TotalP + c_FinalP;
				totalMD = c_TotalP;
			}
			break;
		
			case "Business Interruption" :
				sCol_PremiumSummary = "PS_BusinessInterruption_";
				
				customAssert.assertTrue(calculate_Book_Rate("BI"), "Issue in book Rate calculation for Business Interruption cover");
				
				String BI_BBInterruption = ObjectMap.properties.getProperty(CommonFunction.product+"_BI_BBInterruption");
				BI_Extensions = ObjectMap.properties.getProperty(CommonFunction.product+"_BI_Extensions");		
				
				// Read values from screen , calculate n compare :
				
				for(int i = 0; i< totalRows-2; i++){
					
					// Read Values :
					
					s_Description = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[1]");
					s_Activity = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]");
					
					if(s_Description.contains("Business Interruption")){
						s_SumIns  = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[3]"));
					}
					
					s_BookRatePerc  = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[4]/input"));
					s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
					s_AutoAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
					s_BookRate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[7]/input"));
					s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[8]/input"));
					s_RevisedP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[9]/input"));
					s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[10]/input"));
					s_OverrideP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[11]/input"));
					s_FinalP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[12]/input"));
					
										
					if(!s_Description.contains("Business Interruption")){
						
						s_InnerSheetName = "AddBespoke"+s_Abvr.replace("_", "");
						i_abvr = s_Abvr+"AddB_";
						indemnity_period = 0;
						
						c_BookP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"Premium"));
						c_BookRatePerc  = common_CCD.Book_rate_Rater_output.get("BI"+"_"+s_Activity);						
						c_BookRate = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"Premium"));
						c_TechAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"TechAdjust"));
						c_CommAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"CommAdjust"));
						c_OverrideP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"PremiumOverride"));
						
						iBespokeIndex = iBespokeIndex + 1;
						
					}else if(s_Description.contains("Business Interruption") && s_Activity.contains("Book Debts")){
						
						indemnity_period = 0;
						c_SumIns = Double.parseDouble((String)map_data.get("BI_SumInsured_BookDebts"));
						c_TechAdjust = Double.parseDouble((String)map_data.get("BI_TechAdjust"));
						c_CommAdjust = Double.parseDouble((String)map_data.get("BI_CommAdjust"));
						c_OverrideP = Double.parseDouble((String)map_data.get("BI_PremiumOverride"));
					
						c_BookRatePerc  = common_CCD.Book_rate_Rater_output.get("BI"+"_"+s_Activity);
						c_BookP = (c_SumIns * c_BookRatePerc)/100;
						
					}else if(s_Description.contains("Business Interruption") && BI_Extensions.contains(s_Activity)){
						String sFieldName = null;
						if(s_Activity.contains("ROI")){
							sFieldName = "ROI";
						}else{
							sFieldName = s_Activity.replace(" ", "");
							sFieldName = sFieldName.replace("-", "");
						}
						
						indemnity_period = 0;						
						
						double default_Val = Double.parseDouble((String)map_data.get("BI_Extensions_"+sFieldName+"_Default"));
						//BI _Extensions_ContractSites_Default
						double input_Val = Double.parseDouble((String)map_data.get("BI_Extensions_"+sFieldName));
						
						c_SumIns = input_Val - default_Val;
						c_TechAdjust = Double.parseDouble((String)map_data.get("BI_TechAdjust"));
						c_CommAdjust = Double.parseDouble((String)map_data.get("BI_CommAdjust"));
						c_OverrideP = Double.parseDouble((String)map_data.get("BI_PremiumOverride"));
						c_BookRatePerc  = common_CCD.Book_rate_Rater_output.get("BI"+"_"+s_Activity);
						
						c_BookP = (c_SumIns * c_BookRatePerc)/100;
						
					}else if(s_Description.contains("Business Interruption") && BI_BBInterruption.contains(s_Activity)){
					    s_InnerSheetName = "BI-BBI";
						i_abvr = "BI_BBI_";
						
						double ip_Factor = 0.00;
						
						indemnity_period = Integer.parseInt((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"IP"));
						c_SumIns = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"SumInsured"));
						c_TechAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
						c_CommAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
						c_OverrideP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"PremiumOverride"));
						c_BookRatePerc  = common_CCD.Book_rate_Rater_output.get("BI"+"_"+s_Activity);
						
						if(s_Activity.contains("Declaration Linked") || s_Activity.contains("Flexible Limit of Loss") ||s_Activity.contains("Rent Receivable") ){
							ip_Factor = 1.0;
						}else{
							if(indemnity_period == 18){
								ip_Factor = 1.5;
							}else if(indemnity_period == 24){
								ip_Factor = 2.0;							
							}else if(indemnity_period == 24){
								ip_Factor = 3.0;
							}else{
								ip_Factor = 1.0;
							}
						}
						
						c_BookP = ((c_SumIns * c_BookRatePerc)/100)*ip_Factor;
							
						InnerCount = InnerCount + 1;
						
					}else{
						
						s_InnerSheetName = "BI-AdditionalExt";
						i_abvr = "BI_AE_";
						
						double ip_Factor = 0.00;
						
						c_BookRatePerc  = common_CCD.Book_rate_Rater_output.get("BI"+"_"+s_Activity);
												
						indemnity_period = Integer.parseInt((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"IP"));
						c_SumIns = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"SumInsured"));
						c_TechAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"TechAdjust"));
						c_CommAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"CommAdjust"));
						c_OverrideP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"PremiumOverride"));
						
						
						if(indemnity_period == 18){
							ip_Factor = 1.5;
						}else if(indemnity_period == 24){
							ip_Factor = 2.0;							
						}else if(indemnity_period == 24){
							ip_Factor = 3.0;
						}else{
							ip_Factor = 1.0;
						}
						
						c_BookP = ((c_SumIns * c_BookRatePerc)/100)*ip_Factor;
						
						iIndex = iIndex + 1;	
					}
					
					c_AutoAdjust  = get_BI_Auto_Adjustment_(s_Activity, indemnity_period);
					c_BookRate =  c_BookP - ((c_BookP*c_AutoAdjust)/100);
					c_RevisedP =  c_BookRate + ((c_BookRate*c_TechAdjust)/100);
					
					temp_P = c_RevisedP + ((c_RevisedP*c_CommAdjust)/100);
					
					if(c_OverrideP == 0.00){
						c_FinalP = temp_P;
					}else{
						c_FinalP = c_OverrideP;
					}
					
					// Compare values :
					
					try {
						if(s_Description.contains("Business Interruption")){
							CommonFunction.compareValues(c_SumIns , s_SumIns ,"Sum insured for activity  "+s_Activity +" of cover - "+s_CoverName);
						}
						
						CommonFunction.compareValues(c_BookRatePerc, s_BookRatePerc,"BookRate % for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_BookP, s_BookP,"BookP for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(Math.abs(c_AutoAdjust), Math.abs(s_AutoAdjust),"AutoAdjust for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_BookRate, s_BookRate,"BookRate  for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"TechAdjust for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_RevisedP, s_RevisedP,"RevisedP for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_CommAdjust, s_CommAdjust,"CommAdjust for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_OverrideP, s_OverrideP,"PremiumOverride for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_FinalP, s_FinalP,"FinalPremium for activity "+s_Activity +" of cover - "+s_CoverName);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					c_TotalP = c_TotalP + c_FinalP;
				}
				break;
				
			case "Money & Assault" :
				
				sCol_PremiumSummary = "PS_Money&Assault_";
				customAssert.assertTrue(calculate_Book_Rate("MONEY"), "Issue in book Rate calculation for MONEY cover");
				
				for(int i = 0; i< totalRows-2; i++){
					s_Description = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[1]");
					
					s_SumIns = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]/input"));
					s_BookRatePerc  = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[3]/input"));
					s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[4]/input"));
					s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
					s_RevisedP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
					s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[7]/input"));
					s_OverrideP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[8]/input"));
					s_FinalP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[9]/input"));
					
					double default_Val = 0.00;
					
					if(s_Description.contains("Any other loss of money")){
						i_abvr = "MA_LOL_";
						c_SumIns = Double.parseDouble((String)map_data.get("MA_LossOfMoney"));
					}else if(s_Description.contains("Limit of cash in locked safe")){
						i_abvr = "MA_LOC_";
						default_Val = Double.parseDouble((String)map_data.get("MA_LimitOfCashInLockedSafe_Default"));
						c_SumIns = Double.parseDouble((String)map_data.get("MA_LimitOfCashInLockedSafe"));
						
						if(c_SumIns < default_Val ){
							c_SumIns = 0.00;
						}
					}else{
						i_abvr = "MA_EOA_";
						c_SumIns = Double.parseDouble((String)map_data.get("MA_OwnAnnualCarryings"));
					}
					
					c_TechAdjust = Double.parseDouble((String)map_data.get(i_abvr+"TechAdjust"));
					c_CommAdjust = Double.parseDouble((String)map_data.get(i_abvr+"CommAdjust"));
					c_OverrideP = Double.parseDouble((String)map_data.get(i_abvr+"PremiumOverride"));
					c_BookRatePerc  = common_CCD.Book_rate_Rater_output.get("MONEY"+"_"+s_Description);				
					c_BookP = ((c_SumIns * c_BookRatePerc)/100);
					
					c_RevisedP =  c_BookP + ((c_BookP * c_TechAdjust)/100);
				
					temp_P = c_RevisedP + ((c_RevisedP*c_CommAdjust)/100);
				
					if(c_OverrideP == 0.00){
						c_FinalP = temp_P;
					}else{
						c_FinalP = c_OverrideP;
					}
				
					// Compare values :
					
					try {
						
						CommonFunction.compareValues(c_SumIns , s_SumIns ,"Sum insured for activity  "+s_Description +" of cover - "+s_CoverName);										
						CommonFunction.compareValues(c_BookRatePerc, s_BookRatePerc,"BookRate % for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_BookP, s_BookP,"BookP for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"TechAdjust for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_RevisedP, s_RevisedP,"RevisedP for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_CommAdjust, s_CommAdjust,"CommAdjust for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_OverrideP, s_OverrideP,"PremiumOverride for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_FinalP, s_FinalP,"FinalPremium for activity "+s_Description +" of cover - "+s_CoverName);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					c_TotalP = c_TotalP + c_FinalP;
				}
				break;
				
			case "Employers Liability" :
				
				double c_minP = 0.00;
				
				String EL_AdditionalCovers = ObjectMap.properties.getProperty(CommonFunction.product+"_EL_AdditionalCovers");
						
				sCol_PremiumSummary = "PS_EmployersLiability_";
				
				customAssert.assertTrue(calculate_Book_Rate("EL"), "Issue in book Rate calculation for Employers Liability cover");
				
				for(int i = 0; i< totalRows-2; i++){
					
					// 	Read Values :
					
					s_Description = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[1]");
					s_Activity = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]");
					
					if(s_Description.contains("Employers Liability")){
						s_SumIns  = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[3]"));
					}
					
					s_BookRatePerc  = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[4]/input"));
					s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
					s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
					s_RevisedP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[7]/input"));
					s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[8]/input"));
					s_OverrideP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[9]/input"));
					s_FinalP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[10]/input"));
					
					if(!s_Description.contains("Employers Liability")){
						
						s_InnerSheetName = "AddBespoke"+s_Abvr.replace("_", "");
						i_abvr = s_Abvr+"AddB_";
						
						c_BookP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"Premium"));
						c_BookRatePerc  = common_CCD.Book_rate_Rater_output.get("EL"+"_"+s_Activity);						
						c_TechAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"TechAdjust"));
						c_CommAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"CommAdjust"));
						c_OverrideP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"PremiumOverride"));
										
						iBespokeIndex = iBespokeIndex + 1;
						
						
					}else if(s_Description.contains("Employers Liability") && EL_AdditionalCovers.contains(s_Activity)){
					
						s_InnerSheetName = "AddBespSumInsEL";
						i_abvr = "EL_ABSI_";
						
						c_SumIns = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"Wageroll"));
						c_TechAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"TechAdjust"));
						c_CommAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"CommAdjust"));
						c_OverrideP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"PremiumOverride"));
						c_BookRatePerc  = common_CCD.Book_rate_Rater_output.get("EL"+"_"+s_Activity);
						
						c_BookP = ((c_SumIns * c_BookRatePerc)/100);
						
						iIndex = iIndex + 1;
						
					}else{
						
						s_InnerSheetName = "AddEmpWages";
						i_abvr = "EL_ACT_";
						
						c_SumIns = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"WageRollEmployees"));
						c_TechAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
						c_CommAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
						c_OverrideP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"PremiumOverride"));
						c_BookRatePerc  = common_CCD.Book_rate_Rater_output.get("EL"+"_"+s_Activity);
						c_BookP = ((c_SumIns * c_BookRatePerc)/100);
						
						c_minP = common_CCD.get_Min_Premium_from_Properties("EL", s_Activity);
						
						if(c_BookP < c_minP){
							c_BookP = c_minP;
						}						
						
						InnerCount = InnerCount + 1;	
					}
					
					c_RevisedP =  c_BookP + ((c_BookP*c_TechAdjust)/100);
					
					temp_P = c_RevisedP + ((c_RevisedP*c_CommAdjust)/100);
					
					if(c_OverrideP == 0.00){
						c_FinalP = temp_P;
					}else{
						c_FinalP = c_OverrideP;
					}
					
					// Compare values :
					
					try {
						if(s_Description.contains("Employers Liability")){
							CommonFunction.compareValues(c_SumIns , s_SumIns ,"Sum insured for activity  "+s_Activity +" of cover - "+s_CoverName);
						}
						
						CommonFunction.compareValues(c_BookRatePerc, s_BookRatePerc,"BookRate % for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_BookP, s_BookP,"BookP for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"TechAdjust for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_RevisedP, s_RevisedP,"RevisedP for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_CommAdjust, s_CommAdjust,"CommAdjust for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_OverrideP, s_OverrideP,"PremiumOverride for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_FinalP, s_FinalP,"FinalPremium for activity "+s_Activity +" of cover - "+s_CoverName);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					c_TotalP = c_TotalP + c_FinalP;
				}
				break;
				
			case "Public Liability" :
						
				sCol_PremiumSummary = "PS_PublicLiability_";
				double s_turnover = 0.00, c_turnover = 0.00, c_minPm = 0.00;
				
				customAssert.assertTrue(calculate_Book_Rate("PL"), "Issue in book Rate calculation for Public Liability cover");
				
				for(int i = 0; i< totalRows-2; i++){
					
					// Read Values :
					
					s_Description = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[1]");
					s_Activity = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]");
					
					if(s_Description.contains("Public Liability")){
						s_SumIns  = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[3]"));
						s_turnover  = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[4]"));
					}
					
					s_BookRatePerc  = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
					s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
					s_AutoAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[7]/input"));
					s_BookRate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[8]/input"));
					s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[9]/input"));
					s_RevisedP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[10]/input"));
					s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[11]/input"));
					s_OverrideP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[12]/input"));
					s_FinalP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[13]/input"));
					
								
					if(!s_Description.contains("Public Liability")){
						s_InnerSheetName = "AddBespoke"+s_Abvr.replace("_", "");
						i_abvr = s_Abvr+"AddB_";
						
						c_BookP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"Premium"));
						c_BookRatePerc  = common_CCD.Book_rate_Rater_output.get("PL"+"_"+s_Activity);						
						c_BookRate = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"Premium"));
						c_TechAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"TechAdjust"));
						c_CommAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"CommAdjust"));
						c_OverrideP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iBespokeIndex).get(i_abvr+"PremiumOverride"));
												
						iBespokeIndex = iBespokeIndex + 1;
						
						
					}else if(s_Description.contains("Public Liability AC")){
						
						s_InnerSheetName = "AddBespSumInsPL";
						i_abvr = "PL_ABSI_";
						
						c_SumIns = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"Wageroll"));
						c_turnover = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"Turnover"));
						c_TechAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
						c_CommAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
						c_OverrideP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"PremiumOverride"));
						
						c_BookRatePerc  = common_CCD.Book_rate_Rater_output.get("PL"+"_"+s_Activity);
						c_BookP = (c_SumIns * c_BookRatePerc)/100;
																						
						InnerCount = InnerCount + 1;
						
					}else if(s_Description.contains("Public Liability BF")){
					
						s_InnerSheetName = "AddBFSActivityPL";
						i_abvr = "PL_BFS_";
						
						c_SumIns = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"WageRollEmployees"));
						c_turnover = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"Turnover"));
						c_TechAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"TechAdjust"));
						c_CommAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"CommAdjust"));
						c_OverrideP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex).get(i_abvr+"PremiumOverride"));
						
						c_BookRatePerc  = common_CCD.Book_rate_Rater_output.get("PL"+"_"+s_Activity);
						
						String TO_W = get_WageRoll_Turnover_string("PL", s_Activity);
						if(TO_W.contains("W")){
							c_BookP = (c_SumIns * c_BookRatePerc)/100;
						}else{
							c_BookP = (c_turnover * c_BookRatePerc)/100;
						}
						
						c_minPm = common_CCD.get_Min_Premium_from_Properties("PL", s_Activity);
						if(c_BookP < c_minPm){
							c_BookP = c_minPm;
						}	
												
						iIndex = iIndex + 1;
						
					}else{
					
						s_InnerSheetName = "AddActivitiesPL";
						i_abvr = "PL_ACT_";
						
						c_SumIns = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex_temp).get(i_abvr+"WageRollEmployees"));
						c_turnover = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex_temp).get(i_abvr+"Turnover"));
						c_TechAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex_temp).get(i_abvr+"TechAdjust"));
						c_CommAdjust = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex_temp).get(i_abvr+"CommAdjust"));
						c_OverrideP = Double.parseDouble((String)data_map.get(s_InnerSheetName).get(iIndex_temp).get(i_abvr+"PremiumOverride"));
						
						c_BookRatePerc  = common_CCD.Book_rate_Rater_output.get("PL"+"_"+s_Activity);
						
						String TO_W = get_WageRoll_Turnover_string("PL", s_Activity);
						if(TO_W.contains("W")){
							c_BookP = (c_SumIns * c_BookRatePerc)/100;
						}else{
							c_BookP = (c_turnover * c_BookRatePerc)/100;
						}
						
						c_minPm = common_CCD.get_Min_Premium_from_Properties("PL", s_Activity);
						if(c_BookP < c_minPm){
							c_BookP = c_minPm;
						}	
						
						iIndex_temp = iIndex_temp + 1;	
					}
					
					c_AutoAdjust  = get_PL_Auto_Adjustment_(s_Activity);
					c_BookRate =  c_BookP + ((c_BookP*c_AutoAdjust)/100);
					c_RevisedP =  c_BookRate + ((c_BookRate*c_TechAdjust)/100);
					
					temp_P = c_RevisedP + ((c_RevisedP*c_CommAdjust)/100);
					
					if(c_OverrideP == 0.00){
						c_FinalP = temp_P;
					}else{
						c_FinalP = c_OverrideP;
					}
					
					// Compare values :
					
					try {
						if(s_Description.contains("Public Liability")){
							CommonFunction.compareValues(c_SumIns , s_SumIns ,"Sum insured for activity  "+s_Activity +" of cover - "+s_CoverName);
							CommonFunction.compareValues(c_turnover , s_turnover ,"Turnover for activity  "+s_Activity +" of cover - "+s_CoverName);
						}						
						
						CommonFunction.compareValues(c_BookRatePerc, s_BookRatePerc,"BookRate % for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_BookP, s_BookP,"BookP for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(Math.abs(c_AutoAdjust), Math.abs(s_AutoAdjust),"AutoAdjust for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_BookRate, s_BookRate,"BookRate  for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"TechAdjust for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_RevisedP, s_RevisedP,"RevisedP for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_CommAdjust, s_CommAdjust,"CommAdjust for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_OverrideP, s_OverrideP,"PremiumOverride for activity "+s_Activity +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_FinalP, s_FinalP,"FinalPremium for activity "+s_Activity +" of cover - "+s_CoverName);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					c_TotalP = c_TotalP + c_FinalP;
				}
				break;
				
			case "Personal Accident Standard" :
				sCol_PremiumSummary = "PS_PersonalAccident_";
				customAssert.assertTrue(calculate_Book_Rate("PAS"), "Issue in book Rate calculation for Personal Accident Standard cover");
				
				for(int i = 0; i< totalRows-2; i++){
					s_Description = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[1]");
					s_Activity = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]");
					
					s_SumIns = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[3]"));
					s_BookRatePerc  = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[4]/input"));
					s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
					s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
					s_RevisedP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[7]/input"));
					s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[8]/input"));
					s_OverrideP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[9]/input"));
					s_FinalP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[10]/input"));
					
					if(s_Activity.contains("Clerical")){
						i_abvr = "PAS_Clerical_";
						c_SumIns = Double.parseDouble(pas_NoOfEmp_Clerical);
					}else if(s_Activity.contains("Drivers")){
						i_abvr = "PAS_Drivers_";
						c_SumIns = Double.parseDouble(pas_NoOfEmp_Drivers);
					}else{
						i_abvr = "PAS_Other_";
						c_SumIns = Double.parseDouble(pas_NoOfEmp_AllOthers);
					}
					
					c_TechAdjust = Double.parseDouble((String)map_data.get(i_abvr+"TechAdjust"));
					c_CommAdjust = Double.parseDouble((String)map_data.get(i_abvr+"CommAdjust"));
					c_OverrideP = Double.parseDouble((String)map_data.get(i_abvr+"PremiumOverride"));
					c_BookRatePerc  = common_CCD.Book_rate_Rater_output.get("PAS"+"_"+s_Activity);				
					c_BookP = (c_SumIns * c_BookRatePerc);
					
					c_RevisedP =  c_BookP + ((c_BookP * c_TechAdjust)/100);
					
					temp_P = c_RevisedP + ((c_RevisedP*c_CommAdjust)/100);
				
					if(c_OverrideP == 0.00){
						c_FinalP = temp_P;
					}else{
						c_FinalP = c_OverrideP;
					}
				
					// Compare values :
					
					try {
						
						CommonFunction.compareValues(c_SumIns , s_SumIns ,"Sum insured for activity  "+s_Description +" of cover - "+s_CoverName);										
						CommonFunction.compareValues(c_BookRatePerc, s_BookRatePerc,"BookRate % for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_BookP, s_BookP,"BookP for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"TechAdjust for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_RevisedP, s_RevisedP,"RevisedP for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_CommAdjust, s_CommAdjust,"CommAdjust for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_OverrideP, s_OverrideP,"PremiumOverride for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_FinalP, s_FinalP,"FinalPremium for activity "+s_Description +" of cover - "+s_CoverName);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					c_TotalP = c_TotalP + c_FinalP;				
				}
				break;
				
			case "Personal Accident Optional" :
				
				for(int i = 0; i< totalRows-2; i++){
					
					i_abvr = "PAO_ACT_";					
					
					sVal = (String)map_data.get(i_abvr+"TechAdjust");
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
					
					sVal = (String)map_data.get(i_abvr+"CommAdjust");
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(sVal);
					
					String pOverride = (String)map_data.get(i_abvr+"PremiumOverride");
					if(!pOverride.contains("0")){
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						driver.findElement(By.xpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[9]/input")).sendKeys(pOverride);
					}				
				}
				break;
				
			case "Goods in Transit" :
				
				sCol_PremiumSummary = "PS_GoodsinTransit_";
				customAssert.assertTrue(calculate_Book_Rate("GIT"), "Issue in book Rate calculation for GIT cover");
				
				for(int i = 0; i< totalRows-2; i++){
					s_Description = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[1]");
					
					s_SumIns = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]/input"));
					s_BookRatePerc  = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[3]/input"));
					s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[4]/input"));
					s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
					s_RevisedP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
					s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[7]/input"));
					s_OverrideP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[8]/input"));
					s_FinalP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[9]/input"));
					
					i_abvr = "GIT_";					
					
					sVal = (String)map_data.get(i_abvr+"NumberOfVehicles");
					String sValue = (String)map_data.get(i_abvr+"OneLoad");
										
					c_SumIns = Double.parseDouble(sVal)*Double.parseDouble(sValue);
					c_TechAdjust = Double.parseDouble((String)map_data.get(i_abvr+"TechAdjust"));
					c_CommAdjust = Double.parseDouble((String)map_data.get(i_abvr+"CommAdjust"));
					c_OverrideP = Double.parseDouble((String)map_data.get(i_abvr+"PremiumOverride"));
					c_BookRatePerc  = common_CCD.Book_rate_Rater_output.get("GIT"+"_"+s_Description);				
					c_BookP = ((c_SumIns * c_BookRatePerc)/100);
					
					c_RevisedP =  c_BookP + ((c_BookP * c_TechAdjust)/100);
				
					temp_P = c_RevisedP + ((c_RevisedP*c_CommAdjust)/100);
				
					if(c_OverrideP == 0.00){
						c_FinalP = temp_P;
					}else{
						c_FinalP = c_OverrideP;
					}
					

					// Compare values :
					
					try {
						
						CommonFunction.compareValues(c_SumIns , s_SumIns ,"Sum insured for activity  "+s_Description +" of cover - "+s_CoverName);										
						CommonFunction.compareValues(c_BookRatePerc, s_BookRatePerc,"BookRate % for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_BookP, s_BookP,"BookP for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"TechAdjust for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_RevisedP, s_RevisedP,"RevisedP for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_CommAdjust, s_CommAdjust,"CommAdjust for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_OverrideP, s_OverrideP,"PremiumOverride for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_FinalP, s_FinalP,"FinalPremium for activity "+s_Description +" of cover - "+s_CoverName);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					c_TotalP = c_TotalP + c_FinalP;
					
				}
				break;
				
			case "Terrorism" :
				
				sCol_PremiumSummary = "PS_Terrorism_";
				customAssert.assertTrue(calculate_Book_Rate("TER"), "Issue in book Rate calculation for Terrorism cover");
				
				for(int i = 0; i< totalRows-2; i++){
					
					s_Description = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[1]");
					
					s_SumIns = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]"));
					s_BookRatePerc  = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[3]/input"));
					s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[4]/input"));
					s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
					s_RevisedP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
					s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[7]/input"));
					s_OverrideP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[8]/input"));
					s_FinalP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[9]/input"));
					
					if(s_Description.contains("Business Interruption")){
						i_abvr = "Ter_BI_";
						c_SumIns = Ter_BI_Sum;
					}else if(s_Description.contains("Buildings and Contents")){
						i_abvr = "Ter_BC_";
						c_SumIns = Ter_BuildingContents_Sum;
					}	
					
					c_TechAdjust = Double.parseDouble((String)map_data.get(i_abvr+"TechAdjust"));
					c_CommAdjust = Double.parseDouble((String)map_data.get(i_abvr+"CommAdjust"));
					c_OverrideP = Double.parseDouble((String)map_data.get(i_abvr+"PremiumOverride"));
					if(s_Description.equalsIgnoreCase("Buildings and Contents")){
						c_BookRatePerc = Double.parseDouble((String)map_data.get("Ter_BC_BookRate"));

					}else{
					c_BookRatePerc  = common_CCD.Book_rate_Rater_output.get("TER"+"_"+s_Description);
					}
					c_BookP = ((c_SumIns * c_BookRatePerc)/100);
					
					c_RevisedP =  c_BookP + ((c_BookP * c_TechAdjust)/100);
				
					temp_P = c_RevisedP + ((c_RevisedP*c_CommAdjust)/100);
				
					if(c_OverrideP == 0.00){
						c_FinalP = temp_P;
					}else{
						c_FinalP = c_OverrideP;
					}
					
					// Compare values :
					
					try {
						
						CommonFunction.compareValues(c_SumIns , s_SumIns ,"Sum insured for activity  "+s_Description +" of cover - "+s_CoverName);										
						CommonFunction.compareValues(c_BookRatePerc, s_BookRatePerc,"BookRate % for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_BookP, s_BookP,"BookP for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"TechAdjust for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_RevisedP, s_RevisedP,"RevisedP for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_CommAdjust, s_CommAdjust,"CommAdjust for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_OverrideP, s_OverrideP,"PremiumOverride for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_FinalP, s_FinalP,"FinalPremium for activity "+s_Description +" of cover - "+s_CoverName);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					c_TotalP = c_TotalP + c_FinalP;
					
				}
				break;
				
			case "Legal Expenses" :
				
				sCol_PremiumSummary = "PS_LegalExpenses_";
				
				for(int i = 0; i< totalRows-2; i++){
					
					i_abvr = "LE_";									
					
					s_SumIns = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]/input"));
					s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[3]/input"));
					s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[4]/input"));
					s_RevisedP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
					s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
					s_OverrideP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[7]/input"));
					s_FinalP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[8]/input"));
					
					c_SumIns = Double.parseDouble((String)map_data.get("LE_TotalWages"));
					c_BookP = common_CCD.calculate_LE_Book_Premium(map_data);
					c_TechAdjust = Double.parseDouble((String)map_data.get(i_abvr+"TechAdjust"));
					c_CommAdjust = Double.parseDouble((String)map_data.get(i_abvr+"CommAdjust"));
					c_OverrideP = Double.parseDouble((String)map_data.get(i_abvr+"PremiumOverride"));
					
					c_RevisedP =  c_BookP + ((c_BookP * c_TechAdjust)/100);
					
					temp_P = c_RevisedP + ((c_RevisedP*c_CommAdjust)/100);
				
					if(c_OverrideP == 0.00){
						c_FinalP = temp_P;
					}else{
						c_FinalP = c_OverrideP;
					}
					
					// Compare values :
					
					try {
						
						CommonFunction.compareValues(c_SumIns , s_SumIns ,"Sum insured for activity  "+s_Description +" of cover - "+s_CoverName);										
						CommonFunction.compareValues(c_BookP, s_BookP,"BookP for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"TechAdjust for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_RevisedP, s_RevisedP,"RevisedP for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_CommAdjust, s_CommAdjust,"CommAdjust for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_OverrideP, s_OverrideP,"PremiumOverride for activity "+s_Description +" of cover - "+s_CoverName);
						CommonFunction.compareValues(c_FinalP, s_FinalP,"FinalPremium for activity "+s_Description +" of cover - "+s_CoverName);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					c_TotalP = c_TotalP + c_FinalP;	
				}
				break;
		}
		
	  TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), sCol_PremiumSummary+"NetNetPremium", String.valueOf(c_TotalP), map_data);
		return retVal;
	}

/**
 * 
 * This method handles CCD Flat Premium screens scripting.
 * 
 */

public boolean func_Flat_Premiums_(Map<Object, Object> map_data,Map<String, List<Map<String, String>>> internal_data_map){
	
	boolean retvalue = true;
	String isFlatPremium=null;
	try{
		isFlatPremium = (String)map_data.get("FP_isFlatPremium");
		if(isFlatPremium.equalsIgnoreCase("Yes"))
			customAssert.assertTrue(common.funcButtonSelection("Flat Premiums"), "Error while clicking on Flat Premiums button .");
		else
			return true;
		
		customAssert.assertTrue(common.funcPageNavigation("Flat Premiums", ""), "Navigation Problem to Flat Premiums page .");
		
		String[] fp_entries = ((String)map_data.get("FP_FlatPremium_Entries")).split(";");
		int no_of_fp_e = fp_entries.length;
		int[] total_fp_e = new int[no_of_fp_e];
		k.ImplicitWaitOff();
		for(int count=1;count<=no_of_fp_e;count++){
			
			customAssert.assertTrue(k.Click("POF_Add_Flat_P_btn"),"Unable to Click Add Flat Premium button . ");
			customAssert.assertTrue(Verify_FP_Section_Values(),"Error while verifying covers list in flat premium section dropdown . ");

			customAssert.assertTrue(k.DropDownSelection("POF_FP_Section", internal_data_map.get("Flat-Premiums").get(count-1).get("FP_Section")),"Unable to enter FP_Section in Flat Premium page .");
			customAssert.assertTrue(k.Input("POF_FP_Premium", internal_data_map.get("Flat-Premiums").get(count-1).get("FP_Premium")),"Unable to enter FP_Premium in Flat Premium page .");
			customAssert.assertTrue(k.Input("POF_FP_TaxRate", internal_data_map.get("Flat-Premiums").get(count-1).get("FP_TaxRate")),"Unable to enter FP_TaxRate in Flat Premium page .");
			customAssert.assertTrue(k.Input("POF_FP_Description", internal_data_map.get("Flat-Premiums").get(count-1).get("FP_Description")),"Unable to enter FP_Description in Flat Premium page .");
			
			customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"), "Unable to click Inner Save button on Flat Premiums .");
			
			customAssert.assertTrue(get_Flat_Premium_Entries(count), "Error while reading Flat Premium Entries .");
			common_HHAZ.func_FP_Entries_Verification_MTA(internal_data_map.get("Flat-Premiums").get(count-1).get("FP_Section"),internal_data_map,count);
			
			//For each added entry in FP, cover name will be removed from Section List
			common_HHAZ.CoversDetails_data_list.remove(internal_data_map.get("Flat-Premiums").get(count-1).get("FP_Section").replaceAll(" ", ""));
		}
				
		driver.findElements(By.xpath("//*[@id='ex-back']")).get(0).click();
	
		TestUtil.reportStatus("Entered and Verified all the details on Flat Premiums page .", "Info", true);
		k.ImplicitWaitOn();
		return retvalue;
		
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Unable to to do operation on Flat Premium page. \n", t);k.ImplicitWaitOn();
        return false;
 }
}

public boolean get_Flat_Premium_Entries(int row_index){
	
	
	try{
	String testName = (String)common.MTA_excel_data_map.get("Automation Key");
	
	customAssert.assertTrue(common.funcPageNavigation("Flat Premiums", ""),"Flat Premiums page navigations issue(S)");
	
	if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
		int policy_Duration = Integer.parseInt((String)common.Renewal_excel_data_map.get("PS_Duration"));
	}else{
		int policy_Duration = Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"));
	}
	
	
	String FPTble_xpath = "//table[@id='table0']";
	WebElement fp_Table = driver.findElement(By.xpath(FPTble_xpath));
	
	List<WebElement> colms = fp_Table.findElements(By.tagName("th"));
		//Map<SPI,MAP<NNP,12345.67>>
	
	int fp_tble_Rows = fp_Table.findElements(By.tagName("tr")).size();
	int fp_tble_Cols = colms.size();
	
	List<String> sectionNames = new ArrayList<>();
	String sectionName = null;
	String sectionValue = null;
	String headerName = null;
	
		
	if(fp_Table.isDisplayed()){
		
		//For Each Cover Row
		for(int row = row_index; row < fp_tble_Rows ;row ++){
			
			WebElement sec_Name = driver.findElement(By.xpath(FPTble_xpath+"//tbody//tr["+row+"]//td["+1+"]"));
			sectionName = sec_Name.getText();
			
			switch(sectionName){
			
			case "Totals":
				Map<String,Double> fp_Section_Vals_Total = new HashMap<>();
				//For Each Cols
				for(int col = 2; col <= fp_tble_Cols ;col ++){
			
					////p[text()=' Transaction Premium']//following-sibling::table[@id='table0']//thead//th[2]
					WebElement header_Name = driver.findElement(By.xpath(FPTble_xpath+"//thead//th["+col+"]"));
					headerName = header_Name.getText();
				
					if(!headerName.contains("Pen Comm %") && !headerName.contains("Broker Comm %") && !headerName.contains("Gross Comm %")
							&& !headerName.contains("Insurance Tax Rate") ){
						WebElement sec_Val = driver.findElement(By.xpath(FPTble_xpath+"//tbody//tr["+row+"]//td["+col+"]"));
						sectionValue = sec_Val.getText();
						sectionValue = sectionValue.replaceAll(",", "");
						fp_Section_Vals_Total.put(headerName, Double.parseDouble(sectionValue));
						
					}else{
						continue;
					}
					common.transaction_Details_Premium_Values.put(sectionName, fp_Section_Vals_Total);
			}
			
			break;
			
			default:
				Map<String,Double> fp_Section_Vals = new HashMap<>();
				//For Each Cols
				for(int col = 2; col <= fp_tble_Cols-2 ;col ++){
			
					////p[text()=' Transaction Premium']//following-sibling::table[@id='table0']//thead//th[2]
					WebElement header_Name = driver.findElement(By.xpath(FPTble_xpath+"//thead//th["+col+"]"));
					headerName = header_Name.getText();
					
					if(headerName.equalsIgnoreCase("Pen Commm")){
						headerName = "Pen Comm";
					}
				
					WebElement sec_Val = driver.findElement(By.xpath(FPTble_xpath+"//tbody//tr["+row+"]//td["+col+"]"));
					sectionValue = sec_Val.getText();
				
					fp_Section_Vals.put(headerName, Double.parseDouble(sectionValue));
			}
				common.transaction_Details_Premium_Values.put(sectionName+"_FP", fp_Section_Vals);
			
			break;
			
			}
			
		}
	
	}

	
}catch(Throwable t){
	
	//System.out.println("Error while reading Flat Premium Entries . ");
	return false;
	
	
}
	
	return true;
	
}

public boolean Verify_FP_Section_Values(){
	
	try{
		 
		int count = 0 , count_datasheet = 0;
		 String coverName = null , coverName_datasheet = null;
		 
		 	List<WebElement> names = driver.findElements(By.tagName("option"));
			List<String> coversNameList = new ArrayList<>();
			String coverName_withoutSpace =null,key=null;
			
			for(int i=0;i<names.size();i++){
				coverName = names.get(i).getText();
					
				coverName_withoutSpace = coverName.replaceAll(" ", "");
			
				coversNameList.add(coverName_withoutSpace);
				
				if(common.currentRunningFlow.equalsIgnoreCase("MTA")){
						key = "CD_"+coverName_withoutSpace;
								
						if(common.MTA_excel_data_map.get(key).toString().equalsIgnoreCase("Yes")){
							continue;
						}else{
							if(common.MTA_excel_data_map.get("CD_Add_"+coverName_withoutSpace.replaceAll(" ", "")).toString().equalsIgnoreCase("Yes")){
						
							}else{
								TestUtil.reportStatus("Cover Name <b>  ["+coverName+"]  </b> should not present in the flat fremium section dropdown list as This cover is not selected on Cover Details page.", "FAIL", false);
							count++;
						}
					
					}
					}
				} //For loop
		 
			for(int p=0;p<common_HHAZ.CoversDetails_data_list.size();p++){
				coverName_datasheet = common_HHAZ.CoversDetails_data_list.get(p);
				
				if(coversNameList.contains(coverName_datasheet)){
					continue;
				}else{
					TestUtil.reportStatus("Cover Name <b>  ["+coverName_datasheet+"]  </b> is selected as 'NO' in datasheet but still listed in the flat premium section dropdown list.", "FAIL", false);
					count_datasheet++;
				}
			}
		 
		 
	
	}catch(Throwable t){
		return false;
		
	}
	
	return true;	
	
} 


//MTA

public boolean funcCreateEndorsement(){
	
  boolean retvalue = true;
  Date dateobj = null;
  String testName = null;
  df = new SimpleDateFormat("dd/MM/yyyy");
  
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
  
  try {
  	customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"), "Navigation problem to Premium Summary page .");
  	customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page is not loaded to perfrm MTA . ");
 
		customAssert.assertTrue(common.funcButtonSelection("Create Endorsement"), "Unable to click on Create Endorsement button .");
		
  	int ammendmet_period = Integer.parseInt((String)data_map.get("MTA_EndorsementPeriod"));
  	
  	if(CommonFunction.businessEvent.equalsIgnoreCase("Renewal")){
  		if(ammendmet_period > Integer.parseInt((String)common.Renewal_excel_data_map.get("PS_Duration"))){
      		TestUtil.reportStatus("Amendement Period Should not be greater than Policy Duration", "Fail", true);
      		return false;
      	}
  	}else{
  		if(ammendmet_period > Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"))){
      		TestUtil.reportStatus("Amendement Period Should not be greater than Policy Duration", "Fail", true);
      		return false;
      	}
  	}
  	TimeZone uk_timezone = TimeZone.getTimeZone("Europe/London");
  	Calendar c = Calendar.getInstance(uk_timezone);
  	
  	if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
  		int d = Integer.parseInt(((String)data_map.get("PS_PolicyStartDate")).substring(0, 2));
  		dateobj = common.addDays(df.parse((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate")), ammendmet_period);
  	}else if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
  		int d = Integer.parseInt(((String)common.NB_excel_data_map.get("PS_PolicyStartDate")).substring(0, 2));
  		dateobj = common.addDays(df.parse((String)common.NB_excel_data_map.get("PS_PolicyStartDate")), ammendmet_period);
  	}else{
  		c.add(Calendar.DATE, ammendmet_period);
  	  	dateobj = df.parse(df.format(c.getTime()));
  	}
  	
  	
  	
  
      customAssert.assertTrue(k.Click("SPI_Endorsement_eff_date"), "Unable to enter Endorsement effective Date.");
      customAssert.assertTrue(k.Type("SPI_Endorsement_eff_date", df.format(dateobj)), "Unable to Enter Endorsement effective Date .");
      customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
      customAssert.assertTrue(k.Input("SPI_Reson_for_Endors", (String)data_map.get("MTA_Reason_for_Endorsement")),"Unable to Enter Reason for Endorsement");
      
      if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
      	WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "MTA_Endorsement", testName, "MTA_EffectiveDate", k.getAttribute("SPI_Endorsement_eff_date", "value"),data_map);
      }else{
      	WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", testName, "MTA_EffectiveDate", k.getAttribute("SPI_Endorsement_eff_date", "value"),data_map);
      }
      customAssert.assertTrue(common.funcButtonSelection("Create Endorsement"), "Unable to click on Create Endorsement button .");
     	
      //Writing to MTA Excel
  	WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "MTA_Endorsement", testName, "MTA_PolicyNumber", k.getText("SPI_MTA_policy_number"),data_map);
  	if(common.currentRunningFlow.equals("MTA")){
  		if(((String)data_map.get("MTA_Status")).equals("Endorsement Rewind"))
  			WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "MTA_Endorsement", testName, "MTA_isMTARewind", "Y",data_map);
  		else
  			WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "MTA_Endorsement", testName, "MTA_isMTARewind", "N",data_map);
  	}
      
  	TestUtil.reportStatus("Create Endorsement Details filled successfully . ", "Info", true);
		
      return retvalue;
  
		} catch (ParseException e) {
			//System.out.println("Unable to Parse Endorsement Dates - "+e.getMessage());
			return false;
		}
  	catch(Throwable t) {
           String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
           TestUtil.reportFunctionFailed("Failed in "+methodName+" function");  
           k.reportErr("Failed in "+methodName+" function", t);
           return false;
    }  
}
	
	// Cancellation starts here :

public void CancellationFlow(String code,String event) throws ErrorInTestMethod{
	
	String testName = (String)common.CAN_excel_data_map.get("Automation Key");
	try{
		if(((String)common.CAN_excel_data_map.get("CAN_ExistingPolicy")).equalsIgnoreCase("Yes")) {
			customAssert.assertTrue(common_EP.ExistingPolicyAlgorithm(common.CAN_excel_data_map , (String)common.CAN_excel_data_map.get("CAN_ExistingPolicy_Type"), (String)common.CAN_excel_data_map.get("CAN_ExistingPolicy_Status")), "Existing Policy Algorithm function is having issues. ");
		}else{
			NewBusinessFlow(code,"NB");
		}	
		common.currentRunningFlow="CAN";
		System.out.println("Test method of CAN For - "+code);
		
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


public boolean CancelPolicy(Map<Object, Object> map_data) throws ErrorInTestMethod{
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
                        
           customAssert.assertTrue(k.Click("CCD_CP_CancellationDate"), "Unable to enter Cancellation date.");
           customAssert.assertTrue(k.Input("CCD_CP_CancellationDate", Cancellation_date),"Unable to Enter Cancellation date.");
           customAssert.assertTrue(k.Click("SPI_Btn_Calender_Done"), "Unable to click on done button in calander.");
           customAssert.assertTrue(!k.getAttributeIsEmpty("CCD_CP_CancellationDate", "value"),"CCD_CP_CancellationDate Field Should Contain Valid Value on Cancel Policy page .");
           
           common.CAN_excel_data_map.put("Cancellation_date", Cancellation_date);
           common.CAN_excel_data_map.put("CP_CancellationDate", Cancellation_date);
           
           customAssert.assertTrue(k.Input("CCD_CP_CancellationReason", (String)map_data.get("CP_CancellationReason")),"Unable to Enter Cancellation Reason.");
           customAssert.assertTrue(common.funcButtonSelection("Continue"), "Unable to click on Cancel Policy Button");
                        
           // Read Cancellation Return Premium Summary and put values to Map  :
           
           Cancel_RetrunPremiumTable(map_data);
           
           customAssert.assertTrue(k.Click("COB_Btn_FinalCancelPolicy"), "Unable to click on Cancel Polcy button after verifying Cancellation Return Premium");
           customAssert.assertTrue(k.AcceptPopup(), "Unable to handl pop up");
                  
           return retVal;
           
    
    }catch(Throwable t){
          return false;
    }           
}

public boolean Cancel_RetrunPremiumTable(Map<Object, Object> map_data) throws ErrorInTestMethod{
	boolean retVal = true;
	String testName = (String)common.CAN_excel_data_map.get("Automation Key");
	
	try{
					
		int policy_Duration = Integer.parseInt(k.getText("CCD_CAN_Duration"));
		int DaysRemain = Integer.parseInt(k.getText("CCD_CAN_DaysRemain"));
		
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
										common_CCD.CAN_CCD_ReturnP_Values_Map.put(sectionName, ReturnP_Table_TotalVal);
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
								
								common_CCD.CAN_CCD_ReturnP_Values_Map.put(sectionName, ReturnP_Table_CoverVal);
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
						Can_returnP_Error = Can_returnP_Error + Can_ReturnP_Total_Validation(sectionNames,common_CCD.CAN_CCD_ReturnP_Values_Map);
					else
						Can_returnP_Error = Can_returnP_Error + CanReturnPTable_CoverSection_Validation(policy_Duration,DaysRemain, s_Name, common_CCD.CAN_CCD_ReturnP_Values_Map);								
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
					exp_value = exp_value + common_CCD.CAN_CCD_ReturnP_Values_Map.get(section).get("Net Net Premium");
			}
			
			String canRP_NetNetP_actual = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Net Net Premium"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_NetNetP_actual)," Net Net Premium");

			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_CCD.CAN_CCD_ReturnP_Values_Map.get(section).get("Pen Comm");
			}
			String canRP_pc_actual = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Pen Comm"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_pc_actual)," Pen Commission");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_CCD.CAN_CCD_ReturnP_Values_Map.get(section).get("Net Premium");
			}
			String canRP_netP_actual = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Net Premium"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_netP_actual),"Net Premium");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_CCD.CAN_CCD_ReturnP_Values_Map.get(section).get("Broker Commission");
			}
			String canRP_bc_actual =  Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Broker Commission"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_bc_actual),"Broker Commission");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_CCD.CAN_CCD_ReturnP_Values_Map.get(section).get("Gross Premium");
			}
			String canRP_grossP_actual = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Gross Premium"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_grossP_actual)," Gross Premium");
		
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_CCD.CAN_CCD_ReturnP_Values_Map.get(section).get("Insurance Tax");
			}
			String canRP_InsuranceTax_actual = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Insurance Tax"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_InsuranceTax_actual),"Insurance Tax");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_CCD.CAN_CCD_ReturnP_Values_Map.get(section).get("Total Premium");
			}
			String canRP_p_actual = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Total Premium"));
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
		
	code = sectionNames.replaceAll(" ", "");
		
	try{
			
			TestUtil.reportStatus("---------------"+sectionNames+"-----------------","Info",false);
			
			if(code.contains("LegalExpenses")){
				code = "LegalExpenses";
			}
			if(code.contains("BusinesssInterruption")){
				code = "BusinessInterruption";
			}
			double annual_NetNetP = Double.parseDouble((String)map_data.get("PS_"+code+"_NetNetPremium"));
			String canRP_NetNetP_expected = Double.toString((annual_NetNetP/365)*DaysRemain);
			String canRP_NetNetP_actual = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionNames).get("Net Net Premium"));
			CommonFunction.compareValues(Double.parseDouble(canRP_NetNetP_expected),Double.parseDouble(canRP_NetNetP_actual)," Net Net Premium");
			
			//COB CAN Transaction Pen commission Calculation : 
			double canRP_pen_comm = (( Double.parseDouble(canRP_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate"))/100)));
			String canRP_pc_expected = common.roundedOff(Double.toString(canRP_pen_comm));
			String canRP_pc_actual = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionNames).get("Pen Comm"));
			CommonFunction.compareValues(Double.parseDouble(canRP_pc_expected),Double.parseDouble(canRP_pc_actual)," Pen Commission");
			
			
			//COB CAN Transaction Net Premium verification : 
			double canRP_netP = Double.parseDouble(canRP_pc_expected) + Double.parseDouble(canRP_NetNetP_expected);
			String canRP_netP_expected = common.roundedOff(Double.toString(canRP_netP));
			String canRP_netP_actual = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionNames).get("Net Premium"));
			CommonFunction.compareValues(Double.parseDouble(canRP_netP_expected),Double.parseDouble(canRP_netP_actual),"Net Premium");
			
			
			//COB CAN Transaction Broker commission Calculation : 
			double canRP_broker_comm = ((Double.parseDouble(canRP_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate"))/100)));
			String canRP_bc_expected = common.roundedOff(Double.toString(canRP_broker_comm));
			String canRP_bc_actual =  Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionNames).get("Broker Commission"));
			CommonFunction.compareValues(Double.parseDouble(canRP_bc_expected),Double.parseDouble(canRP_bc_actual),"Broker Commission");
			
			
			//COB CAN Transaction GrossPremium verification : 
			double canRP_grossP = Double.parseDouble(canRP_netP_expected) + Double.parseDouble(canRP_bc_expected);
			String canRP_grossP_actual = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionNames).get("Gross Premium"));
			CommonFunction.compareValues(canRP_grossP,Double.parseDouble(canRP_grossP_actual),sectionNames+" Transaction Gross Premium");
			
			
			double canRP_InsuranceTax = (canRP_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_IPT")))/100.0;
			canRP_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(canRP_InsuranceTax)));
			String canRP_InsuranceTax_actual = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionNames).get("Insurance Tax"));
			CommonFunction.compareValues(canRP_InsuranceTax,Double.parseDouble(canRP_InsuranceTax_actual),"Insurance Tax");
						
			//COB CAN  Transaction Total Premium verification : 
			double canRP_Premium = canRP_grossP + canRP_InsuranceTax;
			String canRP_p_expected = common.roundedOff(Double.toString(canRP_Premium));
			
			String canRP_p_actual = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionNames).get("Total Premium"));
			
			double premium_diff = Double.parseDouble(canRP_p_expected) - Double.parseDouble(canRP_p_actual);
			
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

public boolean Cancel_PremiumSummary(Map<Object, Object> map_data) throws ErrorInTestMethod{
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
					
					common.compareValues(s_NetNetP, common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionName).get("Net Net Premium"), "Cancellation Net Net Premium");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_PenCommRate,common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionName).get("Pen Comm %"), "Cancellation Pen Comm Percentage");
					}				
					common.compareValues(s_PenCommAmt, common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionName).get("Pen Comm"), "Cancellation Pen commm Amount");
					common.compareValues(s_NetP, common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionName).get("Net Premium"), "Cancellation Net Premium");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_BrokerCommRate, common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionName).get("Broker Comm %"), "Cancellation Broker Comm rate");
					}
					
					common.compareValues(s_BrokerCommAmt, common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionName).get("Broker Commission"), "Cancellation Broker commission amount");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_GrossCommRate, common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionName).get("Gross Comm %"), "Cancellation Gross comm rate");
					}
					
					common.compareValues(s_GrossCommAmt, common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionName).get("Gross Premium"), "Cancellation Gross Premium");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_InsTRate, common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionName).get("Insurance Tax Rate"), "Cancellation Ins tax rate");
					}
					
					common.compareValues(s_InsTAmt, common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionName).get("Insurance Tax"), "Cancellation Ins tax Premium");
					common.compareValues(s_TotalP, common_CCD.CAN_CCD_ReturnP_Values_Map.get(sectionName).get("Total Premium"), "Cancellation Total Premium");
										
				}	
			}
				
			return retVal;		
	
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}

public boolean funcUpdateCoverDetails_MTA(Map<Object, Object> map_data){
	   
	try {
			customAssert.assertTrue(common.funcPageNavigation("Covers", ""),"Cover page is having issue(S)");
			String coverName = null;
			String c_locator = null;
			common.CoversDetails_data_list = new ArrayList<>();
			k.pressDownKeyonPage();
			String all_cover = ObjectMap.properties.getProperty(CommonFunction.product+"_CD_AllCovers");
			String[] split_all_covers = all_cover.split(",");
			for(String coverWithLocator : split_all_covers){
				String coverWithoutLocator = coverWithLocator.split("__")[0];
				try{
					//CoversDetails_data_list.add(coverWithoutLocator);
					coverName = coverWithLocator.split("__")[0];	
					c_locator = coverWithLocator.split("__")[1];
					k.waitTwoSeconds();
					if(c_locator.equals("md")){
						
						
						if (!driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"')]")).isSelected()){
							if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("No"))
								continue;
							else
					 			customAssert.assertTrue(common_Zennor.selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
						}else{
							if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("Yes")){
								if(common.currentRunningFlow.equalsIgnoreCase("MTA"))
									common.CoversDetails_data_list.add(coverName);
								continue;
							}
							else
								customAssert.assertTrue(common_Zennor.deSelectCovers(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
						}
					
					}else if(c_locator.equals("PEL")){
						
					}else{
						if (!driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"')]")).isSelected()){
							JavascriptExecutor j_exe = (JavascriptExecutor) driver;
							j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"')]")));
							
								if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("No"))
									continue;
								else
						 			customAssert.assertTrue(common_Zennor.selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
													
							}else{
								if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("Yes")){
									if(common.currentRunningFlow.equalsIgnoreCase("MTA"))
										common.CoversDetails_data_list.add(coverName);
									continue;
								}
								else
									customAssert.assertTrue(common_Zennor.deSelectCovers(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
							 }
					
					}	
					
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
 * This method gives MF&D pages referrals texts."
 * 
 *
 */
public boolean func_Referrals_MaterialFactsDeclerationPage(){
	
	boolean retValue = true;
	
	Map<Object,Object> map_data=null;
	Properties CCD_referrals = OR.getORProperties();
	
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
		 //Have there been any previous claims  in the last 5 years?
		 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_previous_claims");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"previousClaims"));
		 }catch(Throwable t){
			 }
		 
		 try{
		 //Has any proposer, director or partner of the Trade or Business or its Subsidiary Companies ever, either personally or in any business capacity, had a proposal refused or declined or claim repudiated or ever had an insurance cancelled, renewal refused or had special terms imposed?
		 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_special_terms_imposed");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"specialTermsImposed"));
		 }catch(Throwable t){
		 }
		 
		 try{
		 //Has any proposer, director or partner of the Trade or Business or its Subsidiary Companies ever, either personally or in any business capacity had any convictions, criminal offences or prosecutions pending other than motor offences?
		 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_motor_offences");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"motorOffences"));
		 }catch(Throwable t){
		 }
		 
		 try{
		 //Has any proposer, director or partner of the Trade or Business or its Subsidiary Companies ever, either personally or in any business capacity been declared bankrupt or insolvent or been the subject of bankruptcy proceedings or receivership/ insolvency proceedings?
		 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_bankrupt_or_insolvent");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"bankruptOrInsolvent"));
		 }catch(Throwable t){
		 }
		 
		 try{
		 //Involved in another company within 6 months before receivership/insolvency?
		 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_6_months_receivership_insolvency");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"6monthsReceivershipInsolvency"));
		 }catch(Throwable t){
		 }
			
		 try{
		/* A director or partner in any business which has been the subject of an individual voluntary 
			 arrangement with creditors, voluntary liquidation, a winding up or administrative order, or 
			 administrative receivership proceedings?*/
		 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_administrative_receivership_proceedings");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"administrativeReceivershipProceedings"));
		 }catch(Throwable t){
		 }
		
		 try{
		 //Has any proposer, director or partner of the Trade or Business or its Subsidiary Companies ever, either personally or in any business capacity been the owner or director of, or partner in, any business, company or partnership had a county court judgement awarded against them?
		 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_awarded_against_them");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"awardedAgainstThem"));
		 }catch(Throwable t){
		 }
		
		 try{
		 //Do you use high pressure water jetting equipment?
		 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_jetting_equipment");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"jettingEquipment"));
		 }catch(Throwable t){
		 }
		
		 try{
		 //Does a senior person have overall reponsibility for health and safety
		 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_senior_person");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"seniorPerson"));
		 }catch(Throwable t){
		 }
		 
		 try{
		 //Have you appointed a competent person to advise you on health and safety matters?
		 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_health_safety_matters");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"healthSafetyMatters"));
		 }catch(Throwable t){
		 }
		 
		 try{
			
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_representative_company");
			 if(mfd_q_value.equalsIgnoreCase("Yes"))
				 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"representativeCompany"));
			 }catch(Throwable t){
			 }
		 
		 try{
			
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_Risk_Assessments");
			 if(mfd_q_value.equalsIgnoreCase("Yes"))
				 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"RiskAssessments"));
			 }catch(Throwable t){
			 }
		 
		 try{
			 
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_safe_working_procedures");
			 if(mfd_q_value.equalsIgnoreCase("Yes"))
				 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"safeWorkingProcedures"));
			 }catch(Throwable t){
			 }
		 
		 try{
			 
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_employees_undertake");
			 if(mfd_q_value.equalsIgnoreCase("Yes"))
				 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"employeesUndertake"));
			 }catch(Throwable t){
			 }
		 
		 try{
			
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_hazardous_to_health");
			 if(mfd_q_value.equalsIgnoreCase("Yes"))
				 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"hazardousToHealth"));
			 }catch(Throwable t){
			 }
		 
		 try{

			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_Noise_Assessment");
			 if(mfd_q_value.equalsIgnoreCase("Yes"))
				 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"NoiseAssessment"));
			 }catch(Throwable t){
			 }
		 
		 try{
			 
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_third_party_ladders");
			 if(mfd_q_value.equalsIgnoreCase("Yes"))
				 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"thirdPartyLadders"));
			 }catch(Throwable t){
			 }
		 
		 try{
			 
			 mfd_q_value = k.GetDropDownSelectedValueIgnoreError("CCD_MFD_confined_space_work");
			 if(mfd_q_value.equalsIgnoreCase("Yes"))
				 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"confinedSpaceWork"));
			 }catch(Throwable t){
			 }
		 
		 
		 //Do you knowingly - Multi-select list
		 try{
			 List<WebElement> _dyknow_MF = driver.findElements(By.xpath("//*[text()='Do you knowingly']//following::ul[1]//li"));
			 String know_value =null,exp_val=null;
			 for(WebElement know_:_dyknow_MF){
				 try{
					 know_value = know_.getAttribute("title");
				 }catch(Throwable t){
					 know_value="None";
					 
					 }
				 exp_val = CCD_referrals.getProperty("CCD_MFD_DoYouKnow_title");
				 if(know_value.equalsIgnoreCase(exp_val)){
				 	 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"DoYouKnowingly"));
				 }
				 
			 }
		 }catch(Throwable t){
			 
		 }
		
		 
		 // Do you operate? - Multi Select list
		 try{
			 List<WebElement> _Operate = driver.findElements(By.xpath("//*[text()='Do you operate?']//following::ul[1]//li"));
			 String _operate_value =null,exp_val=null;
			 for(WebElement prd_elm:_Operate){
				 try{
					 _operate_value = prd_elm.getAttribute("title");
				 }catch(Throwable t){
					 _operate_value="None";
					 
					 }
				 
				 exp_val = CCD_referrals.getProperty("CCD_MFD_DoYouOperate_title");
				 if(_operate_value.equalsIgnoreCase(exp_val)){
				 	 common_HHAZ.referrals_list.add((String)map_data.get(mf_key+"DoYouOperate"));
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

//For EL- Additional Extensions
public boolean is_BI_referral_activity(String bi_Activity)
{
	
	CCD_Rater = OR.getORProperties();
	int f=0;
	String bi_act_list = CCD_Rater.getProperty("CCD_BI_Referral_Activities");
	String[] list_act = bi_act_list.split(":");
	
	for(String bi_ext : list_act){
		if(bi_ext.equalsIgnoreCase(bi_Activity))
		{
			f=1;
			return true;
		}
	}
	if(f==0){
		return false;
	}
	return false;
		
}

//For EL- Activities
public boolean is_EL_referral_activity(String el_Activity)
{
	
	CCD_Rater = OR.getORProperties();
	int f=0;
	String el_act_list = CCD_Rater.getProperty("CCD_EL_Referral_Activities");
	String[] list_act = el_act_list.split(":");
	
	for(String el_act : list_act){
		if(el_act.equalsIgnoreCase(el_Activity))
		{
			f=1;
			return true;
		}
	}
	if(f==0){
		return false;
	}
	return false;
		
}

//For PL- Activities
public boolean is_PL_referral_activity(String pl_Activity)
{
	
	CCD_Rater = OR.getORProperties();
	int f=0;
	String pl_act_list = CCD_Rater.getProperty("CCD_PL_Referral_Activities");
	String[] list_act = pl_act_list.split(":");
	
	for(String pl_act : list_act){
		if(pl_act.equalsIgnoreCase(pl_Activity))
		{
			f=1;
			return true;
		}
	}
	if(f==0){
		return false;
	}
	return false;
		
}

//For PL- Activities
public boolean is_Trade_referral_activity(String trade_Activity)
{
	
	CCD_Rater = OR.getORProperties();
	int f=0;
	String pl_act_list = CCD_Rater.getProperty("CCD_Trade_Referral_Activities");
	String[] list_act = pl_act_list.split(":");
	
	for(String pl_act : list_act){
		if(pl_act.equalsIgnoreCase(trade_Activity))
		{
			f=1;
			return true;
		}
	}
	if(f==0){
		return false;
	}
	return false;
		
}

//This function is to check " if Gross Premium generated is less than Minimum Premium
	//Applicable Covers --> MD,BI,money,EL , PL,GIT
public boolean func_Check_Section_Minimum_Gross_Premium(String section,double grossPremium){
	
			
			CCD_Rater = OR.getORProperties();
			double _Min_Premium_=0.0;
			Map<Object,Object> data_map = new HashMap<>();
			
			switch(common.currentRunningFlow){
			case "NB":
				
				data_map = common.NB_excel_data_map;
			break;
			case "CAN":
				
				data_map = common.CAN_excel_data_map;
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
				
				_Min_Premium_ = get_Section_Min_Premium_from_Properties(section);
				
				section = section.replace(" ", "");
				if(grossPremium < _Min_Premium_){ //Section Minimum Premium Rule
					common_HHAZ.referrals_list.add((String)data_map.get("RM_PremiumSummary_Section"+section));
				}
					
				return true;
			}catch(Throwable t){
				
				return false;
			}
			
			
}

public double get_Section_Min_Premium_from_Properties(String section){
	
	double r_value=0.0;
	DecimalFormat formatter = new DecimalFormat("###.##");

	try{
		CCD_Rater = OR.getORProperties();
		String _section = section.replaceAll(" ", "");
		if(section.equals("Policy"))
			_section = "Policy_Minimum_Premium_MP";
		else	
			_section = _section +"_MP";
		
		r_value = Double.parseDouble(CCD_Rater.getProperty(_section));
		r_value = Double.valueOf(formatter.format(r_value));
		
	}catch(Throwable t ){
		//System.out.println("Error while getting Section Minimum Premium for Section > "+section+" < "+t.getMessage());
	}
	return r_value;
		
}

public boolean Carspage(Map<Object, Object> map_data) {
	try{
		CarsNetP = 0.00;
		customAssert.assertTrue(common.funcPageNavigation("Cars",""), "Cars Page Navigation issue . ");
		if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }
			
		String[] SpecifiedUnspecified = ((String)map_data.get("MFB_CARS_Specified")).split(",");
		List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
		for(String geo_limit : SpecifiedUnspecified){
			for(WebElement each_ul : ul_elements){
				customAssert.assertTrue(k.Click("MFB_CARS_Specified"),"Error while Clicking Specified/ Unspecified cars List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed()){
					each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
					//driver.findElement(By.xpath("//*[@id='mainpanel']")).click();
					}
					
				else
					continue;
				break;
				
			}
		}
				
		customAssert.assertTrue(k.DropDownType("MFB_CARS_Drivers", (String)map_data.get("MFB_CARS_Drivers")),"Unable to select Drivers value");
		customAssert.assertTrue(k.Input("MFB_CARS_AccidentalDamage", (String)map_data.get("MFB_CARS_AccidentalDamage")),	"Unable to enter value in Accidental Name Name  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARS_AccidentalDamage", "value"),"Accidental Damage Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("MFB_CARS_FireEccess", (String)map_data.get("MFB_CARS_FireEccess")),	"Unable to enter value in FireAccess  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARS_FireEccess", "value"),"Fire Access Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("MFB_CARS_TheftEccess", (String)map_data.get("MFB_CARS_TheftEccess")),	"Unable to enter value in TheftEccess Name Name  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARS_TheftEccess", "value"),"TheftEccess Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("MFB_CARS_WindscreenEccess", (String)map_data.get("MFB_CARS_WindscreenEccess")),	"Unable to enter value in WindscreenEccess Name  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARS_WindscreenEccess", "value"),"WindscreenEccess Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.DropDownSelection("MFB_CARS_HighRiskVehicles", (String)map_data.get("MFB_CARS_HighRiskVehicles")),"Unable to select HighRiskVehicles value");
		
		if(k.getText("MFB_CARS_Specified").contains("Unspecified")){
			AddUnspecifiedCars(map_data);
		}
		
		if(k.getText("MFB_CARS_Specified").contains("Specified")){
			AddSpecifiedCars(map_data);
		}
		
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Cars page .");
		//MFB_CARS_ApplyBookRates
		customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Cars page .");
		int CarBRIndex = k.getTableIndex("Auto Fill", "xpath","html/body/div[3]/form/div/table");
		String CRBRTbl = "html/body/div[3]/form/div/table["+CarBRIndex+"]";
		//List<WebElement> we = driver.findElements(By.xpath("CRBRTbl"+"/tbody/tr"));
		
		String CarsPremium = k.getAttributeByXpath("//*[@id='mfb_cars_sec_tot']", "value");
		System.out.println(CarsPremium);
		common.compareValues(CarsNetP, Double.parseDouble(CarsPremium), "Cars Net Premium Value");
		map_data.put("PS_Cars_NetNetPremium", CarsPremium);
		
		
		
	}catch(Throwable t){
	   	String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
        Assert.fail("Unable to enter details in ClaimsExperience Page", t);
        return false;	
	}
	return true;
}


public boolean AddUnspecifiedCars(Map<Object, Object> map_data){
	tempNP = 0.00;
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
		String[] Un = ((String)map_data.get("MFB_CARS_Unspecified_Items")).split(";");
		int no = 0;
		int ItemCount = Un.length;
		int uIndex;
//		double UnspecifiedTotal=0.0;
//		double rtf 	;
//		double expBP;
//		double actBP;
//		double ExpPremium, ActPremium;
		
		while(no < ItemCount){
			customAssert.assertTrue(k.Click("MFB_CARS_AddCarUnspecified"));
			customAssert.assertTrue(common.funcPageNavigation("Car Unspecified",""), "Car Unspecified Page Navigation issue . ");
			TestUtil.reportStatus("Entering  Details for Unspecified Cars item- "+(no+1) , "info", false);
			customAssert.assertTrue(k.DropDownSelectionByVal("MFB_CARUNSPECIFIED_VehicleType", (String)internal_data_map.get("AddUnspecifiedCar").get(no).get("MFB_CARUNSPECIFIED_VehicleType")),"Unable to select Vehicle Type value");
			
			customAssert.assertTrue(k.Input("MFB_CARUNSPECIFIED_Number", (String)internal_data_map.get("AddUnspecifiedCar").get(no).get("MFB_CARUNSPECIFIED_Number")),	"Unable to enter value in Vehicle Number  field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARUNSPECIFIED_Number", "value"),"MFB_CARUNSPECIFIED_Number Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.DropDownSelectionByVal("MFB_CARUNSPECIFIED_CoverBasis", (String)internal_data_map.get("AddUnspecifiedCar").get(no).get("MFB_CARUNSPECIFIED_CoverBasis")),"Unable to select MFB_CARUNSPECIFIED_CoverBasis value");
			// UL Code  			MFB_CARUNSPECIFIED_ClassUse
			String[] Unspecified = ((String)internal_data_map.get("AddUnspecifiedCar").get(no).get("MFB_CARUNSPECIFIED_ClassUse")).split(",");
			List<WebElement> l_elements = driver.findElements(By.xpath("//ul"));
			for(String geo_limit : Unspecified){
				for(WebElement each_ul : l_elements){
					customAssert.assertTrue(k.Click("MFB_CARUNSPECIFIED_ClassUse"),"Error while Clicking MFB_CARUNSPECIFIED_ClassUse List object . ");
					k.waitTwoSeconds();
					if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
						each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
					else
						continue;
					break;
				}
			}
			
		//customAssert.assertTrue(k.DropDownSelectionByVal("MFB_CARUNSPECIFIED_ClassUse", (String)map_data.get("MFB_CARUNSPECIFIED_ClassUse")),"Unable to select MFB_CARUNSPECIFIED_ClassUse value");
			
			customAssert.assertTrue(k.DropDownType("MFB_CARUNSPECIFIED_PermittedDriver", (String)internal_data_map.get("AddUnspecifiedCar").get(no).get("MFB_CARUNSPECIFIED_PermittedDriver")),"Unable to select MFB_CARUNSPECIFIED_PermittedDriver	 value");
			
			customAssert.assertTrue(k.DropDownSelection("MFB_CARUNSPECIFIED_HighRisk", (String)internal_data_map.get("AddUnspecifiedCar").get(no).get("MFB_CARUNSPECIFIED_HighRisk")),	"Unable to enter value in MFB_CARUNSPECIFIED_HighRisk field .");
			
			customAssert.assertTrue(k.Input("MFB_CARUNSPECIFIED_AccidentalDamage", (String)internal_data_map.get("AddUnspecifiedCar").get(no).get("MFB_CARUNSPECIFIED_AccidentalDamage")),	"Unable to enter value in MFB_CARUNSPECIFIED_AccidentalDamage  field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARUNSPECIFIED_AccidentalDamage", "value"),"MFB_CARUNSPECIFIED_AccidentalDamage Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_CARUNSPECIFIED_FireEccess", (String)internal_data_map.get("AddUnspecifiedCar").get(no).get("MFB_CARUNSPECIFIED_FireEccess")),	"Unable to enter value in MFB_CARUNSPECIFIED_FireEccess  field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARUNSPECIFIED_FireEccess", "value"),"MFB_CARUNSPECIFIED_FireEccess Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_CARUNSPECIFIED_TheftEccess", (String)internal_data_map.get("AddUnspecifiedCar").get(no).get("MFB_CARUNSPECIFIED_TheftEccess")),	"Unable to enter value in MFB_CARUNSPECIFIED_TheftEccess field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARUNSPECIFIED_TheftEccess", "value"),"MFB_CARUNSPECIFIED_TheftEccess Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_CARUNSPECIFIED_WindscreenExcess", (String)internal_data_map.get("AddUnspecifiedCar").get(no).get("MFB_CARUNSPECIFIED_WindscreenExcess")),	"Unable to enter value in MFB_CARUNSPECIFIED_WindscreenExcess field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARUNSPECIFIED_WindscreenExcess", "value"),"MFB_CARUNSPECIFIED_WindscreenExcess Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_CARUNSPECIFIED_HighRiskVehicles", (String)internal_data_map.get("AddUnspecifiedCar").get(no).get("MFB_CARUNSPECIFIED_HighRiskVehicles")),	"Unable to enter value in MFB_CARUNSPECIFIED_HighRiskVehicles field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARUNSPECIFIED_HighRiskVehicles", "value"),"MFB_CARUNSPECIFIED_HighRiskVehicles Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Unspecified Cars page .");  
			//CCF_Btn_Back
			customAssert.assertTrue(k.Click("CCF_Btn_Back"), "Unable to click on Back Button on Unspecified Cars page .");
			customAssert.assertTrue(common.funcPageNavigation("Cars",""), "Cars Page Navigation issue . ");
			
			uIndex = k.getTableIndex("Vehicle Type", "xpath","html/body/div[3]/form/div/table");
					
			String Unspcfdtblxp = "html/body/div[3]/form/div/table["+uIndex+"]";
			String UnspcfdtblXpath=Unspcfdtblxp+"/tbody/tr["+(no+1)+"]/td[2]";
			
			customAssert.assertTrue(k.isDisplayedByXpath(Unspcfdtblxp));
			
			if(k.getTextByXpath(UnspcfdtblXpath).equals((String)internal_data_map.get("AddUnspecifiedCar").get(no).get("MFB_CARUNSPECIFIED_Number"))){
				TestUtil.reportStatus("UnSpecified car values have been added properly for item - "+ (no+1) , "info", false);
			}else{
				TestUtil.reportStatus("UnSpecified car values have not been added properly" , "Fail", true);
			}
			
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Cars page .");
			
			//MFB_CARS_ApplyBookRates
			customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Cars page .");
			
			int CarBRIndex = k.getTableIndex("Auto Fill", "xpath","html/body/div[3]/form/div/table");
			
			customAssert.assertTrue(funcValidate_CoverRatedTable(CarBRIndex, no, "AddUnspecifiedCar","MFB_CARUNSPECIFIED" ),"Rater Table validation failed for Unspecified item - "+(no +1));
			CarsNetP = CarsNetP + tempNP;
			
		no++;
		}
	
}catch(Throwable t){
	return false;
}
	
	return true;
	
}
public boolean AddSpecifiedCars(Map<Object, Object> map_data){
	tempNP = 0.00;
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
		String[] Sp = ((String)map_data.get("MFB_CARS_Unspecified_Items")).split(";");
		int no = 0;
		int ItemSize = Sp.length;
		int SIndex;
		while(no < ItemSize){
			TestUtil.reportStatus("Entering  Details for Specified Cars item - "+ (no+1) , "info", false);
			customAssert.assertTrue(k.Click("MFB_CARS_AddCarSpecified"),"Unable to click on Add Specified Cars Button on Cars page .");
			customAssert.assertTrue(common.funcPageNavigation("Car Detail",""), "Car Detail Page Navigation issue . ");
			
			customAssert.assertTrue(k.Input("MFB_CARSPECIFIED_RegNumver", (String)internal_data_map.get("AddSpecifiedCar").get(no).get("MFB_CARSPECIFIED_RegNumver")),	"Unable to enter value in MFB_CARUNSPECIFIED_WindscreenExcess field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARSPECIFIED_RegNumver", "value"),"MFB_CARSPECIFIED_RegNumver Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_CARSPECIFIED_Model", (String)internal_data_map.get("AddSpecifiedCar").get(no).get("MFB_CARSPECIFIED_Model")),	"Unable to enter value in MFB_CARUNSPECIFIED_HighRiskVehicles field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARSPECIFIED_Model", "value"),"MFB_CARSPECIFIED_Model Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.DropDownSelectionByVal("MFB_CARSPECIFIED_CoverBasis", (String)internal_data_map.get("AddSpecifiedCar").get(no).get("MFB_CARSPECIFIED_CoverBasis")),"Unable to select MFB_CARSPECIFIED_CoverBasis value");
			
			// UL Code  			MFB_CARSPECIFIED_ClassUse

						String[] Specified = ((String)internal_data_map.get("AddSpecifiedCar").get(no).get("MFB_CARSPECIFIED_ClassUse")).split(",");
						List<WebElement> l_elements = driver.findElements(By.xpath("//ul"));
						for(String geo_limit : Specified){
							for(WebElement each_ul : l_elements){
								customAssert.assertTrue(k.Click("MFB_CARSPECIFIED_ClassUse"),"Error while Clicking MFB_CARSPECIFIED_ClassUse List object . ");
								k.waitTwoSeconds();
								if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
									each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
								else
									continue;
								break;
							}
						}
			
			customAssert.assertTrue(k.Input("MFB_CARSPECIFIED_NoOfSeats", (String)internal_data_map.get("AddSpecifiedCar").get(no).get("MFB_CARSPECIFIED_NoOfSeats")),	"Unable to enter value in MFB_CARSPECIFIED_NoOfSeats field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARSPECIFIED_NoOfSeats", "value"),"MFB_CARSPECIFIED_NoOfSeats Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_CARSPECIFIED_AccidentalDamage", (String)internal_data_map.get("AddSpecifiedCar").get(no).get("MFB_CARSPECIFIED_AccidentalDamage")),	"Unable to enter value in MFB_CARUNSPECIFIED_WindscreenExcess field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARSPECIFIED_AccidentalDamage", "value"),"MFB_CARSPECIFIED_AccidentalDamage Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_CARSPECIFIED_FireExcess", (String)internal_data_map.get("AddSpecifiedCar").get(no).get("MFB_CARSPECIFIED_FireExcess")),"Unable to enter value in MFB_CARSPECIFIED_FireExcess field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARSPECIFIED_FireExcess", "value"),"MFB_CARSPECIFIED_FireExcess Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_CARSPECIFIED_TheftExcess", (String)internal_data_map.get("AddSpecifiedCar").get(no).get("MFB_CARSPECIFIED_TheftExcess")),	"Unable to enter value in MFB_CARSPECIFIED_TheftExcess field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARSPECIFIED_TheftExcess", "value"),"MFB_CARSPECIFIED_TheftExcess Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_CARSPECIFIED_WindscreenExcess", (String)internal_data_map.get("AddSpecifiedCar").get(no).get("MFB_CARSPECIFIED_WindscreenExcess")),	"Unable to enter value in MFB_CARSPECIFIED_WindscreenExcess field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARSPECIFIED_WindscreenExcess", "value"),"MFB_CARSPECIFIED_WindscreenExcess Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_CARSPECIFIED_DriversExcess", (String)internal_data_map.get("AddSpecifiedCar").get(no).get("MFB_CARSPECIFIED_DriversExcess")),"Unable to enter value in MFB_CARSPECIFIED_DriversExcess field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_CARSPECIFIED_DriversExcess", "value"),"MFB_CARSPECIFIED_DriversExcess Field Should Contain Valid Name  .");
			
			
			
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Unspecified Cars page .");  
			//CCF_Btn_Back
			customAssert.assertTrue(k.Click("CCF_Btn_Back"), "Unable to click on Back Button on Unspecified Cars page .");
			customAssert.assertTrue(common.funcPageNavigation("Cars",""), "Cars Page Navigation issue . ");
			
			 SIndex = k.getTableIndex("Make/ Model", "xpath","html/body/div[3]/form/div/table");
			String spcfdtblxp = "html/body/div[3]/form/div/table["+SIndex+"]";
			String spcfdtblXpath=spcfdtblxp+"/tbody/tr["+(no+1)+"]/td[1]";
			
			customAssert.assertTrue(k.isDisplayedByXpath(spcfdtblxp));
			if(k.getTextByXpath(spcfdtblXpath).equals((String)internal_data_map.get("AddSpecifiedCar").get(no).get("MFB_CARSPECIFIED_Model"))){
				TestUtil.reportStatus("Specified car values have been added properly for Item - "+(no+1) , "info", false);
			}else{
				TestUtil.reportStatus("Specified car values have not been added properly" , "Fail", true);
			}
			
			customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Cars page .");
			
			int CarBRIndex = k.getTableIndex("Auto Fill", "xpath","html/body/div[3]/form/div/table");
			
			customAssert.assertTrue(funcValidate_CoverRatedTable(CarBRIndex, no, "AddSpecifiedCar","MFB_CARSPECIFIED" ),"Rater Table validation failed for Specified item - "+(no +1));
			CarsNetP = CarsNetP + tempNP;
			
			no++;
		}
	
}catch(Throwable t){
	return false;
}
	
	return true;
	
}


public boolean funcValidate_CoverRatedTable(int TblIndx, int rowNmbr,String innerSheet, String abvr){
	tempNP = 0.00;
	int uIndex;
	double UnspecifiedTotal=0.0;
	double rtf 	;
	double expBP;
	double actBP;
	double ExpPremium, ActPremium;
	int no=0;
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
		
		
		String CRBRTbl = "html/body/div[3]/form/div/table["+TblIndx+"]";
		int row = driver.findElements(By.xpath(CRBRTbl+"/tbody/tr")).size();
		if(row == 0){
			TestUtil.reportStatus("No Row has been added in cover rating  table", "Fail", false);
		}else
			row--;
		
		String Tbl = CRBRTbl +"/tbody/tr["+(row)+"]/td";
		String tblH= CRBRTbl +"/thead/tr/th"; //html/body/div[3]/form/div/table[4]/thead/tr/th
		
		//List<WebElement> we = driver.findElements(By.xpath("CRBRTbl"+"/tbody/tr"));
		String Desc = k.getTextByXpath(Tbl+"["+getTableHeaderIndex(tblH,"Description")+"]" );
		String initial_rateXP = (Tbl+"["+getTableHeaderIndex(tblH,"Initial Rate")+"]/input" );
		String Tech_AdjustXP = (Tbl+"["+getTableHeaderIndex(tblH,"Tech. Adjust")+"]/input");
		String Comm_AdjustXP = (Tbl+"["+getTableHeaderIndex(tblH,"Comm. Adjust ")+"]/input");
		String PremiumXP =(Tbl+"["+getTableHeaderIndex(tblH,"Premium")+"]/input");
		
		String InitialRate = (String)internal_data_map.get(innerSheet).get(no).get(abvr+"_InitialRate");
		String TechAdjust = (String)internal_data_map.get(innerSheet).get(no).get(abvr+"_TechAdjust");
		String CommAdjust =(String)internal_data_map.get(innerSheet).get(no).get(abvr+"_CommAdjust");
		
		
		
		//MFB_CARUNSPECIFIED_InitialRate
		customAssert.assertTrue(k.InputByXpath(initial_rateXP,InitialRate),"Issue while enterind data in Initial rate");
		customAssert.assertTrue(k.InputByXpath(Tech_AdjustXP,TechAdjust),"Issue while entering data in Tech Adjust% rate");
		customAssert.assertTrue(k.InputByXpath(Comm_AdjustXP,CommAdjust),"Issue while entering data in Comm Adjust% rate");
		
		//Click on appy book rate button
		customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Cars page for ");
		 if(abvr.equals("MFB_OTBS"))
			 rtf=1;
		 else
			 rtf = Double.parseDouble(k.getTextByXpath(Tbl+"["+getTableHeaderIndex(tblH,"Rateable")+"]"));
		 
		 expBP = rtf* Double.parseDouble((String)internal_data_map.get(innerSheet).get(no).get(abvr+"_InitialRate")); 
		 actBP = Double.parseDouble(k.getAttributeByXpath(Tbl+"["+getTableHeaderIndex(tblH,"Book Premium")+"]/input","value"));
		 common.compareValues(expBP, actBP, "Book Premium Value - "+Desc+" - Item :"+(rowNmbr+1));
		
		 ExpPremium = expBP + (expBP*((Double.parseDouble(TechAdjust))/100))+((expBP + (expBP*((Double.parseDouble(TechAdjust))/100)))*(Double.parseDouble(CommAdjust)/100));
		 ActPremium = Double.parseDouble(k.getAttributeByXpath(PremiumXP, "value"));
		 common.compareValues(ExpPremium, ActPremium, "Premiums values - "+Desc+" - Item :"+(rowNmbr+1));
		 tempNP = ExpPremium;
		
	}catch(Throwable t){
		return false;
		
	}
	return true;
}

public boolean TradePlatepage(Map<Object, Object> map_data) {
	try{
		TpNetP=0.00;
		
		customAssert.assertTrue(common.funcPageNavigation("Trade Plate",""), "Trade Plate Page Navigation issue . ");
		if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }
		
		customAssert.assertTrue(k.Input("MFB_TP_Number", (String)map_data.get("MFB_TP_Number")),	"Unable to enter value in MFB_TP_Number  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_TP_Number", "value"),"Accidental Damage Field Should Contain Valid Name  .");
		
		
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
		
			String[] Tp = ((String)map_data.get("MFB_TP_Items")).split(";");
			int no = 0;
			int ItemSize = Tp.length;
			int SIndex;
			while(no < ItemSize){
				tempNP = 0.00;
				TestUtil.reportStatus("Entering  Details for Trade Plates item - "+ (no+1) , "info", false);
				customAssert.assertTrue(k.Click("MFB_TP_AddTPBtn"), "Unable to click on Add Trade Plate number Button on Trade Plate page for ");
				customAssert.assertTrue(common.funcPageNavigation("Trade Plates",""), "Trade Plates Page Navigation issue . ");
				//Entering Details
				customAssert.assertTrue(k.Input("MFB_TP_PlateNumber", (String)internal_data_map.get("AddTradePlate").get(no).get("MFB_TP_PlateNumber")),	"Unable to enter value in MFB_TP_Number  field .");
				customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_TP_PlateNumber", "value"),"AMFB_TP_PlateNumber Field Should Contain Valid Name  .");
				
				customAssert.assertTrue(k.DropDownSelectionByVal("MFB_TP_CoverBasis", (String)internal_data_map.get("AddTradePlate").get(no).get("MFB_TP_CoverBasis")),"Unable to select MFB_TP_CoverBasis value");
				customAssert.assertTrue(k.DropDownSelectionByVal("MFB_TP_TradePlate", (String)internal_data_map.get("AddTradePlate").get(no).get("MFB_TP_TradePlate")),"Unable to select MFB_TP_TradePlate value");
				
				customAssert.assertTrue(k.Input("MFB_TP_FireEcess", (String)internal_data_map.get("AddTradePlate").get(no).get("MFB_TP_FireExcess")),	"Unable to enter value in MFB_TP_FireExcess field .");
				customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_TP_FireEcess", "value"),"MFB_TP_FireExcess Field Should Contain Valid Name  .");
				
				customAssert.assertTrue(k.Input("MFB_TP_Windscreen", (String)internal_data_map.get("AddTradePlate").get(no).get("MFB_TP_WindscreenExcess")),	"Unable to enter value in MFB_TP_WindscreenExcess field .");
				customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_TP_Windscreen", "value"),"MFB_TP_WindscreenExcess Field Should Contain Valid Name  .");				
				
				customAssert.assertTrue(k.Input("MFB_TP_TheftParty", (String)internal_data_map.get("AddTradePlate").get(no).get("MFB_TP_TheftExcess")),	"Unable to enter value in MFB_TP_TheftExcess field .");
				customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_TP_TheftParty", "value"),"MFB_TP_TheftExcess Field Should Contain Valid Name  .");
				
				customAssert.assertTrue(k.Input("MFB_TP_AccidentalDamage", (String)internal_data_map.get("AddTradePlate").get(no).get("MFB_TP_AccidentalDamage")),	"Unable to enter value in MFB_TP_AccidentalDamage field .");
				customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_TP_AccidentalDamage", "value"),"MFB_TP_AccidentalDamage Field Should Contain Valid Name  .");
				
				// UL Code  			MFB_TP_UseBasis

				String[] Specified = ((String)internal_data_map.get("AddTradePlate").get(no).get("MFB_TP_UseBasis")).split(",");
				List<WebElement> l_elements = driver.findElements(By.xpath("//ul"));
				for(String geo_limit : Specified){
					for(WebElement each_ul : l_elements){
						customAssert.assertTrue(k.Click("MFB_CARUNSPECIFIED_ClassUse"),"Error while Clicking MFB_TP_UseBasis List object . ");
						k.waitTwoSeconds();
						if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
							each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
						else
							continue;
						break;
					}
				}
				
				customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Trade Plates page .");  
				//CCF_Btn_Back
				customAssert.assertTrue(k.Click("CCF_Btn_Back"), "Unable to click on Back Button on  Trade Plates page .");
				customAssert.assertTrue(common.funcPageNavigation("Trade Plate",""), "Trade Plate Page Navigation issue . ");
				
				 SIndex = k.getTableIndex("Plate number", "xpath","html/body/div[3]/form/div/table");
				String spcfdtblxp = "html/body/div[3]/form/div/table["+SIndex+"]";
				String spcfdtblXpath=spcfdtblxp+"/tbody/tr["+(no+1)+"]/td[1]";
				
				customAssert.assertTrue(k.isDisplayedByXpath(spcfdtblxp));
				if(k.getTextByXpath(spcfdtblXpath).equals((String)internal_data_map.get("AddTradePlate").get(no).get("MFB_TP_PlateNumber"))){
					TestUtil.reportStatus("Trade Plates values have been added properly for Item - "+(no+1) , "info", false);
				}else{
					TestUtil.reportStatus("Trade Plates values have not been added properly" , "Fail", true);
				}
				
				customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Trade Plate page .");
				
				int TPBRIndex = k.getTableIndex("Book Premium", "xpath","html/body/div[3]/form/div/table");
				
				customAssert.assertTrue(funcValidate_CoverRatedTable(TPBRIndex, no, "AddTradePlate","MFB_TP" ),"Rater Table validation failed for Specified item - "+(no +1));
				TpNetP=TpNetP+tempNP;
				
			
				
			no++;
			}
		
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Trade Plate page .");
			//MFB_CARS_ApplyBookRates
			customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Trade Plate page .");
			
			int TPBRIndex = k.getTableIndex("Book Premium", "xpath","html/body/div[3]/form/div/table");
			String CRBRTbl = "html/body/div[3]/form/div/table["+TPBRIndex+"]";
			//List<WebElement> we = driver.findElements(By.xpath("CRBRTbl"+"/tbody/tr"));
			
			String TPPremium = k.getAttributeByXpath("//*[@id='mfb_tp_tot']", "value");
			System.out.println("Trade Plate Premium :"+TPPremium);
			map_data.put("PS_TradePlate_NetNetPremium", TPPremium);
			common.compareValues(TpNetP, Double.parseDouble(TPPremium), "Trade Plate Net Premium Value");
		
		
	}catch(Throwable t){
		return false;
	}
	return true;
}




public int getTableHeaderIndex(String tblXPath , String val){
	
	int hCount = driver.findElements(By.xpath(tblXPath)).size();
	String s;
	if(val.equals("Premium"))
		return hCount;
	for(int i =1;i<=hCount;i++){
		s = k.getTextByXpath(tblXPath+"["+i+"]");
		
		if(s.contains(val))
			return i;
			
	}
	TestUtil.reportStatus(val+" Column Header not found in given table", "fail", false);
	return -1;
}

public boolean Trailerpage(Map<Object, Object> map_data) {
	try{
		TrNetP=0.00;
		customAssert.assertTrue(common.funcPageNavigation("Trailers",""), "Trailers Page Navigation issue . ");
		if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }
			
		String[] SpecifiedUnspecified = ((String)map_data.get("MFB_TL_Specified")).split(",");
		List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
		for(String geo_limit : SpecifiedUnspecified){
			for(WebElement each_ul : ul_elements){
				customAssert.assertTrue(k.Click("MFB_TL_Specified"),"Error while Clicking Specified/ Unspecified Trailers List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed()){
					each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
					//driver.findElement(By.xpath("//*[@id='mainpanel']")).click();
					}
					
				else
					continue;
				break;
				
			}
		}
		
		customAssert.assertTrue(k.Input("MFB_TL_Number", (String)map_data.get("MFB_TL_Number")),	"Unable to enter value in MFB_TL_Number field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_TL_Number", "value"),"MFB_TL_Number Field Should Contain Valid Name  .");
	
		customAssert.assertTrue(k.DropDownType("MFB_TL_UndisclosedTrailers", (String)map_data.get("MFB_TL_UndisclosedTrailers")),"Unable to select MFB_TL_UndisclosedTrailers value");
		if((k.getText("MFB_TL_UndisclosedTrailers").equals("Yes"))){
			customAssert.assertTrue(k.Input("MFB_TL_TrailersDetail", (String)map_data.get("MFB_TL_TrailersDetail")),"Unable to enter trailer details on Trailers Page");
		}
		customAssert.assertTrue(k.DropDownType("MFB_TL_ThirdParty", (String)map_data.get("MFB_TL_ThirdParty")),"Unable to select MFB_TL_ThirdParty value");
		customAssert.assertTrue(k.DropDownType("MFB_TL_PreventTheft", (String)map_data.get("MFB_TL_PreventTheft")),"Unable to select MFB_TL_PreventTheft value");
		if((k.getText("MFB_TL_PreventTheft").equals("Yes"))){
			customAssert.assertTrue(k.Input("MFB_TL_TheftDetails", (String)map_data.get("MFB_TL_TheftDetails")),"Unable to enter theft details on Trailers Page");
		}
		
		
		
		if(k.getText("MFB_TL_Specified").contains("Unspecified")){
			AddUnspecifiedTrailers(map_data);
		}
		
		if(k.getText("MFB_TL_Specified").contains("Specified")){
			AddSpecifiedTrailers(map_data);
		}
		
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Trailers page .");
		//MFB_TL_ApplyBookRates
		customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Trailers page .");
		int CarBRIndex = k.getTableIndex("Book Premium", "xpath","html/body/div[3]/form/div/table");
		String CRBRTbl = "html/body/div[3]/form/div/table["+CarBRIndex+"]";
		//List<WebElement> we = driver.findElements(By.xpath("CRBRTbl"+"/tbody/tr"));
		
		String TrailersPremium = k.getAttributeByXpath("//*[@id='mfb_tr_tot']", "value");
		System.out.println(TrailersPremium);
		map_data.put("PS_Trailers_NetNetPremium", TrailersPremium);
		common.compareValues(TrNetP, Double.parseDouble(TrailersPremium), "Trailer Net Premium Value");
		
		
	}catch(Throwable t){
	   	String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
        Assert.fail("Unable to enter details in ClaimsExperience Page", t);
        return false;	
	}
	return true;
}



public boolean AddSpecifiedTrailers(Map<Object, Object> map_data){
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
		String[] Sp = ((String)map_data.get("MFB_TL_Specified_Items")).split(";");
		int no = 0;
		int ItemSize = Sp.length;
		int SIndex;
		while(no < ItemSize){
			TestUtil.reportStatus("Entering  Details for Specified Trailers item - "+ (no+1) , "info", false);
			customAssert.assertTrue(k.Click("MFB_TL_AddSpecified"),"Unable to click on Add Specified Trailers Button on Trailers page .");
			customAssert.assertTrue(common.funcPageNavigation("Trailer - Specified",""), "Trailer - Specified Detail Page Navigation issue . ");
			
			
			customAssert.assertTrue(k.DropDownSelectionByVal("MFB_TLSP_TrailerType", (String)internal_data_map.get("AddSpecifiedTrailers").get(no).get("MFB_TLSP_TrailerType")),"Unable to select MFB_TLSP_TrailerType value");
			customAssert.assertTrue(k.DropDownSelectionByVal("MFB_TLSP_CoverBasis", (String)internal_data_map.get("AddSpecifiedTrailers").get(no).get("MFB_TLSP_CoverBasis")),"Unable to select MFB_TLSP_CoverBasis value");
			
		
			customAssert.assertTrue(k.Input("MFB_TLSP_AccidentalDamage", (String)internal_data_map.get("AddSpecifiedTrailers").get(no).get("MFB_TLSP_AccidentalDamage")),	"Unable to enter value in MFB_TLSP_AccidentalDamage field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_TLSP_AccidentalDamage", "value"),"MFB_TLSP_AccidentalDamage Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_TLSP_FireExcess", (String)internal_data_map.get("AddSpecifiedTrailers").get(no).get("MFB_TLSP_FireExcess")),"Unable to enter value in MFB_TLSP_FireExcess field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_TLSP_FireExcess", "value"),"MFB_TLSP_FireExcess Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_TLSP_TheftParty", (String)internal_data_map.get("AddSpecifiedTrailers").get(no).get("MFB_TLSP_TheftExcess")),	"Unable to enter value in MFB_TLSP_TheftExcess field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_TLSP_TheftParty", "value"),"MFB_TLSP_TheftExcess Field Should Contain Valid Name  .");
			
						
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Unspecified Trailers page .");  
			//CCF_Btn_Back
			customAssert.assertTrue(k.Click("CCF_Btn_Back"), "Unable to click on Back Button on Unspecified Trailers page .");
			customAssert.assertTrue(common.funcPageNavigation("Trailers",""), "Trailers Page Navigation issue . ");
			
			 SIndex = k.getTableIndex("Make", "xpath","html/body/div[3]/form/div/table");
			String spcfdtblxp = "html/body/div[3]/form/div/table["+SIndex+"]";
			String spcfdtblXpath=spcfdtblxp+"/tbody/tr["+(no+1)+"]/td[1]";
			
			customAssert.assertTrue(k.isDisplayedByXpath(spcfdtblxp));
			if(k.getTextByXpath(spcfdtblXpath).equals((String)internal_data_map.get("AddSpecifiedTrailers").get(no).get("MFB_TLSP_TrailerType"))){
				TestUtil.reportStatus("Specified Trailer values have been added properly for Item - "+(no+1) , "info", false);
			}else{
				TestUtil.reportStatus("Specified Trailer values have not been added properly" , "Fail", true);
			}
			
			customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Trailers page .");
			
			int CarBRIndex = k.getTableIndex("Book Premium", "xpath","html/body/div[3]/form/div/table");
			
			customAssert.assertTrue(funcValidate_CoverRatedTable(CarBRIndex, no, "AddSpecifiedTrailers","MFB_TLSP" ),"Rater Table validation failed for Specified item - "+(no +1));
			TrNetP=TrNetP+tempNP;
			
			no++;
		}
	
}catch(Throwable t){
	return false;
}
	
	return true;
	
}

public boolean AddUnspecifiedTrailers(Map<Object, Object> map_data){
	tempNP = 0.00;
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
		String[] Sp = ((String)map_data.get("MFB_TL_Unspecified_Items")).split(";");
		int no = 0;
		int ItemSize = Sp.length;
		int SIndex;
		while(no < ItemSize){
			TestUtil.reportStatus("Entering  Details for Unspecified Trailers item - "+ (no+1) , "info", false);
			customAssert.assertTrue(k.Click("MFB_TL_AddUnspecified"),"Unable to click on Add Unspecified Trailers Button on Trailers page .");
			customAssert.assertTrue(common.funcPageNavigation("Add Unspecified Trailer",""), "Trailer - Unspecified Detail Page Navigation issue . ");
			
			
			customAssert.assertTrue(k.DropDownSelectionByVal("MFB_TLUN_BodyType", (String)internal_data_map.get("AddUnspecifiedTrailers").get(no).get("MFB_TLUN_BodyType")),"Unable to select MFB_TLUN_BodyType value");
			customAssert.assertTrue(k.DropDownSelectionByVal("MFB_TLUN_CoverBasis", (String)internal_data_map.get("AddUnspecifiedTrailers").get(no).get("MFB_TLUN_CoverBasis")),"Unable to select MFB_TLUN_CoverBasis value");
			
		
			customAssert.assertTrue(k.Input("MFB_TLUN_Number", (String)internal_data_map.get("AddUnspecifiedTrailers").get(no).get("MFB_TLUN_Number")),	"Unable to enter value in MFB_TLUN_Number field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_TLUN_Number", "value"),"MFB_TLUN_Number Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Unspecified Trailers page .");  
			//CCF_Btn_Back
			customAssert.assertTrue(k.Click("CCF_Btn_Back"), "Unable to click on Back Button on Unspecified Trailers page .");
			customAssert.assertTrue(common.funcPageNavigation("Trailers",""), "Trailers Page Navigation issue . ");
			
			 SIndex = k.getTableIndex("Body Type", "xpath","html/body/div[3]/form/div/table");
			String spcfdtblxp = "html/body/div[3]/form/div/table["+SIndex+"]";
			String spcfdtblXpath=spcfdtblxp+"/tbody/tr["+(no+1)+"]/td[1]";
			
			customAssert.assertTrue(k.isDisplayedByXpath(spcfdtblxp));
			if(k.getTextByXpath(spcfdtblXpath).equals((String)internal_data_map.get("AddUnspecifiedTrailers").get(no).get("MFB_TLUN_BodyType"))){
				TestUtil.reportStatus("Unspecified Trailer values have been added properly for Item - "+(no+1) , "info", false);
			}else{
				TestUtil.reportStatus("Unspecified Trailer values have not been added properly" , "Fail", true);
			}
			
			customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Trailers page .");
			
			int CarBRIndex = k.getTableIndex("Book Premium", "xpath","html/body/div[3]/form/div/table");
			
			customAssert.assertTrue(funcValidate_CoverRatedTable(CarBRIndex, no, "AddUnspecifiedTrailers","MFB_TLUN" ),"Rater Table validation failed for Unspecified item - "+(no +1));
			TrNetP=TrNetP+tempNP;
			
			no++;
		}
	
}catch(Throwable t){
	return false;
}
	
	return true;
	
}

public boolean SpecialTypepages(Map<Object, Object> map_data) {
	try{
		StNetP=0.00;
		customAssert.assertTrue(common.funcPageNavigation("Special Types",""), "ST Page Navigation issue . ");
		if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }
		
		String[] SpecifiedUnspecified = ((String)map_data.get("MFB_ST_Specified")).split(",");
		List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
		for(String geo_limit : SpecifiedUnspecified){
			for(WebElement each_ul : ul_elements){
				customAssert.assertTrue(k.Click("MFB_ST_Specified"),"Error while Clicking Specified/ Unspecified ST List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed()){
					each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
					//driver.findElement(By.xpath("//*[@id='mainpanel']")).click();
					}
					
				else
					continue;
				break;
				
			}
		}
				
		customAssert.assertTrue(k.DropDownType("MFB_ST_PermittedDrivers", (String)map_data.get("MFB_ST_Drivers")),"Unable to select Drivers value");
		customAssert.assertTrue(k.Input("MFB_ST_AccidentalDamage", (String)map_data.get("MFB_ST_AccidentalDamage")),	"Unable to enter value in Accidental Name Name  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_ST_AccidentalDamage", "value"),"Accidental Damage Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("MFB_ST_FireExcess", (String)map_data.get("MFB_ST_FireExcess")),	"Unable to enter value in FireAccess  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_ST_FireExcess", "value"),"Fire Access Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("MFB_ST_TheftParty", (String)map_data.get("MFB_ST_TheftParty")),	"Unable to enter value in MFB_ST_TheftParty Name Name  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_ST_TheftParty", "value"),"MFB_ST_TheftParty Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("MFB_ST_Windscreen", (String)map_data.get("MFB_ST_Windscreen")),	"Unable to enter value in MFB_ST_Windscreen Name  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_ST_Windscreen", "value"),"MFB_ST_Windscreen Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.DropDownType("MFB_ST_DriversExcess", (String)map_data.get("MFB_ST_DriversExcess")),"Unable to select HighRisk Driver value");
		
		if(k.getText("MFB_ST_Specified").contains("Unspecified")){
			AddUnspecifiedST(map_data);
		}
		
		if(k.getText("MFB_ST_Specified").contains("Specified")){
			AddSpecifiedST(map_data);
		}
		
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on ST page .");
		//MFB_ST_ApplyBookRates
		customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on ST page .");
		int CarBRIndex = k.getTableIndex("Book Premium", "xpath","html/body/div[3]/form/div/table");
		String CRBRTbl = "html/body/div[3]/form/div/table["+CarBRIndex+"]";
		//List<WebElement> we = driver.findElements(By.xpath("CRBRTbl"+"/tbody/tr"));
		
		String STPremium = k.getAttributeByXpath("//*[@id='mfb_st_tot']", "value");
		System.out.println(STPremium);
		map_data.put("PS_SpecialTypes_NetNetPremium", STPremium);
		common.compareValues(StNetP, Double.parseDouble(STPremium), "Special Type Net Premium Value");
		
		
	}catch(Throwable t){
	   	String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
        Assert.fail("Unable to enter details in Special Type Page", t);
        return false;	
	}
	return true;
}


public boolean AddUnspecifiedST(Map<Object, Object> map_data){
	tempNP = 0.00;
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
		String[] Un = ((String)map_data.get("MFB_ST_Unspecified_Items")).split(";");
		int no = 0;
		int ItemCount = Un.length;
		int uIndex;
//		double UnspecifiedTotal=0.0;
//		double rtf 	;
//		double expBP;
//		double actBP;
//		double ExpPremium, ActPremium;
		
		while(no < ItemCount){
			customAssert.assertTrue(k.Click("MFB_ST_AddUnspecified"));
			customAssert.assertTrue(common.funcPageNavigation("Special Type - Unspecified",""), "Special Type - Unspecified Page Navigation issue . ");
			TestUtil.reportStatus("Entering  Details for Unspecified ST item- "+(no+1) , "info", false);
			customAssert.assertTrue(k.DropDownType("MFB_STUN_VehicleType", (String)internal_data_map.get("AddUnspecifiedST").get(no).get("MFB_STUN_VehicleType")),"Unable to select Vehicle Type value");
			
			customAssert.assertTrue(k.Input("MFB_STUN_Number", (String)internal_data_map.get("AddUnspecifiedST").get(no).get("MFB_STUN_Number")),	"Unable to enter value in Vehicle Number  field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_STUN_Number", "value"),"MFB_STUN_Number Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.DropDownType("MFB_STUN_CoverBasis", (String)internal_data_map.get("AddUnspecifiedST").get(no).get("MFB_STUN_CoverBasis")),"Unable to select MFB_STUN_CoverBasis value");
		
			// UL Code  			MFB_CARUNSPECIFIED_ClassUse
			String[] Unspecified = ((String)internal_data_map.get("AddUnspecifiedST").get(no).get("MFB_STUN_ClassUse")).split(",");
			List<WebElement> l_elements = driver.findElements(By.xpath("//ul"));
			for(String geo_limit : Unspecified){
				for(WebElement each_ul : l_elements){
					customAssert.assertTrue(k.Click("MFB_STUN_ClassOfUse"),"Error while Clicking MFB_STUN_ClassUse List object . ");
					k.waitTwoSeconds();
					if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
						each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
					else
						continue;
					break;
				}
			}
			
		//customAssert.assertTrue(k.DropDownSelectionByVal("MFB_CARUNSPECIFIED_ClassUse", (String)map_data.get("MFB_CARUNSPECIFIED_ClassUse")),"Unable to select MFB_CARUNSPECIFIED_ClassUse value");
			
			customAssert.assertTrue(k.DropDownType("MFB_STUN_PermittedDrivers", (String)internal_data_map.get("AddUnspecifiedST").get(no).get("MFB_STUN_PermittedDriver")),"Unable to select MFB_STUN_PermittedDriver value");
			
			customAssert.assertTrue(k.DropDownType("MFB_STUN_DriversExcess", (String)internal_data_map.get("AddUnspecifiedST").get(no).get("MFB_STUN_HighRisk")),	"Unable to enter value in MFB_STUN_HighRisk field .");
//			
//			customAssert.assertTrue(k.Input("MFB_STUN_AccidentalDamage", (String)internal_data_map.get("AddUnspecifiedST").get(no).get("MFB_STUN_AccidentalDamage")),	"Unable to enter value in MFB_STUN_AccidentalDamage  field .");
//			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_STUN_AccidentalDamage", "value"),"MFB_STUN_AccidentalDamage Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_STUN_FireExcess", (String)internal_data_map.get("AddUnspecifiedST").get(no).get("MFB_STUN_FireExcess")),	"Unable to enter value in MFB_STUN_FireExcess  field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_STUN_FireExcess", "value"),"MFB_STUN_FireExcess Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_STUN_TheftParty", (String)internal_data_map.get("AddUnspecifiedST").get(no).get("MFB_STUN_TheftParty")),	"Unable to enter value in MFB_STUN_TheftParty field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_STUN_TheftParty", "value"),"MFB_STUN_TheftParty Field Should Contain Valid Name  .");
						
			customAssert.assertTrue(k.Input("MFB_STUN_ThirdParty", (String)internal_data_map.get("AddUnspecifiedST").get(no).get("MFB_STUN_ThirdParty")),	"Unable to enter value in MFB_STUN_ThirdParty field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_STUN_ThirdParty", "value"),"MFB_STUN_ThirdParty Field Should Contain Valid Name  .");
					
			customAssert.assertTrue(k.Input("MFB_STUN_Windscreen", (String)internal_data_map.get("AddUnspecifiedST").get(no).get("MFB_STUN_Windscreen")),	"Unable to enter value in MFB_STUN_Windscreen field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_STUN_Windscreen", "value"),"MFB_STUN_Windscreen Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_STUN_HighRiskVehicles", (String)internal_data_map.get("AddUnspecifiedST").get(no).get("MFB_STUN_HighRiskVehicles")),	"Unable to enter value in MFB_STUN_HighRiskVehicles field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_STUN_HighRiskVehicles", "value"),"MFB_STUN_HighRiskVehicles Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Unspecified ST page .");  
			//CCF_Btn_Back
			customAssert.assertTrue(k.Click("CCF_Btn_Back"), "Unable to click on Back Button on Unspecified ST page .");
			customAssert.assertTrue(common.funcPageNavigation("Special Types",""), "ST Page Navigation issue . ");
			
			uIndex = k.getTableIndex("Vehicle Type", "xpath","html/body/div[3]/form/div/table");
					
			String Unspcfdtblxp = "html/body/div[3]/form/div/table["+uIndex+"]";
			String UnspcfdtblXpath=Unspcfdtblxp+"/tbody/tr["+(no+1)+"]/td[1]";
			
			customAssert.assertTrue(k.isDisplayedByXpath(Unspcfdtblxp));
			
			if(k.getTextByXpath(UnspcfdtblXpath).equals((String)internal_data_map.get("AddUnspecifiedST").get(no).get("MFB_STUN_VehicleType"))){
				TestUtil.reportStatus("UnSpecified Special Type values have been added properly for item - "+ (no+1) , "info", false);
			}else{
				TestUtil.reportStatus("UnSpecified Special Type values have not been added properly" , "Fail", true);
			}
			
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on ST page .");
			
			//MFB_ST_ApplyBookRates
			customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on ST page .");
			
			int CarBRIndex = k.getTableIndex("Book Premium", "xpath","html/body/div[3]/form/div/table");
			
			customAssert.assertTrue(funcValidate_CoverRatedTable(CarBRIndex, no, "AddUnspecifiedST","MFB_STUN" ),"Rater Table validation failed for Unspecified item - "+(no +1));
			StNetP=StNetP+tempNP;
			
		no++;
		}
	
}catch(Throwable t){
	return false;
}
	
	return true;
	
}
public boolean AddSpecifiedST(Map<Object, Object> map_data){
	tempNP=0.00;
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
		String[] Sp = ((String)map_data.get("MFB_ST_Specified_Items")).split(";");
		int no = 0;
		int ItemSize = Sp.length;
		int SIndex;
		while(no < ItemSize){
			TestUtil.reportStatus("Entering  Details for Specified ST item - "+ (no+1) , "info", false);
			customAssert.assertTrue(k.Click("MFB_ST_AddSpecified"),"Unable to click on Add Specified ST Button on ST page .");
			customAssert.assertTrue(common.funcPageNavigation("Special Type - Specified",""), "Special Type - Specified Detail Page Navigation issue . ");
			
			customAssert.assertTrue(k.Input("MFB_STSP_RegNumber", (String)internal_data_map.get("AddSpecifiedST").get(no).get("MFB_STSP_RegNumber")),	"Unable to enter value in MFB_STSP_RegNumber field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_STSP_RegNumber", "value"),"MFB_STSP_RegNumber Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_STSP_VehicleModel", (String)internal_data_map.get("AddSpecifiedST").get(no).get("MFB_STSP_VehicleModel")),	"Unable to enter value in MFB_STSP_VehicleModel field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_STSP_VehicleModel", "value"),"MFB_STSP_VehicleModel Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.DropDownType("MFB_STSP_VehicleCover", (String)internal_data_map.get("AddSpecifiedCar").get(no).get("MFB_STSP_VehicleCover")),"Unable to select MFB_STPECIFIED_CoverBasis value");
			
			
			customAssert.assertTrue(k.Input("MFB_STSP_CurrentValue", (String)internal_data_map.get("AddSpecifiedST").get(no).get("MFB_STSP_CurrentValue")),	"Unable to enter value in MFB_STSP_CurrentValue field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_STSP_CurrentValue", "value"),"MFB_STSP_CurrentValue Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_STSP_AccidentalDamage", (String)internal_data_map.get("AddSpecifiedST").get(no).get("MFB_STSP_AccidentalDamage")),	"Unable to enter value in MFB_STSP_AccidentalDamage field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_STSP_AccidentalDamage", "value"),"MFB_STSP_AccidentalDamage Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_STSP_FireExcess", (String)internal_data_map.get("AddSpecifiedST").get(no).get("MFB_STSP_FireExcess")),"Unable to enter value in MFB_STSP_FireExcess field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_STSP_FireExcess", "value"),"MFB_STSP_FireExcess Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_STSP_TheftParty", (String)internal_data_map.get("AddSpecifiedST").get(no).get("MFB_STSP_TheftParty")),	"Unable to enter value in MFB_STSP_TheftParty field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_STSP_TheftParty", "value"),"MFB_STSP_TheftParty Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_STSP_Windscreen", (String)internal_data_map.get("AddSpecifiedST").get(no).get("MFB_STSP_Windscreen")),	"Unable to enter value in MFB_STSP_Windscreen field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_STSP_Windscreen", "value"),"MFB_STSP_Windscreen Field Should Contain Valid Name  .");
			
			customAssert.assertTrue(k.Input("MFB_STSP_DriversExcess", (String)internal_data_map.get("AddSpecifiedST").get(no).get("MFB_STSP_DriversExcess")),"Unable to enter value in MFB_STSP_DriversExcess field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("MFB_STSP_DriversExcess", "value"),"MFB_STSP_DriversExcess Field Should Contain Valid Name  .");
			
			
			
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Unspecified ST page .");  
			//CCF_Btn_Back
			customAssert.assertTrue(k.Click("CCF_Btn_Back"), "Unable to click on Back Button on Unspecified ST page .");
			customAssert.assertTrue(common.funcPageNavigation("Special Types",""), "ST Page Navigation issue . ");
			
			SIndex = k.getTableIndex("Make", "xpath","html/body/div[3]/form/div/table");
			String spcfdtblxp = "html/body/div[3]/form/div/table["+SIndex+"]";
			String spcfdtblXpath=spcfdtblxp+"/tbody/tr["+(no+1)+"]/td[4]";
			
			customAssert.assertTrue(k.isDisplayedByXpath(spcfdtblxp));
			if(k.getTextByXpath(spcfdtblXpath).equals((String)internal_data_map.get("AddSpecifiedST").get(no).get("MFB_STSP_RegNumber"))){
				TestUtil.reportStatus("Specified Special Types values have been added properly for Item - "+(no+1) , "info", false);
			}else{
				TestUtil.reportStatus("Specified Special Types values have not been added properly" , "Fail", true);
			}
			
			customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on ST page .");
			
			int CarBRIndex = k.getTableIndex("Book Premium", "xpath","html/body/div[3]/form/div/table");
			
			customAssert.assertTrue(funcValidate_CoverRatedTable(CarBRIndex, no, "AddSpecifiedST","MFB_STSP" ),"Rater Table validation failed for Specified item - "+(no +1));
			StNetP=StNetP+tempNP;
			
			no++;
		}
	
}catch(Throwable t){
	return false;
}
	
	return true;
	
}


public boolean OtherTypepages(Map<Object, Object> map_data) {
	try{
		OtNetP=0.00;
		customAssert.assertTrue(common.funcPageNavigation("Other Types",""), "Other Page Navigation issue . ");
		if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }
		
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
		
		String[] Sp = ((String)map_data.get("MFB_OTBS_Items")).split(";");
		int no = 0;
		int ItemSize = Sp.length;
		int SIndex;
		while(no < ItemSize){
			tempNP=0.00;
			customAssert.assertTrue(k.Click("MFB_OT_AddBespoke"),"unable to click on AddBespoke button");
			
			customAssert.assertTrue(k.DropDownType("MFB_OT_Description", (String)internal_data_map.get("AddBespokeOT").get(no).get("MFB_OTBS_Description")));
			customAssert.assertTrue(k.Input("MFB_OT_InitialRate", (String)internal_data_map.get("AddBespokeOT").get(no).get("MFB_OTBS_InitialRate")));
			
			if(k.getText("MFB_OT_Description").equals("Other")){
				customAssert.assertTrue(k.Input("MFB_OT_ProvideDetails", (String)internal_data_map.get("AddBespokeOT").get(no).get("MFB_OTBS_ProvideDetails")));
			}
			
			customAssert.assertTrue(k.Click("MFB_OT_BS_Savebtn"), "Unable to click on Inner button.");
			
			customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Other Type page .");
			
			 SIndex = k.getTableIndex("Actions", "xpath","html/body/div[3]/form/div/table");
				String spcfdtblxp = "html/body/div[3]/form/div/table["+SIndex+"]";
				String spcfdtblXpath=spcfdtblxp+"/tbody/tr/td[1]";
				
				customAssert.assertTrue(k.isDisplayedByXpath(spcfdtblxp));
				if(((String)internal_data_map.get("AddBespokeOT").get(no).get("MFB_OTBS_Description")).equals("Other")){
					if(k.getTextByXpath(spcfdtblXpath).equals((String)internal_data_map.get("AddBespokeOT").get(no).get("MFB_OTBS_ProvideDetails"))){
						TestUtil.reportStatus("Specified Other Types values have been added properly for Item - "+(no+1) , "info", false);
					}else{
						TestUtil.reportStatus("Specified Other Types values have not been added properly" , "Fail", true);
					}
				}else{
					if(k.getTextByXpath(spcfdtblXpath).equals((String)internal_data_map.get("AddBespokeOT").get(no).get("MFB_OTBS_Description"))){
						TestUtil.reportStatus("Specified Other Types values have been added properly for Item - "+(no+1) , "info", false);
					}else{
						TestUtil.reportStatus("Specified Other Types values have not been added properly" , "Fail", true);
					}
				}
			
			int CarBRIndex = k.getTableIndex("Book Premium", "xpath","html/body/div[3]/form/div/table");
			customAssert.assertTrue(funcValidate_CoverRatedTable(CarBRIndex, no, "AddBespokeOT","MFB_OTBS" ),"Rater Table validation failed for Specified item - "+(no +1));
			
			OtNetP=OtNetP+tempNP;
			no++;
		}
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on ST page .");
		//MFB_ST_ApplyBookRates
		customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Other Types page .");
		int CarBRIndex = k.getTableIndex("Book Premium", "xpath","html/body/div[3]/form/div/table");
		String CRBRTbl = "html/body/div[3]/form/div/table["+CarBRIndex+"]";
		//List<WebElement> we = driver.findElements(By.xpath("CRBRTbl"+"/tbody/tr"));
		
		String OTPremium = k.getAttributeByXpath("//*[@id='mfb_ov_tot']", "value");
		System.out.println(OTPremium);
		map_data.put("PS_OtherTypes_NetNetPremium", OTPremium);
		common.compareValues(OtNetP, Double.parseDouble(OTPremium), "Other Types Net Premium Value");
		
	}catch(Throwable t){
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
        Assert.fail("Unable to enter details in Other Type Page", t);
        return false;	
	}
	
	
	
	return true;
}

//commercial Vehicle & Agriculture vehicles cover

public boolean funcCommercialVehicles(Map<Object, Object> map_data) {
	try{
		CvNetP=0.00;
		customAssert.assertTrue(common.funcPageNavigation("Commercial Vehicles",""), "Commercial Vehicles Page Navigation issue . ");
		if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }
		
		String[] SpecifiedUnspecified = ((String)map_data.get("MFB_CV_Specified")).split(",");
		List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
		for(String geo_limit : SpecifiedUnspecified){
			for(WebElement each_ul : ul_elements){
				customAssert.assertTrue(k.Click("MFB_CV_Specified"),"Error while Clicking Specified/ Unspecified cars List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		
		
		customAssert.assertTrue(k.DropDownSelection("MFB_CV_InsuredInvolved", (String)map_data.get("MFB_CV_InsuredInvolved")),"Unable to select InsuredInvolved value");
		customAssert.assertTrue(k.DropDownSelection("MFB_CV_InsuredDistribrute", (String)map_data.get("MFB_CV_InsuredDistribrute")),"Unable to select HighRiskVehicles value");
		customAssert.assertTrue(k.DropDownSelection("MFB_CV_Explosives", (String)map_data.get("MFB_CV_Explosives")),"Unable to select HighRiskVehicles value");
		customAssert.assertTrue(k.DropDownSelection("MFB_CV_Radioactive", (String)map_data.get("MFB_CV_Radioactive")),"Unable to select HighRiskVehicles value");
		customAssert.assertTrue(k.DropDownSelection("MFB_CV_AirsideCover", (String)map_data.get("MFB_CV_AirsideCover")),"Unable to select HighRiskVehicles value");
		customAssert.assertTrue(k.DropDownSelection("MFB_CV_HighRiskVehicles", (String)map_data.get("MFB_CV_HighRiskVehicles")),"Unable to select HighRiskVehicles value");
		
		if(k.getText("MFB_CV_Specified").contains("Specified")){
			AddSpecifiedCommVehicles(map_data);
		}
		
		if(k.getText("MFB_CV_Specified").contains("Unspecified")){
			AddUnSpecifiedCommVehicles(map_data);
		}
		
		String CVPremium = k.getAttributeByXpath("//*[@id='mfb_cv_tot']", "value");
		System.out.println(CVPremium);
		map_data.put("PS_CommercialVehicles_NetNetPremium", CVPremium);
		common.compareValues(CvNetP, Double.parseDouble(CVPremium), "Commercial Vehicles Net Premium Value");
		
	}
		catch(Throwable t){
		   	String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
	        Assert.fail("Unable to enter details in ClaimsExperience Page", t);
	        return false;	
		}
		return true;
	}

	public boolean AddSpecifiedCommVehicles(Map<Object, Object> map_data){
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
			String[] Sp = ((String)map_data.get("MFB_SpecifiedCommVehicles_Items")).split(";");
			int no = 0;
			int ItemSize = Sp.length;
			int SIndex;
			while(no < ItemSize){
			
				TestUtil.reportStatus("Entering  Details for Specified Commercial Vehicles item - "+ (no+1) , "info", false);
			
				customAssert.assertTrue(k.Click("MFB_CV_AddVehiclespecified"), "Unable to click on Save Button on Commercial Vehicle  page .");
				customAssert.assertTrue(common.funcPageNavigation("Commercial Vehicle – Specified",""), "Specified Commercial Vehicles Page Navigation issue . ");
				
				customAssert.assertTrue(k.Input("MFB_CVSP_RegNumber", (String)internal_data_map.get("AddSpecifiedCommVehicles").get(no).get("MFB_CVSP_RegNumber")),	"Unable to enter value in Accidental Name Name  field .");
				customAssert.assertTrue(k.DropDownSelection("MFB_CVSP_Make", (String)internal_data_map.get("AddSpecifiedCommVehicles").get(no).get("MFB_CVSP_Make")),"Unable to select HighRiskVehicles value");
				
				
				customAssert.assertTrue(k.DropDownSelection("MFB_CVSP_BodyType", (String)internal_data_map.get("AddSpecifiedCommVehicles").get(no).get("MFB_CVSP_BodyType")),"Unable to select InsuredInvolved value");
				customAssert.assertTrue(k.DropDownSelection("MFB_CVSP_VehicleWeight", (String)internal_data_map.get("AddSpecifiedCommVehicles").get(no).get("MFB_CVSP_VehicleWeight")),"Unable to select InsuredInvolved value");
				customAssert.assertTrue(k.DropDownSelection("MFB_CVSP_CoverBasis", (String)internal_data_map.get("AddSpecifiedCommVehicles").get(no).get("MFB_CVSP_CoverBasis")),"Unable to select InsuredInvolved value");
				//customAssert.assertTrue(k.DropDownSelection("MFB_CVSP_ClassOfUse", (String)map_data.get("MFB_CVSP_ClassOfUse")),"Unable to select InsuredInvolved value");
				String[] SpecifiedUnspecified1 = ((String)internal_data_map.get("AddSpecifiedCommVehicles").get(no).get("MFB_CVSP_ClassOfUse")).split(",");
				List<WebElement> ul_elements1 = driver.findElements(By.xpath("//ul"));
				for(String geo_limit : SpecifiedUnspecified1){
					for(WebElement each_ul : ul_elements1){
						customAssert.assertTrue(k.Click("MFB_CVSP_ClassOfUse"),"Error while Clicking Specified/ Unspecified cars List object . ");
						k.waitTwoSeconds();
						if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
							each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
						else
							continue;
						break;
					}
				}
				
				
				customAssert.assertTrue(k.DropDownSelection("MFB_CVSP_AnnualMileage", (String)internal_data_map.get("AddSpecifiedCommVehicles").get(no).get("MFB_CVSP_AnnualMileage")),"Unable to select InsuredInvolved value");
				customAssert.assertTrue(k.DropDownSelection("MFB_CVSP_PermittedDrivers", (String)internal_data_map.get("AddSpecifiedCommVehicles").get(no).get("MFB_CVSP_PermittedDrivers")),"Unable to select InsuredInvolved value");
				customAssert.assertTrue(k.Click("MFB_CVSP_Save"), "Unable to click on Save Button on Cars page .");
				customAssert.assertTrue(k.Click("MFB_CVSP_Back"), "Unable to click on Save Button on Cars page .");
				
				customAssert.assertTrue(common.funcPageNavigation("Commercial Vehicles",""), "Commercial Vehicles Page Navigation issue . ");
				//Make/ Model
				SIndex = k.getTableIndex("Make/ Model", "xpath","html/body/div[3]/form/div/table");
				String spcfdtblxp = "html/body/div[3]/form/div/table["+SIndex+"]";
				String spcfdtblXpath=spcfdtblxp+"/tbody/tr["+(no+1)+"]/td[1]";
				
				customAssert.assertTrue(k.isDisplayedByXpath(spcfdtblxp));
				if(k.getTextByXpath(spcfdtblXpath).equals((String)internal_data_map.get("AddSpecifiedCommVehicles").get(no).get("MFB_CVSP_Make"))){
					TestUtil.reportStatus("Specified Commercial Vehicles values have been added properly for Item - "+(no+1) , "info", false);
				}else{
					TestUtil.reportStatus("Specified Commercial Vehicles values have not been added properly" , "Fail", true);
				}
				
				
				customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Cars page .");
				
				int CarBRIndex = k.getTableIndex("Book Premium", "xpath","html/body/div[3]/form/div/table");
				
				customAssert.assertTrue(funcValidate_CoverRatedTable(CarBRIndex, no, "AddSpecifiedCommVehicles","MFB_CVSP" ),"Rater Table validation failed for Specified item - "+(no +1));
				CvNetP=CvNetP+tempNP;
				
				no++;
			}
		
	}catch(Throwable t){
		return false;
	}
		
		return true;
		
	}
	public boolean AddUnSpecifiedCommVehicles(Map<Object, Object> map_data){
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
			String[] Un = ((String)map_data.get("MFB_UnSpecifiedCommVehicles_Items")).split(";");
			int no = 0;
			int ItemCount = Un.length;
			int uIndex;
//			double UnspecifiedTotal=0.0;
//			double rtf 	;
//			double expBP;
//			double actBP;
//			double ExpPremium, ActPremium;
			
			while(no < ItemCount){
				//customAssert.assertTrue(k.Click("MFB_CARS_AddCarUnspecified"));
				
				
				customAssert.assertTrue(k.Click("MFB_CUV_Add"), "Unable to click on Save Button on Commercial Vehicle  page .");
				customAssert.assertTrue(common.funcPageNavigation("Commercial Vehicle - Unspecified Vehicle",""), "Commercial Vehicle - Unspecified Vehicle Page Navigation issue . ");
				
				customAssert.assertTrue(k.DropDownType("MFB_CUV_VehicleType", (String)internal_data_map.get("AddUnspecifiedCommVehicles").get(no).get("MFB_CUV_VehicleType")),"Unable to select HighRiskVehicles value");
				customAssert.assertTrue(k.Input("MFB_CUV_Number", (String)internal_data_map.get("AddUnspecifiedCommVehicles").get(no).get("MFB_CUV_Number")),	"Unable to enter Number .");
				customAssert.assertTrue(k.DropDownType("MFB_CUV_CoverBasis", (String)internal_data_map.get("AddUnspecifiedCommVehicles").get(no).get("MFB_CUV_CoverBasis")),"Unable to select HighRiskVehicles value");
				customAssert.assertTrue(k.DropDownType("MFB_CUV_Gvw", (String)internal_data_map.get("AddUnspecifiedCommVehicles").get(no).get("MFB_CUV_Gvw")),"Unable to select HighRiskVehicles value");
				
				
				
				
				//customAssert.assertTrue(k.DropDownSelection("MFB_CUV_CoverBasis", (String)internal_data_map.get("AddUnspecifiedCommVehicles").get(no).get("MFB_CUV_CoverBasis")),"Unable to select HighRiskVehicles value");
				//customAssert.assertTrue(k.DropDownSelection("MFB_CUV_Gvw", (String)internal_data_map.get("AddUnspecifiedCommVehicles").get(no).get("MFB_CUV_Gvw")),"Unable to select HighRiskVehicles value");
				
				customAssert.assertTrue(k.Click("MFB_CVSP_Save"), "Unable to click on Save Button on Unspecified Commercial Vehicles page .");
				customAssert.assertTrue(k.Click("MFB_CVSP_Back"), "Unable to click on Save Button on Unspecified Commercial Vehicles page .");
				
				customAssert.assertTrue(common.funcPageNavigation("Commercial Vehicles",""), "Commercial Vehicles Page Navigation issue . ");
				
				//Make/ Model
				int SIndex = k.getTableIndex("Vehicle Type", "xpath","html/body/div[3]/form/div/table");
				String spcfdtblxp = "html/body/div[3]/form/div/table["+SIndex+"]";
				String spcfdtblXpath=spcfdtblxp+"/tbody/tr["+(no+1)+"]/td[2]";
				
				customAssert.assertTrue(k.isDisplayedByXpath(spcfdtblxp));
				if(k.getTextByXpath(spcfdtblXpath).equals((String)internal_data_map.get("AddUnspecifiedCommVehicles").get(no).get("MFB_CUV_VehicleType"))){
					TestUtil.reportStatus("Unspecified Commercial Vehicles values have been added properly for Item - "+(no+1) , "info", false);
				}else{
					TestUtil.reportStatus("Unspecified Commercial Vehicles values have not been added properly" , "Fail", true);
				}
				
                customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Cars page .");
				
				int CarBRIndex = k.getTableIndex("Book Premium", "xpath","html/body/div[3]/form/div/table");
				
				customAssert.assertTrue(funcValidate_CoverRatedTable(CarBRIndex, no, "AddUnspecifiedCommVehicles","MFB_CUV" ),"Rater Table validation failed for Specified item - "+(no +1));				
				
				CvNetP=CvNetP+tempNP;
			no++;
			}
		
	}catch(Throwable t){
		return false;
	}
		
		return true;
		
	}

	public boolean funcAgriculturalVehicles(Map<Object, Object> map_data) {
		try{
			AvNetP=0.00;
			customAssert.assertTrue(common.funcPageNavigation("Agricultural Vehicles",""), "Agricultural Vehicles Page Navigation issue . ");
			if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	         	
		    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
		      }
			
			String[] SpecifiedUnspecified = ((String)map_data.get("MFB_AV_Specified")).split(",");
			List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
			for(String geo_limit : SpecifiedUnspecified){
				for(WebElement each_ul : ul_elements){
					customAssert.assertTrue(k.Click("MFB_AV_Specified"),"Error while Clicking Specified/ Unspecified cars List object . ");
					k.waitTwoSeconds();
					if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
						each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
					else
						continue;
					break;
				}
			}
			
			
			//customAssert.assertTrue(k.DropDownSelection("MFB_AV_PermDrivers", (String)map_data.get("MFB_AV_PermDrivers")),"Unable to select InsuredInvolved value");
			customAssert.assertTrue(k.Input("MFB_AV_Accidental", (String)map_data.get("MFB_AV_Accidental")),"Unable to select HighRiskVehicles value");
			customAssert.assertTrue(k.Input("MFB_AV_FireExcess", (String)map_data.get("MFB_AV_FireExcess")),"Unable to select HighRiskVehicles value");
			customAssert.assertTrue(k.Input("MFB_AV_TheftParty", (String)map_data.get("MFB_AV_TheftParty")),"Unable to select HighRiskVehicles value");
			customAssert.assertTrue(k.Input("MFB_AV_Windscreen", (String)map_data.get("MFB_AV_Windscreen")),"Unable to select HighRiskVehicles value");
			customAssert.assertTrue(k.DropDownSelection("MFB_AV_HighRisk", (String)map_data.get("MFB_AV_HighRisk")),"Unable to select InsuredInvolved value");
			//customAssert.assertTrue(k.Click("MFB_AV_AddSpecified"), "Unable to click on Save Button on Commercial Vehicle  page .");
			
			
			
			
			if(k.getText("MFB_AV_Specified").contains("Specified")){
				AddSpecifiedAgrVehicles(map_data);
			}
			
			if(k.getText("MFB_AV_Specified").contains("Unspecified")){
				AddUnspecifiedAgrVehicles(map_data);
			}
			
					
			String ACVPremium = k.getAttributeByXpath("//*[@id='mfb_av_tot']", "value");
			System.out.println(ACVPremium);
			map_data.put("PS_AgriculturalVehicles_NetNetPremium", ACVPremium);
			common.compareValues(AvNetP, Double.parseDouble(ACVPremium), "Agriculture Vehicles Net Premium Value");
		}
			catch(Throwable t){
			   	String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
		        Assert.fail("Unable to enter details in ClaimsExperience Page", t);
		        return false;	
			}
			return true;
		}

		public boolean AddSpecifiedAgrVehicles(Map<Object, Object> map_data){
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
				String[] Sp = ((String)map_data.get("MFB_SpecifiedAgrVehicles_Items")).split(";");
				int no = 0;
				int ItemSize = Sp.length;
				int SIndex;
				while(no < ItemSize){
				
					TestUtil.reportStatus("Entering  Details for gricultural Vehicles - Specified item - "+ (no+1) , "info", false);
				
					customAssert.assertTrue(k.Click("MFB_AV_AddSpecified"), "Unable to click on Save Button on gricultural Vehicles page.");
					//Agricultural Vehicles - Specified
					customAssert.assertTrue(common.funcPageNavigation("Agricultural Vehicles - Specified",""), "Agricultural Vehicles - Specified Page Navigation issue . ");
					
					customAssert.assertTrue(k.Input("MFB_AVSPE_RegNumber", (String)internal_data_map.get("AddSpecifiedAgrVehicles").get(no).get("MFB_AVSPE_RegNumber")),	"Unable to enter value in MFB_AVSPE_RegNumber  field .");
					customAssert.assertTrue(k.DropDownSelection("MFB_AVSPE_VehicleMake", (String)internal_data_map.get("AddSpecifiedAgrVehicles").get(no).get("MFB_AVSPE_VehicleMake")),"Unable to select MFB_AVSPE_VehicleMake value");
					customAssert.assertTrue(k.DropDownType("MFB_AVSPE_BodyType", (String)internal_data_map.get("AddSpecifiedAgrVehicles").get(no).get("MFB_AVSPE_BodyType")),"Unable to select MFB_AVSPE_BodyType value");
					customAssert.assertTrue(k.DropDownType("MFB_AVSPE_CoverBasis", (String)internal_data_map.get("AddSpecifiedAgrVehicles").get(no).get("MFB_AVSPE_CoverBasis")),"Unable to select MFB_AVSPE_CoverBasis value");
					
					
					String[] SpecifiedUnspecified1 = ((String)internal_data_map.get("AddSpecifiedAgrVehicles").get(no).get("MFB_AVSPE_ClassOfUse")).split(",");
					List<WebElement> ul_elements1 = driver.findElements(By.xpath("//ul"));
					for(String geo_limit : SpecifiedUnspecified1){
						for(WebElement each_ul : ul_elements1){
							customAssert.assertTrue(k.Click("MFB_AVSPE_ClassOfUse"),"Error while Clicking Specified/ Unspecified cars List object . ");
							k.waitTwoSeconds();
							if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
								each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
							else
								continue;
							break;
						}
					}
					
					
					customAssert.assertTrue(k.Input("MFB_AVSPE_AccidentalDamage", (String)internal_data_map.get("AddSpecifiedAgrVehicles").get(no).get("MFB_AVSPE_AccidentalDamage")),	"Unable to enter value in MFB_AVSPE_AccidentalDamage  field .");
					customAssert.assertTrue(k.Input("MFB_AVSPE_FireExcess", (String)internal_data_map.get("AddSpecifiedAgrVehicles").get(no).get("MFB_AVSPE_FireExcess")),	"Unable to enter value in MFB_AVSPE_FireExcess field .");
					customAssert.assertTrue(k.Input("MFB_AVSPE_TheftParty", (String)internal_data_map.get("AddSpecifiedAgrVehicles").get(no).get("MFB_AVSPE_TheftParty")),	"Unable to enter value in MFB_AVSPE_TheftParty  field .");
					customAssert.assertTrue(k.Input("MFB_AVSPE_Windscreen", (String)internal_data_map.get("AddSpecifiedAgrVehicles").get(no).get("MFB_AVSPE_Windscreen")),	"Unable to enter value in MFB_AVSPE_Windscreen  field .");
					customAssert.assertTrue(k.Input("MFB_AVSPE_DriversExcess", (String)internal_data_map.get("AddSpecifiedAgrVehicles").get(no).get("MFB_AVSPE_DriversExcess")),	"Unable to enter value in MFB_AVSPE_DriversExcess field .");
					
					customAssert.assertTrue(k.Click("MFB_AVSPE_SpSave"), "Unable to click on save Button on Agricultural Vehicle - Specified page .");
					customAssert.assertTrue(k.Click("MFB_AUV_UnSpBack"), "Unable to click on back Button on Agricultural Vehicle - Specified page .");
					
					customAssert.assertTrue(common.funcPageNavigation("Agricultural Vehicles",""), "Agricultural Vehicles Page Navigation issue . ");
					
					//Make/ Model
					int SIndex1 = k.getTableIndex("Make", "xpath","html/body/div[3]/form/div/table");
					String spcfdtblxp = "html/body/div[3]/form/div/table["+SIndex1+"]";
					String spcfdtblXpath=spcfdtblxp+"/tbody/tr["+(no+1)+"]/td[1]";
					
					customAssert.assertTrue(k.isDisplayedByXpath(spcfdtblxp));
					if(k.getTextByXpath(spcfdtblXpath).equals((String)internal_data_map.get("AddSpecifiedAgrVehicles").get(no).get("MFB_AVSPE_VehicleMake"))){
						TestUtil.reportStatus("Specified Agriculture Vehicles values have been added properly for Item - "+(no+1) , "info", false);
					}else{
						TestUtil.reportStatus("Specified Agriculture Vehicles values have not been added properly" , "Fail", true);
					}
					
					 customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Agricultural Vehicles Page.");
					int CarBRIndex = k.getTableIndex("Book Premium", "xpath","html/body/div[3]/form/div/table");
					
					customAssert.assertTrue(funcValidate_CoverRatedTable(CarBRIndex, no, "AddSpecifiedAgrVehicles","MFB_AVSPE" ),"Rater Table validation failed for Specified item - "+(no +1));
					AvNetP=AvNetP+tempNP;
					
					no++;
				}
			
		}catch(Throwable t){
			return false;
		}
			
			return true;
			
		}
		public boolean AddUnspecifiedAgrVehicles(Map<Object, Object> map_data){
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
				String[] Un = ((String)map_data.get("MFB_UnSpecifiedAgrVehicles_Items")).split(";");
				int no = 0;
				int ItemCount = Un.length;
				int uIndex;
//				double UnspecifiedTotal=0.0;
//				double rtf 	;
//				double expBP;
//				double actBP;
//				double ExpPremium, ActPremium;
				
				while(no < ItemCount){
					//customAssert.assertTrue(k.Click("MFB_CARS_AddCarUnspecified"));
					TestUtil.reportStatus("Entering  Details for gricultural Vehicles - Unspecified item - "+ (no+1) , "info", false);
					
					customAssert.assertTrue(k.Click("MFB_AUV_AddUnSpecified"), "Unable to click on Save Button on Agricultural Vehicle  page .");
					//Agricultural Vehicles - Unspecified
					customAssert.assertTrue(common.funcPageNavigation("Agricultural Vehicles - Unspecified",""), "Agricultural Vehicles - Unspecified Page Navigation issue . ");
					
					//customAssert.assertTrue(k.DropDownType("MFB_AUV_VehicleType", (String)internal_data_map.get("AddUnspecifiedAgrVehicles").get(no).get("MFB_AUV_VehicleType")),"Unable to select HighRiskVehicles value");
					customAssert.assertTrue(k.Input("MFB_AUV_Number", (String)internal_data_map.get("AddUnspecifiedAgrVehicles").get(no).get("MFB_AUV_Number")),	"Unable to enter Number .");
					customAssert.assertTrue(k.DropDownType("MFB_AUV_CoverBasis", (String)internal_data_map.get("AddUnspecifiedAgrVehicles").get(no).get("MFB_AUV_CoverBasis")),"Unable to select HighRiskVehicles value");
					
					String[] SpecifiedUnspecified2 = ((String)internal_data_map.get("AddUnspecifiedAgrVehicles").get(no).get("MFB_AUV_ClassOfUse")).split(",");
					List<WebElement> ul_elements2 = driver.findElements(By.xpath("//ul"));
					for(String geo_limit : SpecifiedUnspecified2){
						for(WebElement each_ul : ul_elements2){
							customAssert.assertTrue(k.Click("MFB_AUV_ClassOfUse"),"Error while Clicking Specified/ Unspecified Agricultural vehicle List object . ");
							k.waitTwoSeconds();
							if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
								each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
							else
								continue;
							break;
						}
					}

					customAssert.assertTrue(k.DropDownType("MFB_AUV_PermittedDrivers", (String)internal_data_map.get("AddUnspecifiedAgrVehicles").get(no).get("MFB_AUV_PermittedDrivers")),"Unable to select MFB_AUV_PermittedDrivers value");
					customAssert.assertTrue(k.DropDownType("MFB_AUV_HighRisk", (String)internal_data_map.get("AddUnspecifiedAgrVehicles").get(no).get("MFB_AUV_HighRisk")),"Unable to select HighRiskVehicles value");
					customAssert.assertTrue(k.Input("MFB_AUV_AccidentalDamage", (String)internal_data_map.get("AddUnspecifiedAgrVehicles").get(no).get("MFB_AUV_AccidentalDamage")),	"Unable to enter MFB_AUV_AccidentalDamage value .");
					customAssert.assertTrue(k.Input("MFB_AUV_FireExcess", (String)internal_data_map.get("AddUnspecifiedAgrVehicles").get(no).get("MFB_AUV_FireExcess")),	"Unable to enter MFB_AUV_FireExcess .");
					customAssert.assertTrue(k.Input("MFB_AUV_TheftParty", (String)internal_data_map.get("AddUnspecifiedAgrVehicles").get(no).get("MFB_AUV_TheftParty")),	"Unable to enter MFB_AUV_TheftParty .");
					customAssert.assertTrue(k.Input("MFB_AUV_Windscreen", (String)internal_data_map.get("AddUnspecifiedAgrVehicles").get(no).get("MFB_AUV_Windscreen")),	"Unable to enter MFB_AUV_Windscreen .");
					customAssert.assertTrue(k.Input("MFB_AUV_DriversExcess", (String)internal_data_map.get("AddUnspecifiedAgrVehicles").get(no).get("MFB_AUV_DriversExcess")),	"Unable to enter MFB_AUV_DriversExcess .");
					
					//customAssert.assertTrue(k.DropDownSelection("MFB_CUV_CoverBasis", (String)internal_data_map.get("AddUnspecifiedCommVehicles").get(no).get("MFB_CUV_CoverBasis")),"Unable to select HighRiskVehicles value");
					//customAssert.assertTrue(k.DropDownSelection("MFB_CUV_Gvw", (String)internal_data_map.get("AddUnspecifiedCommVehicles").get(no).get("MFB_CUV_Gvw")),"Unable to select HighRiskVehicles value");
					
					customAssert.assertTrue(k.Click("MFB_AUV_UnSpSave"), "Unable to click on Save Button on Cars page .");
					customAssert.assertTrue(k.Click("MFB_AUV_UnSpBack"), "Unable to click on Save Button on Cars page .");
					
					customAssert.assertTrue(common.funcPageNavigation("Agricultural Vehicles",""), "Agricultural Vehicles Page Navigation issue . ");
					
					//Make/ Model
					int SIndex1 = k.getTableIndex("Body Type", "xpath","html/body/div[3]/form/div/table");
					String spcfdtblxp = "html/body/div[3]/form/div/table["+SIndex1+"]";
					String spcfdtblXpath=spcfdtblxp+"/tbody/tr["+(no+1)+"]/td[7]";
					
					customAssert.assertTrue(k.isDisplayedByXpath(spcfdtblxp));
					if(k.getTextByXpath(spcfdtblXpath).equals((String)internal_data_map.get("AddUnspecifiedAgrVehicles").get(no).get("MFB_AUV_PermittedDrivers"))){
						TestUtil.reportStatus("Unspecified Agriculture Vehicles values have been added properly for Item - "+(no+1) , "info", false);
					}else{
						TestUtil.reportStatus("Unspecified Agriculture Vehicles values have not been added properly" , "Fail", true);
					}
					
					
	                customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Cars page .");
					
					int CarBRIndex = k.getTableIndex("Book Premium", "xpath","html/body/div[3]/form/div/table");
					
					customAssert.assertTrue(funcValidate_CoverRatedTable(CarBRIndex, no, "AddUnspecifiedCommVehicles","MFB_CUV" ),"Rater Table validation failed for Specified item - "+(no +1));				
					
					AvNetP=AvNetP+tempNP;
				no++;
				}
			
		}catch(Throwable t){

			return false;
		}
			
			return true;
			
		}
		
		public boolean PersonalAccidentOptionalPage(Map<Object, Object> map_data){
			try{
				PoaNetP=0.00;
				String cur="";
				if(TestBase.product.equals("MFB"))
					cur="€";
				else if(TestBase.product.equals("MFC"))
					cur="£";
				
				customAssert.assertTrue(common.funcPageNavigation("Personal Accident Optional",""), "Personal Accident Optional Page Navigation issue . ");
				
				if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
		         	
			    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
			      }
				
				customAssert.assertTrue(k.DropDownType("MFB_PAO_PersonsGoodHealth", (String)map_data.get("MFB_PAO_PersonsGoodHealth")),"Unable to select MFB_PAO_PersonsGoodHealth value");
				customAssert.assertTrue(k.DropDownType("MFB_PAO_Deformity", (String)map_data.get("MFB_PAO_Deformity")),"Unable to select MFB_PAO_Deformity value");
				customAssert.assertTrue(k.DropDownType("MFB_PAO_DriverExtension", (String)map_data.get("MFB_PAO_DriverExtension")),"Unable to select MFB_PAO_DriverExtension value");
				
				
				customAssert.assertTrue(k.DropDownType("MFB_PAO_BenefitAmount", cur+(String)map_data.get("MFB_PAO_BenefitAmount")),"Unable to select MFB_PAO_BenefitAmount value");
				
				int indx = k.getTableIndex("Activities", "xpath","html/body/div[3]/form/div/table");
				//html/body/div[3]/form/div/table[2]/tbody/tr[1]/td[2]/input
				String PV_Driver_NOP="html/body/div[3]/form/div/table["+indx+"]/tbody/tr[1]/td[2]/input";
				String PV_Driver_AW="html/body/div[3]/form/div/table["+indx+"]/tbody/tr[1]/td[3]/input";
				String TankerCV_Driver_NOP="html/body/div[3]/form/div/table["+indx+"]/tbody/tr[2]/td[2]/input";
				String TankerCV_Driver_AW="html/body/div[3]/form/div/table["+indx+"]/tbody/tr[2]/td[3]/input";
				
				customAssert.assertTrue(k.InputByXpath(PV_Driver_NOP,(String)map_data.get("PV_Driver_NOP")),"Unable to select PV_Driver_NOP value");
				customAssert.assertTrue(k.InputByXpath(PV_Driver_AW,(String)map_data.get("PV_Driver_AW")),"Unable to select PV_Driver_AW value");
				customAssert.assertTrue(k.InputByXpath(TankerCV_Driver_NOP,(String)map_data.get("TankerCV_Driver_NOP")),"Unable to select TankerCV_Driver_NOP value");
				customAssert.assertTrue(k.InputByXpath(TankerCV_Driver_AW,(String)map_data.get("TankerCV_Driver_AW")),"Unable to select TankerCV_Driver_AW value");
				
				Map<Integer,Double> tmp = new HashMap<>();
				Map<Integer,Double> tmp1 = new HashMap<>();
				Map<String,Map<Integer,Double>> PAO_initial_Rater = new HashMap<>();
				
				tmp.put(50,12.00);			
				PAO_initial_Rater.put("PV_Driver",tmp);
				tmp1.put(50,18.00);
				PAO_initial_Rater.put("TankerCV_Driver",tmp1);
				
				tmp.put(100,9.8);			
				PAO_initial_Rater.put("PV_Driver",tmp);
				tmp1.put(100,14.00);
				PAO_initial_Rater.put("TankerCV_Driver",tmp1);
				
				tmp.put(150,11.2);			
				PAO_initial_Rater.put("PV_Driver",tmp);
				tmp1.put(150,22.00);
				PAO_initial_Rater.put("TankerCV_Driver",tmp1);
				
			//	customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Personal Acidental Option page .");
				int TblIndx = k.getTableIndex("Book Premium", "xpath","html/body/div[3]/form/div/table");
				
				tempNP = 0.00;
				int uIndex;
				double UnspecifiedTotal=0.0;
				double rtf 	;
				double expBP;
				double actBP;
				double ExpPremium, ActPremium;
				int no=0;
				
								
				String CRBRTbl = "html/body/div[3]/form/div/table["+TblIndx+"]";
				int row = driver.findElements(By.xpath(CRBRTbl+"/tbody/tr")).size();
				if(row == 0){
					TestUtil.reportStatus("No Row has been added in cover rating  table", "Fail", false);
				}else
					row--;
				
				String Tbl = CRBRTbl +"/tbody/tr["+(1)+"]/td";
				String Tbl1 = CRBRTbl +"/tbody/tr["+(2)+"]/td";
				String tblH= CRBRTbl +"/thead/tr/th"; //html/body/div[3]/form/div/table[4]/thead/tr/th
				 //html/body/div[3]/form/div/table[4]/thead/tr/th
				
				//List<WebElement> we = driver.findElements(By.xpath("CRBRTbl"+"/tbody/tr"));
				String Desc = k.getTextByXpath(Tbl+"["+getTableHeaderIndex(tblH,"Description")+"]" );
				String Desc1 = k.getTextByXpath(Tbl1+"["+getTableHeaderIndex(tblH,"Description")+"]" );
				String initial_rateXP = (Tbl+"["+getTableHeaderIndex(tblH,"Initial Rate")+"]/input" );
				String initial_rateXP1 = (Tbl1+"["+getTableHeaderIndex(tblH,"Initial Rate")+"]/input" );
				String Tech_AdjustXP = (Tbl+"["+getTableHeaderIndex(tblH,"Tech. Adjust")+"]/input");
				String Tech_AdjustXP1 = (Tbl1+"["+getTableHeaderIndex(tblH,"Tech. Adjust")+"]/input");
				
				String PremiumXP =(Tbl+"["+getTableHeaderIndex(tblH,"Premium")+"]/input");
				String PremiumXP1 =(Tbl1+"["+getTableHeaderIndex(tblH,"Premium")+"]/input");
				
				
				String stir = ((k.getAttribute("MFB_PAO_BenefitAmount", "value").replace(cur,"")).trim());
				int ir = Integer.parseInt(stir);
				
								
				String InitialRate_PV_Driver = PAO_initial_Rater.get("PV_Driver").get(ir).toString();
				String InitialRate_TankerCV_Driver = PAO_initial_Rater.get("TankerCV_Driver").get(ir).toString();
				
				String TechAdjust_PV = (String)map_data.get("MFB_POA_PV_TechAdjust");
				String TechAdjust_CV = (String)map_data.get("MFB_POA_CV_TechAdjust");
				
									
				
				//MFB_CARUNSPECIFIED_InitialRate
				customAssert.assertTrue(k.InputByXpath(Tech_AdjustXP,TechAdjust_PV),"Issue while entering data in Tech Adjust% PV rate");
				customAssert.assertTrue(k.InputByXpath(Tech_AdjustXP1,TechAdjust_CV),"Issue while entering data in Tech Adjust% CV rate");
				
				
				//Click on appy book rate button
				customAssert.assertTrue(k.Click("MFB_POA_AppyBookRates"), "Unable to click on Apply Bookrate Button on Personal Accidental Optional page for ");
				 
				rtf = Double.parseDouble(k.getTextByXpath(Tbl+"["+getTableHeaderIndex(tblH,"Rating")+"]"));
				double rtf1 = Double.parseDouble(k.getTextByXpath(Tbl1+"["+getTableHeaderIndex(tblH,"Rating")+"]"));
				 
				 expBP = rtf* Double.parseDouble(InitialRate_PV_Driver); 
				 double expBP1 = rtf1* Double.parseDouble(InitialRate_TankerCV_Driver);
				 
				 actBP = Double.parseDouble(k.getAttributeByXpath(Tbl+"["+getTableHeaderIndex(tblH,"Book Premium")+"]/input","value"));
				 double actBP1 = Double.parseDouble(k.getAttributeByXpath(Tbl1+"["+getTableHeaderIndex(tblH,"Book Premium")+"]/input","value"));
				 
				 //double actBP_CV = Double.parseDouble(k.getText(Tbl1+"["+getTableHeaderIndex(tblH,"Book Premium")+"]/input"));
				 common.compareValues(expBP, actBP, "Book Premium Value - "+Desc+" - PV Item :");
				 common.compareValues(expBP1, actBP1, "Book Premium Value - "+Desc+" - CV Item :");
				 
				 
				 ExpPremium = expBP + (expBP*((Double.parseDouble(TechAdjust_PV))/100));
				 double ExpPremium_CV = expBP1 + (expBP1*((Double.parseDouble(TechAdjust_CV))/100));
				 
				 ActPremium = Double.parseDouble(k.getAttributeByXpath(PremiumXP, "value"));
				 double ActPremium_CV = Double.parseDouble(k.getAttributeByXpath(PremiumXP1, "value"));
				 common.compareValues(ExpPremium, ActPremium, "Premiums values - "+Desc+" - PV Item :");
				 common.compareValues(ExpPremium_CV, ActPremium_CV, "Premiums values - "+Desc+" - CV Item :");
				 
				 PoaNetP =  ExpPremium+ExpPremium_CV;
			
							
				String PAOPremium = k.getAttributeByXpath("//*[@id='mfb_pao_tot']", "value");
				System.out.println(PAOPremium);
				map_data.put("PS_PersonalAccidentOptional_NetNetPremium", PAOPremium);
				common.compareValues(PoaNetP, Double.parseDouble(PAOPremium), "Personal Accidental Optional Net Premium Value");
			}
			catch(Throwable t){
				   	String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
			        Assert.fail("Unable to enter details in Personal Accident Optional Page", t);
			        return false;	
			}
			return true;
		}
		public boolean funcDrivers(Map<Object, Object> map_data) {
			try{
				customAssert.assertTrue(common.funcPageNavigation("Drivers",""), "Drivers Page Navigation issue . ");
				customAssert.assertTrue(k.Input("MFB_DS_DriversEmployed", (String)map_data.get("MFB_DS_DriversEmployed")),"Unable to select Use value");
				
				String[] SpecifiedUnspecified = ((String)map_data.get("MFB_DS_Use")).split(",");
				List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
				for(String geo_limit : SpecifiedUnspecified){
					for(WebElement each_ul : ul_elements){
						customAssert.assertTrue(k.Click("MFB_DS_Use"),"Error while Clicking Specified/ Unspecified cars List object . ");
						k.waitTwoSeconds();
						if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
							each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
						else
							continue;
						break;
					}
				}
				
				customAssert.assertTrue(k.Input("MFB_DS_Convictions", (String)map_data.get("MFB_DS_Convictions")),"Unable to select Convictions value");
				customAssert.assertTrue(k.Input("MFB_DS_InsuredOperate", (String)map_data.get("MFB_DS_InsuredOperate")),"Unable to select InsuredOperate value");
				customAssert.assertTrue(k.Input("MFB_DS_InsuredKeep", (String)map_data.get("MFB_DS_InsuredKeep")),"Unable to select InsuredKeep value");
				customAssert.assertTrue(k.Input("MFB_DS_MotorConvictions", (String)map_data.get("MFB_DS_MotorConvictions")),"Unable to select MotorConvictions value");
				customAssert.assertTrue(k.Input("MFB_DS_MedicalHistory", (String)map_data.get("MFB_DS_MedicalHistory")),"Unable to select MedicalHistory value");
				customAssert.assertTrue(k.Input("MFB_DS_DVLA", (String)map_data.get("MFB_DS_DVLA")),"Unable to select DVLA value");
				customAssert.assertTrue(k.Input("MFB_DS_AgeOf30", (String)map_data.get("MFB_DS_AgeOf30")),"Unable to select AgeOf30 value");
				customAssert.assertTrue(k.Click("MFB_DS_Save"), "Unable to click on Save Button on Drivers page .");
				//customAssert.assertTrue(k.Click("MFB_DS_Back"), "Unable to click on Back Button on Drivers page .");
				
			}
				catch(Throwable t){
				   	String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
			        Assert.fail("Unable to enter details in Drivers Page", t);
			        return false;	
				}
				return true;
			}

		public boolean funcComputer(Map<Object, Object> mdata) {
			double Comp_HW_Amt=0.00;
			double Comp_P_Amt=0.00;
			try {
				customAssert.assertTrue(common.funcPageNavigation("Computers",""), "Computers Page Navigation issue . ");
				
				if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
		         	
			    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
			      }
				// Retrieving the additional item count over the default item.
				int iCount = (((String)mdata.get("Computer_Items_list")).split(";")).length;
				
				if(iCount>0){
					
					tempNP=0.00;
					Map<String, List<Map<String, String>>> internal_data_map = new HashMap<>();
					String Ctype;
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
					
					for(int i =0;i<iCount;i++){
						
						customAssert.assertTrue(k.Click("CMA_Add_computer_Item"), "Unable to select CMA_Add_omputer_Item");
						k.waitTwoSeconds();
						
						customAssert.assertTrue(k.DropDownSelection("CMA_Computer_Item_Type",(String) internal_data_map.get("AddComputerItem").get(i).get("CMA_Add_computer_Item")),
								"Unable to select the CMA_Computer_Item_Type ");
						customAssert.assertTrue(k.Input("CMA_Computer_item_sum_insured",(String) internal_data_map.get("AddComputerItem").get(i).get("CMA_Computer_item_sum_insured")),
								"Unable to select CMA_Computer_item_sum_insured  ");
						customAssert.assertTrue(k.DropDownSelection("CMA_Territorial_Limit",(String) internal_data_map.get("AddComputerItem").get(i).get("CMA_Territorial_Limit")),
								"Unable to select CMA_Territorial_Limit ");
						customAssert.assertTrue(k.DropDownType("CMA_Alarm_Details", (String)internal_data_map.get("AddComputerItem").get(i).get("CMA_Alarm_Details")),
								"Unable to select CMA_Alarm_Details");
						customAssert.assertTrue(k.Click("CMA_computer_item_Save"), "Unable to click on save button");
						
						int SIndex = k.getTableIndex("Computer Item Type", "xpath","html/body/div[3]/form/div/table");
						String spcfdtblxp = "html/body/div[3]/form/div/table["+SIndex+"]";
						String spcfdtblXpath=spcfdtblxp+"/tbody/tr["+(i+1)+"]/td[1]";
						
						customAssert.assertTrue(k.isDisplayedByXpath(spcfdtblxp));
						if(k.getTextByXpath(spcfdtblXpath).equals((String)internal_data_map.get("AddComputerItem").get(i).get("CMA_Add_computer_Item"))){
							TestUtil.reportStatus("Computer Items values have been added properly for Item - "+(i) , "info", false);
							
							Ctype=((String) internal_data_map.get("AddComputerItem").get(i).get("CMA_Add_computer_Item")).replaceAll(" ", "_");
							tempNP = Double.parseDouble(internal_data_map.get("AddComputerItem").get(i).get("CMA_Computer_item_sum_insured"));
							
							if((OR.properties.getProperty(Ctype)).equals("Hardware")){
								Comp_HW_Amt=Comp_HW_Amt+tempNP;
							}else if((OR.properties.getProperty(Ctype)).equals("Portable")){
								Comp_P_Amt=Comp_P_Amt+tempNP;
							}
							tempNP=0.00;
						}else{
							TestUtil.reportStatus("Computer Items  values have not been added properly" , "Fail", true);
						}
						
						
						
						customAssert.assertTrue(k.Click("CMA_Apply_Book_Rate"), "unable to click on CMA_Apply_Book_Rate");
						k.waitTwoSeconds();
					}
					
				}
				// Computer Rating references are used from file -> OR_CMA_Rater.properties
				// Computer Hardware Premium ratings
				
				double Hw_Initial_rate = 0.00;
				if((Comp_HW_Amt >=0) && (Comp_HW_Amt<=50000)){
					Hw_Initial_rate = Double.parseDouble(OR.properties.getProperty("NonPortable_Item_Rating_£0_to_£50000"));
				}else if((Comp_HW_Amt >=50001) && (Comp_HW_Amt<=100000)){
					Hw_Initial_rate = Double.parseDouble(OR.properties.getProperty("NonPortable_Item_Rating_£50001_to_£100000"));
				}else if((Comp_HW_Amt >=100001) && (Comp_HW_Amt<=150000)){
					Hw_Initial_rate = Double.parseDouble(OR.properties.getProperty("NonPortable_Item_Rating_£100001_to_£150000"));
				}else if((Comp_HW_Amt >=150001) && (Comp_HW_Amt<=200000)){
					Hw_Initial_rate = Double.parseDouble(OR.properties.getProperty("NonPortable_Item_Rating_£150001_to_£250000"));
				}else if((Comp_HW_Amt >=200001) && (Comp_HW_Amt<=250000)){
					Hw_Initial_rate = Double.parseDouble(OR.properties.getProperty("NonPortable_Item_Rating_£250001_to_£500000"));
				}else if((Comp_HW_Amt >=250001) && (Comp_HW_Amt<=500000)){
					Hw_Initial_rate = Double.parseDouble(OR.properties.getProperty("NonPortable_Item_Rating_£500001_to_£750000"));
				}else if((Comp_HW_Amt >=500001) && (Comp_HW_Amt<=750000)){
					Hw_Initial_rate = Double.parseDouble(OR.properties.getProperty("NonPortable_Item_Rating_£750001_to_£1000000"));
				}else if((Comp_HW_Amt >=750001) && (Comp_HW_Amt<=1000000)){
					Hw_Initial_rate = Double.parseDouble(OR.properties.getProperty("NonPortable_Item_Rating_£1000001"));
				}else if((Comp_HW_Amt >=1000001)){
					Hw_Initial_rate = 0.00;
					TestUtil.reportStatus("Hardware sum assured is greater that 1000000, So it will be referred", "Fail", false);
				}
								
				double P_Initial_rate = Double.parseDouble(OR.properties.getProperty("Portable_Items_Rate"));
				
				int CompBRIndex = k.getTableIndex("Revised Premium", "xpath","html/body/div[3]/form/div/table");
				String CmpBRTbl = "html/body/div[3]/form/div/table["+CompBRIndex+"]";
				
				// - html/body/div[3]/form/div/table[6]/tbody/tr[1]/td[2]/input
				String HW_IR_path = CmpBRTbl+"/tbody/tr[1]/td[2]/input";
				String P_IR_path=CmpBRTbl+"/tbody/tr[2]/td[2]/input";
				String HW_SI_path=CmpBRTbl+"/tbody/tr[1]/td[3]";
				String P_SI_path=CmpBRTbl+"/tbody/tr[2]/td[3]";
				String HW_BR_path = CmpBRTbl+"/tbody/tr[1]/td[5]/input";
				String P_BR_path=CmpBRTbl+"/tbody/tr[2]/td[5]/input";
				String HW_BP_path = CmpBRTbl+"/tbody/tr[1]/td[6]/input";
				String P_BP_path=CmpBRTbl+"/tbody/tr[2]/td[6]/input";
				String HW_TechAdj_path = CmpBRTbl+"/tbody/tr[1]/td[7]/input";
				String P_TechAdj_path=CmpBRTbl+"/tbody/tr[2]/td[7]/input";
				String HW_PO_path = CmpBRTbl+"/tbody/tr[1]/td[9]/input";
				String P_PO_path=CmpBRTbl+"/tbody/tr[2]/td[9]/input";
				
				String HW_Prem_path = CmpBRTbl+"/tbody/tr[1]/td[10]/input";
				String P_Prem_path=CmpBRTbl+"/tbody/tr[2]/td[10]/input";
				
				
				customAssert.assertTrue(k.InputByXpath(HW_TechAdj_path, (String)mdata.get("CMA_HNP_TechAdjust")),"Unable to enter Hardware Tech Adjustment value");
				customAssert.assertTrue(k.InputByXpath(P_TechAdj_path, (String)mdata.get("CMA_PI_TechAdjust")),"Unable to enter Portable Tech Adjustment value");
				customAssert.assertTrue(k.InputByXpath(HW_PO_path, (String)mdata.get("CMA_HNP_PremiumOverride")),"Unable to enter Hardware Premium Overide value");
				customAssert.assertTrue(k.InputByXpath(P_PO_path, (String)mdata.get("CMA_PI_PremiumOverride")),"Unable to enter Poratble Premium Overide value");
				driver.findElement(By.xpath(P_PO_path)).sendKeys(Keys.TAB);
				
				customAssert.assertTrue(k.Click("CMA_Apply_Book_Rate"), "unable to click on CMA_Apply_Book_Rate");
				k.waitTwoSeconds();
				
				
				double act_HW_IR = Double.parseDouble(k.getAttributeByXpath(HW_IR_path,"value"));
				double act_P_IR = Double.parseDouble(k.k.getAttributeByXpath(P_IR_path,"value"));
				double act_HW_SI = Double.parseDouble(k.getTextByXpath(HW_SI_path));
				double act_P_SI = Double.parseDouble(k.getTextByXpath(P_SI_path));
				double act_HW_BR = Double.parseDouble(k.k.getAttributeByXpath(HW_BR_path,"value"));
				double act_P_BR = Double.parseDouble(k.k.getAttributeByXpath(P_BR_path,"value"));
				double act_HW_TechAdj = Double.parseDouble(k.k.getAttributeByXpath(HW_TechAdj_path,"value"));
				double act_P_TechAdj = Double.parseDouble(k.k.getAttributeByXpath(P_TechAdj_path,"value"));
				double act_HW_PO = Double.parseDouble(k.k.getAttributeByXpath(HW_PO_path,"value"));
				double act_P_PO = Double.parseDouble(k.k.getAttributeByXpath(P_PO_path,"value"));
				double act_HW_Prem = Double.parseDouble(k.k.getAttributeByXpath(HW_Prem_path,"value"));
				double act_P_Prem = Double.parseDouble(k.k.getAttributeByXpath(P_Prem_path,"value"));
				
				double exp_HW_IR = Hw_Initial_rate;
				double exp_P_IR = Comp_P_Amt*(P_Initial_rate/100);
				double exp_HW_SI =Comp_HW_Amt;
				double exp_P_SI = Comp_P_Amt;
				double exp_HW_BR =Hw_Initial_rate;
				double exp_P_BR = Comp_P_Amt*(P_Initial_rate/100);
				double exp_HW_TechAdj = Double.parseDouble((String)mdata.get("CMA_HNP_TechAdjust"));
				double exp_P_TechAdj =Double.parseDouble((String)mdata.get("CMA_PI_TechAdjust"));
				double exp_HW_PO =Double.parseDouble((String)mdata.get("CMA_HNP_PremiumOverride"));
				double exp_P_PO = Double.parseDouble((String)mdata.get("CMA_PI_PremiumOverride"));
				double exp_HW_Prem =0.00;
				double exp_P_Prem =0.00;
				
				if(exp_HW_PO > 0){
					exp_HW_Prem = exp_HW_PO;
				}
				if(exp_P_PO > 0){
					exp_P_Prem = exp_P_PO;
				}
				
				exp_HW_Prem = exp_HW_BR +(exp_HW_BR * (exp_HW_TechAdj / 100));
				exp_P_Prem = exp_P_BR +(exp_P_BR * (exp_P_TechAdj / 100));
								
				double exp_CompPremium = exp_HW_Prem + exp_P_Prem;
				String act_CompPremium = k.getAttributeByXpath("//*[@id='cma_tot']", "value");
				
				
				// Initial Rate
				common.compareValues(exp_HW_IR, act_HW_IR, "Hardware Initial Rate");
				common.compareValues(exp_P_IR, act_P_IR, "Portable Initial Rate");
				
				// Sum Insured
				common.compareValues(exp_HW_SI, act_HW_SI, "Hardware Sum Insured");
				common.compareValues(exp_P_SI, act_P_SI, "Portable Sum Insured");
				
				//Book Rate
				common.compareValues(exp_HW_BR, act_HW_BR, "Hardware Book Rate");
				common.compareValues(exp_P_BR, act_P_BR, "Portable Book Rate");
				
				// Book Premium
				common.compareValues(exp_HW_Prem, act_HW_Prem, "Hardware  Premium");
				common.compareValues(exp_P_Prem, act_P_Prem, "Portable  Premium");
				
				// Net Premium (Cover Net Premium)
				
				common.compareValues(exp_CompPremium, Double.parseDouble(act_CompPremium), "Cover Net Premium");
				
				
				
				System.out.println(act_CompPremium);
				//common.compareValues(CvNetP, Double.parseDouble(CompPremium), "Computerss Net Premium Value");
				mdata.put("PS_Computers_NetNetPremium", act_CompPremium);
				
				
				
				
				TestUtil.reportStatus("Rating table Calculated Sucessfully", "Info", true);

				return true;

			} catch (Throwable t) {
				String methodName = new Object() {
				}.getClass().getEnclosingMethod().getName();
				TestUtil.reportFunctionFailed("Failed in " + methodName + " function");
				k.reportErr("Failed in " + methodName + " function", t);
				Assert.fail("Unable to fill the details on Computer page \n", t);
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
