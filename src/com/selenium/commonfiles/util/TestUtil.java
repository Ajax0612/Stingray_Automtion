package com.selenium.commonfiles.util;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.RollingFileAppender;
//import org.junit.Assert;
import com.relevantcodes.extentreports.LogStatus;
import com.selenium.commonfiles.base.TestBase;

public class TestUtil extends TestBase {

	Map<String, List<Map<String, String>>> Structure_of_InnerPagesMaps = new HashMap<>();
	
	public static enum Test_Cases_Staus{
		PASS,FAIL,NoRUN;
	}
	// For getting corresponding data to Testcase from the Test Module sheet
	// -Row Wise
	public static HashMap<String, String> getTestDataSetMap(XLS_Reader xls, String testDataID) {
		HashMap<String, String> test_data_map = null;
		int start_col = 0;
		int total_cols = 0;
		String dataSheet = null;

		final int no_of_Sheets = xls.CountSheets();
		for (int sheet = 0; sheet < no_of_Sheets; sheet++) {
			dataSheet = xls.GetSheetAtIndex(sheet);
			// dataSheet=s1.toString();

			if (!xls.IsSheetExists(dataSheet)) {
				xls = null;
				return null;
			}

			final int row_number = xls.getRowIndex(dataSheet, testDataID);
			int cols = 0;
			if (row_number != -1)
				cols = xls.GetColumnCount(dataSheet);

			total_cols = total_cols + cols;
		}
		// System.out.println("rows->"+rows+" Columns ->"+cols);
		Object[][] data = new Object[2][total_cols];
		for (int sheet = 0; sheet < no_of_Sheets; sheet++) {
			dataSheet = xls.GetSheetAtIndex(sheet);
			// dataSheet=s1.toString();

			if (!xls.IsSheetExists(dataSheet)) {
				xls = null;
				return null;
			}

			final int row_number = xls.getRowIndex(dataSheet, testDataID);

			int cols = xls.GetColumnCount(dataSheet);

			int colnum;
			int c = 0;
			for (colnum = start_col; colnum < (cols + start_col); colnum++) {

				data[0][colnum] = xls.getCellData(dataSheet, 0, c);
				data[1][colnum] = xls.getCellData(dataSheet, row_number, c);
				// System.out.print(xls.getCellData(testCaseName, 0,
				// colnum)+"---");
				c++;
			}
			start_col = colnum;
		} // sheet for loop

		test_data_map = new HashMap<>();

		for (int ic = 0; ic < total_cols; ic++) {
			test_data_map.put(data[0][ic].toString().trim(), data[1][ic].toString().trim());
		}

		// System.out.println("Hello");
		return test_data_map;
	}

	/**
	 * 
	 * This method return Runnable Test Case Count.
	 *
	 */
	public static int getRunnableTestDataSets(XLS_Reader xls, String sheetName) {
		int count = 0;
		for (int i = 1; i < xls.GetColumnCount(sheetName); i++) {
			String Status = xls.getCellData_ColumnWise(sheetName, i, "RunStatus");
			if (Status.equalsIgnoreCase("Y") || Status.equalsIgnoreCase("Yes")) {
				count++;
			} else {
				continue;
			}
			// }
		}
		xls = null; // release memory
		return count;
	}

	/**
	 * 
	 * This method return Runnable Test Case Count.
	 *
	 */
	public static int getRunnableTestDataSets_column(XLS_Reader xls, String sheetName) {
		int count = 0;
		for (int i = 1; i < xls.GetRowCount(sheetName); i++) {
			String Status = xls.getCellData_ColumnWise(sheetName, "RunStatus", i);
			if (Status.equalsIgnoreCase("Y") || Status.equalsIgnoreCase("Yes")) {
				count++;
			} else {
				continue;
			}
			// }
		}
		xls = null; // release memory
		return count;
	}

