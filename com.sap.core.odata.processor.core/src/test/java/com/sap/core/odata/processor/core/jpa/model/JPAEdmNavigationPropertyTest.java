package com.sap.core.odata.processor.core.jpa.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Type;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmBuilder;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;
import com.sap.core.odata.processor.core.jpa.mock.model.JPAEntityTypeMock;
import com.sap.core.odata.processor.core.jpa.mock.model.JPAPluralAttributeMock;

public class JPAEdmNavigationPropertyTest extends JPAEdmTestModelView {

  private static JPAEdmNavigationProperty objNavigationProperty;
  private static JPAEdmNavigationPropertyTest navPropView;

  @BeforeClass
  public static void setup() {
    JPAEdmNavigationPropertyTest localView = new JPAEdmNavigationPropertyTest();
    navPropView = new JPAEdmNavigationPropertyTest();
    objNavigationProperty = new JPAEdmNavigationProperty(localView,
        localView, 1);
    try {
      objNavigationProperty.getBuilder().build();
    } catch (ODataJPAModelException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (ODataJPARuntimeException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
  }

  @Override
  public String getpUnitName() {
    return "salesorderprocessing";
  }

  @Override
  public JPAEdmEntityTypeView getJPAEdmEntityTypeView() {
    return this;
  }

  @Override
  public EntityType<?> getJPAEntityType() {
    return new JPAEdmEntityType();
  }

  private Attribute<?, ?> getJPAAttributeLocal() {
    AttributeMock<Object, String> attr = new AttributeMock<Object, String>();
    return attr;
  }

  @Override
  public Attribute<?, ?> getJPAAttribute() {
    return getJPAAttributeLocal();
  }

  @Override
  public Association getEdmAssociation() {

    Association association = new Association();
    association.setName("Assoc_SalesOrderHeader_SalesOrderItem");
    association.setEnd1(new AssociationEnd().setType(
        new FullQualifiedName("salesorderprocessing", "String"))
        .setRole("SalesOrderHeader"));
    association.setEnd2(new AssociationEnd()
        .setType(
            new FullQualifiedName("salesorderprocessing",
                "SalesOrderItem")).setRole("SalesOrderItem"));
    return association;
  }

  @Test
  public void testGetBuilder() {
    assertNotNull(objNavigationProperty.getBuilder());

  }

  @Test
  public void testGetBuilderIdempotent() {
    JPAEdmBuilder builder1 = objNavigationProperty.getBuilder();
    JPAEdmBuilder builder2 = objNavigationProperty.getBuilder();

    assertEquals(builder1.hashCode(), builder2.hashCode());
  }

  @Test
  public void testGetEdmNavigationProperty() {
    if (objNavigationProperty == null || objNavigationProperty.getEdmNavigationProperty() == null) {
      JPAEdmNavigationPropertyTest localView = new JPAEdmNavigationPropertyTest();
      navPropView = new JPAEdmNavigationPropertyTest();
      objNavigationProperty = new JPAEdmNavigationProperty(localView,
          localView, 1);
      try {
        objNavigationProperty.getBuilder().build();
      } catch (ODataJPAModelException e) {
        fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
      } catch (ODataJPARuntimeException e) {
        fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
      }
    }
    assertEquals(
        objNavigationProperty.getEdmNavigationProperty().getName(),
        "StringDetails");
  }

  @Test
  public void testGetConsistentEdmNavigationProperties() {
    assertTrue(objNavigationProperty.getConsistentEdmNavigationProperties()
        .size() > 0);
  }

  @Test
  public void testAddJPAEdmNavigationPropertyView() {
    JPAEdmNavigationPropertyTest localView = new JPAEdmNavigationPropertyTest();
    navPropView = new JPAEdmNavigationPropertyTest();
    objNavigationProperty = new JPAEdmNavigationProperty(localView,
        localView, 1);
    try {
      objNavigationProperty.getBuilder().build();
    } catch (ODataJPAModelException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (ODataJPARuntimeException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    objNavigationProperty.addJPAEdmNavigationPropertyView(navPropView);
    assertTrue(objNavigationProperty.getConsistentEdmNavigationProperties()
        .size() > 1);
  }

  @Override
  public boolean isConsistent() {
    return true;
  }

  @Test
  public void testBuildNavigationProperty() {

    try {
      objNavigationProperty.getBuilder().build();
    } catch (ODataJPARuntimeException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (ODataJPAModelException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    assertEquals(objNavigationProperty.getEdmNavigationProperty()
        .getFromRole(), "SalesOrderItem");
    assertEquals(objNavigationProperty.getEdmNavigationProperty()
        .getToRole(), "SalesOrderHeader");

  }

  @SuppressWarnings("hiding")
  private class AttributeMock<Object, String> extends
      JPAPluralAttributeMock {

    @Override
    public boolean isCollection() {
      return true;
    }

    @Override
    public Type<java.lang.String> getElementType() {
      return new ElementType();
    }
  }

  private class JPAEdmEntityType extends JPAEntityTypeMock<String> {
    @Override
    public String getName() {
      return "SalesOrderHeader";
    }
  }

  private class ElementType implements Type<String> {

    @Override
    public javax.persistence.metamodel.Type.PersistenceType getPersistenceType() {
      return PersistenceType.BASIC;
    }

    @Override
    public Class<String> getJavaType() {
      return String.class;
    }

  }

}
