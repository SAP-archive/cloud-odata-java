package org.odata4j.format.xml;

import java.io.Reader;

import org.odata4j.core.OError;
import org.odata4j.core.OErrors;
import org.odata4j.format.FormatParser;
import org.odata4j.stax2.QName2;
import org.odata4j.stax2.XMLEvent2;
import org.odata4j.stax2.XMLEventReader2;
import org.odata4j.stax2.util.StaxUtil;

public class AtomErrorFormatParser extends XmlFormatParser implements FormatParser<OError> {

  private static final QName2 ERROR = new QName2(NS_METADATA, "error");
  private static final QName2 CODE = new QName2(NS_METADATA, "code");
  private static final QName2 MESSAGE = new QName2(NS_METADATA, "message");
  private static final QName2 INNER_ERROR = new QName2(NS_METADATA, "innererror");

  @Override
  public OError parse(Reader reader) {
    String code = null;
    String message = null;
    String innerError = null;
    XMLEventReader2 xmlReader = StaxUtil.newXMLEventReader(reader);
    XMLEvent2 event = xmlReader.nextEvent();
    while (!event.isStartElement())
      event = xmlReader.nextEvent();
    if (!isStartElement(event, ERROR))
      throw new RuntimeException("Bad error response: <" + ERROR.getLocalPart() + "> not found");
    while (!isEndElement(event = xmlReader.nextEvent(), ERROR)) {
      if (isStartElement(event, CODE))
        code = xmlReader.getElementText();
      else if (isStartElement(event, MESSAGE))
        message = xmlReader.getElementText();
      else if (isStartElement(event, INNER_ERROR))
        innerError = StaxUtil.innerXml(event, xmlReader);
      else if (!event.isStartElement() || !event.isEndElement())
        continue;
      else
        throw new RuntimeException("Bad error response: Unexpected structure");
    }
    if (!isEndElement(event, ERROR))
      throw new RuntimeException("Bad error response: Expected </" + ERROR.getLocalPart() + ">");
    if (code == null && message == null && innerError == null)
      throw new RuntimeException("Bad error response: Unknown elements");
    return OErrors.error(code, message, innerError);
  }
}
