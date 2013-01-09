package com.sap.core.odata.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.feature.Batch;
import com.sap.core.odata.api.processor.feature.Entity;
import com.sap.core.odata.api.processor.feature.EntityComplexProperty;
import com.sap.core.odata.api.processor.feature.EntityLink;
import com.sap.core.odata.api.processor.feature.EntityLinks;
import com.sap.core.odata.api.processor.feature.EntityMedia;
import com.sap.core.odata.api.processor.feature.EntitySet;
import com.sap.core.odata.api.processor.feature.EntitySimpleProperty;
import com.sap.core.odata.api.processor.feature.EntitySimplePropertyValue;
import com.sap.core.odata.api.processor.feature.FunctionImport;
import com.sap.core.odata.api.processor.feature.FunctionImportValue;
import com.sap.core.odata.api.processor.feature.Metadata;
import com.sap.core.odata.api.processor.feature.ServiceDocument;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.commons.ODataHttpMethod;
import com.sap.core.odata.core.uri.UriInfoImpl;
import com.sap.core.odata.core.uri.UriType;

/**
 * Tests for request dispatching according to URI type and HTTP method
 * @author SAP AG
 */
public class DispatcherTest {

  private static ODataService service;

  @BeforeClass
  public static void createMockProcessor() throws ODataException {
    ServiceDocument serviceDocument = mock(ServiceDocument.class);
    when(serviceDocument.readServiceDocument(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());

    EntitySet entitySet = mock(EntitySet.class);
    when(entitySet.readEntitySet(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entitySet.countEntitySet(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entitySet.createEntity(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());

    Entity entity = mock(Entity.class);
    when(entity.readEntity(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entity.existsEntity(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entity.deleteEntity(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entity.updateEntity(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());

    EntityComplexProperty entityComplexProperty = mock(EntityComplexProperty.class);
    when(entityComplexProperty.readEntityComplexProperty(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entityComplexProperty.updateEntityComplexProperty(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());

    EntitySimpleProperty entitySimpleProperty = mock(EntitySimpleProperty.class);
    when(entitySimpleProperty.readEntitySimpleProperty(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entitySimpleProperty.updateEntitySimpleProperty(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());

    EntitySimplePropertyValue entitySimplePropertyValue = mock(EntitySimplePropertyValue.class);
    when(entitySimplePropertyValue.readEntitySimplePropertyValue(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entitySimplePropertyValue.deleteEntitySimplePropertyValue(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entitySimplePropertyValue.updateEntitySimplePropertyValue(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());

    EntityLink entityLink = mock(EntityLink.class);
    when(entityLink.readEntityLink(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entityLink.existsEntityLink(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entityLink.deleteEntityLink(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entityLink.updateEntityLink(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());

    EntityLinks entityLinks = mock(EntityLinks.class);
    when(entityLinks.readEntityLinks(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entityLinks.countEntityLinks(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entityLinks.createEntityLink(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());

    Metadata metadata = mock(Metadata.class);
    when(metadata.readMetadata(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());

    Batch batch = mock(Batch.class);
    when(batch.executeBatch(any(String.class))).thenAnswer(getAnswer());

    FunctionImport functionImport = mock(FunctionImport.class);
    when(functionImport.executeFunctionImport(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());

    FunctionImportValue functionImportValue = mock(FunctionImportValue.class);
    when(functionImportValue.executeFunctionImportValue(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());

    EntityMedia entityMedia = mock(EntityMedia.class);
    when(entityMedia.readEntityMedia(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entityMedia.deleteEntityMedia(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());
    when(entityMedia.updateEntityMedia(any(UriInfoImpl.class), any(String.class))).thenAnswer(getAnswer());

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

  private void checkDispatch(final ODataHttpMethod method, final UriType uriType, final boolean isValue, final String expectedMethodName) throws ODataException {
    Dispatcher dispatcher = new Dispatcher(service);

    UriInfoImpl uriParserResult = mock(UriInfoImpl.class);
    when(uriParserResult.getUriType()).thenReturn(uriType);
    when(uriParserResult.isValue()).thenReturn(isValue);
    final String contentType = method == ODataHttpMethod.GET ? ContentType.APPLICATION_XML.toContentTypeString() : null;
    ODataRequest request = ODataRequestImpl.create(null, contentType).build();
    final ODataResponse response = dispatcher.dispatch(method, uriParserResult, request);
    assertEquals(expectedMethodName, response.getEntity());
  }

  private void checkDispatch(final ODataHttpMethod method, final UriType uriType, final String expectedMethodName) throws ODataException {
    checkDispatch(method, uriType, false, expectedMethodName);
  }

  private void wrongDispatch(final ODataHttpMethod method, final UriType uriType) {
    try {
      checkDispatch(method, uriType, null);
      fail("Expected ODataException not thrown");
    } catch (ODataException e) {
      assertNotNull(e);
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
//    checkDispatch(ODataHttpMethod.DELETE, UriType.URI4, true, "deleteEntitySimplePropertyValue");
    checkDispatch(ODataHttpMethod.PATCH, UriType.URI4, true, "updateEntitySimplePropertyValue");
    checkDispatch(ODataHttpMethod.MERGE, UriType.URI4, true, "updateEntitySimplePropertyValue");

    checkDispatch(ODataHttpMethod.GET, UriType.URI5, "readEntitySimpleProperty");
    checkDispatch(ODataHttpMethod.PUT, UriType.URI5, "updateEntitySimpleProperty");
    checkDispatch(ODataHttpMethod.PATCH, UriType.URI5, "updateEntitySimpleProperty");
    checkDispatch(ODataHttpMethod.MERGE, UriType.URI5, "updateEntitySimpleProperty");
    checkDispatch(ODataHttpMethod.GET, UriType.URI5, true, "readEntitySimplePropertyValue");
    checkDispatch(ODataHttpMethod.PUT, UriType.URI5, true, "updateEntitySimplePropertyValue");
//    checkDispatch(ODataHttpMethod.DELETE, UriType.URI5, true, "deleteEntitySimplePropertyValue");
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

    checkDispatch(ODataHttpMethod.GET, UriType.URI10, "executeFunctionImport");
    checkDispatch(ODataHttpMethod.GET, UriType.URI11, "executeFunctionImport");
    checkDispatch(ODataHttpMethod.GET, UriType.URI12, "executeFunctionImport");
    checkDispatch(ODataHttpMethod.GET, UriType.URI13, "executeFunctionImport");
    checkDispatch(ODataHttpMethod.GET, UriType.URI14, "executeFunctionImport");
    checkDispatch(ODataHttpMethod.GET, UriType.URI14, true, "executeFunctionImportValue");

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
}
