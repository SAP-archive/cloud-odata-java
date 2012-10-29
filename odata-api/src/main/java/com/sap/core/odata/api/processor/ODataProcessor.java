package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.exception.ODataError;
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
import com.sap.core.odata.api.processor.aspect.FunctionImportValue;
import com.sap.core.odata.api.processor.aspect.Metadata;
import com.sap.core.odata.api.processor.aspect.ServiceDocument;

public abstract interface ODataProcessor {

  public Metadata getMetadataProcessor() throws ODataError;

  public ServiceDocument getServiceDocumentProcessor() throws ODataError;

  public Entity getEntityProcessor() throws ODataError;

  public EntitySet getEntitySetProcessor() throws ODataError;

  public EntityComplexProperty getEntityComplexPropertyProcessor() throws ODataError;

  public EntityLink getEntityLinkProcessor() throws ODataError;

  public EntityLinks getEntityLinksProcessor() throws ODataError;

  public EntityMedia getEntityMediaProcessor() throws ODataError;

  public EntitySimpleProperty getEntitySimplePropertyProcessor() throws ODataError;

  public EntitySimplePropertyValue getEntitySimplePropertyValueProcessor() throws ODataError;

  public FunctionImport getFunctionImportProcessor() throws ODataError;

  public FunctionImportValue getFunctionImportValueProcessor() throws ODataError;

  public Batch getBatchProcessor() throws ODataError;

  public void setContext(ODataContext context);

  public ODataContext getContext();

}
