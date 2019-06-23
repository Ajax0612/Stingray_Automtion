package com.selenium.commonfiles.base;

import static com.selenium.commonfiles.util.TestUtil.WriteDataToXl;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.selenium.commonfiles.util.ErrorInTestMethod;
import com.selenium.commonfiles.util.ErrorUtil;
import com.selenium.commonfiles.util.ObjectMap;
import com.selenium.commonfiles.util.TestUtil;



public class CommonFunction_PIA extends TestBase{

	
Properties PIA_Rater = new Properties();
DateFormat df = null;
public int actual_no_of_years=0,err_count=0,trans_error_val=0, Can_returnP_Error=0;
public double spi_GrossFee = 0.0;

public static double TotalFeesIncome_Estimate = 0.00, TotalFeesIncome_2017 = 0.00, TotalFeesIncome_2016 = 0.00, TotalFeesIncome_2015 = 0.00, TotalFeesIncome_2014 = 0.00, Total_Average = 0.00;
public static double AvgReagon_1 = 0.00, AvgReagon_2 = 0.00, AvgReagon_3 = 0.00, AvgReagon_4 = 0.00, AvgTotal = 0.00;
public static double burnRate = 0.00, dAdminFee_CoverSPI = 0.00, aAdminFee_CoverSE = 0.00;
public static int duration_SoPI;
public String currentFlow = null;

Date currentDate = new Date();

public static int global_PeriodMonths =0;
public static double global_FeesIncome = 0.00, global_IndemnityLimit = 0.00, global_SOI_BookRate = 0.00, global_SOI_InitialP = 0.00, gloabal_SOI_BookP = 0.00, global_SOI_CommAdjust = 0.00, global_SOI_RevisedP = 0.00, global_SOI_sPremium = 0.00;
public static double global_SOI_GrossP = 0.00, global_SOI_GrossFees = 0.00, global_SOI_TotalPremium = 0.00, global_SOI_AnnualP = 0.00, global_SOI_TecgAdjust = 0.00, global_SOI_NetNetP = 0.00 ;
public static double Refer_PenComm = 0.00, Refer_BrokerComm = 0.00, Refer_InsTax = 0.00;
public static double PS_PenComm = 0.00, PS_BrokerComm = 0.00, PS_InsTaxRate = 0.00;
public double PI_pdf_InsuranceTax = 0.0, PI_pdf_GrossPremium = 0.0,SEL_pdf_InsuranceTax = 0.0, SEL_pdf_GrossPremium = 0.0;
public String Refer_Policy_SDate, Refer_Policy_EDate, isRewindSelected = "No";
public boolean isMTARewindFlow = false, isRenewalRewindFlow=false;

public double global_PreviousPremium  = 0.00;

public static double global_SEL_AdminFees  = 0.00, global_SEL_GrossP = 0.00, global_SEL_PenComm = 0.00, global_SEL_BrokerComm = 0.00, global_SEL_GrossComm = 0.00, global_SEL_BookP =0.00;


public Map<String,Double> PIA_Rater_output = new HashMap<>();

public String Endorsement_date = null, dateDiff_Endorse;

/**
 * 
 * This method handles all New Business Cases for SPI product.
 * 
 */
public void NewBusinessFlow(String code,String event) throws ErrorInTestMethod{
	
	String testName = (String)common.NB_excel_data_map.get("Automation Key");
	common_PIA.currentFlow="NB";
	common.currentRunningFlow="NB";
	
	try{
		customAssert.assertTrue(common.StingrayLogin("IRS"),"Unable to login.");
		customAssert.assertTrue(common.checkClient(common.NB_excel_data_map,code,event),"Unable to check Client.");
		customAssert.assertTrue(common.createNewQuote(common.NB_excel_data_map,code,event), "Unable to create new quote.");
		customAssert.assertTrue(common.selectLatestQuote(common.NB_excel_data_map,code,event), "Unable to select quote from table.");
		customAssert.assertTrue(funcPolicyGeneral(common.NB_excel_data_map,code,event), "Policy General function having issue .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(common.funcCovers(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Material Facts & Declarations"),"Issue while Navigating to Material Facts & Declarations  . ");
		customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
	
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Claims History"),"Issue while Navigating to Specified Perils  . ");
		customAssert.assertTrue(funcClaimsHistory(common.NB_excel_data_map), "Claims History function having issue .");
		
		if(((String)common.NB_excel_data_map.get("CD_SolicitorsPI")).equals("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Solicitors PI"),"Issue while Navigating to Solicitors PI. ");
			customAssert.assertTrue(Solicitors_PI(common.NB_excel_data_map), "Solicitors PI function is having issue(S) . ");
		}
		
		if(((String)common.NB_excel_data_map.get("CD_SolicitorsExcessLayer")).equals("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Solicitors Excess Layer"),"Issue while Navigating to Solicitors Excess Layer. ");
			customAssert.assertTrue(func_Solicitors_ExcessLayer(common.NB_excel_data_map),"Issue while Navigating to Solicitors Excess Layer page  . ");
		}
		
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(funcPremiumSummary(common.NB_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
		
		Assert.assertTrue(common.funcStatusHandling(common.NB_excel_data_map,code,event));
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		
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


/**
 * 
 * This method handles all MTA Cases for SPI product.
 * @throws ErrorInTestMethod 
 * 
 */
public void MTAFlow_Org(String code,String fileName) throws ErrorInTestMethod{
	
	String testName = (String)common.MTA_excel_data_map.get("Automation Key");
	try{
		NewBusinessFlow(code,"NB");
		common_PIA.currentFlow="MTA";
		common.currentRunningFlow="MTA";
		customAssert.assertTrue(funcCreateEndorsement(),"Error while creating Endorsement . ");
		Assert.assertTrue(funcDecideMTAFlow(), "Error in decide MTA flow method");
		
		if(((String)common.MTA_excel_data_map.get("CD_MTAAddRemoveCover")).equals("Add")){
			
			if(((String)common.MTA_excel_data_map.get("CD_MTA_SolicitorsPI")).equals("Yes")){
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Solicitors PI"),"Issue while Navigating to Solicitors PI. ");
				customAssert.assertTrue(Solicitors_PI(common.MTA_excel_data_map), "MTA Add Cover Solicitors PI function is having issue(S) . ");
			}else if(((String)common.MTA_excel_data_map.get("CD_MTA_SolicitorsExcessLayer")).equals("Yes")){
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Solicitors Excess Layer"),"Issue while Navigating to Solicitors Excess Layer . ");
				customAssert.assertTrue(func_Solicitors_ExcessLayer(common.MTA_excel_data_map),"MTA Add Cover Solicitors Excess Layer page having issues  . ");
			}
		}
		
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(func_MTAPremiumSummary(common.MTA_excel_data_map,TestBase.product,TestBase.businessEvent), "Premium Summary function is having issue(S) . ");
		Assert.assertTrue(common.funcStatusHandling(common.MTA_excel_data_map,code,TestBase.businessEvent));
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		
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

public void RewindFlow(String code,String event) throws ErrorInTestMethod{
	String testName = (String)common.Rewind_excel_data_map.get("Automation Key");
	try{
		
		if(!common.currentRunningFlow.equalsIgnoreCase("Renewal") && !common.currentRunningFlow.equalsIgnoreCase("MTA")){
			NewBusinessFlow(code,"NB");
		}
		common.currentRunningFlow="Rewind";
		String navigationBy = CONFIG.getProperty("NavigationBy");
		//common_HHAZ.PremiumFlag = false;
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common.funcRewind());
		
		TestUtil.reportStatus("<b> -----------------------Rewind flow is started---------------------- </b>", "Info", false);
		
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
		customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
		if(TestBase.businessEvent.equalsIgnoreCase("MTA"))
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status (Endorsement Submitted (Rewind)) function is having issue(S) . ");
		else
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		
		
		customAssert.assertTrue(funcPolicyGeneral_MTA(common.Rewind_excel_data_map,code,"MTA"), "MTA Policy General function having issue .");
		
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(funcUpdateCoverDetails_MTA(common.Rewind_excel_data_map),"Error in selecting cover for MTA.");
			
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Claims History"),"Issue while Navigating to Specified Perils  . ");
		customAssert.assertTrue(funcClaimsHistory(common.Rewind_excel_data_map), "Rewind Claims History function having issue .");
	
		
		if(((String)common.Rewind_excel_data_map.get("CD_SolicitorsPI")).equals("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Solicitors PI"),"Issue while Navigating to Solicitors PI. ");
			customAssert.assertTrue(Solicitors_PI_MTA(common.Rewind_excel_data_map), "Solicitors PI function is having issue(S) . ");
		}
		
		if(((String)common.Rewind_excel_data_map.get("CD_SolicitorsExcessLayer")).equals("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Solicitors Excess Layer"),"Issue while Navigating to Solicitors Excess Layer. ");
			customAssert.assertTrue(func_Solicitors_ExcessLayer(common.Rewind_excel_data_map),"Issue while Navigating to Solicitors Excess Layer page  . ");
		}
		
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary. ");
		
		
		if(TestBase.businessEvent.equalsIgnoreCase("MTA"))
			customAssert.assertTrue(func_MTAPremiumSummary_New(common.Rewind_excel_data_map,code,event), "Rewind MTA Premium Summary in function is having issue(S) . ");
		else
			customAssert.assertTrue(funcPremiumSummary(common.Rewind_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
		if(!TestBase.businessEvent.equalsIgnoreCase("Renewal") && !TestBase.businessEvent.equalsIgnoreCase("MTA")){
			customAssert.assertTrue(common.funcStatusHandling(common.Rewind_excel_data_map,code,event));
		}
		
		
		/*customAssert.assertTrue(funcPolicyGeneral(common.Rewind_excel_data_map,code,event), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(common_HHAZ.funcCovers(common.Rewind_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts and Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(MaterialFactsDeclerationPage(), "MaterialFactsDeclerationPage function is having issue(S) . ");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcPreviousClaims(common.Rewind_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Claims Experience"),"Issue while Navigating to Material Facts and Declarations . ");
		customAssert.assertTrue(funcClaimsExperience(common.Rewind_excel_data_map), "Previous claim function is having issue(S) .");
		if(((String)common.Rewind_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Damage"),"Issue while Navigating to Material Damage screen.");
			customAssert.assertTrue(MaterialDamagePage(common.Rewind_excel_data_map), "Material DamagePage function is having issue(S) . ");
		}	
		if(((String)common.Rewind_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
			customAssert.assertTrue(funcBusinessInterruption(common.Rewind_excel_data_map), "Business Interruption function is having issue(S) . ");
		}
		if(((String)common.Rewind_excel_data_map.get("CD_Money&Assault")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Money & Assault"),"Issue while Navigating to Money & Assault screen.");
			customAssert.assertTrue(MoneyAssaultPage(common.Rewind_excel_data_map), "Material DamagePage function is having issue(S) . ");
		}
		if(((String)common.Rewind_excel_data_map.get("CD_EmployersLiability")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Employers Liability"),"Issue while Navigating to Employers Liability screen.");
			customAssert.assertTrue(EmployersLiabilityPage(common.Rewind_excel_data_map), "EmployersLiabilityPage function is having issue(S) . ");
		}
		if(((String)common.Rewind_excel_data_map.get("CD_PublicLiability")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Public Liability"),"Issue while Navigating to Public Liability screen.");
			customAssert.assertTrue(PublicLiabilityPage(common.Rewind_excel_data_map), "Public Liability function is having issue(S) . ");
		}
		if(((String)common.Rewind_excel_data_map.get("CD_GoodsInTransit")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Goods in Transit"),"Issue while Navigating to Goods in Transit screen.");
			customAssert.assertTrue(GoodsinTransitPage(common.Rewind_excel_data_map), "Personal Accident Optional function is having issue(S) . ");
		}
		if(((String)common.Rewind_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){	
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Legal Expenses"),"Issue while Navigating to Legal Expenses screen.");
			customAssert.assertTrue(LegalExpensesPage(common.Rewind_excel_data_map), "Personal Accident Optional function is having issue(S) . ");
		}
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Endorsements"),"Issue while Navigating to Property Owners Liability screen.");
		customAssert.assertTrue(common_HHAZ.funcEndorsementOperations(common.Rewind_excel_data_map),"Insurance tax adjustment is having issue(S).");
		customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		*/
		
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
///SPI Screens Scripting --///

/**
 * 
 * This method handles SPI Policy General screens scripting.
 * 
 */
public boolean funcPolicyGeneral(Map<Object, Object> map_data,String code ,String event){
	
	boolean retvalue = true;
	try{
		customAssert.assertTrue(common.funcPageNavigation("Policy General", ""), "Navigation problem to Policy General page .");
		
		customAssert.assertTrue(k.Input("SPI_PG_DateEstablished", (String)map_data.get("PG_DateEstablishment")),	"Unable to enter value in Date Established field .");
		customAssert.assertTrue(k.DropDownSelection("SPI_PG_IsYourPracticeLLP",(String)common.NB_excel_data_map.get("PG_IsYourPracticeLLP")), "Unable to select value from Is your practice an LLP dropdown .");
		customAssert.assertTrue(k.DropDownSelection("SPI_PG_QuoteValidity",(String)common.NB_excel_data_map.get("PG_QuoteValidity")), "Unable to select value from Quote Validity dropdown .");
		
		driver.findElement(By.xpath("html/body/div[3]/form/div/table[1]/tbody/tr[10]/td[2]/span/span[1]/span/ul/li[1]/span")).click();
		
		String[] geographical_Limits = ((String)common.NB_excel_data_map.get("PG_Geographical_limit")).split(",");
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
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Policy General .");
		TestUtil.reportStatus("Entered all the details on Policy General page .", "Info", true);
		
		return retvalue;
		
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Unable to to do operation on policy General page. \n", t);
        return false;
 }
}

/**
 * 
 * This method handles SPI Policy General screens scripting.
 * 
 */
public boolean funcPolicyGeneral_MTA(Map<Object, Object> map_data,String code ,String event){
	
	boolean retvalue = true;
	try{
		customAssert.assertTrue(common.funcPageNavigation("Policy General", ""), "Navigation problem to Policy General page .");
		
		customAssert.assertTrue(k.Input("SPI_PG_DateEstablished", (String)map_data.get("PG_DateEstablishment")),	"Unable to enter value in Date Established field .");
		customAssert.assertTrue(k.DropDownSelection("SPI_PG_IsYourPracticeLLP",(String)map_data.get("PG_IsYourPracticeLLP")), "Unable to select value from Is your practice an LLP dropdown .");
		customAssert.assertTrue(k.DropDownSelection("SPI_PG_QuoteValidity",(String)map_data.get("PG_QuoteValidity")), "Unable to select value from Quote Validity dropdown .");
		
		k.ImplicitWaitOff();
		for(int i=1;i<=4;i++){
			try{
				driver.findElement(By.xpath("html/body/div[3]/form/div/table[1]/tbody/tr[10]/td[2]/span/span[1]/span/ul/li["+1+"]/span")).click();
			}catch(Throwable t){
				continue;
			}
		}
		k.ImplicitWaitOn();
		
		String[] geographical_Limits = ((String)map_data.get("PG_Geographical_limit")).split(",");
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
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Policy General .");
		TestUtil.reportStatus("Entered all the details on Policy General page .", "Info", true);
		
		return retvalue;
		
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Unable to to do operation on policy General page. \n", t);
        return false;
 }
}

/**
 * 
 * This method handles Claims History screens scripting.
 * 
 */
public boolean funcClaimsHistory(Map<Object, Object> map_data){
	
	boolean r_value= true;
	
	try{
		
		customAssert.assertTrue(common.funcPageNavigation("Claims History", ""),"Claims History page navigations issue(S)");
	
		//Add Claims History Data
		addInput_ClaimsHistoryData("this_year", 0);
		addInput_ClaimsHistoryData("this_year", 1);
		addInput_ClaimsHistoryData("this_year", 2);
		addInput_ClaimsHistoryData("this_year", 3);
		addInput_ClaimsHistoryData("this_year", 4);
		addInput_ClaimsHistoryData("this_year", 5);
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Claims History .");
		TestUtil.reportStatus("Claims History details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean addInput_ClaimsHistoryData(String year_keyword,int year){
	
	boolean r_value = false;
	Map<Object,Object> data_map = new HashMap<>();
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
	}
	
	try{
	
	String _openClaim_xpath = "//*[contains(@id,'open_claims_"+year_keyword+"_"+year+"')]";
	customAssert.assertTrue(k.DynamicXpathWebElement_driver(_openClaim_xpath,(String)data_map.get("CH_open_claims_"+year_keyword+"_"+year),"Input"),"Error while adding "+year_keyword+"_"+year+" Open Claims data . ");
	//CH_open_claims_this_year_0
	String _closedClaim_xpath = "//*[contains(@id,'closed_claims_"+year_keyword+"_"+year+"')]";
	customAssert.assertTrue(k.DynamicXpathWebElement_driver(_closedClaim_xpath,(String)data_map.get("CH_closed_claims_"+year_keyword+"_"+year),"Input"),"Error while adding "+year_keyword+"_"+year+" Closed Claims data . ");
	
	String _reserve_xpath = "//*[contains(@id,'reserve_"+year_keyword+"_"+year+"')]";
	customAssert.assertTrue(k.DynamicXpathWebElement_driver(_reserve_xpath,(String)data_map.get("CH_reserve_"+year_keyword+"_"+year),"Input"),"Error while adding "+year_keyword+"_"+year+" reserve data . ");
	
	String _paid_xpath = "//*[contains(@id,'paid_"+year_keyword+"_"+year+"')]";
	customAssert.assertTrue(k.DynamicXpathWebElement_driver(_paid_xpath,(String)data_map.get("CH_paid_"+year_keyword+"_"+year),"Input"),"Error while adding "+year_keyword+"_"+year+" paid data . ");
		
	}catch(Throwable t){
		System.out.println("Error while adding Claims History Data - "+t);
	}
	
	return r_value;
	
	
}


public boolean funcPremiumSummary(Map<Object, Object> map_data,String code,String event){
	
	boolean r_value= true;
	Date currentDate = new Date();
	String testName = (String)map_data.get("Automation Key");
	String customPolicyDuration=null;
	SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
	
	try{
	customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
	
	switch(common.currentRunningFlow){
	
	case "NB":
		
		
		int policy_Duration = Integer.parseInt((String)map_data.get("PS_Duration"));
		boolean negativeFlag = policy_Duration < 0 ?false:true;
		customAssert.assertTrue(negativeFlag , "Policy Duration should be non-negative value . ");
		boolean Months18_Flag = policy_Duration <= 730 ?true:false;
		customAssert.assertTrue(Months18_Flag , "Policy Duration Should be less than 24 Months ( <= 730 Days) . ");
		policy_Duration--;
		String Policy_End_Date = common.daysIncrementWithOutFormation(df1.format(currentDate), policy_Duration);
		
		String policy_Start_date = common_VELA.get_PolicyStartDate((String)map_data.get("PS_PolicyStartDate"));
		map_data.put("PS_PolicyStartDate", policy_Start_date);
		
		
		if(((String)map_data.get("PS_DefaultStartEndDate")).equals("No")){ //This is for PIA renewal policy creation having 365 days
			
			int policy_expiry_after_days = Integer.parseInt((String)map_data.get("PS_ExpiryAfterDays"));
			policy_Duration = Integer.parseInt((String)map_data.get("PS_Duration"));
			--policy_Duration;
			Policy_End_Date = common.daysIncrementWithOutFormation(df1.format(currentDate), policy_expiry_after_days);
			policy_Start_date = common.daysIncrementWithOutFormation(Policy_End_Date, -(policy_Duration));
			
			map_data.put("PS_PolicyStartDate", policy_Start_date);
			
			customAssert.assertTrue(k.Click("Policy_Start_Date"), "Unable to Click Policy_Start_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_Start_Date", policy_Start_date),"Unable to Enter Policy_Start_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
			customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
		}	
		k.waitTwoSeconds();
		customAssert.assertTrue(k.SelectRadioBtn("SPI_isPolicyFinanced", (String)map_data.get("PS_PaymentWarrantyRules")),"Unable to Select Is the policy financed? radio button . ");
		
		
	break;	
	
	case "Rewind":
		
		
		policy_Duration = Integer.parseInt((String)map_data.get("PS_Duration"));
		negativeFlag = policy_Duration < 0 ?false:true;
		customAssert.assertTrue(negativeFlag , "Policy Duration should be non-negative value . ");
		Months18_Flag = policy_Duration <= 730 ?true:false;
		customAssert.assertTrue(Months18_Flag , "Policy Duration Should be less than 24 Months ( <= 730 Days) . ");
		policy_Duration--;
		Policy_End_Date = common.daysIncrementWithOutFormation(df1.format(currentDate), policy_Duration);
		policy_Start_date = common_VELA.get_PolicyStartDate((String)map_data.get("PS_PolicyStartDate"));
		map_data.put("PS_PolicyStartDate", policy_Start_date);
		
		
		if(((String)map_data.get("PS_DefaultStartEndDate")).equals("No")){ 
			
			int policy_expiry_after_days = Integer.parseInt((String)map_data.get("PS_ExpiryAfterDays"));
			policy_Duration = Integer.parseInt((String)map_data.get("PS_Duration"));
			--policy_Duration;
			Policy_End_Date = common.daysIncrementWithOutFormation(df1.format(currentDate), policy_expiry_after_days);
			policy_Start_date = common.daysIncrementWithOutFormation(Policy_End_Date, -(policy_Duration));
			
			map_data.put("PS_PolicyStartDate", policy_Start_date);
			
			customAssert.assertTrue(k.Click("Policy_Start_Date"), "Unable to Click Policy_Start_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_Start_Date", policy_Start_date),"Unable to Enter Policy_Start_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
			customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
		}	
		k.waitTwoSeconds();
		customAssert.assertTrue(k.SelectRadioBtn("SPI_isPolicyFinanced", (String)map_data.get("PS_PaymentWarrantyRules")),"Unable to Select Is the policy financed? radio button . ");
		
		
	break;	
		
		
	case "Renewal":
		
		customAssert.assertTrue(funcGetDataForRenewal(),"Error while getting data for renewals");
		break;
	}
	
	
	customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
	
	customPolicyDuration = k.getText("Policy_Duration");
	WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,map_data);
	TestUtil.reportStatus("Policy Duration = "+customPolicyDuration+" Days . ", "Info", true);
	
	String total_admin_fee = k.getText("PIA_AdminFee");
	total_admin_fee = total_admin_fee.replaceAll(",", "");
	WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_TotalAdminFee", total_admin_fee,map_data);
	TestUtil.reportStatus("Total Policy Admin Fee = "+total_admin_fee,"Info", true);

	customAssert.assertTrue(Verify_premiumSummaryTable(), "Error while verifying Premium Summary table .");
	
	if(Integer.parseInt(customPolicyDuration)!=365){
		customAssert.assertTrue(funcTransactionPremiumTable(code, event), "Error while verifying Transaction Premium table on premium Summary page .");
	}
	
	TestUtil.reportStatus("Premium Summary details are filled and Verified sucessfully . ", "Info", true);
	
}catch(Throwable t){
	return false;
}

return r_value;
}

public boolean Verify_premiumSummaryTable(){
	err_count = 0;
	final String code = TestBase.product;
	final String event = TestBase.businessEvent;
	String testName = null;
	Map<Object,Object> data_map = null;
	List<String> coverList=new ArrayList<String>();
	
	switch(common.currentRunningFlow){
		case "NB":
			testName = (String)common.NB_excel_data_map.get("Automation Key");
			data_map = common.NB_excel_data_map;
		break;
		case "Renewal":
			testName = (String)common.Renewal_excel_data_map.get("Automation Key");
			data_map = common.Renewal_excel_data_map;
		break;
		case "Rewind":
			testName = (String)common.Rewind_excel_data_map.get("Automation Key");
			data_map = common.Rewind_excel_data_map;
		break;
	}
	
	final Map<String,String> locator_map = new HashMap<>();
	locator_map.put("PenComm", "pencom");
	locator_map.put("NetPremium", "nprem");
	locator_map.put("BrokerComm", "comm");
	locator_map.put("GrossPremium", "gprem");
	locator_map.put("InsuranceTax", "gipt");
	
	double exp_PI_Premium = 0.0,exp_SEC_Premium = 0.0;
	
	try{
	
	if(((String)data_map.get("CD_SolicitorsPI")).equals("Yes")){
		customAssert.assertTrue(funcAddInput_PremiumSummary("PI","SolicitorsPI",data_map),"Add Premium Summary Input function having issues . ");
	}
	
	customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
	
	if(((String)data_map.get("CD_SolicitorsPI")).equals("Yes")){
		err_count = err_count + func_SPI_PremiumSummaryCalculation("PI","Solicitors PI",locator_map);
		exp_PI_Premium = Double.parseDouble((String)data_map.get("PS_PI_TotalPremium"));
		coverList.add("PI");
	}
	if(((String)data_map.get("CD_SolicitorsExcessLayer")).equals("Yes")){
		err_count = err_count + func_SPI_PremiumSummaryCalculation("SEL","Solicitors excess layer",locator_map);
		exp_SEC_Premium = Double.parseDouble((String)data_map.get("PS_SEL_TotalPremium"));
		coverList.add("SEL");
	}
	
	String exp_Total_Premium = common.roundedOff(Double.toString(exp_PI_Premium + exp_SEC_Premium));
	String act_Total_Premium = k.getAttribute("SPI_Total_Premium", "value");
	act_Total_Premium = act_Total_Premium.replaceAll(",", "");
	
	double premium_diff = Double.parseDouble(common.roundedOff(Double.toString(Double.parseDouble(exp_Total_Premium) - Double.parseDouble(act_Total_Premium))));
	
	TestUtil.reportStatus("---------------Total Premium-----------------","Info",false);
	
	if(Math.abs(premium_diff)<=0.06){
		TestUtil.reportStatus("Total Premium :[<b> "+exp_Total_Premium+" </b>] matches with actual premium [<b> "+act_Total_Premium+"</b>]as expected with some difference upto '0.05' on premium summary page.", "Pass", false);
		WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_TotalPremium", exp_Total_Premium,data_map);
	}else{
		TestUtil.reportStatus("Mismatch in Expected Premium [<b> "+exp_Total_Premium+"</b>] and Actual Premium [<b> "+act_Total_Premium+"</b>] on premium summary page.", "Fail", false);
		WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_TotalPremium", exp_Total_Premium,data_map);
	}
		
	//customAssert.assertTrue(func_SPI_Write_Total_PremiumSummary_Values(),"Error while writing Total Premium Summary values to excel . ");
	customAssert.assertTrue(func_SPI_Write_Total_PremiumSummary_Values_Calculated(coverList),"Error while writing Total Premium Summary values to excel . ");
	
	if(Integer.parseInt((String)data_map.get("PS_Duration")) == 365){
	//Toal Premium With Admin Fees
	double total_premium_with_admin_fee = Double.parseDouble((String)data_map.get("PS_GrossPremiumTotal")) + 
			Double.parseDouble((String)data_map.get("PS_InsuranceTaxTotal")) +
			Double.parseDouble((String)data_map.get("PS_TotalAdminFee"));
	
	String exp_Total_Premium_with_Admin_fee = common.roundedOff(Double.toString(total_premium_with_admin_fee));
	k.waitTwoSeconds();
	
	String xPath = "//table[@id='table0']//*//td[text()='Total']//following-sibling::td";
	String act_Total_Premium_with_Admin_fee = k.getTextByXpath(xPath);
	
	act_Total_Premium_with_Admin_fee = act_Total_Premium_with_Admin_fee.replaceAll(",", "");
	premium_diff = Double.parseDouble(common.roundedOff(Double.toString(Double.parseDouble(exp_Total_Premium_with_Admin_fee) - Double.parseDouble(act_Total_Premium_with_Admin_fee))));
	
	TestUtil.reportStatus("---------------Total Premium with Admin Fees-----------------","Info",false);
	
	if(Math.abs(premium_diff)<=0.06){
		TestUtil.reportStatus("Total Premium with Admin Fees :[<b> "+exp_Total_Premium_with_Admin_fee+" </b>] matches with actual premium [<b> "+act_Total_Premium_with_Admin_fee+"</b>]as expected with some difference upto '0.05' on premium summary page.", "Pass", false);
		WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_TotalFinalPremium", exp_Total_Premium_with_Admin_fee,data_map);
	}else{
		TestUtil.reportStatus("Mismatch in Expected Total Premium with Admin Fees [<b> "+exp_Total_Premium_with_Admin_fee+"</b>] and Actual Premium [<b> "+act_Total_Premium_with_Admin_fee+"</b>] on premium summary page.", "Fail", false);
		WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_TotalFinalPremium", exp_Total_Premium_with_Admin_fee,data_map);
	}
	}
	
	}catch(Throwable t){
		
		return false;
		
	}
	
	return true;
}




public boolean funcAddInput_PremiumSummary(String code, String cover_name,Map<Object,Object> data_map) {
	boolean retvalue=true;
	
	
	
	try{
		//SPI_SolicitorsPI_PenComm
		customAssert.assertTrue(k.Input("SPI_"+cover_name+"_PenComm", (String)data_map.get("PS_"+code+"_PenComm_rate")), "Unable to enter value of Pen Commission for "+cover_name+".");
		customAssert.assertTrue(k.Input("SPI_"+cover_name+"_BrokerComm", (String)data_map.get("PS_"+code+"_BrokerComm_rate")), "Unable to enter value of Broker Commission for "+cover_name+".");
		
		return retvalue;
		
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Premium Summary Add Input function is having issue(S). \n", t);
        return false;
 }
}
//Reusable for both NB and MTA
public int func_SPI_PremiumSummaryCalculation(String code,String cover_name,Map<String,String> premium_loc) {
	
	
	Map<Object,Object> map_data = null;
	
	String event=null;
	
	
	switch(TestBase.businessEvent){
		case "NB":
			map_data = common.NB_excel_data_map;
		break;
		case "MTA":
			if(common_PIA.currentFlow.equals("NB")){
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
			if(common_PIA.currentFlow.equals("NB")){
				map_data = common.NB_excel_data_map;
				event = "NB";
				}
			else{
				map_data = common.CAN_excel_data_map;
				event = "CAN";				
			}
			
		break;	
		case "Rewind":
			if(common.currentRunningFlow.equals("NB")){
				map_data = common.NB_excel_data_map;
				event = "NB";
				}
			else{
				map_data = common.Rewind_excel_data_map;
				event = "Rewind";
				
			}
			break;
		
	}
		String testName = (String)map_data.get("Automation Key");
		double NetNet_Premium = Double.parseDouble((String)map_data.get("PS_"+code+"_NetNetPremium"));
		
		String val_cover = null;
		switch(code){
		case "PI":
			val_cover = "pi";
			break;
		case "SEL":
			val_cover = "el";
			break;
		default:
				System.out.println("**Cover Name is not in Scope for SPI**");
			break;
		
		}
		
		//Commented as per discussion - PIA Solicitors Excess Layer cover defects are not in scope
		/*if(code.equals("SEL") && common.currentRunningFlow.equals("MTA") && ((String)map_data.get("Automation Key")).contains("MTA_02")){
			
			try{
			String SEL_penComm_type = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("PenComm")+"')]", "type");
			if(SEL_penComm_type.equals("text")){
			
				TestUtil.reportStatus("Solicitors Excess Layers's Pen/Broker commission should not be editable on premium Summary page . ", "Fail", true);
				ErrorUtil.addVerificationFailure(new Throwable("Solicitors Excess Layers's Pen/Broker commission editability issue on premium Summary page"));
			}
			}catch(Throwable t){
				String isSEL_penComm_readOnly = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("PenComm")+"')]", "readonly");
				if(!isSEL_penComm_readOnly.equals("true")){
					TestUtil.reportStatus("Solicitors Excess Layers's Pen/Broker commission should not be editable on premium Summary page . ", "Fail", true);
					ErrorUtil.addVerificationFailure(new Throwable("Solicitors Excess Layers's Pen/Broker commission editability issue on premium Summary page"));
				}
			}
			
		}*/
		
	try{
			
			TestUtil.reportStatus("---------------"+cover_name+"-----------------","Info",false);
			//SPI Pen commission Calculation : 
			double pen_comm = ((NetNet_Premium / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate"))/100)));
			String pc_expected = common.roundedOff(Double.toString(pen_comm));
			String pc_actual = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("PenComm")+"')]", "value");
			//String pc_actual = driver.findElement(By.xpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("PenComm")+"')]"))
			CommonFunction.compareValues(Double.parseDouble(pc_expected),Double.parseDouble(pc_actual),"Pen Commission");
			WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow,"Premium Summary",testName,"PS_"+code+"_PenComm",pc_expected,map_data);
			
			
			//SPI Net Premium verification : 
			double netP = Double.parseDouble(pc_expected) + NetNet_Premium;
			String netP_expected = common.roundedOff(Double.toString(netP));
			String netP_actual = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("NetPremium")+"')]", "value");
			CommonFunction.compareValues(Double.parseDouble(netP_expected),Double.parseDouble(netP_actual),"Net Premium");
			WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_NetPremium",netP_expected,map_data);
			
			
			//SPI Broker commission Calculation : 
			double broker_comm = ((NetNet_Premium / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate"))/100)));
			String bc_expected = common.roundedOff(Double.toString(broker_comm));
			String bc_actual =  k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("BrokerComm")+"')]", "value");
			CommonFunction.compareValues(Double.parseDouble(bc_expected),Double.parseDouble(bc_actual),"Broker Commission");
			WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_BrokerComm",bc_expected,map_data);
			
			
			//SPI GrossPremium verification : 
			double grossP = Double.parseDouble(netP_expected) + Double.parseDouble(bc_expected);
			String grossP_actual = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("GrossPremium")+"')]", "value");
			if(code.equals("PI"))
				common_PIA.PI_pdf_GrossPremium = Double.parseDouble(grossP_actual);
			else
				common_PIA.SEL_pdf_GrossPremium = Double.parseDouble(grossP_actual);
			CommonFunction.compareValues(grossP,Double.parseDouble(grossP_actual),"Gross Premium");
			WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_GrossPremium",Double.toString(grossP),map_data);
			
			String insurance_Tax_Rate = null;
			double InsuranceTax = 0.0;
			
			if(!common.currentRunningFlow.equalsIgnoreCase("MTA")){
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
			Date policy_Start_date = sdf.parse((String)map_data.get(("PS_PolicyStartDate"))); 
			Date tax_rate_change_date = sdf.parse("01/06/2017");
	
			
				if(policy_Start_date.before(tax_rate_change_date)){
					insurance_Tax_Rate = "10";
				}else{
					insurance_Tax_Rate = (String)map_data.get("PS_"+code+"_InsuranceTaxRate");
				}
			
				InsuranceTax = (grossP * Double.parseDouble(insurance_Tax_Rate))/100.0;
			
			}else{
				InsuranceTax = (grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_InsuranceTaxRate")))/100.0;
			}
			
			
			
			//double InsuranceTax = (grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_InsuranceTaxRate")))/100.0;
			InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(InsuranceTax)));
			String InsuranceTax_actual = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("InsuranceTax")+"')]", "value");
			if(code.equals("PI"))
				common_PIA.PI_pdf_InsuranceTax = Double.parseDouble(InsuranceTax_actual);
			else
				common_PIA.SEL_pdf_InsuranceTax = Double.parseDouble(InsuranceTax_actual);
			CommonFunction.compareValues(InsuranceTax,Double.parseDouble(InsuranceTax_actual),"Insurance Tax");
			WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_InsuranceTax",Double.toString(InsuranceTax),map_data);
			
			//SPI Total Premium verification : 
			double Premium = grossP + InsuranceTax;
			String p_expected = common.roundedOff(Double.toString(Premium));
			
			String p_actual = common.roundedOff(k.getAttribute("SPI_"+code+"_Premium", "value"));
			
			double premium_diff = Double.parseDouble(common.roundedOff(Double.toString(Double.parseDouble(p_expected) - Double.parseDouble(p_actual))));
			
			if(premium_diff<=0.06 && premium_diff>=-0.06){
				TestUtil.reportStatus("Total Premium [<b> "+p_expected+" </b>] matches with actual total premium [<b> "+p_actual+" </b>]as expected for "+cover_name+" on premium summary page.", "Pass", false);
				WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_expected,map_data);
				return 0;
				
			}else{
				TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+p_expected+"</b>] and Actual Premium [<b> "+p_actual+"</b>] for "+cover_name+" on premium summary page. </p>", "Fail", true);
				WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_expected,map_data);
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
			if(common_PIA.currentFlow.equals("NB")){
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
			if(common_PIA.currentFlow.equals("NB")){
				data_map = common.NB_excel_data_map;				
				}
			else{
				data_map = common.CAN_excel_data_map;
			}
		break;
		
	}
	
	
	
	String testName = (String)data_map.get("Automation Key");
	
	try{
		
	val_total = k.getText("SPI_AdminFee");
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
	customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName,"PS_GrossPremiumTotal",val_total,data_map),"Error while writing Total Gross Premium to excel .");
	
	val_total = k.getAttribute("SPI_Total_InsuranceTax", "value");
	val_total = val_total.replaceAll(",","");
	customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName,"PS_InsuranceTaxTotal",val_total,data_map),"Error while writing Total IPT to excel .");
	
	return r_value;
	
	}catch(Throwable t){
		
		return false;
		
	}
	
	
}

