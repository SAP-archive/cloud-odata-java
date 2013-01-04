package com.sap.core.odata.api.service;

import java.util.List;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.enums.ODataVersion;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
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
import com.sap.core.odata.api.processor.aspect.ProcessorAspect;
import com.sap.core.odata.api.processor.aspect.ServiceDocument;

/**
 * Root interface for a custom OData service.
 * 
 * @author SAP AG
 *
 */
public interface ODataService {

  /**
   * @return implemented OData version of this service
   * @throws ODataException
   * @see ODataVersion
   */
  ODataVersion getODataVersion() throws ODataException;

  /**
   * @return entity data model of this service 
   * @see Edm
   * @throws ODataException
   */
  Edm getEntityDataModel() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see Metadata
   */
  Metadata getMetadataProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see ServiceDocument
   */
  ServiceDocument getServiceDocumentProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see Entity
   */
  Entity getEntityProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see EntitySet
   */
  EntitySet getEntitySetProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see EntityComplexProperty
   */
  EntityComplexProperty getEntityComplexPropertyProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see EntityLink
   */
  EntityLink getEntityLinkProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see EntityLinks
   */
  EntityLinks getEntityLinksProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see EntityMedia
   */
  EntityMedia getEntityMediaProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see EntitySimpleProperty
   */
  EntitySimpleProperty getEntitySimplePropertyProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see EntitySimplePropertyValue
   */
  EntitySimplePropertyValue getEntitySimplePropertyValueProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see FunctionImport
   */
  FunctionImport getFunctionImportProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see FunctionImportValue
   */
  FunctionImportValue getFunctionImportValueProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see Batch
   */
  Batch getBatchProcessor() throws ODataException;

  /**
   * @return root processor interface 
   * @throws ODataException
   * @see ODataProcessor
   */
  ODataProcessor getProcessor() throws ODataException;

  List<String> getSupportedContentTypes(ProcessorAspect processorAspect) throws ODataException;
  
}
