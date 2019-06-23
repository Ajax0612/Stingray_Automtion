package com.selenium.commonfiles.base;

import static com.selenium.commonfiles.util.TestUtil.WriteDataToXl;

import java.awt.AWTException;
import java.awt.Robot;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.selenium.commonfiles.util.ErrorInTestMethod;
import com.selenium.commonfiles.util.TestUtil;

public class CommonFunction_ENA extends TestBase{

	SimpleDateFormat df = new SimpleDateFormat();
	public int actual_no_of_years=0,err_count=0,trans_error_val=0;
	
public void NewBusinessFlow(String code,String event) throws ErrorInTestMethod{
	String testName = (String)common.NB_excel_data_map.get("Automation Key");
	String navigationBy = CONFIG.getProperty("NavigationBy");
	try{
		
		customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
		customAssert.assertTrue(common.checkClient(common.NB_excel_data_map,code,event),"");
		customAssert.assertTrue(common.createNewQuote(common.NB_excel_data_map,code,event), "Unable to create new quote.");
		customAssert.assertTrue(common.selectLatestQuote(common.NB_excel_data_map,code,event), "Unable to select quote from table.");
		customAssert.assertTrue(funcPolicyDetails(common.NB_excel_data_map,code,event), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(funcPreviousClaims(common.NB_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(common.funcCovers(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
		if(navigationBy.equalsIgnoreCase("Next")){
			common.funcPageNavigation("", "Next");
		}
		if(((String)common.NB_excel_data_map.get("CD_Machinerymovement")).equals("Yes")){		
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Machinery Movement"),"Issue while Navigating to Terrorism screen . ");
			customAssert.assertTrue(funcMachinerymovement(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_Machinerysuddenandunforeseendamage")).equals("Yes")){		
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Machinery Damage"),"Issue while Navigating to Terrorism screen . ");
			customAssert.assertTrue(funcMachineryDamage(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_Machinerylossofprofits")).equals("Yes")){		
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Machinery loss of profits"),"Issue while Navigating to Terrorism screen . ");
			//customAssert.assertTrue(funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_Computer")).equals("Yes")){		
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Computer"),"Issue while Navigating to Terrorism screen . ");
			//customAssert.assertTrue(funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_Deteriorationofstock")).equals("Yes")){		
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Deterioration of stock"),"Issue while Navigating to Terrorism screen . ");
			//customAssert.assertTrue(funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_Contractorsplant")).equals("Yes")){		
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Contractors plant"),"Issue while Navigating to Terrorism screen . ");
			//customAssert.assertTrue(funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		
		if(((String)common.NB_excel_data_map.get("CD_Contractworksconstruction")).equals("Yes")){		
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Contract works construction"),"Issue while Navigating to Terrorism screen . ");
			//customAssert.assertTrue(funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Terrorism"),"Issue while Navigating to Terrorism screen . ");
			//customAssert.assertTrue(funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		/*
				
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcPremiumSummary(common.NB_excel_data_map,code,event,"NB"), "Premium Summary function is having issue(S) . ");
			Assert.assertTrue(common.funcStatusHandling(common.NB_excel_data_map,code,event));
			customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
			
			if(TestBase.businessEvent.equals("NB")){
			customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
			customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
			
			TestUtil.reportTestCasePassed(testName);
			}*/
	
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
		
		customAssert.assertTrue(k.Input("CCF_PD_ProposerName", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_PD_ProposerName", (String)map_data.get("NB_ClientName")),	"Unable to enter value in Proposer Name  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_PD_ProposerName", "value"),"Proposer Name Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("CCF_CC_TradingName", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_CC_TradingName", (String)map_data.get("PD_TradingName")),	"Unable to enter value in Trading Name  field .");
		customAssert.assertTrue(k.Input("CCF_PD_BusinessDesc", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_PD_BusinessDesc", (String)map_data.get("PD_BusinessDesc")),	"Unable to enter value in Business Desc  field .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_1QS", (String)map_data.get("PD_1QS")), "Unable to Select 1QS radio button on Policy Details Page.");
		customAssert.assertTrue(k.Input("ENA_PD_DateEstablishment", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("ENA_PD_DateEstablishment", (String)map_data.get("PD_DateEstablishment")),	"Unable to enter value in Date Establishment  field .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_NewVenture", (String)map_data.get("PD_NewVenture")), "Unable to Select New Venture radio button on Policy Details Page.");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_Prospect", (String)map_data.get("PD_Prospect")), "Unable to Select Prospect radio button on Policy Details Page.");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CrossSell", (String)map_data.get("PD_CrossSell")), "Unable to Select CrossSell radio button on Policy Details Page.");
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
		customAssert.assertTrue(k.Input("CCF_QC_TargetPemium", (String) map_data.get("PD_TargetPremium")),"Unable to enter value in Target Pemium field. ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_TaxExempt", (String)map_data.get("PD_TaxExempt")), "Unable to Select TaxExempt radio button on Policy Details Page.");
		if(((String)map_data.get("PD_TaxExempt")).equalsIgnoreCase("Yes")){
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_data.get("Automation Key"), "PS_IPTRate", "0", common.NB_excel_data_map);
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_data.get("Automation Key"), "PS_InsuranceTaxButton", "Yes", common.NB_excel_data_map);
		}else{
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_data.get("Automation Key"), "PS_IPTRate", "12", common.NB_excel_data_map);
		}
		
		/*customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CarrierOverride", (String)map_data.get("PD_CarrierOverride")), "Unable to Select Carrier Override radio button on Policy Details Page.");
		
		if(map_data.get("PD_CarrierOverride").equals("Yes")){
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CO_RefferedToHead", (String)map_data.get("PD_CO_RefferedToHead")), "Unable to Select Reffered To Head radio button on Policy Details Page.");
		}
		k.waitTwoSeconds();
		*/
		if(((String)common.NB_excel_data_map.get("PD_TCS_TradeCode_Button")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.tradeCodeSelection((String)map_data.get("PD_TCS_TradeCode") , "Policy Details" , 0),"Trade code selection function is having issue(S).");
		}
		
		customAssert.assertTrue(k.SelectRadioBtn("POC_PD_HazardGroup", (String)map_data.get("PD_HazardGroup")), "Unable to Select  Hazard Group radio button on Policy Details Page.");
		switch ((String)map_data.get("PD_HazardGroup")) {
		case "Yes":
			customAssert.assertTrue(k.Input("POC_PD_NewHazardGroupValue", (String) map_data.get("PD_NewHazardGroupValue")),"Unable to enter value in  Hazard Group value field. ");
			customAssert.assertTrue(k.Input("POC_PD_HazardGroupOverrideReason", (String) map_data.get("PD_HazardGroupOverrideReason")),"Unable to enter value in Hazard Group Override Reason. ");
			break;
		}
		
		customAssert.assertTrue(k.DropDownSelection("ENA_longtermundertaking", (String)map_data.get("PD_Unertaking")), "Unable to Select  Hazard Group radio button on Policy Details Page.");
		switch ((String)map_data.get("PD_Unertaking")) {
		case "Yes":
			customAssert.assertTrue(k.Click("ENA_undertakingexpirydate"), "Unable to Click Long term undertaking expiry date.");
			customAssert.assertTrue(k.Input("ENA_undertakingexpirydate", (String)map_data.get("PD_UnderTakingExpiryDate")),"Unable to Enter Long term undertaking expiry date.");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			break;
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

public boolean funcMachinerymovement(Map<Object, Object> map_data){
	
	boolean r_Value = true;
	try{
		customAssert.assertTrue(common.funcPageNavigation("Machinery Movement", ""),"Machinery Movement page is having issue(S)");
		
		customAssert.assertTrue(k.Input("ENA_Situation", (String)map_data.get("MM_Situation")),	"Unable to enter value in Situation field .");
		String[] geographical_Limits = ((String)common.NB_excel_data_map.get("MM_Operations")).split(",");
		List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
		for(String geo_limit : geographical_Limits){
			for(WebElement each_ul : ul_elements){
				customAssert.assertTrue(k.Click("SPI_PG_Geographic_Limit"),"Error while Clicking Geographic Limit List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		customAssert.assertTrue(k.Input("ENA_Limit", (String)map_data.get("MM_Limit")),"Unable to enter value in Limit field .");
		customAssert.assertTrue(k.Input("ENA_Excess", (String)map_data.get("MM_Excess")),"Unable to enter value in Excess field .");
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
		
		int count = 0;
		int noOfProperties = 0;
		if(common.no_of_inner_data_sets.get("BS Insured MM")==null){
			noOfProperties = 0;
		}else{
			noOfProperties = common.no_of_inner_data_sets.get("BS Insured MM");
		}
		
		while(count < noOfProperties ){
			
			customAssert.assertTrue(add_BespokeSumInsured(map_data),"Error while adding insured proprty  .");
			TestUtil.reportStatus("Insured Property  <b>[  "+common.NB_Structure_of_InnerPagesMaps.get("BS Insured MM").get(count).get("Automation Key")+"  ]</b>  added successfully . ", "Info", true);
			count++;
		}
			
		TestUtil.reportStatus("All the specified BeSpoke SumInsured added and verified successfully . ", "Info", true);
		
		return r_Value;
		
		
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Insured Properties function is having issue(S). \n", t);
        return false;
 }
}

public boolean funcMachineryDamage(Map<Object, Object> map_data){
	
	boolean r_Value = true;
	try{
		customAssert.assertTrue(common.funcPageNavigation("Machinery Sudden And Unforeseen Damage", ""),"Machinery Movement page is having issue(S)");
		
		customAssert.assertTrue(k.Input("ENA_Situation", (String)map_data.get("MM_Situation")),	"Unable to enter value in Situation field .");
		String[] geographical_Limits = ((String)common.NB_excel_data_map.get("MM_Operations")).split(",");
		List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
		for(String geo_limit : geographical_Limits){
			for(WebElement each_ul : ul_elements){
				customAssert.assertTrue(k.Click("SPI_PG_Geographic_Limit"),"Error while Clicking Geographic Limit List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		customAssert.assertTrue(k.Input("ENA_Limit", (String)map_data.get("MM_Limit")),"Unable to enter value in Limit field .");
		customAssert.assertTrue(k.Input("ENA_Excess", (String)map_data.get("MM_Excess")),"Unable to enter value in Excess field .");
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
		
		int count = 0;
		int noOfProperties = 0;
		if(common.no_of_inner_data_sets.get("BS Insured MM")==null){
			noOfProperties = 0;
		}else{
			noOfProperties = common.no_of_inner_data_sets.get("BS Insured MM");
		}
		
		while(count < noOfProperties ){
			
			customAssert.assertTrue(add_BespokeSumInsured(map_data),"Error while adding insured proprty  .");
			TestUtil.reportStatus("Insured Property  <b>[  "+common.NB_Structure_of_InnerPagesMaps.get("BS Insured MM").get(count).get("Automation Key")+"  ]</b>  added successfully . ", "Info", true);
			count++;
		}
			
		TestUtil.reportStatus("All the specified BeSpoke SumInsured added and verified successfully . ", "Info", true);
		
		return r_Value;
		
		
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Insured Properties function is having issue(S). \n", t);
        return false;
 }
}

/*public boolean addProperty(Map<Object, Object> map_data,int count){
	
	boolean r_value=true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Property Details", ""),"Property Details page navigation issue(S)");
		
		//
		if(!(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_CopyAddress")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(k.Input("CCF_Address_CC_Address", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_Address")),"Unable to enter value in Address field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"Address Field Should Contain Valid Value on Client Details .");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line2", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_AddressL2")),"Unable to enter value in Address field line 2 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line3", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_AddressL3")),"Unable to enter value in Address field line 3 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Town", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_Town")),"Unable to enter value in Town field . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_County", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_County")),"Unable to enter value in County  . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_Postcode")),"Unable to enter value in PostCode field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Postcode", "value"),"Postcode Field Should Contain Valid Value on Client Details .");
			customAssert.assertTrue(common.validatePostCode(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_Postcode")),"Post Code is not in Correct format .");
		}else{
			customAssert.assertTrue(k.Click("CCF_Btn_CopyCorAddress"));
		}
		//
		
		customAssert.assertTrue(k.Input("CCF_PoD_PropertyAge", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_PropertyAge")),"Unable to enter value in Age of Property (years) . ");
		customAssert.assertTrue(k.DropDownSelection("CCF_PoD_TerrorismZone", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_TerrorismZone")), "Unable to select value from Terrorism Zone dropdown .");
		
		//Statement of Fact
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q1", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_SOF_Q1")), "Unable to Select first SOF radio button on Policy Details Page.");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q2", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_SOF_Q2")), "Unable to Select second SOF radio button on Policy Details Page.");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q3", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_SOF_Q3")), "Unable to Select third SOF radio button on Policy Details Page.");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q4", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_SOF_Q4")), "Unable to Select fourth SOF radio button on Policy Details Page.");
		
		//Sums Insured
		customAssert.assertTrue(k.DropDownSelection("CCF_PoD_DayOneUplift",common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_DayOneUplift")), "Unable to select value from Day One uplift dropdown .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q1", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_SOF_Q1")), "Unable to Select first SOF radio button on Policy Details Page.");
		
		//Proximity
		customAssert.assertTrue(k.Input("CCF_PoD_ProximityDescription", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_ProximityDescription")),"Unable to enter value in Proximity description . ");
		
		//Trade Code
		if((common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_TCS_TradeCode_Button")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.tradeCodeSelection((String)common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_MD_TCS_TradeCode"),"Property Details",count),"Trade code selection function is having issue(S).");	
		}
		customAssert.assertTrue(k.Click("CCF_Btn_PoD_SelectTradeCode"), "Unable to click on Select MD Trade Code button in Policy Details .");
		customAssert.assertTrue(common.funcPageNavigation("Trade Code Selection", ""), "Navigation problem to Trade Code Selection page .");
		
		customAssert.assertTrue(k.Input("CCF_TradeCode_SearchBox", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_MD_TCS_TradeCode")),"Unable to enter value in TradeCode Search field . ");
		k.waitTwoSeconds();
		String v_exp_selectedTradeCode = k.getLinkText("CCF_SearchedTradeCode");
		String v_exp_selectedTradeCode_Desc = k.getLinkText("CCF_SearchedTradeCode_Description");
//		String v_selectedTradeCode_Hazard = k.getLinkText("CCF_SearchedTradeCode");
		customAssert.assertTrue(k.Click("CCF_SearchedTradeCode"), "Unable to click on Searched Trade Code in Trade Code Selection page .");
		
		String a_selectedTradeCode = k.getText("CCF_Gray_PD_TradeCode");
		customAssert.assertEquals(a_selectedTradeCode, v_exp_selectedTradeCode,"Trade Code on Insured Property - Policy Details Screen is incorrect - Expected [ <b>"+v_exp_selectedTradeCode+"</b>] and Actual [<b>"+a_selectedTradeCode+"</b>]  .");
		TestUtil.reportStatus("Trade Code on Insured Property - Policy Details Screen is Correct.", "Pass", true);
			
		String a_selectedTradeCode_desc = k.getText("CCF_Gray_PD_TradeCode_Desc");
		customAssert.assertEquals(a_selectedTradeCode_desc, v_exp_selectedTradeCode_Desc,"Trade Code Description on Insured Property - Policy Details Screen is incorrect- Expected [ <b>"+v_exp_selectedTradeCode+"</b>] and Actual [<b>"+a_selectedTradeCode+"</b>]  . ");
		TestUtil.reportStatus("Trade Code Description on Insured Property - Policy Details Screen is Correct.", "Pass", true);
		
		
		//EML
		customAssert.assertTrue(k.Input("CCF_PoD_EmlAmount_GBP", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_EmlAmount_GBP")),"Unable to enter value in EmlAmount_GBP . ");
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_PoD_EmlAmount_GBP", "value"),"Eml amount (GBP) Field Should Contain Valid Value on Property Details .");
		customAssert.assertTrue(k.Input("CCF_PoD_EmlAmount_Percent", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_EmlAmount_Percent")),"Unable to enter value in Eml amount (%) . ");
		
		//Inner MD-Bespoke Sum Insured
		//customAssert.assertTrue(addMD_BIBespokeSumInsured(map_data), "Error while adding Bespoke data . ");
		List<WebElement> bespoke_MD_btns = k.getWebElements("CCF_Btn_AddBespokeSumIns");
		WebElement MD_bespoke_btn = bespoke_MD_btns.get(0);
		MD_bespoke_btn.click();
		customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("BSI_MD_Description")),"Unable to enter value in Bespoke Description . ");
		customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("BSI_MD_SumInsured")),"Unable to enter value in Bespoke Sum Insured . ");
		customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Bespoke Sum Insured inner page .");
		
		if(((String)common.NB_excel_data_map.get("CD_BusinessInterruption")).equalsIgnoreCase("Yes") ||
				((String)common.NB_excel_data_map.get("CD_Add_BusinessInterruption")).equalsIgnoreCase("Yes")){
			List<WebElement> bespoke_BI_btns = k.getWebElements("CCF_Btn_AddBespokeSumIns");
			WebElement BI_bespoke_btn = bespoke_BI_btns.get(1);
			BI_bespoke_btn.click();
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("BSI_BI_Description")),"Unable to enter value in BI Bespoke Description . ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("BSI_BI_SumInsured")),"Unable to enter value in Bespoke Sum Insured . ");
			customAssert.assertTrue(k.DropDownSelection("CCF_BSI_BI_IndemnityPeriod", common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("BSI_BI_IndemnityPeriod")), "Unable to select value from Indemnity Period dropdown .");
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Bespoke Sum Insured inner page .");
		}
				
		 //String sVal = (String)map_data.get("IP_AddProperty");
		 //String sValue[] = sVal.split(";");
		 //int pCount = sValue.length;
		double finalMDPremium = 0.00, finalBIPremium = 0.00;
		 
		 if(((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equalsIgnoreCase("Yes") || 
				 ((String)common.NB_excel_data_map.get("CD_Add_MaterialDamage")).equalsIgnoreCase("Yes")){
			 customAssert.assertTrue(CommonFunction.PropertyDetails_HandleTables(map_data, "MD", count),"failed in MD handle table");
			 customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
			 
			 finalMDPremium =  finalMDPremium + Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("MD_TotalPremium"));
			 TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_MaterialDamage_NP", String.valueOf(finalMDPremium), common.NB_excel_data_map);
			 
			 if(((String)common.NB_excel_data_map.get("CD_BusinessInterruption")).equalsIgnoreCase("Yes") ||
					 ((String)common.NB_excel_data_map.get("CD_Add_BusinessInterruption")).equalsIgnoreCase("Yes")){
				 customAssert.assertTrue(CommonFunction.PropertyDetails_HandleTables(map_data, "BI", count),"failed in BI handle table");
				 customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
				 
				 finalBIPremium =  finalBIPremium + Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("BI_TotalPremium"));
				 TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_BusinessInterruption_NP", String.valueOf(finalBIPremium), common.NB_excel_data_map);
			 }
		 }
		 
		//customAssert.assertTrue(common.PropertyDetails_HandleTables(NB_excel_data_map, "MD"));
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
}*/


public boolean add_BespokeSumInsured(Map<Object, Object> map_data){
	boolean r_value=true;
	
	try{
		
		int total_count_MM_bespoke = common.no_of_inner_data_sets.get("BS Insured MM");
		int count=0;
		while(count < total_count_MM_bespoke){
			List<WebElement> bespoke_btns = k.getWebElements("ENA_AddBeSpokeSumInsured");
			System.out.println(bespoke_btns.size());
			WebElement MM_bespoke_btn = bespoke_btns.get(0);
			MM_bespoke_btn.click();
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("ENA_BSI_MM_Description", common.NB_Structure_of_InnerPagesMaps.get("BS Insured MM").get(count).get("BSI_MM_Description")),"Unable to enter value in MD Bespoke Description . ");
			customAssert.assertTrue(k.Input("ENA_BSI_MM_SumInsured", common.NB_Structure_of_InnerPagesMaps.get("BS Insured MM").get(count).get("BSI_MM_SumInsured")),"Unable to enter value in Bespoke Sum Insured . ");
			
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Bespoke Sum Insured inner page .");
			count++;
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
		customAssert.assertTrue(k.Input("CCF_BI_UnspecifiedSupplier", Keys.chord(Keys.CONTROL, "a")),"Unable to select Unspecified Supplier(s) Limit field");
		customAssert.assertTrue(k.Input("CCF_BI_UnspecifiedSupplier", (String)map_data.get("BI_UnspecifiedSupplier")),"Unable to enter value in Unspecified Supplier(s) Limit . ");
		customAssert.assertTrue(k.Input("CCF_BI_UnspecifiedCustomer", Keys.chord(Keys.CONTROL, "a")),"Unable to select Unspecified Supplier(s) Limit field");
		customAssert.assertTrue(k.Input("CCF_BI_UnspecifiedCustomer", (String)map_data.get("BI_UnspecifiedCustomer")),"Unable to enter value in Unspecified Supplier(s) Limit . ");
		customAssert.assertTrue(k.Input("CCF_BI_DenialAccess", Keys.chord(Keys.CONTROL, "a")),"Unable to select Denial of Access Limit field");
		customAssert.assertTrue(k.Input("CCF_BI_DenialAccess", (String)map_data.get("BI_DenialAccess")),"Unable to enter value in Denial of Access Limit . ");
		customAssert.assertTrue(k.Input("CCF_BI_PublicUtilities", Keys.chord(Keys.CONTROL, "a")),"Unable to select Public Utilities Limit field");
		customAssert.assertTrue(k.Input("CCF_BI_PublicUtilities", (String)map_data.get("BI_PublicUtilities")),"Unable to enter value in Public Utilities Limit . ");
		customAssert.assertTrue(k.Input("CCF_BI_InfectiousDiseases", Keys.chord(Keys.CONTROL, "a")),"Unable to select Infectious Diseases Limit field");
		customAssert.assertTrue(k.Input("CCF_BI_InfectiousDiseases", (String)map_data.get("BI_InfectiousDiseases")),"Unable to enter value in Infectious Diseases Limit . ");
		customAssert.assertTrue(k.Input("CCF_BI_BookDebts", Keys.chord(Keys.CONTROL, "a")),"Unable to select Book Debts Limit field");
		customAssert.assertTrue(k.Input("CCF_BI_BookDebts", (String)map_data.get("BI_BookDebts")),"Unable to enter value in Book Debts Limit . ");
		
		//Additional Information
		customAssert.assertTrue(k.Input("CCF_BI_AdditionalInfo_CoverReq", Keys.chord(Keys.CONTROL, "a")),"Unable to select Additional information field");
		customAssert.assertTrue(k.Input("CCF_BI_AdditionalInfo_CoverReq", (String)map_data.get("BI_AdditionalInfo_CoverReq")),"Unable to enter value in Additional information . ");
		
		//EML
		customAssert.assertTrue(k.Input("CCF_PoD_EmlAmount_GBP", Keys.chord(Keys.CONTROL, "a")),"Unable to select Eml amount (GBP) field");
		customAssert.assertTrue(k.Input("CCF_PoD_EmlAmount_GBP", (String)map_data.get("BI_EmlAmount")),"Unable to enter value in EmlAmount_GBP . ");
		customAssert.assertTrue(k.Input("CCF_PoD_EmlAmount_Percent", Keys.chord(Keys.CONTROL, "a")),"Unable to select Eml amount (%) field");
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
		if(common.no_of_inner_data_sets.get("SpecifiedSupplier")==null){
			total_count_BI_SpecifiedSupplier = 0;
		}else{
			total_count_BI_SpecifiedSupplier = common.no_of_inner_data_sets.get("SpecifiedSupplier");
		}
		
		int count=0;
		while(count < total_count_BI_SpecifiedSupplier){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddSpecified_Supplier"), "Unable to click on AddSpecified_Supplier Button on BI page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_SS_SupplierName", common.NB_Structure_of_InnerPagesMaps.get("SpecifiedSupplier").get(count).get("SS_SupplierName")),"Unable to enter value in Address field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_SS_SupplierName", "value"),"SupplierName Field Should Contain Valid Value  .");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Address", common.NB_Structure_of_InnerPagesMaps.get("SpecifiedSupplier").get(count).get("SS_SupplierAddress_Line1")),"Unable to enter value in SupplierAddress_Line1 field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"SupplierAddress Field Should Contain Valid Value  .");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line2", common.NB_Structure_of_InnerPagesMaps.get("SpecifiedSupplier").get(count).get("SS_SupplierAddress_Line2")),"Unable to enter value in SupplierAddress field line 2 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line3", common.NB_Structure_of_InnerPagesMaps.get("SpecifiedSupplier").get(count).get("SS_SupplierAddress_Line3")),"Unable to enter value in SupplierAddress field line 3 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Town", common.NB_Structure_of_InnerPagesMaps.get("SpecifiedSupplier").get(count).get("SS_Town")),"Unable to enter value in Town field . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_County", common.NB_Structure_of_InnerPagesMaps.get("SpecifiedSupplier").get(count).get("SS_County")),"Unable to enter value in County  . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode", common.NB_Structure_of_InnerPagesMaps.get("SpecifiedSupplier").get(count).get("SS_Postcode")),"Unable to enter value in PostCode field .");
			customAssert.assertTrue(common.validatePostCode(common.NB_Structure_of_InnerPagesMaps.get("SpecifiedSupplier").get(count).get("SS_Postcode")),"Post Code is not in Correct format .");
			customAssert.assertTrue(k.Input("CCF_SS_SupplierLimit", Keys.chord(Keys.CONTROL, "a")),"Unable to select SupplierLimit field");
			customAssert.assertTrue(k.Input("CCF_SS_SupplierLimit", common.NB_Structure_of_InnerPagesMaps.get("SpecifiedSupplier").get(count).get("SS_SupplierLimit")),"Unable to enter value in SupplierLimit field .");
			
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
		if(common.no_of_inner_data_sets.get("SpecifiedCustomer")==null){
			total_count_BI_SpecifiedCustomer = 0;
		}else{
			total_count_BI_SpecifiedCustomer = common.no_of_inner_data_sets.get("SpecifiedCustomer");
		}
		
		int count=0;
		while(count < total_count_BI_SpecifiedCustomer){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddSpecified_Customer"), "Unable to click on AddSpecified_Customer Button on BI page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_SS_CustomerName", common.NB_Structure_of_InnerPagesMaps.get("SpecifiedCustomer").get(count).get("SC_CustomerName")),"Unable to enter value in Address field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_SS_CustomerName", "value"),"SupplierName Field Should Contain Valid Value  .");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Address", common.NB_Structure_of_InnerPagesMaps.get("SpecifiedCustomer").get(count).get("SC_CustomerAddress_Line1")),"Unable to enter value in CustomerAddress_Line1 field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"SupplierAddress Field Should Contain Valid Value  .");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line2", common.NB_Structure_of_InnerPagesMaps.get("SpecifiedCustomer").get(count).get("SC_CustomerAddress_Line2")),"Unable to enter value in CustomerAddress field line 2 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line3", common.NB_Structure_of_InnerPagesMaps.get("SpecifiedCustomer").get(count).get("SC_CustomerAddress_Line3")),"Unable to enter value in CustomerAddress field line 3 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Town", common.NB_Structure_of_InnerPagesMaps.get("SpecifiedCustomer").get(count).get("SC_Town")),"Unable to enter value in Town field . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_County", common.NB_Structure_of_InnerPagesMaps.get("SpecifiedCustomer").get(count).get("SC_County")),"Unable to enter value in County  . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode", common.NB_Structure_of_InnerPagesMaps.get("SpecifiedCustomer").get(count).get("SC_Postcode")),"Unable to enter value in PostCode field .");
			customAssert.assertTrue(common.validatePostCode(common.NB_Structure_of_InnerPagesMaps.get("SpecifiedCustomer").get(count).get("SC_Postcode")),"Post Code is not in Correct format .");
			customAssert.assertTrue(k.Input("CCF_SS_CustomerLimit", Keys.chord(Keys.CONTROL, "a")),"Unable to select Customer Limit field");
			customAssert.assertTrue(k.Input("CCF_SS_CustomerLimit", common.NB_Structure_of_InnerPagesMaps.get("SpecifiedCustomer").get(count).get("SC_CustomerLimit")),"Unable to enter value in Customer Limit field .");
			
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
		
		
		customAssert.assertTrue(k.Input("CCF_EL_LimitOfIndemnity", Keys.chord(Keys.CONTROL, "a")),"Unable to select LimitOfIndemnity field");
		customAssert.assertTrue(k.Input("CCF_EL_LimitOfIndemnity", (String)map_data.get("EL_LimitOfIndemnity")),"Unable to enter value in LimitOfIndemnity . ");
		
		//Excesses
		customAssert.assertTrue(k.Input("CCF_EL_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select EL Excess field");
		customAssert.assertTrue(k.Input("CCF_EL_Excess", (String)map_data.get("EL_Excess")),"Unable to enter value in EL Excess . ");
		customAssert.assertTrue(k.Input("CCF_EL_MinimumDeposit", Keys.chord(Keys.CONTROL, "a")),"Unable to select Minimum Deposit field");
		customAssert.assertTrue(k.Input("CCF_EL_MinimumDeposit", (String)map_data.get("EL_MinimumDeposit")),"Unable to enter value in Minimum Deposit . ");
				
		//Inner BI-Specified Suppliers
		
		customAssert.assertTrue(addELItems(), "Error while adding EL Items . ");
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Employers Liability .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "EL", "Employers Liability", "EL AddItem");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_EmployersLiability_NP", String.valueOf(sPremiumm), common.NB_excel_data_map);
		 
		TestUtil.reportStatus("Employers Liability details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
	}

public boolean addELItems(){
	boolean r_value=true;
	
	try{
		int total_count_EL_items = 0;
		if(common.no_of_inner_data_sets.get("EL AddItem")==null){
			total_count_EL_items = 0;
		}else{
			total_count_EL_items = common.no_of_inner_data_sets.get("EL AddItem");
		}
		int count=0;
		while(count < total_count_EL_items){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on EL page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", common.NB_Structure_of_InnerPagesMaps.get("EL AddItem").get(count).get("AD_EL_ItemDesc")),"Unable to enter value in Description field. ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", Keys.chord(Keys.CONTROL, "a")),"Unable to select Sum Insured field");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", common.NB_Structure_of_InnerPagesMaps.get("EL AddItem").get(count).get("AD_EL_ItemSumIns")),"Unable to enter value in Sum Insured field. ");
			
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
		
		
		customAssert.assertTrue(k.Input("CCF_PL_IndemnityLimit", Keys.chord(Keys.CONTROL, "a")),"Unable to select PL Indemnity Limit field");
		customAssert.assertTrue(k.Input("CCF_PL_IndemnityLimit", (String)map_data.get("PL_IndemnityLimit")),"Unable to enter value in PL Indemnity Limit . ");
		customAssert.assertTrue(k.Input("CCF_PL_DepositPremium", Keys.chord(Keys.CONTROL, "a")),"Unable to select PL Indemnity Limit field");
		customAssert.assertTrue(k.Input("CCF_PL_DepositPremium", (String)map_data.get("PL_DepositPremium")),"Unable to enter value in PL Indemnity Limit . ");
		customAssert.assertTrue(k.Input("CCF_PL_PropertyDamageExcess", Keys.chord(Keys.CONTROL, "a")),"Unable to select PL Indemnity Limit field");
		customAssert.assertTrue(k.Input("CCF_PL_PropertyDamageExcess", (String)map_data.get("PL_PropertyDamageExcess")),"Unable to enter value in PL Indemnity Limit . ");
		customAssert.assertTrue(k.Input("CCF_PL_HeatWorkAwayExcess", Keys.chord(Keys.CONTROL, "a")),"Unable to select PL Indemnity Limit field");
		customAssert.assertTrue(k.Input("CCF_PL_HeatWorkAwayExcess", (String)map_data.get("PL_HeatWorkAwayExcess")),"Unable to enter value in PL Indemnity Limit . ");
		
		//Inner BI-Specified Suppliers
		customAssert.assertTrue(addPLItems(), "Error while adding PL Items . ");		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Public Liability .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "PL", "Public Liability", "PL AddItem");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_PublicLiability_NP", String.valueOf(sPremiumm), common.NB_excel_data_map);
				
		TestUtil.reportStatus("Public Liability details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
	}

public boolean addPLItems(){
	boolean r_value=true;
	
	try{
		int total_count_PL_items = 0;
		if(common.no_of_inner_data_sets.get("PL AddItem")==null){
			total_count_PL_items = 0;
		}else{
			total_count_PL_items = common.no_of_inner_data_sets.get("PL AddItem");
		}
		int count=0;
		while(count < total_count_PL_items){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on PL page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", common.NB_Structure_of_InnerPagesMaps.get("PL AddItem").get(count).get("AD_PL_ItemDesc")),"Unable to enter value in Description field. ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", Keys.chord(Keys.CONTROL, "a")),"Unable to select Sum Insured field");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", common.NB_Structure_of_InnerPagesMaps.get("PL AddItem").get(count).get("AD_PL_ItemSumIns")),"Unable to enter value in Sum Insured field. ");
			
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
		
		
		customAssert.assertTrue(k.Input("CCF_PRL_IndemnityLimit", Keys.chord(Keys.CONTROL, "a")),"Unable to select PRL Indemnity Limit field");
		customAssert.assertTrue(k.Input("CCF_PRL_IndemnityLimit", (String)map_data.get("PRL_IndemnityLimit")),"Unable to enter value in PRL Indemnity Limit . ");
		customAssert.assertTrue(k.Input("CCF_PL_DepositPremium", Keys.chord(Keys.CONTROL, "a")),"Unable to select PRL DepositPremium field");
		customAssert.assertTrue(k.Input("CCF_PL_DepositPremium", (String)map_data.get("PRL_DepositPremium")),"Unable to enter value in PRL DepositPremium . ");
			
		//Inner BI-Specified Suppliers
		//customAssert.assertTrue(addPRLItems(), "Error while adding PRL Items . ");		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Products Liability .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "PRL", "Product Liability", "PRL AddItem");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_ProductsLiability_NP", String.valueOf(sPremiumm), common.NB_excel_data_map);
				
		TestUtil.reportStatus("Products Liability details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
	}

public boolean addPRLItems(){
	boolean r_value=true;
	
	try{
		int total_count_PL_items = 0;
		if(common.no_of_inner_data_sets.get("PRL AddItem")==null){
			total_count_PL_items = 0;
		}else{
			total_count_PL_items = common.no_of_inner_data_sets.get("PRL AddItem");
		}
		
		int count=0;
		while(count < total_count_PL_items){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on PRL page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", common.NB_Structure_of_InnerPagesMaps.get("PRL AddItem").get(count).get("AD_PLR_ItemDesc")),"Unable to enter value in Description field. ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", Keys.chord(Keys.CONTROL, "a")),"Unable to select Sum Insured field");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", common.NB_Structure_of_InnerPagesMaps.get("PRL AddItem").get(count).get("AD_PLR_ItemSumIns")),"Unable to enter value in Sum Insured field. ");
			
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
		
		customAssert.assertTrue(k.Input("CCF_SAR_AdditionalInfo", Keys.chord(Keys.CONTROL, "a")),"Unable to select SAR_AdditionalInfo field");
		customAssert.assertTrue(k.Input("CCF_SAR_AdditionalInfo", (String)map_data.get("SAR_AdditionalInfo")),"Unable to enter value in SAR_AdditionalInfo . ");
		
		//Inner Add Item
		customAssert.assertTrue(addSARItem(), "Error while adding SAR Item . ");
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "SAR", "Specified All Risks", "SAR AddItem");	
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_SpecifiedAllRisks_NP", String.valueOf(sPremiumm), common.NB_excel_data_map);
		
		TestUtil.reportStatus("Specified All Risks details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
	}

public boolean addSARItem(){
	boolean r_value=true;
	
	try{
		int total_count_SAR_items = 0;
		if(common.no_of_inner_data_sets.get("SAR AddItem")==null){
			total_count_SAR_items = 0;
		}else{
			total_count_SAR_items = common.no_of_inner_data_sets.get("SAR AddItem");
		}
		
		int count=0;
		while(count < total_count_SAR_items){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on SAR page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("AD_SAR_ItemDesc", common.NB_Structure_of_InnerPagesMaps.get("SAR AddItem").get(count).get("AD_SAR_ItemDesc")),"Unable to enter value in Item Description field. ");
			customAssert.assertTrue(k.DropDownSelection("AD_SAR_GeoLimit", common.NB_Structure_of_InnerPagesMaps.get("SAR AddItem").get(count).get("AD_SAR_GeoLimit")),"Unable to Select value in Geographical Limit field. ");
			customAssert.assertTrue(k.Input("AD_SAR_SumIns", Keys.chord(Keys.CONTROL, "a")),"Unable to select Sum Insured field");
			customAssert.assertTrue(k.Input("AD_SAR_SumIns", common.NB_Structure_of_InnerPagesMaps.get("SAR AddItem").get(count).get("AD_SAR_SumInsured")),"Unable to enter value in Sum Insured . ");
			customAssert.assertTrue(k.Input("AD_SAR_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select Excess field");
			customAssert.assertTrue(k.Input("AD_SAR_Excess", common.NB_Structure_of_InnerPagesMaps.get("SAR AddItem").get(count).get("AD_SAR_Excess")),"Unable to enter value in Excess  . ");
			
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
		
		//TradeCode Selection & Verification
		
		if(((String)common.NB_excel_data_map.get("CAR_TCS_TradeCode_Button")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.tradeCodeSelection((String)common.NB_excel_data_map.get("CAR_TradeCode"),"Contractors All Risks",0),"Trade code selection function is having issue(S).");
			/*customAssert.assertTrue(k.Click("CCF_Btn_SelectTradeCode"), "Unable to click on Select Trade Code button in Policy Details .");
			customAssert.assertTrue(common.funcPageNavigation("Trade Code Selection", ""), "Navigation problem to Trade Code Selection page .");
					
			customAssert.assertTrue(k.Input("CCF_TradeCode_SearchBox", (String)map_data.get("CAR_TradeCode")),"Unable to enter value in TradeCode Search field . ");
			k.waitTwoSeconds();
			String v_exp_selectedTradeCode = k.getLinkText("CCF_SearchedTradeCode");
			String v_exp_selectedTradeCode_Desc = k.getLinkText("CCF_SearchedTradeCode_Description");
//			String v_selectedTradeCode_Hazard = k.getLinkText("CCF_SearchedTradeCode");
			customAssert.assertTrue(k.Click("CCF_SearchedTradeCode"), "Unable to click on Searched Trade Code in Trade Code Selection page .");
					
			customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Policy Details .");
					
			String a_selectedTradeCode = k.getText("CCF_Gray_CAR_TradeCode");
			customAssert.assertEquals(a_selectedTradeCode, v_exp_selectedTradeCode,"Trade Code on Policy Details Screen is incorrect - Expected [ <b>"+v_exp_selectedTradeCode+"</b>] and Actual [<b>"+a_selectedTradeCode+"</b>]  .");
			TestUtil.reportStatus("Trade Code on Policy Details Screen is Correct.", "Pass", true);
						
			String a_selectedTradeCode_desc = k.getText("CCF_Gray_CAR_TradeCode_Desc");
			customAssert.assertEquals(a_selectedTradeCode_desc, v_exp_selectedTradeCode_Desc,"Trade Code Description on Policy Details Screen is incorrect- Expected [ <b>"+v_exp_selectedTradeCode+"</b>] and Actual [<b>"+a_selectedTradeCode+"</b>]  . ");
			TestUtil.reportStatus("Trade Code Description on Policy Details Screen is Correct.", "Pass", true);*/
		}
		
		//Inner Add Item
		customAssert.assertTrue(addCARItems(), "Error while adding CAR Item . ");
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
		
		double sPremiumm = CommonFunction.func_CAR_HandleTables( map_data, "CAR", "Contractors All Risks", "CAR AddItem");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_ContractorsAllRisks_NP", String.valueOf(sPremiumm), common.NB_excel_data_map);
		
		TestUtil.reportStatus("Contractors All Risks details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean addCARItems(){
	boolean r_value=true;
	
	try{
		int total_count_CAR_items = 0;
		if(common.no_of_inner_data_sets.get("CAR AddItem")==null){
			total_count_CAR_items = 0;
		}else{
			total_count_CAR_items = common.no_of_inner_data_sets.get("CAR AddItem");
		}
		
		int count=0;
		while(count < total_count_CAR_items){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on CAR page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", common.NB_Structure_of_InnerPagesMaps.get("CAR AddItem").get(count).get("AD_CAR_ItemDesc")),"Unable to enter value in Description field. ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", Keys.chord(Keys.CONTROL, "a")),"Unable to select Sum Insured field");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", common.NB_Structure_of_InnerPagesMaps.get("CAR AddItem").get(count).get("AD_CAR_SumInsured")),"Unable to enter value in Sum Insured field. ");
			
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
		
		customAssert.assertTrue(k.Input("CCF_CER_Erisk_VirusHacking", Keys.chord(Keys.CONTROL, "a")),"Unable to select E Risk: Virus & Hacking field");
		customAssert.assertTrue(k.Input("CCF_CER_Erisk_VirusHacking", (String)map_data.get("CER_Erisk_VirusHacking")),"Unable to enter value in E Risk: Virus & Hacking . ");
		customAssert.assertTrue(k.DropDownSelection("CCF_CER_MaxIndemnityPeriod", (String)map_data.get("CER_MaxIndemnityPeriod")),"Unable to enter value in Maximum Indemnity Period (months) field. ");
		customAssert.assertTrue(k.Input("CCF_CER_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select Excess field");
		customAssert.assertTrue(k.Input("CCF_CER_Excess", (String)map_data.get("CER_Excess")),"Unable to enter value in Excess . ");
	
		customAssert.assertTrue(k.Input("CCF_CER_Computers_SumInsured_tbl", Keys.chord(Keys.CONTROL, "a")),"Unable to select Computers: Sum Insured field");
		customAssert.assertTrue(k.Input("CCF_CER_Computers_SumInsured_tbl", (String)map_data.get("CER_Computers_SumInsured")),"Unable to enter value in Computers: Sum Insured . ");
		customAssert.assertTrue(k.Input("CCF_CER_AdditionalExp_SumInsured_tbl", Keys.chord(Keys.CONTROL, "a")),"Unable to select Additional Expenditure: Sum Insured field");
		customAssert.assertTrue(k.Input("CCF_CER_AdditionalExp_SumInsured_tbl", (String)map_data.get("CER_AdditionalExp_SumInsured")),"Unable to enter value in Additional Expenditure: Sum Insured . ");
			
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Computers and Electronic Risks .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "CER", "Computers and Electronic Risks", "");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_ComputersandElectronicRisks_NP", String.valueOf(sPremiumm), common.NB_excel_data_map);
		
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
	
		customAssert.assertTrue(k.Input("CCF_M_Field_1", Keys.chord(Keys.CONTROL, "a")),"Unable to select Money field 1");
		customAssert.assertTrue(k.Input("CCF_M_Field_1", (String)map_data.get("M_Field_1")),"Unable to enter value in Money field 1 . ");
		customAssert.assertTrue(k.Input("CCF_M_Field_2", Keys.chord(Keys.CONTROL, "a")),"Unable to select Money field 2 ");
		customAssert.assertTrue(k.Input("CCF_M_Field_2", (String)map_data.get("M_Field_2")),"Unable to enter value in Money field 2 . ");
		customAssert.assertTrue(k.Input("CCF_M_Field_3", Keys.chord(Keys.CONTROL, "a")),"Unable to select Money field 3 ");
		customAssert.assertTrue(k.Input("CCF_M_Field_3", (String)map_data.get("M_Field_3")),"Unable to enter value in Money field 3 . ");
		customAssert.assertTrue(k.Input("CCF_M_Field_4", Keys.chord(Keys.CONTROL, "a")),"Unable to select Money field 4 ");
		customAssert.assertTrue(k.Input("CCF_M_Field_4", (String)map_data.get("M_Field_4")),"Unable to enter value in Money field 4 . ");
		
		//Bodily Injury
		customAssert.assertTrue(k.Input("CCF_M_BodilyInjury_Death", Keys.chord(Keys.CONTROL, "a")),"Unable to select Money BodilyInjury_Death ");
		customAssert.assertTrue(k.Input("CCF_M_BodilyInjury_Death", (String)map_data.get("M_BodilyInjury_Death")),"Unable to enter value in Money BodilyInjury_Death . ");
		customAssert.assertTrue(k.Input("CCF_M_LossOfLimbs", Keys.chord(Keys.CONTROL, "a")),"Unable to select Money LossOfLimbs ");
		customAssert.assertTrue(k.Input("CCF_M_LossOfLimbs", (String)map_data.get("M_LossOfLimbs")),"Unable to enter value in Money LossOfLimbs . ");
		customAssert.assertTrue(k.Input("CCF_M_LossOfSight", Keys.chord(Keys.CONTROL, "a")),"Unable to select Money LossOfSight ");
		customAssert.assertTrue(k.Input("CCF_M_LossOfSight", (String)map_data.get("M_LossOfSight")),"Unable to enter value in Money LossOfSight . ");
		customAssert.assertTrue(k.Input("CCF_M_PermanentTotalDis", Keys.chord(Keys.CONTROL, "a")),"Unable to select Money PermanentTotalDis ");
		customAssert.assertTrue(k.Input("CCF_M_PermanentTotalDis", (String)map_data.get("M_PermanentTotalDis")),"Unable to enter value in Money PermanentTotalDis . ");
		customAssert.assertTrue(k.Input("CCF_M_TempTotalDisablement", Keys.chord(Keys.CONTROL, "a")),"Unable to select Money TempTotalDisablement ");
		customAssert.assertTrue(k.Input("CCF_M_TempTotalDisablement", (String)map_data.get("M_TempTotalDisablement")),"Unable to enter value in Money TempTotalDisablement . ");
		
		//Additional Information
		customAssert.assertTrue(k.Input("CCF_M_AdditionalInfo", Keys.chord(Keys.CONTROL, "a")),"Unable to select Money AdditionalInfo ");
		customAssert.assertTrue(k.Input("CCF_M_AdditionalInfo", (String)map_data.get("M_AdditionalInfo")),"Unable to enter value in Money AdditionalInfo . ");
		
		customAssert.assertTrue(k.Input("CCF_M_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select Excess field");
		customAssert.assertTrue(k.Input("CCF_M_Excess", (String)map_data.get("M_Excess")),"Unable to enter value in Excess . ");
	
		//Inner Add Item
		customAssert.assertTrue(addSafe_Money(), "Error while adding Money Safe Item . ");
		
		double sPremiumm = CommonFunction.func_GIT_HandleTables( map_data, "M", "Money", "");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_Money_NP", String.valueOf(sPremiumm), common.NB_excel_data_map);
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Money .");
		
		TestUtil.reportStatus("Money details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean addSafe_Money(){
	boolean r_value=true;
	
	try{
		int total_count_Safe_items = 0;
		if(common.no_of_inner_data_sets.get("M AddSafe")==null){
			total_count_Safe_items = 0;
		}else{
			total_count_Safe_items = common.no_of_inner_data_sets.get("M AddSafe");
		}
		
		int count=0;
		while(count < total_count_Safe_items){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddSafe"), "Unable to click on Add Safe Button on Money page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_M_AS_MakeModelOfSafe", common.NB_Structure_of_InnerPagesMaps.get("M AddSafe").get(count).get("M_AS_MakeModelOfSafe")),"Unable to enter value in Make/Model of Safe field. ");
			customAssert.assertTrue(k.Input("CCF_M_AS_SafeLimit", Keys.chord(Keys.CONTROL, "a")),"Unable to select Safe Limit field");
			customAssert.assertTrue(k.Input("CCF_M_AS_SafeLimit", common.NB_Structure_of_InnerPagesMaps.get("M AddSafe").get(count).get("M_AS_SafeLimit")),"Unable to enter value in Safe Limit field. ");
			
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
		customAssert.assertTrue(k.Input("CCF_GIT_MaxNoOfVehicles", Keys.chord(Keys.CONTROL, "a")),"Unable to select MaxNoOfVehicles ");
		customAssert.assertTrue(k.Input("CCF_GIT_MaxNoOfVehicles", (String)map_data.get("GIT_MaxNoOfVehicles")),"Unable to enter value in MaxNoOfVehicles  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_GIT_Q1", (String)map_data.get("GIT_Q1")), "Unable to Select GIT_Q1 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_GIT_Q2", (String)map_data.get("GIT_Q2")), "Unable to Select GIT_Q2 radio button .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_GIT_Q3", (String)map_data.get("GIT_Q3")), "Unable to Select GIT_Q3 radio button .");
		if(((String)map_data.get("GIT_Q3")).equals("Yes")){
			customAssert.assertTrue(k.SelectRadioBtn("CCF_GIT_Q3_SubQ1", (String)map_data.get("GIT_Q3_SubQ1")), "Unable to Select GIT_Q3_SubQ1 radio button .");
		}
		
		customAssert.assertTrue(k.Input("CCF_GIT_DetailsOfAny_ID_Locks", Keys.chord(Keys.CONTROL, "a")),"Unable to select DetailsOfAny_ID_Locks ");
		customAssert.assertTrue(k.Input("CCF_GIT_DetailsOfAny_ID_Locks", (String)map_data.get("GIT_DetailsOfAny_ID_Locks")),"Unable to enter value in DetailsOfAny_ID_Locks . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_GIT_Q4", (String)map_data.get("GIT_Q4")), "Unable to Select GIT_Q3 radio button .");
		
		//Limit of Liability for Conveyance
		customAssert.assertTrue(k.Input("CCF_GIT_AnyOnePostalPackage", Keys.chord(Keys.CONTROL, "a")),"Unable to select AnyOnePostalPackage ");
		customAssert.assertTrue(k.Input("CCF_GIT_AnyOnePostalPackage", (String)map_data.get("GIT_AnyOnePostalPackage")),"Unable to enter value in AnyOnePostalPackage . ");
		customAssert.assertTrue(k.Input("CCF_GIT_AnyOneConsignment", Keys.chord(Keys.CONTROL, "a")),"Unable to select AnyOneConsignment");
		customAssert.assertTrue(k.Input("CCF_GIT_AnyOneConsignment", (String)map_data.get("GIT_AnyOneConsignment")),"Unable to enter value in AnyOneConsignment . ");
		customAssert.assertTrue(k.Input("CCF_GIT_AnyOneLoss", Keys.chord(Keys.CONTROL, "a")),"Unable to select AnyOneLoss ");
		customAssert.assertTrue(k.Input("CCF_GIT_AnyOneLoss", (String)map_data.get("GIT_AnyOneLoss")),"Unable to enter value in AnyOneLoss . ");
		
		//Schedule of Vehicles  
		//Inner 
		customAssert.assertTrue(addSpecifiedVehicles_GIT(), "Error while adding Specified Vehicles . ");
		
		//Additional Information
		customAssert.assertTrue(k.Input("CCF_GIT_AdditionalInformation", Keys.chord(Keys.CONTROL, "a")),"Unable to select GIT_AdditionalInformation ");
		customAssert.assertTrue(k.Input("CCF_GIT_AdditionalInformation", (String)map_data.get("GIT_AdditionalInformation")),"Unable to enter value in GIT_AdditionalInformation . ");
		customAssert.assertTrue(k.Input("CCF_GIT_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select GIT_Excess ");
		customAssert.assertTrue(k.Input("CCF_GIT_Excess", (String)map_data.get("GIT_Excess")),"Unable to enter value in GIT_Excess . ");
			
				
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Goods In Transit .");
		
		double sPremiumm = CommonFunction.func_GIT_HandleTables( map_data, "GIT", "Goods In Transit", "");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_GoodsInTransit_NP", String.valueOf(sPremiumm), common.NB_excel_data_map);
		
		TestUtil.reportStatus("Goods In Transit details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean addSpecifiedVehicles_GIT(){
	boolean r_value=true;
	
	try{
		int total_count_Spe_Vehicle = 0;
		if(common.no_of_inner_data_sets.get("GIT AddSpecificVehicle")==null){
			total_count_Spe_Vehicle = 0;
		}else{
			total_count_Spe_Vehicle = common.no_of_inner_data_sets.get("GIT AddSpecificVehicle");
		}
		
		int count=0;
		while(count < total_count_Spe_Vehicle){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddSpecifiedVehicle"), "Unable to click on Add AddSpecifiedVehicle Button on GIT page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_GIT_ASV_VehicleDesc", common.NB_Structure_of_InnerPagesMaps.get("GIT AddSpecificVehicle").get(count).get("GIT_ASV_VehicleDesc")),"Unable to enter value in Vehicle Description field. ");
			customAssert.assertTrue(k.Input("CCF_GIT_LOL_PerVehicle", Keys.chord(Keys.CONTROL, "a")),"Unable to select Limit of Liability per Vehicle field");
			customAssert.assertTrue(k.Input("CCF_GIT_LOL_PerVehicle", common.NB_Structure_of_InnerPagesMaps.get("GIT AddSpecificVehicle").get(count).get("GIT_LOL_PerVehicle")),"Unable to enter value in Limit of Liability per Vehicle field. ");
			customAssert.assertTrue(k.Input("CCF_GIT_LOL_PerTrailer", Keys.chord(Keys.CONTROL, "a")),"Unable to select Limit of Liability per Trailer field");
			customAssert.assertTrue(k.Input("CCF_GIT_LOL_PerTrailer", common.NB_Structure_of_InnerPagesMaps.get("GIT AddSpecificVehicle").get(count).get("GIT_LOL_PerTrailer")),"Unable to enter value in Limit of Liability per Trailer field. ");
			
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on GIT-Add Specified Vehicle inner page .");
			count++;
		}
		
	}catch(Throwable t){
		
		r_value=false;
	}
	
	return r_value;
	
}

public boolean funcMarineCargo(Map<Object, Object> map_data){
	
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
	}

public boolean funcFrozenFoods(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Frozen Foods", ""),"Frozen Foods page navigations issue(S)");
		
		
		customAssert.assertTrue(k.Input("CCF_FF_Premises", Keys.chord(Keys.CONTROL, "a")),"Unable to select Premises ");
		customAssert.assertTrue(k.Input("CCF_FF_Premises", (String)map_data.get("FF_Premises")),"Unable to enter value in Premises  . ");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_FF_MaintenanceContract", (String)map_data.get("FF_MaintenanceContract")),"Unable to Select value from MaintenanceContract radio button . ");
		
		//Additional Information
		customAssert.assertTrue(k.Input("CCF_FF_AdditionalInformation", Keys.chord(Keys.CONTROL, "a")),"Unable to select AdditionalInformation ");
		customAssert.assertTrue(k.Input("CCF_FF_AdditionalInformation", (String)map_data.get("FF_AdditionalInformation")),"Unable to enter value in AdditionalInformation  . ");
		
		//Excesses
		customAssert.assertTrue(k.Input("CCF_FF_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select Excess ");
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
	
		customAssert.assertTrue(k.Input("CCF_LOL_Premises", Keys.chord(Keys.CONTROL, "a")),"Unable to select Premises ");
		customAssert.assertTrue(k.Input("CCF_LOL_Premises", (String)map_data.get("LOL_Premises")),"Unable to enter value in Premises  . ");
		customAssert.assertTrue(k.Input("CCF_LOL_DesignatedPremisesSupervisor", Keys.chord(Keys.CONTROL, "a")),"Unable to select DesignatedPremisesSupervisor ");
		customAssert.assertTrue(k.Input("CCF_LOL_DesignatedPremisesSupervisor", (String)map_data.get("LOL_DesignatedPremisesSupervisor")),"Unable to enter value in DesignatedPremisesSupervisor  . ");
		customAssert.assertTrue(k.Input("CCF_LOL_LicencePeriod", Keys.chord(Keys.CONTROL, "a")),"Unable to select LicencePeriod ");
		customAssert.assertTrue(k.Input("CCF_LOL_LicencePeriod", (String)map_data.get("LOL_LicencePeriod")),"Unable to enter value in LicencePeriod  . ");
		customAssert.assertTrue(k.Click("CCF_LOL_LicenceRenewalDate"), "Unable to enter LicenceRenewalDate .");
		customAssert.assertTrue(k.Input("CCF_LOL_LicenceRenewalDate", (String)map_data.get("LOL_LicenceRenewalDate")),"Unable to Enter LicenceRenewalDate .");
		customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
		k.waitTwoSeconds();
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LOL_Ques1", (String)map_data.get("LOL_Ques1")),"Unable to enter value in LOL_Ques1  . ");
		
		//Statement of Fact
		customAssert.assertTrue(k.SelectRadioBtn("CCF_LOL_Ques2", (String)map_data.get("LOL_Ques2")),"Unable to enter value in LOL_Ques2  . ");
			
		//Additional Information
		customAssert.assertTrue(k.Input("CCF_LOL_AdditionalInfo", Keys.chord(Keys.CONTROL, "a")),"Unable to select AdditionalInformation ");
		customAssert.assertTrue(k.Input("CCF_LOL_AdditionalInfo", (String)map_data.get("LOL_AdditionalInfo")),"Unable to enter value in AdditionalInformation  . ");
				
		//Excesses
		customAssert.assertTrue(k.Input("CCF_LOL_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select Excess ");
		customAssert.assertTrue(k.Input("CCF_LOL_Excess", (String)map_data.get("LOL_Excess")),"Unable to enter value in Excess  . ");
						
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Loss of Licence .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "LOL", "Loss of Licence", "");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_BusinessInterruption_NP", String.valueOf(sPremiumm), common.NB_excel_data_map);
		
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
		customAssert.assertTrue(addFidelityGuaranteeItems(),"Error while adding Fidelity Guarantee items .  ");
		
		customAssert.assertTrue(k.Input("CCF_FG_AggLimitOfLiability", Keys.chord(Keys.CONTROL, "a")),"Unable to select AggLimitOfLiability ");
		customAssert.assertTrue(k.Input("CCF_FG_AggLimitOfLiability", (String)map_data.get("FG_AggLimitOfLiability")),"Unable to enter value in AggLimitOfLiability  . ");
		customAssert.assertTrue(k.Input("CCF_FG_MinDepositPremium", Keys.chord(Keys.CONTROL, "a")),"Unable to select MinDepositPremium ");
		customAssert.assertTrue(k.Input("CCF_FG_MinDepositPremium", (String)map_data.get("FG_MinDepositPremium")),"Unable to enter value in MinDepositPremium  . ");
				
		//Excesses
		customAssert.assertTrue(k.Input("CCF_FG_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select Excess ");
		customAssert.assertTrue(k.Input("CCF_FG_Excess", (String)map_data.get("FG_Excess")),"Unable to enter value in Excess  . ");
						
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Fidelity Guarantee .");
		
		double sPremiumm = CommonFunction.func_GIT_HandleTables( map_data, "FG", "Fidelity Guarantee", "FG SOE AddItem");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.NB_excel_data_map.get("Automation Key"), "PS_FidelityGuarantee_NP", String.valueOf(sPremiumm), common.NB_excel_data_map);
		
		TestUtil.reportStatus("Fidelity Guarantee details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}
public boolean addFidelityGuaranteeItems(){
	boolean r_value=true;
	
	try{
		int total_count_FG_items = 0;
		if(common.no_of_inner_data_sets.get("FG SOE AddItem")==null){
			total_count_FG_items = 0;
		}else{
			total_count_FG_items = common.no_of_inner_data_sets.get("FG SOE AddItem");
		}
		
		int count=0;
		while(count < total_count_FG_items){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on Fidelity Guarantee page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_AD_SOE_EmployeeDesc", common.NB_Structure_of_InnerPagesMaps.get("FG SOE AddItem").get(count).get("AD_SOE_EmployeeDesc")),"Unable to enter value in Employee Description field. ");
			customAssert.assertTrue(k.Input("CCF_AD_SOE_LimitOfLiability", Keys.chord(Keys.CONTROL, "a")),"Unable to select Sum Insured field");
			customAssert.assertTrue(k.Input("CCF_AD_SOE_LimitOfLiability", common.NB_Structure_of_InnerPagesMaps.get("FG SOE AddItem").get(count).get("AD_SOE_LimitOfLiability")),"Unable to enter value in LimitOfLiability field. ");
			customAssert.assertTrue(k.Input("CCF_AD_SOE_Wages", Keys.chord(Keys.CONTROL, "a")),"Unable to select Sum Insured field");
			customAssert.assertTrue(k.Input("CCF_AD_SOE_Wages", common.NB_Structure_of_InnerPagesMaps.get("FG SOE AddItem").get(count).get("AD_SOE_Wages")),"Unable to enter value in Wages field. ");
		
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

@SuppressWarnings("static-access")
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
	}

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
}
