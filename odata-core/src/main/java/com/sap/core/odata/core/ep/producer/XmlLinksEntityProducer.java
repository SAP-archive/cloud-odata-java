package com.sap.core.odata.core.ep.producer;

import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.util.FormatXml;

/**
 * Provider for writing a collection of links
 * @author SAP AG
 */
public class XmlLinksEntityProducer {

  private final EntityProviderProperties properties;

  public XmlLinksEntityProducer(final EntityProviderProperties properties) throws EntityProviderException {
    this.properties = properties;
  }

  public void append(final XMLStreamWriter writer, final EntityInfoAggregator entityInfo, final List<Map<String, Object>> data) throws EntityProviderException {
    try {
      writer.writeStartElement(FormatXml.D_LINKS);
      writer.writeDefaultNamespace(Edm.NAMESPACE_D_2007_08);
      if (properties.getInlineCount() != null) {
        writer.writeStartElement(Edm.PREFIX_M, FormatXml.M_COUNT, Edm.NAMESPACE_M_2007_08);
        writer.writeNamespace(Edm.PREFIX_M, Edm.NAMESPACE_M_2007_08);
        writer.writeCharacters(properties.getInlineCount().toString());
        writer.writeEndElement();
      }
      XmlLinkEntityProducer provider = new XmlLinkEntityProducer(properties);
      for (final Map<String, Object> entityData : data) {
        provider.append(writer, entityInfo, entityData, false);
      }
      writer.writeEndElement();
      writer.flush();
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }
}
