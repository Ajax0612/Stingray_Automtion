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

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import org.testng.Assert;

import com.selenium.commonfiles.util.ErrorInTestMethod;
import com.selenium.commonfiles.util.ObjectMap;
import com.selenium.commonfiles.util.TestUtil;

public class CommonFunction_POF extends TestBase{

	SimpleDateFormat df = new SimpleDateFormat();
	Properties PLEL_Rater = new Properties();
	public int actual_no_of_years=0,err_count=0,trans_error_val=0 , Can_returnP_Error=0;
	
	public Map<String,Double> Book_rate_Rater_output = new HashMap<>();
	public Map<String,Double> TC_Calculations = new HashMap<>();
	public boolean isIncludingHeatPresent = false,isExcludingHeatPresent = false,isIncludeHeat_10M=false,isFPEntries=false,isMTARewindStarted=false, isNBRewindStarted = false, isNBRquoteStarted = false;
	public double cent_work_including_heat = 0.0; 
	Date currentDate = new Date();
	public static String Environment = null;
	public boolean isMTARewindFlow = false, isRenewalRewindFlow=false;
	public double JCT_TotalPremium = 0.0, OP_TotalPremium = 0.00, Ter_TotalPremium = 0.00; 
	DecimalFormat formatter = new DecimalFormat("#,###,###.##");
	public Map<String,Map<String,Double>> CAN_COB_ReturnP_Values_Map = new HashMap<>();
	
