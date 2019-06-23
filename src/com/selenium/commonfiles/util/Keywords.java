package com.selenium.commonfiles.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.os.WindowsUtils;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.relevantcodes.extentreports.LogStatus;
import com.selenium.commonfiles.base.CommonFunction;
import com.selenium.commonfiles.base.TestBase;

public class Keywords extends TestBase {
	// WebDriver driver = null;
	// String Url = (String) CONFIG.get("testSiteName");
	public DecimalFormat df = new DecimalFormat("#0.00");

	public void openBrowser() {
		String browser = (String) CONFIG.get("browserType");
try{
		processKill("chromedriver.exe");

		if (browser.equalsIgnoreCase("Firefox")) {

			ProfilesIni prof = new ProfilesIni();
			FirefoxProfile p = prof.getProfile("Automation");
			driver = new FirefoxDriver(p);

		}

		else if (browser.equalsIgnoreCase("Chrome")) {
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")
					+ "\\src\\com\\selenium\\configuration\\Add-ons\\ChromeDriverServer_Win\\chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("disable-infobars");
			driver = new ChromeDriver(options);

		}

		else if (browser.equalsIgnoreCase("IE")) {
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")
					+ "\\src\\com\\selenium\\configuration\\Add-ons\\IEDriverServer_Win\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();

		}
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
}catch(Throwable t){
	System.out.println(t.getMessage());
}
	}

	public void closeBrowser() {
		if (driver != null)
			driver.quit();
	}

	public void Navigate(String TestSiteURL) { // for Navigating the URL
		try {
			driver.navigate().to(TestSiteURL);
		} catch (Throwable t) {
			reportErr("Unable to launch the URL-" + TestSiteURL, t);
		}
	}

	public boolean Click(String xpathKey) { // Clicking on Object
		try {
			// driver.findElement(objmap.getLocator("Username_field")
			HighlightObject(xpathKey);
			if (scrollInView(xpathKey)) {
				driver.findElement(OR.getLocator(xpathKey)).click();
				return true;
			} else {
				throw new Exception("Scroll In View Error");
			}
		} catch (Exception t) {
			reportErr("Unable to click on object " + xpathKey, t);
			return false;
		}
	}

	// Created to avoid error in Consol. - Jainil
	public boolean ClickButton(String xpathKey) { // Clicking on Object
		try {
			// driver.findElement(objmap.getLocator("Username_field")
			HighlightObject(xpathKey);
			if (scrollInView(xpathKey)) {
				driver.findElement(OR.getLocator(xpathKey)).click();
				return true;
			} else {
				throw new Exception("Scroll In View Error");
			}
		} catch (Exception t) {

			return false;
		}
	}

	public boolean Input(String xpathKey, String text) { // For input to the
															// object
		try {
			if (scrollInView(xpathKey)) {
				driver.findElement(OR.getLocator(xpathKey)).sendKeys(Keys.chord(Keys.CONTROL, "a"));
				driver.findElement(OR.getLocator(xpathKey)).sendKeys(text);
				//HighlightObject(xpathKey);
				return true;
			} else {
				return false;
			}
		} catch (Throwable t) {
			reportErr("Unable to enter text (" + text + ") in field " + xpathKey, t);
			return false;
		}
	}
	
	public boolean InputWithoutError(String xpathKey, String text) { // For input to the
		// object
	try {
		if (scrollInViewWithOutError(xpathKey)) {
			driver.findElement(OR.getLocator(xpathKey)).sendKeys(Keys.chord(Keys.CONTROL, "a"));
			driver.findElement(OR.getLocator(xpathKey)).sendKeys(text);
			HighlightObject(xpathKey);
			return true;
		} else {
			return false;
		}
		} catch (Throwable t) {
			return false;
		}
	}
	
	public String GetText_DynamicXpathWebDriver(WebDriver element , String xpathKey) { // For input to the
		String getTextVal = "";
		try {
			if (xpathKey.contains("input")) {
				
					getTextVal = driver.findElement(By.xpath(xpathKey)).getAttribute("value").replaceAll(",", "");
					return getTextVal;
			} else {
				getTextVal = driver.findElement(By.xpath(xpathKey)).getText().replaceAll(",", "");
				return getTextVal;
			}
		} catch (Throwable t) {
		reportErr("Unable to get text for variable : " + xpathKey, t);
		return "";
		}
	}
	
	public boolean DynamicXpathWebElement(WebElement element , String xpathKey, String text , String keyWord) { // For input to the
		// object
		try {
			if (scrollInView(xpathKey)) {
				
					switch (keyWord) {
					case "Input":
						element.findElement(By.xpath(xpathKey)).sendKeys(text);
						//HighlightObject(xpathKey);
						break;
					case "GetText" :
						element.findElement(By.xpath(xpathKey)).getText();
						break;
					default:
						break;
					}
					return true;
			} else {
				return false;
			}
		} catch (Throwable t) {
		reportErr("Unable to enter text (" + text + ") in field " + xpathKey, t);
		return false;
		}
	}
	