public boolean func_SPI_Write_Total_PremiumSummary_Values_Calculated(List<String> coverList){
	
	boolean r_value=true;
	String val_total = null,event=null;
	Map<Object,Object> data_map = null;
	
	switch(TestBase.businessEvent){
		case "NB":
			data_map = common.NB_excel_data_map;
		break;
		case "MTA":
			if(common_PIA.currentFlow.equals("NB")){
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
			if(common_PIA.currentFlow.equals("NB")){
				data_map = common.NB_excel_data_map;				
				}
			else{
				data_map = common.CAN_excel_data_map;
			}
		break;
		
		case "Rewind":
			if(common.currentRunningFlow.equals("NB")){
				data_map = common.NB_excel_data_map;
				
			}else{
				data_map = common.Rewind_excel_data_map;
			}
		break;
		
	}
	
		
	String testName = (String)data_map.get("Automation Key");
	
	try{
		customAssert.assertTrue(calculate_Total(coverList,"NetNetPremium",data_map,testName,"NetNetPemiumTotal"),"Error While writing NetNetPemiumTotal to data sheet.");
		customAssert.assertTrue(calculate_Total(coverList,"PenComm",data_map,testName,"PenCommTotal"),"Error While writing NetNetPemiumTotal to data sheet.");
		customAssert.assertTrue(calculate_Total(coverList,"NetPremium",data_map,testName,"NetPremiumTotal"),"Error While writing NetNetPemiumTotal to data sheet.");
		customAssert.assertTrue(calculate_Total(coverList,"BrokerComm",data_map,testName,"BrokerCommissionTotal"),"Error While writing BrokerCommissionTotal to data sheet.");
		customAssert.assertTrue(calculate_Total(coverList,"GrossPremium",data_map,testName,"GrossPremiumTotal"),"Error While writing GrossPremiumTotal to data sheet.");
		customAssert.assertTrue(calculate_Total(coverList,"InsuranceTax",data_map,testName,"InsuranceTaxTotal"),"Error While writing Insurance to data sheet.");
		customAssert.assertTrue(calculate_Total(coverList,"TotalPremium",data_map,testName,"TotalPremium"),"Error While writing Total to data sheet.");
		
		
	
	return r_value;
	
	}catch(Throwable t){
		
		return false;
		
	}
	
	
}

public boolean calculate_Total(List<String> coverList,String code,Map<Object,Object> data_map,String testName,String total_Key){
	boolean r_value=true;
	double _val_total=0.0;String val_total=null;
	try{
		for(String cover:coverList){
			_val_total = _val_total + Double.parseDouble((String)data_map.get("PS_"+cover+"_"+code));
	
		}
		val_total=Double.toString(_val_total);
		customAssert.assertTrue(TestUtil.WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName,"PS_"+total_Key,val_total,data_map),"Error while writing Total NN Premium to excel .");
	}catch(Throwable t){
		r_value=false;
	}

	return r_value;

}

//-----SPI Specific Functions ----////

//-----SPI Rater Calculation Functions ----////
/**
 * This method Calculates Rater sheet's B7 Value
 */
public int calculate_ActualNo_OfYears(){
	
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
		case "Rewind":
			data_map = common.Rewind_excel_data_map;
			break;
	
	}
	
	String policyStartDate = null;
	try{
		if(common.currentRunningFlow.equalsIgnoreCase("NB") || common.currentRunningFlow.equalsIgnoreCase("Renewal") || common.currentRunningFlow.equalsIgnoreCase("Rewind"))
			policyStartDate = k.getAttribute("Policy_Start_Date", "value");
		else
			policyStartDate = driver.findElement(By.xpath("//*[contains(@id,'start_date') and @class='read']")).getText();
		//String policyStartDate = k.getAttribute("Policy_Start_Date", "value");
		//String policyStartDate = "27/05/2017";
		int month_of_policyStartDate = Integer.parseInt(policyStartDate.split("/")[1]);
		int year_of_PolicyStartDate = Integer.parseInt(policyStartDate.split("/")[2]); 
		if(month_of_policyStartDate < 10){
			actual_no_of_years = year_of_PolicyStartDate-1-Integer.parseInt((String)data_map.get("PG_DateEstablishment"));
		}else{
			actual_no_of_years = year_of_PolicyStartDate-Integer.parseInt((String)data_map.get("PG_DateEstablishment"));
		}
	}catch(Throwable t){
		System.out.println("Error while Calculating Actual No Of Years - "+t);
		return 0;
	}
	return actual_no_of_years;
	
}
/**
 * This method Calculates Rater sheet's B8 Value
 */
public int calculate_No_OfYears(){
	
	PIA_Rater = OR.getORProperties();
	actual_no_of_years=calculate_ActualNo_OfYears();
	
	int no_of_years=0;
	try{
		if(actual_no_of_years < 1){
			no_of_years = 1;
		}else{
			if(actual_no_of_years > Integer.parseInt(PIA_Rater.getProperty("PIA_No_years_claims_to_look_at"))){
				no_of_years = Integer.parseInt(PIA_Rater.getProperty("PIA_No_years_claims_to_look_at"));
			}else{
				no_of_years = actual_no_of_years;
			}
		}
	}catch(Throwable t){
		System.out.println("Error while Calculating No Of Years - "+t);
	}
	return no_of_years;
	
}

/**
 * This method Calculates Premium_Rates sheet's A14(Base Rate) Value
 */
public double calculate_BaseRate(){
	
	double baseRate = 0.0;
	
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
		case "Rewind":
			data_map = common.Rewind_excel_data_map;
			break;
	
	}
	PIA_Rater = OR.getORProperties();
	
	try{
		if(((String)data_map.get("PG_IsYourPracticeLLP")).equals("Yes")){
			baseRate = Double.parseDouble(PIA_Rater.getProperty("Base_Rate_LLP"));
		}else{
			baseRate = Double.parseDouble(PIA_Rater.getProperty("Base_Rate_Non_LLP"));
		}
	}catch(Throwable t){
		System.out.println("Error while Calculating No Of Years - "+t);
	}
	return baseRate;
	
}

/**
 * This method Calculates Premium_Rates sheet's B14(RDI Rate) Value
 */
public double calculate_RDIRate(){
	
	double rdiRate = 0.0;
	df = new SimpleDateFormat("dd/MM/yyyy");
	PIA_Rater = OR.getORProperties();
	Date riskStartDate = null;
	
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
		case "Rewind":
			data_map = common.Rewind_excel_data_map;
			break;
	
	}
	//Policy_End_Date.compareTo(todaysDate)>0
	try{
		if(common.currentRunningFlow.equalsIgnoreCase("NB") || common.currentRunningFlow.equalsIgnoreCase("Rewind"))
			riskStartDate = (Date)df.parse(k.getAttribute("Policy_Start_Date", "value"));
		else if(common.currentRunningFlow.equalsIgnoreCase("MTA"))
			riskStartDate = (Date)df.parse(driver.findElement(By.xpath("//*[contains(@id,'start_date') and @class='read']")).getText());
		else if(common.currentRunningFlow.equalsIgnoreCase("Renewal"))
			riskStartDate = (Date)df.parse(k.getAttribute("Policy_Start_Date", "value"));
		//riskStartDate = (Date)df.parse("27/05/2017");
		
		//Right Side lookup
		Date r_first_Date = (Date)df.parse("01/10/2016");
		Date r_last_Date = (Date)df.parse("30/09/2017");
		
		if((riskStartDate.compareTo(r_first_Date) > 0 ) && (riskStartDate.compareTo(r_last_Date) < 0 )){
			
			switch(Integer.parseInt((String)data_map.get("PG_DateEstablishment"))){
			
				case 2014:
					rdiRate = Double.parseDouble(PIA_Rater.getProperty("RDI_Risk_Start_01_10_2016_to_30_09_2017_DOE_2014"));
					return rdiRate;
				case 2015:
					rdiRate = Double.parseDouble(PIA_Rater.getProperty("RDI_Risk_Start_01_10_2016_to_30_09_2017_DOE_2015"));
					return rdiRate;
				case 2016:
					rdiRate = Double.parseDouble(PIA_Rater.getProperty("RDI_Risk_Start_01_10_2016_to_30_09_2017_DOE_2016"));
					return rdiRate;
				case 2017:
					rdiRate = Double.parseDouble(PIA_Rater.getProperty("RDI_Risk_Start_01_10_2016_to_30_09_2017_DOE_2017"));
					return rdiRate;
				default:
					
					return 1;
			
			}
		}
		
			//Left Side lookup
			Date l_first_Date = (Date)df.parse("01/10/2017");
			Date l_last_Date = (Date)df.parse("30/09/2018");
				
			if((riskStartDate.compareTo(l_first_Date) > 0 ) && (riskStartDate.compareTo(l_last_Date) < 0 )){
					
				switch(Integer.parseInt((String)data_map.get("PG_DateEstablishment"))){
					
					case 2014:
						rdiRate = Double.parseDouble(PIA_Rater.getProperty("RDI_Risk_Start_01_10_2017_to_30_09_2018_DOE_2014"));
						return rdiRate;
					case 2015:
						rdiRate = Double.parseDouble(PIA_Rater.getProperty("RDI_Risk_Start_01_10_2017_to_30_09_2018_DOE_2015"));
						return rdiRate;
					case 2016:
						rdiRate = Double.parseDouble(PIA_Rater.getProperty("RDI_Risk_Start_01_10_2017_to_30_09_2018_DOE_2016"));
						return rdiRate;
					case 2017:
						rdiRate = Double.parseDouble(PIA_Rater.getProperty("RDI_Risk_Start_01_10_2017_to_30_09_2018_DOE_2017"));
						return rdiRate;
					default:
						return 1;
					
				}
			}
			
			//CR 274- SPI2 change
			//Left Side second lookup
			Date l2_first_Date = (Date)df.parse("01/10/2018");
			Date l2_last_Date = (Date)df.parse("30/09/2019");
				
			if((riskStartDate.compareTo(l2_first_Date) > 0 ) && (riskStartDate.compareTo(l2_last_Date) < 0 )){
					
				switch(Integer.parseInt((String)data_map.get("PG_DateEstablishment"))){
					
					case 2016:
						rdiRate = Double.parseDouble(PIA_Rater.getProperty("PIA_RDI_Risk_Start_01_10_2018_to_30_09_2019_DOE_2016"));
						return rdiRate;
					case 2017:
						rdiRate = Double.parseDouble(PIA_Rater.getProperty("PIA_RDI_Risk_Start_01_10_2018_to_30_09_2019_DOE_2017"));
						return rdiRate;
					case 2018:
						rdiRate = Double.parseDouble(PIA_Rater.getProperty("PIA_RDI_Risk_Start_01_10_2018_to_30_09_2019_DOE_2018"));
						return rdiRate;
					default:
						return 1;
					
				}
			}
		
	}catch(ParseException parse_e){	
		System.out.println("Error while Parsing Dates in calculating RDI rate -  "+parse_e);
	}catch(Throwable t){
		System.out.println("Error while Calculating RDI rate - "+t);
	}
	return rdiRate;
	
}

/**
 * This method Calculates Premium_Rates sheet's C14(Size Rate) Value
 */
public double calculate_SizeRate(){
	
	double sizeRate = 0.0;
	
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
		case "Rewind":
			data_map = common.Rewind_excel_data_map;
			break;
	
	}
	PIA_Rater = OR.getORProperties();
	
	try{
		//int MULTIPLIER = Integer.parseInt(PIA_Rater.getProperty("MULTIPLIER"));
		//CR 274
		double MULTIPLIER = Double.parseDouble(PIA_Rater.getProperty("PIA_MULTIPLIER"));
		
		double POWER = Double.parseDouble(PIA_Rater.getProperty("PIA_POWER"));
		spi_GrossFee = Double.parseDouble((String)data_map.get("SP_GrossFee"));
		
		if((MULTIPLIER*Math.pow(spi_GrossFee, POWER)) > 1){
			sizeRate=1;
		}else{			sizeRate = MULTIPLIER*Math.pow(spi_GrossFee, POWER);
		}
		
		
	}catch(Throwable t){
		System.out.println("Error while Calculating Size Rate - "+t);
	}
	return sizeRate;
	
}

/**
 * This method Calculates Premium_Rates sheet's AS3(Ops Rate) Value
 */
public double calculate_OPsRate(){
	
	double final_OPsRate = 0.0;
	PIA_Rater = OR.getORProperties();
	int count_AOP = common.no_of_inner_data_sets.get("Area of Practice");
	String areaOfPractice=null;
	int AOP_Cent_Value = 0;
	int total_AOP_Cent_Value = 0;
	double AOP_Rate = 0.0;
	double AOP_final_value=0.0;
	String[] aop_ =null;
	
Map<Object,Object> data_map = null;
Map<String, List<Map<String, String>>> data_Structure_of_InnerPagesMaps = null;
	
	switch(common.currentRunningFlow){
	
		case "NB":
			data_map = common.NB_excel_data_map;
			data_Structure_of_InnerPagesMaps = common.NB_Structure_of_InnerPagesMaps;
			break;
		case "MTA":
			data_map = common.MTA_excel_data_map;
			data_Structure_of_InnerPagesMaps = common.MTA_Structure_of_InnerPagesMaps;
			aop_ = ((String)data_map.get("SP_AreaofPractice")).split(";");
			count_AOP = aop_.length;
			break;
		case "Renewal":
			data_map = common.Renewal_excel_data_map;
			data_Structure_of_InnerPagesMaps = common.Renewal_Structure_of_InnerPagesMaps;
			aop_ = ((String)data_map.get("SP_AreaofPractice")).split(";");
			count_AOP = aop_.length;
			break;
		case "Rewind":
			data_map = common.Rewind_excel_data_map;
			data_Structure_of_InnerPagesMaps = common.Rewind_Structure_of_InnerPagesMaps;
			aop_ = ((String)data_map.get("SP_AreaofPractice")).split(";");
			count_AOP = aop_.length;
			break;
	
	}
	
	try{
		
		String WorkSplit = (String)data_map.get("SP_WorkSplittoUse");
		
		switch(WorkSplit){
		
		case "Last Year":
			
			for(int count=0;count<count_AOP;count++){
				
				areaOfPractice = data_Structure_of_InnerPagesMaps.get("Area of Practice").get(count).get("PIA_AOP_AreaOfPracticeValue");
				AOP_Cent_Value = Integer.parseInt(data_Structure_of_InnerPagesMaps.get("Area of Practice").get(count).get("PIA_AOP_LastYear"));
				total_AOP_Cent_Value = total_AOP_Cent_Value + AOP_Cent_Value;
				AOP_Rate = get_AreaOfPractice_Rate(areaOfPractice);
				AOP_final_value = AOP_Cent_Value*AOP_Rate;
				if(total_AOP_Cent_Value > 100){
					final_OPsRate = 0;
					System.out.println("** Total Area Of Practice 'Last Year %' is greater than 100 **");
				}else{
					final_OPsRate = final_OPsRate + AOP_final_value;
				}
			}
			
			if(total_AOP_Cent_Value < 100){
				final_OPsRate = 0;
				System.out.println("** Total Area Of Practice 'Last Year %' is less than 100 **");
			}
			
			break;
			
		case "Prior Year":
			
			for(int count=0;count<count_AOP;count++){
				
				areaOfPractice = data_Structure_of_InnerPagesMaps.get("Area of Practice").get(count).get("PIA_AOP_AreaOfPracticeValue");
				AOP_Cent_Value = Integer.parseInt(data_Structure_of_InnerPagesMaps.get("Area of Practice").get(count).get("PIA_AOP_PriorYear"));
				total_AOP_Cent_Value = total_AOP_Cent_Value + AOP_Cent_Value;
				AOP_Rate = get_AreaOfPractice_Rate(areaOfPractice);
				AOP_final_value = AOP_Cent_Value*AOP_Rate;
				if(total_AOP_Cent_Value > 100){
					final_OPsRate = 0;
					System.out.println("** Total Area Of Practice 'Prior Year %' is greater than 100 **");
				}else{
					final_OPsRate = final_OPsRate + AOP_final_value;
				}
			}
			if(total_AOP_Cent_Value < 100){
				final_OPsRate = 0;
				System.out.println("** Total Area Of Practice 'Prior Year %' is less than 100 **");
			}
			
			break;
			
		case "Prior Year 2":
			
			for(int count=0;count<count_AOP;count++){
				
				areaOfPractice = data_Structure_of_InnerPagesMaps.get("Area of Practice").get(count).get("PIA_AOP_AreaOfPracticeValue");
				AOP_Cent_Value = Integer.parseInt(data_Structure_of_InnerPagesMaps.get("Area of Practice").get(count).get("PIA_AOP_PriorYear2"));
				total_AOP_Cent_Value = total_AOP_Cent_Value + AOP_Cent_Value;
				AOP_Rate = get_AreaOfPractice_Rate(areaOfPractice);
				AOP_final_value = AOP_Cent_Value*AOP_Rate;
				if(total_AOP_Cent_Value > 100){
					final_OPsRate = 0;
					System.out.println("** Total Area Of Practice 'Prior Year %' is greater than 100 **");
				}else{
					final_OPsRate = final_OPsRate + AOP_final_value;
				}
			}
			if(total_AOP_Cent_Value < 100){
				final_OPsRate = 0;
				System.out.println("** Total Area Of Practice 'Prior Year %' is less than 100 **");
			}
			
			break;
			
			
		}
			
	}catch(Throwable t){
		System.out.println("Error while Calculating OPs Rate - "+t);
	}
	final_OPsRate = final_OPsRate/100;
	return final_OPsRate;
	
}


/**
 * This method reads Area Of Practices property values
 */
public double get_AreaOfPractice_Rate(String area_Of_Practice){
	
	double OPsRate = 0.0;
	PIA_Rater = OR.getORProperties();
	
	try{
		
		switch(area_Of_Practice){
		
			case "Acting as an Arbitrator, Adjudicator and Mediator":
				return Double.parseDouble(PIA_Rater.getProperty("Acting_as_an_Arbitrator_Adjudicator_and_Mediator"));
			case "Administering oaths, taking affidavits and Notary Public":
				return Double.parseDouble(PIA_Rater.getProperty("Administering_oaths_taking_affidavits_and_Notary_Public"));
			case "Agency Advocacy":
				return Double.parseDouble(PIA_Rater.getProperty("Agency_Advocacy"));
			case "Children, Mental Health Tribunal and Welfare":
				return Double.parseDouble(PIA_Rater.getProperty("Children_Mental_Health_Tribunal_and_Welfare"));
			case "Conveyancing - Commercial":
				return Double.parseDouble(PIA_Rater.getProperty("Conveyancing_Commercial"));
			case "Conveyancing - Residential":
				return Double.parseDouble(PIA_Rater.getProperty("PIA_Conveyancing_Residential"));
			case "Corporate/Commercial work, including public companies":
				return Double.parseDouble(PIA_Rater.getProperty("Corporate_Commercial_work_including_public_companies"));
			case "Corporate/Commercial, (excluding work related to public companies)":
				return Double.parseDouble(PIA_Rater.getProperty("Corporate_Commercial_excluding_work_related_to_public_companies"));
			case "Criminal Law ":
				return Double.parseDouble(PIA_Rater.getProperty("Criminal_Law"));
			case "Debt Collection":
				return Double.parseDouble(PIA_Rater.getProperty("Debt_Collection"));
			case "Defendant litigious work for Insurers":
				return Double.parseDouble(PIA_Rater.getProperty("Defendant_litigious_work_for_Insurers"));
			case "EC Competition Law and Human Rights Law":
				return Double.parseDouble(PIA_Rater.getProperty("EC_Competition_Law_and_Human_Rights_Law"));
			case "Employment":
				return Double.parseDouble(PIA_Rater.getProperty("Employment"));
			case "Financial Advice and Services regulated by the Solicitors Regulation Authority":
				return Double.parseDouble(PIA_Rater.getProperty("Financial_Advice_and_Services_regulated_by_the_Solicitors_Regulation_Authority"));
			case "Financial Advice and Services where you opted into regulation by the FCA/ FSA":
				return Double.parseDouble(PIA_Rater.getProperty("Financial_Advice_and_Services_where_you_opted_into_regulation_by_the_FCA_FSA"));
			case "Immigration":
				return Double.parseDouble(PIA_Rater.getProperty("Immigration"));
			case "Intellectual Property including Patent, Trademark and Copyright":
				return Double.parseDouble(PIA_Rater.getProperty("Intellectual_Property_including_Patent_Trademark_and_Copyright"));
			case "Landlord and Tenant":
				return Double.parseDouble(PIA_Rater.getProperty("Landlord_and_Tenant"));
			case "Lecturing and related activities and expert witness work":
				return Double.parseDouble(PIA_Rater.getProperty("Lecturing_and_related_activities_and_expert_witness_work"));
			case "Litigation (Commercial) ":
				return Double.parseDouble(PIA_Rater.getProperty("Litigation_Commercial"));
			case "Litigious work other than included in any other category.":
				return Double.parseDouble(PIA_Rater.getProperty("Litigious_work_other_than_included_in_any_other_category"));
			case "Marine Litigation":
				return Double.parseDouble(PIA_Rater.getProperty("Marine_Litigation"));
			case "Matrimonial / Family ":
				return Double.parseDouble(PIA_Rater.getProperty("Matrimonial_Family"));
			case "Mergers & Acquisitions including Management":
				return Double.parseDouble(PIA_Rater.getProperty("Mergers_&_Acquisitions_including_Management"));
			case "Non-Litigious work other than included in any other category.":
				return Double.parseDouble(PIA_Rater.getProperty("PIA_NonLitigious_work_other_than_included_in_any_other_category"));
			case "Offices and Appointments ":
				return Double.parseDouble(PIA_Rater.getProperty("Offices_and_Appointments"));
			case "Other Low Risk Work":
				return Double.parseDouble(PIA_Rater.getProperty("Other_Low_Risk_Work"));
			case "Parliamentary Agency ":
				return Double.parseDouble(PIA_Rater.getProperty("Parliamentary_Agency"));
			case "Pension Trustee":
				return Double.parseDouble(PIA_Rater.getProperty("Pension_Trustee"));
			case "Personal Injury (Claimant) - Fast Track ":
				return Double.parseDouble(PIA_Rater.getProperty("Personal_Injury_Claimant_Fast_Track"));
			case "Personal Injury (Claimant) - Other":
				return Double.parseDouble(PIA_Rater.getProperty("Personal_Injury_Claimant_Other"));
			case "Personal Injury (Defendant)":
				return Double.parseDouble(PIA_Rater.getProperty("Personal_Injury_Defendant"));
			case "Probate and Estate Administration":
				return Double.parseDouble(PIA_Rater.getProperty("Probate_and_Estate_Administration"));
			case "Property Selling / Valuations and Property Management":
				return Double.parseDouble(PIA_Rater.getProperty("Property_Selling_Valuations_and_Property_Management"));
			case "Tax Planning/Mitigation":
				return Double.parseDouble(PIA_Rater.getProperty("Tax_PlanningMitigation"));
			case "Town & Country Planning":
				return Double.parseDouble(PIA_Rater.getProperty("Town_&_Country_Planning"));
			case "Trusts":
				return Double.parseDouble(PIA_Rater.getProperty("Trusts"));
			case "Wills":
				return Double.parseDouble(PIA_Rater.getProperty("Wills"));
				
			default:
				break;
		
		
		
		}
		
		
		
	}catch(Throwable t){
		System.out.println("Error while Calculating OPs Rate - "+t);
	}
	return OPsRate;
	
}

/**
 * This method Calculates Premium_Rates sheet's A14/B57(Notional Rate) Value
 */
public double calculate_NotionalRate(){
	
	double notionalRate = 0.0;
	PIA_Rater = OR.getORProperties();
	
	try{
		double base_rate = calculate_BaseRate();
		double RDI_rate = calculate_RDIRate();
		double size_rate = calculate_SizeRate();
		double OPs_rate = calculate_OPsRate();
		notionalRate = base_rate*RDI_rate*size_rate*OPs_rate;
	
	}catch(Throwable t){
		System.out.println("Error while Calculating Notional Rate - "+t);
	}
	return notionalRate;
	
}

/**
 * This method Calculates Premium_Rates sheet's B14/B58(Notional Premium) Value
 */
public double calculate_NotionalPremium(){
	
	double notionalPremium = 0.0;
	
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
		case "Rewind":
			data_map = common.Rewind_excel_data_map;
			break;
	
	}
	
	PIA_Rater = OR.getORProperties();
	spi_GrossFee = Double.parseDouble((String)data_map.get("SP_GrossFee"));
	try{
		notionalPremium = calculate_NotionalRate()*spi_GrossFee;
	//Two Decimal
	}catch(Throwable t){
		System.out.println("Error while Calculating Notional Rate - "+t);
	}
	//notionalPremium = Double.parseDouble(common.roundedOff(Double.toString(notionalPremium)));
	return notionalPremium;
	
}

/**
 * This method Calculates Premium_Rates sheet's C17(Established 4 or more years?) Value
 */
public String Established_4_or_more_years(){
	
	String r_value = null;
		r_value = calculate_No_OfYears()>4?"Yes":"No";  
	return r_value;
	
}


/**
 * This method Calculates Premium_Rates sheet's B19(ClaimIncurred flags) Value
 */
public int get_ClaimIncurred(int claim_year_seq,String year_to_consider){
	
	int claimIncurred = 0;
	String year=null;
	
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
		case "Rewind":
			data_map = common.Rewind_excel_data_map;
			break;
	
	}
	try{
		
		/*switch(year_to_consider){
	
		case "This Year":
			year="TY";
			break;
		case "Last Year":
			year="LY";
			break;
		}*/
		
		switch(year_to_consider){
		
		case "This Year":
			year="this_year";
			break;
		case "Last Year":
			year="last_year";
			break;
		}
	
		//CH_2011_Open_Claims_TY
		double _paid = Double.parseDouble((String)data_map.get("CH_paid_"+year+"_"+claim_year_seq));
		double _reserve = Double.parseDouble((String)data_map.get("CH_reserve_"+year+"_"+claim_year_seq));
		if((_paid > 0) || (_reserve > 0)){
			claimIncurred=1;
		}else{
			claimIncurred=0;
		} 
			
		
	}catch(Throwable t){
		System.out.println("Error while Calculating Claim Incurred - "+t);
	}
	return claimIncurred;
	
}
/**
 * This method Calculates Premium_Rates sheet's B25(Total Years with Claims incurred) Value
 */
public int calculate_Total_Years_with_Claims_incurred(){
	
	int total_yrs = 0;
	PIA_Rater = OR.getORProperties();
	try{
		String[] years_for_Claims = PIA_Rater.getProperty("PIA_Years_to_consider_For_Claims").split(",");
		int[] year_index = {0,1,2,3,4};
		for(int i=0;i<years_for_Claims.length;i++){
			total_yrs = total_yrs + get_ClaimIncurred(year_index[i],"This Year");
		}
			
	}catch(Throwable t){
		System.out.println("Error while Calculating Total Yesrs Claim Incurred - "+t);
	}
	return total_yrs;
	
}

/**
 * This method Calculates Premium_Rates sheet's B26(Eligible for claims discount? (>3 years history + Claims in 1 year only?)) Value
 */
public String Eligible_for_claims_discount_3_years_only(){
	
	String isEligibleForClaimDiscount = "";

	try{
		
		switch(Established_4_or_more_years()){
		
			case "Yes":
				if(calculate_Total_Years_with_Claims_incurred()==1){
					isEligibleForClaimDiscount = "Yes";
				}else{
					isEligibleForClaimDiscount = "No";
				}
			break;
			
			case "No":
				isEligibleForClaimDiscount = "No";
				break;
			default:
				System.out.println("** Invalid Value - Established_4_or_more_years ***");
				break;
		}
		
		
		
	}catch(Throwable t){
		System.out.println("Error while Calculating Eligible_for_claims_discount_3_years_only - "+t);
	}
	return isEligibleForClaimDiscount;
	
}

/**
 * This method Calculates Premium_Rates sheet's B27(Year in which claim(s) incurred) Value
 */
public String Year_in_which_claims_incurred(){
	
	try{
		
		if(calculate_Total_Years_with_Claims_incurred()==0){
				return "None";
		}else{
			if(calculate_Total_Years_with_Claims_incurred()>1){
				return "Multiple";
			}else{
				String[] years_for_Claims = PIA_Rater.getProperty("PIA_Years_to_consider_For_Claims").split(",");
				for(String claim_year : years_for_Claims){
					if(get_ClaimIncurred(Integer.parseInt(claim_year),"This Year")==1)
						return claim_year;
					else
						continue;
				}
				
			}
		}
	}catch(Throwable t){
		System.out.println("Error while Calculating Year in which claim(s) incurred - "+t);
		return "";
	}
	return null;
	
}

/**
 * This method Calculates Premium_Rates sheet's B29(No. claims reported) Value
 */
public int No_of_claims_reported(){
	
	int total_claims_reported = 0;
	PIA_Rater = OR.getORProperties();
	try{
		String[] years_for_Claims = PIA_Rater.getProperty("PIA_Years_to_consider_For_Claims").split(",");
		for(String claim_year : years_for_Claims){
			total_claims_reported = total_claims_reported + get_ClaimReported(Integer.parseInt(claim_year),"This Year");
		}
		
		
			
	}catch(Throwable t){
		System.out.println("Error while Calculating No_of_claims_reported - "+t);
		return 0;
	}
	return total_claims_reported;
	
}

/**
 * This method Calculates Open Closed Claims Reported Value
 */
public int get_ClaimReported(int claim_year,String year_to_consider){
	
	int claimReported = 0;
	String year=null;
	
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
		case "Rewind":
			data_map = common.Rewind_excel_data_map;
			break;
	
	}
	try{
		
		switch(year_to_consider){
	
		case "This Year":
			year="TY";
			break;
		case "Last Year":
			year="LY";
			break;
		}
	
		//CH_2011_Open_Claims_TY
		int  _open_claims = Integer.parseInt((String)data_map.get("CH_"+claim_year+"_Open_Claims_"+year));
		int _closed_claims = Integer.parseInt((String)data_map.get("CH_"+claim_year+"_Closed_Claims_"+year));
		claimReported = _open_claims + _closed_claims;
		
	}catch(Throwable t){
		System.out.println("Error while Calculating Claim Incurred - "+t);
		return 0;
	}
	return claimReported;
	
}

/**
 * This method Calculates Premium_Rates sheet's B30(Eligible for single loss discount (exactly 1 claim reported?)) Value
 */
public String Eligible_for_single_loss_discount_exactly_1_Claim_reported(){
	
	try{
		
		if(Eligible_for_claims_discount_3_years_only().equals("No")){
				return "No";
		}else{
			if(No_of_claims_reported()==1){
				return "Yes";
			}else{
				return "No";
			}
		}
	}catch(Throwable t){
		System.out.println("Error while Calculating Eligible_for_single_loss_discount_exactly_1_Claim_reported - "+t);
		return "";
	}
	
}

/**
 * This method Calculates Premium_Rates sheet's B31(Claims History Adjustment) Value
 */
public double calculate_Claims_History_Adjustment(){
	
	double claimHistoryAdjust = 0.0;
	try{
		if(Eligible_for_claims_discount_3_years_only().equals("No")){
			claimHistoryAdjust=1;
		}else{
			if(calculate_No_OfYears()==3){ //CR 274 changes
				claimHistoryAdjust=0.75;
			}else if(calculate_No_OfYears()==4){ //CR 274 changes
				claimHistoryAdjust=0.625;
			}else if(calculate_No_OfYears()>=5){ //CR 274 changes
				claimHistoryAdjust=0.5;
			}
	}
				
	}catch(Throwable t){
		System.out.println("Error while Calculating Claims_History_Adjustment - "+t);
		return 0;
	}
	return claimHistoryAdjust;
	
}


/**
 * This method Calculates Premium_Rates sheet's B32(Single Loss Adjustment) Value
 */
public double calculate_Single_Loss_Adjustment(){
	
	double single_Loss_Adjustment = 0.0;
	try{
		if(Eligible_for_claims_discount_3_years_only().equals("No")){
			single_Loss_Adjustment=1;
		}else{
			if(Eligible_for_single_loss_discount_exactly_1_Claim_reported().equals("Yes")){
				single_Loss_Adjustment=0.5;
			}else if(calculate_No_OfYears()==5){
				single_Loss_Adjustment=1;
			
			}
		}		
	}catch(Throwable t){
		System.out.println("Error while Calculating Single Loss Adjustment - "+t);
		return 0;
	}
	return single_Loss_Adjustment;
	
}

/**
 * This method Calculates Premium_Rates sheet's B33(Total Claims Adjustment) Value
 */
public double calculate_Total_Claims_Adjustment(){
	
	double total_Claims_Adjustment = 0.0;
	try{
		total_Claims_Adjustment = calculate_Claims_History_Adjustment()*calculate_Single_Loss_Adjustment();
	}catch(Throwable t){
		System.out.println("Error while Calculating Total Claims Adjustment - "+t);
		return 0;
	}
	return total_Claims_Adjustment;
	
}

/**
 * This method is to Calculates Premium_Rates sheet's B54(Adjusted Claims Incurred) Value
 */
public double get_AdjustedClaimsIncurred(int claim_year,String year_to_consider,int year_index,double total_claims_adj){
	
	double claimIncurred = 0;
	String year=null;
	
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
		case "Rewind":
			data_map = common.Rewind_excel_data_map;
			break;
	
	}
	
	try{
		
		switch(year_to_consider){
	
		case "This Year":
			year="this_year";
			break;
		case "Last Year":
			year="last_year";
			break;
		}
		
		//CR 274 change
		
	
		//CH_2011_Open_Claims_TY
		double _paid = total_claims_adj * Double.parseDouble((String)data_map.get("CH_paid_"+year+"_"+year_index));
		double _reserve = total_claims_adj * Double.parseDouble((String)data_map.get("CH_reserve_"+year+"_"+year_index));
		claimIncurred = _paid + _reserve;
			
		
	}catch(Throwable t){
		System.out.println("Error while Calculating Claim Incurred - "+t);
		return 0.0;
	}
	return claimIncurred;
	
}
/**
 * This method Calculates Premium_Rates sheet's B54(Adjusted Claims Incurred) Value
 */
public double calculate_Total_Adjusted_Claims_incurred(){
	
	double total_Adjusted_Claims = 0;
	PIA_Rater = OR.getORProperties();
	try{
		String[] years_for_Claims = PIA_Rater.getProperty("PIA_Years_to_consider_For_Claims").split(",");
		double total_claims_adj = calculate_Total_Claims_Adjustment(); //CR 274
		//int[] year_index = {0,1,2,3,4,5,6};
		//CR 274
		int[] year_index = {0,1,2,3,4};
		for(int i=0;i< years_for_Claims.length;i++){
			total_Adjusted_Claims = total_Adjusted_Claims + get_AdjustedClaimsIncurred(Integer.parseInt(years_for_Claims[i]),"This Year",year_index[i],total_claims_adj);
		}
			
	}catch(Throwable t){
		System.out.println("Error while Calculating Adjusted Claims Incurred - "+t);
		return 0.0;
	}
	return total_Adjusted_Claims;
	
}

/**
 * This method is to Calculates Premium_Rates sheet's B55(Adjusted Loaded Claims) Value
 */
