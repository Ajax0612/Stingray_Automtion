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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.selenium.commonfiles.util.ErrorUtil;
import com.selenium.commonfiles.util.ObjectMap;
import com.selenium.commonfiles.util.TestUtil;
import com.selenium.commonfiles.util.XLS_Reader;

public class CommonFunction_Zennor extends TestBase {
	
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
	public Map<String, Map<String , String>> EndorsementCollectiveData = null;
	public Map<String, String> ExtraEndorsementList = new LinkedHashMap<>();
	public String quoteStatus = "", FP_Covers = null;
	
	public static int size;
	SimpleDateFormat df = new SimpleDateFormat();
	Date currentDate = new Date();
	public Map<Object, Object> NB_excel_data_map = null;
	public Map<Object, Object> MTA_excel_data_map = null;
	public Map<Object, Object> Rewind_excel_data_map = null;
	public Map<Object, Object> Renewal_excel_data_map = null;
	public Map<Object, Object> CAN_excel_data_map = null;
	public List<String> CoversDetails_data_list = null;
	public static Map<String, Double> Adjusted_Premium_map = null;
	
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
	public double InsTaxTerrDoc = 0.00, tpTotal = 0.00, AddTaxTerrDoc = 0.00, rewindMTADoc_AddTaxTer = 0.00;
	public double rewindDoc_Premium = 0.00, rewindDoc_TerP = 0.00, rewindDoc_InsPTax = 0.00, rewindDoc_TotalP = 0.00, rewindDoc_InsTaxTer = 0.00;
	public double rewindMTADoc_Premium = 0.00, rewindMTADoc_TerP = 0.00, rewindMTADoc_InsPTax = 0.00, rewindMTADoc_TotalP = 0.00;

public boolean funcPremiumSummary(Map<Object, Object> map_data,String code,String event) {
		
		boolean r_value= true;
		Date currentDate = new Date();
		String testName = (String)map_data.get("Automation Key");
		String customPolicyDuration=null;
		SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
		
		try{
			customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
			//if(!TestBase.businessEvent.equalsIgnoreCase("Renewal")){
				customAssert.assertTrue(common.verifyEndorsementONPremiumSummary(common.Renewal_excel_data_map),"Endorsement on Premium is having issue(S).");
			//}
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
				k.waitFiveSeconds();
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
		TestUtil.reportStatus("Policy Duration = "+customPolicyDuration+" Days . ", "Info", true);
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

	public boolean Verify_premiumSummaryTable(){
		
		if(common_POF.isNBRquoteStarted){
			TerPremDocAct = 0.00;
			rewindDoc_InsTaxTer= 0.00;
			rewindDoc_Premium= 0.00;
			rewindDoc_TerP= 0.00;
			InsTaxDocAct= 0.00;
			TotalPremiumWithAdminDocAct= 0.00;
			rewindDoc_InsPTax = 0.00;
		}
		
		err_count = 0;
		final String code = TestBase.product;
		final String event = TestBase.businessEvent;
		String testName = null,cover_code=null;;
		Map<Object,Object> data_map = null;
		
		
		switch(common.currentRunningFlow){
			case "NB":
				testName = (String)common.NB_excel_data_map.get("Automation Key");
				data_map = common.NB_excel_data_map;
			break;
			case "MTA":
				testName = (String)common.MTA_excel_data_map.get("Automation Key");
				data_map = common.MTA_excel_data_map;
			break;
			case "Renewal":
				testName = (String)common.Renewal_excel_data_map.get("Automation Key");
				data_map = common.Renewal_excel_data_map;
			break;
		}
		
		final Map<String,String> locator_map = new HashMap<>();
		locator_map.put("PenComm", "pencom");
		locator_map.put("NetPremium", "nprem");
		locator_map.put("BrokerComm", "comm");
		locator_map.put("GrossPremium", "gprem");
		locator_map.put("InsuranceTax", "gipt");
		
		final Map<String,String> section_map = new HashMap<>();
		
		
//		section_map.put("EmployersLiability", "el");
		
		section_map.put("MaterialDamage", "md");
		section_map.put("BusinessInterruption", "bi");
		section_map.put("PropertyOwnersLiabilities", "pol");		
		section_map.put("Terrorism", "tr");
		section_map.put("BespokeCover", "bs");		
		section_map.put("Total", "tot");
		
		
		
		double exp_Premium = 0.0;
		
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
			
				customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
				if(((String)data_map.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
					data_map.put("PS_"+sectionName+"_IPT", "0.0");
				}else{
					if(!policy_status_actual.contains("Rewind")){
						data_map.put("PS_"+sectionName+"_IPT", data_map.get("PS_IPTRate"));
					}else{
						if(sectionName.contains("PropertyOwners")){
							sectionName="Liabilities-POL";
						}
						if(((((String)data_map.get("CD_"+sectionName)).equals("No") && ((String)data_map.get("CD_Add_"+sectionName)).equals("Yes")))){
							data_map.put("PS_"+sectionName+"_IPT", data_map.get("PS_IPTRate"));
						}
					}
				}
			}
			
			}
			
			if(common.currentRunningFlow.equalsIgnoreCase("MTA")){
					
				//if(!PremiumFlag)
				for(int i =1;i<=trans_tble_Rows-1;i++){
					String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
					sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
					
					switch(sectionName){
					
					case "MaterialDamage":
						//code = "MaterialDamage";
						cover_code = "MaterialDamage";
						break;
					case "BusinessInterruption":
						//code = "BusinessInterruption";
						cover_code = "BusinessInterruption";
						break;
					case "PropertyOwnersLiabilities":
						//code = "PropertyOwnersLiabilities";
						cover_code = "Liabilities-POL";
						break;
					case "Terrorism":
						//code = "Terrorism";
						cover_code = "Terrorism";
						break;
					case "BespokeCover":
						//code = "BespokeCover";
						cover_code = "BespokeCover";
						break;
					
					default:
							System.out.println("**Cover Name is not in Scope for POF**");
						break;
					
					}
					if(sectionName.contains("Totals"))
						sectionName = "Total";
					if(CommonFunction.businessEvent.equalsIgnoreCase("Renewal") && common.currentRunningFlow.equalsIgnoreCase("MTA")){
						if(((String)common.Renewal_excel_data_map.get("CD_"+cover_code)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_"+cover_code)).equals("Yes")){
							customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
						}else{
							String cover_name = section_map.get(sectionName);
							String PencCommXpath , BrokerCommXpath;
							if(section_map.get(sectionName).contains("md")){
								PencCommXpath = "//*[@name='md_pof"+"_penr']";
								BrokerCommXpath ="//*[@name='md_pof"+"_comr']" ;
							    
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
						if(((String)common.NB_excel_data_map.get("CD_"+cover_code)).equals("No") && ((String)common.MTA_excel_data_map.get("CD_"+cover_code)).equals("Yes")){
							customAssert.assertTrue(funcAddInput_PremiumSummary(sectionName,section_map.get(sectionName),data_map),"Add Premium Summary Input function having issues for "+sectionName);
						}else{
							String cover_name = section_map.get(sectionName);
							String PencCommXpath , BrokerCommXpath;
							if(section_map.get(sectionName).contains("md")){
								PencCommXpath = "//*[@name='md_pof"+"_penr']";
								BrokerCommXpath ="//*[@name='md_pof"+"_comr']" ;
							    
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
					/*if(((String)data_map.get("PS_TaxExempt")).equalsIgnoreCase("Yes")){
						data_map.put("PS_"+sectionName+"_IPT", "0.0");
					}else{
						if(!policy_status_actual.contains("Rewind")){
							data_map.put("PS_"+sectionName+"_IPT", data_map.get("PS_IPTRate"));
						}else{
							if(sectionName.contains("PropertyOwners")){
								sectionName="Liabilities-POL";
							}
							if(((((String)data_map.get("CD_"+sectionName)).equals("No") && ((String)data_map.get("CD_Add_"+sectionName)).equals("Yes")))){
								data_map.put("PS_"+sectionName+"_IPT", data_map.get("PS_IPTRate"));
							}
						}
					}*/
				}
			
				
					//NB_NNP = 0.0;
				}
			
			PremiumFlag = true;	 
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
			
			//if(((String)data_map.get("CD_SolicitorsPI")).equals("Yes")){
				
			for(int i =1;i<=trans_tble_Rows-1;i++){
				String annualTblXpath2 = "/tbody/tr["+i+"]/td[1]";
				sectionName = driver.findElement(By.xpath(annualTble_xpath+annualTblXpath2)).getText().replaceAll(" ", "");
				 if(sectionName.contains("Totals")){
					sectionName = "Total";}
				
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
			
			//customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_Total_GT", f.format(Total_GT),data_map),"Error while writing Gross Tax  data to excel .");
			//customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_Total_GP", f.format(Total_GP),data_map),"Error while writing Gross Premium data to excel .");
			//customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_TotalPremium", f.format(exp_Premium),data_map),"Error while writing Total Premium data to excel .");
	
			String exp_Total_Premium = common.roundedOff(Double.toString(exp_Premium));
			String act_Total_Premium = k.getAttribute("SPI_Total_Premium", "value");
			act_Total_Premium = act_Total_Premium.replaceAll(",", "");
			
			//double premium_diff = Double.parseDouble(common.roundedOff(Double.toString(Double.parseDouble(exp_Total_Premium) - Double.parseDouble(act_Total_Premium))));
			
			TestUtil.reportStatus("---------------Total Premium-----------------","Info",false);
			
			CommonFunction.compareValues(Double.parseDouble(exp_Total_Premium),Double.parseDouble(act_Total_Premium),"Total Premium.");
			
			/*if(Math.abs(premium_diff)<=0.02){
				TestUtil.reportStatus("Total Premium :[<b> "+exp_Total_Premium+" </b>] matches with actual premium [<b> "+act_Total_Premium+"</b>]as expected with some difference upto '0.05' on premium summary page.", "Pass", false);
				customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_TotalPremium", act_Total_Premium,data_map),"Error while writing Total Premium data to excel .");
			}else{
				TestUtil.reportStatus("Mismatch in Expected Premium [<b> "+exp_Total_Premium+"</b>] and Actual Premium [<b> "+act_Total_Premium+"</b>] on premium summary page.", "Fail", false);
				customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_TotalPremium", exp_Total_Premium,data_map),"Error while writing Total Premium data to excel .");
			}*/
				
			//customAssert.assertTrue(func_SPI_Write_Total_PremiumSummary_Values(),"Error while writing Total Premium Summary values to excel . ");
			
			if(Integer.parseInt((String)data_map.get("PS_Duration")) == 365){
				//Toal Premium With Admin Fees
				double total_premium_with_admin_fee = Double.parseDouble(exp_Total_Premium);
				
				String exp_Total_Premium_with_Admin_fee = common.roundedOff(Double.toString(total_premium_with_admin_fee));
				k.waitTwoSeconds();
				
				String xPath = "//table[@id='table0']//*//td[text()='Total']//following-sibling::td";
				String act_Total_Premium_with_Admin_fee = k.getTextByXpath(xPath);
				
				act_Total_Premium_with_Admin_fee = act_Total_Premium_with_Admin_fee.replaceAll(",", "");
				//premium_diff = Double.parseDouble(common.roundedOff(Double.toString(Double.parseDouble(exp_Total_Premium_with_Admin_fee) - Double.parseDouble(act_Total_Premium_with_Admin_fee))));
				
				/*TestUtil.reportStatus("---------------Total Premium with Admin Fees-----------------","Info",false);
				
				CommonFunction.compareValues(Double.parseDouble(exp_Total_Premium_with_Admin_fee),Double.parseDouble(act_Total_Premium_with_Admin_fee),"Total Premium.");
				*/
				/*if(Math.abs(premium_diff)<=0.20){
					TestUtil.reportStatus("Total Premium with Admin Fees :[<b> "+exp_Total_Premium_with_Admin_fee+" </b>] matches with actual premium [<b> "+act_Total_Premium_with_Admin_fee+"</b>]as expected with some difference upto '0.05' on premium summary page.", "Pass", false);
					customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_TotalFinalPremium", act_Total_Premium_with_Admin_fee,data_map),"Error while writing Total Final Premium data to excel .");
				}else{
					TestUtil.reportStatus("Mismatch in Expected Total Premium with Admin Fees [<b> "+exp_Total_Premium_with_Admin_fee+"</b>] and Actual Premium [<b> "+act_Total_Premium_with_Admin_fee+"</b>] on premium summary page.", "Fail", false);
					customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_TotalFinalPremium", exp_Total_Premium_with_Admin_fee,data_map),"Error while writing Total Final Premium data to excel .");
				}*/
			}
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
				PencCommXpath = "//*[@name='md_pof"+"_penr']";
				BrokerCommXpath ="//*[@name='md_pof"+"_comr']" ;
			    GrossCommXpath= ".//*[@id='md_pof"+"_gcomm']";
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
	//Reusable for both NB and MTA
	public int func_PremiumSummaryCalculation(String code,String covername,Map<String,String> premium_loc) {
				
		Map<Object,Object> map_data = null;
		
		String event=null;
		
		
		switch(TestBase.businessEvent){
			case "NB":
				map_data = common.NB_excel_data_map;
			break;
			case "MTA":
				if(common.currentRunningFlow.equals("NB")){
					map_data = common.NB_excel_data_map;
					event = "NB";
					}
				else{
					map_data = common.MTA_excel_data_map;
					event = "MTA";
					
				}
				break;	
			case "Renewal":
				if(CommonFunction.businessEvent.equalsIgnoreCase("Renewal") && common.currentRunningFlow.equalsIgnoreCase("MTA")){
					map_data = common.MTA_excel_data_map;
				}else{
					map_data = common.Renewal_excel_data_map;
				}
				break;
			case "CAN":
				if(common.currentRunningFlow.equals("NB")){
					map_data = common.NB_excel_data_map;
					event = "NB";
					}
				else{
					map_data = common.CAN_excel_data_map;
					event = "CAN";				
				}
				
			break;	
			
		}
			String testName = (String)map_data.get("Automation Key");
			
			double NetNet_Premium = Double.parseDouble((String)map_data.get("PS_"+covername+"_NetNetPremium"));
			
//			String val_cover = null;
//			switch(code){
//			case "PI":
//				val_cover = "pi";
//				break;
//			case "SEL":
//				val_cover = "el";
//				break;
//			default:
//					System.out.println("**Cover Name is not in Scope for SPI**");
//				break;
//			
//			}
			//val_cover = code;
			
//			if(code.equals("SEL") && common.currentRunningFlow.equals("MTA") && ((String)map_data.get("Automation Key")).contains("MTA_02")){
//				
//				try{
//				String SEL_penComm_type = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("PenComm")+"')]", "type");
//				if(SEL_penComm_type.equals("text")){
//				
//					TestUtil.reportStatus("Solicitors Excess Layers's Pen/Broker commission should not be editable on premium Summary page . ", "Fail", true);
//					ErrorUtil.addVerificationFailure(new Throwable("Solicitors Excess Layers's Pen/Broker commission editability issue on premium Summary page"));
//				}
//				}catch(Throwable t){
//					String isSEL_penComm_readOnly = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("PenComm")+"')]", "readonly");
//					if(!isSEL_penComm_readOnly.equals("true")){
//						TestUtil.reportStatus("Solicitors Excess Layers's Pen/Broker commission should not be editable on premium Summary page . ", "Fail", true);
//						ErrorUtil.addVerificationFailure(new Throwable("Solicitors Excess Layers's Pen/Broker commission editability issue on premium Summary page"));
//					}
//				}
//				
//			}
			
		try{
				
				TestUtil.reportStatus("---------------"+covername+"-----------------","Info",false);
				//SPI Pen commission Calculation : 
				double pen_comm = ((NetNet_Premium / (1-((Double.parseDouble((String)map_data.get("PS_"+covername+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+covername+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+covername+"_PenComm_rate"))/100)));
				String pc_expected = common.roundedOff(Double.toString(pen_comm));
				if(code.contains("md"))
					code = "md_pof_";
				else
					code="_"+code+"_";
									
				String pc_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("PenComm")+"')]", "value");
				//String pc_actual = driver.findElement(By.xpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("PenComm")+"')]"))
				CommonFunction.compareValues(Double.parseDouble(pc_expected),Double.parseDouble(pc_actual),"Pen Commission");
				//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow,"Premium Summary",testName,"PS_"+covername+"_PenComm",pc_expected,map_data),"Error while writing Pen Commission for cover "+covername+" to excel .");
				map_data.put("PS_"+covername+"_PenComm",pc_expected);
				
				// Net Premium verification : 
				double netP = Double.parseDouble(pc_expected) + NetNet_Premium;
				String netP_expected = common.roundedOff(Double.toString(netP));
				String netP_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("NetPremium")+"')]", "value");
				CommonFunction.compareValues(Double.parseDouble(netP_expected),Double.parseDouble(netP_actual),"Net Premium");
				//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+covername+"_NetPremium",netP_expected,map_data),"Error while writing Net Premium for cover "+code+" to excel .");
				map_data.put("PS_"+covername+"_NetPremium",netP_expected);
				
				// Broker commission Calculation : 
				double broker_comm = ((NetNet_Premium / (1-((Double.parseDouble((String)map_data.get("PS_"+covername+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+covername+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+covername+"_BrokerComm_rate"))/100)));
				String bc_expected = common.roundedOff(Double.toString(broker_comm));
				String bc_actual =  k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("BrokerComm")+"')]", "value");
				CommonFunction.compareValues(Double.parseDouble(bc_expected),Double.parseDouble(bc_actual),"Broker Commission");
				//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+covername+"_BrokerComm",bc_expected,map_data),"Error while writing Broker Commission for cover "+code+" to excel .");
				map_data.put("PS_"+covername+"_BrokerComm",bc_expected);
				
				// GrossPremium verification :  
				double grossP = Double.parseDouble(netP_expected) + Double.parseDouble(bc_expected);
				
				
				String grossP_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("GrossPremium")+"')]", "value");
							
				if(!covername.contains("Terrorism")){
					PremiumExcTerrDocAct = PremiumExcTerrDocAct + Double.parseDouble(grossP_actual);
				}else{
					TerPremDocAct = Double.parseDouble(grossP_actual);
				}
				
				
				CommonFunction.compareValues(grossP,Double.parseDouble(grossP_actual),"Gross Premium");
				//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+covername+"_GP",(grossP_actual),map_data),"Error while writing Gross Premium for cover "+covername+" to excel .");
				map_data.put("PS_"+covername+"_GP",(grossP_actual));
				
				double InsuranceTax = (grossP * Double.parseDouble((String)map_data.get("PS_"+covername+"_IPT")))/100.0;
				//InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(InsuranceTax)));
				String InsuranceTax_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+premium_loc.get("InsuranceTax")+"')]", "value");					
								
				//CommonFunction.compareValues(InsuranceTax,Double.parseDouble(InsuranceTax_actual),"Insurance Tax");
				//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+covername+"_GT",Double.toString(InsuranceTax),map_data),"Error while writing Total Premium for cover "+covername+" to excel .");
				map_data.put("PS_"+covername+"_GT",Double.toString(InsuranceTax));
								
				if(covername.contains("Terrorism")){
					InsTaxTerrDoc = Double.parseDouble(InsuranceTax_actual);
					if(common_POF.isNBRewindStarted){
						rewindDoc_InsTaxTer = Double.parseDouble(InsuranceTax_actual);	
					}			
				}
				
				String Nb_Status = (String)map_data.get("NB_Status");
				if(common_POF.isNBRewindStarted){
					
					/*String newCover = "";
					if(covername.contains("PropertyOwnersLiabilities")){
						newCover = "Liabilities-POL";
					}else if(covername.contains("EmployersLiability")) {
						newCover = "Liabilities-EL";
					}else{
						newCover = covername;
					}
					
					String sCover = (String)map_data.get("CD_Add_"+newCover);
					
					if(sCover.contains("Yes")){*/
						rewindDoc_InsPTax  = rewindDoc_InsPTax + Double.parseDouble(InsuranceTax_actual);
						if(!covername.contains("Terrorism")){
							rewindDoc_Premium  = rewindDoc_Premium + Double.parseDouble(grossP_actual);
						}else{
							rewindDoc_TerP  = Double.parseDouble(grossP_actual);
						}
					//}
					
				}
				
				InsTaxDocAct = InsTaxDocAct + Double.parseDouble(InsuranceTax_actual);
				
				//SPI Total Premium verification : 
				double Premium = grossP + InsuranceTax;
				String p_expected = common.roundedOff(Double.toString(Premium));
				
				String p_actual = common.roundedOff(k.getAttributeByXpath("//*[contains(@id,'"+code+"tot')]", "value"));
				
					TotalPremiumWithAdminDocAct = TotalPremiumWithAdminDocAct + Double.parseDouble(p_actual);
								
				double premium_diff = Double.parseDouble(common.roundedOff(Double.toString(Double.parseDouble(p_expected) - Double.parseDouble(p_actual))));
				
				if(premium_diff<=0.20 && premium_diff>=-0.20){
					TestUtil.reportStatus("Total Premium [<b> "+p_expected+" </b>] matches with actual total premium [<b> "+p_actual+" </b>]as expected for "+covername+" on premium summary page.", "Pass", false);
					//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+covername+"_TotalPremium", p_expected,map_data),"Error while writing Total Premium for cover "+code+" to excel .");
					map_data.put("PS_"+covername+"_TotalPremium", p_expected);
					return 0;
					
				}else{
					TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+p_expected+"</b>] and Actual Premium [<b> "+p_actual+"</b>] for "+code+" on premium summary page. </p>", "Fail", true);
					//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+covername+"_TotalPremium", p_expected,map_data),"Error while writing Total Premium for cover "+code+" to excel .");
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
	
	//Reusable for both NB and MTA
		public int func_PremiumSummaryCalculation_LEA(String code,String covername,Map<String,String> premium_loc) {
			
			Map<Object,Object> map_data = null;
			
			String event=null;
			
			
			switch(TestBase.businessEvent){
				case "NB":
					map_data = common.NB_excel_data_map;
				break;
				case "MTA":
					if(common_SPI.currentFlow.equals("NB")){
						map_data = common.NB_excel_data_map;
						event = "NB";
						}
					else{
						map_data = common.MTA_excel_data_map;
						event = "MTA";
						
					}
					break;	
				case "Renewal":
					map_data = common.Renewal_excel_data_map;
					break;
				case "CAN":
					if(common_SPI.currentFlow.equals("NB")){
						map_data = common.NB_excel_data_map;
						event = "NB";
						}
					else{
						map_data = common.CAN_excel_data_map;
						event = "CAN";				
					}
					
				break;	
				
			}
				String testName = (String)map_data.get("Automation Key");
				double NetNet_Premium = Double.parseDouble((String)map_data.get("PS_"+covername+"_NetNetPremium"));
				
				//String val_cover = null;
//				switch(code){
//				case "PI":
//					val_cover = "pi";
//					break;
//				case "SEL":
//					val_cover = "el";
//					break;
//				default:
//						System.out.println("**Cover Name is not in Scope for SPI**");
//					break;
//				
//				}
				//val_cover = code;
				
//				if(code.equals("SEL") && common.currentRunningFlow.equals("MTA") && ((String)map_data.get("Automation Key")).contains("MTA_02")){
//					
//					try{
//					String SEL_penComm_type = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("PenComm")+"')]", "type");
//					if(SEL_penComm_type.equals("text")){
//					
//						TestUtil.reportStatus("Solicitors Excess Layers's Pen/Broker commission should not be editable on premium Summary page . ", "Fail", true);
//						ErrorUtil.addVerificationFailure(new Throwable("Solicitors Excess Layers's Pen/Broker commission editability issue on premium Summary page"));
//					}
//					}catch(Throwable t){
//						String isSEL_penComm_readOnly = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("PenComm")+"')]", "readonly");
//						if(!isSEL_penComm_readOnly.equals("true")){
//							TestUtil.reportStatus("Solicitors Excess Layers's Pen/Broker commission should not be editable on premium Summary page . ", "Fail", true);
//							ErrorUtil.addVerificationFailure(new Throwable("Solicitors Excess Layers's Pen/Broker commission editability issue on premium Summary page"));
//						}
//					}
//					
//				}
				
			try{
					
					TestUtil.reportStatus("---------------"+covername+"-----------------","Info",false);
					//SPI Pen commission Calculation : 
					double pen_comm = ((NetNet_Premium / (1-((Double.parseDouble((String)map_data.get("PS_"+covername+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+covername+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+covername+"_PenComm_rate"))/100)));
					String pc_expected = common.roundedOff(Double.toString(pen_comm));
					if(TestBase.product.contains("XOC")|| TestBase.product.contains("XOQ")){
						code = code+"_sec";}
						
					String pc_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+premium_loc.get("PenComm")+"')]", "value");
					//String pc_actual = driver.findElement(By.xpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("PenComm")+"')]"))
					CommonFunction.compareValues(Double.parseDouble(pc_expected),Double.parseDouble(pc_actual),"Pen Commission");
					//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow,"Premium Summary",testName,"PS_"+covername+"_PenComm",pc_expected,map_data),"Error while writing Pen Commission for cover "+covername+" to excel .");
					map_data.put("PS_"+covername+"_PenComm",pc_expected);
					
					// Net Premium verification : 
					double netP = Double.parseDouble(pc_expected) + NetNet_Premium;
					String netP_expected = common.roundedOff(Double.toString(netP));
					String netP_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+premium_loc.get("NetPremium")+"')]", "value");
					CommonFunction.compareValues(Double.parseDouble(netP_expected),Double.parseDouble(netP_actual),"Net Premium");
					//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+covername+"_NetPremium",netP_expected,map_data),"Error while writing Net Premium for cover "+code+" to excel .");
					map_data.put("PS_"+covername+"_NetPremium",netP_expected);
					
					// Broker commission Calculation : 
					double broker_comm = ((NetNet_Premium / (1-((Double.parseDouble((String)map_data.get("PS_"+covername+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+covername+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+covername+"_BrokerComm_rate"))/100)));
					String bc_expected = common.roundedOff(Double.toString(broker_comm));
					String bc_actual =  k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+premium_loc.get("BrokerComm")+"')]", "value");
					CommonFunction.compareValues(Double.parseDouble(bc_expected),Double.parseDouble(bc_actual),"Broker Commission");
					//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+covername+"_BrokerComm",bc_expected,map_data),"Error while writing Broker Commission for cover "+code+" to excel .");
					map_data.put("PS_"+covername+"_BrokerComm",bc_expected);
					
					// GrossPremium verification :  
					double grossP = Double.parseDouble(netP_expected) + Double.parseDouble(bc_expected);
					String grossP_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+premium_loc.get("GrossPremium")+"')]", "value");
					if(code.equals("PI"))
						PI_pdf_GrossPremium = Double.parseDouble(grossP_actual);
					else
						SEL_pdf_GrossPremium = Double.parseDouble(grossP_actual);
					CommonFunction.compareValues(grossP,Double.parseDouble(grossP_actual),"Gross Premium");
					//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+covername+"_GP",(grossP_actual),map_data),"Error while writing Gross Premium for cover "+covername+" to excel .");
					map_data.put("PS_"+covername+"_GP",(grossP_actual));
					
					double InsuranceTax = (grossP * Double.parseDouble((String)map_data.get("PS_"+covername+"_IPT")))/100.0;
					InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(InsuranceTax)));
					String InsuranceTax_actual = k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+premium_loc.get("InsuranceTax")+"')]", "value");
					if(code.equals("PI"))
						PI_pdf_InsuranceTax = Double.parseDouble(InsuranceTax_actual);
					else
						SEL_pdf_InsuranceTax = Double.parseDouble(InsuranceTax_actual);
					CommonFunction.compareValues(InsuranceTax,Double.parseDouble(InsuranceTax_actual),"Insurance Tax");
					//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+covername+"_GT",Double.toString(InsuranceTax),map_data),"Error while writing Total Premium for cover "+covername+" to excel .");
					map_data.put("PS_"+covername+"_GT",Double.toString(InsuranceTax));
					
					//SPI Total Premium verification : 
					double Premium = grossP + InsuranceTax;
					String p_expected = common.roundedOff(Double.toString(Premium));
					
					String p_actual = common.roundedOff(k.getAttributeByXpath("//*[contains(@id,'"+code+"_"+"tot')]", "value"));
					
					double premium_diff = Double.parseDouble(common.roundedOff(Double.toString(Double.parseDouble(p_expected) - Double.parseDouble(p_actual))));
					
					if(premium_diff<=0.20 && premium_diff>=-0.20){
						TestUtil.reportStatus("Total Premium [<b> "+p_expected+" </b>] matches with actual total premium [<b> "+p_actual+" </b>]as expected for "+covername+" on premium summary page.", "Pass", false);
						//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+covername+"_TotalPremium", p_expected,map_data),"Error while writing Total Premium for cover "+code+" to excel .");
						map_data.put("PS_"+covername+"_TotalPremium", p_expected);
						return 0;
						
					}else{
						TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+p_expected+"</b>] and Actual Premium [<b> "+p_actual+"</b>] for "+code+" on premium summary page. </p>", "Fail", true);
						//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+covername+"_TotalPremium", p_expected,map_data),"Error while writing Total Premium for cover "+code+" to excel .");
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

	public boolean func_SPI_Write_Total_PremiumSummary_Values(){
		
		boolean r_value=true;
		String val_total = null,event=null;
		Map<Object,Object> data_map = null;
		
		switch(TestBase.businessEvent){
			case "NB":
				data_map = common.NB_excel_data_map;
			break;
			case "MTA":
				if(common_SPI.currentFlow.equals("NB")){
					data_map = common.NB_excel_data_map;
					
					}
				else{
					data_map = common.MTA_excel_data_map;
					
					
				}
			break;
			case "Renewal":
				
					data_map = common.Renewal_excel_data_map;
				
			break;
			case "CAN":
				if(common_SPI.currentFlow.equals("NB")){
					data_map = common.NB_excel_data_map;				
					}
				else{
					data_map = common.CAN_excel_data_map;
				}
			break;
			
		}
		
		
		
		String testName = (String)data_map.get("Automation Key");
		
		try{
			
		val_total = k.getAttributeByXpath("//*[contains(@name,'_admin_fee')]","value");
		customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName,"PS_TotalAdminFee",val_total,data_map),"Error while writing Total Admin Fee to excel .");
		
		val_total = k.getAttribute("SPI_Total_NNPremium", "value");
		val_total = val_total.replaceAll(",","");
		customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName,"PS_NetNetPemiumTotal",val_total,data_map),"Error while writing Total NN Premium to excel .");
		
		val_total = k.getAttribute("SPI_Total_PenComm", "value");
		val_total = val_total.replaceAll(",","");
		customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName,"PS_PenCommTotal",val_total,data_map),"Error while writing Total Pen Comm. to excel .");
		
		val_total = k.getAttribute("SPI_Total_NPremium", "value");
		val_total = val_total.replaceAll(",","");
		customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName,"PS_NetPremiumTotal",val_total,data_map),"Error while writing Total Net Premium to excel .");
		
		val_total = k.getAttribute("SPI_Total_BrokerComm", "value");
		val_total = val_total.replaceAll(",","");
		customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName,"PS_BrokerCommissionTotal",val_total,data_map),"Error while writing Total Broker Comm to excel .");
		
		val_total = k.getAttribute("SPI_Total_GrossPremium", "value");
		val_total = val_total.replaceAll(",","");
		customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName,"PS_Total_GP",val_total,data_map),"Error while writing Total Gross Premium to excel .");
		
		val_total = k.getAttribute("SPI_Total_InsuranceTax", "value");
		val_total = val_total.replaceAll(",","");
		customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName,"PS_Total_GT",val_total,data_map),"Error while writing Total IPT to excel .");
		
		return r_value;
		
		}catch(Throwable t){
			
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
						sectionNames.add(sec_Name.getText());
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
		}else{
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
	
	//This function is to check " if Rate charged is less than Minimum Rate " referral rule
	public boolean func_Referral_Rating_minimum_Premium(String coverName,String activityName,double adjustedRate){
		
		boolean r_value=false;
		Properties VELA_CPRS_Rater = OR.getORProperties();
		String activity=null,fullcoverName=null;
		double net_Min_Rate=0.0;
		
		try{
			
			switch(coverName){
			case "PL":
				fullcoverName = "Public & Products Liability";
				break;
			case "EL":
				fullcoverName = "Employers Liability";
				break;
			case "Annual Works":
				fullcoverName = "Annual Works";
				break;
			case "Single Project":
				fullcoverName = "Single Project";
				break;
			case "Own Plant":
				fullcoverName = "Own Plant";
				break;
			case "Hired In Plant":
				fullcoverName = "Hired In Plant";
				break;
			}
			
			
			if(activityName.contains("Work Away")){
				activity = activityName.substring("Work Away - ".length());
			}else {	
				activity = activityName;
			}
			net_Min_Rate = get_Net_Minimum_Rate(activity,coverName);
			
			if(adjustedRate < net_Min_Rate){
				common_VELA.referrals_list.add(fullcoverName+"_"+coverName+" rate charged for "+activity+" is below the minimum rate of "+net_Min_Rate+"%");
			}
				
			r_value=true;
		}catch(Throwable t){
			System.out.println("Error in func_Referral_Rating_minimum_Premium "+t.getMessage());
			return false;
		}
		return r_value;
	}
	
	//This function is to check " if Rate charged is less than Minimum Rate " referral rule
		public boolean func_Referral_Rating_minimum_Premium_Plant(String coverName,String activityName,double adjustedRate){
			
			boolean r_value=false;
			Properties VELA_CPRS_Rater = OR.getORProperties();
			String activity=null,fullcoverName=null;
			double net_Min_Rate=0.0;
			
			try{
				
				switch(coverName){
				
				case "Own Plant":
					fullcoverName = "Own Plant";
					break;
				case "Hired In Plant":
					fullcoverName = "Hired In Plant";
					break;
				}
			
				activity = activityName;
				
				net_Min_Rate = get_Net_Minimum_Rate_Plant(activity,coverName);
				
				if(adjustedRate < net_Min_Rate){
					common_VELA.referrals_list.add(fullcoverName+"_"+coverName+" rate charged for "+activity+" is below the minimum rate of "+net_Min_Rate+"%");
				}
					
				r_value=true;
			}catch(Throwable t){
				System.out.println("Error in func_Referral_Rating_minimum_Premium "+t.getMessage());
				return false;
			}
			return r_value;
		}
	
	public double get_Net_Minimum_Rate(String trade_Activity,String cover){
		
		double r_value=0.0;
		DecimalFormat formatter = new DecimalFormat("###.####");
		Properties PLEL_Rater;
		PLEL_Rater = OR.getORProperties();
		String t_activity =null;
		try{
			
			if(cover.equals("Annual Works"))
				cover = "Works";
			
			if(trade_Activity.contains("Bona Fide")){
				t_activity = "Bona_Fide_Sub_Contractors_cent_of_PL_rate_PL_NMR";
			}else{
				t_activity = trade_Activity.replaceAll(" -", "").replaceAll(" ", "_").replaceAll(",", "");
				t_activity = t_activity + "_"+cover + "_NMR";
			}
			
			r_value = Double.parseDouble(PLEL_Rater.getProperty(t_activity));
			r_value = Double.valueOf(formatter.format(r_value));
		
		}catch(Throwable t ){
			System.out.println("Error while getting Net Minimum Rate for activity > "+trade_Activity+" < "+t.getMessage());
		}
		return r_value;
			
	}
public double get_Net_Minimum_Rate_Plant(String trade_Activity,String cover){
		
		double r_value=0.0;
		DecimalFormat formatter = new DecimalFormat("###.####");
		Properties PLEL_Rater;
		PLEL_Rater = OR.getORProperties();
		String t_activity =null;
		try{
			
			
				t_activity = trade_Activity.replaceAll(" -", "").replaceAll(" ", "_").replaceAll(",", "");
				t_activity = t_activity + "_MIN_RATE";
			
			
			r_value = Double.parseDouble(PLEL_Rater.getProperty(t_activity));
			r_value = Double.valueOf(formatter.format(r_value));
		
		}catch(Throwable t ){
			System.out.println("Error while getting Net Minimum Rate for activity > "+trade_Activity+" < "+t.getMessage());
		}
		return r_value;
			
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
				return true;
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
				
				if((referrel_tble_Rows-1) <= common_VELA.referrals_list.size()){
				//For each referral message
				for(String referral_msg : common_VELA.referrals_list){
					isReferralFound=false;
				//for(int message = 1; message <= total_referral_messages ;message ++){
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
				
				}else if((referrel_tble_Rows-1) > common_VELA.referrals_list.size()){
					for(int r_row = 1; r_row < referrel_tble_Rows ;r_row ++){
						isReferralFound=false;
					//for(int message = 1; message <= total_referral_messages ;message ++){
						w_referral_Row = driver.findElement(By.xpath(referralRulesTbl_xpath+"//tbody//tr["+r_row+"]//td["+Description_col_index+"]"));
						w_referralSection_Row = driver.findElement(By.xpath(referralRulesTbl_xpath+"//tbody//tr["+r_row+"]//td["+Section_col_index+"]"));
						
					
						for(String referral_msg : common_VELA.referrals_list){
							
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
				TestUtil.reportStatus("Referral Rules table not exist on quote check screen . ", "Fail", true);
				
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
	
/****
 * 
 * Variable - tradeCodeValue ---- > TradeCode from Excel Sheet
 * Variable - pageName ---- > Trade code selection Page. E.G - Property Details/Policy Details
 * Variable - currentPropertyIndex ---- > This variable is only used for Inner pages to get current index. For non Inner pass as "0"
 * 
 */
	@SuppressWarnings("static-access")
	public boolean tradeCodeSelection(String tradeCodeValue , String pageName , int currentPropertyIndex) {
		
		try{
			customAssert.assertTrue(k.Click("CCF_Btn_SelectTradeCode"), "Unable to click on Select Trade Code button in Policy Details .");
			customAssert.assertTrue(common.funcPageNavigation("Trade Code Selection", ""), "Navigation problem to Trade Code Selection page .");
			String a_selectedTradeCode = null;
			String a_selectedTradeCode_desc = null;
	 		List<WebElement> list = k.findElements("TradeCodeName");/*driver.findElements(By.xpath("//*[contains(@class,'std')]/tBody/tr/td/a"));*/
	 		String flag = null;
	 		String v_exp_selectedTradeCode_Desc = null;
	 		for(int i=0;i<list.size();i++){
	 			
	 			String tradeCode = list.get(i).getText();
	 			if(tradeCodeValue.equalsIgnoreCase(tradeCode)){
	 				JavascriptExecutor j_exe = (JavascriptExecutor) driver;
					j_exe.executeScript("arguments[0].scrollIntoView(true);", list.get(i));
					v_exp_selectedTradeCode_Desc = driver.findElement(By.xpath("//*[contains(@class,'std')]/tBody/tr["+(i+1)+"]/td[2]")).getText();
	 				list.get(i).click();
	 				flag = "found";
	 				break;
	 			}else{
	 				flag = "Not Found";
	 			}
	 		}
	 		
	 		if(!flag.equalsIgnoreCase("found")){
	 			
	 			if((String)common.NB_excel_data_map.get("PD_TCS_TradeCode")==null || ((String)common.NB_excel_data_map.get("PD_TCS_TradeCode")).equalsIgnoreCase("")){
	 				TestUtil.reportStatus("Trade code value is empty. Please check your datasheet. Trade code is mandatory to proceed further. Path : "+common.product+"_"+common.businessEvent+".xlsx > "+(String)common.NB_excel_data_map.get("Automation Key")+" > Policy Details Sheet > Field Name : TCS_TradeCode ", "Fail", true);
	 			}else{
	 				TestUtil.reportStatus("Trade code <b>[  "+(String)common.NB_excel_data_map.get("PD_TCS_TradeCode")+"  ]</b> is not present in the selection list. Please enter valid Trade code. Trade code is mandatory to proceed further. Path to update value : "+common.product+"_"+common.businessEvent+".xlsx > "+(String)common.NB_excel_data_map.get("Automation Key")+" > Policy Details Sheet > Field Name : TCS_TradeCode ", "Fail", true);
	 			}
	 			return false;
	 		}else{
	 			
	 			switch (pageName) {
				case "Policy Details":
					
					switch (CommonFunction.product) {
					case "CCG":
						TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Policy Details" , (String)common.NB_excel_data_map.get("Automation Key"), "PD_TCS_TradeCode_HazardGroup", k.getText("CCF_Gray_PD_TradeCode_HazardGroup"), common.NB_excel_data_map);
						break;
					case "POC":
						TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Policy Details" , (String)common.NB_excel_data_map.get("Automation Key"), "PD_TCS_TradeCode_HazardGroup", k.getText("CCF_Gray_PD_TradeCode_HazardGroup"), common.NB_excel_data_map);
						break;
					case "XOE":
						TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Policy Details" , (String)common.NB_excel_data_map.get("Automation Key"), "PD_TCS_TradeCode_HazardGroup", k.getText("CCF_Gray_PD_TradeCode_HazardGroup"), common.NB_excel_data_map);
						break;	
					}	
					a_selectedTradeCode = k.getText("POF_Gray_PD_TradeCode");
					customAssert.SoftAssertEquals(a_selectedTradeCode, tradeCodeValue,"Trade Code on Policy Details Screen is incorrect - Expected [ <b>"+tradeCodeValue+"</b>] and Actual [<b>"+a_selectedTradeCode+"</b>]  .");
	 	 			TestUtil.reportStatus("Trade Code on Policy Details Screen is Correct.", "Pass", true);
	 	 			
	 	 			a_selectedTradeCode_desc = k.getText("POF_Gray_PD_TradeCode_Desc");
	 	 			customAssert.SoftAssertEquals(a_selectedTradeCode_desc, v_exp_selectedTradeCode_Desc,"Trade Code Description on Policy Details Screen is incorrect- Expected [ <b>"+v_exp_selectedTradeCode_Desc+"</b>] and Actual [<b>"+a_selectedTradeCode+"</b>]  . ");
	 	 			TestUtil.reportStatus("Trade Code Description on Policy Details Screen is Correct.", "Pass", true);
	 	 			
					break;
				case "Property Details":
					a_selectedTradeCode = k.getText("CCF_Gray_PD_TradeCode");
	 	 			customAssert.SoftAssertEquals(a_selectedTradeCode, tradeCodeValue,"Trade Code on Policy Details Screen is incorrect - Expected [ <b>"+tradeCodeValue+"</b>] and Actual [<b>"+a_selectedTradeCode+"</b>]  .");
	 	 			TestUtil.reportStatus("Trade Code on Policy Details Screen is Correct.", "Pass", true);
	 	 			
	 	 			a_selectedTradeCode_desc = k.getText("CCF_Gray_PD_TradeCode_Desc");
	 	 			customAssert.SoftAssertEquals(a_selectedTradeCode_desc, v_exp_selectedTradeCode_Desc,"Trade Code Description on Policy Details Screen is incorrect- Expected [ <b>"+v_exp_selectedTradeCode_Desc+"</b>] and Actual [<b>"+a_selectedTradeCode+"</b>]  . ");
	 	 			TestUtil.reportStatus("Trade Code Description on Policy Details Screen is Correct.", "Pass", true);
	 	 			
					break;
				case "Contractors All Risks":
					a_selectedTradeCode = k.getText("CCF_Gray_PD_TradeCode_CAR");
	 	 			customAssert.SoftAssertEquals(a_selectedTradeCode, tradeCodeValue,"Trade Code on Policy Details Screen is incorrect - Expected [ <b>"+tradeCodeValue+"</b>] and Actual [<b>"+a_selectedTradeCode+"</b>]  .");
	 	 			TestUtil.reportStatus("Trade Code on Policy Details Screen is Correct.", "Pass", true);
	 	 			
	 	 			a_selectedTradeCode_desc = k.getText("CCF_Gray_PD_TradeCode_Desc_CAR");
	 	 			customAssert.SoftAssertEquals(a_selectedTradeCode_desc, v_exp_selectedTradeCode_Desc,"Trade Code Description on Policy Details Screen is incorrect- Expected [ <b>"+v_exp_selectedTradeCode_Desc+"</b>] and Actual [<b>"+a_selectedTradeCode+"</b>]  . ");
	 	 			TestUtil.reportStatus("Trade Code Description on Policy Details Screen is Correct.", "Pass", true);
	 	 			
					break;

				default:
					break;
				}
	 			
	 			return true;
	 		}
		}catch(Throwable t){
			return false;
		}
	}
	
	
	/**
	 * This method selects the specified covers from cover Page
	 */
	    
	public boolean funcCovers(Map<Object, Object> map_data){
		
		boolean retvalue = true;
		
	    try {
		     customAssert.assertTrue(common.funcPageNavigation("Covers", ""),"Cover page is having issue(S)");
		 	 k.pressDownKeyonPage();
		 	 String all_cover = ObjectMap.properties.getProperty(CommonFunction.product+"_CD_AllCovers");
		 	 String[] split_all_covers = all_cover.split(",");
		 	 for(String coverWithLocator : split_all_covers){
		 		 String coverWithoutLocator = coverWithLocator.split("__")[0];
		 		 try{
		 		if(((String)map_data.get("CD_"+coverWithoutLocator)).equals("Yes")){
		 			
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
						if(common.currentRunningFlow.equalsIgnoreCase("MTA")){
							common.CoversDetails_data_list.add(coverName);
						}
					}else{
						TestUtil.reportStatus("Cover: <b>"+coverName+"</b> is checked by default.", "Info", false);
					}
				
			
				
		 
		}catch(Throwable t){
			
			System.out.println("Error while selecting Cover - "+t.getMessage());
			result=false;
		}
		return result;
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
			case "CAN":
 				data_map = common.CAN_excel_data_map;
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
   				/*if((String)data_map.get("NB_Status")!=null && ((String)data_map.get("NB_Status")).equalsIgnoreCase("ReQuote"))
   					trasacSummaryType="ReQuote Summary";*/
   				String ExpecteDueDate = "";
   				switch (trasacSummaryType) {
   				case "New Business" : 
   					
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
   						
   						if(Recipient.contains("Aviva") || Recipient.contains("NIG")){
   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   						if(Recipient.equalsIgnoreCase("PEN")){
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
   						
   						if(Recipient.contains("Aviva") || Recipient.contains("NIG")){
   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   						if(Recipient.equalsIgnoreCase("PEN")){
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
   						
   						if(Recipient.contains("Aviva") || Recipient.contains("NIG")){
   							double carrierAmount = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + carrierAmount;
   						}
   						if(Recipient.equalsIgnoreCase("PEN")){
   							double PEN_Amount = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + PEN_Amount;
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
   						
   						if(Recipient.contains("Aviva") || Recipient.contains("NIG")){
   							double carrierAmount = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + carrierAmount;
   						}
   						if(Recipient.equalsIgnoreCase("PEN")){
   							double PEN_Amount = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + PEN_Amount;
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
   						
   						if(Recipient.contains("Aviva") || Recipient.contains("NIG")){
   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   						if(Recipient.equalsIgnoreCase("PEN")){
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
   						
   						if(Recipient.contains("Aviva") || Recipient.contains("NIG")){
   							double LegalExpenses = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + LegalExpenses;
   						}
   						if(Recipient.equalsIgnoreCase("PEN")){
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
   						
   						if(Recipient.contains("Aviva") || Recipient.contains("NIG")){
   							double carrierAmount = calculateCarrierTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + carrierAmount;
   						}
   						if(Recipient.equalsIgnoreCase("PEN")){
   							double PEN_Amount = calculatePENTS(code,data_map,trasacSummaryType,j,td);	
   							Total = Total + PEN_Amount;
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
					if(!CommonFunction_VELA.product.equalsIgnoreCase("XOE")){
						customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}
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
					if(!CommonFunction_VELA.product.equalsIgnoreCase("XOE")){
						customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}
					customAssert.assertTrue(common.funcGoOnCover(common.NB_excel_data_map), "Go On Cover function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					if(TestBase.product.equals("SPI")){
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover (Unconfirmed)"), "Verify Policy Status (On Cover (Unconfirmed)) function is having issue(S) . ");
						customAssert.assertTrue(common_SPI.func_Confirm_Policy(), "Error while changing SPI policy Status from Unconfirmed to Confirmed . ");
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,(String)map_data.get("NB_Status")), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}else{
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,(String)map_data.get("NB_Status")), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}
					if(!CommonFunction_VELA.product.equalsIgnoreCase("XOE")){
						customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}
					customAssert.assertTrue(common_Zennor.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
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
					if(!CommonFunction_VELA.product.equalsIgnoreCase("XOE")){
						customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}
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
					if(!CommonFunction_VELA.product.equalsIgnoreCase("XOE")){
						customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}
					customAssert.assertTrue(common.funcGoOnCover(common.NB_excel_data_map), "Go On Cover function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					if(TestBase.product.equals("SPI")){
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover (Unconfirmed)"), "Verify Policy Status (On Cover (Unconfirmed)) function is having issue(S) . ");
						customAssert.assertTrue(common_SPI.func_Confirm_Policy(), "Error while changing SPI policy Status from Unconfirmed to Confirmed . ");
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}else{
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}	if(!CommonFunction_VELA.product.equalsIgnoreCase("XOE")){
						customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}
					customAssert.assertTrue(common_Zennor.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					TestUtil.reportStatus("Policy has been created with mentioned Status :<b>[ Submitted->Indicate->Indication Accepted->Quoted->On Cover ]</b>", "Info", true);
					
					break;
				case "ReQuote":
					quoteStatus = "ReQuote";
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
					Assert.assertTrue(common_VELA.funcQuoteCheck(common.NB_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.NB_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Quoted"), "Verify Policy Status (Quoted) function is having issue(S) . ");
					customAssert.assertTrue(common.funcButtonSelection("Re-Quote"));
					TestUtil.reportStatus("Policy Status Changed to Requote. ", "Info", true);
					customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Submitted"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
					common_POF.isNBRquoteStarted = true;
					customAssert.assertTrue(common_Zennor.decideRewindMethod());
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
					Assert.assertTrue(common_VELA.funcQuoteCheck(common.NB_excel_data_map));
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Quoted"), "Verify Policy Status (Quoted) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification_Rewind("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					
					customAssert.assertTrue(common.funcGoOnCover(common.NB_excel_data_map), "Go On Cover function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					
					if(TestBase.product.equals("SPI")){
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover (Unconfirmed)"), "Verify Policy Status (On Cover (Unconfirmed)) function is having issue(S) . ");
						customAssert.assertTrue(common_SPI.func_Confirm_Policy(), "Error while changing SPI policy Status from Unconfirmed to Confirmed . ");
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}else{
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}
					customAssert.assertTrue(funcPDFdocumentVerification_Rewind("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					
					customAssert.assertTrue(common_Zennor.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", CommonFunction_VELA.product,CommonFunction_VELA.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
					quoteStatus = "";
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("NB_Status")+"  ]</b> status. ", "Info", true);
					
					break;
				case "Rewind":
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Submitted"), "Verify Policy Status (Submitted) function is having issue(S) . ");
					Assert.assertTrue(common_VELA.funcQuoteCheck(common.NB_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.NB_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Quoted"), "Verify Policy Status (Quoted) function is having issue(S) . ");
					if(!CommonFunction_VELA.product.equalsIgnoreCase("XOE")){
						customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}
					customAssert.assertTrue(common.funcGoOnCover(common.NB_excel_data_map), "Go On Cover function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					if(TestBase.product.equals("SPI")){
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover (Unconfirmed)"), "Verify Policy Status (On Cover (Unconfirmed)) function is having issue(S) . ");
						customAssert.assertTrue(common_SPI.func_Confirm_Policy(), "Error while changing SPI policy Status from Unconfirmed to Confirmed . ");
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,(String)map_data.get("NB_Status")), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}else{
						customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}
					if(!CommonFunction_VELA.product.equalsIgnoreCase("XOE")){
						customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					}
					customAssert.assertTrue(common_Zennor.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
					customAssert.assertTrue(common.funcRewind());
					customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
					customAssert.assertTrue(common_Zennor.decideRewindMethod());
					customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
					customAssert.assertTrue(common_Zennor.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
					
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("NB_Status")+"  ]</b> status. ", "Info", true);
					break;
				case "Cancelled":
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.CAN_excel_data_map,code,event,"Cancelled"), "Verify Policy Status (Cancelled) function is having issue(S) . ");
					customAssert.assertTrue(common_Zennor.transactionSummary((String)common.CAN_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
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
				
					Assert.assertTrue(common.funcQuoteCheck(common.NB_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.NB_excel_data_map.put("QuoteDate", quoteDate);
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
				
					Assert.assertTrue(common.funcQuoteCheck(common.NB_excel_data_map));
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
					customAssert.assertTrue(verifyPolicyReferanceNumber(map_data , code , event));
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Error in PDF Verification (Documents)(Endorsement On Cover) function is having issue(S) . ");
					
					customAssert.assertTrue(common_Zennor.transactionSummary((String)common.MTA_excel_data_map.get("Automation Key"), "", code, event), "MTA Transaction Summary function is having issue(S) . ");
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
					customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Error in PDF Verification (Draft Documents) function is having issue(S) . ");
				
					customAssert.assertTrue(common.funcGoOnCover_Endorsement(common.NB_excel_data_map), "GoOnCover_Endorsement function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement On Cover"), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
					customAssert.assertTrue(verifyPolicyReferanceNumber(map_data , code , event));
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Error in PDF Verification (Documents)(Endorsement On Cover) function is having issue(S) . ");
					customAssert.assertTrue(common_Zennor.transactionSummary((String)common.MTA_excel_data_map.get("Automation Key"), "", code, event), "MTA Transaction Summary function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
					customAssert.assertTrue(common.funcRewind());
					customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status (Endorsement Submitted (Rewind)) function is having issue(S) . ");
					customAssert.assertTrue(common_Zennor.decideRewindMethod());
					customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
					customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,event,"Endorsement On Cover"), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
					customAssert.assertTrue(verifyPolicyReferanceNumber(map_data , code , event));
					customAssert.assertTrue(funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
				
				
				//	customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Error in PDF Verification (Documents)(Endorsement On Cover after MTA Rewind) function is having issue(S) . ");
				
					customAssert.assertTrue(common_Zennor.transactionSummary((String)common.MTA_excel_data_map.get("Automation Key"), "", CommonFunction_VELA.product,CommonFunction_VELA.businessEvent), "Transaction Summary function is having issue(S) after MTA Rewind  . ");
					
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
					customAssert.assertTrue(common_Zennor.transactionSummary((String)common.CAN_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
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
					customAssert.assertTrue(common_Zennor.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
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
					customAssert.assertTrue(funcPDFdocumentVerification("Draft Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(common.funcGoOnCover(common.Renewal_excel_data_map), "Go On Cover function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(common_Zennor.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
					customAssert.assertTrue(common.funcRewind());
					customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Renewal Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
					customAssert.assertTrue(common_Zennor.decideRewindMethod());
					customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
					
					customAssert.assertTrue(funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
					customAssert.assertTrue(common_Zennor.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Rewind  . ");
					
					TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)map_data.get("NB_Status")+"  ]</b> status. ", "Info", true);
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
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Verify Policy Status (On Cover) function is having issue(S) . ");
					customAssert.assertTrue(common_Zennor.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", code, event), "Transaction Summary function is having issue(S) . ");
					common.MTA_excel_data_map = new HashMap<>();
					common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
					Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "POF_MTA.xlsx");
					common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, "POF_MTA_01");
					common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
							Events_Suite_TC_Xls);
					
					common_POF.MTAFlow(code, "MTA");
					
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Endorsement Submitted"), "Verify Policy Status (Endorsement Submitted) function is having issue(S) . ");
					customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"),"Unable to Click on Assign Underwriter button . ");
					customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter.");
				
					Assert.assertTrue(common.funcQuoteCheck(common.Renewal_excel_data_map));
					//This will get UK date after putting policy on Quoted status.
					quoteDate = common.getUKDate();
					common.MTA_excel_data_map.put("QuoteDate", quoteDate);
					/////////////////////////////////////////////////////////////
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Endorsement Quoted"), "Verify Policy Status (Endorsement Quoted) function is having issue(S) . ");
					customAssert.assertTrue(common.funcGoOnCover_Endorsement(common.Renewal_excel_data_map), "GoOnCover_Endorsement function is having issue(S) . ");
					customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
					customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,code,event,"Endorsement On Cover"), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
					customAssert.assertTrue(verifyPolicyReferanceNumber(map_data , code , event));
					
					customAssert.assertTrue(funcPDFdocumentVerification("Documents"), "Error in PDF Verification (Documents)(Endorsement On Cover) function is having issue(S) . ");
					
					customAssert.assertTrue(common_Zennor.transactionSummary((String)common.MTA_excel_data_map.get("Automation Key"), "", code, event), "MTA Transaction Summary function is having issue(S) . ");
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
					doc_fail_count = doc_fail_count + iteratePDFDocuments(docType);
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
					if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.Renewal_excel_data_map.get("Renewal_ClientName")), "Insured Name : "+(String)common.Renewal_excel_data_map.get("Renewal_ClientName") , fileName);
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)common.NB_excel_data_map.get("NB_ClientName")), "Insured Name : "+(String)common.NB_excel_data_map.get("NB_ClientName") , fileName);
					}
					
				}else{
				
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("INSURED NAME - "+(String)mdata.get("NB_ClientName")), "Insured Name : "+(String)mdata.get("NB_ClientName") , fileName);
				}
				if(!TestBase.businessEvent.equals("Renewal")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.NB_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.NB_excel_data_map.get("QC_AgencyName") , fileName);
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains((String)common.Renewal_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.Renewal_excel_data_map.get("QC_AgencyName") , fileName);
				}
				
				if(docType.contains("Draft")){
					if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
						if(common.currentRunningFlow.equalsIgnoreCase("MTA") && TestBase.businessEvent.equalsIgnoreCase("Renewal")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.Renewal_excel_data_map.get("Renewal_QuoteNumber")) ,"Quote Reference : "+common.Renewal_excel_data_map.get("Renewal_QuoteNumber"),fileName);
							
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quotation Date: "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), -incrementalDays)) ,"Quote Date : "+common.daysIncrement((String)common.Renewal_excel_data_map.get("PS_PolicyStartDate"), -incrementalDays),fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.NB_excel_data_map.get("NB_QuoteNumber")) ,"Quote Reference : "+common.NB_excel_data_map.get("NB_QuoteNumber"),fileName);
							
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quotation Date: "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), -incrementalDays)) ,"Quote Date : "+common.daysIncrement((String)common.NB_excel_data_map.get("QC_InceptionDate"), -incrementalDays),fileName);
						}
						
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Quote Reference: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_QuoteNumber")) ,"Quote Reference : "+common.Renewal_excel_data_map.get(common.currentRunningFlow+"_QuoteNumber"),fileName);
					}
				}else{
					if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
						if(common.currentRunningFlow.equalsIgnoreCase("MTA")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.MTA_excel_data_map.get("PG_CarrierPolicyNumber")) ,"Policy Number : "+common.MTA_excel_data_map.get("PG_CarrierPolicyNumber"),fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.NB_excel_data_map.get("PG_CarrierPolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("PG_CarrierPolicyNumber"),fileName);
						}
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.Renewal_excel_data_map.get("PG_CarrierPolicyNumber")) ,"Policy Number : "+common.Renewal_excel_data_map.get("PG_CarrierPolicyNumber"),fileName);
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
				
					if(((String)mdata.get("CD_MaterialDamage")).equals("Yes")){
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
						
					}
					
					// No any validation for BI Cover 
					//if(((String)mdata.get("CD_BusinessInterruption")).equals("Yes")){
						
					//}
					
					if(((String)mdata.get("CD_Liabilities-POL")).equals("Yes")){
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
						
					}
					
					if(((String)mdata.get("CD_Terrorism")).equals("Yes")){
						String sCarrier = (String)mdata.get("PG_Carrier");
						if(sCarrier.contains("NIG")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Terrorism Insured"), "Terrorism Insured" , fileName);
						}else{
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Terrorism Operative"), "Terrorism Operative" , fileName);
						}					
					}else{
						
					}
					
					if(((String)mdata.get("CD_BespokeCover")).equals("Yes")){
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
						
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium excluding Terrorism £"+formatter.format((int) Math.floor(PremiumExcTerrDocAct))), "Premium excluding Terrorism £"+formatter.format((int) Math.floor(PremiumExcTerrDocAct)) , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Terrorism Premium £"+formatter.format((int) Math.floor(TerPremDocAct))), "Terrorism Premium £"+formatter.format((int) Math.floor(TerPremDocAct)) , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+formatter.format((int) Math.floor(InsTaxDocAct))), "Insurance Premium Tax £"+formatter.format((int) Math.floor(InsTaxDocAct)) , fileName);
						TotalPremiumWithAdminDocAct = PremiumExcTerrDocAct + TerPremDocAct + InsTaxDocAct;
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+formatter.format((int) Math.floor(TotalPremiumWithAdminDocAct))), "TOTAL £"+formatter.format((int) Math.floor(TotalPremiumWithAdminDocAct)) , fileName);
						
					}else if(common.currentRunningFlow.contains("MTA")){
						DecimalFormat newSum = new DecimalFormat("#,###.00");
						
						if(parsedText.contains("Additional Premium excluding Terrorism")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Additional Premium excluding Terrorism £"+formatter.format((int) Math.floor(Math.abs(AdditionalExcTerrDocAct)))), "Additional Premium excluding Terrorism £"+formatter.format((int) Math.floor(Math.abs(AdditionalExcTerrDocAct))) , fileName);
						}else if(parsedText.contains("Return Premium excluding Terrorism")){
							fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Return Premium excluding Terrorism £"+formatter.format((int) Math.floor(Math.abs(AdditionalExcTerrDocAct)))), "Return Premium excluding Terrorism £"+formatter.format((int) Math.floor(Math.abs(AdditionalExcTerrDocAct))) , fileName);
						}
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+formatter.format((int) Math.floor(Math.abs(AdditionalInsTaxDocAct)))), "Insurance Premium Tax £"+formatter.format((int) Math.floor(Math.abs(AdditionalInsTaxDocAct))) , fileName);
						AdditionalPWithAdminDocAct = AdditionalExcTerrDocAct + AdditionalTerPDocAct + AdditionalInsTaxDocAct;
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+formatter.format((int) Math.floor(Math.abs(AdditionalPWithAdminDocAct)))), "TOTAL £"+formatter.format((int) Math.floor(Math.abs(AdditionalPWithAdminDocAct))) , fileName);
						
					}
					
				
				
				// verify excess :
					if(((String)mdata.get("CD_MaterialDamage")).equals("Yes")){
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
					}
					
					
					//This will verify endorsement in Section Endorsements.
					String flag="true";
					int k = 0;
					System.out.println(common.EndorsementCollectiveData);
					
					Iterator collectiveDataIT = common.EndorsementCollectiveData.entrySet().iterator();
					while(collectiveDataIT.hasNext()){
						k = 0;
						Map.Entry collectiveDataMapValue = (Map.Entry)collectiveDataIT.next();
						String collectiveEndorsementCode = collectiveDataMapValue.getKey().toString();
						
						Iterator individualDataIT = common.EndorsementCollectiveData.get(collectiveEndorsementCode).entrySet().iterator();
						while(individualDataIT.hasNext()){
							Map.Entry individualDataMapValue = (Map.Entry)individualDataIT.next();
							String individualEndorsementSection = individualDataMapValue.getValue().toString();
							
							String mergedEndorsementText = "";
							if(docType.contains("Draft")){
								if(common.EndorsementCollectiveData.get(collectiveEndorsementCode).containsValue("Policy")){
									mergedEndorsementText = individualEndorsementSection;
									k++;
								}else{
									mergedEndorsementText = individualEndorsementSection;
									k++;
								}
								
								if(parsedText.contains(mergedEndorsementText) && k==3){
									TestUtil.reportStatus("Verified Endorsement : [<b> "+collectiveEndorsementCode+" -- "+individualEndorsementSection+"</b>] in "+fileName+" document.", "Pass", false);
									//TestUtil.reportStatus("Applied Endorsement <b> [ "+collectiveEndorsementCode+" </b> with title as <b> "+individualEndorsementSection+" ] </b> is present on premium summary page.", "Info", false);
									fail_count = fail_count + 0;
									flag = "true";
									break;
								}else{
									flag="false";
								}
								
								
							}else{
								
								if(common.EndorsementCollectiveData.get(collectiveEndorsementCode).containsValue("Endorsement")){
									mergedEndorsementText = individualEndorsementSection;
									k++;
									
									if(parsedText.contains(mergedEndorsementText) && k==3){
										TestUtil.reportStatus("Verified Endorsement : [<b> "+collectiveEndorsementCode+" -- "+individualEndorsementSection+"</b>] in "+fileName+" document.", "Pass", false);
										//TestUtil.reportStatus("Applied Endorsement <b> [ "+collectiveEndorsementCode+" </b> with title as <b> "+individualEndorsementSection+" ] </b> is present on premium summary page.", "Info", false);
										fail_count = fail_count + 0;
										flag = "true";
										break;
									}else{
										flag="false";
									}
								}
							}
							
							
						}
						if(flag.equalsIgnoreCase("false")){
							TestUtil.reportStatus("<p style='color:red'> PDF Document: "+fileName+" does not contain [<b> "+collectiveEndorsementCode+" </b>] </p>", "Fail", false);
							//TestUtil.reportStatus("<p style='color:red'> Endorsement <b> [ "+collectiveEndorsementCode+" ] </b> is not present on premium summary page.</p>", "Info", false);
							fail_count = fail_count + 1;
						}
					}
					
					//Validate extra endorsement present on Endorsement screen should not be present on Premium Screen.
					Iterator extraEndorsementDetailsIT = common.ExtraEndorsementList.entrySet().iterator();
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
				if(((String)mdata.get("CD_MaterialDamage")).equals("Yes")){
					String sExcessNo = (String)mdata.get("EXS_Properties");
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
				}
				
				break;
		
			case "Aviva Terrorism Certificate":
				DecimalFormat newSum = new DecimalFormat("#,###");
				formatter = new DecimalFormat("#,###,###.##");
				System.out.println(parsedText);
				
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TERRORISM INSURANCE CERTIFICATE"), "TERRORISM INSURANCE CERTIFICATE" , fileName);		
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurer(s): Aviva Insurance Limited"), "Insurer(s): Aviva Insurance Limited" , fileName);
				
				if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
					if(common.currentRunningFlow.equalsIgnoreCase("MTA")){
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("General Cover Policy No: "+(String)common.MTA_excel_data_map.get("PG_CarrierPolicyNumber")), "General Cover Policy No: "+(String)common.MTA_excel_data_map.get("PG_CarrierPolicyNumber") , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("The Insured: "+(String)common.Renewal_excel_data_map.get("Renewal_ClientName")), "The Insured: "+(String)common.Renewal_excel_data_map.get("Renewal_ClientName") , fileName);
					}else{
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("General Cover Policy No: "+(String)common.Renewal_excel_data_map.get("PG_CarrierPolicyNumber")), "General Cover Policy No: "+(String)common.Renewal_excel_data_map.get("PG_CarrierPolicyNumber") , fileName);
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("The Insured: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName")), "The Insured: "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_ClientName") , fileName);
					}
					
				}else if(common.currentRunningFlow.equalsIgnoreCase("MTA")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("General Cover Policy No: "+(String)common.MTA_excel_data_map.get("PG_CarrierPolicyNumber")), "General Cover Policy No: "+(String)common.MTA_excel_data_map.get("PG_CarrierPolicyNumber") , fileName);
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("The Insured: "+(String)common.NB_excel_data_map.get("NB_ClientName")), "The Insured: "+(String)common.NB_excel_data_map.get("NB_ClientName") , fileName);
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("General Cover Policy No: "+(String)common.NB_excel_data_map.get("PG_CarrierPolicyNumber")), "General Cover Policy No: "+(String)common.NB_excel_data_map.get("PG_CarrierPolicyNumber") , fileName);
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("The Insured: "+(String)common.NB_excel_data_map.get(common.currentRunningFlow+"_ClientName")), "The Insured: "+(String)common.NB_excel_data_map.get(common.currentRunningFlow+"_ClientName") , fileName);
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yyyy");
				String sDuration = (String)mdata.get("PS_Duration");
				int EndDate = Integer.parseInt( sDuration);
				EndDate--;
				
				if(common.currentRunningFlow.equalsIgnoreCase("MTA")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: Effective:         "+common.daysIncrement((String)mdata.get("MTA_EffectiveDate"),0)), "Period of Insurance: Effective:         "+common.daysIncrement((String)mdata.get("MTA_EffectiveDate"),0) , fileName);
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Period of Insurance: Effective:         "+common.daysIncrement((String)mdata.get("QC_InceptionDate"),0)), "Period of Insurance: Effective:         "+common.daysIncrement((String)mdata.get("QC_InceptionDate"),0) , fileName);
				}
				
				
				if(parsedText.contains("Expiring:         "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"),0))){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Expiring:         "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"),0)), "Expiring:         "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"),0) , fileName);
				}else{
					
					int t_Count = fail_count;
					
					String eDate[] = common.daysIncrement((String)mdata.get("PS_PolicyEndDate"),0).split(" ");
					String datePart1 = eDate[0] +" "+ eDate[1]; 
					t_Count = t_Count + CommonFunction_VELA.verification(parsedText.contains("Expiring:         "+datePart1), "Expiring:         "+datePart1 , fileName);
					t_Count = t_Count + CommonFunction_VELA.verification(parsedText.contains(eDate[2]), eDate[2] , fileName);
					
					if(t_Count == 0){
						fail_count = 0;
					}else{
						fail_count = t_Count;
					}
				}
				
				String sVal = "Renewal Date: "+common.daysIncrement((String)mdata.get("PS_PolicyEndDate"),1);
				sVal = sVal.replaceAll("&"+"nbsp;", " "); 
				sVal = sVal.replaceAll(String.valueOf((char) 160), " ");
				
				fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains(sVal), sVal , fileName);
				
				if(common.currentRunningFlow.contains("NB")){
					
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium Details: Insurance Premium:          £"+formatter.format((int) Math.floor(TerPremDocAct))), "Premium Details: Insurance Premium:          £"+formatter.format((int) Math.floor(TerPremDocAct)) , fileName);
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax:   £"+formatter.format((int) Math.floor(InsTaxTerrDoc))), "Insurance Premium Tax:   £"+formatter.format((int) Math.floor(InsTaxTerrDoc)) , fileName);
					tpTotal = TerPremDocAct + InsTaxTerrDoc;
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Total Amount Due:           £"+formatter.format((int) Math.floor(tpTotal))), "Total Amount Due:           £"+formatter.format((int) Math.floor(tpTotal)) , fileName);
					
				}else{
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Premium Details: Insurance Premium:          £"+formatter.format((int) Math.floor(TerPremDocAct))), "Premium Details: Insurance Premium:          £"+formatter.format((int) Math.floor(TerPremDocAct)) , fileName);
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax:   £"+formatter.format((int) Math.floor(InsTaxTerrDoc))), "Insurance Premium Tax:   £"+formatter.format((int) Math.floor(InsTaxTerrDoc)) , fileName);
					
					tpTotal = TerPremDocAct + InsTaxTerrDoc;
				
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Total Amount Due:           £"+formatter.format((int) Math.floor(tpTotal))), "Total Amount Due:           £"+formatter.format((int) Math.floor(tpTotal)) , fileName);
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
				System.out.println(parsedText);
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
				
				if(common.currentRunningFlow.contains("NB") || common.currentRunningFlow.contains("MTA")){
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("BROKER NAME – "+(String)common.NB_excel_data_map.get("QC_AgencyName")), "BROKER NAME - "+(String)common.NB_excel_data_map.get("QC_AgencyName") , fileName);
				}else{
					String sVal = "BROKER NAME - "+(String)mdata.get("QC_AgencyName");
					sVal = sVal.replaceAll("&"+"nbsp;", " "); 
					sVal = sVal.replaceAll(String.valueOf((char) 160), " ");
					fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("BROKER NAME - "+(String)mdata.get("QC_AgencyName")), "BROKER NAME - "+(String)mdata.get("QC_AgencyName") , fileName);
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
						fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Policy Number: "+(String)common.MTA_excel_data_map.get("PG_CarrierPolicyNumber")) ,"Policy Number : "+common.NB_excel_data_map.get("PG_CarrierPolicyNumber"),fileName);						
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
                           
                           if(parsedText.contains("Additional Premium excluding Terrorism")){
                                  fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Additional Premium excluding Terrorism £"+newSum.format(rewindMTADoc_Premium)), "Additional Premium excluding Terrorism £"+newSum.format(rewindMTADoc_Premium) , fileName);
                           }else if(parsedText.contains("Return Premium")){
                        	   rewindMTADoc_Premium = Math.abs(common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium"));
                                  fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Return Premium £"+formatter.format((int) Math.floor(Math.abs(rewindMTADoc_Premium)))), "Return Premium £"+formatter.format((int) Math.floor(Math.abs(rewindMTADoc_Premium))) , fileName);
                           }
                            
                           String sCarrier = (String)mdata.get("PG_Carrier");
                           if(sCarrier.contains("Aviva")){
                                  fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Additional Premium Terrorism £"+newSum.format(rewindMTADoc_TerP)), "Additional Premium Terrorism £"+newSum.format(rewindMTADoc_TerP) , fileName);
                           }                                        
                           rewindMTADoc_InsPTax =  Math.abs(common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax"));
                           fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("Insurance Premium Tax £"+formatter.format((int) Math.floor(Math.abs(rewindMTADoc_InsPTax)))), "Insurance Premium Tax £"+formatter.format((int) Math.floor(Math.abs(rewindMTADoc_InsPTax))) , fileName);
                           
                           if(sCarrier.contains("NIG")){
                        	   rewindMTADoc_TotalP  = rewindMTADoc_Premium + rewindMTADoc_InsPTax;
                           }else{
                        	   rewindMTADoc_TotalP  = rewindMTADoc_Premium + rewindMTADoc_TerP + rewindMTADoc_InsPTax;
                           }
                          
                           fail_count = fail_count + CommonFunction_VELA.verification(parsedText.contains("TOTAL £"+formatter.format((int) Math.floor(Math.abs(rewindMTADoc_TotalP)))), "TOTAL £"+formatter.format((int) Math.floor(Math.abs(rewindMTADoc_TotalP))) , fileName);
                           
                    }
				
                    String sExcessNo = null;
				// verify excess :
                    if(((String)mdata.get("CD_MaterialDamage")).equals("Yes")){
                    	sExcessNo = (String)mdata.get("EXS_Properties");
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
	
	public double calculateCarrierTS(String fileName,Map<Object, Object> data_map,String code ,int j,int td){
		try{
			String Premium = null;
			String IPT = null;
            if(common.currentRunningFlow.equals("MTA")){
				
				try{
					Premium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Net Net Premium"));
					IPT = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax"));
				}catch(NullPointerException npe){
					Premium = "0.0";
					IPT = "0.0";
				}
			}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_Zennor.Can_ReturnP_Values_Map.get("Totals").get("Net Net Premium"));
					IPT = Double.toString(common_Zennor.Can_ReturnP_Values_Map.get("Totals").get("Insurance Tax"));
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
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+3)+"]")).getText();
			double Due = Double.parseDouble(Premium)+ Double.parseDouble(IPT);
			String Dueamt= common.roundedOff(Double.toString(Due)) ;
			CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), data_map.get("PG_Carrier")+" Due ");
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
            if(common.currentRunningFlow.equals("MTA")){
				
				try{
					Premium = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Pen Comm"));
					//IPT = Double.toString(common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax"));
				}catch(NullPointerException npe){
					Premium = "0.0";
					//IPT = "0.0";
				}
			}
            if(common.currentRunningFlow.equals("CAN")){
				
				try{
					Premium = Double.toString(common_Zennor.Can_ReturnP_Values_Map.get("Totals").get("Pen Comm"));

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
			if(common.currentRunningFlow.equals("Renewal")){
				
				if(!((String)data_map.get("PS_Duration")).equalsIgnoreCase("365")){
					Premium = Double.toString(transaction_Premium_Values.get("Totals").get("Pen Comm"));
				
				}else{
					Premium = (String)data_map.get("PS_PenCommTotal");
				}
				
			}
			String part1= "//*[@id='table0']/tbody";
			String actualDue = driver.findElement(By.xpath(part1+"/tr["+j+"]/td["+(td+1)+"]")).getText();
			double Due = Double.parseDouble(Premium);
			String Dueamt= common.roundedOff(Double.toString(Due)) ;
			CommonFunction.compareValues(Math.abs(Double.parseDouble(Dueamt)), Math.abs(Double.parseDouble(actualDue)), "PEN Due ");
			return Due;
		
		}catch(Throwable t) {
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			TestUtil.reportFunctionFailed("Failed in "+methodName+" function");    
			k.reportErr("Failed in "+methodName+" function", t);
			Assert.fail("Failed in Calculate Legal Expenses ammount.  \n", t);
			return 0;
		}

	}
	
