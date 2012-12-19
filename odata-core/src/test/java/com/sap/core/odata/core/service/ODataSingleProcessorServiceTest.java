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
import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.processor.ContentTypeSupport;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.aspect.ProcessorAspect;
import com.sap.core.odata.api.service.ODataSingleProcessorService;

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
    List<ContentType> types = service.getSupportedContentTypes(ProcessorAspect.ENTITY_SET);
    assertTrue(types.contains(ContentType.APPLICATION_ATOM_XML));
    assertTrue(types.contains(ContentType.APPLICATION_ATOM_XML_FEED));
    assertTrue(types.contains(ContentType.APPLICATION_JSON));
  }

  @Test
  public void defaultSupportedContentTypesForEntity() throws Exception {
    List<ContentType> types = service.getSupportedContentTypes(ProcessorAspect.ENTITY);
    assertTrue(types.contains(ContentType.APPLICATION_ATOM_XML));
    assertTrue(types.contains(ContentType.APPLICATION_ATOM_XML_ENTRY));
    assertTrue(types.contains(ContentType.APPLICATION_JSON));
  }

  // TODO SKL do we miss an interface here?
  //  @Test
  //  public void defaultSupportedContentTypesForFunctionImportEntry() throws Exception {
  //    List<ContentType> types = service.getSupportedContentTypes(ProcessorAspect.FUNCTION_IMPORT_ENTRY);
  //    assertTrue(types.contains(ContentType.APPLICATION_ATOM_XML));
  //    assertTrue(types.contains(ContentType.APPLICATION_ATOM_XML_ENTRY));
  //    assertTrue(types.contains(ContentType.APPLICATION_JSON));
  //  }

  @Test
  public void defaultSupportedContentTypesForServiceDocument() throws Exception {
    List<ContentType> types = service.getSupportedContentTypes(ProcessorAspect.SERVICE_DOCUMENT);
    assertTrue(types.contains(ContentType.APPLICATION_ATOM_SVC));
    assertTrue(types.contains(ContentType.APPLICATION_JSON));
  }

  @Test
  public void defaultSupportedContentTypesForMetadata() throws Exception {
    List<ContentType> types = service.getSupportedContentTypes(ProcessorAspect.METDDATA);
    assertTrue(types.contains(ContentType.APPLICATION_XML));
  }

  @Test
  public void defaultSupportedContentTypesForFunctionImport() throws Exception {
    List<ContentType> types = service.getSupportedContentTypes(ProcessorAspect.FUNCTION_IMPORT);
    assertTrue(types.contains(ContentType.APPLICATION_XML));
    assertTrue(types.contains(ContentType.APPLICATION_JSON));
  }

  @Test
  public void defaultSupportedContentTypesForEntityComplexProperty() throws Exception {
    List<ContentType> types = service.getSupportedContentTypes(ProcessorAspect.ENTITY_COMPLEX_PROPERTY);
    assertTrue(types.contains(ContentType.APPLICATION_XML));
    assertTrue(types.contains(ContentType.APPLICATION_JSON));
  }

  @Test
  public void defaultSupportedContentTypesForEntitySimpleProperty() throws Exception {
    List<ContentType> types = service.getSupportedContentTypes(ProcessorAspect.ENTITY_SIMPLE_PROPERTY);
    assertTrue(types.contains(ContentType.APPLICATION_XML));
    assertTrue(types.contains(ContentType.APPLICATION_JSON));
  }

  @Test
  public void defaultSupportedContentTypesForEntityLinks() throws Exception {
    List<ContentType> types = service.getSupportedContentTypes(ProcessorAspect.ENTITY_LINKS);
    assertTrue(types.contains(ContentType.APPLICATION_XML));
    assertTrue(types.contains(ContentType.APPLICATION_JSON));
  }

  @Test
  public void defaultSupportedContentTypesForEntityLink() throws Exception {
    ContentType ctGif = ContentType.create("image", "gif");

    List<ContentType> types = service.getSupportedContentTypes(ProcessorAspect.ENTITY_LINK);
    assertTrue(types.contains(ContentType.APPLICATION_XML));
    assertTrue(types.contains(ContentType.APPLICATION_JSON));
    assertFalse(types.contains(ctGif));
  }

  @Test
  public void defaultSupportedContentTypesAndGifForEntityLink() throws Exception {
    ContentType ctGif = ContentType.create("image", "gif");
    when(((ContentTypeSupport) processor).getSupportedContentTypes(ProcessorAspect.ENTITY_LINK)).thenReturn(Arrays.asList(ctGif));
    
    List<ContentType> types = service.getSupportedContentTypes(ProcessorAspect.ENTITY_LINK);
    assertTrue(types.contains(ctGif));
  }
  

  @Test
  public void defaultSupportedContentTypesForEntityMedia() throws Exception {
    List<ContentType> types = service.getSupportedContentTypes(ProcessorAspect.ENTITY_MEDIA);
    assertTrue(types.contains(ContentType.WILDCARD));
  }

  @Test
  public void defaultSupportedContentTypesForSimplePropertyValue() throws Exception {
    List<ContentType> types = service.getSupportedContentTypes(ProcessorAspect.ENTITY_SIMPLE_PROPERTY_VALUE);
    assertTrue(types.contains(ContentType.WILDCARD));
  }

  @Test
  public void defaultSupportedContentTypesForFunctionImportValue() throws Exception {
    List<ContentType> types = service.getSupportedContentTypes(ProcessorAspect.FUNCTION_IMPORT_VALUE);
    assertTrue(types.contains(ContentType.WILDCARD));
  }

  @Test
  public void defaultSupportedContentTypesForBatch() throws Exception {
    List<ContentType> types = service.getSupportedContentTypes(ProcessorAspect.BATCH);
    assertTrue(types.contains(ContentType.MULTIPART_MIXED));
  }
}