public double get_Adjusted_Loaded_Claims(int claim_year,int claim_year_seq,String year_to_consider,int year_index,double total_claims_adj){
	
	double adjusted_Loaded_Claims = 0;
	String year=null;
	
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
		case "Rewind":
			data_map = common.Rewind_excel_data_map;
			break;
	
	}
	
	
	try{
		
		switch(year_to_consider){
	
		case "This Year":
			year="this_year";
			break;
		case "Last Year":
			year="last_year";
			break;
		}
	
		//CH_2011_Open_Claims_TY
		//CH_paid_this_year_0
	
		double _paid = total_claims_adj *Double.parseDouble((String)data_map.get("CH_paid_"+year+"_"+year_index));
		double _reserve = total_claims_adj * Double.parseDouble((String)data_map.get("CH_reserve_"+year+"_"+year_index));
		//Paid_Claims_Multiplier_previous_yr5
		if(claim_year_seq!=0){
			adjusted_Loaded_Claims = _paid*Double.parseDouble(PIA_Rater.getProperty("PIA_Paid_Claims_Multiplier_previous_yr"+claim_year_seq)) 
				+ 	_reserve*Double.parseDouble(PIA_Rater.getProperty("PIA_Reserved_Claims_Multiplier_previous_yr"+claim_year_seq)) ;
		}else{
			adjusted_Loaded_Claims = _paid*Double.parseDouble(PIA_Rater.getProperty("PIA_Paid_Claims_Multiplier_last_year"))
					+ 	_reserve*Double.parseDouble(PIA_Rater.getProperty("PIA_Reserved_Claims_Multiplier_last_year")) ;
			
		}
			
		
	}catch(Throwable t){
		System.out.println("Error while Calculating Adjusted Loaded Claims - "+t);
		return 0.0;
	}
	return adjusted_Loaded_Claims;
	
}
/**
 * This method Calculates Premium_Rates sheet's B55(Adjusted Loaded Claims) Value
 */
public double calculate_Adjusted_Loaded_Claims(){
	
	double total_Adjusted_Loaded_Claims = 0.0;
	PIA_Rater = OR.getORProperties();
	try{
		String[] years_for_Claims = PIA_Rater.getProperty("PIA_Years_to_consider_For_Claims").split(",");
		String[] years_for_Claims_seq = PIA_Rater.getProperty("PIA_Years_to_consider_For_Claims_Sequence").split(",");
		double total_claims_adj = calculate_Total_Claims_Adjustment(); //CR 274
		//int[] year_index = {0,1,2,3,4,5,6};
		//CR 274
		int[] year_index = {1,2,3,4,5};
		for(int i=0;i<years_for_Claims.length;i++){
			total_Adjusted_Loaded_Claims = total_Adjusted_Loaded_Claims + get_Adjusted_Loaded_Claims(Integer.parseInt(years_for_Claims[i]),Integer.parseInt(years_for_Claims_seq[i]),"This Year",year_index[i],total_claims_adj);
		}
			
	}catch(Throwable t){
		System.out.println("Error while Calculating Adjusted Loaded Claims - "+t);
		return 0.0;
	}
	return total_Adjusted_Loaded_Claims;
	
}

/**
 * This method Calculates Premium_Rates sheet's B55(Annual Burn) Value
 */
public double calculate_Annual_Burn(){
	
	double total_Annual_Burn = 0.0;
	try{
		
		total_Annual_Burn = calculate_Adjusted_Loaded_Claims()/calculate_No_OfYears();
				
	}catch(Throwable t){
		System.out.println("Error while Calculating Annual Burn - "+t);
		return 0.0;
	}
	return total_Annual_Burn;
	
}

/**
 * This method Calculates Premium_Rates sheet's B59(Burn Rate) Value
 */
public double calculate_Annual_Burn_Rate(){
	
	double total_Annual_Burn_Rate = 0.0,raw_burn_rate=0.0;
	DecimalFormat df = new DecimalFormat("00.0000");
	try{
		
		total_Annual_Burn_Rate = calculate_Annual_Burn()/calculate_NotionalPremium();
		raw_burn_rate = total_Annual_Burn_Rate;
		if(Double.isInfinite(total_Annual_Burn_Rate) || Double.isNaN(total_Annual_Burn_Rate)){
			//TestUtil.reportStatus("SPI Rater | Annual Burn Rate = <b>"+0.0+" %</b>", "Info", false);
			common_PIA.PIA_Rater_output.put("Burn Rate", 0.0);
			total_Annual_Burn_Rate = 0.0;
		}else{
			total_Annual_Burn_Rate = Double.parseDouble(df.format(total_Annual_Burn_Rate));
			total_Annual_Burn_Rate = total_Annual_Burn_Rate*100;//Two Decimal
			//TestUtil.reportStatus("SPI Rater | Annual Burn Rate = <b>"+common.roundedOff(Double.toString(total_Annual_Burn_Rate))+" %</b>", "Info", false);
			common_PIA.PIA_Rater_output.put("Burn Rate", Double.parseDouble(common.roundedOff(Double.toString(total_Annual_Burn_Rate))));
		}
				
	}catch(Throwable t){
		
		System.out.println("Error while Calculating Annual Burn Rate - "+t);
		return 0.0;
	}
	//System.out.println("Annula Burn Rate B59 = "+total_Annual_Burn_Rate);
	return raw_burn_rate;
	
}

/**
 * This method Calculates Premium_Rates sheet's B60(Is Burn between 40%-50%?) Value
 */
/*public boolean Is_Burn_between_40_50_Cent(){
	
	double annual_burn_rate = calculate_Annual_Burn_Rate();
	try{
		if(annual_burn_rate>=40 && annual_burn_rate <=50){
			return true;
		}else{
			
			return false;
		}
				
	}catch(Throwable t){
		System.out.println("Error while Calculating Is Burn between 40%-50%? - "+t);
		return false;
	}
	
}*/

/**
 * This method Calculates Premium_Rates sheet's B60(Is Burn between 51%-80%?) Value //CR 274
 */
public boolean Is_Burn_between_51_80_Cent(){
	
	double annual_burn_rate = calculate_Annual_Burn_Rate();
	try{
		if((annual_burn_rate)>=50 && (annual_burn_rate) <=80){
			return true;
		}else{
			
			return false;
		}
				
	}catch(Throwable t){
		System.out.println("Error while Calculating Is Burn between 50%-80%? - "+t);
		return false;
	}
	
}

/**
 * This method Calculates Premium_Rates sheet's B61(Claims Adjustments) Value
 */
public double calculate_Claims_Adjustments(){
	
	double total_Claims_Adjustments = 0.0;
	double annula_Burn_Rate = calculate_Annual_Burn_Rate();
	try{
			//if(annula_Burn_Rate > 0.4){
			//CR 274
		//annula_Burn_Rate=Double.parseDouble(common.roundedOff(Double.toString(annula_Burn_Rate)));
		if((annula_Burn_Rate) > 0.5){
			double f_part = (annula_Burn_Rate)-0.8;
			total_Claims_Adjustments = (f_part*1.3)+1;
		}else{
			int no_of_years_Claims = Integer.parseInt(PIA_Rater.getProperty("PIA_No_years_claims_to_look_at"));
			total_Claims_Adjustments = 1-(((1-(((0.5/0.4)*(annula_Burn_Rate))+0.5))*calculate_No_OfYears())/no_of_years_Claims);
		}
		/*	if(annula_Burn_Rate > 0.4){
				double f_part = (annula_Burn_Rate/100)-0.5;
				total_Claims_Adjustments = (f_part*1.32)+1;
			}else{
				int no_of_years_Claims = Integer.parseInt(PIA_Rater.getProperty("No_years_claims_to_look_at"));
				total_Claims_Adjustments = 1-(((1-(((0.5/0.4)*(annula_Burn_Rate/100))+0.5))*calculate_No_OfYears())/no_of_years_Claims);
			}*/		
		
				
	}catch(Throwable t){
		System.out.println("Error while Calculating Claims Adjustments - "+t);
		return 0.0;
	}
	//System.out.println("Claims Adjustments B61 = "+total_Claims_Adjustments);
	return total_Claims_Adjustments;
	
}

/**
 * This method Calculates Premium_Rates sheet's B62(Rate) Value
 */
public double calculate_Rate(){
	
	double claim_adjustments = calculate_Claims_Adjustments();
	try{
		if(Is_Burn_between_51_80_Cent()){
			return 1;
		}else{
			return claim_adjustments;
		}
				
	}catch(Throwable t){
		System.out.println("Error while Calculating Rate - "+t);
		return 0;
	}
	
}

/**
 * This method Calculates Premium_Rates sheet's B63(Capped Rate) Value
 */
public double calculate_Capped_Rate(){
	
	double capped_Rate = calculate_Rate();
	try{
		if(calculate_Rate() > 3){
			return 3;
		}else{
			return capped_Rate;
		}
				
	}catch(Throwable t){
		System.out.println("Error while Calculating Capped Rate - "+t);
		return 0;
	}
	
}

/**
 * This method Calculates Premium_Rates sheet's B63(Rate Post Claims/Book Rate)/B20(Rater sheet) Value
 */
public double calculate_Rate_Post_Claims(){
	
	double rate_Post_Claims = 0.0;
	DecimalFormat formatter = new DecimalFormat("00.00000000"); //CR 274- added more decimals
	try{
		rate_Post_Claims = calculate_NotionalRate()*calculate_Rate();//6 Decimals
		if(!Double.isInfinite(rate_Post_Claims) && spi_GrossFee!=0.0 ){
			double f_book_rate = Double.parseDouble(formatter.format(((rate_Post_Claims*100))));
			common_PIA.PIA_Rater_output.put("Book_Rate_Cent", f_book_rate);
			TestUtil.reportStatus("Book Rate % = <b>"+f_book_rate+"</b>", "Info", false);
		}else{
				rate_Post_Claims = 0.0;
				common_PIA.PIA_Rater_output.put("Book_Rate_Cent", 0.0);
				TestUtil.reportStatus("Book Rate % = <b>"+0.0+"</b>", "Info", false);
			
		}
	}catch(Throwable t){
		System.out.println("Error while Calculating Rate Post Claims- "+t);
		return 0;
	}
	//System.out.println("Rate Post Claims Premium_Rates sheet's B63 = "+rate_Post_Claims);
	return rate_Post_Claims;
}

/**
 * This method Calculates Rater sheet's B25(Technical Premium Capped) Value //CR 274
 */
public double Calculate_Technical_Premium_Capped(){
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
		case "Rewind":
			data_map = common.Rewind_excel_data_map;
			break;
	
	}
	
	
	
	double tech_Premium_cap = 0.0;
	double initial_Premium = 0.0;
	spi_GrossFee = Double.parseDouble((String)data_map.get("SP_GrossFee"));
	double OPsRate = calculate_OPsRate();
	try{
		initial_Premium = calculate_Rate_Post_Claims()*spi_GrossFee;// 2 Decimal
		//Minimum Premium Check
		if(OPsRate<0.4){
			if(initial_Premium<2150){
				tech_Premium_cap=2150;
			}else{
				common_PIA.PIA_Rater_output.put("Technical_Premium_Capped", initial_Premium);
				return initial_Premium;
			}
		}else{
			if(initial_Premium<3400){
				tech_Premium_cap=3400;
			}else{
				common_PIA.PIA_Rater_output.put("Technical_Premium_Capped", initial_Premium);
				return initial_Premium;
			}
		}
				
	}catch(Throwable t){
		System.out.println("Error while Calculating Technical_Premium_Capped - "+t);
		return 0;
	}
	System.out.println("Technical_Premium_Capped Rater sheet's B25 =  "+tech_Premium_cap);
	common_PIA.PIA_Rater_output.put("Technical_Premium_Capped", tech_Premium_cap);
	return tech_Premium_cap;
}


/**
 * This method Calculates Rater sheet's B21/B25(Initial Premium) Value
 */
public double calculate_Initial_Premium(){
	
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
		case "Rewind":
			data_map = common.Rewind_excel_data_map;
			break;
	
	}
	
	
	
	double initial_Premium = 0.0;
	spi_GrossFee = Double.parseDouble((String)data_map.get("SP_GrossFee"));
	double OPsRate = calculate_OPsRate();
	try{
		initial_Premium = calculate_Rate_Post_Claims()*spi_GrossFee;// 2 Decimal
		//Minimum Premium Check
		if(OPsRate<0.4){
			if(initial_Premium<2150){
				initial_Premium=2150;
			}else{
				common_PIA.PIA_Rater_output.put("Initial_Premium", initial_Premium);
				common_PIA.PIA_Rater_output.put("Technical_Premium_Capped", initial_Premium); //CR274
				return initial_Premium;
			}
		}else{
			if(initial_Premium<3400){
				initial_Premium=3400;
			}else{
				common_PIA.PIA_Rater_output.put("Initial_Premium", initial_Premium);
				common_PIA.PIA_Rater_output.put("Technical_Premium_Capped", initial_Premium); //CR 274
				return initial_Premium;
			}
		}
				
	}catch(Throwable t){
		System.out.println("Error while Calculating Initial Premium - "+t);
		return 0;
	}
	System.out.println("initial_Premium Rater sheet's B21 =  "+initial_Premium);
	common_PIA.PIA_Rater_output.put("Initial_Premium", initial_Premium);
	common_PIA.PIA_Rater_output.put("Technical_Premium_Capped", initial_Premium); //CR 274
	return initial_Premium;
}

/**
 * This method Calculates Rater sheet's B22/B23/B24(Book Premium) Value
 */
public boolean calculate_SPI_Book_Premium(){
	
	double book_Premium_12m = 0.0,book_Premium_18m = 0.0,book_Premium_24m = 0.0;
	double tech_Premium_cap = calculate_Initial_Premium();
	//double tech_Premium_cap = Calculate_Technical_Premium_Capped(); //CR 274
	try{
		
		book_Premium_12m = tech_Premium_cap*1.05;// 2-decimal
		book_Premium_12m = Double.parseDouble(common.roundedOff(Double.toString(book_Premium_12m)));
		TestUtil.reportStatus("SPI Rater Book Premium 12m = "+book_Premium_12m, "Info", false);
		//System.out.println("Book Premium 12 m = "+book_Premium_12m);
		
		book_Premium_18m = (tech_Premium_cap*1.025); //2-decimal 
		//book_Premium_18m = (tech_Premium_cap*1.025) * 1.5; //2-decimal //CR 274
		book_Premium_18m = Double.parseDouble(common.roundedOff(Double.toString(book_Premium_18m)));
		TestUtil.reportStatus("SPI Rater Book Premium 18m = "+book_Premium_18m, "Info", false);
		//System.out.println("Book Premium 18 m = "+book_Premium_18m);
		
		book_Premium_24m = tech_Premium_cap; 
		//book_Premium_24m = tech_Premium_cap * 2; //CR274
		book_Premium_24m = Double.parseDouble(common.roundedOff(Double.toString(book_Premium_24m)));
		TestUtil.reportStatus("SPI Rater Book Premium 24m = "+book_Premium_24m, "Info", false);
		//System.out.println("Book Premium 24 m = "+book_Premium_24m);
		
		common_PIA.PIA_Rater_output.put("Book_Premium_12_months", book_Premium_12m);
		common_PIA.PIA_Rater_output.put("Book_Premium_18_months", book_Premium_18m);
		common_PIA.PIA_Rater_output.put("Book_Premium_24_months", book_Premium_24m);
		
		System.out.println(common_PIA.PIA_Rater_output);
				
	}catch(Throwable t){
		System.out.println("Error while Calculating Book Premium Values - "+t);
		return false;
	
	}
	return true;
}

/**
 * This method Calculates Rater sheet's B29/B30/B31(Gross Premium annualised) Value
 */
public void calculate_Gross_Premium(){
	
	//double gross_Premium_12m = 0.0,gross_Premium_18m = 0.0,gross_Premium_24m = 0.0;
	
	try{
		
		/*gross_Premium_24m = calculate_Initial_Premium();
		gross_Premium_12m = calculate_Initial_Premium()*1.05;
		gross_Premium_18m = calculate_Initial_Premium()*1.025;
		
		
		PIA_Rater_output.put("gross_Premium_12_months", gross_Premium_12m);
		PIA_Rater_output.put("gross_Premium_18_months", gross_Premium_18m);
		PIA_Rater_output.put("gross_Premium_24_months", gross_Premium_24m);*/
		
				
	}catch(Throwable t){
		System.out.println("Error while Calculating Gross Premium - "+t);
	
	}
}


public boolean Solicitors_PI( Map<Object, Object> map_data){
	boolean retVal = true;
	
	try{
		customAssert.assertTrue(common.funcPageNavigation("Solicitors PI", ""), "Navigation problem to Solicitors PI page .");
		
		// Area of Practice Table section :
		
		customAssert.assertTrue(SPI_AreaOfPractice(NB_Structure_of_InnerPagesMaps),"Unable to handle table 'Area of practice'");
		
		// Select Work split to value :
		customAssert.assertTrue(k.DropDownSelection("SPI_SP_WorkSplittoUse", (String)map_data.get("SP_WorkSplittoUse")), "Unable to enter 'Work Split to use' value");

		// fees/Turnover Table section :
		
		customAssert.assertTrue(SPI_Fees_Turnover(map_data),"Unable to handle table 'Fees/Turnover'");
		
		// Select 'Rate on' value :
		customAssert.assertTrue(k.DropDownSelection("SPI_SP_RateOn", (String)common.NB_excel_data_map.get("SP_RateOn")), "Unable to enter 'Rate on' value");
		
		String default_PI_policy_fee = k.getAttribute("PIA_SP_AdminFeeSP", "value");
		customAssert.assertEquals(default_PI_policy_fee, (String)map_data.get("SP_DefaultAdminFeeSP"),"PI Default Admin fee is In-correct. Expected: "+(String)map_data.get("SP_DefaultAdminFeeSP")+" and Actual: "+default_PI_policy_fee);
		
		// Excess Section :
		customAssert.assertTrue(k.Input("PIA_SP_AdminFeeSP", (String)map_data.get("SP_AdminFeeSP")),	"Unable to enter value in Admin fees field .");
		customAssert.assertTrue(k.DropDownSelection("SPI_SP_Aggegate", (String)common.NB_excel_data_map.get("SP_Aggegate")), "Unable to enter 'Aggregate' value");
		customAssert.assertTrue(calculate_SPI_Book_Premium(), "SPI Book Premium Calculation is function having issue .");
		TestUtil.reportStatus("SPI Rater Lookup values are calculated sucessfully . ", "Info", true);
		// Burn Rate:
		burnRate = common_PIA.PIA_Rater_output.get("Burn Rate");
				
		//Policy start and end date :
		Refer_Policy_SDate = k.getText("SPI_SP_PolicyStartDate");
		Refer_Policy_EDate = k.getText("SPI_SP_PolicyEndDate");
		dAdminFee_CoverSPI=  Double.parseDouble((String)map_data.get("SP_AdminFeeSP"));
		
		//Period Rating Table Section : 
		customAssert.assertTrue(coverSPI_PeriodRatingTable(map_data, ""),"Error in Period Rating Table function . ");
		
		TestUtil.reportStatus("Solicitors PI Cover details are filled and Verified sucessfully . ", "Info", true);
		
		
		return retVal;
	
	}catch(Throwable t){
		System.out.println("Error in Solicitors_PI function - "+t);
		return false;
	}

	
}

public static boolean SPI_AreaOfPractice( Map<String, List<Map<String, String>>> mdata){
	boolean retVal = true;
	int totalCols, sTotal = 0;
	
	try{
	
		String sUniqueCol ="Area of practice";
		int tableId = 0;
		int noOfActivities =0;
		
		String sTablePath = "html/body/div[3]/form/div/table";
		
		tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
		
		sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
		WebElement table= driver.findElement(By.xpath(sTablePath));
		totalCols = table.findElements(By.tagName("th")).size();
		try{
		
		noOfActivities = common.no_of_inner_data_sets.get("Area of Practice");
		
		}catch(Throwable t){
			noOfActivities = 0;
		}
		
		// Enter values to the table :
		
		for(int i = 0; i < noOfActivities; i++ ){
			
			//click on 'Add Row' link :
			driver.findElement(By.xpath(sTablePath + "/tbody/tr["+ (i+2) +"]/td[5]/a")).click();
			
			//Enter Data :
			
			customAssert.assertTrue(k.DropDownSelection_DynamicXpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[1]/select", common.NB_Structure_of_InnerPagesMaps.get("Area of Practice").get(i).get("PIA_AOP_AreaOfPracticeValue")),"Unable to enter activity in Area of practice table");
			
			customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[2]/input", common.NB_Structure_of_InnerPagesMaps, "Area of Practice", i, "PIA_AOP_", "LastYear", "Last year (%)", "Input"),"Unable to enter Last year (%) in Area of practice table");
			customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[3]/input", common.NB_Structure_of_InnerPagesMaps, "Area of Practice", i, "PIA_AOP_", "PriorYear", "PriorYear", "Input"),"Unable to enter prior year (%) in Area of practice table");
			customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[4]/input", common.NB_Structure_of_InnerPagesMaps, "Area of Practice", i, "PIA_AOP_", "PriorYear2", "PriorYear", "Input"),"Unable to enter prior year 2 (%) in Area of practice table");		
		
		}
		
		k.Click("SPI_Save_SOI");
		// Read total, verify and adjust if not equals to 100% :
						
		for(int j = 0; j < totalCols-2; j++){
			
			sTotal =  Integer.parseInt(k.GetText_DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr["+(noOfActivities+1)+"]/td["+(j+2)+"]"));
			
			if(sTotal != 100){
				TestUtil.reportStatus("Total does not equal to 100% for AreaOfPractice table. Entered value is : <b>[  "+ sTotal +"  ]</b>. ", "Info", false);
				retVal = false;
			}else{
				TestUtil.reportStatus("Total is verified AreaOfPractice table. Entered value is : <b>[  "+ sTotal +"  ]</b>.", "Info", true);
			}
		}		
		
		return retVal;
	
	}catch(Throwable t){
		System.out.println("Error in SPI_AreaOfPractice - "+t);
		return false;
	}
	
	
}

public static boolean SPI_Fees_Turnover( Map<Object, Object> map_data){
	boolean retVal = true;
	int totalCols, sTotal = 0, totalRows, GLimit;
	String sFinal ="", sVal = "";
	
	try{
		
		if(common_PIA.isRewindSelected.contains("Yes") || common_PIA.isRenewalRewindFlow){
			sVal = "Rewind";
		}
								
		String sUniqueCol ="Year";
		int tableId = 0;
		
		String sTablePath = "html/body/div[3]/form/div/table";
		
		tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
		
		sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
		WebElement table= driver.findElement(By.xpath(sTablePath));
		
		totalCols = table.findElements(By.tagName("th")).size();
		totalRows = table.findElements(By.tagName("tr")).size();
		
		String sval = (String)map_data.get("PG_Geographical_limit");
		GLimit  = (sval.split(",")).length;
		
		switch (sVal){
			case "Rewind" :
				sFinal = (String)map_data.get("SP_RateOn_Rewind");
				  break;
				 
			default :
				
				// Enter values to the table :
				sFinal = (String)map_data.get("SP_RateOn");
				for(int i = 0; i < totalCols-3; i++ ){
					
					//Get Column name :
					
					String Reagon = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/thead/tr/th["+(i+3)+"]");
					
					if(Reagon.length() > 0){
						
						if(Reagon.contains("Rest of the World")){
							Reagon = "ROW";
						}else if(Reagon.contains("USA/ Canada")){
							Reagon = "USACanada";
						}
						
						//Enter Data :
						
							customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr[1]/td[" + (i+3) +"]/input", map_data, "Solicitors PI", i, "SP_", "Estimate"+Reagon, Reagon, "Input"),"Unable to enter Estimate value for reagon - "+Reagon);				
							customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr[2]/td[" + (i+3) +"]/input", map_data, "Solicitors PI", i, "SP_", "2017"+Reagon, Reagon, "Input"),"Unable to enter 2017 value for reagon - "+Reagon);
							customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr[3]/td[" + (i+3) +"]/input", map_data, "Solicitors PI", i, "SP_", "2016"+Reagon, Reagon, "Input"),"Unable to enter 2016 value for reagon - "+Reagon);
							customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr[4]/td[" + (i+3) +"]/input", map_data, "Solicitors PI", i, "SP_", "2015"+Reagon, Reagon, "Input"),"Unable to enter 2015 value for reagon - "+Reagon);
							customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr[5]/td[" + (i+3) +"]/input", map_data, "Solicitors PI", i, "SP_", "2014"+Reagon, Reagon, "Input"),"Unable to enter 2014 value for reagon - "+Reagon);
					}			
				}
				
				break;			
				
		}
		
		// Read FeesIncome Total :
		
			TotalFeesIncome_Estimate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[1]/td[" +totalCols+ "]/input").replaceAll(",", ""));
			TotalFeesIncome_2017 = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[2]/td[" +totalCols+ "]/input").replaceAll(",", ""));
			TotalFeesIncome_2016 = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[3]/td[" +totalCols+ "]/input").replaceAll(",", ""));
			TotalFeesIncome_2015 = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[4]/td[" +totalCols+ "]/input").replaceAll(",", ""));
			TotalFeesIncome_2014 = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[5]/td[" +totalCols+ "]/input").replaceAll(",", ""));
			Total_Average = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[6]/td[" +totalCols+ "]/input").replaceAll(",", ""));
							
			if(sFinal.contains("Estimate")){
				global_SOI_GrossFees = TotalFeesIncome_Estimate; 
			}else if(sFinal.contains("Last Year")){
				global_SOI_GrossFees = TotalFeesIncome_2017;
			}else if(sFinal.equalsIgnoreCase("Prior Year")){
				global_SOI_GrossFees = TotalFeesIncome_2016;
			}else if(sFinal.equalsIgnoreCase("Prior Year 2")){
				global_SOI_GrossFees = TotalFeesIncome_2015;
			}else if(sFinal.equalsIgnoreCase("Prior Year 3")){
				global_SOI_GrossFees = TotalFeesIncome_2014;
			}else{
				global_SOI_GrossFees = Total_Average;
			}
			
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Solicitors PI",(String)map_data.get("Automation Key"), "SP_GrossFee", String.valueOf(global_SOI_GrossFees), map_data);
			
			if(common_PIA.isRewindSelected.contains("Yes") || common_PIA.isRenewalRewindFlow){
				map_data.put("SP_GrossFee",  String.valueOf(global_SOI_GrossFees));
			}
			
		return retVal;
	
	}catch(Throwable t){
		System.out.println("Error in SPI_Fees_Turnover - "+t);
		return false;
	}
	
}

public boolean coverSPI_PeriodRatingTable( Map<Object, Object> map_data, String sCase){
	boolean retVal = true;
	double s_SOI_BookRate = 0.00, s_SOI_InitialP = 0.00, s_SOI_BookP = 0.00, s_SOI_TechA = 0.00, s_SOI_RevisedP = 0.00, s_SOI_AnnualP = 0.00;
	double s_SOI_NetNetP = 0.00, s_SOI_Grossp = 0.00, s_SOI_TotalP = 0.00, s_SOI_IndLimit = 0.00;
	
	double s_SOI_PenComm = 0.00, s_SOI_BrokerComm = 0.00, s_SOI_InsTaxR = 0.00;
	double s_Val_PenAmt = 0.00, s_Val_BrokerAmt = 0.00, s_Val = 0.00;
	int xlWRite_Index = 0;
	
	String SummaryTable_UniqueCol, SummaryTable_Path;
	
	String sPeriodMonths;
	
	Map<String, List<Map<String, String>>> data_Structure_of_InnerPagesMaps = null;
	
	switch(common.currentRunningFlow){
	
		case "NB":
			data_Structure_of_InnerPagesMaps = common.NB_Structure_of_InnerPagesMaps;
			break;
		case "MTA":
			data_Structure_of_InnerPagesMaps = common.MTA_Structure_of_InnerPagesMaps;
			break;
		case "Renewal":
			data_Structure_of_InnerPagesMaps = common.Renewal_Structure_of_InnerPagesMaps;
			break;	
	
	}
	
	
	try{
		
		//Identify Period Rating Table :
		
			String sUniqueCol ="OPTION";
			int tableId = 0;
			
			String sTablePath = "html/body/div[3]/form/div/table";
			
			tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
			
			sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
			WebElement table= driver.findElement(By.xpath(sTablePath));
			
			String sPeriodRatings = (String)map_data.get("SP_PeriodRatingTable");
			String sValue[] = sPeriodRatings.split(";");
			int noOfPR = sValue.length;
			
		// Identify Summary Table  :
			
			SummaryTable_UniqueCol ="Description";		
			tableId = k.getTableIndex(SummaryTable_UniqueCol,"xpath","html/body/div[3]/form/div/table");		
			SummaryTable_Path = "html/body/div[3]/form/div/table["+ tableId +"]";
			WebElement Summary_table= driver.findElement(By.xpath(sTablePath));			
			
		switch (sCase){
		
			case "" :
				
					// Enter Data in Period Rating Table :
				
						if(common_PIA.isRewindSelected.contains("Yes") || common_PIA.isRenewalRewindFlow){
							burnRate = common_PIA.PIA_Rater_output.get("Burn Rate");
							// Do Nothing
						}else{
							for(int i = 0; i < noOfPR ; i++ ){
								
								String sVal = data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_IsSelected");
														
									//click on 'Add Row' link :
									
									if(i == 0){
										driver.findElement(By.xpath(sTablePath + "/tbody/tr/td[12]/a")).click();
									}else{
										driver.findElement(By.xpath(sTablePath + "/tbody/tr["+ (i+1) +"]/td[12]/a")).click();
									}							
									
									// Enter Data :
									
									customAssert.assertTrue(k.DropDownSelection_DynamicXpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[2]/select", data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_PeriodofInsurance")),"Unable to enter PeriodofInsurance in Period rating table");
									customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[3]/input", data_Structure_of_InnerPagesMaps, "Period Rating Table", i, "PRT_", "ExcessAmount", "ExcessAmount", "Input"),"Unable to enter ExcessAmount in Period rating table");
									customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[4]/input", data_Structure_of_InnerPagesMaps, "Period Rating Table", i, "PRT_", "TechAdjust", "TechAdjust", "Input"),"Unable to enter TechAdjust in Period rating table");
									
							}
				
							// Enter Comm Adjust in Summary Table if required :
								customAssert.assertTrue(k.DynamicXpathWebDriver(driver, SummaryTable_Path + "/tbody/tr[1]/td[10]/input", map_data, "Solicitors PI", 0, "SP_", "PICommAdjustment", "Comm Adjust", "Input"),"Unable to enter Comm Adjust value in summary table");
								global_SOI_CommAdjust = Double.parseDouble((String)map_data.get("SP_PICommAdjustment"));
						}
						
						if(common_PIA.isRenewalRewindFlow){
							customAssert.assertTrue(k.DynamicXpathWebDriver(driver, SummaryTable_Path + "/tbody/tr[1]/td[10]/input", map_data, "Solicitors PI", 0, "SP_", "PICommAdjustment", "Comm Adjust", "Input"),"Unable to enter Comm Adjust value in summary table");
							global_SOI_CommAdjust = Double.parseDouble((String)map_data.get("SP_PICommAdjustment"));
			
						}
							
					// Read Values of Period Rating Table from screen and calculations :
					
						for(int i = 0; i < noOfPR ; i++ ){	
																					
							if(data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_IsSelected").contains("Yes")){
								
								xlWRite_Index = i ;
								
								if(i>0){
										driver.findElement(By.xpath(sTablePath + "/tbody/tr["+ (i+1) +"]/td[1]/input[2]")).click();
								}								
								
								k.Click("SPI_ApplyBookRate");
								
								
								//Duration :
									duration_SoPI = Integer.parseInt(k.getText("SPI_SP_Duration"));
								
								//Read Values from Screen -  Summary Table :
								
									global_PeriodMonths = Integer.parseInt(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[2]"));
									global_FeesIncome = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[3]").replaceAll(",", ""));
									global_IndemnityLimit = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[4]").replaceAll(",", ""));
									global_SOI_BookRate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[5]/input").replaceAll(",", ""));
									global_SOI_InitialP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[6]").replaceAll(",", "")));
									gloabal_SOI_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[7]/input").replaceAll(",", ""));
									global_SOI_TecgAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[8]/input").replaceAll(",", ""));
									global_SOI_RevisedP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[9]/input").replaceAll(",", ""));
									global_SOI_sPremium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[12]/input").replaceAll(",", ""));							
								
								// Read values from Period Rating Table :
								
									global_SOI_AnnualP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[5]/input").replaceAll(",", "")));
									global_SOI_NetNetP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[6]/input").replaceAll(",", "")));
									global_SOI_GrossP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[9]/input").replaceAll(",", "")));
									global_SOI_TotalPremium = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[11]/input").replaceAll(",", "")));
									
								// Read Default Values Which are referred on Premium Summary Screen :
							
								Refer_PenComm = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[7]/input").replaceAll(",", "")));
								Refer_BrokerComm = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[8]/input").replaceAll(",", "")));
								Refer_InsTax = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[10]/input").replaceAll(",", "")));
							
							//Calculated Values :
							
								sPeriodMonths = data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_PeriodofInsurance");
								
								String sVal = (String)map_data.get("PG_IsYourPracticeLLP");
								if(sVal.contains("Yes")){
									s_SOI_IndLimit = 3000000.00;
								}else{
									s_SOI_IndLimit = 2000000.00;
								}
								
								s_SOI_BookRate = common_PIA.PIA_Rater_output.get("Book_Rate_Cent");
								//s_SOI_InitialP = common_PIA.PIA_Rater_output.get("Book_Premium_24_months");
								//CR 274
								s_SOI_InitialP = common_PIA.PIA_Rater_output.get("Technical_Premium_Capped");
								
								if(sPeriodMonths.contains("12")){
									s_SOI_BookP = common_PIA.PIA_Rater_output.get("Book_Premium_12_months");
								}else if(sPeriodMonths.contains("18")){
									s_SOI_BookP = common_PIA.PIA_Rater_output.get("Book_Premium_18_months");
								}else{
									s_SOI_BookP = common_PIA.PIA_Rater_output.get("Book_Premium_24_months");
								}
								
								s_SOI_TechA = Double.parseDouble(common.roundedOff(data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_TechAdjust")));
								s_SOI_RevisedP = Double.parseDouble(common.roundedOff(Double.toString(((s_SOI_BookP*s_SOI_TechA)/100) + s_SOI_BookP)));
								s_SOI_AnnualP = Double.parseDouble(common.roundedOff(Double.toString(((s_SOI_RevisedP * global_SOI_CommAdjust)/100) + s_SOI_RevisedP)));
								
								s_SOI_NetNetP = Double.parseDouble(common.roundedOff(Double.toString(((s_SOI_AnnualP/365)*duration_SoPI))));
								
								double grossCommPerc = Refer_PenComm + Refer_BrokerComm;
								
								s_Val_PenAmt = Math.abs(Double.parseDouble(common.roundedOff(Double.toString((s_SOI_NetNetP/((100-grossCommPerc)/100))*(Refer_PenComm/100)))));
								s_Val_BrokerAmt  = Math.abs(Double.parseDouble(common.roundedOff(Double.toString((s_SOI_NetNetP/((100-grossCommPerc)/100))*(Refer_BrokerComm/100)))));
								s_Val  = s_SOI_NetNetP + s_Val_PenAmt;
								
								s_SOI_Grossp = s_Val + s_Val_BrokerAmt;
								//CR 274 change
								
								
								s_SOI_TotalP = Double.parseDouble(common.roundedOff(Double.toString(s_SOI_Grossp + ((s_SOI_Grossp*Refer_InsTax)/100))));
								
								double s_BurnRate = Double.parseDouble(common.roundedOff(k.getText("SPI_PI_BurnRate").replaceAll(",", "")));
								
							// Comparisons :
									
								
								CommonFunction.compareValues(burnRate, s_BurnRate,"BurnRate");
								CommonFunction.compareValues(Double.parseDouble(sPeriodMonths), Double.parseDouble(String.valueOf(global_PeriodMonths)),"Period of insurance month ");
								CommonFunction.compareValues(global_SOI_GrossFees, global_FeesIncome,"Fees Income");
								CommonFunction.compareValues(s_SOI_IndLimit, global_IndemnityLimit,"Indemnity Limit");
								CommonFunction.compareValues(s_SOI_BookRate, global_SOI_BookRate,"SOI BookRate");
								CommonFunction.compareValues(s_SOI_InitialP, global_SOI_InitialP,"SOI Initial Premium");
								CommonFunction.compareValues(s_SOI_BookP, gloabal_SOI_BookP,"SOI Book Premium");
								CommonFunction.compareValues(s_SOI_TechA, global_SOI_TecgAdjust,"SOI TechAdjust");
								CommonFunction.compareValues(s_SOI_RevisedP, global_SOI_RevisedP,"SOI Revised Premium");
								CommonFunction.compareValues(s_SOI_AnnualP, global_SOI_sPremium,"SOI Annual Premium From Summary Table");
								CommonFunction.compareValues(s_SOI_AnnualP, global_SOI_AnnualP,"SOI Annual Premium From PR Table");
								CommonFunction.compareValues(s_SOI_NetNetP, global_SOI_NetNetP,"SOI Net Net Premium");
								CommonFunction.compareValues(s_SOI_Grossp, global_SOI_GrossP,"SOI Gross Premium");
								CommonFunction.compareValues(s_SOI_TotalP, global_SOI_TotalPremium,"SOI Total Premium");
						}						
					}
						
					break;
					
			case "Edit_PS" :
				
				// Enter Comm Adjust in Summary Table if required :
					customAssert.assertTrue(k.DynamicXpathWebDriver(driver, SummaryTable_Path + "/tbody/tr[1]/td[10]/input", map_data, "Solicitors PI", 0, "SP_", "'SP_PICommAdjustment", "Comm Adjust", "Input"),"Unable to enter Comm Adjust value in summary table");
					global_SOI_CommAdjust = Double.parseDouble((String)map_data.get("SP_PICommAdjustment"));
					
				// Enter data in PR Table :
					customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(noOfPR-1)+"]/td[2]/input", data_Structure_of_InnerPagesMaps, "Period Rating Table", (noOfPR-3), "PRT_", "ExcessAmount", "ExcessAmount", "Input"),"Unable to enter ExcessAmount in Period rating table");
					customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(noOfPR-1)+"]/td[3]/input", data_Structure_of_InnerPagesMaps, "Period Rating Table", (noOfPR-3), "PRT_", "TechAdjust", "TechAdjust", "Input"),"Unable to enter TechAdjust in Period rating table");
				
				k.Click("SPI_ApplyBookRate");
				
				//Duration :
					duration_SoPI = Integer.parseInt(k.getText("SPI_SP_Duration"));
				
				// Read reflected values from Premium summary screen :
				
					s_SOI_PenComm = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(noOfPR-1)+"]/td[7]/input").replaceAll(",", ""));
					s_SOI_BrokerComm = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(noOfPR-1)+"]/td[8]/input").replaceAll(",", ""));
					s_SOI_InsTaxR = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(noOfPR-1)+"]/td[10]/input").replaceAll(",", ""));
					
				
		}
			
		
		// Write data to excel :
		
		
		TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+common.currentRunningFlow, "Period Rating Table", data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_AnnualizedPremium", String.valueOf(s_SOI_AnnualP),data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+common.currentRunningFlow, "Period Rating Table", data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_NetNetPremium", String.valueOf(s_SOI_NetNetP),data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		
		if(sCase.equals("")){
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+common.currentRunningFlow, "Period Rating Table", data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_PenCommission", String.valueOf(Refer_PenComm),data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+common.currentRunningFlow, "Period Rating Table", data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_BrokerCommission", String.valueOf(Refer_BrokerComm),data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+common.currentRunningFlow, "Period Rating Table", data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_InsuranceTax", String.valueOf(Refer_InsTax),data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		}else{
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+common.currentRunningFlow, "Period Rating Table", data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_PenCommission", String.valueOf(s_SOI_PenComm),data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+common.currentRunningFlow, "Period Rating Table", data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_BrokerCommission", String.valueOf(s_SOI_BrokerComm),data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+common.currentRunningFlow, "Period Rating Table", data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_InsuranceTax", String.valueOf(s_SOI_InsTaxR),data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		}
		
		TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+common.currentRunningFlow, "Period Rating Table", data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_GrossPremium", String.valueOf(s_SOI_Grossp),data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+common.currentRunningFlow, "Period Rating Table", data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_TotalPremium", String.valueOf(s_SOI_TotalP),data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_PI_NetNetPremium", String.valueOf(s_SOI_AnnualP), map_data);	
		
		return retVal;
	
	}catch(Throwable t){
		System.out.println("Error in coverSPI_PeriodRatingTable - "+t);
		return false;
	}
	
	
}

