package com.sap.core.odata.processor.core.jpa.mock.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.easymock.EasyMock;

import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.processor.core.jpa.mock.data.JPATypeMock.JPARelatedTypeMock;
import com.sap.core.odata.processor.core.jpa.mock.data.JPATypeMock.JPATypeEmbeddableMock;
import com.sap.core.odata.processor.core.jpa.mock.data.JPATypeMock.JPATypeEmbeddableMock2;

public class ODataEntryMockUtil {

  public static final int VALUE_MINT = 20;
  public static Calendar VALUE_DATE_TIME = null;
  public static final String VALUE_MSTRING = "Mock";
  public static final long VALUE_MLONG = 1234567890L;
  public static final double VALUE_MDOUBLE = 20.12;
  public static final byte VALUE_MBYTE = 0XA;
  public static final byte[] VALUE_MBYTEARRAY = { 0XA, 0XB };
  public static final float VALUE_MFLOAT = 2.00F;
  public static final UUID VALUE_UUID = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
  public static final short VALUE_SHORT = 2;

  public static ODataEntry mockODataEntry(String entityName) {
    ODataEntry oDataEntry = EasyMock.createMock(ODataEntry.class);
    EasyMock.expect(oDataEntry.getProperties()).andReturn(mockODataEntryProperties(entityName)).anyTimes();

    EasyMock.expect(oDataEntry.containsInlineEntry()).andReturn(false);
    EasyMock.replay(oDataEntry);
    return oDataEntry;
  }

  public static ODataEntry mockODataEntryWithComplexType(String entityName) {
    ODataEntry oDataEntry = EasyMock.createMock(ODataEntry.class);
    EasyMock.expect(oDataEntry.getProperties()).andReturn(mockODataEntryPropertiesWithComplexType(entityName)).anyTimes();

    EasyMock.expect(oDataEntry.containsInlineEntry()).andReturn(false);
    EasyMock.replay(oDataEntry);
    return oDataEntry;
  }

  public static Map<String, Object> mockODataEntryProperties(String entityName) {
    Map<String, Object> propertyMap = new HashMap<String, Object>();

    if (entityName.equals(JPATypeMock.ENTITY_NAME)) {
      propertyMap.put(JPATypeMock.PROPERTY_NAME_MINT, VALUE_MINT);

      VALUE_DATE_TIME = Calendar.getInstance(TimeZone.getDefault());
      VALUE_DATE_TIME.set(2013, 1, 1, 1, 1, 1);
      propertyMap.put(JPATypeMock.PROPERTY_NAME_MDATETIME, VALUE_DATE_TIME);

      propertyMap.put(JPATypeMock.PROPERTY_NAME_MSTRING, VALUE_MSTRING);
    }
    else if (entityName.equals(JPARelatedTypeMock.ENTITY_NAME)) {
      propertyMap.put(JPARelatedTypeMock.PROPERTY_NAME_MLONG, VALUE_MLONG);
      propertyMap.put(JPARelatedTypeMock.PROPERTY_NAME_MDOUBLE, VALUE_MDOUBLE);
      propertyMap.put(JPARelatedTypeMock.PROPERTY_NAME_MBYTE, VALUE_MBYTE);
      propertyMap.put(JPARelatedTypeMock.PROPERTY_NAME_MBYTEARRAY, VALUE_MBYTEARRAY);
    }
    else if (entityName.equals(JPATypeEmbeddableMock.ENTITY_NAME)) {
      propertyMap.put(JPATypeEmbeddableMock.PROPERTY_NAME_MSHORT, VALUE_SHORT);
      propertyMap.put(JPATypeEmbeddableMock.PROPERTY_NAME_MEMBEDDABLE, mockODataEntryProperties(JPATypeEmbeddableMock2.ENTITY_NAME));
    }
    else if (entityName.equals(JPATypeEmbeddableMock2.ENTITY_NAME)) {
      propertyMap.put(JPATypeEmbeddableMock2.PROPERTY_NAME_MFLOAT, VALUE_MFLOAT);
      propertyMap.put(JPATypeEmbeddableMock2.PROPERTY_NAME_MUUID, VALUE_UUID);
    }

    return propertyMap;
  }

  public static Map<String, Object> mockODataEntryPropertiesWithComplexType(String entityName) {
    Map<String, Object> propertyMap = mockODataEntryProperties(entityName);
    propertyMap.put(JPATypeMock.PROPERTY_NAME_MCOMPLEXTYPE, mockODataEntryProperties(JPATypeEmbeddableMock.ENTITY_NAME));
    return propertyMap;
  }

  public static Map<String, Object> mockODataEntryPropertiesWithInline(String entityName) {
    Map<String, Object> propertyMap = mockODataEntryProperties(entityName);
    List<ODataEntry> relatedEntries = new ArrayList<ODataEntry>();
    relatedEntries.add(mockODataEntry(JPARelatedTypeMock.ENTITY_NAME));
    ODataFeed feed = EasyMock.createMock(ODataFeed.class);
    EasyMock.expect(feed.getEntries()).andReturn(relatedEntries);
    EasyMock.replay(feed);
    propertyMap.put(JPATypeMock.NAVIGATION_PROPERTY_X, feed);

    return propertyMap;

  }

  public static ODataEntry mockODataEntryWithInline(String entityName) {
    ODataEntry oDataEntry = EasyMock.createMock(ODataEntry.class);
    EasyMock.expect(oDataEntry.getProperties()).andReturn(mockODataEntryPropertiesWithInline(entityName)).anyTimes();
    if (entityName.equals(JPATypeMock.ENTITY_NAME))
      EasyMock.expect(oDataEntry.containsInlineEntry()).andReturn(true);
    else
      EasyMock.expect(oDataEntry.containsInlineEntry()).andReturn(false);
    EasyMock.replay(oDataEntry);
    return oDataEntry;
  }
}
