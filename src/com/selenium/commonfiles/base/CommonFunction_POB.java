package com.selenium.commonfiles.base;

import static com.selenium.commonfiles.util.TestUtil.WriteDataToXl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.yaml.snakeyaml.constructor.SafeConstructor.ConstructYamlOmap;

import com.selenium.commonfiles.util.ErrorInTestMethod;
import com.selenium.commonfiles.util.ErrorUtil;
import com.selenium.commonfiles.util.ObjectMap;
import com.selenium.commonfiles.util.TestUtil;

public class CommonFunction_POB extends TestBase{

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
			customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
			customAssert.assertTrue(common_CCF.funcELDInformation(common.NB_excel_data_map), "ELD Information function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability"),"Issue while Navigating to Property Owners Liability  . ");
			customAssert.assertTrue(funcPropertyOwnersLiability(common.NB_excel_data_map), "Public Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability Information"),"Issue while Navigating to Products Liability  . ");
			customAssert.assertTrue(funcLiabilityInformation(common.NB_excel_data_map), "Liability Information function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_CyberandDataSecurity")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
			customAssert.assertTrue(common_CCF.funcCyberAndDataSecurity(common.NB_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
			customAssert.assertTrue(common_CCF.funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		
		if(((String)common.NB_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
			customAssert.assertTrue(common_CCF.funcLegalExpenses(common.NB_excel_data_map,code,event), "Legal Expenses function is having issue(S) . ");
			}
		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			Assert.assertTrue(common.funcPremiumSummary(common.NB_excel_data_map,code,event,"NB"));
			
			Assert.assertTrue(common.funcStatusHandling(common.NB_excel_data_map,code,event));
			
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
	
	//System.out.println("Test method of MTA For - "+code);
	String NavigationBy = CONFIG.getProperty("NavigationBy");
	String testName = (String)common.MTA_excel_data_map.get("Automation Key");
	common_PEN.AdjustedTaxDetails.clear();
	try{
	
	if(!TestBase.businessEvent.equalsIgnoreCase("Renewal")){
		NewBusinessFlow(code,"NB");
	}
	common.currentRunningFlow="MTA";
	//System.out.println("Test method of MTA For - "+code);
	//System.out.println("MTA data Map - "+common.MTA_excel_data_map);
	//System.out.println("MTA inner data Map - "+common.MTA_Structure_of_InnerPagesMaps);
	common_HHAZ.CoversDetails_data_list.clear();
	customAssert.assertTrue(common.funcNextNavigateDecesionMaker(NavigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen.");
	customAssert.assertTrue(funcCreateEndorsement(),"Error in Create Endorsement function .");
	//customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
	//customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
//customAssert.assertTrue(common.funcVerifyPolicyStatus(common.MTA_excel_data_map,code,TestBase.businessEvent,"Endorsement Submitted"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");

    customAssert.assertTrue(funcPolicyDetails(common.MTA_excel_data_map),"Policy Details function having issue");
    customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"), "Issue while Navigating to Previous Claims");
    customAssert.assertTrue(common_CCF.funcPreviousClaims(common.MTA_excel_data_map), "Previous claim function is having issue(S) .");
	String MTA_Method = (String)common.MTA_excel_data_map.get("MTA_TCDescription");
	customAssert.assertTrue(common.funcNextNavigateDecesionMaker(NavigationBy,"Covers"),"Issue while Navigating to Covers screen.");
	customAssert.assertTrue(common.funcCovers(common.MTA_excel_data_map), "Select covers function is having issue(S) . ");
//	customAssert.assertTrue(funcUpdateCoverDetails_MTA(common.MTA_excel_data_map),"Error in selecting cover for MTA.");
	customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified Perils"),"Issue while Navigating to Specified Perils  . ");
	customAssert.assertTrue(common.funcSpecifiedPerils(common.MTA_excel_data_map), "Select covers function is having issue(S) . ");
	

	if(((String)common.MTA_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
		customAssert.assertTrue(common_POB.funcInsuredProperties(common.MTA_excel_data_map), "Insured Property function is having issue(S) . ");
	}
	
	if(((String)common.MTA_excel_data_map.get("CD_Liability")).equals("Yes")){
		
	//customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"), "Issue while Navigating to Employers Liability .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
		customAssert.assertTrue(common_CCF.funcEmployersLiability(common.MTA_excel_data_map), "Employers Liability function is having issue(S) . ");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
		customAssert.assertTrue(common_CCF.funcELDInformation(common.MTA_excel_data_map), "ELD Information function is having issue(S) . ");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability"),"Issue while Navigating to Property Owners Liability  . ");
		customAssert.assertTrue(funcPropertyOwnersLiability(common.MTA_excel_data_map), "Public Liability function is having issue(S) . ");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability Information"),"Issue while Navigating to Products Liability  . ");
		customAssert.assertTrue(funcLiabilityInformation(common.MTA_excel_data_map), "Liability Information function is having issue(S) . ");
		
	}
	
	
	if(((String)common.MTA_excel_data_map.get("CD_CyberandDataSecurity")).equals("Yes")){
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
		customAssert.assertTrue(common_CCF.funcCyberAndDataSecurity(common.MTA_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
	}
	
	if(((String)common.MTA_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
		customAssert.assertTrue(common_CCF.funcTerrorism(common.MTA_excel_data_map), "Terrorism function is having issue(S) . ");
		}
	
	if(((String)common.MTA_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){		
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
		customAssert.assertTrue(common_CCF.funcLegalExpenses(common.MTA_excel_data_map,code,event), "Legal Expenses function is having issue(S) . ");
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
		customAssert.assertTrue(funcPolicyDetails(common.Rewind_excel_data_map), "Policy Details function having issue .");
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
			customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
			customAssert.assertTrue(common_CCF.funcELDInformation(common.Rewind_excel_data_map), "ELD Information function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability"),"Issue while Navigating to Property Owners Liability  . ");
			customAssert.assertTrue(funcPropertyOwnersLiability(common.Rewind_excel_data_map), "Public Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability Information"),"Issue while Navigating to Products Liability  . ");
			customAssert.assertTrue(funcLiabilityInformation(common.Rewind_excel_data_map), "Liability Information function is having issue(S) . ");
			}
		if(((String)common.Rewind_excel_data_map.get("CD_CyberandDataSecurity")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
			customAssert.assertTrue(common_CCF.funcCyberAndDataSecurity(common.Rewind_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
			}
		if(((String)common.Rewind_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
			customAssert.assertTrue(common_CCF.funcTerrorism(common.Rewind_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		
		if(((String)common.Rewind_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
			customAssert.assertTrue(common_CCF.funcLegalExpenses(common.Rewind_excel_data_map,code,event), "Legal Expenses function is having issue(S) . ");
			}
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
//		customAssert.assertTrue(common_HHAZ.funcEndorsementOperations(common.Rewind_excel_data_map),"Insurance tax adjustment is having issue(S).");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
			Assert.assertTrue(funcPremiumSummary_MTA(common.Rewind_excel_data_map,code,event));
		}else{
		Assert.assertTrue(funcPremiumSummary(common.Rewind_excel_data_map,code,event));
		Assert.assertTrue(common_PEN.funcStatusHandling(common.Rewind_excel_data_map,code,event));
		}
		
		
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
	
	
}


public void RenewalFlow(String code,String event){
	String testName = (String)common.Renewal_excel_data_map.get("Automation Key");
	common.currentRunningFlow = "Renewal";
	common_CCD.isMTARewindStarted = true;
	try{
		
		customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(renewalSearchPolicyNEW(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		/*customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		*/
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Pending"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
		customAssert.assertTrue(common.funcAssignUnderWriter((String)common.Renewal_excel_data_map.get("Renewal_AssignUnderwritter")));
		customAssert.assertTrue(common.funcButtonSelection("Send to Underwriter"));
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Submitted"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
		
		customAssert.assertTrue(funcPolicyDetails(common.Renewal_excel_data_map), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(common_CCF.funcPreviousClaims(common.Renewal_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(common.funcCovers(common.Renewal_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified Perils"),"Issue while Navigating to Specified Perils  . ");
		customAssert.assertTrue(common.funcSpecifiedPerils(common.Renewal_excel_data_map), "Select covers function is having issue(S) . ");
		
		if(((String)common.Renewal_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
			customAssert.assertTrue(funcInsuredProperties(common.Renewal_excel_data_map), "Insured Property function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_Liability")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
			customAssert.assertTrue(common_CCF.funcEmployersLiability(common.Renewal_excel_data_map), "Employers Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
			customAssert.assertTrue(common_CCF.funcELDInformation(common.Renewal_excel_data_map), "ELD Information function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability"),"Issue while Navigating to Property Owners Liability  . ");
			customAssert.assertTrue(funcPropertyOwnersLiability(common.Renewal_excel_data_map), "Public Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability Information"),"Issue while Navigating to Products Liability  . ");
			customAssert.assertTrue(funcLiabilityInformation(common.Renewal_excel_data_map), "Liability Information function is having issue(S) . ");
			}
		if(((String)common.Renewal_excel_data_map.get("CD_CyberandDataSecurity")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
			customAssert.assertTrue(common_CCF.funcCyberAndDataSecurity(common.Renewal_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
			}
		if(((String)common.Renewal_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
			customAssert.assertTrue(common_CCF.funcTerrorism(common.Renewal_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		
		if(((String)common.Renewal_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
			customAssert.assertTrue(common_CCF.funcLegalExpenses(common.Renewal_excel_data_map,code,event), "Legal Expenses function is having issue(S) . ");
		}
		
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		Assert.assertTrue(common_PEN.funcPremiumSummary_MTA(common.Renewal_excel_data_map,code,event));
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
		common_HHAZ.PremiumFlag = false;
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common.funcRewind());
		
		TestUtil.reportStatus("<b> -----------------------Renewal Rewind flow is started---------------------- </b>", "Info", false);
		
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
		customAssert.assertTrue(common.funcSearchPolicy(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Renewal Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		
		customAssert.assertTrue(funcPolicyDetails(common.Rewind_excel_data_map), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(common_CCF.funcPreviousClaims(common.Rewind_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
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
			customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
			customAssert.assertTrue(common_CCF.funcELDInformation(common.Rewind_excel_data_map), "ELD Information function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability"),"Issue while Navigating to Property Owners Liability  . ");
			customAssert.assertTrue(funcPropertyOwnersLiability(common.Rewind_excel_data_map), "Public Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability Information"),"Issue while Navigating to Products Liability  . ");
			customAssert.assertTrue(funcLiabilityInformation(common.Rewind_excel_data_map), "Liability Information function is having issue(S) . ");
			}
		if(((String)common.Rewind_excel_data_map.get("CD_CyberandDataSecurity")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
			customAssert.assertTrue(common_CCF.funcCyberAndDataSecurity(common.Rewind_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
			}
		if(((String)common.Rewind_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
			customAssert.assertTrue(common_CCF.funcTerrorism(common.Rewind_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		
		if(((String)common.Rewind_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
			customAssert.assertTrue(common_CCF.funcLegalExpenses(common.Rewind_excel_data_map,code,event), "Legal Expenses function is having issue(S) . ");
		}
		
		//customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
		//customAssert.assertTrue(common_PEN.funcEndorsementOperations(common.Rewind_excel_data_map),"Insurance tax adjustment is having issue(S).");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(funcPremiumSummary(common.Rewind_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
		
		
		
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
	
	
}

/**
 * 
 * This method handles POB Policy Details screens scripting.
 * 
 */
@SuppressWarnings("static-access")
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
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_NewVenture", (String)map_data.get("PD_NewVenture")), "Unable to Select New Venture radio button on Policy Details Page.");
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_Prospect", (String)map_data.get("PD_Prospect")), "Unable to Select Prospect radio button on Policy Details Page.");
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CrossSell", (String)map_data.get("PD_CrossSell")), "Unable to Select CrossSell radio button on Policy Details Page.");
			customAssert.assertTrue(k.Click("inception_date"), "Unable to Click inception date.");
			customAssert.assertTrue(k.Input("inception_date", (String)map_data.get("QC_InceptionDate")),"Unable to Enter inception date.");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			customAssert.assertTrue(!k.getAttributeIsEmpty("inception_date", "value"),"Inception Date Field Should Contain Valid value  .");
			customAssert.assertTrue(k.Click("deadline_date"), "Unable to Click deadline date.");
			customAssert.assertTrue(k.Input("deadline_date", (String)map_data.get("QC_DeadlineDate")),"Unable to Enter deadline date.");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			customAssert.assertTrue(!k.getAttributeIsEmpty("deadline_date", "value"),"Deadline date Field Should Contain Valid value  .");
			customAssert.assertTrue(k.Input("CCF_QC_TargetPemium", (String) map_data.get("QC_TargetPemium")),"Unable to enter value in Target Pemium field. ");
			
			k.waitTwoSeconds();
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_HoldingBroker", (String)map_data.get("PD_HoldingBroker")), "Unable to Select Holding Broker radio button on Policy Details Page.");
			if(map_data.get("PD_HoldingBroker").equals("No")){
				customAssert.assertTrue(k.Input("CCF_PD_HoldingBrokerInfo", (String) map_data.get("PD_HoldingBrokerInfo")),"Unable to enter value in HoldingBrokerInfo field. ");
			}
		}
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_PD_ProposerName", "value"),"Proposer Name Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("CCF_CC_TradingName", (String)map_data.get("PD_TradingName")),	"Unable to enter value in Trading Name  field .");
		customAssert.assertTrue(k.Input("CCF_PD_BusinessDesc", (String)map_data.get("PD_BusinessDesc")),	"Unable to enter value in Business Desc  field .");
		if(!TestBase.businessEvent.equals("Renewal") && !common.currentRunningFlow.equals("MTA") && !common.currentRunningFlow.equals("Rewind")){
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_1QS", (String)map_data.get("PD_1QS")), "Unable to Select 1QS radio button on Policy Details Page.");
		}
		customAssert.assertTrue(k.Input("CCF_PD_DateEstablishment", (String)map_data.get("PD_DateEstablishment")),	"Unable to enter value in Date Establishment  field .");
		customAssert.assertTrue(k.Input("CCF_Address_CC_Address", (String) map_data.get("PD_Address")),"Unable to enter value in Address field. ");
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"Address Field Should Contain Valid Address  .");
		customAssert.assertTrue(k.Input("CCF_Address_CC_line2", (String) map_data.get("PD_Line1")),"Unable to enter value in Address field line 1 . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_line3", (String) map_data.get("PD_Line2")),"Unable to enter value in Address field line 2 . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_Town", (String) map_data.get("PD_Town")),"Unable to enter value in Town field . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_County", (String) map_data.get("PD_County")),"Unable to enter value in County  . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode", (String)map_data.get("PD_Postcode")),"Unable to enter value in PostCode");
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Postcode", "value"),"PostCode Field Should Contain Valid Postcode  .");
		customAssert.assertTrue(common.validatePostCode((String)map_data.get("PD_Postcode")),"Post Code is not in Correct format .");
		
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_TaxExempt", (String)map_data.get("PD_TaxExempt")), "Unable to Select TaxExempt radio button on Policy Details Page.");
		
		if(TestBase.product.equalsIgnoreCase("POE")){
		//	customAssert.assertTrue(k.SelectRadioBtn("POE_businessEP", (String)map_data.get("PD_businessEP")), "Unable to Select Is this Business a Micro Business Enterprise ? on Policy Details Page.");
		}else{
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CarrierOverride", (String)map_data.get("PD_CarrierOverride")), "Unable to Select Carrier Override radio button on Policy Details Page.");
		}
		
		if(!TestBase.businessEvent.equalsIgnoreCase("Renewal") && !common.currentRunningFlow.equals("MTA")&& !common.currentRunningFlow.equals("Rewind")){
			customAssert.assertTrue(k.Input("CCF_PD_PreviousPremium", (String) map_data.get("PD_PreviousPremium")),"Unable to enter value in Previous Premium field. ");
			if(map_data.get("PD_CarrierOverride").equals("Yes")){
				customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CO_RefferedToHead", (String)map_data.get("PD_CO_RefferedToHead")), "Unable to Select Reffered To Head radio button on Policy Details Page.");
			}
			k.waitTwoSeconds();
		}
		
	
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


public boolean funcPremiumSummary(Map<Object, Object> map_data,String code,String event) {
	
	boolean r_value= true;
	Date currentDate = new Date();
	String testName = (String)map_data.get("Automation Key");
	String customPolicyDuration=null;
	SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
	
	try{
		customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
//		customAssert.assertTrue(common_HHAZ.verifyEndorsementONPremiumSummary(map_data),"Endorsement on Premium is having issue(S).");
		int policy_Duration = 0;
		String Policy_End_Date = "" , policy_Start_date="";
	
	switch(common.currentRunningFlow){
	
	case "NB":
		
		policy_Duration = Integer.parseInt((String)map_data.get("PS_Duration"));
		policy_Duration--;
		policy_Start_date = common_VELA.get_PolicyStartDate((String)map_data.get("PS_PolicyStartDate"));
		map_data.put("PS_PolicyStartDate", policy_Start_date);
		Policy_End_Date = common.daysIncrementWithOutFormation((String)map_data.get("PS_PolicyStartDate"), policy_Duration);
		map_data.put("PS_PolicyEndDate", Policy_End_Date);
		if(((String)map_data.get("PS_DefaultStartEndDate")).equals("No") && !common.currentRunningFlow.equalsIgnoreCase("MTA")){
			customAssert.assertTrue(k.Click("Policy_Start_Date"), "Unable to Click Policy_Start_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_Start_Date", (String)map_data.get("PS_PolicyStartDate")),"Unable to Enter Policy_Start_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
			customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
		}	
		k.waitTwoSeconds();
		//driver.findElement(By.xpath("//*[contains(@name,'_admin_fee')]")).sendKeys(Keys.chord(Keys.CONTROL, "a"),(String)map_data.get("PS_AdminFee"));
		k.SelectRadioBtn("insuranceTaxExemptionRadioButton", (String)map_data.get("PS_TaxExempt"));
		if(((String)map_data.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_InsuranceTaxButton", "Yes",map_data), "Error while writing Tax exemption value to excel .");
		}
		
		if(((String)map_data.get("PS_IsPolicyFinanced")).equals("Yes") && k.getText("PS_Broker_Name").contains("Arthur J.")){
			k.SelectRadioBtn("PS_IsPolicyFinanced","Yes");
			k.Input("PS_Finance_RefNumber", (String)map_data.get("PS_FinanceReferenceNumber"));
			k.DropDownSelection("PS_CreditProvider", (String)map_data.get("PS_CreditProvider"));
		}
		k.waitTwoSeconds();
	break;	
	case "Rewind":
		
		policy_Duration = Integer.parseInt((String)map_data.get("PS_Duration"));
		policy_Duration--;
		policy_Start_date = common_VELA.get_PolicyStartDate((String)map_data.get("PS_PolicyStartDate"));
		map_data.put("PS_PolicyStartDate", policy_Start_date);
		Policy_End_Date = common.daysIncrementWithOutFormation((String)map_data.get("PS_PolicyStartDate"), policy_Duration);
		map_data.put("PS_PolicyEndDate", Policy_End_Date);
		if(((String)map_data.get("PS_DefaultStartEndDate")).equals("No") && !common.currentRunningFlow.equalsIgnoreCase("MTA")){
			customAssert.assertTrue(k.Click("Policy_Start_Date"), "Unable to Click Policy_Start_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_Start_Date", (String)map_data.get("PS_PolicyStartDate")),"Unable to Enter Policy_Start_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
			customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
		}	
		k.waitTwoSeconds();
		break;
	case "Requote":
		
		policy_Duration = Integer.parseInt((String)map_data.get("PS_Duration"));
		policy_Duration--;
		policy_Start_date = common_VELA.get_PolicyStartDate((String)map_data.get("PS_PolicyStartDate"));
		map_data.put("PS_PolicyStartDate", policy_Start_date);
		Policy_End_Date = common.daysIncrementWithOutFormation((String)map_data.get("PS_PolicyStartDate"), policy_Duration);
		map_data.put("PS_PolicyEndDate", Policy_End_Date);
		if(((String)map_data.get("PS_DefaultStartEndDate")).equals("No") && !common.currentRunningFlow.equalsIgnoreCase("MTA")){
			customAssert.assertTrue(k.Click("Policy_Start_Date"), "Unable to Click Policy_Start_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_Start_Date", (String)map_data.get("PS_PolicyStartDate")),"Unable to Enter Policy_Start_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
			customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
		}	
		k.waitTwoSeconds();
		//driver.findElement(By.xpath("//*[contains(@name,'_admin_fee')]")).sendKeys(Keys.chord(Keys.CONTROL, "a"),(String)map_data.get("PS_AdminFee"));
		k.SelectRadioBtn("insuranceTaxExemptionRadioButton", (String)map_data.get("PS_TaxExempt"));
		if(((String)map_data.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_InsuranceTaxButton", "Yes",map_data), "Error while writing Tax exemption value to excel .");
		}
		
		if(((String)map_data.get("PS_IsPolicyFinanced")).equals("Yes") && k.getText("PS_Broker_Name").contains("Arthur J.")){
			k.SelectRadioBtn("PS_IsPolicyFinanced","Yes");
			k.Input("PS_Finance_RefNumber", (String)map_data.get("PS_FinanceReferenceNumber"));
			k.DropDownSelection("PS_CreditProvider", (String)map_data.get("PS_CreditProvider"));
		}
		k.waitTwoSeconds();
		break;
	case "Renewal":
		
		policy_Duration = Integer.parseInt((String)map_data.get("PS_Duration"));
		policy_Duration--;
		policy_Start_date = common_VELA.get_PolicyStartDate((String)map_data.get("PS_PolicyStartDate"));
		map_data.put("PS_PolicyStartDate", policy_Start_date);
		Policy_End_Date = common.daysIncrementWithOutFormation((String)map_data.get("PS_PolicyStartDate"), policy_Duration);
		map_data.put("PS_PolicyEndDate", Policy_End_Date);
		if(((String)map_data.get("PS_DefaultStartEndDate")).equals("No") && !common.currentRunningFlow.equalsIgnoreCase("MTA")){
			customAssert.assertTrue(k.Click("Policy_Start_Date"), "Unable to Click Policy_Start_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_Start_Date", (String)map_data.get("PS_PolicyStartDate")),"Unable to Enter Policy_Start_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
			customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
		}	
		k.waitTwoSeconds();
		//driver.findElement(By.xpath("//*[contains(@name,'_admin_fee')]")).sendKeys(Keys.chord(Keys.CONTROL, "a"),(String)map_data.get("PS_AdminFee"));
		k.SelectRadioBtn("insuranceTaxExemptionRadioButton", (String)map_data.get("PS_TaxExempt"));
		if(((String)map_data.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_InsuranceTaxButton", "Yes",map_data), "Error while writing Tax exemption value to excel .");
		}
		
		if(((String)map_data.get("PS_IsPolicyFinanced")).equals("Yes") && k.getText("PS_Broker_Name").contains("Arthur J.")){
			k.SelectRadioBtn("PS_IsPolicyFinanced","Yes");
			k.Input("PS_Finance_RefNumber", (String)map_data.get("PS_FinanceReferenceNumber"));
			k.DropDownSelection("PS_CreditProvider", (String)map_data.get("PS_CreditProvider"));
		}
		k.waitTwoSeconds();
		break;
	}
	k.waitTwoSeconds();
	
	customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
	customPolicyDuration = k.getText("Policy_Duration");
	customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,map_data),"Error while writing Policy Duration data to excel .");
	TestUtil.reportStatus("Policy Duration = "+customPolicyDuration+" Days . ", "Info", true);
	customAssert.assertTrue(Verify_premiumSummaryTable(), "Error while verifying Premium Summary table .");
	customAssert.assertTrue(insuranceTaxAdjustmentHandling(code,event),"Insurance tax adjustment is having issue(S).");
	customAssert.assertTrue(Verify_premiumSummaryTable(), "Error while verifying Premium Summary table .");
	if(Integer.parseInt(customPolicyDuration)!=365){
		customAssert.assertTrue(funcTransactionPremiumTable(code, event), "Error while verifying Transaction Premium table on premium Summary page .");
	}
	
	TestUtil.reportStatus("Premium Summary details are filled and Verified sucessfully . ", "Info", true);
	return r_value;
}catch(Throwable t){
		
		return false;
	}
}
public boolean insuranceTaxAdjustmentHandling(String code , String event){
 	Map<Object, Object> map_to_update=common.NB_excel_data_map;
 	totalGrossPremium = 0.0;
	totalGrossTax = 0.0;
	totalNetPremiumTax = 0.0;
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
			}else{
				map_to_update = common.MTA_excel_data_map;
				event = "MTA";	
			}
			
		break;
	}
		common.funcButtonSelection("Insurance Tax");
		customAssert.assertTrue(common.funcPageNavigation("Tax Adjustment", ""),"Unable to land on Tax adjustment screen.");
		String sectionName;
		
		if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
			try{
				if(common.currentRunningFlow.equalsIgnoreCase("Renewal")){
					deleteItems();
				}else{
					verifyAdjustedTaxOnBusinessEvent(map_to_update);
				}
			}catch(Throwable t){
				
			}
		}
		
		if(!common.currentRunningFlow.equalsIgnoreCase("MTA")){
			customAssert.assertTrue(verifyCoverDetails(),"PremiumValue verification is causing issue(S).");
			customAssert.assertTrue(verifyGrossPremiumValues(),"PremiumValue verification is causing issue(S).");
		}
		
		if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
			customAssert.assertTrue(deleteItems(),"Unable to delete present tax ajustment on Tax adjustment screen");
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
			
			
			if(((String)map_to_update.get("PD_TaxExempt")).equalsIgnoreCase("No") && value.equalsIgnoreCase("No")){
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
				
			customAssert.assertTrue(common.compareValues(totalGrossPremium, Double.parseDouble(actualTotalGP), "Total Gross Premium from Insuracnce Tax screen"), "Unable to compare total gross premium on Tax adjustment screen.");
			customAssert.assertTrue(common.compareValues(totalGrossTax, Double.parseDouble(actualTotalGT), "Total Gross Tax from Insuracnce Tax screen"), "Unable to compare total gross tax on Tax adjustment screen.");
				
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_to_update.get("Automation Key"), "PS_Total_GP", common.roundedOff(Double.toString(totalGrossPremium)), map_to_update);
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_to_update.get("Automation Key"), "PS_Total_GT", common.roundedOff(Double.toString(totalGrossTax)), map_to_update);
			
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
	
	totalGrossTax = totalGrossTax + Double.parseDouble(finalGrossTax);
	totalGrossPremium = totalGrossPremium + Double.parseDouble(finalGrossPremium);
	
	
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
	
	if(description.equalsIgnoreCase("Gross Tax") || description.equalsIgnoreCase("Gross Premium") ||description.equalsIgnoreCase("Un Adjusted Premium") ){
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


public boolean deleteItems(){
	
	boolean isNotStale=true;
	k.ImplicitWaitOff();
	while(isNotStale){
		try{
			
			
			List<WebElement> delete_Btns = driver.findElements(By.xpath("//*[text()='Delete']"));
			
			for(WebElement element: delete_Btns){
				if(element.isDisplayed())
					element = driver.findElement(By.xpath("//*[text()='Delete']"));
					JavascriptExecutor j_exe = (JavascriptExecutor) driver;
					j_exe.executeScript("arguments[0].scrollIntoView(true);", element);
					element.click();
					WebDriverWait wait = new WebDriverWait(driver, 3);
					if(wait.until(ExpectedConditions.alertIsPresent())!=null){
						k.AcceptPopup();
					}
				else
					continue;
			}
			isNotStale=false;
		}catch(Throwable t){
			continue;
		}
	}
	k.ImplicitWaitOn();
	return true;
	
}
@SuppressWarnings({ "rawtypes", "static-access" })
public boolean verifyAdjustedTaxOnBusinessEvent(Map<Object, Object> map_data) throws Exception {
	
	taxTable_tBody = k.getObject("inssuranceTaxMainTableBODY"); 
	List<WebElement> list2 = taxTable_tBody.findElements(By.tagName("tr"));
	countOfCovers = list2.size();
	String sectionName;
	
	double AP,AT,UP,UGT,GT = 0.0,IPT = 0;
	for(int j=0;j<countOfCovers-1;j++){
		
		taxTable_tBody = k.getObject("inssuranceTaxMainTableBODY");
		sectionName = taxTable_tBody.findElement(By.xpath("tr["+(j+1)+"]/td[1]")).getText();
		
		if(sectionName.equalsIgnoreCase("") || sectionName==null || sectionName.equalsIgnoreCase("Totals")){
			continue;
		}else{
			
			if(sectionName.contains("Personal Accident Standard")){
				sectionName = "Personal Accident";
			}
			if(sectionName.contains("Goods In Transit")){
				sectionName = "Goods in Transit";
			}
			String expectedGP = (String)map_data.get("PS_"+sectionName.replaceAll(" ", "")+"_GP");
			String actualTotalGP = taxTable_tBody.findElement(By.xpath("tr["+(j+1)+"]/td[2]")).getText();
			GT =  Double.parseDouble((String)map_data.get("PS_"+sectionName.replaceAll(" ", "")+"_GT"));
			double expectedTotalGT = 0.0;
			
			if(((String)map_data.get("PD_TaxExempt")).equalsIgnoreCase("Yes")){
				expectedTotalGT =  0.0;
			}else{
				Iterator collectiveDataIT = AdjustedTaxDetails.entrySet().iterator();
				while(collectiveDataIT.hasNext()){
					Map.Entry collectiveAdjustedDetails = (Map.Entry)collectiveDataIT.next();
					String sectionNameofAjustedTax = collectiveAdjustedDetails.getKey().toString();
					
					if(sectionNameofAjustedTax.contains(sectionName.replaceAll(" ", ""))){
						AP =  Double.parseDouble(AdjustedTaxDetails.get(sectionName.replaceAll(" ", "")+"_AP"));
						AT =  Double.parseDouble(AdjustedTaxDetails.get(sectionName.replaceAll(" ", "")+"_AT"));
						UP = Double.parseDouble(expectedGP) - AP;
						UGT = UP * Double.parseDouble((String)map_data.get("PS_IPTRate"))/100.0;
						GT = UGT + AT;
						IPT = GT / Double.parseDouble(expectedGP) * 100.0;
						break;
					}
				
				}
				expectedTotalGT =  GT;
				IPT = GT / Double.parseDouble(expectedGP) * 100.0;
			}
			
			String actualTotalGT = taxTable_tBody.findElement(By.xpath("tr["+(j+1)+"]/td[5]")).getText();
			
			customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expectedGP), Double.parseDouble(actualTotalGP), "Gross Premium for "+sectionName+" - <b> [ New business to "+TestBase.businessEvent+" ] </b> flow."), "Unable to compare gross premium on Tax adjustment screen.");
			customAssert.assertTrue(CommonFunction.compareValues(expectedTotalGT, Double.parseDouble(actualTotalGT), "Gross Tax for "+sectionName+" - <b> [ New business to "+TestBase.businessEvent+" ] </b> flow."), "Unable to compare gross tax on Tax adjustment screen.");
			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_"+sectionName.replaceAll(" ", "")+"_IPT", common_HHAZ.roundedOff(Double.toString(IPT)),map_data),"Error while writing Policy Duration data to excel .");
			
		}
			
		taxTable_tBody = k.getObject("inssuranceTaxMainTableBODY");
		List<WebElement> list3 = taxTable_tBody.findElements(By.tagName("tr"));
		countOfCovers = list3.size();
	}
	
	return true;
	
}
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
			if(coverName_withoutSpace.equalsIgnoreCase("EmployersLiability") || coverName_withoutSpace.equalsIgnoreCase("PropertyOwnersLiability"))
			{coverName_withoutSpace = "Liability";}
			
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
		for(int p=0;p<common.CoversDetails_data_list.size();p++){
			coverName_datasheet = common.CoversDetails_data_list.get(p);
			
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
			String key = null;
			String sectionName = taxTable_tBody.findElement(By.xpath("tr["+(i+1)+"]/td[1]")).getText();
			
			if(!(sectionName.equalsIgnoreCase(""))){
				
				
				coverName_withoutSpace = sectionName.replaceAll(" ", "");
				if(coverName_withoutSpace.equalsIgnoreCase("PropertyOwnersLiability") || coverName_withoutSpace.equalsIgnoreCase("EmployersLiability")){
					key = "CD_Liability";
				
				}else{
					key = "CD_"+coverName_withoutSpace;
				}
				
				
				String expectedIPTRate;
				if(((String)map_to_update.get("PD_TaxExempt")).equalsIgnoreCase("Yes")){
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
					key = "CD_"+coverName_withoutSpace;
					if(sectionName.contains("Personal Accident Standard")){
						coverName_withoutSpace = "PersonalAccident";
					}
					if(sectionName.contains("Goods In Transit")){
						coverName_withoutSpace = "GoodsinTransit";
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
					expectedIPTRate = (String)common.Renewal_excel_data_map.get("PS_"+coverName_withoutSpace+"_IPT");
					
				}
				if(coverName_withoutSpace.equalsIgnoreCase("PropertyOwnersLiability") || coverName_withoutSpace.equalsIgnoreCase("EmployersLiability")){
					key = "CD_Liability";
				}
				if(map_to_update.get(key).toString().equalsIgnoreCase("Yes")){
					if(sectionName.contains("Personal Accident Standard")){
						coverName_withoutSpace = "PersonalAccident";
					}
					if(sectionName.contains("Goods In Transit")){
						coverName_withoutSpace = "GoodsinTransit";
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
public boolean Verify_premiumSummaryTable(){
	err_count = 0;
	PremiumExcTerrDocAct = 0;
	InsTaxDocAct = 0;
	InsTaxDocExp = 0;
	final String code = TestBase.product;
	final String event = TestBase.businessEvent;
	String testName = null,cover_code=null;;
	Map<Object,Object> data_map = null;
	
	switch(common.currentRunningFlow){
		case "NB":
			testName = (String)common.NB_excel_data_map.get("Automation Key");
			data_map = common.NB_excel_data_map;
		break;
		case "CAN":
			testName = (String)common.CAN_excel_data_map.get("Automation Key");
			data_map = common.CAN_excel_data_map;
		break;
		case "MTA":
			testName = (String)common.MTA_excel_data_map.get("Automation Key");
			data_map = common.MTA_excel_data_map;
		break;
		case "Renewal":
			testName = (String)common.Renewal_excel_data_map.get("Automation Key");
			data_map = common.Renewal_excel_data_map;
		break;
		case "Rewind":
			testName = (String)common.Rewind_excel_data_map.get("Automation Key");
			data_map = common.Rewind_excel_data_map;
		break;
		case "Requote":
			testName = (String)common.Requote_excel_data_map.get("Automation Key");
			data_map = common.Requote_excel_data_map;
		break;
	}
	
	final Map<String,String> locator_map = new HashMap<>();
	locator_map.put("GP","gprem");
	locator_map.put("CR","comr");
	locator_map.put("GC","comm");
	locator_map.put("NP","nprem");
	locator_map.put("GT","gipt");
	locator_map.put("NPIPT","nipt");
	
	final Map<String,String> section_map = new HashMap<>();
	
	section_map.put("MaterialDamage","md8");
	section_map.put("LossOfRentalIncome","bi3");
	section_map.put("PropertyOwnersLiability","pl3");
	section_map.put("Terrorism","tr3");
	section_map.put("EmployersLiability","el3");
	section_map.put("PublicLiability","pl2");
	section_map.put("ProductsLiability","pr1");
	section_map.put("CyberandDataSecurity","cyb");
	section_map.put("LegalExpenses","lg2");
	section_map.put("Terrorism", "tr3");
	section_map.put("Total", "tot");
	
	double exp_Premium = 0.0, exp_IPT = 0.00;
	
	try{
	
		String annualTble_xpath = "html/body/div[3]/form/div/table[2]";
		String policy_status_actual = k.getText("Policy_status_header");
		int trans_tble_Rows = driver.findElements(By.xpath(annualTble_xpath+"/tbody/tr")).size();
		int trans_tble_Cols = driver.findElements(By.xpath(annualTble_xpath+"/thead/tr/th")).size();
		String sectionName = null;
		if(common.currentRunningFlow.equalsIgnoreCase("NB") ||  common.currentRunningFlow.equalsIgnoreCase("Renewal")){
		
			if(!PremiumFlag)
			for(int i =1;i<=trans_tble_Rows-1;i++){
				String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
				sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
				if(sectionName.contains("Totals"))
					sectionName = "Total";
				if(sectionName.contains("BusinesssInterruption"))
					sectionName = "BusinessInterruption";
				if(isInsuranceTaxDone == false){
					customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
				}
			}
		
		}
		
		if(common.currentRunningFlow.equalsIgnoreCase("Rewind")){
			String covername;
		//	if(!PremiumFlag)
			for(int i =1;i<=trans_tble_Rows-1;i++){
				String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
				sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
				covername = sectionName;
				if(sectionName.contains("Totals"))
					sectionName = "Total";
				if(sectionName.contains("EmployersLiability") ||sectionName.contains("PropertyOwnersLiability") ){
					covername = sectionName;
					sectionName = "Liability";
					
				}
				if(isInsuranceTaxDone == false){
				customAssert.assertTrue(funcAddInput_PremiumSummary(covername,section_map.get(covername),data_map),"Add Premium Summary Input function having issues for "+sectionName);
				if(((String)data_map.get("PD_TaxExempt")).equalsIgnoreCase("Yes")){
					data_map.put("PS_"+sectionName+"_IPT", "0.0");
				}}
			}
		
		}
		if(common.currentRunningFlow.equalsIgnoreCase("Requote")){
			
			if(!PremiumFlag)
			for(int i =1;i<=trans_tble_Rows-1;i++){
				String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
				sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
				if(sectionName.contains("Totals"))
					sectionName = "Total";
				if(sectionName.contains("BusinesssInterruption"))
					sectionName = "BusinessInterruption";
			
				customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
				if(((String)data_map.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
					data_map.put("PS_"+sectionName+"_IPT", "0.0");
				}
			}
		
		}
		if(common.currentRunningFlow.equalsIgnoreCase("Rewind") && TestBase.businessEvent.equalsIgnoreCase("Renewal")){
			
			if(!PremiumFlag)
				for(int i =1;i<=trans_tble_Rows-1;i++){
					String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
					sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
					if(sectionName.contains("Totals"))
						sectionName = "Total";
					if(sectionName.contains("BusinesssInterruption"))
						sectionName = "BusinessInterruption";
				
					customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
					if(((String)data_map.get("PD_TaxExempt")).equalsIgnoreCase("Yes")){
						data_map.put("PS_"+sectionName+"_IPT", "0.0");
					}
				}
		}
			if(common.currentRunningFlow.equalsIgnoreCase("MTA")){
					
				//if(!PremiumFlag)	
				for(int i =1;i<=trans_tble_Rows-1;i++){
					String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
					sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
					if(sectionName.contains("Totals"))
						sectionName = "Total";
					if(CommonFunction.businessEvent.equalsIgnoreCase("Renewal") && common.currentRunningFlow.equalsIgnoreCase("MTA")){
						if(((String)common.Renewal_excel_data_map.get("CD_"+sectionName)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_"+sectionName)).equals("Yes")){
							if(sectionName.contains("PersonalAccident")){
								sectionName="PersonalAccident";
							}
							if(sectionName.contains("GoodsInTransit")){
								sectionName="GoodsinTransit";
							}
							customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
						}else{
							String cover_name = section_map.get(sectionName);
							String PencCommXpath;
							
								 PencCommXpath = "//*[contains(@id,'_"+cover_name+"_penr')]";
								
							String penComm = driver.findElement(By.xpath(PencCommXpath)).getAttribute("value");
							
							common.MTA_excel_data_map.put("PS_"+sectionName+"_PenComm_rate", penComm);
						
									
							
						}
					}else{
						if(isInsuranceTaxDone == false){
							String covername = sectionName;
							if(sectionName.equalsIgnoreCase("EmployersLiability") || sectionName.equalsIgnoreCase("PropertyOwnersLiability")){
								covername = "Liability";
							}
						if(((String)common.NB_excel_data_map.get("CD_"+covername)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_"+covername)).equals("Yes")){
							customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
						}else{
							String cover_name = section_map.get(sectionName);
							String PencCommXpath;
							PencCommXpath = "//*[@id='"+cover_name+"_comr']";
							String penComm = driver.findElement(By.xpath(PencCommXpath)).getAttribute("value");
							common.MTA_excel_data_map.put("PS_"+sectionName+"_CR", penComm);
						}
						if(!policy_status_actual.contains("Rewind") && isInsuranceTaxDone==false){
						//	data_map.put("PS_"+sectionName+"_IPT", data_map.get("PS_IPTRate"));
							}else if(((String)data_map.get("PD_TaxExempt")).equalsIgnoreCase("Yes")){
								data_map.put("PS_"+sectionName+"_IPT", "0.0");
							}
						}
					}
				}
			}
			
			PremiumFlag = true;	 
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
			
			for(int i =1;i<=trans_tble_Rows-1;i++){
				String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
				sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
				 if(sectionName.contains("Totals")){
					sectionName = "Total";}
//				 if(sectionName.contains("BusinesssInterruption"))
//						sectionName = "BusinessInterruption";
//				
				err_count = err_count + func_PremiumSummaryCalculation(section_map.get(sectionName),sectionName,locator_map);
				exp_Premium = exp_Premium + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_GP"));
				exp_IPT = exp_IPT + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_GT"));
			}
			
			double Total_GP = 00.00;
			double Total_GT = 00.00;
			double Total_GC = 00.00,Total_NP = 00.00,Total_NPIPT=00.00 ,Total_BrokComm = 00.00 ;
			
			for(int i =1;i<=trans_tble_Rows-1;i++){
				String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
				sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
				 if(sectionName.contains("Totals")){
					sectionName = "Total";}
				 if(sectionName.contains("BusinesssInterruption")){
						sectionName = "BusinessInterruption";}
				Total_GP = Total_GP + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_GP"));
				Total_GT = Total_GT + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_GT"));
				Total_GC = Total_GC + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_GC"));
				Total_NP = Total_NP + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_NP"));
				Total_NPIPT = Total_NPIPT + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_NPIPT"));
			}
			
			data_map.put("PS_Total_GT", f.format(Total_GT));
			data_map.put("PS_Total_GP", f.format(Total_GP));
			data_map.put("PS_Total_GC", f.format(Total_GC));
			data_map.put("PS_Total_NPIPT", f.format(Total_NPIPT));
			data_map.put("PS_Total_NP", f.format(Total_NP));
					
			String exp_Gross_Premium = common.roundedOff(Double.toString(exp_Premium));
			String exp_Gross_IPT = common.roundedOff(Double.toString(exp_IPT));
			if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
				TransactionDetailsTable(section_map,locator_map,code,event,true);
				Map<String,Double> transSmryData = new LinkedHashMap<String,Double>();
				transSmryData = common.transaction_Details_Premium_Values.get("Totals");
				exp_Gross_Premium = Double.toString(transSmryData.get("Gross Premium (GBP)"));
				exp_Gross_IPT = Double.toString(transSmryData.get("Gross IPT (GBP)"));
			}
			if(TestBase.businessEvent.equalsIgnoreCase("Renewal") && common.currentRunningFlow.equalsIgnoreCase("MTA")){
				TransactionDetailsTable(section_map,locator_map,code,event,true);
				Map<String,Double> transSmryData = new LinkedHashMap<String,Double>();
				transSmryData = common.transaction_Details_Premium_Values.get("Totals");
				exp_Gross_Premium = Double.toString(transSmryData.get("Gross Premium (GBP)"));
				exp_Gross_IPT = Double.toString(transSmryData.get("Gross IPT (GBP)"));
			}
			String act_Gross_Premium = driver.findElement(By.xpath("//*[text()='Annual Premium ']//preceding::*[@id='table0']/tbody/tr[1]/td[3]")).getText();
			String act_Gross_IPT = driver.findElement(By.xpath("//*[text()='Annual Premium ']//preceding::*[@id='table0']/tbody/tr[2]/td[3]")).getText();
			String act_Total_Premium = driver.findElement(By.xpath("//*[text()='Annual Premium ']//preceding::*[@id='table0']/tbody/tr[4]/td[3]")).getText();
			act_Gross_Premium = act_Gross_Premium.replaceAll(",", "").replaceAll("£", "");
			act_Gross_IPT = act_Gross_IPT.replaceAll(",", "").replaceAll("£", "");
			act_Total_Premium = act_Total_Premium.replaceAll(",", "").replaceAll("£", "");
			
			double exp_Total_Premium = Double.parseDouble(exp_Gross_Premium) + Double.parseDouble(exp_Gross_IPT);
			
			TestUtil.reportStatus("---------------Total Premium-----------------","Info",false);
			CommonFunction.compareValues(Double.parseDouble(exp_Gross_Premium),Double.parseDouble(act_Gross_Premium),"Gross Premium.");
			CommonFunction.compareValues(Double.parseDouble(exp_Gross_IPT),Double.parseDouble(act_Gross_IPT),"Gross IPT.");
			CommonFunction.compareValues(exp_Total_Premium,Double.parseDouble(act_Total_Premium),"Total Premium.");
			
			return true;
		   
		}catch(Throwable t){ 
					return false;
			
		}
	}
public static void TransactionDetailsTable(Map<String,String> coverlist,Map<String,String> cHash,String code, String event, boolean xlWrite){
	//Transaction Premium Table
		try{
			k.ImplicitWaitOff();
			cHash.clear();
			cHash.put("gprem","Gross Premium (GBP)");
			cHash.put("comr","Com. Rate (%)");
			cHash.put("comm","Commission (GBP)	");
			cHash.put("nprem","Net Premium (GBP)");
			cHash.put("gipt","Gross IPT (GBP)");
			cHash.put("nipt","Net IPT (GBP)");
			
			List<WebElement> row = driver.findElements(By.xpath("html/body/div[3]/form/div/table[4]/tbody/tr"));
			//System.out.println(row.size());
			int rcount1 = row.size();
			String Trax_table = "html/body/div[3]/form/div/table[4]";
			String[] prem_col = {"gprem","comr","comm","nprem","gipt","nipt"};
			//String[] prem_col = {"GP","CR","GC","NP","GT","NPIPT"};
			String testName = (String)common.MTA_excel_data_map.get("Automation Key");
			List<WebElement> col=driver.findElements(By.xpath("html/body/div[3]/form/div/table[4]/tbody/tr[1]/td"));
			//System.out.println(col.size());
			int cCount = col.size();
			if(driver.findElement(By.xpath(Trax_table)).isDisplayed()){
				
				
				String tXpath =Trax_table+"/tbody/tr";
				String CellValue;
				//System.out.println("Transaction Premium Table exist on premium summary page");
				TestUtil.reportStatus("Transaction Details Table exist on premium summary page", "Info", true);
				//System.out.println("Covers Found -:"+ (rcount-1));
				TestUtil.reportStatus("Covers Found -:"+ (rcount1-1), "Info", false);
				
				for(int i =1;i<=rcount1;i++){
					Map<String,Double> transSmryData = new LinkedHashMap<String,Double>();
					String sectionXpath = tXpath+"["+i+"]/td[1]";
					String sec_name = driver.findElement(By.xpath(sectionXpath)).getText();
					String Covername = sec_name;
					//System.out.print(sec_name+"\t\t\t");
					if(sec_name.contains("Frozen Food")){
						sec_name = "FrozenFoods";
					}else if(sec_name.contains("Licence")){
						sec_name = "LossOfLicence";
					}else if(sec_name.contains("Total")){
						sec_name = "Total";
						sec_name = "Total";
					}else{
						sec_name = sec_name.replaceAll(" ", "");
					}
					
					TestUtil.reportStatus("---------------"+sec_name+"-----------------","Info",false);
					
					for(int j = 2; j<=cCount;j++){
						//transSmryData
						String ColumnValues = tXpath+"["+i+"]/td["+j+"]";
						CellValue = driver.findElement(By.xpath(ColumnValues)).getText().replaceAll(",","");;
						//System.out.print(CellValue +  "\t");	
						//TestUtil.reportStatus(CellValue +  "\t", "Info", false);
						if(cHash.get(prem_col[j-2]).equalsIgnoreCase("Com. Rate (%)") && sec_name.contains("Total"))
						{
							
						}else{
						transSmryData.put(cHash.get(prem_col[j-2]), Double.parseDouble(CellValue));}
					}
					PremiumSummarytableCalculation(transSmryData,sec_name);
					//System.out.println("\n___");
					common.transaction_Details_Premium_Values.put(Covername, transSmryData);
				}
			
			// Writing Data to Excel Sheet from Map
//				Set<String> pKeys=transSmryData.keySet();
//			 	for(String pkey:pKeys){
//			 		if(xlWrite){
//			 			customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName,pkey,transSmryData.get(pkey) ,common.MTA_excel_data_map),"Error while writing Premium Summary data to excel ."+pkey) ;
//			 		}else{
//			 			common.compareValues(Double.parseDouble(transSmryData.get(pkey)), Double.parseDouble(TestUtil.getStringfromMap(transSmryData.get(pkey),"")), pkey);
//			 		}
//			 	}
			}
		}
		catch(Throwable t ){
			k.ImplicitWaitOn();
			System.out.println("--"); 
		}
		
		//System.out.println("Gross Premium for Material Damage Cover in Transaction Premium table is :"+transSmryData.get("MaterialDamage_gprem"));
		 TestUtil.reportStatus("Values have been stored from Transaction Details table", "info", true);
}
public static boolean PremiumSummarytableCalculation(Map<String,Double> tabledata,String section ){
	//MaterialDamage_nipt, MaterialDamage_gprem, MaterialDamage_nprem, MaterialDamage_gipt, MaterialDamage_comm,MaterialDamage_comr
	try{ 	
	if(section.equals("Total")){ return true;}
		DecimalFormat f = new DecimalFormat("00.00");
		String secName = section.replaceAll(" ", "");
		String cSection = null;
		double grossPrem = tabledata.get(secName+"Gross Premium (GBP)");
		double commisn   = tabledata.get(secName+"Commission (GBP)"); 
		double commRate  = tabledata.get(secName+"Com. Rate (%)");
		double netPrem 	 = tabledata.get(secName+"Net Premium (GBP)");
		double grossIPT  = tabledata.get(secName+"Gross IPT (GBP)");
		double netIPT    = tabledata.get(secName+"Net IPT (GBP)");
		
		
		if(secName.contains("Frozen")){
			cSection = "FrozenFoods";
		}else if(secName.contains("Licence")){
			cSection = "LossOfLicence";
		}else if(secName.contains("Total")){
			cSection = "Total";
		}else{
			cSection = secName.replaceAll(" ", "");
		}
		
		double IPT =  Double.parseDouble(TestUtil.getStringfromMap("PS_"+cSection+"_IPT",""));
		TestUtil.reportStatus("Calculation for cover <b> "+section+"</b> as per IPT rate :"+IPT+" %", "Info", false);
		//System.out.println("\nCalculation Cover Name :"+section);// Added to the reporting & Logfile }
		//TestUtil.reportStatus("\nCalculation for Cover : <b>"+section, "Info", true);
		double denominator = (1.00-(commRate/100));
		double calcltdComm = (netPrem/denominator)*(commRate/100);
		//System.out.println("calculated Gross Commission :"+f.format(calcltdComm));// Added to the reporting & Logfile }
		TestUtil.reportStatus("Calculated Gross Commission :<b>"+f.format(calcltdComm), "Info", false);
		double calcltdGprem = calcltdComm + netPrem;
		//System.out.println("Calculated Gross Premium :"+f.format(calcltdGprem));// Added to the reporting & Logfile }
		TestUtil.reportStatus("Calculated Gross Premium :<b>"+f.format(calcltdGprem), "Info", false);
		double calcltdGIPT = calcltdGprem *(IPT/100);
		//System.out.println("Calculated Gross IPT :"+f.format(calcltdGIPT));// Added to the reporting & Logfile }
		TestUtil.reportStatus("Calculated Gross IPT :<b>"+f.format(calcltdGIPT), "Info", false);
		double calcltdNIPT = netPrem*(IPT/100);
		//System.out.println("Calculated Net IPT :"+f.format(calcltdNIPT));// Added to the reporting & Logfile }
		TestUtil.reportStatus("Calculated Net IPT :<b>"+f.format(calcltdNIPT), "Info", false);	
		customAssert.assertTrue(common.compareValues(calcltdGprem,grossPrem,"Gross Premium "),"Mismatched Gross Premium Values");
		customAssert.assertTrue(common.compareValues(calcltdComm,commisn,"Gross Commission "),"Mismatched Gross Commission Values");
		customAssert.assertTrue(common.compareValues(calcltdGIPT,grossIPT,"Gross IPT value "),"Mismatched Gross IPT Values");
		customAssert.assertTrue(common.compareValues(calcltdNIPT,netIPT,"Net IPT Values "),"Mimatched Net IPT Values");
		return true;
	}catch(Throwable t){
		return false;
	}
	}	
public int func_PremiumSummaryCalculation(String code,String covername,Map<String,String> premium_loc) {
	
	Map<Object,Object> map_data = null;
	Map<Object,Object> Tax_map_data = new HashMap<>();
	
	String event=null;
	
	
	switch(TestBase.businessEvent){
		case "NB":
			map_data = common.NB_excel_data_map;
			Tax_map_data = common.NB_excel_data_map;
		break;
		case "Rewind":
			if(common.currentRunningFlow.equals("NB")){
				map_data = common.NB_excel_data_map;
				Tax_map_data = common.NB_excel_data_map;
				event = "NB";
				}
			else{
				map_data = common.Rewind_excel_data_map;
				Tax_map_data = common.Rewind_excel_data_map;
				event = "Rewind";
				
			}
		break;
		case "Requote":
			if(common.currentRunningFlow.equals("NB")){
				map_data = common.NB_excel_data_map;
				Tax_map_data = common.NB_excel_data_map;
				event = "NB";
				}
			else{
				map_data = common.Requote_excel_data_map;
				Tax_map_data = common.Requote_excel_data_map;
				event = "Requote";
				
			}
		break;
		case "MTA":
			if(common.currentRunningFlow.equals("NB")){
				map_data = common.NB_excel_data_map;
				Tax_map_data = common.NB_excel_data_map;
				event = "NB";
				}
			else if(common.currentRunningFlow.equals("Rewind")){
				map_data = common.Rewind_excel_data_map;
				Tax_map_data = common.NB_excel_data_map;
				event = "Rewind";
			}else{
				
				map_data = common.MTA_excel_data_map;
				Tax_map_data = common.NB_excel_data_map;
				event = "MTA";
				
			}
			break;	
		case "Renewal":
			if(CommonFunction.businessEvent.equalsIgnoreCase("Renewal") && common.currentRunningFlow.equalsIgnoreCase("MTA")){
				map_data = common.MTA_excel_data_map;
				Tax_map_data = common.Renewal_excel_data_map;
				event = "MTA";
			}else if(CommonFunction.businessEvent.equalsIgnoreCase("Renewal") && common.currentRunningFlow.equalsIgnoreCase("Rewind")){
				map_data = common.Rewind_excel_data_map;
				Tax_map_data = common.Renewal_excel_data_map;
				event = "Rewind";
			}else{
				map_data = common.Renewal_excel_data_map;
				Tax_map_data = common.Renewal_excel_data_map;
				event = "Renewal";
			}
			break;
		case "CAN":
			if(common.currentRunningFlow.equals("NB")){
				map_data = common.NB_excel_data_map;
				Tax_map_data = common.NB_excel_data_map;
				event = "NB";
				}
			else{
				map_data = common.CAN_excel_data_map;
				Tax_map_data = common.CAN_excel_data_map;
				event = "CAN";				
			}
			
		break;	
		
	}
		String testName = (String)map_data.get("Automation Key");
		
		double Net_Premium = Double.parseDouble((String)map_data.get("PS_"+covername+"_NP"));
		
	try{
		
		
			
			TestUtil.reportStatus("---------------"+covername+"-----------------","Info",false);
			//SPI Pen commission Calculation : 
			
			// Net Premium verification : 
			double netP = Net_Premium;
			String netP_expected = common.roundedOff(Double.toString(netP));
			String netP_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+premium_loc.get("NP")+"')]", "value");
			CommonFunction.compareValues(Double.parseDouble(netP_expected),Double.parseDouble(netP_actual),"Net Premium");
			map_data.put("PS_"+covername+"_NP",netP_expected);
			TestUtil.reportStatus("Net Premium :<b>"+netP_expected+"</b> matches with <b>"+netP_actual, "Info", false);
			
			// Gross Commision Verification:
			double denominator = (1.00-(Double.parseDouble((String)map_data.get("PS_"+covername+"_CR"))/100));
			double calcltdComm = (Net_Premium/denominator)*(Double.parseDouble((String)map_data.get("PS_"+covername+"_CR"))/100);
			String grossC_expected = common.roundedOff(Double.toString(calcltdComm));
			String grossC_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+premium_loc.get("GC")+"')]", "value");
			CommonFunction.compareValues(Double.parseDouble(grossC_expected),Double.parseDouble(grossC_actual),"Gross Commision");
			map_data.put("PS_"+covername+"_GC",netP_expected);
			TestUtil.reportStatus("Commission :<b>"+(netP_expected)+"</b> matches with <b>"+(grossC_actual), "Info", false);
			
			//Gross Premium Verification:
			double grossP = Net_Premium + calcltdComm;
			String grossP_expected = common.roundedOff(Double.toString(grossP));
			String grossP_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+premium_loc.get("GP")+"')]", "value");
			CommonFunction.compareValues(Double.parseDouble(grossP_expected),Double.parseDouble(grossP_actual),"Gross Premium");
			map_data.put("PS_"+covername+"_GP",grossP_expected);
			TestUtil.reportStatus("Gross Premium :<b>"+(grossP_expected)+"</b> matches with <b>"+(grossP_actual), "Info", false);
			
			//Gross IPT Verification:
			if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
				String InsuranceTax = k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+premium_loc.get("GT")+"')]", "value");
				double IPT = (Double.parseDouble(InsuranceTax) / grossP) * 100.0;
				TestUtil.WriteDataToXl(TestBase.product+"_"+event, "Premium Summary",testName, "PS_"+covername+"_IPT", common_HHAZ.roundedOff(Double.toString(IPT)), map_data);
			}
			double calcltdGIPT = grossP *(Double.parseDouble((String)map_data.get("PS_"+covername+"_IPT"))/100);
			String grossIPT_expected = common.roundedOff(Double.toString(calcltdGIPT));
			String grossIPT_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+premium_loc.get("GT")+"')]", "value");
			CommonFunction.compareValues(Double.parseDouble(grossIPT_expected),Double.parseDouble(grossIPT_actual),"Gross IPT");
			map_data.put("PS_"+covername+"_GT",grossIPT_expected);
			TestUtil.reportStatus("Gross IPT :<b>"+(grossIPT_expected)+"</b> matches with <b>"+(grossIPT_actual), "Info", false);
			
			//Net IPT Verification
			double calcltdNIPT = netP *(Double.parseDouble((String)map_data.get("PS_"+covername+"_IPT"))/100);
			String grossNIPT_expected = common.roundedOff(Double.toString(calcltdNIPT));
			String grossNIPT_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+premium_loc.get("NPIPT")+"')]", "value");
			CommonFunction.compareValues(Double.parseDouble(grossNIPT_expected),Double.parseDouble(grossNIPT_actual),"Net IPT");
			map_data.put("PS_"+covername+"_NPIPT",grossNIPT_expected);
			TestUtil.reportStatus("Net IPT:<b>"+(grossNIPT_expected)+"</b> matches with <b>"+(grossNIPT_actual), "Info", false);
			
			if(common.currentRunningFlow.equals("MTA")){
				if(((String)map_data.get("PD_TaxExempt")).equalsIgnoreCase("Yes"))
					Tax_map_data.put("PS_"+covername+"_IPT", "0.0");
			}
		return 0;		
			
	}catch(Throwable t) { 
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Insured Properties function is having issue(S). \n", t);
        return 0;
 }
	
}

public boolean funcAddInput_PremiumSummary(String code, String cover_name,Map<Object,Object> data_map) {
	boolean retvalue=true;
	String testName = (String)data_map.get("Automation Key");
	try{
		//cob_pl_pr_penr
		String PencCommXpath =null;
	
		PencCommXpath = "//*[@id='"+cover_name+"_comr']";
		
		customAssert.assertTrue(k.InputByXpath(PencCommXpath, (String)data_map.get("PS_"+code+"_CR")), "Unable to enter value of Pen Commission for "+cover_name+".");
		String ActualPenComm = driver.findElement(By.xpath(PencCommXpath)).getAttribute("value");
		//k.foc(GrossCommXpath);
		if(common.product.equalsIgnoreCase("POF")){
			double penComm = Double.parseDouble((String)data_map.get("PS_"+code+"_PenComm_rate"));
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Covers Screen .");
		}
		return retvalue;
	}catch(Throwable t) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
		Assert.fail("Premium Summary Add Input function is having issue(S). \n", t);
		return false;
	}
}
public boolean funcTransactionPremiumTable(String code, String event){
	//Transaction Premium Table
	
	
	Map<Object,Object> data_map = null;
	
		switch(common.currentRunningFlow){
			case "NB":
				data_map = common.NB_excel_data_map;
				break;
			case "Renewal":
				data_map = common.Renewal_excel_data_map;
				break;
		}
	
	
		try{
			String testName = (String)data_map.get("Automation Key");
			k.pressDownKeyonPage();
			customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
			
			int policy_Duration = Integer.parseInt((String)data_map.get("PS_Duration"));
			String transactionTble_xpath = "//p[text()=' Transaction Premium']//following-sibling::table[@id='table0']";
			WebElement transaction_Table = driver.findElement(By.xpath(transactionTble_xpath));
			
			List<WebElement> colms = transaction_Table.findElements(By.tagName("th"));
				//Map<SPI,MAP<NNP,12345.67>>
			
			int trans_tble_Rows = transaction_Table.findElements(By.tagName("tr")).size();
			int trans_tble_Cols = colms.size();
			
			List<String> sectionNames = new ArrayList<>();
			String sectionName = null;
			String sectionValue = null;
			String headerName = null;
			PremiumExcTerrDocAct= 0.00;
			
			if(transaction_Table.isDisplayed()){
				
				TestUtil.reportStatus("Transaction Premium Table exist on premium summary page . ", "Info", true);
			
				//For Each Cover Row
				for(int row = 1; row < trans_tble_Rows ;row ++){
					
					WebElement sec_Name = driver.findElement(By.xpath(transactionTble_xpath+"//tbody//tr["+row+"]//td["+1+"]"));
					sectionName = sec_Name.getText();
					
					switch(sectionName){
					
					case "Totals":
						Map<String,Double> transaction_Section_Vals_Total = new HashMap<>();
						//For Each Cols
						for(int col = 2; col <= trans_tble_Cols ;col ++){
					
							////p[text()=' Transaction Premium']//following-sibling::table[@id='table0']//thead//th[2]
							WebElement header_Name = driver.findElement(By.xpath(transactionTble_xpath+"//thead//th["+col+"]"));
							headerName = header_Name.getText();
						
							if(!headerName.contains("Pen Comm %") && !headerName.contains("Broker Comm %") && !headerName.contains("Gross Comm %")
									&& !headerName.contains("Insurance Tax Rate") ){
								WebElement sec_Val = driver.findElement(By.xpath(transactionTble_xpath+"//tbody//tr["+row+"]//td["+col+"]"));
								sectionValue = sec_Val.getText();
								sectionValue = sectionValue.replaceAll(",", "");
								transaction_Section_Vals_Total.put(headerName, Double.parseDouble(sectionValue));
								
							}else{
								continue;
							}
							transaction_Premium_Values.put(sectionName, transaction_Section_Vals_Total);
					}
					
					break;
					
					default:
						Map<String,Double> transaction_Section_Vals = new HashMap<>();
						if(sectionName.equalsIgnoreCase("Businesss Interruption")){sectionName = "Business Interruption";}
						//For Each Cols
						for(int col = 2; col <= trans_tble_Cols ;col ++){
					
							////p[text()=' Transaction Premium']//following-sibling::table[@id='table0']//thead//th[2]
							WebElement header_Name = driver.findElement(By.xpath(transactionTble_xpath+"//thead//th["+col+"]"));
							headerName = header_Name.getText();
						
							WebElement sec_Val = driver.findElement(By.xpath(transactionTble_xpath+"//tbody//tr["+row+"]//td["+col+"]"));
							sectionValue = sec_Val.getText();
						
							transaction_Section_Vals.put(headerName, Double.parseDouble(sectionValue));
					}
						transaction_Premium_Values.put(sectionName, transaction_Section_Vals);
					
					break;
					
					}
					
				}
				//System.out.println(transaction_Premium_Values);
				
				TestUtil.reportStatus("---------------Transaction Premium table Verification-----------------","Info",false);
				//Transaction table Verification
				for(int row = 1; row < trans_tble_Rows ;row ++){
					WebElement sec_Name = driver.findElement(By.xpath(transactionTble_xpath+"//tbody//tr["+row+"]//td["+1+"]"));
					String section = sec_Name.getText();
					if(section.equalsIgnoreCase("Businesss Interruption")){section = "Business Interruption";}
					sectionNames.add(section);
				}
				for(String s_Name : sectionNames){
					if(s_Name.equals("Totals"))
						trans_error_val = trans_error_val + transactionPremiumTable_Verification_Total(sectionNames,transaction_Premium_Values);
					else
						trans_error_val = trans_error_val + transactionPremiumTable_Verification(policy_Duration,s_Name,transaction_Premium_Values);
					
			
				}
				
				 TestUtil.reportStatus("Transaction Premium table has been verified suceesfully . ", "info", true);
				
			}
			
			if(Integer.parseInt((String)data_map.get("PS_Duration")) != 365){
				//Toal Premium With Admin Fees
				double total_premium_with_admin_fee = transaction_Premium_Values.get("Totals").get("Gross Premium") + 
						transaction_Premium_Values.get("Totals").get("Insurance Tax");
				
				String exp_Total_Premium_with_Admin_fee = common.roundedOff(Double.toString(total_premium_with_admin_fee));
				k.waitTwoSeconds();
				
				String xPath = "//table[@id='table0']//*//td[text()='Total']//following-sibling::td";
				String act_Total_Premium_with_Admin_fee = k.getTextByXpath(xPath);
				
				act_Total_Premium_with_Admin_fee = act_Total_Premium_with_Admin_fee.replaceAll(",", "");
				double premium_diff = Double.parseDouble(common.roundedOff(Double.toString(Double.parseDouble(exp_Total_Premium_with_Admin_fee) - Double.parseDouble(act_Total_Premium_with_Admin_fee))));
				
				/*TestUtil.reportStatus("---------------Total Premium with Admin Fees-----------------","Info",false);
				
				if(Math.abs(premium_diff)<=0.20){
					TestUtil.reportStatus("Total Premium with Admin Fees :[<b> "+exp_Total_Premium_with_Admin_fee+" </b>] matches with actual premium [<b> "+act_Total_Premium_with_Admin_fee+"</b>]as expected with some difference upto '0.05' on premium summary page.", "Pass", false);
					customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_TotalFinalPremium", exp_Total_Premium_with_Admin_fee,data_map),"Error while writing Total Final Premium data to excel .");
				}else{
					TestUtil.reportStatus("Mismatch in Expected Total Premium with Admin Fees [<b> "+exp_Total_Premium_with_Admin_fee+"</b>] and Actual Premium [<b> "+act_Total_Premium_with_Admin_fee+"</b>] on premium summary page.", "Fail", false);
					customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_TotalFinalPremium", exp_Total_Premium_with_Admin_fee,data_map),"Error while writing Total Final Premium data to excel .");
				}*/
				}
			
		}catch(Throwable t ){
			return false;
		}
		
		return true;
}
public int transactionPremiumTable_Verification_Total(List<String> sectionNames,Map<String,Map<String,Double>> transaction_Premium_Values){
	
	try{
	
	
	TestUtil.reportStatus("---------------Totals-----------------","Info",false);
	double exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Net Net Premium");
	}
	String t_NetNetP_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Net Net Premium"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_NetNetP_actual)," Net Net Premium");

	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Pen Comm");
	}
	String t_pc_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Pen Comm"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_pc_actual)," Pen Commission");
	
	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Net Premium");
	}
	String t_netP_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Net Premium"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_netP_actual),"Net Premium");
	
	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Broker Commission");
	}
	String t_bc_actual =  Double.toString(transaction_Premium_Values.get("Totals").get("Broker Commission"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_bc_actual),"Broker Commission");
	
	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Gross Premium");
	}
	String t_grossP_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Gross Premium"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_grossP_actual)," Gross Premium");
	
	
	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Insurance Tax");
	}
	String t_InsuranceTax_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Insurance Tax"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_InsuranceTax_actual),"Insurance Tax");
	
	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Total Premium");
	}
	String t_p_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Total Premium"));
	double premium_diff = exp_value - Double.parseDouble(t_p_actual);
	
	if(premium_diff<0.05 && premium_diff>-0.05){
		TestUtil.reportStatus("Total Premium [<b> "+exp_value+" </b>] matches with actual total premium [<b> "+t_p_actual+" </b>]as expected for Totals in Transaction Premium table .", "Pass", false);
		return 0;
		
	}else{
		TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+exp_value+"</b>] and Actual Premium [<b> "+t_p_actual+"</b>] for Totals in Transaction Premium table . </p>", "Fail", true);
		return 1;
	}
	
}catch(Throwable t) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
    Assert.fail("Transaction Premium total Section verification issue.  \n", t);
    return 1;
}
}
public int transactionPremiumTable_Verification(int policy_Duration,String sectionNames,Map<String,Map<String,Double>> transaction_Premium_Values){

	Map<Object,Object> map_data = null;
	
	switch(common.currentRunningFlow){
		case "NB":
			map_data = common.NB_excel_data_map;
			break;
		case "Renewal":
			map_data = common.Renewal_excel_data_map;
			break;
	}
	String code=null;
	if(sectionNames.contains("Legal Expenses")){
		code="LegalExpenses";
	}else if(sectionNames.contains("Businesss Interruption")){
		code= "BusinessInterruption";
		sectionNames = "Business Interruption";
	}
	else{
		code=sectionNames.replace(" ", "");
	}
	
	
	
try{
		
		TestUtil.reportStatus("---------------"+sectionNames+"-----------------","Info",false);
		
		double annual_NetNetP = Double.parseDouble((String)map_data.get("PS_"+code+"_NetNetPremium"));
		String t_NetNetP_expected = common.roundedOff(Double.toString((annual_NetNetP/365)*policy_Duration));
		String t_NetNetP_actual = Double.toString(transaction_Premium_Values.get(sectionNames).get("Net Net Premium"));
		customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(t_NetNetP_expected),Double.parseDouble(t_NetNetP_actual)," Net Net Premium"),"Mismatched Net Net Premium");
		//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent,"Premium Summary",testName,"PS_"+code+"_PenComm",pc_expected,common.NB_excel_data_map),"Error while writing Pen Commission for cover "+code+" to excel .");
		
		// Transaction Pen commission Calculation : 
		double t_pen_comm = (( Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate"))/100)));
		String t_pc_expected = common.roundedOff(Double.toString(t_pen_comm));
		String t_pc_actual = Double.toString(transaction_Premium_Values.get(sectionNames).get("Pen Comm"));
		customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(t_pc_expected),Double.parseDouble(t_pc_actual)," Pen Commission"),"Mismatched Pen Commission Values");
		//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent,"Premium Summary",testName,"PS_"+code+"_PenComm",pc_expected,common.NB_excel_data_map),"Error while writing Pen Commission for cover "+code+" to excel .");
		
		
		// Transaction Net Premium verification : 
		double t_netP = Double.parseDouble(t_pc_expected) + Double.parseDouble(t_NetNetP_expected);
		String t_netP_expected = common.roundedOff(Double.toString(t_netP));
		String t_netP_actual = Double.toString(transaction_Premium_Values.get(sectionNames).get("Net Premium"));
		customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(t_netP_expected),Double.parseDouble(t_netP_actual),"Net Premium"),"Mismatched Net Premium Values");
		//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_NetPremium",netP_expected,common.NB_excel_data_map),"Error while writing Net Premium for cover "+code+" to excel .");
		
		
		// Transaction Broker commission Calculation : 
		double t_broker_comm = ((Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate"))/100)));
		String t_bc_expected = common.roundedOff(Double.toString(t_broker_comm));
		String t_bc_actual =  Double.toString(transaction_Premium_Values.get(sectionNames).get("Broker Commission"));
		customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(t_bc_expected),Double.parseDouble(t_bc_actual),"Broker Commission"),"Mismatched Broker Commission Values");
		//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_BrokerComm",bc_expected,common.NB_excel_data_map),"Error while writing Broker Commission for cover "+code+" to excel .");
		
		
		// Transaction GrossPremium verification : 
		double t_grossP = Double.parseDouble(t_netP_expected) + Double.parseDouble(t_bc_expected);
		String t_grossP_actual = Double.toString(transaction_Premium_Values.get(sectionNames).get("Gross Premium"));
		customAssert.assertTrue(CommonFunction.compareValues(t_grossP,Double.parseDouble(t_grossP_actual),sectionNames+" Transaction Gross Premium"),"Mismatched Gross Premium Values");
		//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_GrossPremium",Double.toString(grossP),common.NB_excel_data_map),"Error while writing Gross Premium for cover "+code+" to excel .");
		
		
		double t_InsuranceTax = (t_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_IPT")))/100.0;
		t_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(t_InsuranceTax)));
		String t_InsuranceTax_actual = Double.toString(transaction_Premium_Values.get(sectionNames).get("Insurance Tax"));
		customAssert.assertTrue(CommonFunction.compareValues(t_InsuranceTax,Double.parseDouble(t_InsuranceTax_actual),"Insurance Tax"),"Mismatched Insurance Tax Values");
		//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_InsuranceTax",Double.toString(InsuranceTax),common.NB_excel_data_map),"Error while writing Total Premium for cover "+code+" to excel .");
		
		//  Transaction Total Premium verification : 
		double t_Premium = t_grossP + t_InsuranceTax;
		String t_p_expected = common.roundedOff(Double.toString(t_Premium));
		
		if(!sectionNames.contains("Terrorism") && !sectionNames.contains("PersonalAccidentOptional")){
			InsTaxDocAct = InsTaxDocAct + Double.parseDouble(t_InsuranceTax_actual);
			PremiumExcTerrDocAct = PremiumExcTerrDocAct + Double.parseDouble(t_grossP_actual);
			
			}
		if(!sectionNames.contains("Terrorism")){
			
		}else{
//			AdditionalTerPDocAct = Double.parseDouble(t_grossP_actual);
	}

		
		String t_p_actual = Double.toString(transaction_Premium_Values.get(sectionNames).get("Total Premium"));
		
		double premium_diff = Double.parseDouble(t_p_expected) - Double.parseDouble(t_p_actual);
		
		if(premium_diff<0.09 && premium_diff>-0.09){
			TestUtil.reportStatus("Total Premium [<b> "+t_p_expected+" </b>] matches with actual total premium [<b> "+t_p_actual+" </b>]as expected for "+sectionNames+" in Transaction Premium table .", "Pass", false);
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_expected,common.NB_excel_data_map),"Error while writing Total Premium for cover "+code+" to excel .");
			return 0;
			
		}else{
			TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+t_p_expected+"</b>] and Actual Premium [<b> "+t_p_actual+"</b>] for "+sectionNames+" in Transaction Premium table . </p>", "Fail", true);
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_expected,common.NB_excel_data_map),"Error while writing Total Premium for cover "+code+" to excel .");
			return 1;
		}
			
}catch(Throwable t) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
    Assert.fail("Transaction Premium verification issue.  \n", t);
    return 1;
}
	
}

public boolean funcInsuredProperties(Map<Object, Object> map_data){
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
	
	boolean r_Value = true;
	try{
		customAssert.assertTrue(common.funcPageNavigation("Insured Properties", ""),"Insured Properties page is having issue(S)");
		if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common.deleteItems(),"Delete Items function is having issues.");
	      }
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
			TestUtil.reportStatus("Insured Property  <b>[  "+internal_data_map.get("Property Details").get(count).get("Automation Key")+"  ]</b>  added successfully . ", "Info", true);
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
		
		if(((String)map_data.get("CD_LossOfRentalIncome")).equalsIgnoreCase("Yes")){
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
		
		if(((String)map_data.get("CD_LossOfRentalIncome")).equalsIgnoreCase("Yes")){
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
	
	List<WebElement> listOfRows = driver.findElements(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr"));
	int innerBeSpokeCount = 1;//common.no_of_inner_data_sets("Be Spkoe column name"); ---> this can be used in case of Multiple Bespoke item added.
	for(int i=0;i<(innerBeSpokeCount+listOfRows.size()-1)-1;i++){
		String Abvr = "";
		String sectionName = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[1]")).getText();
		if(sectionName.equalsIgnoreCase(internal_data_map.get("Property Details").get(count).get("BSI_MD_Description"))){
			Abvr = "BSI_MD_";
		}else if(sectionName.equalsIgnoreCase(internal_data_map.get("Property Details").get(count).get("BSI_LOI_Description"))){
			Abvr = "BSI_LOI_";
		}else{
			Abvr = CommonFunction.func_GetAbrrivation(coverInitial, sectionName);
			customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[2]/input" ,internal_data_map , "Property Details", count , Abvr , "DeclaredValue", sectionName,"Input"),"BAC");
		}
			if(coverInitial.equalsIgnoreCase("MD")){
				
				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[4]/input" , internal_data_map , "Property Details", count , Abvr , "FireRate",sectionName, "Input"),"BAC");
	    		customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[5]/input" , internal_data_map , "Property Details", count , Abvr , "PerilsRate",sectionName, "Input"),"BAC");
	    		customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[6]/input" , internal_data_map , "Property Details", count , Abvr , "SprinkRate",sectionName, "Input"),"BAC");
	    		customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[7]/input" , internal_data_map , "Property Details", count , Abvr , "ADRate", sectionName,"Input"),"BAC");
	    		customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[8]/input" , internal_data_map , "Property Details", count , Abvr , "SubsRate",sectionName, "Input"),"BAC");
	    		customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[10]/input", internal_data_map , "Property Details", count , Abvr , "TechAdjust",sectionName, "Input"),"BAC");
	    		customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[11]/input" , internal_data_map , "Property Details", count , Abvr , "CommAdjust", sectionName,"Input"),"BAC");
	    		
			}
			if(coverInitial.equalsIgnoreCase("LOI")){
				
				customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[5]/input" , internal_data_map , "Property Details", count , Abvr , "FireRate",sectionName, "Input"),"BAC");
	    		customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[6]/input" , internal_data_map , "Property Details", count , Abvr , "PerilsRate",sectionName, "Input"),"BAC");
	    		customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[7]/input" , internal_data_map , "Property Details", count , Abvr , "SprinkRate",sectionName, "Input"),"BAC");
	    		customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[8]/input" , internal_data_map , "Property Details", count , Abvr , "ADRate", sectionName,"Input"),"BAC");
	    		customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[9]/input" , internal_data_map , "Property Details", count , Abvr , "SubsRate",sectionName, "Input"),"BAC");
	    		customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[11]/input", internal_data_map , "Property Details", count , Abvr , "TechAdjust",sectionName, "Input"),"BAC");
	    		customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, "html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[12]/input" , internal_data_map , "Property Details", count , Abvr , "CommAdjust", sectionName,"Input"),"BAC");
			}
	}
	return true;
}

public boolean calculatePremium(int count , String coverInitial , int tableIndex) throws Throwable, Exception {
	
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
	
	List<WebElement> listOfRows = driver.findElements(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr"));
	double MD_Premium = 0.0 , LOI_Premium = 0.0;
	int innerBeSpokeCount = 1;//common.no_of_inner_data_sets("Be Spkoe column name"); ---> this can be used in case of Multiple Bespoke item added.
	for(int i=0;i<(innerBeSpokeCount+listOfRows.size()-1)-1;i++){
		String Abvr = "";
		JavascriptExecutor j_exe = (JavascriptExecutor) driver;
		j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[1]")));
		String sectionName = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[1]")).getText();
		if(sectionName.equalsIgnoreCase(internal_data_map.get("Property Details").get(count).get("BSI_MD_Description"))){
			Abvr = "BSI_MD_";
		}else if(sectionName.equalsIgnoreCase(internal_data_map.get("Property Details").get(count).get("BSI_LOI_Description"))){
			Abvr = "BSI_LOI_";
		}else{
			Abvr = CommonFunction.func_GetAbrrivation(coverInitial, sectionName);
		}
		if(coverInitial.equalsIgnoreCase("MD")){
			
			String actPremium = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[13]/input")).getAttribute("value");
			double totalRate = Double.parseDouble(internal_data_map.get("Property Details").get(count).get(Abvr+"FireRate")) + 
					Double.parseDouble(internal_data_map.get("Property Details").get(count).get(Abvr+"PerilsRate")) + 
					Double.parseDouble(internal_data_map.get("Property Details").get(count).get(Abvr+"SprinkRate")) + 
					Double.parseDouble(internal_data_map.get("Property Details").get(count).get(Abvr+"ADRate")) + 
					Double.parseDouble(internal_data_map.get("Property Details").get(count).get(Abvr+"SubsRate"));
			double commAdjustment = ((totalRate * Double.parseDouble(internal_data_map.get("Property Details").get(count).get(Abvr+"TechAdjust"))) / 100.0 ) + totalRate ; 
			double adjustedRate =  (commAdjustment * Double.parseDouble(internal_data_map.get("Property Details").get(count).get(Abvr+"CommAdjust")) / 100.0 ) + commAdjustment;
			String expPremium = common.roundedOff(Double.toString(((Double.parseDouble(internal_data_map.get("Property Details").get(count).get(Abvr+"DeclaredValue")) * adjustedRate ) / 100.0)));
			customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expPremium),Double.parseDouble(actPremium),"MD premium"),"Mismatched MD premium Values");
			MD_Premium = MD_Premium + Double.parseDouble(expPremium);
			
			if(driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+2)+"]/td[12]")).getText().equalsIgnoreCase("Total")){
				String actTotalPremium = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+2)+"]/td[13]/input")).getAttribute("value");
				String expTotalPremium = Double.toString(MD_Premium);
				customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expTotalPremium),Double.parseDouble(actTotalPremium),"MD premium"),"Mismatched MD premium Values");
				TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Property Details", internal_data_map.get("Property Details").get(count).get("Automation Key"), "MD_TotalPremium", Double.toString(MD_Premium),internal_data_map.get("Property Details").get(count));
				MD_TotalPremium = MD_TotalPremium + MD_Premium;
			}
			
		}
		if(coverInitial.equalsIgnoreCase("LOI")){
			
			String actPremium = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[14]/input")).getAttribute("value");
			double totalRate = Double.parseDouble(internal_data_map.get("Property Details").get(count).get(Abvr+"FireRate")) + 
					Double.parseDouble(internal_data_map.get("Property Details").get(count).get(Abvr+"PerilsRate")) + 
					Double.parseDouble(internal_data_map.get("Property Details").get(count).get(Abvr+"SprinkRate")) + 
					Double.parseDouble(internal_data_map.get("Property Details").get(count).get(Abvr+"ADRate")) + 
					Double.parseDouble(internal_data_map.get("Property Details").get(count).get(Abvr+"SubsRate"));
			double commAdjustment = ((totalRate * Double.parseDouble(internal_data_map.get("Property Details").get(count).get(Abvr+"TechAdjust"))) / 100.0 ) + totalRate ; 
			double adjustedRate =  (commAdjustment * Double.parseDouble(internal_data_map.get("Property Details").get(count).get(Abvr+"CommAdjust")) / 100.0 ) + commAdjustment;
			String expPremium = common.roundedOff(Double.toString(((Double.parseDouble(internal_data_map.get("Property Details").get(count).get(Abvr+"DeclaredValue")) * adjustedRate ) / 100.0)));
			customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expPremium),Double.parseDouble(actPremium),"MD premium"),"Mismatched MD premium Values");
			LOI_Premium = LOI_Premium + Double.parseDouble(expPremium);
			
			if(driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+2)+"]/td[13]")).getText().equalsIgnoreCase("Total")){
				String actTotalPremium = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+2)+"]/td[14]/input")).getAttribute("value");
				String expTotalPremium = Double.toString(LOI_Premium);
				customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expTotalPremium),Double.parseDouble(actTotalPremium),"LOI premium"),"Mismatched LOI premium Values");
				TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Property Details", internal_data_map.get("Property Details").get(count).get("Automation Key"), "LOI_TotalPremium", Double.toString(LOI_Premium),internal_data_map.get("Property Details").get(count));
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
		Map<String, List<Map<String, String>>> data_map = null;
		switch(common.currentRunningFlow){
		case "NB":
			data_map = common.NB_Structure_of_InnerPagesMaps;
		break;
		case "CAN":
			data_map = common.CAN_Structure_of_InnerPagesMaps;
		break;
		case "MTA":
			data_map = common.MTA_Structure_of_InnerPagesMaps;
		break;
		case "Renewal":
			data_map = common.Renewal_Structure_of_InnerPagesMaps;
		break;
		case "Rewind":
			data_map = common.Rewind_Structure_of_InnerPagesMaps;
		break;
		case "Requote":
			data_map = common.Requote_Structure_of_InnerPagesMaps;
		break;
	}
	
		int total_count_PL_items = common.no_of_inner_data_sets.get("POL AddItem");
		int count=0;
		while(count < total_count_PL_items){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on PL page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", data_map.get("POL AddItem").get(count).get("AD_POL_ItemDesc")),"Unable to enter value in Description field. ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", Keys.chord(Keys.CONTROL, "a")),"Unable to select Sum Insured field");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", data_map.get("POL AddItem").get(count).get("AD_POL_ItemSumIns")),"Unable to enter value in Sum Insured field. ");
			
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


