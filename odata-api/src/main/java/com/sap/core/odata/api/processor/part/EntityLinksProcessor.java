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
import com.sap.core.odata.api.uri.info.GetEntitySetLinksCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetLinksUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;

/**
 * Execute a OData entity links request. 
 * 
 * @author SAP AG
 */
public interface EntityLinksProcessor extends ODataProcessor {
  /**
   * Reads the URIs of the target entities of a navigation property.
   * @param uriInfo information about the request URI
   * @param contentType the content type of the response
   * @return an OData response object
   * @throws ODataException
   */
  ODataResponse readEntityLinks(GetEntitySetLinksUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * Counts the number of target entities of a navigation property.
   * @param uriInfo information about the request URI
   * @param contentType the content type of the response
   * @return an OData response object
   * @throws ODataException
   */
  ODataResponse countEntityLinks(GetEntitySetLinksCountUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * Creates a new link to a target entity of a navigation property.
   * @param uriInfo information about the request URI
   * @param content the content of the request, containing the link data
   * @param requestContentType the content type of the request body
   * @param contentType the content type of the response
   * @return an OData response object
   * @throws ODataException
   */
  ODataResponse createEntityLink(PostUriInfo uriInfo, InputStream content, String requestContentType, String contentType) throws ODataException;
}
