package com.sap.core.odata.processor.jpa.access;

import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import static org.junit.Assert.*;
import org.junit.*;

import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.processor.jpa.access.model.JPAEdmBuilderV2;
import com.sap.core.odata.processor.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.jpa.util.MockData;

public class JPAEdmBuilderV2Test {
	
	
	private static final String PUNIT_NAME = "salesorderprocessing";
	private static JPAEdmBuilderV2 builder;

	@BeforeClass
	  public static void setup() throws Exception {
		EntityManagerFactory emf=null;
		try
		{
			emf = Persistence.createEntityManagerFactory(PUNIT_NAME);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(emf != null)
		{
			builder = new JPAEdmBuilderV2(PUNIT_NAME, emf);
		}
		
		
	  }

	@Test
	public void testGetSchemas() {
		List<Schema> schemas = null;
		try {
			schemas = builder.getSchemas();
		} catch (ODataJPAModelException e) {
			fail("JPA Model exception raised with message "+e.getMessage());
		}
		assertEquals(schemas.size(), 1);
		for(Schema schema:schemas)
		{
			assertEquals(schema.getNamespace(),MockData.NAME_SPACE);
		}
		
	}

}
