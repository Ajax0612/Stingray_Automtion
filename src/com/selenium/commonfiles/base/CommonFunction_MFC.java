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


public class CommonFunction_MFC extends TestBase{

	SimpleDateFormat df = new SimpleDateFormat();
	Properties MFC_Rater = new Properties();
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
	public double stale_count=0;
	public static String pas_NoOfEmp_AllOthers = null, pas_NoOfEmp_Clerical = null, pas_NoOfEmp_Drivers = null, LE_TotalWegroll = null;
	public static double Ter_BuildingContents_Sum = 0;
	public double totalMD = 0.0, Ter_BI_Sum = 0.00;
	public boolean isGrossPremiumReferralCheckDone = false;
	public double  CarsNetP,CvNetP,AvNetP,OtNetP,StNetP,TrNetP,TpNetP,PoaNetP;
	public double tempNP=0.00;
	
	
	
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
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common_HHAZ.funcCovers(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Claims Experience"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcClaimsExperience(common.NB_excel_data_map), "Previous claim function is having issue(S) .");
			
			//Non-linear cover selection
			customAssert.assertTrue(Cover_Selection_By_Sequence(common.NB_excel_data_map), "Cover selection by sequence function is having issue(S) .");
	
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
	
	//----------------------------------------------------------------//
		//--This function selects cover as per sequence provided in the data sheet--//
		//--------------------------------------------------------------//
		public boolean Cover_Selection_By_Sequence(Map<Object,Object> data_map){
			boolean r_value=false;
			String navigationBy = CONFIG.getProperty("NavigationBy");
			String[] covers = null;
			try{
				try{
					if(((String)data_map.get("CD_IsLinearCoverSelection")).equalsIgnoreCase("Yes")){
						Cover_selection_linear(data_map);
					}else{
						covers = ((String)data_map.get("CD_Cover_Selection_Sequence")).split(",");
					}
				}catch(Throwable t){
					Cover_selection_linear(data_map);
				}
				for(String cover : covers){	
					switch(cover){
					
					case "Cars":
						if(((String)data_map.get("CD_Cars")).equals("Yes")){	
							customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Cars"),"Issue while Navigating to Cars Cover page ");
							customAssert.assertTrue(Carspage(data_map), "Cars function is having issue(S) .");
						}
					break;
					case "CommercialVehicles":
						if(((String)data_map.get("CD_CommercialVehicles")).equals("Yes")){
							customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Commercial Vehicles"),"Issue while Navigating to Commercial Vehicles Cover page ");
							customAssert.assertTrue(funcCommercialVehicles(data_map), "Commercial Vehicles function is having issue(S) .");
						}
						break;
					//CD_AgriculturalVehicles
					case "AgriculturalVehicles":
						if(((String)data_map.get("CD_AgriculturalVehicles")).equals("Yes")){
							customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Agricultural Vehicles"),"Issue while Navigating to Agricultural Vehicles Cover page ");
							customAssert.assertTrue(funcAgriculturalVehicles(data_map), "Agricultural Vehicles function is having issue(S) .");
						}
						break;
					//SpecialTypepages
					case "SpecialTypes":
						if(((String)data_map.get("CD_SpecialTypes")).equals("Yes")){	
							customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Special Types"),"Issue while Navigating to Special Types Cover page ");
							customAssert.assertTrue(SpecialTypepages(data_map), "Special Types function is having issue(S) .");
						}
						break;
//					OtherTypepages
					case "OtherTypes":
						if(((String)data_map.get("CD_OtherTypes")).equals("Yes")){	
							customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Other Types"),"Issue while Navigating to Other Types Cover page ");
							customAssert.assertTrue(OtherTypepages(data_map), "Other Types function is having issue(S) .");
						}
						break;
					case "TradePlates":
						if(((String)data_map.get("CD_TradePlates")).equals("Yes")){	
							customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Trade Plate"),"Issue while Navigating to Trade Plate Cover page ");
							customAssert.assertTrue(TradePlatepage(data_map), "Trade Plates function is having issue(S) .");
						}
						break;
					case "Trailers":
						if(((String)data_map.get("CD_Trailers")).equals("Yes")){	
							customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Trailers"),"Issue while Navigating to Trailers Cover page ");
							customAssert.assertTrue(Trailerpage(data_map), "Trailers function is having issue(S) .");
						}
						break;
					case "PersonalAccidentOptional":
						if(((String)data_map.get("CD_PersonalAccidentOptional")).equals("Yes")){
							customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Personal Accident Optional"),"Issue while Navigating to Personal Accident Optional Cover page ");
							customAssert.assertTrue(PersonalAccidentOptionalPage(data_map), "Personal Accident Optional function is having issue(S) .");
						}
						break;
					case "Drivers":
						customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Drivers"),"Issue while Navigating to Commercial Vehicles Cover page ");
						customAssert.assertTrue(funcDrivers(data_map), "Drivers function is having issue(S) .");
						break;
						
					
					default:
						System.out.println("Cover selection status is 'NO' in the data sheet for cover - "+cover);
					}
				}
				
				r_value=true;
			}catch(Throwable t){
				System.out.println("Error while selecting cover by sequence --> "+t.getLocalizedMessage());
				return false;
			}
			return r_value;
		}
		
		public boolean Cover_selection_linear(Map<Object,Object> data_map){
			
			String navigationBy = CONFIG.getProperty("NavigationBy");
			
			try{
			
				if(((String)data_map.get("CD_Cars")).equals("Yes")){	
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Cars"),"Issue while Navigating to Cars Cover page ");
					customAssert.assertTrue(Carspage(data_map), "Cars function is having issue(S) .");
				}
				
				if(((String)data_map.get("CD_CommercialVehicles")).equals("Yes")){
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Commercial Vehicles"),"Issue while Navigating to Commercial Vehicles Cover page ");
					customAssert.assertTrue(funcCommercialVehicles(data_map), "Commercial Vehicles function is having issue(S) .");
				}
				
				//CD_AgriculturalVehicles
				if(((String)data_map.get("CD_AgriculturalVehicles")).equals("Yes")){
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Agricultural Vehicles"),"Issue while Navigating to Agricultural Vehicles Cover page ");
					customAssert.assertTrue(funcAgriculturalVehicles(data_map), "Agricultural Vehicles function is having issue(S) .");
				}
				//SpecialTypepages
				if(((String)data_map.get("CD_SpecialTypes")).equals("Yes")){	
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Special Types"),"Issue while Navigating to Special Types Cover page ");
					customAssert.assertTrue(SpecialTypepages(data_map), "Special Types function is having issue(S) .");
				}
				
//				OtherTypepages
				if(((String)data_map.get("CD_OtherTypes")).equals("Yes")){	
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Other Types"),"Issue while Navigating to Other Types Cover page ");
					customAssert.assertTrue(OtherTypepages(data_map), "Other Types function is having issue(S) .");
				}
				
				if(((String)data_map.get("CD_TradePlates")).equals("Yes")){	
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Trade Plate"),"Issue while Navigating to Trade Plate Cover page ");
					customAssert.assertTrue(TradePlatepage(data_map), "Trade Plates function is having issue(S) .");
				}
				if(((String)data_map.get("CD_Trailers")).equals("Yes")){	
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Trailers"),"Issue while Navigating to Trailers Cover page ");
					customAssert.assertTrue(Trailerpage(data_map), "Trailers function is having issue(S) .");
				}

				if(((String)data_map.get("CD_PersonalAccidentOptional")).equals("Yes")){
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Personal Accident Optional"),"Issue while Navigating to Personal Accident Optional Cover page ");
					customAssert.assertTrue(PersonalAccidentOptionalPage(data_map), "Personal Accident Optional function is having issue(S) .");
				}

				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Drivers"),"Issue while Navigating to Commercial Vehicles Cover page ");
				customAssert.assertTrue(funcDrivers(data_map), "Drivers function is having issue(S) .");
			
			}catch(Throwable t){
				System.out.println("Error while selecting covers in linear way - "+t.getMessage());
				return false;
			}
		
			
			return true;
			
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
					customAssert.assertTrue(k.Click("CCD_PG_GeoLimit"),"Error while Clicking Geographic Limit List object . ");
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
					customAssert.assertTrue(k.Click("MFB_PG_InsuredDetails_Q1"),"Error while Clicking Professional Bodies List object . ");
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
				String allTradeValues = (String)map_data.get("PG_TCS_TradeCode");
				String sUniqueCol ="Trade Code";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
			
				WebElement s_table= driver.findElement(By.xpath(sTablePath));
				int totalCols = s_table.findElements(By.tagName("th")).size(); 
				int totalRows = s_table.findElements(By.tagName("tr")).size();
				String val_Trade = "";
				
				for(int i = 0; i<totalRows-1; i++){
					
					if(totalRows > 2){
						val_Trade = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[3]");							
					}else{
						val_Trade = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr/td[3]");
					}
					
					if(allTradeValues.contains(val_Trade)){
						TestUtil.reportStatus(val_Trade + "Tradecode is added and displayed on Policy general screen", "Info", true);
					}
				}
				
				
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
			customAssert.assertTrue(k.Click("COB_Btn_SelectTradeCode"), "Unable to click on Select Trade Code button in Policy Details .");
			customAssert.assertTrue(common.funcPageNavigation("Multi Trade Code Selection", ""), "Navigation problem to TMulti Trade Code Selection page .");
			
			String sVal = tradeCodeValue;
			String[] TradeCodes = tradeCodeValue.split(",");
			
			String a_selectedTradeCode = null;
			String a_selectedTradeCode_desc = null;
			
			for(String s_TradeCode : TradeCodes){
	 			driver.findElement(By.name("multiTrade_"+s_TradeCode)).click();
	 		}
			//Referral code - Trade Referrals
			/*if(is_Trade_referral_activity(tradeCodeName)){
				tradeCodeName = tradeCodeName.replaceAll(" ", "");
				
				common_HHAZ.referrals_list.add((String)map_data.get(referralKey+tradeCodeName));
			}*/
			
			common.funcButtonSelection("Save");
			common.funcButtonSelection("exit (Save First)");
	 		
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
			 
		}catch(StaleElementReferenceException stale_exception){
			stale_count++;
			System.out.println("Caught Stale Element Reference exception on MFD screen . ");
			if(stale_count < 4)
				MaterialFactsDeclerationPage();
			else
				retValue = false;
		}catch(Throwable t){
			k.ImplicitWaitOn();
			String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     
	        return false;
			}
		finally{
			 k.ImplicitWaitOn();
		 }
		 return retValue;
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
			common_CCD.MD_Building_Occupancies_list.clear();
			common_HHAZ.CoversDetails_data_list.clear();
			common_HHAZ.PremiumFlag = false;
		}
			
