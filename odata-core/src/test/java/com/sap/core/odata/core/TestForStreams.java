package com.sap.core.odata.core;

import java.io.OutputStreamWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.junit.Test;

import com.sap.core.odata.core.ep.util.CircleStreamBuffer;
import com.sap.core.odata.testutil.helper.StringHelper;

public class TestForStreams {

  @Test
  public void test() throws Exception {
    OutputStreamWriter writer = null;
    CircleStreamBuffer csb = new CircleStreamBuffer();

    writer = new OutputStreamWriter(csb.getOutputStream(), "UTF-8");

    XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);
    
    
    xmlStreamWriter.writeStartDocument();
    xmlStreamWriter.writeStartElement("Schema");
    xmlStreamWriter.writeCharacters("Hallo");
    xmlStreamWriter.writeEndElement();
    xmlStreamWriter.flush();
    writer.write("<xmlData>Peter</xmlData>");
    xmlStreamWriter.writeEndDocument();
    
    
    xmlStreamWriter.close();
    
    
    
    String output = StringHelper.inputStreamToString(csb.getInputStream());
    
    System.out.println(output);

  }
}