//---------------------------------------------ajinkya-------------------------------------------------

public void CancellationFlow(String code,String event) throws ErrorInTestMethod{
	
	String testName = (String)common.CAN_excel_data_map.get("Automation Key");
	try{
		NewBusinessFlow(code,"NB");
		//		System.out.println("Test method of CAN For - "+code);
//		System.out.println("CAN data Map - "+common.CAN_excel_data_map);
//		System.out.println("CAN inner data Map - "+common.CAN_Structure_of_InnerPagesMaps);
// 
		customAssert.assertTrue(CancellationProcess(common.NB_excel_data_map,code,event),"Failed in Cancellation Process Function");
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
	
	
}


public boolean CancellationProcess(Map<Object, Object> map_data,String code,String event) throws Throwable{
	
	try{
	
	Map<Object, Object> can_data=common.CAN_excel_data_map;
	customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"),"Unable to Navigate Premium Summary Screen");
	customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page is having issue(S)");
	
	String Total_Premium = k.getTextByXpath("html/body/div[3]/form/div/div[2]/table/tbody/tr[4]/td[3]").replaceAll(",", "");
	String pol_StartDt=k.getText("cancelStDt");
	String cancelEndDt=k.getText("cancelEndDt");
	
	customAssert.assertTrue(k.Click("cancel_btn"),"Unable to click on Cancel button on Premium summary page");
	customAssert.assertTrue(common.funcPageNavigation("Cancel Policy", ""),"Unable to load Cancel Policy page");
	int cnDays= Integer.parseInt((String)can_data.get("CancelAfterDays"));
	
	customAssert.assertTrue(pol_StartDt.contains(k.getText("cancelStDt")),"Start Date is different on cancellation page");
	customAssert.assertTrue(cancelEndDt.contains(k.getText("cancelEndDt")),"End Date is different on cancellation page");
	
	int idate= Integer.parseInt(CONFIG.getProperty("DaysIncrementNumber"));
	
	customAssert.assertTrue(k.Input("cancelDt", TestUtil.returnDate(cnDays+idate)));
	customAssert.assertTrue(k.Type("cancelRsn", "Test Cancellation"));
	can_data.put("cancelDate",TestUtil.returnDate(cnDays+idate));
	can_data.put("cancelReason","Test Cancellation");
	
	customAssert.assertTrue(k.Click("cancelCtnu"));
	k.waitTwoSeconds();
	customAssert.assertTrue(k.isDisplayed("cancelRemDayslbl"),"Unable to move cancel summary page ");
	TestUtil.reportStatus("Cancellation Summary Page loaded Successfully", "Info", true);
	

	customAssert.assertTrue(k.getText("cnclSummaryStDt").contentEquals(pol_StartDt),"Policy Start date is not matching on cancellation summary page");
	TestUtil.reportStatus("Cancellation Date is <b>"+k.getText("cancelDate")+ " with remaining days "+k.getText("cancelRemDays"), "Info", true);
	k.pressDownKeyonPage();
	
	String cAP_path1 = "html/body/div[3]/form/div/table[3]/tbody/tr[";
	int tblRow = driver.findElements(By.xpath("html/body/div[3]/form/div/table[3]/tbody/tr")).size();
	String cAP_path = cAP_path1+tblRow+"]/td[2]";
	String currentAP_Total= k.getTextByXpath(cAP_path).replaceAll(",", "");
	
	TestUtil.reportStatus("Current Annual Premium Total "+currentAP_Total, "Info", false);
	
	String canAP_path1 = "html/body/div[3]/form/div/table[4]/tbody/tr[";
	int tblRow1 = driver.findElements(By.xpath("html/body/div[3]/form/div/table[4]/tbody/tr")).size();
	String canAP_path = canAP_path1+tblRow1+"]/td[2]";
	String canAP_Total= k.getTextByXpath(canAP_path).replaceAll(",", "");
	TestUtil.reportStatus("Cancellation Return Premium Total "+canAP_Total, "Info", false);
	String can_GrandTotal=k.getText("can_GtTotal");
	TestUtil.reportStatus("Cancellation Grand Total "+can_GrandTotal, "Info", false);
	
	customAssert.assertTrue(CancellationPremiumTables(code, event, "AP", true),"CancellationPremiumTables function having issue");
	customAssert.assertTrue(CancellationPremiumTables(code, event, "CP", true),"CancellationPremiumTables function having issue");
	k.Click("cancelRecalc");
	customAssert.assertTrue(CancellationPremiumTables(code, event, "CP", false), "CancellationPremiumTables function having issue");
	can_GrandTotal=k.getText("can_GtTotal");
	TestUtil.reportStatus("Cancellation Grand Total after Re-Calculation <b>"+can_GrandTotal, "Info", false);
	
	// Click on Cancel Policy button
	
	k.Click("cancelPol");
	customAssert.assertTrue(k.AcceptPopup(),"Cancel Pop-up handelling failed");
	


	return true;}catch(NumberFormatException e){
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
    
    
    try{
    	
    	customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Navigation problem to Premium Summary page .");
    	customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page is not loaded to perfrm MTA . ");
    	   
    	customAssert.assertTrue(common.funcButtonSelection("Create Endorsement"), "Unable to click on Create Endorsement button .");
    	
    	
    	       int ammendmet_period= Integer.parseInt(data_map.get("MTA_EndorsementPeriod").toString());
    	
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
    	
    	TimeZone uk_Instance = TimeZone.getTimeZone("Europe/London");
    	Calendar c = Calendar.getInstance(uk_Instance);
    	if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
      		int d = Integer.parseInt(((String)data_map.get("PS_PolicyStartDate")).substring(0, 2));
      		dateobj = common.addDays(df.parse((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate")), ammendmet_period);
      	}else{
      		c.add(Calendar.DATE, ammendmet_period);
      	  	dateobj = df.parse(df.format(c.getTime()));
      	}
    	
    	customAssert.assertTrue(k.Click("POB_Endorsement_effective_date"), "Unable to enter Endorsement effective Date.");
        customAssert.assertTrue(k.Type("POB_Endorsement_effective_date", df.format(dateobj)), "Unable to Enter Endorsement effective Date .");
        customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
        if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
	      	WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "MTA_Endorsement", (String)data_map.get("Automation Key"), "MTA_EffectiveDate", k.getAttribute("SPI_Endorsement_eff_date", "value"),data_map);
	    }else{
	      	WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", (String)data_map.get("Automation Key"), "MTA_EffectiveDate", k.getAttribute("SPI_Endorsement_eff_date", "value"),data_map);
	    }
        customAssert.assertTrue(k.Input("POB_Reason_for_Endorsement", (String)data_map.get("MTA_ReasonforEndorsement")),"Unable to Enter Reason for Endorsement");
    	customAssert.assertTrue(k.Click("POB_Create_Endorsement_button"), "Unable to click on create Endorsement button ");
      //Writing to MTA Excel
    	WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "MTA_Endorsement", (String)data_map.get("Automation Key"), "MTA_PolicyNumber", k.getText("SPI_MTA_policy_number"),data_map);
    	
    	
    	if(common.currentRunningFlow.equals("MTA")){
    		if(((String)data_map.get("MTA_Status")).equals("Endorsement Rewind"))
    			WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "MTA_Endorsement", testName, "MTA_isMTARewind", "Y",data_map);
    		else
    			WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "MTA_Endorsement", testName, "MTA_isMTARewind", "N",data_map);
    	}
        
    	TestUtil.reportStatus("Create Endorsement Details filled successfully . ", "Info", true);
		
        return retvalue;
        
        
    }catch (ParseException e) {
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
					c_locator = coverWithLocator.split("_")[1];
					k.waitTwoSeconds();
					if(c_locator.equals("md")){
						
						
						if (!driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).isSelected()){
							if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("No"))
								continue;
							else
					 			customAssert.assertTrue(common.selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
						}else{
							if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("Yes"))
								continue;
							else
								customAssert.assertTrue(common.deSelectCovers(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
						}
					
					}else if(c_locator.equals("PEL")){
					
					}else if(c_locator.equals("cob_hcp")){
							if(((String)map_data.get("QC_AgencyName")).equalsIgnoreCase("Arthur J. Gallagher (UK) Ltd")){
								
								if (!driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).isSelected()){
									JavascriptExecutor j_exe = (JavascriptExecutor) driver;
									j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")));
									
										if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("No"))
											continue;
										else
								 			customAssert.assertTrue(common.selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
															
									}else{
										if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("Yes"))
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
							
								if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("No"))
									continue;
								else
						 			customAssert.assertTrue(common.selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
													
							}else{
								if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("Yes"))
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

//----------------------------------------------MTA Specific functions------------------------------------------------------

public boolean funcPremiumSummary_MTA(Map<Object, Object> map_data,String code,String event) {
	
	boolean r_value= true;
	AdjustedTaxDetails.clear();
	Date currentDate = new Date();
	String testName = (String)map_data.get("Automation Key");
	String customPolicyDuration=null;
	SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
	
	try{
		customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
		int policy_Duration = 0;
		String Policy_End_Date = "" , policy_Start_date="";
	
	switch(common.currentRunningFlow){
	
	case "MTA":
		
		policy_Duration = Integer.parseInt((String)map_data.get("PS_Duration"));
		policy_Duration--;
		policy_Start_date = k.getTextByXpath("//*[contains(text(),'Policy Start Date')]//following::div[1]");
		//policy_Start_date = common_VELA.get_PolicyStartDate((String)map_data.get("PS_PolicyStartDate"));
		map_data.put("PS_PolicyStartDate", policy_Start_date);
		Policy_End_Date = common.daysIncrementWithOutFormation((String)map_data.get("PS_PolicyStartDate"), policy_Duration);
		map_data.put("PS_PolicyEndDate", Policy_End_Date);
		if(((String)map_data.get("PS_DefaultStartEndDate")).equals("No")){
			//customAssert.assertTrue(k.Click("Policy_Start_Date"), "Unable to Click Policy_Start_Date date picker .");
			//customAssert.assertTrue(k.Input("Policy_Start_Date", (String)map_data.get("PS_PolicyStartDate")),"Unable to Enter Policy_Start_Date .");
			//customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
			customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
		}	
		k.waitTwoSeconds();
		
		customAssert.assertTrue(k.SelectRadioBtn("Payment_Warranty_rules", (String)map_data.get("PS_PaymentWarrantyRules")),"Unable to Select Payment_Warranty_rules radio button . ");
		
		k.waitTwoSeconds();
	break;
	
	case "Rewind":
		
		policy_Duration = Integer.parseInt((String)map_data.get("PS_Duration"));
		policy_Duration--;
		if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
			policy_Start_date = driver.findElement(By.xpath("//*[@id='start_date']")).getText();
		}else{
			policy_Start_date = driver.findElement(By.xpath("//*[contains(@name,'start_date')]")).getAttribute("value");
		}
		map_data.put("PS_PolicyStartDate", policy_Start_date);
		Policy_End_Date = common.daysIncrementWithOutFormation((String)map_data.get("PS_PolicyStartDate"), policy_Duration);
		map_data.put("PS_PolicyEndDate", Policy_End_Date);
		if(((String)map_data.get("PS_DefaultStartEndDate")).equals("No")){
			customAssert.assertTrue(k.Click("Policy_Start_Date"), "Unable to Click Policy_Start_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_Start_Date", (String)map_data.get("PS_PolicyStartDate")),"Unable to Enter Policy_Start_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
			customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
		}	
		k.waitTwoSeconds();
		
	break;
	case "Renewal":
		
		policy_Duration = Integer.parseInt((String)map_data.get("PS_Duration"));
		policy_Duration--;
		policy_Start_date = driver.findElement(By.xpath("//*[contains(@name,'start_date')]")).getAttribute("value");
		map_data.put("PS_PolicyStartDate", policy_Start_date);
		Policy_End_Date = common.daysIncrementWithOutFormation((String)map_data.get("PS_PolicyStartDate"), policy_Duration);
		map_data.put("PS_PolicyEndDate", Policy_End_Date);
		if(((String)map_data.get("PS_DefaultStartEndDate")).equals("No")){
			customAssert.assertTrue(k.Click("Policy_Start_Date"), "Unable to Click Policy_Start_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_Start_Date", (String)map_data.get("PS_PolicyStartDate")),"Unable to Enter Policy_Start_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
			customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
		}	
		k.waitTwoSeconds();
		
	break;
	}
	k.waitTwoSeconds();
	customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
	customPolicyDuration = k.getText("Policy_Duration");
	customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,map_data),"Error while writing Policy Duration data to excel .");
	if(common.currentRunningFlow.equalsIgnoreCase("Rewind"))
		TestUtil.reportStatus("MTA Rewind Policy Duration = "+customPolicyDuration+" Days . ", "Info", true);
	else if(common.currentRunningFlow.equalsIgnoreCase("Renewal"))
		TestUtil.reportStatus("Renewal Policy Duration = "+customPolicyDuration+" Days . ", "Info", true);
	else
		TestUtil.reportStatus("MTA Policy Duration = "+customPolicyDuration+" Days . ", "Info", true);
	
	customAssert.assertTrue(Verify_premiumSummaryTable(), "Error while verifying Premium Summary table in MTA  .");
	customAssert.assertTrue(insuranceTaxAdjustmentHandling(code,event), "Premium Summary function is having issue(S) . ");
	customAssert.assertTrue(Verify_premiumSummaryTable(), "Error while verifying Premium Summary table in MTA  .");
	
	if(common.currentRunningFlow.equalsIgnoreCase("MTA"))
		Assert.assertTrue(funcTransactionDetailsMessage_MTA());
	
	TestUtil.reportStatus("Premium Summary details are filled and Verified sucessfully . ", "Info", true);
	return r_value;
}catch(Throwable t){
		
		return false;
	}
}

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


