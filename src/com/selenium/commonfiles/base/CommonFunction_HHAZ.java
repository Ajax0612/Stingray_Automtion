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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

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

public class CommonFunction_HHAZ extends TestBase {
	
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
	public boolean isInsuranceTaxDone = false,isAssignedToUW=false, transTbleValue=false;
	SimpleDateFormat df = new SimpleDateFormat();
	Date currentDate = new Date();
	public Map<Object, Object> NB_excel_data_map = null;
	public Map<Object, Object> MTA_excel_data_map = null;
	public Map<Object, Object> Rewind_excel_data_map = null;
	public Map<Object, Object> Renewal_excel_data_map = null;
	public Map<Object, Object> CAN_excel_data_map = null;
	public List<String> CoversDetails_data_list = null;
	public static Map<String, Double> Adjusted_Premium_map = null;
	public boolean TransactionTable = false;
	
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
	public double InsTaxTerrDoc = 0.00, tpTotal = 0.00, AddTaxTerrDoc = 0.00,MFC_total_premium=0.0,PAO_total_premium=0.0;
	public double TotalPremiumWithAdminDocActOED = 0.00, TotalPremiumWithAdminDocExpOED = 0.00, PremiumExcTerrDocActOED = 0.00,  PremiumExcTerrDocExpOED = 0.00, TerPremDocActOED = 0.00, TerPremDocExpOED = 0.00, InsTaxDocActOED = 0.00, InsTaxDocExpOED = 0.00;
	public boolean CARS_FP= false , CV_FP = false, AV_FP= false , ST_FP = false, OT_FP = false, TP_FP= false , TR_FP = false, PAO_FP = false;
	public boolean PL_FP= false , EL_FP = false, PA_FP= false , LE_FP = false;
	
	
	public String MD_Premium , BI_Premium , MA_Premium , EL_Premium , PL_Premium , PAS_Premium , PAO_Premium , GIT_Premium , TER_Premium , LE_Premium= null;
	
	
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
		 			if(TestBase.product.equalsIgnoreCase("OED")){
		 				if((coverWithoutLocator.equalsIgnoreCase("PublicLiability"))){
			 				
			 				CoversDetails_data_list.add(coverWithoutLocator);
			 				continue;
			 			}
			 			
			 			customAssert.assertTrue(selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
			 			CoversDetails_data_list.add(coverWithoutLocator);
		 			}else{
		 			if(!(coverWithoutLocator.equalsIgnoreCase("ProductsLiability") || 
		 					coverWithoutLocator.equalsIgnoreCase("PollutionLiability(suddenandunforeseen)") || 
		 					coverWithoutLocator.equalsIgnoreCase("Computer") || 
		 					coverWithoutLocator.equalsIgnoreCase("DeteriorationofStock"))){
		 				
		 				
		 				CoversDetails_data_list.add(coverWithoutLocator);
		 				
		 			}
		 			
		 			customAssert.assertTrue(selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
		 		}
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
			
			/*if(!TestBase.product.equals("MFC")&&!TestBase.product.equals("MFB") &&!TestBase.product.equals("OED"))
				customAssert.assertTrue(common_HHAZ.verifyEndorsementONPremiumSummary(map_data),"Endorsement on Premium is having issue(S).");
			*/
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
			//driver.findElement(By.xpath("//*[contains(@name,'_admin_fee')]")).sendKeys(Keys.chord(Keys.CONTROL, "a"),(String)map_data.get("PS_AdminFee"));
			
			//Tax exempt is disabled by default for GTB - Bug is raised
			if(!TestBase.product.contains("GTB")){
				k.SelectRadioBtn("insuranceTaxExemptionRadioButton", (String)map_data.get("PS_TaxExempt"));
			}
			
			if(((String)map_data.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
				customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_InsuranceTaxButton", "Yes",map_data), "Error while writing Tax exemption value to excel .");
			}
			
			if(((String)map_data.get("PS_IsPolicyFinanced")).equals("Yes")/* && k.getText("PS_Broker_Name").contains("Arthur J.")*/){
				k.SelectRadioBtn("PS_IsPolicyFinanced","Yes");
				k.Input("PS_Finance_RefNumber", (String)map_data.get("PS_FinanceReferenceNumber"));
				k.DropDownSelection("PS_CreditProvider", (String)map_data.get("PS_CreditProvider"));
			}
			customAssert.assertTrue(k.SelectRadioBtn("Payment_Warranty_rules", (String)map_data.get("PS_PaymentWarrantyRules")),"Unable to Select Payment_Warranty_rules radio button . ");
			
			if(!TestBase.product.equals("MFC") && !TestBase.product.equals("MFB")&&!TestBase.product.equals("GTC") && !TestBase.product.equals("CMA"))
				if(((String)map_data.get("PS_PaymentWarrantyRules")).equals("Yes")){
					
					customAssert.assertTrue(k.Click("Payment_Warranty_Due_Date"), "Unable to Click Payment_Warranty_Due_Date date picker .");
					customAssert.assertTrue(k.Input("Payment_Warranty_Due_Date", (String)map_data.get("PS_PaymentWarrantyDueDate")),"Unable to Enter Payment_Warranty_Due_Date .");
					customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
				}
			
			k.waitTwoSeconds();
		break;	
		case "Rewind":
			
			policy_Duration = Integer.parseInt((String)map_data.get("PS_Duration"));
			policy_Duration--;
			String startDatePath = "//td[text()='Policy Start Date (dd/mm/yyyy) ']//following-sibling::td//input";
			String startDate = driver.findElement(By.xpath(startDatePath)).getAttribute("value");
			//policy_Start_date = common_VELA.get_PolicyStartDate((String)map_data.get("PS_PolicyStartDate"));
			map_data.put("PS_PolicyStartDate", startDate);
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
			//driver.findElement(By.xpath("//*[contains(@name,'_admin_fee')]")).sendKeys(Keys.chord(Keys.CONTROL, "a"),(String)map_data.get("PS_AdminFee"));
			
			//Tax exempt is disabled by default for GTB - Bug is raised
			if(!TestBase.product.contains("GTB")){
				k.SelectRadioBtn("insuranceTaxExemptionRadioButton", (String)map_data.get("PS_TaxExempt"));
			}
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
				k.waitFiveSeconds();
				customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
				customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
				customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
				
			}	
			k.waitTwoSeconds();
			//driver.findElement(By.xpath("//*[contains(@name,'_admin_fee')]")).sendKeys(Keys.chord(Keys.CONTROL, "a"),(String)map_data.get("PS_AdminFee"));
			
			//Tax exempt is disabled by default for GTB - Bug is raised
			if(!TestBase.product.contains("GTB")){
				k.SelectRadioBtn("insuranceTaxExemptionRadioButton", (String)map_data.get("PS_TaxExempt"));
			}
			
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
				k.waitFiveSeconds();
				customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
				customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
				customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
				
			}	
			k.waitTwoSeconds();
			//driver.findElement(By.xpath("//*[contains(@name,'_admin_fee')]")).sendKeys(Keys.chord(Keys.CONTROL, "a"),(String)map_data.get("PS_AdminFee"));
			
			//Tax exempt is disabled by default for GTB - Bug is raised
			if(!TestBase.product.contains("GTB")){
				k.SelectRadioBtn("insuranceTaxExemptionRadioButton", (String)map_data.get("PS_TaxExempt"));
			}
			
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
		
		if(common.currentRunningFlow.equalsIgnoreCase("Renewal")){
			if(!TestBase.product.equals("MFC") && !TestBase.product.equals("MFB")&&!TestBase.product.equals("GTC")&&!TestBase.product.equals("GTA")&&!TestBase.product.equals("GTB") && !TestBase.product.equals("CMA")
					&& !TestBase.product.equals("PAA")&& !TestBase.product.equals("PAB") && !TestBase.product.equals("GTD") && !TestBase.product.equals("DOB")){
				common.funcButtonSelection("Insurance Tax");
				deleteItems();
				k.Click("Tax_adj_BackBtn");
			}else
				PremiumFlag=false;
				
		}
		if(TestBase.product.equalsIgnoreCase("CCJ"))
			customAssert.assertTrue(Verify_premiumSummaryTable_CCJ(), "Error while verifying Premium Summary table .");
		else
			customAssert.assertTrue(Verify_premiumSummaryTable(), "Error while verifying Premium Summary table .");
		
		common_CCD.isGrossPremiumReferralCheckDone=true;
		if(!TestBase.product.equals("MFC") && !TestBase.product.equals("MFB")&&!TestBase.product.equals("GTC") && !TestBase.product.equals("GTA")&&!TestBase.product.equals("DOB")&& !TestBase.product.equals("GTB") &&!TestBase.product.equals("CMA")&& !TestBase.product.equals("GTD")
				&& !TestBase.product.equals("PAA")&& !TestBase.product.equals("PAB")){
			customAssert.assertTrue(common_HHAZ.insuranceTaxAdjustmentHandling(code,event), "Premium Summary function is having issue(S) . ");
			
			if(TestBase.product.equalsIgnoreCase("CCJ"))
				customAssert.assertTrue(Verify_premiumSummaryTable_CCJ(), "Error while verifying Premium Summary table .");
			else
				customAssert.assertTrue(Verify_premiumSummaryTable(), "Error while verifying Premium Summary table .");
		}
		if(Integer.parseInt(customPolicyDuration)!=365){
			customAssert.assertTrue(funcTransactionPremiumTable(code, event), "Error while verifying Transaction Premium table on premium Summary page .");
			transTbleValue=true;
		}else{	transTbleValue=false;}
		
		TestUtil.reportStatus("Premium Summary details are filled and Verified sucessfully . ", "Info", true);
		return r_value;
	}catch(Throwable t){
			
			return false;
		}
	}
	
public boolean funcPremiumSummary_MTA(Map<Object, Object> map_data,String code,String event) {
		
		boolean r_value= true;
		String testName = (String)map_data.get("Automation Key");
		String customPolicyDuration=null;
		SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
		
		try{
			customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
			/*if(!TestBase.businessEvent.equalsIgnoreCase("Renewal")){
				if(!TestBase.product.equals("MFB") && !TestBase.product.equals("MFC"))
				customAssert.assertTrue(common_HHAZ.verifyEndorsementONPremiumSummary(map_data),"Endorsement on Premium is having issue(S).");
			}*/
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
				k.waitFiveSeconds();
				customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
				customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
				customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
				
			}	
			k.waitTwoSeconds();
			
			// TaxExpemt is disabled for GTB	
			if(!TestBase.product.contains("GTB")){
				k.SelectRadioBtn("insuranceTaxExemptionRadioButton", (String)map_data.get("PS_TaxExempt"));
			}
			
			if(((String)map_data.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
				customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_InsuranceTaxButton", "Yes",map_data), "Error while writing Tax exemption value to excel .");
			}
			
			if(((String)map_data.get("PS_IsPolicyFinanced")).equals("Yes")/* && k.getText("PS_Broker_Name").contains("Arthur J.")*/){
				k.SelectRadioBtn("PS_IsPolicyFinanced","Yes");
				k.Input("PS_Finance_RefNumber", (String)map_data.get("PS_FinanceReferenceNumber"));
				k.DropDownSelection("PS_CreditProvider", (String)map_data.get("PS_CreditProvider"));
			}
			customAssert.assertTrue(k.SelectRadioBtn("Payment_Warranty_rules", (String)map_data.get("PS_PaymentWarrantyRules")),"Unable to Select Payment_Warranty_rules radio button . ");
			
			if(((String)map_data.get("PS_PaymentWarrantyRules")).equals("Yes")){
				
				customAssert.assertTrue(k.Click("Payment_Warranty_Due_Date"), "Unable to Click Payment_Warranty_Due_Date date picker .");
				customAssert.assertTrue(k.Input("Payment_Warranty_Due_Date", (String)map_data.get("PS_PaymentWarrantyDueDate")),"Unable to Enter Payment_Warranty_Due_Date .");
				customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
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
				//customAssert.assertTrue(k.Click("Policy_Start_Date"), "Unable to Click Policy_Start_Date date picker .");
				//customAssert.assertTrue(k.Input("Policy_Start_Date", (String)map_data.get("PS_PolicyStartDate")),"Unable to Enter Policy_Start_Date .");
				//customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
				k.waitFiveSeconds();
				customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
				customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
				customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
				
			}	
			k.waitTwoSeconds();
			
			// TaxExpemt is disabled for GTB
			if(!TestBase.product.contains("GTB")){
				k.SelectRadioBtn("insuranceTaxExemptionRadioButton", (String)map_data.get("PS_TaxExempt"));
			}
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
		if(TestBase.product.equalsIgnoreCase("CCJ"))
			customAssert.assertTrue(Verify_premiumSummaryTable_CCJ(), "Error while verifying Premium Summary table .");
		else
			customAssert.assertTrue(Verify_premiumSummaryTable(), "Error while verifying Premium Summary table in MTA  .");
		if(!TestBase.product.equals("MFB") && !TestBase.product.equals("MFC")&& !TestBase.product.equals("CMA") && !TestBase.product.equals("GTA") && !TestBase.product.equals("GTB")
				&& !TestBase.product.equals("PAA")&& !TestBase.product.equals("PAB") && !TestBase.product.equals("DOB")){
			
			if(!(TestBase.product.equals("GTC")|| TestBase.product.equals("GTD"))  ){
				customAssert.assertTrue(common_HHAZ.insuranceTaxAdjustmentHandling(code,event), "Premium Summary function is having issue(S) . ");
				
			if(TestBase.product.equalsIgnoreCase("CCJ"))
				customAssert.assertTrue(Verify_premiumSummaryTable_CCJ(), "Error while verifying Premium Summary table .");
			else
				customAssert.assertTrue(Verify_premiumSummaryTable(), "Error while verifying Premium Summary table in MTA  .");
			}
			
		}
			if(TestBase.businessEvent.equalsIgnoreCase("Rewind")||(TestBase.businessEvent.equalsIgnoreCase("MTA")&& common.currentRunningFlow.equals("Rewind"))){
				customAssert.assertTrue(common_CCD.func_Flat_Premiums_(common.Rewind_excel_data_map,common.Rewind_Structure_of_InnerPagesMaps), "Error while verifying Flat Premium table in MTA  .");
			}else if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
				if(!common_CCD.isMTARewindStarted){
					customAssert.assertTrue(common_CCD.func_Flat_Premiums_(common.MTA_excel_data_map,common.MTA_Structure_of_InnerPagesMaps), "Error while verifying Flat Premium table in MTA  .");
				}
			}
				
			
		
		customAssert.assertTrue(func_MTATransactionDetailsPremiumTable(code, event), "Error while verifying Transaction Details Premium table on premium Summary page .");
		funcTransactionDetailsMessage_MTA();

		TestUtil.reportStatus("Premium Summary details are filled and Verified sucessfully . ", "Info", true);
		return r_value;
	}catch(Throwable t){
			 
			return false;
		}
	}
	

