package com.selenium.commonfiles.base;

import static com.selenium.commonfiles.util.TestUtil.WriteDataToXl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.selenium.commonfiles.util.ErrorInTestMethod;
import com.selenium.commonfiles.util.TestUtil;


public class CommonFunction_DOB extends TestBase
{
	public boolean isMTARewindFlow=false,isFPEntries=false,isMTARewindStarted=false, isNBRewindStarted = false, isNBRquoteStarted = false;
	public int actual_no_of_years=0,err_count=0,trans_error_val=0 , Can_returnP_Error=0;
	SimpleDateFormat df = new SimpleDateFormat();
	Properties DOB_Rater = OR.getORProperties();

	public void RenewalRewindFlow(String code,String event) throws ErrorInTestMethod
	{
		String testName = (String)common.Rewind_excel_data_map.get("Automation Key");
		try
		{			
			common.currentRunningFlow="Rewind";
			//common_HHAZ.CoversDetails_data_list.clear();
			String navigationBy = CONFIG.getProperty("NavigationBy");
			common_HHAZ.PremiumFlag = false;
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcRewind());
			
			TestUtil.reportStatus("<b> -----------------------Renewal Rewind flow started---------------------- </b>", "Info", false);
			
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
			customAssert.assertTrue(common.funcSearchPolicy(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Renewal Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
			
			customAssert.assertTrue(funcPolicyGeneral(common.Rewind_excel_data_map,code,event), "Policy Details function having issue .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts & Declarations.");
			customAssert.assertTrue(funcMaterialFactDeclaration(common.Rewind_excel_data_map), "Material Facts & Declarations function having issue.");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Directors & Officers"),"Issue while Navigating to Directors & Officers.");
			customAssert.assertTrue(funcDirectorsOfficers(common.Rewind_excel_data_map,code,event), "Directors & Officers function having issue.");
			
			String netnetPremium = funcGetTotalPremium(code,event);
			customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_D&O_NetNetPremium", netnetPremium, common.Rewind_excel_data_map), "Error while writing net net permium value to excel .");
			TestUtil.reportStatus("Total Premium on Directors & Officers page is [<b>"+netnetPremium+"</b>]", "Info", true);
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common_HHAZ.funcPremiumSummary(common.Rewind_excel_data_map,code,event), "Premium Summary function is having issue(S) . ");
			
		}
		catch(Throwable t)
		{
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
	}
	
	public void RenewalFlow(String code,String fileName) throws ErrorInTestMethod
	{
		String testName = (String)common.Renewal_excel_data_map.get("Automation Key");
		try
		{			
			common.currentRunningFlow="Renewal";
			//String event = "Renewal";
			String navigationBy = CONFIG.getProperty("NavigationBy");
		
			customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
			customAssert.assertTrue(common_CCJ.renewalSearchPolicyNEW(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Pending"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
			
			// This variable is initialized in common_CCJ.renewalSearchPolicyNEW function
			if(!common_HHAZ.isAssignedToUW)
			{ 
				customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
				customAssert.assertTrue(common_SPI.funcAssignPolicyToUW());
			}
			
			customAssert.assertTrue(common.funcButtonSelection("Send to Underwriter"));
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");	
			customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Submitted"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
			
			customAssert.assertTrue(funcPolicyGeneral(common.Renewal_excel_data_map,code,"Renewal"), "Policy Details function having issue .");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcMaterialFactDeclaration(common.Renewal_excel_data_map), "Material Facts & Declarations function having issue.");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Directors & Officers"),"Issue while Navigating to Directors & Officers.");
			customAssert.assertTrue(funcDirectorsOfficers(common.Renewal_excel_data_map,code,"Renewal"), "Directors & Officers function having issue.");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen.");
			customAssert.assertTrue(common_HHAZ.funcPremiumSummary(common.Renewal_excel_data_map,code,"Renewal"), "Premium Summary function is having issue(S) . ");
			Assert.assertTrue(common_HHAZ.funcStatusHandling(common.Renewal_excel_data_map,code,"Renewal"));
		
			TestUtil.reportTestCasePassed(testName);
		
		}
		catch(ErrorInTestMethod e) 
		{
			System.out.println("Error in New Business test method for MTA > "+testName);
			throw e;
		}
		catch(Throwable t)
		{
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}	
	}
	
	public void RewindFlow(String code,String event) throws ErrorInTestMethod
	{
		String testName = (String)common.Rewind_excel_data_map.get("Automation Key");
		try
		{			
			if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")) 
			{
				customAssert.assertTrue(common_EP.ExistingPolicyAlgorithm(common.Rewind_excel_data_map,(String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type"), (String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Status")), "Existing Policy Algorithm function is having issues. ");
			}
			else 
			{
				CommonFunction_HHAZ.AdjustedTaxDetails.clear();
				if(!common.currentRunningFlow.equalsIgnoreCase("Renewal") && !common.currentRunningFlow.equalsIgnoreCase("MTA"))
				{
					NewBusinessFlow(code,"NB");
				}
				
				//common_HHAZ.CoversDetails_data_list.clear();
				//common_CCD.MD_Building_Occupancies_list.clear();
				common_HHAZ.PremiumFlag = false;
			}
			
			common.currentRunningFlow="Rewind";
			String navigationBy = CONFIG.getProperty("NavigationBy");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcRewind());
			
			TestUtil.reportStatus("<b> -----------------------Rewind flow started---------------------- </b>", "Info", false);
			
			//Existing Policy search condition for Rewind on MTA or Rewind on NB.
			if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes"))
			{
				customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
				customAssert.assertTrue(common.funcSearchPolicy(common.MTA_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status (Endorsement Submitted (Rewind)) function is having issue(s).");
			}
			else
			{
				customAssert.assertTrue(common.funcMenuSelection("Policies", ""));
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				if(TestBase.businessEvent.equalsIgnoreCase("MTA"))
				{
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Endorsement Submitted (Rewind)"), "Verify Policy Status (Endorsement Submitted (Rewind)) function is having issue(s).");
				}
				else
				{
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.Rewind_excel_data_map,code,event,"Submitted (Rewind)"), "Verify Policy Status (Submitted (Rewind)) function is having issue(s).");
				}
			}
			
			customAssert.assertTrue(funcPolicyGeneral(common.Rewind_excel_data_map,code,event), "Policy Details function having issue.");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts & Declarations.");
			customAssert.assertTrue(funcMaterialFactDeclaration(common.Rewind_excel_data_map), "Material Facts & Declarations function having issue.");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Directors & Officers"),"Issue while Navigating to Directors & Officers.");
			customAssert.assertTrue(funcDirectorsOfficers(common.Rewind_excel_data_map,code,event), "Directors & Officers function having issue.");
			
			String netnetPremium = funcGetTotalPremium(code,event);
			customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_D&O_NetNetPremium", netnetPremium, common.Rewind_excel_data_map), "Error while writing net net permium value to excel .");
			TestUtil.reportStatus("Total Premium on Directors & Officers page is [<b>"+netnetPremium+"</b>]", "Info", true);
			
			customAssert.assertTrue(k.Click("DOB_DO_SaveRater_Button"), "Unabale to click on Save Button on Directors & Officers page");
						
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
			
		}
		catch(Throwable t)
		{
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
	}
	
	public void MTAFlow(String code,String event) throws ErrorInTestMethod
	{
		String testName = (String)common.MTA_excel_data_map.get("Automation Key");
		try
		{
			Map<Object, Object> data_map = new HashMap<>();
			switch(TestBase.businessEvent)
			{
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
			if(((String)common.MTA_excel_data_map.get("MTA_ExistingPolicy")).equalsIgnoreCase("Yes")) 
			{
				customAssert.assertTrue(common_EP.ExistingPolicyAlgorithm(common.MTA_excel_data_map , (String)common.MTA_excel_data_map.get("MTA_ExistingPolicy_Type"), (String)common.MTA_excel_data_map.get("MTA_ExistingPolicy_Status")), "Existing Policy Algorithm function is having issues. ");
			}
			else 
			{
				if(!common.currentRunningFlow.equalsIgnoreCase("Renewal"))
				{
					NewBusinessFlow(code,"NB");
				}
				/*common_CCD.MD_Building_Occupancies_list.clear();
				common_HHAZ.CoversDetails_data_list.clear();
				common_HHAZ.PremiumFlag = false;*/
			}

			common.currentRunningFlow="MTA";
			CommonFunction_HHAZ.AdjustedTaxDetails.clear();
			String navigationBy = CONFIG.getProperty("NavigationBy");
			
			common_HHAZ.CoversDetails_data_list= new ArrayList<String>();
			common_HHAZ.CoversDetails_data_list.add("Directors&Officers");
			
			TestUtil.reportStatus("<b> -----------------------MTA flow started---------------------- </b>", "Info", false);
			
			customAssert.assertTrue(common_CCD.funcCreateEndorsement(),"Error in Create Endorsement function . ");
			
			customAssert.assertTrue(funcPolicyGeneral(common.MTA_excel_data_map,code,event), "Policy Details function having issue .");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts"),"Issue while Navigating to Material Facts and Declarations . ");
			customAssert.assertTrue(funcMaterialFactDeclaration(common.MTA_excel_data_map), "MaterialFactsDeclerationPage function is having issue(S) . ");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Directors & Officers"),"Issue while Navigating to Directors & Officers.");
			customAssert.assertTrue(funcDirectorsOfficers(common.MTA_excel_data_map,code,event), "Directors & Officers function having issue.");
			
			String netnetPremium = funcGetTotalPremium(code,event);
			customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_D&O_NetNetPremium", netnetPremium, common.MTA_excel_data_map), "Error while writing net net permium value to excel .");
			TestUtil.reportStatus("Total Premium on Directors & Officers page is [<b>"+netnetPremium+"</b>]", "Info", true);
			
			customAssert.assertTrue(k.Click("DOB_DO_SaveRater_Button"), "Unabale to click on Save Button on Directors & Officers page");
						
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary.");

			customAssert.assertTrue(common_HHAZ.funcPremiumSummary_MTA(common.MTA_excel_data_map,code,"MTA"), "Premium Summary function is having issue(S) . ");
						
			//customAssert.assertTrue(common_HHAZ.funcStatusHandling(common.MTA_excel_data_map, code, "MTA"), "Status Handling function is having issue(s).");
			
			if(!TestBase.businessEvent.equalsIgnoreCase("Renewal"))
			{
				Assert.assertTrue(common_HHAZ.funcStatusHandling(common.MTA_excel_data_map,code,"MTA"));
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
	
	public void NewBusinessFlow(String code,String event) throws ErrorInTestMethod
	{
		String testName = (String)common.NB_excel_data_map.get("Automation Key");
		String navigationBy = CONFIG.getProperty("NavigationBy");
		common.currentRunningFlow = "NB";
		try
		{			
			customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
			customAssert.assertTrue(common.checkClient(common.NB_excel_data_map,code,event),"Unable to check Client.");
			customAssert.assertTrue(common.createNewQuote(common.NB_excel_data_map,code,event), "Unable to create new quote.");
			customAssert.assertTrue(common.selectLatestQuote(common.NB_excel_data_map,code,event), "Unable to select quote from table.");
			
			customAssert.assertTrue(funcPolicyGeneral(common.NB_excel_data_map,code,event), "Policy Details function having issue.");
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Previous Claims"),"Issue while Navigating to Previous Claims.");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Material Facts & Declarations"),"Issue while Navigating to Material Facts & Declarations.");
			customAssert.assertTrue(funcMaterialFactDeclaration(common.NB_excel_data_map), "Material Facts & Declarations function having issue.");
			
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Directors & Officers"),"Issue while Navigating to Directors & Officers.");
			customAssert.assertTrue(funcDirectorsOfficers(common.NB_excel_data_map,code,event), "Directors & Officers function having issue.");
			
			String netnetPremium = funcGetTotalPremium(code,event);
			customAssert.assertTrue(WriteDataToXl(code+"_"+common.currentRunningFlow, "Premium Summary", testName, "PS_D&O_NetNetPremium", netnetPremium, common.NB_excel_data_map), "Error while writing net net permium value to excel .");
			TestUtil.reportStatus("Total Premium on Directors & Officers page is [<b>"+netnetPremium+"</b>]", "Info", true);
			
			customAssert.assertTrue(k.Click("DOB_DO_SaveRater_Button"), "Unabale to click on Save Button on Directors & Officers page");
						
			customAssert.assertTrue(common.funcNextNavigateDecesionMaker(navigationBy,"Premium Summary"),"Issue while Navigating to Premium Summary.");
			customAssert.assertTrue(common_HHAZ.funcPremiumSummary(common.NB_excel_data_map, code, event), "Premium Summary function is having issue(s).");
			customAssert.assertTrue(common_HHAZ.funcStatusHandling(common.NB_excel_data_map, code, "NB"), "Status Handling function is having issue(s).");
			
			if(TestBase.businessEvent.equals("NB"))
			{
				customAssert.assertEquals(err_count,0,"Errors in premium calculations . ");
				customAssert.assertEquals(trans_error_val,0,"Errors in Transaction premium calculations . ");
				customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
				customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
				TestUtil.reportTestCasePassed(testName);
			}
		}
		catch(Throwable t)
		{
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
	}
	
	public String funcGetTotalPremium(String code, String event)
	{
		int premiumRowNum = driver.findElements(By.xpath("//*[@id='table0']/tbody/tr")).size();
		return driver.findElement(By.xpath("//*[@id='table0']/tbody/tr["+premiumRowNum+"]/td["+DOB_Rater.getProperty("DOB_BR_Table_TotalPremium")+"]/input")).getAttribute("value");
	}
	
	@SuppressWarnings("static-access")
	public void funcCheckBookRateTable(Map<Object, Object> map_data, String code, String event, String cover) throws NumberFormatException, Exception
	{
		double initial_rate = 0;
		double book_rate = 0;
		double book_premium = 0;
		double revised_premium = 0;
		double premium = 0;
		String onPage_initial_rate;
		String onPage_book_rate;
		String onPage_book_premium;
		String onPage_revised_premium; 
		String onPage_premium;
		int tech_adjust = 10; 
		int comm_adjust = 10;
		String onPage_DnO_Premium = "";
		String turnover = (String)map_data.get("PG_TurnOver");
		String indemnity_limit = (String)map_data.get("DO_IndemnityLimit");
		
		//get table row number for cover & is it selected 'yes'
		int rowNum = getCoverRowNum(cover, "//*[@id='table0']/tbody/tr")+1;
		String isCovered = driver.findElement(By.xpath("//*[@id='table0']/tbody/tr["+rowNum+"]/td["+DOB_Rater.getProperty("DOB_BR_Table_CoverRequired")+"]")).getText();
		
		if(isCovered.equalsIgnoreCase("") || isCovered.equalsIgnoreCase("Yes"))
		{	
			//get rater info from properties file
			switch(cover)
			{
				case "D & O":	initial_rate = getBookRate_DO(turnover, indemnity_limit);
								book_rate = initial_rate;
								book_premium = initial_rate;
								revised_premium = book_premium +((book_premium * tech_adjust)/100);
								premium = revised_premium +((revised_premium * comm_adjust)/100);
								break;
				case "Civil Fines and Penalties extension":	
															onPage_DnO_Premium = driver.findElement(By.xpath("//*[@id='table0']/tbody/tr[1]/td["+DOB_Rater.getProperty("DOB_BR_Table_Premium")+"]/input")).getAttribute("value");
															initial_rate = Double.parseDouble(onPage_DnO_Premium)*0.25;
															book_rate = initial_rate;
															book_premium = initial_rate;
															revised_premium = book_premium +((book_premium * tech_adjust)/100);
															premium = revised_premium +((revised_premium * comm_adjust)/100);															
															break;
															
				case "Entity Insurance extension":	
													onPage_DnO_Premium = driver.findElement(By.xpath("//*[@id='table0']/tbody/tr[1]/td["+DOB_Rater.getProperty("DOB_BR_Table_Premium")+"]/input")).getAttribute("value");
													initial_rate = Double.parseDouble(onPage_DnO_Premium)*0.25;
													book_rate = initial_rate;
													book_premium = initial_rate;
													revised_premium = book_premium +((book_premium * tech_adjust)/100);
													premium = revised_premium +((revised_premium * comm_adjust)/100);
													break;
				
			}
			
			if(!cover.equalsIgnoreCase("Flat Premium")) {
				// Initial Rate
				onPage_initial_rate = driver.findElement(By.xpath("//*[@id='table0']/tbody/tr["+rowNum+"]/td["+DOB_Rater.getProperty("DOB_BR_Table_InitialRate")+"]/input")).getAttribute("value");
				customAssert.assertTrue(common.compareValues(initial_rate, Double.parseDouble(onPage_initial_rate), cover+" Initial Rate"),"Initial Rate Mismatch on Directors & Officers Page.<br>"+cover+" expected Initial Rate [<b>"+Double.toString(initial_rate)+"</b>] Found [<b>"+onPage_initial_rate+"</b>]");
							
				//Book Rate
				onPage_book_rate = driver.findElement(By.xpath("//*[@id='table0']/tbody/tr["+rowNum+"]/td["+DOB_Rater.getProperty("DOB_BR_Table_BookRate")+"]/input")).getAttribute("value");
				customAssert.assertTrue(common.compareValues(book_rate, Double.parseDouble(onPage_book_rate), cover+" Book Rate"),"Book Rate Mismatch on Directors & Officers Page.<br>"+cover+" expected Book Rate [<b>"+Double.toString(book_rate)+"</b>] Found [<b>"+onPage_book_rate+"</b>]");
							
				//Book Premium
				onPage_book_premium = driver.findElement(By.xpath("//*[@id='table0']/tbody/tr["+rowNum+"]/td["+DOB_Rater.getProperty("DOB_BR_Table_BookPremium")+"]/input")).getAttribute("value");
				customAssert.assertTrue(common.compareValues(book_premium, Double.parseDouble(onPage_book_premium), cover+" Book Premium"),"Book Premium Mismatch on Directors & Officers Page.<br>"+cover+" expected Book Premium [<b>"+Double.toString(book_premium)+"</b>] Found [<b>"+onPage_book_premium+"</b>]");
							
				//Revised Premium
				driver.findElement(By.xpath("//*[@id='table0']/tbody/tr["+rowNum+"]/td["+DOB_Rater.getProperty("DOB_BR_Table_TechAdjust")+"]/input")).clear();
				driver.findElement(By.xpath("//*[@id='table0']/tbody/tr["+rowNum+"]/td["+DOB_Rater.getProperty("DOB_BR_Table_TechAdjust")+"]/input")).sendKeys(Integer.toString(comm_adjust));
				driver.findElement(By.xpath("//*[@id='table0']/tbody/tr["+rowNum+"]/td["+DOB_Rater.getProperty("DOB_BR_Table_RevisedPremium")+"]/input")).click();
				
				onPage_revised_premium = driver.findElement(By.xpath("//*[@id='table0']/tbody/tr["+rowNum+"]/td["+DOB_Rater.getProperty("DOB_BR_Table_RevisedPremium")+"]/input")).getAttribute("value");
				customAssert.assertTrue(common.compareValues(revised_premium, Double.parseDouble(onPage_revised_premium), cover+" Revised Premium"),"Revised Premium Mismatch on Directors & Officers Page.<br>"+cover+" expected Revised Premium [<b>"+Double.toString(revised_premium)+"</b>] Found [<b>"+onPage_revised_premium+"</b>]");
							
				//Premium
				driver.findElement(By.xpath("//*[@id='table0']/tbody/tr["+rowNum+"]/td["+DOB_Rater.getProperty("DOB_BR_Table_CommAdjust")+"]/input")).clear();
				driver.findElement(By.xpath("//*[@id='table0']/tbody/tr["+rowNum+"]/td["+DOB_Rater.getProperty("DOB_BR_Table_CommAdjust")+"]/input")).sendKeys(Integer.toString(tech_adjust));
				driver.findElement(By.xpath("//*[@id='table0']/tbody/tr["+rowNum+"]/td["+DOB_Rater.getProperty("DOB_BR_Table_RevisedPremium")+"]/input")).click();
				
				onPage_premium = driver.findElement(By.xpath("//*[@id='table0']/tbody/tr["+rowNum+"]/td["+DOB_Rater.getProperty("DOB_BR_Table_Premium")+"]/input")).getAttribute("value");
				customAssert.assertTrue(common.compareValues(premium, Double.parseDouble(onPage_premium), cover+" Premium"),"Premium Mismatch on Directors & Officers Page.<br>"+cover+" expected Premium [<b>"+Double.toString(premium)+"</b>] Found [<b>"+onPage_premium+"</b>]");
							
				customAssert.assertTrue(k.Click("DOB_DO_ApplyBookRate_Button"), "Unabale to click on Apply Book Rate Button");
			}else {
				TestUtil.reportStatus("<p style='color:red'>Flat Premium to be removed from all the cover screens - Defect is opened <b> [ SUP-1544 ] </b> </p>.", "Fail", true);
			}
			
			
		}
	}
	
	//private void doPercentAdjust(double percentage)
	
	private int getCoverRowNum(String coverName, String baseXpath)
	{
		int i;
		
		List<WebElement> elements = driver.findElements(By.xpath(baseXpath));
				
		for(i =0; i< elements.size(); i++)
		{
			WebElement we = elements.get(i);
			if(we.findElement(By.xpath(".//td[1]")).getText().trim().equalsIgnoreCase(coverName))
			{
				break;
			}
		}
		
		return i;
	}
	
	private String getCoverNames(String baseXpath)
	{
		int i;
		String coverNames="";
		
		List<WebElement> elements = driver.findElements(By.xpath(baseXpath));
				
		for(i =0; i< elements.size(); i++)
		{
			WebElement we = elements.get(i);
			if(i==0) coverNames = we.findElement(By.xpath(".//td[1]")).getText();
			else  coverNames = coverNames+","+we.findElement(By.xpath(".//td[1]")).getText();
		}
		
		return coverNames;
	}
	
	public boolean funcDirectorsOfficers(Map<Object, Object> map_data, String code, String event)
	{
		boolean retVal = true;
		
		
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
			case "Rewind":
				map_data = common.Rewind_excel_data_map;
				break;
			/*case "Requote":
				map_data = common.Requote_excel_data_map;
				break;
		*/
		}		
		
		try
		{
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_DO_PremiumRating", (String)map_data.get("DO_PremiumRating")),"Unable to select Directors & Officers value in Material Premium Rating field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_DO_PositiveNetWorth", (String)map_data.get("DO_PositiveNetWorth")),"Unable to select Directors & Officers value in Positive Net Worth field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_DO_NegativeNetWorth", (String)map_data.get("DO_NegativeNetWorth")),"Unable to select Directors & Officers value in Negative Net Worth field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_DO_AuditOpinion", (String)map_data.get("DO_AuditOpinion")),"Unable to select Directors & Officers value in Audit Opinion field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_DO_IndemnityLimit", (String)map_data.get("DO_IndemnityLimit")),"Unable to select Directors & Officers value in Indemnity Limit field.");
			
			customAssert.assertTrue(k.Input("DOB_DO_PollutionDefenceCost", (String)map_data.get("DO_PollutionDefenceCost")),"Unable to enter Directors & Officers value in Pollution Defence Cost field.");
			customAssert.assertTrue(k.Input("DOB_DO_AutoAcquisitionSize", (String)map_data.get("DO_AutoAcquisitionSize")),"Unable to enter Directors & Officers value in Automation Acquisition Size field.");
			
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_DO_CivilFines", (String)map_data.get("DO_CivilFines")),"Unable to select Directors & Officers value in Civil Fines field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_DO_EntityInsurance", (String)map_data.get("DO_EntityInsurance")),"Unable to select Directors & Officers value in Entity Insurance field.");
			
			customAssert.assertTrue(k.Click("DOB_DO_ApplyBookRate_Button"), "Unabale to click on Apply Book Rate Button");
			
			// read list of cover and extensions, if any
			String baseXpath = "//*[@id='table0']/tbody/tr";
			int totalCoverExtensions = driver.findElements(By.xpath(baseXpath)).size()-1; // exclude total row
			String[] coverNames = getCoverNames(baseXpath).split(",");
			
			for(int i=0; i<totalCoverExtensions; i++)
			{
				funcCheckBookRateTable(map_data,code,event, coverNames[i]);
			}			
			
			TestUtil.reportStatus("Entered all the details on <b>Directors & Officers</b> page.", "Info", true);
			
			k.waitTenSeconds();
		}
		catch(Throwable t)
		{
	        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
	        Assert.fail("Unable to do operation on Directors & Officers page. \n", t);
	        retVal = false;
		}
		
		return retVal;
	}
	
	public boolean funcMaterialFactDeclaration(Map<Object, Object> map_data) 
	{
		boolean retVal = true;
		try
		{	
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_MFD_Q1", (String)map_data.get("MFD_Q1")),"Unable to select value in Material Fact & Declaration field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_MFD_Q2", (String)map_data.get("MFD_Q2")),"Unable to select value in Material Fact & Declaration field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_MFD_Q3", (String)map_data.get("MFD_Q3")),"Unable to select value in Material Fact & Declaration field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_MFD_Q4", (String)map_data.get("MFD_Q4")),"Unable to select value in Material Fact & Declaration field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_MFD_Q5", (String)map_data.get("MFD_Q5")),"Unable to select value in Material Fact & Declaration field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_MFD_Q6", (String)map_data.get("MFD_Q6")),"Unable to select value in Material Fact & Declaration field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_MFD_Q7", (String)map_data.get("MFD_Q7")),"Unable to select value in Material Fact & Declaration field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_MFD_Q8", (String)map_data.get("MFD_Q8")),"Unable to select value in Material Fact & Declaration field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_MFD_Q9", (String)map_data.get("MFD_Q9")),"Unable to select value in Material Fact & Declaration field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_MFD_Q10", (String)map_data.get("MFD_Q10")),"Unable to select value in Material Fact & Declaration field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_MFD_Q11", (String)map_data.get("MFD_Q11")),"Unable to select value in Material Fact & Declaration field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_MFD_Q12", (String)map_data.get("MFD_Q12")),"Unable to select value in Material Fact & Declaration field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_MFD_Q13", (String)map_data.get("MFD_Q13")),"Unable to select value in Material Fact & Declaration field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_MFD_Q14", (String)map_data.get("MFD_Q14")),"Unable to select value in Material Fact & Declaration field.");
			customAssert.assertTrue(k.DropDownSelectionByVal("DOB_MFD_Q15", (String)map_data.get("MFD_Q15")),"Unable to select value in Material Fact & Declaration field.");
			
			customAssert.assertTrue(k.Click("DOB_MFC_Save_Button"));
			
			TestUtil.reportStatus("Entered all the details on <b>Material Fact & Declaration</b> page.", "Info", true);
			
			return retVal;
		}
		catch(Throwable t)
		{
	        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
	        Assert.fail("Unable to to do operation on Material Facts & Declarations page. \n", t);
	        retVal = false;
		}
		
		return retVal;
	}
	
	public boolean funcPolicyGeneral(Map<Object, Object> map_data, String code, String event) 
	{
		boolean retVal = true;
		
		try
		{	
			customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");
			customAssert.assertTrue(k.Input("COB_PG_InsuredName", (String)map_data.get("PG_InsuredName")), "Unable to enter value in Insured Name  field .");
			customAssert.assertTrue(k.Input("DOB_PG_CompanyStatus", (String)map_data.get("PG_CompanyStatus")), "Unable to enter value in Companty Status field .");
			customAssert.assertTrue(k.Input("DOB_PG_BusinessYear", (String)map_data.get("PG_BusinessYear")), "Unable to enter value in Business Established Year field .");
			customAssert.assertTrue(k.Input("COB_PG_TurnOver", (String)map_data.get("PG_TurnOver")), "Unable to enter value in Turnover field .");
			
			String[] geographical_Limits = ((String)map_data.get("PG_GeoLimit")).split(",");
			for(int i=0; i< geographical_Limits.length; i++)
			{
				customAssert.assertTrue(k.Click("DOB_PG_GeopraphyLimit"),"Error while Clicking Geographic Limit List object . ");
				k.waitTwoSeconds();
				k.DropDownSelection("DOB_PG_GeopraphyLimit_value", geographical_Limits[i]);
				
			}
			
			String[] Prof_Bodies = ((String)map_data.get("PG_InsuredDetails_Q1")).split(",");
			List<WebElement> ul_ele = driver.findElements(By.xpath("//ul"));
			for(String pBodies_limit : Prof_Bodies)
			{
				for(WebElement each_ul : ul_ele)
				{
					customAssert.assertTrue(k.Click("DOB_PG_InsuredDetails_Q1"),"Error while Clicking Professional Bodies List object . ");
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
			customAssert.assertTrue(k.Input("COB_PG_BusDesc", (String)map_data.get("PG_BusDesc")),	"Unable to enter value in Provided Details field .");
						
			// Select Trade Code :
			if(common.currentRunningFlow.equalsIgnoreCase("NB"))
			{
				String sValue = (String)map_data.get("PG_TCS_TradeCode_Button");
				String val_Trade = (String)map_data.get("PG_TCS_TradeCode");
				if(sValue.contains("Yes"))
				{
					customAssert.assertTrue(SelectTradeCode(val_Trade, "Policy Details" , 0,map_data),"Trade code selection function is having issue(S).");
				}
				
				// check added trade codes
				if(driver.findElement(By.xpath("//td[contains(text(), '"+val_Trade+"')]")).isDisplayed())
				{
					TestUtil.reportStatus("<b>"+val_Trade + "</b> Tradecode is added and displayed on Policy general screen", "Info", true);
				}			
				
				customAssert.assertTrue(common.funcPageNavigation("Policy General", ""),"Policy General page not loaded");	
			}
			
			TestUtil.reportStatus("Entered all the details on <b>Policy General</b> page .", "Info", true);
			
			k.Click("DOB_PG_Save_Button");			
		}
		catch(Throwable t) 
		{
	        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
	        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
	        Assert.fail("Unable to to do operation on policy General page. \n", t);
	        retVal = false;
		}
		
		return retVal;
	}
	
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

	
	public boolean SelectTradeCode(String tradeCodeValue , String pageName , int currentPropertyIndex,Map<Object, Object> map_data) 
	{		
		try
		{			
			customAssert.assertTrue(k.Click("DOB_PG_Btn_SelectTradeCode"), "Unable to click on Select Trade Code button in Policy Details .");
						
			String[] TradeCodes = tradeCodeValue.split(",");
			
			for(String s_TradeCode : TradeCodes)
			{
				//k.HighlightObject(xpathKey);
				WebElement link = driver.findElement(By.xpath("//*[text()='"+s_TradeCode+"']"));
				link.click();
	 			//driver.findElement(By.xpath("//a[contains(text(),'"+s_TradeCode+"')]")).click();
	 			
	 		}
			
			common.funcButtonSelection("Save");
			common.funcButtonSelection("exit (Save First)");
	 		
			return true;	 		
		}
		catch(Throwable t)
		{
			return false;
		}
	}
	
	public double getBookRate_DO(String turnover, String indemnity_limit)
	{
		String propertyName="";
		
		double turnoverValue=Double.parseDouble(turnover.replace(",",""));
		
		if(turnoverValue<10000001) propertyName="Turnover_LessThanEqual10Mil";
		else
		{
			if(turnoverValue<25000001) propertyName="Turnover_LessThanEqual25Mil";
			else
			{
				if(turnoverValue<50000001) propertyName="Turnover_LessThanEqual50Mil";
				else
				{
					if(turnoverValue<75000001) propertyName="Turnover_LessThanEqual75Mil";
					else return 0;					
				}
			}
		}
		switch(indemnity_limit)
		{		
			case "250,000":
				propertyName=propertyName+"_Indemnity250K";
				break;
				
			case "500,000":
				propertyName=propertyName+"_Indemnity500K";
				break;
				
			case "1,000,000":
				propertyName=propertyName+"_Indemnity1Mil";
				break;
				
			case "2,000,000":
				propertyName=propertyName+"_Indemnity2Mil";
				break;
				
			case "3,000,000":
				propertyName=propertyName+"_Indemnity3Mil";
				break;
				
			case "5,000,000":
				propertyName=propertyName+"_Indemnity5Mil";
				break;			
		}
				
		return Double.parseDouble(DOB_Rater.getProperty(propertyName));
	}
}