package com.sap.core.odata.core.ep.producer;

import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.core.commons.Encoder;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.util.FormatXml;

/**
 * Provider for writing a link
 * @author SAP AG
 */
public class XmlLinkEntityProducer {

  private final EntityProviderProperties properties;

  public XmlLinkEntityProducer(final EntityProviderProperties properties) throws EntityProviderException {
    this.properties = properties;
  }

  public void append(XMLStreamWriter writer, final EntityInfoAggregator entityInfo, final Map<String, Object> data, final boolean isRootElement) throws EntityProviderException {
    try {
      writer.writeStartElement(FormatXml.D_URI);
      if (isRootElement) {
        writer.writeDefaultNamespace(Edm.NAMESPACE_D_2007_08);
      }
      writer.writeCharacters(createAbsoluteUri(entityInfo, data));
      writer.writeEndElement();
      writer.flush();
    } catch (final XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  private String createAbsoluteUri(final EntityInfoAggregator entityInfo, final Map<String, Object> data) throws EntityProviderException {
    return properties.getServiceRoot() +
        (entityInfo.isDefaultEntityContainer() ? "" : Encoder.encode(entityInfo.getEntityContainerName()) + Edm.DELIMITER)
        + Encoder.encode(entityInfo.getEntitySetName())
        + "(" + createEntryKey(entityInfo, data) + ")";

  }

  private static String createEntryKey(final EntityInfoAggregator entityInfo, final Map<String, Object> data) throws EntityProviderException {
    final List<EntityPropertyInfo> keyPropertyInfos = entityInfo.getKeyPropertyInfos();
    String keys = "";

    for (final EntityPropertyInfo keyPropertyInfo : keyPropertyInfos) {
      if (!keys.isEmpty())
        keys += ",";

      if (keyPropertyInfos.size() > 1)
        keys += Encoder.encode(keyPropertyInfo.getName()) + "=";

      final EdmSimpleType type = (EdmSimpleType) keyPropertyInfo.getType();
      try {
        keys += Encoder.encode(type.valueToString(data.get(keyPropertyInfo.getName()), EdmLiteralKind.URI, keyPropertyInfo.getFacets()));
      } catch (final EdmSimpleTypeException e) {
        throw new EntityProviderException(EntityProviderException.COMMON, e);
      }
    }

    return keys;
  }
}