	/**
	 * 
	 * This method return Runnable Test Case IDs.
	 *
	 */
	public static ArrayList<String> getRunnableTestDataIDs(XLS_Reader xls, String sheetName) {
		ArrayList<String> testDataIDs = new ArrayList<>();
		for (int i = 1; i < xls.GetColumnCount(sheetName); i++) {
			String Status = xls.getCellData_ColumnWise(sheetName, i, "RunStatus");
			if (Status.equalsIgnoreCase("Y") || Status.equalsIgnoreCase("Yes")) {
				testDataIDs.add(xls.getCellData_ColumnWise(sheetName, i, "TestCaseID"));
			} else {
				continue;
			}
		}
		xls = null; // release memory
		return testDataIDs;
	}

	/**
	 * 
	 * This method return Runnable Test Case IDs.
	 *
	 */
	public static ArrayList<String> getRunnableTestDataIDs_col(XLS_Reader xls, String sheetName) {
		ArrayList<String> testDataIDs = new ArrayList<>();
		for (int i = 1; i < xls.GetRowCount(sheetName); i++) {
			String Status = xls.getCellData_ColumnWise(sheetName, "RunStatus", i);
			if (Status.equalsIgnoreCase("Y") || Status.equalsIgnoreCase("Yes")) {
				testDataIDs.add(xls.getCellData_ColumnWise(sheetName, "TestCaseID", i));
			} else {
				continue;
			}
		}
		xls = null; // release memory
		return testDataIDs;
	}

	/**
	 * 
	 * This method populated Map Structure (Column Wise) with excel data based
	 * on test data ID provided.
	 *
	 */
	public static HashMap<Object, Object> getTestDataSetMap_Column(XLS_Reader xls, String testDataID) {
		HashMap<Object, Object> test_data_map = null;
		int start_row = 0;
		int total_rows = 0;
		String dataSheet = null;
		int keyColumnIndex=Integer.parseInt(CONFIG.getProperty("DataSheetKeyColumnIndex"));
		
		try {

			final int no_of_Sheets = xls.CountSheets();
			for (int sheet = 0; sheet < no_of_Sheets; sheet++) {
				dataSheet = xls.GetSheetAtIndex(sheet);

				if (!xls.IsSheetExists(dataSheet)) {
					xls = null;
					return null;
				}

				final int column_number = xls.getColumnIndex(dataSheet, testDataID);

				int rows = 0;
				if (column_number != -1)
					rows = xls.GetRowCount(dataSheet);

				total_rows = total_rows + rows;
				// System.out.println(sheet);
			}
			Object[][] data = new Object[2][total_rows];
			for (int sheet = 0; sheet < no_of_Sheets; sheet++) {

				dataSheet = xls.GetSheetAtIndex(sheet);
				// System.out.println(dataSheet);

				if (!xls.IsSheetExists(dataSheet)) {
					xls = null;
					return null;
				}

				final int column_number = xls.getColumnIndex(dataSheet, testDataID);

				int rows = 0;
				if (column_number != -1) {
					rows = xls.GetRowCount(dataSheet);

					int rownum;
					int c = 0;
					for (rownum = start_row; rownum < (rows + start_row); rownum++) {

						data[0][rownum] = xls.getCellData(dataSheet, c, keyColumnIndex);
						data[1][rownum] = xls.getCellData(dataSheet, c, column_number);
						// if(c == 53)
						// System.out.println("Row "+c);
						// System.out.println("Col "+column_number);
						c++;
					}
					// System.out.println("Col "+rownum);
					start_row = rownum;
				} else {
					continue;
				}
			} // sheet for loop

			test_data_map = new HashMap<>();

			for (int ic = 0; ic < total_rows; ic++) {
				test_data_map.put(data[0][ic].toString().trim(), data[1][ic].toString().trim());
			}
		} catch (Throwable npe) {
			App_logs.error("Data issue in > "+dataSheet+" < Sheet for Test Case > "+testDataID+" < . ");
			System.out.println("Data issue in > "+dataSheet+" < Sheet for Test Case > "+testDataID+" < . Unable to Populate data in to Structure .  ");
			throw new NullPointerException();
		}

		// System.out.println("Hello");
		return test_data_map;
	}

