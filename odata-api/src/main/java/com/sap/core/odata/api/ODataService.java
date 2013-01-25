package com.sap.core.odata.api;

import java.util.List;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.part.BatchProcessor;
import com.sap.core.odata.api.processor.part.EntityComplexPropertyProcessor;
import com.sap.core.odata.api.processor.part.EntityLinkProcessor;
import com.sap.core.odata.api.processor.part.EntityLinksProcessor;
import com.sap.core.odata.api.processor.part.EntityMediaProcessor;
import com.sap.core.odata.api.processor.part.EntityProcessor;
import com.sap.core.odata.api.processor.part.EntitySetProcessor;
import com.sap.core.odata.api.processor.part.EntitySimplePropertyProcessor;
import com.sap.core.odata.api.processor.part.EntitySimplePropertyValueProcessor;
import com.sap.core.odata.api.processor.part.FunctionImportProcessor;
import com.sap.core.odata.api.processor.part.FunctionImportValueProcessor;
import com.sap.core.odata.api.processor.part.MetadataProcessor;
import com.sap.core.odata.api.processor.part.ServiceDocumentProcessor;

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
