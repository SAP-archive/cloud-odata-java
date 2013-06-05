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
package com.sap.core.odata.testutil.fit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.processor.ODataErrorCallback;
import com.sap.core.odata.api.processor.ODataErrorContext;
import com.sap.core.odata.api.processor.ODataResponse;

/**
 * 
 * @author SAP AG
 */
public class FitErrorCallback implements ODataErrorCallback {

  private static final Logger LOG = LoggerFactory.getLogger(FitErrorCallback.class);

  /* (non-Javadoc)
   * @see com.sap.core.odata.api.processor.ODataErrorCallback#handleError(com.sap.core.odata.api.processor.ODataErrorContext)
   */
  @Override
  public ODataResponse handleError(final ODataErrorContext context) throws ODataApplicationException {

    if (context.getHttpStatus() == HttpStatusCodes.INTERNAL_SERVER_ERROR) {
      LOG.error("Internal Server Error", context.getException());
    }

    return EntityProvider.writeErrorDocument(context);
  }

}
