package com.sap.core.odata.ref.processor;

import java.util.Collection;
import java.util.HashMap;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.enums.HttpStatus;
import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;
import com.sap.core.odata.api.uri.resultviews.GetEntityView;

/**
 * Implementation of the centralized parts of OData processing,
 * allowing to use the simplified {@link CollectionsDataSource}
 * for the actual data handling
 * @author SAP AG
 */
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
    Collection<?> data = dataSource.readData(startEntitySet);
    return ODataResponseBuilder.newInstance().status(HttpStatus.OK).entity(data.toString()).build();
  }

  @Override
  public ODataResponse readEntity(GetEntityView uriParserResultView) throws ODataError {
    final EdmEntitySet startEntitySet = uriParserResultView.getStartEntitySet();
    HashMap<String, Object> keys = new HashMap<String, Object>();
    for (KeyPredicate key : uriParserResultView.getKeyPredicates()) {
      final EdmProperty property = key.getProperty();
      final EdmSimpleType type = (EdmSimpleType) property.getType();
      keys.put(property.getName(), type.valueOfString(key.getLiteral(), null, property.getFacets()));
    }

    final Object data = dataSource.readData(startEntitySet, keys);

    return ODataResponseBuilder.newInstance().status(HttpStatus.OK).entity(data.toString()).build();
  }
}
