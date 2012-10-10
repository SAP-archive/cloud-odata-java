package org.odata4j.format.xml;

import java.io.Writer;

import javax.ws.rs.core.UriInfo;

import org.odata4j.core.ODataConstants;
import org.odata4j.format.FormatWriter;
import org.odata4j.producer.SimpleResponse;
import org.odata4j.stax2.XMLFactoryProvider2;
import org.odata4j.stax2.XMLWriter2;

public class AtomSimpleFormatWriter extends XmlFormatWriter implements FormatWriter<SimpleResponse> {

  @Override
  public void write(UriInfo uriInfo, Writer w, SimpleResponse target) {
    XMLWriter2 writer = XMLFactoryProvider2.getInstance().newXMLWriterFactory2().createXMLWriter(w);

    writer.startDocument();
    this.writeProperty(writer, target.getName(), target.getType(), target.getValue(), true, true);
    writer.endDocument();

  }

  @Override
  public String getContentType() {
    return ODataConstants.APPLICATION_XML_CHARSET_UTF8;
  }

}
