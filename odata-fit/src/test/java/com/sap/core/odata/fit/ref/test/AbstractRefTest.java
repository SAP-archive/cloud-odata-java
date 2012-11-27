package com.sap.core.odata.fit.ref.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.ref.edm.ScenarioEdmProvider;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.processor.ListsProcessor;
import com.sap.core.odata.ref.processor.ScenarioDataSource;
import com.sap.core.odata.testutils.fit.AbstractFitTest;

public class AbstractRefTest extends AbstractFitTest {

  //  protected static final String EMPLOYEE_1_NAME = "Walter Winter";
  //protected static final String EMPLOYEE_2_NAME = "Frederic Fall";
  //  protected static final String EMPLOYEE_3_NAME = "Jonathan Smith";
  //  protected static final String EMPLOYEE_4_NAME = "Peter Burke";
  //  protected static final String EMPLOYEE_5_NAME = "John Field";
  //  protected static final String EMPLOYEE_6_NAME = "Susan Bay";
  //  protected static final String MANAGER_NAME = EMPLOYEE_1_NAME;

  protected static final String EMPLOYEE_2_AGE = "32";
  protected static final String EMPLOYEE_3_AGE = "56";

  //  protected static final String EMPLOYEE_6_AGE = "29";
  
  protected static final String CITY_2_NAME = "Walldorf";

  @Override
  protected EdmProvider createEdmProviderMock() {

    return new ScenarioEdmProvider();
  }

  @Override
  protected ODataSingleProcessor createProcessorMock() throws ODataException {
    DataContainer dataContainer = new DataContainer();
    dataContainer.reset();
    return new ListsProcessor(new ScenarioDataSource(dataContainer));
  }

  protected HttpResponse callUri(String uri, HttpStatusCodes expectedStatusCode) throws Exception {
    HttpGet request = new HttpGet(this.getEndpoint() + uri);
    HttpResponse response = this.getHttpClient().execute(request);

    assertNotNull(response);
    assertEquals(expectedStatusCode.getStatusCode(), response.getStatusLine().getStatusCode());
    return response;
  }

}
