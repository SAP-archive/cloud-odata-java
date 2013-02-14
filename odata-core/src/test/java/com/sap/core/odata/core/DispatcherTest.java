package com.sap.core.odata.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.commons.HttpContentType;
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
import com.sap.core.odata.api.uri.NavigationPropertySegment;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.core.commons.ContentType.ODataFormat;
import com.sap.core.odata.core.commons.ODataHttpMethod;
import com.sap.core.odata.core.uri.UriInfoImpl;
import com.sap.core.odata.core.uri.UriType;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * Tests for request dispatching according to URI type and HTTP method
 * @author SAP AG
 */
public class DispatcherTest extends BaseTest {

  private static ODataService service;

  @BeforeClass
  public static void createMockProcessor() throws ODataException {
    ServiceDocumentProcessor serviceDocument = mock(ServiceDocumentProcessor.class);
    when(serviceDocument.readServiceDocument(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());

    EntitySetProcessor entitySet = mock(EntitySetProcessor.class);
    when(entitySet.readEntitySet(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entitySet.countEntitySet(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entitySet.createEntity(any(UriInfoImpl.class), any(InputStream.class), any(String.class), any(String.class))).thenAnswer(getAnswer());

    EntityProcessor entity = mock(EntityProcessor.class);
    when(entity.readEntity(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entity.existsEntity(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entity.deleteEntity(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entity.updateEntity(any(UriInfoImpl.class), any(InputStream.class), any(String.class), any(Boolean.class), any(String.class))).thenAnswer(getAnswer());

    EntityComplexPropertyProcessor entityComplexProperty = mock(EntityComplexPropertyProcessor.class);
    when(entityComplexProperty.readEntityComplexProperty(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entityComplexProperty.updateEntityComplexProperty(any(UriInfoImpl.class), any(InputStream.class), any(String.class), any(Boolean.class), any(String.class))).thenAnswer(getAnswer());

    EntitySimplePropertyProcessor entitySimpleProperty = mock(EntitySimplePropertyProcessor.class);
    when(entitySimpleProperty.readEntitySimpleProperty(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entitySimpleProperty.updateEntitySimpleProperty(any(UriInfoImpl.class), any(InputStream.class), any(String.class), any(String.class))).thenAnswer(getAnswer());

    EntitySimplePropertyValueProcessor entitySimplePropertyValue = mock(EntitySimplePropertyValueProcessor.class);
    when(entitySimplePropertyValue.readEntitySimplePropertyValue(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entitySimplePropertyValue.deleteEntitySimplePropertyValue(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entitySimplePropertyValue.updateEntitySimplePropertyValue(any(UriInfoImpl.class), any(InputStream.class), any(String.class), any(String.class))).thenAnswer(getAnswer());

    EntityLinkProcessor entityLink = mock(EntityLinkProcessor.class);
    when(entityLink.readEntityLink(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entityLink.existsEntityLink(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entityLink.deleteEntityLink(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entityLink.updateEntityLink(any(UriInfoImpl.class), any(InputStream.class), any(String.class), any(String.class))).thenAnswer(getAnswer());

    EntityLinksProcessor entityLinks = mock(EntityLinksProcessor.class);
    when(entityLinks.readEntityLinks(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entityLinks.countEntityLinks(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entityLinks.createEntityLink(any(UriInfoImpl.class), any(InputStream.class), any(String.class), any(String.class))).thenAnswer(getAnswer());

    MetadataProcessor metadata = mock(MetadataProcessor.class);
    when(metadata.readMetadata(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());

    BatchProcessor batch = mock(BatchProcessor.class);
    when(batch.executeBatch(any(String.class))).thenAnswer(getAnswer());

    FunctionImportProcessor functionImport = mock(FunctionImportProcessor.class);
    when(functionImport.executeFunctionImport(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());

    FunctionImportValueProcessor functionImportValue = mock(FunctionImportValueProcessor.class);
    when(functionImportValue.executeFunctionImportValue(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());

    EntityMediaProcessor entityMedia = mock(EntityMediaProcessor.class);
    when(entityMedia.readEntityMedia(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entityMedia.deleteEntityMedia(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entityMedia.updateEntityMedia(any(UriInfoImpl.class), any(InputStream.class), any(String.class), any(String.class))).thenAnswer(getAnswer());

    service = mock(ODataService.class);
    when(service.getServiceDocumentProcessor()).thenReturn(serviceDocument);
    when(service.getEntitySetProcessor()).thenReturn(entitySet);
    when(service.getEntityProcessor()).thenReturn(entity);
    when(service.getEntityComplexPropertyProcessor()).thenReturn(entityComplexProperty);
    when(service.getEntitySimplePropertyProcessor()).thenReturn(entitySimpleProperty);
    when(service.getEntitySimplePropertyValueProcessor()).thenReturn(entitySimplePropertyValue);
    when(service.getEntityLinkProcessor()).thenReturn(entityLink);
    when(service.getEntityLinksProcessor()).thenReturn(entityLinks);
    when(service.getMetadataProcessor()).thenReturn(metadata);
    when(service.getBatchProcessor()).thenReturn(batch);
    when(service.getFunctionImportProcessor()).thenReturn(functionImport);
    when(service.getFunctionImportValueProcessor()).thenReturn(functionImportValue);
    when(service.getEntityMediaProcessor()).thenReturn(entityMedia);
  }

  private static Answer<ODataResponse> getAnswer() {
    return new Answer<ODataResponse>() {
      public ODataResponse answer(InvocationOnMock invocation) {
        return mockResponse(invocation.getMethod().getName());
      }
    };
  }

  private static ODataResponse mockResponse(final String value) {
    ODataResponse response = mock(ODataResponse.class);
    when(response.getEntity()).thenReturn(value);

    return response;
  }

  private static UriInfoImpl mockUriInfo(final UriType uriType, final boolean isValue) throws EdmException {
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

  private static UriInfoImpl mockOptions(UriInfoImpl uriInfo,
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

  private static UriInfoImpl mockFunctionImport(UriInfoImpl uriInfo, final ODataHttpMethod httpMethod) throws EdmException {
    EdmFunctionImport functionImport = mock(EdmFunctionImport.class);
    when(functionImport.getHttpMethod()).thenReturn(httpMethod == null ? null : httpMethod.toString());
    when(uriInfo.getFunctionImport()).thenReturn(functionImport);

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

  private static UriInfoImpl mockNavigationPath(UriInfoImpl uriInfo, final boolean oneSegment) {
    when(uriInfo.getNavigationSegments()).thenReturn(
        oneSegment ? Arrays.asList(mock(NavigationSegment.class)) :
            Arrays.asList(mock(NavigationSegment.class), mock(NavigationSegment.class)));
    return uriInfo;
  }

  private static void checkDispatch(final ODataHttpMethod method, final UriInfoImpl uriInfo, String requestContentType, final String expectedMethodName) throws ODataException {
    final String contentType = method == ODataHttpMethod.GET ? HttpContentType.APPLICATION_XML : null;

    final Dispatcher dispatcher = new Dispatcher(service);
    final ODataResponse response = dispatcher.dispatch(method, uriInfo, null, requestContentType, contentType);
    assertEquals(expectedMethodName, response.getEntity());
  }

  private static void checkDispatch(final ODataHttpMethod method, final UriType uriType, final boolean isValue, final String expectedMethodName) throws ODataException {
    checkDispatch(method, mockUriInfo(uriType, isValue), expectedMethodName);
  }

  private static void checkDispatch(final ODataHttpMethod method, final UriInfoImpl uriInfo, final String expectedMethodName) throws ODataException {
    String requestContentType = null;
    if (method == ODataHttpMethod.POST || method == ODataHttpMethod.PUT
        || method == ODataHttpMethod.PATCH || method == ODataHttpMethod.MERGE)
        requestContentType = HttpContentType.APPLICATION_XML;

    checkDispatch(method, uriInfo, requestContentType, expectedMethodName);
  }

  private static void checkDispatch(final ODataHttpMethod method, final UriType uriType, final String expectedMethodName) throws ODataException {
    checkDispatch(method, uriType, false, expectedMethodName);
  }

  private static void wrongDispatch(final ODataHttpMethod method, final UriType uriType) {
    try {
      checkDispatch(method, uriType, null);
      fail("Expected ODataException not thrown");
    } catch (ODataException e) {
      assertNotNull(e);
    }
  }

  private static void wrongRequestContentType(final ODataHttpMethod method, final UriType uriType, final String requestContentType) {
    try {
      checkDispatch(method, mockUriInfo(uriType, false), requestContentType, null);
      fail("Expected ODataException not thrown");
    } catch (ODataUnsupportedMediaTypeException e) {
      assertNotNull(e);
    } catch (ODataException e) {
      fail("Unexpected ODataException thrown");
    }
  }

  private static void wrongOptions(final ODataHttpMethod method, final UriType uriType,
      final boolean format,
      final boolean filter, final boolean inlineCount, final boolean orderBy,
      final boolean skipToken, final boolean skip, final boolean top,
      final boolean expand, final boolean select) {
    try {
      checkDispatch(method, mockOptions(mockUriInfo(uriType, false),
          format, filter, inlineCount, orderBy, skipToken, skip, top, expand, select), null);
      fail("Expected ODataMethodNotAllowedException not thrown");
    } catch (ODataMethodNotAllowedException e) {
      assertNotNull(e);
    } catch (ODataException e) {
      fail("Unexpected ODataException thrown");
    }
  }

  private static void wrongFunctionHttpMethod(final ODataHttpMethod method, final UriType uriType, final ODataHttpMethod httpMethod) {
    try {
      checkDispatch(method, mockFunctionImport(mockUriInfo(uriType, false), httpMethod), null);
      fail("Expected ODataMethodNotAllowedException not thrown");
    } catch (ODataMethodNotAllowedException e) {
      assertNotNull(e);
    } catch (ODataException e) {
      fail("Unexpected ODataException thrown");
    }
  }

  private static void wrongProperty(final ODataHttpMethod method, final UriType uriType, final boolean key, final boolean nullable) {
    try {
      checkDispatch(method, mockProperty(mockUriInfo(uriType, true), key, nullable), null);
      fail("Expected ODataMethodNotAllowedException not thrown");
    } catch (ODataMethodNotAllowedException e) {
      assertNotNull(e);
    } catch (ODataException e) {
      fail("Unexpected ODataException thrown");
    }
  }

  private static void wrongNavigationPath(final ODataHttpMethod method, final UriType uriType) {
    try {
      UriInfoImpl uriInfo = mockUriInfo(uriType, true);
      uriInfo = mockNavigationPath(uriInfo,
          uriType != UriType.URI6A && uriType != UriType.URI6B
          && uriType != UriType.URI7A && uriType != UriType.URI7B);
      checkDispatch(method, uriInfo, null);
      fail("Expected ODataBadRequestException not thrown");
    } catch (ODataBadRequestException e) {
      assertNotNull(e);
    } catch (ODataException e) {
      fail("Unexpected ODataException thrown");
    }
  }

  @Test
  public void dispatch() throws Exception {
    checkDispatch(ODataHttpMethod.GET, UriType.URI0, "readServiceDocument");

    checkDispatch(ODataHttpMethod.GET, UriType.URI1, "readEntitySet");
    checkDispatch(ODataHttpMethod.POST, UriType.URI1, "createEntity");

    checkDispatch(ODataHttpMethod.GET, UriType.URI2, "readEntity");
    checkDispatch(ODataHttpMethod.PUT, UriType.URI2, "updateEntity");
    checkDispatch(ODataHttpMethod.DELETE, UriType.URI2, "deleteEntity");
    checkDispatch(ODataHttpMethod.PATCH, UriType.URI2, "updateEntity");
    checkDispatch(ODataHttpMethod.MERGE, UriType.URI2, "updateEntity");

    checkDispatch(ODataHttpMethod.GET, UriType.URI3, "readEntityComplexProperty");
    checkDispatch(ODataHttpMethod.PUT, UriType.URI3, "updateEntityComplexProperty");
    checkDispatch(ODataHttpMethod.PATCH, UriType.URI3, "updateEntityComplexProperty");
    checkDispatch(ODataHttpMethod.MERGE, UriType.URI3, "updateEntityComplexProperty");

    checkDispatch(ODataHttpMethod.GET, UriType.URI4, "readEntitySimpleProperty");
    checkDispatch(ODataHttpMethod.PUT, UriType.URI4, "updateEntitySimpleProperty");
    checkDispatch(ODataHttpMethod.PATCH, UriType.URI4, "updateEntitySimpleProperty");
    checkDispatch(ODataHttpMethod.MERGE, UriType.URI4, "updateEntitySimpleProperty");
    checkDispatch(ODataHttpMethod.GET, UriType.URI4, true, "readEntitySimplePropertyValue");
    checkDispatch(ODataHttpMethod.PUT, UriType.URI4, true, "updateEntitySimplePropertyValue");
    checkDispatch(ODataHttpMethod.DELETE, UriType.URI4, true, "deleteEntitySimplePropertyValue");
    checkDispatch(ODataHttpMethod.PATCH, UriType.URI4, true, "updateEntitySimplePropertyValue");
    checkDispatch(ODataHttpMethod.MERGE, UriType.URI4, true, "updateEntitySimplePropertyValue");

    checkDispatch(ODataHttpMethod.GET, UriType.URI5, "readEntitySimpleProperty");
    checkDispatch(ODataHttpMethod.PUT, UriType.URI5, "updateEntitySimpleProperty");
    checkDispatch(ODataHttpMethod.PATCH, UriType.URI5, "updateEntitySimpleProperty");
    checkDispatch(ODataHttpMethod.MERGE, UriType.URI5, "updateEntitySimpleProperty");
    checkDispatch(ODataHttpMethod.GET, UriType.URI5, true, "readEntitySimplePropertyValue");
    checkDispatch(ODataHttpMethod.PUT, UriType.URI5, true, "updateEntitySimplePropertyValue");
    checkDispatch(ODataHttpMethod.DELETE, UriType.URI5, true, "deleteEntitySimplePropertyValue");
    checkDispatch(ODataHttpMethod.PATCH, UriType.URI5, true, "updateEntitySimplePropertyValue");
    checkDispatch(ODataHttpMethod.MERGE, UriType.URI5, true, "updateEntitySimplePropertyValue");

    checkDispatch(ODataHttpMethod.GET, UriType.URI6A, "readEntity");

    checkDispatch(ODataHttpMethod.GET, UriType.URI6B, "readEntitySet");
    checkDispatch(ODataHttpMethod.POST, UriType.URI6B, "createEntity");

    checkDispatch(ODataHttpMethod.GET, UriType.URI7A, "readEntityLink");
    checkDispatch(ODataHttpMethod.PUT, UriType.URI7A, "updateEntityLink");
    checkDispatch(ODataHttpMethod.DELETE, UriType.URI7A, "deleteEntityLink");
    checkDispatch(ODataHttpMethod.PATCH, UriType.URI7A, "updateEntityLink");
    checkDispatch(ODataHttpMethod.MERGE, UriType.URI7A, "updateEntityLink");

    checkDispatch(ODataHttpMethod.GET, UriType.URI7B, "readEntityLinks");
    checkDispatch(ODataHttpMethod.POST, UriType.URI7B, "createEntityLink");

    checkDispatch(ODataHttpMethod.GET, UriType.URI8, "readMetadata");

    checkDispatch(ODataHttpMethod.POST, UriType.URI9, "executeBatch");

    checkDispatch(ODataHttpMethod.GET, mockFunctionImport(mockUriInfo(UriType.URI10, false), null), "executeFunctionImport");
    checkDispatch(ODataHttpMethod.GET, mockFunctionImport(mockUriInfo(UriType.URI10, false), ODataHttpMethod.GET), "executeFunctionImport");
    checkDispatch(ODataHttpMethod.GET, mockFunctionImport(mockUriInfo(UriType.URI11, false), null), "executeFunctionImport");
    checkDispatch(ODataHttpMethod.GET, mockFunctionImport(mockUriInfo(UriType.URI11, false), ODataHttpMethod.GET), "executeFunctionImport");
    checkDispatch(ODataHttpMethod.GET, mockFunctionImport(mockUriInfo(UriType.URI12, false), null), "executeFunctionImport");
    checkDispatch(ODataHttpMethod.GET, mockFunctionImport(mockUriInfo(UriType.URI12, false), ODataHttpMethod.GET), "executeFunctionImport");
    checkDispatch(ODataHttpMethod.GET, mockFunctionImport(mockUriInfo(UriType.URI13, false), null), "executeFunctionImport");
    checkDispatch(ODataHttpMethod.GET, mockFunctionImport(mockUriInfo(UriType.URI13, false), ODataHttpMethod.GET), "executeFunctionImport");
    checkDispatch(ODataHttpMethod.GET, mockFunctionImport(mockUriInfo(UriType.URI14, false), null), "executeFunctionImport");
    checkDispatch(ODataHttpMethod.GET, mockFunctionImport(mockUriInfo(UriType.URI14, false), ODataHttpMethod.GET), "executeFunctionImport");
    checkDispatch(ODataHttpMethod.GET, mockFunctionImport(mockUriInfo(UriType.URI14, true), null), "executeFunctionImportValue");

    checkDispatch(ODataHttpMethod.GET, UriType.URI15, "countEntitySet");

    checkDispatch(ODataHttpMethod.GET, UriType.URI16, "existsEntity");

    checkDispatch(ODataHttpMethod.GET, UriType.URI17, "readEntityMedia");
    checkDispatch(ODataHttpMethod.PUT, UriType.URI17, "updateEntityMedia");
    checkDispatch(ODataHttpMethod.DELETE, UriType.URI17, "deleteEntityMedia");

    checkDispatch(ODataHttpMethod.GET, UriType.URI50A, "existsEntityLink");

    checkDispatch(ODataHttpMethod.GET, UriType.URI50B, "countEntityLinks");
  }

  @Test
  public void dispatchNotAllowedCombinations() throws Exception {
    wrongDispatch(null, UriType.URI0);

    wrongDispatch(ODataHttpMethod.PUT, UriType.URI0);
    wrongDispatch(ODataHttpMethod.POST, UriType.URI0);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI0);
    wrongDispatch(ODataHttpMethod.PATCH, UriType.URI0);
    wrongDispatch(ODataHttpMethod.MERGE, UriType.URI0);

    wrongDispatch(ODataHttpMethod.PUT, UriType.URI1);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI1);
    wrongDispatch(ODataHttpMethod.PATCH, UriType.URI1);
    wrongDispatch(ODataHttpMethod.MERGE, UriType.URI1);

    wrongDispatch(ODataHttpMethod.POST, UriType.URI2);

    wrongDispatch(ODataHttpMethod.POST, UriType.URI3);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI3);

    wrongDispatch(ODataHttpMethod.POST, UriType.URI4);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI4);

    wrongDispatch(ODataHttpMethod.POST, UriType.URI5);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI5);

    wrongDispatch(ODataHttpMethod.PUT, UriType.URI6A);
    wrongDispatch(ODataHttpMethod.POST, UriType.URI6A);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI6A);
    wrongDispatch(ODataHttpMethod.PATCH, UriType.URI6A);
    wrongDispatch(ODataHttpMethod.MERGE, UriType.URI6A);

    wrongDispatch(ODataHttpMethod.PUT, UriType.URI6B);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI6B);
    wrongDispatch(ODataHttpMethod.PATCH, UriType.URI6B);
    wrongDispatch(ODataHttpMethod.MERGE, UriType.URI6B);

    wrongDispatch(ODataHttpMethod.POST, UriType.URI7A);

    wrongDispatch(ODataHttpMethod.PUT, UriType.URI7B);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI7B);
    wrongDispatch(ODataHttpMethod.PATCH, UriType.URI7B);
    wrongDispatch(ODataHttpMethod.MERGE, UriType.URI7B);

    wrongDispatch(ODataHttpMethod.PUT, UriType.URI8);
    wrongDispatch(ODataHttpMethod.POST, UriType.URI8);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI8);
    wrongDispatch(ODataHttpMethod.PATCH, UriType.URI8);
    wrongDispatch(ODataHttpMethod.MERGE, UriType.URI8);

    wrongDispatch(ODataHttpMethod.GET, UriType.URI9);
    wrongDispatch(ODataHttpMethod.PUT, UriType.URI9);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI9);
    wrongDispatch(ODataHttpMethod.PATCH, UriType.URI9);
    wrongDispatch(ODataHttpMethod.MERGE, UriType.URI9);

    wrongDispatch(ODataHttpMethod.PUT, UriType.URI15);
    wrongDispatch(ODataHttpMethod.POST, UriType.URI15);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI15);
    wrongDispatch(ODataHttpMethod.PATCH, UriType.URI15);
    wrongDispatch(ODataHttpMethod.MERGE, UriType.URI15);

    wrongDispatch(ODataHttpMethod.PUT, UriType.URI16);
    wrongDispatch(ODataHttpMethod.POST, UriType.URI16);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI16);
    wrongDispatch(ODataHttpMethod.PATCH, UriType.URI16);
    wrongDispatch(ODataHttpMethod.MERGE, UriType.URI16);

    wrongDispatch(ODataHttpMethod.POST, UriType.URI17);
    wrongDispatch(ODataHttpMethod.PATCH, UriType.URI17);
    wrongDispatch(ODataHttpMethod.MERGE, UriType.URI17);

    wrongDispatch(ODataHttpMethod.PUT, UriType.URI50A);
    wrongDispatch(ODataHttpMethod.POST, UriType.URI50A);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI50A);
    wrongDispatch(ODataHttpMethod.PATCH, UriType.URI50A);
    wrongDispatch(ODataHttpMethod.MERGE, UriType.URI50A);

    wrongDispatch(ODataHttpMethod.PUT, UriType.URI50B);
    wrongDispatch(ODataHttpMethod.POST, UriType.URI50B);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI50B);
    wrongDispatch(ODataHttpMethod.PATCH, UriType.URI50B);
    wrongDispatch(ODataHttpMethod.MERGE, UriType.URI50B);
  }

  @Test
  public void dispatchNotAllowedOptions() throws Exception {
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
  public void dispatchFunctionImportWrongHttpMethod() throws Exception {
    wrongFunctionHttpMethod(ODataHttpMethod.POST, UriType.URI1, ODataHttpMethod.GET);
    wrongFunctionHttpMethod(ODataHttpMethod.GET, UriType.URI10, ODataHttpMethod.PUT);
    wrongFunctionHttpMethod(ODataHttpMethod.POST, UriType.URI11, ODataHttpMethod.GET);
    wrongFunctionHttpMethod(ODataHttpMethod.PATCH, UriType.URI12, ODataHttpMethod.GET);
    wrongFunctionHttpMethod(ODataHttpMethod.POST, UriType.URI13, ODataHttpMethod.GET);
    wrongFunctionHttpMethod(ODataHttpMethod.GET, UriType.URI14, ODataHttpMethod.PUT);
  }

  @Test
  public void dispatchWrongProperty() throws Exception {
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
  public void dispatchWrongNavigationPath() throws Exception {
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
  public void dispatchWrongRequestContentType() throws Exception {
    wrongRequestContentType(ODataHttpMethod.POST, UriType.URI1, HttpContentType.APPLICATION_ATOM_SVC);
    wrongRequestContentType(ODataHttpMethod.POST, UriType.URI1, HttpContentType.APPLICATION_ATOM_SVC_UTF8);

    wrongRequestContentType(ODataHttpMethod.PUT, UriType.URI2, HttpContentType.APPLICATION_ATOM_SVC);
    wrongRequestContentType(ODataHttpMethod.PUT, UriType.URI2, HttpContentType.APPLICATION_ATOM_SVC_UTF8);
  }
}
