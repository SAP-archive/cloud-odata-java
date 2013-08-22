package com.sap.core.odata.processor.core.jpa.mock.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;

import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmAssociationEnd;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmMapping;
import com.sap.core.odata.processor.core.jpa.mock.data.JPATypeMock.JPARelatedTypeMock;
import com.sap.core.odata.processor.core.jpa.mock.data.JPATypeMock.JPATypeEmbeddableMock;
import com.sap.core.odata.processor.core.jpa.mock.data.JPATypeMock.JPATypeEmbeddableMock2;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmMappingImpl;

public class EdmMockUtilV2 {

  public static interface JPAEdmMappingMock extends JPAEdmMapping, EdmMapping {

  }

  public static EdmEntityType mockEdmEntityType(String entityName, boolean withComplexType) throws EdmException {

    EdmEntityType entityType = EasyMock.createMock(EdmEntityType.class);
    EasyMock.expect(entityType.getName()).andReturn(entityName).anyTimes();
    EasyMock.expect(entityType.getKeyPropertyNames()).andReturn(mockSimpleKeyPropertyNames(entityName));
    if (withComplexType == false)
      EasyMock.expect(entityType.getPropertyNames()).andReturn(mockPropertyNames(entityName)).anyTimes();
    else
      EasyMock.expect(entityType.getPropertyNames()).andReturn(mockPropertyNamesWithComplexType(entityName)).anyTimes();

    EasyMock.expect(entityType.getNavigationPropertyNames()).andReturn(mockNavigationPropertyNames(entityName));
    EasyMock.expect(entityType.getKind()).andReturn(EdmTypeKind.ENTITY);
    EasyMock.expect(entityType.getMapping()).andReturn((EdmMapping) mockEdmMapping(entityName, null, null));
    if (entityName.equals(JPATypeMock.ENTITY_NAME)) {
      EasyMock.expect(entityType.getProperty(JPATypeMock.PROPERTY_NAME_MINT)).andReturn(mockEdmProperty(entityName, JPATypeMock.PROPERTY_NAME_MINT)).anyTimes();
      EasyMock.expect(entityType.getProperty(JPATypeMock.PROPERTY_NAME_MSTRING)).andReturn(mockEdmProperty(entityName, JPATypeMock.PROPERTY_NAME_MSTRING)).anyTimes();
      EasyMock.expect(entityType.getProperty(JPATypeMock.PROPERTY_NAME_MDATETIME)).andReturn(mockEdmProperty(entityName, JPATypeMock.PROPERTY_NAME_MDATETIME)).anyTimes();
      EasyMock.expect(entityType.getProperty(JPATypeMock.PROPERTY_NAME_MCOMPLEXTYPE)).andReturn(mockEdmProperty(entityName, JPATypeMock.PROPERTY_NAME_MCOMPLEXTYPE)).anyTimes();
      EasyMock.expect(entityType.getProperty(JPATypeMock.NAVIGATION_PROPERTY_X)).andReturn(mockEdmNavigationProperty(JPATypeMock.NAVIGATION_PROPERTY_X, EdmMultiplicity.ONE)).anyTimes();
    }
    else if (entityName.equals(JPARelatedTypeMock.ENTITY_NAME)) {
      EasyMock.expect(entityType.getProperty(JPARelatedTypeMock.PROPERTY_NAME_MLONG)).andReturn(mockEdmProperty(entityName, JPARelatedTypeMock.PROPERTY_NAME_MLONG)).anyTimes();
      EasyMock.expect(entityType.getProperty(JPARelatedTypeMock.PROPERTY_NAME_MBYTE)).andReturn(mockEdmProperty(entityName, JPARelatedTypeMock.PROPERTY_NAME_MBYTE)).anyTimes();
      EasyMock.expect(entityType.getProperty(JPARelatedTypeMock.PROPERTY_NAME_MBYTEARRAY)).andReturn(mockEdmProperty(entityName, JPARelatedTypeMock.PROPERTY_NAME_MBYTEARRAY)).anyTimes();
      EasyMock.expect(entityType.getProperty(JPARelatedTypeMock.PROPERTY_NAME_MDOUBLE)).andReturn(mockEdmProperty(entityName, JPARelatedTypeMock.PROPERTY_NAME_MDOUBLE)).anyTimes();
    }
    EasyMock.replay(entityType);
    return entityType;
  }

