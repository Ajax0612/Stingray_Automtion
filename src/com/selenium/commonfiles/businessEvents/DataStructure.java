package com.selenium.commonfiles.businessEvents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.selenium.commonfiles.base.TestBase;
import com.selenium.commonfiles.util.*;

public class DataStructure extends TestBase implements DataInitialiazer {

	int countRunnableTestCases = 0;
	ArrayList<String> TestDataIDs = new ArrayList<>();
	// Per data sheet

	public String currentScenario = null;

	public DataStructure() {

	}

	/**
	 * 
	 * This method initializes basic below data structure variables from
	 * Suite.xlsx file . countRunnableTestCases - Total Number of Runnable Test
	 * Case(S) count. TestDataIDs - All Runnable Test Case IDs
	 * @throws Exception 
	 */
	@Override
	public void Initiliaze_DataStructure() throws DataExcelInitError {

		try {

			Initialize_XL("Suite" + ".xlsx");
			App_logs.debug("Suite Workbook initialized Sucessfully.");
			// Retrieve this number from Suite.xlsx book .
			countRunnableTestCases = TestUtil.getRunnableTestDataSets_column(Master_TC_Xls, "Scenarios");
			TestDataIDs = TestUtil.getRunnableTestDataIDs_col(Master_TC_Xls, "Scenarios");

			System.out.println("--||-----------------------------||--");
			System.out.println("--||---Runnable Test Run Stats---||--");
			System.out.println("--||-----------------------------||--");
			System.out.println("--||---Total Runnable Test Cases count = " + countRunnableTestCases);
			System.out.println("--||----Runnable Test Case IDs = " + TestDataIDs);
			System.out.println("--||-----------------------------||--");
			
			App_logs.debug("** Initialize Common Data Structure **");
			//System.out.println("** Initialize Common Data Structure **");
			} catch (Throwable t) {
			//System.out.println("Master.xlsx initialization Error - " + t.getMessage());
			throw new DataExcelInitError(t.toString());
		}
		// SuiteXls = new
		// XLS_Reader(workDir+"\\src\\com\\selenium\\database\\xls","Suite.xlsx");

	}

	/**
	 * 
	 * This method decides data structure to be initialized for e.g. Case 1: For
	 * NB flow it will init. only NB Structure Case 2: For MTA flow it will
	 * init. NB and MTA Structure, same like for rest of events.
	 * 
	 */
	public void decideDataStructureToInit(String Scenario) {

		currentScenario = Scenario;
		switch (Scenario) {

		case "NB":
			common.NB_excel_data_map = new HashMap<>();
			common.NB_Structure_of_InnerPagesMaps = new HashMap<>();
			break;
		case "MTA":
			common.NB_excel_data_map = new HashMap<>();
			common.NB_Structure_of_InnerPagesMaps = new HashMap<>();
			common.MTA_excel_data_map = new HashMap<>();
			common.MTA_Structure_of_InnerPagesMaps = new HashMap<>();
			// MTA1_excel_data_map = new HashMap<>();
			break;
		case "CAN":
			common.NB_excel_data_map = new HashMap<>();
			common.NB_Structure_of_InnerPagesMaps = new HashMap<>();
			common.CAN_excel_data_map = new HashMap<>();
			common.CAN_Structure_of_InnerPagesMaps = new HashMap<>();
			break;
		case "Rewind":
			common.NB_excel_data_map = new HashMap<>();
			common.NB_Structure_of_InnerPagesMaps = new HashMap<>();
			common.Rewind_excel_data_map = new HashMap<>();
			common.Rewind_Structure_of_InnerPagesMaps = new HashMap<>();
			break;
		case "Requote":
			common.NB_excel_data_map = new HashMap<>();
			common.NB_Structure_of_InnerPagesMaps = new HashMap<>();
			common.Requote_excel_data_map = new HashMap<>();
			common.Requote_Structure_of_InnerPagesMaps = new HashMap<>();
			break;
		case "Renewal":
			//common.NB_excel_data_map = new HashMap<>();
			//common.NB_Structure_of_InnerPagesMaps = new HashMap<>();
			common.Renewal_excel_data_map = new HashMap<>();
			common.Renewal_Structure_of_InnerPagesMaps = new HashMap<>();
			break;
		default:
			System.out.println("**** " + Scenario + " Scenario is not in Scope******");

		}

	}

