package com.selenium.commonfiles.base;

import static com.selenium.commonfiles.util.TestUtil.WriteDataToXl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.relevantcodes.extentreports.model.Test;
import com.selenium.commonfiles.util.CustomAssetions;
import com.selenium.commonfiles.util.ErrorUtil;
import com.selenium.commonfiles.util.ObjectMap;
import com.selenium.commonfiles.util.TestUtil;
import com.selenium.commonfiles.util.XLS_Reader;

public class CommonFunction_PEN extends TestBase {
	
	public static String Environment = null;
	public static String loginUserName;
	public int pdf_count=0,err_count=0,final_err_pdf_count=0,trans_error_val=0;
	
	public static int countOfCovers,countOfTableRows;
	public static int errorVal=0,counter = 0;
	public double totalGrossTax = 0.0,totalGrossTaxMTA = 0.0,totalGrossPremium = 0.0,totalGrossPremiumMTA=0.0,totalNetPremiumTax=0.0,totalNetPremiumTaxMTA=0.0;
	public static double Cover_TotalRate = 0.00, Cover_AdjustedRate = 0.00, Cover_Premium = 0.00, Cover_TotalPremium = 0.00;
	public static WebElement taxTable_tBody;
	public static WebElement objTable;
	public static WebElement taxTable_tHead;
	public static DecimalFormat f = new DecimalFormat("00.00");
	public static double adjustedPremium = 0.0,adjustedTotalPremium=0.0,adjustedTotalPremiumMTA=0.0,adjustedTotalTax=0.0,adjustedTotalTaxMTA=0.0,unAdjustedTotalTax=0.0,unAdjustedTotalTaxMTA=0.0;
	public static double PD_TotalRate = 0.0, PD_AdjustedRate = 0.0, PD_MD_Premium=0.0, PD_BI_Premium=0.0, PD_MD_TotalPremium = 0.00, PD_BI_TotalPremium = 0.00, finalMDPremium = 0.00, finalBIPremium= 0.00;
	public String currentRunningFlow ="NB";
	public static ArrayList<Object> inputarraylist = null;
	public static ArrayList<Object> inputarraylistMTA = null;
	public double PI_pdf_InsuranceTax = 0.0, PI_pdf_GrossPremium = 0.0,SEL_pdf_InsuranceTax = 0.0, SEL_pdf_GrossPremium = 0.0;
//	/public static ArrayList<Object> variableTaxAdjustmentIDs = null;
	public static Map<Object, Integer> variableTaxAdjustmentIDs = null;
	public static Map<Object, Integer> variableTaxAdjustmentIDsMTA = null;
	public static Map<Object, Double> grossTaxValues_Map = null;
	public static Map<Object, Map<Object, Object>> variableTaxAdjustmentVerificationMaps = null;
	public static Map<Object, Object> variableTaxAdjustmentDataMaps = null;
	public static Map<Object, Object> variableTaxAdjustmentDataMapsMTA = null;
	public static List<Object> headerNameStorage = null;
	public static List<Object> headerNameStorageMTA = null;
	public Map<String,Map<String,Double>> transaction_Details_Premium_Values = new HashMap<>();
	public String quoteStatus = "";
	public double rewindMTADoc_Premium = 0.00, rewindMTADoc_TerP = 0.00, rewindMTADoc_InsPTax = 0.00, rewindMTADoc_TotalP = 0.00;
	public double rewindDoc_Premium = 0.00, rewindDoc_TerP = 0.00, rewindDoc_InsPTax = 0.00, rewindDoc_TotalP = 0.00, rewindDoc_InsTaxTer = 0.00;
	public double rewindMTADoc_AddTaxTer = 0.00;
	public static int size;
	public boolean isInsuranceTaxDone = false;
	SimpleDateFormat df = new SimpleDateFormat();
	Date currentDate = new Date();
	public Map<Object, Object> NB_excel_data_map = null;
	public Map<Object, Object> MTA_excel_data_map = null;
	public Map<Object, Object> Rewind_excel_data_map = null;
	public Map<Object, Object> Renewal_excel_data_map = null;
	public Map<Object, Object> CAN_excel_data_map = null;
	public List<String> CoversDetails_data_list = null;
	public static Map<String, Double> Adjusted_Premium_map = null;
	
	public Map<String, String> EndorsementIndividualData = null;
	public Map<String, Map<String , String>> EndorsementCollectiveData = new LinkedHashMap<String, Map<String, String>>();
	public Map<String, String> ExtraEndorsementList = new LinkedHashMap<>();
	public Map<String, Map<String , String>> EndorsementFreeFormatData = new LinkedHashMap<>();
	
	public String FP_Covers = null;
	
	public static Map<String , String> AdjustedTaxDetails = new LinkedHashMap<String, String>();
	public static Map<String , String> AdjustedTaxCollection = new LinkedHashMap<String, String>();
	
	
	public Map<String, List<Map<String, String>>> NB_Structure_of_InnerPagesMaps = null;
	public Map<String, List<Map<String, String>>> MTA_Structure_of_InnerPagesMaps = null;
	public Map<String, List<Map<String, String>>> Rewind_Structure_of_InnerPagesMaps = null;
	public Map<String, List<Map<String, String>>> Renewal_Structure_of_InnerPagesMaps = null;
	public Map<String, List<Map<String, String>>> CAN_Structure_of_InnerPagesMaps = null;
	DecimalFormat decim = new DecimalFormat("#.00");
	public Map<String,Integer> no_of_inner_data_sets = new HashMap<>();
	public Hashtable<String,String> GrosspremSmryData = new Hashtable<String,String>();

	// Premium Summary Data maps
	public Map<String,Map<String,Double>> transaction_Premium_Values = new HashMap<>();
	public Map<String,Map<String,Double>> transaction_Details_Premium_Values_EndorsemntRenewal = new HashMap<>();
	public Map<String,Map<String,Double>> Can_ReturnP_Values_Map = new HashMap<>();
	public boolean PremiumFlag = false;
	public List<String> referrals_list = new ArrayList<>();
	public List<String> quote_validations_list = new ArrayList<>();
	public double TotalPremiumWithAdminDocAct = 0.00, TotalPremiumWithAdminDocExp = 0.00, PremiumExcTerrDocAct = 0.00,  PremiumExcTerrDocExp = 0.00, TerPremDocAct = 0.00, TerPremDocExp = 0.00, InsTaxDocAct = 0.00, InsTaxDocExp = 0.00;
	public double AdditionalPWithAdminDocAct = 0.00, AdditionalExcTerrDocAct = 0.00,  AdditionalTerPDocAct = 0.00, AdditionalInsTaxDocAct = 0.00;
	public double InsTaxTerrDoc = 0.00, tpTotal = 0.00, AddTaxTerrDoc = 0.00;
	public List<String> AlreadyAddedFPEntries = new LinkedList<>();
	
	/**
	 * This method selects the specified covers from cover Page
	 */
	    
