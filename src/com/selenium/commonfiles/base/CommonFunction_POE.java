package com.selenium.commonfiles.base;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.selenium.commonfiles.util.ErrorInTestMethod;
import com.selenium.commonfiles.util.TestUtil;

public class CommonFunction_POE extends TestBase {
	
	SimpleDateFormat df = new SimpleDateFormat();
	public double MD_TotalPremium= 0.0 , LOI_TotalPremium = 0.0, PremiumExcTerrDocAct = 0, InsTaxDocAct = 0,InsTaxDocExp = 0;
	public int err_count = 0 , trans_error_val = 0;
	public static DecimalFormat f = new DecimalFormat("00.00");
	public boolean PremiumFlag = false;
	public static int errorVal=0,counter = 0;
	public boolean isInsuranceTaxDone = false;
	public static double totalGrossTax = 0.0;
	public double totalGrossTaxMTA = 0.0;
	public static double totalGrossPremium = 0.0;
	public double totalGrossPremiumMTA=0.0;
	public double totalNetPremiumTax=0.0;
	public double totalNetPremiumTaxMTA=0.0;
	public static WebElement taxTable_tBody;
	public static WebElement objTable;
	public static WebElement taxTable_tHead;
	public static int countOfCovers,countOfTableRows;
	public static Map<Object, Integer> variableTaxAdjustmentIDs = null;
	public static Map<Object, Integer> variableTaxAdjustmentIDsMTA = null;
	public static Map<Object, Double> grossTaxValues_Map = null;
	public static Map<Object, Map<Object, Object>> variableTaxAdjustmentVerificationMaps = null;
	public static Map<Object, Object> variableTaxAdjustmentDataMaps = null;
	public static Map<Object, Object> variableTaxAdjustmentDataMapsMTA = null;
	public static List<Object> headerNameStorage = null;
	public static List<Object> headerNameStorageMTA = null;
	public Map<String,Map<String,Double>> transaction_Premium_Values = new HashMap<>();
	public static ArrayList<Object> inputarraylist = null;
	public static ArrayList<Object> inputarraylistMTA = null;
	public static Map<String , String> AdjustedTaxDetails = new LinkedHashMap<String, String>();
	public static Map<String , String> AdjustedTaxCollection = new LinkedHashMap<String, String>();
	public static Map<String, Double> Adjusted_Premium_map = null;
	public static double adjustedPremium = 0.0,adjustedTotalPremium=0.0,adjustedTotalPremiumMTA=0.0,adjustedTotalTax=0.0,adjustedTotalTaxMTA=0.0,unAdjustedTotalTax=0.0,unAdjustedTotalTaxMTA=0.0;
	public static double PD_TotalRate = 0.0, PD_AdjustedRate = 0.0, PD_MD_Premium=0.0, PD_BI_Premium=0.0, PD_MD_TotalPremium = 0.00, PD_BI_TotalPremium = 0.00, finalMDPremium = 0.00, finalBIPremium= 0.00;
	public boolean isMTARewindFlow=false,isFPEntries=false,isMTARewindStarted=false, isNBRewindStarted = false, isNBRquoteStarted = false;
	public String FP_Covers = null;
	public double rewindMTADoc_AddTaxTer = 0.00;
	public double rewindMTADoc_Premium = 0.00, rewindMTADoc_TerP = 0.00, rewindMTADoc_InsPTax = 0.00, rewindMTADoc_TotalP = 0.00;
	public double rewindDoc_Premium = 0.00, rewindDoc_TerP = 0.00, rewindDoc_InsPTax = 0.00, rewindDoc_TotalP = 0.00, rewindDoc_InsTaxTer = 0.00;
	public double AdditionalPWithAdminDocAct = 0.00, AdditionalExcTerrDocAct = 0.00,  AdditionalTerPDocAct = 0.00, AdditionalInsTaxDocAct = 0.00;
	public double InsTaxTerrDoc = 0.00, tpTotal = 0.00, AddTaxTerrDoc = 0.00;	



