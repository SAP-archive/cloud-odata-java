package org.odata4j.format.xml;

import org.core4j.Enumerable;
import org.odata4j.stax2.Attribute2;
import org.odata4j.stax2.QName2;
import org.odata4j.stax2.StartElement2;
import org.odata4j.stax2.XMLEvent2;

public class XmlFormatParser {

  public static final String NS_APP = "http://www.w3.org/2007/app";
  public static final String NS_XML = "http://www.w3.org/XML/1998/namespace";
  public static final String NS_ATOM = "http://www.w3.org/2005/Atom";

  public static final String NS_METADATA = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata";
  public static final String NS_DATASERVICES = "http://schemas.microsoft.com/ado/2007/08/dataservices";
  public static final String NS_EDM2006 = "http://schemas.microsoft.com/ado/2006/04/edm"; // edm 1.0
  public static final String NS_EDM2007 = "http://schemas.microsoft.com/ado/2007/05/edm"; // edm 1.1
  public static final String NS_EDM2008_1 = "http://schemas.microsoft.com/ado/2008/01/edm"; // edm 1.2
  public static final String NS_EDM2008_9 = "http://schemas.microsoft.com/ado/2008/09/edm"; // edm 2.0
  public static final String NS_EDM2009 = "http://schemas.microsoft.com/ado/2009/08/edm"; // edm ???
  public static final String NS_EDMX = "http://schemas.microsoft.com/ado/2007/06/edmx";
  public static final String NS_EDMANNOTATION = "http://schemas.microsoft.com/ado/2009/02/edm/annotation";

  public static final QName2 EDMX_EDMX = new QName2(NS_EDMX, "Edmx");
  public static final QName2 EDMX_DATASERVICES = new QName2(NS_EDMX, "DataServices");

  public static final QName2 EDM2006_SCHEMA = new QName2(NS_EDM2006, "Schema");
  public static final QName2 EDM2006_ENTITYTYPE = new QName2(NS_EDM2006, "EntityType");
  public static final QName2 EDM2006_ASSOCIATION = new QName2(NS_EDM2006, "Association");
  public static final QName2 EDM2006_COMPLEXTYPE = new QName2(NS_EDM2006, "ComplexType");
  public static final QName2 EDM2006_ENTITYCONTAINER = new QName2(NS_EDM2006, "EntityContainer");
  public static final QName2 EDM2006_ENTITYSET = new QName2(NS_EDM2006, "EntitySet");
  public static final QName2 EDM2006_ASSOCIATIONSET = new QName2(NS_EDM2006, "AssociationSet");
  public static final QName2 EDM2006_FUNCTIONIMPORT = new QName2(NS_EDM2006, "FunctionImport");
  public static final QName2 EDM2006_PARAMETER = new QName2(NS_EDM2006, "Parameter");
  public static final QName2 EDM2006_END = new QName2(NS_EDM2006, "End");
  public static final QName2 EDM2006_ONDELETE = new QName2(NS_EDM2006, "OnDelete");
  public static final QName2 EDM2006_REFCONSTRAINT = new QName2(NS_EDM2006, "ReferentialConstraint");
  public static final QName2 EDM2006_PRINCIPAL = new QName2(NS_EDM2006, "Principal");
  public static final QName2 EDM2006_DEPENDENT = new QName2(NS_EDM2006, "Dependent");
  public static final QName2 EDM2006_PROPERTYREF = new QName2(NS_EDM2006, "PropertyRef");
  public static final QName2 EDM2006_PROPERTY = new QName2(NS_EDM2006, "Property");
  public static final QName2 EDM2006_NAVIGATIONPROPERTY = new QName2(NS_EDM2006, "NavigationProperty");

