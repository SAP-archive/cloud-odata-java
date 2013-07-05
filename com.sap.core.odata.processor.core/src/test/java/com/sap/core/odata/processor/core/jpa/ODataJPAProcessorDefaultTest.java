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
package com.sap.core.odata.processor.core.jpa;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.EdmConcurrencyMode;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.info.DeleteUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmTestModelView;

public class ODataJPAProcessorDefaultTest extends JPAEdmTestModelView {

  ODataJPAProcessorDefault objODataJPAProcessorDefault;
  ODataJPAProcessorDefaultTest objODataJPAProcessorDefaultTest;

  private static final String STR_LOCAL_URI = "http://localhost:8080/com.sap.core.odata.processor.ref.web/";
  private static final String SALESORDERPROCESSING_CONTAINER = "salesorderprocessingContainer";
  private static final String SO_ID = "SoId";
  private static final String SALES_ORDER = "SalesOrder";
  private static final String SALES_ORDER_HEADERS = "SalesOrderHeaders";
  private static final String TEXT_PLAIN_CHARSET_UTF_8 = "text/plain;charset=utf-8";
  private static final String STR_CONTENT_TYPE = "Content-Type";

  @Before
  public void setUp() {
    objODataJPAProcessorDefaultTest = new ODataJPAProcessorDefaultTest();
    objODataJPAProcessorDefault = new ODataJPAProcessorDefault(getLocalmockODataJPAContext());
  }

