package com.selenium.commonfiles.base;

import static com.selenium.commonfiles.util.TestUtil.WriteDataToXl;

import java.awt.AWTException;
import java.text.DecimalFormat;
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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.selenium.commonfiles.util.ErrorInTestMethod;
import com.selenium.commonfiles.util.ErrorUtil;
import com.selenium.commonfiles.util.TestUtil;

public class CommonFunction_CCI extends TestBase{
	DecimalFormat decim = new DecimalFormat("#.00");
	SimpleDateFormat df = new SimpleDateFormat();
	public int actual_no_of_years=0,err_count=0,trans_error_val=0,Can_returnP_Error = 0;
	boolean isPreVerification=false,isMTARewindStarted=false,isNBRewindStarted=false;
	public Map<String,Double> transactionDetails_table_values = new HashMap<>();
	public static DecimalFormat f = new DecimalFormat("00.00");
	
	public static WebElement taxTable_tBody;
	public static WebElement objTable;
	public static WebElement taxTable_tHead;
	public static int countOfCovers,countOfTableRows;
	public static int errorVal=0,counter = 0;
	
	double totalGrossTax = 0.0,totalGrossTaxMTA = 0.0,totalGrossPremium = 0.0,totalGrossPremiumMTA=0.0,totalNetPremiumTax=0.0,totalNetPremiumTaxMTA=0.0;
	
	public double TotalPremiumWithAdminDocAct = 0.00, TotalPremiumWithAdminDocExp = 0.00, PremiumExcTerrDocAct = 0.00,  PremiumExcTerrDocExp = 0.00, TerPremDocAct = 0.00, TerPremDocExp = 0.00, InsTaxDocAct = 0.00, InsTaxDocExp = 0.00;
	public double AdditionalPWithAdminDocAct = 0.00, AdditionalExcTerrDocAct = 0.00,  AdditionalTerPDocAct = 0.00, AdditionalInsTaxDocAct = 0.00;
	public double InsTaxTerrDoc = 0.00, tpTotal = 0.00, AddTaxTerrDoc = 0.00;
	
	public boolean PremiumFlag = false;
	public boolean isInsuranceTaxDone = false,isTransTable = false;
	
	public Map<String,Map<String,Double>> CAN_CCI_ReturnP_Values_Map = new HashMap<>();
	public Map<String,Map<String,Double>> PremiumSummary_Values = new HashMap<>();
	
	public Map<String, List<Map<String, String>>> NB_Structure_of_InnerPagesMaps = null;
	public Map<String, List<Map<String, String>>> MTA_Structure_of_InnerPagesMaps = null;
	public Map<String, List<Map<String, String>>> Rewind_Structure_of_InnerPagesMaps = null;
	public Map<String, List<Map<String, String>>> Renewal_Structure_of_InnerPagesMaps = null;
	public Map<String, List<Map<String, String>>> CAN_Structure_of_InnerPagesMaps = null;
	public static Map<Object,Object> PS_Map = null;
	public static Map<Object,Double> PS_IPT = null;
	
	
	public Map<String,Map<String,Double>> transaction_Premium_Values = new HashMap<>();
	public Map<String,Map<String,Double>> transaction_Details_Premium_Values_EndorsemntRenewal = new HashMap<>();
	
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
	
	public static Map<String , String> AdjustedTaxDetails = new LinkedHashMap<String, String>();
	public static Map<String , String> AdjustedTaxCollection = new LinkedHashMap<String, String>();
	