  public static final QName2 EDM2007_SCHEMA = new QName2(NS_EDM2007, "Schema");
  public static final QName2 EDM2007_ENTITYTYPE = new QName2(NS_EDM2007, "EntityType");
  public static final QName2 EDM2007_ASSOCIATION = new QName2(NS_EDM2007, "Association");
  public static final QName2 EDM2007_COMPLEXTYPE = new QName2(NS_EDM2007, "ComplexType");
  public static final QName2 EDM2007_ENTITYCONTAINER = new QName2(NS_EDM2007, "EntityContainer");
  public static final QName2 EDM2007_ENTITYSET = new QName2(NS_EDM2007, "EntitySet");
  public static final QName2 EDM2007_ASSOCIATIONSET = new QName2(NS_EDM2007, "AssociationSet");
  public static final QName2 EDM2007_FUNCTIONIMPORT = new QName2(NS_EDM2007, "FunctionImport");
  public static final QName2 EDM2007_PARAMETER = new QName2(NS_EDM2007, "Parameter");
  public static final QName2 EDM2007_END = new QName2(NS_EDM2007, "End");
  public static final QName2 EDM2007_ONDELETE = new QName2(NS_EDM2007, "OnDelete");
  public static final QName2 EDM2007_REFCONSTRAINT = new QName2(NS_EDM2007, "ReferentialConstraint");
  public static final QName2 EDM2007_PRINCIPAL = new QName2(NS_EDM2007, "Principal");
  public static final QName2 EDM2007_DEPENDENT = new QName2(NS_EDM2007, "Dependent");
  public static final QName2 EDM2007_PROPERTYREF = new QName2(NS_EDM2007, "PropertyRef");
  public static final QName2 EDM2007_PROPERTY = new QName2(NS_EDM2007, "Property");
  public static final QName2 EDM2007_NAVIGATIONPROPERTY = new QName2(NS_EDM2007, "NavigationProperty");

  public static final QName2 EDM2008_1_SCHEMA = new QName2(NS_EDM2008_1, "Schema");
  public static final QName2 EDM2008_1_ENTITYTYPE = new QName2(NS_EDM2008_1, "EntityType");
  public static final QName2 EDM2008_1_ASSOCIATION = new QName2(NS_EDM2008_1, "Association");
  public static final QName2 EDM2008_1_COMPLEXTYPE = new QName2(NS_EDM2008_1, "ComplexType");
  public static final QName2 EDM2008_1_ENTITYCONTAINER = new QName2(NS_EDM2008_1, "EntityContainer");
  public static final QName2 EDM2008_1_ENTITYSET = new QName2(NS_EDM2008_1, "EntitySet");
  public static final QName2 EDM2008_1_ASSOCIATIONSET = new QName2(NS_EDM2008_1, "AssociationSet");
  public static final QName2 EDM2008_1_FUNCTIONIMPORT = new QName2(NS_EDM2008_1, "FunctionImport");
  public static final QName2 EDM2008_1_PARAMETER = new QName2(NS_EDM2008_1, "Parameter");
  public static final QName2 EDM2008_1_END = new QName2(NS_EDM2008_1, "End");
  public static final QName2 EDM2008_1_ONDELETE = new QName2(NS_EDM2008_1, "OnDelete");
  public static final QName2 EDM2008_1_REFCONSTRAINT = new QName2(NS_EDM2008_1, "ReferentialConstraint");
  public static final QName2 EDM2008_1_PRINCIPAL = new QName2(NS_EDM2008_1, "Principal");
  public static final QName2 EDM2008_1_DEPENDENT = new QName2(NS_EDM2008_1, "Dependent");
  public static final QName2 EDM2008_1_PROPERTYREF = new QName2(NS_EDM2008_1, "PropertyRef");
  public static final QName2 EDM2008_1_PROPERTY = new QName2(NS_EDM2008_1, "Property");
  public static final QName2 EDM2008_1_NAVIGATIONPROPERTY = new QName2(NS_EDM2008_1, "NavigationProperty");

