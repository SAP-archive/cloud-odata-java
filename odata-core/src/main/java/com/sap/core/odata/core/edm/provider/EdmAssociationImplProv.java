/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmAnnotatable;
import com.sap.core.odata.api.edm.EdmAnnotations;
import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmAssociationEnd;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;

public class EdmAssociationImplProv extends EdmNamedImplProv implements EdmAssociation, EdmAnnotatable {

  private Association association;
  private String namespace;

  public EdmAssociationImplProv(final EdmImplProv edm, final Association association, final String namespace) throws EdmException {
    super(edm, association.getName());
    this.association = association;
    this.namespace = namespace;
  }

  @Override
  public String getNamespace() throws EdmException {
    return namespace;
  }

  @Override
  public EdmTypeKind getKind() {
    return EdmTypeKind.ASSOCIATION;
  }

  @Override
  public EdmAssociationEnd getEnd(final String role) throws EdmException {
    AssociationEnd end = association.getEnd1();
    if (end.getRole().equals(role)) {
      return new EdmAssociationEndImplProv(edm, end);
    }
    end = association.getEnd2();
    if (end.getRole().equals(role)) {
      return new EdmAssociationEndImplProv(edm, end);
    }

    return null;
  }

  @Override
  public EdmAnnotations getAnnotations() throws EdmException {
    return new EdmAnnotationsImplProv(association.getAnnotationAttributes(), association.getAnnotationElements());
  }

  public EdmMultiplicity getEndMultiplicity(final String role) {
    if (association.getEnd1().getRole().equals(role)) {
      return association.getEnd1().getMultiplicity();
    }

    if (association.getEnd2().getRole().equals(role)) {
      return association.getEnd2().getMultiplicity();
    }

    return null;
  }

}