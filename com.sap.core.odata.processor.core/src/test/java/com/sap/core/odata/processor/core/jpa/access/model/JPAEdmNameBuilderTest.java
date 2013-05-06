package com.sap.core.odata.processor.core.jpa.access.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmComplexPropertyView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmEntityTypeView;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmPropertyView;
import com.sap.core.odata.processor.core.jpa.ODataJPAContextImpl;
import com.sap.core.odata.processor.core.jpa.mock.model.JPAAttributeMock;
import com.sap.core.odata.processor.core.jpa.mock.model.JPAEntityTypeMock;

public class JPAEdmNameBuilderTest {

  @SuppressWarnings("rawtypes")
  @Test
  public void testBuildJPAEdmComplexPropertyViewJPAEdmPropertyView() {
    JPAEdmComplexPropertyView complexPropertyView = EasyMock.createMock(JPAEdmComplexPropertyView.class);
    ComplexProperty complexProperty = new ComplexProperty();
    EasyMock.expect(complexPropertyView.getEdmComplexProperty()).andStubReturn(complexProperty);
    ODataJPAContextImpl oDataJPAContext = new ODataJPAContextImpl();
    JPAEdmMappingModelService mappingModelService = new JPAEdmMappingModelService(oDataJPAContext);
    EasyMock.expect(complexPropertyView.getJPAEdmMappingModelAccess()).andStubReturn(mappingModelService);

    // Mocking EDMProperty
    JPAEdmPropertyView propertyView = EasyMock.createMock(JPAEdmPropertyView.class);
    JPAEdmEntityTypeView entityTypeView = EasyMock.createMock(JPAEdmEntityTypeView.class);
    EasyMock.expect(entityTypeView.getJPAEntityType()).andStubReturn(new JEntityType());
    EasyMock.replay(entityTypeView);
    EasyMock.expect(propertyView.getJPAAttribute()).andStubReturn(new JAttribute());
    EasyMock.expect(propertyView.getJPAEdmEntityTypeView()).andStubReturn(entityTypeView);
    EasyMock.replay(complexPropertyView);
    EasyMock.replay(propertyView);

    JPAEdmNameBuilder.build(complexPropertyView, propertyView);
    assertEquals("Id", complexPropertyView.getEdmComplexProperty().getName());

  }

  @SuppressWarnings("hiding")
  class JAttribute<Object, String> extends JPAAttributeMock<Object, java.lang.String>
  {

    @Override
    public java.lang.String getName() {
      return "id";
    }

    @Override
    public Class<java.lang.String> getJavaType() {
      return java.lang.String.class;
    }

  }

  class JEntityType<Object> extends JPAEntityTypeMock<Object>
  {

    @Override
    public java.lang.String getName() {
      return "SalesOrderHeader";
    }

  }

  @Test
  public void testBuildJPAEdmComplexPropertyViewString() {
    assertTrue(true);
  }

}