		common.currentRunningFlow="Rewind";
		String navigationBy = CONFIG.getProperty("NavigationBy");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common.funcRewind());
		
		TestUtil.reportStatus("<b> -----------------------Rewind flow started---------------------- </b>", "Info", false);
		
		//Existing Policy search condition for Rewind on MTA or Rewind on NB.
		if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
			customAssert.assertTrue(common.funcSearchPolicy(common.MTA_excel_data_map), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status (Endorsement Submitted (Rewind)) function is having issue(S) . ");
							
		}else{
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
			customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status (Endorsement Submitted (Rewind)) function is having issue(S) . ");
			}else{
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
			}
		}
		customAssert.assertTrue(funcPolicyGeneral(common.Rewind_excel_data_map,code,event), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(common_HHAZ.funcCovers(common.Rewind_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Claims Experience"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcClaimsExperience(common.Rewind_excel_data_map), "Previous claim function is having issue(S) .");
		
		if(((String)common.Rewind_excel_data_map.get("CD_Cars")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Cars"),"Issue while Navigating to Cars Cover page ");
			customAssert.assertTrue(Carspage(common.Rewind_excel_data_map), "Cars function is having issue(S) .");
		}
		
		if(((String)common.Rewind_excel_data_map.get("CD_CommercialVehicles")).equals("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Commercial Vehicles"),"Issue while Navigating to Commercial Vehicles Cover page ");
			customAssert.assertTrue(funcCommercialVehicles(common.Rewind_excel_data_map), "Commercial Vehicles function is having issue(S) .");
		}
		
		//CD_AgriculturalVehicles
		if(((String)common.Rewind_excel_data_map.get("CD_AgriculturalVehicles")).equals("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Agricultural Vehicles"),"Issue while Navigating to Agricultural Vehicles Cover page ");
			customAssert.assertTrue(funcAgriculturalVehicles(common.Rewind_excel_data_map), "Agricultural Vehicles function is having issue(S) .");
		}
		//SpecialTypepages
		if(((String)common.Rewind_excel_data_map.get("CD_SpecialTypes")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Special Types"),"Issue while Navigating to Special Types Cover page ");
			customAssert.assertTrue(SpecialTypepages(common.Rewind_excel_data_map), "Special Types function is having issue(S) .");
		}
		
		//OtherTypepages
		if(((String)common.Rewind_excel_data_map.get("CD_OtherTypes")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Other Types"),"Issue while Navigating to Other Types Cover page ");
			customAssert.assertTrue(OtherTypepages(common.Rewind_excel_data_map), "Other Types function is having issue(S) .");
		}
		
		
		if(((String)common.Rewind_excel_data_map.get("CD_TradePlates")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Trade Plate"),"Issue while Navigating to Trade Plate Cover page ");
			customAssert.assertTrue(TradePlatepage(common.Rewind_excel_data_map), "Trade Plates function is having issue(S) .");
		}
		if(((String)common.Rewind_excel_data_map.get("CD_Trailers")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Trailers"),"Issue while Navigating to Trailers Cover page ");
			customAssert.assertTrue(Trailerpage(common.Rewind_excel_data_map), "Trailers function is having issue(S) .");
		}

		if(((String)common.Rewind_excel_data_map.get("CD_PersonalAccidentOptional")).equals("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Personal Accident Optional"),"Issue while Navigating to Personal Accident Optional Cover page ");
			customAssert.assertTrue(PersonalAccidentOptionalPage(common.Rewind_excel_data_map), "Personal Accident Optional function is having issue(S) .");
		}

		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Drivers"),"Issue while Navigating to Commercial Vehicles Cover page ");
		customAssert.assertTrue(funcDrivers(common.Rewind_excel_data_map), "Drivers function is having issue(S) .");
		
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

	public void RenewalFlow(String code,String fileName) throws ErrorInTestMethod{
		String testName = (String)common.Renewal_excel_data_map.get("Automation Key");
		try{
			
			common.currentRunningFlow="Renewal";
			String navigationBy = CONFIG.getProperty("NavigationBy");
		
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
			
			customAssert.assertTrue(funcPolicyGeneral(common.Renewal_excel_data_map,code,"Renewal"), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common_HHAZ.funcCovers(common.Renewal_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Claims Experience"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcClaimsExperience(common.Renewal_excel_data_map), "Previous claim function is having issue(S) .");
			
			if(((String)common.Renewal_excel_data_map.get("CD_Cars")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Cars"),"Issue while Navigating to Cars Cover page ");
				customAssert.assertTrue(Carspage(common.Renewal_excel_data_map), "Cars function is having issue(S) .");
			}
			
			if(((String)common.Renewal_excel_data_map.get("CD_CommercialVehicles")).equals("Yes")){
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Commercial Vehicles"),"Issue while Navigating to Commercial Vehicles Cover page ");
				customAssert.assertTrue(funcCommercialVehicles(common.Renewal_excel_data_map), "Commercial Vehicles function is having issue(S) .");
			}
			
			//CD_AgriculturalVehicles
			if(((String)common.Renewal_excel_data_map.get("CD_AgriculturalVehicles")).equals("Yes")){
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Agricultural Vehicles"),"Issue while Navigating to Agricultural Vehicles Cover page ");
				customAssert.assertTrue(funcAgriculturalVehicles(common.Renewal_excel_data_map), "Agricultural Vehicles function is having issue(S) .");
			}
			//SpecialTypepages
			if(((String)common.Renewal_excel_data_map.get("CD_SpecialTypes")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Special Types"),"Issue while Navigating to Special Types Cover page ");
				customAssert.assertTrue(SpecialTypepages(common.Renewal_excel_data_map), "Special Types function is having issue(S) .");
			}
			
//			OtherTypepages
			if(((String)common.Renewal_excel_data_map.get("CD_OtherTypes")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Other Types"),"Issue while Navigating to Other Types Cover page ");
				customAssert.assertTrue(OtherTypepages(common.Renewal_excel_data_map), "Other Types function is having issue(S) .");
			}
			
			if(((String)common.Renewal_excel_data_map.get("CD_TradePlates")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Trade Plate"),"Issue while Navigating to Trade Plate Cover page ");
				customAssert.assertTrue(TradePlatepage(common.Renewal_excel_data_map), "Trade Plates function is having issue(S) .");
			}
			if(((String)common.Renewal_excel_data_map.get("CD_Trailers")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Trailers"),"Issue while Navigating to Trailers Cover page ");
				customAssert.assertTrue(Trailerpage(common.Renewal_excel_data_map), "Trailers function is having issue(S) .");
			}

			if(((String)common.Renewal_excel_data_map.get("CD_PersonalAccidentOptional")).equals("Yes")){
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Personal Accident Optional"),"Issue while Navigating to Personal Accident Optional Cover page ");
				customAssert.assertTrue(PersonalAccidentOptionalPage(common.Renewal_excel_data_map), "Personal Accident Optional function is having issue(S) .");
			}

			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Drivers"),"Issue while Navigating to Commercial Vehicles Cover page ");
			customAssert.assertTrue(funcDrivers(common.Renewal_excel_data_map), "Drivers function is having issue(S) .");
			
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
			common_HHAZ.CoversDetails_data_list.clear();
			String navigationBy = CONFIG.getProperty("NavigationBy");
			common_HHAZ.PremiumFlag = false;
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcRewind());
			
			TestUtil.reportStatus("<b> -----------------------Renewal Rewind flow started---------------------- </b>", "Info", false);
			
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
			customAssert.assertTrue(common.funcSearchPolicy(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Renewal Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
			
			customAssert.assertTrue(funcPolicyGeneral(common.Rewind_excel_data_map,code,event), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common_HHAZ.funcCovers(common.Rewind_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
			//customAssert.assertTrue(func_Referrals_MaterialFactsDeclerationPage(), "Referrals for MaterialFactsDeclerationPage function is having issue(S) . ");
			
//			customAssert.assertTrue(func_Referrals_MaterialFactsDeclerationPage(), "Referrals for MaterialFactsDeclerationPage function is having issue(S) . ");
//			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Claims Experience"),"Issue while Navigating to Material Facts and Declarations . ");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Claims Experience"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcClaimsExperience(common.Rewind_excel_data_map), "Previous claim function is having issue(S) .");
			
			if(((String)common.Rewind_excel_data_map.get("CD_Cars")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Cars"),"Issue while Navigating to Cars Cover page ");
				customAssert.assertTrue(Carspage(common.Rewind_excel_data_map), "Cars function is having issue(S) .");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_CommercialVehicles")).equals("Yes")){
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Commercial Vehicles"),"Issue while Navigating to Commercial Vehicles Cover page ");
				customAssert.assertTrue(funcCommercialVehicles(common.Rewind_excel_data_map), "Commercial Vehicles function is having issue(S) .");
			}
			
			//CD_AgriculturalVehicles
			if(((String)common.Rewind_excel_data_map.get("CD_AgriculturalVehicles")).equals("Yes")){
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Agricultural Vehicles"),"Issue while Navigating to Agricultural Vehicles Cover page ");
				customAssert.assertTrue(funcAgriculturalVehicles(common.Rewind_excel_data_map), "Agricultural Vehicles function is having issue(S) .");
			}
			//SpecialTypepages
			if(((String)common.Rewind_excel_data_map.get("CD_SpecialTypes")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Special Types"),"Issue while Navigating to Special Types Cover page ");
				customAssert.assertTrue(SpecialTypepages(common.Rewind_excel_data_map), "Special Types function is having issue(S) .");
			}
			
//			OtherTypepages
			if(((String)common.Rewind_excel_data_map.get("CD_OtherTypes")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Other Types"),"Issue while Navigating to Other Types Cover page ");
				customAssert.assertTrue(OtherTypepages(common.Rewind_excel_data_map), "Other Types function is having issue(S) .");
			}
			
			if(((String)common.Rewind_excel_data_map.get("CD_TradePlates")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Trade Plate"),"Issue while Navigating to Trade Plate Cover page ");
				customAssert.assertTrue(TradePlatepage(common.Rewind_excel_data_map), "Trade Plates function is having issue(S) .");
			}
			if(((String)common.Rewind_excel_data_map.get("CD_Trailers")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Trailers"),"Issue while Navigating to Trailers Cover page ");
				customAssert.assertTrue(Trailerpage(common.Rewind_excel_data_map), "Trailers function is having issue(S) .");
			}

			if(((String)common.Rewind_excel_data_map.get("CD_PersonalAccidentOptional")).equals("Yes")){
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Personal Accident Optional"),"Issue while Navigating to Personal Accident Optional Cover page ");
				customAssert.assertTrue(PersonalAccidentOptionalPage(common.Rewind_excel_data_map), "Personal Accident Optional function is having issue(S) .");
			}

			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Drivers"),"Issue while Navigating to Commercial Vehicles Cover page ");
			customAssert.assertTrue(funcDrivers(common.Rewind_excel_data_map), "Drivers function is having issue(S) .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_HHAZ.funcPremiumSummary(common.Rewind_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			
			
		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
		
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
			common_HHAZ.CoversDetails_data_list.clear();
			common_HHAZ.PremiumFlag = false;
		}
		common.currentRunningFlow="MTA";
		
		String navigationBy = CONFIG.getProperty("NavigationBy");
		
		customAssert.assertTrue(common_CCD.funcCreateEndorsement(),"Error in Create Endorsement function . ");
		
		customAssert.assertTrue(funcPolicyGeneral(common.MTA_excel_data_map,code,"MTA"), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(common_HHAZ.funcCovers(common.MTA_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Claims Experience"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcClaimsExperience(common.MTA_excel_data_map), "Previous claim function is having issue(S) .");
		
		if(((String)common.MTA_excel_data_map.get("CD_Cars")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Cars"),"Issue while Navigating to Cars Cover page ");
			customAssert.assertTrue(Carspage(common.MTA_excel_data_map), "Cars function is having issue(S) .");
		}
		
		if(((String)common.MTA_excel_data_map.get("CD_CommercialVehicles")).equals("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Commercial Vehicles"),"Issue while Navigating to Commercial Vehicles Cover page ");
			customAssert.assertTrue(funcCommercialVehicles(common.MTA_excel_data_map), "Commercial Vehicles function is having issue(S) .");
		}
		
		//CD_AgriculturalVehicles
		if(((String)common.MTA_excel_data_map.get("CD_AgriculturalVehicles")).equals("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Agricultural Vehicles"),"Issue while Navigating to Agricultural Vehicles Cover page ");
			customAssert.assertTrue(funcAgriculturalVehicles(common.MTA_excel_data_map), "Agricultural Vehicles function is having issue(S) .");
		}
		//SpecialTypepages
		if(((String)common.MTA_excel_data_map.get("CD_SpecialTypes")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Special Types"),"Issue while Navigating to Special Types Cover page ");
			customAssert.assertTrue(SpecialTypepages(common.MTA_excel_data_map), "Special Types function is having issue(S) .");
		}
		
//		OtherTypepages
		if(((String)common.MTA_excel_data_map.get("CD_OtherTypes")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Other Types"),"Issue while Navigating to Other Types Cover page ");
			customAssert.assertTrue(OtherTypepages(common.MTA_excel_data_map), "Other Types function is having issue(S) .");
		}
		
		if(((String)common.MTA_excel_data_map.get("CD_TradePlates")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Trade Plate"),"Issue while Navigating to Trade Plate Cover page ");
			customAssert.assertTrue(TradePlatepage(common.MTA_excel_data_map), "Trade Plates function is having issue(S) .");
		}
		if(((String)common.MTA_excel_data_map.get("CD_Trailers")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Trailers"),"Issue while Navigating to Trailers Cover page ");
			customAssert.assertTrue(Trailerpage(common.MTA_excel_data_map), "Trailers function is having issue(S) .");
		}

		if(((String)common.MTA_excel_data_map.get("CD_PersonalAccidentOptional")).equals("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Personal Accident Optional"),"Issue while Navigating to Personal Accident Optional Cover page ");
			customAssert.assertTrue(PersonalAccidentOptionalPage(common.MTA_excel_data_map), "Personal Accident Optional function is having issue(S) .");
		}

		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Drivers"),"Issue while Navigating to Commercial Vehicles Cover page ");
		customAssert.assertTrue(funcDrivers(common.MTA_excel_data_map), "Drivers function is having issue(S) .");
		
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
	
	MFC_Rater = OR.getORProperties();
	int f=0;
	String bi_act_list = MFC_Rater.getProperty("CCD_BI_Referral_Activities");
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
	
	MFC_Rater = OR.getORProperties();
	int f=0;
	String el_act_list = MFC_Rater.getProperty("CCD_EL_Referral_Activities");
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
	
	MFC_Rater = OR.getORProperties();
	int f=0;
	String pl_act_list = MFC_Rater.getProperty("CCD_PL_Referral_Activities");
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
	
	MFC_Rater = OR.getORProperties();
	int f=0;
	String pl_act_list = MFC_Rater.getProperty("CCD_Trade_Referral_Activities");
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
	
			
			MFC_Rater = OR.getORProperties();
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
		MFC_Rater = OR.getORProperties();
		String _section = section.replaceAll(" ", "");
		if(section.equals("Policy"))
			_section = "Policy_Minimum_Premium_MP";
		else	
			_section = _section +"_MP";
		
		r_value = Double.parseDouble(MFC_Rater.getProperty(_section));
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
		
		String CarsPremium = k.getAttributeByXpath("//*[contains(@name,'_cars_sec_tot')]", "value");
		//System.out.println(CarsPremium);
		CommonFunction.compareValues(CarsNetP, Double.parseDouble(CarsPremium), "Cars Net Premium Value");
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
		String[] Sp = ((String)map_data.get("MFB_CARS_Specified_Items")).split(";");
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
		
		if(Desc.contains("Flat")){
			row--;
			Tbl = CRBRTbl +"/tbody/tr["+(row)+"]/td";
			TestUtil.reportStatus("<p style='color:red'>Flat Premium to be removed from all the cover screens - Defect is opened <b> [ SUP-1544 ] </b> </p>.", "Fail", true);
	
		}
		
		Desc = k.getTextByXpath(Tbl+"["+getTableHeaderIndex(tblH,"Description")+"]" );
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
		 CommonFunction.compareValues(expBP, actBP, "Book Premium Value - "+Desc+" - Item :"+(rowNmbr+1));
		
		 ExpPremium = expBP + (expBP*((Double.parseDouble(TechAdjust))/100))+((expBP + (expBP*((Double.parseDouble(TechAdjust))/100)))*(Double.parseDouble(CommAdjust)/100));
		 ActPremium = Double.parseDouble(k.getAttributeByXpath(PremiumXP, "value"));
		 CommonFunction.compareValues(ExpPremium, ActPremium, "Premiums values - "+Desc+" - Item :"+(rowNmbr+1));
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
			
			String TPPremium = k.getAttributeByXpath("//*[contains(@name,'_tp_tot')]", "value");
			//System.out.println("Trade Plate Premium :"+TPPremium);
			map_data.put("PS_TradePlate_NetNetPremium", TPPremium);
			CommonFunction.compareValues(TpNetP, Double.parseDouble(TPPremium), "Trade Plate Net Premium Value");
		
		
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
		
		String TrailersPremium = k.getAttributeByXpath("//*[contains(@name,'_tr_tot')]", "value");
		//System.out.println(TrailersPremium);
		map_data.put("PS_Trailers_NetNetPremium", TrailersPremium);
		CommonFunction.compareValues(TrNetP, Double.parseDouble(TrailersPremium), "Trailer Net Premium Value");
		
		
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
			customAssert.assertTrue(common.funcPageNavigation("Add Specified Trailer",""), "Trailer - Specified Detail Page Navigation issue . ");
			
			
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
		
		String STPremium = k.getAttributeByXpath("//*[contains(@name,'_st_tot')]", "value");
		//System.out.println(STPremium);
		map_data.put("PS_SpecialTypes_NetNetPremium", STPremium);
		CommonFunction.compareValues(StNetP, Double.parseDouble(STPremium), "Special Type Net Premium Value");
		
		
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
			
			customAssert.assertTrue(k.DropDownType("MFc_STUN_DriversExcess", (String)internal_data_map.get("AddUnspecifiedST").get(no).get("MFB_STUN_HighRisk")),	"Unable to enter value in MFC_STUN_DriversExcess field .");
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
		
		String OTPremium = k.getAttributeByXpath("//*[contains(@name,'_ov_tot')]", "value");
		//System.out.println(OTPremium);
		map_data.put("PS_OtherTypes_NetNetPremium", OTPremium);
		CommonFunction.compareValues(OtNetP, Double.parseDouble(OTPremium), "Other Types Net Premium Value");
		
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
		
		String CVPremium = k.getAttributeByXpath("//*[contains(@name,'_cv_tot')]", "value");
		//System.out.println(CVPremium);
		map_data.put("PS_CommercialVehicles_NetNetPremium", CVPremium);
		CommonFunction.compareValues(CvNetP, Double.parseDouble(CVPremium), "Commercial Vehicles Net Premium Value");
		
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
			
					
			String ACVPremium = k.getAttributeByXpath("//*[contains(@name,'_av_tot')]", "value");
			//System.out.println(ACVPremium);
			map_data.put("PS_AgriculturalVehicles_NetNetPremium", ACVPremium);
			CommonFunction.compareValues(AvNetP, Double.parseDouble(ACVPremium), "Agriculture Vehicles Net Premium Value");
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
					
					customAssert.assertTrue(funcValidate_CoverRatedTable(CarBRIndex, no, "AddUnspecifiedAgrVehicles","MFB_CUV" ),"Rater Table validation failed for Specified item - "+(no +1));				
					
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
					
			//	customAssert.assertTrue(k.Click("MFB_CARS_ApplyBookRates"), "Unable to click on Apply Bookrate Button on Personal Acidental Option page .");
				int TblIndx = k.getTableIndex("Book Premium", "xpath","html/body/div[3]/form/div/table");
				
				tempNP = 0.00;
				double rtf ;
				double expBP;
				double actBP;
				double ExpPremium, ActPremium;
								
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
				String PV = k.getTextByXpath(Tbl+"["+getTableHeaderIndex(tblH,"Description")+"]" );
				String CV = k.getTextByXpath(Tbl1+"["+getTableHeaderIndex(tblH,"Description")+"]" );
				String Tech_AdjustXP = (Tbl+"["+getTableHeaderIndex(tblH,"Tech. Adjust")+"]/input");
				String Tech_AdjustXP1 = (Tbl1+"["+getTableHeaderIndex(tblH,"Tech. Adjust")+"]/input");
				
				String PremiumXP =(Tbl+"["+getTableHeaderIndex(tblH,"Premium")+"]/input");
				String PremiumXP1 =(Tbl1+"["+getTableHeaderIndex(tblH,"Premium")+"]/input");
				
				
				/*String stir = ((k.getAttribute("MFB_PAO_BenefitAmount", "value").replace(cur,"")).trim());
				int ir = Integer.parseInt(stir);
				
				String InitialRate_PV_Driver = PAO_initial_Rater.get("PV_Driver").get(ir).toString();
				String InitialRate_TankerCV_Driver = PAO_initial_Rater.get("TankerCV_Driver").get(ir).toString();*/
				
				//MFB PAO getting rater intial rates 
				String expInitialRate_PV_Driver = Double.toString(get_Initial_Rate(PV, (String)map_data.get("MFB_PAO_DriverExtension"), cur+(String)map_data.get("MFB_PAO_BenefitAmount")));
				String expInitialRate_TankerCV_Driver = Double.toString(get_Initial_Rate(CV, (String)map_data.get("MFB_PAO_DriverExtension"), cur+(String)map_data.get("MFB_PAO_BenefitAmount")));
				
				TestUtil.reportStatus("PAO rater calculations sucessfully completed. ", "Info", false);
				
				String TechAdjust_PV = (String)map_data.get("MFB_POA_PV_TechAdjust");
				String TechAdjust_CV = (String)map_data.get("MFB_POA_CV_TechAdjust");
				
			
				customAssert.assertTrue(k.InputByXpath(Tech_AdjustXP,TechAdjust_PV),"Issue while entering data in Tech Adjust% PV rate");
				customAssert.assertTrue(k.InputByXpath(Tech_AdjustXP1,TechAdjust_CV),"Issue while entering data in Tech Adjust% CV rate");
				
				
				//Click on appy book rate button
				customAssert.assertTrue(k.Click("MFB_POA_AppyBookRates"), "Unable to click on Apply Bookrate Button on Personal Accidental Optional page for ");
				
				String actPVIntitalRate = k.getAttributeByXpath(Tbl+"["+getTableHeaderIndex(tblH,"Initial Rate")+"]/input","value");
				String actCVIntitalRate = k.getAttributeByXpath(Tbl1+"["+getTableHeaderIndex(tblH,"Initial Rate")+"]/input","value" );
				 
				CommonFunction.compareValues(Double.parseDouble(expInitialRate_PV_Driver), Double.parseDouble(actPVIntitalRate), "Initial Rate Value for - "+PV+" - PV Item ");
				CommonFunction.compareValues(Double.parseDouble(expInitialRate_TankerCV_Driver), Double.parseDouble(actCVIntitalRate), "Initial Rate Value for - "+CV+" - CV Item ");
				
				
				rtf = Double.parseDouble(k.getTextByXpath(Tbl+"["+getTableHeaderIndex(tblH,"Rating")+"]"));
				double rtf1 = Double.parseDouble(k.getTextByXpath(Tbl1+"["+getTableHeaderIndex(tblH,"Rating")+"]"));
				 
				 expBP = rtf* Double.parseDouble(expInitialRate_PV_Driver); 
				 double expBP1 = rtf1* Double.parseDouble(expInitialRate_TankerCV_Driver);
				 
				 actBP = Double.parseDouble(k.getAttributeByXpath(Tbl+"["+getTableHeaderIndex(tblH,"Book Premium")+"]/input","value"));
				 double actBP1 = Double.parseDouble(k.getAttributeByXpath(Tbl1+"["+getTableHeaderIndex(tblH,"Book Premium")+"]/input","value"));
				 
				 //double actBP_CV = Double.parseDouble(k.getText(Tbl1+"["+getTableHeaderIndex(tblH,"Book Premium")+"]/input"));
				 
				 CommonFunction.compareValues(expBP, actBP, "Book Premium Value - "+PV+" - PV Item :");
				// CommonFunction.compareValues(expBP, actBP, "Book Premium Value - "+PV+" - PV Item :");
				 CommonFunction.compareValues(expBP1, actBP1, "Book Premium Value - "+CV+" - CV Item :");
				 
				 ExpPremium = expBP + (expBP*((Double.parseDouble(TechAdjust_PV))/100));
				 double ExpPremium_CV = expBP1 + (expBP1*((Double.parseDouble(TechAdjust_CV))/100));
				 
				 ActPremium = Double.parseDouble(k.getAttributeByXpath(PremiumXP, "value"));
				 double ActPremium_CV = Double.parseDouble(k.getAttributeByXpath(PremiumXP1, "value"));
				 CommonFunction.compareValues(ExpPremium, ActPremium, "Premiums values - "+PV+" - PV Item :");
				 CommonFunction.compareValues(ExpPremium_CV, ActPremium_CV, "Premiums values - "+CV+" - CV Item :");
				 
				 PoaNetP =  ExpPremium+ExpPremium_CV;
			
				 String PAOPremium = k.getAttributeByXpath("//*[@id='mfc_pao_tot']", "value");
				 map_data.put("PS_PersonalAccidentOptional_NetNetPremium", PAOPremium);
				 CommonFunction.compareValues(PoaNetP, Double.parseDouble(PAOPremium), "Personal Accidental Optional Net Net Premium Value");
			}
			catch(Throwable t){
				   	String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
			        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
			        Assert.fail("Error while adding data to Personal Accident Optional page . ", t);
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
		
		
		///----------------------------------------------------///	
		///------MFC Autorated Rater logic for PAO cover -----////
		///----------------------------------------------------///		
		public double get_Initial_Rate(String pao_activity,String driverExtension,String benefitAmount){
			
			double initial_rate=0.0;
			try{
				initial_rate = get_Rate_from_Properties(pao_activity,driverExtension,benefitAmount);
				//System.out.println(pao_activity+" --> initial Rate = "+initial_rate);
				common_MFC.Book_rate_Rater_output.put("PAO"+"_"+pao_activity, initial_rate);
				return initial_rate;
				
			}catch(Throwable t){
				System.out.println("Error while calculating intitla rate for PAO cover --> "+t.getLocalizedMessage());
				return initial_rate;
			}
			
		}

		public double get_Rate_from_Properties(String pao_activity,String dExtension,String benefitAmount){
			
			double r_value=0.0;
			DecimalFormat formatter = new DecimalFormat("###.###");
			String pao_activity_f="";
			try{
				MFC_Rater = OR.getORProperties();
				pao_activity_f = "BA_"+benefitAmount + "_"+dExtension+"_"+pao_activity.replaceAll(" ","");
				r_value = Double.parseDouble(MFC_Rater.getProperty(pao_activity_f));
				r_value = Double.valueOf(formatter.format(r_value));
			
			}catch(Throwable t ){
				System.out.println("Error while getting Book rate for PAO activity > "+pao_activity+" < "+t.getMessage());
			}
			return r_value;
				
		}

		
// END OF FUNCTION

}
