package com.sap.core.odata.core.batch;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sap.core.odata.api.batch.BatchException;
import com.sap.core.odata.api.client.batch.BatchChangeSet;
import com.sap.core.odata.api.client.batch.BatchChangeSetPart;
import com.sap.core.odata.api.client.batch.BatchPart;
import com.sap.core.odata.api.client.batch.BatchQueryPart;
import com.sap.core.odata.testutil.helper.StringHelper;

public class BatchRequestWriterTest {

  private static final String POST = "POST";
  private static final String GET = "GET";
  private static final String PUT = "PUT";
  private static final String BOUNDARY = "batch_123";

  private void checkMimeHeaders(final String requestBody) {
    assertTrue(requestBody.contains("Content-Type: application/http"));
    assertTrue(requestBody.contains("Content-Transfer-Encoding: binary"));
  }

  @Test
  public void testBatchQueryPart() throws BatchException, IOException {
    List<BatchPart> batch = new ArrayList<BatchPart>();
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("Accept", "application/json");
    BatchPart request = BatchQueryPart.method(GET).uri("Employees").headers(headers).build();
    batch.add(request);

    BatchRequestWriter writer = new BatchRequestWriter();
    InputStream batchRequest = writer.writeBatchRequest(batch, BOUNDARY);

    String requestBody = StringHelper.inputStreamToString(batchRequest);
    assertNotNull(batchRequest);
    checkMimeHeaders(requestBody);

    assertTrue(requestBody.contains("--batch_"));
    assertTrue(requestBody.contains("GET Employees HTTP/1.1"));
    checkHeaders(headers, requestBody);
  }

  @Test
  public void testBatchChangeSet() throws IOException, BatchException {
    List<BatchPart> batch = new ArrayList<BatchPart>();
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("content-type", "application/json");
    BatchChangeSetPart request = BatchChangeSetPart.method(PUT)
        .uri("Employees('2')")
        .body("{\"Возраст\":40}")
        .headers(headers)
        .contentId("111")
        .build();
    BatchChangeSet changeSet = BatchChangeSet.newBuilder().build();
    changeSet.add(request);
    batch.add(changeSet);

    BatchRequestWriter writer = new BatchRequestWriter();
    InputStream batchRequest = writer.writeBatchRequest(batch, BOUNDARY);

    String requestBody = StringHelper.inputStreamToString(batchRequest, true);
    assertNotNull(batchRequest);
    checkMimeHeaders(requestBody);
    checkHeaders(headers, requestBody);

    assertTrue(requestBody.contains("--batch_"));
    assertTrue(requestBody.contains("--changeset_"));
    assertTrue(requestBody.contains("PUT Employees('2') HTTP/1.1"));
    assertTrue(requestBody.contains("{\"Возраст\":40}"));
  }

  @Test
  public void testBatchWithGetAndPost() throws BatchException, IOException {
    List<BatchPart> batch = new ArrayList<BatchPart>();
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("Accept", "application/json");
    BatchPart request = BatchQueryPart.method(GET).uri("Employees").headers(headers).contentId("000").build();
    batch.add(request);

    Map<String, String> changeSetHeaders = new HashMap<String, String>();
    changeSetHeaders.put("content-type", "application/json");
    String body = "/9j/4AAQSkZJRgABAQEBLAEsAAD/4RM0RXhpZgAATU0AKgAAAAgABwESAAMAAAABAAEA";
    BatchChangeSetPart changeRequest = BatchChangeSetPart.method(POST)
        .uri("Employees")
        .body(body)
        .headers(changeSetHeaders)
        .contentId("111")
        .build();
    BatchChangeSet changeSet = BatchChangeSet.newBuilder().build();
    changeSet.add(changeRequest);
    batch.add(changeSet);
    BatchRequestWriter writer = new BatchRequestWriter();
    InputStream batchRequest = writer.writeBatchRequest(batch, BOUNDARY);

    String requestBody = StringHelper.inputStreamToString(batchRequest);
    assertNotNull(batchRequest);
    checkMimeHeaders(requestBody);

    checkHeaders(headers, requestBody);
    checkHeaders(changeSetHeaders, requestBody);
    assertTrue(requestBody.contains("GET Employees HTTP/1.1"));
    assertTrue(requestBody.contains("POST Employees HTTP/1.1"));
    assertTrue(requestBody.contains(body));
  }

