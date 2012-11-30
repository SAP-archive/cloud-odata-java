package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmAnnotatable;
import com.sap.core.odata.api.edm.EdmAnnotations;
import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmAssociationSet;
import com.sap.core.odata.api.edm.EdmAssociationSetEnd;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;

public class EdmAssociationSetImplProv extends EdmNamedImplProv implements EdmAssociationSet, EdmAnnotatable {

  private AssociationSet associationSet;
  private EdmEntityContainer edmEntityContainer;

  public EdmAssociationSetImplProv(EdmImplProv edm, AssociationSet associationSet, EdmEntityContainer edmEntityContainer) throws EdmException {
    super(edm, associationSet.getName());
    this.associationSet = associationSet;
    this.edmEntityContainer = edmEntityContainer;
  }

  @Override
  public EdmAssociation getAssociation() throws EdmException {
    EdmAssociation association = edm.getAssociation(associationSet.getAssociation().getNamespace(), associationSet.getAssociation().getName());
    if(association == null){
      throw new EdmException(EdmException.COMMON);
    }
    return association;
  }

  @Override
  public EdmAssociationSetEnd getEnd(final String role) throws EdmException {
    final AssociationSetEnd end =
        associationSet.getEnd1().getRole().equals(role) ?
            associationSet.getEnd1() : associationSet.getEnd2();
     EdmEntitySet entitySet =  edmEntityContainer.getEntitySet(end.getEntitySet());   
     if(entitySet == null){
       throw new EdmException(EdmException.COMMON);
     }
       
    return new EdmAssociationSetEndImplProv(end, entitySet);
  }

  @Override
  public EdmEntityContainer getEntityContainer() throws EdmException {
    return edmEntityContainer;
  }

  @Override
  public EdmAnnotations getAnnotations() throws EdmException {
    return new EdmAnnotationsImplProv(associationSet.getAnnotationAttributes(), associationSet.getAnnotationElements());
  }

}
