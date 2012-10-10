package org.odata4j.internal;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AndroidCompat {

  public static String getTextContent(Element element) {
    StringBuilder buffer = new StringBuilder();
    NodeList childList = element.getChildNodes();
    for (int i = 0; i < childList.getLength(); i++) {
      Node child = childList.item(i);
      if (child.getNodeType() == Node.TEXT_NODE)
        buffer.append(child.getNodeValue());
    }
    return buffer.toString();
  }

  public static boolean String_isEmpty(String trim) {
    // String#isEmpty api 9
    return trim.length() == 0;
  }

  public static DecimalFormatSymbols DecimalFormatSymbols_getInstance(Locale locale) {
    // DecimalFormatSymbols#getInstance api 9
    return new DecimalFormatSymbols(locale);
  }
}
