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

import com.selenium.commonfiles.util.ErrorInTestMethod;
import com.selenium.commonfiles.util.ErrorUtil;
import com.selenium.commonfiles.util.ObjectMap;
import com.selenium.commonfiles.util.TestUtil;
import com.selenium.commonfiles.util.XLS_Reader;


public class CommonFunction_CTB extends TestBase{

	SimpleDateFormat df = new SimpleDateFormat();
	public int actual_no_of_years=0,err_count=0,trans_error_val=0, Can_returnP_Error=0;
	public static WebElement taxTable_tBody;
	public static WebElement objTable;
	public static WebElement taxTable_tHead;
	public static int countOfCovers,countOfTableRows;
	public static int errorVal=0,counter = 0;
	public static DecimalFormat f = new DecimalFormat("00.00");
	double totalGrossTax = 0.0,totalGrossTaxMTA = 0.0,totalGrossPremium = 0.0,totalGrossPremiumMTA=0.0,totalNetPremiumTax=0.0,totalNetPremiumTaxMTA=0.0;
	public static Map<Object, Integer> variableTaxAdjustmentIDs = null;
	public static Map<Object, Integer> variableTaxAdjustmentIDsMTA = null;
	public static Map<Object, Double> grossTaxValues_Map = null;
	public static Map<Object, Map<Object, Object>> variableTaxAdjustmentVerificationMaps = null;
	public static Map<Object, Object> variableTaxAdjustmentDataMaps = null;
	public static Map<Object, Object> variableTaxAdjustmentDataMapsMTA = null;
	public static List<Object> headerNameStorage = null;
	public static List<Object> headerNameStorageMTA = null;
	public Map<String,Map<String,Double>> transaction_Details_Premium_Values = new HashMap<>();
	public static ArrayList<Object> inputarraylist = null;
	public static ArrayList<Object> inputarraylistMTA = null;
	public double PI_pdf_InsuranceTax = 0.0, PI_pdf_GrossPremium = 0.0,SEL_pdf_InsuranceTax = 0.0, SEL_pdf_GrossPremium = 0.0;
	public List<String> CoversDetails_data_list = null;
	public static Map<String, Double> Adjusted_Premium_map = null;
	
	public Map<String, String> EndorsementIndividualData = null;
	public Map<String, Map<String , String>> EndorsementCollectiveData = new LinkedHashMap<String, Map<String, String>>();
	public Map<String, String> ExtraEndorsementList = new LinkedHashMap<>();
	public Map<String, Map<String , String>> EndorsementFreeFormatData = new LinkedHashMap<>();
	public static double adjustedPremium = 0.0,adjustedTotalPremium=0.0,adjustedTotalPremiumMTA=0.0,adjustedTotalTax=0.0,adjustedTotalTaxMTA=0.0,unAdjustedTotalTax=0.0,unAdjustedTotalTaxMTA=0.0;
	public static double PD_TotalRate = 0.0, PD_AdjustedRate = 0.0, PD_MD_Premium=0.0, PD_BI_Premium=0.0, PD_MD_TotalPremium = 0.00, PD_BI_TotalPremium = 0.00, finalMDPremium = 0.00, finalBIPremium= 0.00;
	public String currentRunningFlow ="NB";
	public static Map<String , String> AdjustedTaxDetails = new LinkedHashMap<String, String>();
	public static Map<String , String> AdjustedTaxCollection = new LinkedHashMap<String, String>();
	public String FP_Covers = null;
	
	public Map<String, List<Map<String, String>>> NB_Structure_of_InnerPagesMaps = null;
	public Map<String, List<Map<String, String>>> MTA_Structure_of_InnerPagesMaps = null;
	public Map<String, List<Map<String, String>>> Rewind_Structure_of_InnerPagesMaps = null;
	public Map<String, List<Map<String, String>>> Renewal_Structure_of_InnerPagesMaps = null;
	public Map<String, List<Map<String, String>>> CAN_Structure_of_InnerPagesMaps = null;
	public static Map<Object,Object> PS_Map = null;
	
	
	public Map<String,Map<String,Double>> CAN_CTB_ReturnP_Values_Map = new HashMap<>();
	public Map<String,Map<String,Double>> PremiumSummary_Values = new HashMap<>();
	
