package com.sap.core.odata.processor.core.jpa.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Metamodel;

import org.easymock.EasyMock;

import com.sap.core.odata.processor.api.jpa.ODataJPAContext;

public class MockODataJPAContext {
	
	private static final String NAMESPACE = "salesorderprocessing";  
	public static ODataJPAContext mockODataJPAContext()
	{
		ODataJPAContext odataJPAContext = EasyMock.createMock(ODataJPAContext.class);
		EasyMock.expect(odataJPAContext.getPersistenceUnitName()).andReturn(NAMESPACE).times(2);
		EasyMock.expect(odataJPAContext.getEntityManagerFactory()).andReturn(mockEntityManagerFactory());
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
