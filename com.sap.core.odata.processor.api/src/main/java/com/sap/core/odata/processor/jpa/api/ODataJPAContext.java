package com.sap.core.odata.processor.jpa.api;

import javax.persistence.EntityManagerFactory;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataProcessor;

/**
 * Compilation of context objects required for OData JPA Runtime
 * 
 * @author SAP AG <br>
 * @DoNotImplement
 * 
 */
public interface ODataJPAContext {

	/**
	 * 
	 * @return Java Persistence Unit Name set into the context
	 */
	public String getPersistenceUnitName();

	/**
	 * 
	 * @param pUnitName
	 *            is the Java Persistence Unit Name.
	 * 
	 */
	public void setPersistenceUnitName(String pUnitName);

	/**
	 * 
	 * @param processor
	 *            is the specific implementation of {@link ODataJPAProcessor}
	 *            for processing OData service requests.
	 */
	public void setODataProcessor(ODataProcessor processor);

	/**
	 * 
	 * @return OData JPA Processor for processing OData service requests set
	 *         into the context
	 */
	public ODataProcessor getODataProcessor();
	
	/**
	 * 
	 * @param edmProvider is the 
	 * 
	 */
	public void setEdmProvider(EdmProvider edmProvider);

	public EdmProvider getEdmProvider();

	public EntityManagerFactory getEntityManagerFactory();

	public void setEntityManagerFactory(EntityManagerFactory emf);

	public void setODataContext(ODataContext ctx);

	public ODataContext getODataContext();

}
