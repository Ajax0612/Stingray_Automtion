package com.selenium.commonfiles.base;

import static com.selenium.commonfiles.util.TestUtil.WriteDataToXl;

import java.awt.AWTException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.selenium.commonfiles.util.ErrorInTestMethod;
import com.selenium.commonfiles.util.TestUtil;

public class CommonFunction_CCF extends TestBase{

	SimpleDateFormat df = new SimpleDateFormat();
	public int actual_no_of_years=0,err_count=0,trans_error_val=0;
	
public void NewBusinessFlow(String code,String event) throws ErrorInTestMethod{
	String testName = (String)common.NB_excel_data_map.get("Automation Key");
	try{
		
		customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
		customAssert.assertTrue(common.checkClient(common.NB_excel_data_map,code,event),"");
		customAssert.assertTrue(common.createNewQuote(common.NB_excel_data_map,code,event), "Unable to create new quote.");
		customAssert.assertTrue(common.selectLatestQuote(common.NB_excel_data_map,code,event), "Unable to select quote from table.");
		customAssert.assertTrue(funcPolicyDetails(common.NB_excel_data_map,code,event), "Policy Details function having issue .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Previous Claims"),"Issue while Navigating to Previous Claims  . ");
		customAssert.assertTrue(funcPreviousClaims(common.NB_excel_data_map), "Previous claim function is having issue(S) .");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
		customAssert.assertTrue(common.funcCovers(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified Perils"),"Issue while Navigating to Specified Perils  . ");
		customAssert.assertTrue(common.funcSpecifiedPerils(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
		
		if(((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
			customAssert.assertTrue(funcInsuredProperties(common.NB_excel_data_map), "Insured Property function is having issue(S) . ");
		}
		
		if(((String)common.NB_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
			customAssert.assertTrue(funcBusinessInterruption(common.NB_excel_data_map), "Business Interruption function is having issue(S) . ");
		}
		
		if(((String)common.NB_excel_data_map.get("CD_Liability")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
			customAssert.assertTrue(funcEmployersLiability(common.NB_excel_data_map), "Employers Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
			customAssert.assertTrue(funcELDInformation(common.NB_excel_data_map), "ELD Information function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
			customAssert.assertTrue(funcPublicLiability(common.NB_excel_data_map), "Public Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
			customAssert.assertTrue(funcProductsLiability(common.NB_excel_data_map), "Products Liability function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
			customAssert.assertTrue(funcLiabilityInformation(common.NB_excel_data_map), "Liability Information function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_SpecifiedAllRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
			customAssert.assertTrue(funcSpecifiedAllRisks(common.NB_excel_data_map), "Specified All Risks function is having issue(S) . ");
			}
		
		if(((String)common.NB_excel_data_map.get("CD_ContractorsAllRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
			customAssert.assertTrue(funcContractorsAllRisks(common.NB_excel_data_map), "Contractors All Risks function is having issue(S) . ");
			}
		
		if(((String)common.NB_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
			customAssert.assertTrue(funcComputersandElectronicRisks(common.NB_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
			}
		
		if(((String)common.NB_excel_data_map.get("CD_Money")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
			customAssert.assertTrue(funcMoney(common.NB_excel_data_map), "Money function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_GoodsInTransit")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
			customAssert.assertTrue(funcGoodsInTransit(common.NB_excel_data_map), "Goods In Transit function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_MarineCargo")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Marine Cargo"),"Issue while Navigating to Marine Cargo screen . ");
			customAssert.assertTrue(funcMarineCargo(common.NB_excel_data_map), "Marine Cargo function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_CyberandDataSecurity")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
			customAssert.assertTrue(funcCyberAndDataSecurity(common.NB_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_DirectorsandOfficers")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Directors and Officers"),"Issue while Navigating to Directors and Officers screen . ");
			customAssert.assertTrue(funcDirectorsAndOfficers(common.NB_excel_data_map), "Directors and Officers function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_FrozenFood")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Frozen Foods"),"Issue while Navigating to Frozen Foods screen . ");
			customAssert.assertTrue(funcFrozenFoods(common.NB_excel_data_map), "Frozen Foods function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_LossofLicence")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Loss of Licence"),"Issue while Navigating to Loss of Licence screen . ");
		customAssert.assertTrue(funcLossofLicence(common.NB_excel_data_map), "Loss of Licence function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_FidelityGuarantee")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
			customAssert.assertTrue(funcFidelityGuarantee(common.NB_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
			}
		if(((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
			customAssert.assertTrue(funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
			}
		
		if(((String)common.NB_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
			customAssert.assertTrue(funcLegalExpenses(common.NB_excel_data_map,code,event), "Legal Expenses function is having issue(S) . ");
			}
		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcPremiumSummary(common.NB_excel_data_map,code,event,"NB"), "Premium Summary function is having issue(S) . ");
			Assert.assertTrue(common.funcStatusHandling(common.NB_excel_data_map,code,event));
			customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
			
			if(TestBase.businessEvent.equals("NB")){
			customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
			customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
			
			TestUtil.reportTestCasePassed(testName);
			}
	
	}catch(Throwable t){
		TestUtil.reportTestCaseFailed(testName, t);
		throw new ErrorInTestMethod(t.getMessage());
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
public void RewindFlow(String code,String fileName,String policy_Status){
	System.out.println("Test method of Rewind For - "+code);
	TestUtil.reportStatus("Test Method of Rewind For - "+code, "Pass", true);
	
	
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
		customAssert.assertTrue(CancellationProcess(common.NB_excel_data_map,code,event),"Failed in Cancellation Process Function");
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


/**
 * 
 * This method handles CCF Policy Details screens scripting.
 * 
 */
public boolean funcPolicyDetails(Map<Object, Object> map_data,String code ,String event){
	
	boolean retvalue = true;
	try{
		customAssert.assertTrue(common.funcPageNavigation("Policy Details", ""), "Navigation problem to Policy Details page .");
		if(common.currentRunningFlow.equals("NB")){
		customAssert.assertTrue(k.Input("CCF_PD_ProposerName", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_PD_ProposerName", (String)map_data.get("NB_ClientName")),	"Unable to enter value in Proposer Name  field .");}
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_PD_ProposerName", "value"),"Proposer Name Field Should Contain Valid Name  .");
		customAssert.assertTrue(k.Input("CCF_CC_TradingName", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_CC_TradingName", (String)map_data.get("PD_TradingName")),	"Unable to enter value in Trading Name  field .");
		customAssert.assertTrue(k.Input("CCF_PD_BusinessDesc", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_PD_BusinessDesc", (String)map_data.get("PD_BusinessDesc")),	"Unable to enter value in Business Desc  field .");
		if(common.currentRunningFlow.equals("NB")){
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_1QS", (String)map_data.get("PD_1QS")), "Unable to Select 1QS radio button on Policy Details Page.");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_NewVenture", (String)map_data.get("PD_NewVenture")), "Unable to Select New Venture radio button on Policy Details Page.");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_Prospect", (String)map_data.get("PD_Prospect")), "Unable to Select Prospect radio button on Policy Details Page.");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CrossSell", (String)map_data.get("PD_CrossSell")), "Unable to Select CrossSell radio button on Policy Details Page.");
		}
		customAssert.assertTrue(k.Input("CCF_PD_DateEstablishment", Keys.chord(Keys.CONTROL, "a")),"Unable to select Proposer name field");
		customAssert.assertTrue(k.Input("CCF_PD_DateEstablishment", (String)map_data.get("PD_DateEstablishment")),	"Unable to enter value in Date Establishment  field .");
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
		
		if(common.currentRunningFlow.equals("NB")){
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
			customAssert.assertTrue(k.Input("CCF_QC_TargetPemium", (String) map_data.get("QC_TargetPemium")),"Unable to enter value in Target Pemium field. ");
			
		}
		
		
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_TaxExempt", (String)map_data.get("PD_TaxExempt")), "Unable to Select TaxExempt radio button on Policy Details Page.");
		if(((String)map_data.get("PD_TaxExempt")).equalsIgnoreCase("Yes")){
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_data.get("Automation Key"), "PS_IPTRate", "0", map_data);
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_data.get("Automation Key"), "PS_InsuranceTaxButton", "Yes", map_data);
		}else{
			TestUtil.WriteDataToXl(code+"_"+event, "Premium Summary",(String)map_data.get("Automation Key"), "PS_IPTRate", "12", map_data);
		}
		if(!TestBase.product.equals("CTB"))
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CarrierOverride", (String)map_data.get("PD_CarrierOverride")), "Unable to Select Carrier Override radio button on Policy Details Page.");
		
		if(map_data.get("PD_CarrierOverride").equals("Yes")){
			customAssert.assertTrue(k.SelectRadioBtn("CCF_PD_CO_RefferedToHead", (String)map_data.get("PD_CO_RefferedToHead")), "Unable to Select Reffered To Head radio button on Policy Details Page.");
		}
		k.waitTwoSeconds();
		
		if(((String)map_data.get("PD_TCS_TradeCode_Button")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.tradeCodeSelection((String)map_data.get("PD_TCS_TradeCode") , "Policy Details" , 0),"Trade code selection function is having issue(S).");
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

public boolean funcInsuredProperties(Map<Object, Object> map_data){
	
	boolean r_Value = true;
	try{
		customAssert.assertTrue(common.funcPageNavigation("Insured Properties", ""),"Insured Properties page is having issue(S)");
		if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }
		customAssert.assertTrue(k.Input("CCF_IP_AnyOneEvent", Keys.chord(Keys.CONTROL, "a")),"Unable to select any one Event field");
		customAssert.assertTrue(k.Input("CCF_IP_AnyOneEvent", (String)map_data.get("IP_AnyOneEvent")),	"Unable to enter value in any one Event field .");
		customAssert.assertTrue(k.Input("IP_Landslip", Keys.chord(Keys.CONTROL, "a")),"Unable to select Subsidence Ground Heave or Landslip field");
		customAssert.assertTrue(k.Input("IP_Landslip", (String)map_data.get("IP_Landslip")),"Unable to enter value in Subsidence Ground Heave or Landslip field .");
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
		
		int count = 0;
		String[] properties = ((String)map_data.get("IP_AddProperty")).split(";");
        int no_of_property = properties.length;
        
		/*if(common.no_of_inner_data_sets.get("Property Details")==null){
			noOfProperties = 0;
		}else{
			noOfProperties = common.no_of_inner_data_sets.get("Property Details");
		}
		*/
		while(count < no_of_property ){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddProperty"), "Unable to click Add Property Button on Insured Properties .");
			customAssert.assertTrue(addProperty(map_data,count),"Error while adding insured proprty  .");
			TestUtil.reportStatus("Insured Property  <b>[  "+common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("Automation Key")+"  ]</b>  added successfully . ", "Info", true);
			customAssert.assertTrue(k.Click("CCF_Btn_Back"), "Unable to click on Back Button on Property Details .");
			count++;
		}
			
		TestUtil.reportStatus("All the specified Insured properties added and verified successfully . ", "Info", true);
		
		return r_Value;
		
		
	}catch(Throwable t) {
        String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
        TestUtil.reportFunctionFailed("Failed in "+methodName+" function");
        Assert.fail("Insured Properties function is having issue(S). \n", t);
        return false;
 }
}

public boolean addProperty(Map<Object, Object> map_data,int count){
	
	boolean r_value=true;
	
	try{
		
		Map<String, List<Map<String, String>>> internal_data_map = new HashMap<>();
		Map<Object, Object> outer_data_map=common.NB_excel_data_map;
		switch(common.currentRunningFlow){
			case "NB":
				internal_data_map = common.NB_Structure_of_InnerPagesMaps;
				outer_data_map = common.NB_excel_data_map;
				break;
			case "MTA":
				internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
				outer_data_map = common.MTA_excel_data_map;
				break;
			case "Renewal":
				internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
				outer_data_map = common.Renewal_excel_data_map;
				break;
			case "Rewind":
				internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
				outer_data_map = common.Rewind_excel_data_map;
				break;
			case "Requote":
				internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
				outer_data_map = common.Requote_excel_data_map;
				break;
		
		}
		
		customAssert.assertTrue(common.funcPageNavigation("Property Details", ""),"Property Details page navigation issue(S)");
		
		//
		if(!(internal_data_map.get("Property Details").get(count).get("PoD_CopyAddress")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(k.Input("CCF_Address_CC_Address", internal_data_map.get("Property Details").get(count).get("PoD_Address")),"Unable to enter value in Address field. ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line2", internal_data_map.get("Property Details").get(count).get("PoD_AddressL2")),"Unable to enter value in Address field line 2 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line3", internal_data_map.get("Property Details").get(count).get("PoD_AddressL3")),"Unable to enter value in Address field line 3 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Town", internal_data_map.get("Property Details").get(count).get("PoD_Town")),"Unable to enter value in Town field . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_County", internal_data_map.get("Property Details").get(count).get("PoD_County")),"Unable to enter value in County  . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode", internal_data_map.get("Property Details").get(count).get("PoD_Postcode")),"Unable to enter value in PostCode field .");
			customAssert.assertTrue(common.funcButtonSelection("Save"));
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"Address Field Should Contain Valid Value on Client Details .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Postcode", "value"),"Postcode Field Should Contain Valid Value on Client Details .");
			customAssert.assertTrue(common.validatePostCode(internal_data_map.get("Property Details").get(count).get("PoD_Postcode")),"Post Code is not in Correct format .");
			
		}else{
			customAssert.assertTrue(k.Click("CCF_Btn_CopyCorAddress"));
		}
		//
		
		customAssert.assertTrue(k.Input("CCF_PoD_PropertyAge", internal_data_map.get("Property Details").get(count).get("PoD_PropertyAge")),"Unable to enter value in Age of Property (years) . ");
		customAssert.assertTrue(k.DropDownSelection("CCF_PoD_TerrorismZone", internal_data_map.get("Property Details").get(count).get("PoD_TerrorismZone")), "Unable to select value from Terrorism Zone dropdown .");
		
		//Statement of Fact
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q1", internal_data_map.get("Property Details").get(count).get("PoD_SOF_Q1")), "Unable to Select first SOF radio button on Policy Details Page.");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q2", internal_data_map.get("Property Details").get(count).get("PoD_SOF_Q2")), "Unable to Select second SOF radio button on Policy Details Page.");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q3", internal_data_map.get("Property Details").get(count).get("PoD_SOF_Q3")), "Unable to Select third SOF radio button on Policy Details Page.");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q4", internal_data_map.get("Property Details").get(count).get("PoD_SOF_Q4")), "Unable to Select fourth SOF radio button on Policy Details Page.");
		
		//Sums Insured
		customAssert.assertTrue(k.DropDownSelection("CCF_PoD_DayOneUplift",internal_data_map.get("Property Details").get(count).get("PoD_DayOneUplift")), "Unable to select value from Day One uplift dropdown .");
		customAssert.assertTrue(k.SelectRadioBtn("CCF_PoD_SOF_Q1", internal_data_map.get("Property Details").get(count).get("PoD_SOF_Q1")), "Unable to Select first SOF radio button on Policy Details Page.");
		
		//Proximity
		customAssert.assertTrue(k.Input("CCF_PoD_ProximityDescription", internal_data_map.get("Property Details").get(count).get("PoD_ProximityDescription")),"Unable to enter value in Proximity description . ");
		
		/*customAssert.assertTrue(common.funcButtonSelection("Save"));
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"Address Field Should Contain Valid Value on Client Details .");
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Postcode", "value"),"Postcode Field Should Contain Valid Value on Client Details .");
		customAssert.assertTrue(common.validatePostCode(common.NB_Structure_of_InnerPagesMaps.get("Property Details").get(count).get("PoD_Postcode")),"Post Code is not in Correct format .");
		*/
		//Trade Code
		if((internal_data_map.get("Property Details").get(count).get("PoD_TCS_TradeCode_Button")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.tradeCodeSelection((String)internal_data_map.get("Property Details").get(count).get("PoD_MD_TCS_TradeCode"),"Property Details",count),"Trade code selection function is having issue(S).");	
		}
		/*customAssert.assertTrue(k.Click("CCF_Btn_PoD_SelectTradeCode"), "Unable to click on Select MD Trade Code button in Policy Details .");
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
		*/
		
		//EML
		customAssert.assertTrue(k.Input("CCF_PoD_EmlAmount_GBP", internal_data_map.get("Property Details").get(count).get("PoD_EmlAmount_GBP")),"Unable to enter value in EmlAmount_GBP . ");
		customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_PoD_EmlAmount_GBP", "value"),"Eml amount (GBP) Field Should Contain Valid Value on Property Details .");
		customAssert.assertTrue(k.Input("CCF_PoD_EmlAmount_Percent", internal_data_map.get("Property Details").get(count).get("PoD_EmlAmount_Percent")),"Unable to enter value in Eml amount (%) . ");
		
		//Inner MD-Bespoke Sum Insured
		//customAssert.assertTrue(addMD_BIBespokeSumInsured(map_data), "Error while adding Bespoke data . ");
		List<WebElement> bespoke_MD_btns = k.getWebElements("CCF_Btn_AddBespokeSumIns");
		WebElement MD_bespoke_btn = bespoke_MD_btns.get(0);
		MD_bespoke_btn.click();
		customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", internal_data_map.get("Property Details").get(count).get("BSI_MD_Description")),"Unable to enter value in Bespoke Description . ");
		customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", internal_data_map.get("Property Details").get(count).get("BSI_MD_SumInsured")),"Unable to enter value in Bespoke Sum Insured . ");
		customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Bespoke Sum Insured inner page .");
		
		
		if(((String)outer_data_map.get("CD_BusinessInterruption")).equalsIgnoreCase("Yes") ||
				((String)outer_data_map.get("CD_Add_BusinessInterruption")).equalsIgnoreCase("Yes")){
			List<WebElement> bespoke_BI_btns = k.getWebElements("CCF_Btn_AddBespokeSumIns");
			WebElement BI_bespoke_btn = bespoke_BI_btns.get(1);
			k.ScrollInVewWebElement(BI_bespoke_btn);
			BI_bespoke_btn.click();
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", internal_data_map.get("Property Details").get(count).get("BSI_BI_Description")),"Unable to enter value in BI Bespoke Description . ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", internal_data_map.get("Property Details").get(count).get("BSI_BI_SumInsured")),"Unable to enter value in Bespoke Sum Insured . ");
			customAssert.assertTrue(k.DropDownSelection("CCF_BSI_BI_IndemnityPeriod", internal_data_map.get("Property Details").get(count).get("BSI_BI_IndemnityPeriod")), "Unable to select value from Indemnity Period dropdown .");
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Bespoke Sum Insured inner page .");
		}
				
		 //String sVal = (String)map_data.get("IP_AddProperty");
		 //String sValue[] = sVal.split(";");
		 //int pCount = sValue.length;
		double finalMDPremium = 0.00, finalBIPremium = 0.00;
		 
		 if(((String)outer_data_map.get("CD_MaterialDamage")).equalsIgnoreCase("Yes") || 
				 ((String)outer_data_map.get("CD_Add_MaterialDamage")).equalsIgnoreCase("Yes")){
			 customAssert.assertTrue(CommonFunction.PropertyDetails_HandleTables(map_data, "MD", count,internal_data_map),"failed in MD handle table");
			 customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
			 
			 finalMDPremium =  finalMDPremium + Double.parseDouble(internal_data_map.get("Property Details").get(count).get("MD_TotalPremium"));
			 TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)outer_data_map.get("Automation Key"), "PS_MaterialDamage_NP", String.valueOf(finalMDPremium), outer_data_map);
			 
			 if(((String)outer_data_map.get("CD_BusinessInterruption")).equalsIgnoreCase("Yes") ||
					 ((String)outer_data_map.get("CD_Add_BusinessInterruption")).equalsIgnoreCase("Yes")){
				 customAssert.assertTrue(CommonFunction.PropertyDetails_HandleTables(map_data, "BI", count,internal_data_map),"failed in BI handle table");
				 customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
				 
				 finalBIPremium =  finalBIPremium + Double.parseDouble(internal_data_map.get("Property Details").get(count).get("BI_TotalPremium"));
				 TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)outer_data_map.get("Automation Key"), "PS_BusinessInterruption_NP", String.valueOf(finalBIPremium), outer_data_map);
			 }
		 }
		 
		//customAssert.assertTrue(common.PropertyDetails_HandleTables(NB_excel_data_map, "MD"));
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
}


public boolean addMD_BIBespokeSumInsured(Map<Object, Object> map_data){
	boolean r_value=true;
	
	try{
		
		int total_count_MD_bespoke = common.no_of_inner_data_sets.get("BS Insured MD");
		int count=0;
		while(count < total_count_MD_bespoke){
			List<WebElement> bespoke_btns = k.getWebElements("CCF_Btn_AddBespokeSumIns");
			System.out.println(bespoke_btns.size());
			WebElement MD_bespoke_btn = bespoke_btns.get(0);
			MD_bespoke_btn.click();
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", common.NB_Structure_of_InnerPagesMaps.get("BS Insured MD").get(count).get("BSI_MD_Description")),"Unable to enter value in MD Bespoke Description . ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", Keys.chord(Keys.CONTROL, "a")),"Unable to select Bespoke Sum Insured field");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", common.NB_Structure_of_InnerPagesMaps.get("BS Insured MD").get(count).get("BSI_MD_SumInsured")),"Unable to enter value in Bespoke Sum Insured . ");
			
			customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Bespoke Sum Insured inner page .");
			count++;
		}
		
		if(((String)map_data.get("CD_BusinessInterruption")).equals("Yes")){
			int total_count_BI_bespoke = common.no_of_inner_data_sets.get("BS Insured BI");
			count=0;
			while(count < total_count_BI_bespoke){
				k.Click("CCF_Btn_BI_Bespoke");
				k.waitTwoSeconds();
			
				customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", common.NB_Structure_of_InnerPagesMaps.get("BS Insured BI").get(count).get("BSI_BI_Description")),"Unable to enter value in BI Bespoke Description . ");
				customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", Keys.chord(Keys.CONTROL, "a")),"Unable to select Bespoke Sum Insured field");
				customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", common.NB_Structure_of_InnerPagesMaps.get("BS Insured BI").get(count).get("BSI_BI_SumInsured")),"Unable to enter value in Bespoke Sum Insured . ");
				customAssert.assertTrue(k.DropDownSelection("CCF_BSI_BI_IndemnityPeriod", common.NB_Structure_of_InnerPagesMaps.get("BS Insured BI").get(count).get("BSI_BI_IndemnityPeriod")), "Unable to select value from Indemnity Period dropdown .");
				customAssert.assertTrue(k.clickInnerButton("CCF_inner_page_locator", "Save"), "Unable to click on Save Button on Bespoke Sum Insured inner page .");
				count++;
			}
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
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }

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
	Map<String, List<Map<String, String>>> internal_data_map = null;
	Map<Object, Object> map_data = null;
	switch(common.currentRunningFlow){
		case "NB":
			internal_data_map = common.NB_Structure_of_InnerPagesMaps;
			map_data = common.NB_excel_data_map;
			break;
		case "MTA":
			internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
			map_data = common.MTA_excel_data_map;
			break;
		case "Renewal":
			internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
			map_data = common.Renewal_excel_data_map;
			break;
		case "Rewind":
			internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
			map_data = common.Rewind_excel_data_map;
			break;
		case "Requote":
			internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
			map_data = common.Requote_excel_data_map;
			break;
	
	}
	try{
		/*int total_count_BI_SpecifiedSupplier = 0;*/
		String[] properties = ((String)map_data.get("BI_AddSpecifiedSupplier")).split(";");
        int no_of_property = properties.length;
		
       /* if(common.no_of_inner_data_sets.get("SpecifiedSupplier")==null){
			total_count_BI_SpecifiedSupplier = 0;
		}else{
			total_count_BI_SpecifiedSupplier = common.no_of_inner_data_sets.get("SpecifiedSupplier");
		}*/
		
		int count=0;
		while(count < no_of_property){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddSpecified_Supplier"), "Unable to click on AddSpecified_Supplier Button on BI page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_SS_SupplierName", internal_data_map.get("SpecifiedSupplier").get(count).get("SS_SupplierName")),"Unable to enter value in Address field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_SS_SupplierName", "value"),"SupplierName Field Should Contain Valid Value  .");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Address", internal_data_map.get("SpecifiedSupplier").get(count).get("SS_SupplierAddress_Line1")),"Unable to enter value in SupplierAddress_Line1 field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"SupplierAddress Field Should Contain Valid Value  .");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line2", internal_data_map.get("SpecifiedSupplier").get(count).get("SS_SupplierAddress_Line2")),"Unable to enter value in SupplierAddress field line 2 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line3", internal_data_map.get("SpecifiedSupplier").get(count).get("SS_SupplierAddress_Line3")),"Unable to enter value in SupplierAddress field line 3 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Town", internal_data_map.get("SpecifiedSupplier").get(count).get("SS_Town")),"Unable to enter value in Town field . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_County", internal_data_map.get("SpecifiedSupplier").get(count).get("SS_County")),"Unable to enter value in County  . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode", internal_data_map.get("SpecifiedSupplier").get(count).get("SS_Postcode")),"Unable to enter value in PostCode field .");
			customAssert.assertTrue(common.validatePostCode(internal_data_map.get("SpecifiedSupplier").get(count).get("SS_Postcode")),"Post Code is not in Correct format .");
			customAssert.assertTrue(k.Input("CCF_SS_SupplierLimit", Keys.chord(Keys.CONTROL, "a")),"Unable to select SupplierLimit field");
			customAssert.assertTrue(k.Input("CCF_SS_SupplierLimit", internal_data_map.get("SpecifiedSupplier").get(count).get("SS_SupplierLimit")),"Unable to enter value in SupplierLimit field .");
			
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
	Map<String, List<Map<String, String>>> internal_data_map = null;
	Map<Object, Object> map_data = null;
	switch(common.currentRunningFlow){
	case "NB":
		internal_data_map = common.NB_Structure_of_InnerPagesMaps;
		map_data = common.NB_excel_data_map;
		break;
	case "MTA":
		internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
		map_data = common.MTA_excel_data_map;
		break;
	case "Renewal":
		internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
		map_data = common.Renewal_excel_data_map;
		break;
	case "Rewind":
		internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
		map_data = common.Rewind_excel_data_map;
		break;
	case "Requote":
		internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
		map_data = common.Requote_excel_data_map;
		break;
	
	}
	try{
		/*int total_count_BI_SpecifiedCustomer = 0;
		if(common.no_of_inner_data_sets.get("SpecifiedCustomer")==null){
			total_count_BI_SpecifiedCustomer = 0;
		}else{
			total_count_BI_SpecifiedCustomer = common.no_of_inner_data_sets.get("SpecifiedCustomer");
		}*/
		String[] properties = ((String)map_data.get("BI_AddSpecifiedCustomer")).split(";");
        int no_of_property = properties.length;
		
		
		int count=0;
		while(count < no_of_property){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddSpecified_Customer"), "Unable to click on AddSpecified_Customer Button on BI page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_SS_CustomerName", internal_data_map.get("SpecifiedCustomer").get(count).get("SC_CustomerName")),"Unable to enter value in Address field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_SS_CustomerName", "value"),"SupplierName Field Should Contain Valid Value  .");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Address", internal_data_map.get("SpecifiedCustomer").get(count).get("SC_CustomerAddress_Line1")),"Unable to enter value in CustomerAddress_Line1 field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_Address_CC_Address", "value"),"SupplierAddress Field Should Contain Valid Value  .");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line2", internal_data_map.get("SpecifiedCustomer").get(count).get("SC_CustomerAddress_Line2")),"Unable to enter value in CustomerAddress field line 2 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_line3", internal_data_map.get("SpecifiedCustomer").get(count).get("SC_CustomerAddress_Line3")),"Unable to enter value in CustomerAddress field line 3 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Town", internal_data_map.get("SpecifiedCustomer").get(count).get("SC_Town")),"Unable to enter value in Town field . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_County", internal_data_map.get("SpecifiedCustomer").get(count).get("SC_County")),"Unable to enter value in County  . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Postcode", internal_data_map.get("SpecifiedCustomer").get(count).get("SC_Postcode")),"Unable to enter value in PostCode field .");
			customAssert.assertTrue(common.validatePostCode(internal_data_map.get("SpecifiedCustomer").get(count).get("SC_Postcode")),"Post Code is not in Correct format .");
			customAssert.assertTrue(k.Input("CCF_SS_CustomerLimit", Keys.chord(Keys.CONTROL, "a")),"Unable to select Customer Limit field");
			customAssert.assertTrue(k.Input("CCF_SS_CustomerLimit", internal_data_map.get("SpecifiedCustomer").get(count).get("SC_CustomerLimit")),"Unable to enter value in Customer Limit field .");
			
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
		Map<Object,Object> data_map = common.NB_excel_data_map;
		
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
			
		
		customAssert.assertTrue(common.funcPageNavigation("Employers Liability", ""),"Employers Liability page navigations issue(S)");
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }

		
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
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)data_map.get("Automation Key"), "PS_EmployersLiability_NP", String.valueOf(sPremiumm), data_map);
		 
		TestUtil.reportStatus("Employers Liability details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
	}

public boolean addELItems(){
	boolean r_value=true;
	
	try{
		
		Map<String, List<Map<String, String>>> data_map = common.NB_Structure_of_InnerPagesMaps;
		Map<Object, Object> map_data = common.NB_excel_data_map;
		
		
		switch(common.currentRunningFlow){
		case "NB":
			data_map = common.NB_Structure_of_InnerPagesMaps;
			map_data = common.NB_excel_data_map;
		break;
		case "CAN":
			data_map = common.CAN_Structure_of_InnerPagesMaps;
			map_data = common.CAN_excel_data_map;
		break;
		case "MTA":
			data_map = common.MTA_Structure_of_InnerPagesMaps;
			map_data = common.MTA_excel_data_map;
		break;
		case "Renewal":
			data_map = common.Renewal_Structure_of_InnerPagesMaps;
			map_data = common.Renewal_excel_data_map;
		break;
		case "Rewind":
			data_map = common.Rewind_Structure_of_InnerPagesMaps;
			map_data = common.Rewind_excel_data_map;
		break;
		case "Requote":
			data_map = common.Requote_Structure_of_InnerPagesMaps;
			map_data = common.Requote_excel_data_map;
		break;
		}
	
		String[] properties = ((String)map_data.get("EL_AddItem")).split(";");
        int no_of_property = properties.length;
        
		
		int count=0;
		while(count < no_of_property){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on EL page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", data_map.get("EL AddItem").get(count).get("AD_EL_ItemDesc")),"Unable to enter value in Description field. ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", Keys.chord(Keys.CONTROL, "a")),"Unable to select Sum Insured field");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", data_map.get("EL AddItem").get(count).get("AD_EL_ItemSumIns")),"Unable to enter value in Sum Insured field. ");
			
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
Map<Object,Object> data_map = null;
		
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
		
		
		customAssert.assertTrue(common.funcPageNavigation("Employers Liability Database", ""),"Employers Liability Database page navigations issue(S)");
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }

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
		Map<String, List<Map<String, String>>> data_map = common.NB_Structure_of_InnerPagesMaps;
		Map<Object, Object> map_data = common.NB_excel_data_map;
		
		
		switch(common.currentRunningFlow){
		case "NB":
			data_map = common.NB_Structure_of_InnerPagesMaps;
			map_data = common.NB_excel_data_map;
		break;
		case "CAN":
			data_map = common.CAN_Structure_of_InnerPagesMaps;
			map_data = common.CAN_excel_data_map;
		break;
		case "MTA":
			data_map = common.MTA_Structure_of_InnerPagesMaps;
			map_data = common.MTA_excel_data_map;
		break;
		case "Renewal":
			data_map = common.Renewal_Structure_of_InnerPagesMaps;
			map_data = common.Renewal_excel_data_map;
		break;
		case "Rewind":
			data_map = common.Rewind_Structure_of_InnerPagesMaps;
			map_data = common.Rewind_excel_data_map;
		break;
		case "Requote":
			data_map = common.Requote_Structure_of_InnerPagesMaps;
			map_data = common.Requote_excel_data_map;
		break;
		}
		
		String[] properties = ((String)map_data.get("ELD_AddSubsidiary")).split(";");
        int no_of_property = properties.length;
        
		/*int total_count_ELD_Subsidiary = 0;
		if(common.no_of_inner_data_sets.get("ELD AddSubsidiary")==null){
			total_count_ELD_Subsidiary = 0;
		}else{
			total_count_ELD_Subsidiary = common.no_of_inner_data_sets.get("ELD AddSubsidiary");
		}*/
		
		int count=0;
		while(count < no_of_property){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Subsidiary Button on EL page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_ELD_AS_TradingName", data_map.get("ELD AddSubsidiary").get(count).get("ELD_AS_TradingName")),"Unable to enter value in Address field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_ELD_AS_TradingName", "value"),"SupplierName Field Should Contain Valid Value  .");
			customAssert.assertTrue(k.Input("CCF_ELD_AS_AddressLine_1", data_map.get("ELD AddSubsidiary").get(count).get("ELD_AS_AddressLine_1")),"Unable to enter value in CustomerAddress_Line1 field. ");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_ELD_AS_AddressLine_1", "value"),"SupplierAddress Field Should Contain Valid Value  .");
			customAssert.assertTrue(k.Input("CCF_ELD_AS_AddressLine_2", data_map.get("ELD AddSubsidiary").get(count).get("ELD_AS_AddressLine_2")),"Unable to enter value in CustomerAddress field line 2 . ");
			customAssert.assertTrue(k.Input("CCF_ELD_AS_AddressLine_3", data_map.get("ELD AddSubsidiary").get(count).get("ELD_AS_AddressLine_3")),"Unable to enter value in CustomerAddress field line 3 . ");
			customAssert.assertTrue(k.Input("CCF_ELD_AS_AddressLine_4", data_map.get("ELD AddSubsidiary").get(count).get("ELD_AS_AddressLine_4")),"Unable to enter value in CustomerAddress field line 3 . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_Town", data_map.get("ELD AddSubsidiary").get(count).get("ELD_AS_Town")),"Unable to enter value in Town field . ");
			customAssert.assertTrue(k.Input("CCF_Address_CC_County", data_map.get("ELD AddSubsidiary").get(count).get("ELD_AS_County")),"Unable to enter value in County  . ");
			customAssert.assertTrue(k.Input("CCF_ELD_AS_Country", data_map.get("ELD AddSubsidiary").get(count).get("ELD_AS_Country")),"Unable to enter value in Town field . ");
			customAssert.assertTrue(k.Input("CCF_ELD_AS_Postcode", data_map.get("ELD AddSubsidiary").get(count).get("ELD_AS_Postcode")),"Unable to enter value in PostCode field .");
			customAssert.assertTrue(!k.getAttributeIsEmpty("CCF_ELD_AS_Postcode", "value"),"Postcode Field Should Contain Valid Value  .");
			customAssert.assertTrue(common.validatePostCode(data_map.get("ELD AddSubsidiary").get(count).get("ELD_AS_Postcode")),"Post Code is not in Correct format .");
				
			customAssert.assertTrue(k.SelectRadioBtn("CCF_ERNExempt", data_map.get("ELD AddSubsidiary").get(count).get("ELD_AS_ERNExempt")), "Unable to Select ERN Exempt radio button .");
			
			if(data_map.get("ELD AddSubsidiary").get(count).get("ELD_AS_ERNExempt").equals("No")){
				customAssert.assertTrue(k.Input("CCF_EmpRefNo", Keys.chord(Keys.CONTROL, "a")),"Unable to select Employer Reference Number field");
				customAssert.assertTrue(k.Input("CCF_EmpRefNo", data_map.get("ELD AddSubsidiary").get(count).get("ELD_AS_EmployerRefNumber")),"Unable to enter value in Employer Reference Number . ");
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
		
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }

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
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_PublicLiability_NP", String.valueOf(sPremiumm), map_data);
				
		TestUtil.reportStatus("Public Liability details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
	}

public boolean addPLItems(){
	boolean r_value=true;
	Map<String, List<Map<String, String>>> data_map = common.NB_Structure_of_InnerPagesMaps;
	Map<Object, Object> map_data = common.NB_excel_data_map;
	
	
	switch(common.currentRunningFlow){
	case "NB":
		data_map = common.NB_Structure_of_InnerPagesMaps;
		map_data = common.NB_excel_data_map;
	break;
	case "CAN":
		data_map = common.CAN_Structure_of_InnerPagesMaps;
		map_data = common.CAN_excel_data_map;
	break;
	case "MTA":
		data_map = common.MTA_Structure_of_InnerPagesMaps;
		map_data = common.MTA_excel_data_map;
	break;
	case "Renewal":
		data_map = common.Renewal_Structure_of_InnerPagesMaps;
		map_data = common.Renewal_excel_data_map;
	break;
	case "Rewind":
		data_map = common.Rewind_Structure_of_InnerPagesMaps;
		map_data = common.Rewind_excel_data_map;
	break;
	case "Requote":
		data_map = common.Requote_Structure_of_InnerPagesMaps;
		map_data = common.Requote_excel_data_map;
	break;
	}
	
	
	try{
		String[] properties = ((String)map_data.get("PL_AddItem")).split(";");
	    int no_of_property = properties.length;
	    
		int count=0;
		while(count < no_of_property){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on PL page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", data_map.get("PL AddItem").get(count).get("AD_PL_ItemDesc")),"Unable to enter value in Description field. ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", Keys.chord(Keys.CONTROL, "a")),"Unable to select Sum Insured field");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", data_map.get("PL AddItem").get(count).get("AD_PL_ItemSumIns")),"Unable to enter value in Sum Insured field. ");
			
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
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }

		
		customAssert.assertTrue(k.Input("CCF_PRL_IndemnityLimit", Keys.chord(Keys.CONTROL, "a")),"Unable to select PRL Indemnity Limit field");
		customAssert.assertTrue(k.Input("CCF_PRL_IndemnityLimit", (String)map_data.get("PRL_IndemnityLimit")),"Unable to enter value in PRL Indemnity Limit . ");
		customAssert.assertTrue(k.Input("CCF_PL_DepositPremium", Keys.chord(Keys.CONTROL, "a")),"Unable to select PRL DepositPremium field");
		customAssert.assertTrue(k.Input("CCF_PL_DepositPremium", (String)map_data.get("PRL_DepositPremium")),"Unable to enter value in PRL DepositPremium . ");
			
		//Inner BI-Specified Suppliers
		//customAssert.assertTrue(addPRLItems(), "Error while adding PRL Items . ");		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Products Liability .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "PRL", "Product Liability", "PRL AddItem");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_ProductsLiability_NP", String.valueOf(sPremiumm), map_data);
				
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
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }

		customAssert.assertTrue(k.Input("CCF_SAR_AdditionalInfo", Keys.chord(Keys.CONTROL, "a")),"Unable to select SAR_AdditionalInfo field");
		customAssert.assertTrue(k.Input("CCF_SAR_AdditionalInfo", (String)map_data.get("SAR_AdditionalInfo")),"Unable to enter value in SAR_AdditionalInfo . ");
		
		//Inner Add Item
		customAssert.assertTrue(addSARItem(), "Error while adding SAR Item . ");
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Insured Properties .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "SAR", "Specified All Risks", "SAR AddItem");	
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_SpecifiedAllRisks_NP", String.valueOf(sPremiumm), map_data);
		
		TestUtil.reportStatus("Specified All Risks details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
	}

public boolean addSARItem(){
	boolean r_value=true;
	Map<String, List<Map<String, String>>> internal_data_map = null;
	Map<Object, Object> map_data = null;
	switch(common.currentRunningFlow){
		case "NB":
			internal_data_map = common.NB_Structure_of_InnerPagesMaps;
			map_data = common.NB_excel_data_map;
			break;
		case "MTA":
			internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
			map_data = common.MTA_excel_data_map;
			break;
		case "Renewal":
			internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
			map_data = common.Renewal_excel_data_map;
			break;
		case "Rewind":
			internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
			map_data = common.Rewind_excel_data_map;
			break;
		case "Requote":
			internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
			map_data = common.Requote_excel_data_map;
			break;
	
	}
	try{
		String[] properties = ((String)map_data.get("SAR_AddItem")).split(";");
        int no_of_property = properties.length;
		
		int count=0;
		while(count < no_of_property){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on SAR page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("AD_SAR_ItemDesc", internal_data_map.get("SAR AddItem").get(count).get("AD_SAR_ItemDesc")),"Unable to enter value in Item Description field. ");
			customAssert.assertTrue(k.DropDownSelection("AD_SAR_GeoLimit", internal_data_map.get("SAR AddItem").get(count).get("AD_SAR_GeoLimit")),"Unable to Select value in Geographical Limit field. ");
			customAssert.assertTrue(k.Input("AD_SAR_SumIns", Keys.chord(Keys.CONTROL, "a")),"Unable to select Sum Insured field");
			customAssert.assertTrue(k.Input("AD_SAR_SumIns", internal_data_map.get("SAR AddItem").get(count).get("AD_SAR_SumInsured")),"Unable to enter value in Sum Insured . ");
			customAssert.assertTrue(k.Input("AD_SAR_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select Excess field");
			customAssert.assertTrue(k.Input("AD_SAR_Excess", internal_data_map.get("SAR AddItem").get(count).get("AD_SAR_Excess")),"Unable to enter value in Excess  . ");
			
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
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }

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
		
		if(((String)map_data.get("CAR_TCS_TradeCode_Button")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.tradeCodeSelection((String)map_data.get("CAR_TradeCode"),"Contractors All Risks",0),"Trade code selection function is having issue(S).");
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
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_ContractorsAllRisks_NP", String.valueOf(sPremiumm), map_data);
		
		TestUtil.reportStatus("Contractors All Risks details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean addCARItems(){
	boolean r_value=true;
	Map<String, List<Map<String, String>>> internal_data_map = null;
	Map<Object, Object> map_data = null;
	switch(common.currentRunningFlow){
	case "NB":
		internal_data_map = common.NB_Structure_of_InnerPagesMaps;
		map_data = common.NB_excel_data_map;
		break;
	case "MTA":
		internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
		map_data = common.MTA_excel_data_map;
		break;
	case "Renewal":
		internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
		map_data = common.Renewal_excel_data_map;
		break;
	case "Rewind":
		internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
		map_data = common.Rewind_excel_data_map;
		break;
	case "Requote":
		internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
		map_data = common.Requote_excel_data_map;
		break;
	
	}
	try{
		/*int total_count_BI_SpecifiedCustomer = 0;
		if(common.no_of_inner_data_sets.get("SpecifiedCustomer")==null){
			total_count_BI_SpecifiedCustomer = 0;
		}else{
			total_count_BI_SpecifiedCustomer = common.no_of_inner_data_sets.get("SpecifiedCustomer");
		}*/
		String[] properties = ((String)map_data.get("CAR_AddItem")).split(";");
        int no_of_property = properties.length;
		
		int count=0;
		while(count < no_of_property){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on CAR page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_BSI_MD_Description", internal_data_map.get("CAR AddItem").get(count).get("AD_CAR_ItemDesc")),"Unable to enter value in Description field. ");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", Keys.chord(Keys.CONTROL, "a")),"Unable to select Sum Insured field");
			customAssert.assertTrue(k.Input("CCF_BSI_MD_SumInsured", internal_data_map.get("CAR AddItem").get(count).get("AD_CAR_SumInsured")),"Unable to enter value in Sum Insured field. ");
			
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
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }
		 customAssert.assertTrue(k.Input("CCF_CER_Computers_SumInsured", (String)map_data.get("CER_Computers_SumInsured")),"Unable to enter value in Computers: Sum Insured . ");
		customAssert.assertTrue(k.Input("CCF_CER_Erisk_VirusHacking", Keys.chord(Keys.CONTROL, "a")),"Unable to select E Risk: Virus & Hacking field");
		customAssert.assertTrue(k.Input("CCF_CER_Erisk_VirusHacking", (String)map_data.get("CER_Erisk_VirusHacking")),"Unable to enter value in E Risk: Virus & Hacking . ");
		customAssert.assertTrue(k.DropDownSelection("CCF_CER_MaxIndemnityPeriod", (String)map_data.get("CER_MaxIndemnityPeriod")),"Unable to enter value in Maximum Indemnity Period (months) field. ");
		customAssert.assertTrue(k.Input("CCF_CER_Excess", Keys.chord(Keys.CONTROL, "a")),"Unable to select Excess field");
		customAssert.assertTrue(k.Input("CCF_CER_Excess", (String)map_data.get("CER_Excess")),"Unable to enter value in Excess . ");
		customAssert.assertTrue(k.Input("CCF_CER_AdditionalExp_SumInsured", (String)map_data.get("CER_AdditionalExp_SumInsured")),"Unable to enter value in Additional Expenditure: Sum Insured . ");
	
		customAssert.assertTrue(k.Input("CCF_CER_Computers_SumInsured_tbl", Keys.chord(Keys.CONTROL, "a")),"Unable to select Computers: Sum Insured field");
		customAssert.assertTrue(k.Input("CCF_CER_Computers_SumInsured_tbl", (String)map_data.get("CER_Computers_SumInsured")),"Unable to enter value in Computers: Sum Insured . ");
		customAssert.assertTrue(k.Input("CCF_CER_AdditionalExp_SumInsured_tbl", Keys.chord(Keys.CONTROL, "a")),"Unable to select Additional Expenditure: Sum Insured field");
		customAssert.assertTrue(k.Input("CCF_CER_AdditionalExp_SumInsured_tbl", (String)map_data.get("CER_AdditionalExp_SumInsured")),"Unable to enter value in Additional Expenditure: Sum Insured . ");
			
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Computers and Electronic Risks .");
		
		double sPremiumm = CommonFunction.func_AllCvers_HandleTables( map_data, "CER", "Computers and Electronic Risks", "");
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_ComputersandElectronicRisks_NP", String.valueOf(sPremiumm), map_data);
		
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
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }

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
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_Money_NP", String.valueOf(sPremiumm), map_data);
		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Money .");
		
		TestUtil.reportStatus("Money details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean addSafe_Money(){
	boolean r_value=true;
	Map<String, List<Map<String, String>>> internal_data_map = null;
	Map<Object, Object> map_data = null;
	switch(common.currentRunningFlow){
	case "NB":
		internal_data_map = common.NB_Structure_of_InnerPagesMaps;
		map_data = common.NB_excel_data_map;
		break;
	case "MTA":
		internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
		map_data = common.MTA_excel_data_map;
		break;
	case "Renewal":
		internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
		map_data = common.Renewal_excel_data_map;
		break;
	case "Rewind":
		internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
		map_data = common.Rewind_excel_data_map;
		break;
	case "Requote":
		internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
		map_data = common.Requote_excel_data_map;
		break;
	
	}
	try{
		/*int total_count_BI_SpecifiedCustomer = 0;
		if(common.no_of_inner_data_sets.get("SpecifiedCustomer")==null){
			total_count_BI_SpecifiedCustomer = 0;
		}else{
			total_count_BI_SpecifiedCustomer = common.no_of_inner_data_sets.get("SpecifiedCustomer");
		}*/
		String[] properties = ((String)map_data.get("M_AddSafe")).split(";");
        int no_of_property = properties.length;
		int count=0;
		while(count < no_of_property){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddSafe"), "Unable to click on Add Safe Button on Money page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_M_AS_MakeModelOfSafe", internal_data_map.get("M AddSafe").get(count).get("M_AS_MakeModelOfSafe")),"Unable to enter value in Make/Model of Safe field. ");
			customAssert.assertTrue(k.Input("CCF_M_AS_SafeLimit", Keys.chord(Keys.CONTROL, "a")),"Unable to select Safe Limit field");
			customAssert.assertTrue(k.Input("CCF_M_AS_SafeLimit", internal_data_map.get("M AddSafe").get(count).get("M_AS_SafeLimit")),"Unable to enter value in Safe Limit field. ");
			
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
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }

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
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_GoodsInTransit_NP", String.valueOf(sPremiumm), map_data);
		
		TestUtil.reportStatus("Goods In Transit details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}

public boolean addSpecifiedVehicles_GIT(){
	boolean r_value=true;
	Map<String, List<Map<String, String>>> internal_data_map = null;
	Map<Object, Object> map_data = null;
	switch(common.currentRunningFlow){
	case "NB":
		internal_data_map = common.NB_Structure_of_InnerPagesMaps;
		map_data = common.NB_excel_data_map;
		break;
	case "MTA":
		internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
		map_data = common.MTA_excel_data_map;
		break;
	case "Renewal":
		internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
		map_data = common.Renewal_excel_data_map;
		break;
	case "Rewind":
		internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
		map_data = common.Rewind_excel_data_map;
		break;
	case "Requote":
		internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
		map_data = common.Requote_excel_data_map;
		break;
	
	}
	try{
		/*int total_count_BI_SpecifiedCustomer = 0;
		if(common.no_of_inner_data_sets.get("SpecifiedCustomer")==null){
			total_count_BI_SpecifiedCustomer = 0;
		}else{
			total_count_BI_SpecifiedCustomer = common.no_of_inner_data_sets.get("SpecifiedCustomer");
		}*/
		String[] properties = ((String)map_data.get("GIT_AddSpecifiedVehicle")).split(";");
        int no_of_property = properties.length;
        
		int count=0;
		while(count < no_of_property){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddSpecifiedVehicle"), "Unable to click on Add AddSpecifiedVehicle Button on GIT page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_GIT_ASV_VehicleDesc", internal_data_map.get("GIT AddSpecificVehicle").get(count).get("GIT_ASV_VehicleDesc")),"Unable to enter value in Vehicle Description field. ");
			customAssert.assertTrue(k.Input("CCF_GIT_LOL_PerVehicle", Keys.chord(Keys.CONTROL, "a")),"Unable to select Limit of Liability per Vehicle field");
			customAssert.assertTrue(k.Input("CCF_GIT_LOL_PerVehicle", internal_data_map.get("GIT AddSpecificVehicle").get(count).get("GIT_LOL_PerVehicle")),"Unable to enter value in Limit of Liability per Vehicle field. ");
			customAssert.assertTrue(k.Input("CCF_GIT_LOL_PerTrailer", Keys.chord(Keys.CONTROL, "a")),"Unable to select Limit of Liability per Trailer field");
			customAssert.assertTrue(k.Input("CCF_GIT_LOL_PerTrailer", internal_data_map.get("GIT AddSpecificVehicle").get(count).get("GIT_LOL_PerTrailer")),"Unable to enter value in Limit of Liability per Trailer field. ");
			
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
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }

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
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }

		
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
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, "Premium Summary",(String)map_data.get("Automation Key"), "PS_FidelityGuarantee_NP", String.valueOf(sPremiumm), map_data);
		
		TestUtil.reportStatus("Fidelity Guarantee details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}
public boolean addFidelityGuaranteeItems(){
	boolean r_value=true;
	Map<String, List<Map<String, String>>> internal_data_map = null;
	Map<Object, Object> map_data = null;
	switch(common.currentRunningFlow){
	case "NB":
		internal_data_map = common.NB_Structure_of_InnerPagesMaps;
		map_data = common.NB_excel_data_map;
		break;
	case "MTA":
		internal_data_map = common.MTA_Structure_of_InnerPagesMaps;
		map_data = common.MTA_excel_data_map;
		break;
	case "Renewal":
		internal_data_map = common.Renewal_Structure_of_InnerPagesMaps;
		map_data = common.Renewal_excel_data_map;
		break;
	case "Rewind":
		internal_data_map = common.Rewind_Structure_of_InnerPagesMaps;
		map_data = common.Rewind_excel_data_map;
		break;
	case "Requote":
		internal_data_map = common.Requote_Structure_of_InnerPagesMaps;
		map_data = common.Requote_excel_data_map;
		break;
	
	}
	try{
		/*int total_count_BI_SpecifiedCustomer = 0;
		if(common.no_of_inner_data_sets.get("SpecifiedCustomer")==null){
			total_count_BI_SpecifiedCustomer = 0;
		}else{
			total_count_BI_SpecifiedCustomer = common.no_of_inner_data_sets.get("SpecifiedCustomer");
		}*/
		String[] properties = ((String)map_data.get("FG_ScheduleofEmployees")).split(";");
        int no_of_property = properties.length;
		
		int count=0;
		while(count < no_of_property){
			
			customAssert.assertTrue(k.Click("CCF_Btn_AddItem"), "Unable to click on Add Item Button on Fidelity Guarantee page .");
			k.waitTwoSeconds();
			
			customAssert.assertTrue(k.Input("CCF_AD_SOE_EmployeeDesc", internal_data_map.get("FG SOE AddItem").get(count).get("AD_SOE_EmployeeDesc")),"Unable to enter value in Employee Description field. ");
			customAssert.assertTrue(k.Input("CCF_AD_SOE_LimitOfLiability", Keys.chord(Keys.CONTROL, "a")),"Unable to select Sum Insured field");
			customAssert.assertTrue(k.Input("CCF_AD_SOE_LimitOfLiability", internal_data_map.get("FG SOE AddItem").get(count).get("AD_SOE_LimitOfLiability")),"Unable to enter value in LimitOfLiability field. ");
			customAssert.assertTrue(k.Input("CCF_AD_SOE_Wages", Keys.chord(Keys.CONTROL, "a")),"Unable to select Sum Insured field");
			customAssert.assertTrue(k.Input("CCF_AD_SOE_Wages", internal_data_map.get("FG SOE AddItem").get(count).get("AD_SOE_Wages")),"Unable to enter value in Wages field. ");
		
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
		if(!common.currentRunningFlow.equalsIgnoreCase("MTA") && !common.currentRunningFlow.equalsIgnoreCase("Renewal")&& !common.currentRunningFlow.equalsIgnoreCase("Rewind")){
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
		 if(!common.currentRunningFlow.equalsIgnoreCase("NB")){
         	
	    	  customAssert.assertTrue(common_HHAZ.deleteItems(),"Delete Items function is having issues.");
	      }

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
		customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Legal Expenses", testName, "LE_AnnualCarrierPremium", exp_AnnualCarrierPremium,map_data),"Error while writing data to excel for field >NB_ClientId<");
		customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_LegalExpenses_NP", (String)map_data.get("LE_NetPremium"),map_data),"Error while writing data to excel for field >NB_ClientId<");
		
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

public boolean funcRewindOperation(){
	
	boolean r_value= true;
	
	try{
		
		if(((String)common.NB_excel_data_map.get("CD_Add_Remove_Cover")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcRewindCoversCheck(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
			if(((String)common.NB_excel_data_map.get("CD_Add_MaterialDamage")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(funcInsuredProperties(common.NB_excel_data_map), "Insured Property function is having issue(S) . ");
			}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_BusinessInterruption")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_BusinessInterruption")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
				customAssert.assertTrue(funcBusinessInterruption(common.NB_excel_data_map), "Business Interruption function is having issue(S) . ");
			}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_Liability")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Liability")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(funcEmployersLiability(common.NB_excel_data_map), "Employers Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
				customAssert.assertTrue(funcELDInformation(common.NB_excel_data_map), "ELD Information function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
				customAssert.assertTrue(funcPublicLiability(common.NB_excel_data_map), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(funcProductsLiability(common.NB_excel_data_map), "Products Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
				customAssert.assertTrue(funcLiabilityInformation(common.NB_excel_data_map), "Liability Information function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_SpecifiedAllRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_SpecifiedAllRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcSpecifiedAllRisks(common.NB_excel_data_map), "Specified All Risks function is having issue(S) . ");
				}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_ContractorsAllRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_ContractorsAllRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcContractorsAllRisks(common.NB_excel_data_map), "Contractors All Risks function is having issue(S) . ");
				}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_ComputersandElectronicRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
				customAssert.assertTrue(funcComputersandElectronicRisks(common.NB_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
				}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_Money")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Money")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
				customAssert.assertTrue(funcMoney(common.NB_excel_data_map), "Money function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_GoodsInTransit")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_GoodsInTransit")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
				customAssert.assertTrue(funcGoodsInTransit(common.NB_excel_data_map), "Goods In Transit function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_MarineCargo")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_MarineCargo")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Marine Cargo"),"Issue while Navigating to Marine Cargo screen . ");
				customAssert.assertTrue(funcMarineCargo(common.NB_excel_data_map), "Marine Cargo function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_CyberandDataSecurity")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_CyberandDataSecurity")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
				customAssert.assertTrue(funcCyberAndDataSecurity(common.NB_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_DirectorsandOfficers")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_DirectorsandOfficers")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Directors and Officers"),"Issue while Navigating to Directors and Officers screen . ");
				customAssert.assertTrue(funcDirectorsAndOfficers(common.NB_excel_data_map), "Directors and Officers function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_FrozenFood")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_FrozenFood")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Frozen Foods"),"Issue while Navigating to Frozen Foods screen . ");
				customAssert.assertTrue(funcFrozenFoods(common.NB_excel_data_map), "Frozen Foods function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_LossofLicence")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_LossofLicence")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Loss of Licence"),"Issue while Navigating to Loss of Licence screen . ");
			customAssert.assertTrue(funcLossofLicence(common.NB_excel_data_map), "Loss of Licence function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_FidelityGuarantee")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_FidelityGuarantee")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
				customAssert.assertTrue(funcFidelityGuarantee(common.NB_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_Terrorism")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
				}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_LegalExpenses")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_LegalExpenses")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
				customAssert.assertTrue(funcLegalExpenses(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Legal Expenses function is having issue(S) . ");
				}
			
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
				Assert.assertTrue(common.funcPremiumSummary(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"RewindAddCover"));
				customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
				customAssert.assertTrue(common.funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
		}
		
		/*if(((String)common.NB_excel_data_map.get("CD_RemoveCover")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcRewindCoversUnCheck(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			Assert.assertTrue(common.funcPremiumSummary(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"RewindRemoveCover"));
			customAssert.assertTrue(common.insuranceTaxVerification());
		}
		
		customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
		customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		customAssert.assertTrue(common.funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
		*///customAssert.assertTrue(common.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) . ");
		
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
}
public boolean funcRewindOperationMTA(){
	
	boolean r_value= true;
	
	try{
		
		if(((String)common.NB_excel_data_map.get("CD_Add_Remove_Cover")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcRewindCoversCheck(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
			if(((String)common.NB_excel_data_map.get("CD_Add_MaterialDamage")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(funcInsuredProperties(common.NB_excel_data_map), "Insured Property function is having issue(S) . ");
			}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_BusinessInterruption")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_BusinessInterruption")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
				customAssert.assertTrue(funcBusinessInterruption(common.NB_excel_data_map), "Business Interruption function is having issue(S) . ");
			}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_Liability")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Liability")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(funcEmployersLiability(common.NB_excel_data_map), "Employers Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
				customAssert.assertTrue(funcELDInformation(common.NB_excel_data_map), "ELD Information function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
				customAssert.assertTrue(funcPublicLiability(common.NB_excel_data_map), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(funcProductsLiability(common.NB_excel_data_map), "Products Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
				customAssert.assertTrue(funcLiabilityInformation(common.NB_excel_data_map), "Liability Information function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_SpecifiedAllRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_SpecifiedAllRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcSpecifiedAllRisks(common.NB_excel_data_map), "Specified All Risks function is having issue(S) . ");
				}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_ContractorsAllRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_ContractorsAllRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcContractorsAllRisks(common.NB_excel_data_map), "Contractors All Risks function is having issue(S) . ");
				}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_ComputersandElectronicRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
				customAssert.assertTrue(funcComputersandElectronicRisks(common.NB_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
				}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_Money")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Money")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
				customAssert.assertTrue(funcMoney(common.NB_excel_data_map), "Money function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_GoodsInTransit")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_GoodsInTransit")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
				customAssert.assertTrue(funcGoodsInTransit(common.NB_excel_data_map), "Goods In Transit function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_MarineCargo")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_MarineCargo")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Marine Cargo"),"Issue while Navigating to Marine Cargo screen . ");
				customAssert.assertTrue(funcMarineCargo(common.NB_excel_data_map), "Marine Cargo function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_CyberandDataSecurity")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_CyberandDataSecurity")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
				customAssert.assertTrue(funcCyberAndDataSecurity(common.NB_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_DirectorsandOfficers")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_DirectorsandOfficers")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Directors and Officers"),"Issue while Navigating to Directors and Officers screen . ");
				customAssert.assertTrue(funcDirectorsAndOfficers(common.NB_excel_data_map), "Directors and Officers function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_FrozenFood")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_FrozenFood")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Frozen Foods"),"Issue while Navigating to Frozen Foods screen . ");
				customAssert.assertTrue(funcFrozenFoods(common.NB_excel_data_map), "Frozen Foods function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_LossofLicence")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_LossofLicence")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Loss of Licence"),"Issue while Navigating to Loss of Licence screen . ");
			customAssert.assertTrue(funcLossofLicence(common.NB_excel_data_map), "Loss of Licence function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_FidelityGuarantee")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_FidelityGuarantee")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
				customAssert.assertTrue(funcFidelityGuarantee(common.NB_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
				}
			if(((String)common.NB_excel_data_map.get("CD_Add_Terrorism")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(funcTerrorism(common.NB_excel_data_map), "Terrorism function is having issue(S) . ");
				}
			
			if(((String)common.NB_excel_data_map.get("CD_Add_LegalExpenses")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_LegalExpenses")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
				customAssert.assertTrue(funcLegalExpenses(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Legal Expenses function is having issue(S) . ");
				}
			
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
				Assert.assertTrue(common.funcPremiumSummary(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"RewindAddCover"));
				customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
				if(common.businessEvent.equalsIgnoreCase("MTA")){
					customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Endorsement On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
				}else{
				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
				}
				customAssert.assertTrue(common.funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
		}
		
		/*if(((String)common.NB_excel_data_map.get("CD_RemoveCover")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcRewindCoversUnCheck(common.NB_excel_data_map), "Select covers function is having issue(S) . ");
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			Assert.assertTrue(common.funcPremiumSummary(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"RewindRemoveCover"));
			customAssert.assertTrue(common.insuranceTaxVerification());
		}
		
		customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
		customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		customAssert.assertTrue(common.funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
		*///customAssert.assertTrue(common.transactionSummary((String)common.NB_excel_data_map.get("Automation Key"), "", CommonFunction.product,CommonFunction.businessEvent), "Transaction Summary function is having issue(S) . ");
		
		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
}
public boolean funcMTAOperation(){
	
	boolean r_value= true;
	
	try{
		
		if(((String)common.MTA_excel_data_map.get("CD_Add_Remove_CoverMTA")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcMTACoversCheck(common.MTA_excel_data_map), "Select covers function is having issue(S) . ");
			if(((String)common.MTA_excel_data_map.get("CD_Add_MaterialDamage")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(funcInsuredProperties(common.MTA_excel_data_map), "Insured Property function is having issue(S) . ");
			}
			
			if(((String)common.MTA_excel_data_map.get("CD_Add_BusinessInterruption")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
				customAssert.assertTrue(funcBusinessInterruption(common.MTA_excel_data_map), "Business Interruption function is having issue(S) . ");
			}
			
			if(((String)common.MTA_excel_data_map.get("CD_Add_Liability")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Liability")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(funcEmployersLiability(common.MTA_excel_data_map), "Employers Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
				customAssert.assertTrue(funcELDInformation(common.MTA_excel_data_map), "ELD Information function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
				customAssert.assertTrue(funcPublicLiability(common.MTA_excel_data_map), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(funcProductsLiability(common.MTA_excel_data_map), "Products Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
				customAssert.assertTrue(funcLiabilityInformation(common.MTA_excel_data_map), "Liability Information function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_SpecifiedAllRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_SpecifiedAllRisks")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcSpecifiedAllRisks(common.MTA_excel_data_map), "Specified All Risks function is having issue(S) . ");
				}
			
			if(((String)common.MTA_excel_data_map.get("CD_Add_ContractorsAllRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_ContractorsAllRisks")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcContractorsAllRisks(common.MTA_excel_data_map), "Contractors All Risks function is having issue(S) . ");
				}
			
			if(((String)common.MTA_excel_data_map.get("CD_Add_ComputersandElectronicRisks")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
				customAssert.assertTrue(funcComputersandElectronicRisks(common.MTA_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
				}
			
			if(((String)common.MTA_excel_data_map.get("CD_Add_Money")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Money")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
				customAssert.assertTrue(funcMoney(common.MTA_excel_data_map), "Money function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_GoodsInTransit")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_GoodsInTransit")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
				customAssert.assertTrue(funcGoodsInTransit(common.MTA_excel_data_map), "Goods In Transit function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_MarineCargo")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_MarineCargo")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Marine Cargo"),"Issue while Navigating to Marine Cargo screen . ");
				customAssert.assertTrue(funcMarineCargo(common.MTA_excel_data_map), "Marine Cargo function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_CyberandDataSecurity")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_CyberandDataSecurity")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
				customAssert.assertTrue(funcCyberAndDataSecurity(common.MTA_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_DirectorsandOfficers")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_DirectorsandOfficers")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Directors and Officers"),"Issue while Navigating to Directors and Officers screen . ");
				customAssert.assertTrue(funcDirectorsAndOfficers(common.MTA_excel_data_map), "Directors and Officers function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_FrozenFood")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_FrozenFood")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Frozen Foods"),"Issue while Navigating to Frozen Foods screen . ");
				customAssert.assertTrue(funcFrozenFoods(common.MTA_excel_data_map), "Frozen Foods function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_LossofLicence")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_LossofLicence")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Loss of Licence"),"Issue while Navigating to Loss of Licence screen . ");
			customAssert.assertTrue(funcLossofLicence(common.MTA_excel_data_map), "Loss of Licence function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_FidelityGuarantee")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_FidelityGuarantee")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
				customAssert.assertTrue(funcFidelityGuarantee(common.MTA_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
				}
			if(((String)common.MTA_excel_data_map.get("CD_Add_Terrorism")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(funcTerrorism(common.MTA_excel_data_map), "Terrorism function is having issue(S) . ");
				}
			
			if(((String)common.MTA_excel_data_map.get("CD_Add_LegalExpenses")).equals("Yes")&&
					((String)common.NB_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
				customAssert.assertTrue(funcLegalExpenses(common.MTA_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Legal Expenses function is having issue(S) . ");
				}
			
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
				Assert.assertTrue(common.funcPremiumSummary(common.MTA_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,""));
				
				
//				customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
//				customAssert.assertTrue(common.funcSearchPolicy(common.NB_excel_data_map), "Policy Search function is having issue(S) . ");
//				customAssert.assertTrue(common.funcVerifyPolicyStatus(common.NB_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
//				customAssert.assertTrue(common.funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
		}
		if(((String)common.MTA_excel_data_map.get("CD_Change_CoverMTA")).equalsIgnoreCase("Yes")){
			
				if(((String)common.MTA_excel_data_map.get("CD_Change_MaterialDamage")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_MaterialDamage")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
					customAssert.assertTrue(funcInsuredProperties(common.MTA_excel_data_map), "Insured Property function is having issue(S) . ");
				}
				
				if(((String)common.MTA_excel_data_map.get("CD_Change_BusinessInterruption")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_BusinessInterruption")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
					customAssert.assertTrue(funcBusinessInterruption(common.MTA_excel_data_map), "Business Interruption function is having issue(S) . ");
				}
				
				if(((String)common.MTA_excel_data_map.get("CD_Change_Liability")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_Liability")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
					customAssert.assertTrue(funcEmployersLiability(common.MTA_excel_data_map), "Employers Liability function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
					customAssert.assertTrue(funcELDInformation(common.MTA_excel_data_map), "ELD Information function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
					customAssert.assertTrue(funcPublicLiability(common.MTA_excel_data_map), "Public Liability function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
					customAssert.assertTrue(funcProductsLiability(common.MTA_excel_data_map), "Products Liability function is having issue(S) . ");
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
					customAssert.assertTrue(funcLiabilityInformation(common.MTA_excel_data_map), "Liability Information function is having issue(S) . ");
					}
				if(((String)common.MTA_excel_data_map.get("CD_Change_SpecifiedAllRisks")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_SpecifiedAllRisks")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
					customAssert.assertTrue(funcSpecifiedAllRisks(common.MTA_excel_data_map), "Specified All Risks function is having issue(S) . ");
					}
				
				if(((String)common.MTA_excel_data_map.get("CD_Change_ContractorsAllRisks")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_ContractorsAllRisks")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
					customAssert.assertTrue(funcContractorsAllRisks(common.MTA_excel_data_map), "Contractors All Risks function is having issue(S) . ");
					}
				
				if(((String)common.MTA_excel_data_map.get("CD_Change_ComputersandElectronicRisks")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
					customAssert.assertTrue(funcComputersandElectronicRisks(common.MTA_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
					}
				
				if(((String)common.MTA_excel_data_map.get("CD_Change_Money")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_Money")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
					customAssert.assertTrue(funcMoney(common.MTA_excel_data_map), "Money function is having issue(S) . ");
					}
				if(((String)common.MTA_excel_data_map.get("CD_Change_GoodsInTransit")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_GoodsInTransit")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
					customAssert.assertTrue(funcGoodsInTransit(common.MTA_excel_data_map), "Goods In Transit function is having issue(S) . ");
					}
				if(((String)common.MTA_excel_data_map.get("CD_Change_MarineCargo")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_MarineCargo")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Marine Cargo"),"Issue while Navigating to Marine Cargo screen . ");
					customAssert.assertTrue(funcMarineCargo(common.MTA_excel_data_map), "Marine Cargo function is having issue(S) . ");
					}
				if(((String)common.MTA_excel_data_map.get("CD_Change_CyberandDataSecurity")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_CyberandDataSecurity")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
					customAssert.assertTrue(funcCyberAndDataSecurity(common.MTA_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
					}
				if(((String)common.MTA_excel_data_map.get("CD_Change_DirectorsandOfficers")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_DirectorsandOfficers")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Directors and Officers"),"Issue while Navigating to Directors and Officers screen . ");
					customAssert.assertTrue(funcDirectorsAndOfficers(common.MTA_excel_data_map), "Directors and Officers function is having issue(S) . ");
					}
				if(((String)common.MTA_excel_data_map.get("CD_Change_FrozenFood")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_FrozenFood")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Frozen Foods"),"Issue while Navigating to Frozen Foods screen . ");
					customAssert.assertTrue(funcFrozenFoods(common.MTA_excel_data_map), "Frozen Foods function is having issue(S) . ");
					}
				if(((String)common.MTA_excel_data_map.get("CD_Change_LossofLicence")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_LossofLicence")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Loss of Licence"),"Issue while Navigating to Loss of Licence screen . ");
				customAssert.assertTrue(funcLossofLicence(common.MTA_excel_data_map), "Loss of Licence function is having issue(S) . ");
					}
				if(((String)common.MTA_excel_data_map.get("CD_Change_FidelityGuarantee")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_FidelityGuarantee")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
					customAssert.assertTrue(funcFidelityGuarantee(common.MTA_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
					}
				if(((String)common.MTA_excel_data_map.get("CD_Change_Terrorism")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_Terrorism")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
					customAssert.assertTrue(funcTerrorism(common.MTA_excel_data_map), "Terrorism function is having issue(S) . ");
					}
				
				if(((String)common.MTA_excel_data_map.get("CD_Change_LegalExpenses")).equals("Yes")&&
						((String)common.NB_excel_data_map.get("CD_LegalExpenses")).equals("Yes")){		
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
					customAssert.assertTrue(funcLegalExpenses(common.MTA_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Legal Expenses function is having issue(S) . ");
					}
				
					customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
					Assert.assertTrue(common.funcPremiumSummary(common.MTA_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,""));
		}		
	}catch(Throwable t){
		return false;
		
	}
	
	return r_value;
}

/*public boolean funcPremiumSummary(Map<Object, Object> map_data,String code,String event){
	
	boolean r_value= true;
	Date currentDate = new Date();
	String testName = (String)map_data.get("Automation Key");
	String customPolicyDuration=null;
	SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
	//String inception_date = common.daysIncrementWithOutFormation(df.format(currentDate), 0);
	try{
		customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page navigations issue(S)");
		
		int policy_Duration = Integer.parseInt((String)map_data.get("PS_Duration"));
		boolean negativeFlag = policy_Duration < 0 ?false:true;
		customAssert.assertTrue(negativeFlag , "Policy Duration should be non-negative value . ");
		boolean Months18_Flag = policy_Duration < 550 ?true:false;
		customAssert.assertTrue(Months18_Flag , "Policy Duration Should be less than 18 Months ( <= 549 Days) . ");
		policy_Duration--;
		String Policy_End_Date = common.daysIncrementWithOutFormation(df1.format(currentDate), policy_Duration);
		
		
		if(((String)map_data.get("PS_DefaultStartEndDate")).equals("No")){
			customAssert.assertTrue(k.Click("Policy_Start_Date"), "Unable to Click Policy_Start_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_Start_Date", (String)map_data.get("PS_PolicyStartDate")),"Unable to Enter Policy_Start_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
			customAssert.assertTrue(k.Click("Policy_End_Date"), "Unable to Click Policy_End_Date date picker .");
			customAssert.assertTrue(k.Input("Policy_End_Date", Policy_End_Date),"Unable to Enter Policy_End_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
			
		}	
		k.waitTwoSeconds();
		customAssert.assertTrue(k.SelectRadioBtn("Payment_Warranty_rules", (String)map_data.get("PS_PaymentWarrantyRules")),"Unable to Select Payment_Warranty_rules radio button . ");
		
		if(((String)map_data.get("PS_PaymentWarrantyRules")).equals("Yes")){
			
			customAssert.assertTrue(k.Click("Payment_Warranty_Due_Date"), "Unable to Click Payment_Warranty_Due_Date date picker .");
			customAssert.assertTrue(k.Input("Payment_Warranty_Due_Date", (String)map_data.get("PS_PaymentWarrantyDueDate")),"Unable to Enter Payment_Warranty_Due_Date .");
			customAssert.assertTrue(k.Click("calander_btn"), "Unable to click on done button in calander.");
		}
		customAssert.assertTrue(common.funcPremiumSummaryTable(code, event, map_data),"Premium summary Calculation function failed");		
		customAssert.assertTrue(k.Click("CCF_Btn_Save"), "Unable to click on Save Button on Premium Summary .");
		customPolicyDuration = k.getText("Policy_Duration");
		customAssert.assertTrue(WriteDataToXl(code+"_"+event, "Premium Summary", testName, "PS_Duration", customPolicyDuration,common.NB_excel_data_map),"Error while writing Policy Duration data to excel .");
		TestUtil.reportStatus("Policy Duration = "+customPolicyDuration+" Days . ", "Info", true);
		
		TestUtil.reportStatus("Premium Summary details are filled sucessfully . ", "Info", true);
		
	}catch(Throwable t){
		return false;
	}
	
	return r_value;
	}*/

public boolean CancellationProcess(Map<Object, Object> map_data,String code,String event) throws Throwable{
	Map<Object, Object> can_data=common.CAN_excel_data_map;
	customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"),"Unable to Navigate Premium Summary Screen");
	customAssert.assertTrue(common.funcPageNavigation("Premium Summary", ""),"Premium Summary page is having issue(S)");
	String Total_Premium = k.getTextByXpath("html/body/div[3]/form/div/div[2]/table/tbody/tr[4]/td[3]").replaceAll(",", "");
	String pol_StartDt=k.getText("cancelStDt");
	String cancelEndDt=k.getText("cancelEndDt");
	customAssert.assertTrue(k.Click("cancel_btn"),"Unable to click on Cancel button on Premium summary page");
	customAssert.assertTrue(common.funcPageNavigation("Cancel Policy", ""),"Unable to load Cancel Policy page");
	int cnDays= Integer.parseInt((String)can_data.get("CancelAfterDays"));
	customAssert.assertTrue(pol_StartDt.contains(k.getText("cancelStDt")),"Start Date is different on cancellation page");
	customAssert.assertTrue(cancelEndDt.contains(k.getText("cancelEndDt")),"End Date is different on cancellation page");
	//String pol_EndDt=k.getAttribute("cancelStDt", "value");
	int idate= Integer.parseInt(CONFIG.getProperty("DaysIncrementNumber"));
	//customAssert.assertTrue(pol_StartDt.contentEquals(TestUtil.returnDate(idate)),"Policy Start Day is not matching");
	customAssert.assertTrue(k.Input("cancelDt", TestUtil.returnDate(cnDays+idate)));
	customAssert.assertTrue(k.Type("cancelRsn", "Test Cancellation"));
	can_data.put("cancelDate",TestUtil.returnDate(cnDays+idate));
	can_data.put("cancelReason","Test Cancellation");
	customAssert.assertTrue(k.Click("cancelCtnu"));
	k.waitTwoSeconds();
	customAssert.assertTrue(k.isDisplayed("cancelRemDayslbl"),"Unable to move cancel summary page ");
	TestUtil.reportStatus("Cancellation Summary Page loaded Successfully", "Info", true);
	//Assert.assertTrue(common.funcPageNavigation("Cancel Policy", ""),"Unable to load Cancel Policy summary page");

	customAssert.assertTrue(k.getText("cnclSummaryStDt").contentEquals(pol_StartDt),"Policy Start date is not matching on cancellation summary page");
	TestUtil.reportStatus("Cancellation Date is <b>"+k.getText("cancelDate")+ " with remaining days "+k.getText("cancelRemDays"), "Info", true);
	k.pressDownKeyonPage();
	// - html/body/div[3]/form/div/table[3]/tbody/tr[19]/td[2]
	String cAP_path1 = "html/body/div[3]/form/div/table[3]/tbody/tr[";
	int tblRow = driver.findElements(By.xpath("html/body/div[3]/form/div/table[3]/tbody/tr")).size();
	String cAP_path = cAP_path1+tblRow+"]/td[2]";
	String currentAP_Total= k.getTextByXpath(cAP_path).replaceAll(",", "");
	TestUtil.reportStatus("Current Annual Premium Total "+currentAP_Total, "Info", false);
	String canAP_path1 = "html/body/div[3]/form/div/table[4]/tbody/tr[";
	int tblRow1 = driver.findElements(By.xpath("html/body/div[3]/form/div/table[4]/tbody/tr")).size();
	String canAP_path = canAP_path1+tblRow1+"]/td[2]";
	String canAP_Total= k.getTextByXpath(canAP_path).replaceAll(",", "");
	TestUtil.reportStatus("Cancellation Return Premium Total "+canAP_Total, "Info", false);
	String can_GrandTotal=k.getText("can_GtTotal");
	TestUtil.reportStatus("Cancellation Grand Total "+can_GrandTotal, "Info", false);
	
	common.CancellationPremiumTables(code, event, "AP", true);
	common.CancellationPremiumTables(code, event, "CP", true);
	k.Click("cancelRecalc");
	common.CancellationPremiumTables(code, event, "CP", false);
	can_GrandTotal=k.getText("can_GtTotal");
	TestUtil.reportStatus("Cancellation Grand Total after Re-Calculation <b>"+can_GrandTotal, "Info", false);
	// Click on Cancel Policy button
	//common.funcButtonSelection("Cancel Policy");
	k.Click("cancelPol");
	customAssert.assertTrue(k.AcceptPopup(),"Cancel Pop-up handelling failed");
	
//	if (cnDays==0){
//		customAssert.assertTrue(currentAP_Total.contentEquals(canAP_Total),"Current Annual Premium and Cancellation Total are not matched");
//	}else{
//		customAssert.assertFalse(currentAP_Total.contentEquals(canAP_Total),"Current Annual Premium and Cancellation Total are not matched");
//	}
//	

	return true;
}


/**
 * 
 * Renewal Flow : 
 * 
 * 
 */

public void RenewalFlow(String code,String fileName) throws Throwable{
	
	try {
		common.currentRunningFlow = "Renewal";
		customAssert.assertTrue(common.StingrayLogin("PEN"),"Unable to login.");
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Pending"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		customAssert.assertTrue(common.funcUpdateCoverDetails(common.Renewal_excel_data_map));
		customAssert.assertTrue(common.funcMenuSelection("Navigate", "Premium Summary"));
		String agencyName = k.getText("CCF_AgencyName");
		int length = agencyName.length();
		int index = agencyName.indexOf("of");
		String agency = agencyName.substring(index+3, length);
		TestUtil.WriteDataToXl(CommonFunction.product+"_"+common.currentRunningFlow, "QuoteCreation",(String)common.Renewal_excel_data_map.get("Automation Key"), "QC_AgencyName", agency, common.Renewal_excel_data_map);
		customAssert.assertTrue(common.funcPremiumSummary(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,""), "Premium Summary function is having issue(S) . ");
		customAssert.assertTrue(common.decideRENEWALFlow(common.Renewal_excel_data_map));
		customAssert.assertEquals(common.final_err_pdf_count,0,"Varification Errors in PDF Documents . ");
		TestUtil.reportStatus("Current Flow is restricted to <b>[  "+(String)common.Renewal_excel_data_map.get(common.currentRunningFlow+"_Status")+"  ]</b> status. ", "Info", true);
		customAssert.assertTrue(common.StingrayLogout(), "Unable to Logout.");
		TestUtil.reportTestCasePassed((String)common.Renewal_excel_data_map.get("Automation Key"));
		
	} catch (Exception e) {
		
	}
	
	TestUtil.reportStatus("Test Method of Renewal For - "+code, "Pass", true);
	
	
}

public boolean funcEndorsmentOnCoverOperation(Map<Object, Object> map_data){
	
	try {
		
		customAssert.assertTrue(funcRenewalOnCoveOperation(map_data));
		customAssert.assertTrue(common.createEndorsement(map_data));
		customAssert.assertTrue(common.funcButtonSelection("Discard Endorsement"));
		customAssert.assertTrue(k.AcceptPopup());
		TestUtil.reportStatus("Endorsment discarded successfully. ", "Info", true);
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		customAssert.assertTrue(common.createEndorsement(map_data));
		customAssert.assertTrue(common_CCF.funcRenewalSubmittedChange(map_data),"Error in Renewal Submitted Change method");
		customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
		customAssert.assertTrue(common.funcAssignUnderWriter((String)map_data.get("Renewal_AssignUnderwritter")));
		customAssert.assertTrue(k.Click("Quote_btn"),"Unable to click on Quote button.");
		customAssert.assertTrue(k.Click("Quote_proceed_btn"),"Unable to click on Proceed button.");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Endorsement Quoted"), "Verify Policy Status (Renewal Quoted) function is having issue(S) . ");
		customAssert.assertTrue(common.funcGoOnCover(map_data), "Go On Cover function is having issue(S) . ");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Endorsement On Cover"), "Verify Policy Status (Renewal On Cover) function is having issue(S) . ");
		customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Documents function is having issue(S) . ");
		customAssert.assertTrue(common.transactionSummary_Endorsment((String)map_data.get("Automation Key"), "", CommonFunction.product, CommonFunction.businessEvent), "Transaction Summary function is having issue(S) . ");
		
		return true;
		
	} catch (Exception e) {
		
		return false;
	}
	
	
}

public boolean funcRenewalRewindOperation(Map<Object, Object> map_data){
	
	try {
		
		customAssert.assertTrue(funcRenewalOnCoveOperation(map_data));
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common.funcRewind());
		if(((String)common.Renewal_excel_data_map.get("CD_Add_Remove_Cover")).equalsIgnoreCase("Yes")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Covers"),"Issue while Navigating to Covers  . ");
			customAssert.assertTrue(common.funcRewindCoversCheck(common.Renewal_excel_data_map), "Select covers function is having issue(S) . ");
			if(((String)common.Renewal_excel_data_map.get("CD_Add_MaterialDamage")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_MaterialDamage")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(funcInsuredProperties(common.NB_excel_data_map), "Insured Property function is having issue(S) . ");
			}
			
			if(((String)common.Renewal_excel_data_map.get("CD_Add_BusinessInterruption")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_BusinessInterruption")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
				customAssert.assertTrue(funcBusinessInterruption(common.Renewal_excel_data_map), "Business Interruption function is having issue(S) . ");
			}
			
			if(((String)common.Renewal_excel_data_map.get("CD_Add_Liability")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_Liability")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(funcEmployersLiability(common.Renewal_excel_data_map), "Employers Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
				customAssert.assertTrue(funcELDInformation(common.Renewal_excel_data_map), "ELD Information function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
				customAssert.assertTrue(funcPublicLiability(common.Renewal_excel_data_map), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(funcProductsLiability(common.Renewal_excel_data_map), "Products Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
				customAssert.assertTrue(funcLiabilityInformation(common.Renewal_excel_data_map), "Liability Information function is having issue(S) . ");
				}
			if(((String)common.Renewal_excel_data_map.get("CD_Add_SpecifiedAllRisks")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_SpecifiedAllRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcSpecifiedAllRisks(common.Renewal_excel_data_map), "Specified All Risks function is having issue(S) . ");
				}
			
			if(((String)common.Renewal_excel_data_map.get("CD_Add_ContractorsAllRisks")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_ContractorsAllRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcContractorsAllRisks(common.Renewal_excel_data_map), "Contractors All Risks function is having issue(S) . ");
				}
			
			if(((String)common.Renewal_excel_data_map.get("CD_Add_ComputersandElectronicRisks")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_ComputersandElectronicRisks")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
				customAssert.assertTrue(funcComputersandElectronicRisks(common.Renewal_excel_data_map), "Computers and Electronic Risks function is having issue(S) . ");
				}
			
			if(((String)common.Renewal_excel_data_map.get("CD_Add_Money")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_Money")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
				customAssert.assertTrue(funcMoney(common.Renewal_excel_data_map), "Money function is having issue(S) . ");
				}
			if(((String)common.Renewal_excel_data_map.get("CD_Add_GoodsInTransit")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_GoodsInTransit")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
				customAssert.assertTrue(funcGoodsInTransit(common.Renewal_excel_data_map), "Goods In Transit function is having issue(S) . ");
				}
			if(((String)common.Renewal_excel_data_map.get("CD_Add_MarineCargo")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_MarineCargo")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Marine Cargo"),"Issue while Navigating to Marine Cargo screen . ");
				customAssert.assertTrue(funcMarineCargo(common.Renewal_excel_data_map), "Marine Cargo function is having issue(S) . ");
				}
			if(((String)common.Renewal_excel_data_map.get("CD_Add_CyberandDataSecurity")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_CyberandDataSecurity")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
				customAssert.assertTrue(funcCyberAndDataSecurity(common.Renewal_excel_data_map), "Cyber and Data Security function is having issue(S) . ");
				}
			if(((String)common.Renewal_excel_data_map.get("CD_Add_DirectorsandOfficers")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_DirectorsandOfficers")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Directors and Officers"),"Issue while Navigating to Directors and Officers screen . ");
				customAssert.assertTrue(funcDirectorsAndOfficers(common.Renewal_excel_data_map), "Directors and Officers function is having issue(S) . ");
				}
			if(((String)common.Renewal_excel_data_map.get("CD_Add_FrozenFood")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_FrozenFood")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Frozen Foods"),"Issue while Navigating to Frozen Foods screen . ");
				customAssert.assertTrue(funcFrozenFoods(common.Renewal_excel_data_map), "Frozen Foods function is having issue(S) . ");
				}
			if(((String)common.Renewal_excel_data_map.get("CD_Add_LossofLicence")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_LossofLicence")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Loss of Licence"),"Issue while Navigating to Loss of Licence screen . ");
			customAssert.assertTrue(funcLossofLicence(common.Renewal_excel_data_map), "Loss of Licence function is having issue(S) . ");
				}
			if(((String)common.Renewal_excel_data_map.get("CD_Add_FidelityGuarantee")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_FidelityGuarantee")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
				customAssert.assertTrue(funcFidelityGuarantee(common.Renewal_excel_data_map), "Fidelity Guarantee function is having issue(S) . ");
				}
			if(((String)common.Renewal_excel_data_map.get("CD_Add_Terrorism")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_Terrorism")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(funcTerrorism(common.Renewal_excel_data_map), "Terrorism function is having issue(S) . ");
				}
			
			if(((String)common.Renewal_excel_data_map.get("CD_Add_LegalExpenses")).equals("Yes")&&
					((String)common.Renewal_excel_data_map.get("CD_LegalExpenses")).equals("No")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
				customAssert.assertTrue(funcLegalExpenses(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent), "Legal Expenses function is having issue(S) . ");
				}
			
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
				customAssert.assertTrue(common.funcPremiumSummary_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,""), "Premium Summary function is having issue(S) . ");
		}
		customAssert.assertTrue(common.funcButtonSelection("Put Rewind On Cover"));
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover"), "Verify Policy Status (Submitted (Rewind)) function is having issue(S) . ");
		customAssert.assertTrue(common.funcPDFdocumentVerification_Rewind("Documents"), "Document verification function is having issue(S) . ");
		customAssert.assertTrue(common.transactionSummary((String)map_data.get("Automation Key"), "", CommonFunction.product, CommonFunction.businessEvent), "Transaction Summary function is having issue(S) . ");
		return true;
		
	} catch (Exception e) {
		
		return false;
	}
	
	
}

public boolean funcRenewalOnCoveOperation(Map<Object, Object> map_data){
	
	try {
		
		if(!(TestBase.product.equals("SPI") || TestBase.product.equals("PIA"))){
			
		
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Pending"), "Verify Policy Status (Renewal Pending) function is having issue(S) . ");
		
		if(TestBase.product.equals("SPI") || TestBase.product.equals("PIA")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
			customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter in renewal.");
		}
		else{
			customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
			customAssert.assertTrue(common.funcAssignUnderWriter((String)map_data.get("Renewal_AssignUnderwritter")));
		}
		customAssert.assertTrue(common.funcButtonSelection("Send to Underwriter"));
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Submitted"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
		
	}
		if(((String)common.Renewal_excel_data_map.get("CD_isRenewalSubmittedChange")).equalsIgnoreCase("Yes")){
			
			switch(TestBase.product){
				case "SPI":
					customAssert.assertTrue(common_SPI.funcRenewalSubmittedChange(),"Error in Renewal Submitted Change method");
				break;
				case "CCF":
					customAssert.assertTrue(common_CCF.funcRenewalSubmittedChange(map_data),"Error in Renewal Submitted Change method");
				break;
				case "PIA":
					customAssert.assertTrue(common_PIA.funcRenewalSubmittedChange(),"Error in PIA Renewal Submitted Change method");
				break;
				case "CCI":
					customAssert.assertTrue(common_CCI.funcRenewalSubmittedChange(map_data),"Error in Renewal Submitted Change method");
				break;
			}
		}
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(k.Click("Quote_btn"),"Unable to click on Quote button.");
		customAssert.assertTrue(k.Click("Quote_proceed_btn"),"Unable to click on Proceed button.");
		//This will get UK date after putting policy on Quoted status.
		String quoteDate = common.getUKDate();
		common.Renewal_excel_data_map.put("QuoteDate", quoteDate);
		/////////////////////////////////////////////////////////////
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Quoted"), "Verify Policy Status (Renewal Quoted) function is having issue(S) . ");
		customAssert.assertTrue(common.funcPDFdocumentVerification("Draft Documents"), "Draft Documents function is having issue(S) . ");
		customAssert.assertTrue(common.funcGoOnCover(map_data), "Go On Cover function is having issue(S) . ");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		if(TestBase.product.equals("SPI") || TestBase.product.equals("PIA")){
			customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover (Unconfirmed)"), "Verify Policy Status (Renewal On Cover (Unconfirmed)) function is having issue(S) . ");
			customAssert.assertTrue(common_SPI.func_Confirm_Policy(), "Error while changing SPI policy Status from Unconfirmed to Confirmed . ");
			customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
			customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
			customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover"), "Verify Policy Status (Renewal On Cover) function is having issue(S) . ");
		}else{
			customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal On Cover"), "Verify Policy Status (Renewal On Cover) function is having issue(S) . ");
		}
	
		customAssert.assertTrue(common.funcPDFdocumentVerification("Documents"), "Documents function is having issue(S) . ");
		if(TestBase.product.equalsIgnoreCase("CCI"))
			customAssert.assertTrue(common_CCI.transactionSummary_New((String)common.Renewal_excel_data_map.get("Automation Key"), "", "CCI", "Renewal"), "Transaction Summary function is having issue(S) . ");
		else
			customAssert.assertTrue(common.transactionSummary((String)common.Renewal_excel_data_map.get("Automation Key"), "", "CCF", "Renewal"), "Transaction Summary function is having issue(S) . ");
		
		return true;
		
	} catch (Exception e) {
		
		return false;
	}
	
	
	
}

public boolean funcRenewalNTUOperation(Map<Object, Object> map_data){
	
	try {
		
		
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Pending"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
		if(TestBase.product.equals("SPI") || TestBase.product.equals("PIA")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
			customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter in renewal.");
		}
		else{
			customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
			customAssert.assertTrue(common.funcAssignUnderWriter((String)map_data.get("Renewal_AssignUnderwritter")));
		}
		customAssert.assertTrue(common.funcButtonSelection("Send to Underwriter"));
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Submitted"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
		
		if(((String)common.Renewal_excel_data_map.get("CD_isRenewalSubmittedChange")).equalsIgnoreCase("Yes")){
			
			switch(TestBase.product){
				case "SPI":
					customAssert.assertTrue(common_SPI.funcRenewalSubmittedChange(),"Error in Renewal Submitted Change method");
				break;
				case "CCF":
					customAssert.assertTrue(common_CCF.funcRenewalSubmittedChange(map_data),"Error in Renewal Submitted Change method");
				break;
				case "PIA":
					customAssert.assertTrue(common_PIA.funcRenewalSubmittedChange(),"Error in PIA Renewal Submitted Change method");
				break;
			}
		}
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(k.Click("Quote_btn"),"Unable to click on Quote button.");
		customAssert.assertTrue(k.Click("Quote_proceed_btn"),"Unable to click on Proceed button.");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Quoted"), "Verify Policy Status (Renewal Quoted) function is having issue(S) . ");
		customAssert.assertTrue(common.funcPDFdocumentVerification("Draft Documents"), "Draft Documents function is having issue(S) . ");
		customAssert.assertTrue(common.funcNTU(map_data));
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal NTU"), "Verify Policy Status (NTU) function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyNTUstatus(common.Renewal_excel_data_map), "Verify Policy Status (NTU Page) function is having issue(S) . ");
		
		return true;
		
	} catch (Exception e) {
		
		return false;
	}
	
	
}

public boolean funcRenewalDeclinedOperation(Map<Object, Object> map_data){
	
	
	try {
		
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Pending"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
		if(TestBase.product.equals("SPI") || TestBase.product.equals("PIA")){
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
			customAssert.assertTrue(common_SPI.funcAssignPolicyToUW(), "Error while assigning Policy to Underwriter in renewal.");
		}
		else{
			customAssert.assertTrue(common.funcButtonSelection("Assign Underwriter"));
			customAssert.assertTrue(common.funcAssignUnderWriter((String)map_data.get("Renewal_AssignUnderwritter")));
		}
		customAssert.assertTrue(common.funcButtonSelection("Send to Underwriter"));
		customAssert.assertTrue(common.funcMenuSelection("Policies", ""),"");
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(map_data), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(map_data,CommonFunction.product,CommonFunction.businessEvent,"Renewal Submitted"), "Verify Policy Status (Renewal Submitted) function is having issue(S) . ");
		
		if(((String)common.Renewal_excel_data_map.get("CD_isRenewalSubmittedChange")).equalsIgnoreCase("Yes")){
			
			switch(TestBase.product){
				case "SPI":
					customAssert.assertTrue(common_SPI.funcRenewalSubmittedChange(),"Error in Renewal Submitted Change method");
				break;
				case "CCF":
					customAssert.assertTrue(common_CCF.funcRenewalSubmittedChange(map_data),"Error in Renewal Submitted Change method");
				break;
				case "PIA":
					customAssert.assertTrue(common_PIA.funcRenewalSubmittedChange(),"Error in PIA Renewal Submitted Change method");
				break;
			}
		}
		customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
		customAssert.assertTrue(common.funcDecline(common.Renewal_excel_data_map));
		customAssert.assertTrue(common.funcSearchPolicy_Renewal(common.Renewal_excel_data_map), "Policy Search function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyPolicyStatus_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,"Renewal Declined"), "Verify Policy Status (Declined) function is having issue(S) . ");
		customAssert.assertTrue(common.funcVerifyDeclineNTUstatus(common.Renewal_excel_data_map), "Verify Policy Status (Decline Page) function is having issue(S) . ");
		
		
		return true;
		
	} catch (Exception e) {
		
		return false;
	}
	
}
	

public boolean funcRenewalSubmittedChange(Map<Object, Object> map_data){
	
	try {
		
			//////////////////////////////////////////
					
			/*if(((String)map_data.get("CD_MaterialDamage")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Insured Properties"),"Issue while Navigating to Insured Properties  . ");
				customAssert.assertTrue(funcInsuredProperties(map_data), "Insured Property function is having issue(S) . ");
			}
			
			if(((String)map_data.get("CD_BusinessInterruption")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Business Interruption"),"Issue while Navigating to Business Interruption  . ");
				customAssert.assertTrue(funcBusinessInterruption(map_data), "Business Interruption function is having issue(S) . ");
			}
			
			if(((String)map_data.get("CD_Liability")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Employers Liability"),"Issue while Navigating to Employers Liability  . ");
				customAssert.assertTrue(funcEmployersLiability(map_data), "Employers Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","ELD Information"),"Issue while Navigating to ELD Information  . ");
				customAssert.assertTrue(funcELDInformation(map_data), "ELD Information function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Public Liability"),"Issue while Navigating to Public Liability  . ");
				customAssert.assertTrue(funcPublicLiability(map_data), "Public Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Products Liability"),"Issue while Navigating to Products Liability  . ");
				customAssert.assertTrue(funcProductsLiability(map_data), "Products Liability function is having issue(S) . ");
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Liability Information"),"Issue while Navigating to Liability Information  . ");
				customAssert.assertTrue(funcLiabilityInformation(map_data), "Liability Information function is having issue(S) . ");
			}
			if(((String)map_data.get("CD_SpecifiedAllRisks")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Specified All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcSpecifiedAllRisks(map_data), "Specified All Risks function is having issue(S) . ");
			}
			
			if(((String)map_data.get("CD_ContractorsAllRisks")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Contractors All Risks"),"Issue while Navigating to Specified All Risks  . ");
				customAssert.assertTrue(funcContractorsAllRisks(map_data), "Contractors All Risks function is having issue(S) . ");
			}
			
			if(((String)map_data.get("CD_ComputersandElectronicRisks")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Computers and Electronic Risks"),"Issue while Navigating to Computers and Electronic Risks  . ");
				customAssert.assertTrue(funcComputersandElectronicRisks(map_data), "Computers and Electronic Risks function is having issue(S) . ");
			}
			
			if(((String)map_data.get("CD_Money")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Money"),"Issue while Navigating to Money screen . ");
				customAssert.assertTrue(funcMoney(map_data), "Money function is having issue(S) . ");
			}
			if(((String)map_data.get("CD_GoodsInTransit")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Goods In Transit"),"Issue while Navigating to Goods In Transit screen . ");
				customAssert.assertTrue(funcGoodsInTransit(map_data), "Goods In Transit function is having issue(S) . ");
			}
			if(((String)map_data.get("CD_MarineCargo")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Marine Cargo"),"Issue while Navigating to Marine Cargo screen . ");
				customAssert.assertTrue(funcMarineCargo(map_data), "Marine Cargo function is having issue(S) . ");
			}
			if(((String)map_data.get("CD_CyberandDataSecurity")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Cyber and Data Security"),"Issue while Navigating to Cyber and Data Security screen . ");
				customAssert.assertTrue(funcCyberAndDataSecurity(map_data), "Cyber and Data Security function is having issue(S) . ");
			}*/
			
			if(((String)map_data.get("CD_DirectorsandOfficers")).equals("Yes") && ((String)map_data.get("DO_ChangeValue")).equals("Yes")){		
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Directors and Officers"),"Issue while Navigating to Directors and Officers screen . ");
			customAssert.assertTrue(funcDirectorsAndOfficers(map_data), "Directors and Officers function is having issue(S) . ");
			}
			/*
			if(((String)map_data.get("CD_FrozenFood")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Frozen Foods"),"Issue while Navigating to Frozen Foods screen . ");
				customAssert.assertTrue(funcFrozenFoods(map_data), "Frozen Foods function is having issue(S) . ");
			}
			if(((String)map_data.get("CD_LossofLicence")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Loss of Licence"),"Issue while Navigating to Loss of Licence screen . ");
				customAssert.assertTrue(funcLossofLicence(map_data), "Loss of Licence function is having issue(S) . ");
			}
			if(((String)map_data.get("CD_FidelityGuarantee")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Fidelity Guarantee"),"Issue while Navigating to Fidelity Guarantee screen . ");
				customAssert.assertTrue(funcFidelityGuarantee(map_data), "Fidelity Guarantee function is having issue(S) . ");
			}
			if(((String)map_data.get("CD_Terrorism")).equals("Yes") && ((String)map_data.get("TER_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Terrorism"),"Issue while Navigating to Terrorism screen . ");
				customAssert.assertTrue(funcTerrorism(map_data), "Terrorism function is having issue(S) . ");
			}
			
			if(((String)map_data.get("CD_LegalExpenses")).equals("Yes") && ((String)map_data.get("MD_ChangeValue")).equals("Yes")){		
				customAssert.assertTrue(common.funcMenuSelection("Navigate","Legal Expenses"),"Issue while Navigating to Legal Expenses screen . ");
				customAssert.assertTrue(funcLegalExpenses(map_data,CommonFunction.product,CommonFunction.businessEvent), "Legal Expenses function is having issue(S) . ");
			}*/
			customAssert.assertTrue(common.funcMenuSelection("Navigate","Premium Summary"),"Issue while Navigating to Premium Summary screen . ");
			customAssert.assertTrue(common.funcPremiumSummary_Renewal(common.Renewal_excel_data_map,CommonFunction.product,CommonFunction.businessEvent,""), "Premium Summary function is having issue(S) . ");
			/////////////////////////////////////////
		
		return true;
		
	} catch (Exception e) {
		
		return false;
	}
}

}
