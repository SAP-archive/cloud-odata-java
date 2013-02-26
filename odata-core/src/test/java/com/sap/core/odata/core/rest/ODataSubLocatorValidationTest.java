package com.sap.core.odata.core.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataMethodNotAllowedException;
import com.sap.core.odata.api.exception.ODataUnsupportedMediaTypeException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.part.BatchProcessor;
import com.sap.core.odata.api.processor.part.EntityComplexPropertyProcessor;
import com.sap.core.odata.api.processor.part.EntityLinkProcessor;
import com.sap.core.odata.api.processor.part.EntityLinksProcessor;
import com.sap.core.odata.api.processor.part.EntityMediaProcessor;
import com.sap.core.odata.api.processor.part.EntityProcessor;
import com.sap.core.odata.api.processor.part.EntitySetProcessor;
import com.sap.core.odata.api.processor.part.EntitySimplePropertyProcessor;
import com.sap.core.odata.api.processor.part.EntitySimplePropertyValueProcessor;
import com.sap.core.odata.api.processor.part.FunctionImportProcessor;
import com.sap.core.odata.api.processor.part.FunctionImportValueProcessor;
import com.sap.core.odata.api.processor.part.MetadataProcessor;
import com.sap.core.odata.api.processor.part.ServiceDocumentProcessor;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.core.DispatcherTest;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.commons.ContentType.ODataFormat;
import com.sap.core.odata.core.commons.ODataHttpMethod;
import com.sap.core.odata.core.rest.ODataSubLocator.InitParameter;
import com.sap.core.odata.core.uri.UriInfoImpl;
import com.sap.core.odata.core.uri.UriType;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * Tests for the validation of URI path, query options, and request-body content type.
 * @author SAP AG
 */
public class ODataSubLocatorValidationTest extends BaseTest {

  private Edm edm = null;

  @Before
  public void setEdm() throws ODataException {
    edm = MockFacade.getMockEdm();
  }

  private PathSegment mockPathSegment(final String segment) {
    PathSegment pathSegment = mock(PathSegment.class);
    when(pathSegment.getPath()).thenReturn(segment);
    return pathSegment;
  }

  private List<PathSegment> mockPathSegments(final UriType uriType, final boolean moreNavigation, final boolean isValue) {
    List<String> segments = new ArrayList<String>();

    if (uriType == UriType.URI1 || uriType == UriType.URI15) {
      if (moreNavigation) {
        segments.add("Managers('1')");
        segments.add("nm_Employees");
      } else {
        segments.add("Employees");
      }
    } else if (uriType == UriType.URI2 || uriType == UriType.URI3
        || uriType == UriType.URI4 || uriType == UriType.URI5
        || uriType == UriType.URI16 || uriType == UriType.URI17) {
      if (moreNavigation) {
        segments.add("Managers('1')");
        segments.add("nm_Employees('1')");
      } else {
        segments.add("Employees('1')");
      }
    } else if (uriType == UriType.URI6A || uriType == UriType.URI7A || uriType == UriType.URI50A) {
      segments.add("Managers('1')");
      if (moreNavigation) {
        segments.add("nm_Employees('1')");
        segments.add("ne_Manager");
      }
      if (uriType == UriType.URI7A || uriType == UriType.URI50A)
        segments.add("$links");
      segments.add("nm_Employees('1')");
    } else if (uriType == UriType.URI6B || uriType == UriType.URI7B || uriType == UriType.URI50B) {
      segments.add("Managers('1')");
      if (moreNavigation) {
        segments.add("nm_Employees('1')");
        segments.add("ne_Manager");
      }
      if (uriType == UriType.URI7B || uriType == UriType.URI50B)
        segments.add("$links");
      segments.add("nm_Employees");
    } else if (uriType == UriType.URI8) {
      segments.add("$metadata");
    } else if (uriType == UriType.URI9) {
      segments.add("$batch");
    } else if (uriType == UriType.URI10) {
      segments.add("OldestEmployee");
    } else if (uriType == UriType.URI11) {
      segments.add("AllLocations");
    } else if (uriType == UriType.URI12) {
      segments.add("MostCommonLocation");
    } else if (uriType == UriType.URI13) {
      segments.add("AllUsedRoomIds");
    } else if (uriType == UriType.URI14) {
      segments.add("MaximalAge");
    }

    if (uriType == UriType.URI3 || uriType == UriType.URI4)
      segments.add("Location");
    if (uriType == UriType.URI4)
      segments.add("Country");
    else if (uriType == UriType.URI5)
      segments.add("EmployeeName");

    if (uriType == UriType.URI15 || uriType == UriType.URI16
        || uriType == UriType.URI50A || uriType == UriType.URI50B)
      segments.add("$count");

    if (uriType == UriType.URI17 || isValue)
      segments.add("$value");

    // self-test
    try {
      final UriInfoImpl uriInfo = (UriInfoImpl) UriParser.parse(edm,
          MockFacade.getPathSegmentsAsODataPathSegmentMock(segments),
          Collections.<String, String> emptyMap());
      assertEquals(uriType, uriInfo.getUriType());
      assertEquals(uriType == UriType.URI17 || isValue, uriInfo.isValue());
    } catch (final ODataException e) {
      fail();
    }

    List<PathSegment> pathSegments = new ArrayList<PathSegment>();
    for (final String segment : segments)
      pathSegments.add(mockPathSegment(segment));
    return pathSegments;
  }

