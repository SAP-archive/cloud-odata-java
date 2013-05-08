package com.sap.core.odata.core.ep.producer;

import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.util.FormatXml;

/**
 * Provider for writing a single link.
 * @author SAP AG
 */
public class XmlLinkEntityProducer {

  private final EntityProviderWriteProperties properties;

  public XmlLinkEntityProducer(final EntityProviderWriteProperties properties) throws EntityProviderException {
    this.properties = properties == null ? EntityProviderWriteProperties.serviceRoot(null).build() : properties;
  }

  public void append(final XMLStreamWriter writer, final EntityInfoAggregator entityInfo, final Map<String, Object> data, final boolean isRootElement) throws EntityProviderException {
    try {
      writer.writeStartElement(FormatXml.D_URI);
      if (isRootElement)
        writer.writeDefaultNamespace(Edm.NAMESPACE_D_2007_08);
      if (properties.getServiceRoot() != null)
        writer.writeCharacters(properties.getServiceRoot().toASCIIString());
      writer.writeCharacters(AtomEntryEntityProducer.createSelfLink(entityInfo, data, null));
      writer.writeEndElement();
      writer.flush();
    } catch (final XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }
}
