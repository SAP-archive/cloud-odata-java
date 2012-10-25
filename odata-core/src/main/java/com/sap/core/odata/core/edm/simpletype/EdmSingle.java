package com.sap.core.odata.core.edm.simpletype;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;

public class EdmSingle implements EdmSimpleType {

  private EdmSimpleTypeKind edmSimpleType = EdmSimpleTypeKind.Single;

  @Override
  public boolean equals(Object obj) {
    boolean equals = false;
    if (this == obj) {
      equals = true;
    } else if (obj instanceof EdmSingle) {
      equals = true;
    }

    return equals;
  }

  @Override
  public String getNamespace() throws EdmException {
    return EdmSimpleTypeFacade.edmNamespace;
  }

  @Override
  public EdmTypeKind getKind() {
    return EdmTypeKind.SIMPLE;
  }

  @Override
  public String getName() throws EdmException {
    return this.edmSimpleType.toString();
  }

  @Override
  public boolean isCompatible(EdmSimpleType simpleType) {
    boolean compatible;

    if (simpleType instanceof EdmBit || simpleType instanceof EdmUint7) {
      compatible = true;
    } else {
      EdmSimpleTypeKind simpleTypeKind;
      try {
        simpleTypeKind = EdmSimpleTypeKind.valueOf(simpleType.getName());
        switch (simpleTypeKind) {
        case Byte:
        case SByte:
        case Int16:
        case Int32:
        case Int64:
        case Single:
          compatible = true;
          break;
        default:
          compatible = false;
          break;
        }
      } catch (EdmException e) {
        compatible = false;
      }

    }

    return compatible;
  }

  @Override
  public boolean validate(String value, EdmLiteralKind literalKind, EdmFacets facets) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Object valueOfString(String value, EdmLiteralKind literalKind, EdmFacets facets) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String valueToString(Object value, EdmLiteralKind literalKind, EdmFacets facets) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toUriLiteral(String literal) {
    // TODO Auto-generated method stub
    return null;
  }

}
