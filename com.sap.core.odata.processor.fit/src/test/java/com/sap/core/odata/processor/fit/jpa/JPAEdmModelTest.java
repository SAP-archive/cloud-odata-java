package com.sap.core.odata.processor.fit.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmModel;

public class JPAEdmModelTest {
	
	private static JPAEdmModel jpaEdmModel;
	private static MockModel model;
	
	@BeforeClass
	  public static void setup() throws Exception {
		 
		model = new MockModel();
		jpaEdmModel = new JPAEdmModel(model.getMetaModel(), model.getPersistentUnitName());
		
	}

	@Test
	public void testGetSchemaView() throws ODataJPAModelException, ODataJPARuntimeException {
		jpaEdmModel.getBuilder().build();
		JPAEdmSchemaView schemaView = jpaEdmModel.getEdmSchemaView();
		Schema schema = schemaView.getEdmSchema();
		assertEquals(schema.getNamespace(), model.getPersistentUnitName());
		
	}

	

}
