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

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.core.edm.EdmSimpleTypeFacadeImpl;

/**
 * @author SAP AG
 */
public class EdmTypedImplProv extends EdmNamedImplProv implements EdmTyped {

  protected EdmType edmType;
  private FullQualifiedName typeName;
  private EdmMultiplicity multiplicity;

  public EdmTypedImplProv(final EdmImplProv edm, final String name, final FullQualifiedName typeName, final EdmMultiplicity multiplicity) throws EdmException {
    super(edm, name);
    this.typeName = typeName;
    this.multiplicity = multiplicity;
  }

  @Override
  public EdmType getType() throws EdmException {
    if (edmType == null) {
      final String namespace = typeName.getNamespace();
      if (EdmSimpleType.EDM_NAMESPACE.equals(typeName.getNamespace())) {
        edmType = EdmSimpleTypeFacadeImpl.getEdmSimpleType(EdmSimpleTypeKind.valueOf(typeName.getName()));
      } else {
        edmType = edm.getComplexType(namespace, typeName.getName());
      }
      if (edmType == null) {
        edmType = edm.getEntityType(namespace, typeName.getName());
      }

      if (edmType == null) {
        throw new EdmException(EdmException.COMMON);
      }

    }
    return edmType;
  }

  @Override
  public EdmMultiplicity getMultiplicity() throws EdmException {
    return multiplicity;
  }
}
