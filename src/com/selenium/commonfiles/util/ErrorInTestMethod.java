package com.selenium.commonfiles.util;


public class ErrorInTestMethod extends Exception {

	private static final long serialVersionUID = 1L;
	private String message = null;
	public ErrorInTestMethod(){
		
		super();
	}
	
	public ErrorInTestMethod(String message){
		
		super(message);
		this.message = message;
	}
	
	@Override
    public String toString() {
        return message;
    }
 
    @Override
    public String getMessage() {
        return message;
    }
	
	
}

