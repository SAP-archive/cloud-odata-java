package com.sap.core.odata.fit.basic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.batch.BatchHandler;
import com.sap.core.odata.api.batch.BatchPart;
import com.sap.core.odata.api.batch.BatchResponsePart;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderBatchProperties;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.info.GetSimplePropertyUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;
import com.sap.core.odata.core.PathInfoImpl;
import com.sap.core.odata.core.ep.util.CircleStreamBuffer;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;
import com.sap.core.odata.core.processor.ODataSingleProcessorService;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class BasicBatchTest extends AbstractBasicTest {

  private static final String LF = "\n";
  private static final String REG_EX_BOUNDARY = "(([a-zA-Z0-9_\\-\\.'\\+]{1,70})|\"([a-zA-Z0-9_\\-\\.'\\+\\s\\(\\),/:=\\?]{1,69}[a-zA-Z0-9_\\-\\.'\\+\\(\\),/:=\\?])\")";
  private static final String REG_EX = "multipart/mixed;\\s*boundary=" + REG_EX_BOUNDARY + "\\s*";

  private static final String REQUEST_PAYLOAD =
      "--batch_98c1-8b13-36bb" + LF
          + "Content-Type: application/http" + LF
          + "Content-Transfer-Encoding: binary" + LF
          + "Content-Id: mimeHeaderContentId1" + LF
          + LF
          + "GET Employees('1')/EmployeeName HTTP/1.1" + LF
          + "Host: localhost:19000" + LF
          + "Accept: application/atomsvc+xml;q=0.8, application/json;odata=verbose;q=0.5, */*;q=0.1" + LF
          + "Accept-Language: en" + LF
          + "MaxDataServiceVersion: 2.0" + LF
          + "Content-Id: requestHeaderContentId1" + LF
          + LF
          + LF
          + "--batch_98c1-8b13-36bb" + LF
          + "Content-Type: multipart/mixed; boundary=changeset_f980-1cb6-94dd" + LF
          + LF
          + "--changeset_f980-1cb6-94dd" + LF
          + "Content-Type: application/http" + LF
          + "Content-Transfer-Encoding: binary" + LF
          + "Content-Id: mimeHeaderContentId2" + LF
          + LF
          + "PUT Employees('1')/EmployeeName HTTP/1.1" + LF
          + "Host: localhost:19000" + LF
          + "Content-Type: application/json;odata=verbose" + LF
          + "MaxDataServiceVersion: 2.0" + LF
          + "Content-Id: requestHeaderContentId2" + LF
          + LF
          + "{\"EmployeeName\":\"Walter Winter MODIFIED\"}" + LF
          + LF
          + "--changeset_f980-1cb6-94dd--" + LF
          + LF
          + "--batch_98c1-8b13-36bb--";

  @Test
  public void testBatch() throws Exception {
    final HttpPost post = new HttpPost(URI.create(getEndpoint().toString() + "$batch"));
    post.setHeader("Content-Type", "multipart/mixed;boundary=batch_98c1-8b13-36bb");
    HttpEntity entity = new StringEntity(REQUEST_PAYLOAD);
    post.setEntity(entity);
    HttpResponse response = getHttpClient().execute(post);

    assertNotNull(response);
    assertEquals(202, response.getStatusLine().getStatusCode());
    assertEquals("HTTP/1.1", response.getProtocolVersion().toString());
    assertTrue(response.containsHeader("Content-Length"));
    assertTrue(response.containsHeader("Content-Type"));
    assertTrue(response.containsHeader("DataServiceVersion"));
    assertTrue(response.getEntity().getContentType().getValue().matches(REG_EX));
    assertNotNull(response.getEntity().getContent());

    String body = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(body.contains("Content-Id: mimeHeaderContentId1"));
    assertTrue(body.contains("Content-Id: requestHeaderContentId1"));
    assertTrue(body.contains("Content-Id: mimeHeaderContentId2"));
    assertTrue(body.contains("Content-Id: requestHeaderContentId2"));
  }

  static class TestSingleProc extends ODataSingleProcessor {
    @Override
    public ODataResponse executeBatch(final BatchHandler handler, final String requestContentType, final InputStream content) {

      assertFalse(getContext().isInBatchMode());

      ODataResponse batchResponse;
      List<BatchResponsePart> batchResponseParts = new ArrayList<BatchResponsePart>();
      PathInfoImpl pathInfo = new PathInfoImpl();
      try {
        pathInfo.setServiceRoot(new URI("http://localhost:19000/odata"));

        EntityProviderBatchProperties batchProperties = EntityProviderBatchProperties.init().pathInfo(pathInfo).build();
        List<BatchPart> batchParts = EntityProvider.parseBatchRequest(requestContentType, content, batchProperties);
        for (BatchPart batchPart : batchParts) {
          batchResponseParts.add(handler.handleBatchPart(batchPart));
        }
        batchResponse = EntityProvider.writeBatchResponse(batchResponseParts);
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      } catch (ODataException e) {
        throw new RuntimeException(e);
      }
      return batchResponse;
    }

    @Override
    public BatchResponsePart executeChangeSet(final BatchHandler handler, final List<ODataRequest> requests) throws ODataException {
      assertTrue(getContext().isInBatchMode());

      List<ODataResponse> responses = new ArrayList<ODataResponse>();
      for (ODataRequest request : requests) {
        ODataResponse response = handler.handleRequest(request);
        if (response.getStatus().getStatusCode() >= HttpStatusCodes.BAD_REQUEST.getStatusCode()) {
          // Rollback
          List<ODataResponse> errorResponses = new ArrayList<ODataResponse>(1);
          errorResponses.add(response);
          return BatchResponsePart.responses(errorResponses).changeSet(false).build();
        }
        responses.add(response);
      }
      return BatchResponsePart.responses(responses).changeSet(true).build();
    }

    @Override
    public ODataResponse readEntitySimpleProperty(final GetSimplePropertyUriInfo uriInfo, final String contentType) throws ODataException {
      assertTrue(getContext().isInBatchMode());

      CircleStreamBuffer buffer = new CircleStreamBuffer();
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(buffer.getOutputStream()));
      JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
      try {
        jsonStreamWriter.beginObject()
            .name(FormatJson.D)
            .beginObject()
            .namedStringValue("EmployeeName", "Walter Winter")
            .endObject()
            .endObject();
        writer.flush();
        buffer.closeWrite();
      } catch (IOException e) {
        buffer.close();
        throw new RuntimeException(e);
      }

      ODataResponse oDataResponse = ODataResponse.entity(buffer.getInputStream()).status(HttpStatusCodes.OK).contentHeader("application/json").build();
      return oDataResponse;
    }

    @Override
    public ODataResponse updateEntitySimpleProperty(final PutMergePatchUriInfo uriInfo, final InputStream content, final String requestContentType, final String contentType) throws ODataException {
      assertTrue(getContext().isInBatchMode());

      ODataResponse oDataResponse = ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
      return oDataResponse;
    }
  }

  @Override
  protected ODataSingleProcessor createProcessor() throws ODataException {
    return new TestSingleProc();
  }

  @Override
  protected ODataService createService() throws ODataException {
    final EdmProvider provider = createEdmProvider();

    final ODataSingleProcessor processor = createProcessor();

    return new ODataSingleProcessorService(provider, processor) {
      Edm edm = MockFacade.getMockEdm();

      @Override
      public Edm getEntityDataModel() throws ODataException {
        return edm;
      }
    };
  }
}
