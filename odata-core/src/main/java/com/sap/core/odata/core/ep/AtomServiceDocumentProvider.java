package com.sap.core.odata.core.ep;

import java.io.Writer;
import java.util.Collection;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.core.edm.provider.EdmImplProv;

public class AtomServiceDocumentProvider {

  public static void writeServiceDocument(Edm edm, String serviceRoot, Writer writer) throws EntityProviderException {

    EdmProvider edmProvider = ((EdmImplProv) edm).getEdmProvider();

    try {
      XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);

      xmlStreamWriter.writeStartDocument();
      xmlStreamWriter.setPrefix(Edm.PREFIX_XML, Edm.NAMESPACE_XML_1998);
      xmlStreamWriter.setPrefix(Edm.PREFIX_ATOM, Edm.NAMESPACE_ATOM_2005);
      xmlStreamWriter.setDefaultNamespace(Edm.NAMESPACE_APP_2007);

      xmlStreamWriter.writeStartElement(FormatXml.APP_SERVICE);
      xmlStreamWriter.writeAttribute(Edm.NAMESPACE_XML_1998, FormatXml.XML_BASE, serviceRoot);
      xmlStreamWriter.writeDefaultNamespace(Edm.NAMESPACE_APP_2007);
      xmlStreamWriter.writeNamespace(Edm.PREFIX_ATOM, Edm.NAMESPACE_ATOM_2005);

      xmlStreamWriter.writeStartElement(FormatXml.APP_WORKSPACE);
      xmlStreamWriter.writeStartElement(Edm.NAMESPACE_ATOM_2005, FormatXml.ATOM_TITLE);
      xmlStreamWriter.writeCharacters(FormatXml.ATOM_TITLE_DEFAULT);
      xmlStreamWriter.writeEndElement();

      Collection<Schema> schemas = edmProvider.getSchemas();
      if (schemas != null) {
        for (Schema schema : schemas) {
          Collection<EntityContainer> entityContainers = schema.getEntityContainers();
          if (entityContainers != null) {
            for (EntityContainer entityContainer : entityContainers) {
              Collection<EntitySet> entitySets = entityContainer.getEntitySets();
              for (EntitySet entitySet : entitySets) {
                xmlStreamWriter.writeStartElement(FormatXml.APP_COLLECTION);
                if (entityContainer.isDefaultEntityContainer()) {
                  xmlStreamWriter.writeAttribute(FormatXml.ATOM_HREF, entitySet.getName());
                } else {
                  xmlStreamWriter.writeAttribute(FormatXml.ATOM_HREF, entityContainer.getName() + Edm.DELIMITER + entitySet.getName());
                }
                xmlStreamWriter.writeStartElement(Edm.NAMESPACE_ATOM_2005, FormatXml.ATOM_TITLE);
                xmlStreamWriter.writeCharacters(entitySet.getName());
                xmlStreamWriter.writeEndElement();
                xmlStreamWriter.writeEndElement();
              }
            }
          }
        }
      }

      xmlStreamWriter.writeEndElement();
      xmlStreamWriter.writeEndElement();
      xmlStreamWriter.writeEndDocument();

      xmlStreamWriter.flush();
    } catch (Exception e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (FactoryConfigurationError e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }
}