	public boolean funcCovers(Map<Object, Object> map_data){
		
		boolean retvalue = true;
		CoversDetails_data_list = new ArrayList<>();
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
		 				
		 				
		 				CoversDetails_data_list.add(coverWithoutLocator);
		 				
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
					if (!driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"')]")).isSelected()){
						k.scrollInViewByXpath("//*[contains(@name,'"+c_locator+"')]");
						driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"')]")).click();        
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
					if (driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"')]")).isSelected()){
						k.scrollInViewByXpath("//*[contains(@name,'"+c_locator+"')]");
						driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"')]")).click();     
						k.AcceptPopup();
						TestUtil.reportStatus("Cover: <b>"+coverName+"</b> is unchecked ", "Info", false);
					}else{
						TestUtil.reportStatus("Cover: <b>"+coverName+"</b> is checked by default.", "Info", false);
					}
				                    
				return result;
		           
		    } catch(Throwable t) {
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
			if(!TestBase.product.contains("CCI")){
				customAssert.assertTrue(common_HHAZ.verifyEndorsementONPremiumSummary(map_data),"Endorsement on Premium is having issue(S).");
			}
			
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
		customAssert.assertTrue(common_HHAZ.insuranceTaxAdjustmentHandling(code,event), "Premium Summary function is having issue(S) . ");
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
	
public boolean funcPremiumSummary_MTA(Map<Object, Object> map_data,String code,String event) {
		
		boolean r_value= true;
		Date currentDate = new Date();
		String testName = (String)map_data.get("Automation Key");
		String customPolicyDuration=null;
		SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
		
		try{
			customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
//			customAssert.assertTrue(common_HHAZ.verifyEndorsementONPremiumSummary(map_data),"Endorsement on Premium is having issue(S).");
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
				customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
				customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
				customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
				
			}	
			k.waitTwoSeconds();
		
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
			policy_Start_date = k.getTextByXpath("//*[contains(text(),'Policy Start Date')]//following::div[1]");
			//policy_Start_date = common_VELA.get_PolicyStartDate((String)map_data.get("PS_PolicyStartDate"));
			map_data.put("PS_PolicyStartDate", policy_Start_date);
			Policy_End_Date = common.daysIncrementWithOutFormation((String)map_data.get("PS_PolicyStartDate"), policy_Duration);
			map_data.put("PS_PolicyEndDate", Policy_End_Date);
			if(((String)map_data.get("PS_DefaultStartEndDate")).equals("No")){
				customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
				customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
				customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
				
			}	
			k.waitTwoSeconds();
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
		if(common.currentRunningFlow.equalsIgnoreCase("Rewind"))
			TestUtil.reportStatus("MTA Rewind Policy Duration = "+customPolicyDuration+" Days . ", "Info", true);
		else
			TestUtil.reportStatus("MTA Policy Duration = "+customPolicyDuration+" Days . ", "Info", true);
		
		customAssert.assertTrue(Verify_premiumSummaryTable(), "Error while verifying Premium Summary table in MTA  .");
		customAssert.assertTrue(insuranceTaxAdjustmentHandling(code,event), "Premium Summary function is having issue(S) . ");
		customAssert.assertTrue(Verify_premiumSummaryTable(), "Error while verifying Premium Summary table in MTA  .");
		 
		if(!common_CCD.isMTARewindStarted){
			customAssert.assertTrue(func_Flat_Premiums_(common.MTA_excel_data_map,common.MTA_Structure_of_InnerPagesMaps), "Error while verifying Flat Premium table in MTA  .");
		}
		if(TestBase.businessEvent.equals("MTA") || common.currentRunningFlow.equalsIgnoreCase("MTA")){
		customAssert.assertTrue(func_MTATransactionDetailsPremiumTable(code, event), "Error while verifying Transaction Details Premium table on premium Summary page .");
		customAssert.assertTrue(funcTransactionDetailsMessage_MTA());
		}

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
		
		List<WebElement> delete_Btns = driver.findElements(By.xpath("//*[text()='Delete']"));
		int deleteSize =  delete_Btns.size();
		if(deleteSize>0){
			
			for(int deleteFP=1;deleteFP<=deleteSize;deleteFP++){
				customAssert.assertTrue(get_Flat_Premium_Entries(deleteFP), "Error while reading Flat Premium Entries .");
				Iterator collectiveDataIT = common.transaction_Details_Premium_Values.entrySet().iterator();
				while(collectiveDataIT.hasNext()){
					Map.Entry collectiveDataMapValue = (Map.Entry)collectiveDataIT.next();
					String coverName = collectiveDataMapValue.getKey().toString();
					System.out.println(coverName);
					String splittedCoverName[] = coverName.split("_");
					common_HHAZ.CoversDetails_data_list.remove(splittedCoverName[0].replaceAll(" ", ""));
				}
				
				String flatTablePath = "//p[text()='Flat Premiums']//following::table[1]//tbody";
				String CoverName = driver.findElement(By.xpath(flatTablePath+"//tr["+deleteFP+"]//td[1]")).getText();
				String NetNetPremium = driver.findElement(By.xpath(flatTablePath+"//tr["+deleteFP+"]//td[2]")).getText();
				String TaxRate = driver.findElement(By.xpath(flatTablePath+"//tr["+deleteFP+"]//td[10]")).getText();
				String Description = driver.findElement(By.xpath(flatTablePath+"//tr["+deleteFP+"]//td[13]")).getText();
				
				map_data.put(CoverName+"_FP", Double.parseDouble(NetNetPremium));
				AlreadyAddedFPEntries.add(CoverName);
			}
			
		}
		
		for(int count=deleteSize+1;count<=no_of_fp_e+deleteSize;count++){
			
			String coverName = internal_data_map.get("Flat-Premiums").get(count-(deleteSize+1)).get("FP_Section");
			if(TestBase.product.contains("GTA")){
				coverName = "Goods in Transit RSA UK";
			}else if(TestBase.product.contains("GTA")){
				coverName = "Goods in Transit ROI";				
			}
			
			if(!common_HHAZ.CoversDetails_data_list.contains(coverName.replaceAll(" ", ""))){
				TestUtil.reportStatus("", "Info", false);
				continue;
			}
			
			customAssert.assertTrue(k.Click("POF_Add_Flat_P_btn"),"Unable to Click Add Flat Premium button . ");
			customAssert.assertTrue(Verify_FP_Section_Values(),"Error while verifying covers list in flat premium section dropdown . ");
			customAssert.assertTrue(k.DropDownSelection("POF_FP_Section", internal_data_map.get("Flat-Premiums").get(count-(deleteSize+1)).get("FP_Section")),"Unable to enter FP_Section in Flat Premium page .");
			customAssert.assertTrue(k.Input("POF_FP_Premium", internal_data_map.get("Flat-Premiums").get(count-(deleteSize+1)).get("FP_Premium")),"Unable to enter FP_Premium in Flat Premium page .");
			customAssert.assertTrue(k.Input("POF_FP_TaxRate", internal_data_map.get("Flat-Premiums").get(count-(deleteSize+1)).get("FP_TaxRate")),"Unable to enter FP_TaxRate in Flat Premium page .");
			customAssert.assertTrue(k.Input("POF_FP_Description", internal_data_map.get("Flat-Premiums").get(count-(deleteSize+1)).get("FP_Description")),"Unable to enter FP_Description in Flat Premium page .");
			customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"), "Unable to click Inner Save button on Flat Premiums .");
			customAssert.assertTrue(get_Flat_Premium_Entries(count-deleteSize), "Error while reading Flat Premium Entries .");
			func_FP_Entries_Verification_MTA(internal_data_map.get("Flat-Premiums").get(count-(deleteSize+1)).get("FP_Section"),internal_data_map,count-deleteSize);
			
			//For each added entry in FP, cover name will be removed from Section List
			common_HHAZ.CoversDetails_data_list.remove(internal_data_map.get("Flat-Premiums").get(count-(deleteSize+1)).get("FP_Section").replaceAll(" ", ""));
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
			
				if(TestBase.product.contains("GTA")){
					coverName_withoutSpace = "GoodsinTransitRSAUK";
				}else if(TestBase.product.contains("GTB")){
					coverName_withoutSpace = "GoodsinTransitROI";					
				}else if(TestBase.product.contains("POE") || TestBase.product.contains("POB")){
					if(coverName_withoutSpace.contains("Liability")){
						coverName_withoutSpace = "Liability";
					}			
				}
				
				coversNameList.add(coverName_withoutSpace);
				
				if(common.currentRunningFlow.equalsIgnoreCase("MTA")){
						key = "CD_"+coverName_withoutSpace;
						if(key.equalsIgnoreCase("CD_CommercialVehicle"))
							key="CD_CommercialVehicles";
						if(key.equalsIgnoreCase("CD_ComputerRSA"))
							key="CD_Computers";
								
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
				if(common.currentRunningFlow.equalsIgnoreCase("Rewind")){
					key = "CD_"+coverName_withoutSpace;
							
					if(common.Rewind_excel_data_map.get(key).toString().equalsIgnoreCase("Yes")){
						continue;
					}else{
						TestUtil.reportStatus("Cover Name <b>  ["+coverName+"]  </b> should not present in the flat fremium section dropdown list as This cover is not selected on Cover Details page.", "FAIL", false);
						count++;
					}
				
				}
			} //For loop
		 
			for(int p=0;p<common_HHAZ.CoversDetails_data_list.size();p++){
				coverName_datasheet = common_HHAZ.CoversDetails_data_list.get(p);
				
				if(coverName_datasheet.equalsIgnoreCase("CommercialVehicles"))
					coverName_datasheet="CommercialVehicle";
				
				if(coversNameList.contains(coverName_datasheet) || coverName_datasheet.equalsIgnoreCase("LegalExpenses")){
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
	
} public boolean get_Flat_Premium_Entries(int row_index){
	
	
	try{
	String testName = (String)common.MTA_excel_data_map.get("Automation Key");
	
	customAssert.assertTrue(common.funcPageNavigation("Flat Premiums", ""),"Flat Premiums page navigations issue(S)");
	
	/*if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
		int policy_Duration = Integer.parseInt((String)common.Renewal_excel_data_map.get("PS_Duration"));
	}else if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
		int policy_Duration = Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"));
	}else{
		int policy_Duration = Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"));
	}*/
	
	
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
				double NetIPT = fp_Section_Vals.get("Net Premium") *(fp_Section_Vals.get("Tax Percentage")/100);
				fp_Section_Vals.put("Net IPT", NetIPT);
				if(TestBase.product.contains("GTA")){
					sectionName = "Goods in Transit RSA UK";
				}else if(TestBase.product.contains("GTB")){
					sectionName = "Goods in Transit ROI";
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
	
	
//	section_map.put("EmployersLiability", "el");
	
	section_map.put("MaterialDamage", "md8");
	section_map.put("BusinessInterruption", "bi");
	section_map.put("Money&Assault","ma");
	section_map.put("Money","mn2");
	section_map.put("CyberandDataSecurity","cyb");
	section_map.put("EmployersLiability","el3");
	section_map.put("PublicLiability","pl3");
	section_map.put("PropertyOwnersLiability","pl3");
	section_map.put("PersonalAccident","pa");
	section_map.put("PersonalAccidentOptional","pao");
	section_map.put("GoodsinTransit","gt");
	section_map.put("LegalExpenses","lg2");
	section_map.put("LossOfRentalIncome","bi3");
	section_map.put("Terrorism", "tr3");
	section_map.put("Total", "tot");
	
	double exp_Premium = 0.0, exp_Tax =0.00;
	
	try{
	
		String annualTble_xpath = "html/body/div[3]/form/div/table[2]";
		String policy_status_actual = k.getText("Policy_status_header");
		int trans_tble_Rows = driver.findElements(By.xpath(annualTble_xpath+"/tbody/tr")).size();
		int trans_tble_Cols = driver.findElements(By.xpath(annualTble_xpath+"/thead/tr/th")).size();
		String sectionName = null;
		String Covername= null;
		PremiumExcTerrDocExp = 0;
		PremiumExcTerrDocExp = 0;
		if(common.currentRunningFlow.equalsIgnoreCase("NB") ||  common.currentRunningFlow.equalsIgnoreCase("Renewal")){
		
			if(!PremiumFlag)
			for(int i =1;i<=trans_tble_Rows-1;i++){
				String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
				sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
				Covername= sectionName;
				String CoverName = sectionName;
				if(sectionName.contains("Totals"))
					sectionName = "Total";
				if(sectionName.contains("BusinesssInterruption"))
					sectionName = "BusinessInterruption";
				if(TestBase.product.equals("POB")||TestBase.product.equals("POE")){
					if(sectionName.contains("EmployersLiability") || sectionName.contains("PropertyOwnersLiability"))
						Covername = "Liability";	
				}
				if(isInsuranceTaxDone == false){
				customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
				
				if(((String)data_map.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
					data_map.put("PS_"+sectionName+"_IPT", "0.0");
				}else{
					if(!policy_status_actual.contains("Rewind")){
						data_map.put("PS_"+sectionName+"_IPT", data_map.get("PS_IPTRate"));
					}else{
						if(sectionName.contains("PersonalAccident")){
							sectionName="PersonalAccidentStandard";
						}
						if(((((String)data_map.get("CD_"+sectionName)).equals("No") && ((String)data_map.get("CD_Add_"+sectionName)).equals("Yes")))){
							data_map.put("PS_"+sectionName+"_IPT", data_map.get("PS_IPTRate"));
						}
					}
				}
				}
			}
		
		}
		
		if(common.currentRunningFlow.equalsIgnoreCase("Rewind") && TestBase.businessEvent.equalsIgnoreCase("MTA")){
			
			if(!PremiumFlag)
			for(int i =1;i<=trans_tble_Rows-1;i++){
				String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
				sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
				Covername= sectionName;
				if(sectionName.contains("Totals"))
					sectionName = "Total";
				if(sectionName.contains("BusinesssInterruption"))
					sectionName = "BusinessInterruption";
				if(sectionName.equalsIgnoreCase("GoodsinTransit")){
					sectionName = "GoodsInTransit";
				}
			
				if(((String)common.NB_excel_data_map.get("CD_"+sectionName)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_"+sectionName)).equals("No") && ((String)common.Rewind_excel_data_map.get("CD_"+sectionName)).equals("Yes")){
					if(sectionName.contains("PersonalAccident")){
						sectionName="PersonalAccident";
					}
					if(sectionName.contains("GoodsInTransit")){
						sectionName="GoodsinTransit";
					}
				
					customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
					if(((String)data_map.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
						data_map.put("PS_"+sectionName+"_IPT", "0.0");
					}
				}else{
					if(sectionName.contains("GoodsInTransit")){
						sectionName="GoodsinTransit";
					}
					String cover_name = section_map.get(sectionName);
					String PencCommXpath , BrokerCommXpath;
					if(cover_name.contains("md")){
						PencCommXpath = "//*[@name='md_ccc"+"_penr']";
						BrokerCommXpath ="//*[@name='md_ccc"+"_comr']" ;
					}else if(cover_name.contains("el")){
						PencCommXpath = "//*[@name='el_ccc"+"_penr']";
						BrokerCommXpath ="//*[@name='el_ccc"+"_comr']" ;
					}else{
						 PencCommXpath = "//*[contains(@id,'_"+cover_name+"_penr')]";
						 BrokerCommXpath ="//*[contains(@id,'_"+cover_name+"_comr')]";
					}
					
					String penComm = driver.findElement(By.xpath(PencCommXpath)).getAttribute("value");
					String BrokComm = driver.findElement(By.xpath(BrokerCommXpath)).getAttribute("value");
					common.Rewind_excel_data_map.put("PS_"+sectionName+"_PenComm_rate", penComm);
					common.Rewind_excel_data_map.put("PS_"+sectionName+"_BrokerComm_rate", BrokComm);
				}
			}
		}
		if(common.currentRunningFlow.equalsIgnoreCase("Rewind") && TestBase.businessEvent.equalsIgnoreCase("NB")){
			
			if(!PremiumFlag)
				for(int i =1;i<=trans_tble_Rows-1;i++){
					String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
					sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
					Covername= sectionName;
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
					Covername= sectionName;
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
		
		if(common.currentRunningFlow.equalsIgnoreCase("Requote")){
			
			if(!PremiumFlag)
			for(int i =1;i<=trans_tble_Rows-1;i++){
				String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
				sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
				Covername= sectionName;
				Covername= sectionName;
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
			if(common.currentRunningFlow.equalsIgnoreCase("MTA")){
					
				//if(!PremiumFlag)
				for(int i =1;i<=trans_tble_Rows-1;i++){
					String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
					sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
					Covername= sectionName;
					switch(sectionName){
					case "BusinesssInterruption":
						//code = "BusinessInterruption";
						sectionName = "BusinessInterruption";
						break;
					case "PersonalAccident":
						//code = "PersonalAccidentStandard";
						sectionName = "PersonalAccidentStandard";
						break;
					
					case "GoodsinTransit":
						//code = "GoodsInTransit";
						sectionName = "GoodsInTransit";
						break;
					
					
					}
					if(TestBase.product.equals("POB")||TestBase.product.equals("POE")){
						if(sectionName.contains("EmployersLiability") || sectionName.contains("PropertyOwnersLiability"))
							Covername = "Liability";	
					}
					if(sectionName.contains("Totals"))
						sectionName = "Total";
					if(CommonFunction.businessEvent.equalsIgnoreCase("Renewal") && common.currentRunningFlow.equalsIgnoreCase("MTA")){
						if(((String)common.Renewal_excel_data_map.get("CD_"+Covername)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_"+Covername)).equals("Yes")){
							if(sectionName.contains("PersonalAccident")){
								sectionName="PersonalAccident";
							}
							if(sectionName.contains("GoodsInTransit")){
								sectionName="GoodsinTransit";
							}
							customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
						}else{
							String cover_name = section_map.get(sectionName);
							String CommXpath =null;
							if(cover_name.contains("md")){
								CommXpath ="//*[@name='md8_comr']" ;
							  }else{
								CommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
							}
							String Comm = driver.findElement(By.xpath(CommXpath)).getAttribute("value");		
							common.MTA_excel_data_map.put("PS_"+sectionName+"_CR", Comm);
											
						}
					}else{
						
						if(((String)common.NB_excel_data_map.get("CD_"+Covername)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_"+Covername)).equals("Yes")){
							if(sectionName.contains("PersonalAccident")){
								sectionName="PersonalAccident";
							}
							if(sectionName.contains("GoodsInTransit")){
								sectionName="GoodsinTransit";
							}
							if(sectionName.contains("BusinesssInterruption"))
								sectionName = "BusinessInterruption";
							customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
							if(((String)data_map.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
								data_map.put("PS_"+sectionName+"_IPT", "0.0");
							}
						}else{
							
							if(sectionName.contains("PersonalAccident")){
								sectionName="PersonalAccident";
							}
							
							if(sectionName.contains("GoodsInTransit")){
								sectionName="GoodsinTransit";
							}
							
							String cover_name = section_map.get(sectionName);
							String CommXpath;
							if(cover_name.contains("md")){
								CommXpath ="//*[@name='md_comr']" ;
							}else if(cover_name.contains("el")){
								CommXpath ="//*[@name='el_comr']" ;
							}else{
								 CommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
							}
							String comm = driver.findElement(By.xpath(CommXpath)).getAttribute("value");
							common.MTA_excel_data_map.put("PS_"+sectionName+"_CR", comm);
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
				 if(sectionName.contains("BusinesssInterruption"))
						sectionName = "BusinessInterruption";
				
					err_count = err_count + func_PremiumSummaryCalculation_MTA(section_map.get(sectionName),sectionName,locator_map);
				exp_Premium = exp_Premium + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_GP"));
				exp_Tax = exp_Tax + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_GT"));
			}
			
			double Total_GP = 00.00;
			double Total_GT = 00.00;
			double Total_NPIPT = 00.00,Total_comm = 00.00,Total_NP=00.00  ;
			
			for(int i =1;i<=trans_tble_Rows-1;i++){
				String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
				sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
				 if(sectionName.contains("Totals")){
					sectionName = "Total";}
				 if(sectionName.contains("BusinesssInterruption")){
						sectionName = "BusinessInterruption";}
				Total_GP = Total_GP + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_GP"));
				Total_GT = Total_GT + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_GT"));
				Total_comm = Total_comm + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_GC"));
				Total_NP = Total_NP + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_NP"));
				Total_NPIPT = Total_NPIPT + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_NPIPT"));
			}
			
			data_map.put("PS_Total_GT", f.format(Total_GT));
			data_map.put("PS_Total_GP", f.format(Total_GP));
			data_map.put("PS_Total_NP", f.format(Total_NP));
			data_map.put("PS_Total_GC", f.format(Total_comm));
			data_map.put("PS_Total_NPIPT", f.format(Total_NPIPT));
			
			String exp_Total_Premium = common.roundedOff(Double.toString(exp_Premium));
//			String act_Total_Premium = k.getAttribute("SPI_Total_Premium", "value");
//			act_Total_Premium = act_Total_Premium.replaceAll(",", "");
			
			TestUtil.reportStatus("---------------Total Premium-----------------","Info",false);
			String GrossP_total = k.getAttributeByXpath("//*[@id='tot_gprem']", "value").replaceAll(",", "");
			String NetP_total = k.getAttributeByXpath("//*[@id='tot_nprem']", "value").replaceAll(",", "");
			String Commission_total = k.getAttributeByXpath("//*[@id='tot_com']", "value").replaceAll(",", "");
			String IPT_total = k.getAttributeByXpath("//*[@id='tot_gipt']", "value").replaceAll(",", "");
			String NetIPT_total = k.getAttributeByXpath("//*[@id='tot_nipt']", "value").replaceAll(",", "");
			
			
			CommonFunction.compareValues(Double.parseDouble(GrossP_total),Total_GP,"Total Gross Premium.");
			CommonFunction.compareValues(Double.parseDouble(NetP_total),Total_NP,"Total Net Premium.");
			CommonFunction.compareValues(Double.parseDouble(Commission_total),Total_comm,"Total Commission.");
			CommonFunction.compareValues(Double.parseDouble(IPT_total),Total_GT,"Total IPT.");
			CommonFunction.compareValues(Double.parseDouble(NetIPT_total),Total_NPIPT,"Total Net IPT.");			
			return true;
		   
		}catch(Throwable t){ 
					return false;
			
		}
	}
	
	public boolean funcAddInput_PremiumSummary(String code, String cover_name,Map<Object,Object> data_map) {
		boolean retvalue=true;
		try{
			String CommXpath =null;
			if(cover_name.contains("md")){
				CommXpath ="//*[@name='md8_comr']" ;
			  }else{
				CommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
			}
			String CommRate = UpdatedCommissionRate((String)data_map.get("PS_"+code+"_CR"));
			
			customAssert.assertTrue(k.InputByXpath(CommXpath, CommRate), "Unable to enter value of Commission for "+cover_name+".");
			return retvalue;
			
		}catch(Throwable t) {
	        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
	        Assert.fail("Premium Summary Add Input function is having issue(S). \n", t);
	        return false;
	 }
	}
	//Reusable for both NB and MTA
public int func_PremiumSummaryCalculation_MTA(String code,String covername,Map<String,String> premium_loc) {
		
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
				else if(CommonFunction.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind")){
					map_data = common.Rewind_excel_data_map;
					Tax_map_data = common.MTA_excel_data_map;
					event = "Rewind";
				}
				else{
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
			map_data.put("PS_"+covername+"_GC",grossC_expected);
			TestUtil.reportStatus("Commission :<b>"+(grossC_expected)+"</b> matches with <b>"+(grossC_actual), "Info", false);
			
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
	        return 1;
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
				AdditionalTerPDocAct = Double.parseDouble(t_grossP_actual);
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
		common_HHAZ.totalGrossTax=0.00;
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
				customAssert.assertTrue(verifyCoverDetails(),"Cover Details verification is causing issue(S).");
				customAssert.assertTrue(verifyGrossPremiumValues(),"Gross Premium verification is causing issue(S).");
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
				if(coverName_withoutSpace.contains("Liability")){
					coverName_withoutSpace = "Liability";
				}
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
				if(coverName_withoutSpace.contains("Liability")){
					coverName_withoutSpace = "Liability";
				}
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
			for(int p=0;p<common_HHAZ.CoversDetails_data_list.size();p++){
				coverName_datasheet = common_HHAZ.CoversDetails_data_list.get(p);
				
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
					String coverName = coverName_withoutSpace;
					if(coverName_withoutSpace.contains("Liability")){
						coverName = "Liability";
					}
					String key = "CD_"+coverName;
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
						key = "CD_"+coverName;
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
						key = "CD_"+coverName;
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
						key = "CD_"+coverName;
						if(sectionName.contains("Personal Accident Standard")){
							coverName_withoutSpace = "PersonalAccident";
						}
						if(sectionName.contains("Goods In Transit")){
							coverName_withoutSpace = "GoodsinTransit";
						}
						expectedIPTRate = (String)common.Renewal_excel_data_map.get("PS_"+coverName_withoutSpace+"_IPT");
						
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
				//	double t_InsuranceTax = 0.0;
					String iptRate_calc = "0.00";
					//Below code will decide tax rate based on policy start date for 10 cent rate
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
					Date policy_Start_date = sdf.parse((String)outer_data_map.get(("PS_PolicyStartDate"))); 
					Date tax_rate_change_date = sdf.parse("01/06/2017");
				
					if(((String)outer_data_map.get("PS_TaxExempt")).equalsIgnoreCase("Yes") || ((String)outer_data_map.get("PS_InsuranceTaxButton")).equalsIgnoreCase("Yes")){
						iptRate_calc = "0";
					}else if(policy_Start_date.before(tax_rate_change_date)){
						
						iptRate_calc = "10";
						
					}else{
						iptRate_calc = (String)outer_data_map.get("PS_"+sectionName.replaceAll(" ", "")+"_IPT");
					}
					
					String unAdjustedPremium = common.roundedOff(Double.toString(Double.parseDouble((String)outer_data_map.get("PS_"+sectionName.replaceAll(" ", "")+"_GP")) - adjustedTotalPremium));
					//iptRate_calc =  common.roundedOff(Double.toString(Double.parseDouble((String)outer_data_map.get("PS_IPTRate"))));
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
		try{
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
					String iptRate_calc = "0.00";
					//Below code will decide tax rate based on policy start date for 10 cent rate
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
					Date policy_Start_date = sdf.parse((String)outer_data_map.get(("PS_PolicyStartDate"))); 
					Date tax_rate_change_date = sdf.parse("01/06/2017");
				
					if(((String)outer_data_map.get("PS_TaxExempt")).equalsIgnoreCase("Yes") || ((String)outer_data_map.get("PS_InsuranceTaxButton")).equalsIgnoreCase("Yes")){
						iptRate_calc = "0";
					}else if(policy_Start_date.before(tax_rate_change_date)){
						
						iptRate_calc = "10";
						
					}else{
						iptRate_calc = (String)outer_data_map.get("PS_"+sectionName.replaceAll(" ", "")+"_IPT");
					}
					
					finalGrossTax = common.roundedOff(Double.toString((Double.parseDouble((String)outer_data_map.get("PS_"+trimmedSectionName+"_GP")) * ((Double.parseDouble(iptRate_calc) / 100.0)))));
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
		}catch (Throwable t) {
			
			return 1;
		}
		}
	
	public static boolean verification(String actualValue,String expectedValue,String sectionName,String description){
		
		if(description.equalsIgnoreCase("Gross Tax") || description.equalsIgnoreCase("Gross Premium")){
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
	
	
	public boolean funcStatusHandling(Map<Object, Object> map_data , String code , String event){
		
		 boolean ret_value = true;
		 String p_Status = null;
		 String quoteDate = null;
		 if(TestBase.businessEvent.equals("NB")){
		 	p_Status = (String)map_data.get("NB_Status");
		 }else if(TestBase.businessEvent.equals("Renewal")){
			 	p_Status = (String)map_data.get("Renewal_Status");
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
		 }else if(TestBase.businessEvent.equals("Requote")){
			 if(common.currentRunningFlow.equals("NB")){
				 p_Status = (String)common.NB_excel_data_map.get("NB_Status");
			 }else{
				 p_Status = (String)common.Requote_excel_data_map.get("Requote_Status");
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
					Assert.assertTrue(common_VELA.funcQuoteCheck(common.NB_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.NB_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,(String)map_data.get("NB_Status")), "Verify Policy Status (Quoted) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("NB_Status")+"  ]</b> status. ", "Info", true);
					break;
				case "On Cover":
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
					Assert.assertTrue(common_VELA.funcQuoteCheck(common.NB_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.NB_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Quoted"), "Verify Policy Status (Quoted) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(common.funcGoOnCover(common.NB_excel_data_map), "Go On Cover function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,(String)map_data.get("NB_Status")), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("POE") || TestBase.product.equalsIgnoreCase("CTB")){
						customAssert.assertTrue(common_CTB.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}else{
					customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}
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
					Assert.assertTrue(common_VELA.funcQuoteCheck(common.NB_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.NB_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Quoted"), "Verify Policy Status (Quoted) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
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
					Assert.assertTrue(common_VELA.funcQuoteCheck(common.NB_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.NB_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Quoted"), "Verify Policy Status (Quoted) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(common.funcGoOnCover(common.NB_excel_data_map), "Go On Cover function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("POE") || TestBase.product.equalsIgnoreCase("CTB")){
						customAssert.assertTrue(common_CTB.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");	
					}else{
					customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}
					TestUtil.reportStatus("Policy has been created with mentioned Status :<b>[ Submitted->Indicate->Indication Accepted->Quoted->On Cover ]</b>", "Info", true);
					
					break;
				case "Requote":
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Requote_excel_data_map,code,event,"Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
					Assert.assertTrue(common_VELA.funcQuoteCheck(common.Requote_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.Requote_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Requote_excel_data_map,code,event,"Quoted"), "Verify Policy Status (Quoted) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(common.funcGoOnCover(common.Requote_excel_data_map), "Go On Cover function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Requote_excel_data_map,code,event,"On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("POE") || TestBase.product.equalsIgnoreCase("CTB")){
						customAssert.assertTrue(common_CTB.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");	
					}else{
					customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("Requote_Status")+"  ]</b> status. ", "Info", true);
					
					break;
				case "Rewind":
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
					String quoteNumber = k.getText("quoteNumber");
					customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Rewind", (String)common.Rewind_excel_data_map.get("Automation Key"), "Rewind_QuoteNumber", quoteNumber,common.Rewind_excel_data_map),"Error while writing data to excel for field >NB_PolicyNumber<");
					customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("POE")|| TestBase.product.equalsIgnoreCase("CTB")){
						customAssert.assertTrue(common_CTB.transactionSummary((String)common.Rewind_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");	
					}else if(TestBase.product.equalsIgnoreCase("POB") || TestBase.product.equalsIgnoreCase("CTA")|| TestBase.product.equalsIgnoreCase("POE")){
						customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
						customAssert.assertTrue(common.transactionSummary((String)common.Rewind_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
					}else if(TestBase.product.equalsIgnoreCase("CCF")){
						customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
						customAssert.assertTrue(common.transactionSummary((String)common.Rewind_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
					}else{
						customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Document verification function is having issue(S) . ");
						customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.Rewind_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
						
					}
					
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("Rewind_Status")+"  ]</b> status. ", "Info", true);
					break;
				case "Cancelled":
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.CAN_excel_data_map,code,event,"Cancelled"), "Verify Policy Status (Cancelled) function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("POE") || TestBase.product.equalsIgnoreCase("CTB")){
						customAssert.assertTrue(common_CTB.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");	
					}else{
					customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)common.CAN_excel_data_map.get("CAN_Status")+"  ]</b> status. ", "Info", true);
					break;
					
				case "Endorsement Submitted":
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Submitted"), "Verify Policy Status (Endorsement Submitted) function is having issue(S) . ");
					customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"),"Unable to Click on Assign Underwriter button . ");
					customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter.");
					
					
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("MTA_Status")+"  ]</b> status. ", "Info", true);
					break;
					
				case "Endorsement Quoted":
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Submitted"), "Verify Policy Status (Endorsement Submitted) function is having issue(S) . ");
					customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"),"Unable to Click on Assign Underwriter button . ");
					customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter.");
				
					Assert.assertTrue(common_HHAZ.funcQuoteCheck(common.NB_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.MTA_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Quoted"), "Verify Policy Status (Endorsement Quoted) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (Endorsement Quoted) function is having issue(S) . ");
				
					
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("MTA_Status")+"  ]</b> status. ", "Info", true);
					break;
					
				case "Endorsement On Cover":
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Submitted"), "Verify Policy Status (Endorsement Submitted) function is having issue(S) . ");
					customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"),"Unable to Click on Assign Underwriter button . ");
					customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter.");
				
					customAssert.assertTrue(common.funcQuoteCheck(common.NB_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.MTA_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Quoted"), "Verify Policy Status (Endorsement Quoted) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Error in PDF Verification (Draft Documents) function is having issue(S) . ");
					customAssert.assertTrue(common.funcGoOnCover_Endorsement(common.NB_excel_data_map), "GoOnCover_Endorsement function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,(String)map_data.get("MTA_Status")), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Error in PDF Verification (Documents)(Endorsement On Cover) function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("POE")||TestBase.product.equalsIgnoreCase("CTB")){
						customAssert.assertTrue(common_CTB.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");	
					}else{
					customAssert.assertTrue(common_CTA.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}
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
					customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Error in PDF Verification (Draft Documents) function is having issue(S) . ");
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
				
					Assert.assertTrue(common.funcQuoteCheck(common.NB_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.MTA_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Quoted"), "Verify Policy Status (Endorsement Quoted) function is having issue(S) . ");
					//customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Error in PDF Verification (Draft Documents) function is having issue(S) . ");
					customAssert.assertTrue(common.funcGoOnCover_Endorsement(common.NB_excel_data_map), "GoOnCover_Endorsement function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement On Cover"), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
					//customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Error in PDF Verification (Documents)(Endorsement On Cover) function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("POE") || TestBase.product.equalsIgnoreCase("CTB")){
						customAssert.assertTrue(common_CTB.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");	
					}else{
					customAssert.assertTrue(common_CTA.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}
					common.Rewind_excel_data_map = new HashMap<>();
					common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
					
					Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", TestBase.product+"_Rewind.xlsx");
					common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, TestBase.product+"_Rewind_03");
					common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
							Events_Suite_TC_Xls);
					common.transaction_Details_Premium_Values.clear();
					if(TestBase.product.equalsIgnoreCase("POB")){
						common_POB.RewindFlow(code, "Rewind");
					}else if(TestBase.product.equalsIgnoreCase("POE")){
						common_POE.RewindFlow(code, "Rewind");
					}else if(TestBase.product.equalsIgnoreCase("CTA")){
						common_CTA.RewindFlow(code, "Rewind");}
					else if(TestBase.product.equalsIgnoreCase("CTB")){
						common_CTB.RewindFlow(code, "Rewind");
					}
					
					customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status (Endorsement Submitted (Rewind)) function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"), "Navigation problem to Premium Summary page .");
					customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement On Cover"), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Document verification function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("POE")|| TestBase.product.equalsIgnoreCase("CTB")){
						customAssert.assertTrue(common_CTB.transactionSummary((String)common.Rewind_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");	
					}else{
					customAssert.assertTrue(common_CTA.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
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
					customAssert.assertTrue(transactionSummary((String)common.CAN_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					// Reinstate Function
					customAssert.assertTrue(common.ReinstatePolicy(common.NB_excel_data_map));
//					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
//					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.CAN_excel_data_map,code,event,"On Cover"), "Verify Policy Status (Re-Instate) function is having issue(S) . ");
				
					// Cancellation CODE will appear over here...
					
					TestUtil.reportStatus("Current Flow is for <b> 'Reinstate' the Cancelled policy", "Info", true);
					break;
				case "Renewal On Cover":
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
					Assert.assertTrue(common_VELA.funcQuoteCheck(common.Renewal_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.Renewal_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal Quoted"), "Verify Policy Status (Quoted) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(common.funcGoOnCover(common.Renewal_excel_data_map), "Go On Cover function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("POB")|| TestBase.product.equalsIgnoreCase("CTA")){
						customAssert.assertTrue(common_CTA.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}else if(TestBase.product.equalsIgnoreCase("POE") || TestBase.product.equalsIgnoreCase("CCI") || TestBase.product.equalsIgnoreCase("CTB")){
						customAssert.assertTrue(common_CTB.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("Renewal_Status")+"  ]</b> status. ", "Info", true);
					break;
				case "Renewal NTU":
					//Not Taken Up
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
					Assert.assertTrue(common_VELA.funcQuoteCheck(common.Renewal_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.Renewal_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal Quoted"), "Verify Policy Status (Quoted) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					Assert.assertTrue(common.funcNTU(common.Renewal_excel_data_map));
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal NTU"), "Verify Policy Status (NTU) function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyNTUstatus(common.Renewal_excel_data_map), "Verify Policy Status (NTU Page) function is having issue(S) . ");
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("Renewal_Status")+"  ]</b> status. ", "Info", true);
					break;
				case "Renewal Declined":
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
					Assert.assertTrue(common.funcDecline(common.Renewal_excel_data_map));
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal Declined"), "Verify Policy Status (Declined) function is having issue(S) . ");
					//funcVerifyDeclineNTUstatus
					customAssert.assertTrue(common.funcVerifyDeclineNTUstatus(common.Renewal_excel_data_map), "Verify Policy Status (Decline Page) function is having issue(S) . ");
					break;
				case "Renewal Rewind":
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
					Assert.assertTrue(common_VELA.funcQuoteCheck(common.Renewal_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.Renewal_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal Quoted"), "Verify Policy Status (Quoted) function is having issue(S) . ");
					//customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(common.funcGoOnCover(common.Renewal_excel_data_map), "Go On Cover function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					//customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("POB")|| TestBase.product.equalsIgnoreCase("CTA")){
						customAssert.assertTrue(common_CTA.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}else if(TestBase.product.equalsIgnoreCase("POE") || TestBase.product.equalsIgnoreCase("CTB")){
						customAssert.assertTrue(common_CTB.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}
					
					if(TestBase.product.equalsIgnoreCase("POB")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "POB_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "POB_Rewind_02");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_POB.RenewalRewindFlow(code, "Rewind");
					}else if(TestBase.product.equalsIgnoreCase("POE")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "POE_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "POE_Rewind_01");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_POE.RenewalRewindFlow(code, "Rewind");
					}else if(TestBase.product.equalsIgnoreCase("CTA")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CTA_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "CTA_Rewind_03");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_CTA.RewindFlow(code, "Rewind");
					}else if(TestBase.product.equalsIgnoreCase("CTB")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CTB_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "CTB_Rewind_03");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_CTB.RewindFlow(code, "Rewind");
					}
					
					
					customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Rewind_excel_data_map,code,event,"Renewal Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
					customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Rewind_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Document verification function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("POB") || TestBase.product.equalsIgnoreCase("CTA")){
						customAssert.assertTrue(common_CTA.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}else if(TestBase.product.equalsIgnoreCase("POE")||TestBase.product.equalsIgnoreCase("CTB")){
						customAssert.assertTrue(common_CTB.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("Renewal_Status")+"  ]</b> status. ", "Info", true);
					break;
				case "Renewal Endorsment On Cover":
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
					Assert.assertTrue(common_VELA.funcQuoteCheck(common.Renewal_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.Renewal_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal Quoted"), "Verify Policy Status (Quoted) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(common.funcGoOnCover(common.Renewal_excel_data_map), "Go On Cover function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					//customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("POB")||TestBase.product.equalsIgnoreCase("CTA")){
						customAssert.assertTrue(common_CTA.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}else if(TestBase.product.equalsIgnoreCase("POE")||TestBase.product.equalsIgnoreCase("CTB")){
						customAssert.assertTrue(common_CTB.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}
					common.transaction_Details_Premium_Values.clear();
					if(TestBase.product.equalsIgnoreCase("POB")){
						common_CCD.isMTARewindStarted= false;
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "POB_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "POB_MTA_02");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_POB.MTAFlow(code, "MTA");
						
					}else if(TestBase.product.equalsIgnoreCase("POE")){
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "POE_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "POE_MTA_02");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_POE.MTAFlow(code, "MTA");
						
					}else if(TestBase.product.equalsIgnoreCase("CTA")){
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CTA_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "CTA_MTA_02");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_CTA.MTAFlow(code, "MTA");
						
					}else if(TestBase.product.equalsIgnoreCase("CTB")){
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CTB_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, (String)common.Renewal_excel_data_map.get("MTA_UnderlyingFlow"));//"CTB_MTA_03");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_CTB.MTAFlow(code, "MTA");
						
					}else if(TestBase.product.equalsIgnoreCase("CCI")){
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CCI_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, (String)common.Renewal_excel_data_map.get("MTA_UnderlyingFlow"));
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_CCI.MTAFlow(code, "MTA");
						
					}
					
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Endorsement Submitted"), "Verify Policy Status (Endorsement Submitted) function is having issue(S) . ");
					customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"),"Unable to Click on Assign Underwriter button . ");
					customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter.");
				
					Assert.assertTrue(common_VELA.funcQuoteCheck(common.Renewal_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.MTA_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Endorsement Quoted"), "Verify Policy Status (Endorsement Quoted) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Error in PDF Verification (Draft Documents) function is having issue(S) . ");
					
					customAssert.assertTrue(common.funcGoOnCover_Endorsement(common.Renewal_excel_data_map), "GoOnCover_Endorsement function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Endorsement On Cover"), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
					
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Error in PDF Verification (Documents)(Endorsement On Cover) function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("POB")||TestBase.product.equalsIgnoreCase("CTA")){
						customAssert.assertTrue(common_CTA.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}else if(TestBase.product.equalsIgnoreCase("POE")||TestBase.product.equalsIgnoreCase("CTB")){
						customAssert.assertTrue(common_CTB.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					}
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("Renewal_Status")+"  ]</b> status. ", "Info", true);
					
					break;
				default:
					break;
				}
			}catch(Throwable t){
				ret_value = false;
				quoteStatus = "";
			}
			
			return ret_value;
		}
	/*
     * ------------------------------------------------------------
     * PDF Verification Handling function. START
     * ------------------------------------------------------------
     */
		 
	public boolean funcPDFdocumentVerification(String docType){
		boolean retvalue = true;
		int doc_fail_count = 0;
		err_count=0;
		Map<Object,Object> data_map = null;
		if(common.currentRunningFlow.equals("NB")){
			data_map = common.NB_excel_data_map;}
		else if(common.currentRunningFlow.equals("MTA")){
			data_map = common.MTA_excel_data_map;
		}else if(common.currentRunningFlow.equals("Renewal")){
			data_map = common.Renewal_excel_data_map;
		}else if(common.currentRunningFlow.equals("Rewind")){
			data_map = common.Rewind_excel_data_map;
		}else if(common.currentRunningFlow.equals("Requote")){
			data_map = common.Requote_excel_data_map;
		}
		
		
		try{
			if(((String)data_map.get("DocumentVerification")).equals("No")){
				TestUtil.reportStatus("<b> PDF document verification is 'No' hence skipped verification . ", "Info", false);
				TestUtil.reportStatus("<b> Total count of document verification is : [ 0 ]</b>", "Info", false);
			}else{
				customAssert.assertTrue(common.funcButtonSelection(docType) , "Unable to click on <b>[  "+docType+"  ]</b>.");
					doc_fail_count = doc_fail_count + iteratePDFDocuments(docType);
					customAssert.SoftAssertEquals(doc_fail_count, 0,"Verification failure in "+docType);
					final_err_pdf_count = final_err_pdf_count + doc_fail_count;
					TestUtil.reportStatus(docType+" verification is Completed .", "Info", true);
					customAssert.assertTrue(k.Click("Tax_adj_BackBtn"), "Unable to click on back button.");
			}
			
			
			return retvalue;
		}
		catch(Throwable t) {
            String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
            TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
            Assert.fail("Unable to do verification for documents. \n", t);
            return false;
		}
		
	}
				
	/*
	 * PDF verification related functions:
	 * 
	 *  @Param docType - Either "Draft Documents" OR "Documents"
	 *  @Variable o - Number of documents
	 *  @Variable count - Documents data verification failure count
	 *  @Variable counter - incremental count for downloading same file (Used in PDFFileHandling method)
	 */
	
	public int iteratePDFDocuments(String docType) throws ParseException, IOException, InterruptedException{
		try{
			List<WebElement> l_row = driver.findElements(By.xpath("html/body/div[3]/form/div/div[2]/table/tbody/tr/td/a/span"));
			int row_size = l_row.size();
			
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
				case "Requote":
					data_map = common.Requote_excel_data_map;
					break;
				case "Rewind":
					data_map = common.Rewind_excel_data_map;
					break;
			}
			
			if(row_size>0){
				for(int r=0;r<row_size;r++){
					String doc_name = l_row.get(r).getText();			
					if((String)data_map.get("pdf_"+doc_name)!=null && ((String)data_map.get("pdf_"+doc_name)).equals("Yes")){
						l_row.get(r).click();
						k.waitFiveSeconds();
						TestUtil.reportStatus("Document -"+doc_name+" is present.", "info", true);
						counter = 0;
						pdf_count++;
						err_count = err_count + PDFFileHandling(doc_name,docType);
					
						l_row = driver.findElements(By.xpath("html/body/div[3]/form/div/div[2]/table/tbody/tr"));
					}else{
						continue;
					}
				}
			}
			TestUtil.reportStatus("Total count of <b>[  "+docType+" is : "+pdf_count+"  ]</b>", "Info", false);
			}catch(Throwable t)
			{
				String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
				TestUtil.reportFunctionFailed("Failed in "+methodName+" function");   
				TestUtil.reportStatus("<p style='color:red'> Error in iteratePDFDocuments Method </p>", "Fail", true);
			 	return 1;
			}
			return err_count;
	}
			
	public int PDFFileHandling(String fileName,String docType) throws IOException, ParseException, InterruptedException{
		String file_Name=null;
		String PDFCodePath = null;
		String fileCode=null;
		int dataVerificationFailureCount = 0;
		String code = CommonFunction_VELA.product;
		try{
			//TestUtil.reportStatus(fileName+" document verification is started for product - [<b>"+code+"</b>] ", "Info", false);
			String PDFPath= workDir+"\\src\\com\\selenium\\Execution_Report\\Report\\PDF";
			PDFCodePath = PDFPath+"\\"+code;
			File pdfFldr = new File(PDFPath);
			File pdfCodeFldr=new File(PDFCodePath);
			if(!pdfFldr.exists() && !pdfFldr.isDirectory()){
				pdfFldr.mkdir();
				}
			if(!pdfCodeFldr.exists() && !pdfCodeFldr.isDirectory()){
				pdfCodeFldr.mkdir();
				
			}
			
			fileCode = downloadPDF(code,fileName);
			Thread.sleep(4000);
			file_Name = PDFCodePath+"\\"+fileCode+".pdf";
			//System.out.println(file_Name);
				
			File file = new File(file_Name);
			FileInputStream fis = new FileInputStream(file);
			TestUtil.reportStatus(fileName+" file is downloaded to the specified folder and ready for verification.", "Info", false);
			
			dataVerificationFailureCount = dataVerificationFailureCount + PDFDataVerification(fis,fileName,docType);
			TestUtil.reportStatus("<b> Total count of document verification is : [ "+pdf_count+" ]</b>", "Info", false);
			
}
			
			// Below code will handle PDF failure up to 3 chance.
			catch(FileNotFoundException fnf)
			{
				
				if(counter==3){
					TestUtil.reportStatus("<b>Due to some reason , Not able to downalod -[  "+fileName+"  ]. 3 times tried to download his file .</b>", "Info", false);
				}else{
					counter++;
					TestUtil.reportStatus("Due to some reason , Not able to downalod - "+fileName+" . Retried downloading.", "Info", false);
					PDFFileHandling(fileName,docType);
				}
			return 0;
			}
			catch(NullPointerException npe)
			{
						TestUtil.reportStatus("Data Issue while verification . ", "Fail", false);
						return 1;
			}
				
			
			catch(Exception ex)
			{
				if(counter==3){
					TestUtil.reportStatus("<b>Due to some reason , Not able to downalod -[  "+fileName+"  ]. 3 times tried to download his file .</b>", "Info", false);
			}else{
				counter++;
				TestUtil.reportStatus("Due to some reason , Not able to downalod - "+fileName+" . Retried downloading.", "Info", false);
				PDFFileHandling(fileName,docType);
			}
		    return 0;
			}
		
		customAssert.assertTrue(fileDeletion(PDFCodePath) , "Unable to delete extra pdf files from folder : "+PDFCodePath);
		
		return dataVerificationFailureCount;
			
	}
	
	/*
	 * This function will delete unnecessary 
	 * PDF files files from mentioned Path
	 * 
	 * @param filePath - folder path from where extra files needs to be deleted
	 */
	
	public boolean fileDeletion(String filePath) {
		
		File f = new File(filePath);
		File[] listOfFiles = f.listFiles();
		int size = listOfFiles.length;
		//System.out.println(size);
		
		for(int i=0;i<size;i++){
			if (listOfFiles[i].isFile()) {
				
				int position = listOfFiles[i].getName().lastIndexOf(".");
				if(position!=-1){
					/*
					 *  @Variable fileName - it will return fileName without extention
					 */
					String fileName = listOfFiles[i].getName().substring(0,position);  
					if(fileName.contains("pdf")){
						listOfFiles[i].delete();
					}
				}
			}
		}
		return true;
	}
			
	/**
	 * @param code
	 * @param fileName
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public String downloadPDF(String code,String fileName) throws InterruptedException, IOException {
	    
	    String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.US).format(new Date());
	    try{
	     
	    	WebDriverWait wait = new WebDriverWait(driver, 50); 
	    	WebElement menuItem = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='mainpanel']/div[3]/object")));  // until this submenu is found
	     
		     if(menuItem!=null && menuItem.isDisplayed()){
		           
		           driver.findElement(By.xpath("//*[@id='mainpanel']/div[3]/object")).click();
		           Actions action = new Actions(driver);
		           Thread.sleep(10000);
		           action.keyDown(Keys.SHIFT).sendKeys(Keys.TAB).keyUp(Keys.SHIFT).perform();
		           action.keyDown(Keys.SHIFT).sendKeys(Keys.TAB).keyUp(Keys.SHIFT).perform();
		           action.keyDown(Keys.SHIFT).sendKeys(Keys.TAB).keyUp(Keys.SHIFT).perform();
		           action.keyDown(Keys.SHIFT).sendKeys(Keys.TAB).keyUp(Keys.SHIFT).perform();
		           action.keyDown(Keys.SHIFT).sendKeys(Keys.TAB).keyUp(Keys.SHIFT).perform();
		           action.sendKeys(Keys.ENTER).perform();
		           k.waitTenSeconds();
		           
		           String fileCode = code+"_"+fileName+"_"+timeStamp;
		           
		           String[] parms = {"wscript", workDir+"\\src\\BatFiles\\CloseDialog.vbs", workDir+"\\src\\com\\selenium\\Execution_Report\\Report\\PDF\\"+code+"\\"+fileCode};
		           Runtime.getRuntime().exec(parms);
		           return fileCode;
		     }else{
		           
		           return "";
		     }
	    }catch(Exception e){
	           String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	           TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
	           TestUtil.reportStatus("<p style='color:red'> PDF Document is not visible for download </p>", "Fail", true);
	           return "";
	    }
	}
			
	/**
	 * @param fis - Downloaded file referance
	 * @param fileName - e.g. Policy Schedule
	 * @param docType - Draft Documents/Documents
	 */
	
@SuppressWarnings("rawtypes")
public int PDFDataVerification(FileInputStream fis,String fileName,String docType) throws IOException, ParseException, InterruptedException {
		
		String parsedText=null;
		int fail_count=0;
		PDFParser parser = new PDFParser(fis);
		parser.parse();
		COSDocument cosDoc = parser.getDocument();
	    PDDocument pdDoc = new PDDocument(cosDoc);
	    PDFTextStripper pdfStripper = new PDFTextStripper();
	    
		parsedText = pdfStripper.getText(pdDoc);
		int count = pdDoc.getNumberOfPages();
		DecimalFormat newSum = new DecimalFormat("#,###.00");
		pdfStripper.setStartPage(1);
		pdfStripper.setEndPage(count);
		Map<Object,Object> mdata =  null;
		
		Map<String, List<Map<String, String>>> Map_InnerPagesMaps = null;
		
		switch (common.currentRunningFlow) {
			case "Renewal":
				mdata = common.Renewal_excel_data_map;
				Map_InnerPagesMaps = common.Renewal_Structure_of_InnerPagesMaps;
				break;
			case "MTA":
				mdata = common.MTA_excel_data_map;
				Map_InnerPagesMaps = common.MTA_Structure_of_InnerPagesMaps;
			break;
			case "Rewind":
				mdata = common.Rewind_excel_data_map;
				Map_InnerPagesMaps = common.Rewind_Structure_of_InnerPagesMaps;
			break;
			case "Requote":
				mdata = common.Requote_excel_data_map;
				Map_InnerPagesMaps = common.Requote_Structure_of_InnerPagesMaps;
			break;
			default:
				mdata=common.NB_excel_data_map;
				Map_InnerPagesMaps = common.NB_Structure_of_InnerPagesMaps;
			break;
		
		}
		//System.out.println(parsedText);
		switch(fileName){
		
			case "Policy Schedule":
				
				DecimalFormat formatter = new DecimalFormat("#,###,###.##");
				int incrementalDays = Integer.parseInt(CONFIG.getProperty("DaysIncrementNumber"));
				int policyDuration = Integer.parseInt((String)mdata.get("PS_Duration"));
				fail_count=0;
						
				if(docType.contains("Draft")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("QUOTATION SUMMARY"), "Document : QUOTATION SUMMARY", fileName);
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("POLICY SCHEDULE"), "Document : POLICY SCHEDULE", fileName);
				}
				if(common.currentRunningFlow.equals("Renewal")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName")), "Insured Name : "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName") , fileName);
				}else if(common.currentRunningFlow.equals("MTA") || common.currentRunningFlow.equals("Requote") || common.currentRunningFlow.equals("Rewind")){
					if(TestBase.businessEvent.equals("Renewal")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.Renewal_excel_data_map.get("Renewal_ClientName")), "Insured Name : "+(String)common.Renewal_excel_data_map.get("Renewal_ClientName") , fileName);
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.NB_excel_data_map.get("NB_ClientName")), "Insured Name : "+(String)common.NB_excel_data_map.get("NB_ClientName") , fileName);
					}
					
				}else{
				
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)mdata.get("NB_ClientName")), "Insured Name : "+(String)mdata.get("NB_ClientName") , fileName);
				}
				if(!common.currentRunningFlow.equals("Renewal")){
					if(common.currentRunningFlow.equals("Rewind") && TestBase.businessEvent.equalsIgnoreCase("Renewal")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.Renewal_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.Renewal_excel_data_map.get("QC_AgencyName") , fileName);
					}else if(common.currentRunningFlow.equals("MTA") && TestBase.businessEvent.equalsIgnoreCase("Renewal")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.Renewal_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.Renewal_excel_data_map.get("QC_AgencyName") , fileName);
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.NB_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.NB_excel_data_map.get("QC_AgencyName") , fileName);
					}
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.Renewal_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.Renewal_excel_data_map.get("QC_AgencyName") , fileName);
				}
				
				if(docType.contains("Draft")){
					if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
						if(common.currentRunningFlow.equalsIgnoreCase("MTA") && TestBase.businessEvent.equalsIgnoreCase("Renewal")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.Renewal_excel_data_map.get("Renewal_QuoteNumber")) ,"Quote Reference : "+common.Renewal_excel_data_map.get("Renewal_QuoteNumber"),fileName);
							
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quotation Date: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("QuoteDate"), -incrementalDays)) ,"Quote Date : "+common.daysIncrement((String)common.Renewal_excel_data_map.get("QuoteDate"), -incrementalDays),fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.NB_excel_data_map.get("NB_QuoteNumber")) ,"Quote Reference : "+common.NB_excel_data_map.get("NB_QuoteNumber"),fileName);
							
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quotation Date: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), -incrementalDays)) ,"Quote Date : "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), -incrementalDays),fileName);
						}
						
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_QuoteNumber")) ,"Quote Reference : "+common.Renewal_excel_data_map.get(common.currentRunningFlow+"_QuoteNumber"),fileName);
					}
				}else{
					if(!common.currentRunningFlow.equalsIgnoreCase("Renewal") && !common.currentRunningFlow.equalsIgnoreCase("Requote")){
						if(common.currentRunningFlow.equals("Rewind") && TestBase.businessEvent.equalsIgnoreCase("Renewal")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Policy Number : "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
						}else if(common.currentRunningFlow.equals("MTA") && TestBase.businessEvent.equalsIgnoreCase("Renewal")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Policy Number : "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.NB_excel_data_map.get("NB_PolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("NB_PolicyNumber"),fileName);
						}
												
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)mdata.get(common.currentRunningFlow+"_PolicyNumber")) ,"Policy Number : "+mdata.get(common.currentRunningFlow+"_PolicyNumber"),fileName);
					}
					if(((String)mdata.get("PS_DefaultStartEndDate")).equals("No")){
						if(common.currentRunningFlow.equals("MTA")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 0)) ,"Effective Date : "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"),0),fileName);
						}else if(common.currentRunningFlow.equals("Requote")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.NB_excel_data_map.get("PS_PolicyStartDate"), 0)) ,"Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);								
						}else if(common.currentRunningFlow.equals("Rewind")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.NB_excel_data_map.get("PS_PolicyStartDate"), 0)) ,"Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);								
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0)) ,"Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);							
						}
					}else{
						if(common.currentRunningFlow.equals("MTA")){
							if(!TestBase.businessEvent.equalsIgnoreCase("Renewal")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0)) ,"Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);
							}
						}else if(common.currentRunningFlow.equals("Requote")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), 0)) ,"Effective Date: "+common.daysIncrement((String)mdata.get("QC_InceptionDate"), 0),fileName);
							
						}else if(common.currentRunningFlow.equals("Rewind")){
							if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), 0)) ,"Effective Date: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), 0),fileName);
							}else if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 0)) ,"Effective Date: "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 0),fileName);
							}else{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), 0)) ,"Effective Date: "+common.daysIncrement((String)mdata.get("QC_InceptionDate"), 0),fileName);
								
							}
							
							
						}else if(common.currentRunningFlow.equals("Renewal")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), 0)) ,"Effective Date: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);
							
						}else{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)mdata.get("QC_InceptionDate"), 0)) ,"Effective Date: "+common.daysIncrement((String)mdata.get("QC_InceptionDate"), 0),fileName);
							 }
					}
					
				}
				if(!common.currentRunningFlow.equals("Renewal")){
					if(!common.currentRunningFlow.equals("MTA")){
						if(((String)mdata.get("PS_DefaultStartEndDate")).equals("No")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)mdata.get("QC_InceptionDate"), 0)), "Period of Insurance: From: "+common.daysIncrement((String)mdata.get("QC_InceptionDate"), 0) , fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)mdata.get("QC_InceptionDate"), policyDuration-1)), "To: "+common.daysIncrement((String)mdata.get("QC_InceptionDate"), policyDuration-1) , fileName);
						}else{
							if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), 0)), "Period of Insurance: From: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), 0) , fileName);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), policyDuration-1)), "To: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), policyDuration-1) , fileName);
							}else{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), 0)), "Period of Insurance: From: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), 0) , fileName);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), policyDuration-1)), "To: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), policyDuration-1) , fileName);
							}
							
						}
					}
				}
				
			switch(TestBase.product){
			case "POB":
				if(((String)mdata.get("CD_LossOfRentalIncome")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Loss of Rental Income Insured"), "Loss of Rental Income Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO LOSS OF RENTAL INCOME SECTION"), "APPENDIX TO LOSS OF RENTAL INCOME SECTION" , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Loss of Rental Income Not Insured"), "Loss of Rental Income Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_Liability")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Liability Insured"), "Liability Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO LIABILITY SECTION"), "APPENDIX TO LIABILITY SECTION" , fileName);
					String EL_LOI = formatter.format(Double.parseDouble((String)mdata.get("EL_LimitOfIndemnity")));
					String POL_LOI = formatter.format(Double.parseDouble((String)mdata.get("POL_IndemnityLimit")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Employers' Liability £"+EL_LOI), "Employers' Liability &pound;"+EL_LOI , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Property Owners Liability £"+POL_LOI), "Property Owners Liability &pound;"+POL_LOI , fileName);
					
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Liability Not Insured"), "Liability Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_CyberandDataSecurity")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Cyber & Data Security Insured"), "Cyber & Data Security Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO CYBER & DATA SECURITY SECTION"), "APPENDIX TO CYBER & DATA SECURITY SECTION" , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Cyber & Data Security Not Insured"), "Cyber & Data Security Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_Terrorism")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Terrorism Insured"), "Terrorism Insured" , fileName);
					if(((String)mdata.get("CD_MaterialDamage")).equals("Yes")){
						fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO TERRORISM SECTION"), "APPENDIX TO TERRORISM SECTION" , fileName);
					}
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Terrorism Not Insured"), "Terrorism Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_LegalExpenses")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Legal Expenses Insured"), "Legal Expenses Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO LEGAL EXPENSES SECTION"), "APPENDIX TO LEGAL EXPENSES SECTION" , fileName);
					
					String LE_LimitOfLiability = (String)mdata.get("LE_LimitOfLiability");
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Limit of Liability £"+LE_LimitOfLiability), "Limit of Liability &pound;"+LE_LimitOfLiability , fileName);
					
						
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Legal Expenses Not Insured"), "Legal Expenses Not Insured" , fileName);
				}
				break;
				
			case "POE":
				if(((String)mdata.get("CD_LossOfRentalIncome")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Loss of Rental Income Insured"), "Loss of Rental Income Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO LOSS OF RENTAL INCOME SECTION"), "APPENDIX TO LOSS OF RENTAL INCOME SECTION" , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Loss of Rental Income Not Insured"), "Loss of Rental Income Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_Liability")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Liability Insured"), "Liability Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO LIABILITY SECTION"), "APPENDIX TO LIABILITY SECTION" , fileName);
					String EL_LOI = formatter.format(Double.parseDouble((String)mdata.get("EL_LimitOfIndemnity")));
					String POL_LOI = formatter.format(Double.parseDouble((String)mdata.get("POL_IndemnityLimit")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Employers' Liability £"+EL_LOI), "Employers' Liability &pound;"+EL_LOI , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Property Owners Liability £"+POL_LOI), "Property Owners Liability &pound;"+POL_LOI , fileName);
					
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Liability Not Insured"), "Liability Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_Terrorism")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Terrorism Insured"), "Terrorism Insured" , fileName);
					if(((String)mdata.get("CD_MaterialDamage")).equals("Yes")){
						fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO TERRORISM SECTION"), "APPENDIX TO TERRORISM SECTION" , fileName);
					}
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Terrorism Not Insured"), "Terrorism Not Insured" , fileName);
				}
				
				break;
			case "CTA":
				if(((String)mdata.get("CD_BusinessInterruption")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Business Interruption Insured"), "Business Interruption Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO BUSINESS INTERRUPTION SECTION"), "APPENDIX TO BUSINESS INTERRUPTION SECTION" , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Business Interruption Not Insured"), "Business Interruption Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_Liability")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Liability Insured"), "Liability Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO LIABILITY SECTION"), "APPENDIX TO LIABILITY SECTION" , fileName);
					String EL_LOI = formatter.format(Double.parseDouble((String)mdata.get("EL_LimitOfIndemnity")));
					String PL_LOI = formatter.format(Double.parseDouble((String)mdata.get("PL_IndemnityLimit")));
					String PRL_LOI = formatter.format(Double.parseDouble((String)mdata.get("PRL_IndemnityLimit")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Employers' Liability £"+EL_LOI), "Employers' Liability &pound;"+EL_LOI , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Public Liability £"+PL_LOI), "Public Liability &pound;"+PL_LOI , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Products Liability £"+PRL_LOI), "Products Liability &pound;"+PRL_LOI , fileName);
					
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Liability Not Insured"), "Liability Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_SpecifiedAllRisks")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Specified All Risks Insured"), "Specified All Risks Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO SPECIFIED ALL RISKS SECTION"), "APPENDIX TO SPECIFIED ALL RISKS SECTION" , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Specified All Risks Not Insured"), "Specified All Risks Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_ContractorsAllRisks")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Contractors All Risks Insured"), "Contractors All Risks Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO CONTRACTORS ALL RISKS SECTION"), "APPENDIX TO CONTRACTORS ALL RISKS SECTION" , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Contractors All Risks Not Insured"), "Contractors All Risks Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_ComputersandElectronicRisks")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Computers & Electronic Risks Insured"), "Computers & Electronic Risks Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO COMPUTERS & ELECTRONIC RISKS SECTION"), "APPENDIX TO COMPUTERS & ELECTRONIC RISKS SECTION" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Subsection: Computers"), "Subsection: Computers" , fileName);
					
					String CER_Sum_Ins = formatter.format(Double.parseDouble((String)mdata.get("CER_Computers_SumInsured")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Sum Insured £"+CER_Sum_Ins), "Sum Insured &pound;"+CER_Sum_Ins , fileName);
					
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Subsection: E Risk: Virus & Hacking"), "Subsection: E Risk: Virus & Hacking" , fileName);
					
					String CER_Erisk_Virus_LOI = formatter.format(Double.parseDouble((String)mdata.get("CER_Erisk_VirusHacking")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Limit of Liability £"+CER_Erisk_Virus_LOI), "Limit of Liability &pound;"+CER_Erisk_Virus_LOI , fileName);
			
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Subsection: Additional Expenditure"), "Subsection: Additional Expenditure" , fileName);
					
					String CER_Additional_Sum_Ins = formatter.format(Double.parseDouble((String)mdata.get("CER_AdditionalExp_SumInsured")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Sum Insured £"+CER_Additional_Sum_Ins), "Sum Insured &pound;"+CER_Additional_Sum_Ins , fileName);
			
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Computers & Electronic Risks Not Insured"), "Computers & Electronic Risks Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_Money")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Money Insured"), "Money Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO MONEY SECTION"), "APPENDIX TO MONEY SECTION" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Bodily Injury Benefits"), "Bodily Injury Benefits" , fileName);
					
					String M_LossOfLimbs = formatter.format(Double.parseDouble((String)mdata.get("M_LossOfLimbs")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Loss of limbs £"+M_LossOfLimbs), "Loss of limbs &pound;"+M_LossOfLimbs , fileName);
					
					String M_LossOfSight = formatter.format(Double.parseDouble((String)mdata.get("M_LossOfSight")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Loss of sight £"+M_LossOfSight), "Loss of sight &pound;"+M_LossOfSight , fileName);
	
					String M_PermanentTotalDis = formatter.format(Double.parseDouble((String)mdata.get("M_PermanentTotalDis")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Permanent Total Disablement £"+M_PermanentTotalDis), "Permanent Total Disablement &pound;"+M_PermanentTotalDis , fileName);
	
					String M_TempTotalDisablement = formatter.format(Double.parseDouble((String)mdata.get("M_TempTotalDisablement")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Temporary Total Disablement (Up to 104 weeks) £"+M_TempTotalDisablement), "Temporary Total Disablement (Up to 104 weeks) &pound;"+M_TempTotalDisablement , fileName);
	
				
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Money Not Insured"), "Money Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_GoodsInTransit")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Goods In Transit Insured"), "Goods In Transit Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO GOODS IN TRANSIT SECTION"), "APPENDIX TO GOODS IN TRANSIT SECTION" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Conveyance Limit of Liability"), "Conveyance Limit of Liability" , fileName);
					
					String GIT_AnyOnePostalPackage = formatter.format(Double.parseDouble((String)mdata.get("GIT_AnyOnePostalPackage")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Any one postal package £"+GIT_AnyOnePostalPackage), "Any one postal package &pound;"+GIT_AnyOnePostalPackage , fileName);
					
					String GIT_AnyOneConsignment = formatter.format(Double.parseDouble((String)mdata.get("GIT_AnyOneConsignment")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Any one consignment by rail or road £"+GIT_AnyOneConsignment), "Any one consignment by rail or road &pound;"+GIT_AnyOneConsignment , fileName);
	
				
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Goods In Transit Not Insured"), "Goods In Transit Not Insured" , fileName);
				}
				
								
				if(((String)mdata.get("CD_CyberandDataSecurity")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Cyber & Data Security Insured"), "Cyber & Data Security Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO CYBER & DATA SECURITY SECTION"), "APPENDIX TO CYBER & DATA SECURITY SECTION" , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Cyber & Data Security Not Insured"), "Cyber & Data Security Not Insured" , fileName);
				}
				if(((String)mdata.get("CD_DirectorsandOfficers")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Directors & Officers Insured"), "Directors & Officers Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO DIRECTORS & OFFICERS LIABILITY SECTION"), "APPENDIX TO DIRECTORS & OFFICERS LIABILITY SECTION" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Applicable Excess"), "Applicable Excess" , fileName);
					
					String DO_Excess = formatter.format(Double.parseDouble((String)mdata.get("DO_Excess")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Directors and Officers Liability £"+DO_Excess), "Directors and Officers Liability &pound;"+DO_Excess , fileName);
					
					String DO_CorporateLiabilityExcess = formatter.format(Double.parseDouble((String)mdata.get("DO_CorporateLiabilityExcess")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Corporate Liability £"+DO_CorporateLiabilityExcess), "Corporate Liability &pound;"+DO_CorporateLiabilityExcess , fileName);
					
					String DO_EPL_Excess = formatter.format(Double.parseDouble((String)mdata.get("DO_EPL_Excess")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Employment Practices Liability £"+DO_EPL_Excess), "Employment Practices Liability &pound;"+DO_EPL_Excess , fileName);
				
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Directors & Officers Not Insured"), "Directors & Officers Not Insured" , fileName);
				}
				
				
									
				if(((String)mdata.get("CD_Terrorism")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Terrorism Insured"), "Terrorism Insured" , fileName);
					if(((String)mdata.get("CD_MaterialDamage")).equals("Yes")){
						fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO TERRORISM SECTION"), "APPENDIX TO TERRORISM SECTION" , fileName);
					}
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Terrorism Not Insured"), "Terrorism Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_LegalExpenses")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Legal Expenses Insured"), "Legal Expenses Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO LEGAL EXPENSES SECTION"), "APPENDIX TO LEGAL EXPENSES SECTION" , fileName);
					
					String LE_LimitOfLiability = (String)mdata.get("LE_LimitOfLiability");
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Limit of Liability £"+LE_LimitOfLiability), "Limit of Liability &pound;"+LE_LimitOfLiability , fileName);
					
						
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Legal Expenses Not Insured"), "Legal Expenses Not Insured" , fileName);
				}
				if(((String)mdata.get("CD_FidelityGuarantee")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Fidelity Guarantee Insured"), "Fidelity Guarantee Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO FIDELITY GUARANTEE SECTION"), "APPENDIX TO FIDELITY GUARANTEE SECTION" , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Fidelity Guarantee Not Insured"), "Fidelity Guarantee Not Insured" , fileName);
				}
				break;
				
			case "CTB":
				if(((String)mdata.get("CD_BusinessInterruption")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Business Interruption Insured"), "Business Interruption Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO BUSINESS INTERRUPTION SECTION"), "APPENDIX TO BUSINESS INTERRUPTION SECTION" , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Business Interruption Not Insured"), "Business Interruption Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_Liability")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Liability Insured"), "Liability Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO LIABILITY SECTION"), "APPENDIX TO LIABILITY SECTION" , fileName);
					String EL_LOI = formatter.format(Double.parseDouble((String)mdata.get("EL_LimitOfIndemnity")));
					String PL_LOI = formatter.format(Double.parseDouble((String)mdata.get("PL_IndemnityLimit")));
					String PRL_LOI = formatter.format(Double.parseDouble((String)mdata.get("PRL_IndemnityLimit")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Employers' Liability £"+EL_LOI), "Employers' Liability &pound;"+EL_LOI , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Public Liability £"+PL_LOI), "Public Liability &pound;"+PL_LOI , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Products Liability £"+PRL_LOI), "Products Liability &pound;"+PRL_LOI , fileName);
					
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Liability Not Insured"), "Liability Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_SpecifiedAllRisks")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Specified All Risks Insured"), "Specified All Risks Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO SPECIFIED ALL RISKS SECTION"), "APPENDIX TO SPECIFIED ALL RISKS SECTION" , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Specified All Risks Not Insured"), "Specified All Risks Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_ContractorsAllRisks")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Contractors All Risks Insured"), "Contractors All Risks Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO CONTRACTORS ALL RISKS SECTION"), "APPENDIX TO CONTRACTORS ALL RISKS SECTION" , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Contractors All Risks Not Insured"), "Contractors All Risks Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_ComputersandElectronicRisks")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Computers & Electronic Risks Insured"), "Computers & Electronic Risks Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO COMPUTERS & ELECTRONIC RISKS SECTION"), "APPENDIX TO COMPUTERS & ELECTRONIC RISKS SECTION" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Subsection: Computers"), "Subsection: Computers" , fileName);
					
					String CER_Sum_Ins = formatter.format(Double.parseDouble((String)mdata.get("CER_Computers_SumInsured")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Sum Insured £"+CER_Sum_Ins), "Sum Insured &pound;"+CER_Sum_Ins , fileName);
					
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Subsection: E Risk: Virus & Hacking"), "Subsection: E Risk: Virus & Hacking" , fileName);
					
					String CER_Erisk_Virus_LOI = formatter.format(Double.parseDouble((String)mdata.get("CER_Erisk_VirusHacking")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Limit of Liability £"+CER_Erisk_Virus_LOI), "Limit of Liability &pound;"+CER_Erisk_Virus_LOI , fileName);
			
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Subsection: Additional Expenditure"), "Subsection: Additional Expenditure" , fileName);
					
					String CER_Additional_Sum_Ins = formatter.format(Double.parseDouble((String)mdata.get("CER_AdditionalExp_SumInsured")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Sum Insured £"+CER_Additional_Sum_Ins), "Sum Insured &pound;"+CER_Additional_Sum_Ins , fileName);
			
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Computers & Electronic Risks Not Insured"), "Computers & Electronic Risks Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_Money")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Money Insured"), "Money Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO MONEY SECTION"), "APPENDIX TO MONEY SECTION" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Bodily Injury Benefits"), "Bodily Injury Benefits" , fileName);
					
					String M_LossOfLimbs = formatter.format(Double.parseDouble((String)mdata.get("M_LossOfLimbs")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Loss of limbs £"+M_LossOfLimbs), "Loss of limbs &pound;"+M_LossOfLimbs , fileName);
					
					String M_LossOfSight = formatter.format(Double.parseDouble((String)mdata.get("M_LossOfSight")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Loss of sight £"+M_LossOfSight), "Loss of sight &pound;"+M_LossOfSight , fileName);
	
					String M_PermanentTotalDis = formatter.format(Double.parseDouble((String)mdata.get("M_PermanentTotalDis")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Permanent Total Disablement £"+M_PermanentTotalDis), "Permanent Total Disablement &pound;"+M_PermanentTotalDis , fileName);
	
					String M_TempTotalDisablement = formatter.format(Double.parseDouble((String)mdata.get("M_TempTotalDisablement")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Temporary Total Disablement (Up to 104 weeks) £"+M_TempTotalDisablement), "Temporary Total Disablement (Up to 104 weeks) &pound;"+M_TempTotalDisablement , fileName);
	
				
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Money Not Insured"), "Money Not Insured" , fileName);
				}
				
				if(((String)mdata.get("CD_GoodsInTransit")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Goods In Transit Insured"), "Goods In Transit Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO GOODS IN TRANSIT SECTION"), "APPENDIX TO GOODS IN TRANSIT SECTION" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Conveyance Limit of Liability"), "Conveyance Limit of Liability" , fileName);
					
					String GIT_AnyOnePostalPackage = formatter.format(Double.parseDouble((String)mdata.get("GIT_AnyOnePostalPackage")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Any one postal package £"+GIT_AnyOnePostalPackage), "Any one postal package &pound;"+GIT_AnyOnePostalPackage , fileName);
					
					String GIT_AnyOneConsignment = formatter.format(Double.parseDouble((String)mdata.get("GIT_AnyOneConsignment")));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Any one consignment by rail or road £"+GIT_AnyOneConsignment), "Any one consignment by rail or road &pound;"+GIT_AnyOneConsignment , fileName);
	
				
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Goods In Transit Not Insured"), "Goods In Transit Not Insured" , fileName);
				}
				
								
				
//				if(((String)mdata.get("CD_DirectorsandOfficers")).equals("Yes")){
//					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Directors & Officers Insured"), "Directors & Officers Insured" , fileName);
//					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO DIRECTORS & OFFICERS LIABILITY SECTION"), "APPENDIX TO DIRECTORS & OFFICERS LIABILITY SECTION" , fileName);
//					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Applicable Excess"), "Applicable Excess" , fileName);
//					
//					String DO_Excess = formatter.format(Double.parseDouble((String)mdata.get("DO_Excess")));
//					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Directors and Officers Liability £"+DO_Excess), "Directors and Officers Liability &pound;"+DO_Excess , fileName);
//					
//					String DO_CorporateLiabilityExcess = formatter.format(Double.parseDouble((String)mdata.get("DO_CorporateLiabilityExcess")));
//					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Corporate Liability £"+DO_CorporateLiabilityExcess), "Corporate Liability &pound;"+DO_CorporateLiabilityExcess , fileName);
//					
//					String DO_EPL_Excess = formatter.format(Double.parseDouble((String)mdata.get("DO_EPL_Excess")));
//					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Employment Practices Liability £"+DO_EPL_Excess), "Employment Practices Liability &pound;"+DO_EPL_Excess , fileName);
//				
//					
//				}else{
//					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Directors & Officers Not Insured"), "Directors & Officers Not Insured" , fileName);
//				}
				
				
									
				if(((String)mdata.get("CD_Terrorism")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Terrorism Insured"), "Terrorism Insured" , fileName);
					if(((String)mdata.get("CD_MaterialDamage")).equals("Yes")){
						fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO TERRORISM SECTION"), "APPENDIX TO TERRORISM SECTION" , fileName);
					}
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Terrorism Not Insured"), "Terrorism Not Insured" , fileName);
				}
				
				
				if(((String)mdata.get("CD_FidelityGuarantee")).equals("Yes")){
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Fidelity Guarantee Insured"), "Fidelity Guarantee Insured" , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("APPENDIX TO FIDELITY GUARANTEE SECTION"), "APPENDIX TO FIDELITY GUARANTEE SECTION" , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Fidelity Guarantee Not Insured"), "Fidelity Guarantee Not Insured" , fileName);
				}
				break;
				
				}	
			
				// Verify Premium :
			if(TestBase.product.equals("POB") || TestBase.product.equals("POE")){
				
				if(common.currentRunningFlow.contains("NB") || common.currentRunningFlow.contains("Renewal") || common.currentRunningFlow.contains("Requote") || (common.currentRunningFlow.contains("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA"))){
					
					String total_Gross_Premium = "",total_I_tax = "";		
					
					total_Gross_Premium = (String)mdata.get("PS_Total_GP");
					total_I_tax = (String)mdata.get("PS_Total_GT");
				
							
					
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Premium £"+formatter.format((int) Math.floor(Double.parseDouble(total_Gross_Premium)))), "Premium = &pound;"+formatter.format((int) Math.floor(Double.parseDouble(total_Gross_Premium))) , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Insurance Premium Tax £"+formatter.format((int) Math.floor(Double.parseDouble(total_I_tax)))), "Insurance Premium Tax = &pound;"+formatter.format((int) Math.floor(Double.parseDouble(total_I_tax))) , fileName);
					double total_p=(Double.parseDouble(total_Gross_Premium)) + (Double.parseDouble(total_I_tax));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("TOTAL £"+formatter.format((int) Math.floor(total_p))), "TOTAL &pound;"+formatter.format((int) Math.floor(total_p)) , fileName);
					
					/*fail_count = fail_count + CommonFunction.verification(parsedText.contains("Premium £"+formatter.format((int) Math.floor(Double.parseDouble(total_Gross_Premium)))), "Premium = &pound;"+formatter.format((int) Math.floor(Double.parseDouble(total_Gross_Premium))) , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Insurance Premium Tax £"+formatter.format((int) Math.floor(Double.parseDouble(total_I_tax)))), "Insurance Premium Tax = &pound;"+formatter.format((int) Math.floor(Double.parseDouble(total_I_tax))) , fileName);
					double total_p=(Double.parseDouble(total_Gross_Premium)) + (Double.parseDouble(total_I_tax));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("TOTAL £"+formatter.format((int) Math.floor(total_p))), "TOTAL &pound;"+formatter.format((int) Math.floor(total_p)) , fileName);
					*/
				}else if (common.currentRunningFlow.contains("MTA") ||(common.currentRunningFlow.contains("Rewind") && common.businessEvent.equals("MTA"))) {
//					String total_Gross_Premium = (String)mdata.get("PS_Total_GP");
//					String total_I_tax = (String)mdata.get("PS_Total_GT");
					String total_Gross_Premium =common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium (GBP)").toString(); 
					String total_I_tax = common.transaction_Details_Premium_Values.get("Totals").get("Gross IPT (GBP)").toString();
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Premium £"+formatter.format(Double.parseDouble(total_Gross_Premium))), "Premium = &pound;"+formatter.format((Double.parseDouble(total_Gross_Premium))) , fileName);
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("Insurance Premium Tax £"+formatter.format(Double.parseDouble(total_I_tax))), "Insurance Premium Tax = &pound;"+formatter.format((Double.parseDouble(total_I_tax))) , fileName);
					double total_p=(Double.parseDouble(total_Gross_Premium)) + (Double.parseDouble(total_I_tax));
					fail_count = fail_count + CommonFunction.verification(parsedText.contains("TOTAL £"+formatter.format((total_p))), "TOTAL &pound;"+formatter.format((int) Math.floor(total_p)) , fileName);
				}
				
			}else{
					if(common.currentRunningFlow.contains("NB") || common.currentRunningFlow.contains("Renewal") || common.currentRunningFlow.contains("Requote") || (common.currentRunningFlow.contains("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA"))){
						
						String total_Gross_Premium = "",total_I_tax = "";		
						if(common_CTB.isTransTable){
								total_Gross_Premium = String.valueOf(common_CTB.transaction_Premium_Values.get("Totals").get("Gross Premium (GBP)"));
								total_I_tax = String.valueOf(common_CTB.transaction_Premium_Values.get("Totals").get("Gross IPT (GBP)"));
						}else{
								total_Gross_Premium = String.valueOf(common_CTB.PremiumSummary_Values.get("Total").get("PS_Total_GP"));
								total_I_tax = String.valueOf(common_CTB.PremiumSummary_Values.get("Total").get("PS_Total_GT"));
						}
						
						fail_count = fail_count + CommonFunction.verification(parsedText.contains("Premium £"+formatter.format((int) Math.floor(Double.parseDouble(total_Gross_Premium)))), "Premium = &pound;"+formatter.format((int) Math.floor(Double.parseDouble(total_Gross_Premium))) , fileName);
						fail_count = fail_count + CommonFunction.verification(parsedText.contains("Insurance Premium Tax £"+formatter.format((int) Math.floor(Double.parseDouble(total_I_tax)))), "Insurance Premium Tax = &pound;"+formatter.format((int) Math.floor(Double.parseDouble(total_I_tax))) , fileName);
						double total_p=(Double.parseDouble(total_Gross_Premium)) + (Double.parseDouble(total_I_tax));
						fail_count = fail_count + CommonFunction.verification(parsedText.contains("TOTAL £"+formatter.format((int) Math.floor(total_p))), "TOTAL &pound;"+formatter.format((int) Math.floor(total_p)) , fileName);
						
						/*fail_count = fail_count + CommonFunction.verification(parsedText.contains("Premium £"+formatter.format((int) Math.floor(Double.parseDouble(total_Gross_Premium)))), "Premium = &pound;"+formatter.format((int) Math.floor(Double.parseDouble(total_Gross_Premium))) , fileName);
						fail_count = fail_count + CommonFunction.verification(parsedText.contains("Insurance Premium Tax £"+formatter.format((int) Math.floor(Double.parseDouble(total_I_tax)))), "Insurance Premium Tax = &pound;"+formatter.format((int) Math.floor(Double.parseDouble(total_I_tax))) , fileName);
						double total_p=(Double.parseDouble(total_Gross_Premium)) + (Double.parseDouble(total_I_tax));
						fail_count = fail_count + CommonFunction.verification(parsedText.contains("TOTAL £"+formatter.format((int) Math.floor(total_p))), "TOTAL &pound;"+formatter.format((int) Math.floor(total_p)) , fileName);
						*/
					}else if (common.currentRunningFlow.contains("MTA") ||(common.currentRunningFlow.contains("Rewind") && common.businessEvent.equals("MTA"))) {
//						String total_Gross_Premium = (String)mdata.get("PS_Total_GP");
//						String total_I_tax = (String)mdata.get("PS_Total_GT");
						String total_Gross_Premium =common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium (GBP)").toString(); 
						String total_I_tax = common.transaction_Details_Premium_Values.get("Totals").get("Gross IPT (GBP)").toString();
						fail_count = fail_count + CommonFunction.verification(parsedText.contains("Premium £"+formatter.format(Double.parseDouble(total_Gross_Premium))), "Premium = &pound;"+formatter.format((Double.parseDouble(total_Gross_Premium))) , fileName);
						fail_count = fail_count + CommonFunction.verification(parsedText.contains("Insurance Premium Tax £"+formatter.format(Double.parseDouble(total_I_tax))), "Insurance Premium Tax = &pound;"+formatter.format((Double.parseDouble(total_I_tax))) , fileName);
						double total_p=(Double.parseDouble(total_Gross_Premium)) + (Double.parseDouble(total_I_tax));
						fail_count = fail_count + CommonFunction.verification(parsedText.contains("TOTAL £"+formatter.format((total_p))), "TOTAL &pound;"+formatter.format((int) Math.floor(total_p)) , fileName);
					}
				}
					
				break;
				
			case "Statement of Fact":
								
				formatter = new DecimalFormat("#,###,###.##");
				incrementalDays = Integer.parseInt(CONFIG.getProperty("DaysIncrementNumber"));
				policyDuration = Integer.parseInt((String)mdata.get("PS_Duration"));
				
				fail_count=0;
				
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("STATEMENT OF FACT"), "Document : QUOTE SCHEDULE", fileName);
				if(common.currentRunningFlow.equals("Renewal")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName")), "Insured Name : "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName") , fileName);
				}else{
					if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.Renewal_excel_data_map.get("Renewal_ClientName")), "Insured Name : "+(String)common.Renewal_excel_data_map.get("Renewal_ClientName") , fileName);
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.NB_excel_data_map.get("NB_ClientName")), "Insured Name : "+(String)common.NB_excel_data_map.get("NB_ClientName") , fileName);
					}
					
				}
				if(!common.currentRunningFlow.equals("Renewal")){
					if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.Renewal_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.Renewal_excel_data_map.get("QC_AgencyName") , fileName);
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.NB_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.NB_excel_data_map.get("QC_AgencyName") , fileName);
					}
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.Renewal_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.Renewal_excel_data_map.get("QC_AgencyName") , fileName);
				}
				
				if(docType.contains("Draft")){
					if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
						if(TestBase.businessEvent.equalsIgnoreCase("Renewal") && common.currentRunningFlow.equalsIgnoreCase("MTA")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.Renewal_excel_data_map.get("Renewal_QuoteNumber")) ,"Quote Reference : "+common.Renewal_excel_data_map.get("Renewal_QuoteNumber"),fileName);
							
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quotation Date: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("QuoteDate"), -incrementalDays)) ,"Quote Date : "+common.daysIncrement((String)common.Renewal_excel_data_map.get("QuoteDate"), -incrementalDays),fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.NB_excel_data_map.get("NB_QuoteNumber")) ,"Quote Reference : "+common.NB_excel_data_map.get("NB_QuoteNumber"),fileName);
							
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quotation Date: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), -incrementalDays)) ,"Quote Date : "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), -incrementalDays),fileName);
						}
						
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_QuoteNumber")) ,"Quote Reference : "+common.Renewal_excel_data_map.get(common.currentRunningFlow+"_QuoteNumber"),fileName);
					}
				}else{
					if(!common.currentRunningFlow.equalsIgnoreCase("Renewal") && !common.currentRunningFlow.equalsIgnoreCase("Requote")){
						if(common.currentRunningFlow.equalsIgnoreCase("Rewind") && TestBase.businessEvent.equalsIgnoreCase("Renewal")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Policy Number : "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
						}else if(common.currentRunningFlow.equalsIgnoreCase("MTA") && TestBase.businessEvent.equalsIgnoreCase("Renewal")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Policy Number : "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.NB_excel_data_map.get("NB_PolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("NB_PolicyNumber"),fileName);
						}
						
					}if(common.currentRunningFlow.equalsIgnoreCase("Rewind") || common.currentRunningFlow.equalsIgnoreCase("MTA")){
						if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Policy Number : "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.NB_excel_data_map.get("NB_PolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("NB_PolicyNumber"),fileName);
						}
						
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)mdata.get(common.currentRunningFlow+"_PolicyNumber")) ,"Policy Number : "+mdata.get(common.currentRunningFlow+"_PolicyNumber"),fileName);
					}
				}
			
				break;
				
			case "Employers Liability Certificate" :
				fail_count=0;
				
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("CERTIFICATE OF EMPLOYERS' LIABILITY INSURANCE"), "Document : CERTIFICATE OF EMPLOYERS' LIABILITY INSURANCE", fileName);
					if(docType.contains("Draft")){
						
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)mdata.get(common.currentRunningFlow+"_PolicyNumber")) ,"Policy Number : "+mdata.get(common.currentRunningFlow+"_PolicyNumber"),fileName);
					}
					
		//			fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 0)) ,"Effective Date : "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"),0),fileName);
					
					if(common.currentRunningFlow.equals("Renewal")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Name of Policy Holder: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName")), "Name of Policy Holder: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName") , fileName);
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Name of Policy Holder: "+(String)common.NB_excel_data_map.get("NB_ClientName")), "Name of Policy Holder: "+(String)common.NB_excel_data_map.get("NB_ClientName") , fileName);
					}
					if(((String)mdata.get("PS_DefaultStartEndDate")).equals("No")){
						if(common.currentRunningFlow.equals("MTA")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of Commencement of Insurance Policy:"+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 0)) ,"Date of Commencement of Insurance Policy:"+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"),0),fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of Expiry of Insurance Policy:"+common.daysIncrement((String)common.MTA_excel_data_map.get("PS_PolicyEndDate"), 0)) ,"Date of Expiry of Insurance Policy:"+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0),fileName);							
							
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of Commencement of Insurance Policy:"+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0)) ,"Date of Commencement of Insurance Policy:"+common.daysIncrement((String)mdata.get("QC_InceptionDate"), 0),fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of Expiry of Insurance Policy:"+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0)) ,"Date of Expiry of Insurance Policy:"+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0),fileName);							
							
							}
					}else{
						if(common.currentRunningFlow.equals("MTA")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of Commencement of Insurance Policy:"+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 0)) ,"Date of Commencement of Insurance Policy:"+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"),0),fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of Expiry of Insurance Policy:"+common.daysIncrement((String)common.MTA_excel_data_map.get("PS_PolicyEndDate"), 0)) ,"Date of Expiry of Insurance Policy:"+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0),fileName);							
							
							}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of Commencement of Insurance Policy: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0)) ,"Date of Commencement of Insurance Policy:"+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of Expiry of Insurance Policy: "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0)) ,"Date of Expiry of Insurance Policy:"+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0),fileName);							
							
							}
					}
				break;
				
			case "Policy Schedule Personal Accident":
				
				formatter = new DecimalFormat("#,###,###.##");
				incrementalDays = Integer.parseInt(CONFIG.getProperty("DaysIncrementNumber"));
				policyDuration = Integer.parseInt((String)mdata.get("PS_Duration"));
				
				fail_count=0;
						
				if(docType.contains("Draft")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("QUOTATION SCHEDULE"), "Document : QUOTATION SCHEDULE", fileName);
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("POLICY SCHEDULE"), "Document : POLICY SCHEDULE", fileName);
				}
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("PERSONAL ACCIDENT"), "PERSONAL ACCIDENT", fileName);
				
				if(common.currentRunningFlow.equals("Renewal")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName")), "Insured Name : "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName") , fileName);
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.NB_excel_data_map.get("NB_ClientName")), "Insured Name : "+(String)common.NB_excel_data_map.get("NB_ClientName") , fileName);
				}
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.NB_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.NB_excel_data_map.get("QC_AgencyName") , fileName);
				
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Correspondence Address: "+(String)common.NB_excel_data_map.get("CC_Address")), "Correspondence Address:  "+(String)common.NB_excel_data_map.get("CC_Address") , fileName);
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Postcode: "+(String)common.NB_excel_data_map.get("CC_Postcode")), "Postcode: "+(String)common.NB_excel_data_map.get("CC_Postcode") , fileName);
				
				if(docType.contains("Draft")){
					if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.NB_excel_data_map.get("NB_QuoteNumber")) ,"Quote Reference : "+common.NB_excel_data_map.get("NB_QuoteNumber"),fileName);
						
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quotation Date: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), -incrementalDays)) ,"Quote Date : "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), -incrementalDays),fileName);
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_QuoteNumber")) ,"Quote Reference : "+common.Renewal_excel_data_map.get(common.currentRunningFlow+"_QuoteNumber"),fileName);
					}
				}else{
					if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)mdata.get(common.currentRunningFlow+"_PolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("PG_CarrierPolicyNumber"),fileName);
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)mdata.get(common.currentRunningFlow+"_PolicyNumber")) ,"Policy Number : "+mdata.get(common.currentRunningFlow+"_PolicyNumber"),fileName);
					}
				}
				if(((String)mdata.get("PS_DefaultStartEndDate")).equals("No")){
					if(common.currentRunningFlow.equals("MTA")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 0)) ,"Effective Date : "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"),0),fileName);
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0)) ,"Effective Date : "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"),0),fileName);
						}
				}else{
					if(common.currentRunningFlow.equals("MTA")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0)) ,"Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);							
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To:"+common.daysIncrement((String)mdata.get("QC_DeadlineDate"), 0)) ,"To:"+common.daysIncrement((String)mdata.get("QC_DeadlineDate"), 0),fileName);							
						
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0)) ,"Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0)) ,"To:"+common.daysIncrement((String)mdata.get("QC_DeadlineDate"), 0),fileName);							
							
						}
				}
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium £"+formatter.format(Double.parseDouble((String)mdata.get("PS_PersonalAccidentOptional_GP")))), "Premium £"+formatter.format(Double.parseDouble((String)mdata.get("PS_PersonalAccidentOptional_GP"))) , fileName);
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+newSum.format(Double.parseDouble((String)mdata.get("PS_PersonalAccidentOptional_GT")))), "Insurance Premium Tax £"+newSum.format(Double.parseDouble((String)mdata.get("PS_PersonalAccidentOptional_GT"))) , fileName);
				double PAOTotal = Double.parseDouble((String)mdata.get("PS_PersonalAccidentOptional_GP")) + Double.parseDouble((String)mdata.get("PS_PersonalAccidentOptional_GT"));
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+newSum.format(PAOTotal)), "TOTAL £"+newSum.format(PAOTotal) , fileName);
				
				
				break;
		
		
			case "Policy Wording":
				
				TestUtil.reportStatus(fileName+" Verification Not in Scope . ", "Info", true);
				break;
				
			}
		
		return fail_count;
		
	}


