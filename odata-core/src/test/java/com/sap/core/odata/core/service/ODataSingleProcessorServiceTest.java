package com.sap.core.odata.core.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.ODataSingleProcessorService;
import com.sap.core.odata.api.processor.feature.Batch;
import com.sap.core.odata.api.processor.feature.CustomContentType;
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

/**
 * @author SAP AG
 */
public class ODataSingleProcessorServiceTest {

  private ODataSingleProcessorService service;
  private ODataSingleProcessor processor;
  EdmProvider provider;

  @Before
  public void before() throws Exception {
    provider = mock(EdmProvider.class);
    processor = mock(ODataSingleProcessor.class, withSettings().extraInterfaces(CustomContentType.class));
    service = new ODataSingleProcessorService(provider, processor);
  }

  @Test
  public void defaultSupportedContentTypesForEntitySet() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntitySet.class);
    assertTrue(types.contains(ContentType.APPLICATION_ATOM_XML.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_ATOM_XML_FEED.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForEntity() throws Exception {
    List<String> types = service.getSupportedContentTypes(Entity.class);
    assertTrue(types.contains(ContentType.APPLICATION_ATOM_XML.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_ATOM_XML_ENTRY.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForServiceDocument() throws Exception {
    List<String> types = service.getSupportedContentTypes(ServiceDocument.class);
    assertTrue(types.contains(ContentType.APPLICATION_ATOM_SVC.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForMetadata() throws Exception {
    List<String> types = service.getSupportedContentTypes(Metadata.class);
    assertTrue(types.contains(ContentType.APPLICATION_XML.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForFunctionImport() throws Exception {
    List<String> types = service.getSupportedContentTypes(FunctionImport.class);
    assertTrue(types.contains(ContentType.APPLICATION_XML.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForEntityComplexProperty() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntityComplexProperty.class);
    assertTrue(types.contains(ContentType.APPLICATION_XML.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForEntitySimpleProperty() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntitySimpleProperty.class);
    assertTrue(types.contains(ContentType.APPLICATION_XML.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForEntityLinks() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntityLinks.class);
    assertTrue(types.contains(ContentType.APPLICATION_XML.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForEntityLink() throws Exception {
    ContentType ctGif = ContentType.create("image", "gif");

    List<String> types = service.getSupportedContentTypes(EntityLink.class);
    assertTrue(types.contains(ContentType.APPLICATION_XML.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
    assertFalse(types.contains(ctGif.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesAndGifForEntityLink() throws Exception {
    String ctGif = ContentType.create("image", "gif").toContentTypeString();
    when(((CustomContentType) processor).getCustomContentTypes(EntityLink.class)).thenReturn(Arrays.asList(ctGif));

    List<String> types = service.getSupportedContentTypes(EntityLink.class);
    assertTrue(types.contains(ctGif));
  }

  @Test
  public void defaultSupportedContentTypesForEntityMedia() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntityMedia.class);
    assertTrue(types.contains(ContentType.WILDCARD.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForSimplePropertyValue() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntitySimplePropertyValue.class);
    assertTrue(types.contains(ContentType.WILDCARD.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForFunctionImportValue() throws Exception {
    List<String> types = service.getSupportedContentTypes(FunctionImportValue.class);
    assertTrue(types.contains(ContentType.WILDCARD.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForBatch() throws Exception {
    List<String> types = service.getSupportedContentTypes(Batch.class);
    assertTrue(types.contains(ContentType.MULTIPART_MIXED.toContentTypeString()));
  }
}