	public void NewBusinessFlow(String code,String event) throws ErrorInTestMethod{
		String testName = (String)common.NB_excel_data_map.get("Automation Key");
		String navigationBy = CONFIG.getProperty("NavigationBy");
		try{
			
			customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
			customAssert.assertTrue(common.checkClient(common.NB_excel_data_map,code,event),"Unable to check Client.");
			customAssert.assertTrue(common.createNewQuote(common.NB_excel_data_map,code,event), "Unable to create new quote.");
			customAssert.assertTrue(common.selectLatestQuote(common.NB_excel_data_map,code,event), "Unable to select quote from table.");
			customAssert.assertTrue(funcPolicyGeneral(common.NB_excel_data_map,code,event), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common_Zennor.funcCovers(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcPreviousClaims(common.NB_excel_data_map), "Previous claim function is having issue(S) .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Claims Experience"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcClaimsExperience(common.NB_excel_data_map), "Previous claim function is having issue(S) .");
			
			if(((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Damage"),"Issue while Navigating to Material Damage screen.");
				customAssert.assertTrue(MaterialDamagePage(common.NB_excel_data_map), "Material DamagePage function is having issue(S) . ");
			}
			if(((String)common.NB_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Business Interruption"),"Issue while Navigating to Business Interruption screen.");
				customAssert.assertTrue(BusinessInterruptionPage(common.NB_excel_data_map), "Business InterruptionPage function is having issue(S) . ");
			}
			if(((String)common.NB_excel_data_map.get("CD_Liabilities-POL")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Property Owners Liabilities"),"Issue while Navigating to Properties Owners Liabilities screen.");
				customAssert.assertTrue(PropertyOwnersLiabilityPage(common.NB_excel_data_map), "PropertyOwnersLiabilityPage function is having issue(S) . ");
			}
			//if(((String)common.NB_excel_data_map.get("CD_Liabilities-EL")).equals("Yes")){	
				//customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to Employers Liability screen.");
				//customAssert.assertTrue(Liabilities-ELPage(common.NB_excel_data_map), "Employers Liability function is having issue(S) . ");
			//}
			
			if(((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("Yes")){	
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Terrorism"),"Issue while Navigating to Terrorism screen.");
				customAssert.assertTrue(TerrorismPage(common.NB_excel_data_map), "TerrorismPage function is having issue(S) . ");
				
			}
			if(((String)common.NB_excel_data_map.get("CD_BespokeCover")).equals("Yes")){	
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Bespoke Cover"),"Issue while Navigating to Bespoke Cover screen . ");
				customAssert.assertTrue(funcBespokeCover(common.NB_excel_data_map), "Bespoke Cover function is having issue(S) . ");
			}
			if(((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Excess"),"Issue while Navigating to Excess screen.");
				customAssert.assertTrue(func_Verify_Excesses(common.NB_excel_data_map,common.NB_Structure_of_InnerPagesMaps), "Verify Excess function is having issue(S) . ");
			}
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
			customAssert.assertTrue(common.funcEndorsementOperations(common.NB_excel_data_map),"Insurance tax adjustment is having issue(S).");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_Zennor.funcPremiumSummary(common.NB_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			Assert.assertTrue(common_Zennor.funcStatusHandling(common.NB_excel_data_map,code,event));
			if(((String)common.NB_excel_data_map.get("NB_SearchByPolicyRefNum")).equals("Yes")){
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Policies"),"Issue while Navigating to Excess screen.");
				funcSearchPolicyByRefNum(common.NB_excel_data_map);
			}
			
			if(TestBase.businessEvent.equals("NB")){
					customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
					TestUtil.reportTestCasePassed(testName);
			}

		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
		
	}

	public boolean MaterialDamagePage(Map<Object, Object> map_data){
		boolean retValue = true;
		String abvr = null, coverName = null;
		
		
		try{
				customAssert.assertTrue(common.funcPageNavigation("Material Damage", ""),"Material Damage page is having issue(S)");
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
		
		}
		
		try{
			
			customAssert.assertTrue(common.funcPageNavigation("Material Damage", ""),"Property Details page navigation issue(S)");
			
			customAssert.assertTrue(k.DropDownSelection("POF_MD_Location", internal_data_map.get("Property Details").get(count).get("PoD_Locations")), "Unable to select Property location on Material damage screen.");
			String locationBName = internal_data_map.get("Property Details").get(count).get("PoD_Locations");
			
			if(locationBName.equalsIgnoreCase("Floating")){
				customAssert.assertTrue(k.Input("POF_MD_HVP_Limit", internal_data_map.get("Property Details").get(count).get("HVP_accomodation")), "Unable to enter High value premises accomodation value.");
				customAssert.assertTrue(k.DropDownSelection("POF_MD_HVP_SpecifyBusiness", internal_data_map.get("Property Details").get(count).get("HVP_specifyLargeBuildings")), "Unable to select value from High value premises buildings on the schedule.");
				String buildingSchedule = internal_data_map.get("Property Details").get(count).get("HVP_specifyLargeBuildings");
				if(buildingSchedule.equalsIgnoreCase("Yes")){
					customAssert.assertTrue(k.Click("POF_MD_AddHighValuePremises"),"Unable to click on Add items button for High values premises.");
					customAssert.assertTrue(k.Input("POF_MD_HVP_Address", internal_data_map.get("Property Details").get(count).get("HVP_address")), "Unable to enter description for High value pemises.");
					customAssert.assertTrue(k.Input("POF_MD_HVP_TotalSumInsured",internal_data_map.get("Property Details").get(count).get("HVP_SumInsured")), "Unable to enter description for High value pemises.");
					customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"), "Unable to click on Inner button.");
				}
				
			}
			else if(locationBName.equalsIgnoreCase("Specified")){
				customAssert.assertTrue(k.Input("POF_MD_PostCode", internal_data_map.get("Property Details").get(count).get("PoD_Postcode")), "Unable to enter postcode in material damage.");
			}
			
			//Add Buildings :
			if((internal_data_map.get("Property Details").get(count).get("PoD_AddBuildings")).equalsIgnoreCase("Yes")){
				customAssert.assertTrue(k.Click("POF_MD_AddBuildings"),"Unable to click on Add item button for Buildings.");
				
				WebElement innerPage = k.getObject("Inner_page_locator");
				WebElement propertyList = innerPage.findElement(By.xpath("//*[@name='pof_md_property_list']"));
				Select mySelect = new Select(propertyList);
				int index = getIndexDropdownBD(internal_data_map.get("Property Details").get(count).get("AddBuilding_Property"));
				mySelect.selectByIndex(index);
				
				customAssert.assertTrue(k.Input("POF_MD_BD_DeclaredValue", internal_data_map.get("Property Details").get(count).get("AddBuilding_DeclaredValue")), "Unable to enter Declraed value for buildings.");
				customAssert.assertTrue(k.Input("POF_PG_BD_DayOne", internal_data_map.get("Property Details").get(count).get("AddBuilding_DayOneUplift")), "Unable to enter day one uplift for buildings.");
				
				double DV = Double.parseDouble((internal_data_map.get("Property Details").get(count).get("AddBuilding_DeclaredValue")).replaceAll(",", ""));
				double DOP = Double.parseDouble((internal_data_map.get("Property Details").get(count).get("AddBuilding_DayOneUplift")).replaceAll(",", ""));
				double SI = DV + (( DV * DOP ) / 100.0);
				
				double expectedBDSumInsured = (int) Math.floor(SI);
				//double actualBDSumInsured = Double.parseDouble(k.getAttribute("POF_PG_BD_SumInsured", "value").replaceAll(",", ""));
				double expectedLimitValue = (int)Math.floor((expectedBDSumInsured * Double.parseDouble((internal_data_map.get("Property Details").get(count).get("AddBuilding_LimitRequired")).replaceAll(",", ""))) / 100.0);
				//This verification can not be done because application is not assigning value to the input tag. Hence commented.
				//customAssert.assertTrue(CommonFunction.compareValues(expectedBDSumInsured, actualSumInsured, "Buildings Sum Insured"));
				
				customAssert.assertTrue(k.Input("POF_PG_BD_NoOfUnits", internal_data_map.get("Property Details").get(count).get("AddBuilding_NumberOfUnits")), "Unable to enter Number of units for buildings.");
				customAssert.assertTrue(k.DropDownSelection("POF_PG_BD_Lor", internal_data_map.get("Property Details").get(count).get("AddBuilding_StandardLossofRentLimit")), "Unable to enter select value from Standard loss of rent limit.");
				customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"), "Unable to click on Inner button.");
				
				double actualTotalSumInsured = Double.parseDouble(k.getText("POF_MD_BD_SumInsuredTotal").replaceAll(",", ""));
				customAssert.assertTrue(CommonFunction.compareValues(expectedBDSumInsured, actualTotalSumInsured, "Buildings Total Sum Insured"));
				TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Property Details", internal_data_map.get("Property Details").get(count).get("Automation Key"), "AddBuilding_SumInsured", Double.toString(expectedBDSumInsured), internal_data_map.get("Property Details").get(count));
				double actualTotalLimitValue = Double.parseDouble(k.getText("POF_MD_BD_LimitValueTotal").replaceAll(",", ""));
				
				double diff = expectedLimitValue - actualTotalLimitValue;
				if(diff<=1.0 && diff>=-1.0){
					String tMsg="<p style='color:blue'>Values have been matched for Expected:<b>"+expectedLimitValue+" </b> with Actual value :<b> "+actualTotalLimitValue+" </p>";
					TestUtil.reportStatus(tMsg, "Pass", false);
				}else{
					String eMsg="Values have not been matched for Expected:<b>"+expectedLimitValue+"</b> with Actual value :<b>"+actualTotalLimitValue; 
					TestUtil.reportStatus(eMsg, "Fail", true);
				}
				
				//customAssert.assertTrue(CommonFunction.compareValues(expectedLimitValue, actualTotalLimitValue, "Buildings Total no of Units"));
				double expectedNoOfUnits = Double.parseDouble((internal_data_map.get("Property Details").get(count).get("AddBuilding_NumberOfUnits")).replaceAll(",", ""));
				double actualNoOfUnitsTotal = Double.parseDouble(k.getText("POF_MD_BD_NoOfUnitsTotal").replaceAll(",", ""));
				customAssert.assertTrue(CommonFunction.compareValues(expectedNoOfUnits, actualNoOfUnitsTotal, "Buildings Total Limit Value"));
			}
			
			//Add Contents :
			if((internal_data_map.get("Property Details").get(count).get("PoD_AddContents")).equalsIgnoreCase("Yes")){
				customAssert.assertTrue(k.Click("POF_MD_AddContents"),"Unable to click on Add item button for Contents.");
				
				WebElement innerPageContents = k.getObject("Inner_page_locator");
				WebElement propertyListContents = innerPageContents.findElement(By.xpath("//*[@name='pof_md_contents_list']"));
				Select mySelectContents = new Select(propertyListContents);
				int indexContents = getIndexDropdownCO(internal_data_map.get("Property Details").get(count).get("AddContents_Contents"));
				mySelectContents.selectByIndex(indexContents);
				
				//customAssert.assertTrue(k.DropDownSelection("POF_MD_CO_ContentsList", internal_data_map.get("Property Details").get(count).get("AddContents_Contents")),"Unable to select property for Contents.");
				customAssert.assertTrue(k.Input("POF_PG_BD_SumInsured", internal_data_map.get("Property Details").get(count).get("AddContents_SumInsured")), "Unable to enter Sum Insured for Contents.");
				customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"), "Unable to click on Inner button.");
				
				double expectedCOSumInsured = Double.parseDouble((internal_data_map.get("Property Details").get(count).get("AddContents_SumInsured")).replaceAll(",", ""));
				double actualCOSumInsured = Double.parseDouble(k.getText("POF_MD_CO_SumInsuredTotal").replaceAll(",", ""));
				customAssert.assertTrue(CommonFunction.compareValues(expectedCOSumInsured, actualCOSumInsured, "Contents Total Limit Value"));
			}
			
			customAssert.assertTrue(k.Click("POF_CalculateButton"), "Unable to click on Calculate Premium button.");
			
			customAssert.assertTrue(CoverSpecificCalculationMD("MD",map_data,count),"Calculation of Material Damage cover is having issues.");
            
			
		}catch(Throwable t){
			return false;
		}
		
		return r_value;
	}	

	
	public int getIndexDropdownBD(String optionValue) {
		
		HashMap<String, Integer> dropdownMap = new HashMap<>();
		
		dropdownMap.put("Loss Of Rent", 0);
		dropdownMap.put("All Buildings", 1);
		dropdownMap.put("Buildings Leased on Full Repairing Leases to Other Organisations", 2);
		dropdownMap.put("Care Home Buildings", 3);
		dropdownMap.put("CCTV Equipment", 4);
		dropdownMap.put("Commercial Buildings", 5);
		dropdownMap.put("Commercial Office Buildings", 6);
		dropdownMap.put("Commercial Shop Buildings", 7);
		dropdownMap.put("Communal Areas", 8);
		dropdownMap.put("Communal Areas Mixed Block", 9);
		dropdownMap.put("Communal Hall", 10);
		dropdownMap.put("Community Blocks", 11);
		dropdownMap.put("Cooling Plant", 12);
		dropdownMap.put("Extra Care Buildings", 13);
		dropdownMap.put("Extra Care Guest Room Buildings", 14);
		dropdownMap.put("Extra Care Leasehold Buildings", 15);
		dropdownMap.put("Extra Care Shared Owner Buildings", 16);
		dropdownMap.put("Factored Buildings", 17);
		dropdownMap.put("Factored Commercial Buildings", 18);
		dropdownMap.put("Fuel Tanks", 19);
		
		
		return dropdownMap.get(optionValue);
		
	}
	
	public int getIndexDropdownCO(String optionValue) {
		
		HashMap<String, Integer> dropdownMap = new HashMap<>();
		
		dropdownMap.put("Residents Household Goods", 1);
		dropdownMap.put("Care Home Contents", 2);
		dropdownMap.put("Landlords Contents", 3);
		dropdownMap.put("Show Home Contents", 4);
		dropdownMap.put("Contents / Contents of Common Parts", 5);
		dropdownMap.put("Cleaning and Caretaking Equipment", 6);
		dropdownMap.put("Other", 7);
		
		if(optionValue.contains("Residents Household Goods")){
			return dropdownMap.get("Residents Household Goods");
		}else if(optionValue.contains("Care Home Contents")){
			return dropdownMap.get("Care Home Contents");
		}else if(optionValue.contains("Landlords Contents")){
			return dropdownMap.get("Landlords Contents");
		}else if(optionValue.contains("Show Home Contents")){
			return dropdownMap.get("Show Home Contents");
		}else if(optionValue.contains("Contents / Contents of Common Parts")){
			return dropdownMap.get("Contents / Contents of Common Parts");
		}else if(optionValue.contains("Cleaning and Caretaking Equipment")){
			return dropdownMap.get("Cleaning and Caretaking Equipment");
		}else if(optionValue.contains("Other")){
			return dropdownMap.get("Other");
		}
		
		
		return dropdownMap.get(optionValue);
		
	}
	
	public boolean BusinessInterruptionPage(Map<Object, Object> map_data){
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
		
		}
		
		try{
			
            customAssert.assertTrue(common.funcPageNavigation("Business Interruption", ""), "Navigation Problem to Business Interruption page .");
            
            customAssert.assertTrue(k.DropDownSelection("POF_BI_BookDebtsRequired", (String)map_data.get("BI_BookDebtsReq")), "Unable to select value from dropdown - Is Book Debts Required");
            String BookDebts = (String)map_data.get("BI_BookDebtsReq");
            if(BookDebts.equalsIgnoreCase("Yes")){
            	customAssert.assertTrue(k.Input("POF_BI_BookDebtsSumInsured", (String)map_data.get("BI_DebtsSumInsured")), "Unable to enter value for - Book Debts Sum Insured");
            }
            
            String[] properties = ((String)map_data.get("BI_AddLOI")).split(";");
            int no_of_property = properties.length;
            List<WebElement> elm = null;
            
            if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
            	
            	boolean isNotStale=true;
        		
            	k.ImplicitWaitOff();
        		while(isNotStale){
        			try{
        				List<WebElement> delete_Btns = driver.findElements(By.xpath("//div[text()='Loss Of Rent']//following::table[1]//*[text()='Delete']"));
        				for(WebElement element: delete_Btns){
        					if(element.isDisplayed())
        						element.click();
        					else
        						continue;
        				}
        				isNotStale=false;
        			}catch(Throwable t){
        				continue;
        			}
        			}
        		k.ImplicitWaitOn();
            }
            k.ImplicitWaitOn();
            for(int count=1;count<=no_of_property;count++){
                   
                   WebElement add_row = driver.findElement(By.xpath("//*[contains(@id,'p6_table1')]//a[text()='Add Row']"));
                   add_row.click();
                   
                   elm = driver.findElements(By.xpath("//*[contains(@id,'p6_table1')]//*//select[contains(@name,'pof_bi_cover')]"));
                   //int index = getIndexDropdownBD(internal_data_map.get("Loss Of Rent").get(count-1).get("LOI_CoverName"));
                   customAssert.assertTrue(k.DropDownSelection_WebElement(elm.get(count-1), "0"),"Unable to select LOI_CoverName in Los of rental table.");
                   
                   elm = driver.findElements(By.xpath("//*[contains(@name,'address')]"));
                   customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("Loss Of Rent").get(count-1).get("LOI_Address"),"Input"),"Error while entering Description");
                   
                   elm = driver.findElements(By.xpath("//*[contains(@name,'sum_insured')]"));
                   customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("Loss Of Rent").get(count-1).get("LOI_AnnualGrossRentSumInsured"),"Input"),"Error while entering EXS_ExcessValue");
                   
                   elm = driver.findElements(By.xpath("//*[contains(@name,'indemnity_period')]"));
                   customAssert.assertTrue(k.DropDownSelection_WebElement(elm.get(count-1), internal_data_map.get("Loss Of Rent").get(count-1).get("LOI_IndemnityPeriod")),"Unable to enter EXS_ExcessApplies in Excesses table");
                   
                   customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Excesses page .");
                   
                   double indemnityPeriod = getIndemnityPeriod(internal_data_map.get("Loss Of Rent").get(count-1).get("LOI_IndemnityPeriod"));
                   
                   double expectedGrossRentSumInsured = Double.parseDouble(internal_data_map.get("Loss Of Rent").get(count-1).get("LOI_AnnualGrossRentSumInsured")) * indemnityPeriod;
                   
                   elm = driver.findElements(By.xpath("//*[contains(@name,'sum_total')]"));
                   double actualGrossRentSumInsured = Double.parseDouble(k.DynamicXpathWebElement_GetValue(elm.get(count-1),"GetValue").replaceAll(",", "")); 
                   double diff = expectedGrossRentSumInsured - actualGrossRentSumInsured;
       				if(diff==0.0){
       					String tMsg="Values have been matched for Expected:<b>"+expectedGrossRentSumInsured+" </b> with Actual value :<b> "+actualGrossRentSumInsured+"";
       					TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Loss Of Rent", internal_data_map.get("Loss Of Rent").get(count-1).get("Automation Key"), "LOI_TotalGrossRentSumInsured", Double.toString(actualGrossRentSumInsured), internal_data_map.get("Loss Of Rent").get(count-1));
       					TestUtil.reportStatus(tMsg, "Pass", false);
       				}else if(diff<=1.0 && diff>=-1.0){
       					String tMsg="<p style='color:blue'>Values have been matched for Expected:<b>"+expectedGrossRentSumInsured+" </b> with Actual value :<b> "+actualGrossRentSumInsured+" </p>";
       					TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Loss Of Rent", internal_data_map.get("Loss Of Rent").get(count-1).get("Automation Key"), "LOI_TotalGrossRentSumInsured", Double.toString(actualGrossRentSumInsured), internal_data_map.get("Loss Of Rent").get(count-1));
       					TestUtil.reportStatus(tMsg, "Pass", false);
       				}else{
       					String eMsg="Values have not been matched for Expected:<b>"+expectedGrossRentSumInsured+"</b> with Actual value :<b>"+actualGrossRentSumInsured; 
       					TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Loss Of Rent", internal_data_map.get("Loss Of Rent").get(count-1).get("Automation Key"), "LOI_TotalGrossRentSumInsured", Double.toString(expectedGrossRentSumInsured), internal_data_map.get("Loss Of Rent").get(count-1));
       					TestUtil.reportStatus(eMsg, "Fail", true);
       				}
                   
            }
            
            customAssert.assertTrue(k.Click("POF_CalculateButton"), "Unable to click on Calculate Premium button.");
			
            customAssert.assertTrue(CoverSpecificCalculation("BI",map_data),"Calculation of Business interuption cover is having issues.");
            
            TestUtil.reportStatus("Entered all the details on Business Interruption page .", "Info", true);
            
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
	
	public double getIndemnityPeriod(String IndemnityPeriod) {
		
		
		if(IndemnityPeriod.equalsIgnoreCase("12")){
			return 1.0;
		}else if(IndemnityPeriod.equalsIgnoreCase("18")){
			return 1.5;
		}else if(IndemnityPeriod.equalsIgnoreCase("24")){
			return 2.0;
		}else if(IndemnityPeriod.equalsIgnoreCase("36")){
			return 3.0;
		}else if(IndemnityPeriod.equalsIgnoreCase("48")){
			return 4.0;
		}else if(IndemnityPeriod.equalsIgnoreCase("60")){
			return 5.0;
		}else{
			return 0.0;
		}
		
	}
	
	public boolean funcRewindOperation(Map<Object, Object> map_data){
		
		boolean r_value= true;
		String navigationBy = CONFIG.getProperty("NavigationBy");
		try{
			
			
			//customAssert.assertTrue(funcPolicyGeneral_Rewind(map_data), "Unable to do operation on Policy general page in rewind flow.");
			customAssert.assertTrue(funcPolicyGeneral_MTA(map_data,"POF",common.currentRunningFlow), "Unable to do operation on Policy general page in rewind flow.");
			
			if(((String)map_data.get("CD_Add_Remove_Cover")).equalsIgnoreCase("Yes")){
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
				customAssert.assertTrue(common_Zennor.funcRewindCoversCheck(map_data), "Select covers function is having issue(S) . ");
				
				if(((String)map_data.get("CD_MaterialDamage")).equals("No") &&
						((String)map_data.get("CD_Add_MaterialDamage")).equals("Yes")){
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Damage"),"Issue while Navigating to Material Damage screen.");
					customAssert.assertTrue(MaterialDamagePage(map_data), "Material DamagePage function is having issue(S) . ");
				}
				if(((String)map_data.get("CD_BusinessInterruption")).equals("No") &&
						((String)map_data.get("CD_Add_BusinessInterruption")).equals("Yes")){
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Business Interruption"),"Issue while Navigating to Business Interruption screen.");
					customAssert.assertTrue(BusinessInterruptionPage(map_data), "Business InterruptionPage function is having issue(S) . ");
				}
				if(((String)map_data.get("CD_Liabilities-POL")).equals("No") &&
						((String)map_data.get("CD_Add_Liabilities-POL")).equals("Yes")){
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Property Owners Liabilities"),"Issue while Navigating to Properties Owners Liabilities screen.");
					customAssert.assertTrue(PropertyOwnersLiabilityPage(map_data), "PropertyOwnersLiabilityPage function is having issue(S) . ");
				}
				/*if(((String)common.NB_excel_data_map.get("CD_Liabilities-EL")).equals("Yes")){	
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to Employers Liability screen.");
					//customAssert.assertTrue(Liabilities-ELPage(common.NB_excel_data_map), "Employers Liability function is having issue(S) . ");
				}*/
				if(((String)map_data.get("CD_Terrorism")).equals("No") &&
						((String)map_data.get("CD_Add_Terrorism")).equals("Yes")){
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Terrorism"),"Issue while Navigating to Terrorism screen.");
					customAssert.assertTrue(TerrorismPage(map_data), "TerrorismPage function is having issue(S) . ");
					
				}
				if(((String)map_data.get("CD_BespokeCover")).equals("No") &&
						((String)map_data.get("CD_Add_BespokeCover")).equals("Yes")){
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Bespoke Cover"),"Issue while Navigating to Bespoke Cover screen . ");
					customAssert.assertTrue(funcBespokeCover(map_data), "Bespoke Cover function is having issue(S) . ");
				}
				
				//customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
				//customAssert.assertTrue(common.funcEndorsementOperations(common.NB_excel_data_map),"Endorsement function(funcEndorsementOperations) is having issue(S).");
				common_Zennor.PremiumFlag = false;	
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
				if(common.currentRunningFlow.equalsIgnoreCase("MTA")){
					customAssert.assertTrue(common_Zennor.funcPremiumSummary_MTA(map_data,CommonFunction.product,CommonFunction.businessEvent), "Premium Summary MTA Rewind function is having issue(S) . ");
				}else{
					customAssert.assertTrue(common_Zennor.funcPremiumSummary(map_data,CommonFunction.product,CommonFunction.businessEvent), "Premium Summary function is having issue(S) . ");
				}
				
			}
			
		}catch(Throwable t){
			return false;
			
		}
		
		return r_value;
	}
	




public boolean AnnualWorkPage(Map<Object, Object> map_data){
	boolean retValue = true;
	String abvr = null, coverName = null;
	String s_Sheet =null;
	
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Annual Works", ""),"Annual Works page is having issue(S)");
		 
		 String[] rist_mgmt = ((String)common.NB_excel_data_map.get("AW_InterestsInsured")).split(",");
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
		 customAssert.assertTrue(k.Input("COB_AW_SumInsured_ContractValue", (String)map_data.get("AW_SumInsured_ContractValue")),"Unable to put value in SumInsured_ContractValue field");
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
		 if(sVal.length() > 0){
			 abvr = "AW_AddB_";
			 coverName = "AW";
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
		 
		 String[] rist_mgmt = ((String)common.NB_excel_data_map.get("PPL_PL_RiskManagement")).split(",");
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

	
	try{
		
					
		customAssert.assertTrue(k.Input("COB_BD_Percentage", (String)map_data.get("BD_Percentage")),"Unable to put value in Percentage field");
		
		if(s_coverName.contains("Public & Products Liability") || s_coverName.contains("Annual Works") ){
			
				 // Turnover Breakdown by Geographical Limit
				
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
				
				common.compareValues(Double.parseDouble(s_Total), Double.parseDouble(e_Total), "Total % turnover for Turnover Breakdown by Geographical Limit for cover - " + s_coverName);
			
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
				
				common.compareValues(Double.parseDouble(sTC_Total), Double.parseDouble(e_Total), "Total % turnover for Turnover Breakdown by Trade code for cover - " + s_coverName);
		}
		
				
		// Contracting Turnover by Area of Work
				
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
				
				common.compareValues(Double.parseDouble(sAOW_Total), Double.parseDouble(e_Total), "Total % turnover for Contracting Turnover by Area of Work for cover - " + s_coverName);
				
				
				if(!s_coverName.contains("Annual Works") ){
					// Add Employee Wages :
					
					String s_Wages = (String)map_data.get("BD_EmployeeWageWorkActivities");
					
					s_Xpath = "//table[contains(@id,'_employee_wages_breakdown_table')]";
					s_Table  = driver.findElement(By.xpath(s_Xpath));
					 
					 List<WebElement> EW_Rows = s_Table.findElements(By.tagName("tr"));
					 int e_Rows = EW_Rows.size();
					 
					if(s_Wages.length() > 0){
						
						String wagesList[] = s_Wages.split(";");
						
						for(int i = 0; i < wagesList.length; i++){
							String e_WagesActivity = s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[1]/input")).getAttribute("value");
							String Val_Excel = (String)common.NB_Structure_of_InnerPagesMaps.get("Employee Wages").get(i).get("EW_Employee_Wage_Breakdown_Activities");
							if(e_WagesActivity.contains(Val_Excel)){
								
								// Select region
									s_Val = (String)common.NB_Structure_of_InnerPagesMaps.get("Employee Wages").get(i).get("EW_Workaway_location _region");
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
								
									s_Val = (String)common.NB_Structure_of_InnerPagesMaps.get("Employee Wages").get(i).get("EW_Description_of_Activity");
									s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[3]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[3]/input")).sendKeys(s_Val);
									
								// Enter Estimated Annual WageRoll :
									s_Val = (String)common.NB_Structure_of_InnerPagesMaps.get("Employee Wages").get(i).get("EW_Estimated_Annual_WageRoll");
									s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[4]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[4]/input")).sendKeys(s_Val);
									
								// Enter Number of employees :
									s_Val = (String)common.NB_Structure_of_InnerPagesMaps.get("Employee Wages").get(i).get("EW_Number_of_employees");
									s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									s_Table.findElement(By.xpath(s_Xpath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(s_Val);
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
			
		 customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, sTablePath, sCover, abvr),"Unable to handle manual rated premium table on Legal Expenses screen");
			 
			 
		 
//		int LE_Premium_Decimal_Places = common.countDecimalPlaces(k.getAttribute("CCF_LE_NetPremium", "value"));
//		boolean decimalFlag = LE_Premium_Decimal_Places > 2 ?false:true;
//		customAssert.assertTrue(decimalFlag , "LE Premium Should contain up to two Decimal Places . ");
//		
//		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques1", (String)map_data.get("LE_Ques1")),"Unable to Select LE_Ques1 radio button . ");
//		
//		//Statement of Fact
//		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques1", (String)map_data.get("LE_Ques1")),"Unable to Select LE_Ques1 radio button . ");
//		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques2", (String)map_data.get("LE_Ques2")),"Unable to Select LE_Ques2 radio button . ");
//		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques3", (String)map_data.get("LE_Ques3")),"Unable to Select LE_Ques3 radio button . ");
//		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques4", (String)map_data.get("LE_Ques4")),"Unable to Select LE_Ques4 radio button . ");
//		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques5", (String)map_data.get("LE_Ques5")),"Unable to Select LE_Ques5 radio button . ");
//		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques6", (String)map_data.get("LE_Ques6")),"Unable to Select LE_Ques6 radio button . ");
//				
//		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Legal Expenses .");
//		
//		exp_AnnualCarrierPremium = common.roundedOff(common.getLEAnnualCarrierPremium(common.product,(String)map_data.get("LE_LimitOfLiability"), (String)map_data.get("LE_Turnover"), (String)map_data.get("LE_Wages")));
//		act_AnnualCarrierPremium = k.getAttribute("CCF_LE_AnnualCarrierPremium","value");
//		customAssert.assertEquals(exp_AnnualCarrierPremium, act_AnnualCarrierPremium,"Annual Carrier Premium (Excludes IPT) is incorrect Exppected: <b>"+exp_AnnualCarrierPremium+"</b> Actual: <b>"+act_AnnualCarrierPremium+" for Legal Expense . ");
//		customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Legal Expenses", testName, "LE_AnnualCarrierPremium", exp_AnnualCarrierPremium,common.NB_excel_data_map),"Error while writing data to excel for field >NB_ClientId<");
		
		
		TestUtil.reportStatus("Legal Expenses details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean PropertyOwnersLiabilityPage(Map<Object, Object> map_data){
	boolean retValue = true;
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Property Owners Liabilities", ""),"Property Owners Liability page is having issue(S)");
		 
		 customAssert.assertTrue(k.DropDownSelection("POF_indemnity", (String)map_data.get("POL_LimitOfIndemnity")),"Unable to select value from Indemnity dropdown");
		 customAssert.assertTrue(k.DropDownSelection("POF_premium", (String)map_data.get("POL_PremiumIncluded")),"Unable to select value from premium dropdown.");
		
		 customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Property Owners Liability page .");
		 
		 String PremiumIncludedMaterialDamageSection = (String)map_data.get("POL_PremiumIncluded");
		 
		 if(!PremiumIncludedMaterialDamageSection.equalsIgnoreCase("Yes")){
			 customAssert.assertTrue(CoverSpecificCalculation("POL",map_data),"Calculation of Business interuption cover is having issues.");
		 }else{
			 TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_PropertyOwnersLiabilities_NetNetPremium", "0", map_data);
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


public boolean TerrorismPage(Map<Object, Object> map_data){
boolean retValue = true;
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Terrorism", ""),"Terrorism page is having issue(S)");
		
		 
		 Map<String, List<Map<String, String>>> internal_data_map = new HashMap<>() ;
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
			
			}
		 
			if(((String)map_data.get("PG_Carrier")).contains("NIG")){
				customAssert.assertTrue(k.Input("POF_TER_CarrierPolicyNumber", (String)map_data.get("TER_CarrierPolicyNumber")), "Unable to enter Carrier Policy Number on Terorism screen.");
			}
			
		 String[] properties = ((String)map_data.get("Add_TerrorismZone")).split(";");
         int no_of_property = properties.length;
         
         	//For MTA Flow
			if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	         	boolean isNotStale=true;
	         	k.ImplicitWaitOff();
 			
	         	while(isNotStale){
	         		try{
	         			List<WebElement> delete_Btns = driver.findElements(By.xpath("//div[text()='Terrorism']//following::table[2]//*[text()='Delete']"));
	         			for(WebElement element: delete_Btns){
	         				if(element.isDisplayed())
	         					element.click();
	         				else
	         					continue;
	         			}
	         			isNotStale=false;
	         		}catch(Throwable t){
	         			continue;
	         		}
     			} //while loop
	         	k.ImplicitWaitOff();
				}
			k.ImplicitWaitOff();
         
         List<WebElement> elm = null;
         
         for(int count=1;count<=no_of_property;count++){
                
            WebElement add_row = driver.findElement(By.xpath("//*[contains(@id,'p8_table1')]//a[text()='Add Row']"));
            add_row.click();
            
            elm = driver.findElements(By.xpath("//*[contains(@id,'p8_table1')]//*//select[contains(@name,'pof_tr_zone')]"));
            customAssert.assertTrue(k.DropDownSelection_WebElement(elm.get(count-1), internal_data_map.get("Terrorism Zone").get(count-1).get("TER_Zone")),"Unable to select TER_Zone in Terrorism table.");
            
            elm = driver.findElements(By.xpath("//*[contains(@name,'sum_insured')]"));
            customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("Terrorism Zone").get(count-1).get("TER_SumInsured"),"Input"),"Error while entering TER_SumInsured in Terrorism table.");
            
            elm = driver.findElements(By.xpath("//*[contains(@name,'pool_re')]"));
            customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1), internal_data_map.get("Terrorism Zone").get(count-1).get("TER_PoolRe"),"Input"),"Unable to enter TER_PoolRe in Terrorism table.");
            
            elm = driver.findElements(By.xpath("//*[contains(@name,'premium')]"));
            customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1), internal_data_map.get("Terrorism Zone").get(count-1).get("TER_Premium"),"Input"),"Unable to enter TER_Premium in Terrorism table.");
            
            customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Excesses page .");
            
         }
         
         customAssert.assertTrue(k.Click("POF_CalculateButton"), "Unable to click on Calculate Premium button.");
         customAssert.assertTrue(CoverSpecificCalculation("TER",map_data),"Calculation of Business interuption cover is having issues.");
         TestUtil.reportStatus("Entered all the details on Terrorism page .", "Info", true);
         				
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

public boolean funcBespokeCover(Map<Object, Object> map_data){
	boolean retValue = true;
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Bespoke Cover", ""),"Bespoke Cover page is having issue(S)");
		 
		 
		 Map<String, List<Map<String, String>>> internal_data_map = new HashMap<>() ;
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
			
			}
		 
			int count = 0;
			int noOfProperties = 0;
			
			
			if(common.currentRunningFlow.equalsIgnoreCase("NB")){
					
				if(common.no_of_inner_data_sets.get("Add BeSpokeCover")==null){
					noOfProperties = 0;
				}else{
					noOfProperties = common.no_of_inner_data_sets.get("Add BeSpokeCover");
				}
			}else{
				
				String[] n_properties = ((String)map_data.get("Add_BeSpoke")).split(";");
				noOfProperties = n_properties.length;
				
			}
			
			//For MTA Flow
			if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
            	
	         	boolean isNotStale=true;
        		
    			k.ImplicitWaitOff();
        		while(isNotStale){
        			try{
        				List<WebElement> delete_Btns = driver.findElements(By.xpath("//div[text()='Bespoke Cover']//following::table[2]//*[text()='Delete']"));
        				for(WebElement element: delete_Btns){
        					if(element.isDisplayed()){
        						element.click();
        						k.AcceptPopup();
        					}else{
        						continue;}
        				}
        				isNotStale=false;
        			}catch(Throwable t){
        				continue;
        			}
        			}
        		k.ImplicitWaitOn();
				}
			k.ImplicitWaitOn();
			
			while(count < noOfProperties ){
				
				customAssert.assertTrue(k.Click("POF_BS_AddItem"), "Unable to click Add Item Button on BeSpoke Cover screen .");
				customAssert.assertTrue(addItems(map_data,count,internal_data_map),"Error while adding items  .");
				TestUtil.reportStatus("Item on Besoke Cover  <b>[  "+internal_data_map.get("Add BeSpokeCover").get(count).get("Automation Key")+"  ]</b>  added successfully . ", "Info", true);
				count++;
			}
			
			customAssert.assertTrue(CoverSpecificCalculation("BSC",map_data),"Calculation of Business interuption cover is having issues.");
			
			TestUtil.reportStatus("All the specified Insured properties added and verified successfully . ", "Info", true);
			
		 
		 
		 
		 
		 
		/* String[] properties = ((String)map_data.get("Add_BeSpoke")).split(";");
         int no_of_property = properties.length;
         
         List<WebElement> elm = null;
         
         for(int count=1;count<=no_of_property;count++){
                
            
            customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Excesses page .");
            
         }
         
         customAssert.assertTrue(k.Click("POF_CalculateButton"), "Unable to click on Calculate Premium button.");*/
			
         TestUtil.reportStatus("Entered all the details on Terrorism page .", "Info", true);
         				
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
public boolean addItems(Map<Object, Object> map_data,int count,Map<String, List<Map<String, String>>> internal_data_map){
	
	boolean r_value=true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Bespoke Cover", ""),"Property Details page navigation issue(S)");
		
		customAssert.assertTrue(k.Input("POF_BS_CoverType", internal_data_map.get("Add BeSpokeCover").get(count).get("Add_BeSpokeCoverType")), "Unable to enter Cover Type on Bespoke cover screen.");
		customAssert.assertTrue(k.Input("POF_BS_SumInsured", internal_data_map.get("Add BeSpokeCover").get(count).get("Add_BeSpokeSumInsured")), "Unable to enter Sum Insured / Limit  on Bespoke cover screen.");
		customAssert.assertTrue(k.Input("POF_BS_Excess", internal_data_map.get("Add BeSpokeCover").get(count).get("Add_BeSpokeExcess")), "Unable to enter Excess on Bespoke cover screen.");
		
		customAssert.assertTrue(k.clickInnerButton("Inner_page_locator", "Save"), "Unable to click on Inner button.");
		
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
}	
public boolean JCTPage(Map<Object, Object> map_data){
	boolean retValue = true;
	String abvr = null;
	String s_Sheet =null;
	
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("JCT 6.5.1", ""),"JCT 6.5.1 page is having issue(S)");
		 
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
				
			String AllBuilding[] =  s_Building.split(";");
			
			for(int i = 0; i < AllBuilding.length; i++){
				
				customAssert.assertTrue(k.Click("COB_JCT_AddBuilding"),"Unable to click on JCT_AddBuilding button");
								
				customAssert.assertTrue(k.Input("COB_AB_JCT_Description", (String)common.NB_Structure_of_InnerPagesMaps.get("AddBuildingJCT").get(i).get("AB_JCT_Description")),"Unable to put value in Description field for add building");
				customAssert.assertTrue(k.Input("COB_AB_JCT_Address",(String)common.NB_Structure_of_InnerPagesMaps.get("AddBuildingJCT").get(i).get("AB_JCT_Address")),"Unable to put value in PleaseProvideFullDetails field");
				customAssert.assertTrue(k.Input("COB_AB_JCT_Address_PostCode",(String)common.NB_Structure_of_InnerPagesMaps.get("AddBuildingJCT").get(i).get("AB_JCT_Address_PostCode")),"Unable to put value in BookPremium field");
				customAssert.assertTrue(k.Input("COB_AB_JCT_ApproxDistanceFromSite",(String)common.NB_Structure_of_InnerPagesMaps.get("AddBuildingJCT").get(i).get("AB_JCT_ApproxDistanceFromSite")),"Unable to put value in Description field");
				customAssert.assertTrue(k.Input("COB_AB_JCT_ApproxAge",(String)common.NB_Structure_of_InnerPagesMaps.get("AddBuildingJCT").get(i).get("AB_JCT_ApproxAge")),"Unable to put value in Description field");
				
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

public boolean HireCoverPlusPage(Map<Object, Object> map_data){
	boolean retValue = true;
	String abvr = null;
	
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Hire Cover Plus", ""),"Hire Cover Plus page is having issue(S)");
				
		 customAssert.assertTrue(k.Input("COB_HCP_HireWithWaiver", (String)map_data.get("HCP_HireWithWaiver")),"Unable to put value in CurrentMarketValue field");
		 customAssert.assertTrue(k.Input("COB_HCP_HireWithoutWaiver", (String)map_data.get("HCP_HireWithoutWaiver")),"Unable to put value in CurrentMarketValue field");
		 
		 k.Click("COB_HCP_Total");
		 String s_Total = k.getAttribute("COB_HCP_Total","value");
		 CommonFunction.compareValues(Double.parseDouble((String)map_data.get("HCP_Total")), Double.parseDouble(s_Total),"Total Turnover % for Hire Cover Plus cover ");
		
		 customAssert.assertTrue(k.Input("COB_HCP_OPIL_AnyOneOccurrence", (String)map_data.get("HCP_OPIL_AnyOneOccurrence")),"Unable to put value in IL_AnyOneOccurrence field");
		 customAssert.assertTrue(k.Input("COB_HCP_OPIL_AnyOneItem", (String)map_data.get("HCP_OPIL_AnyOneItem")),"Unable to put value in IL_AnyOneItem field");
		 customAssert.assertTrue(k.Input("COB_HCP_NPHP_IndemnityLimit", (String)map_data.get("HCP_IL_Premises")),"Unable to put value in IL_Premises field");
		 
		 customAssert.assertTrue(k.Input("COB_HCP_HPIL_AnyOneOccurrence", (String)map_data.get("HCP_HPIL_AnyOneOccurrence")),"Unable to put value in HPIL_AnyOneOccurrence field");
		 customAssert.assertTrue(k.Input("COB_HCP_HPIL_AnyOneItem", (String)map_data.get("HCP_HPIL_AnyOneItem")),"Unable to put value in HPIL_AnyOneItem field");
		 
		 customAssert.assertTrue(k.Input("COB_HCP_CHCIL_AnyOneOccurrence", (String)map_data.get("HCP_CHCIL_AnyOneOccurrence")),"Unable to put value in CHCIL_AnyOneOccurrence field");
		 customAssert.assertTrue(k.Input("COB_HCP_CHCIL_AnyOneItem", (String)map_data.get("HCP_CHCIL_AnyOneItem")),"Unable to put value in CHCIL_AnyOneItem field");
		 
		 String sVal = (String)map_data.get("HCP_AddBespoke");
		 if(sVal.length() > 0){
			 abvr = "HCP_AddB_";
			 customAssert.assertTrue(funcAddBespoke(map_data, sVal, abvr,"HCP"),"Unable to add bspoke item for HCP cover");
		 }
		 
		 // Click on apply book rates button :
		 
		 customAssert.assertTrue(k.Click("COB_HCP_CalculatePremium"),"Unable to click on CalculatePremium button");
		 
		 // Get table id :
		 
		 	String sUniqueCol ="Tech. Adjust (%)";
			int tableId = 0;
			
			String sTablePath = "html/body/div[3]/form/div/table";
			
			tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
			sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
			String sCover = "Hire Cover Plus";
			 abvr = "HCP_";
			
			customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, sTablePath, sCover,abvr),"Unable to handle Auto rated premium table on HCP screen");
			
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
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(sVal);
									
								}else if(s_Section.contains("Bona Fide Sub-Contractors")){
									sVal = (String)map_data.get("PPL_BonaFide_TechAdjust");
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
									
									sVal = (String)map_data.get("PPL_BonaFide_CommAdjust");
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[7]/input")).sendKeys(sVal);
								}else{						
									sVal = (String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(sVal);
									
									sVal = (String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
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
									sVal = (String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
									
									sVal = (String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
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
									innerSheetName = AllBespoke[0].replace("_01", "");
									int j;
									for(j=0; j<AllBespoke.length; j++){
										if(s_Section.equals((String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(j).get("LE_AddB_Description"))){
											break;
										}
									}
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys((String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(j).get("LE_TechAdjustment"));
									
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys((String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(j).get("LE_PremiumOverride"));
								}
							}
							break;	
						case "Annual Works":
							sRater = "Annual Works";
							for(int i = 0; i< totalRows-2; i++){
								s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[1]");
								
								if(s_Section.contains("Existing Structures") || s_Section.contains("DE5") || s_Section.contains("Other works")){
									
									sVal = (String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
									
									sVal = (String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
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
									
									sVal = (String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
									
									sVal = (String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
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
									
									 sVal = (String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
										driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
										
										sVal = (String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
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
									
									sVal = (String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
									
									sVal = (String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
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

									sVal = (String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust");
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(sVal);
									
									sVal = (String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust");
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
							c_Wages = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get("Employee Wages").get(i).get("EW_Estimated_Annual_WageRoll"));
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
							c_BookP = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"BookPremium"));
							c_TechAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
							c_CommAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
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
							c_Wages = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get("Employee Wages").get(i).get("EW_Estimated_Annual_WageRoll"));
							c_TechAdjust = Double.parseDouble((String)map_data.get(s_ColName +" Tech Adjust"));
							c_CommAdjust = Double.parseDouble((String)map_data.get(s_ColName +" Comm Adjust"));
							
							c_BookRate = common_COB.Book_rate_Rater_output.get(sRater+"_"+s_Section);
							c_BookP =  c_Wages* c_BookRate/100;
							ad_Rate = c_BookRate + ((c_BookRate*c_TechAdjust)/100);
							ad_Rate = ad_Rate + ((ad_Rate*c_CommAdjust)/100);
							c_Premium = ad_Rate*c_Wages/100;
							customAssert.assertTrue(common_VELA.func_Referral_Rating_minimum_Premium(sRater, s_Section, ad_Rate),"Unable to handle func_Referral_Rating_minimum_Premium function for EL");
							
						}else{
							c_BookP = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"BookPremium"));
							c_TechAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
							c_CommAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
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
							
							c_BookP = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"BookPremium"));
							c_TechAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
							c_CommAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
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
							innerSheetName = AllBespoke[0].replace("_01", "");
							int j;
							for(j=0; j<AllBespoke.length; j++){
								if(s_Section.equals((String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(j).get("LE_AddB_Description"))){
									break;
								}
							}
							CommonFunction_VELA.compareValues(Double.parseDouble(BookPremium), Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(j).get("LE_AddB_BookPremium")), "Book Premium for Legal Expenses All Bespoke"+AllBespoke[j]);
							
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
					for(int i = 0; i< totalRows-2; i++){
						s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[1]");
						
						s_BookRate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[3]/input"));
											
						s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[4]/input"));
						s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[5]/input"));
						s_CommAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[6]/input"));
						s_Premium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[9]/input"));
						
						if(s_Section.contains("Existing Structures") || s_Section.contains("DE5") || s_Section.contains("Other works")){
							
							c_BookP = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"BookPremium"));
							c_TechAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
							c_CommAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
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
							
							customAssert.assertTrue(common_VELA.func_Referral_Rating_minimum_Premium_Plant(sRater, s_Section, ad_Rate),"Unable to handle func_Referral_Rating_minimum_Premium function for Own Plant");
															
						}else{
							
							c_BookP = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"BookPremium"));
							c_TechAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
							c_CommAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
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
							
							customAssert.assertTrue(common_VELA.func_Referral_Rating_minimum_Premium_Plant(sRater, s_Section, ad_Rate),"Unable to handle func_Referral_Rating_minimum_Premium function for HIP");
															
						}else{
							
							c_BookP = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"BookPremium"));
							c_TechAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
							c_CommAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
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
							
							c_BookP = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"BookPremium"));
							c_TechAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"TechAdjust"));
							c_CommAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(InnerCount).get(i_abvr+"CommAdjust"));
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
	
	try{
				
			String AllContract[] =  s_Contract.split(";");
			innerSheetName = AllContract[0].replace("_01", "");
			
			for(int i = 0; i < AllContract.length; i++){
				
				customAssert.assertTrue(k.Click("COB_AW_AddContract"),"Unable to click on AddContract button");
				
				customAssert.assertTrue(k.Input("COB_AW_AD_Description",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
				customAssert.assertTrue(k.Input("COB_AW_AD_AddressLine_1",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"AddressLine_1")),"Unable to put value in AddressLine_1 field");
				customAssert.assertTrue(k.Input("COB_AW_AD_Postcode",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Postcode")),"Unable to put value in Postcode field");
				customAssert.assertTrue(k.Input("COB_AW_AD_Duration",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Duration")),"Unable to put value in Duration field");
				customAssert.assertTrue(k.Input("COB_AW_AD_Value",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Value")),"Unable to put value in Value field");
				
				
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
							customAssert.assertTrue(k.Input("COB_JCT_AddB_Description",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
						
					case "Ter" :
						if(k.isDisplayed("COB_HCP_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_HCP_AddB_Description",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
						
					case "HCP" :
						if(k.isDisplayed("COB_HCP_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_HCP_AddB_Description",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
						
					case "POL" :
						if(k.isDisplayed("COB_POL_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_POL_AddB_Description",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
					case "LE" : 
						if(k.isDisplayed("COB_LE_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_LE_AddB_Description",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_LE_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_LE_AddB_BookPremium",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
						
					case "SP" :
					case "AW" :
						//customAssert.assertTrue(k.DropDownSelection("COB_AddB_Additionalcover", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")),"Unable to put value in Additionalcover field");
						if(s_CoverName.equalsIgnoreCase("SP"))
							customAssert.assertTrue(k.DropDownSelection("COB_AddB_Additionalcover", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")),"Unable to put value in Additionalcover field");
						else{
							WebElement el = driver.findElement(By.xpath("//*[@class='subpage']//*[text()='Additional Cover']//following::select[1]"));
							Select sl = new Select(el);
							sl.selectByValue((String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover"));
						}
						
						//if(k.isDisplayed("COB_AddB_SP_TypeOfLimit")){
							//customAssert.assertTrue(k.DropDownSelection("COB_AddB_SP_TypeOfLimit", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"TypeOfLimit")),"Unable to put value in TypeOfLimit field");
						//}
						
						if(k.isDisplayed("COB_AddB_SP_LOI")){
							customAssert.assertTrue(k.Input("COB_AddB_SP_LOI",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"LOI")),"Unable to put value in LOI field");
						}		
						
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						
						String AddCov = (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover");
						if(AddCov.contains("Other")){
							customAssert.assertTrue(k.Input("COB_AddB_Description",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");							
						}
						
						break;
						
					case "OP" :
						customAssert.assertTrue(k.DropDownSelection("COB_AddB_Additionalcover", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")),"Unable to put value in Additionalcover field");
						
						//if(k.isDisplayed("COB_AddB_SP_TypeOfLimit")){
							//customAssert.assertTrue(k.DropDownSelection("COB_AddB_SP_TypeOfLimit", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"TypeOfLimit")),"Unable to put value in TypeOfLimit field");
						//}
						
						if(k.isDisplayed("COB_AddB_OP_BespokeLOI")){
							customAssert.assertTrue(k.Input("COB_AddB_OP_BespokeLOI",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"LOI")),"Unable to put value in LOI field");
						}		
						
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						
						AddCov = (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover");
						if(AddCov.contains("Other")){
							customAssert.assertTrue(k.Input("COB_AddB_Description",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");							
						}
						
						break;
						
					case "HIP" :
						
						customAssert.assertTrue(k.DropDownSelection("COB_AddB_Additionalcover", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")),"Unable to put value in Additionalcover field");
						
						//if(k.isDisplayed("COB_AddB_HIP_TypeOfLimit")){
							//customAssert.assertTrue(k.DropDownSelection("COB_AddB_HIP_TypeOfLimit", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"TypeOfLimit")),"Unable to put value in TypeOfLimit field");
						//}
						
						if(k.isDisplayed("COB_AddB_LOI")){
							customAssert.assertTrue(k.Input("COB_AddB_LOI",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"LOI")),"Unable to put value in LOI field");
						}
						
						if(k.isDisplayed("COB_AddB_HIP_Description")){
							customAssert.assertTrue(k.Input("COB_AddB_HIP_Description",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
						
					default :	
						customAssert.assertTrue(k.DropDownSelection("COB_AddB_Additionalcover", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")),"Unable to put value in Additionalcover field");
						
						if(k.isDisplayed("COB_AddB_TypeOfLimit")){
							customAssert.assertTrue(k.DropDownSelection("COB_AddB_TypeOfLimit", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"TypeOfLimit")),"Unable to put value in TypeOfLimit field");
						}
						
						if(k.isDisplayed("COB_AddB_LOI")){
							customAssert.assertTrue(k.Input("COB_AddB_LOI",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"LOI")),"Unable to put value in LOI field");
						}
						
						if(k.isDisplayed("COB_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_AddB_Description",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						
						if(s_CoverName.equalsIgnoreCase("OP")){
							//Referral code
								if(((String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")).equalsIgnoreCase("Subrogation Waiver"));
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
		 k.ImplicitWaitOff();
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
				 customAssert.assertTrue(k.Input("COB_GRD_AddMP_ArchitectPostCode", (String)common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get("GRD_"+s_Section+"_ArchitectPostCode")),"Unable to put value in ArchitectPostCode field");
				 customAssert.assertTrue(k.Input("COB_GRD_AddMP_EngineerPostCode", (String)common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get("GRD_"+s_Section+"_EngineerPostCode")),"Unable to put value in EngineerPostCode field");
				 customAssert.assertTrue(k.DropDownSelection("COB_GRD_AddMP_TypeOfPremises", (String)common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get("GRD_"+s_Section+"_TypeOfPremises")),"Unable to put value in TypeOfPremises field");
				 customAssert.assertTrue(k.Input("COB_GRD_AddMP_AgeAndCondition", (String)common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get("GRD_"+s_Section+"_AgeAndCondition")),"Unable to put value in AgeAndCondition field");
				 customAssert.assertTrue(k.Input("COB_GRD_AddMP_ProvideFullDetails",(String)common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get("GRD_"+s_Section+"_ProvideFullDetails")),"Unable to put value in  PleaseProvideFullDetails field");
				 customAssert.assertTrue(k.DropDownSelection("COB_GRD_AddMP_ConstrustionMethod", (String)common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get("GRD_"+s_Section+"_ConstrustionMethod")),"Unable to put value in ConstrustionMethod field");
				 customAssert.assertTrue(k.Input("COB_GRD_AddMP_StucturalWorkPerc", (String)common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get("GRD_"+s_Section+"_SucturalWorkPerc")),"Unable to put value in StucturalWorkPerc field");
				 customAssert.assertTrue(k.Input("COB_GRD_AddMP_HeatWorkPerc", (String)common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get("GRD_"+s_Section+"_HeatWorkPerc")),"Unable to put value in HeatWorkPerc field");
				 customAssert.assertTrue(k.Input("COB_GRD_AddMP_ContractPrice", (String)common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get("GRD_"+s_Section+"_ContractPrice")),"Unable to put value in ContractPrice field");
				 
				 // Start Date and Completion date :
				 
				 	customAssert.assertTrue(k.Click("COB_GRD_AddMP_WorkStartDate"), "Unable to Click WorkStartDate");
					customAssert.assertTrue(k.Input("COB_GRD_AddMP_WorkStartDate", (String)common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get("GRD_"+s_Section+"_WorkStartDate")),"Unable to Enter WorkStartDate.");
					customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
					customAssert.assertTrue(!k.getAttributeIsEmpty("COB_GRD_AddMP_WorkStartDate", "value"),"WorkStartDate Field Should Contain Valid value  .");
					
					customAssert.assertTrue(k.Click("COB_GRD_AddMP_CompletionDate"), "Unable to Click CompletionDate.");
					customAssert.assertTrue(k.Input("COB_GRD_AddMP_CompletionDate", (String)common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get("GRD_"+s_Section+"_CompletionDate")),"Unable to Enter CompletionDate.");
					customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
					customAssert.assertTrue(!k.getAttributeIsEmpty("COB_GRD_AddMP_CompletionDate", "value"),"CompletionDate Field Should Contain Valid value  .");
					
				customAssert.assertTrue(k.Input("COB_GRD_AddMP_ReinstatementValue", (String)common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get("GRD_"+s_Section+"_ReinstatementValue")),"Unable to put value in ReinstatementValue field");
				customAssert.assertTrue(k.Input("COB_GRD_AddMP_SiteSecurity", (String)common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get("GRD_"+s_Section+"_SiteSecurity")),"Unable to put value in SiteSecurity field");
				
				
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
								 q_value = (String)common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get("GRD_"+s_Section+"_Q"+(j-5));
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
		 
		List<WebElement> elements = driver.findElements(By.className("selectinput"));
		 Select sel = null;
		
		 for(int i = 0;i<elements.size();i++){
			 if(elements.get(i).isDisplayed()){
				 
				 sel = new Select(elements.get(i));
				 try{
					 q_value = (String)common.NB_excel_data_map.get("MFD_Q"+(i+1));
					 
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
			 String[] geographical_Limits = ((String)common.NB_excel_data_map.get("MFD_PLMF")).split(",");
			 customAssert.assertTrue(k.Click("MFD_PLMF"),"Error while Clicking Geographic Limit List object . ");
			 List<WebElement> _prd_liability_MF = driver.findElements(By.xpath("//html//body//span//span//following::ul//li"));	
			 	for(String geo_limit : geographical_Limits){
					for(WebElement each_ul : _prd_liability_MF){
						//customAssert.assertTrue(k.Click("MFD_PLMF"),"Error while Clicking Geographic Limit List object . ");
						//each_ul.click();
						k.waitTwoSeconds();//[text()=
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
		 q_value = (String)common.NB_excel_data_map.get("MFD_T");
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
	
	try{
		common.currentRunningFlow = "NB";
	
		customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");
		customAssert.assertTrue(k.Input("COB_PG_InsuredName", (String)map_data.get("PG_InsuredName")),	"Unable to enter value in Insured Name  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_InsuredName", "value"),"Insured Name Field Should Contain Valid Name  .");
		if(((String)map_data.get("PG_AdditionalInsuredName")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(k.Click("POF_PG_AdditionalInsuredName"),	"Unable to enter value in Turnover field .");
			customAssert.assertTrue(k.Input("POF_PG_AdditionalFurtherDetails", (String)map_data.get("PG_AdditionalFurtherDetails")),	"Unable to enter value in Turnover field .");
			
		}
		customAssert.assertTrue(k.Input("POF_PG_TradingName", (String)map_data.get("PG_TradingName")),	"Unable to enter value in Trading Name  field .");
		customAssert.assertTrue(k.DropDownSelection("POF_PG_CompanyStatus", (String)map_data.get("PG_CompanyStatus")), "Unable to select Comapny status from dropdown field.");
		customAssert.assertTrue(k.DropDownSelection("POF_PG_Carrier", (String)map_data.get("PG_Carrier")), "Unable to select Comapny status from dropdown field.");
		//if(((String)map_data.get("PG_Carrier")).contains("NIG")){
		// CR272_001, CR272_002, CR272_003, CR272_004, CR272_005
		String policyRefNumber = "TEST_POLICY_NUMBER_1234";
		customAssert.assertTrue(k.Input("POF_PG_CarrierPolicyNumber", policyRefNumber),"Unable to enter value in Address field line 1 . ");
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Policy General Page.");
		String errorMsg = driver.findElement(By.xpath(".//*[@id='V_pof_carrier_ref_num']")).getText();
		if(errorMsg.equalsIgnoreCase("Please enter a valid policy number in \"YY/RSL/SYSTEM\" format")){
			TestUtil.reportStatus("Error Message Verified for incorrect format of Carrier Policy Number.", "Pass", false);
				
		}else{
			TestUtil.reportStatus("Error Message is not correct for Carrier Policy Number", "fail", true);
		}
		policyRefNumber = common_Zennor.getUniquePolicyReferanceNumber(map_data);
		customAssert.assertTrue(k.Input("POF_PG_CarrierPolicyNumber", policyRefNumber),"Unable to enter value in Address field line 1 . ");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Policy General", (String)map_data.get("Automation Key"), "PG_CarrierPolicyNumber", policyRefNumber, map_data);
		TestUtil.reportStatus("Policy Referance Number for Carrier : <b>[  "+(String)map_data.get("PG_Carrier")+"  ]</b> is : <b>[  "+policyRefNumber+"  ]</b>", "Info", false);
		//}
		customAssert.assertTrue(k.Input("CCF_Address_CC_Address", (String) map_data.get("PG_Address")),"Unable to enter value in Address field. ");
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"Address Field Should Contain Valid Address  .");
		customAssert.assertTrue(k.Input("CCF_Address_CC_line2", (String) map_data.get("PG_Line1")),"Unable to enter value in Address field line 1 . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_line3", (String) map_data.get("PG_Line2")),"Unable to enter value in Address field line 2 . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_Town", (String) map_data.get("PG_Town")),"Unable to enter value in Town field . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_County", (String) map_data.get("PG_County")),"Unable to enter value in County  . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode", (String)map_data.get("PG_Postcode")),"Unable to enter value in PostCode");
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Postcode", "value"),"PostCode Field Should Contain Valid Postcode  .");
		customAssert.assertTrue(common.validatePostCode((String)map_data.get("PG_Postcode")),"Post Code is not in Correct format .");
		
		
		customAssert.assertTrue(k.Input("COB_PG_BusDesc", (String)map_data.get("PG_BusDesc")),	"Unable to enter value in Provided Details field .");
		
		// Select Trade Code :
		
		if(((String)common.NB_excel_data_map.get("PG_TCS_TradeCode_Button")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common_Zennor.tradeCodeSelection((String)common.NB_excel_data_map.get("PG_TCS_TradeCode") , "Policy Details" , 0),"Trade code selection function is having issue(S).");
		}
		
		customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");		
		customAssert.assertTrue(k.DropDownSelection("POF_PG_SuppressPremiumFromDocumentation", (String)map_data.get("PG_SuppressPremiumFromDocumentation")), "Unable to select Comapny status from dropdown field.");
		customAssert.assertTrue(k.DropDownSelection("POF_PG_SuppressDocumentation", (String)map_data.get("PG_SuppressDocumentation")), "Unable to select Comapny status from dropdown field.");
		TestUtil.reportStatus("Entered all the details on Policy General page .", "Info", true);
		
		return retVal;
	
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Unable to to do operation on policy General page. \n", t);
        return false;
	}
	
}


public boolean funcPolicyGeneral_Rewind(Map<Object, Object> map_data) {
	boolean retVal = true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");
		
		if(common.currentRunningFlow.equalsIgnoreCase("Renewal")){
			String Carrier = k.getText("POF_PG_Carrier_Renewal");
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Policy General", (String)map_data.get("Automation Key"), "PG_Carrier", Carrier, map_data);
			String policyRefNumber = common_Zennor.getUniquePolicyReferanceNumber(map_data);
			customAssert.assertTrue(k.Input("POF_PG_CarrierPolicyNumber", policyRefNumber),"Unable to enter value in POF_PG_CarrierPolicyNumber . ");
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Policy General", (String)map_data.get("Automation Key"), "PG_CarrierPolicyNumber", policyRefNumber, map_data);
			TestUtil.reportStatus("Policy Referance Number for Carrier : <b>[  "+(String)map_data.get("PG_Carrier")+"  ]</b> is : <b>[  "+policyRefNumber+"  ]</b>", "Info", false);
		}else{
			String exp_nbP_number = (String)common.NB_excel_data_map.get("PG_CarrierPolicyNumber");
			String act_c_P_number = k.getAttribute("POF_PG_CarrierPolicyNumber", "value");
				
			if(!exp_nbP_number.equalsIgnoreCase(act_c_P_number)){
					 TestUtil.reportStatus("<p style='color:red'> Carrier Policy Number displayed for MTA is not equal to New Business Expected: <b>"+exp_nbP_number+"</b> Actual: <b>"+act_c_P_number+"</b> . </p>" , "Fail", true);
			}
				
			if(!((String)map_data.get("IsSameCarrierPolicyNumber")).equalsIgnoreCase("Yes")){
				String policyRefNumber = common_Zennor.getUniquePolicyReferanceNumber(map_data);
				customAssert.assertTrue(k.Input("POF_PG_CarrierPolicyNumber", policyRefNumber),"Unable to enter value in POF_PG_CarrierPolicyNumber . ");
				TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Policy General", (String)map_data.get("Automation Key"), "PG_CarrierPolicyNumber", policyRefNumber, map_data);
				TestUtil.reportStatus("Policy Referance Number for Carrier : <b>[  "+(String)map_data.get("PG_Carrier")+"  ]</b> is : <b>[  "+policyRefNumber+"  ]</b>", "Info", false);
			
			}else{
				String policyRefNumber = (String)common.NB_excel_data_map.get("PG_CarrierPolicyNumber");
				TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Policy General", (String)map_data.get("Automation Key"), "PG_CarrierPolicyNumber", policyRefNumber, map_data);
				TestUtil.reportStatus("Policy Referance Number for Carrier : <b>[  "+(String)map_data.get("PG_Carrier")+"  ]</b> is : <b>[  "+policyRefNumber+"  ]</b>", "Info", false);
			}
		}
			
		customAssert.assertTrue(k.DropDownSelection("POF_PG_SuppressPremiumFromDocumentation", (String)map_data.get("PG_Rewind_SuppressPremiumFromDocumentation")), "Unable to select Suppress Premium From Documentation value from dropdown field.");
		customAssert.assertTrue(k.DropDownSelection("POF_PG_SuppressDocumentation", (String)map_data.get("PG_Rewind_SuppressDocumentation")), "Unable to select Suppress Documentation value from dropdown field.");
		
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
			
			for(String s_TradeCode : TradeCodes){
	 			driver.findElement(By.name("multiTrade_"+s_TradeCode)).click();
	 		}
			
			common.funcButtonSelection("Save");
			common.funcButtonSelection("exit (Save First)");
	 		
	 		return true;
	 		
		}catch(Throwable t){
			return false;
		}
}

public void MTAFlow(String code,String fileName) throws ErrorInTestMethod{
	String testName = (String)common.MTA_excel_data_map.get("Automation Key");
	try{
		if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
			NewBusinessFlow(code,"NB");
		}
		common.currentRunningFlow="MTA";
		
		String navigationBy = CONFIG.getProperty("NavigationBy");
			
		customAssert.assertTrue(funcCreateEndorsement(),"Error in Create Endorsement function . ");
		
		customAssert.assertTrue(funcPolicyGeneral_MTA(common.MTA_excel_data_map,"POF","MTA"),"Error in Policy General for MTA function.");
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Cover"),"Issue while Navigating to Covers . ");
		customAssert.assertTrue(funcUpdateCoverDetails_MTA(common.MTA_excel_data_map),"Error in selecting cover for MTA.");
	
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcPreviousClaims(common.MTA_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Claims Experience"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcClaimsExperience(common.MTA_excel_data_map), "Previous claim function is having issue(S) .");
	
		if(((String)common.MTA_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Damage"),"Issue while Navigating to Material Damage screen.");
			customAssert.assertTrue(MaterialDamagePage_MTA(common.MTA_excel_data_map,common.MTA_Structure_of_InnerPagesMaps), "MTA Material Damage function is having issue(S) . ");
		}
		if(((String)common.MTA_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Business Interruption"),"Issue while Navigating to Business Interruption screen.");
			customAssert.assertTrue(BusinessInterruptionPage(common.MTA_excel_data_map), "Business InterruptionPage function is having issue(S) . ");
		}
		if(((String)common.MTA_excel_data_map.get("CD_Liabilities-POL")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Property Owners Liabilities"),"Issue while Navigating to Properties Owners Liabilities screen.");
			customAssert.assertTrue(PropertyOwnersLiabilityPage(common.MTA_excel_data_map), "PropertyOwnersLiabilityPage function is having issue(S) . ");
		}
		
		if(((String)common.MTA_excel_data_map.get("CD_Terrorism")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Terrorism"),"Issue while Navigating to Terrorism screen.");
			customAssert.assertTrue(TerrorismPage(common.MTA_excel_data_map), "TerrorismPage function is having issue(S) . ");
			
		}
		if(((String)common.MTA_excel_data_map.get("CD_BespokeCover")).equals("Yes")){	
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Bespoke Cover"),"Issue while Navigating to Bespoke Cover screen . ");
			customAssert.assertTrue(funcBespokeCover(common.MTA_excel_data_map), "Bespoke Cover function is having issue(S) . ");
		}
		
		if(((String)common.MTA_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Excess"),"Issue while Navigating to Excess screen.");
			customAssert.assertTrue(func_Verify_Excesses(common.MTA_excel_data_map,common.MTA_Structure_of_InnerPagesMaps), "Verify Excess function is having issue(S) . ");
		}
		
		if(!TestBase.businessEvent.equalsIgnoreCase("Renewal")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
			customAssert.assertTrue(common.funcEndorsementOperations(common.MTA_excel_data_map),"Endorsement Operations is having issue(S).");
		}
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common_Zennor.funcPremiumSummary_MTA(common.MTA_excel_data_map,code,"MTA"), "Premium Summary function is having issue(S) . ");
		if(!TestBase.businessEvent.equalsIgnoreCase("Renewal")){
			Assert.assertTrue(common_Zennor.funcStatusHandling(common.MTA_excel_data_map,code,"MTA"));
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


public void RenewalFlow(String code,String fileName) throws ErrorInTestMethod{
	String testName = (String)common.Renewal_excel_data_map.get("Automation Key");
	try{
		
		common.currentRunningFlow="Renewal";
		String navigationBy = CONFIG.getProperty("NavigationBy");
		
		customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Pending"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
		customAssert.assertTrue(common.funcAssignUnderWriter((String)common.Renewal_excel_data_map.get("Renewal_AssignUnderwritter")));
		customAssert.assertTrue(common.funcButtonSelection("Send to Underwriter"));
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Submitted"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
		String QuoteNumber = k.getText("POF_QuoteNumber");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Renewal", testName, "Renewal_QuoteNumber", QuoteNumber, common.Renewal_excel_data_map);
		customAssert.assertTrue(funcPolicyGeneral_MTA(common.Renewal_excel_data_map,"POF","Renewal"),"Error in Policy General for MTA function.");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Cover"),"Issue while Navigating to Covers . ");
		customAssert.assertTrue(funcUpdateCoverDetails_MTA(common.Renewal_excel_data_map),"Error in selecting cover for MTA.");
	
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcPreviousClaims(common.Renewal_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Claims Experience"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcClaimsExperience(common.Renewal_excel_data_map), "Previous claim function is having issue(S) .");
	
		if(((String)common.Renewal_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Damage"),"Issue while Navigating to Material Damage screen.");
			customAssert.assertTrue(MaterialDamagePage_MTA(common.Renewal_excel_data_map,common.Renewal_Structure_of_InnerPagesMaps), "MTA Material Damage function is having issue(S) . ");
		}
		if(((String)common.Renewal_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Business Interruption"),"Issue while Navigating to Business Interruption screen.");
			customAssert.assertTrue(BusinessInterruptionPage(common.Renewal_excel_data_map), "Business InterruptionPage function is having issue(S) . ");
		}
		if(((String)common.Renewal_excel_data_map.get("CD_Liabilities-POL")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Property Owners Liabilities"),"Issue while Navigating to Properties Owners Liabilities screen.");
			customAssert.assertTrue(PropertyOwnersLiabilityPage(common.Renewal_excel_data_map), "PropertyOwnersLiabilityPage function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_Terrorism")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Terrorism"),"Issue while Navigating to Terrorism screen.");
			customAssert.assertTrue(TerrorismPage(common.Renewal_excel_data_map), "TerrorismPage function is having issue(S) . ");
			
		}
		if(((String)common.Renewal_excel_data_map.get("CD_BespokeCover")).equals("Yes")){	
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Bespoke Cover"),"Issue while Navigating to Bespoke Cover screen . ");
			customAssert.assertTrue(funcBespokeCover(common.Renewal_excel_data_map), "Bespoke Cover function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Excess"),"Issue while Navigating to Excess screen.");
			customAssert.assertTrue(func_Verify_Excesses(common.Renewal_excel_data_map,common.Renewal_Structure_of_InnerPagesMaps), "Verify Excess function is having issue(S) . ");
		}
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
		customAssert.assertTrue(common.deleteEndorsement(), "Unable to delete endorsement from Endorsement screen.");
		customAssert.assertTrue(common.funcEndorsementOperations(common.Renewal_excel_data_map),"Endorsement Operations is having issue(S).");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common_Zennor.funcPremiumSummary(common.Renewal_excel_data_map,code,"Renewal"), "Premium Summary function is having issue(S) . ");
		Assert.assertTrue(common_Zennor.funcStatusHandling(common.Renewal_excel_data_map,code,"Renewal"));
	
		customAssert.assertEquals(err_count,0,"Errors in premium calculations . ");
		customAssert.assertEquals(trans_error_val,0,"Errors in Transaction premium calculations . ");
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


/**
 * 
 * This method handles all Cancellation Cases for COB product.
 * 
 */
public void CancellationFlow(String code,String fileName) throws ErrorInTestMethod{
	
	String testName = (String)common.CAN_excel_data_map.get("Automation Key");
	try{
		
		NewBusinessFlow(code,"NB");
		common_PIA.currentFlow = "CAN";
		common.currentRunningFlow="CAN";
		//System.out.println("Test method of CAN For - "+code);
		
		customAssert.assertTrue(POF_CancelPolicy(common.CAN_excel_data_map), "Error while Cancelling policy.");
		customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
		
		customAssert.assertTrue(common_POF.POF_Cancel_PremiumSummary(common.CAN_excel_data_map), "Issue in verifying Premium summary for cancellation");
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""), "Unable to click on policies tab");
		Assert.assertTrue(common_Zennor.funcStatusHandling(common.NB_excel_data_map,code,fileName));
		
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

public boolean POF_CancelPolicy(Map<Object, Object> map_data) throws ErrorInTestMethod{
    boolean retVal = true;
    int dateDif = Integer.parseInt((String)map_data.get("CP_AddDifference"));
    //String Cancellation_date = common.daysIncrementWithOutFormation(df.format(currentDate), dateDif); 
    try{
    		String testName = (String)common.CAN_excel_data_map.get("Automation Key");
    		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    		//UK time zone change 
    		Date c_date = df.parse(common.getUKDate());
    		String Cancellation_date = common.daysIncrementWithOutFormation(df.format(c_date), dateDif);
    
           customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"), "Unable to navigate to Premium Summary screen");
           customAssert.assertTrue(k.Click("SPI_Btn_CancelPolicy"), "Unable to click on Cancel Policy Button");
                        
           customAssert.assertTrue(k.Click("SPI_CP_CancellationDate"), "Unable to enter Cancellation date.");
           customAssert.assertTrue(k.Input("SPI_CP_CancellationDate", Cancellation_date),"Unable to Enter Cancellation date.");
           customAssert.assertTrue(k.Click("SPI_Btn_Calender_Done"), "Unable to click on done button in calander.");
           customAssert.assertTrue(!k.getAttributeIsEmpty("SPI_CP_CancellationDate", "value"),"CancellationDate Field Should Contain Valid Value on Cancel Policy page .");
           customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Cancel Policy", testName, "CP_CancellationDate", Cancellation_date,common.CAN_excel_data_map),"Error while writing data to excel for field >Cancellation_date<");
			
           customAssert.assertTrue(k.Input("SPI_CP_CancellationReason", (String)map_data.get("CP_CancellationReason")),"Unable to Enter Cancellation Reason.");
           customAssert.assertTrue(k.Click("SPI_Btn_Continue"), "Unable to click on Continue Button on Cancel Policy Screen");
                        
           // Read Cancellation Return Premium Summary and put values to Map  :
           
           customAssert.assertTrue(POF_Cancel_RetrunPremiumTable(map_data), "Issue in Verifying Cancellation Return Premium");
           
           customAssert.assertTrue(k.Click("SPI_Btn_FinalCancelPolicy"), "Unable to click on Cancel Polcy button after verifying Cancellation Return Premium");
           customAssert.assertTrue(k.AcceptPopup(), "Unable to handle pop up");
                  
           return retVal;
           
    
    }catch(Throwable t){ 
          return false;
    }
           
}

public boolean POF_Cancel_PremiumSummary(Map<Object, Object> map_data) throws ErrorInTestMethod{
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
					sectionName = sectionName.replaceAll(" ", "");
					
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
					
					CommonFunction.compareValues(s_NetNetP, common_Zennor.Can_ReturnP_Values_Map.get(sectionName).get("Net Net Premium"), "Cancellation Net Net Premium");
					
					if(!sectionName.equals("Totals")){
						CommonFunction.compareValues(s_PenCommRate,common_Zennor.Can_ReturnP_Values_Map.get(sectionName).get("Pen Comm %"), "Cancellation Pen Comm Percentage");
					}				
					CommonFunction.compareValues(s_PenCommAmt, common_Zennor.Can_ReturnP_Values_Map.get(sectionName).get("Pen Comm"), "Cancellation Pen commm Amount");
					CommonFunction.compareValues(s_NetP, common_Zennor.Can_ReturnP_Values_Map.get(sectionName).get("Net Premium"), "Cancellation Net Premium");
					
					if(!sectionName.equals("Totals")){
						CommonFunction.compareValues(s_BrokerCommRate, common_Zennor.Can_ReturnP_Values_Map.get(sectionName).get("Broker Comm %"), "Cancellation Broker Comm rate");
					}
					
					CommonFunction.compareValues(s_BrokerCommAmt, common_Zennor.Can_ReturnP_Values_Map.get(sectionName).get("Broker Commission"), "Cancellation Broker commission amount");
					
					if(!sectionName.equals("Totals")){
						CommonFunction.compareValues(s_GrossCommRate, common_Zennor.Can_ReturnP_Values_Map.get(sectionName).get("Gross Comm %"), "Cancellation Gross comm rate");
					}
					
					CommonFunction.compareValues(s_GrossCommAmt, common_Zennor.Can_ReturnP_Values_Map.get(sectionName).get("Gross Premium"), "Cancellation Gross Premium");
					
					if(!sectionName.equals("Totals")){
						CommonFunction.compareValues(s_InsTRate, common_Zennor.Can_ReturnP_Values_Map.get(sectionName).get("Insurance Tax Rate"), "Cancellation Ins tax rate");
					}
					
					CommonFunction.compareValues(s_InsTAmt, common_Zennor.Can_ReturnP_Values_Map.get(sectionName).get("Insurance Tax"), "Cancellation Ins tax Premium");
					CommonFunction.compareValues(s_TotalP, common_Zennor.Can_ReturnP_Values_Map.get(sectionName).get("Total Premium"), "Cancellation Total Premium");
										
				}	
			}
				
			return retVal;		
	
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}


public boolean POF_Cancel_RetrunPremiumTable(Map<Object, Object> map_data) throws ErrorInTestMethod{
	boolean retVal = true;
	String testName = (String)common.NB_excel_data_map.get("Automation Key");
	
	try{
					
		int policy_Duration = Integer.parseInt(k.getText("SPI_Can_Duration"));
		int DaysRemain = Integer.parseInt(k.getText("SPI_Can_DaysRemain"));
		
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
					sectionName = sec_Name.getText().replaceAll(" ", "");
										
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
										common_Zennor.Can_ReturnP_Values_Map.put(sectionName, ReturnP_Table_TotalVal);
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
								
								common_Zennor.Can_ReturnP_Values_Map.put(sectionName, ReturnP_Table_CoverVal);
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
						Can_returnP_Error = Can_returnP_Error + Can_ReturnP_Total_Validation(sectionNames,common_Zennor.Can_ReturnP_Values_Map);
					else
						Can_returnP_Error = Can_returnP_Error + CanReturnPTable_CoverSection_Validation(policy_Duration,DaysRemain, s_Name,common_Zennor.Can_ReturnP_Values_Map);								
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


public boolean COB_Cancel_RetrunPremiumTable(Map<Object, Object> map_data) throws ErrorInTestMethod{
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
										common_COB.CAN_COB_ReturnP_Values_Map.put(sectionName, ReturnP_Table_TotalVal);
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
								
								common_COB.CAN_COB_ReturnP_Values_Map.put(sectionName, ReturnP_Table_CoverVal);
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
						Can_returnP_Error = Can_returnP_Error + Can_ReturnP_Total_Validation(sectionNames,common_COB.CAN_COB_ReturnP_Values_Map);
					else
						Can_returnP_Error = Can_returnP_Error + CanReturnPTable_CoverSection_Validation(policy_Duration,DaysRemain, s_Name, common_COB.CAN_COB_ReturnP_Values_Map);								
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
				section = section.replaceAll(" ", "");
				if(!section.equalsIgnoreCase("Totals")){
					exp_value = exp_value + common_Zennor.Can_ReturnP_Values_Map.get(section).get("Net Net Premium");
				}
			}
			
			String canRP_NetNetP_actual = Double.toString(common_Zennor.Can_ReturnP_Values_Map.get("Totals").get("Net Net Premium"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_NetNetP_actual)," Net Net Premium");

			exp_value = 0.0;
			for(String section : sectionNames){
				section = section.replaceAll(" ", "");
				if(!section.contains("Total"))
					exp_value = exp_value + common_Zennor.Can_ReturnP_Values_Map.get(section).get("Pen Comm");
			}
			String canRP_pc_actual = Double.toString(common_Zennor.Can_ReturnP_Values_Map.get("Totals").get("Pen Comm"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_pc_actual)," Pen Commission");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				section = section.replaceAll(" ", "");
				if(!section.contains("Total"))
					exp_value = exp_value + common_Zennor.Can_ReturnP_Values_Map.get(section).get("Net Premium");
			}
			String canRP_netP_actual = Double.toString(common_Zennor.Can_ReturnP_Values_Map.get("Totals").get("Net Premium"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_netP_actual),"Net Premium");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				section = section.replaceAll(" ", "");
				if(!section.contains("Total"))
					exp_value = exp_value + common_Zennor.Can_ReturnP_Values_Map.get(section).get("Broker Commission");
			}
			String canRP_bc_actual =  Double.toString(common_Zennor.Can_ReturnP_Values_Map.get("Totals").get("Broker Commission"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_bc_actual),"Broker Commission");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				section = section.replaceAll(" ", "");
				if(!section.contains("Total"))
					exp_value = exp_value + common_Zennor.Can_ReturnP_Values_Map.get(section).get("Gross Premium");
			}
			String canRP_grossP_actual = Double.toString(common_Zennor.Can_ReturnP_Values_Map.get("Totals").get("Gross Premium"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_grossP_actual)," Gross Premium");
		
			exp_value = 0.0;
			for(String section : sectionNames){
				section = section.replaceAll(" ", "");
				if(!section.contains("Total"))
					exp_value = exp_value + common_Zennor.Can_ReturnP_Values_Map.get(section).get("Insurance Tax");
			}
			String canRP_InsuranceTax_actual = Double.toString(common_Zennor.Can_ReturnP_Values_Map.get("Totals").get("Insurance Tax"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_InsuranceTax_actual),"Insurance Tax");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				section = section.replaceAll(" ", "");
				if(!section.contains("Total"))
					exp_value = exp_value + common_Zennor.Can_ReturnP_Values_Map.get(section).get("Total Premium");
			}
			String canRP_p_actual = Double.toString(common_Zennor.Can_ReturnP_Values_Map.get("Totals").get("Total Premium"));
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
	sectionNames = sectionNames.replaceAll(" ", "");
		
	try{
			
			TestUtil.reportStatus("---------------"+sectionNames+"-----------------","Info",false);
			
			if(code.contains("LegalExpenses")){
				code = "LegalExpenses";
			}
			double annual_NetNetP = Double.parseDouble((String)map_data.get("PS_"+code+"_NetNetPremium"));
			String canRP_NetNetP_expected = Double.toString((annual_NetNetP/365)*DaysRemain);
			String canRP_NetNetP_actual = Double.toString(common_Zennor.Can_ReturnP_Values_Map.get(sectionNames).get("Net Net Premium"));
			CommonFunction.compareValues(Double.parseDouble(canRP_NetNetP_expected),Double.parseDouble(canRP_NetNetP_actual)," Net Net Premium");
			
			//COB CAN Transaction Pen commission Calculation : 
			double canRP_pen_comm = (( Double.parseDouble(canRP_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate"))/100)));
			String canRP_pc_expected = common.roundedOff(Double.toString(canRP_pen_comm));
			String canRP_pc_actual = Double.toString(common_Zennor.Can_ReturnP_Values_Map.get(sectionNames).get("Pen Comm"));
			CommonFunction.compareValues(Double.parseDouble(canRP_pc_expected),Double.parseDouble(canRP_pc_actual)," Pen Commission");
			
			
			//COB CAN Transaction Net Premium verification : 
			double canRP_netP = Double.parseDouble(canRP_pc_expected) + Double.parseDouble(canRP_NetNetP_expected);
			String canRP_netP_expected = common.roundedOff(Double.toString(canRP_netP));
			String canRP_netP_actual = Double.toString(common_Zennor.Can_ReturnP_Values_Map.get(sectionNames).get("Net Premium"));
			CommonFunction.compareValues(Double.parseDouble(canRP_netP_expected),Double.parseDouble(canRP_netP_actual),"Net Premium");
			
			
			//COB CAN Transaction Broker commission Calculation : 
			double canRP_broker_comm = ((Double.parseDouble(canRP_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate"))/100)));
			String canRP_bc_expected = common.roundedOff(Double.toString(canRP_broker_comm));
			String canRP_bc_actual =  Double.toString(common_Zennor.Can_ReturnP_Values_Map.get(sectionNames).get("Broker Commission"));
			CommonFunction.compareValues(Double.parseDouble(canRP_bc_expected),Double.parseDouble(canRP_bc_actual),"Broker Commission");
			
			
			//COB CAN Transaction GrossPremium verification : 
			double canRP_grossP = Double.parseDouble(canRP_netP_expected) + Double.parseDouble(canRP_bc_expected);
			String canRP_grossP_actual = Double.toString(common_Zennor.Can_ReturnP_Values_Map.get(sectionNames).get("Gross Premium"));
			CommonFunction.compareValues(canRP_grossP,Double.parseDouble(canRP_grossP_actual),sectionNames+" Transaction Gross Premium");
			
			
			double canRP_InsuranceTax = (canRP_grossP * Double.parseDouble(common.roundedOff((String)map_data.get("PS_"+code+"_IPT"))))/100.0;
			canRP_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(canRP_InsuranceTax)));
			String canRP_InsuranceTax_actual = Double.toString(common_Zennor.Can_ReturnP_Values_Map.get(sectionNames).get("Insurance Tax"));
			CommonFunction.compareValues(canRP_InsuranceTax,Double.parseDouble(canRP_InsuranceTax_actual),"Insurance Tax");
						
			//COB CAN  Transaction Total Premium verification : 
			double canRP_Premium = canRP_grossP + canRP_InsuranceTax;
			String canRP_p_expected = common.roundedOff(Double.toString(canRP_Premium));
			
			String canRP_p_actual = Double.toString(common_Zennor.Can_ReturnP_Values_Map.get(sectionNames).get("Total Premium"));
			
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

public boolean COB_Cancel_PremiumSummary(Map<Object, Object> map_data) throws ErrorInTestMethod{
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
					
					common.compareValues(s_NetNetP, common_COB.CAN_COB_ReturnP_Values_Map.get(sectionName).get("Net Net Premium"), "Cancellation Net Net Premium");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_PenCommRate,common_COB.CAN_COB_ReturnP_Values_Map.get(sectionName).get("Pen Comm %"), "Cancellation Pen Comm Percentage");
					}				
					common.compareValues(s_PenCommAmt, common_COB.CAN_COB_ReturnP_Values_Map.get(sectionName).get("Pen Comm"), "Cancellation Pen commm Amount");
					common.compareValues(s_NetP, common_COB.CAN_COB_ReturnP_Values_Map.get(sectionName).get("Net Premium"), "Cancellation Net Premium");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_BrokerCommRate, common_COB.CAN_COB_ReturnP_Values_Map.get(sectionName).get("Broker Comm %"), "Cancellation Broker Comm rate");
					}
					
					common.compareValues(s_BrokerCommAmt, common_COB.CAN_COB_ReturnP_Values_Map.get(sectionName).get("Broker Commission"), "Cancellation Broker commission amount");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_GrossCommRate, common_COB.CAN_COB_ReturnP_Values_Map.get(sectionName).get("Gross Comm %"), "Cancellation Gross comm rate");
					}
					
					common.compareValues(s_GrossCommAmt, common_COB.CAN_COB_ReturnP_Values_Map.get(sectionName).get("Gross Premium"), "Cancellation Gross Premium");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_InsTRate, common_COB.CAN_COB_ReturnP_Values_Map.get(sectionName).get("Insurance Tax Rate"), "Cancellation Ins tax rate");
					}
					
					common.compareValues(s_InsTAmt, common_COB.CAN_COB_ReturnP_Values_Map.get(sectionName).get("Insurance Tax"), "Cancellation Ins tax Premium");
					common.compareValues(s_TotalP, common_COB.CAN_COB_ReturnP_Values_Map.get(sectionName).get("Total Premium"), "Cancellation Total Premium");
										
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
		case "Renewal":
			data_map = common.Renewal_excel_data_map;
			break;
	}
	
	
	int currentYear = Integer.parseInt(PLEL_Rater.getProperty("CurrentYear"));
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
		System.out.println("Error while Calculating Bona Fide book rate - "+t);
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
	
	//System.out.println("Total Loads = "+total_PL_Loads);
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
	//System.out.println("Total MF = "+multiplying_Factor);
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
	
	//System.out.println("Book Rate of "+trade_Activity + " = "+book_rate);
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
	
	//System.out.println("Book Rate of "+trade_Activity + " = "+book_rate);
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
//**** COB Book Rate Calculator Based on CPRS Sheet ********//
//************************************************************//
//************************************************************//





/***
 * ------------------------------------------------------------------------
 * Rewind Reused Function for COB product.
 * ------------------------------------------------------------------------
 */

public boolean PublicProductsLiabilityPage_Rewind(Map<Object, Object> map_data){
	boolean retValue = true;
	String abvr = null, coverName = null;
	String s_Sheet =null;
	
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Public & Products Liability", ""),"Public & Products Liability page is having issue(S)");
		 
		 customAssert.assertTrue(k.SelectRadioBtn("COB_PPL_BonaFide", (String)map_data.get("PPL_Rewind_BonaFide")),"Unable to put value in BonaFide field");
		 String b_Val =(String)map_data.get("PPL_BonaFide");
		 
		 if(b_Val.contains("Yes")){
			customAssert.assertTrue(k.Input("COB_PPL_BFSC_Payments", (String)map_data.get("PPL_Rewind_BFSC_Payments")),"Unable to put value in BFSC_Payments field");
			customAssert.assertTrue(k.Input("COB_PPL_BF_PecentTotalWork", (String)map_data.get("PPL_Rewind_BF_PecentTotalWork")),"Unable to put value in PecentTotalWork field");
			 
			 customAssert.assertTrue(k.SelectRadioBtn("COB_PPL_BF_WrittenRecord", (String)map_data.get("PPL_Rewind_BF_WrittenRecord")),"Unable to put value in WrittenRecord field");
			 //referral code
			 if(((String)map_data.get("PPL_Rewind_BF_WrittenRecord")).equalsIgnoreCase("No"))
				 common_VELA.referrals_list.add((String)map_data.get("RM_Rewind_Public&ProductsLiability_NoWrittenRecord"));
		 }
		 
		 String[] rist_mgmt = ((String)common.NB_excel_data_map.get("PPL_Rewind_PL_RiskManagement")).split(",");
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
		 
		 customAssert.assertTrue(k.Input("COB_PPL_DescribeProducts", (String)map_data.get("PPL_Rewind_DescribeProducts")),"Unable to put value in DescribeProducts field");
		 customAssert.assertTrue(k.Input("COB_PPL_DeclarationCondition", (String)map_data.get("PPL_Rewind_DeclarationCondition")),"Unable to put value in DeclarationCondition field");
		 customAssert.assertTrue(k.DropDownSelection("COB_PPL_LOI_PPLiability", (String)map_data.get("PPL_Rewind_LOI_PPLiability")),"Unable to put value in LOI field");
		 customAssert.assertTrue(k.DropDownSelection("COB_PPL_TypeOfLimit", (String)map_data.get("PPL_Rewind_TypeOfLimit")),"Unable to put value in TypeOfLimit field");
		
		 // Add Edit Wages Breakdown :
		 
		 String sValue = (String)map_data.get("PPL_Rewind_AddEditWagesBreakdown");
		 
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


public boolean EmployersLiabilityPage_Rewind(Map<Object, Object> map_data){
	boolean retValue = true;
	String abvr = null, coverName = null;
	String s_Sheet =null;
	
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Employers Liability", ""),"Employers Liability page is having issue(S)");
		 
		 customAssert.assertTrue(k.Input("COB_EL_DeclarationCondition", (String)map_data.get("EL_DeclarationCondition")),"Unable to put value in DeclarationCondition field");
		 customAssert.assertTrue(k.DropDownSelection("COB_EL_LOI", (String)map_data.get("EL_LOI")),"Unable to put value in LOI field");
		// customAssert.assertTrue(k.DropDownSelection("COB_EL_TypeOfLimit", (String)map_data.get("EL_TypeOfLimit")),"Unable to put value in TypeOfLimit field");
		
		 /*// Add Edit Wages Breakdown :
		 
		 String sValue = (String)map_data.get("EL_AddEditWagesBreakdown");
		 
		 if(sValue.contains("Yes")){
			 coverName = "Public & Products Liability";
			 abvr = "PPL";
			 customAssert.assertTrue(k.Click("COB_Btn_AddWagesBreakdown"),"Unable to click on AddWagesBreakdown button");
			 customAssert.assertTrue(AddWages(map_data, coverName, abvr),"");
		 }
		 */
		 
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

public boolean SingleProjectPage_Rewind(Map<Object, Object> map_data){
	boolean retValue = true;
	String abvr = null, coverName = null;
	String s_Sheet =null;
	
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Single Project", ""),"Single Project page is having issue(S)");
		 
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



public boolean AnnualWorkPage_Rewind(Map<Object, Object> map_data){
	boolean retValue = true;
	String abvr = null, coverName = null;
	String s_Sheet =null;
	
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Annual Works", ""),"Annual Works page is having issue(S)");
		 
		 String[] rist_mgmt = ((String)common.NB_excel_data_map.get("AW_InterestsInsured")).split(",");
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
		 customAssert.assertTrue(k.Input("COB_AW_SumInsured_ContractValue", (String)map_data.get("AW_SumInsured_ContractValue")),"Unable to put value in SumInsured_ContractValue field");
		 customAssert.assertTrue(k.Input("COB_AW_DeclarationCondition", (String)map_data.get("AW_DeclarationCondition")),"Unable to put value in DeclarationCondition field");
		 customAssert.assertTrue(k.Input("COB_AW_LOI", (String)map_data.get("AW_LOI")),"Unable to put value in LOI field");
		 
		// customAssert.assertTrue(k.DropDownSelection("COB_AW_TypeOfLimit", (String)map_data.get("AW_TypeOfLimit")),"Unable to put value in TypeOfLimit field");
		 
		 
		 
		 /*// Add Edit Wages Breakdown :
		 
		 String sValue = (String)map_data.get("EL_AddEditWagesBreakdown");
		 
		 if(sValue.contains("Yes")){
			 coverName = "Public & Products Liability";
			 abvr = "PPL";
			 customAssert.assertTrue(k.Click("COB_Btn_AddWagesBreakdown"),"Unable to click on AddWagesBreakdown button");
			 customAssert.assertTrue(AddWages(map_data, coverName, abvr),"");
		 }
		 */
		 
		 
		 /*String sValue = (String)map_data.get("AwAddContract_01;");
		 if(sValue.length() > 0){
			 abvr = "AW_AD_";
			 coverName = "AW";
			 customAssert.assertTrue(funcAddContract(map_data, sValue, abvr,coverName),"Unable to add contact for AW cover");
		 }*/
			 
		 
		String sVal = (String)map_data.get("AW_AddBespoke");
		 if(sVal.length() > 0){
			 abvr = "AW_AddB_";
			 coverName = "AW";
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

public boolean HireCoverPlusPage_Rewind(Map<Object, Object> map_data){
	boolean retValue = true;
	String abvr = null;
	
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Hire Cover Plus", ""),"Hire Cover Plus page is having issue(S)");
				
		 customAssert.assertTrue(k.Input("COB_HCP_HireWithWaiver", (String)map_data.get("HCP_HireWithWaiver")),"Unable to put value in CurrentMarketValue field");
		 customAssert.assertTrue(k.Input("COB_HCP_HireWithoutWaiver", (String)map_data.get("HCP_HireWithoutWaiver")),"Unable to put value in CurrentMarketValue field");
		 
		 k.Click("COB_HCP_Total");
		 String s_Total = k.getAttribute("COB_HCP_Total","value");
		 CommonFunction.compareValues(Double.parseDouble((String)map_data.get("HCP_Total")), Double.parseDouble(s_Total),"Total Turnover % for Hire Cover Plus cover ");
		
		 customAssert.assertTrue(k.Input("COB_HCP_OPIL_AnyOneOccurrence", (String)map_data.get("HCP_OPIL_AnyOneOccurrence")),"Unable to put value in IL_AnyOneOccurrence field");
		 customAssert.assertTrue(k.Input("COB_HCP_OPIL_AnyOneItem", (String)map_data.get("HCP_OPIL_AnyOneItem")),"Unable to put value in IL_AnyOneItem field");
		 customAssert.assertTrue(k.Input("COB_HCP_NPHP_IndemnityLimit", (String)map_data.get("HCP_IL_Premises")),"Unable to put value in IL_Premises field");
		 
		 customAssert.assertTrue(k.Input("COB_HCP_HPIL_AnyOneOccurrence", (String)map_data.get("HCP_HPIL_AnyOneOccurrence")),"Unable to put value in HPIL_AnyOneOccurrence field");
		 customAssert.assertTrue(k.Input("COB_HCP_HPIL_AnyOneItem", (String)map_data.get("HCP_HPIL_AnyOneItem")),"Unable to put value in HPIL_AnyOneItem field");
		 
		 customAssert.assertTrue(k.Input("COB_HCP_CHCIL_AnyOneOccurrence", (String)map_data.get("HCP_CHCIL_AnyOneOccurrence")),"Unable to put value in CHCIL_AnyOneOccurrence field");
		 customAssert.assertTrue(k.Input("COB_HCP_CHCIL_AnyOneItem", (String)map_data.get("HCP_CHCIL_AnyOneItem")),"Unable to put value in CHCIL_AnyOneItem field");
		 
		 String sVal = (String)map_data.get("HCP_AddBespoke");
		 if(sVal.length() > 0){
			 abvr = "HCP_AddB_";
			 customAssert.assertTrue(funcAddBespoke(map_data, sVal, abvr,"HCP"),"Unable to add bspoke item for HCP cover");
		 }
		 
		 // Click on apply book rates button :
		 
		 customAssert.assertTrue(k.Click("COB_HCP_CalculatePremium"),"Unable to click on CalculatePremium button");
		 
		 // Get table id :
		 
		 	String sUniqueCol ="Tech. Adjust (%)";
			int tableId = 0;
			
			String sTablePath = "html/body/div[3]/form/div/table";
			
			tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
			sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
			String sCover = "Hire Cover Plus";
			 abvr = "HCP_";
			
			customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, sTablePath, sCover,abvr),"Unable to handle Auto rated premium table on HCP screen");
			
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

public boolean HireInPlantPage_Rewind(Map<Object, Object> map_data){
	boolean retValue = true;
	String abvr = null;
	
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Hired In Plant", ""),"Hired In Plant page is having issue(S)");
		 
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


public boolean OwnPlantsPage_Rewind(Map<Object, Object> map_data){
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

public boolean JCTPage_Rewind(Map<Object, Object> map_data){
	boolean retValue = true;
	String abvr = null;
	String s_Sheet =null;
	
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("JCT 6.5.1", ""),"JCT 6.5.1 page is having issue(S)");
		 
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
@SuppressWarnings("static-access")
public boolean funcLegalExpenses_Rewind(Map<Object, Object> map_data,String code,String event){
	
	boolean r_value= true;
	String abvr = null, coverName = null;
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Legal Expenses", ""),"Legal Expenses page navigations issue(S)");
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
			
		 customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, sTablePath, sCover, abvr),"Unable to handle manual rated premium table on Own Plant screen");
			 
			 
		 
//		int LE_Premium_Decimal_Places = common.countDecimalPlaces(k.getAttribute("CCF_LE_NetPremium", "value"));
//		boolean decimalFlag = LE_Premium_Decimal_Places > 2 ?false:true;
//		customAssert.assertTrue(decimalFlag , "LE Premium Should contain up to two Decimal Places . ");
//		
//		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques1", (String)map_data.get("LE_Ques1")),"Unable to Select LE_Ques1 radio button . ");
//		
//		//Statement of Fact
//		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques1", (String)map_data.get("LE_Ques1")),"Unable to Select LE_Ques1 radio button . ");
//		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques2", (String)map_data.get("LE_Ques2")),"Unable to Select LE_Ques2 radio button . ");
//		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques3", (String)map_data.get("LE_Ques3")),"Unable to Select LE_Ques3 radio button . ");
//		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques4", (String)map_data.get("LE_Ques4")),"Unable to Select LE_Ques4 radio button . ");
//		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques5", (String)map_data.get("LE_Ques5")),"Unable to Select LE_Ques5 radio button . ");
//		customAssert.assertTrue(k.SelectRadioBtn("CCF_LE_Ques6", (String)map_data.get("LE_Ques6")),"Unable to Select LE_Ques6 radio button . ");
//				
//		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Legal Expenses .");
//		
//		exp_AnnualCarrierPremium = common.roundedOff(common.getLEAnnualCarrierPremium(common.product,(String)map_data.get("LE_LimitOfLiability"), (String)map_data.get("LE_Turnover"), (String)map_data.get("LE_Wages")));
//		act_AnnualCarrierPremium = k.getAttribute("CCF_LE_AnnualCarrierPremium","value");
//		customAssert.assertEquals(exp_AnnualCarrierPremium, act_AnnualCarrierPremium,"Annual Carrier Premium (Excludes IPT) is incorrect Exppected: <b>"+exp_AnnualCarrierPremium+"</b> Actual: <b>"+act_AnnualCarrierPremium+" for Legal Expense . ");
//		customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Legal Expenses", testName, "LE_AnnualCarrierPremium", exp_AnnualCarrierPremium,common.NB_excel_data_map),"Error while writing data to excel for field >NB_ClientId<");
		
		
		TestUtil.reportStatus("Legal Expenses details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean PropertyOwnersLiabilityPage_Rewind(Map<Object, Object> map_data){
	boolean retValue = true;
	String abvr = null, coverName = null;
	String s_Sheet =null;
	
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Property Owners Liability", ""),"Property Owners Liability page is having issue(S)");
		 
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


public boolean TerrorismPage_Rewind(Map<Object, Object> map_data){
	boolean retValue = true;
	String abvr = null, coverName = null;
	String s_Sheet =null;
	
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Terrorism", ""),"Terrorism page is having issue(S)");
		 
		
		 customAssert.assertTrue(k.Input("COB_Ter_Turnover_Zone_A", (String)map_data.get("Ter_Turnover_Zone_A")),"Unable to put value in Zone_A field");
		 customAssert.assertTrue(k.Input("COB_Ter_Turnover_Zone_B", (String)map_data.get("Ter_Turnover_Zone_B")),"Unable to put value in Zone_B field");
		 customAssert.assertTrue(k.Input("COB_Ter_Turnover_Zone_C", (String)map_data.get("Ter_Turnover_Zone_C")),"Unable to put value in Zone_C field");
		 customAssert.assertTrue(k.Input("COB_Ter_Turnover_Zone_D", (String)map_data.get("Ter_Turnover_Zone_D")),"Unable to put value in Zone_D field");
		 
		 k.Click("COB_Ter_Turnover_Total");	
		 String s_TotalTurnover = k.getAttribute("COB_Ter_Turnover_Total", "value");
		 CommonFunction.compareValues(Double.parseDouble((String)map_data.get("Ter_Turnover_Total")), Double.parseDouble(s_TotalTurnover),"Total Turnover % for terrorism ");
				 
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

/***
 * ------------------------------------------------------------------------
 * End ---- Rewind Reused Function for COB product.
 * ------------------------------------------------------------------------
 */

/**
 * 
 * This method handles POF Excesses screens scripting.
 * 
 */
@SuppressWarnings("null")
public boolean func_Verify_Excesses(Map<Object, Object> map_data,Map<String, List<Map<String, String>>> internal_data_map){
	
	boolean retvalue = true;
	boolean isExcessCap=false,isExcessDeductible=false;
	Map<String,List<String>> property_ExcessType = new HashMap<>();
	List<String> excess_types = null;
	try{
		customAssert.assertTrue(common.funcPageNavigation("Excesses", ""), "Navigation Problem to Excesses page .");
		
		String[] properties = ((String)map_data.get("EXS_Properties")).split(";");
		int no_of_property = properties.length;
		int[] total_ExcessTypes = new int[no_of_property];
		
		if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
        	
        	boolean isNotStale=true;
    		
        	k.ImplicitWaitOff();
    		while(isNotStale){
    			try{
    				List<WebElement> delete_Btns = driver.findElements(By.xpath("//*[contains(@id,'_excesses_table')]//a[text()='Delete']"));
    				for(WebElement element: delete_Btns){
    					if(element.isDisplayed())
    						element.click();
    					else
    						continue;
    				}
    				isNotStale=false;
    			}catch(Throwable t){
    				continue;
    			}
    			}
    		k.ImplicitWaitOn();
    		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Excesses page after deleting excess entries .");	
        }
        k.ImplicitWaitOn();
		
		
		
		
		List<WebElement> elm = null;
		String[] excessTypes =null;
		
		for(int count=1;count<=no_of_property;count++){
			
			WebElement add_row = driver.findElement(By.xpath("//*[contains(@id,'_excesses_table')]//a[text()='Add Row']"));
			add_row.click();
			
			
			elm = driver.findElements(By.xpath("//*[contains(@id,'_excesses_table')]//*//select[contains(@name,'_ex_property')]"));
			
			//Verify Options available in Property dropdown list
			customAssert.assertTrue(Verify_added_properties(elm.get(count-1),map_data,internal_data_map),"Error while verifying added properties to Excess table . ");
			customAssert.assertTrue(k.DropDownSelection_WebElement(elm.get(count-1), internal_data_map.get("Excess-Property").get(count-1).get("EXS_Property")),"Unable to enter EXS_Property in Excesses table");
			
			
			//For Each Excess Property
			excessTypes = (internal_data_map.get("Excess-Property").get(count-1).get("EXS_ExcessType")).split(":");
			total_ExcessTypes[count-1] = excessTypes.length;
			List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
			for(String ex_types : excessTypes){
				for(WebElement each_ul : ul_elements){
					elm = driver.findElements(By.xpath("//*[contains(@class,'select2-selection__rendered')]"));
					elm.get(count-1).click();
					//customAssert.assertTrue(k.Click("POF_EXS_ExcessType"),"Error while Clicking POF_EXS_ExcessType object . ");
					k.waitTwoSeconds();
					if(each_ul.findElement(By.xpath("//li[text()='"+ex_types+"']")).isDisplayed())
						each_ul.findElement(By.xpath("//li[text()='"+ex_types+"']")).click();
					else
						continue;
					break;
				}
			}
			
			excess_types = Arrays.asList(excessTypes);
			property_ExcessType.put(internal_data_map.get("Excess-Property").get(count-1).get("EXS_Property"), excess_types);
			
			elm = driver.findElements(By.xpath("//*[contains(@name,'ex_description')]"));
			customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("Excess-Property").get(count-1).get("EXS_Description"),"Input"),"Error while entering Description");
			elm = driver.findElements(By.xpath("//*[contains(@name,'ex_value')]"));
			customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("Excess-Property").get(count-1).get("EXS_ExcessValue"),"Input"),"Error while entering EXS_ExcessValue");
			elm = driver.findElements(By.xpath("//*[contains(@name,'_ex_applies')]"));
			customAssert.assertTrue(k.DropDownSelection_WebElement(elm.get(count-1), internal_data_map.get("Excess-Property").get(count-1).get("EXS_ExcessApplies")),"Unable to enter EXS_ExcessApplies in Excesses table");
			elm = driver.findElements(By.xpath("//*[contains(@name,'_ex_max_number')]"));
			customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(count-1),internal_data_map.get("Excess-Property").get(count-1).get("EXS_MaximumNumberOfExcesses"),"Input"),"Error while entering EXS_MaximumNumberOfExcesses");
			elm = driver.findElements(By.xpath("//*[contains(@name,'ex_aggregate_ex_cap')]"));
			customAssert.assertTrue(k.DropDownSelection_WebElement(elm.get(count-1), internal_data_map.get("Excess-Property").get(count-1).get("EXS_AggregateExcessCap")),"Unable to enter EXS_AggregateExcessCap in Excesses table");
			if(!isExcessCap){
				isExcessCap = internal_data_map.get("Excess-Property").get(count-1).get("EXS_AggregateExcessCap").equalsIgnoreCase("Yes") ? true:false;
			}
			
			
			elm = driver.findElements(By.xpath("//*[contains(@name,'_ex_aggregate_deductible_cap')]"));
			k.ScrollInVewWebElement(elm.get(count-1));
			customAssert.assertTrue(k.DropDownSelection_WebElement(elm.get(count-1), internal_data_map.get("Excess-Property").get(count-1).get("EXS_AggregateDeductible")),"Unable to enter EXS_AggregateDeductible in Excesses table");
			if(!isExcessDeductible){
				isExcessDeductible = internal_data_map.get("Excess-Property").get(count-1).get("EXS_AggregateDeductible").equalsIgnoreCase("Yes") ? true:false;
			}
			
		}
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Excesses page .");
		
		//Aggregate Excess Cap table
		int total_cap_rows = driver.findElements(By.xpath("//*[contains(@id,'_excesses_cap_')]//tr")).size();
		int total_caps=0;
		if(isExcessCap){ //IF Excess cap is Yes then Verify
			
			
			for(int i=1;i<=no_of_property;i++){
				total_caps = total_caps + (total_ExcessTypes[i-1]);
			}
		
			if(total_cap_rows != (total_caps + 2)){
				 TestUtil.reportStatus("<p style='color:red'> Aggregate Excess Cap table contains incorrect row count. Expected:"+total_caps+" and Actual:"+total_cap_rows+" . </p>", "Fail", false);
			}
			
			customAssert.assertTrue(func_verify_Aggregate_Excess_Cap_(no_of_property,total_caps,property_ExcessType),"Error while verifying Aggregate Excess Cap table . ");
			
		}else{
			if(total_cap_rows != 2){
				 TestUtil.reportStatus("<p style='color:red'> Aggregate Excess Cap table contains incorrect row count. Expected:"+2+" and Actual:"+total_cap_rows+" . </p>", "Fail", false);
					
			}
		}
		
				//Aggregate Excess Deductible
				int total_Deductible_rows = driver.findElements(By.xpath("//*[contains(@id,'_excesses_deductible_')]//tr")).size();
				int total_deduct=0;
				if(isExcessDeductible){ //IF Excess Deductible is Yes then Verify
					
					
					for(int i=1;i<=no_of_property;i++){
						total_deduct = total_deduct + (total_ExcessTypes[i-1]);
					}
				
					if(total_Deductible_rows != (total_deduct + 2)){
						 TestUtil.reportStatus("<p style='color:red'> Aggregate Excess Deductible table contains incorrect row count. Expected:"+total_deduct+" and Actual:"+total_Deductible_rows+" . </p>", "Fail", false);
							
					}
					
					customAssert.assertTrue(func_verify_Aggregate_Excess_Deductible_(no_of_property,total_deduct,property_ExcessType),"Error while verifying Aggregate Excess Deductible table . ");
					
				}else{
					if(total_Deductible_rows != 2){
						 TestUtil.reportStatus("<p style='color:red'> Aggregate Excess Deductible table contains incorrect row count. Expected:"+2+" and Actual:"+total_Deductible_rows+" . </p>", "Fail", false);
							
					}
				}
				
				customAssert.assertTrue(func_Add_Input_To_Excess_("Excess Cap",map_data,property_ExcessType),"Error while Adding data to Aggregate Excess Cap table . ");
				customAssert.assertTrue(func_Add_Input_To_Excess_("Excess Deductible",map_data,property_ExcessType),"Error while Adding data to Aggregate Excess Deductible table . ");
				
				customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Excesses page .");		
		
		TestUtil.reportStatus("Entered all the details on Excesses page .", "Info", true);
		
		return retvalue;
		
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Unable to to do operation on Excesses page. \n", t);
        return false;
 }
}

public boolean Verify_added_properties(WebElement elm,Map<Object, Object> map_data,Map<String, List<Map<String, String>>> internal_data_map){
	
	try{
		
		 Select sel = new Select(elm);
		 
		 List<String> property_Keys = new ArrayList<>();
		 String[] props = ((String)map_data.get("EXS_PropertieKeys")).split(":");
		 property_Keys = Arrays.asList(props);		
		 boolean equals = false;
		 		 
		 List<WebElement> all_properties = sel.getOptions();
		
		 String act_prop=null;
		 boolean isPresent=false;
		 String exp_prop=null;
		 
		 if(all_properties.size() <= property_Keys.size()){
		 for(String exp_property:property_Keys){
			 isPresent=false;
			 for(WebElement property: all_properties){
				 
				 act_prop = property.getText();
				 exp_prop = internal_data_map.get("Property Details").get(0).get(exp_property);
				
				 if(exp_prop.contains("Home Buildings")){ //Special Character is comming for Care Home Buildings string so
					 // its content break down
					 exp_prop = exp_prop.split(Character.toString(exp_prop.charAt(4)))[1];
				 }
				 
				 equals = act_prop.trim().contains(exp_prop.trim())?true:false;
				 if(equals){
					 isPresent=true;
					 break;
				 }else{
					 continue;
				 }
				 
			 }
			 
			 if(!isPresent){
					TestUtil.reportStatus("<p style='color:red'>Property Value <b>"+exp_prop+"</b> is missing in property dropdown list .</p> ", "Fail", false);
			 }
			 
			 
		 }
		 }else if(all_properties.size() > property_Keys.size()){
		 
			 for(WebElement property: all_properties){
				 isPresent=false;
				 act_prop = property.getText();
				 
				 for(String exp_property:property_Keys){
					 
					 exp_prop = internal_data_map.get("Property Details").get(0).get(exp_property);
						
					 if(exp_prop.contains("Home Buildings")){
						 exp_prop = exp_prop.split(Character.toString(exp_prop.charAt(4)))[1];
						 equals = act_prop.trim().contains(exp_prop.trim())?true:false;
					 }else{
						 equals = act_prop.trim().equalsIgnoreCase(exp_prop.trim())?true:false;
					 }
				 
						 if(equals){
						 isPresent=true;
						 break;
					 }else{
						 continue;
					 }
					 
				 }
				 
				 if(!isPresent){
						TestUtil.reportStatus("<p style='color:red'>Property Value <b>"+act_prop+"</b> should not present in property dropdown list .</p> ", "Fail", false);
				 }
				 
				 
			 }
		 }
	
	}catch(Throwable t){
		return false;
		
	}
	
	return true;	
	
} 

@SuppressWarnings("null")
public boolean func_verify_Aggregate_Excess_Cap_(int total_Properties,int total_ExcessTypes,Map<String,List<String>> property_ExcessType){
	
	try{
	List<WebElement> stockTypes = null,coverTypes = null;
	List<WebElement> elm = null;
	List<String> properties = new ArrayList<>(), exp_coverTypes_=null;
	TestUtil.reportStatus("Verification Started on Aggregate Excess Cap table .  ", "Info", false);
	for(int c=1;c<=total_Properties;c++){
		
		elm = driver.findElements(By.xpath("//*[contains(@id,'_excesses_table')]//*//select[contains(@name,'_ex_property')]"));
		properties.add((String)k.GetDropDownSelectedValue_WebElement(elm.get(c-1)));
	
	}
	
	stockTypes = driver.findElements(By.xpath("//*[contains(@name,'_ex_stock_type_cap')]"));
	coverTypes = driver.findElements(By.xpath("//table[contains(@id,'_excesses_cap_')]//*[contains(@name,'ex_cover_type_')]"));
	
	String act_st_value=null,act_ct_value=null,exp_ct_value=null;
	int l=0;
	int n=1,m=1;
	
	for(int count=1;count<=total_ExcessTypes;count++){
		
		act_st_value = k.GetDropDownSelectedValue_WebElement(stockTypes.get(count-1));
		boolean isCoverTypeFound=false;
		outer:
		for(int i=1;i<=properties.size();i++){
			
			if(act_st_value.equalsIgnoreCase(properties.get(i-1))){
				
				exp_coverTypes_ = property_ExcessType.get(act_st_value);
				
				//exp_ct_value = exp_coverTypes_.get(i - 1);
				l=exp_coverTypes_.size();
				
				//For each Cover type of Stock Type
				for(String e_coverType : exp_coverTypes_){
					m=1;
					
					while(m++ <= l){
						act_ct_value = k.GetDropDownSelectedValue_WebElement(coverTypes.get(n - 1));
						if(e_coverType.equalsIgnoreCase(act_ct_value)){
							isCoverTypeFound = true;
							TestUtil.reportStatus("StockType: <b>"+act_st_value+"</b> | CoverType: <b>"+e_coverType+"</b> as expected . ", "Pass", false);
							n++;
							break outer;
						}else
						{
							continue;
						}
					}
					
				}
				if(!isCoverTypeFound){
					TestUtil.reportStatus("<p style='color:red'>Missing StockType/CoverType for StockType: <b>"+act_st_value+"</b> .</p> ", "Pass", false);
				}
				
			}else{
				continue;
			}
				
		}
		
	}
	
	}catch(Throwable t){
		System.out.println("Error in func_verify_Aggregate_Excess_Cap_ ");
		return false;
	}
	
	TestUtil.reportStatus("Successfully verified Aggregate Excess Cap table .  ", "Info", false);
	
	return true;
}

@SuppressWarnings("null")
public boolean func_verify_Aggregate_Excess_Deductible_(int total_Properties,int total_ExcessTypes,Map<String,List<String>> property_ExcessType){
	
	try{
	List<WebElement> stockTypes = null,coverTypes = null;
	List<WebElement> elm = null;
	List<String> properties = new ArrayList<>(), exp_coverTypes_=null;
	TestUtil.reportStatus("Verification Started on Aggregate Excess Deductible table .  ", "Info", false);
	for(int c=1;c<=total_Properties;c++){
		
		elm = driver.findElements(By.xpath("//*[contains(@id,'_excesses_table')]//*//select[contains(@name,'_ex_property')]"));
		properties.add((String)k.GetDropDownSelectedValue_WebElement(elm.get(c-1)));
	
	}
	
	stockTypes = driver.findElements(By.xpath("//*[contains(@name,'_ex_stock_type_deductible')]"));
	coverTypes = driver.findElements(By.xpath("//table[contains(@id,'_excesses_deductible_')]//*[contains(@name,'ex_cover_type_')]"));
	
	String act_st_value=null,act_ct_value=null,exp_ct_value=null;
	int l=0;
	int n=1,m=1;
	
	for(int count=1;count<=total_ExcessTypes;count++){
		
		act_st_value = k.GetDropDownSelectedValue_WebElement(stockTypes.get(count-1));
		boolean isCoverTypeFound=false;
		outer:
		for(int i=1;i<=properties.size();i++){
			
			if(act_st_value.equalsIgnoreCase(properties.get(i-1))){
				
				exp_coverTypes_ = property_ExcessType.get(act_st_value);
				
				//exp_ct_value = exp_coverTypes_.get(i - 1);
				l=exp_coverTypes_.size();
				
				//For each Cover type of Stock Type
				for(String e_coverType : exp_coverTypes_){
					m=1;
					
					while(m++ <= l){
						act_ct_value = k.GetDropDownSelectedValue_WebElement(coverTypes.get(n - 1));
						if(e_coverType.equalsIgnoreCase(act_ct_value)){
							isCoverTypeFound = true;
							TestUtil.reportStatus("StockType: <b>"+act_st_value+"</b> | CoverType: <b>"+e_coverType+"</b> as expected . ", "Pass", false);
							n++;
							break outer;
						}else
						{
							continue;
						}
					}
					
				}
				if(!isCoverTypeFound){
					TestUtil.reportStatus("<p style='color:red'>Missing StockType/CoverType for StockType: <b>"+act_st_value+"</b> .</p> ", "Pass", false);
				}
				
			}else{
				continue;
			}
				
		}
		
	}
	
	}catch(Throwable t){
		System.out.println("Error in Aggregate_Excess_Deductible_ ");
		return false;
	}
	
	TestUtil.reportStatus("Successfully verified Aggregate Excess Deductible table .  ", "Info", false);
	
	return true;
}

public boolean func_Add_Input_To_Excess_(String excess_Type,Map<Object,Object> data_map,Map<String,List<String>> coverTypes){
	
	boolean r_value=true;
	int r_index=0;
	List<WebElement> _coverTypes = null;
	String c_types=null;
	try{
		
		switch(excess_Type){
		
		case "Excess Cap":
			
			WebElement cap_table = driver.findElement(By.xpath("//*[contains(@id,'_excesses_cap_table')]"));
			int total_Rows_cap = cap_table.findElements(By.tagName("tr")).size();
			_coverTypes = driver.findElements(By.xpath("//table[contains(@id,'_excesses_cap_')]//*[contains(@name,'ex_cover_type_')]"));
			
			
			List<WebElement> elm = null;
			
			for(int i=2;i<total_Rows_cap;i++){
				
				c_types = k.GetDropDownSelectedValue_WebElement(_coverTypes.get(i-2));
				r_index = get_Row_Index(excess_Type, _coverTypes, c_types,total_Rows_cap);
				
				elm = driver.findElements(By.xpath("//*[contains(@name,'_ex_cap_nr')]"));
				customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get((r_index)),(String)data_map.get("EXS_CAP_"+c_types+"_ExcessCap"),"Input"),"Error while adding EXS_CAP_ExcessCap value . ");
				
				elm = driver.findElements(By.xpath("//*[contains(@name,'_ex_cap_amount')]"));
				customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(r_index),(String)data_map.get("EXS_CAP_"+c_types+"_CapAmount"),"Input"),"Error while adding EXS_CAP_CapAmount value . ");
		
				elm = driver.findElements(By.xpath("//*[contains(@name,'_ex_non_ranking_cap')]"));
				customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(r_index),(String)data_map.get("EXS_CAP_"+c_types+"_NonRankingExcess"),"Input"),"Error while adding EXS_CAP_CapAmount value . ");
		
				elm = driver.findElements(By.xpath("//*[contains(@name,'_ex_residual_excess_cap')]"));
				customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(r_index),(String)data_map.get("EXS_CAP_"+c_types+"_ResidualExcess"),"Input"),"Error while adding EXS_CAP_CapAmount value . ");
		
		
			}
			
			break;
		case "Excess Deductible":
			
			WebElement deduct_table = driver.findElement(By.xpath("//*[contains(@id,'_excesses_deductible_table')]"));
			int total_Rows_deduct = deduct_table.findElements(By.tagName("tr")).size();
			_coverTypes = driver.findElements(By.xpath("//table[contains(@id,'_excesses_deductible_')]//*[contains(@name,'ex_cover_type_')]"));
			
			elm = null;
			
			for(int i=2;i<total_Rows_deduct;i++){
				
				c_types = k.GetDropDownSelectedValue_WebElement(_coverTypes.get(i-2));
				r_index = get_Row_Index(excess_Type, _coverTypes, c_types,total_Rows_deduct);
				
				elm = driver.findElements(By.xpath("//*[contains(@name,'_ex_deductible_nr')]"));
				customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(r_index),(String)data_map.get("EXS_CAP_"+c_types+"_AggregateDeductible"),"Input"),"Error while adding EXS_DEDUCT_AggregateDeductible value . ");
				
				elm = driver.findElements(By.xpath("//*[contains(@name,'_ex_deductible_amount')]"));
				customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(r_index),(String)data_map.get("EXS_CAP_"+c_types+"_Amount"),"Input"),"Error while adding EXS_DEDUCT_Amount value . ");
		
				elm = driver.findElements(By.xpath("//*[contains(@name,'_ex_non_ranking_deductible')]"));
				customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(r_index),(String)data_map.get("EXS_CAP_"+c_types+"_NonRankingExcess"),"Input"),"Error while adding EXS_DEDUCT_NonRankingExcess value . ");
		
				elm = driver.findElements(By.xpath("//*[contains(@name,'_ex_residual_excess_deductible')]"));
				customAssert.assertTrue(k.DynamicXpathWebElement_WebElement(elm.get(r_index),(String)data_map.get("EXS_CAP_"+c_types+"_ResidualExcess"),"Input"),"Error while adding EXS_DEDUCT_ResidualExcess value . ");
		
		
			}
			
			break;
		
		}
		
		
		
	}catch(Throwable t){
		return false;
	}
	
	
	return r_value;
	
}


public int  get_Row_Index(String excess_Type,List<WebElement> coverType,String exp_coverType,int total_Rows){
	
	int index =0;

	String _c_type=null;
	
	switch(excess_Type){
	
	case "Excess Cap":
	
		
		for(int i=0;i<total_Rows;i++){
			
			_c_type = k.GetDropDownSelectedValue_WebElement(coverType.get(i));
			if(_c_type.equalsIgnoreCase(exp_coverType)){
				return i;
			}else{
				continue;
			}
			
		}
	
			
		break;
		
	case "Excess Deductible":
		
		
		
		for(int i=0;i<total_Rows;i++){
			
			_c_type = k.GetDropDownSelectedValue_WebElement(coverType.get(i));
			if(_c_type.equalsIgnoreCase(exp_coverType)){
				return i;
			}else{
				continue;
			}
			
		}
	}
	
	return index;
	
	
}

public boolean CoverSpecificCalculation(String coverName,Map<Object, Object> map_data) throws NumberFormatException, Exception {
	
	
	String sUniqueCol ="Tech. Adjust (%)";
	int tableId = 0;
	int totalCols = 0, totalRows = 0;
	String sTablePath = "html/body/div[3]/form/div/table";
	double expectedfinalTotalPremium = 0.0 , premiumwithTechAdjust = 0.0 , finalExpectedPremium = 0.0;
	String properties[];
	String expectedTechAdjust="", finalActualPremium="" , actualfinalTotalPremium="" , expectedCommAdjust = "";
	int no_of_property = 0;
	tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
	sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
	String abvr = "";
	WebElement table= driver.findElement(By.xpath(sTablePath));
	totalCols = table.findElements(By.tagName("th")).size(); 
	totalRows = table.findElements(By.tagName("tr")).size();
	
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
	
	}
	
	switch (coverName) {
	case "MD":
		
        no_of_property = 2;
        abvr = "MD_";
        
        //Input data in to table : 
        
        
        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr[1]/td[3]/input", internal_data_map, "Property Details", 0, abvr, "BookRate", "BookRate", "Input");
        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr[1]/td[5]/input", internal_data_map, "Property Details", 0, abvr, "TechAdjust", "TechAdjust", "Input");
        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr[1]/td[7]/input", internal_data_map, "Property Details", 0, abvr, "CommAdjust", "CommAdjust", "Input");
        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr[1]/td[8]/input", internal_data_map, "Property Details", 0, abvr, "PremiumOverride", "PremiumOverride", "Input");
        
        
        //Calculate for entered data : 
        for(int i=0;i<no_of_property;i++){
        	
        	String expectedSumInsured = internal_data_map.get("Loss Of Rent").get(i).get("LOI_TotalGrossRentSumInsured");
        	String actualSumInsured = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]/input").replaceAll(",", "");
        	customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expectedSumInsured), Double.parseDouble(actualSumInsured), "Sum Insured"), "Comparison between SumInsured is failing.");
        	
        	double BR = Double.parseDouble(internal_data_map.get("Loss Of Rent").get(i).get("LOI_BookRate"));
        	double expectedBookPremium = (Double.parseDouble(expectedSumInsured) * BR ) / 100.0;
        	double actualBookPremium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[4]/input").replaceAll(",", ""));
        	customAssert.assertTrue(CommonFunction.compareValues(expectedBookPremium, actualBookPremium, "Book Premium"), "Comparison between Book Premium is failing.");
        	expectedTechAdjust = internal_data_map.get("Loss Of Rent").get(i).get("LOI_TechAdjust");
        	expectedCommAdjust = internal_data_map.get("Loss Of Rent").get(i).get("LOI_CommAdjust");
        	
        	premiumwithTechAdjust = expectedBookPremium + ((expectedBookPremium * Double.parseDouble(expectedTechAdjust))/100.0); 
        	finalExpectedPremium = premiumwithTechAdjust + ((premiumwithTechAdjust * Double.parseDouble(expectedCommAdjust))/100.0);
        	finalActualPremium = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[9]/input").replaceAll(",", "");
        	customAssert.assertTrue(CommonFunction.compareValues(finalExpectedPremium, Double.parseDouble(finalActualPremium), "Net Premium"), "Comparison between Net Premium is failing.");
        	
        	expectedfinalTotalPremium = expectedfinalTotalPremium + finalExpectedPremium;
        	
        }
        
        customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save button.");
        actualfinalTotalPremium = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(no_of_property+1)+"]/td[9]/input").replaceAll(",", "");
        customAssert.assertTrue(CommonFunction.compareValues(expectedfinalTotalPremium, Double.parseDouble(actualfinalTotalPremium), "Total Net Premium"), "Comparison between Net Premium is failing.");
    	TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_BusinessInterruption_NetNetPremium", common.roundedOff(Double.toString(expectedfinalTotalPremium)), map_data);
		
		break;
	case "BI":
		properties = ((String)map_data.get("BI_AddLOI")).split(";");
        no_of_property = properties.length;
        abvr = "LOI_";
        
        //Input data in to table : 
        for(int i=0;i<no_of_property;i++){
        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[3]/input", internal_data_map, "Loss Of Rent", i, abvr, "BookRate", "BookRate", "Input");
        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[5]/input", internal_data_map, "Loss Of Rent", i, abvr, "TechAdjust", "TechAdjust", "Input");
        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input", internal_data_map, "Loss Of Rent", i, abvr, "CommAdjust", "CommAdjust", "Input");
        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input", internal_data_map, "Loss Of Rent", i, abvr, "PremiumOverride", "PremiumOverride", "Input");
        }
        
        //Calculate for entered data : 
        for(int i=0;i<no_of_property;i++){
        	
        	String expectedSumInsured = internal_data_map.get("Loss Of Rent").get(i).get("LOI_TotalGrossRentSumInsured");
        	String actualSumInsured = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]/input").replaceAll(",", "");
        	customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expectedSumInsured), Double.parseDouble(actualSumInsured), "Sum Insured"), "Comparison between SumInsured is failing.");
        	
        	double BR = Double.parseDouble(internal_data_map.get("Loss Of Rent").get(i).get("LOI_BookRate"));
        	double expectedBookPremium = (Double.parseDouble(expectedSumInsured) * BR ) / 100.0;
        	double actualBookPremium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[4]/input").replaceAll(",", ""));
        	customAssert.assertTrue(CommonFunction.compareValues(expectedBookPremium, actualBookPremium, "Book Premium"), "Comparison between Book Premium is failing.");
        	expectedTechAdjust = internal_data_map.get("Loss Of Rent").get(i).get("LOI_TechAdjust");
        	expectedCommAdjust = internal_data_map.get("Loss Of Rent").get(i).get("LOI_CommAdjust");
        	
        	premiumwithTechAdjust = expectedBookPremium + ((expectedBookPremium * Double.parseDouble(expectedTechAdjust))/100.0); 
        	finalExpectedPremium = premiumwithTechAdjust + ((premiumwithTechAdjust * Double.parseDouble(expectedCommAdjust))/100.0);
        	finalActualPremium = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[9]/input").replaceAll(",", "");
        	customAssert.assertTrue(CommonFunction.compareValues(finalExpectedPremium, Double.parseDouble(finalActualPremium), "Net Premium"), "Comparison between Net Premium is failing.");
        	
        	expectedfinalTotalPremium = expectedfinalTotalPremium + finalExpectedPremium;
        	
        }
        
        customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save button.");
        actualfinalTotalPremium = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(no_of_property+1)+"]/td[9]/input").replaceAll(",", "");
        customAssert.assertTrue(CommonFunction.compareValues(expectedfinalTotalPremium, Double.parseDouble(actualfinalTotalPremium), "Total Net Premium"), "Comparison between Net Premium is failing.");
    	TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_BusinessInterruption_NetNetPremium", common.roundedOff(Double.toString(expectedfinalTotalPremium)), map_data);
		
		break;
	case "POL":
		
		String s_Section = "Property Owners Liabilities";
		String s_SheetName = "Property Owners Liabilities";
		abvr = "POL_";
		
		customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr[1]/td[3]/input", map_data, s_SheetName, 0, abvr, "BookPremium", s_Section, "Input"),"Unable to enter Book Premium value for - "+s_Section);
		customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr[1]/td[4]/input", map_data, s_SheetName, 0, abvr, "TechAdjust", s_Section, "Input"),"Unable to enter TechAdjust value for - "+s_Section);
		customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr[1]/td[6]/input", map_data, s_SheetName, 0, abvr, "CommAdjust", s_Section, "Input"),"Unable to enter CommAdjust value for - "+s_Section);
		customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr[1]/td[7]/input", map_data, s_SheetName, 0, abvr, "PremiumOverride", s_Section, "Input"),"Unable to enter Premium Override value for - "+s_Section);
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save button.");
        
		String expectedLimitOfIndeminity = (String)map_data.get("POL_LimitOfIndemnity");
    	String actualLimitOfIndeminity = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[1]/td[2]/input").replaceAll(",", "");
    	customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expectedLimitOfIndeminity), Double.parseDouble(actualLimitOfIndeminity), "Limit of Indeminity"), "Comparison between Limit of Indeminity is failing.");
    	
    	double BP = Double.parseDouble((String)map_data.get("POL_BookPremium"));
    	expectedTechAdjust = (String)map_data.get("POL_TechAdjust");
    	expectedCommAdjust = (String)map_data.get("POL_CommAdjust");
    	
    	premiumwithTechAdjust = BP + ((BP * Double.parseDouble(expectedTechAdjust))/100.0); 
    	finalExpectedPremium = premiumwithTechAdjust + ((premiumwithTechAdjust * Double.parseDouble(expectedCommAdjust))/100.0);
    	finalActualPremium = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[1]/td[8]/input").replaceAll(",", "");
    	customAssert.assertTrue(CommonFunction.compareValues(finalExpectedPremium, Double.parseDouble(finalActualPremium), "Net Premium"), "Comparison between Net Premium is failing.");
    	
    	customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save button.");
    	String actualfinalTotalPremiumPOL = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[2]/td[8]/input").replaceAll(",", "");
    	customAssert.assertTrue(CommonFunction.compareValues(finalExpectedPremium, Double.parseDouble(actualfinalTotalPremiumPOL), "Total Net Premium"), "Comparison between Net Premium is failing.");
    	TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_PropertyOwnersLiabilities_NetNetPremium", common.roundedOff(Double.toString(finalExpectedPremium)), map_data);
		
		break;
	case "TER":
		properties = ((String)map_data.get("Add_TerrorismZone")).split(";");
        no_of_property = properties.length;
        abvr = "TER_";
        
        //Input data in to table : 
        for(int i=0;i<no_of_property;i++){
        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[4]/input", internal_data_map, "Terrorism Zone", i, abvr, "TechAdjust", "TechAdjust", "Input");
        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[6]/input", internal_data_map, "Terrorism Zone", i, abvr, "CommAdjust", "CommAdjust", "Input");
        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input", internal_data_map, "Terrorism Zone", i, abvr, "PremiumOverride", "PremiumOverride", "Input");
        }
        
        //Calculate for entered data : 
        for(int i=0;i<no_of_property;i++){
        	
        	String expectedSumInsured = internal_data_map.get("Terrorism Zone").get(i).get("TER_SumInsured");
        	String actualSumInsured = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]").replaceAll(",", "");
        	customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expectedSumInsured), Double.parseDouble(actualSumInsured), "Sum Insured"), "Comparison between SumInsured is failing.");
        	
        	String expectedBookPremium = internal_data_map.get("Terrorism Zone").get(i).get("TER_Premium");
        	String actualBookPremium = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[3]/input").replaceAll(",", "");
        	customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expectedBookPremium), Double.parseDouble(actualBookPremium), "Book Premium"), "Comparison between SumInsured is failing.");
        	
        	expectedTechAdjust = internal_data_map.get("Terrorism Zone").get(i).get("TER_TechAdjust");
        	expectedCommAdjust = internal_data_map.get("Terrorism Zone").get(i).get("TER_CommAdjust");
        	
        	premiumwithTechAdjust = Double.parseDouble(expectedBookPremium) + ((Double.parseDouble(expectedBookPremium) * Double.parseDouble(expectedTechAdjust))/100.0); 
        	finalExpectedPremium = premiumwithTechAdjust + ((premiumwithTechAdjust * Double.parseDouble(expectedCommAdjust))/100.0);
        	finalActualPremium = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[8]/input").replaceAll(",", "");
        	customAssert.assertTrue(CommonFunction.compareValues(finalExpectedPremium, Double.parseDouble(finalActualPremium), "Net Premium"), "Comparison between Net Premium is failing.");
        	
        	expectedfinalTotalPremium = expectedfinalTotalPremium + finalExpectedPremium;
        	
        }
        
        customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save button.");
        actualfinalTotalPremium = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(no_of_property+1)+"]/td[8]/input").replaceAll(",", "");
        customAssert.assertTrue(CommonFunction.compareValues(expectedfinalTotalPremium, Double.parseDouble(actualfinalTotalPremium), "Total Net Premium"), "Comparison between Net Premium is failing.");
    	TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_Terrorism_NetNetPremium", common.roundedOff(Double.toString(expectedfinalTotalPremium)), map_data);
		
		break;
	case "BSC":
		properties = ((String)map_data.get("Add_BeSpoke")).split(";");
        no_of_property = properties.length;
        abvr = "BSC_";
        
        //Input data in to table : 
        for(int i=0;i<no_of_property;i++){
        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[3]/input", internal_data_map, "Add BeSpokeCover", i, abvr, "BookRate", "BookRate", "Input");
        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[5]/input", internal_data_map, "Add BeSpokeCover", i, abvr, "TechAdjust", "TechAdjust", "Input");
        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[7]/input", internal_data_map, "Add BeSpokeCover", i, abvr, "CommAdjust", "CommAdjust", "Input");
        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[8]/input", internal_data_map, "Add BeSpokeCover", i, abvr, "PremiumOverride", "PremiumOverride", "Input");
        }
        
        //Calculate for entered data : 
        for(int i=0;i<no_of_property;i++){
        	
        	String expectedSumInsured = internal_data_map.get("Add BeSpokeCover").get(i).get("Add_BeSpokeSumInsured");
        	String actualSumInsured = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]/input").replaceAll(",", "");
        	customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expectedSumInsured), Double.parseDouble(actualSumInsured), "Sum Insured"), "Comparison between SumInsured is failing.");
        	
        	double BR = Double.parseDouble(internal_data_map.get("Add BeSpokeCover").get(i).get("BSC_BookRate"));
        	double expectedBookPremium = (Double.parseDouble(expectedSumInsured) * BR ) / 100.0;
        	double actualBookPremium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[4]/input").replaceAll(",", ""));
        	customAssert.assertTrue(CommonFunction.compareValues(expectedBookPremium, actualBookPremium, "Book Premium"), "Comparison between Book Premium is failing.");
        	expectedTechAdjust = internal_data_map.get("Add BeSpokeCover").get(i).get("BSC_TechAdjust");
        	expectedCommAdjust = internal_data_map.get("Add BeSpokeCover").get(i).get("BSC_CommAdjust");
        	
        	premiumwithTechAdjust = expectedBookPremium + ((expectedBookPremium * Double.parseDouble(expectedTechAdjust))/100.0); 
        	finalExpectedPremium = premiumwithTechAdjust + ((premiumwithTechAdjust * Double.parseDouble(expectedCommAdjust))/100.0);
        	finalActualPremium = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[9]/input").replaceAll(",", "");
        	customAssert.assertTrue(CommonFunction.compareValues(finalExpectedPremium, Double.parseDouble(finalActualPremium), "Net Premium"), "Comparison between Net Premium is failing.");
        	
        	expectedfinalTotalPremium = expectedfinalTotalPremium + finalExpectedPremium;
        	
        }
        
        customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save button.");
        actualfinalTotalPremium = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(no_of_property+1)+"]/td[9]/input").replaceAll(",", "");
        customAssert.assertTrue(CommonFunction.compareValues(expectedfinalTotalPremium, Double.parseDouble(actualfinalTotalPremium), "Total Net Premium"), "Comparison between Net Premium is failing.");
    	TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_BespokeCover_NetNetPremium", common.roundedOff(Double.toString(expectedfinalTotalPremium)), map_data);
		
		break;
	default:
		break;
	}
	
	return true;
	
}

@SuppressWarnings("unused")
public boolean CoverSpecificCalculationMD(String coverName,Map<Object, Object> map_data,int count) throws NumberFormatException, Exception {
	
	
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
	
	}
	
	String sUniqueCol ="Tech. Adjust (%)";
	int tableId = 0;
	int totalCols = 0, totalRows = 0;
	String sTablePath = "html/body/div[3]/form/div/table";
	double expectedfinalTotalPremium = 0.0 , premiumwithTechAdjust = 0.0 , finalExpectedPremium = 0.0;
	String expectedTechAdjust="", finalActualPremium="" , actualfinalTotalPremium="" , expectedCommAdjust = "";
	String abvr1 = "";
	tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
	sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
	WebElement table= driver.findElement(By.xpath(sTablePath));
	totalCols = table.findElements(By.tagName("th")).size(); 
	totalRows = table.findElements(By.tagName("tr")).size();
	int noOfItems = 0; 
	double BR , expectedBookPremium , actualBookPremium ;
	String expectedSumInsured , actualSumInsured;
	switch (coverName) {
	case "MD":
		
        //Input data in to table : 
        
			if(((internal_data_map.get("Property Details").get(count).get("PoD_AddBuildings")).equalsIgnoreCase("Yes") && (internal_data_map.get("Property Details").get(count).get("PoD_AddContents")).equalsIgnoreCase("No"))){
				noOfItems = 1;
				abvr1 = "AddBuilding_";
				k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(noOfItems)+"]/td[3]/input", internal_data_map, "Property Details", count, abvr1, "BookRate", "BookRate", "Input");
	        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(noOfItems)+"]/td[5]/input", internal_data_map, "Property Details", count, abvr1, "TechAdjustment", "TechAdjustment", "Input");
	        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(noOfItems)+"]/td[7]/input", internal_data_map, "Property Details", count, abvr1, "CommAdjustment", "CommAdjustment", "Input");
	        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(noOfItems)+"]/td[8]/input", internal_data_map, "Property Details", count, abvr1, "PremiumOverride", "PremiumOverride", "Input");
	        	
	        	customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save button.");
	        	
	        	expectedSumInsured = internal_data_map.get("Property Details").get(count).get("AddBuilding_SumInsured");
	        	actualSumInsured = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(noOfItems)+"]/td[2]/input").replaceAll(",", "");
	        	customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expectedSumInsured), Double.parseDouble(actualSumInsured), "Sum Insured"), "Comparison between SumInsured is failing.");
	        	
