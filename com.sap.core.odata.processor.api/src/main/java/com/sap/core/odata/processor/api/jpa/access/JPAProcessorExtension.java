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
package com.sap.core.odata.processor.api.jpa.access;

import java.util.HashMap;
import java.util.List;

public class JPAProcessorExtension {

  private JPAProcessor jpaProcessor;
  private HashMap<JPAProcessorOperation, List<String>> operationEntityMap;

  public JPAProcessor getJpaProcessor() {
    return jpaProcessor;
  }

  public void setJpaProcessor(final JPAProcessor jpaProcessor) {
    this.jpaProcessor = jpaProcessor;
  }

  public HashMap<JPAProcessorOperation, List<String>> getOperationEntityMap() {
    return operationEntityMap;
  }

  public void setOperationEntityMap(final HashMap<JPAProcessorOperation, List<String>> operationEntityMap) {
    this.operationEntityMap = operationEntityMap;
  }

}
