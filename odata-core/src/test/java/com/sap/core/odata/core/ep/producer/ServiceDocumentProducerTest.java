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
package com.sap.core.odata.core.ep.producer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.edm.provider.EdmImplProv;
import com.sap.core.odata.core.ep.AbstractXmlProducerTestHelper;

public class ServiceDocumentProducerTest extends AbstractXmlProducerTestHelper {

  private EdmImplProv edm;

  public ServiceDocumentProducerTest(final StreamWriterImplType type) {
    super(type);
  }

  @Before
  public void before() throws ODataException {
    EdmProvider edmProvider = mock(EdmProvider.class);

    EntityType entityType = new EntityType().setName("EntityType1");
    when(edmProvider.getEntityType(new FullQualifiedName("EntityType1Ns", "EntityType1"))).thenReturn(entityType);

    ComplexType complexType = new ComplexType().setName("ComplexType1");
    when(edmProvider.getComplexType(new FullQualifiedName("ComplexType1Ns", "ComplexType1"))).thenReturn(complexType);

    Association association = new Association().setName("Association1");
    when(edmProvider.getAssociation(new FullQualifiedName("Association1Ns", "Association1"))).thenReturn(association);

    EntityContainerInfo defaultEntityContainer = new EntityContainerInfo().setName("Container1");
    when(edmProvider.getEntityContainerInfo(null)).thenReturn(defaultEntityContainer);
    when(edmProvider.getEntityContainerInfo("Container1")).thenReturn(defaultEntityContainer);

    edm = new EdmImplProv(edmProvider);

  }

  @Test
  public void testServiceDocumentXml() throws EntityProviderException, ODataException {
    ODataResponse response = EntityProvider.writeServiceDocument(HttpContentType.APPLICATION_ATOM_XML, edm, "http://localhost/");

    assertEquals(HttpContentType.APPLICATION_ATOM_SVC_UTF8, response.getContentHeader());
  }

}