	        	BR = Double.parseDouble(internal_data_map.get("Property Details").get(count).get("AddBuilding_BookRate"));
	        	expectedBookPremium = (Double.parseDouble(expectedSumInsured) * BR ) / 100.0;
	        	actualBookPremium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(noOfItems)+"]/td[4]/input").replaceAll(",", ""));
	        	customAssert.assertTrue(CommonFunction.compareValues(expectedBookPremium, actualBookPremium, "Book Premium"), "Comparison between Book Premium is failing.");
	        	expectedTechAdjust = internal_data_map.get("Property Details").get(count).get("AddBuilding_TechAdjustment");
	        	expectedCommAdjust = internal_data_map.get("Property Details").get(count).get("AddBuilding_CommAdjustment");
	        	
	        	premiumwithTechAdjust = expectedBookPremium + ((expectedBookPremium * Double.parseDouble(expectedTechAdjust))/100.0); 
	        	finalExpectedPremium = premiumwithTechAdjust + ((premiumwithTechAdjust * Double.parseDouble(expectedCommAdjust))/100.0);
	        	finalActualPremium = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(noOfItems)+"]/td[9]/input").replaceAll(",", "");
	        	customAssert.assertTrue(CommonFunction.compareValues(finalExpectedPremium, Double.parseDouble(finalActualPremium), "Net Premium"), "Comparison between Net Premium is failing.");
	        	
	        	expectedfinalTotalPremium = expectedfinalTotalPremium + finalExpectedPremium;
			}
			else if(((internal_data_map.get("Property Details").get(count).get("PoD_AddBuildings")).equalsIgnoreCase("No") && (internal_data_map.get("Property Details").get(count).get("PoD_AddContents")).equalsIgnoreCase("Yes"))){
				noOfItems = 1;
				abvr1 = "AddContents_";
				k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(noOfItems)+"]/td[3]/input", internal_data_map, "Property Details", count, abvr1, "BookRate", "BookRate", "Input");
	        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(noOfItems)+"]/td[5]/input", internal_data_map, "Property Details", count, abvr1, "TechAdjustment", "TechAdjustment", "Input");
	        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(noOfItems)+"]/td[7]/input", internal_data_map, "Property Details", count, abvr1, "CommAdjustment", "CommAdjustment", "Input");
	        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(noOfItems)+"]/td[8]/input", internal_data_map, "Property Details", count, abvr1, "PremiumOverride", "PremiumOverride", "Input");
	        	
	        	customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save button.");
	        	
	        	expectedSumInsured = internal_data_map.get("Property Details").get(count).get("AddBuilding_SumInsured");
	        	actualSumInsured = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(noOfItems)+"]/td[2]/input").replaceAll(",", "");
	        	customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expectedSumInsured), Double.parseDouble(actualSumInsured), "Sum Insured"), "Comparison between SumInsured is failing.");
	        	
	        	BR = Double.parseDouble(internal_data_map.get("Property Details").get(count).get("AddBuilding_BookRate"));
	        	expectedBookPremium = (Double.parseDouble(expectedSumInsured) * BR ) / 100.0;
	        	actualBookPremium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(noOfItems)+"]/td[4]/input").replaceAll(",", ""));
	        	customAssert.assertTrue(CommonFunction.compareValues(expectedBookPremium, actualBookPremium, "Book Premium"), "Comparison between Book Premium is failing.");
	        	expectedTechAdjust = internal_data_map.get("Property Details").get(count).get("AddBuilding_TechAdjustment");
	        	expectedCommAdjust = internal_data_map.get("Property Details").get(count).get("AddBuilding_CommAdjustment");
	        	
	        	premiumwithTechAdjust = expectedBookPremium + ((expectedBookPremium * Double.parseDouble(expectedTechAdjust))/100.0); 
	        	finalExpectedPremium = premiumwithTechAdjust + ((premiumwithTechAdjust * Double.parseDouble(expectedCommAdjust))/100.0);
	        	finalActualPremium = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(noOfItems)+"]/td[9]/input").replaceAll(",", "");
	        	customAssert.assertTrue(CommonFunction.compareValues(finalExpectedPremium, Double.parseDouble(finalActualPremium), "Net Premium"), "Comparison between Net Premium is failing.");
	        	
	        	expectedfinalTotalPremium = expectedfinalTotalPremium + finalExpectedPremium;
			}
			else if(((internal_data_map.get("Property Details").get(count).get("PoD_AddBuildings")).equalsIgnoreCase("Yes") && (internal_data_map.get("Property Details").get(count).get("PoD_AddContents")).equalsIgnoreCase("Yes"))){
				noOfItems = 2;
				abvr1 = "AddBuilding_";
				k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr[1]/td[3]/input", internal_data_map, "Property Details", count, abvr1, "BookRate", "BookRate", "Input");
	        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr[1]/td[5]/input", internal_data_map, "Property Details", count, abvr1, "TechAdjustment", "TechAdjustment", "Input");
	        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr[1]/td[7]/input", internal_data_map, "Property Details", count, abvr1, "CommAdjustment", "CommAdjustment", "Input");
	        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr[1]/td[8]/input", internal_data_map, "Property Details", count, abvr1, "PremiumOverride", "PremiumOverride", "Input");
	        	abvr1 = "AddContents_";
	        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr[2]/td[3]/input", internal_data_map, "Property Details", count, abvr1, "BookRate", "BookRate", "Input");
	        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr[2]/td[5]/input", internal_data_map, "Property Details", count, abvr1, "TechAdjustment", "TechAdjustment", "Input");
	        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr[2]/td[7]/input", internal_data_map, "Property Details", count, abvr1, "CommAdjustment", "CommAdjustment", "Input");
	        	k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr[2]/td[8]/input", internal_data_map, "Property Details", count, abvr1, "PremiumOverride", "PremiumOverride", "Input");
	        	
	        	customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save button.");
	        	
	        	expectedSumInsured = internal_data_map.get("Property Details").get(count).get("AddBuilding_SumInsured");
	        	actualSumInsured = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[1]/td[2]/input").replaceAll(",", "");
	        	customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expectedSumInsured), Double.parseDouble(actualSumInsured), "Sum Insured"), "Comparison between SumInsured is failing.");
	        	
	        	BR = Double.parseDouble(internal_data_map.get("Property Details").get(count).get("AddBuilding_BookRate"));
	        	expectedBookPremium = (Double.parseDouble(expectedSumInsured) * BR ) / 100.0;
	        	actualBookPremium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[1]/td[4]/input").replaceAll(",", ""));
	        	customAssert.assertTrue(CommonFunction.compareValues(expectedBookPremium, actualBookPremium, "Book Premium"), "Comparison between Book Premium is failing.");
	        	expectedTechAdjust = internal_data_map.get("Property Details").get(count).get("AddBuilding_TechAdjustment");
	        	expectedCommAdjust = internal_data_map.get("Property Details").get(count).get("AddBuilding_CommAdjustment");
	        	
	        	premiumwithTechAdjust = expectedBookPremium + ((expectedBookPremium * Double.parseDouble(expectedTechAdjust))/100.0); 
	        	finalExpectedPremium = premiumwithTechAdjust + ((premiumwithTechAdjust * Double.parseDouble(expectedCommAdjust))/100.0);
	        	finalActualPremium = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[1]/td[9]/input").replaceAll(",", "");
	        	customAssert.assertTrue(CommonFunction.compareValues(finalExpectedPremium, Double.parseDouble(finalActualPremium), "Net Premium"), "Comparison between Net Premium is failing.");
	        	
	        	expectedfinalTotalPremium = expectedfinalTotalPremium + finalExpectedPremium;
	        	
	        	expectedSumInsured = internal_data_map.get("Property Details").get(count).get("AddContents_SumInsured");
	        	actualSumInsured = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[2]/td[2]/input").replaceAll(",", "");
	        	customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expectedSumInsured), Double.parseDouble(actualSumInsured), "Sum Insured"), "Comparison between SumInsured is failing.");
	        	
	        	BR = Double.parseDouble(internal_data_map.get("Property Details").get(count).get("AddContents_BookRate"));
	        	expectedBookPremium = (Double.parseDouble(expectedSumInsured) * BR ) / 100.0;
	        	actualBookPremium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[2]/td[4]/input").replaceAll(",", ""));
	        	customAssert.assertTrue(CommonFunction.compareValues(expectedBookPremium, actualBookPremium, "Book Premium"), "Comparison between Book Premium is failing.");
	        	expectedTechAdjust = internal_data_map.get("Property Details").get(count).get("AddContents_TechAdjustment");
	        	expectedCommAdjust = internal_data_map.get("Property Details").get(count).get("AddContents_CommAdjustment");
	        	
	        	premiumwithTechAdjust = expectedBookPremium + ((expectedBookPremium * Double.parseDouble(expectedTechAdjust))/100.0); 
	        	finalExpectedPremium = premiumwithTechAdjust + ((premiumwithTechAdjust * Double.parseDouble(expectedCommAdjust))/100.0);
	        	finalActualPremium = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[2]/td[9]/input").replaceAll(",", "");
	        	customAssert.assertTrue(CommonFunction.compareValues(finalExpectedPremium, Double.parseDouble(finalActualPremium), "Net Premium"), "Comparison between Net Premium is failing.");
	        	
	        	expectedfinalTotalPremium = expectedfinalTotalPremium + finalExpectedPremium;
			}
		
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save button.");
	        actualfinalTotalPremium = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(noOfItems+1)+"]/td[9]/input").replaceAll(",", "");
	        customAssert.assertTrue(CommonFunction.compareValues(expectedfinalTotalPremium, Double.parseDouble(actualfinalTotalPremium), "Total Net Premium"), "Comparison between Net Premium is failing.");
	    	TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_MaterialDamage_NetNetPremium", common.roundedOff(Double.toString(expectedfinalTotalPremium)), map_data);
			
		break;
	}
	
	return true;
	
}

