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
package com.sap.core.odata.core.ep.producer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;

import org.junit.Test;

import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.ODataHttpHeaders;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySetInfo;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.ep.JsonEntityProvider;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * @author SAP AG
 */
public class JsonServiceDocumentProducerTest extends BaseTest {

  @Test
  public void serviceDocumentEmpty() throws Exception {
    Edm edm = mock(Edm.class);
    EdmServiceMetadata metadata = mock(EdmServiceMetadata.class);
    when(edm.getServiceMetadata()).thenReturn(metadata);
    final ODataResponse response = new JsonEntityProvider().writeServiceDocument(edm, "http://host:80/service/");
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());
    assertEquals(ODataServiceVersion.V10, response.getHeader(ODataHttpHeaders.DATASERVICEVERSION));

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"EntitySets\":[]}}", json);
  }

  @Test
  public void serviceDocument() throws Exception {
    Edm edm = mock(Edm.class);
    EdmServiceMetadata metadata = mock(EdmServiceMetadata.class);
    EdmEntitySetInfo entitySetInfo1 = mock(EdmEntitySetInfo.class);
    when(entitySetInfo1.getEntitySetUri()).thenReturn(URI.create("EntitySet"));
    EdmEntitySetInfo entitySetInfo2 = mock(EdmEntitySetInfo.class);
    when(entitySetInfo2.getEntitySetUri()).thenReturn(URI.create("Container2.EntitySet2"));
    when(metadata.getEntitySetInfos()).thenReturn(Arrays.asList(entitySetInfo1, entitySetInfo2));
    when(edm.getServiceMetadata()).thenReturn(metadata);
    final ODataResponse response = new JsonEntityProvider().writeServiceDocument(edm, "http://host:80/service/");
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());
    assertEquals(ODataServiceVersion.V10, response.getHeader(ODataHttpHeaders.DATASERVICEVERSION));

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"EntitySets\":[\"EntitySet\",\"Container2.EntitySet2\"]}}", json);
  }
}