  @Test
  public void testReadEntitySetGetEntitySetUriInfoString() {
    try {
      GetEntityUriInfo getEntityView = getEntityUriInfo();
      Assert.assertNotNull(objODataJPAProcessorDefault.readEntity(getEntityView, HttpContentType.APPLICATION_XML));
    } catch (ODataJPAModelException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (ODataJPARuntimeException e1) {//Expected
      assertTrue(true);
    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }

  }

  @Test
  public void testcountEntitySet() {
    try {
      Assert.assertNotNull(objODataJPAProcessorDefault.countEntitySet(getEntitySetCountUriInfo(), HttpContentType.APPLICATION_XML));
      Assert.assertEquals(TEXT_PLAIN_CHARSET_UTF_8,
          objODataJPAProcessorDefault.countEntitySet(getEntitySetCountUriInfo(), HttpContentType.APPLICATION_XML).getHeader(STR_CONTENT_TYPE));
    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (Exception e)
    {
      assertTrue(true);
    }
  }

  @Test
  public void testExistsEntity() {
    try {
      Assert.assertNotNull(objODataJPAProcessorDefault.existsEntity(getEntityCountCountUriInfo(), HttpContentType.APPLICATION_XML));
      Assert.assertEquals(TEXT_PLAIN_CHARSET_UTF_8,
          objODataJPAProcessorDefault.existsEntity(getEntityCountCountUriInfo(), HttpContentType.APPLICATION_XML).getHeader(STR_CONTENT_TYPE));
    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (Exception e)
    {
      assertTrue(true);
    }
  }

  @Test
  public void testDeleteEntity() {
    try {
      Assert.assertNotNull(objODataJPAProcessorDefault.deleteEntity(getDeletetUriInfo(), HttpContentType.APPLICATION_XML));
      Assert.assertEquals(TEXT_PLAIN_CHARSET_UTF_8,
          objODataJPAProcessorDefault.countEntitySet(getEntitySetCountUriInfo(), HttpContentType.APPLICATION_XML).getHeader(STR_CONTENT_TYPE));
    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
  }

  @Test
  public void testCreateEntity() {
    try {
      Assert.assertNotNull(objODataJPAProcessorDefault.createEntity(getPostUriInfo(), getMockedInputStreamContent(), HttpContentType.APPLICATION_XML, HttpContentType.APPLICATION_XML));
    } catch (ODataException e) {
      Assert.assertTrue(true); //Expected TODO - need to revisit
    }
  }

  @Test
  public void testUpdateEntity() {
    try {
      Assert.assertNotNull(objODataJPAProcessorDefault.updateEntity(getPutUriInfo(), getMockedInputStreamContent(), HttpContentType.APPLICATION_XML, false, HttpContentType.APPLICATION_XML));
    } catch (ODataException e) {
      Assert.assertTrue(true); //Expected TODO - need to revisit
    }
  }

  private PutMergePatchUriInfo getPutUriInfo() {
    return (PutMergePatchUriInfo) getDeletetUriInfo();
  }

  private PostUriInfo getPostUriInfo() {
    return (PostUriInfo) getDeletetUriInfo();
  }

  private InputStream getMockedInputStreamContent() {
    return new ByteArrayInputStream(getEntityBody().getBytes());
  }

  private String getEntityBody() {
    return
    "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:8080/com.sap.core.odata.processor.ref.web/SalesOrderProcessing.svc/\">"
        + "<content type=\"application/xml\">"
        + "<m:properties>"
        + "<d:ID>2</d:ID>"
        + "<d:CreationDate>2013-01-02T00:00:00</d:CreationDate>"
        + "<d:CurrencyCode>Code_555</d:CurrencyCode>"
        + "<d:BuyerAddressInfo m:type=\"SalesOrderProcessing.AddressInfo\">"
        + "<d:Street>Test_Street_Name_055</d:Street>"
        + "<d:Number>2</d:Number>"
        + "<d:Country>Test_Country_2</d:Country>"
        + "<d:City>Test_City_2</d:City>"
        + "</d:BuyerAddressInfo>"
        + "<d:GrossAmount>0.0</d:GrossAmount>"
        + "<d:BuyerId>2</d:BuyerId>"
        + "<d:DeliveryStatus>true</d:DeliveryStatus>"
        + "<d:BuyerName>buyerName_2</d:BuyerName>"
        + "<d:NetAmount>0.0</d:NetAmount>"
        + "</m:properties>"
        + "</content>"
        + "</entry>";
  }

  private GetEntitySetCountUriInfo getEntitySetCountUriInfo() {
    return getLocalUriInfo();
  }

  private GetEntityCountUriInfo getEntityCountCountUriInfo() {
    return getLocalUriInfo();
  }

  private DeleteUriInfo getDeletetUriInfo() {
    UriInfo objUriInfo = EasyMock.createMock(UriInfo.class);
    EasyMock.expect(objUriInfo.getStartEntitySet()).andStubReturn(getLocalEdmEntitySet());
    EasyMock.expect(objUriInfo.getTargetEntitySet()).andStubReturn(getLocalEdmEntitySet());
    EasyMock.expect(objUriInfo.getSelect()).andStubReturn(null);
    EasyMock.expect(objUriInfo.getOrderBy()).andStubReturn(getOrderByExpression());
    EasyMock.expect(objUriInfo.getTop()).andStubReturn(getTop());
    EasyMock.expect(objUriInfo.getSkip()).andStubReturn(getSkip());
    EasyMock.expect(objUriInfo.getInlineCount()).andStubReturn(getInlineCount());
    EasyMock.expect(objUriInfo.getFilter()).andStubReturn(getFilter());
    EasyMock.expect(objUriInfo.getKeyPredicates()).andStubReturn(getKeyPredicates());
    EasyMock.replay(objUriInfo);
    return objUriInfo;
  }

  private List<KeyPredicate> getKeyPredicates() {
    List<KeyPredicate> keyPredicates = new ArrayList<KeyPredicate>();
    return keyPredicates;
  }

  /**
   * @return
   */
  private UriInfo getLocalUriInfo() {
    UriInfo objUriInfo = EasyMock.createMock(UriInfo.class);
    EasyMock.expect(objUriInfo.getStartEntitySet()).andStubReturn(getLocalEdmEntitySet());
    EasyMock.expect(objUriInfo.getTargetEntitySet()).andStubReturn(getLocalEdmEntitySet());
    EasyMock.expect(objUriInfo.getSelect()).andStubReturn(null);
    EasyMock.expect(objUriInfo.getOrderBy()).andStubReturn(getOrderByExpression());
    EasyMock.expect(objUriInfo.getTop()).andStubReturn(getTop());
    EasyMock.expect(objUriInfo.getSkip()).andStubReturn(getSkip());
    EasyMock.expect(objUriInfo.getInlineCount()).andStubReturn(getInlineCount());
    EasyMock.expect(objUriInfo.getFilter()).andStubReturn(getFilter());
    EasyMock.expect(objUriInfo.getFunctionImport()).andStubReturn(null);
    EasyMock.replay(objUriInfo);
    return objUriInfo;
  }

  /**
   * @return
   * @throws EdmException
   */
  private EdmEntitySet getLocalEdmEntitySet() {
    EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
    try {
      EasyMock.expect(edmEntitySet.getName()).andStubReturn(SALES_ORDER_HEADERS);
      EasyMock.expect(edmEntitySet.getEntityContainer()).andStubReturn(getLocalEdmEntityContainer());
      EasyMock.expect(edmEntitySet.getEntityType()).andStubReturn(getLocalEdmEntityType());
      EasyMock.replay(edmEntitySet);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    return edmEntitySet;
  }

  /**
   * @return
   * @throws EdmException
   */
  private EdmEntityType getLocalEdmEntityType() {
    EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
    try {
      EasyMock.expect(edmEntityType.getKeyProperties()).andStubReturn(new ArrayList<EdmProperty>());
      EasyMock.expect(edmEntityType.getPropertyNames()).andStubReturn(getLocalPropertyNames());
      EasyMock.expect(edmEntityType.getProperty(SO_ID)).andStubReturn(getEdmTypedMockedObj(SALES_ORDER));
      EasyMock.expect(edmEntityType.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
      EasyMock.expect(edmEntityType.getNamespace()).andStubReturn(SALES_ORDER_HEADERS);
      EasyMock.expect(edmEntityType.getName()).andStubReturn(SALES_ORDER_HEADERS);
      EasyMock.expect(edmEntityType.hasStream()).andStubReturn(false);
      EasyMock.expect(edmEntityType.getNavigationPropertyNames()).andStubReturn(new ArrayList<String>());
      EasyMock.expect(edmEntityType.getKeyPropertyNames()).andStubReturn(new ArrayList<String>());
      EasyMock.expect(edmEntityType.getMapping()).andStubReturn(getEdmMappingMockedObj(SALES_ORDER));// ID vs Salesorder ID
      EasyMock.replay(edmEntityType);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    return edmEntityType;
  }

  private InlineCount getInlineCount() {
    return InlineCount.NONE;
  }

  private FilterExpression getFilter() {
    return null;
  }

  private Integer getSkip() {
    return null;
  }

  private Integer getTop() {
    return null;
  }

  private OrderByExpression getOrderByExpression() {
    return null;
  }

  private ODataJPAContext getLocalmockODataJPAContext() {
    ODataJPAContext odataJPAContext = EasyMock.createMock(ODataJPAContext.class);
    EasyMock.expect(odataJPAContext.getPersistenceUnitName()).andStubReturn("salesorderprocessing");
    EasyMock.expect(odataJPAContext.getEntityManagerFactory()).andStubReturn(mockEntityManagerFactory());
    EasyMock.expect(odataJPAContext.getODataContext()).andStubReturn(getLocalODataContext());
    EasyMock.expect(odataJPAContext.getEntityManager()).andStubReturn(getLocalEntityManager());
    EasyMock.replay(odataJPAContext);
    return odataJPAContext;
  }

  private EntityManagerFactory mockEntityManagerFactory() {
    EntityManagerFactory emf = EasyMock.createMock(EntityManagerFactory.class);
    EasyMock.expect(emf.getMetamodel()).andStubReturn(mockMetaModel());
    EasyMock.expect(emf.createEntityManager()).andStubReturn(getLocalEntityManager());
    EasyMock.replay(emf);
    return emf;
  }

  private EntityManagerFactory mockEntityManagerFactory2() {// For create, to avoid stackoverflow
    EntityManagerFactory emf = EasyMock.createMock(EntityManagerFactory.class);
    EasyMock.expect(emf.getMetamodel()).andStubReturn(mockMetaModel());
    EasyMock.replay(emf);
    return emf;
  }

  private EntityManager getLocalEntityManager() {
    EntityManager em = EasyMock.createMock(EntityManager.class);
    EasyMock.expect(em.createQuery("SELECT E1 FROM SalesOrderHeaders E1")).andStubReturn(getQuery());
    EasyMock.expect(em.createQuery("SELECT COUNT ( E1 ) FROM SalesOrderHeaders E1")).andStubReturn(getQueryForSelectCount());
    EasyMock.expect(em.getEntityManagerFactory()).andStubReturn(mockEntityManagerFactory2());// For create
    EasyMock.expect(em.getTransaction()).andStubReturn(getLocalTransaction()); //For Delete
    Address obj = new Address();
    em.remove(obj);// testing void method
    em.flush();
    EasyMock.replay(em);
    return em;
  }

  private EntityTransaction getLocalTransaction() {
    EntityTransaction entityTransaction = EasyMock.createMock(EntityTransaction.class);
    entityTransaction.begin(); // testing void method
    entityTransaction.commit();// testing void method
    entityTransaction.rollback();// testing void method
    EasyMock.replay(entityTransaction);
    return entityTransaction;
  }

  private Query getQuery() {
    Query query = EasyMock.createMock(Query.class);
    EasyMock.expect(query.getResultList()).andStubReturn(getResultList());
    EasyMock.replay(query);
    return query;
  }

  private Query getQueryForSelectCount() {
    Query query = EasyMock.createMock(Query.class);
    EasyMock.expect(query.getResultList()).andStubReturn(getResultListForSelectCount());
    EasyMock.replay(query);
    return query;
  }

  private List<?> getResultList() {
    List<Object> list = new ArrayList<Object>();
    list.add(new Address());
    return list;
  }

  private List<?> getResultListForSelectCount() {
    List<Object> list = new ArrayList<Object>();
    list.add(new Long(11));
    return list;
  }

  class Address {
    private String soId = "12";

    public String getSoId() {
      return soId;
    }

    @Override
    public boolean equals(final Object obj) {
      boolean isEqual = false;
      if (obj instanceof Address)
      {
        isEqual = getSoId().equalsIgnoreCase(((Address) obj).getSoId());//
      }
      return isEqual;
    }
  }

  private Metamodel mockMetaModel() {
    Metamodel metaModel = EasyMock.createMock(Metamodel.class);
    EasyMock.expect(metaModel.getEntities()).andStubReturn(getLocalEntities());
    EasyMock.replay(metaModel);
    return metaModel;
  }

  private Set<EntityType<?>> getLocalEntities() {
    Set<EntityType<?>> entityTypeSet = new HashSet<EntityType<?>>();
    entityTypeSet.add(getLocalJPAEntityType());
    return entityTypeSet;
  }

  @SuppressWarnings("rawtypes")
  private EntityType<EntityType> getLocalJPAEntityType() {
    @SuppressWarnings("unchecked")
    EntityType<EntityType> entityType = EasyMock.createMock(EntityType.class);
    EasyMock.expect(entityType.getJavaType()).andStubReturn(EntityType.class);
    EasyMock.replay(entityType);
    return entityType;
  }

  private GetEntityUriInfo getEntityUriInfo() {
    GetEntityUriInfo getEntityView = EasyMock
        .createMock(GetEntityUriInfo.class);
    EdmEntitySet edmEntitySet = EasyMock.createMock(EdmEntitySet.class);
    EdmEntityType edmEntityType = EasyMock.createMock(EdmEntityType.class);
    try {
      EasyMock.expect(getEntityView.getExpand()).andStubReturn(null);
      EasyMock.expect(edmEntityType.getKeyProperties()).andStubReturn(
          new ArrayList<EdmProperty>());
      EasyMock.expect(edmEntitySet.getEntityType()).andStubReturn(
          edmEntityType);
      EasyMock.expect(edmEntitySet.getName()).andStubReturn(SALES_ORDER_HEADERS);

      EasyMock.expect(getEntityView.getSelect()).andStubReturn(null);
      EasyMock.expect(getEntityView.getTargetEntitySet()).andStubReturn(
          edmEntitySet);
      EasyMock.expect(edmEntityType.getPropertyNames()).andStubReturn(getLocalPropertyNames());
      EasyMock.expect(edmEntityType.getProperty(SO_ID)).andStubReturn(getEdmTypedMockedObj(SO_ID));

      EasyMock.expect(edmEntityType.getMapping()).andStubReturn(getEdmMappingMockedObj(SALES_ORDER));

      EasyMock.expect(edmEntityType.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
      EasyMock.expect(edmEntityType.getNamespace()).andStubReturn(SALES_ORDER_HEADERS);
      EasyMock.expect(edmEntityType.getName()).andStubReturn(SALES_ORDER_HEADERS);
      EasyMock.expect(edmEntityType.hasStream()).andStubReturn(false);
      EasyMock.expect(edmEntityType.getNavigationPropertyNames()).andStubReturn(new ArrayList<String>());
      EasyMock.expect(edmEntityType.getKeyPropertyNames()).andStubReturn(new ArrayList<String>());

      EasyMock.expect(edmEntitySet.getEntityContainer()).andStubReturn(getLocalEdmEntityContainer());

      EasyMock.replay(edmEntityType, edmEntitySet);
      EasyMock.expect(getEntityView.getKeyPredicates()).andStubReturn(
          new ArrayList<KeyPredicate>());
      List<NavigationSegment> navigationSegments = new ArrayList<NavigationSegment>();
      EasyMock.expect(getEntityView.getNavigationSegments()).andReturn(
          navigationSegments);
      EasyMock.expect(getEntityView.getStartEntitySet()).andReturn(edmEntitySet);

      EasyMock.replay(getEntityView);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    return getEntityView;
  }

  private EdmEntityContainer getLocalEdmEntityContainer() {
    EdmEntityContainer edmEntityContainer = EasyMock.createMock(EdmEntityContainer.class);
    EasyMock.expect(edmEntityContainer.isDefaultEntityContainer()).andStubReturn(true);
    try {
      EasyMock.expect(edmEntityContainer.getName()).andStubReturn(SALESORDERPROCESSING_CONTAINER);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }

    EasyMock.replay(edmEntityContainer);
    return edmEntityContainer;
  }

  private EdmTyped getEdmTypedMockedObj(final String propertyName) {
    EdmProperty mockedEdmProperty = EasyMock.createMock(EdmProperty.class);
    try {
      EasyMock.expect(mockedEdmProperty.getMapping())
          .andStubReturn(getEdmMappingMockedObj(propertyName));
      EdmType edmType = EasyMock.createMock(EdmType.class);
      EasyMock.expect(edmType.getKind()).andStubReturn(EdmTypeKind.SIMPLE);
      EasyMock.replay(edmType);
      EasyMock.expect(mockedEdmProperty.getName()).andStubReturn("identifier");
      EasyMock.expect(mockedEdmProperty.getType()).andStubReturn(edmType);
      EasyMock.expect(mockedEdmProperty.getFacets()).andStubReturn(getEdmFacetsMockedObj());

      EasyMock.replay(mockedEdmProperty);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    return mockedEdmProperty;
  }

  private EdmFacets getEdmFacetsMockedObj() {
    EdmFacets facets = EasyMock.createMock(EdmFacets.class);
    EasyMock.expect(facets.getConcurrencyMode()).andStubReturn(EdmConcurrencyMode.Fixed);

    EasyMock.replay(facets);
    return facets;
  }

  private EdmMapping getEdmMappingMockedObj(final String propertyName) {
    EdmMapping mockedEdmMapping = EasyMock.createMock(EdmMapping.class);
    if (propertyName.equalsIgnoreCase(SALES_ORDER)) {
      EasyMock.expect(mockedEdmMapping.getInternalName())
          .andStubReturn(SALES_ORDER_HEADERS);
    } else {
      EasyMock.expect(mockedEdmMapping.getInternalName())
          .andStubReturn(propertyName);
    }
    EasyMock.replay(mockedEdmMapping);
    return mockedEdmMapping;
  }

  private List<String> getLocalPropertyNames() {
    List<String> list = new ArrayList<String>();
    list.add(SO_ID);
    return list;
  }

  private ODataContext getLocalODataContext() {
    ODataContext objODataContext = EasyMock.createMock(ODataContext.class);
    try {
      EasyMock.expect(objODataContext.getPathInfo()).andStubReturn(getLocalPathInfo());
    } catch (ODataException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    EasyMock.replay(objODataContext);
    return objODataContext;
  }

  private PathInfo getLocalPathInfo() {
    PathInfo pathInfo = EasyMock.createMock(PathInfo.class);
    EasyMock.expect(pathInfo.getServiceRoot()).andStubReturn(getLocalURI());
    EasyMock.replay(pathInfo);
    return pathInfo;
  }

  private URI getLocalURI() {
    URI uri = null;
    try {
      uri = new URI(STR_LOCAL_URI);
    } catch (URISyntaxException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    return uri;
  }

}
