package com.sap.core.odata.fit.basic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderBatchProperties;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.part.EntitySimplePropertyProcessor;
import com.sap.core.odata.api.uri.info.GetSimplePropertyUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;
import com.sap.core.odata.core.PathInfoImpl;
import com.sap.core.odata.core.processor.ODataSingleProcessorService;
import com.sap.core.odata.core.uri.UriInfoImpl;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * 
 * @author SAP AG
 */
public class BasicBatchTest extends AbstractBasicTest {

  private static final String REQUEST_PAYLOAD = "--batch_98c1-8b13-36bb" + "\n"
      + "Content-Type: application/http" + "\n"
      + "Content-Transfer-Encoding: binary" + "\n"
      + "\n"
      + "GET Employees('1')/EmployeeName HTTP/1.1" + "\n"
      + "Accept: application/atomsvc+xml;q=0.8, application/json;odata=verbose;q=0.5, */*;q=0.1" + "\n"
      + "Accept-Language: en" + "\n"
      + "MaxDataServiceVersion: 2.0" + "\n"
      + "\n"
      + "\n"
      + "--batch_98c1-8b13-36bb" + "\n"
      + "Content-Type: multipart/mixed; boundary=changeset_f980-1cb6-94dd" + "\n"
      + "\n"
      + "--changeset_f980-1cb6-94dd" + "\n"
      + "Content-Type: application/http" + "\n"
      + "Content-Transfer-Encoding: binary" + "\n"
      + "\n"
      + "PUT Employees('1')/EmployeeName HTTP/1.1" + "\n"
      + "Accept-Language: en" + "\n"
      + "Accept: application/atomsvc+xml;q=0.8, application/json;odata=verbose;q=0.5, */*;q=0.1" + "\n"
      + "Content-Type: application/json;odata=verbose" + "\n"
      + "MaxDataServiceVersion: 2.0" + "\n"
      + "\n"
      + "{\"EmployeeName\":\"Walter Winter MODIFIED\"}" + "\n"
      + "\n"
      + "--changeset_f980-1cb6-94dd--" + "\n"
      + "\n"
      + "--batch_98c1-8b13-36bb--";

  @Test
  public void testBatch() throws Exception {
    final HttpPost post = new HttpPost(URI.create(getEndpoint().toString() + "$batch"));
    post.setHeader("Content-Type", "multipart/mixed");
    HttpEntity entity = new StringEntity(REQUEST_PAYLOAD);
    post.setEntity(entity);
    HttpResponse response = getHttpClient().execute(post);

    assertNotNull(response);
    assertEquals(202, response.getStatusLine().getStatusCode());
    assertEquals("HTTP/1.1", response.getProtocolVersion().toString());
    assertEquals(true, response.getEntity().getContentType().getValue().contains("multipart/mixed"));
    Scanner scanner = new Scanner((InputStream) response.getEntity().getContent()).useDelimiter("\n");
    while (scanner.hasNext()) {
      System.out.println(scanner.next());
    }
    scanner.close();
  }

  static class TestSingleProc extends ODataSingleProcessor {
    @SuppressWarnings("unchecked")
    @Override
    public ODataResponse executeBatch(final String contentType, final InputStream content) throws ODataException {
      ODataResponse response;

      String tempContentType = "multipart/mixed;boundary=batch_98c1-8b13-36bb";
      try {
        PathInfoImpl pathInfo = new PathInfoImpl();
        pathInfo.setServiceRoot(new URI("http://localhost/odata"));
        ODataServiceFactory factory = Mockito.mock(ODataServiceFactory.class);
        ODataService service = Mockito.mock(ODataService.class);
        when(service.getProcessor()).thenReturn(new TestSingleProc());
        Edm edm = MockFacade.getMockEdm();
        EntitySimplePropertyProcessor entitySimpleProperty = mock(EntitySimplePropertyProcessor.class);
        when(entitySimpleProperty.readEntitySimpleProperty(any(UriInfoImpl.class), anyString())).thenReturn(readEntitySimpleProperty(null, null));
        when(entitySimpleProperty.updateEntitySimpleProperty(any(UriInfoImpl.class), any(InputStream.class), anyString(), anyString())).thenReturn(updateEntitySimpleProperty(null, null, null, null));
        when(service.getEntityDataModel()).thenReturn(edm);
        when(service.getSupportedContentTypes(Matchers.any(Class.class))).thenReturn(Arrays.asList("application/json"));
        when(service.getEntitySimplePropertyProcessor()).thenReturn(entitySimpleProperty);
        when(factory.createService(any(ODataContext.class))).thenReturn(service);

        EntityProviderBatchProperties batchProperties = EntityProviderBatchProperties.init().pathInfo(pathInfo).serviceFactory(factory).build();
        response = EntityProvider.writeBatch(tempContentType, content, batchProperties);
      } catch (EntityProviderException e) {
        throw new RuntimeException(e);
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      } catch (ODataException e) {
        throw new RuntimeException(e);
      }
      return response;
    }

    @Override
    public ODataResponse readEntitySimpleProperty(final GetSimplePropertyUriInfo uriInfo, final String contentType) throws ODataException {
      ODataResponse oDataResponse = ODataResponse.entity("{\"d\":{\"EmployeeName\":\"Walter Winter\"}}").status(HttpStatusCodes.OK).contentHeader("application/json").build();
      return oDataResponse;
    }

    @Override
    public ODataResponse updateEntitySimpleProperty(final PutMergePatchUriInfo uriInfo, final InputStream content, final String requestContentType, final String contentType) throws ODataException {
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

    return new ODataSingleProcessorService(provider, processor) {};
  }
}
