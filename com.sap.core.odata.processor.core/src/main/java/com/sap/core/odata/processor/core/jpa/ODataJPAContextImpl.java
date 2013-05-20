package com.sap.core.odata.processor.core.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmExtension;

public class ODataJPAContextImpl implements ODataJPAContext {

	private String pUnitName;
	private EntityManagerFactory emf;
	private EntityManager em;
	private ODataContext odataContext;
	private ODataProcessor processor;
	private EdmProvider edmProvider;
	private String jpaEdmMappingModelName;
	private JPAEdmExtension jpaEdmExtension;
	private static final ThreadLocal<ODataContext> oDataContextThreadLocal = new ThreadLocal<ODataContext>();

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
		setContextInThreadLocal(this.odataContext);
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
	public void setJPAEdmMappingModel(String name) {
		jpaEdmMappingModelName = name;

	}

	@Override
	public String getJPAEdmMappingModel() {
		return jpaEdmMappingModelName;
	}

	public static void setContextInThreadLocal(ODataContext ctx) {
		oDataContextThreadLocal.set(ctx);
	}

	public static void unsetContextInThreadLocal() {
		oDataContextThreadLocal.remove();
	}

	public static ODataContext getContextInThreadLocal() {
		return (ODataContext) oDataContextThreadLocal.get();
	}

	@Override
	public EntityManager getEntityManager() {
		if (em == null)
			em = emf.createEntityManager();
		em.setFlushMode(FlushModeType.COMMIT);
		return em;
	}

	@Override
	public void setJPAEdmExtension(JPAEdmExtension jpaEdmExtension) {
		this.jpaEdmExtension = jpaEdmExtension;
		
	}

	@Override
	public JPAEdmExtension getJPAEdmExtension() {
		return jpaEdmExtension;
	}
}