  public static List<String> mockNavigationPropertyNames(String entityName) {
    List<String> propertyNames = new ArrayList<String>();
    propertyNames.add(JPATypeMock.NAVIGATION_PROPERTY_X);
    propertyNames.add(JPATypeMock.NAVIGATION_PROPERTY_XS);
    return propertyNames;
  }

  public static List<String> mockSimpleKeyPropertyNames(String entityName) {
    List<String> keyPropertyNames = new ArrayList<String>();
    if (entityName.equals(JPATypeMock.ENTITY_NAME)) {
      keyPropertyNames.add(JPATypeMock.PROPERTY_NAME_MINT);
    }
    else if (entityName.equals(JPARelatedTypeMock.ENTITY_NAME)) {
      keyPropertyNames.add(JPARelatedTypeMock.PROPERTY_NAME_MLONG);
    }

    return keyPropertyNames;
  }

  public static List<String> mockPropertyNames(String entityName) {
    List<String> propertyNames = new ArrayList<String>();

    if (entityName.equals(JPATypeMock.ENTITY_NAME)) {
      propertyNames.add(JPATypeMock.PROPERTY_NAME_MINT);
      propertyNames.add(JPATypeMock.PROPERTY_NAME_MDATETIME);
      propertyNames.add(JPATypeMock.PROPERTY_NAME_MSTRING);
    }
    else if (entityName.equals(JPARelatedTypeMock.ENTITY_NAME)) {
      propertyNames.add(JPARelatedTypeMock.PROPERTY_NAME_MLONG);
      propertyNames.add(JPARelatedTypeMock.PROPERTY_NAME_MBYTE);
      propertyNames.add(JPARelatedTypeMock.PROPERTY_NAME_MBYTEARRAY);
      propertyNames.add(JPARelatedTypeMock.PROPERTY_NAME_MDOUBLE);
    }
    else if (entityName.equals(JPATypeEmbeddableMock.ENTITY_NAME)) {
      propertyNames.add(JPATypeMock.JPATypeEmbeddableMock.PROPERTY_NAME_MSHORT);
      propertyNames.add(JPATypeMock.JPATypeEmbeddableMock.PROPERTY_NAME_MEMBEDDABLE);
    }
    else if (entityName.equals(JPATypeEmbeddableMock2.ENTITY_NAME)) {
      propertyNames.add(JPATypeMock.JPATypeEmbeddableMock2.PROPERTY_NAME_MFLOAT);
      propertyNames.add(JPATypeMock.JPATypeEmbeddableMock2.PROPERTY_NAME_MUUID);
    }

    return propertyNames;
  }

  public static List<String> mockPropertyNamesWithComplexType(String entityName) {
    List<String> propertyNames = mockPropertyNames(entityName);
    propertyNames.add(JPATypeMock.PROPERTY_NAME_MCOMPLEXTYPE);

    return propertyNames;

  }

  public static EdmAssociationEnd mockEdmAssociatioEnd(String navigationPropertyName, String role) throws EdmException {
    EdmAssociationEnd associationEnd = EasyMock.createMock(EdmAssociationEnd.class);
    EasyMock.expect(associationEnd.getMultiplicity()).andReturn(EdmMultiplicity.ONE);
    EdmEntityType entityType = EasyMock.createMock(EdmEntityType.class);
    EasyMock.expect(entityType.getMapping()).andReturn((EdmMapping) mockEdmMapping("JPARelatedTypeMock", null, null));
    EasyMock.replay(entityType);

    EasyMock.expect(associationEnd.getEntityType()).andReturn(entityType);
    EasyMock.replay(associationEnd);
    return associationEnd;
  }

