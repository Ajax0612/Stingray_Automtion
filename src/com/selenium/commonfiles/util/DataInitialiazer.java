package com.selenium.commonfiles.util;


public interface DataInitialiazer {
		void Initiliaze_DataStructure() throws Exception;
		void Populate_DataStructure(String current_TestDataID_index) throws DataStructurePopulateError;
		void Clear_DataStructure();
	
}
