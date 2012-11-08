package com.sap.core.odata.ref.fit.test;

import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.UriParserException;
import com.sap.core.odata.core.rest.ODataLocatorImpl;
import com.sap.core.odata.ref.processor.ScenarioServiceFactory;

/**
 * @author SAP AG
 */
public class BasicTest {

  private Response call(final List<PathSegment> pathSegments, final HttpHeaders httpHeaders, final UriInfo uriInfo, final Request request) throws ODataException {
    final ODataLocatorImpl oDataLocator = new ODataLocatorImpl();
    oDataLocator.initializeService(new ScenarioServiceFactory(),
        pathSegments,
        httpHeaders,
        uriInfo,
        request);
    return oDataLocator.handleGet();
  }

  private UriInfo getUriInfo() {
    return new UriInfo() {

      @Override
      public UriBuilder getRequestUriBuilder() {
        return null;
      }

      @Override
      public URI getRequestUri() {
        return null;
      }

      @Override
      public MultivaluedMap<String, String> getQueryParameters(final boolean decode) {
        return null;
      }

      @Override
      public MultivaluedMap<String, String> getQueryParameters() {
        return new MultivaluedHashMap<String, String>();
      }

      @Override
      public List<PathSegment> getPathSegments(final boolean decode) {
        return null;
      }

      @Override
      public List<PathSegment> getPathSegments() {
        return null;
      }

      @Override
      public MultivaluedMap<String, String> getPathParameters(final boolean decode) {
        return null;
      }

      @Override
      public MultivaluedMap<String, String> getPathParameters() {
        return null;
      }

      @Override
      public String getPath(boolean decode) {
        return null;
      }

      @Override
      public String getPath() {
        return null;
      }

      @Override
      public List<String> getMatchedURIs(final boolean decode) {
        return null;
      }

      @Override
      public List<String> getMatchedURIs() {
        return null;
      }

      @Override
      public List<Object> getMatchedResources() {
        return null;
      }

      @Override
      public UriBuilder getBaseUriBuilder() {
        return null;
      }

      @Override
      public URI getBaseUri() {
        return null;
      }

      @Override
      public UriBuilder getAbsolutePathBuilder() {
        return null;
      }

      @Override
      public URI getAbsolutePath() {
        return null;
      }
    };
  }

  private List<PathSegment> getPathSegments(final String resourcePath) throws UriParserException {
    List<PathSegment> pathSegments = new ArrayList<PathSegment>();
    for (final String segment : resourcePath.split("/", -1))
      pathSegments.add(getPathSegment(segment));
    return pathSegments;
  }

  private PathSegment getPathSegment(final String segment) {
    return new PathSegment() {

      @Override
      public String getPath() {
        return segment;
      }

      @Override
      public MultivaluedMap<String, String> getMatrixParameters() {
        return null;
      }
    };
  }

  private void checkUrl(final String urlString) throws ODataException {
    final Response response = call(getPathSegments(urlString), null, getUriInfo(), null);
    assertNotNull(response);
  }

  @Test
  public void checkUrls() throws Exception {
    checkUrl("/");

//    checkUrl("Managers('1')/$links/nm_Employees");
//    checkUrl("Managers('1')/$links/nm_Employees()");
//    checkUrl("Managers('1')/$links/nm_Employees('2')");
    checkUrl("Employees('1')/ne_Room/nr_Employees");
    checkUrl("Employees('1')/ne_Room/nr_Employees()");
    checkUrl("Employees('2')/ne_Team/nt_Employees('1')");

//    checkUrl("Employees('2')/ne_Team/nt_Employees('1')/Location");
//    checkUrl("Employees('2')/ne_Team/nt_Employees('1')/Location/City/CityName");
//    checkUrl("Employees('2')/ne_Team/nt_Employees('1')/Location/City/CityName/$value");
//    checkUrl("Employees('2')/ne_Team/nt_Employees('1')/$links/ne_Room");
//    checkUrl("Employees('2')/ne_Team/nt_Employees('1')/ne_Room/$links/nr_Employees");

    checkUrl("Employees('2')/ne_Team/nt_Employees('3')/ne_Room");
    checkUrl("Employees('2')/ne_Team/nt_Employees('3')/ne_Room/nr_Employees");
    checkUrl("Employees('2')/ne_Manager");
//    checkUrl("Employees('2')/ne_Manager/$links/nm_Employees()");
    checkUrl("Employees('2')/ne_Manager/nm_Employees('3')");
//    checkUrl("Employees('2')/ne_Manager/nm_Employees('3')/Age");
  }
}
