package com.sap.core.odata.fit.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.BasicProvider;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.api.uri.info.GetSimplePropertyUriInfo;

public class MapProcessor extends ODataSingleProcessor {

  public static final int RECORD_COUNT = 10;
  private ArrayList<HashMap<String, String>> records = new ArrayList<HashMap<String, String>>();

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

  private int indexOf(String key, String value) {
    for(int i = 0; i< RECORD_COUNT; i++) {
      if (records.get(i).containsKey(key) && records.get(i).containsValue(value)) {
        return i;
      }
    }
    return -1;
  }
  
  @Override
  public ODataResponse readEntitySet(GetEntitySetUriInfo uriInfo, String contentType) throws ODataException {
    EntityProvider ep = EntityProvider.create(contentType);
    EntityProviderProperties properties = EntityProviderProperties.baseUri(getContext().getPathInfo().getServiceRoot()).build();

    List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();

    for (HashMap<String, String> record : records) {
      HashMap<String, Object> data = new HashMap<String, Object>();
      
      for (String pName: uriInfo.getTargetEntitySet().getEntityType().getPropertyNames()) {
        EdmProperty property = (EdmProperty) uriInfo.getTargetEntitySet().getEntityType().getProperty(pName);
        String mappedPropertyName = (String) property.getMapping().getDataContainer();
        data.put(pName, record.get(mappedPropertyName)); 
      }
      
      values.add(data);
    }
    
    ODataResponse response = ep.writeFeed(uriInfo.getTargetEntitySet(), values, properties);

    return response;
  }

   @Override
  public ODataResponse readEntity(GetEntityUriInfo uriInfo, String contentType) throws ODataException {
    EntityProvider ep = EntityProvider.create(contentType);
    EntityProviderProperties properties = EntityProviderProperties.baseUri(getContext().getPathInfo().getServiceRoot()).build();

    // query
    String mappedKeyName = (String) uriInfo.getTargetEntitySet().getEntityType().getKeyProperties().get(0).getMapping().getDataContainer();
    String keyValue = uriInfo.getKeyPredicates().get(0).getLiteral();
    int index = indexOf(mappedKeyName, keyValue);
    if (index < 0 || index > records.size()) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY.addContent(keyValue));
    }
    HashMap<String, String> record = records.get(index); 
    
    HashMap<String, Object> data = new HashMap<String, Object>();
    for (String pName: uriInfo.getTargetEntitySet().getEntityType().getPropertyNames()) {
      EdmProperty property = (EdmProperty) uriInfo.getTargetEntitySet().getEntityType().getProperty(pName);
      String mappedPropertyName = (String) property.getMapping().getDataContainer();
      data.put(pName, record.get(mappedPropertyName)); 
    }

    ODataResponse response = ep.writeEntry(uriInfo.getTargetEntitySet(), data, properties);
    return response;
  }

  @Override
  public ODataResponse readEntitySimplePropertyValue(GetSimplePropertyUriInfo uriInfo, String contentType) throws ODataException {
    BasicProvider bp = BasicProvider.create();

    final List<EdmProperty> propertyPath = uriInfo.getPropertyPath();
    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);

    String mappedKeyName = (String) uriInfo.getTargetEntitySet().getEntityType().getKeyProperties().get(0).getMapping().getDataContainer();
    String keyValue = uriInfo.getKeyPredicates().get(0).getLiteral();

    int index = indexOf(mappedKeyName, keyValue);
    if (index < 0 || index > records.size()) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY.addContent(keyValue));
    }
    HashMap<String, String> record = records.get(index); 
    
    String mappedPropertyName = (String) property.getMapping().getDataContainer();
    Object value = record.get(mappedPropertyName);
   
    ODataResponse response = bp.writePropertyValue(property, value);
    return response;
  }

   
}