  private MultivaluedMap<String, String> mockOptions(
      final boolean format,
      final boolean filter, final boolean inlineCount, final boolean orderBy,
      final boolean skipToken, final boolean skip, final boolean top,
      final boolean expand, final boolean select) {

    MultivaluedMap<String, String> map = new MultivaluedHashMap<String, String>();

    if (format)
      map.add("$format", ODataFormat.XML.toString());
    if (filter)
      map.add("$filter", "true");
    if (inlineCount)
      map.add("$inlinecount", "none");
    if (orderBy)
      map.add("$orderby", "Age");
    if (skipToken)
      map.add("$skiptoken", "x");
    if (skip)
      map.add("$skip", "0");
    if (top)
      map.add("$top", "0");
    if (expand)
      map.add("$expand", "ne_Team");
    if (select)
      map.add("$select", "Age");

    return map;
  }

  private void mockSubLocatorInputForUriInfoTests(final ODataSubLocator locator,
      final List<PathSegment> pathSegments,
      final MultivaluedMap<String, String> queryParameters,
      final String requestContentType) throws Exception {

    InitParameter param = locator.new InitParameter();

    HttpHeaders httpHeaders = mock(HttpHeaders.class);
    final MultivaluedMap<String, String> map = new MultivaluedHashMap<String, String>();
    when(httpHeaders.getRequestHeaders()).thenReturn(map);
    MediaType mediaType = mock(MediaType.class);
    when(mediaType.toString()).thenReturn(requestContentType);
    when(httpHeaders.getMediaType()).thenReturn(mediaType);
    param.setHttpHeaders(httpHeaders);

    param.setPathSplit(0);
    param.setPathSegments(pathSegments);

    UriInfo initUriInfo = mock(UriInfo.class);
    UriBuilder uriBuilder = mock(UriBuilder.class);
    when(uriBuilder.build()).thenReturn(URI.create(""));
    when(initUriInfo.getBaseUriBuilder()).thenReturn(uriBuilder);
    if (queryParameters == null)
      when(initUriInfo.getQueryParameters()).thenReturn(map);
    else
      when(initUriInfo.getQueryParameters()).thenReturn(queryParameters);
    param.setUriInfo(initUriInfo);

    HttpServletRequest servletRequest = mock(HttpServletRequest.class);
    when(servletRequest.getInputStream()).thenReturn(mock(ServletInputStream.class));
    param.setServletRequest(servletRequest);

    ODataServiceFactory serviceFactory = mock(ODataServiceFactory.class);
    mockODataService(serviceFactory);

    param.setServiceFactory(serviceFactory);

    locator.initialize(param);
  }

