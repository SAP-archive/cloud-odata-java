package com.sap.core.odata.ref.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.enums.HttpStatus;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.resultviews.GetMetadataView;
import com.sap.core.odata.api.uri.resultviews.GetServiceDocumentView;
import com.sap.core.testutils.mocks.MockFacade;

public class ScenarioProcessor extends ODataSingleProcessor {

  private static final Logger log = LoggerFactory.getLogger(ScenarioProcessor.class);

  @Override
  public Edm getEntityDataModel() throws ODataException {
    return MockFacade.getMockEdm();
  }

  @Override
  public ODataResponse readServiceDocument(GetServiceDocumentView uriParserResultView) throws ODataException {
    return ODataResponse.status(HttpStatus.OK).entity("this is service document").build();
  }

  @Override
  public ODataResponse readMetadata(GetMetadataView uriParserResultView) throws ODataException {
    return ODataResponse.status(HttpStatus.OK).entity("this is metadata").build();
  }

}