public boolean Verify_premiumSummaryTable(){
	err_count = 0;
	PremiumExcTerrDocAct = 0;
	PremiumExcTerrDocActOED = 0;
	InsTaxDocActOED=0;
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
	locator_map.put("PenComm", "pencom");
	locator_map.put("NetPremium", "nprem");
	locator_map.put("BrokerComm", "comm");
	locator_map.put("GrossPremium", "gprem");
	locator_map.put("InsuranceTax", "gipt");
	
	final Map<String,String> section_map = new HashMap<>();
	
	
//	section_map.put("EmployersLiability", "el");
	
	section_map.put("Cargo", "qgt_car");
	if(TestBase.product.contains("GTD")){
		section_map.put("GoodsinTransit", "gtd_git");
	}else{
		section_map.put("GoodsinTransit","gt");
	}
	
	section_map.put("MaterialDamage", "md");
	section_map.put("BusinessInterruption", "bi");
	section_map.put("Money&Assault","ma");
	section_map.put("EmployersLiability","el");
	section_map.put("PublicLiability","pl");
	section_map.put("PersonalAccident","pa");
	if(TestBase.product.equals("PAA")){
		section_map.put("PersonalAccident","paa_pa");
	}else if(TestBase.product.equals("PAB")){
		section_map.put("PersonalAccident","pab_pa");
	}
	
	section_map.put("PersonalAccidentOptional","pao");
	section_map.put("LegalExpenses","le");
	section_map.put("Terrorism", "t");
	section_map.put("Cars","cars_sec");
	section_map.put("CommercialVehicles","cv");
	section_map.put("AgriculturalVehicles","av");
	section_map.put("SpecialTypes","st");
	section_map.put("OtherTypes","ov");
	section_map.put("TradePlate","tp");
	section_map.put("Trailers","tr");
	section_map.put("Computers","cma");
	section_map.put("Total", "tot");
	section_map.put("D&O", "do");
	section_map.put("GoodsinTransitRSAUK", "gta");
	section_map.put("GoodsinTransitRSAROI", "gtb");
	
	double exp_Premium = 0.0;
	
	try{
	
		String annualTble_xpath = "html/body/div[3]/form/div/table[2]";
		String policy_status_actual = k.getText("Policy_status_header");
		int trans_tble_Rows = driver.findElements(By.xpath(annualTble_xpath+"/tbody/tr")).size();
		int trans_tble_Cols = driver.findElements(By.xpath(annualTble_xpath+"/thead/tr/th")).size();
		String sectionName = null;
		PremiumExcTerrDocExp = 0;
		PremiumExcTerrDocExp = 0;
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
				
				if(((String)data_map.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
					data_map.put("PS_"+sectionName+"_IPT", "0.0");
				}else{
					if(!policy_status_actual.contains("Rewind")){
						data_map.put("PS_"+sectionName+"_IPT", data_map.get("PS_IPTRate"));
					}else{
						if(sectionName.contains("PersonalAccident") && !(TestBase.product.equals("PAA")) && !(TestBase.product.equals("PAB"))){
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
					}else if(cover_name.contains("el") && !TestBase.product.equals("OED")){
						PencCommXpath = "//*[@name='el_ccc"+"_penr']";
						BrokerCommXpath ="//*[@name='el_ccc"+"_comr']" ;
					}else if(cover_name.contains("cma")){
						 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
						 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
					}else if(cover_name.contains("qgt")){
						 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
						 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
					}else if(cover_name.contains("gtd") || cover_name.contains("gta") || cover_name.contains("gtb")){
						 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
						 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
					}else if(cover_name.contains("paa_pa")||cover_name.contains("pab_pa")){
						 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
						 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
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
		if(common.currentRunningFlow.equalsIgnoreCase("Rewind") && TestBase.businessEvent.equalsIgnoreCase("Rewind")){
			
			if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
				if(!PremiumFlag)
					for(int i =1;i<=trans_tble_Rows-1;i++){
						String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
						sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
						if(sectionName.contains("Totals"))
							sectionName = "Total";
						if(sectionName.contains("BusinesssInterruption"))
							sectionName = "BusinessInterruption";
						if(sectionName.equalsIgnoreCase("GoodsinTransit")){
							sectionName = "GoodsInTransit";
						}
						if(sectionName.equalsIgnoreCase("PersonalAccident")){
							sectionName="PersonalAccidentStandard";
						}
					
						if(((String)common.NB_excel_data_map.get("CD_"+sectionName)).equals("No")/* && ((String)common.MTA_excel_data_map.get("CD_"+sectionName)).equals("No") */&& ((String)common.Rewind_excel_data_map.get("CD_"+sectionName)).equals("Yes")){
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
							if(sectionName.contains("PersonalAccident")){
								sectionName="PersonalAccident";
							}
							String cover_name = section_map.get(sectionName);
							String PencCommXpath , BrokerCommXpath;
							if(cover_name.contains("md")){
								PencCommXpath = "//*[@name='md_ccc"+"_penr']";
								BrokerCommXpath ="//*[@name='md_ccc"+"_comr']" ;
							}else if(cover_name.contains("el") && !TestBase.product.equals("OED")){
								PencCommXpath = "//*[@name='el_ccc"+"_penr']";
								BrokerCommXpath ="//*[@name='el_ccc"+"_comr']" ;
							}else if(cover_name.contains("cma")){
								 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
								 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
							}else if(cover_name.contains("paa_pa")||cover_name.contains("pab_pa")){
								 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
								 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
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
			}else{
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
					
					switch(sectionName){
					
					
					case "BusinesssInterruption":
						//code = "BusinessInterruption";
						sectionName = "BusinessInterruption";
						break;
					
					
					case "PersonalAccident":
						//code = "PersonalAccidentStandard";
						if(!TestBase.product.equals("OED") && !TestBase.product.equals("PAA")&& !TestBase.product.equals("PAB")){
						sectionName = "PersonalAccidentStandard";}
						break;
					
					case "GoodsinTransit":
						//code = "GoodsInTransit";
						sectionName = "GoodsInTransit";
						break;
					case "TradePlate":
						sectionName="TradePlates";
						break;
					
					}
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
							
							if(TestBase.product.equals("GTD")){
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
							}else if(cover_name.contains("cma")){
								PencCommXpath = "//*[@name='cma"+"_penr']";
								BrokerCommXpath ="//*[@name='cma"+"_comr']" ;
							}else if(cover_name.contains("cma")){
								 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
								 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
							}else if(cover_name.contains("paa_pa") ||cover_name.contains("pab_pa")){
								 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
								 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
							}else if(cover_name.contains("qgt") || cover_name.contains("gta") || cover_name.contains("gtb")|| cover_name.contains("gtd_git")){
								 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
								 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
							}
							else{
								 PencCommXpath = "//*[contains(@id,'_"+cover_name+"_penr')]";
								 BrokerCommXpath ="//*[contains(@id,'_"+cover_name+"_comr')]";
							}
							
							String penComm = driver.findElement(By.xpath(PencCommXpath)).getAttribute("value");
							String BrokComm = driver.findElement(By.xpath(BrokerCommXpath)).getAttribute("value");
							common.MTA_excel_data_map.put("PS_"+sectionName+"_PenComm_rate", penComm);
							common.MTA_excel_data_map.put("PS_"+sectionName+"_BrokerComm_rate", BrokComm);
									
							
						}
					}else{
						
						if(((String)common.NB_excel_data_map.get("CD_"+sectionName)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_"+sectionName)).equals("Yes")){
							if(!TestBase.product.equals("MFB") && !TestBase.product.equals("MFC")){
								if(sectionName.contains("PersonalAccident")){
									sectionName="PersonalAccident";
								}
								if(sectionName.contains("GoodsInTransit")){
									sectionName="GoodsinTransit";
								}
							}else{
								if(sectionName.contains("TradePlate")){
									sectionName="TradePlate";
								}
							}
							customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
							if(((String)data_map.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
								data_map.put("PS_"+sectionName+"_IPT", "0.0");
							}
						}else{
							
							if(!TestBase.product.equals("MFB") && !TestBase.product.equals("MFC")){
								if(sectionName.contains("PersonalAccident")){
									sectionName="PersonalAccident";
								}
								if(sectionName.contains("GoodsInTransit")){
									sectionName="GoodsinTransit";
								}
							}
							
							if(sectionName.contains("TradePlate")){
								sectionName="TradePlate";
							}
							
							String cover_name = section_map.get(sectionName);
							String PencCommXpath , BrokerCommXpath;
							if(cover_name.contains("md")){
								PencCommXpath = "//*[@name='md_ccc"+"_penr']";
								BrokerCommXpath ="//*[@name='md_ccc"+"_comr']" ;
							}else if(cover_name.contains("el") && !TestBase.product.equals("OED")){
								PencCommXpath = "//*[@name='el_ccc"+"_penr']";
								BrokerCommXpath ="//*[@name='el_ccc"+"_comr']" ;
							}else if(cover_name.contains("cma")){
								 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
								 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
							}else if(cover_name.contains("qgt") || cover_name.contains("gta") || cover_name.contains("gtb") || cover_name.contains("gtd") ){
								 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
								 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
							}else if(cover_name.contains("paa_pa") || cover_name.contains("pab_pa")){
								 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
								 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
							}else{
								 PencCommXpath = "//*[contains(@id,'_"+cover_name+"_penr')]";
								 BrokerCommXpath ="//*[contains(@id,'_"+cover_name+"_comr')]";
							}
							k.ImplicitWaitOff();
							String penComm = driver.findElement(By.xpath(PencCommXpath)).getAttribute("value");
							String BrokComm = driver.findElement(By.xpath(BrokerCommXpath)).getAttribute("value");
							k.ImplicitWaitOn();
							common.MTA_excel_data_map.put("PS_"+sectionName+"_PenComm_rate", penComm);
							common.MTA_excel_data_map.put("PS_"+sectionName+"_BrokerComm_rate", BrokComm);
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
				
					err_count = err_count + func_PremiumSummaryCalculation(section_map.get(sectionName),sectionName,locator_map);
				exp_Premium = exp_Premium + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_TotalPremium"));
			}
			
			double Total_GP = 00.00;
			double Total_GT = 00.00;
			double Total_NetNetPemium = 00.00,Total_PenComm = 00.00,Total_NetPremium=00.00 ,Total_BrokComm = 00.00 ;
			
			for(int i =1;i<=trans_tble_Rows-1;i++){
				String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
				sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
				 if(sectionName.contains("Totals")){
					sectionName = "Total";}
				 if(sectionName.contains("BusinesssInterruption")){
						sectionName = "BusinessInterruption";}
				Total_GP = Total_GP + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_GP"));
				Total_GT = Total_GT + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_GT"));
				Total_NetNetPemium = Total_NetNetPemium + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_NetNetPremium"));
				Total_PenComm = Total_PenComm + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_PenComm"));
				Total_BrokComm = Total_BrokComm + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_BrokerComm"));
				Total_NetPremium = Total_NetPremium + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_NetPremium"));
			}
			
			data_map.put("PS_Total_GT", f.format(Total_GT));
			data_map.put("PS_Total_GP", f.format(Total_GP));
			data_map.put("PS_TotalPremium", f.format(exp_Premium));
			data_map.put("PS_NetNetPemiumTotal", f.format(Total_NetNetPemium));
			data_map.put("PS_BrokerCommissionTotal", f.format(Total_BrokComm));
			data_map.put("PS_PenCommTotal", f.format(Total_PenComm));
			
			String exp_Total_Premium = common.roundedOff(Double.toString(exp_Premium));
			String act_Total_Premium = k.getAttribute("SPI_Total_Premium", "value");
			act_Total_Premium = act_Total_Premium.replaceAll(",", "");
			
			TestUtil.reportStatus("---------------Total Premium-----------------","Info",false);
			
			CommonFunction.compareValues(Double.parseDouble(exp_Total_Premium),Double.parseDouble(act_Total_Premium),"Total Premium.");
			
			return true;
		   
		}catch(Throwable t){ 
					return false;
			
		}
	}

public boolean Verify_premiumSummaryTable_CCJ(){
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
	locator_map.put("PenComm", "pencom");
	locator_map.put("NetPremium", "nprem");
	locator_map.put("BrokerComm", "comm");
	locator_map.put("GrossPremium", "gprem");
	locator_map.put("InsuranceTax", "gipt");
	
	final Map<String,String> section_map = new HashMap<>();
	
	
//	section_map.put("EmployersLiability", "el");
	
	section_map.put("MaterialDamage", "md");
	section_map.put("BusinessInterruption", "bi");
	section_map.put("Money&Assault","ma");
	section_map.put("EmployersLiability","el");
	section_map.put("PublicLiability","pl");
	section_map.put("PersonalAccident","pa");
	section_map.put("PersonalAccidentOptional","pao");
	section_map.put("GoodsinTransit","gt");
	section_map.put("LegalExpenses","le");
	section_map.put("Terrorism", "t");
	section_map.put("Total", "tot");
	
	double exp_Premium = 0.0;
	
	try{
	
		String annualTble_xpath = "html/body/div[3]/form/div/table[2]";
		String policy_status_actual = k.getText("Policy_status_header");
		int trans_tble_Rows = driver.findElements(By.xpath(annualTble_xpath+"/tbody/tr")).size();
		int trans_tble_Cols = driver.findElements(By.xpath(annualTble_xpath+"/thead/tr/th")).size();
		String sectionName = null;
		PremiumExcTerrDocExp = 0;
		PremiumExcTerrDocExp = 0;
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
				customAssert.assertTrue(funcAddInput_PremiumSummary_CCJ(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
				
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
				
					customAssert.assertTrue(funcAddInput_PremiumSummary_CCJ(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
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
		if(common.currentRunningFlow.equalsIgnoreCase("Rewind") && TestBase.businessEvent.equalsIgnoreCase("Rewind")){
			
			if(!PremiumFlag)
				for(int i =1;i<=trans_tble_Rows-1;i++){
					String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
					sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
					if(sectionName.contains("Totals"))
						sectionName = "Total";
					if(sectionName.contains("BusinesssInterruption"))
						sectionName = "BusinessInterruption";
				
					customAssert.assertTrue(funcAddInput_PremiumSummary_CCJ(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
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
				
					customAssert.assertTrue(funcAddInput_PremiumSummary_CCJ(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
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
				if(sectionName.contains("Totals"))
					sectionName = "Total";
				if(sectionName.contains("BusinesssInterruption"))
					sectionName = "BusinessInterruption";
			
				customAssert.assertTrue(funcAddInput_PremiumSummary_CCJ(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
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
					
					
					case "PersonalAccident":
						//code = "PersonalAccidentStandard";
						sectionName = "PersonalAccidentStandard";
						break;
					
					case "GoodsinTransit":
						//code = "GoodsInTransit";
						sectionName = "GoodsInTransit";
						break;
					
					
					}
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
							customAssert.assertTrue(funcAddInput_PremiumSummary_CCJ(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
						}else{
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
							common.MTA_excel_data_map.put("PS_"+sectionName+"_PenComm_rate", penComm);
							common.MTA_excel_data_map.put("PS_"+sectionName+"_BrokerComm_rate", BrokComm);
									
							
						}
					}else{
						
						if(((String)common.NB_excel_data_map.get("CD_"+sectionName)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_"+sectionName)).equals("Yes")){
							if(sectionName.contains("PersonalAccident")){
								sectionName="PersonalAccident";
							}
							if(sectionName.contains("GoodsInTransit")){
								sectionName="GoodsinTransit";
							}
							customAssert.assertTrue(funcAddInput_PremiumSummary_CCJ(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
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
							common.MTA_excel_data_map.put("PS_"+sectionName+"_PenComm_rate", penComm);
							common.MTA_excel_data_map.put("PS_"+sectionName+"_BrokerComm_rate", BrokComm);
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
				
					err_count = err_count + func_PremiumSummaryCalculation_CCJ(section_map.get(sectionName),sectionName,locator_map);
				exp_Premium = exp_Premium + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_TotalPremium"));
			}
			
			double Total_GP = 00.00;
			double Total_GT = 00.00;
			double Total_NetNetPemium = 00.00,Total_PenComm = 00.00,Total_NetPremium=00.00 ,Total_BrokComm = 00.00 ;
			
			for(int i =1;i<=trans_tble_Rows-1;i++){
				String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
				sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
				 if(sectionName.contains("Totals")){
					sectionName = "Total";}
				 if(sectionName.contains("BusinesssInterruption")){
						sectionName = "BusinessInterruption";}
				Total_GP = Total_GP + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_GP"));
				Total_GT = Total_GT + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_GT"));
				Total_NetNetPemium = Total_NetNetPemium + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_NetNetPremium"));
				Total_PenComm = Total_PenComm + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_PenComm"));
				Total_BrokComm = Total_BrokComm + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_BrokerComm"));
				Total_NetPremium = Total_NetPremium + Double.parseDouble((String)data_map.get("PS_"+sectionName+"_NetPremium"));
			}
			
			data_map.put("PS_Total_GT", f.format(Total_GT));
			data_map.put("PS_Total_GP", f.format(Total_GP));
			data_map.put("PS_TotalPremium", f.format(exp_Premium));
			data_map.put("PS_NetNetPemiumTotal", f.format(Total_NetNetPemium));
			data_map.put("PS_BrokerCommissionTotal", f.format(Total_BrokComm));
			data_map.put("PS_PenCommTotal", f.format(Total_PenComm));
			
			String exp_Total_Premium = common.roundedOff(Double.toString(exp_Premium));
			String act_Total_Premium = k.getAttribute("SPI_Total_Premium", "value");
			act_Total_Premium = act_Total_Premium.replaceAll(",", "");
			
			TestUtil.reportStatus("---------------Total Premium-----------------","Info",false);
			
			CommonFunction.compareValues(Double.parseDouble(exp_Total_Premium),Double.parseDouble(act_Total_Premium),"Total Premium.");
			
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
			String BrokerCommXpath =null;
			String GrossCommXpath = null;
			
			// changes made for CR 264_001, CR 264_002, CR 264_003, CR 264_004, CR 264_005, CR 264_006
			if(cover_name.contains("md")){
				PencCommXpath = "//*[@name='md_ccc"+"_penr']";
				BrokerCommXpath ="//*[@name='md_ccc"+"_comr']" ;
			    GrossCommXpath= ".//*[@id='md_ccc"+"_gcomm']";
			}else if(cover_name.contains("qgt")){
				 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
				 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
				 GrossCommXpath ="//*[contains(@id,'"+cover_name+"_gcomm')]";
			}
			else if(cover_name.contains("gtd_git")){
				 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
				 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
				 GrossCommXpath ="//*[contains(@id,'"+cover_name+"_gcomm')]";
			}else if(cover_name.contains("paa_pa") || cover_name.contains("pab_pa")){
				 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
				 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
				 GrossCommXpath ="//*[contains(@id,'"+cover_name+"_gcomm')]";
			}
			else if(cover_name.contains("el")){
				PencCommXpath = "//*[@name='el_ccc"+"_penr']";
				BrokerCommXpath ="//*[@name='el_ccc"+"_comr']" ;
			    GrossCommXpath= ".//*[@id='el_ccc"+"_gcomm']";
			}else if(cover_name.contains("cma")){
				PencCommXpath = "//*[@name='cma"+"_penr']";
				BrokerCommXpath ="//*[@name='cma"+"_comr']" ;
			    GrossCommXpath= ".//*[@id='cma"+"_gcomm']";
			}else if(cover_name.contains("do")){
				 PencCommXpath = "//*[@id='dob_do_penr']";
				 BrokerCommXpath ="//*[@id='dob_do_comr']";
				 GrossCommXpath ="//*[@id='dob_do_gcomm']";
			}else{
				 PencCommXpath = "//*[contains(@id,'_"+cover_name+"_penr')]";
				 BrokerCommXpath ="//*[contains(@id,'_"+cover_name+"_comr')]";
				 GrossCommXpath ="//*[contains(@id,'_"+cover_name+"_gcomm')]";
			}
			if(TestBase.product.equals("OED")){
				 PencCommXpath = "//*[contains(@id,'_"+cover_name+"_penr')]";
				 BrokerCommXpath ="//*[contains(@id,'_"+cover_name+"_comr')]";
				 GrossCommXpath ="//*[contains(@id,'_"+cover_name+"_gcomm')]";
			}
			if(TestBase.product.equals("GTA") || TestBase.product.equals("GTB")){
				 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
				 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
				 GrossCommXpath ="//*[contains(@id,'"+cover_name+"_gcomm')]";
			}

			/*else if(cover_name.contains("el")){
				PencCommXpath = "//*[@name='el_ccc"+"_penr']";
				BrokerCommXpath ="//*[@name='el_ccc"+"_comr']" ;
			    GrossCommXpath= ".//*[@id='el_ccc"+"_gcomm']";
			}else if(cover_name.contains("cma")){
				PencCommXpath = "//*[@name='cma"+"_penr']";
				BrokerCommXpath ="//*[@name='cma"+"_comr']" ;
			    GrossCommXpath= ".//*[@id='cma"+"_gcomm']";
			}else if(cover_name.contains("do")){
				 PencCommXpath = "//*[@id='dob_do_penr']";
				 BrokerCommXpath ="//*[@id='dob_do_comr']";
				 GrossCommXpath ="//*[@id='dob_do_gcomm']";
			}else if(TestBase.product.equals("OED")){
				 PencCommXpath = "//*[contains(@id,'_"+cover_name+"_penr')]";
				 BrokerCommXpath ="//*[contains(@id,'_"+cover_name+"_comr')]";
				 GrossCommXpath ="//*[contains(@id,'_"+cover_name+"_gcomm')]";
			}
			else if(TestBase.product.equals("GTA")){
				 PencCommXpath = "//*[contains(@id,'"+cover_name+"_penr')]";
				 BrokerCommXpath ="//*[contains(@id,'"+cover_name+"_comr')]";
				 GrossCommXpath ="//*[contains(@id,'"+cover_name+"_gcomm')]";
			}else{
				 PencCommXpath = "//*[contains(@id,'_"+cover_name+"_penr')]";
				 BrokerCommXpath ="//*[contains(@id,'_"+cover_name+"_comr')]";
				 GrossCommXpath ="//*[contains(@id,'_"+cover_name+"_gcomm')]";
			}*/
			
			
			// Changes made for CR265 B_001 , CR265 B_002
			String penCommRate = UpdatedCommissionRate((String)data_map.get("PS_"+code+"_PenComm_rate"));
			String broakerCommRate = UpdatedCommissionRate((String)data_map.get("PS_"+code+"_BrokerComm_rate"));
			
			customAssert.assertTrue(k.InputByXpath(PencCommXpath, (String)data_map.get("PS_"+code+"_PenComm_rate")), "Unable to enter value of Pen Commission for "+cover_name+".");
			driver.findElement(By.xpath(GrossCommXpath)).sendKeys("");
			String ActualPenComm = driver.findElement(By.xpath(PencCommXpath)).getAttribute("value");
			CommonFunction.compareValues(Double.parseDouble(penCommRate),Double.parseDouble(ActualPenComm), "Pen Commission Round off.");
			customAssert.assertTrue(k.InputByXpath(BrokerCommXpath, (String)data_map.get("PS_"+code+"_BrokerComm_rate")), "Unable to enter value of Broker Commission for "+cover_name+".");
			driver.findElement(By.xpath(GrossCommXpath)).sendKeys("");
			String ActualBrokerComm = driver.findElement(By.xpath(BrokerCommXpath)).getAttribute("value");
			CommonFunction.compareValues(Double.parseDouble(broakerCommRate),Double.parseDouble(ActualBrokerComm), "Broker Commission Round off.");
			
			
			//k.foc(GrossCommXpath);
			driver.findElement(By.xpath(GrossCommXpath)).sendKeys("");
			if(common.product.equalsIgnoreCase("POF")){
				double penComm = Double.parseDouble((String)data_map.get("PS_"+code+"_PenComm_rate"));
				double brokerComm = Double.parseDouble((String)data_map.get("PS_"+code+"_BrokerComm_rate"));
				double totalComm = Double.parseDouble(penCommRate) + Double.parseDouble(broakerCommRate);
				if(totalComm >= 50){
				double expPenComm = 50 - brokerComm;
			
				if(expPenComm <=0){expPenComm =0;}
				double actPenComm = Double.parseDouble(driver.findElement(By.xpath(PencCommXpath)).getAttribute("value"));
				CommonFunction.compareValues(expPenComm, actPenComm, "Pen Commission");
				
				TestUtil.reportStatus("Verified Gross Commission is 50 as total of Pen Commission"+penCommRate+" and Broker Commission"+broakerCommRate+" is more than equal to 50.", "Pass", false);
				customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Covers Screen .");
				double grossComm = Double.parseDouble(driver.findElement(By.xpath(GrossCommXpath)).getAttribute("value"));
				
					if(grossComm == 50){
					TestUtil.reportStatus("Verified Gross Commission is 50 as total of Pen Commission"+penCommRate+" and Broker Commission"+broakerCommRate+" is more than equal to 50.", "Pass", false);
					ActualPenComm = driver.findElement(By.xpath(PencCommXpath)).getAttribute("value");
					String ActualBrokeComm = driver.findElement(By.xpath(BrokerCommXpath)).getAttribute("value");
					customAssert.assertTrue(WriteDataToXl(common.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_PenComm_rate", ActualPenComm,data_map),"Error while writing Pen Commission data to excel .");
					customAssert.assertTrue(WriteDataToXl(common.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_BrokerComm_rate", ActualBrokeComm,data_map),"Error while writing Broker Commission data to excel .");
					}
				}
				if(totalComm >= 35){
				 referrals_list.add(cover_name);
				}
					
			}
			return retvalue;
			
		}catch(Throwable t) {
	        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
	        Assert.fail("Premium Summary Add Input function is having issue(S). \n", t);
	        return false;
	 }
	}
	
	public boolean funcAddInput_PremiumSummary_CCJ(String code, String cover_name,Map<Object,Object> data_map) {
		boolean retvalue=true;
		String testName = (String)data_map.get("Automation Key");
		try{
			//cob_pl_pr_penr
			String PencCommXpath =null;
			String BrokerCommXpath =null;
			String GrossCommXpath = null;
			
			// changes made for CR 264_001, CR 264_002, CR 264_003, CR 264_004, CR 264_005, CR 264_006
			if(cover_name.contains("md")){
				PencCommXpath = "//*[@name='md_ccj"+"_penr']";
				BrokerCommXpath ="//*[@name='md_ccj"+"_comr']" ;
			    GrossCommXpath= ".//*[@id='md_ccj"+"_gcomm']";
			}else if(cover_name.contains("el")){
				PencCommXpath = "//*[@name='el_ccj"+"_penr']";
				BrokerCommXpath ="//*[@name='el_ccj"+"_comr']" ;
			    GrossCommXpath= ".//*[@id='el_ccj"+"_gcomm']";
			}else{
				
				 PencCommXpath = "//*[contains(@id,'_"+cover_name+"_penr')]";
				 BrokerCommXpath ="//*[contains(@id,'_"+cover_name+"_comr')]";
				 GrossCommXpath ="//*[contains(@id,'_"+cover_name+"_gcomm')]";
			}
			// Changes made for CR265 B_001 , CR265 B_002
			String penCommRate = UpdatedCommissionRate((String)data_map.get("PS_"+code+"_PenComm_rate"));
			String broakerCommRate = UpdatedCommissionRate((String)data_map.get("PS_"+code+"_BrokerComm_rate"));
			
			customAssert.assertTrue(k.InputByXpath(PencCommXpath, (String)data_map.get("PS_"+code+"_PenComm_rate")), "Unable to enter value of Pen Commission for "+cover_name+".");
			driver.findElement(By.xpath(GrossCommXpath)).sendKeys("");
			String ActualPenComm = driver.findElement(By.xpath(PencCommXpath)).getAttribute("value");
			CommonFunction.compareValues(Double.parseDouble(penCommRate),Double.parseDouble(ActualPenComm), "Pen Commission Round off.");
			customAssert.assertTrue(k.InputByXpath(BrokerCommXpath, (String)data_map.get("PS_"+code+"_BrokerComm_rate")), "Unable to enter value of Broker Commission for "+cover_name+".");
			driver.findElement(By.xpath(GrossCommXpath)).sendKeys("");
			String ActualBrokerComm = driver.findElement(By.xpath(BrokerCommXpath)).getAttribute("value");
			CommonFunction.compareValues(Double.parseDouble(broakerCommRate),Double.parseDouble(ActualBrokerComm), "Broker Commission Round off.");
			
			
			//k.foc(GrossCommXpath);
			driver.findElement(By.xpath(GrossCommXpath)).sendKeys("");
			
			if(TestBase.product.equalsIgnoreCase("CCJ")){
				common_CCJ.CCJ_Rater = OR.getORProperties();
				double penComm = Double.parseDouble((String)data_map.get("PS_"+code+"_PenComm_rate"));
				double brokerComm = Double.parseDouble((String)data_map.get("PS_"+code+"_BrokerComm_rate"));
				double totalComm = Double.parseDouble(penCommRate) + Double.parseDouble(broakerCommRate);
				double total_pen_broker_ = Double.parseDouble(common_CCJ.CCJ_Rater.getProperty("CCJ_PEN_BROKER_MAX_Limit"));
				if(totalComm >= total_pen_broker_){
				double expPenComm = total_pen_broker_ - brokerComm;
			
				if(expPenComm <=0){expPenComm =0;}
				double actPenComm = Double.parseDouble(driver.findElement(By.xpath(PencCommXpath)).getAttribute("value"));
				CommonFunction.compareValues(expPenComm, actPenComm, "Pen Commission");
				
				TestUtil.reportStatus("Verified Gross Commission is"+total_pen_broker_+" as total of Pen Commission"+penCommRate+" and Broker Commission"+broakerCommRate+" is more than equal to "+total_pen_broker_+" .", "Pass", false);
				customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Covers Screen .");
				double grossComm = Double.parseDouble(driver.findElement(By.xpath(GrossCommXpath)).getAttribute("value"));
				
					if(grossComm == total_pen_broker_){
						TestUtil.reportStatus("Verified Gross Commission is"+total_pen_broker_+" as total of Pen Commission"+penCommRate+" and Broker Commission"+broakerCommRate+" is more than equal to "+total_pen_broker_+" .", "Pass", false);
						ActualPenComm = driver.findElement(By.xpath(PencCommXpath)).getAttribute("value");
						String ActualBrokeComm = driver.findElement(By.xpath(BrokerCommXpath)).getAttribute("value");
						customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_PenComm_rate", ActualPenComm,data_map),"Error while writing Pen Commission data to excel .");
						customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_BrokerComm_rate", ActualBrokeComm,data_map),"Error while writing Broker Commission data to excel .");
					}
				}
				
					
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
			
			double NetNet_Premium = Double.parseDouble((String)map_data.get("PS_"+covername+"_NetNetPremium"));
			
		try{
				
				TestUtil.reportStatus("---------------"+covername+"-----------------","Info",false);
				//SPI Pen commission Calculation : 
				double pen_comm = ((NetNet_Premium / (1-((Double.parseDouble((String)map_data.get("PS_"+covername+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+covername+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+covername+"_PenComm_rate"))/100)));
				String pc_expected = common.roundedOff(Double.toString(pen_comm));
				if(code.contains("md")){
					code = "md_ccc_";
				}else if(code.contains("gta") || code.contains("gtb") ||code.contains("qgt") || code.contains("gtd_git")|| code.contains("cma") || code.contains("paa_pa")|| code.contains("pab_pa")){
					code= code+"_";
				}else if(code.contains("el")){
					if(TestBase.product.equals("OED")){
						code="_"+code+"_";
					}else
					code = "el_ccc_";}
				else{
					code="_"+code+"_";}
					
				
				String pc_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("PenComm")+"')]", "value");
				CommonFunction.compareValues(Double.parseDouble(pc_expected),Double.parseDouble(pc_actual),"Pen Commission");
				map_data.put("PS_"+covername+"_PenComm",pc_expected);
				
				// Net Premium verification : 
				double netP = Double.parseDouble(pc_expected) + NetNet_Premium;
				String netP_expected = common.roundedOff(Double.toString(netP));
				String netP_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("NetPremium")+"')]", "value");
				CommonFunction.compareValues(Double.parseDouble(netP_expected),Double.parseDouble(netP_actual),"Net Premium");
				map_data.put("PS_"+covername+"_NetPremium",netP_expected);
				
				// Broker commission Calculation : 
				double broker_comm = ((NetNet_Premium / (1-((Double.parseDouble((String)map_data.get("PS_"+covername+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+covername+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+covername+"_BrokerComm_rate"))/100)));
				String bc_expected = common.roundedOff(Double.toString(broker_comm));
				String bc_actual =  k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("BrokerComm")+"')]", "value");
				CommonFunction.compareValues(Double.parseDouble(bc_expected),Double.parseDouble(bc_actual),"Broker Commission");
				map_data.put("PS_"+covername+"_BrokerComm",bc_expected);
				
				// GrossPremium verification :  
				double grossP = Double.parseDouble(netP_expected) + Double.parseDouble(bc_expected);
				
				if(!covername.contains("Terrorism") && !covername.contains("PersonalAccidentOptional")){
					PremiumExcTerrDocExp = PremiumExcTerrDocExp + grossP;
				}else if(!covername.contains("Terrorism") ){
					TerPremDocExp = grossP;
				}
				
				String grossP_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("GrossPremium")+"')]", "value");
				
				if(!covername.contains("Terrorism") && !covername.contains("PersonalAccidentOptional")){
					PremiumExcTerrDocAct = PremiumExcTerrDocAct + Double.parseDouble(grossP_actual);
				}else if(covername.contains("Terrorism")){
					TerPremDocAct = Double.parseDouble(grossP_actual);					
					
				}
				if(TestBase.product.equals("OED") && (!covername.contains("PersonalAccident")&& !covername.contains("LegalExpenses"))){
					PremiumExcTerrDocActOED = PremiumExcTerrDocActOED + Double.parseDouble(grossP_actual);
				}
				
				CommonFunction.compareValues(grossP,Double.parseDouble(grossP_actual),"Gross Premium");
				map_data.put("PS_"+covername+"_GP",(grossP_actual));
				
				if(!common_CCD.isGrossPremiumReferralCheckDone){
					//Section minimum  Premium Referral Messages - For MD, BI, El, PL, Money , GIT
					if(!covername.contains("PersonalAccident") && !covername.contains("LegalExpenses") && !covername.contains("Terrorism")){
						customAssert.assertTrue(common_CCD.func_Check_Section_Minimum_Gross_Premium(covername,grossP), "Error while verifying section minimum gross premium . ");
					}
				}
				
				if(common.currentRunningFlow.equals("MTA")){
					if(((String)map_data.get("PS_TaxExempt")).equalsIgnoreCase("Yes"))
						Tax_map_data.put("PS_"+covername+"_IPT", "0.0");
					
				}
				double InsuranceTax1 = 0.0;
				if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
					
					
					String InsuranceTax = k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("InsuranceTax")+"')]", "value");
					
					if(grossP == 0.00 || InsuranceTax.contains("0.00")){
						InsuranceTax1 = 0.00;
					}else{
						double IPT = (Double.parseDouble(InsuranceTax) / grossP) * 100.0;
						InsuranceTax1 = (grossP * IPT)/100.0;
					}
					
					//TestUtil.WriteDataToXl(TestBase.product+"_"+event, "Premium Summary",(String)Tax_map_data.get("Automation Key"), "PS_"+covername+"_IPT", common_HHAZ.roundedOff(Double.toString(IPT)), Tax_map_data);
				}else{
					InsuranceTax1 = (grossP * Double.parseDouble((String)Tax_map_data.get("PS_"+covername+"_IPT")))/100.0;
				}
				
				
				//InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(InsuranceTax)));
				String InsuranceTax_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("InsuranceTax")+"')]", "value");
				
				if(covername.contains("Terrorism")){
					InsTaxTerrDoc = Double.parseDouble(InsuranceTax_actual);
				}
				if(!covername.contains("PersonalAccidentOptional")){
				InsTaxDocAct = InsTaxDocAct + Double.parseDouble(InsuranceTax_actual);
				InsTaxDocExp = InsTaxDocExp + InsuranceTax1;
				}
				if(TestBase.product.equals("OED") && (!covername.contains("PersonalAccident")&& !covername.contains("LegalExpenses"))){
					InsTaxDocActOED = InsTaxDocActOED + Double.parseDouble(InsuranceTax_actual);
				}
				CommonFunction.compareValues(InsuranceTax1,Double.parseDouble(InsuranceTax_actual),"Insurance Tax");
				map_data.put("PS_"+covername+"_GT",Double.toString(InsuranceTax1));
				
				//SPI Total Premium verification : 
				double Premium = grossP + InsuranceTax1;
				String p_expected = common.roundedOff(Double.toString(Premium));
				
				String p_actual = common.roundedOff(k.getAttributeByXpath("//*[contains(@id,'"+code+"tot')]", "value"));
				
				TotalPremiumWithAdminDocAct = TotalPremiumWithAdminDocAct + Double.parseDouble(p_actual);
				TotalPremiumWithAdminDocExp = TotalPremiumWithAdminDocExp + Double.parseDouble(p_expected);
				
				double premium_diff = Double.parseDouble(common.roundedOff(Double.toString(Double.parseDouble(p_expected) - Double.parseDouble(p_actual))));
				
				if(premium_diff<=0.50 && premium_diff>=-0.50){
					TestUtil.reportStatus("Total Premium [<b> "+p_expected+" </b>] matches with actual total premium [<b> "+p_actual+" </b>]as expected for "+covername+" on premium summary page.", "Pass", false);
					map_data.put("PS_"+covername+"_TotalPremium", p_expected);
					return 0;
					
				}else{
					TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+p_expected+"</b>] and Actual Premium [<b> "+p_actual+"</b>] for "+code+" on premium summary page. </p>", "Fail", true);
					map_data.put("PS_"+covername+"_TotalPremium", p_expected);
					return 1;
				}
					
		}catch(Throwable t) { 
	        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
	        Assert.fail("Insured Properties function is having issue(S). \n", t);
	        return 0;
	 }
		
	}
	
	public int func_PremiumSummaryCalculation_CCJ(String code,String covername,Map<String,String> premium_loc) {
		
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
			
			double NetNet_Premium = Double.parseDouble((String)map_data.get("PS_"+covername+"_NetNetPremium"));
			
		try{
				
				TestUtil.reportStatus("---------------"+covername+"-----------------","Info",false);
				//SPI Pen commission Calculation : 
				double pen_comm = ((NetNet_Premium / (1-((Double.parseDouble((String)map_data.get("PS_"+covername+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+covername+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+covername+"_PenComm_rate"))/100)));
				String pc_expected = common.roundedOff(Double.toString(pen_comm));
				if(code.contains("md"))
					code = "md_ccj_";
				else if(code.contains("el"))
					code = "el_ccj_";
				else
					code="_"+code+"_";
									
				String pc_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("PenComm")+"')]", "value");
				CommonFunction.compareValues(Double.parseDouble(pc_expected),Double.parseDouble(pc_actual),"Pen Commission");
				map_data.put("PS_"+covername+"_PenComm",pc_expected);
				
				// Net Premium verification : 
				double netP = Double.parseDouble(pc_expected) + NetNet_Premium;
				String netP_expected = common.roundedOff(Double.toString(netP));
				String netP_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("NetPremium")+"')]", "value");
				CommonFunction.compareValues(Double.parseDouble(netP_expected),Double.parseDouble(netP_actual),"Net Premium");
				map_data.put("PS_"+covername+"_NetPremium",netP_expected);
				
				// Broker commission Calculation : 
				double broker_comm = ((NetNet_Premium / (1-((Double.parseDouble((String)map_data.get("PS_"+covername+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+covername+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+covername+"_BrokerComm_rate"))/100)));
				String bc_expected = common.roundedOff(Double.toString(broker_comm));
				String bc_actual =  k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("BrokerComm")+"')]", "value");
				CommonFunction.compareValues(Double.parseDouble(bc_expected),Double.parseDouble(bc_actual),"Broker Commission");
				map_data.put("PS_"+covername+"_BrokerComm",bc_expected);
				
				// GrossPremium verification :  
				double grossP = Double.parseDouble(netP_expected) + Double.parseDouble(bc_expected);
				
				if(!covername.contains("Terrorism") && !covername.contains("PersonalAccidentOptional")){
					PremiumExcTerrDocExp = PremiumExcTerrDocExp + grossP;
				}else if(!covername.contains("Terrorism") ){
					TerPremDocExp = grossP;
				}
				
				String grossP_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("GrossPremium")+"')]", "value");
				
				if(!covername.contains("Terrorism") && !covername.contains("PersonalAccidentOptional")){
					PremiumExcTerrDocAct = PremiumExcTerrDocAct + Double.parseDouble(grossP_actual);
				}else if(covername.contains("Terrorism")){
					TerPremDocAct = Double.parseDouble(grossP_actual);										
				}
		
				
				CommonFunction.compareValues(grossP,Double.parseDouble(grossP_actual),"Gross Premium");
				map_data.put("PS_"+covername+"_GP",(grossP_actual));
				
				if(!common_CCD.isGrossPremiumReferralCheckDone){
					//Section minimum  Premium Referral Messages - For MD, BI, El, PL, Money , GIT
					if(!covername.contains("PersonalAccident") && !covername.contains("LegalExpenses") && !covername.contains("Terrorism")){
						customAssert.assertTrue(common_CCD.func_Check_Section_Minimum_Gross_Premium(covername,grossP), "Error while verifying section minimum gross premium . ");
					}
				}
				
				if(common.currentRunningFlow.equals("MTA")){
					if(((String)map_data.get("PS_TaxExempt")).equalsIgnoreCase("Yes"))
						Tax_map_data.put("PS_"+covername+"_IPT", "0.0");
					
				}
					
				if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
					String InsuranceTax = k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("InsuranceTax")+"')]", "value");
					double IPT = (Double.parseDouble(InsuranceTax) / grossP) * 100.0;
					TestUtil.WriteDataToXl(TestBase.product+"_"+event, "Premium Summary",(String)Tax_map_data.get("Automation Key"), "PS_"+covername+"_IPT", common_HHAZ.roundedOff(Double.toString(IPT)), Tax_map_data);
				}
				
				double InsuranceTax = (grossP * Double.parseDouble((String)Tax_map_data.get("PS_"+covername+"_IPT")))/100.0;
				//InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(InsuranceTax)));
				String InsuranceTax_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("InsuranceTax")+"')]", "value");
				
				if(covername.contains("Terrorism")){
					InsTaxTerrDoc = Double.parseDouble(InsuranceTax_actual);
				}
				if(!covername.contains("Terrorism") && !covername.contains("PersonalAccidentOptional")){
				InsTaxDocAct = InsTaxDocAct + Double.parseDouble(InsuranceTax_actual);
				InsTaxDocExp = InsTaxDocExp + InsuranceTax;
				}
				
				CommonFunction.compareValues(InsuranceTax,Double.parseDouble(InsuranceTax_actual),"Insurance Tax");
				map_data.put("PS_"+covername+"_GT",Double.toString(InsuranceTax));
				
				//SPI Total Premium verification : 
				double Premium = grossP + InsuranceTax;
				String p_expected = common.roundedOff(Double.toString(Premium));
				
				String p_actual = common.roundedOff(k.getAttributeByXpath("//*[contains(@id,'"+code+"tot')]", "value"));
				
				TotalPremiumWithAdminDocAct = TotalPremiumWithAdminDocAct + Double.parseDouble(p_actual);
				TotalPremiumWithAdminDocExp = TotalPremiumWithAdminDocExp + Double.parseDouble(p_expected);
				
				double premium_diff = Double.parseDouble(common.roundedOff(Double.toString(Double.parseDouble(p_expected) - Double.parseDouble(p_actual))));
				
				if(premium_diff<=0.20 && premium_diff>=-0.20){
					TestUtil.reportStatus("Total Premium [<b> "+p_expected+" </b>] matches with actual total premium [<b> "+p_actual+" </b>]as expected for "+covername+" on premium summary page.", "Pass", false);
					map_data.put("PS_"+covername+"_TotalPremium", p_expected);
					return 0;
					
				}else{
					TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+p_expected+"</b>] and Actual Premium [<b> "+p_actual+"</b>] for "+code+" on premium summary page. </p>", "Fail", true);
					map_data.put("PS_"+covername+"_TotalPremium", p_expected);
					return 1;
				}
					
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
				case "Renewal":
					data_map = common.Renewal_excel_data_map;
					break;
				case "Rewind":
					data_map = common.Rewind_excel_data_map;
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
				TransactionTable=true;
				
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
		
		//Policy minimum  Premium Referral Message
		customAssert.assertTrue(common_CCD.func_Check_Section_Minimum_Gross_Premium("Policy",exp_value), "Error while verifying Policy minimum gross premium . ");
		
		
		exp_value = 0.0;
		for(String section : sectionNames){
			if(!section.contains("Total"))
				exp_value = exp_value + transaction_Premium_Values.get(section).get("Insurance Tax");
		}
		String t_InsuranceTax_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Insurance Tax"));
		CommonFunction.compareValues(exp_value,Double.parseDouble(t_InsuranceTax_actual),"Insurance Tax");
		InsTaxDocAct = Double.parseDouble(t_InsuranceTax_actual);
		
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
			case "Rewind":
				map_data = common.Rewind_excel_data_map;
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
			
			
				//Section minimum  Premium Referral Messages - For MD, BI, El, PL, Money , GIT
				if(!sectionNames.contains("Personal Accident") && !sectionNames.contains("Legal Expenses") && !sectionNames.contains("Terrorism")){
					customAssert.assertTrue(common_CCD.func_Check_Section_Minimum_Gross_Premium(sectionNames,t_grossP), "Error while verifying section minimum gross premium . ");
				}
			
			
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
				TerPremDocAct = AdditionalTerPDocAct;
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
	 	common_HHAZ.totalGrossPremium = 0.0;
	 	common_HHAZ.totalGrossTax = 0.0;
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
				
				common.compareValues(common_HHAZ.totalGrossPremium, Double.parseDouble(actualTotalGP), "Total Gross Premium from Insuracnce Tax screen");
				common.compareValues(common_HHAZ.totalGrossTax, Double.parseDouble(actualTotalGT), "Total Gross Tax from Insuracnce Tax screen");
					
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
		
		if(description.equalsIgnoreCase("Gross Tax")){
			double actVal = Double.parseDouble(actualValue);
			double expVal = Double.parseDouble(expectedValue);
			double diffrence = actVal - expVal;
			
			if(diffrence<=0.1 && diffrence>=-0.1){
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
					Assert.assertTrue(common_HHAZ.funcQuoteCheck(common.NB_excel_data_map));
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
					customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					if(TestBase.businessEvent.equals("NB")){
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("NB_Status")+"  ]</b> status. ", "Info", true);
					}
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
					customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
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
					customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.Requote_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("Requote_Status")+"  ]</b> status. ", "Info", true);
					
					break;
				case "Rewind":
					
					if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
						customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
						quoteDate = common.getUKDate();
						common.Rewind_excel_data_map.put("QuoteDate", quoteDate);
						customAssert.assertTrue(common.funcSearchPolicy(common.MTA_excel_data_map), "Policy Search function is having issue(S) . ");
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Endorsement On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
						if(TestBase.product.equalsIgnoreCase("POB") || TestBase.product.equalsIgnoreCase("CTA")){
							customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
							customAssert.assertTrue(common.transactionSummary((String)common.Rewind_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
						}else if(TestBase.product.equalsIgnoreCase("COA")){
							customAssert.assertTrue(common_VELA.funcPDFdocumentVerification("Documents"), "Document verification function is having issue(S) . ");
							customAssert.assertTrue(common_VELA.transactionSummary((String)common.Rewind_excel_data_map.get("Automation Key"), "", CommonFunction_VELA.product,CommonFunction_VELA.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
							
						}else{
							customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Document verification function is having issue(S) . ");
							customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.Rewind_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
						}
					}else if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("New Business") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
						customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
						quoteDate = common.getUKDate();
						common.Rewind_excel_data_map.put("QuoteDate", quoteDate);
						customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
						if(TestBase.product.equalsIgnoreCase("POB") || TestBase.product.equalsIgnoreCase("CTA")){
							customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
							customAssert.assertTrue(common.transactionSummary((String)common.Rewind_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
						}else if(TestBase.product.equalsIgnoreCase("COA")){
							customAssert.assertTrue(common_VELA.funcPDFdocumentVerification("Documents"), "Document verification function is having issue(S) . ");
							customAssert.assertTrue(common_VELA.transactionSummary((String)common.Rewind_excel_data_map.get("Automation Key"), "", CommonFunction_VELA.product,CommonFunction_VELA.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
							
						}else{
							customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Document verification function is having issue(S) . ");
							customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.Rewind_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
						}
					}else{
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
						customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
						customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
						if(TestBase.product.equalsIgnoreCase("POB") || TestBase.product.equalsIgnoreCase("CTA")){
							customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
							customAssert.assertTrue(common.transactionSummary((String)common.Rewind_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
						}else if(TestBase.product.equalsIgnoreCase("COA")){
							customAssert.assertTrue(common_VELA.funcPDFdocumentVerification("Documents"), "Document verification function is having issue(S) . ");
							customAssert.assertTrue(common_VELA.transactionSummary((String)common.Rewind_excel_data_map.get("Automation Key"), "", CommonFunction_VELA.product,CommonFunction_VELA.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
							
						}else{
							customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Document verification function is having issue(S) . ");
							customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.Rewind_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
						}
					}
					
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("Rewind_Status")+"  ]</b> status. ", "Info", true);
										
					break;
				case "Cancelled":
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.CAN_excel_data_map,code,event,"Cancelled"), "Verify Policy Status (Cancelled) function is having issue(S) . ");
					customAssert.assertTrue(transactionSummary((String)common.CAN_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
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
					customAssert.assertTrue(common_HHAZ.funcQuoteCheck(common.NB_excel_data_map));
					k.ImplicitWaitOff();
					k.ImplicitWaitOn();
					
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.MTA_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Quoted"), "Verify Policy Status (Endorsement Quoted) function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("POB") ||TestBase.product.equalsIgnoreCase("CTA") ){
						customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
						customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");}
					else{
						customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Error in PDF Verification (Draft Documents) function is having issue(S) . ");
					}
						
					
					customAssert.assertTrue(common.funcGoOnCover_Endorsement(common.NB_excel_data_map), "GoOnCover_Endorsement function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,(String)map_data.get("MTA_Status")), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("POB") ){
						customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
						customAssert.assertTrue(common.transactionSummary((String)common.MTA_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
					}
					if(TestBase.product.equalsIgnoreCase("CTA") ){
						customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
						customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
						customAssert.assertTrue(common_CTA.transactionSummary((String)common.MTA_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
					}else{
						customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Error in PDF Verification (Documents)(Endorsement On Cover) function is having issue(S) . ");
						customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.MTA_excel_data_map.get("Automation Key"), "", code, event), "MTA Transaction Summary function is having issue(S) . ");
					}
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("MTA_Status")+"  ]</b> status. ", "Info", true);
					break;
					
				case "Endorsement Declined":
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Submitted"), "Verify Policy Status (Endorsement Submitted) function is having issue(S) . ");
					customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"),"Unable to Click on Assign Underwriter button . ");
					customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter.");
				
					Assert.assertTrue(common.funcDecline(common.MTA_excel_data_map));
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					if(((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy")).equalsIgnoreCase("Yes") && ((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement")) {
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.MTA_excel_data_map,code,event,"Endorsement On Cover"), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
					}else{
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}
					
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("MTA_Status")+"  ]</b> status. ", "Info", true);
					break;
					
				case "Endorsement NTU":
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
					customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Error in PDF Verification (Draft Documents) function is having issue(S) . ");
					Assert.assertTrue(common.funcNTU(common.MTA_excel_data_map));
					
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					if(((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy")).equalsIgnoreCase("Yes") && ((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement")) {
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.MTA_excel_data_map,code,event,"Endorsement On Cover"), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
					}else{
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("MTA_Status")+"  ]</b> status. ", "Info", true);
					break;
					
				case "Endorsement Rewind":
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Submitted"), "Verify Policy Status (Endorsement Submitted) function is having issue(S) . ");
				    customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"), "Navigation problem to Transaction Summary page .");
					customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"),"Unable to Click on Assign Underwriter button . ");
					customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter.");
				
					Assert.assertTrue(common_HHAZ.funcQuoteCheck(common.NB_excel_data_map));
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
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Error in PDF Verification (Documents)(Endorsement On Cover) function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("CTA") ){
						customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
						customAssert.assertTrue(common_CTA.transactionSummary((String)common.MTA_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
					}else{
					customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.MTA_excel_data_map.get("Automation Key"), "", code, event), "MTA Transaction Summary function is having issue(S) . ");
					}
					
					if(TestBase.product.equalsIgnoreCase("CCD")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CCD_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "CCD_Rewind_03");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
					
					common_CCD.RewindFlow(code, "Rewind");
					
					}else if(TestBase.product.equalsIgnoreCase("CCJ")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CCJ_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "CCJ_Rewind_03");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_CCJ.RewindFlow(code, "Rewind");
					}else if(TestBase.product.equalsIgnoreCase("CCC")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CCC_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "CCC_Rewind_04");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
					
					common_CCC.RewindFlow(code, "Rewind");
					
					}
					else if(TestBase.product.equalsIgnoreCase("MFB")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "MFB_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "MFB_Rewind_03");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_MFB.RewindFlow(code, "Rewind");
					}
					else if(TestBase.product.equalsIgnoreCase("MFc")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "MFC_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "MFC_Rewind_03");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_MFC.RewindFlow(code, "Rewind");
					}else if(TestBase.product.equalsIgnoreCase("CMA")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CMA_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "CMA_Rewind_03");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_CMA.RewindFlow(code, "Rewind");
					}else if(TestBase.product.equalsIgnoreCase("OED")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "OED_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "OED_Rewind_04");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_OED.RewindFlow(code, "Rewind");
					}else if(TestBase.product.equalsIgnoreCase("GTC")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "GTC_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "GTC_Rewind_01");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_GTC.RewindFlow(code, "Rewind");
					}else if(TestBase.product.equalsIgnoreCase("GTA")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "GTA_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "GTA_Rewind_01");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_GTA.RewindFlow(code, "Rewind");
					}else if(TestBase.product.equalsIgnoreCase("GTB")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "GTB_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "GTB_Rewind_01");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_GTB.RewindFlow(code, "Rewind");
					}else if(TestBase.product.equalsIgnoreCase("PAA")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "PAA_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "PAA_Rewind_02");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_PAA.RewindFlow(code, "Rewind");
					}else if(TestBase.product.equalsIgnoreCase("PAB")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "PAB_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "PAB_Rewind_01");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_PAB.RewindFlow(code, "Rewind");
					}else if(TestBase.product.equalsIgnoreCase("GTD")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "GTD_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "GTD_Rewind_01");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_GTD.RewindFlow(code, "Rewind");
					}else if(TestBase.product.equalsIgnoreCase("DOB")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "DOB_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "DOB_Rewind_04");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						
						common_DOB.RewindFlow(code, "Rewind");
					}
					
					customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status (Endorsement Submitted (Rewind)) function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"), "Navigation problem to Premium Summary page .");
					customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
					quoteDate = common.getUKDate();
					common.Rewind_excel_data_map.put("QuoteDate", quoteDate);
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement On Cover"), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Document verification function is having issue(S) . ");
					if(TestBase.product.equalsIgnoreCase("CTA") ){
						customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
						customAssert.assertTrue(common_CTA.transactionSummary((String)common.MTA_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
					}else{
					customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.MTA_excel_data_map.get("Automation Key"), "", CommonFunction_VELA.product,CommonFunction_VELA.businessEvent), "Transaction Summary function is having issue(S) after MTA Rewind  . ");
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
					Assert.assertTrue(common_HHAZ.funcQuoteCheck(common.Renewal_excel_data_map));
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
					customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("Renewal_Status")+"  ]</b> status. ", "Info", true);
					break;
				case "Renewal NTU":
					//Not Taken Up
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
					Assert.assertTrue(common_HHAZ.funcQuoteCheck(common.Renewal_excel_data_map));
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
					Assert.assertTrue(common_HHAZ.funcQuoteCheck(common.Renewal_excel_data_map));
					
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.Renewal_excel_data_map.put("QuoteDate", quoteDate);
					
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal Quoted"), "Verify Policy Status (Quoted) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(common.funcGoOnCover(common.Renewal_excel_data_map), "Go On Cover function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					
					if(TestBase.product.equalsIgnoreCase("CCD")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CCD_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "CCD_Rewind_02");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
					
			
						common_CCD.RenewalRewindFlow(code, "Rewind");
					
					}else if(TestBase.product.equalsIgnoreCase("CCJ")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CCJ_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "CCJ_Rewind_02");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
					
						common_CCJ.RenewalRewindFlow(code, "Rewind");
					
					}else if(TestBase.product.equalsIgnoreCase("CCC")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CCC_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "CCC_Rewind_05");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
					
			
						common_CCC.RenewalRewindFlow(code, "Rewind");
					
					}else if(TestBase.product.equalsIgnoreCase("MFB")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "MFB_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "MFB_Rewind_02");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
					
			
						common_MFB.RenewalRewindFlow(code, "Rewind");
					
					}else if(TestBase.product.equalsIgnoreCase("MFC")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "MFC_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "MFC_Rewind_02");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						common_MFC.RenewalRewindFlow(code, "Rewind");
					
					}else if(TestBase.product.equalsIgnoreCase("CMA")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CMA_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "CMA_Rewind_02");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						common_CMA.RenewalRewindFlow(code, "Rewind");
					
					}else if(TestBase.product.equalsIgnoreCase("OED")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "OED_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "OED_Rewind_02");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						common_OED.RenewalRewindFlow(code, "Rewind");
					
					}else if(TestBase.product.equalsIgnoreCase("GTC")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "GTC_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "GTC_Rewind_01");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						common_GTC.RenewalRewindFlow(code, "Rewind");
					
					}else if(TestBase.product.equalsIgnoreCase("GTD")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "GTD_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "GTD_Rewind_02");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						common_GTD.RenewalRewindFlow(code, "Rewind");
					
					}else if(TestBase.product.equalsIgnoreCase("GTA")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "GTA_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "GTA_Rewind_02");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						common_GTA.RenewalRewindFlow(code, "Rewind");
					
					}else if(TestBase.product.equalsIgnoreCase("GTB")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "GTB_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "GTB_Rewind_02");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						//common_GTB.RenewalRewindFlow(code, "Rewind");
					
					}else if(TestBase.product.equalsIgnoreCase("PAA")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "PAA_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "PAA_Rewind_02");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						common_PAA.RenewalRewindFlow(code, "Rewind");
					
					}else if(TestBase.product.equalsIgnoreCase("PAB")){
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "PAB_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "PAB_Rewind_02");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						common_PAB.RenewalRewindFlow(code, "Rewind");
					
					}
					else if(TestBase.product.equalsIgnoreCase("DOB"))
					{
						common.Rewind_excel_data_map = new HashMap<>();
						common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "DOB_Rewind.xlsx");
						common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "DOB_Rewind_01");
						common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
								Events_Suite_TC_Xls);
						common_DOB.RenewalRewindFlow(code, "Rewind");
					
					}
						
					customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Rewind_excel_data_map,code,event,"Renewal Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
					customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Rewind_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Document verification function is having issue(S) . ");
					customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.Rewind_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
					
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("Renewal_Status")+"  ]</b> status. ", "Info", true);
					break;
				case "Renewal Endorsment On Cover":
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
					Assert.assertTrue(common_HHAZ.funcQuoteCheck(common.Renewal_excel_data_map));
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
					customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					
					if(TestBase.product.equalsIgnoreCase("CCD")){
						
					
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CCD_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "CCD_MTA_02");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
					
						common_CCD.MTAFlow(code, "MTA");
					
					}else if(TestBase.product.equalsIgnoreCase("CCJ")){
					
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CCJ_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "CCJ_MTA_03");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
					
						common_CCJ.MTAFlow(code, "MTA");
					}else if(TestBase.product.equalsIgnoreCase("CCC")){
						
					
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CCC_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "CCC_MTA_02");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
					
						common_CCC.MTAFlow(code, "MTA");
					
					}else if(TestBase.product.equalsIgnoreCase("MFB")){
						
					
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "MFB_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "MFB_MTA_02");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
					
						common_MFB.MTAFlow(code, "MTA");
					
					}else if(TestBase.product.equalsIgnoreCase("MFC")){
						
					
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "MFC_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "MFC_MTA_02");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
					
						common_MFC.MTAFlow(code, "MTA");
					
					}else if(TestBase.product.equalsIgnoreCase("CMA")){
						
					
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "CMA_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "CMA_MTA_02");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
					
						common_CMA.MTAFlow(code, "MTA");
					
					}else if(TestBase.product.equalsIgnoreCase("OED")){
						
					
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "OED_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "OED_MTA_02");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
					
						common_OED.MTAFlow(code, "MTA");
					
					}else if(TestBase.product.equalsIgnoreCase("GTC")){
						
					
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "GTC_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "GTC_MTA_01");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
					
						common_GTC.MTAFlow(code, "MTA");
					
					}else if(TestBase.product.equalsIgnoreCase("GTD")){
						
					
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "GTD_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "GTD_MTA_02");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
					
						common_GTD.MTAFlow(code, "MTA");
					
					}else if(TestBase.product.equalsIgnoreCase("GTA")){
						
					
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "GTA_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "GTA_MTA_02");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
					
						common_GTA.MTAFlow(code, "MTA");
					
					}else if(TestBase.product.equalsIgnoreCase("GTB")){
						
					
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "GTB_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "GTB_MTA_02");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
					
						//common_GTB.MTAFlow(code, "MTA");
					
					}else if(TestBase.product.equalsIgnoreCase("PAA")){
						
					
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "PAA_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "PAA_MTA_02");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
					
						common_PAA.MTAFlow(code, "MTA");
					
					}else if(TestBase.product.equalsIgnoreCase("PAB")){
						
					
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "PAB_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "PAB_MTA_02");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
					
						common_PAB.MTAFlow(code, "MTA");
					
					}else if(TestBase.product.equalsIgnoreCase("DOB")){
						
					
						common.MTA_excel_data_map = new HashMap<>();
						common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
						Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "DOB_MTA.xlsx");
						common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "DOB_MTA_02");
						common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
								Events_Suite_TC_Xls);
					
						common_DOB.MTAFlow(code, "MTA");
					
					}
					
					
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Endorsement Submitted"), "Verify Policy Status (Endorsement Submitted) function is having issue(S) . ");
					customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"),"Unable to Click on Assign Underwriter button . ");
					customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter.");
				
					Assert.assertTrue(common_HHAZ.funcQuoteCheck(common.Renewal_excel_data_map));
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
					
					customAssert.assertTrue(common_HHAZ.transactionSummary((String)common.MTA_excel_data_map.get("Automation Key"), "", code, event), "MTA Transaction Summary function is having issue(S) . ");
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
		
		DecimalFormat zeroVal = new DecimalFormat("0.00");
		
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
		
		String PolicyNumberMessage = "";
		
		if(TestBase.product.equalsIgnoreCase("MFB") || TestBase.product.equalsIgnoreCase("MFC")){
			PolicyNumberMessage = "Pen Reference: ";
		}else{
			PolicyNumberMessage = "Policy Number: ";
		}
		
		
		switch(fileName)
		{
		
			case "Policy Schedule":
				
				int incrementalDays = Integer.parseInt(CONFIG.getProperty("DaysIncrementNumber"));
				int policyDuration = Integer.parseInt((String)mdata.get("PS_Duration"));
				fail_count=0;
						
				if(docType.contains("Draft"))
				{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("QUOTATION SCHEDULE"), "Document : QUOTATION SCHEDULE", fileName);
				}
				else
				{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("POLICY SCHEDULE"), "Document : POLICY SCHEDULE", fileName);
				}
				if(common.currentRunningFlow.equals("Renewal"))
				{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName")), "Insured Name : "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName") , fileName);
				}
				else if(common.currentRunningFlow.equals("MTA") || common.currentRunningFlow.equals("Requote") || common.currentRunningFlow.equals("Rewind"))
				{
					if(TestBase.businessEvent.equals("Renewal"))
					{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.Renewal_excel_data_map.get("Renewal_ClientName")), "Insured Name : "+(String)common.Renewal_excel_data_map.get("Renewal_ClientName") , fileName);
					}
					else
					{
						if(common.currentRunningFlow.equalsIgnoreCase("MTA"))
						{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.NB_excel_data_map.get("NB_ClientName")), "Insured Name : "+(String)common.NB_excel_data_map.get("NB_ClientName") , fileName);
						}
						else if(common.currentRunningFlow.equalsIgnoreCase("Rewind"))
						{
							if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes"))
							{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.Rewind_excel_data_map.get("Rewind_ClientName")), "Insured Name : "+(String)common.Rewind_excel_data_map.get("Rewind_ClientName") , fileName);
							}
							else
							{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.NB_excel_data_map.get("NB_ClientName")), "Insured Name : "+(String)common.NB_excel_data_map.get("NB_ClientName") , fileName);
							}
						}						
					}					
				}
				else
				{
				
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)mdata.get("NB_ClientName")), "Insured Name : "+(String)mdata.get("NB_ClientName") , fileName);
				}
				
				if(!common.currentRunningFlow.equals("Renewal"))
				{
					if(common.currentRunningFlow.equals("Rewind") && TestBase.businessEvent.equalsIgnoreCase("Renewal"))
					{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.Renewal_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.Renewal_excel_data_map.get("QC_AgencyName") , fileName);
					}
					else if(common.currentRunningFlow.equals("MTA") && TestBase.businessEvent.equalsIgnoreCase("Renewal"))
					{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.Renewal_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.Renewal_excel_data_map.get("QC_AgencyName") , fileName);
					}
					else
					{
						if(common.currentRunningFlow.equalsIgnoreCase("MTA"))
						{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.NB_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.NB_excel_data_map.get("QC_AgencyName") , fileName);
						}
						else if(common.currentRunningFlow.equalsIgnoreCase("Rewind"))
						{
							if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")) 
							{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.Rewind_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.Rewind_excel_data_map.get("QC_AgencyName") , fileName);
							}
							else
							{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.NB_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.NB_excel_data_map.get("QC_AgencyName") , fileName);
							}
						}
					}
					
				}
				else
				{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.Renewal_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.Renewal_excel_data_map.get("QC_AgencyName") , fileName);
				}
				
				if(docType.contains("Draft"))
				{
					if(!common.currentRunningFlow.equalsIgnoreCase("Renewal"))
					{
						if(common.currentRunningFlow.equalsIgnoreCase("MTA") && TestBase.businessEvent.equalsIgnoreCase("Renewal"))
						{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.Renewal_excel_data_map.get("Renewal_QuoteNumber")) ,"Quote Reference : "+common.Renewal_excel_data_map.get("Renewal_QuoteNumber"),fileName);
							
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quotation Date: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("QuoteDate"), -incrementalDays)) ,"Quote Date : "+common.daysIncrement((String)common.Renewal_excel_data_map.get("QuoteDate"), -incrementalDays),fileName);
						}
						else if(common.currentRunningFlow.equalsIgnoreCase("MTA") && TestBase.businessEvent.equalsIgnoreCase("MTA"))
						{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.NB_excel_data_map.get("NB_QuoteNumber")) ,"Quote Reference : "+common.NB_excel_data_map.get("NB_QuoteNumber"),fileName);
							
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quotation Date: "+common.daysIncrement((String)common.MTA_excel_data_map.get("QuoteDate"), -incrementalDays)) ,"Quote Date : "+common.daysIncrement((String)common.MTA_excel_data_map.get("QuoteDate"), -incrementalDays),fileName);
						}
						else
						{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.NB_excel_data_map.get("NB_QuoteNumber")) ,"Quote Reference : "+common.NB_excel_data_map.get("NB_QuoteNumber"),fileName);
							
							if(!TestBase.product.contains("DOB"))
							{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quotation Date: "+common.daysIncrement((String)common.NB_excel_data_map.get("QuoteDate"), -incrementalDays)) ,"Quote Date : "+common.daysIncrement((String)common.NB_excel_data_map.get("QuoteDate"), -incrementalDays),fileName);
							}
						}
						
					}
					else
					{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_QuoteNumber")) ,"Quote Reference : "+common.Renewal_excel_data_map.get(common.currentRunningFlow+"_QuoteNumber"),fileName);
					}
				}
				else
				{
					if(!common.currentRunningFlow.equalsIgnoreCase("Renewal") && !common.currentRunningFlow.equalsIgnoreCase("Requote"))
					{
						if(common.currentRunningFlow.equals("Rewind") && TestBase.businessEvent.equalsIgnoreCase("Renewal"))
						{
							if(TestBase.product.equals("MFB") || TestBase.product.equals("MFC"))
							{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Pen Reference: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Pen Reference:  "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
							}
							else
							{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Policy Number : "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
							}
						}
						else if(common.currentRunningFlow.equals("MTA") && TestBase.businessEvent.equalsIgnoreCase("Renewal"))
						{
							if(TestBase.product.equals("MFB") || TestBase.product.equals("MFC"))
							{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Pen Reference: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Pen Reference:  "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
							}
							else
							{	
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Policy Number : "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
							}
						}
						else if(common.currentRunningFlow.equalsIgnoreCase("Rewind"))
						{
							if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")) 
							{
								if(TestBase.businessEvent.equalsIgnoreCase("Rewind"))
								{
									if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement"))
									{
										fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(PolicyNumberMessage+(String)common.MTA_excel_data_map.get("MTA_PolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("MTA_PolicyNumber"),fileName);
									}
									else if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("New Business")){
										fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(PolicyNumberMessage+(String)common.NB_excel_data_map.get("NB_PolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("NB_PolicyNumber"),fileName);
									}
								}
								else
								{
									fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(PolicyNumberMessage+(String)common.MTA_excel_data_map.get("MTA_PolicyNumber")) ,"Policy Number : "+common.MTA_excel_data_map.get("MTA_PolicyNumber"),fileName);
								}
							}
							else
							{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(PolicyNumberMessage+(String)common.NB_excel_data_map.get("NB_PolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("NB_PolicyNumber"),fileName);
							}
						}
						else
						{
							if(TestBase.product.equals("MFB") || TestBase.product.equals("MFC"))
							{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Pen Reference: "+(String)common.NB_excel_data_map.get("NB_PolicyNumber")) ,"Pen Reference:  "+common.NB_excel_data_map.get("NB_PolicyNumber"),fileName);
							}
							else
							{
								if(!common_CCD.isEndorsementDone)
								{
									fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.NB_excel_data_map.get("NB_PolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("NB_PolicyNumber"),fileName);
								}
								else
								{
									fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.MTA_excel_data_map.get("MTA_PolicyNumber")) ,"Policy Number : "+common.MTA_excel_data_map.get("MTA_PolicyNumber"),fileName);
								}
							}							
						}
												
					}else{
						//fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)mdata.get(common.currentRunningFlow+"_PolicyNumber")) ,"Policy Number : "+mdata.get(common.currentRunningFlow+"_PolicyNumber"),fileName);
						if(TestBase.product.equals("MFB") || TestBase.product.equals("MFC")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Pen Reference: "+(String)mdata.get("Renewal_PolicyNumber")) ,"Pen Reference:  "+mdata.get("Renewal_PolicyNumber"),fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)mdata.get("Renewal_PolicyNumber")) ,"Policy Number : "+mdata.get("Renewal_PolicyNumber"),fileName);
						}
					}
					if(((String)mdata.get("PS_DefaultStartEndDate")).equals("No"))
					{
						if(common.currentRunningFlow.equals("MTA"))
						{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 0)) ,"Effective Date : "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"),0),fileName);
						}
						else if(common.currentRunningFlow.equals("Requote"))
						{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.NB_excel_data_map.get("PS_PolicyStartDate"), 0)) ,"Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);								
						}
						else if(common.currentRunningFlow.equals("Rewind"))
						{
							if(TestBase.businessEvent.equalsIgnoreCase("Renewal"))
							{
								String temp = (String)common.Rewind_excel_data_map.get("PS_PolicyStartDate");
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), 0)) ,"Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);
							}
							else
							{
								String temp = (String)common.NB_excel_data_map.get("PS_PolicyStartDate");
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.NB_excel_data_map.get("PS_PolicyStartDate"), 0)) ,"Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);
							}
						}
						else
						{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0)) ,"Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);							
						}
					}else{
						if(common.currentRunningFlow.equals("MTA")){
							if(!TestBase.businessEvent.equalsIgnoreCase("Renewal")){
								if(common.currentRunningFlow.equalsIgnoreCase("MTA") && TestBase.businessEvent.equalsIgnoreCase("MTA")){
									fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.NB_excel_data_map.get("PS_PolicyStartDate"), 0)) ,"Period of Insurance: From: "+common.daysIncrement((String)common.NB_excel_data_map.get("PS_PolicyStartDate"), 0),fileName);
								}else{
									fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0)) ,"Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);
								}
							}
						}else if(common.currentRunningFlow.equals("Requote")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), 0)) ,"Effective Date: "+common.daysIncrement((String)mdata.get("QC_InceptionDate"), 0),fileName);
							
						}else if(common.currentRunningFlow.equals("Rewind")){
							if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), 0)) ,"Effective Date: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), 0),fileName);
							}else if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 0)) ,"Effective Date: "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 0),fileName);
							}else if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
								if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement")) {
									fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 0)) ,"Effective Date: "+common.daysIncrement((String)common.MTA_excel_data_map.get("MTA_EffectiveDate"), 0),fileName);
								}else if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("New Business")) {
									fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), 0)) ,"Effective Date: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), 0),fileName);
								}else{
									fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Effective Date: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), 0)) ,"Effective Date: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), 0),fileName);
								}
								
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
					if(!common.currentRunningFlow.equals("MTA") && !common.currentRunningFlow.equals("Rewind")){
						if(((String)mdata.get("PS_DefaultStartEndDate")).equals("No")){
							if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), 0)), "Period of Insurance: From: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), 0) , fileName);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), policyDuration-1)), "To: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), policyDuration-1) , fileName);
							}else if(TestBase.businessEvent.equalsIgnoreCase("Rewind") && common.currentRunningFlow.equalsIgnoreCase("Rewind")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), 0)), "Period of Insurance: From: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), 0) , fileName);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), policyDuration-1)), "To: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), policyDuration-1) , fileName);
							}else if(TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind") && ((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy")).equalsIgnoreCase("Yes")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), 0)), "Period of Insurance: From: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), 0) , fileName);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), policyDuration-1)), "To: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), policyDuration-1) , fileName);
							}else{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), 0)), "Period of Insurance: From: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), 0) , fileName);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), policyDuration-1)), "To: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), policyDuration-1) , fileName);
							}
						}else{
							if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), 0)), "Period of Insurance: From: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), 0) , fileName);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), policyDuration-1)), "To: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), policyDuration-1) , fileName);
							}else if(TestBase.businessEvent.equalsIgnoreCase("Rewind") && common.currentRunningFlow.equalsIgnoreCase("Rewind")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), 0)), "Period of Insurance: From: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), 0) , fileName);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), policyDuration-1)), "To: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), policyDuration-1) , fileName);
							}else if(TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind") && ((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy")).equalsIgnoreCase("Yes")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), 0)), "Period of Insurance: From: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), 0) , fileName);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), policyDuration-1)), "To: "+common.daysIncrement((String)common.Rewind_excel_data_map.get("PS_PolicyStartDate"), policyDuration-1) , fileName);
							}else{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), 0)), "Period of Insurance: From: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), 0) , fileName);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), policyDuration-1)), "To: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), policyDuration-1) , fileName);
							}
							
						}
					}
				}
				
			switch(TestBase.product){	
			
			case "DOB":
				
				//check D&O cover
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Directors and Officers Insured"), "Directors and Officers Insured" , fileName);
				
				//cover extension
				if(((String)mdata.get("DO_CivilFines")).equals("Yes"))
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Civil Fines and Penalties Insured"), "Civil Fines and Penalties Insured" , fileName);
				else
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Civil Fines and Penalties Not Insured"), "Civil Fines and Penalties Not Insured" , fileName);
				
				if(((String)mdata.get("DO_EntityInsurance")).equals("Yes"))
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Entity Insurance Insured"), "Entity Insurance Insured" , fileName);
				else
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Entity Insurance Not Insured"), "Entity Insurance Not Insured" , fileName);
				
				//indemnity limit
				String indemnityLimit = (String)mdata.get("DO_IndemnityLimit");
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Limit of indemnity: £"+indemnityLimit), "Limit of indemnity: £"+indemnityLimit, fileName);
				
				//pollution defense
				String pollutionDefenceFormat = common.getWholeNumber(Double.parseDouble((String)mdata.get("DO_PollutionDefenceCost")));
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Sub-limit of indemnity for pollution"),"Sub-limit of indemnity for pollution", fileName);
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("defence costs:"),"defence costs:", fileName);
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(pollutionDefenceFormat),""+pollutionDefenceFormat, fileName);
				
				//automatic acquisition size
				String autoAcquisitionFormat = common.getWholeNumber(Double.parseDouble((String)mdata.get("DO_AutoAcquisitionSize")));
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Automatic acquisition size (clause 8.20.2) £"+autoAcquisitionFormat), "Automatic acquisition size (clause 8.20.2) £"+autoAcquisitionFormat, fileName);
				
				//Turnover
				String turnoverFormat = common.getWholeNumber(Double.parseDouble((String)mdata.get("PG_TurnOver")));
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Rated on total turnover of: £"+turnoverFormat), "Rated on total turnover of: £"+turnoverFormat , fileName);
				break;
			case "CCD":
			
				// Cover Sections and Premium :
				
				if(((String)mdata.get("CD_MaterialDamage")).equals("Yes")){
					
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Property Insured"), "Property Insured" , fileName);			
					int count1 = 0;
					int noOfProperties = 0;
					double totalSumInsured =0.00;
					
					String[] properties = ((String)mdata.get("IP_AddProperty")).split(";");
		            noOfProperties = properties.length;
		            while(count1 < noOfProperties ){
		            	 String SunInsuredB = ((String)Map_InnerPagesMaps.get("Property Details").get(count1).get("AddBuilding_Suminsured"));
		            	 String FormatedSunInsuredB = common.getWholeNumber((Double.parseDouble(SunInsuredB)));				        
				         String DeclaredValueC = ((String)Map_InnerPagesMaps.get("Property Details").get(count1).get("AddSPContent_DeclaredValue"));
				         fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Buildings GBP "+FormatedSunInsuredB), "Buildings GBP "+FormatedSunInsuredB , fileName);
				         totalSumInsured = totalSumInsured + Double.parseDouble(SunInsuredB);
//				         
//				         fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(CoverBasicsC+" Operative:"), CoverBasicsC+" Operative:" , fileName);
//				         fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Declared Value GBP "+DeclaredValueC), "Declared Value GBP "+DeclaredValueC , fileName);
				         count1++;  		
		            }
		            fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Buildings GBP "+common.getWholeNumber(totalSumInsured)), "Buildings GBP "+common.getWholeNumber(totalSumInsured), fileName);
			         
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Property Not Insured"), "Property Not Insured" , fileName);
				}
				
				
				if(((String)mdata.get("CD_BusinessInterruption")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Business Interruption Insured"), "Business Interruption Insured" , fileName);			
					int count1 = 0;
					int noOfProperties = 0;
					
					String[] properties = ((String)mdata.get("BI_BBInterruption")).split(";");
		            noOfProperties = properties.length;
		            while(count1 < noOfProperties ){
		            	 String BICover = ((String)Map_InnerPagesMaps.get("BI-BBI").get(count1).get("BI_BBI_cover"));
		            	 if(BICover.equalsIgnoreCase("Additional increased costs of working")){BICover = "Additional increased cost of working";}
		            	 String LOIPeriod = ((String)Map_InnerPagesMaps.get("BI-BBI").get(count1).get("BI_BBI_IP"));
				         String AnnualSumInsured = ((String)Map_InnerPagesMaps.get("BI-BBI").get(count1).get("BI_BBI_SumInsured"));
				//         fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(BICover+" "+LOIPeriod+" any one occurrence GBP "+formatter.format(Double.parseDouble(AnnualSumInsured))), BICover+" "+LOIPeriod+" any one occurrence GBP "+AnnualSumInsured , fileName);
				         count1++;
		            }
			        
		  		}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Business Interruption Not Insured"), "Business Interruption Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_Money&Assault")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Money Insured"), "Money Insured" , fileName);	
					// fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Non-negotiable money any one occurrence GBP "+(String)mdata.get("MA_NonNegotiableInstrument")), "Non-negotiable money any one occurrence GBP "+(String)mdata.get("MA_NonNegotiableInstrument") , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Money Not Insured"), "Money Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_EmployersLiability")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Employers Liability Insured"), "Employers Liability Insured" , fileName);			
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Employers’ Liability cover (defence costs form part\r\nof the Limit of Indemnity)\r\nany one occurrence GBP "+common.getWholeNumber(Double.parseDouble((String)mdata.get("EL_IndemnityLimit")))), "Employers’ Liability cover (defence costs form partof the Limit of Indemnity)any one occurrence GBP"+common.getWholeNumber((String)mdata.get("EL_IndemnityLimit")), fileName);
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Employers Liability Not Insured"), "Employers Liability Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_PublicLiability")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Insured"), "Public, products and pollution liability Insured" , fileName);
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public Liability (separate limit of indemnity) Any one occurrence GBP "+common.getWholeNumber(Double.parseDouble((String)mdata.get("PL_IndemnityLimit")))), "Public Liability (separate limit of indemnity) Any one occurrence GBP "+common.getWholeNumber((String)mdata.get("PL_IndemnityLimit")), fileName);
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Not Insured"), "Public, products and pollution liability Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_ProductsLiability")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Insured"), "Public, products and pollution liability Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Not Insured"), "Public, products and pollution liability Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_PollutionLiability(suddenandunforeseen)")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Insured"), "Public, products and pollution liability Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Not Insured"), "Public, products and pollution liability Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_PersonalAccidentStandard")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Personal Accident Insured"), "Personal Accident Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Personal Accident Not Insured"), "Personal Accident Not Insured" , fileName);			
					
				}
				if(((String)mdata.get("CD_GoodsInTransit")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Goods In Transit Insured"), "Goods In Transit Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Goods In Transit Not Insured"), "Goods In Transit Not Insured" , fileName);			
					
				}
				if(((String)mdata.get("CD_LegalExpenses")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Legal Expenses Insured"), "Legal Expenses Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Legal Expenses Not Insured"), "Legal Expenses Not Insured" , fileName);			
					
				}
				if(((String)mdata.get("CD_Terrorism")).equals("Yes")){					
					
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Terrorism Insured"), "Terrorism Insured" , fileName);
					
				}	
			
				break;
				
			case "CCC":
				
				// Cover Sections and Premium :
				
				if(((String)mdata.get("CD_MaterialDamage")).equals("Yes")){
					
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Property Insured"), "Property Insured" , fileName);			
					int count1 = 0;
					int noOfProperties = 0;
					double totalSumInsured =0.00;
					
					String[] properties = ((String)mdata.get("IP_AddProperty")).split(";");
		            noOfProperties = properties.length;
		            while(count1 < noOfProperties )
		            {
		            	 String SunInsuredB = ((String)Map_InnerPagesMaps.get("Property Details").get(count1).get("AddBuilding_Suminsured"));
		            	 String FormatedSunInsuredB = common.getWholeNumber(Double.parseDouble(SunInsuredB));				        
				         String DeclaredValueC = ((String)Map_InnerPagesMaps.get("Property Details").get(count1).get("AddSPContent_DeclaredValue"));
				         fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Buildings GBP "+FormatedSunInsuredB), "Buildings GBP "+FormatedSunInsuredB , fileName);
				         totalSumInsured = totalSumInsured + Double.parseDouble(SunInsuredB);
//				         
//				         fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(CoverBasicsC+" Operative:"), CoverBasicsC+" Operative:" , fileName);
//				         fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Declared Value GBP "+DeclaredValueC), "Declared Value GBP "+DeclaredValueC , fileName);
				         count1++;  		
		            }
		            fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Buildings GBP "+common.getWholeNumber(totalSumInsured)), "Buildings GBP "+common.getWholeNumber(totalSumInsured), fileName);
			         
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Property Not Insured"), "Property Not Insured" , fileName);
				}
				
				
				if(((String)mdata.get("CD_BusinessInterruption")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Business Interruption Insured"), "Business Interruption Insured" , fileName);			
					int count1 = 0;
					int noOfProperties = 0;
					
					String[] properties = ((String)mdata.get("BI_BBInterruption")).split(";");
		            noOfProperties = properties.length;
		            while(count1 < noOfProperties ){
		            	 String BICover = ((String)Map_InnerPagesMaps.get("BI-BBI").get(count1).get("BI_BBI_cover"));
		            	 if(BICover.equalsIgnoreCase("Additional increased costs of working")){BICover = "Additional increased cost of working";}
		            	 String LOIPeriod = ((String)Map_InnerPagesMaps.get("BI-BBI").get(count1).get("BI_BBI_IP"));
				         String AnnualSumInsured = ((String)Map_InnerPagesMaps.get("BI-BBI").get(count1).get("BI_BBI_SumInsured"));
				//         fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(BICover+" "+LOIPeriod+" any one occurrence GBP "+formatter.format(Double.parseDouble(AnnualSumInsured))), BICover+" "+LOIPeriod+" any one occurrence GBP "+AnnualSumInsured , fileName);
				         count1++;
		            }
   
		  		}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Business Interruption Not Insured"), "Business Interruption Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_Money&Assault")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Money Insured"), "Money Insured" , fileName);	
					// fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Non-negotiable money any one occurrence GBP "+(String)mdata.get("MA_NonNegotiableInstrument")), "Non-negotiable money any one occurrence GBP "+(String)mdata.get("MA_NonNegotiableInstrument") , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Money Not Insured"), "Money Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_EmployersLiability")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Employers Liability Insured"), "Employers Liability Insured" , fileName);			
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Employers’ Liability cover (defence costs form part\r\nof the Limit of Indemnity)\r\nany one occurrence GBP "+common.getWholeNumber(Double.parseDouble((String)mdata.get("EL_IndemnityLimit")))), "Employers’ Liability cover (defence costs form partof the Limit of Indemnity)any one occurrence GBP"+common.getWholeNumber((String)mdata.get("EL_IndemnityLimit")), fileName);
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Employers Liability Not Insured"), "Employers Liability Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_PublicLiability")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Insured"), "Public, products and pollution liability Insured" , fileName);
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public Liability (separate limit of indemnity) Any one occurrence GBP "+common.getWholeNumber(Double.parseDouble((String)mdata.get("PL_IndemnityLimit")))), "Public Liability (separate limit of indemnity) Any one occurrence GBP "+common.getWholeNumber((String)mdata.get("PL_IndemnityLimit")), fileName);
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Not Insured"), "Public, products and pollution liability Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_ProductsLiability")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Insured"), "Public, products and pollution liability Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Not Insured"), "Public, products and pollution liability Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_PollutionLiability(suddenandunforeseen)")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Insured"), "Public, products and pollution liability Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Not Insured"), "Public, products and pollution liability Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_PersonalAccidentStandard")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Personal Accident Insured"), "Personal Accident Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Personal Accident Not Insured"), "Personal Accident Not Insured" , fileName);			
					
				}
				if(((String)mdata.get("CD_GoodsInTransit")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Goods In Transit Insured"), "Goods In Transit Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Goods In Transit Not Insured"), "Goods In Transit Not Insured" , fileName);			
					
				}
				if(((String)mdata.get("CD_LegalExpenses")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Legal Expenses Insured"), "Legal Expenses Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Legal Expenses Not Insured"), "Legal Expenses Not Insured" , fileName);			
					
				}
				if(((String)mdata.get("CD_Terrorism")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Terrorism Insured"), "Terrorism Insured" , fileName);			
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Terrorism Premium £"+common.getWholeNumber(TerPremDocAct)), "Terrorism Premium £"+common.getWholeNumber(TerPremDocAct) , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Terrorism Not Insured"), "Terrorism Not Insured" , fileName);			
					
				}
				
				break;
				
			case "CCJ":
				// Cover Sections and Premium :
				
				if(((String)mdata.get("CD_MaterialDamage")).equals("Yes")){
					
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Property Insured"), "Property Insured" , fileName);			
					int count1 = 0;
					int noOfProperties = 0;
					double totalSumInsured =0.00;
					
					String[] properties = ((String)mdata.get("IP_AddProperty")).split(";");
		            noOfProperties = properties.length;
		            while(count1 < noOfProperties ){
		            	 String SunInsuredB = ((String)Map_InnerPagesMaps.get("Property Details").get(count1).get("AddBuilding_Suminsured"));
		            	 String FormatedSunInsuredB = common.getWholeNumber(Double.parseDouble(SunInsuredB));				        
				         String DeclaredValueC = ((String)Map_InnerPagesMaps.get("Property Details").get(count1).get("AddSPContent_DeclaredValue"));
				       //  fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Buildings EUR "+FormatedSunInsuredB), "Buildings GBP "+FormatedSunInsuredB , fileName);
				         totalSumInsured = totalSumInsured + Double.parseDouble(SunInsuredB);
//				         
				         count1++;  		
		            }
		            //fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Buildings EUR "+formatter.format(totalSumInsured)), "Buildings GBP "+totalSumInsured , fileName);
			         
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Property Not Insured"), "Property Not Insured" , fileName);
				}
				
				
				if(((String)mdata.get("CD_BusinessInterruption")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Business Interruption Insured"), "Business Interruption Insured" , fileName);			
					int count1 = 0;
					int noOfProperties = 0;
					
					String[] properties = ((String)mdata.get("BI_BBInterruption")).split(";");
		            noOfProperties = properties.length;
		            while(count1 < noOfProperties ){
		            	 String BICover = ((String)Map_InnerPagesMaps.get("BI-BBI").get(count1).get("BI_BBI_cover"));
		            	 if(BICover.equalsIgnoreCase("Additional increased costs of working")){BICover = "Additional increased cost of working";}
		            	 String LOIPeriod = ((String)Map_InnerPagesMaps.get("BI-BBI").get(count1).get("BI_BBI_IP"));
				         String AnnualSumInsured = ((String)Map_InnerPagesMaps.get("BI-BBI").get(count1).get("BI_BBI_SumInsured"));
			
				         count1++;
		            }
		        
		  		}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Business Interruption Not Insured"), "Business Interruption Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_Money&Assault")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Money Insured"), "Money Insured" , fileName);	
					// fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Non-negotiable money any one occurrence GBP "+(String)mdata.get("MA_NonNegotiableInstrument")), "Non-negotiable money any one occurrence GBP "+(String)mdata.get("MA_NonNegotiableInstrument") , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Money Not Insured"), "Money Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_EmployersLiability")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Employers Liability Insured"), "Employers Liability Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Employers Liability Not Insured"), "Employers Liability Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_PublicLiability")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Insured"), "Public, products and pollution liability Insured" , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Not Insured"), "Public, products and pollution liability Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_ProductsLiability")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Insured"), "Public, products and pollution liability Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Not Insured"), "Public, products and pollution liability Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_PollutionLiability(suddenandunforeseen)")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Insured"), "Public, products and pollution liability Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public, products and pollution liability Not Insured"), "Public, products and pollution liability Not Insured" , fileName);			
					
				}

				if(((String)mdata.get("CD_PersonalAccidentStandard")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Personal Accident Insured"), "Personal Accident Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Personal Accident Not Insured"), "Personal Accident Not Insured" , fileName);			
					
				}
				if(((String)mdata.get("CD_GoodsInTransit")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Goods In Transit Insured"), "Goods In Transit Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Goods In Transit Not Insured"), "Goods In Transit Not Insured" , fileName);			
					
				}
				if(((String)mdata.get("CD_LegalExpenses")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Legal Expenses Insured"), "Legal Expenses Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Legal Expenses Not Insured"), "Legal Expenses Not Insured" , fileName);			
					
				}
				if(((String)mdata.get("CD_Terrorism")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Terrorism Insured"), "Terrorism Insured" , fileName);			
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Terrorism Premium £"+common.getWholeNumber(TerPremDocAct)), "Terrorism Premium £"+common.getWholeNumber(TerPremDocAct) , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Terrorism Not Insured"), "Terrorism Not Insured" , fileName);			
					TerPremDocAct = 0.0;
				}
				
				break;
			case "OED":
				if(((String)mdata.get("CD_EmployersLiability")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(" Employers Liability Insured"), " Employers Liability Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(" Employers Liability Not Insured"), " Employers Liability Not Insured" , fileName);			
					
				}
				if(((String)mdata.get("CD_PublicLiability")).equals("Yes")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(" Public / Products Liability Insured"), "Public / Products Liability Insured" , fileName);			
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Pollution Liability Insured"), "Pollution Liability Insured" , fileName);			
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Public / Products Liability Not Insured"), "Public / Products Liability Not Insured" , fileName);			
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Pollution Liability Not Insured"), "Pollution Liability Not Insured" , fileName);			
					
				}
				break;
			case "CMA" :
				if(((String)mdata.get("CMA_SumInsured")).equals("0")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Computer Not Insured"), "Computer Not Insured" , fileName);		
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Additional Expenditure Not Insured"), "Additional Expenditure Not Insured" , fileName);
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Computer Insured"), "Computer Insured" , fileName);	
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Additional Expenditure Insured"), "Additional Expenditure Insured" , fileName);		
				}
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(" Electronic Equipment Not Insured"), " Electronic Equipment Not Insured" , fileName);
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Business Interruption Not Insured"), "Business Interruption Not Insured" , fileName);
			
				if(((String)mdata.get("CMA_Cover_Basis")).equals("Full Theft")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("All Risks Terrorism Insurance Not Insured"), "All Risks Terrorism Insurance Not Insured" , fileName);		
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("All Risks Terrorism Insurance Insured"), "All Risks Terrorism Insurance Insured" , fileName);
				}
				break;
			}	
				// Verify Premium :
			if(TestBase.product.equalsIgnoreCase("CCD") || TestBase.product.equalsIgnoreCase("CCC") ||TestBase.product.equalsIgnoreCase("MFC") || TestBase.product.equalsIgnoreCase("DOB")||TestBase.product.equals("PAA")){
				
					if(common.currentRunningFlow.contains("NB") || common.currentRunningFlow.contains("Renewal") || common.currentRunningFlow.contains("Requote") || ((common.currentRunningFlow.contains("Rewind") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("No")) && !TestBase.businessEvent.equalsIgnoreCase("MTA")) ||
							((common.currentRunningFlow.contains("Rewind") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("New Business") && TestBase.businessEvent.equalsIgnoreCase("Rewind"))))
						{
						String smsg = "Premium excluding Terrorism £";
						if(TestBase.product.equals("MFC")||TestBase.product.equals("CMA")||TestBase.product.equals("DOB")||TestBase.product.equals("PAA")){
							smsg = "Premium £";
							
						}
						String PremiumExcTerr = common.getWholeNumber(PremiumExcTerrDocAct);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(smsg+PremiumExcTerr), smsg+PremiumExcTerr , fileName);
						
						String insTax = common.getWholeNumber(InsTaxDocAct);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+insTax), "Insurance Premium Tax £"+insTax, fileName);
						
						TotalPremiumWithAdminDocAct = PremiumExcTerrDocAct + TerPremDocAct + InsTaxDocAct;
						String total = common.getWholeNumber(TotalPremiumWithAdminDocAct);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+total), "TOTAL £"+total, fileName);
						
					}else if(common.currentRunningFlow.contains("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind")) || ((common.currentRunningFlow.contains("Rewind") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")))){
						
						double g_premium = common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium");
						String IPTmMsg = "Insurance Premium Tax £";
						
						if(TestBase.product.equals("MFC")){
						
													 	
							 double PAO_GP=0.00;
							 double PAO_TP=0.00;
							 double PAO_IT=0.00;
							 double PAO_Total_Flat=0.00;
							 if(common.transaction_Details_Premium_Values.get("Personal Accident Optional")!=null){
								 PAO_GP =  common.transaction_Details_Premium_Values.get("Personal Accident Optional").get("Gross Premium");
								 PAO_TP= common.transaction_Details_Premium_Values.get("Personal Accident Optional").get("Total Premium");
								 PAO_IT= common.transaction_Details_Premium_Values.get("Personal Accident Optional").get("Insurance Tax");
								 
							 }
							 if(common.transaction_Details_Premium_Values.get("Personal Accident Optional_FP")!=null){
								 PAO_GP =  PAO_GP + common.transaction_Details_Premium_Values.get("Personal Accident Optional_FP").get("Gross Premium");
								 PAO_TP= PAO_TP + common.transaction_Details_Premium_Values.get("Personal Accident Optional_FP").get("Total Premium");
								 PAO_IT= PAO_IT + common.transaction_Details_Premium_Values.get("Personal Accident Optional_FP").get("Insurance Tax");
								 PAO_Total_Flat = common.transaction_Details_Premium_Values.get("Personal Accident Optional_FP").get("Total Premium");
							 }
							 
							 	g_premium = g_premium -PAO_GP;
							 	int f_g_premium = Integer.parseInt(String.valueOf(g_premium).split("\\.")[0]);
								
							 	if(g_premium < 0){
							 		f_g_premium = Math.abs(f_g_premium);
									fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Return Premium £"+common.getWholeNumber((int) Math.floor(f_g_premium))), "Return Premium £"+common.getWholeNumber((int) Math.floor(f_g_premium)) , fileName);
								}else{
							    
							 		fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Additional Premium £"+common.getWholeNumber((int) Math.floor(f_g_premium))), "Premium €"+common.getWholeNumber((int) Math.floor(f_g_premium)) , fileName);
								}
								
								double tot_premium = common.transaction_Details_Premium_Values.get("Totals").get("Total Premium")-PAO_TP;
								tot_premium = Math.abs(tot_premium);
								
								double iTax_ = common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax") - PAO_IT;
								iTax_ = Math.abs(iTax_);
								
								int f_total_premium = Integer.parseInt(String.valueOf(common_HHAZ.MFC_total_premium-(common_HHAZ.PAO_total_premium+PAO_Total_Flat)).split("\\.")[0]);
								f_total_premium = Math.abs(f_total_premium);
								
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(IPTmMsg+common.getWholeNumber((int) Math.floor(iTax_))), "Insurance Premium Tax £"+common.getWholeNumber((int) Math.floor(iTax_)) , fileName);
								//fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+formatter.format((int) Math.floor(tot_premium))), "TOTAL £"+formatter.format((int) Math.floor(tot_premium)) , fileName);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+common.getWholeNumber(f_total_premium)), "TOTAL £"+common.getWholeNumber(f_total_premium) , fileName);
								
							 
						}else{
						
							
							if(g_premium < 0){
								g_premium = Math.abs(g_premium);
								
								double Ter_g_premium =  0.00;
								
								if(TestBase.product.contains("CCD")){
									if(common.transaction_Details_Premium_Values.get("Terrorism")!=null){
										Ter_g_premium = common.transaction_Details_Premium_Values.get("Terrorism").get("Gross Premium");
									}							
								}
								
								g_premium = g_premium - Math.abs(Ter_g_premium);
								
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Return Premium £"+common.getWholeNumber((int) Math.floor(g_premium))), "Return Premium £"+common.getWholeNumber((int) Math.floor(g_premium)) , fileName);								
							}else{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Additional Premium £"+common.getWholeNumber((int) Math.floor(common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium")))), "Additional Premium £"+common.getWholeNumber((int) Math.floor(common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium"))) , fileName);
								
							}
							double tot_premium = common.transaction_Details_Premium_Values.get("Totals").get("Total Premium");
							tot_premium = Math.abs(tot_premium);
							
							double iTax_ = common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax");
							iTax_ = Math.abs(iTax_);
							
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(IPTmMsg+common.getWholeNumber((int) Math.floor(iTax_))), "Insurance Premium Tax £"+common.getWholeNumber((int) Math.floor(iTax_)) , fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+common.getWholeNumber((int) Math.floor(tot_premium))), "TOTAL £"+common.getWholeNumber((int) Math.floor(tot_premium)) , fileName);
						}	
					}
			}else if(TestBase.product.equalsIgnoreCase("CCJ")||TestBase.product.equalsIgnoreCase("MFB")||TestBase.product.equalsIgnoreCase("PAB")){
				if(common.currentRunningFlow.contains("NB") || common.currentRunningFlow.contains("Renewal") || common.currentRunningFlow.contains("Requote") || (common.currentRunningFlow.contains("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA"))){
					try {
						if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes") && 
								((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement")) {
							
							double PAO_GP=0.00;
							double PAO_TP=0.00;
							double PAO_IT=0.00;
						
							if(common.transaction_Details_Premium_Values.get("Personal Accident Optional")!=null){
								PAO_GP =  common.transaction_Details_Premium_Values.get("Personal Accident Optional").get("Gross Premium");
								PAO_TP= common.transaction_Details_Premium_Values.get("Personal Accident Optional").get("Total Premium");
								PAO_IT= common.transaction_Details_Premium_Values.get("Personal Accident Optional").get("Insurance Tax");
							 
							}
							double g_premium = common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium");
						
							g_premium = g_premium -PAO_GP;
						
							if(g_premium < 0){
								g_premium = Math.abs(g_premium);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Return Premium €"+common.getWholeNumber((int) Math.floor(g_premium))), "Return Premium €"+common.getWholeNumber((int) Math.floor(g_premium)) , fileName);
							
							}else{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Additional Premium €"+common.getWholeNumber((int) Math.floor(common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium")))), "Additional Premium €"+common.getWholeNumber((int) Math.floor(common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium"))) , fileName);
							
							}
							double iTax_ = common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax");
							iTax_ = Math.abs(iTax_);
						
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Levy €"+common.getWholeNumber((int) Math.floor(iTax_))), "Levy €"+common.getWholeNumber((int) Math.floor(iTax_)) , fileName);
						
							double tot_premium = common.transaction_Details_Premium_Values.get("Totals").get("Total Premium")-PAO_TP;
							tot_premium = Math.abs(tot_premium);
						
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL €"+common.getWholeNumber((int) Math.floor(tot_premium))), "TOTAL €"+common.getWholeNumber((int) Math.floor(tot_premium)) , fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium €"+common.getWholeNumber((int) Math.floor(PremiumExcTerrDocAct))), "Premium €"+common.getWholeNumber((int) Math.floor(PremiumExcTerrDocAct)) , fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Levy €"+common.getWholeNumber((int) Math.floor(InsTaxDocAct))), "Levy €"+common.getWholeNumber((int) Math.floor(InsTaxDocAct)) , fileName);
							TotalPremiumWithAdminDocAct = PremiumExcTerrDocAct + TerPremDocAct + InsTaxDocAct;
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL €"+common.getWholeNumber((int) Math.floor(TotalPremiumWithAdminDocAct))), "TOTAL €"+common.getWholeNumber((int) Math.floor(TotalPremiumWithAdminDocAct)) , fileName);
						}
					}catch(NullPointerException npe) {
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium €"+common.getWholeNumber((int) Math.floor(PremiumExcTerrDocAct))), "Premium €"+common.getWholeNumber((int) Math.floor(PremiumExcTerrDocAct)) , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Levy €"+common.getWholeNumber((int) Math.floor(InsTaxDocAct))), "Levy €"+common.getWholeNumber((int) Math.floor(InsTaxDocAct)) , fileName);
						TotalPremiumWithAdminDocAct = PremiumExcTerrDocAct + TerPremDocAct + InsTaxDocAct;
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL €"+common.getWholeNumber((int) Math.floor(TotalPremiumWithAdminDocAct))), "TOTAL €"+common.getWholeNumber((int) Math.floor(TotalPremiumWithAdminDocAct)) , fileName);
						
					}	
					
				}else if(common.currentRunningFlow.contains("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
					
					double g_premium = common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium");
					String IPTmMsg = "Levy €";
					
					if(TestBase.product.equals("MFB") ){
						 IPTmMsg = "Government Levy €";
						 	
						 double PAO_GP=0.00;
						 double PAO_TP=0.00,PAO_Total_P=0.00;
						 double PAO_IT=0.00;
						 double PAO_Total_Flat=0.00;
						 if(common.transaction_Details_Premium_Values.get("Personal Accident Optional")!=null){
							 PAO_GP =  common.transaction_Details_Premium_Values.get("Personal Accident Optional").get("Gross Premium");
							 PAO_TP= common.transaction_Details_Premium_Values.get("Personal Accident Optional").get("Total Premium");
							 PAO_IT= common.transaction_Details_Premium_Values.get("Personal Accident Optional").get("Insurance Tax");
							 PAO_Total_P = common.transaction_Details_Premium_Values.get("Personal Accident Optional").get("Total Premium");
							 
						 }
						 
						 if(common.transaction_Details_Premium_Values.get("Personal Accident Optional_FP")!=null){
							 PAO_GP =  PAO_GP + common.transaction_Details_Premium_Values.get("Personal Accident Optional_FP").get("Gross Premium");
							 PAO_TP= PAO_TP + common.transaction_Details_Premium_Values.get("Personal Accident Optional_FP").get("Total Premium");
							 PAO_IT= PAO_IT + common.transaction_Details_Premium_Values.get("Personal Accident Optional_FP").get("Insurance Tax");
							 PAO_Total_Flat = common.transaction_Details_Premium_Values.get("Personal Accident Optional_FP").get("Total Premium");
						 }
						 
						 
						 
						 	g_premium = g_premium -PAO_GP;
						 	int f_g_premium = Integer.parseInt(String.valueOf(g_premium).split("\\.")[0]);
						    
						 	if(g_premium < 0){
						 		f_g_premium = Math.abs(f_g_premium);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Return Premium €"+common.getWholeNumber((int) Math.floor(f_g_premium))), "Return Premium €"+common.getWholeNumber((int) Math.floor(f_g_premium)) , fileName);
							}else{
						    
						 		fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Additional Premium €"+common.getWholeNumber((int) Math.floor(f_g_premium))), "Additional Premium €"+common.getWholeNumber((int) Math.floor(f_g_premium)) , fileName);
							}
							//}else{
								//fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Additional Premium €"+formatter.format((int) Math.floor(common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium")))), "Additional Premium €"+formatter.format((int) Math.floor(common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium"))) , fileName);
								
							//}
							double tot_premium = common.transaction_Details_Premium_Values.get("Totals").get("Total Premium")-PAO_TP;
							tot_premium = Math.abs(tot_premium);
							
							double iTax_ = common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax") - PAO_IT;
							iTax_ = Math.abs(iTax_);
							
							int f_total_premium = Integer.parseInt(String.valueOf(common.transaction_Details_Premium_Values.get("Totals").get("Total Premium")-(PAO_Total_P+PAO_Total_Flat)).split("\\.")[0]);
							f_total_premium = Math.abs(f_total_premium);
							
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(IPTmMsg+common.getWholeNumber((int) Math.floor(iTax_))), IPTmMsg+common.getWholeNumber((int) Math.floor(iTax_)) , fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL €"+common.getWholeNumber((int) Math.floor(f_total_premium))), "TOTAL €"+common.getWholeNumber((int) Math.floor(f_total_premium)) , fileName);
							 
					}else{
					
					//if(g_premium < 0){
						//g_premium = Math.abs(g_premium);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium €"+common.getWholeNumber((int) Math.floor(g_premium))), "Premium €"+common.getWholeNumber((int) Math.floor(g_premium)) , fileName);
						
					//}else{
						//fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Additional Premium €"+formatter.format((int) Math.floor(common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium")))), "Additional Premium €"+formatter.format((int) Math.floor(common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium"))) , fileName);
						
						//}
						double tot_premium = common.transaction_Details_Premium_Values.get("Totals").get("Total Premium");
						tot_premium = Math.abs(tot_premium);
						
						double iTax_ = common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax");
						iTax_ = Math.abs(iTax_);
						
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Levy €"+common.getWholeNumber((int) Math.floor(iTax_))), IPTmMsg+common.getWholeNumber((int) Math.floor(iTax_)) , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL €"+common.getWholeNumber((int) Math.floor(tot_premium))), "TOTAL €"+common.getWholeNumber((int) Math.floor(tot_premium)) , fileName);
					}
				}
			}else if(TestBase.product.equalsIgnoreCase("OED")){
				TotalPremiumWithAdminDocAct =0;
				
				
				if(common.currentRunningFlow.contains("NB") || common.currentRunningFlow.contains("Renewal") || common.currentRunningFlow.contains("Requote") || (common.currentRunningFlow.contains("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA"))){
					if(Integer.parseInt((String)mdata.get("PS_Duration"))!=365){
		//			if((String)mdata.get("PS_Duration").equals(obj)){
						double publicLiabilityPrem =0.00, publicLiabilityIPT =0.00, EmployersLiabilityPrem=0.00, EmployersLiabilityIPT= 0.00;
						try{
							publicLiabilityPrem = transaction_Premium_Values.get("Public Liability").get("Gross Premium");
							publicLiabilityIPT = transaction_Premium_Values.get("Public Liability").get("Insurance Tax");
						}catch(NullPointerException npe){
							publicLiabilityPrem = 0.0;
							publicLiabilityIPT = 0.0;
						}
						try{
							EmployersLiabilityPrem = transaction_Premium_Values.get("Employers Liability").get("Gross Premium");
							EmployersLiabilityIPT = transaction_Premium_Values.get("Employers Liability").get("Insurance Tax");
						}catch(NullPointerException npe){
							EmployersLiabilityPrem = 0.0;
							EmployersLiabilityIPT = 0.0;
						}
						double totalPremium = publicLiabilityPrem + EmployersLiabilityPrem;
						double totalIPT = publicLiabilityIPT +EmployersLiabilityIPT;
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium £"+common.getWholeNumber((int) Math.floor(totalPremium))), "Premium £"+common.getWholeNumber((int) Math.floor(totalPremium)) , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+common.getWholeNumber((int) Math.floor(totalIPT))), "Insurance Premium Tax £"+common.getWholeNumber((int) Math.floor(totalIPT)) , fileName);
						TotalPremiumWithAdminDocAct = totalPremium + totalIPT;
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+common.getWholeNumber((int) Math.floor(TotalPremiumWithAdminDocAct))), "TOTAL £"+common.getWholeNumber((int) Math.floor(TotalPremiumWithAdminDocAct)) , fileName);
					
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium £"+common.getWholeNumber((int) Math.floor(PremiumExcTerrDocActOED))), "Premium £"+common.getWholeNumber((int) Math.floor(PremiumExcTerrDocAct)) , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+common.getWholeNumber((int) Math.floor(InsTaxDocActOED))), "Insurance Premium Tax £"+common.getWholeNumber((int) Math.floor(InsTaxDocActOED)) , fileName);
						TotalPremiumWithAdminDocAct = PremiumExcTerrDocActOED + InsTaxDocActOED;
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+common.getWholeNumber((int) Math.floor(TotalPremiumWithAdminDocAct))), "TOTAL £"+common.getWholeNumber((int) Math.floor(TotalPremiumWithAdminDocAct)) , fileName);
					}
					
				}else if(common.currentRunningFlow.contains("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
				//	if(Integer.parseInt((String)mdata.get("PS_Duration"))!=365){
						//			if((String)mdata.get("PS_Duration").equals(obj)){
										double publicLiabilityPrem =0.00, publicLiabilityIPT =0.00, EmployersLiabilityPrem=0.00, EmployersLiabilityIPT= 0.00;
										try{
											publicLiabilityPrem = common.transaction_Details_Premium_Values.get("Public Liability").get("Gross Premium");
											publicLiabilityIPT = common.transaction_Details_Premium_Values.get("Public Liability").get("Insurance Tax");
										}catch(NullPointerException npe){
											try{
												publicLiabilityPrem = common.transaction_Details_Premium_Values.get("Public Liability_FP").get("Gross Premium");
												publicLiabilityIPT = common.transaction_Details_Premium_Values.get("Public Liability_FP").get("Insurance Tax");
											}catch(NullPointerException npe1){
											publicLiabilityPrem = 0.0;
											publicLiabilityIPT = 0.0;
											}
										}
										try{
											EmployersLiabilityPrem = common.transaction_Details_Premium_Values.get("Employers Liability").get("Gross Premium");
											EmployersLiabilityIPT = common.transaction_Details_Premium_Values.get("Employers Liability").get("Insurance Tax");
										}catch(NullPointerException npe){
											try{
												EmployersLiabilityPrem = common.transaction_Details_Premium_Values.get("Employers Liability_FP").get("Gross Premium");
												EmployersLiabilityIPT = common.transaction_Details_Premium_Values.get("Employers Liability_FP").get("Insurance Tax");
											}catch(NullPointerException npe1){
											EmployersLiabilityPrem = 0.0;
											EmployersLiabilityIPT = 0.0;
										}
										}
										double totalPremium = publicLiabilityPrem + EmployersLiabilityPrem;
										double totalIPT = publicLiabilityIPT +EmployersLiabilityIPT;
										fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium £"+common.getWholeNumber((int) Math.floor(totalPremium))), "Premium £"+common.getWholeNumber((int) Math.floor(totalPremium)) , fileName);
										fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+common.getWholeNumber((int) Math.floor(totalIPT))), "Insurance Premium Tax £"+common.getWholeNumber((int) Math.floor(totalIPT)) , fileName);
										TotalPremiumWithAdminDocAct = totalPremium + totalIPT;
										fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+common.getWholeNumber((int) Math.floor(TotalPremiumWithAdminDocAct))), "TOTAL £"+common.getWholeNumber((int) Math.floor(TotalPremiumWithAdminDocAct)) , fileName);
									
