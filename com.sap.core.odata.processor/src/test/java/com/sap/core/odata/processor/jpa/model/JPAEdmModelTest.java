package com.sap.core.odata.processor.jpa.model;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.processor.jpa.api.model.JPAEdmSchemaView;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;

public class JPAEdmModelTest {
	
	private static JPAEdmModel jpaEdmModel;
	private static MockModel model;
	
	@BeforeClass
	  public static void setup() throws Exception {
		 
		model = new MockModel();
		jpaEdmModel = new JPAEdmModel(model.getMetaModel(), model.getPersistentUnitName());
		
	}

	@Test
	public void testGetSchemaView() throws ODataJPAModelException {
		jpaEdmModel.getBuilder().build();
		JPAEdmSchemaView schemaView = jpaEdmModel.getSchemaView();
		Schema schema = schemaView.getEdmSchema();
		assertEquals(schema.getNamespace(), model.getPersistentUnitName());
		
	}

	

}
