package com.sap.core.odata.api;

import java.util.List;

import com.sap.core.odata.api.commons.ODataVersion;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.feature.Batch;
import com.sap.core.odata.api.processor.feature.Entity;
import com.sap.core.odata.api.processor.feature.EntityComplexProperty;
import com.sap.core.odata.api.processor.feature.EntityLink;
import com.sap.core.odata.api.processor.feature.EntityLinks;
import com.sap.core.odata.api.processor.feature.EntityMedia;
import com.sap.core.odata.api.processor.feature.EntitySet;
import com.sap.core.odata.api.processor.feature.EntitySimpleProperty;
import com.sap.core.odata.api.processor.feature.EntitySimplePropertyValue;
import com.sap.core.odata.api.processor.feature.FunctionImport;
import com.sap.core.odata.api.processor.feature.FunctionImportValue;
import com.sap.core.odata.api.processor.feature.Metadata;
import com.sap.core.odata.api.processor.feature.ProcessorFeature;
import com.sap.core.odata.api.processor.feature.ServiceDocument;

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

  List<String> getSupportedContentTypes(ProcessorFeature processorAspect) throws ODataException;
  
}
