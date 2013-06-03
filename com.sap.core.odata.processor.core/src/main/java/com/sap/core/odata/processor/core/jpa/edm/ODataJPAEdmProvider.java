package com.sap.core.odata.processor.core.jpa.edm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.factory.ODataJPAFactory;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmModelView;

public class ODataJPAEdmProvider extends EdmProvider {

  private ODataJPAContext oDataJPAContext;
  private JPAEdmModelView jpaEdmModel;

  private List<Schema> schemas;
  private HashMap<String, EntityType> entityTypes;
  private HashMap<String, EntityContainerInfo> entityContainerInfos;
  private HashMap<String, ComplexType> complexTypes;
  private HashMap<String, Association> associations;
  private HashMap<String, FunctionImport> functionImports;

  public ODataJPAEdmProvider() {
    entityTypes = new HashMap<String, EntityType>();
    entityContainerInfos = new HashMap<String, EntityContainerInfo>();
    complexTypes = new HashMap<String, ComplexType>();
    associations = new HashMap<String, Association>();
    functionImports = new HashMap<String, FunctionImport>();
  }

  public ODataJPAEdmProvider(final ODataJPAContext oDataJPAContext) {
    if (oDataJPAContext == null) {
      throw new IllegalArgumentException(
          ODataJPAException.ODATA_JPACTX_NULL);
    }
    entityTypes = new HashMap<String, EntityType>();
    entityContainerInfos = new HashMap<String, EntityContainerInfo>();
    complexTypes = new HashMap<String, ComplexType>();
    associations = new HashMap<String, Association>();
    functionImports = new HashMap<String, FunctionImport>();
    jpaEdmModel = ODataJPAFactory.createFactory().getJPAAccessFactory()
        .getJPAEdmModelView(oDataJPAContext);
  }

  public ODataJPAContext getODataJPAContext() {
    return oDataJPAContext;
  }

  public void setODataJPAContext(final ODataJPAContext jpaContext) {
    oDataJPAContext = jpaContext;
  }

  @Override
  public EntityContainerInfo getEntityContainerInfo(final String name)
      throws ODataException {

    if (entityContainerInfos.containsKey(name)) {
      return entityContainerInfos.get(name);
    } else {

      if (schemas == null) {
        getSchemas();
      }

      for (EntityContainer container : schemas.get(0)
          .getEntityContainers()) {
        if (name == null && container.isDefaultEntityContainer()) {
          entityContainerInfos.put(name, container);
          return container;
        } else if (name != null && name.equals(container.getName())) {
          return container;
        }
      }
    }
    return null;
  }

  @Override
  public EntityType getEntityType(final FullQualifiedName edmFQName)
      throws ODataException {

    String strEdmFQName = edmFQName.toString();

    if (edmFQName != null) {
      if (edmFQName != null && entityTypes.containsKey(strEdmFQName)) {
        return entityTypes.get(strEdmFQName);
      } else if (schemas == null) {
        getSchemas();
      }

      if (edmFQName != null) {

        String entityTypeNamespace = edmFQName.getNamespace();
        String entityTypeName = edmFQName.getName();

        for (Schema schema : schemas) {
          String schemaNamespace = schema.getNamespace();
          if (schemaNamespace.equals(entityTypeNamespace)) {
            for (EntityType et : schema.getEntityTypes()) {
              if (et.getName().equals(entityTypeName)) {
                entityTypes.put(strEdmFQName, et);
                return et;
              }
            }
          }
        }
      }
    }

    return null;
  }

  @Override
  public ComplexType getComplexType(final FullQualifiedName edmFQName)
      throws ODataException {
    ComplexType returnCT = null;
    if (edmFQName != null) {
      if (edmFQName != null
          && complexTypes.containsKey(edmFQName.toString())) {
        return complexTypes.get(edmFQName.toString());
      } else if (schemas == null) {
        getSchemas();
      }

      if (edmFQName != null) {
        for (Schema schema : schemas) {
          if (schema.getNamespace().equals(edmFQName.getNamespace())) {
            for (ComplexType ct : schema.getComplexTypes()) {
              if (ct.getName().equals(edmFQName.getName())) {
                complexTypes.put(edmFQName.toString(), ct);
                returnCT = ct;
              }
            }
          }
        }
      }

    }

    return returnCT;
  }