/**
 * 
 * PDF Verifications Functions for REWIND Flow.
 * 
 * 
 * 
 */


public boolean funcPDFdocumentVerification_Rewind(String docType){
	boolean retvalue = true;
	int doc_fail_count = 0;
	err_count=0;
	Map<Object,Object> data_map = null;
	if(common.currentRunningFlow.equals("NB")){
		data_map = common.NB_excel_data_map;}
	else if(common.currentRunningFlow.equals("MTA")){
		data_map = common.MTA_excel_data_map;
	}
	else if(common.currentRunningFlow.equals("Renewal")){
		data_map = common.Renewal_excel_data_map;
	}
	
	
	try{
		if(((String)data_map.get("DocumentVerification")).equals("No")){
			TestUtil.reportStatus("<b> PDF document verification is 'No' hence skipped verification . ", "Info", false);
			TestUtil.reportStatus("<b> Total count of document verification is : [ 0 ]</b>", "Info", false);
		}else{
			customAssert.assertTrue(common.funcButtonSelection(docType) , "Unable to click on <b>[  "+docType+"  ]</b>.");
			
			String sDocuments =  (String)data_map.get("PG_SuppressDocumentation");
			if(sDocuments.contains("No")){
				doc_fail_count = doc_fail_count + iteratePDFDocuments_Rewind(docType);
				customAssert.SoftAssertEquals(doc_fail_count, 0,"Verification failure in "+docType);
				final_err_pdf_count = final_err_pdf_count + doc_fail_count;
				TestUtil.reportStatus(docType+" verification is Completed .", "Info", true);
			
			}else{
				List<WebElement> l_row = driver.findElements(By.xpath("html/body/div[3]/form/div/div[2]/table/tbody/tr/td/a/span"));
				int row_size = l_row.size();
				for(int r=0;r<row_size;r++){
					String doc_name = l_row.get(r).getText();
					
					if(!doc_name.contains("Policy Wording")){
						TestUtil.reportStatus(doc_name+"should not be generated as SuppressDocumentation is marked as Yes", "fail", true);
					}
				}
				
			}
			customAssert.assertTrue(k.Click("Tax_adj_BackBtn"), "Unable to click on back button.");
		}
		
		
		return retvalue;
	}
	catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Unable to do verification for documents. \n", t);
        return false;
	}
}
		
