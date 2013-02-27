package com.sap.core.odata.processor.core.jpa.access.model;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.access.JPAEdmMappingModelAccess;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.model.mapping.JPAAttributeMapType.JPAAttribute;
import com.sap.core.odata.processor.api.jpa.model.mapping.JPAEdmMappingModel;
import com.sap.core.odata.processor.api.jpa.model.mapping.JPAEmbeddableTypeMapType;
import com.sap.core.odata.processor.api.jpa.model.mapping.JPAEntityTypeMapType;
import com.sap.core.odata.processor.api.jpa.model.mapping.JPAPersistenceUnitMapType;
import com.sap.core.odata.processor.api.jpa.model.mapping.JPARelationshipMapType.JPARelationship;

public class JPAEdmMappingModelService implements JPAEdmMappingModelAccess {

	boolean mappingModelExists = true;
	private JPAEdmMappingModel mappingModel;
	private String mappingModelName;

	public JPAEdmMappingModelService(ODataJPAContext ctx) {
		mappingModelName = ctx.getJPAEdmNameMappingModel();
		if (mappingModelName == null) {
			mappingModelExists = false;
		}
	}

	@Override
	public void loadMappingModel() {

		if (mappingModelExists) {
			JAXBContext context;
			try {
				context = JAXBContext.newInstance(JPAEdmMappingModel.class);

				Unmarshaller unmarshaller = context.createUnmarshaller();
				InputStream is = loadMappingModelInputStream();
				if (is == null) {
					mappingModelExists = false;
					return;
				}

				mappingModel = (JPAEdmMappingModel) unmarshaller
						.unmarshal(is);

				if (mappingModel != null)
					mappingModelExists = true;

			} catch (JAXBException e) {
				mappingModelExists = false;
				ODataJPAModelException.throwException(
						ODataJPAModelException.GENERAL, e);
			}
		}
	}

	@Override
	public boolean isMappingModelExists() {
		return mappingModelExists;
	}

	@Override
	public JPAEdmMappingModel getJPAEdmMappingModel() {
		return mappingModel;
	}

	@Override
	public String mapJPAPersistenceUnit(String persistenceUnitName) {

		JPAPersistenceUnitMapType persistenceUnit = mappingModel
				.getPersistenceUnit();
		if (persistenceUnit.getName().equals(persistenceUnitName))
			return persistenceUnit.getEDMSchemaNamespace();

		return null;
	}

	@Override
	public String mapJPAEntityType(String jpaEntityTypeName) {

		JPAEntityTypeMapType jpaEntityTypeMap = searchJPAEntityTypeMapType(jpaEntityTypeName);
		if (jpaEntityTypeMap != null)
			return jpaEntityTypeMap.getEDMEntityType();
		else
			return null;
	}

	@Override
	public String mapJPAEntitySet(String jpaEntityTypeName) {
		JPAEntityTypeMapType jpaEntityTypeMap = searchJPAEntityTypeMapType(jpaEntityTypeName);
		if (jpaEntityTypeMap != null)
			return jpaEntityTypeMap.getEDMEntitySet();
		else
			return null;
	}

	@Override
	public String mapJPAAttribute(String jpaEntityTypeName,
			String jpaAttributeName) {
		JPAEntityTypeMapType jpaEntityTypeMap = searchJPAEntityTypeMapType(jpaEntityTypeName);
		if (jpaEntityTypeMap != null)
			for (JPAAttribute jpaAttribute : jpaEntityTypeMap
					.getJPAAttributes().getJPAAttribute())
				if (jpaAttribute.getName().equals(jpaAttributeName))
					return jpaAttribute.getValue();

		return null;
	}

	@Override
	public String mapJPARelationship(String jpaEntityTypeName,
			String jpaRelationshipName) {
		JPAEntityTypeMapType jpaEntityTypeMap = searchJPAEntityTypeMapType(jpaEntityTypeName);
		if (jpaEntityTypeMap != null)
			for (JPARelationship jpaRealtionship : jpaEntityTypeMap
					.getJPARelationships().getJPARelationship())
				if (jpaRealtionship.getName().equals(jpaRelationshipName))
					return jpaRealtionship.getValue();

		return null;
	}

	@Override
	public String mapJPAEmbeddableType(String jpaEmbeddableTypeName) {
		JPAEmbeddableTypeMapType jpaEmbeddableType = searchJPAEmbeddableTypeMapType(jpaEmbeddableTypeName);
		if (jpaEmbeddableType != null)
			return jpaEmbeddableType.getEDMComplexType();
		else
			return null;
	}

	@Override
	public String mapJPAEmbeddableTypeAttribute(String jpaEmbeddableTypeName,
			String jpaAttributeName) {
		JPAEmbeddableTypeMapType jpaEmbeddableType = searchJPAEmbeddableTypeMapType(jpaEmbeddableTypeName);
		if (jpaEmbeddableType != null)
			for (JPAAttribute jpaAttribute : jpaEmbeddableType
					.getJPAAttributes().getJPAAttribute())
				if (jpaAttribute.getName().equals(jpaAttributeName))
					return jpaAttribute.getValue();
		return null;
	}

	private JPAEntityTypeMapType searchJPAEntityTypeMapType(
			String jpaEntityTypeName) {
		for (JPAEntityTypeMapType jpaEntityType : mappingModel
				.getPersistenceUnit().getJPAEntityTypes().getJPAEntityType())
			if (jpaEntityType.getName().equals(jpaEntityTypeName))
				return jpaEntityType;

		return null;
	}

	private JPAEmbeddableTypeMapType searchJPAEmbeddableTypeMapType(
			String jpaEmbeddableTypeName) {
		for (JPAEmbeddableTypeMapType jpaEmbeddableType : mappingModel
				.getPersistenceUnit().getJPAEmbeddableTypes()
				.getJPAEmbeddableType())
			if (jpaEmbeddableType.getName().equals(jpaEmbeddableTypeName))
				return jpaEmbeddableType;

		return null;
	}

	protected InputStream loadMappingModelInputStream() {
		InputStream is = JPAEdmMappingModelService.class.getClassLoader()
				.getResourceAsStream("../../" + mappingModelName);

		return is;

	}

}
