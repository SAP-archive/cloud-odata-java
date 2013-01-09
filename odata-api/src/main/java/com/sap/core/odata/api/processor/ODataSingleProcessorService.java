package com.sap.core.odata.api.processor;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
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
  public ODataServiceVersion getVersion() throws ODataException {
    return ODataServiceVersion.V20;
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
  public List<String> getSupportedContentTypes(Class<? extends ProcessorFeature> processorFeature) throws ODataException {
    List<String> result = new ArrayList<String>();

    result.addAll(processor.getCustomContentTypes(processorFeature));

    if (processorFeature == Batch.class) {
      result.add(HttpContentType.MULTIPART_MIXED);
    } else if (processorFeature == Entity.class) {
      result.add(HttpContentType.APPLICATION_ATOM_XML_ENTRY);
      result.add(HttpContentType.APPLICATION_ATOM_XML);
      result.add(HttpContentType.APPLICATION_JSON);
      result.add(HttpContentType.APPLICATION_XML);
    } else if (processorFeature == FunctionImport.class
        || processorFeature == EntityLink.class
        || processorFeature == EntityLinks.class
        || processorFeature == EntitySimpleProperty.class
        || processorFeature == EntityComplexProperty.class) {
      result.add(HttpContentType.APPLICATION_XML);
      result.add(HttpContentType.APPLICATION_JSON);
    } else if (processorFeature == EntityMedia.class
        || processorFeature == EntitySimplePropertyValue.class
        || processorFeature == FunctionImportValue.class) {
      result.add(HttpContentType.WILDCARD);
    } else if (processorFeature == EntitySet.class) {
      result.add(HttpContentType.APPLICATION_ATOM_XML_FEED);
      result.add(HttpContentType.APPLICATION_ATOM_XML);
      result.add(HttpContentType.APPLICATION_JSON);
      result.add(HttpContentType.APPLICATION_XML);
    } else if (processorFeature == Metadata.class) {
      result.add(HttpContentType.APPLICATION_XML);
    } else if (processorFeature == ServiceDocument.class) {
      result.add(HttpContentType.APPLICATION_ATOM_SVC);
      result.add(HttpContentType.APPLICATION_JSON);
      result.add(HttpContentType.APPLICATION_XML);
    } else {
      throw new ODataNotImplementedException();
    }

    return result;
  }
}
