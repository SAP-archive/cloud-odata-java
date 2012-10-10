package org.odata4j.format.xml;

import java.io.Writer;

import javax.ws.rs.core.UriInfo;

import org.odata4j.core.OCollection;
import org.odata4j.core.ODataConstants;
import org.odata4j.format.FormatWriter;
import org.odata4j.producer.CollectionResponse;
import org.odata4j.stax2.QName2;
import org.odata4j.stax2.XMLFactoryProvider2;
import org.odata4j.stax2.XMLWriter2;

public class AtomCollectionFormatWriter extends XmlFormatWriter implements FormatWriter<CollectionResponse<?>> {

  @Override
  public void write(UriInfo uriInfo, Writer w, CollectionResponse<?> target) {
    XMLWriter2 writer = XMLFactoryProvider2.getInstance().newXMLWriterFactory2().createXMLWriter(w);

    writer.startDocument();

    writer.startElement(new QName2(d, target.getCollectionName(), "d"));
    writer.writeNamespace("d", d);
    writer.writeNamespace("m", m);

    OCollection<?> coll = target.getCollection();
    for (Object e : coll) {
      this.writeProperty(writer, "element", coll.getType(), e, false, !coll.getType().isSimple());
    }

    writer.endElement(target.getCollectionName());
    writer.endDocument();

  }

  @Override
  public String getContentType() {
    return ODataConstants.APPLICATION_XML_CHARSET_UTF8;
  }

}
