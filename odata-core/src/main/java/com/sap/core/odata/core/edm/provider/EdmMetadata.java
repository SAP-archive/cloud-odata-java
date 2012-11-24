package com.sap.core.odata.core.edm.provider;

import java.io.Reader;
import java.io.Writer;
import java.util.Collection;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.DataServices;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.edm.provider.FunctionImportParameter;
import com.sap.core.odata.api.edm.provider.NavigationProperty;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.PropertyRef;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.edm.provider.Using;
import com.sap.core.odata.api.serialization.ODataSerializationException;

public class EdmMetadata {

  private static String EDMX_NAMESPACE = "http://schemas.microsoft.com/ado/2007/06/edmx";
  private static String METADATA_NAMESPACE = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata";
  private static String EDM_NAMESPACE = "http://schemas.microsoft.com/ado/2008/09/edm";

  public static void writeMetadata(DataServices metadata, Writer writer) throws ODataSerializationException {

    try {
      XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);

      xmlStreamWriter.writeStartDocument();
      xmlStreamWriter.setPrefix("edmx", EDMX_NAMESPACE);
      xmlStreamWriter.setPrefix("m", METADATA_NAMESPACE);
      xmlStreamWriter.setDefaultNamespace(EDM_NAMESPACE);

      xmlStreamWriter.writeStartElement(EDMX_NAMESPACE, "Edmx");
      xmlStreamWriter.writeAttribute("Version", "1.0");
      xmlStreamWriter.writeNamespace("edmx", EDMX_NAMESPACE);

      xmlStreamWriter.writeStartElement(EDMX_NAMESPACE, "DataServices");
      xmlStreamWriter.writeAttribute("m", METADATA_NAMESPACE, "DataServiceVersion", metadata.getDataServiceVersion());
      xmlStreamWriter.writeNamespace("m", METADATA_NAMESPACE);

      Collection<Schema> schemas = metadata.getSchemas();
      if (schemas != null) {
        for (Schema schema : schemas) {
          xmlStreamWriter.writeStartElement("Schema");
          xmlStreamWriter.writeAttribute("Namespace", schema.getNamespace());
          if (schema.getAlias() != null) {
            xmlStreamWriter.writeAttribute("Alias", schema.getAlias());
          }
          xmlStreamWriter.writeDefaultNamespace(EDM_NAMESPACE);

          Collection<Using> usings = schema.getUsings();
          if (usings != null) {
            for (Using using : usings) {
              xmlStreamWriter.writeStartElement("Using");
              xmlStreamWriter.writeAttribute("Namespace", using.getNamespace());
              xmlStreamWriter.writeAttribute("Alias", using.getAlias());
              xmlStreamWriter.writeEndElement();
            }
          }

          Collection<EntityType> entityTypes = schema.getEntityTypes();
          if (entityTypes != null) {
            for (EntityType entityType : entityTypes) {
              xmlStreamWriter.writeStartElement("EntityType");
              xmlStreamWriter.writeAttribute("Name", entityType.getName());
              if (entityType.getBaseType() != null) {
                xmlStreamWriter.writeAttribute("BaseType", entityType.getBaseType().toString());
              }
              if (entityType.isAbstract()) {
                xmlStreamWriter.writeAttribute("Abstract", "true");
              }
              if (entityType.isHasStream()) {
                xmlStreamWriter.writeAttribute("m", METADATA_NAMESPACE, "HasStream", "true");
              }
              if (entityType.getKey() != null) {
                xmlStreamWriter.writeStartElement("Key");
                Collection<PropertyRef> propertyRefs = entityType.getKey().getKeys();
                for (PropertyRef propertyRef : propertyRefs) {
                  xmlStreamWriter.writeStartElement("PropertyRef");
                  xmlStreamWriter.writeAttribute("Name", propertyRef.getName());
                  xmlStreamWriter.writeEndElement();
                }
                xmlStreamWriter.writeEndElement();
              }
              
              Collection<Property> properties = entityType.getProperties();
              if (properties != null) {
                writeProperties(properties, xmlStreamWriter);
              }
              
              Collection<NavigationProperty> navigationProperties = entityType.getNavigationProperties();
              if (navigationProperties != null) {
                for (NavigationProperty navigationProperty : navigationProperties) {
                  xmlStreamWriter.writeStartElement("NavigationProperty");
                  xmlStreamWriter.writeAttribute("Name", navigationProperty.getName());
                  xmlStreamWriter.writeAttribute("Relationship", navigationProperty.getRelationship().toString());
                  xmlStreamWriter.writeAttribute("FromRole", navigationProperty.getFromRole());
                  xmlStreamWriter.writeAttribute("ToRole", navigationProperty.getToRole());
                  xmlStreamWriter.writeEndElement();
                }
              }

              xmlStreamWriter.writeEndElement();
            }
          }

          Collection<ComplexType> complexTypes = schema.getComplexTypes();
          if (complexTypes != null) {
            for (ComplexType complexType : complexTypes) {
              xmlStreamWriter.writeStartElement("ComplexType");
              xmlStreamWriter.writeAttribute("Name", complexType.getName());
              if (complexType.getBaseType() != null) {
                xmlStreamWriter.writeAttribute("BaseType", complexType.getBaseType().toString());
              }
              if (complexType.isAbstract()) {
                xmlStreamWriter.writeAttribute("Abstract", "true");
              }
              Collection<Property> properties = complexType.getProperties();
              if (properties != null) {
                writeProperties(properties, xmlStreamWriter);
              }
              xmlStreamWriter.writeEndElement();
            }
          }

          Collection<Association> associations = schema.getAssociations();
          if (associations != null) {
            for (Association association : associations) {
              xmlStreamWriter.writeStartElement("Association");
              xmlStreamWriter.writeAttribute("Name", association.getName());
              writeAssociationEnd(association.getEnd1(), xmlStreamWriter);
              writeAssociationEnd(association.getEnd2(), xmlStreamWriter);
              xmlStreamWriter.writeEndElement();
            }
          }

          Collection<EntityContainer> entityContainers = schema.getEntityContainers();
          if (entityContainers != null) {
            for (EntityContainer entityContainer : entityContainers) {
              xmlStreamWriter.writeStartElement("EntityContainer");
              xmlStreamWriter.writeAttribute("Name", entityContainer.getName());
              if (entityContainer.getExtendz() != null) {
                xmlStreamWriter.writeAttribute("Extends", entityContainer.getExtendz());
              }
              if (entityContainer.isDefaultEntityContainer()) {
                xmlStreamWriter.writeAttribute("m", METADATA_NAMESPACE, "IsDefaultEntityContainer", "true");
              }

              Collection<EntitySet> entitySets = entityContainer.getEntitySets();
              if (entitySets != null) {
                for (EntitySet entitySet : entitySets) {
                  xmlStreamWriter.writeStartElement("EntitySet");
                  xmlStreamWriter.writeAttribute("Name", entitySet.getName());
                  xmlStreamWriter.writeAttribute("EntityType", entitySet.getEntityType().toString());
                  xmlStreamWriter.writeEndElement();
                }
              }

              Collection<AssociationSet> associationSets = entityContainer.getAssociationSets();
              if (associationSets != null) {
                for (AssociationSet associationSet : associationSets) {
                  xmlStreamWriter.writeStartElement("AssociationSet");
                  xmlStreamWriter.writeAttribute("Name", associationSet.getName());
                  xmlStreamWriter.writeAttribute("Association", associationSet.getAssociation().toString());
                  writeAssociationSetEnd(associationSet.getEnd1(), xmlStreamWriter);
                  writeAssociationSetEnd(associationSet.getEnd2(), xmlStreamWriter);
                  xmlStreamWriter.writeEndElement();
                }
              }

              Collection<FunctionImport> functionImports = entityContainer.getFunctionImports();
              if (functionImports != null) {
                for (FunctionImport functionImport : functionImports) {
                  xmlStreamWriter.writeStartElement("FunctionImport");
                  xmlStreamWriter.writeAttribute("Name", functionImport.getName());
                  if (functionImport.getReturnType() != null) {
                    xmlStreamWriter.writeAttribute("ReturnType", functionImport.getReturnType().toString());
                  }
                  if (functionImport.getEntitySet() != null) {
                    xmlStreamWriter.writeAttribute("EntitySet", functionImport.getEntitySet());
                  }
                  if (functionImport.getHttpMethod() != null) {
                    xmlStreamWriter.writeAttribute("m", METADATA_NAMESPACE, "HttpMethod", functionImport.getHttpMethod());
                  }
                  Collection<FunctionImportParameter> functionImportParameters = functionImport.getParameters();
                  if (functionImportParameters != null) {
                    for (FunctionImportParameter functionImportParameter : functionImportParameters) {
                      xmlStreamWriter.writeStartElement("Parameter");
                      xmlStreamWriter.writeAttribute("Name", functionImportParameter.getName());
                      xmlStreamWriter.writeAttribute("Type", functionImportParameter.getQualifiedName().toString());
                      if (functionImportParameter.getMode() != null) {
                        xmlStreamWriter.writeAttribute("Mode", functionImportParameter.getMode());
                      }
                      EdmFacets facets = functionImportParameter.getFacets();
                      if (facets != null) {
                        if (facets.getMaxLength() != null) {
                          xmlStreamWriter.writeAttribute("MaxLength", facets.getMaxLength().toString());
                        }
                        if (facets.getPrecision() != null) {
                          xmlStreamWriter.writeAttribute("Precision", facets.getPrecision().toString());
                        }
                        if (facets.getScale() != null) {
                          xmlStreamWriter.writeAttribute("Scale", facets.getScale().toString());
                        }
                      }
                      xmlStreamWriter.writeEndElement();
                    }
                  }
                  xmlStreamWriter.writeEndElement();
                }
              }

              xmlStreamWriter.writeEndElement();
            }
          }

          xmlStreamWriter.writeEndElement();
        }
      }

      xmlStreamWriter.writeEndElement();
      xmlStreamWriter.writeEndElement();
      xmlStreamWriter.writeEndDocument();

      xmlStreamWriter.flush();
    } catch (XMLStreamException e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    } catch (FactoryConfigurationError e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  private static void writeProperties(Collection<Property> properties, XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
    for (Property property : properties) {
      xmlStreamWriter.writeStartElement("Property");
      xmlStreamWriter.writeAttribute("Name", property.getName());
      xmlStreamWriter.writeAttribute("Type", property.getType().toString());
      EdmFacets facets = property.getFacets();
      if (facets != null) {
        if (facets.isNullable() != null) {
          xmlStreamWriter.writeAttribute("Nullable", facets.isNullable().toString().toLowerCase());
        }
        if (facets.getDefaultValue() != null) {
          xmlStreamWriter.writeAttribute("DefaultValue", facets.getDefaultValue());
        }
        if (facets.getMaxLength() != null) {
          xmlStreamWriter.writeAttribute("MaxLength", facets.getMaxLength().toString());
        }
        if (facets.isFixedLength() != null) {
          xmlStreamWriter.writeAttribute("FixedLength", facets.isFixedLength().toString().toLowerCase());
        }
        if (facets.getPrecision() != null) {
          xmlStreamWriter.writeAttribute("Precision", facets.getPrecision().toString());
        }
        if (facets.getScale() != null) {
          xmlStreamWriter.writeAttribute("Scale", facets.getScale().toString());
        }
        if (facets.isUnicode() != null) {
          xmlStreamWriter.writeAttribute("Unicode", facets.isUnicode().toString());
        }
        if (facets.getCollation() != null) {
          xmlStreamWriter.writeAttribute("Collation", facets.getCollation());
        }
        if (facets.getConcurrencyMode() != null) {
          xmlStreamWriter.writeAttribute("ConcurrencyMode", facets.getConcurrencyMode().toString());
        }
        //TODO customizable feed mappings
      }
      xmlStreamWriter.writeEndElement();
    }
  }

  private static void writeAssociationEnd(AssociationEnd end1, XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
    xmlStreamWriter.writeStartElement("End");
    xmlStreamWriter.writeAttribute("Type", end1.getType().toString());
    xmlStreamWriter.writeAttribute("Multiplicity", end1.getMultiplicity().toString());
    xmlStreamWriter.writeAttribute("Role", end1.getRole());
    //TODO referential constraint
    xmlStreamWriter.writeEndElement();
  }

  private static void writeAssociationSetEnd(AssociationSetEnd end, XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
    xmlStreamWriter.writeStartElement("End");
    xmlStreamWriter.writeAttribute("EntitySet", end.getEntitySet().toString());
    if (end.getRole() != null) {
      xmlStreamWriter.writeAttribute("Role", end.getRole());
    }
    xmlStreamWriter.writeEndElement();
  }
  
  //TODO Implement + Exception handling
  public static DataServices readMetadata(Reader reader) {
    return null;
  }
}