public boolean SPI_RenewalEndorse_PeriodRatingTable( Map<Object, Object> map_data, String sCase){
	boolean retVal = true;
	double s_SOI_BookRate = 0.00, s_SOI_InitialP = 0.00, s_SOI_BookP = 0.00, s_SOI_TechA = 0.00, s_SOI_RevisedP = 0.00, s_SOI_AnnualP = 0.00;
	double s_SOI_NetNetP = 0.00, s_SOI_Grossp = 0.00, s_SOI_TotalP = 0.00, s_SOI_IndLimit = 0.00;
	
	double s_SOI_PenComm = 0.00, s_SOI_BrokerComm = 0.00, s_SOI_InsTaxR = 0.00;
	double s_Val_PenAmt = 0.00, s_Val_BrokerAmt = 0.00, s_Val = 0.00;
	int xlWRite_Index = 0;
	
	String SummaryTable_UniqueCol, SummaryTable_Path;
	
	String sPeriodMonths;
	
	Map<String, List<Map<String, String>>> data_Structure_of_InnerPagesMaps = null;
	
	data_Structure_of_InnerPagesMaps = common.Renewal_Structure_of_InnerPagesMaps;
	
	try{
		
				
		//Identify Period Rating Table :
		
			String sUniqueCol ="OPTION";
			int tableId = 0;
			
			String sTablePath = "html/body/div[3]/form/div/table";
			
			tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
			
			sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
			WebElement table= driver.findElement(By.xpath(sTablePath));
			
			global_PreviousPremium = Double.parseDouble(driver.findElement(By.xpath("html/body/div[3]/form/div/table["+ tableId +"]/tbody/tr[1]/td[5]/input")).getAttribute("value").replaceAll(",", ""));
			
			String sPeriodRatings = (String)map_data.get("SP_PeriodRatingTable");
			String sValue[] = sPeriodRatings.split(";");
			int noOfPR = sValue.length;
			
		// Identify Summary Table  :
			
			SummaryTable_UniqueCol ="Description";		
			tableId = k.getTableIndex(SummaryTable_UniqueCol,"xpath","html/body/div[3]/form/div/table");		
			SummaryTable_Path = "html/body/div[3]/form/div/table["+ tableId +"]";
			WebElement Summary_table= driver.findElement(By.xpath(sTablePath));			
			
		// Enter Data in Period Rating Table :
				
			customAssert.assertTrue(k.DynamicXpathWebDriver(driver, SummaryTable_Path + "/tbody/tr[1]/td[10]/input", map_data, "Solicitors PI", 0, "SP_", "RenewalPICommAdjustment", "Comm Adjust", "Input"),"Unable to enter Comm Adjust value in summary table for renewal");
			global_SOI_CommAdjust = Double.parseDouble((String)map_data.get("SP_RenewalPICommAdjustment"));
		
		// Read Values of Period Rating Table from screen and calculations :
					
			k.Click("SPI_ApplyBookRate");
			global_SOI_sPremium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[12]/input").replaceAll(",", ""));
			
		// Write data to excel :
		
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_PI_NetNetPremium", String.valueOf(global_SOI_sPremium), map_data);	
		
		return retVal;
	
	}catch(Throwable t){
		System.out.println("Error in coverSPI_PeriodRatingTable - "+t);
		return false;
	}
	
	
}


public boolean func_Solicitors_ExcessLayer( Map<Object, Object> map_data){
	boolean retVal = true;
	double 	s_SEL_GrossP = 0.00, s_SEL_PenComm = 0.00, s_SEL_BrokerComm = 0.00, s_SEL_BookP = 0.00, s_SEL_Premium = 0.00, s_SEL_TotalP = 0.00;
	
	try{
		customAssert.assertTrue(common.funcPageNavigation("Solicitors Excess Layer", ""), "Navigation problem to Solicitors Excess Layer page .");
		
		// Enter Mandatory Values : 
		
		customAssert.assertTrue(k.Input("SPI_SEL_UICompany", (String)map_data.get("SEL_UICompany")),"Unable to enter value in UICompany field .");
		customAssert.assertTrue(k.Input("SPI_SEL_UILimitxpath", (String)map_data.get("SEL_UILimit")),	"Unable to enter value in UILimit field .");
		customAssert.assertTrue(k.Input("SPI_SEL_UIPolicyNumbe", (String)map_data.get("SEL_UIPolicyNumbe")),	"Unable to enter value in SEL_PolicyNumbe field .");
		
		customAssert.assertTrue(k.Input("PIA_SEL_BrokerComm", (String)map_data.get("SEL_BrokerComm")),	"Unable to enter value in BrokerComm field .");
		customAssert.assertTrue(k.Input("PIA_SEL_PenComm", (String)map_data.get("SEL_PenComm")),	"Unable to enter value in PenComm field .");
		customAssert.assertTrue(k.Input("SPI_SEL_GrossPremium", (String)map_data.get("SEL_GrossPremium")),	"Unable to enter value in GrossPremium field .");
		customAssert.assertTrue(k.Input("SPI_SEL_AdminFeeSEL", (String)map_data.get("SEL_AdminFeeSEL")),	"Unable to enter value in AdminFeeSEL field .");
		
		k.Click("SPI_Save_SOI");
		k.Click("SPI_ApplyBookRate");
				
		// Identify Table :
		
		String sUniqueCol ="Description";
		int tableId = 0;
		
		String sTablePath = "html/body/div[3]/form/div/table";
		
		tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
		
		sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
		WebElement table= driver.findElement(By.xpath(sTablePath));
		
		// Calculation :
		
		global_SEL_GrossP =  Double.parseDouble((String)map_data.get("SEL_GrossPremium"));
		global_SEL_PenComm = Double.parseDouble((String)map_data.get("SEL_PenComm"));
		global_SEL_BrokerComm = Double.parseDouble((String)map_data.get("SEL_BrokerComm"));
		global_SEL_GrossComm = (global_SEL_PenComm + global_SEL_BrokerComm);
		global_SEL_BookP = (global_SEL_GrossP *(1 - (global_SEL_GrossComm/100)))/((1 - (global_SEL_GrossComm/100)) + (global_SEL_PenComm/100) + (global_SEL_BrokerComm/100));
		
		// Read Values from screen :
		
		global_SEL_AdminFees =  Double.parseDouble(k.getAttribute("SPI_SEL_AdminFeeSEL", "value"));
				
		s_SEL_GrossP =  Double.parseDouble(k.getAttribute("SPI_SEL_GrossPremium", "value").replaceAll(",",""));
		s_SEL_PenComm =  Double.parseDouble(k.getAttribute("PIA_SEL_PenComm", "value").replaceAll(",",""));
		s_SEL_BrokerComm =  Double.parseDouble(k.getAttribute("PIA_SEL_BrokerComm", "value").replaceAll(",",""));
		
		s_SEL_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[1]/td[2]/input").replaceAll(",", ""));
		s_SEL_Premium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[1]/td[3]/input").replaceAll(",", ""));
		s_SEL_TotalP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[2]/td[3]/input").replaceAll(",", ""));
		
		//Compare Values :
		
		CommonFunction.compareValues(global_SEL_GrossP, s_SEL_GrossP,"Gross Premium SEL");
		CommonFunction.compareValues(global_SEL_PenComm, s_SEL_PenComm,"Pen Comm SEL");
		CommonFunction.compareValues(global_SEL_BrokerComm, s_SEL_BrokerComm,"Broker Comm SEL");
		CommonFunction.compareValues(global_SEL_BookP, s_SEL_BookP,"Book Premium SEL");
		
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_SEL_NetNetPremium", String.valueOf(s_SEL_BookP), map_data);
		
		TestUtil.reportStatus("Solicitors Excess Layer Cover details are filled and Verified sucessfully . ", "Info", true);
		
		
		return retVal;
	
	}catch(Throwable t){
		System.out.println("Error in func_Solicitors_ExcessLayer - "+t);
		return false;
	}
	
	
}

public boolean func_Confirm_Policy(){
	
	try{
	customAssert.assertTrue(common.funcButtonSelection("Confirm"), "Unable to click on Confirm button .");
    customAssert.assertTrue(common.funcPageNavigation("Confirm Policy", ""),"Confirm Policy page is having issue(S)");
	customAssert.assertTrue(common.funcButtonSelection("Confirm"), "Unable to click on Confirm button .");
	TestUtil.reportStatus("Entered all the details on Confirm Policy page .", "Info", true);
	
	}catch(Throwable t){
		return false;
	}
	
	return true;
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
							common.transaction_Premium_Values.put(sectionName, transaction_Section_Vals_Total);
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
						common.transaction_Premium_Values.put(sectionName, transaction_Section_Vals);
					
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
						common_PIA.trans_error_val = common_PIA.trans_error_val + transactionPremiumTable_Verification_Total(sectionNames,common.transaction_Premium_Values);
					else
						common_PIA.trans_error_val = common_PIA.trans_error_val + transactionPremiumTable_Verification(policy_Duration,s_Name,common.transaction_Premium_Values);
					
			
				}
				
				 TestUtil.reportStatus("Transaction Premium table has been verified suceesfully . ", "info", true);
				 if(common_PIA.trans_error_val>0){
					 ErrorUtil.addVerificationFailure(new Throwable("Premium mismatches in transaction premium table ."));
				 }
				
			}
			
			if(Integer.parseInt((String)data_map.get("PS_Duration")) != 365){
				//Toal Premium With Admin Fees
				double total_premium_with_admin_fee = common.transaction_Premium_Values.get("Totals").get("Gross Premium") + 
						common.transaction_Premium_Values.get("Totals").get("Insurance Tax") +
						Double.parseDouble((String)data_map.get("PS_TotalAdminFee"));
				
				
				
				String exp_Total_Premium_with_Admin_fee = common.roundedOff(Double.toString(total_premium_with_admin_fee));
				k.waitTwoSeconds();
				
				String xPath = "//table[@id='table0']//*//td[text()='Total']//following-sibling::td";
				String act_Total_Premium_with_Admin_fee = k.getTextByXpath(xPath);
				
				act_Total_Premium_with_Admin_fee = act_Total_Premium_with_Admin_fee.replaceAll(",", "");
				double premium_diff = Double.parseDouble(common.roundedOff(Double.toString(Double.parseDouble(exp_Total_Premium_with_Admin_fee) - Double.parseDouble(act_Total_Premium_with_Admin_fee))));
				
				TestUtil.reportStatus("---------------Total Premium with Admin Fees-----------------","Info",false);
				
				if(Math.abs(premium_diff)<=0.05){
					TestUtil.reportStatus("Total Premium with Admin Fees :[<b> "+exp_Total_Premium_with_Admin_fee+" </b>] matches with actual premium [<b> "+act_Total_Premium_with_Admin_fee+"</b>]as expected with some difference upto '0.05' on premium summary page.", "Pass", false);
					WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_TotalFinalPremium", exp_Total_Premium_with_Admin_fee,data_map);
				}else{
					TestUtil.reportStatus("Mismatch in Expected Total Premium with Admin Fees [<b> "+exp_Total_Premium_with_Admin_fee+"</b>] and Actual Premium [<b> "+act_Total_Premium_with_Admin_fee+"</b>] on premium summary page.", "Fail", false);
					WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_TotalFinalPremium", exp_Total_Premium_with_Admin_fee,data_map);
				}
				}
			
		}catch(Throwable t ){
			return false;
		}
		
		return true;
}


public int transactionPremiumTable_Verification(int policy_Duration,String sectionNames,Map<String,Map<String,Double>> transaction_Premium_Values){

	Map<Object,Object> map_data = common.NB_excel_data_map;
	Map<String,Double> transaction_Section_Vals = new HashMap<>();
	//String testName = (String)map_data.get("Automation Key");
	String code=null;
		
	switch(sectionNames){
	case "Solicitors PI":
		code = "PI";
		break;
	case "Solicitors excess layer":
		code = "SEL";
		break;
	default:
			System.out.println("**Cover Name is not in Scope for SPI**");
		break;
	
	}
	
try{
		
		TestUtil.reportStatus("---------------"+sectionNames+"-----------------","Info",false);
		
		double annual_NetNetP = Double.parseDouble((String)map_data.get("PS_"+code+"_NetNetPremium"));
		String t_NetNetP_expected = common.roundedOff(Double.toString((annual_NetNetP/365)*policy_Duration));
		String t_NetNetP_actual = Double.toString(transaction_Premium_Values.get(sectionNames).get("Net Net Premium"));
		CommonFunction.compareValues(Double.parseDouble(t_NetNetP_expected),Double.parseDouble(t_NetNetP_actual)," Net Net Premium");
		transaction_Section_Vals.put("Net Net Premium", Double.parseDouble(t_NetNetP_expected));
		//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent,"Premium Summary",testName,"PS_"+code+"_PenComm",pc_expected,common.NB_excel_data_map),"Error while writing Pen Commission for cover "+code+" to excel .");
		
		//SPI Transaction Pen commission Calculation : 
		double t_pen_comm = (( Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate"))/100)));
		String t_pc_expected = common.roundedOff(Double.toString(t_pen_comm));
		String t_pc_actual = Double.toString(transaction_Premium_Values.get(sectionNames).get("Pen Comm"));
		CommonFunction.compareValues(Double.parseDouble(t_pc_expected),Double.parseDouble(t_pc_actual)," Pen Commission");
		transaction_Section_Vals.put("Pen Comm", Double.parseDouble(t_pc_expected));
		//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent,"Premium Summary",testName,"PS_"+code+"_PenComm",pc_expected,common.NB_excel_data_map),"Error while writing Pen Commission for cover "+code+" to excel .");
		
		
		//SPI Transaction Net Premium verification : 
		double t_netP = Double.parseDouble(t_pc_expected) + Double.parseDouble(t_NetNetP_expected);
		String t_netP_expected = common.roundedOff(Double.toString(t_netP));
		String t_netP_actual = Double.toString(transaction_Premium_Values.get(sectionNames).get("Net Premium"));
		CommonFunction.compareValues(Double.parseDouble(t_netP_expected),Double.parseDouble(t_netP_actual),"Net Premium");
		transaction_Section_Vals.put("Net Premium", Double.parseDouble(t_netP_expected));
		//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_NetPremium",netP_expected,common.NB_excel_data_map),"Error while writing Net Premium for cover "+code+" to excel .");
		
		
		//SPI Transaction Broker commission Calculation : 
		double t_broker_comm = ((Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate"))/100)));
		String t_bc_expected = common.roundedOff(Double.toString(t_broker_comm));
		String t_bc_actual =  Double.toString(transaction_Premium_Values.get(sectionNames).get("Broker Commission"));
		CommonFunction.compareValues(Double.parseDouble(t_bc_expected),Double.parseDouble(t_bc_actual),"Broker Commission");
		transaction_Section_Vals.put("Broker Commission", Double.parseDouble(t_bc_expected));
		//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_BrokerComm",bc_expected,common.NB_excel_data_map),"Error while writing Broker Commission for cover "+code+" to excel .");
		
		
		//SPI Transaction GrossPremium verification : 
		double t_grossP = Double.parseDouble(t_netP_expected) + Double.parseDouble(t_bc_expected);
		String t_grossP_actual = Double.toString(transaction_Premium_Values.get(sectionNames).get("Gross Premium"));
		CommonFunction.compareValues(t_grossP,Double.parseDouble(t_grossP_actual),sectionNames+" Transaction Gross Premium");
		transaction_Section_Vals.put("Gross Premium", t_grossP);
		//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_GrossPremium",Double.toString(grossP),common.NB_excel_data_map),"Error while writing Gross Premium for cover "+code+" to excel .");
		
		
		double t_InsuranceTax = (t_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_InsuranceTaxRate")))/100.0;
		t_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(t_InsuranceTax)));
		String t_InsuranceTax_actual = Double.toString(transaction_Premium_Values.get(sectionNames).get("Insurance Tax"));
		CommonFunction.compareValues(t_InsuranceTax,Double.parseDouble(t_InsuranceTax_actual),"Insurance Tax");
		transaction_Section_Vals.put("Insurance Tax", t_InsuranceTax);
		//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_InsuranceTax",Double.toString(InsuranceTax),common.NB_excel_data_map),"Error while writing Total Premium for cover "+code+" to excel .");
		
		//SPI  Transaction Total Premium verification : 
		double t_Premium = t_grossP + t_InsuranceTax;
		String t_p_expected = common.roundedOff(Double.toString(t_Premium));
		
		String t_p_actual = Double.toString(transaction_Premium_Values.get(sectionNames).get("Total Premium"));
		transaction_Section_Vals.put("Total Premium", Double.parseDouble(t_p_expected));
		
		common.transaction_Premium_Values.put(sectionNames, transaction_Section_Vals);
		
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

public int transactionPremiumTable_Verification_Total(List<String> sectionNames,Map<String,Map<String,Double>> transaction_Premium_Values){
	
	try{
	
		Map<String,Double> transaction_Section_Vals = new HashMap<>();
		
	TestUtil.reportStatus("---------------Totals-----------------","Info",false);
	double exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Net Net Premium");
	}
	String t_NetNetP_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Net Net Premium"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_NetNetP_actual)," Net Net Premium");
	transaction_Section_Vals.put("Net Net Premium", exp_value);

	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Pen Comm");
	}
	String t_pc_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Pen Comm"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_pc_actual)," Pen Commission");
	transaction_Section_Vals.put("Pen Comm", exp_value);
	
	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Net Premium");
	}
	String t_netP_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Net Premium"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_netP_actual),"Net Premium");
	transaction_Section_Vals.put("Net Premium", exp_value);
	
	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Broker Commission");
	}
	String t_bc_actual =  Double.toString(transaction_Premium_Values.get("Totals").get("Broker Commission"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_bc_actual),"Broker Commission");
	transaction_Section_Vals.put("Broker Commission", exp_value);
	
	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Gross Premium");
	}
	String t_grossP_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Gross Premium"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_grossP_actual)," Gross Premium");
	transaction_Section_Vals.put("Gross Premium", exp_value);
	
	
	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Insurance Tax");
	}
	String t_InsuranceTax_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Insurance Tax"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_InsuranceTax_actual),"Insurance Tax");
	transaction_Section_Vals.put("Insurance Tax", exp_value);
	
	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Premium_Values.get(section).get("Total Premium");
	}
	String t_p_actual = Double.toString(transaction_Premium_Values.get("Totals").get("Total Premium"));
	transaction_Section_Vals.put("Total Premium", exp_value);
	
	common.transaction_Premium_Values.put("Totals", transaction_Section_Vals);
	
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

public boolean funcRewindOperation(){
	
	boolean r_value= true;
	
	try{
			isRewindSelected = "Yes";
			customAssert.assertTrue(performRewindChange(common.NB_excel_data_map), "Error in do Rewind Change function . ");
			customAssert.assertTrue(SPI_Fees_Turnover(common.NB_excel_data_map),"Unable to handle table 'Fees/Turnover' in Rewind ");
			customAssert.assertTrue(calculate_SPI_Book_Premium(), "SPI Book Premium Calculation is function having issue .");
			customAssert.assertTrue(coverSPI_PeriodRatingTable(common.NB_excel_data_map, ""),"Error in Covers SPI PRT table in Rewind . ");
		
			
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(funcPremiumSummary(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent),"Error in Premium Summary function in rewind . ");
			
			customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));

			if(TestBase.product.equals("SPI") ){
				customAssert.assertTrue(k.Click("SPI_RewindOnCoverBtn"),"Error in Clikcing SPI Rewind On Cover Button . ");
			}
			if(TestBase.product.equals("PIA") ){
				try{
					driver.findElement(By.xpath("//*[contains(@id,'pia-rwd-')]")).click();
					//Assert.assertTrue(k.Click("PIA_RewindOnCoverBtn"));
				}catch(Throwable t){
					k.Click("SPI_RewindOnCoverBtn");
				}
				
			}
			customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			if(TestBase.product.equals("SPI") || TestBase.product.equals("PIA")){
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover (Unconfirmed)"), "Verify Policy Status (On Cover (Unconfirmed)) function is having issue(S) . ");
				customAssert.assertTrue(common_PIA.func_Confirm_Policy(), "Error while changing SPI policy Status from Unconfirmed to Confirmed . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
			}else{
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (On Cover) function is having issue(S) . ");
			}
			customAssert.assertTrue(common.funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
		
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
}

public boolean funcMTARewindOperation(){
	
	boolean r_value= true;
	
	try{
			common_PIA.isMTARewindFlow = true;
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Solicitors PI"),"Issue while Navigating to Solicitors PI. ");
			customAssert.assertTrue(func_MTA_Solicitors_PI(common.MTA_excel_data_map), "MTA Solicitors PI function is having issue(S) . ");
		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(func_MTAPremiumSummary(common.MTA_excel_data_map,TestBase.product,TestBase.businessEvent), "Premium Summary function is having issue(S) . ");
		
			customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));

			if(TestBase.product.equals("SPI")){
				customAssert.assertTrue(k.Click("SPI_RewindOnCoverBtn"),"Error in Clikcing SPI Rewind On Cover Button . ");
			}
			customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			if(TestBase.product.equals("SPI")){
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Endorsement On Cover (Unconfirmed)"), "Verify Policy Status (Endorsement On Cover (Unconfirmed)) function is having issue(S) . ");
				customAssert.assertTrue(common_PIA.func_Confirm_Policy(), "Error while changing SPI policy Status from Unconfirmed to Confirmed . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Endorsement On Cover"), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
			}else{
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Endorsement On Cover"), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
			}
		
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
}

