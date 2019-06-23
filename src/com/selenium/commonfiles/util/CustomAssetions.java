package com.selenium.commonfiles.util;

import java.util.List;

import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;
import org.testng.collections.Lists;

import com.selenium.commonfiles.util.TestUtil;

public class CustomAssetions extends Assertion {
		 
		  private List<String> m_messages = Lists.newArrayList();
		 
		  @Override
		  public void onBeforeAssert(@SuppressWarnings("rawtypes") IAssert a) {
		    m_messages.add("Test:" + a.getMessage());
		  }
		  
		  @Override
		  public void assertTrue(boolean condition, String message){
			 
			  if(!condition && !message.equals("")){
				  
				  TestUtil.reportStatus("<p style='color:red'>"+message+"</p>" , "Fail", true);
				  throw new AssertionError(" -"+message);
			  }else if(!condition && message.equals("")){
				  
				  throw new AssertionError();
			  }
		  }
		  
		  @Override
		  public void assertTrue(boolean condition){
			 
			  if(!condition){
				  
				  TestUtil.reportStatus("<p style='color:red'>Assertion Faiure</p>" , "Fail", true);
				  throw new AssertionError();
			  }
		  }
		  
		  
		  @Override
		  public void assertEquals(String str1,String str2 ,String message){
		 
			  if(!(str1.equalsIgnoreCase(str2))){
				  
				  TestUtil.reportStatus("<p style='color:red'>"+message+"</p>" , "Fail", true);
				  throw new AssertionError(" -"+message);
			  }
		  }
		  
		  
		  @Override
		  public void assertEquals(int value1,int value2 ,String message){
			 
			  if(!(value1 == value2)){
				  
				  TestUtil.reportStatus("<p style='color:red'>"+message+"</p>" , "Fail", true);
				  throw new AssertionError(" -"+message);
			  }
		  }
		  
		  //Soft assertion
		  public void SoftAssertEquals(int value1,int value2 ,String message){
			 
			  if(!(value1 == value2)){
				  
				  TestUtil.reportStatus("<p style='color:red'>"+message+"</p>" , "Fail", true);
			  }
		  }
		  public void SoftAssertEquals(String value1,String value2 ,String message){
				 
			  if(!(value1.equalsIgnoreCase(value2))){
				  
				  TestUtil.reportStatus("<p style='color:red'>"+message+"</p>" , "Fail", true);
			  }
		  }
		  
		  public List<String> getMessages() {
		    return m_messages;
		  }
		


}




	