	/**
	 * 
	 * This method populated Map Structure (Column Wise) with excel data based
	 * on test data ID provided.
	 *
	 */
	public static HashMap<String, String> getTestDataSetMap_Column_String_Map(XLS_Reader xls, String testDataID) {
		HashMap<String, String> test_data_map = null;
		int start_row = 0;
		int total_rows = 0;
		String dataSheet = null;
		int keyColumnIndex=Integer.parseInt(CONFIG.getProperty("DataSheetKeyColumnIndex"));
		
		try {

			final int no_of_Sheets = xls.CountSheets();
			for (int sheet = 0; sheet < no_of_Sheets; sheet++) {
				dataSheet = xls.GetSheetAtIndex(sheet);

				if (!xls.IsSheetExists(dataSheet)) {
					xls = null;
					return null;
				}

				final int column_number = xls.getColumnIndex(dataSheet, testDataID);

				int rows = 0;
				if (column_number != -1)
					rows = xls.GetRowCount(dataSheet);

				total_rows = total_rows + rows;
				// System.out.println(sheet);
			}
			Object[][] data = new Object[2][total_rows];
			for (int sheet = 0; sheet < no_of_Sheets; sheet++) {

				dataSheet = xls.GetSheetAtIndex(sheet);
				// System.out.println(dataSheet);

				if (!xls.IsSheetExists(dataSheet)) {
					xls = null;
					return null;
				}

				final int column_number = xls.getColumnIndex(dataSheet, testDataID);

				int rows = 0;
				if (column_number != -1) {
					rows = xls.GetRowCount(dataSheet);

					int rownum;
					int c = 0;
					for (rownum = start_row; rownum < (rows + start_row); rownum++) {

						data[0][rownum] = xls.getCellData(dataSheet, c, keyColumnIndex);
						data[1][rownum] = xls.getCellData(dataSheet, c, column_number);
						// System.out.println("Col "+column_number);
						c++;
					}
					// System.out.println("Col "+rownum);
					start_row = rownum;
				} else {
					continue;
				}
			} // sheet for loop

			test_data_map = new HashMap<>();

			for (int ic = 0; ic < total_rows; ic++) {
				test_data_map.put(data[0][ic].toString().trim(), data[1][ic].toString().trim());
			}
		} catch (NullPointerException npe) {
			App_logs.error("Data issue in > "+dataSheet+" < Sheet for Test Case > "+testDataID+" < . ");
			System.out.println("Data issue in > "+dataSheet+" < Sheet for Test Case > "+testDataID+" < . Unable to Populate data in to Structure .  ");
		}

		// System.out.println("Hello");
		return test_data_map;
	}

	// public void getInnerTestDataIDs(Map<String,String>)
	/**
	 * 
	 * This method creates Map of all inner Pages individual maps.
	 *
	 */
	public Map<String, List<Map<String, String>>> populateInnerPagesDataStructures(Map<Object, Object> map,XLS_Reader current_Suite_TC_Xls) {

		Map<String, List<Map<String, String>>> temp_Structure_of_InnerPagesMaps = new HashMap<>();
		List<String> innerDataIds = null;
		String sheet_name = null;
		int count;
		// Map<List<String>,List<List<String>>> map_of_list= new HashMap<>();
		innerDataIds = new ArrayList<>();
		for (Object key : map.keySet()) {
			if (((String) map.get(key)).contains(";")) {
				count=0;
				String dataIds[] = ((String) map.get(key)).split(";");
				for (String dataId : dataIds) {
					innerDataIds.add(dataId);
					count++;
				}
				String[] sheet_tokens = dataIds[0].split("_");
				sheet_name = sheet_tokens[0];
				common.no_of_inner_data_sets.put(sheet_name, count);
				
				//populateListOfMaps(sheet_name, innerDataIds, current_Suite_TC_Xls);
				//Populate list of inner maps data
				List<Map<String, String>> list_of_Maps = new ArrayList<>();
				Map<String, String> temp_Map = new HashMap<String, String>();
				for (String dataID : innerDataIds) {
					temp_Map = TestUtil.getTestDataSetMap_Column_String_Map(current_Suite_TC_Xls, dataID);
					list_of_Maps.add(temp_Map);
				}
				temp_Structure_of_InnerPagesMaps.put(sheet_name, list_of_Maps);
		
			} else {
				continue;
			}
			innerDataIds.clear();
		}
		//System.out.println(common.no_of_inner_data_sets);
		return temp_Structure_of_InnerPagesMaps;
	}