public boolean funcRewindCoversCheck(Map<Object, Object> map_data){
   boolean retvalue = true;
    try {
    customAssert.assertTrue(common.funcPageNavigation("Covers", ""),"Cover page is having issue(S)");
     
     String all_cover = ObjectMap.properties.getProperty(CommonFunction.product+"_CD_AllCovers");
 	 String[] split_all_covers = all_cover.split(",");
 	 for(String coverWithLocator : split_all_covers){
 		 String coverWithoutLocator = coverWithLocator.split("_")[0];
 		 try{if(((String)map_data.get("CD_Rewind_"+coverWithoutLocator)).equals("Yes")){
	 			customAssert.assertTrue(common.selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
	 		}else{
	 			continue;
	 		}
 		 }catch(Throwable tt){
 			 System.out.println("Error while selecting Cover - "+coverWithoutLocator);
 			 break;
 			}
 		}
 	 
 	  customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Covers Screen .");
      TestUtil.reportStatus("All specified covers are selected successfully in Rewind   .", "Info", true);
      return retvalue;
           
    } catch(Throwable t) {
    	return false;
 }
}

//This function works for both NB and Renewal Flow

public boolean performRewindChange(Map<Object,Object> data_map){
	
	try{
		
		customAssert.assertTrue(common.funcMenuSelection("Navigate", "Solicitors PI"), "Unable to Navigate to Solicitors PI screen");
		customAssert.assertTrue(common.funcPageNavigation("Solicitors PI", ""), "Navigation problem to Solicitors PI page .");
		// Select 'Rate on' value for Rewind:
		customAssert.assertTrue(k.DropDownSelection("SPI_SP_RateOn", (String)data_map.get("SP_RateOn_Rewind")), "Unable to enter 'Rate on' value in rewind");
		TestUtil.reportStatus("In Rewind, Changed Fees/ Turnover Rate On Value to -"+(String)data_map.get("SP_RateOn_Rewind"), "Info", false);
		
	}catch(Throwable t){return false;}
	return true;
}

//-----SPI MTA------///

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
    
    try {
    	customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page is not loaded to perfrm MTA . ");
   
		customAssert.assertTrue(common.funcButtonSelection("Create Endorsement"), "Unable to click on Create Endorsement button .");
	
    	int ammendmet_period = Integer.parseInt((String)data_map.get("MTA_EndorsementPeriod"));
    	if(ammendmet_period > Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"))){
    		TestUtil.reportStatus("Amendement Period Should not be greater than Policy Duration", "Fail", true);
    		return false;
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
        WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", testName, "MTA_EffectiveDate", k.getAttribute("SPI_Endorsement_eff_date", "value"),data_map);
        customAssert.assertTrue(common.funcButtonSelection("Create Endorsement"), "Unable to click on Create Endorsement button .");
       	
        //Writing to MTA Excel
    	WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", testName, "MTA_PolicyNumber", k.getText("SPI_MTA_policy_number"),data_map);
    	if(common.currentRunningFlow.equals("MTA")){
    		if(((String)data_map.get("MTA_Status")).equals("Endorsement Rewind"))
    			WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", testName, "MTA_isMTARewind", "Y",data_map);
    		else
    			WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "MTA_Endorsement", testName, "MTA_isMTARewind", "N",data_map);
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


public boolean funcCreateEndorsement_Renewal(){
	
    boolean retvalue = true;
    Date dateobj = null;
    String testName = null;
    int ammendmet_period = 0;
    
    Map<Object,Object> data_map = null;
    data_map = common.Renewal_excel_data_map;
	
	testName = (String)data_map.get("Automation Key");
    
    try {
    	customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page is not loaded to perfrm MTA . ");   
		customAssert.assertTrue(common.funcButtonSelection("Create Endorsement"), "Unable to click on Create Endorsement button .");	
		ammendmet_period = Integer.parseInt((String)data_map.get("Renewal_MTA_EndorsementPeriod"));
		
		String sDuration = (String)common.Renewal_excel_data_map.get("PS_Duration");
		
    	if(ammendmet_period > Integer.parseInt(sDuration)){
    		TestUtil.reportStatus("Amendement Period Should not be greater than Policy Duration", "Fail", true);
    		return false;
    	}
    	int dateDiff = Integer.parseInt((String)data_map.get("Renewal_MTA_EndorsementPeriod"));
    	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	Calendar c = Calendar.getInstance();   
    	Date d_date = df.parse((String)data_map.get("PS_PolicyStartDate"));
    	d_date = common.addDays(d_date, - (dateDiff+1));
    	
    	Date Endorse_date = common.addDays(d_date, dateDiff);
    	Endorsement_date = df.format(Endorse_date);
    	//dateobj = df.parse(df.format(c.getTime()));
	
        customAssert.assertTrue(k.Click("SPI_Endorsement_eff_date"), "Unable to enter Endorsement effective Date.");
        customAssert.assertTrue(k.Type("SPI_Endorsement_eff_date", df.format(Endorse_date)), "Unable to Enter Endorsement effective Date .");
        customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
        customAssert.assertTrue(k.Input("SPI_Reson_for_Endors", (String)data_map.get("Renewal_MTA_Reason_for_Endorsement")),"Unable to Enter Reason for Endorsement");
        customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Renewal_Endorsement", testName, "Renewal_MTA_EffectiveDate", k.getAttribute("SPI_Endorsement_eff_date", "value"),data_map),"Error while writing data to excel for field >PS_MTAEffectiveDate<");
        customAssert.assertTrue(common.funcButtonSelection("Create Endorsement"), "Unable to click on Create Endorsement button .");
       	
        //Writing to MTA/Renewal Excel
        customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Renewal_Endorsement", testName, "Renewal_MTA_PolicyNumber", k.getText("SPI_MTA_policy_number"),data_map),"Error while writing data to excel for field >MTA Policy Date<");
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

public boolean func_MTA_Solicitors_PI( Map<Object, Object> map_data){
	
	boolean retVal = true;
	String SummaryTable_UniqueCol, SummaryTable_Path;
	
	try{
		customAssert.assertTrue(common.funcPageNavigation("Solicitors PI", ""), "Navigation problem to Solicitors PI page .");
		
		// Area of Practice Table section :
		
		/*customAssert.assertTrue(SPI_AreaOfPractice(NB_Structure_of_InnerPagesMaps),"Unable to handle table 'Area of practice'");
		
		// Select Work split to value :
		customAssert.assertTrue(k.DropDownSelection("SPI_SP_WorkSplittoUse", (String)map_data.get("SP_WorkSplittoUse")), "Unable to enter 'Work Split to use' value");

		// fees/Turnover Table section :
		
		customAssert.assertTrue(SPI_Fees_Turnover(map_data),"Unable to handle table 'Fees/Turnover'");
		
		// Select 'Rate on' value :
		customAssert.assertTrue(k.DropDownSelection("SPI_SP_RateOn", (String)common.NB_excel_data_map.get("SP_RateOn")), "Unable to enter 'Rate on' value");
		
		// Excess Section :
		customAssert.assertTrue(k.Input("SPI_SP_AdminFeeSP", (String)map_data.get("SP_AdminFeeSP")),	"Unable to enter value in Admin fees field .");
		customAssert.assertTrue(k.DropDownSelection("SPI_SP_Aggegate", (String)common.NB_excel_data_map.get("SP_Aggegate")), "Unable to enter 'Aggregate' value");
		customAssert.assertTrue(calculate_SPI_Book_Premium(), "SPI Book Premium Calculation is function having issue .");
		TestUtil.reportStatus("SPI Rater Lookup values are calculated sucessfully . ", "Info", true);
		burnRate = PIA_Rater_output.get("Burn Rate");
				
		//Policy start and end date :
		Refer_Policy_SDate = k.getText("SPI_SP_PolicyStartDate");
		Refer_Policy_EDate = k.getText("SPI_SP_PolicyEndDate");
		dAdminFee_CoverSPI=  Double.parseDouble((String)map_data.get("SP_AdminFeeSP"));*/
		
		//Period Rating Table Section : 
		customAssert.assertTrue(func_MTA_coverSPI_PeriodRatingTable(map_data, ""),"Unable to handle table 'Fees/Turnover'");
		
		
		
		TestUtil.reportStatus("Solicitors PI Cover details are filled and Verified sucessfully . ", "Info", true);
		
		
		return retVal;
	
	}catch(Throwable t){
		System.out.println("Error in Solicitors_PI function - "+t);
		return false;
	}

	
}

public boolean func_MTA_coverSPI_PeriodRatingTable(Map<Object, Object> map_data, String sCase){
	boolean retVal = true;
	double s_SOI_BookRate = 0.00, s_SOI_InitialP = 0.00, s_SOI_BookP = 0.00, s_SOI_TechA = 0.00, s_SOI_RevisedP = 0.00, s_SOI_AnnualP = 0.00;
	double s_SOI_NetNetP = 0.00, s_SOI_Grossp = 0.00, s_SOI_TotalP = 0.00, s_SOI_IndLimit = 0.00;
	
	double s_SOI_PenComm = 0.00, s_SOI_BrokerComm = 0.00, s_SOI_InsTaxR = 0.00;
	double s_Val_PenAmt = 0.00, s_Val_BrokerAmt = 0.00, s_Val = 0.00;
	int xlWRite_Index = 0;
	
	String SummaryTable_UniqueCol, SummaryTable_Path;
	
	String sPeriodMonths;
	
	try{
		
		//Identify Period Rating Table :
		
			String sUniqueCol ="OPTION";
			int tableId = 0;
			
			String sTablePath = "html/body/div[3]/form/div/table";
			
			tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
			
			sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
			WebElement table= driver.findElement(By.xpath(sTablePath));
			
			String sPeriodRatings = (String)map_data.get("SP_PeriodRatingTable");
			String sValue[] = sPeriodRatings.split(";");
			int noOfPR = sValue.length;
			
		// Identify Summary Table  :
			
			SummaryTable_UniqueCol ="Description";		
			tableId = k.getTableIndex(SummaryTable_UniqueCol,"xpath","html/body/div[3]/form/div/table");		
			SummaryTable_Path = "html/body/div[3]/form/div/table["+ tableId +"]";
			WebElement Summary_table= driver.findElement(By.xpath(sTablePath));			
		
		switch (sCase){
		
			case "" :
				
				/*// Enter Data in Period Rating Table :
				
						if(isRewindSelected.contains("Yes")){
							burnRate = PIA_Rater_output.get("Burn Rate");
							// Do Nothing
						}else{
							for(int i = 0; i < noOfPR ; i++ ){
								
								String sVal = common.NB_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_IsSelected");
														
									//click on 'Add Row' link :
									
									if(i == 0){
										driver.findElement(By.xpath(sTablePath + "/tbody/tr/td[12]/a")).click();
									}else{
										driver.findElement(By.xpath(sTablePath + "/tbody/tr["+ (i+1) +"]/td[12]/a")).click();
									}							
									
									// Enter Data :
									
									customAssert.assertTrue(k.DropDownSelection_DynamicXpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[2]/select", common.NB_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_PeriodofInsurance")),"Unable to enter PeriodofInsurance in Period rating table");
									customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[3]/input", common.NB_Structure_of_InnerPagesMaps, "Period Rating Table", i, "PRT_", "ExcessAmount", "ExcessAmount", "Input"),"Unable to enter ExcessAmount in Period rating table");
									customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[4]/input", common.NB_Structure_of_InnerPagesMaps, "Period Rating Table", i, "PRT_", "TechAdjust", "TechAdjust", "Input"),"Unable to enter TechAdjust in Period rating table");
									
							}
				*/
							// Enter Comm Adjust in Summary Table if required :
							if(common_PIA.isMTARewindFlow){
								customAssert.assertTrue(k.DynamicXpathWebDriver(driver, SummaryTable_Path + "/tbody/tr[1]/td[10]/input", map_data, "Solicitors PI", 0, "SP_", "MTARewindPICommAdjustment", "Comm Adjust", "Input"),"Unable to enter Comm Adjust value in summary table for MTA Rewind ");
								global_SOI_CommAdjust = Double.parseDouble((String)map_data.get("SP_MTARewindPICommAdjustment"));
							}else{
								customAssert.assertTrue(k.DynamicXpathWebDriver(driver, SummaryTable_Path + "/tbody/tr[1]/td[10]/input", map_data, "Solicitors PI", 0, "SP_", "PICommAdjustment", "Comm Adjust", "Input"),"Unable to enter Comm Adjust value in summary table for MTA ");
								global_SOI_CommAdjust = Double.parseDouble((String)map_data.get("SP_PICommAdjustment"));
							}
						//}
							
					// Read Values of Period Rating Table from screen and calculations :
					
						for(int i = 0; i < noOfPR ; i++ ){	
																					
							if(common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_IsSelected").contains("Yes")){
								
								xlWRite_Index = i ;
								
								if(i>0){
										driver.findElement(By.xpath(sTablePath + "/tbody/tr["+ (i+1) +"]/td[1]/input[2]")).click();
								}								
								
								k.Click("SPI_ApplyBookRate");
								
								
								//Duration :
									duration_SoPI = Integer.parseInt(k.getText("SPI_SP_Duration"));
								
								//Read Values from Screen -  Summary Table :
								
									global_PeriodMonths = Integer.parseInt(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[2]"));
									global_FeesIncome = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[3]").replaceAll(",", ""));
									global_IndemnityLimit = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[4]").replaceAll(",", ""));
									global_SOI_BookRate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[5]/input").replaceAll(",", ""));
									global_SOI_InitialP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[6]").replaceAll(",", "")));
									gloabal_SOI_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[7]/input").replaceAll(",", ""));
									global_SOI_TecgAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[8]/input").replaceAll(",", ""));
									global_SOI_RevisedP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[9]/input").replaceAll(",", ""));
									global_SOI_sPremium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[12]/input").replaceAll(",", ""));							
								
								// Read values from Period Rating Table :
								
									global_SOI_AnnualP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[5]/input").replaceAll(",", "")));
									global_SOI_NetNetP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[6]/input").replaceAll(",", "")));
									global_SOI_GrossP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[9]/input").replaceAll(",", "")));
									global_SOI_TotalPremium = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[11]/input").replaceAll(",", "")));
									
								// Read Default Values Which are referred on Premium Summary Screen :
							
								Refer_PenComm = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[7]/input").replaceAll(",", "")));
								Refer_BrokerComm = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[8]/input").replaceAll(",", "")));
								Refer_InsTax = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[10]/input").replaceAll(",", "")));
							
							//Calculated Values :
							
								sPeriodMonths = common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_PeriodofInsurance");
								
								String sVal = (String)map_data.get("PG_IsYourPracticeLLP");
								if(sVal.contains("Yes")){
									s_SOI_IndLimit = 3000000.00;
								}else{
									s_SOI_IndLimit = 2000000.00;
								}
								
								s_SOI_BookRate = common_PIA.PIA_Rater_output.get("Book_Rate_Cent");
								s_SOI_InitialP = common_PIA.PIA_Rater_output.get("Book_Premium_24_months");
								
								if(sPeriodMonths.contains("12")){
									s_SOI_BookP = common_PIA.PIA_Rater_output.get("Book_Premium_12_months");
								}else if(sPeriodMonths.contains("18")){
									s_SOI_BookP = common_PIA.PIA_Rater_output.get("Book_Premium_18_months");
								}else{
									s_SOI_BookP = common_PIA.PIA_Rater_output.get("Book_Premium_24_months");
								}
								
								s_SOI_TechA = Double.parseDouble(common.roundedOff(common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_TechAdjust")));
								s_SOI_RevisedP = Double.parseDouble(common.roundedOff(Double.toString(((s_SOI_BookP*s_SOI_TechA)/100) + s_SOI_BookP)));
								s_SOI_AnnualP = Double.parseDouble(common.roundedOff(Double.toString(((s_SOI_RevisedP * global_SOI_CommAdjust)/100) + s_SOI_RevisedP)));
								
								//s_SOI_NetNetP = Double.parseDouble(common.roundedOff(Double.toString(((s_SOI_AnnualP/365)*duration_SoPI))));
								double NB_PI_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_PI_NetNetPremium"));
								s_SOI_NetNetP = ((s_SOI_AnnualP - NB_PI_NNP)/Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")))*
										((Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
								
								double grossCommPerc = Refer_PenComm + Refer_BrokerComm;
								
								s_Val_PenAmt = Math.abs(Double.parseDouble(common.roundedOff(Double.toString((s_SOI_NetNetP/((100-grossCommPerc)/100))*(Refer_PenComm/100)))));
								s_Val_BrokerAmt  = Math.abs(Double.parseDouble(common.roundedOff(Double.toString((s_SOI_NetNetP/((100-grossCommPerc)/100))*(Refer_BrokerComm/100)))));
								s_Val  = s_SOI_NetNetP + s_Val_PenAmt;
								
								s_SOI_Grossp = s_Val + s_Val_BrokerAmt;
								s_SOI_TotalP = Double.parseDouble(common.roundedOff(Double.toString(s_SOI_Grossp + ((s_SOI_Grossp*Refer_InsTax)/100))));
								
								double s_BurnRate = Double.parseDouble(common.roundedOff(k.getText("SPI_PI_BurnRate").replaceAll(",", "")));
								
							// Comparisons :
									
								
								CommonFunction.compareValues(burnRate, s_BurnRate,"BurnRate");
								CommonFunction.compareValues(Double.parseDouble(sPeriodMonths), Double.parseDouble(String.valueOf(global_PeriodMonths)),"Period of insurance month ");
								CommonFunction.compareValues(global_SOI_GrossFees, global_FeesIncome,"Fees Income");
								CommonFunction.compareValues(s_SOI_IndLimit, global_IndemnityLimit,"Indemnity Limit");
								CommonFunction.compareValues(s_SOI_BookRate, global_SOI_BookRate,"SOI BookRate");
								CommonFunction.compareValues(s_SOI_InitialP, global_SOI_InitialP,"SOI Initial Premium");
								CommonFunction.compareValues(s_SOI_BookP, gloabal_SOI_BookP,"SOI Book Premium");
								CommonFunction.compareValues(s_SOI_TechA, global_SOI_TecgAdjust,"SOI TechAdjust");
								CommonFunction.compareValues(s_SOI_RevisedP, global_SOI_RevisedP,"SOI Revised Premium");
								CommonFunction.compareValues(s_SOI_AnnualP, global_SOI_sPremium,"SOI Annual Premium From Summary Table");
								CommonFunction.compareValues(s_SOI_AnnualP, global_SOI_AnnualP,"SOI Annual Premium From PR Table");
								CommonFunction.compareValues(s_SOI_NetNetP, global_SOI_NetNetP,"SOI Net Net Premium");
								CommonFunction.compareValues(s_SOI_Grossp, global_SOI_GrossP,"SOI Gross Premium");
								CommonFunction.compareValues(s_SOI_TotalP, global_SOI_TotalPremium,"SOI Total Premium");
						}						
					}
						
					break;
		}
		
		// Write data to excel :
		
		
		TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_AnnualizedPremium", String.valueOf(s_SOI_AnnualP),common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_NetNetPremium", String.valueOf(s_SOI_NetNetP),common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		
		if(sCase.equals("")){
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_PenCommission", String.valueOf(Refer_PenComm),common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_BrokerCommission", String.valueOf(Refer_BrokerComm),common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_InsuranceTax", String.valueOf(Refer_InsTax),common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		}else{
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_PenCommission", String.valueOf(s_SOI_PenComm),common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_BrokerCommission", String.valueOf(s_SOI_BrokerComm),common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_InsuranceTax", String.valueOf(s_SOI_InsTaxR),common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		}
		
		TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_GrossPremium", String.valueOf(s_SOI_Grossp),common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_TotalPremium", String.valueOf(s_SOI_TotalP),common.MTA_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary", (String)common.MTA_excel_data_map.get("Automation Key"), "PS_PI_NetNetPremium", String.valueOf(s_SOI_AnnualP), common.MTA_excel_data_map);	
		
		return retVal;
		
		
	}catch(Throwable t){
		System.out.println("Error in coverSPI_PeriodRatingTable - "+t);
		return false;
	}

}

public boolean func_MTAPremiumSummary(Map<Object, Object> map_data,String code,String event){
	
	boolean r_value= true;
	
	try{
	customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
	
	if(common_PIA.isMTARewindFlow)
		TestUtil.reportStatus("---------------Premium Verification Started After Rewind Endorsement (MTA)-----------------","Info",false);
	else
		TestUtil.reportStatus("---------------Premium Verification Started After Endorsement (MTA)-----------------","Info",false);
	
	customAssert.assertTrue(Verify_premiumSummaryTable_MTA(), "Error while verifying Premium Summary table after MTA .");
	customAssert.assertTrue(func_MTATransactionDetailsPremiumTable(code, event), "Error while verifying Transaction Details table on premium Summary page .");
	Assert.assertTrue(funcTransactionDetailsMessage_MTA());
	
	TestUtil.reportStatus("Premium Summary details are filled and Verified sucessfully after Endorsement (MTA). ", "Info", true);
	
}catch(Throwable t){
	return false;
}

return r_value;
}

public boolean func_Renewal_EndorsementPS(Map<Object, Object> map_data,String code,String event){
	
	boolean r_value= true;
	
	try{
		customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"),"Unable to navigate to Premium Summary screen");
		customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
		TestUtil.reportStatus("---------------Premium Verification Started After Endorsement (Renewal)-----------------","Info",false);
		
		customAssert.assertTrue(Verify_PSTable_Renewal_Endorsement(), "Error while verifying Premium Summary table after Endorsement (Renewal) .");
		customAssert.assertTrue(func_Renewal_Endorsement_TransactionDetailsPremiumTable(code, event), "Error while verifying Transaction Details table on premium Summary page .");
		TestUtil.reportStatus("Premium Summary details are filled and Verified sucessfully after Endorsement (MTA). ", "Info", true);
	
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
}

public boolean Verify_PSTable_Renewal_Endorsement(){
	err_count = 0;
	final String code = TestBase.product;
	final String event = TestBase.businessEvent;
	String testName = (String)common.Renewal_excel_data_map.get("Automation Key");
	
	final Map<String,String> locator_map = new HashMap<>();
	locator_map.put("PenComm", "pencom");
	locator_map.put("NetPremium", "nprem");
	locator_map.put("BrokerComm", "comm");
	locator_map.put("GrossPremium", "gprem");
	locator_map.put("InsuranceTax", "gipt");
	
	double exp_PI_Premium = 0.0,exp_SEC_Premium = 0.0;
	
	try{
		 String PolicyEndDate = k.getAttribute("SPI_SP_PolicyEndDate", "value");
		    
		 dateDiff_Endorse = common.DateDiff(PolicyEndDate, Endorsement_date);
	
		err_count = err_count + func_SPI_PremiumSummaryCalculation("PI","Solicitors PI",locator_map);
		exp_PI_Premium = Double.parseDouble((String)common.Renewal_excel_data_map.get("PS_PI_TotalPremium"));
		//exp_SEC_Premium = Double.parseDouble((String)common.Renewal_excel_data_map.get("PS_SEL_TotalPremium"));
				
	String exp_Total_Premium = common.roundedOff(Double.toString(exp_PI_Premium + exp_SEC_Premium));
	String act_Total_Premium = k.getAttribute("SPI_Total_Premium", "value");
	act_Total_Premium = act_Total_Premium.replaceAll(",", "");
	
	double premium_diff = Double.parseDouble(common.roundedOff(Double.toString(Double.parseDouble(exp_Total_Premium) - Double.parseDouble(act_Total_Premium))));
	
	TestUtil.reportStatus("---------------Total Premium after Endorsement (Renewal)-----------------","Info",false);
	
	if(Math.abs(premium_diff)<=0.07){
		TestUtil.reportStatus("Total Premium :[<b> "+exp_Total_Premium+" </b>] matches with actual premium [<b> "+act_Total_Premium+"</b>]as expected with some difference upto '0.05' on premium summary page after MTA .", "Pass", false);
		WriteDataToXl(code+"_Renewal", "Premium Summary", testName, "PS_TotalPremium", exp_Total_Premium,common.Renewal_excel_data_map);
	}else{
		TestUtil.reportStatus("Mismatch in Expected Premium [<b> "+exp_Total_Premium+"</b>] and Actual Premium [<b> "+act_Total_Premium+"</b>] on premium summary page.", "Fail", false);
		WriteDataToXl(code+"_"+common_PIA.currentFlow, "Premium Summary", testName, "PS_TotalPremium", exp_Total_Premium,common.Renewal_excel_data_map);
	}
		
	customAssert.assertTrue(func_SPI_Write_Total_PremiumSummary_Values(),"Error while writing Total Premium Summary values to excel . ");
	
	
	
	}catch(Throwable t){
		
		return false;
		
	}
	
	return true;
}



public boolean Verify_premiumSummaryTable_MTA(){
	err_count = 0;
	final String code = TestBase.product;
	final String event = TestBase.businessEvent;
	String testName = (String)common.MTA_excel_data_map.get("Automation Key");
	
	final Map<String,String> locator_map = new HashMap<>();
	locator_map.put("PenComm", "pencom");
	locator_map.put("NetPremium", "nprem");
	locator_map.put("BrokerComm", "comm");
	locator_map.put("GrossPremium", "gprem");
	locator_map.put("InsuranceTax", "gipt");
	
	double exp_PI_Premium = 0.0,exp_SEC_Premium = 0.0;
	
	try{
		
	switch((String)common.MTA_excel_data_map.get("CD_MTAAddRemoveCover")){
	
		case "Add":
			
			if(((String)common.MTA_excel_data_map.get("CD_MTA_SolicitorsPI")).equals("Yes")){
				err_count = err_count + func_SPI_PremiumSummaryCalculation("PI","Solicitors PI",locator_map);
				exp_PI_Premium = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_PI_TotalPremium"));
				exp_SEC_Premium = Double.parseDouble((String)common.NB_excel_data_map.get("PS_SEL_TotalPremium"));
			}
			if(((String)common.MTA_excel_data_map.get("CD_MTA_SolicitorsExcessLayer")).equals("Yes")){
				err_count = err_count + func_SPI_PremiumSummaryCalculation("SEL","Solicitors excess layer",locator_map);
				exp_SEC_Premium = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_SEL_TotalPremium"));
				exp_PI_Premium = Double.parseDouble((String)common.NB_excel_data_map.get("PS_PI_TotalPremium"));
			}
			
			break;
		case "Remove":
			
			if(((String)common.MTA_excel_data_map.get("CD_MTA_SolicitorsPI")).equals("No")){
				err_count = err_count + func_SPI_PremiumSummaryCalculation("PI","Solicitors PI",locator_map);
				exp_PI_Premium = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_PI_TotalPremium"));
			}
			if(((String)common.MTA_excel_data_map.get("CD_MTA_SolicitorsExcessLayer")).equals("No")){
				err_count = err_count + func_SPI_PremiumSummaryCalculation("SEL","Solicitors excess layer",locator_map);
				exp_SEC_Premium = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_SEL_TotalPremium"));
			}
			
			break;
		case "Increase":
			
			if(((String)common.NB_excel_data_map.get("CD_SolicitorsPI")).equals("Yes")){
				err_count = err_count + func_SPI_PremiumSummaryCalculation("PI","Solicitors PI",locator_map);
				exp_PI_Premium = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_PI_TotalPremium"));
			}
			if(((String)common.NB_excel_data_map.get("CD_SolicitorsExcessLayer")).equals("Yes")){
				err_count = err_count + func_SPI_PremiumSummaryCalculation("SEL","Solicitors excess layer",locator_map);
				exp_SEC_Premium = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_SEL_TotalPremium"));
			}
			
			break;
	
	
	}	
	
	
	String exp_Total_Premium = common.roundedOff(Double.toString(exp_PI_Premium + exp_SEC_Premium));
	String act_Total_Premium = k.getAttribute("SPI_Total_Premium", "value");
	act_Total_Premium = act_Total_Premium.replaceAll(",", "");
	
	double premium_diff = Double.parseDouble(common.roundedOff(Double.toString(Double.parseDouble(exp_Total_Premium) - Double.parseDouble(act_Total_Premium))));
	
	TestUtil.reportStatus("---------------Total Premium after Endorsement-----------------","Info",false);
	
	if(Math.abs(premium_diff)<=0.07){
		TestUtil.reportStatus("Total Premium :[<b> "+exp_Total_Premium+" </b>] matches with actual premium [<b> "+act_Total_Premium+"</b>]as expected with some difference upto '0.05' on premium summary page after MTA .", "Pass", false);
		customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_TotalPremium", exp_Total_Premium,common.MTA_excel_data_map),"Error while writing Total Premium data to excel .");
	}else{
		TestUtil.reportStatus("Mismatch in Expected Premium [<b> "+exp_Total_Premium+"</b>] and Actual Premium [<b> "+act_Total_Premium+"</b>] on premium summary page.", "Fail", false);
		customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_TotalPremium", exp_Total_Premium,common.MTA_excel_data_map),"Error while writing Total Premium data to excel .");
	}
		
	customAssert.assertTrue(func_SPI_Write_Total_PremiumSummary_Values(),"Error while writing Total Premium Summary values to excel . ");
	
	
	
	}catch(Throwable t){
		
		return false;
		
	}
	
	return true;
}

public boolean func_Renewal_Endorsement_TransactionDetailsPremiumTable(String code, String event){
	//Transaction Premium Table
	
		try{
			String testName = (String)common.Renewal_excel_data_map.get("Automation Key");
			k.pressDownKeyonPage();
			customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
		
			int policy_Duration = Integer.parseInt((String)common.Renewal_excel_data_map.get("PS_Duration"));
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
			
			if(transactionDetails_Table.isDisplayed()){
				
				TestUtil.reportStatus("Verification Started for Transaction Details table on premium summary page for endorsement after renewal  . ", "Info", true);
				
				//For Each Cover Row
				for(int row = 1; row < trans_tble_Rows ;row ++){
					
					WebElement sec_Name = driver.findElement(By.xpath(transactionDetailsTble_xpath+"//tbody//tr["+row+"]//td["+1+"]"));
					sectionName = sec_Name.getText();
					
					switch(sectionName){
					
					case "Totals":
						Map<String,Double> transaction_Section_Vals_Total_Renwal = new HashMap<>();
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
								transaction_Section_Vals_Total_Renwal.put(headerName, Double.parseDouble(sectionValue));
								
							}else{
								continue;
							}
							common.transaction_Details_Premium_Values_EndorsemntRenewal.put(sectionName, transaction_Section_Vals_Total_Renwal);
					}
					
					break;
					
					default:
						Map<String,Double> transaction_Section_Vals_Renewal = new HashMap<>();
						//For Each Cols
						for(int col = 2; col <= trans_tble_Cols ;col ++){
					
							////p[text()=' Transaction Premium']//following-sibling::table[@id='table0']//thead//th[2]
							WebElement header_Name = driver.findElement(By.xpath(transactionDetailsTble_xpath+"//thead//th["+col+"]"));
							headerName = header_Name.getText();
																			
							WebElement sec_Val = driver.findElement(By.xpath(transactionDetailsTble_xpath+"//tbody//tr["+row+"]//td["+col+"]"));
							sectionValue = sec_Val.getText();
						
							transaction_Section_Vals_Renewal.put(headerName, Double.parseDouble(sectionValue));
					}
						common.transaction_Details_Premium_Values_EndorsemntRenewal.put(sectionName, transaction_Section_Vals_Renewal);
					
					break;
					
					}
					
				}
				//System.out.println(transaction_Premium_Values);
				
				TestUtil.reportStatus("---------------Transaction Details table Verification or Endorsement after renewal-----------------","Info",false);
			
				//Transaction table Verification
				for(int row = 1; row < trans_tble_Rows ;row ++){
					WebElement sec_Name = driver.findElement(By.xpath(transactionDetailsTble_xpath+"//tbody//tr["+row+"]//td["+1+"]"));
					sectionNames.add(sec_Name.getText());
				}
				for(String s_Name : sectionNames){
					if(s_Name.equals("Totals"))
						trans_error_val = trans_error_val + funcTransactionDetailsTable_Verification_Total_RenewalEndorsement(sectionNames,common.transaction_Details_Premium_Values_EndorsemntRenewal);
					else
						trans_error_val = trans_error_val + funcTransactionDetailsTable_Verification_RenewalEndorse(policy_Duration,s_Name,common.transaction_Details_Premium_Values_EndorsemntRenewal);				
			
				}
					TestUtil.reportStatus("Transaction Details table has been verified suceesfully after Renewal Endorsement . ", "info", true);			
				
			}
			
		
				//Total Premium With Admin Fees
				double total_premium_with_admin_fee = common.transaction_Details_Premium_Values_EndorsemntRenewal.get("Totals").get("Gross Premium") + 
						common.transaction_Details_Premium_Values_EndorsemntRenewal.get("Totals").get("Insurance Tax") +
						Double.parseDouble((String)common.Renewal_excel_data_map.get("PS_TotalAdminFee"));
				
				
				String exp_Total_Premium_with_Admin_fee = common.roundedOff(Double.toString(total_premium_with_admin_fee));
				k.waitTwoSeconds();
				
				String xPath = "//table[@id='table0']//*//td[text()='Total']//following-sibling::td";
				String act_Total_Premium_with_Admin_fee = k.getTextByXpath(xPath);
				
				act_Total_Premium_with_Admin_fee = act_Total_Premium_with_Admin_fee.replaceAll(",", "");
				double premium_diff = Double.parseDouble(common.roundedOff(Double.toString(Double.parseDouble(exp_Total_Premium_with_Admin_fee) - Double.parseDouble(act_Total_Premium_with_Admin_fee))));
				
				TestUtil.reportStatus("---------------Endorsement Premium Summary with Admin Fees-----------------","Info",false);
				
				if(Math.abs(premium_diff)<=0.09){
					TestUtil.reportStatus("Total Premium with Admin Fees :[<b> "+exp_Total_Premium_with_Admin_fee+" </b>] matches with actual premium [<b> "+act_Total_Premium_with_Admin_fee+"</b>]as expected with some difference upto '0.05' on premium summary page.", "Pass", false);
					customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_TotalFinalPremium", exp_Total_Premium_with_Admin_fee,common.Renewal_excel_data_map),"Error while writing Total Final Premium data to excel .");
				}else{
					TestUtil.reportStatus("Mismatch in Expected Total Premium with Admin Fees [<b> "+exp_Total_Premium_with_Admin_fee+"</b>] and Actual Premium [<b> "+act_Total_Premium_with_Admin_fee+"</b>] on premium summary page.", "Fail", false);
					customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_TotalFinalPremium", exp_Total_Premium_with_Admin_fee,common.Renewal_excel_data_map),"Error while writing Total Final Premium data to excel .");
				}
			
				// write new values of premium to the PremiumSummary Sheet which will b used n Transaction summary page :
				
				// Values for SI :
				if(sectionNames.contains("Solicitors PI")){
					customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_PI_NetNetPremium", String.valueOf(common.transaction_Details_Premium_Values_EndorsemntRenewal.get("Solicitors PI").get("Net Net Premium")),common.Renewal_excel_data_map),"Error while writing Total Final Premium data to excel .");
					customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_PI_InsuranceTax", String.valueOf(common.transaction_Details_Premium_Values_EndorsemntRenewal.get("Solicitors PI").get("Insurance Tax")),common.Renewal_excel_data_map),"Error while writing Total Final Premium data to excel .");
				}
				if(!sectionNames.contains("Solicitors excess layer")){
					customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_SEL_NetNetPremium", "0.00", common.Renewal_excel_data_map),"Error while writing Total Final Premium data to excel .");
					customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_SEL_InsuranceTax", "0.00",common.Renewal_excel_data_map),"Error while writing Total Final Premium data to excel .");
				}
				
				
				
		}catch(Throwable t ){
			return false;
		}
		
		return true;
}


public boolean func_MTATransactionDetailsPremiumTable(String code, String event){
	//Transaction Premium Table
	
		try{
			String testName = (String)common.MTA_excel_data_map.get("Automation Key");
			k.pressDownKeyonPage();
			customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
			
			int policy_Duration = Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"));
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
			
			if(common_PIA.isMTARewindFlow){
				common.transaction_Details_Premium_Values.clear();
			}
			
			if(transactionDetails_Table.isDisplayed()){
				
				if(common_PIA.isMTARewindFlow)
					TestUtil.reportStatus("Verification Started for Transaction Details table on premium summary page after Endorsement(MTA) Rewind . ", "Info", true);
				else
					TestUtil.reportStatus("Verification Started for Transaction Details table on premium summary page  . ", "Info", true);
				//For Each Cover Row
				for(int row = 1; row < trans_tble_Rows ;row ++){
					
					WebElement sec_Name = driver.findElement(By.xpath(transactionDetailsTble_xpath+"//tbody//tr["+row+"]//td["+1+"]"));
					sectionName = sec_Name.getText();
					
					switch(sectionName){
					
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
					
					}
					
				}
				//System.out.println(transaction_Premium_Values);
				if(common_PIA.isMTARewindFlow){
					TestUtil.reportStatus("---------------Transaction Details table Verification after Rewind Endorsement(MTA)-----------------","Info",false);
				}else{
					TestUtil.reportStatus("---------------Transaction Details table Verification-----------------","Info",false);
				}
				//Transaction table Verification
				for(int row = 1; row < trans_tble_Rows ;row ++){
					WebElement sec_Name = driver.findElement(By.xpath(transactionDetailsTble_xpath+"//tbody//tr["+row+"]//td["+1+"]"));
					sectionNames.add(sec_Name.getText());
				}
				for(String s_Name : sectionNames){
					if(s_Name.equals("Totals"))
						trans_error_val = trans_error_val + funcTransactionDetailsTable_Verification_Total_MTA(sectionNames,common.transaction_Details_Premium_Values);
					else
						trans_error_val = trans_error_val + funcTransactionDetailsTable_Verification_MTA(policy_Duration,s_Name,common.transaction_Details_Premium_Values);
					
			
				}
				if(common_PIA.isMTARewindFlow){
					TestUtil.reportStatus("Transaction Details table has been verified suceesfully after Rewind Endorsement . ", "info", true);
				}else{
					TestUtil.reportStatus("Transaction Details table has been verified suceesfully . ", "info", true);
				}
				
			}
			
		
				//Total Premium With Admin Fees
				double total_premium_with_admin_fee = common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium") + 
						common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax") +
						Double.parseDouble((String)common.MTA_excel_data_map.get("PS_TotalAdminFee"));
				
				
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

public int funcTransactionDetailsTable_Verification_MTA(int policy_Duration,String sectionName,Map<String,Map<String,Double>> transactionDetails_Premium_Values){

	Map<Object,Object> map_data = common.MTA_excel_data_map;
	Map<Object,Object> NB_map_data = common.NB_excel_data_map;
	//String testName = (String)map_data.get("Automation Key");
	double NB_PI_NNP = 0.0;
	double NB_SEL_NNP = 0.0;
	double trans_NetNetP = 0.0;
	String code=null;
	int p_Duration = 0;
	if(Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"))!=365)
		p_Duration = 365;
	else
		p_Duration = Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"));
		
	switch(sectionName){
	case "Solicitors PI":
		code = "PI";
		break;
	case "Solicitors excess layer":
		code = "SEL";
		break;
	default:
			System.out.println("**Cover Name is not in Scope for SPI**");
		break;
	
	}
	
try{
		
		TestUtil.reportStatus("---------------"+sectionName+"-----------------","Info",false);
		
		switch((String)common.MTA_excel_data_map.get("CD_MTAAddRemoveCover")){
		
			case "Add":
				
				if(sectionName.contains("PI")){
					NB_PI_NNP = 0.0;
					trans_NetNetP = ((Double.parseDouble((String)common.MTA_excel_data_map.get("PS_PI_NetNetPremium")))/p_Duration)*
							((Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
					
				}else{
					NB_SEL_NNP = 0.0;
					trans_NetNetP = ((Double.parseDouble((String)common.MTA_excel_data_map.get("PS_SEL_NetNetPremium")))/p_Duration)*
							((Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
					
				}
				
				break;
			case "Remove":
				
				if(sectionName.contains("PI")){
					NB_PI_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_PI_NetNetPremium"));
					trans_NetNetP = ((NB_PI_NNP)/p_Duration)*
							((Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
					
				}else{
					NB_SEL_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_SEL_NetNetPremium"));
					trans_NetNetP = ((NB_SEL_NNP)/p_Duration)*
							((Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
					
				}
				trans_NetNetP = -trans_NetNetP;
				break;
			case "Increase":
				
				if(sectionName.contains("PI")){
					NB_PI_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_PI_NetNetPremium"));
					trans_NetNetP = ((Double.parseDouble((String)common.MTA_excel_data_map.get("PS_PI_NetNetPremium")) - NB_PI_NNP)/p_Duration)*
							((Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
					
				}else{
					NB_SEL_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_SEL_NetNetPremium"));
					trans_NetNetP = ((Double.parseDouble((String)common.MTA_excel_data_map.get("PS_SEL_NetNetPremium")) - NB_SEL_NNP)/p_Duration)*
							((Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
					
				}
				
				break;
		
		
		}
		
		
		//double trans_NetNetP = ((Double.parseDouble((String)common.MTA_excel_data_map.get("PS_PI_NetNetPremium")) - NB_PI_NNP)/Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")))*
				//((Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
		
		//double trans_NetNetP = Double.parseDouble((String)map_data.get("PS_"+code+"_NetNetPremium")) - Double.parseDouble((String)NB_map_data.get("PS_"+code+"_NetNetPremium"));
		String t_NetNetP_expected = common.roundedOff(Double.toString(trans_NetNetP));
		String t_NetNetP_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Net Net Premium"));
		CommonFunction.compareValues(Double.parseDouble(t_NetNetP_expected),Double.parseDouble(t_NetNetP_actual)," Net Net Premium");
		
		double t_pen_comm = (( Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate"))/100)));
		String t_pc_expected = common.roundedOff(Double.toString(t_pen_comm));
		String t_pc_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Pen Comm"));
		CommonFunction.compareValues(Double.parseDouble(t_pc_expected),Double.parseDouble(t_pc_actual)," Pen Commission");
		
		
		double t_netP = Double.parseDouble(t_pc_expected) + Double.parseDouble(t_NetNetP_expected);
		String t_netP_expected = common.roundedOff(Double.toString(t_netP));
		String t_netP_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Net Premium"));
		CommonFunction.compareValues(Double.parseDouble(t_netP_expected),Double.parseDouble(t_netP_actual),"Net Premium");
		
		
		double t_broker_comm = ((Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate"))/100)));
		String t_bc_expected = common.roundedOff(Double.toString(t_broker_comm));
		String t_bc_actual =  Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Broker Commission"));
		CommonFunction.compareValues(Double.parseDouble(t_bc_expected),Double.parseDouble(t_bc_actual),"Broker Commission");
		
		
		double t_grossP = Double.parseDouble(t_netP_expected) + Double.parseDouble(t_bc_expected);
		String t_grossP_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Gross Premium"));
		CommonFunction.compareValues(t_grossP,Double.parseDouble(t_grossP_actual)," Gross Premium");
		
		
		double t_InsuranceTax = (t_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_InsuranceTaxRate")))/100.0;
		t_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(t_InsuranceTax)));
		String t_InsuranceTax_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Insurance Tax"));
		CommonFunction.compareValues(t_InsuranceTax,Double.parseDouble(t_InsuranceTax_actual),"Insurance Tax");
		
		//SPI  Transaction Total Premium verification : 
		double t_Premium = t_grossP + t_InsuranceTax;
		String t_p_expected = common.roundedOff(Double.toString(t_Premium));
		
		String t_p_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Total Premium"));
		
		double premium_diff = Double.parseDouble(t_p_expected) - Double.parseDouble(t_p_actual);
		
		if(premium_diff<0.09 && premium_diff>-0.09){
			TestUtil.reportStatus("Total Premium [<b> "+t_p_expected+" </b>] matches with actual total premium [<b> "+t_p_actual+" </b>]as expected for "+sectionName+" in Transaction Details table .", "Pass", false);
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_expected,common.NB_excel_data_map),"Error while writing Total Premium for cover "+code+" to excel .");
			return 0;
		}else{
			TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+t_p_expected+"</b>] and Actual Premium [<b> "+t_p_actual+"</b>] for "+sectionName+" in Transaction Details table . </p>", "Fail", true);
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

public int funcTransactionDetailsTable_Verification_RenewalEndorse(int policy_Duration,String sectionName,Map<String,Map<String,Double>> transaction_Details_Premium_Values_EndorsemntRenewal){

	Map<Object,Object> map_data = common.Renewal_excel_data_map;
	Map<Object,Object> NB_map_data = common.NB_excel_data_map;
	//String testName = (String)map_data.get("Automation Key");
	double NB_PI_NNP = 0.0;
	double NB_SEL_NNP = 0.0;
	double trans_NetNetP = 0.0;
	String code=null;
	int p_Duration = 0;
	if(Integer.parseInt((String)common.Renewal_excel_data_map.get("PS_Duration"))!=365)
		p_Duration = 365;
	else
		p_Duration = Integer.parseInt((String)common.Renewal_excel_data_map.get("PS_Duration"));
		
	switch(sectionName){
	case "Solicitors PI":
		code = "PI";
		break;
	case "Solicitors excess layer":
		code = "SEL";
		break;
	default:
			System.out.println("**Cover Name is not in Scope for SPI**");
		break;
	
	}
	
try{
		
		TestUtil.reportStatus("---------------"+sectionName+"-----------------","Info",false);
		
		NB_PI_NNP = 0.0;
		trans_NetNetP = (((Double.parseDouble((String)common.Renewal_excel_data_map.get("PS_PI_NetNetPremium"))) - (global_PreviousPremium))/365)* Math.abs(Double.parseDouble(dateDiff_Endorse));
			
		String t_NetNetP_expected = common.roundedOff(Double.toString(trans_NetNetP));
		String t_NetNetP_actual = Double.toString(transaction_Details_Premium_Values_EndorsemntRenewal.get(sectionName).get("Net Net Premium"));
		CommonFunction.compareValues(Double.parseDouble(t_NetNetP_expected),Double.parseDouble(t_NetNetP_actual)," Net Net Premium");
		
		double t_pen_comm = (( Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate"))/100)));
		String t_pc_expected = common.roundedOff(Double.toString(t_pen_comm));
		String t_pc_actual = Double.toString(transaction_Details_Premium_Values_EndorsemntRenewal.get(sectionName).get("Pen Comm"));
		CommonFunction.compareValues(Double.parseDouble(t_pc_expected),Double.parseDouble(t_pc_actual)," Pen Commission");
		
		 
		double t_netP = Double.parseDouble(t_pc_expected) + Double.parseDouble(t_NetNetP_expected);
		String t_netP_expected = common.roundedOff(Double.toString(t_netP));
		String t_netP_actual = Double.toString(transaction_Details_Premium_Values_EndorsemntRenewal.get(sectionName).get("Net Premium"));
		CommonFunction.compareValues(Double.parseDouble(t_netP_expected),Double.parseDouble(t_netP_actual),"Net Premium");
		
		
		double t_broker_comm = ((Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate"))/100)));
		String t_bc_expected = common.roundedOff(Double.toString(t_broker_comm));
		String t_bc_actual =  Double.toString(transaction_Details_Premium_Values_EndorsemntRenewal.get(sectionName).get("Broker Commission"));
		CommonFunction.compareValues(Double.parseDouble(t_bc_expected),Double.parseDouble(t_bc_actual),"Broker Commission");
		
		
		double t_grossP = Double.parseDouble(t_netP_expected) + Double.parseDouble(t_bc_expected);
		String t_grossP_actual = Double.toString(transaction_Details_Premium_Values_EndorsemntRenewal.get(sectionName).get("Gross Premium"));
		CommonFunction.compareValues(t_grossP,Double.parseDouble(t_grossP_actual)," Gross Premium");
		
		
		double t_InsuranceTax = (t_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_InsuranceTaxRate")))/100.0;
		t_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(t_InsuranceTax)));
		String t_InsuranceTax_actual = Double.toString(transaction_Details_Premium_Values_EndorsemntRenewal.get(sectionName).get("Insurance Tax"));
		CommonFunction.compareValues(t_InsuranceTax,Double.parseDouble(t_InsuranceTax_actual),"Insurance Tax");
		
		//SPI  Transaction Total Premium verification : 
		double t_Premium = t_grossP + t_InsuranceTax;
		String t_p_expected = common.roundedOff(Double.toString(t_Premium));
		
		String t_p_actual = Double.toString(transaction_Details_Premium_Values_EndorsemntRenewal.get(sectionName).get("Total Premium"));
		
		double premium_diff = Double.parseDouble(t_p_expected) - Double.parseDouble(t_p_actual);
		
		if(premium_diff<0.09 && premium_diff>-0.09){
			TestUtil.reportStatus("Total Premium [<b> "+t_p_expected+" </b>] matches with actual total premium [<b> "+t_p_actual+" </b>]as expected for "+sectionName+" in Transaction Details table .", "Pass", false);
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_expected,common.NB_excel_data_map),"Error while writing Total Premium for cover "+code+" to excel .");
			return 0;
		}else{
			TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+t_p_expected+"</b>] and Actual Premium [<b> "+t_p_actual+"</b>] for "+sectionName+" in Transaction Details table . </p>", "Fail", true);
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

public int funcTransactionDetailsTable_Verification_Total_RenewalEndorsement(List<String> sectionNames,Map<String,Map<String,Double>> transaction_Details_Premium_Values_EndorsemntRenewal){
	
	try{
	
	
	TestUtil.reportStatus("---------------Totals-----------------","Info",false);
	double exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Details_Premium_Values_EndorsemntRenewal.get(section).get("Net Net Premium");
	}
	String t_NetNetP_actual = Double.toString(transaction_Details_Premium_Values_EndorsemntRenewal.get("Totals").get("Net Net Premium"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_NetNetP_actual)," Net Net Premium");

	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Details_Premium_Values_EndorsemntRenewal.get(section).get("Pen Comm");
	}
	String t_pc_actual = Double.toString(transaction_Details_Premium_Values_EndorsemntRenewal.get("Totals").get("Pen Comm"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_pc_actual)," Pen Commission");
	
	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Details_Premium_Values_EndorsemntRenewal.get(section).get("Net Premium");
	}
	String t_netP_actual = Double.toString(transaction_Details_Premium_Values_EndorsemntRenewal.get("Totals").get("Net Premium"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_netP_actual),"Net Premium");
	
	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Details_Premium_Values_EndorsemntRenewal.get(section).get("Broker Commission");
	}
	String t_bc_actual =  Double.toString(transaction_Details_Premium_Values_EndorsemntRenewal.get("Totals").get("Broker Commission"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_bc_actual),"Broker Commission");
	
	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Details_Premium_Values_EndorsemntRenewal.get(section).get("Gross Premium");
	}
	String t_grossP_actual = Double.toString(transaction_Details_Premium_Values_EndorsemntRenewal.get("Totals").get("Gross Premium"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_grossP_actual)," Gross Premium");
	
	
	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Details_Premium_Values_EndorsemntRenewal.get(section).get("Insurance Tax");
	}
	String t_InsuranceTax_actual = Double.toString(transaction_Details_Premium_Values_EndorsemntRenewal.get("Totals").get("Insurance Tax"));
	CommonFunction.compareValues(exp_value,Double.parseDouble(t_InsuranceTax_actual),"Insurance Tax");
	
	exp_value = 0.0;
	for(String section : sectionNames){
		if(!section.contains("Total"))
			exp_value = exp_value + transaction_Details_Premium_Values_EndorsemntRenewal.get(section).get("Total Premium");
	}
	String t_p_actual = Double.toString(transaction_Details_Premium_Values_EndorsemntRenewal.get("Totals").get("Total Premium"));
	double premium_diff = exp_value - Double.parseDouble(t_p_actual);
	
	if(premium_diff<0.05 && premium_diff>-0.05){
		TestUtil.reportStatus("Total Premium [<b> "+exp_value+" </b>] matches with actual total premium [<b> "+t_p_actual+" </b>]as expected for Totals in Transaction Details table .", "Pass", false);
		return 0;
		
	}else{
		TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+exp_value+"</b>] and Actual Premium [<b> "+t_p_actual+"</b>] for Totals in Transaction Details table . </p>", "Fail", true);
		return 1;
	}
	
}catch(Throwable t) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
    Assert.fail("Transaction Premium total Section verification issue.  \n", t);
    return 1;
}
}


public int funcTransactionDetailsTable_Verification_Total_MTA(List<String> sectionNames,Map<String,Map<String,Double>> transaction_Premium_Values){
	
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
		TestUtil.reportStatus("Total Premium [<b> "+exp_value+" </b>] matches with actual total premium [<b> "+t_p_actual+" </b>]as expected for Totals in Transaction Details table .", "Pass", false);
		return 0;
		
	}else{
		TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+exp_value+"</b>] and Actual Premium [<b> "+t_p_actual+"</b>] for Totals in Transaction Details table . </p>", "Fail", true);
		return 1;
	}
	
}catch(Throwable t) {
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
    Assert.fail("Transaction Premium total Section verification issue.  \n", t);
    return 1;
}
}

public boolean funcTransactionDetailsMessage_RenewalEndorse(){
	
	try{
	// Amendment Effective From : 22/06/2017, Period: 355 days.
	String t_Act_Message = null,t_Exp_Message = null;
	int MTA_duration = Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"));
	String Amend_Eff_Date = (String)common.Renewal_excel_data_map.get("Renewal_MTA_EffectiveDate");
	
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

public boolean funcAssignPolicyToUW(){
	
	boolean retvalue=true;
 
    try {
    	   customAssert.assertTrue(common.funcPageNavigation("Assign Underwriter",""), "Unable to navigate through Assign Underwriter Page.");
        
           List<WebElement> assign_Btns = driver.findElements(By.xpath("//a[text()='Assign']"));
           for(WebElement assign_btn:assign_Btns){
        	   k.ScrollInVewWebElement(assign_btn);
        	   if(assign_btn.isDisplayed()){
        		   assign_btn.click();
        		   break;
        	   }else
        		   continue;
           }
          // String tableXpath = "html/body/div[3]/form/div/table/tbody/tr[1]/td[4]/a";
          // driver.findElement(By.xpath(tableXpath)).click(); 
           customAssert.assertTrue(k.getText("Page_Header").equalsIgnoreCase("Premium Summary"), "Premium Summary Page is not loaded after policy assigned to UW . ");
           TestUtil.reportStatus("Policy Assigned to Underwriter successfully", "Info", true);
             
          return retvalue;
                                   
        }catch(Throwable t) {
       String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
         TestUtil.reportFunctionFailed("Failed in "+methodName+" function");     k.reportErr("Failed in "+methodName+" function", t);
         Assert.fail("Unable to Assign Policy to underwriter \n", t);
         return false;
       }
    
}

public boolean funcDecideMTAFlow(){
	
	try{
	String MTA_Add_Remove_Cover = (String)common.MTA_excel_data_map.get("CD_MTAAddRemoveCover");
	switch(MTA_Add_Remove_Cover){
		case "Add":
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(funcMTACoversCheck(common.MTA_excel_data_map), "MTA Add covers function is having issue(S) . ");
			break;
		case "Remove":
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(funcMTACoversUnCheck(common.MTA_excel_data_map), "MTA Remove covers function is having issue(S) . ");
			break;
		default:
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Solicitors PI"),"Issue while Navigating to Solicitors PI. ");
			customAssert.assertTrue(func_MTA_Solicitors_PI(common.MTA_excel_data_map), "MTA Solicitors PI function is having issue(S) . ");
			break;
	}
	
	
	}catch(Throwable t){
		return false;
	}
	return true;
	
}

public boolean funcMTACoversCheck(Map<Object, Object> map_data){
   boolean retvalue = true;
    try {
    	
    customAssert.assertTrue(common.funcPageNavigation("Covers", ""),"Cover page is having issue(S)");
    	
 	 
     String all_cover = ObjectMap.properties.getProperty(CommonFunction.product+"_CD_AllCovers");
 	 String[] split_all_covers = all_cover.split(",");
 	 for(String coverWithLocator : split_all_covers){
 		 String coverWithoutLocator = coverWithLocator.split("_")[0];
 		 try{
 			 if(((String)map_data.get("CD_MTA_"+coverWithoutLocator)).equals("Yes")){
	 			customAssert.assertTrue(common.selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
 			 }
 		 }catch(Throwable tt){
 			 System.out.println("Error while selecting Cover - "+coverWithoutLocator);
 			 break;
 			}
 		}
 	 
 	  customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Covers Screen .");
      TestUtil.reportStatus("All specified covers are selected successfully in MTA  .", "Info", true);
      return retvalue;
           
    } catch(Throwable t) {
    	return false;
 }
}

public boolean funcMTACoversUnCheck(Map<Object, Object> map_data){

boolean retvalue = true;

try {
	
	customAssert.assertTrue(common.funcPageNavigation("Covers", ""),"Cover page is having issue(S)");
	
	 
 String all_cover = ObjectMap.properties.getProperty(CommonFunction.product+"_CD_AllCovers");
	 String[] split_all_covers = all_cover.split(",");
	 for(String coverWithLocator : split_all_covers){
		 String coverWithoutLocator = coverWithLocator.split("_")[0];
		 try{if(((String)map_data.get("CD_MTA_"+coverWithoutLocator)).equals("Yes")){
 			//CoversDetails_data_list.add(coverWithoutLocator);
 			customAssert.assertTrue(common.deSelectCovers(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
 		}else{
 			continue;
 		}
		 }catch(Throwable tt){
			 System.out.println("Error while unchecking Cover - "+coverWithoutLocator);
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

public boolean MaterialFactsDeclerationPage(){
	boolean retValue = true;
	
	try{
		 customAssert.assertTrue(common.funcPageNavigation("Material Facts & Declarations", ""),"Material Facts & Declarations page is having issue(S)");
		 k.ImplicitWaitOff();
		 List<WebElement> elements = driver.findElements(By.className("selectinput"));
		 Select sel = null;
		 String q_value = null;
		 for(int i = 0;i<elements.size();i++){
			 sel = new Select(elements.get(i));
			 try{
				 q_value = (String)common.NB_excel_data_map.get("MF_Q"+(i+1));
				 sel.selectByVisibleText(q_value);}
			 catch(Throwable t){
				 sel.selectByVisibleText("No");
				 //System.out.println("Error while selecting Material Facts questions .");
			 }
			 
		 }
		 customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Material Facts & Declarations Screen .");
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


//------------------  All Cancellation Functions :

public void CancellationFlow(String code,String fileName) throws ErrorInTestMethod{
	
	String testName = (String)common.CAN_excel_data_map.get("Automation Key");
	try{
		
		NewBusinessFlow(code,"NB");
		common_PIA.currentFlow = "CAN";
		common.currentRunningFlow="CAN";
		//System.out.println("Test method of CAN For - "+code);
		
		customAssert.assertTrue(SPI_CancelPolicy(common.CAN_excel_data_map), "Error while Cancelling policy.");
		customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
		
		customAssert.assertTrue(common_PIA.SPI_Cancel_PremiumSummary(common.CAN_excel_data_map), "Issue in verifying Premium summary for cancellation");
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""), "Unable to click on policies tab");
		Assert.assertTrue(common.funcStatusHandling(common.NB_excel_data_map,code,fileName));
		
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		
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

public boolean SPI_CancelPolicy(Map<Object, Object> map_data) throws ErrorInTestMethod{
    boolean retVal = true;
    int dateDif = Integer.parseInt((String)map_data.get("CP_AddDifference"));
    //String Cancellation_date = common.daysIncrementWithOutFormation(df.format(currentDate), dateDif);
    try{
    	
    		//UK time zone change
    		Date c_date = df.parse(common.getUKDate());
    		String Cancellation_date = common.daysIncrementWithOutFormation(df.format(c_date), dateDif);
    
           customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"), "Unable to navigate to Premium Summary screen");
           customAssert.assertTrue(k.Click("SPI_Btn_CancelPolicy"), "Unable to click on Cancel Policy Button");
                        
           customAssert.assertTrue(k.Click("SPI_CP_CancellationDate"), "Unable to enter Cancellation date.");
           customAssert.assertTrue(k.Input("SPI_CP_CancellationDate", Cancellation_date),"Unable to Enter Cancellation date.");
           customAssert.assertTrue(k.Click("SPI_Btn_Calender_Done"), "Unable to click on done button in calander.");
           customAssert.assertTrue(!k.getAttributeIsEmpty("SPI_CP_CancellationDate", "value"),"SPI_CP_CancellationDate Field Should Contain Valid Value on Cancel Policy page .");
           
           customAssert.assertTrue(k.Input("SPI_CP_CancellationReason", (String)map_data.get("CP_CancellationReason")),"Unable to Enter Cancellation Reason.");
           customAssert.assertTrue(k.Click("SPI_Btn_Continue"), "Unable to click on Continue Button on Cancel Policy Screen");
                        
           // Read Cancellation Return Premium Summary and put values to Map  :
           
           customAssert.assertTrue(SPI_Cancel_RetrunPremiumTable(map_data), "Issue in Verifying Cancellation Return Premium");
           
           customAssert.assertTrue(k.Click("SPI_Btn_FinalCancelPolicy"), "Unable to click on Cancel Polcy button after verifying Cancellation Return Premium");
           customAssert.assertTrue(k.AcceptPopup(), "Unable to handl pop up");
                  
           return retVal;
           
    
    }catch(Throwable t){
          return false;
    }
           
}


public boolean SPI_Cancel_RetrunPremiumTable(Map<Object, Object> map_data) throws ErrorInTestMethod{
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
										common.Can_ReturnP_Values_Map.put(sectionName, ReturnP_Table_TotalVal);
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
								
								common.Can_ReturnP_Values_Map.put(sectionName, ReturnP_Table_CoverVal);
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
						Can_returnP_Error = Can_returnP_Error + Can_ReturnP_Total_Validation(sectionNames,common.Can_ReturnP_Values_Map);
					else
						Can_returnP_Error = Can_returnP_Error + CanReturnPTable_CoverSection_Validation(policy_Duration,DaysRemain, s_Name,common.Can_ReturnP_Values_Map);								
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


public boolean SPI_Cancel_PremiumSummary(Map<Object, Object> map_data) throws ErrorInTestMethod{
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
					
					common.compareValues(s_NetNetP, common.Can_ReturnP_Values_Map.get(sectionName).get("Net Net Premium"), "Cancellation Net Net Premium");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_PenCommRate,common.Can_ReturnP_Values_Map.get(sectionName).get("Pen Comm %"), "Cancellation Pen Comm Percentage");
					}				
					common.compareValues(s_PenCommAmt, common.Can_ReturnP_Values_Map.get(sectionName).get("Pen Comm"), "Cancellation Pen commm Amount");
					common.compareValues(s_NetP, common.Can_ReturnP_Values_Map.get(sectionName).get("Net Premium"), "Cancellation Net Premium");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_BrokerCommRate, common.Can_ReturnP_Values_Map.get(sectionName).get("Broker Comm %"), "Cancellation Broker Comm rate");
					}
					
					common.compareValues(s_BrokerCommAmt, common.Can_ReturnP_Values_Map.get(sectionName).get("Broker Commission"), "Cancellation Broker commission amount");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_GrossCommRate, common.Can_ReturnP_Values_Map.get(sectionName).get("Gross Comm %"), "Cancellation Gross comm rate");
					}
					
					common.compareValues(s_GrossCommAmt, common.Can_ReturnP_Values_Map.get(sectionName).get("Gross Premium"), "Cancellation Gross Premium");
					
					if(!sectionName.equals("Totals")){
						common.compareValues(s_InsTRate, common.Can_ReturnP_Values_Map.get(sectionName).get("Insurance Tax Rate"), "Cancellation Ins tax rate");
					}
					
					common.compareValues(s_InsTAmt, common.Can_ReturnP_Values_Map.get(sectionName).get("Insurance Tax"), "Cancellation Ins tax Premium");
					common.compareValues(s_TotalP, common.Can_ReturnP_Values_Map.get(sectionName).get("Total Premium"), "Cancellation Total Premium");
										
				}	
			}
				
			return retVal;		
	
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
	}
	
}

public int CanReturnPTable_CoverSection_Validation(int policy_Duration,int DaysRemain, String sectionNames,Map<String,Map<String,Double>> Can_ReturnP_Values_Map){

	Map<Object,Object> map_data = common.NB_excel_data_map;
	//String testName = (String)map_data.get("Automation Key");
	String code=null;
		
	switch(sectionNames){
	case "Solicitors PI":
		code = "PI";
		break;
	case "Solicitors excess layer":
		code = "SEL";
		break;
	default:
			System.out.println("**Cover Name is not in Scope for SPI**");
		break;
	
	}
	
	try{
			
			TestUtil.reportStatus("---------------"+sectionNames+"-----------------","Info",false);
			
			double annual_NetNetP = Double.parseDouble((String)map_data.get("PS_"+code+"_NetNetPremium"));
			String canRP_NetNetP_expected = Double.toString((annual_NetNetP/365)*DaysRemain);
			String canRP_NetNetP_actual = Double.toString(Can_ReturnP_Values_Map.get(sectionNames).get("Net Net Premium"));
			customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(canRP_NetNetP_expected),Double.parseDouble(canRP_NetNetP_actual)," Net Net Premium"),"Mismatched Net Net Premium values for section :"+sectionNames);
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent,"Premium Summary",testName,"PS_"+code+"_PenComm",pc_expected,common.NB_excel_data_map),"Error while writing Pen Commission for cover "+code+" to excel .");
			
			//SPI Transaction Pen commission Calculation : 
			double canRP_pen_comm = (( Double.parseDouble(canRP_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate"))/100)));
			String canRP_pc_expected = common.roundedOff(Double.toString(canRP_pen_comm));
			String canRP_pc_actual = Double.toString(Can_ReturnP_Values_Map.get(sectionNames).get("Pen Comm"));
			customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(canRP_pc_expected),Double.parseDouble(canRP_pc_actual)," Pen Commission"),"Mismatched Pen Commission Values for section :"+sectionNames);
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent,"Premium Summary",testName,"PS_"+code+"_PenComm",pc_expected,common.NB_excel_data_map),"Error while writing Pen Commission for cover "+code+" to excel .");
			
			
			//SPI Transaction Net Premium verification : 
			double canRP_netP = Double.parseDouble(canRP_pc_expected) + Double.parseDouble(canRP_NetNetP_expected);
			String canRP_netP_expected = common.roundedOff(Double.toString(canRP_netP));
			String canRP_netP_actual = Double.toString(Can_ReturnP_Values_Map.get(sectionNames).get("Net Premium"));
			customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(canRP_netP_expected),Double.parseDouble(canRP_netP_actual),"Net Premium"),"Mismatched Net Premium Values for section :"+sectionNames);
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_NetPremium",netP_expected,common.NB_excel_data_map),"Error while writing Net Premium for cover "+code+" to excel .");
			
			
			//SPI Transaction Broker commission Calculation : 
			double canRP_broker_comm = ((Double.parseDouble(canRP_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate"))/100)));
			String canRP_bc_expected = common.roundedOff(Double.toString(canRP_broker_comm));
			String canRP_bc_actual =  Double.toString(Can_ReturnP_Values_Map.get(sectionNames).get("Broker Commission"));
			customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(canRP_bc_expected),Double.parseDouble(canRP_bc_actual),"Broker Commission"),"Mismatched Broker Commission Values for section :"+sectionNames);
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_BrokerComm",bc_expected,common.NB_excel_data_map),"Error while writing Broker Commission for cover "+code+" to excel .");
			
			
			//SPI Transaction GrossPremium verification : 
			double canRP_grossP = Double.parseDouble(canRP_netP_expected) + Double.parseDouble(canRP_bc_expected);
			String canRP_grossP_actual = Double.toString(Can_ReturnP_Values_Map.get(sectionNames).get("Gross Premium"));
			customAssert.assertTrue(CommonFunction.compareValues(canRP_grossP,Double.parseDouble(canRP_grossP_actual),sectionNames+" Transaction Gross Premium"),"Mismatched Gross Premium Values for section :"+sectionNames);
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_GrossPremium",Double.toString(grossP),common.NB_excel_data_map),"Error while writing Gross Premium for cover "+code+" to excel .");
			
			
			double canRP_InsuranceTax = (canRP_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_InsuranceTaxRate")))/100.0;
			canRP_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(canRP_InsuranceTax)));
			String canRP_InsuranceTax_actual = Double.toString(Can_ReturnP_Values_Map.get(sectionNames).get("Insurance Tax"));
			customAssert.assertTrue(CommonFunction.compareValues(canRP_InsuranceTax,Double.parseDouble(canRP_InsuranceTax_actual),"Insurance Tax"),"Mismatched Insurance Tax Values for section :"+sectionNames);
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_InsuranceTax",Double.toString(InsuranceTax),common.NB_excel_data_map),"Error while writing Total Premium for cover "+code+" to excel .");
			
			//SPI  Transaction Total Premium verification : 
			double canRP_Premium = canRP_grossP + canRP_InsuranceTax;
			String canRP_p_expected = common.roundedOff(Double.toString(canRP_Premium));
			
			String canRP_p_actual = Double.toString(Can_ReturnP_Values_Map.get(sectionNames).get("Total Premium"));
			
			double premium_diff = Double.parseDouble(canRP_p_expected) - Double.parseDouble(canRP_p_actual);
			
			if(premium_diff<0.10 && premium_diff>-0.10){
				TestUtil.reportStatus("Total Premium [<b> "+canRP_p_expected+" </b>] matches with actual total premium [<b> "+canRP_p_actual+" </b>]as expected for "+sectionNames+" in Cancellation Return Premium table .", "Pass", false);
				//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_expected,common.NB_excel_data_map),"Error while writing Total Premium for cover "+code+" to excel .");
				return 0;
				
			}else{
				TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+canRP_p_expected+"</b>] and Actual Premium [<b> "+canRP_p_actual+"</b>] for "+sectionNames+" in Cancellation Return Premium table . </p>", "Fail", true);
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


public int Can_ReturnP_Total_Validation(List<String> sectionNames,Map<String,Map<String,Double>> Can_ReturnP_Values_Map){
	
	try{
	
			TestUtil.reportStatus("---------------Totals-----------------","Info",false);
			
			double exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + Can_ReturnP_Values_Map.get(section).get("Net Net Premium");
			}
			
			String canRP_NetNetP_actual = Double.toString(Can_ReturnP_Values_Map.get("Totals").get("Net Net Premium"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_NetNetP_actual)," Net Net Premium");

			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + Can_ReturnP_Values_Map.get(section).get("Pen Comm");
			}
			String canRP_pc_actual = Double.toString(Can_ReturnP_Values_Map.get("Totals").get("Pen Comm"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_pc_actual)," Pen Commission");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + Can_ReturnP_Values_Map.get(section).get("Net Premium");
			}
			String canRP_netP_actual = Double.toString(Can_ReturnP_Values_Map.get("Totals").get("Net Premium"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_netP_actual),"Net Premium");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + Can_ReturnP_Values_Map.get(section).get("Broker Commission");
			}
			String canRP_bc_actual =  Double.toString(Can_ReturnP_Values_Map.get("Totals").get("Broker Commission"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_bc_actual),"Broker Commission");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + Can_ReturnP_Values_Map.get(section).get("Gross Premium");
			}
			String canRP_grossP_actual = Double.toString(Can_ReturnP_Values_Map.get("Totals").get("Gross Premium"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_grossP_actual)," Gross Premium");
		
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + Can_ReturnP_Values_Map.get(section).get("Insurance Tax");
			}
			String canRP_InsuranceTax_actual = Double.toString(Can_ReturnP_Values_Map.get("Totals").get("Insurance Tax"));
			CommonFunction.compareValues(exp_value,Double.parseDouble(canRP_InsuranceTax_actual),"Insurance Tax");
	
			exp_value = 0.0;
			for(String section : sectionNames){
				if(!section.contains("Total"))
					exp_value = exp_value + Can_ReturnP_Values_Map.get(section).get("Total Premium");
			}
			String canRP_p_actual = Double.toString(Can_ReturnP_Values_Map.get("Totals").get("Total Premium"));
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

///////////---Renewal methods--///////
public boolean funcRenewalRewindOnCover(Map<Object, Object> map_data){
	
		
		try{
		customAssert.assertTrue(common_CCF.funcRenewalOnCoveOperation(map_data));
		
		//Renewal Rewind
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common.funcRewind());
		//customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
		//customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Submitted (Rewind)"), "Verify Policy Status (Renewal Submitted (Rewind)) function is having issue(S) . ");
		customAssert.assertTrue(common.decideRewindMethod());
		customAssert.assertTrue(common.transactionSummary((String)map_data.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) after Renewal Rewind  . ");

		
		return true;
		
	} catch (Exception e) {
		
		return false;
	}
	
	
	
}

public boolean funcEndorsmentOnCoverOperation(Map<Object, Object> map_data){
	
	try {
		String testName = (String)map_data.get("Automation Key");
		customAssert.assertTrue(common.funcMenuSelection("Navigate", "Policy General"),"Unable to navigate to Solicitors PI page");
		customAssert.assertTrue(func_SPIRenwal_AssignUnderwriter(map_data),"Error while Assigning Undrwriter ");
		customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"),"Unable to navigate to Premium Summary page");
		customAssert.assertTrue(funcCreateEndorsement_Renewal(),"Error while creating Endorsement . ");
		customAssert.assertTrue(func_SPIRenwal_PerfomEndorsement(map_data),"Error while Performing Endorsement . ");
		customAssert.assertTrue(common.transactionSummary("", testName, TestBase.businessEvent, TestBase.product),"");
		return true;
		
	} catch (Exception e) {
		
		return false;
	}
	
	
}

public boolean func_SPIRenwal_PerfomEndorsement(Map<Object, Object> map_data){
	boolean retVal = true;
	try {
		
		customAssert.assertTrue(common.funcMenuSelection("Navigate", "Solicitors PI"),"Unable to navigate to Solicitors PI page");
		customAssert.assertTrue(common.funcPageNavigation("Solicitors PI", ""),"Solicitors PI Page not loaded");
		customAssert.assertTrue(SPI_RenewalEndorse_PeriodRatingTable(map_data, ""),"Unable to Handle Period Rating table for renewal endorsement ");
		customAssert.assertTrue(func_Renewal_EndorsementPS(map_data, "", ""),"Unable to Handle Period Rating table for renewal endorsement ");
		
		// click on Quote :
		customAssert.assertTrue(func_SPIRenwal_AssignUnderwriter(map_data),"Error while Assigning Undrwriter ");
		customAssert.assertTrue(k.Click("SPI_Btn_Quote"),"Unable to click on Quote Button");
		customAssert.assertTrue(common.funcPageNavigation("Quote Check",""), "Quote Check page does not open . ");
		customAssert.assertTrue(k.Click("SPI_Btn_Proceed"),"Unable to click on proceed Button");
		
		String quoteDate = common.getUKDate();
		common.Renewal_excel_data_map.put("QuoteDate", quoteDate);
		
		// Click On 'Go On Cover' :
		
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(k.Click("SPI_Btn_GoOnCover"),"Unable to click on 'Go On Cover' Button");
		customAssert.assertTrue(common.funcPageNavigation("Go On Cover",""), "Go On Cover page does not open . ");
		
		if(TestBase.product.equals("PIA") ){
			try{
				driver.findElement(By.xpath("//*[contains(@id,'pia-amd-')]")).click();
				//Assert.assertTrue(k.Click("PIA_RewindOnCoverBtn"));
			}catch(Throwable t){
				k.Click("SPI_Btn_PutEndorseOnCover");
			}
			
		}
		
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcButtonSelection("Confirm"), "Unable to click on Confirm button .");
		customAssert.assertTrue(common.funcPageNavigation("Confirm Policy",""), "Confirm Policy page does not open . ");
		customAssert.assertTrue(common.funcButtonSelection("Confirm"), "Unable to click on Confirm button .");
				         
		return retVal;
		
	} catch (Exception e) {
		
		return false;
	}
	
	
}

public boolean func_SPIRenwal_AssignUnderwriter(Map<Object, Object> map_data){
	boolean retVal = true;
	try {
		//Page should be Policy General Or Premium Summary
		//customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General Page not loaded");
		customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"), "Unable to click on Assign Underwriter button .");
		customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter in renewal.");
		
		/* k.pressDownKeyonPage();
         String tableXpath = "html/body/div[3]/form/div/table/tbody/tr[1]/td[4]/a";
         driver.findElement(By.xpath(tableXpath)).click(); 
        // customAssert.assertTrue(k.getText("Page_Header").equalsIgnoreCase("Policy General"), "Policy General Page is not loaded");
         TestUtil.reportStatus("Policy Assigned to Underwriter successfully", "Info", true);*/
         
		return retVal;
		
	} catch (Exception e) {
		
		return false;
	}
	
	
}


public boolean funcGetDataForRenewal(){
	   
	   
	   try{
		   
		   
		   String quoteNumber = k.getText("Quote_Number");
		   TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Renewal",(String)common.Renewal_excel_data_map.get("Automation Key"), "Renewal_QuoteNumber", quoteNumber, common.Renewal_excel_data_map);
		   String startDate = k.getAttribute("Policy_Start_Date", "value");
		   String endDate = k.getAttribute("Policy_End_Date", "value");
		   TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.Renewal_excel_data_map.get("Automation Key"), "PS_PolicyStartDate", startDate, common.Renewal_excel_data_map);
		   TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "QuoteCreation",(String)common.Renewal_excel_data_map.get("Automation Key"), "QC_InceptionDate", startDate, common.Renewal_excel_data_map);
		   TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)common.Renewal_excel_data_map.get("Automation Key"), "PS_PolicyEndDate", endDate , common.Renewal_excel_data_map);
		
		
			String testName = null;
			Map<Object,Object> data_map = null;
			
			switch(common.currentRunningFlow){
				case "NB":
					testName = (String)common.NB_excel_data_map.get("Automation Key");
					data_map = common.NB_excel_data_map;
				break;
				case "Renewal":
					testName = (String)common.Renewal_excel_data_map.get("Automation Key");
					data_map = common.Renewal_excel_data_map;
				break;
			}
			
			final Map<String,String> locator_map = new HashMap<>();
			locator_map.put("NetNetPremium", "nnprem");
			locator_map.put("PenCommCent", "penr");
			locator_map.put("PenComm", "pencom");
			locator_map.put("NetPremium", "nprem");
			locator_map.put("BrokerCommCent", "comr");
			locator_map.put("BrokerComm", "comm");
			locator_map.put("GrossCommCent", "gcomm");
			locator_map.put("GrossPremium", "gprem");
			locator_map.put("InsuranceTaxRate", "iptr");
			locator_map.put("InsuranceTax", "gipt");
		
			
			if(((String)data_map.get("CD_SolicitorsPI")).equals("Yes")){
				func_SPI_PremiumSummaryRenewalDataWrite_Cent("PI","Solicitors PI",locator_map);
			}
			if(((String)data_map.get("CD_SolicitorsExcessLayer")).equals("Yes")){
				func_SPI_PremiumSummaryRenewalDataWrite_Cent("SEL","Solicitors excess layer",locator_map);
			}
			
			TestUtil.reportStatus("Renewal Data captured Succesfully . ", "Info", true);
		   
		   return true;
		   
	   }catch(Exception e){
		   return false;
	   }
}

////

public void RenewalFlow(String code,String fileName) throws Throwable{
	
	
	try {
		common.currentRunningFlow = "Renewal";
		
		customAssert.assertTrue(common.StingrayLogin("IRS"),"Unable to login.");
		
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		/*customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Pending"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		//customAssert.assertTrue(common.funcUpdateCoverDetails(common.Renewal_excel_data_map));
		customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"));*/
		
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
			
		String agencyName = k.getText("CCF_AgencyName");
		int length = agencyName.length();
		int index = agencyName.indexOf("of");
		String agency = agencyName.substring(index+3, length);
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "QuoteCreation",(String)common.Renewal_excel_data_map.get("Automation Key"), "QC_AgencyName", agency, common.Renewal_excel_data_map);
		customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"));
		
		customAssert.assertTrue(funcGetDataForRenewal(), "Error in function getDataForRenewal . ");
		//customAssert.assertTrue(funcPremiumSummary(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Premium Summary function is having issue(S) in Renewal . ");
		customAssert.assertTrue(common.decideRENEWALFlow(common.Renewal_excel_data_map));
		customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
		customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
		TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_Status")+"  ]</b> status. ", "Info", true);
		TestUtil.reportTestCasePassed((String)common.Renewal_excel_data_map.get("Automation Key"));
		
	} catch (Exception e) {
		
	}
	
	TestUtil.reportStatus("Test Method of Renewal For - "+code, "Pass", true);
	
	
}

public int func_SPI_PremiumSummaryRenewalDataWrite(String code,String cover_name,Map<String,String> premium_loc) {
	
	
	Map<Object,Object> map_data = null;
	
	String event=null;
	
	
	switch(TestBase.businessEvent){
		case "NB":
			map_data = common.NB_excel_data_map;
		break;
		case "MTA":
			if(common_PIA.currentFlow.equals("NB")){
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
			
		
	}
		String testName = (String)map_data.get("Automation Key");
		//double NetNet_Premium = Double.parseDouble((String)map_data.get("PS_"+code+"_NetNetPremium"));
		
		String val_cover = null;
		switch(code){
		case "PI":
			val_cover = "pi";
			break;
		case "SEL":
			val_cover = "el";
			break;
		default:
				System.out.println("**Cover Name is not in Scope for SPI**");
			break;
		
		}
		
		
	try{
			
			//SPI Pen commission Calculation : 
		
			String NNP = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("NetNetPremium")+"')]", "value");
			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow,"Premium Summary",testName,"PS_"+code+"_NetNetPremium",NNP,map_data),"Error while writing NNP for cover "+code+" to excel .");
			
			String pc_cent = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("PenCommCent")+"')]", "value");
			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow,"Premium Summary",testName,"PS_"+code+"_PenComm_rate",pc_cent,map_data),"Error while writing Pen Commission for cover "+code+" to excel .");
			
			String pc_actual = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("PenComm")+"')]", "value");
			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow,"Premium Summary",testName,"PS_"+code+"_PenComm",pc_actual,map_data),"Error while writing Pen Commission for cover "+code+" to excel .");
				
			String netP_actual = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("NetPremium")+"')]", "value");
			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_NetPremium",netP_actual,map_data),"Error while writing Net Premium for cover "+code+" to excel .");
			
			String bc_actual_cent = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("BrokerCommCent")+"')]", "value");
			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow,"Premium Summary",testName,"PS_"+code+"_BrokerComm_rate",bc_actual_cent,map_data),"Error while writing Broker Commission Cent sfor cover "+code+" to excel .");
		
			
			String bc_actual =  k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("BrokerComm")+"')]", "value");
			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_BrokerComm",bc_actual,map_data),"Error while writing Broker Commission for cover "+code+" to excel .");
			
			String gc_cent = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("GrossCommCent")+"')]", "value");
			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow,"Premium Summary",testName,"PS_"+code+"_GrossComm_rate",gc_cent,map_data),"Error while writing Pen Commission for cover "+code+" to excel .");
		
			String grossP_actual = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("GrossPremium")+"')]", "value");
			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_GrossPremium",grossP_actual,map_data),"Error while writing Gross Premium for cover "+code+" to excel .");
			
			String IPT_cent = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("InsuranceTaxRate")+"')]", "value");
			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow,"Premium Summary",testName,"PS_"+code+"_InsuranceTaxRate",IPT_cent,map_data),"Error while writing Pen Commission for cover "+code+" to excel .");
		
			
			String InsuranceTax_actual = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("InsuranceTax")+"')]", "value");
			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_InsuranceTax",InsuranceTax_actual,map_data),"Error while writing Total Premium for cover "+code+" to excel .");
			
			//SPI Total Premium verification : 
			String p_actual = common.roundedOff(k.getAttribute("SPI_"+code+"_Premium", "value"));
			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_actual,map_data),"Error while writing Total Premium for cover "+code+" to excel .");
			
			
			
			
			return 0;
			
				
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Insured Properties function is having issue(S). \n", t);
        return 1;
 }
	
}

