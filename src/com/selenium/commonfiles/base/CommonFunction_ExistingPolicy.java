package com.selenium.commonfiles.base;

import java.awt.AWTException;
import java.awt.Event;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.relevantcodes.extentreports.model.Test;
import com.selenium.commonfiles.util.ErrorInTestMethod;
import com.selenium.commonfiles.util.TestUtil;




public class CommonFunction_ExistingPolicy extends TestBase {
	
	public Map<Object, Object> Rewind_Underlying_data_map = null;
	public Map<Object, Object> Reinstate_data_map = new HashMap<>();
	public boolean isReinstateTablePresent = false;
	
	public boolean ExistingPolicyAlgorithm(Map<Object, Object>data_map , String type , String status){
		
		try{
			
			customAssert.assertTrue(searchExistingPolicy(data_map , type,status) , "Search Policy function is having issues.");
			customAssert.assertTrue(selectPolicy(type,status) , "Select Policy function is having issues.");
			if(!TestBase.product.equals("CMA")&&!TestBase.product.equals("DOB")&&!TestBase.product.equals("GTA")&&!TestBase.product.equals("GTB")&&!TestBase.product.equals("GTC")&&!TestBase.product.equals("GTD")){
				customAssert.assertTrue(coverDetailsUpdation(type,status) , "Cover details updation function is having issues.");
			}
			
			return true;
			
		}catch(Throwable t){
			return false;
		}
		
		
	}
	
	
	
