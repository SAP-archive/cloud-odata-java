package com.sap.core.odata.processor.core.jpa.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmAssociationView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityContainerView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntitySetView;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;

public class JPAEdmAssociationSetTest extends JPAEdmTestModelView {
	
	private JPAEdmAssociationSetTest objJPAEdmAssociationSetTest;
	private JPAEdmAssociationSet objJPAEdmAssociationSet;
	
	
	@Before
	public void setUp() {
		objJPAEdmAssociationSetTest = new JPAEdmAssociationSetTest();
		objJPAEdmAssociationSet = new JPAEdmAssociationSet(objJPAEdmAssociationSetTest);
		try {
			objJPAEdmAssociationSet.getBuilder().build();
		} catch (ODataJPAModelException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		} catch (ODataJPARuntimeException e) {
			fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1+e.getMessage()+ ODataJPATestConstants.EXCEPTION_MSG_PART_2);
		}
		
	}

	@Test
	public void testGetBuilder() {
		assertNotNull(objJPAEdmAssociationSet.getBuilder());
	}

	@Test
	public void testGetConsistentEdmAssociationSetList() {
		assertNotNull(objJPAEdmAssociationSet.getConsistentEdmAssociationSetList());
	}

	@Test
	public void testGetEdmAssociationSet() {
		assertNotNull(objJPAEdmAssociationSet.getEdmAssociationSet());
	}

	@Test
	public void testGetEdmAssociation() {
		assertNotNull(objJPAEdmAssociationSet.getEdmAssociation());
	}

	@Test
	public void testIsConsistent() {
		assertTrue(objJPAEdmAssociationSet.isConsistent());
	}
	
	@Test
	public void testGetBuilderIdempotent(){
		JPAEdmBuilder builder1 = objJPAEdmAssociationSet.getBuilder();
		JPAEdmBuilder builder2 = objJPAEdmAssociationSet.getBuilder();
		
		assertEquals(builder1.hashCode(), builder2.hashCode());
	}

	@Override
	public JPAEdmEntityContainerView getJPAEdmEntityContainerView() {
		return this;
	}

	@Override
	public JPAEdmEntitySetView getJPAEdmEntitySetView() {
		return this;
	}

	@Override
	public JPAEdmAssociationView getJPAEdmAssociationView() {
		return this;
	}

	@Override
	public AssociationSet getEdmAssociationSet() {
		AssociationSet associationSet = new AssociationSet();
		associationSet.setEnd1(new AssociationSetEnd());
		associationSet.setEnd2(new AssociationSetEnd());
		
		return associationSet;
	}

	@Override
	public List<Association> getConsistentEdmAssociationList() {
		return getEdmAssociationListLocal();
	}
	
	
	@Override
	public List<AssociationSet> getConsistentEdmAssociationSetList() {
		
		List<AssociationSet> associationSetList = new ArrayList<AssociationSet>();
		associationSetList.add(getEdmAssociationSet());
		associationSetList.add(getEdmAssociationSet());
		
		return associationSetList;
	}

	@Override
	public List<EntitySet> getConsistentEdmEntitySetList() {
		return getEntitySetListLocal();
	}
	
	@Override
	public boolean isConsistent() {
		return true;
	}
	
	@Override
	public Schema getEdmSchema() {
		Schema schema = new Schema();
		schema.setNamespace("salesordereprocessing");
		return schema;
	}

	private List<EntitySet> getEntitySetListLocal() {
		List<EntitySet> entitySetList = new ArrayList<EntitySet>();
		
		EntitySet entitySet = new EntitySet();
		entitySet.setName("SalesOrderHeader");
		entitySet.setEntityType(new FullQualifiedName("salesorderprocessing", "SOID"));
		
		EntitySet entitySet2 = new EntitySet();
		entitySet2.setName("SalesOrderItem");
		entitySet2.setEntityType(new FullQualifiedName("salesorderprocessing", "SOID"));
		
		entitySetList.add(entitySet);
		entitySetList.add(entitySet2);
		return entitySetList;
	}

	private List<Association> getEdmAssociationListLocal() {
		List<Association> associationList = new ArrayList<Association>();
		
		Association association = new Association();
		association.setName("Assoc_SalesOrderHeader_SalesOrderItem");
		association.setEnd1(new AssociationEnd().setType(new FullQualifiedName("salesorderprocessing", "String")).setRole("SalesOrderHeader"));
		association.setEnd2(new AssociationEnd().setType(new FullQualifiedName("salesorderprocessing", "SalesOrderItem")).setRole("SalesOrderItem"));
		
		associationList.add(association);
		return associationList;
	}
	
	
	

}
