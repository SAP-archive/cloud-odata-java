package org.odata4j.format.xml;

import java.util.ArrayList;
import java.util.List;

import org.odata4j.stax2.StartElement2;
import org.odata4j.stax2.XMLEvent2;
import org.odata4j.stax2.XMLEventReader2;

public class AtomServiceDocumentFormatParser extends XmlFormatParser {

  public static Iterable<AtomWorkspaceInfo> parseWorkspaces(XMLEventReader2 reader) {
    List<AtomWorkspaceInfo> workspaces = new ArrayList<AtomWorkspaceInfo>();
    String baseUrl = null;
    while (reader.hasNext()) {
      XMLEvent2 event = reader.nextEvent();
      if (isStartElement(event, APP_SERVICE)) {
        baseUrl = event.asStartElement().getAttributeByName(XML_BASE).getValue();
      } else if (isStartElement(event, APP_WORKSPACE)) {
        workspaces.add(parseWorkspace(baseUrl, reader, event.asStartElement()));
      } else if (isEndElement(event, APP_SERVICE)) {
        return workspaces;
      }
    }
    throw new IllegalStateException("Closing service tag not found");
  }

  private static AtomWorkspaceInfo parseWorkspace(String baseUrl, XMLEventReader2 reader, StartElement2 startElement) {
    String title = null;
    List<AtomCollectionInfo> collections = new ArrayList<AtomCollectionInfo>();
    while (reader.hasNext()) {
      XMLEvent2 event = reader.nextEvent();
      if (isStartElement(event, ATOM_TITLE)) {
        title = reader.getElementText();
      } else if (event.isEndElement() && event.asEndElement().getName().equals(startElement.getName())) {
        return new AtomWorkspaceInfo(title, collections);
      } else if (isStartElement(event, APP_COLLECTION)) {
        collections.add(parseCollection(baseUrl, reader, event.asStartElement()));
      }
    }
    return new AtomWorkspaceInfo(title, collections);
  }

  private static AtomCollectionInfo parseCollection(String baseUrl, XMLEventReader2 reader, StartElement2 startElement) {
    String href = getAttributeValueIfExists(startElement, "href");
    String url = urlCombine(baseUrl, href);
    String title = null;
    String accept = null;
    while (reader.hasNext()) {
      XMLEvent2 event = reader.nextEvent();
      if (event.isEndElement() && event.asEndElement().getName().equals(startElement.getName())) {
        return new AtomCollectionInfo(href, url, title, accept);
      } else if (isStartElement(event, ATOM_TITLE)) {
        title = reader.getElementText();
      } else if (isStartElement(event, APP_ACCEPT)) {
        accept = reader.getElementText();
      }
    }
    return new AtomCollectionInfo(href, url, title, accept);
  }

}
