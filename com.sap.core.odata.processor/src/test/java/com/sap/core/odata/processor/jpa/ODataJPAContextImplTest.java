package com.sap.core.odata.processor.jpa;

import static org.junit.Assert.*;

import javax.persistence.EntityManagerFactory;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.processor.jpa.ODataJPAContextImpl;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.edm.ODataJPAEdmProvider;
import com.sap.core.odata.processor.jpa.util.MockData;

public class ODataJPAContextImplTest {

	@Test
	public void test() {
		
		EdmProvider edmProvider = new ODataJPAEdmProvider();
		EntityManagerFactory emf = EasyMock.createMock(EntityManagerFactory.class);
		EasyMock.replay(emf);
		ODataContext odataContext = EasyMock.createMock(ODataContext.class);
		EasyMock.replay(odataContext);
		ODataProcessor processor = EasyMock.createMock(ODataProcessor.class);
		EasyMock.replay(processor);
		ODataJPAContextImpl odataJPAContextImpl = new ODataJPAContextImpl();
		odataJPAContextImpl.setEdmProvider(edmProvider);
		odataJPAContextImpl.setEntityManagerFactory(emf);
		odataJPAContextImpl.setODataContext(odataContext);
		odataJPAContextImpl.setODataProcessor(processor);
		odataJPAContextImpl.setPersistenceUnitName(MockData.PERSISTENVE_UNIT_NAME);
		assertEquals(odataJPAContextImpl.getEdmProvider(), edmProvider);
		assertEquals(odataJPAContextImpl.getEntityManagerFactory(), emf);
		assertEquals(odataJPAContextImpl.getODataContext(), odataContext);
		assertEquals(odataJPAContextImpl.getODataProcessor(), processor);
		assertEquals(odataJPAContextImpl.getPersistenceUnitName(), MockData.PERSISTENVE_UNIT_NAME);
	}

}
