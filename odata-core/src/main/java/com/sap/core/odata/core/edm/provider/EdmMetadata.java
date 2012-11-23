package com.sap.core.odata.core.edm.provider;

import java.io.Reader;
import java.io.Writer;
import java.util.Collection;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.DataServices;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.exception.ODataSerializationException;

public class EdmMetadata {

  private static String EDMX_NAMESPACE = "http://schemas.microsoft.com/ado/2007/06/edmx";
  private static String METADATA_NAMESPACE = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata";
  private static String EDM_NAMESPACE = "http://schemas.microsoft.com/ado/2008/09/edm";

  //TODO Implement + Exception handling
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
          xmlStreamWriter.writeDefaultNamespace(EDM_NAMESPACE);

          Collection<EntityType> entityTypes = schema.getEntityTypes();
          if (entityTypes != null) {
            for (EntityType entityType : entityTypes) {
              xmlStreamWriter.writeStartElement("EntityType");
              xmlStreamWriter.writeAttribute("Name", entityType.getName());
              xmlStreamWriter.writeEndElement();
            }
          }

          Collection<ComplexType> complexTypes = schema.getComplexTypes();
          if (complexTypes != null) {
            for (ComplexType complexType : complexTypes) {
              xmlStreamWriter.writeStartElement("ComplexType");
              xmlStreamWriter.writeAttribute("Name", complexType.getName());
              xmlStreamWriter.writeEndElement();
            }
          }

          Collection<Association> associations = schema.getAssociations();
          if (associations != null) {
            for (Association association : associations) {
              xmlStreamWriter.writeStartElement("Association");
              xmlStreamWriter.writeAttribute("Name", association.getName());
              xmlStreamWriter.writeEndElement();
            }
          }

          Collection<EntityContainer> entityContainers = schema.getEntityContainers();
          if (entityContainers != null) {
            for (EntityContainer entityContainer : entityContainers) {
              xmlStreamWriter.writeStartElement("EntityContainer");
              xmlStreamWriter.writeAttribute("Name", entityContainer.getName());

              Collection<EntitySet> entitySets = entityContainer.getEntitySets();
              if (entitySets != null) {
                for (EntitySet entitySet : entitySets) {
                  xmlStreamWriter.writeStartElement("EntitySet");
                  xmlStreamWriter.writeAttribute("Name", entitySet.getName());
                  xmlStreamWriter.writeEndElement();
                }
              }

              Collection<AssociationSet> associationSets = entityContainer.getAssociationSets();
              if (associationSets != null) {
                for (AssociationSet associationSet : associationSets) {
                  xmlStreamWriter.writeStartElement("AssociationSet");
                  xmlStreamWriter.writeAttribute("Name", associationSet.getName());
                  xmlStreamWriter.writeEndElement();
                }
              }

              Collection<FunctionImport> functionImports = entityContainer.getFunctionImports();
              if (functionImports != null) {
                for (FunctionImport functionImport : functionImports) {
                  xmlStreamWriter.writeStartElement("FunctionImport");
                  xmlStreamWriter.writeAttribute("Name", functionImport.getName());
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
    } catch (XMLStreamException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (FactoryConfigurationError e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }

  //TODO Implement + Exception handling
  public static DataServices readMetadata(Reader reader) {
    return null;
  }
}