	@SuppressWarnings("unused")
	public boolean DynamicXpathWebDriver_Inner(WebDriver element , String xpathKey , Map<String, List<Map<String, String>>> mdata ,String sheetName, int count , String  abrevation , String code , String sectionName , String keyWord) { // For input to the
		Map<String, List<Map<String, String>>> internal_data_map = new HashMap<>();
		//Map<Object, Object> event_data_map = 
		
		try {
			//if (scrollInView(xpathKey)) {
					int counter=0;
					String value = null;
					int dotCounter=0;
					char dot = '.';
					switch (keyWord) {
					case "Input":
						driver.findElement(By.xpath(xpathKey)).sendKeys(Keys.chord(Keys.CONTROL, "a"));
						value = mdata.get(sheetName).get(count).get(abrevation+code);
						driver.findElement(By.xpath(xpathKey)).sendKeys(value);
						
						if(value.contains(".") && ((code.equalsIgnoreCase("SumInsured")) || code.equalsIgnoreCase("DeclaredValue"))){
							
							int indexOfDot = value.indexOf(".");
							value = value.substring(0, indexOfDot);
							TestUtil.reportStatus("You have entered garbage value into "+code+" for "+sectionName+". Entered value is : <b>[  "+mdata.get(sheetName).get(count).get(abrevation+code)+"  ]</b>. Script has corrected it to <b>[  "+value+"  ]</b>", "Info", true);
							TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent,sheetName  , mdata.get(sheetName).get(count).get("Automation Key"), abrevation+code, value, mdata.get(sheetName).get(count));
							
						}else if(value.contains(".")){
							
							for(int j=0;j<value.length();j++){
								
								if(dot==value.charAt(j)){
									dotCounter++;
								}
							}
							
							if(dotCounter>1){
								value = "0";
								TestUtil.reportStatus("You have entered garbage value into "+code+" for "+sectionName+". Entered value is : <b>[  "+mdata.get(sheetName).get(count).get(abrevation+code)+"  ]</b>. Script has corrected it to <b>[  "+value+"  ]</b>", "Info", true);
								TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent,sheetName  , mdata.get(sheetName).get(count).get("Automation Key"), abrevation+code, value, mdata.get(sheetName).get(count));
							}/*else{
								value = mdata.get(sheetName).get(count).get(abrevation+code);
								TestUtil.reportStatus("You have entered garbage value into "+code+" for "+sectionName+". Entered value is : <b>[  "+mdata.get(sheetName).get(count).get(abrevation+code)+"  ]</b>. We have corrected it to <b>[  "+value+"  ]</b>", "Info", true);
								TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent,sheetName  , common.NB_Structure_of_InnerPagesMaps.get(sheetName).get(count).get("Automation Key"), mdata.get(sheetName).get(count).get(abrevation+code), value, mdata.get(sheetName).get(count));
							}*/
						}
						
						StringBuilder myNumbers = new StringBuilder();
					    for (int i = 0; i < value.length(); i++) {
					        if (Character.isDigit(value.charAt(i)) || (dotCounter==1 && dot==value.charAt(i))) {
					            myNumbers.append(value.charAt(i));
					        } else {
					        	counter++;
					        }
					    }
					    
					    if(counter>0){
					    	TestUtil.reportStatus("You have entered garbage value into "+code+" for "+sectionName+". Entered value is : <b>[  "+value+"  ]</b>. Script has corrected it to <b>[  "+myNumbers.toString()+"  ]</b>", "Info", true);
					    	TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent,sheetName  , mdata.get(sheetName).get(count).get("Automation Key"), abrevation+code, myNumbers.toString(), mdata.get(sheetName).get(count));
					    	counter=0;
					    }
					    
