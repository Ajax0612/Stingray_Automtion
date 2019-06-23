package com.selenium.commonfiles.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;


public class ArchiveReport {
	
	public ArchiveReport(){
		 String workD = System.getProperty("user.dir");
		 String report_path =workD+"\\src\\com\\selenium\\Execution_Report\\Report"; 
    	 String archive_path = workD+"\\src\\com\\selenium\\Execution_Report\\Archive";
    	 String img_path = report_path+"\\images";
    	 String pdf_path = report_path+"\\PDF";
    	// String logFldr = workD+"\\src\\com\\selenium\\Execution_Report\\Report\\exec_logs";
    	 String archive_report_folder = archive_path+"\\xReport_"+(new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));
    	 File file1 = new File(report_path);
    	 File file2 = new File(archive_report_folder);
    	 File imgFile = new File(img_path);
    	 File pdfFile = new File(pdf_path);
    	 //File logFolder = new File(logFldr);
    	 File logFile = new File(workD+"\\src\\com\\selenium\\Execution_Report\\Report\\exec_logs\\Application.log");
    	 try{
    		 if (file2.exists())
    			 file2.delete();
	    	 
	    	 FileUtils.copyDirectory(file1, file2);
	    	 FileUtils.cleanDirectory(imgFile);
	    	 // Clear PDF File folder
	    	 if (pdfFile.exists())
	    		 FileUtils.cleanDirectory(pdfFile);
	    	 
	    	 logFile.delete();
	    	// FileUtils.cleanDirectory(logFolder);
	    	 logFile.createNewFile();
    	 	}
    	 catch(IOException t){
    	 	// TestBase.logger.log(LogStatus.ERROR, "Archiving reports failed - Try again after closing all related files.");
    	 	 Assert.fail("Archiving reports failed - Try again after closing all related files.");
           	 }
    	     	 
    	 //return true;
	}

}
