package com.selenium.commonfiles.base;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.selenium.commonfiles.util.TestUtil;

public class CommonFunction_XOE extends TestBase{

	SimpleDateFormat df = new SimpleDateFormat();
	public double totalPremium=0.0,totalSumInsured=0.0;
	
public void NewBusinessFlow(String code,String event){
	String testName = (String)common.NB_excel_data_map.get("Automation Key");
	try{
		
		customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
		customAssert.assertTrue(common.checkClient(common.NB_excel_data_map,code,event),"Unable to check Client.");
		customAssert.assertTrue(common.createNewQuote(common.NB_excel_data_map,code,event), "Unable to create new quote.");
		customAssert.assertTrue(common.selectLatestQuote(common.NB_excel_data_map,code,event), "Unable to select quote from table.");
		customAssert.assertTrue(funcPolicyDetails(common.NB_excel_data_map), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Additional Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(common.funcCovers(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Locations Summary"),"Issue while Navigating to Layers  . ");
		customAssert.assertTrue(funcLocationsSummary(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
		
		if(((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
			customAssert.assertTrue(common_CCF.funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcPremiumSummary(common.NB_excel_data_map,code,event,"NB"), "Premium Summary function is having issue(S) . ");
			customAssert.assertTrue(common.funcStatusHandling(common.NB_excel_data_map,code,event), "Status Handling function is having issue(S) . ");
			customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
			customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
		
			TestUtil.reportTestCasePassed(testName);
	
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
	}
	
}
public void MTAFlow(String code,String fileName,String policy_Status){
	System.out.println("Test method of MTA For - "+code);
	TestUtil.reportStatus("Test Method of MTA For - "+code, "Pass", true);
	
	
}
public void RewindFlow(String code,String fileName,String policy_Status){
	System.out.println("Test method of Rewind For - "+code);
	TestUtil.reportStatus("Test Method of Rewind For - "+code, "Pass", true);
	
	
}

/**
 * 
 * This method handles XOE Policy Details screens scripting.
 * 
 */
public boolean funcPolicyDetails(Map<Object, Object> map_data){
	
	boolean retvalue = true;
	try{
		customAssert.assertTrue(common.funcPageNavigation("General", ""), "Navigation problem to Policy Details page .");
		
		customAssert.assertTrue(k.Input("XOE_PD_InsuredName", (String)map_data.get("NB_ClientName")),	"Unable to enter value in Proposer Name  field .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("XOE_PD_InsuredName", "value"),"Proposer Name Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("CCF_CC_TradingName", (String)map_data.get("PD_TradingName")),	"Unable to enter value in Trading Name  field .");
		customAssert.assertTrue(k.Input("CCF_PD_BusinessDesc", (String)map_data.get("PD_BusinessDesc")),	"Unable to enter value in Business Desc  field .");
		customAssert.assertTrue(k.Input("CCF_Address_CC_Address", (String) map_data.get("PD_Address")),"Unable to enter value in Address field. ");
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"Address Field Should Contain Valid Address  .");
		customAssert.assertTrue(k.Input("CCF_Address_CC_line2", (String) map_data.get("PD_Line1")),"Unable to enter value in Address field line 1 . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_line3", (String) map_data.get("PD_Line2")),"Unable to enter value in Address field line 2 . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_Town", (String) map_data.get("PD_Town")),"Unable to enter value in Town field . ");
		customAssert.assertTrue(k.Input("CCF_Address_CC_County", (String) map_data.get("PD_County")),"Unable to enter value in County  . ");
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
		customAssert.assertTrue(k.Input("XOE_PD_CurrentP", (String) map_data.get("PD_CurrentP")),"Unable to enter value in Current Premium field. ");
		customAssert.assertTrue(k.Input("XOE_PD_TargetP", (String) map_data.get("PD_TargetP")),"Unable to enter value in Target Pemium field. ");
		
		//TradeCode Selection & Verification
		if(((String)common.NB_excel_data_map.get("PD_TCS_TradeCode_Button")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.tradeCodeSelection((String)common.NB_excel_data_map.get("PD_TCS_TradeCode") ,"Policy Details" , 0),"Trade code selection function is having issue(S).");
		}
		
		customAssert.assertTrue(k.SelectRadioBtn("POC_PD_HazardGroup", (String)map_data.get("PD_HazardGroup")), "Unable to Select  Hazard Group radio button on Policy Details Page.");
		switch ((String)map_data.get("PD_HazardGroup")) {
		case "Yes":
			customAssert.assertTrue(k.Input("POC_PD_NewHazardGroupValue", (String) map_data.get("PD_NewHazardGroupValue")),"Unable to enter value in  Hazard Group value field. ");
			customAssert.assertTrue(k.Input("POC_PD_HazardGroupOverrideReason", (String) map_data.get("PD_HazardGroupOverrideReason")),"Unable to enter value in Hazard Group Override Reason. ");
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
 * This method handles XOE Policy Details screens scripting.
 * 
 */
public boolean funcLocationsSummary(Map<Object, Object> map_data){
	
	boolean retvalue = true;
	try{
		totalPremium = 0.0;
		totalSumInsured=0.0;
		customAssert.assertTrue(common.funcPageNavigation("Locations Summary", ""), "Navigation problem to Location Details page .");
		
		int count = 0;
		int noOfProperties = 0;
		if(common.no_of_inner_data_sets.get("Location Details")==null){
			noOfProperties = 0;
		}else{
			noOfProperties = common.no_of_inner_data_sets.get("Location Details");
		}
		int tableIndex = 0;
		while(count < noOfProperties ){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddProperty"), "Unable to click Add Property Button on Locations Summary .");
			customAssert.assertTrue(addLocationDetails(map_data,count),"Error while adding proprty  .");
			TestUtil.reportStatus("Location Property  <b>[  "+common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("Automation Key")+"  ]</b>  added successfully . ", "Info", true);
			customAssert.assertTrue(k.Click("CCF_Btn_Back"), "Unable to click on Back Button on Property Details .");
			tableIndex = k.getTableIndex("Property Number", "xpath", "html/body/div[3]/form/div/table");
			customAssert.assertTrue(inputTableData(map_data,count,tableIndex),"Error while adding proprty  .");
			customAssert.assertTrue(calculatePremium(map_data,count,tableIndex),"Error while adding proprty  .");
			count++;
		}
		
		if(((String)common.NB_excel_data_map.get("LS_RecalculateButton")).equalsIgnoreCase("Yes")){
			k.Click("XOE_PD_ReCalCButton");
		}
		
		for(int i=0;i<=noOfProperties;i++){
			String propertyNumber = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[1]")).getText();
			if(propertyNumber.equalsIgnoreCase("Totals")){
				String actTotalSumInsured = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[3]/input")).getAttribute("value");
				String actTotalPremium = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[6]/input")).getAttribute("value");
				
				customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(actTotalPremium), totalPremium, "Total Premium"));
				customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(actTotalSumInsured), totalSumInsured, "Total SumInsured"));
			}
		}
		
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Locations Summary", (String)common.NB_excel_data_map.get("Automation Key"), "LS_TotalPremium", Double.toString(totalPremium), common.NB_excel_data_map);
		
		return retvalue;
		
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Unable to to do operation on policy details page. \n", t);
        return false;
 }
}



public boolean addLocationDetails(Map<Object, Object> map_data,int count){
	
	boolean retvalue = true;
	try{
		customAssert.assertTrue(common.funcPageNavigation("Location Details", ""), "Navigation problem to Location Details page .");
		
		String locationNumber = null;
		locationNumber = common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_LocationNumber");
		
		Random number = new Random();
		int no = number.nextInt(999);
		
		if(locationNumber==null || (locationNumber).equalsIgnoreCase("0") || (locationNumber).equalsIgnoreCase("0.0")){
			customAssert.assertTrue(k.Input("XOE_LD_LocationNumber", "0_"+no),"Unable to Enter Location Number.");
		}else{
			if(!locationNumber.contains("_")){
				locationNumber = common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_LocationNumber")+"_"+no;
				TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Location Details", common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("Automation Key"), "LD_LocationNumber", locationNumber, common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count));
				customAssert.assertTrue(k.Input("XOE_LD_LocationNumber", locationNumber),"Unable to Enter Location Number.");
			}else{
				customAssert.assertTrue(k.Input("XOE_LD_LocationNumber", common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_LocationNumber")),"Unable to Enter Location Number.");
			}
		}
		
		customAssert.assertTrue(k.Input("XOE_LD_Address", common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_Address")),"Unable to Enter Location Address.");
		customAssert.assertTrue(k.Input("XOE_LD_AddressL2", common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_AddressL2")),"Unable to Enter Location Address Line2.");
		customAssert.assertTrue(k.Input("XOE_LD_AddressL3", common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_AddressL3")),"Unable to Enter Location Address Line3.");
		customAssert.assertTrue(k.Input("XOE_LD_Town", common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_Town")),"Unable to Enter Town.");
		customAssert.assertTrue(k.Input("XOE_LD_Postcode", common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_Postcode")),"Unable to Enter Postcode .");
		customAssert.assertTrue(k.Input("XOE_LD_County", common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_County")),"Unable to Enter County.");
		if((common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_SumInsured")).equalsIgnoreCase("") || (common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_SumInsured"))==null){
			TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent, "Location Details", common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("Automation Key"), "LD_SumInsured", "0", common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count));
		}
		customAssert.assertTrue(k.Input("XOE_LD_SumInsuredn", common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_SumInsured")),"Unable to Enter Sum Insured (GBP).");
		
		//TradeCode Selection & Verification
		if((common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_TCS_TradeCode_Button")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.tradeCodeSelection(common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_TCS_TradeCode") ,"Property Details" , count),"Trade code selection function is having issue(S).");
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

public boolean inputTableData(Map<Object, Object> map_data,int count,int tableIndex){
	
	boolean retvalue = true;
	try{
		
		List<WebElement> listOfRows = driver.findElements(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr"));
		int counter=0;
		for(int i=0;i<listOfRows.size()-1;i++){
			
			String propertyNumber = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[1]")).getText();
			if(propertyNumber.equalsIgnoreCase((String)common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_LocationNumber"))){
				
				driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(Keys.CONTROL, "a");
				driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[5]/input")).sendKeys(common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_BookRate"));
				common.funcButtonSelection("Save");
				counter++;
				break;
			}
		}
		if(counter==0){
			TestUtil.reportStatus((String)common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_LocationNumber")+" proeprty is not present in tabl hence not able to enter bookrate.", "Info", true);
		}
		return retvalue;
		
	}catch(Throwable t) {
        return false;
 }
}

public boolean calculatePremium(Map<Object, Object> map_data,int count,int tableIndex){
	
	boolean retvalue = true;
	int counter=0;
	try{
		List<WebElement> listOfRows = driver.findElements(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr"));
		
		for(int i=0;i<listOfRows.size()-1;i++){
			
			String propertyNumber = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[1]")).getText();
			if(propertyNumber.equalsIgnoreCase((String)common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_LocationNumber"))){
				
				double sumInsured = Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_SumInsured"));
				double bookRate = Double.parseDouble(common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_BookRate"));
				
				String expPremium =  common.roundedOff(Double.toString(((sumInsured * bookRate)/100.0)));
				totalPremium = totalPremium + Double.parseDouble(expPremium);
				totalSumInsured = totalSumInsured + sumInsured;
				String actPremium = driver.findElement(By.xpath("html/body/div[3]/form/div/table["+tableIndex+"]/tbody/tr["+(i+1)+"]/td[6]/input")).getAttribute("value");
				customAssert.assertTrue(CommonFunction.compareValues(Double.parseDouble(expPremium), Double.parseDouble(actPremium), "Property Number : "+common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_LocationNumber")));
				counter++;
				break;
			}
		}
		if(counter==0){
			TestUtil.reportStatus((String)common.NB_Structure_of_InnerPagesMaps.get("Location Details").get(count).get("LD_LocationNumber")+" proeprty is not present in table hence not able to verify premium.", "Info", true);
		}
		return retvalue;
		
	}catch(Throwable t) {
        return false;
 }
}

public boolean funcRewindOperation(){
	
	boolean r_value= true;
	
	try{
		
		if(((String)common.NB_excel_data_map.get("CD_Add_Remove_Cover")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcRewindCoversCheck(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
			if(((String)common.NB_excel_data_map.get("CD_Add_Terrorism")).equals("Yes") &&
					((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(common_CCF.funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
			}
			
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			Assert.assertTrue(common.funcPremiumSummary(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"RewindAddCover"));
			customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
			customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
			//customAssert.assertTrue(common.funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
		}
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
}

//End of CommonFunction_XOE.java
}
