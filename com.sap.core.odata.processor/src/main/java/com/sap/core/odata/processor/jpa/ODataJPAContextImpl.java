package com.sap.core.odata.processor.jpa;

import javax.persistence.EntityManagerFactory;

import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.processor.jpa.access.api.NameMapper;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;

public class ODataJPAContextImpl implements ODataJPAContext {
	
	private String pUnitName;
	private NameMapper nameMapper;
	private EntityManagerFactory emf;
	private ODataContext odataContext;

	
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

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return this.emf;
	}

	@Override
	public void setEntityManagerFactory(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@Override
	public void setODataContext(ODataContext ctx) {
		this.odataContext = ctx;
		
	}

	@Override
	public ODataContext getODataContext() {
		return this.odataContext;
	}
	
	

}