public int func_SPI_PremiumSummaryRenewalDataWrite_Cent(String code,String cover_name,Map<String,String> premium_loc) {
	
	
	Map<Object,Object> map_data = null;
	
	String event=null;
	
	
	switch(TestBase.businessEvent){
		case "NB":
			map_data = common.NB_excel_data_map;
		break;
		case "MTA":
			if(common_PIA.currentFlow.equals("NB")){
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
			
		
	}
		String testName = (String)map_data.get("Automation Key");
		//double NetNet_Premium = Double.parseDouble((String)map_data.get("PS_"+code+"_NetNetPremium"));
		
		String val_cover = null;
		switch(code){
		case "PI":
			val_cover = "pi";
			break;
		case "SEL":
			val_cover = "el";
			break;
		default:
				System.out.println("**Cover Name is not in Scope for SPI**");
			break;
		
		}
		
		
	try{
			
			//SPI Pen commission Calculation : 
		
			//String NNP = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("NetNetPremium")+"')]", "value");
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow,"Premium Summary",testName,"PS_"+code+"_NetNetPremium",NNP,map_data),"Error while writing NNP for cover "+code+" to excel .");
			
			String pc_cent = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("PenCommCent")+"')]", "value");
			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow,"Premium Summary",testName,"PS_"+code+"_PenComm_rate",pc_cent,map_data),"Error while writing Pen Commission for cover "+code+" to excel .");
			
			//String pc_actual = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("PenComm")+"')]", "value");
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow,"Premium Summary",testName,"PS_"+code+"_PenComm",pc_actual,map_data),"Error while writing Pen Commission for cover "+code+" to excel .");
				
			//String netP_actual = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("NetPremium")+"')]", "value");
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_NetPremium",netP_actual,map_data),"Error while writing Net Premium for cover "+code+" to excel .");
			
			String bc_actual_cent = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("BrokerCommCent")+"')]", "value");
			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow,"Premium Summary",testName,"PS_"+code+"_BrokerComm_rate",bc_actual_cent,map_data),"Error while writing Broker Commission Cent sfor cover "+code+" to excel .");
		
			
			//String bc_actual =  k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("BrokerComm")+"')]", "value");
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_BrokerComm",bc_actual,map_data),"Error while writing Broker Commission for cover "+code+" to excel .");
			
			String gc_cent = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("GrossCommCent")+"')]", "value");
			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow,"Premium Summary",testName,"PS_"+code+"_GrossComm_rate",gc_cent,map_data),"Error while writing Pen Commission for cover "+code+" to excel .");
		
			//String grossP_actual = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("GrossPremium")+"')]", "value");
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_GrossPremium",grossP_actual,map_data),"Error while writing Gross Premium for cover "+code+" to excel .");
			
			String IPT_cent = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("InsuranceTaxRate")+"')]", "value");
			customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow,"Premium Summary",testName,"PS_"+code+"_InsuranceTaxRate",IPT_cent,map_data),"Error while writing Pen Commission for cover "+code+" to excel .");
		
			
			//String InsuranceTax_actual = k.getAttributeByXpath("//*[contains(@id,'_"+val_cover+"_"+premium_loc.get("InsuranceTax")+"')]", "value");
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_InsuranceTax",InsuranceTax_actual,map_data),"Error while writing Total Premium for cover "+code+" to excel .");
			
			//SPI Total Premium verification : 
			//String p_actual = common.roundedOff(k.getAttribute("SPI_"+code+"_Premium", "value"));
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_actual,map_data),"Error while writing Total Premium for cover "+code+" to excel .");
			
			
			
			
			return 0;
			
				
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Insured Properties function is having issue(S). \n", t);
        return 1;
 }
	
}

