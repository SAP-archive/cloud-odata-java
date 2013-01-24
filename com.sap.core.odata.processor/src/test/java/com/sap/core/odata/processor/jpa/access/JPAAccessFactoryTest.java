package com.sap.core.odata.processor.jpa.access;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.processor.jpa.access.model.JPAEdmBuilderV2;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.api.access.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.factory.ODataJPAFactoryImpl;
import com.sap.core.odata.processor.jpa.util.MockData;
import com.sap.core.odata.processor.jpa.util.MockEmf;

public class JPAAccessFactoryTest {

	private ODataJPAContext odataJpaContext;
	@Before
	public void setUp() throws Exception {
		
		odataJpaContext = EasyMock.createMock(ODataJPAContext.class);
		EasyMock.expect(odataJpaContext.getEntityManagerFactory()).andReturn(MockEmf.mockEntityManagerFactory());
		EasyMock.expect(odataJpaContext.getPersistenceUnitName()).andReturn(MockData.PERSISTENVE_UNIT_NAME);
		EasyMock.replay(odataJpaContext);
	}

	@Test
	public void testGetJPAEdmBuilder() {
		
		ODataJPAFactoryImpl factory = new ODataJPAFactoryImpl();
		//JPAEdmBuilder builder = factory.getJPAAccessFactory().getJPAEdmBuilder(odataJpaContext);
		//assertEquals(JPAEdmBuilderV2.class, builder.getClass());
	}

}
