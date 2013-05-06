package com.sap.core.odata.processor.core.jpa.access.data;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmParameter;
import com.sap.core.odata.api.edm.provider.Mapping;
import com.sap.core.odata.api.uri.info.GetFunctionImportUriInfo;
import com.sap.core.odata.processor.api.jpa.access.JPAMethodContext;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPAModelException;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.jpql.JPQLContextType;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;
import com.sap.core.odata.processor.core.jpa.model.JPAEdmMappingImpl;

public class JPAFunctionContextTest {

  private int VARIANT = 0;

  public JPAFunctionContext build() {
    JPAFunctionContext functionContext = null;
    try {
      if (VARIANT == 0) {
        functionContext = (JPAFunctionContext) JPAMethodContext
            .createBuilder(JPQLContextType.FUNCTION, getView())
            .build();
      }

    } catch (ODataJPAModelException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (ODataJPARuntimeException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }

    return functionContext;
  }

  @Test
  public void testGetEnclosingObject() {

    VARIANT = 0;

    Assert.assertNotNull(build());

  }

  private GetFunctionImportUriInfo getView() {
    GetFunctionImportUriInfo functiontView = EasyMock
        .createMock(GetFunctionImportUriInfo.class);
    EasyMock.expect(functiontView.getFunctionImport()).andStubReturn(
        getEdmFunctionImport());
    EasyMock.expect(functiontView.getFunctionImportParameters())
        .andStubReturn(getFunctionImportParameters());

    EasyMock.replay(functiontView);
    return functiontView;
  }

  private Map<String, EdmLiteral> getFunctionImportParameters() {
    return null;
  }

  private EdmFunctionImport getEdmFunctionImport() {
    EdmFunctionImport edmFunctionImport = EasyMock
        .createMock(EdmFunctionImport.class);
    try {
      EasyMock.expect(edmFunctionImport.getMapping()).andStubReturn(
          getMapping());
      EasyMock.expect(edmFunctionImport.getParameterNames())
          .andStubReturn(getParameterNames());
      EasyMock.expect(edmFunctionImport.getParameter("Gentleman"))
          .andStubReturn(getParameter("Gentleman"));
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }

    EasyMock.replay(edmFunctionImport);
    return edmFunctionImport;
  }

  private EdmParameter getParameter(final String string) {
    EdmParameter edmParameter = EasyMock.createMock(EdmParameter.class);
    try {
      EasyMock.expect(edmParameter.getMapping()).andStubReturn(
          getEdmMapping());
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    EasyMock.replay(edmParameter);
    return edmParameter;
  }

  private EdmMapping getEdmMapping() {
    JPAEdmMappingImpl mapping = new JPAEdmMappingImpl();
    mapping.setJPAType(String.class);
    ((Mapping) mapping).setInternalName("Gentleman");
    return mapping;
  }

  private JPAEdmMappingImpl getMapping() {
    JPAEdmMappingImpl mapping = new JPAEdmMappingImpl();
    mapping.setJPAType(FunctionImportTestClass.class);
    ((Mapping) mapping).setInternalName("testMethod");
    return mapping;
  }

  private Collection<String> getParameterNames() {
    Collection<String> parametersList = new ArrayList<String>();
    parametersList.add("Gentleman");
    return parametersList;
  }

  public static class FunctionImportTestClass {

    public FunctionImportTestClass() {

    }

    public String testMethod(final String message) {
      return "Hello " + message + "!!";
    }
  }
}