public boolean func_MTATransactionDetailsPremiumTable(String code, String event){
	//Transaction Premium Table
	
		try{
			
			boolean isMTARewindFPEntries=false;
			String testName = (String)common.MTA_excel_data_map.get("Automation Key");
			k.pressDownKeyonPage();
			customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
			
			if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
				int policy_Duration = Integer.parseInt((String)common.Renewal_excel_data_map.get("PS_Duration"));
			}else{
				int policy_Duration = Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"));
			}
			
			
			String transactionDetailsTble_xpath = "//p[text()=' Transaction Details ']//following-sibling::table[@id='table0']";
			WebElement transactionDetails_Table = driver.findElement(By.xpath(transactionDetailsTble_xpath));
			
			List<WebElement> colms = transactionDetails_Table.findElements(By.tagName("th"));
				//Map<SPI,MAP<NNP,12345.67>>
			
			int trans_tble_Rows = transactionDetails_Table.findElements(By.tagName("tr")).size();
			int trans_tble_Cols = colms.size();
			
			List<String> sectionNames = new ArrayList<>();
			String sectionName = null;
			String sectionValue = null;
			String headerName = null;
			String isFP_Text = "No";boolean FP_Entry=false;
			
			if(transactionDetails_Table.isDisplayed()){
				
				if(common_POB.isMTARewindFlow)
					TestUtil.reportStatus("Verification Started for Transaction Details table on premium summary page after Endorsement(MTA) Rewind . ", "Info", true);
				else
					TestUtil.reportStatus("Verification Started for Transaction Details table on premium summary page after Endorsement(MTA) . ", "Info", true);
				//For Each Cover Row
				For:
				for(int row = 1; row < trans_tble_Rows ;row ++){
					
					WebElement sec_Name = driver.findElement(By.xpath(transactionDetailsTble_xpath+"//tbody//tr["+row+"]//td["+1+"]"));
					sectionName = sec_Name.getText();
					
					if(sectionName.equalsIgnoreCase("Totals")){
						Map<String,Double> transaction_Section_Vals_Total = new HashMap<>();
						//For Each Cols
						for(int col = 2; col <= trans_tble_Cols ;col ++){
					
							////p[text()=' Transaction Premium']//following-sibling::table[@id='table0']//thead//th[2]
							WebElement header_Name = driver.findElement(By.xpath(transactionDetailsTble_xpath+"//thead//th["+col+"]"));
							headerName = header_Name.getText();
						
							if(!headerName.contains("Com. Rate (%)")){
								WebElement sec_Val = driver.findElement(By.xpath(transactionDetailsTble_xpath+"//tbody//tr["+row+"]//td["+col+"]"));
								sectionValue = sec_Val.getText();
								sectionValue = sectionValue.replaceAll(",", "");
								transaction_Section_Vals_Total.put(headerName, Double.parseDouble(sectionValue));
								
							}else{
								continue;
							}
							if(common_POB.isMTARewindFlow){
								//common.transaction_Details_Premium_Values.clear();
								common.transaction_Details_Premium_Values.remove(sectionName);
							}
							common.transaction_Details_Premium_Values.put(sectionName, transaction_Section_Vals_Total);
					}
					}
					else if(!FP_Entry && !sectionName.contains("Flat")){
						
						Map<String,Double> transaction_Section_Vals = new HashMap<>();
						//For Each Cols
						for(int col = 2; col <= trans_tble_Cols ;col ++){
					
							////p[text()=' Transaction Premium']//following-sibling::table[@id='table0']//thead//th[2]
							WebElement header_Name = driver.findElement(By.xpath(transactionDetailsTble_xpath+"//thead//th["+col+"]"));
							headerName = header_Name.getText();
						
							WebElement sec_Val = driver.findElement(By.xpath(transactionDetailsTble_xpath+"//tbody//tr["+row+"]//td["+col+"]"));
							sectionValue = sec_Val.getText();
						
							transaction_Section_Vals.put(headerName, Double.parseDouble(sectionValue));
					}
						
						if(common_POB.isMTARewindFlow){
							//common.transaction_Details_Premium_Values.clear();
							common.transaction_Details_Premium_Values.remove(sectionName);
						}
						common.transaction_Details_Premium_Values.put(sectionName, transaction_Section_Vals);
					
						
					}else if(sectionName.contains("Flat")){
						FP_Entry=true;
					}else if(FP_Entry){
						continue;
					}
					
					
					
				}
				//System.out.println(transaction_Premium_Values);
				
				if(common_POB.isMTARewindFlow){
					TestUtil.reportStatus("---------------Transaction Details table Verification after Rewind Endorsement(MTA)-----------------","Info",false);
				}else{
					TestUtil.reportStatus("---------------Transaction Details table Verification in MTA-----------------","Info",false);
				}
				//Transaction table Verification
				
				// Check if Flat premium is added or not :
				
				String flatPremium = (String)common.MTA_excel_data_map.get("FP_isFlatPremium");
				String flatPremiumEntries = null; 
									
				if(flatPremium.contains("Yes")){
					flatPremiumEntries = (String)common.MTA_excel_data_map.get("FP_FlatPremium_Entries");
				}			
				String[] arrF_Premium = null;
				
				if(flatPremiumEntries != null){
					
						arrF_Premium = flatPremiumEntries.split(";");
				
						for(int i = 0; i < arrF_Premium.length; i ++){
					
						if(i == 0){
							FP_Covers = (String)common.MTA_Structure_of_InnerPagesMaps.get("Flat-Premiums").get(i).get("FP_Section");
						}else{
							FP_Covers = FP_Covers + ","+ (String)common.MTA_Structure_of_InnerPagesMaps.get("Flat-Premiums").get(i).get("FP_Section");
						}					
					}
				}
				
				for(int row = 1; row < trans_tble_Rows ;row ++){
					WebElement sec_Name = driver.findElement(By.xpath(transactionDetailsTble_xpath+"//tbody//tr["+row+"]//td["+1+"]"));
					sectionNames.add(sec_Name.getText());
				}
				for(String s_Name : sectionNames){
					
					isFP_Text = "No";
					//FP Entries
					if(s_Name.contains("Flat")){
						common_CCD.isFPEntries = true;isFP_Text="Yes";
						
						if(common_CCD.isMTARewindFlow){
							isMTARewindFPEntries=true;}
					}
					
					if(common_CCD.isFPEntries && isFP_Text.equalsIgnoreCase("No") && !s_Name.equals("Totals")){
						
						trans_error_val = trans_error_val + func_FP_Entries_Transaction_Details_Verification_MTA(s_Name,common.MTA_Structure_of_InnerPagesMaps);
						
						
					}else{
						if(s_Name.equals("Totals"))
							trans_error_val = trans_error_val + funcTransactionDetailsTable_Verification_Total_MTA(sectionNames,common.transaction_Details_Premium_Values);
						else if(!s_Name.contains("Flat"))
							trans_error_val = trans_error_val + funcTransactionDetailsTable_Verification_MTA(s_Name,common.transaction_Details_Premium_Values);
						
					}			
				}
				if(common_CCD.isMTARewindFlow){
					
					if(flatPremium.equalsIgnoreCase("Yes") && arrF_Premium.length > 0){
						if(!isMTARewindFPEntries){
							TestUtil.reportStatus("<p style='color:red'> Flat Premium Entries added in MTA Flow are not present while doing MTA Rewind in Transaction Details table . </p>", "Fail", true);
							ErrorUtil.addVerificationFailure(new Throwable("Flat Premium Entries added in MTA Flow are not present while doing MTA Rewind in Transaction Details table . "));
						}
					}
					
					TestUtil.reportStatus("Transaction Details table has been verified suceesfully after Rewind Endorsement . ", "info", true);
				}else{
					TestUtil.reportStatus("Transaction Details table has been verified suceesfully . ", "info", true);
				}
				
			}
			
		
				//Total Premium With Admin Fees
				double total_premium_with_admin_fee = common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium") + 
						common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax") ;
				
				
				String exp_Total_Premium_with_Admin_fee = common.roundedOff(Double.toString(total_premium_with_admin_fee));
				k.waitTwoSeconds();
				
				String xPath = "//table[@id='table0']//*//td[text()='Total']//following-sibling::td";
				String act_Total_Premium_with_Admin_fee = k.getTextByXpath(xPath);
				
				act_Total_Premium_with_Admin_fee = act_Total_Premium_with_Admin_fee.replaceAll(",", "");
				double premium_diff = Double.parseDouble(common.roundedOff(Double.toString(Double.parseDouble(exp_Total_Premium_with_Admin_fee) - Double.parseDouble(act_Total_Premium_with_Admin_fee))));
				
				TestUtil.reportStatus("---------------Endorsement Premium Summary with Admin Fees-----------------","Info",false);
				
				if(Math.abs(premium_diff)<=0.09){
					TestUtil.reportStatus("Total Premium with Admin Fees :[<b> "+exp_Total_Premium_with_Admin_fee+" </b>] matches with actual premium [<b> "+act_Total_Premium_with_Admin_fee+"</b>]as expected with some difference upto '0.05' on premium summary page.", "Pass", false);
					customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_TotalFinalPremium", exp_Total_Premium_with_Admin_fee,common.MTA_excel_data_map),"Error while writing Total Final Premium data to excel .");
				}else{
					TestUtil.reportStatus("Mismatch in Expected Total Premium with Admin Fees [<b> "+exp_Total_Premium_with_Admin_fee+"</b>] and Actual Premium [<b> "+act_Total_Premium_with_Admin_fee+"</b>] on premium summary page.", "Fail", false);
					customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_TotalFinalPremium", exp_Total_Premium_with_Admin_fee,common.MTA_excel_data_map),"Error while writing Total Final Premium data to excel .");
				}
			
			
		}catch(Throwable t ){
			return false;
		}
		
		return true;
}




