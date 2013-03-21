package com.sap.core.odata.processor.core.jpa.mock;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Metamodel;

import org.easymock.EasyMock;

import com.sap.core.odata.processor.api.jpa.ODataJPAContext;

public abstract class ODataJPAContextMock {
	
	public static final String NAMESPACE = "salesorderprocessing";
	public static final String MAPPING_MODEL = "SalesOrderProcessingMappingModel";
	public static final String PERSISTENCE_UNIT_NAME = "salesorderprocessing";
	
	public static ODataJPAContext mockODataJPAContext()
	{
		ODataJPAContext odataJPAContext = EasyMock.createMock(ODataJPAContext.class);
		EasyMock.expect(odataJPAContext.getPersistenceUnitName()).andStubReturn(NAMESPACE);
		EasyMock.expect(odataJPAContext.getEntityManagerFactory()).andReturn(mockEntityManagerFactory());
		EasyMock.expect(odataJPAContext.getJPAEdmMappingModel()).andReturn(MAPPING_MODEL);
		EasyMock.expect(odataJPAContext.getJPAEdmExtension()).andReturn(null);
		EasyMock.replay(odataJPAContext);
		return odataJPAContext;
	}
	private static EntityManagerFactory mockEntityManagerFactory() {
		EntityManagerFactory emf = EasyMock.createMock(EntityManagerFactory.class);
		EasyMock.expect(emf.getMetamodel()).andReturn(mockMetaModel());
		EasyMock.replay(emf);
		return emf;
	}
	private static Metamodel mockMetaModel() {
		Metamodel metaModel = EasyMock.createMock(Metamodel.class);
		EasyMock.replay(metaModel);
		return metaModel;
	}
	

}
