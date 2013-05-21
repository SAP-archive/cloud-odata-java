package com.sap.core.odata.core.svc.parser;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.junit.Test;

import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.ep.EntityProviderException;

public class ServiceDocParserTest {
  private final static String SERVICE_DOC =
      "<service xml:base=\"https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/\" xmlns=\"http://www.w3.org/2007/app\" xmlns:atom=\"http://www.w3.org/2005/Atom\" >" +
          "<workspace><atom:title>Default</atom:title>" +
          "<collection href=\"Employees\"><atom:title>Employees</atom:title></collection>" +
          "<collection href=\"Teams\"><atom:title>Teams</atom:title></collection>" +
          "<collection href=\"Rooms\"><atom:title>Rooms</atom:title></collection>" +
          "<collection href=\"Managers\"><atom:title>Managers</atom:title></collection>" +
          "<collection href=\"Buildings\"><atom:title>Buildings</atom:title></collection>" +
          "<collection href=\"Container2.Photos\"><atom:title>Photos</atom:title></collection></workspace></service>";

  @Test
  public void test() throws EntityProviderException {
    List<EntitySet> entitySets = new ServiceDocParser().readServiceDokument(createStreamReader(SERVICE_DOC));
    assertEquals(6, entitySets.size());
  }

  private XMLStreamReader createStreamReader(final String xml) throws EntityProviderException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
    XMLStreamReader streamReader;
    try {
      streamReader = factory.createXMLStreamReader(new StringReader(xml));
    } catch (XMLStreamException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }

    return streamReader;
  }
}
