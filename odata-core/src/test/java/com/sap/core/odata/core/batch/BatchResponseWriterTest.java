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
package com.sap.core.odata.core.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sap.core.odata.api.batch.BatchException;
import com.sap.core.odata.api.batch.BatchResponsePart;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.processor.ODataResponse;

public class BatchResponseWriterTest {

  @Test
  public void testBatchResponse() throws BatchException, IOException {
    List<BatchResponsePart> parts = new ArrayList<BatchResponsePart>();
    ODataResponse response = ODataResponse.entity("Walter Winter")
        .status(HttpStatusCodes.OK)
        .contentHeader("application/json")
        .build();
    List<ODataResponse> responses = new ArrayList<ODataResponse>(1);
    responses.add(response);
    parts.add(BatchResponsePart.responses(responses).changeSet(false).build());

    ODataResponse changeSetResponse = ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
    responses = new ArrayList<ODataResponse>(1);
    responses.add(changeSetResponse);
    parts.add(BatchResponsePart.responses(responses).changeSet(true).build());

    BatchResponseWriter writer = new BatchResponseWriter();
    ODataResponse batchResponse = writer.writeResponse(parts);

    assertEquals(202, batchResponse.getStatus().getStatusCode());
    assertNotNull(batchResponse.getEntity());
    String body = (String) batchResponse.getEntity();

    assertTrue(body.contains("--batch"));
    assertTrue(body.contains("--changeset"));
    assertTrue(body.contains("HTTP/1.1 200 OK"));
    assertTrue(body.contains("Content-Type: application/http"));
    assertTrue(body.contains("Content-Transfer-Encoding: binary"));
    assertTrue(body.contains("Walter Winter"));
    assertTrue(body.contains("multipart/mixed; boundary=changeset"));
    assertTrue(body.contains("HTTP/1.1 204 No Content"));

  }

  @Test
  public void testResponse() throws BatchException, IOException {
    List<BatchResponsePart> parts = new ArrayList<BatchResponsePart>();
    ODataResponse response = ODataResponse.entity("Walter Winter").status(HttpStatusCodes.OK).contentHeader("application/json").build();
    List<ODataResponse> responses = new ArrayList<ODataResponse>(1);
    responses.add(response);
    parts.add(BatchResponsePart.responses(responses).changeSet(false).build());
    BatchResponseWriter writer = new BatchResponseWriter();
    ODataResponse batchResponse = writer.writeResponse(parts);

    assertEquals(202, batchResponse.getStatus().getStatusCode());
    assertNotNull(batchResponse.getEntity());
    String body = (String) batchResponse.getEntity();

    assertTrue(body.contains("--batch"));
    assertFalse(body.contains("--changeset"));
    assertTrue(body.contains("HTTP/1.1 200 OK" + "\r\n"));
    assertTrue(body.contains("Content-Type: application/http" + "\r\n"));
    assertTrue(body.contains("Content-Transfer-Encoding: binary" + "\r\n"));
    assertTrue(body.contains("Walter Winter"));
    assertFalse(body.contains("multipart/mixed; boundary=changeset"));

  }

  @Test
  public void testChangeSetResponse() throws BatchException, IOException {
    List<BatchResponsePart> parts = new ArrayList<BatchResponsePart>();
    ODataResponse changeSetResponse = ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
    List<ODataResponse> responses = new ArrayList<ODataResponse>(1);
    responses.add(changeSetResponse);
    parts.add(BatchResponsePart.responses(responses).changeSet(true).build());

    BatchResponseWriter writer = new BatchResponseWriter();
    ODataResponse batchResponse = writer.writeResponse(parts);

    assertEquals(202, batchResponse.getStatus().getStatusCode());
    assertNotNull(batchResponse.getEntity());
    String body = (String) batchResponse.getEntity();
    assertTrue(body.contains("--batch"));
    assertTrue(body.contains("--changeset"));
    assertTrue(body.indexOf("--changeset") != body.lastIndexOf("--changeset"));
    assertFalse(body.contains("HTTP/1.1 200 OK" + "\r\n"));
    assertTrue(body.contains("Content-Type: application/http" + "\r\n"));
    assertTrue(body.contains("Content-Transfer-Encoding: binary" + "\r\n"));
    assertTrue(body.contains("HTTP/1.1 204 No Content" + "\r\n"));
    assertTrue(body.contains("Content-Type: multipart/mixed; boundary=changeset"));

  }

  @Test
  public void testContentIdEchoing() throws BatchException, IOException {
    List<BatchResponsePart> parts = new ArrayList<BatchResponsePart>();
    ODataResponse response = ODataResponse.entity("Walter Winter")
        .status(HttpStatusCodes.OK)
        .contentHeader("application/json")
        .header(BatchHelper.MIME_HEADER_CONTENT_ID, "mimeHeaderContentId123")
        .header(BatchHelper.REQUEST_HEADER_CONTENT_ID, "requestHeaderContentId123")
        .build();
    List<ODataResponse> responses = new ArrayList<ODataResponse>(1);
    responses.add(response);
    parts.add(BatchResponsePart.responses(responses).changeSet(false).build());
    BatchResponseWriter writer = new BatchResponseWriter();
    ODataResponse batchResponse = writer.writeResponse(parts);

    assertEquals(202, batchResponse.getStatus().getStatusCode());
    assertNotNull(batchResponse.getEntity());
    String body = (String) batchResponse.getEntity();

    String mimeHeader = "Content-Type: application/http" + "\r\n"
        + "Content-Transfer-Encoding: binary" + "\r\n"
        + "Content-Id: mimeHeaderContentId123" + "\r\n";

    String requestHeader = "Content-Id: requestHeaderContentId123" + "\r\n"
        + "Content-Type: application/json" + "\r\n"
        + "Content-Length: 13" + "\r\n";

    assertTrue(body.contains(mimeHeader));
    assertTrue(body.contains(requestHeader));
  }

}
