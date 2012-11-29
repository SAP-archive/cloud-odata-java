package com.sap.core.odata.core.serializer;

import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.serialization.ODataSerializationException;

public interface ODataXmlSerializer {

  void appendTo(XMLStreamWriter writer) throws ODataSerializationException;
}