	public boolean searchExistingPolicy(Map<Object, Object> eventMap , String type , String status) throws AWTException, InterruptedException, ErrorInTestMethod{
		
		boolean retvalue = true;
		String testName = (String)eventMap.get("Automation Key");
		try{
			
			customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
			k.ImplicitWaitOff();
			customAssert.assertTrue(k.DropDownSelection("Renewal_SearchType", type), "Unable to select Type on search page for Renewal Policies.");
			customAssert.assertTrue(k.DropDownSelection("Renewal_SearchStatus", status), "Unable to select Status on search page for Renewal Policies.");
			customAssert.assertTrue(k.DropDownSelection("Renewal_SearchProduct", TestBase.product), "Unable to select Prouct Code on search page for Renewal Policies.");
			k.ImplicitWaitOn();
			customAssert.assertTrue(k.Click("comm_search"), "Unable to click on search button.");
			
			TestUtil.reportStatus("Existing policy successfully searched for further operations.", "Info", false);
			
			return retvalue;
			
		}catch(Throwable t){
			TestUtil.reportTestCaseFailed(testName, t);
			throw new ErrorInTestMethod(t.getMessage());
		}
		
	    
	}
	

	
public boolean selectPolicy(String type , String status) throws AWTException, InterruptedException, ParseException{
	
	Map<Object, Object> data_map = null;
	Map<Object, Object> Underlying_data_map = null;
	String sheetName = "" , code = "";
	switch (type) {
	case "Endorsement":
	case "New Business":
		if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
			data_map = common.MTA_excel_data_map;
			Underlying_data_map = common.NB_excel_data_map;
			sheetName = "MTA";
			code = "NB";
		}else if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
			if(type.equalsIgnoreCase("Endorsement")){
				data_map = common.Rewind_excel_data_map;
				Underlying_data_map = common.MTA_excel_data_map;
				Rewind_Underlying_data_map = common.NB_excel_data_map;
				sheetName = "Rewind";
				code = "MTA";
			}else{
				data_map = common.Rewind_excel_data_map;
				Underlying_data_map = common.NB_excel_data_map;
				sheetName = "Rewind";
				code = "NB";
			}
		}else if(TestBase.businessEvent.equalsIgnoreCase("CAN")){
			data_map = common.CAN_excel_data_map;
			Underlying_data_map = common.NB_excel_data_map;
			sheetName = "CAN";
			code = "NB";
		}
		break;
	case "Renewal":
		data_map = common.MTA_excel_data_map;
		Underlying_data_map = common.Renewal_excel_data_map;
		sheetName = "MTA";
		break;
	}
	
	boolean retvalue = true;
	boolean flag = true;
	String PolicyNumber ="" ,duration="";  
	WebElement SearchedPolicyTable = driver.findElement(By.xpath("//*[@id='srch-update']//following::table[1]"));
	List<WebElement> rows = SearchedPolicyTable.findElements(By.tagName("tr"));
	int counter = 0;
	if(rows.size() >0){
		Outerloop:
		for(int i = 1; i < rows.size(); i++ ) {
			
			JavascriptExecutor j_exe = (JavascriptExecutor) driver;
			j_exe.executeScript("arguments[0].scrollIntoView(true);", SearchedPolicyTable.findElement(By.xpath("//tbody//tr["+i+"]")));
			
			String praposerName = SearchedPolicyTable.findElement(By.xpath("//tbody//tr["+i+"]//td[1]/a[1]")).getText();
			String AgencyName = SearchedPolicyTable.findElement(By.xpath("//tbody//tr["+i+"]//td[6]")).getText();
			String policy_type = SearchedPolicyTable.findElement(By.xpath("//tbody//tr["+i+"]//td[4]")).getText();
			String policy_status = SearchedPolicyTable.findElement(By.xpath("//tbody//tr["+i+"]//td[5]")).getText();
			
			if(policy_type.equalsIgnoreCase(type) && policy_status.equalsIgnoreCase(status)) {
				
				data_map.put(sheetName+"_ClientName", praposerName);
				data_map.put("QC_AgencyName", AgencyName);
				Underlying_data_map.put(code+"_ClientName", praposerName);
				Underlying_data_map.put("QC_AgencyName", AgencyName);
				
				SearchedPolicyTable.findElement(By.xpath("//tr["+i+"]//td[1]/a[1]")).click();
				String durationPath = "//td[text()='Duration (days)']//following-sibling::td//div";
				duration = driver.findElement(By.xpath(durationPath)).getText();
				PolicyNumber = k.getText("PremiumSummary_PolicyNumber");
				
				// Verification between Amendment period and Duration. 
				// Amendment period (Passed from data sheet) should not greate than searched policy duration.
				
				if(TestBase.businessEvent.equalsIgnoreCase("MTA") && type.equalsIgnoreCase("Endorsement")){
					int ammendmet_period = Integer.parseInt((String)data_map.get("MTA_EndorsementPeriod"));
					String transactionDetailsMsg_xpath = "//p[text()=' Transaction Details ']//following-sibling::p";
					WebElement transactionDetails_Msg = driver.findElement(By.xpath(transactionDetailsMsg_xpath));
					
					String text = transactionDetails_Msg.getText();
					
					String date[] = text.split(",");
					String days[] = text.split(",");
					
					if(ammendmet_period > (Integer.parseInt(days[1].substring(days[1].indexOf(":")+2, days[1].indexOf("days")).trim()))){
						TestUtil.reportStatus("<p style='color:blue'>Amendment period <b> [ "+ammendmet_period+" ] </b> for Policy : <b> ["+PolicyNumber+" ] </b> is greater than duration <b> [ "+duration+" ] </b> of policy. Searched new Policy.</p>", "Info", false);
						customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
						SearchedPolicyTable = driver.findElement(By.xpath("//*[@id='srch-update']//following::table[1]"));
						rows = SearchedPolicyTable.findElements(By.tagName("tr"));
						continue;
					}
					if(Integer.parseInt(days[1].substring(days[1].indexOf(":")+2, days[1].indexOf("days")).trim()) < 0){
						TestUtil.reportStatus("<p style='color:blue'>Effective days of searched Policy : <b> ["+PolicyNumber+" ] </b> is less than 0. Searching new Policy.</p>", "Info", false);
						customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
						SearchedPolicyTable = driver.findElement(By.xpath("//*[@id='srch-update']//following::table[1]"));
						rows = SearchedPolicyTable.findElements(By.tagName("tr"));
						continue;
					}
					/**
					 * 
					 *
					 *  To manage AP(Additional Premium) or RP(Reduced Premium) for MTA flow.
					 * 
					 * 
					 */
					boolean isCoverPresent = false;
					String AP_RP_Key = (String)common.MTA_excel_data_map.get("CD_AP_RP_CoverSpecific_Decision");
					
					if(!(AP_RP_Key.equalsIgnoreCase(""))){
						String[] AP_RP_Array = AP_RP_Key.split(",");
						
						
						for(String cover : AP_RP_Array){
							
							String[] splitCoverNameFormat = cover.split("-");
							
							/////////
							
							
							String annualPremiumTablePath = "//p[text()='Annual Premium ']//following-sibling::table[1]//tbody//tr";
							JavascriptExecutor j_exe1 = (JavascriptExecutor) driver;
							j_exe1.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(annualPremiumTablePath)));
							
							List<WebElement> listOfRows = driver.findElements(By.xpath(annualPremiumTablePath));
							int sizeOfAnnualPremiumTable = listOfRows.size();
							
							for(int coverDetails = 1;coverDetails<=sizeOfAnnualPremiumTable;coverDetails++){
								
								String CoverName = driver.findElement(By.xpath(annualPremiumTablePath+"["+coverDetails+"]//td[1]")).getText().replaceAll(" ", "");
								
								if(splitCoverNameFormat[1].equalsIgnoreCase(CoverName.replaceAll(" ", ""))){
									isCoverPresent=true;
									break;
								}else{
									isCoverPresent=false;
								}
							}
							
							if(!isCoverPresent){
								// Counter variable is used to restrict search policy up to 4 times only.
								if(counter!=4){
									TestUtil.reportStatus("<p style='color:blue'>Effective days of searched Policy : <b> ["+PolicyNumber+" ] </b> is less than 0. Searching new Policy.</p>", "Info", false);
									customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
									SearchedPolicyTable = driver.findElement(By.xpath("//*[@id='srch-update']//following::table[1]"));
									rows = SearchedPolicyTable.findElements(By.tagName("tr"));
									counter++;
									continue Outerloop;
								}else{
									common.MTA_excel_data_map.put("CD_AP_RP_CoverSpecific_Decision","");
									break;
								}
								
							}
							
							/////////
							
						}
					}
					
				}
				if(TestBase.businessEvent.equalsIgnoreCase("MTA") && type.equalsIgnoreCase("New Business")){
					int ammendmet_period = Integer.parseInt((String)data_map.get("MTA_EndorsementPeriod"));
					
					if(ammendmet_period > (Integer.parseInt(duration))){
						TestUtil.reportStatus("<p style='color:blue'>Amendment period <b> [ "+ammendmet_period+" ] </b> for Policy : <b> ["+PolicyNumber+" ] </b> is greater than duration <b> [ "+duration+" ] </b> of policy. Searched new Policy.</p>", "Info", false);
						customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
						SearchedPolicyTable = driver.findElement(By.xpath("//*[@id='srch-update']//following::table[1]"));
						rows = SearchedPolicyTable.findElements(By.tagName("tr"));
						continue;
					}
					
					/**
					 * 
					 *
					 *  To manage AP(Additional Premium) or RP(Reduced Premium) for MTA flow.
					 * 
					 * 
					 */
					boolean isCoverPresent = false;
					String AP_RP_Key = (String)common.MTA_excel_data_map.get("CD_AP_RP_CoverSpecific_Decision");
					
					if(!(AP_RP_Key.equalsIgnoreCase(""))){
						String[] AP_RP_Array = AP_RP_Key.split(",");
						
						
						for(String cover : AP_RP_Array){
							
							String[] splitCoverNameFormat = cover.split("-");
							
							/////////
							
							String annualPremiumTablePath = "//p[text()='Annual Premium ']//following-sibling::table[1]//tbody//tr";
							JavascriptExecutor j_exe1 = (JavascriptExecutor) driver;
							j_exe1.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(annualPremiumTablePath)));
							
							List<WebElement> listOfRows = driver.findElements(By.xpath(annualPremiumTablePath));
							int sizeOfAnnualPremiumTable = listOfRows.size();
							
							for(int coverDetails = 1;coverDetails<=sizeOfAnnualPremiumTable;coverDetails++){
								
								String CoverName = driver.findElement(By.xpath(annualPremiumTablePath+"["+coverDetails+"]//td[1]")).getText().replaceAll(" ", "");
								
								if(splitCoverNameFormat[1].equalsIgnoreCase(CoverName.replaceAll(" ", ""))){
									isCoverPresent=true;
									break;
								}else{
									isCoverPresent=false;
								}
							}
							
							if(!isCoverPresent){
								// Counter variable is used to restrict search policy up to 4 times only.
								if(counter!=4){
									TestUtil.reportStatus("<p style='color:blue'>Effective days of searched Policy : <b> ["+PolicyNumber+" ] </b> is less than 0. Searching new Policy.</p>", "Info", false);
									customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
									SearchedPolicyTable = driver.findElement(By.xpath("//*[@id='srch-update']//following::table[1]"));
									rows = SearchedPolicyTable.findElements(By.tagName("tr"));
									counter++;
									continue Outerloop;
								}else{
									common.MTA_excel_data_map.put("CD_AP_RP_CoverSpecific_Decision","");
									break;
								}
								
							}
							
							/////////
							
						}
					}
					
					//For Skipping Reinstatement Policies
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker("Navigate","History"),"Issue while Navigating to History . ");
					
					WebElement tableID = driver.findElement(By.xpath("//*[@id='linkpanels']//following::table[1]//tbody"));
					List<WebElement> HistoryRows = tableID.findElements(By.tagName("tr"));
					int size = HistoryRows.size();
					boolean isReinstament=false;
					for(int j=1;j<=size;j++){
						
						String EventName = "";
						
						try{
							k.ImplicitWaitOff();
							EventName = driver.findElement(By.xpath("//*[@id='linkpanels']//following::table[1]//tbody//tr["+j+"]//td[1]//a")).getText();
							k.ImplicitWaitOn();
						}catch(Throwable t){
							k.ImplicitWaitOff();
							EventName = driver.findElement(By.xpath("//*[@id='linkpanels']//following::table[1]//tbody//tr["+j+"]//td[1]")).getText();
							k.ImplicitWaitOn();
						}
							
						if(EventName.contains("Reinstat")){
							isReinstament=true;
							break;
						}
						
					}
					if(isReinstament){
						TestUtil.reportStatus("<p style='color:blue'>Searched policy has Reinstament event in it . Searching next policy . </p>", "Info", false);
						customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
						SearchedPolicyTable = driver.findElement(By.xpath("//*[@id='srch-update']//following::table[1]"));
						rows = SearchedPolicyTable.findElements(By.tagName("tr"));
						continue;
					}else{
						customAssert.assertTrue(common.funcNextNavigateDecesionMaker("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary . ");
					}
						
				}
				if(TestBase.businessEvent.equalsIgnoreCase("CAN")){
					String endDatePath = "//td[text()='Policy End Date (dd/mm/yyyy) ']//following-sibling::td";
					String endDate = driver.findElement(By.xpath(endDatePath)).getText();
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					
					int dateDif = Integer.parseInt((String)common.CAN_excel_data_map.get("CP_AddDifference"));
					Date c_date = df.parse(common.getUKDate());
		    		
					String Cancellation_date = common.daysIncrementWithOutFormation(df.format(c_date), dateDif);
					
					Date date1 = df.parse(endDate);
		            Date date2 = df.parse(Cancellation_date);
		            
		            if(date2.after(date1)){
		            	TestUtil.reportStatus("<p style='color:blue'>Cancellation is done on Post inception date <b> [ "+Cancellation_date+" ] </b> for Policy : <b> ["+PolicyNumber+" ] </b> which is greater than Policy end date <b> [ "+endDate+" ] </b>.</p>", "Info", false);
						customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
						SearchedPolicyTable = driver.findElement(By.xpath("//*[@id='srch-update']//following::table[1]"));
						rows = SearchedPolicyTable.findElements(By.tagName("tr"));
						continue;
		            }
					
		            //For Skipping Reinstatement Policies
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker("Navigate","History"),"Issue while Navigating to History . ");
					
					WebElement tableID = driver.findElement(By.xpath("//*[@id='linkpanels']//following::table[1]//tbody"));
					List<WebElement> HistoryRows = tableID.findElements(By.tagName("tr"));
					int size = HistoryRows.size();
					boolean isReinstament=false;
					for(int j=1;j<=size;j++){
						
						String EventName = "";
						
						try{
							k.ImplicitWaitOff();
							EventName = driver.findElement(By.xpath("//*[@id='linkpanels']//following::table[1]//tbody//tr["+j+"]//td[1]//a")).getText();
							k.ImplicitWaitOn();
						}catch(Throwable t){
							k.ImplicitWaitOff();
							EventName = driver.findElement(By.xpath("//*[@id='linkpanels']//following::table[1]//tbody//tr["+j+"]//td[1]")).getText();
							k.ImplicitWaitOn();
						}
							
						if(EventName.contains("Reinstat")){
							isReinstament=true;
							break;
						}
						
					}
					if(isReinstament){
						TestUtil.reportStatus("<p style='color:blue'>Searched policy has Reinstament event in it . Searching next policy . </p>", "Info", false);
						customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
						SearchedPolicyTable = driver.findElement(By.xpath("//*[@id='srch-update']//following::table[1]"));
						rows = SearchedPolicyTable.findElements(By.tagName("tr"));
						continue;
					}else{
						customAssert.assertTrue(common.funcNextNavigateDecesionMaker("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary . ");
					}
				}
				if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
					
					String startDatePath = "//td[text()='Policy Start Date (dd/mm/yyyy) ']//following-sibling::td";
					String startDate = driver.findElement(By.xpath(startDatePath)).getText();
					int RewindButtonCheckDays = Integer.parseInt(CONFIG.get("RewindButtonDurration").toString());
					String FORMAT = "dd/MM/yyyy";
					SimpleDateFormat sd = new SimpleDateFormat(FORMAT);
					Date currentDate = new Date();
					
					int daysDiff = Integer.parseInt(common.DateDiff(sd.format(currentDate), startDate));
					
					if(daysDiff > RewindButtonCheckDays){
						TestUtil.reportStatus("<p style='color:blue'>Rewind button is not present for Policy : <b> ["+PolicyNumber+" ] </b> because the differance between start date <b> [ "+startDate+" ] </b> and today's date <b> [ "+sd.format(currentDate)+" ] </b> is greater than duration <b> [ 55 ] </b> days. Searched new Policy.</p>", "Info", false);
						customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
						SearchedPolicyTable = driver.findElement(By.xpath("//*[@id='srch-update']//following::table[1]"));
						rows = SearchedPolicyTable.findElements(By.tagName("tr"));
						continue;
		            }
					
					customAssert.assertTrue(common.funcNextNavigateDecesionMaker("Navigate","History"),"Issue while Navigating to History . ");
					boolean isReinstate = false;
					WebElement tableID = driver.findElement(By.xpath("//*[@id='linkpanels']//following::table[1]//tbody"));
					List<WebElement> HistoryRows = tableID.findElements(By.tagName("tr"));
					int size = HistoryRows.size();
					
					for(int j=1;j<=size;j++){
						
						String EventName = "";
						
						try{
							k.ImplicitWaitOff();
							EventName = driver.findElement(By.xpath("//*[@id='linkpanels']//following::table[1]//tbody//tr["+j+"]//td[1]//a")).getText();
							k.ImplicitWaitOn();
						}catch(Throwable t){
							k.ImplicitWaitOff();
							EventName = driver.findElement(By.xpath("//*[@id='linkpanels']//following::table[1]//tbody//tr["+j+"]//td[1]")).getText();
							k.ImplicitWaitOn();
						}
						
						
						if(EventName.equalsIgnoreCase("Reinstated New Business")){
							isReinstate = true;
							break;
						}
						
					}
					
					if(isReinstate){
						TestUtil.reportStatus("<p style='color:blue'>Rewind button is not present for Policy : <b> ["+PolicyNumber+" ] </b> because this policy is reinsted Policy.</p>", "Info", false);
						customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
						SearchedPolicyTable = driver.findElement(By.xpath("//*[@id='srch-update']//following::table[1]"));
						rows = SearchedPolicyTable.findElements(By.tagName("tr"));
						continue;
					}else{
						customAssert.assertTrue(common.funcNextNavigateDecesionMaker("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary . ");
					}
					
		        }
				TestUtil.reportStatus("Existing Policy algorithm applied for Product : <b> [ "+TestBase.product+" ] </b> and selected Policy is : <b> [ "+PolicyNumber+" ] </b>.", "Info", false);
				flag = true;
				break;
			}else{
				flag = false;
			}
		}
		// UpdatePremiumSummaryData : This function will update all necessary details from premium summary to NB and MTA map.
		if(flag){
			TestUtil.reportStatus("Policy has been identified with Status as <b> [ "+type+" & "+status+" ] </b> for product <b> [ "+TestBase.product+" ]", "Info", false);
			customAssert.assertTrue(updatePremiumSummaryData(PolicyNumber,duration,type) , "Update Premium Summary Values function is having issues.");
			return retvalue;
		}else{
			TestUtil.reportStatus("<p style='color:red'>No policies are present with Status as <b> [ "+type+" & "+status+" ] </b> for product <b> [ "+TestBase.product+" ] </b> OR pre-requistis conditions are not matching to proceed further.</p>", "Info", false);
			return false;
		}
	}else if(common_HHAZ.is_Pagination_enabled()){
			WebElement btn_next = driver.findElement(By.xpath("//*[@id='mainpanel']//a[contains(text(),'next')]"));
			btn_next.click(); //for  Pagination purpose
			retvalue = true;
			selectPolicy(type,status);
			
	}else{
		TestUtil.reportStatus("No policies are present with Status as <b> [ "+type+" & "+status+" ] </b> for product <b> [ "+TestBase.product+" ] </b>", "Fail", true);
		return false;
	}
	
	return retvalue;
	
    
}