  public static final QName2 EDM2008_9_SCHEMA = new QName2(NS_EDM2008_9, "Schema");
  public static final QName2 EDM2008_9_ENTITYTYPE = new QName2(NS_EDM2008_9, "EntityType");
  public static final QName2 EDM2008_9_ASSOCIATION = new QName2(NS_EDM2008_9, "Association");
  public static final QName2 EDM2008_9_COMPLEXTYPE = new QName2(NS_EDM2008_9, "ComplexType");
  public static final QName2 EDM2008_9_ENTITYCONTAINER = new QName2(NS_EDM2008_9, "EntityContainer");
  public static final QName2 EDM2008_9_ENTITYSET = new QName2(NS_EDM2008_9, "EntitySet");
  public static final QName2 EDM2008_9_ASSOCIATIONSET = new QName2(NS_EDM2008_9, "AssociationSet");
  public static final QName2 EDM2008_9_FUNCTIONIMPORT = new QName2(NS_EDM2008_9, "FunctionImport");
  public static final QName2 EDM2008_9_PARAMETER = new QName2(NS_EDM2008_9, "Parameter");
  public static final QName2 EDM2008_9_END = new QName2(NS_EDM2008_9, "End");
  public static final QName2 EDM2008_9_ONDELETE = new QName2(NS_EDM2008_9, "OnDelete");
  public static final QName2 EDM2008_9_REFCONSTRAINT = new QName2(NS_EDM2008_9, "ReferentialConstraint");
  public static final QName2 EDM2008_9_PRINCIPAL = new QName2(NS_EDM2008_9, "Principal");
  public static final QName2 EDM2008_9_DEPENDENT = new QName2(NS_EDM2008_9, "Dependent");
  public static final QName2 EDM2008_9_PROPERTYREF = new QName2(NS_EDM2008_9, "PropertyRef");
  public static final QName2 EDM2008_9_PROPERTY = new QName2(NS_EDM2008_9, "Property");
  public static final QName2 EDM2008_9_NAVIGATIONPROPERTY = new QName2(NS_EDM2008_9, "NavigationProperty");

  public static final QName2 EDM2009_SCHEMA = new QName2(NS_EDM2009, "Schema");
  public static final QName2 EDM2009_ENTITYTYPE = new QName2(NS_EDM2009, "EntityType");
  public static final QName2 EDM2009_ASSOCIATION = new QName2(NS_EDM2009, "Association");
  public static final QName2 EDM2009_COMPLEXTYPE = new QName2(NS_EDM2009, "ComplexType");
  public static final QName2 EDM2009_ENTITYCONTAINER = new QName2(NS_EDM2009, "EntityContainer");
  public static final QName2 EDM2009_ENTITYSET = new QName2(NS_EDM2009, "EntitySet");
  public static final QName2 EDM2009_ASSOCIATIONSET = new QName2(NS_EDM2009, "AssociationSet");
  public static final QName2 EDM2009_FUNCTIONIMPORT = new QName2(NS_EDM2009, "FunctionImport");
  public static final QName2 EDM2009_PARAMETER = new QName2(NS_EDM2009, "Parameter");
  public static final QName2 EDM2009_END = new QName2(NS_EDM2009, "End");
  public static final QName2 EDM2009_ONDELETE = new QName2(NS_EDM2009, "OnDelete");
  public static final QName2 EDM2009_REFCONSTRAINT = new QName2(NS_EDM2009, "ReferentialConstraint");
  public static final QName2 EDM2009_PRINCIPAL = new QName2(NS_EDM2009, "Principal");
  public static final QName2 EDM2009_DEPENDENT = new QName2(NS_EDM2009, "Dependent");
  public static final QName2 EDM2009_PROPERTYREF = new QName2(NS_EDM2009, "PropertyRef");
  public static final QName2 EDM2009_PROPERTY = new QName2(NS_EDM2009, "Property");
  public static final QName2 EDM2009_NAVIGATIONPROPERTY = new QName2(NS_EDM2009, "NavigationProperty");

