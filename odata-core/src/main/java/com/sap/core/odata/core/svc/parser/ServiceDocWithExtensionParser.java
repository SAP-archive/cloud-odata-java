package com.sap.core.odata.core.svc.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.edm.EdmAction;
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
    List<ExtensionElementImpl> extElements = new ArrayList<ExtensionElementImpl>();
    CommonAttributesImpl attributes = new CommonAttributesImpl();
    try {
      while (reader.hasNext()
          && !(reader.isEndElement() && ATOM_NAMESPACE_APP.equals(reader.getNamespaceURI()) && ServiceDocConstants.APP_SERVICE.equals(reader.getLocalName()))) {
        reader.next();
        if (reader.isStartElement()) {
          currentHandledStartTagName = reader.getLocalName();
          if (ServiceDocConstants.APP_SERVICE.equals(currentHandledStartTagName)) {
            attributes = parseCommonAttribute(reader);
          } else if (ServiceDocConstants.APP_WORKSPACE.equals(currentHandledStartTagName)) {
            workspaces.add(parseWorkspace(reader));
          } else {
            ExtensionElementImpl extElement = parseExtensionElement(reader);
            if (extElement != null) {
              extElements.add(extElement);
            }
          }
        }
      }
      if (workspaces.isEmpty()) {
        throw new EntityProviderException(EntityProviderException.COMMON.addContent("Service element must contain at least one workspace element"));
      }
      reader.close();
      return svcDocument.setWorkspaces(workspaces).setCommonAttributes(attributes).setExtesionElements(extElements);
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private CommonAttributesImpl parseCommonAttribute(final XMLStreamReader reader) {
    CommonAttributesImpl attribute = new CommonAttributesImpl();
    List<ExtensionAttributeImpl> extAttributes = new ArrayList<ExtensionAttributeImpl>();
    attribute.setBase(reader.getAttributeValue(null, ServiceDocConstants.BASE));
    attribute.setLang(reader.getAttributeValue(null, ServiceDocConstants.LANG));
    for (int i = 0; i < reader.getAttributeCount(); i++) {
      if (!(ServiceDocConstants.BASE.equals(reader.getAttributeLocalName(i)) && XML_PREFIX.equals(reader.getAttributePrefix(i)))
          || (ServiceDocConstants.LANG.equals(reader.getAttributeLocalName(i)) && XML_PREFIX.equals(reader.getAttributePrefix(i)))
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
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_APP, ServiceDocConstants.APP_WORKSPACE);

    TitleImpl title = null;
    List<CollectionImpl> collections = new ArrayList<CollectionImpl>();
    List<ExtensionElementImpl> extElements = new ArrayList<ExtensionElementImpl>();
    CommonAttributesImpl attributes = parseCommonAttribute(reader);
    while (reader.hasNext() && !(reader.isEndElement() && ATOM_NAMESPACE_APP.equals(reader.getNamespaceURI()) && ServiceDocConstants.APP_WORKSPACE.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        currentHandledStartTagName = reader.getLocalName();
        if (ServiceDocConstants.APP_COLLECTION.equals(currentHandledStartTagName)) {
          collections.add(parseCollection(reader));
        } else if (ServiceDocConstants.ATOM_TITLE.equals(currentHandledStartTagName)) {
          title = parseTitle(reader);
        } else {
          extElements.add(parseExtensionSansTitleElement(reader));
        }
      }
    }
    if (title == null) {
      throw new EntityProviderException(EntityProviderException.COMMON.addContent("Missing element title for workspace"));
    }
    return new WorkspaceImpl().setTitle(title).setCollections(collections).setAttributes(attributes).setExtesionElements(extElements);
  }

  private CollectionImpl parseCollection(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_APP, ServiceDocConstants.APP_COLLECTION);
    TitleImpl title = null;
    String resourceIdentifier = reader.getAttributeValue(null, ServiceDocConstants.HREF);
    CommonAttributesImpl attributes = parseCommonAttribute(reader);
    List<ExtensionElementImpl> extElements = new ArrayList<ExtensionElementImpl>();
    List<AcceptImpl> acceptList = new ArrayList<AcceptImpl>();
    List<CategoriesImpl> categories = new ArrayList<CategoriesImpl>();
    if (resourceIdentifier == null) {
      throw new EntityProviderException(EntityProviderException.MISSING_ATTRIBUTE.addContent("Missing Attribute href"));
    }
    while (reader.hasNext() && !(reader.isEndElement() && ATOM_NAMESPACE_APP.equals(reader.getNamespaceURI()) && ServiceDocConstants.APP_COLLECTION.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        currentHandledStartTagName = reader.getLocalName();
        if (ServiceDocConstants.ATOM_TITLE.equals(currentHandledStartTagName)) {
          title = parseTitle(reader);
        } else if (ServiceDocConstants.APP_ACCEPT.equals(currentHandledStartTagName)) {
          acceptList.add(parseAccept(reader));
        } else if (ServiceDocConstants.APP_CATEGORIES.equals(currentHandledStartTagName)) {
          categories.add(parseCategories(reader));
        } else {
          extElements.add(parseExtensionSansTitleElement(reader));
        }
      }
    }
    return new CollectionImpl().setHref(resourceIdentifier).setTitle(title).setCommonAttributes(attributes).setExtesionElements(extElements).setAcceptElements(acceptList).setCategories(categories);
  }

  private TitleImpl parseTitle(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_ATOM, ServiceDocConstants.ATOM_TITLE);
    String text = "";
    while (reader.hasNext() && !(reader.isEndElement() && ATOM_NAMESPACE_ATOM.equals(reader.getNamespaceURI()) && ServiceDocConstants.ATOM_TITLE.equals(reader.getLocalName()))) {
      if (reader.isCharacters()) {
        text += reader.getText();
      }
      reader.next();
    }
    return new TitleImpl().setText(text);
  }

  private AcceptImpl parseAccept(final XMLStreamReader reader) throws XMLStreamException {
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_APP, ServiceDocConstants.APP_ACCEPT);
    CommonAttributesImpl commonAttributes = parseCommonAttribute(reader);
    String text = "";
    while (reader.hasNext() && !(reader.isEndElement() && ATOM_NAMESPACE_APP.equals(reader.getNamespaceURI()) && ServiceDocConstants.APP_ACCEPT.equals(reader.getLocalName()))) {
      if (reader.isCharacters()) {
        text += reader.getText();
      }
      reader.next();
    }
    return new AcceptImpl().setCommonAttributes(commonAttributes).setText(text);
  }

  private CategoriesImpl parseCategories(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_APP, ServiceDocConstants.APP_CATEGORIES);
    CategoriesImpl categories = new CategoriesImpl();
    String href = reader.getAttributeValue(null, ServiceDocConstants.HREF);
    String fixed = reader.getAttributeValue(null, ServiceDocConstants.FIXED);
    categories.setScheme(reader.getAttributeValue(null, ServiceDocConstants.SCHEME));
    categories.setHref(href);
    if (href == null) {
      for (int i = 0; i < EdmAction.values().length; i++) {
        if (Fixed.values()[i].name().equalsIgnoreCase(fixed)) {
          categories.setFixed(Fixed.values()[i]);
        }
      }
      if (categories.getFixed() == null) {
        categories.setFixed(Fixed.NO);
      }
      List<CategoryImpl> categoriesList = new ArrayList<CategoryImpl>();
      while (reader.hasNext() && !(reader.isEndElement() && ATOM_NAMESPACE_APP.equals(reader.getNamespaceURI()) && ServiceDocConstants.APP_CATEGORIES.equals(reader.getLocalName()))) {
        reader.next();
        if (reader.isStartElement()) {
          currentHandledStartTagName = reader.getLocalName();
          if (ServiceDocConstants.ATOM_CATEGORY.equals(currentHandledStartTagName)) {
            categoriesList.add(parseCategory(reader));
          }
        }
      }
      categories.setCategoryList(categoriesList);
    }
    if ((href != null && fixed != null && categories.getScheme() != null) ||
        (href == null && fixed == null && categories.getScheme() == null)) {
      throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid Attributes"));
    }
    return categories;
  }

  private CategoryImpl parseCategory(final XMLStreamReader reader) throws XMLStreamException {
    reader.require(XMLStreamConstants.START_ELEMENT, ATOM_NAMESPACE_ATOM, ServiceDocConstants.ATOM_CATEGORY);
    CategoryImpl category = new CategoryImpl();
    category.setScheme(reader.getAttributeValue(null, ServiceDocConstants.SCHEME));
    category.setTerm(reader.getAttributeValue(null, ServiceDocConstants.TERM));
    category.setLabel(reader.getAttributeValue(null, ServiceDocConstants.LABEL));
    CommonAttributesImpl attributes = parseCommonAttribute(reader);
    return category.setCommonAttribute(attributes);
  }

  private ExtensionElementImpl parseExtensionSansTitleElement(final XMLStreamReader reader) {
    ExtensionElementImpl extElement = new ExtensionElementImpl();
    if (!(ATOM_NAMESPACE_APP.equals(reader.getNamespaceURI())
    || (ServiceDocConstants.ATOM_TITLE.equals(reader.getLocalName()) || ATOM_NAMESPACE_ATOM.equals(reader.getNamespaceURI())))) {
      extElement.setName(reader.getLocalName());
      extElement.setNamespace(reader.getNamespaceURI());
      extElement.setPrefix(reader.getPrefix());
    }
    return extElement;
  }

  private ExtensionElementImpl parseExtensionElement(final XMLStreamReader reader) throws XMLStreamException, EntityProviderException {
    ExtensionElementImpl extElement = null;
    if (!ATOM_NAMESPACE_APP.equals(reader.getNamespaceURI())) {
      extElement = new ExtensionElementImpl();
      List<ExtensionElementImpl> extensionElements = new ArrayList<ExtensionElementImpl>();
      extElement.setName(reader.getLocalName());
      extElement.setNamespace(reader.getNamespaceURI());
      extElement.setPrefix(reader.getPrefix());
      extElement.setAttributes(parseAttribute(reader));
      while (reader.hasNext() && !(reader.isEndElement() && extElement.getName() != null && extElement.getName().equals(reader.getLocalName()))) {
        reader.next();
        if (reader.isCharacters()) {
          extElement.setText(reader.getText());
        } else if (reader.isStartElement()) {
          extensionElements.add(parseExtensionElement(reader));
        }
      }
      extElement.setElements(extensionElements);
      if (extElement.getText() == null && extElement.getAttributes().isEmpty() && extElement.getElements().isEmpty()) {
        throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid extension element"));
      }
    }
    return extElement;
  }

  private List<ExtensionAttributeImpl> parseAttribute(final XMLStreamReader reader) {
    List<ExtensionAttributeImpl> extAttributes = new ArrayList<ExtensionAttributeImpl>();
    for (int i = 0; i < reader.getAttributeCount(); i++) {
      {
        extAttributes.add(new ExtensionAttributeImpl()
            .setName(reader.getAttributeLocalName(i))
            .setNamespace(reader.getAttributeNamespace(i))
            .setPrefix(reader.getAttributePrefix(i))
            .setText(reader.getAttributeValue(i)));
      }
    }

    return extAttributes;
  }

}
