package com.sap.core.odata.core.ep;

import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.ep.ODataSerializationException;

public interface ODataXmlSerializer {

  void appendTo(XMLStreamWriter writer) throws ODataSerializationException;
}
