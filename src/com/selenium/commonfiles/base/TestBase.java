package com.selenium.commonfiles.base;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import org.apache.log4j.PropertyConfigurator;



import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.NetworkMode;

import com.selenium.commonfiles.util.*;
import com.selenium.configuration.listener.ListenerClass;

//import com.selenium.commonfiles.util.ObjectMap;
//import com.selenium.commonfiles.util.XLS_Reader;

//import com.selenium.commonfiles.util.CustomAssetions;

public class TestBase {
	public static ArchiveReport ar = new ArchiveReport();
	protected final static String workDir = System.getProperty("user.dir");
	public static ExtentReports report = null;
	public static final Properties CONFIG = new Properties();
	protected static XLS_Reader Suite_TC_Xls = null;
	protected static XLS_Reader NB_Suite_TC_Xls = null;
	protected static XLS_Reader MTA_Suite_TC_Xls = null;
	protected static XLS_Reader Events_Suite_TC_Xls = null;
	protected static XLS_Reader Master_TC_Xls = null;

	protected static ObjectMap OR = null;
	protected static XLS_Reader SuiteXls = null;

	protected static List<String> finalinfo = new ArrayList<String>();
	public final static String screnshtpth = workDir + "\\src\\com\\selenium\\Execution_Report\\Report\\images\\Auto_";
	public static WebDriver driver = null;
	public static Keywords k = null;
	public static CommonFunction common = null;
	public static CommonFunction_VELA common_VELA = null;
	public static CommonFunction_SPI common_SPI = null;
	public static CommonFunction_PIA common_PIA = null;
	public static CommonFunction_CTA common_CTA = null;
	public static CommonFunction_CTB common_CTB = null;
	public static CommonFunction_CCF common_CCF = null;
	public static CommonFunction_CCI common_CCI = null;
	public static CommonFunction_CCG common_CCG = null;
	public static CommonFunction_POB common_POB = null;
	public static CommonFunction_POC common_POC = null;
	public static CommonFunction_XOE common_XOE = null;
	public static CommonFunction_COB common_COB = null;
	public static CommonFunction_COA common_COA = null;
	public static CommonFunction_XOC common_XOC = null;
	public static CommonFunction_XOQ common_XOQ = null;
	public static CommonFunction_LEA common_LEA = null;
	public static CommonFunction_ENA common_ENA = null;
	public static CommonFunction_POF common_POF = null;
	public static CommonFunction_CCD common_CCD = null;
	public static CommonFunction_CCJ common_CCJ = null;
	public static CommonFunction_XOG common_XOG = null;
	public static CommonFunction_HHAZ common_HHAZ = null;
	public static CommonFunction_PEN common_PEN = null;
	//public static CommonFunction_POA common_POA = null;
	public static CommonFunction_Zennor common_Zennor = null;
	public static CommonFunction_POE common_POE = null;
	public static CommonFunction_OED common_OED = null;
	public static CommonFunction_CCC common_CCC = null;
	public static CommonFunction_MFB common_MFB = null;
	public static CommonFunction_MFC common_MFC = null;
	public static CommonFunction_GTC common_GTC = null;
	public static CommonFunction_GTD common_GTD = null;
	public static CommonFunction_GTA common_GTA = null;
	public static CommonFunction_GTB common_GTB = null;
	public static CommonFunction_ExistingPolicy common_EP = null;
	public static CommonFunction_CMA common_CMA = null;
	public static CommonFunction_PAA common_PAA=null;
	public static CommonFunction_PAB common_PAB=null;
	public static CommonFunction_DOB common_DOB = null;
	//public CommonFunction_COB common_COB = null;
	public static TestUtil testUtil = null;
	public static boolean isInitialized = false;
	public static boolean isXLInitialized = false;
	public static boolean isMasterXLInitialized = false;
	public static boolean isNBSuiteXLInitialized = false;
	public static boolean isMTASuiteXLInitialized = false;
	public static boolean isEventsSuiteXLInitialized = false;
	public static String product = null;
	public static String businessEvent = null;
	public static String testResult = "PASS";
	public static String suiteResult = "PASS";
	protected static List<String> exceptionList = new ArrayList<String>();
	public static String Test_Case = null;
	protected static Logger App_logs = null;
	public static ExtentTest logger;
	public static Map<String, String> sysInfo = null;
	public static CustomAssetions customAssert;
	public static ListenerClass listner = null;