  @Override
  public Association getAssociation(final FullQualifiedName edmFQName)
      throws ODataException {
    if (edmFQName != null) {
      if (edmFQName != null
          && associations.containsKey(edmFQName.toString())) {
        return associations.get(edmFQName.toString());
      } else if (schemas == null) {
        getSchemas();
      }

      if (edmFQName != null) {
        for (Schema schema : schemas) {
          if (schema.getNamespace().equals(edmFQName.getNamespace())) {
            for (Association association : schema.getAssociations()) {
              if (association.getName().equals(
                  edmFQName.getName())) {
                associations.put(edmFQName.toString(),
                    association);
                return association;
              }
            }
          }
        }
      }

    }
    throw ODataJPAModelException.throwException(
        ODataJPAModelException.INVALID_ASSOCIATION.addContent(edmFQName
            .toString()), null);
  }

  @Override
  public EntitySet getEntitySet(final String entityContainer, final String name)
      throws ODataException {

    EntitySet returnedSet = null;
    EntityContainer container = null;
    if (!entityContainerInfos.containsKey(entityContainer)) {
      container = (EntityContainer) getEntityContainerInfo(entityContainer);
    } else {
      container = (EntityContainer) entityContainerInfos
          .get(entityContainer);
    }

    if (container != null && name != null) {
      for (EntitySet es : container.getEntitySets()) {
        if (name.equals(es.getName())) {
          returnedSet = es;
          break;
        }
      }
    }

    return returnedSet;
  }

  @Override
  public AssociationSet getAssociationSet(final String entityContainer,
      final FullQualifiedName association, final String sourceEntitySetName,
      final String sourceEntitySetRole) throws ODataException {

    EntityContainer container = null;
    if (!entityContainerInfos.containsKey(entityContainer)) {
      container = (EntityContainer) getEntityContainerInfo(entityContainer);
    } else {
      container = (EntityContainer) entityContainerInfos
          .get(entityContainer);
    }

    if (container != null && association != null) {
      for (AssociationSet as : container.getAssociationSets()) {
        if (association.equals(as.getAssociation())) {
          AssociationSetEnd end = as.getEnd1();
          if (sourceEntitySetName.equals(end.getEntitySet())
              && sourceEntitySetRole.equals(end.getRole())) {
            return as;
          } else {
            end = as.getEnd2();
            if (sourceEntitySetName.equals(end.getEntitySet())
                && sourceEntitySetRole.equals(end.getRole())) {
              return as;
            }
          }
        }
      }
    }
    throw ODataJPAModelException.throwException(
        ODataJPAModelException.INVALID_ASSOCIATION_SET
            .addContent(association.toString()), null);
  }

  @Override
  public FunctionImport getFunctionImport(final String entityContainer, final String name)
      throws ODataException {

    if (functionImports.containsKey(name)) {
      return functionImports.get(name);
    }

    FunctionImport returnFI = null;
    EntityContainer container = null;
    if (!entityContainerInfos.containsKey(entityContainer)) {
      container = (EntityContainer) getEntityContainerInfo(entityContainer);
    } else {
      container = (EntityContainer) entityContainerInfos
          .get(entityContainer);
    }

    if (container != null && name != null) {
      for (FunctionImport fi : container.getFunctionImports()) {
        if (name.equals(fi.getName())) {
          functionImports.put(name, fi);
          returnFI = fi;
        }
      }
    }
    return returnFI;
  }

  @Override
  public List<Schema> getSchemas() throws ODataException {
    if (schemas == null && jpaEdmModel != null) {
      jpaEdmModel.getBuilder().build();
      schemas = new ArrayList<Schema>();
      schemas.add(jpaEdmModel.getEdmSchemaView().getEdmSchema());
    }
    if (jpaEdmModel == null) {

      throw ODataJPAModelException.throwException(
          ODataJPAModelException.BUILDER_NULL, null);
    }

    return schemas;

  }

}