/*
* PDF verification related functions:
* 
*  @Param docType - Either "Draft Documents" OR "Documents"
*  @Variable o - Number of documents
*  @Variable count - Documents data verification failure count
*  @Variable counter - incremental count for downloading same file (Used in PDFFileHandling method)
*/

public int iteratePDFDocuments_Rewind(String docType) throws ParseException, IOException, InterruptedException{
try{
	List<WebElement> l_row = driver.findElements(By.xpath("html/body/div[3]/form/div/div[2]/table/tbody/tr/td/a/span"));
	int row_size = l_row.size();
	
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
	
	if(row_size>0){
		for(int r=0;r<row_size;r++){
			String doc_name = l_row.get(r).getText();
			if(doc_name.contains("Policy Wording NIG") || doc_name.contains("Policy Wording AVIVA") || doc_name.contains("Policy Wording NIG Logo")){
				TestUtil.reportStatus(" Policy Wording documentes Verification Not in Scope . ", "Info", true);
			} 
			
			String sValue =  (String)data_map.get("PG_SuppressPremiumFromDocumentation");
			if(sValue.contains("No")&&doc_name.contains("Policy Schedule - Client")){
				TestUtil.reportStatus(doc_name+"should not be generated as SuppressPremiumFromDocumentation is marked as Yes", "fail", true);
			}  
			
			if((String)data_map.get("pdf_"+doc_name)!=null && ((String)data_map.get("pdf_"+doc_name)).equals("Yes")){
				l_row.get(r).click();
				k.waitFiveSeconds();
				TestUtil.reportStatus("Document -"+doc_name+" is present.", "info", true);
				counter = 0;
				pdf_count++;
				err_count = err_count + PDFFileHandling_Rewind(doc_name,docType);
			
				l_row = driver.findElements(By.xpath("html/body/div[3]/form/div/div[2]/table/tbody/tr"));
			}else{
				continue;
			}
		}
	}
	TestUtil.reportStatus("Total count of <b>[  "+docType+" is : "+pdf_count+"  ]</b>", "Info", false);
	}catch(Throwable t)
	{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		TestUtil.reportFunctionFailed("Failed in "+methodName+" function");   
		TestUtil.reportStatus("<p style='color:red'> Error in iteratePDFDocuments Method </p>", "Fail", true);
	 	return 1;
	}
	return err_count;
}
	
public int PDFFileHandling_Rewind(String fileName,String docType) throws IOException, ParseException, InterruptedException{
	String file_Name=null;
	String PDFCodePath = null;
	String fileCode=null;
	int dataVerificationFailureCount = 0;
	String code = CommonFunction_VELA.product;
	try{
		//TestUtil.reportStatus(fileName+" document verification is started for product - [<b>"+code+"</b>] ", "Info", false);
		String PDFPath= workDir+"\\src\\com\\selenium\\Execution_Report\\Report\\PDF";
		PDFCodePath = PDFPath+"\\"+code;
		File pdfFldr = new File(PDFPath);
		File pdfCodeFldr=new File(PDFCodePath);
		if(!pdfFldr.exists() && !pdfFldr.isDirectory()){
			pdfFldr.mkdir();
			}
		if(!pdfCodeFldr.exists() && !pdfCodeFldr.isDirectory()){
			pdfCodeFldr.mkdir();
			
		}
		
		fileCode = downloadPDF(code,fileName);
		Thread.sleep(4000);
		file_Name = PDFCodePath+"\\"+fileCode+".pdf";
		//System.out.println(file_Name);
			
		File file = new File(file_Name);
		FileInputStream fis = new FileInputStream(file);
		TestUtil.reportStatus(fileName+" file is downloaded to the specified folder and ready for verification.", "Info", false);
		
		dataVerificationFailureCount = dataVerificationFailureCount + PDFDataVerification_Rewind(fis,fileName,docType);
		TestUtil.reportStatus("<b> Total count of document verification is : [ "+pdf_count+" ]</b>", "Info", false);
		
		}
		
		// Below code will handle PDF failure up to 3 chance.
		catch(FileNotFoundException fnf)
		{
			
			if(counter==3){
				TestUtil.reportStatus("<b>Due to some reason , Not able to downalod -[  "+fileName+"  ]. 3 times tried to download his file .</b>", "Info", false);
			}else{
				counter++;
				TestUtil.reportStatus("Due to some reason , Not able to downalod - "+fileName+" . Retried downloading.", "Info", false);
				PDFFileHandling(fileName,docType);
			}
		return 0;
		}
		catch(NullPointerException npe)
		{
					TestUtil.reportStatus("Data Issue while verification . ", "Fail", false);
					return 1;
		}
			
		
		catch(Exception ex)
		{
			if(counter==3){
				TestUtil.reportStatus("<b>Due to some reason , Not able to downalod -[  "+fileName+"  ]. 3 times tried to download his file .</b>", "Info", false);
		}else{
			counter++;
			TestUtil.reportStatus("Due to some reason , Not able to downalod - "+fileName+" . Retried downloading.", "Info", false);
			PDFFileHandling(fileName,docType);
		}
	    return 0;
		}
	
	customAssert.assertTrue(fileDeletion(PDFCodePath) , "Unable to delete extra pdf files from folder : "+PDFCodePath);
	
	return dataVerificationFailureCount;
	
}
	