					    break;
					}
					return true;
				} catch (Throwable t) {
				reportErr("Unable to enter text (" + mdata.get(sheetName).get(count).get(abrevation+code) + ") in field " + xpathKey, t);
				return false;
				}
		}
	
	public boolean DynamicXpathWebDriver(WebDriver element , String xpathKey , Map<Object, Object> mdata ,String sheetName, int count , String  abrevation , String code , String sectionName , String keyWord) { // For input to the
		// object
		try {
			//if (scrollInView(xpathKey)) {
			int counter=0;
			String value = null;
			int dotCounter=0;
			char dot = '.';
			switch (keyWord) {
			case "Input":
				driver.findElement(By.xpath(xpathKey)).sendKeys(Keys.chord(Keys.CONTROL, "a"));
				value = (String)mdata.get(abrevation+code);
				driver.findElement(By.xpath(xpathKey)).sendKeys(value);
				
				if(value.contains(".") && ((code.equalsIgnoreCase("SumInsured")) || code.equalsIgnoreCase("DeclaredValue"))){
					
					int indexOfDot = value.indexOf(".");
					value = value.substring(0, indexOfDot);
					TestUtil.reportStatus("You have entered garbage value into "+code+" for "+sectionName+". Entered value is : <b>[  "+mdata.get(abrevation+code)+"  ]</b>. Script has corrected it to <b>[  "+value+"  ]</b>", "Info", true);
					TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent,sheetName  , (String)mdata.get("Automation Key"), abrevation+code, value, mdata);
					
				}else if(value.contains(".")){
					
					for(int j=0;j<value.length();j++){
						
						if(dot==value.charAt(j)){
							dotCounter++;
						}
					}
					
					if(dotCounter>1){
						value = "0";
						TestUtil.reportStatus("You have entered garbage value into "+code+" for "+sectionName+". Entered value is : <b>[  "+mdata.get(abrevation+code)+"  ]</b>. Script has corrected it to <b>[  "+value+"  ]</b>", "Info", true);
						TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent,sheetName  , (String)mdata.get("Automation Key"), abrevation+code, value, mdata);
					}/*else{
						value = mdata.get(sheetName).get(count).get(abrevation+code);
						TestUtil.reportStatus("You have entered garbage value into "+code+" for "+sectionName+". Entered value is : <b>[  "+mdata.get(sheetName).get(count).get(abrevation+code)+"  ]</b>. We have corrected it to <b>[  "+value+"  ]</b>", "Info", true);
						TestUtil.WriteDataToXl_innerSheet(CommonFunction.product+"_"+CommonFunction.businessEvent,sheetName  , common.NB_Structure_of_InnerPagesMaps.get(sheetName).get(count).get("Automation Key"), mdata.get(sheetName).get(count).get(abrevation+code), value, mdata.get(sheetName).get(count));
					}*/
				}
				
				StringBuilder myNumbers = new StringBuilder();
			    for (int i = 0; i < value.length(); i++) {
			        if (Character.isDigit(value.charAt(i)) || (dotCounter==1 && dot==value.charAt(i))) {
			            myNumbers.append(value.charAt(i));
			        } else {
			        	counter++;
			        }
			    }
			    if(counter>0){
			    	TestUtil.reportStatus("You have entered garbage value into "+code+" for "+sectionName+". Entered value is : <b>[  "+value+"  ]</b>. Script has corrected it to <b>[  "+myNumbers.toString()+"  ]</b>", "Info", true);
			    	TestUtil.WriteDataToXl(CommonFunction.product+"_"+CommonFunction.businessEvent, sheetName  , (String)mdata.get("Automation Key"), abrevation+code, myNumbers.toString(), mdata);
			    	counter=0;
			    }
				break;
			}
			return true;
			//} else {
			//	return false;
			//}
		} catch (Throwable t) {
		reportErr("Unable to enter text (" + (String)mdata.get(abrevation+code) + ") in field " + xpathKey, t);
		return false;
		}
	}

	public void reportErr(String testName, Throwable t) {
		// String logPath =
		// System.getProperty("user.dir")+"\\src\\com\\selenium\\logs\\Application.log";
		// <a target="_blank" href="<file:///C:\Users\10617201\workspace\Copy of
		// Stingray\src\com\selenium\logs\Application.log>">Tutorials Point</a>
		final String log_path = workDir + "\\src\\com\\selenium\\configuration\\logs\\Application.log";
		String link = "<a target='_blank' alt='click here for logs' href='file:///"
				+ log_path.concat("'>Execution Logs</a>");
		String file = screnshtpth + "_Error_screen" + (new Random().nextInt()) + ".png";
		try {
			screenCaptureUtil.takeScreenshot(TestBase.driver, file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			TestBase.logger.log(LogStatus.ERROR, testName, e.getLocalizedMessage());
			ErrorUtil.addVerificationFailure(e);
			Assert.fail("Unable to capture screenshot");
			return;
		}
		link = TestUtil.PathChange(link);
		System.out.println(testName + "\n" + t);
		String errorMessage = "";

		if (t.getLocalizedMessage() != null) {
			errorMessage = "," + t.getLocalizedMessage();
		}
		//String fmsg = "<p style='color:red'>" + testName + "<br>" + errorMessage + "</p>, click here for <u>" + link + "</u>";
		String fmsg = "<p style='color:red'> Errors - <br>" + errorMessage + "</p>, click here for <u>" + link + "</u>";
		// System.out.println(fmsg);
		// TestBase.logger.log(LogStatus.FAIL,fmsg , image);
		TestUtil.addInfo(fmsg);
		// TestBase.logger.log(LogStatus.INFO, "check here for error logs -->",
		// link);
		ErrorUtil.addVerificationFailure(t);
		App_logs.debug(testName, t);
	}

	public String getTitle() {
		try {
			return driver.getTitle();
		} catch (Throwable t) {
			reportErr("Unable to fetch the title of browser ", t);
			return null;
		}
	}

	public boolean isDisplayed(String xpathKey) {
		try {
			return driver.findElement(OR.getLocator(xpathKey)).isDisplayed();
		} catch (Throwable t) {
			reportErr("Object didn't display " + xpathKey, t);
			return false;
		}
	}
	
	public boolean isDisplayedByXpath(String Xpath) {
		try {
			k.scrollInViewByXpath(Xpath);
			return driver.findElement(By.xpath(Xpath)).isDisplayed();
		} catch (Throwable t) {
			reportErr("Object didn't display " + Xpath, t);
			return false;
		}
	}

	//This method is for Verification purpose only.
	public boolean isDisplayedField(String xpathKey) {
		try {
			k.ImplicitWaitOff();
			boolean tVal =  driver.findElement(OR.getLocator(xpathKey)).isDisplayed();
			k.ImplicitWaitOn();
			return tVal;
		} catch (Throwable t) {
			return false;
		}
	}
	
	public boolean checkElementPresent(String xpathKey) {

		try {
			int child = driver.findElements(OR.getLocator(xpathKey)).size();
			HighlightObject(xpathKey);
			Assert.assertTrue(child > 0, xpathKey + "- Element doesn't exist");
		} catch (Throwable t) {

			reportErr(xpathKey + " - Element doesn't exist", t);
			return false;
		}
		return true;
	}

	// This function is checking if element is present or not.. If not it won't
	// throw any error : Jainil
	public boolean isElementPresent(String xpathKey) {

		try {
			int child = driver.findElements(OR.getLocator(xpathKey)).size();
			HighlightObject(xpathKey);
			Assert.assertTrue(child > 0, xpathKey + "- Element doesn't exist");
		} catch (Throwable t) {

			return false;
		}
		return true;
	}

	public static WebElement getObject(String xpathKey) throws Exception {
		return driver.findElement(OR.getLocator(xpathKey));
	}

	public List<WebElement> findElements(String xpathKey) {
		try {
			List<WebElement> wb = driver.findElements(OR.getLocator(xpathKey));
			return wb;
		} catch (Throwable t) {
			reportErr("Unable to find the child Items " + xpathKey, t);
			return null;
		}
	}

	public String getText(String xpathKey) {
		try {
			return driver.findElement(OR.getLocator(xpathKey)).getText();
		} catch (Exception t) {
			reportErr("Unable to get text for the object " + xpathKey, t);

			return null;
		}
	}
	
	public String getTextWithoutError(String xpathKey) {
		try {
			k.ImplicitWaitOff();
			return driver.findElement(OR.getLocator(xpathKey)).getText();
		} catch (Exception t) {
			k.ImplicitWaitOn();
			return null;
			
		}
	}

	/**
	 * Method wait 5 seconds and during that process it will catch any exception
	 * that get thrown.
	 */
	public void waitFiveSeconds() {
		try {
			Thread.sleep(2500);
		} catch (Exception t) {
			reportErr("wait() interrupted! \n", t);

		}
	}

	/**
	 * Method wait 10 seconds and during that process it will catch any
	 * exception that get thrown.
	 */
	public void waitTenSeconds() {
		try {
			Thread.sleep(5000);
		} catch (Exception t) {
			reportErr("wait() interrupted! \n" + " Error-:" + t, t);

		}
	}

	/**
	 * Method checks if the checkbox is checked
	 */
	public boolean chkboxSelection(String xpathKey) {
		try {
			HighlightObject(xpathKey);
			return driver.findElement(OR.getLocator(xpathKey)).isSelected();
		} catch (Throwable t) {
			reportErr("Object didn't display " + xpathKey, t);
			return false;
		}
	}
	
	public boolean chkboxSelection_Dynamic(String xpathKey) {
		try {
			
			return driver.findElement(By.xpath(xpathKey)).isSelected();
		} catch (Throwable t) {
			reportErr("Object didn't display " + xpathKey, t);
			return false;
		}
	}

	/**
	 * Method gets the attribute of any object
	 */
	public String getAttribute(String xpathKey, String value) {
		try {
			return driver.findElement(OR.getLocator(xpathKey)).getAttribute(value);
		} catch (Exception t) {
			reportErr("Unable to get text for the object" + xpathKey, t);
			return null;
		}
	}

	public boolean funHndlchildchkbx(String xpath, String classname, String action) throws Exception {
		Throwable t = null;
		try {
			List<WebElement> boxes = driver.findElements(By.className(classname));
			for (WebElement ss : boxes) {
				if (!ss.isSelected() && action.equalsIgnoreCase("verify")) {
					reportErr("unchecked box is " + ss.getAttribute("name") + " " + xpath, t);
					return false;
				}
				if (action.equalsIgnoreCase("click")) {
					ss.click();
				}
				if (action.equalsIgnoreCase("clear")) {
					ss.clear();
				}
			}
			return true;
		} catch (Throwable t1) {
			reportErr("Error in Perils page", t1);
			return false;
		}

	}

	/**
	 * Method to select item from Dropdown list
	 */
	
	public boolean DropDownSelection(String xpathKey, String option) {
		try {
			WebElement SelectItem = driver.findElement(OR.getLocator(xpathKey));
			Select mySelect = new Select(SelectItem);
			try {
				
				mySelect.selectByValue(option);
				
			} catch (Throwable tt) {
				try{
					
					mySelect.selectByVisibleText(option);
				}catch(Throwable ttt){
					
					mySelect.selectByIndex(Integer.parseInt(option));
				}
			}
			
			return true;
		} catch (Throwable t) {
			reportErr("Object didn't display " + xpathKey, t);
			return false;
		}
	}
	
	/**
	 * Method to select item from Dropdown list using dynamic xpath
	 */
	
	public boolean DropDownSelection_DynamicXpath(String xpathKey, String option) {
		try {
			WebElement SelectItem = driver.findElement(By.xpath(xpathKey));
			Select mySelect = new Select(SelectItem);
			try {
				mySelect.selectByValue(option);
			} catch (Throwable tt) {
				try{
					mySelect.selectByVisibleText(option);
				}catch(Throwable ttt){
					mySelect.selectByIndex(Integer.parseInt(option));
				}
			}
			return true;
		} catch (Throwable t) {
			reportErr("Object didn't display " + xpathKey, t);
			return false;
		}
	}
	
	/**
	 * Method to select item from Dropdown list using dynamic xpath
	 */
	
	public boolean DropDownSelection_WebElement(WebElement elm, String option) {
		try {
			//WebElement SelectItem = driver.findElement(By.xpath(xpathKey));
			k.ImplicitWaitOff();
			Select mySelect = new Select(elm);
			try {
				mySelect.selectByValue(option);
			} catch (Throwable tt) {
				try{
					mySelect.selectByVisibleText(option);
				}catch(Throwable ttt){
					mySelect.selectByIndex(Integer.parseInt(option));
				}
			}
			return true;
		} catch (Throwable t) {
			reportErr("Object didn't display " + elm, t);
			return false;
		}finally {
			k.ImplicitWaitOn();
		}
	}

	/**
	 * Method to verify the objects on a page
	 */
	public void funCheckObjectsOnPage(String Object, String classname, String attributeValue, ArrayList<String> Array)
			throws Exception {
		Throwable t = null;
		boolean mfound = false;
		try {
			if (Object.equalsIgnoreCase("textboxes")) {
				List<WebElement> objects = driver.findElements(By.className(classname));
				for (WebElement ss : objects) {
					String TextBoxValue = ss.getAttribute(attributeValue);
					for (int i = 0; i <= Array.size(); i++)
						if (TextBoxValue.equalsIgnoreCase(Array.get(i))) {
							TestUtil.reportStatus("Text Box found on page - " + TextBoxValue, "info", false);
							mfound = true;
							break;
						}
					if (mfound = false)
						TestUtil.reportStatus("Text Box not found on page - " + TextBoxValue, "info", false);
				}

			}
			if (Object.equalsIgnoreCase("radio")) {
				List<WebElement> objects = driver.findElements(By.tagName(classname));
				for (WebElement ss : objects) {
					String TextBoxValue = ss.getAttribute("type");
					if (TextBoxValue.equalsIgnoreCase("radio")) {
						String radioItem = ss.getAttribute(attributeValue);
						for (int i = 0; i <= Array.size(); i++)
							if (radioItem.equalsIgnoreCase(Array.get(i))) {
								TestUtil.reportStatus("Radio Button found on page - " + radioItem, "info", false);
								mfound = true;
								break;
							}

						if (mfound = false)
							TestUtil.reportStatus("Radio Button not found on page - " + radioItem, "info", false);
					}
				}
			}
			if (Object.equalsIgnoreCase("DropDown")) {
				mfound = false;
				String ListOptions = null;
				List<WebElement> Selectobjects = driver.findElements(By.className(classname));
				for (WebElement ss : Selectobjects) {
					Select mySelect = new Select(ss);
					List<WebElement> options = mySelect.getOptions();
					for (WebElement option : options) {
						ListOptions = option.getText();
						if (options.size() == Array.size()) {
							for (int i = 0; i <= Array.size(); i++)
								if (ListOptions.equalsIgnoreCase(Array.get(i))) {
									TestUtil.reportStatus("List Item " + ListOptions + " found in the ListBox", "info",
											false);
									mfound = true;
									break;
								}
						}
					}

					if (mfound == true) {
						return;
					}
				}
				if (mfound == false) {
					TestUtil.reportStatus("List Item " + ListOptions + " not found in the ListBox - " + Selectobjects,
							"info", false);

				}

			}
		} catch (Throwable t1) {
			reportErr("Error in finding objects in page", t1);
		}
	}

	/**
	 * Method types value in text box
	 */
	public boolean Type(String xpathKey, String text) {
		try {
			if (isDisplayed(xpathKey)) {
				driver.findElement(OR.getLocator(xpathKey)).sendKeys(Keys.chord(Keys.CONTROL, "a"), text);
				return true;
			}
		} catch (Exception t) {
			reportErr("Unable to input the text into the  object " + xpathKey, t);
		}
		return false;
	}

	/**
	 * Method to verify the objects on a page
	 */
	public boolean funCheckButtonsOnPage(String classname, String attributeValue, ArrayList<String> Array)
			throws Exception {
		Throwable t = null;
		boolean mfound = false;
		try {

			List<WebElement> objects = driver.findElements(By.className(classname));
			for (WebElement ss : objects) {
				String Button = ss.getAttribute(attributeValue);
				for (int i = 0; i <= Array.size(); i++)
					if (Button.equalsIgnoreCase(Array.get(i))) {
						TestUtil.reportStatus("Button found on page - " + Button, "info", false);
						mfound = true;
						break;
					}
				if (mfound = false)
					TestUtil.reportStatus("Button not found on page - " + Button, "info", false);
			}
			return true;
		} catch (Throwable t1) {
			reportErr("Error in finding Buttons on the page", t1);
			return false;
		}
	}

	public boolean SendKeysByXpath(String xpathKey, String text) { // For input
																	// to the
																	// object
		try {
			HighlightObject(xpathKey);
			driver.findElement(By.xpath(xpathKey)).sendKeys(text);
			return true;
		} catch (Throwable t) {
			reportErr("Unable to enter text (" + text + ") in field " + xpathKey, t);
			return false;
		}
	}

	public String getTextByXpath(String xpathKey) { // For input to the object
		try {
			return driver.findElement(By.xpath(xpathKey)).getText();

		} catch (Throwable t) {
			reportErr("Unable to get text for the object" + xpathKey, t);
			return null;
		}
	}

	public void pressDownKeyonPage() {
		driver.findElement(By.xpath("html/body")).sendKeys(Keys.PAGE_DOWN);
	}

	public void pressUpKeyonPage() {
		driver.findElement(By.xpath("html/body")).sendKeys(Keys.PAGE_UP);
	}

	/**
	 * Method to select item from Dropdown list
	 */
	public boolean DropDownSelectionByIndex(String xpathKey, int option) {
		try {
			WebElement SelectItem = driver.findElement(OR.getLocator(xpathKey));
			Select mySelect = new Select(SelectItem);

			mySelect.selectByIndex(option);
			return true;
		} catch (Throwable t) {
			reportErr("Object didn't display " + xpathKey, t);
			return false;
		}
	}
	
	/**
	 * Method to select item from Dropdown list
	 */
	public boolean DropDownSelectionByVal(String xpathKey, String option) {
		try {
			WebElement SelectItem = driver.findElement(OR.getLocator(xpathKey));
			Select mySelect = new Select(SelectItem);

			mySelect.selectByValue(option);
			return true;
		} catch (Throwable t) {
			reportErr("Object didn't display " + xpathKey, t);
			return false;
		}
	}

	public String roundedOff(String number) {

		String replacedString = number.replace(".", ",");

		String[] stringArray = replacedString.split(",");

		if (stringArray[1].length() == 2) {
			double parsedNumber = Double.parseDouble(number);
			return Double.toString(parsedNumber);
		} else {

			String formatedNumber = df.format(Double.parseDouble(number));
			return formatedNumber;

		}

	}

	public double FMT(double d) {
		try {
			DecimalFormat f = new DecimalFormat("00.00");
			return Double.parseDouble(f.format(d));
		} catch (Throwable t) {
			reportErr("Formatting issue " + d, t);
			return 0.00;
		}
	}

	public int ValidateValues(double exp, double act, String msg) {
		try {
			int retvalue = 0;
			if (exp != act) {
				retvalue = retvalue + 1;
				TestUtil.reportStatus(
						"<p style='color:red'>" + msg + " Verified - Expected [" + exp + "] with Actual [" + act + "]",
						"Fail", false);
				;
			} else {
				TestUtil.reportStatus(
						msg + " Verified - Expected <b>[" + exp + "]</b> with Actual <b>[" + act + "]</b>", "Pass",
						false);
			}
			return retvalue;
		} catch (Throwable t) {
			return 1;
		}
	}

	public int ValidateStringValues(String exp, String act, String msg) {
		try {
			int retvalue = 0;
			if (exp != act) {
				retvalue = retvalue + 1;
				TestUtil.reportStatus(
						"<p style='color:red'>" + msg + " Not Matched with " + exp + " from page value - " + act,
						"fail", true);
			} else {
				TestUtil.reportStatus(msg + " Verified - Expected [" + exp + "] with Actual [" + act + "]", "Pass",
						false);
			}
			return retvalue;
		} catch (Throwable t) {
			return 1;
		}
	}

	public int ValidateDifference_Calculations(double exp, double act, String msg) {
		try {
			int retvalue = 0;
			double diff = exp - act;

			if (Math.abs(diff) <= 0.02) {
				TestUtil.reportStatus("Expected " + msg + " [<b> " + exp + " </b>] matches with actual " + msg
						+ "  [<b> " + act + " </b>]as expected on premium summary page.", "Pass", false);
			} else {
				TestUtil.reportStatus("<p style='color:red'> Mismatch in values, Expectd is : [<b> " + exp
						+ "</b>] and Actual displayd on application is : [<b> " + act
						+ "</b>] on premium summary page. </p>", "Fail", true);
			}
			return retvalue;
		} catch (Throwable t) {
			return 1;
		}

	}

	public int ValidatePremium(double exp, double act) {
		try {
			int retvalue = 0;
			if (exp != act) {
				retvalue = retvalue + 1;
				// TestUtil.reportStatus(msg+" Not Matched with "+act +" from
				// page value - "+exp, "fail", true);
			} else {

			}
			return retvalue;
		} catch (Throwable t) {
			return 1;
		}

	}

	public boolean SelectRadioBtn(String xpathKey, String option) throws Exception {
		
		if (scrollInView(xpathKey)) {
			List<WebElement> cbp_radio = driver.findElements(OR.getLocator(xpathKey));
			for (int e = 0; e < cbp_radio.size(); e++) {
				String val = cbp_radio.get(e).getAttribute("value");
				if (val.equalsIgnoreCase(option)) {
					cbp_radio.get(e).click();
					break;
				}
			}
			return true;
		} else {
			return false;
		}
		
	}
	
	//This will select button.
	public boolean SelectBtnWebElement(WebElement element , String xpathKey, String option) throws Exception {
		List<WebElement> cbp_radio = element.findElements(OR.getLocator(xpathKey));
		for (int e = 0; e < cbp_radio.size(); e++) {
			String val = cbp_radio.get(e).getText();
			if (val.equalsIgnoreCase(option)) {
				JavascriptExecutor j_exe = (JavascriptExecutor) driver;
				j_exe.executeScript("arguments[0].scrollIntoView(true);", cbp_radio.get(e));
				cbp_radio.get(e).click();
				break;
			}
		}

		return true;
	}

	/**
	 * Method types value in text box
	 */
	public boolean DropDownType(String xpathKey, String text) {
		try {
			if (isDisplayed(xpathKey)) {
				driver.findElement(OR.getLocator(xpathKey)).sendKeys(Keys.chord(text));
				return true;
			}
		} catch (Exception t) {
			reportErr("Unable to input the text into the  object " + xpathKey, t);
		}
		return false;
	}

	// Highlight the object

	public static void HighlightObject(String xpathKey) throws Exception {
		// Parsing xpath and declaring element
		WebElement element = driver.findElement(OR.getLocator(xpathKey));
		// Creating JavaScriptExecuter Interface
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (int iCnt = 0; iCnt < 3; iCnt++) {
			// Execute javascript
			js.executeScript("arguments[0].style.border='2px groove red'", element);
			Thread.sleep(20);
			js.executeScript("arguments[0].style.border=''", element);
		}
	}

	public void WaitUntilClickable(String xpathKey, int TimeUnit) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, TimeUnit);
		wait.until(ExpectedConditions.elementToBeClickable(OR.getLocator(xpathKey)));

	}

	public boolean AcceptPopup() {
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
			return true;
		} 
		  catch(NoAlertPresentException Ex){
			  TestUtil.reportStatus("No Alert box appears while de-selection of check boxes ", "info", false);
			  return true;
		  }
		catch (Exception t) {
			k.reportErr("Unable to detect popup box. ", t);
		}
		return false;

	}

	/**
	 * Method to scroll until object comes into view
	 */
	public boolean scrollInView(String xpathKey) {

		try {
			WebElement element = driver.findElement(OR.getLocator(xpathKey));
			JavascriptExecutor j_exe = (JavascriptExecutor) driver;
			j_exe.executeScript("arguments[0].scrollIntoView(true);", element);
			return true;
		} catch (Exception t) {
			reportErr("Unable to Scroll In View for object " + xpathKey, t);
			return false;
		}
	}
	
	public boolean scrollInViewWithOutError(String xpathKey) {

		try {
			WebElement element = driver.findElement(OR.getLocator(xpathKey));
			JavascriptExecutor j_exe = (JavascriptExecutor) driver;
			j_exe.executeScript("arguments[0].scrollIntoView(true);", element);
			return true;
		} catch (Exception t) {
			return false;
		}
	}

	/**
	 * Method to get Selected item from dropdown list
	 */
	public String GetDropDownSelectedValue(String xpathKey) {
		try {
			if (scrollInView(xpathKey)) {
				WebElement SelectItem = driver.findElement(OR.getLocator(xpathKey));
				Select mySelect = new Select(SelectItem);
				try {
					return mySelect.getFirstSelectedOption().getText();
				} catch (Throwable tt) {
					TestUtil.reportStatus(
							"<p style='color:red'> Unable get Selected value from dropdown - " + xpathKey + "</p>",
							"Fail", true);
					return "";
				}

			} else {
				throw new Exception("Scroll In View Error for an object - " + xpathKey);
			}

		} catch (Throwable t) {
			k.reportErr("Object didn't display " + xpathKey, t);
			return "";
		}
	}
	
	public String GetDropDownSelectedValueIgnoreError(String xpathKey) {
		try {
			
				WebElement SelectItem = driver.findElement(OR.getLocator(xpathKey));
				Select mySelect = new Select(SelectItem);
				return mySelect.getFirstSelectedOption().getText();

		} catch (Throwable t) {
			//k.reportErr("Object didn't display " + xpathKey, t);
			return "";
		}
	}

	/**
	 * Method to scroll until object comes into view
	 */
	public boolean scrollInViewByXpath(String xpathValue) {

		try {
			WebElement element = driver.findElement(By.xpath(xpathValue));
			JavascriptExecutor j_exe = (JavascriptExecutor) driver;
			j_exe.executeScript("arguments[0].scrollIntoView(true);", element);
			return true;
		} catch (Exception t) {
			reportErr("Unable to Scroll In View for object " + xpathValue, t);
			return false;
		}
	}

	/**
	 * Method wait 2 seconds and during that process it will catch any exception
	 * that get thrown.
	 */
	public void waitTwoSeconds() {
		try {
			Thread.sleep(500);
		} catch (Exception t) {
			k.reportErr("wait() interrupted! \n", t);

		}
	}

	/**
	 * Method to kill process by Name
	 */
	public void processKill(String processName) {

		try {
			if (isProcessRunning(processName)) {
				WindowsUtils.killByName(processName);
			}
		} catch (Exception t) {
			System.out.println(("Unable to kill process " + processName));

		}
	}

	/**
	 * Method to check given process is running or not.
	 */
	public static boolean isProcessRunning(String processName) {
		boolean r_value = false;
		try {
			String line;
			Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				//System.out.println(line);
				if (line.contains(processName)) {
					r_value = true;
					break;
				}
			}
			input.close();
		} catch (Exception err) {
			err.printStackTrace();
			r_value = false;

		}

		return r_value;
	}
	
	
	public String getLinkText(String xpathKey) {
		try {
			return driver.findElement(OR.getLocator(xpathKey)).getText();
		} catch (Exception t) {
			reportErr("Unable to get text for the object " + xpathKey, t);

			return null;
		}
	}
	
	/**
	 * Method gets the attribute of any object
	 */
	public boolean getAttributeIsEmpty(String xpathKey,String value) {
		try {
			return driver.findElement(OR.getLocator(xpathKey)).getAttribute(value).isEmpty();
			//k.getAttribute("CCF_PD_ProposerName", "value").isEmpty()
		} catch (Exception t) {
			reportErr("Unable to get text for the object" + xpathKey, t);
			return false;
		}
	}
	/**
	 * Method gets the attribute of any list
	 */
	public boolean getListAttributeIsEmpty(String xpathKey,String value) {
		try {
			return driver.findElement(OR.getLocator(xpathKey)).getText().isEmpty();
			//k.getAttribute("CCF_PD_ProposerName", "value").isEmpty()
		} catch (Exception t) {
			reportErr("Unable to get text for the object" + xpathKey, t);
			return false;
		}
	}
	
	/**
	 * Method find web elements
	 */
	public List<WebElement> getWebElements(String xpathKey) {
		try {
			return driver.findElements(OR.getLocator(xpathKey));
			//k.getAttribute("CCF_PD_ProposerName", "value").isEmpty()
		} catch (Exception t) {
			reportErr("Unable to find web elements -" + xpathKey, t);
			return null;
		}
	}
	
	/**
	 * Method clicks inner pages buttons
	 */
