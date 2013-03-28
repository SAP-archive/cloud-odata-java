package com.sap.core.odata.core.ep.producer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.provider.AnnotationAttribute;
import com.sap.core.odata.api.edm.provider.AnnotationElement;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.CustomizableFeedMappings;
import com.sap.core.odata.api.edm.provider.DataServices;
import com.sap.core.odata.api.edm.provider.Documentation;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.edm.provider.FunctionImportParameter;
import com.sap.core.odata.api.edm.provider.Key;
import com.sap.core.odata.api.edm.provider.NavigationProperty;
import com.sap.core.odata.api.edm.provider.OnDelete;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.PropertyRef;
import com.sap.core.odata.api.edm.provider.ReferentialConstraint;
import com.sap.core.odata.api.edm.provider.ReferentialConstraintRole;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.api.edm.provider.Using;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.core.exception.ODataRuntimeException;

public class XmlMetadataProducer {

  public static void writeMetadata(final DataServices metadata, final XMLStreamWriter xmlStreamWriter, Map<String, String> predefinedNamespaces) throws EntityProviderException {

    try {
      xmlStreamWriter.writeStartDocument();
      xmlStreamWriter.setPrefix(Edm.PREFIX_EDMX, Edm.NAMESPACE_EDMX_2007_06);
      xmlStreamWriter.setPrefix(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08);
      xmlStreamWriter.setDefaultNamespace(Edm.NAMESPACE_EDM_2008_09);

      xmlStreamWriter.writeStartElement(Edm.NAMESPACE_EDMX_2007_06, "Edmx");
      xmlStreamWriter.writeAttribute("Version", "1.0");
      xmlStreamWriter.writeNamespace(Edm.PREFIX_EDMX, Edm.NAMESPACE_EDMX_2007_06);

      xmlStreamWriter.writeStartElement(Edm.NAMESPACE_EDMX_2007_06, "DataServices");
      xmlStreamWriter.writeAttribute(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08, "DataServiceVersion", metadata.getDataServiceVersion());
      xmlStreamWriter.writeNamespace(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08);

      if (predefinedNamespaces != null) {
        for (Map.Entry<String, String> entry : predefinedNamespaces.entrySet()) {
          xmlStreamWriter.writeNamespace(entry.getKey(), entry.getValue());
        }
      } else {
        predefinedNamespaces = new HashMap<String, String>();
      }

      Collection<Schema> schemas = metadata.getSchemas();
      if (schemas != null) {
        for (Schema schema : schemas) {
          xmlStreamWriter.writeStartElement("Schema");
          if (schema.getAlias() != null) {
            xmlStreamWriter.writeAttribute("Alias", schema.getAlias());
          }
          xmlStreamWriter.writeAttribute("Namespace", schema.getNamespace());
          xmlStreamWriter.writeDefaultNamespace(Edm.NAMESPACE_EDM_2008_09);

          writeAnnotationAttributes(schema.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

          Collection<Using> usings = schema.getUsings();
          if (usings != null) {
            for (Using using : usings) {
              xmlStreamWriter.writeStartElement("Using");
              xmlStreamWriter.writeAttribute("Namespace", using.getNamespace());
              xmlStreamWriter.writeAttribute("Alias", using.getAlias());
              writeAnnotationAttributes(using.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);
              writeDocumentation(using.getDocumentation(), predefinedNamespaces, xmlStreamWriter);
              writeAnnotationElements(using.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);
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
                xmlStreamWriter.writeAttribute(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08, "HasStream", "true");
              }

              writeCustomizableFeedMappings(entityType.getCustomizableFeedMappings(), xmlStreamWriter);

              writeAnnotationAttributes(entityType.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

              writeDocumentation(entityType.getDocumentation(), predefinedNamespaces, xmlStreamWriter);

              Key key = entityType.getKey();
              if (key != null) {
                xmlStreamWriter.writeStartElement("Key");

                writeAnnotationAttributes(key.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

                Collection<PropertyRef> propertyRefs = entityType.getKey().getKeys();
                for (PropertyRef propertyRef : propertyRefs) {
                  xmlStreamWriter.writeStartElement("PropertyRef");

                  writeAnnotationAttributes(propertyRef.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

                  xmlStreamWriter.writeAttribute("Name", propertyRef.getName());

                  writeAnnotationElements(propertyRef.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);

                  xmlStreamWriter.writeEndElement();
                }

                writeAnnotationElements(key.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);

                xmlStreamWriter.writeEndElement();
              }

              Collection<Property> properties = entityType.getProperties();
              if (properties != null) {
                writeProperties(properties, predefinedNamespaces, xmlStreamWriter);
              }

              Collection<NavigationProperty> navigationProperties = entityType.getNavigationProperties();
              if (navigationProperties != null) {
                for (NavigationProperty navigationProperty : navigationProperties) {
                  xmlStreamWriter.writeStartElement("NavigationProperty");
                  xmlStreamWriter.writeAttribute("Name", navigationProperty.getName());
                  xmlStreamWriter.writeAttribute("Relationship", navigationProperty.getRelationship().toString());
                  xmlStreamWriter.writeAttribute("FromRole", navigationProperty.getFromRole());
                  xmlStreamWriter.writeAttribute("ToRole", navigationProperty.getToRole());

                  writeAnnotationAttributes(navigationProperty.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

                  writeDocumentation(navigationProperty.getDocumentation(), predefinedNamespaces, xmlStreamWriter);

                  writeAnnotationElements(navigationProperty.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);

                  xmlStreamWriter.writeEndElement();
                }
              }

              writeAnnotationElements(entityType.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);

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

              writeAnnotationAttributes(complexType.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

              writeDocumentation(complexType.getDocumentation(), predefinedNamespaces, xmlStreamWriter);

              Collection<Property> properties = complexType.getProperties();
              if (properties != null) {
                writeProperties(properties, predefinedNamespaces, xmlStreamWriter);
              }

              writeAnnotationElements(complexType.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);

              xmlStreamWriter.writeEndElement();
            }
          }

          Collection<Association> associations = schema.getAssociations();
          if (associations != null) {
            for (Association association : associations) {
              xmlStreamWriter.writeStartElement("Association");
              xmlStreamWriter.writeAttribute("Name", association.getName());

              writeAnnotationAttributes(association.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

              writeDocumentation(association.getDocumentation(), predefinedNamespaces, xmlStreamWriter);

              writeAssociationEnd(association.getEnd1(), predefinedNamespaces, xmlStreamWriter);
              writeAssociationEnd(association.getEnd2(), predefinedNamespaces, xmlStreamWriter);

              ReferentialConstraint referentialConstraint = association.getReferentialConstraint();
              if (referentialConstraint != null) {
                xmlStreamWriter.writeStartElement("ReferentialConstraint");
                writeAnnotationAttributes(referentialConstraint.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);
                writeDocumentation(referentialConstraint.getDocumentation(), predefinedNamespaces, xmlStreamWriter);

                ReferentialConstraintRole principal = referentialConstraint.getPrincipal();
                xmlStreamWriter.writeStartElement("Principal");
                xmlStreamWriter.writeAttribute("Role", principal.getRole());
                writeAnnotationAttributes(principal.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

                for (PropertyRef propertyRef : principal.getPropertyRefs()) {
                  xmlStreamWriter.writeStartElement("PropertyRef");
                  xmlStreamWriter.writeAttribute("Name", propertyRef.getName());
                  xmlStreamWriter.writeEndElement();
                }
                writeAnnotationElements(principal.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);
                xmlStreamWriter.writeEndElement();

                ReferentialConstraintRole dependent = referentialConstraint.getDependent();
                xmlStreamWriter.writeStartElement("Dependent");
                xmlStreamWriter.writeAttribute("Role", dependent.getRole());
                writeAnnotationAttributes(dependent.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

                for (PropertyRef propertyRef : dependent.getPropertyRefs()) {
                  xmlStreamWriter.writeStartElement("PropertyRef");
                  xmlStreamWriter.writeAttribute("Name", propertyRef.getName());
                  xmlStreamWriter.writeEndElement();
                }
                writeAnnotationElements(dependent.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);
                xmlStreamWriter.writeEndElement();

                writeAnnotationElements(referentialConstraint.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);
                xmlStreamWriter.writeEndElement();
              }

              writeAnnotationElements(association.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);

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
                xmlStreamWriter.writeAttribute(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08, "IsDefaultEntityContainer", "true");
              }

              writeAnnotationAttributes(entityContainer.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

              writeDocumentation(entityContainer.getDocumentation(), predefinedNamespaces, xmlStreamWriter);

              Collection<EntitySet> entitySets = entityContainer.getEntitySets();
              if (entitySets != null) {
                for (EntitySet entitySet : entitySets) {
                  xmlStreamWriter.writeStartElement("EntitySet");
                  xmlStreamWriter.writeAttribute("Name", entitySet.getName());
                  xmlStreamWriter.writeAttribute("EntityType", entitySet.getEntityType().toString());

                  writeAnnotationAttributes(entitySet.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

                  writeDocumentation(entitySet.getDocumentation(), predefinedNamespaces, xmlStreamWriter);

                  writeAnnotationElements(entitySet.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);

                  xmlStreamWriter.writeEndElement();
                }
              }

              Collection<AssociationSet> associationSets = entityContainer.getAssociationSets();
              if (associationSets != null) {
                for (AssociationSet associationSet : associationSets) {
                  xmlStreamWriter.writeStartElement("AssociationSet");
                  xmlStreamWriter.writeAttribute("Name", associationSet.getName());
                  xmlStreamWriter.writeAttribute("Association", associationSet.getAssociation().toString());

                  writeAnnotationAttributes(associationSet.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

                  writeDocumentation(associationSet.getDocumentation(), predefinedNamespaces, xmlStreamWriter);

                  writeAssociationSetEnd(associationSet.getEnd1(), predefinedNamespaces, xmlStreamWriter);
                  writeAssociationSetEnd(associationSet.getEnd2(), predefinedNamespaces, xmlStreamWriter);

                  writeAnnotationElements(associationSet.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);

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
                    xmlStreamWriter.writeAttribute(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08, "HttpMethod", functionImport.getHttpMethod());
                  }

                  writeAnnotationAttributes(functionImport.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

                  writeDocumentation(functionImport.getDocumentation(), predefinedNamespaces, xmlStreamWriter);

                  Collection<FunctionImportParameter> functionImportParameters = functionImport.getParameters();
                  if (functionImportParameters != null) {
                    for (FunctionImportParameter functionImportParameter : functionImportParameters) {
                      xmlStreamWriter.writeStartElement("Parameter");
                      xmlStreamWriter.writeAttribute("Name", functionImportParameter.getName());
                      xmlStreamWriter.writeAttribute("Type", functionImportParameter.getType().getFullQualifiedName().toString());
                      if (functionImportParameter.getMode() != null) {
                        xmlStreamWriter.writeAttribute("Mode", functionImportParameter.getMode());
                      }

                      writeFacets(xmlStreamWriter, functionImportParameter.getFacets());

                      writeAnnotationAttributes(functionImportParameter.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

                      writeDocumentation(functionImportParameter.getDocumentation(), predefinedNamespaces, xmlStreamWriter);

                      writeAnnotationElements(functionImportParameter.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);

                      xmlStreamWriter.writeEndElement();
                    }
                  }

                  writeAnnotationElements(functionImport.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);

                  xmlStreamWriter.writeEndElement();
                }
              }

              writeAnnotationElements(entityContainer.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);

              xmlStreamWriter.writeEndElement();
            }
          }

          writeAnnotationElements(schema.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);

          xmlStreamWriter.writeEndElement();
        }
      }

      xmlStreamWriter.writeEndElement();
      xmlStreamWriter.writeEndElement();
      xmlStreamWriter.writeEndDocument();

      xmlStreamWriter.flush();
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (FactoryConfigurationError e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private static void writeCustomizableFeedMappings(final CustomizableFeedMappings customizableFeedMappings, final XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
    if (customizableFeedMappings != null) {
      if (customizableFeedMappings.getFcKeepInContent() != null) {
        xmlStreamWriter.writeAttribute(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08, "FC_KeepInContent", customizableFeedMappings.getFcKeepInContent().toString().toLowerCase(Locale.ROOT));
      }
      if (customizableFeedMappings.getFcContentKind() != null) {
        xmlStreamWriter.writeAttribute(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08, "FC_ContentKind", customizableFeedMappings.getFcContentKind().toString());
      }
      if (customizableFeedMappings.getFcNsPrefix() != null) {
        xmlStreamWriter.writeAttribute(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08, "FC_NsPrefix", customizableFeedMappings.getFcNsPrefix());
      }
      if (customizableFeedMappings.getFcNsUri() != null) {
        xmlStreamWriter.writeAttribute(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08, "FC_NsUri", customizableFeedMappings.getFcNsUri());
      }
      if (customizableFeedMappings.getFcSourcePath() != null) {
        xmlStreamWriter.writeAttribute(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08, "FC_SourcePath", customizableFeedMappings.getFcSourcePath());
      }
      if (customizableFeedMappings.getFcTargetPath() != null) {
        xmlStreamWriter.writeAttribute(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08, "FC_TargetPath", customizableFeedMappings.getFcTargetPath().toString());
      }
    }
  }

  private static void writeProperties(final Collection<Property> properties, final Map<String, String> predefinedNamespaces, final XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
    for (Property property : properties) {
      xmlStreamWriter.writeStartElement("Property");
      xmlStreamWriter.writeAttribute("Name", property.getName());
      if (property instanceof SimpleProperty) {
        xmlStreamWriter.writeAttribute("Type", ((SimpleProperty) property).getType().getFullQualifiedName().toString());
      } else if (property instanceof ComplexProperty) {
        xmlStreamWriter.writeAttribute("Type", ((ComplexProperty) property).getType().toString());
      } else {
        throw new ODataRuntimeException();
      }

      writeFacets(xmlStreamWriter, property.getFacets());

      if (property.getMimeType() != null) {
        xmlStreamWriter.writeAttribute(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08, "MimeType", property.getMimeType());
      }

      writeCustomizableFeedMappings(property.getCustomizableFeedMappings(), xmlStreamWriter);

      writeAnnotationAttributes(property.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

      writeDocumentation(property.getDocumentation(), predefinedNamespaces, xmlStreamWriter);

      writeAnnotationElements(property.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);

      xmlStreamWriter.writeEndElement();
    }
  }

  private static void writeFacets(final XMLStreamWriter xmlStreamWriter, final EdmFacets facets) throws XMLStreamException {
    if (facets != null) {
      if (facets.isNullable() != null) {
        xmlStreamWriter.writeAttribute("Nullable", facets.isNullable().toString().toLowerCase(Locale.ROOT));
      }
      if (facets.getDefaultValue() != null) {
        xmlStreamWriter.writeAttribute("DefaultValue", facets.getDefaultValue());
      }
      if (facets.getMaxLength() != null) {
        xmlStreamWriter.writeAttribute("MaxLength", facets.getMaxLength().toString());
      }
      if (facets.isFixedLength() != null) {
        xmlStreamWriter.writeAttribute("FixedLength", facets.isFixedLength().toString().toLowerCase(Locale.ROOT));
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
    }
  }

  private static void writeAssociationEnd(final AssociationEnd end, final Map<String, String> predefinedNamespaces, final XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
    xmlStreamWriter.writeStartElement("End");
    xmlStreamWriter.writeAttribute("Type", end.getType().toString());
    xmlStreamWriter.writeAttribute("Multiplicity", end.getMultiplicity().toString());
    if (end.getRole() != null) {
      xmlStreamWriter.writeAttribute("Role", end.getRole());
    }

    writeAnnotationAttributes(end.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

    writeDocumentation(end.getDocumentation(), predefinedNamespaces, xmlStreamWriter);

    OnDelete onDelete = end.getOnDelete();
    if (onDelete != null) {
      xmlStreamWriter.writeStartElement("OnDelete");
      xmlStreamWriter.writeAttribute("Action", onDelete.getAction().toString());
      writeAnnotationAttributes(onDelete.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);
      writeDocumentation(onDelete.getDocumentation(), predefinedNamespaces, xmlStreamWriter);
      writeAnnotationElements(onDelete.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);
      xmlStreamWriter.writeEndElement();
    }

    writeAnnotationElements(end.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);

    xmlStreamWriter.writeEndElement();
  }

  private static void writeAssociationSetEnd(final AssociationSetEnd end, final Map<String, String> predefinedNamespaces, final XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
    xmlStreamWriter.writeStartElement("End");
    xmlStreamWriter.writeAttribute("EntitySet", end.getEntitySet().toString());
    if (end.getRole() != null) {
      xmlStreamWriter.writeAttribute("Role", end.getRole());
    }
    writeAnnotationAttributes(end.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);
    writeDocumentation(end.getDocumentation(), predefinedNamespaces, xmlStreamWriter);
    writeAnnotationElements(end.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);
    xmlStreamWriter.writeEndElement();
  }

  private static void writeDocumentation(final Documentation documentation, final Map<String, String> predefinedNamespaces, final XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
    if (documentation != null) {
      xmlStreamWriter.writeStartElement("Documentation");
      writeAnnotationAttributes(documentation.getAnnotationAttributes(), predefinedNamespaces, null, xmlStreamWriter);

      xmlStreamWriter.writeStartElement("Summary");
      xmlStreamWriter.writeCharacters(documentation.getSummary());
      xmlStreamWriter.writeEndElement();

      xmlStreamWriter.writeStartElement("LongDescription");
      xmlStreamWriter.writeCharacters(documentation.getLongDescription());
      xmlStreamWriter.writeEndElement();

      writeAnnotationElements(documentation.getAnnotationElements(), predefinedNamespaces, xmlStreamWriter);
      xmlStreamWriter.writeEndElement();
    }
  }

  private static void writeAnnotationAttributes(final Collection<AnnotationAttribute> annotationAttributes, final Map<String, String> predefinedNamespaces, ArrayList<String> setNamespaces, final XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
    if (annotationAttributes != null) {
      if (setNamespaces == null) {
        setNamespaces = new ArrayList<String>();
      }
      for (AnnotationAttribute annotationAttribute : annotationAttributes) {
        if (annotationAttribute.getNamespace() != null) {
          xmlStreamWriter.writeAttribute(annotationAttribute.getPrefix(), annotationAttribute.getNamespace(), annotationAttribute.getName(), annotationAttribute.getText());
          if (setNamespaces.contains(annotationAttribute.getNamespace()) == false && predefinedNamespaces.containsValue(annotationAttribute.getNamespace()) == false) {
            xmlStreamWriter.writeNamespace(annotationAttribute.getPrefix(), annotationAttribute.getNamespace());
            setNamespaces.add(annotationAttribute.getNamespace());
          }
        } else {
          xmlStreamWriter.writeAttribute(annotationAttribute.getName(), annotationAttribute.getText());
        }
      }
    }
  }

  private static void writeAnnotationElements(final Collection<AnnotationElement> annotationElements, final Map<String, String> predefinedNamespaces, final XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
    if (annotationElements != null) {
      for (AnnotationElement annotationElement : annotationElements) {
        ArrayList<String> setNamespaces = new ArrayList<String>();
        if (annotationElement.getNamespace() != null) {
          if (annotationElement.getPrefix() != null) {
            xmlStreamWriter.writeStartElement(annotationElement.getPrefix(), annotationElement.getName(), annotationElement.getNamespace());
            if (!predefinedNamespaces.containsValue(annotationElement.getNamespace())) {
              xmlStreamWriter.writeNamespace(annotationElement.getPrefix(), annotationElement.getNamespace());
              setNamespaces.add(annotationElement.getNamespace());
            }
          } else {
            xmlStreamWriter.writeStartElement("", annotationElement.getName(), annotationElement.getNamespace());
            if (!predefinedNamespaces.containsValue(annotationElement.getNamespace())) {
              xmlStreamWriter.writeNamespace("", annotationElement.getNamespace());
              setNamespaces.add(annotationElement.getNamespace());
            }
          }
        } else {
          xmlStreamWriter.writeStartElement(annotationElement.getName());
        }

        writeAnnotationAttributes(annotationElement.getAttributes(), predefinedNamespaces, setNamespaces, xmlStreamWriter);

        if (annotationElement.getChildElements() != null) {
          writeAnnotationElements(annotationElement.getChildElements(), predefinedNamespaces, xmlStreamWriter);
        } else {
          if (annotationElement.getText() != null) {
            xmlStreamWriter.writeCharacters(annotationElement.getText());
          }
        }

        xmlStreamWriter.writeEndElement();
      }
    }
  }
}