public boolean decideRewindMethod(){
	 
		
		try {
			
			
			switch (CommonFunction_VELA.product) {
			case "POF":
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

public boolean funcRewindCoversCheck(Map<Object, Object> map_data){
	//CoversDetails_data_list.clear();
    boolean retvalue = true;
    CoversDetails_data_list = new ArrayList<>();
    try {
    	if(CommonFunction_VELA.product.equalsIgnoreCase("XOE")){
    		customAssert.assertTrue(common.funcPageNavigation("Additional Covers", ""),"Cover page is having issue(S)");
    	}else{
    		customAssert.assertTrue(common.funcPageNavigation("Covers", ""),"Cover page is having issue(S)");
    	}
 	 
     String all_cover = ObjectMap.properties.getProperty(CommonFunction_VELA.product+"_CD_AllCovers");
 	 String[] split_all_covers = all_cover.split(",");
 	 for(String coverWithLocator : split_all_covers){
 		 String coverWithoutLocator = coverWithLocator.split("__")[0];
 		 try{
 			 if(((String)map_data.get("CD_Add_"+coverWithoutLocator)).equals("Yes")){
	 			CoversDetails_data_list.add(coverWithoutLocator);
	 			customAssert.assertTrue(selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
	 		}else if(((String)map_data.get("CD_Add_"+coverWithoutLocator)).equals("No")){
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
    	return false;
 }
}

public boolean deSelectCovers(String coverNameWithLocator,Map<Object, Object> map_data){
	 boolean result=true;
	 String c_locator = null;
	 String coverName = null;
	 try{
			coverName = coverNameWithLocator.split("__")[0];	
			c_locator = coverNameWithLocator.split("__")[1];
			if(c_locator.equals("md")){
				if (driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"')]")).isSelected()){
					driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"')]")).click();  
					k.waitTwoSeconds();
		           	Assert.assertTrue(k.AcceptPopup(), "Unable to locate popup box.");
					TestUtil.reportStatus("Cover  <b>"+coverName+"</b> is Unchecked ", "Info", false);
				}else{
					TestUtil.reportStatus("Cover: <b>"+coverName+"</b> is already not selected.", "Info", false);
				}
			}else if(c_locator.equals("comb_liab") || c_locator.equals("xoc_pl") || c_locator.equals("xoc_el") || c_locator.equals("xoc_jct_6_5_1") || c_locator.equals("xoc_third_party_motor")){
				if (driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"')]")).isSelected()){
					driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"')]")).click();  
					k.waitTwoSeconds();
		           	Assert.assertTrue(k.AcceptPopup(), "Unable to locate popup box.");
					TestUtil.reportStatus("Cover  <b>"+coverName+"</b> is Unchecked ", "Info", false);
				}else{
					TestUtil.reportStatus("Cover: <b>"+coverName+"</b> is already not selected.", "Info", false);
				}
			}else if(c_locator.equals("PEL")){
				
			}else if(coverName.equalsIgnoreCase("HireCoverPlus")){
				String agency_Name = driver.findElement(By.xpath("html/body/div[3]/form/table/tbody/tr[2]/td[2]")).getText();
				if(!agency_Name.equals("M722")){
					TestUtil.reportStatus("Cover: <b>"+coverName+"</b> is not selected as Agency Selected Is Not 'Arthur J. Gallagher (UK) Ltd' ", "Info", false);
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
			}
			else{
				k.waitTwoSeconds();
				if (driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"')]")).isSelected()){
					k.scrollInViewByXpath("//*[contains(@name,'"+c_locator+"')]");
					driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"')]")).click();     
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


