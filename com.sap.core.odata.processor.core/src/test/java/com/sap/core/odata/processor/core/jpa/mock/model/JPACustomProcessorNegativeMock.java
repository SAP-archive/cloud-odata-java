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
package com.sap.core.odata.processor.core.jpa.mock.model;

import java.util.List;

import com.sap.core.odata.api.annotation.edm.FunctionImport;
import com.sap.core.odata.api.annotation.edm.FunctionImport.Multiplicity;
import com.sap.core.odata.api.annotation.edm.FunctionImport.ReturnType;
import com.sap.core.odata.api.annotation.edm.Parameter;

public class JPACustomProcessorNegativeMock {

  @FunctionImport(returnType = ReturnType.ENTITY_TYPE, multiplicity = Multiplicity.MANY)
  public List<JPACustomProcessorNegativeMock> method5() {
    return null;
  }

  @FunctionImport(returnType = ReturnType.ENTITY_TYPE, entitySet = "MockSet", multiplicity = Multiplicity.MANY)
  public void method6() {
    return;
  }

  @FunctionImport(returnType = ReturnType.ENTITY_TYPE, entitySet = "MockSet", multiplicity = Multiplicity.MANY)
  public JPACustomProcessorNegativeMock method8() {
    return null;
  }

  @FunctionImport(returnType = ReturnType.COMPLEX_TYPE, multiplicity = Multiplicity.ONE)
  public JPACustomProcessorNegativeMock method11() {
    return null;
  }

  @FunctionImport(returnType = ReturnType.SCALAR, multiplicity = Multiplicity.ONE)
  public JPACustomProcessorMock method12() {
    return null;
  }

  @FunctionImport(returnType = ReturnType.SCALAR, multiplicity = Multiplicity.ONE)
  public int method13(@Parameter(name = "") final int y) {
    return 0;
  }

  @FunctionImport(returnType = ReturnType.SCALAR, multiplicity = Multiplicity.ONE)
  public void method16(@Parameter(name = "") final int y) {
    return;
  }

  @FunctionImport(returnType = ReturnType.COMPLEX_TYPE, multiplicity = Multiplicity.ONE)
  public void method17(@Parameter(name = "") final int y) {
    return;
  }
}
