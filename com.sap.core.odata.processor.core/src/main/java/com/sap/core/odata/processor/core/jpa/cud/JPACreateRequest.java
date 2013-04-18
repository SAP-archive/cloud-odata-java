package com.sap.core.odata.processor.core.jpa.cud;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
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

	@SuppressWarnings("unchecked")
	public <T> List<T> process(PostUriInfo postUriInfo, InputStream content, String requestContentType) throws ODataJPARuntimeException  {
		ExpandSelectTreeNode expandSelectTree = null;
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
	    	throw ODataJPARuntimeException
			.throwException(ODataJPARuntimeException.GENERAL
					.addContent(e1.getMessage()), e1);
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
			entryValues = parseEntry(entitySet, content, requestContentType, true);
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
		try {
			createInlinedEntities(jpaEntity, entitySet, entryValues, currentEntityName);
		} catch (ODataException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		expandSelectTree = entryValues.getExpandSelectTree();
//		return jpaEntity;
//		return expandSelectTree;
		List<T> objectList = new ArrayList();
		objectList.add((T) jpaEntity);
		objectList.add((T) expandSelectTree);
		return objectList;
		
	}
	
	@SuppressWarnings("unchecked")
	public final Object parse2JPAEntityValueMap(
			Object jpaEntity, EdmStructuralType edmEntityType, Map<String, Object> propertyValueMap,String entityName)
			throws ODataJPARuntimeException {

		if (jpaEntity == null || edmEntityType == null || propertyValueMap == null || propertyValueMap.size() == 0)
			return null;

		String jpaEntityAccessKey = jpaEntity.getClass().getName();

		if (!jpaEntityAccessMap.containsKey(jpaEntityAccessKey))
			jpaEntityAccessMap.put(jpaEntityAccessKey,
					getSetters(jpaEntity, edmEntityType, true));

		HashMap<String, Method> setters = jpaEntityAccessMap
				.get(jpaEntityAccessKey);
		HashMap<String, String> embeddableKeys = jpaEmbeddableKeyMap
				.get(jpaEntityAccessKey);
		String propertyName = null;
		try {
			for (String key : setters.keySet()) {

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
				for (String embeddableKey : embeddableKeys.keySet()) {
					String name = embeddableKeys.get(embeddableKey);
					String[] nameParts = name.split("\\.");//$NON-NLS-1$
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
							populateEmbeddableKey(embeddableKeyObj, embeddableKey, nameParts[1], propertyValueMap);
						} catch (NoSuchMethodException e) {
							throw ODataJPARuntimeException
							.throwException(ODataJPARuntimeException.GENERAL
									.addContent(e.getMessage()), e);
						}
				}
				propertyName = "Embeddable Key";//$NON-NLS-1$
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
					.throwException(ODataJPARuntimeException.ERROR_JPQL_PARAM_VALUE
							.addContent(propertyName), e);
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
				.throwException(ODataJPARuntimeException.ERROR_JPQL_KEY_VALUE
						.addContent(key), e);
			} catch (InvocationTargetException e) {
				throw ODataJPARuntimeException
				.throwException(ODataJPARuntimeException.GENERAL
						.addContent(e.getMessage()), e);
			}

	}
	
	private <T> void createInlinedEntities(final T jpaEntity, final EdmEntitySet entitySet, final ODataEntry entryValues, String jpaEntityName) throws ODataException {
		Map<String, Object> relatedPropertyValueMap = new HashMap<String, Object>();
		Map<String, Class<?>> relatedClassMap = new HashMap<String, Class<?>>();
	    final EdmEntityType entityType = entitySet.getEntityType();
	    for (final String navigationPropertyName : entityType.getNavigationPropertyNames()) {
	    	final EdmNavigationProperty navigationProperty = (EdmNavigationProperty) entityType.getProperty(navigationPropertyName);
	    	List<ODataEntry> relatedValueList = new ArrayList<ODataEntry>();
	    	if(navigationProperty.getMultiplicity() == EdmMultiplicity.ONE){
	    		ODataEntry oDataEntry = (ODataEntry)entryValues.getProperties().get(navigationPropertyName);
	    		relatedValueList.add(oDataEntry);
	    	}else{	      
	      relatedValueList = (List<ODataEntry>) entryValues.getProperties().get(navigationPropertyName);
	    }
	      if (relatedValueList != null) {
//	        final EdmNavigationProperty navigationProperty = (EdmNavigationProperty) entityType.getProperty(navigationPropertyName);
	        final EdmEntitySet relatedEntitySet = entitySet.getRelatedEntitySet(navigationProperty);
	        for (final ODataEntry relatedValues : relatedValueList) {
	        	
	        	String entityName = null;
	        	EdmEntityType relatedEntityType = relatedEntitySet.getEntityType();
	    	    try{
	    	    if(relatedEntityType.getMapping() != null && relatedEntityType.getMapping().getInternalName() != null)
	    	    {
	    	    	entityName = relatedEntityType.getMapping().getInternalName();
	    	    }else{
	    	    	entityName = relatedEntityType.getName();
	    	    }
	    	    }catch(EdmException e1){
	    	    	throw ODataJPARuntimeException
	    			.throwException(ODataJPARuntimeException.GENERAL
	    					.addContent(e1.getMessage()), e1);
	    	    }
	        	
	        	
		        Object relatedData = null;
		  		Set<EntityType<?>> entityTypeSet = this.metamodel.getEntities();
		  		String currentEntityName = null;
		  		for(EntityType<?> entityTypeTemp : entityTypeSet){
		  			if(entityTypeTemp.getJavaType().getName().endsWith("."+entityName)){
		  				currentEntityName = entityTypeTemp.getName();
		  				try {
		  					relatedClassMap.put(navigationProperty.getMapping().getInternalName(), entityTypeTemp.getJavaType());
		  					relatedData = entityTypeTemp.getJavaType().newInstance();
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
//	          setStructuralTypeValuesFromMap(relatedData, relatedEntitySet.getEntityType(), relatedValues.getProperties(), true);
		  		if(relatedValues != null && relatedEntitySet != null)
		  			parse2JPAEntityValueMap(relatedData, relatedEntitySet.getEntityType(), relatedValues.getProperties(),currentEntityName);
		  		else continue;
	          relatedPropertyValueMap.put(navigationProperty.getMapping().getInternalName(), relatedData);
	          
	          //	          dataSource.createData(relatedEntitySet, relatedData);
//	          dataSource.writeRelation(entitySet, data, relatedEntitySet, getStructuralTypeValueMap(relatedData, relatedEntitySet.getEntityType()));
	          createInlinedEntities(relatedData, relatedEntitySet, relatedValues,currentEntityName);
	        }
	      }
	    }
	    setNavigationProperties(jpaEntity, entitySet, relatedPropertyValueMap, jpaEntityName, relatedClassMap);
	  }
	
	
	private void setNavigationProperties(
			Object jpaEntity, EdmEntitySet entitySet, Map<String, Object> propertyValueMap,String entityName, Map<String, Class<?>> relatedClassMap){
		if (jpaEntity == null || entitySet == null || propertyValueMap == null || propertyValueMap.size() == 0)
			return ;
		List<HashMap<?, ?>> mapList = getSettersForNavigationProperties(jpaEntity, entitySet, relatedClassMap);
		HashMap<String, Method> setters = (HashMap<String, Method>) mapList.get(0);
		HashMap<String, EdmMultiplicity> multiplicityMap = (HashMap<String, EdmMultiplicity>) mapList.get(1);
		for (String key : setters.keySet()) {
			/*EdmNavigationProperty property = (EdmNavigationProperty) edmEntityType.getProperty(key);				
			if(property.getMapping() != null && property.getMapping().getInternalName() !=  null){
				propertyName = property.getMapping().getInternalName();
			}else { 
				propertyName = property.getName();
			}*/
			
			Method method = setters.get(key);
			Object propertyValue = propertyValueMap.get(key);
			if(propertyValue == null) continue;
			try {
				if(multiplicityMap.get(key) == EdmMultiplicity.MANY){
					List propertyList = new ArrayList();
					propertyList.add(propertyValue);
					propertyValue = propertyList;
				}else{
					
				}
				method.invoke(jpaEntity,propertyValue);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private List<HashMap<?, ?>> getSettersForNavigationProperties(Object jpaEntity, EdmEntitySet edmEntitySet,Map<String, Class<?>> relatedClassMap) {
		List<HashMap<?,?>> mapList = new ArrayList<HashMap<?,?>>(); 
		HashMap<String, Method> setters = new HashMap<String, Method>();
		HashMap<String, EdmMultiplicity> multiplicityMap = new HashMap<String, EdmMultiplicity>();
		EdmEntityType edmEntityType = null;
		try {
			edmEntityType = edmEntitySet.getEntityType();
		} catch (EdmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	
		try {
			for (final String navigationPropertyName : edmEntityType.getNavigationPropertyNames()) {
				final EdmNavigationProperty navigationProperty = (EdmNavigationProperty) edmEntityType.getProperty(navigationPropertyName);
		        final EdmEntitySet relatedEntitySet = edmEntitySet.getRelatedEntitySet(navigationProperty);
//		        for (final ODataEntry relatedValues : relatedValueList) {
		        	
		        	String entityName = null;
		        	EdmEntityType relatedEntityType = relatedEntitySet.getEntityType();
		    	    try{
		    	    if(navigationProperty.getMapping() != null && navigationProperty.getMapping().getInternalName() != null)
		    	    {
		    	    	entityName = navigationProperty.getMapping().getInternalName();
		    	    }else{
		    	    	entityName = navigationProperty.getName();
		    	    }
		    	    }catch(EdmException e1){
		    	    	try {
							throw ODataJPARuntimeException
							.throwException(ODataJPARuntimeException.GENERAL
									.addContent(e1.getMessage()), e1);
						} catch (ODataJPARuntimeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    	    }
		    	    String name = null;
					try {
						name = getSetterName(entityName);
					} catch (ODataJPARuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	    Class<?> propertyClass = null;
		    	    if(navigationProperty.getMultiplicity() == EdmMultiplicity.MANY){
		    	    	propertyClass = List.class;
		    	    	multiplicityMap.put(entityName, EdmMultiplicity.MANY);
		    	    }else{
		    	    	propertyClass = relatedClassMap.get(entityName);
		    	    	multiplicityMap.put(entityName, EdmMultiplicity.ONE);
		    	    }
		    	    try {
						setters.put(
								entityName,
								jpaEntity.getClass().getMethod(name,propertyClass));
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		} catch (EdmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mapList.add(0,setters);
		mapList.add(1,multiplicityMap);
		return mapList;
	}
	
	private String getSetterName(String navigationPropertyName)
			throws ODataJPARuntimeException {
		EdmMapping mapping = null;
		String name = navigationPropertyName;		

		String[] nameParts = name.split("\\.");		//$NON-NLS-1$
		StringBuilder builder = new StringBuilder();

		if (nameParts.length == 1) {
			if (name != null) {
				char c = Character.toUpperCase(name.charAt(0));

				builder.append("set").append(c).append(name.substring(1))	//$NON-NLS-1$
						.toString();
			}
		} else if (nameParts.length > 1) {

			for (int i = 0; i < nameParts.length; i++) {
				name = nameParts[i];
				char c = Character.toUpperCase(name.charAt(0));
				if (i == 0)
					builder.append("set").append(c).append(name.substring(1)); 	//$NON-NLS-1$
				else
					builder.append(".").append("set").append(c)	//$NON-NLS-1$ //$NON-NLS-2$
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

	
	
}