	/*public void populateListOfMaps(String sheet_name, List<String> innerDataIds, XLS_Reader current_Suite_TC_Xls,Map<String, List<Map<String, String>>> temp_Structure_of_InnerPagesMaps) {
		List<Map<String, String>> list_of_Maps = new ArrayList<>();
		// List<Map<String, String>> list_of_Maps1 = new ArrayList<>();
		Map<String, String> temp_Map = new HashMap<String, String>();
		for (String dataID : innerDataIds) {
			temp_Map = TestUtil.getTestDataSetMap_Column_String_Map(current_Suite_TC_Xls, dataID);
			list_of_Maps.add(temp_Map);
		}
		// list_of_Maps1 = list_of_Maps;
		temp_Structure_of_InnerPagesMaps.put(sheet_name, list_of_Maps);
		// list_of_Maps.clear();

	}*/
	/**
	 * 
	 * This method checks if Open/Close status of Excel data file. 
	 * 
	 *
	 */
	public static boolean isFileOpened(String excelFile)
	{
		
		boolean isFileOpened = false;
		String datafile = System.getProperty("user.dir")+"\\src\\com\\selenium\\database\\xls\\"+excelFile+".xlsx";
		File file_ = new File(datafile);
		File sameFile_ = new File(datafile);
		if(file_.renameTo(sameFile_)){
	        //System.out.println(excelFile+".xlsx file is closed");
	        isFileOpened = false;
	    }else{
	        System.out.println(excelFile+".xlsx file is opened, first Close it and try again...");
	        isFileOpened = true;
	    }
		
		return isFileOpened;
		
	}
	
	/**
	 * Method writes data to excel data Sheet and Update Map Structure .
	 * 
	 */
	public static boolean WriteDataToXl(String XL_Name,String Sheet_Name,String TestCase_ColName, String FieldName, String DataToWrite,Map<Object, Object> map_to_update){
		boolean xValue = true;
		XLS_Reader xl = null;
		try{
			xl = new XLS_Reader(System.getProperty("user.dir")+"\\src\\com\\selenium\\database\\xls",XL_Name+".xlsx");
			int RowNum = xl.getRowIndex(Sheet_Name,FieldName);
			if(!isFileOpened(XL_Name)){
				xl.setCellData(Sheet_Name, TestCase_ColName, RowNum, DataToWrite);	
				//System.out.println("Data has been written successfully");
				map_to_update.put(FieldName, DataToWrite);	
			}else{
				 System.out.println("**Unable to write to "+XL_Name+".xlsx as file is opened in Editor .");
				 App_logs.debug("Unable to write to "+XL_Name+".xlsx as file is opened in Editor .");
				 xValue = false;
			}
			
		}catch(Throwable t){
			System.out.println("Unable to write data to excel sheet -- "+XL_Name+".xlsx --" + t.getLocalizedMessage());
			return false;
		}
		
		xl.closeInputStream();
		return xValue;
	}
	
