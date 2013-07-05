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
package com.sap.core.odata.processor.core.jpa.jpql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContext;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContext.JPQLContextBuilder;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;

public class JPQLSelectSingleStatementBuilderTest {

  /**
   * @throws java.lang.Exception
   */
  private JPQLSelectSingleStatementBuilder JPQLSelectSingleStatementBuilder;

  @Before
  public void setUp() throws Exception {

  }

  private JPQLSelectSingleContext createSelectContext() throws ODataJPARuntimeException, EdmException {
    //Object Instantiation

    JPQLSelectSingleContext JPQLSelectSingleContextImpl = null;// new JPQLSelectSingleContextImpl();
    GetEntityUriInfo getEntityView = EasyMock.createMock(GetEntityUriInfo.class);

    EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
    EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
    List<SelectItem> selectItemList = null;

    //Setting up the expected value
    KeyPredicate keyPredicate = EasyMock
        .createMock(KeyPredicate.class);
    EdmProperty kpProperty = EasyMock
        .createMock(EdmProperty.class);
    EdmSimpleType edmType = EasyMock
        .createMock(EdmSimpleType.class);
    EdmMapping edmMapping = EasyMock.createMock(EdmMapping.class);
    EasyMock.expect(edmMapping.getInternalName()).andStubReturn("Field1");
    EasyMock.expect(keyPredicate.getLiteral()).andStubReturn("1");
    try {
      EasyMock.expect(kpProperty.getName()).andStubReturn("Field1");
      EasyMock.expect(kpProperty.getType()).andStubReturn(edmType);

      EasyMock.expect(kpProperty.getMapping()).andStubReturn(edmMapping);

    } catch (EdmException e2) {
      fail("this should not happen");
    }
    EasyMock.expect(keyPredicate.getProperty()).andStubReturn(kpProperty);
    EasyMock.replay(edmMapping, edmType, kpProperty, keyPredicate);
    EasyMock.expect(getEntityView.getTargetEntitySet()).andStubReturn(edmEntitySet);
    EasyMock.expect(getEntityView.getSelect()).andStubReturn(selectItemList);

    EasyMock.expect(edmEntitySet.getEntityType()).andStubReturn(edmEntityType);
    EasyMock.replay(edmEntitySet);
    EasyMock.expect(edmEntityType.getMapping()).andStubReturn(null);
    EasyMock.expect(edmEntityType.getName()).andStubReturn("SalesOrderHeader");
    EasyMock.replay(edmEntityType);
    ArrayList<KeyPredicate> arrayList = new ArrayList<KeyPredicate>();
    arrayList.add(keyPredicate);
    EasyMock.expect(getEntityView.getKeyPredicates()).andStubReturn(
        arrayList);
    EasyMock.replay(getEntityView);

    JPQLContextBuilder contextBuilder1 = JPQLContext.createBuilder(JPQLContextType.SELECT_SINGLE, getEntityView);
    try {
      JPQLSelectSingleContextImpl = (JPQLSelectSingleContext) contextBuilder1.build();
    } catch (ODataJPAModelException e) {
      fail("Model Exception thrown");
    }

    return JPQLSelectSingleContextImpl;
  }

  /**
  * Test method for {@link com.sap.core.odata.processor.jpa.jpql.JPQLSelectSingleStatementBuilder#build)}.
  * @throws EdmException 
   * @throws ODataJPARuntimeException 
  */

  @Test
  public void testBuildSimpleQuery() throws EdmException, ODataJPARuntimeException {
    JPQLSelectSingleContext JPQLSelectSingleContextImpl = createSelectContext();
    JPQLSelectSingleStatementBuilder = new JPQLSelectSingleStatementBuilder(JPQLSelectSingleContextImpl);

    assertEquals("SELECT E1 FROM SalesOrderHeader E1 WHERE E1.Field1 = 1", JPQLSelectSingleStatementBuilder.build().toString());
  }

}
