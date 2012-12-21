package com.sap.core.odata.core.ep;

import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.ep.ODataEntityProviderException;
import com.sap.core.odata.api.ep.ODataEntityProviderProperties;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;

/**
 * Provider for writing a collection of links
 * @author SAP AG
 */
public class XmlLinksEntityProvider {

  private final ODataEntityProviderProperties properties;

  XmlLinksEntityProvider(final ODataEntityProviderProperties properties) throws ODataEntityProviderException {
    this.properties = properties;
  }

  public void append(XMLStreamWriter writer, final EntityInfoAggregator entityInfo, final List<Map<String, Object>> data) throws ODataEntityProviderException {
    try {
      writer.writeStartElement(FormatXml.D_LINKS);
      writer.writeDefaultNamespace(Edm.NAMESPACE_D_2007_08);
      if (properties.getInlineCount() != null) {
        writer.writeStartElement("m", FormatXml.M_COUNT, Edm.NAMESPACE_M_2007_08);
        writer.writeNamespace("m", Edm.NAMESPACE_M_2007_08);
        writer.writeCharacters(properties.getInlineCount().toString());
        writer.writeEndElement();
      }
      XmlLinkEntityProvider provider = new XmlLinkEntityProvider(properties);
      for (final Map<String, Object> entityData : data)
        provider.append(writer, entityInfo, entityData, false);
      writer.writeEndElement();
      writer.flush();
    } catch (XMLStreamException e) {
      throw new ODataEntityProviderException(ODataEntityProviderException.COMMON, e);
    }
  }
}
