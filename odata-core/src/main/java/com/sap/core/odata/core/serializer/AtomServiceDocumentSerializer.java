package com.sap.core.odata.core.serializer;

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
import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.core.edm.provider.EdmImplProv;

public class AtomServiceDocumentSerializer {

  public static void writeServiceDocument(Edm edm, String serviceRoot, Writer writer) throws ODataSerializationException {

    EdmProvider edmProvider = ((EdmImplProv) edm).getEdmProvider();

    try {
      XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);

      xmlStreamWriter.writeStartDocument();
      xmlStreamWriter.setPrefix(Edm.PREFIX_APP, Edm.NAMESPACE_APP);
      xmlStreamWriter.setPrefix(Edm.PREFIX_ATOM, Edm.NAMESPACE_ATOM);
      xmlStreamWriter.setDefaultNamespace(Edm.NAMESPACE_APP);

      xmlStreamWriter.writeStartElement("service");
      xmlStreamWriter.writeAttribute("base", serviceRoot);
      xmlStreamWriter.writeNamespace("atom", Edm.NAMESPACE_ATOM);
      xmlStreamWriter.writeNamespace("app", Edm.NAMESPACE_APP);

      xmlStreamWriter.writeStartElement("workspace");
      xmlStreamWriter.writeStartElement(Edm.NAMESPACE_ATOM, "title");
      xmlStreamWriter.writeCharacters("Default");
      xmlStreamWriter.writeEndElement();

      Collection<Schema> schemas = edmProvider.getSchemas();
      if (schemas != null) {
        for (Schema schema : schemas) {
          Collection<EntityContainer> entityContainers = schema.getEntityContainers();
          if (entityContainers != null) {
            for (EntityContainer entityContainer : entityContainers) {
              Collection<EntitySet> entitySets = entityContainer.getEntitySets();
              for (EntitySet entitySet : entitySets) {
                xmlStreamWriter.writeStartElement("collection");
                xmlStreamWriter.writeAttribute("href", entitySet.getName());
                xmlStreamWriter.writeStartElement(Edm.NAMESPACE_ATOM, "title");
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
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    } catch (FactoryConfigurationError e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }
}