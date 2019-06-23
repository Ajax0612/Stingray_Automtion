package com.selenium.commonfiles.base;

import static com.selenium.commonfiles.util.TestUtil.WriteDataToXl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class CommonFunction_XOG extends TestBase{

	SimpleDateFormat df = new SimpleDateFormat();
	Properties PLEL_Rater = new Properties();
	public int actual_no_of_years=0,err_count=0,trans_error_val=0, Can_returnP_Error=0;
	public String MTARewindFlow = "", Endorse_Date = "";
	public Map<String,Double> Book_rate_Rater_output = new HashMap<>();
	public boolean isIncludingHeatPresent = false,isExcludingHeatPresent = false,isIncludeHeat_10M=false;
	public double cent_work_including_heat = 0.0; 
	
	public Map<String,Map<String,Double>> CAN_XOC_ReturnP_Values_Map = new HashMap<>();
	
public void NewBusinessFlow(String code,String event) throws ErrorInTestMethod{
	String testName = (String)common.NB_excel_data_map.get("Automation Key");
	String navigationBy = CONFIG.getProperty("NavigationBy");
	try{
		
		customAssert.assertTrue(common.StingrayLogin("VELA"),"Unable to login.");
		customAssert.assertTrue(common.checkClient(common.NB_excel_data_map,code,event),"Unable to check Client.");
		customAssert.assertTrue(common.createNewQuote(common.NB_excel_data_map,code,event), "Unable to create new quote.");
		customAssert.assertTrue(common.selectLatestQuote(common.NB_excel_data_map,code,event), "Unable to select quote from table.");
		customAssert.assertTrue(funcPolicyGeneral(common.NB_excel_data_map,code,event), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(funcPreviousClaims(common.NB_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(common_VELA.funcCovers(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"General Risk Details"),"Issue while Navigating to General Risk Details screen . ");
		customAssert.assertTrue(common_XOC.GeneralRiskDetailsPage(common.NB_excel_data_map));
		if(((String)common.NB_excel_data_map.get("CD_ThirdPartyMotorLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Third Party Motor Liability"),"Issue while Navigating to Third Party Motor Liability screen . ");
			customAssert.assertTrue(common_XOC.ThirdPartyMotorLiability(common.NB_excel_data_map), "Issue with  Third Party Motor Liability function");
		}
		if(((String)common.NB_excel_data_map.get("CD_CombinedLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Combined Liability"),"Issue while Navigating to Combined Liabilit screen . ");
			customAssert.assertTrue(common_XOC.CombinedLiability(common.NB_excel_data_map), "Issue with  CombinedLiability function");
		}
		if(((String)common.NB_excel_data_map.get("CD_Public&ProductsLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen . ");
			customAssert.assertTrue(common_XOC.PublicProductsLiabilityPage(common.NB_excel_data_map), "Issue with  Public & Products Liability function");
		}
		if(((String)common.NB_excel_data_map.get("CD_EmployersLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.EmployersLiabilityPage(common.NB_excel_data_map), "Issue with  Employers Liability function");
		}
		if(((String)common.NB_excel_data_map.get("CD_JCT6.5.1")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.JCTPage(common.NB_excel_data_map), "Issue with  Employers Liability function");
		}
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
		customAssert.assertTrue(common.funcEndorsementOperations(common.NB_excel_data_map),"Endorsement is having issue(S).");
			
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_VELA.funcPremiumSummary(common.NB_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			Assert.assertTrue(common_VELA.funcStatusHandling(common.NB_excel_data_map,code,event));
			customAssert.assertEquals(common_VELA.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
			if(!TestBase.businessEvent.equals("NB")){
				customAssert.assertEquals(common_VELA.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
				customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
			}
			//customAssert.assertTrue(common_VELA.funcPremiumSummary(common.NB_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			TestUtil.reportTestCasePassed(testName);
			
//			customAssert.assertTrue(common.funcEndorsementOperations(common.NB_excel_data_map),"Insurance tax adjustment is having issue(S).");
//			customAssert.assertTrue(common.verifyEndorsementONPremiumSummary(common.NB_excel_data_map),"Insurance tax adjustment is having issue(S).");
			
			
			/*driver.navigate().to("http://206.142.240.56/stingray/ctl/web/main.jsp?RID=307109&action=next&THIS=p22&PAGE=p1");
			customAssert.assertTrue(common_VELA.insuranceTaxAdjustmentHandling(code,event),"Insurance tax adjustment is having issue(S).");
			*/
			
		
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}

public boolean MaterialFactsDeclerationPage(){
	boolean retValue = true;
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Material Facts & Declarations", ""),"Material Facts and Declarations page is having issue(S)");
		 k.ImplicitWaitOff();
		 String q_value = null;
		 
		List<WebElement> elements = driver.findElements(By.className("selectinput"));
		 Select sel = null;
		 String sInput = null;
		
		 for(int i = 0;i<elements.size();i++){
			 if(elements.get(i).isDisplayed()){
				 if(elements.get(i).getAttribute("name").contains("_proposer_")){
					 sel = new Select(elements.get(i));
					 try{
						 
						 q_value = (String)common.NB_excel_data_map.get("MFD_Q"+(i+1));
						 
						 sel.selectByVisibleText(q_value);
					 }
					 catch(Throwable t){
						 //sel.selectByVisibleText("No");
					 } 
				 }else if(elements.get(i).getAttribute("name").contains("_work")){
					 sel = new Select(elements.get(i));
					 try{
						 sInput = elements.get(i).getAttribute("name");
						 q_value = (String)common.NB_excel_data_map.get(sInput);
						 sel.selectByVisibleText(q_value);
					 }
					 catch(Throwable t){
						 //sel.selectByVisibleText("No");
					 } 
				 }else {
					 sel = new Select(elements.get(i));
					 try{
						 sInput = elements.get(i).getAttribute("name");
						 q_value = (String)common.NB_excel_data_map.get(sInput);
						 sel.selectByVisibleText(q_value);
					 }
					 catch(Throwable t){
						 //sel.selectByVisibleText("No");
					 } 
				 }
		 }
		 }	 
		 ////*[contains(@name,'_work')]
		
		  
		 
//		if(((String)common.NB_excel_data_map.get("MFD_Q10")).equalsIgnoreCase("Yes")){
//			 String[] Industries = ((String)common.NB_excel_data_map.get("MFD_Industries")).split(",");
//				List<WebElement> ul_elements_Industries = driver.findElements(By.xpath("//ul"));
//				for(String geo_limit : Industries){
//					for(WebElement each_ul : ul_elements_Industries){
//						customAssert.assertTrue(k.Click("XOC_MFD_Industries"),"Error while Clicking Geographic Limit List object . ");
//						k.waitTwoSeconds();
//						if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
//							each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
//						else
//							continue;
//						break;
//					}
//				}
//		 }
//		 if(((String)common.NB_excel_data_map.get("MFD_Q12")).equalsIgnoreCase("Yes")){
//			 String[] Regoins = ((String)common.NB_excel_data_map.get("MFD_Regoins")).split(",");
//				List<WebElement> ul_elements_Regoins = driver.findElements(By.xpath("//ul"));
//				for(String geo_limit : Regoins){
//					for(WebElement each_ul : ul_elements_Regoins){
//						customAssert.assertTrue(k.Click("XOC_MFD_Regoins"),"Error while Clicking Geographic Limit List object . ");
//						k.waitTwoSeconds();
//						if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
//							each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
//						else
//							continue;
//						break;
//					}
//				}
//		 }
//		 xoc_details_
		 if(k.isDisplayedField("XOC_MFD_ProductLiability")){
			 
			 String[] MDF_Limits = ((String)common.NB_excel_data_map.get("XOC_MFD_ProductLiability")).split(",");
				List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
				for(String ins_limit : MDF_Limits){
					for(WebElement each_ul : ul_elements){
						customAssert.assertTrue(k.Click("XOC_MFD_ProductLiability"),"Error while Clicking Products Liability Material facts List object . ");
						k.waitTwoSeconds();
						if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed())
							each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
						else
							continue;
						break;
					}
				}
			 
				List<WebElement> det_elements = driver.findElements(By.xpath("//*[starts-with(@name,'xoc_details_')]"));
					for(WebElement each_el : det_elements){
						if(each_el.isDisplayed())
							each_el.sendKeys("Test input");
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
		
		customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");
		
		customAssert.assertTrue(k.Input("COB_PG_InsuredName", (String)map_data.get("PG_InsuredName")),	"Unable to enter value in Insured Name  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_InsuredName", "value"),"Insured Name Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("COB_PG_BusinessEstYear", (String)map_data.get("PG_YearEstablished")),	"Unable to enter value in Business Instablished year  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_BusinessEstYear", "value"),"Business Establishment Year Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("COB_PG_TurnOver", (String)map_data.get("PG_TurnOver")),	"Unable to enter value in Turnover field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_TurnOver", "value"),"TurnOver Field Should Contain Valid Name  .");
		customAssert.assertTrue(!k.getListAttributeIsEmpty("XOC_PG_GeoLimit", ""),"Geographic Limit Field Should Contain Valid Name  .");
		
		String[] geographical_Limits = ((String)common.NB_excel_data_map.get("PG_GeoLimit")).split(",");
		List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
		for(String geo_limit : geographical_Limits){
			for(WebElement each_ul : ul_elements){
				customAssert.assertTrue(k.Click("XOC_PG_GeoLimit"),"Error while Clicking Geographic Limit List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		
		String[] Prof_Bodies = ((String)common.NB_excel_data_map.get("PG_InsuredDetails_Q1")).split(",");
		List<WebElement> ul_ele = driver.findElements(By.xpath("//ul"));
		for(String pBodies_limit : Prof_Bodies){
			for(WebElement each_ul : ul_ele){
				customAssert.assertTrue(k.Click("XOC_PG_InsuredDetails_Q1"),"Error while Clicking Professional Bodies List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+pBodies_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+pBodies_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		customAssert.assertTrue(!k.getListAttributeIsEmpty("XOC_PG_InsuredDetails_Q1", ""),"Geographic Limit Field Should Contain Valid Name  .");	
		customAssert.assertTrue(k.DropDownSelection("COB_PG_InsuredDetails_Q2", (String)map_data.get("PG_InsuredDetails_Q2")),"Unable to select Second qustion");
		String sVal = (String)map_data.get("PG_InsuredDetails_Q2");
		if(sVal.contains("Yes")){
			customAssert.assertTrue(k.Input("COB_PG_InsuredDetails_details", (String)map_data.get("PG_InsuredDetails_details")),	"Unable to enter value in Provided Details field .");
		}
		
		customAssert.assertTrue(k.DropDownSelection("COB_PG_QuoteValidity", (String)map_data.get("PG_QuoteValidity")),"Unable to select Auote Validity value");
		
		customAssert.assertTrue(k.Input("COB_PG_BusDesc", (String)map_data.get("PG_BusDesc")),	"Unable to enter value in Provided Details field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_BusDesc", "value"),"Business Description Field Should Contain Valid Name  .");
		// Select Trade Code :
			
			String sValue = (String)map_data.get("PG_TCS_TradeCode");
			if(!sValue.equalsIgnoreCase("") || sValue!=null){
				customAssert.assertTrue(XOC_SelectTradeCode((String)common.NB_excel_data_map.get("PG_TCS_TradeCode") , "Policy General" , 0),"Trade code selection function is having issue(S).");
			}
			
		//customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");
		
		TestUtil.reportStatus("Entered all the details on Policy General page .", "Info", true);
		
		return retVal;
	
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Unable to to do operation on policy details page. \n", t);
        return false;
	}
	
}

public boolean funcPolicyGeneral_MTA(Map<Object, Object> map_data, String code, String event) {
	boolean retVal = true;
	String testName = "";
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");
		
		customAssert.assertTrue(k.Input("COB_PG_InsuredName", (String)map_data.get("PG_InsuredName")),	"Unable to enter value in Insured Name  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_InsuredName", "value"),"Insured Name Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("COB_PG_BusinessEstYear", (String)map_data.get("PG_YearEstablished")),	"Unable to enter value in Business Instablished year  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_BusinessEstYear", "value"),"Business Establishment Year Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("COB_PG_TurnOver", (String)map_data.get("PG_TurnOver")),	"Unable to enter value in Turnover field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_TurnOver", "value"),"TurnOver Field Should Contain Valid Name  .");
		customAssert.assertTrue(!k.getListAttributeIsEmpty("XOC_PG_GeoLimit", ""),"Geographic Limit Field Should Contain Valid Name  .");
		
		String[] geographical_Limits = ((String)common.NB_excel_data_map.get("PG_GeoLimit")).split(",");
		List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
		for(String geo_limit : geographical_Limits){
			for(WebElement each_ul : ul_elements){
				customAssert.assertTrue(k.Click("XOC_PG_GeoLimit"),"Error while Clicking Geographic Limit List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+geo_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		
		String[] Prof_Bodies = ((String)common.NB_excel_data_map.get("PG_InsuredDetails_Q1")).split(",");
		List<WebElement> ul_ele = driver.findElements(By.xpath("//ul"));
		for(String pBodies_limit : Prof_Bodies){
			for(WebElement each_ul : ul_ele){
				customAssert.assertTrue(k.Click("XOC_PG_InsuredDetails_Q1"),"Error while Clicking Professional Bodies List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+pBodies_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+pBodies_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		customAssert.assertTrue(!k.getListAttributeIsEmpty("XOC_PG_InsuredDetails_Q1", ""),"Geographic Limit Field Should Contain Valid Name  .");	
		customAssert.assertTrue(k.DropDownSelection("COB_PG_InsuredDetails_Q2", (String)map_data.get("PG_InsuredDetails_Q2")),"Unable to select Second qustion");
		String sVal = (String)map_data.get("PG_InsuredDetails_Q2");
		if(sVal.contains("Yes")){
			customAssert.assertTrue(k.Input("COB_PG_InsuredDetails_details", (String)map_data.get("PG_InsuredDetails_details")),	"Unable to enter value in Provided Details field .");
		}
		
		customAssert.assertTrue(k.DropDownSelection("COB_PG_QuoteValidity", (String)map_data.get("PG_QuoteValidity")),"Unable to select Auote Validity value");
		
		customAssert.assertTrue(k.Input("COB_PG_BusDesc", (String)map_data.get("PG_BusDesc")),	"Unable to enter value in Provided Details field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_BusDesc", "value"),"Business Description Field Should Contain Valid Name  .");
		// Select Trade Code :
			
			String sValue = (String)map_data.get("PG_TCS_TradeCode");
			if(!sValue.equalsIgnoreCase("") || sValue!=null){
				customAssert.assertTrue(XOC_SelectTradeCode((String)common.NB_excel_data_map.get("PG_TCS_TradeCode") , "Policy General" , 0),"Trade code selection function is having issue(S).");
			}
			
		//customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");
		
		TestUtil.reportStatus("Entered all the details on Policy General page .", "Info", true);
		
		return retVal;
	
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Unable to to do operation on policy details page. \n", t);
        return false;
	}
	
}

public boolean XOC_SelectTradeCode(String tradeCodeValue , String pageName , int currentPropertyIndex) {
	
		try{
			
			customAssert.assertTrue(k.Click("COB_Btn_SelectTradeCode"), "Unable to click on Select Trade Code button in Policy Details .");
			customAssert.assertTrue(common.funcPageNavigation("Multi Trade Code Selection", ""), "Navigation problem to Trade Code Selection page .");
			
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

public boolean GeneralRiskDetailsPage(Map<Object, Object> map_data){
	boolean retValue = true;
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("General Risk Details", ""),"General Risk Details page is having issue(S)");
		 String sVal = (String)map_data.get("GRD_AddMainP");
		 
		 if(sVal.length() > 0){
			 customAssert.assertTrue(func_GRD_AddMainP(map_data, sVal));
		 }
		 
		 sVal = (String)map_data.get("GRD_AddAdditionalP");
		 if(sVal.length() > 0){
			 customAssert.assertTrue(func_GRD_AddMainP(map_data, sVal));
		 }
		 
		 return retValue;
		 
	}catch(Throwable t){
		k.ImplicitWaitOn();
		return false;
	}
	finally{
		 k.ImplicitWaitOn();
	}
	
}

@SuppressWarnings("unused")
public boolean func_GRD_AddMainP(Map<Object, Object> map_data, String s_Premises){
	boolean retValue = true;
	String s_Section = null;
	String s_Sheet = null;
	
	try{
			if(s_Premises.contains("AddMainP")){
				s_Section = "AddMainP";
				s_Sheet = "GRDAddMainP";
			}else{
				s_Section = "AddAdditionalP";
				s_Sheet = "GRDAddAdditionalP";
			}
			
			String AllPremises[] =  s_Premises.split(";");
			int length = AllPremises.length;
			
			switch (s_Section) {
			case "AddMainP":
				if(length>1){
					TestUtil.reportStatus("Main Premisies should be added only one.", "Info", false);
				}
				length = 1;
			break;
			}
			
			for(int i = 0; i < length; i++){
				customAssert.assertTrue(k.Click("COB_GRD_"+s_Section),"Unable to click on GRDAddMainPremises button");
				switch (common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get(s_Section+"_CopyAddress")) {
				case "Yes":
					customAssert.assertTrue(common.funcButtonSelection("Copy Business Address Details"),"Unable to click on Copy correspondance Address button");
				break;
				default:
					customAssert.assertTrue(k.Input("XOC_GRD_Address1", common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get(s_Section+"_AddressLine1")),"Unable to enter value in Address line 1");
					customAssert.assertTrue(k.Input("XOC_GRD_Address2", common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get(s_Section+"_AddressLine2")),"Unable to enter value in Address line 2");
					customAssert.assertTrue(k.Input("XOC_GRD_Address3", common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get(s_Section+"_AddressLine3")),"Unable to enter value in Address line 3");
					customAssert.assertTrue(k.Input("XOC_GRD_Address4", common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get(s_Section+"_AddressLine4")),"Unable to enter value in Address line 4");
					customAssert.assertTrue(k.Input("XOC_GRD_Town", common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get(s_Section+"_Town")),"Unable to enter value in Town field");
					customAssert.assertTrue(k.Input("XOC_GRD_PostCode", common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get(s_Section+"_PostCode")),"Unable to enter value in Postcode field");
					customAssert.assertTrue(k.Input("XOC_GRD_County", common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get(s_Section+"_County")),"Unable to enter value in County field");
					customAssert.assertTrue(k.DropDownSelection("XOC_GRD_Country", common.NB_Structure_of_InnerPagesMaps.get(s_Sheet).get(i).get(s_Section+"_Country")),"Unable to enter value in Country field");
					break;
				}
				customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_GRD_Address1", "value"),"Address Line1 Field Should Contain Valid address details");
				customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_GRD_PostCode", "value"),"Postcode Field Should Contain Valid valid postcode");
				customAssert.assertTrue(common.funcButtonSelection("Save"), "Unable to click on Save Button on Main Premises screen");
				customAssert.assertTrue(k.Click("CCF_Btn_Back"), "Unable to click on Back Button on Main Premises screen");
			}		 
		  return retValue;
		 
	}catch(Throwable t){
		k.ImplicitWaitOn();
		return false;
	}
	finally{
		 k.ImplicitWaitOn();
	}
	
}
//For XOG
public void MTAFlow(String code,String fileName) throws ErrorInTestMethod{
	
	String testName = (String)common.MTA_excel_data_map.get("Automation Key");
	try{
		
		NewBusinessFlow(code,"NB");
		common.currentRunningFlow="MTA";
		
		String navigationBy = CONFIG.getProperty("NavigationBy");
		
		try{
				
				customAssert.assertTrue(funcCreateEndorsement(),"Issue while creating Endorsement. ");
				customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
				customAssert.assertTrue(common.funcSearchPolicy(common.MTA_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.MTA_excel_data_map,code,TestBase.businessEvent,"Endorsement Submitted"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
				
				customAssert.assertTrue(funcPolicyGeneral_MTA(common.MTA_excel_data_map,code,"MTA"), "Policy Details function having issue .");
				
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Previous Claims  . ");
				customAssert.assertTrue(funcPreviousClaims(common.MTA_excel_data_map), "Previous claim function is having issue(S) .");
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Previous Claims  . ");
				
				customAssert.assertTrue(funcUpdateCoverDetails_MTA(common.MTA_excel_data_map),"Error in selecting cover for MTA.");
				
				customAssert.assertTrue(common_VELA.funcCovers(common.MTA_excel_data_map), "Select covers function is having issue(S) . ");
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
				customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"General Risk Details"),"Issue while Navigating to General Risk Details screen . ");
				customAssert.assertTrue(common_XOG.GeneralRiskDetailsPage(common.MTA_excel_data_map));
				if(((String)common.MTA_excel_data_map.get("CD_ThirdPartyMotorLiability")).equalsIgnoreCase("Yes")){
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Third Party Motor Liability"),"Issue while Navigating to Third Party Motor Liability screen . ");
					customAssert.assertTrue(common_XOG.ThirdPartyMotorLiability(common.MTA_excel_data_map), "Issue with  Third Party Motor Liability function");
				}
				if(((String)common.MTA_excel_data_map.get("CD_CombinedLiability")).equalsIgnoreCase("Yes")){
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Combined Liability"),"Issue while Navigating to Combined Liabilit screen . ");
					customAssert.assertTrue(common_XOG.CombinedLiability(common.MTA_excel_data_map), "Issue with  CombinedLiability function");
				}
				if(((String)common.MTA_excel_data_map.get("CD_Public&ProductsLiability")).equalsIgnoreCase("Yes")){
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen . ");
					customAssert.assertTrue(common_XOG.PublicProductsLiabilityPage(common.MTA_excel_data_map), "Issue with  Public & Products Liability function");
				}
				if(((String)common.MTA_excel_data_map.get("CD_EmployersLiability")).equalsIgnoreCase("Yes")){
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to General Risk Details screen . ");
					customAssert.assertTrue(common_XOG.EmployersLiabilityPage(common.MTA_excel_data_map), "Issue with  Employers Liability function");
				}
				if(((String)common.MTA_excel_data_map.get("CD_JCT6.5.1")).equalsIgnoreCase("Yes")){
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to General Risk Details screen . ");
					customAssert.assertTrue(common_XOG.JCTPage(common.MTA_excel_data_map), "Issue with  Employers Liability function");
				}
				customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
				customAssert.assertTrue(common.funcEndorsementOperations(common.MTA_excel_data_map),"Endorsement is having issue(S).");
					
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
				customAssert.assertTrue(common_VELA.funcPremiumSummary(common.MTA_excel_data_map,code,"MTA"), "MTA Premium Summary function is having issue(S) . ");
				Assert.assertTrue(common_VELA.funcStatusHandling(common.MTA_excel_data_map,code,"MTA"));
				customAssert.assertEquals(common_VELA.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
					
				customAssert.assertEquals(common_VELA.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
				
				customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
				TestUtil.reportTestCasePassed(testName);
					
//					customAssert.assertTrue(common.funcEndorsementOperations(common.NB_excel_data_map),"Insurance tax adjustment is having issue(S).");
//					customAssert.assertTrue(common.verifyEndorsementONPremiumSummary(common.NB_excel_data_map),"Insurance tax adjustment is having issue(S).");
					
					
					/*driver.navigate().to("http://206.142.240.56/stingray/ctl/web/main.jsp?RID=307109&action=next&THIS=p22&PAGE=p1");
					customAssert.assertTrue(common_VELA.insuranceTaxAdjustmentHandling(code,event),"Insurance tax adjustment is having issue(S).");
					*/
					
				
			}catch(Throwable t){
				TestUtil.reportTestCaseFailed(testName, t);
				throw new ErrorInTestMethod(t.getMessage());
			}
		
		
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


public boolean funcRewindOperation() {
	try{
		String navigationBy = CONFIG.getProperty("NavigationBy");
		if(((String)common.NB_excel_data_map.get("CD_AddCover")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common_VELA.funcRewindCoversCheck(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
			if(((String)common.NB_excel_data_map.get("CD_Add_ThirdPartyMotorLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Third Party Motor Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.ThirdPartyMotorLiability(common.NB_excel_data_map), "Issue with  Third Party Motor Liability function");
		}
		if(((String)common.NB_excel_data_map.get("CD_Add_CombinedLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Combined Liability"),"Issue while Navigating to Combined Liabilit screen . ");
			customAssert.assertTrue(common_XOC.CombinedLiability(common.NB_excel_data_map), "Issue with  CombinedLiability function");
		}
		if(((String)common.NB_excel_data_map.get("CD_Add_Public&ProductsLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen . ");
			customAssert.assertTrue(common_XOC.PublicProductsLiabilityPage(common.NB_excel_data_map), "Issue with  Public & Products Liability function");
		}
		if(((String)common.NB_excel_data_map.get("CD_Add_EmployersLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.EmployersLiabilityPage(common.NB_excel_data_map), "Issue with  Employers Liability function");
		}
		if(((String)common.NB_excel_data_map.get("CD_Add_JCT6.5.1")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.JCTPage(common.NB_excel_data_map), "Issue with  Employers Liability function");
		}
		
		
		}
		if(((String)common.NB_excel_data_map.get("CD_ChangeCover")).equalsIgnoreCase("Yes")){
		if(((String)common.NB_excel_data_map.get("CD_ThirdPartyMotorLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Third Party Motor Liability"),"Issue while Navigating to General Risk Details screen . ");
			customAssert.assertTrue(common_XOC.ThirdPartyMotorLiabilityRewind(common.NB_excel_data_map), "Issue with  Third Party Motor Liability function");
		}
		if(((String)common.NB_excel_data_map.get("CD_CombinedLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Combined Liability"),"Issue while Navigating to Combined Liabilit screen . ");
			customAssert.assertTrue(common_XOC.CombinedLiabilityRewind(common.NB_excel_data_map), "Issue with  CombinedLiability function");
		}
		if(((String)common.NB_excel_data_map.get("CD_Public&ProductsLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public & Products Liability"),"Issue while Navigating to Public & Products Liability screen . ");
		//	customAssert.assertTrue(common_XOC.PublicProductsLiabilityPage(common.NB_excel_data_map), "Issue with  Public & Products Liability function");
		}
		if(((String)common.NB_excel_data_map.get("CD_EmployersLiability")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to General Risk Details screen . ");
		//	customAssert.assertTrue(common_XOC.EmployersLiabilityPage(common.NB_excel_data_map), "Issue with  Employers Liability function");
		}
		if(((String)common.NB_excel_data_map.get("CD_JCT6.5.1")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"JCT 6.5.1"),"Issue while Navigating to General Risk Details screen . ");
			//customAssert.assertTrue(common_XOC.JCTPage(common.NB_excel_data_map), "Issue with  Employers Liability function");
		}
		
		
		}
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_VELA.funcPremiumSummary(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Premium Summary function is having issue(S) . ");
			return true;
	}
	catch(Throwable t){
		return false;
	}
}


//*************************START***********************************//
//************************************************************//
//**** PL EL Book Rate Calculator Based on CPRS Sheet ********//
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
			annualWagerollalue = Integer.parseInt(cent_employeeWages.get(i).getAttribute("value"));
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
			
			case "Works":
				
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
					System.out.println("* Invalid Area of Work *");
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
	case "Renewal":
		data_map = common.Renewal_excel_data_map;
		break;

}
	
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

	Map<Object,Object> data_map = null;
	
	switch(common.currentRunningFlow){
	
		case "NB":
			data_map = common.NB_excel_data_map;
			break;
		case "Renewal":
			data_map = common.Renewal_excel_data_map;
			break;
	
	}
	
	List<WebElement> wage_work_activities = driver.findElements(By.xpath("//select[contains(@name,'coa_empl_break_activity_2')]//option[@selected='selected']")); 
	int total_selected_elm = wage_work_activities.size();
	
	List<WebElement> cent_wage_work_activities = driver.findElements(By.xpath("//input[contains(@name,'coa_empl_break_percentage_total')]")); 
	
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

	String works_LOI = k.getAttribute("COB_works_LOI", "value").replaceAll(",", "");
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
	
	k.scrollInView("COB_AW_ErectionOfTimberFrame");
	String timber_Frame_Option = k.GetDropDownSelectedValue("COB_AW_ErectionOfTimberFrame");
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
	System.out.println("Bona Fide book rate = "+final_Bf_book_rate);
	return final_Bf_book_rate;
	
}

//This method calculates total Loads
public void calculate_Total_Loads(String coverName){
	
	
	double total_PL_Loads = calculate_Height_Depth_Heat_Load("Height",coverName) + calculate_Height_Depth_Heat_Load("Depth",coverName) + 
							calculate_Height_Depth_Heat_Load("heat",coverName) + calculate_Years_Business_Established_Load();
	System.out.println("Total PL Loads = "+total_PL_Loads);
	common_COB.Book_rate_Rater_output.put("Total_PL_Loads", total_PL_Loads);
}
public void calculate_Plant_Total_Loads(String coverName){
	
	double total_PL_Loads = 0.0;
	
	if(coverName.equals("PL")){
		total_PL_Loads = calculate_Height_Depth_Heat_Load("heat",coverName) + calculate_Years_Business_Established_Load();
	}else if(coverName.equals("EL")){
		total_PL_Loads = calculate_Years_Business_Established_Load();
	}
	
	
	System.out.println("Total PL Loads = "+total_PL_Loads);
	common_COB.Book_rate_Rater_output.put("Total_PL_Loads", total_PL_Loads);
}
public void calculate_Works_Total_Loads(String coverName){
	
	double total_PL_Loads = 0.0;
	
	total_PL_Loads = calculate_Works_LOI_Load(coverName) + calculate_Works_Timber_Frame_Load(coverName) + calculate_Works_DE5_4_extension_Load(coverName);
	
	System.out.println("Total PL Loads = "+total_PL_Loads);
	common_COB.Book_rate_Rater_output.put("Total_PL_Loads", total_PL_Loads);
}

public void calculate_Multiplying_Factor(String coverName){
	
	double total_Load_Discount_aplied = 0.0;
	double multiplying_Factor = 0.0;
	
	
	if(coverName.equalsIgnoreCase("Works")){
		
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
	System.out.println("Total MF = "+multiplying_Factor);
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
	System.out.println("Total MF = "+multiplying_Factor);
	common_COB.Book_rate_Rater_output.put("Multiplying_Factor", multiplying_Factor);
}

public double get_PL_Book_Rate(String trade_Activity,String coverName){
	
	double book_rate = 0.0,net_Base_Rate=0.0;
	String activity = null;
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
	
	System.out.println("Book Rate of "+activity + " = "+book_rate);
	common_COB.Book_rate_Rater_output.put(coverName+"_"+trade_Activity, book_rate);
	return book_rate;
		
}


public double get_Net_Base_Rate(String trade_Activity,String cover){
	
	double r_value=0.0;
	DecimalFormat formatter = new DecimalFormat("###.####");
	
	PLEL_Rater = OR.getORProperties();
	String t_activity = trade_Activity.replaceAll(" -", "").replaceAll(" ", "_").replaceAll(",", "");
	t_activity = t_activity + "_"+cover + "_NBR";
	
	r_value = Double.parseDouble(PLEL_Rater.getProperty(t_activity));
	r_value = Double.valueOf(formatter.format(r_value));
	
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
			}else if(activity.contains("Asbestos") || activity.contains("CPA Contract Lift Cover")) {
				common_COB.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
				continue;
			}else{
				
				get_PL_Book_Rate(activity,coverName);
				common_COB.isIncludingHeatPresent = false;
				common_COB.isExcludingHeatPresent = false;
				common_COB.cent_work_including_heat=0.0;
				
			}
		}
		TestUtil.reportStatus("Book Rate for Public and Products Liability Cover calculated Successfully . ", "Info", false);
		
		break;
		
	case "EL": //Employers Liability
		
		List<String> el_activites = get_PL_Activities();
		for(String activity : el_activites){
			if(activity.contains("Asbestos") || activity.contains("Offshore Work & Visits") 
					|| activity.contains("Overseas Work")) {
				common_COB.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
				continue;
			}else{
				get_PL_Book_Rate(activity,coverName);
				common_COB.isIncludingHeatPresent = false;
				common_COB.isExcludingHeatPresent = false;
				common_COB.cent_work_including_heat=0.0;
			}
		}
		TestUtil.reportStatus("Book Rate for Employers Liability Cover calculated Successfully . ", "Info", false);
		
		break;
		
		
	case "Works": //Annual Works
		
		List<String> aw_activites = get_PL_Activities();
		for(String activity : aw_activites){
			if(activity.contains("DE") || activity.contains("Existing Structures") || activity.contains("Other")){
				common_COB.Book_rate_Rater_output.put(coverName+"_"+activity, 0.0);
			}else {
				
				get_PL_Book_Rate(activity,coverName);
				common_COB.isIncludingHeatPresent = false;
				common_COB.isExcludingHeatPresent = false;
				common_COB.cent_work_including_heat=0.0;
				
			}
		}
		TestUtil.reportStatus("Book Rate for Public and Products Liability Cover calculated Successfully . ", "Info", false);
		
		break;
		
	
	}	
		
	
	}catch(Throwable t){
		return false;
	}
	
	
	System.out.println("Book Rate lookup Outputs = "+common_COB.Book_rate_Rater_output);
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

//************************END************************************//
//************************************************************//
//**** PL EL Book Rate Calculator Based on CPRS Sheet ********//
//************************************************************//
//************************************************************//


public boolean ThirdPartyMotorLiability(Map<Object, Object> map_data){
	try{
		customAssert.assertTrue(common.funcPageNavigation("Third Party Motor Liability", ""),"Third Party Motor Liability page is having issue(S)");
		customAssert.assertTrue(k.Input("XOC_TPML_ExcessLayer", (String)map_data.get("Excess_Layer")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_TPML_ExcessLayer", "value"),"Excess Layer Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_TPML_ExcessLiabilityIns", (String)map_data.get("Excess_LiabiltyIns")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_TPML_ExcessLiabilityIns", "value"),"Excess Liability Insurance Field should not be empty  .");
		//customAssert.assertTrue(k.Input("XOC_TPML_ExcessLayer", (String)map_data.get("PrimaryLayerInsCover")));
	
		String[] InsuranceCover_Limits = ((String)map_data.get("PrimaryLayerInsCover")).split(",");
		List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
		for(String ins_limit : InsuranceCover_Limits){
			for(WebElement each_ul : ul_elements){
				customAssert.assertTrue(k.Click("XOC_TPML_LayerInsuranceCovelist"),"Error while Clicking Primary Layer Insurance Cover List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		//customAssert.assertTrue(!k.getListAttributeIsEmpty("XOC_TPML_ExcessLayer", ""),"Primary Layer Insurance Cover Field Should Contain Valid Name  .");
		//customAssert.assertTrue(!k.getAttributeIsEmpty("COB_PG_TurnOver", "value"),"TurnOver Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("XOC_TPML_LayerPremium", (String)map_data.get("LayerPremium")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_TPML_LayerPremium", "value"),"Layer Premium Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_TPML_LimitOfIndemnity", (String)map_data.get("LimitOfIndemnity")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_TPML_LimitOfIndemnity", "value"),"Limit of indeminity Field should not be empty  .");
		//customAssert.assertTrue(!k.getListAttributeIsEmpty("XOC_TPML_ExcessLayer", ""),"Primary Layer Insurance Cover Field Should Contain Valid Name  .");
		//Product Details
		String[] Vehicle_type = ((String)common.NB_excel_data_map.get("VehicleType")).split(",");
		List<WebElement> ul_elements1 = driver.findElements(By.xpath("//ul"));
		for(String Vcl_type : Vehicle_type){
			for(WebElement each_ul : ul_elements1){
				customAssert.assertTrue(k.Click("XOC_TPML_VehicleType"),"Error while Clicking Vehicle type List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+Vcl_type+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+Vcl_type+"']")).click();
				else
					continue;
				break;
			}
		}
		customAssert.assertTrue(k.Input("XOC_TPML_NoOfCars", (String)map_data.get("NumberOfCars")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_TPML_NoOfCars", "value"),"No. of cars field should not be empty  .");
		
		// Adding bespoke items
				
		String Sval = (String)map_data.get("AddBeSpokeItems");
		String bsAdd= (String)map_data.get("addBespoke?");  
		if(Sval!=null && bsAdd.contains("Yes")){
		String coverName = null;
		coverName="TPM";
		
		customAssert.assertTrue(funcAddBespoke(map_data, Sval, coverName),"Unable to add bspoke item for TPM cover");
		int tVal = k.getTableIndex("Actions", "xpath","html/body/div[3]/form/div/table");
		String bespoketableXpath ="html/body/div[3]/form/div/table[" +tVal+"]";
		if(driver.findElements(By.xpath(bespoketableXpath)).size()!=0){
			TestUtil.reportStatus("Bespoke Items have been listed into the table ", "Info", true);
		}else{
			TestUtil.reportStatus("Bespoke Items couldn't be added into the table ", "false", true);}
				
		customAssert.assertTrue(common.funcClickPageButton("Calculate Premium"),"");
		
		//html/body/div[3]/form/div/table[5]/tbody/tr[1]/td[2]/input
				
		String sUniqueCol ="Tech. Adjust (%)";
		int tableId = 0;
		
		String sTablePath = "html/body/div[3]/form/div/table";
		
		tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
		sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
		coverName = "Third Party Motor Liability";
		String abvr = "TPM_";
		
		customAssert.assertTrue(funcValidate_ManualRatedTables(map_data, sTablePath, coverName, abvr),"Unable to handle manual rated premium table on Third Party Motor Liability screen");	 
		
		
		}
		
	}catch(Throwable t){
		return false; 
	}
	
	
	return true;
}

public boolean ThirdPartyMotorLiabilityRewind(Map<Object, Object> map_data){
	try{
		double s_BookP = 0.00, s_TechAdjust = 0.00, s_commAdjust = 0.00, s_Final = 0.00, s_TotalP = 0.00;
		double c_BookP = 0.00, c_TechAdjust = 0.00, c_commAdjust = 0.00, c_Final = 0.00;
		String s_Section=null;
	
		customAssert.assertTrue(common.funcPageNavigation("Third Party Motor Liability", ""),"Third Party Motor Liability page is having issue(S)");
		String sUniqueCol ="Tech. Adjust (%)";
		int tableId = 0;
	
		String sTablePath = "html/body/div[3]/form/div/table";
		tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
		sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
		WebElement table= driver.findElement(By.xpath(sTablePath));
		int totalRows = table.findElements(By.tagName("tr")).size();
		String coverName = "Third Party Motor Liability";
		String abvr = "Rwd_TPM_";
		String i_abvr = "AddBeSpokeTPM_";
		String s_InnerSheetName = "AddBeSpokeTPM";
		for(int i = 0; i< totalRows-2; i++){
			s_Section = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]");
			if(i==0){
			
				customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[2]/input", map_data, coverName, i, abvr, "BookPremium", s_Section, "Input"),"Unable to enter Book Premium value for - "+s_Section);
				customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[3]/input", map_data, coverName, i, abvr, "TechAdjust", s_Section, "Input"),"Unable to enter TechAdjust value for - "+s_Section);
				customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[4]/input", map_data, coverName, i, abvr, "CommAdjust", s_Section, "Input"),"Unable to enter CommAdjust value for - "+s_Section);
			}
		}
		customAssert.assertTrue(common.funcClickPageButton("Calculate Premium"),"");	
		 
		// Read Premium values from screen, Compare with Calculated Value :
		 
		 for(int i = 0; i< totalRows-2; i++){
			 
			 //Read Values :
			 
				s_Section = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]");
				
				s_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[2]/input"));
				s_TechAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[3]/input"));
				s_commAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[4]/input")); 
				s_Final = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[5]/input").replaceAll(",", ""));
				
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
		 String SecCoverName = "PS_"+ coverName.replaceAll(" ", "")+"_NetNetPremium";
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), SecCoverName, String.valueOf(s_TotalP), map_data);
	 
//		customAssert.assertTrue(funcValidate_ManualRatedTables(map_data, sTablePath, coverName, abvr),"Unable to handle manual rated premium table on Third Party Motor Liability screen");	 
		
		
//		}
		
	}catch(Throwable t){
		return false; 
	}
	
	
	return true;
}


public boolean funcAddBespoke(Map<Object, Object> map_data, String s_Bespoke,  String s_CoverName){
	boolean retValue = true;
	String innerSheetName = null;
	String s_Abvr = "_"+s_CoverName;
	try{
				
			String AllBespoke[] =  s_Bespoke.split(";");
			innerSheetName = AllBespoke[0].replace("_01", "");
			k.pressDownKeyonPage();
			for(int i = 0; i < AllBespoke.length; i++){
				
				customAssert.assertTrue(k.Click("XOC_Btn_AddBespokeItem"),"Unable to click on AddBespokeItem button");
				
				if(!driver.findElement(By.xpath(OR.getlocatorValue("XOC_TPML_bespoke_desc"))).isDisplayed()){
					k.scrollInView("XOC_CL_AddBeSpoke");
					customAssert.assertTrue(k.Click("XOC_CL_AddBeSpoke"),"Unable to click on AddBespokeItem button");
				}
				 
				switch(s_CoverName){
					case "JCT" :
						s_Abvr = "AddBeSpokeJCT";
						if(k.isDisplayed("XOC_TPML_bespoke_desc")){
							customAssert.assertTrue(k.Input("XOC_TPML_bespoke_desc",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"_Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("XOC_TPML_bespoke_amt")){
							customAssert.assertTrue(k.Input("XOC_TPML_bespoke_amt",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"_BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
					case "PPL" :	
						s_Abvr = "AddBeSpokePPL";
						if(k.isDisplayed("XOC_TPML_bespoke_desc")){
							customAssert.assertTrue(k.Input("XOC_TPML_bespoke_desc",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"_Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("XOC_TPML_bespoke_amt")){
							customAssert.assertTrue(k.Input("XOC_TPML_bespoke_amt",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"_BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
					case "EL" :	
						s_Abvr = "AddBeSpokeEL";
						if(k.isDisplayed("XOC_TPML_bespoke_desc")){
							customAssert.assertTrue(k.Input("XOC_TPML_bespoke_desc",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"_Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("XOC_TPML_bespoke_amt")){
							customAssert.assertTrue(k.Input("XOC_TPML_bespoke_amt",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"_BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
					
					case "TPM" :	
						s_Abvr = "AddBeSpokeTPM";
						if(k.isDisplayed("XOC_TPML_bespoke_desc")){
							customAssert.assertTrue(k.Input("XOC_TPML_bespoke_desc",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"_Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("XOC_TPML_bespoke_amt")){
							customAssert.assertTrue(k.Input("XOC_TPML_bespoke_amt",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"_BookPremium")),"Unable to put value in BookPremium field");
						}
						break;
					case "CL" :	
						s_Abvr = "AddBeSpokeCL";
						if(k.isDisplayed("XOC_TPML_bespoke_desc")){
							customAssert.assertTrue(k.Input("XOC_TPML_bespoke_desc",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"_Description")),"Unable to put value in Description field");
						}
						if(k.isDisplayed("XOC_TPML_bespoke_amt")){
							customAssert.assertTrue(k.Input("XOC_TPML_bespoke_amt",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"_BookPremium")),"Unable to put value in BookPremium field");
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
						
					default :	
						customAssert.assertTrue(k.DropDownSelection("COB_AddB_Additionalcover", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Additionalcover")),"Unable to put value in Additionalcover field");
						
						if(k.isDisplayed("COB_AddB_TypeOfLimit")){
							customAssert.assertTrue(k.DropDownSelection("COB_AddB_TypeOfLimit", (String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"TypeOfLimit")),"Unable to put value in TypeOfLimit field");
						}
						
						if(k.isDisplayed("COB_AddB_LOI")){
							customAssert.assertTrue(k.Input("COB_AddB_LOI",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"_ProvideFullDetails")),"Unable to put value in PleaseProvideFullDetails field");
						}
						
						if(k.isDisplayed("COB_AddB_Description")){
							customAssert.assertTrue(k.Input("COB_AddB_Description",(String)common.NB_Structure_of_InnerPagesMaps.get(innerSheetName).get(i).get(s_Abvr+"Description")),"Unable to put value in Description field");
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

public boolean funcValidate_ManualRatedTables(Map<Object, Object> map_data, String s_TablePath, String s_CoverName, String abvr){
	boolean retValue = true;
	String s_Section = null, i_abvr = null, t_abvr=null;
	t_abvr = abvr;
	int totalCols = 0, totalRows = 0;
	String s_SheetName =null, s_InnerSheetName = null;
	double s_BookP = 0.00, s_TechAdjust = 0.00, s_commAdjust = 0.00, s_Final = 0.00, s_TotalP = 0.00;
	double c_BookP = 0.00, c_TechAdjust = 0.00, c_commAdjust = 0.00, c_Final = 0.00;
	int bs_counter = 0;
	try{
			
			WebElement table= driver.findElement(By.xpath(s_TablePath));
			totalCols = table.findElements(By.tagName("th")).size(); 
			totalRows = table.findElements(By.tagName("tr")).size();
			
			s_InnerSheetName = "AddBeSpoke"+abvr.replace("_", "");
			i_abvr = "AddBeSpoke"+abvr;
					
			if(s_CoverName.contains("JCT"))
				s_SheetName = "JCT 6.5.1";
			else
				s_SheetName = s_CoverName;							
			
			for(int i = 0; i< totalRows-2; i++){
				s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]");
				if(s_Section.contains("Public & Products Liability")&& s_CoverName.contains("Combined Liability"))
					abvr = "CL_PL_";
				else if(s_Section.contains("Employers Liability")&& s_CoverName.contains("Combined Liability"))
					abvr = "CL_EL_";
				
				 	
				
				if(i == 0){
					customAssert.assertTrue(k.DynamicXpathWebDriver(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[2]/input", map_data, s_SheetName, i, abvr, "BookPremium", s_Section, "Input"),"Unable to enter Book Premium value for - "+s_Section);
					customAssert.assertTrue(k.DynamicXpathWebDriver(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[3]/input", map_data, s_SheetName, i, abvr, "TechAdjust", s_Section, "Input"),"Unable to enter TechAdjust value for - "+s_Section);
					customAssert.assertTrue(k.DynamicXpathWebDriver(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[4]/input", map_data, s_SheetName, i, abvr, "CommAdjust", s_Section, "Input"),"Unable to enter CommAdjust value for - "+s_Section);
				}else{
					if((s_Section.contains("Public & Products Liability") || s_Section.contains("Employers Liability")) && s_CoverName.contains("Combined Liability")){
						customAssert.assertTrue(k.DynamicXpathWebDriver(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[2]/input", map_data, s_SheetName, i, abvr, "BookPremium", s_Section, "Input"),"Unable to enter Book Premium value for - "+s_Section);
						customAssert.assertTrue(k.DynamicXpathWebDriver(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[3]/input", map_data, s_SheetName, i, abvr, "TechAdjust", s_Section, "Input"),"Unable to enter TechAdjust value for - "+s_Section);
						customAssert.assertTrue(k.DynamicXpathWebDriver(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[4]/input", map_data, s_SheetName, i, abvr, "CommAdjust", s_Section, "Input"),"Unable to enter CommAdjust value for - "+s_Section);
					}else if(s_Section.contains("Third Party Motor Liability")&& s_CoverName.contains("Combined Liability")){
						abvr = "CL_TP_";
						customAssert.assertTrue(k.DynamicXpathWebDriver(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[3]/input", map_data, s_SheetName, i, abvr, "TechAdjust", s_Section, "Input"),"Unable to enter TechAdjust value for - "+s_Section);
						customAssert.assertTrue(k.DynamicXpathWebDriver(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[4]/input", map_data, s_SheetName, i, abvr, "CommAdjust", s_Section, "Input"),"Unable to enter CommAdjust value for - "+s_Section);
					}else if(s_Section.contains("Third Party Motor Liability")&& s_CoverName.contains("Public & Products Liability")){
						abvr = "PPL_TP_";
						customAssert.assertTrue(k.DynamicXpathWebDriver(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[3]/input", map_data, s_SheetName, i, abvr, "TechAdjust", s_Section, "Input"),"Unable to enter TechAdjust value for - "+s_Section);
						customAssert.assertTrue(k.DynamicXpathWebDriver(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[4]/input", map_data, s_SheetName, i, abvr, "CommAdjust", s_Section, "Input"),"Unable to enter CommAdjust value for - "+s_Section);
					} else {
						customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[3]/input", common.NB_Structure_of_InnerPagesMaps, s_InnerSheetName, bs_counter, i_abvr, "TechAdjust", "TechAdjust", "Input"),"Unable to enter TechAdjust in table");
						customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, s_TablePath + "/tbody/tr["+(i+1)+"]/td[4]/input", common.NB_Structure_of_InnerPagesMaps, s_InnerSheetName, bs_counter, i_abvr, "CommAdjust", "CommAdjust", "Input"),"Unable to enter CommAdjust in table");
						bs_counter = bs_counter +1;
					}
				}
				abvr = t_abvr;	
			}
			
			// Click on Calculate Premium button :
			 
			customAssert.assertTrue(common.funcClickPageButton("Calculate Premium"),"");	
			 
			// Read Premium values from screen, Compare with Calculated Value :
			bs_counter = 0;
			
			 for(int i = 0; i< totalRows-2; i++){ 
				 
				 //Read Values :
				 abvr = t_abvr;
					s_Section = k.GetText_DynamicXpathWebDriver(driver, s_TablePath+"/tbody/tr["+(i+1)+"]");
					if(s_Section.contains("Public & Products Liability")&& s_CoverName.contains("Combined Liability"))
						abvr = "CL_PL_";
					else if(s_Section.contains("Employers Liability") && s_CoverName.contains("Combined Liability"))
						abvr = "CL_EL_";
					else if(s_Section.contains("Third Party Motor Liability") && s_CoverName.contains("Combined Liability"))
						abvr= "CL_TP_";
					else if(s_Section.contains("Third Party Motor Liability") && s_CoverName.contains("Public & Products Liability"))
						abvr= "PPL_TP_";
					
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
						if((s_Section.contains("Public & Products Liability") || s_Section.contains("Employers Liability") || s_Section.contains("Third Party Motor Liability")) && (s_CoverName.contains("Combined Liability")||s_CoverName.contains("Public & Products Liability"))){
							c_BookP = Double.parseDouble((String)map_data.get(abvr+"BookPremium"));
							c_TechAdjust =  Double.parseDouble((String)map_data.get(abvr+"TechAdjust"));
							c_commAdjust = Double.parseDouble((String)map_data.get(abvr+"CommAdjust"));
						}else{
							c_BookP = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(bs_counter).get(i_abvr+"BookPremium"));
							c_TechAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(bs_counter).get(i_abvr+"TechAdjust"));
							c_commAdjust = Double.parseDouble((String)common.NB_Structure_of_InnerPagesMaps.get(s_InnerSheetName).get(bs_counter).get(i_abvr+"CommAdjust"));
							bs_counter = bs_counter+1; 
						} 
					}
					
					double t_Premium = (c_BookP + ((c_BookP * c_TechAdjust)/100));
					c_Final = ((t_Premium* c_commAdjust)/100) + t_Premium;
					
					CommonFunction.compareValues(c_BookP, s_BookP,"Book Premium for section "+s_Section);
					CommonFunction.compareValues(c_TechAdjust, s_TechAdjust,"Tech Adjust for section "+s_Section);
					CommonFunction.compareValues(c_commAdjust, s_commAdjust,"Comm Adjust for section "+s_Section);
					CommonFunction.compareValues(c_Final, s_Final,"Final Premium for section "+s_Section);
					
					s_TotalP = s_TotalP + s_Final;
				}	
			 String SecCoverName = "PS_"+ s_CoverName.replaceAll(" ", "")+"_NetNetPremium";
			 if(s_CoverName.contains("JCT"))
				 SecCoverName = "PS_"+ s_SheetName.replaceAll(" ", "")+"_NetNetPremium";
			 
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), SecCoverName, String.valueOf(s_TotalP), map_data);
		 
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
		 
		 //Work with silica, asbestos, or substances containing asbestos?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_containingAsbestos");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"containingAsbestos"));
		 
		 //Work with acids, gases, chemicals, explosives, radioactive or similar dangerous liquids or substances?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_dangerousLiquidsORsubstances");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"dangerousLiquidsORsubstances"));
		 
		 //Work on power stations, nuclear installations or establishments?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_nuclearInstallations");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"nuclearInstallations"));
		 
		 //Work on refineries, bulk storage, or premises in oil, gas or chemical industries?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_gasORchemicalIndustries");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"gasORchemicalIndustries"));
		 
		 //Work airside or on aircraft, hovercraft, aerospace systems, watercraft, railway, underground, quarries, underwater at docks, harbours or piers?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_harboursORpiers");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"harboursORpiers"));
			 
		 //Work in or on computer suites or on computers?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_onComputers");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"onComputers"));
		
		 //Work on Bridges, viaducts, towers, steeples, chimney shafts or blast furnaces?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_blastFurnace");
		 if(mfd_q_value.equalsIgnoreCase("Yes"))
			 common_VELA.referrals_list.add((String)map_data.get(mf_key+"blastFurnace"));
		
		 	 
		 //Do you source products from outside of the EU?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_outside_of_the_EU");
		 if(mfd_q_value.equalsIgnoreCase("Yes")){
			 //Products Liability Material facts - Multi-select list
			 try{
				 List<WebElement> _outside_eu_MF = driver.findElements(By.xpath("//*[text()='Please select all relevant regions']//following::ul[1]//li"));
				 String eu_value =null;
				 for(WebElement prd_elm:_outside_eu_MF){
					 try{
						 eu_value = prd_elm.getAttribute("title");
					 }catch(Throwable t){
						 eu_value="None";
						 
						 }
					 
					 if(!eu_value.equalsIgnoreCase("None") && !eu_value.isEmpty()){
						 common_VELA.referrals_list.add("Material Facts & Declarations_Products sourced from outside of the EU: "+eu_value);
					 }
					 
				 }
			 }catch(Throwable t){
				 
			 }
			 
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
					 common_VELA.referrals_list.add("Material Facts & Declarations_"+prd_value);
				 }
				 
			 }
		 }catch(Throwable t){
			 
		 }
		
		 //Are any products supplied to hazardous industries?
		 mfd_q_value = k.GetDropDownSelectedValue("XOC_MFD_hazardousIndustries");
		 if(mfd_q_value.equalsIgnoreCase("Yes")){
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
					 common_VELA.referrals_list.add("Material Facts & Declarations_Products supplied to hazardous industries: "+_relevant_industries_vlaue);
				 }
				 
			 }
			
		 }catch(Throwable t){
			 
		 }
		  
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

public boolean CombinedLiability(Map<Object, Object> map_data){
	try{
		
		//Employers Liability In Combined Liability Cover
		customAssert.assertTrue(common.funcPageNavigation("Combined Liability", ""),"Combined Liability page is having issue(S)");
		customAssert.assertTrue(k.Input("XOC_CL_LimitOfIndeminity", (String)map_data.get("XOC_CL_LimitOfIndeminity")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_LimitOfIndeminity", "value"),"Employers Limit of Indeminity Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_CL_ExcessLiabilityIns", (String)map_data.get("XOC_CL_ExcessLiabilityIns")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_ExcessLiabilityIns", "value"),"Employers Excess Liability Insurance Field should not be empty  .");
		//customAssert.assertTrue(k.Input("XOC_TPML_ExcessLayer", (String)map_data.get("PrimaryLayerInsCover")));
	
		String[] EL_InsuranceCover_Limits = ((String)common.NB_excel_data_map.get("XOC_CL_LayerInsuranceCoverList")).split(",");
		List<WebElement> el_ul_elements = driver.findElements(By.xpath("//*[contains(@id,'_cmb_el_insurer_primary')]"));
		for(String ins_limit : EL_InsuranceCover_Limits){
			for(WebElement each_ul : el_ul_elements){
				customAssert.assertTrue(k.Click("XOC_CL_LayerInsuranceCoverList"),"Error while Clicking EL Primary Layer Insurance Cover List object . ");
				k.waitTwoSeconds();
				ins_limit = ins_limit.trim();
				if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		
		customAssert.assertTrue(k.Input("XOC_CL_PrimaryLayerPremium", (String)map_data.get("XOC_CL_PrimaryLayerPremium")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_PrimaryLayerPremium", "value"),"Employers Primary Layer Premium Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_CL_PrimaryLayerofIndeminity", (String)map_data.get("XOC_CL_PrimaryLayerofIndeminity")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_PrimaryLayerofIndeminity", "value"),"Employers Primary Layer of Indeminity Field should not be empty  .");
		
		
		//Public Liability in Combined Liability Cover
		
		customAssert.assertTrue(k.Input("XOC_CL_PL_ExcessLayer", (String)map_data.get("XOC_CL_PL_ExcessLayer")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_PL_ExcessLayer", "value"),"Public Liability excess Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_CL_PL_AttchmntLayerInsCover", (String)map_data.get("XOC_CL_PL_AttchmntLayerInsCover")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_ExcessLiabilityIns", "value"),"Public liabilty Layer Insurance Field should not be empty  .");
		String[] PL_InsuranceCover_Limits = ((String)common.NB_excel_data_map.get("XOC_CL_PL_LayerInsuranceCoverList")).split(",");
		customAssert.assertTrue(k.Click("XOC_CL_PL_LayerInsuranceCoverList"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");
		List<WebElement> pl_ul_elements = driver.findElements(By.xpath("//*[contains(@id,'xoc_cmb_pl_insurer_primary')]")); // *[contains(@id,'_cmb_pl_insurer_primary')]
		for(String ins_limit : PL_InsuranceCover_Limits){
			for(WebElement each_ul : pl_ul_elements){
				//customAssert.assertTrue(k.Click("XOC_CL_PL_LayerInsuranceCoverList"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed()){ 
					each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
					customAssert.assertTrue(k.Click("XOC_CL_PL_LayerInsuranceCoverList"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");	
			}	else{
					continue;}
				break;
			}
		} 
		
		customAssert.assertTrue(k.Input("XOC_CL_PL_PrimaryLayerPremium", (String)map_data.get("XOC_CL_PL_PrimaryLayerPremium")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_PL_PrimaryLayerPremium", "value"),"Public Liability Primary Layer Premium Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_CL_PL_PrimaryLayerOfIndeminity", (String)map_data.get("XOC_CL_PL_PrimaryLayerOfIndeminity")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_PL_PrimaryLayerOfIndeminity", "value"),"Public Liability Primary Layer of Indeminity Field should not be empty  .");
		
		
		customAssert.assertTrue(k.Input("XOC_CL_PL_ProductDesc", (String)map_data.get("XOC_CL_PL_ProductDesc")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_PL_ProductDesc", "value"),"Public Liability Primary Layer of Indeminity Field should not be empty  .");
		String[] PL_Product_Limits = null;
		
		PL_Product_Limits = ((String)common.NB_excel_data_map.get("XOC_CL_PL_ProductLibilityRskMgmtFtrs")).split(",");
		//List<WebElement> prod_ul_elements = driver.findElements(By.xpath("//ul"));
		String item1 = "//*[text()='"; // Batch recording']
		String l_item = null;
		for(String ins_limit : PL_Product_Limits){
			//for(WebElement each_ul : prod_ul_elements){ 
				if(k.isDisplayedField("XOC_CL_PL_ProductLibilityRskMgmtFtrs")){
					customAssert.assertTrue(k.Click("XOC_CL_PL_ProductLibilityRskMgmtFtrs"),"Error while Clicking PL product List object . ");
				}else {
					customAssert.assertTrue(k.Click("XOQ_CL_PL_ProductLibilityRskMgmtFtrs"),"Error while Clicking PL product List object . ");
				}
				
				k.waitTwoSeconds();
				ins_limit = ins_limit.trim();
				l_item = item1+ins_limit+"']";
				if(driver.findElement(By.xpath(l_item)).isDisplayed())
					driver.findElement(By.xpath(l_item)).click();
				else
					continue;
				 
			//}
		} 
		
		List<WebElement> PLPD = driver.findElements(By.xpath("//*[contains(@name,'xoc_cmb_pl_risk_') and contains(@name,'_desc')]"));
			for(WebElement pl_desc:PLPD){ 
				if(pl_desc.isDisplayed())
					pl_desc.sendKeys("Test Details");
					
				else
					continue;
			}
		customAssert.assertTrue(k.Input("XOC_CL_PL_ProductDetails", (String)map_data.get("XOC_CL_PL_ProductDetails")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_PL_PrimaryLayerOfIndeminity", "value"),"Public Liability Product Details  Field should not be empty  .");
		if(k.isDisplayedField("XOQ_RequireAsbestosCover")){
			customAssert.assertTrue(k.DropDownSelection("XOQ_RequireAsbestosCover", (String)map_data.get("XOQ_RequireAsbestosCover")));
		}
		if(k.isDisplayedField("XOQ_RequireAsbestosCover") && common.product.contains("XOQ")){
			customAssert.assertTrue(k.DropDownSelection("XOQ_RequireAsbestosCover", (String)map_data.get("XOQ_RequireAsbestosCover")));
		}
		
		// Optional Extension Third Party Motor Liability
		if(((String)map_data.get("XOC_CL_TPMPL_OptionalExtn")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(k.DropDownSelection("XOC_CL_TPMPL_OptionalExtn", (String)map_data.get("XOC_CL_TPMPL_OptionalExtn")));
			
			customAssert.assertTrue(k.Input("XOC_CL_TPMPL_BookPremium", (String)map_data.get("CL_TP_BookPremium")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_TPMPL_BookPremium", "value"),"Third Party Motor Liability book premium Field should not be empty  .");
			customAssert.assertTrue(k.Input("XOC_CL_TPMPL_ExcessLayer", (String)map_data.get("XOC_CL_TPMPL_ExcessLayer")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_TPMPL_ExcessLayer", "value"),"Third Party Motor Liability Field should not be empty  .");
			customAssert.assertTrue(k.Input("XOC_CL_TPMPL_ExcessLiabilityIns", (String)map_data.get("XOC_CL_TPMPL_ExcessLiabilityIns")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_TPMPL_ExcessLiabilityIns", "value"),"Third Party Motor Liability Excess Liability Insurance Field should not be empty  .");
			String[] TPML_InsuranceCover_Limits = ((String)common.NB_excel_data_map.get("XOC_CL_TPMPL_LayerInsuranceCoverList")).split(",");
			customAssert.assertTrue(k.Click("XOC_CL_TPMPL_LayerInsuranceCoverList"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");
			List<WebElement> tpml_ul_elements = driver.findElements(By.xpath("//*[contains(@id,'xoc_cmb_thr_insurer_primary-')]"));
			
				
			for(String ins_limit : TPML_InsuranceCover_Limits){
				for(WebElement each_ul : tpml_ul_elements){
					
					k.waitTwoSeconds();
					ins_limit = ins_limit.trim();
					if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed())
						{each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
						customAssert.assertTrue(k.Click("XOC_CL_TPMPL_LayerInsuranceCoverList"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");
						}
					else
						continue;
					break;
				}
			}
			customAssert.assertTrue(k.Click("XOC_CL_TPMPL_LayerInsuranceCoverList"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");
			customAssert.assertTrue(k.Input("XOC_CL_TPMPL_PremiumLayerPremium", (String)map_data.get("XOC_CL_TPMPL_PremiumLayerPremium")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_TPMPL_PremiumLayerPremium", "value"),"Third Party Motor Liability  Primary Layer Premium Field should not be empty  .");
			customAssert.assertTrue(k.Input("XOC_CL_TPMPL_PremiumLayerofIndeminity", (String)map_data.get("XOC_CL_TPMPL_PremiumLayerofIndeminity")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_TPMPL_PremiumLayerofIndeminity", "value"),"Third Party Motor Liability  Primary Layer of Indeminity Field should not be empty  .");
			
//			String[] TPML_vhclType_Limits = ((String)common.NB_excel_data_map.get("XOC_CL_TPMPL_VehicleType")).split(",");
//			customAssert.assertTrue(k.Click("XOC_CL_TPMPL_VehicleType"),"Error while Clicking Vehical Type list object . ");
//			Thread.sleep(1000);
//			List<WebElement> tpml_VTul_elements = driver.findElements(By.xpath("//li[contains(@id,'xoc_cmb_thr_vehicle_type')]"));
//			if(tpml_VTul_elements.size()==0){
//				customAssert.assertTrue(k.Input("XOC_CL_TPMPL_VehicleType"," "),"Error while entering Vehicle type List object . ");
//				tpml_VTul_elements = driver.findElements(By.xpath("//li[contains(@id,'xoc_cmb_thr_vehicle_type')]"));
//			}
//			for(String ins_limit : TPML_vhclType_Limits){
//				for(WebElement each_ul : tpml_VTul_elements){
//					ins_limit = ins_limit.trim();
//					if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed())
//						{each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
//						customAssert.assertTrue(k.Click("XOC_CL_TPMPL_VehicleType"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");
//						}
//					else
//						continue;
//					break;
//				}
//			}
			driver.findElement(By.xpath("//*[@id='mainpanel']")).click();
			String[] InsuranceCover_Limits = ((String)common.NB_excel_data_map.get("XOC_CL_TPMPL_VehicleType")).split(",");
			List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
			for(String ins_limit : InsuranceCover_Limits){
				for(WebElement each_ul : ul_elements){
					customAssert.assertTrue(k.Click("XOC_CL_TPMPL_VehicleType"),"Error while Clicking Vehicle type List object . ");
					k.waitTwoSeconds();
					if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed())
						each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
					else
						continue;
					break;
				}
			}
			customAssert.assertTrue(k.Input("XOC_CL_TPMPL_NumberOfCars", (String)map_data.get("XOC_CL_TPMPL_NumberOfCars")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_TPMPL_NumberOfCars", "value"),"Third Party Motor Liability  Number of cars Field should not be empty  .");
			
		}	
		// Adding bespoke items
			String coverName = null;
			String Sval = (String) common.NB_excel_data_map.get("CL_AddBeSpokeItems");
			String bsAdd= (String) common.NB_excel_data_map.get("CL_addBespoke?");  
			if(Sval!=null && bsAdd.contains("Yes")){
					
					coverName="CL";
					customAssert.assertTrue(funcAddBespoke(map_data, Sval, coverName),"Unable to add bspoke item for CL cover");
					
					int tVal = k.getTableIndex("Actions", "xpath","html/body/div[3]/form/div/table");
					String bespoketableXpath ="html/body/div[3]/form/div/table[" +tVal+"]";
					if(driver.findElements(By.xpath(bespoketableXpath)).size()!=0){
						TestUtil.reportStatus("Bespoke Items have been listed into the table ", "Info", true);
					}else{
						TestUtil.reportStatus("Bespoke Items couldn't be added into the table ", "false", true);}
							
					customAssert.assertTrue(common.funcClickPageButton("Calculate Premium"),"");
				}
				
				//html/body/div[3]/form/div/table[5]/tbody/tr[1]/td[2]/input
						
				String sUniqueCol ="Tech. Adjust (%)";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
				coverName = "Combined Liability";
				String abvr = "CL_";
				
				customAssert.assertTrue(funcValidate_ManualRatedTables(map_data, sTablePath, coverName, abvr),"Unable to handle manual rated premium table on Third Party Motor Liability screen");	 
				
				return true;
		}	
	catch(Throwable t){
		return false; 
	}
}	
public boolean CombinedLiabilityRewind(Map<Object, Object> map_data){
	try{
		
		//Employers Liability In Combined Liability Cover
		customAssert.assertTrue(common.funcPageNavigation("Combined Liability", ""),"Combined Liability page is having issue(S)");
		customAssert.assertTrue(k.Input("XOC_CL_LimitOfIndeminity", (String)map_data.get("XOC_CL_LimitOfIndeminity")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_LimitOfIndeminity", "value"),"Employers Limit of Indeminity Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_CL_ExcessLiabilityIns", (String)map_data.get("XOC_CL_ExcessLiabilityIns")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_ExcessLiabilityIns", "value"),"Employers Excess Liability Insurance Field should not be empty  .");
		//customAssert.assertTrue(k.Input("XOC_TPML_ExcessLayer", (String)map_data.get("PrimaryLayerInsCover")));
	
		String[] EL_InsuranceCover_Limits = ((String)common.NB_excel_data_map.get("XOC_CL_LayerInsuranceCoverList")).split(",");
		List<WebElement> el_ul_elements = driver.findElements(By.xpath("//*[contains(@id,'_cmb_el_insurer_primary')]"));
		for(String ins_limit : EL_InsuranceCover_Limits){
			for(WebElement each_ul : el_ul_elements){
				customAssert.assertTrue(k.Click("XOC_CL_LayerInsuranceCoverList"),"Error while Clicking EL Primary Layer Insurance Cover List object . ");
				k.waitTwoSeconds();
				ins_limit = ins_limit.trim();
				if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		
		customAssert.assertTrue(k.Input("XOC_CL_PrimaryLayerPremium", (String)map_data.get("XOC_CL_PrimaryLayerPremium")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_PrimaryLayerPremium", "value"),"Employers Primary Layer Premium Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_CL_PrimaryLayerofIndeminity", (String)map_data.get("XOC_CL_PrimaryLayerofIndeminity")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_PrimaryLayerofIndeminity", "value"),"Employers Primary Layer of Indeminity Field should not be empty  .");
		
		
		//Public Liability in Combined Liability Cover
		
		customAssert.assertTrue(k.Input("XOC_CL_PL_ExcessLayer", (String)map_data.get("XOC_CL_PL_ExcessLayer")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_PL_ExcessLayer", "value"),"Public Liability excess Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_CL_PL_AttchmntLayerInsCover", (String)map_data.get("XOC_CL_PL_AttchmntLayerInsCover")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_ExcessLiabilityIns", "value"),"Public liabilty Layer Insurance Field should not be empty  .");
		String[] PL_InsuranceCover_Limits = ((String)common.NB_excel_data_map.get("XOC_CL_PL_LayerInsuranceCoverList")).split(",");
		customAssert.assertTrue(k.Click("XOC_CL_PL_LayerInsuranceCoverList"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");
		List<WebElement> pl_ul_elements = driver.findElements(By.xpath("//*[contains(@id,'xoc_cmb_pl_insurer_primary')]")); // *[contains(@id,'_cmb_pl_insurer_primary')]
		for(String ins_limit : PL_InsuranceCover_Limits){
			for(WebElement each_ul : pl_ul_elements){
				//customAssert.assertTrue(k.Click("XOC_CL_PL_LayerInsuranceCoverList"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed()){ 
					each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
					customAssert.assertTrue(k.Click("XOC_CL_PL_LayerInsuranceCoverList"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");	
			}	else{
					continue;}
				break;
			}
		} 
		
		customAssert.assertTrue(k.Input("XOC_CL_PL_PrimaryLayerPremium", (String)map_data.get("XOC_CL_PL_PrimaryLayerPremium")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_PL_PrimaryLayerPremium", "value"),"Public Liability Primary Layer Premium Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_CL_PL_PrimaryLayerOfIndeminity", (String)map_data.get("XOC_CL_PL_PrimaryLayerOfIndeminity")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_PL_PrimaryLayerOfIndeminity", "value"),"Public Liability Primary Layer of Indeminity Field should not be empty  .");
		
		
		customAssert.assertTrue(k.Input("XOC_CL_PL_ProductDesc", (String)map_data.get("XOC_CL_PL_ProductDesc")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_PL_ProductDesc", "value"),"Public Liability Primary Layer of Indeminity Field should not be empty  .");
		String[] PL_Product_Limits = null;
		
		PL_Product_Limits = ((String)common.NB_excel_data_map.get("XOC_CL_PL_ProductLibilityRskMgmtFtrs")).split(",");
		//List<WebElement> prod_ul_elements = driver.findElements(By.xpath("//ul"));
		String item1 = "//*[text()='"; // Batch recording']
		String l_item = null;
		for(String ins_limit : PL_Product_Limits){
			//for(WebElement each_ul : prod_ul_elements){ 
				if(k.isDisplayedField("XOC_CL_PL_ProductLibilityRskMgmtFtrs")){
					customAssert.assertTrue(k.Click("XOC_CL_PL_ProductLibilityRskMgmtFtrs"),"Error while Clicking PL product List object . ");
				}else {
					customAssert.assertTrue(k.Click("XOQ_CL_PL_ProductLibilityRskMgmtFtrs"),"Error while Clicking PL product List object . ");
				}
				
				k.waitTwoSeconds();
				ins_limit = ins_limit.trim();
				l_item = item1+ins_limit+"']";
				if(driver.findElement(By.xpath(l_item)).isDisplayed())
					driver.findElement(By.xpath(l_item)).click();
				else
					continue;
				 
			//}
		} 
		
		List<WebElement> PLPD = driver.findElements(By.xpath("//*[contains(@name,'xoc_cmb_pl_risk_') and contains(@name,'_desc')]"));
			for(WebElement pl_desc:PLPD){ 
				if(pl_desc.isDisplayed())
					pl_desc.sendKeys("Test Details");
					
				else
					continue;
			}
		customAssert.assertTrue(k.Input("XOC_CL_PL_ProductDetails", (String)map_data.get("XOC_CL_PL_ProductDetails")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_PL_PrimaryLayerOfIndeminity", "value"),"Public Liability Product Details  Field should not be empty  .");
		if(k.isDisplayedField("XOQ_RequireAsbestosCover")){
			customAssert.assertTrue(k.DropDownSelection("XOQ_RequireAsbestosCover", (String)map_data.get("XOQ_RequireAsbestosCover")));
		}
		if(k.isDisplayedField("XOQ_RequireAsbestosCover") && common.product.contains("XOQ")){
			customAssert.assertTrue(k.DropDownSelection("XOQ_RequireAsbestosCover", (String)map_data.get("XOQ_RequireAsbestosCover")));
		}
		
		// Optional Extension Third Party Motor Liability
		if(((String)map_data.get("XOC_CL_TPMPL_OptionalExtn")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(k.DropDownSelection("XOC_CL_TPMPL_OptionalExtn", (String)map_data.get("XOC_CL_TPMPL_OptionalExtn")));
			
			customAssert.assertTrue(k.Input("XOC_CL_TPMPL_BookPremium", (String)map_data.get("CL_TP_BookPremium")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_TPMPL_BookPremium", "value"),"Third Party Motor Liability book premium Field should not be empty  .");
			customAssert.assertTrue(k.Input("XOC_CL_TPMPL_ExcessLayer", (String)map_data.get("XOC_CL_TPMPL_ExcessLayer")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_TPMPL_ExcessLayer", "value"),"Third Party Motor Liability Field should not be empty  .");
			customAssert.assertTrue(k.Input("XOC_CL_TPMPL_ExcessLiabilityIns", (String)map_data.get("XOC_CL_TPMPL_ExcessLiabilityIns")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_TPMPL_ExcessLiabilityIns", "value"),"Third Party Motor Liability Excess Liability Insurance Field should not be empty  .");
			String[] TPML_InsuranceCover_Limits = ((String)common.NB_excel_data_map.get("XOC_CL_TPMPL_LayerInsuranceCoverList")).split(",");
			customAssert.assertTrue(k.Click("XOC_CL_TPMPL_LayerInsuranceCoverList"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");
			List<WebElement> tpml_ul_elements = driver.findElements(By.xpath("//*[contains(@id,'xoc_cmb_thr_insurer_primary-')]"));
			
				
			for(String ins_limit : TPML_InsuranceCover_Limits){
				for(WebElement each_ul : tpml_ul_elements){
					
					k.waitTwoSeconds();
					ins_limit = ins_limit.trim();
					if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed())
						{each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
						customAssert.assertTrue(k.Click("XOC_CL_TPMPL_LayerInsuranceCoverList"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");
						}
					else
						continue;
					break;
				}
			}
			
			customAssert.assertTrue(k.Input("XOC_CL_TPMPL_PremiumLayerPremium", (String)map_data.get("XOC_CL_TPMPL_PremiumLayerPremium")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_TPMPL_PremiumLayerPremium", "value"),"Third Party Motor Liability  Primary Layer Premium Field should not be empty  .");
			customAssert.assertTrue(k.Input("XOC_CL_TPMPL_PremiumLayerofIndeminity", (String)map_data.get("XOC_CL_TPMPL_PremiumLayerofIndeminity")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_TPMPL_PremiumLayerofIndeminity", "value"),"Third Party Motor Liability  Primary Layer of Indeminity Field should not be empty  .");
			
//			String[] TPML_vhclType_Limits = ((String)common.NB_excel_data_map.get("XOC_CL_TPMPL_VehicleType")).split(",");
//			customAssert.assertTrue(k.Click("XOC_CL_TPMPL_VehicleType"),"Error while Clicking Vehical Type list object . ");
//			Thread.sleep(1000);
//			List<WebElement> tpml_VTul_elements = driver.findElements(By.xpath("//li[contains(@id,'xoc_cmb_thr_vehicle_type')]"));
//			if(tpml_VTul_elements.size()==0){
//				customAssert.assertTrue(k.Input("XOC_CL_TPMPL_VehicleType"," "),"Error while entering Vehicle type List object . ");
//				tpml_VTul_elements = driver.findElements(By.xpath("//li[contains(@id,'xoc_cmb_thr_vehicle_type')]"));
//			}
//			for(String ins_limit : TPML_vhclType_Limits){
//				for(WebElement each_ul : tpml_VTul_elements){
//					ins_limit = ins_limit.trim();
//					if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed())
//						{each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
//						customAssert.assertTrue(k.Click("XOC_CL_TPMPL_VehicleType"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");
//						}
//					else
//						continue;
//					break;
//				}
//			}
			driver.findElement(By.xpath("//*[@id='mainpanel']")).click();
			String[] InsuranceCover_Limits = ((String)common.NB_excel_data_map.get("XOC_CL_TPMPL_VehicleType")).split(",");
			List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
			for(String ins_limit : InsuranceCover_Limits){
				for(WebElement each_ul : ul_elements){
					customAssert.assertTrue(k.Click("XOC_CL_TPMPL_VehicleType"),"Error while Clicking Vehicle type List object . ");
					k.waitTwoSeconds();
					if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed())
						each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
					else
						continue;
					break;
				}
			}
			customAssert.assertTrue(k.Input("XOC_CL_TPMPL_NumberOfCars", (String)map_data.get("XOC_CL_TPMPL_NumberOfCars")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_TPMPL_NumberOfCars", "value"),"Third Party Motor Liability  Number of cars Field should not be empty  .");
			
		}	
		// Adding bespoke items
			String coverName = null;
			String Sval = (String) common.NB_excel_data_map.get("CL_AddBeSpokeItems");
			String bsAdd= (String) common.NB_excel_data_map.get("CL_addBespoke?");  
			if(Sval!=null && bsAdd.contains("Yes")){
					
					coverName="CL";
					customAssert.assertTrue(funcAddBespoke(map_data, Sval, coverName),"Unable to add bspoke item for CL cover");
					
					int tVal = k.getTableIndex("Actions", "xpath","html/body/div[3]/form/div/table");
					String bespoketableXpath ="html/body/div[3]/form/div/table[" +tVal+"]";
					if(driver.findElements(By.xpath(bespoketableXpath)).size()!=0){
						TestUtil.reportStatus("Bespoke Items have been listed into the table ", "Info", true);
					}else{
						TestUtil.reportStatus("Bespoke Items couldn't be added into the table ", "false", true);}
							
					customAssert.assertTrue(common.funcClickPageButton("Calculate Premium"),"");
				}
				
				//html/body/div[3]/form/div/table[5]/tbody/tr[1]/td[2]/input
						
				String sUniqueCol ="Tech. Adjust (%)";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
				coverName = "Combined Liability";
				String abvr = "CL_";
				
				customAssert.assertTrue(funcValidate_ManualRatedTables(map_data, sTablePath, coverName, abvr),"Unable to handle manual rated premium table on Third Party Motor Liability screen");	 
				
				return true;
		}	
	catch(Throwable t){
		return false; 
	}
}	

public boolean PublicProductsLiabilityPage(Map<Object, Object> map_data){
	boolean retValue = true;
	String abvr = null, coverName = null;
	String s_Sheet =null;
	
	
	try{
		customAssert.assertTrue(k.Input("XOC_ELPL_ExcessLayer", (String)map_data.get("XOC_PL_ExcessLayer")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_ELPL_ExcessLayer", "value"),"Public Liability excess Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_ELPL_ExcessLiabilityIns", (String)map_data.get("XOC_PL_ExcessLiabilityIns")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_ELPL_ExcessLiabilityIns", "value"),"Public liabilty Layer Insurance Field should not be empty  .");
		String[] PL_InsuranceCover_Limits = ((String)common.NB_excel_data_map.get("XOC_PL_LyerInsuranceCoverList")).split(",");
		customAssert.assertTrue(k.Click("XOC_ELPL_LyerInsuranceCoverList"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");
		List<WebElement> pl_ul_elements = driver.findElements(By.xpath("//li[contains(@id,'xoc_pl_insurer_primary')]")); // *[contains(@id,'_cmb_pl_insurer_primary')]
		for(String ins_limit : PL_InsuranceCover_Limits){
			for(WebElement each_ul : pl_ul_elements){
				//customAssert.assertTrue(k.Click("XOC_CL_PL_LayerInsuranceCoverList"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed()){ 
					each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
					customAssert.assertTrue(k.Click("XOC_ELPL_LyerInsuranceCoverList"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");	
			}	else{
					continue;}
				break;
			}
		} 
		
		customAssert.assertTrue(k.Input("XOC_ELPL_PrimaryLayerPremium", (String)map_data.get("XOC_PL_PrimaryLayerPremium")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_ELPL_PrimaryLayerPremium", "value"),"Public Liability Primary Layer Premium Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_ELPL_PrimaryLayerofIndeminity", (String)map_data.get("XOC_PL_PrimaryLayerofIndeminity")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_ELPL_PrimaryLayerofIndeminity", "value"),"Public Liability Primary Layer of Indeminity Field should not be empty  .");
		
		customAssert.assertTrue(k.Input("XOC_ELPL_ProductDesc", (String)map_data.get("XOC_PL_ProductDesc")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_ELPL_ProductDesc", "value"),"Product Description Field should not be empty  .");
//		customAssert.assertTrue(k.Input("XOC_CL_PL_ProductDesc", (String)map_data.get("XOC_CL_PL_ProductDesc")));
//		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_PL_ProductDesc", "value"),"Public Liability Primary Layer of Indeminity Field should not be empty  .");
		String[] PL_Product_Limits = null;
		
		PL_Product_Limits = ((String)common.NB_excel_data_map.get("XOC_PL_ProductLiabilityRskMgmtFtrs")).split(",");
		//List<WebElement> prod_ul_elements = driver.findElements(By.xpath("//ul"));
		String item1 = "//*[text()='"; // Batch recording']
		String l_item = null;
		for(String ins_limit : PL_Product_Limits){
			//for(WebElement each_ul : prod_ul_elements){ 
				if(k.isDisplayedField("XOC_ELPL_ProductLiabilityRskMgmtFtrs1"))
					customAssert.assertTrue(k.Click("XOC_ELPL_ProductLiabilityRskMgmtFtrs1"),"Error while Clicking PL product List object . ");
				else
					customAssert.assertTrue(k.Click("XOC_ELPL_ProductLiabilityRskMgmtFtrs"),"Error while Clicking PL product List object . ");
				
				k.waitTwoSeconds();
				ins_limit = ins_limit.trim();
				l_item = item1+ins_limit+"']";
				if(driver.findElement(By.xpath(l_item)).isDisplayed())
					driver.findElement(By.xpath(l_item)).click();
				else
					continue;
				 
			//}
		} 
//		 String[] rist_mgmt = ((String)common.NB_excel_data_map.get("PPL_PL_RiskManagement")).split(",");
//		 List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
//		 for(String risk : rist_mgmt){
//		 	for(WebElement each_ul : ul_elements){
//		 	    customAssert.assertTrue(k.Click("COB_PPL_PL_RiskManagement"),"Error while Clicking RiskManagement List object . ");
//				k.waitTwoSeconds();
//				if(each_ul.findElement(By.xpath("//li[text()='"+risk+"']")).isDisplayed())
//					each_ul.findElement(By.xpath("//li[text()='"+risk+"']")).click();
//				else
//					continue;
//				break;
//			}
//		 }
		
		List<WebElement> PLPD = driver.findElements(By.xpath("//textarea[contains(@name,'l_risk_') and contains(@name,'_desc')]"));
			for(WebElement pl_desc:PLPD){ 
				if(pl_desc.isDisplayed())
					pl_desc.sendKeys("Test Details");
					
				else
					continue;
			}
		
			if(k.isDisplayedField("XOC_ELPL_AsbestosReq")&& common.product.contains("XOQ"))
			customAssert.assertTrue(k.DropDownSelection("XOC_ELPL_AsbestosReq", (String)map_data.get("XOC_ELPL_AsbestosReq")));
			
		// Optional Extension Third Party Motor Liability
		if(((String)map_data.get("XOC_PL_TPMLExtns")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(k.DropDownSelection("XOC_ELPL_TPMLExtns", (String)map_data.get("XOC_PL_TPMLExtns")));
			
			customAssert.assertTrue(k.Input("XOC_ELPL_TPMLBookPremium", (String)map_data.get("PPL_TP_BookPremium")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_ELPL_TPMLBookPremium", "value"),"Third Party Motor Liability book premium Field should not be empty  .");
			customAssert.assertTrue(k.Input("XOC_TPML_ExcessLayer", (String)map_data.get("XOC_PL_TPML_ExcessLayer")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_TPML_ExcessLayer", "value"),"Third Party Motor Liability Field should not be empty  .");
			customAssert.assertTrue(k.Input("XOC_TPML_ExcessLiabilityIns", (String)map_data.get("XOC_PL_TPML_ExcessLiabilityIns")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_TPML_ExcessLiabilityIns", "value"),"Third Party Motor Liability Excess Liability Insurance Field should not be empty  .");
			String[] TPML_InsuranceCover_Limits = ((String)common.NB_excel_data_map.get("XOC_PL_TPML_LayerInsuranceCovelist")).split(",");
			customAssert.assertTrue(k.Click("XOC_ELPL_TPMLCoverlist"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");
			List<WebElement> tpml_ul_elements = driver.findElements(By.xpath("//li[contains(@id,'xoc_third_party_motor_insurer_primary_pl')]"));
			
				
			for(String ins_limit : TPML_InsuranceCover_Limits){
				for(WebElement each_ul : tpml_ul_elements){
					
					k.waitTwoSeconds();
					ins_limit = ins_limit.trim();
					if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed())
						{each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
						customAssert.assertTrue(k.Click("XOC_ELPL_TPMLCoverlist"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");
						}
					else
						continue;
					break;
				}
			}
			
			customAssert.assertTrue(k.Input("XOC_TPML_LayerPremium", (String)map_data.get("XOC_PL_TPML_LayerPremium")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_TPML_LayerPremium", "value"),"Third Party Motor Liability  Primary Layer Premium Field should not be empty  .");
			customAssert.assertTrue(k.Input("XOC_TPML_LimitOfIndemnity", (String)map_data.get("XOC_PL_TPML_LimitOfIndemnity")));
			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_TPML_LimitOfIndemnity", "value"),"Third Party Motor Liability  Primary Layer of Indeminity Field should not be empty  .");
			
//			String[] TPML_vhclType_Limits = ((String)common.NB_excel_data_map.get("XOC_PL_TPMLVehicleType")).split(",");
//			customAssert.assertTrue(k.Click("XOC_ELPL_TPMLVehicleType"),"Error while Clicking Vehical Type list object . ");
//			Thread.sleep(1000);
//			List<WebElement> tpml_VTul_elements = driver.findElements(By.xpath("//li[contains(@id,'xoc_third_party_motor_vehicle_type_pl')]"));
//			if(tpml_VTul_elements.size()==0){
//				customAssert.assertTrue(k.Input("XOC_ELPL_TPMLVehicleType"," "),"Error while entering Vehicle type List object . ");
//				tpml_VTul_elements = driver.findElements(By.xpath("//li[contains(@id,'xoc_third_party_motor_vehicle_type_pl')]"));
//			}
//			for(String ins_limit : TPML_vhclType_Limits){
//				for(WebElement each_ul : tpml_VTul_elements){
//					ins_limit = ins_limit.trim();
//					if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed())
//						{each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
//						customAssert.assertTrue(k.Click("XOC_ELPL_TPMLVehicleType"),"Error while Clicking PL Primary Layer Insurance Cover List object . ");
//						}
//					else
//						continue;
//					break;
//				}
//			}
			driver.findElement(By.xpath("//*[@id='mainpanel']")).click();
			String[] InsuranceCover_Limits = ((String)common.NB_excel_data_map.get("XOC_PL_TPMLVehicleType")).split(",");
			List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
			for(String ins_limit : InsuranceCover_Limits){
				for(WebElement each_ul : ul_elements){
					customAssert.assertTrue(k.Click("XOC_ELPL_TPMLVehicleType"),"Error while Clicking Vehicle type List object . ");
					k.waitTwoSeconds();
					if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed())
						each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
					else
						continue;
					break;
				}
			}
			
			
			String [] aa = {"xoc_third_party_motor_cars_pl","xoc_third_party_motor_1_17_tonnes_pl","xoc_third_party_motor_18_31_tonnes_pl","xoc_third_party_motor_32_44_tonnes_pl","xoc_third_party_motor_45_more_pl"};
			for(int i=0;i<aa.length;i++){
				if(driver.findElement(By.xpath("//input[@name='"+aa[i]+"']")).isDisplayed()){
					driver.findElement(By.xpath("//input[@name='"+aa[i]+"']")).sendKeys("1");
				}
				
			}
//			customAssert.assertTrue(k.Input("XOC_CL_TPMPL_NumberOfCars", (String)map_data.get("XOC_CL_TPMPL_NumberOfCars")));
//			customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CL_TPMPL_NumberOfCars", "value"),"Third Party Motor Liability  Number of cars Field should not be empty  .");
//			
		}	
		// Adding bespoke items
			String coverName1 = null;
			String Sval = (String) common.NB_excel_data_map.get("PPL_AddBeSpokeItems");
			String bsAdd= (String) common.NB_excel_data_map.get("PPL_addBespoke?");  
			if(Sval!=null && bsAdd.contains("Yes")){
					
					coverName1="PPL";
					customAssert.assertTrue(funcAddBespoke(map_data, Sval, coverName1),"Unable to add bspoke item for CL cover");
					
					int tVal = k.getTableIndex("Actions", "xpath","html/body/div[3]/form/div/table");
					String bespoketableXpath ="html/body/div[3]/form/div/table[" +tVal+"]";
					if(driver.findElements(By.xpath(bespoketableXpath)).size()!=0){
						TestUtil.reportStatus("Bespoke Items have been listed into the table ", "Info", true);
					}else{
						TestUtil.reportStatus("Bespoke Items couldn't be added into the table ", "false", true);}
							
					customAssert.assertTrue(common.funcClickPageButton("Calculate Premium"),"");
				}
				
				//html/body/div[3]/form/div/table[5]/tbody/tr[1]/td[2]/input
						
				String sUniqueCol ="Tech. Adjust (%)";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
				coverName1 = "Public & Products Liability";
				String abvr1 = "PPL_";
				
				customAssert.assertTrue(funcValidate_ManualRatedTables(map_data, sTablePath, coverName1, abvr1),"Unable to handle manual rated premium table on Third Party Motor Liability screen");	 
			
		 
		
		 
//		 customAssert.assertTrue(k.Input("COB_PPL_DescribeProducts", (String)map_data.get("PPL_DescribeProducts")),"Unable to put value in DescribeProducts field");
//		 customAssert.assertTrue(k.Input("COB_PPL_DeclarationCondition", (String)map_data.get("PPL_DeclarationCondition")),"Unable to put value in DeclarationCondition field");
//		 customAssert.assertTrue(k.DropDownSelection("COB_PPL_LOI_PPLiability", (String)map_data.get("PPL_LOI_PPLiability")),"Unable to put value in LOI field");
//		 customAssert.assertTrue(k.DropDownSelection("COB_PPL_TypeOfLimit", (String)map_data.get("PPL_TypeOfLimit")),"Unable to put value in TypeOfLimit field");
//		
		 // Add Edit Wages Breakdown :
		 
//		 String sValue = (String)map_data.get("PPL_AddEditWagesBreakdown");
//		 
//		 if(sValue.contains("Yes")){
//			 coverName1 = "Public & Products Liability";
//			 abvr1 = "PPL";
//			 customAssert.assertTrue(k.Click("COB_Btn_AddWagesBreakdown"),"Unable to click on AddWagesBreakdown button");
//			 customAssert.assertTrue(AddWages(map_data, coverName1, abvr1),"");
//		 }
//		 
//		String sVal = (String)map_data.get("PPL_AddBespoke");
//		 if(sVal.length() > 0){
//			 abvr1 = "PPL_AddB_";
//			 coverName1 = "PPL";
//			 customAssert.assertTrue(funcAddBespoke(map_data, sVal,coverName1),"Unable to add bspoke item for PPL cover");
//		 }
//		 
//		 // Click on Calculate Premium button :
//		 
//		 customAssert.assertTrue(k.Click("COB_Btn_ApplyBookRate"),"Unable to click on ApplyBookRate button");
//		 
//		 // Get table id :
//		 
//		 	String sUniqueCol1 ="Tech. Adjust (%)";
//			int tableId1 = 0;
//			
//			String sTablePath1 = "html/body/div[3]/form/div/table";
//			
//			tableId1 = k.getTableIndex(sUniqueCol1,"xpath",sTablePath1);
//			sTablePath1 = "html/body/div[3]/form/div/table["+ tableId1 +"]";
//			coverName1 = "Public & Products Liability";
//			abvr1 = "PPL_";
//			
//			customAssert.assertTrue(funcValidate_ManualRatedTables(map_data, sTablePath1, coverName1, abvr1),"Unable to handle manual rated premium table on Property Owners Liability screen");	 
						
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
	
	try{
			
		customAssert.assertTrue(k.Input("COB_BD_Percentage", (String)map_data.get("BD_Percentage")),"Unable to put value in Percentage field");
		
		 // Turnover Breakdown by Geographical Limit
		
				 String s_Xpath = "//table[contains(@id,'_geo_limit_bd_table')]";
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
				
		// Contracting Turnover by Area of Work
				
				s_Xpath = "//table[contains(@id,'_percentage_bd_table')]";
				s_Table  = driver.findElement(By.xpath(s_Xpath));
				 
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

public boolean EmployersLiabilityPage(Map<Object, Object> map_data){
	boolean retValue = true;
	String abvr = null, coverName = null;
	String s_Sheet =null;
	
	try{
		customAssert.assertTrue(k.Input("XOC_ELPL_ExcessLayer", (String)map_data.get("XOC_EL_ExcessLayer")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_ELPL_ExcessLayer", "value"),"Public Liability excess Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_ELPL_ExcessLiabilityIns", (String)map_data.get("XOC_EL_ExcessLiabilityIns")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_ELPL_ExcessLiabilityIns", "value"),"Public liabilty Layer Insurance Field should not be empty  .");

		String[] InsuranceCover_Limits = ((String)map_data.get("XOC_EL_LyerInsuranceCoverList")).split(",");
		List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
		for(String ins_limit : InsuranceCover_Limits){
			for(WebElement each_ul : ul_elements){
				customAssert.assertTrue(k.Click("XOC_ELPL_LyerInsuranceCoverList"),"Error while Clicking Primary Layer Insurance Cover List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		
		
		customAssert.assertTrue(k.Input("XOC_ELPL_PrimaryLayerPremium", (String)map_data.get("XOC_EL_PrimaryLayerPremium")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_ELPL_PrimaryLayerPremium", "value"),"Public Liability Primary Layer Premium Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_ELPL_PrimaryLayerofIndeminity", (String)map_data.get("XOC_EL_PrimaryLayerofIndeminity")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_ELPL_PrimaryLayerofIndeminity", "value"),"Public Liability Primary Layer of Indeminity Field should not be empty  .");

		// Adding bespoke items
			String coverName1 = null;
			String Sval = (String) map_data.get("EL_AddBeSpokeItems");
			String bsAdd= (String) map_data.get("EL_addBespoke?");  
			if(Sval!=null && bsAdd.contains("Yes")){
					
					coverName1="EL";
					customAssert.assertTrue(funcAddBespoke(map_data, Sval, coverName1),"Unable to add bspoke item for CL cover");
					
					int tVal = k.getTableIndex("Actions", "xpath","html/body/div[3]/form/div/table");
					String bespoketableXpath ="html/body/div[3]/form/div/table[" +tVal+"]";
					if(driver.findElements(By.xpath(bespoketableXpath)).size()!=0){
						TestUtil.reportStatus("Bespoke Items have been listed into the table ", "Info", true);
					}else{
						TestUtil.reportStatus("Bespoke Items couldn't be added into the table ", "false", true);}
							
					customAssert.assertTrue(common.funcClickPageButton("Calculate Premium"),"");
				}
				
								
				String sUniqueCol ="Tech. Adjust (%)";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
				coverName1 = "Employers Liability";
				String abvr1 = "EL_";
				
				customAssert.assertTrue(funcValidate_ManualRatedTables(map_data, sTablePath, coverName1, abvr1),"Unable to handle manual rated premium table on Third Party Motor Liability screen");	 
				
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

public boolean JCTPage(Map<Object, Object> map_data){
	boolean retValue = true;
	String abvr = null, coverName = null;
	String s_Sheet =null;
	
	try{
		customAssert.assertTrue(k.Input("XOC_JCT_ExcessLayer", (String)map_data.get("XOC_JCT_ExcessLayer")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_JCT_ExcessLayer", "value"),"JCT excess Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_JCT_ExcessLiabilityIns", (String)map_data.get("XOC_JCT_ExcessLiabilityIns")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_JCT_ExcessLiabilityIns", "value"),"Public liabilty Layer Insurance Field should not be empty  .");

		String[] InsuranceCover_Limits = ((String)common.NB_excel_data_map.get("XOC_JCT_LyerInsuranceCoverList")).split(",");
		List<WebElement> ul_elements = driver.findElements(By.xpath("//ul"));
		for(String ins_limit : InsuranceCover_Limits){
			for(WebElement each_ul : ul_elements){
				customAssert.assertTrue(k.Click("XOC_JCT_LyerInsuranceCoverList"),"Error while Clicking Primary Layer Insurance Cover List object . ");
				k.waitTwoSeconds();
				if(each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).isDisplayed())
					each_ul.findElement(By.xpath("//li[text()='"+ins_limit+"']")).click();
				else
					continue;
				break;
			}
		}
		
		customAssert.assertTrue(k.Input("XOC_JCT_PrimaryLayerPremium", (String)map_data.get("XOC_JCT_PrimaryLayerPremium")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_JCT_PrimaryLayerPremium", "value"),"JCT Primary Layer Premium Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_JCT_PrimaryLayerofIndeminity", (String)map_data.get("XOC_JCT_PrimaryLayerofIndeminity")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_JCT_PrimaryLayerofIndeminity", "value"),"JCT Primary Layer of Indeminity Field should not be empty  .");
		
		customAssert.assertTrue(k.Input("XOC_JCT_POC_From", (String)map_data.get("XOC_JCT_POC_From")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_JCT_POC_From", "value"),"JCT Primary Layer Premium Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_JCT_POC_To", (String)map_data.get("XOC_JCT_POC_To")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_JCT_POC_To", "value"),"JCT Primary Layer of Indeminity Field should not be empty  .");
		
		customAssert.assertTrue(k.Input("XOC_JCT_DefectLbltyPeriod", (String)map_data.get("XOC_JCT_DefectLbltyPeriod")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_JCT_DefectLbltyPeriod", "value"),"JCT Defect Liability Period Field should not be empty  .");
		if (((String)map_data.get("XOC_JCT_DefectLbltyPeriod")).contains("Yes")){
		customAssert.assertTrue(k.Input("XOC_JCT_JCTPeriod", (String)map_data.get("XOC_JCT_JCTPeriod")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_JCT_JCTPeriod", "value"),"JCT Preiod Field should not be empty  .");}
		
		customAssert.assertTrue(k.Input("XOC_JCT_ContractValue", (String)map_data.get("XOC_JCT_ContractValue")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_JCT_ContractValue", "value"),"JCT Contract Value Field should not be empty  .");
		customAssert.assertTrue(k.Input("XOC_JCT_ContractWrksDesc", (String)map_data.get("XOC_JCT_ContractWrksDesc")));
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_JCT_ContractWrksDesc", "value"),"JCT Contract Value description Field should not be empty  .");
		customAssert.assertTrue(k.DropDownSelection("XOC_JCT_HaveAnySchedules", (String)map_data.get("XOC_JCT_HaveAnySchedules")));
		customAssert.assertTrue(k.DropDownSelection("XOC_JCT_Demolition", (String)map_data.get("XOC_JCT_Demolition")));
		customAssert.assertTrue(k.DropDownSelection("XOC_JCT_partyWalls", (String)map_data.get("XOC_JCT_partyWalls")));
		customAssert.assertTrue(k.DropDownSelection("XOC_JCT_HazardousWork", (String)map_data.get("XOC_JCT_HazardousWork")));
		customAssert.assertTrue(k.DropDownSelection("XOC_JCT_HazardousWork", (String)map_data.get("XOC_JCT_HazardousWork")));
		
		List<WebElement> dtls = driver.findElements(By.xpath("//*[contains(@name,'_details')]"));
		for(WebElement pl_desc:dtls){ 
			if(pl_desc.isDisplayed()){
				k.ScrollInVewWebElement(pl_desc);
				pl_desc.sendKeys("Test Details");}
			else
				continue;
		}
	
		// Adding bespoke items
			String coverName1 = null;
			String Sval = (String) common.NB_excel_data_map.get("JCT_AddBeSpokeItems");
			String bsAdd= (String) common.NB_excel_data_map.get("JCT_addBespoke?");  
			if(Sval!=null && bsAdd.contains("Yes")){
					
					coverName1="JCT";
					customAssert.assertTrue(funcAddBespoke(map_data, Sval, coverName1),"Unable to add bspoke item for CL cover");
					
					int tVal = k.getTableIndex("Actions", "xpath","html/body/div[3]/form/div/table");
					String bespoketableXpath ="html/body/div[3]/form/div/table[" +tVal+"]";
					if(driver.findElements(By.xpath(bespoketableXpath)).size()!=0){
						TestUtil.reportStatus("Bespoke Items have been listed into the table ", "Info", true);
					}else{
						TestUtil.reportStatus("Bespoke Items couldn't be added into the table ", "false", true);}
							
					customAssert.assertTrue(common.funcClickPageButton("Calculate Premium"),"");
				}
				
								
				String sUniqueCol ="Tech. Adjust (%)";
				int tableId = 0;
				
				String sTablePath = "html/body/div[3]/form/div/table";
				
				tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
				sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
				coverName1 = "JCT 6.5.1";
				String abvr1 = "JCT_";
				
				customAssert.assertTrue(funcValidate_ManualRatedTables(map_data, sTablePath, coverName1, abvr1),"Unable to handle manual rated premium table on Third Party Motor Liability screen");	 
				
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
//------------------------------------ajinkya----------------------------------------------------------//

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
		
System.out.println("Test method of CAN For - "+code);
		
		customAssert.assertTrue(XOC_CancelPolicy(common.CAN_excel_data_map), "Unable to Logout.");
		customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
		
		customAssert.assertTrue(common_XOC.XOC_Cancel_PremiumSummary(common.CAN_excel_data_map), "Issue in verifying Premium summary for cancellation");
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""), "Unable to click on policies tab");
		Assert.assertTrue(common_VELA.funcStatusHandling(common.NB_excel_data_map,code,TestBase.businessEvent));
				
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


public boolean XOC_CancelPolicy(Map<Object, Object> map_data) throws ErrorInTestMethod{
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
                        
           customAssert.assertTrue(k.Click("XOC_CP_CancellationDate"), "Unable to enter Cancellation date.");
           customAssert.assertTrue(k.Input("XOC_CP_CancellationDate", Cancellation_date),"Unable to Enter Cancellation date.");
           customAssert.assertTrue(k.Click("SPI_Btn_Calender_Done"), "Unable to click on done button in calander.");
           customAssert.assertTrue(!k.getAttributeIsEmpty("XOC_CP_CancellationDate", "value"),"XOC_CP_CancellationDate Field Should Contain Valid Value on Cancel Policy page .");
           common.NB_excel_data_map.put("Cancellation_date", Cancellation_date);
           customAssert.assertTrue(k.Input("XOC_CP_CancellationReason", (String)map_data.get("CP_CancellationReason")),"Unable to Enter Cancellation Reason.");
           customAssert.assertTrue(common.funcButtonSelection("Continue"), "Unable to click on Cancel Policy Button");
                        
           // Read Cancellation Return Premium Summary and put values to Map  :
           
         XOC_Cancel_RetrunPremiumTable(map_data);
           
           customAssert.assertTrue(k.Click("XOC_Btn_FinalCancelPolicy"), "Unable to click on Cancel Polcy button after verifying Cancellation Return Premium");
           customAssert.assertTrue(k.AcceptPopup(), "Unable to handl pop up");
                  
           return retVal;
           
    
    }catch(Throwable t){
          return false;
    }
           
}

public boolean XOC_Cancel_PremiumSummary(Map<Object, Object> map_data) throws ErrorInTestMethod{
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
					
					common.compareValues(s_NetNetP, common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionName).get("Net Net Premium"), "Cancellation Net Net Premium");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_PenCommRate,common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionName).get("Pen Comm %"), "Cancellation Pen Comm Percentage");
					}				
					common.compareValues(s_PenCommAmt, common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionName).get("Pen Comm"), "Cancellation Pen commm Amount");
					common.compareValues(s_NetP, common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionName).get("Net Premium"), "Cancellation Net Premium");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_BrokerCommRate, common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionName).get("Broker Comm %"), "Cancellation Broker Comm rate");
					}
					
					common.compareValues(s_BrokerCommAmt, common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionName).get("Broker Commission"), "Cancellation Broker commission amount");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_GrossCommRate, common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionName).get("Gross Comm %"), "Cancellation Gross comm rate");
					}
					
					common.compareValues(s_GrossCommAmt, common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionName).get("Gross Premium"), "Cancellation Gross Premium");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_InsTRate, common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionName).get("Insurance Tax Rate"), "Cancellation Ins tax rate");
					}
					
					common.compareValues(s_InsTAmt, common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionName).get("Insurance Tax"), "Cancellation Ins tax Premium");
					common.compareValues(s_TotalP, common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionName).get("Total Premium"), "Cancellation Total Premium");
										
				}	
			}
				
			return retVal;		
	
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}



public boolean XOC_Cancel_RetrunPremiumTable(Map<Object, Object> map_data) throws ErrorInTestMethod{
	boolean retVal = true;
	String testName = (String)common.CAN_excel_data_map.get("Automation Key");
	
	try{
					
		int policy_Duration = Integer.parseInt(k.getText("XOC_CAN_Duration"));
		int DaysRemain = Integer.parseInt(k.getText("XOC_CAN_DaysRemain"));
		
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
										common_XOC.CAN_XOC_ReturnP_Values_Map.put(sectionName, ReturnP_Table_TotalVal);
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
								
								common_XOC.CAN_XOC_ReturnP_Values_Map.put(sectionName, ReturnP_Table_CoverVal);
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
						Can_returnP_Error = Can_returnP_Error + Can_ReturnP_Total_Validation(sectionNames,common_XOC.CAN_XOC_ReturnP_Values_Map);
					else
						Can_returnP_Error = Can_returnP_Error + CanReturnPTable_CoverSection_Validation(policy_Duration,DaysRemain, s_Name, common_XOC.CAN_XOC_ReturnP_Values_Map);								
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
					exp_value = exp_value + common_XOC.CAN_XOC_ReturnP_Values_Map.get(section).get("Net Net Premium");
			}
			
			String canRP_NetNetP_actual = Double.toString(common_XOC.CAN_XOC_ReturnP_Values_Map.get("Totals").get("Net Net Premium"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_NetNetP_actual)," Net Net Premium");

			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_XOC.CAN_XOC_ReturnP_Values_Map.get(section).get("Pen Comm");
			}
			String canRP_pc_actual = Double.toString(common_XOC.CAN_XOC_ReturnP_Values_Map.get("Totals").get("Pen Comm"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_pc_actual)," Pen Commission");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_XOC.CAN_XOC_ReturnP_Values_Map.get(section).get("Net Premium");
			}
			String canRP_netP_actual = Double.toString(common_XOC.CAN_XOC_ReturnP_Values_Map.get("Totals").get("Net Premium"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_netP_actual),"Net Premium");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_XOC.CAN_XOC_ReturnP_Values_Map.get(section).get("Broker Commission");
			}
			String canRP_bc_actual =  Double.toString(common_XOC.CAN_XOC_ReturnP_Values_Map.get("Totals").get("Broker Commission"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_bc_actual),"Broker Commission");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_XOC.CAN_XOC_ReturnP_Values_Map.get(section).get("Gross Premium");
			}
			String canRP_grossP_actual = Double.toString(common_XOC.CAN_XOC_ReturnP_Values_Map.get("Totals").get("Gross Premium"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_grossP_actual)," Gross Premium");
		
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_XOC.CAN_XOC_ReturnP_Values_Map.get(section).get("Insurance Tax");
			}
			String canRP_InsuranceTax_actual = Double.toString(common_XOC.CAN_XOC_ReturnP_Values_Map.get("Totals").get("Insurance Tax"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_InsuranceTax_actual),"Insurance Tax");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + common_XOC.CAN_XOC_ReturnP_Values_Map.get(section).get("Total Premium");
			}
			String canRP_p_actual = Double.toString(common_XOC.CAN_XOC_ReturnP_Values_Map.get("Totals").get("Total Premium"));
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
			double annual_NetNetP = Double.parseDouble((String)map_data.get("PS_"+code+"_NetNetPremium"));
			String canRP_NetNetP_expected = Double.toString((annual_NetNetP/365)*DaysRemain);
			String canRP_NetNetP_actual = Double.toString(common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionNames).get("Net Net Premium"));
			CommonFunction.compareValues(Double.parseDouble(canRP_NetNetP_expected),Double.parseDouble(canRP_NetNetP_actual)," Net Net Premium");
			
			//COB CAN Transaction Pen commission Calculation : 
			double canRP_pen_comm = (( Double.parseDouble(canRP_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate"))/100)));
			String canRP_pc_expected = common.roundedOff(Double.toString(canRP_pen_comm));
			String canRP_pc_actual = Double.toString(common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionNames).get("Pen Comm"));
			CommonFunction.compareValues(Double.parseDouble(canRP_pc_expected),Double.parseDouble(canRP_pc_actual)," Pen Commission");
			
			
			//COB CAN Transaction Net Premium verification : 
			double canRP_netP = Double.parseDouble(canRP_pc_expected) + Double.parseDouble(canRP_NetNetP_expected);
			String canRP_netP_expected = common.roundedOff(Double.toString(canRP_netP));
			String canRP_netP_actual = Double.toString(common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionNames).get("Net Premium"));
			CommonFunction.compareValues(Double.parseDouble(canRP_netP_expected),Double.parseDouble(canRP_netP_actual),"Net Premium");
			
			
			//COB CAN Transaction Broker commission Calculation : 
			double canRP_broker_comm = ((Double.parseDouble(canRP_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate"))/100)));
			String canRP_bc_expected = common.roundedOff(Double.toString(canRP_broker_comm));
			String canRP_bc_actual =  Double.toString(common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionNames).get("Broker Commission"));
			CommonFunction.compareValues(Double.parseDouble(canRP_bc_expected),Double.parseDouble(canRP_bc_actual),"Broker Commission");
			
			
			//COB CAN Transaction GrossPremium verification : 
			double canRP_grossP = Double.parseDouble(canRP_netP_expected) + Double.parseDouble(canRP_bc_expected);
			String canRP_grossP_actual = Double.toString(common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionNames).get("Gross Premium"));
			CommonFunction.compareValues(canRP_grossP,Double.parseDouble(canRP_grossP_actual),sectionNames+" Transaction Gross Premium");
			
			
			double canRP_InsuranceTax = (canRP_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_IPT")))/100.0;
			canRP_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(canRP_InsuranceTax)));
			String canRP_InsuranceTax_actual = Double.toString(common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionNames).get("Insurance Tax"));
			CommonFunction.compareValues(canRP_InsuranceTax,Double.parseDouble(canRP_InsuranceTax_actual),"Insurance Tax");
						
			//COB CAN  Transaction Total Premium verification : 
			double canRP_Premium = canRP_grossP + canRP_InsuranceTax;
			String canRP_p_expected = common.roundedOff(Double.toString(canRP_Premium));
			
			String canRP_p_actual = Double.toString(common_XOC.CAN_XOC_ReturnP_Values_Map.get(sectionNames).get("Total Premium"));
			
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
	  		//c.add(d,ammendmet_period);
	  		//c.add((String)data_map.get("PS_PolicyStartDate"), ammendmet_period);
	  	  	//dateobj = df.parse(df.format(c.getTime()));
	  	}else{
	  		c.add(Calendar.DATE, ammendmet_period);
	  	  	dateobj = df.parse(df.format(c.getTime()));
	  	}
	  	
	  	
	  	
	  
	      customAssert.assertTrue(k.Click("SPI_Endorsement_eff_date"), "Unable to enter Endorsement effective Date.");
	      customAssert.assertTrue(k.Type("SPI_Endorsement_eff_date", df.format(dateobj)), "Unable to Enter Endorsement effective Date .");
	      customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
	      customAssert.assertTrue(k.Input("SPI_Reson_for_Endors", (String)data_map.get("MTA_Reason_for_Endorsement")),"Unable to Enter Reason for Endorsement");
	      Endorse_Date = k.getAttribute("SPI_Endorsement_eff_date", "value");
	      
	      if(TestBase.businessEvent.equalsIgnoreCase("Renewal")){
	      	WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "MTA_Endorsement", testName, "MTA_EffectiveDate", k.getAttribute("SPI_Endorsement_eff_date", "value"),data_map);
	      }else{
	      	WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", testName, "MTA_EffectiveDate", k.getAttribute("SPI_Endorsement_eff_date", "value"),data_map);
	      }
	      customAssert.assertTrue(common.funcButtonSelection("Create Endorsement"), "Unable to click on Create Endorsement button .");
	     	
	      //Writing to MTA Excel
	  	WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "MTA_Endorsement", testName, "MTA_PolicyNumber", k.getText("SPI_MTA_policy_number"),data_map);
	  	/*if(common.currentRunningFlow.equals("MTA")){
	  		if(((String)data_map.get("MTA_Status")).equals("Endorsement Rewind"))
	  			WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "MTA_Endorsement", testName, "MTA_isMTARewind", "Y",data_map);
	  		else
	  			WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "MTA_Endorsement", testName, "MTA_isMTARewind", "N",data_map);
	  	}*/
	      
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
					c_locator = coverWithLocator.split("__")[1];
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
//END of Function File *************************************

}
