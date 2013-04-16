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

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetSimplePropertyUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;

/**
 * Execute a OData entity simple property request. 
 * 
 * @author SAP AG
 */
public interface EntitySimplePropertyProcessor extends ODataProcessor {

  /**
   * Reads a simple property of an entity.
   * @param contentType the content type of the response
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntitySimpleProperty(GetSimplePropertyUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * Updates a simple property of an entity.
   * @param uriInfo information about the request URI
   * @param content the content of the request, containing the updated property data
   * @param requestContentType the content type of the request body
   * @param contentType the content type of the response
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntitySimpleProperty(PutMergePatchUriInfo uriInfo, InputStream content, String requestContentType, String contentType) throws ODataException;
}
