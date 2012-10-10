package org.odata4j.format.xml;

import java.io.Writer;

import javax.ws.rs.core.UriInfo;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.odata4j.core.ODataConstants;
import org.odata4j.core.OEntity;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.format.Entry;
import org.odata4j.format.FormatWriter;
import org.odata4j.internal.InternalUtil;
import org.odata4j.producer.EntityResponse;
import org.odata4j.stax2.QName2;
import org.odata4j.stax2.XMLFactoryProvider2;
import org.odata4j.stax2.XMLWriter2;

public class AtomEntryFormatWriter extends XmlFormatWriter implements FormatWriter<EntityResponse> {

  protected String baseUri;

  public void writeRequestEntry(Writer w, Entry entry) {

    DateTime utc = new DateTime().withZone(DateTimeZone.UTC);
    String updated = InternalUtil.toString(utc);

    XMLWriter2 writer = XMLFactoryProvider2.getInstance().newXMLWriterFactory2().createXMLWriter(w);
    writer.startDocument();

    writer.startElement(new QName2("entry"), atom);
    writer.writeNamespace("d", d);
    writer.writeNamespace("m", m);

    OEntity entity = entry.getEntity();
    writeEntry(writer, null, entity.getProperties(), entity.getLinks(),
        null, updated, entity.getEntitySet(), false);
    writer.endDocument();

  }

  @Override
  public String getContentType() {
    return ODataConstants.APPLICATION_ATOM_XML_CHARSET_UTF8;
  }

  @Override
  public void write(UriInfo uriInfo, Writer w, EntityResponse target) {
    String baseUri = uriInfo.getBaseUri().toString();
    EdmEntitySet ees = target.getEntity().getEntitySet();

    DateTime utc = new DateTime().withZone(DateTimeZone.UTC);
    String updated = InternalUtil.toString(utc);

    XMLWriter2 writer = XMLFactoryProvider2.getInstance().newXMLWriterFactory2().createXMLWriter(w);
    writer.startDocument();

    writer.startElement(new QName2("entry"), atom);
    writer.writeNamespace("m", m);
    writer.writeNamespace("d", d);
    writer.writeAttribute("xml:base", baseUri);

    writeEntry(writer, target.getEntity(), target.getEntity().getProperties(), target.getEntity().getLinks(), baseUri, updated, ees, true);
    writer.endDocument();
  }

}
