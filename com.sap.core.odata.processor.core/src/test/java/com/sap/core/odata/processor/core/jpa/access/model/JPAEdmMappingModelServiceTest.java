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
package com.sap.core.odata.processor.core.jpa.access.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.processor.core.jpa.mock.ODataJPAContextMock;

public class JPAEdmMappingModelServiceTest extends JPAEdmMappingModelService {

  private static JPAEdmMappingModelServiceTest objJPAEdmMappingModelServiceTest;

  private static final String MAPPING_FILE_CORRECT = "SalesOrderProcessingMappingModels.xml";
  private static final String MAPPING_FILE_INCORRECT = "TEST.xml";

  private static int VARIANT_MAPPING_FILE; // 0 FOR INCORRECT, 1 FOR CORRECT

  private static String PERSISTENCE_UNIT_NAME_JPA = "salesorderprocessing";
  private static String PERSISTENCE_UNIT_NAME_EDM = "SalesOrderProcessing";

  private static String ENTITY_TYPE_NAME_JPA = "SalesOrderHeader";
  private static String ENTITY_TYPE_NAME_EDM = "SalesOrder";
  private static String ENTITY_SET_NAME_EDM = "SalesOrders";
  private static String RELATIONSHIP_NAME_JPA = "salesOrderItems";
  private static String RELATIONSHIP_NAME_EDM = "SalesOrderItemDetails";
  private static String ATTRIBUTE_NAME_JPA = "netAmount";
  private static String ATTRIBUTE_NAME_EDM = "NetAmount";
  private static String EMBEDDABLE_TYPE_NAME_JPA = "SalesOrderItemKey";
  private static String EMBEDDABLE_ATTRIBUTE_NAME_JPA = "liId";
  private static String EMBEDDABLE_ATTRIBUTE_NAME_EDM = "ID";
  private static String EMBEDDABLE_TYPE_2_NAME_JPA = "SalesOrderItemKey";

  private static String ENTITY_TYPE_NAME_JPA_WRONG = "SalesOrderHeaders";
  private static String RELATIONSHIP_NAME_JPA_WRONG = "value";
  private static String EMBEDDABLE_TYPE_NAME_JPA_WRONG = "SalesOrderItemKeys";

  public JPAEdmMappingModelServiceTest() {
    super(ODataJPAContextMock.mockODataJPAContext());
  }

  @BeforeClass
  public static void setup() {
    objJPAEdmMappingModelServiceTest = new JPAEdmMappingModelServiceTest();
    VARIANT_MAPPING_FILE = 1;
    objJPAEdmMappingModelServiceTest.loadMappingModel();
  }

  @Test
  public void testLoadMappingModel() {
    VARIANT_MAPPING_FILE = 1;
    loadMappingModel();
    assertTrue(isMappingModelExists());
  }

  @Test
  public void testLoadMappingModelNegative() {
    VARIANT_MAPPING_FILE = 0;
    loadMappingModel();
    assertFalse(isMappingModelExists());
    // reset it for other JUnits
    VARIANT_MAPPING_FILE = 1;
    loadMappingModel();
  }

  @Test
  public void testIsMappingModelExists() {
    assertTrue(objJPAEdmMappingModelServiceTest.isMappingModelExists());
  }

  @Test
  public void testGetJPAEdmMappingModel() {
    assertNotNull(objJPAEdmMappingModelServiceTest.getJPAEdmMappingModel());
  }

  @Test
  public void testMapJPAPersistenceUnit() {
    assertEquals(PERSISTENCE_UNIT_NAME_EDM, objJPAEdmMappingModelServiceTest.mapJPAPersistenceUnit(PERSISTENCE_UNIT_NAME_JPA));
  }

  @Test
  public void testMapJPAPersistenceUnitNegative() {
    assertNull(objJPAEdmMappingModelServiceTest.mapJPAPersistenceUnit(PERSISTENCE_UNIT_NAME_EDM));// Wrong value to bring null
  }

  @Test
  public void testMapJPAEntityType() {
    assertEquals(ENTITY_TYPE_NAME_EDM, objJPAEdmMappingModelServiceTest.mapJPAEntityType(ENTITY_TYPE_NAME_JPA));
  }

  @Test
  public void testMapJPAEntityTypeNegative() {
    assertNull(objJPAEdmMappingModelServiceTest.mapJPAEntityType(ENTITY_TYPE_NAME_JPA_WRONG));// Wrong value to bring null
  }

