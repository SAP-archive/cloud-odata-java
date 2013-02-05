package com.sap.core.odata.processor.core.jpa.model;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.Metamodel;


public class MockModel {
	
	private static final String PUNIT_NAME = "salesorderprocessing";
	
	private EntityManagerFactory entityManagerFactory;
	private Metamodel metaModel;
	private String persistentUnitName;
	
	MockModel()
	{
		this.persistentUnitName = PUNIT_NAME;
		this.entityManagerFactory = Persistence.createEntityManagerFactory(PUNIT_NAME);
		this.metaModel = this.entityManagerFactory.getMetamodel();
	}
	
	public Metamodel getMetaModel()
	{
		return metaModel;
	}
	public String getPersistentUnitName()
	{
		return this.persistentUnitName;
	}
	public EntityManagerFactory getEntityManagerFactory()
	{
		return this.entityManagerFactory;
	}
	
	

}
