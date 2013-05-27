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

import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.testutil.TestUtilRuntimeException;
import com.sap.core.odata.testutil.server.ServerRuntimeException;
import com.sap.core.odata.testutil.server.TestServer;

/**
 * @author SAP AG
 */
public abstract class AbstractFitTest extends BaseTest {

  private final TestServer server;

  private ODataService service;

  private final HttpClient httpClient = new DefaultHttpClient();

  public AbstractFitTest() {
    server = new TestServer(this.getClass().getSimpleName());
  }

  protected URI getEndpoint() {
    return server.getEndpoint();
  }

  protected HttpClient getHttpClient() {
    return httpClient;
  }

  protected ODataService getService() {
    return service;
  }

  protected abstract ODataService createService() throws ODataException;

  @Before
  public void before() {
    try {
      service = createService();
      server.startServer(service);
    } catch (final ODataException e) {
      throw new TestUtilRuntimeException(e);
    }
  }

  @After
  public void after() {
    try {
      server.stopServer();
    } catch (final ServerRuntimeException e) {
      throw new TestUtilRuntimeException(e);
    }
  }
}
