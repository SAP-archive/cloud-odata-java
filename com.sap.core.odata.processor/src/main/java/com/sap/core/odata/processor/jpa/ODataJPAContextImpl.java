package com.sap.core.odata.processor.jpa;

import com.sap.core.odata.processor.jpa.access.api.NameMapper;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;

public class ODataJPAContextImpl implements ODataJPAContext {
	
	private String pUnitName;
	private NameMapper nameMapper;

	
	@Override
	public String getPersistenceUnitName() {
		return pUnitName;
	}

	@Override
	public void setPersistenceUnitName(String pUnitName) {
		this.pUnitName = pUnitName;
	}

	@Override
	public NameMapper getNameMapper() {
		return nameMapper;
	}

	@Override
	public void setNameMapper(NameMapper nameMapper) {

		
	}


}
