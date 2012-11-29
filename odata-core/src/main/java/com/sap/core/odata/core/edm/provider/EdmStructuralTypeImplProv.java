package com.sap.core.odata.core.edm.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.Property;

/**
 * @author SAP AG
 */
public abstract class EdmStructuralTypeImplProv extends EdmNamedImplProv implements EdmStructuralType {

  protected EdmStructuralType edmBaseType;
  private ComplexType structuralType;
  private EdmTypeKind edmTypeKind;
  protected String namespace;
  protected Map<String, EdmTyped> edmProperties;
  private Map<String, Property> properties;
  private Collection<String> edmPropertyNames;

  public EdmStructuralTypeImplProv(EdmImplProv edm, ComplexType structuralType, EdmTypeKind edmTypeKind, String namespace) throws EdmException {
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
    this.properties = new HashMap<String, Property>();

    Collection<Property> properties = structuralType.getProperties();
    if (properties != null) {
      Property property;
      for (Iterator<Property> iterator = properties.iterator(); iterator.hasNext();) {
        property = iterator.next();
        this.properties.put(property.getName(), property);
      }
    }
  }

  @Override
  public String getNamespace() throws EdmException {
    return namespace;
  }

  @Override
  public EdmTyped getProperty(String name) throws EdmException {
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
  public Collection<String> getPropertyNames() throws EdmException {
    if (edmPropertyNames == null) {
      edmPropertyNames = new ArrayList<String>();
      edmPropertyNames.addAll(properties.keySet());
      if (edmBaseType != null) {
        edmPropertyNames.addAll(edmBaseType.getPropertyNames());
      }
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

  protected EdmTyped getPropertyInternal(String name) throws EdmException {
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

  protected EdmTyped createProperty(Property property) throws EdmException {
    return new EdmPropertyImplProv(edm, property);
  }
}