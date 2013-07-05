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

import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.testutil.server.TestServer;

/**
 * @author SAP AG
 */
public class FitStaticServiceFactory extends ODataServiceFactory {

  @SuppressWarnings("unchecked")
  @Override
  public <T extends ODataCallback> T getCallback(final Class<? extends ODataCallback> callbackInterface) {
    if (callbackInterface.isAssignableFrom(FitErrorCallback.class)) {
      return (T) new FitErrorCallback();
    }

    return super.getCallback(callbackInterface);
  }

  private static Map<String, ODataService> PORT_2_SERVICE = Collections.synchronizedMap(new HashMap<String, ODataService>());

  public static void bindService(final TestServer server, final ODataService service) {
    PORT_2_SERVICE.put(createId(server), service);
  }

  public static void unbindService(final TestServer server) {
    PORT_2_SERVICE.remove(createId(server));
  }

  @Override
  public ODataService createService(final ODataContext ctx) throws ODataException {

    assertNotNull(ctx);
    assertNotNull(ctx.getAcceptableLanguages());

    final Map<String, List<String>> requestHeaders = ctx.getRequestHeaders();
    final String host = requestHeaders.get("Host").get(0);

    String tmp[] = host.split(":", 2);
    String port = (tmp.length == 2 && tmp[1] != null) ? tmp[1] : "80";

    // access and validation in synchronized block
    synchronized (PORT_2_SERVICE) {
      final ODataService service = PORT_2_SERVICE.get(port);
      if (service == null) {
        throw new IllegalArgumentException("no static service set for JUnit test");
      }
      return service;
    }
  }

  private static String createId(final TestServer server) {
    final URI endpoint = server.getEndpoint();
    if (endpoint == null) {
      throw new IllegalArgumentException("Got TestServer without endpoint.");
    }
    return "" + endpoint.getPort();
  }
}
