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
import com.sap.core.odata.api.processor.feature.ContentTypeSupport;
import com.sap.core.odata.api.processor.feature.ProcessorFeature;
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
    processor = mock(ODataSingleProcessor.class, withSettings().extraInterfaces(ContentTypeSupport.class));
    service = new ODataSingleProcessorService(provider, processor);
  }

  @Test
  public void defaultSupportedContentTypesForEntitySet() throws Exception {
    List<String> types = service.getSupportedContentTypes(ProcessorFeature.ENTITY_SET);
    assertTrue(types.contains(ContentType.APPLICATION_ATOM_XML.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_ATOM_XML_FEED.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForEntity() throws Exception {
    List<String> types = service.getSupportedContentTypes(ProcessorFeature.ENTITY);
    assertTrue(types.contains(ContentType.APPLICATION_ATOM_XML.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_ATOM_XML_ENTRY.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
  }

  // TODO SKL do we miss an interface here?
  //  @Test
  //  public void defaultSupportedContentTypesForFunctionImportEntry() throws Exception {
  //    List<String> types = service.getSupportedContentTypes(ProcessorAspect.FUNCTION_IMPORT_ENTRY);
  //    assertTrue(types.contains(ContentType.APPLICATION_ATOM_XML.toContentTypeString()));
  //    assertTrue(types.contains(ContentType.APPLICATION_ATOM_XML_ENTRY.toContentTypeString()));
  //    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
  //  }

  @Test
  public void defaultSupportedContentTypesForServiceDocument() throws Exception {
    List<String> types = service.getSupportedContentTypes(ProcessorFeature.SERVICE_DOCUMENT);
    assertTrue(types.contains(ContentType.APPLICATION_ATOM_SVC.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForMetadata() throws Exception {
    List<String> types = service.getSupportedContentTypes(ProcessorFeature.METADATA);
    assertTrue(types.contains(ContentType.APPLICATION_XML.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForFunctionImport() throws Exception {
    List<String> types = service.getSupportedContentTypes(ProcessorFeature.FUNCTION_IMPORT);
    assertTrue(types.contains(ContentType.APPLICATION_XML.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForEntityComplexProperty() throws Exception {
    List<String> types = service.getSupportedContentTypes(ProcessorFeature.ENTITY_COMPLEX_PROPERTY);
    assertTrue(types.contains(ContentType.APPLICATION_XML.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForEntitySimpleProperty() throws Exception {
    List<String> types = service.getSupportedContentTypes(ProcessorFeature.ENTITY_SIMPLE_PROPERTY);
    assertTrue(types.contains(ContentType.APPLICATION_XML.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForEntityLinks() throws Exception {
    List<String> types = service.getSupportedContentTypes(ProcessorFeature.ENTITY_LINKS);
    assertTrue(types.contains(ContentType.APPLICATION_XML.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForEntityLink() throws Exception {
    ContentType ctGif = ContentType.create("image", "gif");

    List<String> types = service.getSupportedContentTypes(ProcessorFeature.ENTITY_LINK);
    assertTrue(types.contains(ContentType.APPLICATION_XML.toContentTypeString()));
    assertTrue(types.contains(ContentType.APPLICATION_JSON.toContentTypeString()));
    assertFalse(types.contains(ctGif.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesAndGifForEntityLink() throws Exception {
    String ctGif = ContentType.create("image", "gif").toContentTypeString();
    when(((ContentTypeSupport) processor).getSupportedContentTypes(ProcessorFeature.ENTITY_LINK)).thenReturn(Arrays.asList(ctGif));
    
    List<String> types = service.getSupportedContentTypes(ProcessorFeature.ENTITY_LINK);
    assertTrue(types.contains(ctGif));
  }
  

  @Test
  public void defaultSupportedContentTypesForEntityMedia() throws Exception {
    List<String> types = service.getSupportedContentTypes(ProcessorFeature.ENTITY_MEDIA);
    assertTrue(types.contains(ContentType.WILDCARD.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForSimplePropertyValue() throws Exception {
    List<String> types = service.getSupportedContentTypes(ProcessorFeature.ENTITY_SIMPLE_PROPERTY_VALUE);
    assertTrue(types.contains(ContentType.WILDCARD.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForFunctionImportValue() throws Exception {
    List<String> types = service.getSupportedContentTypes(ProcessorFeature.FUNCTION_IMPORT_VALUE);
    assertTrue(types.contains(ContentType.WILDCARD.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForBatch() throws Exception {
    List<String> types = service.getSupportedContentTypes(ProcessorFeature.BATCH);
    assertTrue(types.contains(ContentType.MULTIPART_MIXED.toContentTypeString()));
  }
}
