package com.sap.core.odata.processor.jpa.access;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.processor.jpa.access.api.JPAEdmBuilder;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.util.MockData;
import com.sap.core.odata.processor.jpa.util.MockEmf;

public class JPAControllerTest {

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
		
		JPAEdmBuilder builder = JPAController.getJPAEdmBuilder(odataJpaContext);
		assertEquals(JPAEdmBuilderV2.class, builder.getClass());
	}

}
