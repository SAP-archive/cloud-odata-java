package com.sap.core.odata.ref.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataHttpException;
import com.sap.core.odata.api.uri.UriParserException;
import com.sap.core.odata.core.ODataLocatorImpl;
import com.sap.core.odata.core.ODataLocatorImpl.InitParameter;
import com.sap.core.odata.ref.processor.ScenarioServiceFactory;

/**
 * @author SAP AG
 */
public abstract class AbstractTest {

  protected static final MediaType IMAGE_JPEG = new MediaType("image", "jpeg");

  private static final ScenarioServiceFactory SERVICE_FACTORY = new ScenarioServiceFactory();

  protected Response call(final String urlString, final Map<String, String> httpHeaders, final Request request, final HttpStatusCodes expectedStatus) throws ODataException {
    final ODataLocatorImpl oDataLocator = new ODataLocatorImpl();
    InitParameter param = oDataLocator.new InitParameter();

    if (httpHeaders != null)
      param.setHttpHeaders(getHttpHeaders(httpHeaders));
    param.setPathSegments(getPathSegments(urlString.split("\\?", -1)[0]));
    param.setRequest(request);
    param.setUriInfo(getUriInfo(param.getPathSegments(), getQueryParameters(urlString)));
    param.setServiceFactory(SERVICE_FACTORY);
    oDataLocator.initialize(param);

    try {
      final Response response = oDataLocator.handleGet();
      assertEquals(expectedStatus.getStatusCode(), response.getStatus());
      if (expectedStatus == HttpStatusCodes.OK) {
        assertTrue(response.hasEntity());
        assertNotNull(response.getEntity());
        assertFalse(response.getEntity().toString().isEmpty());
      } else if (expectedStatus == HttpStatusCodes.NO_CONTENT) {
        assertTrue(response.hasEntity() == false
            || response.getEntity() == null
            || response.getEntity().toString().isEmpty());
      }
      return response;
    } catch (ODataException e) {
      if (e instanceof ODataHttpException)
        assertEquals(expectedStatus, ((ODataHttpException) e).getHttpStatus());
      else if (e instanceof ODataApplicationException)
        assertEquals(expectedStatus, ((ODataApplicationException) e).getHttpStatus());
      else
        assertEquals(expectedStatus, HttpStatusCodes.INTERNAL_SERVER_ERROR);
      return null;
    }
  }

  protected Response callUrl(final String urlString) throws ODataException {
    return call(urlString, null, null, HttpStatusCodes.OK);
  }

  protected Response badRequest(final String urlString) throws ODataException {
    return call(urlString, null, null, HttpStatusCodes.BAD_REQUEST);
  }

  protected Response notFound(final String urlString) throws ODataException {
    return call(urlString, null, null, HttpStatusCodes.NOT_FOUND);
  }

  protected void checkMediaType(final Response response, final MediaType expectedMediaType) {
    assertTrue(expectedMediaType.isCompatible(response.getMediaType()));
  }

  protected void checkEtag(final Response response, final boolean expectedWeak, final String expectedEtag) {
    final EntityTag entityTag = response.getEntityTag();
    assertNotNull(entityTag);
    assertTrue(entityTag.isWeak() == expectedWeak);
    assertTrue(entityTag.getValue().equals(expectedEtag));
  }

  private HttpHeaders getHttpHeaders(final Map<String, String> httpHeaders) {
    HttpHeaders headers = mock(HttpHeaders.class);
    when(headers.getRequestHeaders()).thenReturn(new MultivaluedHashMap<String, String>(httpHeaders));
    when(headers.getAcceptableLanguages()).thenReturn(Collections.<Locale> emptyList());
    return headers;
  }

  private UriInfo getUriInfo(final List<PathSegment> pathSegments, final Map<String, String> queryParameters) {
    UriInfo uriInfo = mock(UriInfo.class);
    when(uriInfo.getQueryParameters()).thenReturn(new MultivaluedHashMap<String, String>(queryParameters));
    when(uriInfo.getPathSegments()).thenReturn(pathSegments);
    return uriInfo;
  }

  private List<PathSegment> getPathSegments(final String resourcePath) throws UriParserException {
    List<PathSegment> pathSegments = new ArrayList<PathSegment>();
    for (final String segment : resourcePath.split("/", -1))
      pathSegments.add(getPathSegment(unescape(segment)));
    return pathSegments;
  }

  private PathSegment getPathSegment(final String segment) {
    PathSegment pathsegment = mock(PathSegment.class);
    when(pathsegment.getPath()).thenReturn(segment);
    return pathsegment;
  }

  private Map<String, String> getQueryParameters(final String urlString) throws UriParserException {
    Map<String, String> queryParameters = new HashMap<String, String>();
    if (urlString.contains("?")) {
      final String querystring = unescape(urlString.split("\\?", -1)[1]);
      for (final String option : querystring.split("&")) {
        final String[] keyAndValue = option.split("=");
        queryParameters.put(keyAndValue[0], keyAndValue[1]);
      }
    }
    return queryParameters;
  }

  private String unescape(final String s) throws UriParserException {
    try {
      return new URI(s).getPath();
    } catch (URISyntaxException e) {
      throw new UriParserException(UriParserException.NOTEXT);
    }
  }
}