public boolean updatePremiumSummaryData(String PolicyNumber , String duration , String type) throws AWTException, InterruptedException{
	
	Map<Object, Object> data_map = null;
	Map<Object, Object> Underlying_data_map = null;
	
	
	String sheetName = "" , code = "";
	switch (type) {
	case "Endorsement":
	case "New Business":
		if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
			data_map = common.MTA_excel_data_map;
			Underlying_data_map = common.NB_excel_data_map;
			sheetName = "MTA";
			code="NB";
		}else if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
			if(type.equalsIgnoreCase("Endorsement")){
				data_map = common.Rewind_excel_data_map;
				Underlying_data_map = common.MTA_excel_data_map;
				Rewind_Underlying_data_map = common.NB_excel_data_map;
				sheetName = "Rewind";
				code = "MTA";
			}else{
				data_map = common.Rewind_excel_data_map;
				Underlying_data_map = common.NB_excel_data_map;
				sheetName = "Rewind";
				code = "NB";
			}
		}else if(TestBase.businessEvent.equalsIgnoreCase("CAN")){
			data_map = common.CAN_excel_data_map;
			Underlying_data_map = common.NB_excel_data_map;
			sheetName = "CAN";
			code = "NB";
		}
		break;
	case "Renewal":
		data_map = common.MTA_excel_data_map;
		Underlying_data_map = common.Renewal_excel_data_map;
		sheetName = "MTA";
		break;
	}
	
	boolean retvalue = true;
   
	String QuoteNumber = k.getText("POF_QuoteNumber");
	String startDatePath = "//td[text()='Policy Start Date (dd/mm/yyyy) ']//following-sibling::td";
	String startDate = driver.findElement(By.xpath(startDatePath)).getText();
	String endDatePath = "//td[text()='Policy End Date (dd/mm/yyyy) ']//following-sibling::td";
	String endDate = driver.findElement(By.xpath(endDatePath)).getText();
	String taxExemptionPath = "";
	
	//For GTB product, Tax Exempt is displayed as non editable textbox - bug id is - PRD-14483 
	if(TestBase.product.contains("GTB")){
		taxExemptionPath = "//td[text()='Tax Exempt?']//following-sibling::td//div";
	}else{
		taxExemptionPath = "//td[text()='Is this policy exempt from insurance tax?']//following-sibling::td//div";
	}
	
	String taxExemption = driver.findElement(By.xpath(taxExemptionPath)).getText();
	String policyFinacePath = "//td[text()='Is the policy financed?']//following-sibling::td//div";
	String policyFinace = driver.findElement(By.xpath(policyFinacePath)).getText();
	String paymentWarrentyPath = "//td[text()='Is this business conducted under Premium Payment Warranty rules?']//following-sibling::td//div";
	String paymentWarrenty = driver.findElement(By.xpath(paymentWarrentyPath)).getText();
	
	if(paymentWarrenty.equalsIgnoreCase("Yes")){
		String paymentWarrentyDueDatePath = "//td[text()='Policy Start Date (dd/mm/yyyy) ']//following-sibling::td";
		String paymentWarrentyDueDate = k.getText("startDatePath");
	}
	
	Underlying_data_map.put("PS_PolicyStartDate", startDate);
	Underlying_data_map.put("PS_PolicyEndDate", endDate);
	Underlying_data_map.put(code+"_PolicyNumber", PolicyNumber);
	data_map.put("MTA_PolicyNumber", PolicyNumber);
	Underlying_data_map.put(code+"_QuoteNumber", QuoteNumber);
	data_map.put("MTA_QuoteNumber", QuoteNumber);
	Underlying_data_map.put("PS_Duration", duration);
	Underlying_data_map.put("PS_TaxExempt", taxExemption);
	Underlying_data_map.put("PS_IsPolicyFinanced", policyFinace);
	Underlying_data_map.put("PS_PaymentWarrantyRules", paymentWarrenty);
	
	/**
	 * 
	 * Store Premium Summary table values :  
	 * 
	 */
	
	String annualPremiumTablePath = "//p[text()='Annual Premium ']//following-sibling::table[1]//tbody//tr";
	List<WebElement> listOfRows = driver.findElements(By.xpath(annualPremiumTablePath));
	int sizeOfAnnualPremiumTable = listOfRows.size();
	
	for(int coverDetails = 1;coverDetails<=sizeOfAnnualPremiumTable;coverDetails++){
		
		String CoverName = driver.findElement(By.xpath(annualPremiumTablePath+"["+coverDetails+"]//td[1]")).getText().replaceAll(" ", "");
		String NetNetPremium = driver.findElement(By.xpath(annualPremiumTablePath+"["+coverDetails+"]//td[2]")).getText();
		String PenCommRate = driver.findElement(By.xpath(annualPremiumTablePath+"["+coverDetails+"]//td[3]")).getText();
		String PenCommision = driver.findElement(By.xpath(annualPremiumTablePath+"["+coverDetails+"]//td[4]")).getText();
		String NetPremium = driver.findElement(By.xpath(annualPremiumTablePath+"["+coverDetails+"]//td[5]")).getText();
		String BrokerCommRate = driver.findElement(By.xpath(annualPremiumTablePath+"["+coverDetails+"]//td[6]")).getText();
		String BrokerCommision = driver.findElement(By.xpath(annualPremiumTablePath+"["+coverDetails+"]//td[7]")).getText();
		String GrossCommRate = driver.findElement(By.xpath(annualPremiumTablePath+"["+coverDetails+"]//td[8]")).getText();
		String GrossPremium = driver.findElement(By.xpath(annualPremiumTablePath+"["+coverDetails+"]//td[9]")).getText();
		String InsuranceTaxRate = driver.findElement(By.xpath(annualPremiumTablePath+"["+coverDetails+"]//td[10]")).getText();
		String InsuranceTax = driver.findElement(By.xpath(annualPremiumTablePath+"["+coverDetails+"]//td[11]")).getText();
		String TotalPremium = driver.findElement(By.xpath(annualPremiumTablePath+"["+coverDetails+"]//td[12]")).getText();
		
		if(CoverName.equalsIgnoreCase("Totals")){
			Underlying_data_map.put("PS_NetNetPremiumTotal", NetNetPremium);
			Underlying_data_map.put("PS_PenCommTotal", PenCommision);
			Underlying_data_map.put("PS_NetPremiumTotal", NetPremium);
			Underlying_data_map.put("PS_BrokerCommissionTotal", BrokerCommision);
			Underlying_data_map.put("PS_Total_GP", GrossPremium);
			Underlying_data_map.put("PS_Total_GT", InsuranceTax);
			Underlying_data_map.put("PS_TotalPremium", TotalPremium);
		}else{
			if(CoverName.contains("Businesss")){
				CoverName = "BusinessInterruption";
			}
			Underlying_data_map.put("PS_"+CoverName+"_NetNetPremium", NetNetPremium);
			Underlying_data_map.put("PS_"+CoverName+"_PenComm_rate", PenCommRate);
			Underlying_data_map.put("PS_"+CoverName+"_PenComm", PenCommision);
			Underlying_data_map.put("PS_"+CoverName+"_NetPremium", NetPremium);
			Underlying_data_map.put("PS_"+CoverName+"_BrokerComm_rate", BrokerCommRate);
			Underlying_data_map.put("PS_"+CoverName+"_BrokerComm", BrokerCommision);
			Underlying_data_map.put("PS_"+CoverName+"_GrossComm_rate", GrossCommRate);
			Underlying_data_map.put("PS_"+CoverName+"_GP", GrossPremium);
			Underlying_data_map.put("PS_"+CoverName+"_IPT", InsuranceTaxRate);
			Underlying_data_map.put("PS_"+CoverName+"_GT", InsuranceTax);
			Underlying_data_map.put("PS_"+CoverName+"_TotalPremium", TotalPremium);
		
		}
	}
	
	// ---------------------------------------- Start -----------------------------------------------------------
	// Below code will be only be execute if We are doing Rewind on MTA directlly.
	
	if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
		if(((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement") && 
				((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy")).equalsIgnoreCase("Yes")){
			
			
			String Rewind_annualPremiumTablePath = "//p[text()=' Previous Premium']//following-sibling::table[1]//tbody//tr";
			List<WebElement> Rewind_listOfRows = driver.findElements(By.xpath(Rewind_annualPremiumTablePath));
			int Rewind_sizeOfAnnualPremiumTable = Rewind_listOfRows.size();
			common.NB_excel_data_map.put("PS_PolicyStartDate", startDate);
			for(int coverDetails = 1;coverDetails<=Rewind_sizeOfAnnualPremiumTable;coverDetails++){
				
				String CoverName = driver.findElement(By.xpath(Rewind_annualPremiumTablePath+"["+coverDetails+"]//td[1]")).getText().replaceAll(" ", "");
				String NetNetPremium = driver.findElement(By.xpath(Rewind_annualPremiumTablePath+"["+coverDetails+"]//td[2]")).getText();
				String PenCommRate = driver.findElement(By.xpath(Rewind_annualPremiumTablePath+"["+coverDetails+"]//td[3]")).getText();
				String PenCommision = driver.findElement(By.xpath(Rewind_annualPremiumTablePath+"["+coverDetails+"]//td[4]")).getText();
				String NetPremium = driver.findElement(By.xpath(Rewind_annualPremiumTablePath+"["+coverDetails+"]//td[5]")).getText();
				String BrokerCommRate = driver.findElement(By.xpath(Rewind_annualPremiumTablePath+"["+coverDetails+"]//td[6]")).getText();
				String BrokerCommision = driver.findElement(By.xpath(Rewind_annualPremiumTablePath+"["+coverDetails+"]//td[7]")).getText();
				String GrossCommRate = driver.findElement(By.xpath(Rewind_annualPremiumTablePath+"["+coverDetails+"]//td[8]")).getText();
				String GrossPremium = driver.findElement(By.xpath(Rewind_annualPremiumTablePath+"["+coverDetails+"]//td[9]")).getText();
				String InsuranceTaxRate = driver.findElement(By.xpath(Rewind_annualPremiumTablePath+"["+coverDetails+"]//td[10]")).getText();
				String InsuranceTax = driver.findElement(By.xpath(Rewind_annualPremiumTablePath+"["+coverDetails+"]//td[11]")).getText();
				String TotalPremium = driver.findElement(By.xpath(Rewind_annualPremiumTablePath+"["+coverDetails+"]//td[12]")).getText();
				
				if(CoverName.equalsIgnoreCase("Totals")){
					Rewind_Underlying_data_map.put("PS_NetNetPremiumTotal", NetNetPremium);
					Rewind_Underlying_data_map.put("PS_PenCommTotal", PenCommision);
					Rewind_Underlying_data_map.put("PS_NetPremiumTotal", NetPremium);
					Rewind_Underlying_data_map.put("PS_BrokerCommissionTotal", BrokerCommision);
					Rewind_Underlying_data_map.put("PS_Total_GP", GrossPremium);
					Rewind_Underlying_data_map.put("PS_Total_GT", InsuranceTax);
					Rewind_Underlying_data_map.put("PS_TotalPremium", TotalPremium);
				}else{
					if(CoverName.contains("Businesss")){
						CoverName = "BusinessInterruption";
					}
					Rewind_Underlying_data_map.put("PS_"+CoverName+"_NetNetPremium", NetNetPremium);
					Rewind_Underlying_data_map.put("PS_"+CoverName+"_PenComm_rate", PenCommRate);
					Rewind_Underlying_data_map.put("PS_"+CoverName+"_PenComm", PenCommision);
					Rewind_Underlying_data_map.put("PS_"+CoverName+"_NetPremium", NetPremium);
					Rewind_Underlying_data_map.put("PS_"+CoverName+"_BrokerComm_rate", BrokerCommRate);
					Rewind_Underlying_data_map.put("PS_"+CoverName+"_BrokerComm", BrokerCommision);
					Rewind_Underlying_data_map.put("PS_"+CoverName+"_GrossComm_rate", GrossCommRate);
					Rewind_Underlying_data_map.put("PS_"+CoverName+"_GP", GrossPremium);
					Rewind_Underlying_data_map.put("PS_"+CoverName+"_IPT", InsuranceTaxRate);
					Rewind_Underlying_data_map.put("PS_"+CoverName+"_GT", InsuranceTax);
					Rewind_Underlying_data_map.put("PS_"+CoverName+"_TotalPremium", TotalPremium);
					if(CoverName.contains("PersonalAccident")){
						CoverName = "PersonalAccidentStandard";
					}
					if(TestBase.product.equals("GTD")){
						CoverName = "GoodsInTransit";
					}
					Rewind_Underlying_data_map.put("CD_"+CoverName, "Yes");
				}
			}
		}
	}
	
	
		
	// -------------------------------- End -------------------------------------------------
	
	// ---------------------------------------- Start -----------------------------------------------------------
		// Below code will be only be execute if We are doing Rewind on MTA directlly.
		
		if(TestBase.businessEvent.equalsIgnoreCase("CAN")){
			
			try{
				k.ImplicitWaitOff();
				String ReinstateTable = "//p[text()=' Reinstatement Details']//following-sibling::table[1]//tbody//tr";
				JavascriptExecutor j_exe = (JavascriptExecutor) driver;
				j_exe.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(ReinstateTable)));
				
				WebElement ReinstateTableCheck = driver.findElement(By.xpath(ReinstateTable));
				k.ImplicitWaitOff();
				if(ReinstateTableCheck.isDisplayed()){
					isReinstateTablePresent = true;
					String Rewind_annualPremiumTablePath = "//p[text()='Annual Premium ']//following-sibling::table[1]//tbody//tr";
					List<WebElement> Rewind_listOfRows = driver.findElements(By.xpath(Rewind_annualPremiumTablePath));
					int Rewind_sizeOfAnnualPremiumTable = Rewind_listOfRows.size();
					
					for(int coverDetails = 1;coverDetails<=Rewind_sizeOfAnnualPremiumTable;coverDetails++){
						
						String CoverName = driver.findElement(By.xpath(Rewind_annualPremiumTablePath+"["+coverDetails+"]//td[1]")).getText().replaceAll(" ", "");
						String PenCommRate = driver.findElement(By.xpath(Rewind_annualPremiumTablePath+"["+coverDetails+"]//td[3]")).getText();
						
						if(!CoverName.equalsIgnoreCase("Totals")){
							if(CoverName.contains("Businesss")){
								CoverName = "BusinessInterruption";
							}
							Underlying_data_map.put("PS_"+CoverName+"_PenComm_rate", PenCommRate);
						}
					
					}
				}
				if(ReinstateTableCheck.isDisplayed()){
					isReinstateTablePresent = true;
					String Rewind_annualPremiumTablePath = "//p[text()=' Reinstatement Details']//following-sibling::table[1]//tbody//tr";
					List<WebElement> Rewind_listOfRows = driver.findElements(By.xpath(Rewind_annualPremiumTablePath));
					int Rewind_sizeOfAnnualPremiumTable = Rewind_listOfRows.size();
					
					for(int coverDetails = 1;coverDetails<=Rewind_sizeOfAnnualPremiumTable;coverDetails++){
						
						String CoverName = driver.findElement(By.xpath(Rewind_annualPremiumTablePath+"["+coverDetails+"]//td[1]")).getText().replaceAll(" ", "");
						String PenCommRate = driver.findElement(By.xpath(Rewind_annualPremiumTablePath+"["+coverDetails+"]//td[3]")).getText();
						
						if(!CoverName.equalsIgnoreCase("Totals")){
							if(CoverName.contains("Businesss")){
								CoverName = "BusinessInterruption";
							}
							Reinstate_data_map.put("PS_"+CoverName+"_PenComm_rate", PenCommRate);
						}
					
					}
				}
				k.ImplicitWaitOn();
			}catch(Throwable t){
				k.ImplicitWaitOn();
			}finally {
				k.ImplicitWaitOn();
			}
			
			
			
			
			
		
		}
			
		// -------------------------------- End -------------------------------------------------
	
	if(type.equalsIgnoreCase("Endorsement") && TestBase.businessEvent.equalsIgnoreCase("Rewind")){
		String transactionDetailsMsg_xpath = "//p[text()=' Transaction Details ']//following-sibling::p";
		WebElement transactionDetails_Msg = driver.findElement(By.xpath(transactionDetailsMsg_xpath));
		
		String text = transactionDetails_Msg.getText();
		
		String date[] = text.split(",");
		String days[] = text.split(",");
		
		Underlying_data_map.put("MTA_EffectiveDays",days[1].substring(days[1].indexOf(":")+2, days[1].indexOf("days")));
		Underlying_data_map.put("MTA_EffectiveDate",date[0].substring(date[0].indexOf(":")+2, date[0].length()));

	}else{
		String transactionDetailsMsg_xpath = "//p[text()=' Transaction Details ']//following-sibling::p";
		try{
			k.ImplicitWaitOff();
			try{
				WebElement transactionDetails_Msg = driver.findElement(By.xpath(transactionDetailsMsg_xpath));
				k.ImplicitWaitOn();
				String text = transactionDetails_Msg.getText();
				
				String date[] = text.split(",");
				String days[] = text.split(",");
				
				data_map.put("MTA_EffectiveDays",days[1].substring(days[1].indexOf(":")+2, days[1].indexOf("days")));
				data_map.put("MTA_EffectiveDate",date[0].substring(date[0].indexOf(":")+2, date[0].length()));
			}catch(Throwable t){
				
			}
			
		}catch(Throwable t){
			return retvalue;
		}
	}
	
	customAssert.assertTrue(common.funcNextNavigateDecesionMaker("Navigate","History"),"Issue while Navigating to History . ");
	
	WebElement tableID = driver.findElement(By.xpath("//*[@id='linkpanels']//following::table[1]//tbody"));
	List<WebElement> HistoryRows = tableID.findElements(By.tagName("tr"));
	int size = HistoryRows.size();
	
	String EventName = tableID.findElement(By.xpath("//tr["+size+"]//td[1]//a")).getText();
	
	if(EventName.equalsIgnoreCase("New Business") || EventName.equalsIgnoreCase("Renewal")){
		
		String effectiveDate = driver.findElement(By.xpath("//*[@id='linkpanels']//following::table[1]//tbody//tr["+size+"]//td[5]")).getText();
		if(type.equalsIgnoreCase("Endorsement") && TestBase.businessEvent.equalsIgnoreCase("Rewind")){
			Rewind_Underlying_data_map.put("EffectiveDate", effectiveDate);
		}else{
			Underlying_data_map.put("EffectiveDate", effectiveDate);
		}
	}
	
	
	TestUtil.reportStatus("Policy: "+(String)data_map.get(sheetName+"_PolicyNumber")+" successfully searched . ", "Info", true);
		
	return retvalue;
	}

