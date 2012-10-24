package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.aspect.Batch;
import com.sap.core.odata.api.processor.aspect.Entity;
import com.sap.core.odata.api.processor.aspect.EntityComplexProperty;
import com.sap.core.odata.api.processor.aspect.EntityLink;
import com.sap.core.odata.api.processor.aspect.EntityLinks;
import com.sap.core.odata.api.processor.aspect.EntityMedia;
import com.sap.core.odata.api.processor.aspect.EntitySet;
import com.sap.core.odata.api.processor.aspect.EntitySimpleProperty;
import com.sap.core.odata.api.processor.aspect.EntitySimplePropertyValue;
import com.sap.core.odata.api.processor.aspect.FunctionImport;
import com.sap.core.odata.api.processor.aspect.Metadata;
import com.sap.core.odata.api.processor.aspect.ServiceDocument;
import com.sap.core.odata.api.rest.ODataContext;
import com.sap.core.odata.api.rest.ODataResponse;

public abstract class ODataSingleProcessor
    implements ODataProcessor,
    Metadata,
    ServiceDocument,
    Entity,
    EntitySet,
    EntityComplexProperty,
    EntityLink,
    EntityLinks,
    EntityMedia,
    EntitySimpleProperty,
    EntitySimplePropertyValue,
    FunctionImport,
    Batch
{

  private ODataContext context;
  
  @Override
  public void setContext(ODataContext context) {
    this.context = context;
  }

  @Override
  public ODataContext getContext() {
    return this.context;
  }

  @Override
  public ODataResponse executeBatch() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse executeFunctionImport() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntitySimplePropertyValue() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse updateEntitySimplePropertyValue() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse deleteEntitySimplePropertyValue() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntitySimpleProperty() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse updateEntitySimpleProperty() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntityMedia() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse updateEntityMedia() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse deleteEntityMedia() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntityLinks() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse countEntityLinks() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse createEntityLink() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntityLink() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse existsEntityLink() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse updateEntityLink() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse deleteEntityLink() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntityComplexProperty() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse updateEntityComplexProperty() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntitySet() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse countEntitySet() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse createEntity() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntity() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse existsEntity() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse updateEntity() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse deleteEntity() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readServiceDocument() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public Edm getEdm() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readMetadata() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public Metadata getMetadataProcessor() throws ODataError {
    return this;
  }

  @Override
  public ServiceDocument getServiceDocumentProcessor() throws ODataError {
    return this;
  }

  @Override
  public Entity getEntityProcessor() throws ODataError {
    return this;
  }

  @Override
  public EntitySet getEntitySetProcessor() throws ODataError {
    return this;
  }

  @Override
  public EntityComplexProperty getEntityComplexPropertyProcessor() throws ODataError {
    return this;
  }

  @Override
  public EntityLink getEntityLinkProcessor() throws ODataError {
    return this;
  }

  @Override
  public EntityLinks getEntityLinksProcessor() throws ODataError {
    return this;
  }

  @Override
  public EntityMedia getEntityMediaProcessor() throws ODataError {
    return this;
  }

  @Override
  public EntitySimpleProperty getEntitySimplePropertyProcessor() throws ODataError {
    return this;
  }

  @Override
  public EntitySimplePropertyValue getEntitySimplePropertyValueProcessor() throws ODataError {
    return this;
  }

  @Override
  public FunctionImport getFunctionImportProcessor() throws ODataError {
    return this;
  }

  @Override
  public Batch getBatchProcessor() throws ODataError {
    return this;
  }

}
