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
package com.sap.core.odata.testutil.mock;

import static org.junit.Assert.fail;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;

/**
 * Helper for the entity data model used as technical reference scenario.
 * @author SAP AG
 */
public class TecEdmInfo {
  private final Edm edm;

  public TecEdmInfo(final Edm edm) {
    this.edm = edm;
  }

  public EdmEntityType getTypeEtAllTypes() {
    try {
      return edm
          .getEntityContainer(TechnicalScenarioEdmProvider.ENTITY_CONTAINER_1)
          .getEntitySet(TechnicalScenarioEdmProvider.ES_ALL_TYPES)
          .getEntityType();
    } catch (final EdmException e) {
      fail("Error in test setup" + e.getLocalizedMessage());
    }
    return null;
  }
}