  private void mockODataService(final ODataServiceFactory serviceFactory) throws ODataException {
    ODataService service = DispatcherTest.getMockService();
    when(service.getEntityDataModel()).thenReturn(edm);
    when(service.getProcessor()).thenReturn(mock(ODataProcessor.class));
    when(serviceFactory.createService(Matchers.any(ODataContext.class))).thenReturn(service);

    Mockito.when(service.getSupportedContentTypes(BatchProcessor.class)).thenReturn(
        Arrays.asList(HttpContentType.MULTIPART_MIXED));

    Mockito.when(service.getSupportedContentTypes(EntityProcessor.class)).thenReturn(Arrays.asList(
        HttpContentType.APPLICATION_ATOM_XML_ENTRY_UTF8,
        HttpContentType.APPLICATION_ATOM_XML_UTF8,
        HttpContentType.APPLICATION_JSON_UTF8,
        HttpContentType.APPLICATION_XML_UTF8));

    Mockito.when(service.getSupportedContentTypes(FunctionImportProcessor.class)).thenReturn(Arrays.asList(
        HttpContentType.APPLICATION_JSON_UTF8,
        HttpContentType.APPLICATION_XML_UTF8));
    Mockito.when(service.getSupportedContentTypes(EntityLinkProcessor.class)).thenReturn(Arrays.asList(
        HttpContentType.APPLICATION_JSON_UTF8,
        HttpContentType.APPLICATION_XML_UTF8));
    Mockito.when(service.getSupportedContentTypes(EntityLinksProcessor.class)).thenReturn(Arrays.asList(
        HttpContentType.APPLICATION_JSON_UTF8,
        HttpContentType.APPLICATION_XML_UTF8));
    Mockito.when(service.getSupportedContentTypes(EntitySimplePropertyProcessor.class)).thenReturn(Arrays.asList(
        HttpContentType.APPLICATION_JSON_UTF8,
        HttpContentType.APPLICATION_XML_UTF8));
    Mockito.when(service.getSupportedContentTypes(EntityComplexPropertyProcessor.class)).thenReturn(Arrays.asList(
        HttpContentType.APPLICATION_JSON_UTF8,
        HttpContentType.APPLICATION_XML_UTF8));

    Mockito.when(service.getSupportedContentTypes(EntityMediaProcessor.class)).thenReturn(Arrays.asList(
        HttpContentType.WILDCARD));
    Mockito.when(service.getSupportedContentTypes(EntitySimplePropertyValueProcessor.class)).thenReturn(Arrays.asList(
        HttpContentType.WILDCARD));
    Mockito.when(service.getSupportedContentTypes(FunctionImportValueProcessor.class)).thenReturn(Arrays.asList(
        HttpContentType.WILDCARD));

    Mockito.when(service.getSupportedContentTypes(EntitySetProcessor.class)).thenReturn(Arrays.asList(
        HttpContentType.APPLICATION_ATOM_XML_FEED_UTF8,
        HttpContentType.APPLICATION_ATOM_XML_UTF8,
        HttpContentType.APPLICATION_JSON_UTF8,
        HttpContentType.APPLICATION_XML_UTF8));

    Mockito.when(service.getSupportedContentTypes(MetadataProcessor.class)).thenReturn(Arrays.asList(
        HttpContentType.APPLICATION_XML_UTF8));

    Mockito.when(service.getSupportedContentTypes(ServiceDocumentProcessor.class)).thenReturn(Arrays.asList(
        HttpContentType.APPLICATION_ATOM_SVC_UTF8,
        HttpContentType.APPLICATION_JSON_UTF8,
        HttpContentType.APPLICATION_XML_UTF8));
  }

  private void checkRequest(final ODataHttpMethod method,
      final List<PathSegment> pathSegments,
      final MultivaluedMap<String, String> queryParameters,
      final String requestContentType) throws Exception {

    ODataSubLocator locator = new ODataSubLocator();
    mockSubLocatorInputForUriInfoTests(locator, pathSegments, queryParameters, requestContentType);
    switch (method) {
    case GET:
      locator.handleGet();
      break;
    case POST:
      locator.handlePost(null);
      break;
    case PUT:
      locator.handlePut();
      break;
    case DELETE:
      locator.handleDelete();
      break;
    case PATCH:
      locator.handlePatch();
      break;
    case MERGE:
      locator.handleMerge();
      break;
    default:
      fail();
    }
  }

  private void checkValueContentType(final ODataHttpMethod method, final UriType uriType, final String requestContentType) throws Exception {
    checkRequest(method, mockPathSegments(uriType, false, true), null, requestContentType);
  }

