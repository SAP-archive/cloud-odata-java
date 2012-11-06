package com.sap.core.odata.core.service;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.enums.ODataVersion;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
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
import com.sap.core.odata.api.service.ODataService;

public class ODataSingleProcessorService implements ODataService {

  private ODataProcessor processor;
  private Edm edm;
  
  public ODataSingleProcessorService(ODataProcessor processor, Edm edm) {
    if (!(processor instanceof ODataSingleProcessor)) {
      throw new ClassCastException("processor must derive from ODataSingleProcessor");
    }
    this.processor = processor;
    this.edm = edm;
  }

  @Override
  public ODataVersion getODataVersion() throws ODataException {
    return ODataVersion.V20;
  }

  @Override
  public Edm getEntityDataModel() throws ODataException {
    return this.edm;
  }

  @Override
  public Metadata getMetadataProcessor() throws ODataException {
    return (Metadata) this.processor;
  }

  @Override
  public ServiceDocument getServiceDocumentProcessor() throws ODataException {
    return (ServiceDocument) this.processor;
  }

  @Override
  public Entity getEntityProcessor() throws ODataException {
    return (Entity) this.processor;
  }

  @Override
  public EntitySet getEntitySetProcessor() throws ODataException {
    return (EntitySet) this.processor;
  }

  @Override
  public EntityComplexProperty getEntityComplexPropertyProcessor() throws ODataException {
    return (EntityComplexProperty) this.processor;
  }

  @Override
  public EntityLink getEntityLinkProcessor() throws ODataException {
    return (EntityLink) this.processor;
  }

  @Override
  public EntityLinks getEntityLinksProcessor() throws ODataException {
    return (EntityLinks) this.processor;
  }

  @Override
  public EntityMedia getEntityMediaProcessor() throws ODataException {
    return (EntityMedia) this.processor;
  }

  @Override
  public EntitySimpleProperty getEntitySimplePropertyProcessor() throws ODataException {
    return (EntitySimpleProperty) this.processor;
  }

  @Override
  public EntitySimplePropertyValue getEntitySimplePropertyValueProcessor() throws ODataException {
    return (EntitySimplePropertyValue) this.processor;
  }

  @Override
  public FunctionImport getFunctionImportProcessor() throws ODataException {
    return (FunctionImport) this.processor;
  }

  @Override
  public FunctionImportValue getFunctionImportValueProcessor() throws ODataException {
    return (FunctionImportValue) this.processor;
  }

  @Override
  public Batch getBatchProcessor() throws ODataException {
    return (Batch) this.processor;
  }
}