  @Test
  public void testChangeSetWithContentIdReferencing() throws BatchException, IOException {
    List<BatchPart> batch = new ArrayList<BatchPart>();

    Map<String, String> changeSetHeaders = new HashMap<String, String>();
    changeSetHeaders.put("content-type", "application/json");
    String body = "/9j/4AAQSkZJRgABAQEBLAEsAAD/4RM0RXhpZgAATU0AKgAAAAgABwESAAMAAAABAAEA";
    BatchChangeSetPart changeRequest = BatchChangeSetPart.method(POST)
        .uri("Employees('2')")
        .body(body)
        .headers(changeSetHeaders)
        .contentId("1")
        .build();
    BatchChangeSet changeSet = BatchChangeSet.newBuilder().build();
    changeSet.add(changeRequest);

    changeSetHeaders = new HashMap<String, String>();
    changeSetHeaders.put("content-type", "application/json;odata=verbose");
    BatchChangeSetPart changeRequest2 = BatchChangeSetPart.method(PUT)
        .uri("$/ManagerId")
        .body("{\"ManagerId\":1}")
        .headers(changeSetHeaders)
        .contentId("2")
        .build();
    changeSet.add(changeRequest2);
    batch.add(changeSet);

    BatchRequestWriter writer = new BatchRequestWriter();
    InputStream batchRequest = writer.writeBatchRequest(batch, BOUNDARY);

    String requestBody = StringHelper.inputStreamToString(batchRequest);
    assertNotNull(batchRequest);
    checkMimeHeaders(requestBody);

    assertTrue(requestBody.contains("POST Employees('2') HTTP/1.1"));
    assertTrue(requestBody.contains("PUT $/ManagerId HTTP/1.1"));
    assertTrue(requestBody.contains(BatchHelper.HTTP_CONTENT_ID + ": 1"));
    assertTrue(requestBody.contains(BatchHelper.HTTP_CONTENT_ID + ": 2"));
    assertTrue(requestBody.contains(body));

  }

  @Test
  public void testBatchWithTwoChangeSets() throws BatchException, IOException {
    List<BatchPart> batch = new ArrayList<BatchPart>();

    Map<String, String> changeSetHeaders = new HashMap<String, String>();
    changeSetHeaders.put("content-type", "application/json");
    changeSetHeaders.put("content-Id", "111");
    String body = "/9j/4AAQSkZJRgABAQEBLAEsAAD/4RM0RXhpZgAATU0AKgAAAAgABwESAAMAAAABAAEA";
    BatchChangeSetPart changeRequest = BatchChangeSetPart.method(POST)
        .uri("Employees")
        .body(body)
        .headers(changeSetHeaders)
        .build();
    BatchChangeSet changeSet = BatchChangeSet.newBuilder().build();
    changeSet.add(changeRequest);
    batch.add(changeSet);

    Map<String, String> changeSetHeaders2 = new HashMap<String, String>();
    changeSetHeaders2.put("content-type", "application/json;odata=verbose");
    changeSetHeaders2.put("content-Id", "222");
    BatchChangeSetPart changeRequest2 = BatchChangeSetPart.method(PUT)
        .uri("Employees('2')/ManagerId")
        .body("{\"ManagerId\":1}")
        .headers(changeSetHeaders2)
        .build();
    BatchChangeSet changeSet2 = BatchChangeSet.newBuilder().build();
    changeSet2.add(changeRequest2);
    batch.add(changeSet2);

    BatchRequestWriter writer = new BatchRequestWriter();
    InputStream batchRequest = writer.writeBatchRequest(batch, BOUNDARY);

    String requestBody = StringHelper.inputStreamToString(batchRequest);
    assertNotNull(batchRequest);
    checkMimeHeaders(requestBody);

    assertTrue(requestBody.contains("POST Employees HTTP/1.1"));
    assertTrue(requestBody.contains("PUT Employees('2')/ManagerId HTTP/1.1"));

    assertTrue(requestBody.contains(body));

  }

  private void checkHeaders(final Map<String, String> headers, final String requestBody) {
    for (Map.Entry<String, String> header : headers.entrySet()) {
      assertTrue(requestBody.contains(header.getKey() + ": " + header.getValue()));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBatchQueryPartWithInvalidMethod() throws BatchException, IOException {
    BatchQueryPart.method(PUT).uri("Employees").build();

  }

  @Test(expected = IllegalArgumentException.class)
  public void testBatchChangeSetPartWithInvalidMethod() throws BatchException, IOException {
    BatchChangeSetPart.method(GET).uri("Employees('2')").build();

  }
}