public boolean funcValidate_ManualRatedTables(Map<Object, Object> map_data, String s_TablePath, String s_CoverName, String abvr){
	boolean retValue = true;
	String s_Section = null, i_abvr = null;
	int totalCols = 0, totalRows = 0;
	String s_SheetName =null, s_InnerSheetName = null, s_ColName = null;
	double s_BookP = 0.00, s_TechAdjust = 0.00, s_commAdjust = 0.00, s_Final = 0.00, s_TotalP = 0.00;
	double c_BookP = 0.00, c_TechAdjust = 0.00, c_commAdjust = 0.00, c_Final = 0.00;
	
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
									
					customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[3]/input", common.NB_Structure_of_InnerPagesMaps, s_InnerSheetName, i-1, i_abvr, "TechAdjust", "TechAdjust", "Input"),"Unable to enter TechAdjust in table");
					customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[4]/input", common.NB_Structure_of_InnerPagesMaps, s_InnerSheetName, i-1, i_abvr, "CommAdjust", "CommAdjust", "Input"),"Unable to enter CommAdjust in table");
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
						c_BookP = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(i-1).get(i_abvr+"BookPremium"));
						c_TechAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(i-1).get(i_abvr+"TechAdjust"));
						c_commAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(i-1).get(i_abvr+"CommAdjust"));
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
    	c.add(Calendar.DATE, ammendmet_period);
    	dateobj = df.parse(df.format(c.getTime()));
    	
    	/*SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	Calendar c = Calendar.getInstance();    
    	c.add(Calendar.DATE, ammendmet_period);
    	dateobj = df.parse(df.format(c.getTime()));*/
    	
	
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

