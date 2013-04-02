package com.sap.core.odata.processor.core.jpa.cud;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

public class JPAUpdateRequest extends JPAWriteRequest{
	
	public JPAUpdateRequest() {
		super();
	}

	public void process(Object jpaEntity, PutMergePatchUriInfo putUriInfo, InputStream content, String requestContentType) throws ODataJPARuntimeException  {
		
		final EdmEntitySet entitySet = putUriInfo.getTargetEntitySet();
	    EdmEntityType entityType = null;
		try {
			entityType = entitySet.getEntityType();
		} catch (EdmException e3) {
			throw ODataJPARuntimeException
			.throwException(ODataJPARuntimeException.GENERAL
					.addContent(e3.getMessage()), e3);
		}	    
				
		ODataEntry entryValues = null;
		try {
			entryValues = parseEntry(entitySet, content, requestContentType, false, new HashMap<String, Object>());
		} catch (ODataBadRequestException e1) {
			throw ODataJPARuntimeException
			.throwException(ODataJPARuntimeException.GENERAL
					.addContent(e1.getMessage()), e1);
		} 		
		try {
			Map<String, Object> propertValueMap = entryValues.getProperties();
			parse2JPAEntityValueMap(jpaEntity, entityType, propertValueMap);
		} catch (ODataJPARuntimeException e) {
			throw ODataJPARuntimeException
			.throwException(ODataJPARuntimeException.GENERAL
					.addContent(e.getMessage()), e);
		}		
	}
	
	
	
	@SuppressWarnings("unchecked")
	public final Object parse2JPAEntityValueMap(
			Object jpaEntity, EdmStructuralType edmEntityType, Map<String, Object> propertyValueMap)
			throws ODataJPARuntimeException {

		if (jpaEntity == null || edmEntityType == null || propertyValueMap == null)
			return null;

		String jpaEntityAccessKey = jpaEntity.getClass().getName();

		if (!jpaEntityAccessMap.containsKey(jpaEntityAccessKey))
			jpaEntityAccessMap.put(jpaEntityAccessKey,
					getSetters(jpaEntity, edmEntityType, false));

		HashMap<String, Method> setters = jpaEntityAccessMap
				.get(jpaEntityAccessKey);
		List<EdmProperty> keyProperties = null;
		if(edmEntityType instanceof EdmEntityType){
			try {
				keyProperties = ((EdmEntityType)edmEntityType).getKeyProperties();
			} catch (EdmException e) {
				throw ODataJPARuntimeException
				.throwException(ODataJPARuntimeException.GENERAL
						.addContent(e.getMessage()), e);
			}
		}
		boolean isKeyProperty = false;
		String propertyName = null;
		try {
			for (String key : setters.keySet()) {
				isKeyProperty = false;
				if(keyProperties != null){
					for(EdmProperty keyProperty : keyProperties){
						if(keyProperty.getName().equalsIgnoreCase(key)){
							isKeyProperty = true;
							break;
						}
					}
					if(isKeyProperty){
						continue;
					}
				}
				EdmProperty property = (EdmProperty) edmEntityType
						.getProperty(key);				
				if(property.getMapping() != null && property.getMapping().getInternalName() !=  null){
					propertyName = property.getMapping().getInternalName();
				}else { 
					propertyName = property.getName();
				}
				Method method = setters.get(key);
				Object propertyValue = propertyValueMap.get(key);
				if(propertyValue == null) continue;
				if(propertyValue != null){
					if(propertyValue instanceof java.util.GregorianCalendar){
						propertyValue = ((java.util.GregorianCalendar)propertyValue).getTime();
					}
	
					if (method != null) {
						if (property.getType().getKind().equals(EdmTypeKind.COMPLEX)) {
							Object complexObject = jpaComplexObjectMap.get(propertyName);
							parse2JPAEntityValueMap(complexObject, ((EdmComplexType)property.getType()), 
									(Map<String, Object>)propertyValue);
							setters.get(key).invoke(jpaEntity,complexObject);
						} else 
							setters.get(key).invoke(jpaEntity,propertyValue);
					}
				}

			}			
		} catch (SecurityException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		} catch (EdmException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		} catch (IllegalAccessException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		} catch (IllegalArgumentException e) {
			throw ODataJPARuntimeException
			.throwException(ODataJPARuntimeException.ERROR_JPQL_PARAM_VALUE
					.addContent(propertyName), e);
		} catch (InvocationTargetException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		}
		return jpaEntity;
	}			
	
}