public boolean funcTransactionDetailsMessage_MTA(){
	
	try{
	// Amendment Effective From : 22/06/2017, Period: 355 days.
		String t_Act_Message = null,t_Exp_Message = null;
	int MTA_duration = Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"));
	String Amend_Eff_Date = (String)common.MTA_excel_data_map.get("MTA_EffectiveDate");
	
	String transactionDetailsMsg_xpath = "//p[text()=' Transaction Details ']//following-sibling::p";
	WebElement transactionDetails_Msg = driver.findElement(By.xpath(transactionDetailsMsg_xpath));
	
	t_Act_Message = transactionDetails_Msg.getText();
	
	t_Exp_Message = "Amendment Effective From : "+Amend_Eff_Date+", Period: "+MTA_duration+" days.";
	common.MTA_excel_data_map.put("MTA_Duration",MTA_duration);
	customAssert.assertEquals(t_Act_Message, t_Exp_Message,"Mismatch in Transaction Details table Message: Expected: "+t_Exp_Message+" and Actual: "+t_Act_Message+" . ");
	
	TestUtil.reportStatus(t_Exp_Message, "Pass", false);
	
	}catch(Throwable t){
		return false;
	}
	return true;
	
		
}


public int func_FP_Entries_Transaction_Details_Verification_MTA(String sectionName,Map<String, List<Map<String, String>>> internal_data_map){

	Map<Object,Object> map_data = common.MTA_excel_data_map;
	Map<Object,Object> NB_map_data = common.NB_excel_data_map;
	Map<Object, Object> data_map = null;
	
	
	double final_fp_NNP=0.0;
	String code=null,cover_code=null;
	String flat_section=null;
	
	Map<String,Double> fp_details_values = new HashMap<>();
	
	switch (TestBase.businessEvent) {
	case "Renewal":
		data_map = common.Renewal_excel_data_map;
		break;
	case "MTA":
		data_map = common.NB_excel_data_map;
		break;
	default:
		break;
	}
	
	
	
		
	switch(sectionName){
	
	case "Material Damage":
		code = "MaterialDamage";
		cover_code = "MaterialDamage";
		flat_section = sectionName;
		break;
	case "Businesss Interruption":
		
		code = "BusinessInterruption";
		cover_code = "BusinessInterruption";
		flat_section="Business Interruption";
		break;
	case "Money & Assault":
		code = "Money&Assault";
		cover_code = "Money&Assault";
		flat_section = sectionName;
		break;
	case "Employers Liability":
		code = "Employers Liability";
		cover_code = "Employers Liability";
		flat_section = sectionName;
		break;
	case "Public Liability":
		code = "PublicLiability";
		cover_code = "PublicLiability";
		flat_section = sectionName;
		break;
	case "Personal Accident":
		code = "PersonalAccidentStandard";
		cover_code = "PersonalAccidentStandard";
		flat_section = sectionName;
		break;
	case "Personal Accident Optional":
		code = "PersonalAccidentOptional";
		cover_code = "PersonalAccidentOptional";
		flat_section = sectionName;
		break;
	case "Goods in Transit":
		code = "GoodsinTransit";
		cover_code = "GoodsInTransit";
		flat_section = "Goods In Transit";
		break;
	case "Legal Expenses":
		code = "LegalExpenses";
		cover_code = "LegalExpenses";
		flat_section = sectionName;
		break;
	case "Terrorism":
		code = "Terrorism";
		cover_code = "Terrorism";
		flat_section = sectionName;
		break;
	default:
			System.out.println("**Cover Name is not in Scope for POF**");
		break;
	
	}
	
try{
		
			TestUtil.reportStatus("---------------"+sectionName+" in Flat Premium Section-----------------","Info",false);
		
			//final_fp_NNP = Double.parseDouble(internal_data_map.get("Flat-Premiums").get(count-1).get("FP_Premium"));
		
			//final_fp_NNP = common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Net Net Premium");
			final_fp_NNP = (Double)map_data.get(flat_section+"_FP");
		
			String t_NetNetP_expected = common.roundedOff(Double.toString(final_fp_NNP));
			String t_NetNetP_actual = Double.toString(common.transaction_Details_Premium_Values.get(flat_section+"_FP").get("Net Net Premium"));
			CommonFunction.compareValues(Double.parseDouble(t_NetNetP_expected),Double.parseDouble(t_NetNetP_actual)," Net Net Premium");
			//fp_details_values.put("Net Net Premium", Double.parseDouble(t_NetNetP_expected));
			
			double t_pen_comm = (( Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate"))/100)));
			String t_pc_expected = common.roundedOff(Double.toString(t_pen_comm));
			String t_pc_actual = Double.toString(common.transaction_Details_Premium_Values.get(flat_section+"_FP").get("Pen Comm"));
			CommonFunction.compareValues(Double.parseDouble(t_pc_expected),Double.parseDouble(t_pc_actual)," Pen Commission");
			//fp_details_values.put("Pen Comm", Double.parseDouble(t_pc_expected));
			
			
			double t_netP = Double.parseDouble(t_pc_expected) + Double.parseDouble(t_NetNetP_expected);
			String t_netP_expected = common.roundedOff(Double.toString(t_netP));
			String t_netP_actual = Double.toString(common.transaction_Details_Premium_Values.get(flat_section+"_FP").get("Net Premium"));
			CommonFunction.compareValues(Double.parseDouble(t_netP_expected),Double.parseDouble(t_netP_actual),"Net Premium");
			//fp_details_values.put("Net Premium", Double.parseDouble(t_netP_expected));
			
			
			double t_broker_comm = ((Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate"))/100)));
			String t_bc_expected = common.roundedOff(Double.toString(t_broker_comm));
			String t_bc_actual =  Double.toString(common.transaction_Details_Premium_Values.get(flat_section+"_FP").get("Broker Commission"));
			CommonFunction.compareValues(Double.parseDouble(t_bc_expected),Double.parseDouble(t_bc_actual),"Broker Commission");
			//fp_details_values.put("Broker Commission", Double.parseDouble(t_bc_expected));
			
			
			double t_grossP = Double.parseDouble(t_netP_expected) + Double.parseDouble(t_bc_expected);
			String t_grossP_actual = Double.toString(common.transaction_Details_Premium_Values.get(flat_section+"_FP").get("Gross Premium"));
			CommonFunction.compareValues(t_grossP,Double.parseDouble(t_grossP_actual)," Gross Premium");
			//fp_details_values.put("Gross Premium", t_grossP);
			
			
			double t_InsuranceTax = (t_grossP * common.transaction_Details_Premium_Values.get(flat_section+"_FP").get("Insurance Tax Rate"))/100.0;
			
			t_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(t_InsuranceTax)));
			String t_InsuranceTax_actual = Double.toString(common.transaction_Details_Premium_Values.get(flat_section+"_FP").get("Insurance Tax"));
			//fp_details_values.put("Insurance Tax", t_InsuranceTax);
			
			//SPI  Transaction Total Premium verification : 
			double t_Premium = t_grossP + t_InsuranceTax;
			String t_p_expected = common.roundedOff(Double.toString(t_Premium));
			//fp_details_values.put("Total Premium", Double.parseDouble(t_p_expected));
			
			String t_p_actual = Double.toString(common.transaction_Details_Premium_Values.get(flat_section+"_FP").get("Total Premium"));
			
			//common.transaction_Details_Premium_Values.put(sectionName+"_FP", fp_details_values);
			
			// Add Flat premium values  to the MTA premium values to verify on documents :
			
			String sCover = null;
			
			if(sectionName.contains("Property Owners Liabilities")){
				sCover = "Liabilities - POL";
			}else if(sectionName.contains("Employers Liabilities")){
				sCover = "Liabilities - EL";
			}else{
				sCover = sectionName;
			}
						
			if(FP_Covers.contains(sCover) ){
				if(!sectionName.contains("Terrorism")){
					AdditionalExcTerrDocAct  = AdditionalExcTerrDocAct  + Double.parseDouble(t_grossP_actual);
				}else{
					AdditionalTerPDocAct  = AdditionalTerPDocAct  + Double.parseDouble(t_grossP_actual);
				}

				if(sectionName.contains("Terrorism")){
					AddTaxTerrDoc  = AddTaxTerrDoc  + Double.parseDouble(t_InsuranceTax_actual);						
				}
				
				String Nb_Status = (String)map_data.get("MTA_Status");
				if(common_POF.isMTARewindStarted){
					
					String newCover = "";
					if(sectionName.contains("PropertyOwnersLiabilities")){
						newCover = "Liabilities-POL";
					}else if(sectionName.contains("EmployersLiability")) {
						newCover = "Liabilities-EL";
					}else{
						newCover = sectionName;
					}
					
					String s_Cover = (String)map_data.get("CD_Add_"+newCover);
					
					if(sCover.contains("Yes")){
						rewindDoc_InsPTax  = rewindDoc_InsPTax + Double.parseDouble(t_InsuranceTax_actual);
						if(!sectionName.contains("Terrorism")){
							rewindMTADoc_Premium   = rewindMTADoc_Premium  + Double.parseDouble(t_grossP_actual);
						}else{
							rewindMTADoc_TerP   = Double.parseDouble(t_grossP_actual);
						}
					}
					
					
					
				}				
			}
			AdditionalInsTaxDocAct = AdditionalInsTaxDocAct + Double.parseDouble(t_InsuranceTax_actual);
			
			double premium_diff = Double.parseDouble(t_p_expected) - Double.parseDouble(t_p_actual);
			
			if(premium_diff<0.05 && premium_diff>-0.05){
				TestUtil.reportStatus("Total Premium [<b> "+t_p_expected+" </b>] matches with actual total premium [<b> "+t_p_actual+" </b>]as expected for "+sectionName+" in Flat Premium table .", "Pass", false);
				//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_expected,common.NB_excel_data_map),"Error while writing Total Premium for cover "+code+" to excel .");
				return 0;
			}else{
				TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+t_p_expected+"</b>] and Actual Premium [<b> "+t_p_actual+"</b>] for "+sectionName+" in Flat Premium table . </p>", "Fail", true);
				//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_expected,common.NB_excel_data_map),"Error while writing Total Premium for cover "+code+" to excel .");
				return 1;
			}
			
	
		
		
		
			
}catch(Throwable t) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
    Assert.fail("Transaction Premium verification issue.  \n", t);
    return 1;
}

	
}

