package com.sap.core.odata.api;

import java.util.List;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.BatchProcessor;
import com.sap.core.odata.api.processor.EntityComplexPropertyProcessor;
import com.sap.core.odata.api.processor.EntityLinkProcessor;
import com.sap.core.odata.api.processor.EntityLinksProcessor;
import com.sap.core.odata.api.processor.EntityMediaProcessor;
import com.sap.core.odata.api.processor.EntityProcessor;
import com.sap.core.odata.api.processor.EntitySetProcessor;
import com.sap.core.odata.api.processor.EntitySimplePropertyProcessor;
import com.sap.core.odata.api.processor.EntitySimplePropertyValueProcessor;
import com.sap.core.odata.api.processor.FunctionImportProcessor;
import com.sap.core.odata.api.processor.FunctionImportValueProcessor;
import com.sap.core.odata.api.processor.MetadataProcessor;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ServiceDocumentProcessor;

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
   * @see {@link ODataServiceVersion}
   */
  String getVersion() throws ODataException;

  /**
   * @return entity data model of this service 
   * @see Edm
   * @throws ODataException
   */
  Edm getEntityDataModel() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see MetadataProcessor
   */
  MetadataProcessor getMetadataProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see ServiceDocumentProcessor
   */
  ServiceDocumentProcessor getServiceDocumentProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see EntityProcessor
   */
  EntityProcessor getEntityProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see EntitySetProcessor
   */
  EntitySetProcessor getEntitySetProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see EntityComplexPropertyProcessor
   */
  EntityComplexPropertyProcessor getEntityComplexPropertyProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see EntityLinkProcessor
   */
  EntityLinkProcessor getEntityLinkProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see EntityLinksProcessor
   */
  EntityLinksProcessor getEntityLinksProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see EntityMediaProcessor
   */
  EntityMediaProcessor getEntityMediaProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see EntitySimplePropertyProcessor
   */
  EntitySimplePropertyProcessor getEntitySimplePropertyProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see EntitySimplePropertyValueProcessor
   */
  EntitySimplePropertyValueProcessor getEntitySimplePropertyValueProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see FunctionImportProcessor
   */
  FunctionImportProcessor getFunctionImportProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see FunctionImportValueProcessor
   */
  FunctionImportValueProcessor getFunctionImportValueProcessor() throws ODataException;

  /**
   * @return a processor which handles this request 
   * @throws ODataException
   * @see BatchProcessor
   */
  BatchProcessor getBatchProcessor() throws ODataException;

  /**
   * @return root processor interface 
   * @throws ODataException
   * @see ODataProcessor
   */
  ODataProcessor getProcessor() throws ODataException;

  /**
   * @param processorFeature 
   * @return ordered list of all <code>content types</code> this service supports
   * @throws ODataException
   */
  List<String> getSupportedContentTypes(Class<? extends ODataProcessor> processorFeature) throws ODataException;
}
