package com.sap.core.odata.processor.core.jpa.cud;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
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
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataHttpException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.access.model.EdmTypeConvertor;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmMappingImpl;

public class JPACreateContext {
	
	private HashMap<String, HashMap<String, Method>> jpaEntityAccessMap = null;
	private HashMap<String, HashMap<String, String>> jpaEmbeddableKeyMap = null;
	private HashMap<String, Object> jpaComplexObjectMap = null;
	private HashMap<String, Class<?>> jpaEmbeddableKeyObjectMap = null;
	private Metamodel metamodel;

	public JPACreateContext() {
		jpaEntityAccessMap = new HashMap<String, HashMap<String, Method>>();
		jpaEmbeddableKeyMap = new HashMap<String, HashMap<String, String>>();
		jpaComplexObjectMap = new HashMap<String, Object>();
		jpaEmbeddableKeyObjectMap = new HashMap<String, Class<?>>();		
	}

	public JPACreateContext(Metamodel metamodel) {
		this();
		this.metamodel = metamodel;
	}

	public <T> Object build(PostUriInfo postUriInfo, InputStream content, String requestContentType) throws ODataJPARuntimeException  {
		
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
			entryValues = parseEntry(entitySet, content, requestContentType, true, getStructuralTypeTypeMap(jpaEntity, entityType));
		} catch (ODataBadRequestException e1) {
			throw ODataJPARuntimeException
			.throwException(ODataJPARuntimeException.GENERAL
					.addContent(e1.getMessage()), e1);
		} catch (ODataException e1) {
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
	
	private HashMap<String, Method> getSetters(Object jpaEntity,
			EdmStructuralType structuralType,String entityName) throws ODataJPARuntimeException {

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
				if (nameParts.length > 1) {
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

		if (!embeddableKey.isEmpty()) {
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
	
	@SuppressWarnings("unchecked")
	public final Object parse2JPAEntityValueMap(
			Object jpaEntity, EdmStructuralType edmEntityType, Map<String, Object> propertyValueMap,String entityName)
			throws ODataJPARuntimeException {

		if (jpaEntity == null || edmEntityType == null)
			return null;

		String jpaEntityAccessKey = jpaEntity.getClass().getName();

		if (!jpaEntityAccessMap.containsKey(jpaEntityAccessKey))
			jpaEntityAccessMap.put(jpaEntityAccessKey,
					getSetters(jpaEntity, edmEntityType,entityName));

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
	
	private ODataEntry parseEntry(final EdmEntitySet entitySet, final InputStream content, final String requestContentType, final boolean merge, final Map<String, Object> typeMap) throws ODataBadRequestException {
	    ODataEntry entryValues;
	    try {
	      entryValues = EntityProvider.readEntry(requestContentType, entitySet, content, merge, typeMap);
	    } catch (EntityProviderException e) {
	      throw new ODataBadRequestException(ODataBadRequestException.BODY, e);
	    }
	    return entryValues;
	  }
	
	private <T> Map<String, Object> getStructuralTypeValueMap(final T data, final EdmStructuralType type) throws ODataException {

	    Map<String, Object> valueMap = new HashMap<String, Object>();

	    for (final String propertyName : type.getPropertyNames()) {
	      final EdmProperty property = (EdmProperty) type.getProperty(propertyName);
	      final Object value = getPropertyValue(data, property);

	      if (property.isSimple()) {
	        if (property.getMapping() == null || property.getMapping().getMimeType() == null) {
	          valueMap.put(propertyName, value);
	        } else {
	          valueMap.put(propertyName, getSimpleTypeValueMap(data, Arrays.asList(property)));
	        }
	      } else {
	        valueMap.put(propertyName, getStructuralTypeValueMap(value, (EdmStructuralType) property.getType()));
	      }
	    }

	    return valueMap;
	  }

	  private <T> Map<String, Object> getStructuralTypeTypeMap(final T data, final EdmStructuralType type) throws ODataException {

	    Map<String, Object> valueMap = new HashMap<String, Object>();
	    for (final String propertyName : type.getPropertyNames()) {
	      final EdmProperty property = (EdmProperty) type.getProperty(propertyName);
	      if (property.isSimple())
	        valueMap.put(propertyName, getPropertyType(data, property));
	      else
	        valueMap.put(propertyName, getStructuralTypeValueMap(getPropertyValue(data, property), (EdmStructuralType) property.getType()));
	    }

	    return valueMap;
	  }
	  
	  private static <T> Class<?> getPropertyType(final T data, final EdmProperty property) throws ODataException {
		    return getType(data, getGetterMethodName(property));
		  }
	  
	  private static <T> Object getPropertyValue(final T data, final EdmProperty property) throws ODataException {
		    return getValue(data, getGetterMethodName(property));
		  }
	  
	  private static <T> Map<String, Object> getSimpleTypeValueMap(final T data, final List<EdmProperty> propertyPath) throws ODataException {
		    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);
		    Map<String, Object> valueWithMimeType = new HashMap<String, Object>();
		    valueWithMimeType.put(property.getName(), getPropertyValue(data, propertyPath));
		    final String mimeTypeMappingName = property.getMapping().getMimeType();
		    valueWithMimeType.put(mimeTypeMappingName, getValue(data, mimeTypeMappingName));
		    return valueWithMimeType;
		  }
	  
	  private static <T> Object getPropertyValue(final T data, final List<EdmProperty> propertyPath) throws ODataException {
		    Object dataObject = data;
		    for (final EdmProperty property : propertyPath)
		      if (dataObject != null)
		        dataObject = getPropertyValue(dataObject, property);
		    return dataObject;
		  }
	  
	  private static <T> Object getValue(final T data, final String methodName) throws ODataNotFoundException {
		    Object dataObject = data;

		    for (final String method : methodName.split("\\.", -1))
		      if (dataObject != null)
		        try {
		          dataObject = dataObject.getClass().getMethod(method).invoke(dataObject);
		        } catch (SecurityException e) {
		          throw new ODataNotFoundException(ODataHttpException.COMMON, e);
		        } catch (NoSuchMethodException e) {
		          throw new ODataNotFoundException(ODataHttpException.COMMON, e);
		        } catch (IllegalArgumentException e) {
		          throw new ODataNotFoundException(ODataHttpException.COMMON, e);
		        } catch (IllegalAccessException e) {
		          throw new ODataNotFoundException(ODataHttpException.COMMON, e);
		        } catch (InvocationTargetException e) {
		          throw new ODataNotFoundException(ODataHttpException.COMMON, e);
		        }

		    return dataObject;
		  }
	  
	  
	  private static String getGetterMethodName(EdmProperty property)
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

					builder.append("get").append(c).append(name.substring(1))
							.toString();
				}
			} else if (nameParts.length > 1) {

				for (int i = 0; i < nameParts.length; i++) {
					name = nameParts[i];
					char c = Character.toUpperCase(name.charAt(0));
					if (i == 0)
						builder.append("get").append(c).append(name.substring(1));
					else
						builder.append(".").append("get").append(c)
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

	  
	  private static <T> Class<?> getType(final T data, final String methodName) throws ODataNotFoundException {
		    if (data == null)
		      throw new ODataNotFoundException(ODataHttpException.COMMON);

		    Class<?> type = data.getClass();
		    for (final String method : methodName.split("\\.", -1))
		      try {
		        type = type.getMethod(method).getReturnType();
		      } catch (final SecurityException e) {
		        throw new ODataNotFoundException(ODataHttpException.COMMON, e);
		      } catch (final NoSuchMethodException e) {
		        throw new ODataNotFoundException(ODataHttpException.COMMON, e);
		      }
		    return type;
		  }

}
