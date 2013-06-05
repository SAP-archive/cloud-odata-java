/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

  public EdmxProvider parse(final InputStream in, final boolean validate) throws EntityProviderException {
    EdmParser parser = new EdmParser();
    XMLStreamReader streamReader = createStreamReader(in);
    dataServices = parser.readMetadata(streamReader, validate);
    return this;
  }

  @Override
  public EntityContainerInfo getEntityContainerInfo(final String name) throws ODataException {
    if (name != null) {
      for (Schema schema : dataServices.getSchemas()) {
        for (EntityContainer container : schema.getEntityContainers()) {
          if (container.getName().equals(name)) {
            return container;
          }
        }
      }
    } else {
      for (Schema schema : dataServices.getSchemas()) {
        for (EntityContainer container : schema.getEntityContainers()) {
          if (container.isDefaultEntityContainer()) {
            return container;
          }
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

  private XMLStreamReader createStreamReader(final InputStream in) throws EntityProviderException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);

    XMLStreamReader streamReader;
    try {
      streamReader = factory.createXMLStreamReader(in);
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }

    return streamReader;
  }
}
