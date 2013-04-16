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
package com.sap.core.odata.fit.mapping;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;

/**
 * @author SAP AG
 */
public class MapFactory extends ODataServiceFactory {

  @Override
  public ODataService createService(final ODataContext ctx) throws ODataException {
    final MapProvider provider = new MapProvider();
    final MapProcessor processor = new MapProcessor();

    return createODataSingleProcessorService(provider, processor);
  }

  public static ODataService create() throws ODataException {
    return new MapFactory().createService(null);
  }

}
