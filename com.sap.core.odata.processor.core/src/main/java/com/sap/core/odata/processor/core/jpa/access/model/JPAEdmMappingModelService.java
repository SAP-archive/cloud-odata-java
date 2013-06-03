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

  public JPAEdmMappingModelService(final ODataJPAContext ctx) {
    mappingModelName = ctx.getJPAEdmMappingModel();
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

        mappingModel = (JPAEdmMappingModel) unmarshaller.unmarshal(is);

        if (mappingModel != null) {
          mappingModelExists = true;
        }

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
  public String mapJPAPersistenceUnit(final String persistenceUnitName) {

    JPAPersistenceUnitMapType persistenceUnit = mappingModel
        .getPersistenceUnit();
    if (persistenceUnit.getName().equals(persistenceUnitName)) {
      return persistenceUnit.getEDMSchemaNamespace();
    }

    return null;
  }

  @Override
  public String mapJPAEntityType(final String jpaEntityTypeName) {

    JPAEntityTypeMapType jpaEntityTypeMap = searchJPAEntityTypeMapType(jpaEntityTypeName);
    if (jpaEntityTypeMap != null) {
      return jpaEntityTypeMap.getEDMEntityType();
    } else {
      return null;
    }
  }

  @Override
  public String mapJPAEntitySet(final String jpaEntityTypeName) {
    JPAEntityTypeMapType jpaEntityTypeMap = searchJPAEntityTypeMapType(jpaEntityTypeName);
    if (jpaEntityTypeMap != null) {
      return jpaEntityTypeMap.getEDMEntitySet();
    } else {
      return null;
    }
  }

  @Override
  public String mapJPAAttribute(final String jpaEntityTypeName,
      final String jpaAttributeName) {
    JPAEntityTypeMapType jpaEntityTypeMap = searchJPAEntityTypeMapType(jpaEntityTypeName);
    if (jpaEntityTypeMap != null
        && jpaEntityTypeMap.getJPAAttributes() != null) {
      // fixing attributes
      // removal issue
      // from mapping
      for (JPAAttribute jpaAttribute : jpaEntityTypeMap
          .getJPAAttributes().getJPAAttribute()) {
        if (jpaAttribute.getName().equals(jpaAttributeName)) {
          return jpaAttribute.getValue();
        }
      }
    }

    return null;
  }

  @Override
  public String mapJPARelationship(final String jpaEntityTypeName,
      final String jpaRelationshipName) {
    JPAEntityTypeMapType jpaEntityTypeMap = searchJPAEntityTypeMapType(jpaEntityTypeName);
    if (jpaEntityTypeMap != null) {
      for (JPARelationship jpaRealtionship : jpaEntityTypeMap
          .getJPARelationships().getJPARelationship()) {
        if (jpaRealtionship.getName().equals(jpaRelationshipName)) {
          return jpaRealtionship.getValue();
        }
      }
    }

    return null;
  }

  @Override
  public String mapJPAEmbeddableType(final String jpaEmbeddableTypeName) {
    JPAEmbeddableTypeMapType jpaEmbeddableType = searchJPAEmbeddableTypeMapType(jpaEmbeddableTypeName);
    if (jpaEmbeddableType != null) {
      return jpaEmbeddableType.getEDMComplexType();
    } else {
      return null;
    }
  }

  @Override
  public String mapJPAEmbeddableTypeAttribute(final String jpaEmbeddableTypeName,
      final String jpaAttributeName) {
    JPAEmbeddableTypeMapType jpaEmbeddableType = searchJPAEmbeddableTypeMapType(jpaEmbeddableTypeName);
    if (jpaEmbeddableType != null && jpaEmbeddableType.getJPAAttributes() != null) {
      for (JPAAttribute jpaAttribute : jpaEmbeddableType
          .getJPAAttributes().getJPAAttribute()) {
        if (jpaAttribute.getName().equals(jpaAttributeName)) {
          return jpaAttribute.getValue();
        }
      }
    }
    return null;
  }

  private JPAEntityTypeMapType searchJPAEntityTypeMapType(
      final String jpaEntityTypeName) {
    for (JPAEntityTypeMapType jpaEntityType : mappingModel
        .getPersistenceUnit().getJPAEntityTypes().getJPAEntityType()) {
      if (jpaEntityType.getName().equals(jpaEntityTypeName)) {
        return jpaEntityType;
      }
    }

    return null;
  }

  private JPAEmbeddableTypeMapType searchJPAEmbeddableTypeMapType(
      final String jpaEmbeddableTypeName) {
    for (JPAEmbeddableTypeMapType jpaEmbeddableType : mappingModel
        .getPersistenceUnit().getJPAEmbeddableTypes()
        .getJPAEmbeddableType()) {
      if (jpaEmbeddableType.getName().equals(jpaEmbeddableTypeName)) {
        return jpaEmbeddableType;
      }
    }

    return null;
  }

  protected InputStream loadMappingModelInputStream() {
    InputStream is = JPAEdmMappingModelService.class.getClassLoader()
        .getResourceAsStream("../../" + mappingModelName);

    return is;

  }

  @Override
  public boolean checkExclusionOfJPAEntityType(final String jpaEntityTypeName) {
    JPAEntityTypeMapType type = searchJPAEntityTypeMapType(jpaEntityTypeName);
    if (type != null) {
      return type.isExclude();
    }
    return false;
  }

  @Override
  public boolean checkExclusionOfJPAAttributeType(final String jpaEntityTypeName,
      final String jpaAttributeName) {
    JPAEntityTypeMapType type = searchJPAEntityTypeMapType(jpaEntityTypeName);
    if (type != null && type.getJPAAttributes() != null) {
      for (JPAAttribute jpaAttribute : type.getJPAAttributes()
          .getJPAAttribute()) {
        if (jpaAttribute.getName().equals(jpaAttributeName)) {
          return jpaAttribute.isExclude();
        }
      }
    }
    return false;
  }

  @Override
  public boolean checkExclusionOfJPAEmbeddableType(
      final String jpaEmbeddableTypeName) {
    JPAEmbeddableTypeMapType type = searchJPAEmbeddableTypeMapType(jpaEmbeddableTypeName);
    if (type != null) {
      return type.isExclude();
    }
    return false;
  }

  @Override
  public boolean checkExclusionOfJPAEmbeddableAttributeType(
      final String jpaEmbeddableTypeName, final String jpaAttributeName) {
    JPAEmbeddableTypeMapType type = searchJPAEmbeddableTypeMapType(jpaEmbeddableTypeName);
    if (type != null && type.getJPAAttributes() != null) {
      for (JPAAttribute jpaAttribute : type.getJPAAttributes()
          .getJPAAttribute()) {
        if (jpaAttribute.getName().equals(jpaAttributeName)) {
          return jpaAttribute.isExclude();
        }
      }
    }
    return false;
  }
}