	/**
	 * Method writes data to excel data Sheet for Inner pages and Update Map Structure .
	 * 
	 */
	public static boolean WriteDataToXl_innerSheet(String XL_Name,String Sheet_Name,String TestCase_ColName, String FieldName, String DataToWrite,Map<String, String> map_to_update){
		boolean xValue = true;
		XLS_Reader xl = null;
		try{
			xl = new XLS_Reader(System.getProperty("user.dir")+"\\src\\com\\selenium\\database\\xls",XL_Name+".xlsx");
			int RowNum = xl.getRowIndex(Sheet_Name,FieldName);
			if(!isFileOpened(XL_Name)){
				xl.setCellData(Sheet_Name, TestCase_ColName, RowNum, DataToWrite);	
				//System.out.println("Data has been written successfully");
				map_to_update.put(FieldName, DataToWrite);	
			}else{
				 System.out.println("**Unable to write to "+XL_Name+".xlsx as file is opened in Editor .");
				 App_logs.debug("Unable to write to "+XL_Name+".xlsx as file is opened in Editor .");
				 xValue = false;
			}
			
		}catch(Throwable t){
			System.out.println("Unable to write data to excel sheet -- "+XL_Name+".xlsx --" + t.getLocalizedMessage());
			return false;
		}
		
		xl.closeInputStream();
		return xValue;
	}

	
	// Read Dynamically Data from Excel Sheet.
	
	public static String ReadDataFromXl(String XL_Name,String Sheet_Name,String TestCase_ColName, String FieldName){
		String ss = null;
		XLS_Reader xl=null;
		try{
			xl = new XLS_Reader(System.getProperty("user.dir")+"\\src\\com\\selenium\\database\\xls",XL_Name+".xlsx");
			int RowNum = xl.getRowIndex(Sheet_Name,FieldName);
			//xl.setCellData(Sheet_Name, TestCase_ColName, RowNum, DataToWrite);
			ss= xl.getCellData(Sheet_Name, RowNum, TestCase_ColName);
			//System.out.println("Data has been retrieved successfully");
			xl.closeInputStream();
		}catch(Throwable t){
			System.out.println("Unable to read data from excel sheet -- "+XL_Name+".xlsx --" + t.getLocalizedMessage());
			xl.closeInputStream();
			return null;
		}
		
		return ss;
	}
	
	
	public static void processErrorInfo(){
		
		TestUtil.reportProcessErrorInfoStatus(processInfo(), "Fail", true);
		
	}
	
	public static void reportProcessErrorInfoStatus(String Message, String Status, boolean scrnFlag) {
		
		String image = "";
		if (Status.equalsIgnoreCase("Info")) {
			TestBase.logger.log(LogStatus.INFO, Message, image);
		} else if (Status.equalsIgnoreCase("Fail")) {
			TestBase.logger.log(LogStatus.FAIL, "<p style='color:red'>" + Message, image);
		} else if (Status.equalsIgnoreCase("Pass")) {
			TestBase.logger.log(LogStatus.PASS, Message, image);
		} else if (Status.equalsIgnoreCase("Skip")) {
			TestBase.logger.log(LogStatus.SKIP, Message, image);
		} else {
			TestBase.logger.log(LogStatus.ERROR, "Wrong Argument passed in function");
			org.testng.Assert.fail("Wrong Argument passed in function");
		}

	}
	
	

	/////////////// *******//*****************************////

	/**
	 * Method returns the given date offset by fromCurrent
	 * 
	 * @param fromCurrent
	 * @return
	 */
	public static String returnDate(int fromCurrent) {
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		cal.add(Calendar.DATE, fromCurrent);
		return dateFormat.format(cal.getTime());
	}

	public static void captureScreenshot(String Status, String Message) {
		String file = System.getProperty("user.dir") + "\\src\\com\\selenium\\configuration\\screenshots\\Auto_"
				+ (new Random().nextInt()) + ".png";
		String sErr = "Error while taking screenshot";
		try {
			screenCaptureUtil.takeScreenshot(TestBase.driver, file);
		} catch (Exception e) {

			TestBase.logger.log(LogStatus.ERROR, sErr, e.getLocalizedMessage());
			ErrorUtil.addVerificationFailure(e);
		}

		String image = TestBase.logger.addScreenCapture(file);
		if (Status.equalsIgnoreCase("Fail")) {
			TestBase.logger.log(LogStatus.FAIL, Message, image);
		} else if (Status.equalsIgnoreCase("Pass")) {
			TestBase.logger.log(LogStatus.INFO, Message, image);
		}

	}

