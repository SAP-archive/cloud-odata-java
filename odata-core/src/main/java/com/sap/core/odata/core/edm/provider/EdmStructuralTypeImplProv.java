package com.sap.core.odata.core.edm.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmAnnotatable;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.SimpleProperty;

/**
 * @author SAP AG
 */
public abstract class EdmStructuralTypeImplProv extends EdmNamedImplProv implements EdmStructuralType, EdmAnnotatable {

  protected EdmStructuralType edmBaseType;
  protected ComplexType structuralType;
  private EdmTypeKind edmTypeKind;
  protected String namespace;
  protected Map<String, EdmTyped> edmProperties;
  private Map<String, Property> properties;
  private List<String> edmPropertyNames;

  public EdmStructuralTypeImplProv(final EdmImplProv edm, final ComplexType structuralType, final EdmTypeKind edmTypeKind, final String namespace) throws EdmException {
    super(edm, structuralType.getName());
    this.structuralType = structuralType;
    this.namespace = namespace;
    this.edmTypeKind = edmTypeKind;

    resolveBaseType();

    buildPropertiesInternal();

    edmProperties = new HashMap<String, EdmTyped>();
  }

  private void resolveBaseType() throws EdmException {
    FullQualifiedName fqName = structuralType.getBaseType();
    if (fqName != null) {
      if (EdmTypeKind.COMPLEX.equals(edmTypeKind)) {
        edmBaseType = edm.getComplexType(fqName.getNamespace(), fqName.getName());
      } else if (EdmTypeKind.ENTITY.equals(edmTypeKind)) {
        edmBaseType = edm.getEntityType(fqName.getNamespace(), fqName.getName());
      }
      if (edmBaseType == null) {
        throw new EdmException(EdmException.COMMON);
      }
    }
  }

  private void buildPropertiesInternal() throws EdmException {
    properties = new HashMap<String, Property>();

    if (structuralType.getProperties() != null)
      for (final Property property : structuralType.getProperties())
        properties.put(property.getName(), property);
  }

  @Override
  public String getNamespace() throws EdmException {
    return namespace;
  }

  @Override
  public EdmTyped getProperty(final String name) throws EdmException {
    EdmTyped property = edmProperties.get(name);
    if (property == null) {
      property = getPropertyInternal(name);
      if (property == null && edmBaseType != null) {
        property = edmBaseType.getProperty(name);
      }
    }
    return property;
  }

  @Override
  public List<String> getPropertyNames() throws EdmException {
    if (edmPropertyNames == null) {
      edmPropertyNames = new ArrayList<String>();
      if (edmBaseType != null)
        edmPropertyNames.addAll(edmBaseType.getPropertyNames());
      if (structuralType.getProperties() != null)
        for (final Property property : structuralType.getProperties())
          edmPropertyNames.add(property.getName());
    }

    return edmPropertyNames;
  }

  @Override
  public EdmStructuralType getBaseType() throws EdmException {
    return edmBaseType;
  }

  @Override
  public EdmTypeKind getKind() {
    return edmTypeKind;
  }

  @Override
  public EdmMapping getMapping() throws EdmException {
    return structuralType.getMapping();
  }

  protected EdmTyped getPropertyInternal(final String name) throws EdmException {
    EdmTyped edmProperty = null;

    if (properties.containsKey(name)) {
      edmProperty = createProperty(properties.get(name));
      edmProperties.put(name, edmProperty);
    } else if (edmBaseType != null) {
      edmProperty = edmBaseType.getProperty(name);
      if (edmProperty != null) {
        edmProperties.put(name, edmProperty);
      }
    }

    return edmProperty;
  }

  protected EdmTyped createProperty(final Property property) throws EdmException {
    if (property instanceof SimpleProperty) {
      return new EdmSimplePropertyImplProv(edm, (SimpleProperty) property);
    } else if (property instanceof ComplexProperty) {
      return new EdmComplexPropertyImplProv(edm, (ComplexProperty) property);
    } else {
      throw new EdmException(EdmException.COMMON);
    }

  }
}