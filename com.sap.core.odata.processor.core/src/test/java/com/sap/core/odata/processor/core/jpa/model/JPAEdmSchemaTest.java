package com.sap.core.odata.processor.core.jpa.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.metamodel.Metamodel;

import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.core.jpa.mock.model.JPAMetaModelMock;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmSchema;

public class JPAEdmSchemaTest extends JPAEdmTestModelView {
	private JPAEdmSchemaTest objJPAEdmSchemaTest;
	private JPAEdmSchema objJPAEdmSchema;

	@Before
	public void setUp() throws Exception {
		objJPAEdmSchemaTest = new JPAEdmSchemaTest();
		objJPAEdmSchema = new JPAEdmSchema(objJPAEdmSchemaTest);
		//objJPAEdmSchema.getBuilder().build(); //building schema is not required as downstream structure already tested
	
	}

	@Test
	public void testClean() {
		assertTrue(objJPAEdmSchema.isConsistent());
		objJPAEdmSchema.clean();
		assertFalse(objJPAEdmSchema.isConsistent());
	}

	@Test
	public void testGetEdmSchema() {
		assertNull(objJPAEdmSchema.getEdmSchema());
	}

	@Test
	public void testGetJPAEdmEntityContainerView() {
		assertNull(objJPAEdmSchema.getJPAEdmEntityContainerView());
	}

	@Test
	public void testGetJPAEdmComplexTypeView() {
		assertNull(objJPAEdmSchema.getJPAEdmComplexTypeView());
	}

	@Test
	public void testGetBuilder() {
		assertNotNull(objJPAEdmSchema.getBuilder());
	}
	
	@Test
	public void testGetBuilderIdempotent(){
		JPAEdmBuilder builder1 = objJPAEdmSchema.getBuilder();
		JPAEdmBuilder builder2 = objJPAEdmSchema.getBuilder();
		
		assertEquals(builder1.hashCode(), builder2.hashCode());
	}

	@Test
	public void testGetJPAEdmAssociationView() {
		assertNull(objJPAEdmSchema.getJPAEdmAssociationView());
	}

	@Test
	public void testIsConsistent() {
		assertTrue(objJPAEdmSchema.isConsistent());
		objJPAEdmSchema.clean();
		assertFalse(objJPAEdmSchema.isConsistent());
	}
	
	@Override
	public Metamodel getJPAMetaModel() {
		return new JPAMetaModelMock();
	}
	
	@Override
	public String getpUnitName() {
		return "salesorderprocessing";
	}

}