public boolean funcPolicyGeneral_MTA(Map<Object, Object> map_data, String code, String event) {
	boolean retVal = true;
	
	try{
		
	
		customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");
		customAssert.assertTrue(k.Input("COB_PG_InsuredName", (String)map_data.get("PG_InsuredName")),	"Unable to enter value in Insured Name  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_InsuredName", "value"),"Insured Name Field Should Contain Valid Name  .");
		if(((String)map_data.get("PG_AdditionalInsuredName")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(k.Click("POF_PG_AdditionalInsuredName"),	"Unable to enter value in Turnover field .");
			customAssert.assertTrue(k.Input("POF_PG_AdditionalFurtherDetails", (String)map_data.get("PG_AdditionalFurtherDetails")),	"Unable to enter value in Turnover field .");
			
		}
		customAssert.assertTrue(k.Input("POF_PG_TradingName", (String)map_data.get("PG_TradingName")),	"Unable to enter value in Trading Name  field .");
		customAssert.assertTrue(k.DropDownSelection("POF_PG_CompanyStatus", (String)map_data.get("PG_CompanyStatus")), "Unable to select Comapny status from dropdown field.");
		//customAssert.assertTrue(k.DropDownSelection("POF_PG_Carrier", (String)map_data.get("PG_Carrier")), "Unable to select Comapny status from dropdown field.");
		//if(((String)map_data.get("PG_Carrier")).contains("NIG")){
		
		//Carrier related code is commented due to defect : SUP-1374
		
		if(common.currentRunningFlow.equalsIgnoreCase("Renewal")){
			String Carrier = k.getText("POF_PG_Carrier_Renewal");
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Policy General", (String)map_data.get("Automation Key"), "PG_Carrier", Carrier, map_data);
			String policyRefNumber = common_Zennor.getUniquePolicyReferanceNumber(map_data);
			customAssert.assertTrue(k.Input("POF_PG_CarrierPolicyNumber", policyRefNumber),"Unable to enter value in POF_PG_CarrierPolicyNumber . ");
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Policy General", (String)map_data.get("Automation Key"), "PG_CarrierPolicyNumber", policyRefNumber, map_data);
			TestUtil.reportStatus("Policy Referance Number for Carrier : <b>[  "+(String)map_data.get("PG_Carrier")+"  ]</b> is : <b>[  "+policyRefNumber+"  ]</b>", "Info", false);
		}else{
			String exp_nbP_number , act_c_P_number;
			String Carrier = k.getTextWithoutError("POF_PG_Carrier_Renewal");
			if(Carrier==null){
				
				WebElement elm = driver.findElement(By.xpath("//*[@name='pof_carrier_list']"));
				Select select = new Select(elm);
				Carrier = select.getFirstSelectedOption().getText();
				
			}
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Policy General", (String)map_data.get("Automation Key"), "PG_Carrier", Carrier, map_data);
			if(common.currentRunningFlow.equalsIgnoreCase("MTA") && CommonFunction.businessEvent.equalsIgnoreCase("Renewal")){
				exp_nbP_number = (String)common.Renewal_excel_data_map.get("PG_CarrierPolicyNumber");
				act_c_P_number = k.getAttribute("POF_PG_CarrierPolicyNumber", "value");
			}else{
				if(common_POF.isMTARewindFlow){
					exp_nbP_number = (String)common.MTA_excel_data_map.get("PG_CarrierPolicyNumber");
					act_c_P_number = k.getAttribute("POF_PG_CarrierPolicyNumber", "value");
				}else{
					exp_nbP_number = (String)common.NB_excel_data_map.get("PG_CarrierPolicyNumber");
					act_c_P_number = k.getAttribute("POF_PG_CarrierPolicyNumber", "value");	
				}
			}
			
			
				
			if(!exp_nbP_number.equalsIgnoreCase(act_c_P_number)){
					 TestUtil.reportStatus("<p style='color:red'> Carrier Policy Number displayed for MTA is not equal to New Business Expected: <b>"+exp_nbP_number+"</b> Actual: <b>"+act_c_P_number+"</b> . </p>" , "Fail", true);
			}
				
			if(!((String)map_data.get("IsSameCarrierPolicyNumber")).equalsIgnoreCase("Yes")){
				String policyRefNumber = common_Zennor.getUniquePolicyReferanceNumber(map_data);
				customAssert.assertTrue(k.Input("POF_PG_CarrierPolicyNumber", policyRefNumber),"Unable to enter value in POF_PG_CarrierPolicyNumber . ");
				TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Policy General", (String)map_data.get("Automation Key"), "PG_CarrierPolicyNumber", policyRefNumber, map_data);
				TestUtil.reportStatus("Policy Referance Number for Carrier : <b>[  "+(String)map_data.get("PG_Carrier")+"  ]</b> is : <b>[  "+policyRefNumber+"  ]</b>", "Info", false);
			
			}else{
				String policyRefNumber = (String)common.NB_excel_data_map.get("PG_CarrierPolicyNumber");
				TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Policy General", (String)map_data.get("Automation Key"), "PG_CarrierPolicyNumber", policyRefNumber, map_data);
				TestUtil.reportStatus("Policy Referance Number for Carrier : <b>[  "+(String)map_data.get("PG_Carrier")+"  ]</b> is : <b>[  "+policyRefNumber+"  ]</b>", "Info", false);
			}
		}
			
		
			//}
		customAssert.assertTrue(k.Input("CCF_Address_CC_Address", (String) map_data.get("PG_Address")),"Unable to enter value in Address field. ");
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"Address Field Should Contain Valid Address  .");
		customAssert.assertTrue(k.Input("CCF_Address_CC_line2", (String) map_data.get("PG_Line1")),"Unable to enter value in Address field line 1 . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_line3", (String) map_data.get("PG_Line2")),"Unable to enter value in Address field line 2 . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_Town", (String) map_data.get("PG_Town")),"Unable to enter value in Town field . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_County", (String) map_data.get("PG_County")),"Unable to enter value in County  . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode", (String)map_data.get("PG_Postcode")),"Unable to enter value in PostCode");
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Postcode", "value"),"PostCode Field Should Contain Valid Postcode  .");
		customAssert.assertTrue(common.validatePostCode((String)map_data.get("PG_Postcode")),"Post Code is not in Correct format .");
		
		customAssert.assertTrue(k.Input("COB_PG_BusDesc", (String)map_data.get("PG_BusDesc")),	"Unable to enter value in Provided Details field .");
		
		// Select Trade Code :
		
	//	if(((String)common.MTA_excel_data_map.get("PG_TCS_TradeCode_Button")).equalsIgnoreCase("Yes")){
	//		customAssert.assertTrue(common_Zennor.tradeCodeSelection((String)common.NB_excel_data_map.get("PG_TCS_TradeCode") , "Policy Details" , 0),"Trade code selection function is having issue(S).");
	//	}
		
		customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");		
		customAssert.assertTrue(k.DropDownSelection("POF_PG_SuppressPremiumFromDocumentation", (String)map_data.get("PG_SuppressPremiumFromDocumentation")), "Unable to select Comapny status from dropdown field.");
		customAssert.assertTrue(k.DropDownSelection("POF_PG_SuppressDocumentation", (String)map_data.get("PG_SuppressDocumentation")), "Unable to select Comapny status from dropdown field.");
		TestUtil.reportStatus("Entered all the details on Policy General page after Endorsement .", "Info", true);
		
		return retVal;
	
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Unable to to do operation on policy General page. \n", t);
        return false;
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
 * This method selects the specified covers from cover Page
 */
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

