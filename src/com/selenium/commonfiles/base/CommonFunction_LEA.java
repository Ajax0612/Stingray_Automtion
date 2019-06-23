package com.selenium.commonfiles.base;

import static com.selenium.commonfiles.util.TestUtil.WriteDataToXl;

import java.awt.AWTException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.selenium.commonfiles.util.ErrorInTestMethod;
import com.selenium.commonfiles.util.TestUtil;

public class CommonFunction_LEA extends TestBase{

	SimpleDateFormat df = new SimpleDateFormat();
	Properties PLEL_Rater = new Properties();
	public int actual_no_of_years=0,err_count=0,trans_error_val=0;
	public Map<String,Double> PL_EL_Rater_output = new HashMap<>();
	public boolean isIncludingHeatPresent = false,isExcludingHeatPresent = false,isIncludeHeat_10M=false;
	public double cent_work_including_heat = 0.0; 
	
public void NewBusinessFlow(String code,String event) throws ErrorInTestMethod{
	String testName = (String)common.NB_excel_data_map.get("Automation Key");
	String navigationBy = CONFIG.getProperty("NavigationBy");
	try{
		
		customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
		customAssert.assertTrue(common.checkClient(common.NB_excel_data_map,code,event),"Unable to check Client.");
		customAssert.assertTrue(common.createNewQuote(common.NB_excel_data_map,code,event), "Unable to create new quote.");
		customAssert.assertTrue(common.selectLatestQuote(common.NB_excel_data_map,code,event), "Unable to select quote from table.");
		customAssert.assertTrue(funcPolicyGeneral(common.NB_excel_data_map,code,event), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcPreviousClaims(common.NB_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
		customAssert.assertTrue(func_Referrals_MaterialFactsDeclerationPage(), "Referrals for MaterialFactsDeclerationPage function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Legal Expenses"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcLegalExpenses(common.NB_excel_data_map,code,event), "Legal Expenses function is having issue(S) . ");
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
		customAssert.assertTrue(common.funcEndorsementOperations(common.NB_excel_data_map),"Insurance tax adjustment is having issue(S).");
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common_VELA.funcPremiumSummary(common.NB_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
		Assert.assertTrue(common_VELA.funcStatusHandling(common.NB_excel_data_map,code,event));
		if(TestBase.businessEvent.equalsIgnoreCase("NB")){
			TestUtil.reportTestCasePassed(testName);
		}
		
	
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}

public void RewindFlow(String code,String event) throws ErrorInTestMethod{
	String testName = (String)common.Rewind_excel_data_map.get("Automation Key");
	try{
		common.currentRunningFlow="NB";
		if(!common.currentRunningFlow.equalsIgnoreCase("Renewal")){
			NewBusinessFlow(code,"NB");
		}
		
		common.currentRunningFlow="Rewind";
		String navigationBy = CONFIG.getProperty("NavigationBy");
		common_VELA.PremiumFlag = false;
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common.funcRewind());
		
		TestUtil.reportStatus("<b> -----------------------Rewind flow is started---------------------- </b>", "Info", false);
		
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
		customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		
		customAssert.assertTrue(funcPolicyGeneral(common.Rewind_excel_data_map,code,event), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcPreviousClaims(common.Rewind_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
		customAssert.assertTrue(func_Referrals_MaterialFactsDeclerationPage(), "Referrals for MaterialFactsDeclerationPage function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Legal Expenses"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcLegalExpenses(common.Rewind_excel_data_map,code,event), "Legal Expenses function is having issue(S) . ");
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common_VELA.funcPremiumSummary(common.Rewind_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
		Assert.assertTrue(common_VELA.funcStatusHandling(common.Rewind_excel_data_map,code,event));
		customAssert.assertEquals(common_VELA.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
		
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}


public void RequoteFlow(String code,String event) throws ErrorInTestMethod{
	if(!common.currentRunningFlow.equalsIgnoreCase("Renewal") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
		NewBusinessFlow(code,"NB");
	}
	common.currentRunningFlow="Requote";
	
	String testName = (String)common.Requote_excel_data_map.get("Automation Key");
	String navigationBy = CONFIG.getProperty("NavigationBy");
	try{
		
		customAssert.assertTrue(common.funcButtonSelection("Re-Quote"));
		
		TestUtil.reportStatus("<b> -----------------------Requote flow is started---------------------- </b>", "Info", false);
		
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
		customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Requote_excel_data_map,code,event,"Submitted"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		
		customAssert.assertTrue(funcPolicyGeneral(common.Requote_excel_data_map,code,event), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcPreviousClaims(common.Requote_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
		customAssert.assertTrue(func_Referrals_MaterialFactsDeclerationPage(), "Referrals for MaterialFactsDeclerationPage function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Legal Expenses"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcLegalExpenses(common.Requote_excel_data_map,code,event), "Legal Expenses function is having issue(S) . ");
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common_VELA.funcPremiumSummary(common.Requote_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
		Assert.assertTrue(common_VELA.funcStatusHandling(common.Requote_excel_data_map,code,event));
		
		customAssert.assertEquals(common_VELA.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
		

	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}

public boolean funcClaimsExperience(Map<Object, Object> map_data){

    boolean retvalue = true;
    
      try {    
    	  	customAssert.assertTrue(common.funcMenuSelection("Navigate", "Claims Experience"),"Unable To navigate to Claims Experience screen");
			customAssert.assertTrue(common.funcPageNavigation("Claims Experience",""), "Claims Experience Page Navigation issue . ");
			TestUtil.reportStatus("varified Claims Experience page .", "Info", true);
			customAssert.assertTrue(common.funcButtonSelection("Next"), "Unable to click on Next Button on Claims Experience .");
			return retvalue;
             
      } catch(Throwable t) {
             String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
          TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
          Assert.fail("Unable to enter details in Previous Claims Page", t);
          return false;
      }


}

public boolean funcPreviousClaims(Map<Object, Object> map_data){

    boolean retvalue = true;
    
      try {    
    		customAssert.assertTrue(common.funcPageNavigation("Previous Claims",""), "Previous Claims Page Navigation issue . ");
			TestUtil.reportStatus("varified Previous Claims page .", "Info", true);
			return retvalue;
             
      } catch(Throwable t) {
             String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
          TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
          Assert.fail("Unable to enter details in Previous Claims Page", t);
          return false;
      }


}

public boolean funcPolicyGeneral(Map<Object, Object> map_data, String code, String event) {
	boolean retVal = true;
	String testName = "";
	try{
		
		//customAssert.assertTrue(common.funcMenuSelection("Navigate", "Policy General"), "Unable to navigate to policy general screen");
		customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");
		customAssert.assertTrue(k.Input("COB_PG_InsuredName", (String)map_data.get("PG_InsuredName")),	"Unable to enter value in Insured Name  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_InsuredName", "value"),"Insured Name Field Should Contain Valid Name  .");
		
		customAssert.assertTrue(k.Input("COB_PG_BusinessEstYear", (String)map_data.get("PG_YearEstablished")),	"Unable to enter value in Business Instablished year  field .");
		customAssert.assertTrue(k.Input("COB_PG_TurnOver", (String)map_data.get("PG_TurnOver")),	"Unable to enter value in Turnover field .");
		
		String[] geographical_Limits = ((String)common.NB_excel_data_map.get("PG_GeoLimit")).split(",");
		List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
		for(String geo_limit : geographical_Limits){
			for(WebElement each_ul : ul_elements){
				customAssert.assertTrue(k.Click("COB_PG_GeoLimit"),"Error while Clicking Geographic Limit List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		
		String[] Prof_Bodies = ((String)common.NB_excel_data_map.get("PG_InsuredDetails_Q1")).split(",");
		WebElement ul1 = driver.findElement(By.xpath("//*[contains(text(),'Trade Associations or Schemes?')]//following::ul[1]"));
		List<WebElement> ul_ele = driver.findElements(By.xpath("//ul"));
		for(String pBodies_limit : Prof_Bodies){
			for(WebElement each_ul : ul_ele){
				ul1.click();
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+pBodies_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+pBodies_limit+"']")).click();
				else
					continue;
				break;
			}
		}
				
		customAssert.assertTrue(k.DropDownSelection("COB_PG_InsuredDetails_Q2", (String)map_data.get("PG_InsuredDetails_Q2")),"Unable to select Second qustion");
		String sVal = (String)map_data.get("PG_InsuredDetails_Q2");
		if(sVal.contains("Yes")){
			customAssert.assertTrue(k.Input("COB_PG_InsuredDetails_details", (String)map_data.get("PG_InsuredDetails_details")),	"Unable to enter value in Provided Details field .");
		}
		
		customAssert.assertTrue(k.DropDownSelection("COB_PG_QuoteValidity", (String)map_data.get("PG_QuoteValidity")),"Unable to select Auote Validity value");
		customAssert.assertTrue(k.DropDownSelection("LEA_PG_leInsurance", (String)map_data.get("PG_leInsurance")),"Unable to select LE Insurance value");
		
		customAssert.assertTrue(k.Input("LEA_PG_BusDesc", (String)map_data.get("PG_BusDesc")),	"Unable to enter value in Business Description field .");
		
		// Select Trade Code :
			if(common.currentRunningFlow.equalsIgnoreCase("NB")){
				String sValue = (String)map_data.get("PG_TCS_TradeCode_Button");
				if(sValue.contains("Yes")){
					customAssert.assertTrue(COB_SelectTradeCode((String)common.NB_excel_data_map.get("PG_TCS_TradeCode") , "Policy Details" , 0),"Trade code selection function is having issue(S).");
				}
				
			// check added trade codes
				String allTradeValues = (String)common.NB_excel_data_map.get("PG_TCS_TradeCode");
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
						val_Trade = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[1]");							
					}else{
						val_Trade = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr/td[1]");
					}
					
					if(allTradeValues.contains(val_Trade)){
						TestUtil.reportStatus(val_Trade + "Tradecode is added and displayed on Policy general screen", "Info", true);
					}
				}
				
			}
			
			
		customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");		
						
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
			customAssert.assertTrue(common.funcPageNavigation("Multi Trade Code Selection", ""), "Navigation problem to Multi Trade Code Selection page .");
			
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

public boolean MaterialFactsDeclerationPage(){
	boolean retValue = true;
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Material Facts & Declarations", ""),"Material Facts & Declarations page is having issue(S)");
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
		 
		 
		 
		 
		/* String[] geographical_Limits = ((String)common.NB_excel_data_map.get("MFD_PLMF")).split(",");
		 customAssert.assertTrue(k.Click("MFD_PLMF"),"Error while Clicking Geographic Limit List object . ");
		 List<WebElement> _prd_liability_MF = driver.findElements(By.xpath("//html//body//span//span//following::ul//li"));	
		 	for(String geo_limit : geographical_Limits){
				for(WebElement each_ul : _prd_liability_MF){
					//customAssert.assertTrue(k.Click("MFD_PLMF"),"Error while Clicking Geographic Limit List object . ");
					//each_ul.click();
					k.waitTwoSeconds();//*[text()=
					System.out.println(each_ul.findElement(By.xpath("//*[text()='"+geo_limit+"']")).isDisplayed());
					if(each_ul.findElement(By.xpath("//*[text()='"+geo_limit+"']")).isDisplayed())
						each_ul.findElement(By.xpath("//*[text()='"+geo_limit+"']")).click();
					else
						continue;
					break;
				}
			}
		 */
				 
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

public void MTAFlow(String code,String fileName) throws ErrorInTestMethod{
	String testName = (String)common.MTA_excel_data_map.get("Automation Key");
	try{
		NewBusinessFlow(code,"NB");
		common.currentRunningFlow="MTA";
		System.out.println("Test method of MTA For - "+code);
		System.out.println("MTA data Map - "+common.MTA_excel_data_map);
		System.out.println("MTA inner data Map - "+common.MTA_Structure_of_InnerPagesMaps);
		customAssert.assertTrue(common.createEndorsement(common.MTA_excel_data_map),"Issue while Navigating to Terrorism screen . ");
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
		customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
//		customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,code,TestBase.businessEvent,"Endorsement Submitted"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		customAssert.assertTrue(common.decideMTAMethod());
		Assert.assertTrue(common.funcStatusHandling(common.MTA_excel_data_map,code,TestBase.businessEvent));
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
	//	customAssert.assertTrue(common.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", code, TestBase.businessEvent), "Transaction Summary function is having issue(S) . ");
		
		
		TestUtil.reportStatus("Test Method of MTA For - "+code, "Pass", true);
		
		
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
 * This method handles all Cancellation Cases for SPI product.
 * 
 */
public void CancellationFlow(String code,String event) throws ErrorInTestMethod{
	
	String testName = (String)common.CAN_excel_data_map.get("Automation Key");
	try{
		NewBusinessFlow(code,"NB");
		//		System.out.println("Test method of CAN For - "+code);
//		System.out.println("CAN data Map - "+common.CAN_excel_data_map);
//		System.out.println("CAN inner data Map - "+common.CAN_Structure_of_InnerPagesMaps);
// 
		//customAssert.assertTrue(CancellationProcess(common.NB_excel_data_map,code,event),"Failed in Cancellation Process Function");
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


//************************START*******************************//
//************************************************************//
//**** LE Book Premium Calculator  ***************************//
//************************************************************//
//************************************************************//

//Legal Expense Book Premium Calculation
public double get_LE_Book_Premium(String LE_LOI,String LE_turnover){
	
	
	double book_premium=0.0,LOI_rate_multiplier=0.0;
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
			book_premium = 0.0;
		}else if(_40_Cent_of_actual_turnover.equalsIgnoreCase("Yes")){ //Referral Rule - 6
			book_premium = 0.0;
		}else if(turnOver > 25000000){ //Referral Rule - 1
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

	

//************************END*********************************//
//************************************************************//
//**** LE Book Premium Calculator ****************************//
//************************************************************//
//************************************************************//
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
		 customAssert.assertTrue(common.funcPageNavigation("Material Facts & Declarations", ""),"Material Facts and Declarations page is having issue(S)");
		 k.ImplicitWaitOff();
		 String mfd_q_value = null,mf_key="RM_MaterialFactsandDeclarations_";
		 
		 try{
		 //In the last 3 years has the proposer, director or partner of the Trade or Business or its Subsidiary Companies ever, either personally or in any business capacity, suffered any loss, made any claims or been involved in any incident or circumstances which may give rise to a claim in respect of the risks proposed?
		 mfd_q_value = k.GetDropDownSelectedValue("LEA_MFD_ClaimsHistory");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"ClaimsHistory"));
		 }catch(Throwable t){
			 }
		 
		 try{
		 //Has any proposer, director or partner of the Trade or Business or its Subsidiary Companies ever, either personally or in any business capacity, had a proposal refused or declined or claim repudiated or ever had an insurance cancelled, renewal refused or had special terms imposed?
		 mfd_q_value = k.GetDropDownSelectedValue("LEA_MFD_specialTermImposed");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"specialTermImposed"));
		 }catch(Throwable t){
		 }
		 
		 try{
		 //Has any proposer, director or partner of the Trade or Business or its Subsidiary Companies ever, either personally or in any business capacity had any convictions, criminal offences or prosecutions pending other than motor offences? 
		 mfd_q_value = k.GetDropDownSelectedValue("LEA_MFD_motorOffences");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"motorOffences"));
		 }catch(Throwable t){
		 }
		 
		 try{
		 //Has any proposer, director or partner of the Trade or Business or its Subsidiary Companies ever, either personally or in any business capacity been declared bankrupt or insolvent or been the subject of bankruptcy proceedings or receivership/ insolvency proceedings?
		 mfd_q_value = k.GetDropDownSelectedValue("LEA_MFD_insolvencyProceedings");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"insolvencyProceedings"));
		 }catch(Throwable t){
		 }
		 
		 try{
		 //Has any proposer, director or partner of the Trade or Business or its Subsidiary Companies ever been disqualified from holding company directorship?
		 mfd_q_value = k.GetDropDownSelectedValue("LEA_MFD_holdingCompanyDirectorship");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"holdingCompanyDirectorship"));
		 }catch(Throwable t){
		 }
			
		 try{
		 //Have you been involved in any disputes or legal proceedings during the last 5 years? (whether they formed part of a Legal Expenses Insurance claim or not) 
		 mfd_q_value = k.GetDropDownSelectedValue("LEA_MFD_legalProceedingsDuring5Years");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"legalProceedingsDuring5Years"));
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


public boolean funcLegalExpenses(Map<Object, Object> map_data,String code,String event){
	
	boolean r_value= true;
	String abvr = null, coverName = null;
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Legal Expenses", ""),"Legal Expenses page navigations issue(S)");
		if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
        	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	    }
		if(((String)map_data.get("LE_ContractDisputes")).equals("Yes")){ 
			if(k.chkboxSelection("COB_LE_ContractDisputes")== false){
				customAssert.assertTrue(k.Click("COB_LE_ContractDisputes"), "Unable to select value from COB_LE_ContractDisputes Checkbox .");
		}
			}
		customAssert.assertTrue(k.DropDownSelection("COB_LE_lOI", (String)map_data.get("LE_lOI")), "Unable to select value from LE_lOI dropdown .");
		customAssert.assertTrue(k.Input("COB_LE_Turnover", (String)map_data.get("LE_Turnover")),"Unable to enter value in Turnover  . ");
		customAssert.assertTrue(k.Input("COB_LE_Average", (String)map_data.get("LE_Average")),"Unable to enter value in LE_Average  . ");
		customAssert.assertTrue(k.Input("COB_LE_Largest", (String)map_data.get("LE_Largest")),"Unable to enter value in LE_Largest  . ");
		customAssert.assertTrue(k.DropDownSelection("COB_LE_Contract_Represent", (String)map_data.get("LE_ContractRepresentsMoreThan40")), "Unable to select value from LE_lOI dropdown .");
		if(((String)map_data.get("LE_ContractRepresentsMoreThan40")).equalsIgnoreCase("Yes")){
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
			
		 customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, sTablePath, sCover, abvr),"Error in Auto Rated premium Calculation for Legal Expenses screen . ");
			 
			 
		 

		TestUtil.reportStatus("Legal Expenses details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean funcValidate_AutoRatedTables(Map<Object, Object> map_data, String s_TablePath, String s_CoverName, String s_Abvr){
	boolean retValue = true;
	String s_Section = null, sVal = null, s_ColName = null, s_InnerSheetName = null, i_abvr = null, s_SheetName = null;
	int totalCols = 0, totalRows = 0, InnerCount = 0;
	String sRater = null;
	
	double s_Wages = 0.00, s_BookRate = 0.00, s_BookP = 0.00, s_TechAdjust = 0.00, s_CommAdjust = 0.00, s_Premium = 0.00;
	double c_Wages = 0.00, c_BookRate = 0.00, c_BookP = 0.00, c_TechAdjust = 0.00, c_CommAdjust = 0.00, c_Premium = 0.00, c_TotalP = 0.00;
	double ad_Rate = 0.00;
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
			
			WebElement s_table= driver.findElement(By.xpath(s_TablePath));
			totalCols = s_table.findElements(By.tagName("th")).size(); 
			totalRows = s_table.findElements(By.tagName("tr")).size();
			String policy_status_actual = k.getText("Policy_status_header");
			String code = "";
			s_InnerSheetName = "AddBespoke"+s_Abvr.replace("_", "");
			i_abvr = s_Abvr+"AddB_";
			if(policy_status_actual.contains("Rewind")){
				code = "";
			}else if(common_VELA.quoteStatus.contains("ReQuote")){
				code = "";
			}else{
				code = "";
			}
				
			s_SheetName = s_CoverName;		
			
			// Enter Data on screen :
					switch(s_CoverName){
					
						
						case "Legal Expenses":
							sRater = "LE";
							String innerSheetName = null;
							for(int i = 0; i< totalRows-2; i++){
								s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]/td[2]");
								
								if(s_Section.contains("Legal Expenses (BASE LEVEL COVER)")){
									
									s_ColName = s_Section.replace("LE_BASE_LEVEL_COVER", "");
									sVal = (String)map_data.get(s_ColName +"TechAdjust");
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys((String)map_data.get("LE_"+code+"BASE_LEVEL_COVER_TechAdjust"));
									
									sVal = (String)map_data.get(s_ColName +"PremiumOverride");
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys((String)map_data.get("LE_"+code+"BASE_LEVEL_COVER_PremiumOverride"));
									
								
									
									InnerCount = InnerCount + 1;									
								}else{
									String s_Bespoke = (String)map_data.get("LE_AddBespoke");
									String AllBespoke[] =  s_Bespoke.split(";");
									innerSheetName = AllBespoke[0].replace("_01", "");
									int j;
									for(j=0; j<AllBespoke.length; j++){
										if(s_Section.equals((String)internal_data_map.get(innerSheetName).get(j).get("LE_"+code+"AddB_Description"))){
											break;
										}
									}
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[6]/input")).sendKeys((String)internal_data_map.get(innerSheetName).get(j).get("LE_"+code+"TechAdjustment"));
									
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
									driver.findElement(By.xpath(s_TablePath + "/tbody/tr["+(i+1)+"]/td[8]/input")).sendKeys((String)internal_data_map.get(innerSheetName).get(j).get("LE_"+code+"PremiumOverride"));
								}
							}
							break;	
						
					}
				
			
				 customAssert.assertTrue(k.Click("COB_Btn_ApplyBookRate"),"Unable to click on ApplyBookRate button");
		
	 
		 
			
			 // Read data from Screen and compare :
			 
			 InnerCount = 0;
			 switch(s_CoverName){
				
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
							CommonFunction_VELA.compareValues(Double.parseDouble((String)map_data.get("LE_"+code+"Turnover")), Double.parseDouble(turnover), "TurnOver for Legal Expse Base Cover");
							CommonFunction_VELA.compareValues(Double.parseDouble(((String)map_data.get("LE_"+code+"lOI")).replace(",", "")), Double.parseDouble(LOI.replace(",", "")), "Limi of Indemnity for Legal Expse Base Cover");	
							if(Double.parseDouble(premiumOverride)==0){
								double calclatedbookPremium = get_LE_Book_Premium(LOI,turnover);
								CommonFunction_VELA.compareValues(calclatedbookPremium, Double.parseDouble(BookPremium), "Book Premium for Legal Expse Base Cover");
								calculatedpremium = calclatedbookPremium +(calclatedbookPremium* Double.parseDouble((String)map_data.get("LE_BASE_LEVEL_COVER_TechAdjust"))/100.0);
								CommonFunction_VELA.compareValues(calculatedpremium, Double.parseDouble(premium), "Premium for Legal Expse Base Cover");
							}else{
								CommonFunction_VELA.compareValues(Double.parseDouble((String)map_data.get("LE_"+code+"BASE_LEVEL_COVER_PremiumOverride")), Double.parseDouble(premium), "Premium for Legal Expenses Base Cover");
							}
							
							calTotalPremium = calTotalPremium + Double.parseDouble(premium);
						}else{
							String s_Bespoke = (String)map_data.get("LE_AddBespoke");
							String AllBespoke[] =  s_Bespoke.split(";");
							innerSheetName = AllBespoke[0].replace("_01", "");
							int j;
							for(j=0; j<AllBespoke.length; j++){
								if(s_Section.equals((String)internal_data_map.get(innerSheetName).get(j).get("LE_"+code+"AddB_Description"))){
									break;
								}
							}
							CommonFunction_VELA.compareValues(Double.parseDouble(BookPremium), Double.parseDouble((String)internal_data_map.get(innerSheetName).get(j).get("LE_"+code+"AddB_BookPremium")), "Book Premium for Legal Expenses All Bespoke"+AllBespoke[j]);
							
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
				
			String AllBespoke[] =  s_Bespoke.split(";");
			innerSheetName = AllBespoke[0].replace("_01", "");
			
			for(int i = 0; i < AllBespoke.length; i++){
				
				if(s_CoverName.contains("POL")){
					customAssert.assertTrue(k.Click("COB_POB_Btn_AddBespokeItem"),"Unable to click on AddBespokeItem button");
				}else{
					customAssert.assertTrue(k.Click("COB_Btn_AddBespokeItem"),"Unable to click on AddBespokeItem button");
				}
				
				switch(s_CoverName){
					case "JCT" :						
						if(k.isDisplayed("COB_JCT_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_JCT_AddB_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
						
					case "Ter" :
						if(k.isDisplayed("COB_HCP_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_HCP_AddB_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
						
					case "HCP" :
						if(k.isDisplayed("COB_HCP_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_HCP_AddB_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
						
					case "POL" :
						if(k.isDisplayed("COB_POL_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_POL_AddB_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
					case "LE" : 
						if(k.isDisplayed("COB_LE_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_LE_AddB_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_LE_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_LE_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
						
					case "SP" :
					case "AW" :
						//customAssert.assertTrue(k.DropDownSelection("COB_AddB_Additionalcover", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")),"Unable to put value in Additionalcover field");
						if(s_CoverName.equalsIgnoreCase("SP"))
							customAssert.assertTrue(k.DropDownSelection("COB_AddB_Additionalcover", (String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")),"Unable to put value in Additionalcover field");
						else{
							WebElement el = driver.findElement(By.xpath("//*[@class='subpage']//*[text()='Additional Cover']//following::select[1]"));
							Select sl = new Select(el);
							sl.selectByValue((String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover"));
						}
						
						//if(k.isDisplayed("COB_AddB_SP_TypeOfLimit")){
							//customAssert.assertTrue(k.DropDownSelection("COB_AddB_SP_TypeOfLimit", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"TypeOfLimit")),"Unable to put value in TypeOfLimit field");
						//}
						
						if(k.isDisplayed("COB_AddB_SP_LOI")){
							customAssert.assertTrue(k.Input("COB_AddB_SP_LOI",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"LOI")),"Unable to put value in LOI field");
						}		
						
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						
						String AddCov = (String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover");
						if(AddCov.contains("Other")){
							customAssert.assertTrue(k.Input("COB_AddB_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");							
						}
						
						break;
						
					case "OP" :
						customAssert.assertTrue(k.DropDownSelection("COB_AddB_Additionalcover", (String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")),"Unable to put value in Additionalcover field");
						
						//if(k.isDisplayed("COB_AddB_SP_TypeOfLimit")){
							//customAssert.assertTrue(k.DropDownSelection("COB_AddB_SP_TypeOfLimit", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"TypeOfLimit")),"Unable to put value in TypeOfLimit field");
						//}
						
						if(k.isDisplayed("COB_AddB_OP_BespokeLOI")){
							customAssert.assertTrue(k.Input("COB_AddB_OP_BespokeLOI",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"LOI")),"Unable to put value in LOI field");
						}		
						
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						
						AddCov = (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover");
						if(AddCov.contains("Other")){
							customAssert.assertTrue(k.Input("COB_AddB_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");							
						}
						
						break;
						
					case "HIP" :
						
						customAssert.assertTrue(k.DropDownSelection("COB_AddB_Additionalcover", (String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")),"Unable to put value in Additionalcover field");
						
						//if(k.isDisplayed("COB_AddB_HIP_TypeOfLimit")){
							//customAssert.assertTrue(k.DropDownSelection("COB_AddB_HIP_TypeOfLimit", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"TypeOfLimit")),"Unable to put value in TypeOfLimit field");
						//}
						
						if(k.isDisplayed("COB_AddB_LOI")){
							customAssert.assertTrue(k.Input("COB_AddB_LOI",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"LOI")),"Unable to put value in LOI field");
						}
						
						if(k.isDisplayed("COB_AddB_HIP_Description")){
							customAssert.assertTrue(k.Input("COB_AddB_HIP_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
						
					default :	
						customAssert.assertTrue(k.DropDownSelection("COB_AddB_Additionalcover", (String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")),"Unable to put value in Additionalcover field");
						
						if(k.isDisplayed("COB_AddB_TypeOfLimit")){
							customAssert.assertTrue(k.DropDownSelection("COB_AddB_TypeOfLimit", (String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"TypeOfLimit")),"Unable to put value in TypeOfLimit field");
						}
						
						if(k.isDisplayed("COB_AddB_LOI")){
							customAssert.assertTrue(k.Input("COB_AddB_LOI",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"LOI")),"Unable to put value in LOI field");
						}
						
						if(k.isDisplayed("COB_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_AddB_Description",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_AddB_BookPremium",(String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
						
						if(s_CoverName.equalsIgnoreCase("OP")){
							//Referral code
								if(((String)internal_data_map.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")).equalsIgnoreCase("Subrogation Waiver"));
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

public boolean funcRewindOperation() {
	try{
		String navigationBy = CONFIG.getProperty("NavigationBy");
		
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Legal Expenses"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcLegalExpensesRewind(common.NB_excel_data_map,TestBase.product,CommonFunction.businessEvent), "Legal Expenses function is having issue(S) . ");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common_VELA.funcPremiumSummary(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Premium Summary function is having issue(S) . ");
		return true;
	}
	catch(Throwable t){
		return false;
	}
}

public boolean funcLegalExpensesRewind(Map<Object, Object> map_data,String code,String event){
	
	boolean r_value= true;
	String abvr = null, coverName = null;
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Legal Expenses", ""),"Legal Expenses page navigations issue(S)");
		customAssert.assertTrue(k.DropDownSelection("COB_LE_lOI", (String)map_data.get("LE_Rewind_lOI")), "Unable to select value from LE_lOI dropdown .");
		customAssert.assertTrue(k.Input("COB_LE_Turnover", (String)map_data.get("LE_Rewind_Turnover")),"Unable to enter value in Turnover  . ");
		customAssert.assertTrue(k.Input("COB_LE_Average", (String)map_data.get("LE_Rewind_Average")),"Unable to enter value in LE_Average  . ");
		customAssert.assertTrue(k.Input("COB_LE_Largest", (String)map_data.get("LE_Rewind_Largest")),"Unable to enter value in LE_Largest  . ");
		customAssert.assertTrue(k.DropDownSelection("COB_LE_Contract_Represent", (String)map_data.get("LE_Rewind_ContractRepresentsMoreThan40")), "Unable to select value from LE_lOI dropdown .");
		if(((String)map_data.get("LE_Rewind_ContractRepresentsMoreThan40")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(k.Type("COB_LE_Contract_Represent_Details", (String)map_data.get("LE_Rewind_ContractRepresentsMoreThan40Details")),"Unable to enter value in COB_LE_Contract_Represent_Details  . ");
		}
		String sVal = (String)map_data.get("LE_Rewind_AddBespoke");
		 if(sVal.length() > 0){
			 abvr = "LE_Rewind_AddB_";
			 coverName = "LE";
			 customAssert.assertTrue(funcDeleteAddBespoke(map_data, sVal, abvr,coverName),"Unable to add bspoke item for PPL cover");
		
		 }
		 customAssert.assertTrue(k.Click("COB_Btn_ApplyBookRate"),"Unable to click on ApplyBookRates button");
		 
		    String sUniqueCol ="Description";
			int tableId = 0;
			
			String sTablePath = "html/body/div[3]/form/div/table";
			
			tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
			sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
			String sCover = "Legal Expenses";
			
		 customAssert.assertTrue(funcValidate_AutoRatedTables(map_data, sTablePath, sCover, abvr),"Error in Auto Rated premium Calculation for Legal Expenses screen . ");

		TestUtil.reportStatus("Legal Expenses details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean funcDeleteAddBespoke(Map<Object, Object> map_data, String s_Bespoke, String s_Abvr, String s_CoverName){
	boolean retValue = true;
	String innerSheetName = null;
	
	try{
				
			String AllBespoke[] =  s_Bespoke.split(";");
			innerSheetName = AllBespoke[0].replace("_01", "");
			
			//This will delete existing bespoke items.
			WebElement AddBespoketable = driver.findElement(By.xpath("//*[@class='std']/tbody"));
			List<WebElement> rows = AddBespoketable.findElements(By.tagName("tr"));
			int size = rows.size();
			while (size!=0){
				AddBespoketable.findElement(By.xpath("//tr[1]//a[2]")).click();
				k.AcceptPopup();
				try{
					k.ImplicitWaitOff();
					AddBespoketable = driver.findElement(By.xpath("//*[@class='std']/tbody"));
					rows = AddBespoketable.findElements(By.tagName("tr"));
					size = rows.size();
				}catch(Throwable t){
					size = 0;
					k.ImplicitWaitOn();
				}finally {
					k.ImplicitWaitOn();
				}
			}
			
			
			/*for(int j=0;j<rows.size();j++){
				AddBespoketable.findElement(By.xpath("//tr[1]//a[2]")).click();
				k.AcceptPopup();
				AddBespoketable = driver.findElement(By.xpath("//*[@class='std']/tbody"));
				rows = AddBespoketable.findElements(By.tagName("tr"));
				j=0;
			}*/
			
			
			
			for(int i = 0; i < AllBespoke.length; i++){
				
				if(s_CoverName.contains("POL")){
					customAssert.assertTrue(k.Click("COB_POB_Btn_AddBespokeItem"),"Unable to click on AddBespokeItem button");
				}else{
					customAssert.assertTrue(k.Click("COB_Btn_AddBespokeItem"),"Unable to click on AddBespokeItem button");
				}
				
				switch(s_CoverName){
					case "LE" : 
						if(k.isDisplayed("COB_LE_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_LE_AddB_Description",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("COB_LE_AddB_BookPremium")){
							customAssert.assertTrue(k.Input("COB_LE_AddB_BookPremium",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"BookPremium")),"Unable to put value in BookPremium field");
						}
					break;
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

}
