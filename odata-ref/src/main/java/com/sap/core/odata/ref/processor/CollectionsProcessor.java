package com.sap.core.odata.ref.processor;

import java.util.Collection;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.enums.HttpStatus;
import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;
import com.sap.core.odata.api.uri.resultviews.GetEntityView;

public class CollectionsProcessor extends ODataSingleProcessor {

  private final CollectionsDataSource dataSource;

  public CollectionsProcessor(CollectionsDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Edm getEdm() throws ODataError {
    return null;
  }

  @Override
  public ODataResponse readEntitySet(GetEntitySetView uriParserResultView) throws ODataError {
    final EdmEntitySet startEntitySet = uriParserResultView.getStartEntitySet();
    Collection<?> data = dataSource.readDataSet(startEntitySet);
    return ODataResponseBuilder.newInstance().status(HttpStatus.OK).entity(data.toString()).build();
  }

  @Override
  public ODataResponse readEntity(GetEntityView uriParserResultView) throws ODataError {
    final EdmEntitySet startEntitySet = uriParserResultView.getStartEntitySet();
    final Object data = dataSource.readDataObject(startEntitySet, uriParserResultView.getKeyPredicates());

    return ODataResponseBuilder.newInstance().status(HttpStatus.OK).entity(data.toString()).build();
  }
}