public String getUniquePolicyReferanceNumber(Map<Object, Object> map_data) {
	
	String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.US).format(new Date());
	String year = new SimpleDateFormat("yy", Locale.US).format(new Date());
	String code = (String)map_data.get(common.currentRunningFlow+"_PolicyReferanceNumberCode");
	
	return year+"/"+code+"/"+timeStamp;
	
}

public boolean funcPremiumSummary_MTA(Map<Object, Object> map_data,String code,String event) {
	
	boolean r_value= true;
	Date currentDate = new Date();
	String testName = (String)map_data.get("Automation Key");
	String customPolicyDuration=null,policy_Start_date=null,Policy_End_Date=null;
	SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
	int policy_Duration=0;
	
	try{
		customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
		if(!TestBase.businessEvent.equalsIgnoreCase("Renewal")){
			customAssert.assertTrue(common.verifyEndorsementONPremiumSummary(common.MTA_excel_data_map),"Endorsement on Premium is having issue(S).");	
		}
		
	
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
				//customAssert.assertTrue(funcGetPremiumsForRenewal(),"Error while getting premium for renewals");
		break;
	}
	
	customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
	customPolicyDuration = k.getText("Policy_Duration");
	customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,map_data),"Error while writing Policy Duration data to excel .");
	TestUtil.reportStatus("MTA Policy Duration = "+customPolicyDuration+" Days . ", "Info", true);
	customAssert.assertTrue(Verify_premiumSummaryTable(), "Error while verifying Premium Summary table in MTA  .");
	if(!common_POF.isMTARewindStarted){
		customAssert.assertTrue(common_POF.func_Flat_Premiums_(common.MTA_excel_data_map,common.MTA_Structure_of_InnerPagesMaps), "Error while verifying Flat Premium table in MTA  .");
	}
	customAssert.assertTrue(func_MTATransactionDetailsPremiumTable(code, event), "Error while verifying Transaction Details Premium table on premium Summary page .");
	Assert.assertTrue(funcTransactionDetailsMessage_MTA());

	// Verification of insurance Tax.
	
	if(Integer.parseInt(customPolicyDuration)!=365){
	}
	
	TestUtil.reportStatus("Premium Summary details are filled and Verified sucessfully after Endorsement . ", "Info", true);
	return r_value;
}catch(Throwable t){
		
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
			
			/*if(common_POF.isMTARewindFlow){
				common.transaction_Details_Premium_Values.clear();
				common.transaction_Details_Premium_Values.remove(key)
			}*/
			
			if(transactionDetails_Table.isDisplayed()){
				
				if(common_POF.isMTARewindFlow)
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
							if(common_POF.isMTARewindFlow){
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
						
						if(common_POF.isMTARewindFlow){
							//common.transaction_Details_Premium_Values.clear();
							common.transaction_Details_Premium_Values.remove(sectionName);
						}
						common.transaction_Details_Premium_Values.put(sectionName, transaction_Section_Vals);
					
						
					}else if(sectionName.contains("Flat")){
						FP_Entry=true;
					}else if(FP_Entry){
						continue;
					}
					
					
					/*switch(sectionName){
					
					case "Totals":
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
							common.transaction_Details_Premium_Values.put(sectionName, transaction_Section_Vals_Total);
					}
					
					break;
					
					case "Flat Premiums":
						break ;
						
					
					default:
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
						common.transaction_Details_Premium_Values.put(sectionName, transaction_Section_Vals);
					
					break;
					
					}*/
					
					
				}
				//System.out.println(transaction_Premium_Values);
				
				if(common_POF.isMTARewindFlow){
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
				
				String arrF_Premium[] = flatPremiumEntries.split(";");
				
				for(int i = 0; i < arrF_Premium.length; i ++){
					
					if(i == 0){
						FP_Covers = (String)common.MTA_Structure_of_InnerPagesMaps.get("Flat-Premiums").get(i).get("FP_Section");
					}else{
						FP_Covers = FP_Covers + ","+ (String)common.MTA_Structure_of_InnerPagesMaps.get("Flat-Premiums").get(i).get("FP_Section");
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
						common_POF.isFPEntries = true;isFP_Text="Yes";
						
						if(common_POF.isMTARewindFlow){
							isMTARewindFPEntries=true;}
					}
					
					if(common_POF.isFPEntries && isFP_Text.equalsIgnoreCase("No") && !s_Name.equals("Totals")){
						
						trans_error_val = trans_error_val + func_FP_Entries_Transaction_Details_Verification_MTA(s_Name,common.MTA_Structure_of_InnerPagesMaps);
						
						
					}else{
						if(s_Name.equals("Totals"))
							trans_error_val = trans_error_val + funcTransactionDetailsTable_Verification_Total_MTA(sectionNames,common.transaction_Details_Premium_Values);
						else if(!s_Name.contains("Flat"))
							trans_error_val = trans_error_val + funcTransactionDetailsTable_Verification_MTA(s_Name,common.transaction_Details_Premium_Values);
						
					}			
				}
				if(common_POF.isMTARewindFlow){
					
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

public int funcTransactionDetailsTable_Verification_MTA(String sectionName,Map<String,Map<String,Double>> transactionDetails_Premium_Values){

	Map<Object,Object> map_data = common.MTA_excel_data_map;
	Map<Object,Object> NB_map_data = common.NB_excel_data_map;
	Map<Object, Object> data_map = null;
	//String testName = (String)common.Renewal_excel_data_map.get("Automation Key");
	//String testName = (String)map_data.get("Automation Key");
	double NB_NNP = 0.0;
	double NB_SEL_NNP = 0.0,MTA_NNP=0.0;
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
	case "Business Interruption":
		code = "BusinessInterruption";
		cover_code = "BusinessInterruption";
		break;
	case "Property Owners Liabilities":
		code = "PropertyOwnersLiabilities";
		cover_code = "Liabilities-POL";
		break;
	case "Terrorism":
		code = "Terrorism";
		cover_code = "Terrorism";
		break;
	case "Bespoke Cover":
		code = "BespokeCover";
		cover_code = "BespokeCover";
		break;
	
	default:
			System.out.println("**Cover Name is not in Scope for POF**");
		break;
	
	}
	
try{
		
		TestUtil.reportStatus("---------------"+sectionName+"-----------------","Info",false);
		
		if(common_POF.isMTARewindFlow){ // MTA Rewind Flow
			
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
		
						
			double t_InsuranceTax = (t_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_IPT")))/100.0;
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

public int funcTransactionDetailsTable_Verification_Total_MTA(List<String> sectionNames,Map<String,Map<String,Double>> transaction_Premium_Values){
	
	try{
		
	Map<String,Double> trans_details_values = new HashMap<>();
	boolean Start_Fp = false;
	
	TestUtil.reportStatus("---------------Totals-----------------","Info",false);
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
				exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Gross Premium");
			}catch(Throwable t){
				continue;
			}
			}
	}
	}
	String t_grossP_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Gross Premium"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_grossP_actual)," Gross Premium");
	trans_details_values.put("Gross Premium",Double.parseDouble(t_grossP_actual));
	
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
				exp_value = exp_value + common.transaction_Details_Premium_Values.get(section+"_FP").get("Total Premium");
			}catch(Throwable t){
				continue;
			}
			}
	}
		
	}
	String t_p_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Total Premium"));
	
	trans_details_values.put("Total Premium",Double.parseDouble(t_p_actual));
	
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
//	return true;
	
		
}