  private void wrongRequest(final ODataHttpMethod method,
      final List<PathSegment> pathSegments,
      final MultivaluedMap<String, String> queryParameters) {
    try {
      checkRequest(method, pathSegments, queryParameters, null);
      fail("Expected ODataMethodNotAllowedException not thrown");
    } catch (ODataMethodNotAllowedException e) {
      assertNotNull(e);
    } catch (Exception e) {
      fail("Unexpected Exception thrown");
    }
  }

  private void wrongOptions(final ODataHttpMethod method, final UriType uriType,
      final boolean format,
      final boolean filter, final boolean inlineCount, final boolean orderBy,
      final boolean skipToken, final boolean skip, final boolean top,
      final boolean expand, final boolean select) {
    wrongRequest(method,
        mockPathSegments(uriType, false, false),
        mockOptions(format, filter, inlineCount, orderBy, skipToken, skip, top, expand, select));
  }

  private void wrongFunctionHttpMethod(final ODataHttpMethod method, final UriType uriType) {
    if (uriType == UriType.URI1)
      wrongRequest(method, Arrays.asList(mockPathSegment("EmployeeSearch")), null);
    else
      wrongRequest(method, mockPathSegments(uriType, false, false), null);
  }

  private void wrongProperty(final ODataHttpMethod method, final boolean ofComplex, final boolean key, final boolean nullable) {
    EdmProperty property = null;
    try {
      if (ofComplex)
        property = (EdmProperty) edm.getEntityType("RefScenario", "Employee").getProperty("Age");
      else
        property = (EdmProperty) edm.getComplexType("RefScenario", "c_Location").getProperty("Country");
      EdmFacets facets = mock(EdmFacets.class);
      when(facets.isNullable()).thenReturn(nullable);
      when(property.getFacets()).thenReturn(facets);
    } catch (final EdmException e) {
      fail();
    }
    List<PathSegment> pathSegments = new ArrayList<PathSegment>();
    pathSegments.add(mockPathSegment("Employees('1')"));
    if (ofComplex)
      pathSegments.add(mockPathSegment("Location"));
    if (ofComplex)
      pathSegments.add(mockPathSegment("Country"));
    else if (key)
      pathSegments.add(mockPathSegment("EmployeeId"));
    else
      pathSegments.add(mockPathSegment("Age"));

    wrongRequest(method, pathSegments, null);
  }

  private void wrongNavigationPath(final ODataHttpMethod method, final UriType uriType) {
    try {
      checkRequest(method, mockPathSegments(uriType, true, false), null, null);
      fail("Expected ODataBadRequestException not thrown");
    } catch (ODataBadRequestException e) {
      assertNotNull(e);
    } catch (Exception e) {
      fail("Unexpected Exception thrown");
    }
  }

  private void wrongRequestContentType(final ODataHttpMethod method, final UriType uriType, final ContentType requestContentType) throws EdmException, ODataException {
    wrongRequestContentType(method, uriType, false, requestContentType);
  }

  private void wrongRequestContentType(final ODataHttpMethod method, final UriType uriType, final boolean isValue, final ContentType requestContentType) throws EdmException, ODataException {
    wrongRequestContentType(method, uriType, isValue, requestContentType.toContentTypeString());
  }

  private void wrongRequestContentType(final ODataHttpMethod method, final UriType uriType, final boolean isValue, final String requestContentType) throws EdmException, ODataException {
    try {
      checkRequest(method, mockPathSegments(uriType, false, isValue), null, requestContentType);
      fail("Expected ODataException not thrown");
    } catch (ODataUnsupportedMediaTypeException e) {
      assertNotNull(e);
    } catch (Exception e) {
      fail("Unexpected Exception thrown");
    }
  }

  @Test
  public void requestContentTypeMediaResource() throws Exception {
    checkRequest(ODataHttpMethod.PUT, mockPathSegments(UriType.URI2, false, false), null, "image/jpeg");
    checkRequest(ODataHttpMethod.PATCH, mockPathSegments(UriType.URI2, false, false), null, "image/jpeg");
    checkRequest(ODataHttpMethod.MERGE, mockPathSegments(UriType.URI2, false, false), null, "image/jpeg");
  }