	public static double adjustedPremium = 0.0,adjustedTotalPremium=0.0,adjustedTotalPremiumMTA=0.0,adjustedTotalTax=0.0,adjustedTotalTaxMTA=0.0,unAdjustedTotalTax=0.0,unAdjustedTotalTaxMTA=0.0;
	public static double PD_TotalRate = 0.0, PD_AdjustedRate = 0.0, PD_MD_Premium=0.0, PD_BI_Premium=0.0, PD_MD_TotalPremium = 0.00, PD_BI_TotalPremium = 0.00, finalMDPremium = 0.00, finalBIPremium= 0.00;
	
public void NewBusinessFlow(String code,String event) throws ErrorInTestMethod{
	String testName = (String)common.NB_excel_data_map.get("Automation Key");
	try{
		
		customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
		customAssert.assertTrue(common.checkClient(common.NB_excel_data_map,code,event),"");
		customAssert.assertTrue(common.createNewQuote(common.NB_excel_data_map,code,event), "Unable to create new quote.");
		customAssert.assertTrue(common.selectLatestQuote(common.NB_excel_data_map,code,event), "Unable to select quote from table.");
		customAssert.assertTrue(funcPolicyDetails(common.NB_excel_data_map,code,event), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(funcPreviousClaims(common.NB_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(common.funcCovers(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified Perils"),"Issue while Navigating to Specified Perils  . ");
		customAssert.assertTrue(common.funcSpecifiedPerils(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
		
		if(((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
			customAssert.assertTrue(funcInsuredProperties(common.NB_excel_data_map), "Insured Property function is having issue(S) . ");
		}
		
		if(((String)common.NB_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
			customAssert.assertTrue(funcBusinessInterruption(common.NB_excel_data_map), "Business Interruption function is having issue(S) . ");
		}
		
		if(((String)common.NB_excel_data_map.get("CD_Liability")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
			customAssert.assertTrue(funcEmployersLiability(common.NB_excel_data_map), "Employers Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
			customAssert.assertTrue(funcPublicLiability(common.NB_excel_data_map), "Public Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
			customAssert.assertTrue(funcProductsLiability(common.NB_excel_data_map), "Products Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
			customAssert.assertTrue(funcLiabilityInformation(common.NB_excel_data_map), "Liability Information function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_SpecifiedAllRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
			customAssert.assertTrue(funcSpecifiedAllRisks(common.NB_excel_data_map), "Specified All Risks function is having issue(S) . ");
			}
		
		/*if(((String)common.NB_excel_data_map.get("CD_ContractorsAllRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
			customAssert.assertTrue(funcContractorsAllRisks(common.NB_excel_data_map), "Contractors All Risks function is having issue(S) . ");
			}*/
		
		if(((String)common.NB_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
			customAssert.assertTrue(funcComputersandElectronicRisks(common.NB_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
			}
		
		if(((String)common.NB_excel_data_map.get("CD_Money")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
			customAssert.assertTrue(funcMoney(common.NB_excel_data_map), "Money function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_GoodsInTransit")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
			customAssert.assertTrue(funcGoodsInTransit(common.NB_excel_data_map), "Goods In Transit function is having issue(S) . ");
			}
		/*if(((String)common.NB_excel_data_map.get("CD_MarineCargo")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Marine Cargo"),"Issue while Navigating to Marine Cargo screen . ");
			customAssert.assertTrue(funcMarineCargo(common.NB_excel_data_map), "Marine Cargo function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_CyberandDataSecurity")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
			customAssert.assertTrue(funcCyberAndDataSecurity(common.NB_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_DirectorsandOfficers")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Directors and Officers"),"Issue while Navigating to Directors and Officers screen . ");
			customAssert.assertTrue(funcDirectorsAndOfficers(common.NB_excel_data_map), "Directors and Officers function is having issue(S) . ");
			}*/
		if(((String)common.NB_excel_data_map.get("CD_FrozenFood")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Frozen Foods"),"Issue while Navigating to Frozen Foods screen . ");
			customAssert.assertTrue(funcFrozenFoods(common.NB_excel_data_map), "Frozen Foods function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_LossofLicence")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Loss of Licence"),"Issue while Navigating to Loss of Licence screen . ");
		customAssert.assertTrue(funcLossofLicence(common.NB_excel_data_map), "Loss of Licence function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_FidelityGuarantee")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
			customAssert.assertTrue(funcFidelityGuarantee(common.NB_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
			customAssert.assertTrue(funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		
		/*if(((String)common.NB_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
			customAssert.assertTrue(funcLegalExpenses(common.NB_excel_data_map,code,event), "Legal Expenses function is having issue(S) . ");
			}*/
		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcPremiumSummary(common.NB_excel_data_map,code,event,"NB"), "Premium Summary function is having issue(S) . ");
			Assert.assertTrue(common.funcStatusHandling(common.NB_excel_data_map,code,event));
			if(TestBase.businessEvent.equals("NB")){
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
		if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
			NewBusinessFlow(code,"NB");
		}
		common.currentRunningFlow="MTA";
		customAssert.assertTrue(common_CCI.createEndorsement(common.MTA_excel_data_map),"Issue while Navigating to Terrorism screen . ");
		
		customAssert.assertTrue(funcPolicyDetails_MTA(common.MTA_excel_data_map,code,"MTA"), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(funcPreviousClaims(common.MTA_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(common.funcCovers(common.MTA_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified Perils"),"Issue while Navigating to Specified Perils  . ");
		customAssert.assertTrue(common.funcSpecifiedPerils(common.MTA_excel_data_map), "Select covers function is having issue(S) . ");
		
		if(((String)common.MTA_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
			customAssert.assertTrue(funcInsuredProperties(common.MTA_excel_data_map), "Insured Property function is having issue(S) . ");
		}
		
		if(((String)common.MTA_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
			customAssert.assertTrue(funcBusinessInterruption(common.MTA_excel_data_map), "Business Interruption function is having issue(S) . ");
		}
		
		if(((String)common.MTA_excel_data_map.get("CD_Liability")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
			customAssert.assertTrue(funcEmployersLiability(common.MTA_excel_data_map), "Employers Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
			customAssert.assertTrue(funcPublicLiability(common.MTA_excel_data_map), "Public Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
			customAssert.assertTrue(funcProductsLiability(common.MTA_excel_data_map), "Products Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
			customAssert.assertTrue(funcLiabilityInformation(common.MTA_excel_data_map), "Liability Information function is having issue(S) . ");
			}
		if(((String)common.MTA_excel_data_map.get("CD_SpecifiedAllRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
			customAssert.assertTrue(funcSpecifiedAllRisks(common.MTA_excel_data_map), "Specified All Risks function is having issue(S) . ");
			}
		
		
		if(((String)common.MTA_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
			customAssert.assertTrue(funcComputersandElectronicRisks(common.MTA_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
			}
		
		if(((String)common.MTA_excel_data_map.get("CD_Money")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
			customAssert.assertTrue(funcMoney(common.MTA_excel_data_map), "Money function is having issue(S) . ");
			}
		if(((String)common.MTA_excel_data_map.get("CD_GoodsInTransit")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
			customAssert.assertTrue(funcGoodsInTransit(common.MTA_excel_data_map), "Goods In Transit function is having issue(S) . ");
			}
		/*if(((String)common.MTA_excel_data_map.get("CD_MarineCargo")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Marine Cargo"),"Issue while Navigating to Marine Cargo screen . ");
			customAssert.assertTrue(funcMarineCargo(common.MTA_excel_data_map), "Marine Cargo function is having issue(S) . ");
			}*/
		/*if(((String)common.MTA_excel_data_map.get("CD_CyberandDataSecurity")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
			customAssert.assertTrue(funcCyberAndDataSecurity(common.MTA_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
			}*/
		/*if(((String)common.MTA_excel_data_map.get("CD_DirectorsandOfficers")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Directors and Officers"),"Issue while Navigating to Directors and Officers screen . ");
			customAssert.assertTrue(funcDirectorsAndOfficers(common.MTA_excel_data_map), "Directors and Officers function is having issue(S) . ");
			}*/
		if(((String)common.MTA_excel_data_map.get("CD_FrozenFood")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Frozen Foods"),"Issue while Navigating to Frozen Foods screen . ");
			customAssert.assertTrue(funcFrozenFoods(common.MTA_excel_data_map), "Frozen Foods function is having issue(S) . ");
			}
		if(((String)common.MTA_excel_data_map.get("CD_LossofLicence")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Loss of Licence"),"Issue while Navigating to Loss of Licence screen . ");
		customAssert.assertTrue(funcLossofLicence(common.MTA_excel_data_map), "Loss of Licence function is having issue(S) . ");
			}
		if(((String)common.MTA_excel_data_map.get("CD_FidelityGuarantee")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
			customAssert.assertTrue(funcFidelityGuarantee(common.MTA_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
			}
		if(((String)common.MTA_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
			customAssert.assertTrue(funcTerrorism(common.MTA_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common_CCI.funcPremiumSummary(common.MTA_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Error in MTA Premium Summary . "));
		
		
		if(!TestBase.businessEvent.equalsIgnoreCase("Renewal")){
			Assert.assertTrue(common.funcStatusHandling(common.MTA_excel_data_map,code,"MTA"));
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
public void RewindFlow(String code,String fileName,String policy_Status){
	System.out.println("Test method of Rewind For - "+code);
	TestUtil.reportStatus("Test Method of Rewind For - "+code, "Pass", true);
	
	
}

/**
 * 
 * This method handles all Cancellation Cases for CCI product.
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
		
		customAssert.assertTrue(Cancel_PremiumSummary(common.CAN_excel_data_map), "Issue in verifying Premium summary for cancellation");
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""), "Unable to click on policies tab");
		Assert.assertTrue(common.funcStatusHandling(common.NB_excel_data_map,code,TestBase.businessEvent));
				
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
    Map<Object, Object> Date_map = common.CAN_excel_data_map;
    int dateDif = Integer.parseInt((String)Date_map.get("CancelAfterDays"));
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
									
										if(!headerName.contains("Pen Comm %")&& !headerName.contains("Com. Rate (%)") && !headerName.contains("Broker Comm %") && !headerName.contains("Gross Comm %")
												&& !headerName.contains("Insurance Tax Rate") ){
											WebElement sec_Val = driver.findElement(By.xpath(ReturnP_TablePath+"//tbody//tr["+row+"]//td["+col+"]"));
											sectionValue = sec_Val.getText();
											sectionValue = sectionValue.replaceAll(",", "");
											ReturnP_Table_TotalVal.put(headerName, Double.parseDouble(sectionValue));
											
										}else{
											continue;
										}
										common_CCI.CAN_CCI_ReturnP_Values_Map.put(sectionName, ReturnP_Table_TotalVal);
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
								
								common_CCI.CAN_CCI_ReturnP_Values_Map.put(sectionName, ReturnP_Table_CoverVal);
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
						Can_returnP_Error = Can_returnP_Error + Can_ReturnP_Total_Validation(sectionNames,common_CCI.CAN_CCI_ReturnP_Values_Map);
					else
						Can_returnP_Error = Can_returnP_Error + CanReturnPTable_CoverSection_Validation(policy_Duration,DaysRemain, s_Name, common_CCI.CAN_CCI_ReturnP_Values_Map);								
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
					exp_value = exp_value + common_CCI.CAN_CCI_ReturnP_Values_Map.get(section).get("Net Premium (GBP)");
			}
			String t_NetNetP_actual = Double.toString(common_CCI.CAN_CCI_ReturnP_Values_Map.get("Totals").get("Net Premium (GBP)"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(t_NetNetP_actual),"Net Premium (GBP)");

			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_CCI.CAN_CCI_ReturnP_Values_Map.get(section).get("Gross Premium (GBP)");
			}
			String t_grossP_actual = Double.toString(common_CCI.CAN_CCI_ReturnP_Values_Map.get("Totals").get("Gross Premium (GBP)"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(t_grossP_actual)," Gross Premium (GBP)");
				
			double premium_diff = Double.parseDouble(common.roundedOff(String.valueOf(exp_value))) - Double.parseDouble(t_grossP_actual);
			
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_CCI.CAN_CCI_ReturnP_Values_Map.get(section).get("Commission (GBP)");
			}
			String t_bc_actual =  Double.toString(common_CCI.CAN_CCI_ReturnP_Values_Map.get("Totals").get("Commission (GBP)"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(t_bc_actual),"Commission (GBP)");
		
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_CCI.CAN_CCI_ReturnP_Values_Map.get(section).get("Gross IPT (GBP)");
			}
			String t_GrossIPT_actual =  Double.toString(common_CCI.CAN_CCI_ReturnP_Values_Map.get("Totals").get("Gross IPT (GBP)"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(t_GrossIPT_actual),"Gross IPT (GBP)");
		
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_CCI.CAN_CCI_ReturnP_Values_Map.get(section).get("Net IPT (GBP)");
			}
			String t_NetIPT_actual = Double.toString(common_CCI.CAN_CCI_ReturnP_Values_Map.get("Totals").get("Net IPT (GBP)"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(t_NetIPT_actual),"Net IPT (GBP)");
					
			if(premium_diff<0.05 && premium_diff>-0.05){
				TestUtil.reportStatus("Total Premium [<b> "+exp_value+" </b>] matches with actual total premium [<b> "+t_grossP_actual+" </b>]as expected for Totals in Transaction Premium table .", "Pass", false);
				return 0;
				
			}else{
				TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+exp_value+"</b>] and Actual Premium [<b> "+t_grossP_actual+"</b>] for Totals in Transaction Premium table . </p>", "Fail", true);
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
			}if(code.contains("LossofLicence")){
				code = "LossOfLicence";
			}
			
			double annual_NetNetP = Double.parseDouble((String)map_data.get("PS_"+code+"_NP"));
			String t_NetNetP_expected = common.roundedOff(Double.toString((annual_NetNetP/365)*DaysRemain));
			String t_NetNetP_actual = Double.toString(common_CCI.CAN_CCI_ReturnP_Values_Map.get(sectionNames).get("Net Premium (GBP)"));
			customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(t_NetNetP_expected),Double.parseDouble(t_NetNetP_actual)," Net Net Premium"),"Mismatched Net Net Premium");
			
			// Gross Premium : 
			
			double denominator = 1- (Double.parseDouble((String)map_data.get("PS_"+code+"_CR"))/100);
			double t_grossP = Double.parseDouble(t_NetNetP_expected) / denominator;
			String t_grossP_actual = Double.toString(common_CCI.CAN_CCI_ReturnP_Values_Map.get(sectionNames).get("Gross Premium (GBP)"));
			customAssert.assertTrue(CommonFunction.compareValues(t_grossP,Double.parseDouble(t_grossP_actual),sectionNames+" Transaction Gross Premium"),"Mismatched Gross Premium Values");
			
			//Commision :
			
			double t_comm = t_grossP* (Double.parseDouble((String)map_data.get("PS_"+code+"_CR"))/100);
			String t_Actual_Comm = Double.toString(common_CCI.CAN_CCI_ReturnP_Values_Map.get(sectionNames).get("Commission (GBP)"));
			customAssert.assertTrue(CommonFunction.compareValues(t_comm,Double.parseDouble(t_Actual_Comm),"Commission (GBP)"),"Mismatched Commission (GBP) Values");
			
			//Gross IPT 
			double t_InsuranceTax = (t_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_IPT")))/100.0;
			t_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(t_InsuranceTax)));
			String t_InsuranceTax_actual = Double.toString(common_CCI.CAN_CCI_ReturnP_Values_Map.get(sectionNames).get("Gross IPT (GBP)"));
			customAssert.assertTrue(CommonFunction.compareValues(t_InsuranceTax,Double.parseDouble(t_InsuranceTax_actual),"Gross IPT (GBP)"),"Mismatched Gross IPT (GBP) Values");
			
			
			//Net IPT :
			
			double t_NetIPT =  Double.parseDouble(t_NetNetP_expected)  * (Double.parseDouble((String)map_data.get("PS_"+code+"_IPT"))/100);
			t_NetIPT = Double.parseDouble(common.roundedOff(Double.toString(t_NetIPT)));
			String t_NetIPT_actual = Double.toString(common_CCI.CAN_CCI_ReturnP_Values_Map.get(sectionNames).get("Net IPT (GBP)"));
			
			customAssert.assertTrue(CommonFunction.compareValues(t_NetIPT,Double.parseDouble(t_NetIPT_actual),"Net IPT (GBP)"),"Mismatched Net IPT (GBP) Values");
			
		
			double premium_diff = t_grossP - Double.parseDouble(t_grossP_actual);
			
			if(premium_diff<0.09 && premium_diff>-0.09){
				TestUtil.reportStatus("Total Premium [<b> "+t_grossP+" </b>] matches with actual total premium [<b> "+t_grossP_actual+" </b>]as expected for "+sectionNames+" in Transaction Premium table .", "Pass", false);
				return 0;
			}else{
				TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+t_grossP+"</b>] and Actual Premium [<b> "+t_grossP_actual+"</b>] for "+sectionNames+" in Transaction Premium table . </p>", "Fail", true);
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
						s_GrossCommRate = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+3+"]").replaceAll(",", "")));
					}
													
					double s_GrossCommAmt = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+4+"]").replaceAll(",", "")));
					
					double s_NetP = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+5+"]").replaceAll(",", "")));
					
					
					double s_GrossIPT = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+6+"]").replaceAll(",", "")));
					
									
					double s_NetIPT = Math.abs(Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, CancelP_TablePath+"//tbody//tr["+row+"]//td["+7+"]").replaceAll(",", "")));
					
					
					common.compareValues(s_GrossP, common_CCI.CAN_CCI_ReturnP_Values_Map.get(sectionName).get("Gross Premium (GBP)"), "Cancellation Gross Premium");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_GrossCommRate,common_CCI.CAN_CCI_ReturnP_Values_Map.get(sectionName).get("Com. Rate (%)"), "Cancellation Gross Comm Percentage");
					}				
					common.compareValues(s_GrossCommAmt, common_CCI.CAN_CCI_ReturnP_Values_Map.get(sectionName).get("Commission (GBP)"), "Cancellation Commission (GBP) Amount");
					common.compareValues(s_NetP, common_CCI.CAN_CCI_ReturnP_Values_Map.get(sectionName).get("Net Premium (GBP)"), "Cancellation Net Premium");
					
					common.compareValues(s_GrossIPT, common_CCI.CAN_CCI_ReturnP_Values_Map.get(sectionName).get("Gross IPT (GBP)"), "Cancellation Gross IPT (GBP) amount");
					common.compareValues(s_NetIPT, common_CCI.CAN_CCI_ReturnP_Values_Map.get(sectionName).get("Net IPT (GBP)"), "CancellationNet IPT (GBP)");
														
				}	
			}
				
			return retVal;		
	
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}

/**
 * 
 * This method handles CCF Policy Details screens scripting.
 * 
 */
public boolean funcPolicyDetails(Map<Object, Object> map_data,String code ,String event){
	
	boolean retvalue = true;
	try{
		customAssert.assertTrue(common.funcPageNavigation("Policy Details", ""), "Navigation problem to Policy Details page .");
		
		//customAssert.assertTrue(k.Input("CCF_PD_ProposerName", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		//customAssert.assertTrue(k.Input("CCF_PD_ProposerName", (String)map_data.get("NB_ClientName")),	"Unable to enter value in Proposer Name  field .");
		//TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "NB_ClientName", k.getAttribute("CCF_PD_ProposerName", "value"), common.NB_excel_data_map);
		
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_PD_ProposerName", "value"),"Proposer Name Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("CCF_CC_TradingName", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_CC_TradingName", (String)map_data.get("PD_TradingName")),	"Unable to enter value in Trading Name  field .");
		customAssert.assertTrue(k.Input("CCF_PD_BusinessDesc", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_PD_BusinessDesc", (String)map_data.get("PD_BusinessDesc")),	"Unable to enter value in Business Desc  field .");
		
		if(!common.currentRunningFlow.contains("Renewal")){
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_1QS", (String)map_data.get("PD_1QS")), "Unable to Select 1QS radio button on Policy Details Page.");
		}
		
		customAssert.assertTrue(k.Input("CCF_PD_DateEstablishment", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_PD_DateEstablishment", (String)map_data.get("PD_DateEstablishment")),	"Unable to enter value in Date Establishment  field .");
		if(!common.currentRunningFlow.contains("Renewal")){
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
		customAssert.assertTrue(common.validatePostCode((String)map_data.get("PD_Postcode")),"Post Code is not in Correct format .");
		
		if(!common.currentRunningFlow.contains("Renewal")){
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
		
		//customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CarrierOverride", (String)map_data.get("PD_CarrierOverride")), "Unable to Select Carrier Override radio button on Policy Details Page.");
		
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

public boolean funcPolicyDetails_MTA(Map<Object, Object> map_data,String code ,String event){
	
	boolean retvalue = true;
	try{
		customAssert.assertTrue(common.funcPageNavigation("Policy Details", ""), "Navigation problem to Policy Details page .");
		
		//customAssert.assertTrue(k.Input("CCF_PD_ProposerName", (String)map_data.get(common.currentRunningFlow+"_ClientName")),	"Unable to enter value in Proposer Name  field .");
		//customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_PD_ProposerName", "value"),"Proposer Name Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("CCF_CC_TradingName", (String)map_data.get("PD_TradingName")),	"Unable to enter value in Trading Name  field .");
		customAssert.assertTrue(k.Input("CCF_PD_BusinessDesc", (String)map_data.get("PD_BusinessDesc")),	"Unable to enter value in Business Desc  field .");
		
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
		k.waitTwoSeconds();
		
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_TaxExempt", (String)map_data.get("PD_TaxExempt")), "Unable to Select TaxExempt radio button on Policy Details Page.");
		if(((String)map_data.get("PD_TaxExempt")).equalsIgnoreCase("Yes")){
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_data.get("Automation Key"), "PS_IPTRate", "0", map_data);
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_data.get("Automation Key"), "PS_InsuranceTaxButton", "Yes", map_data);
		}else{
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_data.get("Automation Key"), "PS_IPTRate", "12", map_data);
		}
		
		k.waitTwoSeconds();
		
		//if(((String)common.NB_excel_data_map.get("PD_TCS_TradeCode_Button")).equalsIgnoreCase("Yes")){
			//customAssert.assertTrue(common.tradeCodeSelection((String)map_data.get("PD_TCS_TradeCode") , "Policy Details" , 0),"Trade code selection function is having issue(S).");
		//}
		
		TestUtil.reportStatus("Entered all the details on Policy Details page .", "Info", true);
		
		return retvalue;
		
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Unable to to do operation on policy details page. \n", t);
        return false;
 }
}


/**
 * 
 * This method handles CCF Previous Claims screens scripting.
 * 
 */
public boolean funcPreviousClaims(Map<Object, Object> map_data){

    boolean retvalue = true;
    
      try {               
			customAssert.assertTrue(common.funcPageNavigation("Previous Claims",""), "Previous Claims Page Navigation issue . ");
			
			
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PC_NoClaim_InLastFiveYears", (String)map_data.get("PC_NoClaim_InLastFiveYears")), "Unable to Select NoClaim_InLastFiveYears radio button on Previous Claims Page.");
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PC_NoClaim_Exceeding", (String)map_data.get("PC_NoClaim_Exceeding")), "Unable to Select NoClaim_Exceeding radio button on Previous Claims Page.");
			
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Previous Claims .");
			
			TestUtil.reportStatus("Entered all the details on Previous Claims page .", "Info", true);
			                         
             return retvalue;
             
      } catch(Throwable t) {
             String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
          TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
          Assert.fail("Unable to enter details in Previous Claims Page", t);
          return false;
      }


}

public boolean funcInsuredProperties(Map<Object, Object> map_data){
	
	boolean r_Value = true;
	
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
		customAssert.assertTrue(common.funcPageNavigation("Insured Properties", ""),"Insured Properties page is having issue(S)");
		
		customAssert.assertTrue(k.Input("CCF_IP_AnyOneEvent", (String)map_data.get("IP_AnyOneEvent")),	"Unable to enter value in any one Event field .");
		customAssert.assertTrue(k.Input("IP_Landslip", (String)map_data.get("IP_Landslip")),"Unable to enter value in Subsidence Ground Heave or Landslip field .");
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
		
		int count = 0;
		int noOfProperties = 0;
		
		
		
		String[] properties = ((String)map_data.get("IP_AddProperty")).split(";");
        noOfProperties = properties.length;
		
        
        if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
        	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	    }
		
		while(count < noOfProperties ){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddProperty"), "Unable to click Add Property Button on Insured Properties .");
			customAssert.assertTrue(addProperty(map_data,count,internal_data_map),"Error while adding insured proprty  .");
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

public boolean addProperty(Map<Object, Object> map_data,int count,Map<String, List<Map<String, String>>> internal_data_map){
	
	boolean r_value=true;
	
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
		if(common.currentRunningFlow.equalsIgnoreCase("NB")){
			customAssert.assertTrue(k.DropDownSelection("CCF_PoD_DayOneUplift",internal_data_map.get("Property Details").get(count).get("PoD_DayOneUplift")), "Unable to select value from Day One uplift dropdown .");
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q1", internal_data_map.get("Property Details").get(count).get("PoD_SOF_Q1")), "Unable to Select first SOF radio button on Policy Details Page.");
		}
		
		//Proximity
		customAssert.assertTrue(k.Input("CCF_PoD_ProximityDescription", internal_data_map.get("Property Details").get(count).get("PoD_ProximityDescription")),"Unable to enter value in Proximity description . ");
		
		/*customAssert.assertTrue(common.funcButtonSelection("Save"));
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"Address Field Should Contain Valid Value on Client Details .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Postcode", "value"),"Postcode Field Should Contain Valid Value on Client Details .");
		customAssert.assertTrue(common.validatePostCode(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_Postcode")),"Post Code is not in Correct format .");
		*/
		//Trade Code
		if((internal_data_map.get("Property Details").get(count).get("PoD_TCS_TradeCode_Button")).equalsIgnoreCase("Yes")){
		//	customAssert.assertTrue(common.tradeCodeSelection((String)common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_MD_TCS_TradeCode"),"Property Details",count),"Trade code selection function is having issue(S).");	
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
		
		
		if(((String)map_data.get("CD_BusinessInterruption")).equalsIgnoreCase("Yes") ||
				((String)map_data.get("CD_Add_BusinessInterruption")).equalsIgnoreCase("Yes")){
			List<WebElement> bespoke_BI_btns = k.getWebElements("CCF_Btn_AddBespokeSumIns");
			WebElement BI_bespoke_btn = bespoke_BI_btns.get(1);
			BI_bespoke_btn.click();
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", internal_data_map.get("Property Details").get(count).get("BSI_BI_Description")),"Unable to enter value in BI Bespoke Description . ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", internal_data_map.get("Property Details").get(count).get("BSI_BI_SumInsured")),"Unable to enter value in Bespoke Sum Insured . ");
			customAssert.assertTrue(k.DropDownSelection("CCF_BSI_BI_IndemnityPeriod", internal_data_map.get("Property Details").get(count).get("BSI_BI_IndemnityPeriod")), "Unable to select value from Indemnity Period dropdown .");
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Bespoke Sum Insured inner page .");
		}
				
		
		double finalMDPremium = 0.00, finalBIPremium = 0.00;
		 
		 if(((String)map_data.get("CD_MaterialDamage")).equalsIgnoreCase("Yes") || 
				 ((String)map_data.get("CD_Add_MaterialDamage")).equalsIgnoreCase("Yes")){
			 customAssert.assertTrue(CommonFunction.PropertyDetails_HandleTables(map_data, "MD", count,internal_data_map),"failed in MD handle table");
			 customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
			 
			 finalMDPremium =  finalMDPremium + Double.parseDouble(internal_data_map.get("Property Details").get(count).get("MD_TotalPremium"));
			 TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_MaterialDamage_NP", String.valueOf(finalMDPremium), map_data);
			 
			 if(((String)map_data.get("CD_BusinessInterruption")).equalsIgnoreCase("Yes") ||
					 ((String)map_data.get("CD_Add_BusinessInterruption")).equalsIgnoreCase("Yes")){
				 customAssert.assertTrue(CommonFunction.PropertyDetails_HandleTables(map_data, "BI", count,internal_data_map),"failed in BI handle table");
				 customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
				 
				 finalBIPremium =  finalBIPremium + Double.parseDouble(internal_data_map.get("Property Details").get(count).get("BI_TotalPremium"));
				 TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_BusinessInterruption_NP", String.valueOf(finalBIPremium),map_data);
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


public boolean funcBusinessInterruption(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Business Interruption", ""),"Business Interruption page navigations issue(S)");
		
		//Extensions
		customAssert.assertTrue(k.Input("CCF_BI_UnspecifiedSupplier", (String)map_data.get("BI_UnspecifiedSupplier")),"Unable to enter value in Unspecified Supplier(s) Limit . ");
		customAssert.assertTrue(k.Input("CCF_BI_UnspecifiedCustomer", (String)map_data.get("BI_UnspecifiedCustomer")),"Unable to enter value in Unspecified Supplier(s) Limit . ");
		customAssert.assertTrue(k.Input("CCF_BI_DenialAccess", (String)map_data.get("BI_DenialAccess")),"Unable to enter value in Denial of Access Limit . ");
		customAssert.assertTrue(k.Input("CCF_BI_PublicUtilities", (String)map_data.get("BI_PublicUtilities")),"Unable to enter value in Public Utilities Limit . ");
		customAssert.assertTrue(k.Input("CCF_BI_InfectiousDiseases", (String)map_data.get("BI_InfectiousDiseases")),"Unable to enter value in Infectious Diseases Limit . ");
		customAssert.assertTrue(k.Input("CCF_BI_BookDebts", (String)map_data.get("BI_BookDebts")),"Unable to enter value in Book Debts Limit . ");
		
		//Additional Information
		customAssert.assertTrue(k.Input("CCF_BI_AdditionalInfo_CoverReq", (String)map_data.get("BI_AdditionalInfo_CoverReq")),"Unable to enter value in Additional information . ");
		
		//EML
		customAssert.assertTrue(k.Input("CCF_PoD_EmlAmount_GBP", (String)map_data.get("BI_EmlAmount")),"Unable to enter value in EmlAmount_GBP . ");
		customAssert.assertTrue(k.Input("CCF_PoD_EmlAmount_Percent", (String)map_data.get("BI_EmlAmount_Percent")),"Unable to enter value in Eml amount (%) . ");
				
		//Inner BI-Specified Suppliers
		customAssert.assertTrue(addSpecifiedSuppliers(), "Error while adding Specified Suppliers . ");
		
		//Inner BI-Specified Customer
		customAssert.assertTrue(addSpecifiedCustomer(), "Error while adding Specified Customer . ");
				
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
		
		TestUtil.reportStatus("Business Interruption details are filled successfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
	}

public boolean addSpecifiedSuppliers(){
	boolean r_value=true;
	
	try{
		int total_count_BI_SpecifiedSupplier = 0;
		
        Map<Object, Object> data_map = new HashMap<>();
		Map<String, List<Map<String, String>>> internal_data_map = new HashMap<>();
		switch(common.currentRunningFlow){
			case "NB":
				internal_data_map = common.NB_Structure_of_InnerPagesMaps;
				data_map = common.NB_excel_data_map;
				break;
			case "MTA":
				internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
				data_map = common.MTA_excel_data_map;
				break;
			case "Renewal":
				internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
				data_map = common.Renewal_excel_data_map;
				break;
			case "Rewind":
				internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
				data_map = common.Rewind_excel_data_map;
				break;
			case "Requote":
				internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
				break;
		
		}
		
		String[] properties = ((String)data_map.get("BI_AddSpecifiedSupplier")).split(";");
		total_count_BI_SpecifiedSupplier = properties.length;
		
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	        	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	    }
		
		int count=0;
		while(count < total_count_BI_SpecifiedSupplier){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddSpecified_Supplier"), "Unable to click on AddSpecified_Supplier Button on BI page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_SS_SupplierName", internal_data_map.get("SpecifiedSupplier").get(count).get("SS_SupplierName")),"Unable to enter value in Address field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_SS_SupplierName", "value"),"SupplierName Field Should Contain Valid Value  .");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Address", internal_data_map.get("SpecifiedSupplier").get(count).get("SS_SupplierAddress_Line1")),"Unable to enter value in SupplierAddress_Line1 field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"SupplierAddress Field Should Contain Valid Value  .");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line2", internal_data_map.get("SpecifiedSupplier").get(count).get("SS_SupplierAddress_Line2")),"Unable to enter value in SupplierAddress field line 2 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line3", internal_data_map.get("SpecifiedSupplier").get(count).get("SS_SupplierAddress_Line3")),"Unable to enter value in SupplierAddress field line 3 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Town", internal_data_map.get("SpecifiedSupplier").get(count).get("SS_Town")),"Unable to enter value in Town field . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_County", internal_data_map.get("SpecifiedSupplier").get(count).get("SS_County")),"Unable to enter value in County  . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode",internal_data_map.get("SpecifiedSupplier").get(count).get("SS_Postcode")),"Unable to enter value in PostCode field .");
			customAssert.assertTrue(common.validatePostCode(internal_data_map.get("SpecifiedSupplier").get(count).get("SS_Postcode")),"Post Code is not in Correct format .");
			customAssert.assertTrue(k.Input("CCF_SS_SupplierLimit", internal_data_map.get("SpecifiedSupplier").get(count).get("SS_SupplierLimit")),"Unable to enter value in SupplierLimit field .");
			
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Specified Suppliers inner page .");
			count++;
		}
		
	}catch(Throwable t){
		
		r_value=false;
	}
	
	return r_value;
	
}

public boolean addSpecifiedCustomer(){
	boolean r_value=true;
	
	try{
		int total_count_BI_SpecifiedCustomer = 0;
		
		 Map<Object, Object> data_map = new HashMap<>();
			Map<String, List<Map<String, String>>> internal_data_map = new HashMap<>();
			switch(common.currentRunningFlow){
				case "NB":
					internal_data_map = common.NB_Structure_of_InnerPagesMaps;
					data_map = common.NB_excel_data_map;
					break;
				case "MTA":
					internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
					data_map = common.MTA_excel_data_map;
					break;
				case "Renewal":
					internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
					data_map = common.Renewal_excel_data_map;
					break;
				case "Rewind":
					internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
					data_map = common.Rewind_excel_data_map;
					break;
				case "Requote":
					internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
					break;
			
			}
			
			String[] properties = ((String)data_map.get("BI_AddSpecifiedCustomer")).split(";");
			total_count_BI_SpecifiedCustomer = properties.length;
			
			 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
		        	
		    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
		    }
		
		int count=0;
		while(count < total_count_BI_SpecifiedCustomer){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddSpecified_Customer"), "Unable to click on AddSpecified_Customer Button on BI page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_SS_CustomerName", internal_data_map.get("SpecifiedCustomer").get(count).get("SC_CustomerName")),"Unable to enter value in Address field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_SS_CustomerName", "value"),"SupplierName Field Should Contain Valid Value  .");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Address", internal_data_map.get("SpecifiedCustomer").get(count).get("SC_CustomerAddress_Line1")),"Unable to enter value in CustomerAddress_Line1 field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"SupplierAddress Field Should Contain Valid Value  .");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line2", internal_data_map.get("SpecifiedCustomer").get(count).get("SC_CustomerAddress_Line2")),"Unable to enter value in CustomerAddress field line 2 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line3", internal_data_map.get("SpecifiedCustomer").get(count).get("SC_CustomerAddress_Line3")),"Unable to enter value in CustomerAddress field line 3 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Town", internal_data_map.get("SpecifiedCustomer").get(count).get("SC_Town")),"Unable to enter value in Town field . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_County", internal_data_map.get("SpecifiedCustomer").get(count).get("SC_County")),"Unable to enter value in County  . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode", internal_data_map.get("SpecifiedCustomer").get(count).get("SC_Postcode")),"Unable to enter value in PostCode field .");
			customAssert.assertTrue(common.validatePostCode(internal_data_map.get("SpecifiedCustomer").get(count).get("SC_Postcode")),"Post Code is not in Correct format .");
			customAssert.assertTrue(k.Input("CCF_SS_CustomerLimit", internal_data_map.get("SpecifiedCustomer").get(count).get("SC_CustomerLimit")),"Unable to enter value in Customer Limit field .");
			
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Specified Customer inner page .");
			count++;
		}
		
	}catch(Throwable t){
		
		r_value=false;
	}
	
	return r_value;
	
}

public boolean funcEmployersLiability(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Employers Liability", ""),"Employers Liability page navigations issue(S)");
		
		
		customAssert.assertTrue(k.Input("CCF_EL_LimitOfIndemnity", (String)map_data.get("EL_LimitOfIndemnity")),"Unable to enter value in LimitOfIndemnity . ");
		
		//Excesses
		customAssert.assertTrue(k.Input("CCF_EL_Excess", (String)map_data.get("EL_Excess")),"Unable to enter value in EL Excess . ");
		//customAssert.assertTrue(k.Input("CCF_EL_MinimumDeposit", Keys.chord(Keys.CONTROL, "a")),"Unable to select Minimum Deposit field");
		customAssert.assertTrue(k.Input("CCF_EL_MinimumDeposit", (String)map_data.get("EL_MinimumDeposit")),"Unable to enter value in Minimum Deposit . ");
				
		//Inner BI-Specified Suppliers
		
		customAssert.assertTrue(addELItems(map_data), "Error while adding EL Items . ");
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Employers Liability .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "EL", "Employers Liability", "EL AddItem");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_EmployersLiability_NP", String.valueOf(sPremiumm), map_data);
		 
		TestUtil.reportStatus("Employers Liability details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
	}

public boolean addELItems(Map<Object, Object> map_data){
	boolean r_value=true;
	
	try{
		int total_count_EL_items = 0;
		/*if(common.no_of_inner_data_sets.get("EL AddItem")==null){
			total_count_EL_items = 0;
		}else{
			total_count_EL_items = common.no_of_inner_data_sets.get("EL AddItem");
		}*/
		
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
		
		String[] properties = ((String)map_data.get("EL_AddItem")).split(";");
		total_count_EL_items = properties.length;
		
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	        	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	    }
		
		int count=0;
		while(count < total_count_EL_items){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on EL page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", internal_data_map.get("EL AddItem").get(count).get("AD_EL_ItemDesc")),"Unable to enter value in Description field. ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", internal_data_map.get("EL AddItem").get(count).get("AD_EL_ItemSumIns")),"Unable to enter value in Sum Insured field. ");
			
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Specified Customer inner page .");
			count++;
			
		}
		
	}catch(Throwable t){
		
		r_value=false;
	}
	
	return r_value;
	
}

public boolean funcELDInformation(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Employers Liability Database", ""),"Employers Liability Database page navigations issue(S)");
		
		//Policy Details
		customAssert.assertTrue(k.SelectRadioBtn("CCF_ERNExempt", (String)map_data.get("ELD_ERNExempt")), "Unable to Select ERN Exempt radio button .");
		
		if(((String)map_data.get("ELD_ERNExempt")).equals("No")){
			customAssert.assertTrue(k.Input("CCF_EmpRefNo", Keys.chord(Keys.CONTROL, "a")),"Unable to select Employer Reference Number field");
			customAssert.assertTrue(k.Input("CCF_EmpRefNo", (String)map_data.get("ELD_Employer RefNumber")),"Unable to enter value in Employer Reference Number . ");
		}
		//Inner - Subsidiary Details
		customAssert.assertTrue(addSubsidiary(), "Error while adding ELD Subsidiary . ");
				
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
		
		TestUtil.reportStatus("ELD Information details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
	}

public boolean addSubsidiary(){
	boolean r_value=true;
	
	try{
		int total_count_ELD_Subsidiary = 0;
		if(common.no_of_inner_data_sets.get("ELD AddSubsidiary")==null){
			total_count_ELD_Subsidiary = 0;
		}else{
			total_count_ELD_Subsidiary = common.no_of_inner_data_sets.get("ELD AddSubsidiary");
		}
		
		int count=0;
		while(count < total_count_ELD_Subsidiary){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Subsidiary Button on EL page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_ELD_AS_TradingName", common.NB_Structure_of_InnerPagesMaps.get("ELD AddSubsidiary").get(count).get("ELD_AS_TradingName")),"Unable to enter value in Address field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_ELD_AS_TradingName", "value"),"SupplierName Field Should Contain Valid Value  .");
			customAssert.assertTrue(k.Input("CCF_ELD_AS_AddressLine_1", common.NB_Structure_of_InnerPagesMaps.get("ELD AddSubsidiary").get(count).get("ELD_AS_AddressLine_1")),"Unable to enter value in CustomerAddress_Line1 field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_ELD_AS_AddressLine_1", "value"),"SupplierAddress Field Should Contain Valid Value  .");
			customAssert.assertTrue(k.Input("CCF_ELD_AS_AddressLine_2", common.NB_Structure_of_InnerPagesMaps.get("ELD AddSubsidiary").get(count).get("ELD_AS_AddressLine_2")),"Unable to enter value in CustomerAddress field line 2 . ");
			customAssert.assertTrue(k.Input("CCF_ELD_AS_AddressLine_3", common.NB_Structure_of_InnerPagesMaps.get("ELD AddSubsidiary").get(count).get("ELD_AS_AddressLine_3")),"Unable to enter value in CustomerAddress field line 3 . ");
			customAssert.assertTrue(k.Input("CCF_ELD_AS_AddressLine_4", common.NB_Structure_of_InnerPagesMaps.get("ELD AddSubsidiary").get(count).get("ELD_AS_AddressLine_4")),"Unable to enter value in CustomerAddress field line 3 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Town", common.NB_Structure_of_InnerPagesMaps.get("ELD AddSubsidiary").get(count).get("ELD_AS_Town")),"Unable to enter value in Town field . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_County", common.NB_Structure_of_InnerPagesMaps.get("ELD AddSubsidiary").get(count).get("ELD_AS_County")),"Unable to enter value in County  . ");
			customAssert.assertTrue(k.Input("CCF_ELD_AS_Country", common.NB_Structure_of_InnerPagesMaps.get("ELD AddSubsidiary").get(count).get("ELD_AS_Country")),"Unable to enter value in Town field . ");
			customAssert.assertTrue(k.Input("CCF_ELD_AS_Postcode", common.NB_Structure_of_InnerPagesMaps.get("ELD AddSubsidiary").get(count).get("ELD_AS_Postcode")),"Unable to enter value in PostCode field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_ELD_AS_Postcode", "value"),"Postcode Field Should Contain Valid Value  .");
			customAssert.assertTrue(common.validatePostCode(common.NB_Structure_of_InnerPagesMaps.get("ELD AddSubsidiary").get(count).get("ELD_AS_Postcode")),"Post Code is not in Correct format .");
				
			customAssert.assertTrue(k.SelectRadioBtn("CCF_ERNExempt", common.NB_Structure_of_InnerPagesMaps.get("ELD AddSubsidiary").get(count).get("ELD_AS_ERNExempt")), "Unable to Select ERN Exempt radio button .");
			
			if(common.NB_Structure_of_InnerPagesMaps.get("ELD AddSubsidiary").get(count).get("ELD_AS_ERNExempt").equals("No")){
				customAssert.assertTrue(k.Input("CCF_EmpRefNo", Keys.chord(Keys.CONTROL, "a")),"Unable to select Employer Reference Number field");
				customAssert.assertTrue(k.Input("CCF_EmpRefNo", common.NB_Structure_of_InnerPagesMaps.get("ELD AddSubsidiary").get(count).get("ELD_AS_EmployerRefNumber")),"Unable to enter value in Employer Reference Number . ");
			}
			
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Subsidiary inner page .");
			count++;
		}
		
	}catch(Throwable t){
		
		r_value=false;
	}
	
	return r_value;
	
}

public boolean funcPublicLiability(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Public Liability", ""),"Employers Liability page navigations issue(S)");
		
		
		customAssert.assertTrue(k.Input("CCF_PL_IndemnityLimit", (String)map_data.get("PL_IndemnityLimit")),"Unable to enter value in PL Indemnity Limit . ");
		customAssert.assertTrue(k.Input("CCF_PL_DepositPremium", (String)map_data.get("PL_DepositPremium")),"Unable to enter value in PL Indemnity Limit . ");
		customAssert.assertTrue(k.Input("CCF_PL_PropertyDamageExcess", (String)map_data.get("PL_PropertyDamageExcess")),"Unable to enter value in PL Indemnity Limit . ");
		customAssert.assertTrue(k.Input("CCF_PL_HeatWorkAwayExcess", (String)map_data.get("PL_HeatWorkAwayExcess")),"Unable to enter value in PL Indemnity Limit . ");
		
		//Inner BI-Specified Suppliers
		customAssert.assertTrue(addPLItems(map_data), "Error while adding PL Items . ");		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Public Liability .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "PL", "Public Liability", "PL AddItem");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_PublicLiability_NP", String.valueOf(sPremiumm), common.NB_excel_data_map);
				
		TestUtil.reportStatus("Public Liability details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
	}

public boolean addPLItems(Map<Object, Object> map_data){
	boolean r_value=true;
	
	try{
		int total_count_PL_items = 0;
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
		
		String[] properties = ((String)map_data.get("PL_AddItem")).split(";");
		total_count_PL_items = properties.length;
		
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	        	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	    }
		int count=0;
		while(count < total_count_PL_items){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on PL page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", internal_data_map.get("PL AddItem").get(count).get("AD_PL_ItemDesc")),"Unable to enter value in Description field. ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", internal_data_map.get("PL AddItem").get(count).get("AD_PL_ItemSumIns")),"Unable to enter value in Sum Insured field. ");
			
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Specified Customer inner page .");
			count++;
		}
		
	}catch(Throwable t){
		
		r_value=false;
	}
	
	return r_value;
	
}

public boolean funcProductsLiability(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Products Liability", ""),"Products Liability page navigations issue(S)");
		
		
		customAssert.assertTrue(k.Input("CCF_PRL_IndemnityLimit", (String)map_data.get("PRL_IndemnityLimit")),"Unable to enter value in PRL Indemnity Limit . ");
		customAssert.assertTrue(k.Input("CCF_PL_DepositPremium", (String)map_data.get("PRL_DepositPremium")),"Unable to enter value in PRL DepositPremium . ");
			
		//Inner PRL
		customAssert.assertTrue(addPRLItems(map_data), "Error while adding PRL Items . ");		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Products Liability .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "PRL", "Product Liability", "PRL AddItem");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_ProductsLiability_NP", String.valueOf(sPremiumm), map_data);
				
		TestUtil.reportStatus("Products Liability details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
	}

public boolean addPRLItems(Map<Object, Object> map_data){
	boolean r_value=true;
	
	try{
		int total_count_PL_items = 0;
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
		
		String[] properties = ((String)map_data.get("PRL_AddItem")).split(";");
		total_count_PL_items = properties.length;
		
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	        	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	    }
		
		int count=0;
		while(count < total_count_PL_items){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on PRL page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description",internal_data_map.get("PRL AddItem").get(count).get("AD_PLR_ItemDesc")),"Unable to enter value in Description field. ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", internal_data_map.get("PRL AddItem").get(count).get("AD_PLR_ItemSumIns")),"Unable to enter value in Sum Insured field. ");
			
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
		
		customAssert.assertTrue(common.funcPageNavigation("Liability Information", ""),"Liability Information page navigations issue(S)");
		
		//Statement of Fact
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LI_SOF_Q1", (String)map_data.get("LI_SOF_Q1")), "Unable to Select Liability Information MF Q1 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LI_SOF_Q2", (String)map_data.get("LI_SOF_Q2")), "Unable to Select Liability Information MF Q2 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LI_SOF_Q3", (String)map_data.get("LI_SOF_Q3")), "Unable to Select Liability Information MF Q3 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LI_SOF_Q4", (String)map_data.get("LI_SOF_Q4")), "Unable to Select Liability Information MF Q4 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LI_SOF_Q5", (String)map_data.get("LI_SOF_Q5")), "Unable to Select Liability Information MF Q5 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LI_SOF_Q6", (String)map_data.get("LI_SOF_Q6")), "Unable to Select Liability Information MF Q6 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LI_SOF_Q7", (String)map_data.get("LI_SOF_Q7")), "Unable to Select Liability Information MF Q7 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LI_SOF_Q8", (String)map_data.get("LI_SOF_Q8")), "Unable to Select Liability Information MF Q8 radio button .");
				
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
		
		TestUtil.reportStatus("Liability Information details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
	}

public boolean funcSpecifiedAllRisks(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Specified All Risks", ""),"Specified All Risks page navigations issue(S)");
		
		customAssert.assertTrue(k.Input("CCF_SAR_AdditionalInfo", (String)map_data.get("SAR_AdditionalInfo")),"Unable to enter value in SAR_AdditionalInfo . ");
		
		//Inner Add Item
		customAssert.assertTrue(addSARItem(map_data), "Error while adding SAR Item . ");
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "SAR", "Specified All Risks", "SAR AddItem");	
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_SpecifiedAllRisks_NP", String.valueOf(sPremiumm), map_data);
		
		TestUtil.reportStatus("Specified All Risks details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
	}

public boolean addSARItem(Map<Object, Object> map_data){
	boolean r_value=true;
	
	try{
		int total_count_SAR_items = 0;
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
		
		String[] properties = ((String)map_data.get("SAR_AddItem")).split(";");
		total_count_SAR_items = properties.length;
		
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	        	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	    }
		
		int count=0;
		while(count < total_count_SAR_items){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on SAR page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("AD_SAR_ItemDesc", internal_data_map.get("SAR AddItem").get(count).get("AD_SAR_ItemDesc")),"Unable to enter value in Item Description field. ");
			customAssert.assertTrue(k.DropDownSelection("AD_SAR_GeoLimit", internal_data_map.get("SAR AddItem").get(count).get("AD_SAR_GeoLimit")),"Unable to Select value in Geographical Limit field. ");
			customAssert.assertTrue(k.Input("AD_SAR_SumIns", internal_data_map.get("SAR AddItem").get(count).get("AD_SAR_SumInsured")),"Unable to enter value in Sum Insured . ");
			customAssert.assertTrue(k.Input("AD_SAR_Excess", internal_data_map.get("SAR AddItem").get(count).get("AD_SAR_Excess")),"Unable to enter value in Excess  . ");
			
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Specified Suppliers inner page .");
			count++;
		}
		
	}catch(Throwable t){
		
		r_value=false;
	}
	
	return r_value;
	
}

public boolean funcContractorsAllRisks(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Contractors All Risks", ""),"Contractors All Risks page navigations issue(S)");
		
		customAssert.assertTrue(k.DropDownSelection("CCF_CAR_SingleRisk", (String)map_data.get("CAR_SingleRisk")),"Unable to enter value in Single Risk field. ");
		customAssert.assertTrue(k.DropDownSelection("CCF_CAR_ContractWorks_MaximumPeriod", (String)map_data.get("CAR_ContractWorks_MaximumPeriod")),"Unable to enter value in Contract Works Maximum Period field. ");
		customAssert.assertTrue(k.DropDownSelection("CCF_CAR_MaintinancePeriod", (String)map_data.get("CAR_MaintinancePeriod")),"Unable to enter value in Maintinance Period field. ");
		
		//Contract Works
		customAssert.assertTrue(k.Input("CCF_CAR_ContractWorks_IndemnityLimit", Keys.chord(Keys.CONTROL, "a")),"Unable to select SAR_AdditionalInfo field");
		customAssert.assertTrue(k.Input("CCF_CAR_ContractWorks_IndemnityLimit", (String)map_data.get("CAR_ContractWorks_IndemnityLimit")),"Unable to enter value in SAR_AdditionalInfo . ");
		
		//Construction Plant Tools & Equipment
		customAssert.assertTrue(k.Input("CCF_CAR_CPTE_PerItem", Keys.chord(Keys.CONTROL, "a")),"Unable to select SAR_AdditionalInfo field");
		customAssert.assertTrue(k.Input("CCF_CAR_CPTE_PerItem", (String)map_data.get("CAR_CPTE_IndemnityLimit")),"Unable to enter value in SAR_AdditionalInfo . ");
		customAssert.assertTrue(k.Input("CCF_CAR_CPTE_IndemnityLimit", Keys.chord(Keys.CONTROL, "a")),"Unable to select SAR_AdditionalInfo field");
		customAssert.assertTrue(k.Input("CCF_CAR_CPTE_IndemnityLimit", (String)map_data.get("CAR_CPTE_IndemnityLimit")),"Unable to enter value in SAR_AdditionalInfo . ");
		
		//Temporary Buildings
		customAssert.assertTrue(k.Input("CCF_CAR_TemporaryBuildings_IndemnityLimit", Keys.chord(Keys.CONTROL, "a")),"Unable to select SAR_AdditionalInfo field");
		customAssert.assertTrue(k.Input("CCF_CAR_TemporaryBuildings_IndemnityLimit", (String)map_data.get("CAR_TemporaryBuildings_IndemnityLimit")),"Unable to enter value in SAR_AdditionalInfo . ");
	
		//Hired In Property
		customAssert.assertTrue(k.Input("CCF_CAR_HiredInProperty_IndemnityLimit", Keys.chord(Keys.CONTROL, "a")),"Unable to select SAR_AdditionalInfo field");
		customAssert.assertTrue(k.Input("CCF_CAR_HiredInProperty_IndemnityLimit", (String)map_data.get("CAR_HiredInProperty_IndemnityLimit")),"Unable to enter value in SAR_AdditionalInfo . ");
		
		//Employees Personal Property
		customAssert.assertTrue(k.Input("CCF_CAR_EPP_IndemnityLimit", Keys.chord(Keys.CONTROL, "a")),"Unable to select SAR_AdditionalInfo field");
		customAssert.assertTrue(k.Input("CCF_CAR_EPP_IndemnityLimit", (String)map_data.get("CAR_EPP_IndemnityLimit")),"Unable to enter value in SAR_AdditionalInfo . ");
		customAssert.assertTrue(k.Input("CCF_CAR_EPP_PerItem", Keys.chord(Keys.CONTROL, "a")),"Unable to select SAR_AdditionalInfo field");
		customAssert.assertTrue(k.Input("CCF_CAR_EPP_PerItem", (String)map_data.get("CAR_EPP_PerItem")),"Unable to enter value in SAR_AdditionalInfo . ");
		
		customAssert.assertTrue(k.Input("CCF_CAR_PolicyExcess", Keys.chord(Keys.CONTROL, "a")),"Unable to select SAR_AdditionalInfo field");
		customAssert.assertTrue(k.Input("CCF_CAR_PolicyExcess", (String)map_data.get("CAR_PolicyExcess")),"Unable to enter value in SAR_AdditionalInfo . ");
		customAssert.assertTrue(k.Input("CCF_CAR_PolicyExcess_AnyOtherLoss", Keys.chord(Keys.CONTROL, "a")),"Unable to select SAR_AdditionalInfo field");
		customAssert.assertTrue(k.Input("CCF_CAR_PolicyExcess_AnyOtherLoss", (String)map_data.get("CAR_PolicyExcess_AnyOtherLoss")),"Unable to enter value in SAR_AdditionalInfo . ");
		customAssert.assertTrue(k.Input("CCF_CAR_MinDepositPremium", Keys.chord(Keys.CONTROL, "a")),"Unable to select SAR_AdditionalInfo field");
		customAssert.assertTrue(k.Input("CCF_CAR_MinDepositPremium", (String)map_data.get("CAR_MinDepositPremium")),"Unable to enter value in SAR_AdditionalInfo . ");
		
				
		//Inner Add Item
		customAssert.assertTrue(addCARItems(map_data), "Error while adding CAR Item . ");
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
		
		double sPremiumm = CommonFunction.func_CAR_HandleTables( map_data, "CAR", "Contractors All Risks", "CAR AddItem");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_ContractorsAllRisks_NP", String.valueOf(sPremiumm), map_data);
		
		TestUtil.reportStatus("Contractors All Risks details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean addCARItems(Map<Object, Object> map_data){
	boolean r_value=true;
	
	try{
		int total_count_CAR_items = 0;
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
		
		String[] properties = ((String)map_data.get("SAR_AddItem")).split(";");
		total_count_CAR_items = properties.length;
		
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	        	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	    }
		
		int count=0;
		while(count < total_count_CAR_items){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on CAR page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", internal_data_map.get("CAR AddItem").get(count).get("AD_CAR_ItemDesc")),"Unable to enter value in Description field. ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", internal_data_map.get("CAR AddItem").get(count).get("AD_CAR_SumInsured")),"Unable to enter value in Sum Insured field. ");
			
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Contractors All Risks inner page .");
			count++;
		}
		
	}catch(Throwable t){
		
		r_value=false;
	}
	
	return r_value;
	
}

public boolean funcComputersandElectronicRisks(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Computers and Electronic Risks", ""),"Computers and Electronic Risks page navigations issue(S)");
		
		customAssert.assertTrue(k.Input("CCF_CER_Erisk_VirusHacking", (String)map_data.get("CER_Erisk_VirusHacking")),"Unable to enter value in E Risk: Virus & Hacking . ");
		customAssert.assertTrue(k.DropDownSelection("CCF_CER_MaxIndemnityPeriod", (String)map_data.get("CER_MaxIndemnityPeriod")),"Unable to enter value in Maximum Indemnity Period (months) field. ");
		customAssert.assertTrue(k.Input("CCF_CER_Excess", (String)map_data.get("CER_Excess")),"Unable to enter value in Excess . ");
	
		customAssert.assertTrue(k.Input("CCF_CER_Computers_SumInsured_tbl", (String)map_data.get("CER_Computers_SumInsured")),"Unable to enter value in Computers: Sum Insured . ");
		customAssert.assertTrue(k.Input("CCF_CER_AdditionalExp_SumInsured_tbl", (String)map_data.get("CER_AdditionalExp_SumInsured")),"Unable to enter value in Additional Expenditure: Sum Insured . ");
			
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Computers and Electronic Risks .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "CER", "Computers and Electronic Risks", "");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_ComputersandElectronicRisks_NP", String.valueOf(sPremiumm), map_data);
		
		TestUtil.reportStatus("Computers and Electronic Risks details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean funcMoney(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Money", ""),"Money page navigations issue(S)");
	
		customAssert.assertTrue(k.Input("CCF_M_Field_1", (String)map_data.get("M_Field_1")),"Unable to enter value in Money field 1 . ");
		customAssert.assertTrue(k.Input("CCF_M_Field_2", (String)map_data.get("M_Field_2")),"Unable to enter value in Money field 2 . ");
		customAssert.assertTrue(k.Input("CCF_M_Field_3", (String)map_data.get("M_Field_3")),"Unable to enter value in Money field 3 . ");
		customAssert.assertTrue(k.Input("CCF_M_Field_4", (String)map_data.get("M_Field_4")),"Unable to enter value in Money field 4 . ");
		
		//Bodily Injury
		customAssert.assertTrue(k.Input("CCF_M_BodilyInjury_Death", (String)map_data.get("M_BodilyInjury_Death")),"Unable to enter value in Money BodilyInjury_Death . ");
		customAssert.assertTrue(k.Input("CCF_M_LossOfLimbs", (String)map_data.get("M_LossOfLimbs")),"Unable to enter value in Money LossOfLimbs . ");
		customAssert.assertTrue(k.Input("CCF_M_LossOfSight", (String)map_data.get("M_LossOfSight")),"Unable to enter value in Money LossOfSight . ");
		customAssert.assertTrue(k.Input("CCF_M_PermanentTotalDis", (String)map_data.get("M_PermanentTotalDis")),"Unable to enter value in Money PermanentTotalDis . ");
		customAssert.assertTrue(k.Input("CCF_M_TempTotalDisablement", (String)map_data.get("M_TempTotalDisablement")),"Unable to enter value in Money TempTotalDisablement . ");
		
		//Additional Information
		customAssert.assertTrue(k.Input("CCF_M_AdditionalInfo", (String)map_data.get("M_AdditionalInfo")),"Unable to enter value in Money AdditionalInfo . ");
		
		customAssert.assertTrue(k.Input("CCF_M_Excess", (String)map_data.get("M_Excess")),"Unable to enter value in Excess . ");
	
		//Inner Add Item
		customAssert.assertTrue(addSafe_Money(map_data), "Error while adding Money Safe Item . ");
		
		double sPremiumm = CommonFunction.func_GIT_HandleTables( map_data, "M", "Money", "");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_Money_NP", String.valueOf(sPremiumm), map_data);
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Money .");
		
		TestUtil.reportStatus("Money details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean addSafe_Money(Map<Object, Object> map_data){
	boolean r_value=true;
	
	try{
		int total_count_Safe_items = 0;
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
		
		String[] properties = ((String)map_data.get("M_AddSafe")).split(";");
		total_count_Safe_items = properties.length;
		
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	        	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	    }
		
		int count=0;
		while(count < total_count_Safe_items){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddSafe"), "Unable to click on Add Safe Button on Money page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_M_AS_MakeModelOfSafe", internal_data_map.get("M AddSafe").get(count).get("M_AS_MakeModelOfSafe")),"Unable to enter value in Make/Model of Safe field. ");
			customAssert.assertTrue(k.Input("CCF_M_AS_SafeLimit", internal_data_map.get("M AddSafe").get(count).get("M_AS_SafeLimit")),"Unable to enter value in Safe Limit field. ");
			
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Money-Add Safe inner page .");
			count++;
		}
		
	}catch(Throwable t){
		
		r_value=false;
	}
	
	return r_value;
	
}

public boolean funcGoodsInTransit(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Goods In Transit", ""),"Goods In Transit page navigations issue(S)");
	
		//Own Vehicles
		customAssert.assertTrue(k.Input("CCF_GIT_MaxNoOfVehicles", (String)map_data.get("GIT_MaxNoOfVehicles")),"Unable to enter value in MaxNoOfVehicles  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_GIT_Q1", (String)map_data.get("GIT_Q1")), "Unable to Select GIT_Q1 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_GIT_Q2", (String)map_data.get("GIT_Q2")), "Unable to Select GIT_Q2 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_GIT_Q3", (String)map_data.get("GIT_Q3")), "Unable to Select GIT_Q3 radio button .");
		if(((String)map_data.get("GIT_Q3")).equals("Yes")){
			customAssert.assertTrue(k.SelectRadioBtn("CCF_GIT_Q3_SubQ1", (String)map_data.get("GIT_Q3_SubQ1")), "Unable to Select GIT_Q3_SubQ1 radio button .");
		}
		
		customAssert.assertTrue(k.Input("CCF_GIT_DetailsOfAny_ID_Locks", (String)map_data.get("GIT_DetailsOfAny_ID_Locks")),"Unable to enter value in DetailsOfAny_ID_Locks . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_GIT_Q4", (String)map_data.get("GIT_Q4")), "Unable to Select GIT_Q3 radio button .");
		
		//Limit of Liability for Conveyance
		customAssert.assertTrue(k.Input("CCF_GIT_AnyOnePostalPackage", (String)map_data.get("GIT_AnyOnePostalPackage")),"Unable to enter value in AnyOnePostalPackage . ");
		customAssert.assertTrue(k.Input("CCF_GIT_AnyOneConsignment", (String)map_data.get("GIT_AnyOneConsignment")),"Unable to enter value in AnyOneConsignment . ");
		customAssert.assertTrue(k.Input("CCF_GIT_AnyOneLoss", (String)map_data.get("GIT_AnyOneLoss")),"Unable to enter value in AnyOneLoss . ");
		
		//Schedule of Vehicles  
		//Inner 
		customAssert.assertTrue(addSpecifiedVehicles_GIT(map_data), "Error while adding Specified Vehicles . ");
		
		//Additional Information
		customAssert.assertTrue(k.Input("CCF_GIT_AdditionalInformation", (String)map_data.get("GIT_AdditionalInformation")),"Unable to enter value in GIT_AdditionalInformation . ");
		customAssert.assertTrue(k.Input("CCF_GIT_Excess", (String)map_data.get("GIT_Excess")),"Unable to enter value in GIT_Excess . ");
			
				
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Goods In Transit .");
		
		double sPremiumm = CommonFunction.func_GIT_HandleTables( map_data, "GIT", "Goods In Transit", "");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_GoodsInTransit_NP", String.valueOf(sPremiumm), map_data);
		
		TestUtil.reportStatus("Goods In Transit details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean addSpecifiedVehicles_GIT(Map<Object, Object> map_data){
	boolean r_value=true;
	
	try{
		int total_count_Spe_Vehicle = 0;
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
		
		String[] properties = ((String)map_data.get("GIT_AddSpecifiedVehicle")).split(";");
		total_count_Spe_Vehicle = properties.length;
		
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	        	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	    }
		
		int count=0;
		while(count < total_count_Spe_Vehicle){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddSpecifiedVehicle"), "Unable to click on Add AddSpecifiedVehicle Button on GIT page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_GIT_ASV_VehicleDesc", internal_data_map.get("GIT AddSpecificVehicle").get(count).get("GIT_ASV_VehicleDesc")),"Unable to enter value in Vehicle Description field. ");
			customAssert.assertTrue(k.Input("CCF_GIT_LOL_PerVehicle", internal_data_map.get("GIT AddSpecificVehicle").get(count).get("GIT_LOL_PerVehicle")),"Unable to enter value in Limit of Liability per Vehicle field. ");
			customAssert.assertTrue(k.Input("CCF_GIT_LOL_PerTrailer", internal_data_map.get("GIT AddSpecificVehicle").get(count).get("GIT_LOL_PerTrailer")),"Unable to enter value in Limit of Liability per Trailer field. ");
			
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on GIT-Add Specified Vehicle inner page .");
			count++;
		}
		
	}catch(Throwable t){
		
		r_value=false;
	}
	
	return r_value;
	
}

/*public boolean funcMarineCargo(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Marine Cargo", ""),"Marine Cargo page navigations issue(S)");
	
		//The Goods
		customAssert.assertTrue(k.Input("CCF_MC_DescOfGoods", Keys.chord(Keys.CONTROL, "a")),"Unable to select Description of The Goods ");
		customAssert.assertTrue(k.Input("CCF_MC_DescOfGoods", (String)map_data.get("MC_DescOfGoods")),"Unable to enter value in Description of The Goods  . ");
		
		//Basis of Valuation
		customAssert.assertTrue(k.Input("CCF_MC_ImportsExports", Keys.chord(Keys.CONTROL, "a")),"Unable to select ImportsExports ");
		customAssert.assertTrue(k.Input("CCF_MC_ImportsExports", (String)map_data.get("MC_ImportsExports")),"Unable to enter value in ImportsExports  . ");
		customAssert.assertTrue(k.Input("CCF_MC_InlandTransits_UK", Keys.chord(Keys.CONTROL, "a")),"Unable to select InlandTransits_UK ");
		customAssert.assertTrue(k.Input("CCF_MC_InlandTransits_UK", (String)map_data.get("MC_InlandTransits_UK")),"Unable to enter value in InlandTransits_UK  . ");
		customAssert.assertTrue(k.Input("CCF_MC_FOB_CFR", Keys.chord(Keys.CONTROL, "a")),"Unable to select FOB and CFR and similar terms of sale ");
		customAssert.assertTrue(k.Input("CCF_MC_FOB_CFR", (String)map_data.get("MC_FOB_CFR")),"Unable to enter value in FOB and CFR and similar terms of sale  . ");
		customAssert.assertTrue(k.Input("CCF_MC_Exhibition_Representative", Keys.chord(Keys.CONTROL, "a")),"Unable to select Exhibition_Representative ");
		customAssert.assertTrue(k.Input("CCF_MC_Exhibition_Representative", (String)map_data.get("MC_Exhibition_Representative")),"Unable to enter value in Exhibition_Representative  . ");
	
		//Basis of Premium
		customAssert.assertTrue(k.SelectRadioBtn("CCF_MC_BasisOfPremium", (String)map_data.get("MC_BasisOfPremium")), "Unable to Select Basis of Premium radio button .");
		if(((String)map_data.get("MC_BasisOfPremium")).equals("Minimum & Deposit Premium")){
			customAssert.assertTrue(k.Input("CCF_MC_MinDeposit_PremiumPerc", Keys.chord(Keys.CONTROL, "a")),"Unable to select Minimum & Deposit Premium Percentage ");
			customAssert.assertTrue(k.Input("CCF_MC_MinDeposit_PremiumPerc", (String)map_data.get("MC_MinDeposit_PremiumPerc")),"Unable to enter value in MaxNoOfVMinimum & Deposit Premium Percentage  . ");
		}
		
		//Conveyance / Insured Risk
		customAssert.assertTrue(k.Input("CCF_MC_Aircraft_MaximumValue", Keys.chord(Keys.CONTROL, "a")),"Unable to select Aircraft_MaximumValue ");
		customAssert.assertTrue(k.Input("CCF_MC_Aircraft_MaximumValue", (String)map_data.get("MC_Aircraft_MaximumValue")),"Unable to enter value in Aircraft_MaximumValue  . ");
		customAssert.assertTrue(k.Input("CCF_MC_Aircraf_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select Aircraf_Excess ");
		customAssert.assertTrue(k.Input("CCF_MC_Aircraf_Excess", (String)map_data.get("MC_Aircraf_Excess")),"Unable to enter value in Aircraf_Excess  . ");
		customAssert.assertTrue(k.Input("CCF_MC_RoadVehicle_MaxValue", Keys.chord(Keys.CONTROL, "a")),"Unable to select RoadVehicle_MaxValue ");
		customAssert.assertTrue(k.Input("CCF_MC_RoadVehicle_MaxValue", (String)map_data.get("MC_RoadVehicle_MaxValue")),"Unable to enter value in RoadVehicle_MaxValue  . ");
		customAssert.assertTrue(k.Input("CCF_MC_RoadVehicle_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select RoadVehicle_Excess ");
		customAssert.assertTrue(k.Input("CCF_MC_RoadVehicle_Excess", (String)map_data.get("MC_RoadVehicle_Excess")),"Unable to enter value in RoadVehicle_Excess  . ");
		customAssert.assertTrue(k.Input("CCF_MC_VehicleOwned_MaxValue", Keys.chord(Keys.CONTROL, "a")),"Unable to select VehicleOwned_MaxValue ");
		customAssert.assertTrue(k.Input("CCF_MC_VehicleOwned_MaxValue", (String)map_data.get("MC_VehicleOwned_MaxValue")),"Unable to enter value in VehicleOwned_MaxValue  . ");
		customAssert.assertTrue(k.Input("CCF_MC_VehicleOwned_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select VehicleOwned_Excess ");
		customAssert.assertTrue(k.Input("CCF_MC_VehicleOwned_Excess", (String)map_data.get("MC_VehicleOwned_Excess")),"Unable to enter value in VehicleOwned_Excess  . ");
		customAssert.assertTrue(k.Input("CCF_MC_Location_MaxValue", Keys.chord(Keys.CONTROL, "a")),"Unable to select Location_MaxValue ");
		customAssert.assertTrue(k.Input("CCF_MC_Location_MaxValue", (String)map_data.get("MC_Location_MaxValue")),"Unable to enter value in Location_MaxValue  . ");
		customAssert.assertTrue(k.Input("CCF_MC_Location_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select Location_Excess ");
		customAssert.assertTrue(k.Input("CCF_MC_Location_Excess", (String)map_data.get("MC_Location_Excess")),"Unable to enter value in Location_Excess  . ");
		
		//Voyage(s) Insured
		customAssert.assertTrue(k.Input("CCF_MC_Zone_1", Keys.chord(Keys.CONTROL, "a")),"Unable to select MC_Zone_1 ");
		customAssert.assertTrue(k.Input("CCF_MC_Zone_1", (String)map_data.get("MC_Zone_1")),"Unable to enter value in MC_Zone_1  . ");
		customAssert.assertTrue(k.Input("CCF_MC_Zone_2", Keys.chord(Keys.CONTROL, "a")),"Unable to select MC_Zone_2 ");
		customAssert.assertTrue(k.Input("CCF_MC_Zone_2", (String)map_data.get("MC_Zone_2")),"Unable to enter value in MC_Zone_2  . ");
		customAssert.assertTrue(k.Input("CCF_MC_Zone_3", Keys.chord(Keys.CONTROL, "a")),"Unable to select MC_Zone_3 ");
		customAssert.assertTrue(k.Input("CCF_MC_Zone_3", (String)map_data.get("MC_Zone_3")),"Unable to enter value in MC_Zone_3  . ");
		customAssert.assertTrue(k.Input("CCF_MC_Zone_4", Keys.chord(Keys.CONTROL, "a")),"Unable to select MC_Zone_4 ");
		customAssert.assertTrue(k.Input("CCF_MC_Zone_4", (String)map_data.get("MC_Zone_4")),"Unable to enter value in MC_Zone_4  . ");
		customAssert.assertTrue(k.Input("CCF_MC_Zone_5", Keys.chord(Keys.CONTROL, "a")),"Unable to select MC_Zone_5 ");
		customAssert.assertTrue(k.Input("CCF_MC_Zone_5", (String)map_data.get("MC_Zone_5")),"Unable to enter value in MC_Zone_5  . ");
		customAssert.assertTrue(k.Input("CCF_MC_Zone_6", Keys.chord(Keys.CONTROL, "a")),"Unable to select MC_Zone_6 ");
		customAssert.assertTrue(k.Input("CCF_MC_Zone_6", (String)map_data.get("MC_Zone_6")),"Unable to enter value in MC_Zone_6  . ");
		customAssert.assertTrue(k.Input("CCF_MC_Zone_7", Keys.chord(Keys.CONTROL, "a")),"Unable to select MC_Zone_7 ");
		customAssert.assertTrue(k.Input("CCF_MC_Zone_7", (String)map_data.get("MC_Zone_7")),"Unable to enter value in MC_Zone_7  . ");
		customAssert.assertTrue(k.Input("CCF_MC_Zone_8", Keys.chord(Keys.CONTROL, "a")),"Unable to select MC_Zone_8 ");
		customAssert.assertTrue(k.Input("CCF_MC_Zone_8", (String)map_data.get("MC_Zone_8")),"Unable to enter value in MC_Zone_8  . ");
				
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Marine Cargo .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "MC", "Marine Cargo", "");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_MarineCargo_NP", String.valueOf(sPremiumm), map_data);
		
		TestUtil.reportStatus("Marine Cargo details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean funcCyberAndDataSecurity(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Cyber and Data Security", ""),"Cyber and Data Security page navigations issue(S)");
	
		customAssert.assertTrue(k.Input("CCF_CDS_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select Excess ");
		customAssert.assertTrue(k.Input("CCF_CDS_Excess", (String)map_data.get("CDS_Excess")),"Unable to enter value in Excess  . ");
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Cyber and Data Security .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "CDS", "Cyber and Data Security", "");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_CyberandDataSecurity_NP", String.valueOf(sPremiumm), map_data);
		
		TestUtil.reportStatus("Cyber and Data Security details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean funcDirectorsAndOfficers(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Directors and Officers", ""),"Directors and Officers page navigations issue(S)");
	
		
		customAssert.assertTrue(k.Click("CCF_DO_PriorPendingLitigationDate"), "Unable to enter Prior and Pending Litigation Date .");
		customAssert.assertTrue(k.Input("CCF_DO_PriorPendingLitigationDate", (String)map_data.get("DO_PriorPendingLitigationDate")),"Unable to Enter Prior and Pending Litigation Date .");
		customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
		
		//Directors & Officers
		customAssert.assertTrue(k.Input("CCF_DO_Turnover_UK", Keys.chord(Keys.CONTROL, "a")),"Unable to select Turnover_UK ");
		customAssert.assertTrue(k.Input("CCF_DO_Turnover_UK", (String)map_data.get("DO_Turnover_UK")),"Unable to enter value in Turnover_UK  . ");
		customAssert.assertTrue(k.Input("CCF_DO_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select Excess ");
		customAssert.assertTrue(k.Input("CCF_DO_Excess", (String)map_data.get("DO_Excess")),"Unable to enter value in Excess  . ");
		
		//Corporate Liability
		customAssert.assertTrue(k.Input("CCF_DO_CorporateLiabilityTurnover_UK", Keys.chord(Keys.CONTROL, "a")),"Unable to select CorporateLiabilityTurnover_UK ");
		customAssert.assertTrue(k.Input("CCF_DO_CorporateLiabilityTurnover_UK", (String)map_data.get("DO_CorporateLiabilityTurnover_UK")),"Unable to enter value in CorporateLiabilityTurnover_UK  . ");
		customAssert.assertTrue(k.Input("CCF_DO_CorporateLiabilityExcess", Keys.chord(Keys.CONTROL, "a")),"Unable to select CorporateLiabilityExcess ");
		customAssert.assertTrue(k.Input("CCF_DO_CorporateLiabilityExcess", (String)map_data.get("DO_CorporateLiabilityExcess")),"Unable to enter value in CorporateLiabilityExcess  . ");
	
		//Employment Practices Liability
		customAssert.assertTrue(k.Input("CCF_DO_EPL_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select EPL_Excess ");
		customAssert.assertTrue(k.Input("CCF_DO_EPL_Excess", (String)map_data.get("DO_EPL_Excess")),"Unable to enter value in EPL_Excess  . ");
		
		//Statement of Fact
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_1", (String)map_data.get("DO_SOF_Field_1")),"Unable to Select SOF_Field_1 radio button . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_2", (String)map_data.get("DO_SOF_Field_2")),"Unable to Select SOF_Field_2 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_3", (String)map_data.get("DO_SOF_Field_3")),"Unable to Select SOF_Field_3 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_4", (String)map_data.get("DO_SOF_Field_4")),"Unable to Select SOF_Field_4 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_5", (String)map_data.get("DO_SOF_Field_5")),"Unable to Select SOF_Field_5 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_6", (String)map_data.get("DO_SOF_Field_6")),"Unable to Select SOF_Field_6 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_7", (String)map_data.get("DO_SOF_Field_7")),"Unable to Select SOF_Field_7 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_8", (String)map_data.get("DO_SOF_Field_8")),"Unable to Select SOF_Field_8 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_9", (String)map_data.get("DO_SOF_Field_9")),"Unable to Select SOF_Field_9 radio button . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_10", (String)map_data.get("DO_SOF_Field_10")),"Unable to Select SOF_Field_10 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_11", (String)map_data.get("DO_SOF_Field_11")),"Unable to Select SOF_Field_11 radio button . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_12", (String)map_data.get("DO_SOF_Field_12")),"Unable to Select SOF_Field_12 radio button  . ");
				
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Directors and Officers .");
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "DO", "Directors and Officers", "");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_DirectorsandOfficers_NP", String.valueOf(sPremiumm), map_data);
		
		TestUtil.reportStatus("Directors and Officers details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}*/

public boolean funcFrozenFoods(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Frozen Foods", ""),"Frozen Foods page navigations issue(S)");
		
		
		customAssert.assertTrue(k.Input("CCF_FF_Premises", (String)map_data.get("FF_Premises")),"Unable to enter value in Premises  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_FF_MaintenanceContract", (String)map_data.get("FF_MaintenanceContract")),"Unable to Select value from MaintenanceContract radio button . ");
		
		//Additional Information
		customAssert.assertTrue(k.Input("CCF_FF_AdditionalInformation", (String)map_data.get("FF_AdditionalInformation")),"Unable to enter value in AdditionalInformation  . ");
		
		//Excesses
		customAssert.assertTrue(k.Input("CCF_FF_Excess", (String)map_data.get("FF_Excess")),"Unable to enter value in Excess  . ");
				
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Frozen Foods .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "FF", "Frozen Foods", "");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_FrozenFoods_NP", String.valueOf(sPremiumm), map_data);
		
		TestUtil.reportStatus("Frozen Foods details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}


public boolean funcLossofLicence(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Loss of Licence", ""),"Loss of Licence page navigations issue(S)");
	
		customAssert.assertTrue(k.Input("CCF_LOL_Premises", (String)map_data.get("LOL_Premises")),"Unable to enter value in Premises  . ");
		customAssert.assertTrue(k.Input("CCF_LOL_DesignatedPremisesSupervisor", (String)map_data.get("LOL_DesignatedPremisesSupervisor")),"Unable to enter value in DesignatedPremisesSupervisor  . ");
		customAssert.assertTrue(k.Input("CCF_LOL_LicencePeriod", (String)map_data.get("LOL_LicencePeriod")),"Unable to enter value in LicencePeriod  . ");
		customAssert.assertTrue(k.Click("CCF_LOL_LicenceRenewalDate"), "Unable to enter LicenceRenewalDate .");
		customAssert.assertTrue(k.Input("CCF_LOL_LicenceRenewalDate", (String)map_data.get("LOL_LicenceRenewalDate")),"Unable to Enter LicenceRenewalDate .");
		customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
		k.waitTwoSeconds();
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LOL_Ques1", (String)map_data.get("LOL_Ques1")),"Unable to enter value in LOL_Ques1  . ");
		
		//Statement of Fact
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LOL_Ques2", (String)map_data.get("LOL_Ques2")),"Unable to enter value in LOL_Ques2  . ");
			
		//Additional Information
		customAssert.assertTrue(k.Input("CCF_LOL_AdditionalInfo", (String)map_data.get("LOL_AdditionalInfo")),"Unable to enter value in AdditionalInformation  . ");
				
		//Excesses
		customAssert.assertTrue(k.Input("CCF_LOL_Excess", (String)map_data.get("LOL_Excess")),"Unable to enter value in Excess  . ");
						
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Loss of Licence .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "LOL", "Loss of Licence", "");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_LossOfLicence_NP", String.valueOf(sPremiumm), map_data);
		
		TestUtil.reportStatus("Loss of Licence details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean funcFidelityGuarantee(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Fidelity Guarantee", ""),"Fidelity Guarantee page navigations issue(S)");
	
		
		//Schedule of Employees
		customAssert.assertTrue(addFidelityGuaranteeItems(map_data),"Error while adding Fidelity Guarantee items .  ");
		
		customAssert.assertTrue(k.Input("CCF_FG_AggLimitOfLiability", (String)map_data.get("FG_AggLimitOfLiability")),"Unable to enter value in AggLimitOfLiability  . ");
		customAssert.assertTrue(k.Input("CCF_FG_MinDepositPremium", (String)map_data.get("FG_MinDepositPremium")),"Unable to enter value in MinDepositPremium  . ");
				
		//Excesses
		customAssert.assertTrue(k.Input("CCF_FG_Excess", (String)map_data.get("FG_Excess")),"Unable to enter value in Excess  . ");
						
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Fidelity Guarantee .");
		
		double sPremiumm = CommonFunction.func_GIT_HandleTables( map_data, "FG", "Fidelity Guarantee", "FG SOE AddItem");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_FidelityGuarantee_NP", String.valueOf(sPremiumm), map_data);
		
		TestUtil.reportStatus("Fidelity Guarantee details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}
public boolean addFidelityGuaranteeItems(Map<Object, Object> map_data){
	boolean r_value=true;
	
	try{
		int total_count_FG_items = 0;
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
		
		String[] properties = ((String)map_data.get("FG_ScheduleofEmployees")).split(";");
		total_count_FG_items = properties.length;
		
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
	        	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	    }
		
		int count=0;
		while(count < total_count_FG_items){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on Fidelity Guarantee page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_AD_SOE_EmployeeDesc", internal_data_map.get("FG SOE AddItem").get(count).get("AD_SOE_EmployeeDesc")),"Unable to enter value in Employee Description field. ");
			customAssert.assertTrue(k.Input("CCF_AD_SOE_LimitOfLiability", internal_data_map.get("FG SOE AddItem").get(count).get("AD_SOE_LimitOfLiability")),"Unable to enter value in LimitOfLiability field. ");
			customAssert.assertTrue(k.Input("CCF_AD_SOE_Wages", internal_data_map.get("FG SOE AddItem").get(count).get("AD_SOE_Wages")),"Unable to enter value in Wages field. ");
		
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Add Item inner page .");
			count++;
			
		}
		
	}catch(Throwable t){
		
		r_value=false;
	}
	
	return r_value;
	
}

public boolean funcTerrorism(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Terrorism", ""),"Terrorism page navigations issue(S)");
		if(!common.currentRunningFlow.equalsIgnoreCase("MTA") && !common.currentRunningFlow.equalsIgnoreCase("Renewal")){
			customAssert.assertTrue(k.SelectRadioBtn("CCF_TER_CedeComm", (String)map_data.get("TER_CedeComm")),"Unable to Select Cede Commission? radio button value . ");
		}
				
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Terrorism .");
		customAssert.assertTrue(CommonFunction.func_Terrorism_HandleTables( map_data, "TER"), "Error while validating table . ");
		TestUtil.reportStatus("Terrorism details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

/*@SuppressWarnings("static-access")
public boolean funcLegalExpenses(Map<Object, Object> map_data,String code,String event){
	
	boolean r_value= true;
	String exp_AnnualCarrierPremium=null;
	String act_AnnualCarrierPremium=null;
	String testName = (String)map_data.get("Automation Key");
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Legal Expenses", ""),"Legal Expenses page navigations issue(S)");
	
		customAssert.assertTrue(k.DropDownSelection("CCF_LE_LimitOfLiability", (String)map_data.get("LE_LimitOfLiability")), "Unable to select value from LE_LimitOfLiability dropdown .");
		customAssert.assertTrue(k.Input("CCF_LE_Turnover", Keys.chord(Keys.CONTROL, "a")),"Unable to select Turnover ");
		customAssert.assertTrue(k.Input("CCF_LE_Turnover", (String)map_data.get("LE_Turnover")),"Unable to enter value in Turnover  . ");
		customAssert.assertTrue(k.Input("CCF_LE_Wages", Keys.chord(Keys.CONTROL, "a")),"Unable to select Wages ");
		customAssert.assertTrue(k.Input("CCF_LE_Wages", (String)map_data.get("LE_Wages")),"Unable to enter value in Wages  . ");
		customAssert.assertTrue(k.Input("CCF_LE_NetPremium", Keys.chord(Keys.CONTROL, "a")),"Unable to select NetPremium ");
		customAssert.assertTrue(k.Input("CCF_LE_NetPremium", (String)map_data.get("LE_NetPremium")),"Unable to enter value in NetPremium  . ");
		int LE_Premium_Decimal_Places = common.countDecimalPlaces(k.getAttribute("CCF_LE_NetPremium", "value"));
		boolean decimalFlag = LE_Premium_Decimal_Places > 2 ?false:true;
		customAssert.assertTrue(decimalFlag , "LE Premium Should contain up to two Decimal Places . ");
		
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques1", (String)map_data.get("LE_Ques1")),"Unable to Select LE_Ques1 radio button . ");
		
		//Statement of Fact
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques1", (String)map_data.get("LE_Ques1")),"Unable to Select LE_Ques1 radio button . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques2", (String)map_data.get("LE_Ques2")),"Unable to Select LE_Ques2 radio button . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques3", (String)map_data.get("LE_Ques3")),"Unable to Select LE_Ques3 radio button . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques4", (String)map_data.get("LE_Ques4")),"Unable to Select LE_Ques4 radio button . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques5", (String)map_data.get("LE_Ques5")),"Unable to Select LE_Ques5 radio button . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques6", (String)map_data.get("LE_Ques6")),"Unable to Select LE_Ques6 radio button . ");
				
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Legal Expenses .");
		
		exp_AnnualCarrierPremium = common.roundedOff(common.getLEAnnualCarrierPremium(common.product,(String)map_data.get("LE_LimitOfLiability"), (String)map_data.get("LE_Turnover"), (String)map_data.get("LE_Wages")));
		act_AnnualCarrierPremium = k.getAttribute("CCF_LE_AnnualCarrierPremium","value");
		customAssert.assertEquals(exp_AnnualCarrierPremium, act_AnnualCarrierPremium,"Annual Carrier Premium (Excludes IPT) is incorrect Exppected: <b>"+exp_AnnualCarrierPremium+"</b> Actual: <b>"+act_AnnualCarrierPremium+" for Legal Expense . ");
		customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Legal Expenses", testName, "LE_AnnualCarrierPremium", exp_AnnualCarrierPremium,common.NB_excel_data_map),"Error while writing data to excel for field >NB_ClientId<");
		
		
		TestUtil.reportStatus("Legal Expenses details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}*/

public boolean funcGeneral(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("General", ""),"General page navigations issue(S)");
	
		
		customAssert.assertTrue(k.Click("CCF_DO_PriorPendingLitigationDate"), "Unable to enter Prior and Pending Litigation Date .");
		customAssert.assertTrue(k.Input("CCF_DO_PriorPendingLitigationDate", (String)map_data.get("DO_PriorPendingLitigationDate")),"Unable to Enter Prior and Pending Litigation Date .");
		customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
		
		//Directors & Officers
		customAssert.assertTrue(k.Input("CCF_DO_Turnover_UK", Keys.chord(Keys.CONTROL, "a")),"Unable to select Turnover_UK ");
		customAssert.assertTrue(k.Input("CCF_DO_Turnover_UK", (String)map_data.get("DO_Turnover_UK")),"Unable to enter value in Turnover_UK  . ");
		customAssert.assertTrue(k.Input("CCF_DO_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select Excess ");
		customAssert.assertTrue(k.Input("CCF_DO_Excess", (String)map_data.get("DO_Excess")),"Unable to enter value in Excess  . ");
		
		//Corporate Liability
		customAssert.assertTrue(k.Input("CCF_DO_CorporateLiabilityTurnover_UK", Keys.chord(Keys.CONTROL, "a")),"Unable to select CorporateLiabilityTurnover_UK ");
		customAssert.assertTrue(k.Input("CCF_DO_CorporateLiabilityTurnover_UK", (String)map_data.get("DO_CorporateLiabilityTurnover_UK")),"Unable to enter value in CorporateLiabilityTurnover_UK  . ");
		customAssert.assertTrue(k.Input("CCF_DO_CorporateLiabilityExcess", Keys.chord(Keys.CONTROL, "a")),"Unable to select CorporateLiabilityExcess ");
		customAssert.assertTrue(k.Input("CCF_DO_CorporateLiabilityExcess", (String)map_data.get("DO_CorporateLiabilityExcess")),"Unable to enter value in CorporateLiabilityExcess  . ");
	
		//Employment Practices Liability
		customAssert.assertTrue(k.Input("CCF_DO_EPL_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select EPL_Excess ");
		customAssert.assertTrue(k.Input("CCF_DO_EPL_Excess", (String)map_data.get("DO_EPL_Excess")),"Unable to enter value in EPL_Excess  . ");
		
		//Statement of Fact
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_1", (String)map_data.get("DO_SOF_Field_1")),"Unable to Select SOF_Field_1 radio button . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_2", (String)map_data.get("DO_SOF_Field_2")),"Unable to Select SOF_Field_2 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_3", (String)map_data.get("DO_SOF_Field_3")),"Unable to Select SOF_Field_3 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_4", (String)map_data.get("DO_SOF_Field_4")),"Unable to Select SOF_Field_4 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_5", (String)map_data.get("DO_SOF_Field_5")),"Unable to Select SOF_Field_5 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_6", (String)map_data.get("DO_SOF_Field_6")),"Unable to Select SOF_Field_6 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_7", (String)map_data.get("DO_SOF_Field_7")),"Unable to Select SOF_Field_7 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_8", (String)map_data.get("DO_SOF_Field_8")),"Unable to Select SOF_Field_8 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_9", (String)map_data.get("DO_SOF_Field_9")),"Unable to Select SOF_Field_9 radio button . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_10", (String)map_data.get("DO_SOF_Field_10")),"Unable to Select SOF_Field_10 radio button  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_11", (String)map_data.get("DO_SOF_Field_11")),"Unable to Select SOF_Field_11 radio button . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_DO_SOF_Field_13", (String)map_data.get("DO_SOF_Field_12")),"Unable to Select SOF_Field_12 radio button  . ");
				
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on General .");
		
		TestUtil.reportStatus("General details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean funcRewindOperation(){
	
	boolean r_value= true;
	common_CCI.isNBRewindStarted=true;
	
	try{
		
		if(((String)common.NB_excel_data_map.get("CD_Add_Remove_Cover")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcRewindCoversCheck(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
			if(((String)common.NB_excel_data_map.get("CD_Add_MaterialDamage")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(funcInsuredProperties(common.NB_excel_data_map), "Insured Property function is having issue(S) . ");
			}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_BusinessInterruption")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_BusinessInterruption")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
				customAssert.assertTrue(funcBusinessInterruption(common.NB_excel_data_map), "Business Interruption function is having issue(S) . ");
			}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_Liability")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Liability")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(funcEmployersLiability(common.NB_excel_data_map), "Employers Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
				customAssert.assertTrue(funcELDInformation(common.NB_excel_data_map), "ELD Information function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
				customAssert.assertTrue(funcPublicLiability(common.NB_excel_data_map), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(funcProductsLiability(common.NB_excel_data_map), "Products Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
				customAssert.assertTrue(funcLiabilityInformation(common.NB_excel_data_map), "Liability Information function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_SpecifiedAllRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_SpecifiedAllRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcSpecifiedAllRisks(common.NB_excel_data_map), "Specified All Risks function is having issue(S) . ");
				}
			
			/*if(((String)common.NB_excel_data_map.get("CD_Add_ContractorsAllRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_ContractorsAllRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcContractorsAllRisks(common.NB_excel_data_map), "Contractors All Risks function is having issue(S) . ");
				}*/
			
			if(((String)common.NB_excel_data_map.get("CD_Add_ComputersandElectronicRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
				customAssert.assertTrue(funcComputersandElectronicRisks(common.NB_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
				}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_Money")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Money")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
				customAssert.assertTrue(funcMoney(common.NB_excel_data_map), "Money function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_GoodsInTransit")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_GoodsInTransit")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
				customAssert.assertTrue(funcGoodsInTransit(common.NB_excel_data_map), "Goods In Transit function is having issue(S) . ");
				}
			/*if(((String)common.NB_excel_data_map.get("CD_Add_MarineCargo")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_MarineCargo")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Marine Cargo"),"Issue while Navigating to Marine Cargo screen . ");
				customAssert.assertTrue(funcMarineCargo(common.NB_excel_data_map), "Marine Cargo function is having issue(S) . ");
				}*/
			/*if(((String)common.NB_excel_data_map.get("CD_Add_CyberandDataSecurity")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_CyberandDataSecurity")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
				customAssert.assertTrue(funcCyberAndDataSecurity(common.NB_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
				}*/
			/*if(((String)common.NB_excel_data_map.get("CD_Add_DirectorsandOfficers")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_DirectorsandOfficers")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Directors and Officers"),"Issue while Navigating to Directors and Officers screen . ");
				customAssert.assertTrue(funcDirectorsAndOfficers(common.NB_excel_data_map), "Directors and Officers function is having issue(S) . ");
				}*/
			if(((String)common.NB_excel_data_map.get("CD_Add_FrozenFood")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_FrozenFood")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Frozen Foods"),"Issue while Navigating to Frozen Foods screen . ");
				customAssert.assertTrue(funcFrozenFoods(common.NB_excel_data_map), "Frozen Foods function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_LossofLicence")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_LossofLicence")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Loss of Licence"),"Issue while Navigating to Loss of Licence screen . ");
			customAssert.assertTrue(funcLossofLicence(common.NB_excel_data_map), "Loss of Licence function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_FidelityGuarantee")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_FidelityGuarantee")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
				customAssert.assertTrue(funcFidelityGuarantee(common.NB_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_Terrorism")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
				}
			
			/*if(((String)common.NB_excel_data_map.get("CD_Add_LegalExpenses")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_LegalExpenses")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
				customAssert.assertTrue(funcLegalExpenses(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Legal Expenses function is having issue(S) . ");
				}*/
			
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
				Assert.assertTrue(common.funcPremiumSummary(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"RewindAddCover"));
				customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
				customAssert.assertTrue(common.funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
		}
		
		/*if(((String)common.NB_excel_data_map.get("CD_RemoveCover")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcRewindCoversUnCheck(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			Assert.assertTrue(common.funcPremiumSummary(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"RewindRemoveCover"));
			customAssert.assertTrue(common.insuranceTaxVerification());
		}
		
		customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
		customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		customAssert.assertTrue(common.funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
		*///customAssert.assertTrue(common.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) . ");
		
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
}
public boolean funcRewindOperationMTA(){
	
	boolean r_value= true;
	
	try{
		common_CCI.isMTARewindStarted=true;
		if(((String)common.MTA_excel_data_map.get("CD_Add_Remove_CoverMTA")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcRewindCoversCheck(common.MTA_excel_data_map), "Select covers function is having issue(S) . ");
			if(((String)common.MTA_excel_data_map.get("CD_Add_MaterialDamage")).equals("Yes")&&
					((String)common.MTA_excel_data_map.get("CD_MaterialDamage")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(funcInsuredProperties(common.MTA_excel_data_map), "Insured Property function is having issue(S) . ");
			}
			
			if(((String)common.MTA_excel_data_map.get("CD_Add_BusinessInterruption")).equals("Yes")&&
					((String)common.MTA_excel_data_map.get("CD_BusinessInterruption")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
				customAssert.assertTrue(funcBusinessInterruption(common.MTA_excel_data_map), "Business Interruption function is having issue(S) . ");
			}
			
			if(((String)common.MTA_excel_data_map.get("CD_Add_Liability")).equals("Yes")&&
					((String)common.MTA_excel_data_map.get("CD_Liability")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(funcEmployersLiability(common.NB_excel_data_map), "Employers Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
				customAssert.assertTrue(funcPublicLiability(common.NB_excel_data_map), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(funcProductsLiability(common.NB_excel_data_map), "Products Liability function is having issue(S) . ");
					}
			if(((String)common.MTA_excel_data_map.get("CD_Add_SpecifiedAllRisks")).equals("Yes")&&
					((String)common.MTA_excel_data_map.get("CD_SpecifiedAllRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcSpecifiedAllRisks(common.NB_excel_data_map), "Specified All Risks function is having issue(S) . ");
				}
			
			if(((String)common.MTA_excel_data_map.get("CD_Add_ComputersandElectronicRisks")).equals("Yes")&&
					((String)common.MTA_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
				customAssert.assertTrue(funcComputersandElectronicRisks(common.MTA_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
				}
			
			if(((String)common.MTA_excel_data_map.get("CD_Add_Money")).equals("Yes")&&
					((String)common.MTA_excel_data_map.get("CD_Money")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
				customAssert.assertTrue(funcMoney(common.MTA_excel_data_map), "Money function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_GoodsInTransit")).equals("Yes")&&
					((String)common.MTA_excel_data_map.get("CD_GoodsInTransit")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
				customAssert.assertTrue(funcGoodsInTransit(common.MTA_excel_data_map), "Goods In Transit function is having issue(S) . ");
				}
			
			if(((String)common.MTA_excel_data_map.get("CD_Add_FrozenFood")).equals("Yes")&&
					((String)common.MTA_excel_data_map.get("CD_FrozenFood")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Frozen Foods"),"Issue while Navigating to Frozen Foods screen . ");
				customAssert.assertTrue(funcFrozenFoods(common.MTA_excel_data_map), "Frozen Foods function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_LossofLicence")).equals("Yes")&&
					((String)common.MTA_excel_data_map.get("CD_LossofLicence")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Loss of Licence"),"Issue while Navigating to Loss of Licence screen . ");
			customAssert.assertTrue(funcLossofLicence(common.MTA_excel_data_map), "Loss of Licence function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_FidelityGuarantee")).equals("Yes")&&
					((String)common.MTA_excel_data_map.get("CD_FidelityGuarantee")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
				customAssert.assertTrue(funcFidelityGuarantee(common.MTA_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_Terrorism")).equals("Yes")&&
					((String)common.MTA_excel_data_map.get("CD_Terrorism")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(funcTerrorism(common.MTA_excel_data_map), "Terrorism function is having issue(S) . ");
				}
			
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
				Assert.assertTrue(common_CCI.funcPremiumSummary(common.MTA_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"RewindAddCover"));
				customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Endorsement On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
				}else{
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
				}
		}
		
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
}
public boolean funcMTAOperation(){
	
	boolean r_value= true;
	
	try{
		
		if(((String)common.MTA_excel_data_map.get("CD_Add_Remove_CoverMTA")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcMTACoversCheck(common.MTA_excel_data_map), "Select covers function is having issue(S) . ");
			if(((String)common.MTA_excel_data_map.get("CD_Add_MaterialDamage")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(funcInsuredProperties(common.MTA_excel_data_map), "Insured Property function is having issue(S) . ");
			}
			
			if(((String)common.MTA_excel_data_map.get("CD_Add_BusinessInterruption")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
				customAssert.assertTrue(funcBusinessInterruption(common.MTA_excel_data_map), "Business Interruption function is having issue(S) . ");
			}
			
			if(((String)common.MTA_excel_data_map.get("CD_Add_Liability")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Liability")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(funcEmployersLiability(common.MTA_excel_data_map), "Employers Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
				customAssert.assertTrue(funcELDInformation(common.MTA_excel_data_map), "ELD Information function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
				customAssert.assertTrue(funcPublicLiability(common.MTA_excel_data_map), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(funcProductsLiability(common.MTA_excel_data_map), "Products Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
				customAssert.assertTrue(funcLiabilityInformation(common.MTA_excel_data_map), "Liability Information function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_SpecifiedAllRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_SpecifiedAllRisks")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcSpecifiedAllRisks(common.MTA_excel_data_map), "Specified All Risks function is having issue(S) . ");
				}
			
			if(((String)common.MTA_excel_data_map.get("CD_Add_ContractorsAllRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_ContractorsAllRisks")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcContractorsAllRisks(common.MTA_excel_data_map), "Contractors All Risks function is having issue(S) . ");
				}
			
			if(((String)common.MTA_excel_data_map.get("CD_Add_ComputersandElectronicRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
				customAssert.assertTrue(funcComputersandElectronicRisks(common.MTA_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
				}
			
			if(((String)common.MTA_excel_data_map.get("CD_Add_Money")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Money")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
				customAssert.assertTrue(funcMoney(common.MTA_excel_data_map), "Money function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_GoodsInTransit")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_GoodsInTransit")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
				customAssert.assertTrue(funcGoodsInTransit(common.MTA_excel_data_map), "Goods In Transit function is having issue(S) . ");
				}
			/*if(((String)common.MTA_excel_data_map.get("CD_Add_MarineCargo")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_MarineCargo")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Marine Cargo"),"Issue while Navigating to Marine Cargo screen . ");
				customAssert.assertTrue(funcMarineCargo(common.MTA_excel_data_map), "Marine Cargo function is having issue(S) . ");
				}*/
			/*if(((String)common.MTA_excel_data_map.get("CD_Add_CyberandDataSecurity")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_CyberandDataSecurity")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
				customAssert.assertTrue(funcCyberAndDataSecurity(common.MTA_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
				}*/
			/*if(((String)common.MTA_excel_data_map.get("CD_Add_DirectorsandOfficers")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_DirectorsandOfficers")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Directors and Officers"),"Issue while Navigating to Directors and Officers screen . ");
				customAssert.assertTrue(funcDirectorsAndOfficers(common.MTA_excel_data_map), "Directors and Officers function is having issue(S) . ");
				}*/
			if(((String)common.MTA_excel_data_map.get("CD_Add_FrozenFood")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_FrozenFood")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Frozen Foods"),"Issue while Navigating to Frozen Foods screen . ");
				customAssert.assertTrue(funcFrozenFoods(common.MTA_excel_data_map), "Frozen Foods function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_LossofLicence")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_LossofLicence")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Loss of Licence"),"Issue while Navigating to Loss of Licence screen . ");
			customAssert.assertTrue(funcLossofLicence(common.MTA_excel_data_map), "Loss of Licence function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_FidelityGuarantee")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_FidelityGuarantee")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
				customAssert.assertTrue(funcFidelityGuarantee(common.MTA_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_Terrorism")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(funcTerrorism(common.MTA_excel_data_map), "Terrorism function is having issue(S) . ");
				}
			
			/*if(((String)common.MTA_excel_data_map.get("CD_Add_LegalExpenses")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
				customAssert.assertTrue(funcLegalExpenses(common.MTA_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Legal Expenses function is having issue(S) . ");
				}
*/			
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
				Assert.assertTrue(common.funcPremiumSummary(common.MTA_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,""));
				
				
//				customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
//				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
//				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
//				customAssert.assertTrue(common.funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
		}
		if(((String)common.MTA_excel_data_map.get("CD_Change_CoverMTA")).equalsIgnoreCase("Yes")){
			
				if(((String)common.MTA_excel_data_map.get("CD_Change_MaterialDamage")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
					customAssert.assertTrue(funcInsuredProperties(common.MTA_excel_data_map), "Insured Property function is having issue(S) . ");
				}
				
				if(((String)common.MTA_excel_data_map.get("CD_Change_BusinessInterruption")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
					customAssert.assertTrue(funcBusinessInterruption(common.MTA_excel_data_map), "Business Interruption function is having issue(S) . ");
				}
				
				if(((String)common.MTA_excel_data_map.get("CD_Change_Liability")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_Liability")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
					customAssert.assertTrue(funcEmployersLiability(common.MTA_excel_data_map), "Employers Liability function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
					customAssert.assertTrue(funcELDInformation(common.MTA_excel_data_map), "ELD Information function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
					customAssert.assertTrue(funcPublicLiability(common.MTA_excel_data_map), "Public Liability function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
					customAssert.assertTrue(funcProductsLiability(common.MTA_excel_data_map), "Products Liability function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
					customAssert.assertTrue(funcLiabilityInformation(common.MTA_excel_data_map), "Liability Information function is having issue(S) . ");
					}
				if(((String)common.MTA_excel_data_map.get("CD_Change_SpecifiedAllRisks")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_SpecifiedAllRisks")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
					customAssert.assertTrue(funcSpecifiedAllRisks(common.MTA_excel_data_map), "Specified All Risks function is having issue(S) . ");
					}
				
				if(((String)common.MTA_excel_data_map.get("CD_Change_ContractorsAllRisks")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_ContractorsAllRisks")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
					customAssert.assertTrue(funcContractorsAllRisks(common.MTA_excel_data_map), "Contractors All Risks function is having issue(S) . ");
					}
				
				if(((String)common.MTA_excel_data_map.get("CD_Change_ComputersandElectronicRisks")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
					customAssert.assertTrue(funcComputersandElectronicRisks(common.MTA_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
					}
				
				if(((String)common.MTA_excel_data_map.get("CD_Change_Money")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_Money")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
					customAssert.assertTrue(funcMoney(common.MTA_excel_data_map), "Money function is having issue(S) . ");
					}
				if(((String)common.MTA_excel_data_map.get("CD_Change_GoodsInTransit")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_GoodsInTransit")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
					customAssert.assertTrue(funcGoodsInTransit(common.MTA_excel_data_map), "Goods In Transit function is having issue(S) . ");
					}
				/*if(((String)common.MTA_excel_data_map.get("CD_Change_MarineCargo")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_MarineCargo")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Marine Cargo"),"Issue while Navigating to Marine Cargo screen . ");
					customAssert.assertTrue(funcMarineCargo(common.MTA_excel_data_map), "Marine Cargo function is having issue(S) . ");
					}*/
				/*if(((String)common.MTA_excel_data_map.get("CD_Change_CyberandDataSecurity")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_CyberandDataSecurity")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
					customAssert.assertTrue(funcCyberAndDataSecurity(common.MTA_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
					}*/
				/*if(((String)common.MTA_excel_data_map.get("CD_Change_DirectorsandOfficers")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_DirectorsandOfficers")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Directors and Officers"),"Issue while Navigating to Directors and Officers screen . ");
					customAssert.assertTrue(funcDirectorsAndOfficers(common.MTA_excel_data_map), "Directors and Officers function is having issue(S) . ");
					}*/
				if(((String)common.MTA_excel_data_map.get("CD_Change_FrozenFood")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_FrozenFood")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Frozen Foods"),"Issue while Navigating to Frozen Foods screen . ");
					customAssert.assertTrue(funcFrozenFoods(common.MTA_excel_data_map), "Frozen Foods function is having issue(S) . ");
					}
				if(((String)common.MTA_excel_data_map.get("CD_Change_LossofLicence")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_LossofLicence")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Loss of Licence"),"Issue while Navigating to Loss of Licence screen . ");
				customAssert.assertTrue(funcLossofLicence(common.MTA_excel_data_map), "Loss of Licence function is having issue(S) . ");
					}
				if(((String)common.MTA_excel_data_map.get("CD_Change_FidelityGuarantee")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_FidelityGuarantee")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
					customAssert.assertTrue(funcFidelityGuarantee(common.MTA_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
					}
				if(((String)common.MTA_excel_data_map.get("CD_Change_Terrorism")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
					customAssert.assertTrue(funcTerrorism(common.MTA_excel_data_map), "Terrorism function is having issue(S) . ");
					}
				
				/*if(((String)common.MTA_excel_data_map.get("CD_Change_LegalExpenses")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
					customAssert.assertTrue(funcLegalExpenses(common.MTA_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Legal Expenses function is having issue(S) . ");
					}*/
				
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
					Assert.assertTrue(common.funcPremiumSummary(common.MTA_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,""));
		}		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
}

/*public boolean funcPremiumSummary(Map<Object, Object> map_data,String code,String event){
	
	boolean r_value= true;
	Date currentDate = new Date();
	String testName = (String)map_data.get("Automation Key");
	String customPolicyDuration=null;
	SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
	//String inception_date = common.daysIncrementWithOutFormation(df.format(currentDate), 0);
	try{
		customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
		
		int policy_Duration = Integer.parseInt((String)map_data.get("PS_Duration"));
		boolean negativeFlag = policy_Duration < 0 ?false:true;
		customAssert.assertTrue(negativeFlag , "Policy Duration should be non-negative value . ");
		boolean Months18_Flag = policy_Duration < 550 ?true:false;
		customAssert.assertTrue(Months18_Flag , "Policy Duration Should be less than 18 Months ( <= 549 Days) . ");
		policy_Duration--;
		String Policy_End_Date = common.daysIncrementWithOutFormation(df1.format(currentDate), policy_Duration);
		
		
		if(((String)map_data.get("PS_DefaultStartEndDate")).equals("No")){
			customAssert.assertTrue(k.Click("Policy_Start_Date"), "Unable to Click Policy_Start_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_Start_Date", (String)map_data.get("PS_PolicyStartDate")),"Unable to Enter Policy_Start_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
			customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
		}	
		k.waitTwoSeconds();
		customAssert.assertTrue(k.SelectRadioBtn("Payment_Warranty_rules", (String)map_data.get("PS_PaymentWarrantyRules")),"Unable to Select Payment_Warranty_rules radio button . ");
		
		if(((String)map_data.get("PS_PaymentWarrantyRules")).equals("Yes")){
			
			customAssert.assertTrue(k.Click("Payment_Warranty_Due_Date"), "Unable to Click Payment_Warranty_Due_Date date picker .");
			customAssert.assertTrue(k.Input("Payment_Warranty_Due_Date", (String)map_data.get("PS_PaymentWarrantyDueDate")),"Unable to Enter Payment_Warranty_Due_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
		}
		customAssert.assertTrue(common.funcPremiumSummaryTable(code, event, map_data),"Premium summary Calculation function failed");		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
		customPolicyDuration = k.getText("Policy_Duration");
		customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,common.NB_excel_data_map),"Error while writing Policy Duration data to excel .");
		TestUtil.reportStatus("Policy Duration = "+customPolicyDuration+" Days . ", "Info", true);
		
		TestUtil.reportStatus("Premium Summary details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}*/

public boolean CancellationProcess(Map<Object, Object> map_data,String code,String event) throws Throwable{
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
	//String pol_EndDt=k.getAttribute("cancelStDt", "value");
	int idate= Integer.parseInt(CONFIG.getProperty("DaysIncrementNumber"));
	//customAssert.assertTrue(pol_StartDt.contentEquals(TestUtil.returnDate(idate)),"Policy Start Day is not matching");
	customAssert.assertTrue(k.Input("cancelDt", TestUtil.returnDate(cnDays+idate)));
	customAssert.assertTrue(k.Type("cancelRsn", "Test Cancellation"));
	can_data.put("cancelDate",TestUtil.returnDate(cnDays+idate));
	can_data.put("cancelReason","Test Cancellation");
	customAssert.assertTrue(k.Click("cancelCtnu"));
	k.waitTwoSeconds();
	customAssert.assertTrue(k.isDisplayed("cancelRemDayslbl"),"Unable to move cancel summary page ");
	TestUtil.reportStatus("Cancellation Summary Page loaded Successfully", "Info", true);
	//Assert.assertTrue(common.funcPageNavigation("Cancel Policy", ""),"Unable to load Cancel Policy summary page");

	customAssert.assertTrue(k.getText("cnclSummaryStDt").contentEquals(pol_StartDt),"Policy Start date is not matching on cancellation summary page");
	TestUtil.reportStatus("Cancellation Date is <b>"+k.getText("cancelDate")+ " with remaining days "+k.getText("cancelRemDays"), "Info", true);
	k.pressDownKeyonPage();
	// - html/body/div[3]/form/div/table[3]/tbody/tr[19]/td[2]
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
	
	common.CancellationPremiumTables(code, event, "AP", true);
	common.CancellationPremiumTables(code, event, "CP", true);
	k.Click("cancelRecalc");
	common.CancellationPremiumTables(code, event, "CP", false);
	can_GrandTotal=k.getText("can_GtTotal");
	TestUtil.reportStatus("Cancellation Grand Total after Re-Calculation <b>"+can_GrandTotal, "Info", false);
	// Click on Cancel Policy button
	//common.funcButtonSelection("Cancel Policy");
	k.Click("cancelPol");
	customAssert.assertTrue(k.AcceptPopup(),"Cancel Pop-up handelling failed");
	
//	if (cnDays==0){
//		customAssert.assertTrue(currentAP_Total.contentEquals(canAP_Total),"Current Annual Premium and Cancellation Total are not matched");
//	}else{
//		customAssert.assertFalse(currentAP_Total.contentEquals(canAP_Total),"Current Annual Premium and Cancellation Total are not matched");
//	}
//	

	return true;
}


/**
 * 
 * Renewal Flow : 
 * 
 * 
 */

public void RenewalFlow(String code,String fileName) throws Throwable{
	String testName = (String)common.Renewal_excel_data_map.get("Automation Key");
	
	try {
		
		common.currentRunningFlow="Renewal";
		String navigationBy = CONFIG.getProperty("NavigationBy");
		
		customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common_CCJ.renewalSearchPolicyNEW(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Pending"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		
		if(!common_HHAZ.isAssignedToUW){ 
			customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
			customAssert.assertTrue(common_SPI.funcAssignPolicyToUW());
		}
		
		customAssert.assertTrue(common.funcButtonSelection("Send to Underwriter"));
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
	
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Submitted"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
		
		String agencyName = k.getText("CCF_AgencyName");
		int length = agencyName.length();
		int index = agencyName.indexOf("of");
		String agency = agencyName.substring(index+3, length);
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "QuoteCreation",(String)common.Renewal_excel_data_map.get("Automation Key"), "QC_AgencyName", agency, common.Renewal_excel_data_map);
		
		//This will write Quote Number from screen to excel sheet.
		
		String QuoteNumber = k.getText("POF_QuoteNumber");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Renewal", testName, "Renewal_QuoteNumber", QuoteNumber, common.Renewal_excel_data_map);
		
		customAssert.assertTrue(funcPolicyDetails(common.Renewal_excel_data_map,code,TestBase.businessEvent), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(funcPreviousClaims(common.Renewal_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(common.funcCovers(common.Renewal_excel_data_map), "Select covers function is having issue(S) . ");
		
		if(((String)common.Renewal_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
			customAssert.assertTrue(funcInsuredProperties(common.Renewal_excel_data_map), "Insured Property function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
			customAssert.assertTrue(funcBusinessInterruption(common.Renewal_excel_data_map), "Business Interruption function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_Liability")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
			customAssert.assertTrue(funcEmployersLiability(common.Renewal_excel_data_map), "Employers Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
			customAssert.assertTrue(funcPublicLiability(common.Renewal_excel_data_map), "Public Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
			customAssert.assertTrue(funcProductsLiability(common.Renewal_excel_data_map), "Products Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
			customAssert.assertTrue(funcLiabilityInformation(common.Renewal_excel_data_map), "Liability Information function is having issue(S) . ");
			}
		if(((String)common.Renewal_excel_data_map.get("CD_SpecifiedAllRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
			customAssert.assertTrue(funcSpecifiedAllRisks(common.Renewal_excel_data_map), "Specified All Risks function is having issue(S) . ");
		}
				
		if(((String)common.Renewal_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
			customAssert.assertTrue(funcComputersandElectronicRisks(common.Renewal_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_Money")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
			customAssert.assertTrue(funcMoney(common.Renewal_excel_data_map), "Money function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_GoodsInTransit")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
			customAssert.assertTrue(funcGoodsInTransit(common.Renewal_excel_data_map), "Goods In Transit function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_FrozenFood")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Frozen Foods"),"Issue while Navigating to Frozen Foods screen . ");
			customAssert.assertTrue(funcFrozenFoods(common.Renewal_excel_data_map), "Frozen Foods function is having issue(S) . ");
			}
		if(((String)common.Renewal_excel_data_map.get("CD_LossofLicence")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Loss of Licence"),"Issue while Navigating to Loss of Licence screen . ");
			customAssert.assertTrue(funcLossofLicence(common.Renewal_excel_data_map), "Loss of Licence function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_FidelityGuarantee")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
			customAssert.assertTrue(funcFidelityGuarantee(common.Renewal_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
			customAssert.assertTrue(funcTerrorism(common.Renewal_excel_data_map), "Terrorism function is having issue(S) . ");
		}
	
		
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common_CTB.funcPremiumSummary(common.Renewal_excel_data_map,code,"Renewal"), "Premium Summary function is having issue(S) . ");
		Assert.assertTrue(common_PEN.funcStatusHandling(common.Renewal_excel_data_map,code,TestBase.businessEvent));
		
		TestUtil.reportTestCasePassed(testName);
		
	} catch (Exception e) {
		
	}
	
	TestUtil.reportStatus("Test Method of Renewal For - "+code, "Pass", true);
	
	
}

public boolean funcEndorsmentOnCoverOperation(Map<Object, Object> map_data){
	
	try {
		
		customAssert.assertTrue(funcRenewalOnCoveOperation(map_data));
		customAssert.assertTrue(common.createEndorsement(map_data));
		customAssert.assertTrue(common.funcButtonSelection("Discard Endorsement"));
		customAssert.assertTrue(k.AcceptPopup());
		TestUtil.reportStatus("Endorsment discarded successfully. ", "Info", true);
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		customAssert.assertTrue(common.createEndorsement(map_data));
		customAssert.assertTrue(common_CCF.funcRenewalSubmittedChange(map_data),"Error in Renewal Submitted Change method");
		customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
		customAssert.assertTrue(common.funcAssignUnderWriter((String)map_data.get("Renewal_AssignUnderwritter")));
		customAssert.assertTrue(k.Click("Quote_btn"),"Unable to click on Quote button.");
		customAssert.assertTrue(k.Click("Quote_proceed_btn"),"Unable to click on Proceed button.");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Endorsement Quoted"), "Verify Policy Status (Renewal Quoted) function is having issue(S) . ");
		customAssert.assertTrue(common.funcGoOnCover(map_data), "Go On Cover function is having issue(S) . ");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Endorsement On Cover"), "Verify Policy Status (Renewal On Cover) function is having issue(S) . ");
		customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Documents function is having issue(S) . ");
		customAssert.assertTrue(common.transactionSummary_Endorsment((String)map_data.get("Automation Key"), "", CommonFunction.product, CommonFunction.businessEvent), "Transaction Summary function is having issue(S) . ");
		
		return true;
		
	} catch (Exception e) {
		
		return false;
	}
	
	
}

public boolean funcRenewalRewindOperation(Map<Object, Object> map_data){
	
	try {
		
		customAssert.assertTrue(funcRenewalOnCoveOperation(map_data));
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common.funcRewind());
		if(((String)common.Renewal_excel_data_map.get("CD_Add_Remove_Cover")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcRewindCoversCheck(common.Renewal_excel_data_map), "Select covers function is having issue(S) . ");
			if(((String)common.Renewal_excel_data_map.get("CD_Add_MaterialDamage")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_MaterialDamage")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(funcInsuredProperties(common.NB_excel_data_map), "Insured Property function is having issue(S) . ");
			}
			
			if(((String)common.Renewal_excel_data_map.get("CD_Add_BusinessInterruption")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_BusinessInterruption")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
				customAssert.assertTrue(funcBusinessInterruption(common.Renewal_excel_data_map), "Business Interruption function is having issue(S) . ");
			}
			
			if(((String)common.Renewal_excel_data_map.get("CD_Add_Liability")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_Liability")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(funcEmployersLiability(common.Renewal_excel_data_map), "Employers Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
				customAssert.assertTrue(funcELDInformation(common.Renewal_excel_data_map), "ELD Information function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
				customAssert.assertTrue(funcPublicLiability(common.Renewal_excel_data_map), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(funcProductsLiability(common.Renewal_excel_data_map), "Products Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
				customAssert.assertTrue(funcLiabilityInformation(common.Renewal_excel_data_map), "Liability Information function is having issue(S) . ");
				}
			if(((String)common.Renewal_excel_data_map.get("CD_Add_SpecifiedAllRisks")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_SpecifiedAllRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcSpecifiedAllRisks(common.Renewal_excel_data_map), "Specified All Risks function is having issue(S) . ");
				}
			
			/*if(((String)common.Renewal_excel_data_map.get("CD_Add_ContractorsAllRisks")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_ContractorsAllRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcContractorsAllRisks(common.Renewal_excel_data_map), "Contractors All Risks function is having issue(S) . ");
				}*/
			
			if(((String)common.Renewal_excel_data_map.get("CD_Add_ComputersandElectronicRisks")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
				customAssert.assertTrue(funcComputersandElectronicRisks(common.Renewal_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
				}
			
			if(((String)common.Renewal_excel_data_map.get("CD_Add_Money")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_Money")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
				customAssert.assertTrue(funcMoney(common.Renewal_excel_data_map), "Money function is having issue(S) . ");
				}
			if(((String)common.Renewal_excel_data_map.get("CD_Add_GoodsInTransit")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_GoodsInTransit")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
				customAssert.assertTrue(funcGoodsInTransit(common.Renewal_excel_data_map), "Goods In Transit function is having issue(S) . ");
				}
			/*if(((String)common.Renewal_excel_data_map.get("CD_Add_MarineCargo")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_MarineCargo")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Marine Cargo"),"Issue while Navigating to Marine Cargo screen . ");
				customAssert.assertTrue(funcMarineCargo(common.Renewal_excel_data_map), "Marine Cargo function is having issue(S) . ");
				}*/
			/*if(((String)common.Renewal_excel_data_map.get("CD_Add_CyberandDataSecurity")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_CyberandDataSecurity")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
				customAssert.assertTrue(funcCyberAndDataSecurity(common.Renewal_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
				}*/
			/*if(((String)common.Renewal_excel_data_map.get("CD_Add_DirectorsandOfficers")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_DirectorsandOfficers")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Directors and Officers"),"Issue while Navigating to Directors and Officers screen . ");
				customAssert.assertTrue(funcDirectorsAndOfficers(common.Renewal_excel_data_map), "Directors and Officers function is having issue(S) . ");
				}*/
			if(((String)common.Renewal_excel_data_map.get("CD_Add_FrozenFood")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_FrozenFood")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Frozen Foods"),"Issue while Navigating to Frozen Foods screen . ");
				customAssert.assertTrue(funcFrozenFoods(common.Renewal_excel_data_map), "Frozen Foods function is having issue(S) . ");
				}
			if(((String)common.Renewal_excel_data_map.get("CD_Add_LossofLicence")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_LossofLicence")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Loss of Licence"),"Issue while Navigating to Loss of Licence screen . ");
			customAssert.assertTrue(funcLossofLicence(common.Renewal_excel_data_map), "Loss of Licence function is having issue(S) . ");
				}
			if(((String)common.Renewal_excel_data_map.get("CD_Add_FidelityGuarantee")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_FidelityGuarantee")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
				customAssert.assertTrue(funcFidelityGuarantee(common.Renewal_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
				}
			if(((String)common.Renewal_excel_data_map.get("CD_Add_Terrorism")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_Terrorism")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(funcTerrorism(common.Renewal_excel_data_map), "Terrorism function is having issue(S) . ");
				}
			
		/*	if(((String)common.Renewal_excel_data_map.get("CD_Add_LegalExpenses")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_LegalExpenses")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
				customAssert.assertTrue(funcLegalExpenses(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Legal Expenses function is having issue(S) . ");
				}*/
			
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
				customAssert.assertTrue(common.funcPremiumSummary_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,""), "Premium Summary function is having issue(S) . ");
		}
		customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		customAssert.assertTrue(common.funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
		customAssert.assertTrue(common.transactionSummary((String)map_data.get("Automation Key"), "", CommonFunction.product, CommonFunction.businessEvent), "Transaction Summary function is having issue(S) . ");
		return true;
		
	} catch (Exception e) {
		
		return false;
	}
	
	
}

public boolean funcRenewalOnCoveOperation(Map<Object, Object> map_data){
	
	try {
		
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Pending"), "Verify Policy Status (Renewal Pending) function is having issue(S) . ");
		
		if(TestBase.product.equals("SPI") || TestBase.product.equals("PIA")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
			customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter in renewal.");
		}
		else{
			customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
			customAssert.assertTrue(common.funcAssignUnderWriter((String)map_data.get("Renewal_AssignUnderwritter")));
		}
		customAssert.assertTrue(common.funcButtonSelection("Send to Underwriter"));
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Submitted"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
		
		if(((String)common.Renewal_excel_data_map.get("CD_isRenewalSubmittedChange")).equalsIgnoreCase("Yes")){
			
			switch(TestBase.product){
				case "SPI":
					customAssert.assertTrue(common_SPI.funcRenewalSubmittedChange(),"Error in Renewal Submitted Change method");
				break;
				case "CCF":
					customAssert.assertTrue(common_CCF.funcRenewalSubmittedChange(map_data),"Error in Renewal Submitted Change method");
				break;
				case "PIA":
					customAssert.assertTrue(common_PIA.funcRenewalSubmittedChange(),"Error in PIA Renewal Submitted Change method");
				break;
			}
		}
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(k.Click("Quote_btn"),"Unable to click on Quote button.");
		customAssert.assertTrue(k.Click("Quote_proceed_btn"),"Unable to click on Proceed button.");
		//This will get UK date after putting policy on Quoted status.
		String quoteDate = common.getUKDate();
		common.Renewal_excel_data_map.put("QuoteDate", quoteDate);
		/////////////////////////////////////////////////////////////
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Quoted"), "Verify Policy Status (Renewal Quoted) function is having issue(S) . ");
		customAssert.assertTrue(common.funcPDFdocumentVerification("Draft Documents"), "Draft Documents function is having issue(S) . ");
		customAssert.assertTrue(common.funcGoOnCover(map_data), "Go On Cover function is having issue(S) . ");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		if(TestBase.product.equals("SPI") || TestBase.product.equals("PIA")){
			customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover (Unconfirmed)"), "Verify Policy Status (Renewal On Cover (Unconfirmed)) function is having issue(S) . ");
			customAssert.assertTrue(common_SPI.func_Confirm_Policy(), "Error while changing SPI policy Status from Unconfirmed to Confirmed . ");
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
			customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover"), "Verify Policy Status (Renewal On Cover) function is having issue(S) . ");
		}else{
			customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover"), "Verify Policy Status (Renewal On Cover) function is having issue(S) . ");
		}
	
		customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Documents function is having issue(S) . ");
		customAssert.assertTrue(common.transactionSummary((String)map_data.get("Automation Key"), "", CommonFunction.product, CommonFunction.businessEvent), "Transaction Summary function is having issue(S) . ");
		
		return true;
		
	} catch (Exception e) {
		
		return false;
	}
	
	
	
}

public boolean funcRenewalNTUOperation(Map<Object, Object> map_data){
	
	try {
		
		
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Pending"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
		if(TestBase.product.equals("SPI") || TestBase.product.equals("PIA")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
			customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter in renewal.");
		}
		else{
			customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
			customAssert.assertTrue(common.funcAssignUnderWriter((String)map_data.get("Renewal_AssignUnderwritter")));
		}
		customAssert.assertTrue(common.funcButtonSelection("Send to Underwriter"));
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Submitted"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
		
		if(((String)common.Renewal_excel_data_map.get("CD_isRenewalSubmittedChange")).equalsIgnoreCase("Yes")){
			
			switch(TestBase.product){
				case "SPI":
					customAssert.assertTrue(common_SPI.funcRenewalSubmittedChange(),"Error in Renewal Submitted Change method");
				break;
				case "CCF":
					customAssert.assertTrue(common_CCF.funcRenewalSubmittedChange(map_data),"Error in Renewal Submitted Change method");
				break;
				case "PIA":
					customAssert.assertTrue(common_PIA.funcRenewalSubmittedChange(),"Error in PIA Renewal Submitted Change method");
				break;
			}
		}
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(k.Click("Quote_btn"),"Unable to click on Quote button.");
		customAssert.assertTrue(k.Click("Quote_proceed_btn"),"Unable to click on Proceed button.");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Quoted"), "Verify Policy Status (Renewal Quoted) function is having issue(S) . ");
		customAssert.assertTrue(common.funcPDFdocumentVerification("Draft Documents"), "Draft Documents function is having issue(S) . ");
		customAssert.assertTrue(common.funcNTU(map_data));
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal NTU"), "Verify Policy Status (NTU) function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyNTUstatus(common.Renewal_excel_data_map), "Verify Policy Status (NTU Page) function is having issue(S) . ");
		
		return true;
		
	} catch (Exception e) {
		
		return false;
	}
	
	
}

public boolean funcRenewalDeclinedOperation(Map<Object, Object> map_data){
	
	
	try {
		
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Pending"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
		if(TestBase.product.equals("SPI") || TestBase.product.equals("PIA")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
			customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter in renewal.");
		}
		else{
			customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
			customAssert.assertTrue(common.funcAssignUnderWriter((String)map_data.get("Renewal_AssignUnderwritter")));
		}
		customAssert.assertTrue(common.funcButtonSelection("Send to Underwriter"));
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Submitted"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
		
		if(((String)common.Renewal_excel_data_map.get("CD_isRenewalSubmittedChange")).equalsIgnoreCase("Yes")){
			
			switch(TestBase.product){
				case "SPI":
					customAssert.assertTrue(common_SPI.funcRenewalSubmittedChange(),"Error in Renewal Submitted Change method");
				break;
				case "CCF":
					customAssert.assertTrue(common_CCF.funcRenewalSubmittedChange(map_data),"Error in Renewal Submitted Change method");
				break;
				case "PIA":
					customAssert.assertTrue(common_PIA.funcRenewalSubmittedChange(),"Error in PIA Renewal Submitted Change method");
				break;
			}
		}
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common.funcDecline(common.Renewal_excel_data_map));
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Declined"), "Verify Policy Status (Declined) function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyDeclineNTUstatus(common.Renewal_excel_data_map), "Verify Policy Status (Decline Page) function is having issue(S) . ");
		
		
		return true;
		
	} catch (Exception e) {
		
		return false;
	}
	
}
	

public boolean funcRenewalSubmittedChange(Map<Object, Object> map_data){
	
	try {
		
			//////////////////////////////////////////
					
			/*if(((String)map_data.get("CD_MaterialDamage")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(funcInsuredProperties(map_data), "Insured Property function is having issue(S) . ");
			}
			
			if(((String)map_data.get("CD_BusinessInterruption")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
				customAssert.assertTrue(funcBusinessInterruption(map_data), "Business Interruption function is having issue(S) . ");
			}
			
			if(((String)map_data.get("CD_Liability")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(funcEmployersLiability(map_data), "Employers Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
				customAssert.assertTrue(funcELDInformation(map_data), "ELD Information function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
				customAssert.assertTrue(funcPublicLiability(map_data), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(funcProductsLiability(map_data), "Products Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
				customAssert.assertTrue(funcLiabilityInformation(map_data), "Liability Information function is having issue(S) . ");
			}
			if(((String)map_data.get("CD_SpecifiedAllRisks")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcSpecifiedAllRisks(map_data), "Specified All Risks function is having issue(S) . ");
			}
			
			if(((String)map_data.get("CD_ContractorsAllRisks")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcContractorsAllRisks(map_data), "Contractors All Risks function is having issue(S) . ");
			}
			
			if(((String)map_data.get("CD_ComputersandElectronicRisks")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
				customAssert.assertTrue(funcComputersandElectronicRisks(map_data), "Computers and Electronic Risks function is having issue(S) . ");
			}
			
			if(((String)map_data.get("CD_Money")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
				customAssert.assertTrue(funcMoney(map_data), "Money function is having issue(S) . ");
			}
			if(((String)map_data.get("CD_GoodsInTransit")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
				customAssert.assertTrue(funcGoodsInTransit(map_data), "Goods In Transit function is having issue(S) . ");
			}
			if(((String)map_data.get("CD_MarineCargo")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Marine Cargo"),"Issue while Navigating to Marine Cargo screen . ");
				customAssert.assertTrue(funcMarineCargo(map_data), "Marine Cargo function is having issue(S) . ");
			}
			if(((String)map_data.get("CD_CyberandDataSecurity")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
				customAssert.assertTrue(funcCyberAndDataSecurity(map_data), "Cyber and Data Security function is having issue(S) . ");
			}*/
			
			/*if(((String)map_data.get("CD_DirectorsandOfficers")).equals("Yes") && ((String)map_data.get("DO_ChangeValue")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Directors and Officers"),"Issue while Navigating to Directors and Officers screen . ");
			customAssert.assertTrue(funcDirectorsAndOfficers(map_data), "Directors and Officers function is having issue(S) . ");
			}*/
			
			if(((String)map_data.get("CD_FrozenFood")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Frozen Foods"),"Issue while Navigating to Frozen Foods screen . ");
				customAssert.assertTrue(funcFrozenFoods(map_data), "Frozen Foods function is having issue(S) . ");
			}
			/*
			if(((String)map_data.get("CD_LossofLicence")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Loss of Licence"),"Issue while Navigating to Loss of Licence screen . ");
				customAssert.assertTrue(funcLossofLicence(map_data), "Loss of Licence function is having issue(S) . ");
			}
			if(((String)map_data.get("CD_FidelityGuarantee")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
				customAssert.assertTrue(funcFidelityGuarantee(map_data), "Fidelity Guarantee function is having issue(S) . ");
			}
			if(((String)map_data.get("CD_Terrorism")).equals("Yes") && ((String)map_data.get("TER_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(funcTerrorism(map_data), "Terrorism function is having issue(S) . ");
			}
			
			if(((String)map_data.get("CD_LegalExpenses")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
				customAssert.assertTrue(funcLegalExpenses(map_data,CommonFunction.product,CommonFunction.businessEvent), "Legal Expenses function is having issue(S) . ");
			}*/
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcPremiumSummary_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,""), "Premium Summary function is having issue(S) . ");
			/////////////////////////////////////////
		
		return true;
		
	} catch (Exception e) {
		
		return false;
	}
}

public boolean funcPremiumSummary(Map<Object, Object> map_data,String code,String event,String flowName){
	
	boolean r_value= true;
	Date currentDate = new Date();
	String testName = (String)map_data.get("Automation Key");
String customPolicyDuration=null;
SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
//String inception_date = common.daysIncrementWithOutFormation(df.format(currentDate), 0);
try{
	customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
	
	int policy_Duration = Integer.parseInt((String)map_data.get("PS_Duration"));
	boolean negativeFlag = policy_Duration < 0 ?false:true;
	customAssert.assertTrue(negativeFlag , "Policy Duration should be non-negative value . ");
	boolean Months18_Flag = policy_Duration < 550 ?true:false;
	customAssert.assertTrue(Months18_Flag , "Policy Duration Should be less than 18 Months ( <= 549 Days) . ");
	policy_Duration--;
	
	String policy_Start_date = common_VELA.get_PolicyStartDate((String)map_data.get("PS_PolicyStartDate"));
	map_data.put("PS_PolicyStartDate", policy_Start_date);
	String Policy_End_Date = common.daysIncrementWithOutFormation((String)map_data.get("PS_PolicyStartDate"), policy_Duration);
	
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
	customAssert.assertTrue(common_CCI.funcPremiumSummaryTable(code, event, map_data),"Premium summary Calculation function failed");		
	customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
	customPolicyDuration = k.getText("Policy_Duration");
	customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,map_data),"Error while writing Policy Duration data to excel .");
	TestUtil.reportStatus("Policy Duration = "+customPolicyDuration+" Days . ", "Info", true);
	
	TestUtil.reportStatus("Premium Summary details are filled sucessfully . ", "Info", true);
	
}catch(Throwable t){
	return false;
}

return r_value;
}

public boolean funcPremiumSummaryTable(String code,String event,Map<Object, Object> map_data) throws Exception{
	
	String testName = (String)map_data.get("Automation Key");
	WebDriverWait wait;
	wait = new WebDriverWait(driver, 5);
	Hashtable<String,String> coverlist = new Hashtable<String,String>();
	Hashtable<String,String> premSmryData = new Hashtable<String,String>();
	Hashtable<String,String> transSmryData = new Hashtable<String,String>();
	Hashtable<String,String> cHash = new Hashtable<String,String>();
	boolean transPremFlag =false;
	String[] prem_col = {"gprem","comr","comm","nprem","gipt","nipt"};
	String[] prem_header = {"GrossPremium","CommissionRate","Commission","NetPremium","GrossIPT","NetIPT"};
	//System.out.println(prem_col(0));
	if(CommonFunction.product.equalsIgnoreCase("POB")|| CommonFunction.product.equalsIgnoreCase("POC")){
		coverlist.put("Material Damage","md8");
		coverlist.put("Loss Of Rental Income","bi3");
		coverlist.put("Property Owners Liability","pl3");
		coverlist.put("Terrorism","tr3");
	}else if(CommonFunction.product.equalsIgnoreCase("XOE")){
		coverlist.put("Property Excess of Loss","xo1");
		coverlist.put("Terrorism","tr2");
	}else{
		coverlist.put("Material Damage","md7");
		coverlist.put("Business Interruption","bi2");
		coverlist.put("Terrorism","tr2");
	}
	coverlist.put("Employers Liability","el3");
	coverlist.put("Public Liability","pl2");
	coverlist.put("Products Liability","pr1");
	coverlist.put("Specified All Risks","sar");
	//coverlist.put("Contractors All Risks","car");
	coverlist.put("Computers and Electronic Risks","it");
	coverlist.put("Money","mn2");
	coverlist.put("Goods In Transit","gt2");
	//coverlist.put("Marine Cargo","mar");
	//coverlist.put("Cyber and Data Security","cyb");
	/*if(code.equalsIgnoreCase("CTA")){
		coverlist.put("Directors and Officers","do_pct");
	}else{
		coverlist.put("Directors and Officers","do2");
		}*/
	coverlist.put("Frozen Foods","ff2");
	coverlist.put("Loss of Licence","ll2");
	coverlist.put("Fidelity Guarantee","fg");
	//coverlist.put("Legal Expenses","lg2");
	coverlist.put("Total","tot");
	
	cHash.put("gprem","GP");
	cHash.put("comr","CR");
	cHash.put("comm","GC");
	cHash.put("nprem","NP");
	cHash.put("gipt","GT");
	cHash.put("nipt","NPIPT");
	cHash.put("com","GC");
	
	if (driver.findElement(By.xpath("//*[@id='main']/form/p")).getText().contains("Premium Summary")==false){
		Assert.fail("Premium summary Page couldn't displayed");
	}
	String stDate = null;
	String endDate =null;
	try{
		k.ImplicitWaitOff();
		stDate = driver.findElement(By.xpath("//*[@name='start_date']")).getAttribute("value");
	}catch(Exception e){
		stDate = driver.findElement(By.xpath("//*[@id='start_date']")).getText() ;//getAttribute("value");
	}
	try{
		k.ImplicitWaitOff();
		endDate = driver.findElement(By.xpath("//*[@name='end_date']")).getAttribute("value");
	}catch(Exception e){
		 endDate = driver.findElement(By.xpath("//*[@id='end_date']")).getText() ;//.getAttribute("value");
	}
	String duration = driver.findElement(By.xpath("//*[@id='duration']")).getText();
	//System.out.println("Policy Start Date " + stDate );
	TestUtil.reportStatus("Policy Start Date " + stDate, "Info", false);
	//System.out.println("Policy End Date " + endDate );
	TestUtil.reportStatus("Policy End Date " + endDate, "Info", false);
	//System.out.println("Policy Duration "+duration);
	TestUtil.reportStatus("Policy Duration "+duration, "Info", false);
	
	List<WebElement> col=driver.findElements(By.xpath("html/body/div[3]/form/div/table[2]/tbody/tr[1]/td"));
	//System.out.println(col.size());
	int cCount = col.size();
	
	List<WebElement> row = driver.findElements(By.xpath("html/body/div[3]/form/div/table[2]/tbody/tr"));
	//System.out.println(row.size());
	int rcount = row.size();
	String Status = driver.findElement(By.xpath("//*[@id='headbox']/tbody/tr[1]/td[6]")).getText();
	//System.out.println("Policy Status - "+ Status); 
	TestUtil.reportStatus("Current Policy Status - "+ Status, "Info", false);
	String tXpath;
	String keyName;
	String CellValue;
	switch(Status){
	
	case "Submitted":  		// Submitted State
		//System.out.println("Covers Found -:"+ (rcount-1));
		customAssert.assertTrue(PremiumSummaryDataTraverse(rcount,coverlist,cHash,code,event,true),"Failed in Premium Summary Calculation");
		customAssert.assertTrue(common.insuranceTaxAdjustmentHandling(code,event),"Insurance tax adjustment is having issue(S).");
		TestUtil.reportStatus("Premium Summary data verification after Insurance Tax Adjustment", "Info", true);
		if(((String)map_data.get("PS_DefaultStartEndDate")).equalsIgnoreCase("No") && ((String)map_data.get("PS_Duration"))!="365"){
			TransactionPremiumTable(rcount,coverlist,cHash,code,event,true);
		}
		customAssert.assertTrue(PremiumSummaryDataTraverse(rcount,coverlist,cHash,code,event,false),"Failed in Premium Summary Calculation after Insurance adjustment");
		
		break;
	case "Submitted (Rewind)":  		
		customAssert.assertTrue(PremiumSummaryDataTraverse(rcount,coverlist,cHash,code,event,true),"Failed in Premium Summary Calculation");
		//if(!(flowName.equalsIgnoreCase("RewindRemoveCover"))){
		customAssert.assertTrue(common.insuranceTaxAdjustmentHandling(code,event),"Insurance tax adjustment is having issue(S).");
		//}
		TestUtil.reportStatus("Premium Summary data verification after Insurance Tax Adjustment", "Info", true);
		customAssert.assertTrue(PremiumSummaryDataTraverse(rcount,coverlist,cHash,code,event,false),"Failed in Premium Summary Calculation after Insurance adjustment");
		
		break;
	case "Endorsement Submitted (Rewind)":  		
		customAssert.assertTrue(common.insuranceTaxAdjustmentHandling(code,event),"Insurance tax adjustment is having issue(S).");
		common_CCI.transactionDetails_table_values.clear();
		customAssert.assertTrue(CommonFunction_CCI.PremiumSummaryDataTraverseMTA(rcount,coverlist,cHash,code,event,true),"Failed in Premium Summary Calculation");				
		break;	
	case "Renewal Pending": 
		
		customAssert.assertTrue(common_CCI.funcGetPremiums(rcount,coverlist,cHash,code,event,true),"Failed in Premium Summary Calculation");
		/*customAssert.assertTrue(common.insuranceTaxAdjustmentHandling(code,event),"Insurance tax adjustment is having issue(S).");
		TestUtil.reportStatus("Premium Summary data verification after Insurance Tax Adjustment", "Info", true);
		customAssert.assertTrue(PremiumSummaryDataTraverse(rcount,coverlist,cHash,code,event,false),"Failed in Premium Summary Calculation after Insurance adjustment");
		*/
		break;	
	case "Renewal Submitted": 
		customAssert.assertTrue(PremiumSummaryDataTraverse(rcount,coverlist,cHash,code,event,true),"Failed in Premium Summary Calculation");
		customAssert.assertTrue(common.insuranceTaxAdjustmentHandling(code,event),"Insurance tax adjustment is having issue(S).");
		TestUtil.reportStatus("Premium Summary data verification after Insurance Tax Adjustment", "Info", true);
		customAssert.assertTrue(PremiumSummaryDataTraverse(rcount,coverlist,cHash,code,event,false),"Failed in Premium Summary Calculation after Insurance adjustment");
		break;
	case "Endorsement Submitted":  		
		common_CCI.isPreVerification=true;
		customAssert.assertTrue(CommonFunction_CCI.PremiumSummaryDataTraverseMTA(rcount,coverlist,cHash,code,event,true),"Failed in Premium Summary Calculation");
		//if(!(flowName.equalsIgnoreCase("RewindRemoveCover"))){
		customAssert.assertTrue(common.insuranceTaxAdjustmentHandling(code,event),"Insurance tax adjustment is having issue(S).");
		//}
		TestUtil.reportStatus("Premium Summary data verification after Insurance Tax Adjustment", "Info", true);
		common_CCI.isPreVerification=false;
		customAssert.assertTrue(CommonFunction_CCI.PremiumSummaryDataTraverseMTA(rcount,coverlist,cHash,code,event,false),"Failed in Premium Summary Calculation after Insurance adjustment");
		
		break;	 
	case "Quoted":  //Quoted State
	
		//System.out.println("Covers Found -:"+ (rcount-1));
		TestUtil.reportStatus("Covers Found -:"+ (rcount-1), "info", true);
		tXpath = "//*[@class='matrix']/tbody/tr";
		for(int i =1;i<=rcount;i++){
			String sectionXpath = tXpath+"["+i+"]/td[1]";
			String sec_name = driver.findElement(By.xpath(sectionXpath)).getText();
			for(int j = 2; j<=cCount;j++){
				String ColumnValues = tXpath+"["+i+"]/td["+j+"]";
				//System.out.print(driver.findElement(By.xpath(ColumnValues)).getText()+"\t");
				CellValue = driver.findElement(By.xpath(ColumnValues)).getText().replaceAll(",","");;
				premSmryData.put("PS_"+sec_name.replaceAll(" ", "")+"_"+cHash.get(prem_col[j-2]), CellValue);
			}
			PremiumSummarytableCalculation(premSmryData,sec_name);
			
		}
		break;
	case  "On Cover" :  //On Cover State
		
		//System.out.println("Covers Found -:"+ (rcount-1));
		TestUtil.reportStatus("Covers Found -:"+ (rcount-1), "info", true);
		tXpath = "//*[@class='matrix']/tbody/tr";
		for(int i =1;i<=rcount;i++){
			String sectionXpath = tXpath+"["+i+"]/td[1]";
			String sec_name = driver.findElement(By.xpath(sectionXpath)).getText();
			//System.out.print(sec_name+"\t\t\t");
			//TestUtil.reportStatus(sec_name+"\t\t\t", "Info", false);
			for(int j = 2; j<=cCount;j++){
				String ColumnValues = tXpath+"["+i+"]/td["+j+"]";
				CellValue = driver.findElement(By.xpath(ColumnValues)).getText().replaceAll(",","");;
				//System.out.print(CellValue +  "\t");
				//TestUtil.reportStatus(CellValue +  "\t", "Info", false);
				premSmryData.put("PS_"+sec_name.replaceAll(" ", "")+"_"+cHash.get(prem_col[j-2]), CellValue);
				
				}
			PremiumSummarytableCalculation(premSmryData,sec_name);	
			
		}
		break;
	}
	
	
	double FinalPremium = 0.0 , Total_GrossPremium , Total_GrossTax , calcFinalPremium = 0.0;
	switch (common.currentRunningFlow) {
	case "NB":
		FinalPremium =Double.parseDouble(k.getTextByXpath("//*[@class='gttext']").replaceAll(",",""));
		Total_GrossPremium =Double.parseDouble(TestUtil.getStringfromMap("PS_Total_GP",""));// premSmryData.get("PS_Total_GP"));
		Total_GrossTax=Double.parseDouble(TestUtil.getStringfromMap("PS_Total_GT",""));// premSmryData.get("PS_Total_GT"));
		calcFinalPremium = Total_GrossPremium +Total_GrossTax;
		break;
	case "CAN":
		FinalPremium =Double.parseDouble(k.getTextByXpath("//*[@class='gttext']").replaceAll(",",""));
		Total_GrossPremium =Double.parseDouble(TestUtil.getStringfromMap("PS_Total_GP",""));// premSmryData.get("PS_Total_GP"));
		Total_GrossTax=Double.parseDouble(TestUtil.getStringfromMap("PS_Total_GT",""));// premSmryData.get("PS_Total_GT"));
		calcFinalPremium = Total_GrossPremium +Total_GrossTax;
		break;
		
	case "MTA":
		FinalPremium =Double.parseDouble(k.getTextByXpath("//*[@class='gttext']").replaceAll(",",""));
		Total_GrossPremium =Double.parseDouble(TestUtil.getStringfromMap("PS_Total_GP",CommonFunction.businessEvent));// premSmryData.get("PS_Total_GP"));
		Total_GrossTax=Double.parseDouble(TestUtil.getStringfromMap("PS_Total_GT",CommonFunction.businessEvent));// premSmryData.get("PS_Total_GT"));
		calcFinalPremium = Total_GrossPremium +Total_GrossTax;
		break;
	case "Renewal":
		FinalPremium =Double.parseDouble(k.getTextByXpath("//*[@class='gttext']").replaceAll(",",""));
		Total_GrossPremium =Double.parseDouble(TestUtil.getStringfromMap("PS_Total_GP",CommonFunction.businessEvent));// premSmryData.get("PS_Total_GP"));
		Total_GrossTax=Double.parseDouble(TestUtil.getStringfromMap("PS_Total_GT",CommonFunction.businessEvent));// premSmryData.get("PS_Total_GT"));
		calcFinalPremium = Total_GrossPremium +Total_GrossTax;
		break;
	
	default:
		break;
	}
	
	compareValues(calcFinalPremium,FinalPremium,"Policy Premium ");
	TestUtil.reportStatus("Final Policy Premium  :<b>"+calcFinalPremium, "Info", false);

	k.ImplicitWaitOn();
	return true;
	
}

public static boolean PremiumSummaryDataTraverse(int rcount, Hashtable<String,String> coverlist,Hashtable<String,String> cHash,String code, String event,boolean xlWrite){
    
    try{
           common.GrosspremSmryData.clear();
           String[] prem_col = {"gprem","comr","comm","nprem","gipt","nipt"};
           String sec_name_withoutSpace = null;
           String CellValue;
           String testName = (String)common.NB_excel_data_map.get("Automation Key");
           Hashtable<String,String> premSmryData = new Hashtable<String,String>();
           
           TestUtil.reportStatus("Covers Found -:"+ (rcount-1), "info", true);
           String tXpath = "//*[@class='matrix']/tbody/tr";
           String Trax_table = "html/body/div[3]/form/div/table[3]/tbody/tr";
           List<WebElement> ls = driver.findElements(By.xpath(Trax_table));
           
           for(int i =1;i<=rcount;i++){
                  String sectionXpath = tXpath+"["+i+"]/td[1]";
                  String sec_name = driver.findElement(By.xpath(sectionXpath)).getText();
                  
                  if(sec_name.contains("Frozen Food")){
                        sec_name_withoutSpace = "FrozenFoods";
                  }else if(sec_name.contains("Licence")){
                        sec_name_withoutSpace = "LossOfLicence";
                  }else if(sec_name.contains("Total")){
                        sec_name = "Total";
                        sec_name_withoutSpace = "Total";
                  }else{
                        sec_name_withoutSpace = sec_name.replaceAll(" ", "");
                  }
                  
                  String policy_status_actual = k.getText("Policy_status_header");
                  
                  if(policy_status_actual.contains("Rewind")){
                        if(sec_name_withoutSpace.contains("Total")){
                               if(sec_name_withoutSpace.contains("Total")==false && xlWrite==true )
                                      TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_"+sec_name_withoutSpace+"_IPT", TestUtil.getStringfromMap("PS_IPTRate",CommonFunction.businessEvent), common.NB_excel_data_map);
                                      
                                      //System.out.print(sec_name+"\t\t\t");
                                      TestUtil.reportStatus("---------------"+sec_name+"-----------------","Info",false);
                                      for(int j=0;j<prem_col.length;j++){
                                             String xpathVal;
                                             xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j]+"']";
                                             String keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]);
                                             
                                             if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comm")){
                                                    xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_com']";
                                                    CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
                                                    customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                             }
                                             if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comr")){
                                                    System.out.print(" ___ \t ");
                                             }else if(prem_col[j].equals("comr") && (coverlist.get(sec_name).equals("tot"))==false){
                                                    if(xlWrite){
                                                           k.scrollInViewByXpath(xpathVal);
                                                           driver.findElement(By.xpath(xpathVal)).click();
                                                           driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.chord(Keys.CONTROL, "a"),TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR",CommonFunction.businessEvent));
                                                           driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.TAB);
                                                    }
                                                    CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
                                                    if(xlWrite){
                                                           premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                           //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                                    }else if(ls.size()==0) {
                                                           compareValues(Double.parseDouble(TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR",CommonFunction.businessEvent)),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
                                                           premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                    }
                                                    keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]);
                                                    xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j-1]+"']";
                                                    CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
                                                    if(xlWrite){
                                                           premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
                                                           common.GrosspremSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
                                                           //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                                    }      
                                             }else{
                                                    CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");
                                                    if(xlWrite){
                                                           premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                           //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                                    }else if(ls.size()==0){
                                                           
                                                           compareValues(Double.parseDouble(TestUtil.getStringfromMap(keyName,CommonFunction.businessEvent)),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
                                                           premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                    }
                                                 keyName=null;
                                                 }
                                      }
                                             //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,common.NB_excel_data_map),"Error while writing Policy Duration data to excel .");
                                      if(ls.size()==0){
                                    	  CommonFunction_CCI.PremiumSummarytableCalculation(premSmryData,sec_name_withoutSpace);}
                        }else{
                               
                               if(sec_name.contains("Liability")){
                                      sec_name_withoutSpace = "Liability";
                               }
                               if(sec_name.contains("Frozen")){
                                      sec_name_withoutSpace = "FrozenFood";
                               }
                               if(sec_name.contains("Licence")){
                                      sec_name_withoutSpace = "LossofLicence";
                               }
                               
                               if((((String)common.NB_excel_data_map.get("CD_Add_"+sec_name_withoutSpace)).equalsIgnoreCase("Yes") &&
                                             ((String)common.NB_excel_data_map.get("CD_"+sec_name_withoutSpace)).equalsIgnoreCase("No"))){
                                      
                                      if(sec_name.contains("Employers")){
                                             sec_name_withoutSpace = "EmployersLiability";
                                      }
                                      if(sec_name.contains("Public")){
                                             sec_name_withoutSpace = "PublicLiability";
                                      }
                                      if(sec_name.contains("Product")){
                                             sec_name_withoutSpace = "ProductsLiability";
                                      }
                                      if(sec_name.contains("Owners")){
                                             sec_name_withoutSpace = "PropertyOwnersLiability";
                                      }
                                      if(sec_name.contains("Frozen")){
                                             sec_name_withoutSpace = "FrozenFoods";
                                      }
                                      if(sec_name.contains("Licence")){
                                             sec_name_withoutSpace = "LossOfLicence";
                                      }
                                      
                                      if(sec_name_withoutSpace.contains("Total")==false && xlWrite==true )
                                             if(((String)common.NB_excel_data_map.get("PS_InsuranceTaxButton")).equalsIgnoreCase("Yes")){
                                                    TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_"+sec_name_withoutSpace+"_IPT", "00.00", common.NB_excel_data_map);
                                             }else{
                                                    TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_"+sec_name_withoutSpace+"_IPT", TestUtil.getStringfromMap("PS_IPTRate",CommonFunction.businessEvent), common.NB_excel_data_map);
                                             }
                                      
                                             //System.out.print(sec_name+"\t\t\t");
                                             TestUtil.reportStatus("---------------"+sec_name+"-----------------","Info",false);
                                             for(int j=0;j<prem_col.length;j++){
                                                    String xpathVal;
                                                    xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j]+"']";
                                                    String keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]);
                                                    
                                                    if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comm")){
                                                           xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_com']";
                                                           CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
                                                           customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                                    }
                                                    if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comr")){
                                                           System.out.print(" ___ \t ");
                                                    }else if(prem_col[j].equals("comr") && (coverlist.get(sec_name).equals("tot"))==false){
                                                           if(xlWrite){
                                                                  k.scrollInViewByXpath(xpathVal);
                                                                  driver.findElement(By.xpath(xpathVal)).click();
                                                                  driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.chord(Keys.CONTROL, "a"),TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR",CommonFunction.businessEvent));
                                                                  driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.TAB);
                                                           }
                                                           CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
                                                           if(xlWrite){
                                                                  premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                                  //common.GrosspremSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
                                                                  //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                                           }else if(ls.size()==0) {
                                                                  compareValues(Double.parseDouble(TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR",CommonFunction.businessEvent)),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
                                                                  premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                           }
                                                           keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]);
                                                           xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j-1]+"']";
                                                           CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
                                                           if(xlWrite){
                                                                  premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
                                                                  common.GrosspremSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
                                                                  //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                                           }      
                                                    }else{
                                                           CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");
                                                           if(xlWrite){
                                                                  premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                                  //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                                           }else if(ls.size()==0){
                                                                  
                                                                  compareValues(Double.parseDouble(TestUtil.getStringfromMap(keyName,CommonFunction.businessEvent)),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
                                                                  premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                           }
                                                        keyName=null;
                                                        }
                                             }
                                                    //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,common.NB_excel_data_map),"Error while writing Policy Duration data to excel .");
                                             if(ls.size()==0){
                                            	 CommonFunction_CCI.PremiumSummarytableCalculation(premSmryData,sec_name_withoutSpace);}
                                             
                               }else if((((String)common.NB_excel_data_map.get("CD_Add_"+sec_name_withoutSpace)).equalsIgnoreCase("No") &&
                                             ((String)common.NB_excel_data_map.get("CD_"+sec_name_withoutSpace)).equalsIgnoreCase("Yes"))){
                                      
                                      if(sec_name.contains("Employers")){
                                             sec_name_withoutSpace = "EmployersLiability";
                                      }
                                      if(sec_name.contains("Public")){
                                             sec_name_withoutSpace = "PublicLiability";
                                      }
                                      if(sec_name.contains("Product")){
                                             sec_name_withoutSpace = "ProductsLiability";
                                      }
                                      if(sec_name.contains("Owners")){
                                             sec_name_withoutSpace = "PropertyOwnersLiability";
                                      }
                                      if(sec_name.contains("Frozen")){
                                             sec_name_withoutSpace = "FrozenFoods";
                                      }
                                      if(sec_name.contains("Licence")){
                                             sec_name_withoutSpace = "LossOfLicence";
                                      }
                                      
                                      if(sec_name_withoutSpace.contains("Total")==false && xlWrite==true )
                                             //TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_"+sec_name_withoutSpace+"_IPT", TestUtil.getStringfromMap("PS_IPTRate"), common.NB_excel_data_map);
                                             
                                             //System.out.print(sec_name+"\t\t\t");
                                             TestUtil.reportStatus("---------------"+sec_name+"-----------------","Info",false);
                                             for(int j=0;j<prem_col.length;j++){
                                                    String xpathVal;
                                                    xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j]+"']";
                                                    String keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]);
                                                    
                                                    if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comm")){
                                                           xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_com']";
                                                           CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
                                                           customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                                    }
                                                    if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comr")){
                                                           System.out.print(" ___ \t ");
                                                    }else if(prem_col[j].equals("comr") && (coverlist.get(sec_name).equals("tot"))==false){
                                                           if(xlWrite){
                                                                  k.scrollInViewByXpath(xpathVal);
                                                                  //driver.findElement(By.xpath(xpathVal)).click();
                                                                  //driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.chord(Keys.CONTROL, "a"),TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR"));
                                                                  //driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.TAB);
                                                           }
                                                           CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
                                                           if(xlWrite){
                                                                  premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                                  //common.GrosspremSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
                                                                  //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                                           }else if(ls.size()==0) {
                                                                  compareValues(Double.parseDouble(TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR",CommonFunction.businessEvent)),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
                                                                  premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                           }
                                                           keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]);
                                                           xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j-1]+"']";
                                                           CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
                                                           if(xlWrite){
                                                                  premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
                                                                  common.GrosspremSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
                                                                  //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                                           }      
                                                    }else{
                                                           CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");
                                                           if(xlWrite){
                                                                  premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                                  //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                                           }else if(ls.size()==0){
                                                                  
                                                                  compareValues(Double.parseDouble(TestUtil.getStringfromMap(keyName,CommonFunction.businessEvent)),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
                                                                  premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                           }
                                                        keyName=null;
                                                        }
                                             }
                                                    //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,common.NB_excel_data_map),"Error while writing Policy Duration data to excel .");
                                             if(ls.size()==0){
                                            	 CommonFunction_CCI.PremiumSummarytableCalculation(premSmryData,sec_name_withoutSpace);}
                               }
                               else if((((String)common.NB_excel_data_map.get("CD_Add_"+sec_name_withoutSpace)).equalsIgnoreCase("Yes") &&
                                             ((String)common.NB_excel_data_map.get("CD_"+sec_name_withoutSpace)).equalsIgnoreCase("Yes"))){
                                      
                                      if(sec_name.contains("Employers")){
                                             sec_name_withoutSpace = "EmployersLiability";
                                      }
                                      if(sec_name.contains("Public")){
                                             sec_name_withoutSpace = "PublicLiability";
                                      }
                                      if(sec_name.contains("Product")){
                                             sec_name_withoutSpace = "ProductsLiability";
                                      }
                                      if(sec_name.contains("Owners")){
                                             sec_name_withoutSpace = "PropertyOwnersLiability";
                                      }
                                      if(sec_name.contains("Frozen")){
                                             sec_name_withoutSpace = "FrozenFoods";
                                      }
                                      if(sec_name.contains("Licence")){
                                             sec_name_withoutSpace = "LossOfLicence";
                                      }
                                      
                                      if(sec_name_withoutSpace.contains("Total")==false && xlWrite==true )
                                             //TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_"+sec_name_withoutSpace+"_IPT", TestUtil.getStringfromMap("PS_IPTRate"), common.NB_excel_data_map);
                                             
                                             //System.out.print(sec_name+"\t\t\t");
                                             TestUtil.reportStatus("---------------"+sec_name+"-----------------","Info",false);
                                             for(int j=0;j<prem_col.length;j++){
                                                    String xpathVal;
                                                    xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j]+"']";
                                                    String keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]);
                                                    
                                                    if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comm")){
                                                           xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_com']";
                                                           CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
                                                           customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                                    }
                                                    if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comr")){
                                                           System.out.print(" ___ \t ");
                                                    }else if(prem_col[j].equals("comr") && (coverlist.get(sec_name).equals("tot"))==false){
                                                           if(xlWrite){
                                                                  k.scrollInViewByXpath(xpathVal);
                                                                  //driver.findElement(By.xpath(xpathVal)).click();
                                                                  //driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.chord(Keys.CONTROL, "a"),TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR"));
                                                                  //driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.TAB);
                                                           }
                                                           CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
                                                           if(xlWrite){
                                                                  premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                                  //common.GrosspremSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
                                                                  //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                                           }else if(ls.size()==0) {
                                                                  compareValues(Double.parseDouble(TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR",CommonFunction.businessEvent)),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
                                                                  premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                           }
                                                           keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]);
                                                           xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j-1]+"']";
                                                           CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
                                                           if(xlWrite){
                                                                  premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
                                                                  common.GrosspremSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
                                                                  //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                                           }      
                                                    }else{
                                                           CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");
                                                           if(xlWrite){
                                                                  premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                                  //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                                           }else if(ls.size()==0){
                                                                  
                                                                  compareValues(Double.parseDouble(TestUtil.getStringfromMap(keyName,CommonFunction.businessEvent)),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
                                                                  premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                           }
                                                        keyName=null;
                                                        }
                                             }
                                                    //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,common.NB_excel_data_map),"Error while writing Policy Duration data to excel .");
                                             if(ls.size()==0){
                                            	 CommonFunction_CCI.PremiumSummarytableCalculation(premSmryData,sec_name_withoutSpace);}
                               }
                        }
                        
                  }else{
                	  if(sec_name_withoutSpace.contains("Total")==false && xlWrite==true )
                      	/*if(((String)common.NB_excel_data_map.get("PS_InsuranceTaxButton")).equalsIgnoreCase("Yes")){
								TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_"+sec_name_withoutSpace+"_IPT", "00.00", common.NB_excel_data_map);
							}else{*/
								TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_"+sec_name_withoutSpace+"_IPT", TestUtil.getStringfromMap("PS_IPTRate",CommonFunction.businessEvent), common.NB_excel_data_map);
							/*}*/
                               
                               //System.out.print(sec_name+"\t\t\t");
                               TestUtil.reportStatus("---------------"+sec_name+"-----------------","Info",false);
                               for(int j=0;j<prem_col.length;j++){
                                      String xpathVal;
                                      xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j]+"']";
                                      String keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]);
                                      
                                      if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comm")){
                                             xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_com']";
                                             CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");
                                             if(((String)common.NB_excel_data_map.get("PS_DefaultStartEndDate")).equalsIgnoreCase("Yes") && ((String)common.NB_excel_data_map.get("PS_Duration"))=="365"){
                                            	 customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                             }
                                      }
                                      if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comr")){
                                             System.out.print(" ___ \t ");
                                      }else if(prem_col[j].equals("comr") && (coverlist.get(sec_name).equals("tot"))==false){
                                             if(xlWrite){
                                                    k.scrollInViewByXpath(xpathVal);
                                                    driver.findElement(By.xpath(xpathVal)).click();
                                                    driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.chord(Keys.CONTROL, "a"),TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR",CommonFunction.businessEvent));
                                                    driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.TAB);
                                             }
                                             CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
                                             if(xlWrite){
                                                    premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                    //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                             }else if(ls.size()==0) {
                                                    compareValues(Double.parseDouble(TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR",CommonFunction.businessEvent)),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
                                                    premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                             }
                                             keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]);
                                             xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j-1]+"']";
                                             CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
                                             if(xlWrite){
                                                    premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
                                                    common.GrosspremSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
                                                    //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                             }      
                                      }else{
                                             CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");
                                             if(xlWrite){
                                                    premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                                    //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
                                             }else if(ls.size()==0){
                                                    
                                                    compareValues(Double.parseDouble(TestUtil.getStringfromMap(keyName,"")),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
                                                    premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
                                             }
                                          keyName=null;
                                          }
                               }
                                      //customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,common.NB_excel_data_map),"Error while writing Policy Duration data to excel .");
                               if(ls.size()==0){
                            	   CommonFunction_CCI.PremiumSummarytableCalculation(premSmryData,sec_name_withoutSpace);}
                  }
                  
           }
           customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
           
            
            if(ls.size()>0){ 
            	CommonFunction_CCI.TransactionPremiumTable(rcount,coverlist,cHash,code,event,xlWrite);
           }else if(xlWrite){
                  Set<String> pKeys=premSmryData.keySet();
                        for(String pkey:pKeys){
                               customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName,pkey,premSmryData.get(pkey) ,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+pkey) ;
                        }
           }
           
           return true;
    }catch(Throwable t){
                  k.ImplicitWaitOn();
                  return false;
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
		
		double IPT = 0.0;
		if(common_CCI.isPreVerification)
			 IPT =  Double.parseDouble(TestUtil.getStringfromMap("PS_"+cSection+"_IPT",""));
		else
			 IPT =  Double.parseDouble(TestUtil.getStringfromMap("PS_"+cSection+"_IPT","MTA"));
		
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
		compareValues(calcltdGprem,grossPrem,section+"- Gross Premium ");
		compareValues(calcltdComm,commisn,section+"- Gross Commission ");
		compareValues(calcltdGIPT,grossIPT,section+"- Gross IPT value ");
		compareValues(calcltdNIPT,netIPT,section+"- Net IPT Values ");
		common_CCI.transactionDetails_table_values.put(cSection+"_GP",calcltdGprem);
		common_CCI.transactionDetails_table_values.put(cSection+"_GT",calcltdGIPT);
		common_CCI.transactionDetails_table_values.put(cSection+"_NP",netPrem);
		return true;
	}catch(Throwable t){
		return false;
	}
	}

public static boolean PremiumSummaryDataTraverseMTA(int rcount, Hashtable<String,String> coverlist,Hashtable<String,String> cHash,String code, String event,boolean xlWrite){
	
	try{
		common.GrosspremSmryData.clear();
		String[] prem_col = {"gprem","comr","comm","nprem","gipt","nipt"};
		String sec_name_withoutSpace = null;
		String CellValue;
		String testName = (String)common.MTA_excel_data_map.get("Automation Key");
		Hashtable<String,String> premSmryData = new Hashtable<String,String>();
		
		TestUtil.reportStatus("Covers Found -:"+ (rcount-1), "info", true);
		String tXpath = "//*[@class='matrix']/tbody/tr";
		String Trax_table = "html/body/div[3]/form/div/table[2]/tbody/tr";
		List<WebElement> ls = driver.findElements(By.xpath(Trax_table));
		
		 for(int i =1;i<=rcount;i++){
			 String sectionXpath = tXpath+"["+i+"]/td[1]";
			 String sec_name = driver.findElement(By.xpath(sectionXpath)).getText();
			
			if(sec_name.contains("Frozen Food")){
				sec_name_withoutSpace = "FrozenFoods";
			}else if(sec_name.contains("Licence")){
				sec_name_withoutSpace = "LossOfLicence";
			}else if(sec_name.contains("Total")){
				sec_name = "Total";
				sec_name_withoutSpace = "Total";
			}else{
				sec_name_withoutSpace = sec_name.replaceAll(" ", "");
			}
			
			String policy_status_actual = k.getText("Policy_status_header");
			
			if(policy_status_actual.contains("Endorsement Submitted")){
				if(sec_name_withoutSpace.contains("Total")){
					if(sec_name_withoutSpace.contains("Total")==false && xlWrite==true )
						TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)common.MTA_excel_data_map.get("Automation Key"), "PS_"+sec_name_withoutSpace+"_IPT", TestUtil.getStringfromMap("PS_IPTRate",""), common.MTA_excel_data_map);
						
						//System.out.print(sec_name+"\t\t\t");
						TestUtil.reportStatus("---------------"+sec_name+"-----------------","Info",false);
						for(int j=0;j<prem_col.length;j++){
							String xpathVal;
							xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j]+"']";
							 String keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]);
							
							if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comm")){
								xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_com']";
								CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
								customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.MTA_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
							}
							if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comr")){
								System.out.print(" ___ \t ");
							}else if(prem_col[j].equals("comr") && (coverlist.get(sec_name).equals("tot"))==false){
								if(xlWrite){
									k.scrollInViewByXpath(xpathVal);
									driver.findElement(By.xpath(xpathVal)).click();
									driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.chord(Keys.CONTROL, "a"),TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR",""));
									driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.TAB);
								}
								CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
								if(xlWrite){
									premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
									//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
								}else if(ls.size()==0) {
									compareValues(Double.parseDouble(TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR","")),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
									premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
								}
								keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]);
								xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j-1]+"']";
								CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
								if(xlWrite){
									premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
									common.GrosspremSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
									//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
								}	
							}else{
							    	CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");
							    	if(xlWrite){
									premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
									//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
							    	}else if(ls.size()==0){
							    		
									compareValues(Double.parseDouble(TestUtil.getStringfromMap(keyName,"")),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
									premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
							    	}
							    keyName=null;
							    }
						}
							//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,common.NB_excel_data_map),"Error while writing Policy Duration data to excel .");
						if(ls.size()==0){
							CommonFunction_CCI.PremiumSummarytableCalculation(premSmryData,sec_name_withoutSpace);}
				}else{
					
					if(sec_name.contains("Liability")){
						sec_name_withoutSpace = "Liability";
					}
					if(sec_name.contains("Frozen")){
						sec_name_withoutSpace = "FrozenFood";
					}
					if(sec_name.contains("Licence")){
						sec_name_withoutSpace = "LossofLicence";
					}
					
					if((((String)common.MTA_excel_data_map.get("CD_Add_"+sec_name_withoutSpace)).equalsIgnoreCase("Yes") &&
							((String)common.NB_excel_data_map.get("CD_"+sec_name_withoutSpace)).equalsIgnoreCase("No"))){
						
						if(sec_name.contains("Employers")){
							sec_name_withoutSpace = "EmployersLiability";
						}
						if(sec_name.contains("Public")){
							sec_name_withoutSpace = "PublicLiability";
						}
						if(sec_name.contains("Product")){
							sec_name_withoutSpace = "ProductsLiability";
						}
						if(sec_name.contains("Owners")){
							sec_name_withoutSpace = "PropertyOwnersLiability";
						}
						if(sec_name.contains("Frozen")){
							sec_name_withoutSpace = "FrozenFoods";
						}
						if(sec_name.contains("Licence")){
							sec_name_withoutSpace = "LossOfLicence";
						}
						
						if(sec_name_withoutSpace.contains("Total")==false && xlWrite==true ){
							if(!common_CCI.isMTARewindStarted)
								TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)common.MTA_excel_data_map.get("Automation Key"), "PS_"+sec_name_withoutSpace+"_IPT", TestUtil.getStringfromMap("PS_IPTRate",""), common.MTA_excel_data_map);
						}
							
							//System.out.print(sec_name+"\t\t\t");
							TestUtil.reportStatus("---------------"+sec_name+"-----------------","Info",false);
							for(int j=0;j<prem_col.length;j++){
								String xpathVal;
								xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j]+"']";
								 String keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]);
								
								if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comm")){
									xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_com']";
									CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
									customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.MTA_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
								}
								if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comr")){
									System.out.print(" ___ \t ");
								}else if(prem_col[j].equals("comr") && (coverlist.get(sec_name).equals("tot"))==false){
									if(xlWrite){
										k.scrollInViewByXpath(xpathVal);
										driver.findElement(By.xpath(xpathVal)).click();
										driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.chord(Keys.CONTROL, "a"),TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR",""));
										driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.TAB);
									}
									CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
									if(xlWrite){
										premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
										//common.GrosspremSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
										//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
									}else if(ls.size()==0) {
										compareValues(Double.parseDouble(TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR","")),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
										premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
									}
									keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]);
									xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j-1]+"']";
									CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
									if(xlWrite){
										premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
										common.GrosspremSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
										//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
									}	
								}else{
								    	CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");
								    	if(xlWrite){
										premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
										//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
								    	}else if(ls.size()==0){
								    		
										compareValues(Double.parseDouble(TestUtil.getStringfromMap(keyName,"")),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
										premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
								    	}
								    keyName=null;
								    }
							}
								//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,common.NB_excel_data_map),"Error while writing Policy Duration data to excel .");
							if(ls.size()==0){
								CommonFunction_CCI.PremiumSummarytableCalculation(premSmryData,sec_name_withoutSpace);}
							
					}else if((((String)common.MTA_excel_data_map.get("CD_Add_"+sec_name_withoutSpace)).equalsIgnoreCase("No") &&
							((String)common.NB_excel_data_map.get("CD_"+sec_name_withoutSpace)).equalsIgnoreCase("Yes"))){
						
						if(sec_name.contains("Employers")){
							sec_name_withoutSpace = "EmployersLiability";
						}
						if(sec_name.contains("Public")){
							sec_name_withoutSpace = "PublicLiability";
						}
						if(sec_name.contains("Product")){
							sec_name_withoutSpace = "ProductsLiability";
						}
						if(sec_name.contains("Owners")){
							sec_name_withoutSpace = "PropertyOwnersLiability";
						}
						if(sec_name.contains("Frozen")){
							sec_name_withoutSpace = "FrozenFoods";
						}
						if(sec_name.contains("Licence")){
							sec_name_withoutSpace = "LossOfLicence";
						}
						
						if(sec_name_withoutSpace.contains("Total")==false && xlWrite==true )
							//TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_"+sec_name_withoutSpace+"_IPT", TestUtil.getStringfromMap("PS_IPTRate"), common.NB_excel_data_map);
							
							//System.out.print(sec_name+"\t\t\t");
							TestUtil.reportStatus("---------------"+sec_name+"-----------------","Info",false);
							for(int j=0;j<prem_col.length;j++){
								String xpathVal;
								xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j]+"']";
								 String keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]);
								
								if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comm")){
									xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_com']";
									CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
									customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
								}
								if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comr")){
									System.out.print(" ___ \t ");
								}else if(prem_col[j].equals("comr") && (coverlist.get(sec_name).equals("tot"))==false){
									if(xlWrite){
										k.scrollInViewByXpath(xpathVal);
										//driver.findElement(By.xpath(xpathVal)).click();
										//driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.chord(Keys.CONTROL, "a"),TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR"));
										//driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.TAB);
									}
									CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
									if(xlWrite){
										premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
										//common.GrosspremSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
										//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
									}else if(ls.size()==0) {
										compareValues(Double.parseDouble(TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR","")),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
										premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
									}
									keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]);
									xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j-1]+"']";
									CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
									if(xlWrite){
										premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
										common.GrosspremSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
										//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
									}	
								}else{
								    	CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");
								    	if(xlWrite){
										premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
										//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
								    	}else if(ls.size()==0){
								    		
										compareValues(Double.parseDouble(TestUtil.getStringfromMap(keyName,"")),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
										premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
								    	}
								    keyName=null;
								    }
							}
								//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,common.NB_excel_data_map),"Error while writing Policy Duration data to excel .");
							if(ls.size()==0){
								CommonFunction_CCI.PremiumSummarytableCalculation(premSmryData,sec_name_withoutSpace);}
					}
					else if((((String)common.MTA_excel_data_map.get("CD_Add_"+sec_name_withoutSpace)).equalsIgnoreCase("Yes") &&
							((String)common.NB_excel_data_map.get("CD_"+sec_name_withoutSpace)).equalsIgnoreCase("Yes"))){
						
						if(sec_name.contains("Employers")){
							sec_name_withoutSpace = "EmployersLiability";
						}
						if(sec_name.contains("Public")){
							sec_name_withoutSpace = "PublicLiability";
						}
						if(sec_name.contains("Product")){
							sec_name_withoutSpace = "ProductsLiability";
						}
						if(sec_name.contains("Owners")){
							sec_name_withoutSpace = "PropertyOwnersLiability";
						}
						if(sec_name.contains("Frozen")){
							sec_name_withoutSpace = "FrozenFoods";
						}
						if(sec_name.contains("Licence")){
							sec_name_withoutSpace = "LossOfLicence";
						}
						
						if(sec_name_withoutSpace.contains("Total")==false && xlWrite==true )
							//TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_"+sec_name_withoutSpace+"_IPT", TestUtil.getStringfromMap("PS_IPTRate"), common.NB_excel_data_map);
							
							//System.out.print(sec_name+"\t\t\t");
							TestUtil.reportStatus("---------------"+sec_name+"-----------------","Info",false);
							for(int j=0;j<prem_col.length;j++){
								String xpathVal;
								xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j]+"']";
								 String keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]);
								
								if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comm")){
									xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_com']";
									CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
									customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.MTA_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
								}
								if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comr")){
									System.out.print(" ___ \t ");
								}else if(prem_col[j].equals("comr") && (coverlist.get(sec_name).equals("tot"))==false){
									if(xlWrite){
										k.scrollInViewByXpath(xpathVal);
										//driver.findElement(By.xpath(xpathVal)).click();
										//driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.chord(Keys.CONTROL, "a"),TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR"));
										//driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.TAB);
									}
									CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
									if(xlWrite){
										premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
										//common.GrosspremSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
										//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
									}else if(ls.size()==0) {
										compareValues(Double.parseDouble(TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR","")),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
										premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
									}
									keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]);
									xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j-1]+"']";
									CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
									if(xlWrite){
										premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
										common.GrosspremSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
										//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
									}	
								}else{
								    	CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");
								    	if(xlWrite){
										premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
										//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
								    	}else if(ls.size()==0){
								    		
										compareValues(Double.parseDouble(TestUtil.getStringfromMap(keyName,"")),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
										premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
								    	}
								    keyName=null;
								    }
							}
								//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,common.NB_excel_data_map),"Error while writing Policy Duration data to excel .");
							if(ls.size()==0){
								CommonFunction_CCI.PremiumSummarytableCalculation(premSmryData,sec_name_withoutSpace);}
					}
				}
				
			}else{
				if(sec_name_withoutSpace.contains("Total")==false && xlWrite==true ){
					if(!common_CCI.isMTARewindStarted)
						TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)common.MTA_excel_data_map.get("Automation Key"), "PS_"+sec_name_withoutSpace+"_IPT", TestUtil.getStringfromMap("PS_IPTRate",""), common.MTA_excel_data_map);
				}
					
					//System.out.print(sec_name+"\t\t\t");
					TestUtil.reportStatus("---------------"+sec_name+"-----------------","Info",false);
					for(int j=0;j<prem_col.length;j++){
						String xpathVal;
						xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j]+"']";
						 String keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]);
						
						if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comm")){
							xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_com']";
							CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
							customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.MTA_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
						}
						if(coverlist.get(sec_name).equals("tot") && prem_col[j].equals("comr")){
							System.out.print(" ___ \t ");
						}else if(prem_col[j].equals("comr") && (coverlist.get(sec_name).equals("tot"))==false){
							if(xlWrite){
								k.scrollInViewByXpath(xpathVal);
								driver.findElement(By.xpath(xpathVal)).click();
								driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.chord(Keys.CONTROL, "a"),TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR",""));
								driver.findElement(By.xpath(xpathVal)).sendKeys(Keys.TAB);
							}
							CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
							if(xlWrite){
								premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
								//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
							}else if(ls.size()==0) {
								compareValues(Double.parseDouble(TestUtil.getStringfromMap("PS_"+sec_name_withoutSpace+"_CR","")),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
								premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
							}
							keyName = "PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]);
							xpathVal = "//*[@id='"+coverlist.get(sec_name)+"_"+prem_col[j-1]+"']";
							CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");;
							if(xlWrite){
								premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
								common.GrosspremSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j-1]), CellValue);
								//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
							}	
						}else{
						    	CellValue = driver.findElement(By.xpath(xpathVal)).getAttribute("value").replaceAll(",","");
						    	if(xlWrite){
								premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
								//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, keyName, CellValue,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+keyName);
						    	}else if(ls.size()==0){
						    		
								compareValues(Double.parseDouble(TestUtil.getStringfromMap(keyName,"")),Double.parseDouble(CellValue),cHash.get(prem_col[j]));
								premSmryData.put("PS_"+sec_name_withoutSpace+"_"+cHash.get(prem_col[j]), CellValue);
						    	}
						    keyName=null;
						    }
					}
						//customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,common.NB_excel_data_map),"Error while writing Policy Duration data to excel .");
					if(ls.size()==0){
						CommonFunction_CCI.PremiumSummarytableCalculation(premSmryData,sec_name_withoutSpace);}
			}
			
		 }
		 customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
		 
		 
		 if(ls.size()>0){ 
		 TransactionDetailsTable(rcount,coverlist,cHash,code,event,true);
		 }else if(xlWrite){
			 Set<String> pKeys=premSmryData.keySet();
			 	for(String pkey:pKeys){
			 		customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName,pkey,premSmryData.get(pkey) ,common.MTA_excel_data_map),"Error while writing Premium Summary data to excel ."+pkey) ;
			 	}
		 }
		
		 return true;
	}catch(Throwable t){
			k.ImplicitWaitOn();
			return false;
	}
  
 }

public static void TransactionPremiumTable(int rcount, Hashtable<String,String> coverlist,Hashtable<String,String> cHash,String code, String event, boolean xlWrite){
	//Transaction Premium Table
		try{
			k.ImplicitWaitOff();
			
			String Trax_table = "html/body/div[3]/form/div/table[3]";
			String[] prem_col = {"gprem","comr","comm","nprem","gipt","nipt"};
			String testName = (String)common.NB_excel_data_map.get("Automation Key");
			List<WebElement> col=driver.findElements(By.xpath("html/body/div[3]/form/div/table[3]/tbody/tr[1]/td"));
			//System.out.println(col.size());
			int cCount = col.size();
			if(driver.findElement(By.xpath(Trax_table)).isDisplayed()){
				
				Hashtable<String,String> transSmryData = new Hashtable<String,String>();
				String tXpath =Trax_table+"/tbody/tr";
				String CellValue;
				//System.out.println("Transaction Premium Table exist on premium summary page");
				TestUtil.reportStatus("Transaction Premium Table exist on premium summary page", "Info", true);
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
						//transSmryData
						String ColumnValues = tXpath+"["+i+"]/td["+j+"]";
						CellValue = driver.findElement(By.xpath(ColumnValues)).getText().replaceAll(",","");;
						//System.out.print(CellValue +  "\t");	
						//TestUtil.reportStatus(CellValue +  "\t", "Info", false);
						if(cHash.get(prem_col[j-2]).equalsIgnoreCase("CR") && sec_name.contains("Total"))
						{}else{
						transSmryData.put("PS_"+sec_name.replaceAll(" ", "")+"_"+cHash.get(prem_col[j-2]), CellValue);}
					}
					CommonFunction_CCI.PremiumSummarytableCalculation(transSmryData,sec_name);
					//System.out.println("\n___");
				}
			
			// Writing Data to Excel Sheet from Map
				Set<String> pKeys=transSmryData.keySet();
			 	for(String pkey:pKeys){
			 		if(xlWrite){
			 			customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName,pkey,transSmryData.get(pkey) ,common.NB_excel_data_map),"Error while writing Premium Summary data to excel ."+pkey) ;
			 		}else{
			 			compareValues(Double.parseDouble(transSmryData.get(pkey)), Double.parseDouble(TestUtil.getStringfromMap(pkey,"NB")), pkey);
			 		}
			 	}
			}
		}
		catch(Throwable t ){
			k.ImplicitWaitOn();
			System.out.println("--"); 
		}
		
		//System.out.println("Gross Premium for Material Damage Cover in Transaction Premium table is :"+transSmryData.get("MaterialDamage_gprem"));
		 TestUtil.reportStatus("Values have been stored from Transaction Premium table", "info", true);
}

@SuppressWarnings("static-access")
public boolean funcGetPremiums(int rcount, Hashtable<String,String> coverlist,Hashtable<String,String> cHash,String code, String event,boolean xlWrite){
	   
	   
	   try{
		   
		   customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page is having issue(S)");
		   String flag = "false";
		   k.ImplicitWaitOff();
		   if(k.isDisplayedField("CCF_AdjustmentInfo")){
			   flag = "true";
		   }
		   String sectionName = "";
		   
		   String quoteNumber = k.getText("Quote_Number");
		   TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Renewal",(String)common.Renewal_excel_data_map.get("Automation Key"), "Renewal_QuoteNumber", quoteNumber, common.Renewal_excel_data_map);
		   String startDate = k.getAttribute("Policy_Start_Date", "value");
		   String endDate = k.getAttribute("Policy_End_Date", "value");
		   TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.Renewal_excel_data_map.get("Automation Key"), "PS_PolicyStartDate", startDate, common.Renewal_excel_data_map);
		   TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "QuoteCreation",(String)common.Renewal_excel_data_map.get("Automation Key"), "QC_InceptionDate", startDate, common.Renewal_excel_data_map);
		   TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.Renewal_excel_data_map.get("Automation Key"), "PS_PolicyEndDate", endDate , common.Renewal_excel_data_map);
		   
		   
		   //boolean AdjustmentInfo = k.isDisplayed("CCF_AdjustmentInfo");
		   
		   //To write data from application to Excel for Renewal flow.
		   //customAssert.assertTrue(PremiumSummaryDataTraverse_Renewal(rcount,coverlist,cHash,code,event,true),"Failed in Premium Summary Calculation");

		   switch (flag) {
			   case "true":
				  common.funcButtonSelection("Insurance Tax");
				  customAssert.assertTrue(common.funcPageNavigation("Tax Adjustment", ""),"Unable to land on Tax adjustment screen.");
				  common.totalGrossPremium = 0.0;
				  common.totalGrossTax = 0.0;
				  common.totalNetPremiumTax = 0.0;
				  common.taxTable_tBody = k.getObject("inssuranceTaxMainTableBODY");
				  List<WebElement> list3 = common.taxTable_tBody.findElements(By.tagName("tr"));
					for(int j=0;j<list3.size()-1;j++){
						
						//taxTable_tBody = k.getObject("inssuranceTaxMainTableBODY");
						sectionName = common.taxTable_tBody.findElement(By.xpath("tr["+(j+1)+"]/td[1]")).getText();
						
						if(sectionName.equalsIgnoreCase("") || sectionName==null || sectionName.equalsIgnoreCase("Totals")){
							continue;
						}else{
							
							//String actGP =  taxTable_tBody.findElement(By.xpath("tr["+(j+1)+"]/td[2]")).getText();
							//String actGT =  taxTable_tBody.findElement(By.xpath("tr["+(j+1)+"]/td[5]")).getText();
							String actIPT =  common.taxTable_tBody.findElement(By.xpath("tr["+(j+1)+"]/td[4]")).getText();
							
							//TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.Renewal_excel_data_map.get("Automation Key"), "PS_"+sectionName.replaceAll(" ", "")+"_GP", actGP, common.Renewal_excel_data_map);
							//TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.Renewal_excel_data_map.get("Automation Key"), "PS_"+sectionName.replaceAll(" ", "")+"_GT", actGT, common.Renewal_excel_data_map);
							TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.Renewal_excel_data_map.get("Automation Key"), "PS_"+sectionName.replaceAll(" ", "")+"_IPT", actIPT, common.Renewal_excel_data_map);
							
							//common.totalGrossPremium = common.totalGrossPremium + Double.parseDouble(actGP);
							//common.totalGrossTax = common.totalGrossTax + Double.parseDouble(actGT);
							//common.totalNetPremiumTax = common.totalNetPremiumTax + Double.parseDouble(expNPIT);
							
						}
						common.taxTable_tBody = k.getObject("inssuranceTaxMainTableBODY");
						//list3 = taxTable_tBody.findElements(By.tagName("tr"));
						//countOfCovers = list3.size();
					}
				
				//TestUtil.reportStatus("<b> Tax adjustment operatios is completed. </b>", "Info", false);
				//TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.Renewal_excel_data_map.get("Automation Key"), "PS_Total_GP", common.roundedOff(Double.toString(totalGrossPremium)), common.Renewal_excel_data_map);
				//TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.Renewal_excel_data_map.get("Automation Key"), "PS_Total_GT", common.roundedOff(Double.toString(totalGrossTax)), common.Renewal_excel_data_map);
				//TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.Renewal_excel_data_map.get("Automation Key"), "PS_Total_NPIPT", common.roundedOff(Double.toString(totalNetPremiumTax)), common.Renewal_excel_data_map);
				common.funcPageNavigation("", "Save");
				k.Click("Tax_adj_BackBtn");
				break;
		   }
		   
		   return true;
		   
	   }catch(Exception e){
		   return false;
	   }
  }

public static void TransactionDetailsTable(int rcount, Hashtable<String,String> coverlist,Hashtable<String,String> cHash,String code, String event, boolean xlWrite){
	//Transaction Premium Table
		try{
			k.ImplicitWaitOff();
			List<WebElement> row = driver.findElements(By.xpath("html/body/div[3]/form/div/table[4]/tbody/tr"));
			//System.out.println(row.size());
			int rcount1 = row.size();
			String Trax_table = "html/body/div[3]/form/div/table[4]";
			String[] prem_col = {"gprem","comr","comm","nprem","gipt","nipt"};
			String testName = (String)common.MTA_excel_data_map.get("Automation Key");
			List<WebElement> col=driver.findElements(By.xpath("html/body/div[3]/form/div/table[4]/tbody/tr[1]/td"));
			//System.out.println(col.size());
			int cCount = col.size();
			if(driver.findElement(By.xpath(Trax_table)).isDisplayed()){
				
				Hashtable<String,String> transSmryData = new Hashtable<String,String>();
				String tXpath =Trax_table+"/tbody/tr";
				String CellValue;
				//System.out.println("Transaction Premium Table exist on premium summary page");
				TestUtil.reportStatus("Transaction Details Table exist on premium summary page", "Info", true);
				//System.out.println("Covers Found -:"+ (rcount-1));
				TestUtil.reportStatus("Covers Found -:"+ (rcount1-1), "Info", false);
				
				for(int i =1;i<=rcount1;i++){
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
						//transSmryData
						String ColumnValues = tXpath+"["+i+"]/td["+j+"]";
						CellValue = driver.findElement(By.xpath(ColumnValues)).getText().replaceAll(",","");;
						//System.out.print(CellValue +  "\t");	
						//TestUtil.reportStatus(CellValue +  "\t", "Info", false);
						if(cHash.get(prem_col[j-2]).equalsIgnoreCase("CR") && sec_name.contains("Total"))
						{
							
						}else{
						transSmryData.put("PS_"+sec_name.replaceAll(" ", "")+"_"+cHash.get(prem_col[j-2]), CellValue);}
					}
					CommonFunction_CCI.PremiumSummarytableCalculation(transSmryData,sec_name);
					//System.out.println("\n___");
				}
			
			// Writing Data to Excel Sheet from Map
				Set<String> pKeys=transSmryData.keySet();
			 	for(String pkey:pKeys){
			 		if(xlWrite){
			 			customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName,pkey,transSmryData.get(pkey) ,common.MTA_excel_data_map),"Error while writing Premium Summary data to excel ."+pkey) ;
			 		}else{
			 			compareValues(Double.parseDouble(transSmryData.get(pkey)), Double.parseDouble(TestUtil.getStringfromMap(transSmryData.get(pkey),"")), pkey);
			 		}
			 	}
			}
		}
		catch(Throwable t ){
			k.ImplicitWaitOn();
			System.out.println("--"); 
		}
		
		//System.out.println("Gross Premium for Material Damage Cover in Transaction Premium table is :"+transSmryData.get("MaterialDamage_gprem"));
		 TestUtil.reportStatus("Values have been stored from Transaction Details table", "info", true);
}
//--------------------------------------------------------------------//
//--------------------Transaction Summary-----------------------------//
//--------------------------------------------------------------------//
public boolean transactionSummary_New(String fileName,String testName,String event,String code){
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
				
				TestUtil.reportStatus("Verification Started on Transaction Summary page after Endorsement (MTA) . ", "PASS", false);
				ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
				
				if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
					
						if(TestBase.businessEvent.contains("Renewal") && common.currentRunningFlow.equalsIgnoreCase("MTA")){
							ExpecteDueDate = common.getLastDayOfMonth((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 1);
						}else{
							ExpecteDueDate = common.getLastDayOfMonth((String)common.MTA_excel_data_map.get("MTA_QC_InceptionDate"), 1);
						}
						
					
					
				}else{
					if(((String)common.MTA_excel_data_map.get("PS_PaymentWarrantyRules")).equals("Yes")){
						ExpecteDueDate = (String)common.MTA_excel_data_map.get("PS_PaymentWarrantyDueDate");
					}else{
						if(TestBase.businessEvent.contains("Renewal")){
							ExpecteDueDate = common.getLastDayOfMonth((String)common.Renewal_excel_data_map.get("QC_InceptionDate"), 1);
						}else{
							ExpecteDueDate = common.getLastDayOfMonth((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 1);
						}
						
					}
					
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
				
				if(TestBase.businessEvent.contains("Renewal")){
					//ExpecteTransactionDate = (String)common.Renewal_excel_data_map.get("Renewal_MTA_EffectiveDate");
					ExpecteTransactionDate = (String)common.Renewal_excel_data_map.get("QuoteDate");
					
				}else{
					ExpecteTransactionDate = (String)common.NB_excel_data_map.get("QuoteDate");
				}   						
				
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
					}else if(covername.isEmpty()){
						double general = calculateOtherTS(testName,code,j,td,event,val);
						
						Total = Total + general;
					}
					if(exit.equalsIgnoreCase("Total")){
						i=j;
						String actualTotal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[4]")).getText();  
						common.compareValues(Double.parseDouble(actualTotal), Double.parseDouble(common.roundedOff(Double.toString(Total))), "Transaction Summary Total");
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
  						common.compareValues(Double.parseDouble(actualTotal), Double.parseDouble(common.roundedOff(Double.toString(Total))), "Transaction Summary Total");
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
			if(common.currentRunningFlow.contains("NB")||common.currentRunningFlow.contains("CAN")){
				data_map = common.NB_excel_data_map;
			}
		String product = common.product;
		String part1= "//*[@id='table0']/tbody";
		
		switch (TestBase.businessEvent){
			case "NB" :
				if(((String)data_map.get("CD_Terrorism")).equalsIgnoreCase("Yes")){
					terrorPremium = (String)data_map.get("PS_Terrorism_GP");
					terrorIPT = (String)data_map.get("PS_Terrorism_GT");
				}else{
					terrorPremium="0.00";
					terrorIPT="0.00";
				}
					totalGrossPremium = (String)data_map.get("PS_Total_GP");
					totalGrossIPT = (String)data_map.get("PS_Total_GT");
					break;
					
			case "MTA" :
				
					if(common.currentRunningFlow.contains("NB")){
						if(((String)data_map.get("CD_Terrorism")).equalsIgnoreCase("Yes")){
							terrorPremium = (String)data_map.get("PS_Terrorism_GP");
							terrorIPT = (String)data_map.get("PS_Terrorism_GT");
						}else{
							terrorPremium="0.00";
							terrorIPT="0.00";
						}
						totalGrossPremium = (String)data_map.get("PS_Total_GP");
						totalGrossIPT = (String)data_map.get("PS_Total_GT");
					}else if(common.currentRunningFlow.contains("MTA")){
						
						//if(common.transaction_Details_Premium_Values.get("Terrorism")!=null){
						
						try{
							terrorPremium = Double.toString(common_CCI.transactionDetails_table_values.get("Terrorism_GP"));
							terrorIPT = Double.toString(common_CCI.transactionDetails_table_values.get("Terrorism_GT"));
						
						}catch(Throwable t){
							terrorPremium = "0.0";
							terrorIPT = "0.0";
						
						}
						/*if(((String)data_map.get("CD_Terrorism")).equalsIgnoreCase("Yes")){
							terrorPremium = Double.toString(common.transaction_Details_Premium_Values.get("Terrorism").get("Gross Premium (GBP)"));
							terrorIPT = Double.toString(common.transaction_Details_Premium_Values.get("Terrorism").get("Gross IPT (GBP)"));
							
							terrorPremium = (String)data_map.get("PS_Terrorism_GP");
							terrorIPT = (String)data_map.get("PS_Terrorism_GT");
							
						}else{
							terrorPremium="0.00";
							terrorIPT="0.00";
						}*/
						/*totalGrossPremium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium (GBP)"));
						totalGrossIPT = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Gross IPT (GBP)"));*/
						
						totalGrossPremium = (String)data_map.get("PS_Total_GP");
						totalGrossIPT = (String)data_map.get("PS_Total_GT");
						
					}else if(common.currentRunningFlow.contains("Rewind"))
					{}
						// Values from Endorsement Rewind map
					
					break;
				
			case "CAN" :
					if(common.currentRunningFlow.contains("NB")){
						if(((String)data_map.get("CD_Terrorism")).equalsIgnoreCase("Yes")){
							terrorPremium = (String)data_map.get("PS_Terrorism_GP");
							terrorIPT = (String)data_map.get("PS_Terrorism_GT");
						}else{
							terrorPremium="0.00";
							terrorIPT="0.00";
						}
						totalGrossPremium = (String)data_map.get("PS_Total_GP");
						totalGrossIPT = (String)data_map.get("PS_Total_GT");
					}else{
						if(((String)common.NB_excel_data_map.get("CD_Terrorism")).equalsIgnoreCase("Yes")){
							if(TestBase.product.contains("CCI")){
								terrorPremium = String.valueOf(common_CCI.CAN_CCI_ReturnP_Values_Map.get("Terrorism").get("Gross Premium (GBP)"));
								terrorIPT = String.valueOf(common_CCI.CAN_CCI_ReturnP_Values_Map.get("Terrorism").get("Gross IPT (GBP)"));
							}else if(TestBase.product.contains("POE")){
								// values of cancellation return Premium map
							}else if(TestBase.product.contains("CCJ")){
								// values of cancellation return Premium map
							}
						
							totalGrossPremium =  String.valueOf(common_CCI.CAN_CCI_ReturnP_Values_Map.get("Totals").get("Gross Premium (GBP)"));
							totalGrossIPT = String.valueOf(common_CCI.CAN_CCI_ReturnP_Values_Map.get("Totals").get("Gross IPT (GBP)"));
						}else{
							totalGrossPremium = "0.00";
							totalGrossIPT = "0.00";
						}
					}
					
					break;
					
			case "Renewal" :
				if(((String)data_map.get("CD_Terrorism")).equalsIgnoreCase("Yes")){
					terrorPremium = (String)data_map.get("PS_Terrorism_GP");
					terrorIPT = (String)data_map.get("PS_Terrorism_GT");
				}else{
					terrorPremium="0.00";
					terrorIPT="0.00";
				}
					totalGrossPremium = (String)data_map.get("PS_Total_GP");
					totalGrossIPT = (String)data_map.get("PS_Total_GT");
					break;
					
			case "Rewind" :
				
					if(common.currentRunningFlow.contains("NB")){
						if(((String)data_map.get("CD_Terrorism")).equalsIgnoreCase("Yes")){
							terrorPremium = (String)data_map.get("PS_Terrorism_GP");
							terrorIPT = (String)data_map.get("PS_Terrorism_GT");
						}else{
							terrorPremium="0.00";
							terrorIPT="0.00";
						}
						totalGrossPremium = (String)data_map.get("PS_Total_GP");
						totalGrossIPT = (String)data_map.get("PS_Total_GT");
					}else{
						
						//values from NB Rewind map : 
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
			
		}
					
			
		String general= driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+td+"]")).getText();
		String generalInsTax= driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+1)+"]")).getText();
		String generalDue= driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+2)+"]")).getText();
		
		
		if(terrorPremium == null) {terrorPremium = "0.00";}
		if(terrorIPT == null) {terrorIPT = "0.00";}
		
		if(((String)data_map.get("CD_Terrorism")).equals("No") ||(String)data_map.get("CD_Terrorism")== null )
		{
			terrorPremium="0.00";
			terrorIPT="0.00";
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
					Commission = (String)data_map.get("TS_AJGSplit_A");					
				}
		}
		
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
double LegalExpensesFP=0.00, terrorismFP=0.00, DirectorsAndOfficersFP=0.00, SpecifiedRisksFP=0.00,LossOfLicenceFP=0.0,FrozenFoodsFP=0.0, generalPremium;
double MarineCargoFP=0.00, FrozenFoodFP=0.00, LossofLicenceFP=0.00, LOIFP = 0.00,PropertyOwnersLiability=0.00, PropertyOwnersLiabilityFP=0.00,LossOfRentalIncomeFP=0.00;
double PropertyExcessofLossFP=0.00;

String part1= "//*[@id='table0']/tbody";
String GrossPremium = null, BrokerCommission = null, NetPremium = null;

try{
	
	String brokerageAccount= driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+td+"]")).getText();
	String grossCommTotal =  (String)data_map.get("TS_GrossCommRate");
	
	
	if((String)data_map.get("CD_MaterialDamage")!= null ){
		GrossPremium = "0.00";
		BrokerCommission = "0.00";
		//double[] values =GetValuesTs("MaterialDamage",data_map);
		switch (common.currentRunningFlow){
			case "NB" :
			case "CAN" :
			case "Renewal" :
			case "Requote" :
				
				if(common_CCI.isNBRewindStarted){
					if(((String)data_map.get("CD_Add_MaterialDamage")).equalsIgnoreCase("Yes")){
						GrossPremium = (String)data_map.get("PS_MaterialDamage_GP");
						BrokerCommission = (String)data_map.get("PS_MaterialDamage_CR");
					}else{
						GrossPremium = "0.00";
						BrokerCommission = "0.00";
					}
				}else{
					
					if(((String)data_map.get("CD_MaterialDamage")).equalsIgnoreCase("Yes")){
						GrossPremium = (String)data_map.get("PS_MaterialDamage_GP");
						BrokerCommission = (String)data_map.get("PS_MaterialDamage_CR");
					}else{
						GrossPremium = "0.00";
						BrokerCommission = "0.00";
					}
				
				}
				break;
				
			case "MTA" :
				//If covers is marked yes in NB, MTA, rewind: - values will be from Rewind Map
				
				String coverNB = (String)common.NB_excel_data_map.get("CD_MaterialDamage");
				String coverMTA = (String)common.MTA_excel_data_map.get("CD_MaterialDamage");
				String coverMTARewind = (String)common.MTA_excel_data_map.get("CD_Add_MaterialDamage");
				
				//if(common.transaction_Details_Premium_Values.get("MaterialDamage")!=null){
				//if(((String)common.MTA_excel_data_map.get("CD_MaterialDamage")).equalsIgnoreCase("Yes")){
			/*		GrossPremium = "-" + Double.toString(  common.transaction_Details_Premium_Values.get("MaterialDamage").get("Gross Premium (GBP)"));
					BrokerCommission ="-" + Double.toString( common.transaction_Details_Premium_Values.get("MaterialDamage").get("Com. Rate (%)"));*/
					try{
						GrossPremium = Double.toString(common_CCI.transactionDetails_table_values.get("MaterialDamage_GP"));
					
						BrokerCommission = (String)common.MTA_excel_data_map.get("PS_MaterialDamage_CR");
					}catch(Throwable t){
						GrossPremium = "0.0";
						
						BrokerCommission = "0.0";
					
					}
					
				//}
//				if(coverNB.equalsIgnoreCase("Yes") && coverMTA.equalsIgnoreCase("Yes") && coverMTARewind.equalsIgnoreCase("Yes")){
//					// All Yes - values will be from MTA rewind Map
//				}else if(coverNB.equalsIgnoreCase("Yes") && coverMTA.equalsIgnoreCase("No") && coverMTARewind.equalsIgnoreCase("Yes")){
//					// values will be from MTA rewind Map
//				}else if(coverNB.equalsIgnoreCase("Yes") && coverMTA.equalsIgnoreCase("Yes") && coverMTARewind.equalsIgnoreCase("No")){
//					// values will be from MTA  Map but negative
//					
//				}else if(coverNB.equalsIgnoreCase("Yes") && coverMTA.equalsIgnoreCase("No") && coverMTARewind.equalsIgnoreCase("No")){
//					// Values will be from NB map but negative 
//					
//					GrossPremium = "-"+ (String)data_map.get("PS_MaterialDamage_GP");
//					BrokerCommission = "-" +(String)data_map.get("PS_MaterialDamage_CR");
//					
//				}else if(coverNB.equalsIgnoreCase("No") && coverMTA.equalsIgnoreCase("Yes") && coverMTARewind.equalsIgnoreCase("Yes")){
//					// Values will be from MTA rewind map:
//					
//				}else if(coverNB.equalsIgnoreCase("No") && coverMTA.equalsIgnoreCase("No") && coverMTARewind.equalsIgnoreCase("Yes")){
//					// Values will be from MTA rewind map:
//				}else if(coverNB.equalsIgnoreCase("No") && coverMTA.equalsIgnoreCase("Yes") && coverMTARewind.equalsIgnoreCase("No")){
//					// values will be from MTA  Map but negative
//					
//					GrossPremium = "-" + Double.toString(  common.transaction_Details_Premium_Values.get("MaterialDamage").get("Gross Premium (GBP)"));
//					BrokerCommission = "-" + Double.toString( common.transaction_Details_Premium_Values.get("MaterialDamage").get("Com. Rate (%)"));
//				}
				break;
			
			case "Rewind" :
				coverNB = (String)common.NB_excel_data_map.get("CD_MaterialDamage");
				String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_MaterialDamage");
				
				if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("Yes")){
					// Values will be from rewind map
				}else if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("No")){
					// Values will be from NB map but negative 
					
					GrossPremium = "-"+ (String)data_map.get("PS_MaterialDamage_GP");
					BrokerCommission = "-" +(String)data_map.get("PS_MaterialDamage_CR");
					
				}else if(coverNB.equalsIgnoreCase("No") && coverNBRewind.equalsIgnoreCase("Yes")){
					// Values will be from rewind map
				}
		}
		
		materialDamageFP = calculateGeneralPremium(GrossPremium,BrokerCommission,grossCommTotal);				
	}
	
	
	if((String)data_map.get("CD_BusinessInterruption")!= null){
		
		GrossPremium = "0.00";
		BrokerCommission = "0.00";
		switch (common.currentRunningFlow){
		case "NB" :
		case "CAN" :
		case "Renewal" :
		case "Requote" :
			
			if(common_CCI.isNBRewindStarted){
				if(((String)data_map.get("CD_Add_BusinessInterruption")).equalsIgnoreCase("Yes")){
					GrossPremium = (String)data_map.get("PS_BusinessInterruption_GP");
					BrokerCommission = (String)data_map.get("PS_BusinessInterruption_CR");
				}else{
					GrossPremium = "0.00";
					BrokerCommission = "0.00";
				}
			}else{
				
				if(((String)data_map.get("CD_BusinessInterruption")).equalsIgnoreCase("Yes")){
					GrossPremium = (String)data_map.get("PS_BusinessInterruption_GP");
					BrokerCommission = (String)data_map.get("PS_BusinessInterruption_CR");
				}else{
					GrossPremium = "0.00";
					BrokerCommission = "0.00";
				}
				
			}
			break;
			
		case "MTA" :
			//If covers is marked yes in NB, MTA, rewind: - values will be from Rewind Map
			
			String coverNB = (String)common.NB_excel_data_map.get("CD_BusinessInterruption");
			String coverMTA = (String)common.MTA_excel_data_map.get("CD_BusinessInterruption");
			String coverMTARewind = (String)common.MTA_excel_data_map.get("CD_BusinessInterruption");
			
			
			//if(common.transaction_Details_Premium_Values.get("BusinessInterruption")!=null){
			try{
				GrossPremium = Double.toString(common_CCI.transactionDetails_table_values.get("BusinessInterruption_GP"));
			
				BrokerCommission = (String)common.MTA_excel_data_map.get("PS_BusinessInterruption_CR");
			}catch(Throwable t){
				GrossPremium = "0.0";
				
				BrokerCommission = "0.0";
			
			}

			break;
		
		case "Rewind" :
			coverNB = (String)common.NB_excel_data_map.get("CD_BusinessInterruption");
			String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_BusinessInterruption");
			
			if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}else if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("No")){
				// Values will be from NB map but negative 
				
				GrossPremium = "-"+ (String)data_map.get("PS_BusinessInterruption_GP");
				BrokerCommission = "-" +(String)data_map.get("PS_BusinessInterruption_CR");
				
			}else if(coverNB.equalsIgnoreCase("No") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}
	}
	
		businessInteruptionFP = calculateGeneralPremium(GrossPremium,BrokerCommission,grossCommTotal);	
	}
	
	if((String)data_map.get("CD_Liability")!= null){
		GrossPremium = "0.00";
		BrokerCommission = "0.00";
		switch (common.currentRunningFlow){
		case "NB" :
		case "CAN" :
		case "Renewal" :
		case "Requote" :
			if(common_CCI.isNBRewindStarted){
				if(((String)data_map.get("CD_Add_Liability")).equalsIgnoreCase("Yes")){
					GrossPremium = (String)data_map.get("PS_EmployersLiability_GP");
					BrokerCommission = (String)data_map.get("PS_EmployersLiability_CR");
				}else{
					GrossPremium = "0.00";
					BrokerCommission = "0.00";
				}
			}else{
				if(((String)data_map.get("CD_Liability")).equalsIgnoreCase("Yes")){
					GrossPremium = (String)data_map.get("PS_EmployersLiability_GP");
					BrokerCommission = (String)data_map.get("PS_EmployersLiability_CR");
				}else{
					GrossPremium = "0.00";
					BrokerCommission = "0.00";
				}
			}
			break;
			
		case "MTA" :
			//If covers is marked yes in NB, MTA, rewind: - values will be from Rewind Map
			
			String coverNB = (String)common.NB_excel_data_map.get("CD_EmployersLiability");
			String coverMTA = (String)common.MTA_excel_data_map.get("CD_EmployersLiability");
			String coverMTARewind = (String)common.MTA_excel_data_map.get("CD_EmployersLiability");
			
		
			try{
				GrossPremium = Double.toString(common_CCI.transactionDetails_table_values.get("EmployersLiability_GP"));
			
				BrokerCommission = (String)common.MTA_excel_data_map.get("PS_EmployersLiability_CR");
			}catch(Throwable t){
				GrossPremium = "0.0";
				
				BrokerCommission = "0.0";
			
			}

			break;
		
		case "Rewind" :
			coverNB = (String)common.NB_excel_data_map.get("CD_EmployersLiability");
			String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_EmployersLiability");
			
			if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}else if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("No")){
				// Values will be from NB map but negative 
				
				GrossPremium = "-"+ (String)data_map.get("PS_EmployersLiability_GP");
				BrokerCommission = "-" +(String)data_map.get("PS_EmployersLiability_CR");
				
			}else if(coverNB.equalsIgnoreCase("No") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}
	}
	
		EmployersLiabiliyFP = calculateGeneralPremium(GrossPremium,BrokerCommission,grossCommTotal);	
	}		

	if(!TestBase.product.equalsIgnoreCase("POE")){
		
		if((String)data_map.get("CD_Liability")!= null){
			GrossPremium = "0.00";
			BrokerCommission = "0.00";
			switch (common.currentRunningFlow){
			case "NB" :
			case "CAN" :
			case "Renewal" :
			case "Requote" :
				if(common_CCI.isNBRewindStarted){
					if(((String)data_map.get("CD_Add_Liability")).equalsIgnoreCase("Yes")){
						GrossPremium = (String)data_map.get("PS_PublicLiability_GP");
						BrokerCommission = (String)data_map.get("PS_PublicLiability_CR");
					}else{
						GrossPremium = "0.00";
						BrokerCommission = "0.00";
					}
				}else{
					if(((String)data_map.get("CD_Liability")).equalsIgnoreCase("Yes")){
						GrossPremium = (String)data_map.get("PS_PublicLiability_GP");
						BrokerCommission = (String)data_map.get("PS_PublicLiability_CR");
					}else{
						GrossPremium = "0.00";
						BrokerCommission = "0.00";
					}
				}
				break;
				
			case "MTA" :
				//If covers is marked yes in NB, MTA, rewind: - values will be from Rewind Map
				
				String coverNB = (String)common.NB_excel_data_map.get("CD_PublicLiability");
				String coverMTA = (String)common.MTA_excel_data_map.get("CD_PublicLiability");
				String coverMTARewind = (String)common.MTA_excel_data_map.get("CD_PublicLiability");
				
				//if(((String)common.MTA_excel_data_map.get("CD_Liability")).equalsIgnoreCase("Yes")){
					
					try{
						GrossPremium = Double.toString(common_CCI.transactionDetails_table_values.get("PublicLiability_GP"));
					
						BrokerCommission = (String)common.MTA_excel_data_map.get("PS_PublicLiability_CR");
					}catch(Throwable t){
						GrossPremium = "0.0";
						
						BrokerCommission = "0.0";
					
					}
	
				break;
			
			case "Rewind" :
				coverNB = (String)common.NB_excel_data_map.get("CD_PublicLiability");
				String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_PublicLiability");
				
				if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("Yes")){
					// Values will be from rewind map
				}else if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("No")){
					// Values will be from NB map but negative 
					
					GrossPremium = "-"+ (String)data_map.get("PS_PublicLiability_GP");
					BrokerCommission = "-" +(String)data_map.get("PS_PublicLiability_CR");
					
				}else if(coverNB.equalsIgnoreCase("No") && coverNBRewind.equalsIgnoreCase("Yes")){
					// Values will be from rewind map
				}
		}
		
			PublicLiabilityFP = calculateGeneralPremium(GrossPremium,BrokerCommission,grossCommTotal);	
		}
		
		if((String)data_map.get("CD_Liability")!= null){
			GrossPremium = "0.00";
			BrokerCommission = "0.00";
			switch (common.currentRunningFlow){
			case "NB" :
			case "CAN" :
			case "Renewal" :
			case "Requote" :
				if(common_CCI.isNBRewindStarted){
					if(((String)data_map.get("CD_Add_Liability")).equalsIgnoreCase("Yes")){
						GrossPremium = (String)data_map.get("PS_ProductsLiability_GP");
						BrokerCommission = (String)data_map.get("PS_ProductsLiability_CR");
					}else{
						GrossPremium = "0.00";
						BrokerCommission = "0.00";
					}
				}else{
					if(((String)data_map.get("CD_Liability")).equalsIgnoreCase("Yes")){
						GrossPremium = (String)data_map.get("PS_ProductsLiability_GP");
						BrokerCommission = (String)data_map.get("PS_ProductsLiability_CR");
					}else{
						GrossPremium = "0.00";
						BrokerCommission = "0.00";
					}
				}
				
				
				break;
				
			case "MTA" :
				//If covers is marked yes in NB, MTA, rewind: - values will be from Rewind Map
				
				String coverNB = (String)common.NB_excel_data_map.get("CD_ProductsLiability");
				String coverMTA = (String)common.MTA_excel_data_map.get("CD_ProductsLiability");
				String coverMTARewind = (String)common.MTA_excel_data_map.get("CD_ProductsLiability");
				
				//if(((String)common.MTA_excel_data_map.get("CD_Liability")).equalsIgnoreCase("Yes")){
					
					try{
						GrossPremium = Double.toString(common_CCI.transactionDetails_table_values.get("ProductsLiability_GP"));
					
						BrokerCommission = (String)common.MTA_excel_data_map.get("PS_ProductsLiability_CR");
					}catch(Throwable t){
						GrossPremium = "0.0";
						
						BrokerCommission = "0.0";
					
					}

				break;
			
			case "Rewind" :
				coverNB = (String)common.NB_excel_data_map.get("CD_ProductsLiability");
				String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_ProductsLiability");
				
				if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("Yes")){
					// Values will be from rewind map
				}else if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("No")){
					// Values will be from NB map but negative 
					
					GrossPremium = "-"+ (String)data_map.get("PS_ProductsLiability_GP");
					BrokerCommission = "-" +(String)data_map.get("PS_ProductsLiability_CR");
					
				}else if(coverNB.equalsIgnoreCase("No") && coverNBRewind.equalsIgnoreCase("Yes")){
					// Values will be from rewind map
				}
			}
		
			ProductLiability = calculateGeneralPremium(GrossPremium,BrokerCommission,grossCommTotal);	
		}
	}
	
	
	
	if((String)data_map.get("CD_SpecifiedAllRisks")!= null){
		GrossPremium = "0.00";
		BrokerCommission = "0.00";
		switch (common.currentRunningFlow){
		case "NB" :
		case "CAN" :
		case "Renewal" :
		case "Requote" :
			if(common_CCI.isNBRewindStarted){
			if(((String)data_map.get("CD_Add_SpecifiedAllRisks")).equalsIgnoreCase("Yes")){
				GrossPremium = (String)data_map.get("PS_SpecifiedAllRisks_GP");
				BrokerCommission = (String)data_map.get("PS_SpecifiedAllRisks_CR");
			}else{
				GrossPremium = "0.00";
				BrokerCommission = "0.00";
			}
			}else{
				if(((String)data_map.get("CD_SpecifiedAllRisks")).equalsIgnoreCase("Yes")){
					GrossPremium = (String)data_map.get("PS_SpecifiedAllRisks_GP");
					BrokerCommission = (String)data_map.get("PS_SpecifiedAllRisks_CR");
				}else{
					GrossPremium = "0.00";
					BrokerCommission = "0.00";
				}
			}
			break;
			
		case "MTA" :
			//If covers is marked yes in NB, MTA, rewind: - values will be from Rewind Map
			
			String coverNB = (String)common.NB_excel_data_map.get("CD_SpecifiedAllRisks");
			String coverMTA = (String)common.MTA_excel_data_map.get("CD_SpecifiedAllRisks");
			String coverMTARewind = (String)common.MTA_excel_data_map.get("CD_SpecifiedAllRisks");
			
		//	if(coverMTA.equalsIgnoreCase("Yes")){
					try{
						GrossPremium = Double.toString(common_CCI.transactionDetails_table_values.get("SpecifiedAllRisks_GP"));
					
						BrokerCommission = (String)common.MTA_excel_data_map.get("PS_SpecifiedAllRisks_CR");
					}catch(Throwable t){
						GrossPremium = "0.0";
						
						BrokerCommission = "0.0";
					
					}
	
			break;
		
		case "Rewind" :
			coverNB = (String)common.NB_excel_data_map.get("CD_SpecifiedAllRisks");
			String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_SpecifiedAllRisks");
			
			if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}else if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("No")){
				// Values will be from NB map but negative 
				
				GrossPremium = "-"+ (String)data_map.get("PS_SpecifiedAllRisks_GP");
				BrokerCommission = "-" +(String)data_map.get("PS_SpecifiedAllRisks_CR");
				
			}else if(coverNB.equalsIgnoreCase("No") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}
		}
	
		SpecifiedRisksFP = calculateGeneralPremium(GrossPremium,BrokerCommission,grossCommTotal);	
	}
	
	if((String)data_map.get("CD_ComputersandElectronicRisks")!= null){
		switch (common.currentRunningFlow){
		case "NB" :
		case "CAN" :
		case "Renewal" :
		case "Requote" :
			if(common_CCI.isNBRewindStarted){
			if(((String)data_map.get("CD_Add_ComputersandElectronicRisks")).equalsIgnoreCase("Yes")){
				GrossPremium = (String)data_map.get("PS_ComputersandElectronicRisks_GP");
				BrokerCommission = (String)data_map.get("PS_ComputersandElectronicRisks_CR");
			}else{
				GrossPremium = "0.00";
				BrokerCommission = "0.00";
			}
			}else{
				if(((String)data_map.get("CD_ComputersandElectronicRisks")).equalsIgnoreCase("Yes")){
					GrossPremium = (String)data_map.get("PS_ComputersandElectronicRisks_GP");
					BrokerCommission = (String)data_map.get("PS_ComputersandElectronicRisks_CR");
				}else{
					GrossPremium = "0.00";
					BrokerCommission = "0.00";
				}
			}
			break;
			
		case "MTA" :
			//If covers is marked yes in NB, MTA, rewind: - values will be from Rewind Map
			
			String coverNB = (String)common.NB_excel_data_map.get("CD_ComputersandElectronicRisks");
			String coverMTA = (String)common.MTA_excel_data_map.get("CD_ComputersandElectronicRisks");
			String coverMTARewind = (String)common.MTA_excel_data_map.get("CD_ComputersandElectronicRisks");
			
			//if(coverMTA.equalsIgnoreCase("Yes")){
				try{
					GrossPremium = Double.toString(common_CCI.transactionDetails_table_values.get("ComputersandElectronicRisks_GP"));
				
					BrokerCommission = (String)common.MTA_excel_data_map.get("PS_ComputersandElectronicRisks_CR");
				}catch(Throwable t){
					GrossPremium = "0.0";
					
					BrokerCommission = "0.0";
				
				}
	
			break;
		
		case "Rewind" :
			coverNB = (String)common.NB_excel_data_map.get("CD_ComputersandElectronicRisks");
			String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_ComputersandElectronicRisks");
			
			if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}else if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("No")){
				// Values will be from NB map but negative 
				
				GrossPremium = "-"+ (String)data_map.get("PS_ComputersandElectronicRisks_GP");
				BrokerCommission = "-" +(String)data_map.get("PS_ComputersandElectronicRisks_CR");
				
			}else if(coverNB.equalsIgnoreCase("No") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}
		}
	
		ComputersAndElectronicRiskFP = calculateGeneralPremium(GrossPremium,BrokerCommission,grossCommTotal);	
	}
	
	if((String)data_map.get("CD_Money")!= null){
		GrossPremium = "0.00";
		BrokerCommission = "0.00";
		switch (common.currentRunningFlow){
		case "NB" :
		case "CAN" :
		case "Renewal" :
		case "Requote" :
			if(common_CCI.isNBRewindStarted){
			if(((String)data_map.get("CD_Add_Money")).equalsIgnoreCase("Yes")){
				GrossPremium = (String)data_map.get("PS_Money_GP");
				BrokerCommission = (String)data_map.get("PS_Money_CR");
			}else{
				GrossPremium = "0.00";
				BrokerCommission = "0.00";
			}
			}else{
				if(((String)data_map.get("CD_Money")).equalsIgnoreCase("Yes")){
					GrossPremium = (String)data_map.get("PS_Money_GP");
					BrokerCommission = (String)data_map.get("PS_Money_CR");
				}else{
					GrossPremium = "0.00";
					BrokerCommission = "0.00";
				}
			}
			break;
			
		case "MTA" :
			//If covers is marked yes in NB, MTA, rewind: - values will be from Rewind Map
			
			String coverNB = (String)common.NB_excel_data_map.get("CD_Money");
			String coverMTA = (String)common.MTA_excel_data_map.get("CD_Money");
			String coverMTARewind = (String)common.MTA_excel_data_map.get("CD_Money");
			
			//if(coverMTA.equalsIgnoreCase("Yes")){
				try{
					GrossPremium = Double.toString(common_CCI.transactionDetails_table_values.get("Money_GP"));
				
					BrokerCommission = (String)common.MTA_excel_data_map.get("PS_Money_CR");
				}catch(Throwable t){
					GrossPremium = "0.0";
					
					BrokerCommission = "0.0";
				
				}
				
	
			break;
		
		case "Rewind" :
			coverNB = (String)common.NB_excel_data_map.get("CD_Money");
			String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_Money");
			
			if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}else if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("No")){
				// Values will be from NB map but negative 
				
				GrossPremium = "-"+ (String)data_map.get("PS_Money_GP");
				BrokerCommission = "-" +(String)data_map.get("PS_Money_CR");
				
			}else if(coverNB.equalsIgnoreCase("No") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}
		}
	
		MoneyFP = calculateGeneralPremium(GrossPremium,BrokerCommission,grossCommTotal);	
	}
	
	if((String)data_map.get("CD_GoodsInTransit")!= null){
		GrossPremium = "0.00";
		BrokerCommission = "0.00";
		switch (common.currentRunningFlow){
		case "NB" :
		case "CAN" :
		case "Renewal" :
		case "Requote" :
			if(common_CCI.isNBRewindStarted){
			if(((String)data_map.get("CD_Add_GoodsInTransit")).equalsIgnoreCase("Yes")){
				GrossPremium = (String)data_map.get("PS_GoodsInTransit_GP");
				BrokerCommission = (String)data_map.get("PS_GoodsInTransit_CR");
			}else{
				GrossPremium = "0.00";
				BrokerCommission = "0.00";
			}
			}else{
				if(((String)data_map.get("CD_GoodsInTransit")).equalsIgnoreCase("Yes")){
					GrossPremium = (String)data_map.get("PS_GoodsInTransit_GP");
					BrokerCommission = (String)data_map.get("PS_GoodsInTransit_CR");
				}else{
					GrossPremium = "0.00";
					BrokerCommission = "0.00";
				}
			}
			break;
			
		case "MTA" :
			//If covers is marked yes in NB, MTA, rewind: - values will be from Rewind Map
			
			String coverNB = (String)common.NB_excel_data_map.get("CD_GoodsInTransit");
			String coverMTA = (String)common.MTA_excel_data_map.get("CD_GoodsInTransit");
			String coverMTARewind = (String)common.MTA_excel_data_map.get("CD_GoodsInTransit");
			
			//if(coverMTA.equalsIgnoreCase("Yes")){
				try{
					GrossPremium = Double.toString(common_CCI.transactionDetails_table_values.get("GoodsInTransit_GP"));
				
					BrokerCommission = (String)common.MTA_excel_data_map.get("PS_GoodsInTransit_CR");
				}catch(Throwable t){
					GrossPremium = "0.0";
					
					BrokerCommission = "0.0";
				
				}
	
			break;
		
		case "Rewind" :
			coverNB = (String)common.NB_excel_data_map.get("CD_GoodsInTransit");
			String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_GoodsInTransit");
			
			if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}else if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("No")){
				// Values will be from NB map but negative 
				
				GrossPremium = "-"+ (String)data_map.get("PS_GoodsInTransit_GP");
				BrokerCommission = "-" +(String)data_map.get("PS_GoodsInTransit_CR");
				
			}else if(coverNB.equalsIgnoreCase("No") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}
		}
	
		GoodsInTansitFP = calculateGeneralPremium(GrossPremium,BrokerCommission,grossCommTotal);	
	}
	
	if((String)data_map.get("CD_LossofLicence")!= null){
		GrossPremium = "0.00";
		BrokerCommission = "0.00";
		switch (common.currentRunningFlow){
		case "NB" :
		case "CAN" :
		case "Renewal" :
		case "Requote" :
			if(common_CCI.isNBRewindStarted){
			if(((String)data_map.get("CD_Add_LossofLicence")).equalsIgnoreCase("Yes")){
				GrossPremium = (String)data_map.get("PS_LossOfLicence_GP");
				BrokerCommission = (String)data_map.get("PS_LossOfLicence_CR");
			}else{
				GrossPremium = "0.00";
				BrokerCommission = "0.00";
			}
			}else{
				if(((String)data_map.get("CD_LossofLicence")).equalsIgnoreCase("Yes")){
					GrossPremium = (String)data_map.get("PS_LossOfLicence_GP");
					BrokerCommission = (String)data_map.get("PS_LossOfLicence_CR");
				}else{
					GrossPremium = "0.00";
					BrokerCommission = "0.00";
				}
			}
			break;
			
		case "MTA" :
			//If covers is marked yes in NB, MTA, rewind: - values will be from Rewind Map
			
			String coverNB = (String)common.NB_excel_data_map.get("CD_LossofLicence");
			String coverMTA = (String)common.MTA_excel_data_map.get("CD_LossofLicence");
			String coverMTARewind = (String)common.MTA_excel_data_map.get("CD_Add_LossofLicence");
			
			
				try{
					GrossPremium = Double.toString(common_CCI.transactionDetails_table_values.get("LossOfLicence_GP"));
				
					BrokerCommission = (String)common.MTA_excel_data_map.get("PS_LossOfLicence_CR");
				}catch(Throwable t){
					GrossPremium = "0.0";
					
					BrokerCommission = "0.0";
				
				}
	
			break;
		
		case "Rewind" :
			coverNB = (String)common.NB_excel_data_map.get("CD_LossofLicence");
			String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_LossofLicence");
			
			if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}else if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("No")){
				// Values will be from NB map but negative 
				
				GrossPremium = "-"+ (String)data_map.get("PS_LossOfLicence_GP");
				BrokerCommission = "-" +(String)data_map.get("PS_LossOfLicence_CR");
				
			}else if(coverNB.equalsIgnoreCase("No") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}
		}
	
		LossOfLicenceFP = calculateGeneralPremium(GrossPremium,BrokerCommission,grossCommTotal);	
	}
	
	if((String)data_map.get("CD_FrozenFood")!= null){
		GrossPremium = "0.00";
		BrokerCommission = "0.00";
		switch (common.currentRunningFlow){
		case "NB" :
		case "CAN" :
		case "Renewal" :
		case "Requote" :
			if(common_CCI.isNBRewindStarted){
			if(((String)data_map.get("CD_Add_FrozenFood")).equalsIgnoreCase("Yes")){
				GrossPremium = (String)data_map.get("PS_FrozenFoods_GP");
				BrokerCommission = (String)data_map.get("PS_FrozenFoods_CR");
			}else{
				GrossPremium = "0.00";
				BrokerCommission = "0.00";
			}
			}else{
				if(((String)data_map.get("CD_FrozenFood")).equalsIgnoreCase("Yes")){
					GrossPremium = (String)data_map.get("PS_FrozenFoods_GP");
					BrokerCommission = (String)data_map.get("PS_FrozenFoods_CR");
				}else{
					GrossPremium = "0.00";
					BrokerCommission = "0.00";
				}
			}
			break;
			
		case "MTA" :
			//If covers is marked yes in NB, MTA, rewind: - values will be from Rewind Map
			
			String coverNB = (String)common.NB_excel_data_map.get("CD_FrozenFood");
			String coverMTA = (String)common.MTA_excel_data_map.get("CD_FrozenFood");
			String coverMTARewind = (String)common.MTA_excel_data_map.get("CD_FrozenFood");
			
			//if(coverMTA.equalsIgnoreCase("Yes")){
				try{
					GrossPremium = Double.toString(common_CCI.transactionDetails_table_values.get("FrozenFoods_GP"));
				
					BrokerCommission = (String)common.MTA_excel_data_map.get("PS_FrozenFoods_CR");
				}catch(Throwable t){
					GrossPremium = "0.0";
					
					BrokerCommission = "0.0";
				
				}
		
			break;
		
		case "Rewind" :
			coverNB = (String)common.NB_excel_data_map.get("CD_FrozenFood");
			String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_FrozenFood");
			
			if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}else if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("No")){
				// Values will be from NB map but negative 
				
				GrossPremium = "-"+ (String)data_map.get("PS_FrozenFoods_GP");
				BrokerCommission = "-" +(String)data_map.get("PS_FrozenFoods_CR");
				
			}else if(coverNB.equalsIgnoreCase("No") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}
		}
	
		FrozenFoodsFP = calculateGeneralPremium(GrossPremium,BrokerCommission,grossCommTotal);	
	}
	
	if((String)data_map.get("CD_FidelityGuarantee")!= null){
		GrossPremium = "0.00";
		BrokerCommission = "0.00";
		switch (common.currentRunningFlow){
		case "NB" :
		case "CAN" :
		case "Renewal" :
		case "Requote" :
			if(common_CCI.isNBRewindStarted){
				if(((String)data_map.get("CD_Add_FidelityGuarantee")).equalsIgnoreCase("Yes")){
					GrossPremium = (String)data_map.get("PS_FidelityGuarantee_GP");
					BrokerCommission = (String)data_map.get("PS_FidelityGuarantee_CR");
				}else{
					GrossPremium = "0.00";
					BrokerCommission = "0.00";
				}
			}else{
				if(((String)data_map.get("CD_FidelityGuarantee")).equalsIgnoreCase("Yes")){
					if(TestBase.product.contains("CCI")){
						if(common.currentRunningFlow.contains("CAN")){
							GrossPremium = String.valueOf(common_CCI.CAN_CCI_ReturnP_Values_Map.get("Fidelity Guarantee").get("Gross Premium (GBP)"));
							BrokerCommission = String.valueOf(common_CCI.CAN_CCI_ReturnP_Values_Map.get("Fidelity Guarantee").get("Com. Rate (%)"));
						}else{
							GrossPremium = (String)data_map.get("PS_FidelityGuarantee_GP");
							BrokerCommission = (String)data_map.get("PS_FidelityGuarantee_CR");
						}
					}else{
						GrossPremium = (String)data_map.get("PS_FidelityGuarantee_GP");
						BrokerCommission = (String)data_map.get("PS_FidelityGuarantee_CR");
					}
						
				}else{
						GrossPremium = "0.00";
						BrokerCommission = "0.00";
				}
			}
			break;
			
		case "MTA" :
			//If covers is marked yes in NB, MTA, rewind: - values will be from Rewind Map
			
			String coverNB = (String)common.NB_excel_data_map.get("CD_FidelityGuarantee");
			String coverMTA = (String)common.MTA_excel_data_map.get("CD_FidelityGuarantee");
			String coverMTARewind = (String)common.MTA_excel_data_map.get("CD_FidelityGuarantee");
			
			//if(coverMTA.equalsIgnoreCase("Yes")){
				try{
					GrossPremium = Double.toString(common_CCI.transactionDetails_table_values.get("FidelityGuarantee_GP"));
				
					BrokerCommission = (String)common.MTA_excel_data_map.get("PS_FidelityGuarantee_CR");
				}catch(Throwable t){
					GrossPremium = "0.0";
					
					BrokerCommission = "0.0";
				
				}
	
			break;
		
		case "Rewind" :
			coverNB = (String)common.NB_excel_data_map.get("CD_FidelityGuarantee");
			String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_FidelityGuarantee");
			
			if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}else if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("No")){
				// Values will be from NB map but negative 
				
				GrossPremium = "-"+ (String)data_map.get("PS_FidelityGuarantee_GP");
				BrokerCommission = "-" +(String)data_map.get("PS_FidelityGuarantee_CR");
				
			}else if(coverNB.equalsIgnoreCase("No") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}
		}
	
		FidilityGuaranteFP = calculateGeneralPremium(GrossPremium,BrokerCommission,grossCommTotal);	
	}
	
	if((String)data_map.get("CD_Terrorism")!= null){
		GrossPremium = "0.00";
		BrokerCommission = "0.00";
		String cedeComm = null;
		switch (common.currentRunningFlow){
		case "NB" :			
		case "Renewal" :
		case "Requote" :
			if(common_CCI.isNBRewindStarted){
			if(((String)data_map.get("CD_Add_Terrorism")).equalsIgnoreCase("Yes")){
				GrossPremium = (String)data_map.get("PS_Terrorism_GP");
				NetPremium = (String)data_map.get("PS_Terrorism_NP");
			}else{
				GrossPremium = "0.00";
				NetPremium = "0.00";
			}
			}else{
				if(((String)data_map.get("CD_Terrorism")).equalsIgnoreCase("Yes")){
					GrossPremium = (String)data_map.get("PS_Terrorism_GP");
					NetPremium = (String)data_map.get("PS_Terrorism_NP");
				}else{
					GrossPremium = "0.00";
					NetPremium = "0.00";
				}		
			}
			cedeComm = (String)data_map.get("TER_CedeComm");
			break;
			
		case "CAN" :
			if(common.currentRunningFlow.contains("NB")){
				GrossPremium = (String)data_map.get("PS_Terrorism_GP");
				NetPremium = (String)data_map.get("PS_Terrorism_NP");
				cedeComm = (String)data_map.get("TER_CedeComm");
			}else{
				NetPremium = String.valueOf(common_CCI.CAN_CCI_ReturnP_Values_Map.get("Terrorism").get("Net Premium (GBP)"));
				GrossPremium = String.valueOf(common_CCI.CAN_CCI_ReturnP_Values_Map.get("Terrorism").get("Gross Premium (GBP)"));
				cedeComm = (String)data_map.get("TER_CedeComm");
			}
			break;
		case "MTA" :
			//If covers is marked yes in NB, MTA, rewind: - values will be from Rewind Map
			
			String coverNB = (String)common.NB_excel_data_map.get("CD_Terrorism");
			String coverMTA = (String)common.MTA_excel_data_map.get("CD_Terrorism");
			String coverMTARewind = (String)common.MTA_excel_data_map.get("CD_Terrorism");
			cedeComm = (String)common.MTA_excel_data_map.get("TER_CedeComm");
			
			//if(coverMTA.equalsIgnoreCase("Yes")){
				try{
					GrossPremium = Double.toString(common_CCI.transactionDetails_table_values.get("Terrorism_GP"));
				
					BrokerCommission = (String)common.MTA_excel_data_map.get("PS_Terrorism_CR");
					
					NetPremium = Double.toString(common_CCI.transactionDetails_table_values.get("Terrorism_NP"));
				}catch(Throwable t){
					GrossPremium = "0.0";
					
					BrokerCommission = "0.0";
					NetPremium = "0.0";
				}
	
			break;
		
		case "Rewind" :
			coverNB = (String)common.NB_excel_data_map.get("CD_Terrorism");
			String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_Terrorism");
			cedeComm = (String)common.Rewind_excel_data_map.get("TER_CedeComm");
			
			if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}else if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("No")){
				// Values will be from NB map but negative 
				
				GrossPremium = "-"+ (String)data_map.get("PS_Terrorism_GP");
				NetPremium = "-" +(String)data_map.get("PS_Terrorism_NP");
				
			}else if(coverNB.equalsIgnoreCase("No") && coverNBRewind.equalsIgnoreCase("Yes")){
				// Values will be from rewind map
			}
		}
	
		if(cedeComm.equalsIgnoreCase("Yes")){
			terrorismFP = 0.00;
		}else{				
			terrorismFP= Double.parseDouble(NetPremium) * 0.1;
		}	
	}
	
	generalPremium= (materialDamageFP + businessInteruptionFP + EmployersLiabiliyFP 
			+ PublicLiabilityFP +  ContractorAllRisksFP + SpecifiedRisksFP + ProductLiability 
			+ ComputersAndElectronicRiskFP + MoneyFP + GoodsInTansitFP + PropertyOwnersLiabilityFP + LOIFP + FidilityGuaranteFP + LossOfLicenceFP + FrozenFoodsFP) + terrorismFP ;
	
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
	String GrossPremium = null, BrokerCommission = null, NetPremium = null;
	
	if(!sCoverName.contains("Terrorism")){
		if((String)data_map.get("CD_"+sCoverName)!= null){
			switch (common.currentRunningFlow){
			case "NB" :
			case "CAN" :
			case "Renewal" :
			case "Requote" :
					GrossPremium = (String)data_map.get("PS_"+sCoverName+"_GP");
					BrokerCommission = (String)data_map.get("PS_"+sCoverName+"_CR");
				break;
				
			case "MTA" :
				//If covers is marked yes in NB, MTA, rewind: - values will be from Rewind Map
				
				String coverNB = (String)common.NB_excel_data_map.get("CD_"+sCoverName);
				String coverMTA = (String)common.MTA_excel_data_map.get("CD_"+sCoverName);
				String coverMTARewind = (String)common.MTA_excel_data_map.get("CD_"+sCoverName);
				
				if(coverNB.equalsIgnoreCase("Yes") && coverMTA.equalsIgnoreCase("Yes") && coverMTARewind.equalsIgnoreCase("Yes")){
					// All Yes - values will be from MTA rewind Map
				}else if(coverNB.equalsIgnoreCase("Yes") && coverMTA.equalsIgnoreCase("No") && coverMTARewind.equalsIgnoreCase("Yes")){
					// values will be from MTA rewind Map
				}else if(coverNB.equalsIgnoreCase("Yes") && coverMTA.equalsIgnoreCase("Yes") && coverMTARewind.equalsIgnoreCase("No")){
					// values will be from MTA  Map but negative
					
					GrossPremium = "-" + Double.toString(  common.transaction_Details_Premium_Values.get(sCoverName).get("Gross Premium (GBP)"));
					BrokerCommission = "-" + Double.toString( common.transaction_Details_Premium_Values.get(sCoverName).get("Com. Rate (%)"));
					
				}else if(coverNB.equalsIgnoreCase("Yes") && coverMTA.equalsIgnoreCase("No") && coverMTARewind.equalsIgnoreCase("No")){
					// Values will be from NB map but negative 
					
					GrossPremium = "-"+ (String)data_map.get("PS_"+sCoverName+"_GP");
					BrokerCommission = "-" +(String)data_map.get("PS_"+sCoverName+"_CR");
					
				}else if(coverNB.equalsIgnoreCase("No") && coverMTA.equalsIgnoreCase("Yes") && coverMTARewind.equalsIgnoreCase("Yes")){
					// Values will be from MTA rewind map:
					
				}else if(coverNB.equalsIgnoreCase("No") && coverMTA.equalsIgnoreCase("No") && coverMTARewind.equalsIgnoreCase("Yes")){
					// Values will be from MTA rewind map:
				}else if(coverNB.equalsIgnoreCase("No") && coverMTA.equalsIgnoreCase("Yes") && coverMTARewind.equalsIgnoreCase("No")){
					// values will be from MTA  Map but negative
					
					GrossPremium = "-" + Double.toString(  common.transaction_Details_Premium_Values.get(sCoverName).get("Gross Premium (GBP)"));
					BrokerCommission = "-" + Double.toString( common.transaction_Details_Premium_Values.get(sCoverName).get("Com. Rate (%)"));
				}
				break;
			
			case "Rewind" :
				coverNB = (String)common.NB_excel_data_map.get("CD_"+sCoverName);
				String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_"+sCoverName);
				
				if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("Yes")){
					// Values will be from rewind map
				}else if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("No")){
					// Values will be from NB map but negative 
					
					GrossPremium = "-"+ (String)data_map.get("PS_"+sCoverName+"_GP");
					BrokerCommission = "-" +(String)data_map.get("PS_"+sCoverName+"_CR");
					
				}else if(coverNB.equalsIgnoreCase("No") && coverNBRewind.equalsIgnoreCase("Yes")){
					// Values will be from rewind map
				}
			}
		}
	}else{
		if((String)data_map.get("CD_"+sCoverName)!= null){
			String cedeComm = null;
			switch (common.currentRunningFlow){
			case "NB" :			
			case "Renewal" :
			case "Requote" :
				GrossPremium = (String)data_map.get("PS_"+sCoverName+"_GP");
				NetPremium = (String)data_map.get("PS_"+sCoverName+"_NP");
				cedeComm = (String)data_map.get("TER_CedeComm");
				break;
				
			case "CAN" :
				if(common.currentRunningFlow.contains("NB")){
					GrossPremium = (String)data_map.get("PS_"+sCoverName+"_GP");
					NetPremium = (String)data_map.get("PS_"+sCoverName+"_NP");
					cedeComm = (String)data_map.get("TER_CedeComm");
				}else{
					NetPremium = String.valueOf(common_CCI.CAN_CCI_ReturnP_Values_Map.get(sCoverName).get("Net Premium"));
					GrossPremium = String.valueOf(common_CCI.CAN_CCI_ReturnP_Values_Map.get(sCoverName).get("Gross Premium"));
				}
				break;
			case "MTA" :
				//If covers is marked yes in NB, MTA, rewind: - values will be from Rewind Map
				
				String coverNB = (String)common.NB_excel_data_map.get("CD_"+sCoverName);
				String coverMTA = (String)common.MTA_excel_data_map.get("CD_"+sCoverName);
				String coverMTARewind = (String)common.MTA_excel_data_map.get("CD_"+sCoverName);
				cedeComm = (String)common.MTA_excel_data_map.get("TER_CedeComm");
				
				if(coverNB.equalsIgnoreCase("Yes") && coverMTA.equalsIgnoreCase("Yes") && coverMTARewind.equalsIgnoreCase("Yes")){
					// All Yes - values will be from MTA rewind Map
				
				}else if(coverNB.equalsIgnoreCase("Yes") && coverMTA.equalsIgnoreCase("No") && coverMTARewind.equalsIgnoreCase("Yes")){
					// values will be from MTA rewind Map
					cedeComm = (String)data_map.get("TER_CedeComm");
				}else if(coverNB.equalsIgnoreCase("Yes") && coverMTA.equalsIgnoreCase("Yes") && coverMTARewind.equalsIgnoreCase("No")){
					// values will be from MTA  Map but negative
					
					GrossPremium = "-" + Double.toString(  common.transaction_Details_Premium_Values.get("Terrorism").get("Gross Premium (GBP)"));
					NetPremium = "-" + Double.toString( common.transaction_Details_Premium_Values.get("Terrorism").get("Net Premium (GBP)"));
					
				}else if(coverNB.equalsIgnoreCase("Yes") && coverMTA.equalsIgnoreCase("No") && coverMTARewind.equalsIgnoreCase("No")){
					// Values will be from NB map but negative 
					
					GrossPremium = "-"+ (String)data_map.get("PS_Terrorism_GP");
					NetPremium = "-" +(String)data_map.get("PS_Terrorism_NP");
					
				}else if(coverNB.equalsIgnoreCase("No") && coverMTA.equalsIgnoreCase("Yes") && coverMTARewind.equalsIgnoreCase("Yes")){
					// Values will be from MTA rewind map:
					
				}else if(coverNB.equalsIgnoreCase("No") && coverMTA.equalsIgnoreCase("No") && coverMTARewind.equalsIgnoreCase("Yes")){
					// Values will be from MTA rewind map:
				}else if(coverNB.equalsIgnoreCase("No") && coverMTA.equalsIgnoreCase("Yes") && coverMTARewind.equalsIgnoreCase("No")){
					// values will be from MTA  Map but negative
					
					GrossPremium = "-" + Double.toString(  common.transaction_Details_Premium_Values.get("FidelityGuarantee").get("Gross Premium (GBP)"));
					NetPremium = "-" + Double.toString( common.transaction_Details_Premium_Values.get("FidelityGuarantee").get("Net Premium (GBP)"));
				}
				break;
			
			case "Rewind" :
				coverNB = (String)common.NB_excel_data_map.get("CD_Terrorism");
				String coverNBRewind = (String)common.Rewind_excel_data_map.get("CD_Terrorism");
				cedeComm = (String)common.Rewind_excel_data_map.get("TER_CedeComm");
				
				if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("Yes")){
					// Values will be from rewind map
				}else if(coverNB.equalsIgnoreCase("Yes") && coverNBRewind.equalsIgnoreCase("No")){
					// Values will be from NB map but negative 
					
					GrossPremium = "-"+ (String)data_map.get("PS_Terrorism_GP");
					NetPremium = "-" +(String)data_map.get("PS_Terrorism_NP");
					
				}else if(coverNB.equalsIgnoreCase("No") && coverNBRewind.equalsIgnoreCase("Yes")){
					// Values will be from rewind map
				}
			}
		}
	}
	
	if(common.currentRunningFlow.contentEquals("CAN")){
		return new double[] {-Double.parseDouble(GrossPremium), -Double.parseDouble(BrokerCommission)};	
	}else if(sCoverName.contains("Terrorism")){
		return new double[] {Double.parseDouble(GrossPremium), Double.parseDouble(NetPremium)};
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
	double grossCommRate = Double.parseDouble(CommRateTotal);
	
	return(Double.parseDouble(grossPremium )*((grossCommRate- Double.parseDouble(brokeCommision))/100));

	
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
		
		BrokerageAmount = calculateBrokeageAmountTS(recipient,data_map,account,j,td);
		if(common.currentRunningFlow.contains("CAN")){
			BrokerageAmount = Double.parseDouble("-" + String.valueOf(BrokerageAmount));
		}
		calAmount =  BrokerageAmount;
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
	String terrorPremium, terrorIPT;
	
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
	
	if(common.currentRunningFlow.contains("MTA")){
		/*terrorPremium = Double.toString(common.transaction_Details_Premium_Values.get("Terrorism").get("Net Premium (GBP)"));
		terrorIPT = Double.toString(common.transaction_Details_Premium_Values.get("Terrorism").get("Gross IPT (GBP)"));*/
		
		terrorPremium = (String)data_map.get("PS_Terrorism_NP");
		terrorIPT = (String)data_map.get("PS_Terrorism_GT");
		
	}else if(common.currentRunningFlow.contains("CAN")){
		if(TestBase.product.contains("CCI")){
			terrorPremium = String.valueOf(common_CCI.CAN_CCI_ReturnP_Values_Map.get("Terrorism").get("Net Premium (GBP)"));
			terrorIPT = String.valueOf(common_CCI.CAN_CCI_ReturnP_Values_Map.get("Terrorism").get("Gross IPT (GBP)"));
		}else{
			terrorPremium = (String)data_map.get("PS_Terrorism_NP");
			terrorIPT = (String)data_map.get("PS_Terrorism_GT");
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
		common.compareValues(Double.parseDouble(actualTerrorPremium), values[0], "Terrorism RSA Amount ");
		common.compareValues(Double.parseDouble(actualTerrorIPT), values[1], "Terrorism RSA IPT ");
		double terrorRSADue=values[0] + values[1];
		common.compareValues(Double.parseDouble(actualerrorDue), terrorRSADue, "Terrorism RSA Due ");
			return  Double.parseDouble(common.roundedOff(Double.toString(terrorRSADue)));
	}
	else if(recipient.equalsIgnoreCase("Arthur J Gallagher (UK) Limited"))
	{
		double[] values = TerrorismCalculation(recipient,terrorPremium, terrorIPT); 
		common.compareValues(Double.parseDouble(actualTerrorPremium), values[0], "Terrorism AJG Amount ");
		common.compareValues(Double.parseDouble(actualTerrorIPT), values[1], "Terrorism AJG IPT ");
		double terrorAJGDue=values[0] + values[1];
		common.compareValues(Double.parseDouble(actualerrorDue), Double.parseDouble(common.roundedOff(Double.toString(terrorAJGDue))), "Terrorism AJG Amount Due ");
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
			case "Renweal":
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

@SuppressWarnings("finally")
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

public boolean createEndorsement(Map<Object, Object> map_data){
	   Date dateobj = null;
	   try{
		   customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		   customAssert.assertTrue(common.funcButtonSelection("Create Endorsement"),"Unable to click on Create Endorsement button.");
			customAssert.assertTrue(common.funcPageNavigation("Create Endorsement", ""), "Navigation problem to Create Endorsement Reason page .");
			customAssert.assertTrue(k.Input("Endorsement_AdditionalInfo","Test Endorsement"),"Unable to enter additional info on Create Endorsement page");
			df = new SimpleDateFormat("dd/MM/yyyy");
			
			int ammendmet_period = Integer.parseInt((String)map_data.get("MTA_EndorsementPeriod"));
	    	if(ammendmet_period > Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"))){
	    		TestUtil.reportStatus("Amendement Period Should not be greater than Policy Duration", "Fail", true);
	    		return false;
	    	}
	    	
	    	TimeZone uk_timezone = TimeZone.getTimeZone("Europe/London");
	    	Calendar c = Calendar.getInstance(uk_timezone);
	    	c.add(Calendar.DATE, ammendmet_period);
	    	dateobj = df.parse(df.format(c.getTime()));
//	    	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//	    	Calendar c = Calendar.getInstance();    
//	    	c.add(Calendar.DATE, ammendmet_period);
//	    	dateobj = df.parse(df.format(c.getTime()));
//		
	        customAssert.assertTrue(k.Click("SPI_Endorsement_eff_date"), "Unable to enter Endorsement effective Date.");
	        customAssert.assertTrue(k.Type("SPI_Endorsement_eff_date", df.format(dateobj)), "Unable to Enter Endorsement effective Date .");
	        customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
	        String testName = (String)map_data.get("Automation Key");
	        customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "EndorsementCreation", testName, "MTA_QC_InceptionDate", df.format(dateobj),map_data),"Error while writing data to excel for field >PS_MTAEffectiveDate<");
	        
	        	if(TestBase.product.equalsIgnoreCase("CCI"))
	        	{
	        		WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "EndorsementCreation", testName, "MTA_EffectiveDate", k.getAttribute("SPI_Endorsement_eff_date", "value"),map_data);
	        	 
	        	}
			
//			map_data.put("NTU_AdditionalInfo",  k.getAttribute("NTU_AdditionalInfo","value"));
			customAssert.assertTrue(common.funcButtonSelection("Create Endorsement"),"Unable to click on Create Endorsement button on Create Endorsement page.");
			
		   return true;
}
	   catch(Throwable t){
	   		TestUtil.reportStatus("Failed In Create Endorsement function. " , "Fail", false);
	   		ErrorUtil.addVerificationFailure(t);
	   		return false;
	   		}
}

public boolean funcPremiumSummary_CCI(Map<Object, Object> map_data,String code,String event) {
	
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
		common_CCI.isTransTable = true;
		customAssert.assertTrue(funcTransactionPremiumTable(code, event), "Error while verifying Transaction Premium table on premium Summary page .");
	}
	
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
			common_CCI.PremiumSummary_Values.put(covername, temp_PremiumValues);
		return 0;		
			
	}catch(Throwable t) { 
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Insured Properties function is having issue(S). \n", t);
        return 0;
 }
	
}

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
				totalGrossPremium = totalGrossPremium + Double.parseDouble(common.roundedOff(String.valueOf(common_CCI.PremiumSummary_Values.get(sectionName.replaceAll(" ", "")).get("PS_"+sectionName.replaceAll(" ", "")+"_GP"))));
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
							common_CCI.transaction_Premium_Values.put(sectionName, transaction_Section_Vals_Total);
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
						common_CCI.transaction_Premium_Values.put(sectionName, transaction_Section_Vals);
					
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
						trans_error_val = trans_error_val + transactionPremiumTable_Verification_Total(sectionNames,common_CCI.transaction_Premium_Values);
					else
						trans_error_val = trans_error_val + transactionPremiumTable_Verification(policy_Duration,s_Name,common_CCI.transaction_Premium_Values);
					
			
				}
				
				 TestUtil.reportStatus("Transaction Premium table has been verified suceesfully . ", "info", true);
				
			}
			
			
		}catch(Throwable t ){
			return false;
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

public int transactionPremiumTable_Verification_Total(List<String> sectionNames,Map<String,Map<String,Double>> transaction_Premium_Values){
	
	try{
	
	
	TestUtil.reportStatus("---------------Totals-----------------","Info",false);
	double exp_value = 0.0;
	
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + common_CCI.transaction_Premium_Values.get(section).get("Net Premium (GBP)");
	}
	String t_NetNetP_actual = Double.toString(common_CCI.transaction_Premium_Values.get("Totals").get("Net Premium (GBP)"));
	CommonFunction.compareValues(Double.parseDouble(common.roundedOff(String.valueOf(exp_value))),Double.parseDouble(t_NetNetP_actual),"Net Premium (GBP)");

	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + common_CCI.transaction_Premium_Values.get(section).get("Gross Premium (GBP)");
	}
	String t_grossP_actual = Double.toString(common_CCI.transaction_Premium_Values.get("Totals").get("Gross Premium (GBP)"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_grossP_actual)," Gross Premium (GBP)");
		
	double premium_diff = Double.parseDouble(common.roundedOff(String.valueOf(exp_value))) - Double.parseDouble(t_grossP_actual);
	
	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + common_CCI.transaction_Premium_Values.get(section).get("Commission (GBP)");
	}
	String t_bc_actual =  Double.toString(common_CCI.transaction_Premium_Values.get("Totals").get("Commission (GBP)"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_bc_actual),"Commission (GBP)");

	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + common_CCI.transaction_Premium_Values.get(section).get("Gross IPT (GBP)");
	}
	String t_GrossIPT_actual =  Double.toString(common_CCI.transaction_Premium_Values.get("Totals").get("Gross IPT (GBP)"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_GrossIPT_actual),"Gross IPT (GBP)");

	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + common_CCI.transaction_Premium_Values.get(section).get("Net IPT (GBP)");
	}
	String t_NetIPT_actual = Double.toString(common_CCI.transaction_Premium_Values.get("Totals").get("Net IPT (GBP)"));
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
		String t_NetNetP_actual = Double.toString(common_CCI.transaction_Premium_Values.get(sectionNames).get("Net Premium (GBP)"));
		customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(t_NetNetP_expected),Double.parseDouble(t_NetNetP_actual)," Net Premium (GBP)"),"Mismatched Net Premium (GBP)");
		//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent,"Premium Summary",testName,"PS_"+code+"_PenComm",pc_expected,common.NB_excel_data_map),"Error while writing Pen Commission for cover "+code+" to excel .");
		
		// Gross premium Calculation
		double denominator = 1- (Double.parseDouble((String)map_data.get("PS_"+code+"_CR"))/100);
		double t_grossP = Double.parseDouble(t_NetNetP_expected) / denominator;
		String t_grossP_actual = Double.toString(common_CCI.transaction_Premium_Values.get(sectionNames).get("Gross Premium (GBP)"));
		customAssert.assertTrue(CommonFunction.compareValues(t_grossP,Double.parseDouble(t_grossP_actual),sectionNames+" Transaction Gross Premium"),"Mismatched Gross Premium Values");
						
		//Commission GBP calculation
		
		double t_comm = t_grossP* (Double.parseDouble((String)map_data.get("PS_"+code+"_CR"))/100);
		String t_Actual_Comm = Double.toString(common_CCI.transaction_Premium_Values.get(sectionNames).get("Commission (GBP)"));
		customAssert.assertTrue(CommonFunction.compareValues(t_comm,Double.parseDouble(t_Actual_Comm),"Commission (GBP)"),"Mismatched Commission (GBP) Values");
		
		
		//Gross IPT Calculation
		String iptRate = (String)map_data.get("PS_"+code+"_IPT"); 
		
		double t_InsuranceTax = (t_grossP * Double.parseDouble(iptRate))/100.0;
		t_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(t_InsuranceTax)));
		String t_InsuranceTax_actual = Double.toString(common_CCI.transaction_Premium_Values.get(sectionNames).get("Gross IPT (GBP)"));
		customAssert.assertTrue(CommonFunction.compareValues(t_InsuranceTax,Double.parseDouble(t_InsuranceTax_actual),"Gross IPT (GBP)"),"Mismatched Gross IPT (GBP) Values");
		
		// Net IPT calculation :
		
		double t_NetIPT =  Double.parseDouble(t_NetNetP_expected)  * (Double.parseDouble(iptRate)/100);
		t_NetIPT = Double.parseDouble(common.roundedOff(Double.toString(t_NetIPT)));
		String t_NetIPT_actual = Double.toString(common_CCI.transaction_Premium_Values.get(sectionNames).get("Net IPT (GBP)"));
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

}
