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
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedHashMap;

import org.junit.Test;
import org.mockito.Mockito;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataErrorCallback;
import com.sap.core.odata.api.processor.ODataErrorContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.core.rest.ODataErrorHandlerCallbackImpl;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class ODataExceptionWrapperTest extends BaseTest {

  /**
   * Wrap an exception and verify that {@link PathInfo} is available and filled with correct values.
   * 
   */
  @Test
  public void testCallbackPathInfoAvailable() throws Exception {
    ODataContextImpl context = getMockedContext("http://localhost:80/test", "ODataServiceRoot");
    ODataErrorCallback errorCallback = new ODataErrorCallback() {
      @Override
      public ODataResponse handleError(final ODataErrorContext context) throws ODataApplicationException {
        PathInfo pathInfo = context.getPathInfo();
        assertEquals("ODataServiceRoot", pathInfo.getServiceRoot().toString());
        assertEquals("http://localhost:80/test", pathInfo.getRequestUri().toString());
        return ODataResponse.entity("bla").status(HttpStatusCodes.BAD_REQUEST).contentHeader("text/html").build();
      }
    };
    when(context.getServiceFactory()).thenReturn(new MapperServiceFactory(errorCallback));

    //
    Map<String, String> queryParameters = Collections.emptyMap();
    List<String> acceptContentTypes = Arrays.asList("text/html");
    ODataExceptionWrapper exceptionWrapper = createWrapper(context, queryParameters, acceptContentTypes);
    ODataResponse response = exceptionWrapper.wrapInExceptionResponse(new Exception());

    // verify
    assertNotNull(response);
    assertEquals(HttpStatusCodes.BAD_REQUEST.getStatusCode(), response.getStatus().getStatusCode());
    String errorMessage = (String) response.getEntity();
    assertEquals("bla", errorMessage);
    String contentTypeHeader = response.getContentHeader();
    assertEquals("text/html", contentTypeHeader);
  }

  private ODataExceptionWrapper createWrapper(final ODataContextImpl context, final Map<String, String> queryParameters, final List<String> acceptContentTypes) throws URISyntaxException {
    ODataExceptionWrapper exceptionWrapper = new ODataExceptionWrapper(context, queryParameters, acceptContentTypes);

    return exceptionWrapper;
  }

  private ODataContextImpl getMockedContext(final String requestUri, final String serviceRoot) throws ODataException, URISyntaxException {
    ODataContextImpl context = Mockito.mock(ODataContextImpl.class);
    PathInfoImpl pathInfo = new PathInfoImpl();
    pathInfo.setRequestUri(new URI(requestUri));
    pathInfo.setServiceRoot(new URI(serviceRoot));
    when(context.getPathInfo()).thenReturn(pathInfo);
    when(context.getRequestHeaders()).thenReturn(new MultivaluedHashMap<String, String>());
    return context;
  }

  public static final class MapperServiceFactory extends ODataServiceFactory {
    private ODataErrorCallback errorCallback;

    public MapperServiceFactory(final ODataErrorCallback callback) {
      errorCallback = callback;
    }

    @Override
    public ODataService createService(final ODataContext ctx) throws ODataException {
      return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ODataCallback> T getCallback(final Class<? extends ODataCallback> callbackInterface) {
      if (callbackInterface == ODataErrorCallback.class) {
        if (errorCallback == null) {
          return (T) new ODataErrorHandlerCallbackImpl();
        }
        return (T) errorCallback;
      }
      // only error callbacks are handled here
      return null;
    }
  }
}
