package com.sap.core.odata.processor.core.jpa;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManagerFactory;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.core.jpa.edm.ODataJPAEdmProvider;
import com.sap.core.odata.processor.core.jpa.mock.ODataJPAContextMock;

public class ODataJPAContextImplTest {

	private ODataContext odataContext = null;
	private ODataJPAContext odataJPAContext = null;
	private EdmProvider edmProvider = null;
	private EntityManagerFactory emf = null;
	private ODataProcessor processor = null;

	@Before
	public void setup() {

		edmProvider = new ODataJPAEdmProvider();
		emf = EasyMock.createMock(EntityManagerFactory.class);
		EasyMock.replay(emf);

		odataContext = EasyMock.createMock(ODataContext.class);
		EasyMock.expect(odataContext.getAcceptableLanguages()).andStubReturn(null);
		EasyMock.replay(odataContext);

		processor = EasyMock.createMock(ODataProcessor.class);
		EasyMock.replay(processor);

		odataJPAContext = new ODataJPAContextImpl();
		odataJPAContext.setEdmProvider(edmProvider);
		odataJPAContext.setEntityManagerFactory(emf);
		odataJPAContext.setODataContext(odataContext);
		odataJPAContext.setODataProcessor(processor);
		odataJPAContext
				.setPersistenceUnitName(ODataJPAContextMock.PERSISTENCE_UNIT_NAME);
		odataJPAContext.setJPAEdmNameMappingModel(ODataJPAContextMock.MAPPING_MODEL);
	}

	@Test
	public void testgetMethodsOfODataJPAContext() {

		assertEquals(odataJPAContext.getEdmProvider().hashCode(), edmProvider.hashCode());
		assertEquals(odataJPAContext.getEntityManagerFactory().hashCode(), emf.hashCode());
		assertEquals(odataJPAContext.getODataContext().hashCode(), odataContext.hashCode());
		assertEquals(odataJPAContext.getODataProcessor().hashCode(), processor.hashCode());
		assertEquals(odataJPAContext.getPersistenceUnitName(),
				ODataJPAContextMock.PERSISTENCE_UNIT_NAME);
		assertEquals(odataJPAContext.getJPAEdmNameMappingModel(), ODataJPAContextMock.MAPPING_MODEL);

	}

}
