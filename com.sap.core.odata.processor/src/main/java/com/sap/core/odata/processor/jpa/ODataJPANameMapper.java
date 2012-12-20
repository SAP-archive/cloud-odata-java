package com.sap.core.odata.processor.jpa;

import java.util.HashMap;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.processor.jpa.access.api.NameMapper;

public class ODataJPANameMapper implements NameMapper {
	
	private HashMap<String, String> nameMap = null;
	
	public String getSourceName(FullQualifiedName fqName) {
		nameMap.get(fqName.toString());
		return null;
	}
	
	public void setSourceName(FullQualifiedName fqName,String sourceName){
		
	}
	
}
