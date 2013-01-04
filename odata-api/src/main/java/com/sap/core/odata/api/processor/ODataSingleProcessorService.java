package com.sap.core.odata.api.processor;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.commons.ODataVersion;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.feature.Batch;
import com.sap.core.odata.api.processor.feature.ContentTypeSupport;
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

  private static final String WILDCARD = "*/*";
  private static final String MULTIPART_MIXED = "multipart/mixed";
  private static final String APPLICATION_XML = "application/xml";
  private static final String APPLICATION_ATOM_XML = "application/atom+xml";
  private static final String APPLICATION_ATOM_XML_ENTRY = "application/atom+xml; type=entry";
  private static final String APPLICATION_ATOM_XML_FEED = "application/atom+xml; type=feed";
  private static final String APPLICATION_ATOM_SVC = "application/atomsvc+xml";
  private static final String APPLICATION_JSON = "application/json";
  
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
  public List<String> getSupportedContentTypes(ProcessorFeature processorAspect) throws ODataException {
    List<String> result = new ArrayList<String>();

    switch (processorAspect) {
    case BATCH:
      result.add(MULTIPART_MIXED);
      break;
    case ENTITY:
      result.add(APPLICATION_ATOM_XML_ENTRY);
      result.add(APPLICATION_ATOM_XML);
      result.add(APPLICATION_JSON);
      break;
    case FUNCTION_IMPORT:
    case ENTITY_LINK:
    case ENTITY_LINKS:
    case ENTITY_SIMPLE_PROPERTY:
    case ENTITY_COMPLEX_PROPERTY:
      result.add(APPLICATION_XML);
      result.add(APPLICATION_JSON);
      break;
    case ENTITY_MEDIA:
    case ENTITY_SIMPLE_PROPERTY_VALUE:
    case FUNCTION_IMPORT_VALUE:
      result.add(WILDCARD);
      break;
    case ENTITY_SET:
      result.add(APPLICATION_ATOM_XML_FEED);
      result.add(APPLICATION_ATOM_XML);
      result.add(APPLICATION_JSON);
      break;
    case METDDATA:
      result.add(APPLICATION_XML);
      break;
    case SERVICE_DOCUMENT:
      result.add(APPLICATION_ATOM_SVC);
      result.add(APPLICATION_JSON);
      result.add(APPLICATION_XML);
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
