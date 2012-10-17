package com.sap.core.odata.core.experimental.edm.adapter;

import org.odata4j.edm.EdmCollectionType;
import org.odata4j.edm.EdmComplexType;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmProperty.CollectionKind;

import com.sap.core.odata.core.edm.EdmMultiplicity;
import com.sap.core.odata.core.edm.EdmType;
import com.sap.core.odata.core.edm.EdmTypeKind;
import com.sap.core.odata.core.edm.EdmTyped;

public class EdmReturnTypeAdapter implements EdmTyped {

  private org.odata4j.edm.EdmType edmType;
  private String name;
  private String fullQualifiedName;
  private EdmTypeKind edmTypeKind = EdmTypeKind.UNDEFINED;
  private EdmMultiplicity edmMultiplicity = EdmMultiplicity.ONE;

  public EdmReturnTypeAdapter(org.odata4j.edm.EdmType edmType) {
    this.edmType = edmType;
    this.fullQualifiedName = edmType.getFullyQualifiedTypeName();
    parseTypeName(this.fullQualifiedName);
    resolveType(edmType);
  }

  private void resolveType(org.odata4j.edm.EdmType edmType) {
    if (edmType.isSimple()) {
      this.edmTypeKind = EdmTypeKind.SIMPLE;
    } else {
      if (edmType instanceof EdmComplexType) {
        this.edmTypeKind = EdmTypeKind.COMPLEX;
      } else if (edmType instanceof EdmEntityType) {
        this.edmTypeKind = EdmTypeKind.ENTITY;
      } else if (edmType instanceof EdmCollectionType) {
        if (((EdmCollectionType) edmType).getCollectionKind().equals(CollectionKind.Collection)) {
          this.edmMultiplicity = EdmMultiplicity.MANY;
          resolveType(((EdmCollectionType) edmType).getItemType());
        }
      }
    }
  }

  private void parseTypeName(String fullQualifiedName) {
    
    //could also be Collection(...) !!!
    if (fullQualifiedName.startsWith("Collection(")) {
      int beginIndex = fullQualifiedName.indexOf("(") + 1;
      int endIndex = fullQualifiedName.length() - 1;
      fullQualifiedName = fullQualifiedName.substring(beginIndex, endIndex);
    }
    
    int pos = fullQualifiedName.lastIndexOf(".");
    if (pos > -1) {
      this.name = fullQualifiedName.substring(pos + 1);
    } else {
      this.name = fullQualifiedName;
    }
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public EdmType getType() {
    
    org.odata4j.edm.EdmType edmItemType;
    
    if (edmType instanceof EdmCollectionType) {
      edmItemType = ((EdmCollectionType) edmType).getItemType();
    } else {
      edmItemType = edmType;
    }
    
    if (EdmTypeKind.SIMPLE.equals(edmTypeKind)) {
      return new EdmSimpleTypeAdapter(edmItemType.getSimple(edmItemType.getFullyQualifiedTypeName())).getType();
    } else if (EdmTypeKind.COMPLEX.equals(edmTypeKind)) {
      return new EdmComplexTypeAdapter((EdmComplexType)edmItemType);
    } else if (EdmTypeKind.ENTITY.equals(edmTypeKind)) {
      return new EdmEntityTypeAdapter((EdmEntityType)edmItemType);
    }
    
    return null;
  }

  @Override
  public EdmMultiplicity getMultiplicity() {
    return this.edmMultiplicity;
  }

}