	public Map<Object, Object> NB_excel_data_map = null;
	public Map<Object, Object> CAN_excel_data_map = null;
	public Map<Object, Object> MTA_excel_data_map = null;
	public Map<Object, Object> Rewind_excel_data_map = null;
	public Map<Object, Object> Renewal_excel_data_map = null;
	public Map<String, List<Map<String, String>>> NB_Structure_of_InnerPagesMaps = null;
	public Map<String, List<Map<String, String>>> MTA_Structure_of_InnerPagesMaps = null;
	public Map<String, List<Map<String, String>>> Rewind_Structure_of_InnerPagesMaps = null;
	public Map<String, List<Map<String, String>>> Renewal_Structure_of_InnerPagesMaps = null;

	// public Hashtable<String,String> rawVal = null;

	// public static void main(String[] args) {
	public static void Initialize() throws Exception {

		// org.apache.log4j.BasicConfigurator.configure();
				
		//TestUtil.setLog4jLogFile();
		PropertyConfigurator.configure(TestUtil.getLog4jProperties());
		App_logs = Logger.getLogger("Application_log");
		// App_logs.removeAllAppenders();
		OR = new ObjectMap(workDir + "\\src\\com\\selenium\\configuration\\propertyFiles\\");
		
		// CONFIG.setProperty("log4j.appender.devpinoyLogger.File",
		// workDir+"\\src\\com\\selenium\\configuration\\logs\\Application.log");
		App_logs.debug("Archiving the reports successfully");
		App_logs.debug("loading Properties files");
		FileInputStream ip = new FileInputStream(
				workDir + "\\src\\com\\selenium\\configuration\\propertyFiles\\config.properties");
		CONFIG.load(ip);
		ip.close();
		if (CONFIG.get("OfflineReport").equals("Y")) {
			report = new ExtentReports(
					workDir + "\\src\\com\\selenium\\Execution_Report\\Report\\Execution_report.html",
					NetworkMode.OFFLINE);
		} else {
			report = new ExtentReports(
					workDir + "\\src\\com\\selenium\\Execution_Report\\Report\\Execution_report.html");
		}

		report.loadConfig(new File(workDir + "\\src\\com\\selenium\\configuration\\propertyFiles\\extent-config.xml"));
		App_logs.debug("loading Properties files successfully");
		App_logs.debug("loading extent config file ....");
		isInitialized = true;
		k = new Keywords();

		sysInfo = new HashMap<>();
		testUtil = new TestUtil();
		customAssert = new CustomAssetions();
		listner = new ListenerClass(); 
		App_logs.debug("loading reporting drivers ....");
		
	}

	

