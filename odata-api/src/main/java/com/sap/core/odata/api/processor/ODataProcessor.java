package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.exception.ODataException;
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

  public Metadata getMetadataProcessor() throws ODataException;

  public ServiceDocument getServiceDocumentProcessor() throws ODataException;

  public Entity getEntityProcessor() throws ODataException;

  public EntitySet getEntitySetProcessor() throws ODataException;

  public EntityComplexProperty getEntityComplexPropertyProcessor() throws ODataException;

  public EntityLink getEntityLinkProcessor() throws ODataException;

  public EntityLinks getEntityLinksProcessor() throws ODataException;

  public EntityMedia getEntityMediaProcessor() throws ODataException;

  public EntitySimpleProperty getEntitySimplePropertyProcessor() throws ODataException;

  public EntitySimplePropertyValue getEntitySimplePropertyValueProcessor() throws ODataException;

  public FunctionImport getFunctionImportProcessor() throws ODataException;

  public FunctionImportValue getFunctionImportValueProcessor() throws ODataException;

  public Batch getBatchProcessor() throws ODataException;

  public void setContext(ODataContext context);

  public ODataContext getContext();

}
