package org.odata4j.format.xml;

import java.io.Writer;

import javax.ws.rs.core.UriInfo;

import org.odata4j.core.OComplexObject;
import org.odata4j.core.ODataConstants;
import org.odata4j.format.FormatWriter;
import org.odata4j.producer.ComplexObjectResponse;
import org.odata4j.stax2.QName2;
import org.odata4j.stax2.XMLFactoryProvider2;
import org.odata4j.stax2.XMLWriter2;

public class AtomComplexFormatWriter extends XmlFormatWriter implements FormatWriter<ComplexObjectResponse> {

  @Override
  public void write(UriInfo uriInfo, Writer w, ComplexObjectResponse target) {
    XMLWriter2 writer = XMLFactoryProvider2.getInstance().newXMLWriterFactory2().createXMLWriter(w);

    OComplexObject obj = target.getObject();

    writer.startDocument();

    writer.startElement(new QName2(d, target.getComplexObjectName(), "d"));

    writer.writeAttribute(new QName2(m, "type", "m"), obj.getType().getFullyQualifiedTypeName());

    writer.writeNamespace("d", d);
    writer.writeNamespace("m", m);

    this.writeProperties(writer, obj.getProperties());

    writer.endElement(target.getComplexObjectName());
    writer.endDocument();

  }

  @Override
  public String getContentType() {
    return ODataConstants.APPLICATION_XML_CHARSET_UTF8;
  }

}
