package com.sap.core.odata.processor.jpa.api.access;

import java.util.HashMap;

public class JPAReferenceEntityKey {
	
	private String entityName;
	private HashMap<String,String> keyPredicates;
	
	
	
	public JPAReferenceEntityKey(String entityName,
			HashMap<String, String> keyPredicates) {
		super();
		this.entityName = entityName;
		this.keyPredicates = keyPredicates;
	}
	
	public String getEntityName() {
		return entityName;
	}
	public HashMap<String, String> getKeyPredicates() {
		return keyPredicates;
	}
	
	
}