  public static EdmAssociation mockEdmAssociation(String navigationPropertyName) throws EdmException {
    EdmAssociation edmAssociation = EasyMock.createMock(EdmAssociation.class);
    EasyMock.expect(edmAssociation.getEnd("TO")).andReturn(mockEdmAssociatioEnd(navigationPropertyName, "TO"));
    EasyMock.expect(edmAssociation.getEnd("FROM")).andReturn(mockEdmAssociatioEnd(navigationPropertyName, "FROM"));
    EasyMock.replay(edmAssociation);
    return edmAssociation;
  }

  public static EdmEntitySet mockEdmEntitySet(String entityName, boolean withComplexType) throws EdmException {
    EdmEntitySet entitySet = null;
    if (entityName.equals(JPATypeMock.ENTITY_NAME)) {
      entitySet = EasyMock.createMock(EdmEntitySet.class);
      EasyMock.expect(entitySet.getEntityType()).andReturn(mockEdmEntityType(entityName, withComplexType)).anyTimes();
      EasyMock.expect(entitySet.getRelatedEntitySet(EasyMock.isA(EdmNavigationProperty.class))).andReturn(mockEdmEntitySet(JPARelatedTypeMock.ENTITY_NAME, false)).anyTimes();
    }
    else if (entityName.equals(JPARelatedTypeMock.ENTITY_NAME)) {
      entitySet = EasyMock.createMock(EdmEntitySet.class);
      EasyMock.expect(entitySet.getEntityType()).andReturn(mockEdmEntityType(entityName, withComplexType)).anyTimes();
    }

    EasyMock.replay(entitySet);
    return entitySet;
  }

  public static EdmNavigationProperty mockEdmNavigationProperty(String navigationPropertyName, EdmMultiplicity multiplicity) throws EdmException {

    EdmEntityType edmEntityType = mockEdmEntityType(JPARelatedTypeMock.ENTITY_NAME, false);

    EdmNavigationProperty navigationProperty = EasyMock.createMock(EdmNavigationProperty.class);
    EasyMock.expect(navigationProperty.getType()).andReturn(edmEntityType).anyTimes();
    EasyMock.expect(navigationProperty.getMultiplicity()).andReturn(multiplicity);
    EasyMock.expect(navigationProperty.getMapping()).andReturn((EdmMapping) mockEdmMapping(null, null, navigationPropertyName));
    EasyMock.expect(navigationProperty.getToRole()).andReturn("TO");
    EasyMock.expect(navigationProperty.getRelationship()).andReturn(mockEdmAssociation(navigationPropertyName));
    if (multiplicity.equals(EdmMultiplicity.ONE))
      EasyMock.expect(navigationProperty.getName()).andReturn(JPATypeMock.NAVIGATION_PROPERTY_X);

    EasyMock.replay(navigationProperty);

    return navigationProperty;
  }

  public static EdmProperty mockEdmProperty(String entityName, String propertyName) throws EdmException {
    EdmProperty edmProperty = EasyMock.createMock(EdmProperty.class);

    if (propertyName.equals(JPATypeMock.PROPERTY_NAME_MINT) ||
        propertyName.equals(JPATypeMock.PROPERTY_NAME_MSTRING) ||
        propertyName.equals(JPATypeMock.PROPERTY_NAME_MDATETIME) ||
        propertyName.equals(JPATypeMock.JPATypeEmbeddableMock.PROPERTY_NAME_MSHORT) ||
        propertyName.equals(JPATypeMock.JPATypeEmbeddableMock2.PROPERTY_NAME_MFLOAT) ||
        propertyName.equals(JPATypeMock.JPATypeEmbeddableMock2.PROPERTY_NAME_MUUID) ||
        propertyName.equals(JPARelatedTypeMock.PROPERTY_NAME_MLONG) ||
        propertyName.equals(JPARelatedTypeMock.PROPERTY_NAME_MBYTE) ||
        propertyName.equals(JPARelatedTypeMock.PROPERTY_NAME_MDOUBLE) ||
        propertyName.equals(JPARelatedTypeMock.PROPERTY_NAME_MBYTEARRAY)) {

      EdmType edmType = EasyMock.createMock(EdmType.class);
      EasyMock.expect(edmProperty.getType()).andReturn(edmType).anyTimes();
      EasyMock.expect(edmType.getKind()).andReturn(EdmTypeKind.SIMPLE).anyTimes();
      EasyMock.replay(edmType);
      EasyMock.expect(edmProperty.getName()).andReturn(propertyName).anyTimes();
      EasyMock.expect(edmProperty.getMapping()).andReturn((EdmMapping) mockEdmMapping(entityName, propertyName, null)).anyTimes();

    }
    else if (propertyName.equals(JPATypeMock.JPATypeEmbeddableMock.PROPERTY_NAME_MEMBEDDABLE) ||
        propertyName.equals(JPATypeMock.PROPERTY_NAME_MCOMPLEXTYPE)) {
      EdmComplexType complexType = mockComplexType(propertyName);

      EasyMock.expect(edmProperty.getType()).andReturn(complexType).anyTimes();
      EasyMock.expect(edmProperty.getName()).andReturn(propertyName).anyTimes();
      EasyMock.expect(edmProperty.getMapping()).andReturn((EdmMapping) mockEdmMapping(null, propertyName, null)).anyTimes();

    }

    EasyMock.replay(edmProperty);
    return edmProperty;
  }

