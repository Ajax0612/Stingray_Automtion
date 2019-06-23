package com.selenium.commonfiles.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.Properties;
import java.util.Vector;

import org.openqa.selenium.By;

public class ObjectMap  {
	
	
	public static Properties properties;
	   
	   public ObjectMap(String mapFile)
	   {
		    properties = new Properties();
		    String saperator="" ;
		    FileInputStream ip=null;
		    try { 
		    Vector<FileInputStream> stream = new Vector<FileInputStream>() ;
			File folder = new File(mapFile);
			File[] listOfFiles = folder.listFiles();
			 System.out.println("=============Loading OR============");
			for (File file : listOfFiles) {
			    if (file.isFile() && file.getName().contains("OR")&& file.getName().endsWith("properties")) {
			       // System.out.println("Loading OR..."+file.getName());
			    	//System.out.println("...");
			    	saperator = saperator +" | ";
			    	
			    		ip = new FileInputStream(file.getAbsolutePath());
			        stream.add(ip);
				    }
			}
			System.out.println(saperator);
			 System.out.println("OR files loaded successfully...");
			SequenceInputStream sequenceInputStream = new SequenceInputStream(stream.elements());
			    // FileInputStream Master = new FileInputStream(mapFile);
		       properties.load(sequenceInputStream);
		       ip.close();
		           }catch (IOException e) {
		             System.out.println(e.getMessage());
		          }
	       }
	 

	   public By getLocator(String ElementName) throws Exception {
	          //Read value using the logical name as Key
	          String locator = properties.getProperty(ElementName);
	          if(locator.equals(null)){ 
	        	 TestUtil.reportStatus("Locator Not '" + ElementName + "' not defined in properties file!!", "Fail", false);
	        	  throw new Exception("Locator Not '" + ElementName + "' not defined in properties file!!");};
	          //Split the value which contains locator type and locator value
	          String locatorType = locator.split(":")[0];
	          String locatorValue = locator.split(":")[1];
	          //Return a instance of By class based on type of locator
	          switch(locatorType.toLowerCase()){
	          		case "id":
	          			return By.id(locatorValue);
	          		case "name":
	          			return By.name(locatorValue);
	          		case "class":
	          			return By.className(locatorValue);
	          		case "tagname":
	          			return By.tagName(locatorValue);
	          		case "tag":
	          			return By.className(locatorValue);
	          		case "linktext":
	          		case "link":
	          			return By.linkText(locatorValue);
	          		case "partiallinktext":
	          			return By.partialLinkText(locatorValue);
	          		case "cssselector":
	          		case "css":
	          			return By.cssSelector(locatorValue);
	          		case "xpath":
	          			return By.xpath(locatorValue);
	          			
	          			default:
	          				 throw new Exception("Locator type '" + locatorType + "' not defined!!");
	          			
	          }
	          
	   }
	          
	            /*if(locatorType.toLowerCase().equals("id"))
	                  return By.id(locatorValue);
	            else if(locatorType.toLowerCase().equals("name"))
	                  return By.name(locatorValue);
	            else if((locatorType.toLowerCase().equals("name")) || (locatorType.toLowerCase().equals("class")))
	                  return By.className(locatorValue);
	            else if((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag")))
	                  return By.className(locatorValue);
	            else if((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link")))
	                  return By.linkText(locatorValue);
	            else if(locatorType.toLowerCase().equals("partiallinktext"))
	                  return By.partialLinkText(locatorValue);
	            else if((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css")))
	                  return By.cssSelector(locatorValue);
	            else if(locatorType.toLowerCase().equals("xpath"))
	                  return By.xpath(locatorValue);
	            else
	                    throw new Exception("Locator type '" + locatorType + "' not defined!!");
	          }*/
	   public String getlocatorValue(String ElementName) throws Exception{
		   String locator = properties.getProperty(ElementName);
	          if(locator.equals(null)){ 
	        	 TestUtil.reportStatus("Locator Not '" + ElementName + "' not defined in properties file!!", "Fail", false);
	        	  throw new Exception("Locator Not '" + ElementName + "' not defined in properties file!!");}
	          //Split the value which contains locator type and locator value
	          String locatorType = locator.split(":")[0];
	          String locatorValue = locator.split(":")[1];
	          
	          return locatorValue;
	          
		   
	   }
	   
	   public Properties getORProperties(){
		   
		   return properties;
		   
	   }
	 }

