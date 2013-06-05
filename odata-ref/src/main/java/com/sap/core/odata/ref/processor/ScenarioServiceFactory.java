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
package com.sap.core.odata.ref.processor;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.ref.edm.ScenarioEdmProvider;
import com.sap.core.odata.ref.model.DataContainer;

/**
 * @author SAP AG
 */
public class ScenarioServiceFactory extends ODataServiceFactory {

  @Override
  public ODataService createService(final ODataContext context) throws ODataException {
    DataContainer dataContainer = new DataContainer();
    dataContainer.reset();

    return createODataSingleProcessorService(
        new ScenarioEdmProvider(),
        new ListsProcessor(new ScenarioDataSource(dataContainer)));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends ODataCallback> T getCallback(final Class<? extends ODataCallback> callbackInterface) {
    return (T) (callbackInterface.isAssignableFrom(ScenarioErrorCallback.class) ?
        new ScenarioErrorCallback() : super.getCallback(callbackInterface));
  }
}
