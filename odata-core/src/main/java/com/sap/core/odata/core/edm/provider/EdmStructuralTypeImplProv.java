package com.sap.core.odata.core.edm.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
  private Collection<String> edmPropertyNames;

  public EdmStructuralTypeImplProv(EdmImplProv edm, ComplexType structuralType, EdmTypeKind edmTypeKind, String namespace) throws EdmException {
    super(edm, structuralType.getName());
    edmProperties = new HashMap<String, EdmTyped>();
    this.namespace = namespace;
    this.edmTypeKind = edmTypeKind;

    FullQualifiedName fqName = structuralType.getBaseType();
    if (fqName != null) {
      if (EdmTypeKind.COMPLEX.equals(edmTypeKind)) {
        edmBaseType = edm.getComplexType(fqName.getNamespace(), fqName.getName());
      } else if (EdmTypeKind.ENTITY.equals(edmTypeKind)) {
        edmBaseType = edm.getEntityType(fqName.getNamespace(), fqName.getName());
      }
      if (edmBaseType == null) {
        throw new EdmException();
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
    if (property == null)
      property = getPropertyInternal(name);
    return property;
  }

  @Override
  public Collection<String> getPropertyNames() throws EdmException {
    if (edmPropertyNames == null) {
      edmPropertyNames = new ArrayList<String>();

      Map<String, Property> properties = structuralType.getProperties();
      for (String name : properties.keySet()) {
        edmPropertyNames.add(name);
      }

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

    Map<String, Property> properties = structuralType.getProperties();

    if (properties.containsKey(name)) {
      edmProperty = createProperty(properties.get(name), name);
      edmProperties.put(name, edmProperty);
    } else if (edmBaseType != null) {
      edmProperty = edmBaseType.getProperty(name);
      if (edmProperty != null) {
        edmProperties.put(name, edmProperty);
      }
    }

    return edmProperty;
  }

  protected EdmTyped createProperty(Property property, String name) throws EdmException {
    if (!name.equals(property.getName())) {
      throw new EdmException();
    }
    return new EdmPropertyImplProv(edm, property);
  }
}