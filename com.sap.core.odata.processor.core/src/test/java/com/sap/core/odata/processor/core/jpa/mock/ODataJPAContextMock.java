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
package com.sap.core.odata.processor.core.jpa.mock;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Metamodel;

import org.easymock.EasyMock;

import com.sap.core.odata.processor.api.jpa.ODataJPAContext;

public abstract class ODataJPAContextMock {

  public static final String NAMESPACE = "salesorderprocessing";
  public static final String MAPPING_MODEL = "SalesOrderProcessingMappingModel";
  public static final String PERSISTENCE_UNIT_NAME = "salesorderprocessing";

  public static ODataJPAContext mockODataJPAContext()
  {
    ODataJPAContext odataJPAContext = EasyMock.createMock(ODataJPAContext.class);
    EasyMock.expect(odataJPAContext.getPersistenceUnitName()).andStubReturn(NAMESPACE);
    EasyMock.expect(odataJPAContext.getEntityManagerFactory()).andReturn(mockEntityManagerFactory());
    EasyMock.expect(odataJPAContext.getJPAEdmMappingModel()).andReturn(MAPPING_MODEL);
    EasyMock.expect(odataJPAContext.getJPAEdmExtension()).andReturn(null);
    EasyMock.replay(odataJPAContext);
    return odataJPAContext;
  }

  private static EntityManagerFactory mockEntityManagerFactory() {
    EntityManagerFactory emf = EasyMock.createMock(EntityManagerFactory.class);
    EasyMock.expect(emf.getMetamodel()).andReturn(mockMetaModel());
    EasyMock.replay(emf);
    return emf;
  }

  private static Metamodel mockMetaModel() {
    Metamodel metaModel = EasyMock.createMock(Metamodel.class);
    EasyMock.replay(metaModel);
    return metaModel;
  }

}