  public static EdmComplexType mockComplexType(String complexPropertyName) throws EdmException {

    String complexTypeName = null;
    if (complexPropertyName.equals(JPATypeEmbeddableMock.PROPERTY_NAME_MEMBEDDABLE))
      complexTypeName = JPATypeEmbeddableMock2.ENTITY_NAME;
    else if (complexPropertyName.equals(JPATypeMock.PROPERTY_NAME_MCOMPLEXTYPE))
      complexTypeName = JPATypeEmbeddableMock.ENTITY_NAME;

    EdmComplexType edmComplexType = EasyMock.createMock(EdmComplexType.class);
    EasyMock.expect(edmComplexType.getKind()).andReturn(EdmTypeKind.COMPLEX);
    EasyMock.expect(edmComplexType.getPropertyNames()).andReturn(mockPropertyNames(complexTypeName)).anyTimes();
    EasyMock.expect(edmComplexType.getMapping()).andReturn((EdmMapping) mockEdmMapping(complexTypeName, null, null));

    if (complexTypeName.equals(JPATypeEmbeddableMock.ENTITY_NAME)) {
      EasyMock.expect(edmComplexType.getProperty(JPATypeEmbeddableMock.PROPERTY_NAME_MSHORT)).andReturn(mockEdmProperty(complexTypeName, JPATypeEmbeddableMock.PROPERTY_NAME_MSHORT)).anyTimes();
      EasyMock.expect(edmComplexType.getProperty(JPATypeEmbeddableMock.PROPERTY_NAME_MEMBEDDABLE)).andReturn(mockEdmProperty(complexTypeName, JPATypeEmbeddableMock.PROPERTY_NAME_MEMBEDDABLE)).anyTimes();
    }
    else if (complexTypeName.equals(JPATypeEmbeddableMock2.ENTITY_NAME)) {
      EasyMock.expect(edmComplexType.getProperty(JPATypeEmbeddableMock2.PROPERTY_NAME_MFLOAT)).andReturn(mockEdmProperty(complexTypeName, JPATypeEmbeddableMock2.PROPERTY_NAME_MFLOAT)).anyTimes();
      EasyMock.expect(edmComplexType.getProperty(JPATypeEmbeddableMock2.PROPERTY_NAME_MUUID)).andReturn(mockEdmProperty(complexTypeName, JPATypeEmbeddableMock2.PROPERTY_NAME_MUUID)).anyTimes();
    }

    EasyMock.replay(edmComplexType);
    return edmComplexType;
  }

