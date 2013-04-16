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
import com.sap.core.odata.api.edm.EdmAssociationSetEnd;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;

public class EdmAssociationSetEndImplProv implements EdmAssociationSetEnd, EdmAnnotatable {

  private EdmEntitySet entitySet;
  private String role;
  private AssociationSetEnd end;

  public EdmAssociationSetEndImplProv(final AssociationSetEnd end, final EdmEntitySet entitySet) throws EdmException {
    this.end = end;
    this.entitySet = entitySet;
    role = end.getRole();
  }

  @Override
  public EdmEntitySet getEntitySet() throws EdmException {
    return entitySet;
  }

  @Override
  public String getRole() {
    return role;
  }

  @Override
  public EdmAnnotations getAnnotations() throws EdmException {
    return new EdmAnnotationsImplProv(end.getAnnotationAttributes(), end.getAnnotationElements());
  }
}
