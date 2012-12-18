package com.sap.core.odata.fit.basic;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.service.ODataSingleProcessorService;
import com.sap.core.odata.ref.edm.ScenarioEdmProvider;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.processor.ListsProcessor;
import com.sap.core.odata.ref.processor.ScenarioDataSource;
import com.sap.core.odata.testutils.fit.AbstractFitTest;
import com.sap.core.odata.testutils.helper.StringHelper;
import com.sap.core.odata.testutils.helper.XMLUnitHelper;

public class AtomEntryEncodingTest extends AbstractFitTest {

  @Override
  protected ODataSingleProcessorService createService() throws ODataException {
    DataContainer dataContainer = new DataContainer();
    dataContainer.reset();
    ODataSingleProcessor processor = new ListsProcessor(new ScenarioDataSource(dataContainer));
    EdmProvider provider = new ScenarioEdmProvider();
    return new ODataSingleProcessorService(provider, processor) {};
  }

  @Before
  public void before() throws Exception {
    super.before();

    Map<String, String> ns = new HashMap<String, String>();
    ns.put("d", Edm.NAMESPACE_D_2007_08);
    ns.put("m", Edm.NAMESPACE_M_2007_08);
    ns.put("a", Edm.NAMESPACE_ATOM_2005);
    XMLUnitHelper.registerXmlNs(ns);
  }

  @Test
  public void testETagEncoding() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "Rooms('1')"));
    HttpResponse response = this.getHttpClient().execute(get);

    String xml = StringHelper.inputStreamToString(response.getEntity().getContent());

    assertXpathEvaluatesTo("W/\"1\"", "/a:entry/@m:etag", xml);
  }
}