//									}else{
//										fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium £"+formatter.format((int) Math.floor(PremiumExcTerrDocActOED))), "Premium £"+formatter.format((int) Math.floor(PremiumExcTerrDocAct)) , fileName);
//										fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+formatter.format((int) Math.floor(InsTaxDocActOED))), "Insurance Premium Tax £"+formatter.format((int) Math.floor(InsTaxDocActOED)) , fileName);
//										TotalPremiumWithAdminDocAct = PremiumExcTerrDocActOED + InsTaxDocActOED;
//										fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+formatter.format((int) Math.floor(TotalPremiumWithAdminDocAct))), "TOTAL £"+formatter.format((int) Math.floor(TotalPremiumWithAdminDocAct)) , fileName);
//									}
//									
				}
			}else if(TestBase.product.equalsIgnoreCase("CMA")){
				TotalPremiumWithAdminDocAct =0;
				if(common.currentRunningFlow.contains("NB") || common.currentRunningFlow.contains("Renewal") || common.currentRunningFlow.contains("Requote") || (common.currentRunningFlow.contains("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA"))){
					if(Integer.parseInt((String)mdata.get("PS_Duration"))!=365){
		//			if((String)mdata.get("PS_Duration").equals(obj)){
						double totalPremium = Double.parseDouble((String)mdata.get("PS_Total_GP"));
						double totalIPT = Double.parseDouble((String)mdata.get("PS_Total_GT"));
						String finalPremium = common.getWholeNumber((int) Math.floor(Double.parseDouble((String)mdata.get("PS_TotalPremium"))));
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium £"+common.getWholeNumber((int) Math.floor(totalPremium))), "Premium £"+common.getWholeNumber(totalPremium), fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+common.getWholeNumber((int) Math.floor(totalIPT))), "Insurance Premium Tax £"+common.getWholeNumber(totalIPT), fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+common.getWholeNumber(finalPremium)), "TOTAL £"+finalPremium , fileName);
					
					}else{
						double totalPremium = Double.parseDouble((String)mdata.get("PS_Total_GP"));
						double totalIPT = Double.parseDouble((String)mdata.get("PS_Total_GT"));
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium £"+common.getWholeNumber((int) Math.floor(totalPremium))), "Premium £"+totalPremium , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+common.getWholeNumber((int) Math.floor(totalIPT))), "Insurance Premium Tax £"+totalIPT , fileName);
						String finalPremium = common.getWholeNumber((int) Math.floor(Double.parseDouble((String)mdata.get("PS_TotalPremium"))));
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+finalPremium), "TOTAL £"+finalPremium , fileName);
					}
					
				}else if(common.currentRunningFlow.contains("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
				//	if(Integer.parseInt((String)mdata.get("PS_Duration"))!=365){
					String totalPremium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium")).split("\\.")[0];
					String totalIPT = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax"));
					String finalPremium =  Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Total Premium")).split("\\.")[0];
					if(common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium") < 0){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Return Premium £"+Math.abs(Integer.parseInt(totalPremium))), "Premium £"+ Math.abs(Double.parseDouble(totalPremium)) , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+Math.abs(Double.parseDouble(totalIPT))), "Insurance Premium Tax £"+Math.abs(Double.parseDouble(totalIPT)) , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+Math.abs(Integer.parseInt(finalPremium))), "TOTAL £"+Math.abs(Double.parseDouble(finalPremium)) , fileName);
					}else{					
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium £"+totalPremium), "Premium £"+totalPremium , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+totalIPT), "Insurance Premium Tax £"+totalIPT , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+finalPremium), "TOTAL £"+finalPremium , fileName);
					}
				
				}
			}
					//This will verify endorsement in Section Endorsements.
					String flag="true";
					Iterator collectiveDataIT = EndorsementCollectiveData.entrySet().iterator();
					while(collectiveDataIT.hasNext()){
						Map.Entry collectiveDataMapValue = (Map.Entry)collectiveDataIT.next();
						String collectiveEndorsementCode = collectiveDataMapValue.getKey().toString();
						
						Iterator individualDataIT = EndorsementCollectiveData.get(collectiveEndorsementCode).entrySet().iterator();
						while(individualDataIT.hasNext()){
							Map.Entry individualDataMapValue = (Map.Entry)individualDataIT.next();
							String individualEndorsementSection = individualDataMapValue.getValue().toString();
							if(individualEndorsementSection.contains("Contract Lift")){
								individualEndorsementSection = "CPA Contract Lift Cover (Lifted Goods)";
							}
							if(individualEndorsementSection.contains("Offshore Work")){
								individualEndorsementSection = "Offshore Work And Visits";
							}
							if(individualEndorsementSection.contains("Contract Works")){
								individualEndorsementSection = "Contract Works";
							}
							String mergedEndorsementText = null;
							if(EndorsementCollectiveData.get(collectiveEndorsementCode).containsValue("Policy")){
								mergedEndorsementText = collectiveEndorsementCode + " " + individualEndorsementSection;
							}else{
								mergedEndorsementText = individualEndorsementSection + " " +collectiveEndorsementCode;
							}
							
							if(parsedText.contains(mergedEndorsementText)){
								TestUtil.reportStatus("Verified Endorsement : [<b> "+collectiveEndorsementCode+" -- "+individualEndorsementSection+"</b>] in "+fileName+" document.", "Pass", false);
								//TestUtil.reportStatus("Applied Endorsement <b> [ "+collectiveEndorsementCode+" </b> with title as <b> "+individualEndorsementSection+" ] </b> is present on premium summary page.", "Info", false);
								fail_count = fail_count + 0;
								flag = "true";
								break;
							}else{
								flag="false";
							}
						}
						if(flag.equalsIgnoreCase("false")){
							TestUtil.reportStatus("<p style='color:red'> PDF Document: "+fileName+" does not contain [<b> "+collectiveEndorsementCode+" </b>] </p>", "Fail", false);
							//TestUtil.reportStatus("<p style='color:red'> Endorsement <b> [ "+collectiveEndorsementCode+" ] </b> is not present on premium summary page.</p>", "Info", false);
							fail_count = fail_count + 1;
						}
					}
					
					//Validate extra endorsement present on Endorsement screen should not be present on Premium Screen.
					Iterator extraEndorsementDetailsIT = ExtraEndorsementList.entrySet().iterator();
					while(extraEndorsementDetailsIT.hasNext()){
						Map.Entry extraEdnorsementValue = (Map.Entry)extraEndorsementDetailsIT.next();
						String extraEDCode = extraEdnorsementValue.getValue().toString();
						
						if(parsedText.contains(extraEDCode)){
							TestUtil.reportStatus("<p style='color:red'> PDF Document: "+fileName+" does not contain [<b> "+extraEDCode+" </b>] </p>", "Fail", false);
							//TestUtil.reportStatus("<p style='color:red'> Endorsement <b> [ "+collectiveEndorsementCode+" ] </b> is not present on premium summary page.</p>", "Info", false);
							fail_count = fail_count + 1;
							//TestUtil.reportStatus("<p style='color:red'> Extra endorsement <b> [ "+extraEDCode+" ] </b> is getting displayed on Premium Summary screen which should not be present. </p>", "Info", true);
						}
					}
					
					//This will verify endorsements presents below "Sections Endorsements" table.
					flag="true";
					collectiveDataIT = EndorsementCollectiveData.entrySet().iterator();
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
							String mergedEndorsementText = collectiveEndorsementCode+" - "+individualEndorsementTitle;
							if(parsedText.contains(mergedEndorsementText)){
								//TestUtil.reportStatus("Verified Endorsement : [<b> "+collectiveEndorsementCode+" - "+individualEndorsementTitle+"</b>] in "+fileName+" document.", "Pass", false);
								//TestUtil.reportStatus("Applied Endorsement <b> [ "+collectiveEndorsementCode+" </b> with title as <b> "+individualEndorsementSection+" ] </b> is present on premium summary page.", "Info", false);
								fail_count = fail_count + 0;
								flag = "true";
								break;
							}else{
								flag="false";
							}
						}
						if(flag.equalsIgnoreCase("false")){
							TestUtil.reportStatus("<p style='color:red'> PDF Document: "+fileName+" does not contain [<b> "+collectiveEndorsementCode+" </b>] </p>", "Fail", false);
							//TestUtil.reportStatus("<p style='color:red'> Endorsement <b> [ "+collectiveEndorsementCode+" ] </b> is not present on premium summary page.</p>", "Info", false);
							fail_count = fail_count + 1;
						}
					}
					
					//Validate extra endorsement present on Endorsement screen should not be present on Premium Screen.
					extraEndorsementDetailsIT = ExtraEndorsementList.entrySet().iterator();
					while(extraEndorsementDetailsIT.hasNext()){
						Map.Entry extraEdnorsementValue = (Map.Entry)extraEndorsementDetailsIT.next();
						String extraEDCode = extraEdnorsementValue.getValue().toString();
						
						if(parsedText.contains(extraEDCode)){
							TestUtil.reportStatus("<p style='color:red'> PDF Document: "+fileName+" does not contain [<b> "+extraEDCode+" </b>] </p>", "Fail", false);
							//TestUtil.reportStatus("<p style='color:red'> Endorsement <b> [ "+collectiveEndorsementCode+" ] </b> is not present on premium summary page.</p>", "Info", false);
							fail_count = fail_count + 1;
							//TestUtil.reportStatus("<p style='color:red'> Extra endorsement <b> [ "+extraEDCode+" ] </b> is getting displayed on Premium Summary screen which should not be present. </p>", "Info", true);
						}
					}	
					
				break;
				
			case "Statement of Fact":
								
				incrementalDays = Integer.parseInt(CONFIG.getProperty("DaysIncrementNumber"));
				policyDuration = Integer.parseInt((String)mdata.get("PS_Duration"));
				
				fail_count=0;
				
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("STATEMENT OF FACT"), "Document : STATEMENT OF FACT", fileName);
							
				if(common.currentRunningFlow.equals("Renewal")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName")), "Insured Name : "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName") , fileName);
				}else if(common.currentRunningFlow.equals("MTA") || common.currentRunningFlow.equals("Requote") || common.currentRunningFlow.equals("Rewind")){
					if(TestBase.businessEvent.equals("Renewal")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.Renewal_excel_data_map.get("Renewal_ClientName")), "Insured Name : "+(String)common.Renewal_excel_data_map.get("Renewal_ClientName") , fileName);
					}else{
						
							if(common.currentRunningFlow.equalsIgnoreCase("MTA")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.NB_excel_data_map.get("NB_ClientName")), "Insured Name : "+(String)common.NB_excel_data_map.get("NB_ClientName") , fileName);
							}else if(common.currentRunningFlow.equalsIgnoreCase("Rewind")){
								if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")) {
									fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.Rewind_excel_data_map.get("Rewind_ClientName")), "Insured Name : "+(String)common.Rewind_excel_data_map.get("Rewind_ClientName") , fileName);
								}else{
									fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.NB_excel_data_map.get("NB_ClientName")), "Insured Name : "+(String)common.NB_excel_data_map.get("NB_ClientName") , fileName);
								}
							}
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
						if(common.currentRunningFlow.equalsIgnoreCase("MTA")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.NB_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.NB_excel_data_map.get("QC_AgencyName") , fileName);
							}else if(common.currentRunningFlow.equalsIgnoreCase("Rewind")){
								if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")) {
									fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.Rewind_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.Rewind_excel_data_map.get("QC_AgencyName") , fileName);
								}else{
									fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.NB_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.NB_excel_data_map.get("QC_AgencyName") , fileName);
								}
							}
					}
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.Renewal_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.Renewal_excel_data_map.get("QC_AgencyName") , fileName);
				}
				
				if(docType.contains("Draft")){
					if(!common.currentRunningFlow.equalsIgnoreCase("Renewal"))
					{
						if(common.currentRunningFlow.equalsIgnoreCase("MTA") && TestBase.businessEvent.equalsIgnoreCase("Renewal"))
						{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.Renewal_excel_data_map.get("Renewal_QuoteNumber")) ,"Quote Reference : "+common.Renewal_excel_data_map.get("Renewal_QuoteNumber"),fileName);
							
							if(!TestBase.product.contains("GTA")  && !TestBase.product.contains("GTB") && !TestBase.product.contains("DOB"))
							{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quotation Date: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("QuoteDate"), -incrementalDays)) ,"Quote Date : "+common.daysIncrement((String)common.Renewal_excel_data_map.get("QuoteDate"), -incrementalDays),fileName);
							}
							
						}else if(common.currentRunningFlow.equalsIgnoreCase("MTA") && TestBase.businessEvent.equalsIgnoreCase("MTA")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.NB_excel_data_map.get("NB_QuoteNumber")) ,"Quote Reference : "+common.NB_excel_data_map.get("NB_QuoteNumber"),fileName);
							
							if(!TestBase.product.contains("GTA") && !TestBase.product.contains("GTB") && !TestBase.product.contains("DOB")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quotation Date: "+common.daysIncrement((String)common.MTA_excel_data_map.get("QuoteDate"), -incrementalDays)) ,"Quote Date : "+common.daysIncrement((String)common.MTA_excel_data_map.get("QuoteDate"), -incrementalDays),fileName);
							}
							
						}
						else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.NB_excel_data_map.get("NB_QuoteNumber")) ,"Quote Reference : "+common.NB_excel_data_map.get("NB_QuoteNumber"),fileName);
							
							if(!TestBase.product.contains("GTA")  && !TestBase.product.contains("GTB") && !TestBase.product.contains("DOB")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quotation Date: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), -incrementalDays)) ,"Quote Date : "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), -incrementalDays),fileName);
							}
							
						}
						
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_QuoteNumber")) ,"Quote Reference : "+common.Renewal_excel_data_map.get(common.currentRunningFlow+"_QuoteNumber"),fileName);
					}
				}else{
					if(!common.currentRunningFlow.equalsIgnoreCase("Renewal") && !common.currentRunningFlow.equalsIgnoreCase("Requote")){
						if(common.currentRunningFlow.equals("Rewind") && TestBase.businessEvent.equalsIgnoreCase("Renewal")){
							if(TestBase.product.equals("MFB") || TestBase.product.equals("MFC")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Pen Reference: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Pen Reference:  "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
							}else{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Policy Number : "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
							}
						}else if(common.currentRunningFlow.equals("MTA") && TestBase.businessEvent.equalsIgnoreCase("Renewal")){
							if(TestBase.product.equals("MFB") || TestBase.product.equals("MFC")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Pen Reference: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Pen Reference:  "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
							}else{	
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Policy Number : "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
							}
						}else if(common.currentRunningFlow.equalsIgnoreCase("Rewind")){
							if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")) {
								if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
									if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement")){
										fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(PolicyNumberMessage+(String)common.MTA_excel_data_map.get("MTA_PolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("MTA_PolicyNumber"),fileName);
									}else if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("New Business")){
										fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(PolicyNumberMessage+(String)common.NB_excel_data_map.get("NB_PolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("NB_PolicyNumber"),fileName);
									}
								}else{
									fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(PolicyNumberMessage+(String)common.MTA_excel_data_map.get("MTA_PolicyNumber")) ,"Policy Number : "+common.MTA_excel_data_map.get("MTA_PolicyNumber"),fileName);
								}
							}else{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(PolicyNumberMessage+(String)common.NB_excel_data_map.get("NB_PolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("NB_PolicyNumber"),fileName);
							}
						}else{
							if(TestBase.product.equals("MFB") || TestBase.product.equals("MFC")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Pen Reference: "+(String)common.NB_excel_data_map.get("NB_PolicyNumber")) ,"Pen Reference:  "+common.NB_excel_data_map.get("NB_PolicyNumber"),fileName);
							}else{
								if(!common_CCD.isEndorsementDone){
									fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.NB_excel_data_map.get("NB_PolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("NB_PolicyNumber"),fileName);
								}else{
									fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.MTA_excel_data_map.get("MTA_PolicyNumber")) ,"Policy Number : "+common.MTA_excel_data_map.get("MTA_PolicyNumber"),fileName);
								}
							}
							
						}
												
					}else{
						//fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)mdata.get(common.currentRunningFlow+"_PolicyNumber")) ,"Policy Number : "+mdata.get(common.currentRunningFlow+"_PolicyNumber"),fileName);
						if(TestBase.product.equals("MFB") || TestBase.product.equals("MFC")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Pen Reference: "+(String)mdata.get("Renewal_PolicyNumber")) ,"Pen Reference:  "+mdata.get("Renewal_PolicyNumber"),fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)mdata.get("Renewal_PolicyNumber")) ,"Policy Number : "+mdata.get("Renewal_PolicyNumber"),fileName);
						}
					}
					
				}
				/*if(!common.currentRunningFlow.equals("Renewal")){
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
						}else if(common.currentRunningFlow.equalsIgnoreCase("MTA") && TestBase.businessEvent.equalsIgnoreCase("MTA")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.NB_excel_data_map.get("NB_QuoteNumber")) ,"Quote Reference : "+common.NB_excel_data_map.get("NB_QuoteNumber"),fileName);
							
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quotation Date: "+common.daysIncrement((String)common.MTA_excel_data_map.get("QuoteDate"), -incrementalDays)) ,"Quote Date : "+common.daysIncrement((String)common.MTA_excel_data_map.get("QuoteDate"), -incrementalDays),fileName);
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
							if(TestBase.product.equals("MFB") || TestBase.product.equals("MFC")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Pen Reference: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Pen Reference:  "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
							}else{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Policy Number : "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
							}
							
						}else if(common.currentRunningFlow.equalsIgnoreCase("MTA") && TestBase.businessEvent.equalsIgnoreCase("Renewal")){
							//fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Policy Number : "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
							if(TestBase.product.equals("MFB") || TestBase.product.equals("MFC")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Pen Reference: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Pen Reference:  "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
							}else{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.Renewal_excel_data_map.get("Renewal_PolicyNumber")) ,"Policy Number : "+common.Renewal_excel_data_map.get("Renewal_PolicyNumber"),fileName);
							}
						}else{
							if(TestBase.product.equals("MFB") || TestBase.product.equals("MFC")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Pen Reference: "+(String)common.NB_excel_data_map.get("NB_PolicyNumber")) ,"Pen Reference:  "+common.NB_excel_data_map.get("NB_PolicyNumber"),fileName);
							}else{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.NB_excel_data_map.get("NB_PolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("NB_PolicyNumber"),fileName);
							}
							
						}
						
					}else if(common.currentRunningFlow.equalsIgnoreCase("Rewind") || common.currentRunningFlow.equalsIgnoreCase("MTA")){
						if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
							if(TestBase.product.equals("MFB") || TestBase.product.equals("MFC")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Pen Reference: "+(String)mdata.get("NB_PolicyNumber")) ,"Pen Reference:  "+mdata.get("Renewal_PolicyNumber"),fileName);
							}else{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)mdata.get("Renewal_PolicyNumber")) ,"Policy Number : "+mdata.get("Renewal_PolicyNumber"),fileName);
							}
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.NB_excel_data_map.get("NB_PolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("NB_PolicyNumber"),fileName);
						}
						
					}else{
						if(TestBase.product.equals("MFB") || TestBase.product.equals("MFC")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Pen Reference: "+(String)mdata.get("Renewal_PolicyNumber")) ,"Pen Reference:  "+mdata.get("Renewal_PolicyNumber"),fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)mdata.get("Renewal_PolicyNumber")) ,"Policy Number : "+mdata.get("Renewal_PolicyNumber"),fileName);
						}
						//fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)mdata.get(common.currentRunningFlow+"_PolicyNumber")) ,"Policy Number : "+mdata.get(common.currentRunningFlow+"_PolicyNumber"),fileName);
					}
				}*/
			
				break;
				
			case "Employers Liability Certificate" :
				fail_count=0;
				
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("CERTIFICATE OF EMPLOYERS' LIABILITY INSURANCE"), "Document : CERTIFICATE OF EMPLOYERS' LIABILITY INSURANCE", fileName);
					if(docType.contains("Draft")){
						
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)mdata.get(common.currentRunningFlow+"_PolicyNumber")) ,"Policy Number : "+mdata.get(common.currentRunningFlow+"_PolicyNumber"),fileName);
					}
					if(TestBase.product.equals("OED")){
						if(common.currentRunningFlow.equals("Renewal")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Name of Policyholder: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName")), "Name of Policy Holder: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName") , fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Name of Policyholder: "+(String)common.NB_excel_data_map.get("NB_ClientName")), "Name of Policy Holder: "+(String)common.NB_excel_data_map.get("NB_ClientName") , fileName);
						}
						if(((String)mdata.get("PS_DefaultStartEndDate")).equals("No")){
							if(common.currentRunningFlow.equals("MTA")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of commencement of insurance policy: "+common.daysIncrement((String)common.MTA_excel_data_map.get("PS_PolicyStartDate"), 0)) ,"Date of Commencement of Insurance Policy: "+common.daysIncrement((String)common.MTA_excel_data_map.get("PS_PolicyStartDate"),0),fileName);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of expiry of insurance policy: "+common.daysIncrement((String)common.MTA_excel_data_map.get("PS_PolicyEndDate"), 0)) ,"Date of Expiry of Insurance Policy: "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0),fileName);							
								
							}else{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of commencement of insurance policy: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0)) ,"Date of Commencement of Insurance Policy: "+common.daysIncrement((String)mdata.get("QC_InceptionDate"), 0),fileName);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of expiry of insurance policy: "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0)) ,"Date of Expiry of Insurance Policy: "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0),fileName);							
								
								}
						}else{
							if(common.currentRunningFlow.equals("MTA")){
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of commencement of insurance policy: "+common.daysIncrement((String)common.MTA_excel_data_map.get("PS_PolicyStartDate"), 0)) ,"Date of Commencement of Insurance Policy:"+common.daysIncrement((String)common.MTA_excel_data_map.get("PS_PolicyStartDate"),0),fileName);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of expiry of insurance policy: "+common.daysIncrement((String)common.MTA_excel_data_map.get("PS_PolicyEndDate"), 0)) ,"Date of Expiry of Insurance Policy:"+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0),fileName);							
								
								}else{
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of commencement of insurance policy: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0)) ,"Date of Commencement of Insurance Policy:"+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);
								fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of expiry of insurance policy: "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0)) ,"Date of Expiry of Insurance Policy:"+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0),fileName);							
								
								}
							}
					}else{
						if(common.currentRunningFlow.equals("Renewal")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Name of Policy Holder: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName")), "Name of Policy Holder: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName") , fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Name of Policy Holder: "+(String)common.NB_excel_data_map.get("NB_ClientName")), "Name of Policy Holder: "+(String)common.NB_excel_data_map.get("NB_ClientName") , fileName);
						}
					
					if(common.currentRunningFlow.equals("Renewal")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Name of Policy Holder: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName")), "Name of Policy Holder: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName") , fileName);
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Name of Policy Holder: "+(String)common.NB_excel_data_map.get("NB_ClientName")), "Name of Policy Holder: "+(String)common.NB_excel_data_map.get("NB_ClientName") , fileName);
					}
					if(((String)mdata.get("PS_DefaultStartEndDate")).equals("No")){
						if(common.currentRunningFlow.equals("MTA")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of Commencement of Insurance Policy: "+common.daysIncrement((String)common.MTA_excel_data_map.get("PS_PolicyStartDate"), 0)) ,"Date of Commencement of Insurance Policy: "+common.daysIncrement((String)common.MTA_excel_data_map.get("PS_PolicyStartDate"),0),fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of Expiry of Insurance Policy: "+common.daysIncrement((String)common.MTA_excel_data_map.get("PS_PolicyEndDate"), 0)) ,"Date of Expiry of Insurance Policy: "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0),fileName);							
							
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of Commencement of Insurance Policy: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0)) ,"Date of Commencement of Insurance Policy: "+common.daysIncrement((String)mdata.get("QC_InceptionDate"), 0),fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of Expiry of Insurance Policy: "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0)) ,"Date of Expiry of Insurance Policy: "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0),fileName);							
							
							}
					}else{
						if(common.currentRunningFlow.equals("MTA")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of Commencement of Insurance Policy: "+common.daysIncrement((String)common.MTA_excel_data_map.get("PS_PolicyStartDate"), 0)) ,"Date of Commencement of Insurance Policy:"+common.daysIncrement((String)common.MTA_excel_data_map.get("PS_PolicyStartDate"),0),fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of Expiry of Insurance Policy: "+common.daysIncrement((String)common.MTA_excel_data_map.get("PS_PolicyEndDate"), 0)) ,"Date of Expiry of Insurance Policy:"+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0),fileName);							
							
							}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of Commencement of Insurance Policy: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0)) ,"Date of Commencement of Insurance Policy:"+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Date of Expiry of Insurance Policy: "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0)) ,"Date of Expiry of Insurance Policy:"+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0),fileName);							
							
							}
						}
					}
				break;
				
			case "Policy Schedule Personal Accident":
				
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
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.Renewal_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.Renewal_excel_data_map.get("QC_AgencyName") , fileName);
					
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Correspondence Address: "+(String)common.Renewal_excel_data_map.get("PG_Address")), "Correspondence Address:  "+(String)common.Renewal_excel_data_map.get("PG_Address") , fileName);
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Postcode: "+(String)common.Renewal_excel_data_map.get("CC_Postcode")), "Postcode: "+(String)common.Renewal_excel_data_map.get("CC_Postcode") , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.NB_excel_data_map.get("NB_ClientName")), "Insured Name : "+(String)common.NB_excel_data_map.get("NB_ClientName") , fileName);
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.NB_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.NB_excel_data_map.get("QC_AgencyName") , fileName);
					
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Correspondence Address: "+(String)common.NB_excel_data_map.get("PG_Address")), "Correspondence Address:  "+(String)common.NB_excel_data_map.get("PG_Address") , fileName);
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Postcode: "+(String)common.NB_excel_data_map.get("CC_Postcode")), "Postcode: "+(String)common.NB_excel_data_map.get("CC_Postcode") , fileName);
					
				}
				
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
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0)) ,"To:"+common.daysIncrement((String)mdata.get("QC_DeadlineDate"), 0),fileName);							
						
						}else if(common.currentRunningFlow.equals("Renewal")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Renewal Date: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 365)) ,"Renewal Date: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0)) ,"To:"+common.daysIncrement((String)mdata.get("QC_DeadlineDate"), 0),fileName);							
							
							
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0)) ,"Period of Insurance: From: "+common.daysIncrement((String)mdata.get("PS_PolicyStartDate"), 0),fileName);
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("To: "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"), 0)) ,"To:"+common.daysIncrement((String)mdata.get("QC_DeadlineDate"), 0),fileName);							
							
						}
				}
				if(TestBase.product.equals("OED")){
					if(common.currentRunningFlow.equals("MTA")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium £"+common.getWholeNumber(common.transaction_Details_Premium_Values.get("Personal Accident").get("Gross Premium"))), "Premium £"+common.getWholeNumber(common.transaction_Details_Premium_Values.get("Personal Accident").get("Gross Premium")) , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+common.getWholeNumber(common.transaction_Details_Premium_Values.get("Personal Accident").get("Insurance Tax"))), "Insurance Premium Tax £"+common.getWholeNumber(common.transaction_Details_Premium_Values.get("Personal Accident").get("Insurance Tax")) , fileName);
						double PAOTotal = common.transaction_Details_Premium_Values.get("Personal Accident").get("Gross Premium") + common.transaction_Details_Premium_Values.get("Personal Accident").get("Insurance Tax");
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+common.getWholeNumber(PAOTotal)), "TOTAL £"+common.getWholeNumber(PAOTotal) , fileName);
						
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium £"+common.getWholeNumber(Double.parseDouble((String)mdata.get("PS_PersonalAccident_GP")))), "Premium £"+common.getWholeNumber(Double.parseDouble((String)mdata.get("PS_PersonalAccident_GP"))) , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+common.getWholeNumber(Double.parseDouble((String)mdata.get("PS_PersonalAccident_GT")))), "Insurance Premium Tax £"+common.getWholeNumber(Double.parseDouble((String)mdata.get("PS_PersonalAccident_GT"))) , fileName);
						double PAOTotal = Double.parseDouble((String)mdata.get("PS_PersonalAccident_GP")) + Double.parseDouble((String)mdata.get("PS_PersonalAccident_GT"));
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+common.getWholeNumber(PAOTotal)), "TOTAL £"+common.getWholeNumber(PAOTotal) , fileName);
					}
				
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium £"+common.getWholeNumber(Double.parseDouble((String)mdata.get("PS_PersonalAccidentOptional_GP")))), "Premium £"+common.getWholeNumber(Double.parseDouble((String)mdata.get("PS_PersonalAccidentOptional_GP"))) , fileName);
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+common.getWholeNumber(Double.parseDouble((String)mdata.get("PS_PersonalAccidentOptional_GT")))), "Insurance Premium Tax £"+common.getWholeNumber(Double.parseDouble((String)mdata.get("PS_PersonalAccidentOptional_GT"))) , fileName);
					double PAOTotal = Double.parseDouble((String)mdata.get("PS_PersonalAccidentOptional_GP")) + Double.parseDouble((String)mdata.get("PS_PersonalAccidentOptional_GT"));
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+common.getWholeNumber(PAOTotal)), "TOTAL £"+common.getWholeNumber(PAOTotal) , fileName);
				}
				
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
   					if(((String)Outermap.get("PS_PaymentWarrantyRules")).equalsIgnoreCase("No")){
   						ExpecteDueDate = common.getLastDayOfMonth((String)Outermap.get("QuoteDate"), 1);
   					}else{
   						ExpecteDueDate = (String)Outermap.get("PS_PaymentWarrantyDueDate");
   					}
   					
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
   						String sectionName = null;
   						if(transactSumVal.equalsIgnoreCase("")){
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[3]")).getText();
   							sectionName = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   	   						td=8;
   						}else{
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   							sectionName = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[9]")).getText();
   	   						td=11;
   						}
   						
   						if(TestBase.product.equalsIgnoreCase("GTC")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double cargo = calculateCargoTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + cargo;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double cargo = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + cargo;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTD")){
   	   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   	   							double GIT = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   	   							Total = Total + GIT;
   	   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   	   							double GIT = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   	   							Total = Total + GIT;
   	   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTA")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double gta = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gta;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double gta = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gta;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTB")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double gtb = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gtb;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double gtb = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gtb;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("OED")){
   							if(Recipient.equalsIgnoreCase("Hcc International Insurance Company Plc") && sectionName.equalsIgnoreCase("Public Liability")){
   	   							double publicLiability = calculatePLTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + publicLiability;
   	   						}else if(Recipient.equalsIgnoreCase("Hcc International Insurance Company Plc") && sectionName.equalsIgnoreCase("Employers Liability")){
   	   							double employersLiability = calculateELTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + employersLiability;
   	   						}else if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited") && sectionName.equalsIgnoreCase("Personal Accident")){
   	   							double personalAcctident = calculatePATS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + personalAcctident;
   	   						}else if(Recipient.equalsIgnoreCase("Das Legal Expenses Insurance Company Limited") && sectionName.equalsIgnoreCase("Legal Expenses")){
   	   							double legalexpense = calculateLETS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + legalexpense;
   	   						}else if(Recipient.equalsIgnoreCase((String)data_map.get("QC_AgencyName"))){
   	   							double agentCommision = calculateBrokerTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + agentCommision;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + LegalExpenses;
   	   						}
   						}else {
   						if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited")){
   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   						if(Recipient.equalsIgnoreCase("Zurich Insurance Plc")){
   							double TotalCovers= calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + TotalCovers;
   						}
   						if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   							double TotalCovers= calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + TotalCovers;
   						}
   						if(Recipient.equalsIgnoreCase("Brokerage Account")){
   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   						if(Recipient.equalsIgnoreCase((String)Outermap.get("QC_AgencyName"))){
   							double agentCommisin = calculateAgentTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + agentCommisin;
   						}
   						
   						}
   					}
   					break;
   				case "Amended New Business" : 
   					
   					TestUtil.reportStatus("Verification Started on Transaction Summary page "+trasacSummaryType+" . ", "PASS", false);
   					ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
   					if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")) {
   						ExpecteDueDate = common.getLastDayOfMonth((String)common.NB_excel_data_map.get("EffectiveDate"), 1);
   					}else{
   						ExpecteDueDate = common.getLastDayOfMonth((String)common.NB_excel_data_map.get("QuoteDate"), 1);
   					}
   					
   					if(ActualDueDate.equalsIgnoreCase(ExpecteDueDate)){
      					 String tMsg="Actual Due Date : <b>[  "+ActualDueDate+"  ]</b> has been matched with Expected Due Date : <b>[  "+ExpecteDueDate+"  ]</b>";
      					 TestUtil.reportStatus(tMsg, "Pass", false);
      					}
      					else{
      	   					 String tMsg="Actual Due Date : <b>[  "+ActualDueDate+"  ]</b> does not matche with Expected Due Date : <b>[   "+ExpecteDueDate+"  ]</b>";
      	   					 TestUtil.reportStatus(tMsg, "Fail", false);
      					}
   					ActualTransationDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[3]")).getText();
   					if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")) {
   						ExpecteTransactionDate = (String)common.Rewind_excel_data_map.get("QuoteDate");
   					}else{
   						ExpecteTransactionDate = (String)common.NB_excel_data_map.get("QuoteDate");
   					}
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
   	   						customAssert.assertTrue(WriteDataToXl(event+"_Rewind", "Transaction Summary", (String)common.Rewind_excel_data_map.get("Automation Key"), "TS_TransactionSummaryTotal", actualTotal,common.Rewind_excel_data_map),"Error while writing Transaction Summary data to excel .");

   	   						break outer;
   	   						}
   						String sectionName = null;
   						if(transactSumVal.equalsIgnoreCase("")){
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[3]")).getText();
   							sectionName = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   	   						td=8;
   						}else{
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   							sectionName = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[9]")).getText();
   	   						td=11;
   						}
   						if(TestBase.product.equalsIgnoreCase("OED")){
   							if(Recipient.equalsIgnoreCase("Hcc International Insurance Company Plc") && sectionName.equalsIgnoreCase("Public Liability")){
   	   							double publicLiability = calculatePLTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + publicLiability;
   	   						}else if(Recipient.equalsIgnoreCase("Hcc International Insurance Company Plc") && sectionName.equalsIgnoreCase("Employers Liability")){
   	   							double employersLiability = calculateELTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + employersLiability;
   	   						}else if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited") && sectionName.equalsIgnoreCase("Personal Accident")){
   	   							double personalAcctident = calculatePATS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + personalAcctident;
   	   						}else if(Recipient.equalsIgnoreCase("Das Legal Expenses Insurance Company Limited") && sectionName.equalsIgnoreCase("Legal Expenses")){
   	   							double legalexpense = calculateLETS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + legalexpense;
   	   						}else if(Recipient.equalsIgnoreCase((String)data_map.get("QC_AgencyName"))){
   	   							double agentCommision = calculateBrokerTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + agentCommision;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + LegalExpenses;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTC")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double cargo = calculateCargoTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + cargo;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double cargo = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + cargo;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTD")){
   	   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   	   							double GIT = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   	   							Total = Total + GIT;
   	   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   	   							double GIT = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   	   							Total = Total + GIT;
   	   	   						}
   						}else	if(TestBase.product.equalsIgnoreCase("GTA")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double gta = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gta;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double gta = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gta;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTB")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double gtb = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gtb;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double gtb = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gtb;
   	   						}
   						}else{
   							if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited")){
	   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + LegalExpenses;
	   						}
	   						if(Recipient.equalsIgnoreCase("Zurich Insurance Plc")){
	   							double TotalCovers= calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + TotalCovers;
	   						}
	   						if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
	   							double TotalCovers= calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + TotalCovers;
	   						}
	   						if(Recipient.equalsIgnoreCase("Brokerage Account")){
	   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + LegalExpenses;
	   						}
	   						if(Recipient.equalsIgnoreCase((String)data_map.get("QC_AgencyName"))){
	   							double agentCommisin = calculateAgentTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + agentCommisin;
	   						}
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

   	   						//For MFB MFC flat premium
   	   						CARS_FP = false;
  	   						CV_FP = false;
  	   						AV_FP = false;
  	   						ST_FP = false;
  	   						OT_FP = false;
  	   						TP_FP = false;
  	   						TR_FP = false;
  	   						PAO_FP = false;
  	   						
  	   						//For OED Flat Premium
  	   						PL_FP = false;
	   						EL_FP = false;
	   						PA_FP = false;
	   						LE_FP = false;	   						
   	   						break outer;
   	   						}
   						
   						String sectionName = null;
   						if(transactSumVal.equalsIgnoreCase("")){
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[3]")).getText();
   							sectionName = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   	   						td=8;
   						}else{
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   							sectionName = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[9]")).getText();
   	   						td=11;
   						}
   						if(TestBase.product.equalsIgnoreCase("OED")){
   							if(Recipient.equalsIgnoreCase("Hcc International Insurance Company Plc") && sectionName.equalsIgnoreCase("Public Liability")){
   	   							double publicLiability = calculatePLTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + publicLiability;
   	   							PL_FP = true;
   	   						}else if(Recipient.equalsIgnoreCase("Hcc International Insurance Company Plc") && sectionName.equalsIgnoreCase("Employers Liability")){
   	   							double employersLiability = calculateELTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + employersLiability;
   	   							EL_FP = true;
   	   						}else if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited") && sectionName.equalsIgnoreCase("Personal Accident")){
   	   							double personalAcctident = calculatePATS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + personalAcctident;
   	   							PA_FP = true;
   	   						}else if(Recipient.equalsIgnoreCase("Das Legal Expenses Insurance Company Limited") && sectionName.equalsIgnoreCase("Legal Expenses")){
   	   							double legalexpense = calculateLETS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + legalexpense;
   	   							EL_FP = true;
   	   						}else if(Recipient.equalsIgnoreCase((String)data_map.get("QC_AgencyName"))){
   	   							double agentCommision = calculateBrokerTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + agentCommision;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + LegalExpenses;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("MFB") || TestBase.product.equalsIgnoreCase("MFC")){
   							if(sectionName.equalsIgnoreCase("Cars")){
   	   							double cars = calculateCarsTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + cars;
   	   							CARS_FP=true;
   	   						}else if(sectionName.equalsIgnoreCase("Commercial Vehicle")){
   	   							double CV = calculateCommercialVehiclesTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + CV;
   	   							CV_FP=true;
   	   						}else if(sectionName.equalsIgnoreCase("Agricultural Vehicles")){
   	   							double AV = calculateAgriVehiclesTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + AV;
   	   							AV_FP=true;
   	   						}else if(sectionName.equalsIgnoreCase("Special Types")){
   	   							double ST = calculateSpecialTypesTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + ST;
   	   							ST_FP=true;
   	   						}else if(sectionName.equalsIgnoreCase("Other Types")){
   	   							double OT = calculateOtherTypesTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + OT;
   	   							OT_FP=true;
   	   						}else if(sectionName.equalsIgnoreCase("Trade Plates")){
   	   							double TP = calculateTradePlatesTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + TP;
   	   							TP_FP=true;
   	   						}else if(sectionName.equalsIgnoreCase("Trailers")){
   	   							double TR = calculateTrailersTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + TR;
   	   							TR_FP=true;
   	   						}else if(sectionName.equalsIgnoreCase("Personal Accident Optional")){
   	   							double PAO = calculatePAOTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + PAO;
   	   							PAO_FP=true;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + LegalExpenses;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTC")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double cargo = calculateCargoTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + cargo;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double cargo = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + cargo;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTD")){
   	   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   	   							double GIT = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   	   							Total = Total + GIT;
   	   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   	   							double GIT = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   	   							Total = Total + GIT;
   	   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTA")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double gta = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gta;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double gta = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gta;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTB")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double gtb = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gtb;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double gtb = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gtb;
   	   						}
   						}else{
	   						if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited")){
	   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + LegalExpenses;
	   						}
	   						if(Recipient.equalsIgnoreCase("Zurich Insurance Plc")){
	   							double TotalCovers= calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + TotalCovers;
	   						}
	   						if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
	   							double TotalCovers= calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + TotalCovers;
	   						}
	   						if(Recipient.equalsIgnoreCase("Brokerage Account")){
	   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + LegalExpenses;
	   						}
	   						if(!TestBase.businessEvent.equalsIgnoreCase("Renewal")){
	   							if(Recipient.equalsIgnoreCase((String)common.NB_excel_data_map.get("QC_AgencyName"))){
	   								double agentCommisin = calculateAgentTS(code,data_map,trasacSummaryType,j,td);	
	   								Total = Total + agentCommisin;
	   							}
	   						}else{
	   							if(Recipient.equalsIgnoreCase((String)common.Renewal_excel_data_map.get("QC_AgencyName"))){
	   								double agentCommisin = calculateAgentTS(code,data_map,trasacSummaryType,j,td);	
	   								Total = Total + agentCommisin;
	   							}
	   						}
	   					}
   					}
   					break;
   				case "Amended Endorsement" : //MTA Rewind
   					
   					TestUtil.reportStatus("Verification Started on Transaction Summary page "+trasacSummaryType+"  . ", "Info", false);
   					ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
   					if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")) {
   						ExpecteDueDate = common.getLastDayOfMonth((String)common.NB_excel_data_map.get("EffectiveDate"), 1);
   					}else{
   						if(((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy")).equalsIgnoreCase("Yes") && ((String)common.MTA_excel_data_map.get("MTA_Status")).equalsIgnoreCase("Endorsement Rewind")) {
   							ExpecteDueDate = common.getLastDayOfMonth((String)common.NB_excel_data_map.get("EffectiveDate"), 1);
   						}else{
   							ExpecteDueDate = common.getLastDayOfMonth((String)common.NB_excel_data_map.get("QuoteDate"), 1);
   						}
   						
   					}
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
   					if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")) {
   						ExpecteTransactionDate = (String)common.Rewind_excel_data_map.get("QuoteDate");
   					}else{
   						if(((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy")).equalsIgnoreCase("Yes") && ((String)common.MTA_excel_data_map.get("MTA_Status")).equalsIgnoreCase("Endorsement Rewind")) {
   							ExpecteTransactionDate = (String)common.Rewind_excel_data_map.get("QuoteDate");
   						}else{
   							ExpecteTransactionDate = (String)common.NB_excel_data_map.get("QuoteDate");
   						}
   						
   					}
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

   	   						
   	   				//For MFB MFC flat premium
   	   						CARS_FP = false;
  	   						CV_FP = false;
  	   						AV_FP = false;
  	   						ST_FP = false;
  	   						OT_FP = false;
  	   						TP_FP = false;
  	   						TR_FP = false;
  	   						PAO_FP = false;
  	   						
  	   					//For OED Flat Premium
  	   						PL_FP = false;
	   						EL_FP = false;
	   						PA_FP = false;
	   						LE_FP = false;
   	   						break outer;
   	   						}
   						String sectionName = null;
   						if(transactSumVal.equalsIgnoreCase("")){
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[3]")).getText();
   							sectionName = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   	   						td=8;
   						}else{
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   							sectionName = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[9]")).getText();
   	   						td=11;
   						}
   						if(TestBase.product.equalsIgnoreCase("OED")){
   							if(Recipient.equalsIgnoreCase("Hcc International Insurance Company Plc") && sectionName.equalsIgnoreCase("Public Liability")){
   	   							double publicLiability = calculatePLTS(code,data_map,trasacSummaryType,j,td);	
   	   							PL_FP = true;
   	   							Total = Total + publicLiability;
   	   						}else if(Recipient.equalsIgnoreCase("Hcc International Insurance Company Plc") && sectionName.equalsIgnoreCase("Employers Liability")){
   	   							double employersLiability = calculateELTS(code,data_map,trasacSummaryType,j,td);
   	   						    EL_FP = true; 
   	   							Total = Total + employersLiability;
   	   						}else if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited") && sectionName.equalsIgnoreCase("Personal Accident")){
   	   							double personalAcctident = calculatePATS(code,data_map,trasacSummaryType,j,td);	
   	   							PA_FP =true;
   	   							Total = Total + personalAcctident;
   	   						}else if(Recipient.equalsIgnoreCase("Das Legal Expenses Insurance Company Limited") && sectionName.equalsIgnoreCase("Legal Expenses")){
   	   							double legalexpense = calculateLETS(code,data_map,trasacSummaryType,j,td);	
   	   							LE_FP = true;	
   	   							Total = Total + legalexpense;
   	   						}else if(Recipient.equalsIgnoreCase((String)data_map.get("QC_AgencyName"))){
   	   							double agentCommision = calculateBrokerTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + agentCommision;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + LegalExpenses;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("MFB") || TestBase.product.equalsIgnoreCase("MFC")){
   							if(sectionName.equalsIgnoreCase("Cars")){
   	   							double cars = calculateCarsTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + cars;
   	   							CARS_FP=true;
   	   						}else if(sectionName.equalsIgnoreCase("Commercial Vehicle")){
   	   							double CV = calculateCommercialVehiclesTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + CV;
   	   							CV_FP=true;
   	   						}else if(sectionName.equalsIgnoreCase("Agricultural Vehicles")){
   	   							double AV = calculateAgriVehiclesTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + AV;
   	   							AV_FP=true;
   	   						}else if(sectionName.equalsIgnoreCase("Special Types")){
   	   							double ST = calculateSpecialTypesTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + ST;
   	   							ST_FP=true;
   	   						}else if(sectionName.equalsIgnoreCase("Other Types")){
   	   							double OT = calculateOtherTypesTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + OT;
   	   							OT_FP=true;
   	   						}else if(sectionName.equalsIgnoreCase("Trade Plates")){
   	   							double TP = calculateTradePlatesTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + TP;
   	   							TP_FP=true;
   	   						}else if(sectionName.equalsIgnoreCase("Trailers")){
   	   							double TR = calculateTrailersTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + TR;
   	   							TR_FP=true;
   	   						}else if(sectionName.equalsIgnoreCase("Personal Accident Optional")){
   	   							double PAO = calculatePAOTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + PAO;
   	   							PAO_FP=true;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + LegalExpenses;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTC")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double cargo = calculateCargoTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + cargo;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double cargo = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + cargo;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTD")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double GIT = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + GIT;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double GIT = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + GIT;
   	   						}
   						}if(TestBase.product.equalsIgnoreCase("GTA")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double gta = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gta;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double gta = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gta;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTB")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double gtb = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gtb;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double gtb = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gtb;
   	   						}
   						}else{
	   						if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited")){
	   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + LegalExpenses;
	   						}
	   						if(Recipient.equalsIgnoreCase("Zurich Insurance Plc")){
	   							double TotalCovers= calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + TotalCovers;
	   						}
	   						if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
	   							double TotalCovers= calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + TotalCovers;
	   						}
	   						if(Recipient.equalsIgnoreCase("Brokerage Account")){
	   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + LegalExpenses;
	   						}
	   						if(Recipient.equalsIgnoreCase((String)common.NB_excel_data_map.get("QC_AgencyName"))){
	   							double agentCommisin = calculateAgentTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + agentCommisin;
	   						}									
   						}
   				}
   					break;
   				case "Renewal" : 
   					
   					TestUtil.reportStatus("Verification Started on Transaction Summary page "+trasacSummaryType+" . ", "PASS", false);
   					ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
   					ExpecteDueDate = common.getLastDayOfMonth((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), 1);
   					
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
   						
   						String sectionName = null;
   						if(transactSumVal.equalsIgnoreCase("")){
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[3]")).getText();
   							sectionName = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   	   						td=8;
   						}else{
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   							sectionName = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[9]")).getText();
   	   						td=11;
   						}
   						if(TestBase.product.equalsIgnoreCase("OED")){
   							if(Recipient.equalsIgnoreCase("Hcc International Insurance Company Plc") && sectionName.equalsIgnoreCase("Public Liability")){
   	   							double publicLiability = calculatePLTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + publicLiability;
   	   						}else if(Recipient.equalsIgnoreCase("Hcc International Insurance Company Plc") && sectionName.equalsIgnoreCase("Employers Liability")){
   	   							double employersLiability = calculateELTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + employersLiability;
   	   						}else if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited") && sectionName.equalsIgnoreCase("Personal Accident")){
   	   							double personalAcctident = calculatePATS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + personalAcctident;
   	   						}else if(Recipient.equalsIgnoreCase("Das Legal Expenses Insurance Company Limited") && sectionName.equalsIgnoreCase("Legal Expenses")){
   	   							double legalexpense = calculateLETS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + legalexpense;
   	   						}else if(Recipient.equalsIgnoreCase((String)data_map.get("QC_AgencyName"))){
   	   							double agentCommision = calculateBrokerTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + agentCommision;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + LegalExpenses;
   	   						}
   						}if(TestBase.product.equalsIgnoreCase("GTC")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double cargo = calculateCargoTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + cargo;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double cargo = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + cargo;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTD")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double GIT = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + GIT;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double GIT = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + GIT;
   	   						}
   						}if(TestBase.product.equalsIgnoreCase("GTA")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double gta = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gta;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double gta = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gta;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTB")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double gtb = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gtb;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double gtb = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gtb;
   	   						}
   						}else{
	   						if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited")){
	   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + LegalExpenses;
	   						}
	   						if(Recipient.equalsIgnoreCase("Zurich Insurance Plc")){
	   							double TotalCovers= calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + TotalCovers;
	   						}
	   						if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
	   							double TotalCovers= calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + TotalCovers;
	   						}
	   						if(Recipient.equalsIgnoreCase("Brokerage Account")){
	   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + LegalExpenses;
	   						}
	   						if(Recipient.equalsIgnoreCase((String)common.Renewal_excel_data_map.get("QC_AgencyName"))){
	   							double agentCommisin = calculateAgentTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + agentCommisin;
	   						}
   					 }
   				}
   					break;
   				case "Amended Renewal" : 
   					
   					TestUtil.reportStatus("Verification Started on Transaction Summary page "+trasacSummaryType+" . ", "PASS", false);
   					ActualDueDate = driver.findElement(By.xpath(part1+"/tr["+i+"]/td[4]")).getText();
   					ExpecteDueDate = common.getLastDayOfMonth((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), 1);
   					
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
   						String sectionName = null;
   						if(transactSumVal.equalsIgnoreCase("")){
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[3]")).getText();
   							sectionName = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   	   						td=8;
   						}else{
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   							sectionName = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[9]")).getText();
   	   						td=11;
   						}
   						if(TestBase.product.equalsIgnoreCase("OED")){
   							if(Recipient.equalsIgnoreCase("Hcc International Insurance Company Plc") && sectionName.equalsIgnoreCase("Public Liability")){
   	   							double publicLiability = calculatePLTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + publicLiability;
   	   						}else if(Recipient.equalsIgnoreCase("Hcc International Insurance Company Plc") && sectionName.equalsIgnoreCase("Employers Liability")){
   	   							double employersLiability = calculateELTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + employersLiability;
   	   						}else if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited") && sectionName.equalsIgnoreCase("Personal Accident")){
   	   							double personalAcctident = calculatePATS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + personalAcctident;
   	   						}else if(Recipient.equalsIgnoreCase("Das Legal Expenses Insurance Company Limited") && sectionName.equalsIgnoreCase("Legal Expenses")){
   	   							double legalexpense = calculateLETS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + legalexpense;
   	   						}else if(Recipient.equalsIgnoreCase((String)data_map.get("QC_AgencyName"))){
   	   							double agentCommision = calculateBrokerTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + agentCommision;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + LegalExpenses;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTC")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double cargo = calculateCargoTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + cargo;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double cargo = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + cargo;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTD")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double GIT = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + GIT;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double GIT = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + GIT;
   	   						}
   						}if(TestBase.product.equalsIgnoreCase("GTA")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double gta = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gta;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double gta = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gta;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTB")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double gtb = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gtb;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double gtb = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gtb;
   	   						}
   						}else{
   							if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited")){
	   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + LegalExpenses;
	   						}
	   						if(Recipient.equalsIgnoreCase("Zurich Insurance Plc")){
	   							double TotalCovers= calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + TotalCovers;
	   						}
	   						if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
	   							double TotalCovers= calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + TotalCovers;
	   						}
	   						if(Recipient.equalsIgnoreCase("Brokerage Account")){
	   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + LegalExpenses;
	   						}
	   						if(Recipient.equalsIgnoreCase((String)common.Renewal_excel_data_map.get("QC_AgencyName"))){
	   							double agentCommisin = calculateAgentTS(code,data_map,trasacSummaryType,j,td);	
	   							Total = Total + agentCommisin;
	   						}												
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
   					if(((String)common.CAN_excel_data_map.get("CAN_ExistingPolicy")).equalsIgnoreCase("Yes")) {
   						ExpecteTransactionDate = (String)common.CAN_excel_data_map.get("QuoteDate");
   					}else{
   						ExpecteTransactionDate = (String)common.NB_excel_data_map.get("QuoteDate");
   					}
   					
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
   	   						CommonFunction.compareValues(Math.abs(Double.parseDouble(actualTotal)),Math.abs(Double.parseDouble(common.roundedOff(Double.toString(Total)))), "Transaction Summary Total");
   	   						customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Transaction Summary", (String)common.CAN_excel_data_map.get("Automation Key"), "TS_TransactionSummaryTotal", actualTotal,common.CAN_excel_data_map),"Error while writing Transaction Summary data to excel .");

   	   						break outer;
   	   						}
   						String sectionName = null;
   						if(transactSumVal.equalsIgnoreCase("")){
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[3]")).getText();
   							sectionName = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   	   						td=8;
   						}else{
   							Recipient= driver.findElement(By.xpath(part1+"/tr["+j+"]/td[6]")).getText();
   							sectionName = driver.findElement(By.xpath(part1+"/tr["+j+"]/td[9]")).getText();
   	   						td=11;
   						}
   						
   						if(TestBase.product.equalsIgnoreCase("OED")){
   							if(Recipient.equalsIgnoreCase("Hcc International Insurance Company Plc") && sectionName.equalsIgnoreCase("Public Liability")){
   	   							double publicLiability = calculatePLTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + publicLiability;
   	   						}else if(Recipient.equalsIgnoreCase("Hcc International Insurance Company Plc") && sectionName.equalsIgnoreCase("Employers Liability")){
   	   							double employersLiability = calculateELTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + employersLiability;
   	   						}else if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited") && sectionName.equalsIgnoreCase("Personal Accident")){
   	   							double personalAcctident = calculatePATS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + personalAcctident;
   	   						}else if(Recipient.equalsIgnoreCase("Das Legal Expenses Insurance Company Limited") && sectionName.equalsIgnoreCase("Legal Expenses")){
   	   							double legalexpense = calculateLETS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + legalexpense;
   	   						}else if(Recipient.equalsIgnoreCase((String)data_map.get("QC_AgencyName"))){
   	   							double agentCommision = calculateBrokerTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + agentCommision;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + Math.abs(LegalExpenses);
   	   						}
   						}if(TestBase.product.equalsIgnoreCase("GTA")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double gta = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gta;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double gta = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gta;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTB")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double gtb = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gtb;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double gtb = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + gtb;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTC")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double cargo = calculateCargoTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + cargo;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double cargo = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + cargo;
   	   						}
   						}else if(TestBase.product.equalsIgnoreCase("GTD")){
   							if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   	   							double GIT = calculateGITTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + GIT;
   	   						}else if(Recipient.equalsIgnoreCase("Brokerage Account")){
   	   							double GIT = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   	   							Total = Total + GIT;
   	   						}
   						}else{
   							if(Recipient.equalsIgnoreCase("QBE Insurance (Europe) Limited")){
   						
   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   						if(Recipient.equalsIgnoreCase("Zurich Insurance Plc")){
   							double TotalCovers= calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + TotalCovers;
   						}
   						if(Recipient.equalsIgnoreCase("Royal & Sun Alliance Insurance Plc")){
   							double TotalCovers= calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + TotalCovers;
   						}
   						if(Recipient.equalsIgnoreCase("Brokerage Account")){
   							double LegalExpenses = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   						if(Recipient.equalsIgnoreCase((String)common.NB_excel_data_map.get("QC_AgencyName"))){
   							double agentCommisin = calculateAgentTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + agentCommisin;
   						}
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
	
	public double calculateCarsTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
            if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
            		try{
            			if(common_HHAZ.CARS_FP){
            				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Cars_FP").get("Net Net Premium"));
            				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Cars_FP").get("Insurance Tax"));
            			}else{
            				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Cars").get("Net Net Premium"));
            				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Cars").get("Insurance Tax"));
            			}
            		}catch(NullPointerException npe){
//            			Premium = Double.toString(common.transaction_Details_Premium_Values.get("Cars_FP").get("Net Net Premium"));
//            			IPT = Double.toString(common.transaction_Details_Premium_Values.get("Cars_FP").get("Insurance Tax"));
            			Premium = "0.0";
    					IPT = "0.0";
            		}
			}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Cars").get("Net Net Premium"));
					IPT = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Cars").get("Insurance Tax"));
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			if(common.currentRunningFlow.equals("NB")){
				
				if(transTbleValue){
				//if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Cars").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Cars").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_Cars_NetNetPremium");
					IPT = (String)data_map.get("PS_Cars_GT");
				}
			}
			if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
				
				if(common_HHAZ.CARS_FP){
    				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Cars_FP").get("Net Net Premium"));
    				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Cars_FP").get("Insurance Tax"));
    			}else if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
    				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Cars").get("Net Net Premium"));
    				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Cars").get("Insurance Tax"));
    			}else if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Cars").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Cars").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_Cars_NetNetPremium");
					IPT = (String)data_map.get("PS_Cars_GT");
				}
			}
			if(common.currentRunningFlow.equals("Requote")){
				
				if(common_VELA.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Cars").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Cars").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_Cars_NetNetPremium");
					IPT = (String)data_map.get("PS_Cars_GT");
				}
			}
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Cars").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Cars").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_Cars_NetNetPremium");
					IPT = (String)data_map.get("PS_Cars_GT");
				}
			}
			String part1= "//*[@id='table0']/tbody";
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+3)+"]")).getText();
			double Due = Double.parseDouble(Premium)+ Double.parseDouble(IPT);
			String Dueamt= common.roundedOff(Double.toString(Due)) ;
			CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "Cars Premium");
			return Due;
		
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
			k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Calculate Cars amount.  \n", t);
			return 0;
		}

	}
	public double calculateAgriVehiclesTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
            if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
            		try{
            			if(common_HHAZ.AV_FP){
            				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Agricultural Vehicles_FP").get("Net Net Premium"));
            				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Agricultural Vehicles_FP").get("Insurance Tax"));
            			}else{
            				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Agricultural Vehicles").get("Net Net Premium"));
            				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Agricultural Vehicles").get("Insurance Tax"));
            			}
            		}catch(NullPointerException npe){
            			Premium = "0.00";
            			IPT = "0.00";
            		}
			}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Agricultural Vehicles").get("Net Net Premium"));
					IPT = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Agricultural Vehicles").get("Insurance Tax"));
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			if(common.currentRunningFlow.equals("NB")){
				
				if(transTbleValue){
				//if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Agricultural Vehicles").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Agricultural Vehicles").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_Cars_NetNetPremium");
					IPT = (String)data_map.get("PS_Cars_GT");
				}
			}
			if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
				
				if(common_HHAZ.AV_FP){
    				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Agricultural Vehicles_FP").get("Net Net Premium"));
    				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Agricultural Vehicles_FP").get("Insurance Tax"));
    			}else if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
    				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Agricultural Vehicles").get("Net Net Premium"));
    				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Agricultural Vehicles").get("Insurance Tax"));
    			}else if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Agricultural Vehicles").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Agricultural Vehicles").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_AgriculturalVehicles_NetNetPremium");
					IPT = (String)data_map.get("PS_AgriculturalVehicles_GT");
				}
			}
			if(common.currentRunningFlow.equals("Requote")){
				
				if(common_VELA.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Agricultural Vehicles").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Agricultural Vehicles").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_AgriculturalVehicles_NetNetPremium");
					IPT = (String)data_map.get("PS_AgriculturalVehicles_GT");
				}
			}
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Agricultural Vehicles").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Agricultural Vehicles").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_AgriculturalVehicles_NetNetPremium");
					IPT = (String)data_map.get("PS_AgriculturalVehicles_GT");
				}
			}
			String part1= "//*[@id='table0']/tbody";
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+3)+"]")).getText();
			double Due = Double.parseDouble(Premium)+ Double.parseDouble(IPT);
			String Dueamt= common.roundedOff(Double.toString(Due)) ;
			CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "Agricultural Vehicles Premium");
			return Due;
		
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
			k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Calculate Agricultural Vehicles amount.  \n", t);
			return 0;
		}

	}
	public double calculateCommercialVehiclesTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
            if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
            		try{
            			if(common_HHAZ.CV_FP){
            				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Commercial Vehicles_FP").get("Net Net Premium"));
            				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Commercial Vehicles_FP").get("Insurance Tax"));
            			}else{
            				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Commercial Vehicles").get("Net Net Premium"));
            				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Commercial Vehicles").get("Insurance Tax"));
            			}
            		}catch(NullPointerException npe){
//            			Premium = Double.toString(common.transaction_Details_Premium_Values.get("Commercial Vehicles_FP").get("Net Net Premium"));
//            			IPT = Double.toString(common.transaction_Details_Premium_Values.get("Commercial Vehicles_FP").get("Insurance Tax"));
            			Premium = "0.0";
    					IPT = "0.0";
            		}
			}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Commercial Vehicles").get("Net Net Premium"));
					IPT = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Commercial Vehicles").get("Insurance Tax"));
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			if(common.currentRunningFlow.equals("NB")){
				
				if(transTbleValue){
				//if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Commercial Vehicles").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Commercial Vehicles").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_CommercialVehicles_NetNetPremium");
					IPT = (String)data_map.get("PS_CommercialVehicles_GT");
				}
			}
			if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
				
				if(common_HHAZ.CV_FP){
    				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Commercial Vehicles_FP").get("Net Net Premium"));
    				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Commercial Vehicles_FP").get("Insurance Tax"));
    			}else if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
    				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Commercial Vehicles").get("Net Net Premium"));
    				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Commercial Vehicles").get("Insurance Tax"));
    			}else if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Commercial Vehicles").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Commercial Vehicles").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_CommercialVehicles_NetNetPremium");
					IPT = (String)data_map.get("PS_CommercialVehicles_GT");
				}
			}
			if(common.currentRunningFlow.equals("Requote")){
				
				if(common_VELA.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Commercial Vehicles").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Commercial Vehicles").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_CommercialVehicles_NetNetPremium");
					IPT = (String)data_map.get("PS_CommercialVehicles_GT");
				}
			}
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Agricultural Vehicles").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Agricultural Vehicles").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_CommercialVehicles_NetNetPremium");
					IPT = (String)data_map.get("PS_CommercialVehicles_GT");
				}
			}
			String part1= "//*[@id='table0']/tbody";
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+3)+"]")).getText();
			double Due = Double.parseDouble(Premium)+ Double.parseDouble(IPT);
			String Dueamt= common.roundedOff(Double.toString(Due)) ;
			CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "Commercial Vehicles Premium");
			return Due;
		
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
			k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Calculate Commercial Vehicles amount.  \n", t);
			return 0;
		}

	}
	public double calculateSpecialTypesTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
            if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
            		try{
            			if(common_HHAZ.ST_FP){
            				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Special Types_FP").get("Net Net Premium"));
            				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Special Types_FP").get("Insurance Tax"));
            			}else{
            				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Special Types").get("Net Net Premium"));
            				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Special Types").get("Insurance Tax"));
            			}
            		}catch(NullPointerException npe){
            			Premium = "0.0";
    					IPT = "0.0";
            		}
			}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Special Types").get("Net Net Premium"));
					IPT = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Special Types").get("Insurance Tax"));
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			if(common.currentRunningFlow.equals("NB")){
				
				if(transTbleValue){
				//if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Special Types").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Special Types").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_SpecialTypes_NetNetPremium");
					IPT = (String)data_map.get("PS_SpecialTypes_GT");
				}
			}
			if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
				
				if(common_HHAZ.ST_FP){
    				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Special Types_FP").get("Net Net Premium"));
    				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Special Types_FP").get("Insurance Tax"));
    			}else if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
    				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Special Types").get("Net Net Premium"));
    				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Special Types").get("Insurance Tax"));
    			}else if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Special Types").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Special Types").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_SpecialTypes_NetNetPremium");
					IPT = (String)data_map.get("PS_SpecialTypes_GT");
				}
			}
			if(common.currentRunningFlow.equals("Requote")){
				
				if(common_VELA.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Special Types").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Special Types").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_SpecialTypes_NetNetPremium");
					IPT = (String)data_map.get("PS_SpecialTypes_GT");
				}
			}
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Special Types").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Special Types").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_SpecialTypes_NetNetPremium");
					IPT = (String)data_map.get("PS_SpecialTypes_GT");
				}
			}
			String part1= "//*[@id='table0']/tbody";
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+3)+"]")).getText();
			double Due = Double.parseDouble(Premium)+ Double.parseDouble(IPT);
			String Dueamt= common.roundedOff(Double.toString(Due)) ;
			CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "Special Types Premium");
			return Due;
		
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
			k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Calculate Special Types amount.  \n", t);
			return 0;
		}

	}
	public double calculateOtherTypesTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
            if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
            		try{
            			if(common_HHAZ.OT_FP){
            				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Other Types_FP").get("Net Net Premium"));
            				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Other Types_FP").get("Insurance Tax"));
            			}else{
            				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Other Types").get("Net Net Premium"));
            				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Other Types").get("Insurance Tax"));
            			}
            		}catch(NullPointerException npe){
//            			Premium = Double.toString(common.transaction_Details_Premium_Values.get("Other Types_FP").get("Net Net Premium"));
//            			IPT = Double.toString(common.transaction_Details_Premium_Values.get("Other Types_FP").get("Insurance Tax"));
            			Premium = "0.0";
    					IPT = "0.0";
            		}
			}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Other Types").get("Net Net Premium"));
					IPT = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Other Types").get("Insurance Tax"));
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			if(common.currentRunningFlow.equals("NB")){
				
				if(transTbleValue){
				//if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Other Types").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Other Types").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_OtherTypes_NetNetPremium");
					IPT = (String)data_map.get("PS_OtherTypes_GT");
				}
			}
			if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
				
				if(common_HHAZ.OT_FP){
    				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Other Types_FP").get("Net Net Premium"));
    				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Other Types_FP").get("Insurance Tax"));
    			}else if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
    				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Other Types").get("Net Net Premium"));
    				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Other Types").get("Insurance Tax"));
    			}else if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Other Types").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Other Types").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_OtherTypes_NetNetPremium");
					IPT = (String)data_map.get("PS_OtherTypes_GT");
				}
			}
			if(common.currentRunningFlow.equals("Requote")){
				
				if(common_VELA.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Other Types").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Other Types").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_OtherTypes_NetNetPremium");
					IPT = (String)data_map.get("PS_OtherTypes_GT");
				}
			}
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Agricultural Vehicles").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Agricultural Vehicles").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_OtherTypes_NetNetPremium");
					IPT = (String)data_map.get("PS_OtherTypes_GT");
				}
			}
			String part1= "//*[@id='table0']/tbody";
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+3)+"]")).getText();
			double Due = Double.parseDouble(Premium)+ Double.parseDouble(IPT);
			String Dueamt= common.roundedOff(Double.toString(Due)) ;
			CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "Other Types Premium");
			return Due;
		
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
			k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Calculate Other Types amount.  \n", t);
			return 0;
		}

	}
	public double calculateTradePlatesTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
            if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
            		try{
            			if(common_HHAZ.TP_FP){
            				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Trade Plates_FP").get("Net Net Premium"));
            				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Trade Plates_FP").get("Insurance Tax"));
            			}else{
            				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Trade Plate").get("Net Net Premium"));
            				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Trade Plate").get("Insurance Tax"));
            			}
            		}catch(NullPointerException npe){
//            			Premium = Double.toString(common.transaction_Details_Premium_Values.get("Trade Plates_FP").get("Net Net Premium"));
//            			IPT = Double.toString(common.transaction_Details_Premium_Values.get("Trade Plates_FP").get("Insurance Tax"));
            			Premium = "0.0";
    					IPT = "0.0";
            		}
			}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Trade Plate").get("Net Net Premium"));
					IPT = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Trade Plate").get("Insurance Tax"));
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			if(common.currentRunningFlow.equals("NB")){
				
				if(transTbleValue){
				//if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Trade Plate").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Trade Plate").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_TradePlate_NetNetPremium");
					IPT = (String)data_map.get("PS_TradePlate_GT");
				}
			}
			if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
				
				if(common_HHAZ.TP_FP){
    				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Trade Plate_FP").get("Net Net Premium"));
    				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Trade Plate_FP").get("Insurance Tax"));
    			}else if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
    				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Trade Plate").get("Net Net Premium"));
    				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Trade Plate").get("Insurance Tax"));
    			}else if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Trade Plate").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Trade Plate").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_TradePlate_NetNetPremium");
					IPT = (String)data_map.get("PS_TradePlate_GT");
				}
			}
			if(common.currentRunningFlow.equals("Requote")){
				
				if(common_VELA.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Trade Plate").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Trade Plate").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_TradePlate_NetNetPremium");
					IPT = (String)data_map.get("PS_TradePlate_GT");
				}
			}
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Trade Plate").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Trade Plate").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_TradePlate_NetNetPremium");
					IPT = (String)data_map.get("PS_TradePlate_GT");
				}
			}
			String part1= "//*[@id='table0']/tbody";
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+3)+"]")).getText();
			double Due = Double.parseDouble(Premium)+ Double.parseDouble(IPT);
			String Dueamt= common.roundedOff(Double.toString(Due)) ;
			CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "Trade Plate Premium");
			return Due;
		
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
			k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Calculate Trade Plates amount.  \n", t);
			return 0;
		}

	}
	public double calculateTrailersTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
            if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
            		try{
            			if(common_HHAZ.TR_FP){
            				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Trailers_FP").get("Net Net Premium"));
            				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Trailers_FP").get("Insurance Tax"));
            			}else{
            				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Trailers").get("Net Net Premium"));
            				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Trailers").get("Insurance Tax"));
            			}
            		}catch(NullPointerException npe){
//            			Premium = Double.toString(common.transaction_Details_Premium_Values.get("Trailers_FP").get("Net Net Premium"));
//            			IPT = Double.toString(common.transaction_Details_Premium_Values.get("Trailers_FP").get("Insurance Tax"));
            			Premium = "0.0";
    					IPT = "0.0";
            		}
			}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Trailers").get("Net Net Premium"));
					IPT = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Trailers").get("Insurance Tax"));
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			if(common.currentRunningFlow.equals("NB")){
				
				if(transTbleValue){
				//if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Trailers").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Trailers").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_Trailers_NetNetPremium");
					IPT = (String)data_map.get("PS_Trailers_GT");
				}
			}
			if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
				
				if(common_HHAZ.TR_FP){
    				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Trailers_FP").get("Net Net Premium"));
    				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Trailers_FP").get("Insurance Tax"));
    			}else if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
    				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Trailers").get("Net Net Premium"));
    				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Trailers").get("Insurance Tax"));
    			}else if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Trailers").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Trailers").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_Trailers_NetNetPremium");
					IPT = (String)data_map.get("PS_Trailers_GT");
				}
			}
			if(common.currentRunningFlow.equals("Requote")){
				
				if(common_VELA.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Trailers").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Trailers").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_Trailers_NetNetPremium");
					IPT = (String)data_map.get("PS_Trailers_GT");
				}
			}
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Trailers").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Trailers").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_Trailers_NetNetPremium");
					IPT = (String)data_map.get("PS_Trailers_GT");
				}
			}
			String part1= "//*[@id='table0']/tbody";
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+3)+"]")).getText();
			double Due = Double.parseDouble(Premium)+ Double.parseDouble(IPT);
			String Dueamt= common.roundedOff(Double.toString(Due)) ;
			CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "Trailers Premium");
			return Due;
		
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
			k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Calculate Trailers ammount.  \n", t);
			return 0;
		}

	}
	public double calculatePAOTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
            if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
            		try{
            			if(common_HHAZ.PAO_FP){
            				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Personal Accident Optional_FP").get("Net Net Premium"));
            				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Personal Accident Optional_FP").get("Insurance Tax"));
            			}else{
            				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Personal Accident Optional").get("Net Net Premium"));
            				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Personal Accident Optional").get("Insurance Tax"));
            			}
            		}catch(NullPointerException npe){
//            			Premium = Double.toString(common.transaction_Details_Premium_Values.get("Personal Accident Optional_FP").get("Net Net Premium"));
//            			IPT = Double.toString(common.transaction_Details_Premium_Values.get("Personal Accident Optional_FP").get("Insurance Tax"));
            			Premium = "0.0";
    					IPT = "0.0";
            		}
			}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Personal Accident Optional").get("Net Net Premium"));
					IPT = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Personal Accident Optional").get("Insurance Tax"));
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			if(common.currentRunningFlow.equals("NB")){
				
				if(transTbleValue){
				//if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Personal Accident Optional").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Personal Accident Optional").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_Personal Accident Optional_NetNetPremium");
					IPT = (String)data_map.get("PS_Personal Accident Optional_GT");
				}
			}
			if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
				
				if(common_HHAZ.PAO_FP){
    				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Personal Accident Optional_FP").get("Net Net Premium"));
    				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Personal Accident Optional_FP").get("Insurance Tax"));
    			}else if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
    				Premium = Double.toString(common.transaction_Details_Premium_Values.get("Personal Accident Optional").get("Net Net Premium"));
    				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Personal Accident Optional").get("Insurance Tax"));
    			}else if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Personal Accident Optional").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Personal Accident Optional").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_Personal Accident Optional_NetNetPremium");
					IPT = (String)data_map.get("PS_Personal Accident Optional_GT");
				}
			}
			if(common.currentRunningFlow.equals("Requote")){
				
				if(common_VELA.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Personal Accident Optional").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Personal Accident Optional").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_Personal Accident Optional_NetNetPremium");
					IPT = (String)data_map.get("PS_Personal Accident Optional_GT");
				}
			}
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Personal Accident Optional").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Personal Accident Optional").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_Personal Accident Optional_NetNetPremium");
					IPT = (String)data_map.get("PS_Personal Accident Optional_GT");
				}
			}
			String part1= "//*[@id='table0']/tbody";
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+3)+"]")).getText();
			double Due = Double.parseDouble(Premium)+ Double.parseDouble(IPT);
			String Dueamt= common.roundedOff(Double.toString(Due)) ;
			CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "Personal Accident Optional Premium");
			return Due;
		
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
			k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Calculate Personal Accident Optional ammount.  \n", t);
			return 0;
		}

	}
	public double calculatePLTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
            if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
            	try{
            	if(!PL_FP){
	            	Premium = Double.toString(common.transaction_Details_Premium_Values.get("Public Liability").get("Net Net Premium"));
					IPT = Double.toString(common.transaction_Details_Premium_Values.get("Public Liability").get("Insurance Tax"));
            	}else{
            		Premium = Double.toString(common.transaction_Details_Premium_Values.get("Public Liability_FP").get("Net Net Premium"));
            		IPT = Double.toString(common.transaction_Details_Premium_Values.get("Public Liability_FP").get("Insurance Tax"));
				}
            	}
            	catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
					}
			}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Public Liability").get("Net Net Premium"));
					IPT = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Public Liability").get("Insurance Tax"));
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			if(common.currentRunningFlow.equals("NB")){
				
				if(transTbleValue){
				//if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Public Liability").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Public Liability").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_PublicLiability_NetNetPremium");
					IPT = (String)data_map.get("PS_PublicLiability_GT");
				}
			}
			if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
				
				if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Public Liability").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Public Liability").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_PublicLiability_NetNetPremium");
					IPT = (String)data_map.get("PS_PublicLiability_GT");
				}
			}
			if(common.currentRunningFlow.equals("Requote")){
				
				if(common_VELA.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("PublicLiability").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("PublicLiability").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_PublicLiability_NetNetPremium");
					IPT = (String)data_map.get("PS_PublicLiability_GT");
				}
			}
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Public Liability").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Public Liability").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_PublicLiability_NetNetPremium");
					IPT = (String)data_map.get("PS_PublicLiability_GT");
				}
			}
			String part1= "//*[@id='table0']/tbody";
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+3)+"]")).getText();
			double Due = Double.parseDouble(Premium)+ Double.parseDouble(IPT);
			String Dueamt= common.roundedOff(Double.toString(Due)) ;
			CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "Hcc International Insurance Company Plc");
			return Due;
		
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
			k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Calculate Legal Expenses ammount.  \n", t);
			return 0;
		}

	}
	public double calculateELTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
            if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
				try{
					if(!EL_FP){
		            	Premium = Double.toString(common.transaction_Details_Premium_Values.get("Employers Liability").get("Net Net Premium"));
						IPT = Double.toString(common.transaction_Details_Premium_Values.get("Employers Liability").get("Insurance Tax"));
					}else{	
						Premium = Double.toString(common.transaction_Details_Premium_Values.get("Employers Liability_FP").get("Net Net Premium"));
						IPT = Double.toString(common.transaction_Details_Premium_Values.get("Employers Liability_FP").get("Insurance Tax"));
					}
					}catch(NullPointerException npe){
						Premium = "0.0";
						IPT = "0.0";
					}
				}
			
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Employers Liability").get("Net Net Premium"));
					IPT = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Employers Liability").get("Insurance Tax"));
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			if(common.currentRunningFlow.equals("NB")){
				
				if(common_HHAZ.transTbleValue){
				//if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Employers Liability").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Employers Liability").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_EmployersLiability_NetNetPremium");
					IPT = (String)data_map.get("PS_EmployersLiability_GT");
				}
			}
			if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
				
				if(common_HHAZ.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Employers Liability").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Employers Liability").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_EmployersLiability_NetNetPremium");
					IPT = (String)data_map.get("PS_EmployersLiability_GT");
				}
			}
			if(common.currentRunningFlow.equals("Requote")){
				
				if(common_HHAZ.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("EmployersLiability").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("EmployersLiability").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_EmployersLiability_NetNetPremium");
					IPT = (String)data_map.get("PS_EmployersLiability_GT");
				}
			}
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(common_HHAZ.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Employers Liability").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Employers Liability").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_EmployersLiability_NetNetPremium");
					IPT = (String)data_map.get("PS_EmployersLiability_GT");
				}
			}
			String part1= "//*[@id='table0']/tbody";
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+2)+"]")).getText();
			double Due = Double.parseDouble(Premium)+ Double.parseDouble(IPT);
			String Dueamt= common.roundedOff(Double.toString(Due)) ;
			CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "Hcc International Insurance Company Plc");
			return Due;
		
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
			k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Calculate Legal Expenses ammount.  \n", t);
			return 0;
		}

	}
	public double calculatePATS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
            if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
            	try{
            		if(!PA_FP){            		
		            	Premium = Double.toString(common.transaction_Details_Premium_Values.get("Personal Accident").get("Net Net Premium"));
						IPT = Double.toString(common.transaction_Details_Premium_Values.get("Personal Accident").get("Insurance Tax"));
            		}else{
						Premium = Double.toString(common.transaction_Details_Premium_Values.get("Personal Accident_FP").get("Net Net Premium"));
						IPT = Double.toString(common.transaction_Details_Premium_Values.get("Personal Accident_FP").get("Insurance Tax"));
					}
            	}catch(NullPointerException npe){
            		Premium = "0.0";
					IPT = "0.0";
				}
            	
			}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Personal Accident").get("Net Net Premium"));
					IPT = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Personal Accident").get("Insurance Tax"));
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			if(common.currentRunningFlow.equals("NB")){
				
				if(common_HHAZ.transTbleValue){
				//if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Personal Accident").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Personal Accident").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_PersonalAccident_NetNetPremium");
					IPT = (String)data_map.get("PS_PersonalAccident_GT");
				}
			}
			if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
				
				if(common_HHAZ.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Personal Accident").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Personal Accident").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_PersonalAccident_NetNetPremium");
					IPT = (String)data_map.get("PS_PersonalAccident_GT");
				}
			}
			if(common.currentRunningFlow.equals("Requote")){
				
				if(common_HHAZ.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("PersonalAccident").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("PersonalAccident").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_PersonalAccident_NetNetPremium");
					IPT = (String)data_map.get("PS_PersonalAccident_GT");
				}
			}
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(common_HHAZ.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Personal Accident").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Personal Accident").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_PersonalAccident_NetNetPremium");
					IPT = (String)data_map.get("PS_PersonalAccident_GT");
				}
			}
			String part1= "//*[@id='table0']/tbody";
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+2)+"]")).getText();
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
	public double calculateLETS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
			 if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
	            	try{
	            		if(!LE_FP){            		
			            	Premium = Double.toString(common.transaction_Details_Premium_Values.get("Legal Expenses").get("Net Net Premium"));
							IPT = Double.toString(common.transaction_Details_Premium_Values.get("Legal Expenses").get("Insurance Tax"));
	            		}else{
							Premium = Double.toString(common.transaction_Details_Premium_Values.get("Legal Expenses_FP").get("Net Net Premium"));
							IPT = Double.toString(common.transaction_Details_Premium_Values.get("Legal Expenses_FP").get("Insurance Tax"));
						}
	            	}catch(NullPointerException npe){
	            		Premium = "0.0";
						IPT = "0.0";
					}
	            	
				}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Legal Expenses").get("Net Net Premium"));
					IPT = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Legal Expenses").get("Insurance Tax"));
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			if(common.currentRunningFlow.equals("NB")){
				
				if(common_HHAZ.transTbleValue){
				//if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Legal Expenses").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Legal Expenses").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_LegalExpenses_NetNetPremium");
					IPT = (String)data_map.get("PS_LegalExpenses_GT");
				}
			}
			if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
				
				if(common_HHAZ.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Legal Expenses").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Legal Expenses").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_LegalExpenses_NetNetPremium");
					IPT = (String)data_map.get("PS_LegalExpenses_GT");
				}
			}
			if(common.currentRunningFlow.equals("Requote")){
				
				if(common_HHAZ.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("LegalExpenses").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("LegalExpenses").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_LegalExpenses_NetNetPremium");
					IPT = (String)data_map.get("PS_LegalExpenses_GT");
				}
			}
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(common_HHAZ.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Legal Expenses").get("Net Net Premium"));
					IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Legal Expenses").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_LegalExpenses_NetNetPremium");
					IPT = (String)data_map.get("PS_LegalExpenses_GT");
				}
			}
			String part1= "//*[@id='table0']/tbody";
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+2)+"]")).getText();
			double Due = Double.parseDouble(Premium)+ Double.parseDouble(IPT);
			String Dueamt= common.roundedOff(Double.toString(Due)) ;
			CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "Das Legal Expenses Insurance Company Limited");
			return Due;
		
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
			k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Calculate Legal Expenses ammount.  \n", t);
			return 0;
		}

	}
	public double calculateBrokerTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
            if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
				
            	Premium = Double.toString(common_VELA.transaction_Details_Premium_Values.get("Totals").get("Broker Commission"));
			}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Broker Commission"));

				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			if(common.currentRunningFlow.equals("NB")){
					
				if(common_HHAZ.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Totals").get("Broker Commission"));
				
				}else{
					Premium = (String)data_map.get("PS_BrokerCommissionTotal");
				}
			}
			if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA") ){
				
				if(common_HHAZ.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Totals").get("Broker Commission"));
				
				}else{
					Premium = (String)data_map.get("PS_BrokerCommissionTotal");
				}
			}
			if(common.currentRunningFlow.equals("Requote")){
				
				if(common_HHAZ.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Totals").get("Broker Commission"));
				
				}else{
					Premium = (String)data_map.get("PS_BrokerCommissionTotal");
				}
			}
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(common_HHAZ.transTbleValue){
					Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Totals").get("Broker Commission"));
				
				}else{
					Premium = (String)data_map.get("PS_BrokerCommissionTotal");
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
		
	public double calculateCarrierTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
			String smsg = "QBE Insurance";
			if(TestBase.product.equals("MFB")||TestBase.product.equals("MFC")){
				smsg="Zurich Insurance Plc";
			}
			
			if(TestBase.product.equals("CMA")){
				smsg="	Royal & Sun Alliance Insurance Plc";
			}
			
            if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
            	try{
	            	if(TestBase.product.equals("MFB")||TestBase.product.equals("MFC")){
						String p1= "//*[@id='table0']/tbody";
						int sc;
						if(j>1){
							sc=6;
							
						}else{
							sc=9;
						}
						String section = driver.findElement(By.xpath(p1+"/tr["+j+"]/td["+sc+"]")).getText();
						
						
						
							//section = section.replaceAll(" ", "");
							
							if(section.contains("Trade"))
								section="Trade Plate";
							
							if(section.contains("Commercial"))
								section="Commercial Vehicles";
							
							Premium = Double.toString(common.transaction_Details_Premium_Values.get(section).get("Net Net Premium"));
							IPT = Double.toString(common.transaction_Details_Premium_Values.get(section).get("Insurance Tax"));
						
						
						
					}else{
	            	
		            	Premium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Net Net Premium"));
						IPT = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax"));
					}
            	}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}	
			}
            else if(common.currentRunningFlow.equals("CAN")){
				
				try{
					if(TestBase.product.equals("MFB")||TestBase.product.equals("MFC")){
						String p1= "//*[@id='table0']/tbody";
						int sc;
						if(j>1){
							sc=6;
							
						}else{
							sc=9;
						}
						String section = driver.findElement(By.xpath(p1+"/tr["+j+"]/td["+sc+"]")).getText();
						
						
												
							if(section.contains("Trade"))
								section="Trade Plate";
							
							if(section.contains("Commercial"))
								section="Commercial Vehicles";
							
							Premium = "-"+Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get(section).get("Net Net Premium"));
							IPT = "-"+Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get(section).get("Insurance Tax"));
						
												
					}else{
						String p1= "//*[@id='table0']/tbody";
						String section = driver.findElement(By.xpath(p1+"/tr["+j+"]/td["+td+"]")).getText();
						//if(TestBase.product.equals("CMA")){
							section="Totals";
						//}
						Premium = "-"+Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get(section).get("Net Net Premium"));
						IPT = "-"+Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get(section).get("Insurance Tax"));
					}
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
            else if(common.currentRunningFlow.equals("NB")){
				
				if(TestBase.product.equals("MFB")||TestBase.product.equals("MFC")){
					String p1= "//*[@id='table0']/tbody";
					int sc;
					if(j>1){
						sc=6;
						
					}else{
						sc=9;
					}
					String section = driver.findElement(By.xpath(p1+"/tr["+j+"]/td["+sc+"]")).getText();
					
					
					if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
						
						if(section.contains("Trade"))
							section="Trade Plate";
						
						if(section.contains("Commercial"))
							section="Commercial Vehicles";
						
						Premium = Double.toString(transaction_Premium_Values.get(section).get("Net Net Premium"));
						IPT = Double.toString(transaction_Premium_Values.get(section).get("Insurance Tax"));
					
					}else{
						section = section.replaceAll(" ", "");
						
						if(section.contains("Trade"))
							section="TradePlate";
						
						if(section.contains("Commercial"))
							section="CommercialVehicles";
						
						
						Premium = (String)data_map.get("PS_"+section+"_NetNetPremium");
						IPT = (String)data_map.get("PS_"+section+"_GT");
					}
					
					
				}else{
						
					if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
						Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Net Net Premium"));
						IPT = Double.toString(transaction_Premium_Values.get("Totals").get("Insurance Tax"));
					
					}else{
						Premium = (String)data_map.get("PS_NetNetPemiumTotal");
						IPT = (String)data_map.get("PS_Total_GT");
					}
				}
			}
            else if(common.currentRunningFlow.equals("Rewind")){
				try{
					if(TestBase.product.equals("MFB")||TestBase.product.equals("MFC")){
						String p1= "//*[@id='table0']/tbody";
						int sc;
						if(j>1){
							sc=6;
							
						}else{
							sc=9;
						}
						String section = driver.findElement(By.xpath(p1+"/tr["+j+"]/td["+sc+"]")).getText();
						
						
						if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
							
							if(section.contains("Trade"))
								section="Trade Plate";
							
							if(section.contains("Commercial"))
								section="Commercial Vehicles";
							
							Premium = Double.toString(transaction_Premium_Values.get(section).get("Net Net Premium"));
							IPT = Double.toString(transaction_Premium_Values.get(section).get("Insurance Tax"));
						
						}else{
							section = section.replaceAll(" ", "");
							
							if(section.contains("Trade"))
								section="TradePlate";
							
							if(section.contains("Commercial"))
								section="CommercialVehicles";
							
							
							Premium = (String)data_map.get("PS_"+section+"_NetNetPremium");
							IPT = (String)data_map.get("PS_"+section+"_GT");
						}
						
						
					}else{
						
						if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
							Premium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Net Net Premium"));
							IPT = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax"));
						}else{
							if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
								Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Net Net Premium"));
								IPT = Double.toString(transaction_Premium_Values.get("Totals").get("Insurance Tax"));
							
							}else{
								Premium = (String)data_map.get("PS_NetNetPemiumTotal");
								IPT = (String)data_map.get("PS_Total_GT");
							}
						}
					
						
					}
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			/*if(common.currentRunningFlow.equals("Rewind") && TestBase.businessEvent.equalsIgnoreCase("Rewind")){
				try{
					if(TestBase.product.equals("MFB")||TestBase.product.equals("MFC")){
						String p1= "//*[@id='table0']/tbody";
						int sc;
						if(j>1){
							sc=6;
							
						}else{
							sc=9;
						}
						String section = driver.findElement(By.xpath(p1+"/tr["+j+"]/td["+sc+"]")).getText();
						
						
						
							//section = section.replaceAll(" ", "");
							
							if(section.contains("Trade"))
								section="Trade Plate";
							
							if(section.contains("Commercial"))
								section="Commercial Vehicles";
							
							Premium = Double.toString(common.transaction_Details_Premium_Values.get(section).get("Net Net Premium"));
							IPT = Double.toString(common.transaction_Details_Premium_Values.get(section).get("Insurance Tax"));
						
						
						
					}else{
	            	
		            	Premium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Net Net Premium"));
						IPT = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax"));
					}
					
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}			
			}*/
            else if(common.currentRunningFlow.equals("Rewind") && TestBase.businessEvent.equalsIgnoreCase("MTA")){
				try{
					if(TestBase.product.equals("MFB")||TestBase.product.equals("MFC")){
						String p1= "//*[@id='table0']/tbody";
						int sc;
						if(j>1){
							sc=6;
							
						}else{
							sc=9;
						}
						String section = driver.findElement(By.xpath(p1+"/tr["+j+"]/td["+sc+"]")).getText();
						
						
						
							//section = section.replaceAll(" ", "");
							
							if(section.contains("Trade"))
								section="Trade Plate";
							
							if(section.contains("Commercial"))
								section="Commercial Vehicles";
							
							Premium = Double.toString(common.transaction_Details_Premium_Values.get(section).get("Net Net Premium"));
							IPT = Double.toString(common.transaction_Details_Premium_Values.get(section).get("Insurance Tax"));
						
						
						
					}else{
	            	
		            	Premium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Net Net Premium"));
						IPT = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax"));
					}
					
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}			
			}
            else if(common.currentRunningFlow.equals("Requote")){
				
				if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Net Net Premium"));
					IPT = Double.toString(transaction_Premium_Values.get("Totals").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_NetNetPemiumTotal");
					IPT = (String)data_map.get("PS_Total_GT");
				}
			}
            else if(common.currentRunningFlow.equals("Renewal")){
				try{
					if(TestBase.product.equals("MFB")||TestBase.product.equals("MFC")){
						String p1= "//*[@id='table0']/tbody";
						int sc;
						if(j>1){
							sc=6;
							
						}else{
							sc=9;
						}
						String section = driver.findElement(By.xpath(p1+"/tr["+j+"]/td["+sc+"]")).getText();
						
						
						if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
							
							if(section.contains("Trade"))
								section="Trade Plate";
							
							if(section.contains("Commercial"))
								section="Commercial Vehicles";
							
							Premium = Double.toString(transaction_Premium_Values.get(section).get("Net Net Premium"));
							IPT = Double.toString(transaction_Premium_Values.get(section).get("Insurance Tax"));
						
						}else{
							section = section.replaceAll(" ", "");
							
							if(section.contains("Trade"))
								section="TradePlate";
							
							if(section.contains("Commercial"))
								section="CommercialVehicles";
							
							
							Premium = (String)data_map.get("PS_"+section+"_NetNetPremium");
							IPT = (String)data_map.get("PS_"+section+"_GT");
						}
						
						
					}else{
							
						if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
							Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Net Net Premium"));
							IPT = Double.toString(transaction_Premium_Values.get("Totals").get("Insurance Tax"));
						
						}else{
							Premium = (String)data_map.get("PS_NetNetPemiumTotal");
							IPT = (String)data_map.get("PS_Total_GT");
						}
					}
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
            else if(common.currentRunningFlow.equals("Requote")){
				
				if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Net Net Premium"));
					IPT = Double.toString(transaction_Premium_Values.get("Totals").get("Insurance Tax"));
				
				}else{
					Premium = (String)data_map.get("PS_NetNetPemiumTotal");
					IPT = (String)data_map.get("PS_Total_GT");
				}
			}
			
			String part1= "//*[@id='table0']/tbody";
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+3)+"]")).getText();
			double Due = Double.parseDouble(Premium)+ Double.parseDouble(IPT);
			String Dueamt= common.roundedOff(Double.toString(Due)) ;
			CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "Due amount of "+smsg);
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
					Premium = "-"+ Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Pen Comm"));

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
			if(common.currentRunningFlow.equals("Rewind") && TestBase.businessEvent.equalsIgnoreCase("Rewind")){
				
				if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
					Premium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Pen Comm"));
				}else{
					if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
						Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Pen Comm"));
					
					}else{
						Premium = (String)data_map.get("PS_PenCommTotal");
					}
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
	
	public double calculateAgentTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
            if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
				
            	Premium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Broker Commission"));
			}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Broker Commission"));

				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
			if(common.currentRunningFlow.equals("NB")){
					
				if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Broker Commission"));
				
				}else{
					Premium = (String)data_map.get("PS_BrokerCommissionTotal");
				}
			}
			if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA") ){
				
				if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Broker Commission"));
				
				}else{
					Premium = (String)data_map.get("PS_BrokerCommissionTotal");
				}
			}
			if(common.currentRunningFlow.equals("Requote")){
				
				if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Broker Commission"));
				
				}else{
					Premium = (String)data_map.get("PS_BrokerCommissionTotal");
				}
			}
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Broker Commission"));
				
				}else{
					Premium = (String)data_map.get("PS_BrokerCommissionTotal");
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
					if(element.isDisplayed()){
							// This is just to check if Bonafied value on Public Liabiliy screen for CCC and CCD product is "Yes" or not.
							// Sometimes If Bonafied contains hiddedn "Delete" button then it is not appearing on page and it is failing. So to handle this case below code will help.
							try{
								String headerName = driver.findElement(By.xpath("html/body/div[3]/form/p")).getText();
								
								if(headerName.equalsIgnoreCase("Public Liability")){
									WebElement bonafiedValue = driver.findElement(By.xpath("//*[contains(@name,'bona_fide_subcontractors')]"));
									JavascriptExecutor j_exe = (JavascriptExecutor) driver;
									j_exe.executeScript("arguments[0].scrollIntoView(true);", bonafiedValue);
									Select mySelect = new Select(bonafiedValue);
									mySelect.selectByValue("Yes");
								}
							}catch(Throwable t){
								
							}
							
						element = driver.findElement(By.xpath("//*[text()='Delete']"));
						JavascriptExecutor j_exe = (JavascriptExecutor) driver;
						j_exe.executeScript("arguments[0].scrollIntoView(true);", element);
						element.click();
						WebDriverWait wait = new WebDriverWait(driver, 3);
						if(wait.until(ExpectedConditions.alertIsPresent())!=null){
							k.AcceptPopup();
						}
						
					}else{
						continue;
					}
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
			case "Endorsement Submitted":
				code = "";
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
			
			
			switch (TestBase.product){
			case "COA":
				if(((String)map_data.get("CD_"+code+"Public&ProductsLiability")).equalsIgnoreCase("Yes")){
					int bespokeCount = 0;
					int count = 0;
					String[] properties = ((String)map_data.get("PPL_AddBespoke")).split(";");
		            int no_of_property = properties.length;
					
					
					/*if(common.no_of_inner_data_sets.get("AddBespokePPL")==null){
						bespokeCount = 0;
					}else{
						bespokeCount = common.no_of_inner_data_sets.get("AddBespokePPL");
					}*/
					while(count < no_of_property){
						String additionalCover = map_InnerPages.get("AddBespokePPL").get(count).get("PPL_AddB_Additionalcover");
						EndorsementIndividualData = new LinkedHashMap<String, String>();
						switch (additionalCover){
						case "Asbestos":
							String ED_Code = map_InnerPages.get("AutoAddedEndorsement").get(0).get("ED_AutoCode");
							System.out.println(EndorsementCollectiveData);
							if(!EndorsementCollectiveData.containsKey(ED_Code)){
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Code", map_InnerPages.get("AutoAddedEndorsement").get(0).get("ED_AutoCode"));
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Section", map_InnerPages.get("AutoAddedEndorsement").get(0).get("ED_AutoSection"));
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Title", map_InnerPages.get("AutoAddedEndorsement").get(0).get("ED_AutoTitle").trim());
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(0).get("ED_AutoCaluseType"));
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Type", map_InnerPages.get("AutoAddedEndorsement").get(0).get("ED_AutoType"));
								EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+additionalCover.replaceAll(" ", "")+"_Code"), EndorsementIndividualData);
							}
							break;
						case "CPA Contract Lift Cover(Lifted Goods)":
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Code", map_InnerPages.get("AutoAddedEndorsement").get(1).get("ED_AutoCode"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Section", map_InnerPages.get("AutoAddedEndorsement").get(1).get("ED_AutoSection"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Title", map_InnerPages.get("AutoAddedEndorsement").get(1).get("ED_AutoTitle").trim());
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(1).get("ED_AutoCaluseType"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Type", map_InnerPages.get("AutoAddedEndorsement").get(1).get("ED_AutoType"));
							EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+additionalCover.replaceAll(" ", "")+"_Code"), EndorsementIndividualData);
							break;
						case "Servicing Indemnity":
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Code", map_InnerPages.get("AutoAddedEndorsement").get(2).get("ED_AutoCode"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Section", map_InnerPages.get("AutoAddedEndorsement").get(2).get("ED_AutoSection"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Title", map_InnerPages.get("AutoAddedEndorsement").get(2).get("ED_AutoTitle").trim());
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(2).get("ED_AutoCaluseType"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Type", map_InnerPages.get("AutoAddedEndorsement").get(2).get("ED_AutoType"));
							EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+additionalCover.replaceAll(" ", "")+"_Code"), EndorsementIndividualData);
							break;
						}
						count++;
					}
				}
				if(((String)map_data.get("CD_"+code+"EmployersLiability")).equalsIgnoreCase("Yes")){
					int bespokeCount = 0;
					int count = 0;
					String[] properties = ((String)map_data.get("EL_AddBespokeEL")).split(";");
		            int no_of_property = properties.length;
					/*if(common.no_of_inner_data_sets.get("AddBespokeEL")==null){
						bespokeCount = 0;
					}else{
						bespokeCount = common.no_of_inner_data_sets.get("AddBespokeEL");
					}*/
					while(count < no_of_property){
						String additionalCover = map_InnerPages.get("AddBespokeEL").get(count).get("EL_AddB_Additionalcover");
						EndorsementIndividualData = new LinkedHashMap<String, String>();
						switch (additionalCover) {
						case "Asbestos":
							String ED_Code = map_InnerPages.get("AutoAddedEndorsement").get(3).get("ED_AutoCode");
							System.out.println(EndorsementCollectiveData);
							if(!EndorsementCollectiveData.containsKey(ED_Code)){
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Code", map_InnerPages.get("AutoAddedEndorsement").get(3).get("ED_AutoCode"));
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Section", map_InnerPages.get("AutoAddedEndorsement").get(3).get("ED_AutoSection"));
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Title", map_InnerPages.get("AutoAddedEndorsement").get(3).get("ED_AutoTitle"));
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(3).get("ED_AutoCaluseType"));
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Type", map_InnerPages.get("AutoAddedEndorsement").get(3).get("ED_AutoType"));
								EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+additionalCover.replaceAll(" ", "")+"_Code"), EndorsementIndividualData);
							}
							break;
						case "Offshore Work & Visits":
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Code", map_InnerPages.get("AutoAddedEndorsement").get(4).get("ED_AutoCode"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Section", map_InnerPages.get("AutoAddedEndorsement").get(4).get("ED_AutoSection"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Title", map_InnerPages.get("AutoAddedEndorsement").get(4).get("ED_AutoTitle"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(4).get("ED_AutoCaluseType"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Type", map_InnerPages.get("AutoAddedEndorsement").get(4).get("ED_AutoType"));
							EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+additionalCover.replaceAll(" ", "")+"_Code"), EndorsementIndividualData);
							break;
						case "Overseas Work":
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Code", map_InnerPages.get("AutoAddedEndorsement").get(5).get("ED_AutoCode"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Section", map_InnerPages.get("AutoAddedEndorsement").get(5).get("ED_AutoSection"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Title", map_InnerPages.get("AutoAddedEndorsement").get(5).get("ED_AutoTitle"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(5).get("ED_AutoCaluseType"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Type", map_InnerPages.get("AutoAddedEndorsement").get(5).get("ED_AutoType"));
							EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+additionalCover.replaceAll(" ", "")+"_Code"), EndorsementIndividualData);
							break;
						}
						count++;
					}
				}
				if(((String)map_data.get("CD_"+code+"OwnPlant")).equalsIgnoreCase("Yes")){
					int bespokeCount = 0;
					int count = 0;
					String[] properties = ((String)map_data.get("OP_AddBespokeItem")).split(";");
		            int no_of_property = properties.length;
		            
					/*if(common.no_of_inner_data_sets.get("AddBespokeOP")==null){
						bespokeCount = 0;
					}else{
						bespokeCount = common.no_of_inner_data_sets.get("AddBespokeOP");
					}*/
					while(count < no_of_property){
						String additionalCover = map_InnerPages.get("AddBespokeOP").get(count).get("OP_AddB_Additionalcover");
						EndorsementIndividualData = new LinkedHashMap<String, String>();
						switch (additionalCover) {
						case "CPA Contract Lift Cover(Lifted Goods)":
							String ED_Code = map_InnerPages.get("AutoAddedEndorsement").get(6).get("ED_AutoCode");
							if(!EndorsementCollectiveData.containsKey(ED_Code)){
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Code", map_InnerPages.get("AutoAddedEndorsement").get(6).get("ED_AutoCode"));
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Section", map_InnerPages.get("AutoAddedEndorsement").get(6).get("ED_AutoSection"));
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Title", map_InnerPages.get("AutoAddedEndorsement").get(6).get("ED_AutoTitle"));
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(6).get("ED_AutoCaluseType"));
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Type", map_InnerPages.get("AutoAddedEndorsement").get(6).get("ED_AutoType"));
								EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+additionalCover.replaceAll(" ", "")+"_Code"), EndorsementIndividualData);
							}
							break;
						case "Subrogation Waiver":
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Code", map_InnerPages.get("AutoAddedEndorsement").get(16).get("ED_AutoCode"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Section", map_InnerPages.get("AutoAddedEndorsement").get(16).get("ED_AutoSection"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Title", map_InnerPages.get("AutoAddedEndorsement").get(16).get("ED_AutoTitle"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(16).get("ED_AutoCaluseType"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Type", map_InnerPages.get("AutoAddedEndorsement").get(16).get("ED_AutoType"));
							EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+additionalCover.replaceAll(" ", "")+"_Code"), EndorsementIndividualData);
							break;
						}
						count++;
					}
					String basisOfCover = (String)map_data.get("OP_PolicyBasis");
					EndorsementIndividualData = new LinkedHashMap<String, String>();
					switch (basisOfCover) {
					case "On Schedule":
						EndorsementIndividualData.put("ED_"+basisOfCover.replaceAll(" ", "")+"(BasisofCover)_Code", map_InnerPages.get("AutoAddedEndorsement").get(7).get("ED_AutoCode"));
						EndorsementIndividualData.put("ED_"+basisOfCover.replaceAll(" ", "")+"(BasisofCover)_Section", map_InnerPages.get("AutoAddedEndorsement").get(7).get("ED_AutoSection"));
						EndorsementIndividualData.put("ED_"+basisOfCover.replaceAll(" ", "")+"(BasisofCover)_Title", map_InnerPages.get("AutoAddedEndorsement").get(7).get("ED_AutoTitle"));
						EndorsementIndividualData.put("ED_"+basisOfCover.replaceAll(" ", "")+"(BasisofCover)_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(7).get("ED_AutoCaluseType"));
						EndorsementIndividualData.put("ED_"+basisOfCover.replaceAll(" ", "")+"(BasisofCover)_Type", map_InnerPages.get("AutoAddedEndorsement").get(7).get("ED_AutoType"));
						EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+basisOfCover.replaceAll(" ", "")+"(BasisofCover)_Code"), EndorsementIndividualData);
						break;
					case "Specified":
						EndorsementIndividualData.put("ED_"+basisOfCover.replaceAll(" ", "")+"(BasisofCover)_Code", map_InnerPages.get("AutoAddedEndorsement").get(15).get("ED_AutoCode"));
						EndorsementIndividualData.put("ED_"+basisOfCover.replaceAll(" ", "")+"(BasisofCover)_Section", map_InnerPages.get("AutoAddedEndorsement").get(15).get("ED_AutoSection"));
						EndorsementIndividualData.put("ED_"+basisOfCover.replaceAll(" ", "")+"(BasisofCover)_Title", map_InnerPages.get("AutoAddedEndorsement").get(15).get("ED_AutoTitle"));
						EndorsementIndividualData.put("ED_"+basisOfCover.replaceAll(" ", "")+"(BasisofCover)_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(15).get("ED_AutoCaluseType"));
						EndorsementIndividualData.put("ED_"+basisOfCover.replaceAll(" ", "")+"(BasisofCover)_Type", map_InnerPages.get("AutoAddedEndorsement").get(15).get("ED_AutoType"));
						EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+basisOfCover.replaceAll(" ", "")+"(BasisofCover)_Code"), EndorsementIndividualData);
						break;
					default:
						break;
					}
				}
				if(((String)map_data.get("CD_"+code+"HiredInPlant")).equalsIgnoreCase("Yes")){
					int bespokeCount = 0;
					int count = 0;
					String[] properties = ((String)map_data.get("HIP_AddBespokeItem")).split(";");
		            int no_of_property = properties.length;
		            
					/*if(common.no_of_inner_data_sets.get("AddBespokeHIP")==null){
						bespokeCount = 0;
					}else{
						bespokeCount = common.no_of_inner_data_sets.get("AddBespokeHIP");
					}*/
					while(count < no_of_property){
						String additionalCover = map_InnerPages.get("AddBespokeHIP").get(count).get("HIP_AddB_Additionalcover");
						EndorsementIndividualData = new LinkedHashMap<String, String>();
						switch (additionalCover) {
						case "CPA Contract Lift Cover(Lifted Goods)":
							String ED_Code = map_InnerPages.get("AutoAddedEndorsement").get(9).get("ED_AutoCode");
							if(!EndorsementCollectiveData.containsKey(ED_Code)){
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Code", map_InnerPages.get("AutoAddedEndorsement").get(9).get("ED_AutoCode"));
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Section", map_InnerPages.get("AutoAddedEndorsement").get(9).get("ED_AutoSection"));
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Title", map_InnerPages.get("AutoAddedEndorsement").get(9).get("ED_AutoTitle"));
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(9).get("ED_AutoCaluseType"));
								EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Type", map_InnerPages.get("AutoAddedEndorsement").get(9).get("ED_AutoType"));
								EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+additionalCover.replaceAll(" ", "")+"_Code"), EndorsementIndividualData);
							}
							break;
						}
						count++;
					}
				}
				if(((String)map_data.get("CD_"+code+"AnnualWorks")).equalsIgnoreCase("Yes")){
					int bespokeCount = 0;
					int count = 0;
					String[] properties = ((String)map_data.get("AW_AddBespoke")).split(";");
		            int no_of_property = properties.length;
		            
					/*if(common.no_of_inner_data_sets.get("AddBespokeAW")==null){
						bespokeCount = 0;
					}else{
						bespokeCount = common.no_of_inner_data_sets.get("AddBespokeAW");
					}*/
					while(count < no_of_property){
						String additionalCover = map_InnerPages.get("AddBespokeAW").get(count).get("AW_AddB_Additionalcover");
						EndorsementIndividualData = new LinkedHashMap<String, String>();
						switch (additionalCover) {
						case "DE5":
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Code", map_InnerPages.get("AutoAddedEndorsement").get(10).get("ED_AutoCode"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Section", map_InnerPages.get("AutoAddedEndorsement").get(10).get("ED_AutoSection"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Title", map_InnerPages.get("AutoAddedEndorsement").get(10).get("ED_AutoTitle"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(10).get("ED_AutoCaluseType"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Type", map_InnerPages.get("AutoAddedEndorsement").get(10).get("ED_AutoType"));
							EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+additionalCover.replaceAll(" ", "")+"_Code"), EndorsementIndividualData);
						break;
						case "Existing Structures":
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Code", map_InnerPages.get("AutoAddedEndorsement").get(11).get("ED_AutoCode"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Section", map_InnerPages.get("AutoAddedEndorsement").get(11).get("ED_AutoSection"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Title", map_InnerPages.get("AutoAddedEndorsement").get(11).get("ED_AutoTitle"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(11).get("ED_AutoCaluseType"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Type", map_InnerPages.get("AutoAddedEndorsement").get(11).get("ED_AutoType"));
							EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+additionalCover.replaceAll(" ", "")+"_Code"), EndorsementIndividualData);
						break;
						}
						count++;
					}
				}
				if(((String)map_data.get("CD_"+code+"SingleProject")).equalsIgnoreCase("Yes")){
					int bespokeCount = 0;
					int count = 0;
					String[] properties = ((String)map_data.get("SP_AddBespoke")).split(";");
		            int no_of_property = properties.length;
					/*if(common.no_of_inner_data_sets.get("AddBespokeSP")==null){
						bespokeCount = 0;
					}else{
						bespokeCount = common.no_of_inner_data_sets.get("AddBespokeSP");
					}*/
					while(count < no_of_property){
						String additionalCover = map_InnerPages.get("AddBespokeSP").get(count).get("SP_AddB_Additionalcover");
						EndorsementIndividualData = new LinkedHashMap<String, String>();
						switch (additionalCover) {
						case "DE5":
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Code", map_InnerPages.get("AutoAddedEndorsement").get(12).get("ED_AutoCode"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Section", map_InnerPages.get("AutoAddedEndorsement").get(12).get("ED_AutoSection"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Title", map_InnerPages.get("AutoAddedEndorsement").get(12).get("ED_AutoTitle"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(12).get("ED_AutoCaluseType"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Type", map_InnerPages.get("AutoAddedEndorsement").get(12).get("ED_AutoType"));
							EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+additionalCover.replaceAll(" ", "")+"_Code"), EndorsementIndividualData);
						break;
						case "Existing Structures":
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Code", map_InnerPages.get("AutoAddedEndorsement").get(13).get("ED_AutoCode"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Section", map_InnerPages.get("AutoAddedEndorsement").get(13).get("ED_AutoSection"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Title", map_InnerPages.get("AutoAddedEndorsement").get(13).get("ED_AutoTitle"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(13).get("ED_AutoCaluseType"));
							EndorsementIndividualData.put("ED_"+additionalCover.replaceAll(" ", "")+"_Type", map_InnerPages.get("AutoAddedEndorsement").get(13).get("ED_AutoType"));
							EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_"+additionalCover.replaceAll(" ", "")+"_Code"), EndorsementIndividualData);
						break;
						}
						count++;
					}
				}
				if(((String)map_data.get("CD_"+code+"PropertyOwnersLiability")).equalsIgnoreCase("Yes")){
					EndorsementIndividualData = new LinkedHashMap<String, String>();
					EndorsementIndividualData.put("ED_PropertyOwnersLiability_Code", map_InnerPages.get("AutoAddedEndorsement").get(14).get("ED_AutoCode"));
					EndorsementIndividualData.put("ED_PropertyOwnersLiability_Section", map_InnerPages.get("AutoAddedEndorsement").get(14).get("ED_AutoSection"));
					EndorsementIndividualData.put("ED_PropertyOwnersLiability_Title", map_InnerPages.get("AutoAddedEndorsement").get(14).get("ED_AutoTitle"));
					EndorsementIndividualData.put("ED_PropertyOwnersLiability_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(14).get("ED_AutoCaluseType"));
					EndorsementIndividualData.put("ED_PropertyOwnersLiability_Type", map_InnerPages.get("AutoAddedEndorsement").get(14).get("ED_AutoType"));
					EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_PropertyOwnersLiability_Code"), EndorsementIndividualData);
				}
				break;
				
			case "XOC":
				if(((String)map_data.get("CD_"+code+"Public&ProductsLiability")).equalsIgnoreCase("Yes")){
					EndorsementIndividualData = new LinkedHashMap<String, String>();
					EndorsementIndividualData.put("ED_EmployersLiabilityexclusion_Code", map_InnerPages.get("AutoAddedEndorsement").get(1).get("ED_AutoCode"));
					EndorsementIndividualData.put("ED_EmployersLiabilityexclusion_Section", map_InnerPages.get("AutoAddedEndorsement").get(1).get("ED_AutoSection"));
					EndorsementIndividualData.put("ED_EmployersLiabilityexclusion_Title", map_InnerPages.get("AutoAddedEndorsement").get(1).get("ED_AutoTitle"));
					EndorsementIndividualData.put("ED_EmployersLiabilityexclusion_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(1).get("ED_AutoCaluseType"));
					EndorsementIndividualData.put("ED_EmployersLiabilityexclusion_Type", map_InnerPages.get("AutoAddedEndorsement").get(1).get("ED_AutoType"));
					EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_EmployersLiabilityexclusion_Code"), EndorsementIndividualData);
				}
				if(((String)map_data.get("CD_"+code+"JCT6.5.1")).equalsIgnoreCase("Yes")){
					EndorsementIndividualData = new LinkedHashMap<String, String>();
					EndorsementIndividualData.put("ED_EmployersLiabilityexclusion_Code", map_InnerPages.get("AutoAddedEndorsement").get(2).get("ED_AutoCode"));
					EndorsementIndividualData.put("ED_EmployersLiabilityexclusion_Section", map_InnerPages.get("AutoAddedEndorsement").get(2).get("ED_AutoSection"));
					EndorsementIndividualData.put("ED_EmployersLiabilityexclusion_Title", map_InnerPages.get("AutoAddedEndorsement").get(2).get("ED_AutoTitle"));
					EndorsementIndividualData.put("ED_EmployersLiabilityexclusion_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(2).get("ED_AutoCaluseType"));
					EndorsementIndividualData.put("ED_EmployersLiabilityexclusion_Type", map_InnerPages.get("AutoAddedEndorsement").get(2).get("ED_AutoType"));
					EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_EmployersLiabilityexclusion_Code"), EndorsementIndividualData);
				}
				if(((String)map_data.get("CD_"+code+"ThirdPartyMotorLiability")).equalsIgnoreCase("Yes")){
					EndorsementIndividualData = new LinkedHashMap<String, String>();
					EndorsementIndividualData.put("ED_EmployersLiabilityexclusion_Code", map_InnerPages.get("AutoAddedEndorsement").get(0).get("ED_AutoCode"));
					EndorsementIndividualData.put("ED_EmployersLiabilityexclusion_Section", map_InnerPages.get("AutoAddedEndorsement").get(0).get("ED_AutoSection"));
					EndorsementIndividualData.put("ED_EmployersLiabilityexclusion_Title", map_InnerPages.get("AutoAddedEndorsement").get(0).get("ED_AutoTitle"));
					EndorsementIndividualData.put("ED_EmployersLiabilityexclusion_ClauseType", map_InnerPages.get("AutoAddedEndorsement").get(0).get("ED_AutoCaluseType"));
					EndorsementIndividualData.put("ED_EmployersLiabilityexclusion_Type", map_InnerPages.get("AutoAddedEndorsement").get(0).get("ED_AutoType"));
					EndorsementCollectiveData.put(EndorsementIndividualData.get("ED_EmployersLiabilityexclusion_Code"), EndorsementIndividualData);
				}
				
				break;
			
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
		case "CCC":
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
		case "CCJ":
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
		case "COA":
			sectionValue.put("Policy", "pol");
			sectionValue.put("Public & Products Liability", "coa_pl_pr");
			sectionValue.put("Employers Liability", "coa_el");
			sectionValue.put("Annual Contract Works", "coa_acw");
			sectionValue.put("Single Project Contract Works", "coa_pcw");
			sectionValue.put("Own Plant", "coa_op");
			sectionValue.put("Hired In Plant", "coa_hp");
			sectionValue.put("JCT 6.5.1", "coa_jc");
			sectionValue.put("Terrorism", "coa_ter");
			sectionValue.put("Property Owners Liability", "coa_po");
			sectionValue.put("Legal Expenses", "coa_lea");
			break;
		case "XOC":
		case "XOQ":
		case "LEA":
			sectionValue.put("Policy", "pol");
			sectionValue.put("Combined Liability", "xoc_cl_sec");
			sectionValue.put("Public & Products Liability", "xoc_pl_sec");
			sectionValue.put("Employers Liability", "xoc_el_sec");
			sectionValue.put("JCT 6.5.1", "xoc_jc_sec");
			sectionValue.put("Third Party Motor Liability", "xoc_tp_sec");
			break;
		case "GTC":
		case "GTD":
			sectionValue.put("Policy", "pol");
			sectionValue.put("Claims", "cl");
		
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
					
					if(!endorsements.contains("None")){
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
		
		if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
			map_data = common.Rewind_excel_data_map;
		}else if(TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.contains("Rewind")){
			map_data = common.Rewind_excel_data_map;
		}else{
			map_data = common.MTA_excel_data_map;
		}
		
		
		Map<Object,Object> NB_map_data = common.NB_excel_data_map;
		Map<Object, Object> data_map = null;
		
		
		double final_fp_NNP=0.0;
		String code=null,cover_code=null;
		
		Map<String,Double> fp_details_values = new HashMap<>();
		
		switch (TestBase.businessEvent) {
		case "Renewal":
			data_map = common.Renewal_excel_data_map;
			break;
		case "MTA":
			if(common.currentRunningFlow.contains("Rewind")){
				data_map = common.Rewind_excel_data_map;
			}else{
				data_map = common.NB_excel_data_map;
			}			
			break;
		case "Rewind":
			data_map = common.Rewind_excel_data_map;
			break;
		default:
			break;
		}
		
		if(TestBase.product.equalsIgnoreCase("PAA") || TestBase.product.equalsIgnoreCase("PAB"))
			sectionName = "PAA Personal Accident"; //Due to conflict with Personal Accident Standerd cover name
			
		switch(sectionName){
		
		case "Material Damage":
			code = "MaterialDamage";
			cover_code = "MaterialDamage";
			break;
		case "Business Interruption":
			code = "BusinessInterruption";
			cover_code = "BusinessInterruption";
			break;
		case "Money & Assault":
			code = "Money&Assault";
			cover_code = "Money&Assault";
			break;
		case "Employers Liability":
			code = "EmployersLiability";
			cover_code = "Employers Liability";
			break;
		case "Public Liability":
			code = "PublicLiability";
			cover_code = "PublicLiability";
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
		case "Goods in Transit":
			
			if(TestBase.product.contains("GTA")){
				code = "GoodsinTransitRSAUK";
				cover_code = "GoodsinTransitRSAUK";
				sectionName = "Goods in Transit RSA UK";
			}else if(TestBase.product.contains("GTB")){
				code = "GoodsinTransitRSAROI";
				cover_code = "GoodsinTransitRSAROI";
				sectionName = "Goods in Transit RSA ROI";
			}else{
				code = "GoodsinTransit";
				cover_code = "GoodsInTransit";
			}
							
			break;
		case "Legal Expenses":
			code = "LegalExpenses";
			cover_code = "LegalExpenses";
			break;
		case "Terrorism":
			code = "Terrorism";
			cover_code = "Terrorism";
			break;
		case "Cargo":
			code = "Cargo";
			cover_code = "Cargo";
			break;
			
		case "Cars":
			code = "Cars";
			cover_code = "Cars";
			break;
		case "Commercial Vehicles":
			code = "CommercialVehicles";
			cover_code = "CommercialVehicles";
			break;
		case "Agricultural Vehicles":
			code = "AgriculturalVehicles";
			cover_code = "AgriculturalVehicles";
			break;
		case "Special Types":
			code = "SpecialTypes";
			cover_code = "SpecialTypes";
			break;
		case "Other Types":
			code = "OtherTypes";
			cover_code = "OtherTypes";
			break;
		case "Trade Plate":
			code = "TradePlate";
			cover_code = "TradePlates";
			break;
		case "Trailers":
			code = "Trailers";
			cover_code = "Trailers";
			break;
		case "Computer RSA":
			code = "Computers";
			cover_code = "Computer";
			break;
		case "Directors & Officers":
			code = "D&O";
			cover_code = "Directors&Officers";
			sectionName = "D&O";
			break;
		case "PAA Personal Accident":
			code = "PersonalAccident";
			cover_code = "PersonalAccident";
			sectionName = "Personal Accident";
			break;
			
			
		
		
		default:
				System.out.println("**Cover Name is not in Scope for POF**");
			break;
		
		}
		
	try{
			
			//TestUtil.reportStatus("---------------"+sectionName+"-----------------","Info",false);
			
				final_fp_NNP = Double.parseDouble(internal_data_map.get("Flat-Premiums").get(count-1).get("FP_Premium"));
			
				map_data.put(sectionName+"_FP", final_fp_NNP);
			
				String t_NetNetP_expected = common.roundedOff(Double.toString(final_fp_NNP));
				String t_NetNetP_actual = Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Net Net Premium"));
				CommonFunction.compareValues(Double.parseDouble(t_NetNetP_expected),Double.parseDouble(t_NetNetP_actual)," Net Net Premium");
				fp_details_values.put("Net Net Premium", Double.parseDouble(t_NetNetP_expected));
									
				double t_pen_comm = (( Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate"))/100)));
				String t_pc_expected = common.roundedOff(Double.toString(t_pen_comm));
				String t_pc_actual = Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Pen Comm"));
				CommonFunction.compareValues(Double.parseDouble(t_pc_expected),Double.parseDouble(t_pc_actual)," Pen Commission");
				fp_details_values.put("Pen Comm", Double.parseDouble(t_pc_expected));
				
				
				double t_netP = Double.parseDouble(t_pc_expected) + Double.parseDouble(t_NetNetP_expected);
				String t_netP_expected = common.roundedOff(Double.toString(t_netP));
				String t_netP_actual = Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Net Premium"));
				CommonFunction.compareValues(Double.parseDouble(t_netP_expected),Double.parseDouble(t_netP_actual),"Net Premium");
				fp_details_values.put("Net Premium", Double.parseDouble(t_netP_expected));
				
				
				double t_broker_comm = ((Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate"))/100)));
				String t_bc_expected = common.roundedOff(Double.toString(t_broker_comm));
				String t_bc_actual =  Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Broker Commission"));
				CommonFunction.compareValues(Double.parseDouble(t_bc_expected),Double.parseDouble(t_bc_actual),"Broker Commission");
				fp_details_values.put("Broker Commission", Double.parseDouble(t_bc_expected));
				
				
				double t_grossP = Double.parseDouble(t_netP_expected) + Double.parseDouble(t_bc_expected);
				String t_grossP_actual = Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Gross Premium"));
				CommonFunction.compareValues(t_grossP,Double.parseDouble(t_grossP_actual)," Gross Premium");
				fp_details_values.put("Gross Premium", t_grossP);
				
				double t_InsuranceTax = 0.0;
				
				if(TestBase.product.contains("GTB")){
					t_InsuranceTax = 0.00; //There is a defect for GTB - PRD-14483
				}else{
					if(((String)map_data.get("PS_TaxExempt")).equalsIgnoreCase("Yes") || ((String)map_data.get("PS_InsuranceTaxButton")).equalsIgnoreCase("Yes")){
						t_InsuranceTax = (t_grossP * 0.0)/100.0;
						fp_details_values.put("Insurance Tax Rate", Double.parseDouble("0.0"));
						
					}else{
						t_InsuranceTax = (t_grossP * Double.parseDouble(internal_data_map.get("Flat-Premiums").get(count-1).get("FP_TaxRate")))/100.0;
						fp_details_values.put("Insurance Tax Rate", Double.parseDouble(internal_data_map.get("Flat-Premiums").get(count-1).get("FP_TaxRate")));
					}
				}				
				
				t_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(t_InsuranceTax)));
				String t_InsuranceTax_actual = Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Insurance Tax"));
				CommonFunction.compareValues(t_InsuranceTax,Double.parseDouble(t_InsuranceTax_actual),"Insurance Tax");
				fp_details_values.put("Insurance Tax", t_InsuranceTax);
				//SPI  Transaction Total Premium verification : 
				double t_Premium = t_grossP + t_InsuranceTax;
				String t_p_expected = common.roundedOff(Double.toString(t_Premium));
				fp_details_values.put("Total Premium", Double.parseDouble(t_p_expected));
				
				String t_p_actual = Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Total Premium"));
				
				common.transaction_Details_Premium_Values.put(sectionName+"_FP", fp_details_values);
				
					
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
				Map<Object, Object> map_data = null;
				Map<String, List<Map<String, String>>> inner_map_data = null;
				if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
					map_data = common.Rewind_excel_data_map;
					inner_map_data = common.Rewind_Structure_of_InnerPagesMaps;
					
				}else if(TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equals("MTA") ){
					map_data = common.MTA_excel_data_map;
					inner_map_data = common.MTA_Structure_of_InnerPagesMaps;
				}else if(TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equals("Rewind") ){
					map_data = common.Rewind_excel_data_map;
					inner_map_data = common.Rewind_Structure_of_InnerPagesMaps;
				}else if(TestBase.businessEvent.equalsIgnoreCase("Renewal") && common.currentRunningFlow.equals("MTA") ){
					map_data = common.MTA_excel_data_map;
					inner_map_data = common.MTA_Structure_of_InnerPagesMaps;
				}
				
				boolean isMTARewindFPEntries=false;
				String testName = (String)map_data.get("Automation Key");
				k.pressDownKeyonPage();
				customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
			
				
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
							
								if(!headerName.contains("Pen Comm %") && !headerName.contains("Broker Comm %") && !headerName.contains("Gross Comm %")
										&& !headerName.contains("Insurance Tax Rate") ){
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
					common_CCD.isFPEntries=false;
					if(common_CCD.isMTARewindFlow){
						TestUtil.reportStatus("---------------Transaction Details table Verification after Rewind Endorsement(MTA)-----------------","Info",false);
					}else{
						TestUtil.reportStatus("---------------Transaction Details table Verification in MTA-----------------","Info",false);
					}
					//Transaction table Verification
					
					// Check if Flat premium is added or not :
			//	if(!TestBase.product.equals("MFB") && !TestBase.product.equals("MFC")){
					String flatPremium = (String)map_data.get("FP_isFlatPremium");
					String flatPremiumEntries = null; 
										
					if(flatPremium.contains("Yes")){
						flatPremiumEntries = (String)map_data.get("FP_FlatPremium_Entries");
					}			
					String[] arrF_Premium = null;
					
					if(flatPremiumEntries != null){
						
							arrF_Premium = flatPremiumEntries.split(";");
					
							for(int i = 0; i < arrF_Premium.length; i ++){
						
							if(i == 0){
								FP_Covers = (String)inner_map_data.get("Flat-Premiums").get(i).get("FP_Section");
							}else{
								FP_Covers = FP_Covers + ","+ (String)inner_map_data.get("Flat-Premiums").get(i).get("FP_Section");
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
							
							trans_error_val = trans_error_val + func_FP_Entries_Transaction_Details_Verification_MTA(s_Name,inner_map_data);
							
							
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
				//}
					
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
					Map<Object, Object> data_map = null;
					//For Tax Map Logic
					switch (common.currentRunningFlow) {
					case "MTA":
						data_map = common.MTA_excel_data_map;
						
						break;
					case "Rewind":
						data_map = common.Rewind_excel_data_map;
						break;
					default:
						break;
					}
					testName = (String)data_map.get("Automation Key");
					if(Math.abs(premium_diff)<=0.6){
						TestUtil.reportStatus("Total Premium with Admin Fees :[<b> "+exp_Total_Premium_with_Admin_fee+" </b>] matches with actual premium [<b> "+act_Total_Premium_with_Admin_fee+"</b>]as expected with some difference upto '0.05' on premium summary page.", "Pass", false);
						WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_TotalFinalPremium", exp_Total_Premium_with_Admin_fee,data_map);
					}else{
						TestUtil.reportStatus("Mismatch in Expected Total Premium with Admin Fees [<b> "+exp_Total_Premium_with_Admin_fee+"</b>] and Actual Premium [<b> "+act_Total_Premium_with_Admin_fee+"</b>] on premium summary page.", "Fail", false);
						WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_TotalFinalPremium", exp_Total_Premium_with_Admin_fee,data_map);
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
					exp_value = exp_value + transaction_Premium_Values.get(section).get("Net Net Premium");
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
							if(TestBase.product.equals("GTD")){
								section = "Goods in Transit";
							}else{
							section = "Goods In Transit";
							}
							}
						if(section.equalsIgnoreCase("Computers"))
							section = "Computer RSA";
						if(section.equalsIgnoreCase("D & O"))
							section = "D&O";
						
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
				if(common_CCD.isFPEntries && !section.contains("Flat")){
					try{
						if(section.equalsIgnoreCase("Property Owners Liabilities"))
							section = "Liabilities - POL";
						if(section.equalsIgnoreCase("Businesss Interruption"))
							section = "Business Interruption";
						if(section.equalsIgnoreCase("Goods in Transit")){
							if(TestBase.product.equals("GTD")){
								section = "Goods in Transit";
							}else{
							section = "Goods In Transit";
							}
						}
						if(section.equalsIgnoreCase("Computers"))
							section = "Computer RSA";
						if(section.equalsIgnoreCase("D & O"))
							section = "D&O";
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
				if(common_CCD.isFPEntries && !section.contains("Flat")){
					try{
						if(section.equalsIgnoreCase("Property Owners Liabilities"))
							section = "Liabilities - POL";
						if(section.equalsIgnoreCase("Businesss Interruption"))
							section = "Business Interruption";
						if(section.equalsIgnoreCase("Goods in Transit")){
							if(TestBase.product.equals("GTD")){
								section = "Goods in Transit";
							}else{
							section = "Goods In Transit";
							}
						}
						if(section.equalsIgnoreCase("Computers"))
							section = "Computer RSA";
						if(section.equalsIgnoreCase("D & O"))
							section = "D&O";
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
				if(common_CCD.isFPEntries && !section.contains("Flat")){
					try{
						if(section.equalsIgnoreCase("Property Owners Liabilities"))
							section = "Liabilities - POL";
						if(section.equalsIgnoreCase("Businesss Interruption"))
							section = "Business Interruption";
						if(section.equalsIgnoreCase("Goods in Transit")){
							if(TestBase.product.equals("GTD")){
								section = "Goods in Transit";
							}else{
							section = "Goods In Transit";
							}
						}
						if(section.equalsIgnoreCase("Computers"))
							section = "Computer RSA";
						if(section.equalsIgnoreCase("D & O"))
							section = "D&O";
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
				if(common_CCD.isFPEntries && !section.contains("Flat")){
					try{
						if(section.equalsIgnoreCase("Property Owners Liabilities"))
							section = "Liabilities - POL";
						if(section.equalsIgnoreCase("Businesss Interruption"))
							section = "Business Interruption";
						if(section.equalsIgnoreCase("Goods in Transit")){
							if(TestBase.product.equals("GTD")){
								section = "Goods in Transit";
							}else{
							section = "Goods In Transit";
							}
						}
						if(section.equalsIgnoreCase("Computers"))
							section = "Computer RSA";
						if(section.equalsIgnoreCase("D & O"))
							section = "D&O";
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
				if(common_CCD.isFPEntries && !section.contains("Flat")){
					try{
						if(section.equalsIgnoreCase("Property Owners Liabilities"))
							section = "Liabilities - POL";
						if(section.equalsIgnoreCase("Businesss Interruption"))
							section = "Business Interruption";
						if(section.equalsIgnoreCase("Goods in Transit")){
							if(TestBase.product.equals("GTD")){
								section = "Goods in Transit";
							}else{
							section = "Goods In Transit";
							}
						}
						if(section.equalsIgnoreCase("Computers"))
							section = "Computer RSA";
						if(section.equalsIgnoreCase("D & O"))
							section = "D&O";
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
				if(common_CCD.isFPEntries && !section.contains("Flat")){
					try{
						if(section.equalsIgnoreCase("Property Owners Liabilities"))
							section = "Liabilities - POL";
						if(section.equalsIgnoreCase("Businesss Interruption"))
							section = "Business Interruption";
						if(section.equalsIgnoreCase("Goods in Transit")){
							if(TestBase.product.equals("GTD")){
								section = "Goods in Transit";
							}else{
							section = "Goods In Transit";
							}
						}
						if(section.equalsIgnoreCase("Computers"))
							section = "Computer RSA";
						if(section.equalsIgnoreCase("D & O"))
							section = "D&O";
					exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Total Premium");
				}catch(Throwable t){
					continue;
				}
				}
		}
			
		}
		String t_p_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Total Premium"));
		
		common_HHAZ.MFC_total_premium = Double.parseDouble(t_p_actual);
		
		trans_details_values.put("Total Premium",exp_value);
		
		common.transaction_Details_Premium_Values.put("Totals", trans_details_values);
		
		double premium_diff = exp_value - Double.parseDouble(t_p_actual);
		
		if(premium_diff<0.7 && premium_diff>-0.7){
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
		case "Rewind":
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
		
		
		if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
			if(((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy")).equalsIgnoreCase("Yes") && ((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && TestBase.businessEvent.equalsIgnoreCase("MTA")) {
				p_MTA_Duration = Integer.parseInt(((String)map_data.get("MTA_EffectiveDays")).trim());
			}else{
				p_MTA_Duration = Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration"));
			}
		}else if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
			if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && TestBase.businessEvent.equalsIgnoreCase("Rewind")){
				p_MTA_Duration = Integer.parseInt(((String)map_data.get("MTA_EffectiveDays")).trim());
				common.NB_excel_data_map.put("PS_Duration", "365");
			}
		}else{
			p_MTA_Duration = Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration"));
		}
		
		
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
			if(TestBase.product.equals("OED") || TestBase.product.equals("PAA")||TestBase.product.equals("PAB")){
				cover_code = "PersonalAccident";
			}else{
				cover_code = "PersonalAccidentStandard";
			}
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
		case "Computers":
			code = "Computers";
			cover_code = "Computers";
			break;
		case "Cars":
			code = "Cars";
			cover_code = "Cars";
			break;
		case "Commercial Vehicles":
			code = "CommercialVehicles";
			cover_code = "CommercialVehicles";
			break;
		case "Agricultural Vehicles":
			code = "AgriculturalVehicles";
			cover_code = "AgriculturalVehicles";
			break;
		case "Special Types":
			code = "SpecialTypes";
			cover_code = "SpecialTypes";
			break;
		case "Other Types":
			code = "OtherTypes";
			cover_code = "OtherTypes";
			break;
		case "Trade Plate":
			code = "TradePlate";
			cover_code = "TradePlates";
			break;
		case "Trailers":
			code = "Trailers";
			cover_code = "Trailers";
			break;	
		case "Cargo":
			code = "Cargo";
			cover_code = "Cargo";
			break;
		case "Goods in Transit RSA UK":
			code = "GoodsinTransitRSAUK";
			cover_code = "GoodsinTransitRSAUK";
			break;
		case "Goods in Transit RSA ROI":
			code = "GoodsinTransitRSAROI";
			cover_code = "GoodsinTransitRSAROI";
			break;
		case "D & O":
			code="D&O";
			cover_code="D&O";
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
								((p_MTA_Duration) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod")));
							
				
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
							map_data = common.Rewind_excel_data_map;
						}
						
						if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
							final_trans_NNP = ((MTA_NNP - NB_NNP)/p_NB_Duration)*
									((p_MTA_Duration) - 0);
						}else{
							final_trans_NNP = ((MTA_NNP - NB_NNP)/p_NB_Duration)*
									((p_MTA_Duration) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod")));
						}
						
						
						
				
				}
				}
			
				else{
					if(Integer.parseInt((String)data_map.get("PS_Duration"))!=Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration"))){
						
						
						//if(sectionName.contains("PI")){
							
							if(((String)data_map.get("CD_"+cover_code)).equals("Yes") && ((String)common.MTA_excel_data_map.get("CD_"+cover_code)).equals("No"))
							{
								NB_NNP = Double.parseDouble((String)data_map.get("PS_"+code+"_NetNetPremium"));
								MTA_NNP = 0.0;
								map_data = data_map;
									
							}else if(((String)data_map.get("CD_"+cover_code)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_"+cover_code)).equals("Yes")){
								NB_NNP = 0.0;
								MTA_NNP = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NetNetPremium"));
								map_data = common.MTA_excel_data_map;
							}else{
								NB_NNP = Double.parseDouble((String)data_map.get("PS_"+code+"_NetNetPremium"));
								if(TestBase.product.equals("CMA")||TestBase.product.equals("MFB")||TestBase.product.equals("MFC")){
									MTA_NNP =transactionDetails_Premium_Values.get(sectionName).get("Net Net Premium");
								}else{
									MTA_NNP = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NetNetPremium"));
								}
								map_data = common.MTA_excel_data_map;
								
							}
							//Previous Premium MTA Calculation
							
							if(TestBase.product.equals("CMA")||TestBase.product.equals("MFB")||TestBase.product.equals("MFC")){
								double t = NB_NNP/p_NB_Duration; // amount per day
								double p =t*p_MTA_Duration;
								final_trans_NNP = p - NB_NNP;
								
								
							}else{
								final_trans_NNP = ((MTA_NNP - NB_NNP)/p_NB_Duration)*
									((p_MTA_Duration) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod")));
							
							}
					}
							/*trans_NetNetP = ((NB_NNP)/p_NB_Duration)*p_MTA_Remaining_Duration;
							previous_mta = NB_NNP - trans_NetNetP;
							annualize_mta = ((Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NetNetPremium")) * (Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) ))/(Integer.parseInt((String)data_map.get("PS_Duration"))));
							_annualize_mta = (annualize_mta/Double.parseDouble((String)common.MTA_excel_data_map.get("PS_Duration"))) * ((Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) - p_MTA_Remaining_Duration));
							final_trans_NNP = _annualize_mta - previous_mta;
							*/
					
					else{
						
						//if(sectionName.contains("PI")){
							if(((String)data_map.get("CD_"+cover_code)).equals("Yes") && ((String)common.MTA_excel_data_map.get("CD_"+cover_code)).equals("No"))
							{
								NB_NNP = Double.parseDouble((String)data_map.get("PS_"+code+"_NetNetPremium"));
								MTA_NNP = 0.0;
								map_data = data_map;
									
							}else if(((String)data_map.get("CD_"+cover_code)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_"+cover_code)).equals("Yes")){
								NB_NNP = 0.0;
								MTA_NNP = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NetNetPremium"));
								map_data = common.MTA_excel_data_map;
							}else{
								NB_NNP = Double.parseDouble((String)data_map.get("PS_"+code+"_NetNetPremium"));
								MTA_NNP = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_"+code+"_NetNetPremium"));
								map_data = common.MTA_excel_data_map;
							}
							
							final_trans_NNP = ((MTA_NNP - NB_NNP)/p_NB_Duration)*
									((p_MTA_Duration) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod")));
							
						
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
				
				//String nbStatus = (String)data_map.get("MTA_Status");
						
				if(!sectionName.contains("Terrorism")){
					AdditionalExcTerrDocAct = AdditionalExcTerrDocAct + Double.parseDouble(t_grossP_actual);
				}else{
					AdditionalTerPDocAct = Double.parseDouble(t_grossP_actual);
				}
					
				double t_InsuranceTax = 0.0;
				if(((String)common.MTA_excel_data_map.get("PS_TaxExempt")).equalsIgnoreCase("Yes") || ((String)common.MTA_excel_data_map.get("PS_InsuranceTaxButton")).equalsIgnoreCase("Yes")){
					t_InsuranceTax = (t_grossP * 0.0)/100.0;
				}else{
					t_InsuranceTax = (t_grossP * Double.parseDouble((String)data_map.get("PS_"+code+"_IPT")))/100.0;
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
				
				//String Nb_Status = (String)map_data.get("MTA_Status");
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
				
				if(premium_diff<0.5 && premium_diff>-0.5){
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
				
				Map<Object, Object> data_map_tax = null;
				//For Tax Map Logic
				switch (common.currentRunningFlow) {
				case "MTA":
					data_map_tax = common.MTA_excel_data_map;
					break;
				case "Rewind":
					data_map_tax = common.Rewind_excel_data_map;
					break;
				default:
					break;
				}
				
				String insurance_Tax_Rate = null;
 				
				double t_InsuranceTax = 0.0;
					
				//Below code will decide tax rate based on policy start date for 10 cent rate
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
				Date policy_Start_date = sdf.parse((String)map_data.get(("PS_PolicyStartDate"))); 
				Date tax_rate_change_date = sdf.parse("01/06/2017");
			
				if(((String)data_map_tax.get("PS_TaxExempt")).equalsIgnoreCase("Yes") || ((String)data_map_tax.get("PS_InsuranceTaxButton")).equalsIgnoreCase("Yes")){
					insurance_Tax_Rate = "0";
				}else if(policy_Start_date.before(tax_rate_change_date)){
					if(TestBase.product.equalsIgnoreCase("MFB") || TestBase.product.equalsIgnoreCase("GTB") ||
						TestBase.product.equalsIgnoreCase("CCE") || TestBase.product.equalsIgnoreCase("PAB") ||
						TestBase.product.equalsIgnoreCase("CCJ"))
						insurance_Tax_Rate = "5";
					else
						insurance_Tax_Rate = "10";
					
				}else{
					insurance_Tax_Rate = (String)map_data.get("PS_"+code+"_IPT");
				}
				
				if(TestBase.product.contains("GTB")){
					t_InsuranceTax = 0.00; // there is a defect raised for GTB - PRD-14483
				}else{
					t_InsuranceTax = (t_grossP * Double.parseDouble(insurance_Tax_Rate))/100.0;
				}
				
					
				/*if(((String)data_map_tax.get("PS_TaxExempt")).equalsIgnoreCase("Yes") || ((String)data_map_tax.get("PS_InsuranceTaxButton")).equalsIgnoreCase("Yes")){
					t_InsuranceTax = (t_grossP * 0.0)/100.0;
				}else{
					t_InsuranceTax = (t_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_IPT")))/100.0;
				}*/
				
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
				
				if(sectionName.equals("Personal Accident Optional"))
					common_HHAZ.PAO_total_premium=Double.parseDouble(t_p_actual);
				
				common.transaction_Details_Premium_Values.put(sectionName, trans_details_values);
				
				double premium_diff = Double.parseDouble(t_p_expected) - Double.parseDouble(t_p_actual);
				
				if(premium_diff<0.5 && premium_diff>-0.5){
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
			if(common.currentRunningFlow.contains("Rewind")){
				data_map = common.Rewind_excel_data_map;
				map_data = common.Rewind_excel_data_map;
			}else{
				data_map = common.NB_excel_data_map;
			}
			break;
			
		case "Rewind":
			map_data = common.Rewind_excel_data_map;
			data_map = common.MTA_excel_data_map;
			break;
			
		default:
			break;
		}
		
		if(TestBase.product.equalsIgnoreCase("PAA") || TestBase.product.equalsIgnoreCase("PAB"))
			sectionName = "PAA Personal Accident"; //Due to conflict with Personal Accident Standerd cover name
		
			
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
			code = "EmployersLiability";
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
			if(TestBase.product.equals("GTD")){
				flat_section = "Goods in Transit";
			}else{
				flat_section = "Goods In Transit";
			}
			break;
		case "Goods in Transit RSA UK":
			code = "GoodsinTransitRSAUK";
			cover_code = "GoodsinTransitRSAUK";
			flat_section = "Goods in Transit RSA UK";
			break;
		case "Goods in Transit RSA ROI":
			code = "GoodsinTransitRSAROI";
			cover_code = "GoodsinTransitRSAROI";
			flat_section = "Goods in Transit RSA ROI";
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
		case "Cargo" :
			code="Cargo";
			cover_code="Cargo";
			flat_section = sectionName;
			break;
			
		case "Cars":
			code = "Cars";
			cover_code = "Cars";
			flat_section = sectionName;
			break;
		case "Commercial Vehicles":
			code = "CommercialVehicles";
			cover_code = "CommercialVehicles";
			flat_section = sectionName;
			break;
		case "Agricultural Vehicles":
			code = "AgriculturalVehicles";
			cover_code = "AgriculturalVehicles";
			flat_section = sectionName;
			break;
		case "Special Types":
			code = "SpecialTypes";
			cover_code = "SpecialTypes";
			flat_section = sectionName;
			break;
		case "Other Types":
			code = "OtherTypes";
			cover_code = "OtherTypes";
			flat_section = sectionName;
			break;
		case "Trade Plate":
			code = "TradePlate";
			cover_code = "TradePlates";
			flat_section = sectionName;
			break;
		case "Trailers":
			code = "Trailers";
			cover_code = "Trailers";
			flat_section = sectionName;
			break;
			
		case "Computers":
			code = "Computers";
			cover_code = "Computer";
			flat_section = "Computer RSA";
			break;
		case "D & O":
			code = "D&O";
			cover_code = "D&O";
			flat_section = "D&O";
			break;
		case "PAA Personal Accident":
			code = "PersonalAccident";
			cover_code = "PersonalAccident";
			flat_section = "Personal Accident";
			break;
		default:
				System.out.println("**Cover Name is not in Scope **");
			break;
		
		}
		
	try{
			
				
		
				TestUtil.reportStatus("---------------"+sectionName+" in Flat Premium Section-----------------","Info",false);
			
				//final_fp_NNP = Double.parseDouble(internal_data_map.get("Flat-Premiums").get(count-1).get("FP_Premium"));
			
				//final_fp_NNP = common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Net Net Premium");
				
				final_fp_NNP = (Double)map_data.get(flat_section+"_FP");
				
				//This is to handle Rewind on MTA for Exisiting Policy Search.
				if(common_CCD.AlreadyAddedFPEntries.contains(flat_section)){
					map_data = common.MTA_excel_data_map;
				}
				
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
				
				double t_InsuranceTax = 0.00;
				if(TestBase.product.contains("GTB")){
					t_InsuranceTax = 0.00; // There is defect raised for GTB - PRD-14483
				}else{
					t_InsuranceTax =  (t_grossP * common.transaction_Details_Premium_Values.get(flat_section+"_FP").get("Insurance Tax Rate"))/100.0;
				}
				
				
				t_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(t_InsuranceTax)));
				String t_InsuranceTax_actual = Double.toString(common.transaction_Details_Premium_Values.get(flat_section+"_FP").get("Insurance Tax"));
				if(((String)map_data.get("PS_TaxExempt")).equalsIgnoreCase("Yes") || ((String)map_data.get("PS_InsuranceTaxButton")).equalsIgnoreCase("Yes")){
					if(!(t_InsuranceTax == 0.0)){
						 TestUtil.reportStatus("Flat Premium Insurance tax calculation is incorrect , defect is ><b>PRD-14011</b><", "Fail", true);
					}
				}else{
					if(!TestBase.product.contains("GTB") && (Double.parseDouble(t_InsuranceTax_actual) == 0.0)){
						 TestUtil.reportStatus("Flat Premium Insurance tax calculation is incorrect , defect is ><b>PRD-14011</b><", "Fail", true);
					}
				}
				CommonFunction.compareValues(t_InsuranceTax,Double.parseDouble(t_InsuranceTax_actual)," Insurance Tax");
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
				}else if(sectionName.contains("Goods in Transit RSA UK") || sectionName.contains("Goods in Transit RSA ROI")){
					sCover = "Goods in Transit";
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
		int MTA_duration = 0;
		int effectiveDays = 0;
		
		if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
			if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement")) {
				if(common_CCD.isEndorsementDone){
					effectiveDays = Integer.parseInt(((String)common.MTA_excel_data_map.get("MTA_EffectiveDays")).trim());
			  		MTA_duration = effectiveDays - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"));
				}else{
					effectiveDays = Integer.parseInt(((String)common.MTA_excel_data_map.get("MTA_EffectiveDays")).trim());
					MTA_duration = effectiveDays;
				}
			}else{
				MTA_duration = Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"));
			}
			  	
				
		}else if(TestBase.businessEvent.equalsIgnoreCase("MTA") ){
			if(((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy")).equalsIgnoreCase("Yes") && ((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement")) {
		  		
				if(common_CCD.isEndorsementDone){
					effectiveDays = Integer.parseInt(((String)common.MTA_excel_data_map.get("MTA_EffectiveDays")).trim());
			  		MTA_duration = effectiveDays - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"));
				}else{
					effectiveDays = Integer.parseInt(((String)common.MTA_excel_data_map.get("MTA_EffectiveDays")).trim());
					MTA_duration = effectiveDays;
				}
		  		
			}else{
				MTA_duration = Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"));
			}
		}else{
			if(((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy")).equalsIgnoreCase("Yes") && ((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement")) {
		  		
				if(common_CCD.isEndorsementDone){
					effectiveDays = Integer.parseInt(((String)common.MTA_excel_data_map.get("MTA_EffectiveDays")).trim());
			  		MTA_duration = effectiveDays - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"));
				}else{
					effectiveDays = Integer.parseInt(((String)common.MTA_excel_data_map.get("MTA_EffectiveDays")).trim());
					MTA_duration = effectiveDays;
				}
		  		
			}else{
				MTA_duration = Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"));
			}
		}
		
		/*if(((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy")).equalsIgnoreCase("Yes") && ((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement")) {
	  		
			if(common_CCD.isEndorsementDone){
				effectiveDays = Integer.parseInt(((String)common.MTA_excel_data_map.get("MTA_EffectiveDays")).trim());
		  		MTA_duration = effectiveDays - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"));
			}else{
				effectiveDays = Integer.parseInt(((String)common.MTA_excel_data_map.get("MTA_EffectiveDays")).trim());
				MTA_duration = effectiveDays;
			}
	  		
		}else{
			MTA_duration = Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"));
		}*/
		
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
		if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
			if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
				AdjustedTaxDetails.clear();
			}
		}
		
		
		
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
				if(sectionName.contains("AbbeyLegalExpenses")){
					sectionName = "LegalExpenses";
				}
				if(sectionName.contains("Annual Contract Works")){
					sectionName = "AnnualWorks";
				}
				if(sectionName.contains("Single Project Contract Works")){
					sectionName = "SingleProject";
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
			
			if(k.isDisplayedField("Page_Header")){
				if(k.getText("Page_Header").equals("Quote Check"))
					k.scrollInViewByXpath("//*[text()='Proceed']");
					driver.findElement(By.xpath("//*[text()='Proceed']")).click();
			}
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

public double calculateCargoTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
	try{
		String Premium = null;
		String IPT = null;
		
		 if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
         	try{
         	Premium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Net Net Premium"));
				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax"));
         	}catch(NullPointerException npe){
         		Premium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Net Net Premium"));
         		IPT = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax"));
				}
			}
       if(common.currentRunningFlow.equals("NB")||common.currentRunningFlow.equals("Renewal")){
			if(common_HHAZ.TransactionTable){
			//if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
				Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Totals").get("Net Net Premium"));
				IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Totals").get("Insurance Tax"));
				
			}else{
				Premium = (String)data_map.get("PS_Cargo_NetNetPremium");
				IPT = (String)data_map.get("PS_Cargo_GT");
			}
		}
		if(common.currentRunningFlow.equals("Requote")){
			
			if(common_HHAZ.TransactionTable){
				Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Totals").get("Net Net Premium"));
				IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Totals").get("Insurance Tax"));
			
			}else{
				Premium = (String)data_map.get("PS_Cargo_NetNetPremium");
				IPT = (String)data_map.get("PS_Cargo_GT");
			}
		}
		if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
			
			if(common_HHAZ.TransactionTable){
				Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Totals").get("Net Net Premium"));
				IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Totals").get("Insurance Tax"));
			
			}else{
				Premium = (String)data_map.get("PS_Cargo_NetNetPremium");
				IPT = (String)data_map.get("PS_Cargo_GT");
			}
		}

		if(common.currentRunningFlow.equals("CAN")){
			
			try{
				Premium = "-"+Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Net Net Premium"));
				IPT = "-"+Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Insurance Tax"));
			}catch(NullPointerException npe){
				Premium = "0.0";
				IPT = "0.0";
			}
		}
		
		String part1= "//*[@id='table0']/tbody";
		String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+2)+"]")).getText();
		double Due = Double.parseDouble(Premium)+ Double.parseDouble(IPT);
		String Dueamt= common.roundedOff(Double.toString(Due)) ;
		CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "Hcc International Insurance Company Plc");
		return Due;
	
	}catch(Throwable t) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
		k.reportErr("Failed in "+methodName+" function", t);
		Assert.fail("Failed in Calculate Legal Expenses ammount.  \n", t);
		return 0;
	}

}

public double calculateGITTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
	try{
		String Premium = null;
		String IPT = null;
		
		String sCover = null;
		
		if(TestBase.product.contains("GTA")){
			sCover = "GoodsinTransitRSAUK";
		}else if(TestBase.product.contains("GTB")){
			sCover = "GoodsinTransitRSAROI";
		}else if(TestBase.product.contains("GTC")){
			sCover = "Cargo";
		}else if(TestBase.product.contains("GTD")){
			sCover = "GoodsinTransit";
		}
			
		
		 if(common.currentRunningFlow.equals("MTA") || (TestBase.businessEvent.equalsIgnoreCase("MTA") && common.currentRunningFlow.equalsIgnoreCase("Rewind"))){
         	try{
         	Premium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Net Net Premium"));
				IPT = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax"));
         	}catch(NullPointerException npe){
         		Premium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Net Net Premium"));
         		IPT = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax"));
				}
			}
       if(common.currentRunningFlow.equals("NB")||common.currentRunningFlow.equals("Renewal")){
			if(common_HHAZ.TransactionTable){
			//if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
				Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Totals").get("Net Net Premium"));
				IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Totals").get("Insurance Tax"));
				
			}else{
				Premium = (String)data_map.get("PS_"+sCover+"_NetNetPremium");
				IPT = (String)data_map.get("PS_"+sCover+"_GT");
			}
		}
		if(common.currentRunningFlow.equals("Requote")){
			
			if(common_HHAZ.TransactionTable){
				Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Totals").get("Net Net Premium"));
				IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Totals").get("Insurance Tax"));
			
			}else{
				Premium = (String)data_map.get("PS_"+sCover+"_NetNetPremium");
				IPT = (String)data_map.get("PS_"+sCover+"_GT");
			}
		}
		if(common.currentRunningFlow.equals("Rewind") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
			
			if(common_HHAZ.TransactionTable){
				Premium = Double.toString(common_HHAZ.transaction_Premium_Values.get("Totals").get("Net Net Premium"));
				IPT = Double.toString(common_HHAZ.transaction_Premium_Values.get("Totals").get("Insurance Tax"));
			
			}else{
				Premium = (String)data_map.get("PS_"+sCover+"_NetNetPremium");
				IPT = (String)data_map.get("PS_"+sCover+"_GT");
			}
		}

		if(common.currentRunningFlow.equals("CAN")){
			
			try{
				Premium = "-"+Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Net Net Premium"));
				IPT = "-"+Double.toString(common_CCD.CAN_CCD_ReturnP_Values_Map.get("Totals").get("Insurance Tax"));
			}catch(NullPointerException npe){
				Premium = "0.0";
				IPT = "0.0";
			}
		}
		
		String part1= "//*[@id='table0']/tbody";
		String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+2)+"]")).getText();
		double Due = Double.parseDouble(Premium)+ Double.parseDouble(IPT);
		String Dueamt= common.roundedOff(Double.toString(Due)) ;
		CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "Hcc International Insurance Company Plc");
		return Due;
	
	}catch(Throwable t) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
		k.reportErr("Failed in "+methodName+" function", t);
		Assert.fail("Failed in Calculate Legal Expenses ammount.  \n", t);
		return 0;
	}

}

//This function handles pagination(multiple pages)
public boolean is_Pagination_enabled(){
	boolean r_value=false;
	
	try{
		
		WebElement btn_next = driver.findElement(By.xpath("//*[@id='mainpanel']//a[contains(text(),'next')]"));
		if(btn_next!=null && btn_next.isDisplayed()){
			r_value = true;
		}
		
	}catch(Throwable t){
		return false;
	}
	
	
	return r_value;
	
}


public boolean rewind_Btn_Visibility() {
	
	try {
		
		String MTA_Effective_date = (String)common.MTA_excel_data_map.get("MTA_EffectiveDate");
		
		if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
			
			//To check Rewind Button visibility for MTA-Rewind event
			String todays_date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
			String rewind_days = common.DateDiff(todays_date, MTA_Effective_date);
			if(Integer.parseInt(rewind_days) > Integer.parseInt(CONFIG.getProperty("RewindButtonDurration"))) {
				TestUtil.reportStatus("Rewind Operation not possible on the selected policy as 'Rewind' button will be available only for >"+CONFIG.getProperty("RewindButtonDurration")+"< day(s) from Policy Start Date .  ", "Fail", true);
				//return false;
			}else{
				if(k.isDisplayedByXpath("//a[text()='Rewind']"))
					TestUtil.reportStatus("Rewind button is visible on premium Summary page . ", "Info", true);
			}
		
		}
	}catch(Throwable t) {
		return false;
	}
	
	return true;
	
	
}

public boolean getPremiumOverride(){
	
	
	try{
		
		
		
		
		return true;
	}catch(Throwable t){
		
		return false; 
		
	}
	
	
}


//End of Common HHAZ

}