public boolean clickInnerButton(String xpathKey,String button){
		
		boolean r_value = true;
		WebElement parentPage;
		
		try {
			parentPage = Keywords.getObject(xpathKey);
			ScrollInVewWebElement(parentPage);
			List<WebElement> listOfButtons = parentPage.findElements(By.tagName("a"));
		
			for(int k=0;k<listOfButtons.size();k++){
				String buttonName = listOfButtons.get(k).getText();
				if(buttonName.equalsIgnoreCase(button)){
					listOfButtons.get(k).click();
					break;
				}
			}
		} catch (Exception e) {
			reportErr("Unable to Click inner button -" + button, e);
			return false;
		}
		return r_value;
	}

	// End of Keywords Class

public int getTableIndex(String sUniqueColumn, String sKey, String sKeyValue){
		
	  k.ImplicitWaitOff();
	   int iTableId = 0, numOfTables = 0, iColumns;
	   List<WebElement> listTables = new ArrayList<>();
	   try{
	      String sInitialable_Path = "html/body/div[3]/form/div/table";
	      switch (sKey){
	      	case "class" :
	            numOfTables = driver.findElements(By.className(sKeyValue)).size();
	            listTables = driver.findElements(By.className(sKeyValue));
	            break;
	      	case "id" :
	            numOfTables = driver.findElements(By.id(sKey)).size();
	            listTables = driver.findElements(By.id(sKey));
	            break;
	        case "xpath" :
	            numOfTables = driver.findElements(By.xpath("html/body/div[3]/form/div/table")).size();
	            listTables = driver.findElements(By.xpath("html/body/div[3]/form/div/table"));
	            break;                                          
	      }
	      for(int i = 0; i < numOfTables; i++){
	    	  iColumns = listTables.get(i).findElements(By.tagName("th")).size();
	    	  for(int j = 0; j < iColumns; j++){
	    		  String sColName = driver.findElement(By.xpath(sInitialable_Path + "[" + (i+1) +"]/thead/tr/th["+ (j+1)+"]")).getText();
	    		  if(sColName.equals(sUniqueColumn)){
	    			  iTableId = i+1;
	    			  return iTableId;
	    		  }
	    	  }
	      }
	   return iTableId; 
	   }catch(Throwable t){
	          System.out.println("Error while getting table id - "+t);
	          return 0;
	   }finally {
		  
		   k.ImplicitWaitOn();
	   }
	   
	}
	
