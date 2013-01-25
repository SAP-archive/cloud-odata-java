package com.sap.core.odata.core.processor;

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
import com.sap.core.odata.core.processor.ODataSingleProcessorService;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class ODataSingleProcessorServiceTest extends BaseTest{

  private static final String APPLICATION_XML = createConstant(ContentType.APPLICATION_XML);
  private static final String APPLICATION_ATOM_SVC = createConstant(ContentType.APPLICATION_ATOM_SVC);
  private static final String APPLICATION_JSON = createConstant(ContentType.APPLICATION_JSON);
  private ODataSingleProcessorService service;
  private ODataSingleProcessor processor;
  EdmProvider provider;

  @Before
  public void before() throws Exception {
    provider = mock(EdmProvider.class);
    processor = mock(ODataSingleProcessor.class, withSettings().extraInterfaces(CustomContentType.class));
    service = new ODataSingleProcessorService(provider, processor);
  }

  private static String createConstant(ContentType contentType) {
    return appendCharset(contentType).toContentTypeString();
  }

  private static ContentType appendCharset(ContentType contentType) {
    return ContentType.create(contentType, ContentType.PARAMETER_CHARSET, ContentType.CHARSET_UTF_8);
  }

  @Test
  public void defaultSupportedContentTypesForEntitySet() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntitySet.class);
    List<ContentType> convertedTypes = ContentType.convert(types);
    assertTrue(convertedTypes.contains(appendCharset(ContentType.APPLICATION_ATOM_XML)));
    assertTrue(convertedTypes.contains(appendCharset(ContentType.APPLICATION_ATOM_XML_FEED)));
    assertTrue(convertedTypes.contains(appendCharset(ContentType.APPLICATION_JSON)));
  }

  @Test
  public void defaultSupportedContentTypesForEntity() throws Exception {
    List<String> types = service.getSupportedContentTypes(Entity.class);
    List<ContentType> convertedTypes = ContentType.convert(types);
    assertTrue(convertedTypes.contains(appendCharset(ContentType.APPLICATION_ATOM_XML)));
    assertTrue(convertedTypes.contains(appendCharset(ContentType.APPLICATION_ATOM_XML_ENTRY)));
    assertTrue(convertedTypes.contains(appendCharset(ContentType.APPLICATION_JSON)));
  }
  
  @Test
  public void defaultSupportedContentTypesForServiceDocument() throws Exception {
    List<String> types = service.getSupportedContentTypes(ServiceDocument.class);
    assertTrue(types.contains(APPLICATION_ATOM_SVC));
    assertTrue(types.contains(APPLICATION_JSON));
  }

  @Test
  public void defaultSupportedContentTypesForMetadata() throws Exception {
    List<String> types = service.getSupportedContentTypes(Metadata.class);
    assertTrue(types.contains(APPLICATION_XML));
  }

  @Test
  public void defaultSupportedContentTypesForFunctionImport() throws Exception {
    List<String> types = service.getSupportedContentTypes(FunctionImport.class);
    assertTrue(types.contains(APPLICATION_XML));
    assertTrue(types.contains(APPLICATION_JSON));
  }

  @Test
  public void defaultSupportedContentTypesForEntityComplexProperty() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntityComplexProperty.class);
    assertTrue(types.contains(APPLICATION_XML));
    assertTrue(types.contains(APPLICATION_JSON));
  }

  @Test
  public void defaultSupportedContentTypesForEntitySimpleProperty() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntitySimpleProperty.class);
    assertTrue(types.contains(APPLICATION_XML));
    assertTrue(types.contains(APPLICATION_JSON));
  }

  @Test
  public void defaultSupportedContentTypesForEntityLinks() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntityLinks.class);
    assertTrue(types.contains(APPLICATION_XML));
    assertTrue(types.contains(APPLICATION_JSON));
  }

  @Test
  public void defaultSupportedContentTypesForEntityLink() throws Exception {
    ContentType ctGif = ContentType.create("image", "gif");

    List<String> types = service.getSupportedContentTypes(EntityLink.class);
    assertTrue(types.contains(APPLICATION_XML));
    assertTrue(types.contains(APPLICATION_JSON));
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
