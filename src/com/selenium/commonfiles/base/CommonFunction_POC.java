package com.selenium.commonfiles.base;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.selenium.commonfiles.util.TestUtil;

public class CommonFunction_POC extends TestBase{

	SimpleDateFormat df = new SimpleDateFormat();
	
public void NewBusinessFlow(String code,String event){
	String testName = (String)common.NB_excel_data_map.get("Automation Key");
	try{
		
		customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
		customAssert.assertTrue(common.checkClient(common.NB_excel_data_map,code,event),"Unable to check Client.");
		customAssert.assertTrue(common.createNewQuote(common.NB_excel_data_map,code,event), "Unable to create new quote.");
		customAssert.assertTrue(common.selectLatestQuote(common.NB_excel_data_map,code,event), "Unable to select quote from table.");
		customAssert.assertTrue(common_POB.funcPolicyDetails(common.NB_excel_data_map), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(common_CCF.funcPreviousClaims(common.NB_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(common.funcCovers(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified Perils"),"Issue while Navigating to Specified Perils  . ");
		customAssert.assertTrue(common.funcSpecifiedPerils(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
		
		if(((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
			customAssert.assertTrue(common_POB.funcInsuredProperties(common.NB_excel_data_map), "Insured Property function is having issue(S) . ");
		}
		
		if(((String)common.NB_excel_data_map.get("CD_Liability")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
			customAssert.assertTrue(common_CCF.funcEmployersLiability(common.NB_excel_data_map), "Employers Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
			customAssert.assertTrue(common_CCF.funcELDInformation(common.NB_excel_data_map), "ELD Information function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability"),"Issue while Navigating to Property Owners Liability  . ");
			customAssert.assertTrue(common_POB.funcPropertyOwnersLiability(common.NB_excel_data_map), "Public Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Property Owners Liability Information"),"Issue while Navigating to Products Liability  . ");
			customAssert.assertTrue(common_POB.funcLiabilityInformation(common.NB_excel_data_map), "Liability Information function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_CyberandDataSecurity")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
			customAssert.assertTrue(common_CCF.funcCyberAndDataSecurity(common.NB_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
			customAssert.assertTrue(common_CCF.funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		
		if(((String)common.NB_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
			customAssert.assertTrue(common_CCF.funcLegalExpenses(common.NB_excel_data_map,code,event), "Legal Expenses function is having issue(S) . ");
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

//End of CommonFunction_POC.java
}
