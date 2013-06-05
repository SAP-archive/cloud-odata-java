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
package com.sap.core.odata.api.servicedocument;

import java.util.List;

import com.sap.core.odata.api.edm.EdmEntitySetInfo;
import com.sap.core.odata.api.ep.EntityProviderException;

/**
 * A Service document
 * <p>Service document lists all EntitySets
 * @author SAP AG
 */
public interface ServiceDocument {
  /**
   * Get the list of the EntitySets
   * 
   * @return a list of {@link EdmEntitySetInfo}
   */
  public List<EdmEntitySetInfo> getEntitySetsInfo() throws EntityProviderException;

  /**
   * Get additional information if the service document is in atom format 
   * 
   * @return {@link AtomInfo} or null
   */
  public AtomInfo getAtomInfo();
}
