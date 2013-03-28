package com.sap.core.odata.processor.core.jpa.cud;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.access.model.EdmTypeConvertor;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmMappingImpl;

public class JPAWriteRequest {
	protected HashMap<String, HashMap<String, Method>> jpaEntityAccessMap = null;
	protected HashMap<String, Object> jpaComplexObjectMap = null;
	protected HashMap<String, HashMap<String, String>> jpaEmbeddableKeyMap = null;
	protected HashMap<String, Class<?>> jpaEmbeddableKeyObjectMap = null;
	
	public JPAWriteRequest(){
		jpaEntityAccessMap = new HashMap<String, HashMap<String, Method>>();
		jpaComplexObjectMap = new HashMap<String, Object>();
	}
	
	protected HashMap<String, Method> getSetters(Object jpaEntity,
			EdmStructuralType structuralType, boolean isCreate) throws ODataJPARuntimeException {

		HashMap<String, Method> setters = new HashMap<String, Method>();
		HashMap<String, String> embeddableKey = new HashMap<String, String>();
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
				if (isCreate && nameParts.length > 1) {
					jpaEmbeddableKeyObjectMap.put(propertyName, propertyClass);
					embeddableKey.put(propertyName, name);
				} else
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

		if (isCreate && !embeddableKey.isEmpty()) {
			jpaEmbeddableKeyMap.put(jpaEntity.getClass().getName(),
					embeddableKey);
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
	
	protected ODataEntry parseEntry(final EdmEntitySet entitySet, final InputStream content, final String requestContentType, final boolean merge, final Map<String, Object> typeMap) throws ODataBadRequestException {
	    ODataEntry entryValues;
	    try {
	      entryValues = EntityProvider.readEntry(requestContentType, entitySet, content, merge, typeMap);
	    } catch (EntityProviderException e) {
	      throw new ODataBadRequestException(ODataBadRequestException.BODY, e);
	    }
	    return entryValues;
	  }		
}