public int func_FP_Entries_Verification_MTA(String sectionName,Map<String, List<Map<String, String>>> internal_data_map,int count){

	Map<Object,Object> map_data = common.MTA_excel_data_map;
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
	case "Property Owners Liabilities":
	case "Liabilities - POL":
		code = "PropertyOwnersLiabilities";
		cover_code = "Liabilities-POL";
		break;
	case "Terrorism":
		code = "Terrorism";
		cover_code = "Terrorism";
		break;
	case "Bespoke Cover":
		code = "BespokeCover";
		cover_code = "BespokeCover";
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
			
			
			double t_InsuranceTax = (t_grossP * Double.parseDouble(internal_data_map.get("Flat-Premiums").get(count-1).get("FP_TaxRate")))/100.0;
			fp_details_values.put("Insurance Tax Rate", Double.parseDouble(internal_data_map.get("Flat-Premiums").get(count-1).get("FP_TaxRate")));
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

public int func_FP_Entries_Transaction_Details_Verification_MTA(String sectionName,Map<String, List<Map<String, String>>> internal_data_map){

	Map<Object,Object> map_data = common.MTA_excel_data_map;
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
	case "Property Owners Liabilities":
	case "Liabilities - POL":
		code = "PropertyOwnersLiabilities";
		cover_code = "Liabilities-POL";
		sectionName = "Liabilities - POL";
		break;
	case "Terrorism":
		code = "Terrorism";
		cover_code = "Terrorism";
		break;
	case "Bespoke Cover":
		code = "BespokeCover";
		cover_code = "BespokeCover";
		break;
	
	default:
			System.out.println("**Cover Name is not in Scope for POF**");
		break;
	
	}
	
try{
		
			TestUtil.reportStatus("---------------"+sectionName+" in Flat Premium Section-----------------","Info",false);
		
			//final_fp_NNP = Double.parseDouble(internal_data_map.get("Flat-Premiums").get(count-1).get("FP_Premium"));
		
			//final_fp_NNP = common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Net Net Premium");
			final_fp_NNP = (Double)map_data.get(sectionName+"_FP");
		
			String t_NetNetP_expected = common.roundedOff(Double.toString(final_fp_NNP));
			String t_NetNetP_actual = Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Net Net Premium"));
			CommonFunction.compareValues(Double.parseDouble(t_NetNetP_expected),Double.parseDouble(t_NetNetP_actual)," Net Net Premium");
			//fp_details_values.put("Net Net Premium", Double.parseDouble(t_NetNetP_expected));
			
			double t_pen_comm = (( Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate"))/100)));
			String t_pc_expected = common.roundedOff(Double.toString(t_pen_comm));
			String t_pc_actual = Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Pen Comm"));
			CommonFunction.compareValues(Double.parseDouble(t_pc_expected),Double.parseDouble(t_pc_actual)," Pen Commission");
			//fp_details_values.put("Pen Comm", Double.parseDouble(t_pc_expected));
			
			
			double t_netP = Double.parseDouble(t_pc_expected) + Double.parseDouble(t_NetNetP_expected);
			String t_netP_expected = common.roundedOff(Double.toString(t_netP));
			String t_netP_actual = Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Net Premium"));
			CommonFunction.compareValues(Double.parseDouble(t_netP_expected),Double.parseDouble(t_netP_actual),"Net Premium");
			//fp_details_values.put("Net Premium", Double.parseDouble(t_netP_expected));
			
			
			double t_broker_comm = ((Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate"))/100)));
			String t_bc_expected = common.roundedOff(Double.toString(t_broker_comm));
			String t_bc_actual =  Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Broker Commission"));
			CommonFunction.compareValues(Double.parseDouble(t_bc_expected),Double.parseDouble(t_bc_actual),"Broker Commission");
			//fp_details_values.put("Broker Commission", Double.parseDouble(t_bc_expected));
			
			
			double t_grossP = Double.parseDouble(t_netP_expected) + Double.parseDouble(t_bc_expected);
			String t_grossP_actual = Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Gross Premium"));
			CommonFunction.compareValues(t_grossP,Double.parseDouble(t_grossP_actual)," Gross Premium");
			//fp_details_values.put("Gross Premium", t_grossP);
			
			
			double t_InsuranceTax = (t_grossP * common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Insurance Tax Rate"))/100.0;
			
			t_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(t_InsuranceTax)));
			String t_InsuranceTax_actual = Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Insurance Tax"));
			//fp_details_values.put("Insurance Tax", t_InsuranceTax);
			
			//SPI  Transaction Total Premium verification : 
			double t_Premium = t_grossP + t_InsuranceTax;
			String t_p_expected = common.roundedOff(Double.toString(t_Premium));
			//fp_details_values.put("Total Premium", Double.parseDouble(t_p_expected));
			
			String t_p_actual = Double.toString(common.transaction_Details_Premium_Values.get(sectionName+"_FP").get("Total Premium"));
			
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

public boolean verifyPolicyReferanceNumber(Map<Object, Object> map_data , String code , String event) {
	
	try{
		String navigationBy = CONFIG.getProperty("NavigationBy");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Policy General"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Premium Summary page navigations issue(S)");
		String Carrier = driver.findElement(By.xpath("//*[@id='pof_carrier_ref_num']")).getText();
		String MTAEffectiveDate = (String)map_data.get("MTA_EffectiveDate");
		String policyRefNumber = (String)map_data.get("PG_CarrierPolicyNumber");
		if(MTAEffectiveDate.contains("2018")){
			policyRefNumber = policyRefNumber.replace("17/RSL/", "18/RSL/");
			System.out.println(policyRefNumber);
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Policy General", (String)map_data.get("Automation Key"), "PG_CarrierPolicyNumber", policyRefNumber, map_data);
		}else if(MTAEffectiveDate.contains("2019")){
			policyRefNumber = policyRefNumber.replace("17/RSL/", "19/RSL/");
			System.out.println(policyRefNumber);
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Policy General", (String)map_data.get("Automation Key"), "PG_CarrierPolicyNumber", policyRefNumber, map_data);
		}
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Material Facts and Declarations . ");
		
		//String year = new SimpleDateFormat("yy", );
		
		return true;
		
	}catch(Throwable t){
		return false;
	}
	
}

}
