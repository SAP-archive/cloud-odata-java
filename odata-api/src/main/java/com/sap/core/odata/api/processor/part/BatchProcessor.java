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
package com.sap.core.odata.api.processor.part;

import java.io.InputStream;
import java.util.List;

import com.sap.core.odata.api.batch.BatchHandler;
import com.sap.core.odata.api.batch.BatchResponsePart;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;

/**
 * Execute a OData batch request. 
 * 
 * @author SAP AG
 *
 */
public interface BatchProcessor extends ODataProcessor {

  /**
   * Executes a OData batch request and provide Batch Response as {@link ODataResponse} 
   * @param handler batch handler
   * @param contentType the content type of the request
   * @param content Batch Request body
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse executeBatch(BatchHandler handler, String contentType, InputStream content) throws ODataException;

  /**
   * Executes a Change Set and provide BatchResponsePart as {@link BatchResponsePart} that contains the responses to change requests.
   * The method has to define a rollback semantic that may be applied when a request within a Change Set fails (all-or-nothing requirement).
   * If a request within a Change Set fails, instead of Change Set Response should be returned the error response 
   * @param handler batch handler
   * @param requests list of single change requests
   * @return a {@link BatchResponsePart} object
   * @throws ODataException 
   */
  BatchResponsePart executeChangeSet(BatchHandler handler, List<ODataRequest> requests) throws ODataException;
}
