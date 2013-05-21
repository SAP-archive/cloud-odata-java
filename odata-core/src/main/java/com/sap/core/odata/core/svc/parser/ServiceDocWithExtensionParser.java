package com.sap.core.odata.core.svc.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.ep.EntityProviderException;

public class ServiceDocWithExtensionParser {
  public static final String ATOM_NAMESPACE_APP = "http://www.w3.org/2007/app";
  public static final String ATOM_NAMESPACE_ATOM = "http://www.w3.org/2005/Atom";
  private String currentHandledStartTagName;
  private static final String DEFAULT_PREFIX = "";
  private static final String XML_PREFIX = "xml";

  public ServiceDokumentImpl readServiceDokument(final XMLStreamReader reader) throws EntityProviderException {
    ServiceDokumentImpl svcDocument = new ServiceDokumentImpl();
    List<WorkspaceImpl> workspaces = new ArrayList<WorkspaceImpl>();
    CommonAttributesImpl attributes = new CommonAttributesImpl();
    try {
      while (reader.hasNext()
          && !(reader.isEndElement() && "service".equals(reader.getLocalName()))) {
        reader.next();
        if (reader.isStartElement()) {
          currentHandledStartTagName = reader.getLocalName();
          if ("service".equals(currentHandledStartTagName)) {
            attributes = parseCommonAttribute(reader);
          } else if ("workspace".equals(currentHandledStartTagName)) {
            workspaces.add(parseWorkspace(reader));
          } else {
            // for extensions
          }
        }
      }

      reader.close();
      return svcDocument.setWorkspaces(workspaces).setCommonAttributes(attributes);
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private CommonAttributesImpl parseCommonAttribute(final XMLStreamReader reader) {
    CommonAttributesImpl attribute = new CommonAttributesImpl();
    List<ExtensionAttributeImpl> extAttributes = new ArrayList<ExtensionAttributeImpl>();
    attribute.setBase(reader.getAttributeValue(null, "base"));
    attribute.setLang(reader.getAttributeValue(null, "lang"));
    for (int i = 0; i < reader.getAttributeCount(); i++) {
      if (!("base".equals(reader.getAttributeLocalName(i)) && XML_PREFIX.equals(reader.getAttributePrefix(i)))
          || ("lang".equals(reader.getAttributeLocalName(i)) && XML_PREFIX.equals(reader.getAttributePrefix(i)))
          || ("local".equals(reader.getAttributeNamespace(i)) || DEFAULT_PREFIX.equals(reader.getAttributePrefix(i)))) {
        extAttributes.add(new ExtensionAttributeImpl()
            .setName(reader.getAttributeLocalName(i))
            .setNamespace(reader.getAttributeNamespace(i))
            .setPrefix(reader.getAttributePrefix(i))
            .setText(reader.getAttributeValue(i)));
      }
    }

    return attribute.setAttributes(extAttributes);
  }

  private WorkspaceImpl parseWorkspace(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_APP, "workspace");

    TitleImpl title = null;
    List<CollectionImpl> collections = new ArrayList<CollectionImpl>();
    CommonAttributesImpl attributes = parseCommonAttribute(reader);
    while (reader.hasNext() && !(reader.isEndElement() && "workspace".equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        currentHandledStartTagName = reader.getLocalName();
        if ("collection".equals(currentHandledStartTagName)) {
          collections.add(parseCollection(reader));
        } else if ("title".equals(currentHandledStartTagName)) {
          title = parseTitle(reader);
        }
      }
    }
    if (title == null) {
      throw new EntityProviderException(EntityProviderException.COMMON.addContent("Missing element title for workspace"));
    }
    return new WorkspaceImpl().setTitle(title).setCollections(collections).setAttributes(attributes);
  }

  private CollectionImpl parseCollection(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_APP, "collection");
    TitleImpl title = null;
    String resourceIdentifier = reader.getAttributeValue(null, "href");
    CommonAttributesImpl attributes = parseCommonAttribute(reader);
    List<ExtensionElementImpl> extElements = new ArrayList<ExtensionElementImpl>();
    List<AcceptImpl> acceptList = new ArrayList<AcceptImpl>();
    List<CategoriesImpl> categories = new ArrayList<CategoriesImpl>();
    if (resourceIdentifier == null) {
      throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE.addContent("Missing Attribute href"));
    }
    while (reader.hasNext() && !(reader.isEndElement() && ATOM_NAMESPACE_APP.equals(reader.getNamespaceURI()) && "collection".equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        currentHandledStartTagName = reader.getLocalName();
        if ("title".equals(currentHandledStartTagName)) {
          title = parseTitle(reader);
        } else if ("accept".equals(currentHandledStartTagName)) {
          acceptList.add(parseAccept(reader));
        } else if ("categories".equals(currentHandledStartTagName)) {
          categories.add(parseCategories(reader));
        } else {
          extElements.add(parseExtensionElement(reader));
        }
      }
    }
    return new CollectionImpl().setHref(resourceIdentifier).setTitle(title).setCommonAttributes(attributes).setExtesionElements(extElements).setAcceptElements(acceptList).setCategories(categories);
  }

  private TitleImpl parseTitle(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_ATOM, "title");
    String text = "";
    while (reader.hasNext() && !(reader.isEndElement() && ATOM_NAMESPACE_ATOM.equals(reader.getNamespaceURI()) && "title".equals(reader.getLocalName()))) {
      if (reader.isCharacters()) {
        text += reader.getText();
      }
      reader.next();
    }
    return new TitleImpl().setText(text);
  }

  private AcceptImpl parseAccept(final XMLStreamReader reader) throws XMLStreamException {
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_APP, "accept");
    CommonAttributesImpl commonAttributes = parseCommonAttribute(reader);
    String text = "";
    while (reader.hasNext() && !(reader.isEndElement() && ATOM_NAMESPACE_APP.equals(reader.getNamespaceURI()) && "accept".equals(reader.getLocalName()))) {
      if (reader.isCharacters()) {
        text += reader.getText();
      }
      reader.next();
    }
    return new AcceptImpl().setCommonAttributes(commonAttributes).setText(text);
  }

  private CategoriesImpl parseCategories(final XMLStreamReader reader) throws XMLStreamException {
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_APP, "categories");
    CategoriesImpl categories = new CategoriesImpl();
    String href = reader.getAttributeValue(null, "href");
    if(href == null) {
      categories.setFixed(reader.getAttributeValue(null, "fixed"));
    }
    return categories;
  }

  private ExtensionElementImpl parseExtensionElement(final XMLStreamReader reader) {
    ExtensionElementImpl extElement = new ExtensionElementImpl();
    if (!(ATOM_NAMESPACE_APP.equals(reader.getNamespaceURI())
    || ("titel".equals(reader.getLocalName()) || ATOM_NAMESPACE_ATOM.equals(reader.getNamespaceURI())))) {
      extElement.setName(reader.getLocalName());
      extElement.setNamespace(reader.getNamespaceURI());
      extElement.setPrefix(reader.getPrefix());
    }
    return extElement;
  }

}