	public static void Initialize_Common_Functions() throws CommonFunctionsInitializationError {

		try {
			common = new CommonFunction();
			common_VELA = new CommonFunction_VELA();
			common_SPI = new CommonFunction_SPI();
			common_PIA = new CommonFunction_PIA();
			common_CTA = new CommonFunction_CTA();
			common_CTB = new CommonFunction_CTB();
			common_CCF = new CommonFunction_CCF();
			common_CCI = new CommonFunction_CCI();
			common_POB = new CommonFunction_POB();
			common_POC = new CommonFunction_POC();
			common_CCG = new CommonFunction_CCG();
			common_XOE = new CommonFunction_XOE();
			common_COB = new CommonFunction_COB();
			common_COA = new CommonFunction_COA();
			common_XOC = new CommonFunction_XOC();
			common_XOQ = new CommonFunction_XOQ();
			common_LEA = new CommonFunction_LEA();
			common_ENA = new CommonFunction_ENA();
			common_Zennor = new CommonFunction_Zennor();
			common_POF = new CommonFunction_POF();
			common_CCD = new CommonFunction_CCD();
			common_CCJ = new CommonFunction_CCJ();
			common_HHAZ = new CommonFunction_HHAZ();
			common_PEN = new CommonFunction_PEN();
			common_POE = new CommonFunction_POE();
			common_XOG = new CommonFunction_XOG();
			common_OED = new CommonFunction_OED();
			common_CCC = new CommonFunction_CCC();
			common_MFB = new CommonFunction_MFB();
			common_MFC = new CommonFunction_MFC();
			common_GTC = new CommonFunction_GTC();
			common_GTD = new CommonFunction_GTD();
			common_GTA = new CommonFunction_GTA();
			common_GTB = new CommonFunction_GTB();
			common_CMA = new CommonFunction_CMA();
			common_EP  = new CommonFunction_ExistingPolicy();
			common_PAA = new CommonFunction_PAA();
			common_PAB = new CommonFunction_PAB();
			common_DOB = new CommonFunction_DOB();
			App_logs.debug("Common Function Instances initialized sucessfully .");
		} catch (Throwable t) {
			System.out.println("Error while intantiating common functions instances.");
			throw new CommonFunctionsInitializationError("Error while intantiating common functions instances .");
		}

	}

	public static void Initialize_XL(String xlSuiteFile) throws DataExcelInitError {

		String NB_event = "_NB";
		String MTA_event = "_MTA";
		String NB_sheet = null , MTA_sheet=null;

		// Initialize Master book
		if (xlSuiteFile.contains("Suite")) {

			try {
				Master_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", "Suite.xlsx");
				isMasterXLInitialized = true;
			} catch (Throwable t) {
				throw new DataExcelInitError("Error while initializing Suite.xlsx data file . ");
			}

		} else {
			try {

				switch (TestBase.businessEvent) {

				case "NB":
					NB_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", xlSuiteFile);
					isNBSuiteXLInitialized = true;
					break;

				case "MTA":
					NB_sheet = product + NB_event + ".xlsx";
					NB_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", NB_sheet);
					isNBSuiteXLInitialized = true;
					Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", xlSuiteFile);
					isEventsSuiteXLInitialized = true;
					break;
				case "CAN":
					NB_sheet = product + NB_event + ".xlsx";
					NB_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", NB_sheet);
					isNBSuiteXLInitialized = true;
					Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", xlSuiteFile);
					isEventsSuiteXLInitialized = true;
					break;
				case "Rewind":
					NB_sheet = product + NB_event + ".xlsx";
					MTA_sheet = product + MTA_event + ".xlsx";
					NB_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", NB_sheet);
					isNBSuiteXLInitialized = true;
					MTA_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", MTA_sheet);
					isMTASuiteXLInitialized = true;
					Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", xlSuiteFile);
					isEventsSuiteXLInitialized = true;
					break;
				case "Requote":
					NB_sheet = product + NB_event + ".xlsx";
					NB_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", NB_sheet);
					isNBSuiteXLInitialized = true;
					Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", xlSuiteFile);
					isEventsSuiteXLInitialized = true;
					break;
				case "Renewal":
					NB_sheet = product + NB_event + ".xlsx";
					NB_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", NB_sheet);
					isNBSuiteXLInitialized = true;
					Events_Suite_TC_Xls = new XLS_Reader(workDir + "\\src\\com\\selenium\\database\\xls", xlSuiteFile);
					isEventsSuiteXLInitialized = true;
					break;

				default:
					App_logs.debug("** Business event is not in scope **");
					break;

				}
			} catch (Throwable t) {

				throw new DataExcelInitError("Error while initializing Test Case data file -  " + xlSuiteFile);
			}

		}

	}

}
