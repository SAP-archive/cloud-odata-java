package com.sap.core.odata.processor.jpa;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public final class JPAResultParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(JPAResultParser.class);
	
	/*
	 * List of buffers used by the Parser
	 */
	private static short MAX_SIZE = 10;

	private HashMap<String, HashMap<String, Method>> jpaEntityAccessMap = null;

	private static JPAResultParser resultParser;

	private JPAResultParser() {
		jpaEntityAccessMap = new HashMap<String, HashMap<String, Method>>(
				MAX_SIZE);
	};

	public static final JPAResultParser create() {
		if (resultParser == null)
			resultParser = new JPAResultParser();
		return resultParser;
	}

	
	/**
	 * The method returns a Hash Map of Properties and values for an EdmEntity Type
	 * The method uses reflection on object jpaEntity to get the list of getters
	 * method. Then uses the getters method to extract the value from JPAEntity.
	 * 
	 * @param jpaEntity
	 * @param structuralType
	 * @return a Hash Map of Properties and values for given EdmEntity Type
	 * @throws ODataJPARuntimeException
	 */
	public final HashMap<String, Object> parse2EdmPropertyValueMap(Object jpaEntity,
			EdmStructuralType structuralType) throws ODataJPARuntimeException {

		if (jpaEntity == null || structuralType == null)
			return null;
		
		String jpaEntityAccessKey = jpaEntity.getClass().getName();
		
		if(!jpaEntityAccessMap.containsKey(jpaEntityAccessKey))
			jpaEntityAccessMap.put(jpaEntityAccessKey,getGetters(jpaEntity,structuralType));
		

		HashMap<String, Object> edmEntity = new HashMap<String, Object>();
		HashMap<String, Method> getters = jpaEntityAccessMap.get(jpaEntityAccessKey);
		
		for (String key : getters.keySet()) {
			try {
				
				EdmProperty property = (EdmProperty) structuralType
						.getProperty(key);
				
				Object propertyValue = getters.get(key).invoke(jpaEntity);
				
				if (property.getType().getKind().equals(EdmTypeKind.COMPLEX)) {
					propertyValue = parse2EdmPropertyValueMap(propertyValue, (EdmStructuralType) property.getType());
				}
				
				edmEntity.put(key, propertyValue);
				
			} catch (EdmException e) {
				LOGGER.error(e.getMessage(), e);
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e
								.getMessage()), e);
			} catch (IllegalAccessException e) {
				LOGGER.error(e.getMessage(), e);
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e
								.getMessage()), e);
			} catch (IllegalArgumentException e) {
				LOGGER.error(e.getMessage(), e);
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e
								.getMessage()), e);
			} catch (InvocationTargetException e) {
				LOGGER.error(e.getMessage(), e);
				throw ODataJPARuntimeException.throwException(
						ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e
								.getMessage()), e);
			}
		}
		return edmEntity;
	}


	private String getGetterName(EdmProperty property)
			throws ODataJPARuntimeException {
		EdmMapping mapping = null;
		String name = null;
		try {
			mapping = property.getMapping();

			if (mapping == null || mapping.getInternalName() == null)
				name = property.getName();
			else
				name = mapping.getInternalName();
		} catch (EdmException e) {
			LOGGER.error(e.getMessage(), e);
			throw ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e
							.getMessage()), e);
		}

		if (name != null) {
			char c = Character.toUpperCase(name.charAt(0));

			return new StringBuilder().append("get").append(c)
					.append(name.substring(1)).toString();
		}
		return null;
	}

	private HashMap<String, Method> getGetters(Object jpaEntity,
			EdmStructuralType structuralType) throws ODataJPARuntimeException {

		HashMap<String, Method> getters = new HashMap<String, Method>();

		try {
			for (String propertyName : structuralType.getPropertyNames()) {

				EdmProperty property = (EdmProperty) structuralType
						.getProperty(propertyName);
				getters.put(
						propertyName,
						jpaEntity.getClass().getMethod(getGetterName(property),
								(Class<?>[]) null));
			}
		} catch (NoSuchMethodException e) {
			LOGGER.error(e.getMessage(), e);
			throw ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e
							.getMessage()), e);
		} catch (SecurityException e) {
			LOGGER.error(e.getMessage(), e);
			throw ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e
							.getMessage()), e);
		} catch (EdmException e) { 
			LOGGER.error(e.getMessage(), e);
			throw ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e
							.getMessage()), e);
		}

		return getters;
	}
}
