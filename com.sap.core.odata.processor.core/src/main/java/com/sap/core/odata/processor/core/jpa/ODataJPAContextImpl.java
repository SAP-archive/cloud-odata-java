package com.sap.core.odata.processor.core.jpa;

import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManagerFactory;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;

public class ODataJPAContextImpl implements ODataJPAContext {
	
	private String pUnitName;
	private EntityManagerFactory emf;
	private ODataContext odataContext;
	private ODataProcessor processor;
	private EdmProvider	edmProvider;
	private String jpaEdmMappingModelName;
	private static final ThreadLocal<List<Locale>> oDataContextThreadLocal = new ThreadLocal<List<Locale>>();

	
	@Override
	public String getPersistenceUnitName() {
		return pUnitName;
	}

	@Override
	public void setPersistenceUnitName(String pUnitName) {
		this.pUnitName = pUnitName;
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
		setLocales(this.odataContext.getAcceptableLanguages());
		
	}

	@Override
	public ODataContext getODataContext() {
		return this.odataContext;
	}

	@Override
	public void setODataProcessor(ODataProcessor processor) {
		this.processor = processor;
	}

	@Override
	public ODataProcessor getODataProcessor() {
		return this.processor;
	}

	@Override
	public void setEdmProvider(EdmProvider edmProvider) {
		this.edmProvider = edmProvider;
	}

	@Override
	public EdmProvider getEdmProvider() {
		return this.edmProvider;
	}

	@Override
	public void setJPAEdmNameMappingModel(String name) {
		jpaEdmMappingModelName = name;
		
	}

	@Override
	public String getJPAEdmNameMappingModel() {
		return jpaEdmMappingModelName;
	}
	
	public static void setLocales(List<Locale> acceptedLocales) {
		oDataContextThreadLocal.set(acceptedLocales);
	}

	public static void unsetLocales() {
		oDataContextThreadLocal.remove();
	}

	public static List<Locale> getLocales() {
		return oDataContextThreadLocal.get();
	}


}