/**
* @param code
* @param fileName
* @return
* @throws InterruptedException
* @throws IOException
*//*
public String downloadPDF(String code,String fileName) throws InterruptedException, IOException {

String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.US).format(new Date());
try{
 
	WebDriverWait wait = new WebDriverWait(driver, 50); 
	WebElement menuItem = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='mainpanel']/div[3]/object")));  // until this submenu is found
 
     if(menuItem!=null && menuItem.isDisplayed()){
           
           driver.findElement(By.xpath("//*[@id='mainpanel']/div[3]/object")).click();
           Actions action = new Actions(driver);
           Thread.sleep(10000);
           action.keyDown(Keys.SHIFT).sendKeys(Keys.TAB).keyUp(Keys.SHIFT).perform();
           action.keyDown(Keys.SHIFT).sendKeys(Keys.TAB).keyUp(Keys.SHIFT).perform();
           action.keyDown(Keys.SHIFT).sendKeys(Keys.TAB).keyUp(Keys.SHIFT).perform();
           action.keyDown(Keys.SHIFT).sendKeys(Keys.TAB).keyUp(Keys.SHIFT).perform();
           action.keyDown(Keys.SHIFT).sendKeys(Keys.TAB).keyUp(Keys.SHIFT).perform();
           action.sendKeys(Keys.ENTER).perform();
           k.waitTenSeconds();
           
           String fileCode = code+"_"+fileName+"_"+timeStamp;
           
           String[] parms = {"wscript", workDir+"\\src\\BatFiles\\CloseDialog.vbs", workDir+"\\src\\com\\selenium\\Execution_Report\\Report\\PDF\\"+code+"\\"+fileCode};
           Runtime.getRuntime().exec(parms);
           return fileCode;
     }else{
           
           return "";
     }
}catch(Exception e){
       String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
       TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
       TestUtil.reportStatus("<p style='color:red'> PDF Document is not visible for download </p>", "Fail", true);
       return "";
}
}*/
	
/**
* @param fis - Downloaded file referance
* @param fileName - e.g. Policy Schedule
* @param docType - Draft Documents/Documents
*/

	@SuppressWarnings("rawtypes")
	public int PDFDataVerification_Rewind(FileInputStream fis,String fileName,String docType) throws IOException, ParseException, InterruptedException {
	
		String parsedText=null;
		int fail_count=0;
		PDFParser parser = new PDFParser(fis);
		parser.parse();
		COSDocument cosDoc = parser.getDocument();
	    PDDocument pdDoc = new PDDocument(cosDoc);
	    PDFTextStripper pdfStripper = new PDFTextStripper();
	    
		parsedText = pdfStripper.getText(pdDoc);
		int count = pdDoc.getNumberOfPages();
		pdfStripper.setStartPage(1);
		pdfStripper.setEndPage(count);
		Map<Object,Object> mdata =  null;
		
		Map<String, List<Map<String, String>>> Map_InnerPagesMaps = null;
		
		switch (common.currentRunningFlow) {
			case "Renewal":
				mdata = common.Renewal_excel_data_map;
				Map_InnerPagesMaps = common.Renewal_Structure_of_InnerPagesMaps;
				break;
			case "MTA":
				mdata = common.MTA_excel_data_map;
				Map_InnerPagesMaps = common.MTA_Structure_of_InnerPagesMaps;
			break;
			default:
				mdata=common.NB_excel_data_map;
				Map_InnerPagesMaps = common.NB_Structure_of_InnerPagesMaps;
			break;
		
		}
		//System.out.println(parsedText);
		switch(fileName){
		
			case "Policy Schedule":
				
				DecimalFormat formatter = new DecimalFormat("#,###,###.##");
				int incrementalDays = Integer.parseInt(CONFIG.getProperty("DaysIncrementNumber"));
				int policyDuration = Integer.parseInt((String)mdata.get("PS_Duration"));
				fail_count=0;
						
				if(docType.contains("Draft")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("QUOTE SCHEDULE"), "Document : QUOTE SCHEDULE", fileName);
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("POLICY SCHEDULE"), "Document : POLICY SCHEDULE", fileName);
				}
				if(common.currentRunningFlow.equals("Renewal")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName")), "Insured Name : "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName") , fileName);
				}else if(common.currentRunningFlow.equals("MTA")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.NB_excel_data_map.get("NB_ClientName")), "Insured Name : "+(String)common.NB_excel_data_map.get("NB_ClientName") , fileName);
				}else{
				
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)mdata.get("NB_ClientName")), "Insured Name : "+(String)mdata.get("NB_ClientName") , fileName);
				}
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)mdata.get("QC_AgencyName")), "BROKER NAME - "+(String)mdata.get("QC_AgencyName") , fileName);
			
				if(docType.contains("Draft")){
					if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.NB_excel_data_map.get("NB_QuoteNumber")) ,"Quote Reference : "+common.NB_excel_data_map.get("NB_QuoteNumber"),fileName);
						
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quotation Date: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), -incrementalDays)) ,"Quote Date : "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), -incrementalDays),fileName);
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_QuoteNumber")) ,"Quote Reference : "+common.Renewal_excel_data_map.get(common.currentRunningFlow+"_QuoteNumber"),fileName);
					}
				}else{
					if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.NB_excel_data_map.get("PG_CarrierPolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("PG_CarrierPolicyNumber"),fileName);						
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)mdata.get(common.currentRunningFlow+"_PolicyNumber")) ,"Policy Number : "+mdata.get(common.currentRunningFlow+"_PolicyNumber"),fileName);
					}
					if(((String)mdata.get("PS_DefaultStartEndDate")).equals("No")){
						if(common.currentRunningFlow.equals("MTA")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 0)) ,"Effective Date : "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"),0),fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0)) ,"Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);							
						}
					}else{
						if(common.currentRunningFlow.equals("MTA")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 0)) ,"Effective Date : "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"),0),fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0)) ,"Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);							
						}
					}
					
				}
				if(!common.currentRunningFlow.equals("Renewal")){
					if(!common.currentRunningFlow.equals("MTA")){
						if(((String)mdata.get("PS_DefaultStartEndDate")).equals("No")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)mdata.get("QC_InceptionDate"), 0)), "Period of Insurance: From: "+common.daysIncrement((String)mdata.get("QC_InceptionDate"), 0) , fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)mdata.get("QC_InceptionDate"), policyDuration-1)), "To: "+common.daysIncrement((String)mdata.get("QC_InceptionDate"), policyDuration-1) , fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), 0)), "Period of Insurance: From: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), 0) , fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), policyDuration-1)), "To: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), policyDuration-1) , fileName);
			
						}
					}
				}
				
			
				// Cover Sections and Premium :
				
					if(((String)mdata.get("CD_Add_MaterialDamage")).equals("Yes")){
						String sCarrier = (String)mdata.get("PG_Carrier");
						if(sCarrier.contains("NIG")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Buildings Insured"), "Buildings Insured" , fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Asset Protection Property Damage – All Risks Operative"), "Asset Protection Property Damage – All Risks Operative" , fileName);
						}
						
						
						if(((String)Map_InnerPagesMaps.get("Property Details").get(0).get("PoD_AddBuildings")).equals("Yes")){
							String sumInsured = (String)Map_InnerPagesMaps.get("Property Details").get(0).get("AddBuilding_SumInsured");
							
							double amount = Double.parseDouble(sumInsured);
							DecimalFormat newSum = new DecimalFormat("#,###");
							System.out.println(newSum.format(amount));
							
							String sVal = (String)Map_InnerPagesMaps.get("Property Details").get(0).get("AddBuilding_Property");
							sVal = sVal.replaceAll("&"+"nbsp;", " "); 
							sVal = sVal.replaceAll(String.valueOf((char) 160), " ");
							
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(sVal+" £"+newSum.format(amount)), sVal+" £"+newSum.format(amount) , fileName);
						}
						
						if(((String)Map_InnerPagesMaps.get("Property Details").get(0).get("PoD_AddContents")).equals("Yes")){
							String sumInsured = (String)Map_InnerPagesMaps.get("Property Details").get(0).get("AddContents_SumInsured");
							double amount = Double.parseDouble(sumInsured);
							DecimalFormat newSum = new DecimalFormat("#,###");
							System.out.println(newSum.format(amount));
							
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)Map_InnerPagesMaps.get("Property Details").get(0).get("AddContents_Contents")+" £"+newSum.format(amount)), (String)Map_InnerPagesMaps.get("Property Details").get(0).get("AddContents_Contents")+" £"+newSum.format(amount) , fileName);
						}
					
					}else{
						String sCarrier = (String)mdata.get("PG_Carrier");
						if(sCarrier.contains("Aviva")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Asset Protection Property Damage – All Risks Not Operative"), "Asset Protection Property Damage – All Risks Not Operative" , fileName);
						}
					}
					
					// No any validation for BI Cover 
					//if(((String)mdata.get("CD_BusinessInterruption")).equals("Yes")){
						
					//}
					
					if(((String)mdata.get("CD_Add_Liabilities-POL")).equals("Yes")){
						String sCarrier = (String)mdata.get("PG_Carrier");
						if(sCarrier.contains("NIG")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Property Owners Liability Insured"), "Property Owners Liability Insured" , fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Legal Liabilities Property Owners Liability Operative"), "Legal Liabilities Property Owners Liability Operative" , fileName);
						}
						
						String LOI = (String)mdata.get("POL_LimitOfIndemnity");
						double amount = Double.parseDouble(LOI);
						DecimalFormat newSum = new DecimalFormat("#,###");
						System.out.println(newSum.format(amount));
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Property Owners Liability £" +newSum.format(amount)), "Property Owners Liability £" +newSum.format(amount) , fileName);
					}else{
						String sCarrier = (String)mdata.get("PG_Carrier");
						if(sCarrier.contains("Aviva")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Legal Liabilities Property Owners Liability Not Operative"), "Legal Liabilities Property Owners Liability Not Operative" , fileName);
						}
					}
					
					if(((String)mdata.get("CD_Add_Terrorism")).equals("Yes")){
						String sCarrier = (String)mdata.get("PG_Carrier");
						if(sCarrier.contains("NIG")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Terrorism Insured"), "Terrorism Insured" , fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Terrorism Operative"), "Terrorism Operative" , fileName);
						}					
					}else{
						String sCarrier = (String)mdata.get("PG_Carrier");
						if(sCarrier.contains("Aviva")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Terrorism Not Operative"), "Terrorism Not Operative" , fileName);
						}
					}
					
					if(((String)mdata.get("CD_Add_BespokeCover")).equals("Yes")){
						String sCarrier = (String)mdata.get("PG_Carrier");
						if(sCarrier.contains("NIG")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Bespoke Cover Sum Insured"), "Bespoke Cover Sum Insured" , fileName);
						}
						
						
						String besPokes = (String)mdata.get("Add_BeSpoke");
						String arrBesPoke[] = besPokes.split(";");
						
						for(int i = 0; i<arrBesPoke.length; i++ ){
							String sVal = (String)Map_InnerPagesMaps.get("Add BeSpokeCover").get(i).get("Add_BeSpokeCoverType");
							String sumVal = (String)Map_InnerPagesMaps.get("Add BeSpokeCover").get(i).get("Add_BeSpokeSumInsured");
							double amount = Double.parseDouble(sumVal);
							DecimalFormat newSum = new DecimalFormat("#,###");
							System.out.println(newSum.format(amount));
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(sVal+" £" +newSum.format(amount)), sVal+" £" +newSum.format(amount) , fileName);
						}
						
					}else{
						
					}
								
					
				// Verify Premium :
					if(common.currentRunningFlow.contains("NB") || common.currentRunningFlow.contains("Renewal") ){
						DecimalFormat newSum = new DecimalFormat("#,###.00");
						
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium excluding Terrorism £"+newSum.format(rewindDoc_Premium )), "Premium excluding Terrorism £"+newSum.format(rewindDoc_Premium ) , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Terrorism Premium £"+newSum.format(rewindDoc_TerP )), "Terrorism Premium £"+newSum.format(rewindDoc_TerP ) , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+newSum.format(rewindDoc_InsPTax)), "Insurance Premium Tax £"+newSum.format(rewindDoc_InsPTax) , fileName);
						rewindDoc_TotalP = rewindDoc_Premium + rewindDoc_TerP + rewindDoc_InsPTax;
						
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+newSum.format(rewindDoc_TotalP )), "TOTAL £"+newSum.format(rewindDoc_TotalP ) , fileName);
						
					}else if(common.currentRunningFlow.contains("MTA")){
						DecimalFormat newSum = new DecimalFormat("#,###.00");
						
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Additional Premium excluding Terrorism £"+newSum.format(rewindMTADoc_Premium)), "Additional Premium excluding Terrorism £"+newSum.format(rewindMTADoc_Premium) , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Additional Premium Terrorism £"+newSum.format(rewindMTADoc_TerP)), "Additional Premium Terrorism £"+newSum.format(rewindMTADoc_TerP) , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+newSum.format(rewindMTADoc_InsPTax )), "Insurance Premium Tax £"+newSum.format(rewindMTADoc_InsPTax ) , fileName);
						rewindMTADoc_TotalP  = rewindMTADoc_Premium + rewindMTADoc_TerP + rewindMTADoc_InsPTax;
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+newSum.format(rewindMTADoc_TotalP)), "TOTAL £"+newSum.format(rewindMTADoc_TotalP) , fileName);
						
					}
					
				
				
				// verify excess :
					String sExcessNo = (String)mdata.get("EXS_Properties");
					String arrExcess[] = sExcessNo.split(";");
					
					for(int i = 0; i<arrExcess.length; i++ ){
						String sExcessPName = (String)Map_InnerPagesMaps.get("Excess-Property").get(i).get("EXS_Property");
						String sExcessPDesc = (String)Map_InnerPagesMaps.get("Excess-Property").get(i).get("EXS_Description");									
						String sExcessPType = (String)Map_InnerPagesMaps.get("Excess-Property").get(i).get("EXS_ExcessType");
						String sExcessPVal = (String)Map_InnerPagesMaps.get("Excess-Property").get(i).get("EXS_ExcessValue");
						String sExcessPApplies = (String)Map_InnerPagesMaps.get("Excess-Property").get(i).get("EXS_ExcessApplies");
						
						double excessVal = Double.parseDouble(sExcessPVal);
						DecimalFormat newSum = new DecimalFormat("#,###");
						System.out.println(newSum.format(excessVal));
						
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(sExcessPName + " - " + sExcessPDesc), sExcessPName + " - " + sExcessPDesc , fileName);
						
						String arrExcessTypes[] = sExcessPType.split(":"); 
						
						for(int j = 0; j<arrExcessTypes.length; j++){
							if(sExcessPApplies.contains("Per Claim")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(arrExcessTypes[j] + " £" + newSum.format(excessVal)+" each and every claim"), arrExcessTypes[j] + " £" + newSum.format(excessVal)+" each and every claim" , fileName);
							}else if(sExcessPApplies.contains("Aggregate Deductible")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(arrExcessTypes[j] + " Aggregate Deductible applies see below"), arrExcessTypes[j] + " Aggregate Deductible applies see below" , fileName);
							}else if(sExcessPApplies.contains("Per Building")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(arrExcessTypes[j] + " £" + newSum.format(excessVal)+" each and every claim per building"), arrExcessTypes[j] + " £" + newSum.format(excessVal)+" each and every claim per building" , fileName);
							}else if(sExcessPApplies.contains("Per Endorsement")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(arrExcessTypes[j] + " Please refer to endorsements"), arrExcessTypes[j] + " Please refer to endorsements" , fileName);
							}else if(sExcessPApplies.contains("Per Unit")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(arrExcessTypes[j] + " £" + newSum.format(excessVal)+" each and every claim per unit"), arrExcessTypes[j] + " £" + newSum.format(excessVal)+" each and every claim per unit" , fileName);
							}else if(sExcessPApplies.contains("Not Insured")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(arrExcessTypes[j] + " Not Insured"), arrExcessTypes[j] + " Not Insured" , fileName);
							}							
						}	
					}
					
					
				break;
				
			case "Policy Schedule - Client":
								
				formatter = new DecimalFormat("#,###,###.##");
				incrementalDays = Integer.parseInt(CONFIG.getProperty("DaysIncrementNumber"));
				policyDuration = Integer.parseInt((String)mdata.get("PS_Duration"));
				
				fail_count=0;
						
				if(docType.contains("Draft")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("QUOTE SCHEDULE"), "Document : QUOTE SCHEDULE", fileName);
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("POLICY SCHEDULE"), "Document : POLICY SCHEDULE", fileName);
				}
				
				if(common.currentRunningFlow.equals("Renewal")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName")), "Insured Name : "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName") , fileName);
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.NB_excel_data_map.get("NB_ClientName")), "Insured Name : "+(String)common.NB_excel_data_map.get("NB_ClientName") , fileName);
				}
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.NB_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.NB_excel_data_map.get("QC_AgencyName") , fileName);
				
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Correspondence Address: "+(String)common.NB_excel_data_map.get("CC_Address")), "Correspondence Address:  "+(String)common.NB_excel_data_map.get("CC_Address") , fileName);
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Postcode: "+(String)common.NB_excel_data_map.get("CC_Postcode")), "Postcode: "+(String)common.NB_excel_data_map.get("CC_Postcode") , fileName);
				
				if(docType.contains("Draft")){
					if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.NB_excel_data_map.get("NB_QuoteNumber")) ,"Quote Reference : "+common.NB_excel_data_map.get("NB_QuoteNumber"),fileName);
						
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quotation Date: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), -incrementalDays)) ,"Quote Date : "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), -incrementalDays),fileName);
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_QuoteNumber")) ,"Quote Reference : "+common.Renewal_excel_data_map.get(common.currentRunningFlow+"_QuoteNumber"),fileName);
					}
				}else{
					if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.NB_excel_data_map.get("PG_CarrierPolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("PG_CarrierPolicyNumber"),fileName);
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)mdata.get(common.currentRunningFlow+"_PolicyNumber")) ,"Policy Number : "+mdata.get(common.currentRunningFlow+"_PolicyNumber"),fileName);
					}
				}
				
				String sPremium = (String)mdata.get("PG_SuppressPremiumFromDocumentation");
				if(sPremium.contains("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium excluding Terrorism Included in the Programme Cost"), "Premium excluding Terrorism Included in the Programme Cost" , fileName);
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Terrorism Premium Included in the Programme Cost"), "Terrorism Premium Included in the Programme Cost" , fileName);
				}
				
				// verify excess :
				sExcessNo = (String)mdata.get("EXS_Properties");
				String ArrExcess[] = sExcessNo.split(";");
				
				for(int i = 0; i<ArrExcess.length; i++ ){
					String sExcessPName = (String)Map_InnerPagesMaps.get("Excess-Property").get(i).get("EXS_Property");
					String sExcessPDesc = (String)Map_InnerPagesMaps.get("Excess-Property").get(i).get("EXS_Description");									
					String sExcessPType = (String)Map_InnerPagesMaps.get("Excess-Property").get(i).get("EXS_ExcessType");
					String sExcessPVal = (String)Map_InnerPagesMaps.get("Excess-Property").get(i).get("EXS_ExcessValue");
					String sExcessPApplies = (String)Map_InnerPagesMaps.get("Excess-Property").get(i).get("EXS_ExcessApplies");
					
					double excessVal = Double.parseDouble(sExcessPVal);
					DecimalFormat newSum = new DecimalFormat("#,###");
					System.out.println(newSum.format(excessVal));
					
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(sExcessPName + " - " + sExcessPDesc), sExcessPName + " - " + sExcessPDesc , fileName);
					
					String arrExcessTypes[] = sExcessPType.split(":"); 
					
					for(int j = 0; j<arrExcessTypes.length; j++){
						if(sExcessPApplies.contains("Per Claim")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(arrExcessTypes[j] + " £" + newSum.format(excessVal)+" each and every claim"), arrExcessTypes[j] + " £" + newSum.format(excessVal)+" each and every claim" , fileName);
						}else if(sExcessPApplies.contains("Aggregate Deductible")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(arrExcessTypes[j] + " Aggregate Deductible applies see below"), arrExcessTypes[j] + " Aggregate Deductible applies see below" , fileName);
						}else if(sExcessPApplies.contains("Per Building")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(arrExcessTypes[j] + " £" + newSum.format(excessVal)+" each and every claim per building"), arrExcessTypes[j] + " £" + newSum.format(excessVal)+" each and every claim per building" , fileName);
						}else if(sExcessPApplies.contains("Per Endorsement")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(arrExcessTypes[j] + " Please refer to endorsements"), arrExcessTypes[j] + " Please refer to endorsements" , fileName);
						}else if(sExcessPApplies.contains("Per Unit")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(arrExcessTypes[j] + " £" + newSum.format(excessVal)+" each and every claim per unit"), arrExcessTypes[j] + " £" + newSum.format(excessVal)+" each and every claim per unit" , fileName);
						}else if(sExcessPApplies.contains("Not Insured")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(arrExcessTypes[j] + " Not Insured"), arrExcessTypes[j] + " Not Insured" , fileName);
						}							
					}	
				}
				
				break;
		
			case "Aviva Terrorism Certificate":
				DecimalFormat newSum = new DecimalFormat("#,###.00");
				System.out.println(parsedText);
				
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TERRORISM INSURANCE CERTIFICATE"), "TERRORISM INSURANCE CERTIFICATE" , fileName);		
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurer(s): Aviva Insurance Limited"), "Insurer(s): Aviva Insurance Limited" , fileName);
				
				if(!common.currentRunningFlow.contains("Renewal")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("General Cover Policy No: "+(String)common.NB_excel_data_map.get("PG_CarrierPolicyNumber")), "General Cover Policy No: "+(String)common.NB_excel_data_map.get("PG_CarrierPolicyNumber") , fileName);
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("The Insured: "+(String)common.NB_excel_data_map.get(common.currentRunningFlow+"_ClientName")), "The Insured: "+(String)common.NB_excel_data_map.get(common.currentRunningFlow+"_ClientName") , fileName);
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yyyy");
				String sDuration = (String)mdata.get("PS_Duration");
				int EndDate = Integer.parseInt( sDuration);
				EndDate--;
								
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: Effective:         "+common.daysIncrement((String)mdata.get("QC_InceptionDate"),0)), "Period of Insurance: Effective:         "+common.daysIncrement((String)mdata.get("QC_InceptionDate"),0) , fileName);
				
				if(parsedText.contains("Expiring:         "+common.daysIncrement((String)mdata.get("QC_DeadlineDate"),EndDate))){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Expiring:         "+common.daysIncrement((String)mdata.get("QC_DeadlineDate"),EndDate)), "Expiring:         "+common.daysIncrement((String)mdata.get("QC_DeadlineDate"),EndDate) , fileName);
				}else{
					
					int t_Count = fail_count;
					
					String eDate[] = common.daysIncrement((String)mdata.get("QC_DeadlineDate"),EndDate).split(" ");
					String datePart1 = eDate[0] +" "+ eDate[1]; 
					t_Count = t_Count + CommonFunction_VELA.verification(parsedText.contains("Expiring:         "+datePart1), "Expiring:         "+datePart1 , fileName);
					t_Count = t_Count + CommonFunction_VELA.verification(parsedText.contains(eDate[2]), eDate[2] , fileName);
					
					if(t_Count == 0){
						fail_count = 0;
					}else{
						fail_count = t_Count;
					}
				}
				
				String sVal = "Renewal Date: "+common.daysIncrement((String)mdata.get("QC_DeadlineDate"),Integer.parseInt( sDuration));
				sVal = sVal.replaceAll("&"+"nbsp;", " "); 
				sVal = sVal.replaceAll(String.valueOf((char) 160), " ");
				
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(sVal), sVal , fileName);
				
				if(common.currentRunningFlow.contains("NB")){
					
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium Details: Insurance Premium:          £"+newSum.format(rewindDoc_TerP)), "Premium Details: Insurance Premium:          £"+newSum.format(rewindDoc_TerP) , fileName);
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax:   £"+newSum.format(rewindDoc_InsTaxTer)), "Insurance Premium Tax:   £"+newSum.format(rewindDoc_InsTaxTer) , fileName);
					tpTotal = rewindDoc_TerP + rewindDoc_InsTaxTer;
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Total Amount Due:           £"+newSum.format(tpTotal)), "Total Amount Due:           £"+newSum.format(tpTotal) , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium Details: Insurance Premium:          £"+newSum.format(rewindMTADoc_TerP )), "Premium Details: Insurance Premium:          £"+newSum.format(rewindMTADoc_TerP ) , fileName);
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax:   £"+newSum.format(rewindMTADoc_AddTaxTer)), "Insurance Premium Tax:   £"+newSum.format(rewindMTADoc_AddTaxTer) , fileName);
					
					tpTotal = rewindMTADoc_TerP + rewindMTADoc_AddTaxTer;
				
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Total Amount Due:           £"+newSum.format(tpTotal)), "Total Amount Due:           £"+newSum.format(tpTotal) , fileName);
				}
				break;
							
			case "Policy Wording":
				
				TestUtil.reportStatus(fileName+" Verification Not in Scope . ", "Info", true);
				break;
				
			}
		
		return fail_count;
	}
	
	@SuppressWarnings("unused")
	public boolean transactionSummary(String fileName,String testName,String event,String code){
   		Boolean retvalue = true;  
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
 				data_map = common.CAN_excel_data_map;
 				break;
			case "Rewind":
 				data_map = common.Rewind_excel_data_map;
 				break;
			}
   			customAssert.assertTrue(common.funcMenuSelection("Navigate", "Transaction Summary"), "Navigation problem to Transaction Summary page .");
   			
   			Assert.assertEquals(k.getText("Page_Header"),"Transaction Summary", "Not on Transaction Summary Page.");
   			String part1= "//*[@id='table0']/tbody";
   			String Recipient = null,exit = "";
   			int td=0;
   			String ActualDueDate , ExpecteTransactionDate , ActualTransationDate;
   			WebElement table = driver.findElement(By.xpath(part1));
   			List<WebElement> list = table.findElements(By.tagName("tr"));
   			outer:
   			for(int i=1;i<list.size();i++){
   				String trasacSummaryType = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[1]")).getText();
   				double Total =0.00;
   				String ExpecteDueDate = "";
   				switch (trasacSummaryType) {
   				case "New Business" : 
   					
   					Map<Object,Object> Outermap = null;
   					
   					
   					switch (common.currentRunningFlow) {
   						
   					case "NB":
   						Outermap = common.NB_excel_data_map;
   						break;
   					case "Requote":
   						Outermap = common.Requote_excel_data_map;
   						break;
   					
   					}
   					TestUtil.reportStatus("Verification Started on Transaction Summary page "+trasacSummaryType+" . ", "PASS", false);
   					ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
   					ExpecteDueDate = common.getLastDayOfMonth((String)Outermap.get("QuoteDate"), 1);
   					
   					if(ActualDueDate.equalsIgnoreCase(ExpecteDueDate)){
      					 String tMsg="Actual Due Date : <b>[  "+ActualDueDate+"  ]</b> has been matched with Expected Due Date : <b>[  "+ExpecteDueDate+"  ]</b>";
      					 TestUtil.reportStatus(tMsg, "Pass", false);
      					}
      					else{
      	   					 String tMsg="Actual Due Date : <b>[  "+ActualDueDate+"  ]</b> does not matche with Expected Due Date : <b>[   "+ExpecteDueDate+"  ]</b>";
      	   					 TestUtil.reportStatus(tMsg, "Fail", false);
      					}
   					ActualTransationDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[3]")).getText();
   					ExpecteTransactionDate = (String)Outermap.get("QuoteDate");
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
   						if(exit.equalsIgnoreCase("Total")){
   	   						i=j;
   	   						String actualTotal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[4]")).getText();  
   	   						CommonFunction.compareValues(Double.parseDouble(actualTotal), Double.parseDouble(common.roundedOff(Double.toString(Total))), "Transaction Summary Total");
   	   						customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Transaction Summary", (String)Outermap.get("Automation Key"), "TS_TransactionSummaryTotal", actualTotal,Outermap),"Error while writing Transaction Summary data to excel .");

   	   						break outer;
   	   						}
   						
   						if(transactSumVal.equalsIgnoreCase("")){
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[3]")).getText();
   	   						td=8;
   						}else{
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   	   						td=8;
   						}
   						
   						if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited")){
   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   						if(Recipient.equalsIgnoreCase("Brokerage Account")){
   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   					}
   					break;
   				case "Amended New Business" : 
   					
   					TestUtil.reportStatus("Verification Started on Transaction Summary page "+trasacSummaryType+" . ", "PASS", false);
   					ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
   					ExpecteDueDate = common.getLastDayOfMonth((String)common.NB_excel_data_map.get("QuoteDate"), 1);
   					
   					if(ActualDueDate.equalsIgnoreCase(ExpecteDueDate)){
      					 String tMsg="Actual Due Date : <b>[  "+ActualDueDate+"  ]</b> has been matched with Expected Due Date : <b>[  "+ExpecteDueDate+"  ]</b>";
      					 TestUtil.reportStatus(tMsg, "Pass", false);
      					}
      					else{
      	   					 String tMsg="Actual Due Date : <b>[  "+ActualDueDate+"  ]</b> does not matche with Expected Due Date : <b>[   "+ExpecteDueDate+"  ]</b>";
      	   					 TestUtil.reportStatus(tMsg, "Fail", false);
      					}
   					ActualTransationDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[3]")).getText();
   					ExpecteTransactionDate = (String)common.NB_excel_data_map.get("QuoteDate");
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
   						if(exit.equalsIgnoreCase("Total")){
   	   						i=j;
   	   						String actualTotal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[4]")).getText();  
   	   						CommonFunction.compareValues(Double.parseDouble(actualTotal), Double.parseDouble(common.roundedOff(Double.toString(Total))), "Transaction Summary Total");
   	   						customAssert.assertTrue(WriteDataToXl(event+"_NB", "Transaction Summary", (String)common.NB_excel_data_map.get("Automation Key"), "TS_TransactionSummaryTotal", actualTotal,common.NB_excel_data_map),"Error while writing Transaction Summary data to excel .");

   	   						break outer;
   	   						}
   						
   						if(transactSumVal.equalsIgnoreCase("")){
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[3]")).getText();
   	   						td=8;
   						}else{
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   	   						td=8;
   						}
   						
   						if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited")){
   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   						if(Recipient.equalsIgnoreCase("Brokerage Account")){
   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   					}
   					break;
   				case "Endorsement" : //MTA
   					
   					TestUtil.reportStatus("Verification Started on Transaction Summary page "+trasacSummaryType+"  . ", "Info", false);
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
   						if(exit.equalsIgnoreCase("Total")){
   	   						i=j;
   	   						String actualTotal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[4]")).getText();  
   	   						CommonFunction.compareValues(Double.parseDouble(actualTotal), Double.parseDouble(common.roundedOff(Double.toString(Total))), "Transaction Summary Total");
   	   						customAssert.assertTrue(WriteDataToXl(event+"_MTA", "Transaction Summary", (String)common.MTA_excel_data_map.get("Automation Key"), "TS_TransactionSummaryTotal", actualTotal,common.MTA_excel_data_map),"Error while writing Transaction Summary data to excel .");

   	   						break outer;
   	   						}
   						
   						if(transactSumVal.equalsIgnoreCase("")){
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[3]")).getText();
   	   						td=8;
   						}else{
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   	   						td=8;
   						}
   						
   						if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited")){
   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   						if(Recipient.equalsIgnoreCase("Brokerage Account")){
   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   					}
   					break;
   				case "Amended Endorsement" : //MTA Rewind
   					
   					TestUtil.reportStatus("Verification Started on Transaction Summary page "+trasacSummaryType+"  . ", "Info", false);
   					ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
   					ExpecteDueDate = common.getLastDayOfMonth((String)(String)common.MTA_excel_data_map.get("QuoteDate"), 1);
   					//For MTA Rewind Operation Due date calculated from from Quote date
   					if(ActualDueDate.equalsIgnoreCase(ExpecteDueDate)){
      					 String tMsg="Actual Due Date : <b>[  "+ActualDueDate+"  ]</b> has been matched with Expected Due Date : <b>[  "+ExpecteDueDate+"  ]</b>";
      					 TestUtil.reportStatus(tMsg, "Pass", false);
      					}
      					else{
      	   					 String tMsg="Actual Due Date : <b>[  "+ActualDueDate+"  ]</b> does not match with Expected Due Date : <b>[   "+ExpecteDueDate+"  ]</b>";
      	   					 TestUtil.reportStatus(tMsg, "Fail", false);
      					}
   					ActualTransationDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[3]")).getText();
   					ExpecteTransactionDate = (String)common.MTA_excel_data_map.get("QuoteDate");
   					if(ActualTransationDate.equalsIgnoreCase(ExpecteTransactionDate)){
      					 String tMsg="Actual Transaction Date : <b>[  "+ActualTransationDate+"  ]</b> has been matched with Expected Transaction Date : <b>[  "+ExpecteTransactionDate+"  ]</b>";
      					 TestUtil.reportStatus(tMsg, "Pass", false);
      					}
      					else{
      	   					 String tMsg="Actual Transaction Date : <b>[  "+ActualTransationDate+"  ]</b> does not match with Expected Transaction Date : <b>[  "+ExpecteTransactionDate+"  ]</b>";
      	   					 TestUtil.reportStatus(tMsg, "Fail", false);
      					}
   					for(int j=i;!exit.equalsIgnoreCase("Total");j++){
   						String transactSumVal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[1]")).getText();
   						exit = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[2]")).getText();
   						if(exit.equalsIgnoreCase("Total")){
   	   						i=j;
   	   						String actualTotal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[4]")).getText();  
   	   						CommonFunction.compareValues(Double.parseDouble(actualTotal), Double.parseDouble(common.roundedOff(Double.toString(Total))), "Transaction Summary Total");
   	   						customAssert.assertTrue(WriteDataToXl(event+"_MTA", "Transaction Summary", (String)common.MTA_excel_data_map.get("Automation Key"), "TS_TransactionSummaryTotal", actualTotal,common.MTA_excel_data_map),"Error while writing Transaction Summary data to excel .");

   	   						break outer;
   	   						}
   						
   						if(transactSumVal.equalsIgnoreCase("")){
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[3]")).getText();
   	   						td=8;
   						}else{
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   	   						td=8;
   						}
   						
   						if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited")){
   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   						if(Recipient.equalsIgnoreCase("Brokerage Account")){
   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   					}
   					break;
   				case "Renewal" : 
   					
   					TestUtil.reportStatus("Verification Started on Transaction Summary page "+trasacSummaryType+" . ", "PASS", false);
   					ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
   					ExpecteDueDate = common.getLastDayOfMonth((String)common.Renewal_excel_data_map.get("QuoteDate"), 1);
   					
   					if(ActualDueDate.equalsIgnoreCase(ExpecteDueDate)){
      					 String tMsg="Actual Due Date : <b>[  "+ActualDueDate+"  ]</b> has been matched with Expected Due Date : <b>[  "+ExpecteDueDate+"  ]</b>";
      					 TestUtil.reportStatus(tMsg, "Pass", false);
      					}
      					else{
      	   					 String tMsg="Actual Due Date : <b>[  "+ActualDueDate+"  ]</b> does not matche with Expected Due Date : <b>[   "+ExpecteDueDate+"  ]</b>";
      	   					 TestUtil.reportStatus(tMsg, "Fail", false);
      					}
   					ActualTransationDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[3]")).getText();
   					ExpecteTransactionDate = (String)common.Renewal_excel_data_map.get("QuoteDate");
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
   						if(exit.equalsIgnoreCase("Total")){
   	   						i=j;
   	   						String actualTotal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[4]")).getText();  
   	   						CommonFunction.compareValues(Double.parseDouble(actualTotal), Double.parseDouble(common.roundedOff(Double.toString(Total))), "Transaction Summary Total");
   	   						customAssert.assertTrue(WriteDataToXl(event+"_Renewal", "Transaction Summary", (String)common.Renewal_excel_data_map.get("Automation Key"), "TS_TransactionSummaryTotal", actualTotal,common.Renewal_excel_data_map),"Error while writing Transaction Summary data to excel .");

   	   						break outer;
   	   						}
   						
   						if(transactSumVal.equalsIgnoreCase("")){
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[3]")).getText();
   	   						td=8;
   						}else{
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   	   						td=8;
   						}
   						
   						if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited")){
   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   						if(Recipient.equalsIgnoreCase("Brokerage Account")){
   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   					}
   					break;
   				case "Amended Renewal" : 
   					
   					TestUtil.reportStatus("Verification Started on Transaction Summary page "+trasacSummaryType+" . ", "PASS", false);
   					ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
   					ExpecteDueDate = common.getLastDayOfMonth((String)common.Renewal_excel_data_map.get("QuoteDate"), 1);
   					
   					if(ActualDueDate.equalsIgnoreCase(ExpecteDueDate)){
      					 String tMsg="Actual Due Date : <b>[  "+ActualDueDate+"  ]</b> has been matched with Expected Due Date : <b>[  "+ExpecteDueDate+"  ]</b>";
      					 TestUtil.reportStatus(tMsg, "Pass", false);
      					}
      					else{
      	   					 String tMsg="Actual Due Date : <b>[  "+ActualDueDate+"  ]</b> does not matche with Expected Due Date : <b>[   "+ExpecteDueDate+"  ]</b>";
      	   					 TestUtil.reportStatus(tMsg, "Fail", false);
      					}
   					ActualTransationDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[3]")).getText();
   					ExpecteTransactionDate = (String)common.Renewal_excel_data_map.get("QuoteDate");
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
   						if(exit.equalsIgnoreCase("Total")){
   	   						i=j;
   	   						String actualTotal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[4]")).getText();  
   	   						CommonFunction.compareValues(Double.parseDouble(actualTotal), Double.parseDouble(common.roundedOff(Double.toString(Total))), "Transaction Summary Total");
   	   						customAssert.assertTrue(WriteDataToXl(event+"_Renewal", "Transaction Summary", (String)common.Renewal_excel_data_map.get("Automation Key"), "TS_TransactionSummaryTotal", actualTotal,common.Renewal_excel_data_map),"Error while writing Transaction Summary data to excel .");

   	   						break outer;
   	   						}
   						
   						if(transactSumVal.equalsIgnoreCase("")){
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[3]")).getText();
   	   						td=8;
   						}else{
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   	   						td=8;
   						}
   						
   						if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited")){
   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   						if(Recipient.equalsIgnoreCase("Brokerage Account")){
   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   					}
   					break;
   				case "Cancel" : //MTA
   					
   					TestUtil.reportStatus("Verification Started on Transaction Summary page "+trasacSummaryType+"  . ", "Info", false);
   					ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
   					ExpecteDueDate = common.getLastDayOfMonth((String)common.CAN_excel_data_map.get("CP_CancellationDate"), 1);
   					
   					if(ActualDueDate.equalsIgnoreCase(ExpecteDueDate)){
      					 String tMsg="Actual Due Date : <b>[  "+ActualDueDate+"  ]</b> has been matched with Expected Due Date : <b>[  "+ExpecteDueDate+"  ]</b>";
      					 TestUtil.reportStatus(tMsg, "Pass", false);
      					}
      					else{
      	   					 String tMsg="Actual Due Date : <b>[  "+ActualDueDate+"  ]</b> does not matche with Expected Due Date : <b>[   "+ExpecteDueDate+"  ]</b>";
      	   					 TestUtil.reportStatus(tMsg, "Fail", false);
      					}
   					ActualTransationDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[3]")).getText();
   					ExpecteTransactionDate = (String)common.NB_excel_data_map.get("QuoteDate");
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
   						if(exit.equalsIgnoreCase("Total")){
   	   						i=j;
   	   						String actualTotal = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[4]")).getText();  
   	   						CommonFunction.compareValues(Math.abs(Double.parseDouble(actualTotal)), Double.parseDouble(common.roundedOff(Double.toString(Total))), "Transaction Summary Total");
   	   						customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Transaction Summary", (String)common.CAN_excel_data_map.get("Automation Key"), "TS_TransactionSummaryTotal", actualTotal,common.CAN_excel_data_map),"Error while writing Transaction Summary data to excel .");

   	   						break outer;
   	   						}
   						
   						if(transactSumVal.equalsIgnoreCase("")){
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[3]")).getText();
   	   						td=8;
   						}else{
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   	   						td=8;
   						}
   						
   						if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited")){
   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   						if(Recipient.equalsIgnoreCase("Brokerage Account")){
   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   					}
   					break;
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

	public double calculateCarrierTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
            if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
				
            	Premium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Net Net Premium"));
				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax"));

			}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Net Net Premium"));
					IPT = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Insurance Tax"));
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			if(common.currentRunningFlow.equals("NB")){
					
				if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Net Net Premium"));
					IPT = Double.toString(transaction_Premium_Values.get("Totals").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_NetNetPemiumTotal");
					IPT = (String)data_map.get("PS_Total_GT");
				}
			}
			if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
				
				if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Net Net Premium"));
					IPT = Double.toString(transaction_Premium_Values.get("Totals").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_NetNetPemiumTotal");
					IPT = (String)data_map.get("PS_Total_GT");
				}
			}
			if(common.currentRunningFlow.equals("Requote")){
				
				if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Net Net Premium"));
					IPT = Double.toString(transaction_Premium_Values.get("Totals").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_NetNetPemiumTotal");
					IPT = (String)data_map.get("PS_Total_GT");
				}
			}
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Net Net Premium"));
					IPT = Double.toString(transaction_Premium_Values.get("Totals").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_NetNetPemiumTotal");
					IPT = (String)data_map.get("PS_Total_GT");
				}
			}
			String part1= "//*[@id='table0']/tbody";
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+5)+"]")).getText();
			double Due = Double.parseDouble(Premium)+ Double.parseDouble(IPT);
			String Dueamt= common.roundedOff(Double.toString(Due)) ;
			CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "Due amount of QBE Insurance");
			return Due;
		
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
			k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Calculate Legal Expenses ammount.  \n", t);
			return 0;
		}

	}
	
	public double calculatePENTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
            if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
				
            	Premium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Pen Comm"));
			}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Pen Comm"));

				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			if(common.currentRunningFlow.equals("NB")){
					
				if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Pen Comm"));
				
				}else{
					Premium = (String)data_map.get("PS_PenCommTotal");
				}
			}
			if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA") ){
				
				if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Pen Comm"));
				
				}else{
					Premium = (String)data_map.get("PS_PenCommTotal");
				}
			}
			if(common.currentRunningFlow.equals("Requote")){
				
				if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Pen Comm"));
				
				}else{
					Premium = (String)data_map.get("PS_PenCommTotal");
				}
			}
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Pen Comm"));
				
				}else{
					Premium = (String)data_map.get("PS_PenCommTotal");
				}
				
			}
			String part1= "//*[@id='table0']/tbody";
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td)+"]")).getText();
			double Due = Double.parseDouble(Premium);
			String Dueamt= common.roundedOff(Double.toString(Due)) ;
			CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "Brokrage account Due ");
			return Due;
		
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
			k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Calculate Legal Expenses ammount.  \n", t);
			return 0;
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
   
	/***
	 * 
	 * -------------------------------------------Endorsement script-----------------------------------------
	 * 
	 * 1. createAutoAddedEndorsementData - Create auto added endorsement data from input while creating NB flow.
	 * 2. verifyAutoAddedEndorsement - Verify Auto added Endorsement present on endorsement screen.
	 * 3. addStandardEndorsement - Add Standard and variable Endorsement from list present on Endorsement screen.
	 * 4. addFreeFormatEndorsement - Add Free format Endorsement.
	 * 5. requireEditEndorsement - To click on require edit link and update details to avoid hardstop on Quote check screen.
	 * 6. verifyEndorsementONPremiumSummary - Verify applied endorsement on Premium Summary screen.
	 */
	
	public boolean funcEndorsementOperations(Map<Object, Object> map_data) {
		
		try{
			
			customAssert.assertTrue(createAutoAddedEndorsementData(map_data),"Create Endorsement function is having issue(S).");
			customAssert.assertTrue(verifyAutoAddedEndorsement(map_data),"Verify auto added endorsement function is having issue(S).");
			customAssert.assertTrue(addStandardEndorsement(map_data),"Add standard endorsement function is having issue(S).");
			customAssert.assertTrue(addFreeFormatEndorsement(map_data),"Add free format endorsment function is having issue(S).");
			customAssert.assertTrue(requireEditEndorsement(map_data),"Require edit function is having issue(S).");
			
			return true;
			
		}catch(Throwable t){
			return false;
		}
		
	}
	
