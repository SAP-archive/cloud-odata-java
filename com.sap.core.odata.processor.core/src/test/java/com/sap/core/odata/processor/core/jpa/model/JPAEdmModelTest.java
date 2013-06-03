package com.sap.core.odata.processor.core.jpa.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.Metamodel;

import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;
import com.sap.core.odata.processor.core.jpa.mock.model.JPAEmbeddableMock;
import com.sap.core.odata.processor.core.jpa.mock.model.JPAMetaModelMock;
import com.sap.core.odata.processor.core.jpa.mock.model.JPASingularAttributeMock;

public class JPAEdmModelTest extends JPAEdmTestModelView {

  private JPAEdmModel objJPAEdmModel;

  @Before
  public void setUp() {
    objJPAEdmModel = new JPAEdmModel(getJPAMetaModel(), "salesorderprocessing");
    try {
      objJPAEdmModel.getBuilder().build();
    } catch (ODataJPAModelException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (ODataJPARuntimeException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage() + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
  }

  @Test
  public void testGetEdmSchemaView() {
    assertNotNull(objJPAEdmModel.getEdmSchemaView());
  }

  @Test
  public void testGetBuilder() {
    assertNotNull(objJPAEdmModel.getBuilder());
  }

  @Override
  public Metamodel getJPAMetaModel() {
    return new JPAEdmMetaModel();
  }

  private class JPAEdmMetaModel extends JPAMetaModelMock
  {
    Set<EmbeddableType<?>> embeddableSet;

    public JPAEdmMetaModel() {
      embeddableSet = new HashSet<EmbeddableType<?>>();
    }

    @Override
    public Set<EmbeddableType<?>> getEmbeddables() {
      embeddableSet.add(new JPAEdmEmbeddable<String>());
      return embeddableSet;
    }

  }

  @SuppressWarnings("hiding")
  private class JPAEdmEmbeddable<String> extends JPAEmbeddableMock<String>
  {

    Set<Attribute<? super String, ?>> attributeSet = new HashSet<Attribute<? super String, ?>>();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void setValuesToSet()
    {
      attributeSet.add((Attribute<? super String, String>) new JPAEdmAttribute(java.lang.String.class, "SOID"));
      attributeSet.add((Attribute<? super String, String>) new JPAEdmAttribute(java.lang.String.class, "SONAME"));
    }

    @Override
    public Set<Attribute<? super String, ?>> getAttributes() {
      setValuesToSet();
      return attributeSet;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<String> getJavaType() {
      return (Class<String>) java.lang.String.class;
    }

  }

  @SuppressWarnings("hiding")
  private class JPAEdmAttribute<Object, String> extends JPASingularAttributeMock<Object, String>
  {

    @Override
    public PersistentAttributeType getPersistentAttributeType() {
      return PersistentAttributeType.BASIC;
    }

    Class<String> clazz;
    java.lang.String attributeName;

    public JPAEdmAttribute(final Class<String> javaType, final java.lang.String name) {
      this.clazz = javaType;
      this.attributeName = name;

    }

    @Override
    public Class<String> getJavaType() {
      return clazz;
    }

    @Override
    public java.lang.String getName() {
      return this.attributeName;
    }

    @Override
    public boolean isId() {
      return false;
    }

  }

}
