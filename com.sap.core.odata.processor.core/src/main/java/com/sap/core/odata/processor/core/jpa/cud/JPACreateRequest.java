package com.sap.core.odata.processor.core.jpa.cud;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

public class JPACreateRequest extends JPAWriteRequest{
	
	private Metamodel metamodel;

	public JPACreateRequest() {
		super();
		jpaEmbeddableKeyMap = new HashMap<String, HashMap<String, String>>();
		jpaEmbeddableKeyObjectMap = new HashMap<String, Class<?>>();		
	}

	public JPACreateRequest(Metamodel metamodel) {
		this();
		this.metamodel = metamodel;
	}

	public <T> Object process(PostUriInfo postUriInfo, InputStream content, String requestContentType) throws ODataJPARuntimeException  {
		
		final EdmEntitySet entitySet = postUriInfo.getTargetEntitySet();
	    EdmEntityType entityType = null;
		try {
			entityType = entitySet.getEntityType();
		} catch (EdmException e3) {
			throw ODataJPARuntimeException
			.throwException(ODataJPARuntimeException.GENERAL
					.addContent(e3.getMessage()), e3);
		}
	    String entityName = null;
	    try{
	    if(entityType.getMapping() != null && entityType.getMapping().getInternalName() != null)
	    {
	    	entityName = entityType.getMapping().getInternalName();
	    }else{
	    	entityName = entityType.getName();
	    }
	    }catch(EdmException e1){
	    	
	    }
		Object jpaEntity = null;
		Set<EntityType<?>> entityTypeSet = this.metamodel.getEntities();
		String currentEntityName = null;
		for(EntityType<?> entityTypeTemp : entityTypeSet){
			if(entityTypeTemp.getJavaType().getName().endsWith("."+entityName)){
				currentEntityName = entityTypeTemp.getName();
				try {
					jpaEntity = entityTypeTemp.getJavaType().newInstance();
					break;
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
		}
		
		ODataEntry entryValues = null;
		try {
			entryValues = parseEntry(entitySet, content, requestContentType, true,new HashMap<String, Object>());
		} catch (ODataBadRequestException e1) {
			throw ODataJPARuntimeException
			.throwException(ODataJPARuntimeException.GENERAL
					.addContent(e1.getMessage()), e1);
		}		
		try {
			Map<String, Object> propertValueMap = entryValues.getProperties();
			parse2JPAEntityValueMap(jpaEntity, entityType, propertValueMap,currentEntityName);
		} catch (ODataJPARuntimeException e) {
			throw ODataJPARuntimeException
			.throwException(ODataJPARuntimeException.GENERAL
					.addContent(e.getMessage()), e);
		}
		return jpaEntity;
	}
	
	@SuppressWarnings("unchecked")
	public final Object parse2JPAEntityValueMap(
			Object jpaEntity, EdmStructuralType edmEntityType, Map<String, Object> propertyValueMap,String entityName)
			throws ODataJPARuntimeException {

		if (jpaEntity == null || edmEntityType == null)
			return null;

		String jpaEntityAccessKey = jpaEntity.getClass().getName();

		if (!jpaEntityAccessMap.containsKey(jpaEntityAccessKey))
			jpaEntityAccessMap.put(jpaEntityAccessKey,
					getSetters(jpaEntity, edmEntityType, true));

		HashMap<String, Method> setters = jpaEntityAccessMap
				.get(jpaEntityAccessKey);
		HashMap<String, String> embeddableKeys = jpaEmbeddableKeyMap
				.get(jpaEntityAccessKey);
		

		try {
			for (String key : setters.keySet()) {

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
				if(propertyValue instanceof java.util.GregorianCalendar){
					propertyValue = ((java.util.GregorianCalendar)propertyValue).getTime();
				}

				if (method != null) {
					if (property.getType().getKind().equals(EdmTypeKind.COMPLEX)) {
						Object complexObject = jpaComplexObjectMap.get(propertyName);
						parse2JPAEntityValueMap(complexObject, ((EdmComplexType)property.getType()), 
								(Map<String, Object>)propertyValue, propertyName);
						setters.get(key).invoke(jpaEntity,complexObject);
					} else 
						setters.get(key).invoke(jpaEntity,propertyValue);
				}

			}
			
			if (embeddableKeys != null) {
				Object embeddableKeyObj = null;
				Method method = null;
				for (String key : embeddableKeys.keySet()) {
					String name = embeddableKeys.get(key);
					String[] nameParts = name.split("\\.");
					Object propertyValue = jpaEntity;
					Class<?> propertyClass = null;
					
						try {
							for(EntityType<?> entity:metamodel.getEntities())
							{
								if(entity.getName().equals(entityName))
								{
									Attribute<?,?> attribute = entity.getAttribute(nameParts[0].substring(3, 4).toLowerCase()+nameParts[0].substring(4));
									propertyClass = attribute.getJavaType();
									if(embeddableKeyObj==null){
										try {
											embeddableKeyObj = propertyClass.newInstance();
										} catch (InstantiationException e) {
											throw ODataJPARuntimeException
											.throwException(ODataJPARuntimeException.GENERAL
													.addContent(e.getMessage()), e);
										}
									}
									break;
								}
							}

							method = propertyValue.getClass().getMethod(
									nameParts[0], propertyClass);
							populateEmbeddableKey(embeddableKeyObj, key, nameParts[1], propertyValueMap);
						} catch (NoSuchMethodException e) {
							throw ODataJPARuntimeException
							.throwException(ODataJPARuntimeException.GENERAL
									.addContent(e.getMessage()), e);
						}
				}
				method.invoke(jpaEntity,embeddableKeyObj);
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
	
	private void populateEmbeddableKey(Object embeddableKeyObject, String key, String setterName,Map<String, Object> propertyValueMap) throws ODataJPARuntimeException{
			Class<?> propertyClass = jpaEmbeddableKeyObjectMap.get(key);
			Method method = null;
			try {
				method = embeddableKeyObject.getClass().getMethod(setterName,propertyClass);
			} catch (NoSuchMethodException e1) {
				throw ODataJPARuntimeException
				.throwException(ODataJPARuntimeException.GENERAL
						.addContent(e1.getMessage()), e1);
			} catch (SecurityException e1) {
				throw ODataJPARuntimeException
				.throwException(ODataJPARuntimeException.GENERAL
						.addContent(e1.getMessage()), e1);
			}
			try {
				method.invoke(embeddableKeyObject,propertyValueMap.get(key));
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

	}
	
}
