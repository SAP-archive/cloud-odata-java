/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
    this.properties = properties;
  }

  public void append(final XMLStreamWriter writer, final EntityInfoAggregator entityInfo, final Map<String, Object> data, final boolean isRootElement) throws EntityProviderException {
    try {
      writer.writeStartElement(FormatXml.D_URI);
      if (isRootElement) {
        writer.writeDefaultNamespace(Edm.NAMESPACE_D_2007_08);
      }
      writer.writeCharacters(properties.getServiceRoot().toASCIIString());
      writer.writeCharacters(AtomEntryEntityProducer.createSelfLink(entityInfo, data, null));
      writer.writeEndElement();
      writer.flush();
    } catch (final XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }
}
