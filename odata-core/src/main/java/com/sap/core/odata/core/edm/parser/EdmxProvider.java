package com.sap.core.odata.core.edm.parser;

import java.io.InputStream;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.DataServices;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.exception.ODataException;

public class EdmxProvider extends EdmProvider {
  private DataServices dataServices;

  public EdmxProvider(final InputStream in) throws XMLStreamException, EntityProviderException {
    EdmParser parser = new EdmParser();
    XMLStreamReader streamReader = createStreamReader(in);
    dataServices = parser.readMetadata(streamReader, false);
  }

  @Override
  public EntityContainerInfo getEntityContainerInfo(final String name) throws ODataException {
    for (Schema schema : dataServices.getSchemas()) {
      for (EntityContainer container : schema.getEntityContainers()) {
        if (container.getName().equals(name)) {
          return container;
        }
      }
    }
    return null;
  }

  @Override
  public EntityType getEntityType(final FullQualifiedName edmFQName) throws ODataException {
    for (Schema schema : dataServices.getSchemas()) {
      if (schema.getNamespace().equals(edmFQName.getNamespace())) {
        for (EntityType entityType : schema.getEntityTypes()) {
          if (entityType.getName().equals(edmFQName.getName())) {
            return entityType;
          }
        }
      }
    }
    return null;
  }

  @Override
  public ComplexType getComplexType(final FullQualifiedName edmFQName) throws ODataException {
    for (Schema schema : dataServices.getSchemas()) {
      if (schema.getNamespace().equals(edmFQName.getNamespace())) {
        for (ComplexType complexType : schema.getComplexTypes()) {
          if (complexType.getName().equals(edmFQName.getName())) {
            return complexType;
          }
        }
      }
    }
    return null;
  }

  @Override
  public Association getAssociation(final FullQualifiedName edmFQName) throws ODataException {
    for (Schema schema : dataServices.getSchemas()) {
      if (schema.getNamespace().equals(edmFQName.getNamespace())) {
        for (Association association : schema.getAssociations()) {
          if (association.getName().equals(edmFQName.getName())) {
            return association;
          }
        }
      }
    }
    return null;
  }

  @Override
  public EntitySet getEntitySet(final String entityContainer, final String name) throws ODataException {
    for (Schema schema : dataServices.getSchemas()) {
      for (EntityContainer container : schema.getEntityContainers()) {
        if (container.getName().equals(entityContainer)) {
          for (EntitySet entitySet : container.getEntitySets()) {
            if (entitySet.getName().equals(name)) {
              return entitySet;
            }
          }
        }
      }
    }
    return null;
  }

  @Override
  public AssociationSet getAssociationSet(final String entityContainer, final FullQualifiedName association, final String sourceEntitySetName, final String sourceEntitySetRole) throws ODataException {
    for (Schema schema : dataServices.getSchemas()) {
      for (EntityContainer container : schema.getEntityContainers()) {
        if (container.getName().equals(entityContainer)) {
          for (AssociationSet associationSet : container.getAssociationSets()) {
            if (associationSet.getAssociation().equals(association)
                && ((associationSet.getEnd1().getEntitySet().equals(sourceEntitySetName) && associationSet.getEnd1().getRole().equals(sourceEntitySetRole))
                || (associationSet.getEnd2().getEntitySet().equals(sourceEntitySetName) && associationSet.getEnd2().getRole().equals(sourceEntitySetRole)))) {
              return associationSet;
            }
          }
        }
      }
    }
    return null;
  }

  @Override
  public FunctionImport getFunctionImport(final String entityContainer, final String name) throws ODataException {
    for (Schema schema : dataServices.getSchemas()) {
      for (EntityContainer container : schema.getEntityContainers()) {
        if (container.getName().equals(entityContainer)) {
          for (FunctionImport function : container.getFunctionImports()) {
            if (function.getName().equals(name)) {
              return function;
            }
          }
        }
      }
    }
    return null;
  }

  @Override
  public List<Schema> getSchemas() throws ODataException {
    return dataServices.getSchemas();
  }

  private XMLStreamReader createStreamReader(final InputStream in)
      throws XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
    XMLStreamReader streamReader = factory
        .createXMLStreamReader(in);

    return streamReader;
  }
}