	/**
	 * 
	 * This method actually populates data to data structure. For NB it will
	 * populate single NB structure with excel data. For MTA, Rewind,Renewal,..
	 * it will populate two structure[NB,MTA/Rewind/Renewal..] with respective
	 * excel data
	 * 
	 */
	@Override
	public void Populate_DataStructure(String current_TestDataID) throws DataStructurePopulateError {

		String parentNBTest = null;
		try{
		switch (currentScenario) {

		case "NB":
			common.NB_excel_data_map = TestUtil.getTestDataSetMap_Column(NB_Suite_TC_Xls, current_TestDataID);
			common.NB_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.NB_excel_data_map,
					NB_Suite_TC_Xls);

			break;
		case "MTA":
			common.MTA_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, current_TestDataID);
			common.MTA_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.MTA_excel_data_map,
					Events_Suite_TC_Xls);
			parentNBTest = getParentNBTest(common.MTA_excel_data_map);
			if (!parentNBTest.equals("NewBusinessID Not Found")) {
				common.NB_excel_data_map = TestUtil.getTestDataSetMap_Column(NB_Suite_TC_Xls, parentNBTest);
				common.NB_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.NB_excel_data_map,
						NB_Suite_TC_Xls);
			} else {
				System.out.println("NewBusinessID is not present in the excel sheet to run the MTA flow for Scenario: "
						+ current_TestDataID);
			}

			break;
		case "CAN":
			common.CAN_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, current_TestDataID);
			common.CAN_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.CAN_excel_data_map,
					Events_Suite_TC_Xls);
			parentNBTest = getParentNBTest(common.CAN_excel_data_map);
			if (!parentNBTest.equals("NewBusinessID Not Found")) {
				common.NB_excel_data_map = TestUtil.getTestDataSetMap_Column(NB_Suite_TC_Xls, parentNBTest);
				common.NB_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.NB_excel_data_map,
						NB_Suite_TC_Xls);
			} else {
				System.out.println("NewBusinessID is not present in the excel sheet to run the CAN flow for Scenario: "
						+ current_TestDataID);
			}

			break;
		case "Rewind":
			common.Rewind_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, current_TestDataID);
			common.Rewind_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Rewind_excel_data_map,
					Events_Suite_TC_Xls);
			parentNBTest = getParentNBTest(common.Rewind_excel_data_map);
			if (!parentNBTest.equals("NewBusinessID Not Found")) {
				common.NB_excel_data_map = TestUtil.getTestDataSetMap_Column(NB_Suite_TC_Xls, parentNBTest);
				common.NB_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.NB_excel_data_map,
						NB_Suite_TC_Xls);
			} else {
				System.out.println("NewBusinessID is not present in the excel sheet to run the Rewind flow for Scenario: "
								+ current_TestDataID);
			}

			break;
		case "Requote":
			common.Requote_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, current_TestDataID);
			common.Requote_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Requote_excel_data_map,
					Events_Suite_TC_Xls);
			parentNBTest = getParentNBTest(common.Requote_excel_data_map);
			if (!parentNBTest.equals("NewBusinessID Not Found")) {
				common.NB_excel_data_map = TestUtil.getTestDataSetMap_Column(NB_Suite_TC_Xls, parentNBTest);
				common.NB_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.NB_excel_data_map,
						NB_Suite_TC_Xls);
			} else {
				System.out.println("NewBusinessID is not present in the excel sheet to run the Rewind flow for Scenario: "
								+ current_TestDataID);
			}

			break;
		case "Renewal":
			common.Renewal_excel_data_map = TestUtil.getTestDataSetMap_Column(Events_Suite_TC_Xls, current_TestDataID);
			common.Renewal_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.Renewal_excel_data_map,
					Events_Suite_TC_Xls);
			/*parentNBTest = getParentNBTest(common.Renewal_excel_data_map);
			if (!parentNBTest.equals("NewBusinessID Not Found")) {
				common.NB_excel_data_map = TestUtil.getTestDataSetMap_Column(NB_Suite_TC_Xls, parentNBTest);
				common.NB_Structure_of_InnerPagesMaps = testUtil.populateInnerPagesDataStructures(common.NB_excel_data_map,
						NB_Suite_TC_Xls);
			} else {
				System.out.println("NewBusinessID is not present in the excel sheet to run the Renewal flow for Scenario: "
								+ current_TestDataID);
			}*/

			break;
		default:
			System.out.println("**** " + currentScenario + " Scenario is not in Scope******");

		}
		// populate inner Test Data Ids
				App_logs.debug("Excel data Sucessfully populated in to Map Structure .");
				// excel_data_map =
				// TestUtil.getTestDataSetMap_Column(Suite_TC_Xls,current_TestDataID);
				//App_logs.debug("** Clear Data Structure **");
				//System.out.println("** Populate Data Structure **");
		}catch(Throwable t){
			throw new DataStructurePopulateError("Error while populating data structure for "+current_TestDataID);
			
		}
		
	}

	/**
	 * 
	 * This method flushes populated data structure.
	 *
	 * 
	 */
	@Override
	public void Clear_DataStructure() {

		if (common.NB_excel_data_map != null)
			common.NB_excel_data_map.clear();
		if (common.MTA_excel_data_map != null)
			common.MTA_excel_data_map.clear();
		if (common.CAN_excel_data_map != null)
			common.CAN_excel_data_map.clear();
		if (common.Rewind_excel_data_map != null)
			common.Rewind_excel_data_map.clear();
		if (common.Renewal_excel_data_map != null)
			common.Renewal_excel_data_map.clear();
		if (common.NB_Structure_of_InnerPagesMaps != null)
			common.NB_Structure_of_InnerPagesMaps.clear();
		if (common.MTA_Structure_of_InnerPagesMaps != null)
			common.MTA_Structure_of_InnerPagesMaps.clear();
		if (common.CAN_Structure_of_InnerPagesMaps != null)
			common.CAN_Structure_of_InnerPagesMaps.clear();
		if (common.Rewind_Structure_of_InnerPagesMaps != null)
			common.Rewind_Structure_of_InnerPagesMaps.clear();
		if (common.Renewal_Structure_of_InnerPagesMaps != null)
			common.Renewal_Structure_of_InnerPagesMaps.clear();
		common.no_of_inner_data_sets.clear();
		common_SPI.SPI_Rater_output.clear();
		common_COB.Book_rate_Rater_output.clear();
		
	
		product = null;
		businessEvent = null;

		common = null;
		common_VELA = null;
		common_SPI = null;
		common_CTA = null;
		common_CCF = null;
		common_POB = null;
		common_POC = null;
		common_COB = null;
		
		finalinfo.clear();
		
		App_logs.debug("** Clear Data Structure **");
		//System.out.println("** Clear Data Structure **");
	}

	/**
	 * 
	 * This method returns parent NB Test ID for MTA/CAN/Renewal.. etc.
	 *
	 * 
	 */
	public String getParentNBTest(Map<Object, Object> businessEvent_data) {

		if (businessEvent_data.get("NewBusinessID") != null)
			return (String) businessEvent_data.get("NewBusinessID");
		else {
			return "NewBusinessID Not Found";
		}

	}

}
