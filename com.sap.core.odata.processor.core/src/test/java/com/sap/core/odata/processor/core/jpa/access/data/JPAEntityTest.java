package com.sap.core.odata.processor.core.jpa.access.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;
import com.sap.core.odata.processor.core.jpa.mock.data.EdmMockUtilV2;
import com.sap.core.odata.processor.core.jpa.mock.data.JPATypeMock;
import com.sap.core.odata.processor.core.jpa.mock.data.JPATypeMock.JPARelatedTypeMock;
import com.sap.core.odata.processor.core.jpa.mock.data.JPATypeMock.JPATypeEmbeddableMock;
import com.sap.core.odata.processor.core.jpa.mock.data.JPATypeMock.JPATypeEmbeddableMock2;
import com.sap.core.odata.processor.core.jpa.mock.data.ODataEntryMockUtil;

public class JPAEntityTest {

  private JPAEntity jpaEntity = null;

  @Test
  public void testCreateODataEntryWithComplexType() {
    try {
      EdmEntitySet edmEntitySet = EdmMockUtilV2.mockEdmEntitySet(JPATypeMock.ENTITY_NAME, true);
      EdmEntityType edmEntityType = edmEntitySet.getEntityType();

      jpaEntity = new JPAEntity(edmEntityType, edmEntitySet);
      jpaEntity.create(ODataEntryMockUtil.mockODataEntryWithComplexType(JPATypeMock.ENTITY_NAME));
    } catch (ODataJPARuntimeException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    JPATypeMock jpaTypeMock = (JPATypeMock) jpaEntity.getJPAEntity();
    assertEquals(jpaTypeMock.getMInt(), ODataEntryMockUtil.VALUE_MINT);
    assertEquals(jpaTypeMock.getMString(), ODataEntryMockUtil.VALUE_MSTRING);
    assertTrue(jpaTypeMock.getMDateTime().equals(ODataEntryMockUtil.VALUE_DATE_TIME));
    JPATypeEmbeddableMock jpaEmbeddableMock = jpaTypeMock.getComplexType();
    assertNotNull(jpaEmbeddableMock);

    assertEquals(jpaEmbeddableMock.getMShort(), ODataEntryMockUtil.VALUE_SHORT);
    JPATypeEmbeddableMock2 jpaEmbeddableMock2 = jpaEmbeddableMock.getMEmbeddable();
    assertNotNull(jpaEmbeddableMock2);
    assertEquals(jpaEmbeddableMock2.getMFloat(), ODataEntryMockUtil.VALUE_MFLOAT,1);
    assertEquals(jpaEmbeddableMock2.getMUUID(), ODataEntryMockUtil.VALUE_UUID);
  }

  @Test
  public void testCreateODataEntry() {
    try {
      EdmEntitySet edmEntitySet = EdmMockUtilV2.mockEdmEntitySet(JPATypeMock.ENTITY_NAME, false);
      EdmEntityType edmEntityType = edmEntitySet.getEntityType();

      jpaEntity = new JPAEntity(edmEntityType, edmEntitySet);
      jpaEntity.create(ODataEntryMockUtil.mockODataEntry(JPATypeMock.ENTITY_NAME));
    } catch (ODataJPARuntimeException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    JPATypeMock jpaTypeMock = (JPATypeMock) jpaEntity.getJPAEntity();
    assertEquals(jpaTypeMock.getMInt(), ODataEntryMockUtil.VALUE_MINT);
    assertEquals(jpaTypeMock.getMString(), ODataEntryMockUtil.VALUE_MSTRING);
    assertTrue(jpaTypeMock.getMDateTime().equals(ODataEntryMockUtil.VALUE_DATE_TIME));
  }

  @Test
  public void testCreateODataEntryWithInline() {
    try {
      EdmEntitySet edmEntitySet = EdmMockUtilV2.mockEdmEntitySet(JPATypeMock.ENTITY_NAME, false);
      EdmEntityType edmEntityType = edmEntitySet.getEntityType();

      jpaEntity = new JPAEntity(edmEntityType, edmEntitySet);
      jpaEntity.create(ODataEntryMockUtil.mockODataEntryWithInline(JPATypeMock.ENTITY_NAME));
    } catch (ODataJPARuntimeException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    JPATypeMock jpaTypeMock = (JPATypeMock) jpaEntity.getJPAEntity();
    assertEquals(jpaTypeMock.getMInt(), ODataEntryMockUtil.VALUE_MINT);
    assertEquals(jpaTypeMock.getMString(), ODataEntryMockUtil.VALUE_MSTRING);
    assertTrue(jpaTypeMock.getMDateTime().equals(ODataEntryMockUtil.VALUE_DATE_TIME));

    JPARelatedTypeMock relatedType = jpaTypeMock.getMRelatedEntity();
    assertNotNull(jpaTypeMock.getMRelatedEntity());
    assertEquals(relatedType.getMByte(), ODataEntryMockUtil.VALUE_MBYTE);
    assertEquals(relatedType.getMByteArray(), ODataEntryMockUtil.VALUE_MBYTEARRAY);
    assertEquals(relatedType.getMDouble(), ODataEntryMockUtil.VALUE_MDOUBLE, 0.0);
    assertEquals(relatedType.getMLong(), ODataEntryMockUtil.VALUE_MLONG);
  }

  @Test
  public void testCreateODataEntryProperty() {
    try {
      EdmEntitySet edmEntitySet = EdmMockUtilV2.mockEdmEntitySet(JPATypeMock.ENTITY_NAME, false);
      EdmEntityType edmEntityType = edmEntitySet.getEntityType();

      jpaEntity = new JPAEntity(edmEntityType, edmEntitySet);
      jpaEntity.create(ODataEntryMockUtil.mockODataEntryProperties(JPATypeMock.ENTITY_NAME));
    } catch (ODataJPARuntimeException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    JPATypeMock jpaTypeMock = (JPATypeMock) jpaEntity.getJPAEntity();
    assertEquals(jpaTypeMock.getMInt(), ODataEntryMockUtil.VALUE_MINT);
    assertEquals(jpaTypeMock.getMString(), ODataEntryMockUtil.VALUE_MSTRING);
    assertTrue(jpaTypeMock.getMDateTime().equals(ODataEntryMockUtil.VALUE_DATE_TIME));
  }

  @Test
  public void testUpdateODataEntry() {
    try {
      EdmEntitySet edmEntitySet = EdmMockUtilV2.mockEdmEntitySet(JPATypeMock.ENTITY_NAME, false);
      EdmEntityType edmEntityType = edmEntitySet.getEntityType();

      jpaEntity = new JPAEntity(edmEntityType, edmEntitySet);
      JPATypeMock jpaTypeMock = new JPATypeMock();
      jpaEntity.setJPAEntity(jpaTypeMock);
      jpaEntity.update(ODataEntryMockUtil.mockODataEntry(JPATypeMock.ENTITY_NAME));
    } catch (ODataJPARuntimeException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    JPATypeMock jpaTypeMock = (JPATypeMock) jpaEntity.getJPAEntity();
    assertEquals(jpaTypeMock.getMInt(), 0);//Key should not be changed
    assertEquals(jpaTypeMock.getMString(), ODataEntryMockUtil.VALUE_MSTRING);
    assertTrue(jpaTypeMock.getMDateTime().equals(ODataEntryMockUtil.VALUE_DATE_TIME));
  }

  @Test
  public void testUpdateODataEntryProperty() {
    try {
      EdmEntitySet edmEntitySet = EdmMockUtilV2.mockEdmEntitySet(JPATypeMock.ENTITY_NAME, false);
      EdmEntityType edmEntityType = edmEntitySet.getEntityType();

      jpaEntity = new JPAEntity(edmEntityType, edmEntitySet);
      JPATypeMock jpaTypeMock = new JPATypeMock();
      jpaEntity.setJPAEntity(jpaTypeMock);
      jpaEntity.update(ODataEntryMockUtil.mockODataEntryProperties(JPATypeMock.ENTITY_NAME));
    } catch (ODataJPARuntimeException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    JPATypeMock jpaTypeMock = (JPATypeMock) jpaEntity.getJPAEntity();
    assertEquals(jpaTypeMock.getMInt(), 0);//Key should not be changed
    assertEquals(jpaTypeMock.getMString(), ODataEntryMockUtil.VALUE_MSTRING);
    assertTrue(jpaTypeMock.getMDateTime().equals(ODataEntryMockUtil.VALUE_DATE_TIME));
  }
}
