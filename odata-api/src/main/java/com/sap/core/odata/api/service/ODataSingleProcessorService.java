package com.sap.core.odata.api.service;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.enums.ODataVersion;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.ContentTypeSupport;
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
import com.sap.core.odata.api.processor.aspect.ProcessorAspect;
import com.sap.core.odata.api.processor.aspect.ServiceDocument;
import com.sap.core.odata.api.rt.RuntimeDelegate;

/**
 * <p>An {@link ODataService} implementation that uses {@link ODataSingleProcessor}.</p>
 * <p>Usually custom services create an instance by their implementation of
 * {@link ODataServiceFactory} and populate it with their custom {@link EdmProvider}
 * and custom {@link ODataSingleProcessor} implementations.</p>
 *
 * @author SAP AG
 */
public class ODataSingleProcessorService implements ODataService {

  private ODataSingleProcessor processor;
  private Edm edm;

  /**
   * Construct service
   * @param provider A custom {@link EdmProvider}
   * @param processor A custom {@link ODataSingleProcessor}
   */
  public ODataSingleProcessorService(EdmProvider provider, ODataSingleProcessor processor) {
    this.processor = processor;
    this.edm = RuntimeDelegate.createEdm(provider);
  }

  /**
   * @see ODataService
   */
  @Override
  public ODataVersion getODataVersion() throws ODataException {
    return ODataVersion.V20;
  }

  /**
   * @see ODataService
   */
  @Override
  public Edm getEntityDataModel() throws ODataException {
    return this.edm;
  }

  /**
   * @see ODataService
   */
  @Override
  public Metadata getMetadataProcessor() throws ODataException {
    return this.processor;
  }

  /**
   * @see ODataService
   */
  @Override
  public ServiceDocument getServiceDocumentProcessor() throws ODataException {
    return this.processor;
  }

  /**
   * @see ODataService
   */
  @Override
  public Entity getEntityProcessor() throws ODataException {
    return this.processor;
  }

  /**
   * @see ODataService
   */
  @Override
  public EntitySet getEntitySetProcessor() throws ODataException {
    return this.processor;
  }

  /**
   * @see ODataService
   */
  @Override
  public EntityComplexProperty getEntityComplexPropertyProcessor() throws ODataException {
    return this.processor;
  }

  /**
   * @see ODataService
   */
  @Override
  public EntityLink getEntityLinkProcessor() throws ODataException {
    return this.processor;
  }

  /**
   * @see ODataService
   */
  @Override
  public EntityLinks getEntityLinksProcessor() throws ODataException {
    return this.processor;
  }

  /**
   * @see ODataService
   */
  @Override
  public EntityMedia getEntityMediaProcessor() throws ODataException {
    return this.processor;
  }

  /**
   * @see ODataService
   */
  @Override
  public EntitySimpleProperty getEntitySimplePropertyProcessor() throws ODataException {
    return this.processor;
  }

  /**
   * @see ODataService
   */
  @Override
  public EntitySimplePropertyValue getEntitySimplePropertyValueProcessor() throws ODataException {
    return this.processor;
  }

  /**
   * @see ODataService
   */
  @Override
  public FunctionImport getFunctionImportProcessor() throws ODataException {
    return this.processor;
  }

  /**
   * @see ODataService
   */
  @Override
  public FunctionImportValue getFunctionImportValueProcessor() throws ODataException {
    return this.processor;
  }

  /**
   * @see ODataService
   */
  @Override
  public Batch getBatchProcessor() throws ODataException {
    return this.processor;
  }

  /**
   * @see ODataService
   */
  @Override
  public ODataProcessor getProcessor() throws ODataException {
    return this.processor;
  }

  @Override
  public List<ContentType> getSupportedContentTypes(ProcessorAspect processorAspect) throws ODataException {
    List<ContentType> result = new ArrayList<ContentType>();

    switch (processorAspect) {
    case BATCH:
      result.add(ContentType.MULTIPART_MIXED);
      break;
    case ENTITY:
      result.add(ContentType.APPLICATION_ATOM_XML_ENTRY);
      result.add(ContentType.APPLICATION_ATOM_XML);
      result.add(ContentType.APPLICATION_JSON);
      break;
    case FUNCTION_IMPORT:
    case ENTITY_LINK:
    case ENTITY_LINKS:
    case ENTITY_SIMPLE_PROPERTY:
    case ENTITY_COMPLEX_PROPERTY:
      result.add(ContentType.APPLICATION_XML);
      result.add(ContentType.APPLICATION_JSON);
      break;
    case ENTITY_MEDIA:
    case ENTITY_SIMPLE_PROPERTY_VALUE:
    case FUNCTION_IMPORT_VALUE:
      result.add(ContentType.WILDCARD);
      break;
    case ENTITY_SET:
      result.add(ContentType.APPLICATION_ATOM_XML_FEED);
      result.add(ContentType.APPLICATION_ATOM_XML);
      result.add(ContentType.APPLICATION_JSON);
      break;
    case METDDATA:
      result.add(ContentType.APPLICATION_XML);
      break;
    case SERVICE_DOCUMENT:
      result.add(ContentType.APPLICATION_ATOM_SVC);
      result.add(ContentType.APPLICATION_JSON);
      result.add(ContentType.APPLICATION_XML);
      break;
    default:
      throw new ODataNotImplementedException();
    }

    if (processor instanceof ContentTypeSupport) {
      ContentTypeSupport cts = (ContentTypeSupport) processor;
      result.addAll(cts.getSupportedContentTypes(processorAspect));
    }

    return result;
  }
}
