package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.facet.Batch;
import com.sap.core.odata.api.processor.facet.Entity;
import com.sap.core.odata.api.processor.facet.EntityComplexProperties;
import com.sap.core.odata.api.processor.facet.EntityLink;
import com.sap.core.odata.api.processor.facet.EntityLinks;
import com.sap.core.odata.api.processor.facet.EntityMedia;
import com.sap.core.odata.api.processor.facet.EntitySet;
import com.sap.core.odata.api.processor.facet.EntitySimpleProperty;
import com.sap.core.odata.api.processor.facet.EntityValueProperties;
import com.sap.core.odata.api.processor.facet.FunctionImport;
import com.sap.core.odata.api.processor.facet.Metadata;
import com.sap.core.odata.api.processor.facet.ServiceDocument;
import com.sap.core.odata.api.rest.ODataResponse;

public abstract class ODataSingleProcessor
    implements ODataProcessor,
    Metadata,
    ServiceDocument,
    Entity,
    EntitySet,
    EntityComplexProperties,
    EntityLink,
    EntityLinks,
    EntityMedia,
    EntitySimpleProperty,
    EntityValueProperties,
    FunctionImport,
    Batch
{

  @Override
  public ODataResponse executeBatch() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse executeFunctionImport() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntityValueProperties() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse updateEntityValueProperties() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse deleteEntityValueProperties() throws ODataError {
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
  public ODataResponse createEntityLinks() throws ODataError {
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
  public ODataResponse readEntityComplexProperties() throws ODataError {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse updateEntityComplexProperties() throws ODataError {
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
  public ODataResponse createEntitySet() throws ODataError {
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
  public EntityComplexProperties getEntityComplexPropertiesProcessor() throws ODataError {
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
  public EntityValueProperties getEntityValuePropertiesProcessor() throws ODataError {
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
