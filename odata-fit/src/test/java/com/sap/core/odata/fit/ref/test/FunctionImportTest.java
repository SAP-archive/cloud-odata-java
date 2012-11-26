package com.sap.core.odata.fit.ref.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

public class FunctionImportTest extends AbstractRefTest {

  @Test
  public void test() throws Exception{

    HttpGet request = new HttpGet(this.getEndpoint() + "EmployeeSearch('1')/ne_Room/Id/$value?q='alter'");
    HttpResponse response = this.getHttpClient().execute(request);
    
    assertNotNull(response);
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

}
