package com.sap.core.odata.processor.core.jpa.edm;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;
import com.sap.core.odata.processor.core.jpa.mock.ODataJPAContextMock;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmModel;

public class ODataJPAEdmProviderNegativeTest {

  private static ODataJPAEdmProvider edmProvider;

  @BeforeClass
  public static void setup() {

    edmProvider = new ODataJPAEdmProvider();
    try {
      Class<? extends ODataJPAEdmProvider> clazz = edmProvider.getClass();
      Field field = clazz.getDeclaredField("schemas");
      field.setAccessible(true);
      List<Schema> schemas = new ArrayList<Schema>();
      schemas.add(new Schema().setNamespace("salesorderprocessing")); //Empty Schema
      field.set(edmProvider, schemas);
      field = clazz.getDeclaredField("oDataJPAContext");
      field.setAccessible(true);
      field.set(edmProvider, ODataJPAContextMock.mockODataJPAContext());
      field = clazz.getDeclaredField("jpaEdmModel");
      field.setAccessible(true);
      field.set(edmProvider, new JPAEdmModel(null, null));
    } catch (IllegalArgumentException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (IllegalAccessException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (NoSuchFieldException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (SecurityException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }

  }

  @Test
  public void testNullGetEntityContainerInfo() {
    EntityContainerInfo entityContainer = null;
    try {
      entityContainer = edmProvider
          .getEntityContainerInfo("salesorderprocessingContainer");
    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    assertNull(entityContainer);
  }

  @Test
  public void testNullGetEntityType() {
    FullQualifiedName entityTypeName = new FullQualifiedName(
        "salesorderprocessing", "SalesOrderHeader");
    try {
      assertNull(edmProvider.getEntityType(entityTypeName));
    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
  }

  @Test
  public void testNullGetComplexType() {
    FullQualifiedName complexTypeName = new FullQualifiedName(
        "salesorderprocessing", "Address");
    try {
      assertNull(edmProvider.getComplexType(complexTypeName));
    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
  }

  @Test
  public void testGetAssociationFullQualifiedName() {
    Association association = null;
    try {
      association = edmProvider.getAssociation(new FullQualifiedName(
          "salesorderprocessing", "SalesOrderHeader_SalesOrderItem"));
    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    assertNull(association);
  }

  @Test
  public void testGetEntitySet() {
    try {
      assertNull(edmProvider.getEntitySet(
          "salesorderprocessingContainer", "SalesOrderHeaders"));
    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
  }

  @Test
  public void testGetAssociationSet() {
    try {
      assertNull(edmProvider.getAssociationSet(
          "salesorderprocessingContainer", new FullQualifiedName(
              "salesorderprocessing",
              "SalesOrderHeader_SalesOrderItem"),
          "SalesOrderHeaders", "SalesOrderHeader"));
    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }

  }

  @Test
  public void testNullGetFunctionImport() {

    try {
      assertNull(edmProvider.getFunctionImport(
          "salesorderprocessingContainer",
          "SalesOrder_FunctionImport1"));
    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }

  }

  @Test
  public void testNullGetFunctionImport2() {

    try {
      ODataJPAEdmProvider provider = new ODataJPAEdmProvider();
      try {
        Class<? extends ODataJPAEdmProvider> clazz = provider.getClass();
        Field field = clazz.getDeclaredField("schemas");
        field.setAccessible(true);
        List<Schema> schemas = new ArrayList<Schema>();
        Schema schema = new Schema().setNamespace("salesorderprocessing");
        EntityContainer container = new EntityContainer().setName("salesorderprocessingContainer");
        List<EntityContainer> containerList = new ArrayList<EntityContainer>();
        containerList.add(container); // Empty Container
        schema.setEntityContainers(containerList);
        schemas.add(schema); //Empty Schema
        field.set(provider, schemas);
      } catch (IllegalArgumentException e) {
        fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
            + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
      } catch (IllegalAccessException e) {
        fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
            + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
      } catch (NoSuchFieldException e) {
        fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
            + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
      } catch (SecurityException e) {
        fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
            + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
      }

      assertNull(provider.getFunctionImport(
          "salesorderprocessingContainer",
          "SalesOrder_FunctionImport1"));
    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }

  }

  @Test
  public void testGetSchemas() {
    try {
      assertNotNull(edmProvider.getSchemas());
    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
  }

}
