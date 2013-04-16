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
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmParameter;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.provider.FunctionImportParameter;
import com.sap.core.odata.core.edm.EdmSimpleTypeFacadeImpl;

/**
 * @author SAP AG
 */
public class EdmParameterImplProv extends EdmElementImplProv implements EdmParameter, EdmAnnotatable {

  FunctionImportParameter parameter;

  public EdmParameterImplProv(final EdmImplProv edm, final FunctionImportParameter parameter) throws EdmException {
    super(edm, parameter.getName(), parameter.getType().getFullQualifiedName(), parameter.getFacets(), parameter.getMapping());
    this.parameter = parameter;
  }

  @Override
  public EdmType getType() throws EdmException {
    if (edmType == null) {
      edmType = EdmSimpleTypeFacadeImpl.getEdmSimpleType(parameter.getType());
      if (edmType == null) {
        throw new EdmException(EdmException.COMMON);
      }
    }
    return edmType;
  }

  @Override
  public EdmAnnotations getAnnotations() throws EdmException {
    return new EdmAnnotationsImplProv(parameter.getAnnotationAttributes(), parameter.getAnnotationElements());
  }
}
