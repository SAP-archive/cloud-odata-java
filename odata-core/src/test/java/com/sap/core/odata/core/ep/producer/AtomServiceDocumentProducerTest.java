package com.sap.core.odata.core.ep.producer;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.edm.provider.EdmServiceMetadataImplProv;
import com.sap.core.odata.core.ep.AbstractXmlProducerTestHelper;
import com.sap.core.odata.core.ep.AtomEntityProvider;
import com.sap.core.odata.testutil.helper.StringHelper;

public class AtomServiceDocumentProducerTest extends AbstractXmlProducerTestHelper {

  private Edm edm;
  private List<Schema> schemas;


  public AtomServiceDocumentProducerTest(final StreamWriterImplType type) {
    super(type);
  }

  @Before
  public void before() throws ODataException {
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("atom", Edm.NAMESPACE_ATOM_2005);
    prefixMap.put("a", Edm.NAMESPACE_APP_2007);
    prefixMap.put("xml", Edm.NAMESPACE_XML_1998);
    prefixMap.put("custom", "http://localhost");
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));

    schemas = new ArrayList<Schema>();

    EdmProvider edmProvider = mock(EdmProvider.class);
    when(edmProvider.getSchemas()).thenReturn(schemas);

    EdmServiceMetadata edmServiceMetadata = new EdmServiceMetadataImplProv(edmProvider);

    edm = mock(Edm.class);
    when(edm.getServiceMetadata()).thenReturn(edmServiceMetadata);
  }

  @Test
  public void writeEmptyServiceDocumentOverRuntimeDelegate() throws Exception {
    ODataResponse response = EntityProvider.writeServiceDocument(HttpContentType.APPLICATION_ATOM_XML, edm, "http://localhost");
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:service", xmlString);
    assertXpathExists("/a:service/a:workspace", xmlString);
    assertXpathExists("/a:service/a:workspace/atom:title", xmlString);
    assertXpathEvaluatesTo("Default", "/a:service/a:workspace/atom:title", xmlString);
  }

  @Test
  public void writeEmptyServiceDocument() throws Exception {
    ODataResponse response = new AtomEntityProvider().writeServiceDocument(edm, "http://localhost");
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:service", xmlString);
    assertXpathExists("/a:service/a:workspace", xmlString);
    assertXpathExists("/a:service/a:workspace/atom:title", xmlString);
    assertXpathEvaluatesTo("Default", "/a:service/a:workspace/atom:title", xmlString);
  }

  @Test
  public void writeServiceDocumentWithOneEnitySetOneContainerOneSchema() throws Exception {
    List<EntitySet> entitySets = new ArrayList<EntitySet>();
    entitySets.add(new EntitySet().setName("Employees"));

    List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
    entityContainers.add(new EntityContainer().setDefaultEntityContainer(true).setName("Container").setEntitySets(entitySets));
       
    schemas.add(new Schema().setEntityContainers(entityContainers));
    
    ODataResponse response = new AtomEntityProvider().writeServiceDocument(edm, "http://localhost");
    String xmlString = verifyResponse(response);
    assertXpathExists("/a:service/a:workspace/a:collection[@href='Employees']", xmlString);
    assertXpathExists("/a:service/a:workspace/a:collection[@href='Employees']/atom:title", xmlString);
    assertXpathEvaluatesTo("Employees", "/a:service/a:workspace/a:collection[@href='Employees']/atom:title", xmlString);
  }

  @Test
  public void writeServiceDocumentWithOneEnitySetTwoContainersOneSchema() throws Exception {
    List<EntitySet> entitySets = new ArrayList<EntitySet>();
    entitySets.add(new EntitySet().setName("Employees"));

    List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
    entityContainers.add(new EntityContainer().setDefaultEntityContainer(true).setName("Container").setEntitySets(entitySets));
    entityContainers.add(new EntityContainer().setDefaultEntityContainer(false).setName("Container2").setEntitySets(entitySets));
    
    schemas.add(new Schema().setEntityContainers(entityContainers));
    
    ODataResponse response = new AtomEntityProvider().writeServiceDocument(edm, "http://localhost");
    String xmlString = verifyResponse(response);
    assertXpathExists("/a:service/a:workspace/a:collection[@href='Employees']", xmlString);
    assertXpathExists("/a:service/a:workspace/a:collection[@href='Employees']/atom:title", xmlString);
    assertXpathEvaluatesTo("Employees", "/a:service/a:workspace/a:collection[@href='Employees']/atom:title", xmlString);
    
    assertXpathExists("/a:service/a:workspace/a:collection[@href='Employees']", xmlString);
    assertXpathExists("/a:service/a:workspace/a:collection[@href='Employees']/atom:title", xmlString);
    assertXpathEvaluatesTo("Employees", "/a:service/a:workspace/a:collection[@href='Container2.Employees']/atom:title", xmlString);
  }
  
  @Test
  public void writeServiceDocumentWithOneEnitySetTwoContainersTwoSchemas() throws Exception {
    List<EntitySet> entitySets = new ArrayList<EntitySet>();
    entitySets.add(new EntitySet().setName("Employees"));

    List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
    entityContainers.add(new EntityContainer().setDefaultEntityContainer(true).setName("Container").setEntitySets(entitySets));
    entityContainers.add(new EntityContainer().setDefaultEntityContainer(false).setName("Container2").setEntitySets(entitySets));

    List<EntityContainer> entityContainers2 = new ArrayList<EntityContainer>();
    entityContainers2.add(new EntityContainer().setDefaultEntityContainer(false).setName("Container3").setEntitySets(entitySets));
    entityContainers2.add(new EntityContainer().setDefaultEntityContainer(false).setName("Container4").setEntitySets(entitySets));

    
    schemas.add(new Schema().setEntityContainers(entityContainers));
    schemas.add(new Schema().setEntityContainers(entityContainers2));
    
    ODataResponse response = new AtomEntityProvider().writeServiceDocument(edm, "http://localhost");
    String xmlString = verifyResponse(response);
    assertXpathExists("/a:service/a:workspace/a:collection[@href='Employees']", xmlString);
    assertXpathExists("/a:service/a:workspace/a:collection[@href='Employees']/atom:title", xmlString);
    assertXpathEvaluatesTo("Employees", "/a:service/a:workspace/a:collection[@href='Employees']/atom:title", xmlString);
    
    assertXpathExists("/a:service/a:workspace/a:collection[@href='Employees']", xmlString);
    assertXpathExists("/a:service/a:workspace/a:collection[@href='Employees']/atom:title", xmlString);
    assertXpathEvaluatesTo("Employees", "/a:service/a:workspace/a:collection[@href='Container2.Employees']/atom:title", xmlString);
    
    assertXpathExists("/a:service/a:workspace/a:collection[@href='Employees']", xmlString);
    assertXpathExists("/a:service/a:workspace/a:collection[@href='Employees']/atom:title", xmlString);
    assertXpathEvaluatesTo("Employees", "/a:service/a:workspace/a:collection[@href='Container3.Employees']/atom:title", xmlString);
    
    assertXpathExists("/a:service/a:workspace/a:collection[@href='Employees']", xmlString);
    assertXpathExists("/a:service/a:workspace/a:collection[@href='Employees']/atom:title", xmlString);
    assertXpathEvaluatesTo("Employees", "/a:service/a:workspace/a:collection[@href='Container4.Employees']/atom:title", xmlString);
  }
  
  private String verifyResponse(final ODataResponse response) throws IOException {
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_ATOM_SVC_UTF8, response.getContentHeader());
    String xmlString = StringHelper.inputStreamToString((InputStream) response.getEntity());
    return xmlString;
  }

}
