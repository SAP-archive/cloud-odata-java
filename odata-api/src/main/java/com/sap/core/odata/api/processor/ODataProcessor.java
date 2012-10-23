package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.exception.ODataError;
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
import com.sap.core.odata.api.rest.ODataContext;

public abstract interface ODataProcessor {

  public Metadata getMetadataProcessor() throws ODataError;

  public ServiceDocument getServiceDocumentProcessor() throws ODataError;

  public Entity getEntityProcessor() throws ODataError;

  public EntitySet getEntitySetProcessor() throws ODataError;

  public EntityComplexProperties getEntityComplexPropertiesProcessor() throws ODataError;

  public EntityLink getEntityLinkProcessor() throws ODataError;

  public EntityLinks getEntityLinksProcessor() throws ODataError;

  public EntityMedia getEntityMediaProcessor() throws ODataError;

  public EntitySimpleProperty getEntitySimplePropertyProcessor() throws ODataError;

  public EntityValueProperties getEntityValuePropertiesProcessor() throws ODataError;

  public FunctionImport getFunctionImportProcessor() throws ODataError;

  public Batch getBatchProcessor() throws ODataError;

  public void setContext(ODataContext context);

  public ODataContext getContext();

}
