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
package com.sap.core.odata.api.batch;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;

/**
 * @author SAP AG
 */
public interface BatchHandler {
  /**
   * <p>Handles the {@link BatchRequestPart} in a way that it results in a corresponding {@link BatchResponsePart}.</p>
   * @param batchRequestPart the incoming MIME part
   * @return the corresponding result
   * @throws ODataException
   */
  public BatchResponsePart handleBatchPart(BatchRequestPart batchRequestPart) throws ODataException;

  /**
   * <p>Delegates a handling of the request {@link ODataRequest} to the request handler and provides ODataResponse {@link ODataResponse}.</p>
   * @param request the incoming request
   * @return the corresponding result
   * @throws ODataException
   */
  public ODataResponse handleRequest(ODataRequest request) throws ODataException;
}