	DecimalFormat decim = new DecimalFormat("#.00");
	public Map<String,Integer> no_of_inner_data_sets = new HashMap<>();
	public Hashtable<String,String> GrosspremSmryData = new Hashtable<String,String>();
	public double rewindDoc_Premium = 0.00, rewindDoc_TerP = 0.00, rewindDoc_InsPTax = 0.00, rewindDoc_TotalP = 0.00, rewindDoc_InsTaxTer = 0.00;
	public double rewindMTADoc_AddTaxTer = 0.00;
	public static int size;
	public double rewindMTADoc_Premium = 0.00, rewindMTADoc_TerP = 0.00, rewindMTADoc_InsPTax = 0.00, rewindMTADoc_TotalP = 0.00;
	
	
	// Premium Summary Data maps
		public Map<String,Map<String,Double>> transaction_Premium_Values = new HashMap<>();
		public Map<String,Map<String,Double>> transaction_Details_Premium_Values_EndorsemntRenewal = new HashMap<>();
		public Map<String,Map<String,Double>> Can_ReturnP_Values_Map = new HashMap<>();
		public boolean PremiumFlag = false;
		public boolean isInsuranceTaxDone = false,isTransTable = false;
		public List<String> referrals_list = new ArrayList<>();
		public List<String> quote_validations_list = new ArrayList<>();
		public double TotalPremiumWithAdminDocAct = 0.00, TotalPremiumWithAdminDocExp = 0.00, PremiumExcTerrDocAct = 0.00,  PremiumExcTerrDocExp = 0.00, TerPremDocAct = 0.00, TerPremDocExp = 0.00, InsTaxDocAct = 0.00, InsTaxDocExp = 0.00;
		public double AdditionalPWithAdminDocAct = 0.00, AdditionalExcTerrDocAct = 0.00,  AdditionalTerPDocAct = 0.00, AdditionalInsTaxDocAct = 0.00;
		public double InsTaxTerrDoc = 0.00, tpTotal = 0.00, AddTaxTerrDoc = 0.00;
	
public void NewBusinessFlow(String code,String event) throws ErrorInTestMethod{
	String testName = (String)common.NB_excel_data_map.get("Automation Key");
	try{
		
		customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
		customAssert.assertTrue(common.checkClient(common.NB_excel_data_map,code,event),"Unable to check Client.");
		customAssert.assertTrue(common.createNewQuote(common.NB_excel_data_map,code,event), "Unable to create new quote.");
		customAssert.assertTrue(common.selectLatestQuote(common.NB_excel_data_map,code,event), "Unable to select quote from table.");
		customAssert.assertTrue(funcPolicyDetails(common.NB_excel_data_map,code,event), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(common_CCF.funcPreviousClaims(common.NB_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(common.funcCovers(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified Perils"),"Issue while Navigating to Specified Perils  . ");
		customAssert.assertTrue(common.funcSpecifiedPerils(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
		
		if(((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
			customAssert.assertTrue(common_CCF.funcInsuredProperties(common.NB_excel_data_map), "Insured Property function is having issue(S) . ");
		}
		
		if(((String)common.NB_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
			customAssert.assertTrue(common_CCF.funcBusinessInterruption(common.NB_excel_data_map), "Business Interruption function is having issue(S) . ");
		}
		
		if(((String)common.NB_excel_data_map.get("CD_Liability")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
			customAssert.assertTrue(common_CCF.funcEmployersLiability(common.NB_excel_data_map), "Employers Liability function is having issue(S) . ");
			
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
			customAssert.assertTrue(common_CCF.funcPublicLiability(common.NB_excel_data_map), "Public Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
			customAssert.assertTrue(common_CCF.funcProductsLiability(common.NB_excel_data_map), "Products Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
			customAssert.assertTrue(funcLiabilityInformation(common.NB_excel_data_map), "Liability Information function is having issue(S) . ");
		}
		
		if(((String)common.NB_excel_data_map.get("CD_SpecifiedAllRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
			customAssert.assertTrue(common_CCF.funcSpecifiedAllRisks(common.NB_excel_data_map), "Specified All Risks function is having issue(S) . ");
		}
		
		if(((String)common.NB_excel_data_map.get("CD_ContractorsAllRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
			customAssert.assertTrue(common_CCF.funcContractorsAllRisks(common.NB_excel_data_map), "Contractors All Risks function is having issue(S) . ");
		}
		
		if(((String)common.NB_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
			customAssert.assertTrue(common_CCF.funcComputersandElectronicRisks(common.NB_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
		}
		
		if(((String)common.NB_excel_data_map.get("CD_Money")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
			customAssert.assertTrue(common_CCF.funcMoney(common.NB_excel_data_map), "Money function is having issue(S) . ");
		}
		
		if(((String)common.NB_excel_data_map.get("CD_GoodsInTransit")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
			customAssert.assertTrue(common_CCF.funcGoodsInTransit(common.NB_excel_data_map), "Goods In Transit function is having issue(S) . ");
		}
		
		if(((String)common.NB_excel_data_map.get("CD_FidelityGuarantee")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
			customAssert.assertTrue(common_CCF.funcFidelityGuarantee(common.NB_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
		}
		
		if(((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
			customAssert.assertTrue(common_CCF.funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
		}
		
		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(funcPremiumSummary(common.NB_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			
			customAssert.assertTrue(common_PEN.funcStatusHandling(common.NB_excel_data_map,code,event));
			
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

public void RenewalFlow(String code,String event){
	String testName = (String)common.Renewal_excel_data_map.get("Automation Key");
	common.currentRunningFlow = "Renewal";
	try{
		
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
		
		customAssert.assertTrue(funcPolicyDetails(common.Renewal_excel_data_map), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(common_CCF.funcPreviousClaims(common.Renewal_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(funcCovers(common.Renewal_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified Perils"),"Issue while Navigating to Specified Perils  . ");
		customAssert.assertTrue(common.funcSpecifiedPerils(common.Renewal_excel_data_map), "Select covers function is having issue(S) . ");
		
		if(((String)common.Renewal_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
			customAssert.assertTrue(funcInsuredProperties(common.Renewal_excel_data_map), "Insured Property function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
			customAssert.assertTrue(common_CCF.funcBusinessInterruption(common.Renewal_excel_data_map), "Business Interruption function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_Liability")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
			customAssert.assertTrue(common_CCF.funcEmployersLiability(common.Renewal_excel_data_map), "Employers Liability function is having issue(S) . ");
			
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
			customAssert.assertTrue(common_CCF.funcPublicLiability(common.Renewal_excel_data_map), "Public Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
			customAssert.assertTrue(common_CCF.funcProductsLiability(common.Renewal_excel_data_map), "Products Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
			customAssert.assertTrue(funcLiabilityInformation(common.Renewal_excel_data_map), "Liability Information function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_SpecifiedAllRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
			customAssert.assertTrue(common_CCF.funcSpecifiedAllRisks(common.Renewal_excel_data_map), "Specified All Risks function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_ContractorsAllRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
			customAssert.assertTrue(common_CCF.funcContractorsAllRisks(common.Renewal_excel_data_map), "Contractors All Risks function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
			customAssert.assertTrue(common_CCF.funcComputersandElectronicRisks(common.Renewal_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_Money")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
			customAssert.assertTrue(common_CCF.funcMoney(common.Renewal_excel_data_map), "Money function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_GoodsInTransit")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
			customAssert.assertTrue(common_CCF.funcGoodsInTransit(common.Renewal_excel_data_map), "Goods In Transit function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_FidelityGuarantee")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
			customAssert.assertTrue(common_CCF.funcFidelityGuarantee(common.Renewal_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
			customAssert.assertTrue(common_CCF.funcTerrorism(common.Renewal_excel_data_map), "Terrorism function is having issue(S) . ");
		}
		
		
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		Assert.assertTrue(funcPremiumSummary(common.Renewal_excel_data_map,code,event));
		Assert.assertTrue(common_PEN.funcStatusHandling(common.Renewal_excel_data_map,code,event));
		
		
		customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
		customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
		
		TestUtil.reportTestCasePassed(testName);
	
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
	}
	
}

public boolean funcInsuredProperties(Map<Object, Object> map_data){
	
	boolean r_Value = true;
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
		customAssert.assertTrue(common.funcPageNavigation("Insured Properties", ""),"Insured Properties page is having issue(S)");
		if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }
		customAssert.assertTrue(k.Input("CCF_IP_AnyOneEvent", Keys.chord(Keys.CONTROL, "a")),"Unable to select any one Event field");
		customAssert.assertTrue(k.Input("CCF_IP_AnyOneEvent", (String)map_data.get("IP_AnyOneEvent")),	"Unable to enter value in any one Event field .");
		customAssert.assertTrue(k.Input("IP_Landslip", Keys.chord(Keys.CONTROL, "a")),"Unable to select Subsidence Ground Heave or Landslip field");
		customAssert.assertTrue(k.Input("IP_Landslip", (String)map_data.get("IP_Landslip")),"Unable to enter value in Subsidence Ground Heave or Landslip field .");
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
		
		int count = 0;
		String[] properties = ((String)map_data.get("IP_AddProperty")).split(";");
        int no_of_property = properties.length;
        
		
		while(count < no_of_property ){
			
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
		
		customAssert.assertTrue(common.funcPageNavigation("Property Details", ""),"Property Details page navigation issue(S)");
		
		//
		if(!(internal_data_map.get("Property Details").get(count).get("PoD_CopyAddress")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(k.Input("CCF_Address_CC_Address", internal_data_map.get("Property Details").get(count).get("PoD_Address")),"Unable to enter value in Address field. ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line2", internal_data_map.get("Property Details").get(count).get("PoD_AddressL2")),"Unable to enter value in Address field line 2 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line3", internal_data_map.get("Property Details").get(count).get("PoD_AddressL3")),"Unable to enter value in Address field line 3 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Town", internal_data_map.get("Property Details").get(count).get("PoD_Town")),"Unable to enter value in Town field . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_County", internal_data_map.get("Property Details").get(count).get("PoD_County")),"Unable to enter value in County  . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode", internal_data_map.get("Property Details").get(count).get("PoD_Postcode")),"Unable to enter value in PostCode field .");
			customAssert.assertTrue(common.funcButtonSelection("Save"));
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"Address Field Should Contain Valid Value on Client Details .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Postcode", "value"),"Postcode Field Should Contain Valid Value on Client Details .");
			customAssert.assertTrue(common.validatePostCode(internal_data_map.get("Property Details").get(count).get("PoD_Postcode")),"Post Code is not in Correct format .");
			
		}else{
			customAssert.assertTrue(k.Click("CCF_Btn_CopyCorAddress"));
		}
		//
		
		customAssert.assertTrue(k.Input("CCF_PoD_PropertyAge", internal_data_map.get("Property Details").get(count).get("PoD_PropertyAge")),"Unable to enter value in Age of Property (years) . ");
		customAssert.assertTrue(k.DropDownSelection("CCF_PoD_TerrorismZone", internal_data_map.get("Property Details").get(count).get("PoD_TerrorismZone")), "Unable to select value from Terrorism Zone dropdown .");
		
		//Statement of Fact
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q1", internal_data_map.get("Property Details").get(count).get("PoD_SOF_Q1")), "Unable to Select first SOF radio button on Policy Details Page.");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q2", internal_data_map.get("Property Details").get(count).get("PoD_SOF_Q2")), "Unable to Select second SOF radio button on Policy Details Page.");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q3", internal_data_map.get("Property Details").get(count).get("PoD_SOF_Q3")), "Unable to Select third SOF radio button on Policy Details Page.");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q4", internal_data_map.get("Property Details").get(count).get("PoD_SOF_Q4")), "Unable to Select fourth SOF radio button on Policy Details Page.");
		
		//Sums Insured
		if(!TestBase.product.equalsIgnoreCase("CTB")){
			customAssert.assertTrue(k.DropDownSelection("CCF_PoD_DayOneUplift",internal_data_map.get("Property Details").get(count).get("PoD_DayOneUplift")), "Unable to select value from Day One uplift dropdown .");
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q1", internal_data_map.get("Property Details").get(count).get("PoD_SOF_Q1")), "Unable to Select first SOF radio button on Policy Details Page.");
		}
		
		//Proximity
		customAssert.assertTrue(k.Input("CCF_PoD_ProximityDescription", internal_data_map.get("Property Details").get(count).get("PoD_ProximityDescription")),"Unable to enter value in Proximity description . ");
		
		//Trade Code
		if((internal_data_map.get("Property Details").get(count).get("PoD_TCS_TradeCode_Button")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.tradeCodeSelection((String)internal_data_map.get("Property Details").get(count).get("PoD_MD_TCS_TradeCode"),"Property Details",count),"Trade code selection function is having issue(S).");	
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
		customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", internal_data_map.get("Property Details").get(count).get("BSI_MD_SumInsured")),"Unable to enter value in Bespoke Sum Insured . ");
		customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Bespoke Sum Insured inner page .");
		
		
		if(((String)map_data.get("CD_BusinessInterruption")).equalsIgnoreCase("Yes")){
			List<WebElement> bespoke_BI_btns = k.getWebElements("CCF_Btn_AddBespokeSumIns");
			WebElement BI_bespoke_btn = bespoke_BI_btns.get(1);
			BI_bespoke_btn.click();
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", internal_data_map.get("Property Details").get(count).get("BSI_BI_Description")),"Unable to enter value in BI Bespoke Description . ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", internal_data_map.get("Property Details").get(count).get("BSI_BI_SumInsured")),"Unable to enter value in Bespoke Sum Insured . ");
			customAssert.assertTrue(k.DropDownSelection("CCF_BSI_BI_IndemnityPeriod", internal_data_map.get("Property Details").get(count).get("BSI_BI_IndemnityPeriod")), "Unable to select value from Indemnity Period dropdown .");
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Bespoke Sum Insured inner page .");
		}
				
		double finalMDPremium = 0.00, finalBIPremium = 0.00;
		 
		 if(((String)map_data.get("CD_MaterialDamage")).equalsIgnoreCase("Yes")){
			 customAssert.assertTrue(CommonFunction.PropertyDetails_HandleTables(map_data, "MD", count),"failed in MD handle table");
			 customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
			 
			 finalMDPremium =  finalMDPremium + Double.parseDouble(internal_data_map.get("Property Details").get(count).get("MD_TotalPremium"));
			 TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_MaterialDamage_NP", String.valueOf(finalMDPremium), map_data);
			 
			 if(((String)map_data.get("CD_BusinessInterruption")).equalsIgnoreCase("Yes") ||
					 ((String)map_data.get("CD_Add_BusinessInterruption")).equalsIgnoreCase("Yes")){
				 customAssert.assertTrue(CommonFunction.PropertyDetails_HandleTables(map_data, "BI", count),"failed in BI handle table");
				 customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
				 
				 finalBIPremium =  finalBIPremium + Double.parseDouble(internal_data_map.get("Property Details").get(count).get("BI_TotalPremium"));
				 TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_BusinessInterruption_NP", String.valueOf(finalBIPremium), map_data);
			 }
		 }
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
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
			
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", common.NB_Structure_of_InnerPagesMaps.get("BS Insured MD").get(count).get("BSI_MD_Description")),"Unable to enter value in MD Bespoke Description . ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", Keys.chord(Keys.CONTROL, "a")),"Unable to select Bespoke Sum Insured field");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", common.NB_Structure_of_InnerPagesMaps.get("BS Insured MD").get(count).get("BSI_MD_SumInsured")),"Unable to enter value in Bespoke Sum Insured . ");
			
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Bespoke Sum Insured inner page .");
			count++;
		}
		
		if(((String)map_data.get("CD_BusinessInterruption")).equals("Yes")){
			int total_count_BI_bespoke = common.no_of_inner_data_sets.get("BS Insured BI");
			count=0;
			while(count < total_count_BI_bespoke){
				k.Click("CCF_Btn_BI_Bespoke");
				k.waitTwoSeconds();
			
				customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", common.NB_Structure_of_InnerPagesMaps.get("BS Insured BI").get(count).get("BSI_BI_Description")),"Unable to enter value in BI Bespoke Description . ");
				customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", Keys.chord(Keys.CONTROL, "a")),"Unable to select Bespoke Sum Insured field");
				customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", common.NB_Structure_of_InnerPagesMaps.get("BS Insured BI").get(count).get("BSI_BI_SumInsured")),"Unable to enter value in Bespoke Sum Insured . ");
				customAssert.assertTrue(k.DropDownSelection("CCF_BSI_BI_IndemnityPeriod", common.NB_Structure_of_InnerPagesMaps.get("BS Insured BI").get(count).get("BSI_BI_IndemnityPeriod")), "Unable to select value from Indemnity Period dropdown .");
				customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Bespoke Sum Insured inner page .");
				count++;
			}
		}
	}catch(Throwable t){
		
		r_value=false;
	}
	
	return r_value;
	
}
public boolean funcPolicyDetails(Map<Object, Object> map_data,String code ,String event){
	
	boolean retvalue = true;
	try{
		customAssert.assertTrue(common.funcPageNavigation("Policy Details", ""), "Navigation problem to Policy Details page .");
		if(common.currentRunningFlow.equals("NB")){
		customAssert.assertTrue(k.Input("CCF_PD_ProposerName", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_PD_ProposerName", (String)map_data.get("NB_ClientName")),	"Unable to enter value in Proposer Name  field .");}
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_PD_ProposerName", "value"),"Proposer Name Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("CCF_CC_TradingName", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_CC_TradingName", (String)map_data.get("PD_TradingName")),	"Unable to enter value in Trading Name  field .");
		customAssert.assertTrue(k.Input("CCF_PD_BusinessDesc", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_PD_BusinessDesc", (String)map_data.get("PD_BusinessDesc")),	"Unable to enter value in Business Desc  field .");
		if(common.currentRunningFlow.equals("NB")){
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_1QS", (String)map_data.get("PD_1QS")), "Unable to Select 1QS radio button on Policy Details Page.");
		customAssert.assertTrue(k.Input("CCF_PD_DateEstablishment", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_PD_DateEstablishment", (String)map_data.get("PD_DateEstablishment")),	"Unable to enter value in Date Establishment  field .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_NewVenture", (String)map_data.get("PD_NewVenture")), "Unable to Select New Venture radio button on Policy Details Page.");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_Prospect", (String)map_data.get("PD_Prospect")), "Unable to Select Prospect radio button on Policy Details Page.");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CrossSell", (String)map_data.get("PD_CrossSell")), "Unable to Select CrossSell radio button on Policy Details Page.");
		}
		customAssert.assertTrue(k.Input("CCF_Address_CC_Address", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_Address_CC_Address", (String) map_data.get("PD_Address")),"Unable to enter value in Address field. ");
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"Address Field Should Contain Valid Address  .");
		customAssert.assertTrue(k.Input("CCF_Address_CC_line2", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_Address_CC_line2", (String) map_data.get("PD_Line1")),"Unable to enter value in Address field line 1 . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_line3", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_Address_CC_line3", (String) map_data.get("PD_Line2")),"Unable to enter value in Address field line 2 . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_Town", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_Address_CC_Town", (String) map_data.get("PD_Town")),"Unable to enter value in Town field . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_County", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_Address_CC_County", (String) map_data.get("PD_County")),"Unable to enter value in County  . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode", (String)map_data.get("PD_Postcode")),"Unable to enter value in PostCode");
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Postcode", "value"),"PostCode Field Should Contain Valid Postcode  .");

		if(common.currentRunningFlow.equals("NB")){
			customAssert.assertTrue(k.Click("inception_date"), "Unable to Click inception date.");
			customAssert.assertTrue(k.Input("inception_date", (String)map_data.get("QC_InceptionDate")),"Unable to Enter inception date.");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			customAssert.assertTrue(!k.getAttributeIsEmpty("inception_date", "value"),"Inception Date Field Should Contain Valid value  .");
			customAssert.assertTrue(k.Click("deadline_date"), "Unable to Click deadline date.");
			customAssert.assertTrue(k.Input("deadline_date", (String)map_data.get("QC_DeadlineDate")),"Unable to Enter deadline date.");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			customAssert.assertTrue(!k.getAttributeIsEmpty("deadline_date", "value"),"Deadline date Field Should Contain Valid value  .");
			k.waitTwoSeconds();
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_HoldingBroker", (String)map_data.get("PD_HoldingBroker")), "Unable to Select Holding Broker radio button on Policy Details Page.");
			if(map_data.get("PD_HoldingBroker").equals("No")){
				customAssert.assertTrue(k.Input("CCF_PD_HoldingBrokerInfo", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
				customAssert.assertTrue(k.Input("CCF_PD_HoldingBrokerInfo", (String) map_data.get("PD_HoldingBrokerInfo")),"Unable to enter value in HoldingBrokerInfo field. ");
			}
			customAssert.assertTrue(k.Input("CCF_PD_PreviousPremium", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
			customAssert.assertTrue(k.Input("CCF_PD_PreviousPremium", (String) map_data.get("PD_PreviousPremium")),"Unable to enter value in Previous Premium field. ");
			customAssert.assertTrue(k.Input("CCF_QC_TargetPemium", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
			customAssert.assertTrue(k.Input("CCF_QC_TargetPemium", (String) map_data.get("QC_TargetPemium")),"Unable to enter value in Target Pemium field. ");
			
		}
		
		
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_TaxExempt", (String)map_data.get("PD_TaxExempt")), "Unable to Select TaxExempt radio button on Policy Details Page.");
		if(((String)map_data.get("PD_TaxExempt")).equalsIgnoreCase("Yes")){
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_data.get("Automation Key"), "PS_IPTRate", "0", map_data);
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_data.get("Automation Key"), "PS_InsuranceTaxButton", "Yes", map_data);
		}else{
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_data.get("Automation Key"), "PS_IPTRate", "12", map_data);
		}
		if(!TestBase.product.equals("CTB"))
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CarrierOverride", (String)map_data.get("PD_CarrierOverride")), "Unable to Select Carrier Override radio button on Policy Details Page.");
		
		if(map_data.get("PD_CarrierOverride").equals("Yes")){
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CO_RefferedToHead", (String)map_data.get("PD_CO_RefferedToHead")), "Unable to Select Reffered To Head radio button on Policy Details Page.");
		}
		k.waitTwoSeconds();
		
		if(((String)map_data.get("PD_TCS_TradeCode_Button")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.tradeCodeSelection((String)map_data.get("PD_TCS_TradeCode") , "Policy Details" , 0),"Trade code selection function is having issue(S).");
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

public boolean funcCovers(Map<Object, Object> map_data){
	
    boolean retvalue = true;
    CoversDetails_data_list = new ArrayList<>();
    try {
	    	if(CommonFunction.product.equalsIgnoreCase("XOE")){
	    		customAssert.assertTrue(common.funcPageNavigation("Additional Covers", ""),"Cover page is having issue(S)");
	    	}else{
	    		customAssert.assertTrue(common.funcPageNavigation("Covers", ""),"Cover page is having issue(S)");
	    	}
	    	
		 	 k.pressDownKeyonPage();
		 	 String all_cover = ObjectMap.properties.getProperty(CommonFunction.product+"_CD_AllCovers");
		 	 String[] split_all_covers = all_cover.split(",");
		 	 
		 	 for(String coverWithLocator : split_all_covers){
		 		 String coverWithoutLocator = coverWithLocator.split("_")[0];
		 		 try{
		 			 
		 			 if(!coverWithoutLocator.contains("DirectorsandOfficers")){
			 				if(((String)map_data.get("CD_"+coverWithoutLocator)).equals("Yes")){
			 		 			CoversDetails_data_list.add(coverWithoutLocator);
			 		 			
			 		 			customAssert.assertTrue(selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
			 		 		}else{
			 		 			customAssert.assertTrue(deSelectCovers(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
			 		 		}
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

public boolean selectCover(String coverNameWithLocator,Map<Object, Object> map_data){
	 
	 boolean result=true;
	 String c_locator = null;
	 String coverName = null;
	try{
			coverName = coverNameWithLocator.split("_")[0];	
			c_locator = coverNameWithLocator.split("_")[1];
			if(common.product.equalsIgnoreCase("COB")&&common.currentRunningFlow.equalsIgnoreCase("MTA")){
				c_locator = coverNameWithLocator.split("__")[1];
			}
			k.waitTwoSeconds();
			if(c_locator.equals("md")){
				if (!driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).isSelected()){
					driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).click();        
					TestUtil.reportStatus("Cover  <b>"+coverName+"</b> is selected ", "Info", false);
				}else{
					TestUtil.reportStatus("Cover: <b>"+coverName+"</b> is checked by default.", "Info", false);
				}
			}else if(c_locator.equals("PEL")){
				
			}else{
				if (!driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).isSelected()){
					JavascriptExecutor j_exe = (JavascriptExecutor) driver;
					j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")));
					driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).click();        
					TestUtil.reportStatus("Cover: <b>"+coverName+"</b> is selected ", "Info", false);
				}else{
					TestUtil.reportStatus("Cover: <b>"+coverName+"</b> is checked by default.", "Info", false);
				}
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
			coverName = coverNameWithLocator.split("_")[0];	
			c_locator = coverNameWithLocator.split("_")[1];
			if(c_locator.equals("md")){
				if (driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).isSelected()){
					driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).click();  
					k.waitTwoSeconds();
		           	Assert.assertTrue(k.AcceptPopup(), "Unable to locate popup box.");
					TestUtil.reportStatus("Cover  <b>"+coverName+"</b> is Unchecked ", "Info", false);
				}else{
					TestUtil.reportStatus("Cover: <b>"+coverName+"</b> is already not selected.", "Info", false);
				}
			}else if(c_locator.equals("PEL")){
				
			}else{
				if (driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).isSelected()){
					JavascriptExecutor j_exe = (JavascriptExecutor) driver;
					j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")));
					driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).click();        
					k.waitTwoSeconds();
		           	Assert.assertTrue(k.AcceptPopup(), "Unable to locate popup box.");
					TestUtil.reportStatus("Cover  <b>"+coverName+"</b> is Unchecked ", "Info", false);
				}else{
					TestUtil.reportStatus("Cover: <b>"+coverName+"</b> is already not selected.", "Info", false);
				}
			}
		                        
			return result;
	           
	    } catch(Throwable t) {
	    	return false;
	}
} 

public boolean funcStatusHandling(Map<Object, Object> map_data , String code , String event){
	
	 boolean ret_value = true;
	 String quoteDate = null;
	 String p_Status = null;
	 if(TestBase.businessEvent.equals("NB")){
	 	p_Status = (String)map_data.get("NB_Status");
	 }else if(TestBase.businessEvent.equals("MTA")){
		 if(common.currentRunningFlow.equals("NB")){
			 p_Status = (String)common.NB_excel_data_map.get("NB_Status");
		 }else{
			 p_Status = (String)common.MTA_excel_data_map.get("MTA_Status");
		 }
	 }else if(TestBase.businessEvent.equals("Rewind")){
		 if(common.currentRunningFlow.equals("NB")){
			 p_Status = (String)common.NB_excel_data_map.get("NB_Status");
		 }else{
			 p_Status = (String)common.Rewind_excel_data_map.get("Rewind_Status");
		 }
	 }else if(TestBase.businessEvent.equals("CAN"))
		 if(common.currentRunningFlow.equals("NB")){
			 p_Status = (String)common.NB_excel_data_map.get("NB_Status");
		 }else{
			 p_Status = (String)common.CAN_excel_data_map.get("CAN_Status");
		 }
		try{
			
			switch (p_Status) {
			case "Submitted":
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,(String)map_data.get("NB_Status")), "Verify Policy Status (Submitted) function is having issue(S) . ");
				TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("NB_Status")+"  ]</b> status. ", "Info", true);
				
				break;
			case "Quoted":
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
				Assert.assertTrue(common.funcQuoteCheck(common.NB_excel_data_map));
				//This will get UK date after putting policy on Quoted status.
				quoteDate = common.getUKDate();
				common.NB_excel_data_map.put("QuoteDate", quoteDate);
				/////////////////////////////////////////////////////////////
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,(String)map_data.get("NB_Status")), "Verify Policy Status (Quoted) function is having issue(S) . ");
				if(!CommonFunction.product.equalsIgnoreCase("XOE")){
					customAssert.assertTrue(common.funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
				}
				TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("NB_Status")+"  ]</b> status. ", "Info", true);
				break;
			case "On Cover":
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
				Assert.assertTrue(common.funcQuoteCheck(common.NB_excel_data_map));
				//This will get UK date after putting policy on Quoted status.
				quoteDate = common.getUKDate();
				common.NB_excel_data_map.put("QuoteDate", quoteDate);
				/////////////////////////////////////////////////////////////
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Quoted"), "Verify Policy Status (Quoted) function is having issue(S) . ");
				if(!CommonFunction.product.equalsIgnoreCase("XOE")){
					customAssert.assertTrue(common.funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
				}
				customAssert.assertTrue(common.funcGoOnCover(common.NB_excel_data_map), "Go On Cover function is having issue(S) . ");
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,(String)map_data.get("NB_Status")), "Verify Policy Status (On Cover) function is having issue(S) . ");
				
				customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
				
				customAssert.assertTrue(transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
				TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("NB_Status")+"  ]</b> status. ", "Info", true);
				break;
				
			case "Declined":
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
				Assert.assertTrue(common.funcDecline(common.NB_excel_data_map));
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Declined"), "Verify Policy Status (Declined) function is having issue(S) . ");
				//funcVerifyDeclineNTUstatus
				customAssert.assertTrue(common.funcVerifyDeclineNTUstatus(common.NB_excel_data_map), "Verify Policy Status (Decline Page) function is having issue(S) . ");
				break;
				
			case "NTU":
				//Not Taken Up
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
				Assert.assertTrue(common.funcQuoteCheck(common.NB_excel_data_map));
				//This will get UK date after putting policy on Quoted status.
				quoteDate = common.getUKDate();
				common.NB_excel_data_map.put("QuoteDate", quoteDate);
				/////////////////////////////////////////////////////////////
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Quoted"), "Verify Policy Status (Quoted) function is having issue(S) . ");
				
				customAssert.assertTrue(common.funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
				
				Assert.assertTrue(common.funcNTU(common.NB_excel_data_map));
				
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Not Taken Up"), "Verify Policy Status (NTU) function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyNTUstatus(common.NB_excel_data_map), "Verify Policy Status (NTU Page) function is having issue(S) . ");
				TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("NB_Status")+"  ]</b> status. ", "Info", true);
				break;
				
			case "Indicate":
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
				// Indicate
				customAssert.assertTrue(common.funcButtonSelection("Indicate"),"Unable to click on Indicate button on premium summary page");
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Indicated"), "Verify Policy Status (Indicated) function is having issue(S) . ");
				// Indication Accept
				customAssert.assertTrue(common.funcButtonSelection("Indication Accept"),"Unable to click on Indication Accept button on premium summary page");
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Indication Accepted"), "Verify Policy Status (Indication Accepted) function is having issue(S) . ");
				//Quote Creation
				Assert.assertTrue(common.funcQuoteCheck(common.NB_excel_data_map));
				//This will get UK date after putting policy on Quoted status.
				quoteDate = common.getUKDate();
				common.NB_excel_data_map.put("QuoteDate", quoteDate);
				/////////////////////////////////////////////////////////////
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Quoted"), "Verify Policy Status (Quoted) function is having issue(S) . ");
				if(!CommonFunction.product.equalsIgnoreCase("XOE")){
					customAssert.assertTrue(common.funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
				}
				customAssert.assertTrue(common.funcGoOnCover(common.NB_excel_data_map), "Go On Cover function is having issue(S) . ");
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
				
				customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
				
				//customAssert.assertTrue(common.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
				TestUtil.reportStatus("Policy has been created with mentioned Status :<b>[ Submitted->Indicate->Indication Accepted->Quoted->On Cover ]</b>", "Info", true);
				
				break;
				
			case "Rewind":
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Submitted (Rewind) "), "Verify Policy Status (Submitted) function is having issue(S) . ");
				Assert.assertTrue(common.funcQuoteCheck(common.NB_excel_data_map));
				//This will get UK date after putting policy on Quoted status.
				quoteDate = common.getUKDate();
				common.NB_excel_data_map.put("QuoteDate", quoteDate);
				/////////////////////////////////////////////////////////////
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Quoted"), "Verify Policy Status (Quoted) function is having issue(S) . ");
				if(!CommonFunction.product.equalsIgnoreCase("XOE")){
					customAssert.assertTrue(common.funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
				}
				customAssert.assertTrue(common.funcGoOnCover(common.NB_excel_data_map), "Go On Cover function is having issue(S) . ");
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
				
				customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
				
				customAssert.assertTrue(transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
				customAssert.assertTrue(common.funcRewind());
				customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
				customAssert.assertTrue(common.decideRewindMethod());
				customAssert.assertTrue(transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
				
				TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("NB_Status")+"  ]</b> status. ", "Info", true);
				break;
				
			case "Cancelled":
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.CAN_excel_data_map,code,event,"Cancelled"), "Verify Policy Status (Cancelled) function is having issue(S) . ");
				customAssert.assertTrue(transactionSummary((String)common.CAN_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
				TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)common.CAN_excel_data_map.get("CAN_Status")+"  ]</b> status. ", "Info", true);
				break;
				
			case "Endorsement Submitted":
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Submitted"), "Verify Policy Status (Endorsement Submitted) function is having issue(S) . ");
						
				TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("MTA_Status")+"  ]</b> status. ", "Info", true);
				break;
				
			case "Endorsement Quoted":
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Submitted"), "Verify Policy Status (Endorsement Submitted) function is having issue(S) . ");
				
				Assert.assertTrue(common.funcQuoteCheck(common.NB_excel_data_map));
				//This will get UK date after putting policy on Quoted status.
				quoteDate = common.getUKDate();
				common.NB_excel_data_map.put("QuoteDate", quoteDate);
				/////////////////////////////////////////////////////////////
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Quoted"), "Verify Policy Status (Endorsement Quoted) function is having issue(S) . ");
				customAssert.assertTrue(common.funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (Endorsement Quoted) function is having issue(S) . ");
				
				TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("MTA_Status")+"  ]</b> status. ", "Info", true);
				break;
				
			case "Endorsement On Cover":
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Submitted"), "Verify Policy Status (Endorsement Submitted) function is having issue(S) . ");
				customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"),"Unable to Click on Assign Underwriter button . ");
				customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter.");
				customAssert.assertTrue(common_HHAZ.funcQuoteCheck(common.NB_excel_data_map));
				k.ImplicitWaitOff();
				if(k.getText("Page_Header").equals("Quote Check"))
					driver.findElement(By.xpath("//*[text()='Proceed']")).click();
				
				k.ImplicitWaitOn();
				
				//This will get UK date after putting policy on Quoted status.
				quoteDate = common.getUKDate();
				common.MTA_excel_data_map.put("QuoteDate", quoteDate);
				/////////////////////////////////////////////////////////////
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Quoted"), "Verify Policy Status (Endorsement Quoted) function is having issue(S) . ");
				
				customAssert.assertTrue(common.funcGoOnCover_Endorsement(common.NB_excel_data_map), "GoOnCover_Endorsement function is having issue(S) . ");
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,(String)map_data.get("MTA_Status")), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
				
				
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
				customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
				customAssert.assertTrue(transactionSummary((String)common.MTA_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
				TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("MTA_Status")+"  ]</b> status. ", "Info", true);
				break;
				
				
			case "Endorsement Declined":
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Submitted"), "Verify Policy Status (Endorsement Submitted) function is having issue(S) . ");
				customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"),"Unable to Click on Assign Underwriter button . ");
				customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter.");
			
				Assert.assertTrue(common.funcDecline(common.MTA_excel_data_map));
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
			
				TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("MTA_Status")+"  ]</b> status. ", "Info", true);
				break;
				
			case "Endorsement NTU":
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Submitted"), "Verify Policy Status (Endorsement Submitted) function is having issue(S) . ");
				customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"),"Unable to Click on Assign Underwriter button . ");
				customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter.");
				
				Assert.assertTrue(common.funcQuoteCheck(common.NB_excel_data_map));
				//This will get UK date after putting policy on Quoted status.
				quoteDate = common.getUKDate();
				common.MTA_excel_data_map.put("QuoteDate", quoteDate);
				/////////////////////////////////////////////////////////////
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Quoted"), "Verify Policy Status (Endorsement Quoted) function is having issue(S) . ");
				customAssert.assertTrue(common.funcPDFdocumentVerification("Draft Documents"), "Error in PDF Verification (Draft Documents) function is having issue(S) . ");
				Assert.assertTrue(common.funcNTU(common.MTA_excel_data_map));
				
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
				TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("MTA_Status")+"  ]</b> status. ", "Info", true);
				break;
				
			case "Endorsement Rewind":
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Submitted"), "Verify Policy Status (Endorsement Submitted) function is having issue(S) . ");
			    customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"), "Navigation problem to Transaction Summary page .");
				customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"),"Unable to Click on Assign Underwriter button . ");
				customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter.");
			
				Assert.assertTrue(common_HHAZ.funcQuoteCheck(common.NB_excel_data_map));
				if (TestBase.product.equals("CTB")){
					if(k.getText("Page_Header").equals("Quote Check"))
						driver.findElement(By.xpath("//*[text()='Proceed']")).click();
					
					k.ImplicitWaitOn();
				}
				//This will get UK date after putting policy on Quoted status.
				quoteDate = common.getUKDate();
				common.MTA_excel_data_map.put("QuoteDate", quoteDate);
				/////////////////////////////////////////////////////////////
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Quoted"), "Verify Policy Status (Endorsement Quoted) function is having issue(S) . ");
				customAssert.assertTrue(common.funcPDFdocumentVerification("Draft Documents"), "Error in PDF Verification (Draft Documents) function is having issue(S) . ");
				if(TestBase.product.equals("CTB")){
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
				}
				customAssert.assertTrue(common.funcGoOnCover_Endorsement(common.NB_excel_data_map), "GoOnCover_Endorsement function is having issue(S) . ");
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement On Cover"), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
				customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Error in PDF Verification (Documents)(Endorsement On Cover) function is having issue(S) . ");
				
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
				customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
				customAssert.assertTrue(transactionSummary((String)common.MTA_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
				
				common.Rewind_excel_data_map = new HashMap<>();
				common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
				Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CCD_Rewind.xlsx");
				common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "CCD_Rewind_03");
				common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
						Events_Suite_TC_Xls);
				
				common_CCD.RewindFlow(code, "Rewind");
				
				customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status (Endorsement Submitted (Rewind)) function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"), "Navigation problem to Premium Summary page .");
				customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement On Cover"), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
				customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Document verification function is having issue(S) . ");
				if(TestBase.product.equalsIgnoreCase("CTB") ){
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
					customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(transactionSummary((String)common.MTA_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
				}
				TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("MTA_Status")+"  ]</b> status. ", "Info", true);
				break;
				
			case "Endorsement Discard":
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Submitted"), "Verify Policy Status (Endorsement Submitted) function is having issue(S) . ");
			
				customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"),"Unable to Click on Assign Underwriter button . ");
				customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter.");
				
				
				Assert.assertTrue(common.funcDiscardMTA(common.MTA_excel_data_map));
				
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
			
				
				TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("MTA_Status")+"  ]</b> status. ", "Info", true);
				break;
				
			case "Reinstate":
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.CAN_excel_data_map,code,event,"Cancelled"), "Verify Policy Status (Cancelled) function is having issue(S) . ");
				customAssert.assertTrue(common.transactionSummary((String)common.CAN_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
				
				customAssert.assertTrue(common.ReinstatePolicy(common.NB_excel_data_map));
				TestUtil.reportStatus("Current Flow is for <b> 'Reinstate' the Cancelled policy", "Info", true);
				
				break;
			default:
				break;
			}
		}catch(Throwable t){
			ret_value = false;;
		}
		
		return ret_value;
	}

public boolean transactionSummary(String fileName,String testName,String event,String code){
	 Boolean retvalue = true;  
		String RcpName=null, AccName = null;
		try{
			
			Map<Object,Object> data_map = null;
			
   			switch (common.currentRunningFlow) {
   			case "NB":
				data_map = common.NB_excel_data_map;
				break;
			case "MTA":
				data_map = common.MTA_excel_data_map;
				break;
			case "Renewal":
				data_map = common.Renewal_excel_data_map;
				break;
			case "Requote":
				data_map = common.Requote_excel_data_map;
				break;
			case "CAN":
 				data_map = common.NB_excel_data_map;
 				break;
			case "Rewind":
 				data_map = common.Rewind_excel_data_map;
 				break;
			}
			customAssert.assertTrue(common.funcMenuSelection("Navigate", "Transaction Summary"), "Navigation problem to Transaction Summary page .");
			
			Assert.assertEquals(k.getText("Page_Header"),"Transaction Summary", "Not on Transaction Summary Page.");
			String part1= "//*[@id='table0']/tbody";
			String covername = null,exit = "";
			int td=0;
			WebElement table = driver.findElement(By.xpath(part1));
			List<WebElement> list = table.findElements(By.tagName("tr"));
			outer:
			for(int i=1;i<list.size();i++){
				String val = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[1]")).getText();
				double Total =0.00;
				String ExpecteDueDate = "";
				
				switch (val) {
				
					case "New Business" : 
						TestUtil.reportStatus("Verification Started on Transaction Summary page New Business . ", "PASS", false);
						
						Map<Object,Object> Outermap = null;	   					
	   					
	   					switch (common.currentRunningFlow) {	   						
		   					case "NB":
		   						Outermap = common.NB_excel_data_map;
		   						break;
		   					case "Requote":
		   						Outermap = common.Requote_excel_data_map;
		   						break;	   					
	   					}
	   					
						String ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
						
						if(((String)common.NB_excel_data_map.get("PS_PaymentWarrantyRules")).equals("Yes") && !common.currentRunningFlow.equalsIgnoreCase("MTA")){
							ExpecteDueDate = (String)common.NB_excel_data_map.get("PS_PaymentWarrantyDueDate");
						}else{
							ExpecteDueDate = common.getLastDayOfMonth((String)common.NB_excel_data_map.get("QuoteDate"), 1);
						}
						
						if(ActualDueDate.equalsIgnoreCase(ExpecteDueDate)){
							String tMsg="Actual Due Date:"+ActualDueDate+" has been matched with Expected Due Date: "+ExpecteDueDate;
							TestUtil.reportStatus(tMsg, "Pass", false);
	 					}
	 					else{
	 	   					 String tMsg="Actual Due Date:"+ActualDueDate+" does not matche with Expected Due Date: "+ExpecteDueDate;
	 	   					 TestUtil.reportStatus(tMsg, "Fail", false);
	 					}
						
						String ActualTransationDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[3]")).getText();
						String ExpecteTransactionDate = (String)Outermap.get("QuoteDate");
						
						if(ActualTransationDate.equalsIgnoreCase(ExpecteTransactionDate)){
							String tMsg="Actual Transaction Date:"+ActualTransationDate+" has been matched with Expected Transaction Date: "+ExpecteTransactionDate;
							TestUtil.reportStatus(tMsg, "Pass", false);
	 					}
	 					else{
	 	   					 String tMsg="Actual Transaction Date:"+ActualTransationDate+" does not matche with Expected Transaction Date: "+ExpecteTransactionDate;
	 	   					 TestUtil.reportStatus(tMsg, "Fail", false);
	 					}
						
						for(int j=i;!exit.equalsIgnoreCase("Total");j++){
							String transactSumVal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[1]")).getText();
							exit = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[2]")).getText();
							if(transactSumVal.isEmpty()){
								covername= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
								td=8;
							}else{ 
								covername= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[9]")).getText();
								td=11;
							}
							
							if(covername.equalsIgnoreCase("Terrorism")){
								double Terrorism = calculateTerrorismTS(code,testName,"New Bussiness",j,td);
								Total = Total + Terrorism;
							}
							else if(covername.isEmpty()){
								double general = calculateOtherTS(testName,code,j,td,event,val);	
								Total = Total + general;
							}
							
							if(exit.equalsIgnoreCase("Total")){
								i=j;
								String actualTotal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[4]")).getText();  
								compareValues(Double.parseDouble(actualTotal), Double.parseDouble(common.roundedOff(Double.toString(Total))), "Transaction Summary Total");
								customAssert.assertTrue(WriteDataToXl(event+"_NB", "Transaction Summary", (String)common.NB_excel_data_map.get("Automation Key"), "TS_TransactionSummaryTotal", actualTotal,common.NB_excel_data_map),"Error while writing Transaction Summary data to excel .");
		
								break outer;
							}
						}
						
						break;
					
				case "Endorsement":
					
					TestUtil.reportStatus("Verification Started on Transaction Summary page "+val+"  . ", "Info", false);
   					ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
   					ExpecteDueDate = common.getLastDayOfMonth((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 1);
   					
   					if(ActualDueDate.equalsIgnoreCase(ExpecteDueDate)){
      					 String tMsg="Actual Due Date : <b>[  "+ActualDueDate+"  ]</b> has been matched with Expected Due Date : <b>[  "+ExpecteDueDate+"  ]</b>";
      					 TestUtil.reportStatus(tMsg, "Pass", false);
      					}
      					else{
      	   					 String tMsg="Actual Due Date : <b>[  "+ActualDueDate+"  ]</b> does not matche with Expected Due Date : <b>[   "+ExpecteDueDate+"  ]</b>";
      	   					 TestUtil.reportStatus(tMsg, "Fail", false);
      					}
   					ActualTransationDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[3]")).getText();
   					ExpecteTransactionDate = (String)common.MTA_excel_data_map.get("QuoteDate");
   					if(ActualTransationDate.equalsIgnoreCase(ExpecteTransactionDate)){
      					 String tMsg="Actual Transaction Date : <b>[  "+ActualTransationDate+"  ]</b> has been matched with Expected Transaction Date : <b>[  "+ExpecteTransactionDate+"  ]</b>";
      					 TestUtil.reportStatus(tMsg, "Pass", false);
      					}
      					else{
      	   					 String tMsg="Actual Transaction Date : <b>[  "+ActualTransationDate+"  ]</b> does not matche with Expected Transaction Date : <b>[  "+ExpecteTransactionDate+"  ]</b>";
      	   					 TestUtil.reportStatus(tMsg, "Fail", false);
      					}
					for(int j=i;!exit.equalsIgnoreCase("Total");j++){
						String transactSumVal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[1]")).getText();
						exit = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[2]")).getText();
						if(transactSumVal.isEmpty()){
							covername= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
							td=8;
						}else{ 
							covername= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[9]")).getText();
							td=11;
						}
						
						if(covername.equalsIgnoreCase("Terrorism")){
							double Terrorism = calculateTerrorismTS(code,testName,"New Bussiness",j,td);
							Total = Total + Terrorism;
						}else if(covername.isEmpty()){
							double general = calculateOtherTS(testName,code,j,td,event,val);
							
							Total = Total + general;
						}
						if(exit.equalsIgnoreCase("Total")){
							i=j;
							String actualTotal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[4]")).getText();  
							compareValues(Double.parseDouble(actualTotal), Double.parseDouble(common.roundedOff(Double.toString(Total))), "Transaction Summary Total");
							if(event.contains("Renewal")){
								customAssert.assertTrue(WriteDataToXl(TestBase.product+"_Renewal", "Transaction Summary", (String)common.Renewal_excel_data_map.get("Automation Key"), "TS_TransactionSummaryTotal", actualTotal,common.Renewal_excel_data_map),"Error while writing Transaction Summary data to excel for MTA .");
							}else{
								customAssert.assertTrue(WriteDataToXl(event+"_MTA", "Transaction Summary", (String)common.MTA_excel_data_map.get("Automation Key"), "TS_TransactionSummaryTotal", actualTotal,common.MTA_excel_data_map),"Error while writing Transaction Summary data to excel for MTA .");
							}
							
	
							break outer;
						}
						}
					break;
					
				case "Amended New Business" : 
					
					TestUtil.reportStatus("Verification Started on Transaction Summary page for Amended New Business . ", "PASS", false);
					ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
										
					if(((String)common.NB_excel_data_map.get("PS_PaymentWarrantyRules")).equals("Yes") && !common.currentRunningFlow.equalsIgnoreCase("MTA")){
						ExpecteDueDate = (String)common.NB_excel_data_map.get("PS_PaymentWarrantyDueDate");
					}else{
						ExpecteDueDate = common.getLastDayOfMonth((String)common.NB_excel_data_map.get("QuoteDate"), 1);
					}
					
					if(ActualDueDate.equalsIgnoreCase(ExpecteDueDate)){
 					 String tMsg="Actual Due Date:"+ActualDueDate+" has been matched with Expected Due Date: "+ExpecteDueDate;
 					 TestUtil.reportStatus(tMsg, "Pass", false);
 					}
 					else{
 	   					 String tMsg="Actual Due Date:"+ActualDueDate+" does not matche with Expected Due Date: "+ExpecteDueDate;
 	   					 TestUtil.reportStatus(tMsg, "Fail", false);
 					}
					
					ActualTransationDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[3]")).getText();
					ExpecteTransactionDate = (String)common.NB_excel_data_map.get("QuoteDate");
					if(ActualTransationDate.equalsIgnoreCase(ExpecteTransactionDate)){
 					 String tMsg="Actual Transaction Date:"+ActualTransationDate+" has been matched with Expected Transaction Date: "+ExpecteTransactionDate;
 					 TestUtil.reportStatus(tMsg, "Pass", false);
 					}
 					else{
 	   					 String tMsg="Actual Transaction Date:"+ActualTransationDate+" does not matche with Expected Transaction Date: "+ExpecteTransactionDate;
 	   					 TestUtil.reportStatus(tMsg, "Fail", false);
 					}
					
					for(int j=i;!exit.equalsIgnoreCase("Total");j++){
						String transactSumVal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[1]")).getText();
						exit = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[2]")).getText();
						
						if(transactSumVal.isEmpty()){
							covername= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
							td=8;
						}else{ 
							covername= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[9]")).getText();
							td=11;
						}
						//System.out.println("Cover Name is :"+covername);
						
						//Cover specific Transaction Summary calculation.
						if(covername.equalsIgnoreCase("Terrorism")){
							double Terrorism = calculateTerrorismTS(code,testName,"New Bussiness",j,td);
							Total = Total + Terrorism;
						}
						else if(covername.isEmpty()){
							double general = calculateOtherTS(testName,code,j,td,event,val);	
							Total = Total + general;
						}
						
						if(exit.equalsIgnoreCase("Total")){
	  						i=j;
	  						String actualTotal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[4]")).getText();  
	  						common.compareValues(Double.parseDouble(actualTotal), Double.parseDouble(common.roundedOff(Double.toString(Total))), "Transaction Summary Total");
	  						customAssert.assertTrue(WriteDataToXl(event+"_NB", "Transaction Summary", (String)common.NB_excel_data_map.get("Automation Key"), "TS_TransactionSummaryTotal_Rewind", actualTotal,common.NB_excel_data_map),"Error while writing Transaction Summary data to excel .");
	
	  						break outer;
							
						}

					}
						
					
					break;
					
					//MTA Rewind
				case "Amended Endorsement":
					
					TestUtil.reportStatus("Verification Started on Transaction Summary page after Rewind Endorsement (MTA) . ", "Info", false);
					ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
					
					if(((String)common.NB_excel_data_map.get("PS_PaymentWarrantyRules")).equals("Yes") && !common.currentRunningFlow.equalsIgnoreCase("MTA")){
						ExpecteDueDate = (String)common.NB_excel_data_map.get("PS_PaymentWarrantyDueDate");
					}else{
						ExpecteDueDate = common.getLastDayOfMonth((String)common.NB_excel_data_map.get("QuoteDate"), 1);
					}
					
					if(ActualDueDate.equalsIgnoreCase(ExpecteDueDate)){
 					 String tMsg="Actual Due Date:"+ActualDueDate+" has been matched with Expected Due Date: "+ExpecteDueDate;
 					 TestUtil.reportStatus(tMsg, "Pass", false);
 					}
 					else{
 	   					 String tMsg="Actual Due Date:"+ActualDueDate+" does not matche with Expected Due Date: "+ExpecteDueDate;
 	   					 TestUtil.reportStatus(tMsg, "Fail", false);
 					}					ActualTransationDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[3]")).getText();
					ExpecteTransactionDate = (String)common.MTA_excel_data_map.get("QuoteDate");
					
					if(ActualTransationDate.equalsIgnoreCase(ExpecteTransactionDate)){
						String tMsg="Actual Transaction Date:"+ActualTransationDate+" has been matched with Expected Transaction Date: "+ExpecteTransactionDate;
						TestUtil.reportStatus(tMsg, "Pass", false);
 					}
 					else{
 	   					 String tMsg="Actual Transaction Date:"+ActualTransationDate+" does not matche with Expected Transaction Date: "+ExpecteTransactionDate;
 	   					 TestUtil.reportStatus(tMsg, "Fail", false);
 					}
					for(int j=i;!exit.equalsIgnoreCase("Total");j++){
						String transactSumVal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[1]")).getText();
						exit = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[2]")).getText();
						if(transactSumVal.isEmpty()){
							covername= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
							td=8;
						}else{ 
							covername= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[9]")).getText();
							td=11;
						}
						
						if(covername.equalsIgnoreCase("Terrorism")){
							double Terrorism = calculateTerrorismTS(code,testName,"Amended Endorsement",j,td);
							Total = Total + Terrorism;
						}
						else if(covername.isEmpty()){
							double general = calculateOtherTS(testName,"Amended Endorsement",j,td,event,val);	
							Total = Total + general;
						}
						if(exit.equalsIgnoreCase("Total")){
							i=j;
							String actualTotal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[4]")).getText();  
							common.compareValues(Double.parseDouble(actualTotal), Double.parseDouble(common.roundedOff(Double.toString(Total))), "Transaction Summary Total");
							customAssert.assertTrue(WriteDataToXl(event+"_MTA", "Transaction Summary", (String)common.MTA_excel_data_map.get("Automation Key"), "TS_TransactionSummaryTotal", actualTotal,common.MTA_excel_data_map),"Error while writing Transaction Summary data to excel for MTA .");
	
							break outer;
						}
					}
					break;
					
				case "Renewal" : 
					
					TestUtil.reportStatus("Verification Started on Transaction Summary page for Renewal. ", "Info", false);
					ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
 					ExpecteDueDate = common.getLastDayOfMonth((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), 1);
					if(ActualDueDate.equalsIgnoreCase(ExpecteDueDate)){
 					 String tMsg="Actual Due Date:"+ActualDueDate+" has been matched with Expected Due Date: "+ExpecteDueDate;
 					 TestUtil.reportStatus(tMsg, "Pass", false);
 					}
 					else{
 	   					 String tMsg="Actual Due Date:"+ActualDueDate+" does not matche with Expected Due Date: "+ExpecteDueDate;
 	   					 TestUtil.reportStatus(tMsg, "Fail", false);
 					}
					
					ActualTransationDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[3]")).getText();
					ExpecteTransactionDate = (String)common.Renewal_excel_data_map.get("QuoteDate");
					
					if(ActualTransationDate.equalsIgnoreCase(ExpecteTransactionDate)){
 					 String tMsg="Actual Transaction Date:"+ActualTransationDate+" has been matched with Expected Transaction Date: "+ExpecteTransactionDate;
 					 TestUtil.reportStatus(tMsg, "Pass", false);
 					}
 					else{
 	   					 String tMsg="Actual Transaction Date:"+ActualTransationDate+" does not matche with Expected Transaction Date: "+ExpecteTransactionDate;
 	   					 TestUtil.reportStatus(tMsg, "Fail", false);
 					}
					
					for(int j=i;!exit.equalsIgnoreCase("Total");j++){
						String transactSumVal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[1]")).getText();
						exit = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[2]")).getText();
						
						if(transactSumVal.isEmpty()){
							covername= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
							td=8;
						}else{ 
							covername= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[9]")).getText();
							td=11;
						}
						
						if(covername.equalsIgnoreCase("Terrorism")){
							double Terrorism = calculateTerrorismTS(code,testName,"New Bussiness",j,td);
							Total = Total + Terrorism;
						}
						else if(covername.isEmpty()){
							double general = calculateOtherTS(testName,code,j,td,event,val);	
							Total = Total + general;
						}
						
						if(exit.equalsIgnoreCase("Total")){
	  						i=j;
	  						String actualTotal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[4]")).getText();  
	  						common.compareValues(Double.parseDouble(actualTotal), Double.parseDouble(common.roundedOff(Double.toString(Total))), "Transaction Summary Total");
	  						customAssert.assertTrue(WriteDataToXl(event+"_Renewal", "Transaction Summary", (String)common.Renewal_excel_data_map.get("Automation Key"), "TS_TransactionSummaryTotal_Rewind", actualTotal,common.Renewal_excel_data_map),"Error while writing Transaction Summary data to excel .");
	
	  						break outer;
						
						}

					}
						
					
					break;	
					
				//Renewal Rewind
				case "Amended Renewal" : 
					
					TestUtil.reportStatus("Verification Started on Transaction Summary page for Renewal. ", "Info", false);
					ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
					ExpecteDueDate = common.getLastDayOfMonth((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), 1);
					if(ActualDueDate.equalsIgnoreCase(ExpecteDueDate)){
 					 String tMsg="Actual Due Date:"+ActualDueDate+" has been matched with Expected Due Date: "+ExpecteDueDate;
 					 TestUtil.reportStatus(tMsg, "Pass", false);
 					}
 					else{
 	   					 String tMsg="Actual Due Date:"+ActualDueDate+" does not matche with Expected Due Date: "+ExpecteDueDate;
 	   					 TestUtil.reportStatus(tMsg, "Fail", false);
 					}
					
					ActualTransationDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[3]")).getText();
					ExpecteTransactionDate = (String)common.Renewal_excel_data_map.get("QuoteDate");
					
					if(ActualTransationDate.equalsIgnoreCase(ExpecteTransactionDate)){
 					 String tMsg="Actual Transaction Date:"+ActualTransationDate+" has been matched with Expected Transaction Date: "+ExpecteTransactionDate;
 					 TestUtil.reportStatus(tMsg, "Pass", false);
 					}
 					else{
 	   					 String tMsg="Actual Transaction Date:"+ActualTransationDate+" does not matche with Expected Transaction Date: "+ExpecteTransactionDate;
 	   					 TestUtil.reportStatus(tMsg, "Fail", false);
 					}
					
					for(int j=i;!exit.equalsIgnoreCase("Total");j++){
						String transactSumVal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[1]")).getText();
						exit = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[2]")).getText();
						
						if(transactSumVal.isEmpty()){
							covername= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
							td=8;
						}else{ 
							covername= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[9]")).getText();
							td=11;
						}
						
						if(covername.equalsIgnoreCase("Terrorism")){
							double Terrorism = calculateTerrorismTS(code,testName,"New Bussiness",j,td);
							Total = Total + Terrorism;
						}
						else if(covername.isEmpty()){
							double general = calculateOtherTS(testName,code,j,td,event,val);	
							Total = Total + general;
						}
						if(exit.equalsIgnoreCase("Total")){
	  						i=j;
	  						String actualTotal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[4]")).getText();  
	  						compareValues(Double.parseDouble(actualTotal), Double.parseDouble(common.roundedOff(Double.toString(Total))), "Transaction Summary Total");
	  						customAssert.assertTrue(WriteDataToXl(event+"_Renewal", "Transaction Summary", (String)common.Renewal_excel_data_map.get("Automation Key"), "TS_TransactionSummaryTotal", actualTotal,common.Renewal_excel_data_map),"Error while writing Transaction Summary data to excel for MTA .");
	
							break outer;
						}
					}
					break;
					
				case "Cancel" :
					TestUtil.reportStatus("Verification Started on Transaction Summary page for Cancellation . ", "PASS", false);
					ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
				
					if(((String)common.NB_excel_data_map.get("PS_PaymentWarrantyRules")).equals("Yes") && !common.currentRunningFlow.equalsIgnoreCase("MTA")){
						ExpecteDueDate = (String)common.NB_excel_data_map.get("PS_PaymentWarrantyDueDate");
					}else{
						ExpecteDueDate = common.getLastDayOfMonth((String)common.CAN_excel_data_map.get("Cancellation_date"), 1);
					}
					
					if(ActualDueDate.equalsIgnoreCase(ExpecteDueDate)){
						String tMsg="Actual Due Date:"+ActualDueDate+" has been matched with Expected Due Date: "+ExpecteDueDate;
						TestUtil.reportStatus(tMsg, "Pass", false);
 					}
 					else{
 	   					 String tMsg="Actual Due Date:"+ActualDueDate+" does not matche with Expected Due Date: "+ExpecteDueDate;
 	   					 TestUtil.reportStatus(tMsg, "Fail", false);
 					}
					
					ActualTransationDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[3]")).getText();
					ExpecteTransactionDate = (String)common.NB_excel_data_map.get("QuoteDate");
					
					if(ActualTransationDate.equalsIgnoreCase(ExpecteTransactionDate)){
						String tMsg="Actual Transaction Date:"+ActualTransationDate+" has been matched with Expected Transaction Date: "+ExpecteTransactionDate;
						TestUtil.reportStatus(tMsg, "Pass", false);
 					}else{
 	   					 String tMsg="Actual Transaction Date:"+ActualTransationDate+" does not matche with Expected Transaction Date: "+ExpecteTransactionDate;
 	   					 TestUtil.reportStatus(tMsg, "Fail", false);
 					}
								
	   					for(int j=i;!exit.equalsIgnoreCase("Total");j++){
	   						String transactSumVal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[1]")).getText();
	   						exit = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[2]")).getText();
	   						if(transactSumVal.isEmpty()){
	   							covername= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
	   							td=8;
	   						}else{ 
	   							covername= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[9]")).getText();
	   							td=11;
	   						}
	   						
	   						if(covername.equalsIgnoreCase("Terrorism")){
	   							double Terrorism = calculateTerrorismTS(code,testName,"New Bussiness",j,td);
	   							Total = Total + Terrorism;
	   						}
	   						else if(covername.isEmpty()){
	   							double general = calculateOtherTS(testName,code,j,td,event,val);	
	   							Total = Total + general;
	   						}
	   						if(exit.equalsIgnoreCase("Total")){
		   						i=j;
		   						String actualTotal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[4]")).getText();  
		   						common.compareValues(Math.abs(Double.parseDouble(actualTotal)), Math.abs(Double.parseDouble(common.roundedOff(Double.toString(Total)))), "Transaction Summary Total");
		   						//customAssert.assertTrue(WriteDataToXl(event+"_NB", "Transaction Summary", (String)common.NB_excel_data_map.get("Automation Key"), "TS_TransactionSummaryTotal", actualTotal,common.NB_excel_data_map),"Error while writing Transaction Summary data to excel .");
	
		   						break outer;
	   						}
	   					}
						break;
				default :
						//System.out.println("In Default");
				}

			}
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Transaction Summary \n", t);
			return false;
		}
		
		TestUtil.reportStatus("Verification Completed successful on Transaction Summary page . ", "Info", false);
		
		return retvalue;

	}

public double calculateGeneralTS(String recipient, Map<Object,Object> data_map, String account,int j, int td, String event,String code,String fileName){
	try{
			String terrorPremium = null,terrorIPT = null,totalGrossPremium = null,totalGrossIPT = null;
				
			String product = common.product;
			String part1= "//*[@id='table0']/tbody";
			
			switch (TestBase.businessEvent){
				case "NB" :
					if(((String)data_map.get("CD_Terrorism")).equalsIgnoreCase("Yes")){
						
						if(common_CTB.isTransTable){
							terrorPremium = Double.toString(common_CTB.transaction_Premium_Values.get("Terrorism").get("Gross Premium (GBP)"));
							terrorIPT = Double.toString(common_CTB.transaction_Premium_Values.get("Terrorism").get("Gross IPT (GBP)"));
						}else{
							terrorPremium = (String)data_map.get("PS_Terrorism_GP");
							terrorIPT = (String)data_map.get("PS_Terrorism_GT");
						}
						
					}else{
						terrorPremium="0.00";
						terrorIPT="0.00";
					}
					
					if(common_CTB.isTransTable){
						totalGrossPremium = Double.toString(common_CTB.transaction_Premium_Values.get("Totals").get("Gross Premium (GBP)"));
						totalGrossIPT = Double.toString(common_CTB.transaction_Premium_Values.get("Totals").get("Gross IPT (GBP)"));
					}else{
						totalGrossPremium = (String)data_map.get("PS_Total_GP");
						totalGrossIPT = (String)data_map.get("PS_Total_GT");
					}
						
					break;
						
				case "MTA" :
					
						if(common.currentRunningFlow.contains("NB")){
							if(((String)data_map.get("CD_Terrorism")).equalsIgnoreCase("Yes")){
								
								if(common_CTB.isTransTable){
									terrorPremium = Double.toString(common_CTB.transaction_Premium_Values.get("Terrorism").get("Gross Premium (GBP)"));
									terrorIPT = Double.toString(common_CTB.transaction_Premium_Values.get("Terrorism").get("Gross IPT (GBP)"));
								}else{
									terrorPremium = (String)data_map.get("PS_Terrorism_GP");
									terrorIPT = (String)data_map.get("PS_Terrorism_GT");
								}
								
							}else{
								terrorPremium="0.00";
								terrorIPT="0.00";
							}
							
							if(common_CTB.isTransTable){
								totalGrossPremium = Double.toString(common_CTB.transaction_Premium_Values.get("Totals").get("Gross Premium (GBP)"));
								totalGrossIPT = Double.toString(common_CTB.transaction_Premium_Values.get("Totals").get("Gross IPT (GBP)"));
							}else{
								totalGrossPremium = (String)data_map.get("PS_Total_GP");
								totalGrossIPT = (String)data_map.get("PS_Total_GT");
							}
						}else if(common.currentRunningFlow.contains("MTA") ||(common.currentRunningFlow.contains("Rewind") && common.businessEvent.contains("MTA"))){
							
							if(common.transaction_Details_Premium_Values.get("Terrorism")!=null){
								terrorPremium = Double.toString(common.transaction_Details_Premium_Values.get("Terrorism").get("Gross Premium (GBP)"));
								terrorIPT = Double.toString(common.transaction_Details_Premium_Values.get("Terrorism").get("Gross IPT (GBP)"));
							}else{
								terrorPremium="0.00";
								terrorIPT="0.00";
							}
							totalGrossPremium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium (GBP)"));
							totalGrossIPT = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Gross IPT (GBP)"));
						}else if(common.currentRunningFlow.contains("Rewind")){
						
						}
							
						break;
					
				case "CAN" :
						if(common.currentRunningFlow.contains("NB")){
							if(((String)data_map.get("CD_Terrorism")).equalsIgnoreCase("Yes")){
								
								if(common_CTB.isTransTable){
									terrorPremium = Double.toString(common_CTB.transaction_Premium_Values.get("Terrorism").get("Gross Premium (GBP)"));
									terrorIPT = Double.toString(common_CTB.transaction_Premium_Values.get("Terrorism").get("Gross IPT (GBP)"));
								}else{
									terrorPremium = (String)data_map.get("PS_Terrorism_GP");
									terrorIPT = (String)data_map.get("PS_Terrorism_GT");
								}
								
							}else{
								terrorPremium="0.00";
								terrorIPT="0.00";
							}
							
							if(common_CTB.isTransTable){
								totalGrossPremium = Double.toString(common_CTB.transaction_Premium_Values.get("Totals").get("Gross Premium (GBP)"));
								totalGrossIPT = Double.toString(common_CTB.transaction_Premium_Values.get("Totals").get("Gross IPT (GBP)"));
							}else{
								totalGrossPremium = (String)data_map.get("PS_Total_GP");
								totalGrossIPT = (String)data_map.get("PS_Total_GT");
							}
						}else{
							if(TestBase.product.contains("CTB")){
								if(common_CTB.CAN_CTB_ReturnP_Values_Map.get("Terrorism")!=null){
									terrorPremium = String.valueOf(common_CTB.CAN_CTB_ReturnP_Values_Map.get("Terrorism").get("Gross Premium (GBP)"));
									terrorIPT = String.valueOf(common_CTB.CAN_CTB_ReturnP_Values_Map.get("Terrorism").get("Gross IPT (GBP)"));
								}else{
									terrorPremium = "0.00";
									terrorIPT = "0.00";
								}
								
							}else if(TestBase.product.contains("POE")){
								totalGrossPremium = (String)data_map.get("PS_Total_GP");
								totalGrossIPT = (String)data_map.get("PS_Total_GT");
							}
							
							if(common_CTB.isTransTable){
								totalGrossPremium = Double.toString(common_CTB.transaction_Premium_Values.get("Totals").get("Gross Premium (GBP)"));
								totalGrossIPT = Double.toString(common_CTB.transaction_Premium_Values.get("Totals").get("Gross IPT (GBP)"));
							}else{
								totalGrossPremium = (String)common.NB_excel_data_map.get("PS_Total_GP");
								totalGrossIPT = (String)common.NB_excel_data_map.get("PS_Total_GT");
							}						
						
						}
						break;
						
				case "Renewal" :
					if(common.currentRunningFlow.contains("MTA")){
						
						if(common.transaction_Details_Premium_Values.get("Terrorism")!=null){
							terrorPremium = Double.toString(common.transaction_Details_Premium_Values.get("Terrorism").get("Gross Premium (GBP)"));
							terrorIPT = Double.toString(common.transaction_Details_Premium_Values.get("Terrorism").get("Gross IPT (GBP)"));
						}else{
							terrorPremium="0.00";
							terrorIPT="0.00";
						}
						totalGrossPremium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium (GBP)"));
						totalGrossIPT = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Gross IPT (GBP)"));
					}else{
						if(((String)data_map.get("CD_Terrorism")).equalsIgnoreCase("Yes")){
							terrorPremium = (String)data_map.get("PS_Terrorism_GP");
							terrorIPT = (String)data_map.get("PS_Terrorism_GT");
						}else{
							terrorPremium="0.00";
							terrorIPT="0.00";
						}
							totalGrossPremium = (String)data_map.get("PS_Total_GP");
							totalGrossIPT = (String)data_map.get("PS_Total_GT");
							
					}
					break;
						
				case "Rewind" :
					
					if(((String)data_map.get("CD_Terrorism")).equalsIgnoreCase("Yes")){
						
						if(common_CTB.isTransTable){
							terrorPremium = Double.toString(common_CTB.transaction_Premium_Values.get("Terrorism").get("Gross Premium (GBP)"));
							terrorIPT = Double.toString(common_CTB.transaction_Premium_Values.get("Terrorism").get("Gross IPT (GBP)"));
						}else{
							terrorPremium = (String)data_map.get("PS_Terrorism_GP");
							terrorIPT = (String)data_map.get("PS_Terrorism_GT");
						}
						
					}else{
						terrorPremium="0.00";
						terrorIPT="0.00";
					}
					
					if(common_CTB.isTransTable){
						totalGrossPremium = Double.toString(common_CTB.transaction_Premium_Values.get("Totals").get("Gross Premium (GBP)"));
						totalGrossIPT = Double.toString(common_CTB.transaction_Premium_Values.get("Totals").get("Gross IPT (GBP)"));
					}else{
						totalGrossPremium = (String)data_map.get("PS_Total_GP");
						totalGrossIPT = (String)data_map.get("PS_Total_GT");
					}
					break;
				
			}
						
				
			String general= driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+td+"]")).getText();
			String generalInsTax= driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+1)+"]")).getText();
			String generalDue= driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+2)+"]")).getText();
			
			
			if(terrorPremium == null) {terrorPremium = "0.00";}
			if(terrorIPT == null) {terrorIPT = "0.00";}
			
			if(!common.currentRunningFlow.equalsIgnoreCase("CAN")){
				if(((String)data_map.get("CD_Terrorism")).equals("No") ||(String)data_map.get("CD_Terrorism")== null )
				{
					terrorPremium="0.00";
					terrorIPT="0.00";
				} 
			}
			
			double generalPremium = Double.parseDouble(totalGrossPremium) - (Double.parseDouble(terrorPremium));		
			double generalIPT = Double.parseDouble(totalGrossIPT) - (Double.parseDouble(terrorIPT));

			if(recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc") && account.equalsIgnoreCase("R066"))
			{
				
				double[] values = OtherCalculation(recipient,Double.toString(generalPremium), Double.toString(generalIPT));
				compareValues(values[0], Double.parseDouble(general), "General RAS Amount");
				compareValues(Double.parseDouble(generalInsTax), values[1], "General RAS IPT");
				double actualDue = values[0] + values[1];
				String dueAmmout =  common.roundedOff(Double.toString(actualDue));
				compareValues(Double.parseDouble(generalDue), Double.parseDouble(generalDue), "General RAS Due Amount");
				return Double.parseDouble(dueAmmout);
			}
			else if(recipient.equalsIgnoreCase("Arthur J Gallagher (UK) Limited") && account.equalsIgnoreCase("A324"))
			{
				double[] values = OtherCalculation(recipient,Double.toString(generalPremium), Double.toString(generalIPT));
				compareValues(values[0], Double.parseDouble(general), "General AJG Amount");
				compareValues(Double.parseDouble(generalInsTax), values[1], "General AJG IPT ");
				double actualDue = values[0] + values[1];
				String dueAmmout = common.roundedOff(Double.toString(actualDue));
				compareValues(Double.parseDouble(generalDue), Double.parseDouble(dueAmmout), "General AJG Due Amount");
				return Double.parseDouble(dueAmmout);
			}
			
			return 0.00;
	
	}catch(Throwable t) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
		Assert.fail("Failed in Calculate General preimum for genral covers \n", t);
		return 0;
	}
}

public double[] OtherCalculation(String split,String premiumAmt, String ipt){
	try{
		
		Map<Object,Object> data_map = null;
		
		switch (common.currentRunningFlow) {
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
			case "CAN":
				data_map = common.NB_excel_data_map;
				break;
		}
	
		String product = common.product;
		String leadCarrier = "";
		if(TestBase.product.equalsIgnoreCase("POE")){
			leadCarrier = (String)data_map.get("PD_businessEP");
		}else{
			leadCarrier = (String)data_map.get("PD_CarrierOverride");
		}
		 
		String splitRate ="1.00";
		String Commission = "1.00";
		
		if(split.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
				if(leadCarrier.equalsIgnoreCase("Yes"))
				{
					Commission = (String)data_map.get("TS_RSASplitCC-A");
					
				}else if(leadCarrier.equalsIgnoreCase("No")){
					Commission = (String)data_map.get("TS_RSASplitCC-A");
				}
		}else if(split.equalsIgnoreCase("Arthur J Gallagher (UK) Limited")){
				if(leadCarrier.equalsIgnoreCase("Yes"))
				{
					Commission = (String)data_map.get("TS_AJGSplitCC-A");
					
				}else if(leadCarrier.equalsIgnoreCase("No")){
					Commission = "0.75";					
				}
		}
		Commission = "0.75";
		
		double Premium = 0.00;
		double IPT = 0.00;
		
		Premium =  Double.parseDouble(premiumAmt) * Double.parseDouble(Commission);
		IPT =  Double.parseDouble(ipt) ;
	
	
	if(common.currentRunningFlow.equals("CAN")){
		return new double[] {-Premium,-IPT};
	}
	
	return new double[] {Premium, IPT};
		
	}catch(Throwable t) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
		k.reportErr("Failed in "+methodName+" function", t);
		Assert.fail("Failed in Calculate Terrorisam ammount according to Split.  \n", t);
		return new double[] {0, 0};
	}
	}


public double calculateBrokeageAmountTS(String recipient,Map<Object,Object> data_map, String account,int j, int td){
	double materialDamageFP = 0.00, businessInteruptionFP=0.00, EmployersLiabiliyFP=0.00, PublicLiabilityFP=0.00, ContractorAllRisksFP=0.00;
	double ProductLiability =0.00, ComputersAndElectronicRiskFP=0.00, MoneyFP =0.00, GoodsInTansitFP=0.00,FidilityGuaranteFP=0.00;
	double LegalExpensesFP=0.00, terrorismFP=0.00, DirectorsAndOfficersFP=0.00, SpecifiedRisksFP=0.00, generalPremium;
	double MarineCargoFP=0.00, FrozenFoodFP=0.00, LossofLicenceFP=0.00, LOIFP = 0.00,PropertyOwnersLiability=0.00, PropertyOwnersLiabilityFP=0.00,LossOfRentalIncomeFP=0.00;
	double PropertyExcessofLossFP=0.00;
	
	String part1= "//*[@id='table0']/tbody";
	String GrossPremium = null, BrokerCommission = null, NetPremium = null;
	double[] values = {0.00, 0.00};
	
	try{
		
		if(common.NB_excel_data_map == null){
			common.NB_excel_data_map = data_map;
		}
		
		switch (common.currentRunningFlow) {
			
			case "NB":
			case "CAN":
			case "Rewind":
				data_map = common.NB_excel_data_map;
				break;
			case "Requote":
				data_map = common.Requote_excel_data_map;
				break;
			case "Renewal":
				data_map = common.Renewal_excel_data_map;
				break;
			case "MTA":
				data_map = common.MTA_excel_data_map;
				break;
			
			}
				
		String brokerageAccount= driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+td+"]")).getText();
		String grossCommTotal =  "25.00";//(String)data_map.get("TS_GrossCommRate");
		
		
		if((String)data_map.get("CD_MaterialDamage")!= null ){
			values = GetValuesTs("Material Damage",data_map);
			materialDamageFP = calculateGeneralPremium(String.valueOf(values[0]),String.valueOf(values[1]),grossCommTotal);				
		}
		
		if(!TestBase.product.equalsIgnoreCase("CTB")){
			
			if((String)data_map.get("CD_LossOfRentalIncome")!= null){
				values = GetValuesTs("Loss Of Rental Income",data_map);
				LOIFP = calculateGeneralPremium(String.valueOf(values[0]),String.valueOf(values[1]),grossCommTotal);
			}				
		}
		
		if((String)data_map.get("CD_BusinessInterruption")!= null ){			
			values = GetValuesTs("Business Interruption",data_map);
			businessInteruptionFP = calculateGeneralPremium(String.valueOf(values[0]),String.valueOf(values[1]),grossCommTotal);	
		}
		
		if((String)data_map.get("CD_Liability")!= null ){
			values = GetValuesTs("Employers Liability",data_map);			
			EmployersLiabiliyFP = calculateGeneralPremium(String.valueOf(values[0]),String.valueOf(values[1]),grossCommTotal);	
		}		
	
		if(!TestBase.product.equalsIgnoreCase("POE")){
			
			if((String)data_map.get("CD_Liability")!= null ){
				values = GetValuesTs("Public Liability",data_map);
				PublicLiabilityFP = calculateGeneralPremium(String.valueOf(values[0]),String.valueOf(values[1]),grossCommTotal);	
			
				values = GetValuesTs("Products Liability",data_map);
				ProductLiability = calculateGeneralPremium(String.valueOf(values[0]),String.valueOf(values[1]),grossCommTotal);
			}		
		}
		
		if(!TestBase.product.equalsIgnoreCase("CTB") && !TestBase.product.equalsIgnoreCase("CCI")){
			
			if((String)data_map.get("CD_Liability")!= null ){
				values = GetValuesTs("Property Owners Liability",data_map);
				PropertyOwnersLiabilityFP = calculateGeneralPremium(String.valueOf(values[0]),String.valueOf(values[1]),grossCommTotal);
			}
		}
		
		if((String)data_map.get("CD_ContractorsAllRisks")!= null ){
			values = GetValuesTs("Contractors All Risks",data_map);
			ContractorAllRisksFP = calculateGeneralPremium(String.valueOf(values[0]),String.valueOf(values[1]),grossCommTotal);	
		}
		
		if((String)data_map.get("CD_SpecifiedAllRisks")!= null ){
			values = GetValuesTs("Specified All Risks",data_map);
			SpecifiedRisksFP = calculateGeneralPremium(String.valueOf(values[0]),String.valueOf(values[1]),grossCommTotal);	
		}
		
		if((String)data_map.get("CD_ComputersandElectronicRisks")!= null ){
			values = GetValuesTs("Computers and Electronic Risks",data_map);
			ComputersAndElectronicRiskFP = calculateGeneralPremium(String.valueOf(values[0]),String.valueOf(values[1]),grossCommTotal);	
		}
		
		if((String)data_map.get("CD_Money")!= null ){
			values = GetValuesTs("Money",data_map);
			MoneyFP = calculateGeneralPremium(String.valueOf(values[0]),String.valueOf(values[1]),grossCommTotal);	
		}
		
		if((String)data_map.get("CD_GoodsInTransit")!= null ){
			values = GetValuesTs("Goods In Transit",data_map);
			GoodsInTansitFP = calculateGeneralPremium(String.valueOf(values[0]),String.valueOf(values[1]),grossCommTotal);	
		}
		
		if((String)data_map.get("CD_FidelityGuarantee")!= null ){
			values = GetValuesTs("Fidelity Guarantee",data_map);
			FidilityGuaranteFP = calculateGeneralPremium(String.valueOf(values[0]),String.valueOf(values[1]),grossCommTotal);	
		}
		
		if(TestBase.product.contains("CCI") && (String)data_map.get("CD_LossofLicence")!= null ){
			values = GetValuesTs("Loss of Licence",data_map);
			LossofLicenceFP = calculateGeneralPremium(String.valueOf(values[0]),String.valueOf(values[1]),grossCommTotal);	
		}
		
		if(TestBase.product.contains("CCI") && (String)data_map.get("CD_FrozenFood")!= null ){
			values = GetValuesTs("Frozen Foods",data_map);
			FrozenFoodFP = calculateGeneralPremium(String.valueOf(values[0]),String.valueOf(values[1]),grossCommTotal);	
		}
		
		if((String)data_map.get("CD_Terrorism")!= null ){
			values = GetValuesTs("Terrorism",data_map);
			terrorismFP = values[1];
		}
		
		
		generalPremium= (materialDamageFP + businessInteruptionFP + EmployersLiabiliyFP 
				+ PublicLiabilityFP +  ContractorAllRisksFP + SpecifiedRisksFP + ProductLiability 
				+ ComputersAndElectronicRiskFP + MoneyFP + GoodsInTansitFP + PropertyOwnersLiabilityFP + LOIFP + FidilityGuaranteFP) + terrorismFP + LossofLicenceFP + FrozenFoodFP;
		
		String generalammount = decim.format(generalPremium);		
	
		compareValues(Math.abs(Double.parseDouble(generalammount)), Math.abs(Double.parseDouble(brokerageAccount)), "General Brokerage Amount ");
		
		return Double.parseDouble(generalammount);
			
	}catch(Throwable t) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
		Assert.fail("Failed in Calculate Brokerage ammout. \n", t);
		return 0;
	}
}

public double[] GetValuesTs(String sCoverName, Map<Object,Object> data_map){
	try{
		String GrossPremium = null, BrokerCommission = null, NetPremium = null, cover_Name = null;
		if(sCoverName.contains("Liability")){
			cover_Name = "Liability";
		}else if(sCoverName.contains("Frozen Foods")){
			cover_Name = "Frozen Food";
		}else{		
			cover_Name = sCoverName;
		}
		double terrorismFP =0.00;
		
		if(!sCoverName.contains("Terrorism")){
			switch (common.currentRunningFlow){
				case "NB" :				
				case "Renewal" :
				case "Requote" :
					if(((String)data_map.get("CD_"+cover_Name.replaceAll(" ", ""))).equalsIgnoreCase("Yes")){
						if(common_CTB.isTransTable){
							GrossPremium = Double.toString(common_CTB.transaction_Premium_Values.get(sCoverName).get("Gross Premium (GBP)"));
							BrokerCommission = Double.toString(common_CTB.transaction_Premium_Values.get(sCoverName).get("Com. Rate (%)"));
						}else{
							if(sCoverName.contains("Loss of Licence")){
								sCoverName = "LossOfLicence";
							}
							GrossPremium = (String)data_map.get("PS_"+sCoverName.replaceAll(" ", "")+"_GP");
							BrokerCommission = (String)data_map.get("PS_"+sCoverName.replaceAll(" ", "")+"_CR");
						}
					}else{
						GrossPremium = "0.00";
						BrokerCommission = "0.00";
					}					
					break;
					
				case "CAN" :
					
					if(common.currentRunningFlow.contains("NB")){
						if(common_CTB.isTransTable){
							GrossPremium = Double.toString(common_CTB.transaction_Premium_Values.get(sCoverName).get("Gross Premium (GBP)"));
							NetPremium = Double.toString(common_CTB.transaction_Premium_Values.get(sCoverName).get("Net Premium (GBP)"));
						}else{
							GrossPremium = (String)data_map.get("PS_"+sCoverName.replaceAll(" ", "")+"_GP");
							BrokerCommission = (String)data_map.get("PS_"+sCoverName.replaceAll(" ", "")+"_CR");
						}
					
					}else{
						
						if(common_CTB.CAN_CTB_ReturnP_Values_Map.get(sCoverName)!=null){
							NetPremium = String.valueOf(common_CTB.CAN_CTB_ReturnP_Values_Map.get(sCoverName).get("Net Premium (GBP)"));
							GrossPremium = String.valueOf(common_CTB.CAN_CTB_ReturnP_Values_Map.get(sCoverName).get("Gross Premium (GBP)"));								
						}else{
							NetPremium = "0.00";
							GrossPremium = "0.00";
						}							
					}
					break;
					
				case "MTA" :
					
					if(common.transaction_Details_Premium_Values.get(sCoverName)!= null){
						GrossPremium = Double.toString(  common.transaction_Details_Premium_Values.get(sCoverName).get("Gross Premium (GBP)"));
						BrokerCommission = Double.toString( common.transaction_Details_Premium_Values.get(sCoverName).get("Com. Rate (%)"));
					}else{
						GrossPremium = "0.00";
						BrokerCommission= "0.00";
					}
					
					break;
				
				case "Rewind" :
					String coverNB = (String)common.NB_excel_data_map.get("CD_"+cover_Name.replaceAll(" ", ""));
					String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_"+cover_Name.replaceAll(" ", ""));
					
					if(coverNBRewind.equalsIgnoreCase("Yes")){
						GrossPremium = String.valueOf(common_CTB.PremiumSummary_Values.get(sCoverName.replaceAll(" ", "")).get("PS_"+sCoverName.replaceAll(" ", "")+"_GP"));
						BrokerCommission = String.valueOf(common_CTB.PremiumSummary_Values.get(sCoverName.replaceAll(" ", "")).get("PS_"+sCoverName.replaceAll(" ", "")+"_CR"));					
					}else{
						GrossPremium = "0.00";
						BrokerCommission= "0.00";
					}
					break;
					
				}
			
		}else{
			if((String)data_map.get("CD_"+sCoverName)!= null){
				String cedeComm = null;
				switch (common.currentRunningFlow){
					case "NB" :			
					case "Renewal" :
					case "Requote" :
						
						if(((String)data_map.get("CD_"+sCoverName.replaceAll(" ", ""))).equalsIgnoreCase("Yes")){
							if(common_CTB.isTransTable){
								GrossPremium = Double.toString(common_CTB.transaction_Premium_Values.get(sCoverName).get("Gross Premium (GBP)"));
								NetPremium = Double.toString(common_CTB.transaction_Premium_Values.get(sCoverName).get("Net Premium (GBP)"));
							}else{
								GrossPremium = (String)data_map.get("PS_"+sCoverName.replaceAll(" ", "")+"_GP");
								NetPremium = (String)data_map.get("PS_"+sCoverName.replaceAll(" ", "")+"_NP");
							}
						}else{
							GrossPremium = "0.00";
							NetPremium= "0.00";
						}
						
						cedeComm = (String)data_map.get("TER_CedeComm");
						break;
							
					case "CAN" :
						if(common.currentRunningFlow.contains("NB")){
							if(common_CTB.isTransTable){
								GrossPremium = Double.toString(common_CTB.transaction_Premium_Values.get("Terrorism").get("Gross Premium (GBP)"));
								NetPremium = Double.toString(common_CTB.transaction_Premium_Values.get("Terrorism").get("Net Premium (GBP)"));
							}else{
								GrossPremium = (String)data_map.get("PS_Terrorism_GP");
								NetPremium = (String)data_map.get("PS_Terrorism_NP");
							}
						
						}else{
							
							if(common_CTB.CAN_CTB_ReturnP_Values_Map.get("Terrorism")!=null){
								NetPremium = String.valueOf(common_CTB.CAN_CTB_ReturnP_Values_Map.get("Terrorism").get("Net Premium (GBP)"));
								GrossPremium = String.valueOf(common_CTB.CAN_CTB_ReturnP_Values_Map.get("Terrorism").get("Gross Premium (GBP)"));								
							}else{
								NetPremium = "0.00";
								GrossPremium = "0.00";
							}							
						}
						cedeComm = (String)data_map.get("TER_CedeComm");
						break;
						
					case "MTA" :
						
						cedeComm = (String)common.MTA_excel_data_map.get("TER_CedeComm");
						
						if(common.transaction_Details_Premium_Values.get("Terrorism")!=null){
							GrossPremium =   Double.toString(  common.transaction_Details_Premium_Values.get("Terrorism").get("Gross Premium (GBP)"));
							NetPremium =   Double.toString( common.transaction_Details_Premium_Values.get("Terrorism").get("Net Premium (GBP)"));
							
						}else{
							GrossPremium = "0.00";
							BrokerCommission= "0.00";
						}
						break;
					
					case "Rewind" :
						if(common.businessEvent.contains("MTA")){
							if(common.transaction_Details_Premium_Values.get("Terrorism")!=null){
								GrossPremium =   Double.toString(  common.transaction_Details_Premium_Values.get("Terrorism").get("Gross Premium (GBP)"));
								NetPremium =   Double.toString( common.transaction_Details_Premium_Values.get("Terrorism").get("Net Premium (GBP)"));								
							}else{
								GrossPremium = "0.00";
								BrokerCommission= "0.00";
							}
							cedeComm = (String)common.Rewind_excel_data_map.get("TER_CedeComm");
							
						}else{
							String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_Terrorism");
							cedeComm = (String)common.Rewind_excel_data_map.get("TER_CedeComm");
							
							if(coverNBRewind.equalsIgnoreCase("No")){
								GrossPremium = "0.00";
								NetPremium = "0.00"; 
							}else{
								GrossPremium = String.valueOf(common_CTB.PremiumSummary_Values.get(sCoverName.replaceAll(" ", "")).get("PS_"+sCoverName.replaceAll(" ", "")+"_GP"));
								NetPremium = String.valueOf(common_CTB.PremiumSummary_Values.get(sCoverName.replaceAll(" ", "")).get("PS_"+sCoverName.replaceAll(" ", "")+"_NP"));
								
							}
							cedeComm = (String)common.Rewind_excel_data_map.get("TER_CedeComm");
						}
						break;
				}
				
				if(cedeComm.equalsIgnoreCase("Yes")){
					terrorismFP = 0.00;
				}else if(GrossPremium.contains("0.00")){
					terrorismFP = 0.00;
				}else{					
					terrorismFP= Double.parseDouble(NetPremium) * 0.1;
				}
			}
		}
		
		if(common.currentRunningFlow.contentEquals("CAN")){
			if(sCoverName.contains("Terrorism")){
				return new double[] {-Double.parseDouble(GrossPremium), -terrorismFP};
			}else{
				return new double[] {-Double.parseDouble(GrossPremium), -Double.parseDouble(BrokerCommission)};
			}				
		}else if(sCoverName.contains("Terrorism")){
			return new double[] {Double.parseDouble(GrossPremium), terrorismFP};
		}else{
			return new double[] {Double.parseDouble(GrossPremium), Double.parseDouble(BrokerCommission)};
		}
			
		
	}catch(Throwable t) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
		k.reportErr("Failed in "+methodName+" function", t);
		Assert.fail("Failed in Calculate Terrorisam ammount according to Split.  \n", t);
		return new double[] {0, 0};
	}
}


public double calculateGeneralPremium(String grossPremium, String brokeCommision, String CommRateTotal){
	try{
		if(grossPremium.contains("--")){
			grossPremium= grossPremium.replace("--", "-");
		}
		double grossCommRate = Double.parseDouble(CommRateTotal);
		
		return(Double.parseDouble(grossPremium )*((grossCommRate- (Double.parseDouble(brokeCommision)))/100));
	
		
	}catch(Throwable t) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
		Assert.fail("Failed in Calculate General preimum for genral covers \n", t);
		return 0;
	}
}

public double calculateOtherTS(String testName,String code ,int j,int td,String event,String type){
	double calAmount =0.00;
	String part1= "//*[@id='table0']/tbody";
	double BrokerageAmount = 0.00;
	double generalAmount = 0.0;
	try{
		
		
		Map<Object,Object> data_map = null;
		
		switch (common.currentRunningFlow) {
			case "NB":
				data_map = common.NB_excel_data_map;
				break;
			case "MTA":
				data_map = common.MTA_excel_data_map;
				break;
			case "Renewal":
				data_map = common.Renewal_excel_data_map;
				break;
			case "Requote":
				data_map = common.Requote_excel_data_map;
				break;
			case "CAN":
				data_map = common.NB_excel_data_map;
				break;
			case "Rewind":
					data_map = common.Rewind_excel_data_map;
					break;
		}
			
		String recipient = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td-5)+"]")).getText();
		String account = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td-4)+"]")).getText();
		
		if(account.equalsIgnoreCase("R066")||account.equalsIgnoreCase("A324")||account.equalsIgnoreCase("AA750")||account.equalsIgnoreCase("Z906")){
			
			generalAmount = calculateGeneralTS(recipient,data_map,account,j,td,event,code,testName);
			calAmount =  generalAmount;
			
		}else if((recipient.equalsIgnoreCase("Brokerage Account")) && account.equalsIgnoreCase("Z001")){// This change has been made for CR274 in SPI product
			if(TestBase.product.equals("POE")){
				BrokerageAmount = common_POE.calculateBrokeageAmountTSPOE(recipient,data_map,account,j,td);
				calAmount =  BrokerageAmount;
			}else{
			BrokerageAmount = calculateBrokeageAmountTS(recipient,data_map,account,j,td);
			calAmount =  BrokerageAmount;
			}
		}
		
		return calAmount;
	
	}catch(Throwable t) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
		Assert.fail("Failed in Other premiums calculation on Transaction summary page. \n", t);
		return 0.00;
	}
	

}

public double calculateTerrorismTS(String fileName,String testName,String code ,int j,int td){
	try{
		String terrorPremium , terrorIPT;
		
		Map<Object,Object> data_map = null;
			
			switch (common.currentRunningFlow) {
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
				case "CAN":
						data_map = common.CAN_excel_data_map;
						break;
				}
			
		if(common.currentRunningFlow.equals("NB")){
			if(common_CTB.isTransTable){
				terrorPremium = Double.toString(common_CTB.transaction_Premium_Values.get("Terrorism").get("Net Premium (GBP)"));
				terrorIPT = Double.toString(common_CTB.transaction_Premium_Values.get("Terrorism").get("Gross IPT (GBP)"));
			}else{
				terrorPremium = (String)data_map.get("PS_Terrorism_NP");
				terrorIPT = (String)data_map.get("PS_Terrorism_GT");
			}
		}else if(common.currentRunningFlow.equals("MTA")){
			if(common.transaction_Details_Premium_Values.get("Terrorism") == null){
				terrorPremium = "0.00";
				terrorIPT = "0.00";
			}else{
				terrorPremium = Double.toString(common.transaction_Details_Premium_Values.get("Terrorism").get("Net Premium (GBP)"));
				terrorIPT = Double.toString(common.transaction_Details_Premium_Values.get("Terrorism").get("Gross IPT (GBP)"));
			}
		}else if(common.currentRunningFlow.contains("CAN")){
			if(TestBase.product.contains("CTB")){
				terrorPremium = String.valueOf(common_CTB.CAN_CTB_ReturnP_Values_Map.get("Terrorism").get("Net Premium (GBP)"));
				terrorIPT = String.valueOf(common_CTB.CAN_CTB_ReturnP_Values_Map.get("Terrorism").get("Gross IPT (GBP)"));
			}else{
				terrorPremium = (String)data_map.get("PS_Terrorism_NP");
				terrorIPT = (String)data_map.get("PS_Terrorism_GT");
			}			
		}else if(common.currentRunningFlow.contains("Rewind") && TestBase.businessEvent.equals("MTA") && TestBase.product.equals("POE")){
			if(common.transaction_Details_Premium_Values.get("Terrorism") == null){
				terrorPremium = "0.00";
				terrorIPT = "0.00";
			}else{
				terrorPremium = Double.toString(common.transaction_Details_Premium_Values.get("Terrorism").get("Net Premium (GBP)"));
				terrorIPT = Double.toString(common.transaction_Details_Premium_Values.get("Terrorism").get("Gross IPT (GBP)"));
			}		
		}else{
			terrorPremium = (String)data_map.get("PS_Terrorism_NP");
			terrorIPT = (String)data_map.get("PS_Terrorism_GT");
		}		
		
		String policy_status_actual = k.getText("Policy_status_header");
		
		String part1= "//*[@id='table0']/tbody";		
		String recipient = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td-5)+"]")).getText();
		String actualTerrorPremium= driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+td+"]")).getText();
		String actualTerrorIPT = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+1)+"]")).getText();
		String actualerrorDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+2)+"]")).getText();
		String product = common.product;		
		
		if(recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")) 
		{
			double[] values = TerrorismCalculation(recipient,terrorPremium, terrorIPT);
			compareValues(Double.parseDouble(actualTerrorPremium), values[0], "Terrorism RSA Amount ");
			compareValues(Double.parseDouble(actualTerrorIPT), values[1], "Terrorism RSA IPT ");
			double terrorRSADue=values[0] + values[1];
			compareValues(Double.parseDouble(actualerrorDue), terrorRSADue, "Terrorism RSA Due ");
   			return  Double.parseDouble(common.roundedOff(Double.toString(terrorRSADue)));
		}
		else if(recipient.equalsIgnoreCase("Arthur J Gallagher (UK) Limited"))
		{
			double[] values = TerrorismCalculation(recipient,terrorPremium, terrorIPT); 
			compareValues(Math.abs(Double.parseDouble(actualTerrorPremium)), Math.abs(values[0]), "Terrorism AJG Amount ");
			compareValues(Math.abs(Double.parseDouble(actualTerrorIPT)), Math.abs(values[1]), "Terrorism AJG IPT ");
			double terrorAJGDue=values[0] + values[1];
			compareValues(Double.parseDouble(actualerrorDue), Double.parseDouble(common.roundedOff(Double.toString(terrorAJGDue))), "Terrorism AJG Amount Due ");
			return  Double.parseDouble(common.roundedOff(Double.toString(terrorAJGDue)));
		}
		return 0;
		
	}catch(Throwable t) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
		k.reportErr("Failed in "+methodName+" function", t);
		Assert.fail("Failed in Calculate Terrorisam ammount.  \n", t);
		return 0;
	}

}

public double[] TerrorismCalculation(String split,String premiumAmt, String ipt){
	try{
		
		Map<Object,Object> data_map = null;
			
			switch (common.currentRunningFlow) {
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
				case "Requote":
					data_map = common.Requote_excel_data_map;
					break;
				case "CAN":
					data_map = common.NB_excel_data_map;
					break;
				}
		
		String codeCommission = (String)data_map.get("TER_CedeComm");
		String Commission = "1.00";
		
		if(codeCommission.equalsIgnoreCase("No")){
			Commission = "0.9";
		}else{
			Commission = "1.00";
		}
			
		double Premium = 0.00;
		double IPT = 0.00;		
		
		Premium =  Double.parseDouble(premiumAmt) * Double.parseDouble(Commission);
		IPT =  Double.parseDouble(ipt);		
		
		if(common.currentRunningFlow.contentEquals("CAN")){
			return new double[] {-Premium, -IPT};	
		}
		
		return new double[] {Premium, IPT};		
		
	}catch(Throwable t) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
		k.reportErr("Failed in "+methodName+" function", t);
		Assert.fail("Failed in Calculate Terrorisam ammount according to Split.  \n", t);
		return new double[] {0, 0};
	}
}

public void MTAFlow(String code,String event) throws ErrorInTestMethod{
	System.out.println("Test method of MTA For - "+code);
	TestUtil.reportStatus("Test Method of MTA For - "+code, "Pass", true);
	
	String testName = (String)common.MTA_excel_data_map.get("Automation Key");
	String navigationBy = CONFIG.getProperty("NavigationBy");
	try{
		if(!TestBase.businessEvent.equalsIgnoreCase("Renewal"))
		NewBusinessFlow(code,"NB");
		
		common.currentRunningFlow="MTA";
		common_HHAZ.CoversDetails_data_list.clear();
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen.");
		customAssert.assertTrue(funcCreateEndorsement(),"Issue while creating Endorsement. ");

		customAssert.assertTrue(common_CCF.funcPolicyDetails(common.MTA_excel_data_map,code,event), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(common_CCF.funcPreviousClaims(common.MTA_excel_data_map), "Previous claim function is having issue(S) .");
		String MTA_Method = (String)common.MTA_excel_data_map.get("MTA_TCDescription");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers screen.");
		customAssert.assertTrue(common.funcCovers(common.MTA_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified Perils"),"Issue while Navigating to Specified Perils  . ");
		customAssert.assertTrue(common.funcSpecifiedPerils(common.MTA_excel_data_map), "Select covers function is having issue(S) . ");
		
		if(((String)common.MTA_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
			customAssert.assertTrue(common_CCF.funcInsuredProperties(common.MTA_excel_data_map), "Insured Property function is having issue(S) . ");
		}
		
		if(((String)common.MTA_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
			customAssert.assertTrue(common_CCF.funcBusinessInterruption(common.MTA_excel_data_map), "Business Interruption function is having issue(S) . ");
		}
		
		if(((String)common.MTA_excel_data_map.get("CD_Liability")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
			customAssert.assertTrue(common_CCF.funcEmployersLiability(common.MTA_excel_data_map), "Employers Liability function is having issue(S) . ");
			
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
			customAssert.assertTrue(common_CCF.funcPublicLiability(common.MTA_excel_data_map), "Public Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
			customAssert.assertTrue(common_CCF.funcProductsLiability(common.MTA_excel_data_map), "Products Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
			customAssert.assertTrue(funcLiabilityInformation(common.MTA_excel_data_map), "Liability Information function is having issue(S) . ");
			}
		if(((String)common.MTA_excel_data_map.get("CD_SpecifiedAllRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
			customAssert.assertTrue(common_CCF.funcSpecifiedAllRisks(common.MTA_excel_data_map), "Specified All Risks function is having issue(S) . ");
			}
		
		if(((String)common.MTA_excel_data_map.get("CD_ContractorsAllRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
			customAssert.assertTrue(common_CCF.funcContractorsAllRisks(common.MTA_excel_data_map), "Contractors All Risks function is having issue(S) . ");
			}
		
		if(((String)common.MTA_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
			customAssert.assertTrue(common_CCF.funcComputersandElectronicRisks(common.MTA_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
			}
		
		if(((String)common.MTA_excel_data_map.get("CD_Money")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
			customAssert.assertTrue(common_CCF.funcMoney(common.MTA_excel_data_map), "Money function is having issue(S) . ");
			}
		if(((String)common.MTA_excel_data_map.get("CD_GoodsInTransit")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
			customAssert.assertTrue(common_CCF.funcGoodsInTransit(common.MTA_excel_data_map), "Goods In Transit function is having issue(S) . ");
			}
		

		if(((String)common.MTA_excel_data_map.get("CD_FidelityGuarantee")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
			customAssert.assertTrue(common_CCF.funcFidelityGuarantee(common.MTA_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
			}
		if(((String)common.MTA_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
			customAssert.assertTrue(common_CCF.funcTerrorism(common.MTA_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		

		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		Assert.assertTrue(common_PEN.funcPremiumSummary_MTA(common.MTA_excel_data_map,code,event));
		
		if(!TestBase.businessEvent.equalsIgnoreCase("Renewal")){
			Assert.assertTrue(common_PEN.funcStatusHandling(common.MTA_excel_data_map,code,"MTA"));
			customAssert.assertEquals(err_count,0,"Errors in premium calculations . ");
			customAssert.assertEquals(trans_error_val,0,"Errors in Transaction premium calculations . ");
			customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
			
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


public boolean funcLiabilityInformation(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Liability Information", ""),"Liability Information page navigations issue(S)");
		
		//Statement of Fact
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LI_SOF_Q1", (String)map_data.get("LI_SOF_Q1")), "Unable to Select Liability Information MF Q1 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LI_SOF_Q2", (String)map_data.get("LI_SOF_Q2")), "Unable to Select Liability Information MF Q2 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LI_SOF_Q3", (String)map_data.get("LI_SOF_Q3")), "Unable to Select Liability Information MF Q3 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LI_SOF_Q4", (String)map_data.get("LI_SOF_Q4")), "Unable to Select Liability Information MF Q4 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LI_SOF_Q5", (String)map_data.get("LI_SOF_Q5")), "Unable to Select Liability Information MF Q5 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LI_SOF_Q6", (String)map_data.get("LI_SOF_Q6")), "Unable to Select Liability Information MF Q6 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LI_SOF_Q7", (String)map_data.get("LI_SOF_Q7")), "Unable to Select Liability Information MF Q7 radio button .");
		//customAssert.assertTrue(k.SelectRadioBtn("CCF_LI_SOF_Q8", (String)map_data.get("LI_SOF_Q8")), "Unable to Select Liability Information MF Q8 radio button .");
				
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
		
		TestUtil.reportStatus("Liability Information details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
	}

public boolean funcDirectorsAndOfficers(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Directors and Officers", ""),"Directors and Officers page navigations issue(S)");
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }

		
		//customAssert.assertTrue(k.Click("CCF_DO_PriorPendingLitigationDate"), "Unable to enter Prior and Pending Litigation Date .");
		customAssert.assertTrue(k.Input("CTA_DO_PriorPendingLitigationDate", (String)map_data.get("DO_PriorPendingLitigationDate")),"Unable to Enter Prior and Pending Litigation Date .");
		//customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
		
		//Directors & Officers
		customAssert.assertTrue(k.Input("CTA_DO_Turnover_UK", Keys.chord(Keys.CONTROL, "a")),"Unable to select Turnover_UK ");
		customAssert.assertTrue(k.Input("CTA_DO_Turnover_UK", (String)map_data.get("DO_Turnover_UK")),"Unable to enter value in Turnover_UK  . ");
		customAssert.assertTrue(k.Input("CTA_DO_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select Excess ");
		customAssert.assertTrue(k.Input("CTA_DO_Excess", (String)map_data.get("DO_Excess")),"Unable to enter value in Excess  . ");
		
		//Corporate Liability
		customAssert.assertTrue(k.Input("CTA_DO_CorporateLiabilityTurnover_UK", Keys.chord(Keys.CONTROL, "a")),"Unable to select CorporateLiabilityTurnover_UK ");
		customAssert.assertTrue(k.Input("CTA_DO_CorporateLiabilityTurnover_UK", (String)map_data.get("DO_CorporateLiabilityTurnover_UK")),"Unable to enter value in CorporateLiabilityTurnover_UK  . ");
		customAssert.assertTrue(k.Input("CTA_DO_CorporateLiabilityExcess", Keys.chord(Keys.CONTROL, "a")),"Unable to select CorporateLiabilityExcess ");
		customAssert.assertTrue(k.Input("CTA_DO_CorporateLiabilityExcess", (String)map_data.get("DO_CorporateLiabilityExcess")),"Unable to enter value in CorporateLiabilityExcess  . ");
	
		//Employment Practices Liability
		customAssert.assertTrue(k.Input("CTA_DO_EPL_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select EPL_Excess ");
		customAssert.assertTrue(k.Input("CTA_DO_EPL_Excess", (String)map_data.get("DO_EPL_Excess")),"Unable to enter value in EPL_Excess  . ");
		
		//Statement of Fact
		customAssert.assertTrue(k.SelectRadioBtn("CTA_DO_SOF_Field_1", (String)map_data.get("DO_SOF_Field_1")),"Unable to Select SOF_Field_1 radio button . ");
		customAssert.assertTrue(k.SelectRadioBtn("CTA_DO_SOF_Field_2", (String)map_data.get("DO_SOF_Field_2")),"Unable to Select SOF_Field_2 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CTA_DO_SOF_Field_3", (String)map_data.get("DO_SOF_Field_3")),"Unable to Select SOF_Field_3 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CTA_DO_SOF_Field_4", (String)map_data.get("DO_SOF_Field_4")),"Unable to Select SOF_Field_4 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CTA_DO_SOF_Field_5", (String)map_data.get("DO_SOF_Field_5")),"Unable to Select SOF_Field_5 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CTA_DO_SOF_Field_6", (String)map_data.get("DO_SOF_Field_6")),"Unable to Select SOF_Field_6 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CTA_DO_SOF_Field_7", (String)map_data.get("DO_SOF_Field_7")),"Unable to Select SOF_Field_7 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CTA_DO_SOF_Field_8", (String)map_data.get("DO_SOF_Field_8")),"Unable to Select SOF_Field_8 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CTA_DO_SOF_Field_9", (String)map_data.get("DO_SOF_Field_9")),"Unable to Select SOF_Field_9 radio button . ");
		customAssert.assertTrue(k.SelectRadioBtn("CTA_DO_SOF_Field_10", (String)map_data.get("DO_SOF_Field_10")),"Unable to Select SOF_Field_10 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CTA_DO_SOF_Field_11", (String)map_data.get("DO_SOF_Field_11")),"Unable to Select SOF_Field_11 radio button . ");
		customAssert.assertTrue(k.SelectRadioBtn("CTA_DO_SOF_Field_12", (String)map_data.get("DO_SOF_Field_12")),"Unable to Select SOF_Field_12 radio button  . ");
				
		customAssert.assertTrue(k.Click("CTA_Btn_Save"), "Unable to click on Save Button on Directors and Officers .");
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "DO", "Directors and Officers", "");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_DirectorsandOfficers_NP", String.valueOf(sPremiumm), map_data);
		
		TestUtil.reportStatus("Directors and Officers details are filled sucessfully . ", "Info", true);
		
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
			if(((String)common.NB_excel_data_map.get("CD_Add_MaterialDamage")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(common_CCF.funcInsuredProperties(common.NB_excel_data_map), "Insured Property function is having issue(S) . ");
			}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_BusinessInterruption")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_BusinessInterruption")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
				customAssert.assertTrue(common_CCF.funcBusinessInterruption(common.NB_excel_data_map), "Business Interruption function is having issue(S) . ");
			}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_Liability")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Liability")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(common_CCF.funcEmployersLiability(common.NB_excel_data_map), "Employers Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
				customAssert.assertTrue(common_CCF.funcELDInformation(common.NB_excel_data_map), "ELD Information function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
				customAssert.assertTrue(common_CCF.funcPublicLiability(common.NB_excel_data_map), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(common_CCF.funcProductsLiability(common.NB_excel_data_map), "Products Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
				customAssert.assertTrue(funcLiabilityInformation(common.NB_excel_data_map), "Liability Information function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_SpecifiedAllRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_SpecifiedAllRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(common_CCF.funcSpecifiedAllRisks(common.NB_excel_data_map), "Specified All Risks function is having issue(S) . ");
				}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_ContractorsAllRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_ContractorsAllRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(common_CCF.funcContractorsAllRisks(common.NB_excel_data_map), "Contractors All Risks function is having issue(S) . ");
				}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_ComputersandElectronicRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
				customAssert.assertTrue(common_CCF.funcComputersandElectronicRisks(common.NB_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
				}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_Money")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Money")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
				customAssert.assertTrue(common_CCF.funcMoney(common.NB_excel_data_map), "Money function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_GoodsInTransit")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_GoodsInTransit")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
				customAssert.assertTrue(common_CCF.funcGoodsInTransit(common.NB_excel_data_map), "Goods In Transit function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_CyberandDataSecurity")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_CyberandDataSecurity")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
				customAssert.assertTrue(common_CCF.funcCyberAndDataSecurity(common.NB_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_DirectorsandOfficers")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_DirectorsandOfficers")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Directors and Officers"),"Issue while Navigating to Directors and Officers screen . ");
				customAssert.assertTrue(funcDirectorsAndOfficers(common.NB_excel_data_map), "Directors and Officers function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_FidelityGuarantee")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_FidelityGuarantee")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
				customAssert.assertTrue(common_CCF.funcFidelityGuarantee(common.NB_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
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


/**
 * 
 * This method handles all Cancellation Cases for SPI product.
 * 
 */
public void CancellationFlow(String code,String event) throws ErrorInTestMethod{
	
	String testName = (String)common.CAN_excel_data_map.get("Automation Key");
	try{
		
			NewBusinessFlow(code,"NB");
			
			common.currentRunningFlow="CAN";
			System.out.println("Test method of CAN For - "+code);
			
			customAssert.assertTrue(CancelPolicy(common.CAN_excel_data_map), "Unable to cancel policy");
			customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			
			customAssert.assertTrue(Cancel_PremiumSummary(common.NB_excel_data_map), "Issue in verifying Premium summary for cancellation");
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""), "Unable to click on policies tab");
			Assert.assertTrue(common_PEN.funcStatusHandling(common.NB_excel_data_map,code,TestBase.businessEvent));
					
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
									
										if(!headerName.contains("Com. Rate (%)")){
											WebElement sec_Val = driver.findElement(By.xpath(ReturnP_TablePath+"//tbody//tr["+row+"]//td["+col+"]"));
											sectionValue = sec_Val.getText();
											sectionValue = sectionValue.replaceAll(",", "");
											ReturnP_Table_TotalVal.put(headerName, Double.parseDouble(sectionValue));
											
										}else{
											continue;
										}
										common_CTB.CAN_CTB_ReturnP_Values_Map.put(sectionName, ReturnP_Table_TotalVal);
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
								
								common_CTB.CAN_CTB_ReturnP_Values_Map.put(sectionName, ReturnP_Table_CoverVal);
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
						Can_returnP_Error = Can_returnP_Error + Can_ReturnP_Total_Validation(sectionNames,common_CTB.CAN_CTB_ReturnP_Values_Map);
					else
						Can_returnP_Error = Can_returnP_Error + CanReturnPTable_CoverSection_Validation(policy_Duration,DaysRemain, s_Name, common_CTB.CAN_CTB_ReturnP_Values_Map);								
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
					exp_value = exp_value + common_CTB.CAN_CTB_ReturnP_Values_Map.get(section).get("Net Premium (GBP)");
			}
			
			String canRP_NetNetP_actual = Double.toString(common_CTB.CAN_CTB_ReturnP_Values_Map.get("Totals").get("Net Premium (GBP)"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_NetNetP_actual)," Net Net Premium");

			/*exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_CTB.CAN_CTB_ReturnP_Values_Map.get(section).get("Com. Rate (%)");
			}
			String canRP_pc_actual = Double.toString(common_CTB.CAN_CTB_ReturnP_Values_Map.get("Totals").get("Com. Rate (%)"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_pc_actual)," Com. Rate (%)");*/
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_CTB.CAN_CTB_ReturnP_Values_Map.get(section).get("Commission (GBP)");
			}
			String canRP_netP_actual = Double.toString(common_CTB.CAN_CTB_ReturnP_Values_Map.get("Totals").get("Commission (GBP)"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_netP_actual),"Commission (GBP)");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_CTB.CAN_CTB_ReturnP_Values_Map.get(section).get("Gross Premium (GBP)");
			}
			String canRP_bc_actual =  Double.toString(common_CTB.CAN_CTB_ReturnP_Values_Map.get("Totals").get("Gross Premium (GBP)"));
			
			double premium_diff = Double.parseDouble(common.roundedOff(String.valueOf(exp_value))) - Double.parseDouble(common.roundedOff(canRP_bc_actual));
			
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_bc_actual),"Gross Premium (GBP)");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_CTB.CAN_CTB_ReturnP_Values_Map.get(section).get("Gross IPT (GBP)");
			}
			String canRP_grossP_actual = Double.toString(common_CTB.CAN_CTB_ReturnP_Values_Map.get("Totals").get("Gross IPT (GBP)"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_grossP_actual),"Gross IPT (GBP)");
		
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_CTB.CAN_CTB_ReturnP_Values_Map.get(section).get("Net IPT (GBP)");
			}
			String canRP_InsuranceTax_actual = Double.toString(common_CTB.CAN_CTB_ReturnP_Values_Map.get("Totals").get("Net IPT (GBP)"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_InsuranceTax_actual),"Net IPT (GBP)");
	
			if(premium_diff<0.05 && premium_diff>-0.05){
				TestUtil.reportStatus("Total Premium [<b> "+exp_value+" </b>] matches with actual total premium [<b> "+canRP_bc_actual+" </b>]as expected for Totals in Cancellation Return Premium table .", "Pass", false);
				return 0;
				
			}else{
				TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+exp_value+"</b>] and Actual Premium [<b> "+canRP_bc_actual+"</b>] for Totals in Cancellation Return Premium table . </p>", "Fail", true);
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
			double annual_NetNetP = common_CTB.PremiumSummary_Values.get(code).get("PS_"+code+"_NP");
			String canRP_NetNetP_expected = Double.toString((annual_NetNetP/365)*DaysRemain);
			String canRP_NetNetP_actual = Double.toString(common_CTB.CAN_CTB_ReturnP_Values_Map.get(sectionNames).get("Net Premium (GBP)"));
			CommonFunction.compareValues(Double.parseDouble(canRP_NetNetP_expected),Double.parseDouble(canRP_NetNetP_actual)," Net Premium (GBP)");
			
			//COB CAN Transaction comm rate Calculation : 
			double canRP_comm_Rate = common_CTB.PremiumSummary_Values.get(code).get("PS_"+code+"_CR");
			String canRP_pc_expected = common.roundedOff(Double.toString(canRP_comm_Rate));
			String canRP_pc_actual = Double.toString(common_CTB.CAN_CTB_ReturnP_Values_Map.get(sectionNames).get("Com. Rate (%)"));
			CommonFunction.compareValues(Double.parseDouble(canRP_pc_expected),Double.parseDouble(canRP_pc_actual),"Com. Rate (%)");
			
		//Calculate gross commision
			
			double denominator = (1.00-(canRP_comm_Rate/100));
			double calcltdComm = (Double.parseDouble(canRP_NetNetP_expected)/denominator)*(canRP_comm_Rate/100);
			String canRP_bc_actual =  Double.toString(common_CTB.CAN_CTB_ReturnP_Values_Map.get(sectionNames).get("Commission (GBP)"));
			CommonFunction.compareValues(calcltdComm,Double.parseDouble(canRP_bc_actual),"Commission (GBP)");
			
			
			//COB CAN Transaction GrossPremium verification : 
			double canRP_grossP = calcltdComm + Double.parseDouble(canRP_NetNetP_expected);
			String canRP_grossP_actual = Double.toString(common_CTB.CAN_CTB_ReturnP_Values_Map.get(sectionNames).get("Gross Premium (GBP)"));
			CommonFunction.compareValues(canRP_grossP,Double.parseDouble(canRP_grossP_actual),sectionNames+" Gross Premium (GBP)");
			
			
			double canRP_InsuranceTax = (canRP_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_IPT")))/100.0;
			canRP_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(canRP_InsuranceTax)));
			String canRP_InsuranceTax_actual = Double.toString(common_CTB.CAN_CTB_ReturnP_Values_Map.get(sectionNames).get("Gross IPT (GBP)"));
			CommonFunction.compareValues(canRP_InsuranceTax,Double.parseDouble(canRP_InsuranceTax_actual),"Gross IPT (GBP)");
						
			//Net IPT
			
			double netIPT = annual_NetNetP* (Double.parseDouble((String)map_data.get("PS_"+code+"_IPT"))/100);
			String canRP_NetIPT_actual = Double.toString(common_CTB.CAN_CTB_ReturnP_Values_Map.get(sectionNames).get("Net IPT (GBP)"));
			CommonFunction.compareValues(netIPT,Double.parseDouble(canRP_NetIPT_actual),"Net IPT (GBP)");
			
			double premium_diff = canRP_grossP -  Double.parseDouble(canRP_grossP_actual);
			
			if(premium_diff<0.10 && premium_diff>-0.10){
				TestUtil.reportStatus("Total Premium [<b> "+canRP_grossP+" </b>] matches with actual total premium [<b> "+canRP_grossP_actual+" </b>]as expected for "+sectionNames+" in Cancellation Return Premium table .", "Pass", false);
				return 0;
				
			}else{
				TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+canRP_grossP+"</b>] and Actual Premium [<b> "+canRP_grossP_actual+"</b>] for "+sectionNames+" in Cancellation Return Premium table . </p>", "Fail", true);
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
	double s_commRate = 0.00, s_BrokerCommRate = 0.00, s_GrossCommRate = 0.00, s_InsTRate = 0.00;
	
	try{
	
		customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"), "Unable to navigate to Premium Summary screen");
		
		String Cancellation_Date = "//p[text()=' Cancellation Details ']//following-sibling::p";
		WebElement Cancel_Date = driver.findElement(By.xpath(Cancellation_Date));
		String C_Date = Cancel_Date.getText();
		
		// Verification of cancellation date :
		
				
		// Read Values From Cancellation Premium Summary Table :
		
			String CancelP_TablePath = "//p[text()=' Cancellation Details ']//following-sibling::p//following-sibling::table[@id='table0']";
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
					
					double s_GrossP = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+2+"]").replaceAll(",", "")));
					
					if(!sectionName.equals("Totals")){
						s_commRate = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+3+"]").replaceAll(",", "")));
					}
													
					double s_GrossCommAmt = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+4+"]").replaceAll(",", "")));
					
										
					double s_NetP = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+5+"]").replaceAll(",", "")));
					double s_GrossIpt = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+6+"]").replaceAll(",", "")));
					double s_NetIpt = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+7+"]").replaceAll(",", "")));
					
					common.compareValues(s_GrossP, common_CTB.CAN_CTB_ReturnP_Values_Map.get(sectionName).get("Gross Premium (GBP)"), "Cancellation Gross Premium (GBP)");
										
					if(!sectionName.equals("Totals")){
						common.compareValues(s_commRate, common_CTB.CAN_CTB_ReturnP_Values_Map.get(sectionName).get("Com. Rate (%)"), "Cancellation Gross comm rate");
					}
					
					common.compareValues(s_GrossCommAmt, common_CTB.CAN_CTB_ReturnP_Values_Map.get(sectionName).get("Commission (GBP)"), "Cancellation Commission (GBP)");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_NetP, common_CTB.CAN_CTB_ReturnP_Values_Map.get(sectionName).get("Net Premium (GBP)"), "Cancellation Net Premium (GBP)");
					}
					
					common.compareValues(s_GrossIpt, common_CTB.CAN_CTB_ReturnP_Values_Map.get(sectionName).get("Gross IPT (GBP)"), "Cancellation Gross IPT (GBP) Premium");
					common.compareValues(s_NetIpt, common_CTB.CAN_CTB_ReturnP_Values_Map.get(sectionName).get("Net IPT (GBP)"), "Cancellation Net IPT (GBP)");
										
				}	
			}
				
			return retVal;		
	
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}


public static boolean PremiumSummarytableCalculation(Hashtable<String,String> tabledata,String section ){
	//MaterialDamage_nipt, MaterialDamage_gprem, MaterialDamage_nprem, MaterialDamage_gipt, MaterialDamage_comm,MaterialDamage_comr
	try{ 	
	if(section.equals("Total")){ return true;}
		DecimalFormat f = new DecimalFormat("00.00");
		String secName = section.replaceAll(" ", "");
		String cSection = null;
		double grossPrem = Double.parseDouble(tabledata.get("PS_"+secName+"_GP"));
		double commisn   = Double.parseDouble(tabledata.get("PS_"+secName+"_GC")); 
		double commRate  = Double.parseDouble(tabledata.get("PS_"+secName+"_CR"));
		double netPrem 	 = Double.parseDouble(tabledata.get("PS_"+secName+"_NP"));
		double grossIPT  = Double.parseDouble(tabledata.get("PS_"+secName+"_GT"));
		double netIPT    = Double.parseDouble(tabledata.get("PS_"+secName+"_NPIPT"));
		
		
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
//    	customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page is not loaded to perfrm MTA . ");
//   
//		customAssert.assertTrue(common.funcButtonSelection("Create Endorsement"), "Unable to click on Create Endorsement button .");
//	
//    	int ammendmet_period = Integer.parseInt((String)data_map.get("MTA_EndorsementPeriod"));
//    	if(CommonFunction.businessEvent.equalsIgnoreCase("Renewal")){
//    		if(ammendmet_period > Integer.parseInt((String)common.Renewal_excel_data_map.get("PS_Duration"))){
//        		TestUtil.reportStatus("Amendement Period Should not be greater than Policy Duration", "Fail", true);
//        		return false;
//        	}
//    	}else{
//    		if(ammendmet_period > Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"))){
//        		TestUtil.reportStatus("Amendement Period Should not be greater than Policy Duration", "Fail", true);
//        		return false;
//        	}
//    	}
//    	
//    	TimeZone uk_timezone = TimeZone.getTimeZone("Europe/London");
//    	Calendar c = Calendar.getInstance(uk_timezone);
//    	if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
//      		int d = Integer.parseInt(((String)data_map.get("PS_PolicyStartDate")).substring(0, 2));
//      		dateobj = common.addDays(df.parse((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate")), ammendmet_period);
//      	}else{
//      		c.add(Calendar.DATE, ammendmet_period);
//      	  	dateobj = df.parse(df.format(c.getTime()));
//      	}
//    	
//    	/*SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//    	Calendar c = Calendar.getInstance();    
//    	c.add(Calendar.DATE, ammendmet_period);
//    	dateobj = df.parse(df.format(c.getTime()));*/
//    	
//	
//        customAssert.assertTrue(k.Click("SPI_Endorsement_eff_date"), "Unable to enter Endorsement effective Date.");
//        customAssert.assertTrue(k.Type("SPI_Endorsement_eff_date", df.format(dateobj)), "Unable to Enter Endorsement effective Date .");
//        customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
//        customAssert.assertTrue(k.Input("SPI_Reson_for_Endors", (String)data_map.get("MTA_Reason_for_Endorsement")),"Unable to Enter Reason for Endorsement");
//       // customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", testName, "MTA_EffectiveDate", k.getAttribute("SPI_Endorsement_eff_date", "value"),data_map),"Error while writing data to excel for field >MTA_EffectiveDate<");
//        if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
//	      	WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "MTA_Endorsement", (String)data_map.get("Automation Key"), "MTA_EffectiveDate", k.getAttribute("SPI_Endorsement_eff_date", "value"),data_map);
//	    }else{
//	      	WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", (String)data_map.get("Automation Key"), "MTA_EffectiveDate", k.getAttribute("SPI_Endorsement_eff_date", "value"),data_map);
//	    }
//        customAssert.assertTrue(common.funcButtonSelection("Create Endorsement"), "Unable to click on Create Endorsement button .");
//     //   customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "EndorsementCreation", testName, "MTA_QC_InceptionDate", df.format(dateobj),data_map),"Error while writing data to excel for field >MTA_QC_InceptionDate<");
//        //Writing to MTA Excel
//    	customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", testName, "MTA_PolicyNumber", k.getText("SPI_MTA_policy_number"),data_map),"Error while writing data to excel for field >MTA Policy Date<");
//    	if(common.currentRunningFlow.equals("MTA")){
//    		if(((String)data_map.get("MTA_Status")).equals("Endorsement Rewind"))
//    			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", testName, "MTA_isMTARewind", "Y",data_map),"Error while writing data to excel for field >MTA is Rewind<");
//    		else
//    			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", testName, "MTA_isMTARewind", "N",data_map),"Error while writing data to excel for field >MTA is Rewind<");
//    	}
//        
//    	//WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "MTA_Endorsement", (String)data_map.get("Automation Key"), "MTA_PolicyNumber", k.getText("SPI_MTA_policy_number"),data_map);
//    	TestUtil.reportStatus("Create Endorsement Details filled successfully . ", "Info", true);
//		
//        return retvalue;
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
        customAssert.assertTrue(k.Input("SPI_Reson_for_Endors", (String)data_map.get("MTA_Reason_for_Endorsement")),"Unable to Enter Reason for Endorsement");
        if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
	      	WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "MTA_Endorsement", (String)data_map.get("Automation Key"), "MTA_EffectiveDate", k.getAttribute("SPI_Endorsement_eff_date", "value"),data_map);
	    }else{
	      	WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", (String)data_map.get("Automation Key"), "MTA_EffectiveDate", k.getAttribute("SPI_Endorsement_eff_date", "value"),data_map);
	    }
       // customAssert.assertTrue(k.Input("POB_Reason_for_Endorsement", (String)data_map.get("MTA_ReasonforEndorsement")),"Unable to Enter Reason for Endorsement");
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
					c_locator = coverWithLocator.split("_")[1];
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
public boolean funcPremiumSummary_MTA(Map<Object, Object> map_data,String code,String event) {
	
	boolean r_value= true;
	Date currentDate = new Date();
	String testName = (String)map_data.get("Automation Key");
	String customPolicyDuration=null;
	SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
	
	try{
		customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
		//customAssert.assertTrue(common_HHAZ.verifyEndorsementONPremiumSummary(map_data),"Endorsement on Premium is having issue(S).");
		int policy_Duration = 0;
		String Policy_End_Date = "" , policy_Start_date="";
	
	switch(common.currentRunningFlow){
	
	case "MTA":
	case "Rewind":
		
		policy_Duration = Integer.parseInt((String)map_data.get("PS_Duration"));
		policy_Duration--;
		policy_Start_date = k.getTextByXpath("//*[contains(text(),'Policy Start Date')]//following::div[1]");
		//policy_Start_date = common_VELA.get_PolicyStartDate((String)map_data.get("PS_PolicyStartDate"));
		map_data.put("PS_PolicyStartDate", policy_Start_date);
		Policy_End_Date = common.daysIncrementWithOutFormation((String)map_data.get("PS_PolicyStartDate"), policy_Duration);
		
		map_data.put("PS_PolicyStartDate", policy_Start_date);
		 Policy_End_Date = common.daysIncrementWithOutFormation((String)map_data.get("PS_PolicyStartDate"), policy_Duration);
		
		if(((String)map_data.get("PS_DefaultStartEndDate")).equals("No") && !common.currentRunningFlow.equalsIgnoreCase("MTA")){
			customAssert.assertTrue(k.Click("Policy_Start_Date"), "Unable to Click Policy_Start_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_Start_Date", (String)map_data.get("PS_PolicyStartDate")),"Unable to Enter Policy_Start_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
			customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
		}	
		k.waitTwoSeconds();
		customAssert.assertTrue(k.SelectRadioBtn("Payment_Warranty_rules", (String)map_data.get("PS_PaymentWarrantyRules")),"Unable to Select Payment_Warranty_rules radio button . ");
		
		if(((String)map_data.get("PS_PaymentWarrantyRules")).equals("Yes") && !common.currentRunningFlow.equalsIgnoreCase("MTA")){
			
			customAssert.assertTrue(k.Click("Payment_Warranty_Due_Date"), "Unable to Click Payment_Warranty_Due_Date date picker .");
			customAssert.assertTrue(k.Input("Payment_Warranty_Due_Date", (String)map_data.get("PS_PaymentWarrantyDueDate")),"Unable to Enter Payment_Warranty_Due_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
		}
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
	break;	
	}
	k.waitTwoSeconds();
	customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
	customPolicyDuration = k.getText("Policy_Duration");
	customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,map_data),"Error while writing Policy Duration data to excel .");
	TestUtil.reportStatus("MTA Policy Duration = "+customPolicyDuration+" Days . ", "Info", true);
	 
	customAssert.assertTrue(Verify_premiumSummaryTable(), "Error while verifying Premium Summary table in MTA  .");
//	if(!common_CCD.isMTARewindStarted){
//		customAssert.assertTrue(common_CCD.func_Flat_Premiums_(common.MTA_excel_data_map,common.MTA_Structure_of_InnerPagesMaps), "Error while verifying Flat Premium table in MTA  .");
//	}
	customAssert.assertTrue(func_MTATransactionDetailsPremiumTable(code, event), "Error while verifying Transaction Details Premium table on premium Summary page .");
	Assert.assertTrue(funcTransactionDetailsMessage_MTA());

	
//	if(Integer.parseInt(customPolicyDuration)!=365){
//		customAssert.assertTrue(funcTransactionPremiumTable(code, event), "Error while verifying Transaction Premium table on premium Summary page .");
//	}
	
	TestUtil.reportStatus("Premium Summary details are filled and Verified sucessfully . ", "Info", true);
	return r_value;
}catch(Throwable t){
		
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
	

	if(CommonFunction.product.equalsIgnoreCase("POB")|| CommonFunction.product.equalsIgnoreCase("POC")){
		section_map.put("MaterialDamage","md8");
		section_map.put("LossOfRentalIncome","bi3");
		section_map.put("PropertyOwnersLiability","pl3");
		section_map.put("Terrorism","tr3");
	}else if(CommonFunction.product.equalsIgnoreCase("XOE")){
		section_map.put("PropertyExcessofLoss","xo1");
		section_map.put("Terrorism","tr2");
	}else{
		section_map.put("MaterialDamage","md7");
		section_map.put("BusinessInterruption","bi2");
		section_map.put("Terrorism","tr2");
	} 
	section_map.put("EmployersLiability","el3");
	section_map.put("PublicLiability","pl2");
	section_map.put("ProductsLiability","pr1");
	section_map.put("SpecifiedAllRisks","sar");
	section_map.put("ContractorsAllRisks","car");
	section_map.put("ComputersandElectronicRisks","it");
	section_map.put("Money","mn2");
	section_map.put("GoodsInTransit","gt2");
	section_map.put("MarineCargo","mar");
	section_map.put("CyberandDataSecurity","cyb");
	if(code.equalsIgnoreCase("CTA")||code.equalsIgnoreCase("CTB")){
		section_map.put("DirectorsandOfficers","do_pct");
	}else{
		section_map.put("DirectorsandOfficers","do2");
		}
	section_map.put("FrozenFoods","ff2");
	section_map.put("LossofLicence","ll2");
	section_map.put("FidelityGuarantee","fg");
	section_map.put("LegalExpenses","lg2");
	section_map.put("Total","tot");
	
	
	double exp_Premium = 0.0, exp_IPT = 0.00;
	
	try{
	
		String annualTble_xpath = "html/body/div[3]/form/div/table[2]";
		String policy_status_actual = k.getText("Policy_status_header");
		int trans_tble_Rows = driver.findElements(By.xpath(annualTble_xpath+"/tbody/tr")).size();
		int trans_tble_Cols = driver.findElements(By.xpath(annualTble_xpath+"/thead/tr/th")).size();
		String sectionName = null;
//		PremiumExcTerrDocExp = 0;
//		PremiumExcTerrDocExp = 0;
		if(common.currentRunningFlow.equalsIgnoreCase("NB") ||  common.currentRunningFlow.equalsIgnoreCase("Renewal")){
		
			if(!PremiumFlag)
			for(int i =1;i<=trans_tble_Rows-1;i++){
				String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
				sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
				if(sectionName.contains("Totals"))
					sectionName = "Total";
				if(sectionName.contains("BusinesssInterruption"))
					sectionName = "BusinessInterruption";
				
				String cName = null;
				
				if(sectionName.contains("LossofLicence")){
					cName = "LossOfLicence";
				}else{
					cName = sectionName;
				}
								
				if(isInsuranceTaxDone == false){
				customAssert.assertTrue(funcAddInput_PremiumSummary(cName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
				
				if(!TestBase.product.equalsIgnoreCase("CTB") && !TestBase.product.equalsIgnoreCase("CCI")){
					if(((String)data_map.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
						data_map.put("PS_"+cName+"_IPT", "0.0");
					}
				}else{
					if(!policy_status_actual.contains("Rewind")){
						data_map.put("PS_"+cName+"_IPT", data_map.get("PS_IPTRate"));
					}else{
						if(cName.contains("PersonalAccident")){
							cName="PersonalAccidentStandard";
						}
						if(((((String)data_map.get("CD_"+cName)).equals("No") && ((String)data_map.get("CD_"+cName)).equals("Yes")))){
							data_map.put("PS_"+cName+"_IPT", data_map.get("PS_IPTRate"));
						}
					}
				}
				}
			}
		
		}
		
		if(common.currentRunningFlow.equalsIgnoreCase("Rewind")){
			for(int i =1;i<=trans_tble_Rows-1;i++){
				String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
				sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
				if(sectionName.contains("Totals"))
					sectionName = "Total";
				if(sectionName.contains("BusinesssInterruption"))
					sectionName = "BusinessInterruption";
				if(sectionName.contains("LossofLicence"))
					sectionName = "LossOfLicence";
				
				customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
				String cover_name = section_map.get(sectionName);
				String PencCommXpath ;
				if(((String)data_map.get("PD_TaxExempt")).equalsIgnoreCase("Yes")){
					data_map.put("PS_"+sectionName+"_IPT", "0.0");
				}
				if(cover_name.contains("md")){
					
					PencCommXpath = "//*[@name='md7"+"_comr']";
					
				}else if(cover_name.contains("el")){
					PencCommXpath = "//*[@name='el3"+"_comr']";
			
				}else{
					 PencCommXpath = "//*[contains(@id,'"+cover_name+"_comr')]";
				}
				String penComm = driver.findElement(By.xpath(PencCommXpath)).getAttribute("value");
				data_map.put("PS_"+sectionName+"_CR", penComm);
				
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
				if(sectionName.contains("LossofLicence"))
					sectionName = "LossOfLicence";
			
				customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
				if(((String)data_map.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
					data_map.put("PS_"+sectionName+"_IPT", "0.0");
				}
			}
		
		}
			if(common.currentRunningFlow.equalsIgnoreCase("MTA")){
					
				//if(!PremiumFlag)
 				for(int i =1;i<=trans_tble_Rows-1;i++){
					String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
					sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
					
					switch(sectionName){
					
					
					case "BusinesssInterruption":
						//code = "BusinessInterruption";
						sectionName = "BusinessInterruption";
						break;
					
									
					}
					if(sectionName.contains("Totals"))
						sectionName = "Total";
					if(CommonFunction.businessEvent.equalsIgnoreCase("Renewal") && common.currentRunningFlow.equalsIgnoreCase("MTA")){
						if(((String)common.Renewal_excel_data_map.get("CD_"+sectionName)).equals("No") && 
								((String)common.MTA_excel_data_map.get("CD_"+sectionName)).equals("Yes")){
							
							if(isInsuranceTaxDone == false)
							customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
						}
						if(((String)data_map.get("PD_TaxExempt")).equalsIgnoreCase("Yes")){
							data_map.put("PS_"+sectionName+"_IPT", "0.0");
						}
						String cover_name = section_map.get(sectionName);
						String PencCommXpath ;
						if(cover_name.contains("md")){
							
							PencCommXpath = "//*[@name='md7"+"_comr']";
							
						}else if(cover_name.contains("el")){
							PencCommXpath = "//*[@name='el3"+"_comr']";
							
						}else{
							 PencCommXpath = "//*[contains(@id,'"+cover_name+"_comr')]";
						}
						String penComm = driver.findElement(By.xpath(PencCommXpath)).getAttribute("value");
						common.MTA_excel_data_map.put("PS_"+sectionName+"_CR", penComm);
						
					}else{
						
						if(((String)common.NB_excel_data_map.get("CD_"+sectionName)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_"+sectionName)).equals("Yes")){
							
							String cover_name = section_map.get(sectionName);
							String PencCommXpath ;
							customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
							if(((String)data_map.get("PD_TaxExempt")).equalsIgnoreCase("Yes")){
								data_map.put("PS_"+sectionName+"_IPT", "0.0");
							}
							if(cover_name.contains("md")){
								
								PencCommXpath = "//*[@name='md7"+"_comr']";
								
							}else if(cover_name.contains("el")){
								PencCommXpath = "//*[@name='el3"+"_comr']";
								
							}else{
								 PencCommXpath = "//*[contains(@id,'"+cover_name+"_comr')]";
								
							}
							
							String penComm = driver.findElement(By.xpath(PencCommXpath)).getAttribute("value");
							
							common.MTA_excel_data_map.put("PS_"+sectionName+"_CR", penComm);
						}else{
							
							
							String cover_name = section_map.get(sectionName);
							String PencCommXpath ;
							if(cover_name.contains("md")){
								
								PencCommXpath = "//*[@name='md7"+"_comr']";
								
							}else if(cover_name.contains("el")){
								PencCommXpath = "//*[@name='el3"+"_comr']";
								
							}else{
								 PencCommXpath = "//*[contains(@id,'"+cover_name+"_comr')]";
								
							}
							
							String penComm = driver.findElement(By.xpath(PencCommXpath)).getAttribute("value");
							
							common.MTA_excel_data_map.put("PS_"+sectionName+"_CR", penComm);
							
						}
					}
				}
			}
			
			PremiumFlag = true;	 
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
			
			if(common.currentRunningFlow.contains("Renewal")){
				common.funcButtonSelection("Insurance Tax");
				customAssert.assertTrue(common.funcPageNavigation("Tax Adjustment", ""),"Unable to land on Tax adjustment screen.");
				
				deleteItems();
				
				common.funcPageNavigation("", "Save");
				k.Click("Tax_adj_BackBtn");
			}
			
			for(int i =1;i<=trans_tble_Rows-1;i++){
				String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
				sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
				
				String cName = null;
				 if(sectionName.contains("Totals")){
					sectionName = "Total";}
				 if(sectionName.contains("BusinesssInterruption"))
						sectionName = "BusinessInterruption";
				 
				 if(sectionName.contains("Terrorism"))
					sectionName = "Terrorism";
				
					if(sectionName.contains("LossofLicence")){
						cName = "LossOfLicence";
					}else{
						cName = sectionName;
					}
					err_count = err_count + func_PremiumSummaryCalculation(section_map.get(sectionName),cName,locator_map);
				exp_Premium = exp_Premium + Double.parseDouble((String)data_map.get("PS_"+cName+"_GP"));
				exp_IPT = exp_IPT + Double.parseDouble((String)data_map.get("PS_"+cName+"_GT"));
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
				 
				 String cName = null;
				 
				 if(sectionName.contains("LossofLicence")){
						cName = "LossOfLicence";
					}else{
						cName = sectionName;
					}
				 
				Total_GP = Total_GP + Double.parseDouble((String)data_map.get("PS_"+cName+"_GP"));
				Total_GT = Total_GT + Double.parseDouble((String)data_map.get("PS_"+cName+"_GT"));
				Total_GC = Total_GC + Double.parseDouble((String)data_map.get("PS_"+cName+"_GC"));
				Total_NP = Total_NP + Double.parseDouble((String)data_map.get("PS_"+cName+"_NP"));
				Total_NPIPT = Total_NPIPT + Double.parseDouble((String)data_map.get("PS_"+cName+"_NPIPT"));
			}
			
			data_map.put("PS_Total_GT", f.format(Total_GT));
			data_map.put("PS_Total_GP", f.format(Total_GP));
			data_map.put("PS_Total_GC", f.format(Total_GC));
			data_map.put("PS_Total_NPIPT", f.format(Total_NPIPT));
			data_map.put("PS_Total_NP", f.format(Total_NP));
					
			String exp_Gross_Premium = common.roundedOff(Double.toString(exp_Premium));
			String exp_Gross_IPT = common.roundedOff(Double.toString(exp_IPT));
			
//			String act_Gross_Premium = driver.findElement(By.xpath("//*[text()='Annual Premium ']//preceding::*[@id='table0']/tbody/tr[1]/td[3]")).getText();
//			String act_Gross_IPT = driver.findElement(By.xpath("//*[text()='Annual Premium ']//preceding::*[@id='table0']/tbody/tr[2]/td[3]")).getText();
//			String act_Total_Premium = driver.findElement(By.xpath("//*[text()='Annual Premium ']//preceding::*[@id='table0']/tbody/tr[4]/td[3]")).getText();
//			act_Gross_Premium = act_Gross_Premium.replaceAll(",", "").replaceAll("£", "");
//			act_Gross_IPT = act_Gross_IPT.replaceAll(",", "").replaceAll("£", "");
//			act_Total_Premium = act_Total_Premium.replaceAll(",", "").replaceAll("£", "");
			
			String act_Gross_Premium = driver.findElement(By.xpath("//*[@id='tot_gprem']")).getAttribute("value").replaceAll(",", "");
			String act_net_Premium = driver.findElement(By.xpath("//*[@id='tot_nprem']")).getAttribute("value").replaceAll(",", "");
			String act_Gross_IPT = driver.findElement(By.xpath("//*[@id='tot_gipt']")).getAttribute("value").replaceAll(",", "");
			
			double exp_Total_Premium = Double.parseDouble(exp_Gross_Premium) + Double.parseDouble(exp_Gross_IPT);
				
			TestUtil.reportStatus("---------------Total Premium-----------------","Info",false);
			CommonFunction.compareValues(Double.parseDouble(exp_Gross_Premium),Double.parseDouble(act_Gross_Premium),"Gross Premium.");
			CommonFunction.compareValues(Double.parseDouble(exp_Gross_IPT),Double.parseDouble(act_Gross_IPT),"Gross IPT.");
			
			totalGrossPremium = Double.parseDouble(exp_Gross_Premium);
			totalGrossTax = Double.parseDouble(exp_Gross_IPT);
			
			return true; 
		   
		}catch(Throwable t){ 
					return false;
			
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
	//Reusable for both NB and MTA
	public int func_PremiumSummaryCalculation(String code,String covername,Map<String,String> premium_loc) {
				
		Map<Object,Object> map_data = null;
		Map<Object,Object> Tax_map_data = new HashMap<>();
		
		Map<String,Double> temp_PremiumValues = new HashMap<>();
		
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
					Tax_map_data = common.Rewind_excel_data_map;
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
			
			// Gross Premium verification : 
			double netP = Net_Premium;
			String netP_expected = common.roundedOff(Double.toString(netP));
			String netP_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+premium_loc.get("NP")+"')]", "value");
			CommonFunction.compareValues(Double.parseDouble(netP_expected),Double.parseDouble(netP_actual),"Net Premium");
			map_data.put("PS_"+covername+"_NP",netP_expected);
			temp_PremiumValues.put("PS_"+covername+"_NP",Double.parseDouble(netP_expected));
			
				
				TestUtil.reportStatus("---------------"+covername+"-----------------","Info",false);
				//SPI Pen commission Calculation : 
				
				// Net Premium verification : 
				
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
				
				temp_PremiumValues.put("PS_"+covername+"_GC",Double.parseDouble(grossC_expected));
				temp_PremiumValues.put("PS_"+covername+"_CR",Double.parseDouble((String)map_data.get("PS_"+covername+"_CR")));
				
				TestUtil.reportStatus("Commission :<b>"+(netP_expected)+"</b> matches with <b>"+(grossC_actual), "Info", false);
				
				//Gross Premium Verification:
				double grossP = Net_Premium + calcltdComm;
				String grossP_expected = common.roundedOff(Double.toString(grossP));
				String grossP_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+premium_loc.get("GP")+"')]", "value");
				CommonFunction.compareValues(Double.parseDouble(grossP_expected),Double.parseDouble(grossP_actual),"Gross Premium");
				map_data.put("PS_"+covername+"_GP",grossP_expected);
				temp_PremiumValues.put("PS_"+covername+"_GP",Double.parseDouble(grossP_expected));
				
				TestUtil.reportStatus("Gross Premium :<b>"+(grossP_expected)+"</b> matches with <b>"+(grossP_actual), "Info", false);
				
				//Gross IPT Verification:
				if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
					String InsuranceTax = k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+premium_loc.get("GT")+"')]", "value");
					double IPT = (Double.parseDouble(InsuranceTax) / grossP) * 100.0;
					TestUtil.WriteDataToXl(TestBase.product+"_"+event, "Premium Summary",(String)map_data.get("Automation Key"), "PS_"+covername+"_IPT", common_HHAZ.roundedOff(Double.toString(IPT)), map_data);
				}
				
				double calcltdGIPT = 0.00, calcltdNIPT = 0.00;
				if(grossP == 0.00){
					calcltdGIPT = 0.0;
					calcltdNIPT = 0.00;
				}else{
					calcltdGIPT = grossP *(Double.parseDouble((String)map_data.get("PS_"+covername+"_IPT"))/100);
					calcltdNIPT = netP *(Double.parseDouble((String)map_data.get("PS_"+covername+"_IPT"))/100);
				}
				
				String grossIPT_expected = common.roundedOff(Double.toString(calcltdGIPT));
				String grossIPT_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+premium_loc.get("GT")+"')]", "value");
				CommonFunction.compareValues(Double.parseDouble(grossIPT_expected),Double.parseDouble(grossIPT_actual),"Gross IPT");
				map_data.put("PS_"+covername+"_GT",grossIPT_expected);
				temp_PremiumValues.put("PS_"+covername+"_GT",Double.parseDouble(grossIPT_expected));
				
				TestUtil.reportStatus("Gross IPT :<b>"+(grossIPT_expected)+"</b> matches with <b>"+(grossIPT_actual), "Info", false);
				
				//Net IPT Verification
				
				String grossNIPT_expected = common.roundedOff(Double.toString(calcltdNIPT));
				String grossNIPT_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+premium_loc.get("NPIPT")+"')]", "value");
				CommonFunction.compareValues(Double.parseDouble(grossNIPT_expected),Double.parseDouble(grossNIPT_actual),"Net IPT");
				map_data.put("PS_"+covername+"_NPIPT",grossNIPT_expected);
				temp_PremiumValues.put("PS_"+covername+"_NPIPT",Double.parseDouble(grossNIPT_expected));
				
				TestUtil.reportStatus("Net IPT:<b>"+(grossNIPT_expected)+"</b> matches with <b>"+(grossNIPT_actual), "Info", false);
				
				if(common.currentRunningFlow.equals("MTA")){
					if(((String)map_data.get("PD_TaxExempt")).equalsIgnoreCase("Yes"))
						map_data.put("PS_"+covername+"_IPT", "0.0");
					temp_PremiumValues.put("PS_"+covername+"_IPT",Double.parseDouble("0.0"));
				}
				PS_Map = map_data;
				common_CTB.PremiumSummary_Values.put(covername, temp_PremiumValues);
			return 0;		
				
		}catch(Throwable t) { 
	        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
	        Assert.fail("Insured Properties function is having issue(S). \n", t);
	        return 0;
	 }
		
	}
	
	public boolean funcTransactionPremiumTable(String code, String event){
		//Transaction Premium Table
		
		
		Map<Object,Object> data_map = null;
		
			switch(common.currentRunningFlow){
				case "NB":
					data_map = common.NB_excel_data_map;
					break;
				case "Rewind":
					data_map = common.Rewind_excel_data_map;
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
							
								if(!headerName.contains("Com. Rate (%)") && !headerName.contains("Broker Comm %") && !headerName.contains("Gross Comm %")
										&& !headerName.contains("Insurance Tax Rate") ){
									WebElement sec_Val = driver.findElement(By.xpath(transactionTble_xpath+"//tbody//tr["+row+"]//td["+col+"]"));
									sectionValue = sec_Val.getText();
									sectionValue = sectionValue.replaceAll(",", "");
									transaction_Section_Vals_Total.put(headerName, Double.parseDouble(sectionValue));
									
								}else{
									continue;
								}
								common_CTB.transaction_Premium_Values.put(sectionName, transaction_Section_Vals_Total);
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
							common_CTB.transaction_Premium_Values.put(sectionName, transaction_Section_Vals);
						
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
							trans_error_val = trans_error_val + transactionPremiumTable_Verification_Total(sectionNames,common_CTB.transaction_Premium_Values);
						else
							trans_error_val = trans_error_val + transactionPremiumTable_Verification(policy_Duration,s_Name,common_CTB.transaction_Premium_Values);
						
				
					}
					
					 TestUtil.reportStatus("Transaction Premium table has been verified suceesfully . ", "info", true);
					
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
				exp_value = exp_value + common_CTB.transaction_Premium_Values.get(section).get("Net Premium (GBP)");
		}
		String t_NetNetP_actual = Double.toString(common_CTB.transaction_Premium_Values.get("Totals").get("Net Premium (GBP)"));
		CommonFunction.compareValues(Double.parseDouble(common.roundedOff(String.valueOf(exp_value))),Double.parseDouble(t_NetNetP_actual),"Net Premium (GBP)");

		exp_value = 0.0;
		for(String section : sectionNames){
			if(!section.contains("Total"))
				exp_value = exp_value + common_CTB.transaction_Premium_Values.get(section).get("Gross Premium (GBP)");
		}
		String t_grossP_actual = Double.toString(common_CTB.transaction_Premium_Values.get("Totals").get("Gross Premium (GBP)"));
		CommonFunction.compareValues(exp_value,Double.parseDouble(t_grossP_actual)," Gross Premium (GBP)");
			
		double premium_diff = Double.parseDouble(common.roundedOff(String.valueOf(exp_value))) - Double.parseDouble(t_grossP_actual);
		
		exp_value = 0.0;
		for(String section : sectionNames){
			if(!section.contains("Total"))
				exp_value = exp_value + common_CTB.transaction_Premium_Values.get(section).get("Commission (GBP)");
		}
		String t_bc_actual =  Double.toString(common_CTB.transaction_Premium_Values.get("Totals").get("Commission (GBP)"));
		CommonFunction.compareValues(exp_value,Double.parseDouble(t_bc_actual),"Commission (GBP)");
	
		exp_value = 0.0;
		for(String section : sectionNames){
			if(!section.contains("Total"))
				exp_value = exp_value + common_CTB.transaction_Premium_Values.get(section).get("Gross IPT (GBP)");
		}
		String t_GrossIPT_actual =  Double.toString(common_CTB.transaction_Premium_Values.get("Totals").get("Gross IPT (GBP)"));
		CommonFunction.compareValues(exp_value,Double.parseDouble(t_GrossIPT_actual),"Gross IPT (GBP)");
	
		exp_value = 0.0;
		for(String section : sectionNames){
			if(!section.contains("Total"))
				exp_value = exp_value + common_CTB.transaction_Premium_Values.get(section).get("Net IPT (GBP)");
		}
		String t_NetIPT_actual = Double.toString(common_CTB.transaction_Premium_Values.get("Totals").get("Net IPT (GBP)"));
		CommonFunction.compareValues(exp_value,Double.parseDouble(t_NetIPT_actual),"Net IPT (GBP)");
				
		if(premium_diff<0.09 && premium_diff>-0.09){
			TestUtil.reportStatus("Total Premium [<b> "+exp_value+" </b>] matches with actual total premium [<b> "+t_grossP_actual+" </b>]as expected for Totals in Transaction Premium table .", "Pass", false);
			return 0;
			
		}else{
			TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+exp_value+"</b>] and Actual Premium [<b> "+t_grossP_actual+"</b>] for Totals in Transaction Premium table . </p>", "Fail", true);
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
			
			double annual_NetNetP = Double.parseDouble((String)map_data.get("PS_"+code+"_NP"));
			String t_NetNetP_expected = common.roundedOff(Double.toString((annual_NetNetP/365)*policy_Duration));
			String t_NetNetP_actual = Double.toString(common_CTB.transaction_Premium_Values.get(sectionNames).get("Net Premium (GBP)"));
			customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(t_NetNetP_expected),Double.parseDouble(t_NetNetP_actual)," Net Premium (GBP)"),"Mismatched Net Premium (GBP)");
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent,"Premium Summary",testName,"PS_"+code+"_PenComm",pc_expected,common.NB_excel_data_map),"Error while writing Pen Commission for cover "+code+" to excel .");
			
			// Gross premium Calculation
			double denominator = 1- (Double.parseDouble((String)map_data.get("PS_"+code+"_CR"))/100);
			double t_grossP = Double.parseDouble(t_NetNetP_expected) / denominator;
			String t_grossP_actual = Double.toString(common_CTB.transaction_Premium_Values.get(sectionNames).get("Gross Premium (GBP)"));
			customAssert.assertTrue(CommonFunction.compareValues(t_grossP,Double.parseDouble(t_grossP_actual),sectionNames+" Transaction Gross Premium"),"Mismatched Gross Premium Values");
							
			//Commission GBP calculation
			
			double t_comm = t_grossP* (Double.parseDouble((String)map_data.get("PS_"+code+"_CR"))/100);
			String t_Actual_Comm = Double.toString(common_CTB.transaction_Premium_Values.get(sectionNames).get("Commission (GBP)"));
			customAssert.assertTrue(CommonFunction.compareValues(t_comm,Double.parseDouble(t_Actual_Comm),"Commission (GBP)"),"Mismatched Commission (GBP) Values");
			
			
			//Gross IPT Calculation
			String iptRate = (String)map_data.get("PS_"+code+"_IPT"); 
			
			double t_InsuranceTax = (t_grossP * Double.parseDouble(iptRate))/100.0;
			t_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(t_InsuranceTax)));
			String t_InsuranceTax_actual = Double.toString(common_CTB.transaction_Premium_Values.get(sectionNames).get("Gross IPT (GBP)"));
			customAssert.assertTrue(CommonFunction.compareValues(t_InsuranceTax,Double.parseDouble(t_InsuranceTax_actual),"Gross IPT (GBP)"),"Mismatched Gross IPT (GBP) Values");
			
			// Net IPT calculation :
			
			double t_NetIPT =  Double.parseDouble(t_NetNetP_expected)  * (Double.parseDouble(iptRate)/100);
			t_NetIPT = Double.parseDouble(common.roundedOff(Double.toString(t_NetIPT)));
			String t_NetIPT_actual = Double.toString(common_CTB.transaction_Premium_Values.get(sectionNames).get("Net IPT (GBP)"));
			customAssert.assertTrue(CommonFunction.compareValues(t_NetIPT,Double.parseDouble(t_NetIPT_actual),"Net IPT (GBP)"),"Mismatched Net IPT (GBP) Values");
			
			//  Transaction Total Premium verification : 
			double t_Premium = t_grossP + t_InsuranceTax;
			String t_p_expected = common.roundedOff(Double.toString(t_Premium));
			
			if(!sectionNames.contains("Terrorism") && !sectionNames.contains("PersonalAccidentOptional")){
				InsTaxDocAct = InsTaxDocAct + Double.parseDouble(t_InsuranceTax_actual);
				PremiumExcTerrDocAct = PremiumExcTerrDocAct + Double.parseDouble(t_grossP_actual);
				
				}
			if(!sectionNames.contains("Terrorism")){
				
			}else{
				AdditionalTerPDocAct = Double.parseDouble(t_grossP_actual);
		}
	
			
			String t_p_actual = Double.toString(Double.parseDouble(t_grossP_actual) + Double.parseDouble(t_InsuranceTax_actual));
			
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
	
	public String UpdatedCommissionRate(String commRate){
		
		try{
			String[] afterdecimal = commRate.split("\\.");
			if(commRate.contains(".") == false){return commRate;}
			DecimalFormat df = new DecimalFormat("#.######");
			if(afterdecimal[1].length()>=6){
				double updatedCommRate = Double.parseDouble(commRate);
				return df.format(updatedCommRate);
				
			}
			else {
				return commRate;
			}
			
		
		}catch(Throwable t){
			return commRate;
		}
//		return true;
		
			
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
					}
				else{
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
					
					TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_to_update.get("Automation Key"), "PS_"+sectionName+"_GT", common.roundedOff("00.00"), map_to_update);
					TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_to_update.get("Automation Key"), "PS_"+sectionName+"_IPT", common.roundedOff("00.00"), map_to_update);
					//TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_to_update.get("Automation Key"), "PS_"+sectionName+"_NPIPT", common.roundedOff("00.00"), map_to_update);
					totalGrossPremium = totalGrossPremium + Double.parseDouble(common.roundedOff(String.valueOf(common_CTB.PremiumSummary_Values.get(sectionName.replaceAll(" ", "")).get("PS_"+sectionName.replaceAll(" ", "")+"_GP"))));
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
					
				//customAssert.assertTrue(common.compareValues(totalGrossPremium, Double.parseDouble(actualTotalGP), "Total Gross Premium from Insuracnce Tax screen"), "Unable to compare total gross premium on Tax adjustment screen.");
				//customAssert.assertTrue(common.compareValues(totalGrossTax, Double.parseDouble(actualTotalGT), "Total Gross Tax from Insuracnce Tax screen"), "Unable to compare total gross tax on Tax adjustment screen.");
					
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
				if(coverName_withoutSpace.contains("Liability")){
					key = "CD_Liability";
				}
				
				if(TestBase.product.contains("CCI") && coverName_withoutSpace.contains("FrozenFoods")){
					key = "CD_FrozenFood";
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
				if(coverName_withoutSpace.contains("Liability")){
					key = "CD_Liability";
				}
				if(TestBase.product.contains("CCI") && coverName_withoutSpace.contains("FrozenFoods")){
					key = "CD_FrozenFood";
				}
				if(TestBase.product.contains("CCI") && coverName_withoutSpace.contains("LossOfLicence")){
					key = "CD_LossofLicence";
				}
								
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
				
				if(coverName.contains("Frozen Food")){
					coverName_withoutSpace = "FrozenFood";
				}else if(coverName.contains("Licence")){
					coverName_withoutSpace = "LossofLicence";
				}else if(coverName.contains("Excess of Loss")){
					coverName_withoutSpace = "PropertyExcessofLoss";
				}else{
					coverName_withoutSpace = coverName.replaceAll(" ", "");
				}
				
				if(coverName_withoutSpace.contains("Liability")){
					coverName_withoutSpace = "Liability";
					key = "CD_"+coverName_withoutSpace;
				}
								
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
			/*for(int p=0;p<common_HHAZ.CoversDetails_data_list.size();p++){
				coverName_datasheet = common_HHAZ.CoversDetails_data_list.get(p);
				
				if(coversNameList.contains(coverName_datasheet)){
					continue;
				}else{
					TestUtil.reportStatus("Cover Name <b>  ["+coverName_datasheet+"]  </b> is selected as 'NO' in datasheet but still listed in the dropdown list.", "FAIL", false);
					count_datasheet++;
				}
			}*/
			
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
										
					if(sectionName.contains("Liability")){
						coverName_withoutSpace = "Liability";
					}else if(sectionName.contains("Frozen Food")){
						coverName_withoutSpace = "FrozenFood";
					}else if(sectionName.contains("Licence")){
						coverName_withoutSpace = "LossofLicence";
					}else if(sectionName.contains("Excess of Loss")){
						coverName_withoutSpace = "PropertyExcessofLoss";
					}else{
						coverName_withoutSpace = sectionName.replaceAll(" ", "");
					}
					
					String key = "CD_"+coverName_withoutSpace;
					

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
						
						key = "CD_"+coverName_withoutSpace;
						if(sectionName.contains("Personal Accident Standard")){
							coverName_withoutSpace = "PersonalAccident";
						}
						
						
						sectionName = sectionName.replaceAll(" ", "");
						expectedIPTRate = (String)common.Rewind_excel_data_map.get("PS_"+sectionName+"_IPT");
						
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
						
						expectedIPTRate = (String)common.Requote_excel_data_map.get("PS_"+coverName_withoutSpace+"_IPT");
						
					}
					
					if(common.currentRunningFlow.equalsIgnoreCase("Renewal")){
						if(sectionName.contains("Personal Accident Standard")){
							coverName_withoutSpace = "PersonalAccidentStandard";
						}
						
						key = "CD_"+coverName_withoutSpace;
						if(sectionName.contains("Personal Accident Standard")){
							coverName_withoutSpace = "PersonalAccident";
						}
						if(sectionName.contains("Employers Liability")){
							coverName_withoutSpace = "EmployersLiability";
						}
						if(sectionName.contains("Public Liability")){
							coverName_withoutSpace = "PublicLiability";
						}
						if(sectionName.contains("Products Liability")){
							coverName_withoutSpace = "ProductsLiability";
						}
						if(sectionName.contains("Frozen Foods")){
							coverName_withoutSpace = "FrozenFoods";
						}
						if(sectionName.contains("Loss Of Licence")){
							coverName_withoutSpace = "LossOfLicence";
						}
						
						expectedIPTRate = (String)common.Renewal_excel_data_map.get("PS_"+coverName_withoutSpace+"_IPT");
						
					}
					if(coverName_withoutSpace.equalsIgnoreCase("PropertyOwnersLiability") || coverName_withoutSpace.equalsIgnoreCase("EmployersLiability")){
						key = "CD_Liability";
					}
					if(map_to_update.get(key).toString().equalsIgnoreCase("Yes")){
						
						
						String actualGrossPremium =  common.roundedOff(taxTable_tBody.findElement(By.xpath("tr["+(i+1)+"]/td[2]")).getText());
						String actualIPTRate =  taxTable_tBody.findElement(By.xpath("tr["+(i+1)+"]/td[4]")).getText();
						String actualGrossTax =  common.roundedOff(taxTable_tBody.findElement(By.xpath("tr["+(i+1)+"]/td[5]")).getText());
						
						String expectedGrossPremium = common.roundedOff(String.valueOf(PS_Map.get("PS_"+sectionName.replaceAll(" ", "")+"_GP")));
						String expectedGrossTax = common.roundedOff(Double.toString(((Double.parseDouble(expectedGrossPremium) * Double.parseDouble(expectedIPTRate)) / 100.0)));
						
						if(common.compareValues(Double.parseDouble(actualGrossPremium), Double.parseDouble(expectedGrossPremium), "Gross Premium for section - "+sectionName) && 
						   /*verification(actualIPTRate, expectedIPTRate, sectionName, "IPT Rate") &&*/
								common.compareValues(Double.parseDouble(actualGrossTax), Double.parseDouble(expectedGrossTax),  "Gross Taxfor section - "+sectionName)){
							
						}
						continue;
					}else{
						
						if(common.NB_excel_data_map.get("CD_"+coverName_withoutSpace).toString().equalsIgnoreCase("Yes")){
							
							
							String actualGrossPremium =  common.roundedOff(taxTable_tBody.findElement(By.xpath("tr["+(i+1)+"]/td[2]")).getText());
							String actualIPTRate =  taxTable_tBody.findElement(By.xpath("tr["+(i+1)+"]/td[4]")).getText();
							String actualGrossTax =  common.roundedOff(taxTable_tBody.findElement(By.xpath("tr["+(i+1)+"]/td[5]")).getText());
							
							String expectedGrossPremium = common.roundedOff((String)PS_Map.get("PS_"+coverName_withoutSpace.replaceAll(" ", "")+"_GP"));
							String expectedGrossTax = common.roundedOff(Double.toString(((Double.parseDouble(expectedGrossPremium) * Double.parseDouble(expectedIPTRate)) / 100.0)));
							
							if(common.compareValues(Double.parseDouble(actualGrossPremium), Double.parseDouble(expectedGrossPremium), "Gross Premium for section - "+sectionName) && 
							   /*verification(actualIPTRate, expectedIPTRate, sectionName, "IPT Rate") &&*/
									common.compareValues(Double.parseDouble(actualGrossTax), Double.parseDouble(expectedGrossTax), "Gross Tax for section - "+sectionName)){
								
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
		
		if(description.equalsIgnoreCase("Gross Tax")|| description.equalsIgnoreCase("Gross Premium")||description.equalsIgnoreCase("Un Adjusted Premium")){
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
	
	public int funcTransactionDetailsTable_Verification_MTA(String sectionName,Map<String,Map<String,Double>> transactionDetails_Premium_Values){

		Map<Object,Object> map_data = common.MTA_excel_data_map;
		
		Map<Object, Object> data_map = null;
		Map<Object,Object> Tax_map_data = new HashMap<>();
		
		double NB_NNP = 0.0;
		double MTA_NNP=0.0;
		double trans_NetNetP = 0.0,previous_mta=0.0,annualize_mta=0.0,_annualize_mta=0.0,final_trans_NNP=0.0;
		String code=null,cover_code=null;
		int p_NB_Duration = 0,p_MTA_Remaining_Duration=0;
		Map<String,Double> trans_details_values = new HashMap<>();
		
		switch (TestBase.businessEvent) {
		case "Renewal":
			data_map = common.Renewal_excel_data_map;
			break;
		case "MTA":
			data_map = common.NB_excel_data_map;
			Tax_map_data = common.NB_excel_data_map;
			break;
		default:
			break;
		}
		
		
		if(Integer.parseInt((String)data_map.get("PS_Duration"))!=365)
			p_NB_Duration = 365;
		else
			p_NB_Duration = Integer.parseInt((String)data_map.get("PS_Duration"));
		
		
		
		p_MTA_Remaining_Duration = Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"));
			
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
						
						if(((String)common.NB_excel_data_map.get("CD_"+cover_code)).equals("Yes") && ((String)common.MTA_excel_data_map.get("CD_Add_"+cover_code)).equals("No"))
						{
							NB_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_"+code+"_NetNetPremium"));
							MTA_NNP = 0.0;
								
						}else if(((String)common.NB_excel_data_map.get("CD_"+cover_code)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_Add_"+cover_code)).equals("Yes")){
							NB_NNP = 0.0;
							MTA_NNP = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NetNetPremium"));
						}else{
							NB_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_"+code+"_NetNetPremium"));
							MTA_NNP = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NetNetPremium"));
						}
						//Previous Premium MTA Calculation
						
						final_trans_NNP = ((MTA_NNP - NB_NNP)/p_NB_Duration)*
								((Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
							
				
				}else{
					
					//if(sectionName.contains("PI")){
						if(((String)common.NB_excel_data_map.get("CD_"+cover_code)).equals("Yes") && (((String)common.MTA_excel_data_map.get("CD_Add_"+cover_code)).equals("No")))
						{
							NB_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_"+code+"_NetNetPremium"));
							MTA_NNP = 0.0;
								
						}else if(((String)common.NB_excel_data_map.get("CD_"+cover_code)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_Add_"+cover_code)).equals("Yes")){
							NB_NNP = 0.0;
							MTA_NNP = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NetNetPremium"));
						}else{
							NB_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_"+code+"_NetNetPremium"));
							MTA_NNP = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NetNetPremium"));
						}
						
						final_trans_NNP = ((MTA_NNP - NB_NNP)/p_NB_Duration)*
								((Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
						
				
				}
			}else{
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
							
							trans_NetNetP = ((NB_NNP)/p_NB_Duration)*p_MTA_Remaining_Duration;
							previous_mta = NB_NNP - trans_NetNetP;
							annualize_mta = ((Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NetNetPremium")) * (Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) ))/(Integer.parseInt((String)data_map.get("PS_Duration"))));
							_annualize_mta = (annualize_mta/Double.parseDouble((String)common.MTA_excel_data_map.get("PS_Duration"))) * ((Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) - p_MTA_Remaining_Duration));
							final_trans_NNP = _annualize_mta - previous_mta;
							
					
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
					
				
				
				double t_InsuranceTax = (t_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_IPT")))/100.0;
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
				
				String Nb_Status = (String)map_data.get("MTA_Status");
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
			
							
				double t_InsuranceTax = (t_grossP * Double.parseDouble((String)Tax_map_data.get("PS_"+code+"_IPT")))/100.0;
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
			
			
			/*String t_NetNetP_expected = common.roundedOff(Double.toString(final_trans_NNP));
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
			
			
			double t_InsuranceTax = (t_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_IPT")))/100.0;
			t_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(t_InsuranceTax)));
			String t_InsuranceTax_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Insurance Tax"));
			CommonFunction.compareValues(t_InsuranceTax,Double.parseDouble(t_InsuranceTax_actual),"Insurance Tax");
			trans_details_values.put("Insurance Tax", t_InsuranceTax);
			
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
			}*/
				
	}catch(Throwable t) {
	    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	    TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
	    Assert.fail("Transaction Premium verification issue.  \n", t);
	    return 1;
	}

		
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
			code = "GoodsInTransit";
			cover_code = "GoodsInTransit";
			flat_section = sectionName;
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
		
		customAssert.assertEquals(t_Act_Message, t_Exp_Message,"Mismatch in Transaction Details table Message: Expected: "+t_Exp_Message+" and Actual: "+t_Act_Message+" . ");
		
		TestUtil.reportStatus(t_Exp_Message, "Pass", false);
		
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
				
				if(common_CCD.isMTARewindFlow)
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
							headerName = header_Name.getText().replaceAll(" (GBP)", "");
						
							if(!headerName.contains("Com. Rate (%)")){
								WebElement sec_Val = driver.findElement(By.xpath(transactionDetailsTble_xpath+"//tbody//tr["+row+"]//td["+col+"]"));
								sectionValue = sec_Val.getText();
								sectionValue = sectionValue.replaceAll(",", "");
								transaction_Section_Vals_Total.put(headerName, Double.parseDouble(sectionValue));
								
							}else{
								continue;
							}
							if(common_CCD.isMTARewindFlow){
								//common.transaction_Details_Premium_Values.clear();
								common.transaction_Details_Premium_Values.remove(sectionName);
							}
							common.transaction_Details_Premium_Values.put(sectionName, transaction_Section_Vals_Total);
					}
					}else if(!FP_Entry && !sectionName.contains("Flat")){
						
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
						
						if(common_CCD.isMTARewindFlow){
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
				
				if(common_CCD.isMTARewindFlow){
					TestUtil.reportStatus("---------------Transaction Details table Verification after Rewind Endorsement(MTA)-----------------","Info",false);
				}else{
					TestUtil.reportStatus("---------------Transaction Details table Verification in MTA-----------------","Info",false);
				}
				//Transaction table Verification
				
				// Check if Flat premium is added or not :
				
//				String flatPremium = (String)common.MTA_excel_data_map.get("FP_isFlatPremium");
//				String flatPremiumEntries = null; 
//									
//				if(flatPremium.contains("Yes")){
//					flatPremiumEntries = (String)common.MTA_excel_data_map.get("FP_FlatPremium_Entries");
//				}			
//				
//				String arrF_Premium[] = flatPremiumEntries.split(";");
//				
//				for(int i = 0; i < arrF_Premium.length; i ++){
//					
//					if(i == 0){
//						FP_Covers = (String)common.MTA_Structure_of_InnerPagesMaps.get("Flat-Premiums").get(i).get("FP_Section");
//					}else{
//						FP_Covers = FP_Covers + ","+ (String)common.MTA_Structure_of_InnerPagesMaps.get("Flat-Premiums").get(i).get("FP_Section");
//					}					
//				}
				
				for(int row = 1; row < trans_tble_Rows ;row ++){
					WebElement sec_Name = driver.findElement(By.xpath(transactionDetailsTble_xpath+"//tbody//tr["+row+"]//td["+1+"]"));
					sectionNames.add(sec_Name.getText());
				}
//				for(String s_Name : sectionNames){
//					
//					isFP_Text = "No";
					//FP Entries
//					if(s_Name.contains("Flat")){
//						common_POF.isFPEntries = true;isFP_Text="Yes";
//						
//						if(common_POF.isMTARewindFlow){
//							isMTARewindFPEntries=true;}
//					}
					
//					if(common_POF.isFPEntries && isFP_Text.equalsIgnoreCase("No") && !s_Name.equals("Totals")){
//						
//						trans_error_val = trans_error_val + func_FP_Entries_Transaction_Details_Verification_MTA(s_Name,common.MTA_Structure_of_InnerPagesMaps);
//						
//						
//					}else{
//						if(s_Name.equals("Totals"))
//							trans_error_val = trans_error_val + funcTransactionDetailsTable_Verification_Total_MTA(sectionNames,common.transaction_Details_Premium_Values);
//						else if(!s_Name.contains("Flat"))
//							trans_error_val = trans_error_val + funcTransactionDetailsTable_Verification_MTA(s_Name,common.transaction_Details_Premium_Values);
//						
//					}			
//				}
//				if(common_CCD.isMTARewindFlow){
//					
////					if(flatPremium.equalsIgnoreCase("Yes") && arrF_Premium.length > 0){
////						if(!isMTARewindFPEntries){
////							TestUtil.reportStatus("<p style='color:red'> Flat Premium Entries added in MTA Flow are not present while doing MTA Rewind in Transaction Details table . </p>", "Fail", true);
////							ErrorUtil.addVerificationFailure(new Throwable("Flat Premium Entries added in MTA Flow are not present while doing MTA Rewind in Transaction Details table . "));
////						}
////					}
//					
//					TestUtil.reportStatus("Transaction Details table has been verified suceesfully after Rewind Endorsement . ", "info", true);
//				}else{
//					TestUtil.reportStatus("Transaction Details table has been verified suceesfully . ", "info", true);
//				}
//				
//			}
			
		
				//Total Premium With Admin Fees 
				double total_premium_with_admin_fee = common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium (GBP)") + 
						common.transaction_Details_Premium_Values.get("Totals").get("Gross IPT (GBP)") ;
				
				
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
			
			
		}
		}	catch(Throwable t ){
			return false;
		}
		
		return true;
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
					exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Net Net Premium");
				}catch(Throwable t){
					continue;
				}
				}
				//}
			//break outer;
			}
			
			
			
			
			/*if(Start_Fp)
			{
			for(String _section : sectionNames){
				if(common_POF.isFPEntries && !_section.contains("Flat")){
					try{
						if(_section.equalsIgnoreCase("Property Owners Liabilities"))
							_section = "Liabilities - POL";
					exp_value = exp_value + common.transaction_Details_Premium_Values.get(_section+"_FP").get("Net Net Premium");
				}catch(Throwable t){
					continue;
				}
				}
				}
			break outer;
			}*/
			
		}
		
		String t_NetNetP_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Net Net Premium"));
		CommonFunction.compareValues(exp_value,Double.parseDouble(t_NetNetP_actual)," Net Net Premium");
		trans_details_values.put("Net Net Premium",exp_value);

		exp_value = 0.0;Start_Fp = false;
		for(String section : sectionNames){
			/*if(!section.contains("Total") && !section.contains("Flat"))
				exp_value = exp_value + transaction_Premium_Values.get(section).get("Pen Comm");
			if(common_POF.isFPEntries && !section.contains("Flat")){
				try{
					if(section.equalsIgnoreCase("Property Owners Liabilities"))
						section = "Liabilities - POL";
				exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Pen Comm");
			}catch(Throwable t){
				continue;
			}
			}*/
			
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
	/*		if(!section.contains("Total") && !section.contains("Flat"))
				exp_value = exp_value + transaction_Premium_Values.get(section).get("Net Premium");
			if(common_POF.isFPEntries && !section.contains("Flat")){
				try{
					if(section.equalsIgnoreCase("Property Owners Liabilities"))
						section = "Liabilities - POL";
				exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Net Premium");
			}catch(Throwable t){
				continue;
			}
			}*/
			
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
			/*if(!section.contains("Total") && !section.contains("Flat"))
				exp_value = exp_value + transaction_Premium_Values.get(section).get("Broker Commission");
			if(common_POF.isFPEntries && !section.contains("Flat")){
				try{
					if(section.equalsIgnoreCase("Property Owners Liabilities"))
						section = "Liabilities - POL";
				exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Broker Commission");
			}catch(Throwable t){
				continue;
			}
			}*/
			
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
			/*if(!section.contains("Total") && !section.contains("Flat"))
				exp_value = exp_value + transaction_Premium_Values.get(section).get("Gross Premium");
			if(common_POF.isFPEntries && !section.contains("Flat")){
				try{
					if(section.equalsIgnoreCase("Property Owners Liabilities"))
						section = "Liabilities - POL";
				exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Gross Premium");
			}catch(Throwable t){
				continue;
			}
			}*/
			
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
			/*if(!section.contains("Total") && !section.contains("Flat"))
				exp_value = exp_value + transaction_Premium_Values.get(section).get("Insurance Tax");
			if(common_POF.isFPEntries && !section.contains("Flat")){
				try{
					if(section.equalsIgnoreCase("Property Owners Liabilities"))
						section = "Liabilities - POL";
				exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Insurance Tax");
			}catch(Throwable t){
				continue;
			}
			}*/
			
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
			/*if(!section.contains("Total") && !section.contains("Flat"))
				exp_value = exp_value + transaction_Premium_Values.get(section).get("Total Premium");
			if(common_POF.isFPEntries && !section.contains("Flat")){
				try{
					if(section.equalsIgnoreCase("Property Owners Liabilities"))
						section = "Liabilities - POL";
				exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Total Premium");
			}catch(Throwable t){
				continue;
			}
			}*/
			
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

public boolean funcPolicyDetails(Map<Object, Object> map_data){
	String event1=null;
	
	if(common.currentRunningFlow.equals("NB")){
		event1=common.currentRunningFlow;
	}else if(common.currentRunningFlow.equals("MTA")) {
		event1=common.currentRunningFlow;
	}else if(common.currentRunningFlow.equals("Renewal")) {
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
		if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_1QS", (String)map_data.get("PD_1QS")), "Unable to Select 1QS radio button on Policy Details Page.");
		}
		customAssert.assertTrue(k.Input("CCF_PD_DateEstablishment", (String)map_data.get("PD_DateEstablishment")),	"Unable to enter value in Date Establishment  field .");
		
		if(!common.currentRunningFlow.equals("MTA") && !common.currentRunningFlow.equals("Renewal")){
			
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
			
			k.waitTwoSeconds();
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_HoldingBroker", (String)map_data.get("PD_HoldingBroker")), "Unable to Select Holding Broker radio button on Policy Details Page.");
			if(map_data.get("PD_HoldingBroker").equals("No")){
				//customAssert.assertTrue(k.Input("CCF_PD_HoldingBrokerInfo", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
				customAssert.assertTrue(k.Input("CCF_PD_HoldingBrokerInfo", (String) map_data.get("PD_HoldingBrokerInfo")),"Unable to enter value in HoldingBrokerInfo field. ");
			}
		}
		
		if(!TestBase.businessEvent.equalsIgnoreCase("Renewal") && !common.currentRunningFlow.equals("MTA")&& !common.currentRunningFlow.equals("Rewind")){
			customAssert.assertTrue(k.Input("CCF_PD_PreviousPremium", (String) map_data.get("PD_PreviousPremium")),"Unable to enter value in Previous Premium field. ");
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CarrierOverride", (String)map_data.get("PD_CarrierOverride")), "Unable to Select Carrier Override radio button on Policy Details Page.");
			if(map_data.get("PD_CarrierOverride").equals("Yes")){
				customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CO_RefferedToHead", (String)map_data.get("PD_CO_RefferedToHead")), "Unable to Select Reffered To Head radio button on Policy Details Page.");
			}
			k.waitTwoSeconds();
		}
		
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_TaxExempt", (String)map_data.get("PD_TaxExempt")), "Unable to Select TaxExempt radio button on Policy Details Page.");
		if(TestBase.product.equalsIgnoreCase("CTB")){
				customAssert.assertTrue(k.SelectRadioBtn("POE_businessEP", (String)map_data.get("PD_businessEP")), "Unable to Select Is this Business a Micro Business Enterprise ? on Policy Details Page.");
		}
		
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

public boolean funcPremiumSummary(Map<Object, Object> map_data,String code,String event) {
	
	boolean r_value= true;
	Date currentDate = new Date();
	String testName = (String)map_data.get("Automation Key");
	String customPolicyDuration=null;
	SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
	
	try{
		customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
		
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
					k.waitFiveSeconds();
					customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
					customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
					customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
					
				}	
				k.waitTwoSeconds();
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
					k.waitFiveSeconds();
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
					k.waitFiveSeconds();
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
				if(((String)map_data.get("PS_DefaultStartEndDate")).equals("No") && !common.currentRunningFlow.equalsIgnoreCase("MTA")){
					customAssert.assertTrue(k.Click("Policy_Start_Date"), "Unable to Click Policy_Start_Date date picker .");
					customAssert.assertTrue(k.Input("Policy_Start_Date", (String)map_data.get("PS_PolicyStartDate")),"Unable to Enter Policy_Start_Date .");
					customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
					k.waitFiveSeconds();
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
	TestUtil.reportStatus("Policy Duration = "+customPolicyDuration+" Days . ", "Info", true);
	customAssert.assertTrue(Verify_premiumSummaryTable(), "Error while verifying Premium Summary table .");
	customAssert.assertTrue(insuranceTaxAdjustmentHandling(code,event),"Insurance tax adjustment is having issue(S).");
	customAssert.assertTrue(Verify_premiumSummaryTable(), "Error while verifying Premium Summary table .");
	if(Integer.parseInt(customPolicyDuration)!=365){
		common_CTB.isTransTable = true;
		customAssert.assertTrue(funcTransactionPremiumTable(code, event), "Error while verifying Transaction Premium table on premium Summary page .");
	}
	
	TestUtil.reportStatus("Premium Summary details are filled and Verified sucessfully . ", "Info", true);
	return r_value;
}catch(Throwable t){		
		return false;
	}
}

//CTA Rewind 

	public void RewindFlow(String code,String event) throws ErrorInTestMethod{
		String testName = (String)common.Rewind_excel_data_map.get("Automation Key");
		try{
			common.currentRunningFlow="NB";
			if(!common.businessEvent.equalsIgnoreCase("Renewal") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
				NewBusinessFlow(code,"NB");
			}
			common.currentRunningFlow="Rewind";
			//common_CTA.CoversDetails_data_list.clear();
			String navigationBy = CONFIG.getProperty("NavigationBy");
			common_CTA.PremiumFlag = false;
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcRewind());
			
			TestUtil.reportStatus("<b> -----------------------Rewind flow is started---------------------- </b>", "Info", false);
			
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
			//customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			if(TestBase.businessEvent.equalsIgnoreCase("MTA") ){
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status Endorsement Submitted (Rewind) function is having issue(S) . ");
				customAssert.assertTrue(funcPolicyDetails(common.NB_excel_data_map,code,event), "Policy Details function having issue .");
			}//Renewal Submitted (Rewind)
			else if(TestBase.businessEvent.equalsIgnoreCase("Renewal") ){
				customAssert.assertTrue(common.funcSearchPolicy(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Renewal_excel_data_map,code,event,"Renewal Submitted (Rewind)"), "Verify Policy Status Renewal Submitted (Rewind) function is having issue(S) . ");
				customAssert.assertTrue(funcPolicyDetails(common.Rewind_excel_data_map,code,event), "Policy Details function having issue .");
			}else{
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");	
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
				customAssert.assertTrue(funcPolicyDetails(common.NB_excel_data_map,code,event), "Policy Details function having issue .");
			}
			
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"),"Issue while Navigating to Previous Claims  . ");
			customAssert.assertTrue(common_CCF.funcPreviousClaims(common.Rewind_excel_data_map), "Previous claim function is having issue(S) .");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(funcCovers(common.Rewind_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified Perils"),"Issue while Navigating to Specified Perils  . ");
			customAssert.assertTrue(common.funcSpecifiedPerils(common.Rewind_excel_data_map), "Select covers function is having issue(S) . ");
			
			if(((String)common.Rewind_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(common_CCF.funcInsuredProperties(common.Rewind_excel_data_map), "Insured Property function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
				customAssert.assertTrue(common_CCF.funcBusinessInterruption(common.Rewind_excel_data_map), "Business Interruption function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_Liability")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(common_CCF.funcEmployersLiability(common.Rewind_excel_data_map), "Employers Liability function is having issue(S) . ");
				
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
				customAssert.assertTrue(common_CCF.funcPublicLiability(common.Rewind_excel_data_map), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(common_CCF.funcProductsLiability(common.Rewind_excel_data_map), "Products Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
				customAssert.assertTrue(funcLiabilityInformation(common.Rewind_excel_data_map), "Liability Information function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_SpecifiedAllRisks")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(common_CCF.funcSpecifiedAllRisks(common.Rewind_excel_data_map), "Specified All Risks function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_ContractorsAllRisks")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(common_CCF.funcContractorsAllRisks(common.Rewind_excel_data_map), "Contractors All Risks function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
				customAssert.assertTrue(common_CCF.funcComputersandElectronicRisks(common.Rewind_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_Money")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
				customAssert.assertTrue(common_CCF.funcMoney(common.Rewind_excel_data_map), "Money function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_GoodsInTransit")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
				customAssert.assertTrue(common_CCF.funcGoodsInTransit(common.Rewind_excel_data_map), "Goods In Transit function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_FidelityGuarantee")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
				customAssert.assertTrue(common_CCF.funcFidelityGuarantee(common.Rewind_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(common_CCF.funcTerrorism(common.Rewind_excel_data_map), "Terrorism function is having issue(S) . ");
			}
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
				customAssert.assertTrue(funcPremiumSummary_MTA(common.Rewind_excel_data_map,code,"MTA"), "Error while verifying Premium Summary table .");
			}else{
				customAssert.assertTrue(funcPremiumSummary(common.Rewind_excel_data_map,TestBase.product,TestBase.businessEvent), "Premium Summary function is having issue(S) . ");
				if(TestBase.businessEvent.equalsIgnoreCase("Rewind"))
				Assert.assertTrue(common_PEN.funcStatusHandling(common.Rewind_excel_data_map,code,event));
			}
			
			
		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
		
	}
	
	public void RenewalRewindFlow(String code,String event) throws ErrorInTestMethod{
		String testName = (String)common.Rewind_excel_data_map.get("Automation Key");
		try{
			//common.currentRunningFlow="NB";
			if(!common.currentRunningFlow.equalsIgnoreCase("Renewal") &&!common.businessEvent.equals("Renewal") ){
				NewBusinessFlow(code,"NB");
			}
			common.currentRunningFlow="Rewind";
			//common_CTA.CoversDetails_data_list.clear();
			String navigationBy = CONFIG.getProperty("NavigationBy");
			common_CTA.PremiumFlag = false;
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcRewind());
			
			TestUtil.reportStatus("<b> -----------------------Rewind flow is started---------------------- </b>", "Info", false);
			
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
			customAssert.assertTrue(common.funcSearchPolicy(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Renewal Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
			
			customAssert.assertTrue(funcPolicyDetails(common.Renewal_excel_data_map,code,event), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"),"Issue while Navigating to Previous Claims  . ");
			customAssert.assertTrue(common_CCF.funcPreviousClaims(common.Rewind_excel_data_map), "Previous claim function is having issue(S) .");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(funcCovers(common.Rewind_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified Perils"),"Issue while Navigating to Specified Perils  . ");
			customAssert.assertTrue(common.funcSpecifiedPerils(common.Rewind_excel_data_map), "Select covers function is having issue(S) . ");
			
			if(((String)common.Rewind_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(common_CCF.funcInsuredProperties(common.Rewind_excel_data_map), "Insured Property function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
				customAssert.assertTrue(common_CCF.funcBusinessInterruption(common.Rewind_excel_data_map), "Business Interruption function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_Liability")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(common_CCF.funcEmployersLiability(common.Rewind_excel_data_map), "Employers Liability function is having issue(S) . ");
				
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
				customAssert.assertTrue(common_CCF.funcPublicLiability(common.Rewind_excel_data_map), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(common_CCF.funcProductsLiability(common.Rewind_excel_data_map), "Products Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
				customAssert.assertTrue(funcLiabilityInformation(common.Rewind_excel_data_map), "Liability Information function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_SpecifiedAllRisks")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(common_CCF.funcSpecifiedAllRisks(common.Rewind_excel_data_map), "Specified All Risks function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_ContractorsAllRisks")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(common_CCF.funcContractorsAllRisks(common.Rewind_excel_data_map), "Contractors All Risks function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
				customAssert.assertTrue(common_CCF.funcComputersandElectronicRisks(common.Rewind_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_Money")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
				customAssert.assertTrue(common_CCF.funcMoney(common.Rewind_excel_data_map), "Money function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_GoodsInTransit")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
				customAssert.assertTrue(common_CCF.funcGoodsInTransit(common.Rewind_excel_data_map), "Goods In Transit function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_FidelityGuarantee")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
				customAssert.assertTrue(common_CCF.funcFidelityGuarantee(common.Rewind_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(common_CCF.funcTerrorism(common.Rewind_excel_data_map), "Terrorism function is having issue(S) . ");
			}
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(funcPremiumSummary(common.Rewind_excel_data_map,TestBase.product,TestBase.businessEvent), "Premium Summary function is having issue(S) . ");
			Assert.assertTrue(common_PEN.funcStatusHandling(common.Rewind_excel_data_map,code,event));
			
			
			
		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
		
	}
public boolean decideRewindMethod(){
	 
		
		try {
			
			
			switch (CommonFunction.product) {
			case "CCF":
				if(common.currentRunningFlow.equals("NB")){
					customAssert.assertTrue(common_CCF.funcRewindOperation(),"Error in function Rewind Operation .");
				}else if(common.currentRunningFlow.equals("MTA")){
					customAssert.assertTrue(common_CCF.funcRewindOperationMTA(),"Error in function Rewind Operation .");
				}
				
				break;
			case "CCG":
				customAssert.assertTrue(common_CCF.funcRewindOperation(),"Error in function Rewind Operation .");
				break;
			case "CTA":
				customAssert.assertTrue(common_CTA.funcRewindOperation(),"Error in function Rewind Operation .");
				break;
			case "CTB":
				customAssert.assertTrue(common_CTB.funcRewindOperation(),"Error in function Rewind Operation .");
				break;
			case "POB":
				customAssert.assertTrue(common_POB.funcRewindOperation(),"Error in function Rewind Operation .");
				break;
			case "POC":
				customAssert.assertTrue(common_POB.funcRewindOperation(),"Error in function Rewind Operation .");
				break;
			case "XOE":
				customAssert.assertTrue(common_XOE.funcRewindOperation(),"Error in function Rewind Operation .");
				break;
			case "SPI":
				if(common.currentRunningFlow.equals("NB")){
					customAssert.assertTrue(common_SPI.funcRewindOperation(),"Error in function Rewind Operation .");
				}else if(common.currentRunningFlow.equals("MTA")){
					customAssert.assertTrue(common_SPI.funcMTARewindOperation_New(),"Error in function MTA Rewind Operation .");
				}
				else if(common.currentRunningFlow.equals("Renewal")){
					customAssert.assertTrue(common_SPI.funcRenewalRewindOperation(),"Error in function Renewal Rewind Operation .");
				}
				break;
			case "PIA":
				if(common.currentRunningFlow.equals("NB")){
					customAssert.assertTrue(common_PIA.funcRewindOperation(),"Error in function Rewind Operation .");
				}else if(common.currentRunningFlow.equals("MTA")){
					customAssert.assertTrue(common_PIA.funcMTARewindOperation_New(),"Error in function MTA Rewind Operation .");
				}
				else if(common.currentRunningFlow.equals("Renewal")){
					customAssert.assertTrue(common_PIA.funcRenewalRewindOperation(),"Error in function Renewal Rewind Operation .");
				}
				break;

			}
			
			
			
		} catch (Exception e) {
			return false;
		}
		
		return true;
		
	}

	public static boolean compareValues(double ExpectedValue, double ActualValue, String val) throws Exception{
		String eMsg="";
		boolean iret =true;
		try{
		
		 DecimalFormat f = new DecimalFormat("00.00");
		 String df1 = f.format(ExpectedValue);
		 String df2 = f.format(ActualValue);
		 double diffrence = Math.abs(Double.parseDouble(df1) - Double.parseDouble(df2));
		 diffrence = Double.parseDouble(common.roundedOff(Double.toString(diffrence)));
		 if(df1.equalsIgnoreCase(df2)){
			 String tMsg="Values have been matched for <b>"+val+"</b> Expected:<b>"+df1+" </b> with Actual value :<b>"+df2;
			 TestUtil.reportStatus(tMsg, "Pass", false);
		 }
		 else if(diffrence<=0.05 && diffrence>=-0.05){
			 if(diffrence==0.0){
				 String tMsg="Values have been matched for <b> "+val+"</b> Expected:<b> "+df1.replaceAll("-", "")+" </b> with Actual value :<b> "+df2.replaceAll("-", "")+" </p>";
				 TestUtil.reportStatus(tMsg, "Pass", false);
			 }else{
			 String tMsg="<p style='color:blue'>Values have been matched for <b>"+val+"</b> Expected:<b>"+df1+" </b> with Actual value :<b> "+df2+" </p>";
			 TestUtil.reportStatus(tMsg, "Pass", false);}	 
		 }
		 else{  
			 eMsg="Values have not been matched for <b>"+val+"</b> Expected:<b>"+df1+"</b> with Actual value :<b>"+df2; 
			 TestUtil.reportStatus(eMsg, "Fail", true);
			 throw new Exception(eMsg);
		 }
		 }catch(Throwable t){
			iret = false;
			ErrorUtil.addVerificationFailure(t);
		 }
		finally{
			return iret;
		}	 
	 }
//End of CF_CTA.java
}