public int funcTransactionDetailsTable_Verification_Total_MTA(List<String> sectionNames,Map<String,Map<String,Double>> transaction_Premium_Values){
	
	try{
		
	Map<String,Double> trans_details_values = new HashMap<>();
	boolean Start_Fp = false;
	
	TestUtil.reportStatus("---------------Totals In Transaction Details Table-----------------","Info",false);
	double exp_value = 0.0;
	outer:
	for(String section : sectionNames){
		
		if(section.contains("Flat")){
			Start_Fp = true;
			continue;
		}
		
		if(!section.contains("Total") && !section.contains("Flat") && !Start_Fp){
			try{
				exp_value = exp_value + transaction_Premium_Values.get(section).get("Net Net Premium");
			}catch(Throwable t){
				continue;
			}
		}else if(Start_Fp && !section.contains("Total")){
		//for(String _section : sectionNames){
			if(common_POF.isFPEntries && !section.contains("Flat")){
				try{
					if(section.equalsIgnoreCase("Property Owners Liabilities"))
						section = "Liabilities - POL";
					if(section.equalsIgnoreCase("Businesss Interruption"))
						section = "Business Interruption";
					if(section.equalsIgnoreCase("Goods in Transit")){
						section = "Goods In Transit";
					}
				exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Net Net Premium");
			}catch(Throwable t){
				continue;
			}
			}
			//}
		//break outer;
		}
	}
	
	String t_NetNetP_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Net Net Premium"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_NetNetP_actual)," Net Net Premium");
	trans_details_values.put("Net Net Premium",exp_value);

	exp_value = 0.0;Start_Fp = false;
	for(String section : sectionNames){
		
		if(section.contains("Flat")){
			Start_Fp = true;
			continue;
		}
		
		if(!section.contains("Total") && !section.contains("Flat") && !Start_Fp){
			try{
				exp_value = exp_value + transaction_Premium_Values.get(section).get("Pen Comm");
			}catch(Throwable t){
				continue;
			}
		}else if(Start_Fp && !section.contains("Total")){
		//for(String _section : sectionNames){
			if(common_POF.isFPEntries && !section.contains("Flat")){
				try{
					if(section.equalsIgnoreCase("Property Owners Liabilities"))
						section = "Liabilities - POL";
					if(section.equalsIgnoreCase("Businesss Interruption"))
						section = "Business Interruption";
					if(section.equalsIgnoreCase("Goods in Transit")){
						section = "Goods In Transit";
					}
				exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Pen Comm");
			}catch(Throwable t){
				continue;
			}
			}
	}
	}
	String t_pc_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Pen Comm"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_pc_actual)," Pen Commission");
	trans_details_values.put("Pen Comm",exp_value);
	
	exp_value = 0.0;Start_Fp = false;
	for(String section : sectionNames){

		if(section.contains("Flat")){
			Start_Fp = true;
			continue;
		}
		
		if(!section.contains("Total") && !section.contains("Flat") && !Start_Fp){
			try{
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Net Premium");
			}catch(Throwable t){
				continue;
			}
		}else if(Start_Fp && !section.contains("Total")){
		//for(String _section : sectionNames){
			if(common_POF.isFPEntries && !section.contains("Flat")){
				try{
					if(section.equalsIgnoreCase("Property Owners Liabilities"))
						section = "Liabilities - POL";
					if(section.equalsIgnoreCase("Businesss Interruption"))
						section = "Business Interruption";
					if(section.equalsIgnoreCase("Goods in Transit")){
						section = "Goods In Transit";
					}
				exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Net Premium");
			}catch(Throwable t){
				continue;
			}
			}
	}
	}
	String t_netP_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Net Premium"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_netP_actual),"Net Premium");
	trans_details_values.put("Net Premium",exp_value);
	
	exp_value = 0.0;Start_Fp = false;
	for(String section : sectionNames){
		
		if(section.contains("Flat")){
			Start_Fp = true;
			continue;
		}
		
		if(!section.contains("Total") && !section.contains("Flat") && !Start_Fp){
			try{
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Broker Commission");
			}catch(Throwable t){
				continue;
			}
		}else if(Start_Fp && !section.contains("Total")){
		//for(String _section : sectionNames){
			if(common_POF.isFPEntries && !section.contains("Flat")){
				try{
					if(section.equalsIgnoreCase("Property Owners Liabilities"))
						section = "Liabilities - POL";
					if(section.equalsIgnoreCase("Businesss Interruption"))
						section = "Business Interruption";
					if(section.equalsIgnoreCase("Goods in Transit")){
						section = "Goods In Transit";
					}
				exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Broker Commission");
			}catch(Throwable t){
				continue;
			}
			}
	}
	}
	String t_bc_actual =  Double.toString(transaction_Premium_Values.get("Totals").get("Broker Commission"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_bc_actual),"Broker Commission");
	trans_details_values.put("Broker Commission",exp_value);
	
	exp_value = 0.0;Start_Fp = false;
	for(String section : sectionNames){
		
		if(section.contains("Flat")){
			Start_Fp = true;
			continue;
		}
		
		if(!section.contains("Total") && !section.contains("Flat") && !Start_Fp){
			try{
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Gross Premium");
			}catch(Throwable t){
				continue;
			}
		}else if(Start_Fp && !section.contains("Total")){
		//for(String _section : sectionNames){
			if(common_POF.isFPEntries && !section.contains("Flat")){
				try{
					if(section.equalsIgnoreCase("Property Owners Liabilities"))
						section = "Liabilities - POL";
					if(section.equalsIgnoreCase("Businesss Interruption"))
						section = "Business Interruption";
					if(section.equalsIgnoreCase("Goods in Transit")){
						section = "Goods In Transit";
					}
				exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Gross Premium");
			}catch(Throwable t){
				continue;
			}
			}
	}
	}
	String t_grossP_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Gross Premium"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_grossP_actual)," Gross Premium");
	trans_details_values.put("Gross Premium",exp_value);
	
	exp_value = 0.0;Start_Fp = false;
	for(String section : sectionNames){
		
		if(section.contains("Flat")){
			Start_Fp = true;
			continue;
		}
		
		if(!section.contains("Total") && !section.contains("Flat") && !Start_Fp){
			try{
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Insurance Tax");
			}catch(Throwable t){
				continue;
			}
		}else if(Start_Fp && !section.contains("Total")){
		//for(String _section : sectionNames){
			if(common_POF.isFPEntries && !section.contains("Flat")){
				try{
					if(section.equalsIgnoreCase("Property Owners Liabilities"))
						section = "Liabilities - POL";
					if(section.equalsIgnoreCase("Businesss Interruption"))
						section = "Business Interruption";
					if(section.equalsIgnoreCase("Goods in Transit")){
						section = "Goods In Transit";
					}
				exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Insurance Tax");
			}catch(Throwable t){
				continue;
			}
			}
	}
		
	}
	String t_InsuranceTax_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Insurance Tax"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_InsuranceTax_actual),"Insurance Tax");
	trans_details_values.put("Insurance Tax",exp_value);
	
	exp_value = 0.0;Start_Fp = false;
	for(String section : sectionNames){
		
		if(section.contains("Flat")){
			Start_Fp = true;
			continue;
		}
		
		if(!section.contains("Total") && !section.contains("Flat") && !Start_Fp){
			try{
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Total Premium");
			}catch(Throwable t){
				continue;
			}
		}else if(Start_Fp && !section.contains("Total")){
		//for(String _section : sectionNames){
			if(common_POF.isFPEntries && !section.contains("Flat")){
				try{
					if(section.equalsIgnoreCase("Property Owners Liabilities"))
						section = "Liabilities - POL";
					if(section.equalsIgnoreCase("Businesss Interruption"))
						section = "Business Interruption";
					if(section.equalsIgnoreCase("Goods in Transit")){
						section = "Goods In Transit";
					}
				exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Total Premium");
			}catch(Throwable t){
				continue;
			}
			}
	}
		
	}
	String t_p_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Total Premium"));
	
	trans_details_values.put("Total Premium",exp_value);
	
	common.transaction_Details_Premium_Values.put("Totals", trans_details_values);
	
	double premium_diff = exp_value - Double.parseDouble(t_p_actual);
	
	if(premium_diff<0.06 && premium_diff>-0.06){
		TestUtil.reportStatus("Total Premium [<b> "+exp_value+" </b>] matches with actual total premium [<b> "+t_p_actual+" </b>]as expected for Totals in Transaction Details table .", "Pass", false);
		return 0;
		
	}else{
		TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+exp_value+"</b>] and Actual Premium [<b> "+t_p_actual+"</b>] for Totals in Transaction Details table . </p>", "Fail", true);
		return 1;
	}
	
}catch(Throwable t) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
    Assert.fail("Transaction Details Premium total Section verification issue.  \n", t);
    return 1;
}
}

