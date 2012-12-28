package com.sap.core.odata.processor.jpa.access;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.junit.BeforeClass;
import org.junit.Test;

public class JPAEdmBuilderV2Test {
	
	@PersistenceContext(unitName = "salesorderprocessing")
	private static final String PUNIT_NAME = "salesorderprocessing";
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
			JPAEdmBuilderV2 builder = new JPAEdmBuilderV2(PUNIT_NAME, emf);
		}
		
	  }

	@Test
	public void testGetSchemas() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEntitySet() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEntityTypes() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEntityType() {
		fail("Not yet implemented");
	}

	@Test
	public void testFormKey() {
		fail("Not yet implemented");
	}

}
