package com.sap.core.odata.core.ep.producer;

import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.core.ep.aggregator.EntityComplexPropertyInfo;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.util.FormatXml;

/**
 * Internal EntityProvider for simple and complex EDM properties which are pre-analyzed as {@link EntityPropertyInfo}.
 * @author SAP AG
 */
public class XmlPropertyEntityProducer {

  /**
   * Append {@link Object} <code>value</code> based on {@link EntityPropertyInfo} to {@link XMLStreamWriter}
   * in an already existing XML structure.
   * 
   * @param writer
   * @param name  Name of the outer XML tag
   * @param propertyInfo
   * @param value
   * @throws EntityProviderException
   */
  public void append(XMLStreamWriter writer, String name, EntityPropertyInfo propertyInfo, Object value) throws EntityProviderException {
    try {
      if (hasCustomNamespace(propertyInfo))
        writeStartElementWithCustomNamespace(writer, propertyInfo, name);
      else
        writer.writeStartElement(Edm.NAMESPACE_D_2007_08, name);

      if (propertyInfo.isComplex())
        appendProperty(writer, (EntityComplexPropertyInfo) propertyInfo, value);
      else
        appendProperty(writer, propertyInfo, value);

      writer.writeEndElement();
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  /**
   * Append {@link Object} <code>value</code> based on {@link EntityPropertyInfo} to {@link XMLStreamWriter}
   * as a stand-alone XML structure, including writing of default namespace declarations.
   * The name of the outermost XML element comes from the {@link EntityPropertyInfo}.
   * 
   * @param writer
   * @param propertyInfo
   * @param value
   * @throws EntityProviderException
   */
  public void append(XMLStreamWriter writer, EntityPropertyInfo propertyInfo, Object value) throws EntityProviderException {
    try {
      writer.writeStartElement(propertyInfo.getName());
      writer.writeDefaultNamespace(Edm.NAMESPACE_D_2007_08);
      writer.writeNamespace(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08);

      if (propertyInfo.isComplex())
        appendProperty(writer, (EntityComplexPropertyInfo) propertyInfo, value);
      else
        appendProperty(writer, propertyInfo, value);

      writer.writeEndElement();
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  /**
   * 
   * @param writer
   * @param propertyInfo
   * @param value
   * @throws XMLStreamException
   * @throws EdmException
   * @throws EntityProviderException
   */
  private void appendProperty(XMLStreamWriter writer, EntityComplexPropertyInfo propertyInfo, Object value) throws XMLStreamException, EdmException, EntityProviderException {

    if (value == null) {
      writer.writeAttribute(Edm.NAMESPACE_M_2007_08, FormatXml.ATOM_NULL, FormatXml.ATOM_VALUE_TRUE);
    } else {
      writer.writeAttribute(Edm.NAMESPACE_M_2007_08, FormatXml.ATOM_TYPE, getFqnTypeName(propertyInfo));
      List<EntityPropertyInfo> propertyInfos = propertyInfo.getPropertyInfos();
      for (EntityPropertyInfo childPropertyInfo : propertyInfos) {
        Object childValue = extractChildValue(value, childPropertyInfo.getName());
        append(writer, childPropertyInfo.getName(), childPropertyInfo, childValue);
      }
    }
  }

  /**
   * Return full qualified name of an type of an given PropertyInfo
   * @param propertyInfo
   * @return Full qualified name
   * @throws EdmException
   */
  private String getFqnTypeName(EntityComplexPropertyInfo propertyInfo) throws EdmException {
    //TODA what means Fqn
    return propertyInfo.getType().getNamespace() + Edm.DELIMITER + propertyInfo.getType().getName();
  }

  /**
   * If <code>value</code> is a {@link Map} the element with given <code>name</code> as key is returned.
   * If <code>value</code> is NOT a {@link Map} its {@link String#valueOf(Object)} result is returned.
   * 
   * @param value
   * @param name
   * @return name or result (see above)
   */
  private Object extractChildValue(Object value, String name) {
    if (value instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) value;
      return map.get(name);
    }
    return String.valueOf(value);
  }

  /**
   * Appends a simple-property value to the XML stream.
   * @param writer the XML stream writer
   * @param prop property informations
   * @param value the value of the property
   * @throws XMLStreamException
   * @throws EdmException
   */
  private void appendProperty(XMLStreamWriter writer, EntityPropertyInfo prop, Object value) throws XMLStreamException, EdmException {
    Object contentValue = value;
    String mimeType = null;
    if (prop.getMimeType() != null) {
      mimeType = prop.getMimeType();
    } else if (prop.getMapping() != null && prop.getMapping().getMimeType() != null) {
        mimeType = (String) extractChildValue(value, prop.getMapping().getMimeType());
        contentValue = extractChildValue(value, prop.getName());
    }

    if (mimeType != null)
      writer.writeAttribute(Edm.NAMESPACE_M_2007_08, FormatXml.M_MIME_TYPE, mimeType);

    final EdmSimpleType type = (EdmSimpleType) prop.getType();
    final String valueAsString = type.valueToString(contentValue, EdmLiteralKind.DEFAULT, prop.getFacets());
    if (valueAsString == null) {
      writer.writeAttribute(Edm.NAMESPACE_M_2007_08, FormatXml.ATOM_NULL, FormatXml.ATOM_VALUE_TRUE);
    } else {
      writer.writeCharacters(valueAsString);
    }
  }

  /**
   * 
   * @param writer
   * @param prop
   * @param name
   * @throws XMLStreamException
   */
  private void writeStartElementWithCustomNamespace(XMLStreamWriter writer, EntityPropertyInfo prop, String name) throws XMLStreamException {
    EdmCustomizableFeedMappings mapping = prop.getCustomMapping();
    String nsPrefix = mapping.getFcNsPrefix();
    String nsUri = mapping.getFcNsUri();
    writer.writeStartElement(nsPrefix, name, nsUri);
    writer.writeNamespace(nsPrefix, nsUri);
  }

  /**
   * 
   * @param prop
   * @return
   */
  private boolean hasCustomNamespace(EntityPropertyInfo prop) {
    if (prop.getCustomMapping() != null) {
      EdmCustomizableFeedMappings mapping = prop.getCustomMapping();
      return !(mapping.getFcNsPrefix() == null || mapping.getFcNsUri() == null);
    }
    return false;
  }

}