  public static JPAEdmMapping mockEdmMapping(String entityName, String propertyName, String navigationPropertyName) {
    JPAEdmMapping mapping = new JPAEdmMappingImpl();

    if (propertyName == null && entityName != null) {
      if (entityName.equals(JPATypeMock.ENTITY_NAME))
        mapping.setJPAType(JPATypeMock.class);
      else if (entityName.equals(JPARelatedTypeMock.ENTITY_NAME))
        mapping.setJPAType(JPARelatedTypeMock.class);
      else if (entityName.equals(JPATypeEmbeddableMock.ENTITY_NAME))
        mapping.setJPAType(JPATypeEmbeddableMock.class);
      else if (entityName.equals(JPATypeEmbeddableMock2.ENTITY_NAME))
        mapping.setJPAType(JPATypeEmbeddableMock2.class);
    }
    else if (entityName == null && navigationPropertyName != null) {
      mapping.setJPAType(JPARelatedTypeMock.class);
      mapping.setJPAColumnName(JPATypeMock.NAVIGATION_PROPERTY_X);
    }
    else if (propertyName.equals(JPATypeMock.PROPERTY_NAME_MINT)) {
      mapping.setJPAType(int.class);
      ((Mapping) mapping).setInternalName(JPATypeMock.PROPERTY_NAME_MINT);
    }
    else if (propertyName.equals(JPATypeMock.PROPERTY_NAME_MSTRING)) {
      mapping.setJPAType(String.class);
      ((Mapping) mapping).setInternalName(JPATypeMock.PROPERTY_NAME_MSTRING);
    }
    else if (propertyName.equals(JPATypeMock.PROPERTY_NAME_MDATETIME)) {
      mapping.setJPAType(Calendar.class);
      ((Mapping) mapping).setInternalName(JPATypeMock.PROPERTY_NAME_MDATETIME);
    }
    else if (propertyName.equals(JPARelatedTypeMock.PROPERTY_NAME_MLONG)) {
      mapping.setJPAType(long.class);
      ((Mapping) mapping).setInternalName(JPARelatedTypeMock.PROPERTY_NAME_MLONG);
    }
    else if (propertyName.equals(JPARelatedTypeMock.PROPERTY_NAME_MDOUBLE)) {
      mapping.setJPAType(double.class);
      ((Mapping) mapping).setInternalName(JPARelatedTypeMock.PROPERTY_NAME_MDOUBLE);
    }
    else if (propertyName.equals(JPARelatedTypeMock.PROPERTY_NAME_MBYTE)) {
      mapping.setJPAType(byte.class);
      ((Mapping) mapping).setInternalName(JPARelatedTypeMock.PROPERTY_NAME_MBYTE);
    }
    else if (propertyName.equals(JPARelatedTypeMock.PROPERTY_NAME_MBYTEARRAY)) {
      mapping.setJPAType(byte[].class);
      ((Mapping) mapping).setInternalName(JPARelatedTypeMock.PROPERTY_NAME_MBYTEARRAY);
    }
    else if (propertyName.equals(JPATypeMock.JPATypeEmbeddableMock.PROPERTY_NAME_MSHORT)) {
      mapping.setJPAType(Short.TYPE);
      ((Mapping) mapping).setInternalName(JPATypeMock.JPATypeEmbeddableMock.PROPERTY_NAME_MSHORT);
    }
    else if (propertyName.equals(JPATypeMock.JPATypeEmbeddableMock2.PROPERTY_NAME_MFLOAT)) {
      mapping.setJPAType(Float.TYPE);
      ((Mapping) mapping).setInternalName(JPATypeMock.JPATypeEmbeddableMock2.PROPERTY_NAME_MFLOAT);
    }
    else if (propertyName.equals(JPATypeMock.JPATypeEmbeddableMock2.PROPERTY_NAME_MUUID)) {
      mapping.setJPAType(UUID.class);
      ((Mapping) mapping).setInternalName(JPATypeMock.JPATypeEmbeddableMock2.PROPERTY_NAME_MUUID);
    }
    else if (propertyName.equals(JPATypeMock.JPATypeEmbeddableMock.PROPERTY_NAME_MEMBEDDABLE)) {
      mapping.setJPAType(JPATypeEmbeddableMock2.class);
      ((Mapping) mapping).setInternalName(JPATypeMock.JPATypeEmbeddableMock.PROPERTY_NAME_MEMBEDDABLE);
    }
    else if (propertyName.equals(JPATypeMock.PROPERTY_NAME_MCOMPLEXTYPE)) {
      mapping.setJPAType(JPATypeEmbeddableMock.class);
      ((Mapping) mapping).setInternalName(JPATypeMock.PROPERTY_NAME_MCOMPLEXTYPE);
    }
    return mapping;
  }

}