  public static final QName2 ATOM_FEED = new QName2(NS_ATOM, "feed");
  public static final QName2 ATOM_ENTRY = new QName2(NS_ATOM, "entry");
  public static final QName2 ATOM_ID = new QName2(NS_ATOM, "id");
  public static final QName2 ATOM_TITLE = new QName2(NS_ATOM, "title");
  public static final QName2 ATOM_SUMMARY = new QName2(NS_ATOM, "summary");
  public static final QName2 ATOM_UPDATED = new QName2(NS_ATOM, "updated");
  public static final QName2 ATOM_CATEGORY = new QName2(NS_ATOM, "category");
  public static final QName2 ATOM_CONTENT = new QName2(NS_ATOM, "content");
  public static final QName2 ATOM_LINK = new QName2(NS_ATOM, "link");

  public static final QName2 APP_WORKSPACE = new QName2(NS_APP, "workspace");
  public static final QName2 APP_SERVICE = new QName2(NS_APP, "service");
  public static final QName2 APP_COLLECTION = new QName2(NS_APP, "collection");
  public static final QName2 APP_ACCEPT = new QName2(NS_APP, "accept");

  public static final QName2 M_ETAG = new QName2(NS_METADATA, "etag");
  public static final QName2 M_PROPERTIES = new QName2(NS_METADATA, "properties");
  public static final QName2 M_TYPE = new QName2(NS_METADATA, "type");
  public static final QName2 M_NULL = new QName2(NS_METADATA, "null");
  public static final QName2 M_INLINE = new QName2(NS_METADATA, "inline");
  public static final QName2 M_MIMETYPE = new QName2(NS_METADATA, "MimeType");
  public static final QName2 M_FC_TARGETPATH = new QName2(NS_METADATA, "FC_TargetPath");
  public static final QName2 M_FC_CONTENTKIND = new QName2(NS_METADATA, "FC_ContentKind");
  public static final QName2 M_FC_KEEPINCONTENT = new QName2(NS_METADATA, "FC_KeepInContent");
  public static final QName2 M_FC_EPMCONTENTKIND = new QName2(NS_METADATA, "FC_EpmContentKind");
  public static final QName2 M_FC_EPMKEEPINCONTENT = new QName2(NS_METADATA, "FC_EpmKeepInContent");
  public static final QName2 M_FC_NSPREFIX = new QName2(NS_METADATA, "FC_NsPrefix");
  public static final QName2 M_FC_NSURI = new QName2(NS_METADATA, "FC_NsUri");

  public static final QName2 DATASERVICES_ELEMENT = new QName2(NS_DATASERVICES, "element"); // a collection element

  public static final QName2 XML_BASE = new QName2(NS_XML, "base");

  protected static boolean isStartElement(XMLEvent2 event, QName2... names) {
    if (!event.isStartElement()) {
      return false;
    }
    QName2 name = new QName2(event.asStartElement().getName().getNamespaceUri(), event.asStartElement().getName().getLocalPart());
    return Enumerable.create(names).contains(name);

  }

  protected static boolean isElement(XMLEvent2 event, QName2... names) {
    QName2 name = new QName2(event.asStartElement().getName().getNamespaceUri(), event.asStartElement().getName().getLocalPart());
    return Enumerable.create(names).contains(name);

  }

  protected static boolean isEndElement(XMLEvent2 event, QName2 qname) {
    if (!event.isEndElement()) {
      return false;
    }
    QName2 name = event.asEndElement().getName();
    return name.getNamespaceUri().equals(qname.getNamespaceUri())
        && name.getLocalPart().equals(qname.getLocalPart());
  }

  protected static String urlCombine(String base, String rel) {
    if (!base.endsWith("/") && !rel.startsWith("/"))
      base = base + "/";
    return base + rel;
  }

  protected static String getAttributeValueIfExists(StartElement2 element, String localName) {
    return getAttributeValueIfExists(element, new QName2(null, localName));
  }

  protected static String getAttributeValueIfExists(StartElement2 element, QName2 attName) {
    Attribute2 rt = element.getAttributeByName(attName);
    return rt == null ? null : rt.getValue();
  }

}
