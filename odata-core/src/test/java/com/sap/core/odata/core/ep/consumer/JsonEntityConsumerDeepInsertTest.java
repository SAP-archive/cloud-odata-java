package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.testutil.mock.MockFacade;

public class JsonEntityConsumerDeepInsertTest extends AbstractConsumerTest {

  private static final int EMPLOYEE_WITH_INLINE_TEAM = 0;

  @Test(expected = EntityProviderException.class)
  public void test() throws Exception {
    prepareAndExecute(EMPLOYEE_WITH_INLINE_TEAM);
  }

  private ODataEntry prepareAndExecute(final int entryIdentificator) throws IOException, EdmException, ODataException, UnsupportedEncodingException, EntityProviderException {
    EdmEntitySet entitySet = null;
    String content = null;
    switch (entryIdentificator) {
    case EMPLOYEE_WITH_INLINE_TEAM:
      entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
      content = readFile("JsonEmployeeWithInlineTeam");
      break;
    default:
      fail("Invalid entryIdentificator: " + entryIdentificator);
    }

    assertNotNull(content);
    InputStream contentBody = createContentAsStream(content);

    // execute
    JsonEntityConsumer xec = new JsonEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, contentBody, EntityProviderReadProperties.init().mergeSemantic(false).build());
    assertNotNull(result);
    return result;
  }
}
