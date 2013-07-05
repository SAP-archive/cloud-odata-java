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
package com.sap.core.odata.api.processor;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.uri.PathInfo;

/**
 * 
 * @author SAP AG
 */
public interface ODataRequest {

  String getRequestHeaderValue(String name);

  Map<String, List<String>> getRequestHeaders();

  InputStream getBody();

  PathInfo getPathInfo();

  ODataHttpMethod getMethod();

  List<Locale> getAcceptableLanguages();

  String getContentType();

  List<String> getAcceptHeaders();

  Map<String, String> getQueryParameters();

}
