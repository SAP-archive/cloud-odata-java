package org.odata4j.stax2;

import java.io.Writer;

public interface XMLOutputFactory2 {

  XMLEventWriter2 createXMLEventWriter(Writer writer);

}