public boolean funcRenewalRewindOperation(){
	
	boolean r_value= true;
	
	try{
			common_PIA.isRenewalRewindFlow = true;
			customAssert.assertTrue(performRewindChange(common.Renewal_excel_data_map), "Error in do Rewind Change function . ");
			customAssert.assertTrue(SPI_Fees_Turnover(common.Renewal_excel_data_map),"Unable to handle table 'Fees/Turnover' in Rewind ");
			customAssert.assertTrue(calculate_SPI_Book_Premium(), "SPI Book Premium Calculation is function having issue .");
			customAssert.assertTrue(coverSPI_PeriodRatingTable(common.Renewal_excel_data_map, ""),"Error in Covers SPI PRT table in Renewal Rewind . ");
		
			
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(funcPremiumSummary(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent),"Error in Premium Summary function in Renewal rewind . ");
			
			customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));

			if(TestBase.product.equals("SPI")){
				customAssert.assertTrue(k.Click("SPI_RewindOnCoverBtn"),"Error in Clicking SPI Rewind On Cover Button . ");
			}
			
			if(TestBase.product.equals("PIA") ){
				try{
					driver.findElement(By.xpath("//*[contains(@id,'pia-rwd-')]")).click();
					//Assert.assertTrue(k.Click("PIA_RewindOnCoverBtn"));
				}catch(Throwable t){
					k.Click("SPI_RewindOnCoverBtn");
				}
				
			}
			customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
			if(TestBase.product.equals("SPI") || TestBase.product.equals("PIA")){
				customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover (Unconfirmed)"), "Verify Policy Status (Renewal On Cover (Unconfirmed)) function is having issue(S) . ");
				customAssert.assertTrue(common_PIA.func_Confirm_Policy(), "Error while changing SPI policy Status from Unconfirmed to Confirmed . ");
				customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
				customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover"), "Verify Policy Status (Renewal On Cover) function is having issue(S) . ");
			}
				
			customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Document verification function is having issue(S) . ");
		
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
}

public boolean funcRenewalSubmittedChange(){
	
	boolean r_value= true;
	
	try{
			
			//customAssert.assertTrue(common.funcMenuSelection("Navigate","Solicitors PI"),"Issue while Navigating to Solicitors PI screen . ");
			//customAssert.assertTrue(func_Renewal_Submitted_coverSPI_PeriodRatingTable(common.Renewal_excel_data_map,""), "Error in do Rewind Change function . ");
		customAssert.assertTrue(common.funcMenuSelection("Navigate", "Policy General"));
		
		customAssert.assertTrue(funcPolicyGeneral_MTA(common.Renewal_excel_data_map,"PIA","Renewal"), "Renewal Policy General function having issue .");
		
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(funcUpdateCoverDetails_MTA(common.Renewal_excel_data_map),"Error in selecting cover for Renewal.");
			
		//Assert.assertTrue(funcDecideMTAFlow(), "Error in decide MTA flow method");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Claims History"),"Issue while Navigating to Specified Perils  . ");
		customAssert.assertTrue(funcClaimsHistory(common.Renewal_excel_data_map), "MTA Claims History function having issue .");
	
		
		if(((String)common.Renewal_excel_data_map.get("CD_SolicitorsPI")).equals("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Solicitors PI"),"Issue while Navigating to Solicitors PI. ");
			customAssert.assertTrue(Solicitors_PI_MTA(common.Renewal_excel_data_map), "Solicitors PI function is having issue(S) . ");
		}
		
		if(((String)common.Renewal_excel_data_map.get("CD_SolicitorsExcessLayer")).equals("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Solicitors Excess Layer"),"Issue while Navigating to Solicitors Excess Layer. ");
			customAssert.assertTrue(func_Solicitors_ExcessLayer(common.Renewal_excel_data_map),"Issue while Navigating to Solicitors Excess Layer page  . ");
		}
			
			
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(funcPremiumSummary(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent),"Error in Premium Summary function in Renewal rewind . ");
			
		/*	customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));

			if(TestBase.product.equals("SPI")){
				customAssert.assertTrue(k.Click("SPI_RewindOnCoverBtn"),"Error in Clikcing SPI Rewind On Cover Button . ");
			}
			customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
			if(TestBase.product.equals("SPI")){
				customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover (Unconfirmed)"), "Verify Policy Status (Renewal On Cover (Unconfirmed)) function is having issue(S) . ");
				customAssert.assertTrue(common_PIA.func_Confirm_Policy(), "Error while changing SPI policy Status from Unconfirmed to Confirmed . ");
				customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
				customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover"), "Verify Policy Status (Renewal On Cover) function is having issue(S) . ");
			}
				
			customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Document verification function is having issue(S) . ");*/
		
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
}

public boolean func_Renewal_Submitted_coverSPI_PeriodRatingTable(Map<Object, Object> map_data, String sCase){
	boolean retVal = true;
	double s_SOI_BookRate = 0.00, s_SOI_InitialP = 0.00, s_SOI_BookP = 0.00, s_SOI_TechA = 0.00, s_SOI_RevisedP = 0.00, s_SOI_AnnualP = 0.00;
	double s_SOI_NetNetP = 0.00, s_SOI_Grossp = 0.00, s_SOI_TotalP = 0.00, s_SOI_IndLimit = 0.00;
	
	double s_SOI_PenComm = 0.00, s_SOI_BrokerComm = 0.00, s_SOI_InsTaxR = 0.00;
	double s_Val_PenAmt = 0.00, s_Val_BrokerAmt = 0.00, s_Val = 0.00;
	int xlWRite_Index = 0;
	
	String SummaryTable_UniqueCol, SummaryTable_Path;
	
	String sPeriodMonths;
	
	try{
		
		//Identify Period Rating Table :
		
			customAssert.assertTrue(common.funcPageNavigation("Solicitors PI", ""),"Solicitors PI page navigations issue(S)");
		
		
			String sUniqueCol ="OPTION";
			int tableId = 0;
			
			String sTablePath = "html/body/div[3]/form/div/table";
			
			tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
			
			sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
			WebElement table= driver.findElement(By.xpath(sTablePath));
			
			String sPeriodRatings = (String)map_data.get("SP_PeriodRatingTable");
			String sValue[] = sPeriodRatings.split(";");
			int noOfPR = sValue.length;
			
		// Identify Summary Table  :
			
			SummaryTable_UniqueCol ="Description";		
			tableId = k.getTableIndex(SummaryTable_UniqueCol,"xpath","html/body/div[3]/form/div/table");		
			SummaryTable_Path = "html/body/div[3]/form/div/table["+ tableId +"]";
			WebElement Summary_table= driver.findElement(By.xpath(sTablePath));			
		
		switch (sCase){
		
			case "" :
				
				
							// Enter Comm Adjust in Summary Table if required :
								customAssert.assertTrue(k.DynamicXpathWebDriver(driver, SummaryTable_Path + "/tbody/tr[1]/td[10]/input", map_data, "Solicitors PI", 0, "SP_", "RenewalPICommAdjustment", "Comm Adjust", "Input"),"Unable to enter Comm Adjust value in summary table for MTA Rewind ");
								global_SOI_CommAdjust = Double.parseDouble((String)map_data.get("SP_RenewalPICommAdjustment"));
							//}
							
					// Read Values of Period Rating Table from screen and calculations :
					
						for(int i = 0; i < noOfPR ; i++ ){	
																					
							if(common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_IsSelected").contains("Yes")){
								
								xlWRite_Index = i ;
								
								if(i>0){
										driver.findElement(By.xpath(sTablePath + "/tbody/tr["+ (i+1) +"]/td[1]/input[2]")).click();
								}								
								
								k.Click("SPI_ApplyBookRate");
								
								
								//Duration :
									duration_SoPI = Integer.parseInt(k.getText("SPI_SP_Duration"));
								
								//Read Values from Screen -  Summary Table :
								
									global_PeriodMonths = Integer.parseInt(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[2]"));
									global_FeesIncome = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[3]").replaceAll(",", ""));
									global_IndemnityLimit = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[4]").replaceAll(",", ""));
									global_SOI_BookRate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[5]/input").replaceAll(",", ""));
									global_SOI_InitialP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[6]").replaceAll(",", "")));
									gloabal_SOI_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[7]/input").replaceAll(",", ""));
									global_SOI_TecgAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[8]/input").replaceAll(",", ""));
									global_SOI_RevisedP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[9]/input").replaceAll(",", ""));
									global_SOI_sPremium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[12]/input").replaceAll(",", ""));							
								
								// Read values from Period Rating Table :
								
									global_SOI_AnnualP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[5]/input").replaceAll(",", "")));
									global_SOI_NetNetP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[6]/input").replaceAll(",", "")));
									global_SOI_GrossP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[9]/input").replaceAll(",", "")));
									global_SOI_TotalPremium = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[11]/input").replaceAll(",", "")));
									
								// Read Default Values Which are referred on Premium Summary Screen :
							
								Refer_PenComm = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[7]/input").replaceAll(",", "")));
								Refer_BrokerComm = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[8]/input").replaceAll(",", "")));
								Refer_InsTax = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[10]/input").replaceAll(",", "")));
							
							
						}						
					}
						
					break;
		}
		
		// Write data to excel :
		
		
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary", (String)common.Renewal_excel_data_map.get("Automation Key"), "PS_PI_NetNetPremium", String.valueOf(global_SOI_sPremium), common.Renewal_excel_data_map);	
		TestUtil.reportStatus("All the details on Solicitors PI covers are filled succesfully after Rewind Submitted . ", "Info", false);
		return retVal;
		
		
	}catch(Throwable t){
		System.out.println("Error in coverSPI_PeriodRatingTable in Renewal Submitted chanegs - "+t);
		return false;
	}

}

public boolean func_Populate_CoverPIA_Rater_Map(Map<Object, Object> map_data, String sCase){
	boolean retVal = true;
	double s_SOI_BookRate = 0.00, s_SOI_InitialP = 0.00, s_SOI_BookP = 0.00, s_SOI_TechA = 0.00, s_SOI_RevisedP = 0.00, s_SOI_AnnualP = 0.00;
	double s_SOI_NetNetP = 0.00, s_SOI_Grossp = 0.00, s_SOI_TotalP = 0.00, s_SOI_IndLimit = 0.00;
	
	double s_SOI_PenComm = 0.00, s_SOI_BrokerComm = 0.00, s_SOI_InsTaxR = 0.00;
	double s_Val_PenAmt = 0.00, s_Val_BrokerAmt = 0.00, s_Val = 0.00;
	int xlWRite_Index = 0;
	
	String SummaryTable_UniqueCol, SummaryTable_Path;
	
	String sPeriodMonths;
	
	try{
		
		//Identify Period Rating Table :
		
			String sUniqueCol ="OPTION";
			int tableId = 0;
			
			String sTablePath = "html/body/div[3]/form/div/table";
			
			tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
			
			sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
			WebElement table= driver.findElement(By.xpath(sTablePath));
			
			String sPeriodRatings = (String)map_data.get("SP_PeriodRatingTable");
			String sValue[] = sPeriodRatings.split(";");
			int noOfPR = sValue.length;
			
		// Identify Summary Table  :
			
			SummaryTable_UniqueCol ="Description";		
			tableId = k.getTableIndex(SummaryTable_UniqueCol,"xpath","html/body/div[3]/form/div/table");		
			SummaryTable_Path = "html/body/div[3]/form/div/table["+ tableId +"]";
			WebElement Summary_table= driver.findElement(By.xpath(sTablePath));			
		
		switch (sCase){
		
			case "" :
				
				/*// Enter Data in Period Rating Table :
				
						if(isRewindSelected.contains("Yes")){
							burnRate = PIA_Rater_output.get("Burn Rate");
							// Do Nothing
						}else{
							for(int i = 0; i < noOfPR ; i++ ){
								
								String sVal = common.NB_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_IsSelected");
														
									//click on 'Add Row' link :
									
									if(i == 0){
										driver.findElement(By.xpath(sTablePath + "/tbody/tr/td[12]/a")).click();
									}else{
										driver.findElement(By.xpath(sTablePath + "/tbody/tr["+ (i+1) +"]/td[12]/a")).click();
									}							
									
									// Enter Data :
									
									customAssert.assertTrue(k.DropDownSelection_DynamicXpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[2]/select", common.NB_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_PeriodofInsurance")),"Unable to enter PeriodofInsurance in Period rating table");
									customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[3]/input", common.NB_Structure_of_InnerPagesMaps, "Period Rating Table", i, "PRT_", "ExcessAmount", "ExcessAmount", "Input"),"Unable to enter ExcessAmount in Period rating table");
									customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[4]/input", common.NB_Structure_of_InnerPagesMaps, "Period Rating Table", i, "PRT_", "TechAdjust", "TechAdjust", "Input"),"Unable to enter TechAdjust in Period rating table");
									
							}
				*/
							// Enter Comm Adjust in Summary Table if required :
								customAssert.assertTrue(k.DynamicXpathWebDriver(driver, SummaryTable_Path + "/tbody/tr[1]/td[10]/input", map_data, "Solicitors PI", 0, "SP_", "RenewalPICommAdjustment", "Comm Adjust", "Input"),"Unable to enter Comm Adjust value in summary table for MTA Rewind ");
								global_SOI_CommAdjust = Double.parseDouble((String)map_data.get("SP_RenewalPICommAdjustment"));
							//}
							
					// Read Values of Period Rating Table from screen and calculations :
					
						for(int i = 0; i < noOfPR ; i++ ){	
																					
							if(common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_IsSelected").contains("Yes")){
								
								xlWRite_Index = i ;
								
								if(i>0){
										driver.findElement(By.xpath(sTablePath + "/tbody/tr["+ (i+1) +"]/td[1]/input[2]")).click();
								}								
								
								k.Click("SPI_ApplyBookRate");
								
								
								//Duration :
									duration_SoPI = Integer.parseInt(k.getText("SPI_SP_Duration"));
								
								//Read Values from Screen -  Summary Table :
								
									global_PeriodMonths = Integer.parseInt(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[2]"));
									global_FeesIncome = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[3]").replaceAll(",", ""));
									global_IndemnityLimit = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[4]").replaceAll(",", ""));
									global_SOI_BookRate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[5]/input").replaceAll(",", ""));
									common_PIA.PIA_Rater_output.put("Book_Rate_Cent",global_SOI_BookRate);
									global_SOI_InitialP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[6]").replaceAll(",", "")));
									//common_PIA.PIA_Rater_output.("Book_Premium_24_months",global_SOI_InitialP);
									gloabal_SOI_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[7]/input").replaceAll(",", ""));
									global_SOI_TecgAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[8]/input").replaceAll(",", ""));
									global_SOI_RevisedP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[9]/input").replaceAll(",", ""));
									global_SOI_sPremium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[12]/input").replaceAll(",", ""));							
								
								// Read values from Period Rating Table :
								
									global_SOI_AnnualP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[5]/input").replaceAll(",", "")));
									global_SOI_NetNetP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[6]/input").replaceAll(",", "")));
									global_SOI_GrossP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[9]/input").replaceAll(",", "")));
									global_SOI_TotalPremium = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[11]/input").replaceAll(",", "")));
									
								// Read Default Values Which are referred on Premium Summary Screen :
							
								Refer_PenComm = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[7]/input").replaceAll(",", "")));
								Refer_BrokerComm = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[8]/input").replaceAll(",", "")));
								Refer_InsTax = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[10]/input").replaceAll(",", "")));
							
							//Calculated Values :
							
								sPeriodMonths = common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_PeriodofInsurance");
								
								String sVal = (String)map_data.get("PG_IsYourPracticeLLP");
								if(sVal.contains("Yes")){
									s_SOI_IndLimit = 3000000.00;
								}else{
									s_SOI_IndLimit = 2000000.00;
								}
								
								s_SOI_BookRate = common_PIA.PIA_Rater_output.get("Book_Rate_Cent");
								s_SOI_InitialP = common_PIA.PIA_Rater_output.get("Book_Premium_24_months");
								
								if(sPeriodMonths.contains("12")){
									s_SOI_BookP = common_PIA.PIA_Rater_output.get("Book_Premium_12_months");
								}else if(sPeriodMonths.contains("18")){
									s_SOI_BookP = common_PIA.PIA_Rater_output.get("Book_Premium_18_months");
								}else{
									s_SOI_BookP = common_PIA.PIA_Rater_output.get("Book_Premium_24_months");
								}
								
								s_SOI_TechA = Double.parseDouble(common.roundedOff(common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_TechAdjust")));
								s_SOI_RevisedP = Double.parseDouble(common.roundedOff(Double.toString(((s_SOI_BookP*s_SOI_TechA)/100) + s_SOI_BookP)));
								s_SOI_AnnualP = Double.parseDouble(common.roundedOff(Double.toString(((s_SOI_RevisedP * global_SOI_CommAdjust)/100) + s_SOI_RevisedP)));
								
								s_SOI_NetNetP = Double.parseDouble(common.roundedOff(Double.toString(((s_SOI_AnnualP/365)*duration_SoPI))));
								/*double NB_PI_NNP = Double.parseDouble((String)common.Renewal_excel_data_map.get("PS_PI_NetNetPremium"));
								s_SOI_NetNetP = ((s_SOI_AnnualP - NB_PI_NNP)/Integer.parseInt((String)common.Renewal_excel_data_map.get("PS_Duration")))*
										((Integer.parseInt((String)common.Renewal_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.Renewal_excel_data_map.get("MTA_EndorsementPeriod"))));
								*/
								double grossCommPerc = Refer_PenComm + Refer_BrokerComm;
								
								s_Val_PenAmt = Math.abs(Double.parseDouble(common.roundedOff(Double.toString((s_SOI_NetNetP/((100-grossCommPerc)/100))*(Refer_PenComm/100)))));
								s_Val_BrokerAmt  = Math.abs(Double.parseDouble(common.roundedOff(Double.toString((s_SOI_NetNetP/((100-grossCommPerc)/100))*(Refer_BrokerComm/100)))));
								s_Val  = s_SOI_NetNetP + s_Val_PenAmt;
								
								s_SOI_Grossp = s_Val + s_Val_BrokerAmt;
								s_SOI_TotalP = Double.parseDouble(common.roundedOff(Double.toString(s_SOI_Grossp + ((s_SOI_Grossp*Refer_InsTax)/100))));
								
								double s_BurnRate = Double.parseDouble(common.roundedOff(k.getText("SPI_PI_BurnRate").replaceAll(",", "")));
								common_PIA.PIA_Rater_output.put("Burn Rate", s_BurnRate);
								
							// Comparisons :
									
								
								CommonFunction.compareValues(burnRate, s_BurnRate,"BurnRate");
								CommonFunction.compareValues(Double.parseDouble(sPeriodMonths), Double.parseDouble(String.valueOf(global_PeriodMonths)),"Period of insurance month ");
								CommonFunction.compareValues(global_SOI_GrossFees, global_FeesIncome,"Fees Income");
								CommonFunction.compareValues(s_SOI_IndLimit, global_IndemnityLimit,"Indemnity Limit");
								CommonFunction.compareValues(s_SOI_BookRate, global_SOI_BookRate,"SOI BookRate");
								CommonFunction.compareValues(s_SOI_InitialP, global_SOI_InitialP,"SOI Initial Premium");
								CommonFunction.compareValues(s_SOI_BookP, gloabal_SOI_BookP,"SOI Book Premium");
								CommonFunction.compareValues(s_SOI_TechA, global_SOI_TecgAdjust,"SOI TechAdjust");
								CommonFunction.compareValues(s_SOI_RevisedP, global_SOI_RevisedP,"SOI Revised Premium");
								CommonFunction.compareValues(s_SOI_AnnualP, global_SOI_sPremium,"SOI Annual Premium From Summary Table");
								CommonFunction.compareValues(s_SOI_AnnualP, global_SOI_AnnualP,"SOI Annual Premium From PR Table");
								CommonFunction.compareValues(s_SOI_NetNetP, global_SOI_NetNetP,"SOI Net Net Premium");
								CommonFunction.compareValues(s_SOI_Grossp, global_SOI_GrossP,"SOI Gross Premium");
								CommonFunction.compareValues(s_SOI_TotalP, global_SOI_TotalPremium,"SOI Total Premium");
						}						
					}
						
					break;
		}
		
		// Write data to excel :
		
		
		TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_AnnualizedPremium", String.valueOf(s_SOI_AnnualP),common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_NetNetPremium", String.valueOf(s_SOI_NetNetP),common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		
		if(sCase.equals("")){
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_PenCommission", String.valueOf(Refer_PenComm),common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_BrokerCommission", String.valueOf(Refer_BrokerComm),common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_InsuranceTax", String.valueOf(Refer_InsTax),common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		}else{
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_PenCommission", String.valueOf(s_SOI_PenComm),common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_BrokerCommission", String.valueOf(s_SOI_BrokerComm),common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_InsuranceTax", String.valueOf(s_SOI_InsTaxR),common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		}
		
		TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_GrossPremium", String.valueOf(s_SOI_Grossp),common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Period Rating Table", common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index).get("Automation Key"), "PRT_TotalPremium", String.valueOf(s_SOI_TotalP),common.Renewal_Structure_of_InnerPagesMaps.get("Period Rating Table").get(xlWRite_Index));
		
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary", (String)common.Renewal_excel_data_map.get("Automation Key"), "PS_PI_NetNetPremium", String.valueOf(s_SOI_AnnualP), common.Renewal_excel_data_map);	
		
		return retVal;
		
		
	}catch(Throwable t){
		System.out.println("Error in coverSPI_PeriodRatingTable in Renewal Submitted chanegs - "+t);
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
							if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("No"))
								continue;
							else
					 			customAssert.assertTrue(common.selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
						}else{
							if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("Yes"))
								continue;
							else
								customAssert.assertTrue(common.deSelectCovers(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
						}
					
					}else if(c_locator.equals("PEL")){
						
					}else{
						if (!driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")).isSelected()){
							JavascriptExecutor j_exe = (JavascriptExecutor) driver;
							j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//*[contains(@name,'"+c_locator+"_selected')]")));
							
								if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("No"))
									continue;
								else
						 			customAssert.assertTrue(common.selectCover(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
													
							}else{
								if(((String) map_data.get("CD_"+coverName)).equalsIgnoreCase("Yes"))
									continue;
								else
									customAssert.assertTrue(common.deSelectCovers(coverWithLocator,map_data), "Select covers function is having issue(S) . ");
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
//New MTA Flow

public void MTAFlow(String code,String fileName) throws ErrorInTestMethod{
	
	String testName = (String)common.MTA_excel_data_map.get("Automation Key");
	try{
		NewBusinessFlow(code,"NB");
		common_PIA.currentFlow="MTA";
		common.currentRunningFlow="MTA";
		customAssert.assertTrue(funcCreateEndorsement(),"Error while creating Endorsement . ");
		
		customAssert.assertTrue(funcPolicyGeneral_MTA(common.MTA_excel_data_map,code,"MTA"), "MTA Policy General function having issue .");
		
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(funcUpdateCoverDetails_MTA(common.MTA_excel_data_map),"Error in selecting cover for MTA.");
			
		//Assert.assertTrue(funcDecideMTAFlow(), "Error in decide MTA flow method");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Claims History"),"Issue while Navigating to Specified Perils  . ");
		customAssert.assertTrue(funcClaimsHistory(common.MTA_excel_data_map), "MTA Claims History function having issue .");
	
		
		if(((String)common.MTA_excel_data_map.get("CD_SolicitorsPI")).equals("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Solicitors PI"),"Issue while Navigating to Solicitors PI. ");
			customAssert.assertTrue(Solicitors_PI_MTA(common.MTA_excel_data_map), "Solicitors PI function is having issue(S) . ");
		}
		
		if(((String)common.MTA_excel_data_map.get("CD_SolicitorsExcessLayer")).equals("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Solicitors Excess Layer"),"Issue while Navigating to Solicitors Excess Layer. ");
			customAssert.assertTrue(func_Solicitors_ExcessLayer(common.MTA_excel_data_map),"Issue while Navigating to Solicitors Excess Layer page  . ");
		}
		
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(func_MTAPremiumSummary_New(common.MTA_excel_data_map,TestBase.product,TestBase.businessEvent), "Premium Summary function is having issue(S) . ");
		Assert.assertTrue(common.funcStatusHandling(common.MTA_excel_data_map,code,TestBase.businessEvent));
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		
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

public boolean Solicitors_PI_MTA( Map<Object, Object> map_data){
	boolean retVal = true;
	
	Map<String, List<Map<String, String>>> data_map = null;
	
	switch(common.currentRunningFlow){
		case "MTA":
				data_map = common.MTA_Structure_of_InnerPagesMaps;
				break;
		case "NB":
				data_map = common.NB_Structure_of_InnerPagesMaps;
				break;
		case "Rewind":
				data_map = common.Rewind_Structure_of_InnerPagesMaps;
				break;
		case "Requote":
				data_map = common.Requote_Structure_of_InnerPagesMaps;
				break;
	}
	
	try{
		customAssert.assertTrue(common.funcPageNavigation("Solicitors PI", ""), "Navigation problem to Solicitors PI page .");
		
		// Area of Practice Table section :
		
		customAssert.assertTrue(SPI_AreaOfPractice_MTA(data_map),"Unable to handle table 'Area of practice'");
		
		// Select Work split to value :
		customAssert.assertTrue(k.DropDownSelection("SPI_SP_WorkSplittoUse", (String)map_data.get("SP_WorkSplittoUse")), "Unable to enter 'Work Split to use' value");

		// fees/Turnover Table section :
		
		customAssert.assertTrue(SPI_Fees_Turnover_MTA(map_data),"Unable to handle table 'Fees/Turnover'");
		
		// Select 'Rate on' value :
		if(common_PIA.isMTARewindFlow){
			customAssert.assertTrue(k.DropDownSelection("SPI_SP_RateOn", (String)map_data.get("SP_MTARewind_RateOn")), "Unable to enter 'Rate on' value");
		}else{
			customAssert.assertTrue(k.DropDownSelection("SPI_SP_RateOn", (String)map_data.get("SP_RateOn")), "Unable to enter 'Rate on' value");
		}
		
		if(!common.currentRunningFlow.equalsIgnoreCase("Renewal") && !common.currentRunningFlow.equalsIgnoreCase("Rewind")){
			String default_PI_policy_fee = k.getAttribute("PIA_SP_AdminFeeSP", "value");
			customAssert.assertEquals(default_PI_policy_fee, (String)map_data.get("SP_DefaultAdminFeeSP"),"PI Default Admin fee is In-correct for Endorsement. Expected: "+(String)map_data.get("SP_DefaultAdminFeeSP")+" and Actual: "+default_PI_policy_fee);
		}
	
		// Excess Section :
		customAssert.assertTrue(k.Input("PIA_SP_AdminFeeSP", (String)map_data.get("SP_AdminFeeSP")),	"Unable to enter value in Admin fees field .");
		customAssert.assertTrue(k.DropDownSelection("SPI_SP_Aggegate", (String)map_data.get("SP_Aggegate")), "Unable to enter 'Aggregate' value");
		customAssert.assertTrue(calculate_SPI_Book_Premium(), "SPI Book Premium Calculation is function having issue .");
		TestUtil.reportStatus("SPI Rater Lookup values are calculated sucessfully after Endorsement . ", "Info", true);
		// Burn Rate:
		burnRate = common_PIA.PIA_Rater_output.get("Burn Rate");
				
		//Policy start and end date :
		//Refer_Policy_SDate = k.getText("SPI_SP_PolicyStartDate");
		Refer_Policy_EDate = k.getText("SPI_SP_PolicyEndDate");
		dAdminFee_CoverSPI=  Double.parseDouble((String)map_data.get("SP_AdminFeeSP"));
		
		//Period Rating Table Section :
		customAssert.assertTrue(coverSPI_PeriodRatingTable_MTA(map_data, ""),"Error in coverSPI_PeriodRatingTable_MTA function. ");
		
		TestUtil.reportStatus("Solicitors PI Cover details are filled and Verified sucessfully after Endorsement . ", "Info", true);
		
		
		return retVal;
	
	}catch(Throwable t){
		System.out.println("Error in Solicitors_PI function - "+t);
		return false;
	}

	
}

public static boolean SPI_AreaOfPractice_MTA( Map<String, List<Map<String, String>>> mdata){
	boolean retVal = true;
	int totalCols, sTotal = 0;
	Map<Object,Object> data_map = null;
	switch(common.currentRunningFlow){
	case "MTA":
		data_map=common.MTA_excel_data_map;
		mdata = common.MTA_Structure_of_InnerPagesMaps;
		break;
	case "Renewal":
		data_map=common.Renewal_excel_data_map;
		mdata = common.Renewal_Structure_of_InnerPagesMaps;
		break;
	case "Rewind":
		data_map = common.Rewind_excel_data_map;
		mdata = common.Rewind_Structure_of_InnerPagesMaps;
		break;
	}
	
	try{
	
		String sUniqueCol ="Area of practice";
		int tableId = 0;
		int noOfActivities =0;
		
		String sTablePath = "html/body/div[3]/form/div/table";
		
		tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
		
		sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
		WebElement table= driver.findElement(By.xpath(sTablePath));
		totalCols = table.findElements(By.tagName("th")).size();
		int noOfAOP =0;
		String sAreaOfPractice = null;
		try{
			
			//if(common_PIA.isMTARewindFlow){
				//sAreaOfPractice = (String)common.MTA_excel_data_map.get("SP_MTARewind_AreaofPractice");
			//}else{
				sAreaOfPractice = (String)data_map.get("SP_AreaofPractice");
			//}
		
			
			String sValue[] = sAreaOfPractice.split(";");
			noOfAOP = sValue.length;
		
		}catch(Throwable t){
			noOfAOP = 0;
		}
		//mdata = common.MTA_Structure_of_InnerPagesMaps;
		//Click Delete Button for Fresh MTA Data
		boolean isNotStale=true;
		
			
		while(isNotStale){
			try{
		List<WebElement> delete_Btns = driver.findElements(By.xpath("//td[text()='Area of Practice']//following::table[@id='p3_spi_table1']//a[text()='Delete']"));
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
		
		

		// Enter values to the table :
		
		for(int i = 0; i < noOfAOP; i++ ){
			int row = i;
			//click on 'Add Row' link :
			WebElement Add_row_btn = driver.findElement(By.xpath("//td[text()='Area of Practice']//following::table[@id='p3_spi_table1']//a[text()='Add Row']"));
			Add_row_btn.click();
			//driver.findElement(By.xpath(sTablePath + "/tbody/tr["+ (i+2) +"]/td[5]/a")).click();
			
			//Enter Data :
			/*if(common_PIA.isMTARewindFlow){
				i = 1;
			}*/
			
			customAssert.assertTrue(k.DropDownSelection_DynamicXpath(sTablePath + "/tbody/tr["+(row+1)+"]/td[1]/select", mdata.get("Area of Practice").get(i).get("PIA_AOP_AreaOfPracticeValue")),"Unable to enter activity in Area of practice table");
			
			customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(row+1)+"]/td[2]/input", mdata, "Area of Practice", i, "PIA_AOP_", "LastYear", "Last year (%)", "Input"),"Unable to enter Last year (%) in Area of practice table");
			customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(row+1)+"]/td[3]/input", mdata, "Area of Practice", i, "PIA_AOP_", "PriorYear", "PriorYear", "Input"),"Unable to enter prior year (%) in Area of practice table");
			customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(row+1)+"]/td[4]/input", mdata, "Area of Practice", i, "PIA_AOP_", "PriorYear2", "PriorYear", "Input"),"Unable to enter prior year 2 (%) in Area of practice table");		
		
		}
		
		k.Click("SPI_Save_SOI");
		// Read total, verify and adjust if not equals to 100% :
						
		for(int j = 0; j < totalCols-2; j++){
			
			sTotal =  Integer.parseInt(k.GetText_DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr["+(noOfAOP+1)+"]/td["+(j+2)+"]"));
			
			if(sTotal != 100){
				TestUtil.reportStatus("Total does not equal to 100% for AreaOfPractice table in Endorsement. Entered value is : <b>[  "+ sTotal +"  ]</b>. ", "Info", false);
				retVal = false;
			}else{
				TestUtil.reportStatus("Total Percent is verified AreaOfPractice table after Endorsement. Entered value is : <b>[  "+ sTotal +"  ]</b>.", "Info", true);
			}
		}		
		
		return retVal;
	
	}catch(Throwable t){
		System.out.println("Error in SPI_AreaOfPractice - "+t);
		return false;
	}
	
	
}

public static boolean SPI_Fees_Turnover_MTA( Map<Object, Object> map_data){
	boolean retVal = true;
	int totalCols, sTotal = 0, totalRows, GLimit;
	String sFinal ="", sVal = "";
	
	try{
		
		if(common_PIA.isRewindSelected.contains("Yes") || common_PIA.isRenewalRewindFlow){
			sVal = "Rewind";
		}
								
		String sUniqueCol ="Year";
		int tableId = 0;
		
		String sTablePath = "html/body/div[3]/form/div/table";
		
		tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
		
		sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
		WebElement table= driver.findElement(By.xpath(sTablePath));
		
		totalCols = table.findElements(By.tagName("th")).size();
		totalRows = table.findElements(By.tagName("tr")).size();
		
		String sval = (String)map_data.get("PG_Geographical_limit");
		GLimit  = (sval.split(",")).length;
		
		switch (sVal){
			case "Rewind" :
				sFinal = (String)map_data.get("SP_RateOn_Rewind");
				  break;
				 
			default :
				
				// Enter values to the table :
				if(common_PIA.isMTARewindFlow){
					sFinal = (String)map_data.get("SP_MTARewind_RateOn");
				}else{
					sFinal = (String)map_data.get("SP_RateOn");
				}
				
				for(int i = 0; i < totalCols-3; i++ ){
					
					//Get Column name :
					
					String Reagon = k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/thead/tr/th["+(i+3)+"]");
					
					if(Reagon.length() > 0){
						
						if(Reagon.contains("Rest of the World")){
							Reagon = "ROW";
						}else if(Reagon.contains("USA/ Canada")){
							Reagon = "USACanada";
						}
						
						//Enter Data :
						
							customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr[1]/td[" + (i+3) +"]/input", map_data, "Solicitors PI", i, "SP_", "Estimate"+Reagon, Reagon, "Input"),"Unable to enter Estimate value for reagon - "+Reagon);				
							customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr[2]/td[" + (i+3) +"]/input", map_data, "Solicitors PI", i, "SP_", "2017"+Reagon, Reagon, "Input"),"Unable to enter 2017 value for reagon - "+Reagon);
							customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr[3]/td[" + (i+3) +"]/input", map_data, "Solicitors PI", i, "SP_", "2016"+Reagon, Reagon, "Input"),"Unable to enter 2016 value for reagon - "+Reagon);
							customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr[4]/td[" + (i+3) +"]/input", map_data, "Solicitors PI", i, "SP_", "2015"+Reagon, Reagon, "Input"),"Unable to enter 2015 value for reagon - "+Reagon);
							customAssert.assertTrue(k.DynamicXpathWebDriver(driver, sTablePath + "/tbody/tr[5]/td[" + (i+3) +"]/input", map_data, "Solicitors PI", i, "SP_", "2014"+Reagon, Reagon, "Input"),"Unable to enter 2014 value for reagon - "+Reagon);
					}			
				}
				
				break;			
				
		}
		
		// Read FeesIncome Total :
		
			TotalFeesIncome_Estimate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[1]/td[" +totalCols+ "]/input").replaceAll(",", ""));
			TotalFeesIncome_2017 = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[2]/td[" +totalCols+ "]/input").replaceAll(",", ""));
			TotalFeesIncome_2016 = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[3]/td[" +totalCols+ "]/input").replaceAll(",", ""));
			TotalFeesIncome_2015 = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[4]/td[" +totalCols+ "]/input").replaceAll(",", ""));
			TotalFeesIncome_2014 = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[5]/td[" +totalCols+ "]/input").replaceAll(",", ""));
			Total_Average = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr[6]/td[" +totalCols+ "]/input").replaceAll(",", ""));
							
			if(sFinal.contains("Estimate")){
				global_SOI_GrossFees = TotalFeesIncome_Estimate; 
			}else if(sFinal.contains("Last Year")){
				global_SOI_GrossFees = TotalFeesIncome_2017;
			}else if(sFinal.equalsIgnoreCase("Prior Year")){
				global_SOI_GrossFees = TotalFeesIncome_2016;
			}else if(sFinal.equalsIgnoreCase("Prior Year 2")){
				global_SOI_GrossFees = TotalFeesIncome_2015;
			}else if(sFinal.equalsIgnoreCase("Prior Year 3")){
				global_SOI_GrossFees = TotalFeesIncome_2014;
			}else{
				global_SOI_GrossFees = Total_Average;
			}
			
			TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Solicitors PI",(String)map_data.get("Automation Key"), "SP_GrossFee", String.valueOf(global_SOI_GrossFees), map_data);
			
			if(common_PIA.isRewindSelected.contains("Yes") || common_PIA.isRenewalRewindFlow){
				map_data.put("SP_GrossFee",  String.valueOf(global_SOI_GrossFees));
			}
			
		return retVal;
	
	}catch(Throwable t){
		System.out.println("Error in SPI_Fees_Turnover - "+t);
		return false;
	}
	
}

public boolean coverSPI_PeriodRatingTable_MTA( Map<Object, Object> map_data, String sCase){
	boolean retVal = true;
	double s_SOI_BookRate = 0.00, s_SOI_InitialP = 0.00, s_SOI_BookP = 0.00, s_SOI_TechA = 0.00, s_SOI_RevisedP = 0.00, s_SOI_AnnualP = 0.00;
	double s_SOI_NetNetP = 0.00, s_SOI_Grossp = 0.00, s_SOI_TotalP = 0.00, s_SOI_IndLimit = 0.00;
	
	double s_SOI_PenComm = 0.00, s_SOI_BrokerComm = 0.00, s_SOI_InsTaxR = 0.00;
	double s_Val_PenAmt = 0.00, s_Val_BrokerAmt = 0.00, s_Val = 0.00;
	int xlWRite_Index = 0;
	
	String SummaryTable_UniqueCol, SummaryTable_Path;
	
	String sPeriodMonths;
	
	Map<String, List<Map<String, String>>> data_Structure_of_InnerPagesMaps = null;
	
	switch(common.currentRunningFlow){
	
		case "NB":
			data_Structure_of_InnerPagesMaps = common.NB_Structure_of_InnerPagesMaps;
			break;
		case "MTA":
			data_Structure_of_InnerPagesMaps = common.MTA_Structure_of_InnerPagesMaps;
			break;
		case "Renewal":
			data_Structure_of_InnerPagesMaps = common.Renewal_Structure_of_InnerPagesMaps;
			break;	
		case "Rewind":
			data_Structure_of_InnerPagesMaps = common.Rewind_Structure_of_InnerPagesMaps;
			break;	
	
	}
	
	
	try{
		
		//Identify Period Rating Table :
		
			String sUniqueCol ="OPTION";
			int tableId = 0;
			
			String sTablePath = "html/body/div[3]/form/div/table";
			
			tableId = k.getTableIndex(sUniqueCol,"xpath",sTablePath);
			
			sTablePath = "html/body/div[3]/form/div/table["+ tableId +"]";
			WebElement table= driver.findElement(By.xpath(sTablePath));
			
			String sPeriodRatings = (String)map_data.get("SP_PeriodRatingTable");
			String sValue[] = sPeriodRatings.split(";");
			int noOfPR = sValue.length;
			
		// Identify Summary Table  :
			
			SummaryTable_UniqueCol ="Description";		
			tableId = k.getTableIndex(SummaryTable_UniqueCol,"xpath","html/body/div[3]/form/div/table");		
			SummaryTable_Path = "html/body/div[3]/form/div/table["+ tableId +"]";
			WebElement Summary_table= driver.findElement(By.xpath(sTablePath));			
			
		switch (sCase){
		
			case "" :
				
					// Enter Data in Period Rating Table :
				
						if(common_PIA.isRewindSelected.contains("Yes") || common_PIA.isRenewalRewindFlow){
							burnRate = common_PIA.PIA_Rater_output.get("Burn Rate");
							// Do Nothing
						}else{
							
							if(!((String)map_data.get("Automation Key")).contains("MTA_07")){
							for(int i = 0; i < noOfPR ; i++ ){
								
								String sVal = data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_IsSelected");
														
									//click on 'Add Row' link :
								
									WebElement Add_row_btn = driver.findElement(By.xpath("//td[text()='Period Rating Table']//following::a[text()='Add Row']"));
									Add_row_btn.click();
									
									/*if(i == 0){
										driver.findElement(By.xpath(sTablePath + "/tbody/tr/td[12]/a")).click();
									}else{
										driver.findElement(By.xpath(sTablePath + "/tbody/tr["+ (i+1) +"]/td[12]/a")).click();
									}*/							
									
									// Enter Data :
									
									customAssert.assertTrue(k.DropDownSelection_DynamicXpath(sTablePath + "/tbody/tr["+(i+1)+"]/td[2]/select", data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_PeriodofInsurance")),"Unable to enter PeriodofInsurance in Period rating table");
									customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[3]/input", data_Structure_of_InnerPagesMaps, "Period Rating Table", i, "PRT_", "ExcessAmount", "ExcessAmount", "Input"),"Unable to enter ExcessAmount in Period rating table");
									customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(i+1)+"]/td[4]/input", data_Structure_of_InnerPagesMaps, "Period Rating Table", i, "PRT_", "TechAdjust", "TechAdjust", "Input"),"Unable to enter TechAdjust in Period rating table");
									
							}
							}
				
							// Enter Comm Adjust in Summary Table if required :
								customAssert.assertTrue(k.DynamicXpathWebDriver(driver, SummaryTable_Path + "/tbody/tr[1]/td[10]/input", map_data, "Solicitors PI", 0, "SP_", "PICommAdjustment", "Comm Adjust", "Input"),"Unable to enter Comm Adjust value in summary table");
								global_SOI_CommAdjust = Double.parseDouble((String)map_data.get("SP_PICommAdjustment"));
						}
						
						if(common_PIA.isRenewalRewindFlow){
							customAssert.assertTrue(k.DynamicXpathWebDriver(driver, SummaryTable_Path + "/tbody/tr[1]/td[10]/input", map_data, "Solicitors PI", 0, "SP_", "PICommAdjustment", "Comm Adjust", "Input"),"Unable to enter Comm Adjust value in summary table");
							global_SOI_CommAdjust = Double.parseDouble((String)map_data.get("SP_PICommAdjustment"));
			
						}
							
					// Read Values of Period Rating Table from screen and calculations :
					
						for(int i = 0; i < noOfPR ; i++ ){	
																					
							if(data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_IsSelected").contains("Yes")){
								
								xlWRite_Index = i ;
								
								if(i>0){
										driver.findElement(By.xpath(sTablePath + "/tbody/tr["+ (i+1) +"]/td[1]/input[2]")).click();
								}								
								
								k.Click("SPI_ApplyBookRate");
								
								
								//Duration :
									duration_SoPI = Integer.parseInt(k.getText("SPI_SP_Duration"));
								
								//Read Values from Screen -  Summary Table :
								
									global_PeriodMonths = Integer.parseInt(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[2]"));
									global_FeesIncome = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[3]").replaceAll(",", ""));
									global_IndemnityLimit = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[4]").replaceAll(",", ""));
									global_SOI_BookRate = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[5]/input").replaceAll(",", ""));
									global_SOI_InitialP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[6]").replaceAll(",", "")));
									gloabal_SOI_BookP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[7]/input").replaceAll(",", ""));
									global_SOI_TecgAdjust = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[8]/input").replaceAll(",", ""));
									global_SOI_RevisedP = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[9]/input").replaceAll(",", ""));
									global_SOI_sPremium = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, SummaryTable_Path+"/tbody/tr[1]/td[12]/input").replaceAll(",", ""));							
								
								// Read values from Period Rating Table :
								
									global_SOI_AnnualP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[5]/input").replaceAll(",", "")));
									global_SOI_NetNetP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[6]/input").replaceAll(",", "")));
									global_SOI_GrossP = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[9]/input").replaceAll(",", "")));
									global_SOI_TotalPremium = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[11]/input").replaceAll(",", "")));
									
								// Read Default Values Which are referred on Premium Summary Screen :
							
								Refer_PenComm = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[7]/input").replaceAll(",", "")));
								Refer_BrokerComm = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[8]/input").replaceAll(",", "")));
								Refer_InsTax = Double.parseDouble(common.roundedOff(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(i+1)+"]/td[10]/input").replaceAll(",", "")));
							
							//Calculated Values :
							
								sPeriodMonths = data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_PeriodofInsurance");
								
								String sVal = (String)map_data.get("PG_IsYourPracticeLLP");
								if(sVal.contains("Yes")){
									s_SOI_IndLimit = 3000000.00;
								}else{
									s_SOI_IndLimit = 2000000.00;
								}
								
								s_SOI_BookRate = common_PIA.PIA_Rater_output.get("Book_Rate_Cent");
								s_SOI_InitialP = common_PIA.PIA_Rater_output.get("Book_Premium_24_months");
								
								if(sPeriodMonths.contains("12")){
									s_SOI_BookP = common_PIA.PIA_Rater_output.get("Book_Premium_12_months");
								}else if(sPeriodMonths.contains("18")){
									s_SOI_BookP = common_PIA.PIA_Rater_output.get("Book_Premium_18_months");
								}else{
									s_SOI_BookP = common_PIA.PIA_Rater_output.get("Book_Premium_24_months");
								}
								
								s_SOI_TechA = Double.parseDouble(common.roundedOff(data_Structure_of_InnerPagesMaps.get("Period Rating Table").get(i).get("PRT_TechAdjust")));
								s_SOI_RevisedP = Double.parseDouble(common.roundedOff(Double.toString(((s_SOI_BookP*s_SOI_TechA)/100) + s_SOI_BookP)));
								s_SOI_AnnualP = Double.parseDouble(common.roundedOff(Double.toString(((s_SOI_RevisedP * global_SOI_CommAdjust)/100) + s_SOI_RevisedP)));
								
								s_SOI_NetNetP = Double.parseDouble(common.roundedOff(Double.toString(((s_SOI_AnnualP/365)*duration_SoPI))));
								
								double grossCommPerc = Refer_PenComm + Refer_BrokerComm;
								
								s_Val_PenAmt = Math.abs(Double.parseDouble(common.roundedOff(Double.toString((s_SOI_NetNetP/((100-grossCommPerc)/100))*(Refer_PenComm/100)))));
								s_Val_BrokerAmt  = Math.abs(Double.parseDouble(common.roundedOff(Double.toString((s_SOI_NetNetP/((100-grossCommPerc)/100))*(Refer_BrokerComm/100)))));
								s_Val  = s_SOI_NetNetP + s_Val_PenAmt;
								
								s_SOI_Grossp = s_Val + s_Val_BrokerAmt;
								s_SOI_TotalP = Double.parseDouble(common.roundedOff(Double.toString(s_SOI_Grossp + ((s_SOI_Grossp*Refer_InsTax)/100))));
								
								double s_BurnRate = Double.parseDouble(common.roundedOff(k.getText("SPI_PI_BurnRate").replaceAll(",", "")));
								
							// Comparisons :
									
								
								CommonFunction.compareValues(burnRate, s_BurnRate,"BurnRate");
								CommonFunction.compareValues(Double.parseDouble(sPeriodMonths), Double.parseDouble(String.valueOf(global_PeriodMonths)),"Period of insurance month ");
								CommonFunction.compareValues(global_SOI_GrossFees, global_FeesIncome,"Fees Income");
								CommonFunction.compareValues(s_SOI_IndLimit, global_IndemnityLimit,"Indemnity Limit");
								CommonFunction.compareValues(s_SOI_BookRate, global_SOI_BookRate,"SOI BookRate");
								CommonFunction.compareValues(s_SOI_InitialP, global_SOI_InitialP,"SOI Initial Premium");
								CommonFunction.compareValues(s_SOI_BookP, gloabal_SOI_BookP,"SOI Book Premium");
								CommonFunction.compareValues(s_SOI_TechA, global_SOI_TecgAdjust,"SOI TechAdjust");
								CommonFunction.compareValues(s_SOI_RevisedP, global_SOI_RevisedP,"SOI Revised Premium");
								CommonFunction.compareValues(s_SOI_AnnualP, global_SOI_sPremium,"SOI Annual Premium From Summary Table");
								CommonFunction.compareValues(s_SOI_AnnualP, global_SOI_AnnualP,"SOI Annual Premium From PR Table");
							}						
					}
						
					break;
					
			case "Edit_PS" :
				
				// Enter Comm Adjust in Summary Table if required :
					customAssert.assertTrue(k.DynamicXpathWebDriver(driver, SummaryTable_Path + "/tbody/tr[1]/td[10]/input", map_data, "Solicitors PI", 0, "SP_", "'SP_PICommAdjustment", "Comm Adjust", "Input"),"Unable to enter Comm Adjust value in summary table");
					global_SOI_CommAdjust = Double.parseDouble((String)map_data.get("SP_PICommAdjustment"));
					
				// Enter data in PR Table :
					customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(noOfPR-1)+"]/td[2]/input", data_Structure_of_InnerPagesMaps, "Period Rating Table", (noOfPR-3), "PRT_", "ExcessAmount", "ExcessAmount", "Input"),"Unable to enter ExcessAmount in Period rating table");
					customAssert.assertTrue(k.DynamicXpathWebDriver_Inner(driver, sTablePath + "/tbody/tr["+(noOfPR-1)+"]/td[3]/input", data_Structure_of_InnerPagesMaps, "Period Rating Table", (noOfPR-3), "PRT_", "TechAdjust", "TechAdjust", "Input"),"Unable to enter TechAdjust in Period rating table");
				
				k.Click("SPI_ApplyBookRate");
				
				//Duration :
					duration_SoPI = Integer.parseInt(k.getText("SPI_SP_Duration"));
				
				// Read reflected values from Premium summary screen :
				
					s_SOI_PenComm = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(noOfPR-1)+"]/td[7]/input").replaceAll(",", ""));
					s_SOI_BrokerComm = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(noOfPR-1)+"]/td[8]/input").replaceAll(",", ""));
					s_SOI_InsTaxR = Double.parseDouble(k.GetText_DynamicXpathWebDriver(driver, sTablePath+"/tbody/tr["+(noOfPR-1)+"]/td[10]/input").replaceAll(",", ""));
					
				
		}
			
		
		// Write data to excel :
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "Premium Summary", (String)map_data.get("Automation Key"), "PS_PI_NetNetPremium", String.valueOf(global_SOI_sPremium), map_data);	
		
		return retVal;
	
	}catch(Throwable t){
		System.out.println("Error in coverSPI_PeriodRatingTable - "+t);
		return false;
	}
	
	
}

