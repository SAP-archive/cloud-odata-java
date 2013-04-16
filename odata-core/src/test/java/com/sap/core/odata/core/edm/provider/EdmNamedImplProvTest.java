package com.sap.core.odata.core.edm.provider;

import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.testutil.fit.BaseTest;

public class EdmNamedImplProvTest extends BaseTest {

  @Test(expected = EdmException.class)
  public void testPropertySimple() throws Exception {

    EdmProvider edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);

    SimpleProperty propertySimple = new SimpleProperty().setName("Prop;ertyName").setType(EdmSimpleTypeKind.String);
    new EdmSimplePropertyImplProv(edmImplProv, propertySimple);
  }
}