  @Test
  public void testMapJPAEntitySet() {
    assertEquals(ENTITY_SET_NAME_EDM, objJPAEdmMappingModelServiceTest.mapJPAEntitySet(ENTITY_TYPE_NAME_JPA));
  }

  @Test
  public void testMapJPAEntitySetNegative() {
    assertNull(objJPAEdmMappingModelServiceTest.mapJPAEntitySet(ENTITY_TYPE_NAME_JPA_WRONG));// Wrong value to bring null
  }

  @Test
  public void testMapJPAAttribute() {
    assertEquals(ATTRIBUTE_NAME_EDM, objJPAEdmMappingModelServiceTest.mapJPAAttribute(ENTITY_TYPE_NAME_JPA, ATTRIBUTE_NAME_JPA));
  }

  @Test
  public void testMapJPAAttributeNegative() {
    assertNull(objJPAEdmMappingModelServiceTest.mapJPAAttribute(ENTITY_TYPE_NAME_JPA, ATTRIBUTE_NAME_JPA + "AA"));// Wrong value to bring null
  }

  @Test
  public void testMapJPARelationship() {
    assertEquals(RELATIONSHIP_NAME_EDM, objJPAEdmMappingModelServiceTest.mapJPARelationship(ENTITY_TYPE_NAME_JPA, RELATIONSHIP_NAME_JPA));
  }

  @Test
  public void testMapJPARelationshipNegative() {
    assertNull(objJPAEdmMappingModelServiceTest.mapJPARelationship(ENTITY_TYPE_NAME_JPA, RELATIONSHIP_NAME_JPA_WRONG));// Wrong value to bring null
  }

  @Test
  public void testMapJPAEmbeddableType() {
    assertEquals("SalesOrderLineItemKey", objJPAEdmMappingModelServiceTest.mapJPAEmbeddableType("SalesOrderItemKey"));
  }

  @Test
  public void testMapJPAEmbeddableTypeNegative() {
    assertNull(objJPAEdmMappingModelServiceTest.mapJPAEmbeddableType(EMBEDDABLE_TYPE_NAME_JPA_WRONG));// Wrong value to bring null
  }

  @Test
  public void testMapJPAEmbeddableTypeAttribute() {
    assertEquals(EMBEDDABLE_ATTRIBUTE_NAME_EDM, objJPAEdmMappingModelServiceTest.mapJPAEmbeddableTypeAttribute(EMBEDDABLE_TYPE_NAME_JPA, EMBEDDABLE_ATTRIBUTE_NAME_JPA));
  }

  @Test
  public void testMapJPAEmbeddableTypeAttributeNegative() {
    assertNull(objJPAEdmMappingModelServiceTest.mapJPAEmbeddableTypeAttribute(EMBEDDABLE_TYPE_NAME_JPA_WRONG, EMBEDDABLE_ATTRIBUTE_NAME_JPA));
  }

  @Test
  public void testCheckExclusionOfJPAEntityType() {
    assertTrue(!objJPAEdmMappingModelServiceTest.checkExclusionOfJPAEntityType(ENTITY_TYPE_NAME_JPA));
  }

  @Test
  public void testCheckExclusionOfJPAAttributeType() {
    assertTrue(!objJPAEdmMappingModelServiceTest.checkExclusionOfJPAAttributeType(ENTITY_TYPE_NAME_JPA, ATTRIBUTE_NAME_JPA));
  }

  @Test
  public void testCheckExclusionOfJPAEmbeddableType() {
    assertTrue(!objJPAEdmMappingModelServiceTest.checkExclusionOfJPAEmbeddableType(EMBEDDABLE_TYPE_2_NAME_JPA));
  }

  @Test
  public void testCheckExclusionOfJPAEmbeddableAttributeType() {
    assertTrue(!objJPAEdmMappingModelServiceTest.checkExclusionOfJPAEmbeddableAttributeType(EMBEDDABLE_TYPE_NAME_JPA, EMBEDDABLE_ATTRIBUTE_NAME_JPA));
  }

  /**
   * This method is for loading the xml file for testing.
   */
  @Override
  protected InputStream loadMappingModelInputStream() {
    if (VARIANT_MAPPING_FILE == 1) {
      return ClassLoader.getSystemResourceAsStream(MAPPING_FILE_CORRECT);
    } else {
      return ClassLoader.getSystemResourceAsStream(MAPPING_FILE_INCORRECT);
    }
  }
}
