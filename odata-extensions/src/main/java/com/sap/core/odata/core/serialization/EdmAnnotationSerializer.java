package com.sap.core.odata.core.serialization;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.annotations.EdmEntity;
import com.sap.core.odata.api.annotations.EdmNavigationProperty;
import com.sap.core.odata.api.annotations.EdmProperty;
import com.sap.core.odata.api.annotations.EdmTypeId;
import com.sap.core.odata.api.annotations.ODataSerializer;
import com.sap.core.odata.api.processor.ODataResponse;

public class EdmAnnotationSerializer implements ODataSerializer<InputStream> {

  private static final Logger LOG = LoggerFactory.getLogger(EdmAnnotationSerializer.class);
  private final String baseUri;
  
  public EdmAnnotationSerializer(String baseUri) {
    this.baseUri = baseUri;
  }

  @Override
  public InputStream serialize(Object obj) {
    byte[] buf = getContent(obj).getBytes();
    InputStream stream = new ByteArrayInputStream(buf);
    return stream;
  }

  public String getContent(Object entity) {
    if(entity == null) {
      return "NULL";
    } else if(isEdmAnnotated(entity)) {
      try {
        return handleEdmAnnotations(entity);
      } catch (Exception e) {
        e.printStackTrace();
        return "Exception with following message occured: " + e.getMessage();
      }
    } else {
      return entity.toString();
    }
  }
  
  public String getContent(ODataResponse response) {
    if(response == null) {
      return "NULL";
    } else {
      return getContent(response.getEntity());
    }
  }

  private boolean isEdmAnnotated(Object entity) {
    if(entity == null) {
      return false;
    } else {
      return null != entity.getClass().getAnnotation(EdmEntity.class);
    }
  }

  private String handleEdmAnnotations(Object entity) throws IllegalArgumentException, IllegalAccessException, IOException {
    LOG.debug("handleEdmAnnotations");

    //
    Writer writer = new StringWriter();
    JsonWriter json = new JsonWriter(writer);

    writeObject(entity, json);
    //

    writer.close();
    return writer.toString();
  }

  private void writeObject(Object entity, JsonWriter json) throws IllegalAccessException {
    List<Field> fields = getAllFields(entity.getClass());
    json.startObject();
    for (Field field: fields) {
      boolean written = writeEdmProperty(entity, json, field);
      if(!written) {
        written = writeEdmNavigationProperty(entity, json, field);
      }
    }
    json.endObject();
  }

  private boolean writeEdmNavigationProperty(Object entity, JsonWriter json, Field field) throws IllegalArgumentException, IllegalAccessException {
    EdmNavigationProperty property = field.getAnnotation(EdmNavigationProperty.class);
    if(property != null) {
      field.setAccessible(true);
      Object fieldValue = field.get(entity);
      Object obj = extractEdmTypeId(fieldValue);
      json.writeProperty("uri", baseUri + property.navigationName() + "('" + obj.toString() + "')");
      return true;
    }
    return false;
  }

  private boolean writeEdmProperty(Object entity, JsonWriter json, Field field) throws IllegalAccessException {
    EdmProperty property = field.getAnnotation(EdmProperty.class);
    if(property != null) {
      field.setAccessible(true);
      Object fieldValue = field.get(entity);
      json.writeEdmProperty(property, fieldValue);
      return true;
    }
    return false;
  }

//  private void writeField(JsonWriter json, Object entity, Field field) throws IllegalAccessException {
//    EdmProperty property = field.getAnnotation(EdmProperty.class);
//    if(property != null) {
//      field.setAccessible(true);
//      Object fieldValue = field.get(entity);
//      switch (property.type()) {
//        case SIMPLE:
//          json.writeProperty(property.name(), fieldValue);
//          break;
//        case NAVIGATION:
//          Object obj = extractEdmTypeId(fieldValue);
//          json.writeProperty("uri", uriInfo.getBaseUri() + "/" + obj.toString());
//          break;
//        default:
//          break;
//      }
//    }
//  }

  private Object extractEdmTypeId(Object value) throws IllegalArgumentException, IllegalAccessException {
    Field idField = getFieldWithAnnotation(value.getClass(), EdmTypeId.class);
    if(idField == null) {
      return "NULL";
    }
    idField.setAccessible(true);
    return idField.get(value);
  }

  private Field getFieldWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClazz) {
    Field fieldWithAnnotation = null;
    
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      if(field.getAnnotation(annotationClazz) != null) {
        fieldWithAnnotation = field;
        break;
      }
    }
    
    Class<?> superclass = clazz.getSuperclass();
    if(fieldWithAnnotation == null && superclass != Object.class) {
      return getFieldWithAnnotation(superclass, annotationClazz);
    }
    
    return fieldWithAnnotation;
  }

  private List<Field> getAllFields(Class<?> clazz) {
    Field[] fields = clazz.getDeclaredFields();
    List<Field> allFields = new ArrayList<Field>(Arrays.asList(fields));

    final Class<?> superclass = clazz.getSuperclass();
    if(superclass != Object.class) {
      allFields.addAll(getAllFields(superclass));
    }
    
    return allFields;
  }
}
