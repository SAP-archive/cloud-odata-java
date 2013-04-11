package com.sap.core.odata.fit.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.api.uri.info.GetSimplePropertyUriInfo;

public class MapProcessor extends ODataSingleProcessor {

  public static final int RECORD_COUNT = 10;
  private final ArrayList<HashMap<String, String>> records = new ArrayList<HashMap<String, String>>();

  {
    HashMap<String, String> record;

    for (int i = 1; i <= RECORD_COUNT; i++) {
      record = new HashMap<String, String>();
      record.put("P01", "V01." + i);
      record.put("P02", "V02." + i);
      record.put("P03", "V03." + i);
      records.add(record);
    }
  }

  private int indexOf(final String key, final String value) {
    for (int i = 0; i < RECORD_COUNT; i++) {
      if (records.get(i).containsKey(key) && records.get(i).containsValue(value)) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public ODataResponse readEntitySet(final GetEntitySetUriInfo uriInfo, final String contentType) throws ODataException {
    final EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build();

    final List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();

    for (final HashMap<String, String> record : records) {
      final HashMap<String, Object> data = new HashMap<String, Object>();

      for (final String pName : uriInfo.getTargetEntitySet().getEntityType().getPropertyNames()) {
        final EdmProperty property = (EdmProperty) uriInfo.getTargetEntitySet().getEntityType().getProperty(pName);
        final String mappedPropertyName = (String) property.getMapping().getObject();
        data.put(pName, record.get(mappedPropertyName));
      }

      values.add(data);
    }

    final ODataResponse response = EntityProvider.writeFeed(contentType, uriInfo.getTargetEntitySet(), values, properties);

    return response;
  }

  @Override
  public ODataResponse readEntity(final GetEntityUriInfo uriInfo, final String contentType) throws ODataException {
    final EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build();

    // query
    final String mappedKeyName = (String) uriInfo.getTargetEntitySet().getEntityType().getKeyProperties().get(0).getMapping().getObject();
    final String keyValue = uriInfo.getKeyPredicates().get(0).getLiteral();
    final int index = indexOf(mappedKeyName, keyValue);
    if ((index < 0) || (index > records.size())) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY.addContent(keyValue));
    }
    final HashMap<String, String> record = records.get(index);

    final HashMap<String, Object> data = new HashMap<String, Object>();
    for (final String pName : uriInfo.getTargetEntitySet().getEntityType().getPropertyNames()) {
      final EdmProperty property = (EdmProperty) uriInfo.getTargetEntitySet().getEntityType().getProperty(pName);
      final String mappedPropertyName = (String) property.getMapping().getObject();
      data.put(pName, record.get(mappedPropertyName));
    }

    final ODataResponse response = EntityProvider.writeEntry(contentType, uriInfo.getTargetEntitySet(), data, properties);
    return response;
  }

  @Override
  public ODataResponse readEntitySimplePropertyValue(final GetSimplePropertyUriInfo uriInfo, final String contentType) throws ODataException {
    final List<EdmProperty> propertyPath = uriInfo.getPropertyPath();
    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);

    final String mappedKeyName = (String) uriInfo.getTargetEntitySet().getEntityType().getKeyProperties().get(0).getMapping().getObject();
    final String keyValue = uriInfo.getKeyPredicates().get(0).getLiteral();

    final int index = indexOf(mappedKeyName, keyValue);
    if ((index < 0) || (index > records.size())) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY.addContent(keyValue));
    }
    final HashMap<String, String> record = records.get(index);

    final String mappedPropertyName = (String) property.getMapping().getObject();
    final Object value = record.get(mappedPropertyName);

    final ODataResponse response = EntityProvider.writePropertyValue(property, value);
    return response;
  }

}