  @Test
  public void requestContentType() throws Exception {
    checkValueContentType(ODataHttpMethod.PUT, UriType.URI4, HttpContentType.TEXT_PLAIN);
    checkValueContentType(ODataHttpMethod.DELETE, UriType.URI4, HttpContentType.TEXT_PLAIN);
    checkValueContentType(ODataHttpMethod.PATCH, UriType.URI4, HttpContentType.TEXT_PLAIN);
    checkValueContentType(ODataHttpMethod.MERGE, UriType.URI4, HttpContentType.TEXT_PLAIN);
    checkValueContentType(ODataHttpMethod.PUT, UriType.URI4, HttpContentType.TEXT_PLAIN_UTF8);
    checkValueContentType(ODataHttpMethod.DELETE, UriType.URI4, HttpContentType.TEXT_PLAIN_UTF8);
    checkValueContentType(ODataHttpMethod.PATCH, UriType.URI4, HttpContentType.TEXT_PLAIN_UTF8);
    checkValueContentType(ODataHttpMethod.MERGE, UriType.URI4, HttpContentType.TEXT_PLAIN_UTF8);

    checkValueContentType(ODataHttpMethod.PUT, UriType.URI5, HttpContentType.TEXT_PLAIN);
    checkValueContentType(ODataHttpMethod.DELETE, UriType.URI5, HttpContentType.TEXT_PLAIN);
    checkValueContentType(ODataHttpMethod.PATCH, UriType.URI5, HttpContentType.TEXT_PLAIN);
    checkValueContentType(ODataHttpMethod.MERGE, UriType.URI5, HttpContentType.TEXT_PLAIN);
  }

  @Test
  public void notAllowedOptions() throws Exception {
    wrongOptions(ODataHttpMethod.POST, UriType.URI1, true, false, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI1, false, true, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI1, false, false, true, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI1, false, false, false, true, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI1, false, false, false, false, true, false, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI1, false, false, false, false, false, true, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI1, false, false, false, false, false, false, true, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI1, false, false, false, false, false, false, false, true, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI1, false, false, false, false, false, false, false, false, true);

    wrongOptions(ODataHttpMethod.PUT, UriType.URI2, true, false, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.PUT, UriType.URI2, false, false, false, false, false, false, false, true, false);
    wrongOptions(ODataHttpMethod.PUT, UriType.URI2, false, false, false, false, false, false, false, false, true);
    wrongOptions(ODataHttpMethod.PATCH, UriType.URI2, true, false, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.PATCH, UriType.URI2, false, false, false, false, false, false, false, true, false);
    wrongOptions(ODataHttpMethod.PATCH, UriType.URI2, false, false, false, false, false, false, false, false, true);
    wrongOptions(ODataHttpMethod.DELETE, UriType.URI2, true, false, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.DELETE, UriType.URI2, false, true, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.DELETE, UriType.URI2, false, false, false, false, false, false, false, true, false);
    wrongOptions(ODataHttpMethod.DELETE, UriType.URI2, false, false, false, false, false, false, false, false, true);

    wrongOptions(ODataHttpMethod.PUT, UriType.URI3, true, false, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.PATCH, UriType.URI3, true, false, false, false, false, false, false, false, false);

    wrongOptions(ODataHttpMethod.PUT, UriType.URI4, true, false, false, false, false, false, false, false, false);

    wrongOptions(ODataHttpMethod.PUT, UriType.URI5, true, false, false, false, false, false, false, false, false);

    wrongOptions(ODataHttpMethod.POST, UriType.URI6B, true, false, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI6B, false, true, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI6B, false, false, true, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI6B, false, false, false, true, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI6B, false, false, false, false, true, false, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI6B, false, false, false, false, false, true, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI6B, false, false, false, false, false, false, true, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI6B, false, false, false, false, false, false, false, true, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI6B, false, false, false, false, false, false, false, false, true);

    wrongOptions(ODataHttpMethod.PUT, UriType.URI7A, true, false, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.PUT, UriType.URI7A, false, true, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.DELETE, UriType.URI7A, true, false, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.DELETE, UriType.URI7A, false, true, false, false, false, false, false, false, false);

    wrongOptions(ODataHttpMethod.POST, UriType.URI7B, true, false, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI7B, false, true, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI7B, false, false, true, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI7B, false, false, false, false, true, false, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI7B, false, false, false, false, false, true, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI7B, false, false, false, false, false, false, true, false, false);

    wrongOptions(ODataHttpMethod.PUT, UriType.URI17, false, true, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.DELETE, UriType.URI17, true, false, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.DELETE, UriType.URI17, false, true, false, false, false, false, false, false, false);
  }

