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
import com.sap.core.odata.api.edm.EdmAssociationSet;
import com.sap.core.odata.api.edm.EdmAssociationSetEnd;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.EntitySet;

public class EdmEntitySetImplProv extends EdmNamedImplProv implements EdmEntitySet, EdmAnnotatable {

  private EntitySet entitySet;
  private EdmEntityContainer edmEntityContainer;
  private EdmEntityType edmEntityType;

  public EdmEntitySetImplProv(final EdmImplProv edm, final EntitySet entitySet, final EdmEntityContainer edmEntityContainer) throws EdmException {
    super(edm, entitySet.getName());
    this.entitySet = entitySet;
    this.edmEntityContainer = edmEntityContainer;
  }

  @Override
  public EdmEntityType getEntityType() throws EdmException {
    if (edmEntityType == null) {
      FullQualifiedName fqName = entitySet.getEntityType();
      edmEntityType = edm.getEntityType(fqName.getNamespace(), fqName.getName());
      if (edmEntityType == null) {
        throw new EdmException(EdmException.COMMON);
      }
    }
    return edmEntityType;
  }

  @Override
  public EdmEntitySet getRelatedEntitySet(final EdmNavigationProperty navigationProperty) throws EdmException {
    EdmAssociationSet associationSet = edmEntityContainer.getAssociationSet(edmEntityContainer.getEntitySet(entitySet.getName()), navigationProperty);
    EdmAssociationSetEnd toEnd = associationSet.getEnd(navigationProperty.getToRole());
    if (toEnd == null) {
      throw new EdmException(EdmException.COMMON);
    }
    EdmEntitySet targetEntitySet = toEnd.getEntitySet();
    if (targetEntitySet == null) {
      throw new EdmException(EdmException.COMMON);
    }
    return targetEntitySet;
  }

  @Override
  public EdmEntityContainer getEntityContainer() throws EdmException {
    return edmEntityContainer;
  }

  @Override
  public EdmAnnotations getAnnotations() throws EdmException {
    return new EdmAnnotationsImplProv(entitySet.getAnnotationAttributes(), entitySet.getAnnotationElements());
  }

  @Override
  public EdmMapping getMapping() throws EdmException {
    return entitySet.getMapping();
  }
}
