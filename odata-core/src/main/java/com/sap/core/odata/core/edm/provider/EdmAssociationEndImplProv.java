/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmAnnotatable;
import com.sap.core.odata.api.edm.EdmAnnotations;
import com.sap.core.odata.api.edm.EdmAssociationEnd;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.AssociationEnd;

public class EdmAssociationEndImplProv implements EdmAssociationEnd, EdmAnnotatable {

  private EdmImplProv edm;
  private AssociationEnd associationEnd;

  public EdmAssociationEndImplProv(final EdmImplProv edm, final AssociationEnd associationEnd) throws EdmException {
    this.edm = edm;
    this.associationEnd = associationEnd;
  }

  @Override
  public String getRole() {
    return associationEnd.getRole();
  }

  @Override
  public EdmEntityType getEntityType() throws EdmException {
    final FullQualifiedName type = associationEnd.getType();
    EdmEntityType entityType = edm.getEntityType(type.getNamespace(), type.getName());
    if (entityType == null) {
      throw new EdmException(EdmException.COMMON);
    }
    return entityType;
  }

  @Override
  public EdmMultiplicity getMultiplicity() {
    return associationEnd.getMultiplicity();
  }

  @Override
  public EdmAnnotations getAnnotations() throws EdmException {
    return new EdmAnnotationsImplProv(associationEnd.getAnnotationAttributes(), associationEnd.getAnnotationElements());
  }

}
