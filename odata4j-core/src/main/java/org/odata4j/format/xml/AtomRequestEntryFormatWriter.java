package org.odata4j.format.xml;

import java.io.Writer;

import javax.ws.rs.core.UriInfo;

import org.odata4j.core.ODataConstants;
import org.odata4j.format.Entry;
import org.odata4j.format.FormatWriter;

public class AtomRequestEntryFormatWriter implements FormatWriter<Entry> {

  @Override
  public void write(UriInfo uriInfo, Writer w, Entry target) {
    new AtomEntryFormatWriter().writeRequestEntry(w, target);
  }

  @Override
  public String getContentType() {
    return ODataConstants.APPLICATION_ATOM_XML_CHARSET_UTF8;
  }

}