public boolean coverDetailsUpdation(String type , String status) throws AWTException, InterruptedException{
	
	Map<Object, Object> data_map = null;
	Map<Object, Object> Underlying_data_map = null;
	String sheetName = "" , code="";
	switch (type) {
	case "Endorsement":
	case "New Business":
		if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
			data_map = common.MTA_excel_data_map;
			Underlying_data_map = common.NB_excel_data_map;
			sheetName = "MTA";
		}else if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
			if(type.equalsIgnoreCase("Endorsement")){
				data_map = common.Rewind_excel_data_map;
				Underlying_data_map = common.MTA_excel_data_map;
				sheetName = "Rewind";
				code = "MTA";
			}else{
				data_map = common.Rewind_excel_data_map;
				Underlying_data_map = common.NB_excel_data_map;
				sheetName = "Rewind";
				code = "NB";
			}
		}else if(TestBase.businessEvent.equalsIgnoreCase("CAN")){
			data_map = common.CAN_excel_data_map;
			Underlying_data_map = common.NB_excel_data_map;
			sheetName = "CAN";
			code = "NB";
		}
		break;
	case "Renewal":
		data_map = common.MTA_excel_data_map;
		Underlying_data_map = common.Renewal_excel_data_map;
		sheetName = "MTA";
		break;
	}
	
	boolean retvalue = true;
   
	customAssert.assertTrue(common.funcNextNavigateDecesionMaker("Navigate","Covers"),"Issue while Navigating to Covers screen . ");
	k.ImplicitWaitOff();
	try{
		WebElement CoverDetailsTable = driver.findElement(By.xpath("//*[@id='main']/form/div//following::table//tbody"));
		List<WebElement> rows = CoverDetailsTable.findElements(By.tagName("tr"));
		
		if(rows.size()>0){
			
			for(int i = 1; i < rows.size(); i++ ) {
				
				String cover = driver.findElement(By.xpath("//*[@id='main']/form/div//following::table//tbody//tr["+(i+1)+"]//td[1]")).getText();
				if(!cover.equalsIgnoreCase("")){
					String coverValue = CoverDetailsTable.findElement(By.xpath("//tr["+(i+1)+"]//td[2]/img")).getAttribute("alt");
					if(cover.contains("Commercial Vehicle"))
						cover = "Commercial Vehicles";
					Underlying_data_map.put("CD_"+cover.replaceAll(" ", ""), coverValue);
					String coverData = "";
					
					if(TestBase.businessEvent.equalsIgnoreCase("Rewind") && ((String)common.Rewind_excel_data_map.get("Rewind_ExistingPolicy_Type")).equalsIgnoreCase("Endorsement")){
						coverData = (String)common_EP.Rewind_Underlying_data_map.get("CD_"+cover.replaceAll(" ", ""));
					}
					
					if(coverData==null){
						common_EP.Rewind_Underlying_data_map.put("CD_"+cover.replaceAll(" ", ""), "No");
					}
					
				}
				
			}
			
			switch (type) {
			case "Endorsement":
			case "New Business":
				if(TestBase.businessEvent.equalsIgnoreCase("MTA")){
					common.NB_excel_data_map.putAll(Underlying_data_map);
				}else if(TestBase.businessEvent.equalsIgnoreCase("Rewind")){
					if(type.equalsIgnoreCase("Endorsement")){
						common.MTA_excel_data_map.putAll(Underlying_data_map);
					}else{
						common.NB_excel_data_map.putAll(Underlying_data_map);
					}
				}
				break;
			case "Renewal":
				common.Renewal_excel_data_map.putAll(Underlying_data_map);
				break;
			}
			
			TestUtil.reportStatus("Cover details are updated successfully under Coverdetails sheet for underlying event.", "Info", true);
			return retvalue;
		}else{
			TestUtil.reportStatus("No policies are present with Status as <b> [ "+type+" & "+status+" ] </b> for product <b> [ "+TestBase.product+" ] </b>", "Fail", true);
			return false;
		}
	}catch(Throwable t){
		TestUtil.reportStatus("Covers updation function is having issue.", "Fail", true);
		return false;
	}
	
	
	
    
}
	
}
