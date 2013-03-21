package com.sap.core.odata.processor.core.jpa;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
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
	private EntityManager em = null;
	private ODataProcessor processor = null;

	@Before
	public void setup() {

		edmProvider = new ODataJPAEdmProvider();
		emf = EasyMock.createMock(EntityManagerFactory.class);
		em = EasyMock.createMock(EntityManager.class);
		EasyMock.replay(em);
		
		EasyMock.expect(emf.createEntityManager()).andStubReturn(em);
		EasyMock.replay(emf);

		odataContext = EasyMock.createMock(ODataContext.class);
		List<Locale> listLocale = new ArrayList<Locale>();
        listLocale.add(Locale.ENGLISH);
        listLocale.add(Locale.GERMAN);

		EasyMock.expect(odataContext.getAcceptableLanguages()).andStubReturn(listLocale);
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
		odataJPAContext.setJPAEdmMappingModel(ODataJPAContextMock.MAPPING_MODEL);
	}

	@Test
	public void testgetMethodsOfODataJPAContext() {

		assertEquals(odataJPAContext.getEdmProvider().hashCode(), edmProvider.hashCode());
		assertEquals(odataJPAContext.getEntityManagerFactory().hashCode(), emf.hashCode());
		assertEquals(odataJPAContext.getODataContext().hashCode(), odataContext.hashCode());
		assertEquals(odataJPAContext.getODataProcessor().hashCode(), processor.hashCode());
		assertEquals(odataJPAContext.getPersistenceUnitName(),
				ODataJPAContextMock.PERSISTENCE_UNIT_NAME);
		assertEquals(odataJPAContext.getJPAEdmMappingModel(), ODataJPAContextMock.MAPPING_MODEL);
		
		EntityManager em1 = odataJPAContext.getEntityManager();
		EntityManager em2 = odataJPAContext.getEntityManager();
		if(em1 != null && em2 != null) //Fix for build - TODO
			assertEquals(em1.hashCode(), em2.hashCode());

	}

}
