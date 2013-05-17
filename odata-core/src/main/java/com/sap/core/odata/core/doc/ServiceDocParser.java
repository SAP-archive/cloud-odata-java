package com.sap.core.odata.core.doc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.doc.ServiceDocument;
import com.sap.core.odata.api.doc.ServiceDocumentParserException;
import com.sap.core.odata.api.edm.provider.EntitySet;

public class ServiceDocParser {
  private static final String ATOM_NAMESPACE_ATOM = "http://www.w3.org/2005/Atom";
  private List<EntitySet> entitySets = new ArrayList<EntitySet>();
  private String currentHandledStartTagName;
  public static final String ATOM_NAMESPACE_APP = "http://www.w3.org/2007/app";

  public List<EntitySet> readServiceDokument(final XMLStreamReader reader) throws ServiceDocumentParserException {
    try {
      while (reader.hasNext()
          && !(reader.isEndElement() && ATOM_NAMESPACE_APP.equals(reader.getNamespaceURI()) && ServiceDocConstants.APP_SERVICE.equals(reader.getLocalName()))) {
        reader.next();
        if (reader.isStartElement()) {
          if (ServiceDocConstants.APP_WORKSPACE.equals(reader.getLocalName())) {
            parseWorkspace(reader);
          }
        }
      }

      reader.close();
      return entitySets;
    } catch (XMLStreamException e) {
      throw new ServiceDocumentParserException("The structure of the service document is not valid");
    }
  }

  private void parseWorkspace(final XMLStreamReader reader) throws XMLStreamException, ServiceDocumentParserException{
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_APP, ServiceDocConstants.APP_WORKSPACE);
    while (reader.hasNext() && !(reader.isEndElement() && ATOM_NAMESPACE_APP.equals(reader.getNamespaceURI()) && ServiceDocConstants.APP_WORKSPACE.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        currentHandledStartTagName = reader.getLocalName();
        if (ServiceDocConstants.APP_COLLECTION.equals(currentHandledStartTagName)) {
          parseCollection(reader);
        }
      }
    }
  }

  private void parseCollection(final XMLStreamReader reader) throws XMLStreamException, ServiceDocumentParserException {
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_APP, ServiceDocConstants.APP_COLLECTION);
    String resourceIdentifier = reader.getAttributeValue(null, "href");
    if (resourceIdentifier != null) {
      entitySets.add(new EntitySet().setName(resourceIdentifier));
    } else {
      throw new ServiceDocumentParserException("Missing Attribute href");
    }
    while (reader.hasNext() && !(reader.isEndElement() && ATOM_NAMESPACE_APP.equals(reader.getNamespaceURI()) && ServiceDocConstants.APP_COLLECTION.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        currentHandledStartTagName = reader.getLocalName();
        if (ServiceDocConstants.ATOM_TITLE.equals(currentHandledStartTagName)) {
          parseTitle(reader);
        }
      }
    }
  }

  private void parseTitle(final XMLStreamReader reader) throws XMLStreamException {
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_ATOM, ServiceDocConstants.ATOM_TITLE);
    while (reader.hasNext() && !(reader.isEndElement() && ATOM_NAMESPACE_ATOM.equals(reader.getNamespaceURI()) && ServiceDocConstants.ATOM_TITLE.equals(reader.getLocalName()))) {
      if (reader.isCharacters()) {}
      reader.next();
    }
  }
  public List<EntitySet> parseXml(final InputStream in) throws ServiceDocumentParserException {
    return readServiceDokument(createStreamReader(in));
  }

  private XMLStreamReader createStreamReader(final InputStream in) throws ServiceDocumentParserException {
    if (in != null) {
      XMLInputFactory factory = XMLInputFactory.newInstance();
      factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
      factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
      XMLStreamReader streamReader;
      try {
        streamReader = factory.createXMLStreamReader(in);
      } catch (XMLStreamException e) {
        throw new ServiceDocumentParserException("XML Exception");
      }
      return streamReader;
    } else {
      throw new ServiceDocumentParserException("Null InputStream");
    }
  }

}
