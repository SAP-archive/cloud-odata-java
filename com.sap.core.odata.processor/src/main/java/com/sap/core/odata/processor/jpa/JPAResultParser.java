package com.sap.core.odata.processor.jpa;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.processor.jpa.exception.ODataJPARuntimeException;

public final class JPAResultParser {

	/*
	 * List of buffers used by the Parser
	 */
	private HashMap<String, Method> getters = null;
	private String jpaEntityType = null;

	private static JPAResultParser resultParser;

	private JPAResultParser() {
	};

	public static final JPAResultParser create() {
		if (resultParser == null)
			resultParser = new JPAResultParser();
		return resultParser;
	}

	/*
	 * The method returns a Hash Map of Properties and values for an EdmEntity
	 * The method uses reflection on object jpaEntity to get the list of getters
	 * method. Then uses the getters method to extract the value from JPAEntity.
	 */
	public final HashMap<String, Object> parse2EdmEntity(Object jpaEntity,
			EdmEntityType entityType) throws ODataJPARuntimeException {

		if (jpaEntity == null || entityType == null)
			return null;

		if (!jpaEntity.getClass().toString().equals(jpaEntityType)) {
			jpaEntityType = jpaEntity.getClass().toString();
			getters = new HashMap<String, Method>();

			try {
				for (String propertyName : entityType.getPropertyNames()) {

					EdmProperty property = (EdmProperty) entityType
							.getProperty(propertyName);
					getters.put(
							propertyName,
							jpaEntity.getClass().getMethod(
									getGetterName(property), (Class<?>[]) null));
				}
			} catch (NoSuchMethodException e) {
				throw new ODataJPARuntimeException(ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e.getMessage()),e);
			} catch (SecurityException e) {
				throw new ODataJPARuntimeException(ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e.getMessage()),e);
			} catch (EdmException e) {
				throw new ODataJPARuntimeException(ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e.getMessage()),e);
			}
		}

		HashMap<String, Object> edmEntity = new HashMap<String, Object>();
		for (String key : getters.keySet()) {
			try {
				Object propertyValue = getters.get(key).invoke(jpaEntity);
				edmEntity.put(key, propertyValue);
			} catch (IllegalAccessException e) {
				throw new ODataJPARuntimeException(ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e.getMessage()),e);
			} catch (IllegalArgumentException e) {
				throw new ODataJPARuntimeException(ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e.getMessage()),e);
			} catch (InvocationTargetException e) {
				throw new ODataJPARuntimeException(ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e.getMessage()),e);
			}
		}
		return edmEntity;
	}

	private String getGetterName(EdmProperty property) throws ODataJPARuntimeException {
		Mapping mapping = null;
		String name = null;
		try {
			mapping = (Mapping) property.getMapping();

			if (mapping == null || mapping.getInternalName() == null)
				name = property.getName();
			else
				name = mapping.getInternalName();
		} catch (EdmException e) {
			throw new ODataJPARuntimeException(ODataJPARuntimeException.RUNTIME_EXCEPTION.addContent(e.getMessage()),e);
		}

		if (name != null) {
			char c = Character.toUpperCase(name.charAt(0));
			
			return new StringBuilder().append("get").append(c).append(name.substring(1)).toString();
		}
		return null;
	}
}