public int funcTransactionDetailsTable_Verification_MTA(String sectionName,Map<String,Map<String,Double>> transactionDetails_Premium_Values){

	Map<Object,Object> map_data = common.MTA_excel_data_map;
	
	Map<Object, Object> data_map = null;
	Map<Object,Object> Tax_map_data = new HashMap<>();
	
	double NB_NNP = 0.0;
	double MTA_NNP=0.0;
	double trans_NetNetP = 0.0,previous_mta=0.0,annualize_mta=0.0,_annualize_mta=0.0,final_trans_NNP=0.0;
	String code=null,cover_code=null;
	int p_NB_Duration = 0,p_MTA_Remaining_Duration=0 , p_MTA_Duration = 0;
	Map<String,Double> trans_details_values = new HashMap<>();
	
	switch (TestBase.businessEvent) {
	case "Renewal":
		data_map = common.Renewal_excel_data_map;
		break;
	case "MTA":
		if(common.currentRunningFlow.equalsIgnoreCase("Rewind")){
			data_map = common.Rewind_excel_data_map;
			Tax_map_data = common.MTA_excel_data_map;
			
		}else{
			data_map = common.NB_excel_data_map;
			Tax_map_data = common.NB_excel_data_map;
		}
		break;
	default:
		break;
	}
	
	
	if(Integer.parseInt((String)data_map.get("PS_Duration"))!=365)
		p_NB_Duration = 365;
	else
		p_NB_Duration = Integer.parseInt((String)data_map.get("PS_Duration"));
	
	
	
	p_MTA_Remaining_Duration = Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"));
	p_MTA_Duration = Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration"));
	switch(sectionName){
	
	case "Material Damage":
		code = "MaterialDamage";
		cover_code = "MaterialDamage";
		break;
	case "Businesss Interruption":
		code = "BusinessInterruption";
		cover_code = "BusinessInterruption";
		break;
	case "Money & Assault":
		code = "Money&Assault";
		cover_code = "Money&Assault";
		break;
	case "Employers Liability":
		code = "EmployersLiability";
		cover_code = "EmployersLiability";
		break;
	case "Public Liability":
		code = "PublicLiability";
		cover_code = "PublicLiability";
		break;
	case "Personal Accident":
		code = "PersonalAccident";
		cover_code = "PersonalAccidentStandard";
		break;
	case "Personal Accident Optional":
		code = "PersonalAccidentOptional";
		cover_code = "PersonalAccidentOptional";
		break;
	case "Goods in Transit":
		code = "GoodsinTransit";
		cover_code = "GoodsInTransit";
		break;
	case "Legal Expenses":
		code = "LegalExpenses";
		cover_code = "LegalExpenses";
		break;
	case "Terrorism":
		code = "Terrorism";
		cover_code = "Terrorism";
		break;
	
	default:
			System.out.println("**Cover Name is not in Scope for POF**");
		break;
	
	}
	
try{
		
		TestUtil.reportStatus("---------------"+sectionName+"-----------------","Info",false);
		
		if(common_CCD.isMTARewindFlow){ // MTA Rewind Flow
			
			if(Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"))!=Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration"))){
				
				
				//if(sectionName.contains("PI")){
					
					if(((String)common.NB_excel_data_map.get("CD_"+cover_code)).equals("Yes") && ((String)common.Rewind_excel_data_map.get("CD_"+cover_code)).equals("No"))
					{
						NB_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_"+code+"_NetNetPremium"));
						MTA_NNP = 0.0;
						map_data = common.NB_excel_data_map;
							
					}else if(((String)common.NB_excel_data_map.get("CD_"+cover_code)).equals("No") && ((String)common.Rewind_excel_data_map.get("CD_"+cover_code)).equals("Yes")){
						NB_NNP = 0.0;
						MTA_NNP = Double.parseDouble((String)common.Rewind_excel_data_map.get("PS_"+code+"_NetNetPremium"));
						map_data = common.Rewind_excel_data_map;
					}else{
						NB_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_"+code+"_NetNetPremium"));
						MTA_NNP = Double.parseDouble((String)common.Rewind_excel_data_map.get("PS_"+code+"_NetNetPremium"));
						map_data = common.NB_excel_data_map;
					}
					//Previous Premium MTA Calculation
					
					final_trans_NNP = ((MTA_NNP - NB_NNP)/p_NB_Duration)*
							((Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
						
			
			}else{
				
				//if(sectionName.contains("PI")){
					if(((String)common.NB_excel_data_map.get("CD_"+cover_code)).equals("Yes") && (((String)common.Rewind_excel_data_map.get("CD_"+cover_code)).equals("No")))
					{
						NB_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_"+code+"_NetNetPremium"));
						MTA_NNP = 0.0;
						map_data = common.NB_excel_data_map;
							
					}else if(((String)common.NB_excel_data_map.get("CD_"+cover_code)).equals("No") && ((String)common.Rewind_excel_data_map.get("CD_"+cover_code)).equals("Yes")){
						NB_NNP = 0.0;
						MTA_NNP = Double.parseDouble((String)common.Rewind_excel_data_map.get("PS_"+code+"_NetNetPremium"));
						map_data = common.Rewind_excel_data_map;
					}else{
						NB_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_"+code+"_NetNetPremium"));
						MTA_NNP = Double.parseDouble((String)common.Rewind_excel_data_map.get("PS_"+code+"_NetNetPremium"));
						map_data = common.NB_excel_data_map;
					}
					
					final_trans_NNP = ((MTA_NNP - NB_NNP)/p_NB_Duration)*
							((Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
					
			
			}
			}
		
			else{
				if(Integer.parseInt((String)data_map.get("PS_Duration"))!=Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration"))){
					
					
					//if(sectionName.contains("PI")){
						
						if(((String)data_map.get("CD_"+cover_code)).equals("Yes") && ((String)common.MTA_excel_data_map.get("CD_"+cover_code)).equals("No"))
						{
							NB_NNP = Double.parseDouble((String)data_map.get("PS_"+code+"_NetNetPremium"));
							MTA_NNP = 0.0;
								
						}else if(((String)data_map.get("CD_"+cover_code)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_"+cover_code)).equals("Yes")){
							NB_NNP = 0.0;
							MTA_NNP = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NetNetPremium"));
						}else{
							NB_NNP = Double.parseDouble((String)data_map.get("PS_"+code+"_NetNetPremium"));
							MTA_NNP = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NetNetPremium"));
						}
						//Previous Premium MTA Calculation
						
						final_trans_NNP = ((MTA_NNP - NB_NNP)/p_NB_Duration)*
								((Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
						
						/*trans_NetNetP = ((NB_NNP)/p_NB_Duration)*p_MTA_Remaining_Duration;
						previous_mta = NB_NNP - trans_NetNetP;
						annualize_mta = ((Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NetNetPremium")) * (Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) ))/(Integer.parseInt((String)data_map.get("PS_Duration"))));
						_annualize_mta = (annualize_mta/Double.parseDouble((String)common.MTA_excel_data_map.get("PS_Duration"))) * ((Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) - p_MTA_Remaining_Duration));
						final_trans_NNP = _annualize_mta - previous_mta;
						*/
				
				}else{
					
					//if(sectionName.contains("PI")){
						if(((String)data_map.get("CD_"+cover_code)).equals("Yes") && ((String)common.MTA_excel_data_map.get("CD_"+cover_code)).equals("No"))
						{
							NB_NNP = Double.parseDouble((String)data_map.get("PS_"+code+"_NetNetPremium"));
							MTA_NNP = 0.0;
								
						}else if(((String)data_map.get("CD_"+cover_code)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_"+cover_code)).equals("Yes")){
							NB_NNP = 0.0;
							MTA_NNP = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NetNetPremium"));
						}else{
							NB_NNP = Double.parseDouble((String)data_map.get("PS_"+code+"_NetNetPremium"));
							MTA_NNP = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NetNetPremium"));
						}
						
						final_trans_NNP = ((MTA_NNP - NB_NNP)/p_NB_Duration)*
								((Integer.parseInt((String)data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
						
					
				}
		}
		if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
			
			if(((String)data_map.get("CD_"+cover_code)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_"+cover_code)).equals("Yes"))
			{
				data_map = common.MTA_excel_data_map;
			}
			
			
			String t_NetNetP_expected = common.roundedOff(Double.toString(final_trans_NNP));
			String t_NetNetP_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Net Net Premium"));
			CommonFunction.compareValues(Double.parseDouble(t_NetNetP_expected),Double.parseDouble(t_NetNetP_actual)," Net Net Premium");
			trans_details_values.put("Net Net Premium", Double.parseDouble(t_NetNetP_expected));
			
			double t_pen_comm = (( Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)data_map.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)data_map.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)data_map.get("PS_"+code+"_PenComm_rate"))/100)));
			String t_pc_expected = common.roundedOff(Double.toString(t_pen_comm));
			String t_pc_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Pen Comm"));
			CommonFunction.compareValues(Double.parseDouble(t_pc_expected),Double.parseDouble(t_pc_actual)," Pen Commission");
			trans_details_values.put("Pen Comm", Double.parseDouble(t_pc_expected));
			
			
			double t_netP = Double.parseDouble(t_pc_expected) + Double.parseDouble(t_NetNetP_expected);
			String t_netP_expected = common.roundedOff(Double.toString(t_netP));
			String t_netP_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Net Premium"));
			CommonFunction.compareValues(Double.parseDouble(t_netP_expected),Double.parseDouble(t_netP_actual),"Net Premium");
			trans_details_values.put("Net Premium", Double.parseDouble(t_netP_expected));
			
			
			double t_broker_comm = ((Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)data_map.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)data_map.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)data_map.get("PS_"+code+"_BrokerComm_rate"))/100)));
			String t_bc_expected = common.roundedOff(Double.toString(t_broker_comm));
			String t_bc_actual =  Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Broker Commission"));
			CommonFunction.compareValues(Double.parseDouble(t_bc_expected),Double.parseDouble(t_bc_actual),"Broker Commission");
			trans_details_values.put("Broker Commission", Double.parseDouble(t_bc_expected));
			
			
			double t_grossP = Double.parseDouble(t_netP_expected) + Double.parseDouble(t_bc_expected);
			String t_grossP_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Gross Premium"));
			CommonFunction.compareValues(t_grossP,Double.parseDouble(t_grossP_actual)," Gross Premium");
			trans_details_values.put("Gross Premium", t_grossP);
			
			String nbStatus = (String)map_data.get("MTA_Status");
					
			if(!sectionName.contains("Terrorism")){
				AdditionalExcTerrDocAct = AdditionalExcTerrDocAct + Double.parseDouble(t_grossP_actual);
			}else{
				AdditionalTerPDocAct = Double.parseDouble(t_grossP_actual);
			}
				
			double t_InsuranceTax = 0.0;
			if(((String)map_data.get("PS_TaxExempt")).equalsIgnoreCase("Yes") || ((String)map_data.get("PS_InsuranceTaxButton")).equalsIgnoreCase("Yes")){
				t_InsuranceTax = (t_grossP * 0.0)/100.0;
			}else{
				t_InsuranceTax = (t_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_IPT")))/100.0;
			}
				
			t_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(t_InsuranceTax)));
			String t_InsuranceTax_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Insurance Tax"));
			CommonFunction.compareValues(t_InsuranceTax,Double.parseDouble(t_InsuranceTax_actual),"Insurance Tax");
			trans_details_values.put("Insurance Tax", t_InsuranceTax);
			
		
			
			if(sectionName.contains("Terrorism")){
				AddTaxTerrDoc = Double.parseDouble(t_InsuranceTax_actual);
				if(common_POF.isMTARewindStarted){
					rewindMTADoc_AddTaxTer = Double.parseDouble(t_InsuranceTax_actual);	
				}			
			}
			
			String Nb_Status = (String)map_data.get("MTA_Status");
			if(common_POF.isMTARewindStarted){
				
				String newCover = "";
				if(sectionName.contains("PropertyOwnersLiabilities")){
					newCover = "Liabilities-POL";
				}else if(sectionName.contains("EmployersLiability")) {
					newCover = "Liabilities-EL";
				}else{
					newCover = sectionName;
				}
				
				String sCover = (String)map_data.get("CD_Add_"+newCover);
				
				if(sCover.contains("Yes")){
					rewindDoc_InsPTax  = rewindDoc_InsPTax + Double.parseDouble(t_InsuranceTax_actual);
					if(!sectionName.contains("Terrorism")){
						rewindMTADoc_Premium   = rewindMTADoc_Premium  + Double.parseDouble(t_grossP_actual);
					}else{
						rewindMTADoc_TerP   = Double.parseDouble(t_grossP_actual);
					}
				}
				
			}
			
			AdditionalInsTaxDocAct = AdditionalInsTaxDocAct + Double.parseDouble(t_InsuranceTax_actual);
			
			//SPI  Transaction Total Premium verification : 
			double t_Premium = t_grossP + t_InsuranceTax;
			String t_p_expected = common.roundedOff(Double.toString(t_Premium));
			trans_details_values.put("Total Premium", Double.parseDouble(t_p_expected));
			
			String t_p_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Total Premium"));
			
			common.transaction_Details_Premium_Values.put(sectionName, trans_details_values);
			
			double premium_diff = Double.parseDouble(t_p_expected) - Double.parseDouble(t_p_actual);
			
			if(premium_diff<0.05 && premium_diff>-0.05){
				TestUtil.reportStatus("Total Premium [<b> "+t_p_expected+" </b>] matches with actual total premium [<b> "+t_p_actual+" </b>]as expected for "+sectionName+" in Transaction Details table .", "Pass", false);
				//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_expected,common.NB_excel_data_map),"Error while writing Total Premium for cover "+code+" to excel .");
				return 0;
			}else{
				TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+t_p_expected+"</b>] and Actual Premium [<b> "+t_p_actual+"</b>] for "+sectionName+" in Transaction Details table . </p>", "Fail", true);
				//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_expected,common.NB_excel_data_map),"Error while writing Total Premium for cover "+code+" to excel .");
				return 1;
			}
			
		}else{
			String t_NetNetP_expected = common.roundedOff(Double.toString(final_trans_NNP));
			String t_NetNetP_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Net Net Premium"));
			CommonFunction.compareValues(Double.parseDouble(t_NetNetP_expected),Double.parseDouble(t_NetNetP_actual)," Net Net Premium");
			trans_details_values.put("Net Net Premium", Double.parseDouble(t_NetNetP_expected));
			
			double t_pen_comm = (( Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate"))/100)));
			String t_pc_expected = common.roundedOff(Double.toString(t_pen_comm));
			String t_pc_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Pen Comm"));
			CommonFunction.compareValues(Double.parseDouble(t_pc_expected),Double.parseDouble(t_pc_actual)," Pen Commission");
			trans_details_values.put("Pen Comm", Double.parseDouble(t_pc_expected));
			
			
			double t_netP = Double.parseDouble(t_pc_expected) + Double.parseDouble(t_NetNetP_expected);
			String t_netP_expected = common.roundedOff(Double.toString(t_netP));
			String t_netP_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Net Premium"));
			CommonFunction.compareValues(Double.parseDouble(t_netP_expected),Double.parseDouble(t_netP_actual),"Net Premium");
			trans_details_values.put("Net Premium", Double.parseDouble(t_netP_expected));
			
			
			double t_broker_comm = ((Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate"))/100)));
			String t_bc_expected = common.roundedOff(Double.toString(t_broker_comm));
			String t_bc_actual =  Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Broker Commission"));
			CommonFunction.compareValues(Double.parseDouble(t_bc_expected),Double.parseDouble(t_bc_actual),"Broker Commission");
			trans_details_values.put("Broker Commission", Double.parseDouble(t_bc_expected));
			
			
			double t_grossP = Double.parseDouble(t_netP_expected) + Double.parseDouble(t_bc_expected);
			String t_grossP_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Gross Premium"));
			CommonFunction.compareValues(t_grossP,Double.parseDouble(t_grossP_actual)," Gross Premium");
			trans_details_values.put("Gross Premium", t_grossP);
			
			String Nb_Status = (String)common.MTA_excel_data_map.get("MTA_Status");
			if(Nb_Status.contains("Endorsement Rewind")){
				if(!sectionName.contains("Terrorism")){
						rewindMTADoc_Premium  =  Double.parseDouble(t_grossP_actual);
				}else{
						rewindMTADoc_TerP  = Double.parseDouble(t_grossP_actual);
				}
			}
			
			if(!sectionName.contains("Terrorism")){
					AdditionalExcTerrDocAct = AdditionalExcTerrDocAct + Double.parseDouble(t_grossP_actual);
				}else{
					AdditionalTerPDocAct = Double.parseDouble(t_grossP_actual);
			}
		
			
			double t_InsuranceTax = 0.0;
			if(((String)map_data.get("PS_TaxExempt")).equalsIgnoreCase("Yes") || ((String)map_data.get("PS_InsuranceTaxButton")).equalsIgnoreCase("Yes")){
				t_InsuranceTax = (t_grossP * 0.0)/100.0;
			}else{
				t_InsuranceTax = (t_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_IPT")))/100.0;
			}
			
			t_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(t_InsuranceTax)));
			String t_InsuranceTax_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Insurance Tax"));
			CommonFunction.compareValues(t_InsuranceTax,Double.parseDouble(t_InsuranceTax_actual),"Insurance Tax");
			trans_details_values.put("Insurance Tax", t_InsuranceTax);
			
			if(sectionName.contains("Terrorism")){
				if(Nb_Status.contains("Endorsement Rewind")){
					rewindMTADoc_AddTaxTer  = Double.parseDouble(t_InsuranceTax_actual);
				}
				
				AddTaxTerrDoc = Double.parseDouble(t_InsuranceTax_actual);
						
			}
			
			AdditionalInsTaxDocAct = AdditionalInsTaxDocAct + Double.parseDouble(t_InsuranceTax_actual);
			
			//SPI  Transaction Total Premium verification : 
			double t_Premium = t_grossP + t_InsuranceTax;
			String t_p_expected = common.roundedOff(Double.toString(t_Premium));
			trans_details_values.put("Total Premium", Double.parseDouble(t_p_expected));
			
			String t_p_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Total Premium"));
			
			common.transaction_Details_Premium_Values.put(sectionName, trans_details_values);
			
			double premium_diff = Double.parseDouble(t_p_expected) - Double.parseDouble(t_p_actual);
			
			if(premium_diff<0.05 && premium_diff>-0.05){
				TestUtil.reportStatus("Total Premium [<b> "+t_p_expected+" </b>] matches with actual total premium [<b> "+t_p_actual+" </b>]as expected for "+sectionName+" in Transaction Details table .", "Pass", false);
				//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_expected,common.NB_excel_data_map),"Error while writing Total Premium for cover "+code+" to excel .");
				return 0;
			}else{
				TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+t_p_expected+"</b>] and Actual Premium [<b> "+t_p_actual+"</b>] for "+sectionName+" in Transaction Details table . </p>", "Fail", true);
				//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_expected,common.NB_excel_data_map),"Error while writing Total Premium for cover "+code+" to excel .");
				return 1;
			}
			
		}
		
			
}catch(Throwable t) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
    Assert.fail("Transaction Details table verification issue.  \n", t);
    return 1;
}


