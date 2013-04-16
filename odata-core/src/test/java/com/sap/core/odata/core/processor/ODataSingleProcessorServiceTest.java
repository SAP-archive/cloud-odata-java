/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
import com.sap.core.odata.api.processor.feature.CustomContentType;
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
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class ODataSingleProcessorServiceTest extends BaseTest {

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

  private static String createConstant(final ContentType contentType) {
    return appendCharset(contentType).toContentTypeString();
  }

  private static ContentType appendCharset(final ContentType contentType) {
    return ContentType.create(contentType, ContentType.PARAMETER_CHARSET, ContentType.CHARSET_UTF_8);
  }

  @Test
  public void defaultSupportedContentTypesForEntitySet() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntitySetProcessor.class);
    List<ContentType> convertedTypes = ContentType.convert(types);
    assertTrue(convertedTypes.contains(appendCharset(ContentType.APPLICATION_ATOM_XML)));
    assertTrue(convertedTypes.contains(appendCharset(ContentType.APPLICATION_ATOM_XML_FEED)));
    assertTrue(convertedTypes.contains(appendCharset(ContentType.APPLICATION_JSON)));
  }

  @Test
  public void defaultSupportedContentTypesForEntity() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntityProcessor.class);
    List<ContentType> convertedTypes = ContentType.convert(types);
    assertTrue(convertedTypes.contains(appendCharset(ContentType.APPLICATION_ATOM_XML)));
    assertTrue(convertedTypes.contains(appendCharset(ContentType.APPLICATION_ATOM_XML_ENTRY)));
    assertTrue(convertedTypes.contains(appendCharset(ContentType.APPLICATION_JSON)));
  }

  @Test
  public void defaultSupportedContentTypesForServiceDocument() throws Exception {
    List<String> types = service.getSupportedContentTypes(ServiceDocumentProcessor.class);
    assertTrue(types.contains(APPLICATION_ATOM_SVC));
    assertTrue(types.contains(APPLICATION_JSON));
  }

  @Test
  public void defaultSupportedContentTypesForMetadata() throws Exception {
    List<String> types = service.getSupportedContentTypes(MetadataProcessor.class);
    assertTrue(types.contains(APPLICATION_XML));
  }

  @Test
  public void defaultSupportedContentTypesForFunctionImport() throws Exception {
    List<String> types = service.getSupportedContentTypes(FunctionImportProcessor.class);
    assertTrue(types.contains(APPLICATION_XML));
    assertTrue(types.contains(APPLICATION_JSON));
  }

  @Test
  public void defaultSupportedContentTypesForEntityComplexProperty() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntityComplexPropertyProcessor.class);
    assertTrue(types.contains(APPLICATION_XML));
    assertTrue(types.contains(APPLICATION_JSON));
  }

  @Test
  public void defaultSupportedContentTypesForEntitySimpleProperty() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntitySimplePropertyProcessor.class);
    assertTrue(types.contains(APPLICATION_XML));
    assertTrue(types.contains(APPLICATION_JSON));
  }

  @Test
  public void defaultSupportedContentTypesForEntityLinks() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntityLinksProcessor.class);
    assertTrue(types.contains(APPLICATION_XML));
    assertTrue(types.contains(APPLICATION_JSON));
  }

  @Test
  public void defaultSupportedContentTypesForEntityLink() throws Exception {
    ContentType ctGif = ContentType.create("image", "gif");

    List<String> types = service.getSupportedContentTypes(EntityLinkProcessor.class);
    assertTrue(types.contains(APPLICATION_XML));
    assertTrue(types.contains(APPLICATION_JSON));
    assertFalse(types.contains(ctGif.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesAndGifForEntityLink() throws Exception {
    String ctGif = ContentType.create("image", "gif").toContentTypeString();
    when(((CustomContentType) processor).getCustomContentTypes(EntityLinkProcessor.class)).thenReturn(Arrays.asList(ctGif));

    List<String> types = service.getSupportedContentTypes(EntityLinkProcessor.class);
    assertTrue(types.contains(ctGif));
  }

  @Test
  public void defaultSupportedContentTypesForEntityMedia() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntityMediaProcessor.class);
    assertTrue(types.contains(ContentType.WILDCARD.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForSimplePropertyValue() throws Exception {
    List<String> types = service.getSupportedContentTypes(EntitySimplePropertyValueProcessor.class);
    assertTrue(types.contains(ContentType.WILDCARD.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForFunctionImportValue() throws Exception {
    List<String> types = service.getSupportedContentTypes(FunctionImportValueProcessor.class);
    assertTrue(types.contains(ContentType.WILDCARD.toContentTypeString()));
  }

  @Test
  public void defaultSupportedContentTypesForBatch() throws Exception {
    List<String> types = service.getSupportedContentTypes(BatchProcessor.class);
    assertTrue(types.contains(ContentType.MULTIPART_MIXED.toContentTypeString()));
  }
}