public boolean MaterialDamagePage_MTA(Map<Object, Object> map_data,Map<String, List<Map<String, String>>> internal_data_map){
	boolean retValue = true;
	String abvr = null, coverName = null;
	
	
	
	
	try{
			customAssert.assertTrue(common.funcPageNavigation("Material Damage", ""),"Material Damage page is having issue(S)");
		 	int count = 0;
		 	int noOfProperties = 0;
			
			String[] properties = ((String)map_data.get("IP_AddProperty")).split(",");
			noOfProperties = properties.length;
			
			boolean isNotStale=true;
    		
    		while(isNotStale){
    			try{
    				List<WebElement> delete_Btns = driver.findElements(By.xpath("//*[@id='table0']//*[text()='Delete']"));
    				for(WebElement element: delete_Btns){
    					if(element.isDisplayed()){
    						element.click();
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
    		
			while(count < noOfProperties ){
				
				customAssert.assertTrue(k.Click("CCF_Btn_AddProperty"), "Unable to click Add Property Button on Insured Properties .");
				customAssert.assertTrue(addProperty(map_data,count),"Error while adding insured proprty  .");
				TestUtil.reportStatus("Insured Property  <b>[  "+internal_data_map.get("Property Details").get(count).get("Automation Key")+"  ]</b>  added successfully . ", "Info", true);
				customAssert.assertTrue(k.Click("CCF_Btn_Back"), "Unable to click on Back Button on Property Details .");
				count++;
			}
			
			
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
/**
 * 
 * This method handles POF Flat Premium screens scripting.
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
			common_Zennor.func_FP_Entries_Verification_MTA(internal_data_map.get("Flat-Premiums").get(count-1).get("FP_Section"),internal_data_map,count);
			
			//For each added entry in FP, cover name will be removed from Section List
			common.CoversDetails_data_list.remove(internal_data_map.get("Flat-Premiums").get(count-1).get("FP_Section").replaceAll(" ", ""));
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
	
	/*if(common_POF.isMTARewindFlow){
		common.transaction_Details_Premium_Values.clear();
	}*/
	
	if(fp_Table.isDisplayed()){
		
		/*if(common_POF.isMTARewindFlow)
			TestUtil.reportStatus("Verification Started for Transaction Details table on premium summary page after Endorsement(MTA) Rewind . ", "Info", true);
		else
			TestUtil.reportStatus("Verification Started for Transaction Details table on premium summary page after Endorsement(MTA) . ", "Info", true);
		*///For Each Cover Row
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
	
	System.out.println("Error while reading Flat Premium Entries . ");
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
			String policy_status_actual = k.getText("Policy_status_header");
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
		 
			for(int p=0;p<common.CoversDetails_data_list.size();p++){
				coverName_datasheet = common.CoversDetails_data_list.get(p);
				
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
//CR272_001, CR272_002, CR272_003, CR272_004, CR272_005
public boolean funcSearchPolicyByRefNum(Map<Object, Object> map_data)
{
	boolean ret_value = true;
	String cl_name = (String)map_data.get("NB_ClientName");
	try{
		TestUtil.reportStatus("Searching Policy using Reference number:"+(String)map_data.get("PG_CarrierPolicyNumber") , "Info", true);
		customAssert.assertTrue(k.getText("searchPoliciesPage_Header").equalsIgnoreCase("Search Policies"), "Search policies Page is not loaded . ");
		customAssert.assertTrue(k.Click("comm_clear"),"Unable to Clear Search Policies Filter Data .");
		customAssert.assertTrue(k.Input("Policy_Ref_Num_txtBox", (String)map_data.get("PG_CarrierPolicyNumber")), "Unable to enter policy Reference Number.");
		customAssert.assertTrue(k.Click("comm_search"), "Unable to click on search button.");
		customAssert.assertTrue(k.getText("Policy_Client_Name").contains(cl_name),"Policy Client Name issue after searching policy . ");
		//customAssert.assertTrue(cl_name.equalsIgnoreCase(k.getText("Policy_Client_Name")),"Policy Client Name issue after searching policy . ");
		TestUtil.reportStatus("Policy: "+(String)map_data.get("NB_QuoteNumber")+" successfully searched using Reference number."+(String)map_data.get("PG_CarrierPolicyNumber") , "Info", true);
		
		k.pressDownKeyonPage();
		customAssert.assertTrue(k.Click("Policy_Client_Name"), "Unable to click on client name . ");
	}catch(Throwable t)
	{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     
        k.reportErr("Failed in "+methodName+" function", t);
        ret_value = false;;
	}
	
	return ret_value;
}
}