  @Test
  public void functionImportWrongHttpMethod() throws Exception {
    wrongFunctionHttpMethod(ODataHttpMethod.POST, UriType.URI1);
    wrongFunctionHttpMethod(ODataHttpMethod.PUT, UriType.URI10);
    wrongFunctionHttpMethod(ODataHttpMethod.POST, UriType.URI11);
    wrongFunctionHttpMethod(ODataHttpMethod.PATCH, UriType.URI12);
    wrongFunctionHttpMethod(ODataHttpMethod.POST, UriType.URI13);
    wrongFunctionHttpMethod(ODataHttpMethod.PUT, UriType.URI14);
  }

  @Test
  public void wrongProperty() throws Exception {
    wrongProperty(ODataHttpMethod.DELETE, true, false, false);

    wrongProperty(ODataHttpMethod.PUT, false, true, false);
    wrongProperty(ODataHttpMethod.PATCH, false, true, false);
    wrongProperty(ODataHttpMethod.DELETE, false, true, true);
    wrongProperty(ODataHttpMethod.DELETE, false, true, false);
    wrongProperty(ODataHttpMethod.DELETE, false, false, false);
  }

  @Test
  public void wrongNavigationPath() throws Exception {
    wrongNavigationPath(ODataHttpMethod.PUT, UriType.URI3);
    wrongNavigationPath(ODataHttpMethod.PATCH, UriType.URI3);

    wrongNavigationPath(ODataHttpMethod.PUT, UriType.URI4);
    wrongNavigationPath(ODataHttpMethod.PATCH, UriType.URI4);
    wrongNavigationPath(ODataHttpMethod.DELETE, UriType.URI4);

    wrongNavigationPath(ODataHttpMethod.PUT, UriType.URI5);
    wrongNavigationPath(ODataHttpMethod.PATCH, UriType.URI5);
    wrongNavigationPath(ODataHttpMethod.DELETE, UriType.URI5);

    wrongNavigationPath(ODataHttpMethod.PUT, UriType.URI7A);
    wrongNavigationPath(ODataHttpMethod.PATCH, UriType.URI7A);
    wrongNavigationPath(ODataHttpMethod.DELETE, UriType.URI7A);

    wrongNavigationPath(ODataHttpMethod.POST, UriType.URI6B);

    wrongNavigationPath(ODataHttpMethod.POST, UriType.URI7B);

    wrongNavigationPath(ODataHttpMethod.PUT, UriType.URI17);
    wrongNavigationPath(ODataHttpMethod.DELETE, UriType.URI17);
  }

  @Test
  public void wrongRequestContentType() throws Exception {
    wrongRequestContentType(ODataHttpMethod.POST, UriType.URI1, ContentType.APPLICATION_ATOM_SVC);
    wrongRequestContentType(ODataHttpMethod.POST, UriType.URI1, ContentType.APPLICATION_ATOM_SVC_CS_UTF_8);

    wrongRequestContentType(ODataHttpMethod.PUT, UriType.URI2, ContentType.APPLICATION_ATOM_SVC);
    wrongRequestContentType(ODataHttpMethod.PUT, UriType.URI2, ContentType.APPLICATION_ATOM_SVC_CS_UTF_8);
    wrongRequestContentType(ODataHttpMethod.PUT, UriType.URI2, ContentType.APPLICATION_ATOM_SVC);
    wrongRequestContentType(ODataHttpMethod.PUT, UriType.URI2, ContentType.APPLICATION_ATOM_SVC_CS_UTF_8);

    wrongRequestContentType(ODataHttpMethod.PUT, UriType.URI5, true, ContentType.APPLICATION_ATOM_SVC);
    wrongRequestContentType(ODataHttpMethod.PUT, UriType.URI5, true, ContentType.APPLICATION_ATOM_SVC_CS_UTF_8);
  }
}