	public static void reportStatus(String Message, String Status, boolean scrnFlag) {
		// System.out.println(Message);
		App_logs.debug(Message);
		String image = "";
		String scrnConfigFlag = TestBase.CONFIG.getProperty("ScreenshotsFlag");
		if ((scrnFlag) && ((scrnConfigFlag.equalsIgnoreCase("Y")) || (Status.equalsIgnoreCase("Fail")))) {
			String file = screnshtpth + (new Random().nextInt()) + ".png";
			String sErr = "Error while taking screenshot";
			try {
				screenCaptureUtil.takeScreenshot(TestBase.driver, file);
			} catch (Exception e) {
				TestBase.logger.log(LogStatus.ERROR, sErr, e.getLocalizedMessage());
				ErrorUtil.addVerificationFailure(e);
				org.testng.Assert.fail(sErr);
				
			}
			image = PathChange(TestBase.logger.addScreenCapture(file));
		}
		if (Status.equalsIgnoreCase("Info")) {
			TestBase.logger.log(LogStatus.INFO, Message, image);
		} else if (Status.equalsIgnoreCase("Fail")) {
			TestBase.logger.log(LogStatus.FAIL, "<p style='color:red'>" + Message, image);
		} else if (Status.equalsIgnoreCase("Pass")) {
			TestBase.logger.log(LogStatus.PASS, Message, image);
		} else if (Status.equalsIgnoreCase("Skip")) {
			TestBase.logger.log(LogStatus.SKIP, Message, image);
		} else {
			TestBase.logger.log(LogStatus.ERROR, "Wrong Argument passed in function");
			org.testng.Assert.fail("Wrong Argument passed in function");
		}

	}

	public static void addInfo(String s) {
		finalinfo.add(s);
	}

	public static String processInfo() {
		// System.out.println(finalinfo);
		String ss = finalinfo.toString();
		ss = ss.replaceFirst("\\[", "");
		ss = ss.replaceFirst("\\]", "");
		ss = ss.replaceAll(",", "");
		return ss;
	}

	public static void reportFunctionFailed(String s) {
		finalinfo.add("<b><p style='color:red'>" + s.concat("</p>"));

	}

	public static void reportTestCasePassed(String s) throws Throwable {
				
		if(ErrorUtil.verificationFailureMapTmp.size()==0){
			
			TestUtil.reportStatus("<b><p style='color:green'>" + s + ":: Test Case has been passed ::</p></b>", "Pass",	false);}
		else{
			throw new Exception("Test Case Failed");
		}
	}

	public static void reportTestCaseFailed(String tc_name, Throwable t) {

		ErrorUtil.addVerificationFailure(t);
		TestUtil.addInfo("<br><p style='color:red'> Test - <b>"+ tc_name + "</b>  has been failed due to error(s)</p>");
		k.reportErr(tc_name, t);
		String finalinfo_with_Applicaion_log_link = TestUtil.processInfo();
		TestUtil.reportProcessErrorInfoStatus(finalinfo_with_Applicaion_log_link, "Fail", true);
		org.testng.Assert.fail(tc_name + " has been failed due to error(s)" + t);
	}

	// This is currently only for OFC product.
	public static void funcReportTestCaseFailed(String tc_name, Throwable t, boolean flag) {

		ErrorUtil.addVerificationFailure(t);
		TestUtil.addInfo("<br><p style='color:red'>" + tc_name + " has been failed due to error(s)</p>");
		String s1 = TestUtil.processInfo();
		TestUtil.reportStatus(s1, "Fail", flag);
		org.testng.Assert.fail(tc_name + " has been failed due to error(s)" + t);
	}

	public static String PathChange(String iName) {
		String s2 = "file:///";
		String s3 = System.getProperty("user.dir");
		s3 = s3.replace("\\", "\\\\");
		String s4 = "\\\\src\\\\com\\\\selenium\\\\Execution_Report\\\\Report";
		String s5 = s2 + s3 + s4;
		String fs = null;
		fs = iName.replaceAll(s5, ".");
		return fs;
	}

