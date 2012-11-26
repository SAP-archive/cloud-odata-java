package com.sap.core.odata.core.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.api.serialization.ODataSerializer;

public class AtomEntrySerializer extends ODataSerializer {

  public static final String NS_DATASERVICES = "http://schemas.microsoft.com/ado/2007/08/dataservices";
  public static final String NS_DATASERVICES_METADATA = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata";
  public static final String NS_ATOM = "http://www.w3.org/2005/Atom";
  public static final String NS_XML = "http://www.w3.org/XML/1998/namespace";

  @Override
  public InputStream serialize() throws ODataSerializationException {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(out, "utf-8");

      writer.writeStartElement("entry");
      writer.writeDefaultNamespace(NS_ATOM);
      writer.writeNamespace("m", NS_DATASERVICES_METADATA);
      writer.writeNamespace("d", NS_DATASERVICES);
      writer.writeAttribute(NS_XML, "base", this.getContext().getUriInfo().getBaseUri());

      writer.writeStartElement("id");
      writer.writeCharacters(this.createIdUri());
      writer.writeEndElement();
      //      
      //      writer.writeStartElement("title");
      //      writer.writeAttribute("type", "text");
      //      writer.writeEndElement();

      writer.writeEndElement();

      writer.flush();

      return new ByteArrayInputStream(out.toByteArray());
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }

  private String createIdUri() throws ODataSerializationException {
    try {
      ODataContext ctx = this.getContext();
      Map<String, Object> data = this.getData();

      EdmEntitySet es = this.getEdmEntitySet();
      EdmEntityContainer ec = es.getEntityContainer();
      List<EdmProperty> kp = es.getEntityType().getKeyProperties();
      
      String id = ctx.getUriInfo().getBaseUri();
      if (!ec.isDefaultEntityContainer()) {
        id = id + ec.getName() + ".";
      }
      String keys = "";
      if (kp.size() == 1) {
        EdmSimpleType st = (EdmSimpleType) kp.get(0).getType();
        Object value = data.get(kp.get(0).getName());
        String strValue =  st.valueToString(value, EdmLiteralKind.URI, kp.get(0).getFacets());
        keys = keys + strValue;
        
        // TODO Introduce a URI encoder
        URI uri = new URI("x", "bbb", "/x xx", null);
        System.out.println(uri.getPath());
        System.out.println(uri.toASCIIString());
        
      }
      else {
        int size = kp.size();
        for (int i = 0; i < size; i++) {
          EdmProperty keyp = kp.get(i);
          Object value = data.get(keyp.getName());

          EdmSimpleType st = (EdmSimpleType) kp.get(i).getType();
          keys = keys + keyp.getName() + "=";
          String strValue =  st.valueToString(value, EdmLiteralKind.URI, kp.get(i).getFacets());
          keys = keys + strValue;
          if (i < size - 1) {
            keys = keys + ",";
          }
        }
      }
      id = id + es.getName() + "(" + keys + ")";
      return id;
    } catch (Exception e) {
      throw new ODataSerializationException(ODataSerializationException.COMMON, e);
    }
  }
}
