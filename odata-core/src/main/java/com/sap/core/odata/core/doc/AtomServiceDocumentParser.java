package com.sap.core.odata.core.doc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.sap.core.odata.api.doc.Fixed;
import com.sap.core.odata.api.doc.ServiceDocumentParserException;
import com.sap.core.odata.api.doc.AtomServiceDocument;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.core.ep.util.FormatXml;

public class AtomServiceDocumentParser {
  private String currentHandledStartTagName;
  private static final String DEFAULT_PREFIX = "";

  public AtomServiceDokumentImpl readServiceDokument(final XMLStreamReader reader) throws ServiceDocumentParserException {
    AtomServiceDokumentImpl svcDocument = new AtomServiceDokumentImpl();
    List<WorkspaceImpl> workspaces = new ArrayList<WorkspaceImpl>();
    List<ExtensionElementImpl> extElements = new ArrayList<ExtensionElementImpl>();
    CommonAttributesImpl attributes = new CommonAttributesImpl();
    try {
      while (reader.hasNext()
          && !(reader.isEndElement() && Edm.NAMESPACE_APP_2007.equals(reader.getNamespaceURI()) && FormatXml.APP_SERVICE.equals(reader.getLocalName()))) {
        reader.next();
        if (reader.isStartElement()) {
          currentHandledStartTagName = reader.getLocalName();
          if (FormatXml.APP_SERVICE.equals(currentHandledStartTagName)) {
            attributes = parseCommonAttribute(reader);
          } else if (FormatXml.APP_WORKSPACE.equals(currentHandledStartTagName)) {
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
        throw new ServiceDocumentParserException("Service element must contain at least one workspace element");
      }
      reader.close();
      return svcDocument.setWorkspaces(workspaces).setCommonAttributes(attributes).setExtesionElements(extElements);
    } catch (XMLStreamException e) {
      throw new ServiceDocumentParserException("Invalid service document");
    }
  }

  private CommonAttributesImpl parseCommonAttribute(final XMLStreamReader reader) {
    CommonAttributesImpl attribute = new CommonAttributesImpl();
    List<ExtensionAttributeImpl> extAttributes = new ArrayList<ExtensionAttributeImpl>();
    attribute.setBase(reader.getAttributeValue(null, FormatXml.XML_BASE));
    attribute.setLang(reader.getAttributeValue(null, FormatXml.XML_LANG));
    for (int i = 0; i < reader.getAttributeCount(); i++) {
      if (!(FormatXml.XML_BASE.equals(reader.getAttributeLocalName(i)) && Edm.PREFIX_XML.equals(reader.getAttributePrefix(i)))
          || (FormatXml.XML_LANG.equals(reader.getAttributeLocalName(i)) && Edm.PREFIX_XML.equals(reader.getAttributePrefix(i)))
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

  private WorkspaceImpl parseWorkspace(final XMLStreamReader reader) throws XMLStreamException, ServiceDocumentParserException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_APP_2007, FormatXml.APP_WORKSPACE);

    TitleImpl title = null;
    List<CollectionImpl> collections = new ArrayList<CollectionImpl>();
    List<ExtensionElementImpl> extElements = new ArrayList<ExtensionElementImpl>();
    CommonAttributesImpl attributes = parseCommonAttribute(reader);
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_APP_2007.equals(reader.getNamespaceURI()) && FormatXml.APP_WORKSPACE.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        currentHandledStartTagName = reader.getLocalName();
        if (FormatXml.APP_COLLECTION.equals(currentHandledStartTagName)) {
          collections.add(parseCollection(reader));
        } else if (FormatXml.ATOM_TITLE.equals(currentHandledStartTagName)) {
          title = parseTitle(reader);
        } else {
          extElements.add(parseExtensionSansTitleElement(reader));
        }
      }
    }
    if (title == null) {
      throw new ServiceDocumentParserException("Missing element title for workspace");
    }
    return new WorkspaceImpl().setTitle(title).setCollections(collections).setAttributes(attributes).setExtesionElements(extElements);
  }

  private CollectionImpl parseCollection(final XMLStreamReader reader) throws XMLStreamException, ServiceDocumentParserException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_APP_2007, FormatXml.APP_COLLECTION);
    TitleImpl title = null;
    String resourceIdentifier = reader.getAttributeValue(null, FormatXml.ATOM_HREF);
    CommonAttributesImpl attributes = parseCommonAttribute(reader);
    List<ExtensionElementImpl> extElements = new ArrayList<ExtensionElementImpl>();
    List<AcceptImpl> acceptList = new ArrayList<AcceptImpl>();
    List<CategoriesImpl> categories = new ArrayList<CategoriesImpl>();
    if (resourceIdentifier == null) {
      throw new ServiceDocumentParserException("Missing Attribute href");
    }
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_APP_2007.equals(reader.getNamespaceURI()) && FormatXml.APP_COLLECTION.equals(reader.getLocalName()))) {
      reader.next();
      if (reader.isStartElement()) {
        currentHandledStartTagName = reader.getLocalName();
        if (FormatXml.ATOM_TITLE.equals(currentHandledStartTagName)) {
          title = parseTitle(reader);
        } else if (FormatXml.APP_ACCEPT.equals(currentHandledStartTagName)) {
          acceptList.add(parseAccept(reader));
        } else if (FormatXml.APP_CATEGORIES.equals(currentHandledStartTagName)) {
          categories.add(parseCategories(reader));
        } else {
          extElements.add(parseExtensionSansTitleElement(reader));
        }
      }
    }
    return new CollectionImpl().setHref(resourceIdentifier).setTitle(title).setCommonAttributes(attributes).setExtesionElements(extElements).setAcceptElements(acceptList).setCategories(categories);
  }

  private TitleImpl parseTitle(final XMLStreamReader reader) throws XMLStreamException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_ATOM_2005, FormatXml.ATOM_TITLE);
    String text = "";
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_ATOM_2005.equals(reader.getNamespaceURI()) && FormatXml.ATOM_TITLE.equals(reader.getLocalName()))) {
      if (reader.isCharacters()) {
        text += reader.getText();
      }
      reader.next();
    }
    return new TitleImpl().setText(text);
  }

  private AcceptImpl parseAccept(final XMLStreamReader reader) throws XMLStreamException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_APP_2007, FormatXml.APP_ACCEPT);
    CommonAttributesImpl commonAttributes = parseCommonAttribute(reader);
    String text = "";
    while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_APP_2007.equals(reader.getNamespaceURI()) && FormatXml.APP_ACCEPT.equals(reader.getLocalName()))) {
      if (reader.isCharacters()) {
        text += reader.getText();
      }
      reader.next();
    }
    return new AcceptImpl().setCommonAttributes(commonAttributes).setText(text);
  }

  private CategoriesImpl parseCategories(final XMLStreamReader reader) throws XMLStreamException, ServiceDocumentParserException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_APP_2007, FormatXml.APP_CATEGORIES);
    CategoriesImpl categories = new CategoriesImpl();
    String href = reader.getAttributeValue(null, FormatXml.ATOM_HREF);
    String fixed = reader.getAttributeValue(null, FormatXml.APP_CATEGORIES_FIXED);
    categories.setScheme(reader.getAttributeValue(null, FormatXml.APP_CATEGORIES_SCHEME));
    categories.setHref(href);
    if (href == null) {
      for (int i = 0; i < Fixed.values().length; i++) {
        if (Fixed.values()[i].name().equalsIgnoreCase(fixed)) {
          categories.setFixed(Fixed.values()[i]);
        }
      }
      if (categories.getFixed() == null) {
        categories.setFixed(Fixed.NO);
      }
      List<CategoryImpl> categoriesList = new ArrayList<CategoryImpl>();
      while (reader.hasNext() && !(reader.isEndElement() && Edm.NAMESPACE_APP_2007.equals(reader.getNamespaceURI()) && FormatXml.APP_CATEGORIES.equals(reader.getLocalName()))) {
        reader.next();
        if (reader.isStartElement()) {
          currentHandledStartTagName = reader.getLocalName();
          if (FormatXml.ATOM_CATEGORY.equals(currentHandledStartTagName)) {
            categoriesList.add(parseCategory(reader));
          }
        }
      }
      categories.setCategoryList(categoriesList);
    }
    if ((href != null && fixed != null && categories.getScheme() != null) ||
        (href == null && fixed == null && categories.getScheme() == null)) {
      throw new ServiceDocumentParserException("Invalid attributes for the element categories");
    }
    return categories;
  }

  private CategoryImpl parseCategory(final XMLStreamReader reader) throws XMLStreamException {
    reader.require(XMLStreamConstants.START_ELEMENT, Edm.NAMESPACE_ATOM_2005, FormatXml.ATOM_CATEGORY);
    CategoryImpl category = new CategoryImpl();
    category.setScheme(reader.getAttributeValue(null, FormatXml.ATOM_CATEGORY_SCHEME));
    category.setTerm(reader.getAttributeValue(null, FormatXml.ATOM_CATEGORY_TERM));
    category.setLabel(reader.getAttributeValue(null, FormatXml.ATOM_CATEGORY_LABEL));
    CommonAttributesImpl attributes = parseCommonAttribute(reader);
    return category.setCommonAttributes(attributes);
  }

  private ExtensionElementImpl parseExtensionSansTitleElement(final XMLStreamReader reader) {
    ExtensionElementImpl extElement = new ExtensionElementImpl();
    if (!(Edm.NAMESPACE_APP_2007.equals(reader.getNamespaceURI())
    || (FormatXml.ATOM_TITLE.equals(reader.getLocalName()) || Edm.NAMESPACE_ATOM_2005.equals(reader.getNamespaceURI())))) {
      extElement.setName(reader.getLocalName());
      extElement.setNamespace(reader.getNamespaceURI());
      extElement.setPrefix(reader.getPrefix());
    }
    return extElement;
  }

  private ExtensionElementImpl parseExtensionElement(final XMLStreamReader reader) throws XMLStreamException, ServiceDocumentParserException {
    ExtensionElementImpl extElement = null;
    if (!Edm.NAMESPACE_APP_2007.equals(reader.getNamespaceURI())) {
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
        throw new ServiceDocumentParserException("Invalid extension element");
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

  public AtomServiceDocument parseXml(final InputStream in) throws ServiceDocumentParserException {
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
