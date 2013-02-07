package com.sap.core.odata.processor.core.jpa.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.metamodel.Metamodel;

import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmBaseViewImpl;
import com.sap.core.odata.processor.core.jpa.model.mock.JPAMetaModelMock;
import com.sap.core.odata.processor.core.jpa.testdata.JPAEdmTestModelView;

public class JPAEdmBaseViewImplTest extends JPAEdmTestModelView{
	
	private JPAEdmBaseViewImplTest objJPAEdmBaseViewImplTest;
	private JPAEdmBaseViewImpl objJPAEdmBaseViewImpl;

	@Before
	public void setUp() throws Exception {
		objJPAEdmBaseViewImplTest = new JPAEdmBaseViewImplTest();
		objJPAEdmBaseViewImpl = new JPAEdmBaseViewImpl(objJPAEdmBaseViewImplTest) {
			
			@Override
			public JPAEdmBuilder getBuilder() {
				return null;
			}
		};
		
		objJPAEdmBaseViewImpl = new JPAEdmBaseViewImpl(getJPAMetaModel(),getpUnitName()) {
			
			@Override
			public JPAEdmBuilder getBuilder() {
				return null;
			}
		};
		
		/*objJPAEdmBaseViewImpl = new JPAEdmBaseViewImpl(this) {
			
			@Override
			public JPAEdmBuilder getBuilder() {
				return null;
			}
		};*/
	}

	@Test
	public void testGetpUnitName() {
		assertTrue(objJPAEdmBaseViewImpl.getpUnitName().equals("salesorderprocessing"));
	}

	@Test
	public void testGetJPAMetaModel() {
		assertNotNull(objJPAEdmBaseViewImpl.getJPAMetaModel());
	}

	@Test
	public void testIsConsistent() {
		assertTrue(objJPAEdmBaseViewImpl.isConsistent());
	}

	@Test
	public void testClean() {
		objJPAEdmBaseViewImpl.clean();
		assertFalse(objJPAEdmBaseViewImpl.isConsistent());
	}
	
	@Override
	public String getpUnitName(){
		return "salesorderprocessing";
	}

	@Override
	public Metamodel getJPAMetaModel() {
		return new JPAMetaModelMock();
	}
	

}