	public static String AttributeORvalue(String xpathkey) {
		try {
			Properties Obj = new Properties();
			FileInputStream ip = new FileInputStream(
					workDir + "\\src\\com\\selenium\\configuration\\propertyFiles\\OR.properties");
			Obj.load(ip);
			ip.close();
			String locator = Obj.getProperty(xpathkey);
			String attVal = locator.split(":")[1];
			return attVal;
		} catch (Throwable t) {
			k.reportErr("Unable to return the OR attribute values" + xpathkey, t);
			return null;
		}

	}

	public static String AsserErrorMsg(String msg) {
		TestUtil.addInfo("<br><p style='color:red'>" + msg + "</p>");
		String s1 = TestUtil.processInfo();
		TestUtil.reportStatus(s1, "Fail", true);
		return msg;

	}
	
	public static void setLog4jLogFile() {
		// TODO Auto-generated method stub
		//PropertyConfigurator.configure(TestUtil.getLog4jProperties());
		/*Properties properties=new Properties();
		
			PatternLayout layout = new PatternLayout();
	        String conversionPattern = "%d{yyyy/MM/dd HH:mm:ss.SSS} [%5p] %t (%F) - %m%n";
	        layout.setConversionPattern(conversionPattern);
	 
	        // creates daily rolling file appender
	        RollingFileAppender rollingAppender = new RollingFileAppender();
	        rollingAppender.setFile(workDir + "\\src\\com\\selenium\\configuration\\logs\\Application.log");
	        //rollingAppender.setDatePattern("'.'yyyy-MM-dd HH:mm:ss");
	        
	        properties.setProperty("log4j.appender.MyFile.MaxFileSize", "100KB");
	        properties.setProperty("log4j.appender.MyFile.MaxBackupIndex", "1");
	        PropertyConfigurator.configure(properties);
	        rollingAppender.setLayout(layout);
	        rollingAppender.activateOptions();
	 
	        // configures the root logger
	        Logger rootLogger = Logger.getRootLogger();
	        rootLogger.setLevel(Level.DEBUG);
	        rootLogger.addAppender(rollingAppender);
	 
	        // creates a custom logger and log messages*/
	        //App_logs = Logger.getLogger("Application_log");
	 
		// App_logs.removeAllAppenders();
	}
	
	public static Properties getLog4jProperties(){
		Properties properties=new Properties();
		   properties.setProperty("log4j.rootLogger","TRACE,Application_log");
		   properties.setProperty("log4j.rootCategory","TRACE");

		   properties.setProperty("log4j.appender.Application_log", "org.apache.log4j.RollingFileAppender");
		   properties.setProperty("log4j.appender.Application_log.File", workDir + "\\src\\com\\selenium\\configuration\\logs\\Application.log");
		   properties.setProperty("log4j.appender.Application_log.Append","false");
		   properties.setProperty("log4j.appender.Application_log.MaxFileSize", "100KB");
		   properties.setProperty("log4j.appender.Application_log.MaxBackupIndex", "1");
		   properties.setProperty("log4j.appender.Application_log.layout",  "org.apache.log4j.PatternLayout");
		   properties.setProperty("log4j.appender.Application_log.layout.ConversionPattern","%d{yyyy/MM/dd HH:mm:ss.SSS} [%5p] %t (%F) - %m%n");
		return properties;
		}

public  static String getStringfromMap(String s,String event){
	switch (event){
	case "NB":
		return (String) common.NB_excel_data_map.get(s);

	case "CAN":
		return (String) common.CAN_excel_data_map.get(s);

	case "Renewal":
		return (String) common.Renewal_excel_data_map.get(s);
		
	case "MTA":
		return (String) common.MTA_excel_data_map.get(s);
	}
	return (String) common.NB_excel_data_map.get(s);
	
	
}
}