//public boolean funcPolicy

//End of CommonFunction_POB.java
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



public boolean CancellationPremiumTables( String code, String event, String tableName, boolean re_Calc){
	//Transaction Premium Table
		try{
			k.ImplicitWaitOff();
			Hashtable<String,String> coverlist = new Hashtable<String,String>();
			Hashtable<String,String> cHash = new Hashtable<String,String>();
			String[] prem_col = {"gprm","comr","comm","nprem","gipt","nipt"};
			String testName = (String)common.CAN_excel_data_map.get("Automation Key");
			
			coverlist.put("Material Damage","md7");
			coverlist.put("Business Interruption","bi2");
			coverlist.put("Terrorism","tr2");
			coverlist.put("Employers Liability","el3");
			coverlist.put("Public Liability","pl2");
			coverlist.put("Products Liability","pr1");
			coverlist.put("Specified All Risks","sar");
			coverlist.put("Contractors All Risks","car");
			coverlist.put("Computers and Electronic Risks","it");
			coverlist.put("Money","mn2");
			coverlist.put("Goods In Transit","gt2");
			coverlist.put("Marine Cargo","mar");
			coverlist.put("Cyber and Data Security","cyb");
			coverlist.put("Directors and Officers","do2");
			coverlist.put("Frozen Foods","ff2");
			coverlist.put("Loss of Licence","ll2");
			coverlist.put("Fidelity Guarantee","fg");
			coverlist.put("Legal Expenses","lg2");
			coverlist.put("Total","tot");
			
			cHash.put("gprm","GP");
			cHash.put("comr","CR");
			cHash.put("comm","GC");
			cHash.put("nprem","NP");
			cHash.put("gipt","GT");
			cHash.put("nipt","NPIPT");
			cHash.put("com","GC");
			//html/body/div[3]/form/div/table[3]/tbody/tr
			switch (tableName){
			
				case "AP":
					String Trax_table = "html/body/div[3]/form/div/table[3]";
					int rcount = driver.findElements(By.xpath(Trax_table+"/tbody/tr")).size();
					
					
					List<WebElement> col=driver.findElements(By.xpath("html/body/div[3]/form/div/table[3]/tbody/tr[1]/td"));
					//System.out.println(col.size());
					int cCount = col.size();
					Hashtable<String,String> transSmryData = new Hashtable<String,String>();
					String tXpath =Trax_table+"/tbody/tr";
					String CellValue;
					//System.out.println("Transaction Premium Table exist on premium summary page");
					TestUtil.reportStatus("Annual Premium Table exist on premium summary page", "Info", true);
					//System.out.println("Covers Found -:"+ (rcount-1));
					TestUtil.reportStatus("Covers Found -:"+ (rcount-1), "Info", false);
						
						for(int i =1;i<=rcount;i++){
							String sectionXpath = tXpath+"["+i+"]/td[1]";
							String sec_name = driver.findElement(By.xpath(sectionXpath)).getText();
							//System.out.print(sec_name+"\t\t\t");
							if(sec_name.contains("Frozen Food")){
								sec_name = "FrozenFoods";
							}else if(sec_name.contains("Licence")){
								sec_name = "LossOfLicence";
							}else if(sec_name.contains("Total")){
								sec_name = "Total";
								sec_name = "Total";
							}else{
								sec_name = sec_name.replaceAll(" ", "");
							}
							
							TestUtil.reportStatus("---------------"+sec_name+"-----------------","Info",false);
							
							for(int j = 2; j<=cCount;j++){
								//Annual Premium Data
								String ColumnValues = tXpath+"["+i+"]/td["+j+"]";
								CellValue = driver.findElement(By.xpath(ColumnValues)).getText().replaceAll(",","");;
								if(cHash.get(prem_col[j-2]).equalsIgnoreCase("CR") && sec_name.contains("Total"))
								{}else{
								transSmryData.put("PS_"+sec_name.replaceAll(" ", "")+"_"+cHash.get(prem_col[j-2]), CellValue);}
							}
							common.PremiumSummarytableCalculation(transSmryData,sec_name);
						
						} 
					
					// Writing Data to Excel Sheet from Map
					if(common.NB_excel_data_map.get("PS_DefaultStartEndDate").equals("Yes")){
						Set<String> pKeys=transSmryData.keySet();
					 	for(String pkey:pKeys){
//					 		if(re_Calc){
//					 			customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName,pkey,transSmryData.get(pkey) ,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+pkey) ;
//					 		}else{
					 			common.compareValues(Double.parseDouble(TestUtil.getStringfromMap(pkey,"NB")),Double.parseDouble(transSmryData.get(pkey)), pkey);
//					 		}
					 	}
					 }
					
					
					
					 	 TestUtil.reportStatus("Values have been stored from Annual Premium table", "info", true);
					
					break;
					
				case "CP":
					String CAN_table = "html/body/div[3]/form/div/table[4]";
					int crowcount = driver.findElements(By.xpath(CAN_table+"/tbody/tr")).size();
					int cColCount = driver.findElements(By.xpath("html/body/div[3]/form/div/table[4]/tbody/tr[1]/td")).size();
					Hashtable<String,String> CanSmryData = new Hashtable<String,String>();
					String cXpath =CAN_table+"/tbody/tr";
					String cCellValue;
					
					String rPath  = "html/body/div[3]/form/div/table[4]/tbody/tr[";
					String rPath2 = "]/td[";
					String cPath  = "]/input";
					
					// html/body/div[3]/form/div/table[4]/tbody/tr[1]/td[2]/input
					for(int l =1; l<=crowcount;l++){
						String sectionXpath = rPath+l+"]/td[1]";
						String sec_name = driver.findElement(By.xpath(sectionXpath)).getText();
						//System.out.print(sec_name+"\t\t\t");
						if(sec_name.contains("Frozen Food")){
							sec_name = "FrozenFoods";
						}else if(sec_name.contains("Licence")){
							sec_name = "LossOfLicence";
						}else if(sec_name.contains("Total")){
							sec_name = "Total";
							sec_name = "Total";
						}else{
							sec_name = sec_name.replaceAll(" ", "");
						}
						
						for(int m =2;m<=cColCount;m++){
							String cXPath =rPath+l+"]/td["+m+"]";
							String cellVariable = "PS_"+sec_name+"_"+cHash.get(prem_col[m-2]);
							if(cHash.get(prem_col[m-2]).equals("GP") && m==2 && !sec_name.contains("Total")){
								String gpXPath =rPath+l+"]/td["+m+"]/input";
								String gPrem =null;
								if(re_Calc){
									String cGP;
									gPrem = driver.findElement(By.xpath(gpXPath)).getAttribute("value").replaceAll(",", "");
									String changeGP = (String)common.CAN_excel_data_map.get("PS_"+sec_name+"_"+cHash.get(prem_col[m-2]));
									if(Integer.parseInt(changeGP)!=0){
										double New_GP = Double.parseDouble(gPrem) - Double.parseDouble(changeGP);
										cGP = f.format(New_GP);
										 k.scrollInViewByXpath(gpXPath);
										driver.findElement(By.xpath(gpXPath)).clear();
										driver.findElement(By.xpath(gpXPath)).sendKeys(cGP);
										driver.findElement(By.xpath(gpXPath)).sendKeys(Keys.TAB);
										k.Click("cancelRecalc");
										gPrem = driver.findElement(By.xpath(gpXPath)).getAttribute("value").replaceAll(",", "");
										
										TestUtil.reportStatus("Section :"+sec_name+" change value for Gross Premium is :<b>"+changeGP, "Info", false);
										TestUtil.reportStatus("Section :"+sec_name+" updated Gross Premium is :<b>"+New_GP, "Info", false);
									}else{
										 k.scrollInViewByXpath(gpXPath);
										gPrem = driver.findElement(By.xpath(gpXPath)).getAttribute("value").replaceAll(",", "");
									}
									
								}else {
									 k.scrollInViewByXpath(gpXPath);
									gPrem = driver.findElement(By.xpath(gpXPath)).getAttribute("value").replaceAll(",", "");
								}
								
								CanSmryData.put(cellVariable, gPrem);
								continue;
							}
							 k.scrollInViewByXpath(cXPath);
							String cellvalue = driver.findElement(By.xpath(cXPath)).getText().replaceAll(",", "");
							CanSmryData.put(cellVariable, cellvalue);
						}
						common.PremiumSummarytableCalculation(CanSmryData,sec_name);
											
					}
					// Writing Data to Excel Sheet from Map
					Set<String> cKeys=CanSmryData.keySet();
				 	for(String pkey:cKeys){
//				 		if(re_Calc){
				 			//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName,pkey,CanSmryData.get(pkey) ,common.CAN_excel_data_map),"Error while writing Premium Summary data to excel"+pkey) ;
				 		if(pkey.equalsIgnoreCase("PS_Total_CR")==false){
				 			common.CAN_excel_data_map.put(pkey, CanSmryData.get(pkey));}
//				 		}else{
				 			//compareValues(Double.parseDouble(TestUtil.getStringfromMap(pkey,"NB")),Double.parseDouble(CanSmryData.get(pkey)), pkey);
//				 		}
				 	}
					
					 TestUtil.reportStatus("Values have been Verified from <b> Cancellation Premium table", "info", true);					
					break;
					
				default:
					break;
					
			}
		
			return true;
		} 
		
		catch(NumberFormatException e){
			System.out.println(e.getMessage());
			return false;
		}
		catch(Throwable t ){
			k.ImplicitWaitOn();
			ErrorUtil.addVerificationFailure(t);
			return false; 
		}
		
		//System.out.println("Gross Premium for Material Damage Cover in Transaction Premium table is :"+transSmryData.get("MaterialDamage_gprem"));
		
}







}