public void ImplicitWaitOn(){
	 driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
}
public void ImplicitWaitOff(){
	 driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
}
public boolean DynamicXpathWebElement_driver(String xpathKey, String text ,String keyWord) { // For input to the
	// object
	try {
		//if (scrollInView(xpathKey)) {
						
				switch (keyWord) {
				case "Input":
					driver.findElement(By.xpath(xpathKey)).sendKeys(Keys.chord(Keys.CONTROL, "a"));
					driver.findElement(By.xpath(xpathKey)).sendKeys(text);
					//HighlightObject(xpathKey);
					break;
				case "GetText" :
					driver.findElement(By.xpath(xpathKey)).getText();
					break;
				default:
					break;
				}
				return true;
		//} else {
			//return false;
		//}
	} catch (Throwable t) {
	reportErr("Unable to enter text (" + text + ") in field " + xpathKey, t);
	return false;
	}
}
public boolean DynamicXpathWebElement_WebElement(WebElement elm, String text ,String keyWord) { // For input to the
	// object
	try {
		//if (scrollInView(xpathKey)) {
						
				switch (keyWord) {
				case "Input":
					elm.sendKeys(Keys.chord(Keys.CONTROL, "a"));
					elm.sendKeys(text);
					//HighlightObject(xpathKey);
					break;
				case "GetText" :
					elm.getText();
					break;
				case "GetValue" :
					elm.getAttribute("value");
					break;
				default:
					break;
				}
				return true;
		//} else {
			//return false;
		//}
	} catch (Throwable t) {
		reportErr("Unable to enter text (" + text + ") in field " + elm, t);
		return false;
	}
}