public boolean createAutoAddedEndorsementData(Map<Object, Object> map_data) {
		
		try{
			String code = null;
			
			String policy_status_actual = k.getText("Policy_status_header");
			switch (policy_status_actual) {
			case "Submitted (Rewind)":
				code = "Add_";
				break;
			case "Endorsement Submitted (Rewind)":
				code = "Add_";
				break;
			case "Submitted":
				switch (common_VELA.quoteStatus) {
				case "ReQuote":
					code = "Add_";
					break;
				default:
					code = "";
					break;
				}
			}
			
			Map<String, List<Map<String, String>>> map_InnerPages = null;
			
			
			if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
				if(!common.currentRunningFlow.contains("MTA")){
					map_InnerPages = common.Renewal_Structure_of_InnerPagesMaps;
					EndorsementCollectiveData = new LinkedHashMap<String, Map<String, String>>();
				}else{
					map_InnerPages = common.MTA_Structure_of_InnerPagesMaps;
				}
				
			}else if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
				if(common.currentRunningFlow.contains("NB")){
					map_InnerPages = common.NB_Structure_of_InnerPagesMaps;
					EndorsementCollectiveData = new LinkedHashMap<String, Map<String, String>>();
				}else{
					map_InnerPages = common.Rewind_Structure_of_InnerPagesMaps;
				}
			}else if(TestBase.businessEvent.equalsIgnoreCase("Requote")){
				if(common.currentRunningFlow.contains("NB")){
					map_InnerPages = common.NB_Structure_of_InnerPagesMaps;
					EndorsementCollectiveData = new LinkedHashMap<String, Map<String, String>>();
				}else{
					map_InnerPages = common.Requote_Structure_of_InnerPagesMaps;
				}
			}else if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
				if(common.currentRunningFlow.contains("NB")){
					map_InnerPages = common.NB_Structure_of_InnerPagesMaps;
					EndorsementCollectiveData = new LinkedHashMap<String, Map<String, String>>();
				}else{
					map_InnerPages = common.MTA_Structure_of_InnerPagesMaps;
				}
			}
			else{
				if(!common.currentRunningFlow.contains("MTA")){
					map_InnerPages = common.NB_Structure_of_InnerPagesMaps;
					EndorsementCollectiveData = new LinkedHashMap<String, Map<String, String>>();
				}else{
					map_InnerPages = common.MTA_Structure_of_InnerPagesMaps;
				}
			}
			
			
		}catch(Throwable t){
			k.ImplicitWaitOn();
			return false;
		}finally{
			k.ImplicitWaitOn();
		}
		return true;
	}
			
		
	
	@SuppressWarnings({ "static-access", "rawtypes" })
	public boolean verifyAutoAddedEndorsement(Map<Object, Object> map_data) {
		
		try{
			String flag = "found";
			k.ImplicitWaitOff();
			
			EndorsementCollectiveData.putAll(EndorsementFreeFormatData);
			
			String policyStatus = k.getText("Policy_status_header");
			boolean endorsementTable = k.isDisplayedField("EndorsementTable");
			String endorsementCode = null;
			if(endorsementTable){
				WebElement endorsementTableObject = k.getObject("EndorsementTable");
				List<WebElement> rowCount = endorsementTableObject.findElements(By.tagName("tr"));
				
					//Below code will check endorsement from application to Map.
					for(int i=0;i<rowCount.size()-1;i++){
						endorsementCode = endorsementTableObject.findElement(By.xpath("tbody/tr["+(i+1)+"]/td[1]")).getText();
						String sections = endorsementTableObject.findElement(By.xpath("tbody/tr["+(i+1)+"]/td[2]")).getText();
						String title = endorsementTableObject.findElement(By.xpath("tbody/tr["+(i+1)+"]/td[3]")).getText();
						String clauseType = endorsementTableObject.findElement(By.xpath("tbody/tr["+(i+1)+"]/td[4]")).getText();
						String type = endorsementTableObject.findElement(By.xpath("tbody/tr["+(i+1)+"]/td[5]")).getText();
						if(title.contains("Contract Lift Cover")){
							title = "CPA Contract Lift Cover(Lifted Goods)";
						}
						if(title.contains("Offshore Work")){
							title = "Offshore Work & Visits";
						}
						
						
						if(EndorsementCollectiveData.containsKey(endorsementCode)){
							try{
								if(verification(EndorsementCollectiveData.get(endorsementCode).get("ED_"+title.replaceAll(" ", "")+"_Code"),endorsementCode,"","Endorsement Code") &&
										verification(EndorsementCollectiveData.get(endorsementCode).get("ED_"+title.replaceAll(" ", "")+"_Section"),sections,"","Endorsement Section") &&
										verification(EndorsementCollectiveData.get(endorsementCode).get("ED_"+title.replaceAll(" ", "")+"_Title"),title,"","Endorsement title") && 
										verification(EndorsementCollectiveData.get(endorsementCode).get("ED_"+title.replaceAll(" ", "")+"_ClauseType"),clauseType,"","Endorsement clause type") &&
										verification(EndorsementCollectiveData.get(endorsementCode).get("ED_"+title.replaceAll(" ", "")+"_Type"),type,"","Endorsement type")){
									
									flag = "found";
									//TestUtil.reportStatus("Auto triggered endorsement <b> [ "+endorsementCode+" ] </b> is verified on Endorsement screen.", "Info", false);
									
								}
							}catch(Throwable t){
								if(verification(EndorsementCollectiveData.get(endorsementCode).get("ED_"+sections.replaceAll(" ", "")+"_Code"),endorsementCode,"","Endorsement Code") &&
										verification(EndorsementCollectiveData.get(endorsementCode).get("ED_"+sections.replaceAll(" ", "")+"_Section"),sections,"","Endorsement Section") &&
										verification(EndorsementCollectiveData.get(endorsementCode).get("ED_"+sections.replaceAll(" ", "")+"_Title"),title,"","Endorsement title") && 
										verification(EndorsementCollectiveData.get(endorsementCode).get("ED_"+sections.replaceAll(" ", "")+"_ClauseType"),clauseType,"","Endorsement clause type") &&
										verification(EndorsementCollectiveData.get(endorsementCode).get("ED_"+sections.replaceAll(" ", "")+"_Type"),type,"","Endorsement type")){
									
									flag = "found";
									//TestUtil.reportStatus("Auto triggered endorsement <b> [ "+endorsementCode+" ] </b> is verified on Endorsement screen.", "Info", false);
									
								}
							}
							
						}else{
							ExtraEndorsementList.put("ExtraED_"+endorsementCode+"_Code", endorsementCode);
							flag = "notFound";
						}
						if(flag.equalsIgnoreCase("notFound")){
							TestUtil.reportStatus("<p style='color:red'> Extra Auto triggered endorsement <b> [ "+endorsementCode+" ] </b> is getting displayed on Endorsement screen which should not be present. </p>", "Info", true);
						}
					}
					
					// Below code will check endorsement from Map to Application.
					Iterator it = EndorsementCollectiveData.entrySet().iterator();
					while(it.hasNext()){
						Map.Entry keyValue = (Map.Entry)it.next();
						String endorsementCodeMap = keyValue.getKey().toString();
						for(int i=0;i<rowCount.size()-1;i++){
							endorsementCode = endorsementTableObject.findElement(By.xpath("tbody/tr["+(i+1)+"]/td[1]")).getText();
					
							if(endorsementCodeMap.equalsIgnoreCase(endorsementCode)){
								String sections = endorsementTableObject.findElement(By.xpath("tbody/tr["+(i+1)+"]/td[2]")).getText();
								String title = endorsementTableObject.findElement(By.xpath("tbody/tr["+(i+1)+"]/td[3]")).getText();
								String clauseType = endorsementTableObject.findElement(By.xpath("tbody/tr["+(i+1)+"]/td[4]")).getText();
								String type = endorsementTableObject.findElement(By.xpath("tbody/tr["+(i+1)+"]/td[5]")).getText();
								if(title.contains("Contract Lift Cover")){
									title = "CPA Contract Lift Cover(Lifted Goods)";
								}
								if(title.contains("Offshore Work")){
									title = "Offshore Work & Visits";
								}
								
								String endorsementCodeMapData , sectionsMap , titleMap , clauseTypeMap , typeMap;
								
								
									endorsementCodeMapData = EndorsementCollectiveData.get(endorsementCodeMap).get("ED_"+title.replaceAll(" ", "")+"_Code");
									sectionsMap = EndorsementCollectiveData.get(endorsementCodeMap).get("ED_"+title.replaceAll(" ", "")+"_Section");
									titleMap = EndorsementCollectiveData.get(endorsementCodeMap).get("ED_"+title.replaceAll(" ", "")+"_Title");
									clauseTypeMap = EndorsementCollectiveData.get(endorsementCodeMap).get("ED_"+title.replaceAll(" ", "")+"_ClauseType");
									typeMap = EndorsementCollectiveData.get(endorsementCodeMap).get("ED_"+title.replaceAll(" ", "")+"_Type");
								
									if(endorsementCodeMapData==null || sectionsMap==null ||titleMap==null||clauseTypeMap==null||typeMap==null){
										endorsementCodeMapData = EndorsementCollectiveData.get(endorsementCodeMap).get("ED_"+sections.replaceAll(" ", "")+"_Code");
										sectionsMap = EndorsementCollectiveData.get(endorsementCodeMap).get("ED_"+sections.replaceAll(" ", "")+"_Section");
										titleMap = EndorsementCollectiveData.get(endorsementCodeMap).get("ED_"+sections.replaceAll(" ", "")+"_Title");
										clauseTypeMap = EndorsementCollectiveData.get(endorsementCodeMap).get("ED_"+sections.replaceAll(" ", "")+"_ClauseType");
										typeMap = EndorsementCollectiveData.get(endorsementCodeMap).get("ED_"+sections.replaceAll(" ", "")+"_Type");
									}
									
								
								if(verification(endorsementCodeMapData,endorsementCode,"","Endorsement Code") &&
										verification(sectionsMap,sections,"","Endorsement Section") &&
										verification(titleMap,title,"","Endorsement title") && 
										verification(clauseTypeMap,clauseType,"","Endorsement clause type") &&
										verification(typeMap,type,"","Endorsement type")){
									
									TestUtil.reportStatus("Auto triggered endorsement <b> [ "+endorsementCodeMap+" ] </b> is verified on Endorsement screen.", "Info", false);
									
								}
								flag="found";
								break;
							}else{
								flag = "notFound";
							}
						}
						if(flag.equalsIgnoreCase("notFound")){
							TestUtil.reportStatus("<p style='color:red'> Auto triggered endorsement <b> [ "+endorsementCodeMap+" ] </b> is not getting displayed on Endorsement screen. </p>", "Info", true);
						}
					}
					
			}else{
				Iterator it = EndorsementCollectiveData.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry keyValue = (Map.Entry)it.next();
					String endorsementCodeMap = keyValue.getKey().toString();
					TestUtil.reportStatus("<p style='color:red'> Auto triggered endorsement <b> [ "+endorsementCodeMap+" ] </b> is not getting displayed on Endorsement screen. </p>", "Info", true);
				}
				
				//TestUtil.reportStatus("No Endorsement applied.", "Info", false);
			}
		}catch(Throwable t){
			k.ImplicitWaitOn();
			return false;
		}finally{
			k.ImplicitWaitOn();
		}
		return true;
	}
	
	public boolean addStandardEndorsement(Map<Object, Object> map_data) {
		
		try{
			
			Map<String, List<Map<String, String>>> map_InnerPages = null;
			
			if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
				if(!common.currentRunningFlow.contains("MTA")){
					map_InnerPages = common.Renewal_Structure_of_InnerPagesMaps;
				}else{
					map_InnerPages = common.MTA_Structure_of_InnerPagesMaps;
				}
			}else if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
				if(common.currentRunningFlow.contains("NB")){
					map_InnerPages = common.NB_Structure_of_InnerPagesMaps;
				}else{
					map_InnerPages = common.Rewind_Structure_of_InnerPagesMaps;
				}
			}else if(TestBase.businessEvent.equalsIgnoreCase("Requote")){
				if(common.currentRunningFlow.contains("NB")){
					map_InnerPages = common.NB_Structure_of_InnerPagesMaps;
				}else{
					map_InnerPages = common.Requote_Structure_of_InnerPagesMaps;
				}
			}else{
				if(!common.currentRunningFlow.contains("MTA")){
					map_InnerPages = common.NB_Structure_of_InnerPagesMaps;
				}else{
					map_InnerPages = common.MTA_Structure_of_InnerPagesMaps;
				}
			}
			
			
			
			
			k.ImplicitWaitOff();
			String policyStatus = k.getText("Policy_status_header");
			int count = 0;
			int noOfProperties = 0;
			String flag = "true";
			if(common.no_of_inner_data_sets.get("StandardEndorsement")==null){
				TestUtil.reportStatus("<b> There are no endorsement details present in input file to add. </b>", "Info", false);
				noOfProperties = 0;
			}else{
				noOfProperties = common.no_of_inner_data_sets.get("StandardEndorsement");
				customAssert.assertTrue(common.funcButtonSelection("Add Standard and Variable Endorsements"));
				customAssert.assertTrue(common.funcPageNavigation("Standard and Variable Endorsements", ""), "Navigation problem to TMulti Trade Code Selection page .");
				boolean AddEndorsementTable = k.isDisplayedField("EndorsementTable");
				if(AddEndorsementTable){
					while(count < noOfProperties ){
						String key = map_InnerPages.get("StandardEndorsement").get(count).get("Automation Key");
						
							String endorsementCode = map_InnerPages.get("StandardEndorsement").get(count).get("ED_Code");
							WebElement table = driver.findElement(By.xpath("//*[@id='mainpanel']/table/tbody"));
							List<WebElement> row = table.findElements(By.tagName("tr"));
							int rowSize = row.size();
							
							for(int i=0;i<rowSize;i++){
								
								String code = table.findElement(By.xpath("tr["+(i+1)+"]/td[1]")).getText();
								if(code.equalsIgnoreCase(endorsementCode)){
									EndorsementIndividualData = new LinkedHashMap<String, String>();
									JavascriptExecutor j_exe = (JavascriptExecutor) driver;
									j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//*[contains(@value,'"+endorsementCode+"')]")));
									if(driver.findElement(By.xpath("//*[contains(@value,'"+endorsementCode+"')]")).isSelected()){
										TestUtil.reportStatus(" <b> [ "+endorsementCode+" ] </b> Standard and Variable Endorsement is already selected.", "Info", false);
									}else{
										driver.findElement(By.xpath("//*[contains(@value,'"+endorsementCode+"')]")).click();
										String section = table.findElement(By.xpath("tr["+(i+1)+"]/td[2]")).getText();
										String title = table.findElement(By.xpath("tr["+(i+1)+"]/td[3]")).getText();
										String clauseType = table.findElement(By.xpath("tr["+(i+1)+"]/td[4]")).getText();
										String type = table.findElement(By.xpath("tr["+(i+1)+"]/td[5]")).getText();
										map_InnerPages.get("StandardEndorsement").get(count).put("ED_Section", section);
										map_InnerPages.get("StandardEndorsement").get(count).put("ED_Title", title);
										map_InnerPages.get("StandardEndorsement").get(count).put("ED_CaluseType", clauseType);
										map_InnerPages.get("StandardEndorsement").get(count).put("ED_Type", type);
										EndorsementIndividualData.put("ED_"+section.replaceAll(" ", "")+"_Code", endorsementCode);
										EndorsementIndividualData.put("ED_"+section.replaceAll(" ", "")+"_Section", section);
										EndorsementIndividualData.put("ED_"+section.replaceAll(" ", "")+"_Title", title);
										EndorsementIndividualData.put("ED_"+section.replaceAll(" ", "")+"_ClauseType", clauseType);
										EndorsementIndividualData.put("ED_"+section.replaceAll(" ", "")+"_Type", type);
										EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+section.replaceAll(" ", "")+"_Code"), EndorsementIndividualData);flag = "true";
									}
									flag = "true";
									break;
								}else{
									flag = "false";
								}
							}
							if(flag.equalsIgnoreCase("false")){
								TestUtil.reportStatus(" <p style='color:blue'> <b> [ "+endorsementCode+" ] </b> Standard and Variable Endorsement is already selected. </p>", "Info", false);
							}else{
								TestUtil.reportStatus(" <b> [ "+endorsementCode+" ] </b> Standard and Variable Endorsement selected from applications successfully.", "Info", false);
							}
							//}
						//}
						count++;
					}
				}else{
					TestUtil.reportStatus(" There are no endorsement present for selection.", "Info", false);
				}
				
				k.Click("addSelectedButton");
			}
			
			

		}catch(Throwable t){
			k.ImplicitWaitOn();
			return false;
		}finally{
			k.ImplicitWaitOn();
		}
		return true;
	}
	
	
	public boolean addFreeFormatEndorsement(Map<Object, Object> map_data) {
		
		try{
			k.ImplicitWaitOff();
			String policyStatus = k.getText("Policy_status_header");
			
			int count = 0;
			int noOfProperties = 0;
			if(common.no_of_inner_data_sets.get("FreeFormatEndorsement")==null){
				noOfProperties = 0;
			}else{
				noOfProperties = common.no_of_inner_data_sets.get("FreeFormatEndorsement");
			}
			
			Map<String, List<Map<String, String>>> map_InnerPages = null;
			
			if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
				if(!common.currentRunningFlow.contains("MTA")){
					map_InnerPages = common.Renewal_Structure_of_InnerPagesMaps;
				}else{
					map_InnerPages = common.MTA_Structure_of_InnerPagesMaps;
				}
			}else if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
				if(common.currentRunningFlow.contains("NB")){
					map_InnerPages = common.NB_Structure_of_InnerPagesMaps;
				}else{
					map_InnerPages = common.Rewind_Structure_of_InnerPagesMaps;
				}
			}else if(TestBase.businessEvent.equalsIgnoreCase("Requote")){
				if(common.currentRunningFlow.contains("NB")){
					map_InnerPages = common.NB_Structure_of_InnerPagesMaps;
				}else{
					map_InnerPages = common.Requote_Structure_of_InnerPagesMaps;
				}
			}else{
				if(!common.currentRunningFlow.contains("MTA")){
					map_InnerPages = common.NB_Structure_of_InnerPagesMaps;
				}else{
					map_InnerPages = common.MTA_Structure_of_InnerPagesMaps;
				}
			}
			
			while(count < noOfProperties ){
				String key = map_InnerPages.get("FreeFormatEndorsement").get(count).get("Automation Key");
				
					customAssert.assertTrue(common.funcButtonSelection("Add Free Format"));
					EndorsementIndividualData = new LinkedHashMap<String, String>();
					String code = map_InnerPages.get("FreeFormatEndorsement").get(count).get("ED_FreeFormatCode");
					String section = map_InnerPages.get("FreeFormatEndorsement").get(count).get("ED_FreeFormatSection").trim();
					String clauseType = map_InnerPages.get("FreeFormatEndorsement").get(count).get("ED_FreeFormatCaluseType");
					String title = map_InnerPages.get("FreeFormatEndorsement").get(count).get("ED_FreeFormatTitle");
					String details = map_InnerPages.get("FreeFormatEndorsement").get(count).get("ED_FreeFormatDetails");
					
					customAssert.assertTrue(k.Input("ED_FreeFormatCode", code),"Unable to enter Code on free format Endorsement screen.");
					String sectioName = getEndorsementSectionName(section);
					customAssert.assertTrue(k.DropDownSelection("ED_FreeFormatSection", sectioName),"Unable to select free format section from dropdowm on Endorsement screen.");
					customAssert.assertTrue(k.DropDownSelection("ED_FreeFormatCaluseType", clauseType),"Unable to select free format clause type on Endorsement screen.");
					customAssert.assertTrue(k.Input("ED_FreeFormatTitle", title),"Unable to enter free format title on Endorsement screen.");
					customAssert.assertTrue(k.Input("ED_FreeFormatDetails", details),"Unable to enter free format details on Endorsement screen.");
					
					if(section.contains("Material")){
						section = "Material Damage";
					}else if(section.contains("Business")){
						section = "Business Interruption";
					}else if(section.contains("Public Liability")){
						section = "Public Liability";
					}
					
					k.clickInnerButton("Inner_page_locator", "Create");
					EndorsementIndividualData.put("ED_"+section.replaceAll(" ", "")+"_Code", code);
					EndorsementIndividualData.put("ED_"+section.replaceAll(" ", "")+"_Section", section);
					EndorsementIndividualData.put("ED_"+section.replaceAll(" ", "")+"_Title", title.toUpperCase());
					EndorsementIndividualData.put("ED_"+section.replaceAll(" ", "")+"_ClauseType", clauseType);
					EndorsementIndividualData.put("ED_"+section.replaceAll(" ", "")+"_Type", "Freeformat");
					EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+section.replaceAll(" ", "")+"_Code"), EndorsementIndividualData);
					EndorsementFreeFormatData.put(EndorsementIndividualData.get("ED_"+section.replaceAll(" ", "")+"_Code"), EndorsementIndividualData);
					TestUtil.reportStatus(" <b> [ "+code+" ] </b> free format Endorsements added successfully.", "Info", false);
					//}
				//}
				
				count++;
			}
			
		}catch(Throwable t){
			k.ImplicitWaitOn();
			return false;
		}finally{
			k.ImplicitWaitOn();
		}
		return true;
	}
	
	public String getEndorsementSectionName(String section) {
		
		Hashtable<String,String> sectionValue = new Hashtable<String,String>();
		
		switch (TestBase.product) {
		case "CCD":
			sectionValue.put("Policy", "pol");
			sectionValue.put("Material Damage", "md_ccc");
			sectionValue.put("Business Interruption", "ccc_bi");
			sectionValue.put("Money & Assault", "ccc_ma");
			sectionValue.put("Public Liability", "ccc_pl");
			sectionValue.put("Products Liability", "ccd_prd_l");
			sectionValue.put("Employers Liability", "el_ccc");
			sectionValue.put("Personal Accident Standard", "ccc_pa");
			sectionValue.put("Personal Accident Optional", "ccc_pao");
			sectionValue.put("Computer", "ccd_c");
			sectionValue.put("Deterioration of Stock", "ccd_ds");
			sectionValue.put("Goods In Transit", "ccc_gt");
			sectionValue.put("Glass", "ccd_g");
			sectionValue.put("Legal Expenses", "ccc_le");
			sectionValue.put("Terrorism", "ccc_t");
			break;
		default:
			break;
		}
		
		if(section.trim().contains("Employers")){
			return sectionValue.get("Employers Liability");
		}else{
			return sectionValue.get(section);
		}
	}
	
	@SuppressWarnings("static-access")
	public boolean requireEditEndorsement(Map<Object, Object> map_data) {
		
		try{
			k.ImplicitWaitOff();
			
			boolean endorsementTable = k.isDisplayedField("EndorsementTable");
			
			if(endorsementTable){
				WebElement endorsementTableObject = k.getObject("EndorsementTable");
				List<WebElement> rowCount = endorsementTableObject.findElements(By.tagName("tr"));
				
				for(int i=0;i<rowCount.size()-1;i++){
					String endorsementCode = endorsementTableObject.findElement(By.xpath("tbody/tr["+(i+1)+"]/td[1]")).getText();
					String actions = endorsementTableObject.findElement(By.xpath("tbody/tr["+(i+1)+"]/td[6]")).getText();
					if(actions.contains("Requires")){
						endorsementTableObject.findElement(By.xpath("tbody/tr["+(i+1)+"]/td[6]/a[2]")).click();
						k.clickInnerButton("Inner_page_locator", "Update");
						TestUtil.reportStatus("Require edit link is present for <b> [ "+endorsementCode+" ] </b> endorsement hence details updated.", "Info", false);
						endorsementTableObject = k.getObject("EndorsementTable");
						rowCount = endorsementTableObject.findElements(By.tagName("tr"));
					}
				}
			}else{
				TestUtil.reportStatus("No Endorsement applied.", "Info", false);
			}
			
		}catch(Throwable t){
			k.ImplicitWaitOn();
			return false;
		}finally{
			k.ImplicitWaitOn();
		}
		return true;
	}
	
	
	@SuppressWarnings({ "rawtypes" })
	public boolean verifyEndorsementONPremiumSummary(Map<Object, Object> map_data) {
		
		try{
			k.ImplicitWaitOff();
			String flag = "true";
			List<WebElement> listOfParagraphTags = driver.findElements(By.xpath("//*[@id='mainpanel']/p"));
			
			for(int i=0;i<listOfParagraphTags.size()-1;i++){
				String textName = listOfParagraphTags.get(i).getText();
				
				if(textName.contains("Applied Endorsements")){
					
					Iterator collectiveDataIT = EndorsementCollectiveData.entrySet().iterator();
					while(collectiveDataIT.hasNext()){
						Map.Entry collectiveDataMapValue = (Map.Entry)collectiveDataIT.next();
						String collectiveEndorsementCode = collectiveDataMapValue.getKey().toString();
						
						Iterator individualDataIT = EndorsementCollectiveData.get(collectiveEndorsementCode).entrySet().iterator();
						while(individualDataIT.hasNext()){
							Map.Entry individualDataMapValue = (Map.Entry)individualDataIT.next();
							String individualEndorsementTitle = individualDataMapValue.getValue().toString();
							if(individualEndorsementTitle.contains("Contract Lift")){
								individualEndorsementTitle = "CPA Contract Lift Cover (Lifted Goods)";
							}
							if(individualEndorsementTitle.contains("Offshore Work")){
								individualEndorsementTitle = "Offshore Work And Visits";
							}
							String mergedEndorsementText = collectiveEndorsementCode + " " + individualEndorsementTitle;
							if(textName.toLowerCase().contains(mergedEndorsementText.toLowerCase())){
								TestUtil.reportStatus("Applied Endorsement <b> [ "+collectiveEndorsementCode+" </b> with title as <b> "+individualEndorsementTitle+" ] </b> is present on premium summary page.", "Info", false);
								flag = "true";
								break;
							}else{
								flag="false";
							}
						}
						if(flag.equalsIgnoreCase("false")){
							TestUtil.reportStatus("<p style='color:red'> Endorsement <b> [ "+collectiveEndorsementCode+" ] </b> is not present on premium summary page.</p>", "Info", false);
						}
					}
					
					String endorsements = textName.replaceAll("Applied Endorsements: ", "");
					//System.out.println(endorsements);
					
					String arrEndorsement[] = endorsements.split(", ");
					for(int j=0;j<arrEndorsement.length;j++){
						//System.out.println(arrEndorsement[j]);
						
						int indexOfSpace = arrEndorsement[j].indexOf(" ");
						//System.out.println(indexOfSpace);
						String endorsementCode = arrEndorsement[j].substring(0,indexOfSpace);
						//System.out.println(endorsementCode);
						if(EndorsementCollectiveData.containsKey(endorsementCode)){
							
						}else{
							TestUtil.reportStatus("<p style='color:red'> Extra endorsement <b> [ "+endorsementCode+" ] </b> is getting displayed on Premium Summary screen which should not be present. </p>", "Info", true);
						}
						
					}
					
					//Validate extra endorsement present on Endorsement screen should not be present on Premium Screen.
					Iterator extraEndorsementDetailsIT = ExtraEndorsementList.entrySet().iterator();
					while(extraEndorsementDetailsIT.hasNext()){
						Map.Entry extraEdnorsementValue = (Map.Entry)extraEndorsementDetailsIT.next();
						String extraEDCode = extraEdnorsementValue.getValue().toString();
						
						if(textName.contains(extraEDCode)){
							TestUtil.reportStatus("<p style='color:red'> Extra endorsement <b> [ "+extraEDCode+" ] </b> is getting displayed on Premium Summary screen which should not be present. </p>", "Info", true);
						}
					}
					break;
				}

			}
		}catch(Throwable t){
			k.ImplicitWaitOn();
			return false;
		}finally{
			k.ImplicitWaitOn();
		}
		return true;
	}
	
	
	/***
	 * 
	 * 
	 * End of Endorsement Functions : 
	 * @throws Exception 
	 * 
	 * 
	 */
	
	public int func_FP_Entries_Verification_MTA(String sectionName,Map<String, List<Map<String, String>>> internal_data_map,int count){

		Map<Object,Object> map_data = common.MTA_excel_data_map;
		Map<Object,Object> NB_map_data = common.NB_excel_data_map;
		Map<Object, Object> data_map = null;
		
		final Map<String,String> locator_map = new HashMap<>();
		locator_map.put("GP","gprem");
		locator_map.put("CR","comr");
		locator_map.put("GC","comm");
		locator_map.put("NP","nprem");
		locator_map.put("GT","gipt");
		locator_map.put("NPIPT","nipt");
		
		double final_fp_NNP=0.0;
		String code=null,cover_code=null;
		
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
			break;
		case "Business Interruption":
			code = "BusinessInterruption";
			cover_code = "BusinessInterruption";
			break;
		case "Loss Of Rental Income":
			code = "LossOfRentalIncome";
			cover_code = "LossOfRentalIncome";
			break;
		case "Cyber and Data Security":
			code = "CyberandDataSecurity";
			cover_code = "CyberandDataSecurity";
			break;
		case "Money & Assault":
			code = "Money&Assault";
			cover_code = "Money&Assault";
			break;
		case "Money":
			code = "Money";
			cover_code = "Money";
			break;
		case "Employers Liability":
			code = "Employers Liability";
			cover_code = "Employers Liability";
			break;	
		case "Products Liability":
			code = "ProductsLiability";
			cover_code = "ProductsLiability";
			if(TestBase.product.contains("CTB")){
				cover_code = "Liability";
			}			
			break;
		case "Contractors All Risks":
			code = "ContractorsAllRisks";
			cover_code = "ContractorsAllRisks";						
			break;
		case "Specified All Risks":
			code = "SpecifiedAllRisks";
			cover_code = "SpecifiedAllRisks";						
			break;
		case "Computers and Electronic Risks":
			code = "ComputersandElectronicRisks";
			cover_code = "ComputersandElectronicRisks";						
			break;
		case "Public Liability":
			code = "PublicLiability";
			cover_code = "PublicLiability";
			if(TestBase.product.contains("CTB")){
				cover_code = "Liability";
			}
			break;
		case "Personal Accident":
			code = "PersonalAccidentStandard";
			cover_code = "PersonalAccidentStandard";
			break;
		case "Personal Accident Optional":
			code = "PersonalAccidentOptional";
			cover_code = "PersonalAccidentOptional";
			break;
		case "Goods In Transit":
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
			
				final_fp_NNP = Double.parseDouble(internal_data_map.get("Flat-Premiums").get(count-1).get("FP_Premium"));
			
				map_data.put(sectionName+"_FP", final_fp_NNP);
			
				double netP = final_fp_NNP;
				String netP_expected = common.roundedOff(Double.toString(netP));
				String netP_actual = Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Net Premium"));
				CommonFunction.compareValues(Double.parseDouble(netP_expected),Double.parseDouble(netP_actual),"Net Premium");
				//map_data.put("PS_"+code+"_NP",netP_expected);
				TestUtil.reportStatus("Flat Net Premium :<b>"+netP_expected+"</b> matches with <b>"+netP_actual, "Info", false);
				
				// Gross Commission Verification:
				double denominator = (1.00-(Double.parseDouble((String)map_data.get("PS_"+code+"_CR"))/100));
				double calcltdComm = (final_fp_NNP/denominator)*(Double.parseDouble((String)map_data.get("PS_"+code+"_CR"))/100);
				String grossC_expected = common.roundedOff(Double.toString(calcltdComm));
				String grossC_actual = Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Commission"));
				CommonFunction.compareValues(Double.parseDouble(grossC_expected),Double.parseDouble(grossC_actual),"Gross Commision");
//				map_data.put("PS_"+code+"_GC",grossC_expected);
				TestUtil.reportStatus("Flat Commission :<b>"+(grossC_expected)+"</b> matches with <b>"+(grossC_actual), "Info", false);
				
				//Gross Premium Verification:
				double grossP = final_fp_NNP + calcltdComm;
				String grossP_expected = common.roundedOff(Double.toString(grossP));
				String grossP_actual = Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Gross Premium"));
				CommonFunction.compareValues(Double.parseDouble(grossP_expected),Double.parseDouble(grossP_actual),"Gross Premium");
//				map_data.put("PS_"+code+"_GP",grossP_expected);
				TestUtil.reportStatus("Flat Gross Premium :<b>"+(grossP_expected)+"</b> matches with <b>"+(grossP_actual), "Info", false);
				
				String InsuranceTax =  Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Insurance Tax"));
				double IPT = (Double.parseDouble(InsuranceTax) / grossP) * 100.0;
	//			TestUtil.WriteDataToXl(TestBase.product+"_"+event, "Premium Summary",testName, "PS_"+covername+"_IPT", common_HHAZ.roundedOff(Double.toString(IPT)), map_data);
				
				double calcltdGIPT = grossP *(IPT /100);
				String grossIPT_expected = common.roundedOff(Double.toString(calcltdGIPT));
				String grossIPT_actual = Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Insurance Tax"));
				CommonFunction.compareValues(Double.parseDouble(grossIPT_expected),Double.parseDouble(grossIPT_actual),"Gross IPT");
//				map_data.put("PS_"+code+"_GT",grossIPT_expected);
				TestUtil.reportStatus("Flat Gross IPT :<b>"+(grossIPT_expected)+"</b> matches with <b>"+(grossIPT_actual), "Info", false);
				
				double premium_diff = Double.parseDouble(grossP_expected) - Double.parseDouble(grossP_actual);
				if(premium_diff<0.05 && premium_diff>-0.05){
					TestUtil.reportStatus("Flat Premium [<b> "+grossP_expected+" </b>] matches with actual Gross Premium [<b> "+grossP_actual+" </b>]as expected for "+sectionName+" in Flat Premium table .", "Pass", false);
					//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_expected,common.NB_excel_data_map),"Error while writing Total Premium for cover "+code+" to excel .");
					return 0;
				}else{
					TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Flat Gross Premium [<b> "+grossP_expected+"</b>] and Actual Premium [<b> "+grossP_actual+"</b>] for "+sectionName+" in Flat Premium table . </p>", "Fail", true);
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
						 			customAssert.assertTrue(common_HHAZ.selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
							}else{
								if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("Yes")){
									if(common.currentRunningFlow.equalsIgnoreCase("MTA"))
										common.CoversDetails_data_list.add(coverName);
									continue;
								}
								else
									customAssert.assertTrue(common_HHAZ.deSelectCovers(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
							}
						
						}else if(c_locator.equals("PEL")){
							
						}else{
							if (!driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"')]")).isSelected()){
								JavascriptExecutor j_exe = (JavascriptExecutor) driver;
								j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"')]")));
								
									if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("No"))
										continue;
									else
							 			customAssert.assertTrue(common_HHAZ.selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
														
								}else{
									if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("Yes")){
										if(common.currentRunningFlow.equalsIgnoreCase("MTA"))
											common.CoversDetails_data_list.add(coverName);
										continue;
									}
									else
										customAssert.assertTrue(common_HHAZ.deSelectCovers(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
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
								headerName = header_Name.getText();
							
								if(!headerName.contains("Com. Rate ")){
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
				
			}catch(Throwable t ){
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
					exp_value = exp_value + transaction_Premium_Values.get(section).get("Net Premium (GBP)");
				}catch(Throwable t){
					continue;
				}
			}else if(Start_Fp && !section.contains("Total")){
			//for(String _section : sectionNames){
				if(common_CCD.isFPEntries && !section.contains("Flat")){
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
				//}
			//break outer;
			}
		}
		
		String t_NetNetP_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Net Premium (GBP)"));
		CommonFunction.compareValues(exp_value,Double.parseDouble(t_NetNetP_actual)," Net Net Premium");
		trans_details_values.put("Net Premium (GBP)",exp_value);

		exp_value = 0.0;Start_Fp = false;
		for(String section : sectionNames){
			
			if(section.contains("Flat")){
				Start_Fp = true;
				continue;
			}
			
			if(!section.contains("Total") && !section.contains("Flat") && !Start_Fp){
				try{
					exp_value = exp_value + transaction_Premium_Values.get(section).get("Commission (GBP)");
				}catch(Throwable t){
					continue;
				}
			}else if(Start_Fp && !section.contains("Total")){
			//for(String _section : sectionNames){
				if(common_CCD.isFPEntries && !section.contains("Flat")){
					try{
						if(section.equalsIgnoreCase("Property Owners Liabilities"))
							section = "Liabilities - POL";
						if(section.equalsIgnoreCase("Businesss Interruption"))
							section = "Business Interruption";
						if(section.equalsIgnoreCase("Goods in Transit")){
							section = "Goods In Transit";
						}
					exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Commission");
				}catch(Throwable t){
					continue;
				}
				}
		}
		}
		String t_pc_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Commission (GBP)"));
		CommonFunction.compareValues(exp_value,Double.parseDouble(t_pc_actual),"Commission");
		trans_details_values.put("Commission (GBP)",exp_value);
		
		exp_value = 0.0;Start_Fp = false;
		for(String section : sectionNames){
	
			if(section.contains("Flat")){
				Start_Fp = true;
				continue;
			}
			
			if(!section.contains("Total") && !section.contains("Flat") && !Start_Fp){
				try{
				exp_value = exp_value + transaction_Premium_Values.get(section).get("Gross Premium (GBP)");
				}catch(Throwable t){
					continue;
				}
			}else if(Start_Fp && !section.contains("Total")){
			//for(String _section : sectionNames){
				if(common_CCD.isFPEntries && !section.contains("Flat")){
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
		String t_netP_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Gross Premium (GBP)"));
		CommonFunction.compareValues(exp_value,Double.parseDouble(t_netP_actual),"Gross Premium (GBP)");
		trans_details_values.put("Gross Premium (GBP)",exp_value);
		
		exp_value = 0.0;Start_Fp = false;
		for(String section : sectionNames){
			
			if(section.contains("Flat")){
				Start_Fp = true;
				continue;
			}
			
			if(!section.contains("Total") && !section.contains("Flat") && !Start_Fp){
				try{
				exp_value = exp_value + transaction_Premium_Values.get(section).get("Gross IPT (GBP)");
				}catch(Throwable t){
					continue;
				}
			}else if(Start_Fp && !section.contains("Total")){
			//for(String _section : sectionNames){
				if(common_CCD.isFPEntries && !section.contains("Flat")){
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
		String t_bc_actual =  Double.toString(transaction_Premium_Values.get("Totals").get("Gross IPT (GBP)"));
		CommonFunction.compareValues(exp_value,Double.parseDouble(t_bc_actual),"Gross IPT (GBP)");
		trans_details_values.put("Gross IPT (GBP)",exp_value);
		
		exp_value = 0.0;Start_Fp = false;
		for(String section : sectionNames){
			
			if(section.contains("Flat")){
				Start_Fp = true;
				continue;
			}
			
			if(!section.contains("Total") && !section.contains("Flat") && !Start_Fp){
				try{
				exp_value = exp_value + transaction_Premium_Values.get(section).get("Net IPT (GBP)");
				}catch(Throwable t){
					continue;
				}
			}else if(Start_Fp && !section.contains("Total")){
			//for(String _section : sectionNames){
				if(common_CCD.isFPEntries && !section.contains("Flat")){
					try{
						if(section.equalsIgnoreCase("Property Owners Liabilities"))
							section = "Liabilities - POL";
						if(section.equalsIgnoreCase("Businesss Interruption"))
							section = "Business Interruption";
						if(section.equalsIgnoreCase("Goods in Transit")){
							section = "Goods In Transit";
						}
					exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Net IPT");
				}catch(Throwable t){
					continue;
				}
				}
		}
		}
		String t_grossP_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Net IPT (GBP)"));
		CommonFunction.compareValues(exp_value,Double.parseDouble(t_grossP_actual),"Net IPT (GBP)");
		trans_details_values.put("Net IPT (GBP)",exp_value);
		

		double t_p_actual = transaction_Premium_Values.get("Totals").get("Gross Premium (GBP)")+transaction_Premium_Values.get("Totals").get("Gross IPT (GBP)");
		String finalPremium  = driver.findElement(By.xpath(".//*[@id='table0']/tbody/tr[4]/td[3]")).getText().replaceAll(",", "");
		trans_details_values.put("Total Premium",Double.parseDouble(finalPremium));
		
		common.transaction_Details_Premium_Values.put("Totals", trans_details_values);
		
		double premium_diff = Double.parseDouble(finalPremium) - t_p_actual;
		
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
		final Map<String,String> locator_map = new HashMap<>();
		locator_map.put("GP","gprem");
		locator_map.put("CR","comr");
		locator_map.put("GC","comm");
		locator_map.put("NP","nprem");
		locator_map.put("GT","gipt");
		locator_map.put("NPIPT","nipt");
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
		case "Money":
			code = "Money";
			cover_code = "Money";
			break;
		case "Loss Of Rental Income":
			code = "LossOfRentalIncome";
			cover_code = "LossOfRentalIncome";
			break;
		case "Cyber and Data Security":
			code = "CyberandDataSecurity";
			cover_code = "CyberandDataSecurity";
			break;
		case "Employers Liability":
			code = "EmployersLiability";
			cover_code = "Liability";
			break;
		case "Property Owners Liability":
			code = "PropertyOwnersLiability";
			cover_code = "Liability";
			break;
		case "Public Liability":
			code = "PublicLiability";
			cover_code = "PublicLiability";
			if(TestBase.product.contains("CTB")){
				cover_code = "Liability";
			}			
			break;
		case "Products Liability":
			code = "ProductsLiability";
			cover_code = "ProductsLiability";
			if(TestBase.product.contains("CTB")){
				cover_code = "Liability";
			}			
			break;
		case "Contractors All Risks":
			code = "ContractorsAllRisks";
			cover_code = "ContractorsAllRisks";						
			break;
		case "Specified All Risks":
			code = "SpecifiedAllRisks";
			cover_code = "SpecifiedAllRisks";						
			break;
		case "Computers and Electronic Risks":
			code = "ComputersandElectronicRisks";
			cover_code = "ComputersandElectronicRisks";						
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
							NB_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_"+code+"_NP"));
							MTA_NNP = 0.0;
							map_data = common.NB_excel_data_map;
								
						}else if(((String)common.NB_excel_data_map.get("CD_"+cover_code)).equals("No") && ((String)common.Rewind_excel_data_map.get("CD_"+cover_code)).equals("Yes")){
							NB_NNP = 0.0;
							MTA_NNP = Double.parseDouble((String)common.Rewind_excel_data_map.get("PS_"+code+"_NP"));
							map_data = common.Rewind_excel_data_map;
						}else{
							NB_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_"+code+"_NP"));
							MTA_NNP = Double.parseDouble((String)common.Rewind_excel_data_map.get("PS_"+code+"_NP"));
							map_data = common.NB_excel_data_map;
						}
						//Previous Premium MTA Calculation
						
						final_trans_NNP = ((MTA_NNP - NB_NNP)/p_NB_Duration)*
								((Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
							
				
				}else{
					
					//if(sectionName.contains("PI")){
						if(((String)common.NB_excel_data_map.get("CD_"+cover_code)).equals("Yes") && (((String)common.Rewind_excel_data_map.get("CD_"+cover_code)).equals("No")))
						{
							NB_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_"+code+"_NP"));
							MTA_NNP = 0.0;
							map_data = common.NB_excel_data_map;
								
						}else if(((String)common.NB_excel_data_map.get("CD_"+cover_code)).equals("No") && ((String)common.Rewind_excel_data_map.get("CD_"+cover_code)).equals("Yes")){
							NB_NNP = 0.0;
							MTA_NNP = Double.parseDouble((String)common.Rewind_excel_data_map.get("PS_"+code+"_NP"));
							map_data = common.Rewind_excel_data_map;
						}else{
							NB_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_"+code+"_NP"));
							MTA_NNP = Double.parseDouble((String)common.Rewind_excel_data_map.get("PS_"+code+"_NP"));
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
								NB_NNP = Double.parseDouble((String)data_map.get("PS_"+code+"_NP"));
								MTA_NNP = 0.0;
									
							}else if(((String)data_map.get("CD_"+cover_code)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_"+cover_code)).equals("Yes")){
								NB_NNP = 0.0;
								MTA_NNP = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NP"));
							}else{
								NB_NNP = Double.parseDouble((String)data_map.get("PS_"+code+"_NP"));
								MTA_NNP = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NP"));
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
				
				
				TestUtil.reportStatus("---------------"+cover_code+"-----------------","Info",false);
				//SPI Pen commission Calculation : 
				
				// Net Premium verification : 
				double netP = final_trans_NNP;
				String netP_expected = common.roundedOff(Double.toString(netP));
				double netP_actual = common.transaction_Details_Premium_Values.get(sectionName).get("Net Premium (GBP)");
				CommonFunction.compareValues(Double.parseDouble(netP_expected),netP_actual,"Net Premium");
				map_data.put("PS_"+code+"_NP",netP_expected);
				TestUtil.reportStatus("Net Premium :<b>"+netP_expected+"</b> matches with <b>"+netP_actual, "Info", false);
				
				// Gross Commision Verification:
				double denominator = (1.00-(common.transaction_Details_Premium_Values.get(sectionName).get("Com. Rate (%)"))/100);
				double calcltdComm = ((final_trans_NNP/denominator)*(common.transaction_Details_Premium_Values.get(sectionName).get("Com. Rate (%)"))/100);
				String grossC_expected = common.roundedOff(Double.toString(calcltdComm));
				double grossC_actual = common.transaction_Details_Premium_Values.get(sectionName).get("Commission (GBP)");
				CommonFunction.compareValues(Double.parseDouble(grossC_expected),(grossC_actual),"Gross Commision");
				map_data.put("PS_"+code+"_GC",grossC_expected);
				TestUtil.reportStatus("Commission :<b>"+(grossC_expected)+"</b> matches with <b>"+(grossC_actual), "Info", false);
				
				//Gross Premium Verification:
				double grossP = final_trans_NNP + calcltdComm;
				String grossP_expected = common.roundedOff(Double.toString(grossP));
				double grossP_actual = common.transaction_Details_Premium_Values.get(sectionName).get("Gross Premium (GBP)");
				CommonFunction.compareValues(Double.parseDouble(grossP_expected),(grossP_actual),"Gross Premium");
				map_data.put("PS_"+code+"_GP",grossP_expected);
				TestUtil.reportStatus("Gross Premium :<b>"+(grossP_expected)+"</b> matches with <b>"+(grossP_actual), "Info", false);
				
				//Gross IPT Verification:
				if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
					double InsuranceTax = common.transaction_Details_Premium_Values.get(sectionName).get("Gross IPT (GBP)");
					double IPT = ((InsuranceTax) / grossP) * 100.0;
//					TestUtil.WriteDataToXl(TestBase.product+"_"+event, "Premium Summary",testName, "PS_"+covername+"_IPT", common_HHAZ.roundedOff(Double.toString(IPT)), map_data);
				}
				double calcltdGIPT = grossP *(Double.parseDouble((String)map_data.get("PS_"+code+"_IPT"))/100);
				String grossIPT_expected = common.roundedOff(Double.toString(calcltdGIPT));
				double grossIPT_actual = common.transaction_Details_Premium_Values.get(sectionName).get("Gross IPT (GBP)");
				CommonFunction.compareValues(Double.parseDouble(grossIPT_expected),grossIPT_actual,"Gross IPT");
				map_data.put("PS_"+code+"_GT",grossIPT_expected);
				TestUtil.reportStatus("Gross IPT :<b>"+(grossIPT_expected)+"</b> matches with <b>"+(grossIPT_actual), "Info", false);
				
				//Net IPT Verification
				double calcltdNIPT = netP *(Double.parseDouble((String)map_data.get("PS_"+code+"_IPT"))/100);
				String grossNIPT_expected = common.roundedOff(Double.toString(calcltdNIPT));
				double grossNIPT_actual = common.transaction_Details_Premium_Values.get(sectionName).get("Net IPT (GBP)");
				CommonFunction.compareValues(Double.parseDouble(grossNIPT_expected),(grossNIPT_actual),"Net IPT");
				map_data.put("PS_"+code+"_NPIPT",grossNIPT_expected);
				TestUtil.reportStatus("Net IPT:<b>"+(grossNIPT_expected)+"</b> matches with <b>"+(grossNIPT_actual), "Info", false);
				
				if(common.currentRunningFlow.equals("MTA")){
					if(((String)map_data.get("PD_TaxExempt")).equalsIgnoreCase("Yes"))
						Tax_map_data.put("PS_"+cover_code+"_IPT", "0.0");
				}
			return 0;
				
			}else{
				TestUtil.reportStatus("---------------"+code+"-----------------","Info",false);
				//SPI Pen commission Calculation : 
				
				// Net Premium verification : 
				double netP = final_trans_NNP;
				String netP_expected = common.roundedOff(Double.toString(netP));
				double netP_actual = common.transaction_Details_Premium_Values.get(sectionName).get("Net Premium (GBP)");
				CommonFunction.compareValues(Double.parseDouble(netP_expected),netP_actual,"Net Premium");
				map_data.put("PS_"+code+"_NP",netP_expected);
				TestUtil.reportStatus("Net Premium :<b>"+netP_expected+"</b> matches with <b>"+netP_actual, "Info", false);
				
				// Gross Commision Verification:
				double denominator = (1.00-(Double.parseDouble((String)map_data.get("PS_"+code+"_CR"))/100));
				double calcltdComm = (final_trans_NNP/denominator)*(Double.parseDouble((String)map_data.get("PS_"+code+"_CR"))/100);
				String grossC_expected = common.roundedOff(Double.toString(calcltdComm));
				double grossC_actual = common.transaction_Details_Premium_Values.get(sectionName).get("Commission (GBP)");
				CommonFunction.compareValues(Double.parseDouble(grossC_expected),(grossC_actual),"Gross Commision");
				map_data.put("PS_"+code+"_GC",grossC_expected);
				TestUtil.reportStatus("Commission :<b>"+(grossC_expected)+"</b> matches with <b>"+(grossC_actual), "Info", false);
				
				//Gross Premium Verification:
				double grossP = final_trans_NNP + calcltdComm;
				String grossP_expected = common.roundedOff(Double.toString(grossP));
				double grossP_actual = common.transaction_Details_Premium_Values.get(sectionName).get("Gross Premium (GBP)");
				CommonFunction.compareValues(Double.parseDouble(grossP_expected),(grossP_actual),"Gross Premium");
				map_data.put("PS_"+code+"_GP",grossP_expected);
				TestUtil.reportStatus("Gross Premium :<b>"+(grossP_expected)+"</b> matches with <b>"+(grossP_actual), "Info", false);
				
				//Gross IPT Verification:
				if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
					double InsuranceTax = common.transaction_Details_Premium_Values.get(sectionName).get("Gross IPT (GBP)");
					double IPT = ((InsuranceTax) / grossP) * 100.0;
//					TestUtil.WriteDataToXl(TestBase.product+"_"+event, "Premium Summary",testName, "PS_"+covername+"_IPT", common_HHAZ.roundedOff(Double.toString(IPT)), map_data);
				}
				double calcltdGIPT = grossP *(Double.parseDouble((String)map_data.get("PS_"+code+"_IPT"))/100);
				String grossIPT_expected = common.roundedOff(Double.toString(calcltdGIPT));
				double grossIPT_actual = common.transaction_Details_Premium_Values.get(sectionName).get("Gross IPT (GBP)");
				CommonFunction.compareValues(Double.parseDouble(grossIPT_expected),grossIPT_actual,"Gross IPT");
				map_data.put("PS_"+code+"_GT",grossIPT_expected);
				TestUtil.reportStatus("Gross IPT :<b>"+(grossIPT_expected)+"</b> matches with <b>"+(grossIPT_actual), "Info", false);
				
				//Net IPT Verification
				double calcltdNIPT = netP *(Double.parseDouble((String)map_data.get("PS_"+code+"_IPT"))/100);
				String grossNIPT_expected = common.roundedOff(Double.toString(calcltdNIPT));
				double grossNIPT_actual = common.transaction_Details_Premium_Values.get(sectionName).get("Net IPT (GBP)");
				CommonFunction.compareValues(Double.parseDouble(grossNIPT_expected),(grossNIPT_actual),"Net IPT");
				map_data.put("PS_"+code+"_NPIPT",grossNIPT_expected);
				TestUtil.reportStatus("Net IPT:<b>"+(grossNIPT_expected)+"</b> matches with <b>"+(grossNIPT_actual), "Info", false);
				
				if(common.currentRunningFlow.equals("MTA")){
					if(((String)map_data.get("PD_TaxExempt")).equalsIgnoreCase("Yes"))
						Tax_map_data.put("PS_"+code+"_IPT", "0.0");
				}
			return 0;
				
			}
			
				
	}catch(Throwable t) {
	    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	    TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
	    Assert.fail("Transaction Details table verification issue.  \n", t);
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
		case "Money":
			code = "Money";
			cover_code = "Money";
			flat_section = sectionName;
			break;		
		case "Products Liability":
			code = "ProductsLiability";
			cover_code = "ProductsLiability";						
			break;
		case "Contractors All Risks":
			code = "ContractorsAllRisks";
			cover_code = "Contractors All Risks";						
			break;
		case "Specified All Risks":
			code = "SpecifiedAllRisks";
			cover_code = "Specified All Risks";						
			break;
		case "Computers and Electronic Risks":
			code = "ComputersandElectronicRisks";
			cover_code = "Computers and ElectronicRisks";						
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
		case "Cyber and Data Security":
			code = "CyberandDataSecurity";
			cover_code = "CyberandDataSecurity";
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
		case "Loss Of Rental Income":
			code = "LossOfRentalIncome";
			cover_code = "LossOfRentalIncome";
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
			
				String t_NetP_expected = common.roundedOff(Double.toString(final_fp_NNP));
				String t_NetP_actual = Double.toString(common.transaction_Details_Premium_Values.get(flat_section+"_FP").get("Net Premium"));
				CommonFunction.compareValues(Double.parseDouble(t_NetP_expected),Double.parseDouble(t_NetP_actual)," Net Premium");
				
				// Gross Commission Verification:
				double denominator = (1.00-(Double.parseDouble((String)map_data.get("PS_"+cover_code+"_CR"))/100));
				double calcltdComm = (final_fp_NNP/denominator)*(Double.parseDouble((String)map_data.get("PS_"+cover_code+"_CR"))/100);
				String grossC_expected = common.roundedOff(Double.toString(calcltdComm));
				String grossC_actual = Double.toString(common.transaction_Details_Premium_Values.get(flat_section+"_FP").get("Commission"));
				CommonFunction.compareValues(Double.parseDouble(grossC_expected),Double.parseDouble(grossC_actual),"Gross Commision");
				
				//Gross Premium Verification:
				double grossP = final_fp_NNP + calcltdComm;
				String grossP_expected = common.roundedOff(Double.toString(grossP));
				String grossP_actual =  Double.toString(common.transaction_Details_Premium_Values.get(flat_section+"_FP").get("Gross Premium"));
				CommonFunction.compareValues(Double.parseDouble(grossP_expected),Double.parseDouble(grossP_actual),"Gross Premium");
			
				String InsuranceTax = Double.toString(common.transaction_Details_Premium_Values.get(flat_section+"_FP").get("Insurance Tax"));
				double IPT = (Double.parseDouble(InsuranceTax) / grossP) * 100.0;
//				TestUtil.WriteDataToXl(TestBase.product+"_"+event, "Premium Summary",testName, "PS_"+covername+"_IPT", common_HHAZ.roundedOff(Double.toString(IPT)), map_data);
				
				double calcltdGIPT = grossP *(IPT/100);
				String grossIPT_expected = common.roundedOff(Double.toString(calcltdGIPT));
				String grossIPT_actual = Double.toString(common.transaction_Details_Premium_Values.get(flat_section+"_FP").get("Insurance Tax"));
				CommonFunction.compareValues(Double.parseDouble(grossIPT_expected),Double.parseDouble(grossIPT_actual),"Gross IPT");
								
				//Net IPT Verification
				double calcltdNIPT = final_fp_NNP *(IPT/100);
				String grossNIPT_expected = common.roundedOff(Double.toString(calcltdNIPT));
				String grossNIPT_actual = Double.toString(common.transaction_Details_Premium_Values.get(flat_section+"_FP").get("Net IPT"));
				CommonFunction.compareValues(Double.parseDouble(grossNIPT_expected),Double.parseDouble(grossNIPT_actual),"Net IPT");	

			return 0;
				
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
	
	@SuppressWarnings({ "rawtypes", "static-access" })
	public boolean verifyAdjustedTaxOnBusinessEvent(Map<Object, Object> map_data) throws Exception {
		
		taxTable_tBody = k.getObject("inssuranceTaxMainTableBODY"); 
		List<WebElement> list2 = taxTable_tBody.findElements(By.tagName("tr"));
		countOfCovers = list2.size();
		String sectionName;
		k.waitTwoSeconds();
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
				
				if(((String)map_data.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
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
				String actualTotalGT = "";
				try{
					k.waitTwoSeconds();
					actualTotalGT = taxTable_tBody.findElement(By.xpath("tr["+(j+1)+"]/td[5]")).getText();
				}catch(Throwable t){
					TestUtil.reportStatus("<b> Object issue exists on Insurance tax screen hence re calling function to verify present taxes. </b>", "Info", false);
					verifyAdjustedTaxOnBusinessEvent(map_data);
				}
				
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
	
	//Rounded off function.
    public String roundedOff(String number) {
    	
    DecimalFormat formatter = new DecimalFormat("00.000000");
 	   
    if(number.contains(".")){
	   String replacedString = number.replace(".", ",");
        String[] stringArray = replacedString.split(",");
        if(stringArray[1].length()>6){
            String roundedNumber = formatter.format(Double.parseDouble(number));
            return roundedNumber;
            
        }else{
            String formatedNumber = f.format(Double.parseDouble(number));
            return formatedNumber;
        }
 	 }
 	 else{
 	   String formatedNumber = f.format(Double.parseDouble(number));
       return formatedNumber;
 	}
  }
	
public boolean decideRewindMethod(){
	 
		
		try {
			
			
			switch (CommonFunction_VELA.product) {
			case "CCD":
				if(common.currentRunningFlow.equals("NB")){
					common_POF.isNBRewindStarted=true;
					customAssert.assertTrue(common_POF.funcRewindOperation(common.NB_excel_data_map),"Error in function Rewind Operation .");				
				}
				else if(common.currentRunningFlow.equals("Renewal")){
					customAssert.assertTrue(common_POF.funcRewindOperation(common.Renewal_excel_data_map),"Error in function Rewind Operation .");
				}else if(common.currentRunningFlow.equals("MTA")){
					common_POF.isMTARewindFlow = true;
					common_POF.isFPEntries=false;
					common_POF.isMTARewindStarted=true;
					customAssert.assertTrue(common_POF.funcRewindOperation(common.MTA_excel_data_map),"Error in function Rewind Operation for MTA flow .");
				}
				
				break;
			}
			
			
			
		} catch (Exception e) {
			return false;
		}
		
		return true;
		
	}


public boolean funcQuoteCheck(Map<Object, Object> map_data){
	
	 boolean retvalue = true;
	 int counter = 0;
		try {     
			
			customAssert.assertTrue(k.Click("Quote_btn"),"Unable to click on Quote button.");
			return retvalue;
		} catch(Throwable t) {
			return false;
		}
	}

/**
 * 
 * This method verifies Referral Rules on Quote Check screen."
 * 
 *
 */

public boolean func_Verify_Referral_Rules(){

boolean r_value=false;

try{
	
	Map<Object,Object> data_map = null;
	switch(common.currentRunningFlow){
		case "NB":
			data_map = common.NB_excel_data_map;
			break;
		case "Rewind":
			data_map = common.Rewind_excel_data_map;
			break;
		case "Requote":
			data_map = common.Requote_excel_data_map;
			break;
		case "MTA":
			data_map = common.MTA_excel_data_map;
			break;
		case "Renewal":
			data_map = common.Renewal_excel_data_map;
			break;
		}
		try{
			k.ImplicitWaitOff();
			if(driver.findElement(By.xpath("//*[text()='This quote has been checked and no issues were found.']")).isDisplayed()){
				return true;
			}
		}catch(Throwable t){
			//return true;
		}
	
		String referralRulesTbl_xpath = "//*[text()='Referral Rules']//following::table[1]";
		WebElement referralRules_Table = driver.findElement(By.xpath(referralRulesTbl_xpath));
		k.ScrollInVewWebElement(referralRules_Table);
		
		int referrel_tble_Rows = referralRules_Table.findElements(By.tagName("tr")).size();
		int Description_col_index = 2,Section_col_index=1;
		
		if(referralRules_Table.isDisplayed()){
			
			TestUtil.reportStatus("Referral verification started on quote check screen . ","Info",false);
			
			//int total_referral_messages = common_VELA.referrals_list.size();
			String referral_msg_section=null,referral_Txt=null;
			WebElement w_referral_Row = null,w_referralSection_Row=null;
			boolean isReferralFound=false;
			
			if((referrel_tble_Rows-1) <= common_HHAZ.referrals_list.size()){
			//For each referral message
			for(String referral_msg : common_HHAZ.referrals_list){
				isReferralFound=false;
				referral_msg_section = referral_msg.split("_")[0];
				referral_Txt = referral_msg.split("_")[1];
				
				for(int row = 1; row < referrel_tble_Rows ;row ++){
					
					
					//Referral Text
					w_referral_Row = driver.findElement(By.xpath(referralRulesTbl_xpath+"//tbody//tr["+row+"]//td["+Description_col_index+"]"));
					if(w_referral_Row.getText().equals(referral_Txt)){
						isReferralFound = true;
						TestUtil.reportStatus("Referral  >><b>"+referral_Txt+"</b><< verified sucessfully . ","Pass",false);
						
						//Referral Section
						w_referralSection_Row = driver.findElement(By.xpath(referralRulesTbl_xpath+"//tbody//tr["+row+"]//td["+Section_col_index+"]"));
						if(!w_referralSection_Row.getText().equals(referral_msg_section)){
							TestUtil.reportStatus("<p style='color:red'> Referral Section is incorrect for referral - >><b>"+referral_Txt+"</b><<  Expexted: <b>"+referral_msg_section+"</b> and Actual: </b>"+w_referralSection_Row.getText()+"</b> . </p>", "Fail", true);
							ErrorUtil.addVerificationFailure(new Throwable("Referral Section is incorrect for referral - >><b>"+referral_Txt+"</b><<  Expexted: <b>"+referral_msg_section+"</b> and Actual: "));
							
						}
						break;
					}else
						continue;
					
				}
				
				if(!isReferralFound){
					TestUtil.reportStatus("<p style='color:red'> Referral  >><b>"+referral_Txt+"</b><< not generated on quote check screen . </p>", "Fail", true);
					ErrorUtil.addVerificationFailure(new Throwable("Referral  >><b>"+referral_Txt+"</b><< not generated on quote check screen . "));
					
				}
			}// referral message for loop end
			
			}else if((referrel_tble_Rows-1) > common_HHAZ.referrals_list.size()){
				for(int r_row = 1; r_row < referrel_tble_Rows ;r_row ++){
					isReferralFound=false;
					w_referral_Row = driver.findElement(By.xpath(referralRulesTbl_xpath+"//tbody//tr["+r_row+"]//td["+Description_col_index+"]"));
					w_referralSection_Row = driver.findElement(By.xpath(referralRulesTbl_xpath+"//tbody//tr["+r_row+"]//td["+Section_col_index+"]"));
					
				
					for(String referral_msg : common_HHAZ.referrals_list){
						
						referral_msg_section = referral_msg.split("_")[0];
						referral_Txt = referral_msg.split("_")[1];
						
						//Referral Text
						if(w_referral_Row.getText().equals(referral_Txt)){
							isReferralFound = true;
							TestUtil.reportStatus("Referral  >><b>"+referral_Txt+"</b><< verified sucessfully . ","Pass",false);
							
							//Referral Section
							if(!w_referralSection_Row.getText().equals(referral_msg_section)){
								TestUtil.reportStatus("<p style='color:red'> Referral Section is incorrect for referral - >><b>"+referral_Txt+"</b><<  Expexted: <b>"+referral_msg_section+"</b> and Actual: </b>"+w_referralSection_Row.getText()+"</b> . </p>", "Fail", true);
								ErrorUtil.addVerificationFailure(new Throwable(" Referral Section is incorrect for referral - >><b>"+referral_Txt+"</b><<  Expexted: <b>"+referral_msg_section+"</b> and Actual: "));
								
							}
							break;
						}else
							continue;
						
					}
					
					if(!isReferralFound){
						TestUtil.reportStatus("<p style='color:red'> Referral  >><b>"+w_referral_Row.getText()+"</b><< should not generate on quote check screen . </p>", "Fail", true);
						ErrorUtil.addVerificationFailure(new Throwable("Referral  >><b>"+w_referral_Row.getText()+"</b><< should not generate on quote check screen . "));
						
					}
			}
		} //else if
				
		}else{
			TestUtil.reportStatus("Referral Rules table not exists on quote check screen . ", "Fail", true);
			
		}
		TestUtil.reportStatus("Referral verification successfully completed on quote check screen . ","Info",false);
		r_value=true;
}catch(Throwable t){
	System.out.println("Error while verifying referral rules "+t.getMessage());
	k.ImplicitWaitOn();
	return false;
}
finally {
	k.ImplicitWaitOn();
}
return r_value;
}
	
}