public boolean Verify_premiumSummaryTable_New_MTA(){
	err_count = 0;
	final String code = TestBase.product;
	final String event = TestBase.businessEvent;
	String testName = (String)common.MTA_excel_data_map.get("Automation Key");
	
	final Map<String,String> locator_map = new HashMap<>();
	locator_map.put("PenComm", "pencom");
	locator_map.put("NetPremium", "nprem");
	locator_map.put("BrokerComm", "comm");
	locator_map.put("GrossPremium", "gprem");
	locator_map.put("InsuranceTax", "gipt");
	
	double exp_PI_Premium = 0.0,exp_SEC_Premium = 0.0;
	
	try{
		
		
		if(((String)common.MTA_excel_data_map.get("CD_SolicitorsPI")).equals("Yes")){
			err_count = err_count + func_SPI_PremiumSummaryCalculation("PI","Solicitors PI",locator_map);
			exp_PI_Premium = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_PI_TotalPremium"));
		}
		if(((String)common.MTA_excel_data_map.get("CD_SolicitorsExcessLayer")).equals("Yes")){
			err_count = err_count + func_SPI_PremiumSummaryCalculation("SEL","Solicitors excess layer",locator_map);
			exp_SEC_Premium = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_SEL_TotalPremium"));
		}
	
		
	
	String exp_Total_Premium = common.roundedOff(Double.toString(exp_PI_Premium + exp_SEC_Premium));
	String act_Total_Premium = k.getAttribute("SPI_Total_Premium", "value");
	act_Total_Premium = act_Total_Premium.replaceAll(",", "");
	
	double premium_diff = Double.parseDouble(common.roundedOff(Double.toString(Double.parseDouble(exp_Total_Premium) - Double.parseDouble(act_Total_Premium))));
	
	TestUtil.reportStatus("---------------Total Premium after Endorsement-----------------","Info",false);
	
	if(Math.abs(premium_diff)<=0.07){
		TestUtil.reportStatus("Total Premium :[<b> "+exp_Total_Premium+" </b>] matches with actual premium [<b> "+act_Total_Premium+"</b>]as expected with some difference upto '0.05' on premium summary page after MTA .", "Pass", false);
		customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_TotalPremium", exp_Total_Premium,common.MTA_excel_data_map),"Error while writing Total Premium data to excel .");
	}else{
		TestUtil.reportStatus("Mismatch in Expected Premium [<b> "+exp_Total_Premium+"</b>] and Actual Premium [<b> "+act_Total_Premium+"</b>] on premium summary page.", "Fail", false);
		customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_TotalPremium", exp_Total_Premium,common.MTA_excel_data_map),"Error while writing Total Premium data to excel .");
	}
		
	customAssert.assertTrue(func_SPI_Write_Total_PremiumSummary_Values(),"Error while writing Total Premium Summary values to excel . ");
	
	
	
	}catch(Throwable t){
		
		return false;
		
	}
	
	return true;
}

public boolean func_MTAPremiumSummary_New(Map<Object, Object> map_data,String code,String event){
	
	boolean r_value= true;
	String customPolicyDuration=null;
	String testName = (String)map_data.get("Automation Key");
	
	try{
	customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
	
	customPolicyDuration = k.getText("Policy_Duration");
	customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,map_data),"Error while writing MTA Policy Duration data to excel .");
	TestUtil.reportStatus("MTA Policy Duration = "+customPolicyDuration+" Days . ", "Info", true);
	
	if(common_PIA.isMTARewindFlow)
		TestUtil.reportStatus("---------------Premium Verification Started After Rewind Endorsement (MTA)-----------------","Info",false);
	else
		TestUtil.reportStatus("---------------Premium Verification Started After Endorsement (MTA)-----------------","Info",false);
	
	customAssert.assertTrue(Verify_premiumSummaryTable_New_MTA(), "Error while verifying Premium Summary table after MTA .");
	customAssert.assertTrue(func_MTATransactionDetailsPremiumTable_New(code, event), "Error while verifying Transaction Details table on premium Summary page .");
	Assert.assertTrue(funcTransactionDetailsMessage_MTA());
	
	TestUtil.reportStatus("Premium Summary details are filled and Verified sucessfully after Endorsement (MTA). ", "Info", true);
	
}catch(Throwable t){
	return false;
}
	return true;
}

public boolean func_MTATransactionDetailsPremiumTable_New(String code, String event){
	//Transaction Premium Table
	
		try{
			String testName = (String)common.MTA_excel_data_map.get("Automation Key");
			k.pressDownKeyonPage();
			customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
			
			int policy_Duration = Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"));
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
			
			if(common_PIA.isMTARewindFlow){
				common.transaction_Details_Premium_Values.clear();
			}
			
			if(transactionDetails_Table.isDisplayed()){
				
				if(common_PIA.isMTARewindFlow)
					TestUtil.reportStatus("Verification Started for Transaction Details table on premium summary page after Endorsement(MTA) Rewind . ", "Info", true);
				else
					TestUtil.reportStatus("Verification Started for Transaction Details table on premium summary page  . ", "Info", true);
				//For Each Cover Row
				for(int row = 1; row < trans_tble_Rows ;row ++){
					
					WebElement sec_Name = driver.findElement(By.xpath(transactionDetailsTble_xpath+"//tbody//tr["+row+"]//td["+1+"]"));
					sectionName = sec_Name.getText();
					
					switch(sectionName){
					
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
					
					}
					
				}
				//System.out.println(transaction_Premium_Values);
				if(common_PIA.isMTARewindFlow){
					TestUtil.reportStatus("---------------Transaction Details table Verification after Rewind Endorsement(MTA)-----------------","Info",false);
				}else{
					TestUtil.reportStatus("---------------Transaction Details table Verification-----------------","Info",false);
				}
				//Transaction table Verification
				for(int row = 1; row < trans_tble_Rows ;row ++){
					WebElement sec_Name = driver.findElement(By.xpath(transactionDetailsTble_xpath+"//tbody//tr["+row+"]//td["+1+"]"));
					sectionNames.add(sec_Name.getText());
				}
				for(String s_Name : sectionNames){
					if(s_Name.equals("Totals"))
						trans_error_val = trans_error_val + funcTransactionDetailsTable_Verification_Total_MTA(sectionNames,common.transaction_Details_Premium_Values);
					else
						trans_error_val = trans_error_val + funcTransactionDetailsTable_Verification_New_MTA(policy_Duration,s_Name,common.transaction_Details_Premium_Values);
					
			
				}
				if(common_PIA.isMTARewindFlow){
					TestUtil.reportStatus("Transaction Details table has been verified suceesfully after Rewind Endorsement . ", "info", true);
				}else{
					TestUtil.reportStatus("Transaction Details table has been verified suceesfully . ", "info", true);
				}
				
			}
			
		
				//Total Premium With Admin Fees
				double total_premium_with_admin_fee = common.transaction_Details_Premium_Values.get("Totals").get("Gross Premium") + 
						common.transaction_Details_Premium_Values.get("Totals").get("Insurance Tax") +
						Double.parseDouble((String)common.MTA_excel_data_map.get("PS_TotalAdminFee"));
				
				
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

public int funcTransactionDetailsTable_Verification_New_MTA(int policy_Duration,String sectionName,Map<String,Map<String,Double>> transactionDetails_Premium_Values){

	Map<Object,Object> map_data = common.MTA_excel_data_map;
	Map<Object,Object> NB_map_data = common.NB_excel_data_map;
	//String testName = (String)map_data.get("Automation Key");
	double NB_PI_NNP = 0.0;
	double NB_SEL_NNP = 0.0,MTA_SEL_NNP=0.0;
	double trans_NetNetP = 0.0,previous_mta=0.0,annualize_mta=0.0,_annualize_mta=0.0,final_trans_NNP=0.0;
	String code=null;
	int p_NB_Duration = 0,p_MTA_Remaining_Duration=0;
	if(Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"))!=365)
		p_NB_Duration = 365;
	else
		p_NB_Duration = Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"));
	
	p_MTA_Remaining_Duration = Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"));
		
	switch(sectionName){
	case "Solicitors PI":
		code = "PI";
		break;
	case "Solicitors excess layer":
		code = "SEL";
		break;
	default:
			System.out.println("**Cover Name is not in Scope for SPI**");
		break;
	
	}
	
try{
		
		TestUtil.reportStatus("---------------"+sectionName+"-----------------","Info",false);
		
			if(Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"))!=Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration"))){
			
							
				if(sectionName.contains("PI")){
					
					if(((String)common.NB_excel_data_map.get("CD_SolicitorsPI")).equals("Yes") && ((String)common.MTA_excel_data_map.get("CD_SolicitorsPI")).equals("No"))
					{
						NB_PI_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_PI_NetNetPremium"));
							
					}else if(((String)common.NB_excel_data_map.get("CD_SolicitorsPI")).equals("No") && ((String)common.MTA_excel_data_map.get("CD_SolicitorsPI")).equals("Yes")){
						NB_PI_NNP = 0.0;
					}else{
						NB_PI_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_PI_NetNetPremium"));
					}
					//Previous Premium MTA Calculation
					
					trans_NetNetP = ((NB_PI_NNP)/p_NB_Duration)*p_MTA_Remaining_Duration;
					previous_mta = NB_PI_NNP - trans_NetNetP;
					annualize_mta = ((Double.parseDouble((String)common.MTA_excel_data_map.get("PS_PI_NetNetPremium")) * (Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) ))/(Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"))));
					_annualize_mta = (annualize_mta/Double.parseDouble((String)common.MTA_excel_data_map.get("PS_Duration"))) * ((Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) - p_MTA_Remaining_Duration));
					final_trans_NNP = _annualize_mta - previous_mta;
					
				}else{
					//Previous Premium MTA Calculation
					if(((String)common.NB_excel_data_map.get("CD_SolicitorsExcessLayer")).equals("Yes") && ((String)common.MTA_excel_data_map.get("CD_SolicitorsExcessLayer")).equals("No"))
					{
						NB_SEL_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_SEL_NetNetPremium"));
					
					}else if(((String)common.NB_excel_data_map.get("CD_SolicitorsExcessLayer")).equals("No") && ((String)common.MTA_excel_data_map.get("CD_SolicitorsExcessLayer")).equals("Yes")){
						NB_SEL_NNP = 0.0;
					}else{
						NB_SEL_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_SEL_NetNetPremium"));
					}
				
					
					trans_NetNetP = ((NB_SEL_NNP)/p_NB_Duration)*p_MTA_Remaining_Duration;
					previous_mta = NB_SEL_NNP - trans_NetNetP;
					annualize_mta = ((Double.parseDouble((String)common.MTA_excel_data_map.get("PS_SEL_NetNetPremium")) * (Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) ))/(Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration"))));
					_annualize_mta = (annualize_mta/Double.parseDouble((String)common.MTA_excel_data_map.get("PS_Duration"))) * ((Integer.parseInt((String)common.MTA_excel_data_map.get("PS_Duration")) - p_MTA_Remaining_Duration));
					final_trans_NNP = _annualize_mta - previous_mta;	
				}
			}else{
				
				if(sectionName.contains("PI")){
					if(((String)common.NB_excel_data_map.get("CD_SolicitorsPI")).equals("Yes") && ((String)common.MTA_excel_data_map.get("CD_SolicitorsPI")).equals("No"))
					{
						NB_PI_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_PI_NetNetPremium"));
							
					}else if(((String)common.NB_excel_data_map.get("CD_SolicitorsPI")).equals("No") && ((String)common.MTA_excel_data_map.get("CD_SolicitorsPI")).equals("Yes")){
						NB_PI_NNP = 0.0;
					}else{
						NB_PI_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_PI_NetNetPremium"));
					}
					
					final_trans_NNP = ((Double.parseDouble((String)common.MTA_excel_data_map.get("PS_PI_NetNetPremium")) - NB_PI_NNP)/p_NB_Duration)*
							((Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
					
				}else{
					if(((String)common.NB_excel_data_map.get("CD_SolicitorsExcessLayer")).equals("Yes") && ((String)common.MTA_excel_data_map.get("CD_SolicitorsExcessLayer")).equals("No"))
					{
						NB_SEL_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_SEL_NetNetPremium"));
						MTA_SEL_NNP = 0.0;
					}else if(((String)common.NB_excel_data_map.get("CD_SolicitorsExcessLayer")).equals("No") && ((String)common.MTA_excel_data_map.get("CD_SolicitorsExcessLayer")).equals("Yes")){
						NB_SEL_NNP = 0.0;
						MTA_SEL_NNP = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_SEL_NetNetPremium"));
					}else{
						NB_SEL_NNP = Double.parseDouble((String)common.NB_excel_data_map.get("PS_SEL_NetNetPremium"));
						MTA_SEL_NNP = Double.parseDouble((String)common.MTA_excel_data_map.get("PS_SEL_NetNetPremium"));
					}	
					
					final_trans_NNP = ((MTA_SEL_NNP - NB_SEL_NNP)/p_NB_Duration)*
							((Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
				}
			}
		
		
		
		
		//double trans_NetNetP = ((Double.parseDouble((String)common.MTA_excel_data_map.get("PS_PI_NetNetPremium")) - NB_PI_NNP)/Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")))*
				//((Integer.parseInt((String)common.NB_excel_data_map.get("PS_Duration")) - Integer.parseInt((String)common.MTA_excel_data_map.get("MTA_EndorsementPeriod"))));
		
		//double trans_NetNetP = Double.parseDouble((String)map_data.get("PS_"+code+"_NetNetPremium")) - Double.parseDouble((String)NB_map_data.get("PS_"+code+"_NetNetPremium"));
		String t_NetNetP_expected = common.roundedOff(Double.toString(final_trans_NNP));
		String t_NetNetP_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Net Net Premium"));
		CommonFunction.compareValues(Double.parseDouble(t_NetNetP_expected),Double.parseDouble(t_NetNetP_actual)," Net Net Premium");
		
		double t_pen_comm = (( Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate"))/100)));
		String t_pc_expected = common.roundedOff(Double.toString(t_pen_comm));
		String t_pc_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Pen Comm"));
		CommonFunction.compareValues(Double.parseDouble(t_pc_expected),Double.parseDouble(t_pc_actual)," Pen Commission");
		
		
		double t_netP = Double.parseDouble(t_pc_expected) + Double.parseDouble(t_NetNetP_expected);
		String t_netP_expected = common.roundedOff(Double.toString(t_netP));
		String t_netP_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Net Premium"));
		CommonFunction.compareValues(Double.parseDouble(t_netP_expected),Double.parseDouble(t_netP_actual),"Net Premium");
		
		
		double t_broker_comm = ((Double.parseDouble(t_NetNetP_expected) / (1-((Double.parseDouble((String)map_data.get("PS_"+code+"_PenComm_rate")) + Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate")))/100)))*((Double.parseDouble((String)map_data.get("PS_"+code+"_BrokerComm_rate"))/100)));
		String t_bc_expected = common.roundedOff(Double.toString(t_broker_comm));
		String t_bc_actual =  Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Broker Commission"));
		CommonFunction.compareValues(Double.parseDouble(t_bc_expected),Double.parseDouble(t_bc_actual),"Broker Commission");
		
		
		double t_grossP = Double.parseDouble(t_netP_expected) + Double.parseDouble(t_bc_expected);
		String t_grossP_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Gross Premium"));
		CommonFunction.compareValues(t_grossP,Double.parseDouble(t_grossP_actual)," Gross Premium");
		
		
		double t_InsuranceTax = (t_grossP * Double.parseDouble((String)map_data.get("PS_"+code+"_InsuranceTaxRate")))/100.0;
		t_InsuranceTax = Double.parseDouble(common.roundedOff(Double.toString(t_InsuranceTax)));
		String t_InsuranceTax_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Insurance Tax"));
		CommonFunction.compareValues(t_InsuranceTax,Double.parseDouble(t_InsuranceTax_actual),"Insurance Tax");
		
		//SPI  Transaction Total Premium verification : 
		double t_Premium = t_grossP + t_InsuranceTax;
		String t_p_expected = common.roundedOff(Double.toString(t_Premium));
		
		String t_p_actual = Double.toString(transactionDetails_Premium_Values.get(sectionName).get("Total Premium"));
		
		double premium_diff = Double.parseDouble(t_p_expected) - Double.parseDouble(t_p_actual);
		
		if(premium_diff<0.09 && premium_diff>-0.09){
			TestUtil.reportStatus("Total Premium [<b> "+t_p_expected+" </b>] matches with actual total premium [<b> "+t_p_actual+" </b>]as expected for "+sectionName+" in Transaction Details table .", "Pass", false);
			//customAssert.assertTrue(WriteDataToXl(TestBase.product+"_"+TestBase.businessEvent, "Premium Summary", testName, "PS_"+code+"_TotalPremium", p_expected,common.NB_excel_data_map),"Error while writing Total Premium for cover "+code+" to excel .");
			return 0;
		}else{
			TestUtil.reportStatus("<p style='color:red'> Mismatch in Expected Premium [<b> "+t_p_expected+"</b>] and Actual Premium [<b> "+t_p_actual+"</b>] for "+sectionName+" in Transaction Details table . </p>", "Fail", true);
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


public boolean funcMTARewindOperation_New(){
	
	boolean r_value= true;
	
	try{
			common_PIA.isMTARewindFlow = true;
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Solicitors PI"),"Issue while Navigating to Solicitors PI. ");
			//customAssert.assertTrue(func_MTA_Solicitors_PI(common.MTA_excel_data_map), "MTA Solicitors PI function is having issue(S) . ");
		
			if(((String)common.MTA_excel_data_map.get("CD_SolicitorsPI")).equals("Yes")){
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Solicitors PI"),"Issue while Navigating to Solicitors PI. ");
				customAssert.assertTrue(Solicitors_PI_MTA(common.MTA_excel_data_map), "Solicitors PI function is having issue(S) . ");
			}

			if(((String)common.MTA_excel_data_map.get("CD_SolicitorsExcessLayer")).equals("Yes")){
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Solicitors Excess Layer"),"Issue while Navigating to Solicitors Excess Layer. ");
				customAssert.assertTrue(func_Solicitors_ExcessLayer(common.MTA_excel_data_map),"Issue while Navigating to Solicitors Excess Layer page  . ");
			}
			
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(func_MTAPremiumSummary_New(common.MTA_excel_data_map,TestBase.product,TestBase.businessEvent), "Premium Summary function is having issue(S) . ");
		
			customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));

			if(TestBase.product.equals("SPI")){
				customAssert.assertTrue(k.Click("SPI_RewindOnCoverBtn"),"Error in Clikcing SPI Rewind On Cover Button . ");
			}
			if(TestBase.product.equals("PIA") ){
				try{
					driver.findElement(By.xpath("//*[contains(@id,'pia-rwd-')]")).click();
					//Assert.assertTrue(k.Click("PIA_RewindOnCoverBtn"));
				}catch(Throwable t){
					k.Click("SPI_RewindOnCoverBtn");
				}
				
			}
			customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			if(TestBase.product.equals("SPI") || TestBase.product.equals("PIA")){
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Endorsement On Cover (Unconfirmed)"), "Verify Policy Status (Endorsement On Cover (Unconfirmed)) function is having issue(S) . ");
				customAssert.assertTrue(common_PIA.func_Confirm_Policy(), "Error while changing SPI policy Status from Unconfirmed to Confirmed . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Endorsement On Cover"), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
			}else{
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Endorsement On Cover"), "Verify Policy Status (Endorsement On Cover) function is having issue(S) . ");
			}
		
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
}

// End oF CommonFunction_SPI

}
