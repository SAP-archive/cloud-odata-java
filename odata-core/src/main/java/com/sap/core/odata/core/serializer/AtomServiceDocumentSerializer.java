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

  private static String APP_NAMESPACE = "http://www.w3.org/2007/app";
  private static String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";

  public static void writeServiceDocument(Edm edm, String serviceRoot, Writer writer) throws ODataSerializationException {

    EdmProvider edmProvider = ((EdmImplProv) edm).getEdmProvider();

    try {
      XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);

      xmlStreamWriter.writeStartDocument();
      xmlStreamWriter.setPrefix("app", APP_NAMESPACE);
      xmlStreamWriter.setPrefix("atom", ATOM_NAMESPACE);
      xmlStreamWriter.setDefaultNamespace(APP_NAMESPACE);

      xmlStreamWriter.writeStartElement("service");
      xmlStreamWriter.writeAttribute("base", serviceRoot);
      xmlStreamWriter.writeNamespace("atom", ATOM_NAMESPACE);
      xmlStreamWriter.writeNamespace("app", APP_NAMESPACE);

      xmlStreamWriter.writeStartElement("workspace");
      xmlStreamWriter.writeStartElement(ATOM_NAMESPACE, "title");
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
                xmlStreamWriter.writeStartElement(ATOM_NAMESPACE, "title");
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