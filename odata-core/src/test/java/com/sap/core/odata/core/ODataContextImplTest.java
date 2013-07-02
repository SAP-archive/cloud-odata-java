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
package com.sap.core.odata.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.processor.ODataRequest;

/**
 * @author SAP AG
 */
public class ODataContextImplTest {

  ODataContextImpl context;

  @Before
  public void before() {
    ODataServiceFactory factory = mock(ODataServiceFactory.class);
    ODataRequest request = mock(ODataRequest.class);

    when(request.getMethod()).thenReturn(ODataHttpMethod.GET);
    when(request.getPathInfo()).thenReturn(new PathInfoImpl());

    context = new ODataContextImpl(request, factory);
  }

  @Test
  public void httpMethod() {
    context.setHttpMethod(ODataHttpMethod.GET.name());
    assertEquals(ODataHttpMethod.GET.name(), context.getHttpMethod());
  }

  @Test
  public void debugMode() {
    context.setDebugMode(true);
    assertTrue(context.isInDebugMode());
  }
}
