package com.sap.core.odata.core.rest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataMethodNotAllowedException;
import com.sap.core.odata.api.exception.ODataUnsupportedMediaTypeException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.NavigationPropertySegment;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.core.Dispatcher;
import com.sap.core.odata.core.ODataContextImpl;
import com.sap.core.odata.core.PathInfoImpl;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.commons.ContentType.ODataFormat;
import com.sap.core.odata.core.commons.ODataHttpMethod;
import com.sap.core.odata.core.uri.UriInfoImpl;
import com.sap.core.odata.core.uri.UriParserImpl;
import com.sap.core.odata.core.uri.UriType;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class ODataSubLocatorValidationTest extends BaseTest {

  private UriInfoImpl mockUriInfo(final UriType uriType, final boolean isValue) throws EdmException {
    UriInfoImpl uriInfo = mock(UriInfoImpl.class);
    when(uriInfo.getUriType()).thenReturn(uriType);
    when(uriInfo.isValue()).thenReturn(isValue);
    when(uriInfo.getPropertyPath()).thenReturn(Arrays.asList(mock(EdmProperty.class)));
    EdmEntityType entityType = mock(EdmEntityType.class);
    when(entityType.getKeyProperties()).thenReturn(Arrays.asList(mock(EdmProperty.class)));
    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getEntityType()).thenReturn(entityType);
    when(uriInfo.getTargetEntitySet()).thenReturn(entitySet);
    if (uriType == UriType.URI6A || uriType == UriType.URI6B
        || uriType == UriType.URI7A || uriType == UriType.URI7B)
      mockNavigationPath(uriInfo, true);
    when(uriInfo.getSkip()).thenReturn(null);
    when(uriInfo.getTop()).thenReturn(null);

    return uriInfo;
  }

  private UriInfoImpl mockFunctionImport(UriInfoImpl uriInfo, final ODataHttpMethod httpMethod) throws EdmException {
    EdmFunctionImport functionImport = mock(EdmFunctionImport.class);
    when(functionImport.getHttpMethod()).thenReturn(httpMethod == null ? null : httpMethod.toString());
    when(uriInfo.getFunctionImport()).thenReturn(functionImport);

    return uriInfo;
  }

  private UriInfoImpl mockNavigationPath(UriInfoImpl uriInfo, final boolean oneSegment) {
    when(uriInfo.getNavigationSegments()).thenReturn(
        oneSegment ? Arrays.asList(mock(NavigationSegment.class)) :
            Arrays.asList(mock(NavigationSegment.class), mock(NavigationSegment.class)));
    return uriInfo;
  }

  private UriInfoImpl mockOptions(UriInfoImpl uriInfo,
      final boolean format,
      final boolean filter, final boolean inlineCount, final boolean orderBy,
      final boolean skipToken, final boolean skip, final boolean top,
      final boolean expand, final boolean select) {
    if (format)
      when(uriInfo.getFormat()).thenReturn(ODataFormat.XML.toString());
    if (filter)
      when(uriInfo.getFilter()).thenReturn(mock(FilterExpression.class));
    if (inlineCount)
      when(uriInfo.getInlineCount()).thenReturn(InlineCount.ALLPAGES);
    if (orderBy)
      when(uriInfo.getOrderBy()).thenReturn(mock(OrderByExpression.class));
    if (skipToken)
      when(uriInfo.getSkipToken()).thenReturn("x");
    if (skip)
      when(uriInfo.getSkip()).thenReturn(0);
    if (top)
      when(uriInfo.getTop()).thenReturn(0);
    if (expand) {
      ArrayList<NavigationPropertySegment> segments = new ArrayList<NavigationPropertySegment>();
      segments.add(mock(NavigationPropertySegment.class));
      List<ArrayList<NavigationPropertySegment>> expandList = new ArrayList<ArrayList<NavigationPropertySegment>>();
      expandList.add(segments);
      when(uriInfo.getExpand()).thenReturn(expandList);
    }
    if (select)
      when(uriInfo.getSelect()).thenReturn(Arrays.asList(mock(SelectItem.class)));

    return uriInfo;
  }

  private static UriInfoImpl mockProperty(UriInfoImpl uriInfo, final boolean key, final boolean nullable) throws EdmException {
    EdmProperty property = uriInfo.getPropertyPath().get(0);
    if (key)
      uriInfo.getPropertyPath().set(0, uriInfo.getTargetEntitySet().getEntityType().getKeyProperties().get(0));
    EdmFacets facets = mock(EdmFacets.class);
    when(facets.isNullable()).thenReturn(nullable);
    when(property.getFacets()).thenReturn(facets);

    return uriInfo;
  }

  @SuppressWarnings("unchecked")
  private void mockSubLocatorInputForUriInfoTests(ODataSubLocator locator, final ODataHttpMethod method, final UriInfoImpl uriInfo, final String requestContentType) throws Exception {
    UriParser parser = Mockito.mock(UriParserImpl.class);
    setField(locator, "uriParser", parser);
    Mockito.when(parser.parse(Mockito.anyList(), Mockito.anyMap())).thenReturn(uriInfo);

    setField(locator, "requestContentType", requestContentType);

    ODataService service = Mockito.mock(ODataService.class);
    setField(locator, "service", service);
    Mockito.when(service.getSupportedContentTypes(Mockito.any(Class.class))).thenReturn(Arrays.asList(ContentType.WILDCARD.toString()));

    ODataContextImpl context = Mockito.mock(ODataContextImpl.class);
    setField(locator, "context", context);
    Mockito.when(context.getPathInfo()).thenReturn(new PathInfoImpl());

    setField(locator, "acceptHeaderContentTypes", Arrays.asList(ContentType.WILDCARD));

    ODataResponse odataResponse = Mockito.mock(ODataResponse.class);
    Mockito.when(odataResponse.getStatus()).thenReturn(HttpStatusCodes.PAYMENT_REQUIRED);
    Mockito.when(odataResponse.getEntity()).thenReturn("");

    Dispatcher dispatcher = Mockito.mock(Dispatcher.class);
    setField(locator, "dispatcher", dispatcher);
    Mockito.when(dispatcher.dispatch(Mockito.refEq(method),
        Mockito.any(UriInfoImpl.class),
        Mockito.any(InputStream.class),
        Mockito.anyString(),
        Mockito.anyString())).thenReturn(odataResponse);
  }

  private static void setField(Object instance, final String fieldname, final Object value) throws SecurityException,
      NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    Field field = instance.getClass().getDeclaredField(fieldname);
    final boolean access = field.isAccessible();
    field.setAccessible(true);
    field.set(instance, value);
    field.setAccessible(access);
  }

  private void checkUriInfo(final ODataHttpMethod method, final UriInfoImpl uriInfo, final String requestContentType) throws Exception {
    ODataSubLocator locator = new ODataSubLocator();
    mockSubLocatorInputForUriInfoTests(locator, method, uriInfo, requestContentType);
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

  private void checkUriInfo(final ODataHttpMethod method, final UriType uriType, final boolean isValue, final String requestContentType) throws Exception {
    checkUriInfo(method, mockUriInfo(uriType, isValue), requestContentType);
  }

  private void wrongOptions(final ODataHttpMethod method, final UriType uriType,
      final boolean format,
      final boolean filter, final boolean inlineCount, final boolean orderBy,
      final boolean skipToken, final boolean skip, final boolean top,
      final boolean expand, final boolean select) {
    try {
      checkUriInfo(method, mockOptions(mockUriInfo(uriType, false),
          format, filter, inlineCount, orderBy, skipToken, skip, top, expand, select), null);
      fail("Expected ODataMethodNotAllowedException not thrown");
    } catch (ODataMethodNotAllowedException e) {
      assertNotNull(e);
    } catch (Exception e) {
      fail("Unexpected Exception thrown");
    }
  }

  private void wrongFunctionHttpMethod(final ODataHttpMethod method, final UriType uriType, final ODataHttpMethod httpMethod) {
    try {
      checkUriInfo(method, mockFunctionImport(mockUriInfo(uriType, false), httpMethod), null);
      fail("Expected ODataMethodNotAllowedException not thrown");
    } catch (ODataMethodNotAllowedException e) {
      assertNotNull(e);
    } catch (Exception e) {
      fail("Unexpected Exception thrown");
    }
  }

  private void wrongProperty(final ODataHttpMethod method, final UriType uriType, final boolean key, final boolean nullable) {
    try {
      checkUriInfo(method, mockProperty(mockUriInfo(uriType, true), key, nullable), null);
      fail("Expected ODataMethodNotAllowedException not thrown");
    } catch (ODataMethodNotAllowedException e) {
      assertNotNull(e);
    } catch (Exception e) {
      fail("Unexpected Exception thrown");
    }
  }

  private void wrongNavigationPath(final ODataHttpMethod method, final UriType uriType) {
    try {
      UriInfoImpl uriInfo = mockUriInfo(uriType, true);
      uriInfo = mockNavigationPath(uriInfo,
          uriType != UriType.URI6A && uriType != UriType.URI6B
              && uriType != UriType.URI7A && uriType != UriType.URI7B);
      checkUriInfo(method, uriInfo, null);
      fail("Expected ODataBadRequestException not thrown");
    } catch (ODataBadRequestException e) {
      assertNotNull(e);
    } catch (Exception e) {
      fail("Unexpected Exception thrown");
    }
  }

  private void wrongRequestContentType(final ODataHttpMethod method, final UriType uriType, ContentType requestContentType) throws EdmException, ODataException {
    wrongRequestContentType(method, uriType, false, requestContentType);
  }

  private void wrongRequestContentType(final ODataHttpMethod method, final UriType uriType, boolean isValue, ContentType requestContentType) throws EdmException, ODataException {
    wrongRequestContentType(method, uriType, isValue, requestContentType.toContentTypeString());
  }

  private void wrongRequestContentType(final ODataHttpMethod method, final UriType uriType, boolean isValue, String requestContentType) throws EdmException, ODataException {
    try {
      checkUriInfo(method, mockUriInfo(uriType, isValue), requestContentType);
      fail("Expected ODataException not thrown");
    } catch (ODataUnsupportedMediaTypeException e) {
      assertNotNull(e);
    } catch (Exception e) {
      fail("Unexpected Exception thrown");
    }
  }

  @Test
  public void requestContentType() throws Exception {
    checkUriInfo(ODataHttpMethod.PUT, UriType.URI4, true, HttpContentType.TEXT_PLAIN);
    checkUriInfo(ODataHttpMethod.DELETE, UriType.URI4, true, HttpContentType.TEXT_PLAIN);
    checkUriInfo(ODataHttpMethod.PATCH, UriType.URI4, true, HttpContentType.TEXT_PLAIN);
    checkUriInfo(ODataHttpMethod.MERGE, UriType.URI4, true, HttpContentType.TEXT_PLAIN);
    checkUriInfo(ODataHttpMethod.PUT, UriType.URI4, true, HttpContentType.TEXT_PLAIN_UTF8);
    checkUriInfo(ODataHttpMethod.DELETE, UriType.URI4, true, HttpContentType.TEXT_PLAIN_UTF8);
    checkUriInfo(ODataHttpMethod.PATCH, UriType.URI4, true, HttpContentType.TEXT_PLAIN_UTF8);
    checkUriInfo(ODataHttpMethod.MERGE, UriType.URI4, true, HttpContentType.TEXT_PLAIN_UTF8);

    checkUriInfo(ODataHttpMethod.PUT, UriType.URI5, true, HttpContentType.TEXT_PLAIN);
    checkUriInfo(ODataHttpMethod.DELETE, UriType.URI5, true, HttpContentType.TEXT_PLAIN);
    checkUriInfo(ODataHttpMethod.PATCH, UriType.URI5, true, HttpContentType.TEXT_PLAIN);
    checkUriInfo(ODataHttpMethod.MERGE, UriType.URI5, true, HttpContentType.TEXT_PLAIN);
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
    wrongOptions(ODataHttpMethod.POST, UriType.URI7B, false, false, false, true, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI7B, false, false, false, false, true, false, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI7B, false, false, false, false, false, true, false, false, false);
    wrongOptions(ODataHttpMethod.POST, UriType.URI7B, false, false, false, false, false, false, true, false, false);

    wrongOptions(ODataHttpMethod.PUT, UriType.URI17, true, false, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.PUT, UriType.URI17, false, true, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.DELETE, UriType.URI17, true, false, false, false, false, false, false, false, false);
    wrongOptions(ODataHttpMethod.DELETE, UriType.URI17, false, true, false, false, false, false, false, false, false);
  }

  @Test
  public void functionImportWrongHttpMethod() throws Exception {
    wrongFunctionHttpMethod(ODataHttpMethod.POST, UriType.URI1, ODataHttpMethod.GET);
    wrongFunctionHttpMethod(ODataHttpMethod.GET, UriType.URI10, ODataHttpMethod.PUT);
    wrongFunctionHttpMethod(ODataHttpMethod.POST, UriType.URI11, ODataHttpMethod.GET);
    wrongFunctionHttpMethod(ODataHttpMethod.PATCH, UriType.URI12, ODataHttpMethod.GET);
    wrongFunctionHttpMethod(ODataHttpMethod.POST, UriType.URI13, ODataHttpMethod.GET);
    wrongFunctionHttpMethod(ODataHttpMethod.GET, UriType.URI14, ODataHttpMethod.PUT);
  }

  @Test
  public void wrongProperty() throws Exception {
    wrongProperty(ODataHttpMethod.PUT, UriType.URI4, true, false);
    wrongProperty(ODataHttpMethod.PATCH, UriType.URI4, true, false);
    wrongProperty(ODataHttpMethod.DELETE, UriType.URI4, true, true);
    wrongProperty(ODataHttpMethod.DELETE, UriType.URI4, true, false);
    wrongProperty(ODataHttpMethod.DELETE, UriType.URI4, false, false);

    wrongProperty(ODataHttpMethod.PUT, UriType.URI5, true, false);
    wrongProperty(ODataHttpMethod.PATCH, UriType.URI5, true, false);
    wrongProperty(ODataHttpMethod.DELETE, UriType.URI5, true, true);
    wrongProperty(ODataHttpMethod.DELETE, UriType.URI5, true, false);
    wrongProperty(ODataHttpMethod.DELETE, UriType.URI5, false, false);
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