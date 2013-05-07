package com.sap.core.odata.core.svc.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.ep.EntityProviderException;

public class ServiceDocParser {
  private static final String ATOM_NAMESPACE_ATOM = "http://www.w3.org/2005/Atom";
  private List<EntitySet> entitySets = new ArrayList<EntitySet>();
  private String currentHandledStartTagName;
  public static final String ATOM_NAMESPACE_APP = "http://www.w3.org/2007/app";

  public List<EntitySet> readServiceDokument(final XMLStreamReader reader) throws EntityProviderException {
    try {
      while (reader.hasNext()
          && !(reader.isEndElement() && "service".equals(reader.getLocalName()))) {
        reader.next();
        if (reader.isStartElement()) {
          if ("workspace".equals(reader.getLocalName())) {
            parseWorkspace(reader);
          } else {
            // for extensions
          }
        }
      }

      reader.close();
      return entitySets;
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private void parseWorkspace(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_APP, "workspace");
    if (ATOM_NAMESPACE_APP.equals(reader.getNamespaceURI())) {
      reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_APP, "workspace");
    }
    while (reader.hasNext() && !(reader.isEndElement() && "workspace".equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        currentHandledStartTagName = reader.getLocalName();
        if ("collection".equals(currentHandledStartTagName)) {
          parseCollection(reader);
        }
      }
    }
  }

  private void parseCollection(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_APP, "collection");
    String resourceIdentifier = reader.getAttributeValue(null, "href");
    if (resourceIdentifier != null) {
      entitySets.add(new EntitySet().setName(resourceIdentifier));
    } else {
      throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE.addContent("Missing Attribute href"));
    }
    while (reader.hasNext() && !(reader.isEndElement() && ATOM_NAMESPACE_APP.equals(reader.getNamespaceURI()) && "collection".equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        currentHandledStartTagName = reader.getLocalName();
        if ("title".equals(currentHandledStartTagName)) {
          parseTitle(reader);
        }
      }
    }
  }

  private void parseTitle(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_ATOM, "title");
    while (reader.hasNext() && !(reader.isEndElement() && ATOM_NAMESPACE_ATOM.equals(reader.getNamespaceURI()) && "title".equals(reader.getLocalName()))) {
      if (reader.isCharacters()) {}
      reader.next();
    }
  }

}