	public void NewBusinessFlow(String code,String event){
		String testName = (String)common.NB_excel_data_map.get("Automation Key");
		try{

			customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
			customAssert.assertTrue(common.checkClient(common.NB_excel_data_map,code,event),"Unable to check Client.");
			customAssert.assertTrue(common.createNewQuote(common.NB_excel_data_map,code,event), "Unable to create new quote.");
			customAssert.assertTrue(common.selectLatestQuote(common.NB_excel_data_map,code,event), "Unable to select quote from table.");
			customAssert.assertTrue(funcPolicyDetails(common.NB_excel_data_map), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"),"Issue while Navigating to Previous Claims  . ");
			customAssert.assertTrue(common_CCF.funcPreviousClaims(common.NB_excel_data_map), "Previous claim function is having issue(S) .");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcCovers(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified Perils"),"Issue while Navigating to Specified Perils  . ");
			customAssert.assertTrue(common.funcSpecifiedPerils(common.NB_excel_data_map), "Select covers function is having issue(S) . ");

			if(((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(funcInsuredProperties(common.NB_excel_data_map), "Insured Property function is having issue(S) . ");
			}

			if(((String)common.NB_excel_data_map.get("CD_Liability")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(common_CCF.funcEmployersLiability(common.NB_excel_data_map), "Employers Liability function is having issue(S) . ");
		//		customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
		//		customAssert.assertTrue(common_CCF.funcELDInformation(common.NB_excel_data_map), "ELD Information function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability"),"Issue while Navigating to Property Owners Liability  . ");
				customAssert.assertTrue(funcPropertyOwnersLiability(common.NB_excel_data_map), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability Information"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(funcLiabilityInformation(common.NB_excel_data_map), "Liability Information function is having issue(S) . ");
			}

			if(((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(common_CCF.funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
			}

			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			Assert.assertTrue(common.funcPremiumSummary(common.NB_excel_data_map,code,event,"NB"));

			Assert.assertTrue(common_PEN.funcStatusHandling(common.NB_excel_data_map,code,event));
			if(TestBase.businessEvent.equalsIgnoreCase("NB")){
				customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
				customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
				TestUtil.reportTestCasePassed(testName);
			}


		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
		}

	}

	public void MTAFlow(String code,String event) throws ErrorInTestMethod{

		String NavigationBy = CONFIG.getProperty("NavigationBy");
		String testName = (String)common.MTA_excel_data_map.get("Automation Key");

		try{

			if(!TestBase.businessEvent.equalsIgnoreCase("Renewal")){
				NewBusinessFlow(code,"NB");
			}
			common_PEN.AdjustedTaxDetails.clear();
			common.currentRunningFlow="MTA";
			common_HHAZ.CoversDetails_data_list.clear();
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(NavigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen.");
			customAssert.assertTrue(common_POB.funcCreateEndorsement(),"Error in Create Endorsement function .");
			//customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
			/*customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			//customAssert.assertTrue(common.funcVerifyPolicyStatus(common.MTA_excel_data_map,code,TestBase.businessEvent,"Endorsement Submitted"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
			*/
			customAssert.assertTrue(common_POB.funcPolicyDetails(common.MTA_excel_data_map),"Policy Details function having issue");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"), "Issue while Navigating to Previous Claims");
			customAssert.assertTrue(common_CCF.funcPreviousClaims(common.MTA_excel_data_map), "Previous claim function is having issue(S) .");
			String MTA_Method = (String)common.MTA_excel_data_map.get("MTA_TCDescription");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(NavigationBy,"Covers"),"Issue while Navigating to Covers screen.");
			customAssert.assertTrue(common.funcCovers(common.MTA_excel_data_map), "Select covers function is having issue(S) . ");
	//		customAssert.assertTrue(common_POB.funcUpdateCoverDetails_MTA(common.MTA_excel_data_map),"Error in selecting cover for MTA.");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified Perils"),"Issue while Navigating to Specified Perils  . ");
			customAssert.assertTrue(common.funcSpecifiedPerils(common.MTA_excel_data_map), "Select covers function is having issue(S) . ");


			if(((String)common.MTA_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(common_POB.funcInsuredProperties(common.MTA_excel_data_map), "Insured Property function is having issue(S) . ");
			}

			if(((String)common.MTA_excel_data_map.get("CD_Liability")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(common_CCF.funcEmployersLiability(common.MTA_excel_data_map), "Employers Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability"),"Issue while Navigating to Property Owners Liability  . ");
				customAssert.assertTrue(common_POB.funcPropertyOwnersLiability(common.MTA_excel_data_map), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability Information"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(common_POB.funcLiabilityInformation(common.MTA_excel_data_map), "Liability Information function is having issue(S) . ");
			}

			if(((String)common.MTA_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(common_CCF.funcTerrorism(common.MTA_excel_data_map), "Terrorism function is having issue(S) . ");
			}

			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(NavigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			Assert.assertTrue(common_PEN.funcPremiumSummary_MTA(common.MTA_excel_data_map,code,event));
			if(!TestBase.businessEvent.equalsIgnoreCase("Renewal")){
				Assert.assertTrue(common_PEN.funcStatusHandling(common.MTA_excel_data_map,code,event));
			}

			TestUtil.reportStatus("Test Method of MTA For - "+code, "Pass", true);

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
			common.CoversDetails_data_list.clear();
			String navigationBy = CONFIG.getProperty("NavigationBy");
			PremiumFlag = false;
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcRewind());

			TestUtil.reportStatus("<b> -----------------------Rewind flow is started---------------------- </b>", "Info", false);

			customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
			customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			if(TestBase.businessEvent.equalsIgnoreCase("MTA") ){
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status Endorsement Submitted (Rewind) function is having issue(S) . ");
			}else{
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
			}
			customAssert.assertTrue(common_POB.funcPolicyDetails(common.Rewind_excel_data_map), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"),"Issue while Navigating to Previous Claims  . ");
			customAssert.assertTrue(common_CCF.funcPreviousClaims(common.Rewind_excel_data_map), "Previous claim function is having issue(S) .");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcCovers(common.Rewind_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified Perils"),"Issue while Navigating to Specified Perils  . ");
			customAssert.assertTrue(common.funcSpecifiedPerils(common.Rewind_excel_data_map), "Select covers function is having issue(S) . ");
			if(((String)common.Rewind_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(funcInsuredProperties(common.Rewind_excel_data_map), "Insured Property function is having issue(S) . ");
			}

			if(((String)common.Rewind_excel_data_map.get("CD_Liability")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(common_CCF.funcEmployersLiability(common.Rewind_excel_data_map), "Employers Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability"),"Issue while Navigating to Property Owners Liability  . ");
				customAssert.assertTrue(funcPropertyOwnersLiability(common.Rewind_excel_data_map), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability Information"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(funcLiabilityInformation(common.Rewind_excel_data_map), "Liability Information function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(common_CCF.funcTerrorism(common.Rewind_excel_data_map), "Terrorism function is having issue(S) . ");
			}
//			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
			//			customAssert.assertTrue(common_HHAZ.funcEndorsementOperations(common.Rewind_excel_data_map),"Insurance tax adjustment is having issue(S).");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
				common_POB.isInsuranceTaxDone = false;
				Assert.assertTrue(common_POB.funcPremiumSummary_MTA(common.Rewind_excel_data_map,code,event));
			}else{
				Assert.assertTrue(common_POB.funcPremiumSummary(common.Rewind_excel_data_map,code,event));
				Assert.assertTrue(common_PEN.funcStatusHandling(common.Rewind_excel_data_map,code,event));
			}


		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}



	}


	public void CancellationFlow(String code,String event) throws ErrorInTestMethod{

		String testName = (String)common.CAN_excel_data_map.get("Automation Key");
		try{
			NewBusinessFlow(code,"NB");
			//		System.out.println("Test method of CAN For - "+code);
			//			System.out.println("CAN data Map - "+common.CAN_excel_data_map);
			//			System.out.println("CAN inner data Map - "+common.CAN_Structure_of_InnerPagesMaps);
			// 
			customAssert.assertTrue(common_POB.CancellationProcess(common.NB_excel_data_map,code,event),"Failed in Cancellation Process Function");
			common.currentRunningFlow = "CAN";
			TestUtil.reportStatus("Test Method of CAN For - "+code, "Pass", true);
			Assert.assertTrue(common_PEN.funcStatusHandling(common.CAN_excel_data_map,code,event));
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


	}


	public void RenewalFlow(String code,String event){
		String testName = (String)common.Renewal_excel_data_map.get("Automation Key");
		common.currentRunningFlow = "Renewal";
		try{
			
			customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
			customAssert.assertTrue(renewalSearchPolicyNEW(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Pending"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
			customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
			customAssert.assertTrue(common.funcAssignUnderWriter((String)common.Renewal_excel_data_map.get("Renewal_AssignUnderwritter")));
			customAssert.assertTrue(common.funcButtonSelection("Send to Underwriter"));
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
			customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Submitted"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
			
			customAssert.assertTrue(common_POB.funcPolicyDetails(common.Renewal_excel_data_map), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"),"Issue while Navigating to Previous Claims  . ");
			customAssert.assertTrue(common_CCF.funcPreviousClaims(common.Renewal_excel_data_map), "Previous claim function is having issue(S) .");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcCovers(common.Renewal_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified Perils"),"Issue while Navigating to Specified Perils  . ");
			customAssert.assertTrue(common.funcSpecifiedPerils(common.Renewal_excel_data_map), "Select covers function is having issue(S) . ");
			
			if(((String)common.Renewal_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(common_POB.funcInsuredProperties(common.Renewal_excel_data_map), "Insured Property function is having issue(S) . ");
			}
			
			if(((String)common.Renewal_excel_data_map.get("CD_Liability")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(common_CCF.funcEmployersLiability(common.Renewal_excel_data_map), "Employers Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability"),"Issue while Navigating to ELD Information  . ");
				customAssert.assertTrue(common_POB.funcPropertyOwnersLiability(common.Renewal_excel_data_map), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability Information"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(common_POB.funcLiabilityInformation(common.Renewal_excel_data_map), "Liability Information function is having issue(S) . ");
				}
			if(((String)common.Renewal_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(common_CCF.funcTerrorism(common.Renewal_excel_data_map), "Terrorism function is having issue(S) . ");
				}
			
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			Assert.assertTrue(common_POB.funcPremiumSummary_MTA(common.Renewal_excel_data_map,code,event));
			Assert.assertTrue(common_PEN.funcStatusHandling(common.Renewal_excel_data_map,code,event));
			
			customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
			customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
			
			TestUtil.reportTestCasePassed(testName);
		
		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
		}
		
	}
	
	public void RenewalRewindFlow(String code,String event) throws ErrorInTestMethod{
		String testName = (String)common.Rewind_excel_data_map.get("Automation Key");
		try{
			
			common.currentRunningFlow="Rewind";
			common.CoversDetails_data_list.clear();
			String navigationBy = CONFIG.getProperty("NavigationBy");
			common_POB.PremiumFlag = false;
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcRewind());
			
			TestUtil.reportStatus("<b> -----------------------Renewal Rewind flow is started---------------------- </b>", "Info", false);
			
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
			customAssert.assertTrue(common.funcSearchPolicy(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Renewal Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
			
			customAssert.assertTrue(common_POB.funcPolicyDetails(common.Rewind_excel_data_map), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"),"Issue while Navigating to Previous Claims  . ");
			customAssert.assertTrue(common_CCF.funcPreviousClaims(common.Rewind_excel_data_map), "Previous claim function is having issue(S) .");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcCovers(common.Rewind_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified Perils"),"Issue while Navigating to Specified Perils  . ");
			customAssert.assertTrue(common.funcSpecifiedPerils(common.Rewind_excel_data_map), "Select covers function is having issue(S) . ");
			
			if(((String)common.Rewind_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(common_POB.funcInsuredProperties(common.Rewind_excel_data_map), "Insured Property function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_Liability")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(common_CCF.funcEmployersLiability(common.Rewind_excel_data_map), "Employers Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability"),"Issue while Navigating to Property Owners Liability  . ");
				customAssert.assertTrue(common_POB.funcPropertyOwnersLiability(common.Rewind_excel_data_map), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability Information"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(common_POB.funcLiabilityInformation(common.Rewind_excel_data_map), "Liability Information function is having issue(S) . ");
				}
			if(((String)common.Rewind_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(common_CCF.funcTerrorism(common.Rewind_excel_data_map), "Terrorism function is having issue(S) . ");
				}
			
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			Assert.assertTrue(common_POB.funcPremiumSummary_MTA(common.Rewind_excel_data_map,code,event));
			
		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
		
		
		
	}
	
	public boolean funcPolicyDetails(Map<Object, Object> map_data){
		String event1=null;

		if(common.currentRunningFlow.equals("NB")){
			event1=common.currentRunningFlow;
		}else if(common.currentRunningFlow.equals("MTA")) {
			event1=common.currentRunningFlow;
		}else if(common.currentRunningFlow.equals("Renewal")) {
			event1=common.currentRunningFlow;
		}else if(common.currentRunningFlow.equals("Rewind")) {
			event1=common.currentRunningFlow;
		}

		boolean retvalue = true;
		try{
			customAssert.assertTrue(common.funcPageNavigation("Policy Details", ""), "Navigation problem to Policy Details page .");
			if(common.currentRunningFlow.equalsIgnoreCase("NB")){
				customAssert.assertTrue(k.Input("CCF_PD_ProposerName", (String)map_data.get("NB_ClientName")),	"Unable to enter value in Proposer Name  field .");
			}else{
				//	customAssert.assertTrue(k.Input("CCF_PD_ProposerName", (String)map_data.get("PD_ProposerName")),	"Unable to enter value in Proposer Name  field .");
			}
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_PD_ProposerName", "value"),"Proposer Name Field Should Contain Valid Name  .");
			customAssert.assertTrue(k.Input("CCF_CC_TradingName", (String)map_data.get("PD_TradingName")),	"Unable to enter value in Trading Name  field .");
			customAssert.assertTrue(k.Input("CCF_PD_BusinessDesc", (String)map_data.get("PD_BusinessDesc")),	"Unable to enter value in Business Desc  field .");

			customAssert.assertTrue(k.Input("CCF_PD_DateEstablishment", (String)map_data.get("PD_DateEstablishment")),	"Unable to enter value in Date Establishment  field .");

			if(!common.currentRunningFlow.equals("MTA") && !common.currentRunningFlow.equals("Rewind") ){
				customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_1QS", (String)map_data.get("PD_1QS")), "Unable to Select 1QS radio button on Policy Details Page.");	
				customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_NewVenture", (String)map_data.get("PD_NewVenture")), "Unable to Select New Venture radio button on Policy Details Page.");
				customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_Prospect", (String)map_data.get("PD_Prospect")), "Unable to Select Prospect radio button on Policy Details Page.");
				customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CrossSell", (String)map_data.get("PD_CrossSell")), "Unable to Select CrossSell radio button on Policy Details Page.");
			}
			customAssert.assertTrue(k.Input("CCF_Address_CC_Address", (String) map_data.get("PD_Address")),"Unable to enter value in Address field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"Address Field Should Contain Valid Address  .");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line2", (String) map_data.get("PD_Line1")),"Unable to enter value in Address field line 1 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line3", (String) map_data.get("PD_Line2")),"Unable to enter value in Address field line 2 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Town", (String) map_data.get("PD_Town")),"Unable to enter value in Town field . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_County", (String) map_data.get("PD_County")),"Unable to enter value in County  . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode", (String)map_data.get("PD_Postcode")),"Unable to enter value in PostCode");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Postcode", "value"),"PostCode Field Should Contain Valid Postcode  .");
			customAssert.assertTrue(common.validatePostCode((String)map_data.get("PD_Postcode")),"Post Code is not in Correct format .");
			if(common.currentRunningFlow.equalsIgnoreCase("NB")){
				customAssert.assertTrue(k.Click("inception_date"), "Unable to Click inception date.");
				customAssert.assertTrue(k.Input("inception_date", (String)map_data.get("QC_InceptionDate")),"Unable to Enter inception date.");
				customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
				customAssert.assertTrue(!k.getAttributeIsEmpty("inception_date", "value"),"Inception Date Field Should Contain Valid value  .");
				customAssert.assertTrue(k.Click("deadline_date"), "Unable to Click deadline date.");
				customAssert.assertTrue(k.Input("deadline_date", (String)map_data.get("QC_DeadlineDate")),"Unable to Enter deadline date.");
				customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
				customAssert.assertTrue(!k.getAttributeIsEmpty("deadline_date", "value"),"Deadline date Field Should Contain Valid value  .");
				customAssert.assertTrue(k.Input("CCF_QC_TargetPemium", (String) map_data.get("QC_TargetPemium")),"Unable to enter value in Target Pemium field. ");
				customAssert.assertTrue(k.Input("CCF_PD_PreviousPremium", (String) map_data.get("PD_PreviousPremium")),"Unable to enter value in Previous Premium field. ");
				k.waitTwoSeconds();
				customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_HoldingBroker", (String)map_data.get("PD_HoldingBroker")), "Unable to Select Holding Broker radio button on Policy Details Page.");
				if(map_data.get("PD_HoldingBroker").equals("No")){
					//customAssert.assertTrue(k.Input("CCF_PD_HoldingBrokerInfo", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
					customAssert.assertTrue(k.Input("CCF_PD_HoldingBrokerInfo", (String) map_data.get("PD_HoldingBrokerInfo")),"Unable to enter value in HoldingBrokerInfo field. ");
				}
			}
			//customAssert.assertTrue(k.Input("CCF_PD_PreviousPremium", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
			//	customAssert.assertTrue(k.Input("CCF_PD_PreviousPremium", (String) map_data.get("PD_PreviousPremium")),"Unable to enter value in Previous Premium field. ");
			//customAssert.assertTrue(k.Input("CCF_QC_TargetPemium", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");

			customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_TaxExempt", (String)map_data.get("PD_TaxExempt")), "Unable to Select TaxExempt radio button on Policy Details Page.");

			//customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CarrierOverride", (String)map_data.get("PD_CarrierOverride")), "Unable to Select Carrier Override radio button on Policy Details Page.");

			/*if(map_data.get("PD_CarrierOverride").equals("Yes")){
				customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CO_RefferedToHead", (String)map_data.get("PD_CO_RefferedToHead")), "Unable to Select Reffered To Head radio button on Policy Details Page.");
			}*/
			k.waitTwoSeconds();

			//TradeCode Selection & Verification
			if(((String)map_data.get("PD_TCS_TradeCode_Button")).equalsIgnoreCase("Yes")){
				customAssert.assertTrue(common.tradeCodeSelection((String)map_data.get("PD_TCS_TradeCode") ,"Policy Details" , 0),"Trade code selection function is having issue(S).");

			}

			switch (common.product) {
			case "POC":
				customAssert.assertTrue(k.SelectRadioBtn("POC_PD_HazardGroup", (String)map_data.get("PD_HazardGroup")), "Unable to Select  Hazard Group radio button on Policy Details Page.");
				switch ((String)map_data.get("PD_HazardGroup")) {
				case "Yes":
					customAssert.assertTrue(k.Input("POC_PD_NewHazardGroupValue", (String) map_data.get("PD_NewHazardGroupValue")),"Unable to enter value in  Hazard Group value field. ");
					customAssert.assertTrue(k.Input("POC_PD_HazardGroupOverrideReason", (String) map_data.get("PD_HazardGroupOverrideReason")),"Unable to enter value in Hazard Group Override Reason. ");
					break;
				}
			}

			TestUtil.reportStatus("Entered all the details on Policy Details page .", "Info", true);

			return retvalue;

		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
			Assert.fail("Unable to to do operation on policy details page. \n", t);
			return false;
		}
	}

	public boolean funcInsuredProperties(Map<Object, Object> map_data){

		boolean r_Value = true;
		try{
			customAssert.assertTrue(common.funcPageNavigation("Insured Properties", ""),"Insured Properties page is having issue(S)");

			customAssert.assertTrue(k.Input("CCF_IP_AnyOneEvent", Keys.chord(Keys.CONTROL, "a")),"Unable to select any one Event field");
			customAssert.assertTrue(k.Input("CCF_IP_AnyOneEvent", (String)map_data.get("IP_AnyOneEvent")),	"Unable to enter value in any one Event field .");
			customAssert.assertTrue(k.Input("IP_Landslip", Keys.chord(Keys.CONTROL, "a")),"Unable to select Subsidence Ground Heave or Landslip field");
			customAssert.assertTrue(k.Input("IP_Landslip", (String)map_data.get("IP_Landslip")),"Unable to enter value in Subsidence Ground Heave or Landslip field .");

			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");

			int count = 0;
			int noOfProperties = 0;
			if(common.no_of_inner_data_sets.get("Property Details")==null){
				noOfProperties = 0;
			}else{
				noOfProperties = common.no_of_inner_data_sets.get("Property Details");
			}
			while(count < noOfProperties ){

				customAssert.assertTrue(k.Click("CCF_Btn_AddProperty"), "Unable to click Add Property Button on Insured Properties .");
				customAssert.assertTrue(addProperty(map_data,count),"Error while adding insured proprty  .");
				TestUtil.reportStatus("Insured Property  <b>[  "+common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("Automation Key")+"  ]</b>  added successfully . ", "Info", true);
				customAssert.assertTrue(k.Click("CCF_Btn_Back"), "Unable to click on Back Button on Property Details .");
				count++;
			}

			TestUtil.reportStatus("All the specified Insured properties added and verified successfully . ", "Info", true);

			return r_Value;


		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
			Assert.fail("Insured Properties function is having issue(S). \n", t);
			return false;
		}
	}

	public boolean addProperty(Map<Object, Object> map_data,int count){

		boolean r_value=true;

		Map<String, List<Map<String, String>>> internal_data_map = new HashMap<>();
		//Map<Object, Object> event_data_map = 

		switch(common.currentRunningFlow){

		case "NB":
			internal_data_map=common.NB_Structure_of_InnerPagesMaps;
			break;

		case "MTA":
			internal_data_map= common.MTA_Structure_of_InnerPagesMaps;
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

			customAssert.assertTrue(common.funcPageNavigation("Property Details", ""),"Property Details page navigation issue(S)");
			if(!(internal_data_map.get("Property Details").get(count).get("PoD_CopyAddress")).equalsIgnoreCase("Yes")){
				customAssert.assertTrue(k.Input("CCF_Address_CC_Address", internal_data_map.get("Property Details").get(count).get("PoD_Address")),"Unable to enter value in Address field. ");
				customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"Address Field Should Contain Valid Value on Client Details .");
				customAssert.assertTrue(k.Input("CCF_Address_CC_line2", internal_data_map.get("Property Details").get(count).get("PoD_AddressL2")),"Unable to enter value in Address field line 2 . ");
				customAssert.assertTrue(k.Input("CCF_Address_CC_line3", internal_data_map.get("Property Details").get(count).get("PoD_AddressL3")),"Unable to enter value in Address field line 3 . ");
				customAssert.assertTrue(k.Input("CCF_Address_CC_Town", internal_data_map.get("Property Details").get(count).get("PoD_Town")),"Unable to enter value in Town field . ");
				customAssert.assertTrue(k.Input("CCF_Address_CC_County", internal_data_map.get("Property Details").get(count).get("PoD_County")),"Unable to enter value in County  . ");
				customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode", internal_data_map.get("Property Details").get(count).get("PoD_Postcode")),"Unable to enter value in PostCode field .");
				customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Postcode", "value"),"Postcode Field Should Contain Valid Value on Client Details .");
				customAssert.assertTrue(common.validatePostCode(internal_data_map.get("Property Details").get(count).get("PoD_Postcode")),"Post Code is not in Correct format .");
			}else{
				customAssert.assertTrue(k.Click("CCF_Btn_CopyCorAddress"));
			}

			customAssert.assertTrue(k.Input("CCF_PoD_PropertyAge", internal_data_map.get("Property Details").get(count).get("PoD_PropertyAge")),"Unable to enter value in Age of Property (years) . ");
			customAssert.assertTrue(k.DropDownSelection("CCF_PoD_TerrorismZone", internal_data_map.get("Property Details").get(count).get("PoD_TerrorismZone")), "Unable to select value from Terrorism Zone dropdown .");

			//Statement of Fact
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q1", internal_data_map.get("Property Details").get(count).get("PoD_SOF_Q1")), "Unable to Select first SOF radio button on Policy Details Page.");
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q2", internal_data_map.get("Property Details").get(count).get("PoD_SOF_Q2")), "Unable to Select second SOF radio button on Policy Details Page.");
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q4", internal_data_map.get("Property Details").get(count).get("PoD_SOF_Q4")), "Unable to Select fourth SOF radio button on Policy Details Page.");

			//Property Certificate
			customAssert.assertTrue(k.SelectRadioBtn("POB_PoD_PC_Q1", internal_data_map.get("Property Details").get(count).get("PoD_SOF_Q1")), "Unable to Select first SOF radio button on Policy Details Page.");
			if((internal_data_map.get("Property Details").get(count).get("PoD_SOF_Q1")).equalsIgnoreCase("True")){
				customAssert.assertTrue(k.Input("POB_PoD_PC_Occupancy", internal_data_map.get("Property Details").get(count).get("PoD_PC_Occupancy")),"Unable to enter value in Property Certificate Occupancy  . ");
			}
			customAssert.assertTrue(k.Input("POB_PoD_PC_Premium", internal_data_map.get("Property Details").get(count).get("PoD_PC_Premium")),"Unable to enter value in Property Certificate Premium  . ");
			customAssert.assertTrue(k.Input("POB_PoD_PC_IPT", internal_data_map.get("Property Details").get(count).get("PoD_PC_IPT")),"Unable to enter value in Property Certificate IPT  . ");
			customAssert.assertTrue(k.Input("POB_PoD_PC_TotalPremium", internal_data_map.get("Property Details").get(count).get("PoD_PC_TotalPremium")),"Unable to enter value in Property Certificate Total Premium  . ");


			//Proximity
			customAssert.assertTrue(k.Input("CCF_PoD_ProximityDescription", internal_data_map.get("Property Details").get(count).get("PoD_ProximityDescription")),"Unable to enter value in Proximity description . ");

			//Trade Code
			if((internal_data_map.get("Property Details").get(count).get("PoD_TCS_TradeCode_Button")).equalsIgnoreCase("Yes")){
				customAssert.assertTrue(common.tradeCodeSelection((String)internal_data_map.get("Property Details").get(count).get("PoD_MD_TCS_TradeCode"),"Property Details" , count),"Trade code selection function is having issue(S).");	
			}

			//EML
			customAssert.assertTrue(k.Input("CCF_PoD_EmlAmount_GBP", internal_data_map.get("Property Details").get(count).get("PoD_EmlAmount_GBP")),"Unable to enter value in EmlAmount_GBP . ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_PoD_EmlAmount_GBP", "value"),"Eml amount (GBP) Field Should Contain Valid Value on Property Details .");
			customAssert.assertTrue(k.Input("CCF_PoD_EmlAmount_Percent", internal_data_map.get("Property Details").get(count).get("PoD_EmlAmount_Percent")),"Unable to enter value in Eml amount (%) . ");

			//Inner MD-Bespoke Sum Insured
			//customAssert.assertTrue(addMD_BIBespokeSumInsured(map_data), "Error while adding Bespoke data . ");
			List<WebElement> bespoke_MD_btns = k.getWebElements("CCF_Btn_AddBespokeSumIns");
			WebElement MD_bespoke_btn = bespoke_MD_btns.get(0);
			MD_bespoke_btn.click();
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", internal_data_map.get("Property Details").get(count).get("BSI_MD_Description")),"Unable to enter value in Bespoke Description . ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", internal_data_map.get("Property Details").get(count).get("BSI_MD_DeclaredValue")),"Unable to enter value in Bespoke Sum Insured . ");
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Bespoke Sum Insured inner page .");

			if(((String)map_data.get("CD_LossOfRentalIncome")).equalsIgnoreCase("Yes") || 
					((String)map_data.get("CD_Add_LossOfRentalIncome")).equalsIgnoreCase("Yes")){
				List<WebElement> bespoke_BI_btns = k.getWebElements("CCF_Btn_AddBespokeSumIns");
				WebElement BI_bespoke_btn = bespoke_BI_btns.get(1);
				BI_bespoke_btn.click();
				customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", internal_data_map.get("Property Details").get(count).get("BSI_LOI_Description")),"Unable to enter value in BI Bespoke Description . ");
				customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", internal_data_map.get("Property Details").get(count).get("BSI_LOI_DeclaredValue")),"Unable to enter value in Bespoke Sum Insured . ");
				customAssert.assertTrue(k.DropDownSelection("CCF_BSI_BI_IndemnityPeriod", internal_data_map.get("Property Details").get(count).get("BSI_LOI_IndemnityPeriod")), "Unable to select value from Indemnity Period dropdown .");
				customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Bespoke Sum Insured inner page .");

			}
			int tableIndex =0; 

			tableIndex = k.getTableIndex("Declared Value","xpath"," html/body/div[3]/form/div/table ");
			customAssert.assertTrue(inputTableData(count, "MD", tableIndex));
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
			customAssert.assertTrue(calculatePremium(count, "MD", tableIndex));
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_MaterialDamage_NP", Double.toString(MD_TotalPremium), map_data);

			if(((String)map_data.get("CD_LossOfRentalIncome")).equalsIgnoreCase("Yes") ||
					((String)map_data.get("CD_Add_LossOfRentalIncome")).equalsIgnoreCase("Yes")){
				tableIndex = k.getTableIndex("Declaration Uplift (%)","xpath"," html/body/div[3]/form/div/table ");
				customAssert.assertTrue(inputTableData(count, "LOI", tableIndex));
				customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
				customAssert.assertTrue(calculatePremium(count, "LOI", tableIndex));
				TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_LossOfRentalIncome_NP", Double.toString(LOI_TotalPremium), map_data);
			}
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");

		}catch(Throwable t){
			return false;
		}



		return r_value;
	}

	public boolean inputTableData(int count , String coverInitial , int tableIndex) {


		List<WebElement> listOfRows = driver.findElements(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr"));
		int innerBeSpokeCount = 1;//common.no_of_inner_data_sets("Be Spkoe column name"); ---> this can be used in case of Multiple Bespoke item added.
		for(int i=0;i<(innerBeSpokeCount+listOfRows.size()-1)-1;i++){
			String Abvr = "";
			String sectionName = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[1]")).getText();
			if(sectionName.equalsIgnoreCase(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("BSI_MD_Description"))){
				Abvr = "BSI_MD_";
			}else if(sectionName.equalsIgnoreCase(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("BSI_LOI_Description"))){
				Abvr = "BSI_LOI_";
			}else{
				Abvr = CommonFunction.func_GetAbrrivation(coverInitial, sectionName);
				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[2]/input" ,common.NB_Structure_of_InnerPagesMaps , "Property Details", count , Abvr , "DeclaredValue", sectionName,"Input"),"BAC");
			}
			if(coverInitial.equalsIgnoreCase("MD")){

				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[4]/input" , common.NB_Structure_of_InnerPagesMaps , "Property Details", count , Abvr , "FireRate",sectionName, "Input"),"BAC");
				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[5]/input" , common.NB_Structure_of_InnerPagesMaps , "Property Details", count , Abvr , "PerilsRate",sectionName, "Input"),"BAC");
				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[6]/input" , common.NB_Structure_of_InnerPagesMaps , "Property Details", count , Abvr , "SprinkRate",sectionName, "Input"),"BAC");
				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[7]/input" , common.NB_Structure_of_InnerPagesMaps , "Property Details", count , Abvr , "ADRate", sectionName,"Input"),"BAC");
				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[8]/input" , common.NB_Structure_of_InnerPagesMaps , "Property Details", count , Abvr , "SubsRate",sectionName, "Input"),"BAC");
				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[10]/input", common.NB_Structure_of_InnerPagesMaps , "Property Details", count , Abvr , "TechAdjust",sectionName, "Input"),"BAC");
				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[11]/input" , common.NB_Structure_of_InnerPagesMaps , "Property Details", count , Abvr , "CommAdjust", sectionName,"Input"),"BAC");

			}
			if(coverInitial.equalsIgnoreCase("LOI")){

				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[5]/input" , common.NB_Structure_of_InnerPagesMaps , "Property Details", count , Abvr , "FireRate",sectionName, "Input"),"BAC");
				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[6]/input" , common.NB_Structure_of_InnerPagesMaps , "Property Details", count , Abvr , "PerilsRate",sectionName, "Input"),"BAC");
				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[7]/input" , common.NB_Structure_of_InnerPagesMaps , "Property Details", count , Abvr , "SprinkRate",sectionName, "Input"),"BAC");
				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[8]/input" , common.NB_Structure_of_InnerPagesMaps , "Property Details", count , Abvr , "ADRate", sectionName,"Input"),"BAC");
				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[9]/input" , common.NB_Structure_of_InnerPagesMaps , "Property Details", count , Abvr , "SubsRate",sectionName, "Input"),"BAC");
				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[11]/input", common.NB_Structure_of_InnerPagesMaps , "Property Details", count , Abvr , "TechAdjust",sectionName, "Input"),"BAC");
				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[12]/input" , common.NB_Structure_of_InnerPagesMaps , "Property Details", count , Abvr , "CommAdjust", sectionName,"Input"),"BAC");
			}
		}
		return true;
	}


	public boolean calculatePremium(int count , String coverInitial , int tableIndex) throws Throwable, Exception {

		List<WebElement> listOfRows = driver.findElements(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr"));
		double MD_Premium = 0.0 , LOI_Premium = 0.0;
		int innerBeSpokeCount = 1;//common.no_of_inner_data_sets("Be Spkoe column name"); ---> this can be used in case of Multiple Bespoke item added.
		for(int i=0;i<(innerBeSpokeCount+listOfRows.size()-1)-1;i++){
			String Abvr = "";
			JavascriptExecutor j_exe = (JavascriptExecutor) driver;
			j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[1]")));
			String sectionName = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[1]")).getText();
			if(sectionName.equalsIgnoreCase(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("BSI_MD_Description"))){
				Abvr = "BSI_MD_";
			}else if(sectionName.equalsIgnoreCase(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("BSI_LOI_Description"))){
				Abvr = "BSI_LOI_";
			}else{
				Abvr = CommonFunction.func_GetAbrrivation(coverInitial, sectionName);
			}
			if(coverInitial.equalsIgnoreCase("MD")){

				String actPremium = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[13]/input")).getAttribute("value");
				double totalRate = Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get(Abvr+"FireRate")) + 
						Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get(Abvr+"PerilsRate")) + 
						Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get(Abvr+"SprinkRate")) + 
						Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get(Abvr+"ADRate")) + 
						Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get(Abvr+"SubsRate"));
				double commAdjustment = ((totalRate * Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get(Abvr+"TechAdjust"))) / 100.0 ) + totalRate ; 
				double adjustedRate =  (commAdjustment * Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get(Abvr+"CommAdjust")) / 100.0 ) + commAdjustment;
				String expPremium = common.roundedOff(Double.toString(((Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get(Abvr+"DeclaredValue")) * adjustedRate ) / 100.0)));
				customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expPremium),Double.parseDouble(actPremium),"MD premium"),"Mismatched MD premium Values");
				MD_Premium = MD_Premium + Double.parseDouble(expPremium);

				if(driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+2)+"]/td[12]")).getText().equalsIgnoreCase("Total")){
					String actTotalPremium = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+2)+"]/td[13]/input")).getAttribute("value");
					String expTotalPremium = Double.toString(MD_Premium);
					customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expTotalPremium),Double.parseDouble(actTotalPremium),"MD premium"),"Mismatched MD premium Values");
					TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Property Details", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("Automation Key"), "MD_TotalPremium", Double.toString(MD_Premium),common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count));
					MD_TotalPremium = MD_TotalPremium + MD_Premium;
				}

			}
			if(coverInitial.equalsIgnoreCase("LOI")){

				String actPremium = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[14]/input")).getAttribute("value");
				double totalRate = Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get(Abvr+"FireRate")) + 
						Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get(Abvr+"PerilsRate")) + 
						Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get(Abvr+"SprinkRate")) + 
						Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get(Abvr+"ADRate")) + 
						Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get(Abvr+"SubsRate"));
				double commAdjustment = ((totalRate * Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get(Abvr+"TechAdjust"))) / 100.0 ) + totalRate ; 
				double adjustedRate =  (commAdjustment * Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get(Abvr+"CommAdjust")) / 100.0 ) + commAdjustment;
				String expPremium = common.roundedOff(Double.toString(((Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get(Abvr+"DeclaredValue")) * adjustedRate ) / 100.0)));
				customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expPremium),Double.parseDouble(actPremium),"MD premium"),"Mismatched MD premium Values");
				LOI_Premium = LOI_Premium + Double.parseDouble(expPremium);

				if(driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+2)+"]/td[13]")).getText().equalsIgnoreCase("Total")){
					String actTotalPremium = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+2)+"]/td[14]/input")).getAttribute("value");
					String expTotalPremium = Double.toString(LOI_Premium);
					customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expTotalPremium),Double.parseDouble(actTotalPremium),"LOI premium"),"Mismatched LOI premium Values");
					TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Property Details", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("Automation Key"), "LOI_TotalPremium", Double.toString(LOI_Premium),common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count));
					LOI_TotalPremium = LOI_TotalPremium + LOI_Premium;
				}
			}
		}
		return true;
	}


	public boolean addMD_BIBespokeSumInsured(Map<Object, Object> map_data){
		boolean r_value=true;

		try{

			int total_count_MD_bespoke = common.no_of_inner_data_sets.get("BS Insured MD");
			int count=0;
			while(count < total_count_MD_bespoke){
				List<WebElement> bespoke_btns = k.getWebElements("CCF_Btn_AddBespokeSumIns");
				System.out.println(bespoke_btns.size());
				WebElement MD_bespoke_btn = bespoke_btns.get(0);
				MD_bespoke_btn.click();
				k.waitTwoSeconds();

				customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", common.NB_Structure_of_InnerPagesMaps.get("BS Insured MD").get(count).get("BSI_MD_Description")),"Unable to enter value in Bespoke Description . ");
				customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", Keys.chord(Keys.CONTROL, "a")),"Unable to select Bespoke Sum Insured field");
				customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", common.NB_Structure_of_InnerPagesMaps.get("BS Insured MD").get(count).get("BSI_MD_SumInsured")),"Unable to enter value in Bespoke Sum Insured . ");

				customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Bespoke Sum Insured inner page .");
				count++;
			}

			if(((String)map_data.get("CD_LossOfRentalIncome")).equals("Yes")){
				int total_count_BI_bespoke = common.no_of_inner_data_sets.get("BS Insured LOI");
				count=0;
				while(count < total_count_BI_bespoke){
					k.Click("CCF_Btn_BI_Bespoke");
					k.waitTwoSeconds();

					customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", common.NB_Structure_of_InnerPagesMaps.get("BS Insured LOI").get(count).get("BSI_LOI_Description")),"Unable to enter value in BI Bespoke Description . ");
					customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", Keys.chord(Keys.CONTROL, "a")),"Unable to select Bespoke Sum Insured field");
					customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", common.NB_Structure_of_InnerPagesMaps.get("BS Insured LOI").get(count).get("BSI_LOI_SumInsured")),"Unable to enter value in Bespoke Sum Insured . ");
					customAssert.assertTrue(k.DropDownSelection("CCF_BSI_BI_IndemnityPeriod", common.NB_Structure_of_InnerPagesMaps.get("BS Insured LOI").get(count).get("BSI_LOI_IndemnityPeriod")), "Unable to select value from Indemnity Period dropdown .");
					customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Bespoke Sum Insured inner page .");
					count++;
				}
			}


		}catch(Throwable t){

			r_value=false;
		}

		return r_value;

	}


	public boolean funcPropertyOwnersLiability(Map<Object, Object> map_data){

		boolean r_value= true;

		try{
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


			customAssert.assertTrue(common.funcPageNavigation("Property Owners Liability", ""),"Employers Liability page navigations issue(S)");
			if(!common.currentRunningFlow.equalsIgnoreCase("NB")){

				customAssert.assertTrue(common.deleteItems(),"Delete Items function is having issues.");
			}

			customAssert.assertTrue(k.Input("POB_POL_LiabilityLimit", (String)map_data.get("POL_IndemnityLimit")),"Unable to enter value in Property Owners Liability Limit . ");
			customAssert.assertTrue(k.Input("POB_POL_LiabilityExcess", (String)map_data.get("POL_LiabilityExcess")),"Unable to enter value in Property Owners Liability Excess . ");

			//Inner BI-Specified Suppliers
			customAssert.assertTrue(addPOLItems(), "Error while adding PL Items . ");

			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Public Liability .");

			double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "POL", "Property Owners Liability", "POL AddItem");
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_PropertyOwnersLiability_NP", Double.toString(sPremiumm), map_data);


			TestUtil.reportStatus("Property Owners Liability details are filled sucessfully . ", "Info", true);

		}catch(Throwable t){
			return false;

		}

		return r_value;
	}

	public boolean addPOLItems(){
		boolean r_value=true;

		try{

			int total_count_PL_items = common.no_of_inner_data_sets.get("POL AddItem");
			int count=0;
			while(count < total_count_PL_items){

				customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on PL page .");
				k.waitTwoSeconds();

				customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", common.NB_Structure_of_InnerPagesMaps.get("POL AddItem").get(count).get("AD_POL_ItemDesc")),"Unable to enter value in Description field. ");
				customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", Keys.chord(Keys.CONTROL, "a")),"Unable to select Sum Insured field");
				customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", common.NB_Structure_of_InnerPagesMaps.get("POL AddItem").get(count).get("AD_POL_ItemSumIns")),"Unable to enter value in Sum Insured field. ");

				customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Specified Customer inner page .");
				count++;
			}

		}catch(Throwable t){

			r_value=false;
		}

		return r_value;

	}

	public boolean funcLiabilityInformation(Map<Object, Object> map_data){

		boolean r_value= true;

		try{

			customAssert.assertTrue(common.funcPageNavigation("Property Owners Liability Information", ""),"Liability Information page navigations issue(S)");

			//Statement of Fact
			customAssert.assertTrue(k.SelectRadioBtn("POB_POLI_SOF_Q1", (String)map_data.get("LI_SOF_Q1")), "Unable to Select Liability Information MF Q1 radio button .");
			customAssert.assertTrue(k.SelectRadioBtn("POB_POLI_SOF_Q2", (String)map_data.get("LI_SOF_Q2")), "Unable to Select Liability Information MF Q2 radio button .");
			customAssert.assertTrue(k.SelectRadioBtn("POB_POLI_SOF_Q3", (String)map_data.get("LI_SOF_Q3")), "Unable to Select Liability Information MF Q3 radio button .");

			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");

			TestUtil.reportStatus("Liability Information details are filled sucessfully . ", "Info", true);

		}catch(Throwable t){
			return false;

		}

		return r_value;
	}

	public boolean funcRewindOperation(){

		boolean r_value= true;

		try{

			if(((String)common.NB_excel_data_map.get("CD_Add_Remove_Cover")).equalsIgnoreCase("Yes")){
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
				customAssert.assertTrue(common.funcRewindCoversCheck(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
				if(((String)common.NB_excel_data_map.get("CD_Add_MaterialDamage")).equals("Yes") &&
						((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equals("No")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
					customAssert.assertTrue(funcInsuredProperties(common.NB_excel_data_map), "Insured Property function is having issue(S) . ");
				}

				if(((String)common.NB_excel_data_map.get("CD_Add_Liability")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_Liability")).equals("No")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
					customAssert.assertTrue(common_CCF.funcEmployersLiability(common.NB_excel_data_map), "Employers Liability function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
					customAssert.assertTrue(common_CCF.funcELDInformation(common.NB_excel_data_map), "ELD Information function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability"),"Issue while Navigating to Property Owners Liability  . ");
					customAssert.assertTrue(funcPropertyOwnersLiability(common.NB_excel_data_map), "Public Liability function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability Information"),"Issue while Navigating to Products Liability  . ");
					customAssert.assertTrue(funcLiabilityInformation(common.NB_excel_data_map), "Liability Information function is having issue(S) . ");
				}
				if(((String)common.NB_excel_data_map.get("CD_Add_CyberandDataSecurity")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_CyberandDataSecurity")).equals("No")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
					customAssert.assertTrue(common_CCF.funcCyberAndDataSecurity(common.NB_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
				}
				if(((String)common.NB_excel_data_map.get("CD_Add_Terrorism")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("No")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
					customAssert.assertTrue(common_CCF.funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
				}

				if(((String)common.NB_excel_data_map.get("CD_Add_LegalExpenses")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_LegalExpenses")).equals("No")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
					customAssert.assertTrue(common_CCF.funcLegalExpenses(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Legal Expenses function is having issue(S) . ");
				}

				customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
				Assert.assertTrue(common.funcPremiumSummary(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"RewindAddCover"));

				customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
				customAssert.assertTrue(common.funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
			}

		}catch(Throwable t){
			return false;

		}

		return r_value;
	}



	public boolean renewalSearchPolicyNEW(Map<Object,Object> map_data){
		
		boolean retvalue = true;
	   
		k.ImplicitWaitOff();
		customAssert.assertTrue(k.DropDownSelection("Renewal_SearchType", TestBase.businessEvent), "Unable to select Type on search page for Renewal Policies.");
		customAssert.assertTrue(k.DropDownSelection("Renewal_SearchStatus", "Draft / Pending"), "Unable to select Status on search page for Renewal Policies.");
		customAssert.assertTrue(k.DropDownSelection("Renewal_SearchProduct", TestBase.product), "Unable to select Prouct Code on search page for Renewal Policies.");
		k.ImplicitWaitOn();
		customAssert.assertTrue(k.Click("comm_search"), "Unable to click on search button.");
		
		WebElement SearchedPolicyTable = driver.findElement(By.xpath("//*[@id='srch-update']//following::table[1]"));
		List<WebElement> rows = SearchedPolicyTable.findElements(By.tagName("tr"));
		
		if(rows.size()>0){
			String praposerName = SearchedPolicyTable.findElement(By.xpath("//tbody//tr[1]//td[1]/a[1]")).getText();
			String AgencyName = SearchedPolicyTable.findElement(By.xpath("//tbody//tr[1]//td[6]")).getText();
			String StartDate = SearchedPolicyTable.findElement(By.xpath("//tbody//tr[1]//td[8]")).getText();
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Renewal",(String)common.Renewal_excel_data_map.get("Automation Key"), "Renewal_ClientName", praposerName, common.Renewal_excel_data_map);
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Policy Details",(String)common.Renewal_excel_data_map.get("Automation Key"), "PD_ProposerName", praposerName, common.Renewal_excel_data_map);
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "QuoteCreation",(String)common.Renewal_excel_data_map.get("Automation Key"), "QC_AgencyName", AgencyName, common.Renewal_excel_data_map);
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.Renewal_excel_data_map.get("Automation Key"), "PS_PolicyStartDate", StartDate, common.Renewal_excel_data_map);
			
			SearchedPolicyTable.findElement(By.xpath("//tr[1]//td[1]/a[1]")).click();
			
			String PolicyNumber = k.getText("Header_Policy_Number");
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Renewal",(String)common.Renewal_excel_data_map.get("Automation Key"), "Renewal_PolicyNumber", PolicyNumber, common.Renewal_excel_data_map);
			String QuoteNumber = k.getText("POF_QuoteNumber");
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Renewal", (String)common.Renewal_excel_data_map.get("Automation Key"), "Renewal_QuoteNumber", QuoteNumber, common.Renewal_excel_data_map);
			
			
			TestUtil.reportStatus("Policy: "+(String)map_data.get("Renewal_PolicyNumber")+" successfully searched . ", "Info", true);
			return retvalue;
		}else{
			TestUtil.reportStatus("No Renewal policies are present with Status as <b> [ Draft / Pending ] </b> for product <b> [ "+TestBase.product+" ] </b>", "Fail", true);
			return false;
		}
		
		
	    
	}	
	public double calculateGeneralPremium(String grossPremium, String brokeCommision, String CommRateTotal){
		try{
			double grossCommRate = Double.parseDouble(CommRateTotal);
			if(common.currentRunningFlow.equalsIgnoreCase("CAN")){
				return -(Double.parseDouble(grossPremium )*((grossCommRate- Double.parseDouble(brokeCommision))/100));
			}else{
			return(Double.parseDouble(grossPremium )*((grossCommRate- Double.parseDouble(brokeCommision))/100));
			}
		
			
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Calculate General preimum for genral covers \n", t);
			return 0;
		}
	}
	public double calculateBrokeageAmountTSPOE(String recipient,Map<Object,Object> data_map,String account,int j, int td){
		double materialDamageFP = 0.00,  EmployersLiabiliyFP=0.00,  terrorismFP=0.00, generalPremium;
		double PropertyOwnersLiability=0.00, LossOfRentalIncomeFP=0.00;
		
		String part1= "//*[@id='table0']/tbody";
		String grossCommTotal = "25.00";
		try{
			String brokerageAccount= driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+td+"]")).getText();
			switch(common.currentRunningFlow){
			case "NB":
			case "Renewal":
				if(data_map.get("CD_MaterialDamage").equals("Yes")){
					String GrossPremium = (String)data_map.get("PS_MaterialDamage_GP");
					String Commission = (String)data_map.get("PS_MaterialDamage_CR"); 
					materialDamageFP = calculateGeneralPremium(GrossPremium, Commission, grossCommTotal);
				}
				if(data_map.get("CD_LossOfRentalIncome").equals("Yes")){
					String GrossPremium = (String)data_map.get("PS_LossOfRentalIncome_GP");
					String Commission = (String)data_map.get("PS_LossOfRentalIncome_CR"); 
					LossOfRentalIncomeFP = calculateGeneralPremium(GrossPremium, Commission, grossCommTotal);
				}
				if(data_map.get("CD_Liability").equals("Yes")){
					String GrossPremium = (String)data_map.get("PS_EmployersLiability_GP");
					String Commission = (String)data_map.get("PS_EmployersLiability_CR"); 
					EmployersLiabiliyFP = calculateGeneralPremium(GrossPremium, Commission, grossCommTotal);
					GrossPremium = (String)data_map.get("PS_PropertyOwnersLiability_GP");
					Commission = (String)data_map.get("PS_PropertyOwnersLiability_CR"); 
					PropertyOwnersLiability = calculateGeneralPremium(GrossPremium, Commission, grossCommTotal);
					
				}
				if(data_map.get("CD_Terrorism").equals("Yes")){
					String NetPremium = (String)data_map.get("PS_Terrorism_NP");
					String Commission = (String)data_map.get("PS_Terrorism_CR"); 
					terrorismFP =  Double.parseDouble(NetPremium) * 0.1; 
				}
				break;
			case "CAN":
				if(data_map.get("CD_MaterialDamage").equals("Yes")){
					String GrossPremium = (String)data_map.get("PS_MaterialDamage_GP");
					String Commission = (String)data_map.get("PS_MaterialDamage_CR"); 
					materialDamageFP = calculateGeneralPremium(GrossPremium, Commission, grossCommTotal);
				}
				if(data_map.get("CD_LossOfRentalIncome").equals("Yes")){
					String GrossPremium = (String)data_map.get("PS_LossOfRentalIncome_GP");
					String Commission = (String)data_map.get("PS_LossOfRentalIncome_CR"); 
					LossOfRentalIncomeFP = calculateGeneralPremium(GrossPremium, Commission, grossCommTotal);
				}
				if(data_map.get("CD_Liability").equals("Yes")){
					String GrossPremium = (String)data_map.get("PS_EmployersLiability_GP");
					String Commission = (String)data_map.get("PS_EmployersLiability_CR"); 
					EmployersLiabiliyFP = calculateGeneralPremium(GrossPremium, Commission, grossCommTotal);
					GrossPremium = (String)data_map.get("PS_PropertyOwnersLiability_GP");
					Commission = (String)data_map.get("PS_PropertyOwnersLiability_CR"); 
					PropertyOwnersLiability = calculateGeneralPremium(GrossPremium, Commission, grossCommTotal);
					
				}
				if(data_map.get("CD_Terrorism").equals("Yes")){
					String NetPremium = (String)data_map.get("PS_Terrorism_NP");
					String Commission = (String)data_map.get("PS_Terrorism_CR"); 
					terrorismFP =  -(Double.parseDouble(NetPremium) * 0.1); 
				}
				break;
			case "MTA":
			case "Rewind":
				if(TestBase.businessEvent.equalsIgnoreCase("MTA") || TestBase.businessEvent.equalsIgnoreCase("Renewal")){
				if(common.transaction_Details_Premium_Values.get("Material Damage") != null){
					double GrossPremium = common.transaction_Details_Premium_Values.get("Material Damage").get("Gross Premium (GBP)");
					double Commission = common.transaction_Details_Premium_Values.get("Material Damage").get("Com. Rate (%)");
					materialDamageFP = calculateGeneralPremium(Double.toString(GrossPremium), Double.toString(Commission), grossCommTotal);
				}
				if(common.transaction_Details_Premium_Values.get("Loss Of Rental Income") != null){
					double GrossPremium = common.transaction_Details_Premium_Values.get("Loss Of Rental Income").get("Gross Premium (GBP)");
					double Commission = common.transaction_Details_Premium_Values.get("Loss Of Rental Income").get("Com. Rate (%)");
					LossOfRentalIncomeFP = calculateGeneralPremium(Double.toString(GrossPremium), Double.toString(Commission), grossCommTotal);
				}
				if(common.transaction_Details_Premium_Values.get("Employers Liability") != null){
					double GrossPremium = common.transaction_Details_Premium_Values.get("Employers Liability").get("Gross Premium (GBP)");
					double Commission = common.transaction_Details_Premium_Values.get("Employers Liability").get("Com. Rate (%)");
					EmployersLiabiliyFP = calculateGeneralPremium(Double.toString(GrossPremium), Double.toString(Commission), grossCommTotal);
					GrossPremium = common.transaction_Details_Premium_Values.get("Property Owners Liability").get("Gross Premium (GBP)");
					Commission = common.transaction_Details_Premium_Values.get("Property Owners Liability").get("Com. Rate (%)");
					PropertyOwnersLiability = calculateGeneralPremium(Double.toString(GrossPremium), Double.toString(Commission), grossCommTotal);
				}
				if(common.transaction_Details_Premium_Values.get("Terrorism") != null){
					double NetPremium = common.transaction_Details_Premium_Values.get("Terrorism").get("Net Premium (GBP)");
					double Commission = common.transaction_Details_Premium_Values.get("Terrorism").get("Com. Rate (%)");
					terrorismFP =  NetPremium * 0.1; 
							}
				if(common.transaction_Details_Premium_Values.get("Material Damage_FP") != null){
					double GrossPremium = common.transaction_Details_Premium_Values.get("Material Damage_FP").get("Gross Premium");
					double Commission = common.transaction_Details_Premium_Values.get("Material Damage").get("Com. Rate (%)");
					materialDamageFP = materialDamageFP + calculateGeneralPremium(Double.toString(GrossPremium), Double.toString(Commission), grossCommTotal);
				}
				if(common.transaction_Details_Premium_Values.get("Loss Of Rental Income_FP") != null){
					double GrossPremium = common.transaction_Details_Premium_Values.get("Loss Of Rental Income_FP").get("Gross Premium");
					double Commission = common.transaction_Details_Premium_Values.get("Loss Of Rental Income").get("Com. Rate (%)");
					LossOfRentalIncomeFP = LossOfRentalIncomeFP + calculateGeneralPremium(Double.toString(GrossPremium), Double.toString(Commission), grossCommTotal);
				}
				if(common.transaction_Details_Premium_Values.get("Employers Liability_FP") != null){
					double GrossPremium = common.transaction_Details_Premium_Values.get("Employers Liability_FP").get("Gross Premium");
					double Commission = common.transaction_Details_Premium_Values.get("Employers Liability").get("Com. Rate (%)");
					EmployersLiabiliyFP = EmployersLiabiliyFP + calculateGeneralPremium(Double.toString(GrossPremium), Double.toString(Commission), grossCommTotal);
					GrossPremium = common.transaction_Details_Premium_Values.get("Property Owners Liability_FP").get("Gross Premium");
					Commission = common.transaction_Details_Premium_Values.get("Property Owners Liability").get("Com. Rate (%)");
					PropertyOwnersLiability = PropertyOwnersLiability + calculateGeneralPremium(Double.toString(GrossPremium), Double.toString(Commission), grossCommTotal);
				}
				if(common.transaction_Details_Premium_Values.get("Terrorism_FP") != null){
					double NetPremium = common.transaction_Details_Premium_Values.get("Terrorism_FP").get("Net Premium");
					double Commission = common.transaction_Details_Premium_Values.get("Terrorism").get("Com. Rate (%)");
					terrorismFP = terrorismFP +( NetPremium * 0.1); 
							}
				}
				else if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
					if(data_map.get("CD_MaterialDamage").equals("Yes")){
						String GrossPremium = (String)data_map.get("PS_MaterialDamage_GP");
						String Commission = (String)data_map.get("PS_MaterialDamage_CR"); 
						materialDamageFP = calculateGeneralPremium(GrossPremium, Commission, grossCommTotal);
					}
					if(data_map.get("CD_LossOfRentalIncome").equals("Yes")){
						String GrossPremium = (String)data_map.get("PS_LossOfRentalIncome_GP");
						String Commission = (String)data_map.get("PS_LossOfRentalIncome_CR"); 
						LossOfRentalIncomeFP = calculateGeneralPremium(GrossPremium, Commission, grossCommTotal);
					}
					if(data_map.get("CD_Liability").equals("Yes")){
						String GrossPremium = (String)data_map.get("PS_EmployersLiability_GP");
						String Commission = (String)data_map.get("PS_EmployersLiability_CR"); 
						EmployersLiabiliyFP = calculateGeneralPremium(GrossPremium, Commission, grossCommTotal);
						GrossPremium = (String)data_map.get("PS_PropertyOwnersLiability_GP");
						Commission = (String)data_map.get("PS_PropertyOwnersLiability_CR"); 
						PropertyOwnersLiability = calculateGeneralPremium(GrossPremium, Commission, grossCommTotal);
						
					}
					if(data_map.get("CD_Terrorism").equals("Yes")){
						String NetPremium = (String)data_map.get("PS_Terrorism_NP");
						String Commission = (String)data_map.get("PS_Terrorism_CR"); 
						terrorismFP =  Double.parseDouble(NetPremium) * 0.1; 
					}
				}
				
				break;
			}
						
			generalPremium= materialDamageFP + LossOfRentalIncomeFP + EmployersLiabiliyFP 
							+ PropertyOwnersLiability + terrorismFP;	
				common.compareValues(generalPremium, Double.parseDouble(brokerageAccount), "General Brokerage Amount ");
				return generalPremium;
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Calculate Brokerage ammout. \n", t);
			return 0;
		}
}
}
