package com.sap.core.odata.processor.core.jpa.cud;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.metamodel.Metamodel;

import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.access.model.EdmTypeConvertor;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmMappingImpl;

public class JPAUpdateContext {
	
	private HashMap<String, HashMap<String, Method>> jpaEntityAccessMap = null;
	private HashMap<String, Object> jpaComplexObjectMap = null;
	
	public JPAUpdateContext() {
		jpaEntityAccessMap = new HashMap<String, HashMap<String, Method>>();
		jpaComplexObjectMap = new HashMap<String, Object>();
	}

	public JPAUpdateContext(Metamodel metamodel) {
		this();		
	}

	public <T> Object build(Object jpaEntity, PutMergePatchUriInfo putUriInfo, InputStream content, String requestContentType) throws ODataJPARuntimeException  {
		
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
		return jpaEntity;
	}
	
	private HashMap<String, Method> getSetters(Object jpaEntity,
			EdmStructuralType structuralType) throws ODataJPARuntimeException {

		HashMap<String, Method> setters = new HashMap<String, Method>();
		try {
			for (String propertyName : structuralType.getPropertyNames()) {

				EdmProperty property = (EdmProperty) structuralType
						.getProperty(propertyName);
				Class<?> propertyClass = null;
				try {					
					if(property.getMapping() != null && ((JPAEdmMappingImpl)property.getMapping()).getJPAType() != null){
						propertyClass = ((JPAEdmMappingImpl)property.getMapping()).getJPAType();
						if (property.getType().getKind().equals(EdmTypeKind.COMPLEX)) {
							try {
								if(((JPAEdmMappingImpl)property.getMapping()).getInternalName() != null)
									jpaComplexObjectMap.put(((JPAEdmMappingImpl)property.getMapping()).getInternalName(), propertyClass.newInstance());
								else 								
									jpaComplexObjectMap.put(propertyName, propertyClass.newInstance());
							} catch (InstantiationException e) {
								throw ODataJPARuntimeException
								.throwException(ODataJPARuntimeException.GENERAL
										.addContent(e.getMessage()), e);
							} catch (IllegalAccessException e) {
								throw ODataJPARuntimeException
								.throwException(ODataJPARuntimeException.GENERAL
										.addContent(e.getMessage()), e);
							}
						}
					} else 
						propertyClass = EdmTypeConvertor.convertToJavaType(property.getType());
				} catch (ODataJPAModelException e) {
					throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
				}
				String name = getSetterName(property);
				String[] nameParts = name.split("\\.");
				if (nameParts.length == 1) 
					setters.put(
							propertyName,
							jpaEntity.getClass().getMethod(name,propertyClass));
			}
		} catch (NoSuchMethodException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		} catch (SecurityException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		} catch (EdmException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		}
		return setters;
	}
	
	private String getSetterName(EdmProperty property)
			throws ODataJPARuntimeException {
		EdmMapping mapping = null;
		String name = null;
		try {
			mapping = property.getMapping();
			if (mapping == null || mapping.getInternalName() == null)
				name = property.getName();
			else {
				name = mapping.getInternalName();
			}

		} catch (EdmException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		}

		String[] nameParts = name.split("\\.");
		StringBuilder builder = new StringBuilder();

		if (nameParts.length == 1) {
			if (name != null) {
				char c = Character.toUpperCase(name.charAt(0));

				builder.append("set").append(c).append(name.substring(1))
						.toString();
			}
		} else if (nameParts.length > 1) {

			for (int i = 0; i < nameParts.length; i++) {
				name = nameParts[i];
				char c = Character.toUpperCase(name.charAt(0));
				if (i == 0)
					builder.append("set").append(c).append(name.substring(1));
				else
					builder.append(".").append("set").append(c)
							.append(name.substring(1));
			}
		} else {
			return null;
		}

		if (builder.length() > 0)
			return builder.toString();
		else
			return null;

	}
	
	@SuppressWarnings("unchecked")
	public final Object parse2JPAEntityValueMap(
			Object jpaEntity, EdmStructuralType edmEntityType, Map<String, Object> propertyValueMap)
			throws ODataJPARuntimeException {

		if (jpaEntity == null || edmEntityType == null)
			return null;

		String jpaEntityAccessKey = jpaEntity.getClass().getName();

		if (!jpaEntityAccessMap.containsKey(jpaEntityAccessKey))
			jpaEntityAccessMap.put(jpaEntityAccessKey,
					getSetters(jpaEntity, edmEntityType));

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
				String propertyName = null;
				if(property.getMapping() != null && property.getMapping().getInternalName() !=  null){
					propertyName = property.getMapping().getInternalName();
				}else { 
					propertyName = property.getName();
				}
				Method method = setters.get(key);
				Object propertyValue = propertyValueMap.get(key);
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
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		} catch (InvocationTargetException e) {
			throw ODataJPARuntimeException
					.throwException(ODataJPARuntimeException.GENERAL
							.addContent(e.getMessage()), e);
		}
		return jpaEntity;
	}
			
	private ODataEntry parseEntry(final EdmEntitySet entitySet, final InputStream content, final String requestContentType, final boolean merge, final Map<String, Object> typeMap) throws ODataBadRequestException {
	    ODataEntry entryValues;
	    try {
	      entryValues = EntityProvider.readEntry(requestContentType, entitySet, content, merge, typeMap);
	    } catch (EntityProviderException e) {
	      throw new ODataBadRequestException(ODataBadRequestException.BODY, e);
	    }
	    return entryValues;
	  }		
}