public String DynamicXpathWebElement_GetValue(WebElement elm,String keyWord) { // For input to the
	// object
	try {

				String value=""	;
				switch (keyWord) {
				case "GetText" :
					value = elm.getText();
					break;
				case "GetValue" :
					value = elm.getAttribute("value");
					break;
				default:
					break;
				}
				return value;
	} catch (Throwable t) {
		reportErr("Unable to enter text () in field " + elm, t);
		return "";
	}
}

/**
 * Method gets the attribute of any object
 */
public String getAttributeByXpath(String xpathKey, String value) {
	try {
		return driver.findElement(By.xpath(xpathKey)).getAttribute(value);
	} catch (Exception t) {
		reportErr("Unable to get text for the object" + xpathKey, t);
		return null;
	}
}

public boolean ScrollInVewWebElement(WebElement element){
	
	try {
		JavascriptExecutor j_exe = (JavascriptExecutor) driver;
		j_exe.executeScript("arguments[0].scrollIntoView(true);", element);
		return true;
	} catch (Exception t) {
		reportErr("Unable to Scroll In View for element " + element, t);
		return false;
	}
	
	
	
	
	
}

public boolean InputByXpath(String xpathKey, String text) { // For input to the
	// object
try {
if (scrollInViewByXpath(xpathKey)) {
driver.findElement(By.xpath(xpathKey)).sendKeys(Keys.chord(Keys.CONTROL, "a"));
driver.findElement(By.xpath(xpathKey)).sendKeys(text);
//HighlightObject(xpathKey);
return true;
} else {
return false;
}
} catch (Throwable t) {
reportErr("Unable to enter text (" + text + ") in field " + xpathKey, t);
return false;
}
}

/**
 * Method to get Selected item from dropdown list
 */
public String GetDropDownSelectedValue_WebElement(WebElement elm) {
	try {
		//if (scrollInView(xpathKey)) {
			
			Select mySelect = new Select(elm);
			try {
				return mySelect.getFirstSelectedOption().getText();
			} catch (Throwable tt) {
				TestUtil.reportStatus(
						"<p style='color:red'> Unable get Selected value from dropdown - " + elm + "</p>",
						"Fail", true);
				return "";
			}

		

	} catch (Throwable t) {
		k.reportErr("Object didn't display " + elm, t);
		return "";
	}
}


}
