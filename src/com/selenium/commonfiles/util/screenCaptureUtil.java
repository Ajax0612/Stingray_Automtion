package com.selenium.commonfiles.util;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.selenium.commonfiles.base.TestBase;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

	public class screenCaptureUtil {
	
		//src\com\selenium\screenshots
		public static void takeScreenshot(WebDriver webdriver,String fileWithPath) throws Exception{
			//TestBase t = new TestBase();
			if(TestBase.CONFIG.getProperty("browserType").equalsIgnoreCase("Firefox")){
			//Convert webdriver object to the TakesScreenshot 
			TakesScreenshot scrShot = ((TakesScreenshot)webdriver);
			//Call getScreenshotAs method to create image file
			File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
           // System.out.println(SrcFile.getAbsolutePath());
			//Move image file to new destination
            File DestFile=new File(fileWithPath);
            //Copy file at destination
            FileUtils.copyFile(SrcFile, DestFile);
            }else{            
			 Screenshot fpScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(webdriver);
			 ImageIO.write(fpScreenshot.getImage(),"PNG",new File(fileWithPath));}
			
		
		}
	
	
	
	
}
